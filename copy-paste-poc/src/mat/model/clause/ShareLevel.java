package mat.model.clause;


/**
 * The Class ShareLevel.
 */
public class ShareLevel {
	
	/** The Constant VIEW_ONLY_ID. */
	public static final String VIEW_ONLY_ID = "1";
	
	/** The Constant MODIFY_ID. */
	public static final String MODIFY_ID = "2";
	
	/** The id. */
	private String id;
	
	/** The description. */
	private String description;
	
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
}
