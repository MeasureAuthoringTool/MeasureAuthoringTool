package org.ifmc.mat.dao.impl.clause;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.ifmc.mat.dao.PropertyOperator;
import org.ifmc.mat.dao.search.CriteriaQuery;
import org.ifmc.mat.dao.search.GenericDAO;
import org.ifmc.mat.dao.search.SearchCriteria;
import org.ifmc.mat.model.clause.Packager;

public class PackagerDAO extends GenericDAO<Packager, String> implements org.ifmc.mat.dao.clause.PackagerDAO {
	private static final Log logger = LogFactory.getLog(PackagerDAO.class);
	
	@Override
	public List<Packager> getForMeasure(String measureId) {
		SearchCriteria criteria = new SearchCriteria("measure.id", measureId, PropertyOperator.EQ, null);
		CriteriaQuery query = new CriteriaQuery(criteria);
		List<Packager> results = find(query);
		if(results == null) {
			results = new ArrayList<Packager>();
		}
		return results;
	}
	
	@Override
	public long getNumberOfPackagesForMeasure(String measureId) {
		SearchCriteria criteria = new SearchCriteria("measure.id", measureId, PropertyOperator.EQ, null);
		CriteriaQuery query = new CriteriaQuery(criteria);
		long results = count(query);
		return results;
	}

	@Override
	public void deletePackage(String measureId, String sequence) {
		logger.info("Deleting package " + sequence + " for measure " + measureId);
		Session session = getSessionFactory().getCurrentSession();
        String hql = "delete from org.ifmc.mat.model.clause.Packager p where p.measure.id=:measureId and p.sequence=:seq";
        Query query = session.createQuery(hql);
        query.setString("measureId", measureId);
        query.setInteger("seq", Integer.parseInt(sequence));
        query.executeUpdate();

	}
	
	@Override
	public void deleteAllPackages(String measureId) {
		logger.info("Deleting All existing packages for measure " + measureId);
		Session session = getSessionFactory().getCurrentSession();
        String hql = "delete from org.ifmc.mat.model.clause.Packager p where p.measure.id=:measureId";
        Query query = session.createQuery(hql);
        query.setString("measureId", measureId);
        query.executeUpdate();

	}
	
}
