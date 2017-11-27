package mat.dao.impl;

import mat.dao.search.GenericDAO;
import mat.model.EmailAuditLog;

/**
 * DAO implementation of Measure Audit Log.
 */
public class EmailAuditLogDAO extends GenericDAO<EmailAuditLog, String> implements mat.dao.EmailAuditLogDAO {
	
	@Override
	public boolean recordEmailEvent(EmailAuditLog log) {
			System.out.println("::Email Audit Log::");
			getSessionFactory().getCurrentSession().saveOrUpdate(log);
			return true; 
	}
}
