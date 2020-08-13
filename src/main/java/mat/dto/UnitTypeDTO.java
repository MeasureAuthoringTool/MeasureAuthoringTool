package mat.dto;


import com.google.gwt.user.client.rpc.IsSerializable;
import mat.client.codelist.HasListBox;

/**
 * DTO for unit type object.
 */
public class UnitTypeDTO implements IsSerializable, HasListBox {
	
	/** The id. */
	private String id;
	
	/** The unit type. */
	private String unitType;
	
	/**
	 * Instantiates a new unit type dto.
	 */
	public UnitTypeDTO(){
		
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
		return unitType;
	}
	
	/**
	 * Sets the item.
	 * 
	 * @param unitType
	 *            the new item
	 */
	public void setItem(String unitType) {
		this.unitType = unitType;
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
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.HasListBox#getSortOrder()
	 */
	@Override
	public int getSortOrder() {
		// TODO Auto-generated method stub
		return 0;
	}
}
