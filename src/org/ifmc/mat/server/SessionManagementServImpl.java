package org.ifmc.mat.server;


import org.ifmc.mat.client.login.service.SessionManagementService;
import org.ifmc.mat.model.User;
import org.ifmc.mat.server.service.UserService;


public class SessionManagementServImpl extends SpringRemoteServiceServlet  implements SessionManagementService {


	private static final long serialVersionUID = 1L;
	
	@Override
	public SessionManagementService.Result getCurrentUserRole() {
		SessionManagementService.Result result = new SessionManagementService.Result();
		result.userId = LoggedInUserUtil.getLoggedInUser();
		result.userRole = LoggedInUserUtil.getLoggedInUserRole();
		
		User user= getUserService().getById(result.userId);
		result.userEmail = user.getEmailAddress();
		result.signInDate = user.getSignInDate();
		result.signOutDate = user.getSignOutDate();
		return result;
	}
	
	@Override 
	public void renewSession() {
		// do nothing
	}

	private UserService getUserService() {
		return (UserService)context.getBean("userService");
	}

}
