package mat.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import mat.dao.search.GenericDAO;
import mat.model.User;
import mat.model.UserPasswordHistory;

// TODO: Auto-generated Javadoc
/**
 * The Class UserPasswordHistoryDAO.
 */
public class UserPasswordHistoryDAO extends GenericDAO<UserPasswordHistory, String> implements mat.dao.UserPasswordHistoryDAO{

	
	/* (non-Javadoc)
	 * @see mat.dao.UserPasswordHistoryDAO#getPasswordHistory(java.lang.String)
	 */
	@Override
	public List<UserPasswordHistory> getPasswordHistory(String userId){
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(UserPasswordHistory.class);
		criteria.add(Restrictions.eq("user.id", userId));
		return criteria.list();
	}

	/* (non-Javadoc)
	 * @see mat.dao.UserPasswordHistoryDAO#addUpdatePasswordHistory(mat.model.User)
	 */
	@Override
	public void addByUpdateUserPasswordHistory(User user) {
		String userPasswordHistoryId = getOldPasswordHistoryIdByCreationDate(user.getId());
		Session session = getSessionFactory().getCurrentSession();
		try {
			String sql = "update mat.model.UserPasswordHistory m set m.password = :password, m.salt = :salt, m.createdDate = :createDate " +
					"where m.id = :passwordHistoryId";
			Query query = session.createQuery(sql);
			query.setString("password", user.getPassword().getPassword());
			query.setString("salt", user.getPassword().getSalt());
			query.setDate("createDate", user.getPassword().getCreatedDate());
			query.setString("passwordHistoryId", userPasswordHistoryId);
			int rowCount = query.executeUpdate();
		} finally {
			closeSession(session);
		}
		
	}


	/**
	 * Gets the old password history id by creation date.
	 *
	 * @return the old password history id by creation date
	 */
	private String getOldPasswordHistoryIdByCreationDate(String userId) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(UserPasswordHistory.class);
		criteria.addOrder(Order.asc("createdDate"));
		criteria.add(Restrictions.eq("user.id", userId));
		criteria.setProjection(Projections.property("id"));
	    String id = (String)criteria.list().get(0);
		return id;
	}


}
