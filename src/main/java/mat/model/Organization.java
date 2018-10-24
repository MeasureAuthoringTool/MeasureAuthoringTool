package mat.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.google.gwt.user.client.rpc.IsSerializable;

@Entity
@Table(name = "ORGANIZATION", uniqueConstraints = @UniqueConstraint(columnNames = "ORG_OID"))
public class Organization implements IsSerializable{
	private Long id;
	private String organizationName;
	private String organizationOID;
	
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Id
	@GeneratedValue
	@Column(name = "ORG_ID", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	
	@Column(name = "ORG_NAME", length = 150)
	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationOID(String organizationOID) {
		this.organizationOID = organizationOID;
	}

	@Column(name = "ORG_OID", unique = true, nullable = false, length = 50)
	public String getOrganizationOID() {
		return organizationOID;
	}
}
