package mat.client.clause.clauseworkspace.presenter;

import java.util.HashMap;
import java.util.Map;

import mat.client.MatPresenter;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.MatContext;
import mat.client.shared.MatTabLayoutPanel;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;

import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class ClauseWorkspacePresenter implements MatPresenter {

	public static interface XmlTreeDisplay {
		public CellTree getXmlTree();

		public Button getSaveButton();

		public Widget asWidget();
		
		public SuccessMessageDisplay getSuccessMessageDisplay();
		
		public ErrorMessageDisplay getErrorMessageDisplay();
		
		public void clearMessages();
		
		public void setEnabled(boolean enable);
		
	}

		
	private SimplePanel emptyWidget = new SimplePanel();
	private SimplePanel simplepanel = new SimplePanel();
	FlowPanel flowPanel = new FlowPanel();
	MeasureServiceAsync service = MatContext.get().getMeasureService();
	
	
	private MatTabLayoutPanel clauseWorkspaceTabs;
	private PopulationClausePresenter populationClausePresenter = new PopulationClausePresenter();
	private MeasureObsClausePresenter measureObsClausePresenter = new MeasureObsClausePresenter();
	private StratificationClausePresenter stratificationClausePresenter = new StratificationClausePresenter();

	public ClauseWorkspacePresenter() {
		emptyWidget.add(new Label("No Measure Selected"));
		simplepanel.setStyleName("contentPanel");
		clauseWorkspaceTabs = new MatTabLayoutPanel(true);
		clauseWorkspaceTabs.setId("clauseWorkspce");
		clauseWorkspaceTabs.addPresenter(populationClausePresenter, "Populations");
		clauseWorkspaceTabs.addPresenter(measureObsClausePresenter, "Measure Observations");
		clauseWorkspaceTabs.addPresenter(stratificationClausePresenter, "Stratification");
		flowPanel.add(new SpacerWidget());
		flowPanel.add(clauseWorkspaceTabs);
		simplepanel.add(flowPanel);
	}


	@Override
	public void beforeDisplay() {
		String currentMeasureId = MatContext.get().getCurrentMeasureId();	
		if(currentMeasureId != null && !"".equals(currentMeasureId)) {
			clauseWorkspaceTabs.selectTab(populationClausePresenter);
			populationClausePresenter.beforeDisplay();
		}else{
			displayEmpty();
		}
	}
	
	@Override
	public void beforeClosingDisplay() {

	}

	@Override
	public Widget getWidget() {
		return simplepanel;
	}

	private void displayEmpty() {
		simplepanel.clear();
		simplepanel.add(emptyWidget);
	}


}
