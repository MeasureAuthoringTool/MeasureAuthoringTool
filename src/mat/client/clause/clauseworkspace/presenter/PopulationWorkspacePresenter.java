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

/**
 * The Class ClauseWorkspacePresenter.
 */
public class PopulationWorkspacePresenter implements MatPresenter {
	
	/** The simplepanel. */
	private SimplePanel simplepanel = new SimplePanel();
	
	/** The flow panel. */
	FlowPanel flowPanel = new FlowPanel();
	
	/** The service. */
	private MeasureServiceAsync service = MatContext.get().getMeasureService();
	
	/** The clause workspace tabs. */
	private MatTabLayoutPanel populationWorkspaceTabs;
	
	/** The population clause presenter. */
	private PopulationClausePresenter populationClausePresenter = new PopulationClausePresenter();
	
	/** The measure obs clause presenter. */
	private MeasureObsClausePresenter measureObsClausePresenter = new MeasureObsClausePresenter();
	
	/** The stratification clause presenter. */
	private StratificationClausePresenter stratificationClausePresenter = new StratificationClausePresenter();
	
	/**
	 * Instantiates a new clause workspace presenter.
	 */
	public PopulationWorkspacePresenter() {
		simplepanel.setStyleName("contentPanel");
		simplepanel.add(flowPanel);
		//MatContext.get().getAllOperators();
		//loadAllUnits();
	}
	
	/**
	 * Load all units.
	 */
	private void loadAllUnits() {
		CodeListServiceAsync codeListServiceAsync = MatContext.get().getCodeListService();
		codeListServiceAsync.getAllUnits(new AsyncCallback<List<String>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Unable to Load Units ");
			}
			
			@Override
			public void onSuccess(List<String> result) {
				PopulationWorkSpaceConstants.units = (ArrayList<String>) result;
			}
		});
	}
	
	/**
	 * Sets the xml on tabs.
	 */
	private void setXMLOnTabs() {
		final String currentMeasureId = MatContext.get().getCurrentMeasureId();
		if ((currentMeasureId != null) && !"".equals(currentMeasureId)) {
			service.getMeasureXmlForMeasure(MatContext.get()
					.getCurrentMeasureId(),
					new AsyncCallback<MeasureXmlModel>() { // Loading the measure's SimpleXML from the Measure_XML table
				@Override
				public void onSuccess(MeasureXmlModel result) {
					try {
						String xml = result != null ? result.getXml() : null;
						com.google.gwt.xml.client.Document document = XMLParser.parse(xml);
						NodeList nodeList = document.getElementsByTagName("scoring");
						
						if ((nodeList != null) && (nodeList.getLength() > 0)) {
							Node scoringNode = nodeList.item(0);
							Node scoringIdAttribute = scoringNode.getAttributes()
									.getNamedItem("id");
							String scoringIdAttributeValue = scoringIdAttribute.getNodeValue();
							
							if ("PROPOR".equals(scoringIdAttributeValue)
									|| "RATIO".equals(scoringIdAttributeValue)
									|| "COHORT".equals(scoringIdAttributeValue)) {
								populationWorkspaceTabs = new MatTabLayoutPanel(true);
								populationWorkspaceTabs.setId("clauseWorkspce");
								populationWorkspaceTabs.addPresenter(
										populationClausePresenter, "Populations");
								populationWorkspaceTabs.addPresenter(
										stratificationClausePresenter, "Stratification");
							} else {
								populationWorkspaceTabs = new MatTabLayoutPanel(true);
								populationWorkspaceTabs.setId("clauseWorkspce");
								populationWorkspaceTabs.addPresenter(
										populationClausePresenter, "Populations");
								populationWorkspaceTabs.addPresenter(
										measureObsClausePresenter, "Measure Observations");
								populationWorkspaceTabs.addPresenter(
										stratificationClausePresenter, "Stratification");
							}
							flowPanel.clear();
							flowPanel.add(new SpacerWidget());
							flowPanel.add(populationWorkspaceTabs);
							
							String newXML = document.getDocumentElement().toString();
							
							populationClausePresenter.setOriginalXML(newXML);
							measureObsClausePresenter.setOriginalXML(newXML);
							stratificationClausePresenter.setOriginalXML(newXML);
							setQdmElementsMap(xml);
							setSubTreeElementsMap(xml);
							populationWorkspaceTabs.selectTab(populationClausePresenter);
							populationClausePresenter.beforeDisplay();
						} else {
							clearPanelAndShowError("Measure Scoring missing in Measure Xml "
									+ currentMeasureId);
						}
					} catch (Exception e) {
						clearPanelAndShowError("Exception Occured in Population Workspace "
								+ currentMeasureId);
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					System.out.println("Server call failed in PopulationWorkspacePresenter.setXMLOnTabs()"
							+ " in service.getMeasureXmlForMeasure");
					clearPanelAndShowError("Loading Measure Xml failed in Population Workspace"
							+ currentMeasureId);
				}
			});
		} else {
			clearPanelAndShowError("Measure Id is null in Population Workspace");
		}
	}
	
	/**
	 * Clear panel and show error.
	 * 
	 * @param auditMessage
	 *            the audit message
	 */
	private void clearPanelAndShowError(String auditMessage) {
		//simplepanel.clear();
		populationWorkspaceTabs = new MatTabLayoutPanel(true);
		populationWorkspaceTabs.setId("PopulationWorkspce");
		populationWorkspaceTabs.addPresenter(populationClausePresenter, "Populations");
		MatContext.get().recordTransactionEvent(MatContext.get().getCurrentMeasureId(),
				null, "CW_TAB_EVENT", auditMessage, ConstantMessages.DB_LOG);
		Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
	}
	
	/**
	 * Sets the qdm elements map.
	 * 
	 * @param xml
	 *            the new qdm elements map
	 */
	private void setQdmElementsMap(String xml) {
		PopulationWorkSpaceConstants.elementLookUpName = new TreeMap<String, String>();
		PopulationWorkSpaceConstants.elementLookUpNode = new TreeMap<String, Node>();
		Document document = XMLParser.parse(xml);
		NodeList nodeList = document.getElementsByTagName("elementLookUp");
		if ((null != nodeList) && (nodeList.getLength() > 0)) {
			NodeList qdms = nodeList.item(0).getChildNodes();
			for (int i = 0; i < qdms.getLength(); i++) {
				if ("qdm".equals(qdms.item(i).getNodeName())) {
					NamedNodeMap namedNodeMap = qdms.item(i).getAttributes();
					String isSupplementData = namedNodeMap.getNamedItem("suppDataElement").getNodeValue();
					if (isSupplementData.equals("false")) { //filter supplementDataElements from elementLookUp
						String name = namedNodeMap.getNamedItem("name").getNodeValue();
						
						if (namedNodeMap.getNamedItem("instance") != null) {
							name = namedNodeMap.getNamedItem("instance").getNodeValue() + " of " + name;
						}
						
						if (namedNodeMap.getNamedItem("datatype") != null) {
							name = name + " : " + namedNodeMap.getNamedItem("datatype").getNodeValue();
						}
						
						String uuid = namedNodeMap.getNamedItem("uuid").getNodeValue();
						PopulationWorkSpaceConstants.elementLookUpNode.put(name + "~" + uuid, qdms.item(i));
						PopulationWorkSpaceConstants.elementLookUpName.put(uuid, name);
					}
				}
			}
		}
	}
	/**
	 * Method to Reterive SubTree Node and corresponding Node Tree and add to SubTreeLookUpNode map.
	 * Also it retrieves Name and UUID and put it in subTreeNodeName map for display.
	 * @param xml - String.
	 */
	private void setSubTreeElementsMap(String xml) {
		PopulationWorkSpaceConstants.subTreeLookUpName = new TreeMap<String, String>();
		PopulationWorkSpaceConstants.subTreeLookUpNode = new TreeMap<String, Node>();
		Document document = XMLParser.parse(xml);
		NodeList nodeList = document.getElementsByTagName("subTreeLookUp");
		if ((null != nodeList) && (nodeList.getLength() > 0)) {
			NodeList subTree = nodeList.item(0).getChildNodes();
			for (int i = 0; i < subTree.getLength(); i++) {
				if ("subTree".equals(subTree.item(i).getNodeName())) {
					NamedNodeMap namedNodeMap = subTree.item(i).getAttributes();
					String name = namedNodeMap.getNamedItem("displayName").getNodeValue();
					String uuid = namedNodeMap.getNamedItem("uuid").getNodeValue();
					PopulationWorkSpaceConstants.subTreeLookUpNode.put(name + "~" + uuid, subTree.item(i));
					PopulationWorkSpaceConstants.subTreeLookUpName.put(uuid, name);
				}
			}
		}
	}
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		setXMLOnTabs();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		return simplepanel;
	}
	
	/**
	 * Gets the selected tree presenter.
	 * 
	 * @return the selected tree presenter
	 */
	public XmlTreePresenter getSelectedTreePresenter() {
		MatPresenter matPresenter = populationWorkspaceTabs.getPresenterMap().get(populationWorkspaceTabs.getSelectedIndex());
		return (XmlTreePresenter) matPresenter;
	}
}
