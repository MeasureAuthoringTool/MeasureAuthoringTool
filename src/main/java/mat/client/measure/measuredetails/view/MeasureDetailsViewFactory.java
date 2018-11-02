package mat.client.measure.measuredetails.view;

import mat.client.shared.MatDetailItem;
import mat.client.shared.MeasureDetailsConstants;
import mat.client.shared.MeasureDetailsConstants.MeasureDetailsItems;
import mat.client.shared.MeasureDetailsConstants.PopulationItems;

public class MeasureDetailsViewFactory {
	private static MeasureDetailsViewFactory instance;
	private MeasureDetailsViewFactory() {}

	public static MeasureDetailsViewFactory get() {
		if(instance == null) {
			instance = new MeasureDetailsViewFactory();
		}
		return instance;
	}

	public ComponentDetailView getMeasureDetailComponentView(MatDetailItem currentMeasureDetail) {
		if(currentMeasureDetail instanceof MeasureDetailsConstants.MeasureDetailsItems) {
			switch((MeasureDetailsItems) currentMeasureDetail) {
			case COMPONENT_MEASURES:
				return new ComponentMeasuresView();
			case DESCRIPTION:
				return new DescriptionView();
			case DISCLAIMER:
				return new DisclaimerView();
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
				return new CopyrightView();
			case POPULATIONS:
				return new PopulationsView();
			case GENERAL_MEASURE_INFORMATION:
			default:
				return new GeneralMeasureInformationView();
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
			default:
				return new GeneralMeasureInformationView();
			}
		}
		return new GeneralMeasureInformationView();
	}
}
