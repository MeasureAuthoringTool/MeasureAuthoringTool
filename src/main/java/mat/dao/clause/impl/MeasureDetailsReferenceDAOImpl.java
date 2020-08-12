package mat.dao.clause.impl;

import mat.dao.clause.MeasureDetailsReferenceDAO;
import mat.dao.search.GenericDAO;
import mat.model.clause.MeasureDetailsReference;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;

@Repository("MeasureDetailsReferenceDAO")
public class MeasureDetailsReferenceDAOImpl extends GenericDAO<MeasureDetailsReference, String> implements MeasureDetailsReferenceDAO{

	public MeasureDetailsReferenceDAOImpl() {

	}
	
	@Override
	public void deleteAllByMeasureDetailsId(int measureDetailsId) {
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaDelete<MeasureDetailsReference> deleteQuery = cb.createCriteriaDelete(MeasureDetailsReference.class);
		final Root<MeasureDetailsReference> root = deleteQuery.from(MeasureDetailsReference.class);
		
		
		deleteQuery.where(cb.equal(root.get("measureDetails").get("id"), measureDetailsId));
		
		session.createQuery(deleteQuery).executeUpdate();
	}

}
