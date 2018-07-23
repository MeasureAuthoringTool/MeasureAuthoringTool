package mat.dao.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Transaction;

import mat.dao.search.GenericDAO;
import mat.model.UserBonnieAccessInfo;

public class UserBonnieAccessInfoDAO extends GenericDAO<UserBonnieAccessInfo, String> implements
mat.dao.UserBonnieAccessInfoDAO{

	@Override
	public Boolean userExists(String userId) {
		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<UserBonnieAccessInfo> criteria = builder.createQuery(UserBonnieAccessInfo.class);
		Root<UserBonnieAccessInfo> userBonnieAccessInfoRoot = criteria.from(UserBonnieAccessInfo.class);
		criteria.select(userBonnieAccessInfoRoot);
		criteria .where(builder.equal(userBonnieAccessInfoRoot.get("userId"), userId));
		List<UserBonnieAccessInfo> results = session.createQuery(criteria).getResultList();
		if(results.size() > 1 || results.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public UserBonnieAccessInfo getUserBonnieAccess(String userBonnieAccessId) {
		UserBonnieAccessInfo info  = find(userBonnieAccessId);
		return info;
	}

	@Override
	public void saveUserBonnieAccessDetails(UserBonnieAccessInfo userBonnieAccessInfo) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(userBonnieAccessInfo);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rollbackUncommitted(transaction);
			closeSession(session);
		}
	}

	@Override
	public void setAccessToken(String accessToken, String userBonnieAccessInfoId) {
		UserBonnieAccessInfo info  = find(userBonnieAccessInfoId);
		info.setAccessToken(accessToken);
		save(info);
	}

	@Override
	public void setRefreshToken(String refreshToken, String userBonnieAccessInfoId) {
		UserBonnieAccessInfo info  = find(userBonnieAccessInfoId);
		info.setRefreshToken(refreshToken);
		save(info);
	}

	@Override
	public UserBonnieAccessInfo findByUserId(String userID) {
		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<UserBonnieAccessInfo> criteria = builder.createQuery(UserBonnieAccessInfo.class);
		Root<UserBonnieAccessInfo> userBonnieAccessInfoRoot = criteria.from(UserBonnieAccessInfo.class);
		criteria.select(userBonnieAccessInfoRoot);
		criteria .where(builder.equal(userBonnieAccessInfoRoot.get("userId"), userID));
		List<UserBonnieAccessInfo> results = session.createQuery(criteria).getResultList();
		if(results.size() > 1 || results.size() == 0) {
			return null;
		} else {
			return results.get(0);
		}
	}

}
