package org.ifmc.mat.simplexml.model;


public class Equal {
	private String value;
	private String unit;

	public void diagram(PrettyPrinter pp) {
		pp.concat("HIGH");
		pp.incrementIndentation();
		pp.concat("NUM", getValue());
		if(getUnit() != null && !getUnit().trim().equals("")){
			pp.concat("UNIT", getUnit());
		}
		pp.decrementIndentation();
	}
	
	public Equal(String num, String unit) {
		this.value = num;
		if(unit!= null &&!unit.trim().equals("")){
			this.unit = unit;
		}
	}
	
	public String getUnit() {
		return unit;
	}
	public void setUnit (String unit ) {
		this.unit = unit;
	}
	public String getValue() {
		return value;
	}
	public void setValue (String value ) {
		this.value = value;
	}
}