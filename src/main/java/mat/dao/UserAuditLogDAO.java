package mat.dao;

import mat.dto.UserAuditLogDTO;
import mat.model.User;
import mat.model.UserAuditLog;

import java.util.List;

public interface UserAuditLogDAO extends IDAO<UserAuditLog, String> {

	
	public boolean recordUserEvent(User user, List<String> event, String additionalInfo);
	
	public List<UserAuditLogDTO> searchHistory(String userId);
	
}
