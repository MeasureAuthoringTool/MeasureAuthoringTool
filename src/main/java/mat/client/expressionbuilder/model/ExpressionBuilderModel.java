package mat.client.expressionbuilder.model;

import java.util.ArrayList;
import java.util.List;

public class ExpressionBuilderModel {
	List<ExpressionBuilderModel> models;
	
	public ExpressionBuilderModel() {
		models = new ArrayList<>();
	}
	
	public String getCQL() {
		StringBuilder builder = new StringBuilder();
		for(ExpressionBuilderModel model : models) {
			builder.append(" ").append(model.getCQL());
		}
		
		return builder.toString().trim();
	}
	
	public void appendExpression(ExpressionBuilderModel model) {
		this.models.add(model);
	}
}
