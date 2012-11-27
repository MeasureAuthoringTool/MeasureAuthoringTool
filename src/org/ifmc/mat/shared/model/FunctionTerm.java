package org.ifmc.mat.shared.model;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class FunctionTerm extends IQDSTerm {

	public String name;
	public Decision term;
	private List<Property>properties;
	
	public FunctionTerm() {
	}
	
	public FunctionTerm(String name, Decision term) {
		this.name = name;
		this.term = term;
	}
	
	public FunctionTerm(String name, String p, Decision term) {
		this.name = name;
		this.properties = new ArrayList<Property>();
		this.properties.add(new Property(p));
		this.term = term;
	}
	
	public Decision getTerm() {
		return term;
	}

	public void setTerm(Decision term) {
		this.term = term;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProperty() {
		if (properties == null)
			return null;
		return properties.get(0).getName();
	}
	
	public String getProperty(int i) {
		if (properties == null)
			return null;
		return (i >= 0 && i < properties.size()) 
		? properties.get(i).getName()
		: null;
	}	
	
	public List<Property> getProperties() {
		if (properties == null)
			properties = new ArrayList<Property>();
		return properties;
	}
	
	public void addProperty(String p) {
		getProperties().add(new Property(p));
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("FunctionTerm ").append(name);
		if (properties != null)
			sb.append(".").append(properties);
		sb.append("\n");
		if (term != null)
			sb.append(term.toString());		
		return sb.toString();
	}	
}
