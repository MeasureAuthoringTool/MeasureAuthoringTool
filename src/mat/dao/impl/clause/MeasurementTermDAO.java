package mat.dao.impl.clause;

import java.util.List;

import mat.dao.search.GenericDAO;
import mat.shared.model.MeasurementTerm;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class MeasurementTermDAO extends GenericDAO<MeasurementTerm, String> implements mat.dao.clause.MeasurementTermDAO {
	public MeasurementTerm findByDecisionId(String decisionId) {
		List<MeasurementTerm> list = null;
		Session session = null;
		Transaction transaction = null;
		
		try {
			session = getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			Criteria criteria = session.createCriteria(MeasurementTerm.class);
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
		if(list.isEmpty()) return null;
		return list.get(0);
	}
}
