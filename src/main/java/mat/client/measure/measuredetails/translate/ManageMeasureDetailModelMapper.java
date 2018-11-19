package mat.client.measure.measuredetails.translate;

import mat.client.measure.ManageCompositeMeasureDetailModel;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.measuredetails.components.ClinicalRecommendationModel;
import mat.client.measure.measuredetails.components.CopyrightModel;
import mat.client.measure.measuredetails.components.DefinitionModel;
import mat.client.measure.measuredetails.components.DenominatorExceptionsModel;
import mat.client.measure.measuredetails.components.DenominatorExclusionsModel;
import mat.client.measure.measuredetails.components.DenominatorModel;
import mat.client.measure.measuredetails.components.DisclaimerModel;
import mat.client.measure.measuredetails.components.GeneralInformationModel;
import mat.client.measure.measuredetails.components.GuidanceModel;
import mat.client.measure.measuredetails.components.ImprovementNotationModel;
import mat.client.measure.measuredetails.components.InitialPopulationModel;
import mat.client.measure.measuredetails.components.MeasureDetailsModel;
import mat.client.measure.measuredetails.components.MeasureObservationsModel;
import mat.client.measure.measuredetails.components.MeasurePopulationExclusionsModel;
import mat.client.measure.measuredetails.components.MeasurePopulationModel;
import mat.client.measure.measuredetails.components.MeasureSetModel;
import mat.client.measure.measuredetails.components.MeasureStewardDeveloperModel;
import mat.client.measure.measuredetails.components.MeasureTypeModel;
import mat.client.measure.measuredetails.components.NumeratorExclusionsModel;
import mat.client.measure.measuredetails.components.NumeratorModel;
import mat.client.measure.measuredetails.components.RateAggregationModel;
import mat.client.measure.measuredetails.components.RationaleModel;
import mat.client.measure.measuredetails.components.ReferencesModel;
import mat.client.measure.measuredetails.components.RiskAdjustmentModel;
import mat.client.measure.measuredetails.components.StratificationModel;
import mat.client.measure.measuredetails.components.SupplementalDataElementsModel;
import mat.client.measure.measuredetails.components.TransmissionFormatModel;

public class ManageMeasureDetailModelMapper implements MeasureDetailModelMapper{
	private ManageMeasureDetailModel manageMeasureDetailModel;
	private MeasureDetailsModel measureDetailsModel;
	
	public ManageMeasureDetailModelMapper(ManageMeasureDetailModel manageMeasureDetailModel) {
		this.manageMeasureDetailModel = manageMeasureDetailModel;
	}
	
	public ManageMeasureDetailModelMapper(MeasureDetailsModel measureDetailsComponent) {
		this.measureDetailsModel = measureDetailsComponent;
	}
	
	@Override
	public MeasureDetailsModel getMeasureDetailsModel(boolean isCompositeMeasure) {
		measureDetailsModel = new MeasureDetailsModel();
		measureDetailsModel.setOwnerUserId(manageMeasureDetailModel.getMeasureOwnerId());
		measureDetailsModel.setComposite(isCompositeMeasure);
		measureDetailsModel.setScoringType(manageMeasureDetailModel.getScoringAbbr());
		measureDetailsModel.setGeneralInformation(buildGeneralInformationModel());
		measureDetailsModel.setClinicalRecommendation(buildClinicalRecommendationModel());
		measureDetailsModel.setCopyright(buildCopyrightModel());
		measureDetailsModel.setDefinition(buildDefinitionModel());
		measureDetailsModel.setDenominatorExceptions(buildDenominatorExceptionsModel());
		measureDetailsModel.setDenominatorExclusions(buildDenominatorExclusionsModel());
		measureDetailsModel.setDenominator(buildDenominatorModel());
		measureDetailsModel.setDisclaimer(buildDisclaimerModel());
		measureDetailsModel.setGuidance(buildGuidanceModel());
		measureDetailsModel.setImprovementNotation(buildImprovementNotationModel());
		measureDetailsModel.setInitialPopulation(buildInitialPopulationModel());
		measureDetailsModel.setMeasureObservations(buildMeasureObservationsModel());
		measureDetailsModel.setMeasurePopulationExclusions(buildMeasurePopulationEclusionsModel());
		measureDetailsModel.setMeasurePopulation(buildMeasurePopulationModel());
		measureDetailsModel.setMeasureStewardDeveloper(buildMeasureStewardDeveloperModel());
		measureDetailsModel.setMeasureSet(buildMeasureSetModel());
		measureDetailsModel.setMeasureTypeModel(buildMeasureTypeModel());
		measureDetailsModel.setNumeratorExclusions(buildNumeratorExclusionsModel());
		measureDetailsModel.setNumerator(buldNumeratorModel());
		measureDetailsModel.setRateAggregation(buildRateAggregationModel());
		measureDetailsModel.setRationale(buldRationaleModel());
		measureDetailsModel.setReferences(buildReferencesModel());
		measureDetailsModel.setRiskAdjustment(buildRiskAdjustmentModel());
		measureDetailsModel.setStratification(buildStratificationModel());
		measureDetailsModel.setSupplementalDataElements(buildSupplementalDataModel());
		measureDetailsModel.setTransmissionFormat(buildTransmissionFormatModel());
		return measureDetailsModel;
	}

	private TransmissionFormatModel buildTransmissionFormatModel() {
		TransmissionFormatModel transmissionFormatModel = new TransmissionFormatModel();
		transmissionFormatModel.setTransmissionFormat(manageMeasureDetailModel.getTransmissionFormat());
		return transmissionFormatModel;
	}

	private SupplementalDataElementsModel buildSupplementalDataModel() {
		SupplementalDataElementsModel supplementalDataModel = new SupplementalDataElementsModel();
		supplementalDataModel.setSupplementalDataElements(manageMeasureDetailModel.getSupplementalData());
		return supplementalDataModel;
	}

	private StratificationModel buildStratificationModel() {
		StratificationModel stratificationModel = new StratificationModel();
		stratificationModel.setStratification(manageMeasureDetailModel.getStratification());
		return stratificationModel;
	}

	private RiskAdjustmentModel buildRiskAdjustmentModel() {
		RiskAdjustmentModel riskAdjustmentModel = new RiskAdjustmentModel();
		riskAdjustmentModel.setRiskAdjustment(manageMeasureDetailModel.getRiskAdjustment());
		return riskAdjustmentModel;
	}

	private ReferencesModel buildReferencesModel() {
		ReferencesModel referencesModel = new ReferencesModel();
		referencesModel.setReferences(manageMeasureDetailModel.getReferencesList());
		return referencesModel;
	}

	private RationaleModel buldRationaleModel() {
		RationaleModel rationaleModel = new RationaleModel();
		rationaleModel.setRationale(manageMeasureDetailModel.getRationale());
		return rationaleModel;
	}

	private RateAggregationModel buildRateAggregationModel() {
		RateAggregationModel rateAggregationModel = new RateAggregationModel();
		rateAggregationModel.setRateAggregation(manageMeasureDetailModel.getRateAggregation());
		return rateAggregationModel;
	}

	private NumeratorModel buldNumeratorModel() {
		NumeratorModel numeratorModel = new NumeratorModel();
		numeratorModel.setNumerator(manageMeasureDetailModel.getNumerator());
		return numeratorModel;
	}

	private NumeratorExclusionsModel buildNumeratorExclusionsModel() {
		NumeratorExclusionsModel numeratorExclusionsModel = new NumeratorExclusionsModel();
		numeratorExclusionsModel.setNumeratorExclusions(manageMeasureDetailModel.getNumeratorExclusions());
		return numeratorExclusionsModel;
	}

	private MeasureTypeModel buildMeasureTypeModel() {
		MeasureTypeModel measureTypeModel = new MeasureTypeModel();
		measureTypeModel.setMeasureTypeList(manageMeasureDetailModel.getMeasureTypeSelectedList());
		return measureTypeModel;
	}

	private MeasureStewardDeveloperModel buildMeasureStewardDeveloperModel() {
		MeasureStewardDeveloperModel measureStewardDeveloperModel = new MeasureStewardDeveloperModel();
		measureStewardDeveloperModel.setMeasureDeveloperList(manageMeasureDetailModel.getAuthorSelectedList());
		measureStewardDeveloperModel.setMeasureSteward(manageMeasureDetailModel.getStewardSelectedList());
		return measureStewardDeveloperModel;
	}

	private MeasureSetModel buildMeasureSetModel() {
		MeasureSetModel measureSetModel = new MeasureSetModel();
		measureSetModel.setMeasureSet(manageMeasureDetailModel.getMeasureSetId());
		return measureSetModel;
	}

	private MeasurePopulationModel buildMeasurePopulationModel() {
		MeasurePopulationModel measurePopulationModel = new MeasurePopulationModel();
		measurePopulationModel.setMeasurePopulation(manageMeasureDetailModel.getMeasurePopulation());
		return measurePopulationModel;
	}

	private MeasurePopulationExclusionsModel buildMeasurePopulationEclusionsModel() {
		MeasurePopulationExclusionsModel measurePopulationExclusionsModel = new MeasurePopulationExclusionsModel();
		measurePopulationExclusionsModel.setMeasurePopulation(manageMeasureDetailModel.getMeasurePopulationExclusions());
		return measurePopulationExclusionsModel;
	}

	private MeasureObservationsModel buildMeasureObservationsModel() {
		MeasureObservationsModel measureObservationsModel = new MeasureObservationsModel();
		measureObservationsModel.setMeasureObservations(manageMeasureDetailModel.getMeasureObservations());
		return measureObservationsModel;
	}

	private InitialPopulationModel buildInitialPopulationModel() {
		InitialPopulationModel initialPopulationModel = new InitialPopulationModel();
		initialPopulationModel.setInitialPopulation(manageMeasureDetailModel.getInitialPop());
		return initialPopulationModel;
	}

	private ImprovementNotationModel buildImprovementNotationModel() {
		ImprovementNotationModel improvementNotationModel = new ImprovementNotationModel();
		improvementNotationModel.setImprovementNotation(manageMeasureDetailModel.getImprovNotations());
		return improvementNotationModel;
	}

	private GuidanceModel buildGuidanceModel() {
		GuidanceModel guidanceModel = new GuidanceModel();
		guidanceModel.setGuidance(manageMeasureDetailModel.getGuidance());
		return guidanceModel;
	}

	private DisclaimerModel buildDisclaimerModel() {
		DisclaimerModel disclaimerModel = new DisclaimerModel();
		disclaimerModel.setDisclaimer(manageMeasureDetailModel.getDisclaimer());
		return disclaimerModel;
	}

	private DenominatorModel buildDenominatorModel() {
		DenominatorModel denominatorModel = new DenominatorModel();
		denominatorModel.setDenominator(manageMeasureDetailModel.getDenominator());
		return denominatorModel;
	}

	private DenominatorExclusionsModel buildDenominatorExclusionsModel() {
		DenominatorExclusionsModel denominatorExclusionsModel = new DenominatorExclusionsModel();
		denominatorExclusionsModel.setDenominatorExclusion(manageMeasureDetailModel.getDenominatorExclusions());
		return denominatorExclusionsModel;
	}

	private DenominatorExceptionsModel buildDenominatorExceptionsModel() {
		DenominatorExceptionsModel denominationsExceptionsModel = new DenominatorExceptionsModel();
		denominationsExceptionsModel.setDenominatorExceptions(manageMeasureDetailModel.getDenominatorExceptions());
		return denominationsExceptionsModel;
	}

	private DefinitionModel buildDefinitionModel() {
		DefinitionModel definitionModel = new DefinitionModel();
		definitionModel.setDefinition(manageMeasureDetailModel.getDefinitions());
		return definitionModel;
	}

	private CopyrightModel buildCopyrightModel() {
		CopyrightModel copyrightModel = new CopyrightModel();
		copyrightModel.setCopyright(manageMeasureDetailModel.getCopyright());
		return copyrightModel;
	}

	private ClinicalRecommendationModel buildClinicalRecommendationModel() {
		ClinicalRecommendationModel clinicalRecommendationModel = new ClinicalRecommendationModel();
		clinicalRecommendationModel.setClinicalRecommendation(manageMeasureDetailModel.getClinicalRecomms());
		return clinicalRecommendationModel;
	}

	private GeneralInformationModel buildGeneralInformationModel() {
		GeneralInformationModel generalInformationModel = new GeneralInformationModel();
		generalInformationModel.setMeasureName(manageMeasureDetailModel.getName());
		generalInformationModel.seteCQMAbbreviatedTitle(manageMeasureDetailModel.getShortName());
		generalInformationModel.setFinalizedDate(manageMeasureDetailModel.getFinalizedDate());
		generalInformationModel.setPatientBased(manageMeasureDetailModel.isPatientBased());
		generalInformationModel.setGuid(manageMeasureDetailModel.getMeasureSetId());
		generalInformationModel.seteCQMVersionNumber(manageMeasureDetailModel.getVersionNumber());
		generalInformationModel.setScoringMethod(manageMeasureDetailModel.getMeasScoring());
		if(manageMeasureDetailModel instanceof ManageCompositeMeasureDetailModel) {
			generalInformationModel.setCompositeScoringMethod(((ManageCompositeMeasureDetailModel) manageMeasureDetailModel).getCompositeScoringMethod());
		}

		return generalInformationModel;
	}

	@Override
	public ManageMeasureDetailModel convertMeasureDetailsToManageMeasureDetailModel() {
		// TODO Auto-generated method stub
		return null;
	}
}
