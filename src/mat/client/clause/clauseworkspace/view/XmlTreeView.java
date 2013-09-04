package mat.client.clause.clauseworkspace.view;

import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.presenter.ClauseConstants;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.MatContext;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;

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
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

public class XmlTreeView extends Composite implements  XmlTreeDisplay, TreeViewModel, KeyDownHandler, FocusHandler{

	interface Template extends SafeHtmlTemplates {
	    @Template("<div class=\"{0}\" title=\"{1}\">{2}</div>")
	    SafeHtml outerDiv(String classes, String title, String content);
	}
	
	private static final Template template = GWT.create(Template.class);
	 
	private FlowPanel  mainPanel = new FlowPanel();
	
	FocusPanel focusPanel = new FocusPanel(mainPanel);

	CellTree cellTree;

	private Button saveBtn = new PrimaryButton("Save","primaryButton");

	private Button buttonExpand = new Button();
	
	private Button buttonCollapse = new Button();

	private ErrorMessageDisplay errorMessageDisplay = new ErrorMessageDisplay();

	private SuccessMessageDisplay successMessageDisplay = new SuccessMessageDisplay();

	private ListDataProvider<CellTreeNode> nodeDataProvider;

	CellTreeNode selectedNode;

	final SingleSelectionModel<CellTreeNode> selectionModel = new SingleSelectionModel<CellTreeNode>();

	CellTreeNode copiedNode;

	PopupPanel popupPanel = new PopupPanel(true);

	ClauseWorkspaceContextMenu clauseWorkspaceContextMenu = new ClauseWorkspaceContextMenu(this, popupPanel);

	boolean enabled;
	
	boolean isDirty = false;
	

	public XmlTreeView(CellTreeNode cellTreeNode) {
		clearMessages();
		createRootNode(cellTreeNode);
		addHandlers();
		mainPanel.getElement().setId("mainPanel_FlowPanel");
	}


	/**
	 * Creates the Root Node in the CellTree.
	 * Sets the Root node to the ListData Provider.
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
		leftPanel.getElement().setId("leftPanel_SimplePanel");
		leftPanel.setStyleName("div-first bottomPadding10px");//left side div which will  have tree

		SimplePanel rightPanel = new SimplePanel();
		rightPanel.getElement().setId("rightPanel_SimplePanel");
		rightPanel.setStyleName("div-second");//right div having tree creation inputs.

		VerticalPanel treePanel =  new VerticalPanel();
		treePanel.getElement().setId("treePanel_VerticalPanel");
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

		treePanel.add(expandCollapse);
		treePanel.add(cellTree);
		leftPanel.add(treePanel);

		SimplePanel bottomSavePanel = new SimplePanel();
		bottomSavePanel.getElement().setId("bottomSavePanel_SimplePanel");
		bottomSavePanel.setStyleName("div-first buttonPadding");
		VerticalPanel savePanel = new VerticalPanel();
		savePanel.getElement().setId("savePanel_VerticalPanel");
		savePanel.add(new SpacerWidget());
//		savePanel.add(errorMessageDisplay);
		savePanel.add(successMessageDisplay);
//		saveBtn.setTitle("Ctrl+Alt+s");
		savePanel.add(saveBtn);

		bottomSavePanel.add(savePanel);

		SimplePanel errPanel = new SimplePanel();
		errPanel.getElement().setId("errPanel_SimplePanel");
		errPanel.add(errorMessageDisplay);
		mainPanel.add(errPanel);
		mainPanel.add(leftPanel);
		mainPanel.add(rightPanel);
		mainPanel.add(bottomSavePanel);	
		focusPanel.addKeyDownHandler(this);
		focusPanel.addFocusHandler(this);
		cellTreeHandlers();
	}

	/**
	 * Selection Handler, Tree Open and Close Handlers Defined
	 */
	private void cellTreeHandlers() {
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				selectedNode = selectionModel.getSelectedObject(); // assigning the selected object to the selectedNode variable.
			}
		});

		/**
		 * This handler is implemented to save the open state of the Celltree in CellTreeNode Object
		 * Set to isOpen boolean in CellTreeNode.
		 * After adding/removing/editing a node to the celltree 
		 * Manually  we have to close and open all nodes to see the new node, 
		 * so using this boolean we will know which node was already in opened state and closed state.
		 */
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
				setOpenToFalse(node);// when a node is closed set all the child nodes isOpen boolean to false.
				node.setOpen(false);
				clearMessages();
			}

		});
	}


	/**
	 * Iterating through all the child nodes and setting the isOpen boolean to false.
	 * @param node
	 */
	private void setOpenToFalse(CellTreeNode node) {
		if(node.hasChildren()){
			for (CellTreeNode child : node.getChilds()) {
				child.setOpen(false);
				setOpenToFalse(child);
			}
		}
	}
	
	/**
	 * Closing all nodes in the CellTree except for the Master Root Node which is the Population node.
	 * This method is called when '-' Collapse All button is clicked on the Screen
	 * @param treeNode
	 * @param fireEvents
	 */
	private void closeNodes(TreeNode node) {
		if(node != null){
			for (int i = 0; i < node.getChildCount(); i++) {
				TreeNode subTree  = null;
				if (((CellTreeNode)node.getChildValue(i)).getNodeType() == CellTreeNode.MASTER_ROOT_NODE){
					subTree =  node.setChildOpen(i, true, true);
				}else{
					subTree =  node.setChildOpen(i, false, true);
				}

				if (subTree != null && ((TreeNode) subTree).getChildCount() > 0){
					closeNodes(subTree);
				}
			}

		}
	}

	/**
	 * This method is called  after removing or editing of the node.
	 * When a node is removed, parent node is closed first and then opened.
	 * Remaining all nodes will be opened or closed based on the isOpen boolean in CellTreeNode
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


	/**
	 * This method is called after adding a child node to the parent.
	 * After adding a child node, close the Parent node and open.
	 * Remaining all nodes will be opened or closed based on the isOpen boolean in CellTreeNode
	 * @param treeNode
	 */
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
	 * Opens all nodes. this is called when '+' Expand All button is clicked on the screen
	 * @param treeNode
	 */
	private void openAllNodes(TreeNode treeNode){		
		if(treeNode != null){
			for (int i = 0; i < treeNode.getChildCount(); i++) {
				TreeNode subTree = treeNode.setChildOpen(i, true);	      			
				if (subTree != null && subTree.getChildCount() > 0){
					openAllNodes(subTree);
				}
			}  
		}
	}

	/**
	 * Expand / Collapse Link - Click Handlers
	 */
	private void addHandlers(){
		
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
		public void render(Context context, CellTreeNode cellTreeNode, SafeHtmlBuilder sb) {
			if (cellTreeNode == null) {
				return;
			}			
			//TODO :  We can add classes based on the NodeType with the specified image. The classes will be picked up from Mat.css
			sb.append(template.outerDiv(getStyleClass(cellTreeNode), cellTreeNode.getTitle(), cellTreeNode.getLabel() != null ? 
					cellTreeNode.getLabel() : cellTreeNode.getName()));
		}

		@Override
		public void onBrowserEvent(Context context, Element parent, CellTreeNode value,
				NativeEvent event, ValueUpdater<CellTreeNode> valueUpdater) {
			if(event.getType().equals(BrowserEvents.CONTEXTMENU)){
				event.preventDefault();
				event.stopPropagation();
				if(MatContext.get().getMeasureLockService().checkForEditPermission()){
					onRightClick(value, (Event)event, parent);
				}					
			}else{
				super.onBrowserEvent(context, parent, value, event, valueUpdater);
			}

		}
	} 

	
	private String getStyleClass(CellTreeNode cellTreeNode){
		switch (cellTreeNode.getNodeType()) {
		case CellTreeNode.ROOT_NODE:
			return "cellTreeRootNode";
		default:
			break;
		}
		return "";
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
		clearMessages();
		selectedNode = value;
		selectionModel.setSelected(selectedNode, true);
		int x = element.getAbsoluteRight() - 10;
		int y = element.getAbsoluteBottom() + 5;
		popupPanel.setPopupPosition(x, y);
		popupPanel.setAnimationEnabled(true);
//		popupPanel.setSize("175px", "75px");
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
		return focusPanel;
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
			this.copiedNode = pasteNode;
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



	@Override
	public void editNode(String name, String label) {
		if(selectedNode != null){
			selectedNode.setName(name);
			selectedNode.setLabel(label);
			closeParentOpenNodes(cellTree.getRootTreeNode());
		}
	}


	/**
	 * @return the clauseWorkspaceContextMenu
	 */
	public ClauseWorkspaceContextMenu getClauseWorkspaceContextMenu() {
		return clauseWorkspaceContextMenu;
	}


	/**
	 * @param clauseWorkspaceContextMenu the clauseWorkspaceContextMenu to set
	 */
	public void setClauseWorkspaceContextMenu(
			ClauseWorkspaceContextMenu clauseWorkspaceContextMenu) {
		this.clauseWorkspaceContextMenu = clauseWorkspaceContextMenu;
	}


	@Override
	public void onKeyDown(KeyDownEvent event) {
//		System.out.println(event.getNativeKeyCode());
		int keyCode = event.getNativeKeyCode();
		if(selectedNode != null){
			short nodeType = selectedNode.getNodeType();
			if(event.isControlKeyDown()){
				if(keyCode == ClauseConstants.COPY_C){//COPY 
					if(nodeType != CellTreeNode.MASTER_ROOT_NODE && nodeType != CellTreeNode.ROOT_NODE){
						popupPanel.hide();
						copy();	
					}
					
				}else if(keyCode == ClauseConstants.PASTE_V){//PASTE
					boolean canPaste = false;
					popupPanel.hide();
					if(copiedNode != null){
						switch (selectedNode.getNodeType()) {
						case CellTreeNode.ROOT_NODE:
							if(selectedNode.equals(copiedNode.getParent())){
								clauseWorkspaceContextMenu.pasteRootNodeTypeItem();
								isDirty = true;
							}
							break;
						case CellTreeNode.LOGICAL_OP_NODE: case CellTreeNode.FUNCTIONS_NODE:
							if(copiedNode.getNodeType() != CellTreeNode.CLAUSE_NODE){
								canPaste = true;
							}
							break;
						case CellTreeNode.TIMING_NODE:
							if(copiedNode.getNodeType() != CellTreeNode.CLAUSE_NODE
									&& (selectedNode.getChilds() == null || selectedNode.getChilds().size() < 2)){
								canPaste = true;
							}
							break;
						default:
							break;
						}
						if(canPaste){
							paste();
							isDirty = true;
						}
					}
					
				}else if(keyCode == ClauseConstants.CUT_X){//CUT
					popupPanel.hide();
					if(selectedNode.getNodeType() != CellTreeNode.MASTER_ROOT_NODE
								&& selectedNode.getNodeType() != CellTreeNode.CLAUSE_NODE 
								&& selectedNode.getNodeType() != CellTreeNode.ROOT_NODE 
								&& selectedNode.getParent().getNodeType() != CellTreeNode.CLAUSE_NODE){
						copy();
						removeNode();
						isDirty = true;
					}
				}
			}else if(keyCode == ClauseConstants.DELETE_DELETE){//DELETE
				popupPanel.hide();
				if((selectedNode.getNodeType() != CellTreeNode.MASTER_ROOT_NODE
						&& selectedNode.getNodeType() != CellTreeNode.ROOT_NODE 
						&& selectedNode.getParent().getNodeType() != CellTreeNode.CLAUSE_NODE
						&& selectedNode.getNodeType() != CellTreeNode.CLAUSE_NODE)
						|| (selectedNode.getNodeType() == CellTreeNode.CLAUSE_NODE && selectedNode.getParent().getChilds().size() > 1 )){
					removeNode();
					isDirty = true;
				}
			}
		}
		if((event.isShiftKeyDown() && (keyCode == ClauseConstants.PLUS_FF || keyCode == ClauseConstants.PLUS_IE))){
			//EXPAND/COLLAPSE (+(Shift +) Expand| - Collapse)
			popupPanel.hide();
			openAllNodes(cellTree.getRootTreeNode());
		}else if((event.isShiftKeyDown() && (keyCode == ClauseConstants.MINUS_FF || keyCode == ClauseConstants.MINUS_IE))){
			popupPanel.hide();
			closeNodes(cellTree.getRootTreeNode());
		}
		/*if(event.isControlKeyDown() && event.isAltKeyDown() && keyCode == 83){
			saveBtn.getElement().focus();
			saveBtn.click();
		}*/
	}


	@Override
	public void setCopiedNode(CellTreeNode cellTreeNode) {
		this.copiedNode = cellTreeNode;
	}

	@Override
	public void onFocus(FocusEvent event) {
		focusPanel.setStyleName("focusPanel");
	}



	@Override
	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}


	@Override
	public boolean isDirty() {
		return this.isDirty;
	}


	@Override
	public void expandSelected(TreeNode treeNode) {
		if(treeNode != null){
			for (int i = 0; i < treeNode.getChildCount(); i++) {
				TreeNode subTree = null;
				if(treeNode.getChildValue(i).equals(selectedNode)){// this check is performed since IE was giving JavaScriptError after removing a node and closing all nodes.
					// to avoid that we are closing the parent of the removed node.
					subTree = treeNode.setChildOpen(i, true, true);
					if (subTree != null && subTree.getChildCount() > 0){
						openAllNodes(subTree);
					}
					break;
				}
				subTree = treeNode.setChildOpen(i, ((CellTreeNode)treeNode.getChildValue(i)).isOpen(), ((CellTreeNode)treeNode.getChildValue(i)).isOpen());
				if (subTree != null && subTree.getChildCount() > 0){
					expandSelected(subTree);
				}
			}  
		}
	}


	@Override
	public CellTreeNode addNode(String name, String label, String uuid,
			short nodeType) {
		CellTreeNode childNode = null;
		if(selectedNode != null &&  name != null && name.trim().length() > 0){//if nodeTex textbox is not empty
			childNode = selectedNode.createChild(name, label, nodeType);
			childNode.setUUID(uuid);
			closeSelectedOpenNodes(cellTree.getRootTreeNode());
			selectionModel.setSelected(selectedNode, true);					
		}
		return childNode;
	}


	@Override
	public void editNode(String name, String label, String uuid) {
		if(selectedNode != null){
			selectedNode.setName(name);
			selectedNode.setLabel(label);
			selectedNode.setUUID(uuid);
			closeParentOpenNodes(cellTree.getRootTreeNode());
		}		
	}

	
	

}
