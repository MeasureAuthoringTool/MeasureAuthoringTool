package mat.dao.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import mat.dao.search.GenericDAO;
import mat.model.User;
import mat.model.UserSecurityQuestion;

/**
 * The Class UserSecurityQuestionDAO.
 */
public class UserSecurityQuestionDAO extends GenericDAO<UserSecurityQuestion, String> implements mat.dao.UserSecurityQuestionDAO{
	public void saveSecurityQuestions(UserSecurityQuestion securityQuestion, User user) {
		
		String sqlSelect = "SELECT QUESTION_ID FROM USER_SECURITY_QUESTIONS"
				+ " WHERE USER_ID = :userId and ROW_ID = :rowId";
		
		Session session = getSessionFactory().getCurrentSession();
		SQLQuery selectQuery = session.createSQLQuery(sqlSelect);
		selectQuery.setString("userId", user.getId());
		selectQuery.setString("rowId", securityQuestion.getRowId());
		List<String> list = selectQuery.list();
		if ((list == null) || list.isEmpty()) {
			//TODO add insert
		} else {
			String sqlUpdate = "UPDATE USER_SECURITY_QUESTIONS SET QUESTION_ID = :questionId, ANSWER = :answer, SALT = :salt where USER_ID = :userId and ROW_ID = :rowId";
			SQLQuery updateQuery = session.createSQLQuery(sqlUpdate);
			updateQuery.setString("questionId", securityQuestion.getSecurityQuestionId());
			updateQuery.setString("answer", securityQuestion.getSecurityAnswer());
			updateQuery.setString("salt", securityQuestion.getSalt());
			updateQuery.setString("userId", user.getId());
			updateQuery.setString("rowId", securityQuestion.getRowId());
			
			updateQuery.executeUpdate();
			
		}
		
	}
}
