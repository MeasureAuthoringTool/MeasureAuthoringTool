package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "SECURITY_ROLE")
public class SecurityRole implements IsSerializable,Serializable {
	
	private static final long serialVersionUID = 7440804941395081940L;

	public static final String ADMIN_ROLE = "Administrator";
	
	public static final String USER_ROLE = "User";
	
	public static final String SUPER_USER_ROLE = "Top Level User";
	
	public static final String ADMIN_ROLE_ID = "1";
	
	public static final String USER_ROLE_ID = "2";
	
	public static final String SUPER_USER_ROLE_ID = "3";
	
	private String id;
	
	private String description;
	

	public SecurityRole() {
		super();
	}

	@Id
	@Column(name = "SECURITY_ROLE_ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "DESCRIPTION", nullable = false, length = 50)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
