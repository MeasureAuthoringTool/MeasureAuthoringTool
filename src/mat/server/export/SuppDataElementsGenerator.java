package mat.server.export;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import mat.model.Category;
import mat.model.ListObject;
import mat.model.QualityDataSet;
import mat.shared.ConstantMessages;
import mat.simplexml.model.Headers;
import mat.simplexml.model.Iqdsel;
import mat.simplexml.model.Qdsel;
import mat.simplexml.model.SupplementalDataElements;

public class SuppDataElementsGenerator {
		
	private SupplementalDataElements suppData;
	private List<Qdsel> qdselList;
	private List<Iqdsel> iqdselList;
	private final String iprefix = "2-";
	
	public SupplementalDataElements createSuppDataElements(List<QualityDataSet> qdsList, Headers headers, String userOrgOid) {
		suppData = new SupplementalDataElements();
		qdselList = new ArrayList<Qdsel>();
		iqdselList = new ArrayList<Iqdsel>();
		suppData.setQdsels(qdselList);
		suppData.setIqdsels(iqdselList);
		
		for(QualityDataSet qds : qdsList) {
			//US 413 and clean up. Support Steward Other
			if(qds.getOccurrence() == null && qds.isSuppDataElement()){
				Qdsel qdsel = new Qdsel();
				createQdsel(qdsel,qds,userOrgOid);
				qdselList.add(qdsel);
			}
		}
		//createIQdsels(userOrgOid,qdsList);//Create QDSEL first then the iqdsel.
		return suppData;
	}
	
	
	/*  
	 * This method has been created as part of US 445 and 446. This method creates iqdsels tag, when 
	 * OCCURRENC of A to Z has been used in a measure phrase. 
	 * IQDSELS tag is not needed if Any occurrence has been used.(Just a regular qdsel tag is sufficient to represent it.
	 * For each iqdsels there should be a reference qdsels.Where refid of iqdsels should point to the referenceqdsels.
	 */
	private void createIQdsels(String userOrgOid,List<QualityDataSet> qdsList){
		for(QualityDataSet qds : qdsList) {
			Category category = qds.getListObject().getCategory();
			String occurrence = qds.getOccurrence();
			if(occurrence != null && 
					!ConstantMessages.CATEGORY_MEASUREEL.equals(category.getId()) && 
					!ConstantMessages.CATEGORY_PROPEL.equals(category.getId()) &&
					qds.isSuppDataElement()){
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
		for(Qdsel q : qdselList){
			if(q.getOid().equalsIgnoreCase(oid))
				return q;
		}
		
		Qdsel referenceQdsel = new Qdsel();
		createQdsel(referenceQdsel,qds,userOrgOid);
		referenceQdsel.setIdAttr(iprefix+qds.getOid());
		qdselList.add(referenceQdsel);
		
		return referenceQdsel;
	}
	
	public List<QualityDataSet> getOnlySupplimentalQDMS(List<QualityDataSet> qdsList){
		List<QualityDataSet> onlySupplimentalQDMS = new ArrayList<QualityDataSet>();
		for(QualityDataSet qds : qdsList) {
			//US 413 and clean up. Support Steward Other
			if(qds.isSuppDataElement()){
				onlySupplimentalQDMS.add(qds);
			}
		}
		return onlySupplimentalQDMS;
	}
	
}
