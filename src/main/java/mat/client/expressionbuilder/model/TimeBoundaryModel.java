package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;

public class TimeBoundaryModel extends ExpressionBuilderModel {
	
	public TimeBoundaryModel(ExpressionBuilderModel parent) {
		super(parent);
	}

	private String operatorText = "";
	
	public void setOperatorText(String operatorText) {
		this.operatorText = operatorText;
	}

	@Override
	public String getCQL(String indentation) {
		final StringBuilder builder = new StringBuilder();
		builder.append(operatorText.toLowerCase() + " ");

		for(int i = 0; i < getChildModels().size(); i++) {
			builder.append(getChildModels().get(i).getCQL(indentation));
		}
		
		builder.append(" ");
		return builder.toString();
	}
	
	@Override
	public CQLType getType() {
		return CQLType.BOOLEAN;
	}
	
	@Override
	public String getDisplayName() {
		return ExpressionType.TIME_BOUNDARY.getDisplayName();
	}
}
