package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * The Class SecurityRole.
 */
public class SecurityRole implements IsSerializable {
	
	/** The Constant ADMIN_ROLE. */
	public static final String ADMIN_ROLE = "Administrator";
	
	/** The Constant USER_ROLE. */
	public static final String USER_ROLE = "User";
	
	/** The Constant SUPER_USER_ROLE. */
	public static final String SUPER_USER_ROLE = "Top Level User";
	
	/** The Constant ADMIN_ROLE_ID. */
	public static final String ADMIN_ROLE_ID = "1";
	
	/** The Constant USER_ROLE_ID. */
	public static final String USER_ROLE_ID = "2";
	
	/** The Constant SUPER_USER_ROLE_ID. */
	public static final String SUPER_USER_ROLE_ID = "3";
	
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
