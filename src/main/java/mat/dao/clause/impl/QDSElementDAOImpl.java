package mat.dao.clause.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.search.GenericDAO;
import mat.model.clause.QDSElement;

@Repository
public class QDSElementDAOImpl extends GenericDAO<QDSElement, String> {

	public QDSElementDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
}
