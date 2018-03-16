package mat.dao;

import mat.model.User;
import mat.model.UserSecurityQuestion;

/**
 * The Interface UserSecurityQuestionDAO.
 */
public interface UserSecurityQuestionDAO extends IDAO<UserSecurityQuestion, String> {

	void saveSecurityQuestions(UserSecurityQuestion userSecurityQuestion, User user);
}
