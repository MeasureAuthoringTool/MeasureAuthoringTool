package org.ifmc.mat.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import org.ifmc.mat.DTO.CodesDTO;
import org.ifmc.mat.client.codelist.ManageCodeListDetailModel;
import org.ifmc.mat.model.CodeList;
import org.ifmc.mat.model.clause.Measure;

public interface CodeListDAO extends IDAO<CodeList, String> {
	//US 413. Parameter added --> stewardOther
	public CodeList getCodeList(ManageCodeListDetailModel currentDetails, String userid);
	public List<CodeList> getValueSetsForCategory(String categoryId);
	public List<CodeList> getCodeListsForCategoryByMeasure(String categoryId, Measure measure);
	
	public Set<CodesDTO> getCodes(String codeListId);
	public List<CodeList> getCodeList(ManageCodeListDetailModel currentDetails, Timestamp ts);
}
