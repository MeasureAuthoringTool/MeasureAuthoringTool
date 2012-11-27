package org.ifmc.mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Unit implements IsSerializable{
	private String id;
	private String unit;
	private int sortOrder;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
