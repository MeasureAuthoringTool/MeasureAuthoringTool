package mat.server.service.impl;

import mat.dao.UserAuditLogDAO;
import mat.dao.UserDAO;
import mat.dto.UserAuditLogDTO;
import mat.model.User;
import mat.server.service.UserAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAuditServiceImpl implements UserAuditService{

	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private UserAuditLogDAO userAuditLogDAO;
	
	
	@Override
	public boolean recordUserEvent(String userId, List<String> event,
			String additionalInfo, boolean isChildLogRequired) {
		boolean result = false;
		
		User user = userDAO.find(userId);
		//User user = userDAO.findByLoginId(userId);
		result = userAuditLogDAO.recordUserEvent(user, event, additionalInfo);
		
		return result;
	}
	
	
	@Override
	public List<UserAuditLogDTO> searchHistory(String userId){
		return userAuditLogDAO.searchHistory(userId);
	}

}
