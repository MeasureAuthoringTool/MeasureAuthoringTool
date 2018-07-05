package mat.dao.impl;

import java.util.List;

import org.hibernate.Session;

import mat.client.bonnie.BonnieLink;
import mat.dao.search.GenericDAO;

//@SuppressWarnings({ "unchecked", "deprecation" })
public class BonnieLinkDAO extends GenericDAO<BonnieLink, String> implements
mat.dao.BonnieLinkDAO{
	
	public String getClientID() {
		String query = "select CLIENT_ID from BONNIE_LINK";
		Session session = getSessionFactory().getCurrentSession();
		List<String> list = session.createSQLQuery(query).list();
		return list.get(0);
	}
	
	public String getResponseType() {
		String query = "select RESPONSE_TYPE from BONNIE_LINK";
		Session session = getSessionFactory().getCurrentSession();
		List<String> list = session.createSQLQuery(query).list();
		return list.get(0);
	}
	
	public String getRedirectURI() {
		String query = "select REDIRECT_URI from BONNIE_LINK";
		Session session = getSessionFactory().getCurrentSession();
		List<String> list = session.createSQLQuery(query).list();
		return list.get(0);
	}
	
	
}
