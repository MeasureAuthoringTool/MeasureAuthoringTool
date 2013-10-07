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

@RemoteServiceRelativePath("measureLibrary")
public interface MeasureService extends RemoteService {
	ManageMeasureDetailModel getMeasure(String key);
	SaveMeasureResult save(ManageMeasureDetailModel model);
	SaveMeasureResult saveFinalizedVersion(String measureId,boolean isMajor,String version) ;
	SaveMeasureResult saveMeasureDetails(ManageMeasureDetailModel model);
	ManageMeasureSearchModel search(String searchText, int startIndex,
			int pageSize, int filter);
	ManageMeasureShareModel getUsersForShare(String measureId, int startIndex, int pageSize);
	void updateUsersShare(ManageMeasureShareModel model);
	SaveMeasureResult updateLockedDate(String measureId,String userId);
	SaveMeasureResult resetLockedDate(String measureId,String userId);
	ManageMeasureSearchModel searchMeasuresForVersion(int startIndex,
			int pageSize);
	ManageMeasureSearchModel searchMeasuresForDraft(int startIndex, int pageSize);
	boolean isMeasureLocked(String id);
	int getMaxEMeasureId();
	int generateAndSaveMaxEmeasureId(ManageMeasureDetailModel measureId);
	TransferMeasureOwnerShipModel searchUsers(int startIndex, int pageSize);
	void transferOwnerShipToUser(List<String> list, String toEmail);
	MeasureXmlModel getMeasureXmlForMeasure(String measureId);
	void saveMeasureXml(MeasureXmlModel measureXmlModel);
	void cloneMeasureXml(boolean creatingDraft, String oldMeasureId, String clonedMeasureId);
	void appendAndSaveNode(MeasureXmlModel measureXmlModel, String nodeName);
	void createAndSaveElementLookUp(ArrayList<QualityDataSetDTO> list,
			String measureID);
	void saveAndDeleteMeasure(String measureID);
	void updatePrivateColumnInMeasure(String measureId, boolean isPrivate);
	void updateMeasureXML(QualityDataSetDTO modifyWithDTO,
			QualityDataSetDTO modifyDTO, String measureId);
	void saveMeasureNote(String noteTitle, String noteDescription,
			String string, String string2);
	MeasureNotesModel getAllMeasureNotesByMeasureID(String measureID);
	void deleteMeasureNotes(MeasureNoteDTO measureNoteDTO);
	void updateMeasureNotes(MeasureNoteDTO measureNoteDTO, String userId);
	ArrayList<QualityDataSetDTO> getAppliedQDMFromMeasureXml(String measureId,
			boolean checkForSupplementData);
	ValidateMeasureResult validateMeasureForExport(String key,
			ArrayList<MatValueSet> matValueSetList) throws MatException;
}
