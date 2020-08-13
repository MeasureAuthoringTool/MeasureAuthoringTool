package mat.dao.impl;

import mat.dao.StatusDAO;
import mat.dao.search.GenericDAO;
import mat.model.Status;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("statusDAO")
public class StatusDAOImpl extends GenericDAO<Status, String> implements StatusDAO{
	
	public StatusDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
}
