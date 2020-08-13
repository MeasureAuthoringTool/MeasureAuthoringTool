package mat.dao;

import mat.model.User;
import mat.model.UserPasswordHistory;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Interface UserPasswordHistoryDAO.
 */
public interface UserPasswordHistoryDAO  extends IDAO<UserPasswordHistory, String>{


	/**
	 * Gets the password history.
	 *
	 * @param userId the user id
	 * @return the password history
	 */
	List<UserPasswordHistory> getPasswordHistory(String userId);

	/**
	 * Adds the update password history.
	 *
	 * @param user the user
	 */
	void addByUpdateUserPasswordHistory(User user);
}
