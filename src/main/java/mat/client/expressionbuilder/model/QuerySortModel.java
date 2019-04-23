package mat.client.expressionbuilder.model;

public class QuerySortModel extends ExpressionBuilderModel {

	private boolean isAscendingSort = true;
	private ExpressionBuilderModel sortExpression;
	
	public QuerySortModel(ExpressionBuilderModel parent) {
		super(parent);
		sortExpression = new ExpressionBuilderModel(this);
	}
	
	public ExpressionBuilderModel getSortExpression() {
		return sortExpression;
	}
	
	public boolean isAscendingSort() {
		return isAscendingSort;
	}

	public void setAscendingSort(boolean isAscendingSort) {
		this.isAscendingSort = isAscendingSort;
	}

	@Override
	public String getCQL(String indentation) {
		StringBuilder builder = new StringBuilder();
		builder.append("sort by ");
		builder.append(sortExpression.getCQL(""));
		builder.append(" ");
		
		if(isAscendingSort) {
			builder.append("asc");
		} else {
			builder.append("desc");
		}
		
		return builder.toString();
	}
}
