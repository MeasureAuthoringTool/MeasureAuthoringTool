package mat.client.expressionbuilder.model;

import java.util.ArrayList;
import java.util.List;

import mat.client.expressionbuilder.constant.CQLType;

public class ExpressionBuilderModel implements IExpressionBuilderModel {
	private IExpressionBuilderModel parentModel;
	private List<IExpressionBuilderModel> models;
	

	public ExpressionBuilderModel() {
		models = new ArrayList<>();
	}

	public String getCQL(String identation) {
		StringBuilder builder = new StringBuilder();

		
		
		if (!models.isEmpty()) {
			
			boolean shouldAddParentheses = models.size() > 1 ;

			if(shouldAddParentheses) {
				builder.append("( ");
			}
			
			
			builder.append(models.get(0).getCQL(identation));

			if(shouldAddParentheses) {
				builder.append("\n");
				builder.append(identation + ")");
			}
			
			
			identation = identation + "  ";
			for (int i = 1; i < models.size(); i += 2) {
				builder.append("\n");
				
				builder.append(models.get(i).getCQL(identation));
				
				if(shouldAddParentheses) {
					builder.append(" ( ");
				}
				
				
				if((i + 1) <= models.size() - 1) {
					builder.append(" " + models.get(i + 1).getCQL(identation));
				}
				
				if(shouldAddParentheses) {
					builder.append("\n");
					builder.append(identation + ")");
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
}
