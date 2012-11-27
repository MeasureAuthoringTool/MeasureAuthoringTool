package mat.dao;

import java.util.List;

import mat.DTO.SearchHistoryDTO;
import mat.model.CodeListAuditLog;
import mat.model.ListObject;

/**
 * DAO Interface for CodeListAuditLog
 *
 */
public interface CodeListAuditLogDAO extends IDAO<CodeListAuditLog, String> {
	public boolean recordCodeListEvent(ListObject codeList, String event, String additionalInfo);
	public SearchHistoryDTO searchHistory(String codeListId, int startIndex, int numberOfRows, List<String> filterList);
}
