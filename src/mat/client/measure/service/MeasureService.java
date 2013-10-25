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
import mat.client.shared.MatException;
import mat.model.MatValueSet;
import mat.model.QualityDataSetDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The Interface MeasureService.
 */
@RemoteServiceRelativePath("measureLibrary")
public interface MeasureService extends RemoteService {
	
	/**
	 * Gets the measure.
	 * 
	 * @param key
	 *            the key
	 * @return the measure
	 */
	ManageMeasureDetailModel getMeasure(String key);
	
	/**
	 * Save.
	 * 
	 * @param model
	 *            the model
	 * @return the save measure result
	 */
	SaveMeasureResult save(ManageMeasureDetailModel model);
	
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
	SaveMeasureResult saveFinalizedVersion(String measureId,boolean isMajor,String version) ;
	
	/**
	 * Save measure details.
	 * 
	 * @param model
	 *            the model
	 * @return the save measure result
	 */
	SaveMeasureResult saveMeasureDetails(ManageMeasureDetailModel model);
	
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
	ManageMeasureShareModel getUsersForShare(String measureId, int startIndex, int pageSize);
	
	/**
	 * Update users share.
	 * 
	 * @param model
	 *            the model
	 */
	void updateUsersShare(ManageMeasureShareModel model);
	
	/**
	 * Update locked date.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param userId
	 *            the user id
	 * @return the save measure result
	 */
	SaveMeasureResult updateLockedDate(String measureId,String userId);
	
	/**
	 * Reset locked date.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param userId
	 *            the user id
	 * @return the save measure result
	 */
	SaveMeasureResult resetLockedDate(String measureId,String userId);
	
	/**
	 * Search measures for version.
	 * 
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @return the manage measure search model
	 */
	ManageMeasureSearchModel searchMeasuresForVersion(int startIndex,
			int pageSize);
	
	/**
	 * Search measures for draft.
	 * 
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @return the manage measure search model
	 */
	ManageMeasureSearchModel searchMeasuresForDraft(int startIndex, int pageSize);
	
	/**
	 * Checks if is measure locked.
	 * 
	 * @param id
	 *            the id
	 * @return true, if is measure locked
	 */
	boolean isMeasureLocked(String id);
	
	/**
	 * Gets the max e measure id.
	 * 
	 * @return the max e measure id
	 */
	int getMaxEMeasureId();
	
	/**
	 * Generate and save max emeasure id.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the int
	 */
	int generateAndSaveMaxEmeasureId(ManageMeasureDetailModel measureId);
	
	/**
	 * Search users.
	 * 
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @return the transfer measure owner ship model
	 */
	TransferMeasureOwnerShipModel searchUsers(int startIndex, int pageSize);
	
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
	 * Gets the measure xml for measure.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the measure xml for measure
	 */
	MeasureXmlModel getMeasureXmlForMeasure(String measureId);
	
	/**
	 * Save measure xml.
	 * 
	 * @param measureXmlModel
	 *            the measure xml model
	 */
	void saveMeasureXml(MeasureXmlModel measureXmlModel);
	
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
	void cloneMeasureXml(boolean creatingDraft, String oldMeasureId, String clonedMeasureId);
	
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
	 * Creates the and save element look up.
	 * 
	 * @param list
	 *            the list
	 * @param measureID
	 *            the measure id
	 */
	void createAndSaveElementLookUp(ArrayList<QualityDataSetDTO> list,
			String measureID);
	
	/**
	 * Save and delete measure.
	 * 
	 * @param measureID
	 *            the measure id
	 */
	void saveAndDeleteMeasure(String measureID);
	
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
	void saveMeasureNote(String noteTitle, String noteDescription,
			String string, String string2);
	
	/**
	 * Gets the all measure notes by measure id.
	 * 
	 * @param measureID
	 *            the measure id
	 * @return the all measure notes by measure id
	 */
	MeasureNotesModel getAllMeasureNotesByMeasureID(String measureID);
	
	/**
	 * Delete measure notes.
	 * 
	 * @param measureNoteDTO
	 *            the measure note dto
	 */
	void deleteMeasureNotes(MeasureNoteDTO measureNoteDTO);
	
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
	 * Gets the applied qdm from measure xml.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param checkForSupplementData
	 *            the check for supplement data
	 * @return the applied qdm from measure xml
	 */
	ArrayList<QualityDataSetDTO> getAppliedQDMFromMeasureXml(String measureId,
			boolean checkForSupplementData);
	
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
			ArrayList<MatValueSet> matValueSetList) throws MatException;
}
