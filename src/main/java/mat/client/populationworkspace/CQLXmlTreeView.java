package mat.client.populationworkspace;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import mat.client.clause.clauseworkspace.model.CQLCellTreeNode;
import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.presenter.CQLXmlTreeDisplay;
import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;
import mat.shared.UUIDUtilClient;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


// TODO: Auto-generated Javadoc
/**
 * The Class XmlTreeView.
 */
public class CQLXmlTreeView extends Composite implements  CQLXmlTreeDisplay, TreeViewModel, KeyDownHandler, FocusHandler {
	
	
	/**
	 * The Interface Template.
	 */
	interface Template extends SafeHtmlTemplates {
		
		/**
		 * Outer div.
		 *
		 * @param classes the classes
		 * @param id the id
		 * @param title the title
		 * @param content the content
		 * @return the safe html
		 */
		@Template("<div class=\"{0}\" id=\"{1}\" title=\"{2}\" aria-role=\"tree\">{3}</div>")
		SafeHtml outerDiv(String classes, String id , String title, String content);
		
		/**
		 * Outer div for Tree Item.
		 *
		 * @param classes the classes
		 * @param id the id
		 * @param title the title
		 * @param content the content
		 * @return the safe html
		 */
		@Template("<div class=\"{0}\" id=\"{1}\" title=\"{2}\" aria-role=\"treeitem\">{3}</div>")
		SafeHtml outerDivItem(String classes,String id, String title, String content);
		
		/**
		 * Div for Nodes with Comment.
		 *
		 * @param classes the classes
		 * @param id the id
		 * @param title the title
		 * @param content the content
		 * @return the safe html
		 */
		@Template("<div class=\"{0}\" id=\"{1}\" title=\"{2}\" aria-role=\"treeitem\">{3}"
				+ "<span class =\"populationWorkSpaceCommentNode\">&nbsp;(C)</span></div>")
		SafeHtml outerDivItemWithSpan(String classes, String id, String title, String content);
		
		
	}
	
	/** The Constant template. */
	private static final Template template = GWT.create(Template.class);
	
	
	/** The main panel. */
	private FlowPanel  mainPanel = new FlowPanel();
	
	private SimplePanel focusPanel = new SimplePanel(mainPanel);
	
	/** The cell tree. */
	private CellTree cellTree;
	
	/** The button expand. */
	private Button buttonExpand = new Button();
	
	/** The button collapse. */
	private Button buttonCollapse = new Button();
	
	
	/** The node data provider. */
	private ListDataProvider<CQLCellTreeNode> nodeDataProvider;
	
	/** The selected node. */
	private CQLCellTreeNode selectedNode;
	
	/** The selection model. */
	private final SingleSelectionModel<CQLCellTreeNode> selectionModel = new SingleSelectionModel<CQLCellTreeNode>();
	
	
	
	public CQLXmlTreeView(CQLCellTreeNode cellTreeNode ) {
		clearMessages();
		if (cellTreeNode != null) {
			createRootNode(cellTreeNode);
			addHandlers();
		}
		mainPanel.getElement().setId("mainPanel_FlowPanel");
		buttonExpand.getElement().setId("buttonExpand_Button");
		buttonCollapse.getElement().setId("buttonCollapse_Button");
	}

	
	public void buildView(CellTree cellTree) {
		this.cellTree = cellTree;
		mainPanel.clear();
		mainPanel.setHeight("100%");
		VerticalPanel treePanel =  new VerticalPanel();
		treePanel.getElement().setId("treePanel_VerticalPanelCW");
		HorizontalPanel expandCollapse  = new HorizontalPanel();
		expandCollapse.getElement().setId("expandCollapse_HorizontalPanel");
		expandCollapse.setStyleName("leftAndTopPadding");
		expandCollapse.setSize("100px", "20px");
		buttonExpand.setStylePrimaryName("expandAllButton");
		buttonCollapse.setStylePrimaryName("collapseAllButton");
		buttonExpand.setTitle("Expand All (Shift +)");
		
		buttonCollapse.setTitle("Collapse All (Shift -)");
		expandCollapse.add(buttonExpand);
		expandCollapse.add(buttonCollapse);
		buttonExpand.setFocus(true);
		buttonCollapse.setVisible(true);
		if (cellTree != null) {
			treePanel.add(expandCollapse);
			treePanel.add(cellTree);
			addHandlers();
			cellTreeHandlers();
			openAllNodes(cellTree.getRootTreeNode()); // all nodes should be open on load
		} else {
			treePanel.setHeight("100%");
		}
		mainPanel.add(treePanel);
	}
	
	/**
	 * Selection Handler, Tree Open and Close Handlers Defined.
	 */
	private void cellTreeHandlers() {
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				// assigning the selected object to the selectedNode variable.
				selectedNode = selectionModel.getSelectedObject();
			}
		});
		/**
		 * This handler is implemented to save the open state of the Celltree in CellTreeNode Object
		 * Set to isOpen boolean in CellTreeNode.
		 * After adding/removing/editing a node to the celltree
		 * Manually  we have to close and open all nodes to see the new node,
		 * so using this boolean we will know which node was already in opened state and closed state.
		 */
		cellTree.addOpenHandler(new OpenHandler<TreeNode>() {
			@Override
			public void onOpen(OpenEvent<TreeNode> event) {
				CQLCellTreeNode node = (CQLCellTreeNode) event.getTarget().getValue();
				node.setOpen(true);
				clearMessages();
			}
		});
		cellTree.addCloseHandler(new CloseHandler<TreeNode>() {
			@Override
			public void onClose(CloseEvent<TreeNode> event) {
				CQLCellTreeNode node = (CQLCellTreeNode) event.getTarget().getValue();
				setOpenToFalse(node); // when a node is closed set all the child nodes isOpen boolean to false.
				node.setOpen(false);
				clearMessages();
			}
		});
	}
	/**
	 * Iterating through all the child nodes and setting the isOpen boolean to
	 * false.
	 * @param node
	 *            the new open to false
	 */
	private void setOpenToFalse(CQLCellTreeNode node) {
		if (node.hasChildren()) {
			for (CQLCellTreeNode child : node.getChilds()) {
				child.setOpen(false);
				setOpenToFalse(child);
			}
		}
	}
	
	/* * Creates the Root Node in the CellTree. Sets the Root node to the ListData
	 * Provider.
	 * 
	 * @param cellTreeNode
	 *            the cell tree node
	 */
	
	private void createRootNode(CQLCellTreeNode cellTreeNode) {
		if ((cellTreeNode.getChilds() != null) && (cellTreeNode.getChilds().size() > 0)) {
			nodeDataProvider = new ListDataProvider<CQLCellTreeNode>(cellTreeNode.getChilds());
		}
	}
	
	/**
	 * Expand / Collapse Link - Click Handlers.
	 */
	private void addHandlers() {
		
		buttonExpand.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				openAllNodes(cellTree.getRootTreeNode());
				buttonExpand.setVisible(true);
				buttonCollapse.setVisible(true);
			}
		});
		
		buttonCollapse.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				closeNodes(cellTree.getRootTreeNode());
				buttonExpand.setVisible(true);
				buttonCollapse.setVisible(true);
			}
		});
	}
	
	public void openMainNode() {
		TreeNode treeNode = cellTree.getRootTreeNode();
		for (int i = 0; i < treeNode.getChildCount(); i++) {
			TreeNode subTree = treeNode.setChildOpen(i, true, true);
			if ((subTree != null) && (subTree.getChildCount() > 0)) {
				for (int j = 0; j < subTree.getChildCount(); j++) {
					subTree.setChildOpen(j, true, true);
				}
			}
		}
	}
	
	@Override
	public void onFocus(FocusEvent event) {
		focusPanel.setStyleName("focusPanel");
		
	}

	@Override
	public void onKeyDown(KeyDownEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {
		if (value == null) {
			NodeCell nodeCell = new NodeCell();
			return new DefaultNodeInfo<CQLCellTreeNode>(nodeDataProvider, nodeCell, selectionModel, null);
		} else {
			CQLCellTreeNode myValue = (CQLCellTreeNode) value;
			ListDataProvider<CQLCellTreeNode> dataProvider = new ListDataProvider<CQLCellTreeNode>(myValue.getChilds());
			NodeCell nodeCell = new NodeCell();
			return new DefaultNodeInfo<CQLCellTreeNode>(dataProvider, nodeCell, selectionModel, null);
		}
	}

	@Override
	public boolean isLeaf(Object value) {
		if (value instanceof CQLCellTreeNode) {
			CQLCellTreeNode t = (CQLCellTreeNode) value;
			if (!t.hasChildren()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public CellTree getXmlTree() {
		return cellTree;
	}

	@Override
	public void clearMessages() {
		
		
	}

	@Override
	public CQLCellTreeNode getSelectedNode() {
		return selectedNode;
	}

	@Override
	public void expandSelected(TreeNode treeNode) {
		if (treeNode != null) {
			for (int i = 0; i < treeNode.getChildCount(); i++) {
				TreeNode subTree = null;
				// this check is performed since IE was giving JavaScriptError after removing a node and closing all nodes.
				if (treeNode.getChildValue(i).equals(selectedNode)) {
					// to avoid that we are closing the parent of the removed node.
					subTree = treeNode.setChildOpen(i, true, true);
					if ((subTree != null) && (subTree.getChildCount() > 0)) {
						openAllNodes(subTree);
					}
					break;
				}
				subTree = treeNode.setChildOpen(i, ((CellTreeNode) treeNode.getChildValue(i)).isOpen()
						, ((CellTreeNode) treeNode.getChildValue(i)).isOpen());
				if ((subTree != null) && (subTree.getChildCount() > 0)) {
					expandSelected(subTree);
				}
			}
		}
		
	}

	@Override
	public void closeNodes(TreeNode node) {
		if (node != null) {
			for (int i = 0; i < node.getChildCount(); i++) {
				TreeNode subTree  = null;
				if (((CQLCellTreeNode) node.getChildValue(i)).getNodeType() == CQLCellTreeNode.MASTER_ROOT_NODE) {
					subTree =  node.setChildOpen(i, true, true);
				} else {
					subTree =  node.setChildOpen(i, false, true);
				}
				
				if ((subTree != null) && (subTree.getChildCount() > 0)){
					closeNodes(subTree);
				}
			}
			
		}
		
	}

	@Override
	public void openAllNodes(TreeNode treeNode) {
		if (treeNode != null) {
			for (int i = 0; i < treeNode.getChildCount(); i++) {
				TreeNode subTree = treeNode.setChildOpen(i, true);
				if ((subTree != null) && (subTree.getChildCount() > 0)) {
					openAllNodes(subTree);
				}
			}
		}
		
	}
	
	
	/**
	 * The Class NodeCell.
	 */
	public class NodeCell extends AbstractCell<CQLCellTreeNode> {
		/**
		 * Instantiates a new node cell.
		 */
		public NodeCell() {
			super(BrowserEvents.CLICK, BrowserEvents.FOCUS, BrowserEvents.CONTEXTMENU);
		}
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.AbstractCell#render(com.google.gwt.cell.client.Cell.Context, java.lang.Object, com.google.gwt.safehtml.shared.SafeHtmlBuilder)
		 */
		@Override
		public void render(Context context, CQLCellTreeNode cellTreeNode, SafeHtmlBuilder sb) {
			if (cellTreeNode == null) {
				return;
			}
			//TODO :  We can add classes based on the NodeType with the specified image.
			//The classes will be picked up from Mat.css
			if ((cellTreeNode.getNodeType()
					== CQLCellTreeNode.MASTER_ROOT_NODE)
					|| (cellTreeNode.getNodeType()
							== CQLCellTreeNode.ROOT_NODE)) {
				sb.append(template.outerDiv(getStyleClass(cellTreeNode), UUIDUtilClient.uuid(5).concat("_treeNode_"+cellTreeNode.getLabel()),
						cellTreeNode.getTitle(),
						cellTreeNode.getLabel() != null
						? cellTreeNode.getLabel() : cellTreeNode.getName()));
			} else {
				if((cellTreeNode.getNodeType() == CQLCellTreeNode.LOGICAL_OP_NODE)
						|| (cellTreeNode.getNodeType() == CQLCellTreeNode.SUBTREE_REF_NODE) ){
					boolean foundComment = false;
					@SuppressWarnings("unchecked")
					List<CQLCellTreeNode> childNode = (List<CQLCellTreeNode>) cellTreeNode.
					getExtraInformation(PopulationWorkSpaceConstants.COMMENTS);
					if (childNode != null) {
						for (CQLCellTreeNode treeNode : childNode) {
							if ((treeNode.getNodeText() != null)
									&& (treeNode.getNodeText().length() > 0)
									&& (treeNode.getNodeText().trim() != StringUtils.EMPTY)) {
								sb.append(template.outerDivItemWithSpan(getStyleClass(cellTreeNode),
										UUIDUtilClient.uuid(5).concat("_treeNode_"+cellTreeNode.getLabel()), cellTreeNode.getTitle(),
										cellTreeNode.getLabel() != null
										? cellTreeNode.getLabel() : cellTreeNode.getName()));
								foundComment = true;
								break;
							}
						}
					}
					if(!foundComment) {
						sb.append(template.outerDivItem(getStyleClass(cellTreeNode),
								UUIDUtilClient.uuid(5).concat("_treeNode_"+cellTreeNode.getLabel()), cellTreeNode.getTitle(),
								cellTreeNode.getLabel() != null
								? cellTreeNode.getLabel() : cellTreeNode.getName()));
					}
					
				} else {
					sb.append(template.outerDivItem(getStyleClass(cellTreeNode),UUIDUtilClient.uuid().concat("_treeNode")
							, cellTreeNode.getTitle(),
							cellTreeNode.getLabel() != null
							? cellTreeNode.getLabel() : cellTreeNode.getName()));
				}
			}
			
		}
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.AbstractCell#onBrowserEvent(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element, java.lang.Object, com.google.gwt.dom.client.NativeEvent, com.google.gwt.cell.client.ValueUpdater)
		 */
		@Override
		public void onBrowserEvent(Context context, Element parent, CQLCellTreeNode value,
				NativeEvent event, ValueUpdater<CQLCellTreeNode> valueUpdater) {
			
			super.onBrowserEvent(context, parent, value, event, valueUpdater);
			/*if (event.getType().equals(BrowserEvents.CONTEXTMENU)) {
				event.preventDefault();
				event.stopPropagation();
				
			} else if (event.getType().equals(BrowserEvents.CLICK)
					|| event.getType().equalsIgnoreCase(BrowserEvents.FOCUS)) {
				
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					if ((value.getNodeType() == CQLCellTreeNodeImpl.LOGICAL_OP_NODE)
							|| (value.getNodeType() == CQLCellTreeNodeImpl.SUBTREE_REF_NODE)) {
						
						setDirty(true);
					} else {
						
					}
				} else {
					if ((value.getNodeType() == CQLCellTreeNodeImpl.LOGICAL_OP_NODE)
							|| (value.getNodeType() == CQLCellTreeNodeImpl.SUBTREE_REF_NODE)) {
					
					}
				}
			}  else {
				
				super.onBrowserEvent(context, parent, value, event, valueUpdater);
			}*/
		}
	}
	/**
	 * Gets the style class.
	 * @param cellTreeNode
	 *            the cell tree node
	 * @return the style class
	 */
	
	private String getStyleClass(CQLCellTreeNode cellTreeNode) {
		if (cellTreeNode.getValidNode()) {
			switch (cellTreeNode.getNodeType()) {
				case CQLCellTreeNode.ROOT_NODE:
					return "cellTreeRootNode";
				case CQLCellTreeNode.SUBTREE_REF_NODE:
					return "populationWorkSpaceCommentNode";
				default:
					break;
			}
		} else {
			return "clauseWorkSpaceInvalidNode";
		}
		return "";
	}

	@Override
	public CellTree getCellTree() {
		return cellTree;
	}

	@Override
	public void setCellTree(CellTree cellTree) {
		this.cellTree = cellTree;
		
	}
	@Override
	public Widget asWidget() {
		return focusPanel;
	}
}
