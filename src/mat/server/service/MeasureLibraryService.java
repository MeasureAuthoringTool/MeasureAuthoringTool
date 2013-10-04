package mat.server.service;

import java.util.ArrayList;
import java.util.List;

import mat.DTO.MeasureNoteDTO;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureShareModel;
import mat.client.measure.MeasureNotesModel;
import mat.client.measure.TransferMeasureOwnerShipModel;
import mat.client.measure.service.SaveMeasureResult;
import mat.client.measure.service.ValidateMeasureResult;
import mat.client.shared.MatException;
import mat.model.MatValueSet;
import mat.model.QualityDataSetDTO;
/** MeasureLibraryService.java.**/
public interface MeasureLibraryService  {
	/**
	 *@param key - {@link String}.
	 *@return {@link ManageMeasureDetailModel}.
	 * **/
	ManageMeasureDetailModel getMeasure(String key);
	/**
	 *@param model - {@link ManageMeasureDetailModel}.
	 *@return {@link SaveMeasureResult}.
	 * **/
	SaveMeasureResult save(ManageMeasureDetailModel model);
	/**
	 *@param measureId - {@link String}.
	 *@param isMajor - {@link Boolean}.
	 *@param version - {@link String}.
	 *@return {@link SaveMeasureResult}.
	 * **/
	SaveMeasureResult saveFinalizedVersion(String measureId, boolean isMajor, String version);
	/**
	 *@param model - {@link ManageMeasureDetailModel}.
	 *@return {@link SaveMeasureResult}.
	 * **/
	SaveMeasureResult saveMeasureDetails(ManageMeasureDetailModel model);
	/**
	 *@param searchText - {@link String}.
	 *@param startIndex - {@link Integer}.
	 *@param pageSize - {@link Integer}.
	 *@param filter - {@link Integer}.
	 *@return {@link ManageMeasureSearchModel}.
	 *
	 * **/
	ManageMeasureSearchModel search(String searchText, int startIndex,
			int pageSize, int filter);
	/**
	 *@param measureId - {@link String}
	 *@param startIndex - {@link Integer}
	 *@param pageSize - {@link Integer}.
	 *@return {@link ManageMeasureShareModel}.
	 * **/
	ManageMeasureShareModel getUsersForShare(String measureId, int startIndex, int pageSize);
	/**
	 *@param model - {@link ManageMeasureShareModel}.
	 *
	 * **/
	void updateUsersShare(ManageMeasureShareModel model);
	/**
	 *@param measureId - {@link String}.
	 *@param userId - {@link String}.
	 *@return {@link SaveMeasureResult}.
	 * **/
	SaveMeasureResult updateLockedDate(String measureId, String userId);
	/**
	 *@param measureId - {@link String}.
	 *@param userId - {@link String}/
	 *@return {@link SaveMeasureResult}.
	 * **/
	SaveMeasureResult resetLockedDate(String measureId, String userId);
	/**
	 *@param startIndex - {@link Integer}.
	 *@param pageSize - {@link Integer}.
	 *@return {@link ManageMeasureSearchModel}.
	 * **/
	ManageMeasureSearchModel searchMeasuresForVersion(int startIndex,
			int pageSize);
	/**
	 *@param startIndex - {@link Integer}.
	 *@param pageSize - {@link Integer}.
	 *@return {@link ManageMeasureSearchModel}.
	 *
	 * **/
	ManageMeasureSearchModel searchMeasuresForDraft(int startIndex, int pageSize);
	/**
	 *@param id - {@link String}
	 *@return {@link Boolean}
	 * **/
	boolean isMeasureLocked(String id);
	/**
	 *@return {@link Integer}.
	 * **/
	int getMaxEMeasureId();
	/**
	 *@param measureId - {@link ManageMeasureDetailModel}.
	 *@return {@link Integer}.
	 * **/
	int generateAndSaveMaxEmeasureId(ManageMeasureDetailModel measureId);
	/**
	 *@param startIndex - {@link Integer}.
	 *@param pageSize - {@link Integer}.
	 *@return {@link TransferMeasureOwnerShipModel}.
	 * **/
	TransferMeasureOwnerShipModel searchUsers(int startIndex, int pageSize);
	/**
	 *@param list - {@link List} of {@link String}.
	 *@param toEmail - {@link String}.
	 * **/
	void transferOwnerShipToUser(List<String> list, String toEmail);
	/**
	 *@param measureId - {@link String}.
	 *@return {@link MeasureXmlModel}.
	 * **/
	MeasureXmlModel getMeasureXmlForMeasure(String measureId);
	/**
	 *@param measureXmlModel - {@link MeasureXmlModel}.
	 * **/
	void saveMeasureXml(MeasureXmlModel measureXmlModel);
	/**
	 *@param creatingDraft - {@link Boolean}.
	 *@param oldMeasureId - {@link String}.
	 *@param clonedMeasureId - {@link String}.
	 * **/
	void cloneMeasureXml(boolean creatingDraft, String oldMeasureId, String clonedMeasureId);
	/**
	 *@param measureXmlModel - {@link MeasureXmlModel}.
	 *@param nodeName - {@link String}.
	 * **/
	void appendAndSaveNode(MeasureXmlModel measureXmlModel, String nodeName);
	/**
	 *@param list - {@link ArrayList} of {@link QualityDataSetDTO}.
	 *@param measureID - {@link String}.
	 * **/
	void createAndSaveElementLookUp(ArrayList<QualityDataSetDTO> list,
			String measureID);
	/**
	 *@param measureID - {@link String}.
	 * **/
	void saveAndDeleteMeasure(String measureID);
	/**
	 *@param measureId - {@link String}.
	 *@param isPrivate - {@link Boolean}.
	 * **/
	void updatePrivateColumnInMeasure(String measureId, boolean isPrivate);
	/**
	 *@param modifyWithDTO - {@link QualityDataSetDTO}.
	 *@param modifyDTO - {@link QualityDataSetDTO}.
	 *@param measureId - {@link String}.
	 * **/
	void updateMeasureXML(QualityDataSetDTO modifyWithDTO,
			QualityDataSetDTO modifyDTO, String measureId);
	/**
	 *@param noteTitle - {@link String}.
	 *@param noteDescription - {@link String}.
	 *@param measureId - {@link String}.
	 *@param userId - {@link String}.
	 *
	 * **/
	void saveMeasureNote(String noteTitle, String noteDescription,
			String measureId, String userId);
	/**
	 *@param measureID - {@link String}.
	 *@return {@link MeasureNotesModel}.
	 * **/
	MeasureNotesModel getAllMeasureNotesByMeasureID(String measureID);
	/**
	 *@param measureNoteDTO - {@link MeasureNoteDTO}.
	 * **/
	void deleteMeasureNotes(MeasureNoteDTO measureNoteDTO);
	/**
	 * @param measureNoteDTO - {@link MeasureNoteDTO}.
	 * @param userId - {@link String}.
	 * **/
	void updateMeasureNotes(MeasureNoteDTO measureNoteDTO, String userId);
	/**
	 *@param measureId - {@link String}.
	 *@param checkForSupplementData - {@link Boolean}.
	 *@return {@link ArrayList} of {@link QualityDataSetDTO}.
	 * **/
	ArrayList<QualityDataSetDTO> getAppliedQDMFromMeasureXml(String measureId,
			boolean checkForSupplementData);
	/**
	 *@param key - {@link String}.
	 *@param matValueSetList - {@link ArrayList} of {@link MatValueSet}.
	 *@return {@link ValidateMeasureResult}.
	 *@throws MatException - {@link MatException}
	 * **/
	ValidateMeasureResult validateMeasureForExport(String key,
			ArrayList<MatValueSet> matValueSetList) throws MatException;
}

