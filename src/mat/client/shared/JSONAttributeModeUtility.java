package mat.client.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import mat.client.clause.QDSAttributesService;
import mat.client.clause.QDSAttributesServiceAsync;
import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;
import mat.client.clause.cqlworkspace.CQLWorkSpaceConstants;
import mat.model.ModeDetailModel;
import mat.model.cql.CQLQualityDataSetDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

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
	private static HashMap<String, JSONArray> jsonModeDetailsMap = new HashMap<String, JSONArray>();
	
	/** The Constant COMPARISON. */
	private static final String COMPARISON = "Comparison";
	
	/** The Constant COMPUTATIVE. */
	private static final String COMPUTATIVE = "Computative";
	
	/** The Constant NULLABLE. */
	private static final String NULLABLE = "Nullable";
	
	/** The Constant VALUE_SET. */
	private static final String VALUE_SET = "Value Sets/Codes";
	
	/** The Constant DATATYPE. */
	static final String DATATYPE = "datatype";
	
	/** The Constant ATTRIBUTE. */
	static final String ATTRIBUTE = "attribute";
	
	/** The Constant UUID. */
	private static final String UUID = "uuid";
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
	 * Gets the all mode details list.
	 *
	 * @return the all mode details list
	 */
	public static void getAllModeDetailsList() {
		getQdsAttributesService().getModeDetailsJSONObjectFromXML(
				new AsyncCallback<String>() {
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onSuccess(String result) {
						extractModeDetailsJSONObject(result);
						
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
	 * Extract json object.
	 *
	 * @param jsonString the json string
	 */
	@SuppressWarnings("deprecation")
	private static void extractModeDetailsJSONObject(String jsonString) {
		if (jsonString != null) {
			try{
				JSONValue jsonValue = JSONParser.parse(jsonString);
				if (jsonValue.isObject() != null) {
					JSONArray modeJsonArray = (JSONArray) jsonValue.isObject().get(
							"modedetails");
					jsonModeDetailsMap.put("mode", modeJsonArray);
				}
				}catch(Exception e){
				  System.out.println("Exception while extracting mode details from XML : "+e.getMessage());
				}  
			
		}
	}
	
	/**
	 * Gets the mode list.
	 *
	 * @param modeName the mode name
	 * @return the mode details list
	 */
	public static List<ModeDetailModel> getModeDetailsList(String modeName) {
		List<ModeDetailModel> modeDetailsList = new ArrayList<ModeDetailModel>();
		if ((modeName != null) && (modeName != "")) {
			if (jsonModeDetailsMap.get("mode").isArray() != null) {
				JSONArray arrayObject = jsonModeDetailsMap.get("mode")
						.isArray();
				for (int i = 0; i < arrayObject.size(); i++) {
					
					JSONObject attrJSONObject = (JSONObject) arrayObject
							.get(i);
					JSONString attrObject = attrJSONObject.get(
							"@modeName").isString();
					//String dataTypeObject = null;
					String mName = attrObject.toString();
					mName = mName.replace("\"", "");
					if (modeName.equalsIgnoreCase(mName)) {
						if(modeName.equalsIgnoreCase("Value Sets/Codes")){
							for(CQLQualityDataSetDTO valSets : MatContext.get().getValueSetCodeQualityDataSetList()){
								ModeDetailModel mode = new ModeDetailModel();
								if(!valSets.getCodeListName().equalsIgnoreCase("Birthdate") && !valSets.getCodeListName().equalsIgnoreCase("Dead")) {
									mode.setModeValue(valSets.getCodeListName());
									if(valSets.getType()!= null)
										mode.setModeName(valSets.getType()+":" +valSets.getCodeListName());
									else 
										mode.setModeName("valueset:"+valSets.getCodeListName());
									modeDetailsList.add(mode);
								}
							}
						} else{
							if (attrJSONObject.get("details").isArray() != null) {
								JSONArray attrModeObject = attrJSONObject.get("details").isArray();
								for (int j = 0; j < attrModeObject.size(); j++) {
									JSONObject modeObject = (JSONObject) attrModeObject.get(j);
									JSONString modeStrObject = modeObject.get(
											"@detail").isString();
									String modeDetail = modeStrObject.toString();
									modeDetail = modeDetail.replace("\"", "");
									ModeDetailModel mode = new ModeDetailModel();
									mode.setModeName(modeDetail);
									mode.setModeValue(modeDetail);
									modeDetailsList.add(mode);
								}
							} /*else if (attrJSONObject.get("details").isObject() != null) {
								JSONObject modeObject = attrJSONObject.get("details")
										.isObject();
								JSONString modeStrObject = modeObject.get(
										"@detail").isString();
								String modeDetail = modeStrObject.toString();
								modeDetail = modeDetail.replace("\"", "");
								modeDetailsList.add(modeDetail);
							}*/
						}
						
					}
				}
			}
		}
		return modeDetailsList;
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
	 * Creates the qdm list box.
	 * 
	 * @return the list box
	 */
	private static List<String> createQdmList() {
		List<String> qdmList = new ArrayList<String>();
		for (Entry<String, Node> qdm : CQLWorkSpaceConstants.getElementLookUpNode()
				.entrySet()) {
			Node qdmNode = qdm.getValue();
			String dataType = qdmNode.getAttributes().getNamedItem(DATATYPE)
					.getNodeValue();
			String uuid = qdmNode.getAttributes().getNamedItem(UUID)
					.getNodeValue();
			if (ATTRIBUTE.equals(dataType)) {
				String result = CQLWorkSpaceConstants.getElementLookUpName().get(uuid).substring(0, CQLWorkSpaceConstants.getElementLookUpName().get(uuid).indexOf(':'));
				qdmList.add(result);
			}
		}
		return qdmList;
	}
	
	public void setupElementLookupQDMNodes(NodeList nodeList) {
		if ((null != nodeList) && (nodeList.getLength() > 0)) {
			NodeList qdms = nodeList.item(0).getChildNodes();
			for (int i = 0; i < qdms.getLength(); i++) {
				if ("qdm".equals(qdms.item(i).getNodeName())) {
					NamedNodeMap namedNodeMap = qdms.item(i).getAttributes();
					String isSupplementData = namedNodeMap.getNamedItem("suppDataElement").getNodeValue();
					if (isSupplementData.equals("false")) { //filter supplementDataElements from elementLookUp
						String name = namedNodeMap.getNamedItem("name").getNodeValue();
						// Prod Issue fixed : qdm name has trailing spaces which is reterived frm VSAC.
						//So QDM attribute dialog box is throwing error in FF.To fix that spaces are removed from start and end.
						name = name.trim();
						//name = name.replaceAll("^\\s+|\\s+$", "");
						String uuid = namedNodeMap.getNamedItem("uuid").getNodeValue();
						if (namedNodeMap.getNamedItem("instance") != null) {
							name = namedNodeMap.getNamedItem("instance").getNodeValue() + " of " + name;
						}
						
						if (namedNodeMap.getNamedItem("datatype") != null) {
							String dataType = namedNodeMap.getNamedItem("datatype").getNodeValue().trim();
							name = name + " : " + namedNodeMap.getNamedItem("datatype").getNodeValue();
							PopulationWorkSpaceConstants.elementLookUpDataTypeName.put(uuid, dataType);
						}
						PopulationWorkSpaceConstants.elementLookUpNode.put(name + "~" + uuid, qdms.item(i));
						PopulationWorkSpaceConstants.elementLookUpName.put(uuid, name);
					}
				}
			}
			
		}
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
		if(mode.equals("Comparison")){
			attrMode = COMPARISON;
		} else if(mode.equals("Computative")){
			attrMode = COMPUTATIVE;
		} else if(mode.equals("Nullable")){
			attrMode = NULLABLE;
		} else if(mode.equals("ValueSets/Codes")){
			attrMode = VALUE_SET;
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
