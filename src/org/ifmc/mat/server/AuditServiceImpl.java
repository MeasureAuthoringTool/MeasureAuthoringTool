package org.ifmc.mat.server;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ifmc.mat.DTO.SearchHistoryDTO;
import org.ifmc.mat.client.audit.service.AuditService;
import org.ifmc.mat.server.service.CodeListAuditService;
import org.ifmc.mat.server.service.MeasureAuditService;
import org.ifmc.mat.server.service.TransactionAuditService;

/**
 * Client implementation of Audit Service Implementation
 *
 */
@SuppressWarnings("serial")
public class AuditServiceImpl extends SpringRemoteServiceServlet implements AuditService{
	private static final Log logger = LogFactory.getLog(AuditServiceImpl.class);
	
	
	/**
	 * Returns the spring bean MeasureAuditService
	 * @return MeasureAuditService
	 */
	private MeasureAuditService getMeasureAuditService(){
		return (MeasureAuditService)context.getBean("measureAuditService");
	}

	/**
	 * Returns the spring bean CodeListAuditService
	 * @return CodeListAuditService
	 */
	private CodeListAuditService getCodeListAuditService(){
		return (CodeListAuditService)context.getBean("codeListAuditService");
	}
	
	/* Records an measure event
	 * @see org.ifmc.mat.client.audit.service.AuditService#recordMeasureEvent(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean recordMeasureEvent(String measureId, String event, String additionalInfo, boolean isChildLogRequired) {
		return getMeasureAuditService().recordMeasureEvent(measureId, event, additionalInfo, isChildLogRequired);
	}
	
	/* Records an code list event
	 * @see org.ifmc.mat.client.audit.service.AuditService#recordCodeListEvent(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean recordCodeListEvent(String codeListId, String event, String additionalInfo) {
		return getCodeListAuditService().recordCodeListEvent(codeListId, event, additionalInfo);
	}
	
	/* Returns the code list log search result for a given code list id
	 * @see org.ifmc.mat.client.audit.service.AuditService#executeCodeListLogSearch(java.lang.String, int, int)
	 */
	@Override
	public SearchHistoryDTO executeCodeListLogSearch(String codeListId, int startIndex, int numberOfRows,List<String> filterList){
		return getCodeListAuditService().executeSearch(codeListId, startIndex, numberOfRows,filterList);
	}
	
	
	
	/* Returns the measure log search result for a given code list id
	 * @see org.ifmc.mat.client.audit.service.AuditService#executeMeasureLogSearch(java.lang.String, int, int)
	 */
	@Override
	public SearchHistoryDTO executeMeasureLogSearch(String measureId, int startIndex, int numberOfRows,List<String> filterList){
		return getMeasureAuditService().executeSearch(measureId, startIndex, numberOfRows,filterList);
	}

	/**
	 * Returns the spring bean TransactionAuditService
	 * @return TransactionService
	 */
	private TransactionAuditService getTransactionAuditService(){
		return (TransactionAuditService)context.getBean("transactionAuditService");
	}
	
	@Override
	public boolean recordTransactionEvent(String primaryId, String secondaryId,
			String activityType, String userId, String additionalInfo, int logLevel) {
		return getTransactionAuditService().recordTransactionEvent(primaryId, secondaryId, activityType, userId, additionalInfo, logLevel);
	}

}
