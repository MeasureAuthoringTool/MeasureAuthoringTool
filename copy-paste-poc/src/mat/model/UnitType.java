package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class UnitType.
 */
public class UnitType implements IsSerializable{
	
	/** The id. */
	private String id;
	
	/** The unit type. */
	private String unitType;
	
	
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
	 * Gets the unit type.
	 * 
	 * @return the unit type
	 */
	public String getUnitType() {
		return unitType;
	}
	
	/**
	 * Sets the unit type.
	 * 
	 * @param unitType
	 *            the new unit type
	 */
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
}
