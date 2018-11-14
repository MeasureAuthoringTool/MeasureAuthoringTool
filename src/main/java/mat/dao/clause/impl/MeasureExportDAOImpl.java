package mat.dao.clause.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.clause.MeasureExportDAO;
import mat.dao.search.GenericDAO;
import mat.model.clause.MeasureExport;

@Repository("measureExportDAO")
public class MeasureExportDAOImpl extends GenericDAO<MeasureExport, String> implements MeasureExportDAO {
	
	public MeasureExportDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	@Override
	public MeasureExport findByMeasureId(String measureId) {
		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<MeasureExport> query = cb.createQuery(MeasureExport.class);
		Root<MeasureExport> root = query.from(MeasureExport.class);
		
		query.select(root).where(cb.equal(root.get("measure").get("id"), measureId));
		
		List<MeasureExport> results = session.createQuery(query).getResultList();

		return !results.isEmpty() ? results.get(0) : null;
	}

}
