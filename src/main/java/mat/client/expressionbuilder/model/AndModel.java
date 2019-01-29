package mat.client.expressionbuilder.model;

public class AndModel extends ExpressionBuilderModel implements OperatorModel {
	
	@Override
	public String getCQL(String identation) {
		return identation  + "and";
	}
}

