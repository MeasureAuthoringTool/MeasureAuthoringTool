package org.ifmc.mat.server.service;

import java.util.List;

import org.ifmc.mat.client.codelist.HasListBox;
import org.ifmc.mat.client.codelist.ManageCodeListDetailModel;
import org.ifmc.mat.client.codelist.ManageValueSetSearchModel;
import org.ifmc.mat.client.codelist.service.SaveUpdateCodeListResult;
import org.ifmc.mat.model.Code;
import org.ifmc.mat.model.CodeListSearchDTO;
import org.ifmc.mat.model.ListObject;
import org.ifmc.mat.model.QualityDataSetDTO;
import org.ifmc.mat.model.User;
import org.ifmc.mat.server.exception.ExcelParsingException;


public interface CodeListService {
	public int countSearchResultsWithFilter(String searchText, boolean defaultCodeList, int filter); 
	public List<CodeListSearchDTO> search(String searchText,
			int startIndex, int pageSize, String sortColumn, boolean isAsc,boolean defaultCodeList, int filter);

	public List<CodeListSearchDTO> search(String searchText,
			int startIndex, int pageSize, String sortColumn, boolean isAsc,boolean defaultCodeList, int filter, String categoryId);
	
	public List<? extends HasListBox> getCodeListsForCategory(String category);
	
	public List<Code> getCodes(String codeListId,int startIndex, int pageSize);
	public ManageCodeListDetailModel  getCodeList(String key);
	public ManageCodeListDetailModel  deleteCodes(String key,List<Code> Codes);
	public ManageCodeListDetailModel  getGroupedCodeList(String key);
	
	public org.ifmc.mat.client.codelist.service.CodeListService.ListBoxData getListBoxData();
	public SaveUpdateCodeListResult saveorUpdateCodeList(
			ManageCodeListDetailModel currentDetails) throws CodeListNotUniqueException, CodeListOidNotUniqueException, ExcelParsingException, InvalidLastModifiedDateException, ValueSetLastModifiedDateNotUniqueException;
	public SaveUpdateCodeListResult saveDefaultCodeList(User user) throws CodeListNotUniqueException;
	public SaveUpdateCodeListResult saveorUpdateGroupedCodeList(
			ManageCodeListDetailModel currentDetails) 
		throws CodeListNotUniqueException, CodeListOidNotUniqueException, InvalidLastModifiedDateException, ValueSetLastModifiedDateNotUniqueException;	
	
	public List<ListObject> getSupplimentalCodeList();
	public SaveUpdateCodeListResult saveQDStoMeasure(String measureId,String dataType,CodeListSearchDTO codeList,boolean isSpecificOccurrence);
	public List<? extends HasListBox> getCodeSystemsForCategory(String category);
	public List<? extends HasListBox> getQDSDataTypeForCategory(String category);
	public List<QualityDataSetDTO> getQDSElements(String measureId, String verision);
	
	public String generateUniqueOid(ManageCodeListDetailModel currentDetails);
	
	public String getGroupedCodeListCodeSystemsForCategory(String categoryId);
	public ManageValueSetSearchModel searchValueSetsForDraft(int startIndex, int pageSize);
	public ManageValueSetSearchModel createDraft(String id, String oid);
	public boolean isCodeAlreadyExists(String codeListId, Code code);
	//US193
	public ManageValueSetSearchModel createClone(String id);
}
