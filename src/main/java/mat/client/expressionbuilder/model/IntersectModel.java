package mat.client.expressionbuilder.model;

public class IntersectModel extends ExpressionBuilderModel implements OperatorModel {
	
	public IntersectModel(ExpressionBuilderModel parent) {
		super(parent);
	}

	@Override
	public String getCQL(String indentation) {
		return indentation  + "intersect";
	}
}

