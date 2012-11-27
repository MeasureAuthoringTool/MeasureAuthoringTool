package org.ifmc.mat.dao;

import java.util.List;

import org.ifmc.mat.DTO.SearchHistoryDTO;
import org.ifmc.mat.model.CodeListAuditLog;
import org.ifmc.mat.model.ListObject;

/**
 * DAO Interface for CodeListAuditLog
 *
 */
public interface CodeListAuditLogDAO extends IDAO<CodeListAuditLog, String> {
	public boolean recordCodeListEvent(ListObject codeList, String event, String additionalInfo);
	public SearchHistoryDTO searchHistory(String codeListId, int startIndex, int numberOfRows, List<String> filterList);
}
