package mat.DTO;


import mat.client.codelist.HasListBox;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * DTO for UNIT object
 *
 */
public class UnitDTO implements IsSerializable, HasListBox {
	private String id;
	private String unit;
	private int sortOrder;
	
	public UnitDTO(){
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	public String getValue() {
		return id;
	}
	public String getItem() {
		// TODO Auto-generated method stub
		return unit;
	}
	
	public void setItem(String unit) {
		this.unit = unit;
	}
	
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	public int getSortOrder() {
		return sortOrder;
	}
	
}
