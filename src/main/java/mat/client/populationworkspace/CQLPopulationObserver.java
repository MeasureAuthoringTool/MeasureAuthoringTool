package mat.client.populationworkspace;

import com.google.gwt.user.client.ui.Grid;
import mat.client.populationworkspace.model.PopulationClauseObject;
import mat.client.populationworkspace.model.PopulationsObject;
import mat.client.populationworkspace.model.StrataDataModel;
import mat.client.populationworkspace.model.StratificationsObject;

public interface CQLPopulationObserver {

	void onDeleteClick(Grid grid, PopulationClauseObject clauseObject);
	void onViewHRClick(PopulationClauseObject population);
	void onAddNewClick(Grid populationGrid, PopulationsObject populationsObject);
	void onSaveClick(PopulationsObject populationsObject);
	void onSaveClick(StrataDataModel strataDataModel);
	void onAddNewStratificationClick(StrataDataModel strataDataModel);
	void onAddNewStratumClick(StratificationsObject stratificationsObject);
	void onDeleteStratificationClick(Grid stratificationGrid, StratificationsObject stratification); 
	void onDeleteStratumClick(Grid stratificationGrid, StratificationsObject stratification, PopulationClauseObject stratum);
	void clearMessagesOnDropdown();

}
