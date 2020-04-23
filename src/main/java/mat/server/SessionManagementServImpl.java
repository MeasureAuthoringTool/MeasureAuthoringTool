package mat.server;


import org.springframework.beans.factory.annotation.Autowired;

import mat.DTO.UserPreferenceDTO;
import mat.client.login.service.SessionManagementService;
import mat.model.User;
import mat.model.UserPreference;
import mat.server.service.MeasureLibraryService;
import mat.server.service.UserService;


public class SessionManagementServImpl extends SpringRemoteServiceServlet implements SessionManagementService {


    private static final long serialVersionUID = 1L;

    @Autowired
    private UserService userService;

    @Autowired
    private MeasureLibraryService measureLibraryService;

    @Override
    public SessionManagementService.Result getCurrentUser() {
        SessionManagementService.Result result = new SessionManagementService.Result();
        result.userId = LoggedInUserUtil.getLoggedInUser();
        User user = userService.getById(result.userId);
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
        if (userPreference != null) {
            userPreferenceDTO.setFreeTextEditorEnabled(userPreference.isFreeTextEditorEnabled());
        }
        result.userPreference = userPreferenceDTO;
        return result;
    }

    @Override
    public void renewSession() {
        // do nothing
    }

    @Override
    public String getCurrentReleaseVersion() {
        return getMeasureLibraryService().getCurrentReleaseVersion();
    }

    private MeasureLibraryService getMeasureLibraryService() {
        return measureLibraryService;
    }
}
