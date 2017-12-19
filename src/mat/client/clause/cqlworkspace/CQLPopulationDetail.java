package mat.client.clause.cqlworkspace;

import java.util.List;

import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.user.client.ui.Grid;

import mat.client.clause.cqlworkspace.model.PopulationClauseObject;
import mat.client.clause.cqlworkspace.model.PopulationsObject;

public interface CQLPopulationDetail {
	void displayPopulationDetail(FlowPanel mainFlowPanel);
	public PopulationsObject getPopulationsObject();
	public void setObserver(CQLPopulationObserver observer); 
	public CQLPopulationObserver getObserver(); 
	public boolean isDirty();
	public void setIsDirty(boolean isDirty);
	public void populateGrid(List<PopulationClauseObject> popClauses, Grid populationGrid, int i);
}
