package org.ifmc.mat.dao.impl.clause;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.ifmc.mat.dao.search.GenericDAO;
import org.ifmc.mat.dao.service.DAOService;
import org.ifmc.mat.model.clause.MeasureSet;
import org.springframework.context.ApplicationContext;

public class MeasureSetDAO extends GenericDAO<MeasureSet, String> implements org.ifmc.mat.dao.clause.MeasureSetDAO {
	
	private final long lockThreshold = 3*60*1000; //3 minutes   
	
	private DAOService dAOService = null;
	private ApplicationContext context = null;
	
	public MeasureSetDAO () {
		
	}
	
	public MeasureSetDAO (DAOService dAOService) {
		//allow to test using DAOService
		this.dAOService = dAOService;
	}
	
	public MeasureSetDAO(ApplicationContext context) {
		this.context = context;
	}
	
	
	
	
	public void saveMeasureSet(MeasureSet measureSet) {
		if (dAOService!=null) {
			//allow to test using DAOService
			dAOService.getMeasureSetDAO().save(measureSet);
		}
			
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
