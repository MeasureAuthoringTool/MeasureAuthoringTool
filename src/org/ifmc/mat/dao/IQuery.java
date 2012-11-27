package org.ifmc.mat.dao;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.ifmc.mat.dao.search.SearchCriteria;

public interface IQuery {
	public void setOrders(HashMap<String, Boolean> orders);
	public void addOrder(String propertyName, Boolean isAscending);
	public void setPaging(int start, int numResults);
	public void setSubCriterias(HashMap<String, DetachedCriteria> subCriterias);
	public void addSubCriteria(String parentPropertyName, DetachedCriteria subCriteria);
	public void setSearchCriterias(List<SearchCriteria> searchCriterias);
	public void addSearchCriteria(SearchCriteria sc);
	@SuppressWarnings("rawtypes")
	public Criteria buildCriteria(Session session, Class clazz);
	
}
