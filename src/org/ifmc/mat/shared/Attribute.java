package org.ifmc.mat.shared;

public class Attribute {
	private String attribute;
	private String type;
	private String comparisonOperator;	
	private String quantity;
	private String unit;
	private String term;

	public Attribute(String attribute, String type, String comparisonOperator, String quantity, String unit, String term) {
		this.attribute = attribute;
		this.type = type;
		this.comparisonOperator = comparisonOperator;
		this.quantity = quantity;
		this.unit = unit;
		this.term = term;
	}

	public Attribute() {
	}

	public Attribute(String currentAttribute, String currentType) {
		this(currentAttribute, currentType, "", "", "", "");
	}

	public Attribute(String currentAttribute, String currentType, String currentTerm) {
		this(currentAttribute, currentType, "", "", "", currentTerm);
	}
	
	public String getAttribute() {
		return attribute;	
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}
	
	public String getComparisonOperator() {
		return comparisonOperator;
	}

	public void setComparisonOperator(String comparisonOperator) {
		this.comparisonOperator = comparisonOperator;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public Attribute clone() {
		Attribute dest = new Attribute();
		
		dest.attribute = attribute;
		dest.type = type;
		dest.comparisonOperator = comparisonOperator;	
		dest.quantity = quantity;
		dest. unit = unit;
		dest.term = term;		
		
		return dest;
	}
}
