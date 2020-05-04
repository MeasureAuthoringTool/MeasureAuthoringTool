package mat.dao;

import java.util.List;

import mat.dto.SearchHistoryDTO;
import mat.model.CQLAuditLog;
import mat.model.clause.CQLLibrary;

/**
 * DAO Interface for CQLAuditLogDAO.
 */
public interface CQLLibraryAuditLogDAO extends IDAO<CQLAuditLog, String> {
	
	/**
	 * Record Cql library event.
	 * 
	 * @param cqlLibrary
	 *            the CQLLibrary
	 * @param event
	 *            the event
	 * @param additionalInfo
	 *            the additional info
	 * @return true, if successful
	 */
	public boolean recordCQLLibraryEvent(CQLLibrary cqlLibrary, String event, String additionalInfo);
	
	/**
	 * Search history.
	 * 
	 * @param CQLID
	 *            the Cql id
	 * @param startIndex
	 *            the start index
	 * @param numberOfRows
	 *            the number of rows
	 * @param filterList
	 *            the filter list
	 * @return the search history dto
	 */
	public SearchHistoryDTO searchHistory(String cqlId, int startIndex, int numberOfRows,List<String> filterList);
}
