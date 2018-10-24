package mat.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import mat.DTO.AuditLogDTO;
import mat.DTO.SearchHistoryDTO;
import mat.dao.search.GenericDAO;
import mat.model.CodeListAuditLog;
import mat.model.ListObject;
import mat.server.LoggedInUserUtil;

/**
 * DAO implementation of Code List Audit Log.
 */
public class CodeListAuditLogDAO extends GenericDAO<CodeListAuditLog, String> implements mat.dao.CodeListAuditLogDAO{
	
	/* Records the custom code list event to the CodeListAuditLog table
	 * @see mat.dao.CodeListAuditLogDAO#recordCodeListEvent(mat.model.ListObject, java.lang.String, java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see mat.dao.CodeListAuditLogDAO#recordCodeListEvent(mat.model.ListObject, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean recordCodeListEvent(ListObject codeList, String event, String additionalInfo){
		Session session = null;
		boolean result = false;
		try {
			CodeListAuditLog codeListAuditLog = new CodeListAuditLog();
			codeListAuditLog.setActivityType(event);
			codeListAuditLog.setTime(new Date());
			codeListAuditLog.setCodeList(codeList);
			codeListAuditLog.setUserId(LoggedInUserUtil.getLoggedInUserEmailAddress());
			codeListAuditLog.setAdditionalInfo(additionalInfo);
			session = getSessionFactory().getCurrentSession();
			session.saveOrUpdate(codeListAuditLog);			
			result = true;
		}
		catch (Exception e) { //TODO: handle application exception
			e.printStackTrace();
			
		}
    	return result;
	}
	
	/* Search and returns the list of events starts with the start index and the given number of rows
	 * @see mat.dao.CodeListAuditLogDAO#searchHistory(java.lang.String, int, int)
	 */
	/* (non-Javadoc)
	 * @see mat.dao.CodeListAuditLogDAO#searchHistory(java.lang.String, int, int, java.util.List)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public SearchHistoryDTO searchHistory(String codeListId, int startIndex, int numberOfRows,List<String> filterList){
		SearchHistoryDTO searchHistoryDTO = new SearchHistoryDTO();
		List<AuditLogDTO> logResults = new ArrayList<AuditLogDTO>();
		
		Criteria logCriteria = getSessionFactory().getCurrentSession().createCriteria(CodeListAuditLog.class);
		logCriteria.add(Restrictions.eq("codeList.id", codeListId));

		for(String filter : filterList){
			logCriteria.add(Restrictions.ne("activityType", filter));
		}

		if(numberOfRows > 0){
			logCriteria.setFirstResult(startIndex);
			logCriteria.setMaxResults(numberOfRows);
		}
		logCriteria.addOrder(Order.desc("time"));
		
		List<CodeListAuditLog> results = logCriteria.list();
		
		for(CodeListAuditLog auditLog: results){
			AuditLogDTO dto = new AuditLogDTO();
			dto.setId(auditLog.getId());
			dto.setActivityType(auditLog.getActivityType());
			dto.setAdditionlInfo(auditLog.getAdditionalInfo());
			dto.setEventTs(auditLog.getTime());
			dto.setUserId(auditLog.getUserId());
			logResults.add(dto);
		}
		
		searchHistoryDTO.setLogs(logResults);		
		setPagesAndRows(codeListId, numberOfRows, filterList, searchHistoryDTO);
		return searchHistoryDTO; 
	}
	
	

	/* Returns the number of page count for a given code list and the total number of rows
	 * @see mat.dao.CodeListAuditLogDAO#numberOfPages(java.lang.String, int)
	 */
	/**
	 * Sets the pages and rows.
	 * 
	 * @param codeListId
	 *            the code list id
	 * @param numberOfRows
	 *            the number of rows
	 * @param filterList
	 *            the filter list
	 * @param searchHistoryDTO
	 *            the search history dto
	 */
	@SuppressWarnings("rawtypes")
	private void setPagesAndRows(String codeListId, int numberOfRows, List<String> filterList, SearchHistoryDTO searchHistoryDTO){
		int pageCount = 0;

		Criteria logCriteria = getSessionFactory().getCurrentSession().createCriteria(CodeListAuditLog.class);
		
		logCriteria.add(Restrictions.eq("codeList.id", codeListId));

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
