package org.ifmc.mat.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class CodeListSearchCriteriaBuilder extends ListObjectSearchCriteriaBuilder {

	CodeListSearchCriteriaBuilder(SessionFactory sessionFactory,
			Class searchClass, String searchText, boolean defaultCodeList) {
		super(sessionFactory, searchClass, searchText, defaultCodeList);
		
	}

	@Override
	protected List<Criterion> getSearchFields(String text) {
		List<Criterion> retList =  super.getSearchFields(text);
		retList.add(Restrictions.ilike("c.description", text));
		retList.add(Restrictions.ilike("cs.description", text));
		retList.add(Restrictions.ilike("cd.description", text));
		retList.add(Restrictions.ilike("cd.code", text));
		return retList;
	}

	@Override
	protected void addAliases(DetachedCriteria criteria) {
		super.addAliases(criteria);
		criteria.createAlias("category", "c");
		criteria.createAlias("codes", "cd", Criteria.LEFT_JOIN);
	}
}
