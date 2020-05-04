package mat.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dto.UserAuditLogDTO;
import mat.dao.UserAuditLogDAO;
import mat.dao.search.GenericDAO;
import mat.model.User;
import mat.model.UserAuditLog;
import mat.server.LoggedInUserUtil;

@Repository("userAuditLogDAO")
public class UserAuditLogDAOImpl extends GenericDAO<UserAuditLog, String> implements UserAuditLogDAO {

	public UserAuditLogDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	@Override
	public boolean recordUserEvent(User user, List<String> event,
			String additionalInfo) {

		Session session = null;
		boolean result = false;
		try {

			for (int i = 0; i < event.size(); i++) {

				final UserAuditLog userAuditLog = new UserAuditLog();
				userAuditLog.setActionType("Administrator");
				userAuditLog.setTime(new Date());
				userAuditLog.setUser(user);
				userAuditLog.setUserEmail(LoggedInUserUtil
						.getLoggedInUserEmailAddress());
				userAuditLog.setAdditionalInfo(additionalInfo);
				session = getSessionFactory().getCurrentSession();
				userAuditLog.setActivityType(event.get(i));
				session.saveOrUpdate(userAuditLog);
			}
			result = true;
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<UserAuditLogDTO> searchHistory(String userId) {
		
		
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<UserAuditLogDTO> query = cb.createQuery(UserAuditLogDTO.class);
		final Root<UserAuditLog> root = query.from(UserAuditLog.class);
		
		query.select(cb.construct(
						UserAuditLogDTO.class, 
						 root.get("id"),
						 root.get("actionType"),
						 root.get("activityType"),
						 root.get("time").as(Date.class),
						 root.get("userEmail"),
						 root.get("user").get("id"),
						 root.get("additionalInfo")));
		
		query.where(cb.equal(root.get("user").get("id"), userId));
		query.orderBy(cb.desc(root.get("time")));

		return session.createQuery(query).getResultList();
	}

}
