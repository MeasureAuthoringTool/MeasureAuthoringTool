package mat.server.service.jobs;

import java.util.List;

import mat.dao.MatFlagDAO;
import mat.dao.UserDAO;
import mat.model.MatFlag;
import mat.model.User;
import mat.model.UserPassword;
import mat.server.service.UserService;
import mat.server.service.impl.UserServiceImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OnetimeUserNotificationTask {
	
	private static final Log logger = LogFactory.getLog(OnetimeUserNotificationTask.class);
	private UserDAO userDAO;
	private UserService userService;
	private MatFlagDAO matFlagDAO;
	
	public void sendOnetimeUserNotificationEmails(){
		System.out.println("Hi there this is OnetimeUserNotificationTask..");
		
		List<User> users = userDAO.find();
		
		if(doesJobNeedExecution()){
			sendUserLoginIdEmail(users);
			sendUserNewTempPasswordEmail(users);
		}
		
	}

	private void sendUserLoginIdEmail(List<User> users) {
	
		for(User user:users){
//			user.setEmailAddress("cbajikar@ifmc.org");
			((UserServiceImpl)userService).notifyUserOfNewAccount(user);
//			break;
			
		}
		
	}
	
	private void sendUserNewTempPasswordEmail(List<User> users) {
		for(User user:users){
			userService.requestResetLockedPassword(user.getLoginId());
//			user.setEmailAddress("cbajikar@ifmc.org");
//			String newPassword = userService.generateRandomPassword();
//			((UserServiceImpl)userService).notifyUserOfTemporaryPassword(user, newPassword);
//			break;
		}
		
	}
	
	private boolean doesJobNeedExecution() {
		boolean result = false;
		
		MatFlag flag = matFlagDAO.find().get(0);
		if(flag.getFlag().equals("0")){
			flag.setFlag("1");
			matFlagDAO.save(flag);
			result=true;
		}
				
		return result;
	}
	
	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserService getUserService() {
		return userService;
	}
	
	public MatFlagDAO getMatFlagDAO() {
		return matFlagDAO;
	}

	public void setMatFlagDAO(MatFlagDAO matFlagDAO) {
		this.matFlagDAO = matFlagDAO;
	}

}
