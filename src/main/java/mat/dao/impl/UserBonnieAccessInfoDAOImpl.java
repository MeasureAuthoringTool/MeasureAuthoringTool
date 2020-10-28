package mat.dao.impl;

import mat.dao.UserBonnieAccessInfoDAO;
import mat.dao.UserDAO;
import mat.dao.search.GenericDAO;
import mat.model.User;
import mat.model.UserBonnieAccessInfo;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("userBonnieAccessInfoDAO")
public class UserBonnieAccessInfoDAOImpl extends GenericDAO<UserBonnieAccessInfo, String> implements UserBonnieAccessInfoDAO{
	
	public UserBonnieAccessInfoDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	@Autowired
	private UserDAO userDAO;


	@Override
	public UserBonnieAccessInfo getUserBonnieAccessInfo(String userBonnieAccessId) {
		return find(userBonnieAccessId);
	}

	@Override
	public UserBonnieAccessInfo findByUserId(String userID) {
		User user = userDAO.find(userID);
		return user.getUserBonnieAccessInfo();
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

}
