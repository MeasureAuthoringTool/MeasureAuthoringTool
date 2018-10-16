package mat.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mat.DTO.AuditLogDTO;
import mat.DTO.SearchHistoryDTO;
import mat.dao.search.GenericDAO;
import mat.model.MeasureAuditLog;
import mat.model.clause.Measure;
import mat.server.LoggedInUserUtil;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * DAO implementation of Measure Audit Log.
 */
public class MeasureAuditLogDAO extends GenericDAO<MeasureAuditLog, String> implements mat.dao.MeasureAuditLogDAO{
	
	/* Records the custom measure event to the MeasureAuditLog table 
	 * @see mat.dao.MeasureAuditLogDAO#recordMeasureEvent(mat.model.clause.Measure, java.lang.String, java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see mat.dao.MeasureAuditLogDAO#recordMeasureEvent(mat.model.clause.Measure, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean recordMeasureEvent(Measure measure, String event, String additionalInfo){
		Session session = null;
		boolean result = false;
		try {
			MeasureAuditLog measureAuditLog = new MeasureAuditLog();
			measureAuditLog.setActivityType(event);
			measureAuditLog.setTime(new Date());
			measureAuditLog.setMeasure(measure);
			measureAuditLog.setUserId(LoggedInUserUtil.getLoggedInUserEmailAddress());
			measureAuditLog.setAdditionalInfo(additionalInfo);
			session = getSessionFactory().getCurrentSession();
			session.saveOrUpdate(measureAuditLog);			
			result = true;
		}
		catch (Exception e) { //TODO: handle application exception
			e.printStackTrace();
		}
    	return result;
	}
	
	/* Search and returns the list of events starts with the start index and the given number of rows
	 * @see mat.dao.MeasureAuditLogDAO#searchHistory(java.lang.String, int, int)
	 */
	/* (non-Javadoc)
	 * @see mat.dao.MeasureAuditLogDAO#searchHistory(java.lang.String, int, int, java.util.List)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public SearchHistoryDTO searchHistory(String measureId, int startIndex, int numberOfRows,List<String> filterList){
		List<AuditLogDTO> logResults = new ArrayList<AuditLogDTO>();
		SearchHistoryDTO searchHistoryDTO = new SearchHistoryDTO();
		
		Criteria logCriteria = getSessionFactory().getCurrentSession().createCriteria(MeasureAuditLog.class);
		logCriteria.add(Restrictions.eq("measure.id", measureId));
		logCriteria.add(Restrictions.ne("activityType", "User Comment"));
		
		for(String filter : filterList){
			logCriteria.add(Restrictions.ne("activityType", filter));
		}
		
		if(numberOfRows > 0){
			logCriteria.setFirstResult(startIndex);
			logCriteria.setMaxResults(numberOfRows);
		}
		logCriteria.addOrder(Order.desc("time"));
		
		List<MeasureAuditLog> results = logCriteria.list();

		for(MeasureAuditLog auditLog: results){
			AuditLogDTO dto = new AuditLogDTO();
			dto.setId(auditLog.getId());
			dto.setActivityType(auditLog.getActivityType());
			dto.setAdditionlInfo(auditLog.getAdditionalInfo());
			dto.setEventTs(auditLog.getTime());
			dto.setUserId(auditLog.getUserId());
			logResults.add(dto);
		}
		searchHistoryDTO.setLogs(logResults);
		setPagesAndRows(measureId, numberOfRows, filterList, searchHistoryDTO);
		return searchHistoryDTO; 
	}
	
	/**
	 * Sets the pages and rows.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param numberOfRows
	 *            the number of rows
	 * @param filterList
	 *            the filter list
	 * @param searchHistoryDTO
	 *            the search history dto
	 */
	@SuppressWarnings("rawtypes")
	private void setPagesAndRows(String measureId, int numberOfRows, List<String> filterList, SearchHistoryDTO searchHistoryDTO){
		int pageCount = 0;

		Criteria logCriteria = getSessionFactory().getCurrentSession().createCriteria(MeasureAuditLog.class);		
		
		logCriteria.add(Restrictions.eq("measure.id", measureId));

		for(String filter : filterList){
			logCriteria.add(Restrictions.ne("activityType", filter));
		}
		
		logCriteria.setProjection(Projections.rowCount());

		List results = logCriteria.list();

		
		if(results != null && !results.isEmpty()){
			long totalRows = Long.parseLong(String.valueOf(results.get(0)));
			
			searchHistoryDTO.setTotalResults(totalRows);
			
			int mod = (int) (totalRows % numberOfRows);
			pageCount = (int) (totalRows / numberOfRows);
			pageCount = (mod > 0)?(pageCount + 1) : pageCount;
			
			searchHistoryDTO.setPageCount(pageCount);
		}
	}	
}
