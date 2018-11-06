package mat.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.RecentCQLActivityLogDAO;
import mat.dao.search.GenericDAO;
import mat.model.RecentCQLActivityLog;

@Repository
public class RecentCQLActivityLogDAOImpl extends GenericDAO<RecentCQLActivityLog, String> implements RecentCQLActivityLogDAO {

	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(RecentCQLActivityLogDAOImpl.class);
	
	public RecentCQLActivityLogDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	@Override
	public void recordRecentCQLLibraryActivity(String cqlId, String userId) {
		
		//List of recently used measure by the given user in the descending order of timestamp(time).
		List<RecentCQLActivityLog> recentlyUsedLibraries = getRecentCQLLibraryActivityLog(userId);
		
		if (recentlyUsedLibraries != null) {
			if (recentlyUsedLibraries.size() == 0) {
				saveRecentCQLActivity(cqlId, userId);
			} else if (recentlyUsedLibraries.size() == 1) {
				if (recentlyUsedLibraries.get(0).getCqlId().equalsIgnoreCase(cqlId)) {
					updateRecentCQLActivity(recentlyUsedLibraries.get(0));
				} else {
					saveRecentCQLActivity(cqlId, userId);
				}
			} else if (recentlyUsedLibraries.size() >= 2) {
				if (recentlyUsedLibraries.get(0).getCqlId().equalsIgnoreCase(cqlId)) {
					updateRecentCQLActivity(recentlyUsedLibraries.get(0));
				} else if (recentlyUsedLibraries.get(1).getCqlId().equalsIgnoreCase(cqlId)) {
					updateRecentCQLActivity(recentlyUsedLibraries.get(1));
				} else {
					for (int i = 1; i < recentlyUsedLibraries.size(); i++) {
						delete(recentlyUsedLibraries.get(i));
					}
					saveRecentCQLActivity(cqlId, userId);
				}
			}
		}		
	}
	
	/**
	 * Save cql in recent cql activity log for the given cql ID and user ID. 
	 *
	 * @param cqlId the cql id
	 * @param userId the user id
	 */
	private void saveRecentCQLActivity(String cqlId, String userId) {
		RecentCQLActivityLog recentCQLActivityLog = new RecentCQLActivityLog();
		recentCQLActivityLog.setCqlId(cqlId);
		recentCQLActivityLog.setUserId(userId);
		recentCQLActivityLog.setTime(new Date());
		save(recentCQLActivityLog);
		logger.info("Inserting recent cql activity log for cql id: " + cqlId);
	}
	
	/**
	 * Update recent cql with the current timestamp in recent cql activity log.
	 *
	 * @param recentCQLActivityLog the recent cql activity log
	 */
	private void updateRecentCQLActivity(RecentCQLActivityLog recentCQLActivityLog) {
		recentCQLActivityLog.setTime(new Date());
		save(recentCQLActivityLog);
		logger.info("Updating recent cql activity log for cql id: " + recentCQLActivityLog.getCqlId());
	}
	
	/**
	 * Gets recently used measures by the given user ID in the descending order of time from recent measure activity log.
	 *
	 * @param userId the user id
	 * @return the list of recently used measures
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RecentCQLActivityLog> getRecentCQLLibraryActivityLog(String userId) {
		List<RecentCQLActivityLog> recentlyUsedLibraries;
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(RecentCQLActivityLog.class);
		criteria.add(Restrictions.eq("userId", userId));
		criteria.addOrder(Order.desc("time"));
		recentlyUsedLibraries = criteria.list();
		return recentlyUsedLibraries;
	}

}
