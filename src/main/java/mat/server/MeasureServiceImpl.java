package mat.server;

import mat.client.clause.clauseworkspace.model.MeasureDetailResult;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.clause.clauseworkspace.model.SortedClauseMapResult;
import mat.client.measure.ManageCompositeMeasureDetailModel;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureShareModel;
import mat.client.measure.TransferOwnerShipModel;
import mat.client.measure.service.MeasureService;
import mat.client.measure.service.SaveMeasureResult;
import mat.client.measure.service.ValidateMeasureResult;
import mat.client.shared.GenericResult;
import mat.client.shared.MatException;
import mat.client.umls.service.VsacApiResult;
import mat.model.CQLValueSetTransferObject;
import mat.model.ComponentMeasureTabObject;
import mat.model.MatCodeTransferObject;
import mat.model.MeasureType;
import mat.model.Organization;
import mat.model.QualityDataModelWrapper;
import mat.model.QualityDataSetDTO;
import mat.model.RecentMSRActivityLog;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLCodeWrapper;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLKeywords;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataModelWrapper;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.server.service.MeasureLibraryService;
import mat.shared.CompositeMeasureValidationResult;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.MeasureSearchModel;
import mat.shared.SaveUpdateCQLResult;
import mat.shared.cql.error.InvalidLibraryException;
import mat.shared.error.AuthenticationException;
import mat.shared.error.measure.DeleteMeasureException;
import mat.shared.measure.measuredetails.models.MeasureDetailsModel;
import mat.shared.measure.measuredetails.translate.ManageMeasureDetailModelMapper;
import mat.shared.measure.measuredetails.translate.MeasureDetailModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import mat.vsacmodel.ValueSet;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MeasureServiceImpl extends SpringRemoteServiceServlet implements MeasureService {
    private static final long serialVersionUID = 2280421300224680146L;

    @Autowired
    private MeasureLibraryService measureLibraryService;

    @Override
    public void appendAndSaveNode(MeasureXmlModel measureXmlModel, String nodeName) {
        this.getMeasureLibraryService().appendAndSaveNode(measureXmlModel, nodeName);

    }

    @Override
    public void createAndSaveElementLookUp(List<QualityDataSetDTO> list,
                                           String measureID, String expProfileToAllQDM) {
        this.getMeasureLibraryService().createAndSaveElementLookUp(list, measureID, expProfileToAllQDM);
    }

    @Override
    public ManageMeasureSearchModel getAllRecentMeasureForUser(String userId, boolean isFhirEnabled) {
        return this.getMeasureLibraryService().getAllRecentMeasureForUser(userId, isFhirEnabled);
    }

    @Override
    public QualityDataModelWrapper getAppliedQDMFromMeasureXml(
            String measureId, boolean checkForSupplementData) {
        return this.getMeasureLibraryService().getAppliedQDMFromMeasureXml(measureId, checkForSupplementData);
    }

    @Override
    public CQLQualityDataModelWrapper getCQLAppliedQDMFromMeasureXml(String measureId, boolean checkForSupplementData) {
        return this.getMeasureLibraryService().getCQLAppliedQDMFromMeasureXml(measureId, checkForSupplementData);
    }

    @Override
    public int getMaxEMeasureId() {
        return this.getMeasureLibraryService().getMaxEMeasureId();
    }

    @Override
    public ManageMeasureDetailModel getMeasure(String key) {
        return this.getMeasureLibraryService().getMeasure(key);
    }

    @Override
    public ManageCompositeMeasureDetailModel getCompositeMeasure(String measureId) {
        return this.getMeasureLibraryService().getCompositeMeasure(measureId);
    }

    public MeasureLibraryService getMeasureLibraryService() {
        return measureLibraryService;
    }

    @Override
    public MeasureXmlModel getMeasureXmlForMeasure(String measureId) {
        return this.getMeasureLibraryService().getMeasureXmlForMeasure(measureId);
    }

    @Override
    public List<RecentMSRActivityLog> getRecentMeasureActivityLog(String userId) {
        return this.getMeasureLibraryService().getRecentMeasureActivityLog(userId);
    }

    @Override
    public ManageMeasureShareModel getUsersForShare(String userName, String measureId,
                                                    int startIndex, int pageSize) {
        return this.getMeasureLibraryService().getUsersForShare(userName, measureId, startIndex, pageSize);
    }

    @Override
    public boolean isMeasureLocked(String id) {
        return this.getMeasureLibraryService().isMeasureLocked(id);
    }

    @Override
    public SaveMeasureResult resetLockedDate(String measureId, String userId) {
        return this.getMeasureLibraryService().resetLockedDate(measureId, userId);
    }

    @Override
    public SaveMeasureResult saveNewMeasure(ManageMeasureDetailModel model) {
        return this.getMeasureLibraryService().saveOrUpdateMeasure(model);
    }

    @Override
    public SaveMeasureResult saveCompositeMeasure(ManageCompositeMeasureDetailModel model) throws MatException {
        return this.getMeasureLibraryService().saveCompositeMeasure(model);
    }

    @Override
    public void deleteMeasure(String measureId, String loggedInUserId) throws DeleteMeasureException, AuthenticationException {
        this.getMeasureLibraryService().deleteMeasure(measureId, loggedInUserId);
    }

    @Override
    public SaveMeasureResult saveFinalizedVersion(String measureId, boolean isMajor, String version, boolean shouldPackage, boolean ignoreUnusedLibraries) {
        return this.getMeasureLibraryService().saveFinalizedVersion(measureId, isMajor, version, shouldPackage, ignoreUnusedLibraries);
    }

    @Override
    public SaveMeasureResult saveMeasureDetails(ManageMeasureDetailModel model) throws MatException {
        return this.getMeasureLibraryService().saveMeasureDetails(model);
    }

    @Override
    public void saveMeasureXml(MeasureXmlModel measureXmlModel, String measureId, boolean isFhir) {
        this.getMeasureLibraryService().saveMeasureXml(measureXmlModel, measureId, isFhir);

    }

    @Override
    public ManageMeasureSearchModel search(MeasureSearchModel advancedSearchModel) {
        return this.getMeasureLibraryService().search(advancedSearchModel);
    }

    @Override
    public TransferOwnerShipModel searchUsers(String searchText, int startIndex,
                                              int pageSize) {
        return this.getMeasureLibraryService().searchUsers(searchText, startIndex, pageSize);
    }

    @Override
    public void transferOwnerShipToUser(List<String> list, String toEmail) {
        this.getMeasureLibraryService().transferOwnerShipToUser(list, toEmail);

    }

    @Override
    public SaveMeasureResult updateLockedDate(String measureId, String userId) {
        return this.getMeasureLibraryService().updateLockedDate(measureId, userId);
    }

    @Override
    public void updateMeasureXML(QualityDataSetDTO modifyWithDTO,
                                 QualityDataSetDTO modifyDTO, String measureId) {
        this.getMeasureLibraryService().updateMeasureXML(modifyWithDTO, modifyDTO, measureId);

    }

    @Override
    public void updatePrivateColumnInMeasure(String measureId, boolean isPrivate) {
        this.getMeasureLibraryService().updatePrivateColumnInMeasure(measureId, isPrivate);
    }

    @Override
    public void updateUsersShare(ManageMeasureShareModel model) {
        this.getMeasureLibraryService().updateUsersShare(model);
    }

    @Override
    public ValidateMeasureResult validateExports(final String measureId) throws Exception {
        return this.getMeasureLibraryService().validateExports(measureId);
    }

    @Override
    public ValidateMeasureResult createExports(String key, List<ValueSet> ValueSetList, boolean shouldCreateArtifacts)
            throws MatException {
        return this.getMeasureLibraryService().createExports(key, ValueSetList, shouldCreateArtifacts);
    }

    @Override
    public String getHumanReadableForNode(String measureId, String populationSubXML) {
        return this.getMeasureLibraryService().getHumanReadableForNode(measureId, populationSubXML);
    }

    @Override
    public SaveMeasureResult saveMeasureAtPackage(ManageMeasureDetailModel model) {
        return this.getMeasureLibraryService().saveMeasureAtPackage(model);
    }

    @Override
    public SortedClauseMapResult saveSubTreeInMeasureXml(MeasureXmlModel measureXmlModel, String nodeName, String nodeUUID) {
        return this.getMeasureLibraryService().saveSubTreeInMeasureXml(measureXmlModel, nodeName, nodeUUID);
    }

    @Override
    public HashMap<String, String> checkAndDeleteSubTree(String measureId, String subTreeUUID) {
        return this.getMeasureLibraryService().checkAndDeleteSubTree(measureId, subTreeUUID);
    }

    @Override
    public boolean isSubTreeReferredInLogic(String measureId, String subTreeUUID) {
        return this.getMeasureLibraryService().isSubTreeReferredInLogic(measureId, subTreeUUID);
    }

    @Override
    public ManageMeasureSearchModel getComponentMeasures(String measureId) {
        return getMeasureLibraryService().getComponentMeasures(measureId);
    }

    @Override
    public ValidateMeasureResult validatePackageGrouping(
            ManageMeasureDetailModel model) {
        return this.getMeasureLibraryService().validatePackageGrouping(
                model);
    }

    @Override
    public ValidateMeasureResult validateMeasureXmlinpopulationWorkspace(
            MeasureXmlModel measureXmlModel) {
        return this.getMeasureLibraryService().validateMeasureXmlAtCreateMeasurePackager(measureXmlModel);
    }

    @Override
    public ValidateMeasureResult validateForGroup(ManageMeasureDetailModel model) {
        return this.getMeasureLibraryService().validateForGroup(model);
    }

    @Override
    public SaveMeasureResult validateAndPackageMeasure(ManageMeasureDetailModel model) {
        return this.getMeasureLibraryService().validateAndPackage(model, true);
    }

    @Override
    public List<MeasureType> getAllMeasureTypes() {
        return this.getMeasureLibraryService().getAllMeasureTypes();
    }

    @Override
    public List<Organization> getAllOrganizations() {
        return this.getMeasureLibraryService().getAllOrganizations();
    }

    @Override
    public SortedClauseMapResult saveSubTreeOccurrence(MeasureXmlModel measureXmlModel, String nodeName, String nodeUUID) {
        return this.getMeasureLibraryService().saveSubTreeOccurrence(measureXmlModel, nodeName, nodeUUID);
    }

    @Override
    public boolean isQDMVariableEnabled(String measureId, String subTreeUUID) {
        return this.getMeasureLibraryService().isQDMVariableEnabled(measureId, subTreeUUID);
    }

    @Override
    public LinkedHashMap<String, String> getSortedClauseMap(String measureId) {
        return this.getMeasureLibraryService().getSortedClauseMap(measureId);
    }

    @Override
    public SortedClauseMapResult getMeasureXmlForMeasureAndSortedSubTreeMap(
            String currentMeasureId) {
        return this.getMeasureLibraryService().getMeasureXmlForMeasureAndSortedSubTreeMap(currentMeasureId);
    }

    @Override
    public MeasureDetailResult getUsedStewardAndDevelopersList(String measureId) {
        return this.getMeasureLibraryService().getUsedStewardAndDevelopersList(measureId);
    }

    @Override
    public void updateMeasureXMLForExpansionIdentifier(List<QualityDataSetDTO> modifyWithDTOList,
                                                       String measureId, String expansionProfile) {
        this.getMeasureLibraryService().updateMeasureXMLForExpansionIdentifier(modifyWithDTOList, measureId, expansionProfile);
    }

    @Override
    public void updateCQLMeasureXMLForExpansionProfile(List<CQLQualityDataSetDTO> modifyWithDTOList,
                                                       String measureId, String expansionProfile) {
        this.getMeasureLibraryService().updateCQLMeasureXMLForExpansionIdentifier(modifyWithDTOList, measureId, expansionProfile);
    }

    @Override
    public QualityDataModelWrapper getDefaultSDEFromMeasureXml(String measureId) {
        return this.getMeasureLibraryService().getDefaultSDEFromMeasureXml(measureId);
    }

    @Override
    public CQLQualityDataModelWrapper getDefaultCQLSDEFromMeasureXml(String measureId) {
        return this.getMeasureLibraryService().getDefaultCQLSDEFromMeasureXml(measureId);
    }

    @Override
    public SaveUpdateCQLResult saveCQLFile(String measureId, String cql) {
        return this.getMeasureLibraryService().saveCQLFile(measureId, cql);
    }

    @Override
    public SaveUpdateCQLResult saveAndModifyDefinitions(String measureId, CQLDefinition toBemodifiedObj,
                                                        CQLDefinition currentObj, List<CQLDefinition> definitionList, boolean isFormatable) {
        return this.getMeasureLibraryService().saveAndModifyDefinitions(measureId, toBemodifiedObj, currentObj, definitionList, isFormatable);
    }

    @Override
    public SaveUpdateCQLResult saveAndModifyParameters(String measureId, CQLParameter toBemodifiedObj,
                                                       CQLParameter currentObj, List<CQLParameter> parameterList, boolean isFormatable) {
        return this.getMeasureLibraryService().saveAndModifyParameters(measureId, toBemodifiedObj, currentObj, parameterList, isFormatable);
    }

    @Override
    public SaveUpdateCQLResult saveAndModifyFunctions(String measureId, CQLFunctions toBeModifiedObj,
                                                      CQLFunctions currentObj, List<CQLFunctions> functionsList, boolean isFormatable) {
        return this.getMeasureLibraryService().saveAndModifyFunctions(measureId, toBeModifiedObj, currentObj, functionsList, isFormatable);
    }

    @Override
    public SaveUpdateCQLResult saveAndModifyCQLGeneralInfo(
            String currentMeasureId, String context, String comments) {
        return this.getMeasureLibraryService().saveAndModifyCQLGeneralInfo(currentMeasureId, context, comments);
    }

    @Override
    public CQLKeywords getCQLKeywordsList() {
        return this.getMeasureLibraryService().getCQLKeywordsLists();
    }

    @Override
    public String getJSONObjectFromXML() {
        return this.getMeasureLibraryService().getJSONObjectFromXML();
    }

    @Override
    public void createAndSaveCQLLookUp(List<QualityDataSetDTO> list, String measureID, String expProfileToAllQDM) {
        this.getMeasureLibraryService().createAndSaveCQLLookUp(list, measureID, expProfileToAllQDM);

    }

    @Override
    public SaveUpdateCQLResult deleteDefinition(String measureId, CQLDefinition toBeDeletedObj) {
        return this.getMeasureLibraryService().deleteDefinition(measureId, toBeDeletedObj);
    }

    @Override
    public SaveUpdateCQLResult deleteFunction(String measureId, CQLFunctions toBeDeletedObj) {
        return this.getMeasureLibraryService().deleteFunction(measureId, toBeDeletedObj);
    }

    @Override
    public SaveUpdateCQLResult deleteParameter(String measureId, CQLParameter toBeDeletedObj) {
        return this.getMeasureLibraryService().deleteParameter(measureId, toBeDeletedObj);
    }

    @Override
    public GetUsedCQLArtifactsResult getUsedCQLArtifacts(String measureId) {
        return this.getMeasureLibraryService().getUsedCqlArtifacts(measureId);
    }

    @Override
    public SaveUpdateCQLResult deleteValueSet(String toBeDeletedValueSetId, String measureID) {
        return this.getMeasureLibraryService().deleteValueSet(toBeDeletedValueSetId, measureID);
    }

    @Override
    public SaveUpdateCQLResult deleteCode(String toBeDeletedId, String measureID) {
        return this.getMeasureLibraryService().deleteCode(toBeDeletedId, measureID);
    }

    @Override
    public CQLQualityDataModelWrapper getCQLValusets(String measureID) {
        return this.getMeasureLibraryService().getCQLValusets(measureID);
    }

    @Override
    public SaveUpdateCQLResult saveCQLValuesettoMeasure(CQLValueSetTransferObject valueSetTransferObject) {
        return this.getMeasureLibraryService().saveCQLValuesettoMeasure(valueSetTransferObject);
    }

    @Override
    public SaveUpdateCQLResult saveIncludeLibrayInCQLLookUp(String measureId, CQLIncludeLibrary toBeModifiedObj, CQLIncludeLibrary currentObj, List<CQLIncludeLibrary> incLibraryList) throws InvalidLibraryException {
        return this.getMeasureLibraryService().saveIncludeLibrayInCQLLookUp(measureId, toBeModifiedObj, currentObj, incLibraryList);
    }

    @Override
    public SaveUpdateCQLResult getMeasureCQLDataForLoad(String measureId) {
        return this.getMeasureLibraryService().getMeasureCQLDataForLoad(measureId);
    }

    @Override
    public SaveUpdateCQLResult getMeasureCQLData(String measureId) {
        return this.getMeasureLibraryService().getMeasureCQLData(measureId);
    }

    @Override
    public SaveUpdateCQLResult getMeasureCQLFileData(String measureId) {
        return this.getMeasureLibraryService().getMeasureCQLFileData(measureId);
    }

    @Override
    public SaveUpdateCQLResult getMeasureCQLLibraryData(String measureId) {
        return this.getMeasureLibraryService().getMeasureCQLLibraryData(measureId);
    }

    @Override
    public SaveUpdateCQLResult deleteInclude(String currentMeasureId, CQLIncludeLibrary toBeModifiedIncludeObj) {
        return this.getMeasureLibraryService().deleteInclude(currentMeasureId, toBeModifiedIncludeObj);
    }

    @Override
    public VsacApiResult updateCQLVSACValueSets(String currentMeasureId, String expansionId) {
        String sessionId = getThreadLocalRequest().getSession().getId();
        return this.getMeasureLibraryService().updateCQLVSACValueSets(currentMeasureId, expansionId, sessionId);
    }

    @Override
    public SaveUpdateCQLResult saveCQLCodestoMeasure(MatCodeTransferObject transferObject) {
        return this.getMeasureLibraryService().saveCQLCodestoMeasure(transferObject);
    }

    @Override
    public SaveUpdateCQLResult saveCQLCodeListToMeasure(List<CQLCode> codeList, String measureId) {
        return this.getMeasureLibraryService().saveCQLCodeListToMeasure(codeList, measureId);
    }

    @Override
    public CQLCodeWrapper getCQLCodes(String measureID) {
        return this.getMeasureLibraryService().getCQLCodes(measureID);
    }

    @Override
    public CQLQualityDataModelWrapper saveValueSetList(List<CQLValueSetTransferObject> transferObjectList,
                                                       List<CQLQualityDataSetDTO> appliedValueSetList, String measureId) {
        return this.getMeasureLibraryService().saveValueSetList(transferObjectList, appliedValueSetList, measureId);
    }

    @Override
    public ManageMeasureSearchModel searchComponentMeasures(MeasureSearchModel searchModel) {
        return this.getMeasureLibraryService().searchComponentMeasures(searchModel);
    }

    @Override
    public ManageCompositeMeasureDetailModel buildCompositeMeasure(ManageCompositeMeasureDetailModel compositeMeasure) {
        return this.getMeasureLibraryService().buildCompositeMeasure(compositeMeasure);
    }

    @Override
    public CompositeMeasureValidationResult validateCompositeMeasure(
            ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel) {
        return this.getMeasureLibraryService().validateCompositeMeasure(manageCompositeMeasureDetailModel);
    }

    @Override
    public List<ComponentMeasureTabObject> getCQLLibraryInformationForComponentMeasure(String compositeMeasureId) {
        return this.getMeasureLibraryService().getCQLLibraryInformationForComponentMeasure(compositeMeasureId);
    }

    @Override
    public GenericResult checkIfMeasureIsUsedAsComponentMeasure(String currentMeasureId) {
        return this.getMeasureLibraryService().checkIfMeasureIsUsedAsComponentMeasure(currentMeasureId);
    }

    @Override
    public Boolean isCompositeMeasure(String currentMeasureId) {
        return this.getMeasureLibraryService().isCompositeMeasure(currentMeasureId);
    }

    @Override
    public MeasureDetailsModel getMeasureDetailsAndLogRecentMeasure(String measureId, String userId) {
        try {
            ManageMeasureDetailModel manageMeasureDetailModel = getManageMeasureDetailModel(measureId, userId);
            MeasureDetailModelMapper measureDetailModelMapper = new ManageMeasureDetailModelMapper(manageMeasureDetailModel);
            MeasureDetailsModel measureDetailsModel = measureDetailModelMapper.getMeasureDetailsModel(isCompositeMeasure(measureId));
            return measureDetailsModel;
        } catch (RuntimeException re) {
            log("MeasureServiceImpl::getMeasureDetailsAndLogRecentMeasure " + re.getMessage(), re);
            throw re;
        }
    }

    private ManageMeasureDetailModel getManageMeasureDetailModel(String currentMeasureId, String loggedinUserId) {
        ManageMeasureDetailModel manageMeasureDetailModel;
        if (isCompositeMeasure(currentMeasureId)) {
            manageMeasureDetailModel = getCompositeMeasure(currentMeasureId);
        } else {
            manageMeasureDetailModel = getMeasure(currentMeasureId);
        }

        if (manageMeasureDetailModel != null) {
            getMeasureLibraryService().recordRecentMeasureActivity(currentMeasureId, loggedinUserId);
        }
        return manageMeasureDetailModel;
    }

    @Override
    public int generateAndSaveMaxEmeasureId(boolean isEditable, String measureId) {
        return this.getMeasureLibraryService().generateAndSaveMaxEmeasureId(isEditable, measureId);
    }

    @Override
    public String getHumanReadableForMeasureDetails(String measureId, String measureModel) {
        return this.getMeasureLibraryService().getHumanReadableForMeasureDetails(measureId, measureModel);
    }

    @Override
    public boolean checkIfLibraryNameExists(String libraryName, String setId) {
        return this.getMeasureLibraryService().libraryNameExists(libraryName, setId);
    }

}
