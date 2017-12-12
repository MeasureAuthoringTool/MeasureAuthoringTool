package mat.client.clause.cqlworkspace;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import mat.client.clause.cqlworkspace.model.PopulationsObject;

public interface CQLPopulationDetail {
	public Button addButtonClicked();
	void displayPopulationDetail(FlowPanel mainFlowPanel);
	public PopulationsObject getPopulationsObject();
	public void setObserver(CQLPopulationObserver observer); 
	public CQLPopulationObserver getObserver(); 
}
