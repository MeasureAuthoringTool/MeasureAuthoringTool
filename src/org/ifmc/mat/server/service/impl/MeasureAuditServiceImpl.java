package org.ifmc.mat.server.service.impl;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ifmc.mat.DTO.SearchHistoryDTO;
import org.ifmc.mat.dao.CodeListAuditLogDAO;
import org.ifmc.mat.dao.MeasureAuditLogDAO;
import org.ifmc.mat.dao.QualityDataSetDAO;
import org.ifmc.mat.dao.clause.MeasureDAO;
import org.ifmc.mat.model.ListObject;
import org.ifmc.mat.model.QualityDataSet;
import org.ifmc.mat.model.clause.Measure;
import org.ifmc.mat.server.service.MeasureAuditService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Service implementation for Measure Audit Service
 *
 */
public class MeasureAuditServiceImpl implements MeasureAuditService{
	private static final Log logger = LogFactory.getLog(MeasureAuditServiceImpl.class);
	
	@Autowired
	private MeasureDAO measureDAO;

	@Autowired
	private MeasureAuditLogDAO measureAuditLogDAO;
	
	@Autowired
	private CodeListAuditLogDAO codeListAuditLogDAO; 
	
	@Autowired
	private QualityDataSetDAO qualityDataSetDAO; 
	
	/* Records the custom measure event to the MeasureAuditLog table.
	 * @see org.ifmc.mat.server.service.MeasureAuditService#recordMeasureEvent(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean recordMeasureEvent(String measureId, String event, String additionalInfo, boolean isChildLogRequired){
		boolean result = false;
		Measure measure = measureDAO.find(measureId);
		result = measureAuditLogDAO.recordMeasureEvent(measure, event, additionalInfo);
		
		//check if the event 
		if(result && isChildLogRequired){
			//find out all the code list applied to this measure.
			List<QualityDataSet> qdmList = qualityDataSetDAO.getForMeasure(measureId);
			for(QualityDataSet qdm: qdmList){
				ListObject codeList = qdm.getListObject();
				result = result && codeListAuditLogDAO.recordCodeListEvent(codeList, event, codeList.getName());
			}
		}

		return result;
	}

	/* Search and returns the list of events starts with the start index and the given number of rows
	 * @see org.ifmc.mat.server.service.MeasureAuditService#executeSearch(java.lang.String, int, int)
	 */
	@Override
	public SearchHistoryDTO executeSearch(String measureId, int startIndex, int numberOfRows,List<String> filterList){
		return measureAuditLogDAO.searchHistory(measureId, startIndex, numberOfRows,filterList);
	}	
}
