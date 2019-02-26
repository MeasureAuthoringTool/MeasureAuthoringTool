package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;

public interface IExpressionBuilderModel {
	public String getDisplayName();
	public String getCQL(String identation);
	public CQLType getType();
}
