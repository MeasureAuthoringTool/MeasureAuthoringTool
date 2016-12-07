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
import mat.model.MatValueSetTransferObject;
import mat.model.QualityDataSetDTO;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The Interface CodeListService.
 */
@RemoteServiceRelativePath("codelist")
public interface CodeListService extends RemoteService {
	
	/**
	 * The Class ListBoxData.
	 */
	public static class ListBoxData implements IsSerializable {
		
		/** The authors list. */
		private List<? extends HasListBox> authorsList;
		
		/** The category list. */
		private List<? extends HasListBox> categoryList;
		
		/** The code system list. */
		private List<? extends HasListBox> codeSystemList;
		
		/** The logical operator list. */
		private List<? extends HasListBox> logicalOperatorList;
		
		/** The measure steward list. */
		private List<? extends HasListBox> measureStewardList;
		
		/** The measure type list. */
		private List<? extends HasListBox> measureTypeList;
		
		//US 171. Retrieve Operators from DB
		/** The operator list. */
		private List<? extends HasListBox> operatorList;
		
		/** The operator type list. */
		private List<? extends HasListBox> operatorTypeList;
		
		/** The rel assoc operator list. */
		private List<? extends HasListBox> relAssocOperatorList;
		
		/** The rel timing operator list. */
		private List<? extends HasListBox> relTimingOperatorList;
		
		//US 421. Retrieve the scoring choices from DB
		/** The scoring list. */
		private List<? extends HasListBox> scoringList;
		
		/** The status list. */
		private List<? extends HasListBox> statusList;
		
		//US 62. Retrieve Units from DB
		/** The unit list. */
		private List<? extends HasListBox> unitList;
		
		/** The unit list. */
		private List<? extends HasListBox> cqlUnitList;
		
		/** The unit type list. */
		private List<? extends HasListBox> unitTypeList;
		
		/** The unit type matrix list. */
		private List<? extends HasListBox> unitTypeMatrixList;
		
		/**
		 * Gets the authors list.
		 * 
		 * @return the authors list
		 */
		public List<? extends HasListBox> getAuthorsList() {
			return authorsList;
		}
		
		/**
		 * Gets the category list.
		 * 
		 * @return the category list
		 */
		public List<? extends HasListBox> getCategoryList() {
			return categoryList;
		}
		
		/**
		 * Gets the code system list.
		 * 
		 * @return the code system list
		 */
		public List<? extends HasListBox> getCodeSystemList() {
			return codeSystemList;
		}
		
		/**
		 * Gets the logical operator list.
		 * 
		 * @return the logical operator list
		 */
		public List<? extends HasListBox> getLogicalOperatorList() {
			return logicalOperatorList;
		}
		
		/**
		 * Gets the measure steward list.
		 * 
		 * @return the measure steward list
		 */
		public List<? extends HasListBox> getMeasureStewardList() {
			return measureStewardList;
		}
		
		/**
		 * Gets the measure type list.
		 * 
		 * @return the measure type list
		 */
		public List<? extends HasListBox> getMeasureTypeList() {
			return measureTypeList;
		}
		
		//US 171. Retrieve the Operators from DB
		/**
		 * Gets the operator list.
		 * 
		 * @return the operator list
		 */
		public List<? extends HasListBox> getOperatorList() {
			return operatorList;
		}
		
		/**
		 * Gets the operator type list.
		 * 
		 * @return the operator type list
		 */
		public List<? extends HasListBox> getOperatorTypeList() {
			return operatorTypeList;
		}
		
		/**
		 * Gets the rel assoc operator list.
		 * 
		 * @return the rel assoc operator list
		 */
		public List<? extends HasListBox> getRelAssocOperatorList() {
			return relAssocOperatorList;
		}
		
		/**
		 * Gets the rel timing operator list.
		 * 
		 * @return the rel timing operator list
		 */
		public List<? extends HasListBox> getRelTimingOperatorList() {
			return relTimingOperatorList;
		}
		
		//US 421. Retrieve the scoring choices from DB
		/**
		 * Gets the scoring list.
		 * 
		 * @return the scoring list
		 */
		public List<? extends HasListBox> getScoringList() {
			return scoringList;
		}
		
		/**
		 * Gets the status list.
		 * 
		 * @return the status list
		 */
		public List<? extends HasListBox> getStatusList() {
			return statusList;
		}
		
		//US 62. Retrieve the Units from DB
		/**
		 * Gets the unit list.
		 * 
		 * @return the unit list
		 */
		public List<? extends HasListBox> getUnitList() {
			return unitList;
		}
		
		/**
		 * @return the cqlUnitList
		 */
		public List<? extends HasListBox> getCqlUnitList() {
			return cqlUnitList;
		}

		/**
		 * @param cqlUnitList the cqlUnitList to set
		 */
		public void setCqlUnitList(List<? extends HasListBox> cqlUnitList) {
			this.cqlUnitList = cqlUnitList;
		}

		/**
		 * Gets the unit type list.
		 * 
		 * @return the unit type list
		 */
		public List<? extends HasListBox> getUnitTypeList() {
			return unitTypeList;
		}
		
		/**
		 * Gets the unit type matrix list.
		 * 
		 * @return the unit type matrix list
		 */
		public List<? extends HasListBox> getUnitTypeMatrixList() {
			return unitTypeMatrixList;
		}
		
		/**
		 * Sets the authors list.
		 * 
		 * @param authorsList
		 *            the new authors list
		 */
		public void setAuthorsList(List<? extends HasListBox> authorsList) {
			this.authorsList = authorsList;
		}
		
		/**
		 * Sets the category list.
		 * 
		 * @param categoryList
		 *            the new category list
		 */
		public void setCategoryList(List<? extends HasListBox> categoryList) {
			this.categoryList = categoryList;
		}
		
		/**
		 * Sets the code system list.
		 * 
		 * @param codeSystemList
		 *            the new code system list
		 */
		public void setCodeSystemList(List<? extends HasListBox> codeSystemList) {
			this.codeSystemList = codeSystemList;
		}
		
		/**
		 * Sets the logical operator list.
		 * 
		 * @param logicalOperatorList
		 *            the new logical operator list
		 */
		public void setLogicalOperatorList(
				List<? extends HasListBox> logicalOperatorList) {
			this.logicalOperatorList = logicalOperatorList;
		}
		
		/**
		 * Sets the measure steward list.
		 * 
		 * @param measureStewardList
		 *            the new measure steward list
		 */
		public void setMeasureStewardList(List<? extends HasListBox> measureStewardList) {
			this.measureStewardList = measureStewardList;
		}
		
		/**
		 * Sets the measure type list.
		 * 
		 * @param measureTypeList
		 *            the new measure type list
		 */
		public void setMeasureTypeList(List<? extends HasListBox> measureTypeList) {
			this.measureTypeList = measureTypeList;
		}
		
		/**
		 * Sets the operator list.
		 * 
		 * @param operatorList
		 *            the new operator list
		 */
		public void setOperatorList(List<? extends HasListBox> operatorList) {
			this.operatorList = operatorList;
		}
		
		/**
		 * Sets the operator type list.
		 * 
		 * @param operatorTypeList
		 *            the new operator type list
		 */
		public void setOperatorTypeList(List<? extends HasListBox> operatorTypeList) {
			this.operatorTypeList = operatorTypeList;
		}
		
		/**
		 * Sets the rel assoc operator list.
		 * 
		 * @param relAssocOperatorList
		 *            the new rel assoc operator list
		 */
		public void setRelAssocOperatorList(
				List<? extends HasListBox> relAssocOperatorList) {
			this.relAssocOperatorList = relAssocOperatorList;
		}
		
		/**
		 * Sets the rel timing operator list.
		 * 
		 * @param relTimingOperatorList
		 *            the new rel timing operator list
		 */
		public void setRelTimingOperatorList(
				List<? extends HasListBox> relTimingOperatorList) {
			this.relTimingOperatorList = relTimingOperatorList;
		}
		
		/**
		 * Sets the scoring list.
		 * 
		 * @param scoringList
		 *            the new scoring list
		 */
		public void setScoringList(List<? extends HasListBox> scoringList) {
			this.scoringList = scoringList;
		}
		
		/**
		 * Sets the status list.
		 * 
		 * @param statusList
		 *            the new status list
		 */
		public void setStatusList(List<? extends HasListBox> statusList) {
			this.statusList = statusList;
		}
		
		/**
		 * Sets the unit list.
		 * 
		 * @param unitList
		 *            the new unit list
		 */
		public void setUnitList(List<? extends HasListBox> unitList) {
			this.unitList = unitList;
		}
		
		/**
		 * Sets the unit type list.
		 * 
		 * @param unitTypeList
		 *            the new unit type list
		 */
		public void setUnitTypeList(List<? extends HasListBox> unitTypeList) {
			this.unitTypeList = unitTypeList;
		}
		
		/**
		 * Sets the unit type matrix list.
		 * 
		 * @param unitTypeMatrixList
		 *            the new unit type matrix list
		 */
		public void setUnitTypeMatrixList(List<? extends HasListBox> unitTypeMatrixList) {
			this.unitTypeMatrixList = unitTypeMatrixList;
		}

		
	}
	
	//US193
	/**
	 * Creates the clone.
	 * 
	 * @param id
	 *            the id
	 * @return the manage value set search model
	 */
	public ManageValueSetSearchModel createClone(String id);
	
	/**
	 * Creates the draft.
	 * 
	 * @param id
	 *            the id
	 * @param oid
	 *            the oid
	 * @return the manage value set search model
	 */
	public ManageValueSetSearchModel createDraft(String id, String oid);
	
	/**
	 * Delete codes.
	 * 
	 * @param codeListID
	 *            the code list id
	 * @param Codes
	 *            the codes
	 * @return the manage code list detail model
	 */
	public ManageCodeListDetailModel deleteCodes(String codeListID,List<Code> Codes);
	
	/**
	 * Generate unique oid.
	 * 
	 * @param currentDetails
	 *            the current details
	 * @return the string
	 */
	public String generateUniqueOid(ManageCodeListDetailModel currentDetails);
	
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
	List<String> getAllUnits();
	
	/**
	 * Gets the code list.
	 * 
	 * @param key
	 *            the key
	 * @return the code list
	 */
	public ManageCodeListDetailModel getCodeList(String key);
	/**
	 * Gets the code lists for category.
	 * 
	 * @param category
	 *            the category
	 * @return the code lists for category
	 */
	public List<? extends HasListBox> getCodeListsForCategory(String category);
	
	
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
	public List<Code> getCodes(String codeListId,int startIndex, int pageSize);
	
	/**
	 * Gets the code systems for category.
	 * 
	 * @param category
	 *            the category
	 * @return the code systems for category
	 */
	public List<? extends HasListBox> getCodeSystemsForCategory(String category);
	
	/**
	 * Gets the grouped code list.
	 * 
	 * @param key
	 *            the key
	 * @return the grouped code list
	 */
	public ManageCodeListDetailModel getGroupedCodeList(String key);
	
	
	/**
	 * Gets the grouped code list.
	 * 
	 * @param key
	 *            the key
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @return the grouped code list
	 */
	public ManageCodeListDetailModel getGroupedCodeList(String key, int startIndex,
			int pageSize);
	
	/**
	 * Gets the list box data.
	 * 
	 * @return the list box data
	 */
	public ListBoxData getListBoxData();
	
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
	 * @param vertsion
	 *            the vertsion
	 * @return the qDS elements
	 */
	public List<QualityDataSetDTO> getQDSElements(String measureId, String vertsion);
	
	/**
	 * Gets the rel associations operators.
	 * 
	 * @return the rel associations operators
	 */
	Map<String, String> getRelAssociationsOperators();
	
	/**
	 * Gets the timing operators.
	 * 
	 * @return the timing operators
	 */
	public Map<String, String> getTimingOperators();
	
	/**
	 * Checks if is code already exists.
	 * 
	 * @param codeListId
	 *            the code list id
	 * @param code
	 *            the code
	 * @return true, if is code already exists
	 */
	public boolean isCodeAlreadyExists(String codeListId, Code code);
	
	/**
	 * Saveor update code list.
	 * 
	 * @param currentDetails
	 *            the current details
	 * @return the save update code list result
	 */
	public SaveUpdateCodeListResult saveorUpdateCodeList(ManageCodeListDetailModel currentDetails);
	
	/**
	 * Save or update grouped code list.
	 * 
	 * @param currentDetails
	 *            the current details
	 * @return the save update code list result
	 */
	public SaveUpdateCodeListResult saveorUpdateGroupedCodeList(ManageCodeListDetailModel currentDetails);
	
	/**
	 * Save qds to measure.
	 * 
	 * @param matValueSetTransferObject
	 *            the mat value set transfer object
	 * @return the save update code list result
	 */
	SaveUpdateCodeListResult saveQDStoMeasure(MatValueSetTransferObject matValueSetTransferObject);
	
	/**
	 * Save user defined qds to measure.
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
	 * @return the manage code list search model
	 */
	public ManageCodeListSearchModel search(String searchText,
			int startIndex, int pageSize, String sortColumn, boolean isAsc,boolean defaultCodeList, int filter);
	
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
	 * @return the manage code list search model
	 */
	public ManageCodeListSearchModel search(String searchText,
			int startIndex, int pageSize, String sortColumn, boolean isAsc, boolean defaultCodeList, int filter,
			String categoryId);
	
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
	 * @return the admin manage code list search model
	 */
	public AdminManageCodeListSearchModel searchForAdmin(String searchText,
			int startIndex, int pageSize, String sortColumn, boolean isAsc,boolean defaultCodeList, int filter);
	
	/**
	 * Search users.
	 * 
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @return the transfer owner ship model
	 */
	public TransferOwnerShipModel searchUsers(int startIndex, int pageSize);
	
	/*US537*/
	/**
	 * Search value sets for draft.
	 * 
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @return the manage value set search model
	 */
	public ManageValueSetSearchModel searchValueSetsForDraft(int startIndex, int pageSize);
	
	/**
	 * Transfer owner ship to user.
	 * 
	 * @param list
	 *            the list
	 * @param toEmail
	 *            the to email
	 */
	public void transferOwnerShipToUser(List<String> list, String toEmail);
	
	/**
	 * Update code list to measure.
	 * 
	 * @param matValueSetTransferObject
	 *            the mat value set transfer object
	 * @return the save update code list result
	 */
	SaveUpdateCodeListResult updateCodeListToMeasure(MatValueSetTransferObject matValueSetTransferObject);
	
	
	SaveUpdateCodeListResult saveCopiedQDMListToMeasure(mat.model.GlobalCopyPasteObject gbCopyPaste,
			List<QualityDataSetDTO> qdmList, String measureId);

	List<String> getAllCqlUnits();
	
	
}
