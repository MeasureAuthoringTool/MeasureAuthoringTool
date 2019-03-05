package mat.client.expressionbuilder.util;

import java.util.ArrayList;
import java.util.List;

import mat.client.expressionbuilder.model.IExpressionBuilderModel;
import mat.client.expressionbuilder.model.QueryModel;
import mat.client.expressionbuilder.model.QuerySortModel;

public class QueryFinderHelper {

	private QueryFinderHelper() {
		throw new IllegalStateException();
	}
	
	public static boolean isPartOfSort(IExpressionBuilderModel currentModel) {
		if(currentModel == null) {
			return false;
		}
		
		else if(currentModel instanceof QuerySortModel) {
			return true;
		}
		
		else {
			return isPartOfSort(currentModel.getParentModel());
		}
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
		
		if(currentModel instanceof QueryModel) {
			QueryModel queryModel = (QueryModel) currentModel;
			if(!queryModel.getAlias().isEmpty()) {
				aliasNames.add(queryModel.getAlias());
			}
			
			return;
		}
						
		findAliasNames(currentModel.getParentModel(), aliasNames);
	}	
}
