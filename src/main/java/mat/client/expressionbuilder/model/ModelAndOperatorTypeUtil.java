package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.OperatorType;

public class ModelAndOperatorTypeUtil {
	
	private ModelAndOperatorTypeUtil() {
		throw new IllegalStateException("Modal and Operator Type Util");
	}

	public static OperatorType getOperatorModel(IExpressionBuilderModel model) {
		if(model instanceof UnionModel) {
			return OperatorType.UNION;
		} else if(model instanceof ExceptModel) {
			return OperatorType.EXCEPT;
		} else if(model instanceof IntersectModel) {
			return OperatorType.INTERSECT;
		} else if(model instanceof AndModel) {
			return OperatorType.AND;
		} else if(model instanceof OrModel) {
			return OperatorType.OR;
		}
		
		return null;
	}
}
