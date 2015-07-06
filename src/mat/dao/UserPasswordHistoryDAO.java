package mat.dao;

import java.util.Date;
import java.util.List;

import mat.model.UserPasswordHistory;

// TODO: Auto-generated Javadoc
/**
 * The Interface UserPasswordHistoryDAO.
 */
public interface UserPasswordHistoryDAO  extends IDAO<UserPasswordHistory, String>{


	List<UserPasswordHistory> getPasswordHistory(String userId);

	Date getOldPasswordCreationDate(String userId);
}
