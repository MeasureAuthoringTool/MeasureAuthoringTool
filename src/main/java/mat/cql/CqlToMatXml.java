package mat.cql;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLCodeSystem;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataSetDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static mat.cql.CqlUtils.getGlobalLibId;
import static mat.cql.CqlUtils.isOid;
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
//Place holder comments. Eventually this will be a prototype capable of having DAOs.
//@Component
//@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CqlToMatXml implements CqlVisitor {
    private List<CQLIncludeLibrary> libsNotFound = new ArrayList<>();
    private CQLModel destinationModel = new CQLModel();

    @Override
    public void libraryTag(String libraryName, String version) {
        //library CWP_HEDIS_2020_CARSON version '1.0.000'
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
        cs.setCodeSystemName(parsedCodeSystemName.getLeft());
        cs.setCodeSystemVersion(parsedCodeSystemName.getRight());
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
        if (isOid(uri)) {
            vs.setOid(parseOid(uri));
        } else {
            vs.setOid(uri);
        }

        // Nonsensical legacy stuff that has to be here for the gwt part to function at the moment.
        vs.setOriginalCodeListName(name);
        vs.setSuppDataElement(false);
        vs.setTaxonomy("Grouping");
        vs.setType("Grouping");

        destinationModel.getValueSetList().add(vs);
    }

    @Override
    public void code(String name, String code, String codeSystemName, String displayName) {
        var parsedCodeSystemName = parseCodeSystemName(codeSystemName);
        var c = new CQLCode();
        c.setCodeSystemName(parsedCodeSystemName.getLeft());
        c.setCodeSystemVersion(parsedCodeSystemName.getRight());
        c.setIsCodeSystemVersionIncluded(parsedCodeSystemName.getRight() != null);
        c.setCodeIdentifier(code);
        c.setCodeOID(code);
        c.setDisplayName(displayName);
        c.setCodeName(name);
        c.setId(newGuid());
        // Backward compatibility this is ugly.
        // I have no idea why they put this stuff in code originally in MAT.
        destinationModel.getCodeSystemList().stream().filter(cs ->
                StringUtils.equals(cs.getCodeSystemName(), c.getCodeSystemName()) &&
                        StringUtils.equals(cs.getCodeSystemVersion(), c.getCodeSystemVersion())).findFirst().ifPresent(cs -> {
            c.setCodeSystemOID(cs.getCodeSystem());
            c.setCodeSystemVersion(cs.getCodeSystemVersion());
            c.setCodeSystemVersionUri(cs.getVersionUri());
        });
        destinationModel.getCodeList().add(c);
    }

    @Override
    public void parameter(String name, String logic) {
        CQLParameter p = new CQLParameter();
        p.setId(newGuid());
        p.setName(name);
        p.setParameterLogic(logic);
        p.setReadOnly(false);
        destinationModel.getCqlParameters().add(p);
    }

    @Override
    public void context(String context) {
        destinationModel.setContext(context);
    }

    @Override
    public void definition(String name, String logic) {
        destinationModel.getDefinitionList().add(buildCQLDef(name, logic));
    }

    @Override
    public void function(String name, List<FunctionArgument> args, String logic) {
        var f = new CQLFunctions();
        f.setId(newGuid());
        f.setName(name);
        f.setLogic(logic);
        f.setArgumentList(args.stream().map(a -> {
            CQLFunctionArgument argument = new CQLFunctionArgument();
            argument.setId(newGuid());
            argument.setArgumentName(a.getName());
            argument.setQdmDataType(a.getType());
            argument.setArgumentType(a.getType());
            return argument;
        }).collect(Collectors.toList()));
        f.setContext(destinationModel.getContext());
        destinationModel.getCqlFunctions().add(f);
    }

    /**
     * @param title The title.
     * @param logic The logic.
     * @return The CQLDefinition with all the defaults and params populated.
     */
    private CQLDefinition buildCQLDef(String title, String logic) {
        CQLDefinition result = new CQLDefinition();
        result.setId(newGuid());
        result.setName(title);
        result.setLogic(logic);
        result.setSupplDataElement(false);
        result.setPopDefinition(false);
        return result;
    }
}
