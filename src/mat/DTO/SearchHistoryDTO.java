package mat.DTO;


import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Data Transfer Object for Audit Logs (Measure, CodeList etc.,) and hasNext option
 *
 */
public class SearchHistoryDTO implements IsSerializable{

	private List<AuditLogDTO> logs;
	private boolean hasNext;
	private int pageCount;
	private long totalResults;
	
	public SearchHistoryDTO(){
		
	}

	public long getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(long totalResults) {
		this.totalResults = totalResults;
	}

	public List<AuditLogDTO> getLogs() {
		return logs;
	}

	public void setLogs(List<AuditLogDTO> logs) {
		this.logs = logs;
	}

	public boolean isHasNext() {
		return hasNext;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}
	
	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}	
}
