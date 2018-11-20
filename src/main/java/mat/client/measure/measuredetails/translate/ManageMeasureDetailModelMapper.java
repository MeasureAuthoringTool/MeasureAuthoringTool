package mat.client.measure.measuredetails.translate;

import java.util.List;

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
import mat.model.MeasureSteward;
import mat.model.MeasureType;

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
		measureDetailsModel.setId(manageMeasureDetailModel.getId());
		measureDetailsModel.setMeasureId(manageMeasureDetailModel.getMeasureId());
		measureDetailsModel.setRevisionNumber(manageMeasureDetailModel.getRevisionNumber());
		measureDetailsModel.setOwnerUserId(manageMeasureDetailModel.getMeasureOwnerId());
		measureDetailsModel.setComposite(isCompositeMeasure);
		measureDetailsModel.setScoringType(manageMeasureDetailModel.getScoringAbbr());
		measureDetailsModel.setGeneralInformationModel(buildGeneralInformationModel());
		measureDetailsModel.setClinicalRecommendationModel(buildClinicalRecommendationModel());
		measureDetailsModel.setCopyrightModel(buildCopyrightModel());
		measureDetailsModel.setDefinitionModel(buildDefinitionModel());
		measureDetailsModel.setDenominatorExceptionsModel(buildDenominatorExceptionsModel());
		measureDetailsModel.setDenominatorExclusionsModel(buildDenominatorExclusionsModel());
		measureDetailsModel.setDenominatorModel(buildDenominatorModel());
		measureDetailsModel.setDisclaimerModel(buildDisclaimerModel());
		measureDetailsModel.setGuidanceModel(buildGuidanceModel());
		measureDetailsModel.setImprovementNotationModel(buildImprovementNotationModel());
		measureDetailsModel.setInitialPopulationModel(buildInitialPopulationModel());
		measureDetailsModel.setMeasureObservationsModel(buildMeasureObservationsModel());
		measureDetailsModel.setMeasurePopulationExclusionsModel(buildMeasurePopulationEclusionsModel());
		measureDetailsModel.setMeasurePopulationModel(buildMeasurePopulationModel());
		measureDetailsModel.setMeasureStewardDeveloperModel(buildMeasureStewardDeveloperModel());
		measureDetailsModel.setMeasureSetModel(buildMeasureSetModel());
		measureDetailsModel.setMeasureTypeModeModel(buildMeasureTypeModel());
		measureDetailsModel.setNumeratorExclusionsModel(buildNumeratorExclusionsModel());
		measureDetailsModel.setNumeratorModel(buldNumeratorModel());
		measureDetailsModel.setRateAggregationModel(buildRateAggregationModel());
		measureDetailsModel.setRationaleModel(buldRationaleModel());
		measureDetailsModel.setReferencesModel(buildReferencesModel());
		measureDetailsModel.setRiskAdjustmentModel(buildRiskAdjustmentModel());
		measureDetailsModel.setStratificationModel(buildStratificationModel());
		measureDetailsModel.setSupplementalDataElementsModel(buildSupplementalDataModel());
		measureDetailsModel.setTransmissionFormatModel(buildTransmissionFormatModel());
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
		measurePopulationExclusionsModel.setMeasurePopulationExclusions(manageMeasureDetailModel.getMeasurePopulationExclusions());
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
		if(measureDetailsModel.isComposite()) {
			manageMeasureDetailModel = new ManageCompositeMeasureDetailModel();
			((ManageCompositeMeasureDetailModel) manageMeasureDetailModel).setCompositeScoringMethod(getCompositeScoringMethod());
		} else {
			manageMeasureDetailModel = new ManageMeasureDetailModel();	
		}
		manageMeasureDetailModel.setId(measureDetailsModel.getId());
		manageMeasureDetailModel.setMeasureId(measureDetailsModel.getMeasureId());
		manageMeasureDetailModel.setRevisionNumber(measureDetailsModel.getRevisionNumber());
		manageMeasureDetailModel.setClinicalRecomms(getClinicalRecommendation());
		manageMeasureDetailModel.setCopyright(getCopyright());
		manageMeasureDetailModel.setDefinitions(getDefinitions());
		manageMeasureDetailModel.setDenominatorExceptions(getDenominatorExceptions());
		manageMeasureDetailModel.setDenominatorExclusions(getDenominatorExclusions());
		manageMeasureDetailModel.setDenominator(getDenominator());
		manageMeasureDetailModel.setDescription(getDescription());
		manageMeasureDetailModel.setDisclaimer(getDisclaimer());
		manageMeasureDetailModel.setIsPatientBased(getPatientBased());
		manageMeasureDetailModel.setMeasScoring(getScoringMethod());
		manageMeasureDetailModel.setName(getMeasureName());
		manageMeasureDetailModel.setFinalizedDate(getFinalizedDate());
		manageMeasureDetailModel.setVersionNumber(getversionNumber());
		manageMeasureDetailModel.setShortName(getShortName());
		manageMeasureDetailModel.setGuidance(getGuidance());
		manageMeasureDetailModel.setImprovNotations(getImprovementNotation());
		manageMeasureDetailModel.setInitialPop(getInitialPopulations());
		manageMeasureDetailModel.setMeasureObservations(getMeasureObservations());
		manageMeasureDetailModel.setMeasurePopulationExclusions(getMeasurePopulationEclusions());
		manageMeasureDetailModel.setMeasurePopulation(getMeasurePopulation());
		manageMeasureDetailModel.setMeasureSetId(getMeasureSetId());
		manageMeasureDetailModel.setStewardSelectedList(getStewardList());
		manageMeasureDetailModel.setMeasureTypeSelectedList(getMeasureTypeSelectedList());
		manageMeasureDetailModel.setNumeratorExclusions(getNumeratorExclusions());
		manageMeasureDetailModel.setNumerator(getNumerator());
		manageMeasureDetailModel.setRateAggregation(getRateAggregation());
		manageMeasureDetailModel.setRationale(getRationale());
		manageMeasureDetailModel.setReferencesList(getReferencesList());
		manageMeasureDetailModel.setRiskAdjustment(getRiskAdjustment());
		manageMeasureDetailModel.setStratification(getStratification());
		manageMeasureDetailModel.setSupplementalData(getSupplementalData());
		manageMeasureDetailModel.setTransmissionFormat(getTransmissionFormat());
		return manageMeasureDetailModel;
	}

	private String getCompositeScoringMethod() {
		if(measureDetailsModel.getGeneralInformationModel() != null) {
			return measureDetailsModel.getGeneralInformationModel().getCompositeScoringMethod();
		}
		return null;
	}

	private String getShortName() {
		if(measureDetailsModel.getGeneralInformationModel() != null) {
			return measureDetailsModel.getGeneralInformationModel().geteCQMAbbreviatedTitle();
		}
		return null;
	}

	private String getversionNumber() {
		if(measureDetailsModel.getGeneralInformationModel() != null) {
			return measureDetailsModel.getGeneralInformationModel().geteCQMVersionNumber();
		}
		return null;
	}

	private String getFinalizedDate() {
		if(measureDetailsModel.getGeneralInformationModel() != null) {
			return measureDetailsModel.getGeneralInformationModel().getFinalizedDate();
		}
		return null;
	}

	private String getMeasureName() {
		if(measureDetailsModel.getGeneralInformationModel() != null) {
			return measureDetailsModel.getGeneralInformationModel().getMeasureName();
		}
		return null;
	}

	private String getScoringMethod() {
		if(measureDetailsModel.getGeneralInformationModel() != null) {
			return measureDetailsModel.getGeneralInformationModel().getScoringMethod();
		}
		return null;
	}

	private boolean getPatientBased() {
		if(measureDetailsModel.getGeneralInformationModel() != null) {
			return measureDetailsModel.getGeneralInformationModel().isPatientBased();
		}
		return false;
	}

	private String getTransmissionFormat() {
		if(measureDetailsModel.getTransmissionFormatModel() != null) {
			return measureDetailsModel.getTransmissionFormatModel().getTransmissionFormat();
		}
		return null;
	}

	private String getSupplementalData() {
		if(measureDetailsModel.getSupplementalDataElementsModel() != null) {
			return measureDetailsModel.getSupplementalDataElementsModel().getSupplementalDataElements();
		}
		return null;
	}

	private String getStratification() {
		if(measureDetailsModel.getStratificationModel() != null) {
			return measureDetailsModel.getStratificationModel().getStratification();
		}
		return null;
	}

	private String getRiskAdjustment() {
		if(measureDetailsModel.getRiskAdjustmentModel() != null) {
			return measureDetailsModel.getRiskAdjustmentModel().getRiskAdjustment();
		}
		return null;
	}

	private List<String> getReferencesList() {
		if(measureDetailsModel.getReferencesModel() != null) {
			return measureDetailsModel.getReferencesModel().getReferences();
		}
		return null;
	}

	private String getRationale() {
		if(measureDetailsModel.getRationaleModel() != null) {
			return measureDetailsModel.getRationaleModel().getRationale();
		}
		return null;
	}

	private String getRateAggregation() {
		if(measureDetailsModel.getRateAggregationModel() != null) {
			return measureDetailsModel.getRateAggregationModel().getRateAggregation();
		}
		return null;
	}

	private String getNumerator() {
		if(measureDetailsModel.getNumeratorModel() != null) {
			return measureDetailsModel.getNumeratorModel().getNumerator();
		}
		return null;
	}

	private String getNumeratorExclusions() {
		if(measureDetailsModel.getNumeratorExclusionsModel() != null) {
			return measureDetailsModel.getNumeratorExclusionsModel().getNumeratorExclusions();
		}
		return null;
	}

	private List<MeasureType> getMeasureTypeSelectedList() {
		if(measureDetailsModel.getMeasureTypeModeModel() != null) {
			return measureDetailsModel.getMeasureTypeModeModel().getMeasureTypeList();
		}
		return null;
	}

	private List<MeasureSteward> getStewardList() {
		if(measureDetailsModel.getMeasureStewardDeveloperModel() != null) {
			measureDetailsModel.getMeasureStewardDeveloperModel().getMeasureStewardList();
		}
		return null;
	}

	private String getMeasureSetId() {
		if(measureDetailsModel.getMeasureSetModel() != null) {
			measureDetailsModel.getMeasureSetModel().getMeasureSet();
		}
		return null;
	}

	private String getMeasurePopulation() {
		if(measureDetailsModel.getMeasurePopulationModel() != null)  {
			return measureDetailsModel.getMeasurePopulationModel().getMeasurePopulation();
		}
		return null;
	}

	private String getMeasurePopulationEclusions() {
		if(measureDetailsModel.getMeasurePopulationExclusionsModel() != null) {
			return measureDetailsModel.getMeasurePopulationExclusionsModel().getMeasurePopulationExclusions();
		}
		return null;
	}

	private String getMeasureObservations() {
		if(measureDetailsModel.getMeasureObservationsModel() != null) {
			return measureDetailsModel.getMeasureObservationsModel().getMeasureObservations();
		}
		return null;
	}

	private String getInitialPopulations() {
		if(measureDetailsModel.getInitialPopulationModel() != null) {
			return measureDetailsModel.getInitialPopulationModel().getInitialPopulation();
		}
		return null;
	}

	private String getImprovementNotation() {
		if(measureDetailsModel.getImprovementNotationModel() != null) {
			return measureDetailsModel.getImprovementNotationModel().getImprovementNotation();
		}
		return null;
	}

	private String getGuidance() {
		if(measureDetailsModel.getDisclaimerModel() != null) {
			return measureDetailsModel.getGuidanceModel().getGuidance();
		}
		return null;
	}

	private String getDisclaimer() {
		if(measureDetailsModel.getDisclaimerModel() != null) {
			return measureDetailsModel.getDisclaimerModel().getDisclaimer();
		}
		return null;
	}

	private String getDescription() {
		if(measureDetailsModel.getDescriptionModel() != null) {
			return measureDetailsModel.getDescriptionModel().getDescription();
		}
		return null;
	}

	private String getDenominator() {
		if(measureDetailsModel.getDenominatorModel() != null) {
			return measureDetailsModel.getDenominatorModel().getDenominator();
		}
		return null;
	}

	private String getDenominatorExclusions() {
		if(measureDetailsModel.getDenominatorExclusionsModel() != null) {
			return measureDetailsModel.getDenominatorExclusionsModel().getDenominatorExclusion();
		}
		return null;
	}

	private String getDenominatorExceptions() {
		if(measureDetailsModel.getDenominatorExceptionsModel() != null) {
			return measureDetailsModel.getDenominatorExceptionsModel().getDenominatorExceptions();
		}
		return null;
	}

	private String getDefinitions() {
		if(measureDetailsModel.getDefinitionModel() != null) {
			return measureDetailsModel.getDefinitionModel().getDefinition();
		}
		return null;
	}

	private String getCopyright() {
		if(measureDetailsModel.getCopyrightModel() != null) {
			return measureDetailsModel.getCopyrightModel().getCopyright();
		}
		return null;
	}

	private String getClinicalRecommendation() {
		if(measureDetailsModel.getClinicalRecommendationModel() != null) {
			return measureDetailsModel.getClinicalRecommendationModel().getClinicalRecommendation();
		}
		return null;
	}
}
