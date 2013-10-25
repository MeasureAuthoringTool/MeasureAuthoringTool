package mat.server.service;

import java.util.List;

import mat.DTO.SearchHistoryDTO;


/**
 * Interface for Measure Audit Service.
 */
public interface MeasureAuditService {
	
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
	 * Execute search.
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
	public SearchHistoryDTO executeSearch(String measureId, int startIndex, int numberOfRows,List<String> filterList);
}
