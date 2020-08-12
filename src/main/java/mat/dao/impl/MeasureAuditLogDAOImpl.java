package mat.dao.impl;

import mat.dao.search.GenericDAO;
import mat.dto.AuditLogDTO;
import mat.dto.SearchHistoryDTO;
import mat.model.MeasureAuditLog;
import mat.model.clause.Measure;
import mat.server.LoggedInUserUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

@Repository("measureAuditLogDAO")
public class MeasureAuditLogDAOImpl extends GenericDAO<MeasureAuditLog, String> implements mat.dao.MeasureAuditLogDAO{
	
	private static final String ACTIVITY_TYPE = "activityType";
	private static final String USER_COMMENT = "User Comment";
	
	public MeasureAuditLogDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	@Override
	public boolean recordMeasureEvent(Measure measure, String event, String additionalInfo){
		boolean result = false;
		try {
			final MeasureAuditLog measureAuditLog = new MeasureAuditLog();
			measureAuditLog.setActivityType(event);
			measureAuditLog.setTime(new Date());
			measureAuditLog.setMeasure(measure);
			measureAuditLog.setUserId(LoggedInUserUtil.getLoggedInUserEmailAddress());
			measureAuditLog.setAdditionalInfo(additionalInfo);
			final Session session = getSessionFactory().getCurrentSession();
			session.saveOrUpdate(measureAuditLog);			
			result = true;
		}
		catch (final Exception e) { 
			e.printStackTrace();
		}
    	return result;
	}
	
	@Override
	public SearchHistoryDTO searchHistory(String measureId, int startIndex, int numberOfRows, List<String> filterList){
		final SearchHistoryDTO searchHistoryDTO = new SearchHistoryDTO();
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<AuditLogDTO> query = cb.createQuery(AuditLogDTO.class);
		final Root<MeasureAuditLog> root = query.from(MeasureAuditLog.class);

		final Predicate predicate = getPredicateForAuditLog(measureId, filterList, cb, root);

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
		
		setPagesAndRows(measureId, numberOfRows, filterList, searchHistoryDTO);

		return searchHistoryDTO; 
	}

	private void setPagesAndRows(String measureId, int numberOfRows, List<String> filterList, SearchHistoryDTO searchHistoryDTO){
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		final Root<MeasureAuditLog> root = countQuery.from(MeasureAuditLog.class);
		
		final Predicate predicate = getPredicateForAuditLog(measureId, filterList, cb, root);
		
		countQuery =  countQuery.select(cb.count(root)).where(predicate);
		
		final Long totalRows = session.createQuery(countQuery).getSingleResult();
		if (totalRows != null) {
			searchHistoryDTO.setTotalResults(totalRows);

			final int mod = (int) (totalRows % numberOfRows);
			int pageCount = (int) (totalRows / numberOfRows);
			pageCount = (mod > 0)?(pageCount + 1) : pageCount;
			
			searchHistoryDTO.setPageCount(pageCount);

		}
	}	
	
	private Predicate getPredicateForAuditLog(String measureId, List<String> filterList, CriteriaBuilder cb, Root<MeasureAuditLog> root) {
		final Predicate p1 = cb.equal(root.get("measure").get("id"), measureId);
		Predicate p2 = null;
		
		if(CollectionUtils.isNotEmpty(filterList)) {
			final In<String> in = cb.in(root.get(ACTIVITY_TYPE));
			filterList.add(USER_COMMENT);
			filterList.forEach(in::value);
			p2 = cb.not(in);
		} else {
			p2 = cb.notEqual(root.get(ACTIVITY_TYPE), USER_COMMENT);
		}
		
		return cb.and(p1, p2);
	}
}
