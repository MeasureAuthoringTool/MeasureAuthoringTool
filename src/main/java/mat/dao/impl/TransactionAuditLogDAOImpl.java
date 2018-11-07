package mat.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.TransactionAuditLogDAO;
import mat.dao.search.GenericDAO;
import mat.model.TransactionAuditLog;

@Repository("transactionAuditLogDAO")
public class TransactionAuditLogDAOImpl extends GenericDAO<TransactionAuditLog, String> implements TransactionAuditLogDAO{

	public TransactionAuditLogDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
}
