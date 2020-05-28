package mat.dto;


import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Data Transfer Object for Audit Logs (Measure, CodeList etc.,) and hasNext option
 *
 */
public class SearchHistoryDTO implements IsSerializable{

	/** The logs. */
	private List<AuditLogDTO> logs;
	
	/** The has next. */
	private boolean hasNext;
	
	/** The page count. */
	private int pageCount;
	
	/** The total results. */
	private long totalResults;
	
	/**
	 * Instantiates a new search history dto.
	 */
	public SearchHistoryDTO(){
		
	}

	/**
	 * Gets the total results.
	 * 
	 * @return the total results
	 */
	public long getTotalResults() {
		return totalResults;
	}

	/**
	 * Sets the total results.
	 * 
	 * @param totalResults
	 *            the new total results
	 */
	public void setTotalResults(long totalResults) {
		this.totalResults = totalResults;
	}

	/**
	 * Gets the logs.
	 * 
	 * @return the logs
	 */
	public List<AuditLogDTO> getLogs() {
		return logs;
	}

	/**
	 * Sets the logs.
	 * 
	 * @param logs
	 *            the new logs
	 */
	public void setLogs(List<AuditLogDTO> logs) {
		this.logs = logs;
	}

	/**
	 * Checks if is checks for next.
	 * 
	 * @return true, if is checks for next
	 */
	public boolean isHasNext() {
		return hasNext;
	}

	/**
	 * Sets the checks for next.
	 * 
	 * @param hasNext
	 *            the new checks for next
	 */
	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}
	
	/**
	 * Gets the page count.
	 * 
	 * @return the page count
	 */
	public int getPageCount() {
		return pageCount;
	}

	/**
	 * Sets the page count.
	 * 
	 * @param pageCount
	 *            the new page count
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}	
}
