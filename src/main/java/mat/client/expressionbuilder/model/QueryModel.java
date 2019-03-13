package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;

public class QueryModel extends ExpressionBuilderModel {

	private ExpressionBuilderModel source;
	private String alias;
	private ExpressionBuilderModel filter;
	private QuerySortModel sort;

	public QueryModel(ExpressionBuilderModel source, String alias, ExpressionBuilderModel filter, ExpressionBuilderModel parent) {
		super(parent);
		this.source = source;
		this.source.setParentModel(this);
		this.alias = alias;
		this.filter = filter;
		this.filter.setParentModel(this);
	}

	public QueryModel(ExpressionBuilderModel parent) {
		super(parent);
		this.source = new ExpressionBuilderModel(this);
		this.filter = new ExpressionBuilderModel(this); 
		this.sort = new QuerySortModel(this);
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
	
	public QuerySortModel getSort() {
		return sort;
	}	

	@Override
	public String getCQL(String identation) {
		
		boolean shouldAddParentheses = source.getChildModels().size() > 1; 
		
		
		StringBuilder builder = new StringBuilder();
				
		if(shouldAddParentheses) {
			builder.append("( ");
		}
		
		builder.append(source.getCQL(identation + "  "));
		
		if(shouldAddParentheses) {
			builder.append(" )");
		}
		
		builder.append(" ");
		builder.append(alias);
		
		String filterIdentation = identation + "  ";
		builder.append("\n" + filterIdentation);
		builder.append("where ");
		
		
		if(this.getChildModels().size() == 1) {
			builder.append(this.getChildModels().get(0).getCQL(filterIdentation));
		} else {
			if (!filter.getChildModels().isEmpty()) {
				builder.append(filter.getChildModels().get(0).getCQL(filterIdentation));

				String innerIdentation = filterIdentation + "  ";
				for (int i = 1; i < filter.getChildModels().size(); i += 2) {
					builder.append("\n");
					builder.append(filter.getChildModels().get(i).getCQL(innerIdentation));
					
					if((i + 1) <= filter.getChildModels().size() - 1) {
						builder.append(" " + filter.getChildModels().get(i + 1).getCQL(innerIdentation));
					}
				}
			}
		}
				
		if(!sort.getSortExpression().getChildModels().isEmpty()) {
			builder.append("\n" + filterIdentation);
			builder.append(sort.getCQL(""));
		}
				
		return builder.toString();
	}

	@Override
	public CQLType getType() {
		return CQLType.LIST;
	}
	
	@Override
	public String getDisplayName() {
		return ExpressionType.QUERY.getDisplayName();
	}
}
