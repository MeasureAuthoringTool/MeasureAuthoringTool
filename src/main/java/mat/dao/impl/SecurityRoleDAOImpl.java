package mat.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.search.GenericDAO;
import mat.model.SecurityRole;

@Repository("securityRoleDAO")
public class SecurityRoleDAOImpl extends GenericDAO<SecurityRole, String> implements mat.dao.SecurityRoleDAO{
	
	public SecurityRoleDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
}
