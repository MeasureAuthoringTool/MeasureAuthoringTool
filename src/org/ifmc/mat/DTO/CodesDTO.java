package org.ifmc.mat.DTO;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CodesDTO implements IsSerializable{

	private String id;
	private String code;
	private String description;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
