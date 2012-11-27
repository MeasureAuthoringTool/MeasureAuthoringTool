package org.ifmc.mat.DTO;

import org.ifmc.mat.client.codelist.HasListBox;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DataTypeDTO implements IsSerializable, HasListBox{
	private String id;
	private String description;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String getValue() {
		return id;
	}
	@Override
	public String getItem() {
		return description;
	}
	@Override
	public int getSortOrder() {
		// TODO Auto-generated method stub
		return 0;
	}
}
