package mat.client.clause.clauseworkspace.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mat.client.MatPresenter;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.codelist.service.CodeListServiceAsync;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.MatContext;
import mat.client.shared.MatTabLayoutPanel;
import mat.client.shared.PreviousContinueButtonBar;
import mat.client.shared.SpacerWidget;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.NamedNodeMap;
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
		simplepanel.add(flowPanel);
		MatContext.get().getAllOperators();
		loadAllUnits();
	}


	private void loadAllUnits() {
		CodeListServiceAsync codeListServiceAsync = MatContext.get().getCodeListService();
		codeListServiceAsync.getAllUnits(new AsyncCallback<List<String>>(){

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Unable to Load Units ");
			}

			@Override
			public void onSuccess(List<String> result) {
				ClauseConstants.units = (ArrayList<String>) result;
				
			}
			
			});
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
								setQdmElementsMap(xml);
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

	
	private void setQdmElementsMap(String xml) {
		Map<String, Node> qdmElementLookUps = new HashMap<String, Node>();
		Document document = XMLParser.parse(xml);
		NodeList nodeList = document.getElementsByTagName("elementLookUp");
		if(null != nodeList && nodeList.getLength() > 0){
			NodeList qdms = nodeList.item(0).getChildNodes();
			for (int i = 0; i < qdms.getLength() ; i++) {
				if("qdm".equals(qdms.item(i).getNodeName())){
					NamedNodeMap namedNodeMap = qdms.item(i).getAttributes();
					String isSupplementData = namedNodeMap.getNamedItem("suppDataElement").getNodeValue();
					if(isSupplementData.equals("false")){//filter supplementDataElements from elementLookUp
						String name = namedNodeMap.getNamedItem("name").getNodeValue();
						
						if(namedNodeMap.getNamedItem("instance") != null){
							name = namedNodeMap.getNamedItem("instance").getNodeValue() + " of " + name;
						}
					
						if(namedNodeMap.getNamedItem("datatype") != null){
							name = name + ": " + namedNodeMap.getNamedItem("datatype").getNodeValue();
						}
						qdmElementLookUps.put(name, qdms.item(i));
					}
				}
			}
		}
		ClauseConstants.elementLookUps = qdmElementLookUps;
	}

	@Override
	public void beforeDisplay() {
		setXMLOnTabs();
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


	public XmlTreePresenter getSelectedTreePresenter(){		
		MatPresenter matPresenter = clauseWorkspaceTabs.getPresenterMap().get(clauseWorkspaceTabs.getSelectedIndex());
		return (XmlTreePresenter)matPresenter;
	}


}
