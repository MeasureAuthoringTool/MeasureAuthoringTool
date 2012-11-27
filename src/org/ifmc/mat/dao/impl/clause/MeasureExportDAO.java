package org.ifmc.mat.dao.impl.clause;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.ifmc.mat.dao.search.GenericDAO;
import org.ifmc.mat.model.clause.MeasureExport;

public class MeasureExportDAO extends GenericDAO<MeasureExport, String> 
	implements org.ifmc.mat.dao.clause.MeasureExportDAO {
	
	@Override
	public MeasureExport findForMeasure(String measureId) {
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
