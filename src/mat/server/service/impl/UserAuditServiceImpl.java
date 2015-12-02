package mat.server.service.impl;

import mat.dao.UserAuditLogDAO;
import mat.dao.UserDAO;
import mat.model.User;
import mat.server.service.UserAuditService;

import org.springframework.beans.factory.annotation.Autowired;

public class UserAuditServiceImpl implements UserAuditService{

	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private UserAuditLogDAO userAuditLogDAO;
	
	
	@Override
	public boolean recordUserEvent(String userId, String event,
			String additionalInfo, boolean isChildLogRequired) {
		boolean result = false;
		
		//User user = userDAO.find(userId);
		User user = userDAO.findByLoginId(userId);
		result = userAuditLogDAO.recordUserEvent(user, event, additionalInfo);
		
		return result;
	}

}
