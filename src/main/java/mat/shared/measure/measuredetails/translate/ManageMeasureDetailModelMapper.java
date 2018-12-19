package mat.shared.measure.measuredetails.translate;

import java.util.List;

import mat.client.measure.ManageCompositeMeasureDetailModel;
import mat.client.measure.ManageMeasureDetailModel;
import mat.model.MeasureSteward;
import mat.model.MeasureType;
import mat.shared.measure.measuredetails.models.ClinicalRecommendationModel;
import mat.shared.measure.measuredetails.models.CopyrightModel;
import mat.shared.measure.measuredetails.models.DefinitionModel;
import mat.shared.measure.measuredetails.models.DenominatorExceptionsModel;
import mat.shared.measure.measuredetails.models.DenominatorExclusionsModel;
import mat.shared.measure.measuredetails.models.DenominatorModel;
import mat.shared.measure.measuredetails.models.DescriptionModel;
import mat.shared.measure.measuredetails.models.DisclaimerModel;
import mat.shared.measure.measuredetails.models.GeneralInformationModel;
import mat.shared.measure.measuredetails.models.GuidanceModel;
import mat.shared.measure.measuredetails.models.ImprovementNotationModel;
import mat.shared.measure.measuredetails.models.InitialPopulationModel;
import mat.shared.measure.measuredetails.models.MeasureDetailsModel;
import mat.shared.measure.measuredetails.models.MeasureObservationsModel;
import mat.shared.measure.measuredetails.models.MeasurePopulationExclusionsModel;
import mat.shared.measure.measuredetails.models.MeasurePopulationModel;
import mat.shared.measure.measuredetails.models.MeasureSetModel;
import mat.shared.measure.measuredetails.models.MeasureStewardDeveloperModel;
import mat.shared.measure.measuredetails.models.MeasureTypeModel;
import mat.shared.measure.measuredetails.models.NumeratorExclusionsModel;
import mat.shared.measure.measuredetails.models.NumeratorModel;
import mat.shared.measure.measuredetails.models.RateAggregationModel;
import mat.shared.measure.measuredetails.models.RationaleModel;
import mat.shared.measure.measuredetails.models.ReferencesModel;
import mat.shared.measure.measuredetails.models.RiskAdjustmentModel;
import mat.shared.measure.measuredetails.models.StratificationModel;
import mat.shared.measure.measuredetails.models.SupplementalDataElementsModel;
import mat.shared.measure.measuredetails.models.TransmissionFormatModel;

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
		measureDetailsModel.setGeneralInformationModel(buildGeneralInformationModel());
		measureDetailsModel.setClinicalRecommendationModel(buildClinicalRecommendationModel());
		measureDetailsModel.setDescriptionModel(buildDescriptionModel());
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
		transmissionFormatModel.setPlainText(manageMeasureDetailModel.getTransmissionFormat());
		transmissionFormatModel.setFormattedText(manageMeasureDetailModel.getTransmissionFormat());
		return transmissionFormatModel;
	}

	private SupplementalDataElementsModel buildSupplementalDataModel() {
		SupplementalDataElementsModel supplementalDataModel = new SupplementalDataElementsModel();
		supplementalDataModel.setPlainText(manageMeasureDetailModel.getSupplementalData());
		supplementalDataModel.setFormattedText(manageMeasureDetailModel.getSupplementalData());
		return supplementalDataModel;
	}

	private StratificationModel buildStratificationModel() {
		StratificationModel stratificationModel = new StratificationModel();
		stratificationModel.setPlainText(manageMeasureDetailModel.getStratification());
		stratificationModel.setFormattedText(manageMeasureDetailModel.getStratification());
		return stratificationModel;
	}

	private RiskAdjustmentModel buildRiskAdjustmentModel() {
		RiskAdjustmentModel riskAdjustmentModel = new RiskAdjustmentModel();
		riskAdjustmentModel.setPlainText(manageMeasureDetailModel.getRiskAdjustment());
		riskAdjustmentModel.setFormattedText(manageMeasureDetailModel.getRiskAdjustment());
		return riskAdjustmentModel;
	}

	private ReferencesModel buildReferencesModel() {
		ReferencesModel referencesModel = new ReferencesModel();
		referencesModel.setReferences(manageMeasureDetailModel.getReferencesList());
		return referencesModel;
	}

	private RationaleModel buldRationaleModel() {
		RationaleModel rationaleModel = new RationaleModel();
		rationaleModel.setPlainText(manageMeasureDetailModel.getRationale());
		rationaleModel.setFormattedText(manageMeasureDetailModel.getRationale());
		return rationaleModel;
	}

	private RateAggregationModel buildRateAggregationModel() {
		RateAggregationModel rateAggregationModel = new RateAggregationModel();
		rateAggregationModel.setPlainText(manageMeasureDetailModel.getRateAggregation());
		rateAggregationModel.setFormattedText(manageMeasureDetailModel.getRateAggregation());
		return rateAggregationModel;
	}

	private NumeratorModel buldNumeratorModel() {
		NumeratorModel numeratorModel = new NumeratorModel();
		numeratorModel.setPlainText(manageMeasureDetailModel.getNumerator());
		numeratorModel.setFormattedText(manageMeasureDetailModel.getNumerator());
		return numeratorModel;
	}

	private NumeratorExclusionsModel buildNumeratorExclusionsModel() {
		NumeratorExclusionsModel numeratorExclusionsModel = new NumeratorExclusionsModel();
		numeratorExclusionsModel.setPlainText(manageMeasureDetailModel.getNumeratorExclusions());
		numeratorExclusionsModel.setFormattedText(manageMeasureDetailModel.getNumeratorExclusions());
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
		measureSetModel.setPlainText(manageMeasureDetailModel.getGroupName());
		measureSetModel.setFormattedText(manageMeasureDetailModel.getGroupName());
		return measureSetModel;
	}

	private MeasurePopulationModel buildMeasurePopulationModel() {
		MeasurePopulationModel measurePopulationModel = new MeasurePopulationModel();
		measurePopulationModel.setPlainText(manageMeasureDetailModel.getMeasurePopulation());
		measurePopulationModel.setFormattedText(manageMeasureDetailModel.getMeasurePopulation());
		return measurePopulationModel;
	}

	private MeasurePopulationExclusionsModel buildMeasurePopulationEclusionsModel() {
		MeasurePopulationExclusionsModel measurePopulationExclusionsModel = new MeasurePopulationExclusionsModel();
		measurePopulationExclusionsModel.setPlainText(manageMeasureDetailModel.getMeasurePopulationExclusions());
		measurePopulationExclusionsModel.setFormattedText(manageMeasureDetailModel.getMeasurePopulationExclusions());
		return measurePopulationExclusionsModel;
	}

	private MeasureObservationsModel buildMeasureObservationsModel() {
		MeasureObservationsModel measureObservationsModel = new MeasureObservationsModel();
		measureObservationsModel.setPlainText(manageMeasureDetailModel.getMeasureObservations());
		measureObservationsModel.setFormattedText(manageMeasureDetailModel.getMeasureObservations());
		return measureObservationsModel;
	}

	private InitialPopulationModel buildInitialPopulationModel() {
		InitialPopulationModel initialPopulationModel = new InitialPopulationModel();
		initialPopulationModel.setPlainText(manageMeasureDetailModel.getInitialPop());
		initialPopulationModel.setFormattedText(manageMeasureDetailModel.getInitialPop());
		return initialPopulationModel;
	}

	private ImprovementNotationModel buildImprovementNotationModel() {
		ImprovementNotationModel improvementNotationModel = new ImprovementNotationModel();
		improvementNotationModel.setPlainText(manageMeasureDetailModel.getImprovNotations());
		improvementNotationModel.setFormattedText(manageMeasureDetailModel.getImprovNotations());
		return improvementNotationModel;
	}

	private GuidanceModel buildGuidanceModel() {
		GuidanceModel guidanceModel = new GuidanceModel();
		guidanceModel.setPlainText(manageMeasureDetailModel.getGuidance());
		guidanceModel.setFormattedText(manageMeasureDetailModel.getGuidance());
		return guidanceModel;
	}

	private DisclaimerModel buildDisclaimerModel() {
		DisclaimerModel disclaimerModel = new DisclaimerModel();
		disclaimerModel.setPlainText(manageMeasureDetailModel.getDisclaimer());
		disclaimerModel.setFormattedText(manageMeasureDetailModel.getDisclaimer());
		return disclaimerModel;
	}

	private DenominatorModel buildDenominatorModel() {
		DenominatorModel denominatorModel = new DenominatorModel();
		denominatorModel.setPlainText(manageMeasureDetailModel.getDenominator());
		denominatorModel.setFormattedText(manageMeasureDetailModel.getDenominator());
		return denominatorModel;
	}

	private DenominatorExclusionsModel buildDenominatorExclusionsModel() {
		DenominatorExclusionsModel denominatorExclusionsModel = new DenominatorExclusionsModel();
		denominatorExclusionsModel.setPlainText(manageMeasureDetailModel.getDenominatorExclusions());
		denominatorExclusionsModel.setFormattedText(manageMeasureDetailModel.getDenominatorExclusions());
		return denominatorExclusionsModel;
	}

	private DenominatorExceptionsModel buildDenominatorExceptionsModel() {
		DenominatorExceptionsModel denominationsExceptionsModel = new DenominatorExceptionsModel();
		denominationsExceptionsModel.setPlainText(manageMeasureDetailModel.getDenominatorExceptions());
		denominationsExceptionsModel.setFormattedText(manageMeasureDetailModel.getDenominatorExceptions());
		return denominationsExceptionsModel;
	}

	private DefinitionModel buildDefinitionModel() {
		DefinitionModel definitionModel = new DefinitionModel();
		definitionModel.setPlainText(manageMeasureDetailModel.getDefinitions());
		definitionModel.setFormattedText(manageMeasureDetailModel.getDefinitions());
		return definitionModel;
	}
	
	private DescriptionModel buildDescriptionModel() {
		DescriptionModel descriptionModel = new DescriptionModel();
		descriptionModel.setPlainText(manageMeasureDetailModel.getDescription());
		descriptionModel.setFormattedText(manageMeasureDetailModel.getDescription());
		return descriptionModel;
	}

	private CopyrightModel buildCopyrightModel() {
		CopyrightModel copyrightModel = new CopyrightModel();
		copyrightModel.setPlainText(manageMeasureDetailModel.getCopyright());
		copyrightModel.setFormattedText(manageMeasureDetailModel.getCopyright());
		return copyrightModel;
	}

	private ClinicalRecommendationModel buildClinicalRecommendationModel() {
		ClinicalRecommendationModel clinicalRecommendationModel = new ClinicalRecommendationModel();
		clinicalRecommendationModel.setPlainText(manageMeasureDetailModel.getClinicalRecomms());
		clinicalRecommendationModel.setFormattedText(manageMeasureDetailModel.getClinicalRecomms());
		return clinicalRecommendationModel;
	}

	private GeneralInformationModel buildGeneralInformationModel() {
		GeneralInformationModel generalInformationModel = new GeneralInformationModel();
		generalInformationModel.seteMeasureId(manageMeasureDetailModel.geteMeasureId());
		generalInformationModel.setMeasureName(manageMeasureDetailModel.getName());
		generalInformationModel.seteCQMAbbreviatedTitle(manageMeasureDetailModel.getShortName());
		generalInformationModel.setFinalizedDate(manageMeasureDetailModel.getFinalizedDate());
		generalInformationModel.setPatientBased(manageMeasureDetailModel.isPatientBased());
		generalInformationModel.setGuid(manageMeasureDetailModel.getMeasureSetId());
		generalInformationModel.seteCQMVersionNumber(manageMeasureDetailModel.getVersionNumber());
		generalInformationModel.setScoringMethod(manageMeasureDetailModel.getMeasScoring());
		generalInformationModel.setNqfId(manageMeasureDetailModel.getNqfId());
		generalInformationModel.setEndorseByNQF(manageMeasureDetailModel.getEndorseByNQF());
		generalInformationModel.setCalenderYear(manageMeasureDetailModel.isCalenderYear());
		generalInformationModel.setMeasureFromPeriod(manageMeasureDetailModel.getMeasFromPeriod());
		generalInformationModel.setMeasureToPeriod(manageMeasureDetailModel.getMeasToPeriod());
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
		manageMeasureDetailModel.seteMeasureId(getEMeasureId());
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
		manageMeasureDetailModel.setGroupName(getMeasureSetText());
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
		manageMeasureDetailModel.setNqfId(getNqfId());
		manageMeasureDetailModel.setEndorseByNQF(getEndorsedByNQF());
		manageMeasureDetailModel.setCalenderYear(isCalendarYear());
		manageMeasureDetailModel.setMeasFromPeriod(getMeasureFromPeriod());
		manageMeasureDetailModel.setMeasToPeriod(getMeasureToPeriod());
		return manageMeasureDetailModel;
	}

	private String getMeasureToPeriod() {
		if(measureDetailsModel.getGeneralInformationModel() != null) {
			return measureDetailsModel.getGeneralInformationModel().getMeasureToPeriod();
		}
		return null;
	}

	private String getMeasureFromPeriod() {
		if(measureDetailsModel.getGeneralInformationModel() != null) {
			return measureDetailsModel.getGeneralInformationModel().getMeasureFromPeriod();
		}
		return null;
	}

	private boolean isCalendarYear() {
		if(measureDetailsModel.getGeneralInformationModel() != null) {
			return measureDetailsModel.getGeneralInformationModel().isCalenderYear();
		}
		return false;
	}

	private Boolean getEndorsedByNQF() {
		if(measureDetailsModel.getGeneralInformationModel() != null) {
			return measureDetailsModel.getGeneralInformationModel().getEndorseByNQF();
		}
		return null;
	}

	private String getNqfId() {
		if(measureDetailsModel.getGeneralInformationModel() != null) {
			return measureDetailsModel.getGeneralInformationModel().getNqfId();
		}
		return null;
	}

	private int getEMeasureId() {
		if(measureDetailsModel.getGeneralInformationModel() != null) {
			return measureDetailsModel.getGeneralInformationModel().geteMeasureId();
		}
		return 0;
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
			return measureDetailsModel.getTransmissionFormatModel().getPlainText();
		}
		return null;
	}

	private String getSupplementalData() {
		if(measureDetailsModel.getSupplementalDataElementsModel() != null) {
			return measureDetailsModel.getSupplementalDataElementsModel().getPlainText();
		}
		return null;
	}

	private String getStratification() {
		if(measureDetailsModel.getStratificationModel() != null) {
			return measureDetailsModel.getStratificationModel().getPlainText();
		}
		return null;
	}

	private String getRiskAdjustment() {
		if(measureDetailsModel.getRiskAdjustmentModel() != null) {
			return measureDetailsModel.getRiskAdjustmentModel().getPlainText();
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
			return measureDetailsModel.getRationaleModel().getPlainText();
		}
		return null;
	}

	private String getRateAggregation() {
		if(measureDetailsModel.getRateAggregationModel() != null) {
			return measureDetailsModel.getRateAggregationModel().getPlainText();
		}
		return null;
	}

	private String getNumerator() {
		if(measureDetailsModel.getNumeratorModel() != null) {
			return measureDetailsModel.getNumeratorModel().getPlainText();
		}
		return null;
	}

	private String getNumeratorExclusions() {
		if(measureDetailsModel.getNumeratorExclusionsModel() != null) {
			return measureDetailsModel.getNumeratorExclusionsModel().getPlainText();
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
			return measureDetailsModel.getMeasureStewardDeveloperModel().getMeasureStewardList();
		}
		return null;
	}

	private String getMeasureSetText() {
		if(measureDetailsModel.getMeasureSetModel() != null) {
			return measureDetailsModel.getMeasureSetModel().getPlainText();
		}
		return null;
	}

	private String getMeasurePopulation() {
		if(measureDetailsModel.getMeasurePopulationModel() != null)  {
			return measureDetailsModel.getMeasurePopulationModel().getPlainText();
		}
		return null;
	}

	private String getMeasurePopulationEclusions() {
		if(measureDetailsModel.getMeasurePopulationExclusionsModel() != null) {
			return measureDetailsModel.getMeasurePopulationExclusionsModel().getPlainText();
		}
		return null;
	}

	private String getMeasureObservations() {
		if(measureDetailsModel.getMeasureObservationsModel() != null) {
			return measureDetailsModel.getMeasureObservationsModel().getPlainText();
		}
		return null;
	}

	private String getInitialPopulations() {
		if(measureDetailsModel.getInitialPopulationModel() != null) {
			return measureDetailsModel.getInitialPopulationModel().getPlainText();
		}
		return null;
	}

	private String getImprovementNotation() {
		if(measureDetailsModel.getImprovementNotationModel() != null) {
			return measureDetailsModel.getImprovementNotationModel().getPlainText();
		}
		return null;
	}

	private String getGuidance() {
		if(measureDetailsModel.getDisclaimerModel() != null) {
			return measureDetailsModel.getGuidanceModel().getPlainText();
		}
		return null;
	}

	private String getDisclaimer() {
		if(measureDetailsModel.getDisclaimerModel() != null) {
			return measureDetailsModel.getDisclaimerModel().getPlainText();
		}
		return null;
	}

	private String getDescription() {
		if(measureDetailsModel.getDescriptionModel() != null) {
			return measureDetailsModel.getDescriptionModel().getPlainText();
		}
		return null;
	}

	private String getDenominator() {
		if(measureDetailsModel.getDenominatorModel() != null) {
			return measureDetailsModel.getDenominatorModel().getPlainText();
		}
		return null;
	}

	private String getDenominatorExclusions() {
		if(measureDetailsModel.getDenominatorExclusionsModel() != null) {
			return measureDetailsModel.getDenominatorExclusionsModel().getPlainText();
		}
		return null;
	}

	private String getDenominatorExceptions() {
		if(measureDetailsModel.getDenominatorExceptionsModel() != null) {
			return measureDetailsModel.getDenominatorExceptionsModel().getPlainText();
		}
		return null;
	}

	private String getDefinitions() {
		if(measureDetailsModel.getDefinitionModel() != null) {
			return measureDetailsModel.getDefinitionModel().getPlainText();
		}
		return null;
	}

	private String getCopyright() {
		if(measureDetailsModel.getCopyrightModel() != null) {
			return measureDetailsModel.getCopyrightModel().getPlainText();
		}
		return null;
	}

	private String getClinicalRecommendation() {
		if(measureDetailsModel.getClinicalRecommendationModel() != null) {
			return measureDetailsModel.getClinicalRecommendationModel().getPlainText();
		}
		return null;
	}
}
