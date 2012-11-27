package mat.client.shared;


public class MetaDataConstants {
	
	// database meta data names 
	//label changed from Name to eMeasure Title
	public static final String EMEASURE_TITLE = "Name";
	//label changed from Abbreviated Name to eMeasure Abbreviated Title
	public static final String EMEASURE_ABBR_TITLE ="ShortName";	
	public static final String MEASURE_STATUS ="MeasureStatus";
	//label changed from NQFId to NQFNumber
	public static final String NQF_NUMBER ="NQFId";
	public static final String MEASUREMENT_FROM_PERIOD ="MeasureFromPeriod";
	public static final String MEASUREMENT_TO_PERIOD ="MeasureToPeriod";
	public static final String MEASURE_STEWARD ="MeasureSteward";
	public static final String MEASURE_STEWARD_OTHER ="MeasureStewardOther";	
	//label changed from Author to Measure Developer
	public static final String MEASURE_DEVELOPER ="Author";
	public static final String ENDORSE_BY_NQF ="EndorseByNQF";
	public static final String DESCRIPTION ="Description";
	public static final String COPYRIGHT ="Copyright";
	public static final String MEASURE_SCORING ="MeasureScoring";
	public static final String MEASURE_TYPE ="MeasureType";
	public static final String STRATIFICATION ="Stratification";
	public static final String RATIONALE ="Rationale";
	public static final String CLINICAL_RECOM_STATE ="ClinicalRecomms";
	public static final String IMPROVEMENT_NOTATION ="ImprovNotations";
	//label changed from Reference to References
	public static final String REFERENCES ="Reference";
	//label changed from Definitions to Definition
	public static final String DEFENITION ="Definitions";
	public static final String GUIDANCE ="Guidance";
	//label changed from SetName to Measure Set
	public static final String MEASURE_SET ="SetName";
	public static final String RISK_ADJUSTMENT ="RiskAdjustment";
	public static final String TRANSMISSION_FORMAT = "TransmissionFormat";
	public static final String SUPPLEMENTAL_DATA_ELEMENTS = "SupplementalDataElements";

	//Constants needed for US 577
	//Some will be removed since they are here only for bookkeeping
	public static final String DISCLAIMER = "Disclaimer";
	public static final String RATE_AGGREGATION ="RateAggregation";
	public static final String INITIAL_PATIENT_POP = "InitialPatientPopulation";
	public static final String DENOM = "Denominator";
	public static final String DENOM_EXCL = "DenominatorExclusions";
	public static final String NUM = "Numerator";
	public static final String NUM_EXCL = "NumeratorExclusions";
	public static final String DENOM_EXEP = "DenominatorExeptions";
	public static final String MEASURE_POP = "MeasurePopulation";
	public static final String MEASURE_OBS = "MeasureObservation";
	
	
}
