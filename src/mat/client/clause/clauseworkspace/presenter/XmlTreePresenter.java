package mat.client.clause.clauseworkspace.presenter;

import mat.client.Mat;
import mat.client.MeasureComposerPresenter;
import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.clause.clauseworkspace.view.XmlTreeView;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.MatContext;
import mat.shared.ConstantMessages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * The Class XmlTreePresenter.
 */
public class XmlTreePresenter {
	
	/**
	 * Cell Tree Node Size to remove show more.
	 */
	private static final int NODESIZE = 500;
	
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
	public final void loadXmlTree(SimplePanel populationWorkSpacePanel) {
		
		if (originalXML.length() > 0) {
			panel = populationWorkSpacePanel;
			
			panel.clear();
			String xml = originalXML;
			XmlTreeView xmlTreeView = new XmlTreeView(
					XmlConversionlHelper.createCellTreeNode(xml, rootNode)); // converts
			// XML
			// to
			// TreeModel
			// Object
			// and
			// sets
			// to
			// XmlTreeView
			// CellTree cellTree = new CellTree(xmlTreeView, null);
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
			invokeValidateHandler();
		} else {
			Mat.hideLoadingMessage();
		}
		MeasureComposerPresenter.setSubSkipEmbeddedLink("ClauseWorkspaceTree");
		Mat.focusSkipLists("MeasureComposer");
		
	}
	
	/**
	 * Method to create Clause Work space View.
	 * @param clauseWorkSpacePanel - SimplePanel.
	 */
	public final void loadClauseWorkSpaceView(SimplePanel clauseWorkSpacePanel) {
		panel = clauseWorkSpacePanel;
		XmlTreeView xmlTreeView = new XmlTreeView(null);
		xmlTreeView.createClauseWorkSpacePageView(null);
		xmlTreeDisplay = xmlTreeView;
		xmlTreeDisplay.setEnabled(MatContext.get().getMeasureLockService()
				.checkForEditPermission());
		panel.clear();
		panel.add(xmlTreeDisplay.asWidget());
		invokeCreateNewClauseHandler();
		invokeSaveSubTreeHandler();
		invokeValidateHandler();
	}
	
	/**
	 * Method to invoke Create SubTree Node Handler.
	 */
	private void invokeCreateNewClauseHandler() {
		xmlTreeDisplay.getCreateSubTree().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!xmlTreeDisplay.isDirty()) {
					if ((xmlTreeDisplay.getSubTreeTextBox().getValue() != null)
							&& (xmlTreeDisplay.getSubTreeTextBox().getValue().trim().length() > 0)) {
						XmlTreeView xmlTreeView = (XmlTreeView) xmlTreeDisplay;
						CellTreeNode subTree = XmlConversionlHelper.createSubTreeNode(
								xmlTreeDisplay.getSubTreeTextBox().getValue());
						xmlTreeView.createRootNode(subTree);
						CellTree.Resources resource = GWT.create(TreeResources.class);
						CellTree cellTree = new CellTree(xmlTreeView, null, resource); // CellTree
						// Creation
						cellTree.setDefaultNodeSize(NODESIZE);  // this will get rid of the show
						// more link on the bottom of the
						// Tree
						xmlTreeView.createClauseWorkSpacePageView(cellTree); // Page Layout
						cellTree.setTabIndex(0);
						xmlTreeDisplay = xmlTreeView;
						xmlTreeDisplay.clearMessages();
						xmlTreeDisplay.setDirty(true);
						xmlTreeDisplay.setEnabled(MatContext.get().getMeasureLockService()
								.checkForEditPermission());
						panel.clear();
						panel.add(xmlTreeDisplay.asWidget());
						setRootNode(xmlTreeDisplay.getSubTreeTextBox().getValue());
					}
				} else {
					Window.alert(" Root Node for SubTree is :: "
							+ xmlTreeDisplay.getSubTreeTextBox().getValue() + " No saved !!!! ");
				}
			}
		});
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
		MeasureXmlModel exportModal = new MeasureXmlModel();
		exportModal.setMeasureId(MatContext.get().getCurrentMeasureId());
		exportModal.setToReplaceNode("subTree");
		exportModal.setParentNode("/measure/subTreeLookUp");
		exportModal.setXml(xml);
		return exportModal;
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
	}
	/**
	 * Invoke Clause Work Space save handler.
	 */
	private void invokeSaveSubTreeHandler() {
		xmlTreeDisplay.getSaveBtnClauseWorkSpace().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				if (xmlTreeDisplay.getXmlTree() != null) {
					xmlTreeDisplay.clearMessages();
					xmlTreeDisplay.setDirty(false);
					MatContext.get().recordTransactionEvent(
							MatContext.get().getCurrentMeasureId(), null,
							"CLAUSEWORKSPACE_TAB_SAVE_EVENT",
							rootNode.toUpperCase().concat(" Saved."),
							ConstantMessages.DB_LOG);
					CellTreeNode cellTreeNode = (CellTreeNode) xmlTreeDisplay
							.getXmlTree().getRootTreeNode().getChildValue(0);
					String nodeUUID = cellTreeNode.getUUID();
					String xml = XmlConversionlHelper.createXmlFromTree(cellTreeNode);
					System.out.println("Generated XML  :: " + xml);
					System.out.println("nodeUUID  :: " + nodeUUID);
					final MeasureXmlModel measureXmlModel = createMeasureXmlModel(xml);
					service.saveSubTreeInMeasureXml(measureXmlModel, nodeUUID,
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
							System.out.println("originalXML is:"
									+ getOriginalXML());
						}
					});
				}
			}
		});
	}
	/**
	 * Invoke validate handler.
	 */
	final void invokeValidateHandler() {
		xmlTreeDisplay.getValidateBtn().addClickHandler(new ClickHandler() {
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
		});
		xmlTreeDisplay.getValidateBtnClauseWorkSpace().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				if (xmlTreeDisplay.getXmlTree() != null) {
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
								MatContext.get().getMessageDelegate().
								getCLAUSE_WORK_SPACE_VALIDATION_SUCCESS());
					}
				}
			}
		});
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
