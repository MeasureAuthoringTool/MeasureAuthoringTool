package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.OperatorType;

public class ModelAndOperatorTypeUtil {

	public static OperatorType getOperatorModel(IExpressionBuilderModel model) {
		if(model instanceof UnionModel) {
			return OperatorType.UNION;
		} else if(model instanceof ExceptModel) {
			return OperatorType.EXCEPT;
		} else if(model instanceof IntersectModel) {
			return OperatorType.INTERSECT;
		}
		
		return null;
	}
}
