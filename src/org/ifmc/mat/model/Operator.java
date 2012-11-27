package org.ifmc.mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Operator implements IsSerializable{
	private String id;
	private String longName;
	private String shortName;
	private OperatorType operatorType;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLongName() {
		return longName;
	}
	public void setLongName(String longName) {
		this.longName = longName;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public OperatorType getOperatorType() {
		return operatorType;
	}
	public void setOperatorType(OperatorType operatorType) {
		this.operatorType = operatorType;
	}
	
}
