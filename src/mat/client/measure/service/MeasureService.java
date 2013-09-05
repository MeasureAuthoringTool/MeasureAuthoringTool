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
import mat.model.QualityDataSetDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("measureLibrary")
public interface MeasureService extends RemoteService {
	public ManageMeasureDetailModel getMeasure(String key);
	public SaveMeasureResult save(ManageMeasureDetailModel model);
	public SaveMeasureResult saveFinalizedVersion(String measureId,boolean isMajor,String version) ;
	public SaveMeasureResult saveMeasureDetails(ManageMeasureDetailModel model);
	ManageMeasureSearchModel search(String searchText, int startIndex,
			int pageSize, int filter);
	public ManageMeasureShareModel getUsersForShare(String measureId, int startIndex, int pageSize);
	public void updateUsersShare(ManageMeasureShareModel model);
	public ValidateMeasureResult validateMeasureForExport(String key) throws MatException;
	public SaveMeasureResult updateLockedDate(String measureId,String userId);
	SaveMeasureResult resetLockedDate(String measureId,String userId);
	ManageMeasureSearchModel searchMeasuresForVersion(int startIndex,
			int pageSize);
	ManageMeasureSearchModel searchMeasuresForDraft(int startIndex, int pageSize);
	public boolean isMeasureLocked(String id);
	public int getMaxEMeasureId();
	public int generateAndSaveMaxEmeasureId(ManageMeasureDetailModel measureId);
	public TransferMeasureOwnerShipModel searchUsers(int startIndex, int pageSize);
	void transferOwnerShipToUser(List<String> list, String toEmail);
	public MeasureXmlModel getMeasureXmlForMeasure(String measureId);
	public void saveMeasureXml(MeasureXmlModel measureXmlModel);
	public void cloneMeasureXml(boolean creatingDraft, String oldMeasureId, String clonedMeasureId);
	void appendAndSaveNode(MeasureXmlModel measureXmlModel, String nodeName);
	ArrayList<QualityDataSetDTO> getMeasureXMLForAppliedQDM(String measureId,
			boolean checkForSupplementData);
	void createAndSaveElementLookUp(ArrayList<QualityDataSetDTO> list,
			String measureID);
	void saveAndDeleteMeasure(String measureID);
	public void updatePrivateColumnInMeasure(String measureId, boolean isPrivate);
	void updateMeasureXML(QualityDataSetDTO modifyWithDTO,
			QualityDataSetDTO modifyDTO, String measureId);
	public void saveMeasureNote(String noteTitle, String noteDescription,
			String string, String string2);
	public MeasureNotesModel getAllMeasureNotesByMeasureID(String measureID);
	public void deleteMeasureNotes(MeasureNoteDTO measureNoteDTO);
	public void updateMeasureNotes(MeasureNoteDTO measureNoteDTO, String userId);
}
