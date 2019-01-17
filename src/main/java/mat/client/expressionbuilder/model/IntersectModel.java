package mat.client.expressionbuilder.model;

public class IntersectModel extends ExpressionBuilderModel implements OperatorModel {

	@Override
	public String getCQL() {
		return "intersect";
	}
}

