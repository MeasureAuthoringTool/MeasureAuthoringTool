package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;

public class ComputationModel extends ExpressionBuilderModel {

	private final ExpressionBuilderModel leftHandSide;
	private final ExpressionBuilderModel rightHandSide;
	private ComparisonOperatorModel computationOperator;
		
	public ComputationModel() {
		leftHandSide = new ExpressionBuilderModel();
		rightHandSide = new ExpressionBuilderModel();
		computationOperator = new ComparisonOperatorModel("");
	}

	public ExpressionBuilderModel getLeftHandSide() {
		return leftHandSide;
	}

	public ExpressionBuilderModel getRightHandSide() {
		return rightHandSide;
	}

	public void setComputationOperator(String computationOperator) {
		this.computationOperator = new ComparisonOperatorModel(computationOperator);
	}
	
	@Override
	public String getCQL(String identation) {
		final StringBuilder builder = new StringBuilder();
		for(final IExpressionBuilderModel model : leftHandSide.getChildModels()) {
			builder.append(" ");
			builder.append(model.getCQL(identation));
		}
		
		builder.append(" ");
		builder.append(computationOperator.getCQL(""));
		
		for(final IExpressionBuilderModel model : rightHandSide.getChildModels()) {
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
		return ExpressionType.COMPUTATION.getDisplayName();
	}
}
