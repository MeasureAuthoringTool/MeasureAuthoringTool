package mat.server;


import mat.DTO.UserPreferenceDTO;
import mat.client.login.service.SessionManagementService;
import mat.model.User;
import mat.model.UserPreference;
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
	public SessionManagementService.Result getCurrentUser() {
		SessionManagementService.Result result = new SessionManagementService.Result();
		result.userId = LoggedInUserUtil.getLoggedInUser();
		User user= getUserService().getById(result.userId);
		result.userRole = user.getSecurityRole().getDescription();
		result.loginId = user.getLoginId();
		result.userEmail = user.getEmailAddress();
		result.signInDate = user.getSignInDate();
		result.signOutDate = user.getSignOutDate();
		result.userFirstName = user.getFirstName();
		result.userLastName = user.getLastName();
		result.currentSessionId = getThreadLocalRequest().getSession().getId();
		result.activeSessionId = user.getSessionId();
		UserPreference userPreference = user.getUserPreference();
		UserPreferenceDTO userPreferenceDTO = new UserPreferenceDTO();
		if(userPreference != null) {
			userPreferenceDTO.setFreeTextEditorEnabled(userPreference.isFreeTextEditorEnabled());
		}
		result.userPreference = userPreferenceDTO;
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
