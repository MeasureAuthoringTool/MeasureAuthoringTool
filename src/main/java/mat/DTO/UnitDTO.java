package mat.DTO;


import mat.client.codelist.HasListBox;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * DTO for UNIT object.
 */
public class UnitDTO implements IsSerializable, HasListBox {
	
	/** The id. */
	private String id;
	
	/** The unit. */
	private String unit;
	
	/** the cql unit **/
	private String cqlunit;
	
	/** The sort order. */
	private int sortOrder;
	
	/**
	 * Instantiates a new unit dto.
	 */
	public UnitDTO(){
		
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
		return unit;
	}
	
	/**
	 * Sets the item.
	 * 
	 * @param unit
	 *            the new item
	 */
	public void setItem(String unit) {
		this.unit = unit;
	}
	
	/**
	 * Gets the unit.
	 * 
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}
	
	/**
	 * Sets the unit.
	 * 
	 * @param unit
	 *            the new unit
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	/**
	 * @return the cqlunit
	 */
	public String getCqlunit() {
		return cqlunit;
	}

	/**
	 * @param cqlunit the cqlunit to set
	 */
	public void setCqlunit(String cqlunit) {
		this.cqlunit = cqlunit;
	}

	/**
	 * Sets the sort order.
	 * 
	 * @param sortOrder
	 *            the new sort order
	 */
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.HasListBox#getSortOrder()
	 */
	public int getSortOrder() {
		return sortOrder;
	}
	
}
