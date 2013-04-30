package mat.client.clause.clauseworkspace.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.xml.client.Node;

public class ClauseConstants{

	private static Map<String, String> constantsMap = new HashMap<String, String>();

	private static Map<String, String> childMap = new HashMap<String, String>();

	private static final String[] POPULATIONS = {"Initial Patient Populations", "Numerators", "Numerator Exclusions", "Denominators", "Denominator Exclusions", "Denominator Exceptions", "Measure Populations"};

	public static final String LOG_OP = "logicalOp";

	public static final String CLAUSE_TYPE = "clause";

	public static final String TYPE = "type";

	public static final String DISPLAY_NAME = "displayName";
	
	public static final String OPERATOR_TYPE = "operatorType";
	
	public static final String QUANTITY = "quantity";
	
	public static final String UNIT = "unit";
	
	public static final String AND = "AND";

	public static final String MASTER_ROOT_NODE_POPULATION = "Populations";

	public static final String ROOT_NODES = "|strata|measureObservations|numerators|denominators|numeratorExclusions|initialPatientPopulations|denominatorExclusions|denominatorExceptions|measurePopulations";
	
	/** Constants added for RighClick SubMenus  */
	
	public static final String[] LOGICAL_OPS = {"AND","OR"};
	
	public static final String RELATIONAL_OP = "relationalOp";
	
	public static Map<String, String> timingOperators;
	public static Map<String, String> relAssociationOperators;
	
	public static ArrayList<String> units;
	
	public static final String[] comparisonOperators = {"Less Than","Greater Than","Less Than or Equal To","Greater Than or Equal To","Equal To"};
	
	public static Map<String,String> comparisonOperatorMap = new HashMap<String,String>();
	
	public static Map<String, Node> elementLookUps;
	
	public static final String ELEMENT_REF = "elementRef";
	
	public static final String ID = "id";
	
	public static final int LABEL_MAX_LENGTH = 50;
	
	public static final String[] FUNCTIONS = {"AVG", "COUNT", "DATEDIFF", "MAX", "MIN", "MEDIAN", "SUM", "TIMEDIFF", "FIRST", "SECOND", "THIRD", "FOURTH", "FIFTH", "MOST RECENT", "NOT"};
	
	public static Map<String, String> functions = new HashMap<String, String>();
	
	public static final String FUNC_NAME = "functionalOp"; 
		
	public static final int MINUS_NUMPAD = 109;

	public static final int MINUS_IE = 189;

	public static final int MINUS_FF = 173;

	public static final int PLUS_NUMPAD = 107;

	public static final int PLUS_IE = 187;

	public static final int PLUS_FF = 61;

	public static final int DELETE_DELETE = 46;

	public static final int CUT_X = 88;

	public static final int PASTE_V = 86;

	public static final int COPY_C = 67;
	
	public static final String EXTRA_ATTRIBUTES = "extraAttributes";
	
	static{
		constantsMap.put("populations", "Populations");
		constantsMap.put("measureObservations", "Measure Observations");
		constantsMap.put("strata", "Stratification");
		constantsMap.put("Stratification", "strata");
		constantsMap.put("Measure Observations", "measureObservations");
		constantsMap.put("Initial Patient Populations", "initialPatientPopulations");
		constantsMap.put("Numerators", "numerators");
		constantsMap.put("Denominators", "denominators");
		constantsMap.put("Denominator Exclusions", "denominatorExclusions");
		constantsMap.put("Denominator Exceptions", "denominatorExceptions");
		constantsMap.put("Measure Populations", "measurePopulations");
		constantsMap.put("Numerator Exclusions", "numeratorExclusions");
		constantsMap.put("Populations", "populations");
	}

	static{
		comparisonOperatorMap.put("Less Than","<");
		comparisonOperatorMap.put("Greater Than",">");
		comparisonOperatorMap.put("Less Than or Equal To","<=");
		comparisonOperatorMap.put("Greater Than or Equal To",">=");
		comparisonOperatorMap.put("Equal To","=");
	}
	
	public static String get(String key){
		return constantsMap.get(key);
	}

	public static String getClauseTypeNodeName(String key){
		return childMap.get(key);
	}

	public static String[] getPopulationsChildren(){
		return POPULATIONS;
	}

	public static void put(String key, String value){
		constantsMap.put(key, value);
	}



	/**
	 * @return the timingOperators
	 */
	public static Map<String, String> getTimingOperators() {
		return timingOperators;
	}


	/**
	 * @return the qdmElementLookupNode
	 */
	public static Map<String, Node> getElementLookUps() {
		return elementLookUps;
	}



	/**
	 * @return the functions
	 */
	public static Map<String, String> getFunctions() {
		return functions;
	}



	public static Map<String, String> getRelAssociationOperators() {
		return relAssociationOperators;
	}

	public static void setRelAssociationOperators(
			Map<String, String> relAssociationOperators) {
		ClauseConstants.relAssociationOperators = relAssociationOperators;
	}

	/**
	 * @param functions the functions to set
	 */
	public static void setFunctions(Map<String, String> functions) {
		ClauseConstants.functions = functions;
	}

	public static String[] getComparisonoperators() {
		return comparisonOperators;
	}

	public static ArrayList<String> getUnits() {
		return units;
	}
	
	public static Map<String, String> getComparisonOperatorMap() {
		return comparisonOperatorMap;
	}


}
