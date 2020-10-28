package mat.shared;

import mat.client.shared.GenericResult;
import mat.model.cql.CQLIncludeLibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GetUsedCQLArtifactsResult extends GenericResult{
	
	private List<String> usedCQLDefinitions = new ArrayList<>(); 
	private List<String> usedCQLFunctions = new ArrayList<>();
	private List<String> usedCQLParameters = new ArrayList<>();
	private List<String> usedCQLValueSets = new ArrayList<>();
	private List<String> usedCQLcodeSystems = new ArrayList<>();
	private List<String> usedCQLcodes = new ArrayList<>();
	private List<String> usedCQLLibraries = new ArrayList<>();

	private List<CQLError> cqlErrors = new ArrayList<>();

	private Map<String, String> expressionReturnTypeMap = new HashMap<>();
	private Map<String, String> expressionToReturnTypeMap = new HashMap<>();

	private Map<String, List<String>> definitionToDefinitionMap = new HashMap<>();
	private Map<String, List<String>> definitionToFunctionMap = new HashMap<>();
	private Map<String, List<String>> functionToDefinitionMap = new HashMap<>();
	private Map<String, List<String>> functionToFunctionMap = new HashMap<>();
	private Map<String, List<String>> valueSetDataTypeMap;
	private Map<String, List<String>> codeDataTypeMap; 

	private Map<String, List<CQLError>> cqlErrorsPerExpression = new HashMap<>();
	private Map<String, List<CQLError>> cqlWarningsPerExpression = new HashMap<>();

	private Map<String, CQLIncludeLibrary> includeLibMap;

	private Map<String, Map<String, String>> nameToReturnTypeMap;
	private Map<String, Map<String, Set<String>>> expressionNameToCodeDataTypeMap; 
	private Map<String, Map<String, Set<String>>> expressionNameToValuesetDataTypeMap;
	
	private Map<String, List<CQLError>> libraryNameErrorsMap = new HashMap<>(); 
	private Map<String, List<CQLError>> libraryNameWarningsMap = new HashMap<>(); 

	public Map<String, List<CQLError>> getCqlWarningsPerExpression() {
		return cqlWarningsPerExpression;
	}

	public void setCqlWarningsPerExpression(Map<String, List<CQLError>> cqlWarningsPerExpression) {
		this.cqlWarningsPerExpression = cqlWarningsPerExpression;
	}

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

	public List<CQLError> getCqlErrors() {
		return cqlErrors;
	}

	public void setCqlErrors(List<CQLError> cqlErrors) {
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

	public Map<String, String> getExpressionToReturnTypeMap() {
		return expressionToReturnTypeMap;
	}

	public void setExpressionToReturnTypeMap(Map<String, String> expressionToReturnTypeMap) {
		this.expressionToReturnTypeMap = expressionToReturnTypeMap;
	}

	public Map<String, List<CQLError>> getCqlErrorsPerExpression() {
		return cqlErrorsPerExpression;
	}

	public void setCqlErrorsPerExpression(Map<String, List<CQLError>> cqlErrorsPerExpression) {
		this.cqlErrorsPerExpression = cqlErrorsPerExpression;
	}

	public void setExpressionNameToCodeDataTypeMap(
			Map<String, Map<String, Set<String>>> expressionNameToCodeDataTypeMap) {
		this.expressionNameToCodeDataTypeMap = expressionNameToCodeDataTypeMap;

	}

	public Map<String, Map<String, Set<String>>> getExpressionNameToCodeDataTypeMap(){
		return this.expressionNameToCodeDataTypeMap;
	}

	public Map<String, Map<String, Set<String>>> getExpressionNameToValuesetDataTypeMap() {
		return expressionNameToValuesetDataTypeMap;
	}

	public void setExpressionNameToValuesetDataTypeMap(Map<String, Map<String, Set<String>>> expressionNameToValuesetDataTypeMap) {
		this.expressionNameToValuesetDataTypeMap = expressionNameToValuesetDataTypeMap;
	}

	public Map<String, Map<String, String>> getNameToReturnTypeMap() {
		return nameToReturnTypeMap;
	}

	public void setNameToReturnTypeMap(Map<String, Map<String, String>> nameToReturnTypeMap) {
		this.nameToReturnTypeMap = nameToReturnTypeMap;
	}

	public Map<String, List<CQLError>> getLibraryNameErrorsMap() {
		return libraryNameErrorsMap;
	}

	public void setLibraryNameErrorsMap(Map<String, List<CQLError>> libraryNameErrorsMap) {
		this.libraryNameErrorsMap = libraryNameErrorsMap;
	}

	public Map<String, List<CQLError>> getLibraryNameWarningsMap() {
		return libraryNameWarningsMap;
	}

	public void setLibraryNameWarningsMap(Map<String, List<CQLError>> libraryNameWarningsMap) {
		this.libraryNameWarningsMap = libraryNameWarningsMap;
	}

}

