package mat.dao;

import java.util.List;

import mat.model.RecentCQLActivityLog;

public interface RecentCQLActivityLogDAO extends IDAO<RecentCQLActivityLog, String> {
	
	/**
	 * Records measure in recent CQL activity log for the given CQL ID and user ID. 
	 *
	 * @param CqlId the CQL id
	 * @param userId the user id
	 */
	void recordRecentCQLLibraryActivity(String cqlId, String userId);
	
	/**
	 * Gets recently used CQL by the given user ID in the descending order of time from recent measure activity log.
	 *
	 * @param userId the user id
	 * @return the recent measure activity log
	 */
	List<RecentCQLActivityLog> getRecentCQLLibraryActivityLog(String userId);
	
}
