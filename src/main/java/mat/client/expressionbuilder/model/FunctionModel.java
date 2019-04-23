package mat.client.expressionbuilder.model;

import java.util.ArrayList;
import java.util.List;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;

public class FunctionModel extends ExpressionBuilderModel {
	private String name;
	private List<ExpressionBuilderModel> arguments ;

	public FunctionModel(ExpressionBuilderModel parent) {
		super(parent);
		this.name = "";
		this.arguments = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<ExpressionBuilderModel> getArguments() {
		return arguments;
	}

	public void setArguments(List<ExpressionBuilderModel> arguments) {
		this.arguments = arguments;
	}

	@Override
	public String getCQL(String indentation) {
		StringBuilder builder = new StringBuilder();
		
		if(!this.name.isEmpty()) {
			builder.append(name);
			builder.append("(");
			
			for(ExpressionBuilderModel argument : arguments) {
				builder.append(", ");
				builder.append(argument.getCQL(""));
			}
			
			
			builder.append(")");
		}

		return builder.toString().replaceFirst(", ", "");
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
