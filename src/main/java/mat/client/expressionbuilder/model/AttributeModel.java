package mat.client.expressionbuilder.model;

import java.util.ArrayList;
import java.util.List;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;

public class AttributeModel extends ExpressionBuilderModel {
	private List<String> attributes;
	
	public AttributeModel() {
		this.attributes = new ArrayList<>();
	}

	public List<IExpressionBuilderModel> getSource() {
		return this.getChildModels();
	}

	public List<String> getAttributes() {
		return attributes;
	}
	
	@Override
	public String getCQL(String identation) {
		StringBuilder builder = new StringBuilder();
		
		if(!this.getChildModels().isEmpty()) {
			builder.append(this.getChildModels().get(0).getCQL(""));
		}
		
		if(!this.attributes.isEmpty()) {
			for(String attribute : this.attributes) {
				builder.append(".");
				builder.append(attribute);
			}
		}

		
		return builder.toString();
	}
	
	@Override
	public CQLType getType() {
		return CQLType.ANY;
	}
	
	@Override
	public String getDisplayName() {
		return ExpressionType.ATTRIBUTE.getDisplayName();
	}
}
