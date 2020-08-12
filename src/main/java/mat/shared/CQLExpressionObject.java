package mat.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class CQLExpressionObject.
 */
public class CQLExpressionObject implements IsSerializable {
       
    /** The type. */
    private String type;
    
    /** The name. */
    private String name;
    /** The logic. */
    private String logic;
    
    /** The return type. */
    private String returnType;
       
       /** The used expressions list. */
    private List<String> usedExpressions = new ArrayList<String>();

    /** The used functions list. */
    private List<String> usedFunctions = new ArrayList<String>();;

    /** The used cql valuesets. */
    private List<String> usedValuesets = new ArrayList<String>();;

    /** THe used parameters list. */
    private List<String> usedParameters = new ArrayList<String>();;

    
    /** The used code systems list. */
    private List<String> usedCodeSystems = new ArrayList<String>();;
    
    /** The used codes list. */
    private List<String> usedCodes = new ArrayList<String>();
    
    /** The value set data type map. */
    private Map<String, List<String>> valueSetDataTypeMap = new HashMap<String,List<String>>();
    
    /** The oprand list. */
    private List<CQLExpressionOprandObject> oprandList = new ArrayList<CQLExpressionOprandObject>();
    
    /** The code data type map. */
    private Map<String, List<String>> codeDataTypeMap = new HashMap<String, List<String>>(); 
    
    
    /**
     * Instantiates a new CQL expression object.
     */
    public CQLExpressionObject(){}
    
    /**
     * Instantiates a new CQL expression object.
     *
     * @param type the type
     * @param name the name
     */
    public CQLExpressionObject(String type, String name) {
              // TODO Auto-generated constructor stub
              this.type = type;
              this.name = name;
       }
    
    
    public CQLExpressionObject(String type, String name, String logic) {
        // TODO Auto-generated constructor stub
        this.type = type;
        this.name = name;
        this.logic = logic;
 }
       /**
        * Gets the used expressions.
        *
        * @return the used expressions
        */
       public List<String> getUsedExpressions() {
              return usedExpressions;
       }

       /**
        * Sets the used expressions.
        *
        * @param usedExpressions the new used expressions
        */
       public void setUsedExpressions(List<String> usedExpressions) {
              this.usedExpressions = usedExpressions;
       }

       /**
        * Gets the used functions.
        *
        * @return the used functions
        */
       public List<String> getUsedFunctions() {
              return usedFunctions;
       }

       /**
        * Sets the used functions.
        *
        * @param usedFunctions the new used functions
        */
       public void setUsedFunctions(List<String> usedFunctions) {
              this.usedFunctions = usedFunctions;
       }

       /**
        * Gets the used valuesets.
        *
        * @return the used valuesets
        */
       public List<String> getUsedValuesets() {
              return usedValuesets;
       }

       /**
        * Sets the used valuesets.
        *
        * @param usedValuesets the new used valuesets
        */
       public void setUsedValuesets(List<String> usedValuesets) {
              this.usedValuesets = usedValuesets;
       }

       /**
        * Gets the used parameters.
        *
        * @return the used parameters
        */
       public List<String> getUsedParameters() {
              return usedParameters;
       }

       /**
        * Sets the used parameters.
        *
        * @param usedParameters the new used parameters
        */
       public void setUsedParameters(List<String> usedParameters) {
              this.usedParameters = usedParameters;
       }

       /**
        * Gets the used code systems.
        *
        * @return the used code systems
        */
       public List<String> getUsedCodeSystems() {
              return usedCodeSystems;
       }

       /**
        * Sets the used code systems.
        *
        * @param usedCodeSystems the new used code systems
        */
       public void setUsedCodeSystems(List<String> usedCodeSystems) {
              this.usedCodeSystems = usedCodeSystems;
       }

       /**
        * Gets the used codes.
        *
        * @return the used codes
        */
       public List<String> getUsedCodes() {
              return usedCodes;
       }

       /**
        * Sets the used codes.
        *
        * @param usedCodes the new used codes
        */
       public void setUsedCodes(List<String> usedCodes) {
              this.usedCodes = usedCodes;
       }

       /**
        * Gets the value set data type map.
        *
        * @return the value set data type map
        */
       public Map<String, List<String>> getValueSetDataTypeMap() {
              return valueSetDataTypeMap;
       }

       /**
        * Sets the value set data type map.
        *
        * @param valueSetDataTypeMap the value set data type map
        */
       public void setValueSetDataTypeMap(Map<String, List<String>> valueSetDataTypeMap) {
              this.valueSetDataTypeMap = valueSetDataTypeMap;
       }

       /**
        * Gets the type.
        *
        * @return the type
        */
       public String getType() {
              return type;
       }

       /**
        * Sets the type.
        *
        * @param type the new type
        */
       public void setType(String type) {
              this.type = type;
       }

       /**
        * Gets the name.
        *
        * @return the name
        */
       public String getName() {
              return name;
       }

       /**
        * Sets the name.
        *
        * @param name the new name
        */
       public void setName(String name) {
              this.name = name;
       }

       /**
        * Merge value set map.
        *
        * @param mergedValueSetMap the merged value set map
        * @param usedValueSetMap the used value set map
        * @return the map
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

       /**
        * Merge code map.
        *
        * @param mergedCodeMap the merged code map
        * @param usedCodeMap the used code map
        * @return the map
        */
       public static Map<String, List<String>> mergeCodeMap(Map<String, List<String>> mergedCodeMap, 
                     Map<String, List<String>> usedCodeMap) {
              
              for(String key: usedCodeMap.keySet()){
                     
                     if(mergedCodeMap.get(key) == null){
                           mergedCodeMap.put(key, new ArrayList<String>(usedCodeMap.get(key)));
                           continue;
                     }
                     
                     List<String> existingDataTypes = mergedCodeMap.get(key);
                     
                     for(String dataType : usedCodeMap.get(key)){
                           if(!existingDataTypes.contains(dataType)){
                                  existingDataTypes.add(dataType);
                           }
                     }
              }
              
              return mergedCodeMap;
       }      
       
       /**
        * Gets the return type.
        *
        * @return the return type
        */
       public String getReturnType() {
              return returnType;
       }

       /**
        * Sets the return type.
        *
        * @param returnType the new return type
        */
       public void setReturnType(String returnType) {
              this.returnType = returnType;
       }

       /**
        * Gets the code data type map.
        *
        * @return the code data type map
        */
       public Map<String, List<String>> getCodeDataTypeMap() {
              return codeDataTypeMap;
       }

       /**
        * Sets the code data type map.
        *
        * @param codeDataTypeMap the code data type map
        */
       public void setCodeDataTypeMap(Map<String, List<String>> codeDataTypeMap) {
              this.codeDataTypeMap = codeDataTypeMap;
       }

       /**
        * Gets the oprand list.
        *
        * @return the oprandList
        */
       public List<CQLExpressionOprandObject> getOprandList() {
              return oprandList;
       }

       /**
        * Sets the oprand list.
        *
        * @param oprandList the oprandList to set
        */
       public void setOprandList(List<CQLExpressionOprandObject> oprandList) {
              this.oprandList = oprandList;
       }

	public String getLogic() {
		return logic;
	}

	public void setLogic(String logic) {
		this.logic = logic;
	}
}

