package mat.dao.clause.impl;

import mat.dao.clause.PackagerDAO;
import mat.dao.search.GenericDAO;
import mat.model.clause.Packager;
import mat.server.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("packagerDAO")
public class PackagerDAOImpl extends GenericDAO<Packager, String> implements PackagerDAO {
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(PackagerDAOImpl.class);
	
	public PackagerDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	@Override
	public void deleteAllPackages(String measureId) {
		logger.info("Deleting All existing packages for measure " + measureId);
		Session session = getSessionFactory().getCurrentSession();
        String hql = "delete from mat.model.clause.Packager p where p.measure.id=:measureId";
        Query query = session.createQuery(hql);
        query.setString("measureId", measureId);
        query.executeUpdate();
	}
	
}
