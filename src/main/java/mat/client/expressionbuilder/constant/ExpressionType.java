package mat.client.expressionbuilder.constant;

import java.util.List;
import mat.client.expressionbuilder.util.*;

public enum ExpressionType implements ExpressionBuilderType {

	RETRIEVE("Data element or Retrieve", OperatorTypeUtil.getSetOperators()),
	DEFINITION("Definition", OperatorTypeUtil.getAllOperators()), 
	EXISTS("Exists", OperatorTypeUtil.getBooleanOperators()),
	NOT("Negation (not)", OperatorTypeUtil.getBooleanOperators()),
	IS_NULL_NOT_NULL("Null (is null/not null)", OperatorTypeUtil.getBooleanOperators()),
	IS_TRUE_FALSE("True/False (is true/false)", OperatorTypeUtil.getBooleanOperators()),
	COMPARISON("Comparison", OperatorTypeUtil.getBooleanOperators()),
	INTERVAL("Interval", OperatorTypeUtil.getBooleanOperators()), 
	QUERY("Query", OperatorTypeUtil.getSetOperators());
	
	private String displayName;
	private List<OperatorType> availableOperators;
	
	ExpressionType(String displayName, List<OperatorType> availableOperators) {
		this.setAvailableOperators(availableOperators);
		this.displayName = displayName;
	}
	
	@Override
	public String getDisplayName() {
		return this.displayName;
	}
	
	@Override
	public String getValue() {
		return this.toString();
	}

	public List<OperatorType> getAvailableOperators() {
		return availableOperators;
	}

	public void setAvailableOperators(List<OperatorType> availableOperators) {
		this.availableOperators = availableOperators;
	}
}
