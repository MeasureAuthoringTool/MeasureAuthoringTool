package mat.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import mat.dao.UserDAO;
import mat.dao.search.GenericDAO;
import mat.model.Organization;
import mat.model.SecurityQuestions;
import mat.model.User;
import mat.model.UserSecurityQuestion;
import mat.server.model.MatUserDetails;


@Repository("userDAO")
public class UserDAOImpl extends GenericDAO<User, String> implements UserDAO {
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(UserDAOImpl.class);
	
	public UserDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
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
		Session session = getSessionFactory().getCurrentSession();
		try {
			int updatedCount = 0;
			final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
	        final CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
	        final Root<User> userRoot = criteriaQuery.from(User.class);
	        criteriaQuery.select(userRoot).where(criteriaBuilder.lessThan(userRoot.<Date>get("createdDate"), targetDate));
			List<User> results = session.createQuery(criteriaQuery).getResultList();
			for (User u : results) {
				u.getPassword().setPassword("expired");
				updatedCount++;
			}
			
			logger.info("Expired password count: " + updatedCount);
		} finally {
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.UserDAO#unlockUsers(java.util.Date)
	 */
	@Override
	public void unlockUsers(Date unlockDate) {
		Session session = getSessionFactory().getCurrentSession();
		try {
			int updatedCount = 0;
			final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			final CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
			final Root<User> userRoot = criteriaQuery.from(User.class);
	        criteriaQuery.select(userRoot).where(criteriaBuilder.lessThan(userRoot.<Date>get("lockedOutDate"), unlockDate));
	     	List<User> results = session.createQuery(criteriaQuery).getResultList();
			for (User u : results) {
				u.setLockedOutDate(null);
				if(u.getPassword() != null) {
					u.getPassword().setForgotPwdlockCounter(0);
					u.getPassword().setPasswordlockCounter(0);
				}
				updatedCount++;
			}
			logger.info("Unlocked user count: " + updatedCount);
		} finally {
		}
	}
	
	
	/**
	 * Creates the search criteria.
	 * @param userRoot 
	 * 
	 * @param text
	 *            the text
	 * @return the criteria
	 */
	private List<Predicate> createSearchCriteria(CriteriaBuilder criteriaBuilder,Root<User> userRoot, String text) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		Predicate predicate1 = criteriaBuilder.or(criteriaBuilder.or(
													criteriaBuilder.like(criteriaBuilder.lower(userRoot.get("firstName")), "%" + text.toLowerCase() + "%"),
													criteriaBuilder.like(criteriaBuilder.lower(userRoot.get("lastName")), "%" + text.toLowerCase() + "%")),
												  criteriaBuilder.or(
											        criteriaBuilder.like(criteriaBuilder.lower(userRoot.get("emailAddress")), "%" + text.toLowerCase() + "%"),
													criteriaBuilder.like(criteriaBuilder.lower(userRoot.get("loginId")), "%" + text.toLowerCase() + "%")));
		Predicate predicate2 = criteriaBuilder.and(criteriaBuilder.notEqual(userRoot.get("id"),"Admin"));
		predicates.add(predicate1);
		predicates.add(predicate2);
		return predicates;
	}
	
	/**
	 * Creates the search criteria non admin user.
	 * 
	 * @param text
	 *            the text
	 * @return the criteria
	 */
	private List<Predicate> createSearchCriteriaNonAdminUser(CriteriaBuilder criteriaBuilder,Root<User> userRoot, String text) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		Predicate predicate1 = criteriaBuilder.or(criteriaBuilder.or(
													criteriaBuilder.like(criteriaBuilder.lower(userRoot.get("firstName")), "%" + text.toLowerCase() + "%"),
													criteriaBuilder.like(criteriaBuilder.lower(userRoot.get("lastName")), "%" + text.toLowerCase() + "%")),
												  criteriaBuilder.or(
													criteriaBuilder.like(criteriaBuilder.lower(userRoot.get("emailAddress")), "%" + text.toLowerCase() + "%"),
													criteriaBuilder.like(criteriaBuilder.lower(userRoot.get("loginId")), "%" + text.toLowerCase() + "%")));
		Predicate predicate2 = criteriaBuilder.and(criteriaBuilder.notEqual(userRoot.get("securityRole").get("id"), "1"));
		Predicate predicate3 = criteriaBuilder.and(criteriaBuilder.notEqual(userRoot.get("status").get("statusId"), "2"));
		predicates.add(predicate1);
		predicates.add(predicate2);
		predicates.add(predicate3);
		return predicates;
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.UserDAO#searchForUsersByName(java.lang.String)
	 */
	@Override
	public List<User> searchForUsersByName(String name) {
		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        final Root<User> userRoot = criteriaQuery.from(User.class);
		List<Predicate> predicates = createSearchCriteria(criteriaBuilder,userRoot,name);
		criteriaQuery.orderBy(criteriaBuilder.asc(userRoot.get("lastName"))).select(userRoot).where(predicates.toArray(new Predicate[predicates.size()]));
		return session.createQuery(criteriaQuery).getResultList();
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.UserDAO#searchAllUsedOrganizations()
	 */
	@Override
	public HashMap<String,Organization> searchAllUsedOrganizations(){
		HashMap<String,Organization> usedOrganizationMap = new HashMap<String,Organization>();
		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Organization> criteriaQuery = criteriaBuilder.createQuery(Organization.class);
        final Root<User> userRoot = criteriaQuery.from(User.class);
        Join<Organization,User> join = userRoot.join("organization",JoinType.INNER);
        criteriaQuery.multiselect(join).distinct(true);

        List<Organization> usedOrganization =  session.createQuery(criteriaQuery).getResultList();
        
		for(Organization org : usedOrganization){
			usedOrganizationMap.put(Long.toString(org.getId()), org);
		}
		return usedOrganizationMap;
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.UserDAO#searchNonAdminUsers(java.lang.String, int, int)
	 */
	@Override
	public List<User> searchNonAdminUsers(String name, int startIndex,int numResults) {
		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        criteriaQuery.orderBy(criteriaBuilder.asc(userRoot.get("lastName")));
        List<Predicate> predicates=  createSearchCriteriaNonAdminUser(criteriaBuilder,userRoot,name);
        TypedQuery<User> typedQuery = session.createQuery(criteriaQuery.select(userRoot).where(predicates.toArray(new Predicate[predicates.size()])));
        typedQuery.setFirstResult(startIndex);
		if (numResults > 0) {
			typedQuery.setMaxResults(numResults);
		}
		return typedQuery.getResultList();
		
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.UserDAO#getAllNonAdminActiveUsers()
	 */
	@Override
	public List<User> getAllNonAdminActiveUsers() {
		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        List<Predicate> predicates = new ArrayList<>();
        Predicate predicate1 = criteriaBuilder.notEqual(userRoot.get("securityRole").get("id"), 1);
        Predicate predicate2 = criteriaBuilder.and(criteriaBuilder.equal(userRoot.get("status").get("statusId"), "1"));
        predicates.add(predicate1);
        predicates.add(predicate2);
        criteriaQuery.orderBy(criteriaBuilder.asc(userRoot.get("lastName")));
        criteriaQuery.select(userRoot).where(predicates.toArray(new Predicate[predicates.size()]));
		return  session.createQuery(criteriaQuery).getResultList();
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.UserDAO#getUser(java.lang.String)
	 */
	@Override
	public UserDetails getUser(String loginId) {
		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<MatUserDetails> criteriaQuery = criteriaBuilder.createQuery(MatUserDetails.class);
        Root<MatUserDetails> userRoot = criteriaQuery.from(MatUserDetails.class);
        criteriaQuery.select(userRoot).where(criteriaBuilder.equal(userRoot.get("loginId"), loginId));
		return (UserDetails) session.createQuery(criteriaQuery).uniqueResult();
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.UserDAO#userExists(java.lang.String)
	 */
	@Override
	public Boolean userExists(String emailAddress) {
		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(User.class))).where(criteriaBuilder.like(userRoot.get("emailAddress"), emailAddress));
		return session.createQuery(criteriaQuery).uniqueResult().intValue() > 0;
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.UserDAO#findUniqueLoginId(java.lang.String)
	 */
	@Override
	public boolean findUniqueLoginId(String loginId) {
		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(User.class))).where(criteriaBuilder.like(userRoot.get("loginId"), loginId));
		return session.createQuery(criteriaQuery).uniqueResult() > 0;
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.UserDAO#findByEmail(java.lang.String)
	 */
	@Override
	public User findByEmail(String email) {
		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        criteriaQuery.select(userRoot).where(criteriaBuilder.equal(userRoot.get("emailAddress"), email));
		List<User> results =  session.createQuery(criteriaQuery).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		} else {
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.UserDAO#findByLoginId(java.lang.String)
	 */
	@Override
	public User findByLoginId(String loginId) {
		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        criteriaQuery.select(userRoot).where(criteriaBuilder.equal(userRoot.get("loginId"), loginId));
		List<User> results =  session.createQuery(criteriaQuery).getResultList();
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
		try {
			session = getSessionFactory().getCurrentSession();
			session.saveOrUpdate(userdetails);
		} catch (Exception e) {
			logger.error(e);
		} finally {
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
			Session session = getSessionFactory().getCurrentSession();
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		    Root<UserSecurityQuestion> userSecurityQuestionsRoot = criteriaQuery.from(UserSecurityQuestion.class);
		    Root<SecurityQuestions> securityQuestionsRoot = criteriaQuery.from(SecurityQuestions.class);
		    Root<User> userRoot = criteriaQuery.from(User.class);
		    criteriaQuery.select(criteriaBuilder.count(userSecurityQuestionsRoot)).where(criteriaBuilder.and(
		    		criteriaBuilder.equal(userSecurityQuestionsRoot.get("securityQuestionId"), securityQuestionsRoot.get("questionId")),
		    		criteriaBuilder.equal(userSecurityQuestionsRoot.get("userId"), userRoot.get("id")),
		    		criteriaBuilder.equal(userRoot.get("id"), user.getId())));
		    int count = session.createQuery(criteriaQuery).uniqueResult().intValue();
		    CriteriaQuery<UserSecurityQuestion> securityQuestionsQuery = criteriaBuilder.createQuery(UserSecurityQuestion.class);
		    userSecurityQuestionsRoot = securityQuestionsQuery.from(UserSecurityQuestion.class);
		    securityQuestionsQuery.select(userSecurityQuestionsRoot);
			question=session.createQuery(securityQuestionsQuery).setFirstResult(new Random().nextInt(count)).setMaxResults(1).uniqueResult().getSecurityQuestions().getQuestion();
		}
		
		return question;
	}
	
	/**
	 * Gets the random security question.
	 * 
	 * @return the random security question
	 */
	public String getRandomSecurityQuestion() {
		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(SecurityQuestions.class)));
		int count = session.createQuery(criteriaQuery).uniqueResult().intValue();
		CriteriaQuery<SecurityQuestions> securityQuestionsQuery = criteriaBuilder.createQuery(SecurityQuestions.class);
		Root<SecurityQuestions> securityQuestionsRoot = securityQuestionsQuery.from(SecurityQuestions.class);
		securityQuestionsQuery.select(securityQuestionsRoot);
		return session.createQuery(securityQuestionsQuery).setFirstResult(new Random().nextInt(count)).setMaxResults(1).uniqueResult().getQuestion();
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.UserDAO#searchForNonTerminatedUser()
	 */
	@Override
	public List<User> searchForNonTerminatedUser() {
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		final CriteriaQuery<User> userCriteriaQuery = criteriaBuilder.createQuery(User.class);
		final Root<User> userRoot = userCriteriaQuery.from(User.class);
		userCriteriaQuery.select(userRoot).where(criteriaBuilder.notEqual(userRoot.get("status").get("statusId"), "2"));
		userCriteriaQuery.orderBy(criteriaBuilder.asc(userRoot.get("lastName")));
		return session.createQuery(userCriteriaQuery).getResultList();
	}
	
	
	@Override
	public List<User> getAllUsers() {
		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> userCriteriaQuery = criteriaBuilder.createQuery(User.class);
        final Root<User> userRoot = userCriteriaQuery.from(User.class);
        userCriteriaQuery.select(userRoot);
		return session.createQuery(userCriteriaQuery).getResultList();
	}
	
	
}
