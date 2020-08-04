package mat.server.humanreadable.cql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import mat.client.measure.ManageCompositeMeasureDetailModel;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ReferenceTextAndType;

public class HumanReadableMeasureInformationModel {
    private double qdmVersion;
    private String ecqmTitle;
    private String ecqmIdentifier;
    private String ecqmVersionNumber;
    private String nqfNumber;
    private String guid;
    private String measurementPeriod;
    private boolean isCalendarYear;
    private String measurementPeriodStartDate;
    private String measurementPeriodEndDate;
    private String measureSteward;
    private List<String> measureDevelopers;
    private String endorsedBy;
    private String description;
    private String copyright;
    private String disclaimer;
    private String compositeScoringMethod;
    private String measureScoring;
    private List<String> measureTypes;
    private List<HumanReadableComponentMeasureModel> componentMeasures;
    private String stratification;
    private String riskAdjustment;
    private String rateAggregation;
    private String rationale;
    private String clinicalRecommendationStatement;
    private String improvementNotation;
    private List<ReferenceTextAndType> references;
    private String definition;
    private String guidance;
    private String transmissionFormat;
    private String initialPopulation;
    private String denominator;
    private String denominatorExclusions;
    private String denominatorExceptions;
    private String numerator;
    private String numeratorExclusions;
    private String measurePopulation;
    private String measurePopulationExclusions;
    private String measureObservations;
    private String supplementalDataElements;
    private String measureSet;
    private boolean patientBased;
    private boolean experimental;

    public HumanReadableMeasureInformationModel() {

    }

    public HumanReadableMeasureInformationModel(ManageMeasureDetailModel model) {
        if (StringUtils.isNotBlank(model.getQdmVersion())) {
            this.qdmVersion = Double.parseDouble(model.getQdmVersion());
        }

        this.ecqmTitle = model.getMeasureName();
        this.ecqmIdentifier = model.geteMeasureId() == 0 ? "" : model.geteMeasureId() + "";
        this.ecqmVersionNumber = model.getFormattedVersion().replace("Draft based on", "").replace("v", "").trim();
        this.nqfNumber = model.getNqfId();
        this.guid = model.getMeasureSetId();
        this.isCalendarYear = model.isCalenderYear();
        this.measurementPeriodStartDate = model.getMeasFromPeriod();
        this.measurementPeriodEndDate = model.getMeasToPeriod();
        this.measureSteward = model.getStewardValue();

        if (!CollectionUtils.isEmpty(model.getAuthorSelectedList())) {
            this.measureDevelopers = new ArrayList<>();
            model.getAuthorSelectedList().forEach(d -> this.measureDevelopers.add(d.getAuthorName()));
        }

        this.endorsedBy = model.getEndorsement();
        this.description = model.getDescription();
        this.copyright = model.getCopyright();
        this.disclaimer = model.getDisclaimer();

        if (!CollectionUtils.isEmpty(model.getComponentMeasuresSelectedList())) {
            ManageCompositeMeasureDetailModel compositeModel = (ManageCompositeMeasureDetailModel) model;
            this.componentMeasures = new ArrayList<>();
            compositeModel.getComponentMeasuresSelectedList().forEach(r -> {
                HumanReadableComponentMeasureModel componentModel = new HumanReadableComponentMeasureModel();
                componentModel.setId(r.getId());
                componentModel.setMeasureSetId(r.getMeasureSetId());
                componentModel.setName(r.getName());
                componentModel.setVersion(r.getVersion());
                this.componentMeasures.add(componentModel);
            });

            this.compositeScoringMethod = compositeModel.getCompositeScoringMethod();
        }

        this.measureScoring = model.getMeasScoring();

        if (!CollectionUtils.isEmpty(model.getMeasureTypeSelectedList())) {
            this.measureTypes = new ArrayList<>();
            model.getMeasureTypeSelectedList().forEach(mt -> this.measureTypes.add(mt.getDescription()));
            Collections.sort(this.measureTypes);
        }

        this.stratification = model.getStratification();
        this.riskAdjustment = model.getRiskAdjustment();
        this.rateAggregation = model.getRateAggregation();
        this.rationale = model.getRationale();
        this.clinicalRecommendationStatement = model.getClinicalRecomms();
        this.improvementNotation = model.getImprovNotations();
        if (model.isFhir()) {
            if ("decrease".equals(this.improvementNotation)) {
                this.improvementNotation = "Decreased score indicates improvement";
            } else {
                this.improvementNotation = "Increased score indicates improvement";
            }
        }

        if (!CollectionUtils.isEmpty(model.getReferencesList())) {
            this.references = new ArrayList<>(model.getReferencesList());
        }

        this.definition = model.getDefinitions();
        this.guidance = model.getGuidance();
        this.transmissionFormat = model.getTransmissionFormat();
        this.initialPopulation = model.getInitialPop();
        this.denominator = model.getDenominator();
        this.denominatorExclusions = model.getDenominatorExclusions();
        this.denominatorExceptions = model.getDenominatorExceptions();
        this.numerator = model.getNumerator();
        this.numeratorExclusions = model.getNumeratorExclusions();
        this.measurePopulation = model.getMeasurePopulation();
        this.measurePopulationExclusions = model.getMeasurePopulationExclusions();
        this.measureObservations = model.getMeasureObservations();
        this.supplementalDataElements = model.getSupplementalData();
        this.measureSet = model.getGroupName();
        this.patientBased = model.isPatientBased();
        this.experimental = model.isExperimental();
    }

    public String getEcqmTitle() {
        return ecqmTitle;
    }

    public void setEcqmTitle(String ecqmTitle) {
        this.ecqmTitle = ecqmTitle;
    }

    public String getEcqmIdentifier() {
        return ecqmIdentifier;
    }

    public void setEcqmIdentifier(String ecqmIdentifier) {
        this.ecqmIdentifier = ecqmIdentifier;
    }

    public String getEcqmVersionNumber() {
        return ecqmVersionNumber;
    }

    public void setEcqmVersionNumber(String ecqmVersionNumber) {
        this.ecqmVersionNumber = ecqmVersionNumber;
    }

    public String getNqfNumber() {
        return nqfNumber;
    }

    public void setNqfNumber(String nafNumber) {
        this.nqfNumber = nafNumber;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getMeasurementPeriod() {
        return measurementPeriod;
    }

    public void setMeasurementPeriod(String measurementPeriod) {
        this.measurementPeriod = measurementPeriod;
    }

    public boolean getIsCalendarYear() {
        return isCalendarYear;
    }

    public void setIsCalendarYear(boolean isCalendarYear) {
        this.isCalendarYear = isCalendarYear;
    }

    public String getMeasurementPeriodStartDate() {
        return measurementPeriodStartDate;
    }

    public void setMeasurementPeriodStartDate(String measurementPeriodStartDate) {
        this.measurementPeriodStartDate = measurementPeriodStartDate;
    }

    public String getMeasurementPeriodEndDate() {
        return measurementPeriodEndDate;
    }

    public void setMeasurementPeriodEndDate(String measurementPeriodEndDate) {
        this.measurementPeriodEndDate = measurementPeriodEndDate;
    }

    public String getMeasureSteward() {
        return measureSteward;
    }

    public void setMeasureSteward(String measureSteward) {
        this.measureSteward = measureSteward;
    }

    public List<String> getMeasureDevelopers() {
        return measureDevelopers;
    }

    public void setMeasureDevelopers(List<String> measureDevelopers) {
        this.measureDevelopers = measureDevelopers;
    }

    public String getEndorsedBy() {
        return endorsedBy;
    }

    public void setEndorsedBy(String endorsedBy) {
        this.endorsedBy = endorsedBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public String getCompositeScoringMethod() {
        return compositeScoringMethod;
    }

    public void setCompositeScoringMethod(String compositeScoring) {
        this.compositeScoringMethod = compositeScoring;
    }

    public String getMeasureScoring() {
        return measureScoring;
    }

    public void setMeasureScoring(String measureScoring) {
        this.measureScoring = measureScoring;
    }

    public List<String> getMeasureTypes() {
        return measureTypes;
    }

    public void setMeasureTypes(List<String> measureTypes) {
        this.measureTypes = measureTypes;
    }

    public List<HumanReadableComponentMeasureModel> getComponentMeasures() {
        return componentMeasures;
    }

    public void setComponentMeasures(List<HumanReadableComponentMeasureModel> compositeMeasures) {
        this.componentMeasures = compositeMeasures;
    }

    public String getStratification() {
        return stratification;
    }

    public void setStratification(String stratification) {
        this.stratification = stratification;
    }

    public String getRiskAdjustment() {
        return riskAdjustment;
    }

    public void setRiskAdjustment(String riskAdjustment) {
        this.riskAdjustment = riskAdjustment;
    }

    public String getRateAggregation() {
        return rateAggregation;
    }

    public void setRateAggregation(String rateAggregation) {
        this.rateAggregation = rateAggregation;
    }

    public String getRationale() {
        return rationale;
    }

    public void setRationale(String rationale) {
        this.rationale = rationale;
    }

    public String getClinicalRecommendationStatement() {
        return clinicalRecommendationStatement;
    }

    public void setClinicalRecommendationStatement(String clinicalRecommendationStatement) {
        this.clinicalRecommendationStatement = clinicalRecommendationStatement;
    }

    public String getImprovementNotation() {
        return improvementNotation;
    }

    public void setImprovementNotation(String improvementNotation) {
        this.improvementNotation = improvementNotation;
    }

    public List<ReferenceTextAndType> getReferences() {
        return references;
    }

    public void setReferences(List<ReferenceTextAndType> references) {
        this.references = references;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getGuidance() {
        return guidance;
    }

    public void setGuidance(String guidance) {
        this.guidance = guidance;
    }

    public String getTransmissionFormat() {
        return transmissionFormat;
    }

    public void setTransmissionFormat(String transmissionFormat) {
        this.transmissionFormat = transmissionFormat;
    }

    public String getInitialPopulation() {
        return initialPopulation;
    }

    public void setInitialPopulation(String initialPopulation) {
        this.initialPopulation = initialPopulation;
    }

    public String getDenominator() {
        return denominator;
    }

    public void setDenominator(String denominator) {
        this.denominator = denominator;
    }

    public String getDenominatorExclusions() {
        return denominatorExclusions;
    }

    public void setDenominatorExclusions(String denominatorExclusions) {
        this.denominatorExclusions = denominatorExclusions;
    }

    public String getDenominatorExceptions() {
        return denominatorExceptions;
    }

    public void setDenominatorExceptions(String denominatorExceptions) {
        this.denominatorExceptions = denominatorExceptions;
    }

    public String getNumerator() {
        return numerator;
    }

    public void setNumerator(String numerator) {
        this.numerator = numerator;
    }

    public String getNumeratorExclusions() {
        return numeratorExclusions;
    }

    public void setNumeratorExclusions(String numeratorExclusions) {
        this.numeratorExclusions = numeratorExclusions;
    }

    public String getMeasurePopulation() {
        return measurePopulation;
    }

    public void setMeasurePopulation(String measurePopulation) {
        this.measurePopulation = measurePopulation;
    }

    public String getMeasurePopulationExclusions() {
        return measurePopulationExclusions;
    }

    public void setMeasurePopulationExclusions(String measurePopulationExclusions) {
        this.measurePopulationExclusions = measurePopulationExclusions;
    }

    public String getMeasureObservations() {
        return measureObservations;
    }

    public void setMeasureObservations(String measureObservations) {
        this.measureObservations = measureObservations;
    }

    public String getSupplementalDataElements() {
        return supplementalDataElements;
    }

    public void setSupplementalDataElements(String supplementalDataElements) {
        this.supplementalDataElements = supplementalDataElements;
    }

    public String getMeasureSet() {
        return measureSet;
    }

    public void setMeasureSet(String measureSet) {
        this.measureSet = measureSet;
    }

    public boolean getPatientBased() {
        return patientBased;
    }

    public void setPatientBased(boolean patientBased) {
        this.patientBased = patientBased;
    }

    public double getQdmVersion() {
        return qdmVersion;
    }

    public void setQdmVersion(double qdmVersion) {
        this.qdmVersion = qdmVersion;
    }

    public boolean isExperimental() {
        return experimental;
    }

    public void setExperimental(boolean experimental) {
        this.experimental = experimental;
    }
}
