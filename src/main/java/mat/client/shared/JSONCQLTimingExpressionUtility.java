package mat.client.shared;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import mat.client.cqlworkspace.TimingExpressionObj;
import mat.client.measure.service.MeasureService;
import mat.client.measure.service.MeasureServiceAsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class JSONCQLTimingExpressionUtility {

	/** The measure service. */
	private static MeasureServiceAsync measureService;

	/** The json object map. */
	private static HashMap<String, JSONArray> jsonObjectMap = new HashMap<>();
	
	private static final String REF = "ref";

	private static final String NAME = "name";
	
	private static final String TRUE = "true";
	
	private static final String OPTION = "option";
	
	private static final String QUANTITY = "quantity";
	
	private static final String TIMING_EXPR = "timingExpression";
	
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
	private static void extractJSONObject(String jsonString) {
		JSONArray attributeJsonArray  = JSONAttributeModeUtility.createJSONArrayFromStr(jsonString, "cqlTimingExpressions", TIMING_EXPR);
		if (attributeJsonArray != null) {
			jsonObjectMap.put(TIMING_EXPR, attributeJsonArray);
		}
	}

	/**
	 * Gets the more viable options list.
	 * 
	 * @param timingExpression
	 *            the timing expression
	 * @return the more viable options list
	 */
	public static TimingExpressionObj getAvaialableTimingQualifierList(String timingExpression) {
		TimingExpressionObj timingExpressionObj = null;

		List<String> optionList = new ArrayList<String>();
		if (jsonObjectMap.get(TIMING_EXPR).isArray() != null) {
			JSONArray arrayObject = jsonObjectMap.get(TIMING_EXPR).isArray();
			for (int i = 0; i < arrayObject.size(); i++) {
				JSONObject attrJSONObject = (JSONObject) arrayObject.get(i);
				if (attrJSONObject.get(NAME) != null) {
					String timingName = JSONAttributeModeUtility.createStrFromJSONValue(NAME, attrJSONObject);
					if (timingName.equalsIgnoreCase(timingExpression)) {
						timingExpressionObj = new TimingExpressionObj();
						timingExpressionObj.setName(timingExpression);
						if (attrJSONObject.get(QUANTITY) != null) {
							String quantity = JSONAttributeModeUtility.createStrFromJSONValue(QUANTITY, attrJSONObject);
							if (quantity.equals(TRUE)) {
								timingExpressionObj.setQuantity(true);
							} else {
								timingExpressionObj.setQuantity(false);
							}
						}

						if (attrJSONObject.get("units") != null) {
							String units = JSONAttributeModeUtility.createStrFromJSONValue("units", attrJSONObject);
							if (units.equals(TRUE)) {
								timingExpressionObj.setUnits(true);
							} else {
								timingExpressionObj.setUnits(false);
							}
						}

						if (attrJSONObject.get("relativeQualifer") != null) {
							String units = JSONAttributeModeUtility.createStrFromJSONValue("relativeQualifer", attrJSONObject);
							if (units.equals(TRUE)) {
								timingExpressionObj.setRelativeQualifier(true);
							} else {
								timingExpressionObj.setRelativeQualifier(false);
							}
						}

						if (attrJSONObject.get("quantityOffsetReq") != null) {
							String units = JSONAttributeModeUtility.createStrFromJSONValue("quantityOffsetReq", attrJSONObject);
							if (units.equals(TRUE)) {
								timingExpressionObj.setQuantityOffsetReq(true);
							} else {
								timingExpressionObj.setQuantityOffsetReq(false);
							}
						}


						if (attrJSONObject.get("dateTimePrecision") != null) {
							String dateTimePrecision = JSONAttributeModeUtility.createStrFromJSONValue("dateTimePrecision", attrJSONObject);
							if (dateTimePrecision.equals(TRUE)) {
								timingExpressionObj.setDateTimePrecesion(true);
							} else {
								timingExpressionObj.setDateTimePrecesion(false);
							}
						}

						if (attrJSONObject.get("dateTimePrecisionOffset") != null) {
							String dateTimePrecisionOffsetStr = JSONAttributeModeUtility.createStrFromJSONValue("dateTimePrecisionOffset", attrJSONObject);
							timingExpressionObj.setDateTimePrecOffset(dateTimePrecisionOffsetStr);
						}
						if (attrJSONObject.get(OPTION) != null) {							
							String optionName = JSONAttributeModeUtility.createStrFromJSONValue(OPTION, attrJSONObject);
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
		List<String> optionList = new ArrayList<>();
		if (jsonObjectMap.get(TIMING_EXPR).isArray() != null) {
			JSONArray arrayObject = jsonObjectMap.get(TIMING_EXPR).isArray();
			for (int i = 0; i < arrayObject.size(); i++) {
				JSONObject attrJSONObject = (JSONObject) arrayObject.get(i);
				if(attrJSONObject.get(REF) != null){
					String timingName = JSONAttributeModeUtility.createStrFromJSONValue(REF, attrJSONObject);
					if (timingName.equalsIgnoreCase(timingExpression)) {
						if (attrJSONObject.get(OPTION).isArray() != null) {
							JSONArray attrModeObject = attrJSONObject.get(OPTION).isArray();
							for (int j = 0; j < attrModeObject.size(); j++) {
								JSONObject optionObject = (JSONObject) attrModeObject.get(j);
								String optionName = JSONAttributeModeUtility.createStrFromJSONValue(NAME, optionObject);
								optionList.add(optionName);
							}
						} else if (attrJSONObject.get(OPTION).isObject() != null) {
							JSONObject optionObject = attrJSONObject.get(OPTION).isObject();
							String optionName = JSONAttributeModeUtility.createStrFromJSONValue(NAME, optionObject);
							optionList.add(optionName);
						}
						break;
					}
				}
			}
		}

		return optionList;
	}

	public static MeasureServiceAsync getMeasureService() {
		if (measureService == null) {
			measureService = GWT.create(MeasureService.class);
		}
		return measureService;
	}

}
