package mat.model.clause;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SHARE_LEVEL")
public class ShareLevel {
	public static final String VIEW_ONLY_ID = "1";
	public static final String MODIFY_ID = "2";
	private String id;
	private String description;
	
	@Id
	@Column(name = "SHARE_LEVEL_ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "DESCRIPTION", length = 50)
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}
