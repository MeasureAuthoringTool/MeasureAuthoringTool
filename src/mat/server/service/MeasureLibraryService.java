package mat.server.service;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import javax.xml.xpath.XPathExpressionException;
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
import mat.client.measure.service.SaveMeasureNotesResult;
import mat.client.measure.service.SaveMeasureResult;
import mat.client.measure.service.ValidateMeasureResult;
import mat.client.shared.MatException;
import mat.model.CQLValueSetTransferObject;
import mat.model.MatValueSet;
import mat.model.MeasureOwnerReportDTO;
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
import mat.server.util.XmlProcessor;
import mat.shared.CQLValidationResult;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.SaveUpdateCQLResult;

// TODO: Auto-generated Javadoc
/**
 * The Interface MeasureLibraryService.
 */
public interface MeasureLibraryService {
	
	/**
	 * Append and save node.
	 * 
	 * @param measureXmlModel
	 *            the measure xml model
	 * @param nodeName
	 *            the node name
	 * @param measureXmlModel
	 *            the new measure xml model
	 * @param nodeName
	 *            the new node name
	 */
	void appendAndSaveNode(MeasureXmlModel measureXmlModel, String nodeName);
	
	/**
	 * Check for timing elements and append.
	 * 
	 * @param xmlProcessor
	 *            the xml processor
	 */
	void checkForTimingElementsAndAppend(XmlProcessor xmlProcessor);
	
	/**
	 * Clone measure xml.
	 * 
	 * @param creatingDraft
	 *            the creating draft
	 * @param oldMeasureId
	 *            the old measure id
	 * @param clonedMeasureId
	 *            the cloned measure id
	 */
	void cloneMeasureXml(boolean creatingDraft, String oldMeasureId,
			String clonedMeasureId);
	
	/**
	 * Creates the and save element look up.
	 *
	 * @param list            the list
	 * @param measureID            the measure id
	 * @param expProfileToAllQDM the exp profile to all qdm
	 */
	void createAndSaveElementLookUp(List<QualityDataSetDTO> list,
			String measureID, String expProfileToAllQDM);
	
	/**
	 * Delete measure notes.
	 * 
	 * @param measureNoteDTO
	 *            the measure note dto
	 */
	void deleteMeasureNotes(MeasureNoteDTO measureNoteDTO);
	
	/**
	 * Generate and save max emeasure id.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the int
	 */
	int generateAndSaveMaxEmeasureId(ManageMeasureDetailModel measureId);
	
	/**
	 * Gets the all measure notes by measure id.
	 * 
	 * @param measureID
	 *            the measure id
	 * @return the all measure notes by measure id
	 */
	MeasureNotesModel getAllMeasureNotesByMeasureID(String measureID);
	
	/** Gets the all recent measure for user.
	 * 
	 * @param userId the user id
	 * @return the all recent measure for user */
	ManageMeasureSearchModel getAllRecentMeasureForUser(String userId);
	
	/**
	 * Gets the applied qdm from measure xml.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param checkForSupplementData
	 *            the check for supplement data
	 * @return the applied qdm from measure xml
	 */
	//	ArrayList<QualityDataSetDTO> getAppliedQDMFromMeasureXml(String measureId,
	//			boolean checkForSupplementData);
	
	QualityDataModelWrapper getAppliedQDMFromMeasureXml(String measureId,
			boolean checkForSupplementData);
	
	/**
	 * Gets the max e measure id.
	 * 
	 * @return the max e measure id
	 */
	int getMaxEMeasureId();
	
	/**
	 * Gets the measure.
	 * 
	 * @param key
	 *            the key
	 * @return the measure
	 */
	ManageMeasureDetailModel getMeasure(String key);
	
	/**
	 * Gets the measure xml for measure.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the measure xml for measure
	 */
	public MeasureXmlModel getMeasureXmlForMeasure(String measureId);
	
	/**
	 * Gets recently used measures by the given user ID in the descending order of time from recent measure activity log.
	 *
	 * @param userId the user id
	 * @return the recent measure activity log
	 */
	List<RecentMSRActivityLog> getRecentMeasureActivityLog(String userId);
	
	/**
	 * Gets the users for share.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @return the users for share
	 */
	ManageMeasureShareModel getUsersForShare(String measureId,
			int startIndex, int pageSize);
	
	/**
	 * Checks if is measure locked.
	 * 
	 * @param id
	 *            the id
	 * @return true, if is measure locked
	 */
	boolean isMeasureLocked(String id);
	
	/**
	 * Record measure in recent measure activity log for the given measure ID and user ID.
	 *
	 * @param measureId the measure id
	 * @param userId the user id
	 */
	void recordRecentMeasureActivity(String measureId, String userId);
	
	/**
	 * Reset locked date.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param userId
	 *            the user id
	 * @return the save measure result
	 */
	SaveMeasureResult resetLockedDate(String measureId, String userId);
	
	/**
	 * Save.
	 * 
	 * @param model
	 *            the model
	 * @return the save measure result
	 */
	SaveMeasureResult save(ManageMeasureDetailModel model);
	
	/**
	 * Save and delete measure.
	 * 
	 * @param measureID
	 *            the measure id
	 *            
	 * @param loginUserId
	 *            the loginUser id
	 */
	void saveAndDeleteMeasure(String measureID,  String loginUserId);
	
	/**
	 * Save finalized version.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param isMajor
	 *            the is major
	 * @param version
	 *            the version
	 * @return the save measure result
	 */
	SaveMeasureResult saveFinalizedVersion(String measureId,
			boolean isMajor, String version);
	
	/**
	 * Save measure details.
	 * 
	 * @param model
	 *            the model
	 * @return the save measure result
	 */
	SaveMeasureResult saveMeasureDetails(ManageMeasureDetailModel model);
	
	
	/**
	 * Save measure note.
	 *
	 * @param model the model
	 * @param measureId the measure id
	 * @param userId the user id
	 * @return the save measure notes result
	 */
	SaveMeasureNotesResult saveMeasureNote(MeasureNoteDTO model,
			String measureId, String userId);
	
	/**
	 * Save measure xml.
	 * 
	 * @param measureXmlModel
	 *            the measure xml model
	 */
	void saveMeasureXml(MeasureXmlModel measureXmlModel);
	
	/**
	 * Search.
	 * 
	 * @param searchText
	 *            the search text
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @param filter
	 *            the filter
	 * @return the manage measure search model
	 */
	ManageMeasureSearchModel search(String searchText, int startIndex,
			int pageSize, int filter);
	
	
	/**
	 * Search measures for draft.
	 *
	 * @param searchText the search text
	 * @return the manage measure search model
	 */
	ManageMeasureSearchModel searchMeasuresForDraft(String searchText);
	
	/**
	 * Search measures for version.
	 *
	 * @param searchText the search text
	 * @return the manage measure search model
	 */
	ManageMeasureSearchModel searchMeasuresForVersion(String searchText);
	
	/**
	 * Search users.
	 *
	 * @param searchText the search text
	 * @param startIndex the start index
	 * @param pageSize the page size
	 * @return the transfer measure owner ship model
	 */
	TransferMeasureOwnerShipModel searchUsers(String searchText, int startIndex,
			int pageSize);
	
	/**
	 * Transfer owner ship to user.
	 * 
	 * @param list
	 *            the list
	 * @param toEmail
	 *            the to email
	 */
	void transferOwnerShipToUser(List<String> list, String toEmail);
	
	/**
	 * Update locked date.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param userId
	 *            the user id
	 * @return the save measure result
	 */
	SaveMeasureResult updateLockedDate(String measureId, String userId);
	
	/**
	 * Update measure notes.
	 * 
	 * @param measureNoteDTO
	 *            the measure note dto
	 * @param userId
	 *            the user id
	 */
	void updateMeasureNotes(MeasureNoteDTO measureNoteDTO, String userId);
	
	/**
	 * Update measure xml.
	 * 
	 * @param modifyWithDTO
	 *            the modify with dto
	 * @param modifyDTO
	 *            the modify dto
	 * @param measureId
	 *            the measure id
	 */
	void updateMeasureXML(QualityDataSetDTO modifyWithDTO,
			QualityDataSetDTO modifyDTO, String measureId);
	
	/**
	 * Update private column in measure.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param isPrivate
	 *            the is private
	 */
	void updatePrivateColumnInMeasure(String measureId, boolean isPrivate);
	
	/**
	 * Update users share.
	 * 
	 * @param model
	 *            the model
	 */
	void updateUsersShare(ManageMeasureShareModel model);
	/**
	 * Validate measure for export.
	 * 
	 * @param key
	 *            the key
	 * @param matValueSetList
	 *            the mat value set list
	 * @return the validate measure result
	 * @throws MatException
	 *             the mat exception
	 */
	ValidateMeasureResult validateMeasureForExport(String key,
			List<MatValueSet> matValueSetList) throws MatException;
	
	/**
	 * Save measure at package.
	 *
	 * @param model the model
	 * @return the save measure result
	 */
	SaveMeasureResult saveMeasureAtPackage(ManageMeasureDetailModel model);
	
	/**
	 * Save SubTree - Append new node if new else update exisiting subTreeNode in measureXml.
	 *
	 * @param measureXmlModel the measure xml model
	 * @param nodeName the node name
	 * @param nodeUUID the node uuid
	 * @return the sorted clause map result
	 */
	SortedClauseMapResult saveSubTreeInMeasureXml(MeasureXmlModel measureXmlModel, String nodeName, String nodeUUID);
	
	/**
	 * Check and delete sub tree.
	 *
	 * @param measureId the measure id
	 * @param subTreeUUID the sub tree uuid
	 * @return true, if successful
	 */
	HashMap<String, String> checkAndDeleteSubTree(String measureId, String subTreeUUID);
	
	/**
	 * Gets the formatted release date.
	 *
	 * @param releaseDate the release date
	 * @return the formatted release date
	 */
	Date getFormattedReleaseDate(String releaseDate);
	
	
	/**
	 * Checks if is sub tree referred in logic.
	 *
	 * @param measureId the measure id
	 * @param subTreeUUID the sub tree uuid
	 * @return true, if is sub tree referred in logic
	 */
	boolean isSubTreeReferredInLogic(String measureId, String subTreeUUID);
	
	/**
	 * Gets the human readable for node.
	 *
	 * @param measureId the measure id
	 * @param populationSubXML the population sub xml
	 * @return the human readable for node
	 */
	String getHumanReadableForNode(String measureId, String populationSubXML);
	
	/**
	 * Gets the component measures.
	 *
	 * @param measureIds the measure ids
	 * @return the component measures
	 */
	ManageMeasureSearchModel getComponentMeasures(List<String> measureIds);
	
	
	
	/**
	 * Validate package grouping.
	 *
	 * @param model the model
	 * @return the string
	 */
	ValidateMeasureResult validatePackageGrouping(ManageMeasureDetailModel model);
	
	/**
	 * Validate measure xmlinpopulation workspace.
	 *
	 * @param measureXmlModel the measure xml model
	 * @return the object
	 */
	ValidateMeasureResult validateMeasureXmlAtCreateMeasurePackager(
			MeasureXmlModel measureXmlModel);
	
	/**
	 * Update component measures on deletion.
	 *
	 * @param measureId the measure id
	 */
	void updateMeasureXmlForDeletedComponentMeasureAndOrg(String measureId);
	
	/**
	 * Validate for group.
	 *
	 * @param model the model
	 * @return the validate measure result
	 */
	ValidateMeasureResult validateForGroup(ManageMeasureDetailModel model);
	
	/**
	 * Gets the all measure types.
	 *
	 * @return the all measure types
	 */
	List<MeasureType> getAllMeasureTypes();
	
	/**
	 * Gets the all authors.
	 *
	 * @return the all authors
	 */
	List<Organization> getAllOrganizations();
	
	/**
	 * Save sub tree occurrence.
	 *
	 * @param measureXmlModel the measure xml model
	 * @param nodeName the node name
	 * @param nodeUUID the node uuid
	 * @return the sorted clause map result
	 */
	SortedClauseMapResult saveSubTreeOccurrence(MeasureXmlModel measureXmlModel, String nodeName, String nodeUUID);
	
	/**
	 * Checks if is QDM variable enabled.
	 *
	 * @param measureId the measure id
	 * @param subTreeUUID the sub tree uuid
	 * @return true, if is QDM variable enabled
	 */
	boolean isQDMVariableEnabled(String measureId, String subTreeUUID);
	
	/**
	 * Gets the sorted clause map.
	 *
	 * @param measureId the measure id
	 * @return the sorted clause map
	 */
	LinkedHashMap<String, String> getSortedClauseMap(String measureId);
	
	/**
	 * Gets the measure xml for measure and sorted sub tree map.
	 *
	 * @param measureId the measure id
	 * @return the measure xml for measure and sorted sub tree map
	 */
	SortedClauseMapResult getMeasureXmlForMeasureAndSortedSubTreeMap(
			String measureId);
	
	/**
	 * Method to get User Steward and Developers List for measure.
	 *
	 * @param measureId the measure id
	 * @return the used steward and developers list
	 */
	MeasureDetailResult getUsedStewardAndDevelopersList(String measureId);
	
	/**
	 * Service to Update Expansion Profile in Measure Xml.
	 *
	 * @param modifyWithDTO the modify with dto
	 * @param measureId the measure id
	 * @param expansionProfile the expansion profile
	 */
	void updateMeasureXMLForExpansionIdentifier(
			List<QualityDataSetDTO> modifyWithDTO, String measureId,
			String expansionProfile);
	
	
	/**
	 * Method to Get Default 4 Supplemental Data Elements for give Measure.
	 *
	 * @param measureId the measure id
	 * @return QualityDataModelWrapper
	 */
	QualityDataModelWrapper getDefaultSDEFromMeasureXml(String measureId);
	
	/**
	 * Gets the measures for measure owner.
	 *
	 * @return the measures for measure owner
	 * @throws XPathExpressionException the x path expression exception
	 */
	List<MeasureOwnerReportDTO> getMeasuresForMeasureOwner() throws XPathExpressionException;
	
	/**
	 * Gets the default expansion identifier.
	 *
	 * @param measureId the measure id
	 * @return the default expansion identifier
	 */
	String getDefaultExpansionIdentifier(String measureId);
	
	/**
	 * Gets the current release version.
	 *
	 * @return the current release version
	 */
	String getCurrentReleaseVersion();
	
	/**
	 * Sets the current release version.
	 *
	 * @param releaseVersion the new current release version
	 */
	void setCurrentReleaseVersion(String releaseVersion);
	
	/**
	 * Parses the cql.
	 *
	 * @param cqlBuilder the cql builder
	 * @return the CQL model
	 */
	CQLModel parseCQL(String cqlBuilder);
	
	//Boolean saveCQLData(CQLModel cqlDataModel);
	
	/**
	 * Gets the CQL data.
	 *
	 * @param measureId the measure id
	 * @return the CQL data
	 */
	SaveUpdateCQLResult getCQLData(String measureId);
	
	/**
	 * Gets the CQL file data.
	 *
	 * @param measureId the measure id
	 * @return the CQL file data
	 */
	SaveUpdateCQLResult getCQLFileData(String measureId);
	
	/**
	 * Validate cql.
	 *
	 * @param cqlModel the cql model
	 * @return the CQL validation result
	 */
	CQLValidationResult validateCQL(CQLModel cqlModel);
	
	/**
	 * Save and modify definitions.
	 *
	 * @param measureId the measure id
	 * @param toBemodifiedObj the to bemodified obj
	 * @param currentObj the current obj
	 * @param definitionList the definition list
	 * @return the save update cql result
	 */
	SaveUpdateCQLResult saveAndModifyDefinitions(String measureId,
			CQLDefinition toBemodifiedObj, CQLDefinition currentObj, List<CQLDefinition> definitionList);
		
	/**
	 * Save and modify parameters.
	 *
	 * @param measureId the measure id
	 * @param toBemodifiedObj the to bemodified obj
	 * @param currentObj the current obj
	 * @param parameterList the parameter list
	 * @return the save update cql result
	 */
	SaveUpdateCQLResult saveAndModifyParameters(String measureId, CQLParameter toBemodifiedObj, CQLParameter currentObj,
			List<CQLParameter> parameterList);
	
	/**
	 * Save and modify cql general info.
	 *
	 * @param currentMeasureId the current measure id
	 * @param context the context
	 * @return the save update cql result
	 */
	SaveUpdateCQLResult saveAndModifyCQLGeneralInfo(String currentMeasureId,
			String context);
	
	/**
	 * Save and modify functions.
	 *
	 * @param measureId the measure id
	 * @param toBeModifiedObj the to be modified obj
	 * @param currentObj the current obj
	 * @param functionsList the functions list
	 * @return the save update cql result
	 */
	SaveUpdateCQLResult saveAndModifyFunctions(String measureId, CQLFunctions toBeModifiedObj, CQLFunctions currentObj,
			List<CQLFunctions> functionsList);
	/**
	 * Delete definition
	 * 
	 * @param measureId the measure id
	 * @param toBeDeletedObj the to be deleted obj
	 * @param currentObj the current obj
	 * @param definitionList the definition list
	 * @return the save and update cql result
	 */
	SaveUpdateCQLResult deleteDefinition(String measureId, CQLDefinition toBeDeletedObj, CQLDefinition currentObj,
			List<CQLDefinition> definitionList);

	/**
	 * Delete functions
	 * 
	 * @param measureId the measure id
	 * @param toBeDeletedObj the to be deleted obj
	 * @param currentObj the current obj
	 * @param functionsList the functions list
	 * @return the save and update cql result
	 */
	SaveUpdateCQLResult deleteFunctions(String measureId, CQLFunctions toBeDeletedObj, CQLFunctions currentObj,
			List<CQLFunctions> functionsList);

	/**
	 * Delete parameter
	 * 
	 * @param measureId the measure id
	 * @param toBeDeletedObj the to be deleted obj
	 * @param currentObj the current obj
	 * @param parameterList the parameter list
	 * @return the save and update cql result
	 */
	SaveUpdateCQLResult deleteParameter(String measureId, CQLParameter toBeDeletedObj, CQLParameter currentObj,
			List<CQLParameter> parameterList);
	
	/**
	 * Gets the CQL data type list.
	 *
	 * @return the CQL data type list
	 */
	CQLKeywords getCQLKeywordsLists();

	String getJSONObjectFromXML();

	/**
	 * Creates the and save cql look up.
	 *
	 * @param list            the list
	 * @param measureID            the measure id
	 * @param expProfileToAllQDM the exp profile to all qdm
	 */
	void createAndSaveCQLLookUp(List<QualityDataSetDTO> list, String measureID, String expProfileToAllQDM);

	GetUsedCQLArtifactsResult getUsedCqlArtifacts(String measureId);
	
	SaveUpdateCQLResult createAndSaveCQLElementLookUp(String Uuid, List<CQLQualityDataSetDTO> list, String measureID, String expProfileToAllQDM);

	CQLQualityDataModelWrapper getCQLAppliedQDMFromMeasureXml(String measureId, boolean checkForSupplementData);

	CQLQualityDataModelWrapper getDefaultCQLSDEFromMeasureXml(String measureId);

	SaveUpdateCQLResult parseCQLStringForError(String cqlFileString);

	CQLQualityDataModelWrapper getCQLValusets(String measureID);

	SaveUpdateCQLResult saveCQLValuesettoMeasure(CQLValueSetTransferObject valueSetTransferObject);

	SaveUpdateCQLResult saveCQLUserDefinedValuesettoMeasure(CQLValueSetTransferObject matValueSetTransferObject);

	SaveUpdateCQLResult updateCQLValueSetstoMeasure(CQLValueSetTransferObject matValueSetTransferObject);

	void updateValueSetsInCQLLookUp(CQLQualityDataSetDTO modifyWithDTO, CQLQualityDataSetDTO modifyDTO,
			String measureId);
	SaveUpdateCQLResult saveIncludeLibrayInCQLLookUp(String measureId,
			CQLIncludeLibrary toBeModifiedObj, CQLIncludeLibrary currentObj,
			List<CQLIncludeLibrary> incLibraryList);

}
