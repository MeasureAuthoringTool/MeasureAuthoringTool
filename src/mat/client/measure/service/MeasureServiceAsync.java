package mat.client.measure.service;

import java.util.List;

import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureShareModel;
import mat.client.measure.TransferMeasureOwnerShipModel;
import mat.model.Author;
import mat.model.MeasureType;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MeasureServiceAsync {
	public void getMeasure(String key, AsyncCallback<ManageMeasureDetailModel> callback);
	public void save(ManageMeasureDetailModel model, AsyncCallback<SaveMeasureResult> callback);
	public void search(String searchText, int startIndex, int pageSize,int filter, AsyncCallback<ManageMeasureSearchModel> callback);
	public void searchMeasuresForVersion(int startIndex, int pageSize, AsyncCallback<ManageMeasureSearchModel> callback);
	public void searchMeasuresForDraft(int startIndex, int pageSize, AsyncCallback<ManageMeasureSearchModel> callback);
	public void getUsersForShare(String measureId, int startIndex, int pageSize, AsyncCallback<ManageMeasureShareModel> callback);
	public void updateUsersShare(ManageMeasureShareModel model, AsyncCallback<Void> callback);
	void saveMeasureDetails(ManageMeasureDetailModel model,AsyncCallback<SaveMeasureResult> callback);
	void deleteAuthors(String measureId, List<Author> selectedAuthorsList,AsyncCallback<ManageMeasureDetailModel> callback);
	void deleteMeasureTypes(String measureId,List<MeasureType> selectedMeasureTypeList,AsyncCallback<ManageMeasureDetailModel> callback);
	public void validateMeasureForExport(String key, AsyncCallback<ValidateMeasureResult> callback);
	public void updateLockedDate(String measureId,String userId, AsyncCallback<SaveMeasureResult> callback);
	void resetLockedDate(String measureId,String userId, AsyncCallback<SaveMeasureResult> callback);
	void saveFinalizedVersion(String measureid,boolean isMajor,String version, AsyncCallback<SaveMeasureResult> callback);
	public void isMeasureLocked(String id, AsyncCallback<Boolean> isLocked);
	public void getMaxEMeasureId(AsyncCallback<Integer> callback);
	public void generateAndSaveMaxEmeasureId(ManageMeasureDetailModel measureId, AsyncCallback<Integer> callback);
	void searchUsers(int startIndex, int pageSize,
			AsyncCallback<TransferMeasureOwnerShipModel> callback);
	void transferOwnerShipToUser(List<String> list, String toEmail,
			AsyncCallback<Void> callback);
	void getMeasureXmlForMeasure(String measureId,
			AsyncCallback<MeasureXmlModel> callback);
	void saveMeasureXml(MeasureXmlModel measureXmlModel,
			AsyncCallback<Void> callback);
	}
