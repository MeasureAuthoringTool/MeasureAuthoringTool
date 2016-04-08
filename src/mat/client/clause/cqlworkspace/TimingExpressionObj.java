package mat.client.clause.cqlworkspace;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class TimingExpressionObj.
 */
public class TimingExpressionObj {

	/** The name. */
	String name;

	/** The option list. */
	List<String> optionList;

	/** The is quantity. */
	boolean isQuantity;

	/** The is units. */
	boolean isUnits;

	/** The is date time precesion. */
	boolean isDateTimePrecesion;
	
	String dateTimePrecOffset;

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Checks if is quantity.
	 * 
	 * @return true, if is quantity
	 */
	public boolean isQuantity() {
		return isQuantity;
	}

	/**
	 * Sets the quantity.
	 * 
	 * @param isQuantity
	 *            the new quantity
	 */
	public void setQuantity(boolean isQuantity) {
		this.isQuantity = isQuantity;
	}

	/**
	 * Checks if is units.
	 * 
	 * @return true, if is units
	 */
	public boolean isUnits() {
		return isUnits;
	}

	/**
	 * Sets the units.
	 * 
	 * @param isUnits
	 *            the new units
	 */
	public void setUnits(boolean isUnits) {
		this.isUnits = isUnits;
	}

	/**
	 * Checks if is date time precesion.
	 * 
	 * @return true, if is date time precesion
	 */
	public boolean isDateTimePrecesion() {
		return isDateTimePrecesion;
	}

	/**
	 * Sets the date time precesion.
	 * 
	 * @param isDateTimePrecesion
	 *            the new date time precesion
	 */
	public void setDateTimePrecesion(boolean isDateTimePrecesion) {
		this.isDateTimePrecesion = isDateTimePrecesion;
	}

	/**
	 * Gets the option list.
	 * 
	 * @return the option list
	 */
	public List<String> getOptionList() {
		return optionList;
	}

	/**
	 * Sets the option list.
	 * 
	 * @param optionList
	 *            the new option list
	 */
	public void setOptionList(List<String> optionList) {
		this.optionList = optionList;
	}

	public String getDateTimePrecOffset() {
		return dateTimePrecOffset;
	}

	public void setDateTimePrecOffset(String dateTimePrecOffset) {
		this.dateTimePrecOffset = dateTimePrecOffset;
	}

}
