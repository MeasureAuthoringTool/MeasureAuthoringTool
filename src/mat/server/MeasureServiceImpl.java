package mat.server;

import java.util.ArrayList;
import java.util.List;

import mat.DTO.MeasureNoteDTO;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureShareModel;
import mat.client.measure.MeasureNotesModel;
import mat.client.measure.TransferMeasureOwnerShipModel;
import mat.client.measure.service.MeasureService;
import mat.client.measure.service.SaveMeasureResult;
import mat.client.measure.service.ValidateMeasureResult;
import mat.client.shared.MatException;
import mat.model.MatValueSet;
import mat.model.QualityDataSetDTO;
import mat.server.service.MeasureLibraryService;

public class MeasureServiceImpl extends SpringRemoteServiceServlet implements
		MeasureService {
	private static final long serialVersionUID = 2280421300224680146L;
	
	private MeasureLibraryService getMeasureLibraryService(){
		return (MeasureLibraryService) this.context.getBean("measureLibraryService");
	}
	@Override
	public ManageMeasureDetailModel getMeasure(String key) {
		return this.getMeasureLibraryService().getMeasure(key);
	}

	@Override
	public SaveMeasureResult save(ManageMeasureDetailModel model) {
		return this.getMeasureLibraryService().save(model);
	}

	@Override
	public SaveMeasureResult saveFinalizedVersion(String measureId,
			boolean isMajor, String version) {
		return this.getMeasureLibraryService().saveFinalizedVersion(measureId, isMajor, version);
	}

	@Override
	public SaveMeasureResult saveMeasureDetails(ManageMeasureDetailModel model) {
		return this.getMeasureLibraryService().saveMeasureDetails(model);
	}

	@Override
	public ManageMeasureSearchModel search(String searchText, int startIndex,
			int pageSize, int filter) {
		return this.getMeasureLibraryService().search(searchText, startIndex, pageSize, filter);
	}

	@Override
	public ManageMeasureShareModel getUsersForShare(String measureId,
			int startIndex, int pageSize) {
		return this.getMeasureLibraryService().getUsersForShare(measureId, startIndex, pageSize);
	}

	@Override
	public void updateUsersShare(ManageMeasureShareModel model) {
		this.getMeasureLibraryService().updateUsersShare(model);		
	}

	@Override
	public ValidateMeasureResult validateMeasureForExport(String key , ArrayList<MatValueSet> matValueSetList)
			throws MatException {
		return this.getMeasureLibraryService().validateMeasureForExport(key, matValueSetList);
	}

	@Override
	public SaveMeasureResult updateLockedDate(String measureId, String userId) {
		return this.getMeasureLibraryService().updateLockedDate(measureId, userId);
	}

	@Override
	public SaveMeasureResult resetLockedDate(String measureId, String userId) {
		return this.getMeasureLibraryService().resetLockedDate(measureId, userId);
	}

	@Override
	public ManageMeasureSearchModel searchMeasuresForVersion(int startIndex,
			int pageSize) {
		return this.getMeasureLibraryService().searchMeasuresForVersion(startIndex, pageSize);
	}

	@Override
	public ManageMeasureSearchModel searchMeasuresForDraft(int startIndex,
			int pageSize) {
		return this.getMeasureLibraryService().searchMeasuresForDraft(startIndex, pageSize);
	}

	@Override
	public boolean isMeasureLocked(String id) {
		return this.getMeasureLibraryService().isMeasureLocked(id);
	}

	@Override
	public int getMaxEMeasureId() {
		return this.getMeasureLibraryService().getMaxEMeasureId();
	}

	@Override
	public int generateAndSaveMaxEmeasureId(ManageMeasureDetailModel measureId) {
		return this.getMeasureLibraryService().generateAndSaveMaxEmeasureId(measureId);
	}

	@Override
	public TransferMeasureOwnerShipModel searchUsers(int startIndex,
			int pageSize) {
		return this.getMeasureLibraryService().searchUsers(startIndex, pageSize);
	}

	@Override
	public void transferOwnerShipToUser(List<String> list, String toEmail) {
		this.getMeasureLibraryService().transferOwnerShipToUser(list, toEmail);

	}

	@Override
	public MeasureXmlModel getMeasureXmlForMeasure(String measureId) {
		return this.getMeasureLibraryService().getMeasureXmlForMeasure(measureId);
	}

	@Override
	public void saveMeasureXml(MeasureXmlModel measureXmlModel) {
		this.getMeasureLibraryService().saveMeasureXml(measureXmlModel);

	}

	@Override
	public void cloneMeasureXml(boolean creatingDraft, String oldMeasureId,
			String clonedMeasureId) {
		this.getMeasureLibraryService().cloneMeasureXml(creatingDraft, oldMeasureId, clonedMeasureId);

	}

	@Override
	public void appendAndSaveNode(MeasureXmlModel measureXmlModel,
			String nodeName) {
		this.getMeasureLibraryService().appendAndSaveNode(measureXmlModel, nodeName);

	}

	@Override
	public void createAndSaveElementLookUp(ArrayList<QualityDataSetDTO> list,
			String measureID) {
		this.getMeasureLibraryService().createAndSaveElementLookUp(list, measureID);
	}

	@Override
	public void saveAndDeleteMeasure(String measureID) {
		this.getMeasureLibraryService().saveAndDeleteMeasure(measureID);
	}

	@Override
	public void updatePrivateColumnInMeasure(String measureId, boolean isPrivate) {
		this.getMeasureLibraryService().updatePrivateColumnInMeasure(measureId, isPrivate);
	}

	@Override
	public void updateMeasureXML(QualityDataSetDTO modifyWithDTO,
			QualityDataSetDTO modifyDTO, String measureId) {
		this.getMeasureLibraryService().updateMeasureXML(modifyWithDTO, modifyDTO, measureId);

	}

	@Override
	public void saveMeasureNote(String noteTitle, String noteDescription,
			String string, String string2) {
		this.getMeasureLibraryService().saveMeasureNote(noteTitle, noteDescription, string, string2);

	}

	@Override
	public MeasureNotesModel getAllMeasureNotesByMeasureID(String measureID) {
		return this.getMeasureLibraryService().getAllMeasureNotesByMeasureID(measureID);
	}

	@Override
	public void deleteMeasureNotes(MeasureNoteDTO measureNoteDTO) {
		this.getMeasureLibraryService().deleteMeasureNotes(measureNoteDTO);
	}

	@Override
	public void updateMeasureNotes(MeasureNoteDTO measureNoteDTO, String userId) {
		this.getMeasureLibraryService().updateMeasureNotes(measureNoteDTO, userId);

	}

	@Override
	public ArrayList<QualityDataSetDTO> getAppliedQDMFromMeasureXml(
			String measureId, boolean checkForSupplementData) {
		return this.getMeasureLibraryService().getAppliedQDMFromMeasureXml(measureId, checkForSupplementData);
	}

}
