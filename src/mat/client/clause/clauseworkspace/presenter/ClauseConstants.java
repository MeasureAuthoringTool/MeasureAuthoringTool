package mat.client.clause.clauseworkspace.presenter;

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

	public static final String AND = "AND";

	public static final String MASTER_ROOT_NODE_POPULATION = "Populations";

	public static final String ROOT_NODES = "|strata|measureObservations|numerators|denominators|numeratorExclusions|initialPatientPopulations|denominatorExclusions|denominatorExceptions|measurePopulations";
	
	/** Constants added for RighClick SubMenus  */
	
	public static final String[] LOGICAL_OPS = {"AND","OR"};
	
	public static final String RELATIONAL_OP = "relationalOp";
	
	public static Map<String, String> timingOperators;
	
	public static Map<String, Node> elementLookUps;
	
	public static final String ELEMENT_REF = "elementRef";
	
	public static final String ID = "id";
	
	public static final int LABEL_MAX_LENGTH = 30;
	
	public static final String[] FUNCTIONS = {"AVG", "COUNT", "DATEDIFF", "MAX", "MIN", "MEDIAN", "SUM", "TIMEDIFF", "FIRST", "SECOND", "THIRD", "FOURTH", "FIFTH", "MOST RECENT", "NOT"};
	
	public static Map<String, String> functions = new HashMap<String, String>();
	
	public static final String FUNC_NAME = "functionalOp"; 
		
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



	/**
	 * @param functions the functions to set
	 */
	public static void setFunctions(Map<String, String> functions) {
		ClauseConstants.functions = functions;
	}


}
