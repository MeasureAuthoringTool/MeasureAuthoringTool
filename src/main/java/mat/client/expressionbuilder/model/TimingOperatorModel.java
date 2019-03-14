package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.TimingOperator;

public class TimingOperatorModel extends ExpressionBuilderModel implements OperatorModel {

	private String value;
	private TimingOperator timingOperator;

	public TimingOperatorModel(ExpressionBuilderModel parent, String value, TimingOperator timingOperator) {
		super(parent);
		this.value = value;
		this.timingOperator = timingOperator;
	}
	
	public String getCQL(String indentation) {
		return value;
	}
	
	public TimingOperator getTimingOperator() {
		return this.timingOperator;
	}
}
