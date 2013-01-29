package mat.client.measure;

import java.util.ArrayList;
import java.util.List;

import mat.model.Author;
import mat.model.MeasureType;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.emory.mathcs.backport.java.util.Collections;

public class ManageMeasureDetailModel implements IsSerializable{
	private String id;
	private String name;
	private String shortName;
	private String versionNumber = "0.0";
	private String measureId;
	private String groupName;
	private String groupId;
	private String finalizedDate;
	private String measFromPeriod;
	private String measToPeriod;
	private String measScoring;
	private String measSteward;

	//US 413. Support Steward Other
	private String measStewardOther;
	
	private Boolean endorseByNQF;
	private String measureStatus;
	private String nqfId;
	private String description;
	private String copyright;
	private String clinicalRecomms;
	private String definitions;
	private String guidance;
	private String transmissionFormat;
	private String rationale;
	private String improvNotations;
	private String stratification;
	private List<String> referencesList;
	private List<Author> authorList;
	private List<MeasureType> measureTypeList;
	private List<Author> toCompareAuthor;
	private List<MeasureType> toCompareMeasure;
	private boolean draft = true;
	private String measureSetId;
	private String valueSetDate;
	private String supplementalData;
	private String disclaimer;
	private String riskAdjustment;
	private String rateAggregation;
	private String initialPatientPop;
	private String denominator;
	private String denominatorExclusions;
	private String numerator;
	private String numeratorExclusions;
	private String denominatorExceptions;
	private String measurePopulation;
	private String measureObservations;
	private int eMeasureId;
	
	
	public boolean isDraft() {
		return draft;
	}
	public void setDraft(boolean draft) {
		this.draft = draft;
	}
	public List<String> getReferencesList() {
		return referencesList;
	}
	public void setReferencesList(List<String> referencesList) {
		this.referencesList = referencesList;
	}
		
	public List<Author> getAuthorList() {
		return authorList;
	}
	public void setAuthorList(List<Author> authorList) {
		this.authorList = authorList;
	}
	public List<MeasureType> getMeasureTypeList() {
		return measureTypeList;
	}
	public void setMeasureTypeList(List<MeasureType> measureTypeList) {
		this.measureTypeList = measureTypeList;
	}
	public String getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = doTrim(versionNumber);
	}
	public String getMeasureId() {
		return measureId;
	}
	public void setMeasureId(String measureId) {
		this.measureId = doTrim(measureId);
	}
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = doTrim(groupName);
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = doTrim(groupId);
	}
	
	public String getFinalizedDate() {
		return finalizedDate;
	}
	public void setFinalizedDate(String finalizedDate) {
		this.finalizedDate = finalizedDate;
	}
	public String getMeasFromPeriod() {
		return measFromPeriod;
	}
	public void setMeasFromPeriod(String measFromPeriod) {
		this.measFromPeriod = doTrim(measFromPeriod);
	}
	public String getMeasToPeriod() {
		return measToPeriod;
	}
	public void setMeasToPeriod(String measToPeriod) {
		this.measToPeriod = doTrim(measToPeriod);
	}
	public String getMeasScoring() {
		return measScoring;
	}
	public void setMeasScoring(String measScoring) {
		this.measScoring = doTrim(measScoring);
	}
	
	public String getMeasSteward() {
		return measSteward;
	}
	public void setMeasSteward(String measSteward) {
		this.measSteward = doTrim(measSteward);
	}
	public Boolean getEndorseByNQF() {
		return endorseByNQF;
	}
	public void setEndorseByNQF(Boolean endorseByNQF) {
		this.endorseByNQF = endorseByNQF;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = doTrim(description);
	}
	public String getClinicalRecomms() {
		return clinicalRecomms;
	}
	public void setClinicalRecomms(String clinicalRecomms) {
		this.clinicalRecomms = doTrim(clinicalRecomms);
	}
	public String getDefinitions() {
		return definitions;
	}
	public void setDefinitions(String definitions) {
		this.definitions = doTrim(definitions);
	}
	public String getGuidance() {
		return guidance;
	}
	public void setGuidance(String guidance) {
		this.guidance = doTrim(guidance);
	}
	public String getRationale() {
		return rationale;
	}
	public void setRationale(String rationale) {
		this.rationale = doTrim(rationale);
	}
	public String getImprovNotations() {
		return improvNotations;
	}
	public void setImprovNotations(String improvNotations) {
		this.improvNotations = doTrim(improvNotations);
	}
	public String getStratification() {
		return stratification;
	}
	public void setStratification(String stratification) {
		this.stratification = doTrim(stratification);
	}
	public String getRiskAdjustment() {
		return riskAdjustment;
	}
	public void setRiskAdjustment(String riskAdjustment) {
		this.riskAdjustment = riskAdjustment;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = doTrim(id);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = doTrim(name);
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = doTrim(shortName);
	}
	public String getMeasureStatus() {
		return measureStatus;
	}
	public void setMeasureStatus(String measureStatus) {
		this.measureStatus = doTrim(measureStatus);
	}
	public String getNqfId() {
		return nqfId;
	}
	public void setNqfId(String nqfId) {
		this.nqfId = doTrim(nqfId);
	}
	
	//US 413
	public String getMeasStewardOther() {
		return measStewardOther;
	}
	public void setMeasStewardOther(String measStewardOther) {
		this.measStewardOther = doTrim(measStewardOther);
	}
	
	private String doTrim(String str){
		if(str == null)
			return str;
		else
			return str.trim();
	}
	public String getMeasureSetId() {
		return measureSetId;
	}
	public void setMeasureSetId(String measureSetId) {
		this.measureSetId = measureSetId;
	}
	public String getValueSetDate() {
		return valueSetDate;
	}
	public void setValueSetDate(String valueSetDate) {
		this.valueSetDate = valueSetDate;
	}
	
	public String getCopyright() {
		return copyright;
	}
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}
	public String getTransmissionFormat() {
		return transmissionFormat;
	}
	public void setTransmissionFormat(String transmissionFormat) {
		this.transmissionFormat = transmissionFormat;
	}
	public String getSupplementalData() {
		return supplementalData;
	}
	public void setSupplementalData(String supplementalData) {
		this.supplementalData = supplementalData;
	}
	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}
	public String getDisclaimer() {
		return disclaimer;
	}
	public void setRateAggregation(String rateAggregation) {
		this.rateAggregation = rateAggregation;
	}
	public String getRateAggregation() {
		return rateAggregation;
	}
	public void setInitialPatientPop(String initialPatientPop) {
		this.initialPatientPop = initialPatientPop;
	}
	public String getInitialPatientPop() {
		return initialPatientPop;
	}
	public void setDenominator(String denominator) {
		this.denominator = denominator;
	}
	public String getDenominator() {
		return denominator;
	}
	public void setDenominatorExclusions(String denominatorExclusions) {
		this.denominatorExclusions = denominatorExclusions;
	}
	public String getDenominatorExclusions() {
		return denominatorExclusions;
	}
	public void setNumerator(String numerator) {
		this.numerator = numerator;
	}
	public String getNumerator() {
		return numerator;
	}
	public void setNumeratorExclusions(String numeratorExclusions) {
		this.numeratorExclusions = numeratorExclusions;
	}
	public String getNumeratorExclusions() {
		return numeratorExclusions;
	}
	public void setDenominatorExceptions(String denominatorExceptions) {
		this.denominatorExceptions = denominatorExceptions;
	}
	public String getDenominatorExceptions() {
		return denominatorExceptions;
	}
	public void setMeasurePopulation(String measurePopulation) {
		this.measurePopulation = measurePopulation;
	}
	public String getMeasurePopulation() {
		return measurePopulation;
	}
	public void setMeasureObservations(String measureObservations) {
		this.measureObservations = measureObservations;
	}
	public String getMeasureObservations() {
		return measureObservations;
	}
	public int geteMeasureId() {
		return eMeasureId;
	}
	public void seteMeasureId(int eMeasureId) {
		this.eMeasureId = eMeasureId;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((toCompareAuthor == null) ? 0 : toCompareAuthor.hashCode());
		result = prime * result
				+ ((clinicalRecomms == null) ? 0 : clinicalRecomms.hashCode());
		result = prime * result
				+ ((copyright == null) ? 0 : copyright.hashCode());
		result = prime * result
				+ ((definitions == null) ? 0 : definitions.hashCode());
		result = prime * result
				+ ((denominator == null) ? 0 : denominator.hashCode());
		result = prime
				* result
				+ ((denominatorExceptions == null) ? 0 : denominatorExceptions
						.hashCode());
		result = prime
				* result
				+ ((denominatorExclusions == null) ? 0 : denominatorExclusions
						.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((disclaimer == null) ? 0 : disclaimer.hashCode());
		result = prime * result + (draft ? 1231 : 1237);
		result = prime * result + eMeasureId;
		result = prime * result
				+ ((endorseByNQF == null) ? 0 : endorseByNQF.hashCode());
		result = prime * result
				+ ((finalizedDate == null) ? 0 : finalizedDate.hashCode());
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result
				+ ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result
				+ ((guidance == null) ? 0 : guidance.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((improvNotations == null) ? 0 : improvNotations.hashCode());
		result = prime
				* result
				+ ((initialPatientPop == null) ? 0 : initialPatientPop
						.hashCode());
		result = prime * result
				+ ((measFromPeriod == null) ? 0 : measFromPeriod.hashCode());
		result = prime * result
				+ ((measScoring == null) ? 0 : measScoring.hashCode());
		result = prime * result
				+ ((measSteward == null) ? 0 : measSteward.hashCode());
		result = prime
				* result
				+ ((measStewardOther == null) ? 0 : measStewardOther.hashCode());
		result = prime * result
				+ ((measToPeriod == null) ? 0 : measToPeriod.hashCode());
		result = prime * result
				+ ((measureId == null) ? 0 : measureId.hashCode());
		result = prime
				* result
				+ ((measureObservations == null) ? 0 : measureObservations
						.hashCode());
		result = prime
				* result
				+ ((measurePopulation == null) ? 0 : measurePopulation
						.hashCode());
		result = prime * result
				+ ((measureSetId == null) ? 0 : measureSetId.hashCode());
		result = prime * result
				+ ((measureStatus == null) ? 0 : measureStatus.hashCode());
		result = prime * result
				+ ((toCompareMeasure == null) ? 0 : toCompareMeasure.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nqfId == null) ? 0 : nqfId.hashCode());
		result = prime * result
				+ ((numerator == null) ? 0 : numerator.hashCode());
		result = prime
				* result
				+ ((numeratorExclusions == null) ? 0 : numeratorExclusions
						.hashCode());
		result = prime * result
				+ ((rateAggregation == null) ? 0 : rateAggregation.hashCode());
		result = prime * result
				+ ((rationale == null) ? 0 : rationale.hashCode());
		result = prime * result
				+ ((referencesList == null) ? 0 : referencesList.hashCode());
		result = prime * result
				+ ((riskAdjustment == null) ? 0 : riskAdjustment.hashCode());
		result = prime * result
				+ ((shortName == null) ? 0 : shortName.hashCode());
		result = prime * result
				+ ((stratification == null) ? 0 : stratification.hashCode());
		result = prime
				* result
				+ ((supplementalData == null) ? 0 : supplementalData.hashCode());
		result = prime
				* result
				+ ((transmissionFormat == null) ? 0 : transmissionFormat
						.hashCode());
		result = prime * result
				+ ((valueSetDate == null) ? 0 : valueSetDate.hashCode());
		result = prime * result
				+ ((versionNumber == null) ? 0 : versionNumber.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ManageMeasureDetailModel other = (ManageMeasureDetailModel) obj;
		if (toCompareAuthor == null) {
			if (other.toCompareAuthor != null)
				return false;
		} else if (!isEqual(toCompareAuthor, other.toCompareAuthor))//(!authorList.equals(other.authorList))
			return false;
		/*if (authorList == null) {
			if (other.authorList != null)
				return false;
		} else if (!isEqual(authorList, other.authorList))//(!authorList.equals(other.authorList))
			return false;*/
		if (trimToNull(clinicalRecomms) == null) {
			if (trimToNull(other.clinicalRecomms) != null)
				return false;
		} else if (!trimToNull(clinicalRecomms).equals(trimToNull(other.clinicalRecomms)))
			return false;
		if (trimToNull(copyright) == null) {
			if (trimToNull(other.copyright) != null)
				return false;
		} else if (!trimToNull(copyright).equals(trimToNull(other.copyright)))
			return false;
		if (trimToNull(definitions) == null) {
			if (trimToNull(other.definitions) != null)
				return false;
		} else if (!trimToNull(definitions).equals(trimToNull(other.definitions)))
			return false;
		if (trimToNull(denominator) == null) {
			if (trimToNull(other.denominator) != null)
				return false;
		} else if (!trimToNull(denominator).equals(trimToNull(other.denominator)))
			return false;
		if (trimToNull(denominatorExceptions) == null) {
			if (trimToNull(other.denominatorExceptions) != null)
				return false;
		} else if (!trimToNull(denominatorExceptions).equals(trimToNull(other.denominatorExceptions)))
			return false;
		if (trimToNull(denominatorExclusions) == null) {
			if (trimToNull(other.denominatorExclusions) != null)
				return false;
		} else if (!trimToNull(denominatorExclusions).equals(trimToNull(other.denominatorExclusions)))
			return false;
		if (trimToNull(description) == null) {
			if (trimToNull(other.description) != null)
				return false;
		} else if (!trimToNull(description).equals(trimToNull(other.description)))
			return false;
		if (trimToNull(disclaimer) == null) {
			if (trimToNull(other.disclaimer) != null)
				return false;
		} else if (!trimToNull(disclaimer).equals(trimToNull(other.disclaimer)))
			return false;
		if (draft != other.draft)
			return false;
		if (eMeasureId != other.eMeasureId)
			return false;
		endorseByNQF = endorseByNQF == null ? false : endorseByNQF.booleanValue();
		other.endorseByNQF = other.endorseByNQF == null ? false : other.endorseByNQF.booleanValue();
		if (!endorseByNQF.equals(other.endorseByNQF))
			return false;
		if (trimToNull(finalizedDate) == null) {
			if (trimToNull(other.finalizedDate) != null)
				return false;
		} else if (!trimToNull(finalizedDate).equals(trimToNull(other.finalizedDate)))
			return false;
		if (trimToNull(groupId) == null) {
			if (trimToNull(other.groupId) != null)
				return false;
		} else if (!trimToNull(groupId).equals(trimToNull(other.groupId)))
			return false;
		if (trimToNull(groupName) == null) {
			if (trimToNull(other.groupName) != null)
				return false;
		} else if (!trimToNull(groupName).equals(trimToNull(other.groupName)))
			return false;
		if (trimToNull(guidance) == null) {
			if (trimToNull(other.guidance) != null)
				return false;
		} else if (!trimToNull(guidance).equals(trimToNull(other.guidance)))
			return false;
/*		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;*/
		if (trimToNull(improvNotations) == null) {
			if (trimToNull(other.improvNotations) != null)
				return false;
		} else if (!trimToNull(improvNotations).equals(trimToNull(other.improvNotations)))
			return false;
		if (trimToNull(initialPatientPop) == null) {
			if (trimToNull(other.initialPatientPop) != null)
				return false;
		} else if (!trimToNull(initialPatientPop).equals(trimToNull(other.initialPatientPop)))
			return false;
		if (trimToNull(measFromPeriod) == null) {
			if (trimToNull(other.measFromPeriod) != null)
				return false;
		} else if (!trimToNull(measFromPeriod).equals(trimToNull(other.measFromPeriod)))
			return false;
		if (trimToNull(measSteward) == null) {
			if (trimToNull(other.measSteward) != null)
				return false;
		} else if (!trimToNull(measSteward).equals(trimToNull(other.measSteward)))
			return false;
		/*if (measStewardOther == null) {
			if (other.measStewardOther != null)
				return false;
		} else if (!measStewardOther.equals(other.measStewardOther))
			return false;*/
		if (trimToNull(measToPeriod) == null) {
			if (trimToNull(other.measToPeriod) != null)
				return false;
		} else if (!trimToNull(measToPeriod).equals(trimToNull(other.measToPeriod)))
			return false;
		if (trimToNull(measureId) == null) {
			if (trimToNull(other.measureId) != null)
				return false;
		} else if (!trimToNull(measureId).equals(trimToNull(other.measureId)))
			return false;
		if (trimToNull(measureObservations) == null) {
			if (trimToNull(other.measureObservations) != null)
				return false;
		} else if (!trimToNull(measureObservations).equals(trimToNull(other.measureObservations)))
			return false;
		if (trimToNull(measurePopulation) == null) {
			if (trimToNull(other.measurePopulation) != null)
				return false;
		} else if (!trimToNull(measurePopulation).equals(trimToNull(other.measurePopulation)))
			return false;
		if (trimToNull(measureSetId) == null) {
			if (trimToNull(other.measureSetId) != null)
				return false;
		} else if (!trimToNull(measureSetId).equals(trimToNull(other.measureSetId)))
			return false;
		if (trimToNull(measureStatus) == null) {
			if (trimToNull(other.measureStatus) != null)
				return false;
		} else if (!trimToNull(measureStatus).equals(trimToNull(other.measureStatus)))
			return false;
		if (toCompareMeasure == null) {
			if (other.toCompareMeasure != null)
				return false;
		} else if (!isEqual(toCompareMeasure, other.toCompareMeasure))
			return false;
		if (trimToNull(name) == null) {
			if (trimToNull(other.name) != null)
				return false;
		} else if (!trimToNull(name).equals(trimToNull(other.name)))
			return false;
		if (trimToNull(nqfId) == null) {
			if (trimToNull(other.nqfId) != null)
				return false;
		} else if (!trimToNull(nqfId).equals(trimToNull(other.nqfId)))
			return false;
		if (trimToNull(numerator) == null) {
			if (trimToNull(other.numerator) != null)
				return false;
		} else if (!trimToNull(numerator).equals(trimToNull(other.numerator)))
			return false;
		if (trimToNull(numeratorExclusions) == null) {
			if (trimToNull(other.numeratorExclusions) != null)
				return false;
		} else if (!trimToNull(numeratorExclusions).equals(trimToNull(other.numeratorExclusions)))
			return false;
		if (trimToNull(rateAggregation) == null) {
			if (trimToNull(other.rateAggregation) != null)
				return false;
		} else if (!trimToNull(rateAggregation).equals(trimToNull(other.rateAggregation)))
			return false;
		if (trimToNull(rationale) == null) {
			if (trimToNull(other.rationale) != null)
				return false;
		} else if (!trimToNull(rationale).equals(trimToNull(other.rationale)))
			return false;
		if (referencesList == null) {
			if (other.referencesList != null)
				return false;
		} else if (!isEqual(getTrimmedList(referencesList),getTrimmedList(other.referencesList)))
			return false;
		if (trimToNull(riskAdjustment) == null) {
			if (trimToNull(other.riskAdjustment) != null)
				return false;
		} else if (!trimToNull(riskAdjustment).equals(trimToNull(other.riskAdjustment)))
			return false;
		if (trimToNull(shortName) == null) {
			if (trimToNull(other.shortName) != null)
				return false;
		} else if (!trimToNull(shortName).equals(trimToNull(other.shortName)))
			return false;
		if (trimToNull(stratification) == null) {
			if (trimToNull(other.stratification) != null)
				return false;
		} else if (!trimToNull(stratification).equals(trimToNull(other.stratification)))
			return false;
		if (trimToNull(supplementalData) == null) {
			if (trimToNull(other.supplementalData) != null)
				return false;
		} else if (!trimToNull(supplementalData).equals(trimToNull(other.supplementalData)))
			return false;
		if (trimToNull(transmissionFormat) == null) {
			if (trimToNull(other.transmissionFormat) != null)
				return false;
		} else if (!trimToNull(transmissionFormat).equals(trimToNull(other.transmissionFormat)))
			return false;
		return true;
	}

	
	
	public boolean isEqual(List listA, List listB) {
	    if (listA.size() != listB.size()) return false;
	    for (int i=0; i<listA.size(); i++) {
	    	if(listA.get(i) instanceof Author){
	    		Author author = (Author) listA.get(i);
	    		Author otherAuthor = (Author) listB.get(i);
	    		if(author.compare(author, otherAuthor) != 0){
	    			return false;
	    		}
	    	}else if(listA.get(i) instanceof MeasureType){
	    		MeasureType measureType = (MeasureType) listA.get(i);
	    		MeasureType otherMeasureType = (MeasureType) listB.get(i);
	    		if(measureType.compare(measureType, otherMeasureType) != 0){
	    			return false;
	    		}
	    	}else if(listA.get(i) instanceof String){
	    		String val1 = (String)listA.get(i);
	    		String val2 = (String)listB.get(i);
	    		if(val1.compareTo(val2) != 0){
	    			return false;
	    		}
	    	}
	    }
	    return true;
	}
	
	private List<String> getTrimmedList(List<String> listA){
		ArrayList<String> newAList = new ArrayList<String>();
		for (String aStr : listA) {
			if(null != trimToNull(aStr)){
				newAList.add(aStr);
			}
		}
		return newAList;
	}
	
	private String trimToNull(String value){
		if(null != value){
			value = value.replaceAll("[\r\n]", "");
			value = value.equals("") ? null : value.trim();
			
		}
		return value;
	}
	/**
	 * @return the toCompareAuthor
	 */
	public List<Author> getToCompareAuthor() {
		return toCompareAuthor;
	}
	/**
	 * @param toCompareAuthor the toCompareAuthor to set
	 */
	public void setToCompareAuthor(List<Author> toCompareAuthor) {
		this.toCompareAuthor = toCompareAuthor;
	}
	/**
	 * @return the toCompareMeasure
	 */
	public List<MeasureType> getToCompareMeasure() {
		return toCompareMeasure;
	}
	/**
	 * @param toCompareMeasure the toCompareMeasure to set
	 */
	public void setToCompareMeasure(List<MeasureType> toCompareMeasure) {
		this.toCompareMeasure = toCompareMeasure;
	}
	
	
}
