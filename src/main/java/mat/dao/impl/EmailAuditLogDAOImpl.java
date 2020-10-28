package mat.dao.impl;

import mat.dao.EmailAuditLogDAO;
import mat.dao.search.GenericDAO;
import mat.model.EmailAuditLog;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("emailAuditLogDAO")
public class EmailAuditLogDAOImpl extends GenericDAO<EmailAuditLog, String> implements EmailAuditLogDAO {
	
	public EmailAuditLogDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
}
