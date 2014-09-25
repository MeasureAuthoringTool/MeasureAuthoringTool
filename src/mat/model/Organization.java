package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Model class representing the Organization table in the database.
 * Referred to in the Organization.hbm.xml file.
 * @author cbajikar
 *
 */

public class Organization implements IsSerializable{
	
	/** The id. */
	private Long id;
	
	/** The organization name. */
	private String organizationName;
	
	/** The organization oid. */
	private String organizationOID;
	
	
	/** Sets the id.
	 * 
	 * @param id the new id */
	public void setId(Long id) {
		this.id = id;
	}
	
	/** Gets the id.
	 * 
	 * @return the id */
	public Long getId() {
		return id;
	}
	
	/** Sets the organization name.
	 * 
	 * @param organizationName the new organization name */
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	
	/** Gets the organization name.
	 * 
	 * @return the organization name */
	public String getOrganizationName() {
		return organizationName;
	}
	
	/** Sets the organization oid.
	 * 
	 * @param organizationOID the new organization oid */
	public void setOrganizationOID(String organizationOID) {
		this.organizationOID = organizationOID;
	}
	
	/** Gets the organization oid.
	 * 
	 * @return the organization oid */
	public String getOrganizationOID() {
		return organizationOID;
	}
}
