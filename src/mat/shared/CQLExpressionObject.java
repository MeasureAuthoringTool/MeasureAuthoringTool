package mat.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CQLExpressionObject implements IsSerializable {
	
	private String type;
    private String name;
    private String returnType;
	
	/**
     * The used expressions list
     */
    private List<String> usedExpressions = new ArrayList<String>();

    /**
     * The used functions list
     */
    private List<String> usedFunctions = new ArrayList<String>();;

    /**
     * The used cql valuesets
     */
    private List<String> usedValuesets = new ArrayList<String>();;

    /**
     * THe used parameters list
     */
    private List<String> usedParameters = new ArrayList<String>();;

    
    /**
     * The used code systems list
     */
    private List<String> usedCodeSystems = new ArrayList<String>();;
    
    /**
     * The used codes list
     */
    private List<String> usedCodes = new ArrayList<String>();
    private Map<String, List<String>> valueSetDataTypeMap = new HashMap<String,List<String>>();
    
    private List<CQLExpressionOprandObject> oprandList = new ArrayList<CQLExpressionOprandObject>();
    
    public CQLExpressionObject(String type, String name) {
		// TODO Auto-generated constructor stub
		this.type = type;
		this.name = name;
	}
    
	public List<String> getUsedExpressions() {
		return usedExpressions;
	}

	public void setUsedExpressions(List<String> usedExpressions) {
		this.usedExpressions = usedExpressions;
	}

	public List<String> getUsedFunctions() {
		return usedFunctions;
	}

	public void setUsedFunctions(List<String> usedFunctions) {
		this.usedFunctions = usedFunctions;
	}

	public List<String> getUsedValuesets() {
		return usedValuesets;
	}

	public void setUsedValuesets(List<String> usedValuesets) {
		this.usedValuesets = usedValuesets;
	}

	public List<String> getUsedParameters() {
		return usedParameters;
	}

	public void setUsedParameters(List<String> usedParameters) {
		this.usedParameters = usedParameters;
	}

	public List<String> getUsedCodeSystems() {
		return usedCodeSystems;
	}

	public void setUsedCodeSystems(List<String> usedCodeSystems) {
		this.usedCodeSystems = usedCodeSystems;
	}

	public List<String> getUsedCodes() {
		return usedCodes;
	}

	public void setUsedCodes(List<String> usedCodes) {
		this.usedCodes = usedCodes;
	}

	public Map<String, List<String>> getValueSetDataTypeMap() {
		return valueSetDataTypeMap;
	}

	public void setValueSetDataTypeMap(Map<String, List<String>> valueSetDataTypeMap) {
		this.valueSetDataTypeMap = valueSetDataTypeMap;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param mergedValueSetMap
	 * @param usedValueSetMap
	 */
	public static Map<String, List<String>> mergeValueSetMap(Map<String, List<String>> mergedValueSetMap, 
			Map<String, List<String>> usedValueSetMap) {
		
		for(String key: usedValueSetMap.keySet()){
			
			if(mergedValueSetMap.get(key) == null){
				mergedValueSetMap.put(key, new ArrayList<String>(usedValueSetMap.get(key)));
				continue;
			}
			
			List<String> existingDataTypes = mergedValueSetMap.get(key);
			
			for(String dataType : usedValueSetMap.get(key)){
				if(!existingDataTypes.contains(dataType)){
					existingDataTypes.add(dataType);
				}
			}
		}
		
		return mergedValueSetMap;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	/**
	 * @return the oprandList
	 */
	public List<CQLExpressionOprandObject> getOprandList() {
		return oprandList;
	}

	/**
	 * @param oprandList the oprandList to set
	 */
	public void setOprandList(List<CQLExpressionOprandObject> oprandList) {
		this.oprandList = oprandList;
	}
}
