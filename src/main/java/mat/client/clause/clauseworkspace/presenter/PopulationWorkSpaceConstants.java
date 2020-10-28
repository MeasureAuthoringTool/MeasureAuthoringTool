package mat.client.clause.clauseworkspace.presenter;

import com.google.gwt.xml.client.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class ClauseConstants.
 */
public class PopulationWorkSpaceConstants {
	
	/** The constants map. */
	private static Map<String, String> constantsMap = new HashMap<String, String>();
	
	/** The child map. */
	private static Map<String, String> childMap = new HashMap<String, String>();
	
	/** The Constant POPULATIONS. */
	private static final String[] POPULATIONS = {"Initial Populations", "Numerators", "Numerator Exclusions",
		"Denominators", "Denominator Exclusions", "Denominator Exceptions", "Measure Populations", "Measure Population Exclusions"};
	private static final ArrayList<String> POPULATION_NAME = new ArrayList<String>();
	/** The Constant LOG_OP. */
	public static final String LOG_OP = "logicalOp";
	
	/** The Constant LOG_OP. */
	public static final String SET_OP = "setOp";
	
	/** The Constant LOG_OP. */
	public static final String CLAUSE = "Clause";
	
	/** The Constant CLAUSE_TYPE. */
	public static final String CLAUSE_TYPE = "clause";
	
	/** The Constant CQL Definition Type. */
	public static final String CQL_DEFINITION_TYPE = "cqldefinition";
	
	/** The Constant CQL Function Type */
	public static final String CQL_FUNCTION_TYPE = "cqlfunction";
	
	/** The Constant CQL Aggregate function type */
	public static final String CQL_AGG_FUNCTION_TYPE = "cqlaggfunction";
	
	/** The Constant TYPE. */
	public static final String TYPE = "type";
	
	/** The Constant DISPLAY_NAME. */
	public static final String DISPLAY_NAME = "displayName";
	
	/** The Constant OPERATOR_TYPE. */
	public static final String OPERATOR_TYPE = "operatorType";
	
	/** The Constant QUANTITY. */
	public static final String QUANTITY = "quantity";
	
	/** The Constant UNIT. */
	public static final String UNIT = "unit";
	
	/** The Constant AND. */
	public static final String AND = "AND";
	
	/** The Constant OR. */
	public static final String OR = "OR";
	
	/** The Constant COMMENTS. */
	public static final String COMMENTS = "COMMENT";
	
	/** The Constant MASTER_ROOT_NODE_POPULATION. */
	public static final String MASTER_ROOT_NODE_POPULATION = "Populations";
	
	public static final String ROOT_NODE_POPULATIONS = "populations";
	public static final String ROOT_NODE_MEASURE_OBSERVATIONS = "measureObservations";
	public static final String SCORING_TYPE_PROPORTION = "PROPOR";
	public static final String SCORING_TYPE_RATIO = "RATIO";
	public static final String SCORING_TYPE_CONTINOUS_VARIABLE = "CONTVAR";
	public static final String SCORING_TYPE_COHORT = "COHORT";
	
	/** The Constant MASTER_ROOT_NODE_STRATA. */
	public static final String MASTER_ROOT_NODE_STRATA = "strata";
	
	/** The Constant ROOT_NODES. */
	public static final String ROOT_NODES = "|Stratification|stratification|strata|measureObservations|numerators|denominators|numeratorExclusions"
			+ "|initialPopulations|denominatorExclusions|denominatorExceptions|measurePopulations"
			+ "|measurePopulationExclusions";
	
	/** The constants map. */
	public static Map<String, String> topNodeOperatorMap = new HashMap<String, String>();
	/** Constants added for RighClick SubMenus. */
	
	public static final String[] LOGICAL_OPS = {"AND", "OR"};
	
	/** The Constant RELATIONAL_OP. */
	public static final String RELATIONAL_OP = "relationalOp";
	
	/** The units. */
	public static ArrayList<String> units;
	
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
	
	/** The Definition Names and Function Names. */
	public static Collection<String> defNames;
	public static Collection<String> funcNames;
	
	/** Map for CQL Definitions for this measure */
	public static LinkedHashMap<String, Node> cqlDefinitionLookupNode;
	
	public static LinkedHashMap<String, Node> cqlFunctionLookupNode;
	/**
	 * Sets the sub tree look up node.
	 *
	 * @param subTreeLookUpNode the sub tree look up node
	 */
	public static void setSubTreeLookUpNode(LinkedHashMap<String, Node> subTreeLookUpNode) {
		PopulationWorkSpaceConstants.subTreeLookUpNode = subTreeLookUpNode;
	}
	
	/** The Constant ELEMENT_REF. */
	public static final String ELEMENT_REF = "elementRef";
	
	/** The Constant SubTree_REF. */
	public static final String SUBTREE_REF = "subTreeRef";
	
	/** The Constant ATTRIBUTE. */
	public static final String ATTRIBUTE = "attribute";
	
	/** The Constant ID. */
	public static final String ID = "id";
	
	/** The Constant LABEL_MAX_LENGTH. */
	public static final int LABEL_MAX_LENGTH = 100;
	
	/** The functions. */
	public static Map<String, String> functions = new HashMap<String, String>();
	
	/** The Constant FUNC_NAME. */
	public static final String FUNC_NAME = "functionalOp";
	/**
	 * The Constant for SUBTREE_NAME.
	 */
	public static final String SUBTREE_NAME = "subTree";
	
	/**
	 * The Constant for SUBTREE_NAME.
	 */
	public static final String SUBTREE_ROOT_NAME = "subTrees";
	
	/**
	 * The Constant for COMMENT_NODE_NAME.
	 */
	public static final String COMMENT_NODE_NAME = "comment";
	
	/** The Constant MINUS_NUMPAD. */
	public static final int MINUS_NUMPAD = 109;
	
	/** The Constant MINUS_IE. */
	public static final int MINUS_IE = 189;
	
	/** The Constant MINUS_FF. */
	public static final int MINUS_FF = 173;
	
	/** The Constant PLUS_NUMPAD. */
	public static final int PLUS_NUMPAD = 107;
	
	/** The Constant PLUS_IE. */
	public static final int PLUS_IE = 187;
	
	/** The Constant PLUS_FF. */
	public static final int PLUS_FF = 61;
	
	/** The Constant DELETE_DELETE. */
	public static final int DELETE_DELETE = 46;
	
	/** The Constant CUT_X. */
	public static final int CUT_X = 88;
	
	/** The Constant PASTE_V. */
	public static final int PASTE_V = 86;
	
	/** The Constant COPY_C. */
	public static final int COPY_C = 67;
	
	/** The Constant EXTRA_ATTRIBUTES. */
	public static final String EXTRA_ATTRIBUTES = "extraAttributes";
	
	/** The Constant UUID. */
	public static final String UUID = "uuid";
	
	/** The Constant PACKAGE_CLAUSE_NODE. */
	public static final String PACKAGE_CLAUSE_NODE = "packageClause";
	
	/** The Constant CLAUSE_QDM_VARIABLE. */
	public static final String CLAUSE_QDM_VARIABLE = "qdmVariable";
	
	/** The datatype map. */
	public static Map<String, List<String>> datatypeMap;
	
	public static final String CONTEXT_PATIENT = "Patient";

	public static List<String> AggfuncNames = new ArrayList<String>();
	
	static {
		constantsMap.put("populations", "Populations");
		constantsMap.put("measureObservations", "Measure Observations");
		constantsMap.put("strata", "Stratification");
		constantsMap.put("Stratification", "strata");
		constantsMap.put("Measure Observations", "measureObservations");
		constantsMap.put("Initial Populations", "initialPopulations");
		constantsMap.put("Numerators", "numerators");
		constantsMap.put("Denominators", "denominators");
		constantsMap.put("Denominator Exclusions", "denominatorExclusions");
		constantsMap.put("Denominator Exceptions", "denominatorExceptions");
		constantsMap.put("Measure Populations", "measurePopulations");
		constantsMap.put("Measure Population Exclusions", "measurePopulationExclusions");
		constantsMap.put("Numerator Exclusions", "numeratorExclusions");
		constantsMap.put("Populations", "populations");
		//commented for MAT-4426 in sprint 44
		//topNodeOperatorMap.put("measureobservations", "and");
		topNodeOperatorMap.put("initialpopulations", "and");
		topNodeOperatorMap.put("numerators", "and");
		topNodeOperatorMap.put("denominators", "and");
		topNodeOperatorMap.put("measurepopulations", "and");
		topNodeOperatorMap.put("denominatorexclusions", "or");
		topNodeOperatorMap.put("numeratorexclusions", "or");
		topNodeOperatorMap.put("denominatorexceptions", "or");
		topNodeOperatorMap.put("measurepopulationexclusions", "or");
		
		//Argument Function Names
		AggfuncNames.add("Count");
		AggfuncNames.add("Sum");
		AggfuncNames.add("Average");
		AggfuncNames.add("Sample Standard Deviation");
		AggfuncNames.add("Sample Variance");
		AggfuncNames.add("Population Standard Deviation");
		AggfuncNames.add("Population Variance");
		AggfuncNames.add("Minimum");
		AggfuncNames.add("Maximum");
		AggfuncNames.add("Median");
		AggfuncNames.add("Mode");
	}
	
	/**
	 * Gets the.
	 * 
	 * @param key
	 *            the key
	 * @return the string
	 */
	public static String get(String key) {
		return constantsMap.get(key);
	}
	
	/**
	 * Gets the clause type node name.
	 * 
	 * @param key
	 *            the key
	 * @return the clause type node name
	 */
	public static String getClauseTypeNodeName(String key) {
		return childMap.get(key);
	}
	
	/**
	 * Gets the populations children.
	 * 
	 * @return the populations children
	 */
	public static String[] getPopulationsChildren() {
		String[] populationsCopy = new String[POPULATIONS.length];
		System.arraycopy(POPULATIONS, 0, populationsCopy, 0, POPULATIONS.length);
		return populationsCopy;
	}
	
	/**
	 * Put.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public static void put(String key, String value) {
		constantsMap.put(key, value);
	}
	
	/**
	 * Gets the element look up name.
	 * 
	 * @return the element look up name
	 */
	public static Map<String, String> getElementLookUpName() {
		return elementLookUpName;
	}
	
	/**
	 * Gets the element look up data type name.
	 *
	 * @return the elementLookUpDataTypeName
	 */
	public static Map<String, String> getElementLookUpDataTypeName() {
		return elementLookUpDataTypeName;
	}
	
	/**
	 * Gets the element look up node.
	 * 
	 * @return the element look up node
	 */
	public static Map<String, Node> getElementLookUpNode() {
		return elementLookUpNode;
	}
	
	/**
	 * Gets the sub tree look up name.
	 *
	 * @return the subTreeLookUpName
	 */
	public static Map<String, String> getSubTreeLookUpName() {
		return subTreeLookUpName;
	}
	
	/**
	 * Gets the sub tree look up node.
	 *
	 * @return the subTreeLookUpNode
	 */
	public static Map<String, Node> getSubTreeLookUpNode() {
		return subTreeLookUpNode;
	}
	/**
	 * Gets the functions.
	 * 
	 * @return the functions
	 */
	public static Map<String, String> getFunctions() {
		return functions;
	}
	
	/**
	 * Sets the functions.
	 * 
	 * @param functions
	 *            the functions to set
	 */
	public static void setFunctions(Map<String, String> functions) {
		PopulationWorkSpaceConstants.functions = functions;
	}
	
	/**
	 * Gets the units.
	 * 
	 * @return the units
	 */
	public static ArrayList<String> getUnits() {
		return units;
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
		PopulationWorkSpaceConstants.datatypeMap = datatypeMap;
	}
	public static ArrayList<String> getPopulationName() {
		POPULATION_NAME.clear();
		POPULATION_NAME.add("Initial Populations");
		POPULATION_NAME.add("Numerators");
		POPULATION_NAME.add("Numerator Exclusions");
		POPULATION_NAME.add("Denominators");
		POPULATION_NAME.add("Denominator Exclusions");
		POPULATION_NAME.add("Denominator Exceptions");
		POPULATION_NAME.add("Measure Populations");
		POPULATION_NAME.add("Measure Population Exclusions");
		
		return POPULATION_NAME;
	}
	
	/*public static Map<String, Node> getElementLookUps() {
		return elementLookUps;
	}*/
}
