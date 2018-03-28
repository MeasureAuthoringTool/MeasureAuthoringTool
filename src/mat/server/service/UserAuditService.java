package mat.server.service;

import java.util.List;

import mat.DTO.UserAuditLogDTO;


/**
 * The Interface UserAuditService.
 */
public interface UserAuditService {

	
	/**
	 * Record measure event.
	 *
	 * @param userId the user id
	 * @param event the event
	 * @param additionalInfo the additional info
	 * @param isChildLogRequired the is child log required
	 * @return true, if successful
	 */
	public boolean recordUserEvent(String userId, List<String> event, String additionalInfo, boolean isChildLogRequired);

	public List<UserAuditLogDTO> searchHistory(String userId);
	
}
