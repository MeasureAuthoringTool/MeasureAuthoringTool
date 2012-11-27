package mat.dao.search;

import java.util.Map;

import mat.dao.ISearchCriteria;
import mat.dao.PropertyOperator;

public class SearchCriteria implements ISearchCriteria{
	private String propertyName;
	private Object propertyValue;
	private PropertyOperator operator;
	//map of object and alias name
	private Map<String, String> alias;
	
	
	public SearchCriteria() {
		super();
	}

	public SearchCriteria(String propertyName, Object propertyValue,
			PropertyOperator operator, Map<String, String> alias) {
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
		this.operator = operator;
		this.alias = alias;
	}
	
	public Map<String, String> getAlias() {
		return alias;
	}
	public void setAlias(Map<String, String> alias) {
		this.alias = alias;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public Object getPropertyValue() {
		return propertyValue;
	}
	public void setPropertyValue(Object propertyValue) {
		this.propertyValue = propertyValue;
	}
	public PropertyOperator getOperator() {
		return operator;
	}
	public void setOperator(PropertyOperator operator) {
		this.operator = operator;
	} 
}
