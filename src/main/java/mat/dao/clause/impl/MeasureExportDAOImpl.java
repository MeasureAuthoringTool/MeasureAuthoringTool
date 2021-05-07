package mat.dao.clause.impl;

import mat.dao.clause.MeasureExportDAO;
import mat.dao.search.GenericDAO;
import mat.model.clause.MeasureExport;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

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

	public void saveAndFlush(MeasureExport me) {
		if (isEmpty(me)) return;
		final Session session = getSessionFactory().getCurrentSession();
		session.saveOrUpdate(me);
		session.flush();
		session.clear();
	}
}
