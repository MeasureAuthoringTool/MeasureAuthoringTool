package mat.client.clause.clauseworkspace.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
import mat.client.clause.QDSAttributesService;
import mat.client.clause.QDSAttributesServiceAsync;
import mat.client.clause.clauseworkspace.model.SortedClauseMapResult;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.JSONAttributeModeUtility;
import mat.client.shared.MatContext;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * The Class ClauseWorkSpacePresenter.
 */
public class ClauseWorkSpacePresenter extends XmlTreePresenter implements MatPresenter {
	/** The simplepanel. */
	private SimplePanel simplepanel = new SimplePanel();
	
	/** The flow panel. */
	private FlowPanel flowPanel = new FlowPanel();
	
	/** The service. */
	private MeasureServiceAsync service = MatContext.get().getMeasureService();
	
	/** The attribute service. */
	private static QDSAttributesServiceAsync attributeService = (QDSAttributesServiceAsync) GWT
			.create(QDSAttributesService.class);
	/**
	 * Instantiates a new clause work space presenter.
	 */
	public ClauseWorkSpacePresenter() {
		simplepanel.setStyleName("contentPanel");
		simplepanel.add(flowPanel);
		MatContext.get().getAllOperators();
		JSONAttributeModeUtility.getAllAttrModeList();		
	}

	/**
	 * Load measure xml.
	 */
	private void loadMeasureXML() {
		
		final String currentMeasureId = MatContext.get().getCurrentMeasureId();
		if ((currentMeasureId != null) && !"".equals(currentMeasureId)) {
			service.getMeasureXmlForMeasureAndSortedSubTreeMap(MatContext.get()
					.getCurrentMeasureId(),
					new AsyncCallback<SortedClauseMapResult>() { // Loading the measure's XML from the Measure_XML table
				@Override
				public void onSuccess(SortedClauseMapResult result) {
					String xml = result != null ? result.getMeasureXmlModel().getXml() : null;
					PopulationWorkSpaceConstants.subTreeLookUpName = result.getClauseMap();
					setQdmElementsAndSubTreeLookUpMap(xml);
					loadClauseWorkSpaceView(simplepanel);
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
	 * Sets the qdm elements map also  reterives SubTree Node and corresponding Node Tree and add to SubTreeLookUpNode map.
	 *
	 * @param xml            the new qdm elements map
	 */
	private void setQdmElementsAndSubTreeLookUpMap(final String xml) {
		PopulationWorkSpaceConstants.elementLookUpDataTypeName = new TreeMap<String, String>();
		PopulationWorkSpaceConstants.elementLookUpName = new TreeMap<String, String>();
		PopulationWorkSpaceConstants.elementLookUpNode = new TreeMap<String, Node>();		
		List<Entry<String, String>> sortedClauses = new LinkedList<Map.Entry<String, String>>(
				PopulationWorkSpaceConstants.subTreeLookUpName.entrySet());
		PopulationWorkSpaceConstants.subTreeLookUpNode = new LinkedHashMap<String, Node>();
		Document document = XMLParser.parse(xml);

		NodeList nodeList = document.getElementsByTagName("elementLookUp");		
		if ((null != nodeList) && (nodeList.getLength() > 0)) {
			NodeList qdms = nodeList.item(0).getChildNodes();

			for (int i = 0; i < qdms.getLength(); i++) {
				if ("qdm".equals(qdms.item(i).getNodeName())) {
					NamedNodeMap namedNodeMap = qdms.item(i).getAttributes();
					String isSupplementData = namedNodeMap.getNamedItem(
							"suppDataElement").getNodeValue();
					if (isSupplementData.equals("false")) { // filter
															// supplementDataElements
															// from
															// elementLookUp
						String name = namedNodeMap.getNamedItem("name")
								.getNodeValue();

						// Prod Issue fixed : qdm name has trailing spaces which
						// is reterived frm VSAC.
						// So QDM attribute dialog box is throwing error in FF.
						// To fix that spaces are removed from start and end.
						name = name.trim();
						String uuid = namedNodeMap.getNamedItem("uuid")
								.getNodeValue();
						// name = name.replaceAll("^\\s+|\\s+$", "");
						if (namedNodeMap.getNamedItem("instance") != null) {
							name = namedNodeMap.getNamedItem("instance")
									.getNodeValue() + " of " + name;
						}
						if (namedNodeMap.getNamedItem("datatype") != null) {
							String dataType = namedNodeMap
									.getNamedItem("datatype").getNodeValue()
									.trim();
							name = name + " : " + dataType;
							PopulationWorkSpaceConstants.elementLookUpDataTypeName
									.put(uuid, dataType);
						}
						PopulationWorkSpaceConstants.elementLookUpNode.put(name
								+ "~" + uuid, qdms.item(i));
						PopulationWorkSpaceConstants.elementLookUpName.put(
								uuid, name);
					}
				}
			}
		}

		NodeList subTreesNodeList = document
				.getElementsByTagName("subTreeLookUp");
		if ((null != subTreesNodeList) && (subTreesNodeList.getLength() > 0)) {
			NodeList subTree = subTreesNodeList.item(0).getChildNodes();
			for (Entry<String, String> entry1 : sortedClauses) {
				for (int i = 0; i < subTree.getLength(); i++) {
					if ("subTree".equals(subTree.item(i).getNodeName())) {
						NamedNodeMap namedNodeMap = subTree.item(i)
								.getAttributes();

						String name = namedNodeMap.getNamedItem("displayName")
								.getNodeValue();						
						name = name.trim();
						// name = name.replaceAll("^\\s+|\\s+$", "");
						String uuid = namedNodeMap.getNamedItem("uuid")
								.getNodeValue();
						if (uuid.equalsIgnoreCase(entry1.getKey())) {
							PopulationWorkSpaceConstants.subTreeLookUpNode.put(
									entry1.getValue() + "~" + entry1.getKey(),
									subTree.item(i));
							break;
						}

					}
				}
			}

		}		
		List<String> dataTypeList = new ArrayList<String>();
		dataTypeList.addAll(PopulationWorkSpaceConstants
				.getElementLookUpDataTypeName().values());
		attributeService.getDatatypeList(dataTypeList,
				new AsyncCallback<Map<String, List<String>>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(Map<String, List<String>> datatypeMap) {
						System.out.println("Data type map:" + datatypeMap);
						PopulationWorkSpaceConstants
								.setDatatypeMap(datatypeMap);

					}
				});

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
		MeasureComposerPresenter.setSubSkipEmbeddedLink("ClauseWorkSpacePanel");
		Mat.focusSkipLists("MeasureComposer");
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		return simplepanel;
	}
}
