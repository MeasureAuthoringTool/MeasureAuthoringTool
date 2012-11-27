package org.ifmc.mat.dao.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.criterion.Subqueries;
import org.ifmc.mat.dao.IQuery;
import org.ifmc.mat.dao.PropertyOperator;

public class CriteriaQuery implements IQuery{
	private List<SearchCriteria> searchCriterias = new ArrayList<SearchCriteria>();
	//map of property name and detached criteria
	private HashMap<String, DetachedCriteria> subCriterias = new HashMap<String, DetachedCriteria>();
	private Paging paging = null;
	//map of property name and boolean value. A true value means ascending order and false value
	//means descending order.
	private HashMap<String, Boolean> orders = new HashMap<String, Boolean>();
	
	public CriteriaQuery(SearchCriteria searchCriteria) {
		this.searchCriterias.add(searchCriteria);
	}	
	
	public CriteriaQuery(List<SearchCriteria> searchCriterias) {
		this.searchCriterias = searchCriterias;
	}

	public HashMap<String, Boolean> getOrders() {
		return orders;
	}

	public void setOrders(HashMap<String, Boolean> orders) {
		this.orders = orders;
	}

	public void addOrder(String propertyName, Boolean isAscending) {
		orders.put(propertyName, isAscending);
	}
	
	public Paging getPaging() {
		return paging;
	}

	public void setPaging(Paging paging) {
		this.paging = paging;
	}
 
	public void setPaging(int start, int numResults) {
		this.paging = new Paging(start, numResults);		
	}
	
	public HashMap<String, DetachedCriteria> getSubCriterias() {
		return subCriterias;
	}

	public void setSubCriterias(HashMap<String, DetachedCriteria> subCriterias) {
		this.subCriterias = subCriterias;
	}
	
	public void addSubCriteria(String parentPropertyName, DetachedCriteria subCriteria) {
		subCriterias.put(parentPropertyName, subCriteria);
	}

	public List<SearchCriteria> getSearchCriterias() {
		return searchCriterias;
	}

	public void addSearchCriteria(SearchCriteria sc) {
		getSearchCriterias().add(sc);		
	}
	
	public void setSearchCriterias(List<SearchCriteria> searchCriterias) {
		this.searchCriterias = searchCriterias;
	}

	private HashMap<String,	String> getAliases() {
		HashMap<String, String> aliases = new HashMap<String, String>();
		for (SearchCriteria sc : getSearchCriterias()) {
			if (sc.getAlias() != null)
				aliases.putAll(sc.getAlias());
		}
		return aliases;
	}

	@SuppressWarnings("rawtypes")
	public Criteria buildCriteria(Session session, Class clazz) {
		Criteria criteria = session.createCriteria(clazz);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		HashMap<String, String> alias = getAliases();
		for (String objectName : alias.keySet())
			criteria.createAlias(objectName, alias.get(objectName));
		
		HashMap<String, DetachedCriteria> subCriteria = getSubCriterias();
        for (String propertyName : subCriteria.keySet()) {
    		DetachedCriteria dc = subCriteria.get(propertyName);
    		criteria.add(Subqueries.propertyIn(propertyName, dc));
        }
	
        for (SearchCriteria sc : getSearchCriterias()) {        	
        		criteria.add(getCriterion(sc));
        }
        
        Paging paging = getPaging();
        if(paging != null) {
        	if (paging.getStart() < 0) paging.setStart(0);
       		criteria.setFirstResult(paging.getStart());        	
        	// A negative max length may be used to indicate no limit
        	if(paging.getMaxLength() >= 0)
        		criteria.setMaxResults(paging.getMaxLength());
        }

        HashMap<String, Boolean> orders = getOrders(); 
		for(String propertyName : orders.keySet()) {
			if((Boolean)orders.get(propertyName).booleanValue())
				criteria.addOrder(Order.asc(propertyName));
			else
				criteria.addOrder(Order.desc(propertyName));
		}
		
		return criteria;
	}
	
	@SuppressWarnings("rawtypes")
	protected Criterion getCriterion(SearchCriteria criteria) {
		SimpleExpression r = null;
		String searchField = criteria.getPropertyName();
		Object value = criteria.getPropertyValue();
		if (criteria.getOperator().ordinal() == PropertyOperator.LIKE.ordinal()) {
			// Use MatchMode.ANYWHERE, so we do not have to hardcode "%" wildcards around value
			return Restrictions.ilike(searchField, (String)value, MatchMode.ANYWHERE);
		} else if (criteria.getOperator().ordinal() == PropertyOperator.IN.ordinal()) {	
			return Restrictions.in(searchField, (List)value);
		}
		else {
			if (criteria.getOperator().ordinal() == PropertyOperator.EQ.ordinal()) {
				r = Restrictions.eq(searchField, value);
			} else if (criteria.getOperator().ordinal() == PropertyOperator.GT.ordinal()) { 
				r = Restrictions.gt(searchField, value);
			} else if (criteria.getOperator().ordinal() == PropertyOperator.GTE.ordinal()) {
				r = Restrictions.ge(searchField, value);
			} else if (criteria.getOperator().ordinal() == PropertyOperator.LT.ordinal()) {
				r = Restrictions.lt(searchField, value);
			} else if (criteria.getOperator().ordinal() == PropertyOperator.LTE.ordinal()) {
				r = Restrictions.le(searchField, value);
			} else if (criteria.getOperator().ordinal() == PropertyOperator.NE.ordinal()) {
				r = Restrictions.ne(searchField, value);
			}
			
			if(r != null && value instanceof String) {
				r = r.ignoreCase();
			}	
		}

		return r;
	}
	
	
	public class Paging {
		public static final int DEFAULT_PAGE_SIZE = 100; 
		private int start = 0;
		private int maxLength = DEFAULT_PAGE_SIZE;
		
		protected Paging(int start, int maxLength) {
			this.start = start;
			this.maxLength = maxLength;
		}
		
		public int getMaxLength() {
			return maxLength;
		}
		public void setMaxLength(int length) {
			this.maxLength = length;
		}
		public int getStart() {
			return start;
		}
		public void setStart(int start) {
			this.start = start;
		}
	}	
}
