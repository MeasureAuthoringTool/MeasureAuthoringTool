package mat.dao.impl.clause;

import java.util.List;

import mat.dao.search.GenericDAO;
import mat.model.clause.QDSAttributeDetails;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * The Class QDSAttributeDetailsDAO.
 */
public class QDSAttributeDetailsDAO extends GenericDAO<QDSAttributeDetails, String> implements mat.dao.clause.QDSAttributeDetailsDAO {

	/* (non-Javadoc)
	 * @see mat.dao.clause.QDSAttributeDetailsDAO#findByDecisionId(java.lang.String)
	 */
	@Override
	public List<QDSAttributeDetails> findByDecisionId(String decisionId) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(QDSAttributeDetails.class);
		criteria.add(Restrictions.eq("decisionId", decisionId));
		return criteria.list();
	}

}
