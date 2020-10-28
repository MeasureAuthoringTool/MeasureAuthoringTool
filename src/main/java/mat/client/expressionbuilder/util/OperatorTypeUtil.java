package mat.client.expressionbuilder.util;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.OperatorType;

import java.util.ArrayList;
import java.util.List;

public class OperatorTypeUtil {	
	
	private OperatorTypeUtil() {
		throw new IllegalStateException("Operator Type Util");
	}
	
	public static List<OperatorType> getAvailableOperatorsCQLType(CQLType type) {
		if(type.equals(CQLType.LIST) || type.equals(CQLType.INTERVAL)) {
			return getSetOperators();
		}
		
		else if(type.equals(CQLType.ANY)) {
			return getAllOperators();
		}
		
		else if(type.equals(CQLType.BOOLEAN)) {
			return getBooleanOperators();
		}
		
		else {
			return getAllOperators();
		}
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
		all.addAll(getSetOperators());
		all.addAll(getBooleanOperators());
		return all;
	}
}
