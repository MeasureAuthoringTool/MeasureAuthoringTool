package mat.dao.impl.clause;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import mat.dao.search.GenericDAO;
import mat.model.clause.MeasureXML;

public class MeasureXMLDAO extends GenericDAO<MeasureXML, String>  implements mat.dao.clause.MeasureXMLDAO {
	
	@Override
	public MeasureXML findForMeasure(String measureId) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(MeasureXML.class);
		
		criteria.add(Restrictions.eq("measure_id", measureId));
		List<MeasureXML> results =  criteria.list();
		if(!results.isEmpty()) {
			return results.get(0);
		}
		else {
			return null;
		}
	}

}
