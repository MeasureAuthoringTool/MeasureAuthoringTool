package mat.client.login.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import mat.client.login.LoginModel;
import mat.client.shared.MatException;
import mat.model.SecurityQuestions;
import mat.model.UserSecurityQuestion;
import mat.shared.ForgottenLoginIDResult;
import mat.shared.ForgottenPasswordResult;


/**
 * The Interface LoginService.
 */
@RemoteServiceRelativePath("loginService")
public interface LoginService extends RemoteService {

    /**
     * Checks if is valid user.
     *
     * @param username        the username
     * @param password        the password
     * @param oneTimePassword 2 factor one time password/security-code
     * @return the login model
     */
    LoginModel isValidUser(String username, String password, String oneTimePassword);

	void switchRole(String newRole);

    /**
     * Forgot password.
     *
     * @param email            the email
     * @param securityQuestion the security question
     * @param securityAnswer   the security answer
     * @return the forgotten password result
     */
    ForgottenPasswordResult forgotPassword(String email,
                                           String securityQuestion, String securityAnswer);

    /**
     * Change password security answers.
     *
     * @param model the model
     * @return the login result
     */
    LoginResult changePasswordSecurityAnswers(LoginModel model);

    /**
     * Sign out.
     */
    void signOut();

    /**
     * Change temp password.
     *
     * @param email    the email
     * @param password the password
     * @return the login model
     */
    LoginModel changeTempPassword(String email, String password);

    /**
     * Gets the security question options.
     *
     * @param userid the userid
     * @return the security question options
     */
    SecurityQuestionOptions getSecurityQuestionOptions(String userid);

    /**
     * Gets the security questions answers.
     *
     * @param userid the userid
     * @return the security questions answers
     */
    List<UserSecurityQuestion> getSecurityQuestionsAnswers(String userid);

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
     * Gets the security question options for email.
     *
     * @param email the email
     * @return the security question options for email
     */
    SecurityQuestionOptions getSecurityQuestionOptionsForEmail(String email);

    /**
     * Forgot login id.
     *
     * @param email the email
     * @return the forgotten login id result
     */
    ForgottenLoginIDResult forgotLoginID(String email);

    /**
     * Gets the security question.
     *
     * @param userid the userid
     * @return the security question
     */
    String getSecurityQuestion(String userid);

    /**
     * Validate password.
     *
     * @param userID          the user id
     * @param enteredPassword the entered password
     * @return the hash map
     */
    HashMap<String, String> validatePassword(String userID, String enteredPassword);

    /**
     * Validate password creation date.
     *
     * @param userID the user id
     * @return the hash map
     */
    HashMap<String, String> validatePasswordCreationDate(String userID);

    /**
     * Validate new password.
     *
     * @param userID      the user id
     * @param newPassword the new password
     * @return the hash map
     */
    HashMap<String, String> validateNewPassword(String userID,
                                                       String newPassword);

    /**
     * Checks if is locked user.
     *
     * @param loginId the login id
     * @return true, if is locked user
     */
    boolean isLockedUser(String loginId);

    /**
     * Checks if the HARP user is locked out of the MAT.
     *
     * @param harpId the user's harp id
     * @return true, if is locked user
     */
    boolean isHarpUserLocked(String harpId);


    /**
     * Gets the security questions.
     *
     * @return the security questions
     */
    List<SecurityQuestions> getSecurityQuestions();

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

    LoginModel initSession(Map<String, String> harpUserInfo) throws MatException;

    Boolean checkForAssociatedHarpId(String harpId) throws MatException;


    String getSecurityQuestionToVerifyHarpUser(String loginId, String password) throws MatException;

    boolean verifyHarpUser(String securityQuestion, String securityAnswer, String loginId, Map<String, String> harpUserInfo) throws MatException;
}
