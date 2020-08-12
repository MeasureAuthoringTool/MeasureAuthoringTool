package mat.client.expressionbuilder.constant;

import mat.client.expressionbuilder.util.OperatorTypeUtil;

import java.util.ArrayList;
import java.util.List;

public enum ExpressionType implements ExpressionBuilderType {

	ATTRIBUTE("Attribute", OperatorTypeUtil.getSetOperators(), getRelevantTypesForAttributes()),
	RETRIEVE("Data element or Retrieve", OperatorTypeUtil.getSetOperators(), getRelevantTypesForRetrieves()),
	DEFINITION("Definition", OperatorTypeUtil.getAllOperators(), getRelevantTypesForDefinitions()), 
	EXISTS("Exists", OperatorTypeUtil.getBooleanOperators(), getRelevantTypesForBooleanExpressions()),
	NOT("Negation (not)", OperatorTypeUtil.getBooleanOperators(), getRelevantTypesForBooleanExpressions()),
	IS_NULL_NOT_NULL("Null (is null/not null)", OperatorTypeUtil.getBooleanOperators(), getRelevantTypesForBooleanExpressions()),
	IS_TRUE_FALSE("True/False (is true/false)", OperatorTypeUtil.getBooleanOperators(), getRelevantTypesForBooleanExpressions()),
	COMPARISON("Comparison", OperatorTypeUtil.getBooleanOperators(), getRelevantTypesForBooleanExpressions()),
	INTERVAL("Interval", OperatorTypeUtil.getSetOperators(), getRelevantTypesForIntervalExpression()), 
	QUERY("Query", OperatorTypeUtil.getSetOperators(), getRelevantTypesForQuery()),
	PARAMETER("Parameter", OperatorTypeUtil.getAllOperators(), new ArrayList<>()),
	VALUESET("Value Set", OperatorTypeUtil.getSetOperators(), new ArrayList<>()),
	CODE("Code", OperatorTypeUtil.getAllOperators(), new ArrayList<>()),
	IN("Membership (In)", OperatorTypeUtil.getBooleanOperators(), getRelevantTypesForBooleanExpressions()),
	COMPUTATION("Computation", OperatorTypeUtil.getBooleanOperators(), getRelevantTypesForComputation()),
	TIME_BOUNDARY("Start of / End of", OperatorTypeUtil.getBooleanOperators(), getRelevantTypesForStartOfEndOf()),
	QUANTITY("Quantity", OperatorTypeUtil.getSetOperators(), getRelevantQuantityExpression()),
	ALIAS("Alias", OperatorTypeUtil.getAllOperators(), new ArrayList<>()),
	FUNCTION("Function", OperatorTypeUtil.getAllOperators(), getRelevantTypesForFunctions()),
	TIMING("Timing", OperatorTypeUtil.getBooleanOperators(), getRelevantTypesForBooleanExpressions()),
	DATE_TIME("Date/Time", new ArrayList<>(), getRelevantTypesForTimeBasedExpressions());
	
	private String displayName;
	private String value;
	private List<OperatorType> availableOperators;
	private List<CQLType> relevantCQLTypes;
	
	ExpressionType(String displayName, List<OperatorType> availableOperators, List<CQLType> relevantCQLTypes) {
		this(displayName, displayName, availableOperators, relevantCQLTypes);
	}
	
	ExpressionType(String displayName, String value, List<OperatorType> availableOperator, List<CQLType> relevantCQLTypes) {
		this.value = value;
		this.setAvailableOperators(availableOperator);
		this.displayName = displayName;
		this.relevantCQLTypes = relevantCQLTypes;
	}
	
	@Override
	public String getDisplayName() {
		return this.displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	@Override
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	public List<OperatorType> getAvailableOperators() {
		return availableOperators;
	}

	public void setAvailableOperators(List<OperatorType> availableOperators) {
		this.availableOperators = availableOperators;
	}

	public List<CQLType> getRelevantCQLTypes() {
		return relevantCQLTypes;
	}

	public void setRelevantCQLTypes(List<CQLType> relevantCQLTypes) {
		this.relevantCQLTypes = relevantCQLTypes;
	}
	
	private static List<CQLType> getRelevantTypesForAttributes() {
		List<CQLType> types = new ArrayList<>();
		types.addAll(getRelevantTypesForTimeBasedExpressions());
		types.add(CQLType.ANY);
		types.add(CQLType.CODE);
		types.add(CQLType.INTEGER);
		types.add(CQLType.DECIMAL);
		types.add(CQLType.QUANTITY);
		types.add(CQLType.RATIO);
		types.add(CQLType.LIST_CODE);
		types.add(CQLType.STRING);
		types.add(CQLType.INTERVAL_DATETIME);
		
		return types;
	}
	
	private static List<CQLType> getRelevantTypesForRetrieves() {
		List<CQLType> types = new ArrayList<>();
		types.add(CQLType.ANY);
		types.add(CQLType.LIST_QDM);
		return types;
	}
	
	private static List<CQLType> getRelevantTypesForDefinitions() {
		List<CQLType> types = new ArrayList<>();
		types.addAll(getRelevantTypesForTimeBasedExpressions());
		types.addAll(getRelevantTypesForTimeBasedIntervals());
		types.add(CQLType.INTERVAL_DECIMAL);
		types.add(CQLType.INTERVAL_NUMBER);
		types.add(CQLType.INTERVAL_QUANTITY);
		types.add(CQLType.ANY);
		types.add(CQLType.BOOLEAN);
		types.add(CQLType.CODE);
		types.add(CQLType.DECIMAL);
		types.add(CQLType.INTEGER);
		types.add(CQLType.QUANTITY);
		types.add(CQLType.RATIO);
		types.add(CQLType.STRING);
		types.add(CQLType.QDM);
		types.add(CQLType.LIST_QDM);
		types.add(CQLType.LIST_BOOLEAN);
		types.add(CQLType.LIST_CODE);
		types.add(CQLType.LIST_DATE);
		types.add(CQLType.LIST_DATETIME);
		types.add(CQLType.LIST_DECIMAL);
		types.add(CQLType.LIST_INTEGER);
		types.add(CQLType.LIST_QUANTITY);
		types.add(CQLType.LIST_STRING);
		types.add(CQLType.LIST_TIME);

		return types;
	}
	
	private static List<CQLType> getRelevantTypesForFunctions() {
		List<CQLType> types = new ArrayList<>();
		types.addAll(getRelevantTypesForTimeBasedExpressions());
		types.addAll(getRelevantTypesForTimeBasedIntervals());
		types.add(CQLType.ANY);
		types.add(CQLType.BOOLEAN);
		types.add(CQLType.CODE);
		types.add(CQLType.DECIMAL);
		types.add(CQLType.INTEGER);
		types.add(CQLType.QUANTITY);
		types.add(CQLType.RATIO);
		types.add(CQLType.STRING);
		types.add(CQLType.QDM);
		types.add(CQLType.LIST_QDM);
		types.add(CQLType.LIST_BOOLEAN);
		types.add(CQLType.LIST_CODE);
		types.add(CQLType.LIST_DATE);
		types.add(CQLType.LIST_DATETIME);
		types.add(CQLType.LIST_DECIMAL);
		types.add(CQLType.LIST_INTEGER);
		types.add(CQLType.LIST_QUANTITY);
		types.add(CQLType.LIST_STRING);
		types.add(CQLType.LIST_TIME);
		types.add(CQLType.INTERVAL_DECIMAL);
		types.add(CQLType.INTERVAL_NUMBER);
		types.add(CQLType.INTERVAL_QUANTITY);
		return types;
	}
	
	private static List<CQLType> getRelevantTypesForQuery() {
		List<CQLType> types = new ArrayList<>();
		types.add(CQLType.ANY);
		types.add(CQLType.LIST_QDM);
		return types;
	}

	private static List<CQLType> getRelevantTypesForTimeBasedExpressions() {
		List<CQLType> types = new ArrayList<>();
		types.add(CQLType.DATE);
		types.add(CQLType.DATETIME);
		types.add(CQLType.TIME);
		return types;
	}
	

	private static List<CQLType> getRelevantTypesForComputation() {
		List<CQLType> types = new ArrayList<>();
		types.addAll(getRelevantTypesForTimeBasedExpressions());
		types.add(CQLType.INTEGER);
		types.add(CQLType.DECIMAL);
		types.add(CQLType.QUANTITY);
		types.add(CQLType.ANY);
		return types;
	}
	
	private static List<CQLType> getRelevantTypesForIntervalExpression() {
		List<CQLType> types = new ArrayList<>();
		types.add(CQLType.ANY);
		types.addAll(getRelevantTypesForTimeBasedIntervals());
		types.add(CQLType.INTERVAL_QUANTITY);
		types.add(CQLType.INTERVAL_DECIMAL);
		types.add(CQLType.INTERVAL_NUMBER);

		return types;
	}
	
	private static List<CQLType> getRelevantTypesForBooleanExpressions() {
		List<CQLType> types = new ArrayList<>();
		types.add(CQLType.BOOLEAN);
		types.add(CQLType.ANY);
		return types;
	}
	
	private static List<CQLType> getRelevantTypesForStartOfEndOf() {
		List<CQLType> types = new ArrayList<>();
		types.add(CQLType.ANY);
		types.add(CQLType.DATE);
		types.add(CQLType.TIME);
		types.add(CQLType.DATETIME);
		types.add(CQLType.INTEGER);
		types.add(CQLType.DECIMAL);
		types.add(CQLType.QUANTITY);
		return types;
	}
	
	private static List<CQLType> getRelevantQuantityExpression() {
		List<CQLType> types = new ArrayList<>();
		types.add(CQLType.ANY);
		types.add(CQLType.QUANTITY);
		return types;
	}
	
	private static List<CQLType> getRelevantTypesForTimeBasedIntervals() {
		List<CQLType> types = new ArrayList<>();
		types.add(CQLType.INTERVAL_DATE);
		types.add(CQLType.INTERVAL_DATETIME);
		types.add(CQLType.INTERVAL_TIME);
		return types;
	}
}
