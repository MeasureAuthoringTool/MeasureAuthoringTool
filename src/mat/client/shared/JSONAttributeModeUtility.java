package mat.client.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mat.client.clause.QDSAttributesService;
import mat.client.clause.QDSAttributesServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;

// TODO: Auto-generated Javadoc
/**
 * The Class JSONAttributeModeUtility.
 */
public class JSONAttributeModeUtility {
	
	/** The qds attributes service. */
	private static QDSAttributesServiceAsync qdsAttributesService;
	
	/** The json object. */
	//	private static JSONObject jsonObject;
	private static HashMap<String, JSONArray> jsonObjectMap = new HashMap<String, JSONArray>();
	
	/** The Constant GREATER_THAN_OR_EQUAL_TO. */
	private static final String GREATER_THAN_OR_EQUAL_TO = "Greater Than Or Equal To";
	
	/** The Constant LESS_THAN_OR_EQUAL_TO. */
	private static final String LESS_THAN_OR_EQUAL_TO = "Less Than Or Equal To";
	
	/** The Constant EQUAL_TO. */
	private static final String EQUAL_TO = "Equal To";
	
	/** The Constant GREATER_THAN. */
	private static final String GREATER_THAN = "Greater Than";
	
	/** The Constant LESS_THAN. */
	private static final String LESS_THAN = "Less Than";
	
	/** The Constant VALUE_SET. */
	private static final String VALUE_SET = "Value Set";
	
	/** The Constant CHECK_IF_PRESENT. */
	private static final String CHECK_IF_PRESENT = "Check if Present";
	
	/**
	 * Gets the all attr mode list.
	 *
	 * @return the all attr mode list
	 */
	public static void getAllAttrModeList() {
		getQdsAttributesService().getJSONObjectFromXML(
				new AsyncCallback<String>() {
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
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
	 * @param jsonString the json string
	 */
	@SuppressWarnings("deprecation")
	private static void extractJSONObject(String jsonString) {
		if (jsonString != null) {
			JSONValue jsonValue = JSONParser.parse(jsonString);
			if (jsonValue.isObject() != null) {
				JSONArray attributeJsonArray = (JSONArray) jsonValue.isObject().get(
						"matattributes");
				jsonObjectMap.put("attribute", attributeJsonArray);
			}
		}
	}
	/**
	 * Gets the attr mode list.
	 *
	 * @param attrName the attr name
	 * @param dataType String qdmDataType.
	 * @return the attr mode list
	 */
	public static List<String> getAttrModeList(String attrName) {
		List<String> modeList = new ArrayList<String>();
		if ((attrName != null) && (attrName != "")) {
			if (jsonObjectMap.get("attribute").isArray() != null) {
				JSONArray arrayObject = jsonObjectMap.get("attribute")
						.isArray();
				for (int i = 0; i < arrayObject.size(); i++) {
					JSONObject attrJSONObject = (JSONObject) arrayObject
							.get(i);
					JSONString attrObject = attrJSONObject.get(
							"@attribName").isString();
					//String dataTypeObject = null;
					String attributeName = attrObject.toString();
					attributeName = attributeName.replace("\"", "");
					if (attrName.equalsIgnoreCase(attributeName)) {
						if (attrJSONObject.get("mode").isArray() != null) {
							JSONArray attrModeObject = attrJSONObject.get("mode").isArray();
							for (int j = 0; j < attrModeObject.size(); j++) {
								JSONObject modeObject = (JSONObject) attrModeObject.get(j);
								JSONString modeStrObject = modeObject.get(
										"@mode").isString();
								String modeName = modeStrObject.toString();
								modeName = modeName.replace("\"", "");
								modeName = getAttrMode(modeName);
								modeList.add(modeName);
							}
						} else if (attrJSONObject.get("mode").isObject() != null) {
							JSONObject modeObject = attrJSONObject.get("mode")
									.isObject();
							JSONString modeStrObject = modeObject.get(
									"@mode").isString();
							String modeName = modeStrObject.toString();
							modeName = modeName.replace("\"", "");
							modeName = getAttrMode(modeName);
							modeList.add(modeName);
						}
					}
				}
			}
		}
		return modeList;
	}
	
	/**
	 * Validate qdm attribute.
	 *
	 * @param attrName the attr name
	 * @param attrMode the attr mode
	 * @return true, if successful
	 */
	/*public static boolean validateQDMAttribute(String attrName, String attrMode){
		List<String> modeList = getAttrModeList(attrName,null);
		for(int i =0; i<modeList.size(); i++){
			if(modeList.get(i).equals(attrMode)){
				return true;
			}
		}
		return false;
	}*/
	
	/*private static boolean findDataType(String dataType, JSONArray jsonArray) {
		boolean foundInArray = false;
		if (jsonArray.isArray() != null) {
			for (int i = 0; i < jsonArray.size(); i++) {
				String dataTypeInArray = (jsonArray.get(i).isString()).toString();
				dataTypeInArray = dataTypeInArray.replace("\"", "");
				System.out.println("array " + dataTypeInArray);
				if(dataType.equalsIgnoreCase(dataTypeInArray)){
					foundInArray = true;
					break;
				}
			}
		}
		return foundInArray;
		
	}*/
	private static  String getAttrMode(String mode){
		String attrMode="";
		if(mode.equals("IsPresent")){
			attrMode = CHECK_IF_PRESENT;
		} else if(mode.equals("ValueSet")){
			attrMode = VALUE_SET;
		} else if(mode.equals("Equals")){
			attrMode = EQUAL_TO;
		} else if(mode.equals("GreaterThan")){
			attrMode = GREATER_THAN;
		} else if(mode.equals("GreaterThanEqualTo")){
			attrMode = GREATER_THAN_OR_EQUAL_TO;
		} else if(mode.equals("LessThan")){
			attrMode = LESS_THAN;
		} else if(mode.equals("LessThanEqualTo")){
			attrMode = LESS_THAN_OR_EQUAL_TO;
		}
		
		return attrMode;
	}
	
	/**
	 * Gets the qds attributes service.
	 *
	 * @return the qds attributes service
	 */
	public static QDSAttributesServiceAsync getQdsAttributesService() {
		if (qdsAttributesService == null) {
			qdsAttributesService = (QDSAttributesServiceAsync) GWT
					.create(QDSAttributesService.class);
		}
		return qdsAttributesService;
	}
}
