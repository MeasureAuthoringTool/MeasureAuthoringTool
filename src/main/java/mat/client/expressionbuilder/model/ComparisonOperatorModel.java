package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;

public class ComparisonOperatorModel extends ExpressionBuilderModel {

	private String operator;

	public ComparisonOperatorModel(String operator) {
		this.operator = operator;
	}
	
	@Override
	public String getCQL(String identation) {
		return this.operator;
	}
	
	@Override
	public CQLType getType() {
		return CQLType.BOOLEAN;
	}
}
