package mat.client.codelist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mat.model.Code;
import mat.model.GroupedCodeListDTO;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class ManageCodeListDetailModel.
 */
public class ManageCodeListDetailModel implements IsSerializable {
	
	/** The key. */
	private String key;
	
	/** The name. */
	private String name;
	
	/** The id. */
	private String ID;
	
	/** The steward. */
	private String steward;

	//US 413. Support Steward Other
	/** The steward value. */
	private String stewardValue;
	
	/** The steward other. */
	private String stewardOther;
	
	/** The category. */
	private String category;
	
	/** The code system. */
	private String codeSystem;
	
	/** The measure id. */
	private String measureId;
	
	/** The import flag. */
	private boolean importFlag;
	
	/** The code system version. */
	private String codeSystemVersion;
	
	/** The rationale. */
	private String rationale;
	
	/** The comments. */
	private String comments;
	
	/** The code. */
	private Code code;
	
	/** The code lists. */
	private List<GroupedCodeListDTO> codeLists = new ArrayList<GroupedCodeListDTO>();
	
	/** The codes. */
	private List<Code> codes = new ArrayList<Code>();
	
	/** The codes page count. */
	private int codesPageCount = 1;
	
	/** The total codes. */
	private int totalCodes;
	
	/** The code lists page count. */
	private int codeListsPageCount = 1;
	
	/** The total code list. */
	private int totalCodeList;
	
	/** The is grouped. */
	private boolean isGrouped = false;
	
	/** The draft. */
	private boolean draft;
	
	/** The last modified date. */
	private String lastModifiedDate;
	
	/** The is my value set. */
	private boolean isMyValueSet = true;
	
	/** The add value sets map. */
	private HashMap<String, GroupedCodeListDTO> addValueSetsMap = 	new HashMap<String, GroupedCodeListDTO>();
	
	/** The remove value sets map. */
	private HashMap<String, GroupedCodeListDTO> removeValueSetsMap = new HashMap<String, GroupedCodeListDTO>();
	
	/**
	 * Gets the checks if is grouped.
	 * 
	 * @return the checks if is grouped
	 */
	public boolean getIsGrouped(){
		return isGrouped;
	}
	
	/**
	 * Sets the checks if is grouped.
	 * 
	 * @param value
	 *            the new checks if is grouped
	 */
	public void setIsGrouped(boolean value){
		isGrouped = value;
	}
	
	
	/** The oid. */
	private String oid;
	
	/**
	 * Gets the codes.
	 * 
	 * @return the codes
	 */
	public List<Code> getCodes() {
		return codes;
	}
	
	/**
	 * Sets the codes.
	 * 
	 * @param codes
	 *            the new codes
	 */
	public void setCodes(List<Code> codes) {
		this.codes = codes;
	}
	
	/** The data type. */
	private String dataType;
	
	
	/**
	 * Gets the data type.
	 * 
	 * @return the data type
	 */
	public String getDataType() {
		return dataType;
	}
	
	/**
	 * Sets the data type.
	 * 
	 * @param dataType
	 *            the new data type
	 */
	public void setDataType(String dataType) {
		this.dataType = doTrim(dataType);
	}
	
	/** The is existing code list. */
	private boolean isExistingCodeList;
	
	/**
	 * Checks if is existing code list.
	 * 
	 * @return true, if is existing code list
	 */
	public boolean isExistingCodeList() {
		return isExistingCodeList;
	}
	
	/**
	 * Sets the existing code list.
	 * 
	 * @param isExistingCodeList
	 *            the new existing code list
	 */
	public void setExistingCodeList(boolean isExistingCodeList) {
		this.isExistingCodeList = isExistingCodeList;
	}
	
	/**
	 * Gets the key.
	 * 
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * Sets the key.
	 * 
	 * @param key
	 *            the new key
	 */
	public void setKey(String key) {
		this.key = doTrim(key);
	}
	
	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = doTrim(name);
	}
	
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getID() {
		return ID;
	}
	
	/**
	 * Sets the id.
	 * 
	 * @param iD
	 *            the new id
	 */
	public void setID(String iD) {
		ID = doTrim(iD);
	}
	
	/**
	 * Gets the steward.
	 * 
	 * @return the steward
	 */
	public String getSteward() {
		return steward;
	}
	
	/**
	 * Sets the steward.
	 * 
	 * @param steward
	 *            the new steward
	 */
	public void setSteward(String steward) {
		this.steward = doTrim(steward);
	}

	/**
	 * Gets the category.
	 * 
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	
	/**
	 * Sets the category.
	 * 
	 * @param category
	 *            the new category
	 */
	public void setCategory(String category) {
		this.category = doTrim(category);
	}
	
	/**
	 * Gets the code system.
	 * 
	 * @return the code system
	 */
	public String getCodeSystem() {
		return codeSystem;
	}
	
	/**
	 * Sets the code system.
	 * 
	 * @param codeSystem
	 *            the new code system
	 */
	public void setCodeSystem(String codeSystem) {
		this.codeSystem = doTrim(codeSystem);
	}
	
	/**
	 * Gets the code system version.
	 * 
	 * @return the code system version
	 */
	public String getCodeSystemVersion() {
		return codeSystemVersion;
	}
	
	/**
	 * Sets the code system version.
	 * 
	 * @param codeSystemVersion
	 *            the new code system version
	 */
	public void setCodeSystemVersion(String codeSystemVersion) {
		this.codeSystemVersion = doTrim(codeSystemVersion);
	}
	
	/**
	 * Gets the rationale.
	 * 
	 * @return the rationale
	 */
	public String getRationale() {
		return rationale;
	}
	
	/**
	 * Sets the rationale.
	 * 
	 * @param rationale
	 *            the new rationale
	 */
	public void setRationale(String rationale) {
		this.rationale = doTrim(rationale);
	}
	
	/**
	 * Gets the comments.
	 * 
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}
	
	/**
	 * Sets the comments.
	 * 
	 * @param comments
	 *            the new comments
	 */
	public void setComments(String comments) {
		this.comments = doTrim(comments);
	}
	
	/**
	 * Gets the code.
	 * 
	 * @return the code
	 */
	public Code getCode() {
		return code;
	}
	
	/**
	 * Sets the code.
	 * 
	 * @param code
	 *            the new code
	 */
	public void setCode(Code code) {
		this.code = code;
	}
	
	/**
	 * Gets the code lists.
	 * 
	 * @return the code lists
	 */
	public List<GroupedCodeListDTO> getCodeLists() {
		return codeLists;
	}
	
	/**
	 * Sets the code lists.
	 * 
	 * @param codeLists
	 *            the new code lists
	 */
	public void setCodeLists(List<GroupedCodeListDTO> codeLists) {
		this.codeLists = codeLists;
	}
	
	/**
	 * Gets the measure id.
	 * 
	 * @return the measure id
	 */
	public String getMeasureId() {
		return measureId;
	}
	
	/**
	 * Sets the measure id.
	 * 
	 * @param measureId
	 *            the new measure id
	 */
	public void setMeasureId(String measureId) {
		this.measureId = doTrim(measureId);
	}
	
	/**
	 * Gets the oid.
	 * 
	 * @return the oid
	 */
	public String getOid() {
		return oid;
	}
	
	/**
	 * Sets the oid.
	 * 
	 * @param oid
	 *            the new oid
	 */
	public void setOid(String oid) {
		this.oid = oid.trim();
	}
	
	/**
	 * Checks if is import flag.
	 * 
	 * @return true, if is import flag
	 */
	public boolean isImportFlag() {
		return importFlag;
	}
	
	/**
	 * Sets the import flag.
	 * 
	 * @param importFlag
	 *            the new import flag
	 */
	public void setImportFlag(boolean importFlag) {
		this.importFlag = importFlag;
	}
	
	//US 413
	/**
	 * Gets the steward value.
	 * 
	 * @return the steward value
	 */
	public String getStewardValue() {
		return stewardValue;
	}
	
	/**
	 * Sets the steward value.
	 * 
	 * @param stewardValue
	 *            the new steward value
	 */
	public void setStewardValue(String stewardValue) {
		this.stewardValue = doTrim(stewardValue);
	}
	
	/**
	 * Gets the steward other.
	 * 
	 * @return the steward other
	 */
	public String getStewardOther() {
		return stewardOther;
	}
	
	/**
	 * Sets the steward other.
	 * 
	 * @param stewardOther
	 *            the new steward other
	 */
	public void setStewardOther(String stewardOther) {
		this.stewardOther = doTrim(stewardOther);
	}	
	
	/**
	 * Do trim.
	 * 
	 * @param str
	 *            the str
	 * @return the string
	 */
	private String doTrim(String str){
		if(str == null)
			return str;
		else
			return str.trim();
	}
	/*US537*/
	/**
	 * Checks if is draft.
	 * 
	 * @return true, if is draft
	 */
	public boolean isDraft() {
		return draft;
	}
	
	/**
	 * Sets the draft.
	 * 
	 * @param draft
	 *            the new draft
	 */
	public void setDraft(boolean draft) {
		this.draft = draft;
	}
	
	/**
	 * Gets the last modified date.
	 * 
	 * @return the last modified date
	 */
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	
	/**
	 * Sets the last modified date.
	 * 
	 * @param lastModifiedDate
	 *            the new last modified date
	 */
	public void setLastModifiedDate(String lastModifiedDate) {
		if(lastModifiedDate != null)
			lastModifiedDate = lastModifiedDate.trim();
		this.lastModifiedDate = lastModifiedDate;
	}
	
	/**
	 * Gets the codes page count.
	 * 
	 * @return the codes page count
	 */
	public int getCodesPageCount() {
		return codesPageCount;
	}
	
	/**
	 * Sets the codes page count.
	 * 
	 * @param codesPageCount
	 *            the new codes page count
	 */
	public void setCodesPageCount(int codesPageCount) {
		this.codesPageCount = codesPageCount;
	}
	
	/**
	 * Gets the total codes.
	 * 
	 * @return the total codes
	 */
	public int getTotalCodes() {
		return totalCodes;
	}
	
	/**
	 * Sets the total codes.
	 * 
	 * @param totalCodes
	 *            the new total codes
	 */
	public void setTotalCodes(int totalCodes) {
		this.totalCodes = totalCodes;
	}
	
	/**
	 * Gets the code lists page count.
	 * 
	 * @return the code lists page count
	 */
	public int getCodeListsPageCount() {
		return codeListsPageCount;
	}
	
	/**
	 * Sets the code lists page count.
	 * 
	 * @param codeListsPageCount
	 *            the new code lists page count
	 */
	public void setCodeListsPageCount(int codeListsPageCount) {
		this.codeListsPageCount = codeListsPageCount;
	}
	
	/**
	 * Gets the total code list.
	 * 
	 * @return the total code list
	 */
	public int getTotalCodeList() {
		return totalCodeList;
	}
	
	/**
	 * Sets the total code list.
	 * 
	 * @param totalCodeList
	 *            the new total code list
	 */
	public void setTotalCodeList(int totalCodeList) {
		this.totalCodeList = totalCodeList;
	}
	
	/**
	 * Checks if is my value set.
	 * 
	 * @return true, if is my value set
	 */
	public boolean isMyValueSet() {
		return isMyValueSet;
	}
	
	/**
	 * Sets the my value set.
	 * 
	 * @param isMyValueSet
	 *            the new my value set
	 */
	public void setMyValueSet(boolean isMyValueSet) {
		this.isMyValueSet = isMyValueSet;
	}
	
	/**
	 * Gets the adds the value sets map.
	 * 
	 * @return the adds the value sets map
	 */
	public HashMap<String, GroupedCodeListDTO> getAddValueSetsMap() {
		return addValueSetsMap;
	}
	
	/**
	 * Gets the removes the value sets map.
	 * 
	 * @return the removes the value sets map
	 */
	public HashMap<String, GroupedCodeListDTO> getRemoveValueSetsMap() {
		return removeValueSetsMap;
	}
	
	
}
