package org.ifmc.mat.server.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ifmc.mat.client.measurepackage.MeasurePackageClauseDetail;
import org.ifmc.mat.client.measurepackage.MeasurePackageDetail;
import org.ifmc.mat.client.measurepackage.MeasurePackageOverview;
import org.ifmc.mat.dao.QualityDataSetDAO;
import org.ifmc.mat.dao.clause.ClauseDAO;
import org.ifmc.mat.dao.clause.DecisionDAO;
import org.ifmc.mat.dao.clause.MeasureDAO;
import org.ifmc.mat.dao.clause.PackagerDAO;
import org.ifmc.mat.model.QualityDataSet;
import org.ifmc.mat.model.QualityDataSetDTO;
import org.ifmc.mat.model.clause.Clause;
import org.ifmc.mat.model.clause.Decision;
import org.ifmc.mat.model.clause.Measure;
import org.ifmc.mat.model.clause.Packager;
import org.ifmc.mat.server.service.PackagerService;
import org.ifmc.mat.shared.ConstantMessages;
import org.springframework.beans.factory.annotation.Autowired;

public class PackagerServiceImpl implements PackagerService {
	private static final Log logger = LogFactory.getLog(PackagerServiceImpl.class);
	 
	@Autowired
	private PackagerDAO packagerDAO;
	@Autowired 
	private ClauseDAO clauseDAO;
	@Autowired
	private MeasureDAO measureDAO;
	@Autowired 
	private DecisionDAO decisionDAO;
	@Autowired 
	private QualityDataSetDAO qualityDataSetDAO;
	
	@Override
	public void save(MeasurePackageDetail detail) {
		packagerDAO.deletePackage(detail.getMeasureId(), detail.getSequence());
		Measure measure = measureDAO.find(detail.getMeasureId());
		int sequence = Integer.valueOf(detail.getSequence());
			
		for(MeasurePackageClauseDetail nvp : detail.getPackageClauses()) {
			Packager pkgr = new Packager();
			pkgr.setMeasure(measure);
			pkgr.setClause(clauseDAO.find(nvp.getId()));
			pkgr.setSequence(sequence);
			packagerDAO.save(pkgr);
		}
	}


	@Override
	public MeasurePackageOverview buildOverviewForMeasure(String measureId) {
		
		MeasurePackageOverview overview = new MeasurePackageOverview();
		
		List<MeasurePackageClauseDetail> clauses = new ArrayList<MeasurePackageClauseDetail>();
		List<Clause> clauseList = clauseDAO.findByMeasureId(measureId, null);
		for(Clause c : clauseList) {
			String clauseContextId = c.getContextId();
			
			/*
			 * 1) if population, numerator, or denominator (clause id = 1-2) and clause id = 4, 6 and 7 then add to clause list
			 * 2) ** if numerator exclusions, denominator exclusions or denominator exceptions (clause id = 3 or 4 or 5), add to clause list if there is any content deeper than top level AND
			 * ** handled by clauseHasContent
			 * 3) else do not add to clause list 
			 */
			
				
			
			//TODO Think about only displaying the clauses needed with regards to the scoring type
			//TODO Replace these with string constants from ConstantMessages.java
			//US497
			boolean doadd = clauseContextId.equals(ConstantMessages.POPULATION_CONTEXT_ID) || clauseContextId.equals(ConstantMessages.NUMERATOR_CONTEXT_ID) 
					|| clauseContextId.equals(ConstantMessages.DENOMINATOR_CONTEXT_ID) || clauseContextId.equals(ConstantMessages.MEASURE_POPULATION_CONTEXT_ID)
					|| clauseContextId.equals(ConstantMessages.MEASURE_OBSERVATION_CONTEXT_ID) ? clauseHasContentForPopulationNumeratorDenominator(c) :
					clauseContextId.equals(ConstantMessages.NUMERATOR_EXCLUSIONS_CONTEXT_ID) || clauseContextId.equals(ConstantMessages.DENOMINATOR_EXCLUSIONS_CONTEXT_ID) 
					|| clauseContextId.equals(ConstantMessages.DENOMINATOR_EXCEPTIONS_CONTEXT_ID) ? clauseHasContentForExclusionsExceptions(c) : false;
			if(doadd) {
				clauses.add(extractClause(c));
			}
		}
		
		List<MeasurePackageDetail> pkgs = new ArrayList<MeasurePackageDetail>();
		Map<Integer, MeasurePackageDetail> seqDetailMap = 
			new HashMap<Integer, MeasurePackageDetail>();
		List<Packager> pList = getForMeasure(measureId);
		for(Packager p : pList) {
			MeasurePackageDetail detail = seqDetailMap.get(p.getSequence());
			if(detail == null) {
				detail = new MeasurePackageDetail();
				detail.setSequence(Integer.toString(p.getSequence()));
				detail.setMeasureId(measureId);
				seqDetailMap.put(p.getSequence(), detail);
				pkgs.add(detail);
			}
			
			detail.getPackageClauses().add(extractClause(p.getClause()));
		}
		
		overview.setClauses(clauses);
		overview.setPackages(pkgs);
				
		overview.setQdmElements(getQDMElements(measureId));
		
		return overview;
	}
	
	//filtered out QDM Elements of type attribute timing element and occurrence US613
	private List<QualityDataSetDTO> getQDMElements(String measureId) {
		List<QualityDataSetDTO> qdmElements = qualityDataSetDAO.getQDSElements(true, measureId);
		List<QualityDataSetDTO> filteredQDMElements = new ArrayList<QualityDataSetDTO>();
		for(QualityDataSetDTO dataSet : qdmElements) {
			if(dataSet.getDataType() != null && !dataSet.getDataType().equalsIgnoreCase(ConstantMessages.TIMING_ELEMENT)
					&& !dataSet.getDataType().equalsIgnoreCase(ConstantMessages.ATTRIBUTE) && StringUtils.isBlank(dataSet.getOccurrenceText())){
				filteredQDMElements.add(dataSet);
			}
		}
		return filteredQDMElements;
	}

	private MeasurePackageClauseDetail extractClause(Clause clause) {
		MeasurePackageClauseDetail detail = new MeasurePackageClauseDetail();
		detail.setId(clause.getId());
		detail.setName(clause.getName());
		detail.setType(clause.getContextId());
		return detail;
	}

	private List<Packager> getForMeasure(String measureId) {
		return packagerDAO.getForMeasure(measureId);
	}

	@Override
	public void delete(MeasurePackageDetail detail) {
		packagerDAO.deletePackage(detail.getMeasureId(), detail.getSequence());
	}

	/**
	 * return true if more than top level conditional
	 * @param c
	 * @return
	 */
	private boolean clauseHasContentForExclusionsExceptions(Clause c) {
		try{
			Clause c1 = clauseDAO.find(c.getId());
			Decision d = decisionDAO.find(c1.getDecision().getId());
			while(d.getOperator().equals("CLAUSE") && d.getChildDecisions().size()==1)
				d = d.getChildDecisions().toArray(new Decision[d.getChildDecisions().size()])[0];
			if(d.getChildDecisions().isEmpty() || !d.getOperator().equals("AND"))
				return false;
			else{
//				Decision child = d.getChildDecisions().toArray(new Decision[d.getChildDecisions().size()])[0];
//				return !child.getChildDecisions().isEmpty();
				return !d.getChildDecisions().isEmpty();
			}
			
		}catch(Exception e){
			return false;
		}
	}
	
	private boolean clauseHasContentForPopulationNumeratorDenominator(Clause c) {
		try{
			Clause c1 = clauseDAO.find(c.getId());
			Decision d = decisionDAO.find(c1.getDecision().getId());
			while(d.getOperator().equals("CLAUSE") && d.getChildDecisions().size()==1)
				d = d.getChildDecisions().toArray(new Decision[d.getChildDecisions().size()])[0];
			return d.getOperator().equals("AND");
//			return !d.getChildDecisions().isEmpty();
		}catch(Exception e){
			return false;
		}
	}
	
	@Override
	public void saveQDMData(MeasurePackageDetail detail) {
		
		List<String> qdmids = new ArrayList<String>();
		for(QualityDataSetDTO qdsd : detail.getSuppDataElements()) {
			qdmids.add(qdsd.getId());
		}
		
		for(QualityDataSetDTO qdsd : detail.getQdmElements()) {
			qdmids.add(qdsd.getId());
		}
		List<QualityDataSet> qdms = qualityDataSetDAO.getQDMsById(qdmids);
		for(QualityDataSet qds : qdms) {
			// save supplemental data elements	
			for(QualityDataSetDTO suppData : detail.getSuppDataElements()) {
				if(qds.getId() != null && qds.getId().equals(suppData.getId())){
					qds.setSuppDataElement(true);
					qualityDataSetDAO.save(qds);
					break;
				}
			}
			// save qdm elements
			for(QualityDataSetDTO qdm : detail.getQdmElements()) {
				if(qds.getId() != null && qds.getId().equals(qdm.getId())){
					qds.setSuppDataElement(false);
					qualityDataSetDAO.save(qds);
					break;
				}
			}
		}
			
	}
	
}
