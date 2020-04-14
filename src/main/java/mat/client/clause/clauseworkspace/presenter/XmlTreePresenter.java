package mat.client.clause.clauseworkspace.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.model.CellTreeNodeImpl;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.clause.clauseworkspace.model.SortedClauseMapResult;
import mat.client.clause.clauseworkspace.view.ClauseWorkspaceContextMenu;
import mat.client.clause.clauseworkspace.view.PopulationWorkSpaceContextMenu;
import mat.client.clause.clauseworkspace.view.XmlTreeView;
import mat.client.event.ClauseSpecificOccurenceEvent;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.MatContext;
import mat.client.shared.SecondaryButton;
import mat.shared.ConstantMessages;


public class XmlTreePresenter {

    private static final String COMMENT = "COMMENT";

    private static final int NODESIZE = 500;

    boolean isSpecificOcc = false;

    String currentSelectedClause = null;

    private PopupPanel popupPanel = new PopupPanel(true, false);

    private Map<Integer, MatPresenter> selectedTreeMap;

    interface TreeResources extends CellTree.Resources {

        @Override
        @Source("mat/client/images/plus.png")
        ImageResource cellTreeClosedItem();

        @Override
        @Source("mat/client/images/minus.png")
        ImageResource cellTreeOpenItem();

        @Override
        @Source("mat/client/images/CwCellTree.css")
        CellTree.Style cellTreeStyle();

    }

    private XmlTreeDisplay xmlTreeDisplay;

    private MeasureServiceAsync service = MatContext.get().getMeasureService();

    private static final String MEASURE = "measure";

    private String rootNode;

    private SimplePanel panel;

    /**
     * This member variable is used to pass the original measure XML to
     * XmlTreePresenter class which will then be used to construct the CellTree.
     * Tree construction is done using loadXmlTree(..) method. Once the
     * loadXmlTree(..) method is done executing, originalXML should not be used.
     * Please refrain from using it anywhere other that the loadXmlTree(...)
     * method.
     */
    private String originalXML = "";

    public final void loadXmlTree(SimplePanel populationWorkSpacePanel, String panelName) {

        if (originalXML.length() > 0) {
            panel = populationWorkSpacePanel;
            panel.getElement().setAttribute("id", panelName);
            panel.clear();
            String xml = originalXML;
            XmlTreeView xmlTreeView = new XmlTreeView(
                    XmlConversionlHelper.createCellTreeNode(xml, rootNode)); // converts
            // XML to TreeModel Object and sets to XmlTreeView CellTree cellTree = new CellTree(xmlTreeView, null);

            PopulationWorkSpaceContextMenu populationWorkspaceContextMenu = new PopulationWorkSpaceContextMenu(xmlTreeView, popupPanel);
            xmlTreeView.setClauseWorkspaceContextMenu(populationWorkspaceContextMenu);
            xmlTreeView.setpopulationWorkspaceContextMenu(populationWorkspaceContextMenu);
            CellTree.Resources resource = GWT.create(TreeResources.class);
            CellTree cellTree = new CellTree(xmlTreeView, null, resource); // CellTree
            // Creation
            cellTree.setDefaultNodeSize(NODESIZE); // this will get rid of the show
            // more link on the bottom of the
            // Tree
            xmlTreeView.createPageView(cellTree); // Page Layout
            cellTree.setTabIndex(0);
            // This is Open Population Node by default in Population Tab.
            TreeNode treeNode = cellTree.getRootTreeNode();
            for (int i = 0; i < treeNode.getChildCount(); i++) {
                if (((CellTreeNode) treeNode.getChildValue(i)).getNodeType() == CellTreeNode.MASTER_ROOT_NODE) {
                    treeNode.setChildOpen(i, true, true);
                }
            }
            xmlTreeDisplay = xmlTreeView;
            xmlTreeDisplay.setEnabled(MatContext.get().getMeasureLockService()
                    .checkForEditPermission());
            panel.clear();
            panel.add(xmlTreeDisplay.asWidget());
            invokeSaveHandler();
        } else {
            Mat.hideLoadingMessage();
        }
        MeasureComposerPresenter.setSubSkipEmbeddedLink(panelName);
        Mat.focusSkipLists("MeasureComposer");

    }

    public final void loadClauseWorkSpaceView(SimplePanel clauseWorkSpacePanel) {
        panel = clauseWorkSpacePanel;
        panel.getElement().setAttribute("id", "ClauseWorkSpacePanel");
        CellTreeNode subTree = XmlConversionlHelper.createRootClauseNode();
        XmlTreeView xmlTreeView = new XmlTreeView(subTree);
        xmlTreeView.setClauseWorkspaceContextMenu(new ClauseWorkspaceContextMenu(xmlTreeView, popupPanel));
        /*xmlTreeView.createRootNode(subTree);*/
        CellTree.Resources resource = GWT.create(TreeResources.class);
        CellTree cellTree = new CellTree(xmlTreeView, null, resource); // CellTree
        // Creation
        cellTree.setDefaultNodeSize(NODESIZE);  // this will get rid of the show
        // more link on the bottom of the
        // Tree
        xmlTreeView.createClauseWorkSpacePageView(cellTree); // Page Layout
        cellTree.setTabIndex(0);
        // This will open the tree by default.
        TreeNode treeNode = cellTree.getRootTreeNode();
        for (int i = 0; i < treeNode.getChildCount(); i++) {
            if (((CellTreeNode) treeNode.getChildValue(i)).getNodeType()
                    == CellTreeNode.SUBTREE_ROOT_NODE) {
                treeNode.setChildOpen(i, true, true);
            }
        }
        setRootNode(cellTree.getRootTreeNode().toString());
        xmlTreeDisplay = xmlTreeView;
        xmlTreeDisplay.setEnabled(false);
        panel.clear();
        panel.add(xmlTreeDisplay.asWidget());
        invokeSaveHandler();
        invokeValidateHandler();
        invokeClearHandler();
        addClauseHandler();
        xmlTreeDisplay.getSaveBtnClauseWorkSpace().setEnabled(false);
        xmlTreeDisplay.getValidateBtnClauseWorkSpace().setEnabled(false);
        xmlTreeDisplay.getDeleteClauseButton().setEnabled(false);
        xmlTreeDisplay.getIncludeQdmVaribale().setEnabled(false);
    }

    private void addClauseHandler() {

        xmlTreeDisplay.getClauseNamesListBox().addChangeHandler(
                new ChangeHandler() {

                    @Override
                    public void onChange(ChangeEvent event) {
                        xmlTreeDisplay.getClauseNamesListBox();
                        System.out
                                .println("listbox change event caught in XmlTreePresenter:"
                                        + event.getAssociatedType().getName());
                        int selectedIndex = xmlTreeDisplay
                                .getClauseNamesListBox().getSelectedIndex();
                        String selectedItemUUID = xmlTreeDisplay
                                .getClauseNamesListBox()
                                .getValue(selectedIndex);
                        String selectedItemName = xmlTreeDisplay
                                .getClauseNamesListBox().getItemText(
                                        selectedIndex);
                        String measureId = MatContext.get()
                                .getCurrentMeasureId();
                        CellTreeNode cellTreeNode = (CellTreeNode) (xmlTreeDisplay
                                .getXmlTree().getRootTreeNode()
                                .getChildValue(0));
                        boolean checkIfUsedInLogic = true;
                        // Disable Show Clause Button for Occurrence CLauses.
                        xmlTreeDisplay.getShowClauseButton().setEnabled(true);


                        Node node = PopulationWorkSpaceConstants
                                .getSubTreeLookUpNode().get(
                                        selectedItemName + "~"
                                                + selectedItemUUID);
                        NamedNodeMap namedNodeMap = node.getAttributes();
                        if (namedNodeMap.getNamedItem("instance") != null) {
                            xmlTreeDisplay.getShowClauseButton()
                                    .setEnabled(false);
                        }

                        if (!MatContext.get().getMeasureLockService()
                                .checkForEditPermission()) {
                            // If the measure is Read Only, the disable the
                            // Delete Clause button.
                            xmlTreeDisplay.getDeleteClauseButton().setEnabled(
                                    false);
                            checkIfUsedInLogic = false;
                        } else if (cellTreeNode.getChilds().size() > 0) {
                            CellTreeNode childNode = cellTreeNode.getChilds()
                                    .get(0);
                            String nodeName = childNode.getName();
                            if (nodeName.equals(selectedItemName)) {
                                System.out
                                        .println("The clause is already being displayed on the LHS tree. Disable Delete Clause button now.");
                                xmlTreeDisplay.getDeleteClauseButton()
                                        .setEnabled(false);
                                checkIfUsedInLogic = false;
                            }
                        }

                        if (checkIfUsedInLogic) {
                            service.isSubTreeReferredInLogic(measureId,
                                    selectedItemUUID,
                                    new AsyncCallback<Boolean>() {

                                        @Override
                                        public void onFailure(Throwable caught) {

                                        }

                                        @Override
                                        public void onSuccess(Boolean result) {
                                            //Disable it always as per 5.0 as clause workspace is read only
                                            xmlTreeDisplay
                                                    .getDeleteClauseButton()
                                                    .setEnabled(false);
                                        }
                                    });

                        }
                        xmlTreeDisplay.getDeleteClauseButton().setEnabled(false);
                        xmlTreeDisplay.getIncludeQdmVaribale().setEnabled(false);
                    }

                });


        xmlTreeDisplay.getShowClauseButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ListBox clauseNamesListBox = xmlTreeDisplay.getClauseNamesListBox();
                if (clauseNamesListBox != null) {
                    int selectedIndex = clauseNamesListBox.getSelectedIndex();
                    if (selectedIndex != -1) {
                        final String selectedClauseName = clauseNamesListBox.getItemText(selectedIndex);
                        final String selectedClauseUUID = clauseNamesListBox.getValue(selectedIndex);
                        currentSelectedClause = selectedClauseUUID;
                        //Disable the Delete Clause button so that user cannot delete the currently editing clauses.
                        xmlTreeDisplay.getDeleteClauseButton().setEnabled(false);

                        System.out.println("Selected clause name and uuid is :"
                                + selectedClauseName + ":" + selectedClauseUUID);

                        final CellTreeNode cellTreeNode = (CellTreeNode) (xmlTreeDisplay
                                .getXmlTree().getRootTreeNode().getChildValue(0));

                        if (cellTreeNode.getChilds().size() > 0) {
                            if (xmlTreeDisplay.isDirty() || xmlTreeDisplay.isQdmVariableDirty()) {
                                //isUnsavedData = true;
                                xmlTreeDisplay.getErrorMessageDisplay().clear();
                                showErrorMessage(xmlTreeDisplay.getErrorMessageDisplay());
                                xmlTreeDisplay.getErrorMessageDisplay().getButtons().get(0).setFocus(true);

                                ClickHandler clickHandler = new ClickHandler() {
                                    @Override
                                    public void onClick(ClickEvent event) {
                                        //isUnsavedData = false;
                                        SecondaryButton button = (SecondaryButton) event.getSource();
                                        // If Yes - do not navigate, set focus to the Save button on the Page and clear cell tree
                                        // // Else -do not navigate, set focus to the Save button on the Page
                                        if ("Yes".equals(button.getText())) {
                                            xmlTreeDisplay.getErrorMessageDisplay().clear();
                                            xmlTreeDisplay.setDirty(false);
                                            xmlTreeDisplay.setQdmVariableDirty(false);
                                            changeClause(cellTreeNode, selectedClauseName, selectedClauseUUID);

                                        } else if ("No".equals(button.getText())) {
                                            xmlTreeDisplay.getErrorMessageDisplay().clear();
                                        }
                                    }
                                };

                                for (SecondaryButton secondaryButton : xmlTreeDisplay.getErrorMessageDisplay().getButtons()) {
                                    secondaryButton.addClickHandler(clickHandler);
                                }
                                //								if (isUnsavedData) {
                                //									MatContext.get().setErrorTab(true);
                                //								}
                            } else {
                                changeClause(cellTreeNode, selectedClauseName, selectedClauseUUID);
                            }

                        } else {
                            changeClause(cellTreeNode, selectedClauseName, selectedClauseUUID);
                        }
                    }
                }
                xmlTreeDisplay.getDeleteClauseButton().setEnabled(false);
                xmlTreeDisplay.getIncludeQdmVaribale().setEnabled(false);
            }
        });
    }

    /**
     * XMLTreePresenter constructor.
     **/
    public XmlTreePresenter() {
        MatContext.get().getEventBus().addHandler(ClauseSpecificOccurenceEvent.TYPE, new ClauseSpecificOccurenceEvent.Handler() {
            @Override
            public void onSave(ClauseSpecificOccurenceEvent event) {
                CellTreeNode cellTreeNode = XmlConversionlHelper.createOccurenceClauseNode(event.getSelectedNode());
                String xml = XmlConversionlHelper.createXmlFromTree(cellTreeNode);
                final String nodeUUID = event.getSelectedNode().getUUID();
                final String nodeName = event.getSelectedNode().getName();
                final MeasureXmlModel measureXmlModel = createMeasureXmlModel(xml);
                isSpecificOcc = event.isOccurrenceCreated();
                event.setOccurrenceCreated(false);
                if (isSpecificOcc) {
                    service.saveSubTreeOccurrence(measureXmlModel,
                            nodeName, nodeUUID, new AsyncCallback<SortedClauseMapResult>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
                                }

                                @Override
                                public void onSuccess(SortedClauseMapResult result) {
                                    setOriginalXML(result.getMeasureXmlModel().getXml());
                                    updateSubTreeElementsMap(getOriginalXML(), result.getClauseMap());
                                    if (xmlTreeDisplay != null) {
                                        xmlTreeDisplay.clearAndAddClauseNamesToListBox();
                                        xmlTreeDisplay.updateSuggestOracle();
                                        enableDisableQDMVariableCheckBox(nodeUUID);
                                    }
                                }
                            });
                }
            }
        });
    }

    /**
     * Change clause.
     *
     * @param cellTreeNode       the cell tree node
     * @param selectedClauseName the selected clause name
     * @param selectedClauseUUID the selected clause uuid
     */
    private void changeClause(CellTreeNode cellTreeNode, String selectedClauseName, String selectedClauseUUID) {

        if (cellTreeNode.getChilds().size() > 0) {
            CellTreeNode childNode = cellTreeNode.getChilds().get(0);
            System.out.println("clearing out:" + childNode.getName());
            cellTreeNode.removeChild(childNode);
        }

        Node node = PopulationWorkSpaceConstants.subTreeLookUpNode.get(selectedClauseName + "~" + selectedClauseUUID);
        CellTreeNode subTreeCellTreeNode = XmlConversionlHelper.createCellTreeNode(node, selectedClauseName);
        //for getting Qdm Variable attribute value for the Subtree Node
        Node qdmAttrNode = node.getAttributes().getNamedItem("qdmVariable");
        if ((qdmAttrNode != null) && qdmAttrNode.getNodeValue().equals("true")) {
            xmlTreeDisplay.getIncludeQdmVaribale().setValue(true);
        } else {
            xmlTreeDisplay.getIncludeQdmVaribale().setValue(false);
        }
        xmlTreeDisplay.setQdmVariable(xmlTreeDisplay.getIncludeQdmVaribale().getValue().toString());
        cellTreeNode.appendChild(subTreeCellTreeNode.getChilds().get(0));

        xmlTreeDisplay.getXmlTree().getRootTreeNode().setChildOpen(0, false);
        xmlTreeDisplay.getXmlTree().getRootTreeNode().setChildOpen(0, true);
        //the statement below will cause a programattic equivalent of clicking the Expand tree button.
        xmlTreeDisplay.getButtonExpandClauseWorkSpace().click();
        xmlTreeDisplay.getIncludeQdmVaribale().setVisible(true);
        enableDisableQDMVariableCheckBox(selectedClauseUUID);

    }

    private MeasureXmlModel createMeasureExportModel(final String xml) {
        MeasureXmlModel exportModal = new MeasureXmlModel();
        exportModal.setMeasureId(MatContext.get().getCurrentMeasureId());
        exportModal.setMeasureModel(MatContext.get().getCurrentMeasureModel());
        exportModal.setToReplaceNode(rootNode);
        exportModal.setParentNode(MEASURE);
        exportModal.setXml(xml);
        return exportModal;
    }

    private MeasureXmlModel createMeasureXmlModel(final String xml) {
        MeasureXmlModel exportModel = new MeasureXmlModel();
        exportModel.setMeasureId(MatContext.get().getCurrentMeasureId());
        exportModel.setMeasureModel(MatContext.get().getCurrentMeasureModel());
        exportModel.setToReplaceNode("subTree");
        exportModel.setParentNode("/measure/subTreeLookUp");
        exportModel.setXml(xml);
        return exportModel;
    }

    /**
     * Invoke Population Work Space save handler.
     */
    private void invokeSaveHandler() {
        xmlTreeDisplay.getSaveButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                if (MatContext.get().getMeasureLockService()
                        .checkForEditPermission()) {
                    xmlTreeDisplay.clearMessages();
                    xmlTreeDisplay.setDirty(false);
                    MatContext.get().recordTransactionEvent(
                            MatContext.get().getCurrentMeasureId(), null,
                            rootNode.toUpperCase() + "_TAB_SAVE_EVENT",
                            rootNode.toUpperCase().concat(" Saved."),
                            ConstantMessages.DB_LOG);
                    xmlTreeDisplay.addCommentNodeToSelectedNode();
                    CellTreeNode cellTreeNode = (CellTreeNode) xmlTreeDisplay
                            .getXmlTree().getRootTreeNode().getChildValue(0);
                    final MeasureXmlModel measureXmlModel = createMeasureExportModel(XmlConversionlHelper
                            .createXmlFromTree(cellTreeNode));
                    service.saveMeasureXml(measureXmlModel,
                            MatContext.get().getCurrentMeasureId(),
                            "FHIR".equals(MatContext.get().getCurrentMeasureModel()),
                            new AsyncCallback<Void>() {
                                @Override
                                public void onFailure(final Throwable caught) {
                                }

                                @Override
                                public void onSuccess(final Void result) {
                                    xmlTreeDisplay.getSuccessMessageAddCommentDisplay()
                                            .removeStyleName("successMessageCommentPanel");
                                    xmlTreeDisplay.getSuccessMessageAddCommentDisplay().clear();
                                    xmlTreeDisplay.getWarningMessageDisplay().clear();
                                    xmlTreeDisplay
                                            .getSuccessMessageDisplay()
                                            .setMessage(
                                                    "Changes are successfully saved.");
                                    setOriginalXML(measureXmlModel.getXml());
                                    System.out.println("originalXML is:"
                                            + getOriginalXML());
                                }
                            });
                }
            }
        });
        xmlTreeDisplay.getSaveBtnClauseWorkSpace().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                if (MatContext.get().getMeasureLockService()
                        .checkForEditPermission()) {
                    if (xmlTreeDisplay.getXmlTree() != null) {
                        xmlTreeDisplay.clearMessages();
                        final CellTreeNode cellTreeNode = (CellTreeNode) (xmlTreeDisplay
                                .getXmlTree().getRootTreeNode().getChildValue(0));
                        if (cellTreeNode.hasChildren()) {
                            xmlTreeDisplay.setDirty(false);
                            xmlTreeDisplay.setQdmVariableDirty(false);
                            MatContext.get().recordTransactionEvent(
                                    MatContext.get().getCurrentMeasureId(), null,
                                    "CLAUSEWORKSPACE_TAB_SAVE_EVENT",
                                    rootNode.toUpperCase().concat(" Saved."),
                                    ConstantMessages.DB_LOG);
                            cellTreeNode.getChilds().get(0).getUUID();
                            cellTreeNode.getChilds().get(0).getName();
                            //for adding qdmVariable as an attribute
                            String isQdmVariable = xmlTreeDisplay.getIncludeQdmVaribale().getValue().toString();
                            CellTreeNode subTreeNode = cellTreeNode.getChilds().get(0);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("qdmVariable", isQdmVariable);
                            subTreeNode.setExtraInformation(PopulationWorkSpaceConstants.EXTRA_ATTRIBUTES, map);
                            String xml = XmlConversionlHelper.createXmlFromTree(cellTreeNode.getChilds().get(0));

                            createMeasureXmlModel(xml);
                        } else {
                            xmlTreeDisplay.getErrorMessageDisplay().setMessage(
                                    "Unable to save clause as no subTree found under it.");
                        }
                    }
                }
            }
        });
        xmlTreeDisplay.getDeleteClauseButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (MatContext.get().getMeasureLockService()
                        .checkForEditPermission()) {
                    MatContext.get().getCurrentMeasureId();
                    final int selectedClauseindex = xmlTreeDisplay.getClauseNamesListBox().getSelectedIndex();
                    if (selectedClauseindex < 0) {
                        return;
                    }
                    final String clauseName = xmlTreeDisplay.getClauseNamesListBox().getItemText(selectedClauseindex);

                    final CellTreeNode cellTreeNode = (CellTreeNode) (xmlTreeDisplay
                            .getXmlTree().getRootTreeNode().getChildValue(0));
                    if (cellTreeNode.getChilds().size() > 0) {
                        CellTreeNode childNode = cellTreeNode.getChilds().get(0);
                        System.out.println("current clause is:" + childNode.getName());
                        if (childNode.getName().equals(clauseName)) {
                            return;
                        }
                    }
                }
            }
        });
        xmlTreeDisplay.getCommentButtons().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                xmlTreeDisplay.getSuccessMessageDisplay().clear();
                @SuppressWarnings("unchecked")
                List<CellTreeNode> commentList = (List<CellTreeNode>) xmlTreeDisplay
                        .getSelectedNode().getExtraInformation(COMMENT);
                if (commentList == null) {
                    commentList = new ArrayList<CellTreeNode>();
                }
                commentList.clear();
                CellTreeNode node = new CellTreeNodeImpl();
                node.setName(PopulationWorkSpaceConstants.COMMENT_NODE_NAME);
                node.setNodeType(CellTreeNode.COMMENT_NODE);
                node.setNodeText(xmlTreeDisplay.getCommentArea().getText());
                commentList.add(node);
                xmlTreeDisplay.getSelectedNode().setExtraInformation(COMMENT, commentList);
                xmlTreeDisplay.refreshCellTreeAfterAddingComment(xmlTreeDisplay.getSelectedNode());

                xmlTreeDisplay.getSuccessMessageDisplay().setMessage(
                        MatContext.get().getMessageDelegate().getCOMMENT_ADDED_SUCCESSFULLY());
            }
        });

        xmlTreeDisplay.getIncludeQdmVaribale().addValueChangeHandler(new ValueChangeHandler<Boolean>() {

            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {

                final CellTreeNode cellTreeNode = (CellTreeNode) (xmlTreeDisplay
                        .getXmlTree().getRootTreeNode().getChildValue(0));
                CellTreeNode subTreeNode = cellTreeNode.getChilds().get(0);
                if (!xmlTreeDisplay.isQdmVariable().equals(event.getValue().toString())) {
                    xmlTreeDisplay.setQdmVariableDirty(true);
                } else {
                    xmlTreeDisplay.setQdmVariableDirty(false);
                }

                // Update qdmVariable map in extraAttribute map of subtreeNode.
                if (subTreeNode.getExtraInformation("extraAttributes") != null) {
                    @SuppressWarnings("unchecked")
                    HashMap<String, String> extraInfoMap = (HashMap<String, String>)
                            subTreeNode.getExtraInformation("extraAttributes");
                    if (extraInfoMap.containsKey("qdmVariable")) {
                        extraInfoMap.put("qdmVariable", event.getValue().toString());
                    } else {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("qdmVariable", event.getValue().toString());
                        subTreeNode.setExtraInformation(PopulationWorkSpaceConstants.EXTRA_ATTRIBUTES, map);
                    }
                }
            }
        });
    }

    /**
     * Method to Reterive SubTree Node and corresponding Node Tree and add to SubTreeLookUpNode map.
     * Also it retrieves Name and UUID and put it in subTreeNodeName map for display.
     *
     * @param xml             - String.
     * @param sortedClauseMap the sorted clause map
     */
    protected void updateSubTreeElementsMap(String xml, LinkedHashMap<String, String> sortedClauseMap) {

        if (PopulationWorkSpaceConstants.subTreeLookUpName == null) {
            PopulationWorkSpaceConstants.subTreeLookUpName = new LinkedHashMap<String, String>();
        }
        if (PopulationWorkSpaceConstants.subTreeLookUpNode == null) {
            PopulationWorkSpaceConstants.subTreeLookUpNode = new LinkedHashMap<String, Node>();
        }
        //after saving Clause workspace, creating new subTreeLookUpNode to fix the clause duplicate issue
        PopulationWorkSpaceConstants.subTreeLookUpNode = new LinkedHashMap<String, Node>();

        PopulationWorkSpaceConstants.subTreeLookUpName = sortedClauseMap;
        Document document = XMLParser.parse(xml);

        List<Entry<String, String>> sortedClauses = new LinkedList<Map.Entry<String, String>>(
                PopulationWorkSpaceConstants.subTreeLookUpName.entrySet());
        for (Entry<String, String> entry1 : sortedClauses) {
            NodeList nodeList = document.getElementsByTagName("subTree");
            if ((nodeList != null) && (nodeList.getLength() > 0)) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    NamedNodeMap namedNodeMap = nodeList.item(i)
                            .getAttributes();
                    String uuid = namedNodeMap.getNamedItem("uuid")
                            .getNodeValue();
                    if (uuid.equalsIgnoreCase(entry1.getKey())) {
                        PopulationWorkSpaceConstants.subTreeLookUpNode.put(
                                entry1.getValue() + "~" + entry1.getKey(),
                                nodeList.item(i));
                        break;
                    }

                }

            }

        }
    }

    final void invokeValidateHandler() {
        xmlTreeDisplay.getValidateBtnClauseWorkSpace().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                if (xmlTreeDisplay.getXmlTree() != null) {
                    xmlTreeDisplay.clearMessages();
                    xmlTreeDisplay.validateCellTreeNodes(xmlTreeDisplay.getXmlTree()
                            .getRootTreeNode(), true);
                }
            }
        });
    }

    /**
     * Clear button Handler.
     */
    private void invokeClearHandler() {
        xmlTreeDisplay.getClearClauseWorkSpace().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                xmlTreeDisplay.clearMessages();
                if (xmlTreeDisplay.isDirty() || xmlTreeDisplay.isQdmVariableDirty()) {
                    //	isUnsavedData = true;
                    showErrorMessage(xmlTreeDisplay.getErrorMessageDisplay());
                    xmlTreeDisplay.getErrorMessageDisplay().getButtons().get(0).setFocus(true);
                    String auditMessage = getRootNode().toUpperCase() + "_TAB_YES_CLICKED";
                    handleClickEventsOnUnsavedErrorMsg(xmlTreeDisplay.getErrorMessageDisplay().getButtons()
                            , xmlTreeDisplay.getErrorMessageDisplay(), auditMessage);
                } else {
                    //	isUnsavedData = false;
                    xmlTreeDisplay.setDirty(false);
                    xmlTreeDisplay.setQdmVariableDirty(false);
                    panel.clear();
                    loadClauseWorkSpaceView(panel);
                    xmlTreeDisplay.getIncludeQdmVaribale().setVisible(false);
                }
            }
        });
    }

    /**
     * Show error message.
     *
     * @param errorMessageDisplay -ErrorMessageDisplay.
     */
    private void showErrorMessage(ErrorMessageDisplay errorMessageDisplay) {
        String msg = MatContext.get().getMessageDelegate().getSaveErrorMsg();
        List<String> btn = new ArrayList<String>();
        btn.add("Yes");
        btn.add("No");
        errorMessageDisplay.setMessageWithButtons(msg, btn);
    }

    /**
     * Handle click events on unsaved error msg.
     *
     * @param btns             -List.
     * @param saveErrorMessage - ErrorMessageDisplay.
     * @param auditMessage     -String.
     */
    private void handleClickEventsOnUnsavedErrorMsg(List<SecondaryButton> btns, final ErrorMessageDisplay saveErrorMessage
            , final String auditMessage) {
        //	isUnsavedData = true;
        ClickHandler clickHandler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                //isUnsavedData = false;
                SecondaryButton button = (SecondaryButton) event.getSource();
                // If Yes - do not navigate, set focus to the Save button on the Page and clear cell tree
                // // Else -do not navigate, set focus to the Save button on the Page
                if ("Yes".equals(button.getText())) {
                    saveErrorMessage.clear();
                    xmlTreeDisplay.setDirty(false);
                    xmlTreeDisplay.setQdmVariableDirty(false);
                    panel.clear();
                    loadClauseWorkSpaceView(panel);
                } else if ("No".equals(button.getText())) {
                    saveErrorMessage.clear();
                }
            }
        };
        for (SecondaryButton secondaryButton : btns) {
            secondaryButton.addClickHandler(clickHandler);
        }
    }

    /**
     * Gets the root node.
     *
     * @return the rootNode
     */
    public final String getRootNode() {
        return rootNode;
    }

    /**
     * Sets the root node.
     *
     * @param rootNode the rootNode to set
     */
    public final void setRootNode(final String rootNode) {
        this.rootNode = rootNode;
    }

    /**
     * Sets the this member variable is used to pass the original measure XML to
     * XmlTreePresenter class which will then be used to construct the CellTree.
     *
     * @param originalXML the new this member variable is used to pass the original
     *                    measure XML to XmlTreePresenter class which will then be used
     *                    to construct the CellTree
     */
    public final void setOriginalXML(final String originalXML) {
        this.originalXML = originalXML;
    }

    /**
     * Gets the this member variable is used to pass the original measure XML to
     * XmlTreePresenter class which will then be used to construct the CellTree.
     *
     * @return the this member variable is used to pass the original measure XML
     * to XmlTreePresenter class which will then be used to construct
     * the CellTree
     */
    public final String getOriginalXML() {
        return originalXML;
    }

    /**
     * Gets the xml tree display.
     *
     * @return the xml tree display
     */
    public final XmlTreeDisplay getXmlTreeDisplay() {
        return xmlTreeDisplay;
    }

    /**
     * Sets the xml tree display.
     *
     * @param xmlTreeDisplay the new xml tree display
     */
    public final void setXmlTreeDisplay(final XmlTreeDisplay xmlTreeDisplay) {
        this.xmlTreeDisplay = xmlTreeDisplay;
    }

    /**
     * Enable disable qdm variable check box.
     *
     * @param clauseUUID the clause uuid
     */
    private void enableDisableQDMVariableCheckBox(final String clauseUUID) {
        // If Editable measure then call the service to enable/Disable Check box else disable always.
        if (MatContext.get().getMeasureLockService()
                .checkForEditPermission()) {
            xmlTreeDisplay.getIncludeQdmVaribale().setEnabled(true);
            service.isQDMVariableEnabled(MatContext.get().getCurrentMeasureId(), clauseUUID, new AsyncCallback<Boolean>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(Boolean result) {
                    //Disable it always as per 5.0 as clause workspace is read only
                    xmlTreeDisplay.getIncludeQdmVaribale().setEnabled(false);
                }
            });
        } else {
            xmlTreeDisplay.getIncludeQdmVaribale().setEnabled(false);
        }
    }

    public Map<Integer, MatPresenter> getSelectedTreeMap() {
        return selectedTreeMap;
    }

    public void setSelectedTreeMap(Map<Integer, MatPresenter> selectedTreeMap) {
        this.selectedTreeMap = selectedTreeMap;
    }
}
