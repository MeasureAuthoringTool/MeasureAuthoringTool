package mat.client.codelist.service;

import java.util.List;
import java.util.Map;
import mat.DTO.OperatorDTO;
import mat.client.codelist.AdminManageCodeListSearchModel;
import mat.client.codelist.HasListBox;
import mat.client.codelist.ManageCodeListDetailModel;
import mat.client.codelist.ManageCodeListSearchModel;
import mat.client.codelist.ManageValueSetSearchModel;
import mat.client.codelist.TransferOwnerShipModel;
import mat.model.Code;
import mat.model.GlobalCopyPasteObject;
import mat.model.MatValueSetTransferObject;
import mat.model.QualityDataSetDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * The Interface CodeListServiceAsync.
 */
public interface CodeListServiceAsync {
	
	//US193
	/**
	 * Creates the clone.
	 * 
	 * @param id
	 *            the id
	 * @param asyncCallback
	 *            the async callback
	 */
	public void createClone(String id, AsyncCallback<ManageValueSetSearchModel> asyncCallback);
	
	/**
	 * Creates the draft.
	 * 
	 * @param id
	 *            the id
	 * @param oid
	 *            the oid
	 * @param asyncCallback
	 *            the async callback
	 */
	public void createDraft(String id, String oid, AsyncCallback<ManageValueSetSearchModel> asyncCallback);
	
	/**
	 * Delete codes.
	 * 
	 * @param codeListID
	 *            the code list id
	 * @param Codes
	 *            the codes
	 * @param callback
	 *            the callback
	 */
	void deleteCodes(String codeListID, List<Code> Codes,
			AsyncCallback<ManageCodeListDetailModel> callback);
	
	/**
	 * Generate unique oid.
	 * 
	 * @param currentDetails
	 *            the current details
	 * @param callBack
	 *            the call back
	 */
	public void generateUniqueOid(ManageCodeListDetailModel currentDetails, AsyncCallback<String> callBack);
	
	/**
	 * Gets the all data types.
	 * 
	 * @param asyncCallback
	 *            the async callback
	 * @return the all data types
	 */
	public void getAllDataTypes(
			AsyncCallback<List<? extends HasListBox>> asyncCallback);
	
	/**
	 * Gets the all operators.
	 * 
	 * @param callback
	 *            the callback
	 * @return the all operators
	 */
	void getAllOperators(AsyncCallback<List<OperatorDTO>> callback);
	
	/**
	 * Gets the all units.
	 * 
	 * @param callback
	 *            the callback
	 * @return the all units
	 */
	void getAllUnits(AsyncCallback<List<String>> callback);
	
	/**
	 * Gets the code list.
	 * 
	 * @param key
	 *            the key
	 * @param callback
	 *            the callback
	 * @return the code list
	 */
	public void getCodeList(String key,
			AsyncCallback<ManageCodeListDetailModel> callback);
	
	/**
	 * Gets the code lists for category.
	 * 
	 * @param category
	 *            the category
	 * @param callback
	 *            the callback
	 * @return the code lists for category
	 */
	public void getCodeListsForCategory(String category, AsyncCallback<List<? extends HasListBox>> callback);
	
	/**
	 * Gets the codes.
	 * 
	 * @param codeListId
	 *            the code list id
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @param callback
	 *            the callback
	 * @return the codes
	 */
	public void getCodes(String codeListId,int startIndex,int pageSize,AsyncCallback<List<Code>> callback);
	
	/**
	 * Gets the code systems for category.
	 * 
	 * @param category
	 *            the category
	 * @param callback
	 *            the callback
	 * @return the code systems for category
	 */
	public void getCodeSystemsForCategory(String category, AsyncCallback<List<? extends HasListBox>> callback);
	
	/**
	 * Gets the grouped code list.
	 * 
	 * @param key
	 *            the key
	 * @param callback
	 *            the callback
	 * @return the grouped code list
	 */
	public void getGroupedCodeList(String key,
			AsyncCallback<ManageCodeListDetailModel> callback);
	
	/**
	 * Gets the grouped code list.
	 * 
	 * @param key
	 *            the key
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @param callback
	 *            the callback
	 * @return the grouped code list
	 */
	public void getGroupedCodeList(String key, int startIndex, int pageSize,
			AsyncCallback<ManageCodeListDetailModel> callback);
	
	
	/**
	 * Gets the list box data.
	 * 
	 * @param callback
	 *            the callback
	 * @return the list box data
	 */
	void getListBoxData(AsyncCallback<CodeListService.ListBoxData> callback);
	
	/**
	 * Gets the qDS data type for category.
	 * 
	 * @param category
	 *            the category
	 * @param callback
	 *            the callback
	 * @return the qDS data type for category
	 */
	public void getQDSDataTypeForCategory(String category, AsyncCallback<List<? extends HasListBox>> callback);
	
	/**
	 * Gets the qDS elements.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param vertsion
	 *            the vertsion
	 * @param callback
	 *            the callback
	 * @return the qDS elements
	 */
	public void getQDSElements(String measureId, String vertsion, AsyncCallback<List<QualityDataSetDTO>> callback);
	
	/**
	 * Gets the rel associations operators.
	 * 
	 * @param callback
	 *            the callback
	 * @return the rel associations operators
	 */
	void getRelAssociationsOperators(AsyncCallback<Map<String, String>> callback);
	
	/**
	 * Gets the timing operators.
	 * 
	 * @param callback
	 *            the callback
	 * @return the timing operators
	 */
	void getTimingOperators(AsyncCallback<Map<String, String>> callback);
	/**
	 * Checks if is code already exists.
	 * 
	 * @param codeListId
	 *            the code list id
	 * @param code
	 *            the code
	 * @param callback
	 *            the callback
	 */
	public void isCodeAlreadyExists(String codeListId, Code code,AsyncCallback<Boolean> callback);
	
	/**
	 * Saveor update code list.
	 * 
	 * @param currentDetails
	 *            the current details
	 * @param callback
	 *            the callback
	 */
	void saveorUpdateCodeList(ManageCodeListDetailModel currentDetails,
			AsyncCallback<SaveUpdateCodeListResult> callback);
	
	/**
	 * Save or update grouped code list.
	 * 
	 * @param currentDetails
	 *            the current details
	 * @param callback
	 *            the callback
	 */
	void saveorUpdateGroupedCodeList(ManageCodeListDetailModel currentDetails,
			AsyncCallback<SaveUpdateCodeListResult> callback);
	
	/**
	 * Save qd sto measure.
	 * 
	 * @param matValueSetTransferObject
	 *            the mat value set transfer object
	 * @param callback
	 *            the callback
	 */
	void saveQDStoMeasure(MatValueSetTransferObject matValueSetTransferObject,
			AsyncCallback<SaveUpdateCodeListResult> callback);
	
	/**
	 * Save user defined qds to measure.
	 * 
	 * @param matValueSetTransferObject
	 *            the mat value set transfer object
	 * @param asyncCallback
	 *            the async callback
	 */
	public void saveUserDefinedQDStoMeasure(MatValueSetTransferObject matValueSetTransferObject,
			AsyncCallback<SaveUpdateCodeListResult> asyncCallback);
	
	
	/**
	 * Search.
	 * 
	 * @param searchText
	 *            the search text
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @param sortColumn
	 *            the sort column
	 * @param isAsc
	 *            the is asc
	 * @param defaultCodeList
	 *            the default code list
	 * @param filter
	 *            the filter
	 * @param callback
	 *            the callback
	 */
	public void search(String searchText,
			int startIndex, int pageSize, String sortColumn, boolean isAsc, boolean defaultCodeList, int filter,
			AsyncCallback<ManageCodeListSearchModel> callback);
	
	/**
	 * Search.
	 * 
	 * @param searchText
	 *            the search text
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @param sortColumn
	 *            the sort column
	 * @param isAsc
	 *            the is asc
	 * @param defaultCodeList
	 *            the default code list
	 * @param filter
	 *            the filter
	 * @param categoryId
	 *            the category id
	 * @param callback
	 *            the callback
	 */
	public void search(String searchText,
			int startIndex, int pageSize, String sortColumn, boolean isAsc, boolean defaultCodeList, int filter,
			String categoryId,
			AsyncCallback<ManageCodeListSearchModel> callback);
	
	/**
	 * Search for admin.
	 * 
	 * @param searchText
	 *            the search text
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @param sortColumn
	 *            the sort column
	 * @param isAsc
	 *            the is asc
	 * @param defaultCodeList
	 *            the default code list
	 * @param filter
	 *            the filter
	 * @param callback
	 *            the callback
	 */
	void searchForAdmin(String searchText, int startIndex, int pageSize,
			String sortColumn, boolean isAsc, boolean defaultCodeList,
			int filter, AsyncCallback<AdminManageCodeListSearchModel> callback);
	
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
			AsyncCallback<TransferOwnerShipModel> callback);
	
	/**
	 * Search value sets for draft.
	 * 
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @param asyncCallback
	 *            the async callback
	 */
	public void searchValueSetsForDraft(int startIndex, int pageSize,
			AsyncCallback<ManageValueSetSearchModel> asyncCallback);
	
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
	 * Update code list to measure.
	 * 
	 * @param matValueSetTransferObject
	 *            mat Value Set Transfer Object
	 * @param asyncCallback
	 *            the async callback
	 */
	void updateCodeListToMeasure(MatValueSetTransferObject matValueSetTransferObject,
			AsyncCallback<SaveUpdateCodeListResult> asyncCallback);
	
	void saveCopiedQDMListToMeasure(GlobalCopyPasteObject gbCopyPaste, List<QualityDataSetDTO> qdmList, String measureId,
			AsyncCallback<SaveUpdateCodeListResult> asyncCallback);
	
}
