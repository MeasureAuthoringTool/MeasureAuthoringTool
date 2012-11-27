package org.ifmc.mat.client.measure;

import java.util.List;

import org.ifmc.mat.model.Author;
import org.ifmc.mat.model.MeasureType;

import com.google.gwt.user.client.rpc.IsSerializable;

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
	
		
}
