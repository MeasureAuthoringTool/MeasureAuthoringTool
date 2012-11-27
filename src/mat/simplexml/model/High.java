package mat.simplexml.model;


public class High {
	private String value;
	private String unit;
	private String inclusive;

	public void diagram(PrettyPrinter pp) {
		pp.concat("HIGH");
		pp.incrementIndentation();
		pp.concat("NUM", getValue());
		if(getUnit() != null && !getUnit().trim().equals("")){
			pp.concat("UNIT", getUnit());
		}
		pp.concat("INCLUSIVE", getInclusive());
		pp.decrementIndentation();
	}
	
	public High(String num, String unit, boolean inclusive) {
		this.value = num;
		if(unit!= null &&!unit.trim().equals("")){
			this.unit = unit;
		}
		this.inclusive = (inclusive == true) ? "true" : "false";
	}
	
	public High(String quantity, String unit) {
		this.value = quantity;
		if(unit!= null &&!unit.trim().equals("")){
			this.unit = unit;
		}
		this.inclusive = "true";
	}

	public String getInclusive() {
		return inclusive;
	}
	public void setInclusive (String inclusive ) {
		this.inclusive = inclusive;
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