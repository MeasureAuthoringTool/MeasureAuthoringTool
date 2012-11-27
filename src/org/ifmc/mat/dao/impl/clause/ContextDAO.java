package org.ifmc.mat.dao.impl.clause;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.ifmc.mat.dao.search.GenericDAO;
import org.ifmc.mat.model.clause.Context;

public class ContextDAO extends GenericDAO<Context, String> implements org.ifmc.mat.dao.clause.ContextDAO{

	public java.util.List<Context> getAll() {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Context.class);
		return criteria.list();
	}

	
	public Context getContext(String desc) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Context.class);
		criteria.add(Restrictions.eq("description", desc));
		List<Context> ctxs = criteria.list();
		if (!ctxs.isEmpty()) {
			return ctxs.get(0);
		} else {
			return null;
		}
	}
}
