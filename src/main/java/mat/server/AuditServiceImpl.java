package mat.server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import mat.dto.SearchHistoryDTO;
import mat.dto.UserAuditLogDTO;
import mat.client.audit.service.AuditService;
import mat.server.service.CQLLibraryAuditService;
import mat.server.service.CodeListAuditService;
import mat.server.service.MeasureAuditService;
import mat.server.service.TransactionAuditService;
import mat.server.service.UserAuditService;

/**
 * Client implementation of Audit Service Implementation.
 */
@SuppressWarnings("serial")
public class AuditServiceImpl extends SpringRemoteServiceServlet implements AuditService {

    @Autowired
    private MeasureAuditService measureAuditService;

    @Autowired
    private CodeListAuditService codeListAuditService;

    @Autowired
    private CQLLibraryAuditService cqlLibraryAuditService;

    @Autowired
    private UserAuditService userAuditService;

    @Autowired
    private TransactionAuditService transactionAuditService;

    @Override
    public boolean recordCQLLibraryEvent(String cqlId, String event, String additionalInfo, boolean isChildLogRequired) {
        return getCQLAuditService().recordCQLLibraryEvent(cqlId, event, additionalInfo, isChildLogRequired);
    }

    @Override
    public SearchHistoryDTO executeSearch(String cqlId, int startIndex, int numberOfRows, List<String> filterList) {
        return getCQLAuditService().executeSearch(cqlId, startIndex, numberOfRows, filterList);
    }

    /* Records an measure event
     * @see mat.client.audit.service.AuditService#recordMeasureEvent(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public boolean recordMeasureEvent(String measureId, String event, String additionalInfo, boolean isChildLogRequired) {
        return getMeasureAuditService().recordMeasureEvent(measureId, event, additionalInfo, isChildLogRequired);
    }

    /* Records an code list event
     * @see mat.client.audit.service.AuditService#recordCodeListEvent(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public boolean recordCodeListEvent(String codeListId, String event, String additionalInfo) {
        return getCodeListAuditService().recordCodeListEvent(codeListId, event, additionalInfo);
    }

    /* Returns the code list log search result for a given code list id
     * @see mat.client.audit.service.AuditService#executeCodeListLogSearch(java.lang.String, int, int)
     */
    @Override
    public SearchHistoryDTO executeCodeListLogSearch(String codeListId, int startIndex, int numberOfRows, List<String> filterList) {
        return getCodeListAuditService().executeSearch(codeListId, startIndex, numberOfRows, filterList);
    }


    /* Returns the measure log search result for a given code list id
     * @see mat.client.audit.service.AuditService#executeMeasureLogSearch(java.lang.String, int, int)
     */
    @Override
    public SearchHistoryDTO executeMeasureLogSearch(String measureId, int startIndex, int numberOfRows, List<String> filterList) {
        return getMeasureAuditService().executeSearch(measureId, startIndex, numberOfRows, filterList);
    }

    private TransactionAuditService getTransactionAuditService() {
        return transactionAuditService;
    }

    @Override
    public boolean recordTransactionEvent(String primaryId, String secondaryId,
                                          String activityType, String userId, String additionalInfo, int logLevel) {
        return getTransactionAuditService().recordTransactionEvent(primaryId, secondaryId, activityType, userId, additionalInfo, logLevel);
    }

    @Override
    public void recordMeasureEvent(List<String> measureIds, String event, String additionalInfo, boolean isChildLogRequired) {
        for (String measureId : measureIds) {
            recordMeasureEvent(measureId, event, additionalInfo, isChildLogRequired);
        }
    }

    public UserAuditService getUserAuditService() {
        return userAuditService;
    }

    @Override
    public boolean recordUserEvent(String userId, List<String> event, String additionalInfo, boolean isChildLogRequired) {
        return getUserAuditService().recordUserEvent(userId, event, additionalInfo, isChildLogRequired);
    }

    @Override
    public List<UserAuditLogDTO> executeUserLogSearch(String userId) {
        return getUserAuditService().searchHistory(userId);
    }

    private MeasureAuditService getMeasureAuditService() {
        return measureAuditService;
    }

    private CodeListAuditService getCodeListAuditService() {
        return codeListAuditService;
    }

    private CQLLibraryAuditService getCQLAuditService() {
        return cqlLibraryAuditService;
    }

}
