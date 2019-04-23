package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;

public class ComparisonOperatorModel extends ExpressionBuilderModel {

	private String operator;

	public ComparisonOperatorModel(String operator, ExpressionBuilderModel parent) {
		super(parent);
		this.operator = operator;
	}
	
	@Override
	public String getCQL(String indentation) {
		return this.operator;
	}
	
	@Override
	public CQLType getType() {
		return CQLType.BOOLEAN;
	}
}
