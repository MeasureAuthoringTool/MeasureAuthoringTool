package mat.model;

/**
 * Model class representing the Organization table in the database.
 * Referred to in the Organization.hbm.xml file.
 * @author cbajikar
 *
 */

public class Organization {
	
	private long id;
	private String organizationName;
	private String organizationOID;
	
	
	public void setId(long id) {
		this.id = id;
	}
	public long getId() {
		return id;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationOID(String organizationOID) {
		this.organizationOID = organizationOID;
	}
	public String getOrganizationOID() {
		return organizationOID;
	}
}
