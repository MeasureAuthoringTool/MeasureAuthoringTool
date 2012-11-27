package org.ifmc.mat.DTO;

import org.ifmc.mat.client.codelist.HasListBox;

import com.google.gwt.user.client.rpc.IsSerializable;

public class HasListBoxDTO implements IsSerializable, HasListBox {
   
	private String id;
	private String description;
	private String oid;
	
	public HasListBoxDTO(){
	
	}
	
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
	public String getValue() {
		return id;
	}
	public String getItem() {
		return description;
	}
	public void setOid(String oid){
		this.oid = oid;
	}
	public String getTitle(){
		return oid;
	}

	@Override
	public int getSortOrder() {
		// TODO Auto-generated method stub
		return 0;
	}
}
