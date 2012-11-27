package org.ifmc.mat.simplexml.model;


public class Low {
	protected String value;
	protected String unit;
	protected String inclusive;
	
	public void diagram(PrettyPrinter pp) {
		pp.concat("LOW");
		pp.incrementIndentation();
		pp.concat("NUM",getValue());
		pp.concat("UNIT",getUnit());
		pp.concat("INCLUSIVE",getInclusive());
		pp.decrementIndentation();
	}
	
	public Low(String value, String unit, boolean inclusive) {
		this.value = value;
		if(unit!= null &&!unit.trim().equals("")){
			this.unit = unit;
		}
		this.inclusive = (inclusive == true) ? "true" : "false";
	}
	
	public Low(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String num) {
		this.value = num;
	}
	
	public String getUnit() {
		return unit;
	}
	
	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getInclusive() {
		return inclusive;
	}

	public void setInclusive(String inclusive) {
		this.inclusive = inclusive;
	}
	
}
