package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;

public class TimingModel extends ExpressionBuilderModel {

	private ExpressionBuilderModel leftHandSide;
	private ExpressionBuilderModel rightHandSide;
	private String timingPhrase = "starts before start of"; // TODO: This will change as part of MAT-9751, placeholder for now
	
	public TimingModel(ExpressionBuilderModel parent) {
		super(parent);
		leftHandSide = new ExpressionBuilderModel(this);
		rightHandSide = new ExpressionBuilderModel(this);
	}
	
	public ExpressionBuilderModel getLeftHandSide() {
		return leftHandSide;
	}
	
	public void setLeftHandSide(ExpressionBuilderModel leftHandSideExpression) {
		this.leftHandSide = leftHandSideExpression;
	}
	
	public ExpressionBuilderModel getRightHandSide() {
		return rightHandSide;
	}
	
	public void setRightHandSide(ExpressionBuilderModel rightHandSideExpression) {
		this.rightHandSide = rightHandSideExpression;
	}
	
	@Override
	public String getCQL(String identation) {
		StringBuilder builder = new StringBuilder();
		builder.append(leftHandSide.getCQL(""));
		builder.append(" ");
		builder.append(timingPhrase);
		builder.append(" ");
		builder.append(rightHandSide.getCQL(""));
		return builder.toString();
	}
	
	@Override
	public CQLType getType() {
		return CQLType.BOOLEAN;
	}
	
	@Override
	public String getDisplayName() {
		return ExpressionType.TIMING.getDisplayName();
	}
}
