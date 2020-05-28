package mat.server.service;

import java.util.List;

import mat.dto.SearchHistoryDTO;


/**
 * Interface for Code List Audit Service.
 */
public interface CodeListAuditService {
	
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
	 * Execute search.
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
	public SearchHistoryDTO executeSearch(String codeListId, int startIndex, int numberOfRows,List<String> filterList);
}
