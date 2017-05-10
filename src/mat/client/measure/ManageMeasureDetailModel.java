package mat.client.measure;

import java.util.List;

import mat.client.clause.clauseworkspace.model.MeasureDetailResult;
import mat.model.Author;
import mat.model.BaseModel;
import mat.model.MeasureSteward;
import mat.model.MeasureType;
import mat.model.QualityDataSetDTO;
import mat.shared.model.util.MeasureDetailsUtil;

import com.google.gwt.user.client.rpc.IsSerializable;

// TODO: Auto-generated Javadoc
/**
 * The Class ManageMeasureDetailModel.
 */
public class ManageMeasureDetailModel implements IsSerializable , BaseModel{
	
	/** The id. */
	private String id;
	
	/** The name. */
	private String name;
	
	/** The short name. */
	private String shortName;
	
	/** The version number. */
	private String versionNumber = "0.0";
	/** The revision number. */
	private String revisionNumber = "000";
	
	/** The measure id. */
	private String measureId;
	
	/** The group name. */
	private String groupName;
	
	/** The group id. */
	private String groupId;
	
	/** The finalized date. */
	private String finalizedDate;
	
	/** The meas from period. */
	private String measFromPeriod;
	
	/** The meas to period. */
	private String measToPeriod;
	
	/** The meas scoring. */
	private String measScoring;
	
	/** The meas steward. */
	//private String measSteward;
	private String stewardValue;
	
	//US 413. Support Steward Other
	/** The meas steward other. */
	//private String measStewardOther;
	
	/** The endorse by nqf. */
	private Boolean endorseByNQF;
	
	/** The measure status. */
	/*private String measureStatus;*/
	
	/** The nqf id. */
	private String nqfId;
	
	/** The description. */
	private String description;
	
	/** The copyright. */
	private String copyright;
	
	/** The clinical recomms. */
	private String clinicalRecomms;
	
	/** The definitions. */
	private String definitions;
	
	/** The guidance. */
	private String guidance;
	
	/** The transmission format. */
	private String transmissionFormat;
	
	/** The rationale. */
	private String rationale;
	
	/** The improv notations. */
	private String improvNotations;
	
	/** The stratification. */
	private String stratification;
	
	/** The references list. */
	private List<String> referencesList;
	
	/** The author list. */
	private List<Author> authorSelectedList;
	
	/** The steward selected list. */
	private List<MeasureSteward> stewardSelectedList;
	
	/** The measure type list. */
	private List<MeasureType> measureTypeSelectedList;
	
	/** The qds selected list. */
	private List<QualityDataSetDTO> qdsSelectedList;
	
	/** The component measures selected list. */
	private List<ManageMeasureSearchModel.Result> componentMeasuresSelectedList;
	
	/** The to compare author. */
	private List<Author> toCompareAuthor;
	
	/** The to compare measure. */
	private List<MeasureType> toCompareMeasure;
		
	/** The to compare component measures. */
	private List<ManageMeasureSearchModel.Result> toCompareComponentMeasures;
	
	/** The draft. */
	private boolean draft = true;
	
	/** The measure set id. */
	private String measureSetId;
	
	/** The value set date. */
	private String valueSetDate;
	
	/** The supplemental data. */
	private String supplementalData;
	
	/** The disclaimer. */
	private String disclaimer;
	
	/** The risk adjustment. */
	private String riskAdjustment;
	
	/** The rate aggregation. */
	private String rateAggregation;
	
	/** The initial patient pop. */
	private String initialPop;
	
	/** The denominator. */
	private String denominator;
	
	/** The denominator exclusions. */
	private String denominatorExclusions;
	
	/** The numerator. */
	private String numerator;
	
	/** The numerator exclusions. */
	private String numeratorExclusions;
	
	/** The denominator exceptions. */
	private String denominatorExceptions;
	
	/** The measure population. */
	private String measurePopulation;
	
	/** The measure observations. */
	private String measureObservations;
	
	/** The e measure id. */
	private int eMeasureId;
	
	/** The org version number. */
	private String orgVersionNumber;
	
	//Below fields are added for Castor mapping XML generation purpose
	/** The qlty measure set uuid. */
	private String qltyMeasureSetUuid;
	
	/** The steward uuid. */
	private String stewardId;
	
	/** The steward value. */
	//private String stewardValue;
	
	/** The scoring abbr. */
	private String scoringAbbr;
	
	/** The period model. */
	private PeriodModel periodModel;
	
	/** The endorsement. */
	private String endorsement;
	
	/** The endorsement id. */
	private String endorsementId;
	
	/** The nqf model. */
	private NqfModel nqfModel;
	
	/** The is deleted. */
	private boolean isDeleted;
	
	/** The measure owner id. */
	private String measureOwnerId;
	
	/** The measure population exclusions. */
	private String measurePopulationExclusions;
	
	/** The is editable. */
	private boolean isEditable;
	
	/** The calender year. */
	private boolean isCalenderYear;
	
	/** The is patient based flag **/
	private boolean isPatientBased; 
	
	private MeasureDetailResult measureDetailResult; 
	
	
	/**
	 * Checks if is calender year.
	 *
	 * @return true, if is calender year
	 */
	public boolean isCalenderYear() {
		return isCalenderYear;
	}
	
	/**
	 * Sets the calender year.
	 *
	 * @param calenderYear the new calender year
	 */
	public void setCalenderYear(boolean isCalenderYear) {
		this.isCalenderYear = isCalenderYear;
	}
	
	/**
	 * Checks if is deleted.
	 * 
	 * @return true, if is deleted
	 */
	public boolean isDeleted() {
		return isDeleted;
	}
	
	/**
	 * Sets the deleted.
	 * 
	 * @param isDeleted
	 *            the new deleted
	 */
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	/**
	 * Checks if is draft.
	 * 
	 * @return true, if is draft
	 */
	public boolean isDraft() {
		return draft;
	}
	
	/**
	 * Sets the draft.
	 * 
	 * @param draft
	 *            the new draft
	 */
	public void setDraft(boolean draft) {
		this.draft = draft;
	}
	
	/**
	 * Gets the references list.
	 * 
	 * @return the references list
	 */
	public List<String> getReferencesList() {
		return referencesList;
	}
	
	/**
	 * Sets the references list.
	 * 
	 * @param referencesList
	 *            the new references list
	 */
	public void setReferencesList(List<String> referencesList) {
		this.referencesList = referencesList;
	}
	
	/**
	 * Gets the author list.
	 * 
	 * @return the author list
	 */
	public List<Author> getAuthorSelectedList() {
		return authorSelectedList;
	}
	
	/**
	 * Sets the author list.
	 *
	 * @param authorSelectedList the new author selected list
	 */
	public void setAuthorSelectedList(List<Author> authorSelectedList) {
		this.authorSelectedList = authorSelectedList;
	}
	
	/**
	 * Gets the measure type list.
	 * 
	 * @return the measure type list
	 */
	public List<MeasureType> getMeasureTypeSelectedList() {
		return measureTypeSelectedList;
	}
	
	/**
	 * Sets the measure type list.
	 *
	 * @param measureTypeSelectedList the new measure type selected list
	 */
	public void setMeasureTypeSelectedList(List<MeasureType> measureTypeSelectedList) {
		this.measureTypeSelectedList = measureTypeSelectedList;
	}
	
	/**
	 * Gets the initial pop.
	 *
	 * @return the initial pop
	 */
	public String getInitialPop() {
		return initialPop;
	}
	
	/**
	 * Sets the initial pop.
	 *
	 * @param initialPop the new initial pop
	 */
	public void setInitialPop(String initialPop) {
		this.initialPop = initialPop;
	}
	
	/**
	 * Gets the version number.
	 * 
	 * @return the version number
	 */
	public String getVersionNumber() {
		return versionNumber;
	}
	
	/**
	 * Sets the version number.
	 * 
	 * @param versionNumber
	 *            the new version number
	 */
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = doTrim(versionNumber);
	}
	
	/**
	 * Gets the measure id.
	 * 
	 * @return the measure id
	 */
	public String getMeasureId() {
		return measureId;
	}
	
	/**
	 * Sets the measure id.
	 * 
	 * @param measureId
	 *            the new measure id
	 */
	public void setMeasureId(String measureId) {
		this.measureId = doTrim(measureId);
	}
	
	/**
	 * Gets the group name.
	 * 
	 * @return the group name
	 */
	public String getGroupName() {
		return groupName;
	}
	
	/**
	 * Sets the group name.
	 * 
	 * @param groupName
	 *            the new group name
	 */
	public void setGroupName(String groupName) {
		this.groupName = doTrim(groupName);
	}
	
	/**
	 * Gets the group id.
	 * 
	 * @return the group id
	 */
	public String getGroupId() {
		return groupId;
	}
	
	/**
	 * Sets the group id.
	 * 
	 * @param groupId
	 *            the new group id
	 */
	public void setGroupId(String groupId) {
		this.groupId = doTrim(groupId);
	}
	
	/**
	 * Gets the finalized date.
	 * 
	 * @return the finalized date
	 */
	public String getFinalizedDate() {
		return finalizedDate;
	}
	
	/**
	 * Sets the finalized date.
	 * 
	 * @param finalizedDate
	 *            the new finalized date
	 */
	public void setFinalizedDate(String finalizedDate) {
		this.finalizedDate = doTrim(finalizedDate);
	}
	
	/**
	 * Gets the meas from period.
	 * 
	 * @return the meas from period
	 */
	public String getMeasFromPeriod() {
		return measFromPeriod;
	}
	
	/**
	 * Sets the meas from period.
	 * 
	 * @param measFromPeriod
	 *            the new meas from period
	 */
	public void setMeasFromPeriod(String measFromPeriod) {
		this.measFromPeriod = doTrim(measFromPeriod);
	}
	
	/**
	 * Gets the meas to period.
	 * 
	 * @return the meas to period
	 */
	public String getMeasToPeriod() {
		return measToPeriod;
	}
	
	/**
	 * Sets the meas to period.
	 * 
	 * @param measToPeriod
	 *            the new meas to period
	 */
	public void setMeasToPeriod(String measToPeriod) {
		this.measToPeriod = doTrim(measToPeriod);
	}
	
	/**
	 * Gets the meas scoring.
	 * 
	 * @return the meas scoring
	 */
	public String getMeasScoring() {
		return measScoring;
	}
	
	/**
	 * Sets the meas scoring.
	 * 
	 * @param measScoring
	 *            the new meas scoring
	 */
	public void setMeasScoring(String measScoring) {
		this.measScoring = doTrim(measScoring);
	}
	
	/**
	 * Gets the meas steward.
	 * 
	 * @return the meas steward
	 */
	/*public String getMeasSteward() {
		return measSteward;
	}
	 */
	/**
	 * Sets the meas steward.
	 * 
	 * @param measSteward
	 *            the new meas steward
	 */
	/*public void setMeasSteward(String measSteward) {
		this.measSteward = doTrim(measSteward);
	}*/
	
	/**
	 * Gets the endorse by nqf.
	 * 
	 * @return the endorse by nqf
	 */
	public Boolean getEndorseByNQF() {
		return endorseByNQF;
	}
	
	/**
	 * Sets the endorse by nqf.
	 * 
	 * @param endorseByNQF
	 *            the new endorse by nqf
	 */
	public void setEndorseByNQF(Boolean endorseByNQF) {
		this.endorseByNQF = endorseByNQF;
	}
	
	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the description.
	 * 
	 * @param description
	 *            the new description
	 */
	public void setDescription(String description) {
		this.description = doTrim(description);
	}
	
	/**
	 * Gets the clinical recomms.
	 * 
	 * @return the clinical recomms
	 */
	public String getClinicalRecomms() {
		return clinicalRecomms;
	}
	
	/**
	 * Sets the clinical recomms.
	 * 
	 * @param clinicalRecomms
	 *            the new clinical recomms
	 */
	public void setClinicalRecomms(String clinicalRecomms) {
		this.clinicalRecomms = doTrim(clinicalRecomms);
	}
	
	/**
	 * Gets the definitions.
	 * 
	 * @return the definitions
	 */
	public String getDefinitions() {
		return definitions;
	}
	
	/**
	 * Sets the definitions.
	 * 
	 * @param definitions
	 *            the new definitions
	 */
	public void setDefinitions(String definitions) {
		this.definitions = doTrim(definitions);
	}
	
	/**
	 * Gets the guidance.
	 * 
	 * @return the guidance
	 */
	public String getGuidance() {
		return guidance;
	}
	
	/**
	 * Sets the guidance.
	 * 
	 * @param guidance
	 *            the new guidance
	 */
	public void setGuidance(String guidance) {
		this.guidance = doTrim(guidance);
	}
	
	/**
	 * Gets the rationale.
	 * 
	 * @return the rationale
	 */
	public String getRationale() {
		return rationale;
	}
	
	/**
	 * Sets the rationale.
	 * 
	 * @param rationale
	 *            the new rationale
	 */
	public void setRationale(String rationale) {
		this.rationale = doTrim(rationale);
	}
	
	/**
	 * Gets the improv notations.
	 * 
	 * @return the improv notations
	 */
	public String getImprovNotations() {
		return improvNotations;
	}
	
	/**
	 * Sets the improv notations.
	 * 
	 * @param improvNotations
	 *            the new improv notations
	 */
	public void setImprovNotations(String improvNotations) {
		this.improvNotations = doTrim(improvNotations);
	}
	
	/**
	 * Gets the stratification.
	 * 
	 * @return the stratification
	 */
	public String getStratification() {
		return stratification;
	}
	
	/**
	 * Sets the stratification.
	 * 
	 * @param stratification
	 *            the new stratification
	 */
	public void setStratification(String stratification) {
		this.stratification = doTrim(stratification);
	}
	
	/**
	 * Gets the risk adjustment.
	 * 
	 * @return the risk adjustment
	 */
	public String getRiskAdjustment() {
		return riskAdjustment;
	}
	
	/**
	 * Sets the risk adjustment.
	 * 
	 * @param riskAdjustment
	 *            the new risk adjustment
	 */
	public void setRiskAdjustment(String riskAdjustment) {
		this.riskAdjustment = doTrim(riskAdjustment);
	}
	
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {
		this.id = doTrim(id);
	}
	
	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = doTrim(name);
	}
	
	/**
	 * Gets the short name.
	 * 
	 * @return the short name
	 */
	public String getShortName() {
		return shortName;
	}
	
	/**
	 * Sets the short name.
	 * 
	 * @param shortName
	 *            the new short name
	 */
	public void setShortName(String shortName) {
		this.shortName = doTrim(shortName);
	}
	
	/**
	 * Gets the measure status.
	 * 
	 * @return the measure status
	 */
	/*public String getMeasureStatus() {
		return measureStatus;
	}
	
	 *//**
	 * Sets the measure status.
	 * 
	 * @param measureStatus
	 *            the new measure status
	 *//*
	public void setMeasureStatus(String measureStatus) {
		this.measureStatus = doTrim(measureStatus);
	}*/
	
	/**
	 * Gets the nqf id.
	 * 
	 * @return the nqf id
	 */
	public String getNqfId() {
		return nqfId;
	}

	/**
	 * Sets the nqf id.
	 * 
	 * @param nqfId
	 *            the new nqf id
	 */
	public void setNqfId(String nqfId) {
		this.nqfId = doTrim(nqfId);
	}
	
	//US 413
	/**
	 * Gets the meas steward other.
	 *
	 * @param str the str
	 * @return the meas steward other
	 */
	/*public String getMeasStewardOther() {
		return measStewardOther;
	}*/
	
	/**
	 * Sets the meas steward other.
	 * 
	 * @param measStewardOther
	 *            the new meas steward other
	 */
	/*public void setMeasStewardOther(String measStewardOther) {
		this.measStewardOther = doTrim(measStewardOther);
	}*/
	
	/**
	 * Do trim.
	 * 
	 * @param str
	 *            the str
	 * @return the string
	 */
	private String doTrim(String str) {
		return (str != null) && (str.trim().length() > 0) ? str.trim() : null;
	}
	
	/**
	 * Gets the measure set id.
	 * 
	 * @return the measure set id
	 */
	public String getMeasureSetId() {
		return measureSetId;
	}
	
	/**
	 * Sets the measure set id.
	 * 
	 * @param measureSetId
	 *            the new measure set id
	 */
	public void setMeasureSetId(String measureSetId) {
		this.measureSetId = measureSetId;
	}
	
	/**
	 * Gets the value set date.
	 * 
	 * @return the value set date
	 */
	public String getValueSetDate() {
		return valueSetDate;
	}
	
	/**
	 * Sets the value set date.
	 * 
	 * @param valueSetDate
	 *            the new value set date
	 */
	public void setValueSetDate(String valueSetDate) {
		this.valueSetDate = valueSetDate;
	}
	
	/**
	 * Gets the copyright.
	 * 
	 * @return the copyright
	 */
	public String getCopyright() {
		return copyright;
	}
	
	/**
	 * Sets the copyright.
	 * 
	 * @param copyright
	 *            the new copyright
	 */
	public void setCopyright(String copyright) {
		this.copyright = doTrim(copyright);
	}
	
	/**
	 * Gets the transmission format.
	 * 
	 * @return the transmission format
	 */
	public String getTransmissionFormat() {
		return transmissionFormat;
	}
	
	/**
	 * Sets the transmission format.
	 * 
	 * @param transmissionFormat
	 *            the new transmission format
	 */
	public void setTransmissionFormat(String transmissionFormat) {
		this.transmissionFormat = doTrim(transmissionFormat);
	}
	
	/**
	 * Gets the supplemental data.
	 * 
	 * @return the supplemental data
	 */
	public String getSupplementalData() {
		return supplementalData;
	}
	
	/**
	 * Sets the supplemental data.
	 * 
	 * @param supplementalData
	 *            the new supplemental data
	 */
	public void setSupplementalData(String supplementalData) {
		this.supplementalData = doTrim(supplementalData);
	}
	
	/**
	 * Sets the disclaimer.
	 * 
	 * @param disclaimer
	 *            the new disclaimer
	 */
	public void setDisclaimer(String disclaimer) {
		this.disclaimer = doTrim(disclaimer);
	}
	
	/**
	 * Gets the disclaimer.
	 * 
	 * @return the disclaimer
	 */
	public String getDisclaimer() {
		return disclaimer;
	}
	
	/**
	 * Sets the rate aggregation.
	 * 
	 * @param rateAggregation
	 *            the new rate aggregation
	 */
	public void setRateAggregation(String rateAggregation) {
		this.rateAggregation = doTrim(rateAggregation);
	}
	
	/**
	 * Gets the rate aggregation.
	 * 
	 * @return the rate aggregation
	 */
	public String getRateAggregation() {
		return rateAggregation;
	}
	
	/**
	 * Sets the denominator.
	 * 
	 * @param denominator
	 *            the new denominator
	 */
	public void setDenominator(String denominator) {
		this.denominator = doTrim(denominator);
	}
	
	/**
	 * Gets the denominator.
	 * 
	 * @return the denominator
	 */
	public String getDenominator() {
		return denominator;
	}
	
	/**
	 * Sets the denominator exclusions.
	 * 
	 * @param denominatorExclusions
	 *            the new denominator exclusions
	 */
	public void setDenominatorExclusions(String denominatorExclusions) {
		this.denominatorExclusions = doTrim(denominatorExclusions);
	}
	
	/**
	 * Gets the denominator exclusions.
	 * 
	 * @return the denominator exclusions
	 */
	public String getDenominatorExclusions() {
		return denominatorExclusions;
	}
	
	/**
	 * Sets the numerator.
	 * 
	 * @param numerator
	 *            the new numerator
	 */
	public void setNumerator(String numerator) {
		this.numerator = doTrim(numerator);
	}
	
	/**
	 * Gets the numerator.
	 * 
	 * @return the numerator
	 */
	public String getNumerator() {
		return numerator;
	}
	
	/**
	 * Sets the numerator exclusions.
	 * 
	 * @param numeratorExclusions
	 *            the new numerator exclusions
	 */
	public void setNumeratorExclusions(String numeratorExclusions) {
		this.numeratorExclusions = doTrim(numeratorExclusions);
	}
	
	/**
	 * Gets the numerator exclusions.
	 * 
	 * @return the numerator exclusions
	 */
	public String getNumeratorExclusions() {
		return numeratorExclusions;
	}
	
	/**
	 * Sets the denominator exceptions.
	 * 
	 * @param denominatorExceptions
	 *            the new denominator exceptions
	 */
	public void setDenominatorExceptions(String denominatorExceptions) {
		this.denominatorExceptions = doTrim(denominatorExceptions);
	}
	
	/**
	 * Gets the denominator exceptions.
	 * 
	 * @return the denominator exceptions
	 */
	public String getDenominatorExceptions() {
		return denominatorExceptions;
	}
	
	/**
	 * Sets the measure population.
	 * 
	 * @param measurePopulation
	 *            the new measure population
	 */
	public void setMeasurePopulation(String measurePopulation) {
		this.measurePopulation = doTrim(measurePopulation);
	}
	
	/**
	 * Gets the measure population.
	 * 
	 * @return the measure population
	 */
	public String getMeasurePopulation() {
		return measurePopulation;
	}
	
	/**
	 * Sets the measure observations.
	 * 
	 * @param measureObservations
	 *            the new measure observations
	 */
	public void setMeasureObservations(String measureObservations) {
		this.measureObservations = doTrim(measureObservations);
	}
	
	/**
	 * Gets the measure observations.
	 * 
	 * @return the measure observations
	 */
	public String getMeasureObservations() {
		return measureObservations;
	}
	
	/**
	 * Gets the e measure id.
	 * 
	 * @return the e measure id
	 */
	public int geteMeasureId() {
		return eMeasureId;
	}
	
	/**
	 * Sets the e measure id.
	 * 
	 * @param eMeasureId
	 *            the new e measure id
	 */
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
		result = (prime * result)
				+ ((toCompareAuthor == null) ? 0 : toCompareAuthor.hashCode());
		result = (prime * result)
				+ ((clinicalRecomms == null) ? 0 : clinicalRecomms.hashCode());
		result = (prime * result)
				+ ((copyright == null) ? 0 : copyright.hashCode());
		result = (prime * result)
				+ ((definitions == null) ? 0 : definitions.hashCode());
		result = (prime * result)
				+ ((denominator == null) ? 0 : denominator.hashCode());
		result = (prime
				* result)
				+ ((denominatorExceptions == null) ? 0 : denominatorExceptions
						.hashCode());
		result = (prime
				* result)
				+ ((denominatorExclusions == null) ? 0 : denominatorExclusions
						.hashCode());
		result = (prime * result)
				+ ((description == null) ? 0 : description.hashCode());
		result = (prime * result)
				+ ((disclaimer == null) ? 0 : disclaimer.hashCode());
		result = (prime * result) + (draft ? 1231 : 1237);
		result = (prime * result)
				+ ((endorseByNQF == null) ? 0 : endorseByNQF.hashCode());
		result = (prime * result)
				+ ((finalizedDate == null) ? 0 : finalizedDate.hashCode());
		result = (prime * result) + ((groupId == null) ? 0 : groupId.hashCode());
		result = (prime * result)
				+ ((groupName == null) ? 0 : groupName.hashCode());
		result = (prime * result)
				+ ((guidance == null) ? 0 : guidance.hashCode());
		result = (prime * result) + ((id == null) ? 0 : id.hashCode());
		result = (prime * result)
				+ ((improvNotations == null) ? 0 : improvNotations.hashCode());
		result = (prime
				* result)
				+ ((initialPop == null) ? 0 : initialPop
						.hashCode());
		result = (prime * result)
				+ ((measFromPeriod == null) ? 0 : measFromPeriod.hashCode());
		result = (prime * result)
				+ ((measScoring == null) ? 0 : measScoring.hashCode());
		result = (prime * result)
				+ ((stewardValue == null) ? 0 : stewardValue.hashCode());
		/*result = (prime
		 * result)
				+ ((measStewardOther == null) ? 0 : measStewardOther.hashCode());*/
		result = (prime * result)
				+ ((measToPeriod == null) ? 0 : measToPeriod.hashCode());
		result = (prime * result)
				+ ((measureId == null) ? 0 : measureId.hashCode());
		result = (prime
				* result)
				+ ((measureObservations == null) ? 0 : measureObservations
						.hashCode());
		result = (prime
				* result)
				+ ((measurePopulation == null) ? 0 : measurePopulation
						.hashCode());
		result = (prime * result)
				+ ((measureSetId == null) ? 0 : measureSetId.hashCode());
		/*result = (prime * result)
				+ ((measureStatus == null) ? 0 : measureStatus.hashCode());*/
		result = (prime * result)
				+ ((toCompareMeasure == null) ? 0 : toCompareMeasure.hashCode());
		result = (prime * result)
				+ ((toCompareComponentMeasures == null) ? 0 : toCompareComponentMeasures.hashCode());
		result = (prime * result) + ((name == null) ? 0 : name.hashCode());
		result = (prime * result) + ((nqfId == null) ? 0 : nqfId.hashCode());
		result = (prime * result)
				+ ((numerator == null) ? 0 : numerator.hashCode());
		result = (prime
				* result)
				+ ((numeratorExclusions == null) ? 0 : numeratorExclusions
						.hashCode());
		result = (prime * result)
				+ ((rateAggregation == null) ? 0 : rateAggregation.hashCode());
		result = (prime * result)
				+ ((rationale == null) ? 0 : rationale.hashCode());
		result = (prime * result)
				+ ((referencesList == null) ? 0 : referencesList.hashCode());
		result = (prime * result)
				+ ((riskAdjustment == null) ? 0 : riskAdjustment.hashCode());
		result = (prime * result)
				+ ((shortName == null) ? 0 : shortName.hashCode());
		result = (prime * result)
				+ ((stratification == null) ? 0 : stratification.hashCode());
		result = (prime
				* result)
				+ ((supplementalData == null) ? 0 : supplementalData.hashCode());
		result = (prime
				* result)
				+ ((transmissionFormat == null) ? 0 : transmissionFormat
						.hashCode());
		result = (prime * result)
				+ ((valueSetDate == null) ? 0 : valueSetDate.hashCode());
		result = (prime * result)
				+ ((versionNumber == null) ? 0 : versionNumber.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
				
		if (getClass() != obj.getClass()) {
			return false;
		}
		
	
		ManageMeasureDetailModel other = (ManageMeasureDetailModel) obj;
		if (toCompareAuthor == null) {
			if (other.toCompareAuthor != null) {
				return false;
			}
		} else if (!isEqual(toCompareAuthor, other.toCompareAuthor)) {
			return false;
		}

		if (trimToNull(clinicalRecomms) == null) {
			if (trimToNull(other.clinicalRecomms) != null) {
				return false;
			}
		} else if (!trimToNull(clinicalRecomms).equals(trimToNull(other.clinicalRecomms))) {
			return false;
		}
		if (trimToNull(copyright) == null) {
			if (trimToNull(other.copyright) != null) {
				return false;
			}
		} else if (!trimToNull(copyright).equals(trimToNull(other.copyright))) {
			return false;
		}
		if (trimToNull(definitions) == null) {
			if (trimToNull(other.definitions) != null) {
				return false;
			}
		} else if (!trimToNull(definitions).equals(trimToNull(other.definitions))) {
			return false;
		}
		if (trimToNull(denominator) == null) {
			if (trimToNull(other.denominator) != null) {
				return false;
			}
		} else if (!trimToNull(denominator).equals(trimToNull(other.denominator))) {
			return false;
		}
		if (trimToNull(denominatorExceptions) == null) {
			if (trimToNull(other.denominatorExceptions) != null) {
				return false;
			}
		} else if (!trimToNull(denominatorExceptions).equals(trimToNull(other.denominatorExceptions))) {
			return false;
		}
		if (trimToNull(denominatorExclusions) == null) {
			if (trimToNull(other.denominatorExclusions) != null) {
				return false;
			}
		} else if (!trimToNull(denominatorExclusions).equals(trimToNull(other.denominatorExclusions))) {
			return false;
		}
		if (trimToNull(description) == null) {
			if (trimToNull(other.description) != null) {
				return false;
			}
		} else if (!trimToNull(description).equals(trimToNull(other.description))) {
			return false;
		}
		if (trimToNull(disclaimer) == null) {
			if (trimToNull(other.disclaimer) != null) {
				return false;
			}
		} else if (!trimToNull(disclaimer).equals(trimToNull(other.disclaimer))) {
			return false;
		}
		endorseByNQF = endorseByNQF == null ? false : endorseByNQF.booleanValue();
		other.endorseByNQF = other.endorseByNQF == null ? false : other.endorseByNQF.booleanValue();
		if (!endorseByNQF.equals(other.endorseByNQF)) {
			return false;
		}
		if (trimToNull(finalizedDate) == null) {
			if (trimToNull(other.finalizedDate) != null) {
				return false;
			}
		} else if (!trimToNull(finalizedDate).equals(trimToNull(other.finalizedDate))) {
			return false;
		}
		if (trimToNull(groupId) == null) {
			if (trimToNull(other.groupId) != null) {
				return false;
			}
		} else if (!trimToNull(groupId).equals(trimToNull(other.groupId))) {
			return false;
		}
		if (trimToNull(groupName) == null) {
			if (trimToNull(other.groupName) != null) {
				return false;
			}
		} else if (!trimToNull(groupName).equals(trimToNull(other.groupName))) {
			return false;
		}
		if (trimToNull(guidance) == null) {
			if (trimToNull(other.guidance) != null) {
				return false;
			}
		} else if (!trimToNull(guidance).equals(trimToNull(other.guidance))) {
			return false;
		}
		if (trimToNull(improvNotations) == null) {
			if (trimToNull(other.improvNotations) != null) {
				return false;
			}
		} else if (!trimToNull(improvNotations).equals(trimToNull(other.improvNotations))) {
			return false;
		}
		if (trimToNull(initialPop) == null) {
			if (trimToNull(other.initialPop) != null) {
				return false;
			}
		} else if (!trimToNull(initialPop).equals(trimToNull(other.initialPop))) {
			return false;
		}
		if (trimToNull(measFromPeriod) == null) {
			if (trimToNull(other.measFromPeriod) != null) {
				return false;
			}
		} else if (!trimToNull(measFromPeriod).equals(trimToNull(other.measFromPeriod))) {
			return false;
		}
		/*if (trimToNull(measSteward) == null) {
			if (trimToNull(other.measSteward) != null) {
				return false;
			}
		} else if(trimToNull(measSteward).equals("Other")){
			if (trimToNull(measStewardOther) == null) {
				if (trimToNull(other.measStewardOther) != null) {
					return false;
				}
			} else if (!trimToNull(measStewardOther).equals(trimToNull(other.measStewardOther))) {
				return false;
			}
		}else if (!trimToNull(measSteward).equals(trimToNull(other.measSteward))){
			return false;
		}*/
		if (trimToNull(stewardValue) == null) {
			if (trimToNull(other.stewardValue) != null) {
				return false;
			}
		}else if (!trimToNull(stewardValue).equals(trimToNull(other.stewardValue))) {
			return false;
		}
		if(isCalenderYear!=other.isCalenderYear){
			return false;
		}
		if (trimToNull(stewardId) == null) {
			if (trimToNull(other.stewardId) != null) {
				return false;
			}
		}else if (!trimToNull(stewardId).equals(trimToNull(other.stewardId))) {
			return false;
		}
		if (trimToNull(measToPeriod) == null) {
			if (trimToNull(other.measToPeriod) != null) {
				return false;
			}
		} else if (!trimToNull(measToPeriod).equals(trimToNull(other.measToPeriod))) {
			return false;
		}
		if (trimToNull(measureId) == null) {
			if (trimToNull(other.measureId) != null) {
				return false;
			}
		} else if (!trimToNull(measureId).equals(trimToNull(other.measureId))) {
			return false;
		}
		if (trimToNull(measureObservations) == null) {
			if (trimToNull(other.measureObservations) != null) {
				return false;
			}
		} else if (!trimToNull(measureObservations).equals(trimToNull(other.measureObservations))) {
			return false;
		}
		if (trimToNull(measurePopulation) == null) {
			if (trimToNull(other.measurePopulation) != null) {
				return false;
			}
		} else if (!trimToNull(measurePopulation).equals(trimToNull(other.measurePopulation))) {
			return false;
		}
		if (trimToNull(measureSetId) == null) {
			if (trimToNull(other.measureSetId) != null) {
				return false;
			}
		} else if (!trimToNull(measureSetId).equals(trimToNull(other.measureSetId))) {
			return false;
		}
		/*if (trimToNull(measureStatus) == null) {
			if (trimToNull(other.measureStatus) != null) {
				return false;
			}
		} else if (!trimToNull(measureStatus).equals(trimToNull(other.measureStatus))) {
			return false;
		}*/
		if (toCompareMeasure == null) {
			if (other.toCompareMeasure != null) {
				return false;
			}
		} else if (!isEqual(toCompareMeasure, other.toCompareMeasure)) {
			return false;
		}
		
		
		if(toCompareComponentMeasures == null){
			if(other.toCompareComponentMeasures != null){
				return false;
			}
		} else if(!isEqual(toCompareComponentMeasures, other.toCompareComponentMeasures)){
			return false;
		}
		
		//		if (trimToNull(name) == null) {
		//			if (trimToNull(other.name) != null) {
		//				return false;
		//			}
		//		} else if (!trimToNull(name).equals(trimToNull(other.name))) {
		//			return false;
		//		}
		if (trimToNull(nqfId) == null) {
			if (trimToNull(other.nqfId) != null) {
				return false;
			}
		} else if (!trimToNull(nqfId).equals(trimToNull(other.nqfId))) {
			return false;
		}
		if (trimToNull(numerator) == null) {
			if (trimToNull(other.numerator) != null) {
				return false;
			}
		} else if (!trimToNull(numerator).equals(trimToNull(other.numerator))) {
			return false;
		}
		if (trimToNull(numeratorExclusions) == null) {
			if (trimToNull(other.numeratorExclusions) != null) {
				return false;
			}
		} else if (!trimToNull(numeratorExclusions).equals(trimToNull(other.numeratorExclusions))) {
			return false;
		}
		if (trimToNull(rateAggregation) == null) {
			if (trimToNull(other.rateAggregation) != null) {
				return false;
			}
		} else if (!trimToNull(rateAggregation).equals(trimToNull(other.rateAggregation))) {
			return false;
		}
		if (trimToNull(rationale) == null) {
			if (trimToNull(other.rationale) != null) {
				return false;
			}
		} else if (!trimToNull(rationale).equals(trimToNull(other.rationale))) {
			return false;
		}
		if (referencesList == null) {
			if (other.referencesList != null) {
				return false;
			}
		} else if (!isEqual(MeasureDetailsUtil.getTrimmedList(referencesList),
				MeasureDetailsUtil.getTrimmedList(other.referencesList))) {
			return false;
		}
		if (trimToNull(riskAdjustment) == null) {
			if (trimToNull(other.riskAdjustment) != null) {
				return false;
			}
		} else if (!trimToNull(riskAdjustment).equals(trimToNull(other.riskAdjustment))) {
			return false;
		}
		if (trimToNull(shortName) == null) {
			if (trimToNull(other.shortName) != null) {
				return false;
			}
		} else if (!trimToNull(shortName).equals(trimToNull(other.shortName))) {
			return false;
		}
		if (trimToNull(stratification) == null) {
			if (trimToNull(other.stratification) != null) {
				return false;
			}
		} else if (!trimToNull(stratification).equals(trimToNull(other.stratification))) {
			return false;
		}
		if (trimToNull(supplementalData) == null) {
			if (trimToNull(other.supplementalData) != null) {
				return false;
			}
		} else if (!trimToNull(supplementalData).equals(trimToNull(other.supplementalData))) {
			return false;
		}
		if (trimToNull(transmissionFormat) == null) {
			if (trimToNull(other.transmissionFormat) != null) {
				return false;
			}
		} else if (!trimToNull(transmissionFormat).equals(trimToNull(other.transmissionFormat))) {
			return false;
		}
		return true;
	}
	
	
	
	/**
	 * Checks if is equal.
	 * 
	 * @param listA
	 *            the list a
	 * @param listB
	 *            the list b
	 * @return true, if is equal
	 */
	public boolean isEqual(List listA, List listB) {
		if (listA.size() != listB.size()) {
			return false;
		}
		for (int i = 0; i < listA.size(); i++) {
			boolean checkIfEqual = true;
			if (listA.get(i) instanceof Author) {
				for (int j = 0; j < listA.size(); j++) {
					Author author = (Author) listA.get(j);
					for (int k = 0; k < listB.size(); k++) {
						Author otherAuthor = (Author) listB.get(k);
						if (author.compare(author, otherAuthor) == 0) {
							checkIfEqual = true;
							break;
						} else {
							checkIfEqual = false;
						}
					}
					if (!checkIfEqual) {
						break;
					}
				}
				return checkIfEqual;
				
			} else if (listA.get(i) instanceof MeasureType) {
				for (int j = 0; j < listA.size(); j++) {
					MeasureType measureType = (MeasureType) listA.get(j);
					for (int k = 0; k < listB.size(); k++) {
						MeasureType otherMeasureType = (MeasureType) listB
								.get(k);
						if (measureType.compare(measureType, otherMeasureType) == 0) {
							checkIfEqual = true;
							break;
						} else {
							checkIfEqual = false;
						}
					}
					if (!checkIfEqual) {
						break;
					}
				}
				return checkIfEqual;
				
			} else if (listA.get(i) instanceof String) {
				
				String val1 = (String) listA.get(i);
				String val2 = (String) listB.get(i);
				if (val1.compareTo(val2) != 0) {
					return false;
				}
			} else if (listA.get(i) instanceof ManageMeasureSearchModel.Result) {
				for (int j = 0; j < listA.size(); j++) {
					ManageMeasureSearchModel.Result val1 = (ManageMeasureSearchModel.Result) listA
							.get(j);
					for (int k = 0; k < listB.size(); k++) {
						ManageMeasureSearchModel.Result val2 = (ManageMeasureSearchModel.Result) listB
								.get(k);
						if (val1.compare(val1, val2) == 0) {
							checkIfEqual = true;
							break;
						} else {
							checkIfEqual = false;
						}
					}
					if (!checkIfEqual) {
						break;
					}
				}
				return checkIfEqual;
				
			}
		}
		return true;
	}
	
	
	/**
	 * Trim to null.
	 * 
	 * @param value
	 *            the value
	 * @return the string
	 */
	private String trimToNull(String value) {
		if (null != value) {
			value = value.replaceAll("[\r\n]", "");
			value = value.equals("") ? null : value.trim();
			
		}
		return value;
	}
	
	/**
	 * Gets the to compare author.
	 * 
	 * @return the toCompareAuthor
	 */
	public List<Author> getToCompareAuthor() {
		return toCompareAuthor;
	}
	
	/**
	 * Sets the to compare author.
	 * 
	 * @param toCompareAuthor
	 *            the toCompareAuthor to set
	 */
	public void setToCompareAuthor(List<Author> toCompareAuthor) {
		this.toCompareAuthor = toCompareAuthor;
	}
	
	/**
	 * Gets the to compare measure.
	 * 
	 * @return the toCompareMeasure
	 */
	public List<MeasureType> getToCompareMeasure() {
		return toCompareMeasure;
	}
	
	/**
	 * Sets the to compare measure.
	 * 
	 * @param toCompareMeasure
	 *            the toCompareMeasure to set
	 */
	public void setToCompareMeasure(List<MeasureType> toCompareMeasure) {
		this.toCompareMeasure = toCompareMeasure;
	}
	
	/**
	 * Gets the qds selected list.
	 *
	 * @return the qds selected list
	 */
	public List<QualityDataSetDTO> getQdsSelectedList() {
		return qdsSelectedList;
	}
	
	/**
	 * Sets the qds selected list.
	 *
	 * @param qdsSelectedList the new qds selected list
	 */
	public void setQdsSelectedList(List<QualityDataSetDTO> qdsSelectedList) {
		this.qdsSelectedList = qdsSelectedList;
	}
	
	
	/**
	 * Gets the to compare component measures.
	 *
	 * @return the to compare component measures
	 */
	public List<ManageMeasureSearchModel.Result> getToCompareComponentMeasures() {
		return toCompareComponentMeasures;
	}
	
	/**
	 * Sets the to compare component measures.
	 *
	 * @param toCompareComponentMeasures the new to compare component measures
	 */
	public void setToCompareComponentMeasures(
			List<ManageMeasureSearchModel.Result> toCompareComponentMeasures) {
		this.toCompareComponentMeasures = toCompareComponentMeasures;
	}
	
	
	/**
	 * Gets the qlty measure set uuid.
	 * 
	 * @return the qltyMeasureSetUuid
	 */
	public String getQltyMeasureSetUuid() {
		return qltyMeasureSetUuid;
	}
	
	/**
	 * Sets the qlty measure set uuid.
	 * 
	 * @param qltyMeasureSetUuid
	 *            the qltyMeasureSetUuid to set
	 */
	public void setQltyMeasureSetUuid(String qltyMeasureSetUuid) {
		this.qltyMeasureSetUuid = qltyMeasureSetUuid;
	}
	
	/**
	 * Gets the steward.
	 * 
	 * @return the steward
	 */
	/*public String getSteward() {
		if ((measSteward != null) && measSteward.equalsIgnoreCase("Other")) {
			return null;
		}
		return measSteward;
	}*/
	
	/**
	 * Gets the steward uuid.
	 * 
	 * @return the stewardUuid
	 */
	public String getStewardId() {
		return stewardId;
	}
	
	/**
	 * Sets the steward uuid.
	 *
	 * @param stewardId the new steward id
	 */
	public void setStewardId(String stewardId) {
		this.stewardId = stewardId;
	}
	
	/**
	 * Gets the scoring abbr.
	 * 
	 * @return the scoringAbbr
	 */
	public String getScoringAbbr() {
		return scoringAbbr;
	}
	
	/**
	 * Sets the scoring abbr.
	 * 
	 * @param scoringAbbr
	 *            the scoringAbbr to set
	 */
	public void setScoringAbbr(String scoringAbbr) {
		this.scoringAbbr = scoringAbbr;
	}
	
	/**
	 * Gets the period model.
	 * 
	 * @return the periodModel
	 */
	public PeriodModel getPeriodModel() {
		return periodModel;
	}
	
	/**
	 * Sets the period model.
	 * 
	 * @param periodModel
	 *            the periodModel to set
	 */
	public void setPeriodModel(PeriodModel periodModel) {
		this.periodModel = periodModel;
	}
	
	/**
	 * Gets the endorsement.
	 * 
	 * @return the endorsement
	 */
	public String getEndorsement() {
		return endorsement;
	}
	
	/**
	 * Sets the endorsement.
	 * 
	 * @param endorsement
	 *            the new endorsement
	 */
	public void setEndorsement(String endorsement) {
		this.endorsement = endorsement;
	}
	
	/**
	 * Gets the endorsement id.
	 * 
	 * @return the endorsement id
	 */
	public String getEndorsementId() {
		return endorsementId;
	}
	
	/**
	 * Sets the endorsement id.
	 * 
	 * @param endorsementId
	 *            the new endorsement id
	 */
	public void setEndorsementId(String endorsementId) {
		this.endorsementId = endorsementId;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ManageMeasureDetailModel [id=" + id + ", name=" + name
				+ ", shortName=" + shortName + ", versionNumber="
				+ versionNumber + ", revisionNumber=" + revisionNumber
				+ ", measureId=" + measureId + ", groupName=" + groupName
				+ ", groupId=" + groupId + ", finalizedDate=" + finalizedDate
				+ ", measFromPeriod=" + measFromPeriod + ", measToPeriod="
				+ measToPeriod +", isCalenderYear= "+ isCalenderYear
				+ ", measScoring=" + measScoring
				+ ", stewardValue=" + stewardValue + ", endorseByNQF="
				+ endorseByNQF + ", nqfId=" + nqfId + ", description="
				+ description + ", copyright=" + copyright
				+ ", clinicalRecomms=" + clinicalRecomms + ", definitions="
				+ definitions + ", guidance=" + guidance
				+ ", transmissionFormat=" + transmissionFormat + ", rationale="
				+ rationale + ", improvNotations=" + improvNotations
				+ ", stratification=" + stratification + ", referencesList="
				+ referencesList + ", authorSelectedList=" + authorSelectedList
				+ ", stewardSelectedList=" + stewardSelectedList
				+ ", measureTypeSelectedList=" + measureTypeSelectedList
				+ ", qdsSelectedList=" + qdsSelectedList
				+ ", componentMeasuresSelectedList="
				+ componentMeasuresSelectedList + ", toCompareAuthor="
				+ toCompareAuthor + ", toCompareMeasure=" + toCompareMeasure
				+ ", toCompareComponentMeasures=" + toCompareComponentMeasures
				+ ", draft=" + draft + ", measureSetId=" + measureSetId
				+ ", valueSetDate=" + valueSetDate + ", supplementalData="
				+ supplementalData + ", disclaimer=" + disclaimer
				+ ", riskAdjustment=" + riskAdjustment + ", rateAggregation="
				+ rateAggregation + ", initialPop=" + initialPop
				+ ", denominator=" + denominator + ", denominatorExclusions="
				+ denominatorExclusions + ", numerator=" + numerator
				+ ", numeratorExclusions=" + numeratorExclusions
				+ ", denominatorExceptions=" + denominatorExceptions
				+ ", measurePopulation=" + measurePopulation
				+ ", measureObservations=" + measureObservations
				+ ", eMeasureId=" + eMeasureId + ", orgVersionNumber="
				+ orgVersionNumber + ", qltyMeasureSetUuid="
				+ qltyMeasureSetUuid + ", stewardId=" + stewardId
				+ ", scoringAbbr=" + scoringAbbr + ", periodModel="
				+ periodModel + ", endorsement=" + endorsement
				+ ", endorsementId=" + endorsementId + ", nqfModel=" + nqfModel
				+ ", isDeleted=" + isDeleted + ", measureOwnerId="
				+ measureOwnerId + ", measurePopulationExclusions="
				+ measurePopulationExclusions + ", isEditable=" + isEditable
				+"]";
	}
	
	/**
	 * Gets the nqf model.
	 * 
	 * @return the nqf model
	 */
	public NqfModel getNqfModel() {
		return nqfModel;
	}
	
	/**
	 * Sets the nqf model.
	 * 
	 * @param nqfModel
	 *            the new nqf model
	 */
	public void setNqfModel(NqfModel nqfModel) {
		this.nqfModel = nqfModel;
	}
	
	/**
	 * Gets the org version number.
	 * 
	 * @return the org version number
	 */
	public String getOrgVersionNumber() {
		return orgVersionNumber;
	}
	
	/**
	 * Sets the org version number.
	 * 
	 * @param orgVersionNumber
	 *            the new org version number
	 */
	public void setOrgVersionNumber(String orgVersionNumber) {
		this.orgVersionNumber = orgVersionNumber;
	}
	
	/**
	 * Gets the measure owner id.
	 * 
	 * @return the measure owner id
	 */
	public String getMeasureOwnerId() {
		return measureOwnerId;
	}
	
	/**
	 * Sets the measure owner id.
	 * 
	 * @param measureOwnerId
	 *            the new measure owner id
	 */
	public void setMeasureOwnerId(String measureOwnerId) {
		this.measureOwnerId = measureOwnerId;
	}
	
	/**
	 * Gets the revision number.
	 *
	 * @return the revisionNumber
	 */
	public String getRevisionNumber() {
		return revisionNumber;
	}
	
	/**
	 * Sets the revision number.
	 *
	 * @param revisionNumber the revisionNumber to set
	 */
	public void setRevisionNumber(String revisionNumber) {
		this.revisionNumber = revisionNumber;
	}
	
	/**
	 * Gets the measure population exclusions.
	 *
	 * @return the measurePopulationExclusions
	 */
	public String getMeasurePopulationExclusions() {
		return measurePopulationExclusions;
	}
	
	/**
	 * Sets the measure population exclusions.
	 *
	 * @param measurePopulationExclusions the measurePopulationExclusions to set
	 */
	public void setMeasurePopulationExclusions(
			String measurePopulationExclusions) {
		this.measurePopulationExclusions = measurePopulationExclusions;
	}
	
	/**
	 * Gets the component measures selected list.
	 *
	 * @return the component measures selected list
	 */
	public List<ManageMeasureSearchModel.Result> getComponentMeasuresSelectedList() {
		return componentMeasuresSelectedList;
	}
	
	/**
	 * Sets the component measures selected list.
	 *
	 * @param componentMeasuresSelectedList the new component measures selected list
	 */
	public void setComponentMeasuresSelectedList(
			List<ManageMeasureSearchModel.Result> componentMeasuresSelectedList) {
		this.componentMeasuresSelectedList = componentMeasuresSelectedList;
	}
	
	/**
	 * Checks if is editable.
	 *
	 * @return true, if is editable
	 */
	public boolean isEditable() {
		return isEditable;
	}
	
	/**
	 * Sets the editable.
	 *
	 * @param isEditable the new editable
	 */
	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
	
	/**
	 * Gets the steward selected list.
	 *
	 * @return the steward selected list
	 */
	public List<MeasureSteward> getStewardSelectedList() {
		return stewardSelectedList;
	}
	
	/**
	 * Sets the steward selected list.
	 *
	 * @param steSelectedList the new steward selected list
	 */
	public void setStewardSelectedList(List<MeasureSteward> steSelectedList) {
		stewardSelectedList =steSelectedList;
	}
	
	/**
	 * Gets the steward value.
	 *
	 * @return the steward value
	 */
	public String getStewardValue() {
		return stewardValue;
	}
	
	/**
	 * Sets the steward value.
	 *
	 * @param stewardValue the new steward value
	 */
	public void setStewardValue(String stewardValue) {
		this.stewardValue = stewardValue;
	}
	
	/**
	 * is the measure patient based?
	 * @return true if the measure is patient based, false if it is not. 
	 */
	public boolean isPatientBased() {
		return isPatientBased;
	}
	
	/**
	 * set is the measure patient based
	 */
	public void setIsPatientBased(boolean isPatientBased) {
		this.isPatientBased = isPatientBased;
	}
	
	/**
	 * Gets the measure detail result
	 * 
	 * @return the mneasure detail result
	 */
	public MeasureDetailResult getMeasureDetailResult() {
		return measureDetailResult;
	}

	/**
	 * Sets the measure detail result
	 * 
	 * @param measureDetailResult the measure detail result
	 */
	public void setMeasureDetailResult(MeasureDetailResult measureDetailResult) {
		this.measureDetailResult = measureDetailResult;
	}
	
	
	
	/*@Override
	public void scrubForMarkUp() {
		
	}*/
	
	@Override
	public void scrubForMarkUp() {
		String markupRegExp = "<[^>]+>";
		if(this.getName() != null) {
			String noMarkupText = this.getName().trim().replaceAll(markupRegExp, "");
			System.out.println("measure name:"+noMarkupText);
			if(this.getName().trim().length() > noMarkupText.length()){
				this.setName(noMarkupText);
			}
		}
		if(this.getShortName() != null) {
			String noMarkupText = this.getShortName().trim().replaceAll(markupRegExp, "");
			System.out.println("measure short-name:"+noMarkupText);
			if(this.getShortName().trim().length() > noMarkupText.length()){
				this.setShortName(noMarkupText);
			}
		}
		if(this.getNqfId() != null) {
			String noMarkupText = this.getNqfId().trim().replaceAll(markupRegExp, "");
			System.out.println("measure nqfid:"+noMarkupText);
			if(this.getNqfId().trim().length() > noMarkupText.length()){
				this.setNqfId(noMarkupText);
			}
		}
		/*if(this.getDescription() != null) {
			String noMarkupText = this.getDescription().trim().replaceAll(markupRegExp, "");
			System.out.println("measure Description:"+noMarkupText);
			if(this.getDescription().trim().length() > noMarkupText.length()){
				this.setDescription(noMarkupText);
			}
		}*/
		/*if(this.getCopyright() != null) {
			String noMarkupText = this.getCopyright().trim().replaceAll(markupRegExp, "");
			System.out.println("measure CopyRight:"+noMarkupText);
			if(this.getCopyright().trim().length() > noMarkupText.length()){
				this.setCopyright(noMarkupText);
			}
		}*/
		/*if(this.getDisclaimer() != null) {
			String noMarkupText = this.getDisclaimer().trim().replaceAll(markupRegExp, "");
			System.out.println("measure Disclaimer:"+noMarkupText);
			if(this.getDisclaimer().trim().length() > noMarkupText.length()){
				this.setDisclaimer(noMarkupText);
			}
		}*/
		/*if(this.getRiskAdjustment() != null) {
			String noMarkupText = this.getRiskAdjustment().trim().replaceAll(markupRegExp, "");
			System.out.println("measure RiskAdjustment:"+noMarkupText);
			if(this.getRiskAdjustment().trim().length() > noMarkupText.length()){
				this.setRiskAdjustment(noMarkupText);
			}
		}*/
		/*if(this.getRateAggregation() != null) {
			String noMarkupText = this.getRateAggregation().trim().replaceAll(markupRegExp, "");
			System.out.println("measure Rate Aggregation:"+noMarkupText);
			if(this.getRateAggregation().trim().length() > noMarkupText.length()){
				this.setRateAggregation(noMarkupText);
			}
		}*/
		/*if(this.getRationale() != null) {
			String noMarkupText = this.getRationale().trim().replaceAll(markupRegExp, "");
			System.out.println("measure Rationale:"+noMarkupText);
			if(this.getRationale().trim().length() > noMarkupText.length()){
				this.setRationale(noMarkupText);
			}
		}*/
		/*if(this.getClinicalRecomms() != null) {
			String noMarkupText = this.getClinicalRecomms().trim().replaceAll(markupRegExp, "");
			System.out.println("measure ClinicalRecomms:"+noMarkupText);
			if(this.getClinicalRecomms().trim().length() > noMarkupText.length()){
				this.setClinicalRecomms(noMarkupText);
			}
		}*/
		/*if(this.getImprovNotations() != null) {
			String noMarkupText = this.getImprovNotations().trim().replaceAll(markupRegExp, "");
			System.out.println("measure ImprovNotations:"+noMarkupText);
			if(this.getImprovNotations().trim().length() > noMarkupText.length()){
				this.setImprovNotations(noMarkupText);
			}
		}*/
		/*if(this.getDefinitions() != null) {
			String noMarkupText = this.getDefinitions().trim().replaceAll(markupRegExp, "");
			System.out.println("measure Definitions:"+noMarkupText);
			if(this.getDefinitions().trim().length() > noMarkupText.length()){
				this.setDefinitions(noMarkupText);
			}
		}*/
		/*if(this.getGuidance() != null) {
			String noMarkupText = this.getGuidance().trim().replaceAll(markupRegExp, "");
			System.out.println("measure Guidance:"+noMarkupText);
			if(this.getGuidance().trim().length() > noMarkupText.length()){
				this.setGuidance(noMarkupText);
			}
		}*/
		/*if(this.getTransmissionFormat() != null) {
			String noMarkupText = this.getTransmissionFormat().trim().replaceAll(markupRegExp, "");
			System.out.println("measure TransmissionFormat:"+noMarkupText);
			if(this.getTransmissionFormat().trim().length() > noMarkupText.length()){
				this.setTransmissionFormat(noMarkupText);
			}
		}*/
		/*if(this.getSupplementalData() != null) {
			String noMarkupText = this.getSupplementalData().trim().replaceAll(markupRegExp, "");
			System.out.println("measure SupplementalData:"+noMarkupText);
			if(this.getSupplementalData().trim().length() > noMarkupText.length()){
				this.setSupplementalData(noMarkupText);
			}
		}*/
		/*if(this.getGroupName() != null) {
			String noMarkupText = this.getGroupName().trim().replaceAll(markupRegExp, "");
			System.out.println("measure Measure Set:"+noMarkupText);
			if(this.getGroupName().trim().length() > noMarkupText.length()){
				this.setGroupName(noMarkupText);
			}
		}*/
		/*if(this.getStratification() != null) {
			String noMarkupText = this.getStratification().trim().replaceAll(markupRegExp, "");
			System.out.println("measure Stratification:"+noMarkupText);
			if(this.getStratification().trim().length() > noMarkupText.length()){
				this.setStratification(noMarkupText);
			}
		}*/
		/*if(this.getInitialPop() != null) {
			String noMarkupText = this.getInitialPop().trim().replaceAll(markupRegExp, "");
			System.out.println("measure InitialPop:"+noMarkupText);
			if(this.getInitialPop().trim().length() > noMarkupText.length()){
				this.setInitialPop(noMarkupText);
			}
		}*/
		/*if(this.getMeasurePopulation() != null) {
			String noMarkupText = this.getMeasurePopulation().trim().replaceAll(markupRegExp, "");
			System.out.println("measure Measure Population:"+noMarkupText);
			if(this.getMeasurePopulation().trim().length() > noMarkupText.length()){
				this.setMeasurePopulation(noMarkupText);
			}
		}*/
		/*if(this.getMeasurePopulationExclusions() != null) {
			String noMarkupText = this.getMeasurePopulationExclusions().trim().replaceAll(markupRegExp, "");
			System.out.println("measure MeasurePopulationExclusions:"+noMarkupText);
			if(this.getMeasurePopulationExclusions().trim().length() > noMarkupText.length()){
				this.setMeasurePopulationExclusions(noMarkupText);
			}
		}*/
		/*if(this.getMeasureObservations() != null) {
			String noMarkupText = this.getMeasureObservations().trim().replaceAll(markupRegExp, "");
			System.out.println("measure MeasureObservations:"+noMarkupText);
			if(this.getMeasureObservations().trim().length() > noMarkupText.length()){
				this.setMeasureObservations(noMarkupText);
			}
		}*/
		/*if(this.getDenominator() != null) {
			String noMarkupText = this.getDenominator().trim().replaceAll(markupRegExp, "");
			System.out.println("measure Denominator:"+noMarkupText);
			if(this.getDenominator().trim().length() > noMarkupText.length()){
				this.setDenominator(noMarkupText);
			}
		}*/
		/*if(this.getDenominatorExceptions() != null) {
			String noMarkupText = this.getDenominatorExceptions().trim().replaceAll(markupRegExp, "");
			System.out.println("measure Denominator Excep:"+noMarkupText);
			if(this.getDenominatorExceptions().trim().length() > noMarkupText.length()){
				this.setDenominatorExceptions(noMarkupText);
			}
		}*/
		/*if(this.getDenominatorExclusions() != null) {
			String noMarkupText = this.getDenominatorExclusions().trim().replaceAll(markupRegExp, "");
			System.out.println("measure Denominator Exclu:"+noMarkupText);
			if(this.getDenominatorExclusions().trim().length() > noMarkupText.length()){
				this.setDenominatorExclusions(noMarkupText);
			}
		}*/
		/*if(this.getNumerator() != null) {
			String noMarkupText = this.getNumerator().trim().replaceAll(markupRegExp, "");
			System.out.println("measure Numerator :"+noMarkupText);
			if(this.getNumerator().trim().length() > noMarkupText.length()){
				this.setNumerator(noMarkupText);
			}
		}*/
		/*if(this.getNumeratorExclusions() != null) {
			String noMarkupText = this.getNumeratorExclusions().trim().replaceAll(markupRegExp, "");
			System.out.println("measure Numerator Exclu :"+noMarkupText);
			if(this.getNumeratorExclusions().trim().length() > noMarkupText.length()){
				this.setNumeratorExclusions(noMarkupText);
			}
		}*/
		/*if(this.getReferencesList() != null) {
			List<String> referenceList = new ArrayList<String>();
			for(int i=0; i< this.getReferencesList().size(); i++ ){
				String noMarkupText = this.getReferencesList().get(i).trim().replaceAll(markupRegExp, "");
				System.out.println("measure Reference:"+i+ " index is :"+ noMarkupText);
				if(this.getReferencesList().get(i).trim().length() > noMarkupText.length()){
					referenceList.add(noMarkupText);
				} else {
					referenceList.add(this.getReferencesList().get(i));
				}
			}
			this.getReferencesList().clear();
			this.setReferencesList(referenceList);
		}*/
		
	}
	
	/**
	 * Gets the steward value.
	 *
	 * @return the steward value
	 */
	/*public String getStewardValue() {
		return stewardValue;
	}*/
	
	/**
	 * Sets the steward value.
	 *
	 * @param stewardValue the new steward value
	 */
	/*public void setStewardValue(String stewardValue) {
		this.stewardValue = stewardValue;
	}*/
	
	
}