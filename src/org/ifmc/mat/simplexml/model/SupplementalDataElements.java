package org.ifmc.mat.simplexml.model;

import java.util.ArrayList;
import java.util.List;

public class SupplementalDataElements {
	private List<Qdsel> qdsels;
	private List<Iqdsel> iqdsels;
	

	public List<Qdsel> getQdsels() {
		if(qdsels == null)
			qdsels = new ArrayList<Qdsel>();
		return qdsels;
	}
	public void setQdsels(List<Qdsel> qdsels) {
		if(qdsels == null)
			qdsels = new ArrayList<Qdsel>();
		this.qdsels = qdsels;
	}
	public List<Iqdsel> getIqdsels() {
		return iqdsels;
	}
	public void setIqdsels(List<Iqdsel> iqdsels) {
		this.iqdsels = iqdsels;
	}
}