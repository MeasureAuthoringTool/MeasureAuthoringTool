package mat.dao.impl.clause;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

import mat.dao.search.GenericDAO;
import mat.model.clause.Packager;

public class PackagerDAO extends GenericDAO<Packager, String> implements mat.dao.clause.PackagerDAO {
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(PackagerDAO.class);
	
	
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
