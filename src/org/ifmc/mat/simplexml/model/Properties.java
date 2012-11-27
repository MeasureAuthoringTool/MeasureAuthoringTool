package org.ifmc.mat.simplexml.model;

import java.util.ArrayList;
import java.util.List;

public class Properties {

	private List<Property> listOfProperties =null;

	public void setProperties(List<Property> listOfProperties) {
		this.listOfProperties = listOfProperties;
	}

	public List<Property> getProperties() {
		return listOfProperties;
	}

	public void addProperty(Property p) {
		if (listOfProperties == null) {
			listOfProperties = new ArrayList<Property>();
		}
		listOfProperties.add(p);
	}
	
}
