package org.ifmc.mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UnitType implements IsSerializable{
	private String id;
	private String unitType;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
}
