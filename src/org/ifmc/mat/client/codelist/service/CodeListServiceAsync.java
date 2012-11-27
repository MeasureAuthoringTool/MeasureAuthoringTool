package org.ifmc.mat.client.codelist.service;

import java.util.List;

import org.ifmc.mat.client.codelist.HasListBox;
import org.ifmc.mat.client.codelist.ManageCodeListDetailModel;
import org.ifmc.mat.client.codelist.ManageCodeListSearchModel;
import org.ifmc.mat.client.codelist.ManageValueSetSearchModel;
import org.ifmc.mat.model.Code;
import org.ifmc.mat.model.CodeListSearchDTO;
import org.ifmc.mat.model.QualityDataSetDTO;

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

	void addCodeListToMeasure(String measureId, String dataType,CodeListSearchDTO codeList,boolean isSpecificOccurrence,
			AsyncCallback<SaveUpdateCodeListResult> callback);

	public void getQDSElements(String measureId, String vertsion, AsyncCallback<List<QualityDataSetDTO>> callback);
	
	public void generateUniqueOid(ManageCodeListDetailModel currentDetails, AsyncCallback<String> callBack);

	public void searchValueSetsForDraft(int startIndex, int pageSize,
			AsyncCallback<ManageValueSetSearchModel> asyncCallback);

	public void createDraft(String id, String oid, AsyncCallback<ManageValueSetSearchModel> asyncCallback);
	//US193
	public void createClone(String id, AsyncCallback<ManageValueSetSearchModel> asyncCallback);
}
