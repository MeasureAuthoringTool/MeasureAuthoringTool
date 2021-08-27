package mat.shared;

import lombok.ToString;
import mat.client.shared.GenericResult;
import mat.model.MeasureSteward;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.shared.cql.model.UnusedCqlElements;

import java.util.*;

@ToString
public class SaveUpdateCQLResult extends GenericResult {

    private CQLModel cqlModel;

    private CQLObject cqlObject;

    private String libraryName;

    private String setId;

    private String xml;

    private String cqlString;

    private String libDescription;

    private boolean libIsExperimental;

    private String libStewardId;

    private List<MeasureSteward> libStewards;

    private String measureDescription;

    private String measureStewardId;

    private List<String> measureTypes;

    private List<CQLError> cqlErrors = new ArrayList<>();

    private List<CQLError> cqlWarnings = new ArrayList<>();

    private List<CQLError> linterErrors = new ArrayList<>();

    private List<String> linterErrorMessages = new ArrayList<>();

    private List<String> linterWarningMessages = new ArrayList<>();

    private CQLDefinition definition;

    private CQLParameter parameter;

    private CQLFunctions function;

    private CQLIncludeLibrary includeLibrary;

    private GetUsedCQLArtifactsResult usedCQLArtifacts = new GetUsedCQLArtifactsResult();

    private int startLine;

    private int endLine;

    public static final int NAME_NOT_UNIQUE = 1;

    public static final int NODE_NOT_FOUND = 2;

    public static final int NO_SPECIAL_CHAR = 3;

    public static final int SERVER_SIDE_VALIDATION = 4;

    public static final int ALREADY_EXISTS = 5;

    public static final int FUNCTION_ARGUMENT_INVALID = 6;

    private static final int DUPLICATE_CODE = 7;

    public static final int COMMENT_INVALID = 8;

    public static final int BIRTHDATE_OR_DEAD_ERROR = 9;

    public static final int DUPLICATE_CQL_KEYWORD = 10;

    public static final int SYNTAX_ERRORS = 11;

    public static final int DUPLICATE_LIBRARY_NAME = 12;

    public static final int CUSTOM = 13;

    public static final int DESCRIPTION_REQUIRED = 14;

    public static final int PUBLISHER_REQUIRED = 15;

    public static final int MEASURE_NAME_INVALID = 16;

    public static final int INVALID_EMEASUREID = 17;

    List<CQLQualityDataSetDTO> cqlAppliedQDMList;

    CQLQualityDataSetDTO cqlQualityDataSetDTO;

    List<CQLCode> cqlCodeList;

    CQLCode cqlCode;

    private String elmString = "";

    private String jsonString = "";

    private boolean isSevereError = false;

    private boolean isDatatypeUsedCorrectly = true;

    private boolean isValidCQLWhileSavingExpression = true;

    private boolean isQDMVersionMatching = true;

    private Map<String, List<CQLError>> libraryNameErrorsMap = new HashMap<>();

    private Map<String, List<CQLError>> libraryNameWarningsMap = new HashMap<>();

    private boolean isMeasureComposite = false;

    private boolean doesMeasureHaveIncludedLibraries = false;

    private Set<String> includeLibrariesWithErrors;

    private String populationBasis;

    private UnusedCqlElements unusedCqlElements;


    public UnusedCqlElements getUnusedCqlElements() {
        return unusedCqlElements;
    }

    public void setUnusedCqlElements(UnusedCqlElements unusedCqlElements) {
        this.unusedCqlElements = unusedCqlElements;
    }

    public boolean hasUnusedElements() {
        if (unusedCqlElements == null) {
            return false;
        } else {
            return unusedCqlElements.haveUnused();
        }
    }

    public boolean hasUnusedLibraries() {
        if(!hasUnusedElements()) {
            return false;
        }
        if (unusedCqlElements.getLibraries() == null) {
            return false;
        } else {
            return !unusedCqlElements.getLibraries().isEmpty();
        }
    }

    /**
     * Gets the cql string.
     *
     * @return the cql string
     */
    public String getCqlString() {
        return cqlString;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    /**
     * Sets the cql string.
     *
     * @param cqlString the new cql string
     */
    public void setCqlString(String cqlString) {
        this.cqlString = cqlString;
    }

    /**
     * Gets the cql errors.
     *
     * @return the cql errors
     */
    public List<CQLError> getCqlErrors() {
        return cqlErrors;
    }

    /**
     * Sets the cql errors.
     *
     * @param cqlErrors the new cql errors
     */
    public void setCqlErrors(List<CQLError> cqlErrors) {
        this.cqlErrors = cqlErrors;
    }

    public List<CQLError> getCqlWarnings() {
        return cqlWarnings;
    }

    public void setCqlWarnings(List<CQLError> cqlWarnings) {
        this.cqlWarnings = cqlWarnings;
    }

    /**
     * Gets the cql model.
     *
     * @return the cql model
     */
    public CQLModel getCqlModel() {
        return cqlModel;
    }

    /**
     * Sets the cql model.
     *
     * @param cqlModel the new cql model
     */
    public void setCqlModel(CQLModel cqlModel) {
        this.cqlModel = cqlModel;
    }


    /**
     * Gets the definition.
     *
     * @return the definition
     */
    public CQLDefinition getDefinition() {
        return definition;
    }

    /**
     * Sets the definition.
     *
     * @param definition the new definition
     */
    public void setDefinition(CQLDefinition definition) {
        this.definition = definition;
    }

    /**
     * Gets the parameter.
     *
     * @return the parameter
     */
    public CQLParameter getParameter() {
        return parameter;
    }

    /**
     * Sets the parameter.
     *
     * @param parameter the new parameter
     */
    public void setParameter(CQLParameter parameter) {
        this.parameter = parameter;
    }

    /**
     * Gets the function.
     *
     * @return the function
     */
    public CQLFunctions getFunction() {
        return function;
    }

    /**
     * Sets the function.
     *
     * @param function the new function
     */
    public void setFunction(CQLFunctions function) {
        this.function = function;
    }

    /**
     * Sets the start line.
     *
     * @param startLine the new start line
     */
    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    /**
     * Gets the start line.
     *
     * @return the start line
     */
    public int getStartLine() {
        return this.startLine;
    }

    /**
     * Sets the end line.
     *
     * @param endLine the new end line
     */
    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    /**
     * Gets the end L ine.
     *
     * @return the end L ine
     */
    public int getEndLIne() {
        return this.endLine;
    }


    /**
     * Gets the cql applied QDM list.
     *
     * @return the cql applied QDM list
     */
    public List<CQLQualityDataSetDTO> getCqlAppliedQDMList() {
        return cqlAppliedQDMList;
    }

    /**
     * Sets the cql applied QDM list.
     *
     * @param cqlAppliedQDMList the new cql applied QDM list
     */
    public void setCqlAppliedQDMList(List<CQLQualityDataSetDTO> cqlAppliedQDMList) {
        this.cqlAppliedQDMList = cqlAppliedQDMList;
    }

    public List<CQLCode> getCqlCodeList() {
        return cqlCodeList;
    }

    public void setCqlCodeList(List<CQLCode> cqlCodeList) {
        this.cqlCodeList = cqlCodeList;
    }

    public CQLCode getCqlCode() {
        return cqlCode;
    }

    public void setCqlCode(CQLCode cqlCode) {
        this.cqlCode = cqlCode;
    }

    /**
     * Gets the cql quality data set DTO.
     *
     * @return the cql quality data set DTO
     */
    public CQLQualityDataSetDTO getCqlQualityDataSetDTO() {
        return cqlQualityDataSetDTO;
    }

    /**
     * Sets the cql quality data set DTO.
     *
     * @param cqlQualityDataSetDTO the new cql quality data set DTO
     */
    public void setCqlQualityDataSetDTO(CQLQualityDataSetDTO cqlQualityDataSetDTO) {
        this.cqlQualityDataSetDTO = cqlQualityDataSetDTO;
    }

    public CQLIncludeLibrary getIncludeLibrary() {
        return includeLibrary;
    }

    public void setIncludeLibrary(CQLIncludeLibrary includeLibrary) {
        this.includeLibrary = includeLibrary;
    }

    public GetUsedCQLArtifactsResult getUsedCQLArtifacts() {
        return usedCQLArtifacts;
    }

    public void setUsedCQLArtifacts(GetUsedCQLArtifactsResult usedCQLArtifacts) {
        this.usedCQLArtifacts = usedCQLArtifacts;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getElmString() {
        return elmString;
    }

    public void setElmString(String elmString) {
        this.elmString = elmString;
    }

    /**
     * @return the setId
     */
    public String getSetId() {
        return setId;
    }

    /**
     * @param setId the setId to set
     */
    public void setSetId(String setId) {
        this.setId = setId;
    }

    public boolean isQDMVersionMatching() {
        return isQDMVersionMatching;
    }

    public void setQDMVersionMatching(boolean isQDMVersionMatching) {
        this.isQDMVersionMatching = isQDMVersionMatching;
    }

    public boolean isDatatypeUsedCorrectly() {
        return isDatatypeUsedCorrectly;
    }

    public void setDatatypeUsedCorrectly(boolean isDatatypeUsedCorrectly) {
        this.isDatatypeUsedCorrectly = isDatatypeUsedCorrectly;
    }

    public CQLObject getCqlObject() {
        return cqlObject;
    }

    public void setCqlObject(CQLObject cqlObject) {
        this.cqlObject = cqlObject;
    }

    public int getDuplicateCode() {
        return DUPLICATE_CODE;
    }


    public int getBirthdateOrDeadError() {
        return BIRTHDATE_OR_DEAD_ERROR;
    }

    public boolean isValidCQLWhileSavingExpression() {
        return isValidCQLWhileSavingExpression;
    }

    public void setValidCQLWhileSavingExpression(boolean isValidCQLWhileSavingExpression) {
        this.isValidCQLWhileSavingExpression = isValidCQLWhileSavingExpression;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    public Map<String, List<CQLError>> getLibraryNameErrorsMap() {
        return libraryNameErrorsMap;
    }

    public void setLibraryNameErrorsMap(Map<String, List<CQLError>> libraryNameErrorsMap) {
        this.libraryNameErrorsMap = libraryNameErrorsMap;
    }

    public Map<String, List<CQLError>> getLibraryNameWarningsMap() {
        return libraryNameWarningsMap;
    }

    public void setLibraryNameWarningsMap(Map<String, List<CQLError>> libraryNameWarningsMap) {
        this.libraryNameWarningsMap = libraryNameWarningsMap;
    }

    public List<CQLError> getLinterErrors() {
        return linterErrors;
    }

    public void setLinterErrors(List<CQLError> linterErrors) {
        this.linterErrors = linterErrors;
    }

    public List<String> getLinterErrorMessages() {
        return linterErrorMessages;
    }

    public void setLinterErrorMessages(List<String> linterErrorMessages) {
        this.linterErrorMessages = linterErrorMessages;
    }

    public List<String> getLinterWarningMessages() {
        return linterWarningMessages;
    }

    public void setLinterWarningMessages(List<String> linterWarningMessages) {
        this.linterWarningMessages = linterWarningMessages;
    }

    public void resetErrors() {
        getCqlErrors().clear();
        getLibraryNameErrorsMap().clear();
        getLibraryNameWarningsMap().clear();
    }

    public boolean isMeasureComposite() {
        return isMeasureComposite;
    }

    public void setMeasureComposite(boolean isMeasureComposite) {
        this.isMeasureComposite = isMeasureComposite;
    }

    public boolean isDoesMeasureHaveIncludedLibraries() {
        return doesMeasureHaveIncludedLibraries;
    }

    public void setDoesMeasureHaveIncludedLibraries(boolean doesMeasureHaveIncludedLibraries) {
        this.doesMeasureHaveIncludedLibraries = doesMeasureHaveIncludedLibraries;
    }

    public void setIncludeLibrariesWithErrors(Set<String> includeLibrariesWithErrors) {
        this.includeLibrariesWithErrors = includeLibrariesWithErrors;
    }

    public Set<String> getIncludeLibrariesWithErrors() {
        return includeLibrariesWithErrors;
    }

    public boolean isSevereError() {
        return isSevereError;
    }

    public void setSevereError(boolean severeError) {
        this.isSevereError = severeError;
    }

    public String getLibDescription() {
        return libDescription;
    }

    public void setLibDescription(String libDescription) {
        this.libDescription = libDescription;
    }

    public boolean isLibIsExperimental() {
        return libIsExperimental;
    }

    public void setLibIsExperimental(boolean libIsExperimental) {
        this.libIsExperimental = libIsExperimental;
    }

    public String getLibStewardId() {
        return libStewardId;
    }

    public void setLibStewardId(String libStewardId) {
        this.libStewardId = libStewardId;
    }

    public List<MeasureSteward> getLibStewards() {
        return libStewards;
    }

    public void setLibStewards(List<MeasureSteward> libStewards) {
        this.libStewards = libStewards;
    }

    public String getMeasureDescription() {
        return measureDescription;
    }

    public void setMeasureDescription(String measureDescription) {
        this.measureDescription = measureDescription;
    }

    public String getMeasureStewardId() {
        return measureStewardId;
    }

    public void setMeasureStewardId(String measureStewardId) {
        this.measureStewardId = measureStewardId;
    }

    public List<String> getMeasureTypes() {
        return measureTypes;
    }

    public void setMeasureTypes(List<String> measureTypes) {
        this.measureTypes = measureTypes;
    }

    public String getPopulationBasis() {
        return populationBasis;
    }

    public void setPopulationBasis(String populationBasis) {
        this.populationBasis = populationBasis;
    }
}
