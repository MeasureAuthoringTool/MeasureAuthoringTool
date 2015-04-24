package mat.reportmodel;



public class SecurityRole {
	
	private static final long serialVersionUID = 1L;
	public static final String ADMIN_ROLE = "Administrator";
	public static final String USER_ROLE = "User";
	public static final String SUPER_USER_ROLE = "Super user";
	public static final String ADMIN_ROLE_ID = "1";
	public static final String USER_ROLE_ID = "2";
	public static final String SUPER_USER_ROLE_ID = "3";
	
	private String id;
	private String description;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
