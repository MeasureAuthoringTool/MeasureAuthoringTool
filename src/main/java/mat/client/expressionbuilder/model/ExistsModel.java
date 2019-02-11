package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;

public class ExistsModel extends ExpressionBuilderModel {
	
	@Override
	public String getCQL(String identation) {
		StringBuilder builder = new StringBuilder();
		builder.append("exists ");
		
		if(this.getChildModels().size() == 1) {
			builder.append(this.getChildModels().get(0).getCQL(identation));
		} else {
			if (!this.getChildModels().isEmpty()) {
				builder.append("( ");
				builder.append(this.getChildModels().get(0).getCQL(identation));

				identation = identation + "  ";
				for (int i = 1; i < this.getChildModels().size(); i += 2) {
					builder.append("\n");
					builder.append(identation);
					builder.append(this.getChildModels().get(i).getCQL(identation));
					
					if((i + 1) <= this.getChildModels().size() - 1) {
						builder.append(" " + this.getChildModels().get(i + 1).getCQL(identation));
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
}