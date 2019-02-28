package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;

public class FunctionModel extends ExpressionBuilderModel {
	private String name;

	public FunctionModel(ExpressionBuilderModel parent) {
		super(parent);
		this.name = "";
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	@Override
	public String getCQL(String identation) {
		StringBuilder builder = new StringBuilder();
		
		if(!this.name.isEmpty()) {
			builder.append(name);
			builder.append("(");
			builder.append(")");
		}

		return builder.toString();
	}
	
	@Override
	public CQLType getType() {
		return CQLType.ANY;
	}
	
	@Override
	public String getDisplayName() {
		return ExpressionType.FUNCTION.getDisplayName();
	}
	
	
}
