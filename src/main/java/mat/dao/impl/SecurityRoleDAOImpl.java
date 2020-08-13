package mat.dao.impl;

import mat.dao.search.GenericDAO;
import mat.model.SecurityRole;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("securityRoleDAO")
public class SecurityRoleDAOImpl extends GenericDAO<SecurityRole, String> implements mat.dao.SecurityRoleDAO{
	
	public SecurityRoleDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
}
