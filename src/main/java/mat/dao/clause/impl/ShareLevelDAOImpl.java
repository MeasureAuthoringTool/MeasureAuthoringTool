package mat.dao.clause.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mat.dao.clause.ShareLevelDAO;
import mat.dao.search.GenericDAO;
import mat.model.clause.ShareLevel;

/**
 * The Class ShareLevelDAO.
 */
@Transactional(readOnly=true)
@Repository("shareLevelDAO")
public class ShareLevelDAOImpl extends GenericDAO<ShareLevel, String> implements ShareLevelDAO {
	public ShareLevelDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
}
