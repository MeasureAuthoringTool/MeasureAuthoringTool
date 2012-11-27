package org.ifmc.mat.client.codelist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ifmc.mat.model.Code;
import org.ifmc.mat.model.GroupedCodeListDTO;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ManageCodeListDetailModel implements IsSerializable {
	private String key;
	private String name;
	private String ID;
	private String steward;

	//US 413. Support Steward Other
	private String stewardValue;
	private String stewardOther;
	
	private String category;
	private String codeSystem;
	private String measureId;
	private boolean importFlag;
	private String codeSystemVersion;
	private String rationale;
	private String comments;
	private Code code;
	private List<GroupedCodeListDTO> codeLists = new ArrayList<GroupedCodeListDTO>();
	private List<Code> codes = new ArrayList<Code>();
	private int codesPageCount = 1;
	private int totalCodes;
	private int codeListsPageCount = 1;
	private int totalCodeList;
	
	private boolean isGrouped = false;
	private boolean draft;
	private String lastModifiedDate;
	private boolean isMyValueSet = true;
	
	private HashMap<String, GroupedCodeListDTO> addValueSetsMap = 	new HashMap<String, GroupedCodeListDTO>();
	private HashMap<String, GroupedCodeListDTO> removeValueSetsMap = new HashMap<String, GroupedCodeListDTO>();
	
	public boolean getIsGrouped(){
		return isGrouped;
	}
	public void setIsGrouped(boolean value){
		isGrouped = value;
	}
	
	
	private String oid;
	
	public List<Code> getCodes() {
		return codes;
	}
	public void setCodes(List<Code> codes) {
		this.codes = codes;
	}
	private String dataType;
	
	
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = doTrim(dataType);
	}
	private boolean isExistingCodeList;
	
	public boolean isExistingCodeList() {
		return isExistingCodeList;
	}
	public void setExistingCodeList(boolean isExistingCodeList) {
		this.isExistingCodeList = isExistingCodeList;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = doTrim(key);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = doTrim(name);
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = doTrim(iD);
	}
	public String getSteward() {
		return steward;
	}
	public void setSteward(String steward) {
		this.steward = doTrim(steward);
	}

	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = doTrim(category);
	}
	public String getCodeSystem() {
		return codeSystem;
	}
	public void setCodeSystem(String codeSystem) {
		this.codeSystem = doTrim(codeSystem);
	}
	public String getCodeSystemVersion() {
		return codeSystemVersion;
	}
	public void setCodeSystemVersion(String codeSystemVersion) {
		this.codeSystemVersion = doTrim(codeSystemVersion);
	}
	public String getRationale() {
		return rationale;
	}
	public void setRationale(String rationale) {
		this.rationale = doTrim(rationale);
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = doTrim(comments);
	}
	public Code getCode() {
		return code;
	}
	public void setCode(Code code) {
		this.code = code;
	}
	public List<GroupedCodeListDTO> getCodeLists() {
		return codeLists;
	}
	public void setCodeLists(List<GroupedCodeListDTO> codeLists) {
		this.codeLists = codeLists;
	}
	public String getMeasureId() {
		return measureId;
	}
	public void setMeasureId(String measureId) {
		this.measureId = doTrim(measureId);
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid.trim();
	}
	public boolean isImportFlag() {
		return importFlag;
	}
	public void setImportFlag(boolean importFlag) {
		this.importFlag = importFlag;
	}
	
	//US 413
	public String getStewardValue() {
		return stewardValue;
	}
	public void setStewardValue(String stewardValue) {
		this.stewardValue = doTrim(stewardValue);
	}
	public String getStewardOther() {
		return stewardOther;
	}
	public void setStewardOther(String stewardOther) {
		this.stewardOther = doTrim(stewardOther);
	}	
	
	private String doTrim(String str){
		if(str == null)
			return str;
		else
			return str.trim();
	}
	/*US537*/
	public boolean isDraft() {
		return draft;
	}
	public void setDraft(boolean draft) {
		this.draft = draft;
	}
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(String lastModifiedDate) {
		if(lastModifiedDate != null)
			lastModifiedDate = lastModifiedDate.trim();
		this.lastModifiedDate = lastModifiedDate;
	}
	
	public int getCodesPageCount() {
		return codesPageCount;
	}
	public void setCodesPageCount(int codesPageCount) {
		this.codesPageCount = codesPageCount;
	}
	public int getTotalCodes() {
		return totalCodes;
	}
	public void setTotalCodes(int totalCodes) {
		this.totalCodes = totalCodes;
	}
	public int getCodeListsPageCount() {
		return codeListsPageCount;
	}
	public void setCodeListsPageCount(int codeListsPageCount) {
		this.codeListsPageCount = codeListsPageCount;
	}
	public int getTotalCodeList() {
		return totalCodeList;
	}
	public void setTotalCodeList(int totalCodeList) {
		this.totalCodeList = totalCodeList;
	}
	public boolean isMyValueSet() {
		return isMyValueSet;
	}
	public void setMyValueSet(boolean isMyValueSet) {
		this.isMyValueSet = isMyValueSet;
	}
	public HashMap<String, GroupedCodeListDTO> getAddValueSetsMap() {
		return addValueSetsMap;
	}
	public HashMap<String, GroupedCodeListDTO> getRemoveValueSetsMap() {
		return removeValueSetsMap;
	}
	
	
}
