package mat.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

/**
 * The Class CodeListSearchCriteriaBuilder.
 */
public class CodeListSearchCriteriaBuilder extends ListObjectSearchCriteriaBuilder {

	/**
	 * Instantiates a new code list search criteria builder.
	 * 
	 * @param sessionFactory
	 *            the session factory
	 * @param searchClass
	 *            the search class
	 * @param searchText
	 *            the search text
	 * @param defaultCodeList
	 *            the default code list
	 */
	CodeListSearchCriteriaBuilder(SessionFactory sessionFactory,
			Class searchClass, String searchText, boolean defaultCodeList) {
		super(sessionFactory, searchClass, searchText, defaultCodeList);
		
	}

	/* (non-Javadoc)
	 * @see mat.dao.impl.ListObjectSearchCriteriaBuilder#getSearchFields(java.lang.String)
	 */
	@Override
	protected List<Criterion> getSearchFields(String text) {
		List<Criterion> retList =  super.getSearchFields(text);
		retList.add(Restrictions.ilike("c.description", text));
		retList.add(Restrictions.ilike("cs.description", text));
		retList.add(Restrictions.ilike("cd.description", text));
		retList.add(Restrictions.ilike("cd.code", text));
		return retList;
	}

	/* (non-Javadoc)
	 * @see mat.dao.impl.ListObjectSearchCriteriaBuilder#addAliases(org.hibernate.criterion.DetachedCriteria)
	 */
	@Override
	protected void addAliases(DetachedCriteria criteria) {
		super.addAliases(criteria);
		criteria.createAlias("category", "c");
		criteria.createAlias("codes", "cd", Criteria.LEFT_JOIN);
	}
}
