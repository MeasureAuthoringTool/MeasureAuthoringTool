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
	public void updateModel(TransmissionFormatModel transmissionFormatModel);
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


	public boolean isDirty(ClinicalRecommendationModel clinicalRecommendationModel);
	public boolean isDirty(CopyrightModel copyrightModel);
	public boolean isDirty(DefinitionModel definitionModel);
	public boolean isDirty(DescriptionModel descriptionModel);
	public boolean isDirty(DenominatorExceptionsModel denominatorExceptionsModel);
	public boolean isDirty(DenominatorExclusionsModel denominatorExclusionsModel);
	public boolean isDirty(DenominatorModel denominatorModel);
	public boolean isDirty(GeneralInformationModel generalInformationModel);
	public boolean isDirty(GuidanceModel guidanceModel);
	public boolean isDirty(ImprovementNotationModel improvementNotationModel);
	public boolean isDirty(InitialPopulationModel initialPopulationModel);
	public boolean isDirty(MeasureObservationsModel measureObservationsModel);
	public boolean isDirty(MeasurePopulationExclusionsModel measurePopulationExclusionsModel);
	public boolean isDirty(MeasurePopulationModel measurePopulationModel);
	public boolean isDirty(MeasureSetModel measureSetModel);
	public boolean isDirty(MeasureStewardDeveloperModel measureStewardDeveloperModel);
	public boolean isDirty(MeasureTypeModel measureTypeModel);
	public boolean isDirty(NumeratorExclusionsModel numeratorExclusionsModel);
	public boolean isDirty(NumeratorModel numeratorModel);
	public boolean isDirty(RateAggregationModel rateAggregationModel);
	public boolean isDirty(RationaleModel rationaleModel);
	public boolean isDirty(ReferencesModel referencesModel);
	public boolean isDirty(RiskAdjustmentModel riskAdjustmentModel);
	public boolean isDirty(StratificationModel stratificationModel);
	public boolean isDirty(SupplementalDataElementsModel supplementalDataElementsModel);
	public boolean isDirty(TransmissionFormatModel transmissionFormatModel);
	public boolean isDirty(DisclaimerModel disclaimerModel);
}


