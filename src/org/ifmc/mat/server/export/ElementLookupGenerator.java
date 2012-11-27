package org.ifmc.mat.server.export;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.ifmc.mat.model.Category;
import org.ifmc.mat.model.ListObject;
import org.ifmc.mat.model.QualityDataSet;
import org.ifmc.mat.shared.ConstantMessages;
import org.ifmc.mat.simplexml.model.Elementlookup;
import org.ifmc.mat.simplexml.model.Headers;
import org.ifmc.mat.simplexml.model.Iqdsel;
import org.ifmc.mat.simplexml.model.Measureel;
import org.ifmc.mat.simplexml.model.Propel;
import org.ifmc.mat.simplexml.model.Qdsel;

public class ElementLookupGenerator {
	
	
	private Elementlookup lookup;
	private List<Qdsel> qdselList;
	private List<Iqdsel> iqdselList;
	private final String iprefix = "2-";
	
	public Elementlookup createElementLookup(List<QualityDataSet> qdsList, Headers headers, String userOrgOid) {
//		User currentUser = userDAO.find(LoggedInUserUtil.getLoggedInUser());
//		String userRootOid = "";
//		String userOrgOid = "";
		lookup = new Elementlookup();
		qdselList = new ArrayList<Qdsel>();
		iqdselList = new ArrayList<Iqdsel>();
//		if(currentUser != null) {
//			userRootOid = currentUser.getRootOID();
//			userOrgOid = currentUser.getOrgOID();
//		}
		List<Measureel> measureelList = new ArrayList<Measureel>();
		List<Propel> propelList = new ArrayList<Propel>();
		lookup.setQdsels(qdselList);
		lookup.setIqdsels(iqdselList);
		lookup.setMeasureels(measureelList);
		lookup.setPropels(propelList);
		
		//List<QualityDataSet> qdsList = qdsDAO.getForMeasure(measureId);
		for(QualityDataSet qds : qdsList) {
			Category category = qds.getListObject().getCategory();
			if(ConstantMessages.CATEGORY_MEASUREEL.equals(category.getId())) {
				Measureel measureel = new Measureel();
				measureel.setId(qds.getOid());
				measureel.setTaxonomy(qds.getListObject().getCodeSystem().getDescription());
				measureel.setName(qds.getListObject().getName());
				measureel.setDatatype(qds.getDataType().getDescription());
				//if(currentUser != null) {
				if(StringUtils.isNotBlank(userOrgOid)){
					measureel.setOid(qds.getListObject().getOid());
				}
				//Needs to be UUID.
				if(measureel.getName().equalsIgnoreCase(ConstantMessages.MEASUREMENT_PERIOD)){
					measureel.setUuid(headers.getPeriod().getUuid());
				}else if(measureel.getName().equalsIgnoreCase(ConstantMessages.MEASUREMENT_START_DATE)){
					measureel.setUuid(headers.getPeriod().getStartdate().getUuid());
				}else if(measureel.getName().equalsIgnoreCase(ConstantMessages.MEASUREMENT_END_DATE)){
					measureel.setUuid(headers.getPeriod().getStopdate().getUuid());
				}
				measureelList.add(measureel);
			}
			else if(ConstantMessages.CATEGORY_PROPEL.equals(category.getId())) {
				Propel propel = new Propel();
				propel.setId(qds.getOid());
				propel.setTaxonomy(qds.getListObject().getCodeSystem().getDescription());
				propel.setName(qds.getListObject().getName());
				propel.setDatatype(qds.getDataType().getDescription());
				propel.setOid(qds.getListObject().getOid());
				//Needs to be UUID.
				propel.setUuid(UUID.randomUUID().toString());
				propelList.add(propel);
			}
			else {
				//US 413 and clean up. Support Steward Other
				if(qds.getOccurrence() == null){
					Qdsel qdsel = new Qdsel();
					createQdsel(qdsel,qds,userOrgOid);
					qdselList.add(qdsel);
				}
			}
		}
		createIQdsels(userOrgOid,qdsList);//Create QDSEL first then the iqdsel.
		return lookup;
	}
	
	
	/*  
	 * This method has been created as part of US 445 and 446. This method creates iqdsels tag, when 
	 * OCCURRENCE of A to Z has been used in a measure phrase. 
	 * IQDSELS tag is not needed if Any occurrence has been used.(Just a regular qdsel tag is sufficient to represent it.
	 * For each iqdsels there should be a reference qdsels.Where refid of iqdsels should point to the referenceqdsels.
	 */
	private void createIQdsels(String userOrgOid,List<QualityDataSet> qdsList){
		for(QualityDataSet qds : qdsList) {
			Category category = qds.getListObject().getCategory();
			String occurrence = qds.getOccurrence();
			if(occurrence != null && 
					!ConstantMessages.CATEGORY_MEASUREEL.equals(category.getId()) && 
					!ConstantMessages.CATEGORY_PROPEL.equals(category.getId())){
				Qdsel referenceQdsel = getReferenceQdsel(qds, userOrgOid);
				Iqdsel iqdsel = new Iqdsel();
				ListObject iqdselListObject = qds.getListObject();
				iqdselList.add(iqdsel);
				iqdsel.setIdAttr(qds.getOid());
				iqdsel.setRefid(referenceQdsel.getIdAttr());//This refId points to the qdsel id.
				iqdsel.setIname(qds.getOccurrence());
				iqdsel.setName(iqdselListObject.getName());
				iqdsel.setDatatype(qds.getOccurrence()+ " of "+ qds.getDataType().getDescription());
				iqdsel.setUuid(UUID.randomUUID().toString());
			}
		}
	}
	
	//This method is used to create a qdsel element in the elementlookup with all it's attributes.
	private Qdsel createQdsel(Qdsel qdsel,QualityDataSet qds,String userOrgOid){
		ListObject lo = qds.getListObject();
		qdsel.setIdAttr(qds.getOid());
		qdsel.setTaxonomy(lo.getCodeSystem().getDescription());
		qdsel.setName(lo.getName());
		qdsel.setDatatype(qds.getDataType().getDescription());
		qdsel.setOid(lo.getOid());
		//Needs to be UUID.
		qdsel.setUuid(UUID.randomUUID().toString());
		qdsel.setCodesystem(userOrgOid);				
		String stewardName = lo.getSteward().getOrgName();
		if(stewardName != null && stewardName.equalsIgnoreCase("Other")){
			stewardName = lo.getStewardOther();
		}
		qdsel.setCodesystemname(stewardName);
		return qdsel;
	}
	
	/**
	 * Create a referenceQdsel if it does not already exist
	 * It will not exist if an Occurrence QDM is created for a Value Set while the Any QDM has not 
	 * @param qds
	 * @param userOrgOid
	 * @return
	 */
	private Qdsel getReferenceQdsel(QualityDataSet qds, String userOrgOid){
		ListObject lo = qds.getListObject();
		String oid = lo.getOid();
		String dataType = qds.getDataType().getDescription();
		for(Qdsel q : qdselList){
			if(q.getOid().equalsIgnoreCase(oid) && q.getDatatype().equalsIgnoreCase(dataType))
				return q;
		}
		
		Qdsel referenceQdsel = new Qdsel();
		createQdsel(referenceQdsel,qds,userOrgOid);
		referenceQdsel.setIdAttr(iprefix+qds.getOid());
		qdselList.add(referenceQdsel);
		
		return referenceQdsel;
	}
	
}
