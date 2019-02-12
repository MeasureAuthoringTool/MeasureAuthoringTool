package mat.client.expressionbuilder.model;

import java.util.ArrayList;
import java.util.List;

public class QueryModel extends ExpressionBuilderModel {

	private List<IExpressionBuilderModel> source;
	private String alias;
	private List<IExpressionBuilderModel> filter;

	public QueryModel(List<IExpressionBuilderModel> source, String alias, List<IExpressionBuilderModel> filter) {
		this.source = source;
		this.alias = alias;
		this.filter = filter;
	}

	public QueryModel() {
		this.source = new ArrayList<>();
		this.filter = new ArrayList<>();
		this.alias = "";
	}
	
	public List<IExpressionBuilderModel> getSource() {
		return source;
	}

	public String getAlias() {
		return alias;
	}

	public List<IExpressionBuilderModel> getFilter() {
		return filter;
	}
	
	@Override
	public String getCQL(String identation) {
		StringBuilder builder = new StringBuilder();
		
		for(IExpressionBuilderModel model : source) {
			builder.append(model.getCQL(identation));
		}
		
		builder.append("\n" + identation + "  ");
		builder.append("where");
		
		for(IExpressionBuilderModel model : filter) {
			builder.append(model.getCQL(identation));
		}
		
		return builder.toString();
	}
}
