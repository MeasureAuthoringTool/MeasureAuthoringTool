package mat.client.expressionbuilder.model;

import java.util.ArrayList;
import java.util.List;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;

public class AttributeModel extends ExpressionBuilderModel {
	private ExpressionBuilderModel source;
	private List<String> attributes;
	
	public AttributeModel(ExpressionBuilderModel parent) {
		super(parent);
		this.source = new ExpressionBuilderModel(this);
		this.attributes = new ArrayList<>();
	}

	public ExpressionBuilderModel getSource() {
		return source;
	}

	public List<String> getAttributes() {
		return attributes;
	}
	
	@Override
	public String getCQL(String identation) {
		StringBuilder builder = new StringBuilder();
		
		if(this.source != null) {
			builder.append(this.source.getCQL(""));
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
