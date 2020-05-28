package mat.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dto.AuditLogDTO;
import mat.dto.SearchHistoryDTO;
import mat.dao.search.GenericDAO;
import mat.model.CodeListAuditLog;
import mat.model.ListObject;
import mat.server.LoggedInUserUtil;

@Repository("codeListAuditLogDAO")
public class CodeListAuditLogDAOImpl extends GenericDAO<CodeListAuditLog, String> implements mat.dao.CodeListAuditLogDAO{
	
	private static final String ACTIVITY_TYPE = "activityType";
	
	public CodeListAuditLogDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@Override
	public boolean recordCodeListEvent(ListObject codeList, String event, String additionalInfo){
		boolean result = false;
		try {
			final CodeListAuditLog codeListAuditLog = new CodeListAuditLog();
			codeListAuditLog.setActivityType(event);
			codeListAuditLog.setTime(new Date());
			codeListAuditLog.setCodeList(codeList);
			codeListAuditLog.setUserId(LoggedInUserUtil.getLoggedInUserEmailAddress());
			codeListAuditLog.setAdditionalInfo(additionalInfo);
			final Session session = getSessionFactory().getCurrentSession();
			session.saveOrUpdate(codeListAuditLog);			
			result = true;
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
    	return result;
	}

	@Override
	public SearchHistoryDTO searchHistory(String codeListId, int startIndex, int numberOfRows, List<String> filterList){
		final SearchHistoryDTO searchHistoryDTO = new SearchHistoryDTO();
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<AuditLogDTO> query = cb.createQuery(AuditLogDTO.class);
		final Root<CodeListAuditLog> root = query.from(CodeListAuditLog.class);

		final Predicate predicate = getPredicateForAuditLog(codeListId, filterList, cb, root);
		
		query.select(cb.construct(
						AuditLogDTO.class, 
						 root.get("id"),
						 root.get(ACTIVITY_TYPE),
						 root.get("userId"),
						 root.get("time"),
						 root.get("additionalInfo")));
		query.where(predicate);
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
	
	private void setPagesAndRows(String codeListId, int numberOfRows, List<String> filterList, SearchHistoryDTO searchHistoryDTO){

		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		final Root<CodeListAuditLog> root = countQuery.from(CodeListAuditLog.class);

		final Predicate predicate = getPredicateForAuditLog(codeListId, filterList, cb, root);
		
		countQuery =  countQuery.select(cb.count(root)).where(predicate);
		
		final Long totalRows = session.createQuery(countQuery).getSingleResult();

		if (totalRows != null){
			searchHistoryDTO.setTotalResults(totalRows);

			final int mod = (int) (totalRows % numberOfRows);
			int pageCount = 0;
			pageCount = (int) (totalRows / numberOfRows);
			pageCount = (mod > 0)?(pageCount + 1) : pageCount;
			
			searchHistoryDTO.setPageCount(pageCount);
			
		}
	}
	
	private Predicate getPredicateForAuditLog(String codeListId, List<String> filterList, CriteriaBuilder cb, Root<CodeListAuditLog> root) {
		final Predicate p1 = cb.equal(root.get("codeList").get("id"), codeListId);
		Predicate p2 = null;
		
		if(CollectionUtils.isNotEmpty(filterList)) {
			final In<String> in = cb.in(root.get(ACTIVITY_TYPE));
			filterList.forEach(in::value);
			p2 = cb.not(in);
		}
		
		return (p2 != null) ? cb.and(p1, p2) : p1;
	}
	
}
