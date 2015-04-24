package mat.dao;

import java.util.HashMap;
import java.util.List;

import mat.dao.search.SearchCriteria;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;

/**
 * The Interface IQuery.
 */
public interface IQuery {
	
	/**
	 * Sets the orders.
	 * 
	 * @param orders
	 *            the orders
	 */
	public void setOrders(HashMap<String, Boolean> orders);
	
	/**
	 * Adds the order.
	 * 
	 * @param propertyName
	 *            the property name
	 * @param isAscending
	 *            the is ascending
	 */
	public void addOrder(String propertyName, Boolean isAscending);
	
	/**
	 * Sets the paging.
	 * 
	 * @param start
	 *            the start
	 * @param numResults
	 *            the num results
	 */
	public void setPaging(int start, int numResults);
	
	/**
	 * Sets the sub criterias.
	 * 
	 * @param subCriterias
	 *            the sub criterias
	 */
	public void setSubCriterias(HashMap<String, DetachedCriteria> subCriterias);
	
	/**
	 * Adds the sub criteria.
	 * 
	 * @param parentPropertyName
	 *            the parent property name
	 * @param subCriteria
	 *            the sub criteria
	 */
	public void addSubCriteria(String parentPropertyName, DetachedCriteria subCriteria);
	
	/**
	 * Sets the search criterias.
	 * 
	 * @param searchCriterias
	 *            the new search criterias
	 */
	public void setSearchCriterias(List<SearchCriteria> searchCriterias);
	
	/**
	 * Adds the search criteria.
	 * 
	 * @param sc
	 *            the sc
	 */
	public void addSearchCriteria(SearchCriteria sc);
	
	/**
	 * Builds the criteria.
	 * 
	 * @param session
	 *            the session
	 * @param clazz
	 *            the clazz
	 * @return the criteria
	 */
	@SuppressWarnings("rawtypes")
	public Criteria buildCriteria(Session session, Class clazz);
	
}
