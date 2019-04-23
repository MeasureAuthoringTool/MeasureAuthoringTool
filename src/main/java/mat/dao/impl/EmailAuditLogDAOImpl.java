package mat.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.EmailAuditLogDAO;
import mat.dao.search.GenericDAO;
import mat.model.EmailAuditLog;

@Repository("emailAuditLogDAO")
public class EmailAuditLogDAOImpl extends GenericDAO<EmailAuditLog, String> implements EmailAuditLogDAO {
	
	public EmailAuditLogDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
}
