package mat.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.DTO.AuditLogDTO;
import mat.DTO.SearchHistoryDTO;
import mat.dao.search.GenericDAO;
import mat.model.CodeListAuditLog;
import mat.model.ListObject;
import mat.server.LoggedInUserUtil;

@Repository("codeListAuditLogDAO")
public class CodeListAuditLogDAOImpl extends GenericDAO<CodeListAuditLog, String> implements mat.dao.CodeListAuditLogDAO{
	
	public CodeListAuditLogDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@Override
	public boolean recordCodeListEvent(ListObject codeList, String event, String additionalInfo){
		Session session = null;
		boolean result = false;
		try {
			final CodeListAuditLog codeListAuditLog = new CodeListAuditLog();
			codeListAuditLog.setActivityType(event);
			codeListAuditLog.setTime(new Date());
			codeListAuditLog.setCodeList(codeList);
			codeListAuditLog.setUserId(LoggedInUserUtil.getLoggedInUserEmailAddress());
			codeListAuditLog.setAdditionalInfo(additionalInfo);
			session = getSessionFactory().getCurrentSession();
			session.saveOrUpdate(codeListAuditLog);			
			result = true;
		}
		catch (final Exception e) {
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
	public SearchHistoryDTO searchHistory(String codeListId, int startIndex, int numberOfRows,List<String> filterList){
		final SearchHistoryDTO searchHistoryDTO = new SearchHistoryDTO();
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<AuditLogDTO> query = cb.createQuery(AuditLogDTO.class);
		final Root<CodeListAuditLog> root = query.from(CodeListAuditLog.class);

		final In<String> in = cb.in(root.get("activityType"));
		filterList.forEach(in::value);
		
		query.select(cb.construct(
						AuditLogDTO.class, 
						 root.get("id"),
						 root.get("activityType"),
						 root.get("userId"),
						 root.get("time"),
						 root.get("additionalInfo")));
		query.where(cb.and(cb.equal(root.get("codeList").get("id"), codeListId), cb.not(in)));
		query.orderBy(cb.desc(root.get("time")));
		
		final TypedQuery<AuditLogDTO> q = session.createQuery(query);

		if(numberOfRows > 0){
			q.setFirstResult(startIndex);
			q.setMaxResults(numberOfRows);
		}
		
		final List<AuditLogDTO> logResults = q.getResultList();
		
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
	private void setPagesAndRows(String codeListId, int numberOfRows, List<String> filterList, SearchHistoryDTO searchHistoryDTO){
		int pageCount = 0;

		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<CodeListAuditLog> query = cb.createQuery(CodeListAuditLog.class);
		final Root<CodeListAuditLog> root = query.from(CodeListAuditLog.class);
		final In<String> in = cb.in(root.get("activityType"));
		filterList.forEach(in::value);
		
		query.select(root).where(cb.and(cb.equal(root.get("codeList").get("id"), codeListId), cb.not(in)));
		query.orderBy(cb.desc(root.get("time")));
		
		final List<CodeListAuditLog> results = session.createQuery(query).getResultList();

		if (CollectionUtils.isNotEmpty(results)){
			final long totalRows = results.size();			
			searchHistoryDTO.setTotalResults(totalRows);

			final int mod = (int) (totalRows % numberOfRows);
			pageCount = (int) (totalRows / numberOfRows);
			pageCount = (mod > 0)?(pageCount + 1) : pageCount;
			
			searchHistoryDTO.setPageCount(pageCount);
			
		}
	}
	
}
