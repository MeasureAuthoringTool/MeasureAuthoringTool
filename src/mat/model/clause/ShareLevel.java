package mat.model.clause;


public class ShareLevel {
	public static final String VIEW_ONLY_ID = "1";
	public static final String MODIFY_ID = "2";
	
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
