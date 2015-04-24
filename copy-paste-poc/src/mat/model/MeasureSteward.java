package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class MeasureSteward.
 */
public class MeasureSteward implements IsSerializable{
	
	/** The id. */
	private String id;
	
	/** The org name. */
	private String orgName;
	
	/** The org oid. */
	private String orgOid;
	
	
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
	 * Gets the org name.
	 * 
	 * @return the org name
	 */
	public String getOrgName() {
		return orgName;
	}
	
	/**
	 * Sets the org name.
	 * 
	 * @param orgName
	 *            the new org name
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	/**
	 * Gets the org oid.
	 * 
	 * @return the org oid
	 */
	public String getOrgOid() {
		return orgOid;
	}
	
	/**
	 * Sets the org oid.
	 * 
	 * @param orgOid
	 *            the new org oid
	 */
	public void setOrgOid(String orgOid) {
		this.orgOid = orgOid;
	}
	

}
