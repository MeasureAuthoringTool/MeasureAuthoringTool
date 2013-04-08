package mat.client.clause.clauseworkspace.view;

import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

public class XmlTreeView extends Composite implements  XmlTreeDisplay, TreeViewModel{

	private FlowPanel  mainPanel = new FlowPanel();

	CellTree cellTree;

	private Button saveBtn = new Button("Save");

	private Button expand = new Button("+");

	private Button collapse = new Button("-");

	private ErrorMessageDisplay errorMessageDisplay = new ErrorMessageDisplay();

	private SuccessMessageDisplay successMessageDisplay = new SuccessMessageDisplay();

	private ListDataProvider<CellTreeNode> nodeDataProvider;

	CellTreeNode selectedNode;

	final SingleSelectionModel<CellTreeNode> selectionModel = new SingleSelectionModel<CellTreeNode>();

	CellTreeNode copiedNode;

	PopupPanel popupPanel = new PopupPanel(true);

	ClauseWorkspaceContextMenu clauseWorkspaceContextMenu = new ClauseWorkspaceContextMenu(this, popupPanel);

	boolean enabled;

	public XmlTreeView(CellTreeNode cellTreeNode) {
		clearMessages();
		createRootNode(cellTreeNode);
		addHandlers();
	}


	/**
	 * Creates the Root Node in the CellTree.
	 * @param treeModel
	 */
	private void createRootNode(CellTreeNode cellTreeNode) {
		nodeDataProvider = new ListDataProvider<CellTreeNode>(cellTreeNode.getChilds());
	}


	/**
	 * Page widgets
	 * @param cellTree
	 */
	public void createPageView(CellTree cellTree) {
		this.cellTree = cellTree;
		mainPanel.setStyleName("div-wrapper");//main div

		SimplePanel leftPanel = new SimplePanel();
		leftPanel.setStyleName("div-first bottomPadding10px");//left side div which will  have tree

		SimplePanel rightPanel = new SimplePanel();
		rightPanel.setStyleName("div-second");//right div having tree creation inputs.

		VerticalPanel treePanel =  new VerticalPanel();

		FlowPanel expandCollapse  = new FlowPanel();
		expandCollapse.setStyleName("leftAndTopPadding");
		expandCollapse.add(expand);
		expandCollapse.add(collapse);
		expand.setSize("25px", "25px");
		collapse.setSize("25px", "25px");
		collapse.setVisible(false);

		treePanel.add(expandCollapse);
		treePanel.add(cellTree);
		leftPanel.add(treePanel);

		SimplePanel bottomSavePanel = new SimplePanel();
		bottomSavePanel.setStyleName("div-first");
		VerticalPanel savePanel = new VerticalPanel();
		savePanel.add(new SpacerWidget());
		savePanel.add(errorMessageDisplay);
		savePanel.add(successMessageDisplay);
		savePanel.add(saveBtn);

		bottomSavePanel.add(savePanel);

		mainPanel.add(leftPanel);
		mainPanel.add(rightPanel);
		mainPanel.add(bottomSavePanel);	

		cellTreeHandlers();
	}

	/**
	 * Selection Handler, Tree Open and Close Handlers Defined
	 */
	private void cellTreeHandlers() {
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				selectedNode = selectionModel.getSelectedObject();
			}
		});

		this.cellTree.addOpenHandler(new OpenHandler<TreeNode>() {

			@Override
			public void onOpen(OpenEvent<TreeNode> event) {

				CellTreeNode node = (CellTreeNode)event.getTarget().getValue();
				node.setOpen(true);
				clearMessages();
			}
		});

		this.cellTree.addCloseHandler(new CloseHandler<TreeNode>() {
			@Override
			public void onClose(CloseEvent<TreeNode> event) {
				CellTreeNode node = (CellTreeNode)event.getTarget().getValue();
				if(node.hasChildren()){
					for (CellTreeNode child : node.getChilds()) {
						child.setOpen(false);
					}
				}
				node.setOpen(false);
				clearMessages();
			}
		});
	}


	/**
	 * Closing all nodes in the CellTree
	 * @param treeNode
	 * @param fireEvents
	 */
	private void closeNodes(TreeNode treeNode, boolean fireEvents) {
		if(treeNode != null){
			for (int i = 0; i < treeNode.getChildCount(); i++) {
				TreeNode subTree = treeNode.setChildOpen(i, false, fireEvents);  
				if (subTree != null && subTree.getChildCount() > 0){
					closeNodes(subTree, fireEvents);
				}
			}  
		}
	}

	/**
	 * this method is called only after removing the node.
	 * @param treeNode
	 */
	private void closeParentOpenNodes(TreeNode treeNode) {
		if(treeNode != null){
			for (int i = 0; i < treeNode.getChildCount(); i++) {
				TreeNode subTree = null;
				if(treeNode.getChildValue(i).equals(selectedNode.getParent())){// this check is performed since IE was giving JavaScriptError after removing a node and closing all nodes.
					// to avoid that we are closing the parent of the removed node.
					subTree = treeNode.setChildOpen(i, false, false);
				}
				subTree = treeNode.setChildOpen(i, ((CellTreeNode)treeNode.getChildValue(i)).isOpen());
				if (subTree != null && subTree.getChildCount() > 0){
					closeParentOpenNodes(subTree);
				}
			}  
		}
	}


	private void closeSelectedOpenNodes(TreeNode treeNode) {
		if(treeNode != null){
			for (int i = 0; i < treeNode.getChildCount(); i++) {
				TreeNode subTree = null;
				if(treeNode.getChildValue(i).equals(selectedNode)){// this check is performed since IE was giving JavaScriptError after removing a node and closing all nodes.
					// to avoid that we are closing the parent of the removed node.
					subTree = treeNode.setChildOpen(i, false, false);
				}
				subTree = treeNode.setChildOpen(i, ((CellTreeNode)treeNode.getChildValue(i)).isOpen());
				if (subTree != null && subTree.getChildCount() > 0){
					closeSelectedOpenNodes(subTree);
				}
			}  
		}
	}

	/**
	 * Opens all nodes.
	 * @param treeNode
	 */
	private void openAllNodes(TreeNode treeNode){		
		if(treeNode != null){
			getNodeInfo(treeNode.getValue());
			for (int i = 0; i < treeNode.getChildCount(); i++) {
				TreeNode subTree = treeNode.setChildOpen(i, true);	      			
				if (subTree != null && subTree.getChildCount() > 0){
					openAllNodes(subTree);
				}
			}  
		}
	}

	/**
	 * Click Handlers
	 */
	private void addHandlers(){

		expand.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				openAllNodes(cellTree.getRootTreeNode());
				expand.setVisible(false);
				collapse.setVisible(true);
			}
		});

		collapse.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				closeNodes(cellTree.getRootTreeNode(), true);
				expand.setVisible(true);
				collapse.setVisible(false);
			}
		});
	}


	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {

		if (value == null) { 
			NodeCell nodeCell = new NodeCell();    
			return new DefaultNodeInfo<CellTreeNode>(nodeDataProvider, nodeCell, selectionModel, null);
		} else {
			CellTreeNode myValue = (CellTreeNode) value;
			ListDataProvider<CellTreeNode> dataProvider = new ListDataProvider<CellTreeNode>(myValue.getChilds());
			NodeCell nodeCell = new NodeCell();
			return new DefaultNodeInfo<CellTreeNode>(dataProvider, nodeCell, selectionModel, null);
		}
	}

	@Override
	public boolean isLeaf(Object value) {
		if (value instanceof CellTreeNode) {
			CellTreeNode t = (CellTreeNode) value;
			if (!t.hasChildren()){
				return true;
			}
		}
		return false;
	}


	public class NodeCell extends AbstractCell<CellTreeNode> {

		public NodeCell() {
			super(BrowserEvents.CONTEXTMENU);
		}
		@Override
		public void render(Context context, CellTreeNode value, SafeHtmlBuilder sb) {
			if (value == null) {
				return;
			}
			sb.appendEscaped(value.getLabel() != null ? value.getLabel() : value.getName());
		}

		@Override
		public void onBrowserEvent(Context context, Element parent, CellTreeNode value,
				NativeEvent event, ValueUpdater<CellTreeNode> valueUpdater) {
			if(event.getType().equals(BrowserEvents.CONTEXTMENU)){
				event.preventDefault();
				event.stopPropagation();
				onRightClick(value, (Event)event, parent);
			}else{
				super.onBrowserEvent(context, parent, value, event, valueUpdater);
			}

		}
	} 


	@Override
	public CellTreeNode addNode(String value, String label, short nodeType) {
		CellTreeNode childNode = null;
		if(selectedNode != null &&  value != null && value.trim().length() > 0){//if nodeTex textbox is not empty
			childNode = selectedNode.createChild(value, label, nodeType);
		closeSelectedOpenNodes(cellTree.getRootTreeNode());
		selectionModel.setSelected(selectedNode, true);					
		}
		return childNode;
	}

	@Override
	public void refreshCellTreeAfterAdding(CellTreeNode selectedNode){
		closeSelectedOpenNodes(cellTree.getRootTreeNode());
		selectionModel.setSelected(selectedNode, true);			
	}



	@Override
	public void removeNode() {
		if(selectedNode != null){
			CellTreeNode parent = selectedNode.getParent();
			parent.removeChild(selectedNode);
			closeParentOpenNodes(cellTree.getRootTreeNode());
			selectionModel.setSelected(parent, true);
		}
	}



	public void onRightClick(CellTreeNode value, Event event, Element element) {
		selectedNode = value;
		selectionModel.setSelected(selectedNode, true);
		int x = element.getAbsoluteRight() - 10;
		int y = element.getAbsoluteBottom() + 5;
		popupPanel.setPopupPosition(x, y);
		popupPanel.setAnimationEnabled(true);
		popupPanel.show();
		popupPanel.setStyleName("popup");
		clauseWorkspaceContextMenu.displayMenuItems(popupPanel);
	}



	@Override
	public CellTree getXmlTree() {
		return cellTree;
	}


	@Override
	public Button getSaveButton() {
		return saveBtn;
	}


	@Override
	public Widget asWidget() {
		return mainPanel;
	}


	@Override
	public SuccessMessageDisplay getSuccessMessageDisplay() {
		return successMessageDisplay;
	}


	@Override
	public ErrorMessageDisplay getErrorMessageDisplay() {
		return errorMessageDisplay;
	}


	@Override
	public void clearMessages() {
		successMessageDisplay.clear();
		errorMessageDisplay.clear();

	}


	@Override
	public void setEnabled(boolean enabled) {
		saveBtn.setEnabled(enabled);
		this.enabled = enabled;
	}


	@Override
	public CellTreeNode getSelectedNode() {
		return this.selectedNode;
	}


	@Override
	public void copy() {
		this.copiedNode = selectedNode;		
	}


	@Override
	public void paste() {
		if(selectedNode != null){
			CellTreeNode pasteNode = copiedNode.cloneNode();
			selectedNode.appendChild(pasteNode);
			closeSelectedOpenNodes(cellTree.getRootTreeNode());
			selectionModel.setSelected(selectedNode, true);		
		}
	}


	@Override
	public CellTreeNode getCopiedNode() {
		return this.copiedNode;
	}


	public CellTree getCellTree() {
		return cellTree;
	}


	public void setCellTree(CellTree cellTree) {
		this.cellTree = cellTree;
	}




}
