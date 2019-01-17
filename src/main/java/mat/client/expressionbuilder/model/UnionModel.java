package mat.client.expressionbuilder.model;

public class UnionModel extends ExpressionBuilderModel implements OperatorModel {

	@Override
	public String getCQL() {
		return "union";
	}
}
