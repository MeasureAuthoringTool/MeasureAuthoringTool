package mat.client.populationworkspace;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import mat.client.Mat;
import mat.client.clause.clauseworkspace.model.CQLCellTreeNode;
import mat.client.clause.clauseworkspace.model.CQLCellTreeNodeImpl;
import mat.client.clause.clauseworkspace.model.SortedClauseMapResult;
import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;
import mat.client.shared.MatContext;
import mat.client.shared.SpacerWidget;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class CQLViewPopulationsDisplay {
    String rootNode = "populations";

    private VerticalPanel mainPanel = new VerticalPanel();

    private String measureXml;

    private Document document;

    /**
     * The Interface TreeResources.
     */
    interface TreeResources extends CellTree.Resources {

        /* (non-Javadoc)
         * @see com.google.gwt.user.cellview.client.CellTree.Resources#cellTreeClosedItem()
         */
        @Override
        @Source("mat/client/images/plus.png")
        ImageResource cellTreeClosedItem();

        /* (non-Javadoc)
         * @see com.google.gwt.user.cellview.client.CellTree.Resources#cellTreeOpenItem()
         */
        @Override
        @Source("mat/client/images/minus.png")
        ImageResource cellTreeOpenItem();

        /* (non-Javadoc)
         * @see com.google.gwt.user.cellview.client.CellTree.Resources#cellTreeStyle()
         */
        @Override
        @Source("mat/client/images/CwCellTree.css")
        CellTree.Style cellTreeStyle();
    }

    private void buildView(String scoringType, boolean patientBased) {
        PopulationTreeBuilderStrategyFactory populationTreeBuilderStrategyFactory = new PopulationTreeBuilderStrategyFactory();
        PopulationTreeBuilderStrategy populationTreeBuilderStrategy = populationTreeBuilderStrategyFactory.getPopulationTreeBuilderStrategy(scoringType);
        CQLCellTreeNode parentNode = populationTreeBuilderStrategy.buildCQLTreeNode(scoringType, document, patientBased);

        CQLCellTreeNode topmainNode = new CQLCellTreeNodeImpl();
        List<CQLCellTreeNode> topchilds = new ArrayList<CQLCellTreeNode>();

        topchilds.add(parentNode);
        topmainNode.setChilds(topchilds);
        topmainNode.setOpen(true);
        CQLXmlTreeView xmlTreeView = new CQLXmlTreeView(topmainNode);
        CellTree.Resources resource = GWT.create(TreeResources.class);
        CellTree cellTree = new CellTree(xmlTreeView, null, resource); // CellTree

        xmlTreeView.buildView(cellTree); // Page Layout
        xmlTreeView.openMainNode();
        mainPanel.add(xmlTreeView.asWidget());
        mainPanel.add(new SpacerWidget());
        mainPanel.add(new SpacerWidget());
    }

    public VerticalPanel getMainPanel() {
        return mainPanel;
    }

    public void getMeasureXmlAndBuildView() {
        Mat.showLoadingMessage();
        mainPanel.clear();
        final String currentMeasureId = MatContext.get().getCurrentMeasureId();
        if ((currentMeasureId != null) && !"".equals(currentMeasureId)) {
            MatContext.get().getMeasureService().getMeasureXmlForMeasureAndSortedSubTreeMap(currentMeasureId,
                    new AsyncCallback<SortedClauseMapResult>() { // Loading the measure's SimpleXML from the Measure_XML table
                        @Override
                        public void onSuccess(SortedClauseMapResult result) {
                            measureXml = result != null ? result.getMeasureXmlModel().getXml() : null;
                            document = XMLParser.parse(measureXml);
                            PopulationWorkSpaceConstants.subTreeLookUpName = result.getClauseMap();
                            setMeasureElementsMap();
                            String scoringIdAttributeValue = MatContext.get().getCurrentMeasureInfo().getScoringType();
                            boolean patientBased = MatContext.get().getCurrentMeasureInfo().isPatientBased();

                            buildView(scoringIdAttributeValue, patientBased);
                            Mat.hideLoadingMessage();
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            Mat.hideLoadingMessage();
                            Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
                        }
                    });

        } else {
            Mat.hideLoadingMessage();
        }
    }

    private void setMeasureElementsMap() {

        PopulationWorkSpaceConstants.elementLookUpName = new TreeMap<String, String>();
        PopulationWorkSpaceConstants.elementLookUpNode = new TreeMap<String, Node>();
        PopulationWorkSpaceConstants.elementLookUpDataTypeName = new TreeMap<String, String>();
        List<Entry<String, String>> sortedClauses = new LinkedList<Map.Entry<String, String>>(
                PopulationWorkSpaceConstants.subTreeLookUpName.entrySet());
        PopulationWorkSpaceConstants.subTreeLookUpNode = new LinkedHashMap<String, Node>();

        setupSubTreeLookupNodes(sortedClauses);

        setupCQLArtifactsNodes();
    }

    /**
     * Setup CQL Definitions in PopulationWorkSpaceConstants.
     */
    private void setupCQLArtifactsNodes() {
        PopulationWorkSpaceConstants.cqlDefinitionLookupNode = new LinkedHashMap<String, Node>();
        PopulationWorkSpaceConstants.cqlFunctionLookupNode = new LinkedHashMap<String, Node>();
        PopulationWorkSpaceConstants.defNames = new ArrayList<String>();
        PopulationWorkSpaceConstants.funcNames = new ArrayList<String>();

        NodeList cqlLookupNodeList = document.getElementsByTagName("cqlLookUp");
        if ((null != cqlLookupNodeList) && (cqlLookupNodeList.getLength() > 0)) {

            NodeList cqlChildNodeList = cqlLookupNodeList.item(0).getChildNodes();
            if ((null != cqlChildNodeList) && (cqlChildNodeList.getLength() > 0)) {

                for (int i = 0; i < cqlChildNodeList.getLength(); i++) {

                    if (cqlChildNodeList.item(i).getNodeName().equals("definitions")) {
                        Node cqlDefinitionsNode = cqlChildNodeList.item(i);
                        NodeList cqlDefinitionsList = cqlDefinitionsNode.getChildNodes();

                        for (int j = 0; j < cqlDefinitionsList.getLength(); j++) {
                            Node cqlDefinitionNode = cqlDefinitionsList.item(j);
                            NamedNodeMap namedNodeMap = cqlDefinitionNode.getAttributes();
                            //MAT-8571 : Filter Non Patient Context type Definitions and Functions.
                            if (!namedNodeMap.getNamedItem("context").getNodeValue().equalsIgnoreCase(PopulationWorkSpaceConstants.CONTEXT_PATIENT)) {
                                continue;
                            }
                            String definitionName = namedNodeMap.getNamedItem("name").getNodeValue().trim();
                            String uuid = namedNodeMap.getNamedItem("id").getNodeValue().trim();
                            PopulationWorkSpaceConstants.defNames.add(definitionName);
                            PopulationWorkSpaceConstants.cqlDefinitionLookupNode.put(definitionName + "~" + uuid, cqlDefinitionNode);
                        }

                    } else if (cqlChildNodeList.item(i).getNodeName().equals("functions")) {
                        Node cqlFunctionsNode = cqlChildNodeList.item(i);
                        NodeList cqlFunctionsList = cqlFunctionsNode.getChildNodes();

                        for (int j = 0; j < cqlFunctionsList.getLength(); j++) {
                            Node cqlFunctionNode = cqlFunctionsList.item(j);
                            NamedNodeMap namedNodeMap = cqlFunctionNode.getAttributes();
                            //MAT-8571 :Filter Non Patient Context type Definitions and Functions.
							if(!namedNodeMap.getNamedItem("context").getNodeValue().equalsIgnoreCase(PopulationWorkSpaceConstants.CONTEXT_PATIENT)){
                                continue;
                            } else {
                                NodeList childNodeList = cqlFunctionNode.getChildNodes();
                                boolean invalidArgList = false;
                                // CHECK IF NO AGRUMENTS ARE ADDED.
                                if (childNodeList.getLength() == 2 && childNodeList.item(0).getNodeName().equalsIgnoreCase("logic")) {
                                    invalidArgList = true;
                                } else { // CHECK IF ARGUMENTS ARE ADDED THEN ONLY SHOW FUNCTIONS WITH ONE AND ONLY ONE ARGUMENT.
                                    for (int k = 0; k < childNodeList.getLength(); k++) {
                                        Node childNode = childNodeList.item(k);
                                        if (childNode.getNodeName().equalsIgnoreCase("arguments")) {
                                            NodeList argumentNodeList = childNode.getChildNodes();
                                            if (argumentNodeList != null && (argumentNodeList.getLength() != 1)) {
                                                invalidArgList = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (invalidArgList) {
                                    continue;
                                }
                            }
                            String functionName = namedNodeMap.getNamedItem("name").getNodeValue().trim();
                            String uuid = namedNodeMap.getNamedItem("id").getNodeValue().trim();
                            PopulationWorkSpaceConstants.funcNames.add(functionName);
                            PopulationWorkSpaceConstants.cqlFunctionLookupNode.put(functionName + "~" + uuid, cqlFunctionNode);
                        }

                    }
                }
            }
        }
        System.out.println(PopulationWorkSpaceConstants.cqlDefinitionLookupNode);
    }

    public void setupSubTreeLookupNodes(
            List<Entry<String, String>> sortedClauses) {
        for (Entry<String, String> entry1 : sortedClauses) {
            NodeList subTreesNodeList = document.getElementsByTagName("subTreeLookUp");
            if ((null != subTreesNodeList) && (subTreesNodeList.getLength() > 0)) {
                NodeList subTree = subTreesNodeList.item(0).getChildNodes();
                for (int i = 0; i < subTree.getLength(); i++) {
                    if ("subTree".equals(subTree.item(i).getNodeName())) {
                        NamedNodeMap namedNodeMap = subTree.item(i).getAttributes();
                        String uuid = namedNodeMap.getNamedItem("uuid").getNodeValue();
                        if (uuid.equalsIgnoreCase(entry1.getKey())) {
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

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public void setMeasureXml(String measureXml) {
        this.measureXml = measureXml;
    }

}
