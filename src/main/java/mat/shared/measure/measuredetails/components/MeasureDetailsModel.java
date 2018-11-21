package mat.shared.measure.measuredetails.components;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MeasureDetailsModel implements MeasureDetailsComponentModel, MeasureDetailsModelVisitor, IsSerializable  {
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


	@Override
	public void accept(MeasureDetailsModelVisitor measureDetailsModelVisitor) {

	}
	
	@Override
	public void visit(ClinicalRecommendationModel clinicalRecommendationModel) {
		setClinicalRecommendationModel(clinicalRecommendationModel);
	}

	@Override
	public void visit(CopyrightModel copyrightModel) {
		setCopyrightModel(copyrightModel);
	}

	@Override
	public void visit(DefinitionModel definitionModel) {
		setDefinitionModel(definitionModel);
	}

	@Override
	public void visit(DenominatorExceptionsModel denominatorExceptionsModel) {
		setDenominatorExceptionsModel(denominatorExceptionsModel);
	}

	@Override
	public void visit(DenominatorExclusionsModel denominatorExclusionsModel) {
		setDenominatorExclusionsModel(denominatorExclusionsModel);
	}

	@Override
	public void visit(DenominatorModel denominatorModel) {
		setDenominatorModel(denominatorModel);
	}
	
	@Override
	public void visit(DescriptionModel descriptionModel) {
		setDescriptionModel(descriptionModel);
	}

	@Override
	public void visit(DisclaimerModel disclaimerModel) {
		setDisclaimerModel(disclaimerModel);
	}
	
	@Override
	public void visit(GeneralInformationModel generalInformationModel) {
		setGeneralInformationModel(generalInformationModel);
	}

	@Override
	public void visit(GuidanceModel guidanceModel) {
		setGuidanceModel(guidanceModel);
	}

	@Override
	public void visit(ImprovementNotationModel improvementNotationModel) {
		setImprovementNotationModel(improvementNotationModel);
	}

	@Override
	public void visit(InitialPopulationModel initialPopulationModel) {
		setInitialPopulationModel(initialPopulationModel);
	}

	@Override
	public void visit(MeasureObservationsModel measureObservationsModel) {
		setMeasureObservationsModel(measureObservationsModel);
	}

	@Override
	public void visit(MeasurePopulationExclusionsModel measurePopulationExclusionsModel) {
		setMeasurePopulationExclusionsModel(measurePopulationExclusionsModel);
	}

	@Override
	public void visit(MeasurePopulationModel measurePopulationModel) {
		setMeasurePopulationModel(measurePopulationModel);
	}

	@Override
	public void visit(MeasureSetModel measureSetModel) {
		setMeasureSetModel(measureSetModel);
	}

	@Override
	public void visit(MeasureStewardDeveloperModel measureStewardDeveloperModel) {
		setMeasureStewardDeveloperModel(measureStewardDeveloperModel);
	}

	@Override
	public void visit(MeasureTypeModel measureTypeModel) {
		setMeasureTypeModeModel(measureTypeModel);
	}

	@Override
	public void visit(NumeratorExclusionsModel numeratorExclusionsModel) {
		setNumeratorExclusionsModel(numeratorExclusionsModel);
	}

	@Override
	public void visit(NumeratorModel numeratorModel) {
		setNumeratorModel(numeratorModel);
	}

	@Override
	public void visit(RateAggregationModel rateAggregationModel) {
		setRateAggregationModel(rateAggregationModel);
	}

	@Override
	public void visit(RationaleModel rationaleModel) {
		setRationaleModel(rationaleModel);
	}

	@Override
	public void visit(ReferencesModel referencesModel) {
		setReferencesModel(referencesModel);
	}

	@Override
	public void visit(RiskAdjustmentModel riskAdjustmentModel) {
		setRiskAdjustmentModel(riskAdjustmentModel);
	}

	@Override
	public void visit(StratificationModel stratificationModel) {
		setStratificationModel(stratificationModel);
	}

	@Override
	public void visit(SupplementalDataElementsModel supplementalDataElementsModel) {
		setSupplementalDataElementsModel(supplementalDataElementsModel);
	}

	@Override
	public void visit(TransmissionFormatModel transmissionFormatModel) {
		setTransmissionFormatModel(transmissionFormatModel);
	}
}
