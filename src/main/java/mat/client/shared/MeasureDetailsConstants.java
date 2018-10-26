package mat.client.shared;

public class MeasureDetailsConstants {
	public enum MeasureDetailsItems implements MatDetailItem{
		GENERAL_MEASURE_INFORMATION("General Measure Information", "General Measure Information"),
		COMPONENT_MEASURES("Component Measures","Component Measures"),
		STEWARD("Measure Steward/Developer", "Measure Steward and Developer"),
		DESCRIPTION("Description", "Description"),
		COPYRIGHT("Copyright", "Copyright"),
		DISCLAIMER("Disclaimer", "Disclaimer"),
		MEASURE_TYPE("Measure Type", "Measure Type"),
		STRATIFICATION("Stratification", "Stratification"),
		RISK_ADJUSTMENT("Risk Adjustment", "Risk Adjustment"),
		RATE_AGGREGATION("Rate Aggregation", "Rate Aggregation"),
		RATIONALE("Rationale", "Rationale"),
		CLINICAL_RECOMMENDATION("Clinical Recommendation", "Clinical Recommendation"),
		IMPROVEMENT_NOTATION("Improvement Notation", "Improvement Notation"),
		REFERENCES("References", "References"),
		DEFINITION("Definition", "Definition"),
		GUIDANCE("Guidance", "Guidance"),
		TRANSMISSION_FORMAT("Transmission Format", "Transmission Format"),
		POPULATIONS("Populations", "Populations"),
		SUPPLEMENTAL_DATA_ELEMENTS("Supplemental Data/Elements", "Supplemental Data/Elements"),
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
}
