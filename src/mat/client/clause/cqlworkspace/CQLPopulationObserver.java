package mat.client.clause.cqlworkspace;

import mat.client.clause.cqlworkspace.model.PopulationClauseObject;
import mat.client.clause.cqlworkspace.model.PopulationDataModel;

public interface CQLPopulationObserver {

	void onDeleteClick(String definitionName);
	void onSaveClick(PopulationDataModel populationDataModel);
	void onViewHRClick(PopulationClauseObject population);
	void onAddNewClick();
}
