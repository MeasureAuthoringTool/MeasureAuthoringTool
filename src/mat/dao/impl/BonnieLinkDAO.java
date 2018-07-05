package mat.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

import mat.client.bonnie.BonnieLink;
import mat.dao.search.GenericDAO;

@SuppressWarnings({ "unchecked", "deprecation" })
public class BonnieLinkDAO extends GenericDAO<BonnieLink, String> implements
mat.dao.BonnieLinkDAO{
	
	private static final Log logger = LogFactory.getLog(BonnieLinkDAO.class);
	
	public String getClientID() {
		logger.info("Getting the client id for the bonnie link");
		String query = "select CLIENT_ID from BONNIE_LINK";
		Session session = getSessionFactory().getCurrentSession();
		List<String> list = session.createSQLQuery(query).list();
		return list.get(0);
	}
	
	public String getResponseType() {
		logger.info("Getting the response type for the bonnie link");
		String query = "select RESPONSE_TYPE from BONNIE_LINK";
		Session session = getSessionFactory().getCurrentSession();
		List<String> list = session.createSQLQuery(query).list();
		return list.get(0);
	}
	
	public String getRedirectURI() {
		logger.info("Getting the redirect URI for the bonnie link");
		String query = "select REDIRECT_URI from BONNIE_LINK";
		Session session = getSessionFactory().getCurrentSession();
		List<String> list = session.createSQLQuery(query).list();
		return list.get(0);
	}
	
	
}
