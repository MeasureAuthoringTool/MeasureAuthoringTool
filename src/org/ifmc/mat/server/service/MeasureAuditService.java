package org.ifmc.mat.server.service;

import java.util.List;

import org.ifmc.mat.DTO.SearchHistoryDTO;


/**
 * Interface for Measure Audit Service
 *
 */
public interface MeasureAuditService {
	public boolean recordMeasureEvent(String measureId, String event, String additionalInfo, boolean isChildLogRequired);
	public SearchHistoryDTO executeSearch(String measureId, int startIndex, int numberOfRows,List<String> filterList);
}
