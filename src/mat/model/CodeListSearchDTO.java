package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CodeListSearchDTO implements IsSerializable {

	private String name;
	private String id;
	private String steward;
	private String oid;
	private String codeSystem;
	private String categoryCode;
	private String categoryDisplay;
	private String abbreviatedCategory;
	private String abbreviatedCodeSystem;
	private String lastModified;
	private boolean isGroupedCodeList;
	private boolean isTransferable;
	private String stewardOthers;
	private String ownerFirstName;
	private String ownerLastName;
	private String ownerEmailAddress;
	
	
	public String getStewardOthers() {
		return stewardOthers;
	}

	public void setStewardOthers(String stewardOthers) {
		this.stewardOthers = stewardOthers;
	}

	public boolean isDraft(){
		if(lastModified.equalsIgnoreCase("Draft"))
				return true;
		return false;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSteward() {
		return steward;
	}
	public void setSteward(String steward) {
		this.steward = steward;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getCategoryDisplay() {
		return categoryDisplay;
	}
	public void setCategoryDisplay(String categoryDisplay) {
		this.categoryDisplay = categoryDisplay;
	}
	public boolean isGroupedCodeList() {
		return isGroupedCodeList;
	}
	public void setGroupedCodeList(boolean isGroupedCodeList) {
		this.isGroupedCodeList= isGroupedCodeList;
	}
	public String getCodeSystem() {
		return codeSystem;
	}
	public void setCodeSystem(String codeSystem) {
		this.codeSystem = codeSystem;
	}
	
	public String getAbbreviatedCategory() {
		return abbreviatedCategory;
	}
	public void setAbbreviatedCategory(String abbreviatedCategory) {
		this.abbreviatedCategory = abbreviatedCategory;
	}
	public String getAbbreviatedCodeSystem() {
		return abbreviatedCodeSystem;
	}
	public void setAbbreviatedCodeSystem(String abbreviatedCodeSystem) {
		this.abbreviatedCodeSystem = abbreviatedCodeSystem;
	}
	public String getLastModified() {
		return lastModified;
	}
	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public void setTransferable(boolean isTransferable) {
		this.isTransferable = isTransferable;
	}

	public boolean isTransferable() {
		return isTransferable;
	}
	
	public String getOwnerFirstName() {
		return ownerFirstName;
	}

	public void setOwnerFirstName(String ownerFirstName) {
		this.ownerFirstName = ownerFirstName;
	}

	public String getOwnerLastName() {
		return ownerLastName;
	}

	public void setOwnerLastName(String ownerLastName) {
		this.ownerLastName = ownerLastName;
	}

	public String getOwnerEmailAddress() {
		return ownerEmailAddress;
	}

	public void setOwnerEmailAddress(String ownerEmailAddress) {
		this.ownerEmailAddress = ownerEmailAddress;
	}
	
	
}
