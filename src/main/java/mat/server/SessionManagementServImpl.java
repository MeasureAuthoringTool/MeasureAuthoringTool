package mat.server;


import mat.client.login.service.CurrentUserInfo;
import mat.client.login.service.SessionManagementService;
import mat.client.login.service.ShortUserInfo;
import mat.dto.UserPreferenceDTO;
import mat.model.User;
import mat.model.UserPreference;
import mat.server.model.MatUserDetails;
import mat.server.service.MeasureLibraryService;
import mat.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;


public class SessionManagementServImpl extends SpringRemoteServiceServlet implements SessionManagementService {


    private static final long serialVersionUID = 1L;

    @Autowired
    private UserService userService;

    @Autowired
    private MeasureLibraryService measureLibraryService;

    @Override
    public CurrentUserInfo getCurrentUser() {
        CurrentUserInfo result = new CurrentUserInfo();
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
        result.organizationName = user.getOrganizationName();
        result.harpId = user.getHarpId();
        result.isFhirAccessible = user.getFhirFlag();
        UserPreference userPreference = user.getUserPreference();
        UserPreferenceDTO userPreferenceDTO = new UserPreferenceDTO();
        if (userPreference != null) {
            userPreferenceDTO.setFreeTextEditorEnabled(userPreference.isFreeTextEditorEnabled());
        }
        result.userPreference = userPreferenceDTO;
        result.users = userService.getAllActiveUserDetailsByHarpId(user.getHarpId()).stream()
                .map(this::convert)
                .collect(Collectors.toList());

        return result;
    }

    private ShortUserInfo convert(MatUserDetails d) {
        ShortUserInfo info = new ShortUserInfo();
        info.loginId = d.getLoginId();
        info.harpId = d.getHarpId();
        info.userEmail = d.getEmailAddress();
        info.userId = d.getId();
        info.userFirstName = d.getUsername();
        info.userLastName = d.getUserLastName();
        info.organizationName = d.getOrganization().getOrganizationName();
        info.role = d.getRoles().getDescription();
        return info;
    }

    @Override
    public String getCurrentReleaseVersion() {
        return getMeasureLibraryService().getCurrentReleaseVersion();
    }

    private MeasureLibraryService getMeasureLibraryService() {
        return measureLibraryService;
    }
}
