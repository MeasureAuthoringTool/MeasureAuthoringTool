package mat.dao;

import java.util.Map;

/**
 * The Interface ISearchCriteria.
 */
public interface ISearchCriteria {
	
	/**
	 * Sets the alias.
	 * 
	 * @param alias
	 *            the alias
	 */
	public void setAlias(Map<String, String> alias);
	
	/**
	 * Sets the property name.
	 * 
	 * @param propertyName
	 *            the new property name
	 */
	public void setPropertyName(String propertyName);
	
	/**
	 * Sets the property value.
	 * 
	 * @param propertyValue
	 *            the new property value
	 */
	public void setPropertyValue(Object propertyValue);
	
	/**
	 * Sets the operator.
	 * 
	 * @param operator
	 *            the new operator
	 */
	public void setOperator(PropertyOperator operator);
}
