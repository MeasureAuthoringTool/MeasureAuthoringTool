package mat.dao;

import java.util.List;

import mat.dto.UserAuditLogDTO;
import mat.model.User;
import mat.model.UserAuditLog;

public interface UserAuditLogDAO extends IDAO<UserAuditLog, String> {

	
	public boolean recordUserEvent(User user, List<String> event, String additionalInfo);
	
	public List<UserAuditLogDTO> searchHistory(String userId);
	
}
