package mat.cql;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataSetDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static mat.cql.CqlUtils.getGlobalLibId;
import static mat.cql.CqlUtils.isOid;
import static mat.cql.CqlUtils.newGuid;
import static mat.cql.CqlUtils.parseOid;

/**
 * A CqlVisitor used for conversion from QDM to FHIR.
 * The CQL used for this visitor should be fhir cql.
 * This version requires a source CQLModel to be set prior to parsing.
 */
@Getter
@Setter
@ToString
@Slf4j
//Place holder comments. Eventually this will be a prototype capable of having DAOs.
//@Component
//@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ConversionCqlToMatXml implements CqlVisitor {
    private List<CQLIncludeLibrary> libsNotFound = new ArrayList<>();
    private CQLModel sourceModel;
    private CQLModel destinationModel = new CQLModel();


    public ConversionCqlToMatXml() {
    }

    @Override
    public void validate() {
        if (sourceModel == null) {
            throw new IllegalStateException("To use a ConversionCqlToMatXml bean you must first set the source model.");
        }
    }

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
        lib.setCqlLibraryId(getGlobalLibId(lib.getCqlLibraryName()));
        destinationModel.getCqlIncludeLibrarys().add(lib);
    }

    @Override
    public void codeSystem(String name, String uri, String versionUri) {
        // TO DO: handle http uris vs oids correctly.

        // Right now microservices will never use the http urls.
        destinationModel.getCodeSystemList().add(findExisting(sourceModel.getCodeSystemList(),
                cs -> StringUtils.equals(name, cs.getCodeSystemName()),
                "Could not find sourceCqlModel.codeSystemList for " + name));
    }

    @Override
    public void valueSet(String name, String uri) {
        // For all of fhir4 valuesets will be in the new format.
        // They are simply a name and a uri.
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
        // TO DO: handle http uris vs oids correctly.
        // Right now microservices will never use the http urls.

        // For now these should be identical to the sourceCqlModel versions.
        // Just find the corresponding one by name and use that.
        destinationModel.getCodeList().add(findExisting(sourceModel.getCodeList(),
                c -> StringUtils.equals(name, c.getName()),
                "Could not find sourceCqlModel.code " + name));
    }

    @Override
    public void parameter(String name,String logic) {
        var existingParam = findExisting(sourceModel.getCqlParameters(),
                p -> StringUtils.equals(p.getName(), name),
                "Could not find parameter for " + name + " in existingCqlModel");
        CQLParameter p = new CQLParameter();
        p.setId(existingParam.getId());
        p.setName(name);
        p.setParameterLogic(logic);
        p.setReadOnly(existingParam.isReadOnly());
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

    private <T> T findExisting(List<T> collection, Predicate<T> filter, String messageIfNotFound) {
        var vs = collection.stream().filter(filter).findFirst();
        if (vs.isPresent()) {
            return vs.get();
        } else {
            throw new IllegalArgumentException(messageIfNotFound);
        }
    }
}
