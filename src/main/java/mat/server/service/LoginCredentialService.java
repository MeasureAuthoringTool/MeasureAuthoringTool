package mat.server.service;

import mat.client.login.LoginModel;

import org.springframework.security.core.userdetails.UserDetails;

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
	public LoginModel isValidUser(String userId,String password, String oneTimePassword,String sessionId);
	
	/**
	 * Sign out Admin Users.
	 */
	public void signOut();
	
	/**
	 * Change temp password.
	 * 
	 * @param userid
	 *            the userid
	 * @param changedpassword
	 *            the changedpassword
	 * @return the login model
	 */
	public LoginModel changeTempPassword(String userid, String changedpassword);
	
	/**
	 * Change password security answers.
	 * 
	 * @param model
	 *            the model
	 * @return true, if successful
	 */
	public boolean changePasswordSecurityAnswers(LoginModel model);
	
	/**
	 * Load user by username.
	 * 
	 * @param userId
	 *            the user id
	 * @return the user details
	 */
	public UserDetails loadUserByUsername(String userId);
	
	/**
	 * Checks if is valid password.
	 * 
	 * @param userId
	 *            the user id
	 * @param password
	 *            the password
	 * @return true, if is valid password
	 */
	boolean isValidPassword(String userId, String password);

	/**
	 * Retrieves MAT user details for provided HARP ID and stores
	 * session ID that was generated client side.
	 *
	 * @param harpId User's HARP id
	 * @param sessionId Session Id generated at login
	 * @param accessToken HARP OAuth2 Access Token
	 * @return
	 */
	LoginModel getUserDetails(String harpId, String sessionId, String accessToken);
}
