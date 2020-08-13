package mat.client.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CQLTypeContainer implements IsSerializable {

	private Map<String, List<String>> typeToTypeAttributeMap = new HashMap<>();
	private Map<String, String> typeAttributeToTypeMap = new HashMap<>();
	
	public CQLTypeContainer() {
		
	}

	public Map<String, List<String>> getTypeToTypeAttributeMap() {
		return typeToTypeAttributeMap;
	}

	public void setTypeToTypeAttributeMap(Map<String, List<String>> typeToTypeAttributeMap) {
		this.typeToTypeAttributeMap = typeToTypeAttributeMap;
	}
	
	public Map<String, String> getTypeAttributeToTypeMap() {
		return typeAttributeToTypeMap;
	}

	public void setTypeAttributeToTypeMap(Map<String, String> typeAttributeToTypeMap) {
		this.typeAttributeToTypeMap = typeAttributeToTypeMap;
	}
	
	public List<String> getCQLTypes() {
		return new ArrayList<>(this.typeToTypeAttributeMap.keySet());
	}
	
	public List<String> getCQLTypeAttributesByType(String type) {
		return this.typeToTypeAttributeMap.get(type);
	}
	
	public String getTypeForAttribute(String attribute) {
		return this.typeAttributeToTypeMap.get(attribute);
	}
}
