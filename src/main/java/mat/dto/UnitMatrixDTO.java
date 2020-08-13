package mat.dto;


import com.google.gwt.user.client.rpc.IsSerializable;
import mat.client.codelist.HasListBox;

/**
 * DTO for UNIT MATRIX object.
 */
public class UnitMatrixDTO implements IsSerializable, HasListBox {
	
	/** The id. */
	private String id;
	
	/** The unit id. */
	private String unitId;
	
	/**
	 * Instantiates a new unit matrix dto.
	 */
	public UnitMatrixDTO(){
		
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
		return unitId;
	}
	
	/**
	 * Sets the item.
	 * 
	 * @param unitId
	 *            the new item
	 */
	public void setItem(String unitId) {
		this.unitId = unitId;
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
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.HasListBox#getSortOrder()
	 */
	@Override
	public int getSortOrder() {
		// TODO Auto-generated method stub
		return 0;
	}
}
