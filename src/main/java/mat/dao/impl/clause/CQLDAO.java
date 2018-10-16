package mat.dao.impl.clause;

import java.util.List;

import mat.dao.search.GenericDAO;
import mat.model.clause.CQLData;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class CQLDAO extends GenericDAO<CQLData, String> implements
mat.dao.clause.CQLDAO{


	@Override
	public final CQLData findByID(final String measureId) {
		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(CQLData.class);

		criteria.add(Restrictions.eq("measure_id", measureId));
		List<CQLData> results = criteria.list();
		if (!results.isEmpty()) {
			//System.out.println(results.get(0).getCqlString().toString());
			return results.get(0);
		} else {
			return null;
		}
	}


}
