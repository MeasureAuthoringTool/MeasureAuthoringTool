package mat.DTO;

import mat.client.codelist.HasListBox;

import com.google.gwt.user.client.rpc.IsSerializable;

public class OrganizationDTO implements IsSerializable, HasListBox{

	private String id;
	private String name;
	private String OID;
	
	@Override
	public int getSortOrder() {
		return 0;
	}

	@Override
	public String getValue() {
		return getId();
	}

	@Override
	public String getItem() {
		return getName();
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the oID
	 */
	public String getOID() {
		return OID;
	}

	/**
	 * @param oID the oID to set
	 */
	public void setOID(String oID) {
		OID = oID;
	}

}
