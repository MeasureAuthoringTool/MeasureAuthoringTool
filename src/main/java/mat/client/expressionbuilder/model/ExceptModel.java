package mat.client.expressionbuilder.model;

public class ExceptModel extends ExpressionBuilderModel implements OperatorModel {
	
	public ExceptModel(ExpressionBuilderModel parent) {
		super(parent);
	}

	@Override
	public String getCQL(String indentation) {
		return indentation  + "except";
	}
}

