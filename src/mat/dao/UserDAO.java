package mat.dao;

import java.util.Date;
import java.util.List;

import mat.model.User;
import mat.server.model.MatUserDetails;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDAO extends IDAO<User, String> {
	public List<User> searchForUsersByName(String name, int startIndex, int numResults);
	public int countSearchResults(String text);
	public Boolean userExists(String userid);
	public User findByEmail(String email);
	public void unlockUsers(Date unlockDate);
	public void expireTemporaryPasswords(Date targetDate);
	public UserDetails getUser(String userId);
	public void saveUserDetails(MatUserDetails userdetails);
	public void setUserSignInDate(String userid);
	public void setUserSignOutDate(String userid);
	public User findByLoginId(String loginId);
	public boolean findUniqueLoginId(String loginId);
	public List<User> searchNonAdminUsers(String orgId, int i, int numResults);
	
}
