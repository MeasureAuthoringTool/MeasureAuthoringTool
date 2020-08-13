package mat.dao;

import mat.dto.SearchHistoryDTO;
import mat.model.CodeListAuditLog;
import mat.model.ListObject;

import java.util.List;

/**
 * DAO Interface for CodeListAuditLog.
 */
public interface CodeListAuditLogDAO extends IDAO<CodeListAuditLog, String> {
	
	/**
	 * Record code list event.
	 * 
	 * @param codeList
	 *            the code list
	 * @param event
	 *            the event
	 * @param additionalInfo
	 *            the additional info
	 * @return true, if successful
	 */
	public boolean recordCodeListEvent(ListObject codeList, String event, String additionalInfo);
	
	/**
	 * Search history.
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
	public SearchHistoryDTO searchHistory(String codeListId, int startIndex, int numberOfRows, List<String> filterList);
}
