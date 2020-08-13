package mat.dao.impl;

import mat.dao.search.GenericDAO;
import mat.model.RecentMSRActivityLog;
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

@Repository("recentMSRActivityLogDAO")
public class RecentMSRActivityLogDAOImpl extends GenericDAO<RecentMSRActivityLog, String> implements mat.dao.RecentMSRActivityLogDAO {

	private static final Log logger = LogFactory.getLog(RecentMSRActivityLogDAOImpl.class);
	
	public RecentMSRActivityLogDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	@Override
	public void recordRecentMeasureActivity(String measureId, String userId) {
		
		//List of recently used measure by the given user in the descending order of timestamp(time).
		final List<RecentMSRActivityLog> recentlyUsedMeasures = getRecentMeasureActivityLog(userId);
		
		if (recentlyUsedMeasures != null) {
			if (recentlyUsedMeasures.isEmpty()) {
				saveRecentMeasureActivity(measureId, userId);
			} else if (recentlyUsedMeasures.size() == 1) {
				if (recentlyUsedMeasures.get(0).getMeasureId().equalsIgnoreCase(measureId)) {
					updateRecentMeasureActivity(recentlyUsedMeasures.get(0));
				} else {
					saveRecentMeasureActivity(measureId, userId);
				}
			} else if (recentlyUsedMeasures.size() >= 2) {
				if (recentlyUsedMeasures.get(0).getMeasureId().equalsIgnoreCase(measureId)) {
					updateRecentMeasureActivity(recentlyUsedMeasures.get(0));
				} else if (recentlyUsedMeasures.get(1).getMeasureId().equalsIgnoreCase(measureId)) {
					updateRecentMeasureActivity(recentlyUsedMeasures.get(1));
				} else {
					for (int i = 1; i < recentlyUsedMeasures.size(); i++) {
						delete(recentlyUsedMeasures.get(i));
					}
					saveRecentMeasureActivity(measureId, userId);
				}
			}
		}		
	}
	
	/**
	 * Save measure in recent measure activity log for the given measure ID and user ID. 
	 *
	 * @param measureId the measure id
	 * @param userId the user id
	 */
	private void saveRecentMeasureActivity(String measureId, String userId) {
		final RecentMSRActivityLog recentMSRActivityLog = new RecentMSRActivityLog();
		recentMSRActivityLog.setMeasureId(measureId);
		recentMSRActivityLog.setUserId(userId);
		recentMSRActivityLog.setTime(new Date());
		save(recentMSRActivityLog);
		logger.info("Inserting recent measure activity log for measure id: " + measureId);
	}
	
	/**
	 * Update recent measure with the current timestamp in recent measure activity log.
	 *
	 * @param recentMSRActivityLog the recent measure activity log
	 */
	private void updateRecentMeasureActivity(RecentMSRActivityLog recentMSRActivityLog) {
		recentMSRActivityLog.setTime(new Date());
		save(recentMSRActivityLog);
		logger.info("Updating recent measure activity log for measure id: " + recentMSRActivityLog.getMeasureId());
	}
	
	/**
	 * Gets recently used measures by the given user ID in the descending order of time from recent measure activity log.
	 *
	 * @param userId the user id
	 * @return the list of recently used measures
	 */
	@Override
	public List<RecentMSRActivityLog> getRecentMeasureActivityLog(String userId) {
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<RecentMSRActivityLog> query = cb.createQuery(RecentMSRActivityLog.class);
		final Root<RecentMSRActivityLog> root = query.from(RecentMSRActivityLog.class);
		
		query.select(root).where(cb.equal(root.get("userId"), userId));
		query.orderBy(cb.desc(root.get("time")));
		
		return session.createQuery(query).getResultList();
	}

}
