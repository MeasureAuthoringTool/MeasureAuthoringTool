package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class CodeListSearchDTO.
 */
public class CodeListSearchDTO implements IsSerializable {

	/** The name. */
	private String name;
	
	/** The id. */
	private String id;
	
	/** The steward. */
	private String steward;
	
	/** The oid. */
	private String oid;
	
	/** The code system. */
	private String codeSystem;
	
	/** The category code. */
	private String categoryCode;
	
	/** The category display. */
	private String categoryDisplay;
	
	/** The abbreviated category. */
	private String abbreviatedCategory;
	
	/** The abbreviated code system. */
	private String abbreviatedCodeSystem;
	
	/** The last modified. */
	private String lastModified;
	
	/** The is grouped code list. */
	private boolean isGroupedCodeList;
	
	/** The is transferable. */
	private boolean isTransferable;
	
	/** The steward others. */
	private String stewardOthers;
	
	/** The owner first name. */
	private String ownerFirstName;
	
	/** The owner last name. */
	private String ownerLastName;
	
	/** The owner email address. */
	private String ownerEmailAddress;
	
	/** The mat.vsac xml payload. */
	private String vsacXMLPayload;
	
	/**
	 * Gets the mat.vsac xml payload.
	 * 
	 * @return the mat.vsac xml payload
	 */
	public String getVsacXMLPayload() {
		return vsacXMLPayload;
	}

	/**
	 * Sets the mat.vsac xml payload.
	 * 
	 * @param vsacXMLPayload
	 *            the new mat.vsac xml payload
	 */
	public void setVsacXMLPayload(String vsacXMLPayload) {
		this.vsacXMLPayload = vsacXMLPayload;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name + ": " + codeSystem + "-" + getOid() ;
}
	
	/**
	 * Gets the steward others.
	 * 
	 * @return the steward others
	 */
	public String getStewardOthers() {
		return stewardOthers;
	}

	/**
	 * Sets the steward others.
	 * 
	 * @param stewardOthers
	 *            the new steward others
	 */
	public void setStewardOthers(String stewardOthers) {
		this.stewardOthers = stewardOthers;
	}

	/**
	 * Checks if is draft.
	 * 
	 * @return true, if is draft
	 */
	public boolean isDraft(){
		if(lastModified.equalsIgnoreCase("Draft"))
				return true;
		return false;
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
		this.name = name;
	}
	
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {
		this.id = id;
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
		this.steward = steward;
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
		this.oid = oid;
	}
	
	/**
	 * Gets the category code.
	 * 
	 * @return the category code
	 */
	public String getCategoryCode() {
		return categoryCode;
	}
	
	/**
	 * Sets the category code.
	 * 
	 * @param categoryCode
	 *            the new category code
	 */
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	
	/**
	 * Gets the category display.
	 * 
	 * @return the category display
	 */
	public String getCategoryDisplay() {
		return categoryDisplay;
	}
	
	/**
	 * Sets the category display.
	 * 
	 * @param categoryDisplay
	 *            the new category display
	 */
	public void setCategoryDisplay(String categoryDisplay) {
		this.categoryDisplay = categoryDisplay;
	}
	
	/**
	 * Checks if is grouped code list.
	 * 
	 * @return true, if is grouped code list
	 */
	public boolean isGroupedCodeList() {
		return isGroupedCodeList;
	}
	
	/**
	 * Sets the grouped code list.
	 * 
	 * @param isGroupedCodeList
	 *            the new grouped code list
	 */
	public void setGroupedCodeList(boolean isGroupedCodeList) {
		this.isGroupedCodeList= isGroupedCodeList;
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
		this.codeSystem = codeSystem;
	}
	
	/**
	 * Gets the abbreviated category.
	 * 
	 * @return the abbreviated category
	 */
	public String getAbbreviatedCategory() {
		return abbreviatedCategory;
	}
	
	/**
	 * Sets the abbreviated category.
	 * 
	 * @param abbreviatedCategory
	 *            the new abbreviated category
	 */
	public void setAbbreviatedCategory(String abbreviatedCategory) {
		this.abbreviatedCategory = abbreviatedCategory;
	}
	
	/**
	 * Gets the abbreviated code system.
	 * 
	 * @return the abbreviated code system
	 */
	public String getAbbreviatedCodeSystem() {
		return abbreviatedCodeSystem;
	}
	
	/**
	 * Sets the abbreviated code system.
	 * 
	 * @param abbreviatedCodeSystem
	 *            the new abbreviated code system
	 */
	public void setAbbreviatedCodeSystem(String abbreviatedCodeSystem) {
		this.abbreviatedCodeSystem = abbreviatedCodeSystem;
	}
	
	/**
	 * Gets the last modified.
	 * 
	 * @return the last modified
	 */
	public String getLastModified() {
		return lastModified;
	}
	
	/**
	 * Sets the last modified.
	 * 
	 * @param lastModified
	 *            the new last modified
	 */
	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * Sets the transferable.
	 * 
	 * @param isTransferable
	 *            the new transferable
	 */
	public void setTransferable(boolean isTransferable) {
		this.isTransferable = isTransferable;
	}

	/**
	 * Checks if is transferable.
	 * 
	 * @return true, if is transferable
	 */
	public boolean isTransferable() {
		return isTransferable;
	}
	
	/**
	 * Gets the owner first name.
	 * 
	 * @return the owner first name
	 */
	public String getOwnerFirstName() {
		return ownerFirstName;
	}

	/**
	 * Sets the owner first name.
	 * 
	 * @param ownerFirstName
	 *            the new owner first name
	 */
	public void setOwnerFirstName(String ownerFirstName) {
		this.ownerFirstName = ownerFirstName;
	}

	/**
	 * Gets the owner last name.
	 * 
	 * @return the owner last name
	 */
	public String getOwnerLastName() {
		return ownerLastName;
	}

	/**
	 * Sets the owner last name.
	 * 
	 * @param ownerLastName
	 *            the new owner last name
	 */
	public void setOwnerLastName(String ownerLastName) {
		this.ownerLastName = ownerLastName;
	}

	/**
	 * Gets the owner email address.
	 * 
	 * @return the owner email address
	 */
	public String getOwnerEmailAddress() {
		return ownerEmailAddress;
	}

	/**
	 * Sets the owner email address.
	 * 
	 * @param ownerEmailAddress
	 *            the new owner email address
	 */
	public void setOwnerEmailAddress(String ownerEmailAddress) {
		this.ownerEmailAddress = ownerEmailAddress;
	}
	
	
	
	
}
