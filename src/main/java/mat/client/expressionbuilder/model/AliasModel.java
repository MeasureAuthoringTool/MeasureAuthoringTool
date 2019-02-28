package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.ExpressionType;

public class AliasModel extends ExpressionBuilderModel {

	String alias = "";
	
	public AliasModel(ExpressionBuilderModel parent) {
		super(parent);
	}
	
	public String getAlias() {
		return this.alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	@Override
	public String getCQL(String identation) {
		return alias;
	}
	
	@Override
	public String getDisplayName() {
		return ExpressionType.ALIAS.getDisplayName();
	}
}
