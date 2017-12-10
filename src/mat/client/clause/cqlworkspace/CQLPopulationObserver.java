package mat.client.clause.cqlworkspace;

import mat.client.clause.cqlworkspace.model.PopulationClauseObject;

public interface CQLPopulationObserver {

	void onDeleteClick(String definitionName);

	void onViewHRClick(PopulationClauseObject population);

}
