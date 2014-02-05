package mat.client.clause.clauseworkspace.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import mat.client.MatPresenter;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.codelist.service.CodeListServiceAsync;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.MatContext;
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
 * The Class ClauseWorkSpacePresenter.
 */
public class ClauseWorkSpacePresenter extends XmlTreePresenter implements MatPresenter {
	/** The simplepanel. */
	private SimplePanel simplepanel = new SimplePanel();
	
	/** The flow panel. */
	FlowPanel flowPanel = new FlowPanel();
	
	/** The service. */
	MeasureServiceAsync service = MatContext.get().getMeasureService();
	
	/**
	 * Instantiates a new clause work space presenter.
	 */
	public ClauseWorkSpacePresenter() {
		simplepanel.setStyleName("contentPanel");
		simplepanel.add(flowPanel);
		MatContext.get().getAllOperators();
		loadAllUnits();
		
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
	 * Load measure xml.
	 */
	private void loadMeasureXML() {
		final String currentMeasureId = MatContext.get().getCurrentMeasureId();
		if ((currentMeasureId != null) && !"".equals(currentMeasureId)) {
			service.getMeasureXmlForMeasure(MatContext.get()
					.getCurrentMeasureId(),
					new AsyncCallback<MeasureXmlModel>() { // Loading the measure's XML from the Measure_XML table
				@Override
				public void onSuccess(MeasureXmlModel result) {
					String xml = result != null ? result.getXml() : null;
					setQdmElementsMap(xml);
					setSubTreeElementsMap(xml);
				}
				@Override
				public void onFailure(Throwable caught) {
					System.out.println("Server call failed in ClauseWorkspacePresenter.loadMeasureXML()"
							+ " in service.getMeasureXmlForMeasure");
				}
			});
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
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		loadMeasureXML();
		loadClauseWorkSpaceView(simplepanel);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		return simplepanel;
	}
}
