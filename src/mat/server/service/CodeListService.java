package mat.server.service;

import java.util.List;

import mat.DTO.OperatorDTO;
import mat.DTO.UnitDTO;
import mat.client.codelist.HasListBox;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.model.Code;
import mat.model.CodeListSearchDTO;
import mat.model.MatValueSetTransferObject;
import mat.model.QualityDataSetDTO;

/**
 * The Interface CodeListService.
 */
public interface CodeListService {
	
	/**
	 * Count search results with filter.
	 * 
	 * @param searchText
	 *            the search text
	 * @param defaultCodeList
	 *            the default code list
	 * @param filter
	 *            the filter
	 * @return the int
	 */
	int countSearchResultsWithFilter(String searchText, boolean defaultCodeList, int filter);
	
	// US193
	/**
	 * Creates the clone.
	 * 
	 * @param id
	 *            the id
	 * @return the manage value set search model
	 */
//	ManageValueSetSearchModel createClone(String id);
	
	/**
	 * Creates the draft.
	 * 
	 * @param id
	 *            the id
	 * @param oid
	 *            the oid
	 * @return the manage value set search model
	 */
	//ManageValueSetSearchModel createDraft(String id, String oid);
	
	/**
	 * Delete codes.
	 * 
	 * @param key
	 *            the key
	 * @param Codes
	 *            the codes
	 * @return the manage code list detail model
	 */
	//ManageCodeListDetailModel deleteCodes(String key, List<Code> Codes);
	
	/**
	 * Generate unique oid.
	 * 
	 * @param currentDetails
	 *            the current details
	 * @return the string
	 */
	//String generateUniqueOid(ManageCodeListDetailModel currentDetails);
	
	/**
	 * Gets the all data types.
	 * 
	 * @return the all data types
	 */
	List<? extends HasListBox> getAllDataTypes();
	
	/**
	 * Gets the all operators.
	 * 
	 * @return the all operators
	 */
	List<OperatorDTO> getAllOperators();
	
	/**
	 * Gets the all units.
	 * 
	 * @return the all units
	 */
	List<UnitDTO> getAllUnits();
	
	/**
	 * Gets the code list.
	 * 
	 * @param key
	 *            the key
	 * @return the code list
	 */
	//ManageCodeListDetailModel getCodeList(String key);
	
	/**
	 * Gets the code lists for category.
	 * 
	 * @param category
	 *            the category
	 * @return the code lists for category
	 */
	List<? extends HasListBox> getCodeListsForCategory(String category);
	
	/**
	 * Gets the codes.
	 * 
	 * @param codeListId
	 *            the code list id
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @return the codes
	 */
	List<Code> getCodes(String codeListId, int startIndex, int pageSize);
	
	/**
	 * Gets the code systems for category.
	 * 
	 * @param category
	 *            the category
	 * @return the code systems for category
	 */
	List<? extends HasListBox> getCodeSystemsForCategory(String category);
	
	/**
	 * Gets the grouped code list.
	 * 
	 * @param key
	 *            the key
	 * @return the grouped code list
	 */
	//ManageCodeListDetailModel getGroupedCodeList(String key);
	
	/**
	 * Gets the grouped code list code systems for category.
	 * 
	 * @param categoryId
	 *            the category id
	 * @return the grouped code list code systems for category
	 */
	//String getGroupedCodeListCodeSystemsForCategory(String categoryId);
	
	/**
	 * Gets the list box data.
	 * 
	 * @return the list box data
	 */
	mat.client.codelist.service.CodeListService.ListBoxData getListBoxData();
	
	/**
	 * Gets the qds data type for category.
	 * 
	 * @param category
	 *            the category
	 * @return the QDS data type for category
	 */
	List<? extends HasListBox> getQDSDataTypeForCategory(String category);
	
	/**
	 * Gets the qDS elements.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param verision
	 *            the verision
	 * @return the qDS elements
	 */
	List<QualityDataSetDTO> getQDSElements(String measureId, String verision);
	
	/**
	 * Gets the rel associations operators.
	 * 
	 * @return the rel associations operators
	 */
//	List<OperatorDTO> getRelAssociationsOperators();
	
	/**
	 * Gets the supplimental code list.
	 * 
	 * @return the supplimental code list
	 */
//	List<ListObject> getSupplimentalCodeList();
	
	/**
	 * Gets the timing operators.
	 * 
	 * @return the timing operators
	 */
//	List<OperatorDTO> getTimingOperators();
	
	/**
	 * Checks if is code already exists.
	 * 
	 * @param codeListId
	 *            the code list id
	 * @param code
	 *            the code
	 * @return true, if is code already exists
	 */
//	boolean isCodeAlreadyExists(String codeListId, Code code);
	
	/**
	 * Save default code list.
	 * 
	 * @param user
	 *            the user
	 * @return the save update code list result
	 * @throws CodeListNotUniqueException
	 *             the code list not unique exception
	 */
//	SaveUpdateCodeListResult saveDefaultCodeList(User user) throws CodeListNotUniqueException;
	
	/**
	 * Saveor update code list.
	 * 
	 * @param currentDetails
	 *            the current details
	 * @return the save update code list result
	 * @throws CodeListNotUniqueException
	 *             the code list not unique exception
	 * @throws CodeListOidNotUniqueException
	 *             the code list oid not unique exception
	 * @throws ExcelParsingException
	 *             the excel parsing exception
	 * @throws InvalidLastModifiedDateException
	 *             the invalid last modified date exception
	 * @throws ValueSetLastModifiedDateNotUniqueException
	 *             the value set last modified date not unique exception
	 */
	//SaveUpdateCodeListResult saveorUpdateCodeList(ManageCodeListDetailModel currentDetails) throws CodeListNotUniqueException,
	//CodeListOidNotUniqueException, ExcelParsingException, InvalidLastModifiedDateException,
	//ValueSetLastModifiedDateNotUniqueException;
	
	/**
	 * Saveor update grouped code list.
	 * 
	 * @param currentDetails
	 *            the current details
	 * @return the save update code list result
	 * @throws CodeListNotUniqueException
	 *             the code list not unique exception
	 * @throws CodeListOidNotUniqueException
	 *             the code list oid not unique exception
	 * @throws InvalidLastModifiedDateException
	 *             the invalid last modified date exception
	 * @throws ValueSetLastModifiedDateNotUniqueException
	 *             the value set last modified date not unique exception
	 */
	//SaveUpdateCodeListResult saveorUpdateGroupedCodeList(ManageCodeListDetailModel currentDetails) throws CodeListNotUniqueException,
//	CodeListOidNotUniqueException, InvalidLastModifiedDateException, ValueSetLastModifiedDateNotUniqueException;
	
	/**
	 * Save qd sto measure.
	 * 
	 * @param valueSetTransferObject
	 *            the value set transfer object
	 * @return the save update code list result
	 */
	SaveUpdateCodeListResult saveQDStoMeasure(MatValueSetTransferObject valueSetTransferObject);
	
	/**
	 * Save user defined qd sto measure.
	 * 
	 * @param matValueSetTransferObject
	 *            the mat value set transfer object
	 * @return the save update code list result
	 */
	SaveUpdateCodeListResult saveUserDefinedQDStoMeasure(MatValueSetTransferObject matValueSetTransferObject);
	
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
	 * @return the list
	 */
	List<CodeListSearchDTO> search(String searchText, int startIndex, int pageSize, String sortColumn, boolean isAsc,
			boolean defaultCodeList, int filter);
	
	
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
	 * @return the list
	 */
//	List<CodeListSearchDTO> search(String searchText, int startIndex, int pageSize, String sortColumn, boolean isAsc,
//			boolean defaultCodeList, int filter, String categoryId);
	
	/**
	 * Search value sets for draft.
	 * 
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @return the manage value set search model
	 */
	//ManageValueSetSearchModel searchValueSetsForDraft(int startIndex, int pageSize);
	
	/**
	 * Transfer owner ship to user.
	 * 
	 * @param list
	 *            the list
	 * @param toEmail
	 *            the to email
	 */
	//void transferOwnerShipToUser(List<String> list, String toEmail);
	
	/**
	 * Update qds to measure.
	 * 
	 * @param matValueSetTransferObject
	 *            the MatValueSetTransferObject object
	 * @return the SaveUpdateCodeListResult
	 */
	SaveUpdateCodeListResult updateQDStoMeasure(MatValueSetTransferObject matValueSetTransferObject);
	
	
}
