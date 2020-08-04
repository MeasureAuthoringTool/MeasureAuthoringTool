package mat.client.measure;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.client.clause.clauseworkspace.model.MeasureDetailResult;
import mat.model.Author;
import mat.model.BaseModel;
import mat.model.MeasureSteward;
import mat.model.MeasureType;
import mat.model.QualityDataSetDTO;
import mat.shared.model.util.MeasureDetailsUtil;


public class ManageMeasureDetailModel implements IsSerializable, BaseModel {
    private String id;
    private String measureName;
    private String measureModel;
    private String cqlLibraryName;
    private String shortName;
    private String versionNumber = "0.0";
    private String revisionNumber = "000";
    private String measureId;
    private String groupName;
    private String groupId;
    private String finalizedDate;
    private String measFromPeriod;
    private String measToPeriod;
    private String measScoring;
    private String stewardValue;
    private Boolean endorseByNQF;
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
    private List<ReferenceTextAndType> referencesList;
    private List<Author> authorSelectedList;
    private List<MeasureSteward> stewardSelectedList;
    private List<MeasureType> measureTypeSelectedList;
    private List<QualityDataSetDTO> qdsSelectedList;
    private List<ManageMeasureSearchModel.Result> componentMeasuresSelectedList;
    private List<Author> toCompareAuthor;
    private List<MeasureType> toCompareMeasure;
    private List<ManageMeasureSearchModel.Result> toCompareComponentMeasures;
    private boolean draft = true;
    private String measureSetId;
    private String valueSetDate;
    private String supplementalData;
    private String disclaimer;
    private String riskAdjustment;
    private String rateAggregation;
    private String initialPop;
    private String denominator;
    private String denominatorExclusions;
    private String numerator;
    private String numeratorExclusions;
    private String denominatorExceptions;
    private String measurePopulation;
    private String measureObservations;
    private int eMeasureId;
    private String orgVersionNumber;
    //Below fields are added for Castor mapping XML generation purpose
    private String qltyMeasureSetUuid;
    private String stewardId;
    private String scoringAbbr;
    private PeriodModel periodModel;
    private String endorsement;
    private String endorsementId;
    private NqfModel nqfModel;
    private boolean isDeleted;
    private String measureOwnerId;
    private String measurePopulationExclusions;
    private boolean isEditable;
    private boolean isCalenderYear;
    private boolean isPatientBased;
    private MeasureDetailResult measureDetailResult;
    private String qdmVersion;
    private String fhirVersion;
    private String formattedVersion;
    private boolean experimental;

    public ManageMeasureDetailModel() {

    }

    public boolean isFhir() {
        return measureModel != null && measureModel.equals("FHIR");
    }

    public boolean isCalenderYear() {
        return isCalenderYear;
    }

    public void setCalenderYear(boolean isCalenderYear) {
        this.isCalenderYear = isCalenderYear;
    }


    public boolean isDeleted() {
        return isDeleted;
    }


    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isDraft() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }

    public List<ReferenceTextAndType> getReferencesList() {
        return referencesList;
    }

    public void setReferencesList(List<ReferenceTextAndType> referencesList) {
        this.referencesList = referencesList;
    }

    public List<Author> getAuthorSelectedList() {
        return authorSelectedList;
    }

    public void setAuthorSelectedList(List<Author> authorSelectedList) {
        this.authorSelectedList = authorSelectedList;
    }

    public List<MeasureType> getMeasureTypeSelectedList() {
        return measureTypeSelectedList;
    }

    public void setMeasureTypeSelectedList(List<MeasureType> measureTypeSelectedList) {
        this.measureTypeSelectedList = measureTypeSelectedList;
    }

    public String getInitialPop() {
        return initialPop;
    }

    public void setInitialPop(String initialPop) {
        this.initialPop = initialPop;
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
        this.finalizedDate = doTrim(finalizedDate);
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
        this.riskAdjustment = doTrim(riskAdjustment);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = doTrim(id);
    }

    public String getMeasureName() {
        return measureName;
    }

    public void setMeasureModel(String measureModel) {
        this.measureModel = measureModel;
    }

    public String getMeasureModel() {
        return measureModel;
    }

    public void setMeasureName(String name) {
        this.measureName = doTrim(name);
    }

    //TODO make sure these are correlate with measure hierarchy
    public String getCQLLibraryName() {
        return cqlLibraryName;
    }

    public void setCQLLibraryName(String name) {
        this.cqlLibraryName = doTrim(name);
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = doTrim(shortName);
    }

    public String getNqfId() {
        return nqfId;
    }

    public void setNqfId(String nqfId) {
        this.nqfId = doTrim(nqfId);
    }

    private String doTrim(String str) {
        return (str != null) && (str.trim().length() > 0) ? str.trim() : null;
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
        this.copyright = doTrim(copyright);
    }

    public String getTransmissionFormat() {
        return transmissionFormat;
    }

    public void setTransmissionFormat(String transmissionFormat) {
        this.transmissionFormat = doTrim(transmissionFormat);
    }

    public String getSupplementalData() {
        return supplementalData;
    }

    public void setSupplementalData(String supplementalData) {
        this.supplementalData = doTrim(supplementalData);
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = doTrim(disclaimer);
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setRateAggregation(String rateAggregation) {
        this.rateAggregation = doTrim(rateAggregation);
    }

    public String getRateAggregation() {
        return rateAggregation;
    }

    public void setDenominator(String denominator) {
        this.denominator = doTrim(denominator);
    }

    public String getDenominator() {
        return denominator;
    }

    public void setDenominatorExclusions(String denominatorExclusions) {
        this.denominatorExclusions = doTrim(denominatorExclusions);
    }

    public String getDenominatorExclusions() {
        return denominatorExclusions;
    }

    public void setNumerator(String numerator) {
        this.numerator = doTrim(numerator);
    }

    public String getNumerator() {
        return numerator;
    }

    public void setNumeratorExclusions(String numeratorExclusions) {
        this.numeratorExclusions = doTrim(numeratorExclusions);
    }

    public String getNumeratorExclusions() {
        return numeratorExclusions;
    }

    public void setDenominatorExceptions(String denominatorExceptions) {
        this.denominatorExceptions = doTrim(denominatorExceptions);
    }

    public String getDenominatorExceptions() {
        return denominatorExceptions;
    }

    public void setMeasurePopulation(String measurePopulation) {
        this.measurePopulation = doTrim(measurePopulation);
    }

    public String getMeasurePopulation() {
        return measurePopulation;
    }

    public void setMeasureObservations(String measureObservations) {
        this.measureObservations = doTrim(measureObservations);
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
        result = (prime * result) + ((measureName == null) ? 0 : measureName.hashCode());
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
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

        if (trimToNull(stewardValue) == null) {
            if (trimToNull(other.stewardValue) != null) {
                return false;
            }
        } else if (!trimToNull(stewardValue).equals(trimToNull(other.stewardValue))) {
            return false;
        }
        if (isCalenderYear != other.isCalenderYear) {
            return false;
        }
        if (trimToNull(stewardId) == null) {
            if (trimToNull(other.stewardId) != null) {
                return false;
            }
        } else if (!trimToNull(stewardId).equals(trimToNull(other.stewardId))) {
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

        if (toCompareMeasure == null) {
            if (other.toCompareMeasure != null) {
                return false;
            }
        } else if (!isEqual(toCompareMeasure, other.toCompareMeasure)) {
            return false;
        }

        if (endorseByNQF) {
            if (trimToNull(nqfId) == null) {
                if (trimToNull(other.nqfId) != null) {
                    return false;
                }
            } else if (!trimToNull(nqfId).equals(trimToNull(other.nqfId))) {
                return false;
            }
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

    @SuppressWarnings("rawtypes")
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

    private String trimToNull(String value) {
        if (null != value) {
            value = value.replaceAll("[\r\n]", "");
            value = value.equals("") ? null : value.trim();

        }
        return value;
    }

    public List<Author> getToCompareAuthor() {
        return toCompareAuthor;
    }

    public void setToCompareAuthor(List<Author> toCompareAuthor) {
        this.toCompareAuthor = toCompareAuthor;
    }

    public List<MeasureType> getToCompareMeasure() {
        return toCompareMeasure;
    }


    public void setToCompareMeasure(List<MeasureType> toCompareMeasure) {
        this.toCompareMeasure = toCompareMeasure;
    }

    public List<QualityDataSetDTO> getQdsSelectedList() {
        return qdsSelectedList;
    }

    public void setQdsSelectedList(List<QualityDataSetDTO> qdsSelectedList) {
        this.qdsSelectedList = qdsSelectedList;
    }

    public List<ManageMeasureSearchModel.Result> getToCompareComponentMeasures() {
        return toCompareComponentMeasures;
    }

    public void setToCompareComponentMeasures(
            List<ManageMeasureSearchModel.Result> toCompareComponentMeasures) {
        this.toCompareComponentMeasures = toCompareComponentMeasures;
    }

    public String getQltyMeasureSetUuid() {
        return qltyMeasureSetUuid;
    }

    public void setQltyMeasureSetUuid(String qltyMeasureSetUuid) {
        this.qltyMeasureSetUuid = qltyMeasureSetUuid;
    }

    public String getStewardId() {
        return stewardId;
    }

    public void setStewardId(String stewardId) {
        this.stewardId = stewardId;
    }

    public String getScoringAbbr() {
        return scoringAbbr;
    }

    public void setScoringAbbr(String scoringAbbr) {
        this.scoringAbbr = scoringAbbr;
    }

    public PeriodModel getPeriodModel() {
        return periodModel;
    }

    public void setPeriodModel(PeriodModel periodModel) {
        this.periodModel = periodModel;
    }

    public String getEndorsement() {
        return endorsement;
    }

    public void setEndorsement(String endorsement) {
        this.endorsement = endorsement;
    }

    public String getEndorsementId() {
        return endorsementId;
    }

    public void setEndorsementId(String endorsementId) {
        this.endorsementId = endorsementId;
    }

    public NqfModel getNqfModel() {
        return nqfModel;
    }

    public void setNqfModel(NqfModel nqfModel) {
        this.nqfModel = nqfModel;
    }

    public String getOrgVersionNumber() {
        return orgVersionNumber;
    }

    public void setOrgVersionNumber(String orgVersionNumber) {
        this.orgVersionNumber = orgVersionNumber;
    }

    public String getMeasureOwnerId() {
        return measureOwnerId;
    }

    public void setMeasureOwnerId(String measureOwnerId) {
        this.measureOwnerId = measureOwnerId;
    }

    public String getRevisionNumber() {
        return revisionNumber;
    }

    public void setRevisionNumber(String revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    public String getMeasurePopulationExclusions() {
        return measurePopulationExclusions;
    }

    public void setMeasurePopulationExclusions(
            String measurePopulationExclusions) {
        this.measurePopulationExclusions = measurePopulationExclusions;
    }

    public List<ManageMeasureSearchModel.Result> getComponentMeasuresSelectedList() {
        return componentMeasuresSelectedList;
    }

    public void setComponentMeasuresSelectedList(
            List<ManageMeasureSearchModel.Result> componentMeasuresSelectedList) {
        this.componentMeasuresSelectedList = componentMeasuresSelectedList;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }


    public List<MeasureSteward> getStewardSelectedList() {
        return stewardSelectedList;
    }

    public void setStewardSelectedList(List<MeasureSteward> steSelectedList) {
        stewardSelectedList = steSelectedList;
    }

    public String getStewardValue() {
        return stewardValue;
    }

    public void setStewardValue(String stewardValue) {
        this.stewardValue = stewardValue;
    }

    public boolean isPatientBased() {
        return isPatientBased;
    }

    public void setIsPatientBased(boolean isPatientBased) {
        this.isPatientBased = isPatientBased;
    }

    public MeasureDetailResult getMeasureDetailResult() {
        return measureDetailResult;
    }

    public void setMeasureDetailResult(MeasureDetailResult measureDetailResult) {
        this.measureDetailResult = measureDetailResult;
    }

    public String getQdmVersion() {
        return qdmVersion;
    }

    public void setQdmVersion(String qdmVersion) {
        this.qdmVersion = qdmVersion;
    }

    public String getFhirVersion() {
        return fhirVersion;
    }

    public void setFhirVersion(String fhirVersion) {
        this.fhirVersion = fhirVersion;
    }

    @Override
    public void scrubForMarkUp() {
        String markupRegExp = "<[^>]+>";
        if (this.getMeasureName() != null) {
            String noMarkupText = this.getMeasureName().trim().replaceAll(markupRegExp, "");
            System.out.println("measure name:" + noMarkupText);
            if (this.getMeasureName().trim().length() > noMarkupText.length()) {
                this.setMeasureName(noMarkupText);
            }
        }
        if (this.getShortName() != null) {
            String noMarkupText = this.getShortName().trim().replaceAll(markupRegExp, "");
            System.out.println("measure short-name:" + noMarkupText);
            if (this.getShortName().trim().length() > noMarkupText.length()) {
                this.setShortName(noMarkupText);
            }
        }
        if (this.getNqfId() != null) {
            String noMarkupText = this.getNqfId().trim().replaceAll(markupRegExp, "");
            System.out.println("measure nqfid:" + noMarkupText);
            if (this.getNqfId().trim().length() > noMarkupText.length()) {
                this.setNqfId(noMarkupText);
            }
        }
    }

    public String getFormattedVersion() {
        return formattedVersion;
    }

    public void setFormattedVersion(String formattedVersion) {
        this.formattedVersion = formattedVersion;
    }

    @Override
    public String toString() {
        return "ManageMeasureDetailModel{" +
                "id='" + id + '\'' +
                ", measureName='" + measureName + '\'' +
                ", measureModel='" + measureModel + '\'' +
                ", cqlLibraryName='" + cqlLibraryName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", versionNumber='" + versionNumber + '\'' +
                ", revisionNumber='" + revisionNumber + '\'' +
                ", measureId='" + measureId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", groupId='" + groupId + '\'' +
                ", finalizedDate='" + finalizedDate + '\'' +
                ", measFromPeriod='" + measFromPeriod + '\'' +
                ", measToPeriod='" + measToPeriod + '\'' +
                ", measScoring='" + measScoring + '\'' +
                ", stewardValue='" + stewardValue + '\'' +
                ", endorseByNQF=" + endorseByNQF +
                ", nqfId='" + nqfId + '\'' +
                ", description='" + description + '\'' +
                ", copyright='" + copyright + '\'' +
                ", clinicalRecomms='" + clinicalRecomms + '\'' +
                ", definitions='" + definitions + '\'' +
                ", guidance='" + guidance + '\'' +
                ", transmissionFormat='" + transmissionFormat + '\'' +
                ", rationale='" + rationale + '\'' +
                ", improvNotations='" + improvNotations + '\'' +
                ", stratification='" + stratification + '\'' +
                ", referencesList=" + referencesList +
                ", authorSelectedList=" + authorSelectedList +
                ", stewardSelectedList=" + stewardSelectedList +
                ", measureTypeSelectedList=" + measureTypeSelectedList +
                ", qdsSelectedList=" + qdsSelectedList +
                ", componentMeasuresSelectedList=" + componentMeasuresSelectedList +
                ", toCompareAuthor=" + toCompareAuthor +
                ", toCompareMeasure=" + toCompareMeasure +
                ", toCompareComponentMeasures=" + toCompareComponentMeasures +
                ", draft=" + draft +
                ", measureSetId='" + measureSetId + '\'' +
                ", valueSetDate='" + valueSetDate + '\'' +
                ", supplementalData='" + supplementalData + '\'' +
                ", disclaimer='" + disclaimer + '\'' +
                ", riskAdjustment='" + riskAdjustment + '\'' +
                ", rateAggregation='" + rateAggregation + '\'' +
                ", initialPop='" + initialPop + '\'' +
                ", denominator='" + denominator + '\'' +
                ", denominatorExclusions='" + denominatorExclusions + '\'' +
                ", numerator='" + numerator + '\'' +
                ", numeratorExclusions='" + numeratorExclusions + '\'' +
                ", denominatorExceptions='" + denominatorExceptions + '\'' +
                ", measurePopulation='" + measurePopulation + '\'' +
                ", measureObservations='" + measureObservations + '\'' +
                ", eMeasureId=" + eMeasureId +
                ", orgVersionNumber='" + orgVersionNumber + '\'' +
                ", qltyMeasureSetUuid='" + qltyMeasureSetUuid + '\'' +
                ", stewardId='" + stewardId + '\'' +
                ", scoringAbbr='" + scoringAbbr + '\'' +
                ", periodModel=" + periodModel +
                ", endorsement='" + endorsement + '\'' +
                ", endorsementId='" + endorsementId + '\'' +
                ", nqfModel=" + nqfModel +
                ", isDeleted=" + isDeleted +
                ", measureOwnerId='" + measureOwnerId + '\'' +
                ", measurePopulationExclusions='" + measurePopulationExclusions + '\'' +
                ", isEditable=" + isEditable +
                ", isCalenderYear=" + isCalenderYear +
                ", isPatientBased=" + isPatientBased +
                ", measureDetailResult=" + measureDetailResult +
                ", qdmVersion='" + qdmVersion + '\'' +
                ", fhirVersion='" + fhirVersion + '\'' +
                ", formattedVersion='" + formattedVersion + '\'' +
                ", experimental=" + experimental +
                '}';
    }

    public boolean isExperimental() {
        return experimental;
    }

    public void setExperimental(boolean experimental) {
        this.experimental = experimental;
    }
}