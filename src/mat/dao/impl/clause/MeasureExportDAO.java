package mat.dao.impl.clause;

import java.util.List;

import mat.dao.search.GenericDAO;
import mat.model.clause.MeasureExport;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class MeasureExportDAO extends GenericDAO<MeasureExport, String> 
	implements mat.dao.clause.MeasureExportDAO {
	
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
