package mat.dao.clause.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.clause.MeasureExportDAO;
import mat.dao.search.GenericDAO;
import mat.model.clause.MeasureExport;

@Repository
public class MeasureExportDAOImpl extends GenericDAO<MeasureExport, String> implements MeasureExportDAO {
	
	public MeasureExportDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	@Override
	public MeasureExport findByMeasureId(String measureId) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(MeasureExport.class);
		criteria.add(Restrictions.eq("measure.id", measureId));
		List<MeasureExport> results =  criteria.list();
		if(!results.isEmpty()) {
			return results.get(0);
		}
		else {
			return null;
		}
	}

}
