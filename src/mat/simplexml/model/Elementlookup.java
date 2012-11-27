package mat.simplexml.model;

import java.util.ArrayList;
import java.util.List;

public class Elementlookup {
	private List<Qdsel> qdsels;
	private List<Iqdsel> iqdsels;
	private List<Measureel> measureels;
	private List<Propel> propels;
	private String  ttext;
	private List<Function> measurecalcs;

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
	public List<Propel> getPropels() {
		if(propels == null)
			propels = new ArrayList<Propel>();
		return propels;
	}
	public void setPropels(List<Propel> propels) {
		this.propels = propels;
	}
	public String getTtext() {
		return ttext;
	}
	public void setTtext (String ttext ) {
		this.ttext = ttext;
	}
	public void setMeasurecalcs(List<Function> measurecalcs) {
		this.measurecalcs = measurecalcs;
	}
	public List<Function> getMeasurecalcs() {
		return measurecalcs;
	}
	public List<Iqdsel> getIqdsels() {
		return iqdsels;
	}
	public void setIqdsels(List<Iqdsel> iqdsels) {
		this.iqdsels = iqdsels;
	}
	
	public void addPropelsSansDups(List<Propel> ps){
		List<Propel> addList = new ArrayList<Propel>();
		for(Propel p : ps){
			if(!hasPropel(p))
				addList.add(p);
		}
		getPropels().addAll(addList);
		addList.clear();
		addList = null;
	}
	
	private boolean hasPropel(Propel p1){
		for(Propel p2 : getPropels())
			if(p2.getId().equalsIgnoreCase(p1.getId()))
				return true;
		return false;
	}
}