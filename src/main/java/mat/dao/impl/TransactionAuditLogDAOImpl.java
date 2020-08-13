package mat.dao.impl;

import mat.dao.TransactionAuditLogDAO;
import mat.dao.search.GenericDAO;
import mat.model.TransactionAuditLog;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("transactionAuditLogDAO")
public class TransactionAuditLogDAOImpl extends GenericDAO<TransactionAuditLog, String> implements TransactionAuditLogDAO{

	public TransactionAuditLogDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
}
