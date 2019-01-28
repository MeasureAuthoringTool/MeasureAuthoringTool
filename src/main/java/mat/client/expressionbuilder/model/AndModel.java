package mat.client.expressionbuilder.model;

public class AndModel extends ExpressionBuilderModel implements OperatorModel {

	@Override
	public String getCQL() {
		return "and";
	}
}

