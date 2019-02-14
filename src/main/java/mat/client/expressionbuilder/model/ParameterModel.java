package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;

public class ParameterModel extends ExpressionBuilderModel {

	private String identifier;

	public ParameterModel(String identifier) {
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	@Override
	public String getCQL(String identation) {
		return this.identifier;
		
	}
	
	@Override
	public CQLType getType() {
		return CQLType.ANY;
	}
}
