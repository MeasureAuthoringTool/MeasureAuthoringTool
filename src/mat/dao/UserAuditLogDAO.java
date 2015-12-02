package mat.dao;

import mat.model.User;
import mat.model.UserAuditLog;

public interface UserAuditLogDAO extends IDAO<UserAuditLog, String> {

	
	public boolean recordUserEvent(User user, String event, String additionalInfo);
	
}
