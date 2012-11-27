package org.ifmc.mat.simplexml.model;


public class Headers {
	private Uuid uuid;
	private Title title;
	private Period period;
	private Description description;
	private Copyright copyright;
	private Setid setid;
	private Version version;
	private Status status;
	private Authors authors;
	private Custodian custodian;
	private Verifiers verifiers;
	private Scores scores;
	private Types types;
	private StratificationDescription stratification;
	private Guidance guidance;
	private TransmissionFormat  transmissionFormat;
	private Rationales rationales;
	private Recommendations recommendations;
	private References references;
	private ImprovementNotations improvementNotation;
	private Definitions definitions;
	private QualityMeasureSet  qualityMeasureSet; 
	private String  ttext;
	private FinalizedDate finalizedDate;
	private SupplementalData supplementalData;
	private Disclaimer disclaimer;
	private RiskAdjustment riskAdjustment;
	private RateAggregation aggregation;
	private InitialPatientPopDescription initialPatientPopDescription;
	private DenominatorExclusionsDescription denominatorExclusionsDescription;
	private NumeratorDescription numeratorDescription;
	private NumeratorExclusionsDescription numeratorExclusionsDescription;
	private DenominatorExceptionsDescription denominatorExceptionsDescription;
	private MeasurePopulationDescription measurePopulationDescription;
	private MeasureObservationsDescription measureObservationsDescription;
	private DenominatorDescription denominatorDescription;
	private NqfId nqfid;
	private Emeasureid emeasureid;
	

	public DenominatorDescription getDenominatorDescription() {
		return denominatorDescription;
	}
	public void setDenominatorDescription(
			DenominatorDescription denominatorDescription) {
		this.denominatorDescription = denominatorDescription;
	}
	public InitialPatientPopDescription getInitialPatientPopDescription() {
		return initialPatientPopDescription;
	}
	public void setInitialPatientPopDescription(
			InitialPatientPopDescription initialPatientPopDescription) {
		this.initialPatientPopDescription = initialPatientPopDescription;
	}
	public DenominatorExclusionsDescription getDenominatorExclusionsDescription() {
		return denominatorExclusionsDescription;
	}
	public void setDenominatorExclusionsDescription(
			DenominatorExclusionsDescription denominatorExclusionsDescription) {
		this.denominatorExclusionsDescription = denominatorExclusionsDescription;
	}
	public NumeratorDescription getNumeratorDescription() {
		return numeratorDescription;
	}
	public void setNumeratorDescription(NumeratorDescription numeratorDescription) {
		this.numeratorDescription = numeratorDescription;
	}
	public NumeratorExclusionsDescription getNumeratorExclusionsDescription() {
		return numeratorExclusionsDescription;
	}
	public void setNumeratorExclusionsDescription(
			NumeratorExclusionsDescription numeratorExclusionsDescription) {
		this.numeratorExclusionsDescription = numeratorExclusionsDescription;
	}
	public DenominatorExceptionsDescription getDenominatorExceptionsDescription() {
		return denominatorExceptionsDescription;
	}
	public void setDenominatorExceptionsDescription(
			DenominatorExceptionsDescription denominatorExceptionsDescription) {
		this.denominatorExceptionsDescription = denominatorExceptionsDescription;
	}
	public MeasurePopulationDescription getMeasurePopulationDescription() {
		return measurePopulationDescription;
	}
	public void setMeasurePopulationDescription(
			MeasurePopulationDescription measurePopulationDescription) {
		this.measurePopulationDescription = measurePopulationDescription;
	}
	public MeasureObservationsDescription getMeasureObservationsDescription() {
		return measureObservationsDescription;
	}
	public void setMeasureObservationsDescription(
			MeasureObservationsDescription measureObservationsDescription) {
		this.measureObservationsDescription = measureObservationsDescription;
	}
	public Guidance getGuidance() {
		return guidance;
	}
	public void setGuidance (Guidance guidance ) {
		this.guidance = guidance;
	}
	public Rationales getRationales() {
		return rationales;
	}
	public void setRationales (Rationales rationales ) {
		this.rationales = rationales;
	}
	public Verifiers getVerifiers() {
		return verifiers;
	}
	public void setVerifiers (Verifiers verifiers ) {
		this.verifiers = verifiers;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus (Status status ) {
		this.status = status;
	}
	public String getTtext() {
		return ttext;
	}
	public void setTtext (String ttext ) {
		this.ttext = ttext;
	}
	public Period getPeriod() {
		return period;
	}
	public void setPeriod (Period period ) {
		this.period = period;
	}
	public Version getVersion() {
		return version;
	}
	public void setVersion (Version version ) {
		this.version = version;
	}
	public Authors getAuthors() {
		return authors;
	}
	public void setAuthors (Authors authors ) {
		this.authors = authors;
	}
	public References getReferences() {
		if(references == null)
			this.references = new References();
		return references;
	}
	public void setReferences (References references ) {
		this.references = references;
	}
	public Title getTitle() {
		return title;
	}
	public void setTitle (Title title ) {
		this.title = title;
	}
	public Custodian getCustodian() {
		return custodian;
	}
	public void setCustodian (Custodian custodian ) {
		this.custodian = custodian;
	}
	public Scores getScores() {
		return scores;
	}
	public void setScores (Scores scores ) {
		this.scores = scores;
	}
	public StratificationDescription getStratification() {
		return stratification;
	}
	public void setStratification (StratificationDescription stratification ) {
		this.stratification = stratification;
	}
	public Description getDescription() {
		return description;
	}
	public void setDescription (Description description ) {
		this.description = description;
	}
	
	

	
	
	public Recommendations getRecommendations() {
		return recommendations;
	}
	public void setRecommendations (Recommendations recommendations ) {
		this.recommendations = recommendations;
	}
	public Setid getSetid() {
		return setid;
	}
	public void setSetid (Setid setid ) {
		this.setid = setid;
	}
	public Uuid getUuid() {
		return uuid;
	}
	public void setUuid (Uuid uuid ) {
		this.uuid = uuid;
	}
	public Types getTypes() {
		return types;
	}
	public void setTypes (Types types ) {
		this.types = types;
	}
	public ImprovementNotations getImprovementNotation() {
		return improvementNotation;
	}
	public void setImprovementNotation(ImprovementNotations improvementNotation) {
		this.improvementNotation = improvementNotation;
	}
	public Definitions getDefinitions() {
		return definitions;
	}
	public void setDefinitions(Definitions definitions) {
		this.definitions = definitions;
	}
	
	public QualityMeasureSet getQualityMeasureSet() {
		return qualityMeasureSet;
	}
	public void setQualityMeasureSet(QualityMeasureSet qualityMeasureSet) {
		this.qualityMeasureSet = qualityMeasureSet;
	}
	public FinalizedDate getFinalizedDate() {
		return finalizedDate;
	}
	public void setFinalizedDate(FinalizedDate finalizedDate) {
		this.finalizedDate = finalizedDate;
	}
	public Copyright getCopyright() {
		return copyright;
	}
	public void setCopyright(Copyright copyright) {
		this.copyright = copyright;
	}
	public RiskAdjustment getRiskAdjustment() {
		return riskAdjustment;
	}
	public void setRiskAdjustment(RiskAdjustment riskAdjustment) {
		this.riskAdjustment = riskAdjustment;
	}
	public TransmissionFormat getTransmissionFormat() {
		return transmissionFormat;
	}
	public void setTransmissionFormat(TransmissionFormat transmissionFormat) {
		this.transmissionFormat = transmissionFormat;
	}
	public SupplementalData getSupplementalData() {
		return supplementalData;
	}
	public void setSupplementalData(SupplementalData supplementalData) {
		this.supplementalData = supplementalData;
	}
	public void setDisclaimer(Disclaimer disclaimer) {
		this.disclaimer = disclaimer;
	}
	public Disclaimer getDisclaimer() {
		return disclaimer;
	}
	public void setRateAggregation(RateAggregation rateAggregation) {
		this.aggregation = rateAggregation;
	}
	public RateAggregation getRateAggregation() {
		return aggregation;
	}
	public NqfId getNqfid() {
		return nqfid;
	}
	public void setNqfid(NqfId nqfid) {
		this.nqfid = nqfid;
	}
	public Emeasureid getEmeasureid() {
		return emeasureid;
	}
	public void setEmeasureid(Emeasureid emeasureid) {
		if(!"0".equals(emeasureid.getTtext()))
			this.emeasureid = emeasureid;
	}
	
	
}