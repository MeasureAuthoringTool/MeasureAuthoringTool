package mat.client.codelist.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mat.client.codelist.AdminManageCodeListSearchModel;
import mat.client.codelist.HasListBox;
import mat.client.codelist.ManageCodeListDetailModel;
import mat.client.codelist.ManageCodeListSearchModel;
import mat.client.codelist.ManageValueSetSearchModel;
import mat.client.codelist.TransferOwnerShipModel;
import mat.model.Code;
import mat.model.CodeListSearchDTO;
import mat.model.QualityDataSetDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface CodeListServiceAsync {
	public void search(String searchText,
			int startIndex, int pageSize, String sortColumn, boolean isAsc, boolean defaultCodeList, int filter,
			AsyncCallback<ManageCodeListSearchModel> callback);
	
	public void search(String searchText,
			int startIndex, int pageSize, String sortColumn, boolean isAsc, boolean defaultCodeList, int filter,
			String categoryId,
			AsyncCallback<ManageCodeListSearchModel> callback);
	
	public void getCodeList(String key, 
			AsyncCallback<ManageCodeListDetailModel> callback);
	public void getGroupedCodeList(String key, 
			AsyncCallback<ManageCodeListDetailModel> callback);
	
	public void getGroupedCodeList(String key, int startIndex, int pageSize,
			AsyncCallback<ManageCodeListDetailModel> callback);
	public void getCodes(String codeListId,int startIndex,int pageSize,AsyncCallback<List<Code>> callback);
	
	public void isCodeAlreadyExists(String codeListId, Code code,AsyncCallback<Boolean> callback);
	void getListBoxData(AsyncCallback<CodeListService.ListBoxData> callback);

	void saveorUpdateCodeList(ManageCodeListDetailModel currentDetails,
			AsyncCallback<SaveUpdateCodeListResult> callback);
	
	void saveorUpdateGroupedCodeList(ManageCodeListDetailModel currentDetails,
			AsyncCallback<SaveUpdateCodeListResult> callback);
	
	public void getCodeSystemsForCategory(String category, AsyncCallback<List<? extends HasListBox>> callback);
	public void getCodeListsForCategory(String category, AsyncCallback<List<? extends HasListBox>> callback);
	public void getQDSDataTypeForCategory(String category, AsyncCallback<List<? extends HasListBox>> callback);

	
	void deleteCodes(String codeListID, List<Code> Codes,
			AsyncCallback<ManageCodeListDetailModel> callback);

	void addCodeListToMeasure(String measureId, String dataType,CodeListSearchDTO codeList,boolean isSpecificOccurrence,ArrayList<QualityDataSetDTO> appliedQDMs,
			AsyncCallback<SaveUpdateCodeListResult> callback);

	public void getQDSElements(String measureId, String vertsion, AsyncCallback<List<QualityDataSetDTO>> callback);
	
	public void generateUniqueOid(ManageCodeListDetailModel currentDetails, AsyncCallback<String> callBack);

	public void searchValueSetsForDraft(int startIndex, int pageSize,
			AsyncCallback<ManageValueSetSearchModel> asyncCallback);

	public void createDraft(String id, String oid, AsyncCallback<ManageValueSetSearchModel> asyncCallback);
	//US193
	public void createClone(String id, AsyncCallback<ManageValueSetSearchModel> asyncCallback);

	void searchForAdmin(String searchText, int startIndex, int pageSize,
			String sortColumn, boolean isAsc, boolean defaultCodeList,
			int filter, AsyncCallback<AdminManageCodeListSearchModel> callback);

		void transferOwnerShipToUser(List<String> list, String toEmail,
			AsyncCallback<Void> callback);

	void searchUsers(int startIndex, int pageSize,
			AsyncCallback<TransferOwnerShipModel> callback);

	void getTimingOperators(AsyncCallback<Map<String, String>> callback);
	
	
}
