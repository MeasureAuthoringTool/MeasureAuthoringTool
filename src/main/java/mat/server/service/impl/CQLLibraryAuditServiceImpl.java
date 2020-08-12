package mat.server.service.impl;


import mat.dao.CQLLibraryAuditLogDAO;
import mat.dao.clause.CQLLibraryDAO;
import mat.dto.SearchHistoryDTO;
import mat.model.clause.CQLLibrary;
import mat.server.service.CQLLibraryAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for Measure Audit Service.
 */
@Service
public class CQLLibraryAuditServiceImpl implements CQLLibraryAuditService{
	
	/** The measure dao. */
	@Autowired
	private CQLLibraryDAO cqlLibraryDAO;

	/** The measure audit log dao. */
	@Autowired
	private CQLLibraryAuditLogDAO cqlAuditLogDAO;
 
	
	@Override
	public boolean recordCQLLibraryEvent(String cqlId, String event, String additionalInfo, boolean isChildLogRequired){
		boolean result = false;
		CQLLibrary measure = cqlLibraryDAO.find(cqlId);
		result = cqlAuditLogDAO.recordCQLLibraryEvent(measure, event, additionalInfo);
		

		return result;
	}

	@Override
	public SearchHistoryDTO executeSearch(String cqlId, int startIndex, int numberOfRows,List<String> filterList){
		return cqlAuditLogDAO.searchHistory(cqlId, startIndex, numberOfRows,filterList);
	}	
}
