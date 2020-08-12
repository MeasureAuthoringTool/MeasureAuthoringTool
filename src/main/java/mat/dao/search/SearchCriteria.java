package mat.dao.search;

import mat.dao.ISearchCriteria;
import mat.dao.PropertyOperator;

import java.util.Map;

/**
 * The Class SearchCriteria.
 */
public class SearchCriteria implements ISearchCriteria{
	
	/** The property name. */
	private String propertyName;
	
	/** The property value. */
	private Object propertyValue;
	
	/** The operator. */
	private PropertyOperator operator;
	//map of object and alias name
	/** The alias. */
	private Map<String, String> alias;
	
	
	/**
	 * Instantiates a new search criteria.
	 */
	public SearchCriteria() {
		super();
	}

	/**
	 * Instantiates a new search criteria.
	 * 
	 * @param propertyName
	 *            the property name
	 * @param propertyValue
	 *            the property value
	 * @param operator
	 *            the operator
	 * @param alias
	 *            the alias
	 */
	public SearchCriteria(String propertyName, Object propertyValue,
			PropertyOperator operator, Map<String, String> alias) {
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
		this.operator = operator;
		this.alias = alias;
	}
	
	/**
	 * Gets the alias.
	 * 
	 * @return the alias
	 */
	public Map<String, String> getAlias() {
		return alias;
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.ISearchCriteria#setAlias(java.util.Map)
	 */
	public void setAlias(Map<String, String> alias) {
		this.alias = alias;
	}
	
	/**
	 * Gets the property name.
	 * 
	 * @return the property name
	 */
	public String getPropertyName() {
		return propertyName;
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.ISearchCriteria#setPropertyName(java.lang.String)
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
	/**
	 * Gets the property value.
	 * 
	 * @return the property value
	 */
	public Object getPropertyValue() {
		return propertyValue;
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.ISearchCriteria#setPropertyValue(java.lang.Object)
	 */
	public void setPropertyValue(Object propertyValue) {
		this.propertyValue = propertyValue;
	}
	
	/**
	 * Gets the operator.
	 * 
	 * @return the operator
	 */
	public PropertyOperator getOperator() {
		return operator;
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.ISearchCriteria#setOperator(mat.dao.PropertyOperator)
	 */
	public void setOperator(PropertyOperator operator) {
		this.operator = operator;
	} 
}
