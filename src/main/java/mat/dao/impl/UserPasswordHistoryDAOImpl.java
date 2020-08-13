package mat.dao.impl;

import mat.dao.UserPasswordHistoryDAO;
import mat.dao.search.GenericDAO;
import mat.model.User;
import mat.model.UserPasswordHistory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository("userPasswordHistoryDAO")
public class UserPasswordHistoryDAOImpl extends GenericDAO<UserPasswordHistory, String> implements UserPasswordHistoryDAO{

	public UserPasswordHistoryDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<UserPasswordHistory> getPasswordHistory(String userId){
		Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        final CriteriaQuery<UserPasswordHistory> criteriaQuery = criteriaBuilder.createQuery(UserPasswordHistory.class);
        final Root<UserPasswordHistory> userPasswordRoot = criteriaQuery.from(UserPasswordHistory.class);
        criteriaQuery.select(userPasswordRoot).where(criteriaBuilder.equal(userPasswordRoot.get("user").get("id"), userId));
		return session.createQuery(criteriaQuery).getResultList();
	}

	@Override
	public void addByUpdateUserPasswordHistory(User user) {
		String userPasswordHistoryId = getOldPasswordHistoryIdByCreationDate(user.getId());
		Session session = getSessionFactory().getCurrentSession();
		try {
			UserPasswordHistory userPasswordHistory = find(userPasswordHistoryId);
			userPasswordHistory.setPassword(user.getPassword().getPassword());
			userPasswordHistory.setSalt(user.getPassword().getSalt());
			userPasswordHistory.setCreatedDate(user.getPassword().getCreatedDate());
			session.update(userPasswordHistory);
		} finally {
		}
		
	}


	/**
	 * Gets the old password history id by creation date.
	 *
	 * @return the old password history id by creation date
	 */
	private String getOldPasswordHistoryIdByCreationDate(String userId) {
		Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        final CriteriaQuery<UserPasswordHistory> criteriaQuery = criteriaBuilder.createQuery(UserPasswordHistory.class);
        final Root<UserPasswordHistory> userPasswordRoot = criteriaQuery.from(UserPasswordHistory.class);
        criteriaQuery.orderBy(criteriaBuilder.asc(userPasswordRoot.get("createdDate")));
        criteriaQuery.select(userPasswordRoot.get("id")).where(criteriaBuilder.equal(userPasswordRoot.get("user").get("id"), userId));
		return String.valueOf(session.createQuery(criteriaQuery).getResultList().get(0));
	}


}
