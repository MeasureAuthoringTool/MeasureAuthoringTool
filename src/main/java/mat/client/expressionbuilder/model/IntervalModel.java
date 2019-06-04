package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;

public class IntervalModel extends ExpressionBuilderModel {

	private ExpressionBuilderModel lowerBound;
	private ExpressionBuilderModel upperBound;
	private boolean isLowerBoundInclusive = true;
	private boolean isUpperBoundInclusive = true;
	
	public IntervalModel(ExpressionBuilderModel parent) {
		super(parent);
		lowerBound = new ExpressionBuilderModel(this);
		upperBound = new ExpressionBuilderModel(this);
	}

	public ExpressionBuilderModel getLowerBound() {
		return lowerBound;
	}
	public ExpressionBuilderModel getUpperBound() {
		return upperBound;
	}

	public boolean isLowerBoundInclusive() {
		return isLowerBoundInclusive;
	}

	public void setLowerBoundInclusive(boolean isLowerBoundInclusive) {
		this.isLowerBoundInclusive = isLowerBoundInclusive;
	}

	public boolean isUpperBoundInclusive() {
		return isUpperBoundInclusive;
	}

	public void setUpperBoundInclusive(boolean isUpperBoundInclusive) {
		this.isUpperBoundInclusive = isUpperBoundInclusive;
	}
	
	@Override
	public String getCQL(String indentation) {
		StringBuilder builder = new StringBuilder();
		builder.append("Interval");
		if(isLowerBoundInclusive) {
			builder.append("[");
		} else {
			builder.append("(");
		}
		
		
		builder.append(lowerBound.getCQL(""));
		builder.append(", ");
		builder.append(upperBound.getCQL(""));
		
		if(isUpperBoundInclusive) {
			builder.append("]");
		} else {
			builder.append(")");
		}
		
		return builder.toString();
	}
	
	@Override
	public CQLType getType() {
		return CQLType.INTERVAL;
	}
	
	@Override
	public String getDisplayName() {
		return ExpressionType.INTERVAL.getDisplayName();
	}
}
