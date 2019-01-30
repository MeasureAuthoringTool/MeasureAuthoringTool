package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;

public class IsTrueFalseModel extends ExpressionBuilderModel {
	
	private String operatorText;
	
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
}
