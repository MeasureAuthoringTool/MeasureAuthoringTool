package mat.server.service.impl;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import mat.DTO.SearchHistoryDTO;
import mat.dao.CQLLibraryAuditLogDAO;
import mat.dao.clause.CQLLibraryDAO;
import mat.model.clause.CQLLibrary;
import mat.server.service.CQLLibraryAuditService;

/**
 * Service implementation for Measure Audit Service.
 */
public class CQLLibraryAuditServiceImpl implements CQLLibraryAuditService{
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(CQLLibraryAuditServiceImpl.class);
	
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
