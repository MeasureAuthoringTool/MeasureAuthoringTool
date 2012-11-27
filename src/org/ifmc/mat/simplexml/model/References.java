package org.ifmc.mat.simplexml.model;

import java.util.ArrayList;
import java.util.List;

public class References {
	private String  ttext;
	private List<Reference> references;

	public String getTtext() {
		return ttext;
	}
	public void setTtext (String ttext ) {
		this.ttext = ttext;
	}
	public List<Reference> getReferences() {
		if(references == null)
			references = new ArrayList<Reference>();
		return references;
	}
	public void setReferences(List<Reference> references) {
		this.references = references;
	}
	
}