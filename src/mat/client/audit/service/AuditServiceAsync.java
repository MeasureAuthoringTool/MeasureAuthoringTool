package mat.client.audit.service;


import java.util.List;

import mat.DTO.SearchHistoryDTO;
import mat.client.login.service.AsynchronousService;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AuditServiceAsync extends AsynchronousService{

	void recordMeasureEvent(String measureId, String event, String additionalInfo, boolean isChildLogRequired, AsyncCallback<Boolean> callback);
	void recordMeasureEvent(List<String> measureIds, String event, String additionalInfo, boolean isChildLogRequired, AsyncCallback<Void> callback);
	void recordCodeListEvent(String codeListId, String event, String additionalInfo, AsyncCallback<Boolean> callback);
	void executeCodeListLogSearch(String codeListId, int startIndex, int numberOfRows,List<String> filterList, AsyncCallback<SearchHistoryDTO> callback);
	void executeMeasureLogSearch(String measureId, int startIndex, int numberOfRows,List<String> filterList, AsyncCallback<SearchHistoryDTO> callback);
	void recordTransactionEvent(String primaryId, String secondaryId, String activityType, String userId, String additionalInfo, int logLevel, AsyncCallback<Boolean> callback);
	
}
