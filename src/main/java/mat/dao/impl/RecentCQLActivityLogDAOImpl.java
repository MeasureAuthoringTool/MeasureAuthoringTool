package mat.dao.impl;

import mat.dao.RecentCQLActivityLogDAO;
import mat.dao.search.GenericDAO;
import mat.model.RecentCQLActivityLog;
import mat.server.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

@Repository("recentCQLActivityLogDAO")
public class RecentCQLActivityLogDAOImpl extends GenericDAO<RecentCQLActivityLog, String> implements RecentCQLActivityLogDAO {

	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(RecentCQLActivityLogDAOImpl.class);
	
	public RecentCQLActivityLogDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	@Override
	public void recordRecentCQLLibraryActivity(String cqlId, String userId) {
		
		//List of recently used measure by the given user in the descending order of timestamp(time).
		final List<RecentCQLActivityLog> recentlyUsedLibraries = getRecentCQLLibraryActivityLog(userId);
		
		if (recentlyUsedLibraries != null) {
			if (recentlyUsedLibraries.isEmpty()) {
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
		final RecentCQLActivityLog recentCQLActivityLog = new RecentCQLActivityLog();
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
	@Override
	public List<RecentCQLActivityLog> getRecentCQLLibraryActivityLog(String userId) {
		
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<RecentCQLActivityLog> query = cb.createQuery(RecentCQLActivityLog.class);
		final Root<RecentCQLActivityLog> root = query.from(RecentCQLActivityLog.class);
		
		query.select(root).where(cb.equal(root.get("userId"), userId));
		query.orderBy(cb.desc(root.get("time")));

		return session.createQuery(query).getResultList();
	}

}
