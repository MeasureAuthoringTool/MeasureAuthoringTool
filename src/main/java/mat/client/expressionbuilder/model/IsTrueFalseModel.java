package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;

public class IsTrueFalseModel extends ExpressionBuilderModel {
	
	public IsTrueFalseModel(ExpressionBuilderModel parent) {
		super(parent);
	}

	private String operatorText = "";
	
	public void setOperatorText(String operatorText) {
		this.operatorText = operatorText;
	}

	@Override
	public String getCQL(String identation) {
		StringBuilder builder = new StringBuilder();
		builder.append(" ");

		for(int i = 0; i < this.getChildModels().size(); i++) {
			builder.append(this.getChildModels().get(i).getCQL(identation));
		}
		
		builder.append(" " + operatorText);
		return builder.toString();
	}
	
	@Override
	public CQLType getType() {
		return CQLType.BOOLEAN;
	}
	
	@Override
	public String getDisplayName() {
		return ExpressionType.IS_TRUE_FALSE.getDisplayName();
	}
}
