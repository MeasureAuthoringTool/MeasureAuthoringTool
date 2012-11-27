package org.ifmc.mat.DTO;


import org.ifmc.mat.client.codelist.HasListBox;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AuthorDTO implements IsSerializable, HasListBox {

	private String id;
	private String authorName;
	
	public AuthorDTO(){
		
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
		return authorName;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	@Override
	public int getSortOrder() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
