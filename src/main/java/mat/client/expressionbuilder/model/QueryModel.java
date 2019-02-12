package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;

public class QueryModel extends ExpressionBuilderModel {

	private ExpressionBuilderModel source;
	private String alias;
	private ExpressionBuilderModel filter;

	public QueryModel(ExpressionBuilderModel source, String alias, ExpressionBuilderModel filter) {
		this.source = source;
		this.alias = alias;
		this.filter = filter;
	}

	public QueryModel() {
		this.source = new ExpressionBuilderModel();
		this.filter = new ExpressionBuilderModel(); 
		this.alias = "";
	}
	
	public ExpressionBuilderModel getSource() {
		return source;
	}

	public String getAlias() {
		return alias;
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}

	public ExpressionBuilderModel getFilter() {
		return filter;
	}
	
	@Override
	public String getCQL(String identation) {
		StringBuilder builder = new StringBuilder();
		builder.append(identation + "( ");
		
		builder.append(source.getCQL(""));
		builder.append(" ");
		builder.append(alias);
		
		String filterIdentationIdentation = identation + "  ";
		builder.append("\n" + filterIdentationIdentation);
		builder.append("where ");
		
		
		if(this.getChildModels().size() == 1) {
			builder.append(this.getChildModels().get(0).getCQL(identation));
		} else {
			if (!filter.getChildModels().isEmpty()) {
				builder.append(filter.getChildModels().get(0).getCQL(identation));

				String innerIdentation = identation + "  ";
				for (int i = 1; i < filter.getChildModels().size(); i += 2) {
					builder.append("\n");
					builder.append(innerIdentation);
					builder.append(filter.getChildModels().get(i).getCQL(innerIdentation));
					
					if((i + 1) <= filter.getChildModels().size() - 1) {
						builder.append(" " + filter.getChildModels().get(i + 1).getCQL(innerIdentation));
					}
				}
			}
		}
				
		builder.append("\n" + identation + ")");
		return builder.toString();
	}

	@Override
	public CQLType getType() {
		return CQLType.LIST;
	}
}
