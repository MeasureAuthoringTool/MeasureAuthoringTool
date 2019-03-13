package mat.client.expressionbuilder.model;

import java.util.ArrayList;
import java.util.List;

import mat.client.expressionbuilder.constant.CQLType;

public class ExpressionBuilderModel implements IExpressionBuilderModel {
	private IExpressionBuilderModel parentModel;
	private List<IExpressionBuilderModel> models;
	

	public ExpressionBuilderModel(ExpressionBuilderModel parent) {
		this.parentModel = parent;
		models = new ArrayList<>();
	}

	@Override
	public String getCQL(String identation) {
		StringBuilder builder = new StringBuilder();
		if (!models.isEmpty()) {			
			
			// this the first element
			boolean firstElementHasMoreThanOneChild = models.get(0).getChildModels().size() > 1;
			boolean firstElementIsQuery = models.get(0) instanceof QueryModel;
			boolean modelHasMoreThanOneChild = models.size() > 1;
			
			boolean shouldAddParenthesesForFirstElement = firstElementHasMoreThanOneChild || (firstElementIsQuery && modelHasMoreThanOneChild);
			
			if(shouldAddParenthesesForFirstElement) {
				builder.append(" (");
			}
			
			builder.append(models.get(0).getCQL(identation));
			
			if(shouldAddParenthesesForFirstElement) {
				builder.append(") ");
			}

			identation = identation + "  ";
			for (int i = 1; i < models.size(); i += 2) {
				builder.append("\n");
				
				// this appends the operator
				builder.append(models.get(i).getCQL(identation));
								
				// this appends the subsequent elements
				if((i + 1) <= models.size() - 1) {
					boolean secondElementHasMoreThanOneChild =  models.get(i + 1).getChildModels().size() > 1;
					boolean secondElementElementIsQuery = models.get(0) instanceof QueryModel;
					boolean shouldAddParenthesesForSecondElement = secondElementHasMoreThanOneChild || secondElementElementIsQuery;
					
					if(shouldAddParenthesesForSecondElement) {
						builder.append(" (");
					}
					 
					builder.append(" " + models.get(i + 1).getCQL(identation));
					
					if(shouldAddParenthesesForSecondElement) {
						builder.append(" )");
					}
				}
			}
		}

		return builder.toString().trim();
	}
	
	public List<IExpressionBuilderModel> getChildModels() {
		return this.models;
	}

	public void appendExpression(IExpressionBuilderModel iExpressionBuilderModel) {
		this.models.add(iExpressionBuilderModel);
	}

	@Override
	public CQLType getType() {
		return CQLType.ANY;
	}

	public IExpressionBuilderModel getParentModel() {
		return parentModel;
	}

	public void setParentModel(IExpressionBuilderModel parentModel) {
		this.parentModel = parentModel;
	}

	@Override
	public String getDisplayName() {
		return "";
	}
}
