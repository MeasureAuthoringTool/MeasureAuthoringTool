package mat.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import mat.dao.UserPasswordDAO;
import mat.dao.search.GenericDAO;
import mat.model.UserPassword;

@Repository("userPasswordDAO")
public class UserPasswordDAOImpl extends GenericDAO<UserPassword, String> implements UserPasswordDAO {
	
	public UserPasswordDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	public UserPassword getUserPasswordInfo(String userId)throws UsernameNotFoundException{
		 Session session = getSessionFactory().getCurrentSession();
		 Criteria criteria = session.createCriteria(UserPassword.class);
		 List results =  criteria.add(Restrictions.eq("emailAddress", userId)).list();
		 if(results.size() < 1){
			 throw new UsernameNotFoundException(userId + "not found");
		 }
		 return (UserPassword) results.get(0);
	}
	
}
