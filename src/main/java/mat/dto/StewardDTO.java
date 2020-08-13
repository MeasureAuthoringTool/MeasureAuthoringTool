package mat.dto;


import com.google.gwt.user.client.rpc.IsSerializable;
import mat.client.codelist.HasListBox;

/**
 * The Class StewardDTO.
 */
public class StewardDTO implements IsSerializable, HasListBox {
	
	/** The id. */
	private String id;
	
	/** The org name. */
	private String orgName;
	
	/**
	 * Instantiates a new steward dto.
	 */
	public StewardDTO(){
		
	}
	
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
	
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.HasListBox#getValue()
	 */
	public String getValue() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.HasListBox#getItem()
	 */
	public String getItem() {
		// TODO Auto-generated method stub
		return orgName;
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
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.HasListBox#getSortOrder()
	 */
	@Override
	public int getSortOrder() {
		// TODO Auto-generated method stub
		return 0;
	}
}
