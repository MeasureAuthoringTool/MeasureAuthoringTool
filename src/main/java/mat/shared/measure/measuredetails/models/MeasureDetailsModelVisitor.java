package mat.shared.measure.measuredetails.models;

import java.util.List;

public interface MeasureDetailsModelVisitor {
	public void updateModel(ClinicalRecommendationModel clinicalRecommendationModel);
	public void updateModel(CopyrightModel copyrightModel);
	public void updateModel(DefinitionModel definitionModel);
	public void updateModel(DescriptionModel descriptionModel);
	public void updateModel(DenominatorExceptionsModel denominatorExceptionsModel);
	public void updateModel(DenominatorExclusionsModel denominatorExclusionsModel);
	public void updateModel(DenominatorModel denominatorModel);
	public void updateModel(GeneralInformationModel generalInformationModel);
	public void updateModel(GuidanceModel guidanceModel);
	public void updateModel(ImprovementNotationModel improvementNotationModel);
	public void updateModel(InitialPopulationModel initialPopulationModel);
	public void updateModel(MeasureObservationsModel measureObservationsModel);
	public void updateModel(MeasurePopulationExclusionsModel measurePopulationExclusionsModel);
	public void updateModel(MeasurePopulationModel measurePopulationModel);
	public void updateModel(MeasureSetModel measureSetModel);
	public void updateModel(MeasureStewardDeveloperModel measureStewardDeveloperModel);
	public void updateModel(MeasureTypeModel measureTypeModel);
	public void updateModel(NumeratorExclusionsModel numeratorExclusionsModel);
	public void updateModel(NumeratorModel numeratorModel);
	public void updateModel(RateAggregationModel rateAggregationModel);
	public void updateModel(RationaleModel rationaleModel);
	public void updateModel(ReferencesModel referencesModel);
	public void updateModel(RiskAdjustmentModel riskAdjustmentModel);
	public void updateModel(StratificationModel stratificationModel);
	public void updateModel(SupplementalDataElementsModel supplementalDataElementsModel);
	public void updatemodel(TransmissionFormatModel transmissionFormatModel);
	public void updateModel(DisclaimerModel disclaimerModel);
	public List<String> validateModel(ClinicalRecommendationModel clinicalRecommendationModel);
	public List<String> validateModel(CopyrightModel copyrightModel);
	public List<String> validateModel(DefinitionModel definitionModel);
	public List<String> validateModel(DescriptionModel descriptionModel);
	public List<String> validateModel(DenominatorExceptionsModel denominatorExceptionsModel);
	public List<String> validateModel(DenominatorExclusionsModel denominatorExclusionsModel);
	public List<String> validateModel(DenominatorModel denominatorModel);
	public List<String> validateModel(GeneralInformationModel generalInformationModel);
	public List<String> validateModel(GuidanceModel guidanceModel);
	public List<String> validateModel(ImprovementNotationModel improvementNotationModel);
	public List<String> validateModel(InitialPopulationModel initialPopulationModel);
	public List<String> validateModel(MeasureObservationsModel measureObservationsModel);
	public List<String> validateModel(MeasurePopulationExclusionsModel measurePopulationExclusionsModel);
	public List<String> validateModel(MeasurePopulationModel measurePopulationModel);
	public List<String> validateModel(MeasureSetModel measureSetModel);
	public List<String> validateModel(MeasureStewardDeveloperModel measureStewardDeveloperModel);
	public List<String> validateModel(MeasureTypeModel measureTypeModel);
	public List<String> validateModel(NumeratorExclusionsModel numeratorExclusionsModel);
	public List<String> validateModel(NumeratorModel numeratorModel);
	public List<String> validateModel(RateAggregationModel rateAggregationModel);
	public List<String> validateModel(RationaleModel rationaleModel);
	public List<String> validateModel(ReferencesModel referencesModel);
	public List<String> validateModel(RiskAdjustmentModel riskAdjustmentModel);
	public List<String> validateModel(StratificationModel stratificationModel);
	public List<String> validateModel(SupplementalDataElementsModel supplementalDataElementsModel);
	public List<String> validateModel(TransmissionFormatModel transmissionFormatModel);
	public List<String> validateModel(DisclaimerModel disclaimerModel);
}


