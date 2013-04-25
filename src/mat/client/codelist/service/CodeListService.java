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

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("codelist")
public interface CodeListService extends RemoteService {
	public static class ListBoxData implements IsSerializable {
		private List<? extends HasListBox> categoryList;
		private List<? extends HasListBox> codeSystemList;
		private List<? extends HasListBox> statusList;
		private List<? extends HasListBox> measureStewardList;
		private List<? extends HasListBox> authorsList;
		private List<? extends HasListBox> measureTypeList;
		
		//US 421. Retrieve the scoring choices from DB
		private List<? extends HasListBox> scoringList;
		
		//US 62. Retrieve Units from DB
		private List<? extends HasListBox> unitList;
		private List<? extends HasListBox> unitTypeList;
		private List<? extends HasListBox> unitTypeMatrixList;
		
		//US 171. Retrieve Operators from DB
		private List<? extends HasListBox> operatorList;
		private List<? extends HasListBox> operatorTypeList;
		
		private List<? extends HasListBox> logicalOperatorList;
		private List<? extends HasListBox> relTimingOperatorList;
		private List<? extends HasListBox> relAssocOperatorList;
		
		public List<? extends HasListBox> getMeasureTypeList() {
			return measureTypeList;
		}
		public void setMeasureTypeList(List<? extends HasListBox> measureTypeList) {
			this.measureTypeList = measureTypeList;
		}
		public List<? extends HasListBox> getAuthorsList() {
			return authorsList;
		}
		public void setAuthorsList(List<? extends HasListBox> authorsList) {
			this.authorsList = authorsList;
		}
		public List<? extends HasListBox> getMeasureStewardList() {
			return measureStewardList;
		}
		public void setMeasureStewardList(List<? extends HasListBox> measureStewardList) {
			this.measureStewardList = measureStewardList;
		}
		public List<? extends HasListBox> getStatusList() {
			return statusList;
		}
		public void setStatusList(List<? extends HasListBox> statusList) {
			this.statusList = statusList;
		}
		public List<? extends HasListBox> getCategoryList() {
			return categoryList;
		}
		public void setCategoryList(List<? extends HasListBox> categoryList) {
			this.categoryList = categoryList;
		}
		public List<? extends HasListBox> getCodeSystemList() {
			return codeSystemList;
		}
		public void setCodeSystemList(List<? extends HasListBox> codeSystemList) {
			this.codeSystemList = codeSystemList;
		}
		
		//US 421. Retrieve the scoring choices from DB
		public List<? extends HasListBox> getScoringList() {
			return scoringList;
		}
		public void setScoringList(List<? extends HasListBox> scoringList) {
			this.scoringList = scoringList;
		}
		
		//US 62. Retrieve the Units from DB
		public List<? extends HasListBox> getUnitList() {
			return unitList;
		}
		public void setUnitList(List<? extends HasListBox> unitList) {
			this.unitList = unitList;
		}

		public List<? extends HasListBox> getUnitTypeList() {
			return unitTypeList;
		}
		public void setUnitTypeList(List<? extends HasListBox> unitTypeList) {
			this.unitTypeList = unitTypeList;
		}
		
		public List<? extends HasListBox> getUnitTypeMatrixList() {
			return unitTypeMatrixList;
		}
		public void setUnitTypeMatrixList(List<? extends HasListBox> unitTypeMatrixList) {
			this.unitTypeMatrixList = unitTypeMatrixList;
		}
		
		//US 171. Retrieve the Operators from DB
		public List<? extends HasListBox> getOperatorList() {
			return operatorList;
		}
		public void setOperatorList(List<? extends HasListBox> operatorList) {
			this.operatorList = operatorList;
		}
		public List<? extends HasListBox> getOperatorTypeList() {
			return operatorTypeList;
		}
		public void setOperatorTypeList(List<? extends HasListBox> operatorTypeList) {
			this.operatorTypeList = operatorTypeList;
		}
		public List<? extends HasListBox> getLogicalOperatorList() {
			return logicalOperatorList;
		}
		public void setLogicalOperatorList(
				List<? extends HasListBox> logicalOperatorList) {
			this.logicalOperatorList = logicalOperatorList;
		}
		public List<? extends HasListBox> getRelTimingOperatorList() {
			return relTimingOperatorList;
		}
		public void setRelTimingOperatorList(
				List<? extends HasListBox> relTimingOperatorList) {
			this.relTimingOperatorList = relTimingOperatorList;
		}
		public List<? extends HasListBox> getRelAssocOperatorList() {
			return relAssocOperatorList;
		}
		public void setRelAssocOperatorList(
				List<? extends HasListBox> relAssocOperatorList) {
			this.relAssocOperatorList = relAssocOperatorList;
		}
		
		
	}
	
	public ManageCodeListSearchModel search(String searchText,
			int startIndex, int pageSize, String sortColumn, boolean isAsc,boolean defaultCodeList, int filter);
	
	public ManageCodeListSearchModel search(String searchText,
			int startIndex, int pageSize, String sortColumn, boolean isAsc, boolean defaultCodeList, int filter,
			String categoryId);
	
	public AdminManageCodeListSearchModel searchForAdmin(String searchText,
			int startIndex, int pageSize, String sortColumn, boolean isAsc,boolean defaultCodeList, int filter);
	
	public ListBoxData getListBoxData();
	
	public List<? extends HasListBox> getCodeSystemsForCategory(String category);
	public List<? extends HasListBox> getCodeListsForCategory(String category);
	
	public ManageCodeListDetailModel getCodeList(String key);
	public ManageCodeListDetailModel deleteCodes(String codeListID,List<Code> Codes);
	SaveUpdateCodeListResult addCodeListToMeasure(String measureId,
			String dataType, CodeListSearchDTO codeList,
			boolean isSpecificOccurrence,
			ArrayList<QualityDataSetDTO> appliedQDMs);
	public ManageCodeListDetailModel getGroupedCodeList(String key);
	
	
	public SaveUpdateCodeListResult saveorUpdateCodeList(ManageCodeListDetailModel currentDetails);
	public SaveUpdateCodeListResult saveorUpdateGroupedCodeList(ManageCodeListDetailModel currentDetails);
	
	List<? extends HasListBox> getQDSDataTypeForCategory(String category);

	
	public List<QualityDataSetDTO> getQDSElements(String measureId, String vertsion);
	
	public String generateUniqueOid(ManageCodeListDetailModel currentDetails);
	
	/*US537*/
	public ManageValueSetSearchModel searchValueSetsForDraft(int startIndex, int pageSize);
	public ManageValueSetSearchModel createDraft(String id, String oid);

	public List<Code> getCodes(String codeListId,int startIndex, int pageSize);

	public ManageCodeListDetailModel getGroupedCodeList(String key, int startIndex,
			int pageSize);

	public boolean isCodeAlreadyExists(String codeListId, Code code);

	//US193
	public ManageValueSetSearchModel createClone(String id);

	public void transferOwnerShipToUser(List<String> list, String toEmail);

	public TransferOwnerShipModel searchUsers(int startIndex, int pageSize);
	
	public Map<String, String> getTimingOperators();

	List<String> getAllUnits(); 
}
