package mat.server.export;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mat.dao.clause.PackagerDAO;
import mat.model.clause.Clause;
import mat.model.clause.Packager;
import mat.shared.ConstantMessages;
import mat.simplexml.model.Attachment;
import mat.simplexml.model.Attachments;
import mat.simplexml.model.Criterion;
import mat.simplexml.model.CriterionWithAttachments;

import org.springframework.beans.factory.annotation.Autowired;

public class AttachmentGenerator {
	@Autowired 
	private PackagerDAO packageDAO;
	
	public static class PackageInfo {
		public List<Clause> numerators = new ArrayList<Clause>();
		public Clause denominator;
		public Clause population;
		public List<Clause> exceptions = new ArrayList<Clause>();
		public List<Clause> exclusions = new ArrayList<Clause>();
		public List<Clause> numExclusions = new ArrayList<Clause>();
		public List<Clause> measurePopulation = new ArrayList<Clause>();
		public List<Clause> measureObservation = new ArrayList<Clause>();
	}
	
	//TODO Handle Numerator exclusions
	public Collection<PackageInfo> buildPackageInfoForMeasure(String measureId) {
		
		List<Packager> packagerList = packageDAO.getForMeasure(measureId);
		Map<String, PackageInfo> packageInfoMap = new HashMap<String, PackageInfo>();
		for(Packager pkger : packagerList) {
			PackageInfo packageInfo = packageInfoMap.get("" + pkger.getSequence());
			if(packageInfo == null) {
				packageInfo = new PackageInfo();
				packageInfoMap.put("" + pkger.getSequence(), packageInfo);
			}
			
			String contextId = pkger.getClause().getContextId();
			if (contextId.equals(ConstantMessages.POPULATION_CONTEXT_ID)) {
				if(packageInfo.population != null) {
					throw new RuntimeException("Package had multiple populations for measure " + measureId);
				}
				packageInfo.population = pkger.getClause();
			}
			else if (contextId.equals(ConstantMessages.NUMERATOR_CONTEXT_ID)) {
				packageInfo.numerators.add(pkger.getClause());
			}
			else if (contextId.equals(ConstantMessages.NUMERATOR_EXCLUSIONS_CONTEXT_ID)) {
				if(packageInfo.numExclusions.size() != 0) {
					// TODO see if this needs to be thrown as it is for the denominator
					//throw new RuntimeException("Package had multiple numerator exclusions for measure " + measureId);
				}
				packageInfo.numExclusions.add(pkger.getClause());
			}
			
			else if (contextId.equals(ConstantMessages.DENOMINATOR_CONTEXT_ID)) {
				if(packageInfo.denominator != null) {
					throw new RuntimeException("Package had multiple denominators for measure " + measureId);
				}
				packageInfo.denominator = pkger.getClause();
			} 
			else if (contextId.equals(ConstantMessages.DENOMINATOR_EXCLUSIONS_CONTEXT_ID)) {
				if(packageInfo.exclusions.size() != 0) {
					throw new RuntimeException("Package had multiple denominator exclusions for measure " + measureId);
				}
				packageInfo.exclusions.add(pkger.getClause());
			}
			else if (contextId.equals(ConstantMessages.DENOMINATOR_EXCEPTIONS_CONTEXT_ID)) {
				packageInfo.exceptions.add(pkger.getClause());
			}else if(contextId.equals(ConstantMessages.MEASURE_POPULATION_CONTEXT_ID)){
				//TODO422:- Need to do validation here for MeasurePopulation.
				packageInfo.measurePopulation.add(pkger.getClause());
			}else if(contextId.equals(ConstantMessages.MEASURE_OBSERVATION_CONTEXT_ID)){
				//TODO422:- Need to do validation here for MeasureObservation.
				packageInfo.measureObservation.add(pkger.getClause());
			}
		}
		return packageInfoMap.values();
	}
	
	
	public void addAttachment(CriterionWithAttachments c, Criterion clause, String clauseStr, String title) {
		Attachment attachment = new Attachment();
		attachment.setUuid(clause.getUuid());
		attachment.setClause(clauseStr);
		attachment.setTitle(title);
		
		if(c.getAttachments() == null) {
			c.setAttachments(new Attachments());
		}
		c.getAttachments().getAttachment().add(attachment);
	}

}
