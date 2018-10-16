package mat.dao.impl.clause;

import java.util.List;

import mat.dao.search.GenericDAO;
import mat.model.clause.MeasureExport;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * The Class MeasureExportDAO.
 */
public class MeasureExportDAO extends GenericDAO<MeasureExport, String> 
	implements mat.dao.clause.MeasureExportDAO {
	
	/* (non-Javadoc)
	 * @see mat.dao.clause.MeasureExportDAO#findForMeasure(java.lang.String)
	 */
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
