package org.ifmc.mat.dao;

import java.util.List;

import org.ifmc.mat.DTO.SearchHistoryDTO;
import org.ifmc.mat.model.MeasureAuditLog;
import org.ifmc.mat.model.clause.Measure;

/**
 * DAO Interface for MeasureAuditLog
 *
 */
public interface MeasureAuditLogDAO extends IDAO<MeasureAuditLog, String> {
	public boolean recordMeasureEvent(Measure measure, String event, String additionalInfo);
	public SearchHistoryDTO searchHistory(String measureId, int startIndex, int numberOfRows,List<String> filterList);
}
