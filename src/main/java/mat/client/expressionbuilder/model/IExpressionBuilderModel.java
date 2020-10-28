package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;

import java.util.List;

public interface IExpressionBuilderModel {
	public String getDisplayName();
	public String getCQL(String indentation);
	public CQLType getType();
	public IExpressionBuilderModel getParentModel();
	public List<IExpressionBuilderModel> getChildModels();
}
