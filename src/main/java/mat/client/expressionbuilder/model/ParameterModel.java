package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.util.ExpressionTypeUtil;

public class ParameterModel extends ExpressionBuilderModel {

	private String identifier;

	public ParameterModel(String identifier, ExpressionBuilderModel parent) {
		super(parent);
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	@Override
	public String getCQL(String indentation) {
		return this.identifier;
		
	}
	
	@Override
	public CQLType getType() {
		return ExpressionTypeUtil.getTypeBasedOnSelectedExpression(identifier);
	}
	
	@Override
	public String getDisplayName() {
		return ExpressionType.PARAMETER.getDisplayName();
	}
}
