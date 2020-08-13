package mat.dao;

import mat.model.RecentMSRActivityLog;

import java.util.List;

public interface RecentMSRActivityLogDAO extends IDAO<RecentMSRActivityLog, String> {
	
	/**
	 * Records measure in recent measure activity log for the given measure ID and user ID. 
	 *
	 * @param measureId the measure id
	 * @param userId the user id
	 */
	void recordRecentMeasureActivity(String measureId, String userId);
	
	/**
	 * Gets recently used measures by the given user ID in the descending order of time from recent measure activity log.
	 *
	 * @param userId the user id
	 * @return the recent measure activity log
	 */
	List<RecentMSRActivityLog> getRecentMeasureActivityLog(String userId);
	
}
