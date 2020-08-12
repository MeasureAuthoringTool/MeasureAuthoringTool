package mat.client.populationworkspace;

import com.google.gwt.user.client.ui.Grid;
import mat.client.populationworkspace.model.PopulationClauseObject;
import mat.client.populationworkspace.model.PopulationsObject;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import java.util.List;

public interface CQLPopulationDetail {
	void displayPopulationDetail(FlowPanel mainFlowPanel);
	public PopulationsObject getPopulationsObject();
	public void setObserver(CQLPopulationObserver observer); 
	public CQLPopulationObserver getObserver(); 
	public boolean isDirty();
	public void setIsDirty(boolean isDirty);
	public void populateGrid(List<PopulationClauseObject> popClauses, Grid populationGrid, int i);
}
