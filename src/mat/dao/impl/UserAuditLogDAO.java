package mat.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mat.DTO.SearchHistoryDTO;
import mat.DTO.UserAuditLogDTO;
import mat.dao.search.GenericDAO;
import mat.model.User;
import mat.model.UserAuditLog;
import mat.server.LoggedInUserUtil;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

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
	public boolean recordUserEvent(User user, List<String> event,
			String additionalInfo) {

		Session session = null;
		boolean result = false;
		try {

			for (int i = 0; i < event.size(); i++) {

				UserAuditLog userAuditLog = new UserAuditLog();
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
		} catch (Exception e) { // TODO: handle application exception
			e.printStackTrace();
		}
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<UserAuditLogDTO> searchHistory(String userId) {
		
		List<UserAuditLogDTO> logResults = new ArrayList<UserAuditLogDTO>();
		Session session = getSessionFactory().getCurrentSession();
		Criteria logCriteria = session.createCriteria(UserAuditLog.class);
		logCriteria.add(Restrictions.eq("user.id",userId));
		logCriteria.addOrder(Order.desc("time"));
		List<UserAuditLog> results = logCriteria.list();
		for (UserAuditLog auditLog : results) {
			UserAuditLogDTO dto = new UserAuditLogDTO();
			dto.setId(auditLog.getId());
			dto.setActivityType(auditLog.getActivityType());
			dto.setActionType(auditLog.getActionType());
			dto.setAdditionalInfo(auditLog.getAdditionalInfo());
			dto.setTime(auditLog.getTime());
			dto.setUserEmail(auditLog.getUserEmail());
			dto.setId(auditLog.getId());
			logResults.add(dto);
		}

		return logResults;
	}

}
