package mat.dao.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mat.dao.IQuery;
import mat.dao.PropertyOperator;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.criterion.Subqueries;

/**
 * The Class CriteriaQuery.
 */
public class CriteriaQuery implements IQuery{
	
	/** The search criterias. */
	private List<SearchCriteria> searchCriterias = new ArrayList<SearchCriteria>();
	//map of property name and detached criteria
	/** The sub criterias. */
	private HashMap<String, DetachedCriteria> subCriterias = new HashMap<String, DetachedCriteria>();
	
	/** The paging. */
	private Paging paging = null;
	//map of property name and boolean value. A true value means ascending order and false value
	//means descending order.
	/** The orders. */
	private HashMap<String, Boolean> orders = new HashMap<String, Boolean>();
	
	/**
	 * Instantiates a new criteria query.
	 * 
	 * @param searchCriteria
	 *            the search criteria
	 */
	public CriteriaQuery(SearchCriteria searchCriteria) {
		this.searchCriterias.add(searchCriteria);
	}	
	
	/**
	 * Instantiates a new criteria query.
	 * 
	 * @param searchCriterias
	 *            the search criterias
	 */
	public CriteriaQuery(List<SearchCriteria> searchCriterias) {
		this.searchCriterias = searchCriterias;
	}

	/**
	 * Gets the orders.
	 * 
	 * @return the orders
	 */
	public HashMap<String, Boolean> getOrders() {
		return orders;
	}

	/* (non-Javadoc)
	 * @see mat.dao.IQuery#setOrders(java.util.HashMap)
	 */
	public void setOrders(HashMap<String, Boolean> orders) {
		this.orders = orders;
	}

	/* (non-Javadoc)
	 * @see mat.dao.IQuery#addOrder(java.lang.String, java.lang.Boolean)
	 */
	public void addOrder(String propertyName, Boolean isAscending) {
		orders.put(propertyName, isAscending);
	}
	
	/**
	 * Gets the paging.
	 * 
	 * @return the paging
	 */
	public Paging getPaging() {
		return paging;
	}

	/**
	 * Sets the paging.
	 * 
	 * @param paging
	 *            the new paging
	 */
	public void setPaging(Paging paging) {
		this.paging = paging;
	}
 
	/* (non-Javadoc)
	 * @see mat.dao.IQuery#setPaging(int, int)
	 */
	public void setPaging(int start, int numResults) {
		this.paging = new Paging(start, numResults);		
	}
	
	/**
	 * Gets the sub criterias.
	 * 
	 * @return the sub criterias
	 */
	public HashMap<String, DetachedCriteria> getSubCriterias() {
		return subCriterias;
	}

	/* (non-Javadoc)
	 * @see mat.dao.IQuery#setSubCriterias(java.util.HashMap)
	 */
	public void setSubCriterias(HashMap<String, DetachedCriteria> subCriterias) {
		this.subCriterias = subCriterias;
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.IQuery#addSubCriteria(java.lang.String, org.hibernate.criterion.DetachedCriteria)
	 */
	public void addSubCriteria(String parentPropertyName, DetachedCriteria subCriteria) {
		subCriterias.put(parentPropertyName, subCriteria);
	}

	/**
	 * Gets the search criterias.
	 * 
	 * @return the search criterias
	 */
	public List<SearchCriteria> getSearchCriterias() {
		return searchCriterias;
	}

	/* (non-Javadoc)
	 * @see mat.dao.IQuery#addSearchCriteria(mat.dao.search.SearchCriteria)
	 */
	public void addSearchCriteria(SearchCriteria sc) {
		getSearchCriterias().add(sc);		
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.IQuery#setSearchCriterias(java.util.List)
	 */
	public void setSearchCriterias(List<SearchCriteria> searchCriterias) {
		this.searchCriterias = searchCriterias;
	}

	/**
	 * Gets the aliases.
	 * 
	 * @return the aliases
	 */
	private HashMap<String,	String> getAliases() {
		HashMap<String, String> aliases = new HashMap<String, String>();
		for (SearchCriteria sc : getSearchCriterias()) {
			if (sc.getAlias() != null)
				aliases.putAll(sc.getAlias());
		}
		return aliases;
	}

	/* (non-Javadoc)
	 * @see mat.dao.IQuery#buildCriteria(org.hibernate.Session, java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	public Criteria buildCriteria(Session session, Class clazz) {
		@SuppressWarnings("deprecation")
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
	
	/**
	 * Gets the criterion.
	 * 
	 * @param criteria
	 *            the criteria
	 * @return the criterion
	 */
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
	
	
	/**
	 * The Class Paging.
	 */
	public class Paging {
		
		/** The Constant DEFAULT_PAGE_SIZE. */
		public static final int DEFAULT_PAGE_SIZE = 100; 
		
		/** The start. */
		private int start = 0;
		
		/** The max length. */
		private int maxLength = DEFAULT_PAGE_SIZE;
		
		/**
		 * Instantiates a new paging.
		 * 
		 * @param start
		 *            the start
		 * @param maxLength
		 *            the max length
		 */
		protected Paging(int start, int maxLength) {
			this.start = start;
			this.maxLength = maxLength;
		}
		
		/**
		 * Gets the max length.
		 * 
		 * @return the max length
		 */
		public int getMaxLength() {
			return maxLength;
		}
		
		/**
		 * Sets the max length.
		 * 
		 * @param length
		 *            the new max length
		 */
		public void setMaxLength(int length) {
			this.maxLength = length;
		}
		
		/**
		 * Gets the start.
		 * 
		 * @return the start
		 */
		public int getStart() {
			return start;
		}
		
		/**
		 * Sets the start.
		 * 
		 * @param start
		 *            the new start
		 */
		public void setStart(int start) {
			this.start = start;
		}
	}	
}
