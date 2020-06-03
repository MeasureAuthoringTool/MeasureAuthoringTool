package mat.cql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import mat.client.umls.service.VSACAPIService;
import mat.client.umls.service.VsacApiResult;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLCodeSystem;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.server.CQLKeywordsUtil;
import mat.server.MappingSpreadsheetService;
import mat.server.service.CodeListService;
import mat.server.service.VSACApiService;
import mat.shared.CQLError;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.StringUtils;
import org.cqframework.cql.gen.cqlLexer;
import org.cqframework.cql.gen.cqlParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static mat.cql.CqlUtils.chomp1;
import static mat.cql.CqlUtils.getGlobalLibId;
import static mat.cql.CqlUtils.isQuoted;
import static mat.cql.CqlUtils.newGuid;
import static mat.cql.CqlUtils.parseCodeSystemName;
import static mat.cql.CqlUtils.parseOid;

/**
 * A CqlVisitor for converting FHIR to MatXml format without a source model.
 * This version determines the correct ValueSets, Codes, and CodeSystems from the MAT DB.
 */
@Getter
@Setter
@ToString
@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CqlToMatXml implements CqlVisitor {
    /**
     * %1s is the code system oid.
     * %2s is the code system version.
     * %3s os the code value.
     */
    private static final String CODE_IDENTIFIER_FORMAT = "CODE:/CodeSystem/%1$s/Version/%2$s/Code/%3$s/Info";
    @Value("${mat.codesystem.valueset.simultaneous.validations}")
    private int simultaneousValidations;
    private List<CQLIncludeLibrary> libsNotFound = new ArrayList<>();
    private List<CQLError> errors = new ArrayList<>();
    private List<CQLError> warnings = new ArrayList<>();
    private CQLModel destinationModel = new CQLModel();
    private CQLModel sourceModel;
    private CodeListService codeListService;
    private MappingSpreadsheetService mappingService;
    private boolean isValidatingCodesystems = true;
    private boolean isValidatingValuesets = true;
    private ExecutorService codeSystemValueSetExecutor;
    private VSACApiService vsacService;
    private VSACAPIService vsacApiService;

    @Data
    @AllArgsConstructor
    private static class CodeValidation {
        private CQLCode code;
        private boolean isValid;
    }

    @Data
    private class CodeValidator implements Callable<CodeValidation> {
        private CQLCode code;
        private boolean isValid;

        public CodeValidator(CQLCode code) {
            this.code = code;
        }

        @Override
        public CodeValidation call() {
            log.info("Validating code {} with vsac.",code.getCodeIdentifier());
            String sessionId = vsacApiService.getSessionId();
            if (StringUtils.isBlank(sessionId)) {
                log.info("No UMLS session, code validation is false for {}.",code.getCodeIdentifier());
                code.setValidatedWithVsac(false);
            } else {
                VsacApiResult r = vsacService.getDirectReferenceCode(code.getCodeIdentifier(), sessionId);
                code.setValidatedWithVsac(r.isSuccess());
            }
            log.info("Validated code {} with vsac. {}", code.getCodeIdentifier(), code.isValidatedWithVsac());
            return new CodeValidation(code, code.isValidatedWithVsac());
        }
    }

    @Data
    @AllArgsConstructor
    private static class ValueSetValidation {
        private CQLQualityDataSetDTO valueSet;
        private boolean isValid;
    }

    @Data
    private class ValueSetValidator implements Callable<ValueSetValidation> {
        private CQLQualityDataSetDTO valueSet;

        public ValueSetValidator(CQLQualityDataSetDTO valueSet) {
            this.valueSet = valueSet;
        }

        @Override
        public ValueSetValidation call() {
            String sessionId = vsacApiService.getSessionId();
            String oid = parseOid(valueSet.getOid());
            if (StringUtils.isBlank(sessionId)) {
                log.info("Validating valueset {} with vsac.",oid);
                VsacApiResult r = vsacService.getMostRecentValueSetByOID(oid,null,null,sessionId);
                valueSet.setValidatedWithVsac(r.isSuccess());
            } else {
                log.info("No UMLS session, valueset validation is false for {}.",oid);
                valueSet.setValidatedWithVsac(false);
            }
            log.info("Validated valueset {} with vsac. {}",oid,valueSet.isValidatedWithVsac());
            return new ValueSetValidation(valueSet, valueSet.isValidatedWithVsac());
        }
    }

    public CqlToMatXml(CodeListService codeListService,
                       MappingSpreadsheetService mappingService) {
        this.codeListService = codeListService;
        this.mappingService = mappingService;
    }

    @PostConstruct
    protected void setupExecutor() {
        codeSystemValueSetExecutor = Executors.newFixedThreadPool(simultaneousValidations);
    }

    @Override
    public void handleError(CQLError e) {
        if (StringUtils.equalsIgnoreCase("Error", e.getSeverity())) {
            errors.add(e);
        } else if (StringUtils.equalsIgnoreCase("Warning", e.getSeverity())) {
            warnings.add(e);
        }
    }

    @Override
    public void validateBeforeParse() {
        if (sourceModel == null) {
            //If source model is null just create a new empty CQLModel.
            sourceModel = new CQLModel();
        }
    }

    @Override
    public void validateAfterParse() {
        if (sourceModel == null) {
            //Validate valuesets and codesystems.
            if (isValidatingCodesystems) {
                List<CodeValidator> codeTasks = destinationModel.getCodeList().stream()
                        .filter(c -> !c.isValidatedWithVsac() &&
                                !StringUtils.contains(c.getCodeSystemOID(),"NOT.IN.VSAC")).
                                map(CodeValidator::new).
                                collect(Collectors.toList());

                // Mark all the non cancelled returned ones with the status returned from VSAC.
                try {
                    codeSystemValueSetExecutor.invokeAll(codeTasks, 2, TimeUnit.MINUTES).stream().
                            filter(c -> !c.isCancelled()).
                            forEach(v -> {
                                try {
                                    v.get().getCode().setValidatedWithVsac(v.get().isValid());
                                } catch (InterruptedException | ExecutionException e) {
                                    log.info("Error getting vs.", e);
                                }
                            });
                } catch (InterruptedException ie) {
                    log.info("Error in invokeAll in Executor", ie);

                }
            }
            if (isValidatingValuesets) {
                List<ValueSetValidator> valuesetTasks = destinationModel.getValueSetList().stream().
                        filter(v -> !v.isValidatedWithVsac()).
                        map(ValueSetValidator::new).
                        collect(Collectors.toList());

                // Mark all the non cancelled returned ones with the status returned from VSAC.
                try {
                    codeSystemValueSetExecutor.invokeAll(valuesetTasks, 2, TimeUnit.MINUTES).stream().
                            filter(v -> !v.isCancelled()).
                            forEach(v -> {
                                try {
                                    v.get().getValueSet().setValidatedWithVsac(v.get().isValid());
                                } catch (InterruptedException | ExecutionException e) {
                                    log.info("Error getting vs.", e);
                                }
                            });
                } catch (InterruptedException ie) {
                    log.info("Error in invokeAll in Executor", ie);
                }
            }
        }
    }

    @Override
    public void libraryTag(String libraryName, String version) {
        destinationModel.setLibraryName(libraryName);
        destinationModel.setVersionUsed(version);
    }

    @Override
    public void fhirVersion(String fhirVersion) {
        destinationModel.setUsingModelVersion(fhirVersion);
        destinationModel.setUsingModel("FHIR");
    }

    @Override
    public void includeLib(String libName, String version, String alias, String model, String modelVersion) {
        CQLIncludeLibrary lib = new CQLIncludeLibrary();
        lib.setId(newGuid());
        lib.setCqlLibraryName(libName);
        lib.setVersion(version);
        lib.setAliasName(alias);
        lib.setLibraryModelType(model);
        lib.setQdmVersion(modelVersion);
        lib.setCqlLibraryId(getGlobalLibId(libName));
        lib.setCqlLibraryId(getGlobalLibId(lib.getCqlLibraryName()));
        destinationModel.getCqlIncludeLibrarys().add(lib);
    }

    @Override
    public void codeSystem(String name, String uri, String versionUri) {
        var parsedCodeSystemName = parseCodeSystemName(name);
        // Backward compatibility this is ugly.
        // versionUri is a new concept in fhir4 yet they have a version already
        // that is after the colon in the system name.
        CQLCodeSystem cs = new CQLCodeSystem();
        cs.setId(newGuid());
        cs.setCodeSystemName(name);
        cs.setCodeSystemVersion(StringUtils.defaultString(parsedCodeSystemName.getRight()));
        cs.setCodeSystem(uri);
        cs.setVersionUri(versionUri);
        destinationModel.getCodeSystemList().add(cs);
    }

    @Override
    public void valueSet(String name, String uri) {
        var vs = new CQLQualityDataSetDTO();
        vs.setId(newGuid());
        vs.setName(name);
        vs.setUuid(newGuid());
        vs.setOid(parseOid(uri));

        // Nonsensical legacy stuff that has to be here for the gwt part to function at the moment.
        vs.setOriginalCodeListName(name);
        vs.setSuppDataElement(false);
        vs.setTaxonomy("Grouping");
        vs.setType("Grouping");

        var existingValueSet = findExisting(sourceModel.getValueSetList(),
                evs -> StringUtils.equals(parseOid(evs.getOid()), vs.getOid()));
        vs.setValidatedWithVsac(existingValueSet.map(CQLQualityDataSetDTO::isValidatedWithVsac).orElse(false));

        destinationModel.getValueSetList().add(vs);
    }

    @Override
    public void code(String name, String code, String codeSystemName, String displayName) {
        var c = new CQLCode();
        c.setCodeSystemName(codeSystemName);
        c.setCodeOID(code);
        c.setDisplayName(StringUtils.isEmpty(displayName) ? name : displayName);
        c.setCodeName(name);
        c.setId(newGuid());
        updateCodeSystemInfo(c);
        updateCodeIdentifierAndVsacValidationFlag(c);
        destinationModel.getCodeList().add(c);
    }

    @Override
    public void parameter(String name, String logic, String comment) {
        var existingParam = findExisting(sourceModel.getCqlParameters(),
                p -> StringUtils.equals(p.getName(), name));

        CQLParameter p = new CQLParameter();
        p.setId(newGuid());
        p.setName(name);
        p.setParameterLogic(logic);
        p.setCommentString(comment);
        if (existingParam.isPresent()) {
            p.setReadOnly(existingParam.get().isReadOnly());
        } else {
            p.setReadOnly(false);
        }

        destinationModel.getCqlParameters().add(p);
    }

    @Override
    public void context(String context) {
        destinationModel.setContext(context);
    }

    @Override
    public void definition(String name, String logic, String comment) {
        destinationModel.getDefinitionList().add(buildCQLDef(name, logic, comment));
    }

    @Override
    public void function(String name, List<FunctionArgument> args, String logic, String comment) {
        var f = new CQLFunctions();
        f.setId(newGuid());
        f.setName(name);
        f.setLogic(logic);
        f.setCommentString(comment);
        f.setArgumentList(args.stream().map(a -> {
            CQLFunctionArgument argument = new CQLFunctionArgument();
            argument.setId(newGuid());
            argument.setArgumentName(a.getName());
            if (mappingService.getFhirTypes().contains(a.getType())) {
                //Reuse QdmDataType for FhirDataType. This isn't ideal but its a real pain to change mat XML.
                argument.setQdmDataType(a.getType());
                argument.setArgumentType("FHIR Datatype");
            } else if (isQuoted(a.getType())) {
                //In conversions we will still have quoted strings here for QDM type.
                argument.setQdmDataType(chomp1(a.getType()));
                argument.setArgumentType("QDM Datatype");
            } else if (CQLKeywordsUtil.getCQLKeywords().getCqlDataTypeList().contains(a.getType())) {
                argument.setArgumentType(a.getType());
            } else { // Other type.
                argument.setArgumentType(("Others"));
                argument.setOtherType(a.getType());
            }
            return argument;
        }).collect(Collectors.toList()));
        f.setContext(destinationModel.getContext());
        destinationModel.getCqlFunctions().add(f);
    }

    /**
     * @param title The title.
     * @param logic The logic.
     * @param comment The comment.
     * @return The CQLDefinition with all the defaults and params populated.
     */
    private CQLDefinition buildCQLDef(String title, String logic, String comment) {
        CQLDefinition result = new CQLDefinition();
        result.setId(newGuid());
        result.setName(title);
        result.setLogic(logic);
        result.setSupplDataElement(false);
        result.setPopDefinition(false);
        result.setCommentString(comment);
        return result;
    }

    private <T> Optional<T> findExisting(List<T> collection, Predicate<T> filter) {
        return collection.stream().filter(filter).findFirst();
    }

    private void updateCodeSystemInfo(CQLCode c) {
        var currentCS = destinationModel.getCodeSystemList().stream().filter(cs ->
                StringUtils.equals(cs.getCodeSystemName(), c.getCodeSystemName())).findFirst();
        var previousCode = findExisting(sourceModel.getCodeList(),
                ec -> StringUtils.equals(ec.getCodeName(), c.getCodeName()) &&
                        StringUtils.equals(ec.getCodeSystemName(), c.getCodeSystemName()));
        if (currentCS.isPresent()) {
            c.setCodeSystemName(currentCS.get().getCodeSystemName());
            c.setIsCodeSystemVersionIncluded(c.getCodeSystemName().contains(":"));
            c.setCodeSystemOID(currentCS.get().getCodeSystem());
            c.setCodeSystemVersion(previousCode.isPresent() ?
                    previousCode.get().getCodeSystemVersion() :
                    currentCS.get().getCodeSystemVersion());
            c.setCodeSystemVersionUri(currentCS.get().getVersionUri());

            if (c.isIsCodeSystemVersionIncluded()) {
                c.setCodeSystemVersion(parseCodeSystemName(c.getCodeSystemName()).getRight());
            }
            if (StringUtils.isBlank(c.getCodeSystemVersion())) {
                //Grab it from vsacCodeSysteMap spread sheet.
                var map = codeListService.getOidToVsacCodeSystemMap().values();
                var vsacCodeSystem = map.stream().filter(
                        v -> StringUtils.equals(v.getUrl(), c.getCodeSystemOID())).findFirst();
                if (vsacCodeSystem.isEmpty()) {
                    //Hard error since we can't find the an entry in the spreadsheet.
                    throw new IllegalArgumentException("Can't find vsacCodeSystem with codeSystemUrl: " +
                            c.getCodeSystemOID() + ". Check to see if the spreadsheet needs to be updated.");
                } else {
                    c.setCodeSystemVersion(vsacCodeSystem.get().getDefaultVsacVersion());
                }
            }
        } else {
            //Hard error since we can't find the existing code system in the cql.
            throw new IllegalArgumentException("Could not find code system named ," +
                    c.getCodeSystemName() + ", for code " + c.getCodeName() + " " + c.getCodeOID());
        }
    }

    private void updateCodeIdentifierAndVsacValidationFlag(CQLCode c) {
        var existingCode = findExisting(sourceModel.getCodeList(),
                ec -> StringUtils.equals(ec.getCodeName(), c.getCodeName()) &&
                        StringUtils.equals(ec.getCodeSystemName(), c.getCodeSystemName()));
        c.setValidatedWithVsac(existingCode.map(CQLCode::isValidatedWithVsac).orElse(false));
        c.setCodeIdentifier(existingCode.isPresent() ?
                existingCode.get().getCodeIdentifier() :
                buildCodeIdentifier(c));
    }

    /**
     * Builds the code identifier vsac url.
     *
     * @param c The CQLCode to build the Identifier for.
     * @return The VSAC code url, e.g. .
     */
    private String buildCodeIdentifier(CQLCode c) {
        var vsacCodeSystem = codeListService.getOidToVsacCodeSystemMap().values().stream().filter(
                v -> StringUtils.equals(v.getUrl(), c.getCodeSystemOID())).findFirst();
        if (vsacCodeSystem.isEmpty()) {
            //Hard error since we can't find the an entry in the spreadsheet.
            throw new IllegalArgumentException("Can't find vsacCodeSystem with codeSystemUrl: " +
                    c.getCodeSystemOID() + ". Check to see if the spreadsheet needs to be updated.");
        } else {
            String defaultVersion = vsacCodeSystem.get().getDefaultVsacVersion();
            return String.format(CODE_IDENTIFIER_FORMAT,
                    parseCodeSystemName(c.getCodeSystemName()).getLeft(),
                    StringUtils.isBlank(c.getCodeSystemVersionUri()) ? defaultVersion : c.getCodeSystemVersion(),
                    c.getCodeOID());
        }
    }
}
