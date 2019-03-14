package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;

public class TimingModel extends ExpressionBuilderModel {

	private ExpressionBuilderModel leftHandSide;
	private ExpressionBuilderModel rightHandSide;
	private TimingPhraseModel intervalOperatorPhrase;
	
	public TimingModel(ExpressionBuilderModel parent) {
		super(parent);
		leftHandSide = new ExpressionBuilderModel(this);
		rightHandSide = new ExpressionBuilderModel(this);
		this.intervalOperatorPhrase = new TimingPhraseModel(this);
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
	
	public TimingPhraseModel getIntervalOperatorPhrase() {
		return this.intervalOperatorPhrase;
	}
	
	@Override
	public String getCQL(String indentation) {
		StringBuilder builder = new StringBuilder();
		builder.append(leftHandSide.getCQL(""));
		builder.append(" ");
		builder.append(intervalOperatorPhrase.getCQL(""));
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
