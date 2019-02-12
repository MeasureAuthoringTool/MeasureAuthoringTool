package mat.client.expressionbuilder.model;

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
		
		builder.append(source.getCQL(""));
		builder.append(" ");
		builder.append(alias);
		
		identation = identation + " ";
		builder.append("\n" + identation + "  ");
		builder.append("where");
		
		builder.append(filter.getCQL(identation));
		
		return builder.toString();
	}
}
