package mat.client.clause.clauseworkspace.presenter;

import mat.client.MatPresenter;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.MatContext;
import mat.client.shared.MatTabLayoutPanel;
import mat.client.shared.SpacerWidget;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class ClauseWorkspacePresenter implements MatPresenter {

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
//		clauseWorkspaceTabs = new MatTabLayoutPanel(true);
//		clauseWorkspaceTabs.setId("clauseWorkspce");
//		clauseWorkspaceTabs.addPresenter(populationClausePresenter, "Populations");
//		clauseWorkspaceTabs.addPresenter(measureObsClausePresenter, "Measure Observations");
//		clauseWorkspaceTabs.addPresenter(stratificationClausePresenter, "Stratification");
//		flowPanel.add(new SpacerWidget());
//		flowPanel.add(clauseWorkspaceTabs);
		simplepanel.add(flowPanel);
		
		
	}


	private void setXMLOnTabs() {
		String currentMeasureId = MatContext.get().getCurrentMeasureId();
		if(currentMeasureId != null && !"".equals(currentMeasureId)) {
			MeasureServiceAsync service = MatContext.get().getMeasureService();
			service.getMeasureXmlForMeasure(MatContext.get()
					.getCurrentMeasureId(),
					new AsyncCallback<MeasureXmlModel>() {// Loading the measure's SimpleXML from the Measure_XML table 

						@Override
						public void onSuccess(MeasureXmlModel result) {
							String xml = result != null ? result.getXml() : null;
							com.google.gwt.xml.client.Document document = XMLParser.parse(xml);
							NodeList nodeList = document.getElementsByTagName("scoring");
							
							if(nodeList != null && nodeList.getLength() > 0){
								Node scoringNode = nodeList.item(0);
								Node scoringIdAttribute = scoringNode.getAttributes().getNamedItem("id");
								String scoringIdAttributeValue = scoringIdAttribute.getNodeValue();
								
								if("PROPOR".equals(scoringIdAttributeValue) || "RATIO".equals(scoringIdAttributeValue)){
									clauseWorkspaceTabs = new MatTabLayoutPanel(true);
									clauseWorkspaceTabs.setId("clauseWorkspce");
									clauseWorkspaceTabs.addPresenter(populationClausePresenter, "Populations");
									clauseWorkspaceTabs.addPresenter(stratificationClausePresenter, "Stratification");
									
									NodeList nodes = document.getElementsByTagName("measureObservations");
									if(nodes != null && nodes.getLength() > 0){
										Node measureObs = nodes.item(0);
										Node parentNode = measureObs.getParentNode();
										parentNode.removeChild(measureObs);
									}
								}else{
									clauseWorkspaceTabs = new MatTabLayoutPanel(true);
									clauseWorkspaceTabs.setId("clauseWorkspce");
									clauseWorkspaceTabs.addPresenter(populationClausePresenter, "Populations");
									clauseWorkspaceTabs.addPresenter(measureObsClausePresenter, "Measure Observations");
									clauseWorkspaceTabs.addPresenter(stratificationClausePresenter, "Stratification");
								}
								flowPanel.clear();
								flowPanel.add(new SpacerWidget());
								flowPanel.add(clauseWorkspaceTabs);
								
								String newXML = document.getDocumentElement().toString();
								System.out.println("newXML:"+newXML);
								populationClausePresenter.setOriginalXML(newXML);
								measureObsClausePresenter.setOriginalXML(newXML);
								stratificationClausePresenter.setOriginalXML(newXML);
								
								clauseWorkspaceTabs.selectTab(populationClausePresenter);
								populationClausePresenter.beforeDisplay();
							}
						}
						@Override
						public void onFailure(Throwable caught) {
							System.out.println("Server call failed in ClauseWorkspacePresenter.setXMLOnTabs() in service.getMeasureXmlForMeasure");
						}
		});
		}else{
			displayEmpty();
		}
		
	}


	@Override
	public void beforeDisplay() {
		setXMLOnTabs();
//		String currentMeasureId = MatContext.get().getCurrentMeasureId();	
//		if(currentMeasureId != null && !"".equals(currentMeasureId)) {
//			clauseWorkspaceTabs.selectTab(populationClausePresenter);
//			populationClausePresenter.beforeDisplay();
//		}else{
//			displayEmpty();
//		}
		
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
