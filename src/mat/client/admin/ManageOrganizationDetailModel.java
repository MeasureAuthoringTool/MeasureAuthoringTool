package mat.client.admin;

import com.google.gwt.user.client.rpc.IsSerializable;

/** The Class ManageUsersDetailModel. */
public class ManageOrganizationDetailModel implements IsSerializable {
	
	/** The is existing Organization. */
	private boolean isExistingOrg;
	
	/** The oid. */
	private String oid;
	
	/** The organization. */
	private String organization;
	
	/** Do trim.
	 * 
	 * @param str the str
	 * @return the string */
	private String doTrim(String str) {
		if (str == null) {
			return str;
		} else {
			return str.trim();
		}
	}
	
	/** Gets the oid.
	 * 
	 * @return the oid */
	public String getOid() {
		return oid;
	}
	
	/** Gets the organization.
	 * 
	 * @return the organization */
	public String getOrganization() {
		return organization;
	}
	
	
	/** Sets the oid.
	 * 
	 * @param oid the new oid */
	public void setOid(String oid) {
		this.oid = doTrim(oid);
	}
	
	/** Sets the organization.
	 * 
	 * @param organization the new organization */
	public void setOrganization(String organization) {
		this.organization = doTrim(organization);
	}

	/** Checks if is existing org.
	 * 
	 * @return the isExistingOrg */
	public boolean isExistingOrg() {
		return isExistingOrg;
	}

	/** Sets the existing org.
	 * 
	 * @param isExistingOrg the isExistingOrg to set */
	public void setExistingOrg(boolean isExistingOrg) {
		this.isExistingOrg = isExistingOrg;
	}
	
}
