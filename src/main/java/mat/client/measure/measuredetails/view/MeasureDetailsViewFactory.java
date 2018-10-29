package mat.client.measure.measuredetails.view;

import mat.client.shared.MatDetailItem;
import mat.client.shared.MeasureDetailsConstants;
import mat.client.shared.MeasureDetailsConstants.MeasureDetailsItems;

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
		// TODO Auto-generated method stub
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
			case GENERAL_MEASURE_INFORMATION:
				default:
				return new GeneralMeasureInformationView();
			}
		}
		return new GeneralMeasureInformationView();
	}

}
