package mat.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.search.GenericDAO;
import mat.model.UserSecurityQuestion;

@Repository("userSecurityQuestionDAO")
public class UserSecurityQuestionDAOImpl extends GenericDAO<UserSecurityQuestion, String> implements mat.dao.UserSecurityQuestionDAO{
	public UserSecurityQuestionDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
}
