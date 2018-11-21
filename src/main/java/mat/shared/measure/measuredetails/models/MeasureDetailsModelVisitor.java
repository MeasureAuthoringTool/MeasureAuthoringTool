package mat.shared.measure.measuredetails.models;

public interface MeasureDetailsModelVisitor {
	public void visit(ClinicalRecommendationModel clinicalRecommendationModel);
	public void visit(CopyrightModel copyrightModel);
	public void visit(DefinitionModel definitionModel);
	public void visit(DescriptionModel descriptionModel);
	public void visit(DenominatorExceptionsModel denominatorExceptionsModel);
	public void visit(DenominatorExclusionsModel denominatorExclusionsModel);
	public void visit(DenominatorModel denominatorModel);
	public void visit(GeneralInformationModel generalInformationModel);
	public void visit(GuidanceModel guidanceModel);
	public void visit(ImprovementNotationModel improvementNotationModel);
	public void visit(InitialPopulationModel initialPopulationModel);
	public void visit(MeasureObservationsModel measureObservationsModel);
	public void visit(MeasurePopulationExclusionsModel measurePopulationExclusionsModel);
	public void visit(MeasurePopulationModel measurePopulationModel);
	public void visit(MeasureSetModel measureSetModel);
	public void visit(MeasureStewardDeveloperModel measureStewardDeveloperModel);
	public void visit(MeasureTypeModel measureTypeModel);
	public void visit(NumeratorExclusionsModel numeratorExclusionsModel);
	public void visit(NumeratorModel numeratorModel);
	public void visit(RateAggregationModel rateAggregationModel);
	public void visit(RationaleModel rationaleModel);
	public void visit(ReferencesModel referencesModel);
	public void visit(RiskAdjustmentModel riskAdjustmentModel);
	public void visit(StratificationModel stratificationModel);
	public void visit(SupplementalDataElementsModel supplementalDataElementsModel);
	public void visit(TransmissionFormatModel transmissionFormatModel);
	public void visit(DisclaimerModel disclaimerModel);
}


