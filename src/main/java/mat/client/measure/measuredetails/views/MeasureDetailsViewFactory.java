package mat.client.measure.measuredetails.views;

import java.util.List;

import mat.DTO.CompositeMeasureScoreDTO;
import mat.client.measure.measuredetails.observers.CopyrightObserver;
import mat.client.measure.measuredetails.observers.DescriptionObserver;
import mat.client.measure.measuredetails.observers.DisclaimerObserver;
import mat.client.measure.measuredetails.observers.GeneralMeasureInformationObserver;
import mat.client.shared.MatContext;
import mat.client.shared.MatDetailItem;
import mat.client.shared.MeasureDetailsConstants;
import mat.client.shared.MeasureDetailsConstants.MeasureDetailsItems;
import mat.client.shared.MeasureDetailsConstants.PopulationItems;
import mat.shared.measure.measuredetails.models.CopyrightModel;
import mat.shared.measure.measuredetails.models.DescriptionModel;
import mat.shared.measure.measuredetails.models.DisclaimerModel;
import mat.shared.measure.measuredetails.models.GeneralInformationModel;
import mat.shared.measure.measuredetails.models.MeasureDetailsModel;

public class MeasureDetailsViewFactory {
	private static MeasureDetailsViewFactory instance;
	private MeasureDetailsViewFactory() {}

	public static MeasureDetailsViewFactory get() {
		if(instance == null) {
			instance = new MeasureDetailsViewFactory();
		}
		return instance;
	}

	public MeasureDetailViewInterface getMeasureDetailComponentView(MeasureDetailsModel measureDetailsComponent, MatDetailItem currentMeasureDetail) {
		if(currentMeasureDetail instanceof MeasureDetailsConstants.MeasureDetailsItems) {
			switch((MeasureDetailsItems) currentMeasureDetail) {
			case COMPONENT_MEASURES:
				return new ComponentMeasuresView();
			case DESCRIPTION:
				return buildDescriptionView(measureDetailsComponent.getDescriptionModel());
			case DISCLAIMER:
				return buildDisclaimerView(measureDetailsComponent.getDisclaimerModel());
			case MEASURE_TYPE:
				return new MeasureTypeView();
			case STRATIFICATION:
				return new StratificationView();
			case RISK_ADJUSTMENT:
				return new RiskAdjustmentView();
			case RATE_AGGREGATION:
				return new RateAggregationView();
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
				return buildCopyrightView(measureDetailsComponent.getCopyrightModel());
			case POPULATIONS:
				return new PopulationsView();
			case GENERAL_MEASURE_INFORMATION:
			default:
				return buildGeneralMeasureInformationView(measureDetailsComponent.isComposite(), measureDetailsComponent.getGeneralInformationModel());
			}
		} else if ( currentMeasureDetail instanceof MeasureDetailsConstants.PopulationItems) {
			switch((PopulationItems) currentMeasureDetail) {
			case INITIAL_POPULATION:
				return new InitialPopulationView();
			case MEASURE_POPULATION:
				return new MeasurePopulationView();
			case MEASURE_POPULATION_EXCLUSIONS:
				return new MeasurePopulationEclusionsView();
			case DENOMINATOR:
				return new DenominatorView();
			case DENOMINATOR_EXCLUSIONS:
				return new DenominatorExclusionsView();
			case NUMERATOR:
				return new NumeratorView();
			case NUMERATOR_EXCLUSIONS:
				return new NumeratorExclusionsView();
			case DENOMINATOR_EXCEPTIONS:
				return new DenominatorExceptionsView();
			case MEASURE_OBSERVATIONS:
				return new MeasureObservationsView();
			}
		}
		return buildGeneralMeasureInformationView(measureDetailsComponent.isComposite(), measureDetailsComponent.getGeneralInformationModel());
	}
	
	GeneralMeasureInformationView buildGeneralMeasureInformationView(boolean isComposite, GeneralInformationModel generalInformationModel) {
		List<CompositeMeasureScoreDTO> compositeChoices = MatContext.get().buildCompositeScoringChoiceList();
		GeneralMeasureInformationView generalInformationView = new GeneralMeasureInformationView(isComposite, generalInformationModel, compositeChoices);
		GeneralMeasureInformationObserver observer = new GeneralMeasureInformationObserver(generalInformationView);
		generalInformationView.setObserver(observer);
		return generalInformationView;
	}
	
	private DescriptionView buildDescriptionView(DescriptionModel descriptionModel) {
		DescriptionView descriptionView = new DescriptionView(descriptionModel);
		DescriptionObserver descriptionObserver = new DescriptionObserver(descriptionView);
		descriptionView.setObserver(descriptionObserver);
		return descriptionView;
	}
	
	private CopyrightView buildCopyrightView(CopyrightModel copyrightModel) {
		CopyrightView copyrightView = new CopyrightView(copyrightModel);
		CopyrightObserver copyrightObserver = new CopyrightObserver(copyrightView);
		copyrightView.setObserver(copyrightObserver);
		return copyrightView;
	}
	
	private DisclaimerView buildDisclaimerView(DisclaimerModel disclaimerModel) {
		DisclaimerView disclaimerView = new DisclaimerView(disclaimerModel);
		DisclaimerObserver disclaimerObserver = new DisclaimerObserver(disclaimerView);
		disclaimerView.setObserver(disclaimerObserver);
		return disclaimerView;
	}
}
