package mat.client.clause.cqlworkspace;

import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.user.client.ui.Grid;

import mat.client.clause.cqlworkspace.model.PopulationClauseObject;
import mat.client.clause.cqlworkspace.model.PopulationDataModel;
import mat.client.clause.cqlworkspace.model.PopulationsObject;

public interface CQLPopulationObserver {

	void onDeleteClick(String definitionName);
	void onSaveClick(PopulationDataModel populationDataModel);
	void onViewHRClick(PopulationClauseObject population);
	void onAddNewClick(FlowPanel flowPanel, Grid populationGrid, PopulationsObject populationsObject);
}
