package mat.shared.model.util;

import java.util.ArrayList;
import java.util.List;
import mat.shared.ConstantMessages;

/**
 * The Class MeasureDetailsUtil.
 */
public class MeasureDetailsUtil {
	
//	/**
//	 * Gets the measure type abbr.
//	 * 
//	 * @param measureType
//	 *            the measure type
//	 * @return the measure type abbr
//	 */
//	public static String getMeasureTypeAbbr(String measureType){
//		String abbr = "";
//		if(measureType.equalsIgnoreCase("Composite")){
//			abbr = "COMPOSITE";
//		}else if(measureType.equalsIgnoreCase("Cost/Resource Use")){
//			abbr = "COSTRESOURCEUSE";
//		}else if(measureType.equalsIgnoreCase("Efficiency")){
//			abbr = "EFFICIENCY";
//		}else if(measureType.equalsIgnoreCase("Outcome")){
//			abbr = "OUTCOME";
//		}else if(measureType.equalsIgnoreCase("Structure")){
//			abbr = "STRUCTURE";
//		}else if(measureType.equalsIgnoreCase("Patient Engagement/Experience")){
//			abbr = "PATENGEXP";
//		}else if(measureType.equalsIgnoreCase("Process")){
//			abbr = "PROCESS";
//		}
//		return abbr;
//	}
	
	/**
	 * Gets the scoring abbr.
	 * 
	 * @param scoring
	 *            the scoring
	 * @return the scoring abbr
	 */
	public static String getScoringAbbr(String scoring) {
		String abbr = "";
		if (scoring.equalsIgnoreCase(ConstantMessages.CONTINUOUS_VARIABLE_SCORING)) {
			abbr = "CONTVAR";
		} else if (scoring.equalsIgnoreCase(ConstantMessages.PROPORTION_SCORING)) {
			abbr = "PROPOR";
		} else if (scoring.equalsIgnoreCase(ConstantMessages.RATIO_SCORING)) {
			abbr = "RATIO";
		} else if (scoring.equalsIgnoreCase(ConstantMessages.COHORT_SCORING)) {
			abbr = "COHORT";
		}
		return abbr;
	}
	/**
	 * Gets the trimmed list.
	 * 
	 * @param listA
	 *            the list a
	 * @return the trimmed list
	 */
	public static List<String> getTrimmedList(List<String> listA){
		ArrayList<String> newAList = new ArrayList<String>();
		if((listA != null) && (listA.size() > 0)){
			for (String aStr : listA) {
				String val = trimToNull(aStr);
				if(null != val){
					newAList.add(val);
				}
			}
		}
		return newAList;
	}
	
	/**
	 * Trim to null.
	 * 
	 * @param value
	 *            the value
	 * @return the string
	 */
	private static String trimToNull(String value){
		if(null != value){
			value = value.replaceAll("[\r\n]", "");
			value = value.equals("") ? null : value.trim();
			
		}
		return value;
	}
	
}
