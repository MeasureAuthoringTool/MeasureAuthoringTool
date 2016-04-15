package mat.client.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mat.client.clause.cqlworkspace.TimingExpressionObj;
import mat.client.measure.service.MeasureService;
import mat.client.measure.service.MeasureServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;

// TODO: Auto-generated Javadoc
/**
 * The Class JSONCQLTimingExpressionUtility.
 */
public class JSONCQLTimingExpressionUtility {

	/** The measure service. */
	private static MeasureServiceAsync measureService;

	/** The json object map. */
	private static HashMap<String, JSONArray> jsonObjectMap = new HashMap<String, JSONArray>();

	/**
	 * Gets the all attr mode list.
	 * 
	 * @return the all attr mode list
	 */
	public static void getAllCQLTimingExpressionsList() {
		getMeasureService().getJSONObjectFromXML(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(String result) {
				extractJSONObject(result);

			}
		});
	}

	/**
	 * Extract json object.
	 * 
	 * @param jsonString
	 *            the json string
	 */
	@SuppressWarnings("deprecation")
	private static void extractJSONObject(String jsonString) {
		if (jsonString != null) {
			JSONValue jsonValue = JSONParser.parse(jsonString);
			if (jsonValue.isObject() != null) {
				JSONArray attributeJsonArray = (JSONArray) jsonValue.isObject()
						.get("cqlTimingExpressions");
				jsonObjectMap.put("timingExpression", attributeJsonArray);
			}
		}
	}

	/**
	 * Gets the more viable options list.
	 * 
	 * @param timingExpression
	 *            the timing expression
	 * @return the more viable options list
	 */
	public static TimingExpressionObj getAvaialableTimingQualifierList(
			String timingExpression) {
		TimingExpressionObj timingExpressionObj = null;

		List<String> optionList = new ArrayList<String>();
		if (jsonObjectMap.get("timingExpression").isArray() != null) {
			JSONArray arrayObject = jsonObjectMap.get("timingExpression")
					.isArray();
			for (int i = 0; i < arrayObject.size(); i++) {
				JSONObject attrJSONObject = (JSONObject) arrayObject.get(i);
				if (attrJSONObject.get("@name") != null) {

					JSONString attrObject = attrJSONObject.get("@name")
							.isString();
					String timingName = attrObject.toString();
					timingName = timingName.replace("\"", "");
					if (timingName.equalsIgnoreCase(timingExpression)) {

						timingExpressionObj = new TimingExpressionObj();
						timingExpressionObj.setName(timingExpression);
						if (attrJSONObject.get("@quantity") != null) {
							JSONString quantityBoolean = attrJSONObject.get(
									"@quantity").isString();
							String quantity = quantityBoolean.toString()
									.replaceAll("\"", "");
							if (quantity.equals("true")) {
								timingExpressionObj.setQuantity(true);
							} else {
								timingExpressionObj.setQuantity(false);
							}
						}

						if (attrJSONObject.get("@units") != null) {
							JSONString unitsBoolean = attrJSONObject.get(
									"@units").isString();
							String units = unitsBoolean.toString().replaceAll(
									"\"", "");
							if (units.equals("true")) {
								timingExpressionObj.setUnits(true);
							} else {
								timingExpressionObj.setUnits(false);
							}
						}

						if (attrJSONObject.get("@dateTimePrecision") != null) {
							JSONString dateTimePreBoolean = attrJSONObject.get(
									"@dateTimePrecision").isString();
							String dateTimePrecision = dateTimePreBoolean
									.toString().replaceAll("\"", "");
							if (dateTimePrecision.equals("true")) {
								timingExpressionObj.setDateTimePrecesion(true);
							} else {
								timingExpressionObj.setDateTimePrecesion(false);
							}
						}

						if (attrJSONObject.get("@dateTimePrecision") != null) {
							JSONString dateTimePrecisionOffset = attrJSONObject
									.get("@dateTimePrecisionOffset").isString();
							String dateTimePrecisionOffsetStr = dateTimePrecisionOffset
									.toString().replaceAll("\"", "");
							timingExpressionObj
									.setDateTimePrecOffset(dateTimePrecisionOffsetStr);
						}
						if (attrJSONObject.get("@option") != null) {
							JSONString moreViableOptionObject = attrJSONObject
									.get("@option").isString();
							String optionName = moreViableOptionObject
									.toString();
							optionName = optionName.replace("\"", "");
							optionList = getOptionList(optionName);
							timingExpressionObj.setOptionList(optionList);
							break;
						} else {
							return timingExpressionObj;
						}
					}

				}
			}
		}

		return timingExpressionObj;
	}

	/**
	 * Gets the option list.
	 * 
	 * @param timingExpression
	 *            the timing expression
	 * @return the option list
	 */
	public static List<String> getOptionList(String timingExpression) {
		List<String> optionList = new ArrayList<String>();
		if (jsonObjectMap.get("timingExpression").isArray() != null) {
			JSONArray arrayObject = jsonObjectMap.get("timingExpression")
					.isArray();
			for (int i = 0; i < arrayObject.size(); i++) {
				JSONObject attrJSONObject = (JSONObject) arrayObject.get(i);
				if(attrJSONObject.get("@ref") != null){
					JSONString attrObject = attrJSONObject.get("@ref").isString();
					String timingName = attrObject.toString();
					timingName = timingName.replace("\"", "");
					if (timingName.equalsIgnoreCase(timingExpression)) {
						if (attrJSONObject.get("option").isArray() != null) {
							JSONArray attrModeObject = attrJSONObject
									.get("option").isArray();
							for (int j = 0; j < attrModeObject.size(); j++) {
								JSONObject optionObject = (JSONObject) attrModeObject
										.get(j);
								JSONString optionStrObject = optionObject.get(
										"@name").isString();
								String optionName = optionStrObject.toString();
								optionName = optionName.replace("\"", "");
								optionList.add(optionName);
							}
						} else if (attrJSONObject.get("option").isObject() != null) {
							JSONObject optionObject = attrJSONObject.get("option")
									.isObject();
							JSONString optionStrObject = optionObject.get("@name")
									.isString();
							String optionName = optionStrObject.toString();
							optionName = optionName.replace("\"", "");
							optionList.add(optionName);
						}
						break;
					}
				}
			}
		}

		return optionList;
	}

	/**
	 * Gets the attr mode list.
	 * 
	 * @return the attr mode list
	 */
	/*
	 * public static TimingExpressionObj getAllCQLTimingExpressionsList(String
	 * timing) { TimingExpressionObj timingExpressionObj = new
	 * TimingExpressionObj(); List<String> modeList = new ArrayList<String>();
	 * if ((timing != null) && (timing != "")) { if
	 * (jsonObjectMap.get("firstLevel").isArray() != null) { JSONArray
	 * arrayObject = jsonObjectMap.get("firstLevel") .isArray(); for (int i = 0;
	 * i < arrayObject.size(); i++) { JSONObject attrJSONObject = (JSONObject)
	 * arrayObject .get(i); JSONString attrObject = attrJSONObject.get(
	 * "@name").isString(); //String dataTypeObject = null; String timingName =
	 * attrObject.toString(); timingName = timingName.replace("\"", ""); if
	 * (timing.equalsIgnoreCase(timingName)) {
	 * 
	 * JSONString unitsBoolean = attrJSONObject.get("@quantity").isString();
	 * String units = unitsBoolean.toString().replaceAll("\"", "");
	 * if(units.equals("true")){ timingExpressionObj.setQuantity(true); } else {
	 * timingExpressionObj.setQuantity(false); }
	 * 
	 * JSONString quanBoolean = attrJSONObject.get("@units").isString(); String
	 * quantity = quanBoolean.toString().replaceAll("\"", "");
	 * if(quantity.equals("true")){ timingExpressionObj.setUnits(true); } else {
	 * timingExpressionObj.setUnits(false); }
	 * 
	 * JSONString dateTimePreBoolean =
	 * attrJSONObject.get("@dateTimePrecision").isString(); String
	 * dateTimePrecision = dateTimePreBoolean.toString().replaceAll("\"", "");
	 * if(dateTimePrecision.equals("true")){
	 * timingExpressionObj.setDateTimePrecesion(true); } else {
	 * timingExpressionObj.setDateTimePrecesion(false); }
	 * 
	 * if (attrJSONObject.get("secondLevel").isArray() != null) { JSONArray
	 * attrModeObject = attrJSONObject.get("secondLevel").isArray();
	 * jsonObjectSecondLevelMap.put("secondLevel", attrModeObject); for (int j =
	 * 0; j < attrModeObject.size(); j++) { JSONObject modeObject = (JSONObject)
	 * attrModeObject.get(j); JSONString modeStrObject = modeObject.get(
	 * "@name").isString(); String modeName = modeStrObject.toString(); modeName
	 * = modeName.replace("\"", ""); // modeName = getAttrMode(modeName);
	 * modeList.add(modeName); } } else if
	 * (attrJSONObject.get("secondLevel").isObject() != null) { JSONObject
	 * modeObject = attrJSONObject.get("secondLevel") .isObject(); JSONString
	 * modeStrObject = modeObject.get( "@name").isString(); String modeName =
	 * modeStrObject.toString(); modeName = modeName.replace("\"", ""); //
	 * modeName = getAttrMode(modeName); modeList.add(modeName); } } } } }
	 * 
	 * timingExpressionObj.setOptionList(modeList);
	 * 
	 * return timingExpressionObj; }
	 */

	public static MeasureServiceAsync getMeasureService() {
		if (measureService == null) {
			measureService = (MeasureServiceAsync) GWT
					.create(MeasureService.class);
		}
		return measureService;
	}

}
