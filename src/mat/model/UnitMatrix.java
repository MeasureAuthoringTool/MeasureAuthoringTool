package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class UnitMatrix.
 */
public class UnitMatrix implements IsSerializable{
	
	/** The id. */
	private String id;
	
	/** The unit type id. */
	private String unitTypeId;
	
	/** The unit id. */
	private String unitId;
	
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
	 * Gets the unit type id.
	 * 
	 * @return the unit type id
	 */
	public String getUnitTypeId() {
		return unitTypeId;
	}
	
	/**
	 * Sets the unit type id.
	 * 
	 * @param unitTypeId
	 *            the new unit type id
	 */
	public void setUnitTypeId(String unitTypeId) {
		this.unitTypeId = unitTypeId;
	}
	
	/**
	 * Gets the unit id.
	 * 
	 * @return the unit id
	 */
	public String getUnitId() {
		return unitId;
	}
	
	/**
	 * Sets the unit id.
	 * 
	 * @param unitId
	 *            the new unit id
	 */
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
}
