package mat.simplexml.model;

import java.util.ArrayList;
import java.util.List;

public class Abbreviations {
	private List<Qdsel> qdsels;
	private List<Measureel> measureels;
	private String  ttext;

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
	public List<Measureel> getMeasureels() {
		if(measureels == null)
			measureels = new ArrayList<Measureel>();
		return measureels;
	}
	public void setMeasureels(List<Measureel> measureels) {
		this.measureels = measureels;
	}
	public String getTtext() {
		return ttext;
	}
	public void setTtext (String ttext ) {
		this.ttext = ttext;
	}
}