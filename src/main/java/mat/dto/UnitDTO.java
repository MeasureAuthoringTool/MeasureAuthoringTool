package mat.dto;


import com.google.gwt.user.client.rpc.IsSerializable;

import mat.client.codelist.HasListBox;

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
	
	public UnitDTO(String id, String unit, String cqlunit, int sortOrder) {
		super();
		this.id = id;
		this.unit = unit;
		this.cqlunit = cqlunit;
		this.sortOrder = sortOrder;
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
	@Override
	public String getValue() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.HasListBox#getItem()
	 */
	@Override
	public String getItem() {
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
	@Override
	public int getSortOrder() {
		return sortOrder;
	}
	
}
