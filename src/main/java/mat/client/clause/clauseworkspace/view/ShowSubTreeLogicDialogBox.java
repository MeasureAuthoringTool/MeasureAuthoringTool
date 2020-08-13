package mat.client.clause.clauseworkspace.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;
import mat.client.clause.clauseworkspace.presenter.XmlConversionlHelper;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;
import mat.client.shared.DialogBoxWithCloseButton;
import mat.client.shared.SpacerWidget;
import org.apache.commons.lang3.StringUtils;

public class ShowSubTreeLogicDialogBox extends XmlConversionlHelper {

	final static DialogBoxWithCloseButton dialogBox = new DialogBoxWithCloseButton(
			StringUtils.EMPTY);

	private static XmlTreeDisplay clauseTreeDisplay;

	private static final int NODESIZE = 500;

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

	public static void showSubTreeLogicDialogBox(
			final XmlTreeDisplay xmlTreeDisplay, boolean isAdd) {
		final ScrollPanel panel = new ScrollPanel();
		final VerticalPanel  dialogContents= new VerticalPanel();
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		dialogBox.getElement().setAttribute("id", "ClauseLogicDialogBox");
		DOM.setStyleAttribute(dialogBox.getElement(), "width", "800px");
		dialogContents.clear();
		dialogBox.setWidget(panel);
		dialogBox.addCloseHandler(new CloseHandler<PopupPanel>() {
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				dialogBox.hide();
				clauseTreeDisplay.setClauseEnabled(true);
			}
		});
		panel.setAlwaysShowScrollBars(true);
		panel.setSize("700px", "400px");
		dialogContents.add(loadClauseLogic(xmlTreeDisplay));
		dialogContents.add(new SpacerWidget());
		dialogContents.add(new SpacerWidget());
		dialogContents.add(new SpacerWidget());
		panel.add(dialogContents);
		dialogBox.setPopupPosition(300, 200);
		dialogBox.show();

	}

	private final static VerticalPanel loadClauseLogic(
			XmlTreeDisplay xmlTreeDisplay) {

		VerticalPanel simplePanel = new VerticalPanel();
		SimplePanel panel = new SimplePanel();
		panel.addStyleDependentName("measurePackagerSupplementalDatascrollable");
		simplePanel.getElement().setAttribute("id", "ClauseLogic");
		CellTreeNode subTree = XmlConversionlHelper.createRootClauseNode();
		XmlTreeView xmlTreeView = new XmlTreeView(subTree);
		CellTree.Resources resource = GWT.create(TreeResources.class);
		CellTree cellTree = new CellTree(xmlTreeView, null, resource); // CellTree Creation
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
		dialogBox.setText("Clause Logic: (" + node.getName() + " )");
		dialogBox.setTitle("Clause Logic: (" + node.getName() + " )");
		xmlTreeView.openAllNodes(cellTree.getRootTreeNode());
		simplePanel.clear();
		panel.add(clauseTreeDisplay.asWidget());
		simplePanel.add(panel);
		return simplePanel;
	}

	private static void showClause(CellTreeNode cellTreeNode,
			String selectedClauseName, String selectedClauseUUID) {

		if (cellTreeNode.getChilds().size() > 0) {
			CellTreeNode childNode = cellTreeNode.getChilds().get(0);
			System.out.println("clearing out:" + childNode.getName());
			cellTreeNode.removeChild(childNode);
		}
		Node node = PopulationWorkSpaceConstants.subTreeLookUpNode
				.get(selectedClauseName + "~" + selectedClauseUUID);
		CellTreeNode subTreeCellTreeNode = null;
		NamedNodeMap namedNodeMap = node.getAttributes();
		if (namedNodeMap.getNamedItem("instance") != null) { 
			String instanceOfUUID = namedNodeMap.getNamedItem("instanceOf").getNodeValue();
			String instanceOfDisplayName = namedNodeMap.getNamedItem("displayName").getNodeValue();
			Node occurenceNode = PopulationWorkSpaceConstants.subTreeLookUpNode
					.get(instanceOfDisplayName + "~" + instanceOfUUID);
			subTreeCellTreeNode = XmlConversionlHelper
					.createCellTreeNode(occurenceNode, instanceOfDisplayName);	
		} else {
			subTreeCellTreeNode = XmlConversionlHelper
					.createCellTreeNode(node, selectedClauseName);
		}
		
		cellTreeNode.appendChild(subTreeCellTreeNode.getChilds().get(0));
		TreeNode treeNode = checkIfClauseAndAppend(clauseTreeDisplay
				.getXmlTree().getRootTreeNode());
		subTreeCellTreeNode = (CellTreeNode) treeNode.getChildValue(0);
	}

	private static TreeNode checkIfClauseAndAppend(TreeNode treeNode) {

		if (treeNode != null) {
			clauseTreeDisplay.openAllNodes(treeNode);
			for (int i = 0; i < treeNode.getChildCount(); i++) {
				TreeNode subTree = null;
				CellTreeNode node = (CellTreeNode) treeNode.getChildValue(i);
				if (node.getNodeType() == CellTreeNode.SUBTREE_REF_NODE) {
					Node childNode = PopulationWorkSpaceConstants.subTreeLookUpNode
							.get(node.getName() + "~" + node.getUUID());
					CellTreeNode subTreeCellTreeNode = null;
					NamedNodeMap namedNodeMap = childNode.getAttributes();
					if (namedNodeMap.getNamedItem("instance") != null) { 
						String instanceOfUUID = namedNodeMap.getNamedItem("instanceOf").getNodeValue();
						String instanceOfDisplayName = namedNodeMap.getNamedItem("displayName").getNodeValue();
						Node occurenceNode = PopulationWorkSpaceConstants.subTreeLookUpNode
								.get(instanceOfDisplayName + "~" + instanceOfUUID);
						subTreeCellTreeNode = XmlConversionlHelper
								.createCellTreeNode(occurenceNode, instanceOfDisplayName);	
					} else {
						subTreeCellTreeNode = XmlConversionlHelper
							.createCellTreeNode(childNode, node.getName());
					}
					if(subTreeCellTreeNode.getChilds()!=null
							 && subTreeCellTreeNode.getChilds().get(0)
								.getChilds()!=null){
							node.appendChild(subTreeCellTreeNode.getChilds().get(0)
									.getChilds().get(0));
					}
					
				}
				subTree = treeNode.setChildOpen(i,
						((CellTreeNode) treeNode.getChildValue(i)).isOpen(),
						((CellTreeNode) treeNode.getChildValue(i)).isOpen());
				if ((subTree != null) && (subTree.getChildCount() > 0)) {
					checkIfClauseAndAppend(subTree);
				}
			}
		}
		return treeNode;
	}

}
