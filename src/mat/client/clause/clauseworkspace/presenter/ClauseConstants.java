package mat.client.clause.clauseworkspace.presenter;

import java.util.HashMap;
import java.util.Map;

public class ClauseConstants{

	private static Map<String, String> constantsMap = new HashMap<String, String>();

	private static Map<String, String> childMap = new HashMap<String, String>();

	private static final String[] POPULATIONS = {"Initial Patient Populations", "Numerators", "Numerator Exclusions", "Denominators", "Denominator Exclusions", "Denominator Exceptions", "Measure Populations"};

	public static final String LOG_OP = "logicalOp";

	public static final String CLAUSE_TYPE = "clause";

	public static final String TYPE="type";

	public static final String DISPLAY_NAME="displayName";

	public static final String AND = "AND";
	public static final String MASTER_ROOT_NODE_POPULATION = "Populations";

	public static final String ROOT_NODES = "|strata|measureObservations|numerators|denominators|numeratorExclusions|denominatorExclusions|denominatorExceptions|initialPatientPopulations|measurePopulations";
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

}
