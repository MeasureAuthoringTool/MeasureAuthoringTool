package mat.client.clause.cqlworkspace;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import mat.client.clause.cqlworkspace.model.PopulationsObject;

public interface CQLPopulationDetail {
	public Button getAddButton();
	public Button getSaveButton();
	void displayPopulationDetail(FlowPanel mainFlowPanel);
	public PopulationsObject getPopulationsObject();
	public void setObserver(CQLPopulationObserver observer); 
	public CQLPopulationObserver getObserver(); 
	public boolean isDirty();
	public void setIsDirty(boolean isDirty);
}
