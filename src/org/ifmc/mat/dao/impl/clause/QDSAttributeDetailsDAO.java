package org.ifmc.mat.dao.impl.clause;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.ifmc.mat.dao.search.GenericDAO;
import org.ifmc.mat.model.clause.QDSAttributeDetails;

public class QDSAttributeDetailsDAO extends GenericDAO<QDSAttributeDetails, String> implements org.ifmc.mat.dao.clause.QDSAttributeDetailsDAO {

	@Override
	public List<QDSAttributeDetails> findByDecisionId(String decisionId) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(QDSAttributeDetails.class);
		criteria.add(Restrictions.eq("decisionId", decisionId));
		return criteria.list();
	}

}
