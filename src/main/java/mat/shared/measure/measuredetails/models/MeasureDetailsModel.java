package mat.shared.measure.measuredetails.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import mat.client.measure.ManageCompositeMeasureDetailModel;
import mat.shared.StringUtility;
import mat.shared.measure.measuredetails.validate.GeneralInformationValidator;

public class MeasureDetailsModel implements MeasureDetailsComponentModel, MeasureDetailsModelVisitor, IsSerializable  {
	private String id;
	private String measureId;
	private String revisionNumber;
	private String ownerUserId; 
	private boolean isComposite;
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
	private ManageCompositeMeasureDetailModel compositeMeasureDetailModel;
	private static String INVALID_TEXT = "Please enter valid text.";
	
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

	public ManageCompositeMeasureDetailModel getCompositeMeasureDetailModel() {
		return compositeMeasureDetailModel;
	}

	public void setCompositeMeasureDetailModel(ManageCompositeMeasureDetailModel compositeMeasureDetailModel) {
		this.compositeMeasureDetailModel = compositeMeasureDetailModel;
	}

	@Override
	public void update(MeasureDetailsModelVisitor measureDetailsModelVisitor) {

	}
	
	@Override
	public void updateModel(ClinicalRecommendationModel clinicalRecommendationModel) {
		clinicalRecommendationModel.setFormattedText(clinicalRecommendationModel.getFormattedText().trim());
		clinicalRecommendationModel.setPlainText(clinicalRecommendationModel.getPlainText().trim());
		setClinicalRecommendationModel(clinicalRecommendationModel);
	}

	@Override
	public void updateModel(CopyrightModel copyrightModel) {
		copyrightModel.setPlainText(copyrightModel.getPlainText().trim());
		copyrightModel.setFormattedText(copyrightModel.getFormattedText().trim());
		setCopyrightModel(copyrightModel);
	}

	@Override
	public void updateModel(DefinitionModel definitionModel) {
		definitionModel.setFormattedText(definitionModel.getFormattedText().trim());
		definitionModel.setPlainText(definitionModel.getPlainText().trim());
		setDefinitionModel(definitionModel);
	}

	@Override
	public void updateModel(DenominatorExceptionsModel denominatorExceptionsModel) {
		denominatorExceptionsModel.setFormattedText(denominatorExceptionsModel.getFormattedText().trim());
		denominatorExceptionsModel.setPlainText(denominatorExceptionsModel.getPlainText().trim());
		setDenominatorExceptionsModel(denominatorExceptionsModel);
	}

	@Override
	public void updateModel(DenominatorExclusionsModel denominatorExclusionsModel) {
		denominatorExclusionsModel.setFormattedText(denominatorExclusionsModel.getFormattedText().trim());
		denominatorExclusionsModel.setPlainText(denominatorExclusionsModel.getPlainText().trim());
		setDenominatorExclusionsModel(denominatorExclusionsModel);
	}

	@Override
	public void updateModel(DenominatorModel denominatorModel) {
		denominatorModel.setFormattedText(denominatorModel.getFormattedText().trim());
		denominatorModel.setPlainText(denominatorModel.getPlainText().trim());
		setDenominatorModel(denominatorModel);
	}
	
	@Override
	public void updateModel(DescriptionModel descriptionModel) {
		descriptionModel.setPlainText(descriptionModel.getPlainText().trim());
		descriptionModel.setFormattedText(descriptionModel.getFormattedText().trim());
		setDescriptionModel(descriptionModel);
	}

	@Override
	public void updateModel(DisclaimerModel disclaimerModel) {
		disclaimerModel.setPlainText(disclaimerModel.getPlainText().trim());
		disclaimerModel.setFormattedText(disclaimerModel.getFormattedText().trim());
		setDisclaimerModel(disclaimerModel);
	}
	
	@Override
	public void updateModel(GeneralInformationModel generalInformationModel) {
		setGeneralInformationModel(generalInformationModel);
	}

	@Override
	public void updateModel(GuidanceModel guidanceModel) {
		guidanceModel.setFormattedText(guidanceModel.getFormattedText().trim());
		guidanceModel.setPlainText(guidanceModel.getPlainText().trim());
		setGuidanceModel(guidanceModel);
	}

	@Override
	public void updateModel(ImprovementNotationModel improvementNotationModel) {
		improvementNotationModel.setFormattedText(improvementNotationModel.getFormattedText().trim());
		improvementNotationModel.setPlainText(improvementNotationModel.getPlainText().trim());
		setImprovementNotationModel(improvementNotationModel);
	}

	@Override
	public void updateModel(InitialPopulationModel initialPopulationModel) {
		initialPopulationModel.setPlainText(initialPopulationModel.getPlainText().trim());
		initialPopulationModel.setFormattedText(initialPopulationModel.getFormattedText().trim());
		setInitialPopulationModel(initialPopulationModel);
	}

	@Override
	public void updateModel(MeasureObservationsModel measureObservationsModel) {
		measureObservationsModel.setFormattedText(measureObservationsModel.getFormattedText().trim());
		measureObservationsModel.setPlainText(measureObservationsModel.getPlainText().trim());
		setMeasureObservationsModel(measureObservationsModel);
	}

	@Override
	public void updateModel(MeasurePopulationExclusionsModel measurePopulationExclusionsModel) {
		measurePopulationExclusionsModel.setFormattedText(measurePopulationExclusionsModel.getFormattedText().trim());
		measurePopulationExclusionsModel.setPlainText(measurePopulationExclusionsModel.getPlainText().trim());
		setMeasurePopulationExclusionsModel(measurePopulationExclusionsModel);
	}

	@Override
	public void updateModel(MeasurePopulationModel measurePopulationModel) {
		setMeasurePopulationModel(measurePopulationModel);
	}

	@Override
	public void updateModel(MeasureSetModel measureSetModel) {
		measureSetModel.setFormattedText(measureSetModel.getFormattedText().trim());
		measureSetModel.setPlainText(measureSetModel.getPlainText().trim());
		setMeasureSetModel(measureSetModel);
	}

	@Override
	public void updateModel(MeasureStewardDeveloperModel measureStewardDeveloperModel) {
		setMeasureStewardDeveloperModel(measureStewardDeveloperModel);
	}

	@Override
	public void updateModel(MeasureTypeModel measureTypeModel) {
		setMeasureTypeModeModel(measureTypeModel);
	}

	@Override
	public void updateModel(NumeratorExclusionsModel numeratorExclusionsModel) {
		numeratorExclusionsModel.setFormattedText(numeratorExclusionsModel.getFormattedText().trim());
		numeratorExclusionsModel.setPlainText(numeratorExclusionsModel.getPlainText().trim());
		setNumeratorExclusionsModel(numeratorExclusionsModel);
	}

	@Override
	public void updateModel(NumeratorModel numeratorModel) {
		numeratorModel.setFormattedText(numeratorModel.getFormattedText().trim());
		numeratorModel.setPlainText(numeratorModel.getPlainText().trim());
		setNumeratorModel(numeratorModel);
	}

	@Override
	public void updateModel(RateAggregationModel rateAggregationModel) {
		rateAggregationModel.setFormattedText(rateAggregationModel.getFormattedText().trim());
		rateAggregationModel.setPlainText(rateAggregationModel.getPlainText().trim());
		setRateAggregationModel(rateAggregationModel);
	}

	@Override
	public void updateModel(RationaleModel rationaleModel) {
		rationaleModel.setFormattedText(rationaleModel.getFormattedText().trim());
		rationaleModel.setPlainText(rationaleModel.getPlainText().trim());
		setRationaleModel(rationaleModel);
	}

	@Override
	public void updateModel(ReferencesModel referencesModel) {
		setReferencesModel(referencesModel);
	}

	@Override
	public void updateModel(RiskAdjustmentModel riskAdjustmentModel) {
		riskAdjustmentModel.setFormattedText(riskAdjustmentModel.getFormattedText().trim());
		riskAdjustmentModel.setPlainText(riskAdjustmentModel.getPlainText().trim());
		setRiskAdjustmentModel(riskAdjustmentModel);
	}

	@Override
	public void updateModel(StratificationModel stratificationModel) {
		stratificationModel.setFormattedText(stratificationModel.getFormattedText().trim());
		stratificationModel.setPlainText(stratificationModel.getPlainText().trim());
		setStratificationModel(stratificationModel);
	}

	@Override
	public void updateModel(SupplementalDataElementsModel supplementalDataElementsModel) {
		supplementalDataElementsModel.setFormattedText(supplementalDataElementsModel.getFormattedText().trim());
		supplementalDataElementsModel.setPlainText(supplementalDataElementsModel.getPlainText().trim());
		setSupplementalDataElementsModel(supplementalDataElementsModel);
	}

	@Override
	public void updateModel(TransmissionFormatModel transmissionFormatModel) {
		transmissionFormatModel.setFormattedText(transmissionFormatModel.getFormattedText().trim());
		transmissionFormatModel.setPlainText(transmissionFormatModel.getPlainText().trim());
		setTransmissionFormatModel(transmissionFormatModel);
	}

	@Override
	public List<String> validateModel(ClinicalRecommendationModel clinicalRecommendationModel) {
		return validateRichTextEditorValue(clinicalRecommendationModel.getPlainText());
	}

	@Override
	public List<String> validateModel(CopyrightModel copyrightModel) {
		return validateRichTextEditorValue(copyrightModel.getPlainText());
	}

	@Override
	public List<String> validateModel(DefinitionModel definitionModel) {
		return validateRichTextEditorValue(definitionModel.getPlainText());
	}

	@Override
	public List<String> validateModel(DescriptionModel descriptionModel) {
		return validateRichTextEditorValue(descriptionModel.getPlainText());
	}

	private List<String> validateRichTextEditorValue(String textValue) {
		if(textValue != null && textValue.length() > 0) {
			String trimmedValue = textValue.trim();
			if(StringUtility.isEmptyOrNull(trimmedValue)) {
				List<String> errorList = new ArrayList<>();
				errorList.add(INVALID_TEXT);
				return errorList;
			}
		}
		return null;
	}

	@Override
	public List<String> validateModel(DenominatorExceptionsModel denominatorExceptionsModel) {
		return validateRichTextEditorValue(denominatorExceptionsModel.getPlainText());
	}

	@Override
	public List<String> validateModel(DenominatorExclusionsModel denominatorExclusionsModel) {
		return validateRichTextEditorValue(denominatorExclusionsModel.getPlainText());
	}

	@Override
	public List<String> validateModel(DenominatorModel denominatorModel) {
		return validateRichTextEditorValue(denominatorModel.getPlainText());
	}

	@Override
	public List<String> validateModel(GeneralInformationModel generalInformationModel) {
		final GeneralInformationValidator validator = new GeneralInformationValidator();
		return validator.validateModel(generalInformationModel);
	}

	@Override
	public List<String> validateModel(GuidanceModel guidanceModel) {
		return validateRichTextEditorValue(guidanceModel.getPlainText());
	}

	@Override
	public List<String> validateModel(ImprovementNotationModel improvementNotationModel) {
		return validateRichTextEditorValue(improvementNotationModel.getPlainText());
	}

	@Override
	public List<String> validateModel(InitialPopulationModel initialPopulationModel) {
		return validateRichTextEditorValue(initialPopulationModel.getPlainText());
	}

	@Override
	public List<String> validateModel(MeasureObservationsModel measureObservationsModel) {
		return validateRichTextEditorValue(measureObservationsModel.getPlainText());
	}

	@Override
	public List<String> validateModel(MeasurePopulationExclusionsModel measurePopulationExclusionsModel) {
		return validateRichTextEditorValue(measurePopulationExclusionsModel.getPlainText());
	}

	@Override
	public List<String> validateModel(MeasurePopulationModel measurePopulationModel) {
		return validateRichTextEditorValue(measurePopulationModel.getPlainText());
	}

	@Override
	public List<String> validateModel(MeasureSetModel measureSetModel) {
		return validateRichTextEditorValue(measureSetModel.getPlainText());
	}

	@Override
	public List<String> validateModel(MeasureStewardDeveloperModel measureStewardDeveloperModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> validateModel(MeasureTypeModel measureTypeModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> validateModel(NumeratorExclusionsModel numeratorExclusionsModel) {
		return validateRichTextEditorValue(numeratorExclusionsModel.getPlainText());
	}

	@Override
	public List<String> validateModel(NumeratorModel numeratorModel) {
		return validateRichTextEditorValue(numeratorModel.getPlainText());
	}

	@Override
	public List<String> validateModel(RateAggregationModel rateAggregationModel) {
		return validateRichTextEditorValue(rateAggregationModel.getPlainText());
	}

	@Override
	public List<String> validateModel(RationaleModel rationaleModel) {
		return validateRichTextEditorValue(rationaleModel.getPlainText());
	}

	@Override
	public List<String> validateModel(ReferencesModel referencesModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> validateModel(RiskAdjustmentModel riskAdjustmentModel) {
		return validateRichTextEditorValue(riskAdjustmentModel.getPlainText());
	}

	@Override
	public List<String> validateModel(StratificationModel stratificationModel) {
		return validateRichTextEditorValue(stratificationModel.getPlainText());
	}

	@Override
	public List<String> validateModel(SupplementalDataElementsModel supplementalDataElementsModel) {
		return validateRichTextEditorValue(supplementalDataElementsModel.getPlainText());
	}

	@Override
	public List<String> validateModel(TransmissionFormatModel transmissionFormatModel) {
		return validateRichTextEditorValue(transmissionFormatModel.getPlainText());
	}

	@Override
	public List<String> validateModel(DisclaimerModel disclaimerModel) {
		return validateRichTextEditorValue(disclaimerModel.getPlainText());
	}

	@Override
	public List<String> validateModel(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDirty(ClinicalRecommendationModel clinicalRecommendationModel) {
		return !clinicalRecommendationModel.equals(this.getClinicalRecommendationModel());
	}

	@Override
	public boolean isDirty(CopyrightModel copyrightModel) {
		return !copyrightModel.equals(this.getCopyrightModel());
	}

	@Override
	public boolean isDirty(DefinitionModel definitionModel) {
		return !definitionModel.equals(this.getDefinitionModel());

	}

	@Override
	public boolean isDirty(DescriptionModel descriptionModel) {
		return !descriptionModel.equals(this.getDescriptionModel());
	}

	@Override
	public boolean isDirty(DenominatorExceptionsModel denominatorExceptionsModel) {
		return !denominatorExceptionsModel.equals(this.getDenominatorExceptionsModel());
	}

	@Override
	public boolean isDirty(DenominatorExclusionsModel denominatorExclusionsModel) {
		return !denominatorExclusionsModel.equals(this.getDenominatorExclusionsModel());
	}

	@Override
	public boolean isDirty(DenominatorModel denominatorModel) {
		return !denominatorModel.equals(this.getDenominatorModel());
	}
	
	@Override
	public boolean isDirty(DisclaimerModel disclaimerModel) {
		return !disclaimerModel.equals(this.getDisclaimerModel());
	}

	@Override
	public boolean isDirty(GeneralInformationModel generalInformationModel) {
		return !generalInformationModel.equals(this.getGeneralInformationModel());
	}

	@Override
	public boolean isDirty(GuidanceModel guidanceModel) {
		return !guidanceModel.equals(this.getGuidanceModel());

	}

	@Override
	public boolean isDirty(ImprovementNotationModel improvementNotationModel) {
		return !improvementNotationModel.equals(this.getImprovementNotationModel());

	}

	@Override
	public boolean isDirty(InitialPopulationModel initialPopulationModel) {
		return !initialPopulationModel.equals(this.getInitialPopulationModel());

	}

	@Override
	public boolean isDirty(MeasureObservationsModel measureObservationsModel) {
		return !measureObservationsModel.equals(this.getMeasureObservationsModel());

	}

	@Override
	public boolean isDirty(MeasurePopulationExclusionsModel measurePopulationExclusionsModel) {
		return !measurePopulationExclusionsModel.equals(this.getMeasurePopulationExclusionsModel());

	}

	@Override
	public boolean isDirty(MeasurePopulationModel measurePopulationModel) {
		return !measurePopulationModel.equals(this.getMeasurePopulationModel());

	}

	@Override
	public boolean isDirty(MeasureSetModel measureSetModel) {
		return !measureSetModel.equals(this.getMeasureSetModel());

	}

	@Override
	public boolean isDirty(MeasureStewardDeveloperModel measureStewardDeveloperModel) {
		return !measureStewardDeveloperModel.equals(this.getMeasureStewardDeveloperModel());

	}

	@Override
	public boolean isDirty(MeasureTypeModel measureTypeModel) {
		return !measureTypeModel.equals(this.getMeasureTypeModeModel());

	}

	@Override
	public boolean isDirty(NumeratorExclusionsModel numeratorExclusionsModel) {
		return !numeratorExclusionsModel.equals(this.getNumeratorExclusionsModel());

	}

	@Override
	public boolean isDirty(NumeratorModel numeratorModel) {
		return !numeratorModel.equals(this.getNumeratorModel());

	}

	@Override
	public boolean isDirty(RateAggregationModel rateAggregationModel) {
		return !rateAggregationModel.equals(this.getRateAggregationModel());
	}

	@Override
	public boolean isDirty(RationaleModel rationaleModel) {
		return !rationaleModel.equals(this.getRationaleModel());

	}

	@Override
	public boolean isDirty(ReferencesModel referencesModel) {
		return !referencesModel.equals(this.getReferencesModel());
	}

	@Override
	public boolean isDirty(RiskAdjustmentModel riskAdjustmentModel) {
		return !riskAdjustmentModel.equals(this.getRiskAdjustmentModel());

	}

	@Override
	public  boolean isDirty(StratificationModel stratificationModel) {
		return !stratificationModel.equals(this.getStratificationModel());

	}

	@Override
	public boolean isDirty(SupplementalDataElementsModel supplementalDataElementsModel) {
		return !supplementalDataElementsModel.equals(this.getSupplementalDataElementsModel());

	}

	@Override
	public boolean isDirty(TransmissionFormatModel transmissionFormatModel) {
		return !transmissionFormatModel.equals(this.getTransmissionFormatModel());
	}

	@Override
	public boolean isDirty(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		return false;
	}
}
