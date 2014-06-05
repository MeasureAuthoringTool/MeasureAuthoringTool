package mat.client.clause.clauseworkspace.view;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.format.CellTextFormatter;

import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;
import mat.client.clause.clauseworkspace.presenter.XmlConversionlHelper;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;
import mat.client.shared.DialogBoxWithCloseButton;
import mat.client.shared.MatContext;
import mat.client.shared.SecondaryButton;
import mat.client.shared.SpacerWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

// TODO: Auto-generated Javadoc
//TODO by Ravi
/**
 * The Class ShowSubTreeLogicDialogBox.
 */
public class ShowSubTreeLogicDialogBox {
	
	/** The Constant dialogBox. */
	final static DialogBoxWithCloseButton dialogBox = new DialogBoxWithCloseButton(StringUtils.EMPTY);
	
	/** The clause tree display. */
	private static XmlTreeDisplay clauseTreeDisplay;
	
	/** The Constant NODESIZE. */
	private static final int NODESIZE = 500;

	/**
	 * The Interface TreeResources.
	 */
	interface TreeResources extends CellTree.Resources {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.google.gwt.user.cellview.client.CellTree.Resources#cellTreeClosedItem
		 * ()
		 */
		@Override
		@Source("mat/client/images/plus.png")
		ImageResource cellTreeClosedItem();

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.google.gwt.user.cellview.client.CellTree.Resources#cellTreeOpenItem
		 * ()
		 */
		@Override
		@Source("mat/client/images/minus.png")
		ImageResource cellTreeOpenItem();

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.google.gwt.user.cellview.client.CellTree.Resources#cellTreeStyle
		 * ()
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

	/**
	 * Show sub tree logic dialog box.
	 *
	 * @param xmlTreeDisplay the xml tree display
	 * @param isAdd the is add
	 */
	public static void showSubTreeLogicDialogBox(
			final XmlTreeDisplay xmlTreeDisplay, boolean isAdd) {
		final VerticalPanel dialogContents = new VerticalPanel();
		//dialogBox.setSize("400px","400px");
		//HorizontalPanel buttonPanel = new HorizontalPanel();
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		dialogBox.setText("Show Clause Logic");
		dialogBox.setTitle("Show Clause Logic");
		dialogBox.getElement().setAttribute("id", "ClauseLogicDialogBox");
		DOM.setStyleAttribute(dialogBox.getElement(), "width", "1000px");
		DOM.setStyleAttribute(dialogBox.getElement(), "height", "1000px");
		dialogContents.clear();
		dialogBox.setWidget(dialogContents);
//		SecondaryButton close = new SecondaryButton("Close");
//		addClickHandlers(close);
		dialogBox.addCloseHandler(new CloseHandler<PopupPanel>() {
			
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				dialogBox.hide();
				clauseTreeDisplay.setClauseEnabled(true);
			}
		});
		//buttonPanel.add(close);
		//buttonPanel.setCellHorizontalAlignment(close,
		//		HasHorizontalAlignment.ALIGN_RIGHT);
        dialogContents.add(loadClauseLogic(xmlTreeDisplay));
		dialogContents.add(new SpacerWidget());
		//dialogContents.add(buttonPanel);
		dialogContents.add(new SpacerWidget());
		dialogBox.setPopupPosition(300, 200);
		dialogBox.show();
		
	}
	
	/**
	 * Adds the click handlers.
	 *
	 * @param close the close
	 */
//	public static void addClickHandlers(SecondaryButton close){
//		close.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				dialogBox.hide();
//				clauseTreeDisplay.setClauseEnabled(true);
//			}
//		});
//	}

	/**
	 * Load clause logic.
	 *
	 * @param xmlTreeDisplay the xml tree display
	 * @return the simple panel
	 */
	private final static VerticalPanel loadClauseLogic(
			XmlTreeDisplay xmlTreeDisplay) {

		VerticalPanel simplePanel = new VerticalPanel();
		SimplePanel panel = new SimplePanel();
		panel.addStyleDependentName("measurePackagerSupplementalDatascrollable");
		//panel.setAlwaysShowScrollBars(true);
		simplePanel.getElement().setAttribute("id", "ClauseLogic");
		CellTreeNode subTree = XmlConversionlHelper.createRootClauseNode();
		XmlTreeView xmlTreeView = new XmlTreeView(subTree);
		CellTree.Resources resource = GWT.create(TreeResources.class);
		CellTree cellTree = new CellTree(xmlTreeView, null, resource); //CellTree Creation
		cellTree.setDefaultNodeSize(NODESIZE); // this will get rid of the show
		// more link on the bottom of the
		// Tree
		xmlTreeView.createClauseLogicPageView(cellTree); // Page Layout
		cellTree.setTabIndex(0);
		// This will open the tree by default.
		TreeNode treeNode = cellTree.getRootTreeNode();
		for (int i = 0; i < treeNode.getChildCount(); i++) {
			if (((CellTreeNode) treeNode.getChildValue(i)).getNodeType() == CellTreeNode.SUBTREE_ROOT_NODE) {
				treeNode.setChildOpen(i, true, true);
			}
		}
		CellTreeNode node = xmlTreeDisplay.getSelectedNode();
		clauseTreeDisplay = xmlTreeView;
		clauseTreeDisplay.setClauseEnabled(true);
		final CellTreeNode cellTreeNode = (CellTreeNode) (clauseTreeDisplay
				.getXmlTree().getRootTreeNode().getChildValue(0));
		changeClause(cellTreeNode, node.getName(), node.getUUID());
		xmlTreeView.openAllNodes(cellTree.getRootTreeNode());
		simplePanel.clear();
		//panel.setSize("400px","400px");
		panel.add(clauseTreeDisplay.asWidget());
		simplePanel.add(panel);
		return simplePanel;
	}

	/**
	 * Change clause.
	 *
	 * @param cellTreeNode the cell tree node
	 * @param selectedClauseName the selected clause name
	 * @param selectedClauseUUID the selected clause uuid
	 */
	private static void changeClause(CellTreeNode cellTreeNode,
			String selectedClauseName, String selectedClauseUUID) {

		if (cellTreeNode.getChilds().size() > 0) {
			CellTreeNode childNode = cellTreeNode.getChilds().get(0);
			System.out.println("clearing out:" + childNode.getName());
			cellTreeNode.removeChild(childNode);
		}

		Node node = PopulationWorkSpaceConstants.subTreeLookUpNode
				.get(selectedClauseName + "~" + selectedClauseUUID);
		CellTreeNode subTreeCellTreeNode = XmlConversionlHelper
				.createCellTreeNode(node, selectedClauseName);
		cellTreeNode.appendChild(subTreeCellTreeNode.getChilds().get(0));
//		clauseTreeDisplay.getXmlTree().getRootTreeNode().setChildOpen(0, false);
//		clauseTreeDisplay.getXmlTree().getRootTreeNode().setChildOpen(0, true);
//		clauseTreeDisplay.getButtonExpandClauseWorkSpace().click();
	}

}
