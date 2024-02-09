package mat.client.measure.measuredetails.views;

import mat.client.measure.measuredetails.MeasureDetailsObserver;
import mat.client.measure.measuredetails.observers.ClinicalRecommendationObserver;
import mat.client.measure.measuredetails.observers.CopyrightObserver;
import mat.client.measure.measuredetails.observers.DefinitionObserver;
import mat.client.measure.measuredetails.observers.DenominatorExceptionsObserver;
import mat.client.measure.measuredetails.observers.DenominatorExclusionsObserver;
import mat.client.measure.measuredetails.observers.DenominatorObserver;
import mat.client.measure.measuredetails.observers.DescriptionObserver;
import mat.client.measure.measuredetails.observers.DisclaimerObserver;
import mat.client.measure.measuredetails.observers.GeneralInformationObserver;
import mat.client.measure.measuredetails.observers.GuidanceObserver;
import mat.client.measure.measuredetails.observers.ImprovementNotationObserver;
import mat.client.measure.measuredetails.observers.InitialPopulationObserver;
import mat.client.measure.measuredetails.observers.MeasureDetailsComponentObserver;
import mat.client.measure.measuredetails.observers.MeasureObservationsObserver;
import mat.client.measure.measuredetails.observers.MeasurePopulationExclusionsObserver;
import mat.client.measure.measuredetails.observers.MeasurePopulationObserver;
import mat.client.measure.measuredetails.observers.MeasureSetObserver;
import mat.client.measure.measuredetails.observers.MeasureStewardDeveloperObserver;
import mat.client.measure.measuredetails.observers.MeasureTypeObserver;
import mat.client.measure.measuredetails.observers.NumeratorExclusionsObserver;
import mat.client.measure.measuredetails.observers.NumeratorObserver;
import mat.client.measure.measuredetails.observers.RateAggregationObserver;
import mat.client.measure.measuredetails.observers.RationaleObserver;
import mat.client.measure.measuredetails.observers.ReferencesObserver;
import mat.client.measure.measuredetails.observers.RiskAdjustmentObserver;
import mat.client.measure.measuredetails.observers.StratificationObserver;
import mat.client.measure.measuredetails.observers.SupplementalDataElementsObserver;
import mat.client.measure.measuredetails.observers.TransmissionFormatObserver;
import mat.client.shared.MatContext;
import mat.client.shared.MatDetailItem;
import mat.client.shared.MeasureDetailsConstants;
import mat.client.shared.MeasureDetailsConstants.MeasureDetailsItems;
import mat.client.shared.MeasureDetailsConstants.PopulationItems;
import mat.client.shared.MessagePanel;
import mat.client.util.FeatureFlagConstant;
import mat.shared.measure.measuredetails.models.GeneralInformationModel;
import mat.shared.measure.measuredetails.models.MeasureDetailsModel;
import mat.shared.measure.measuredetails.models.MeasureDetailsTextAbstractModel;
import mat.shared.measure.measuredetails.models.MeasureStewardDeveloperModel;
import mat.shared.measure.measuredetails.models.ReferencesModel;

public class MeasureDetailsViewFactory {
	private static MeasureDetailsViewFactory instance;
	private MeasureDetailsViewFactory() {}

	public static MeasureDetailsViewFactory get() {
		if(instance == null) {
			instance = new MeasureDetailsViewFactory();
		}
		return instance;
	}

	public MeasureDetailViewInterface getMeasureDetailComponentView(MeasureDetailsModel measureDetailsModel, MatDetailItem currentMeasureDetail, MeasureDetailsObserver measureDetailsObserver, MessagePanel messagePanel) {
		if(currentMeasureDetail instanceof MeasureDetailsConstants.MeasureDetailsItems) {
			switch((MeasureDetailsItems) currentMeasureDetail) {
			case COMPONENT_MEASURES:
				return new ComponentMeasuresView(measureDetailsObserver, measureDetailsModel.getCompositeMeasureDetailModel());
			case DESCRIPTION:
				return buildRichTextEditorView(measureDetailsModel.getDescriptionModel(), new DescriptionView(), new DescriptionObserver());
			case DISCLAIMER:
				return buildRichTextEditorView(measureDetailsModel.getDisclaimerModel(), new DisclaimerView(), new DisclaimerObserver());
			case MEASURE_TYPE:
				return buildMeasureTypeView(new MeasureTypeView(measureDetailsModel.isComposite(), measureDetailsModel.getMeasureTypeModeModel(), MatContext.get().getMeasureTypeList()), new MeasureTypeObserver());
			case STRATIFICATION:
				return buildRichTextEditorView(measureDetailsModel.getStratificationModel(), new StratificationView(), new StratificationObserver());
			case RISK_ADJUSTMENT:
				return buildRichTextEditorView(measureDetailsModel.getRiskAdjustmentModel(), new RiskAdjustmentView(), new RiskAdjustmentObserver());
			case RATE_AGGREGATION:
				return buildRichTextEditorView(measureDetailsModel.getRateAggregationModel(), new RateAggregationView(), new RateAggregationObserver());
			case RATIONALE:
				return buildRichTextEditorView(measureDetailsModel.getRationaleModel(), new RationaleView(), new RationaleObserver());
			case CLINICAL_RECOMMENDATION:
				return buildRichTextEditorView(measureDetailsModel.getClinicalRecommendationModel(), new ClinicalRecommendationView(), new ClinicalRecommendationObserver());
			case IMPROVEMENT_NOTATION:
				return buildRichTextEditorView(measureDetailsModel.getImprovementNotationModel(), new ImprovementNotationView(), new ImprovementNotationObserver());
			case REFERENCES:
				return buildRefererencesView(measureDetailsModel.getReferencesModel(), measureDetailsObserver, messagePanel);
			case DEFINITION:
				return buildRichTextEditorView(measureDetailsModel.getDefinitionModel(), new DefinitionView(), new DefinitionObserver());
			case GUIDANCE:
				return buildRichTextEditorView(measureDetailsModel.getGuidanceModel(), new GuidanceView(), new GuidanceObserver());
			case TRANSMISSION_FORMAT:
				return buildRichTextEditorView(measureDetailsModel.getTransmissionFormatModel(), new TransmissionFormatView(), new TransmissionFormatObserver());
			case SUPPLEMENTAL_DATA_ELEMENTS:
				return buildRichTextEditorView(measureDetailsModel.getSupplementalDataElementsModel(), new SupplementalDataElementsView(), new SupplementalDataElementsObserver());
			case MEASURE_SET:
				return buildRichTextEditorView(measureDetailsModel.getMeasureSetModel(), new MeasureSetView(), new MeasureSetObserver());
			case STEWARD:
				return buildMeasureStewardView(measureDetailsModel.getMeasureStewardDeveloperModel());
			case COPYRIGHT:
				return buildRichTextEditorView(measureDetailsModel.getCopyrightModel(), new CopyrightView(), new CopyrightObserver());
			case POPULATIONS:
				return new PopulationsView();
			case GENERAL_MEASURE_INFORMATION:
			default:
				return buildGeneralMeasureInformationView(measureDetailsModel.isComposite(), measureDetailsModel.getGeneralInformationModel());
			}
		} else if ( currentMeasureDetail instanceof MeasureDetailsConstants.PopulationItems) {
			switch((PopulationItems) currentMeasureDetail) {
			case INITIAL_POPULATION:
				return buildRichTextEditorView(measureDetailsModel.getInitialPopulationModel(), new InitialPopulationView(), new InitialPopulationObserver());
			case MEASURE_POPULATION:
				return buildRichTextEditorView(measureDetailsModel.getMeasurePopulationModel(), new MeasurePopulationView(), new MeasurePopulationObserver());
			case MEASURE_POPULATION_EXCLUSIONS:
				return buildRichTextEditorView(measureDetailsModel.getMeasurePopulationExclusionsModel(), new MeasurePopulationExclusionsView(), new MeasurePopulationExclusionsObserver());
			case DENOMINATOR:
				return buildRichTextEditorView(measureDetailsModel.getDenominatorModel(), new DenominatorView(), new DenominatorObserver());
			case DENOMINATOR_EXCLUSIONS:
				return buildRichTextEditorView(measureDetailsModel.getDenominatorExclusionsModel(), new DenominatorExclusionsView(), new DenominatorExclusionsObserver());
			case NUMERATOR:
				return buildRichTextEditorView(measureDetailsModel.getNumeratorModel(), new NumeratorView(), new NumeratorObserver());
			case NUMERATOR_EXCLUSIONS:
				return buildRichTextEditorView(measureDetailsModel.getNumeratorExclusionsModel(), new NumeratorExclusionsView(), new NumeratorExclusionsObserver());
			case DENOMINATOR_EXCEPTIONS:
				return buildRichTextEditorView(measureDetailsModel.getDenominatorExceptionsModel(), new DenominatorExceptionsView(), new DenominatorExceptionsObserver());
			case MEASURE_OBSERVATIONS:
				return buildRichTextEditorView(measureDetailsModel.getMeasureObservationsModel(), new MeasureObservationsView(), new MeasureObservationsObserver());
			}
		}
		return buildGeneralMeasureInformationView(measureDetailsModel.isComposite(), measureDetailsModel.getGeneralInformationModel());
	}
	
	private MeasureDetailViewInterface buildRefererencesView(ReferencesModel referencesModel, MeasureDetailsObserver measureDetailObserver, MessagePanel messagePanel) {
		ReferencesView referencesView = new ReferencesView(referencesModel);
		referencesView.setObserver(new ReferencesObserver(referencesView, measureDetailObserver, messagePanel));
		return referencesView;
	}

	private GeneralInformationView buildGeneralMeasureInformationView(boolean isComposite, GeneralInformationModel generalInformationModel) {
		final GeneralInformationView generalInformationView = new GeneralInformationView(isComposite, generalInformationModel);
    MatContext.get().setVisible(generalInformationView.getGenerateEMeasureIDButton(),
				MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.CMS_ID_GENERATION));
		final GeneralInformationObserver observer = new GeneralInformationObserver(generalInformationView);
		generalInformationView.setObserver(observer);
		return generalInformationView;
	}

	private MeasureStewardView buildMeasureStewardView(MeasureStewardDeveloperModel measureStewardDeveloperModel) {
		MeasureStewardView view = new MeasureStewardView(measureStewardDeveloperModel); 
		MeasureStewardDeveloperObserver observer = new MeasureStewardDeveloperObserver();
		view.setObserver(observer);
		observer.setView(view);
		return view; 
	}

	private MeasureTypeView buildMeasureTypeView(MeasureTypeView view, MeasureTypeObserver observer) {
		view.setObserver(observer);
		observer.setView(view);
		return view; 
	}

	private MeasureDetailViewInterface buildRichTextEditorView(MeasureDetailsTextAbstractModel model, MeasureDetailViewInterface view, MeasureDetailsComponentObserver observer) {
		view.setMeasureDetailsComponentModel(model);
		observer.setView(view);
		view.setObserver(observer);
		view.buildDetailView();
		return view; 
	}
}
