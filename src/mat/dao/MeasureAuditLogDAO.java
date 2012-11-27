package mat.dao;

import java.util.List;

import mat.DTO.SearchHistoryDTO;
import mat.model.MeasureAuditLog;
import mat.model.clause.Measure;

/**
 * DAO Interface for MeasureAuditLog
 *
 */
public interface MeasureAuditLogDAO extends IDAO<MeasureAuditLog, String> {
	public boolean recordMeasureEvent(Measure measure, String event, String additionalInfo);
	public SearchHistoryDTO searchHistory(String measureId, int startIndex, int numberOfRows,List<String> filterList);
}
