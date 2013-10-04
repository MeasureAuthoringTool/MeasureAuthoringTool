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

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MeasureServiceAsync {
/**
 * */
void getMeasure(String key, AsyncCallback<ManageMeasureDetailModel> callback);
/**
 * */
void save(ManageMeasureDetailModel model, AsyncCallback<SaveMeasureResult> callback);
/**
 * */
void search(String searchText, int startIndex, int pageSize,int filter, AsyncCallback<ManageMeasureSearchModel> callback);
/**
 * */
void searchMeasuresForVersion(int startIndex, int pageSize, AsyncCallback<ManageMeasureSearchModel> callback);
/**
 * */
void searchMeasuresForDraft(int startIndex, int pageSize, AsyncCallback<ManageMeasureSearchModel> callback);
/**
 * */
void getUsersForShare(String measureId, int startIndex, int pageSize, AsyncCallback<ManageMeasureShareModel> callback);
/**
 * */
void updateUsersShare(ManageMeasureShareModel model, AsyncCallback<Void> callback);
/**
 * */
void saveMeasureDetails(ManageMeasureDetailModel model,AsyncCallback<SaveMeasureResult> callback);
/**
 * */
void validateMeasureForExport(String key, ArrayList<MatValueSet> matValueSetList, AsyncCallback<ValidateMeasureResult> callback);
/**
 * */
void updateLockedDate(String measureId,String userId, AsyncCallback<SaveMeasureResult> callback);
/**
 * */
void resetLockedDate(String measureId,String userId, AsyncCallback<SaveMeasureResult> callback);
/**
 * */
void saveFinalizedVersion(String measureid,boolean isMajor,String version, AsyncCallback<SaveMeasureResult> callback);
/**
 * */
void isMeasureLocked(String id, AsyncCallback<Boolean> isLocked);

/**
 * */
void getMaxEMeasureId(AsyncCallback<Integer> callback);
/**
 * */
void generateAndSaveMaxEmeasureId(ManageMeasureDetailModel measureId, AsyncCallback<Integer> callback);
/**
 * */
void searchUsers(int startIndex, int pageSize,
			AsyncCallback<TransferMeasureOwnerShipModel> callback);
/**
 * */
void transferOwnerShipToUser(List<String> list, String toEmail,
			AsyncCallback<Void> callback);
/**
 * */
void getMeasureXmlForMeasure(String measureId,
			AsyncCallback<MeasureXmlModel> callback);
/**
 * */
void saveMeasureXml(MeasureXmlModel measureXmlModel,
			AsyncCallback<Void> callback);
/**
 * */
void cloneMeasureXml(boolean creatingDraft, String oldMeasureId,
			String clonedMeasureId, AsyncCallback<Void> callback);
/**
 * */
void appendAndSaveNode(MeasureXmlModel measureXmlModel, String nodeName,
			AsyncCallback<Void> callback);
/**
 * */
void createAndSaveElementLookUp(ArrayList<QualityDataSetDTO> list,
			String measureID, AsyncCallback<Void> callback);
/**
 * */
void saveAndDeleteMeasure(String measureID, AsyncCallback<Void> callback);
/**
 * */
void updatePrivateColumnInMeasure(String measureId, boolean isPrivate,
			AsyncCallback<Void> callback);
/**
 * */
void updateMeasureXML(QualityDataSetDTO modifyWithDTO,
			QualityDataSetDTO modifyDTO, String measureId,
			AsyncCallback<Void> callback);
/**
 * */
void saveMeasureNote(String noteTitle, String noteDescription,
			String string, String string2, AsyncCallback<Void> callback);
/**
 * */
void getAllMeasureNotesByMeasureID(String measureID,
			AsyncCallback<MeasureNotesModel> callback);
/**
 * */
void deleteMeasureNotes(MeasureNoteDTO measureNoteDTO, AsyncCallback<Void> callback);
/**
 * */
void updateMeasureNotes(MeasureNoteDTO measureNoteDTO, String userId, AsyncCallback<Void> callback);
/**
 * */
void getAppliedQDMFromMeasureXml(String measureId,
			boolean checkForSupplementData,
			AsyncCallback<ArrayList<QualityDataSetDTO>> callback);
}
