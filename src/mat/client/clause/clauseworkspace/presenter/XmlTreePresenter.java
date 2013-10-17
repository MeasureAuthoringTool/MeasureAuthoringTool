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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SimplePanel;

public class XmlTreePresenter {
	
	
	interface TreeResources extends CellTree.Resources {
		@Source("mat/client/images/plus.png")
		ImageResource cellTreeClosedItem();

		@Source("mat/client/images/minus.png")
		ImageResource cellTreeOpenItem();

		@Source("mat/client/images/CwCellTree.css")
		CellTree.Style cellTreeStyle();

		/*
		 * @Source("mat/client/images/cms_gov_footer.png")
		 * 
		 * @ImageOptions(repeatStyle = RepeatStyle.Horizontal, flipRtl = true)
		 * ImageResource cellTreeSelectedBackground();
		 */
	}

	XmlTreeDisplay xmlTreeDisplay;
	MeasureServiceAsync service = MatContext.get().getMeasureService();
	private static final String MEASURE = "measure";
	private String rootNode;
	SimplePanel panel;

	/**
	 * This member variable is used to pass the original measure XML to
	 * XmlTreePresenter class which will then be used to construct the CellTree.
	 * Tree construction is done using loadXmlTree(..) method. Once the
	 * loadXmlTree(..) method is done executing, originalXML should not be used.
	 * Please refrain from using it anywhere other that the loadXmlTree(...)
	 * method.
	 */
	private String originalXML = "";

	public final void loadXmlTree(SimplePanel panel) {

		if (originalXML.length() > 0) {
			this.panel = panel;
			panel.clear();
			String xml = originalXML;
			XmlTreeView xmlTreeView = new XmlTreeView(
					XmlConversionlHelper.createCellTreeNode(xml, rootNode));// converts
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
			CellTree cellTree = new CellTree(xmlTreeView, null, resource);// CellTree
																		  // Creation
			cellTree.setDefaultNodeSize(500);// this will get rid of the show
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
			xmlTreeDisplay = (XmlTreeDisplay) xmlTreeView;
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

	private MeasureXmlModel createMeasureExportModel(final String xml) {
		MeasureXmlModel exportModal = new MeasureXmlModel();
		exportModal.setMeasureId(MatContext.get().getCurrentMeasureId());
		exportModal.setToReplaceNode(rootNode);
		exportModal.setParentNode(MEASURE);
		exportModal.setXml(xml);
		return exportModal;
	}

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

	}

	/**
	 * @return the rootNode
	 */
	public final String getRootNode() {
		return rootNode;
	}

	/**
	 * @param rootNode
	 *            the rootNode to set
	 */
	public final void setRootNode(final String rootNode) {
		this.rootNode = rootNode;
	}

	public final void setOriginalXML(final String originalXML) {
		this.originalXML = originalXML;
	}

	public final String getOriginalXML() {
		return originalXML;
	}

	public final XmlTreeDisplay getXmlTreeDisplay() {
		return xmlTreeDisplay;
	}

	public final void setXmlTreeDisplay(final XmlTreeDisplay xmlTreeDisplay) {
		this.xmlTreeDisplay = xmlTreeDisplay;
	}

}
