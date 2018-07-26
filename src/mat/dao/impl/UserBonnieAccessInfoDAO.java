package mat.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;

import mat.dao.UserDAO;
import mat.dao.search.GenericDAO;
import mat.model.User;
import mat.model.UserBonnieAccessInfo;

public class UserBonnieAccessInfoDAO extends GenericDAO<UserBonnieAccessInfo, String> implements
mat.dao.UserBonnieAccessInfoDAO{
	
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
