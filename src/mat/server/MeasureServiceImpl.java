package mat.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.cqframework.cql.cql2elm.CQLtoELM;
import org.cqframework.cql.cql2elm.CqlTranslatorException;

import mat.DTO.MeasureNoteDTO;
import mat.client.clause.clauseworkspace.model.MeasureDetailResult;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.clause.clauseworkspace.model.SortedClauseMapResult;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureShareModel;
import mat.client.measure.MeasureNotesModel;
import mat.client.measure.TransferMeasureOwnerShipModel;
import mat.client.measure.service.MeasureService;
import mat.client.measure.service.SaveMeasureNotesResult;
import mat.client.measure.service.SaveMeasureResult;
import mat.client.measure.service.ValidateMeasureResult;
import mat.client.shared.MatException;
import mat.model.CQLValueSetTransferObject;
import mat.model.MatValueSet;
import mat.model.MeasureType;
import mat.model.Organization;
import mat.model.QualityDataModelWrapper;
import mat.model.QualityDataSetDTO;
import mat.model.RecentMSRActivityLog;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLKeywords;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataModelWrapper;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.server.service.MeasureLibraryService;
import mat.shared.CQLErrors;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.SaveUpdateCQLResult;

// TODO: Auto-generated Javadoc
/**
 * The Class MeasureServiceImpl.
 */
public class MeasureServiceImpl extends SpringRemoteServiceServlet implements
MeasureService {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2280421300224680146L;
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#appendAndSaveNode(mat.client.clause.clauseworkspace.model.MeasureXmlModel, java.lang.String)
	 */
	@Override
	public void appendAndSaveNode(MeasureXmlModel measureXmlModel, String nodeName) {
		this.getMeasureLibraryService().appendAndSaveNode(measureXmlModel, nodeName);
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#cloneMeasureXml(boolean, java.lang.String, java.lang.String)
	 */
	@Override
	public void cloneMeasureXml(boolean creatingDraft, String oldMeasureId,
			String clonedMeasureId) {
		this.getMeasureLibraryService().cloneMeasureXml(creatingDraft, oldMeasureId, clonedMeasureId);
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#createAndSaveElementLookUp(java.util.ArrayList, java.lang.String)
	 */
	@Override
	public void createAndSaveElementLookUp(List<QualityDataSetDTO> list,
			String measureID, String expProfileToAllQDM) {
		this.getMeasureLibraryService().createAndSaveElementLookUp(list, measureID, expProfileToAllQDM);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#deleteMeasureNotes(mat.DTO.MeasureNoteDTO)
	 */
	@Override
	public void deleteMeasureNotes(MeasureNoteDTO measureNoteDTO) {
		this.getMeasureLibraryService().deleteMeasureNotes(measureNoteDTO);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#generateAndSaveMaxEmeasureId(mat.client.measure.ManageMeasureDetailModel)
	 */
	@Override
	public int generateAndSaveMaxEmeasureId(ManageMeasureDetailModel measureId) {
		return this.getMeasureLibraryService().generateAndSaveMaxEmeasureId(measureId);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#getAllMeasureNotesByMeasureID(java.lang.String)
	 */
	@Override
	public MeasureNotesModel getAllMeasureNotesByMeasureID(String measureID) {
		return this.getMeasureLibraryService().getAllMeasureNotesByMeasureID(measureID);
	}
	
	/** Gets the all recent measure for user.
	 * 
	 * @param userId - String userId.
	 * @return ManageMeasureSearchModel */
	@Override
	public ManageMeasureSearchModel getAllRecentMeasureForUser(String userId) {
		return this.getMeasureLibraryService().getAllRecentMeasureForUser(userId);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#getAppliedQDMFromMeasureXml(java.lang.String, boolean)
	 */
	//	@Override
	//	public List<QualityDataSetDTO> getAppliedQDMFromMeasureXml(
	//			String measureId, boolean checkForSupplementData) {
	//		return this.getMeasureLibraryService().getAppliedQDMFromMeasureXml(measureId, checkForSupplementData);
	//	}
	
	@Override
	public QualityDataModelWrapper getAppliedQDMFromMeasureXml(
			String measureId, boolean checkForSupplementData) {
		return this.getMeasureLibraryService().getAppliedQDMFromMeasureXml(measureId, checkForSupplementData);
	}
	
	@Override
	public CQLQualityDataModelWrapper getCQLAppliedQDMFromMeasureXml(String measureId, boolean checkForSupplementData) {
		return this.getMeasureLibraryService().getCQLAppliedQDMFromMeasureXml(measureId, checkForSupplementData);
	}
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#getMaxEMeasureId()
	 */
	@Override
	public int getMaxEMeasureId() {
		return this.getMeasureLibraryService().getMaxEMeasureId();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#getMeasure(java.lang.String)
	 */
	@Override
	public ManageMeasureDetailModel getMeasure(String key) {
		return this.getMeasureLibraryService().getMeasure(key);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#getMeasureAndLogRecentMeasure(java.lang.String, java.lang.String)
	 */
	@Override
	public ManageMeasureDetailModel getMeasureAndLogRecentMeasure(String measureId, String userId) {
		ManageMeasureDetailModel manageMeasureDetailModel = getMeasure(measureId);
		if(manageMeasureDetailModel != null){
			getMeasureLibraryService().recordRecentMeasureActivity(measureId, userId);
		}
		return manageMeasureDetailModel;
	}
	
	/**
	 * Gets the measure library service.
	 * 
	 * @return the measure library service
	 */
	public MeasureLibraryService getMeasureLibraryService(){
		return (MeasureLibraryService) context.getBean("measureLibraryService");
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#getMeasureXmlForMeasure(java.lang.String)
	 */
	@Override
	public MeasureXmlModel getMeasureXmlForMeasure(String measureId) {
		return this.getMeasureLibraryService().getMeasureXmlForMeasure(measureId);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#getRecentMeasureActivityLog(java.lang.String)
	 */
	@Override
	public List<RecentMSRActivityLog> getRecentMeasureActivityLog(String userId) {
		return this.getMeasureLibraryService().getRecentMeasureActivityLog(userId);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#getUsersForShare(java.lang.String, int, int)
	 */
	@Override
	public ManageMeasureShareModel getUsersForShare(String measureId,
			int startIndex, int pageSize) {
		return this.getMeasureLibraryService().getUsersForShare(measureId, startIndex, pageSize);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#isMeasureLocked(java.lang.String)
	 */
	@Override
	public boolean isMeasureLocked(String id) {
		return this.getMeasureLibraryService().isMeasureLocked(id);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#resetLockedDate(java.lang.String, java.lang.String)
	 */
	@Override
	public SaveMeasureResult resetLockedDate(String measureId, String userId) {
		return this.getMeasureLibraryService().resetLockedDate(measureId, userId);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#save(mat.client.measure.ManageMeasureDetailModel)
	 */
	@Override
	public SaveMeasureResult save(ManageMeasureDetailModel model) {
		return this.getMeasureLibraryService().save(model);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#saveAndDeleteMeasure(java.lang.String)
	 */
	@Override
	public void saveAndDeleteMeasure(String measureID,  String loginUserId) {
		this.getMeasureLibraryService().saveAndDeleteMeasure(measureID,loginUserId);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#saveFinalizedVersion(java.lang.String, boolean, java.lang.String)
	 */
	@Override
	public SaveMeasureResult saveFinalizedVersion(String measureId,
			boolean isMajor, String version) {
		return this.getMeasureLibraryService().saveFinalizedVersion(measureId, isMajor, version);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#saveMeasureDetails(mat.client.measure.ManageMeasureDetailModel)
	 */
	@Override
	public SaveMeasureResult saveMeasureDetails(ManageMeasureDetailModel model) {
		return this.getMeasureLibraryService().saveMeasureDetails(model);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#saveMeasureNote(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public SaveMeasureNotesResult saveMeasureNote(MeasureNoteDTO model,
			String measureId, String userId) {
		return this.getMeasureLibraryService().saveMeasureNote(model, measureId, userId);
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#saveMeasureXml(mat.client.clause.clauseworkspace.model.MeasureXmlModel)
	 */
	@Override
	public void saveMeasureXml(MeasureXmlModel measureXmlModel) {
		this.getMeasureLibraryService().saveMeasureXml(measureXmlModel);
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#search(java.lang.String, int, int, int)
	 */
	@Override
	public ManageMeasureSearchModel search(String searchText, int startIndex,
			int pageSize, int filter) {
		return this.getMeasureLibraryService().search(searchText, startIndex, pageSize, filter);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#searchMeasuresForDraft(java.lang.String)
	 */
	@Override
	public ManageMeasureSearchModel searchMeasuresForDraft(String searchText) {
		return this.getMeasureLibraryService().searchMeasuresForDraft(searchText);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#searchMeasuresForVersion(java.lang.String)
	 */
	@Override
	public ManageMeasureSearchModel searchMeasuresForVersion(String searchText) {
		return this.getMeasureLibraryService().searchMeasuresForVersion(searchText);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#searchUsers(int, int)
	 */
	@Override
	public TransferMeasureOwnerShipModel searchUsers(String searchText, int startIndex,
			int pageSize) {
		return this.getMeasureLibraryService().searchUsers(searchText, startIndex, pageSize);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#transferOwnerShipToUser(java.util.List, java.lang.String)
	 */
	@Override
	public void transferOwnerShipToUser(List<String> list, String toEmail) {
		this.getMeasureLibraryService().transferOwnerShipToUser(list, toEmail);
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#updateLockedDate(java.lang.String, java.lang.String)
	 */
	@Override
	public SaveMeasureResult updateLockedDate(String measureId, String userId) {
		return this.getMeasureLibraryService().updateLockedDate(measureId, userId);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#updateMeasureNotes(mat.DTO.MeasureNoteDTO, java.lang.String)
	 */
	@Override
	public void updateMeasureNotes(MeasureNoteDTO measureNoteDTO, String userId) {
		this.getMeasureLibraryService().updateMeasureNotes(measureNoteDTO, userId);
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#updateMeasureXML(mat.model.QualityDataSetDTO, mat.model.QualityDataSetDTO, java.lang.String)
	 */
	@Override
	public void updateMeasureXML(QualityDataSetDTO modifyWithDTO,
			QualityDataSetDTO modifyDTO, String measureId) {
		this.getMeasureLibraryService().updateMeasureXML(modifyWithDTO, modifyDTO, measureId);
		
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#updatePrivateColumnInMeasure(java.lang.String, boolean)
	 */
	@Override
	public void updatePrivateColumnInMeasure(String measureId, boolean isPrivate) {
		this.getMeasureLibraryService().updatePrivateColumnInMeasure(measureId, isPrivate);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#updateUsersShare(mat.client.measure.ManageMeasureShareModel)
	 */
	@Override
	public void updateUsersShare(ManageMeasureShareModel model) {
		this.getMeasureLibraryService().updateUsersShare(model);
	}
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#validateMeasureForExport(java.lang.String, java.util.ArrayList)
	 */
	@Override
	public ValidateMeasureResult validateMeasureForExport(String key , List<MatValueSet> matValueSetList)
			throws MatException {
		return this.getMeasureLibraryService().validateMeasureForExport(key, matValueSetList);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#getHumanReadableForNode(java.lang.String, java.lang.String)
	 */
	@Override
	public String getHumanReadableForNode(String measureId, String populationSubXML){
		return this.getMeasureLibraryService().getHumanReadableForNode(measureId, populationSubXML);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#saveMeasureAtPackage(mat.client.measure.ManageMeasureDetailModel)
	 */
	@Override
	public SaveMeasureResult saveMeasureAtPackage(ManageMeasureDetailModel model) {
		return this.getMeasureLibraryService().saveMeasureAtPackage(model);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#saveSubTreeInMeasureXml(mat.client.clause.clauseworkspace.model.MeasureXmlModel, java.lang.String, java.lang.String)
	 */
	@Override
	public SortedClauseMapResult saveSubTreeInMeasureXml(MeasureXmlModel measureXmlModel , String nodeName, String nodeUUID) {
		return this.getMeasureLibraryService().saveSubTreeInMeasureXml(measureXmlModel , nodeName, nodeUUID);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#checkAndDeleteSubTree(java.lang.String, java.lang.String)
	 */
	@Override
	public HashMap<String, String> checkAndDeleteSubTree(String measureId, String subTreeUUID){
		return this.getMeasureLibraryService().checkAndDeleteSubTree(measureId, subTreeUUID);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#isSubTreeReferredInLogic(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isSubTreeReferredInLogic(String measureId, String subTreeUUID){
		return this.getMeasureLibraryService().isSubTreeReferredInLogic(measureId, subTreeUUID);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#getComponentMeasures(java.util.List)
	 */
	@Override
	public ManageMeasureSearchModel getComponentMeasures(List<String> measureIds){
		return getMeasureLibraryService().getComponentMeasures(measureIds);
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#validatePackageGrouping(mat.client.measure.ManageMeasureDetailModel)
	 */
	@Override
	public ValidateMeasureResult validatePackageGrouping(
			ManageMeasureDetailModel model) {
		return this.getMeasureLibraryService().validatePackageGrouping(
				model);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#validateMeasureXmlinpopulationWorkspace(mat.client.clause.clauseworkspace.model.MeasureXmlModel)
	 */
	@Override
	public ValidateMeasureResult validateMeasureXmlinpopulationWorkspace(
			MeasureXmlModel measureXmlModel) {
		return this.getMeasureLibraryService().validateMeasureXmlAtCreateMeasurePackager(measureXmlModel);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#updateComponentMeasuresFromXml(java.lang.String)
	 */
	@Override
	public void updateMeasureXmlForDeletedComponentMeasureAndOrg(String measureId) {
		
		this.getMeasureLibraryService().updateMeasureXmlForDeletedComponentMeasureAndOrg(measureId);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#validateForGroup(mat.client.measure.ManageMeasureDetailModel)
	 */
	@Override
	public ValidateMeasureResult validateForGroup(ManageMeasureDetailModel model) {
		
		return this.getMeasureLibraryService().validateForGroup(model);
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#getAllMeasureTypes()
	 */
	@Override
	public List<MeasureType> getAllMeasureTypes(){
		return this.getMeasureLibraryService().getAllMeasureTypes();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#getAllAddEditAuthors()
	 */
	@Override
	public List<Organization> getAllOrganizations() {
		
		return this.getMeasureLibraryService().getAllOrganizations();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#saveSubTreeOccurrence(mat.client.clause.clauseworkspace.model.MeasureXmlModel, java.lang.String, java.lang.String)
	 */
	@Override
	public SortedClauseMapResult saveSubTreeOccurrence(MeasureXmlModel measureXmlModel, String nodeName, String nodeUUID) {
		return this.getMeasureLibraryService().saveSubTreeOccurrence(measureXmlModel, nodeName, nodeUUID);
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#isQDMVariableEnabled(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isQDMVariableEnabled(String measureId, String subTreeUUID) {
		return this.getMeasureLibraryService().isQDMVariableEnabled(measureId, subTreeUUID);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#getSortedClauseMap(java.lang.String)
	 */
	@Override
	public LinkedHashMap<String, String> getSortedClauseMap(String measureId) {
		return this.getMeasureLibraryService().getSortedClauseMap(measureId);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#getMeasureXmlForMeasureAndSortedSubTreeMap(java.lang.String)
	 */
	@Override
	public SortedClauseMapResult getMeasureXmlForMeasureAndSortedSubTreeMap(
			String currentMeasureId) {
		return this.getMeasureLibraryService().getMeasureXmlForMeasureAndSortedSubTreeMap(currentMeasureId);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#getUsedStewardAndDevelopersList(java.lang.String)
	 */
	@Override
	public MeasureDetailResult getUsedStewardAndDevelopersList(String measureId) {
		return this.getMeasureLibraryService().getUsedStewardAndDevelopersList(measureId);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#updateMeasureXMLForExpansionIdentifier(java.util.List, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateMeasureXMLForExpansionIdentifier(List<QualityDataSetDTO> modifyWithDTOList,
			String measureId, String expansionProfile) {
		this.getMeasureLibraryService().updateMeasureXMLForExpansionIdentifier(modifyWithDTOList, measureId, expansionProfile);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#getDefaultSDEFromMeasureXml(java.lang.String)
	 */
	@Override
	public QualityDataModelWrapper getDefaultSDEFromMeasureXml(String measureId) {
		// TODO Auto-generated method stub
		return this.getMeasureLibraryService().getDefaultSDEFromMeasureXml(measureId);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#parseCQL(java.lang.String)
	 */
	@Override
	public CQLModel parseCQL(String cqlBuilder) {
		return this.getMeasureLibraryService().parseCQL(cqlBuilder);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#getCQLData(java.lang.String)
	 */
	@Override
	public SaveUpdateCQLResult getCQLData(String measureId) {
		return this.getMeasureLibraryService().getCQLData(measureId);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#getCQLData(java.lang.String)
	 */
	@Override
	public SaveUpdateCQLResult getCQLFileData(String measureId) {
		return this.getMeasureLibraryService().getCQLFileData(measureId);
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#saveAndModifyDefinitions(java.lang.String, mat.model.cql.CQLDefinition, mat.model.cql.CQLDefinition, java.util.List)
	 */
	@Override
	public SaveUpdateCQLResult saveAndModifyDefinitions(String measureId, CQLDefinition toBemodifiedObj,
			CQLDefinition currentObj, List<CQLDefinition> definitionList){
		return this.getMeasureLibraryService().saveAndModifyDefinitions(measureId, toBemodifiedObj, currentObj, definitionList);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#saveAndModifyParameters(java.lang.String, mat.model.cql.CQLParameter, mat.model.cql.CQLParameter, java.util.List)
	 */
	@Override
	public SaveUpdateCQLResult saveAndModifyParameters(String measureId, CQLParameter toBemodifiedObj,
			CQLParameter currentObj, List<CQLParameter> parameterList){
		return this.getMeasureLibraryService().saveAndModifyParameters(measureId, toBemodifiedObj, currentObj, parameterList);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#saveAndModifyFunctions(java.lang.String, mat.model.cql.CQLFunctions, mat.model.cql.CQLFunctions, java.util.List)
	 */
	@Override
	public SaveUpdateCQLResult saveAndModifyFunctions(String measureId, CQLFunctions toBeModifiedObj,
			CQLFunctions currentObj, List<CQLFunctions> functionsList){
		return this.getMeasureLibraryService().saveAndModifyFunctions(measureId, toBeModifiedObj, currentObj, functionsList);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#saveAndModifyCQLGeneralInfo(java.lang.String, java.lang.String)
	 */
	@Override
	public SaveUpdateCQLResult saveAndModifyCQLGeneralInfo(
			String currentMeasureId, String context) {
		return this.getMeasureLibraryService().saveAndModifyCQLGeneralInfo(currentMeasureId, context);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#getCQLDataTypeList()
	 */
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
	public SaveUpdateCQLResult parseCQLForErrors(String measureId) {
		
		MeasureXmlModel measureXML = getMeasureXmlForMeasure(measureId);
		String cqlFileString = CQLUtilityClass.getCqlString(CQLUtilityClass.getCQLStringFromMeasureXML(measureXML.getXml(),measureId),"").toString();
		
		return parseCQLStringForError(cqlFileString);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#parseCQLStringForError(java.lang.String)
	 */
	@Override
	public SaveUpdateCQLResult parseCQLStringForError( String cqlFileString) {
		return this.getMeasureLibraryService().parseCQLStringForError(cqlFileString);
	}

	@Override
	public SaveUpdateCQLResult deleteDefinition(String measureId, CQLDefinition toBeDeletedObj, CQLDefinition currentObj,
			List<CQLDefinition> definitionList) {
		return this.getMeasureLibraryService().deleteDefinition(measureId, toBeDeletedObj, currentObj, definitionList);
	}

	@Override
	public SaveUpdateCQLResult deleteFunctions(String measureId, CQLFunctions toBeDeletedObj, CQLFunctions currentObj,
			List<CQLFunctions> functionsList) {
		return this.getMeasureLibraryService().deleteFunctions(measureId, toBeDeletedObj, currentObj, functionsList);
	}

	@Override
	public SaveUpdateCQLResult deleteParameter(String measureId, CQLParameter toBeDeletedObj, CQLParameter currentObj,
			List<CQLParameter> parameterList) {
		return this.getMeasureLibraryService().deleteParameter(measureId, toBeDeletedObj, currentObj, parameterList);
	}
	
	@Override
	public GetUsedCQLArtifactsResult getUsedCQLArtifacts(String measureId) {
		return this.getMeasureLibraryService().getUsedCqlArtifacts(measureId);
		
	}
	
	@Override
	public SaveUpdateCQLResult createAndSaveCQLElementLookUp(String Uuid, List<CQLQualityDataSetDTO> list, String measureID,
			String expProfileToAllQDM) {
		return this.getMeasureLibraryService().createAndSaveCQLElementLookUp(Uuid, list, measureID, expProfileToAllQDM);
		
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
	public SaveUpdateCQLResult saveCQLUserDefinedValuesettoMeasure(CQLValueSetTransferObject valueSetTransferObject) {
		return this.getMeasureLibraryService().saveCQLUserDefinedValuesettoMeasure(valueSetTransferObject);
	}
	
	@Override
	public SaveUpdateCQLResult updateCQLValuesetsToMeasure(
			CQLValueSetTransferObject matValueSetTransferObject) {
		return this.getMeasureLibraryService().updateCQLValueSetstoMeasure(matValueSetTransferObject);
	}
	
	@Override
	public SaveUpdateCQLResult saveIncludeLibrayInCQLLookUp(String measureId,
			CQLIncludeLibrary toBeModifiedObj, CQLIncludeLibrary currentObj,
			List<CQLIncludeLibrary> incLibraryList){
		return this.getMeasureLibraryService().saveIncludeLibrayInCQLLookUp(measureId, toBeModifiedObj, currentObj, incLibraryList);
	}

}
