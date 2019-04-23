package mat.client.expressionbuilder.model;

public class UnionModel extends ExpressionBuilderModel implements OperatorModel {
	
	public UnionModel(ExpressionBuilderModel parent) {
		super(parent);
	}

	@Override
	public String getCQL(String indentation) {
		return indentation  + "union";
	}
}
