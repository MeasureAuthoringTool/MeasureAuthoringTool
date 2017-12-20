package mat.client.clause.cqlworkspace;

import com.google.gwt.user.client.ui.Grid;

import mat.client.clause.cqlworkspace.model.PopulationClauseObject;
import mat.client.clause.cqlworkspace.model.PopulationDataModel;
import mat.client.clause.cqlworkspace.model.PopulationsObject;
import mat.client.clause.cqlworkspace.model.StrataDataModel;
import mat.client.clause.cqlworkspace.model.StratificationsObject;

public interface CQLPopulationObserver {

	void onDeleteClick(Grid grid, PopulationClauseObject clauseObject);
	void onSaveClick(PopulationDataModel populationDataModel);
	void onViewHRClick(PopulationClauseObject population);
	void onAddNewClick(Grid populationGrid, PopulationsObject populationsObject);
	void onSaveClick();
	void onAddNewStratificationClick(StrataDataModel strataDataModel);
	void onAddNewStratumClick(StratificationsObject stratificationsObject);
	void onDeleteStratificationClick(Grid stratificationGrid, StratificationsObject stratification); 
	void onDeleteStratumClick(Grid stratificationGrid, StratificationsObject stratification, PopulationClauseObject stratum); 
}
