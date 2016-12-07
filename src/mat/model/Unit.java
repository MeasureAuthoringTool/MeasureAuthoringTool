package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class Unit.
 */
public class Unit implements IsSerializable{
	
	/** The id. */
	private String id;
	
	/** The unit. */
	private String unit;
	
	/** the cql unit **/
	private String cqlunit;
	
	/** The sort order. */
	private int sortOrder;
	
	
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
	
	/**
	 * Gets the sort order.
	 * 
	 * @return the sort order
	 */
	public int getSortOrder() {
		return sortOrder;
	}
}
