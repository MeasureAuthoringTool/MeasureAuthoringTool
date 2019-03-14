package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;

public class NotModel extends ExpressionBuilderModel {

	public NotModel(ExpressionBuilderModel parent) {
		super(parent);
	}

	@Override
	public String getCQL(String indentation) {

		StringBuilder builder = new StringBuilder();
		builder.append("not ");
		
		if(this.getChildModels().size() == 1) {
			builder.append(this.getChildModels().get(0).getCQL(indentation));
		} else {
			if (!this.getChildModels().isEmpty()) {
				builder.append("( ");
				builder.append(this.getChildModels().get(0).getCQL(indentation));

				indentation = indentation + "  ";
				for (int i = 1; i < this.getChildModels().size(); i += 2) {
					builder.append("\n");
					builder.append(indentation);
					builder.append(this.getChildModels().get(i).getCQL(indentation));
					
					if((i + 1) <= this.getChildModels().size() - 1) {
						builder.append(" " + this.getChildModels().get(i + 1).getCQL(indentation));
					}
				}
				
				builder.append("\n)");
			}
		}
		
		
		return builder.toString();
	}
	
	@Override
	public CQLType getType() {
		return CQLType.BOOLEAN;
	}

	@Override
	public String getDisplayName() {
		return ExpressionType.NOT.getDisplayName();
	}
}
