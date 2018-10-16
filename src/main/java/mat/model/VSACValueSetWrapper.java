package mat.model;

import java.util.ArrayList;

/**
 * This class holds list of MatValueSet.
 * **/
public class VSACValueSetWrapper {
	/**
	 * List of MatValueSet.
	 * **/
	private ArrayList<MatValueSet> valueSetList;

	/**
	 * Getter Method.
	 * 
	 * @return valueSetList.
	 * **/
	public final ArrayList<MatValueSet> getValueSetList() {
		return valueSetList;
	}

	/**
	 * Setter Method.
	 * 
	 * @param valueSet
	 *            - List of MatValueSet.
	 * **/
	public final void setValueSetList(final ArrayList<MatValueSet> valueSet) {
		this.valueSetList = valueSet;
	}

}
