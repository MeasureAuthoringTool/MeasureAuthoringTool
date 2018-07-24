package mat.dao.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
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
	public void saveOrUpdate(UserBonnieAccessInfo userBonnieAccessInfo) {
		Session session = null;
		Transaction transaction = null;
		try {			
			UserBonnieAccessInfo exsistingUserBonnieAccessInfo = findByUserId(userBonnieAccessInfo.getUser().getId());
			session = getSessionFactory().openSession();
			transaction = session.beginTransaction();
			if(exsistingUserBonnieAccessInfo == null) {
				super.save(userBonnieAccessInfo);
			} else {
				exsistingUserBonnieAccessInfo.setAccessToken(userBonnieAccessInfo.getAccessToken());
				exsistingUserBonnieAccessInfo.setRefreshToken(userBonnieAccessInfo.getRefreshToken());
				super.save(exsistingUserBonnieAccessInfo);
			}
			
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rollbackUncommitted(transaction);
			closeSession(session);
		}
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
