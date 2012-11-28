package mat.server.service.impl;

import java.util.List;

import mat.DTO.SearchHistoryDTO;
import mat.dao.CodeListAuditLogDAO;
import mat.dao.ListObjectDAO;
import mat.model.ListObject;
import mat.server.service.CodeListAuditService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Service implementation for Code List Audit Service
 *
 */
public class CodeListAuditServiceImpl implements CodeListAuditService{
	private static final Log logger = LogFactory.getLog(CodeListAuditServiceImpl.class);
	
	@Autowired
	private ListObjectDAO listObjectDAO;

	@Autowired
	private CodeListAuditLogDAO codeListAuditLogDAO;
	
	
	/* (non-Javadoc)Records the custom code list event to the CodeListAuditLog table.
	 * @see mat.server.service.CodeListAuditService#recordCodeListEvent(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean recordCodeListEvent(String codeListId, String event, String additionalInfo){
		ListObject codeList = listObjectDAO.find(codeListId);
		return codeListAuditLogDAO.recordCodeListEvent(codeList, event, additionalInfo);
	}
	
	
	/* Search and returns the list of events starts with the start index and the given number of rows
	 * @see mat.server.service.CodeListAuditService#executeSearch(java.lang.String, int, int)
	 */
	@Override
	public SearchHistoryDTO executeSearch(String codeListId, int startIndex, int numberOfRows,List<String> filterList){
		return codeListAuditLogDAO.searchHistory(codeListId, startIndex, numberOfRows,filterList);
	}
}
