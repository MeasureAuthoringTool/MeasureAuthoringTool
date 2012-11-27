package mat.dao.impl.clause;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import mat.dao.QualityDataSetDAO;
import mat.dao.search.GenericDAO;
import mat.dao.service.DAOService;
import mat.model.clause.Clause;
import mat.model.clause.Measure;
import mat.server.util.MeasureUtility;
import mat.shared.ConstantMessages;
import mat.shared.model.QDSTerm;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.ApplicationContext;



public class ClauseDAO extends GenericDAO<Clause, String> implements mat.dao.clause.ClauseDAO {
	private DAOService dAOService = null;
	
	public ClauseDAO () {	
	}
	
	public ClauseDAO (DAOService dAOService) {
		//allow to test using DAOService
		this.dAOService = dAOService;
	}
	
	//full save - clause and it's children
	public void saveClause(Clause clause) {
		ClauseManagerDAO cm;
		if (dAOService!=null) {
			dAOService.getClauseDAO().save(clause);
			cm = new ClauseManagerDAO(dAOService);			
		} else {
			save(clause);
			cm = new ClauseManagerDAO();
		}
		
		cm.saveClause(clause);
	}
	
	//full save - clause and it's children
	//special save from service
	public void saveClause(Clause clause, ApplicationContext context) {
		ClauseManagerDAO cm;
		cm = new ClauseManagerDAO(context);		
		cm.saveClause(clause);
	}
	
	public Clause loadClauseByName(String measureId, String clauseName, ApplicationContext ctx) {
		Clause clause = new Clause();
		clause = findByNameAndMeasureId(clauseName, measureId);
		
		ClauseManagerDAO cm = new ClauseManagerDAO(dAOService);
		mat.shared.model.Decision sharedDecision = cm.loadClause(clause.getDecision().getId(),ctx);
		List<mat.shared.model.Decision> decisions = new ArrayList<mat.shared.model.Decision>();
		decisions.add(sharedDecision);

		clause.setDecisions(decisions);
		
		return clause;
	}
		
	public Clause loadClause(String measureId, String clauseId, ApplicationContext ctx) {
		
//**** SPECIAL UPDATE TO DATABASE ROWS. APPENDING SHORT MEASURE NAME TO MAIN CLAUSE NAMES 
//(IE. "Population1" ---> "AMA1_Population1"
//MUST REMOVE THIS CODE BEFORE PRODUCTION!		
//		upgradeClause(ctx);
		
		if (dAOService!=null) {
			Clause clause = new Clause();
			clause = dAOService.getClauseDAO().find(clauseId);
			
			ClauseManagerDAO cm = new ClauseManagerDAO(dAOService);
			mat.shared.model.Decision sharedDecision = cm.loadClause(clause.getDecision().getId(),ctx);
			List<mat.shared.model.Decision> decisions = new ArrayList<mat.shared.model.Decision>();
			decisions.add(sharedDecision);

			clause.setDecisions(decisions);
			
			return clause;
		} else {
			Clause clause = new Clause();
			//clause = find(measureId, clauseId);
			clause = find(clauseId);
			
			ClauseManagerDAO cm = new ClauseManagerDAO(dAOService);
			mat.shared.model.Decision sharedDecision = cm.loadClause(clause.getDecision().getId(), ctx);
			List<mat.shared.model.Decision> decisions = new ArrayList<mat.shared.model.Decision>();
			decisions.add(sharedDecision);
			clause.setDecisions(decisions);
			
			return clause;
		}
	}
	/**
	//special load from service
	public Clause loadClause(String measureId, String clauseId, ApplicationContext context) {
		Clause clause = new Clause();
		clause = find(clauseId);
		
		ClauseManagerDAO cm = new ClauseManagerDAO(context);
		org.ifmc.mat.shared.model.Decision sharedDecision = cm.loadClause(clause.getDecisionId());
		List<org.ifmc.mat.shared.model.Decision> decisions = new ArrayList<org.ifmc.mat.shared.model.Decision>();
		decisions.add(sharedDecision);
		clause.setDecisions(decisions);
		
		return clause;
	}
*/	
	public java.util.List<Clause> findByMeasureId(String measureId, String version) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Clause.class);
		criteria.add(Restrictions.eq("measureId", measureId));
		criteria.addOrder(Order.asc("name"));
		java.util.List<Clause> clauses = criteria.list();
		Collections.sort(clauses, new ClauseComparator());
		return clauses;
	}
	
	public java.util.List<Clause> findSystemClausesByMeasureId(String measureId, String version) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Clause.class);
		criteria.add(Restrictions.eq("measureId", measureId));
		criteria.add(Restrictions.ne("contextId", ConstantMessages.MEASURE_PHRASE_CONTEXT_ID));//"Measure Phrase"
		criteria.addOrder(Order.asc("name"));
		java.util.List<Clause> clauses = criteria.list();
		Collections.sort(clauses, new ClauseComparator());
		return clauses;
	}

	class ClauseComparator implements Comparator<Clause>{
		
		@Override
		public int compare(Clause o1, Clause o2) {
			String a = o1.getName();
			String b = o2.getName();
			NumericSuffix suff= new NumericSuffix();
			return suff.compare(a,b);
		}
	}
	
	/**
	 * 
	 * @return the list of system clause ids where there is more than a top level AND
	 */
	private List<String> getIdsForSystemClausesWithContent(){
		String query = "select ID from CLAUSE "+ 
		"where not(CONTEXT_ID='"+ConstantMessages.MEASURE_PHRASE_CONTEXT_ID+"') and DECISION_ID in ( "+
        	"select PARENT_ID from DECISION where OPERATOR='AND' and ID in ( "+
            	"select PARENT_ID from DECISION));";
	
		Session session = getSessionFactory().getCurrentSession();
		List<String> list = session.createSQLQuery(query).list();
		return list;
	}
	
	public java.util.List<Clause> findSystemClauses(String measureOwnerId, String userRole, ApplicationContext context) {
		
		List<String> clauseIDs = getIdsForSystemClausesWithContent();
		
		List<Measure> mList = null;
		//admin and super user can see all measures
		if (userRole.equalsIgnoreCase("User")) {//user
			mList = ((mat.dao.clause.MeasureDAO)context.getBean("measureDAO")).findByOwnerId(measureOwnerId);
		} else {
			mList = ((mat.dao.clause.MeasureDAO)context.getBean("measureDAO")).find();
		}
		
		if (mList.isEmpty()) return new ArrayList<Clause>();
		
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Clause.class);
		
		List<String> sList = new ArrayList<String>();
		for (Measure m : mList) {
			sList.add(m.getId());
		}
		
		criteria.add(Restrictions.in("measureId", sList));
		criteria.addOrder(Order.asc("name"));
		criteria.add(Restrictions.in("id", clauseIDs));
		java.util.List<Clause> clauses = criteria.list();
		
		//With US 506 this seems to be working, but we might have to change this 
		// add in draft/version sort logic if we don't get it for free.
		Collections.sort(clauses, new ClauseComparator());
		
		HashMap <String,String> hm = new HashMap<String,String>();
		for(Measure m: mList){
			 hm.put(m.getId(), MeasureUtility.getClauseLibraryVersionPrefix(m.getVersion(), m.isDraft()));
		}
		
		for(Clause c: clauses){
			c.setVersion(hm.get(c.getMeasureId()));
		}

		return clauses;
	}

	public Clause findByNameAndMeasureId(String name, String measureId) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Clause.class);
		criteria.add(Restrictions.eq("name", name));
		criteria.add(Restrictions.eq("measureId", measureId));
		//this search is unique enough to return only one row
		if (!criteria.list().isEmpty()) {
			return (Clause)criteria.list().get(0);
		} else {
			return null;
		}
	}
	
	/*
	 * allow you to clone based on measureid and clause name
	 * use to clone system clauses
	 */
	public void clone(String cloneFromMeasureId, String cloneToMeasureId, 
			String clauseToBeClonedName, String newClauseName, 
			boolean replaceName, boolean keepContext,ApplicationContext context) {
		
		Clause clause = loadClauseByName(cloneFromMeasureId, clauseToBeClonedName, context);

		//set keepContext to true, since we are cloning system clauses
		clone(cloneToMeasureId, clause, newClauseName, replaceName, keepContext, context);
	}

	/*
	 * use to clone any one clause
	 */
	public void clone(String cloneToMeasureId, Clause clause, 
			String newClauseName,boolean replaceName, boolean keepContext, ApplicationContext context) {
		//get clone to measure
		mat.dao.clause.MeasureDAO mDAO = (mat.dao.clause.MeasureDAO)context.getBean("measureDAO");
		Measure cloneToMeasure = mDAO.find(cloneToMeasureId);

		ClauseManagerDAO cm = new ClauseManagerDAO(context);
		
		
		//we need to find all QDSTerms so we clone
		//only ones in the clause
		List<QDSTerm> qDSTermList = cm.getQDSTermsFromClause(clause);
		
		//clone Quality Data Set
		QualityDataSetDAO qdsDAO = (QualityDataSetDAO)context.getBean("qualityDataSetDAO");
		qdsDAO.cloneSelectedQDSElements(clause.getMeasureId(), cloneToMeasure, qDSTermList);
		
		//use clause manager DAO to clone the clause
		cm.cloneClause(clause, cloneToMeasure, newClauseName, replaceName, keepContext, false);

	}
		
	private void upgradeClause(ApplicationContext context) {
		//TODO:consolidate this with AppController
		String POPULATION = "Population";
		String NUMERATOR = "Numerator";
		String DENOMINATOR = "Denominator";
		String EXCLUSIONS = "Exclusions";
		String EXCEPTIONS = "Exceptions";
		String OTHER = "User-defined";

		List<String> criterionNames = Arrays.asList(
			new String[] {POPULATION, NUMERATOR, DENOMINATOR, EXCLUSIONS, EXCEPTIONS, OTHER});

		List<Measure> mList = ((mat.dao.clause.MeasureDAO)context.getBean("measureDAO")).find();		
		for (Measure m : mList) {
			String mId = m.getId();
			String sName = m.getaBBRName();
			
			List<Clause> cList = findByMeasureId(mId, "");
			for (Clause c : cList) {
				for (String criterion : criterionNames) {
					if (c.getName().contains(criterion) && !c.getName().contains(sName)) {
						c.setName(sName + "_" + c.getName());
						save(c);
						break;
					}
				}
			}
		}
	}
	
	//full save - clause and it's children
	public void saveClauses(List<Clause> clauses) {
		ClauseManagerDAO cm;
		for(Clause clause : clauses){
			if (dAOService!=null) {
				dAOService.getClauseDAO().save(clause);
				cm = new ClauseManagerDAO(dAOService);			
			} else {
				save(clause);
				cm = new ClauseManagerDAO();
			}
			
			cm.saveClause(clause);
		}
	}
	
	//full save - clause and it's children
	//special save from service
	public void saveClauses(List<Clause> clauses, ApplicationContext context) {
		ClauseManagerDAO cm;
		cm = new ClauseManagerDAO(context);
		for(Clause clause : clauses){
			cm.saveClause(clause);
		}
	}

	@Override
	public void updateClauseName(String id, String name) {
		Session session = getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery("update CLAUSE c set c.name = '"+name+"' where c.id = '"+id+"';");
		int ret = query.executeUpdate();
	}

	@Override
	public Clause findByClauseIdAndMeasureId(String clauseID, String measureId) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Clause.class);
		criteria.add(Restrictions.eq("id", clauseID));
		criteria.add(Restrictions.eq("measureId", measureId));
		//this search is unique enough to return only one row
		if (!criteria.list().isEmpty()) {
			return (Clause)criteria.list().get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	/*
	 * US 597 
	 * This method returns all the list of stratification clauses for the given measure Id.
	 */
	public List<Clause> getAllStratificationClauses(String measureId,String contextId) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Clause.class);
		criteria.add(Restrictions.eq("contextId",contextId));
		criteria.add(Restrictions.eq("measureId", measureId));
		return criteria.list();
	}
	
	
	
}
