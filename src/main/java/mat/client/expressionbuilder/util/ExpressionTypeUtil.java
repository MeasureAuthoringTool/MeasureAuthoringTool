package mat.client.expressionbuilder.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.shared.MatContext;
import mat.shared.CQLIdentifierObject;
import mat.shared.cql.model.FunctionSignature;

public class ExpressionTypeUtil {

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

		expressions.forEach(def -> def.setReturnType(nameToReturnTypeMap.get(def.getDisplay())));

		if (type.equals(CQLType.BOOLEAN) || type.equals(CQLType.LIST)) {
			expressions.removeIf(def -> !getCQLTypeBasedOnReturnType(def.getReturnType()).equals(type));
		}

		return expressions;
	}

	public static List<String> getPreDefinedFunctionsBasedOnReturnType(CQLType type) {
		if (type == null) {
			return new ArrayList<>(MatContext.get().getCqlConstantContainer().getFunctionNames());
		}
			
		List<FunctionSignature> signatures = new ArrayList<>(MatContext.get().getCqlConstantContainer().getFunctionSignatures());
		if (type.equals(CQLType.BOOLEAN) || type.equals(CQLType.LIST)) {
			signatures.removeIf(func -> !getCQLTypeBasedOnReturnType(func.getReturnType()).equals(type));
		}
		return signatures.stream().map(s -> s.getName()).collect(Collectors.toList());
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
		availableExpressionTypes.removeIf(exp -> exp.equals(ExpressionType.FUNCTION) && getFunctionList(type).isEmpty() && getIncludesFunctionList(type).isEmpty());
		availableExpressionTypes.removeIf(exp -> exp.equals(ExpressionType.PARAMETER) && getParameterList(type).isEmpty() && getIncludesParameterList(type).isEmpty());
		
		return availableExpressionTypes;
	}

	private static CQLType getCQLTypeBasedOnReturnType(String returnType) {
		CQLType cqlType = CQLType.ANY;
		returnType = returnType.toLowerCase();
		final List<String> returnTypes = Arrays.asList("list<", "interval<", "tuple");
		if ("System.Boolean".equalsIgnoreCase(returnType) || "Boolean".equalsIgnoreCase(returnType)) {
			cqlType = CQLType.BOOLEAN;
		} else if (returnTypes.stream().anyMatch(returnType::contains)) {
			cqlType = CQLType.LIST;
		}
		return cqlType;
	}

	public static CQLType getCQLTypeBasedOnOperator(String operator) {
		CQLType cqlType = CQLType.ANY;
		
		if(OperatorTypeUtil.getBooleanOperators().stream().anyMatch(op -> op.getValue().equals(operator))) {
			cqlType = CQLType.BOOLEAN;
		} else if (OperatorTypeUtil.getSetOperators().stream().anyMatch(op -> op.getValue().equals(operator))) {
			cqlType = CQLType.LIST;
		}
		
		return cqlType;
	}
	
}
