package mat.client.expressionbuilder.model;

public class OrModel extends ExpressionBuilderModel implements OperatorModel {
	
	public OrModel(ExpressionBuilderModel parent) {
		super(parent);
	}

	@Override
	public String getCQL(String indentation) {
		return indentation  + "or";
	}
}
