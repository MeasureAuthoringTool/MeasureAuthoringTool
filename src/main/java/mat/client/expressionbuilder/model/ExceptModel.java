package mat.client.expressionbuilder.model;

public class ExceptModel extends ExpressionBuilderModel implements OperatorModel {
	
	@Override
	public String getCQL(String identation) {
		return identation  + "except";
	}
}

