package vsac.model;

import java.util.ArrayList;

/**
 * This class holds list of MatValueSet.
 * **/
public class ValueSetWrapper {
	/**
	 * List of MatValueSet.
	 * **/
	private ArrayList<ValueSet> valueSetList;

	/**
	 * Getter Method.
	 * 
	 * @return valueSetList.
	 * **/
	public final ArrayList<ValueSet> getValueSetList() {
		return valueSetList;
	}

	/**
	 * Setter Method.
	 * 
	 * @param valueSet
	 *            - List of MatValueSet.
	 * **/
	public final void setValueSetList(final ArrayList<ValueSet> valueSet) {
		this.valueSetList = valueSet;
	}

}
