package mat.client.clause.clauseworkspace.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import mat.client.MatPresenter;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.codelist.service.CodeListServiceAsync;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.MatContext;
import mat.client.shared.MatTabLayoutPanel;
import mat.client.shared.SpacerWidget;
import mat.shared.ConstantMessages;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class ClauseWorkspacePresenter implements MatPresenter {

	private SimplePanel simplepanel = new SimplePanel();
	FlowPanel flowPanel = new FlowPanel();
	MeasureServiceAsync service = MatContext.get().getMeasureService();
	
	
	private MatTabLayoutPanel clauseWorkspaceTabs;
	private PopulationClausePresenter populationClausePresenter = new PopulationClausePresenter();
	private MeasureObsClausePresenter measureObsClausePresenter = new MeasureObsClausePresenter();
	private StratificationClausePresenter stratificationClausePresenter = new StratificationClausePresenter();
	
	public ClauseWorkspacePresenter() {
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
		final String currentMeasureId = MatContext.get().getCurrentMeasureId();
		if(currentMeasureId != null && !"".equals(currentMeasureId)) {
			MeasureServiceAsync service = MatContext.get().getMeasureService();
			service.getMeasureXmlForMeasure(MatContext.get()
					.getCurrentMeasureId(),
					new AsyncCallback<MeasureXmlModel>() {// Loading the measure's SimpleXML from the Measure_XML table 

						@Override
						public void onSuccess(MeasureXmlModel result) {
							try{
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
								}else{
									clearPanelAndShowError("Measure Scoring missing in Measure Xml " + currentMeasureId);
								}
							}catch (Exception e) {
								clearPanelAndShowError("Exception Occured in Clause Workspace " + currentMeasureId);	
							}
						}
						
						@Override
						public void onFailure(Throwable caught) {
							System.out.println("Server call failed in ClauseWorkspacePresenter.setXMLOnTabs() in service.getMeasureXmlForMeasure");
							clearPanelAndShowError("Loading Measure Xml failed in Clause Workspace" + currentMeasureId);
						}
		});
		}else{
			clearPanelAndShowError("Measure Id is null in Clause Workspace");
		}
		
	}

	
	private void clearPanelAndShowError(String auditMessage){
		simplepanel.clear();
		clauseWorkspaceTabs = new MatTabLayoutPanel(true);
		clauseWorkspaceTabs.setId("clauseWorkspce");
		clauseWorkspaceTabs.addPresenter(populationClausePresenter, "Populations");
		MatContext.get().recordTransactionEvent(MatContext.get().getCurrentMeasureId(), null, "CW_TAB_EVENT", auditMessage, ConstantMessages.DB_LOG);
		Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
	}
	
	private void setQdmElementsMap(String xml) {
		ClauseConstants.elementLookUpName = new TreeMap<String, String>();
		ClauseConstants.elementLookUpNode = new TreeMap<String, Node>();
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
							name = name + " : " + namedNodeMap.getNamedItem("datatype").getNodeValue();
						}
						
						String uuid = namedNodeMap.getNamedItem("uuid").getNodeValue();
						ClauseConstants.elementLookUpNode.put(name + "~" + uuid, qdms.item(i));
						ClauseConstants.elementLookUpName.put(uuid, name);
					}
				}
			}
		}
		
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



	public XmlTreePresenter getSelectedTreePresenter(){		
		MatPresenter matPresenter = clauseWorkspaceTabs.getPresenterMap().get(clauseWorkspaceTabs.getSelectedIndex());
		return (XmlTreePresenter)matPresenter;
	}


}
