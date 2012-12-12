package mat.dao.impl;

import java.util.Date;
import java.util.List;

import mat.dao.search.GenericDAO;
import mat.model.User;
import mat.server.model.MatUserDetails;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDAO extends GenericDAO<User, String> implements mat.dao.UserDAO {
	private static final Log logger = LogFactory.getLog(UserDAO.class);
	
	@Override
	public void delete(String...ids)  {
		for(String s : ids){
			User u = find(s);
			delete(u);
		}
	}
	
	public void delete(String id){
		
		System.out.println("Howdy");
	}
	
	@Override
	public void expireTemporaryPasswords(Date targetDate) {
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			int updatedCount = 0;
			Criteria criteria = session.createCriteria(User.class).createCriteria("password");
			criteria.add(Restrictions.lt("createdDate", targetDate)).add(Restrictions.eq("temporaryPassword", Boolean.TRUE));
			
			@SuppressWarnings("unchecked")
			List<User> results = criteria.list();
			for(User u : results) {
				u.getPassword().setPassword("expired");
				updatedCount++;
			}

			logger.info("Expired password count: " + updatedCount);	
			tx.commit();
		}
		finally {
			session.close();
		}
	}
	@Override
	public void unlockUsers(Date unlockDate) {
		
		Session session = getSessionFactory().openSession();
		try {
			Transaction tx = session.beginTransaction();
	
			int updatedCount = 0;
			Criteria criteria = session.createCriteria(User.class);
			criteria.add(Restrictions.lt("lockedOutDate", unlockDate));
			@SuppressWarnings("unchecked")
			List<User> results = criteria.list();
			for(User u : results) {
				u.setLockedOutDate(null);
				u.getPassword().setForgotPwdlockCounter(0);
				u.getPassword().setPasswordlockCounter(0);
				updatedCount++;
			}
			
			tx.commit();
			logger.info("Unlocked user count: " + updatedCount);
		}
		finally {
			session.close();
		}
	}
	
	private Criteria createSearchCriteria(String text) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.or(Restrictions.ilike("firstName",  "%" + text + "%"),
									Restrictions.ilike("lastName",  "%" + text + "%")));
		criteria.add(Restrictions.ne("id", "Admin"));
		return criteria;
	}
	@SuppressWarnings("unchecked")
	public List<User> searchForUsersByName(String name, int startIndex, int numResults) {
		Criteria criteria = createSearchCriteria(name);
		criteria.addOrder(Order.asc("lastName"));
		criteria.setFirstResult(startIndex);
		if(numResults > 0) {
			criteria.setMaxResults(numResults);
		}
		return criteria.list();
		
	}
	public int countSearchResults(String text) {
		Criteria criteria = createSearchCriteria(text);
		criteria.setProjection(Projections.rowCount());
		return ((Long)criteria.uniqueResult()).intValue();
	}
	
	@SuppressWarnings("rawtypes")
	public UserDetails getUser(String userId){
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(MatUserDetails.class);
		//List results = criteria.add(Restrictions.ilike("emailAddress",userId)).list();
		List results = criteria.add(Restrictions.ilike("loginId",userId)).list();
		if(results.size() <1){
			return null;
		}
		else {
			return (UserDetails) results.get(0);
		}
	}
	@Override
	public Boolean userExists(String userid) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.ilike("emailAddress", userid));
		criteria.setProjection(Projections.rowCount());
		return ((Long)criteria.uniqueResult()) > 0;
	}
	
	
	@Override
	public boolean findUniqueLoginId(String loginId) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.ilike("loginId", loginId));
		criteria.setProjection(Projections.rowCount());
		return ((Long)criteria.uniqueResult()) > 0;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public User findByEmail(String email) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.ilike("emailAddress", email));
		List<User> results = criteria.list();
		if(results.size() > 0) {
			return results.get(0);
		}
		else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public User findByLoginId(String loginId) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.ilike("loginId", loginId));
		List<User> results = criteria.list();
		if(results.size() > 0) {
			return results.get(0);
		}
		else {
			return null;
		}
	}
	
	
	public void saveUserDetails(MatUserDetails userdetails) {
		if(isEmpty(userdetails)) return;
		
		Session session = null;
		Transaction transaction = null;
		try {
			session = getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(userdetails);
			
			transaction.commit();
		}
		catch (Exception e) {
			e.printStackTrace();		  
		}
		finally {
	    	rollbackUncommitted(transaction);
	    	closeSession(session);
		}
	}

	//US212
	@Override
	public void setUserSignInDate(String userid) {
		User u = find(userid);
		Date current = new Date();
		u.setSignInDate(current);
		save(u);
	}
	//US212
	@Override
	public void setUserSignOutDate(String userid) {
		User u = find(userid);
		Date current = new Date();
		u.setSignOutDate(current);
		save(u);
	}
}
