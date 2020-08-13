package mat.dao.clause.impl;

import mat.dao.clause.MeasureDeveloperDAO;
import mat.dao.search.GenericDAO;
import mat.model.clause.MeasureDeveloperAssociation;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;

@Repository("MeasureDeveloperDAO")
public class MeasureDeveloperDAOImpl extends GenericDAO<MeasureDeveloperDAO, String> implements MeasureDeveloperDAO {

	@Override
	public void deleteAllDeveloperAssociationsByMeasureId(String measureId) {
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaDelete<MeasureDeveloperAssociation> deleteQuery = cb.createCriteriaDelete(MeasureDeveloperAssociation.class);
		final Root<MeasureDeveloperAssociation> root = deleteQuery.from(MeasureDeveloperAssociation.class);
		
		
		deleteQuery.where(cb.equal(root.get("measure").get("id"), measureId));
		
		session.createQuery(deleteQuery).executeUpdate();
	}

}
