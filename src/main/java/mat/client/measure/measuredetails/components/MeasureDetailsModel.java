package mat.client.measure.measuredetails.components;

public class MeasureDetailsModel implements MeasureDetailsComponentModel {
	private String id;
	private String measureId;
	private String revisionNumber;
	private String ownerUserId; 
	private boolean isComposite;
	private String scoringType;
	private GeneralInformationModel generalInformation;
	private MeasureStewardDeveloperModel measureStewardDeveloper;
	private DescriptionModel description;
	private CopyrightModel copyright;
	private DisclaimerModel disclaimer;
	private MeasureTypeModel measureTypeMode;
	private StratificationModel stratification;
	private RiskAdjustmentModel riskAdjustment;
	private RationaleModel rationale;
	private RateAggregationModel rateAggregation;
	private ClinicalRecommendationModel clinicalRecommendation;
	private ImprovementNotationModel improvementNotation;
	private ReferencesModel references;
	private DefinitionModel definition;
	private GuidanceModel guidance;
	private TransmissionFormatModel transmissionFormat;
	private InitialPopulationModel initialPopulation;
	private MeasurePopulationModel measurePopulation;
	private MeasurePopulationExclusionsModel measurePopulationExclusions;
	private DenominatorModel denominator;
	private DenominatorExclusionsModel denominatorExclusions;
	private NumeratorModel numerator;
	private NumeratorExclusionsModel numeratorExclusions;
	private DenominatorExceptionsModel denominatorExceptions;
	private MeasureObservationsModel measureObservations;
	private SupplementalDataElementsModel supplementalDataElements;
	private MeasureSetModel measureSet;
	
	public MeasureDetailsModel() {
		this.ownerUserId = "";
		this.generalInformation = new GeneralInformationModel();
		this.measureStewardDeveloper = new MeasureStewardDeveloperModel();
		this.description = new DescriptionModel();
		this.copyright = new CopyrightModel();
		this.disclaimer = new DisclaimerModel();
		this.measureTypeMode = new MeasureTypeModel();
		this.stratification = new StratificationModel();
		this.riskAdjustment = new RiskAdjustmentModel();
		this.rationale = new RationaleModel();
		this.rateAggregation = new RateAggregationModel();
		this.clinicalRecommendation = new ClinicalRecommendationModel();
		this.improvementNotation = new ImprovementNotationModel();
		this.references = new ReferencesModel();
		this.definition = new DefinitionModel();
		this.guidance = new GuidanceModel();
		this.transmissionFormat = new TransmissionFormatModel();
		this.initialPopulation = new InitialPopulationModel();
		this.measurePopulation = new MeasurePopulationModel();
		this.measurePopulationExclusions = new MeasurePopulationExclusionsModel();
		this.denominator = new DenominatorModel();
		this.denominatorExclusions = new DenominatorExclusionsModel();
		this.numerator = new NumeratorModel();
		this.numeratorExclusions = new NumeratorExclusionsModel();
		this.denominatorExceptions = new DenominatorExceptionsModel();
		this.measureObservations = new MeasureObservationsModel();
		this.supplementalDataElements = new SupplementalDataElementsModel();
		this.measureSet = new MeasureSetModel();
	}
	
	public GeneralInformationModel getGeneralInformation() {
		return generalInformation;
	}
	public void setGeneralInformation(GeneralInformationModel generalInformation) {
		this.generalInformation = generalInformation;
	}
	public MeasureStewardDeveloperModel getMeasureStewardDeveloper() {
		return measureStewardDeveloper;
	}
	public void setMeasureStewardDeveloper(MeasureStewardDeveloperModel measureStewardDeveloper) {
		this.measureStewardDeveloper = measureStewardDeveloper;
	}
	public DescriptionModel getDescription() {
		return description;
	}
	public void setDescription(DescriptionModel description) {
		this.description = description;
	}
	public CopyrightModel getCopyright() {
		return copyright;
	}
	public void setCopyright(CopyrightModel copyright) {
		this.copyright = copyright;
	}
	public DisclaimerModel getDisclaimer() {
		return disclaimer;
	}
	public void setDisclaimer(DisclaimerModel disclaimer) {
		this.disclaimer = disclaimer;
	}
	public MeasureTypeModel getMeasureTypeMode() {
		return measureTypeMode;
	}
	public void setMeasureTypeModel(MeasureTypeModel measureTypeMode) {
		this.measureTypeMode = measureTypeMode;
	}
	public StratificationModel getStratification() {
		return stratification;
	}
	public void setStratification(StratificationModel stratification) {
		this.stratification = stratification;
	}
	public RiskAdjustmentModel getRiskAdjustment() {
		return riskAdjustment;
	}
	public void setRiskAdjustment(RiskAdjustmentModel riskAdjustment) {
		this.riskAdjustment = riskAdjustment;
	}
	public RationaleModel getRationale() {
		return rationale;
	}
	public void setRationale(RationaleModel rationale) {
		this.rationale = rationale;
	}
	public RateAggregationModel getRateAggregation() {
		return rateAggregation;
	}
	public void setRateAggregation(RateAggregationModel rateAggregation) {
		this.rateAggregation = rateAggregation;
	}
	public ClinicalRecommendationModel getClinicalRecommendation() {
		return clinicalRecommendation;
	}
	public void setClinicalRecommendation(ClinicalRecommendationModel clinicalRecommendation) {
		this.clinicalRecommendation = clinicalRecommendation;
	}
	public ImprovementNotationModel getImprovementNotation() {
		return improvementNotation;
	}
	public void setImprovementNotation(ImprovementNotationModel improvementNotation) {
		this.improvementNotation = improvementNotation;
	}
	public ReferencesModel getReferences() {
		return references;
	}
	public void setReferences(ReferencesModel references) {
		this.references = references;
	}
	public DefinitionModel getDefinition() {
		return definition;
	}
	public void setDefinition(DefinitionModel definition) {
		this.definition = definition;
	}
	public GuidanceModel getGuidance() {
		return guidance;
	}
	public void setGuidance(GuidanceModel guidance) {
		this.guidance = guidance;
	}
	public TransmissionFormatModel getTransmissionFormat() {
		return transmissionFormat;
	}
	public void setTransmissionFormat(TransmissionFormatModel transmissionFormat) {
		this.transmissionFormat = transmissionFormat;
	}
	public InitialPopulationModel getInitialPopulation() {
		return initialPopulation;
	}
	public void setInitialPopulation(InitialPopulationModel initialPopulation) {
		this.initialPopulation = initialPopulation;
	}
	public MeasurePopulationModel getMeasurePopulation() {
		return measurePopulation;
	}
	public void setMeasurePopulation(MeasurePopulationModel measurePopulation) {
		this.measurePopulation = measurePopulation;
	}
	public MeasurePopulationExclusionsModel getMeasurePopulationExclusions() {
		return measurePopulationExclusions;
	}
	public void setMeasurePopulationExclusions(MeasurePopulationExclusionsModel measurePopulationExclusions) {
		this.measurePopulationExclusions = measurePopulationExclusions;
	}
	public DenominatorModel getDenominator() {
		return denominator;
	}
	public void setDenominator(DenominatorModel denominator) {
		this.denominator = denominator;
	}
	public DenominatorExclusionsModel getDenominatorExclusions() {
		return denominatorExclusions;
	}
	public void setDenominatorExclusions(DenominatorExclusionsModel denominatorExclusions) {
		this.denominatorExclusions = denominatorExclusions;
	}
	public NumeratorModel getNumerator() {
		return numerator;
	}
	public void setNumerator(NumeratorModel numerator) {
		this.numerator = numerator;
	}
	public NumeratorExclusionsModel getNumeratorExclusions() {
		return numeratorExclusions;
	}
	public void setNumeratorExclusions(NumeratorExclusionsModel numeratorExclusions) {
		this.numeratorExclusions = numeratorExclusions;
	}
	public DenominatorExceptionsModel getDenominatorExceptions() {
		return denominatorExceptions;
	}
	public void setDenominatorExceptions(DenominatorExceptionsModel denominatorExceptions) {
		this.denominatorExceptions = denominatorExceptions;
	}
	public SupplementalDataElementsModel getSupplementalDataElements() {
		return supplementalDataElements;
	}
	public void setSupplementalDataElements(SupplementalDataElementsModel supplementalDataElements) {
		this.supplementalDataElements = supplementalDataElements;
	}
	public MeasureObservationsModel getMeasureObservations() {
		return measureObservations;
	}
	public void setMeasureObservations(MeasureObservationsModel measureObservations) {
		this.measureObservations = measureObservations;
	}
	public MeasureSetModel getMeasureSet() {
		return measureSet;
	}
	public void setMeasureSet(MeasureSetModel measureSet) {
		this.measureSet = measureSet;
	}

	@Override
	public boolean equals(MeasureDetailsComponentModel model) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getOwnerUserId() {
		return ownerUserId;
	}

	public void setOwnerUserId(String ownerUserId) {
		this.ownerUserId = ownerUserId;
	}
	
	public boolean isComposite() {
		return isComposite;
	}

	public void setComposite(boolean isComposite) {
		this.isComposite = isComposite;
	}
	public String getScoringType() {
		return scoringType;
	}

	public void setScoringType(String scoringType) {
		this.scoringType = scoringType;
	}
	public String getMeasureId() {
		return measureId;
	}
	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}
	public String getRevisionNumber() {
		return revisionNumber;
	}

	public void setRevisionNumber(String revisionNumber) {
		this.revisionNumber = revisionNumber;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public void updateModel(MeasureDetailsComponentModel measureDetailsComponentModel) {
		if(measureDetailsComponentModel instanceof ClinicalRecommendationModel) {
			setClinicalRecommendation((ClinicalRecommendationModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof CopyrightModel) {
			setCopyright((CopyrightModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof DefinitionModel) {
			setDefinition((DefinitionModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof DenominatorExceptionsModel) {
			setDenominatorExceptions((DenominatorExceptionsModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof DenominatorExclusionsModel) {
			setDenominatorExclusions((DenominatorExclusionsModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof DenominatorModel) {
			setDenominator((DenominatorModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof DescriptionModel) {
			setDescription((DescriptionModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof DisclaimerModel) {
			setDisclaimer((DisclaimerModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof GeneralInformationModel) {
			setGeneralInformation((GeneralInformationModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof GuidanceModel) {
			setGuidance((GuidanceModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof ImprovementNotationModel) {
			setImprovementNotation((ImprovementNotationModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof InitialPopulationModel) {
			setInitialPopulation((InitialPopulationModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof MeasureObservationsModel) {
			setMeasureObservations((MeasureObservationsModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof MeasurePopulationExclusionsModel) {
			setMeasurePopulationExclusions((MeasurePopulationExclusionsModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof MeasurePopulationModel) {
			setMeasurePopulation((MeasurePopulationModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof MeasureSetModel) {
			setMeasureSet((MeasureSetModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof MeasureStewardDeveloperModel) {
			setMeasureStewardDeveloper((MeasureStewardDeveloperModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof MeasureTypeModel) {
			setMeasureTypeModel((MeasureTypeModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof NumeratorExclusionsModel) {
			setNumeratorExclusions((NumeratorExclusionsModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof NumeratorModel) {
			setNumerator((NumeratorModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof RateAggregationModel) {
			setRateAggregation((RateAggregationModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof RationaleModel) {
			setRationale((RationaleModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof ReferencesModel) {
			setReferences((ReferencesModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof RiskAdjustmentModel) {
			setRiskAdjustment((RiskAdjustmentModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof StratificationModel) {
			setStratification((StratificationModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof SupplementalDataElementsModel) {
			setSupplementalDataElements((SupplementalDataElementsModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof TransmissionFormatModel) {
			setTransmissionFormat((TransmissionFormatModel) measureDetailsComponentModel);
		}
	}
}
