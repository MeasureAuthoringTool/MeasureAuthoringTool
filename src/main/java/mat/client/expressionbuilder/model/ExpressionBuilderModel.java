package mat.client.expressionbuilder.model;

import java.util.ArrayList;
import java.util.List;

import mat.client.expressionbuilder.constant.CQLType;

public class ExpressionBuilderModel implements IExpressionBuilderModel {
	List<IExpressionBuilderModel> models;

	public ExpressionBuilderModel() {
		models = new ArrayList<>();
	}

	public String getCQL() {
		StringBuilder builder = new StringBuilder();

		if (!models.isEmpty()) {
			builder.append(models.get(0).getCQL());

			for (int i = 1; i < models.size(); i += 2) {
				builder.append("\n");
				builder.append("  ");
				builder.append(models.get(i).getCQL());
				
				if((i + 1) <= models.size() - 1) {
					builder.append(" " + models.get(i + 1).getCQL());
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
}
