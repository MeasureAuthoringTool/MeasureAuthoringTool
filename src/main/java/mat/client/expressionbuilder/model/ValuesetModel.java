package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;

public class ValuesetModel extends ExpressionBuilderModel {

	private String identifier;

	public ValuesetModel(String identifier, ExpressionBuilderModel parent) {
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
		return CQLType.LIST;
	}
	
	@Override
	public String getDisplayName() {
		return ExpressionType.VALUESET.getDisplayName();
	}
}
