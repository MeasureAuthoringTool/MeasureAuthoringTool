package mat.client.measure.service;

import java.util.ArrayList;
import java.util.List;
import mat.DTO.MeasureNoteDTO;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureShareModel;
import mat.client.measure.MeasureNotesModel;
import mat.client.measure.TransferMeasureOwnerShipModel;
import mat.model.MatValueSet;
import mat.model.QualityDataSetDTO;
import mat.model.RecentMSRActivityLog;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The Interface MeasureServiceAsync.
 */
public interface MeasureServiceAsync {
	
	/**
	 * Append and save node.
	 * 
	 * @param measureXmlModel
	 *            the measure xml model
	 * @param nodeName
	 *            the node name
	 * @param callback
	 *            the callback
	 */
	void appendAndSaveNode(MeasureXmlModel measureXmlModel, String nodeName,
			AsyncCallback<Void> callback);
	
	/**
	 * Clone measure xml.
	 * 
	 * @param creatingDraft
	 *            the creating draft
	 * @param oldMeasureId
	 *            the old measure id
	 * @param clonedMeasureId
	 *            the cloned measure id
	 * @param callback
	 *            the callback
	 */
	void cloneMeasureXml(boolean creatingDraft, String oldMeasureId,
			String clonedMeasureId, AsyncCallback<Void> callback);
	
	/**
	 * Creates the and save element look up.
	 * 
	 * @param list
	 *            the list
	 * @param measureID
	 *            the measure id
	 * @param callback
	 *            the callback
	 */
	void createAndSaveElementLookUp(ArrayList<QualityDataSetDTO> list,
			String measureID, AsyncCallback<Void> callback);
	
	/**
	 * Delete measure notes.
	 * 
	 * @param measureNoteDTO
	 *            the measure note dto
	 * @param callback
	 *            the callback
	 */
	void deleteMeasureNotes(MeasureNoteDTO measureNoteDTO, AsyncCallback<Void> callback);
	
	/**
	 * Generate and save max emeasure id.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param callback
	 *            the callback
	 */
	void generateAndSaveMaxEmeasureId(ManageMeasureDetailModel measureId, AsyncCallback<Integer> callback);
	
	/**
	 * Gets the all measure notes by measure id.
	 * 
	 * @param measureID
	 *            the measure id
	 * @param callback
	 *            the callback
	 * @return the all measure notes by measure id
	 */
	void getAllMeasureNotesByMeasureID(String measureID,
			AsyncCallback<MeasureNotesModel> callback);
	
	/** Gets the all recent measure for user.
	 * 
	 * @param userId the user id
	 * @param callback the callback
	 * @return the all recent measure for user */
	void getAllRecentMeasureForUser(String userId, AsyncCallback<ManageMeasureSearchModel> callback);
	
	/**
	 * Gets the applied qdm from measure xml.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param checkForSupplementData
	 *            the check for supplement data
	 * @param callback
	 *            the callback
	 * @return the applied qdm from measure xml
	 */
	void getAppliedQDMFromMeasureXml(String measureId,
			boolean checkForSupplementData,
			AsyncCallback<ArrayList<QualityDataSetDTO>> callback);
	
	/**
	 * Gets the max e measure id.
	 * 
	 * @param callback
	 *            the callback
	 * @return the max e measure id
	 */
	void getMaxEMeasureId(AsyncCallback<Integer> callback);
	
	/**
	 * Gets the measure.
	 * 
	 * @param key
	 *            the key
	 * @param callback
	 *            the callback
	 * @return the measure
	 */
	void getMeasure(String key, AsyncCallback<ManageMeasureDetailModel> callback);
	
	/**
	 * Gets the measure and logs in this measure as recently used measure in recent measure activity log.
	 *
	 * @param measureId the measure id
	 * @param userId the user id
	 * @param callback the callback
	 * @return the measure and log recent measure
	 */
	void getMeasureAndLogRecentMeasure(String measureId, String userId, AsyncCallback<ManageMeasureDetailModel> callback);
	
	/**
	 * Gets the measure xml for measure.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param callback
	 *            the callback
	 * @return the measure xml for measure
	 */
	void getMeasureXmlForMeasure(String measureId,
			AsyncCallback<MeasureXmlModel> callback);
	
	/**
	 * Gets recently used measures by the given user ID in the descending order of time from recent measure activity log.
	 *
	 * @param userId the user id
	 * @param callback the callback
	 * @return the recent measure activity log
	 */
	void getRecentMeasureActivityLog(String userId, AsyncCallback<List<RecentMSRActivityLog>> callback);
	
	/**
	 * Gets the users for share.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @param callback
	 *            the callback
	 * @return the users for share
	 */
	void getUsersForShare(String measureId, int startIndex, int pageSize, AsyncCallback<ManageMeasureShareModel> callback);
	
	/**
	 * Checks if is measure locked.
	 * 
	 * @param id
	 *            the id
	 * @param isLocked
	 *            the is locked
	 */
	void isMeasureLocked(String id, AsyncCallback<Boolean> isLocked);
	
	/**
	 * Reset locked date.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param userId
	 *            the user id
	 * @param callback
	 *            the callback
	 */
	void resetLockedDate(String measureId,String userId, AsyncCallback<SaveMeasureResult> callback);
	
	/**
	 * Save.
	 * 
	 * @param model
	 *            the model
	 * @param callback
	 *            the callback
	 */
	void save(ManageMeasureDetailModel model, AsyncCallback<SaveMeasureResult> callback);
	
	/**
	 * Save and delete measure.
	 * 
	 * @param measureID
	 *            the measure id
	 * @param callback
	 *            the callback
	 */
	void saveAndDeleteMeasure(String measureID, AsyncCallback<Void> callback);
	
	/**
	 * Save finalized version.
	 * 
	 * @param measureid
	 *            the measureid
	 * @param isMajor
	 *            the is major
	 * @param version
	 *            the version
	 * @param callback
	 *            the callback
	 */
	void saveFinalizedVersion(String measureid,boolean isMajor,String version, AsyncCallback<SaveMeasureResult> callback);
	
	/**
	 * Save measure details.
	 * 
	 * @param model
	 *            the model
	 * @param callback
	 *            the callback
	 */
	void saveMeasureDetails(ManageMeasureDetailModel model,AsyncCallback<SaveMeasureResult> callback);
	
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
	 * @param callback
	 *            the callback
	 */
	void saveMeasureNote(String noteTitle, String noteDescription,
			String string, String string2, AsyncCallback<Void> callback);
	
	/**
	 * Save measure xml.
	 * 
	 * @param measureXmlModel
	 *            the measure xml model
	 * @param callback
	 *            the callback
	 */
	void saveMeasureXml(MeasureXmlModel measureXmlModel,
			AsyncCallback<Void> callback);
	
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
	 * @param callback
	 *            the callback
	 */
	void search(String searchText, int startIndex, int pageSize,int filter, AsyncCallback<ManageMeasureSearchModel> callback);
	
	/**
	 * Search measures for draft.
	 *
	 * @param searchText the search text
	 * @param callback the callback
	 */
	void searchMeasuresForDraft(String searchText, AsyncCallback<ManageMeasureSearchModel> callback);
	
	/**
	 * Search measures for version.
	 *
	 * @param searchText the search text
	 * @param callback the callback
	 */
	void searchMeasuresForVersion(String searchText, AsyncCallback<ManageMeasureSearchModel> callback);
	
	/**
	 * Search users.
	 * 
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @param callback
	 *            the callback
	 */
	void searchUsers(int startIndex, int pageSize,
			AsyncCallback<TransferMeasureOwnerShipModel> callback);
	
	/**
	 * Transfer owner ship to user.
	 * 
	 * @param list
	 *            the list
	 * @param toEmail
	 *            the to email
	 * @param callback
	 *            the callback
	 */
	void transferOwnerShipToUser(List<String> list, String toEmail,
			AsyncCallback<Void> callback);
	
	/**
	 * Update locked date.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param userId
	 *            the user id
	 * @param callback
	 *            the callback
	 */
	void updateLockedDate(String measureId,String userId, AsyncCallback<SaveMeasureResult> callback);
	
	/**
	 * Update measure notes.
	 * 
	 * @param measureNoteDTO
	 *            the measure note dto
	 * @param userId
	 *            the user id
	 * @param callback
	 *            the callback
	 */
	void updateMeasureNotes(MeasureNoteDTO measureNoteDTO, String userId, AsyncCallback<Void> callback);
	
	/**
	 * Update measure xml.
	 * 
	 * @param modifyWithDTO
	 *            the modify with dto
	 * @param modifyDTO
	 *            the modify dto
	 * @param measureId
	 *            the measure id
	 * @param callback
	 *            the callback
	 */
	void updateMeasureXML(QualityDataSetDTO modifyWithDTO,
			QualityDataSetDTO modifyDTO, String measureId,
			AsyncCallback<Void> callback);
	
	/**
	 * Update private column in measure.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param isPrivate
	 *            the is private
	 * @param callback
	 *            the callback
	 */
	void updatePrivateColumnInMeasure(String measureId, boolean isPrivate,
			AsyncCallback<Void> callback);
	
	/**
	 * Update users share.
	 * 
	 * @param model
	 *            the model
	 * @param callback
	 *            the callback
	 */
	void updateUsersShare(ManageMeasureShareModel model, AsyncCallback<Void> callback);
	/**
	 * Validate measure for export.
	 * 
	 * @param key
	 *            the key
	 * @param matValueSetList
	 *            the mat value set list
	 * @param callback
	 *            the callback
	 */
	void validateMeasureForExport(String key, ArrayList<MatValueSet> matValueSetList, AsyncCallback<ValidateMeasureResult> callback);
}
