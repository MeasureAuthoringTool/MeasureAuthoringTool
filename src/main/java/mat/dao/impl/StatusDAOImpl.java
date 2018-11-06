package mat.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.StatusDAO;
import mat.dao.search.GenericDAO;
import mat.model.Status;

@Repository
public class StatusDAOImpl extends GenericDAO<Status, String> implements StatusDAO{
	
	public StatusDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
}
