package mat.server.service;

import java.util.Map;

import mat.client.login.LoginModel;
import mat.client.shared.MatException;

/**
 * The Interface LoginCredentialService.
 */
public interface LoginCredentialService {

    /**
     * Sign out Admin Users.
     */
    void signOut();

    /**
     * Checks if is valid password.
     *
     * @param loginId  the user id
     * @param password the password
     * @return true, if is valid password
     */
    boolean isValidPassword(String loginId, String password);

    /**
     * Retrieves MAT user details for provided HARP ID and stores
     * session ID that was generated client side.
     *
     * @param harpUserInfo User's HARP ID
     * @param sessionId    Session ID generated at login
     * @return
     */
    LoginModel initSession(Map<String, String> harpUserInfo, String sessionId);

    void saveHarpUserInfo(Map<String, String> harpUserInfo, String loginId, String sessionId) throws MatException;


    void switchRole(String newRole);

    void switchUser(Map<String, String> harpUserInfo, String newUserId, String sessionId);
}
