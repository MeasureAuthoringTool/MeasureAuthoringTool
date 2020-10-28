package mat.dao.clause.impl;

import mat.dao.search.GenericDAO;
import mat.model.clause.QDSElement;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class QDSElementDAOImpl extends GenericDAO<QDSElement, String> {

	public QDSElementDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
}
