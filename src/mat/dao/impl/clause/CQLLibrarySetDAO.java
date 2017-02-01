package mat.dao.impl.clause;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.ApplicationContext;

import mat.dao.search.GenericDAO;
import mat.dao.service.DAOService;
import mat.model.clause.CQLLibrarySet;

/**
 * The Class MeasureSetDAO.
 */
public class CQLLibrarySetDAO extends GenericDAO<CQLLibrarySet, String> implements mat.dao.clause.CQLLibrarySetDAO {
	
	/** The lock threshold. */
	private final long lockThreshold = 3*60*1000; //3 minutes   
	
	/** The d ao service. */
	private DAOService dAOService = null;
	
	/** The context. */
	private ApplicationContext context = null;
	
	/**
	 * Instantiates a new measure set dao.
	 */
	public CQLLibrarySetDAO () {
		
	}
	
	/**
	 * Instantiates a new measure set dao.
	 * 
	 * @param dAOService
	 *            the d ao service
	 */
	public CQLLibrarySetDAO (DAOService dAOService) {
		//allow to test using DAOService
		this.dAOService = dAOService;
	}
	
	/**
	 * Instantiates a new measure set dao.
	 * 
	 * @param context
	 *            the context
	 */
	public CQLLibrarySetDAO(ApplicationContext context) {
		this.context = context;
	}
	
	
	
	
	
	public void saveCQLLibrarySet(CQLLibrarySet cqlSet) {
		if (dAOService!=null) {
			//allow to test using DAOService
			dAOService.getCqlLibrarySetDAO().save(cqlSet);;
		}
			
	}
	
	
	public CQLLibrarySet findCQLLibrarySet(String cqlLibrarySetId) {
		CQLLibrarySet cqlLibrarySet = null;
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(CQLLibrarySet.class);
		criteria.add(Restrictions.eq("id", cqlLibrarySetId));
		List<CQLLibrarySet> setList = criteria.list();
		if(setList != null && setList.size() > 0)
			cqlLibrarySet = setList.get(0);
		return cqlLibrarySet;
	}

	
}
