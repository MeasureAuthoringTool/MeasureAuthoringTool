package mat.dao.impl.clause;

import java.util.List;

import mat.dao.search.GenericDAO;
import mat.shared.model.QDSTerm;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class QDSTermDAO extends GenericDAO<QDSTerm, String> implements mat.dao.clause.QDSTermDAO {

	public QDSTerm findByDecisionId(String decisionId) {
		List<QDSTerm> list = null;
		Session session = null;
		Transaction transaction = null;
		
		try {
			session = getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			Criteria criteria = session.createCriteria(QDSTerm.class);
			criteria.add(Restrictions.eq("decisionId", decisionId));			
	        list = criteria.list();
	        transaction.commit();
		}
		catch (Exception onfe) {
			onfe.printStackTrace();
		}
		finally {
	    	rollbackUncommitted(transaction);
	    	closeSession(session);
		}
		if (!list.isEmpty()) {
			return list.get(0);
		} else {
			return null;
		}
	}
}
