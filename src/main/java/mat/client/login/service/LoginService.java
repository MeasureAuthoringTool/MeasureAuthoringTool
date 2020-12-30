package mat.client.login.service;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import mat.client.login.LoginModel;
import mat.client.shared.MatException;

import java.util.List;
import java.util.Map;


/**
 * The Interface LoginService.
 */
@RemoteServiceRelativePath("loginService")
public interface LoginService extends RemoteService {


    void switchRole(String newRole);

    void switchUser(String accessToken, String newUserId) throws MatException;

    /**
     * Gets the footer ur ls.
     *
     * @return the footer ur ls
     */
    List<String> getFooterURLs();

    /**
     * Update on sign out.
     *
     * @param userId       the user id
     * @param email        the email
     * @param activityType the activity type
     * @return the string
     */
    String updateOnSignOut(String userId, String email, String activityType);

    /**
     * Checks if is valid password.
     *
     * @param userId   the user id
     * @param password the password
     * @return true, if is valid password
     */
    boolean isValidPassword(String userId, String password);

    /**
     * Retrieves MAT user details for provided HARP ID.
     *
     * @param harpUserInfo User's info
     * @return
     */

    Boolean checkForAssociatedHarpId(String harpId) throws MatException;


    String getSecurityQuestionToVerifyHarpUser(String loginId, String password) throws MatException;

    boolean verifyHarpUser(String securityQuestion, String securityAnswer, String loginId, String accessToken) throws MatException;
}
