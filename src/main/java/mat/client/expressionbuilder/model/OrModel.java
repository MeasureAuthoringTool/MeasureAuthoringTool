package mat.client.expressionbuilder.model;

public class OrModel extends ExpressionBuilderModel implements OperatorModel {
	
	@Override
	public String getCQL(String identation) {
		return identation  + "or";
	}
}
