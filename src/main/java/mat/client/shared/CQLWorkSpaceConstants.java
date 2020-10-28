package mat.client.shared;

import com.google.gwt.xml.client.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CQLWorkSpaceConstants {
	
	public static final String MEASURE_OBSERVATION_CAUTION_MSG = "Caution: Both an aggregate function and a user-defined function are required for a Measure Observation. "
			+ "Removing a function or an aggregate function from your Measure Observation will cause any package groupings containing that "
			+ "Measure Observation to be cleared on the Measure Packager.";
	
	public static final String STRATIFICATION_CAUTION_MSG = "Caution: Removing or invalidating a stratum within a stratification will "
			+ "cause any package groupings containing that stratification to be cleared on the Measure Packager.";
	
	public static final String GENERIC_CAUTION_MSG = "Caution: Removing or invalidating a population will "
			+ "cause any package groupings containing that population to be cleared on the Measure Packager.";
	
	public static String CQL_QDM_DATA_TYPE ="QDM Datatype";
	public static String CQL_FHIR_DATA_TYPE ="FHIR Datatype";
	public static String CQL_OTHER_DATA_TYPE ="Others";
	public static String CQL_TIMING_EXPRESSION ="Build CQL Timing Expression";
	public static String CQL_PRIMARY_TIMING_WITHIN ="within";
	
	public static int VALID_INCLUDE_COUNT =10;
	
	private static final ArrayList<String> AVAILABLE_ITEM_TO_INSERT = new ArrayList<String>();
		
	private static final ArrayList<String> TIMING_PRECISIONS = new ArrayList<String>();
	
	private static final ArrayList<String> QUANTITY_OFFSET_UNITS = new ArrayList<String>();
			
	/** The element look up name. */
	public static Map<String, String> elementLookUpName;
	
	/** The element look up name. */
	public static Map<String, String> elementLookUpDataTypeName;
	
	/** The element look up node. */
	public static Map<String, Node> elementLookUpNode;
	
	/** The element look up name. */
	public static LinkedHashMap<String, String> subTreeLookUpName;
	/** The element look up node. */
	public static LinkedHashMap<String, Node> subTreeLookUpNode;
	
	/** The datatype map. */
	public static Map<String, List<String>> datatypeMap;
	
	/**
	 * @return the elementLookUpName
	 */
	public static Map<String, String> getElementLookUpName() {
		return elementLookUpName;
	}

	/**
	 * @param elementLookUpName the elementLookUpName to set
	 */
	public static void setElementLookUpName(Map<String, String> elementLookUpName) {
		CQLWorkSpaceConstants.elementLookUpName = elementLookUpName;
	}

	/**
	 * @return the elementLookUpDataTypeName
	 */
	public static Map<String, String> getElementLookUpDataTypeName() {
		return elementLookUpDataTypeName;
	}

	/**
	 * @param elementLookUpDataTypeName the elementLookUpDataTypeName to set
	 */
	public static void setElementLookUpDataTypeName(Map<String, String> elementLookUpDataTypeName) {
		CQLWorkSpaceConstants.elementLookUpDataTypeName = elementLookUpDataTypeName;
	}

	/**
	 * @return the elementLookUpNode
	 */
	public static Map<String, Node> getElementLookUpNode() {
		return elementLookUpNode;
	}

	/**
	 * @param elementLookUpNode the elementLookUpNode to set
	 */
	public static void setElementLookUpNode(Map<String, Node> elementLookUpNode) {
		CQLWorkSpaceConstants.elementLookUpNode = elementLookUpNode;
	}

	/**
	 * @return the subTreeLookUpName
	 */
	public static LinkedHashMap<String, String> getSubTreeLookUpName() {
		return subTreeLookUpName;
	}

	/**
	 * @param subTreeLookUpName the subTreeLookUpName to set
	 */
	public static void setSubTreeLookUpName(LinkedHashMap<String, String> subTreeLookUpName) {
		CQLWorkSpaceConstants.subTreeLookUpName = subTreeLookUpName;
	}

	/**
	 * @return the subTreeLookUpNode
	 */
	public static LinkedHashMap<String, Node> getSubTreeLookUpNode() {
		return subTreeLookUpNode;
	}

	/**
	 * Gets the datatype map.
	 *
	 * @return the datatypeMap
	 */
	public static Map<String, List<String>> getDatatypeMap() {
		return datatypeMap;
	}
	
	/**
	 * Sets the datatype map.
	 *
	 * @param datatypeMap the datatype map
	 */
	public static  void setDatatypeMap(Map<String, List<String>> datatypeMap) {
		CQLWorkSpaceConstants.datatypeMap = datatypeMap;
	}
	
	/**
	 * @param subTreeLookUpNode the subTreeLookUpNode to set
	 */
	public static void setSubTreeLookUpNode(LinkedHashMap<String, Node> subTreeLookUpNode) {
		CQLWorkSpaceConstants.subTreeLookUpNode = subTreeLookUpNode;
	}

	public static ArrayList<String> getAvailableItem() {
		AVAILABLE_ITEM_TO_INSERT.clear();
		AVAILABLE_ITEM_TO_INSERT.add("Parameters");
		AVAILABLE_ITEM_TO_INSERT.add("Definitions");
		AVAILABLE_ITEM_TO_INSERT.add("Functions");
		AVAILABLE_ITEM_TO_INSERT.add("Timing");
		AVAILABLE_ITEM_TO_INSERT.add("Pre-Defined Functions");
		AVAILABLE_ITEM_TO_INSERT.add("Applied Value Sets/Codes");
		AVAILABLE_ITEM_TO_INSERT.add("Attributes");
		return AVAILABLE_ITEM_TO_INSERT;
	}	
	
	public static ArrayList<String> getTimingPrecisions() {
		TIMING_PRECISIONS.clear();
		TIMING_PRECISIONS.add("day");
		TIMING_PRECISIONS.add("hour");
		TIMING_PRECISIONS.add("millisecond");
		TIMING_PRECISIONS.add("minute");
		TIMING_PRECISIONS.add("month");
		TIMING_PRECISIONS.add("second");
		TIMING_PRECISIONS.add("year");
		return TIMING_PRECISIONS;
	}	
	
	public static ArrayList<String> getQuantityOffsetUnits() {
		QUANTITY_OFFSET_UNITS.clear();
		QUANTITY_OFFSET_UNITS.add("day");
		QUANTITY_OFFSET_UNITS.add("days");
		QUANTITY_OFFSET_UNITS.add("hour");
		QUANTITY_OFFSET_UNITS.add("hours");
		QUANTITY_OFFSET_UNITS.add("millisecond");
		QUANTITY_OFFSET_UNITS.add("milliseconds");
		QUANTITY_OFFSET_UNITS.add("minute");
		QUANTITY_OFFSET_UNITS.add("minutes");
		QUANTITY_OFFSET_UNITS.add("month");
		QUANTITY_OFFSET_UNITS.add("months");
		QUANTITY_OFFSET_UNITS.add("second");
		QUANTITY_OFFSET_UNITS.add("seconds");
		QUANTITY_OFFSET_UNITS.add("year");
		QUANTITY_OFFSET_UNITS.add("years");
		return QUANTITY_OFFSET_UNITS;
	}
	
	
	public static final String CQL_COMPONENTS_MENU = "components";
	public final static String CQL_FUNCTION_MENU = "func";
	public final static String CQL_VIEW_MENU = "view";
	public final static String CQL_DEFINE_MENU = "define";
	public final static String CQL_PARAMETER_MENU = "param";
	public final static String CQL_GENERAL_MENU = "general";
	public final static String CQL_CODES = "codes";
	public final static String CQL_APPLIED_QDM = "qdm";
	public final static String CQL_DEFAULT_MEASUREMENTPERIOD_PARAMETER_NAME = "Measurement Period";
	public final static String CQL_DEFAULT_MEASUREMENTPERIOD_PARAMETER_LOGIC = "Interval<DateTime>";
	public static final String CQL_DEFAULT_DEFINITON_CONTEXT = "Patient";
	public static final String CQL_INCLUDES_MENU = "include";
	public static final String CQL_ATTRIBUTE_RESULT = "result";
	public static final String CQL_ATTRIBUTE_TARGET_OUTCOME = "targetOutcome";
	public static final String CQL_INSERT_AS_CODE_IN = " as Code in ";
	public static final String CQL_INSERT_AS_CODE = " ~ ";
	public static final String CQL_INSERT_IN = " in ";
	public static final String CQL_EQUALS = " = ";
	public static final String CQL_CODE_EQUALS = " ~ "; 
	public static final String CQL_DRAFT = "Draft";
	public static final String CQL_CODE = "CODE:";
	
	public static final String CQL_INITIALPOPULATION = "initialpopulation";
	public static final String CQL_NUMERATOR = "numerator";
	public static final String CQL_DENOMINATOR = "denominator";
	public static final String CQL_NUMERATOREXCLUSIONS = "numeratorexclusions";
	public static final String CQL_DENOMINATOREXCLUSIONS = "denominatorexclusions";
	public static final String CQL_DENOMINATOREXCEPTIONS = "denominatorexceptions";
	public static final String CQL_MEASUREPOPULATIONS = "measurepopulations";
	public static final String CQL_MEASUREPOPULATIONEXCLUSIONS = "measurepopulationexclusions";
	public static final String CQL_STRATIFICATIONS = "stratifications";
	public static final String CQL_MEASUREOBSERVATIONS = "measureobservations";
	public static final String CQL_VIEWPOPULATIONS = "viewpopulations";
	public static final String CQL_STRATUM = "Stratum";
	public static final String SCORING = "scoring";
	public static final String PATIENT_BASED_INDICATOR = "patientBasedIndicator";
	
	public enum POPULATIONS {
		INITIAL_POPULATIONS("Initial Populations"),
	    NUMERATORS("Numerators"),
	    DENOMINATORS("Denominators"),
	    NUMERATOR_EXCLUSIONS("Numerator Exclusions"),
	    DENOMINATOR_EXCLUSIONS("Denominator Exclusions"),
	    DENOMINATOR_EXCEPTIONS("Denominator Exceptions"),
	    MEASURE_POPULATIONS("Measure Populations"),
	    MEASURE_POPULATION_EXCLUSIONS("Measure Population Exclusions"),
	    STRATIFICATION("Stratification"),
	    MEASURE_OBSERVATIONS("Measure Observations"),
	    VIEW_POPULATIONS("View Populations");

	    private String popName;

	    POPULATIONS(String popName) {
	        this.popName = popName;
	    }

	    public String popName() {
	        return popName;
	    }
	}
}
