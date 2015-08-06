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
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureShareModel;
import mat.client.measure.MeasureNotesModel;
import mat.client.measure.TransferMeasureOwnerShipModel;
import mat.client.measure.service.SaveMeasureNotesResult;
import mat.client.measure.service.SaveMeasureResult;
import mat.client.measure.service.ValidateMeasureResult;
import mat.client.shared.MatException;
import mat.model.MatValueSet;
import mat.model.MeasureOwnerReportDTO;
import mat.model.MeasureType;
import mat.model.Organization;
import mat.model.QualityDataModelWrapper;
import mat.model.QualityDataSetDTO;
import mat.model.RecentMSRActivityLog;
import mat.server.util.XmlProcessor;

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
	 * @param list
	 *            the list
	 * @param measureID
	 *            the measure id
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
	 */
	void saveAndDeleteMeasure(String measureID);
	
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
	 * @param noteTitle
	 *            the note title
	 * @param noteDescription
	 *            the note description
	 * @param string
	 *            the string
	 * @param string2
	 *            the string2
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
	 * Gets the applied qdm for item count.
	 *
	 * @param measureId the measure id
	 * @param checkForSupplementData the check for supplement data
	 * @return the applied qdm for item count
	 */
	List<QualityDataSetDTO> getAppliedQDMForItemCount(String measureId,
			boolean checkForSupplementData);
	
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
	 * @param measureId
	 * @return
	 */
	MeasureDetailResult getUsedStewardAndDevelopersList(String measureId);
	
	/**
	 * Service to Update Expansion Profile in Measure Xml.
	 * @param modifyWithDTO
	 * @param measureId
	 * @param expansionProfile
	 */
	void updateMeasureXMLForExpansionIdentifier(
			List<QualityDataSetDTO> modifyWithDTO, String measureId,
			String expansionProfile);
	
	
	/**
	 * Method to Get Default 4 Supplemental Data Elements for give Measure.
	 * @param measureId
	 * @return QualityDataModelWrapper
	 */
	QualityDataModelWrapper getDefaultSDEFromMeasureXml(String measureId);
	
	List<MeasureOwnerReportDTO> getMeasuresForMeasureOwner() throws XPathExpressionException;
	
	String getDefaultExpansionIdentifier(String measureId);
	
	String getCurrentReleaseVersion();
	void setCurrentReleaseVersion(String releaseVersion);
}
