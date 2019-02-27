package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;

public class MembershipInModel extends ExpressionBuilderModel {

	private final ExpressionBuilderModel rightHandSide;
	private final ExpressionBuilderModel leftHandSide;

	public MembershipInModel() {
		this.rightHandSide = new ExpressionBuilderModel();
		this.leftHandSide = new ExpressionBuilderModel();
	}
		
	public ExpressionBuilderModel getRightHandSide() {
		return rightHandSide;
	}

	public ExpressionBuilderModel getLeftHandSide() {
		return leftHandSide;
	}
	
	@Override
	public String getCQL(String identation) {
		final StringBuilder builder = new StringBuilder();
		for(final IExpressionBuilderModel model : leftHandSide.getChildModels()) {
			builder.append(" ");
			builder.append(model.getCQL(identation));
		}
		
		builder.append(" ");
		builder.append("in");
		
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
		return ExpressionType.IN.getDisplayName();
	}
}