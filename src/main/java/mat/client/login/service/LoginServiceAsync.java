package mat.client.login.service;


import com.google.gwt.user.client.rpc.AsyncCallback;
import mat.client.login.LoginModel;

import java.util.List;
import java.util.Map;


/**
 * The Interface LoginServiceAsync.
 */
public interface LoginServiceAsync extends AsynchronousService {

    void switchRole(String role, AsyncCallback<Void> callback);

    void switchUser(Map<String, String> harpUserInfo, String newUserId, AsyncCallback<Void> callback);

    /**
     * Gets the footer ur ls.
     *
     * @param callback the callback
     * @return the footer ur ls
     */
    void getFooterURLs(AsyncCallback<List<String>> callback);

    /**
     * Update on sign out.
     *
     * @param userId       the user id
     * @param email        the email
     * @param activityType the activity type
     * @param callback     the callback
     */
    void updateOnSignOut(String userId, String email, String activityType,
                         AsyncCallback<String> callback);

    /**
     * Checks if is valid password.
     *
     * @param userId   the user id
     * @param password the password
     * @param callback the callback
     */
    void isValidPassword(String userId, String password,
                         AsyncCallback<Boolean> callback);

    /**
     * initializes session with MAT user details for provided HARP ID.
     *
     * @param harpUserInfo User info
     * @return
     * @Param callback
     */
    void initSession(Map<String, String> harpUserInfo, AsyncCallback<LoginModel> callback);

    void checkForAssociatedHarpId(String harpId, AsyncCallback<Boolean> async);

    void getSecurityQuestionToVerifyHarpUser(String loginId, String password, AsyncCallback<String> mapAsyncCallback);

    void verifyHarpUser(String securityQuestion, String securityAnswer, String loginId, Map<String, String> harpUserInfo, AsyncCallback<Boolean> booleanAsyncCallback);

}
