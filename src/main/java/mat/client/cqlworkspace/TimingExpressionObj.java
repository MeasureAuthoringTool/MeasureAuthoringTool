package mat.client.cqlworkspace;

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
	
	/** The is relative qualifier. */
	boolean isRelativeQualifier;
	
	/** The is quantity offset req. */
	boolean isQuantityOffsetReq;
	
	/** The date time prec offset. */
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

	/**
	 * Gets the date time prec offset.
	 *
	 * @return the date time prec offset
	 */
	public String getDateTimePrecOffset() {
		return dateTimePrecOffset;
	}

	/**
	 * Sets the date time prec offset.
	 *
	 * @param dateTimePrecOffset the new date time prec offset
	 */
	public void setDateTimePrecOffset(String dateTimePrecOffset) {
		this.dateTimePrecOffset = dateTimePrecOffset;
	}

	/**
	 * Checks if is relative qualifier.
	 *
	 * @return true, if is relative qualifier
	 */
	public boolean isRelativeQualifier() {
		return isRelativeQualifier;
	}

	/**
	 * Sets the relative qualifier.
	 *
	 * @param isRelativeQualifier the new relative qualifier
	 */
	public void setRelativeQualifier(boolean isRelativeQualifier) {
		this.isRelativeQualifier = isRelativeQualifier;
	}

	/**
	 * Checks if is quantity offset req.
	 *
	 * @return true, if is quantity offset req
	 */
	public boolean isQuantityOffsetReq() {
		return isQuantityOffsetReq;
	}

	/**
	 * Sets the quantity offset req.
	 *
	 * @param isQuantityOffsetReq the new quantity offset req
	 */
	public void setQuantityOffsetReq(boolean isQuantityOffsetReq) {
		this.isQuantityOffsetReq = isQuantityOffsetReq;
	}

}
