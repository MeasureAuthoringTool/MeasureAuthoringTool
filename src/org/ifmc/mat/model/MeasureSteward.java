package org.ifmc.mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MeasureSteward implements IsSerializable{
	private String id;
	private String orgName;
	private String orgOid;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getOrgOid() {
		return orgOid;
	}
	public void setOrgOid(String orgOid) {
		this.orgOid = orgOid;
	}
	

}
