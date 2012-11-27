package org.ifmc.mat.server.service;

import java.util.List;

import org.ifmc.mat.DTO.SearchHistoryDTO;


/**
 * Interface for Code List Audit Service
 *
 */
public interface CodeListAuditService {
	public boolean recordCodeListEvent(String codeListId, String event, String additionalInfo);
	public SearchHistoryDTO executeSearch(String codeListId, int startIndex, int numberOfRows,List<String> filterList);
}
