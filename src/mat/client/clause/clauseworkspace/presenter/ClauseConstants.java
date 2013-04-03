package mat.client.clause.clauseworkspace.presenter;

import java.util.HashMap;
import java.util.Map;

public class ClauseConstants {

	private static Map<String, String> constantsMap = new HashMap<String, String>();
	
	private static Map<String, String> childMap = new HashMap<String, String>();
	
	private static final String[] POPULATIONS = {"initialPatientPopulations", "numerators", "numeratorExclusions", "denominators", "denominatorExclusions", "denominatorExceptions", "measurePopulations"};
	
	private static final String AND = "<logOp type=\"AND\">";
	
	
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
		createDefaultChildren();
		createClauseTypeNodeNameMap();
	}
	
	public static String get(String key){
		return constantsMap.get(key);
	}
	
	private static void createClauseTypeNodeNameMap() {
		childMap.put("strata", "stratum");
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
