package mat.client.shared;

import mat.shared.ConstantMessages;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MeasureDetailsConstants {
	public static final String PROPORTION = ConstantMessages.PROPORTION_SCORING;
	public static final String RATIO = ConstantMessages.RATIO_SCORING;
	public static final String CONTINUOUS_VARIABLE = ConstantMessages.CONTINUOUS_VARIABLE_SCORING;
	public static final String COHORT = ConstantMessages.COHORT_SCORING;
	
	public enum MeasureDetailsItems implements MatDetailItem{
		GENERAL_MEASURE_INFORMATION("General Measure Information", "General Measure Information"),
		COMPONENT_MEASURES("Component Measures","Component Measures"),
		STEWARD("Measure Steward / Developer", "Measure Steward and Measure Developer"),
		DESCRIPTION("Description", "Description"),
		COPYRIGHT("Copyright", "Copyright"),
		DISCLAIMER("Disclaimer", "Disclaimer"),
		MEASURE_TYPE("Measure Type", "Measure Type"),
		STRATIFICATION("Stratification", "Stratification"),
		RISK_ADJUSTMENT("Risk Adjustment", "Risk Adjustment"),
		RATE_AGGREGATION("Rate Aggregation", "Rate Aggregation"),
		RATIONALE("Rationale", "Rationale"),
		CLINICAL_RECOMMENDATION("Clinical Recommendation", "Clinical Recommendation Statement"),
		IMPROVEMENT_NOTATION("Improvement Notation", "Improvement Notation"),
		REFERENCES("References", "References"),
		DEFINITION("Definition", "Definition"),
		GUIDANCE("Guidance", "Guidance"),
		TRANSMISSION_FORMAT("Transmission Format", "Transmission Format"),
		POPULATIONS("Populations", "Populations"),
		SUPPLEMENTAL_DATA_ELEMENTS("Supplemental Data Elements", "Supplemental Data Elements"),
		MEASURE_SET("Measure Set", "Measure Set");
		
	    private String displayName;
	    private String abbreviatedName;
	    MeasureDetailsItems(String abbreviatedName, String displayName) {
	    	this.abbreviatedName = abbreviatedName;
	        this.displayName = displayName;
	    }

	    public String displayName() {
	        return displayName;
	    }
	    
	    public String abbreviatedName() {
	    	return abbreviatedName;
	    }
	}
	
	public enum PopulationItems implements MatDetailItem {
		INITIAL_POPULATION("Initial Population", "Initial Population", Stream.of(PROPORTION, RATIO, CONTINUOUS_VARIABLE, COHORT).collect(Collectors.toList())),
		MEASURE_POPULATION("Measure Population", "Measure Population", Stream.of(CONTINUOUS_VARIABLE).collect(Collectors.toList())),
		MEASURE_POPULATION_EXCLUSIONS("Measure Population Exclusions", "Measure Population Exclusions", Stream.of(CONTINUOUS_VARIABLE).collect(Collectors.toList())),
		DENOMINATOR("Denominator", "Denominator", Stream.of(PROPORTION, RATIO).collect(Collectors.toList())),
		DENOMINATOR_EXCLUSIONS("Denominator Exclusions", "Denominator Exclusions", Stream.of(PROPORTION, RATIO).collect(Collectors.toList())),
		NUMERATOR("Numerator", "Numerator", Stream.of(PROPORTION, RATIO).collect(Collectors.toList())),
		NUMERATOR_EXCLUSIONS("Numerator Exclusions", "Numerator Exclusions", Stream.of(PROPORTION, RATIO).collect(Collectors.toList())),
		DENOMINATOR_EXCEPTIONS("Denominator Exceptions", "Denominator Exceptions", Stream.of(PROPORTION).collect(Collectors.toList())),
		MEASURE_OBSERVATIONS("Measure Observations", "Measure Observations", Stream.of(CONTINUOUS_VARIABLE, RATIO).collect(Collectors.toList()));
	    private String displayName;
	    private String abbreviatedName;
	    private List<String> measureTypes;
	    
	    PopulationItems(String abbreviatedName, String displayName, List<String> measureTypes) {
			this.abbreviatedName = abbreviatedName;
			this.displayName = displayName;
			this.measureTypes = measureTypes;
		}
		
		@Override
		public String displayName() {
			return "Populations > " + displayName;
		}

		@Override
		public String abbreviatedName() {
			return abbreviatedName;
		}
		
		public List<String> getApplicableMeasureTypes() {
			return measureTypes;
		}
	}
}
