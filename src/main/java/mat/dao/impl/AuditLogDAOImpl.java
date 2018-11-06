package mat.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.search.GenericDAO;
import mat.model.AuditLog;

@Repository
public class AuditLogDAOImpl extends GenericDAO<AuditLog, String> implements mat.dao.AuditLogDAO {

	public AuditLogDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
}
