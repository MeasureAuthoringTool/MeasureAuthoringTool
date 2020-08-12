package mat.client.shared;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.NodeList;
import mat.client.clause.QDSAttributesService;
import mat.client.clause.QDSAttributesServiceAsync;
import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;
import mat.model.ModeDetailModel;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.shared.CQLIdentifierObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class JSONAttributeModeUtility {
	private static QDSAttributesServiceAsync qdsAttributesService;
	// QDM Attribute Modes from SimplifiedAttributePatterns.xml
	private static HashMap<String, JSONArray> jsonObjectMap = new HashMap<>();
	// QDM Attribute Mode details from ModelDetails.xml
	private static HashMap<String, JSONArray> jsonModeDetailsMap = new HashMap<>();
	private static final String COMPARISON = "Comparison";
	private static final String COMPUTATIVE = "Computative";
	private static final String NULLABLE = "Nullable";
	private static final String VALUE_SET = "Value Sets";
	private static final String CODES = "Codes";
	private static final String DATATYPE = "datatype";
	private static final String ATTRIBUTE = "attribute";
	private static final String MODE = "mode";

	public static void getAllAttrModeList() {
		getQdsAttributesService().getJSONObjectFromXML(
				new AsyncCallback<String>() {
					
					@Override
					public void onFailure(Throwable caught) {
						// Do nothing
					}
					
					@Override
					public void onSuccess(String result) {
						extractJSONObject(result);
					}
				});
	}
	
	public static void getAllModeDetailsList() {
		getQdsAttributesService().getModeDetailsJSONObjectFromXML(
				new AsyncCallback<String>() {
					
					@Override
					public void onFailure(Throwable caught) {
						// Do nothing
					}
					
					@Override
					public void onSuccess(String result) {
						extractModeDetailsJSONObject(result);
					}
				});
	}
	
	private static void extractJSONObject(String jsonString) {
		JSONArray attributeJsonArray  = createJSONArrayFromStr(jsonString, "matattributes", ATTRIBUTE);
		if (attributeJsonArray != null) {
			jsonObjectMap.put(ATTRIBUTE, attributeJsonArray);
		}
	}
	
	private static void extractModeDetailsJSONObject(String jsonString) {
		JSONArray modeJsonArray  = createJSONArrayFromStr(jsonString, "modedetails", MODE);
		if (modeJsonArray != null) {
			jsonModeDetailsMap.put(MODE, modeJsonArray);
		}
	}
	
	public static List<ModeDetailModel> getModeDetailsList(String modeName) {
		List<ModeDetailModel> measureModeDetailsList = new ArrayList<>();
		List<ModeDetailModel> includeModeDetailsList = new ArrayList<>();
		if ((modeName != null) && (modeName != "")) {
			if (jsonModeDetailsMap.get(MODE).isArray() != null) {
				JSONArray arrayObject = jsonModeDetailsMap.get(MODE).isArray();
				for (int i = 0; i < arrayObject.size(); i++) {
					
					JSONObject attrJSONObject = (JSONObject) arrayObject.get(i);
					String mName = createStrFromJSONValue("modeName", attrJSONObject);
					if (modeName.equalsIgnoreCase(mName)) {
						if(modeName.equalsIgnoreCase(VALUE_SET)){
							for(CQLQualityDataSetDTO valSets : MatContext.get().getValueSetCodeQualityDataSetList()){
								ModeDetailModel mode = new ModeDetailModel();
								if(!valSets.getName().equals("Birthdate") && !valSets.getName().equals("Dead")) {
									mode.setModeValue(formatModeValue(valSets.getName()));
									if(valSets.getType()== null) {
										mode.setModeName("valueset:\""+valSets.getName() + "\"");
										measureModeDetailsList.add(mode);
									}
								}
							}
							getIncludesList(MatContext.get().getIncludedValueSetNames(), includeModeDetailsList, "valueset:");
						} else if(modeName.equalsIgnoreCase(CODES)){
							for(CQLQualityDataSetDTO valSets : MatContext.get().getValueSetCodeQualityDataSetList()){
								ModeDetailModel mode = new ModeDetailModel();
								if(valSets.getType()!= null) {
									mode.setModeValue(formatModeValue(valSets.getDisplayName()));
									mode.setModeName(valSets.getType()+":\"" +valSets.getDisplayName() + "\"");
									measureModeDetailsList.add(mode);
								}
							}
							getIncludesList(MatContext.get().getIncludedCodeNames(), includeModeDetailsList, "code:");
						} else{
							if (attrJSONObject.get("details").isArray() != null) {
								JSONArray attrModeObject = attrJSONObject.get("details").isArray();
								for (int j = 0; j < attrModeObject.size(); j++) {
									JSONObject modeObject = (JSONObject) attrModeObject.get(j);
									String modeDetail = createStrFromJSONValue("detail", modeObject);
									ModeDetailModel mode = new ModeDetailModel();
									mode.setModeName(modeDetail);
									mode.setModeValue(modeDetail);
									measureModeDetailsList.add(mode);
								}
							}
						}
					}
				}
			}
		}
		List<ModeDetailModel> modeDetailsList = new LinkedList<>();
		modeDetailsList.addAll(sortIdentifierList(measureModeDetailsList));
		modeDetailsList.addAll(sortIdentifierList(includeModeDetailsList));
		return modeDetailsList;
	}
	
	private static Collection<? extends ModeDetailModel> sortIdentifierList(List<ModeDetailModel> modeDetailsList) {
		modeDetailsList.sort((ModeDetailModel modeDetailModel1, ModeDetailModel modeDetailModel2) -> modeDetailModel1.getModeValue().compareToIgnoreCase(modeDetailModel2.getModeValue()));
		return modeDetailsList;
	}

	public static List<String> getAttrModeList(String attrName) {
		List<String> modeList = new ArrayList<>();
		if (attrName != null && !attrName.trim().isEmpty()) {
			if (jsonObjectMap.get(ATTRIBUTE).isArray() != null) {
				JSONArray arrayObject = jsonObjectMap.get(ATTRIBUTE).isArray();
				for (int i = 0; i < arrayObject.size(); i++) {
					JSONObject attrJSONObject = (JSONObject) arrayObject.get(i);
					String attributeName = createStrFromJSONValue("attribName", attrJSONObject);
					if (attrName.equalsIgnoreCase(attributeName)) {
						if (attrJSONObject.get(MODE).isArray() != null) {
							JSONArray attrModeObject = attrJSONObject.get(MODE).isArray();
							for (int j = 0; j < attrModeObject.size(); j++) {
								JSONObject modeObject = (JSONObject) attrModeObject.get(j);
								String modeName = createStrFromJSONValue(MODE, modeObject);
								modeName = getAttrMode(modeName);
								modeList.add(modeName);
							}
						} else if (attrJSONObject.get(MODE).isObject() != null) {
							JSONObject modeObject = attrJSONObject.get(MODE).isObject();
							String modeName = createStrFromJSONValue(MODE, modeObject);
							modeName = getAttrMode(modeName);
							modeList.add(modeName);
						}
					}
				}
			}
		}
		return modeList;
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
						name = name.trim();
						String uuid = namedNodeMap.getNamedItem("uuid").getNodeValue();
						if (namedNodeMap.getNamedItem("instance") != null) {
							name = namedNodeMap.getNamedItem("instance").getNodeValue() + " of " + name;
						}
						
						if (namedNodeMap.getNamedItem(DATATYPE) != null) {
							String dataType = namedNodeMap.getNamedItem(DATATYPE).getNodeValue().trim();
							name = name + " : " + namedNodeMap.getNamedItem(DATATYPE).getNodeValue();
							PopulationWorkSpaceConstants.elementLookUpDataTypeName.put(uuid, dataType);
						}
						PopulationWorkSpaceConstants.elementLookUpNode.put(name + "~" + uuid, qdms.item(i));
						PopulationWorkSpaceConstants.elementLookUpName.put(uuid, name);
					}
				}
			}
			
		}
	}	
	
	private static String getAttrMode(String mode){
		String attrMode="";
		if(mode.equals(COMPARISON)){
			attrMode = COMPARISON;
		} else if(mode.equals(COMPUTATIVE)){
			attrMode = COMPUTATIVE;
		} else if(mode.equals(NULLABLE)){
			attrMode = NULLABLE;
		} else if(mode.equals("ValueSets")){
			attrMode = VALUE_SET;
		} else if(mode.equals(CODES)){
			attrMode = CODES;
		}
		
		return attrMode;
	}
	
	public static QDSAttributesServiceAsync getQdsAttributesService() {
		if (qdsAttributesService == null) {
			qdsAttributesService = GWT.create(QDSAttributesService.class);
		}
		return qdsAttributesService;
	}
	
	public static List<ModeDetailModel> getIncludesList(List<CQLIdentifierObject> includesList,
			List<ModeDetailModel> modeDetailList, String type) {
		
		for (int i = 0; i < includesList.size(); i++) {
			ModeDetailModel mode = new ModeDetailModel();
			String includesStr = includesList.get(i).getDisplay();
			mode.setModeValue(formatModeValue(includesStr));
			mode.setModeName(type + includesList.get(i).toString());
			modeDetailList.add(mode);
		}
		
		return modeDetailList;
	}
	
	private static String formatModeValue(String displayName) {
		if(displayName.length() > 65) {
			String firstPart = displayName.substring(0, 55);
			String secondPart = displayName.substring(displayName.length() - 7); 
			displayName = firstPart + "..." + secondPart;
		}
		return displayName;
	}
	
	public static String createStrFromJSONValue(String key, JSONObject jsonObject) {
		JSONValue jsonValue = jsonObject.get(key);
		JSONString jsonString = jsonValue.isString();
		String optionName = jsonString.toString();
		optionName = optionName.replace("\"", "");
		return optionName;
	}

	public static JSONArray createJSONArrayFromStr(String jsonString, String objKey, String arrKey) {
		JSONArray jsonArray = null;
		if (jsonString != null) {
			JSONValue jsonValue = JSONParser.parseStrict(jsonString);
			JSONObject jsonObject = (JSONObject) jsonValue.isObject().get(objKey);
			jsonArray = (JSONArray) jsonObject.get(arrKey);
		}
		return jsonArray;
	}
	
}
