package mat.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import mat.dao.search.GenericDAO;
import mat.model.User;
import mat.model.UserPasswordHistory;

// TODO: Auto-generated Javadoc
/**
 * The Class UserPasswordHistoryDAO.
 */
public class UserPasswordHistoryDAO extends GenericDAO<UserPasswordHistory, String> implements mat.dao.UserPasswordHistoryDAO{

	
	@Override
	public List<UserPasswordHistory> getPasswordHistory(String userId){
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(UserPasswordHistory.class);
		criteria.add(Restrictions.eq("userId", userId));
		return criteria.list();
	}
	
	
	@Override
	public Date getOldPasswordCreationDate(String userId){
		String query = "select create_date from USER_PASSWORD_HISTORY where user_id=:userId";
		query += " order by create_date LIMIT 1";
		Session session = getSessionFactory().getCurrentSession();
		List<Date> list= session.createSQLQuery(query).setParameter("userId", userId).list();
		return list.get(0);
	}


}
