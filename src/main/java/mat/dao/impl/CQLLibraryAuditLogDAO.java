package mat.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import mat.DTO.AuditLogDTO;
import mat.DTO.SearchHistoryDTO;
import mat.dao.search.GenericDAO;
import mat.model.CQLAuditLog;
import mat.model.clause.CQLLibrary;
import mat.server.LoggedInUserUtil;

// TODO: Auto-generated Javadoc
/**
 * DAO implementation of Measure Audit Log.
 */
public class CQLLibraryAuditLogDAO extends GenericDAO<CQLAuditLog, String> implements mat.dao.CQLLibraryAuditLogDAO{
	
	
	/* (non-Javadoc)
	 * @see mat.dao.CQLLibraryAuditLogDAO#recordCQLLibraryEvent(mat.model.clause.CQLLibrary, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean recordCQLLibraryEvent(CQLLibrary cqlLibrary, String event, String additionalInfo){
		Session session = null;
		boolean result = false;
		try {
			CQLAuditLog cqlAuditLog = new CQLAuditLog();
			cqlAuditLog.setActivityType(event);
			cqlAuditLog.setTime(new Date());
			cqlAuditLog.setCqlLibrary(cqlLibrary);
			cqlAuditLog.setUserId(LoggedInUserUtil.getLoggedUserName());
			cqlAuditLog.setAdditionalInfo(additionalInfo);
			session = getSessionFactory().getCurrentSession();
			session.saveOrUpdate(cqlAuditLog);			
			result = true;
		}
		catch (Exception e) { //TODO: handle application exception
			e.printStackTrace();
		}
    	return result;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.dao.CQLLibraryAuditLogDAO#searchHistory(java.lang.String, int, int, java.util.List)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public SearchHistoryDTO searchHistory(String cqlId, int startIndex, int numberOfRows,List<String> filterList){
		List<AuditLogDTO> logResults = new ArrayList<AuditLogDTO>();
		SearchHistoryDTO searchHistoryDTO = new SearchHistoryDTO();
		
		Criteria logCriteria = getSessionFactory().getCurrentSession().createCriteria(CQLAuditLog.class);
		logCriteria.add(Restrictions.eq("cqlLibrary.id", cqlId));
		logCriteria.add(Restrictions.ne("activityType", "User Comment"));
		
		for(String filter : filterList){
			logCriteria.add(Restrictions.ne("activityType", filter));
		}
		
		if(numberOfRows > 0){
			logCriteria.setFirstResult(startIndex);
			logCriteria.setMaxResults(numberOfRows);
		}
		logCriteria.addOrder(Order.desc("time"));
		
		List<CQLAuditLog> results = logCriteria.list();

		for(CQLAuditLog auditLog: results){
			AuditLogDTO dto = new AuditLogDTO();
			dto.setId(auditLog.getId());
			dto.setActivityType(auditLog.getActivityType());
			dto.setAdditionlInfo(auditLog.getAdditionalInfo());
			dto.setEventTs(auditLog.getTime());
			dto.setUserId(auditLog.getUserId());
			logResults.add(dto);
		}
		searchHistoryDTO.setLogs(logResults);
		searchHistoryDTO.setTotalResults(logResults.size());
		return searchHistoryDTO; 
	}
	
		
}
