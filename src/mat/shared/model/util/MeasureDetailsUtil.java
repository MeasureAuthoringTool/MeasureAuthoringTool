package mat.shared.model.util;

import java.util.ArrayList;
import java.util.List;

import mat.shared.ConstantMessages;

public class MeasureDetailsUtil {
	
	/**
	 * @param measureType
	 * @return
	 */
	public static String getMeasureTypeAbbr(String measureType){
		String abbr = "";
		if(measureType.equalsIgnoreCase("Composite")){
			abbr = "COMPOSITE";
		}else if(measureType.equalsIgnoreCase("Cost/Resource Use")){
			abbr = "COSTRESOURCEUSE";
		}else if(measureType.equalsIgnoreCase("Efficiency")){
			abbr = "EFFICIENCY";
		}else if(measureType.equalsIgnoreCase("Outcome")){
			abbr = "OUTCOME";
		}else if(measureType.equalsIgnoreCase("Structure")){
			abbr = "STRUCTURE";
		}else if(measureType.equalsIgnoreCase("Patient Engagement/Experience")){
			abbr = "PATENGEXP";
		}else if(measureType.equalsIgnoreCase("Process")){
			abbr = "PROCESS";
		}
		return abbr;
	}
	
	/**
	 * @param scoring
	 * @return
	 */
	public static String getScoringAbbr(String scoring){
		String abbr = "";
		if(scoring.equalsIgnoreCase(ConstantMessages.CONTINUOUS_VARIABLE_SCORING)){
			abbr = "CONTVAR";
		}else if(scoring.equalsIgnoreCase(ConstantMessages.PROPORTION_SCORING)){
			abbr = "PROPOR";
		}else if(scoring.equalsIgnoreCase(ConstantMessages.RATIO_SCORING)){
			abbr = "RATIO";
		}
		return abbr;
	}
	
	public static List<String> getTrimmedList(List<String> listA){
		ArrayList<String> newAList = new ArrayList<String>();
		if(listA != null && listA.size() > 0){
			for (String aStr : listA) {
				String val = trimToNull(aStr);
				if(null != val){
					newAList.add(val);
				}
			}
		}
		return newAList;
	}

	private static String trimToNull(String value){
		if(null != value){
			value = value.replaceAll("[\r\n]", "");
			value = value.equals("") ? null : value.trim();
			
		}
		return value;
	}

}
