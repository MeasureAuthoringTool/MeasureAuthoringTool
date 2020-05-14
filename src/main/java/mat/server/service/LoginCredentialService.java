package mat.server.service;

import mat.client.login.LoginModel;

import mat.client.shared.MatException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

/**
 * The Interface LoginCredentialService.
 */
public interface LoginCredentialService {
	
	/**
	 * Checks if is valid user.
	 * 
	 * @param userId
	 *            the user id
	 * @param password
	 *            the password
	 * @return the login model
	 */
	LoginModel isValidUser(String userId,String password, String oneTimePassword,String sessionId);
	
	/**
	 * Sign out Admin Users.
	 */
	void signOut();
	
	/**
	 * Change temp password.
	 * 
	 * @param userid
	 *            the userid
	 * @param changedpassword
	 *            the changedpassword
	 * @return the login model
	 */
	LoginModel changeTempPassword(String userid, String changedpassword);
	
	/**
	 * Change password security answers.
	 * 
	 * @param model
	 *            the model
	 * @return true, if successful
	 */
	boolean changePasswordSecurityAnswers(LoginModel model);
	
	/**
	 * Load user by username.
	 * 
	 * @param userId
	 *            the user id
	 * @return the user details
	 */
	UserDetails loadUserByUsername(String userId);
	
	/**
	 * Checks if is valid password.
	 * 
	 * @param loginId
	 *            the user id
	 * @param password
	 *            the password
	 * @return true, if is valid password
	 */
	boolean isValidPassword(String loginId, String password);

	/**
	 * Retrieves MAT user details for provided HARP ID and stores
	 * session ID that was generated client side.
	 *
	 * @param harpUserInfo User's HARP ID
	 * @param sessionId Session ID generated at login

	 * @return
	 */
	LoginModel initSession(Map<String, String> harpUserInfo, String sessionId);

	void saveHarpUserInfo(Map<String, String> harpUserInfo, String loginId, String sessionId) throws MatException;


	void switchRole(String newRole);
}
