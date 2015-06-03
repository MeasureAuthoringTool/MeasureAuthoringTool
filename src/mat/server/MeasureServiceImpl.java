package mat.server;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import mat.DTO.MeasureNoteDTO;
import mat.client.clause.clauseworkspace.model.MeasureDetailResult;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.clause.clauseworkspace.model.SortedClauseMapResult;
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
import mat.model.MatValueSet;
import mat.model.MeasureType;
import mat.model.Organization;
import mat.model.QualityDataModelWrapper;
import mat.model.QualityDataSetDTO;
import mat.model.RecentMSRActivityLog;
import mat.server.service.MeasureLibraryService;

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
	public void appendAndSaveNode(MeasureXmlModel measureXmlModel,
			String nodeName) {
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
	public void saveAndDeleteMeasure(String measureID) {
		this.getMeasureLibraryService().saveAndDeleteMeasure(measureID);
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
	public boolean validateMeasureXmlinpopulationWorkspace(
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
	 * @see mat.client.measure.service.MeasureService#getAppliedQDMForItemCount(java.lang.String, boolean)
	 */
	@Override
	public List<QualityDataSetDTO> getAppliedQDMForItemCount(
			String measureId, boolean checkForSupplementData) {
		return this.getMeasureLibraryService().getAppliedQDMForItemCount(measureId, checkForSupplementData);
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
	public QualityDataModelWrapper getDefaultSDEFromMeasureXml(String measureId) {
		// TODO Auto-generated method stub
		return this.getMeasureLibraryService().getDefaultSDEFromMeasureXml(measureId);
	}
	
}
