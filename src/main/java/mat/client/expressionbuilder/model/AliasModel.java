package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.ExpressionType;

public class AliasModel extends ExpressionBuilderModel {

	String alias = "";
	String aliasType = "";
	
	public AliasModel(ExpressionBuilderModel parent) {
		super(parent);
	}

	public AliasModel(ExpressionBuilderModel parent, String alias, String aliasType) {
		super(parent);
		this.alias = alias;
		this.aliasType = aliasType;
	}

	public String getAlias() {
		return this.alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public String getAliasType() {
		return aliasType;
	}

	public void setAliasType(String aliasType) {
		this.aliasType = aliasType;
	}

	@Override
	public String getCQL(String indentation) {
		return alias;
	}
	
	@Override
	public String getDisplayName() {
		return ExpressionType.ALIAS.getDisplayName();
	}
}
