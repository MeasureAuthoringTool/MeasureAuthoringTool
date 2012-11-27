package org.ifmc.mat.simplexml.model;

import java.util.ArrayList;
import java.util.List;


public class Authors {
	private List<Author> authors;
	private String  ttext;

	public String getTtext() {
		return ttext;
	}
	public void setTtext (String ttext ) {
		this.ttext = ttext;
	}
	public List<Author> getAuthors() {
		if(authors == null)
			authors = new ArrayList<Author>();
		return authors;
	}
	public void setAuthors(List<Author> authorList) {
		this.authors = authorList;
	}
	
}