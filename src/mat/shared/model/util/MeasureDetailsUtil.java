package mat.shared.model.util;

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

}
