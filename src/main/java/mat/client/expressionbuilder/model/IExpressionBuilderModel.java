package mat.client.expressionbuilder.model;

import java.util.List;

import mat.client.expressionbuilder.constant.CQLType;

public interface IExpressionBuilderModel {
	public String getDisplayName();
	public String getCQL(String identation);
	public CQLType getType();
	public IExpressionBuilderModel getParentModel();
	public List<IExpressionBuilderModel> getChildModels();
}
