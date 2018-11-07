package mat.dao.clause.impl;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.clause.MeasureSetDAO;
import mat.dao.search.GenericDAO;
import mat.model.clause.MeasureSet;

@Repository("measureSetDAO")
public class MeasureSetDAOImpl extends GenericDAO<MeasureSet, String> implements MeasureSetDAO {
	
	public MeasureSetDAOImpl (@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	public void saveMeasureSet(MeasureSet measureSet) {
		super.save(measureSet);
	}
	
	public MeasureSet findMeasureSet(String measureSetId) {
		MeasureSet measureSet = null;
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(MeasureSet.class);
		criteria.add(Restrictions.eq("id", measureSetId));
		List<MeasureSet> measureSetList = criteria.list();
		if(measureSetList != null && measureSetList.size() > 0)
			measureSet = measureSetList.get(0);
		return measureSet;
	}

	
}
