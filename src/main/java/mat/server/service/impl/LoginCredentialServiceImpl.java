package mat.server.service.impl;

import mat.client.login.LoginModel;
import mat.client.shared.MatException;
import mat.client.shared.MatRuntimeException;
import mat.dto.UserPreferenceDTO;
import mat.model.SecurityRole;
import mat.model.UserPreference;
import mat.server.LoggedInUserUtil;
import mat.server.hibernate.HibernateUserDetailService;
import mat.server.model.MatUserDetails;
import mat.server.service.LoginCredentialService;
import mat.server.service.UserService;
import mat.shared.HarpConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * The Class LoginCredentialServiceImpl.
 */
@Service
public class LoginCredentialServiceImpl implements LoginCredentialService {

    private static final Logger logger = LoggerFactory.getLogger(LoginCredentialServiceImpl.class);

    @Autowired
    private HibernateUserDetailService hibernateUserService;
    @Autowired
    private UserService userService;

    @Override
    public boolean isValidPassword(String loginId, String password) {
        logger.debug("LoginCredentialServiceImpl: isValidPassword start :  ");
        MatUserDetails userDetails = hibernateUserService.loadUserByUsername(loginId);
        if (userDetails != null) {
            String hashPassword = userService.getPasswordHash(userDetails.getUserPassword().getSalt(), password);
            if (hashPassword.equalsIgnoreCase(userDetails.getUserPassword().getPassword())) {
                logger.debug("LoginCredentialServiceImpl: isValidPassword end : password matched. ");
                return true;
            } else {
                logger.debug("LoginCredentialServiceImpl: isValidPassword end : password mismatched. ");
                return false;
            }
        } else {
            logger.debug("LoginCredentialServiceImpl: isValidPassword end : user detail null ");
            return false;
        }
    }

    public LoginModel initSession(Map<String, String> harpUserInfo, String sessionId) {
        String harpId = harpUserInfo.get(HarpConstants.HARP_ID);
        logger.debug("setUpUserSession::" + harpId + "::" + sessionId);
        MatUserDetails userDetails = hibernateUserService.loadUserByHarpId(harpId);

        if (userDetails == null) {
            throw new IllegalArgumentException("HARP_ID_NOT_FOUND");
        }

        updateUserDetails(harpUserInfo, userDetails, sessionId);

        // Set Authn Token. Used to retrieve user info.
        setAuthenticationToken(userDetails, harpUserInfo.get(HarpConstants.ACCESS_TOKEN));
        // Set and return user details to client.
        return loginModelSetter(new LoginModel(), userDetails);
    }

    @Override
    public void saveHarpUserInfo(Map<String, String> harpUserInfo, String loginId, String sessionId) throws MatException {
        try {
            MatUserDetails userDetails = hibernateUserService.loadUserByUsername(loginId);
            userDetails.setHarpId(harpUserInfo.get(HarpConstants.HARP_ID));

            updateUserDetails(harpUserInfo, userDetails, sessionId);

            setAuthenticationToken(userDetails, harpUserInfo.get(HarpConstants.ACCESS_TOKEN));
        } catch (Exception e) {
            throw new MatException("Unable to save Harp User Info");
        }
    }

    private void updateUserDetails(Map<String, String> harpUserInfo, MatUserDetails userDetails, String sessionId) {
        userDetails.setUsername(harpUserInfo.get(HarpConstants.HARP_GIVEN_NAME));
        userDetails.setUserLastName(harpUserInfo.get(HarpConstants.HARP_FAMILY_NAME));
        // Don't override different emails at different organizations for the same person.
//        userDetails.setEmailAddress(harpUserInfo.get(HarpConstants.HARP_PRIMARY_EMAIL_ID));
        userDetails.setSessionId(sessionId);
        hibernateUserService.saveUserDetails(userDetails);
    }

    /**
     * Login model setter.
     *
     * @param loginmodel  the login model
     * @param userDetails the user details
     * @return the login model
     */
    private LoginModel loginModelSetter(LoginModel loginmodel, MatUserDetails userDetails) {
        logger.debug("LoginCredentialServiceImpl::loginModelSetter::MatUserDetails::userId::" + userDetails.getId());
        LoginModel loginModel = loginmodel;
        loginModel.setRole(userDetails.getRoles());
        loginModel.setInitialPassword(userDetails.getUserPassword().isInitial());
        loginModel.setTemporaryPassword(userDetails.getUserPassword().isTemporaryPassword());
        loginModel.setUserId(userDetails.getId());
        loginModel.setEmail(userDetails.getEmailAddress());
        loginModel.setLoginId(userDetails.getLoginId());
        loginModel.setFirstName(userDetails.getUsername());
        UserPreference userPreference = userDetails.getUserPreference();
        UserPreferenceDTO userPreferenceDTO = new UserPreferenceDTO();
        if (userPreference != null) {
            userPreferenceDTO.setFreeTextEditorEnabled(userPreference.isFreeTextEditorEnabled());
        }
        loginModel.setUserPreference(userPreferenceDTO);
        return loginModel;
    }

    /**
     * Sets the pre-authentication token for HARP logins.
     *
     * @param userDetails the new authentication token
     */
    private void setAuthenticationToken(MatUserDetails userDetails, Object accessToken) {
        logger.debug("Setting authentication token::" + userDetails.getId() + " " + userDetails.getAuthorities());
        PreAuthenticatedAuthenticationToken auth =
                new PreAuthenticatedAuthenticationToken(userDetails.getId(), accessToken, userDetails.getAuthorities());
        auth.setDetails(userDetails);

        SecurityContext sc = new SecurityContextImpl();
        sc.setAuthentication(auth);
        SecurityContextHolder.setContext(sc);
    }

    public void switchRole(String newRole) {
        logger.debug("switchRole: " + newRole);
        if (SecurityRole.ADMIN_ROLE.equals(LoggedInUserUtil.getLoggedInUserRole())) {
            PreAuthenticatedAuthenticationToken token = LoggedInUserUtil.getToken();
            logger.debug("Updating authentication token: " + token.getName() + " with temporary role: " + newRole);
            PreAuthenticatedAuthenticationToken auth =
                    new PreAuthenticatedAuthenticationToken(token.getPrincipal(), token.getCredentials(), Arrays.asList(new SimpleGrantedAuthority(newRole)));
            auth.setDetails(token.getDetails());

            SecurityContext sc = new SecurityContextImpl();
            sc.setAuthentication(auth);
            SecurityContextHolder.setContext(sc);
        } else {
            // Don't throw exception. Looks like GWT can invoke the same method multiple times due to latency.
            // Only first call will succeed. Skip others.
            logger.warn("Only ADMINs can switch their role");
        }
    }

    public void switchUser(Map<String, String> harpUserInfo, String newUserId, String sessionId) {
        logger.debug("switchUser: " + newUserId);

        PreAuthenticatedAuthenticationToken token = LoggedInUserUtil.getToken();
        MatUserDetails userDetails = (MatUserDetails) token.getDetails();
        MatUserDetails newUserDetails = hibernateUserService.loadUserById(newUserId);
        if (!Objects.equals(userDetails.getHarpId(), newUserDetails.getHarpId())) {
            throw new MatRuntimeException("SWITCH_USER_HAS_OTHER_HARP_ID");
        }
        updateUserDetails(harpUserInfo, newUserDetails, sessionId);
        logger.debug("Changing Session ID: " + userDetails.getSessionId() + " -> " + newUserDetails.getSessionId());
        logger.debug("Changing Login ID: " + userDetails.getLoginId() + " -> " + newUserDetails.getLoginId());
        logger.debug("Changing role: " + userDetails.getRoles() + " -> " + newUserDetails.getRoles());

        setAuthenticationToken(newUserDetails, token.getCredentials());
    }

    @Override
    public void signOut() {
        SecurityContextHolder.clearContext();
    }

}
