package mat.server.service.impl;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import mat.DTO.UserPreferenceDTO;
import mat.client.login.LoginModel;
import mat.client.shared.MatException;
import mat.client.shared.MatRuntimeException;
import mat.model.SecurityRole;
import mat.model.UserPreference;
import mat.server.LoggedInUserUtil;
import mat.server.hibernate.HibernateUserDetailService;
import mat.server.model.MatUserDetails;
import mat.server.service.LoginCredentialService;
import mat.server.service.UserService;
import mat.shared.HarpConstants;

/**
 * The Class LoginCredentialServiceImpl.
 */
public class LoginCredentialServiceImpl implements LoginCredentialService {

    private static final Log logger = LogFactory.getLog(LoginCredentialServiceImpl.class);

    @Autowired
    private HibernateUserDetailService hibernateUserService;
    @Autowired
    private UserService userService;

    @Override
    public boolean isValidPassword(String loginId, String password) {
        logger.info("LoginCredentialServiceImpl: isValidPassword start :  ");
        MatUserDetails userDetails = hibernateUserService.loadUserByUsername(loginId);
        if (userDetails != null) {
            String hashPassword = userService.getPasswordHash(userDetails.getUserPassword().getSalt(), password);
            if (hashPassword.equalsIgnoreCase(userDetails.getUserPassword().getPassword())) {
                logger.info("LoginCredentialServiceImpl: isValidPassword end : password matched. ");
                return true;
            } else {
                logger.info("LoginCredentialServiceImpl: isValidPassword end : password mismatched. ");
                return false;
            }
        } else {
            logger.info("LoginCredentialServiceImpl: isValidPassword end : user detail null ");
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
        String fullName = harpUserInfo.get(HarpConstants.HARP_FULLNAME);
        userDetails.setUsername(fullName.substring(0, fullName.indexOf(" ")));
        userDetails.setUserLastName(fullName.substring(fullName.indexOf(" ")).trim());
        // Different emails at different organizatons for the same person.
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
        logger.info("LoginCredentialServiceImpl::loginModelSetter::MatUserDetails::userId::" + userDetails.getId());
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
        logger.info("Setting authentication token::" + userDetails.getId());
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
            logger.info("Updating authentication token: " + token.getName() + " with temporary role: " + newRole);
            PreAuthenticatedAuthenticationToken auth =
                    new PreAuthenticatedAuthenticationToken(token.getPrincipal(), token.getCredentials(), Arrays.asList(new SimpleGrantedAuthority(newRole)));
            auth.setDetails(token.getDetails());

            SecurityContext sc = new SecurityContextImpl();
            sc.setAuthentication(auth);
            SecurityContextHolder.setContext(sc);
        } else {
            throw new IllegalArgumentException("Only ADMINs can switch their role");
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
        logger.info("Changing Session ID: " + userDetails.getSessionId() + " -> " + newUserDetails.getSessionId());
        logger.info("Changing Login ID: " + userDetails.getLoginId() + " -> " + newUserDetails.getLoginId());
        logger.info("Changing role: " + userDetails.getRoles() + " -> " + newUserDetails.getRoles());

        setAuthenticationToken(newUserDetails, token.getCredentials());
    }

    @Override
    public void signOut() {
        SecurityContextHolder.clearContext();
    }

}