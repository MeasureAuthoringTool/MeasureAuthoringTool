package mat.client.clause.clauseworkspace.presenter;

import java.util.HashMap;
import java.util.Map;

public class ClauseConstants {

	private static Map<String, String> constantsMap = new HashMap<String, String>();
	
	private static Map<String, String> childMap = new HashMap<String, String>();
	
	private static final String[] POPULATIONS = {"initialPatientPopulations", "numerators", "numeratorExclusions", "denominators", "denominatorExclusions", "denominatorExceptions", "measurePopulations"};
	
	public static final String LOG_OP = "logicalOp";
	
	public static final String STRATA = "strata";
	
	public static final String CLAUSE_TYPE = "clause";
	
	public static final String AND = "AND";
	
	public static final String TYPE_STRATUM = "stratum";
	
	public static final String ROOT_NODES = "|strata|measureObservations|";
	
	
	
	
	
	static{
		constantsMap.put("populations", "Populations");
		constantsMap.put("measureObservations", "Measure Observations");
		constantsMap.put("strata", "Stratification");
		constantsMap.put("initialPatientPopulations", "Initial Patient Populations");
		constantsMap.put("numerators", "Numerators");
		constantsMap.put("numeratorExclusions", "Numerator Exclusions");
		constantsMap.put("denominators", "Denominators");
		constantsMap.put("denominatorExclusions", "Denominator Exclusions");
		constantsMap.put("denominatorExceptions", "Denominator Exceptions");
		constantsMap.put("measurePopulations", "Measure Populations");
		constantsMap.put("stratum", "Stratum");
		constantsMap.put("Stratification", "strata");
		constantsMap.put("Measure Observations", "measureObservations");
		createDefaultChildren();
		createClauseTypeNodeNameMap();
	}
	
	public static String get(String key){
		return constantsMap.get(key);
	}
	
	private static void createClauseTypeNodeNameMap() {
		childMap.put("strata", "stratum");
		childMap.put("measureObservations", "measureObservation");
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
	
	private static void createDefaultChildren(){
		for (int i = 0; i < POPULATIONS.length; i++) {
			createTitleWithDefaultNodeName(POPULATIONS[i]);
		}
		createTitleWithDefaultNodeName("measureObservations");
		createTitleWithDefaultNodeName("stratification");
	}
	
	private static void createTitleWithDefaultNodeName(String nodeName){
		String key = nodeName + "" + 1;
		String val = ClauseConstants.get(nodeName);
		if(nodeName.lastIndexOf("s") == nodeName.length() -1){
			val =  ClauseConstants.get(nodeName).substring(0, ClauseConstants.get(nodeName).length() - 1);
		}
		String value = val + "" + 1;
		constantsMap.put(key, value);
	}
}
