package mat.server.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import mat.dto.SearchHistoryDTO;
import mat.dao.CodeListAuditLogDAO;
import mat.dao.ListObjectDAO;
import mat.model.ListObject;
import mat.server.service.CodeListAuditService;

/**
 * Service implementation for Code List Audit Service.
 */
@Service
public class CodeListAuditServiceImpl implements CodeListAuditService {

    private ListObjectDAO listObjectDAO;

    private CodeListAuditLogDAO codeListAuditLogDAO;

    public CodeListAuditServiceImpl(ListObjectDAO listObjectDAO, CodeListAuditLogDAO codeListAuditLogDAO) {
        this.listObjectDAO = listObjectDAO;
        this.codeListAuditLogDAO = codeListAuditLogDAO;
    }

    /*
     * Records the custom code list event to the CodeListAuditLog table.
     * @see mat.server.service.CodeListAuditService#recordCodeListEvent(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public boolean recordCodeListEvent(String codeListId, String event, String additionalInfo) {
        ListObject codeList = listObjectDAO.find(codeListId);
        return codeListAuditLogDAO.recordCodeListEvent(codeList, event, additionalInfo);
    }


    /*
     * Search and returns the list of events starts with the start index and the given number of rows
     */
    @Override
    public SearchHistoryDTO executeSearch(String codeListId, int startIndex, int numberOfRows, List<String> filterList) {
        return codeListAuditLogDAO.searchHistory(codeListId, startIndex, numberOfRows, filterList);
    }
}
