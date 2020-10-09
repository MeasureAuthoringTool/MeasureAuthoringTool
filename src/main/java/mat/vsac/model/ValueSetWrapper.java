package mat.vsac.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class holds list of ValueSet.
 * **/
public class ValueSetWrapper implements Serializable  {
	/**
	 * List of ValueSet.
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
	 *            - List of ValueSet.
	 * **/
	public final void setValueSetList(final ArrayList<ValueSet> valueSet) {
		this.valueSetList = valueSet;
	}

}
