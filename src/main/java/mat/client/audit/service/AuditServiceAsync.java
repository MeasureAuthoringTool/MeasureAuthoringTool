package mat.client.audit.service;


import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mat.dto.SearchHistoryDTO;
import mat.dto.UserAuditLogDTO;
import mat.client.login.service.AsynchronousService;

/**
 * The Interface AuditServiceAsync.
 */
public interface AuditServiceAsync extends AsynchronousService {

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
	 * @param callback
	 *            the callback
	 */
	void recordMeasureEvent(String measureId, String event, String additionalInfo, boolean isChildLogRequired,
			AsyncCallback<Boolean> callback);
	
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
	 * @param callback
	 *            the callback
	 */
	void recordMeasureEvent(List<String> measureIds, String event, String additionalInfo, boolean isChildLogRequired,
			AsyncCallback<Void> callback);
	
	/**
	 * Record code list event.
	 * 
	 * @param codeListId
	 *            the code list id
	 * @param event
	 *            the event
	 * @param additionalInfo
	 *            the additional info
	 * @param callback
	 *            the callback
	 */
	void recordCodeListEvent(String codeListId, String event, String additionalInfo, AsyncCallback<Boolean> callback);
	
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
	 * @param callback
	 *            the callback
	 */
	void executeCodeListLogSearch(String codeListId, int startIndex, int numberOfRows, List<String> filterList,
			AsyncCallback<SearchHistoryDTO> callback);
	
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
	 * @param callback
	 *            the callback
	 */
	void executeMeasureLogSearch(String measureId, int startIndex, int numberOfRows, List<String> filterList,
			AsyncCallback<SearchHistoryDTO> callback);
	
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
	 * @param callback
	 *            the callback
	 */
	void recordTransactionEvent(String primaryId, String secondaryId, String activityType, String userId,
			String additionalInfo, int logLevel, AsyncCallback<Boolean> callback);
	
	
	void recordUserEvent(String userId, List<String> event, String additionalInfo, boolean isChildLogRequired,
			AsyncCallback<Boolean> callback);
	
	
	void executeUserLogSearch(String userId, AsyncCallback<List<UserAuditLogDTO>> callback);

	void recordCQLLibraryEvent(String cqlId, String event, String additionalInfo, boolean isChildLogRequired,
			AsyncCallback<Boolean> callback);

	void executeSearch(String cqlId, int startIndex, int numberOfRows, List<String> filterList,
			AsyncCallback<SearchHistoryDTO> callback);
}
