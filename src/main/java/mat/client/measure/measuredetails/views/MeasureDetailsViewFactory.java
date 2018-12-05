package mat.client.measure.measuredetails.views;

import mat.client.measure.measuredetails.observers.CopyrightObserver;
import mat.client.measure.measuredetails.observers.DenominatorExceptionsObserver;
import mat.client.measure.measuredetails.observers.DenominatorExclusionsObserver;
import mat.client.measure.measuredetails.observers.DenominatorObserver;
import mat.client.measure.measuredetails.observers.DescriptionObserver;
import mat.client.measure.measuredetails.observers.DisclaimerObserver;
import mat.client.measure.measuredetails.observers.GeneralInformationObserver;
import mat.client.measure.measuredetails.observers.InitialPopulationObserver;
import mat.client.measure.measuredetails.observers.MeasureDetailsComponentObserver;
import mat.client.measure.measuredetails.observers.MeasureObservationsObserver;
import mat.client.measure.measuredetails.observers.MeasurePopulationExclusionsObserver;
import mat.client.measure.measuredetails.observers.MeasurePopulationObserver;
import mat.client.measure.measuredetails.observers.NumeratorExclusionsObserver;
import mat.client.measure.measuredetails.observers.NumeratorObserver;
import mat.client.measure.measuredetails.observers.RateAggregationObserver;
import mat.client.measure.measuredetails.observers.RiskAdjustmentObserver;
import mat.client.measure.measuredetails.observers.StratificationObserver;
import mat.client.shared.MatDetailItem;
import mat.client.shared.MeasureDetailsConstants;
import mat.client.shared.MeasureDetailsConstants.MeasureDetailsItems;
import mat.client.shared.MeasureDetailsConstants.PopulationItems;
import mat.shared.measure.measuredetails.models.GeneralInformationModel;
import mat.shared.measure.measuredetails.models.MeasureDetailsModel;
import mat.shared.measure.measuredetails.models.MeasureDetailsRichTextAbstractModel;

public class MeasureDetailsViewFactory {
	private static MeasureDetailsViewFactory instance;
	private MeasureDetailsViewFactory() {}

	public static MeasureDetailsViewFactory get() {
		if(instance == null) {
			instance = new MeasureDetailsViewFactory();
		}
		return instance;
	}

	public MeasureDetailViewInterface getMeasureDetailComponentView(MeasureDetailsModel measureDetailsModel, MatDetailItem currentMeasureDetail) {
		if(currentMeasureDetail instanceof MeasureDetailsConstants.MeasureDetailsItems) {
			switch((MeasureDetailsItems) currentMeasureDetail) {
			case COMPONENT_MEASURES:
				return new ComponentMeasuresView();
			case DESCRIPTION:
				return buildRichTextEditorView(measureDetailsModel.getDescriptionModel(), new DescriptionView(), new DescriptionObserver());
			case DISCLAIMER:
				return buildRichTextEditorView(measureDetailsModel.getDisclaimerModel(), new DisclaimerView(), new DisclaimerObserver());
			case MEASURE_TYPE:
				return new MeasureTypeView();
			case STRATIFICATION:
				return buildRichTextEditorView(measureDetailsModel.getStratificationModel(), new StratificationView(), new StratificationObserver());
			case RISK_ADJUSTMENT:
				return buildRichTextEditorView(measureDetailsModel.getRiskAdjustmentModel(), new RiskAdjustmentView(), new RiskAdjustmentObserver());
			case RATE_AGGREGATION:
				return buildRichTextEditorView(measureDetailsModel.getRateAggregationModel(), new RateAggregationView(), new RateAggregationObserver());
			case RATIONALE:
				return new RationaleView();
			case CLINICAL_RECOMMENDATION:
				return new ClinicalRecommendationView();
			case IMPROVEMENT_NOTATION:
				return new ImprovementNotationView();
			case REFERENCES:
				return new ReferencesView();
			case DEFINITION:
				return new DefinitionView();
			case GUIDANCE:
				return new GuidanceView();
			case TRANSMISSION_FORMAT:
				return new TransmissionFormatView();
			case SUPPLEMENTAL_DATA_ELEMENTS:
				return new SupplementalDataView();
			case MEASURE_SET:
				return new MeasureSetView();
			case STEWARD:
				return new MeasureStewardView();
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
	
	private GeneralInformationView buildGeneralMeasureInformationView(boolean isComposite, GeneralInformationModel generalInformationModel) {
		GeneralInformationView generalInformationView = new GeneralInformationView(isComposite, generalInformationModel);
		GeneralInformationObserver observer = new GeneralInformationObserver(generalInformationView);
		generalInformationView.setObserver(observer);
		return generalInformationView;
	}
	
	private MeasureDetailViewInterface buildRichTextEditorView(MeasureDetailsRichTextAbstractModel model, MeasureDetailViewInterface view, MeasureDetailsComponentObserver observer) {
		view.setMeasureDetailsComponentModel(model);
		observer.setView(view);
		view.setObserver(observer);
		view.buildDetailView();
		return view; 
	}
}
