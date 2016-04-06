package mat.client.clause.clauseworkspace.presenter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import mat.client.MatPresenter;
import mat.client.clause.QDSAttributesService;
import mat.client.clause.QDSAttributesServiceAsync;
import mat.client.clause.clauseworkspace.model.SortedClauseMapResult;
import mat.client.codelist.service.CodeListServiceAsync;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.MatContext;
import mat.client.shared.MatTabLayoutPanel;
import mat.client.shared.SpacerWidget;
import mat.shared.ConstantMessages;

import com.google.gwt.core.client.GWT;
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

// TODO: Auto-generated Javadoc
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
	
	/** The attribute service. */
	private static QDSAttributesServiceAsync attributeService = (QDSAttributesServiceAsync) GWT
			.create(QDSAttributesService.class);
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
			service.getMeasureXmlForMeasureAndSortedSubTreeMap(MatContext.get()
					.getCurrentMeasureId(),
					new AsyncCallback<SortedClauseMapResult>() { // Loading the measure's SimpleXML from the Measure_XML table
				@Override
				public void onSuccess(SortedClauseMapResult result) {
					try {
						String xml = result != null ? result.getMeasureXmlModel().getXml() : null;
						com.google.gwt.xml.client.Document document = XMLParser.parse(xml);
						PopulationWorkSpaceConstants.subTreeLookUpName = result.getClauseMap();
						NodeList nodeList = document.getElementsByTagName("scoring");
						
						if ((nodeList != null) && (nodeList.getLength() > 0)) {
							Node scoringNode = nodeList.item(0);
							Node scoringIdAttribute = scoringNode.getAttributes()
									.getNamedItem("id");
							String scoringIdAttributeValue = scoringIdAttribute.getNodeValue();
							
							/*if ("PROPOR".equals(scoringIdAttributeValue)
									|| "RATIO".equals(scoringIdAttributeValue)
									|| "COHORT".equals(scoringIdAttributeValue)) {
							 */
							if ("PROPOR".equals(scoringIdAttributeValue)
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
							setMeasureElementsMap(xml);
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
	 * Also finds SubTree Node and corresponding Node Tree and add to SubTreeLookUpNode map.
	 * Also finds CQL dEfinitions and add to CQLDEfinitionsNode map.
	 * @param xml            the new qdm elements map
	 * @param measureId the measure id
	 */
	private void setMeasureElementsMap(String xml) {
		System.out.println("Measure XML:");
		System.out.println(xml);
		PopulationWorkSpaceConstants.elementLookUpName = new TreeMap<String, String>();
		PopulationWorkSpaceConstants.elementLookUpNode = new TreeMap<String, Node>();
		PopulationWorkSpaceConstants.elementLookUpDataTypeName = new TreeMap<String, String>();
		List<Entry<String, String>> sortedClauses = new LinkedList<Map.Entry<String, String>>(
				PopulationWorkSpaceConstants.subTreeLookUpName.entrySet());
		PopulationWorkSpaceConstants.subTreeLookUpNode = new LinkedHashMap<String, Node>();
		
		Document document = XMLParser.parse(xml);
		NodeList nodeList = document.getElementsByTagName("elementLookUp");
		setupElementLookupQDMNodes(nodeList);
		setupSubTreeLookupNodes(sortedClauses, document);
		
		setupCQLArtifactsNodes(document);
		
		List<String> dataTypeList = new ArrayList<String>();
		dataTypeList.addAll(PopulationWorkSpaceConstants.getElementLookUpDataTypeName().values());
		attributeService.getDatatypeList(dataTypeList, new AsyncCallback<Map<String, List<String>>>() {
			
			@Override
			public void onFailure(Throwable caught) {
				//Window.alert("I failed");
				
			}
			@Override
			public void onSuccess(Map<String, List<String>> datatypeMap) {
				PopulationWorkSpaceConstants.setDatatypeMap(datatypeMap);
			}
		});
	}
	
	/**
	 * Setup CQL Definitions in PopulationWorkSpaceConstants.
	 * @param document
	 */
	private void setupCQLArtifactsNodes(Document document) {
		PopulationWorkSpaceConstants.cqlDefinitionLookupNode = new LinkedHashMap<String, Node>();
		PopulationWorkSpaceConstants.cqlFunctionLookupNode = new LinkedHashMap<String, Node>();
		
		NodeList cqlLookupNodeList = document.getElementsByTagName("cqlLookUp");
		if ( (null != cqlLookupNodeList) &&  (cqlLookupNodeList.getLength() > 0) ){
		
			NodeList cqlChildNodeList = cqlLookupNodeList.item(0).getChildNodes();
			if ( (null != cqlChildNodeList) &&  (cqlChildNodeList.getLength() > 0) ){
				
				for(int i=0;i<cqlChildNodeList.getLength();i++){
					
					if(cqlChildNodeList.item(i).getNodeName().equals("definitions")){
						Node cqlDefinitionsNode = cqlChildNodeList.item(i);
						NodeList cqlDefinitionsList = cqlDefinitionsNode.getChildNodes();						
						
						for(int j=0;j < cqlDefinitionsList.getLength();j++){
							Node cqlDefinitionNode = cqlDefinitionsList.item(j);
							NamedNodeMap namedNodeMap = cqlDefinitionNode.getAttributes();
							String definitionName = namedNodeMap.getNamedItem("name").getNodeValue().trim();
							String uuid = namedNodeMap.getNamedItem("id").getNodeValue().trim();
							PopulationWorkSpaceConstants.cqlDefinitionLookupNode.put(definitionName + "~" + uuid, cqlDefinitionNode);
						}
							
					}else if(cqlChildNodeList.item(i).getNodeName().equals("functions")){
						Node cqlFunctionsNode = cqlChildNodeList.item(i);
						NodeList cqlFunctionsList = cqlFunctionsNode.getChildNodes();						
						
						for(int j=0;j < cqlFunctionsList.getLength();j++){
							Node cqlFunctionNode = cqlFunctionsList.item(j);
							NamedNodeMap namedNodeMap = cqlFunctionNode.getAttributes();
							String functionName = namedNodeMap.getNamedItem("name").getNodeValue().trim();
							String uuid = namedNodeMap.getNamedItem("id").getNodeValue().trim();
							PopulationWorkSpaceConstants.cqlFunctionLookupNode.put(functionName + "~" + uuid, cqlFunctionNode);
						}
				
					} 
				}			
			}
		}
		System.out.println(PopulationWorkSpaceConstants.cqlDefinitionLookupNode);
	}

	public void setupSubTreeLookupNodes(
			List<Entry<String, String>> sortedClauses, Document document) {
		for (Entry<String, String> entry1 : sortedClauses) {
		NodeList subTreesNodeList = document.getElementsByTagName("subTreeLookUp");
		if ((null != subTreesNodeList) && (subTreesNodeList.getLength() > 0)) {
			NodeList subTree = subTreesNodeList.item(0).getChildNodes();
			for (int i = 0; i < subTree.getLength(); i++) {
				if ("subTree".equals(subTree.item(i).getNodeName())) {
					NamedNodeMap namedNodeMap = subTree.item(i).getAttributes();
					String uuid = namedNodeMap.getNamedItem("uuid").getNodeValue();
					if(uuid.equalsIgnoreCase(entry1.getKey())){
						PopulationWorkSpaceConstants.subTreeLookUpNode.put(entry1.getValue() + "~" + entry1.getKey(), subTree.item(i));
						break;
					}					
				}
			}
		}
			
		}
	}

	public void setupElementLookupQDMNodes(NodeList nodeList) {
		if ((null != nodeList) && (nodeList.getLength() > 0)) {
			NodeList qdms = nodeList.item(0).getChildNodes();
			for (int i = 0; i < qdms.getLength(); i++) {
				if ("qdm".equals(qdms.item(i).getNodeName())) {
					NamedNodeMap namedNodeMap = qdms.item(i).getAttributes();
					String isSupplementData = namedNodeMap.getNamedItem("suppDataElement").getNodeValue();
					if (isSupplementData.equals("false")) { //filter supplementDataElements from elementLookUp
						String name = namedNodeMap.getNamedItem("name").getNodeValue();
						// Prod Issue fixed : qdm name has trailing spaces which is reterived frm VSAC.
						//So QDM attribute dialog box is throwing error in FF.To fix that spaces are removed from start and end.
						name = name.trim();
						//name = name.replaceAll("^\\s+|\\s+$", "");
						String uuid = namedNodeMap.getNamedItem("uuid").getNodeValue();
						if (namedNodeMap.getNamedItem("instance") != null) {
							name = namedNodeMap.getNamedItem("instance").getNodeValue() + " of " + name;
						}
						
						if (namedNodeMap.getNamedItem("datatype") != null) {
							String dataType = namedNodeMap.getNamedItem("datatype").getNodeValue().trim();
							name = name + " : " + namedNodeMap.getNamedItem("datatype").getNodeValue();
							PopulationWorkSpaceConstants.elementLookUpDataTypeName.put(uuid, dataType);
						}
						PopulationWorkSpaceConstants.elementLookUpNode.put(name + "~" + uuid, qdms.item(i));
						PopulationWorkSpaceConstants.elementLookUpName.put(uuid, name);
					}
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
