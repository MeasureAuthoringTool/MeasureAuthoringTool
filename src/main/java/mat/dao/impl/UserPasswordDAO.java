package mat.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import mat.dao.search.GenericDAO;
import mat.model.UserPassword;

/**
 * The Class UserPasswordDAO.
 */
public class UserPasswordDAO extends GenericDAO<UserPassword, String> implements mat.dao.UserPasswordDAO {
	
	/* (non-Javadoc)
	 * @see mat.dao.UserPasswordDAO#getUserPasswordInfo(java.lang.String)
	 */
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
