package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;

import java.util.ArrayList;
import java.util.List;

public class ExpressionBuilderModel implements IExpressionBuilderModel {
	private IExpressionBuilderModel parentModel;
	private List<IExpressionBuilderModel> models;
	

	public ExpressionBuilderModel(ExpressionBuilderModel parent) {
		this.parentModel = parent;
		models = new ArrayList<>();
	}

	@Override
	public String getCQL(String indentation) {
		StringBuilder builder = new StringBuilder();
		if (!models.isEmpty()) {			
			
			// if the current model has more than one child element and it's parent is not the main model, put parentheses around the whole
			// expression
			if(this.getChildModels().size() > 1 && (this.getParentModel() != null)) {
				builder.append("(");
			}
			
			if(this.getChildModels().size() > 1) {
				builder.append("(");
			}
			
			// this the first element
			builder.append(models.get(0).getCQL(indentation));
			
			if(this.getChildModels().size() > 1) {
				builder.append(")");
			}
			

			indentation = indentation + "  ";
			for (int i = 1; i < models.size(); i += 2) {
				builder.append("\n");
				// this appends the operator
				builder.append(models.get(i).getCQL(indentation));
					
				if(this.getChildModels().size() > 1) {
					builder.append(" (");
				}
				
				// this appends the subsequent elements
				if((i + 1) <= models.size() - 1) {		
					builder.append(" " + models.get(i + 1).getCQL(indentation));
					
					if(this.getChildModels().size() > 1) {
						builder.append(")");
					}
				}
			}
			
			// if the current model has more than one child element and it's parent is not the main model, put parentheses around the whole
			// expression
			if(this.getChildModels().size() > 1 && (this.getParentModel() != null)) {
				builder.append(")");
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
