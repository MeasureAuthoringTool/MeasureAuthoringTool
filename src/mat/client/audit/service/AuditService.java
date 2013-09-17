package mat.client.audit.service;



import java.util.List;

import mat.DTO.SearchHistoryDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Client interface for Audit Service
 *
 */
@RemoteServiceRelativePath("auditService")
public interface AuditService extends RemoteService {	
	public boolean recordMeasureEvent(String measureId, String event, String additionalInfo, boolean isChildLogRequired);	
	public void recordMeasureEvent(List<String> measureIds, String event, String additionalInfo, boolean isChildLogRequired);
	public boolean recordCodeListEvent(String codeListId, String event, String additionalInfo);
	public SearchHistoryDTO executeCodeListLogSearch(String codeListId, int startIndex, int numberOfRows,List<String> filterList);
	public SearchHistoryDTO executeMeasureLogSearch(String measureId, int startIndex, int numberOfRows,List<String> filterList);
	public boolean recordTransactionEvent(String primaryId, String secondaryId, String activityType, String userId, String additionalInfo, int logLevel);
}
