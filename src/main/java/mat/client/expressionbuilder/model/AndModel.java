package mat.client.expressionbuilder.model;

public class AndModel extends ExpressionBuilderModel implements OperatorModel {
	
	public AndModel(ExpressionBuilderModel parent) {
		super(parent);
	}

	@Override
	public String getCQL(String indentation) {
		return indentation  + "and";
	}
}

