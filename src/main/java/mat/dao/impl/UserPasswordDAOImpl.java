package mat.dao.impl;

import mat.dao.UserPasswordDAO;
import mat.dao.search.GenericDAO;
import mat.model.UserPassword;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("userPasswordDAO")
public class UserPasswordDAOImpl extends GenericDAO<UserPassword, String> implements UserPasswordDAO {
	
	public UserPasswordDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
}
