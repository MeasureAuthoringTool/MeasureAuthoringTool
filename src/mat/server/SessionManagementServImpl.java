package mat.server;


import java.sql.Timestamp;

import mat.client.login.service.SessionManagementService;
import mat.model.User;
import mat.server.service.MeasureLibraryService;
import mat.server.service.UserService;


/**
 * The Class SessionManagementServImpl.
 */
public class SessionManagementServImpl extends SpringRemoteServiceServlet  implements SessionManagementService {


	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/* (non-Javadoc)
	 * @see mat.client.login.service.SessionManagementService#getCurrentUserRole()
	 */
	@Override
	public SessionManagementService.Result getCurrentUserRole() {
		SessionManagementService.Result result = new SessionManagementService.Result();
		result.sessionCreationTimestamp = new Timestamp(getThreadLocalRequest().getSession(false).getCreationTime());
		result.userId = LoggedInUserUtil.getLoggedInUser();
		User user= getUserService().getById(result.userId);
		result.userRole = user.getSecurityRole().getDescription();
		result.loginId = user.getLoginId();
		result.userEmail = user.getEmailAddress();
		result.signInDate = user.getSignInDate();
		result.signOutDate = user.getSignOutDate();
		result.userFirstName = user.getFirstName();
		return result;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.service.SessionManagementService#renewSession()
	 */
	@Override 
	public void renewSession() {
		// do nothing
	}

	/**
	 * Gets the user service.
	 * 
	 * @return the user service
	 */
	private UserService getUserService() {
		return (UserService)context.getBean("userService");
	}

	@Override
	public String getCurrentReleaseVersion(){
		return getMeasureLibraryService().getCurrentReleaseVersion();
	}
	
	private MeasureLibraryService getMeasureLibraryService(){
		return (MeasureLibraryService) context.getBean("measureLibraryService");
	}
}
