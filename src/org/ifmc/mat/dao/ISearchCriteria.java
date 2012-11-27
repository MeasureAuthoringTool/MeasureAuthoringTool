package org.ifmc.mat.dao;

import java.util.Map;

public interface ISearchCriteria {
	public void setAlias(Map<String, String> alias);
	public void setPropertyName(String propertyName);
	public void setPropertyValue(Object propertyValue);
	public void setOperator(PropertyOperator operator);
}
