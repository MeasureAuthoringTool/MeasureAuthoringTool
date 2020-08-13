package mat.client.expressionbuilder.util;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.shared.MatContext;
import mat.shared.CQLIdentifierObject;
import mat.shared.cql.model.FunctionSignature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExpressionTypeUtil {

	private static final String PARSER_BOOLEAN = "System.Boolean";
	private static final String SIGNATURES_JSON_BOOLEAN = "Boolean";
	
	private ExpressionTypeUtil() {
		throw new IllegalStateException("Expression Type Util");
	}

	public static List<ExpressionType> getAvailableExpressionsCQLType (CQLType type) { 
		List<ExpressionType> availableExpressionTypes = new ArrayList<>();
		if (type.equals(CQLType.BOOLEAN)) {
			availableExpressionTypes.addAll(getBooleanExpressions());
			
		} else if (type.equals(CQLType.LIST)){
			availableExpressionTypes.addAll(getInFixSetExpression());
			
		} else {
			availableExpressionTypes.addAll(getBooleanExpressions());
			availableExpressionTypes.addAll(getInFixSetExpression());
			availableExpressionTypes = availableExpressionTypes.stream().distinct().collect(Collectors.toList());
			availableExpressionTypes.sort((e1, e2) -> e1.getDisplayName().compareTo(e2.getDisplayName()));
		}
		return availableExpressionTypes;	
	}
	
	private static List<ExpressionType> getBooleanExpressions(){
		final List<ExpressionType> availableExpressionTypes = new ArrayList<>();
		availableExpressionTypes.add(ExpressionType.COMPARISON);
		availableExpressionTypes.add(ExpressionType.DEFINITION);
		availableExpressionTypes.add(ExpressionType.EXISTS);
		availableExpressionTypes.add(ExpressionType.FUNCTION);
		availableExpressionTypes.add(ExpressionType.NOT);
		availableExpressionTypes.add(ExpressionType.IS_NULL_NOT_NULL);
		availableExpressionTypes.add(ExpressionType.TIMING);
		availableExpressionTypes.add(ExpressionType.IS_TRUE_FALSE);
		return availableExpressionTypes;
	}
	
	private static List<ExpressionType> getInFixSetExpression(){
		final List<ExpressionType> availableExpressionTypes = new ArrayList<>();
		availableExpressionTypes.add(ExpressionType.ATTRIBUTE);
		availableExpressionTypes.add(ExpressionType.RETRIEVE);
		availableExpressionTypes.add(ExpressionType.DEFINITION);
		availableExpressionTypes.add(ExpressionType.FUNCTION);
		availableExpressionTypes.add(ExpressionType.INTERVAL);
		availableExpressionTypes.add(ExpressionType.QUERY);
		return availableExpressionTypes;
	}
	
	public static List<CQLIdentifierObject> getFilteredExpressionList(CQLType type, List<CQLIdentifierObject> expressionsList) {
		final List<CQLIdentifierObject> expressions = new ArrayList<>();
		expressions.addAll(expressionsList);
		
		final Map<String, String> nameToReturnTypeMap = new HashMap<>(); 
		nameToReturnTypeMap.putAll(MatContext.get().getExpressionToReturnTypeMap());

		if (!nameToReturnTypeMap.isEmpty() && isFilterable(type)) {
			expressions.removeIf(def -> !getCQLTypeBasedOnReturnType(nameToReturnTypeMap.get(def.getDisplay())).equals(type));
		}

		return expressions;
	}

	private static boolean isFilterable(CQLType type) {
		return type.equals(CQLType.BOOLEAN) || type.equals(CQLType.LIST) || type.equals(CQLType.INTERVAL);
	}
	
	public static List<String> getPreDefinedFunctionsBasedOnReturnType(CQLType type) {
		if (type == null) {
			return new ArrayList<>(MatContext.get().getCqlConstantContainer().getFunctionNames());
		}
			
		final List<FunctionSignature> signatures = new ArrayList<>(MatContext.get().getCqlConstantContainer().getFunctionSignatures());
		if (isFilterable(type)) {
			signatures.removeIf(func -> !getCQLTypeBasedOnReturnType(func.getReturnType()).equals(type));
		}
		return signatures.stream().map(s -> s.getName()).distinct().collect(Collectors.toList());
	}
	
	public static List<CQLIdentifierObject> getDefinitionsBasedOnReturnType(CQLType type) {
		final List<CQLIdentifierObject> definitions = new ArrayList<>();

		final List<CQLIdentifierObject> measureDefinitions = getDefinitionList(type);
		if (!measureDefinitions.isEmpty()) {
			definitions.addAll(IdentifierSortUtil.sortIdentifierList(measureDefinitions));
		}
		
		final List<CQLIdentifierObject> includedDefinitions = getIncludesDefinitionList(type);
		if (!includedDefinitions.isEmpty()) {
			definitions.addAll(IdentifierSortUtil.sortIdentifierList(includedDefinitions));
		}
		
		return definitions;
	}

	public static List<CQLIdentifierObject> getFunctionsBasedOnReturnType(CQLType type) {
		final List<CQLIdentifierObject> functions = new ArrayList<>();

		final List<CQLIdentifierObject> measureFunctions = getFunctionList(type);
		if (!measureFunctions.isEmpty()) {
			functions.addAll(IdentifierSortUtil.sortIdentifierList(measureFunctions));
		}
		
		final List<CQLIdentifierObject> includedFunctions = getIncludesFunctionList(type);
		if (!includedFunctions.isEmpty()) {
			functions.addAll(IdentifierSortUtil.sortIdentifierList(includedFunctions));
		}
		
		return functions;
	}

	public static List<CQLIdentifierObject> getParametersBasedOnReturnType(CQLType type) {
		final List<CQLIdentifierObject> parameters = new ArrayList<>();

		final List<CQLIdentifierObject> measureparameters = getParameterList(type);
		if (!measureparameters.isEmpty()) {
			parameters.addAll(IdentifierSortUtil.sortIdentifierList(measureparameters));
		}
		
		final List<CQLIdentifierObject> includedParameters = getIncludesParameterList(type);
		if (!includedParameters.isEmpty()) {
			parameters.addAll(IdentifierSortUtil.sortIdentifierList(includedParameters));
		}
		
		return parameters;
	}
	
	private static List<CQLIdentifierObject> getDefinitionList(CQLType type){
		return ExpressionTypeUtil.getFilteredExpressionList(type, MatContext.get().getDefinitions());
	}
	
	private static List<CQLIdentifierObject> getIncludesDefinitionList(CQLType type){
		return ExpressionTypeUtil.getFilteredExpressionList(type, MatContext.get().getIncludedDefNames());
	}

	private static List<CQLIdentifierObject> getFunctionList(CQLType type){
		return ExpressionTypeUtil.getFilteredExpressionList(type, MatContext.get().getFuncs());
	}
	
	private static List<CQLIdentifierObject> getIncludesFunctionList(CQLType type){
		return ExpressionTypeUtil.getFilteredExpressionList(type, MatContext.get().getIncludedFuncNames());
	}

	private static List<CQLIdentifierObject> getParameterList(CQLType type){
		return ExpressionTypeUtil.getFilteredExpressionList(type, MatContext.get().getParameters());
	}
	
	private static List<CQLIdentifierObject> getIncludesParameterList(CQLType type){
		return ExpressionTypeUtil.getFilteredExpressionList(type, MatContext.get().getIncludedParamNames());
	}
	
	public static List<ExpressionType> getFilteredExpressionsCQLType(CQLType type) {
		final List<ExpressionType> availableExpressionTypes = new ArrayList<>();
		availableExpressionTypes.addAll(getAvailableExpressionsCQLType(type));

		availableExpressionTypes.removeIf(exp -> exp.equals(ExpressionType.DEFINITION) && getDefinitionList(type).isEmpty() && getIncludesDefinitionList(type).isEmpty());	
		availableExpressionTypes.removeIf(exp -> exp.equals(ExpressionType.FUNCTION) && getFunctionList(type).isEmpty() && getIncludesFunctionList(type).isEmpty() 
				&& getPreDefinedFunctionsBasedOnReturnType(type).isEmpty());
		availableExpressionTypes.removeIf(exp -> exp.equals(ExpressionType.PARAMETER) && getParameterList(type).isEmpty() && getIncludesParameterList(type).isEmpty());
		
		return availableExpressionTypes;
	}

	private static CQLType getCQLTypeBasedOnReturnType(String returnType) {
		CQLType cqlType = CQLType.ANY;
		if (null != returnType) {
			returnType = returnType.toLowerCase();
			if (PARSER_BOOLEAN.equalsIgnoreCase(returnType) || SIGNATURES_JSON_BOOLEAN.equalsIgnoreCase(returnType)) {
				cqlType = CQLType.BOOLEAN;
			} else if (returnType.startsWith("list<") || returnType.startsWith("tuple")) {
				cqlType = CQLType.LIST;
			} else if (returnType.startsWith("interval<")) {
				cqlType = CQLType.INTERVAL;
			}
		}
		return cqlType;
	}
	
	public static CQLType getTypeBasedOnSelectedExpression(String identifier) {
		final String returnType = MatContext.get().getExpressionToReturnTypeMap().get(identifier.replace("\"", ""));
		return (returnType != null) ? ExpressionTypeUtil.convertReturnTypeToCQLType(returnType) : CQLType.ANY;
	}
	
	public static CQLType convertReturnTypeToCQLType(String returnType) {
		CQLType cqlType = CQLType.ANY;
		if (null != returnType) {
			final String returnTypeLower = returnType.toLowerCase();
			final Optional<CQLType> cqlTypeMatch = Arrays.stream(CQLType.values()).filter(c -> returnTypeLower.contains(c.getName().toLowerCase())).findAny();
			if (cqlTypeMatch.isPresent()) {
				cqlType = cqlTypeMatch.get();
			}
		}
		return cqlType;
	}
}
