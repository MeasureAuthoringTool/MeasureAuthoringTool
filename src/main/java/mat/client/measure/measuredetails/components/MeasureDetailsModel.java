package mat.client.measure.measuredetails.components;

public class MeasureDetailsModel implements MeasureDetailsComponentModel {
	private String id;
	private String measureId;
	private String revisionNumber;
	private String ownerUserId; 
	private boolean isComposite;
	private String scoringType;
	private GeneralInformationModel generalInformationModel;
	private MeasureStewardDeveloperModel measureStewardDeveloperModel;
	private DescriptionModel descriptionModel;
	private CopyrightModel copyrightModel;
	private DisclaimerModel disclaimerModel;
	private MeasureTypeModel measureTypeModeModel;
	private StratificationModel stratificationModel;
	private RiskAdjustmentModel riskAdjustmentModel;
	private RationaleModel rationaleModel;
	private RateAggregationModel rateAggregationModel;
	private ClinicalRecommendationModel clinicalRecommendationModel;
	private ImprovementNotationModel improvementNotationModel;
	private ReferencesModel referencesModel;
	private DefinitionModel definitionModel;
	private GuidanceModel guidanceModel;
	private TransmissionFormatModel transmissionFormatModel;
	private InitialPopulationModel initialPopulationModel;
	private MeasurePopulationModel measurePopulationModel;
	private MeasurePopulationExclusionsModel measurePopulationExclusionsModel;
	private DenominatorModel denominatorModel;
	private DenominatorExclusionsModel denominatorExclusionsModel;
	private NumeratorModel numeratorModel;
	private NumeratorExclusionsModel numeratorExclusionsModel;
	private DenominatorExceptionsModel denominatorExceptionsModel;
	private MeasureObservationsModel measureObservationsModel;
	private SupplementalDataElementsModel supplementalDataElementsModel;
	private MeasureSetModel measureSetModel;
	
	public MeasureDetailsModel() {
		this.ownerUserId = "";
		this.generalInformationModel = new GeneralInformationModel();
		this.measureStewardDeveloperModel = new MeasureStewardDeveloperModel();
		this.descriptionModel = new DescriptionModel();
		this.copyrightModel = new CopyrightModel();
		this.disclaimerModel = new DisclaimerModel();
		this.measureTypeModeModel = new MeasureTypeModel();
		this.stratificationModel = new StratificationModel();
		this.riskAdjustmentModel = new RiskAdjustmentModel();
		this.rationaleModel = new RationaleModel();
		this.rateAggregationModel = new RateAggregationModel();
		this.clinicalRecommendationModel = new ClinicalRecommendationModel();
		this.improvementNotationModel = new ImprovementNotationModel();
		this.referencesModel = new ReferencesModel();
		this.definitionModel = new DefinitionModel();
		this.guidanceModel = new GuidanceModel();
		this.transmissionFormatModel = new TransmissionFormatModel();
		this.initialPopulationModel = new InitialPopulationModel();
		this.measurePopulationModel = new MeasurePopulationModel();
		this.measurePopulationExclusionsModel = new MeasurePopulationExclusionsModel();
		this.denominatorModel = new DenominatorModel();
		this.denominatorExclusionsModel = new DenominatorExclusionsModel();
		this.numeratorModel = new NumeratorModel();
		this.numeratorExclusionsModel = new NumeratorExclusionsModel();
		this.denominatorExceptionsModel = new DenominatorExceptionsModel();
		this.measureObservationsModel = new MeasureObservationsModel();
		this.supplementalDataElementsModel = new SupplementalDataElementsModel();
		this.measureSetModel = new MeasureSetModel();
	}
	
	public GeneralInformationModel getGeneralInformationModel() {
		return generalInformationModel;
	}
	public void setGeneralInformationModel(GeneralInformationModel generalInformationModel) {
		this.generalInformationModel = generalInformationModel;
	}
	public MeasureStewardDeveloperModel getMeasureStewardDeveloperModel() {
		return measureStewardDeveloperModel;
	}
	public void setMeasureStewardDeveloperModel(MeasureStewardDeveloperModel measureStewardDeveloperModel) {
		this.measureStewardDeveloperModel = measureStewardDeveloperModel;
	}
	public DescriptionModel getDescriptionModel() {
		return descriptionModel;
	}
	public void setDescriptionModel(DescriptionModel descriptionModel) {
		this.descriptionModel = descriptionModel;
	}
	public CopyrightModel getCopyrightModel() {
		return copyrightModel;
	}
	public void setCopyrightModel(CopyrightModel copyrightModel) {
		this.copyrightModel = copyrightModel;
	}
	public DisclaimerModel getDisclaimerModel() {
		return disclaimerModel;
	}
	public void setDisclaimerModel(DisclaimerModel disclaimerModel) {
		this.disclaimerModel = disclaimerModel;
	}
	public MeasureTypeModel getMeasureTypeModeModel() {
		return measureTypeModeModel;
	}
	public void setMeasureTypeModeModel(MeasureTypeModel measureTypeModeModel) {
		this.measureTypeModeModel = measureTypeModeModel;
	}
	public StratificationModel getStratificationModel() {
		return stratificationModel;
	}
	public void setStratificationModel(StratificationModel stratificationModel) {
		this.stratificationModel = stratificationModel;
	}
	public RiskAdjustmentModel getRiskAdjustmentModel() {
		return riskAdjustmentModel;
	}
	public void setRiskAdjustmentModel(RiskAdjustmentModel riskAdjustmentModel) {
		this.riskAdjustmentModel = riskAdjustmentModel;
	}
	public RationaleModel getRationaleModel() {
		return rationaleModel;
	}
	public void setRationaleModel(RationaleModel rationaleModel) {
		this.rationaleModel = rationaleModel;
	}
	public RateAggregationModel getRateAggregationModel() {
		return rateAggregationModel;
	}
	public void setRateAggregationModel(RateAggregationModel rateAggregationModel) {
		this.rateAggregationModel = rateAggregationModel;
	}
	public ClinicalRecommendationModel getClinicalRecommendationModel() {
		return clinicalRecommendationModel;
	}
	public void setClinicalRecommendationModel(ClinicalRecommendationModel clinicalRecommendationModel) {
		this.clinicalRecommendationModel = clinicalRecommendationModel;
	}
	public ImprovementNotationModel getImprovementNotationModel() {
		return improvementNotationModel;
	}
	public void setImprovementNotationModel(ImprovementNotationModel improvementNotationModel) {
		this.improvementNotationModel = improvementNotationModel;
	}
	public ReferencesModel getReferencesModel() {
		return referencesModel;
	}
	public void setReferencesModel(ReferencesModel referencesModel) {
		this.referencesModel = referencesModel;
	}
	public DefinitionModel getDefinitionModel() {
		return definitionModel;
	}
	public void setDefinitionModel(DefinitionModel definitionModel) {
		this.definitionModel = definitionModel;
	}
	public GuidanceModel getGuidanceModel() {
		return guidanceModel;
	}
	public void setGuidanceModel(GuidanceModel guidanceModel) {
		this.guidanceModel = guidanceModel;
	}
	public TransmissionFormatModel getTransmissionFormatModel() {
		return transmissionFormatModel;
	}
	public void setTransmissionFormatModel(TransmissionFormatModel transmissionFormatModel) {
		this.transmissionFormatModel = transmissionFormatModel;
	}
	public InitialPopulationModel getInitialPopulationModel() {
		return initialPopulationModel;
	}
	public void setInitialPopulationModel(InitialPopulationModel initialPopulationModel) {
		this.initialPopulationModel = initialPopulationModel;
	}
	public MeasurePopulationModel getMeasurePopulationModel() {
		return measurePopulationModel;
	}
	public void setMeasurePopulationModel(MeasurePopulationModel measurePopulationModel) {
		this.measurePopulationModel = measurePopulationModel;
	}
	public MeasurePopulationExclusionsModel getMeasurePopulationExclusionsModel() {
		return measurePopulationExclusionsModel;
	}
	public void setMeasurePopulationExclusionsModel(MeasurePopulationExclusionsModel measurePopulationExclusionsModel) {
		this.measurePopulationExclusionsModel = measurePopulationExclusionsModel;
	}
	public DenominatorModel getDenominatorModel() {
		return denominatorModel;
	}
	public void setDenominatorModel(DenominatorModel denominatorModel) {
		this.denominatorModel = denominatorModel;
	}
	public DenominatorExclusionsModel getDenominatorExclusionsModel() {
		return denominatorExclusionsModel;
	}
	public void setDenominatorExclusionsModel(DenominatorExclusionsModel denominatorExclusionsModel) {
		this.denominatorExclusionsModel = denominatorExclusionsModel;
	}
	public NumeratorModel getNumeratorModel() {
		return numeratorModel;
	}
	public void setNumeratorModel(NumeratorModel numeratorModel) {
		this.numeratorModel = numeratorModel;
	}
	public NumeratorExclusionsModel getNumeratorExclusionsModel() {
		return numeratorExclusionsModel;
	}
	public void setNumeratorExclusionsModel(NumeratorExclusionsModel numeratorExclusionsModel) {
		this.numeratorExclusionsModel = numeratorExclusionsModel;
	}
	public DenominatorExceptionsModel getDenominatorExceptionsModel() {
		return denominatorExceptionsModel;
	}
	public void setDenominatorExceptionsModel(DenominatorExceptionsModel denominatorExceptionsModel) {
		this.denominatorExceptionsModel = denominatorExceptionsModel;
	}
	public SupplementalDataElementsModel getSupplementalDataElementsModel() {
		return supplementalDataElementsModel;
	}
	public void setSupplementalDataElementsModel(SupplementalDataElementsModel supplementalDataElementsModel) {
		this.supplementalDataElementsModel = supplementalDataElementsModel;
	}
	public MeasureObservationsModel getMeasureObservationsModel() {
		return measureObservationsModel;
	}
	public void setMeasureObservationsModel(MeasureObservationsModel measureObservationsModel) {
		this.measureObservationsModel = measureObservationsModel;
	}
	public MeasureSetModel getMeasureSetModel() {
		return measureSetModel;
	}
	public void setMeasureSetModel(MeasureSetModel measureSetModel) {
		this.measureSetModel = measureSetModel;
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
			setClinicalRecommendationModel((ClinicalRecommendationModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof CopyrightModel) {
			setCopyrightModel((CopyrightModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof DefinitionModel) {
			setDefinitionModel((DefinitionModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof DenominatorExceptionsModel) {
			setDenominatorExceptionsModel((DenominatorExceptionsModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof DenominatorExclusionsModel) {
			setDenominatorExclusionsModel((DenominatorExclusionsModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof DenominatorModel) {
			setDenominatorModel((DenominatorModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof DescriptionModel) {
			setDescriptionModel((DescriptionModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof DisclaimerModel) {
			setDisclaimerModel((DisclaimerModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof GeneralInformationModel) {
			setGeneralInformationModel((GeneralInformationModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof GuidanceModel) {
			setGuidanceModel((GuidanceModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof ImprovementNotationModel) {
			setImprovementNotationModel((ImprovementNotationModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof InitialPopulationModel) {
			setInitialPopulationModel((InitialPopulationModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof MeasureObservationsModel) {
			setMeasureObservationsModel((MeasureObservationsModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof MeasurePopulationExclusionsModel) {
			setMeasurePopulationExclusionsModel((MeasurePopulationExclusionsModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof MeasurePopulationModel) {
			setMeasurePopulationModel((MeasurePopulationModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof MeasureSetModel) {
			setMeasureSetModel((MeasureSetModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof MeasureStewardDeveloperModel) {
			setMeasureStewardDeveloperModel((MeasureStewardDeveloperModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof MeasureTypeModel) {
			setMeasureTypeModeModel((MeasureTypeModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof NumeratorExclusionsModel) {
			setNumeratorExclusionsModel((NumeratorExclusionsModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof NumeratorModel) {
			setNumeratorModel((NumeratorModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof RateAggregationModel) {
			setRateAggregationModel((RateAggregationModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof RationaleModel) {
			setRationaleModel((RationaleModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof ReferencesModel) {
			setReferencesModel((ReferencesModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof RiskAdjustmentModel) {
			setRiskAdjustmentModel((RiskAdjustmentModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof StratificationModel) {
			setStratificationModel((StratificationModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof SupplementalDataElementsModel) {
			setSupplementalDataElementsModel((SupplementalDataElementsModel) measureDetailsComponentModel);
		} else if(measureDetailsComponentModel instanceof TransmissionFormatModel) {
			setTransmissionFormatModel((TransmissionFormatModel) measureDetailsComponentModel);
		}
	}
}
