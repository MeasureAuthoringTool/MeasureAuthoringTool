package mat.dao.impl.clause;


import java.util.List;

import mat.dao.search.GenericDAO;
import mat.dao.service.DAOService;
import mat.model.clause.MeasureSet;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.ApplicationContext;

/**
 * The Class MeasureSetDAO.
 */
public class MeasureSetDAO extends GenericDAO<MeasureSet, String> implements mat.dao.clause.MeasureSetDAO {
	
	/** The d ao service. */
	private DAOService dAOService = null;
	
	/** The context. */
	private ApplicationContext context = null;
	
	/**
	 * Instantiates a new measure set dao.
	 */
	public MeasureSetDAO () {
		
	}
	
	/**
	 * Instantiates a new measure set dao.
	 * 
	 * @param dAOService
	 *            the d ao service
	 */
	public MeasureSetDAO (DAOService dAOService) {
		//allow to test using DAOService
		this.dAOService = dAOService;
	}
	
	/**
	 * Instantiates a new measure set dao.
	 * 
	 * @param context
	 *            the context
	 */
	public MeasureSetDAO(ApplicationContext context) {
		this.context = context;
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see mat.dao.clause.MeasureSetDAO#saveMeasureSet(mat.model.clause.MeasureSet)
	 */
	public void saveMeasureSet(MeasureSet measureSet) {
		if (dAOService!=null) {
			//allow to test using DAOService
			dAOService.getMeasureSetDAO().save(measureSet);
		}
			
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.clause.MeasureSetDAO#findMeasureSet(java.lang.String)
	 */
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
