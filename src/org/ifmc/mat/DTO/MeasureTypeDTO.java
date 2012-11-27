package org.ifmc.mat.DTO;


import org.ifmc.mat.client.codelist.HasListBox;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MeasureTypeDTO implements IsSerializable, HasListBox {
	private String id;
	private String Name;
	
	public MeasureTypeDTO(){
		
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
		return Name;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	@Override
	public int getSortOrder() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
