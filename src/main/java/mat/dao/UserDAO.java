package mat.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import mat.model.Organization;
import mat.model.User;
import mat.server.model.MatUserDetails;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserDAO extends IDAO<User, String> {
	
	/**
	 * Search for users by name.
	 * 
	 * @param name
	 *            the name
	 * @return the list
	 */
	List<User> searchForUsersByName(String name);
	
	/**
	 * Search for non terminated user.
	 *
	 * @return the list
	 */
	List<User> searchForNonTerminatedUser();

	/**
	 * Get user details by Email.
	 * @param email User's email.
	 * @return UserDetails for setting login values.
	 */
    UserDetails getUserDetailsByHarpId(String harpId);

    /**
	 * User exists.
	 * 
	 * @param userid
	 *            the userid
	 * @return the boolean
	 */
    Boolean userExists(String userid);
	
	/**
	 * Find by email.
	 * 
	 * @param email
	 *            the email
	 * @return the user
	 */
	User findByEmail(String email);
	
	/**
	 * Unlock users.
	 * 
	 * @param unlockDate
	 *            the unlock date
	 */
	void unlockUsers(Date unlockDate);
	
	/**
	 * Expire temporary passwords.
	 * 
	 * @param targetDate
	 *            the target date
	 */
	void expireTemporaryPasswords(Date targetDate);
	
	/**
	 * Gets the user.
	 * 
	 * @param userId
	 *            the user id
	 * @return the user
	 */
	UserDetails getUser(String loginId);
	
	/**
	 * Save user details.
	 * 
	 * @param userdetails
	 *            the userdetails
	 */
	void saveUserDetails(MatUserDetails userdetails);
	
	/**
	 * Sets the user sign in date.
	 * 
	 * @param userid
	 *            the new user sign in date
	 */
	void setUserSignInDate(String userid);
	
	/**
	 * Sets the user sign out date.
	 * 
	 * @param userid
	 *            the new user sign out date
	 */
	void setUserSignOutDate(String userid);
	
	/**
	 * Find by login id.
	 * 
	 * @param loginId
	 *            the login id
	 * @return the user
	 */
	User findByLoginId(String loginId);
	
	/**
	 * Find unique login id.
	 * 
	 * @param loginId
	 *            the login id
	 * @return true, if successful
	 */
	boolean findUniqueLoginId(String loginId);
	
	/**
	 * Search non admin users.
	 * 
	 * @param orgId
	 *            the org id
	 * @param i
	 *            the i
	 * @param numResults
	 *            the num results
	 * @return the list
	 */
	List<User> searchNonAdminUsers(String orgId, int i, int numResults);
	
	/**
	 * Gets the random security question.
	 * 
	 * @param loginId
	 *            the user id
	 * @return the random security question
	 */
	String getRandomSecurityQuestion(String loginId);
	
	/**
	 * Gets the all non admin active users.
	 * 
	 * @return the all non admin active users
	 */
	List<User> getAllNonAdminActiveUsers();
	
	/**
	 * Search all used organizations.
	 *
	 * @return the hash map
	 */
	HashMap<String, Organization> searchAllUsedOrganizations();
	
	List<User> getAllUsers();
	
	List<User> getUsersListForSharingMeasureOrLibrary(String userName);

    boolean findAssociatedHarpId(String harpId);

    User findByHarpId(String harpId);
}
