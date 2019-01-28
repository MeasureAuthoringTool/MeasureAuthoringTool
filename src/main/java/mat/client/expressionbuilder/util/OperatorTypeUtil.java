package mat.client.expressionbuilder.util;

import java.util.ArrayList;
import java.util.List;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.OperatorType;
import mat.client.expressionbuilder.model.DefinitionModel;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.IExpressionBuilderModel;
import mat.client.expressionbuilder.model.RetrieveModel;

public class OperatorTypeUtil {	
	
	public static List<OperatorType> getAvailableOperatorsCQLType(CQLType type) {
		if(type.equals(CQLType.List)) {
			return getSetOperators();
		}
		
		if(type.equals(CQLType.ANY)) {
			return getAllOperators();
		}
		
		return getAllOperators();
	}
	
	public static List<OperatorType> getBooleanOperators() {
		List<OperatorType> booleans = new ArrayList<>();
		booleans.add(OperatorType.AND);
		booleans.add(OperatorType.OR);
		return booleans;
	}
	
	public static List<OperatorType> getSetOperators() {
		List<OperatorType> sets = new ArrayList<>();
		sets.add(OperatorType.EXCEPT);
		sets.add(OperatorType.INTERSECT);
		sets.add(OperatorType.UNION);
		return sets;
	}
	
	public static List<OperatorType> getAllOperators() {
		List<OperatorType> all = new ArrayList<>();
		all.addAll(getBooleanOperators());
		all.addAll(getSetOperators());
		return all;
	}
}
