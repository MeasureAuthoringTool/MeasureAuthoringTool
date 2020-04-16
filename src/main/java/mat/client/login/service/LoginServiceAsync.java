package mat.client.login.service;


import java.util.HashMap;
import java.util.List;

import mat.client.login.LoginModel;
import mat.model.SecurityQuestions;
import mat.model.UserSecurityQuestion;
import mat.shared.ForgottenLoginIDResult;
import mat.shared.ForgottenPasswordResult;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * The Interface LoginServiceAsync.
 */
public interface LoginServiceAsync extends AsynchronousService{

	/**
	 * Checks if is valid user.
	 * 
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @param oneTimePassword
	 * 			  one time password/security-code           
	 * @param callback
	 *            the callback
	 */
	void isValidUser(String username, String password, String oneTimePassword,
			AsyncCallback<LoginModel> callback);
	
	/**
	 * Forgot password.
	 * 
	 * @param email
	 *            the email
	 * @param securityQuestion
	 *            the security question
	 * @param securityAnswer
	 *            the security answer
	 * @param callback
	 *            the callback
	 */
	void forgotPassword(String email, 
			String securityQuestion, String securityAnswer, 
			AsyncCallback<ForgottenPasswordResult> callback);
	
	/**
	 * Sign out.
	 *
	 * @param callback
	 *            the callback
	 */
	void signOut(AsyncCallback<Void> callback);

	/**
	 * Change password security answers.
	 * 
	 * @param model
	 *            the model
	 * @param callback
	 *            the callback
	 */
	void changePasswordSecurityAnswers(LoginModel model,
			AsyncCallback<LoginResult> callback);
	
	/**
	 * Change temp password.
	 * 
	 * @param email
	 *            the email
	 * @param password
	 *            the password
	 * @param callback
	 *            the callback
	 */
	void  changeTempPassword(String email, String password, AsyncCallback<LoginModel> callback);
	
	/**
	 * Gets the security question options.
	 * 
	 * @param userid
	 *            the userid
	 * @param callback
	 *            the callback
	 * @return the security question options
	 */
	void getSecurityQuestionOptions(String userid, AsyncCallback<SecurityQuestionOptions> callback);
	
	/**
	 * Gets the security questions answers.
	 * 
	 * @param userid
	 *            the userid
	 * @param callback
	 *            the callback
	 * @return the security questions answers
	 */
	void getSecurityQuestionsAnswers(String userid,
			AsyncCallback<List<UserSecurityQuestion>> callback);
	
	/**
	 * Gets the footer ur ls.
	 * 
	 * @param callback
	 *            the callback
	 * @return the footer ur ls
	 */
	void getFooterURLs(AsyncCallback<List<String>> callback);
	
	/**
	 * Update on sign out.
	 * 
	 * @param userId
	 *            the user id
	 * @param email
	 *            the email
	 * @param activityType
	 *            the activity type
	 * @param callback
	 *            the callback
	 */
	void updateOnSignOut(String userId, String email, String activityType,
			AsyncCallback<String> callback);
	
	/**
	 * Gets the security question options for email.
	 * 
	 * @param email
	 *            the email
	 * @param callback
	 *            the callback
	 * @return the security question options for email
	 */
	void getSecurityQuestionOptionsForEmail(String email,
			AsyncCallback<SecurityQuestionOptions> callback);
	
	/**
	 * Forgot login id.
	 * 
	 * @param email
	 *            the email
	 * @param callback
	 *            the callback
	 */
	void forgotLoginID(String email,
			AsyncCallback<ForgottenLoginIDResult> callback);
	
	/**
	 * Gets the security question.
	 * 
	 * @param userid
	 *            the userid
	 * @param callback
	 *            the callback
	 * @return the security question
	 */
	void getSecurityQuestion(String userid, AsyncCallback<String> callback);
	
	/**
	 * Validate password.
	 *
	 * @param userID the user id
	 * @param newPassword the new password
	 * @param callback the callback
	 */
	void validatePassword(String userID, String newPassword,
			AsyncCallback<HashMap<String,String>> callback);


	/**
	 * Validate password creation date.
	 *
	 * @param userID the user id
	 * @param callback the callback
	 */
	void validatePasswordCreationDate(String userID,AsyncCallback<HashMap<String,String>> callback);
	/**
	 * Validate new password.
	 *
	 * @param userID the user id
	 * @param enteredPassword the entered password
	 * @param callback the callback
	 */
	void validateNewPassword(String userID,
			String enteredPassword, AsyncCallback<HashMap<String,String>> callback);
	/**
	 * Checks if is locked user.
	 * 
	 * @param loginId
	 *            the login id
	 * @param callback
	 *            the callback
	 */
	
	void isLockedUser(String loginId, AsyncCallback<Boolean> callback);
	
	/**
	 * Gets the security questions.
	 * 
	 * @param callback
	 *            the callback
	 * @return the security questions
	 */
	void getSecurityQuestions(AsyncCallback<List<SecurityQuestions>> callback);
	
	/**
	 * Checks if is valid password.
	 * 
	 * @param userId
	 *            the user id
	 * @param password
	 *            the password
	 * @param callback
	 *            the callback
	 */
	void isValidPassword(String userId, String password,
			AsyncCallback<Boolean> callback);

	/**
	 * Retrieves MAT user details for provided HARP ID and stores the
	 * session ID that was generated client side.
	 *
	 * @param harpId User email
	 * @param sessionId HTTP Session ID generated during MAT session set-up. See {@see mat.server.LoginServiceImpl#getUserDetailsByHarpId()}
	 * @Param callback
	 * @return
	 */
	void getUserDetailsByHarpId(String harpId, String accessToken, AsyncCallback<LoginModel> callback);
}
