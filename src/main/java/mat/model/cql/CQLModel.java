package mat.model.cql;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gwt.user.client.rpc.IsSerializable;
import mat.model.clause.ModelTypeHelper;
import mat.shared.CQLIdentifierObject;
import mat.shared.LibHolderObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JsonIgnoreProperties({"includedLibrarys", "includedCQLLibXMLMap", "fhir", "formattedName"})
public class CQLModel implements IsSerializable {
    private String libraryName;
    private String versionUsed;
    private String libraryComment;
    private String usingModelVersion;
    private String usingModel;
    private String context;

    private List<CQLQualityDataSetDTO> valueSetList = new ArrayList<>();
    private List<CQLQualityDataSetDTO> allValueSetAndCodeList = new ArrayList<>();
    private List<CQLParameter> cqlParameters = new ArrayList<>();
    private List<CQLDefinition> cqlDefinitions = new ArrayList<>();
    private List<CQLFunctions> cqlFunctions = new ArrayList<>();
    private List<CQLCodeSystem> codeSystemList = new ArrayList<>();
    private List<CQLCode> codeList = new ArrayList<>();
    private List<CQLIncludeLibrary> cqlIncludeLibrarys = new ArrayList<>();
    private Map<CQLIncludeLibrary, CQLModel> includedLibrarys = new HashMap<>();

    /**
     * This member is set programmatically from some class and isn't populated by Hibernate.
     * So it is possible it is null/empty.
     */
    private Map<String, LibHolderObject> includedCQLLibXMLMap = new HashMap<>();

    private int lines;

    public boolean isFhir() {
        return ModelTypeHelper.isFhir(usingModel);
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public List<CQLQualityDataSetDTO> getValueSetList() {
        return valueSetList;
    }

    public void setValueSetList(List<CQLQualityDataSetDTO> valueSetList) {
        this.valueSetList = valueSetList;
    }

    public List<CQLParameter> getCqlParameters() {
        return cqlParameters;
    }

    public void setCqlParameters(List<CQLParameter> cqlParameters) {
        this.cqlParameters = cqlParameters;
    }

    public List<CQLDefinition> getDefinitionList() {
        return cqlDefinitions;
    }

    public void setDefinitionList(List<CQLDefinition> definitionList) {
        cqlDefinitions = definitionList;
    }

    public List<CQLFunctions> getCqlFunctions() {
        return cqlFunctions;
    }

    public void setCqlFunctions(List<CQLFunctions> cqlFunctions) {
        this.cqlFunctions = cqlFunctions;
    }

    public int getLines() {
        return lines;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }

    public List<CQLCodeSystem> getCodeSystemList() {
        return codeSystemList;
    }

    public void setCodeSystemList(List<CQLCodeSystem> list) {
        this.codeSystemList = list;
    }

    public List<CQLCode> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<CQLCode> codeList) {
        this.codeList = codeList;
    }

    public List<CQLQualityDataSetDTO> getAllValueSetAndCodeList() {
        return allValueSetAndCodeList;
    }

    public void setAllValueSetAndCodeList(List<CQLQualityDataSetDTO> allValueSetAndCodeList) {
        this.allValueSetAndCodeList = allValueSetAndCodeList;
    }

    public List<CQLIncludeLibrary> getCqlIncludeLibrarys() {
        return cqlIncludeLibrarys;
    }

    public void setCqlIncludeLibrarys(List<CQLIncludeLibrary> cqlIncludeLibrarys) {
        this.cqlIncludeLibrarys = cqlIncludeLibrarys;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public String getVersionUsed() {
        return versionUsed;
    }

    public void setVersionUsed(String versionUsed) {
        this.versionUsed = versionUsed;
    }

    public String getLibraryComment() {
        return libraryComment;
    }

    public void setLibraryComment(String libraryComment) {
        this.libraryComment = libraryComment;
    }

    public String getUsingModelVersion() {
        return usingModelVersion;
    }

    public void setUsingModelVersion(String usingModelVersion) {
        this.usingModelVersion = usingModelVersion;
    }

    public String getUsingModel() {
        return usingModel;
    }

    public void setUsingModel(String name) {
        this.usingModel = name;
    }

    public Map<CQLIncludeLibrary, CQLModel> getIncludedLibrarys() {
        return includedLibrarys;
    }

    public void setIncludedLibrarys(Map<CQLIncludeLibrary, CQLModel> includedLibrarys) {
        this.includedLibrarys = includedLibrarys;
    }

    public List<CQLDefinition> getIncludedDef() {
        List<CQLDefinition> includedDefNames = new ArrayList<CQLDefinition>();
        for (CQLModel value : includedLibrarys.values()) {
            includedDefNames.addAll(value.getDefinitionList());
        }
        return includedDefNames;
    }

    public List<CQLIdentifierObject> getCQLIdentifierDefinitions() {
        List<CQLIdentifierObject> includedDefCQLIdentifierObject = new ArrayList<CQLIdentifierObject>();
        for (CQLIncludeLibrary lib : includedLibrarys.keySet()) {
            CQLModel model = includedLibrarys.get(lib);
            for (CQLDefinition def : model.getDefinitionList()) {
                includedDefCQLIdentifierObject.add(new CQLIdentifierObject(lib.getAliasName(), def.getName()));
            }
        }
        return includedDefCQLIdentifierObject;
    }

    public List<CQLFunctions> getIncludedFunc() {
        List<CQLFunctions> includedFunctions = new ArrayList<CQLFunctions>();

        includedLibrarys.forEach((k, v) -> {
            v.getCqlFunctions().forEach(f -> f.setAliasName(k.getAliasName()));
            includedFunctions.addAll(v.getCqlFunctions());
        });

        return includedFunctions;
    }

    public List<CQLIdentifierObject> getCQLIdentifierFunctions() {
        List<CQLIdentifierObject> includedFuncCQLIdentifierObject = new ArrayList<CQLIdentifierObject>();
        for (CQLIncludeLibrary lib : includedLibrarys.keySet()) {
            CQLModel model = includedLibrarys.get(lib);
            for (CQLFunctions fun : model.getCqlFunctions()) {
                includedFuncCQLIdentifierObject.add(new CQLIdentifierObject(lib.getAliasName(), fun.getName()));
            }
        }
        return includedFuncCQLIdentifierObject;
    }

    public List<CQLQualityDataSetDTO> getIncludedValueSet() {
        List<CQLQualityDataSetDTO> includedValueSetNames = new ArrayList<CQLQualityDataSetDTO>();
        for (CQLModel value : includedLibrarys.values()) {
            includedValueSetNames.addAll(value.getValueSetList());
        }
        return includedValueSetNames;
    }

    public List<CQLIdentifierObject> getCQLIdentifierValueSet() {
        List<CQLIdentifierObject> includedValueSetCQLIdentifierObject = new ArrayList<CQLIdentifierObject>();
        for (CQLIncludeLibrary lib : includedLibrarys.keySet()) {
            CQLModel model = includedLibrarys.get(lib);
            for (CQLQualityDataSetDTO value : model.getValueSetList()) {
                includedValueSetCQLIdentifierObject.add(new CQLIdentifierObject(lib.getAliasName(), value.getName()));
            }
        }
        return includedValueSetCQLIdentifierObject;
    }

    public List<CQLParameter> getIncludedParam() {
        List<CQLParameter> includedParamNames = new ArrayList<CQLParameter>();
        for (CQLModel value : includedLibrarys.values()) {
            includedParamNames.addAll(value.getCqlParameters());
        }
        return includedParamNames;
    }

    public List<CQLIdentifierObject> getCQLIdentifierParam() {
        List<CQLIdentifierObject> includedParamCQLIdentifierObject = new ArrayList<CQLIdentifierObject>();
        for (CQLIncludeLibrary lib : includedLibrarys.keySet()) {
            CQLModel model = includedLibrarys.get(lib);
            for (CQLParameter param : model.getCqlParameters()) {
                includedParamCQLIdentifierObject.add(new CQLIdentifierObject(lib.getAliasName(), param.getName()));
            }
        }
        return includedParamCQLIdentifierObject;
    }


    public List<CQLCode> getIncludedCode() {
        List<CQLCode> includedCodeNames = new ArrayList<CQLCode>();
        for (CQLModel value : includedLibrarys.values()) {
            includedCodeNames.addAll(value.getCodeList());
        }
        return includedCodeNames;
    }

    public List<CQLIdentifierObject> getCQLIdentifierCode() {
        List<CQLIdentifierObject> includedCodeCQLIdentifierObject = new ArrayList<>();
        for (CQLIncludeLibrary lib : includedLibrarys.keySet()) {
            CQLModel model = includedLibrarys.get(lib);
            for (CQLCode code : model.getCodeList()) {
                includedCodeCQLIdentifierObject.add(new CQLIdentifierObject(lib.getAliasName(), code.getDisplayName()));
            }
        }
        return includedCodeCQLIdentifierObject;
    }

    public Map<String, LibHolderObject> getIncludedCQLLibXMLMap() {
        return includedCQLLibXMLMap;
    }

    public void setIncludedCQLLibXMLMap(Map<String, LibHolderObject> includedCQLLibXMLMap) {
        this.includedCQLLibXMLMap = includedCQLLibXMLMap;
    }

    /**
     * Gets a valueset by name from the parent or any children
     *
     * @param formattedCodeName the name in the format libraryname-x.x.xxx|alias|code identifier if from child, otherwise just code identifer
     * @return the code found
     */
    public CQLCode getCodeByName(String formattedCodeName) {
        String codeName = formattedCodeName;
        String libraryNameVersion = null; // name in the format libraryname-x.x.xxx
        String[] codeSplit = formattedCodeName.split("\\|");
        if (codeSplit.length == 3) {
            libraryNameVersion = codeSplit[0];
            codeName = codeSplit[2];
        }

        // if the library name version is null, then the code is in the parent
        if (libraryNameVersion == null) {
            for (CQLCode code : codeList) {
                if (code.getDisplayName() == null ? code.getCodeName().equals(codeName) : code.getDisplayName().equals(codeName)) {
                    return code;
                }
            }
        } else {
            final String nameVersion = libraryNameVersion;
            List<CQLIncludeLibrary> cqlIncludeLibrary = includedLibrarys.keySet().stream().filter(lib -> createNameVersionString(lib.getCqlLibraryName(), lib.getVersion()).equals(nameVersion)).collect(Collectors.toList());
            if (cqlIncludeLibrary != null && !cqlIncludeLibrary.isEmpty()) {
                for (CQLCode code : includedLibrarys.get(cqlIncludeLibrary.get(0)).getCodeList()) {
                    if (code.getDisplayName().equals(codeName)) {
                        return code;
                    }
                }
            }
        }

        return null;
    }

    private String createNameVersionString(String name, String version) {
        return name + "-" + version;
    }

    /**
     * Gets a code by name from the parent or any children
     *
     * @param formattedValuesetName the name in the format libraryname-x.x.xxx|alias|valueset identifier
     * @return the code found
     */
    public CQLQualityDataSetDTO getValuesetByName(String formattedValuesetName) {
        String valuesetName = formattedValuesetName;
        String libraryNameVersion = null; // name in the format libraryname-x.x.xxx
        String[] valuesetSplit = formattedValuesetName.split("\\|");
        if (valuesetSplit.length == 3) {
            libraryNameVersion = valuesetSplit[0];
            valuesetName = valuesetSplit[2];
        }

        // if the library name version is null, then the code is in the parent
        if (libraryNameVersion == null) {
            for (CQLQualityDataSetDTO valueset : valueSetList) {
                if (valueset.getName() == valuesetName) {
                    return valueset;
                }
            }
        } else {
            final String nameVersion = libraryNameVersion;
            List<CQLIncludeLibrary> cqlIncludeLibrary = includedLibrarys.keySet().stream().filter(lib -> createNameVersionString(lib.getCqlLibraryName(), lib.getVersion()).equals(nameVersion)).collect(Collectors.toList());
            if (cqlIncludeLibrary != null && !cqlIncludeLibrary.isEmpty()) {
                for (CQLQualityDataSetDTO valueset : includedLibrarys.get(cqlIncludeLibrary.get(0)).getValueSetList()) {
                    if (valueset.getName() == valuesetName) {
                        return valueset;
                    }
                }
            }
        }

        return null;
    }

    /**
     * This function returns a list containing all the definitions and functions names in the model
     *
     * @return list containing all the definitions and functions names in the model
     */
    public List<String> getExpressionListFromCqlModel() {
        List<String> expressionList = new ArrayList<>();

        for (CQLDefinition cqlDefinition : cqlDefinitions) {
            expressionList.add(cqlDefinition.getName());
        }

        for (CQLFunctions cqlFunction : cqlFunctions) {
            expressionList.add(cqlFunction.getName());
        }

        return expressionList;
    }

    public String getFormattedName() {
        return this.libraryName + "-" + this.versionUsed;
    }
}