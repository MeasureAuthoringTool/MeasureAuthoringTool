package mat.shared;

/**
 * The Class Attribute.
 */
public class Attribute implements Cloneable {
	
	/** The attribute. */
	private String attribute;
	
	/** The type. */
	private String type;
	
	/** The comparison operator. */
	private String comparisonOperator;	
	
	/** The quantity. */
	private String quantity;
	
	/** The unit. */
	private String unit;
	
	/** The term. */
	private String term;

	/**
	 * Instantiates a new attribute.
	 * 
	 * @param attribute
	 *            the attribute
	 * @param type
	 *            the type
	 * @param comparisonOperator
	 *            the comparison operator
	 * @param quantity
	 *            the quantity
	 * @param unit
	 *            the unit
	 * @param term
	 *            the term
	 */
	public Attribute(String attribute, String type, String comparisonOperator, String quantity, String unit, String term) {
		this.attribute = attribute;
		this.type = type;
		this.comparisonOperator = comparisonOperator;
		this.quantity = quantity;
		this.unit = unit;
		this.term = term;
	}

	/**
	 * Instantiates a new attribute.
	 */
	public Attribute() {
	}

	/**
	 * Instantiates a new attribute.
	 * 
	 * @param currentAttribute
	 *            the current attribute
	 * @param currentType
	 *            the current type
	 */
	public Attribute(String currentAttribute, String currentType) {
		this(currentAttribute, currentType, "", "", "", "");
	}

	/**
	 * Instantiates a new attribute.
	 * 
	 * @param currentAttribute
	 *            the current attribute
	 * @param currentType
	 *            the current type
	 * @param currentTerm
	 *            the current term
	 */
	public Attribute(String currentAttribute, String currentType, String currentTerm) {
		this(currentAttribute, currentType, "", "", "", currentTerm);
	}
	
	/**
	 * Gets the attribute.
	 * 
	 * @return the attribute
	 */
	public String getAttribute() {
		return attribute;	
	}

	/**
	 * Sets the attribute.
	 * 
	 * @param attribute
	 *            the new attribute
	 */
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	
	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the term.
	 * 
	 * @return the term
	 */
	public String getTerm() {
		return term;
	}

	/**
	 * Sets the term.
	 * 
	 * @param term
	 *            the new term
	 */
	public void setTerm(String term) {
		this.term = term;
	}
	
	/**
	 * Gets the comparison operator.
	 * 
	 * @return the comparison operator
	 */
	public String getComparisonOperator() {
		return comparisonOperator;
	}

	/**
	 * Sets the comparison operator.
	 * 
	 * @param comparisonOperator
	 *            the new comparison operator
	 */
	public void setComparisonOperator(String comparisonOperator) {
		this.comparisonOperator = comparisonOperator;
	}

	/**
	 * Gets the quantity.
	 * 
	 * @return the quantity
	 */
	public String getQuantity() {
		return quantity;
	}

	/**
	 * Sets the quantity.
	 * 
	 * @param quantity
	 *            the new quantity
	 */
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	/**
	 * Gets the unit.
	 * 
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * Sets the unit.
	 * 
	 * @param unit
	 *            the new unit
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
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
