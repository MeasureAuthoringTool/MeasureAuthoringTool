/**
 * 
 */
package mat.shared;

/**
 * This holds constants used across MAT.
 * These constants are available on client as well as server side.
 */

public interface MatConstants {
	/** The Constant NUMERATOR_EXCLUSIONS. */
	String NUMERATOR_EXCLUSIONS = "numeratorExclusions";
	/** The Constant DENOMINATOR_EXCEPTIONS. */
	String DENOMINATOR_EXCEPTIONS = "denominatorExceptions";
	/** The Constant DENOMINATOR_EXCLUSIONS. */
	String DENOMINATOR_EXCLUSIONS = "denominatorExclusions";
	/** The Constant STRATUM. */
	String STRATUM = "stratum";
	/** The Constant MEASURE_POPULATION_EXCLUSIONS. */
	String MEASURE_POPULATION_EXCLUSIONS = "measurePopulationExclusions";
	/** The Constant INITIAL_POPULATION. */
	String INITIAL_POPULATION = "initialPopulation";
	/** The Constant MEASURE_OBS_POPULATION. */
	String MEASURE_OBS_POPULATION = "measureObservation";
	/** The Constant TAXONOMY. */
	String TAXONOMY = "taxonomy";
	/** The Constant DATATYPE. */
	String DATATYPE = "datatype";
	/** The Constant DATETIMEDIFF. */
	String DATETIMEDIFF = "Datetimediff";
	/** The Constant "FIRST". */
	String FIRST = "First";
	/** The Constant "SECOND". */
	String SECOND = "Second";
	/** The Constant "THIRD". */
	String THIRD = "Third";
	/** The Constant FOURTH. */
	String FOURTH = "Fourth";
	/** The Constant FIFTH. */
	String FIFTH = "Fifth";
	/** The Constant MOST RECENT. */
	String MOST_RECENT = "Most Recent";
	/** The Constant TIMEDIFF. */
	String TIMEDIFF = "TIMEDIFF";
	/** The Constant SUM. */
	String SUM = "Sum";
	/** The Constant MEDIAN. */
	String MEDIAN = "Median";
	/** The Constant MIN. */
	String MIN = "Min";
	/** The Constant MAX. */
	String MAX = "Max";
	/** The Constant DATEDIFF. */
	String DATEDIFF = "DATEDIFF";
	/** The Constant COUNT. */
	String COUNT = "Count";
	/** The Constant AVG. */
	String AVG = "Avg";
	/** The Constant AGE_AT. */
	String AGE_AT = "Age At";
	/** The Constant DATE. */
	String DATE = "date";
	/** The Constant MODE. */
	String MODE = "mode";
	/** The Constant NAME. */
	String NAME = "name";
	/** The Constant NEGATION_RATIONALE. */
	String NEGATION_RATIONALE = "negation rationale";
	/** The Constant ATTRIBUTE. */
	String ATTRIBUTE = "attribute";
	/** The Constant OCCURRENCE. */
	String OCCURRENCE = "Occurrence";
	/** The Constant UNIT. */
	String UNIT = "unit";
	/** The Constant UUID. */
	String UUID = "uuid";
	/** The Constant ASSOCIATED_POPULATION_UUID. */
	String ASSOCIATED_POPULATION_UUID = "associatedPopulationUUID";
	/** The Constant NONE. */
	String NONE = "None";
	/** The Constant CONTINUOUS_VARIABLE. */
	String CONTINUOUS_VARIABLE = "continuous Variable";
	/** The Constant COHORT. */
	String COHORT = "cohort";
	/** The Constant PROPORTION. */
	String PROPORTION = "proportion";
	/** The Constant ITEM_COUNT. */
	String ITEM_COUNT = "itemCount";
	/** The Constant SATISFIES. */
	String SATISFIES = "SATISFIES";
	/** The Constant STRATIFICATION. */
	String STRATIFICATION = "Stratification";
	/** The Constant TRUE. */
	String TRUE = "true";
	/** The Constant AND. */
	String AND = "AND";
	/** The Constant MEASURE_OBSERVATION. */
	String MEASURE_OBSERVATION = "Measure Observation";
	/** The Constant MEASURE_DETAILS. */
	String MEASURE_DETAILS = "measureDetails";
	/** The Constant RATIO. */
	String RATIO = "ratio";
	/** The Constant MEASURE_POPULATION. */
	String MEASURE_POPULATION = "measurePopulation";
	/** The Constant NUMERATOR. */
	String NUMERATOR = "numerator";
	/** The Constant DENOMINATOR. */
	String DENOMINATOR = "denominator";
	/** The Constant TYPE. */
	String TYPE = "type";
	/** The Constant FALSE. */
	String FALSE = "false";
	/** The Constant QDM_VARIABLE. */
	String QDM_VARIABLE = "qdmVariable";
	/** The Constant ID. */
	String ID = "id";
	/** The Constant INSTANCE_OF. */
	String INSTANCE_OF = "instanceOf";
	/** The Constant INSTANCE. */
	String INSTANCE = "instance";
	/** The Constant ELEMENT_LOOK_UP. */
	String ELEMENT_LOOK_UP = "elementLookUp";
	/** The Constant FUNCTIONAL_OP. */
	String FUNCTIONAL_OP = "functionalOp";
	/** The Constant DISPLAY_NAME. */
	String DISPLAY_NAME = "displayName";
	/** The Constant ELEMENT_REF. */
	String ELEMENT_REF = "elementRef";
	/** The Constant RELATIONAL_OP. */
	String RELATIONAL_OP = "relationalOp";
	/** The Constant HTML_LI. */
	String HTML_LI = "li";
	/** The Constant HTML_UL. */
	String HTML_UL = "ul";
	/** The Constant SET_OP. */
	String SET_OP = "setOp";
	/** The Constant SUB_TREE. */
	String SUB_TREE = "subTree";
	/** The Constant COMMENT. */
	String COMMENT = "comment";
	/** The Constant LOGICAL_OP. */
	String LOGICAL_OP = "logicalOp";
	/** The Constant ATTR_UUID. */
	String ATTR_UUID = "attrUUID";
	/** The Constant COMMENT. */
	String SUB_TREE_REF = "subTreeRef";
	/** The Constant SUB_TREE_REF. */
	String EXTENSION = "extension";
	/** The Constant FULFILLS. */
	String FULFILLS = "FULFILLS";
	/** The Constant QUANTITY. */
	String QUANTITY = "quantity";
	/** The Constant ATTR_DATE. */
	String ATTR_DATE = "attrDate";
	/** The Constant COMPARISON_VALUE. */
	String COMPARISON_VALUE = "comparisonValue";
	/** The Constant OPERATOR_TYPE. */
	String OPERATOR_TYPE = "operatorType";
	/** The Constant SATISFIES_ANY. */
	String SATISFIES_ANY = "Satisfies Any";
	/** The Constant SATISFIES_ALL. */
	String SATISFIES_ALL = "Satisfies All";
	/** The Constant starts concurrent with. */
	String STARTS_CONCURRENT_WITH =  "Starts Concurrent With";
	/** The Constant starts concurrent with end of. */
	String STARTS_CONCURRENT_WITH_END_OF = "Starts Concurrent With End Of";
	/** The Constant starts during. */
	String STARTS_DURING = "Starts During";
	/** The Constant ends concurrent with. */
	String ENDS_CONCURRENT_WITH = "Ends Concurrent With";
	/** The Constant ends concurrent with start of. */
	String ENDS_CONCURRENT_WITH_START_OF = "Ends Concurrent With Start Of";
	/** The Constant ends during. */
	String ENDS_DURING = "Ends During";
	/** The Constant concurrent with. */
	String CONCURRENT_WITH =  "Concurrent With";
	/** The Constant during. */
	String DURING = "During";
	/** The Constant concurrent with. */
	String OVERLAPS =  "Overlaps";
	
	
}
