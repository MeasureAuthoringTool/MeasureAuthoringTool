package mat.client.expressionbuilder.util;

import java.util.ArrayList;
import java.util.List;

import mat.client.expressionbuilder.model.IExpressionBuilderModel;
import mat.client.expressionbuilder.model.QueryModel;

public class QueryAliasFinder {

	private QueryAliasFinder() {
		throw new IllegalStateException();
	}
	
	public static List<String> findAliasNames(IExpressionBuilderModel currentModel) {
		List<String> aliasNames = new ArrayList<>();
		findAliasNames(currentModel, aliasNames);
		return aliasNames;
	}
	
	private static void findAliasNames(IExpressionBuilderModel currentModel, List<String> aliasNames) {
		if(currentModel == null) {
			return;
		}
		
		System.out.println("LOOKING AT A : " + currentModel.getDisplayName());
		if(currentModel instanceof QueryModel) {
			QueryModel queryModel = (QueryModel) currentModel;
			aliasNames.add(queryModel.getAlias());
			return;
		}
						
		findAliasNames(currentModel.getParentModel(), aliasNames);
	}	
}
