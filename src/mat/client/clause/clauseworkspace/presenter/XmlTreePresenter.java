package mat.client.clause.clauseworkspace.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import mat.client.Mat;
import mat.client.MeasureComposerPresenter;
import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.model.CellTreeNodeImpl;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.clause.clauseworkspace.view.ClauseWorkspaceContextMenu;
import mat.client.clause.clauseworkspace.view.PopulationWorkSpaceContextMenu;
import mat.client.clause.clauseworkspace.view.XmlTreeView;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.MatContext;
import mat.client.shared.SecondaryButton;
import mat.shared.ConstantMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

// TODO: Auto-generated Javadoc
/**
 * The Class XmlTreePresenter.
 */
public class XmlTreePresenter {
	private static final String COMMENT = "COMMENT";
	/**
	 * Cell Tree Node Size to remove show more.
	 */
	private static final int NODESIZE = 500;
	
	
	/** The is unsaved data. */
//	private boolean isUnsavedData = false;
	/**
	 * Pop up Panel for Right Context Menu.
	 */
	private PopupPanel popupPanel = new PopupPanel(true, false);
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
		
		/*
		 * @Source("mat/client/images/cms_gov_footer.png")
		 *
		 * @ImageOptions(repeatStyle = RepeatStyle.Horizontal, flipRtl = true)
		 * ImageResource cellTreeSelectedBackground();
		 */
	}
	
	/** The xml tree display. */
	private XmlTreeDisplay xmlTreeDisplay;
	
	/** The service. */
	private MeasureServiceAsync service = MatContext.get().getMeasureService();
	
	/** The Constant MEASURE. */
	private static final String MEASURE = "measure";
	
	/** The root node. */
	private String rootNode;
	
	/** The panel. */
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
	
	/**
	 * Load xml tree.
	 * 
	 * @param populationWorkSpacePanel
	 *            the SimplePanel
	 */
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
					// ((CellTreeNode)treeNode.getChildValue(i)).setOpen(true);
					treeNode.setChildOpen(i, true, true);
				}
			}
			xmlTreeDisplay = xmlTreeView;
			xmlTreeDisplay.setEnabled(MatContext.get().getMeasureLockService()
					.checkForEditPermission());
			panel.clear();
			panel.add(xmlTreeDisplay.asWidget());
			invokeSaveHandler();
			//invokeValidateHandler();
			invokeValidateHandlerPopulationWorkspace();//added to handle the validate button
		} else {
			Mat.hideLoadingMessage();
		}
		MeasureComposerPresenter.setSubSkipEmbeddedLink(panelName);
		Mat.focusSkipLists("MeasureComposer");
		
	}
	
	/**
	 * Method to create Clause Work space View.
	 * @param clauseWorkSpacePanel - SimplePanel.
	 */
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
		xmlTreeDisplay.setEnabled(MatContext.get().getMeasureLockService()
				.checkForEditPermission());
		panel.clear();
		panel.add(xmlTreeDisplay.asWidget());
		invokeSaveHandler();
		invokeValidateHandler();
		invokeClearHandler();
		addClauseHandler();
	}
	
	
	public final ScrollPanel loadClauseLogic(){
		XmlTreeDisplay clauseTreeDisplay;
		ScrollPanel simplePanel = new ScrollPanel();
		simplePanel.getElement().setAttribute("id", "ClauseLogic");
		CellTreeNode subTree = XmlConversionlHelper.createRootClauseNode();
		XmlTreeView xmlTreeView = new XmlTreeView(subTree);
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
		clauseTreeDisplay = xmlTreeView;
		clauseTreeDisplay.setEnabled(MatContext.get().getMeasureLockService()
				.checkForEditPermission());
		simplePanel.clear();
		simplePanel.add(clauseTreeDisplay.asWidget());
		return simplePanel;
	}
	/**
	 * 
	 */
	private void addClauseHandler() {
		
		xmlTreeDisplay.getClauseNamesListBox().addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				System.out.println("listbox change event caught in XmlTreePresenter:"+event.getAssociatedType().getName());
				int selectedIndex = xmlTreeDisplay.getClauseNamesListBox().getSelectedIndex();
				String selectedItemUUID = xmlTreeDisplay.getClauseNamesListBox().getValue(selectedIndex);
				String selectedItemName = xmlTreeDisplay.getClauseNamesListBox().getItemText(selectedIndex);
				String measureId = MatContext.get().getCurrentMeasureId();
				CellTreeNode cellTreeNode = (CellTreeNode) (xmlTreeDisplay
						.getXmlTree().getRootTreeNode().getChildValue(0));
				boolean checkIfUsedInLogic = true;
				
				if(!MatContext.get().getMeasureLockService().checkForEditPermission()){
					//If the measure is Read Only, the disable the Delete Clause button.
					xmlTreeDisplay.getDeleteClauseButton().setEnabled(false);
					checkIfUsedInLogic = false;
				}else if(cellTreeNode.getChilds().size() > 0){
					CellTreeNode childNode = cellTreeNode.getChilds().get(0);
					String nodeName = childNode.getName();
					if(nodeName.equals(selectedItemName)){
						System.out.println("The clause is already being displayed on the LHS tree. Disable Delete Clause button now.");
						xmlTreeDisplay.getDeleteClauseButton().setEnabled(false);
						checkIfUsedInLogic = false;
					}
				}
								
				if(checkIfUsedInLogic){
					service.isSubTreeReferredInLogic(measureId, selectedItemUUID, new AsyncCallback<Boolean>() {
	
						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							
						}
	
						@Override
						public void onSuccess(Boolean result) {						
							xmlTreeDisplay.getDeleteClauseButton().setEnabled(!result);		
						}
					});
				}
			}
			
		});
		
		
		xmlTreeDisplay.getShowClauseButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ListBox clauseNamesListBox = xmlTreeDisplay.getClauseNamesListBox();
				if(clauseNamesListBox != null){
					int selectedIndex  = clauseNamesListBox.getSelectedIndex();
					if(selectedIndex != -1){
						final String selectedClauseName = clauseNamesListBox.getItemText(selectedIndex);
						final String selectedClauseUUID = clauseNamesListBox.getValue(selectedIndex);
						
						//Disable the Delete Clause button so that user cannot delete the currently editing clauses.
						xmlTreeDisplay.getDeleteClauseButton().setEnabled(false);
						
						System.out.println("Selected clause name and uuid is :"
								+ selectedClauseName + ":" + selectedClauseUUID);
						
						final CellTreeNode cellTreeNode = (CellTreeNode) (xmlTreeDisplay
								.getXmlTree().getRootTreeNode().getChildValue(0));
						
						if(cellTreeNode.getChilds().size() > 0){
							if (xmlTreeDisplay.isDirty()) {
								//isUnsavedData = true;
								showErrorMessage(xmlTreeDisplay.getErrorMessageDisplay());
								xmlTreeDisplay.getErrorMessageDisplay().getButtons().get(0).setFocus(true);
								String auditMessage = getRootNode().toUpperCase() + "_TAB_YES_CLICKED";
								
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
							}else{
								changeClause(cellTreeNode, selectedClauseName, selectedClauseUUID);
							}
							
						}else{
							changeClause(cellTreeNode, selectedClauseName, selectedClauseUUID);
						}
					}
				}
			}
		});
	}
	
	private void changeClause(CellTreeNode cellTreeNode, String selectedClauseName, String selectedClauseUUID){
		
		if(cellTreeNode.getChilds().size() > 0){
			CellTreeNode childNode = cellTreeNode.getChilds().get(0);
			System.out.println("clearing out:"+childNode.getName());
			cellTreeNode.removeChild(childNode);
		}
		
		Node node = PopulationWorkSpaceConstants.subTreeLookUpNode.get(selectedClauseName + "~" + selectedClauseUUID);
		CellTreeNode subTreeCellTreeNode = XmlConversionlHelper.createCellTreeNode(node, selectedClauseName);
		
		cellTreeNode.appendChild(subTreeCellTreeNode.getChilds().get(0));
		
		xmlTreeDisplay.getXmlTree().getRootTreeNode().setChildOpen(0, false);
		xmlTreeDisplay.getXmlTree().getRootTreeNode().setChildOpen(0, true);
		//the statement below will cause a programattic equivalent of clicking the Expand tree button.
		xmlTreeDisplay.getButtonExpandClauseWorkSpace().click();
	}
	
	/**
	 * Creates the measure export model.
	 * 
	 * @param xml
	 *            the xml
	 * @return the measure xml model
	 */
	private MeasureXmlModel createMeasureExportModel(final String xml) {
		MeasureXmlModel exportModal = new MeasureXmlModel();
		exportModal.setMeasureId(MatContext.get().getCurrentMeasureId());
		exportModal.setToReplaceNode(rootNode);
		exportModal.setParentNode(MEASURE);
		exportModal.setXml(xml);
		return exportModal;
	}
	
	/**
	 * Measure XML modal for SubTree/SubTreeLookUp.
	 * @param xml - String.
	 * @return MeasureXmlModel.
	 */
	private MeasureXmlModel createMeasureXmlModel(final String xml) {
		MeasureXmlModel exportModel = new MeasureXmlModel();
		exportModel.setMeasureId(MatContext.get().getCurrentMeasureId());
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
		});
		xmlTreeDisplay.getSaveBtnClauseWorkSpace().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				if (xmlTreeDisplay.getXmlTree() != null) {
					xmlTreeDisplay.clearMessages();
					final CellTreeNode cellTreeNode = (CellTreeNode) (xmlTreeDisplay
							.getXmlTree().getRootTreeNode().getChildValue(0));
					if (cellTreeNode.hasChildren()) {
						xmlTreeDisplay.setDirty(false);
						MatContext.get().recordTransactionEvent(
								MatContext.get().getCurrentMeasureId(), null,
								"CLAUSEWORKSPACE_TAB_SAVE_EVENT",
								rootNode.toUpperCase().concat(" Saved."),
								ConstantMessages.DB_LOG);
						final String nodeUUID = cellTreeNode.getChilds().get(0).getUUID();
						final String nodeName = cellTreeNode.getChilds().get(0).getName();
						String xml = XmlConversionlHelper.createXmlFromTree(cellTreeNode.getChilds().get(0));
						
						final MeasureXmlModel measureXmlModel = createMeasureXmlModel(xml);
						service.saveSubTreeInMeasureXml(measureXmlModel, nodeName, nodeUUID,
								new AsyncCallback<Void>() {
							@Override
							public void onFailure(final Throwable caught) {
							}
							@Override
							public void onSuccess(final Void result) {
								xmlTreeDisplay.getWarningMessageDisplay().clear();
								xmlTreeDisplay
								.getSuccessMessageDisplay()
								.setMessage(
										"Changes are successfully saved.");
								setOriginalXML(measureXmlModel.getXml());
								updateSubTreeElementsMap(getOriginalXML());
								xmlTreeDisplay.clearAndAddClauseNamesToListBox();
								xmlTreeDisplay.updateSuggestOracle();
								System.out.println("originalXML is:"
										+ getOriginalXML());
							}
						});
					} else {
						xmlTreeDisplay.getErrorMessageDisplay().setMessage(
								"Unable to save clause as no subTree found under it.");
					}
				}
			}
		});
		xmlTreeDisplay.getDeleteClauseButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				xmlTreeDisplay.clearMessages();
				String measureId = MatContext.get().getCurrentMeasureId();
				final int selectedClauseindex = xmlTreeDisplay.getClauseNamesListBox().getSelectedIndex();
				if(selectedClauseindex < 0){
					return;
				}
				final String clauseUUID = xmlTreeDisplay.getClauseNamesListBox().getValue(selectedClauseindex);
				final String clauseName = xmlTreeDisplay.getClauseNamesListBox().getItemText(selectedClauseindex);
				
				final CellTreeNode cellTreeNode = (CellTreeNode) (xmlTreeDisplay
						.getXmlTree().getRootTreeNode().getChildValue(0));
				if(cellTreeNode.getChilds().size() > 0){
					CellTreeNode childNode = cellTreeNode.getChilds().get(0);
					System.out.println("current clause is:"+childNode.getName());
					if(childNode.getName().equals(clauseName)){
						return;
					}
				}
				
				service.checkAndDeleteSubTree(measureId, clauseUUID, new AsyncCallback<Boolean>() {
					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							xmlTreeDisplay
							.getSuccessMessageDisplay()
							.setMessage(
									"Clause successfully deleted.");
							xmlTreeDisplay.getClauseNamesListBox().removeItem(selectedClauseindex);
							PopulationWorkSpaceConstants.subTreeLookUpNode.remove(clauseName + "~" + clauseUUID);
							PopulationWorkSpaceConstants.subTreeLookUpName.remove(clauseUUID);
							xmlTreeDisplay.updateSuggestOracle();
						} else {
							xmlTreeDisplay.getErrorMessageDisplay().setMessage(
									"Unable to delete clause as it is referenced in populations.");
						}
						
					}
					@Override
					public void onFailure(Throwable caught) {
					}
				});
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
	}
	/**
	 * Method to Reterive SubTree Node and corresponding Node Tree and add to SubTreeLookUpNode map.
	 * Also it retrieves Name and UUID and put it in subTreeNodeName map for display.
	 * @param xml - String.
	 */
	protected void updateSubTreeElementsMap(String xml) {
		if(PopulationWorkSpaceConstants.subTreeLookUpName == null){
			PopulationWorkSpaceConstants.subTreeLookUpName = new TreeMap<String, String>();
		}
		if(PopulationWorkSpaceConstants.subTreeLookUpNode == null){
			PopulationWorkSpaceConstants.subTreeLookUpNode = new TreeMap<String, com.google.gwt.xml.client.Node>();
		}
		Document document = XMLParser.parse(xml);
		NodeList nodeList = document.getElementsByTagName("subTree");
		if ((null != nodeList) && (nodeList.getLength() > 0)) {
			for (int i = 0; i < nodeList.getLength(); i++) {
				NamedNodeMap namedNodeMap = nodeList.item(i).getAttributes();
				String name = namedNodeMap.getNamedItem("displayName").getNodeValue();
				String uuid = namedNodeMap.getNamedItem("uuid").getNodeValue();
				PopulationWorkSpaceConstants.subTreeLookUpNode.put(name + "~" + uuid, nodeList.item(i));
				PopulationWorkSpaceConstants.subTreeLookUpName.put(uuid, name);
			}
		}
		System.out.println("PopulationWorkSpaceConstants.subTreeLookUpName:"+PopulationWorkSpaceConstants.subTreeLookUpName);
	}
	/**
	 * Invoke validate handler on population workspace.
	 */
	final void invokeValidateHandlerPopulationWorkspace() {
	xmlTreeDisplay.getValidateBtnPopulationWorkspace().addClickHandler(new ClickHandler() {
		@Override
		public void onClick(final ClickEvent event) {
			if (xmlTreeDisplay.getXmlTree() != null) {
				xmlTreeDisplay.clearMessages();
				xmlTreeDisplay.setValid(true);
				xmlTreeDisplay.addCommentNodeToSelectedNode();
				CellTreeNode cellTreeNode = (CellTreeNode) xmlTreeDisplay
						.getXmlTree().getRootTreeNode().getChildValue(0);
				/*final MeasureXmlModel measureXmlModel = createMeasureExportModel(XmlConversionlHelper
						.createXmlFromTree(cellTreeNode));
				service.validateMeasureXmlinpopulationWorkspace(measureXmlModel, new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(final Throwable caught) {
						System.out.println("failure");
					}
					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							xmlTreeDisplay.closeNodes(xmlTreeDisplay.getXmlTree()
									.getRootTreeNode());
							xmlTreeDisplay.openAllNodes(xmlTreeDisplay.getXmlTree()
									.getRootTreeNode());
							xmlTreeDisplay.getWarningMessageDisplay().
							setMessage(MatContext.get().getMessageDelegate().getPOPULATION_WORK_SPACE_VALIDATION_ERROR());
						} else {
							xmlTreeDisplay.closeNodes(xmlTreeDisplay.getXmlTree()
									.getRootTreeNode());
							xmlTreeDisplay.getSuccessMessageDisplay().setMessage(
									MatContext.get().getMessageDelegate().
									getPOPULATION_WORK_SPACE_VALIDATION_SUCCESS());
						}
						
					}
					
				});*/
				
				boolean result = xmlTreeDisplay
						.validateCellTreeNodesPopulationWorkspace(xmlTreeDisplay.getXmlTree()
								.getRootTreeNode());
				if (!result) {
					xmlTreeDisplay.closeNodes(xmlTreeDisplay.getXmlTree()
							.getRootTreeNode());
					xmlTreeDisplay.openAllNodes(xmlTreeDisplay.getXmlTree()
							.getRootTreeNode());
					xmlTreeDisplay.getWarningMessageDisplay().
					setMessage(MatContext.get().getMessageDelegate().getPOPULATION_WORK_SPACE_VALIDATION_ERROR());
				} else {
					xmlTreeDisplay.closeNodes(xmlTreeDisplay.getXmlTree()
							.getRootTreeNode());
					xmlTreeDisplay.getSuccessMessageDisplay().setMessage(
							MatContext.get().getMessageDelegate().
							getPOPULATION_WORK_SPACE_VALIDATION_SUCCESS());
				}
			}
		}
	});
}

	
	/**
	 * Invoke validate handler.
	 */
	final void invokeValidateHandler() {
		//Commented Validate Button from Population Work Space as part of Mat-3162
		//Uncommented start
		/*xmlTreeDisplay.getValidateBtn().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				xmlTreeDisplay.clearMessages();
				xmlTreeDisplay.setValid(true);
				boolean result = xmlTreeDisplay
						.validateCellTreeNodes(xmlTreeDisplay.getXmlTree()
								.getRootTreeNode());
				if (!result) {
					xmlTreeDisplay.closeNodes(xmlTreeDisplay.getXmlTree()
							.getRootTreeNode());
					xmlTreeDisplay.openAllNodes(xmlTreeDisplay.getXmlTree()
							.getRootTreeNode());
					xmlTreeDisplay.getWarningMessageDisplay().
					setMessage(MatContext.get().getMessageDelegate().getCLAUSE_WORK_SPACE_VALIDATION_ERROR());
				} else {
					xmlTreeDisplay.closeNodes(xmlTreeDisplay.getXmlTree()
							.getRootTreeNode());
					xmlTreeDisplay.getSuccessMessageDisplay().setMessage(
							MatContext.get().getMessageDelegate().getCLAUSE_WORK_SPACE_VALIDATION_SUCCESS());
				}
			}
		});*/
		xmlTreeDisplay.getValidateBtnClauseWorkSpace().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				if (xmlTreeDisplay.getXmlTree() != null) {
					xmlTreeDisplay.clearMessages();
					xmlTreeDisplay.setValid(true);
					String  result = xmlTreeDisplay
							.validateCellTreeNodes(xmlTreeDisplay.getXmlTree()
									.getRootTreeNode());
					if (result!=null && result.equalsIgnoreCase("inValidAtOtherNode")) {
						xmlTreeDisplay.closeNodes(xmlTreeDisplay.getXmlTree()
								.getRootTreeNode());
						xmlTreeDisplay.openAllNodes(xmlTreeDisplay.getXmlTree()
								.getRootTreeNode());
						xmlTreeDisplay.getWarningMessageDisplay().
						setMessage(MatContext.get().getMessageDelegate().getCLAUSE_WORK_SPACE_VALIDATION_ERROR());
					} 
					if(result!=null && result.equalsIgnoreCase("inValidAtQDMNode")){
						//don't display any error message
						xmlTreeDisplay.closeNodes(xmlTreeDisplay.getXmlTree()
								.getRootTreeNode());
						xmlTreeDisplay.openAllNodes(xmlTreeDisplay.getXmlTree()
								.getRootTreeNode());
					}
					 if(result!=null && result.equalsIgnoreCase("Valid")){
						xmlTreeDisplay.closeNodes(xmlTreeDisplay.getXmlTree()
								.getRootTreeNode());
						xmlTreeDisplay.openAllNodes(xmlTreeDisplay.getXmlTree()
								.getRootTreeNode());
						xmlTreeDisplay.getSuccessMessageDisplay().setMessage(
								MatContext.get().getMessageDelegate().
								getCLAUSE_WORK_SPACE_VALIDATION_SUCCESS());
						
					}
					
					
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
				if (xmlTreeDisplay.isDirty()) {
				//	isUnsavedData = true;
					showErrorMessage(xmlTreeDisplay.getErrorMessageDisplay());
					xmlTreeDisplay.getErrorMessageDisplay().getButtons().get(0).setFocus(true);
					String auditMessage = getRootNode().toUpperCase() + "_TAB_YES_CLICKED";
					handleClickEventsOnUnsavedErrorMsg(xmlTreeDisplay.getErrorMessageDisplay().getButtons()
							, xmlTreeDisplay.getErrorMessageDisplay(), auditMessage);
				} else {
				//	isUnsavedData = false;
					xmlTreeDisplay.setDirty(false);
					panel.clear();
					loadClauseWorkSpaceView(panel);
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
	 * @param btns -List.
	 * @param saveErrorMessage - ErrorMessageDisplay.
	 * @param auditMessage -String.
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
//		if (isUnsavedData) {
//			MatContext.get().setErrorTab(true);
//		}
	}
	
	/**
	 * Gets the root node.
	 * @return the rootNode
	 */
	public final String getRootNode() {
		return rootNode;
	}
	/**
	 * Sets the root node.
	 * 
	 * @param rootNode
	 *            the rootNode to set
	 */
	public final void setRootNode(final String rootNode) {
		this.rootNode = rootNode;
	}
	
	/**
	 * Sets the this member variable is used to pass the original measure XML to
	 * XmlTreePresenter class which will then be used to construct the CellTree.
	 * 
	 * @param originalXML
	 *            the new this member variable is used to pass the original
	 *            measure XML to XmlTreePresenter class which will then be used
	 *            to construct the CellTree
	 */
	public final void setOriginalXML(final String originalXML) {
		this.originalXML = originalXML;
	}
	
	/**
	 * Gets the this member variable is used to pass the original measure XML to
	 * XmlTreePresenter class which will then be used to construct the CellTree.
	 * 
	 * @return the this member variable is used to pass the original measure XML
	 *         to XmlTreePresenter class which will then be used to construct
	 *         the CellTree
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
	 * @param xmlTreeDisplay
	 *            the new xml tree display
	 */
	public final void setXmlTreeDisplay(final XmlTreeDisplay xmlTreeDisplay) {
		this.xmlTreeDisplay = xmlTreeDisplay;
	}
}
