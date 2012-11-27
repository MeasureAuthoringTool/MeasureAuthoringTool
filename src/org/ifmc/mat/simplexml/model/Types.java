package org.ifmc.mat.simplexml.model;

import java.util.ArrayList;
import java.util.List;



public class Types {
	private String  ttext;
	private List<Type> types;

	public String getTtext() {
		return ttext;
	}
	public void setTtext (String ttext ) {
		this.ttext = ttext;
	}
	public List<Type> getTypes() {
		if(types == null)
			types = new ArrayList<Type>();
		return types;
	}
	public void setTypes(List<Type> types) {
		this.types = types;
	}
	
}