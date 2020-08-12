package mat.client.audit.service;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import mat.dto.SearchHistoryDTO;
import mat.dto.UserAuditLogDTO;

import java.util.List;

/**
 * Client interface for Audit Service.
 */
@RemoteServiceRelativePath("auditService")
public interface AuditService extends RemoteService {
	
	/**
	 * Record measure event.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param event
	 *            the event
	 * @param additionalInfo
	 *            the additional info
	 * @param isChildLogRequired
	 *            the is child log required
	 * @return true, if successful
	 */
	public boolean recordMeasureEvent(String measureId, String event, String additionalInfo, boolean isChildLogRequired);
	
	/**
	 * Record measure event.
	 * 
	 * @param measureIds
	 *            the measure ids
	 * @param event
	 *            the event
	 * @param additionalInfo
	 *            the additional info
	 * @param isChildLogRequired
	 *            the is child log required
	 */
	public void recordMeasureEvent(List<String> measureIds, String event, String additionalInfo, boolean isChildLogRequired);
	
	/**
	 * Record code list event.
	 * 
	 * @param codeListId
	 *            the code list id
	 * @param event
	 *            the event
	 * @param additionalInfo
	 *            the additional info
	 * @return true, if successful
	 */
	public boolean recordCodeListEvent(String codeListId, String event, String additionalInfo);
	
	/**
	 * Execute code list log search.
	 * 
	 * @param codeListId
	 *            the code list id
	 * @param startIndex
	 *            the start index
	 * @param numberOfRows
	 *            the number of rows
	 * @param filterList
	 *            the filter list
	 * @return the search history dto
	 */
	public SearchHistoryDTO executeCodeListLogSearch(String codeListId, int startIndex, int numberOfRows, List<String> filterList);
	
	/**
	 * Execute measure log search.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param startIndex
	 *            the start index
	 * @param numberOfRows
	 *            the number of rows
	 * @param filterList
	 *            the filter list
	 * @return the search history dto
	 */
	public SearchHistoryDTO executeMeasureLogSearch(String measureId, int startIndex, int numberOfRows, List<String> filterList);
	
	/**
	 * Record transaction event.
	 * 
	 * @param primaryId
	 *            the primary id
	 * @param secondaryId
	 *            the secondary id
	 * @param activityType
	 *            the activity type
	 * @param userId
	 *            the user id
	 * @param additionalInfo
	 *            the additional info
	 * @param logLevel
	 *            the log level
	 * @return true, if successful
	 */
	public boolean recordTransactionEvent(String primaryId, String secondaryId, String activityType,
			String userId, String additionalInfo, int logLevel);

	boolean recordUserEvent(String userId, List<String> event, String additionalInfo,
			boolean isChildLogRequired);

	List<UserAuditLogDTO> executeUserLogSearch(String userId);

	public boolean recordCQLLibraryEvent(String cqlId, String event, String additionalInfo, boolean isChildLogRequired);

	SearchHistoryDTO executeSearch(String cqlId, int startIndex, int numberOfRows, List<String> filterList);
}
