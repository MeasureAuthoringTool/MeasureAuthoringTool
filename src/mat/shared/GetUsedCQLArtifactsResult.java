package mat.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mat.client.shared.GenericResult;
import mat.model.cql.CQLIncludeLibrary;

// TODO: Auto-generated Javadoc
/**
* The Class SaveUpdateCQLResult.
*/
public class GetUsedCQLArtifactsResult extends GenericResult{
	
		private Map<String , List<CQLErrors>> cqlErrorsPerExpression = new HashMap<String,List<CQLErrors>>();
       
       private List<String> usedCQLDefinitions = new ArrayList<String>(); 
       
       private List<String> usedCQLFunctions = new ArrayList<String>();
       
       private List<String> usedCQLParameters = new ArrayList<String>();
       
       private List<String> usedCQLValueSets = new ArrayList<String>();
       
       private List<String> usedCQLcodeSystems = new ArrayList<String>();
       
       private List<String> usedCQLcodes = new ArrayList<String>();
       
       private List<String> usedCQLLibraries = new ArrayList<String>();
       
       private Map<String, List<String>> definitionToDefinitionMap = new HashMap<String, List<String>>();
       
       private Map<String, List<String>> definitionToFunctionMap = new HashMap<String, List<String>>();
       
       private Map<String, List<String>> functionToDefinitionMap = new HashMap<String, List<String>>();
       
       private Map<String, List<String>> functionToFunctionMap = new HashMap<String, List<String>>();
       
       private Map<String,String> expressionReturnTypeMap = new HashMap<String, String>();
       
       private Map<String, List<String>> valueSetDataTypeMap;
       
       private Map<String, List<String>> codeDataTypeMap; 
       
       private Map<String, CQLIncludeLibrary> includeLibMap;
       
       private List<CQLErrors> cqlErrors = new ArrayList<CQLErrors>(); 


       public List<String> getUsedCQLDefinitions() {
              return usedCQLDefinitions;
       }

       public void setUsedCQLDefinitions(List<String> usedCQLDefinitions) {
              this.usedCQLDefinitions = usedCQLDefinitions;
       }

       public List<String> getUsedCQLFunctions() {
              return usedCQLFunctions;
       }

       public void setUsedCQLFunctions(List<String> usedCQLFunctionss) {
              this.usedCQLFunctions = usedCQLFunctionss;
       }

       public List<String> getUsedCQLParameters() {
              return usedCQLParameters;
       }

       public void setUsedCQLParameters(List<String> usedCQLParameters) {
              this.usedCQLParameters = usedCQLParameters;
       }

       public List<String> getUsedCQLValueSets() {
              return usedCQLValueSets;
       }

       public void setUsedCQLValueSets(List<String> usedCQLValueSets) {
              this.usedCQLValueSets = usedCQLValueSets;
       }

       public List<String> getUsedCQLcodeSystems() {
              return usedCQLcodeSystems;
       }

       public void setUsedCQLcodeSystems(List<String> usedCQLcodeSystems) {
              this.usedCQLcodeSystems = usedCQLcodeSystems;
       }

       public List<String> getUsedCQLcodes() {
              return usedCQLcodes;
       }

       public void setUsedCQLcodes(List<String> usedCQLcodes) {
              this.usedCQLcodes = usedCQLcodes;
       }

       public List<String> getUsedCQLLibraries() {
              return usedCQLLibraries;
       }

       public void setUsedCQLLibraries(List<String> usedCQLLibraries) {
              this.usedCQLLibraries = usedCQLLibraries;
       }

       public Map<String, List<String>> getValueSetDataTypeMap() {
              return valueSetDataTypeMap;
       }

       public void setValueSetDataTypeMap(Map<String, List<String>> valueSetDataTypeMap) {
              this.valueSetDataTypeMap = valueSetDataTypeMap;
       }

       public List<CQLErrors> getCqlErrors() {
              return cqlErrors;
       }

       public void setCqlErrors(List<CQLErrors> cqlErrors) {
              this.cqlErrors = cqlErrors;
       }

       public Map<String, CQLIncludeLibrary> getIncludeLibMap() {
              return includeLibMap;
       }

       public void setIncludeLibMap(Map<String, CQLIncludeLibrary> includeLibMap) {
              this.includeLibMap = includeLibMap;
       }
       
       public Map<String, List<String>> getDefinitionToDefinitionMap() {
              return definitionToDefinitionMap;
       }

       public void setDefinitionToDefinitionMap(
                     Map<String, List<String>> definitionToDefinitionMap) {
              this.definitionToDefinitionMap = definitionToDefinitionMap;
       }

       public Map<String, List<String>> getDefinitionToFunctionMap() {
              return definitionToFunctionMap;
       }

       public void setDefinitionToFunctionMap(
                     Map<String, List<String>> definitionToFunctionMap) {
              this.definitionToFunctionMap = definitionToFunctionMap;
       }

       public Map<String, List<String>> getFunctionToDefinitionMap() {
              return functionToDefinitionMap;
       }

       public void setFunctionToDefinitionMap(
                     Map<String, List<String>> functionToDefinitionMap) {
              this.functionToDefinitionMap = functionToDefinitionMap;
       }

       public Map<String, List<String>> getFunctionToFunctionMap() {
              return functionToFunctionMap;
       }

       public void setFunctionToFunctionMap(
                     Map<String, List<String>> functionToFunctionMap) {
              this.functionToFunctionMap = functionToFunctionMap;
       }

       public Map<String, List<String>> getCodeDataTypeMap() {
              return codeDataTypeMap;
       }

       public void setCodeDataTypeMap(Map<String, List<String>> codeDataTypeMap) {
              this.codeDataTypeMap = codeDataTypeMap;
       }

	public Map<String, String> getExpressionReturnTypeMap() {
		return expressionReturnTypeMap;
	}

	public void setExpressionReturnTypeMap(Map<String, String> expressionReturnTypeMap) {
		this.expressionReturnTypeMap = expressionReturnTypeMap;
	}

	public Map<String, List<CQLErrors>> getCqlErrorsPerExpression() {
		return cqlErrorsPerExpression;
	}

	public void setCqlErrorsPerExpression(Map<String, List<CQLErrors>> cqlErrorsPerExpression) {
		this.cqlErrorsPerExpression = cqlErrorsPerExpression;
	}

}

