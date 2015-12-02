package mat.dao.impl;

import java.util.Date;

import mat.dao.search.GenericDAO;
import mat.model.User;
import mat.model.UserAuditLog;
import mat.server.LoggedInUserUtil;

import org.hibernate.Session;

// TODO: Auto-generated Javadoc
/**
 * The Class UserAuditLogDAO.
 */
public class UserAuditLogDAO extends GenericDAO<UserAuditLog, String> implements
		mat.dao.UserAuditLogDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.dao.UserAuditLogDAO#recordMeasureEvent(mat.model.User,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public boolean recordUserEvent(User user, String event,
			String additionalInfo) {

		Session session = null;
		boolean result = false;
		try {

			String userRole = "";

			if (user.getSecurityRole().getId().equals("1")) {
				userRole = "Admin";
			} else {
				userRole = "User";
			}

			UserAuditLog userAuditLog = new UserAuditLog();
			userAuditLog.setActionType(userRole);
			userAuditLog.setTime(new Date());
			userAuditLog.setUser(user);
			userAuditLog.setUserEmail(LoggedInUserUtil
					.getLoggedInUserEmailAddress());
			userAuditLog.setAdditionalInfo(additionalInfo);
			session = getSessionFactory().getCurrentSession();
			userAuditLog.setActivityType(event);
			session.saveOrUpdate(userAuditLog);

			result = true;
		} catch (Exception e) { // TODO: handle application exception
			e.printStackTrace();
		}
		return result;
	}

}
