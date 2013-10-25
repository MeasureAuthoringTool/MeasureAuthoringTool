package mat.model;

/**
 * The Class GroupedCodeList.
 */
public class GroupedCodeList implements Cloneable {
	
	/** The id. */
	private String id;
	
	/** The description. */
	private String description;
	
	/** The code list. */
	private CodeList codeList;
	
	
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
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the description.
	 * 
	 * @param description
	 *            the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Gets the code list.
	 * 
	 * @return the code list
	 */
	public CodeList getCodeList() {
		return codeList;
	}
	
	/**
	 * Sets the code list.
	 * 
	 * @param codeList
	 *            the new code list
	 */
	public void setCodeList(CodeList codeList) {
		this.codeList = codeList;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public GroupedCodeList clone(){
		GroupedCodeList clone = new GroupedCodeList();
		clone.setCodeList(this.getCodeList());
		clone.setDescription(this.getDescription());
		return clone;
	}
	
}
