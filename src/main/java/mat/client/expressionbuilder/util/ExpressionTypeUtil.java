package mat.client.expressionbuilder.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;

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
}
