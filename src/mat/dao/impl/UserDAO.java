package mat.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.core.userdetails.UserDetails;

import mat.dao.search.GenericDAO;
import mat.model.Organization;
import mat.model.SecurityQuestions;
import mat.model.User;
import mat.server.model.MatUserDetails;


/**
 * The Class UserDAO.
 */
public class UserDAO extends GenericDAO<User, String> implements
mat.dao.UserDAO {
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(UserDAO.class);
	
	/* (non-Javadoc)
	 * @see mat.dao.search.GenericDAO#delete(ID[])
	 */
	@Override
	public void delete(String... ids) {
		for (String s : ids) {
			User u = find(s);
			delete(u);
		}
	}
	
	
	/* (non-Javadoc)
	 * @see mat.dao.UserDAO#expireTemporaryPasswords(java.util.Date)
	 */
	@Override
	public void expireTemporaryPasswords(Date targetDate) {
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			int updatedCount = 0;
			Criteria criteria = session.createCriteria(User.class)
					.createCriteria("password");
			criteria.add(Restrictions.lt("createdDate", targetDate)).add(
					Restrictions.eq("temporaryPassword", Boolean.TRUE));
			
			@SuppressWarnings("unchecked")
			List<User> results = criteria.list();
			for (User u : results) {
				u.getPassword().setPassword("expired");
				updatedCount++;
			}
			
			logger.info("Expired password count: " + updatedCount);
			tx.commit();
		} finally {
			rollbackUncommitted(tx);
			closeSession(session);
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.UserDAO#unlockUsers(java.util.Date)
	 */
	@Override
	public void unlockUsers(Date unlockDate) {
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			int updatedCount = 0;
			Criteria criteria = session.createCriteria(User.class);
			criteria.add(Restrictions.lt("lockedOutDate", unlockDate));
			@SuppressWarnings("unchecked")
			List<User> results = criteria.list();
			for (User u : results) {
				u.setLockedOutDate(null);
				u.getPassword().setForgotPwdlockCounter(0);
				u.getPassword().setPasswordlockCounter(0);
				updatedCount++;
			}
			tx.commit();
			logger.info("Unlocked user count: " + updatedCount);
		} finally {
			rollbackUncommitted(tx);
			closeSession(session);
		}
	}
	
	/**
	 * Creates the search criteria.
	 * 
	 * @param text
	 *            the text
	 * @return the criteria
	 */
	private Criteria createSearchCriteria(String text) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(User.class);
		Criterion nameRestriction = Restrictions.or(Restrictions.ilike("firstName", "%" + text + "%"),
				Restrictions.ilike("lastName", "%" + text + "%"));
		Criterion idResticition = Restrictions.or(Restrictions.ilike("emailAddress", "%" + text + "%"),
				Restrictions.ilike("loginId", "%" + text + "%"));
		criteria.add(Restrictions.or(nameRestriction, idResticition));
		criteria.add(Restrictions.ne("id", "Admin"));
		return criteria;
	}
	
	/**
	 * Creates the search criteria non admin user.
	 * 
	 * @param text
	 *            the text
	 * @return the criteria
	 */
	private Criteria createSearchCriteriaNonAdminUser(String text) {
		
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(User.class);
		Criterion nameRestriction = Restrictions.or(Restrictions.ilike("firstName", "%" + text + "%"),
				Restrictions.ilike("lastName", "%" + text + "%"));
		Criterion idResticition = Restrictions.or(Restrictions.ilike("emailAddress", "%" + text + "%"),
				Restrictions.ilike("loginId", "%" + text + "%"));
		criteria.add(Restrictions.or(nameRestriction, idResticition));
		criteria.add(Restrictions.ne("securityRole.id", "1"));
		criteria.add(Restrictions.ne("status.id", "2"));
		return criteria;
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.UserDAO#searchForUsersByName(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<User> searchForUsersByName(String name) {
		Criteria criteria = createSearchCriteria(name);
		criteria.addOrder(Order.asc("lastName"));
		/*criteria.setFirstResult(startIndex);
		if (numResults > 0) {
			criteria.setMaxResults(numResults);
		}*/
		return criteria.list();
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.UserDAO#searchAllUsedOrganizations()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public
	HashMap < String , Organization> searchAllUsedOrganizations(){
		List<Organization> usedOrganization = new ArrayList<Organization>();
		HashMap < String , Organization> usedOrganizationMap = new HashMap<String,Organization>();
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.setProjection(Projections.distinct(Projections.property("organization")));
		usedOrganization = criteria.list();
		for(Organization org : usedOrganization){
			usedOrganizationMap.put(Long.toString(org.getId()), org);
		}
		return usedOrganizationMap;
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.UserDAO#searchNonAdminUsers(java.lang.String, int, int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<User> searchNonAdminUsers(String name, int startIndex,
			int numResults) {
		Criteria criteria = createSearchCriteriaNonAdminUser(name);
		criteria.addOrder(Order.asc("lastName"));
		criteria.setFirstResult(startIndex);
		if (numResults > 0) {
			criteria.setMaxResults(numResults);
		}
		return criteria.list();
		
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.UserDAO#getAllNonAdminActiveUsers()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAllNonAdminActiveUsers() {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.ne("securityRole.id", "1"));
		criteria.add(Restrictions.eq("status.id", "1"));
		criteria.addOrder(Order.asc("lastName"));
		return criteria.list();
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.UserDAO#countSearchResults(java.lang.String)
	 */
	@Override
	public int countSearchResults(String text) {
		Criteria criteria = createSearchCriteria(text);
		criteria.setProjection(Projections.rowCount());
		return ((Long) criteria.uniqueResult()).intValue();
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.UserDAO#countSearchResultsNonAdmin(java.lang.String)
	 */
	@Override
	public int countSearchResultsNonAdmin(String text) {
		Criteria criteria = createSearchCriteriaNonAdminUser(text);
		criteria.setProjection(Projections.rowCount());
		return ((Long) criteria.uniqueResult()).intValue();
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.UserDAO#getUser(java.lang.String)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public UserDetails getUser(String userId) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(MatUserDetails.class);
		// List results =
		// criteria.add(Restrictions.ilike("emailAddress",userId)).list();
		List results = criteria.add(Restrictions.ilike("loginId", userId))
				.list();
		if (results.size() < 1) {
			return null;
		} else {
			return (UserDetails) results.get(0);
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.UserDAO#userExists(java.lang.String)
	 */
	@Override
	public Boolean userExists(String userid) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.ilike("emailAddress", userid));
		criteria.setProjection(Projections.rowCount());
		return ((Long) criteria.uniqueResult()) > 0;
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.UserDAO#findUniqueLoginId(java.lang.String)
	 */
	@Override
	public boolean findUniqueLoginId(String loginId) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.ilike("loginId", loginId));
		criteria.setProjection(Projections.rowCount());
		return ((Long) criteria.uniqueResult()) > 0;
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.UserDAO#findByEmail(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public User findByEmail(String email) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.ilike("emailAddress", email));
		List<User> results = criteria.list();
		if (results.size() > 0) {
			return results.get(0);
		} else {
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.UserDAO#findByLoginId(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public User findByLoginId(String loginId) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.ilike("loginId", loginId));
		List<User> results = criteria.list();
		if (results.size() > 0) {
			return results.get(0);
		} else {
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.UserDAO#saveUserDetails(mat.server.model.MatUserDetails)
	 */
	@Override
	public void saveUserDetails(MatUserDetails userdetails) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(userdetails);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rollbackUncommitted(transaction);
			closeSession(session);
		}
	}
	
	// US212
	/* (non-Javadoc)
	 * @see mat.dao.UserDAO#setUserSignInDate(java.lang.String)
	 */
	@Override
	public void setUserSignInDate(String userid) {
		User u = find(userid);
		Date current = new Date();
		u.setSignInDate(current);
		save(u);
	}
	
	// US212
	/* (non-Javadoc)
	 * @see mat.dao.UserDAO#setUserSignOutDate(java.lang.String)
	 */
	@Override
	public void setUserSignOutDate(String userid) {
		User u = find(userid);
		Date current = new Date();
		u.setSignOutDate(current);
		save(u);
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.UserDAO#getRandomSecurityQuestion(java.lang.String)
	 */
	@Override
	public String getRandomSecurityQuestion(String userLoginId) {
		
		
		User user = findByLoginId(userLoginId);
		String question = null;
		if (null == user) {
			question = getRandomSecurityQuestion();
		}else{
			//			String query = "SELECT S.QUESTION FROM USER_SECURITY_QUESTIONS US JOIN SECURITY_QUESTIONS S"
			//					+ " ON US.QUESTION_ID = S.QUESTION_ID WHERE US.USER_ID = '"
			//					+ user.getId() + "' ORDER BY RAND() LIMIT 1";
			String sql = "SELECT S.QUESTION FROM USER_SECURITY_QUESTIONS US JOIN SECURITY_QUESTIONS S"
					+ " ON US.QUESTION_ID = S.QUESTION_ID WHERE US.USER_ID = :userId"
					+ " ORDER BY RAND() LIMIT 1";
			
			Session session = getSessionFactory().getCurrentSession();
			SQLQuery query = session.createSQLQuery(sql);
			query.setString("userId", user.getId());
			
			List<String> list = query.list();
			if ((list == null) || list.isEmpty()) {
				question = getRandomSecurityQuestion();
			} else {
				question = list.get(0);
			}
		}
		
		return question;
	}
	
	/**
	 * Gets the security question by id.
	 * 
	 * @param questionId
	 *            the question id
	 * @return the security question by id
	 */
	public SecurityQuestions getSecurityQuestionById(String questionId) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(SecurityQuestions.class);
		criteria.add(Restrictions.ilike("questionId", questionId));
		List<SecurityQuestions> results = criteria.list();
		return results.get(0);
	}
	
	/**
	 * Gets the random security question.
	 * 
	 * @return the random security question
	 */
	public String getRandomSecurityQuestion() {
		String query = "select QUESTION from SECURITY_QUESTIONS";
		query += " order by rand() LIMIT 1";
		Session session = getSessionFactory().getCurrentSession();
		List<String> list = session.createSQLQuery(query).list();
		return list.get(0);
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.UserDAO#searchForNonTerminatedUser()
	 */
	@Override
	public List<User> searchForNonTerminatedUser() {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.ne("status.id", "2"));
		criteria.addOrder(Order.asc("lastName"));
		return criteria.list();
	}
	
	
	@Override
	public List<User> getAllUsers() {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(User.class);
		return criteria.list();
	}
	
	
}
