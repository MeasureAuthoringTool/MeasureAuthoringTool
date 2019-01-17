package mat.client.expressionbuilder.model;

public class ExceptModel extends ExpressionBuilderModel implements OperatorModel {

	@Override
	public String getCQL() {
		return "except";
	}
}

