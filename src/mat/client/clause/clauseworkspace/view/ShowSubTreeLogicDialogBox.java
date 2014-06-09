package mat.client.clause.clauseworkspace.view;

import java.util.List;

import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;
import mat.client.clause.clauseworkspace.presenter.XmlConversionlHelper;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;
import mat.client.shared.DialogBoxWithCloseButton;
import mat.client.shared.SpacerWidget;

import org.apache.commons.lang.StringUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.Node;

// TODO: Auto-generated Javadoc
//TODO by Ravi
/**
 * The Class ShowSubTreeLogicDialogBox.
 */
public class ShowSubTreeLogicDialogBox extends XmlConversionlHelper{
	
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
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		dialogBox.getElement().setAttribute("id", "ClauseLogicDialogBox");
		DOM.setStyleAttribute(dialogBox.getElement(), "width", "1000px");
		DOM.setStyleAttribute(dialogBox.getElement(), "height", "1000px");
		dialogContents.clear();
		dialogBox.setWidget(dialogContents);
		dialogBox.addCloseHandler(new CloseHandler<PopupPanel>() {
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				dialogBox.hide();
				clauseTreeDisplay.setClauseEnabled(true);
			}
		});
        dialogContents.add(loadClauseLogic(xmlTreeDisplay));
		dialogContents.add(new SpacerWidget());
		dialogContents.add(new SpacerWidget());
		dialogBox.setPopupPosition(300, 200);
		dialogBox.show();
		
	}

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
		showClause(cellTreeNode, node.getName(), node.getUUID());
		dialogBox.setText("Clause Logic: ("+ node.getName() +" )");
		dialogBox.setTitle("Clause Logic: ("+ node.getName() +" )");
		xmlTreeView.openAllNodes(cellTree.getRootTreeNode());
		simplePanel.clear();
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
	private static void showClause(CellTreeNode cellTreeNode,
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
		//	subTreeCellTreeNode = checkIfClauseAndAppend(subTreeCellTreeNode);
		cellTreeNode.appendChild(subTreeCellTreeNode.getChilds().get(0));

	}
	
	/**
	 * Check if clause and append.
	 *
	 * @param subTreeCellTreeNode the sub tree cell tree node
	 * @return the node
	 */
	private static CellTreeNode checkIfClauseAndAppend(CellTreeNode subTreeCellTreeNode){
		 
		List<CellTreeNode> childNodes = (List<CellTreeNode>) subTreeCellTreeNode.getChilds();
		
		CellTreeNode childNode = null;
		
		if(childNodes!=null) {
			
			CellTreeNode treeCellTreeNode = null;
	   
		for(int i = 0; i<childNodes.size(); i ++){
			
			childNode = childNodes.get(i);
			if(childNode.getNodeType() == CellTreeNode.SUBTREE_REF_NODE) {
				//System.out.println("SubtreeRef Node" + childNode.getNodeType());
				Node node = PopulationWorkSpaceConstants.subTreeLookUpNode
						.get(childNode.getName() + "~" + childNode.getUUID());
					CellTreeNode cellTreeNode = XmlConversionlHelper
						.createCellTreeNode(node, childNode.getName());
					childNode.appendChild(cellTreeNode.getChilds().get(0).getChilds().get(0));
					//treeCellTreeNode = cellTreeNode;
			}
			
			if(childNode.getChilds()!=null){
//				treeCellTreeNode = childNode.getChilds().get(i);
				treeCellTreeNode = childNodes.get(i);
				}
				if(treeCellTreeNode!=null){
				checkIfClauseAndAppend(treeCellTreeNode);
				}
		}
		
	}
		return subTreeCellTreeNode;
	}

}
