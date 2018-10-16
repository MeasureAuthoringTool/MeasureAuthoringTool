package mat.dao;

import mat.model.UserPassword;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * The Interface UserPasswordDAO.
 */
public interface UserPasswordDAO extends IDAO<UserPassword, String> {
	
	/**
	 * Gets the user password info.
	 * 
	 * @param userId
	 *            the user id
	 * @return the user password info
	 * @throws UsernameNotFoundException
	 *             the username not found exception
	 */
	public UserPassword getUserPasswordInfo(String userId)throws UsernameNotFoundException;
}
