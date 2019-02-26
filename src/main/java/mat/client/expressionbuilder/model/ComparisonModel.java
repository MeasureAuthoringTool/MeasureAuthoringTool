package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;

public class ComparisonModel extends ExpressionBuilderModel {

	private ExpressionBuilderModel rightHandSide;
	private ExpressionBuilderModel leftHandSide;
	private ComparisonOperatorModel comparisonOperator;
		
	public ComparisonModel() {
		this.rightHandSide = new ExpressionBuilderModel();
		this.leftHandSide = new ExpressionBuilderModel();
		comparisonOperator = new ComparisonOperatorModel("");
	}
		
	public ExpressionBuilderModel getRightHandSide() {
		return rightHandSide;
	}

	public ExpressionBuilderModel getLeftHandSide() {
		return leftHandSide;
	}

	public void setComparisonOperator(String comparisonOperator) {
		this.comparisonOperator = new ComparisonOperatorModel(comparisonOperator);
	}
	
	@Override
	public String getCQL(String identation) {
		StringBuilder builder = new StringBuilder();
		for(IExpressionBuilderModel model : leftHandSide.getChildModels()) {
			builder.append(" ");
			builder.append(model.getCQL(identation));
		}
		
		builder.append(" ");
		builder.append(this.comparisonOperator.getCQL(""));
		
		for(IExpressionBuilderModel model : rightHandSide.getChildModels()) {
			builder.append(" ");
			builder.append(model.getCQL(identation));
		}
		
		return builder.toString().trim();
	}
	
	@Override
	public CQLType getType() {
		return CQLType.BOOLEAN;
	}
	
	@Override
	public String getDisplayName() {
		return ExpressionType.COMPARISON.getDisplayName();
	}
}
