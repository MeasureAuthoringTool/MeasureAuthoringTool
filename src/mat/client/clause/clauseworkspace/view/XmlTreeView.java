package mat.client.clause.clauseworkspace.view;

import java.util.List;
import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.model.CellTreeNodeImpl;
import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatContext;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SecondaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.WarningMessageDisplay;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.client.SafeHtmlTemplates.Template;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

/**
 * The Class XmlTreeView.
 */
public class XmlTreeView extends Composite implements  XmlTreeDisplay, TreeViewModel, KeyDownHandler, FocusHandler {
	/**
	 * Comment Area Max Length - Population Work Space.
	 */
	private static final int COMMENT_MAX_LENGTH = 250;
	/**
	 * The Interface Template.
	 */
	interface Template extends SafeHtmlTemplates {
		
		/**
		 * Outer div.
		 * 
		 * @param classes
		 *            the classes
		 * @param title
		 *            the title
		 * @param content
		 *            the content
		 * @return the safe html
		 */
		@Template("<div class=\"{0}\" title=\"{1}\" aria-role=\"tree\">{2}</div>")
		SafeHtml outerDiv(String classes, String title, String content);
		/**
		 * Outer div for Tree Item.
		 * @param classes
		 *            the classes
		 * @param title
		 *            the title
		 * @param content
		 *            the content
		 * @return the safe html
		 */
		@Template("<div class=\"{0}\" title=\"{1}\" aria-role=\"treeitem\">{2}</div>")
		SafeHtml outerDivItem(String classes, String title, String content);
	}
	
	/** The Constant template. */
	private static final Template template = GWT.create(Template.class);
	
	/** The main panel. */
	private FlowPanel  mainPanel = new FlowPanel();
	
	/** The is valid. */
	private boolean isValid = true;
	
	/**
	 * Comment Area Remaining Character Label - Population Work Space.
	 */
	private Label remainingCharsLabel = new Label("250");
	
	/** The focus panel. */
	private FocusPanel focusPanel = new FocusPanel(mainPanel);
	
	/** The cell tree. */
	private CellTree cellTree;
	
	/** The save btn. */
	private Button saveBtn = new PrimaryButton("Save", "primaryButton");
	
	/** The validate btn. */
	//Commented Validate Button from Population Work Space as part of Mat-3162
	//	private Button validateBtn = new SecondaryButton("Validate");
	
	/** The save btn. */
	private Button saveBtnClauseWorkSpace = new PrimaryButton("Save", "primaryButton");
	
	/** The validate btn. */
	private Button validateBtnClauseWorkSpace = new SecondaryButton("Validate");
	
	/** The Clear btn. */
	private Button clearClauseWorkSpace = new SecondaryButton("Clear");
	
	/**
	 * Comment Ok Button.
	 */
	private Button commentButtons = new Button("OK");
	/**
	 * Comment Input Text area.
	 */
	private CommentAreaTextBox commentArea = new CommentAreaTextBox(COMMENT_MAX_LENGTH);
	
	/** The button expand. */
	private Button buttonExpand = new Button();
	
	/** The button collapse. */
	private Button buttonCollapse = new Button();
	
	/** The button expand. */
	private Button buttonExpandClauseWorkSpace = new Button();
	
	/** The button collapse. */
	private Button buttonCollapseClauseWorkSpace = new Button();
	
	/** The error message display. */
	private ErrorMessageDisplay errorMessageDisplay = new ErrorMessageDisplay();
	
	/** The success message display. */
	private SuccessMessageDisplay successMessageDisplay = new SuccessMessageDisplay();
	
	/** The success message display. */
	private SuccessMessageDisplay successMessageAddCommentDisplay = new SuccessMessageDisplay();
	
	/**
	 * clear Error Display.
	 */
	private ErrorMessageDisplay clearErrorDisplay = new ErrorMessageDisplay();
	
	/** The warning message display. */
	private WarningMessageDisplay warningMessageDisplay = new WarningMessageDisplay();
	
	/** The node data provider. */
	private ListDataProvider<CellTreeNode> nodeDataProvider;
	
	/** The selected node. */
	private CellTreeNode selectedNode;
	
	/** The selection model. */
	private final SingleSelectionModel<CellTreeNode> selectionModel = new SingleSelectionModel<CellTreeNode>();
	
	/** The copied node. */
	private CellTreeNode copiedNode;
	
	/** The popup panel. */
	private PopupPanel popupPanel;
	
	/** The clause workspace context menu. */
	private ClauseWorkspaceContextMenu clauseWorkspaceContextMenu;
	
	/** The is dirty. */
	private boolean isDirty = false;
	/**
	 * Instantiates a new xml tree view.
	 * 
	 * @param cellTreeNode
	 *            the cell tree node
	 */
	public XmlTreeView(CellTreeNode cellTreeNode ) {
		clearMessages();
		if (cellTreeNode != null) {
			createRootNode(cellTreeNode);
			addHandlers();
		}
		mainPanel.getElement().setId("mainPanel_FlowPanel");
		saveBtn.getElement().setId("saveBtn_Button");
		buttonExpand.getElement().setId("buttonExpand_Button");
		buttonCollapse.getElement().setId("buttonCollapse_Button");
	}
	
	/**
	 * Creates the Root Node in the CellTree. Sets the Root node to the ListData
	 * Provider.
	 * 
	 * @param cellTreeNode
	 *            the cell tree node
	 */
	
	private void createRootNode(CellTreeNode cellTreeNode) {
		if ((cellTreeNode.getChilds() != null) && (cellTreeNode.getChilds().size() > 0)) {
			nodeDataProvider = new ListDataProvider<CellTreeNode>(cellTreeNode.getChilds());
		}
	}
	
	
	/**
	 * Page widgets.
	 * 
	 * @param cellTree
	 *            the cell tree
	 */
	public void createPageView(CellTree cellTree) {
		this.cellTree = cellTree;
		mainPanel.setStyleName("div-wrapper"); //main div
		SimplePanel leftPanel = new SimplePanel();
		leftPanel.getElement().setId("leftPanel_SimplePanel");
		leftPanel.setStyleName("div-first bottomPadding10px"); //left side div which will  have tree
		SimplePanel rightPanel = new SimplePanel();
		rightPanel.getElement().setId("rightPanel_SimplePanel");
		rightPanel.setStyleName("div-second-comment-box"); //right div having tree creation inputs.
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
		cellTreeHandlers();
		leftPanel.add(treePanel);
		SimplePanel bottomSavePanel = new SimplePanel();
		bottomSavePanel.getElement().setId("bottomSavePanel_SimplePanel");
		bottomSavePanel.setStyleName("div-first buttonPadding");
		VerticalPanel vp = new VerticalPanel();
		HorizontalPanel savePanel = new HorizontalPanel();
		savePanel.getElement().setId("savePanel_VerticalPanel");
		savePanel.add(new SpacerWidget());
		vp.add(successMessageDisplay);
		savePanel.add(saveBtn);
		//Commented Validate Button from Population Work Space as part of Mat-3162
		//validateBtn.setTitle("Validate");
		//savePanel.add(validateBtn);
		//vp.add(warningMessageDisplay);
		vp.add(savePanel);
		bottomSavePanel.add(vp);
		SimplePanel errPanel = new SimplePanel();
		errPanel.getElement().setId("errPanel_SimplePanel");
		errPanel.add(errorMessageDisplay);
		VerticalPanel addCommentPanel = addCommentPanelToRightPanel();
		rightPanel.add(addCommentPanel);
		mainPanel.add(errPanel);
		mainPanel.add(leftPanel);
		mainPanel.add(rightPanel);
		mainPanel.add(bottomSavePanel);
		focusPanel.addKeyDownHandler(this);
		focusPanel.addFocusHandler(this);
	}
	
	/**
	 * Comment Panel for Population Work Space.
	 * @return VerticalPanel.
	 */
	private VerticalPanel addCommentPanelToRightPanel() {
		VerticalPanel addCommentPanel = new VerticalPanel();
		addCommentPanel.getElement().setId("addCommentPanel_VPanel");
		addCommentPanel.add(new SpacerWidget());
		Label addComment = new Label();
		addCommentPanel.add(LabelBuilder.buildLabel(addComment, "Add/Edit Comment"));
		commentArea.getElement().setAttribute("id", "addComment_TextArea");
		addCommentPanel.add(new SpacerWidget());
		addCommentPanel.add(commentArea);
		HorizontalPanel remainCharsPanel = new HorizontalPanel();
		remainCharsPanel.add(new HTML("Remaining &nbsp;"));
		
		remainCharsPanel.add(remainingCharsLabel);
		remainCharsPanel.add(new HTML("&nbsp;characters."));
		addCommentPanel.add(remainCharsPanel);
		
		HorizontalPanel buttonAndMessagePanel = new HorizontalPanel();
		buttonAndMessagePanel.getElement().setId("buttonAndMessage_hPanel");
		commentButtons.setTitle("OK");
		
		commentButtons.getElement().setId("addCommentOk_Button");
		buttonAndMessagePanel.add(commentButtons);
		buttonAndMessagePanel.add(successMessageAddCommentDisplay);
		addCommentPanel.add(new SpacerWidget());
		addCommentPanel.add(buttonAndMessagePanel);
		commentArea.getElement().setAttribute("maxlength", "250");
		commentArea.setText("");
		commentArea.setHeight("80px");
		commentArea.setWidth("250px");
		addCommentPanel.addStyleName("addCommentPanel");
		setCommentsBoxReadOnly(true);
		return addCommentPanel;
	}
	
	/**
	 * Page View for Clause WorkSpace.
	 * @param cellTree
	 */
	public void createClauseWorkSpacePageView(CellTree cellTree) {
		this.cellTree = cellTree;
		mainPanel.clear();
		mainPanel.setStyleName("div-wrapper"); //main div
		SimplePanel leftPanel = new SimplePanel();
		leftPanel.getElement().setId("leftPanel_SimplePanelCW");
		leftPanel.setStyleName("div-first bottomPadding10px"); //left side div which will  have tree
		SimplePanel rightPanel = new SimplePanel();
		rightPanel.getElement().setId("rightPanel_SimplePanelCW");
		rightPanel.setStyleName("div-second"); //right div having tree creation inputs.
		VerticalPanel treePanel =  new VerticalPanel();
		treePanel.getElement().setId("treePanel_VerticalPanelCW");
		HorizontalPanel expandCollapse  = new HorizontalPanel();
		expandCollapse.getElement().setId("expandCollapse_HorizontalPanelCW");
		expandCollapse.setStyleName("leftAndTopPadding");
		expandCollapse.setSize("100px", "20px");
		buttonExpandClauseWorkSpace.setStylePrimaryName("expandAllButton");
		buttonCollapseClauseWorkSpace.setStylePrimaryName("collapseAllButton");
		buttonExpandClauseWorkSpace.setTitle("Expand All (Shift +)");
		buttonCollapseClauseWorkSpace.setTitle("Collapse All (Shift -)");
		expandCollapse.add(buttonExpandClauseWorkSpace);
		expandCollapse.add(buttonCollapseClauseWorkSpace);
		buttonExpandClauseWorkSpace.setFocus(true);
		buttonCollapseClauseWorkSpace.setVisible(true);
		if (cellTree != null) {
			treePanel.add(expandCollapse);
			treePanel.add(cellTree);
			addCWExpandCollapseButtonHandler();
			cellTreeHandlers();
		} else {
			treePanel.setHeight("100%");
		}
		leftPanel.add(treePanel);
		SimplePanel bottomSavePanel = new SimplePanel();
		bottomSavePanel.getElement().setId("bottomSavePanel_SimplePanelCW");
		bottomSavePanel.setStyleName("div-first buttonPadding");
		VerticalPanel vp = new VerticalPanel();
		HorizontalPanel savePanel = new HorizontalPanel();
		savePanel.getElement().setId("savePanel_VerticalPanelCW");
		savePanel.add(new SpacerWidget());
		//		savePanel.add(errorMessageDisplay);
		vp.add(successMessageDisplay);
		//		saveBtn.setTitle("Ctrl+Alt+s");
		savePanel.add(saveBtnClauseWorkSpace);
		validateBtnClauseWorkSpace.setTitle("Validate");
		clearClauseWorkSpace.setTitle("Clear Clause WorkSpace");
		savePanel.add(validateBtnClauseWorkSpace);
		savePanel.add(clearClauseWorkSpace);
		vp.add(warningMessageDisplay);
		vp.add(savePanel);
		bottomSavePanel.add(vp);
		SimplePanel errPanel = new SimplePanel();
		errPanel.getElement().setId("errPanel_SimplePanelCW");
		errPanel.add(errorMessageDisplay);
		mainPanel.add(errPanel);
		mainPanel.add(leftPanel);
		mainPanel.add(rightPanel);
		mainPanel.add(bottomSavePanel);
		focusPanel.addKeyDownHandler(this);
		focusPanel.addFocusHandler(this);
	}
	
	
	/**
	 * Checks max limit of character count on comment Area and display's remaining character count.
	 * @param remainingCharsLbel - Label.
	 */
	private void onTextAreaContentChanged(final Label remainingCharsLbel) {
		int counter = new Integer(commentArea.getText().length()).intValue();
		int charsRemaining = COMMENT_MAX_LENGTH - counter;
		remainingCharsLbel.setText("" + charsRemaining);
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
				CellTreeNode node = (CellTreeNode) event.getTarget().getValue();
				node.setOpen(true);
				clearMessages();
			}
		});
		cellTree.addCloseHandler(new CloseHandler<TreeNode>() {
			@Override
			public void onClose(CloseEvent<TreeNode> event) {
				CellTreeNode node = (CellTreeNode) event.getTarget().getValue();
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
	private void setOpenToFalse(CellTreeNode node) {
		if (node.hasChildren()) {
			for (CellTreeNode child : node.getChilds()) {
				child.setOpen(false);
				setOpenToFalse(child);
			}
		}
	}
	/**
	 * Closing all nodes in the CellTree except for the Master Root Node which
	 * is the Population node. This method is called when '-' Collapse All
	 * button is clicked on the Screen
	 * 
	 * @param node
	 *            the node
	 */
	@Override
	public void closeNodes(TreeNode node) {
		if (node != null) {
			for (int i = 0; i < node.getChildCount(); i++) {
				TreeNode subTree  = null;
				if (((CellTreeNode) node.getChildValue(i)).getNodeType() == CellTreeNode.MASTER_ROOT_NODE) {
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
	
	/**
	 * This method is called after removing or editing of the node. When a node
	 * is removed, parent node is closed first and then opened. Remaining all
	 * nodes will be opened or closed based on the isOpen boolean in
	 * CellTreeNode
	 * 
	 * @param treeNode
	 *            the tree node
	 */
	private void closeParentOpenNodes(TreeNode treeNode) {
		if (treeNode != null) {
			for (int i = 0; i < treeNode.getChildCount(); i++) {
				TreeNode subTree = null;
				if (treeNode.getChildValue(i).equals(selectedNode.getParent())) {
					// this check is performed since IE was giving JavaScriptError
					//after removing a node and closing all nodes.
					// to avoid that we are closing the parent of the removed node.
					subTree = treeNode.setChildOpen(i, false, false);
				}
				subTree = treeNode.setChildOpen(i, ((CellTreeNode) treeNode.getChildValue(i)).isOpen());
				if ((subTree != null) && (subTree.getChildCount() > 0)) {
					closeParentOpenNodes(subTree);
				}
			}
		}
	}
	
	
	/**
	 * This method is called after adding a child node to the parent. After
	 * adding a child node, close the Parent node and open. Remaining all nodes
	 * will be opened or closed based on the isOpen boolean in CellTreeNode
	 * 
	 * @param treeNode
	 *            the tree node
	 */
	private void closeSelectedOpenNodes(TreeNode treeNode) {
		if (treeNode != null) {
			for (int i = 0; i < treeNode.getChildCount(); i++) {
				TreeNode subTree = null;
				if (treeNode.getChildValue(i).equals(selectedNode)) { // this check is performed since IE
					//was giving JavaScriptError after removing a node and closing all nodes.
					// to avoid that we are closing the parent of the removed node.
					subTree = treeNode.setChildOpen(i, false, false);
				}
				subTree = treeNode.setChildOpen(i, ((CellTreeNode) treeNode.getChildValue(i)).isOpen());
				if ((subTree != null) && (subTree.getChildCount() > 0)) {
					closeSelectedOpenNodes(subTree);
				}
			}
		}
	}
	
	/**
	 * Opens all nodes. this is called when '+' Expand All button is clicked on
	 * the screen
	 * 
	 * @param treeNode
	 *            the tree node
	 */
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
	/**
	 * Expand/Collapse button Handle's for Clause Work Space buttons.
	 */
	private void addCWExpandCollapseButtonHandler() {
		buttonExpandClauseWorkSpace.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				openAllNodes(cellTree.getRootTreeNode());
				buttonExpand.setVisible(true);
				buttonCollapse.setVisible(true);
			}
		});
		
		buttonCollapseClauseWorkSpace.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				closeNodes(cellTree.getRootTreeNode());
				buttonExpand.setVisible(true);
				buttonCollapse.setVisible(true);
			}
		});
	}
	
	
	/* (non-Javadoc)
	 * @see com.google.gwt.view.client.TreeViewModel#getNodeInfo(java.lang.Object)
	 */
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
	
	/* (non-Javadoc)
	 * @see com.google.gwt.view.client.TreeViewModel#isLeaf(java.lang.Object)
	 */
	@Override
	public boolean isLeaf(Object value) {
		if (value instanceof CellTreeNode) {
			CellTreeNode t = (CellTreeNode) value;
			if (!t.hasChildren()) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Comment Area Text class. This is written to add remaining Character's functionality.
	 *
	 */
	public class CommentAreaTextBox extends TextArea {
		/**
		 * property for holding maximum length.
		 */
		private int maxLength;
		/**
		 * Constructor
		 * @param maxLen
		 */
		public CommentAreaTextBox(int maxLen) {
			
			super(Document.get().createTextAreaElement());
			maxLength = maxLen;
			setStyleName("gwt-TextArea");
			sinkEvents(Event.ONPASTE | Event.ONKEYDOWN | Event.ONKEYPRESS);
			
			CommentAreaTextBox.this
			.addValueChangeHandler(new ValueChangeHandler<String>() {
				
				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					
					String commentAreaUpdatedText;
					CommentAreaTextBox.this.setText(event.getValue());
					
					try {
						commentAreaUpdatedText = CommentAreaTextBox.this.getText();
					} catch (Exception e) {
						commentAreaUpdatedText = "";
					}
					if (commentAreaUpdatedText.length() >= maxLength) {
						String subStringText = commentAreaUpdatedText.substring(0,
								maxLength);
						CommentAreaTextBox.this.setValue(subStringText);
					} else {
						CommentAreaTextBox.this.setValue(commentAreaUpdatedText);
					}
					
					onTextAreaContentChanged(remainingCharsLabel);
					
				}
			});
			
		}
		
		/**
		 * Description: Takes the browser event.
		 *
		 * @param event
		 *            declared.
		 */
		@Override
		public void onBrowserEvent(Event event) {
			String commentAreaContent;
			try {
				commentAreaContent = CommentAreaTextBox.this.getText();
			} catch (Exception e) {
				commentAreaContent = "";
			}
			// Checking for paste event
			if (event.getTypeInt() == Event.ONPASTE) {
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					@Override
					public void execute() {
						ValueChangeEvent.fire(CommentAreaTextBox.this,
								CommentAreaTextBox.this.getText());
					}
				});
				return;
			}
			// Checking for keyUp event.
			if ((event.getTypeInt() == Event.ONKEYDOWN)
					&& (commentAreaContent.length() > maxLength)
					&& (event.getKeyCode() != KeyCodes.KEY_LEFT)
					&& (event.getKeyCode() != KeyCodes.KEY_TAB)
					&& (event.getKeyCode() != KeyCodes.KEY_RIGHT)
					&& (event.getKeyCode() != KeyCodes.KEY_DELETE)
					&& (event.getKeyCode() != KeyCodes.KEY_BACKSPACE)
					&& (event.getKeyCode() != KeyCodes.KEY_SHIFT)
					&& (event.getKeyCode() != KeyCodes.KEY_CTRL)) {
				event.preventDefault();
			} else if ((event.getTypeInt() == Event.ONKEYDOWN)
					&& (commentAreaContent.length() <= maxLength)) {
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					@Override
					public void execute() {
						ValueChangeEvent.fire(CommentAreaTextBox.this,
								CommentAreaTextBox.this.getText());
					}
				});
			}
		}
		
		/**
		 * Getter for maximum length.
		 * @return - int.
		 */
		public int getMaxLength() {
			return maxLength;
		}
		
		/**
		 * Setter for maximum length.
		 * @param maxLength
		 */
		public void setMaxLength(int maxLength) {
			this.maxLength = maxLength;
		}
	}
	/**
	 * The Class NodeCell.
	 */
	public class NodeCell extends AbstractCell<CellTreeNode> {
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
		public void render(Context context, CellTreeNode cellTreeNode, SafeHtmlBuilder sb) {
			if (cellTreeNode == null) {
				return;
			}
			//TODO :  We can add classes based on the NodeType with the specified image.
			//The classes will be picked up from Mat.css
			if ((cellTreeNode.getNodeType()
					== CellTreeNode.MASTER_ROOT_NODE)
					|| (cellTreeNode.getNodeType()
							== CellTreeNode.ROOT_NODE)) {
				sb.append(template.outerDiv(getStyleClass(cellTreeNode), cellTreeNode.getTitle(),
						cellTreeNode.getLabel() != null
						? cellTreeNode.getLabel() : cellTreeNode.getName()));
			} else {
				sb.append(template.outerDivItem(getStyleClass(cellTreeNode), cellTreeNode.getTitle(),
						cellTreeNode.getLabel() != null
						? cellTreeNode.getLabel() : cellTreeNode.getName()));
			}
		}
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.AbstractCell#onBrowserEvent(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element, java.lang.Object, com.google.gwt.dom.client.NativeEvent, com.google.gwt.cell.client.ValueUpdater)
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void onBrowserEvent(Context context, Element parent, CellTreeNode value,
				NativeEvent event, ValueUpdater<CellTreeNode> valueUpdater) {
			if (event.getType().equals(BrowserEvents.CONTEXTMENU)) {
				successMessageAddCommentDisplay.removeStyleName("successMessageCommentPanel");
				successMessageAddCommentDisplay.clear();
				event.preventDefault();
				event.stopPropagation();
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					onRightClick(value, (Event) event, parent);
				}
			} else if (event.getType().equals(BrowserEvents.CLICK)
					|| event.getType().equalsIgnoreCase(BrowserEvents.FOCUS)) {
				successMessageAddCommentDisplay.removeStyleName("successMessageCommentPanel");
				successMessageAddCommentDisplay.clear();
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					if ((value.getNodeType() == CellTreeNodeImpl.LOGICAL_OP_NODE)
							|| (value.getNodeType() == CellTreeNodeImpl.SUBTREE_REF_NODE)) {
						commentArea.setText("");
						List<CellTreeNode> childNode = (List<CellTreeNode>) value.
								getExtraInformation(PopulationWorkSpaceConstants.COMMENTS);
						if (childNode != null) {
							for (CellTreeNode cellTreeNode : childNode) {
								if (cellTreeNode.getNodeType() == cellTreeNode.COMMENT_NODE) {
									commentArea.setText(cellTreeNode.getNodeText());
								}
							}
						}
						onTextAreaContentChanged(remainingCharsLabel);
						setCommentsBoxReadOnly(false);
						setDirty(true);
					} else {
						commentArea.setText("");
						onTextAreaContentChanged(remainingCharsLabel);
						setCommentsBoxReadOnly(true);
					}
				}
			} else {
				super.onBrowserEvent(context, parent, value, event, valueUpdater);
			}
		}
	}
	/**
	 * Gets the style class.
	 * @param cellTreeNode
	 *            the cell tree node
	 * @return the style class
	 */
	private String getStyleClass(CellTreeNode cellTreeNode) {
		if (cellTreeNode.getValidNode()) {
			switch (cellTreeNode.getNodeType()) {
				case CellTreeNode.ROOT_NODE:
					return "cellTreeRootNode";
				default:
					break;
			}
		} else {
			return "clauseWorkSpaceInvalidNode";
		}
		return "";
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#addNode(java.lang.String, java.lang.String, short)
	 */
	@Override
	public CellTreeNode addNode(String value, String label, short nodeType) {
		CellTreeNode childNode = null;
		if ((selectedNode != null) &&  (value != null) && (value.trim().length() > 0)) { //if nodeTex textbox is not empty
			childNode = selectedNode.createChild(value, label, nodeType);
			closeSelectedOpenNodes(cellTree.getRootTreeNode());
			selectionModel.setSelected(selectedNode, true);
		}
		return childNode;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#refreshCellTreeAfterAdding(mat.client.clause.clauseworkspace.model.CellTreeNode)
	 */
	@Override
	public void refreshCellTreeAfterAdding(CellTreeNode selectedNode) {
		closeSelectedOpenNodes(cellTree.getRootTreeNode());
		selectionModel.setSelected(selectedNode, true);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#removeNode()
	 */
	@Override
	public void removeNode() {
		if (selectedNode != null) {
			CellTreeNode parent = selectedNode.getParent();
			parent.removeChild(selectedNode);
			closeParentOpenNodes(cellTree.getRootTreeNode());
			selectionModel.setSelected(parent, true);
			// This is done to invoke focus event on Parent node to show Inline comment in Comment Area.
			((NodeCell) getNodeInfo(parent).getCell()).
			onBrowserEvent(new Context(0, 0, null), null, parent, Document.get().createFocusEvent(), null);
		}
	}
	
	/**
	 * On right click.
	 * 
	 * @param value
	 *            the value
	 * @param event
	 *            the event
	 * @param element
	 *            the element
	 */
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
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#getXmlTree()
	 */
	@Override
	public CellTree getXmlTree() {
		return cellTree;
	}
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#getSaveButton()
	 */
	@Override
	public Button getSaveButton() {
		return saveBtn;
	}
	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.Widget#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return focusPanel;
	}
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#getSuccessMessageDisplay()
	 */
	@Override
	public SuccessMessageDisplay getSuccessMessageDisplay() {
		return successMessageDisplay;
	}
	/**
	 * @return the successMessageAddCommentDisplay
	 */
	@Override
	public SuccessMessageDisplay getSuccessMessageAddCommentDisplay() {
		return successMessageAddCommentDisplay;
	}
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#getErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplay getErrorMessageDisplay() {
		return errorMessageDisplay;
	}
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#clearMessages()
	 */
	@Override
	public void clearMessages() {
		successMessageDisplay.clear();
		errorMessageDisplay.clear();
		warningMessageDisplay.clear();
	}
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		saveBtn.setEnabled(enabled);
	}
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#getSelectedNode()
	 */
	@Override
	public CellTreeNode getSelectedNode() {
		return selectedNode;
	}
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#copy()
	 */
	@Override
	public void copy() {
		copiedNode = selectedNode;
	}
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#paste()
	 */
	@Override
	public void paste() {
		if (selectedNode != null) {
			CellTreeNode pasteNode = copiedNode.cloneNode();
			selectedNode.appendChild(pasteNode);
			closeSelectedOpenNodes(cellTree.getRootTreeNode());
			selectionModel.setSelected(selectedNode, true);
			copiedNode = pasteNode;
		}
	}
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#getCopiedNode()
	 */
	@Override
	public CellTreeNode getCopiedNode() {
		return copiedNode;
	}
	/**
	 * Gets the cell tree.
	 * @return the cell tree
	 */
	@Override
	public CellTree getCellTree() {
		return cellTree;
	}
	/**
	 * Sets the cell tree.
	 * @param cellTree
	 *            the new cell tree
	 */
	@Override
	public void setCellTree(CellTree cellTree) {
		this.cellTree = cellTree;
	}
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#editNode(java.lang.String, java.lang.String)
	 */
	@Override
	public void editNode(String name, String label) {
		if (selectedNode != null) {
			selectedNode.setName(name);
			selectedNode.setLabel(label);
			closeParentOpenNodes(cellTree.getRootTreeNode());
		}
	}
	/**
	 * Sets the clause workspace context menu.
	 * @param clauseWorkspaceContextMenu
	 *            the clauseWorkspaceContextMenu to set
	 */
	@Override
	public void setClauseWorkspaceContextMenu(
			ClauseWorkspaceContextMenu clauseWorkspaceContextMenu) {
		this.clauseWorkspaceContextMenu = clauseWorkspaceContextMenu;
		popupPanel = clauseWorkspaceContextMenu.popupPanel;
	}
	/* (non-Javadoc)
	 * @see com.google.gwt.event.dom.client.KeyDownHandler#onKeyDown(com.google.gwt.event.dom.client.KeyDownEvent)
	 */
	@Override
	public void onKeyDown(KeyDownEvent event) {
		int keyCode = event.getNativeKeyCode();
		if (selectedNode != null) {
			short nodeType = selectedNode.getNodeType();
			if (event.isControlKeyDown()) {
				if (keyCode == PopulationWorkSpaceConstants.COPY_C) { //COPY
					if ((nodeType != CellTreeNode.MASTER_ROOT_NODE) && (nodeType != CellTreeNode.ROOT_NODE)) {
						popupPanel.hide();
						copy();
					}
				} else if (keyCode == PopulationWorkSpaceConstants.PASTE_V) { //PASTE
					boolean canPaste = false;
					popupPanel.hide();
					if (copiedNode != null) {
						switch (selectedNode.getNodeType()) {
							case CellTreeNode.ROOT_NODE:
								if (selectedNode.equals(copiedNode.getParent())) {
									clauseWorkspaceContextMenu.pasteRootNodeTypeItem();
									isDirty = true;
								}
								break;
							case CellTreeNode.LOGICAL_OP_NODE: case CellTreeNode.FUNCTIONS_NODE:
								if (copiedNode.getNodeType() != CellTreeNode.CLAUSE_NODE) {
									canPaste = true;
								}
								break;
							case CellTreeNode.TIMING_NODE:
								if ((copiedNode.getNodeType() != CellTreeNode.CLAUSE_NODE)
										&& ((selectedNode.getChilds() == null)
												|| (selectedNode.getChilds().size() < 2))) {
									canPaste = true;
								}
								break;
							default:
								break;
						}
						if (canPaste) {
							paste();
							isDirty = true;
						}
					}
				} else if (keyCode == PopulationWorkSpaceConstants.CUT_X) { //CUT
					popupPanel.hide();
					if ((selectedNode.getNodeType() != CellTreeNode.MASTER_ROOT_NODE)
							&& (selectedNode.getNodeType() != CellTreeNode.CLAUSE_NODE)
							&& (selectedNode.getNodeType() != CellTreeNode.ROOT_NODE)
							&& (selectedNode.getParent().getNodeType() != CellTreeNode.CLAUSE_NODE)) {
						copy();
						removeNode();
						isDirty = true;
					}
				}
			} else if (keyCode == PopulationWorkSpaceConstants.DELETE_DELETE) { //DELETE
				popupPanel.hide();
				if (((selectedNode.getNodeType() != CellTreeNode.MASTER_ROOT_NODE)
						&& (selectedNode.getNodeType() != CellTreeNode.ROOT_NODE)
						&& (selectedNode.getParent().getNodeType() != CellTreeNode.CLAUSE_NODE)
						&& (selectedNode.getNodeType() != CellTreeNode.CLAUSE_NODE))
						|| ((selectedNode.getNodeType() == CellTreeNode.CLAUSE_NODE)
								&& (selectedNode.getParent().getChilds().size() > 1))) {
					removeNode();
					isDirty = true;
				}
			}
		}
		if ((event.isShiftKeyDown() && ((keyCode == PopulationWorkSpaceConstants.PLUS_FF)
				|| (keyCode == PopulationWorkSpaceConstants.PLUS_IE)))) {
			//EXPAND/COLLAPSE (+(Shift +) Expand| - Collapse)
			popupPanel.hide();
			openAllNodes(cellTree.getRootTreeNode());
		} else if ((event.isShiftKeyDown() && ((keyCode == PopulationWorkSpaceConstants.MINUS_FF)
				|| (keyCode == PopulationWorkSpaceConstants.MINUS_IE)))) {
			popupPanel.hide();
			closeNodes(cellTree.getRootTreeNode());
		}
		/*if(event.isControlKeyDown() && event.isAltKeyDown() && keyCode == 83){
			saveBtn.getElement().focus();
			saveBtn.click();
		}*/
	}
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#setCopiedNode(mat.client.clause.clauseworkspace.model.CellTreeNode)
	 */
	@Override
	public void setCopiedNode(CellTreeNode cellTreeNode) {
		copiedNode = cellTreeNode;
	}
	/* (non-Javadoc)
	 * @see com.google.gwt.event.dom.client.FocusHandler#onFocus(com.google.gwt.event.dom.client.FocusEvent)
	 */
	@Override
	public void onFocus(FocusEvent event) {
		focusPanel.setStyleName("focusPanel");
	}
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#setDirty(boolean)
	 */
	@Override
	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return isDirty;
	}
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#expandSelected(com.google.gwt.user.cellview.client.TreeNode)
	 */
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
	
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#validateCellTreeNodes(com.google.gwt.user.cellview.client.TreeNode)
	 */
	@Override
	public boolean validateCellTreeNodes(TreeNode treeNode) {
		
		if (treeNode != null) {
			openAllNodes(treeNode);
			for (int i = 0; i < treeNode.getChildCount(); i++) {
				TreeNode subTree = null;
				CellTreeNode node = (CellTreeNode) treeNode.getChildValue(i);
				if ((node.getNodeType()
						== CellTreeNode.TIMING_NODE) || (node.getNodeType() == CellTreeNode.RELATIONSHIP_NODE)) {
					// this check is performed since IE was giving JavaScriptError after removing a node and
					//closing all nodes.
					subTree = treeNode.setChildOpen(i, true, true);
					if ((subTree != null) && (subTree.getChildCount() == 2)) {
						if (!node.getValidNode()) {
							editNode(true, node, subTree);
						}
					} else {
						editNode(false, node, subTree);
						if (isValid) {
							isValid = false;
						}
					}
					
				}
				subTree = treeNode.setChildOpen(i, ((CellTreeNode) treeNode.getChildValue(i)).isOpen(),
						((CellTreeNode) treeNode.getChildValue(i)).isOpen());
				if ((subTree != null) && (subTree.getChildCount() > 0)) {
					validateCellTreeNodes(subTree);
				}
			}
		}
		return isValid;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#addNode(java.lang.String, java.lang.String, java.lang.String, short)
	 */
	@Override
	public CellTreeNode addNode(String name, String label, String uuid,
			short nodeType) {
		CellTreeNode childNode = null;
		if ((selectedNode != null) &&  (name != null) && (name.trim().length() > 0)) { //if nodeTex textbox is not empty
			childNode = selectedNode.createChild(name, label, nodeType);
			childNode.setUUID(uuid);
			closeSelectedOpenNodes(cellTree.getRootTreeNode());
			selectionModel.setSelected(selectedNode, true);
		}
		return childNode;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#editNode(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void editNode(String name, String label, String uuid) {
		if (selectedNode != null) {
			selectedNode.setName(name);
			selectedNode.setLabel(label);
			selectedNode.setUUID(uuid);
			closeParentOpenNodes(cellTree.getRootTreeNode());
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#editNode(boolean, mat.client.clause.clauseworkspace.model.CellTreeNode, com.google.gwt.user.cellview.client.TreeNode)
	 */
	@Override
	public void editNode(boolean isValideNodeValue, CellTreeNode node, TreeNode subTree) {
		node.setValidNode(isValideNodeValue);
		selectedNode = node;
		closeParentOpenNodes(cellTree.getRootTreeNode());
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#getWarningMessageDisplay()
	 */
	@Override
	public WarningMessageDisplay getWarningMessageDisplay() {
		return warningMessageDisplay;
	}
	
	/**
	 * Checks if is valid.
	 * 
	 * @return the isValid
	 */
	public boolean isValid() {
		return isValid;
	}
	
	
	/**
	 * Sets the valid.
	 * 
	 * @param isValid
	 *            the isValid to set
	 */
	@Override
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	
	
	
	/**
	 * @return the createSubTree
	 *//*
	@Override
	public Button getCreateSubTree() {
		return createSubTree;
	}
	
	
	
	  *//**
	  * @param createSubTree the createSubTree to set
	  *//*
	public void setCreateSubTree(CustomButton createSubTree) {
		this.createSubTree = createSubTree;
	}*/
	
	
	
	/**
	 * @return the buttonExpandClauseWorkSpace
	 */
	public Button getButtonExpandClauseWorkSpace() {
		return buttonExpandClauseWorkSpace;
	}
	
	
	
	/**
	 * @param buttonExpandClauseWorkSpace the buttonExpandClauseWorkSpace to set
	 */
	public void setButtonExpandClauseWorkSpace(Button buttonExpandClauseWorkSpace) {
		this.buttonExpandClauseWorkSpace = buttonExpandClauseWorkSpace;
	}
	
	
	
	/**
	 * @return the buttonCollapseClauseWorkSpace
	 */
	public Button getButtonCollapseClauseWorkSpace() {
		return buttonCollapseClauseWorkSpace;
	}
	
	
	
	/**
	 * @param buttonCollapseClauseWorkSpace the buttonCollapseClauseWorkSpace to set
	 */
	public void setButtonCollapseClauseWorkSpace(Button buttonCollapseClauseWorkSpace) {
		this.buttonCollapseClauseWorkSpace = buttonCollapseClauseWorkSpace;
	}
	
	
	
	/*
	 *//**
	 * @return the clauseCellTree
	 *//*
	public CellTree getClauseCellTree() {
		return clauseCellTree;
	}
	
	
	
	  *//**
	  * @param clauseCellTree the clauseCellTree to set
	  *//*
	public void setClauseCellTree(CellTree clauseCellTree) {
		this.clauseCellTree = clauseCellTree;
	}
	   */
	
	
	/**
	 * @return the saveBtnClauseWorkSpace
	 */
	@Override
	public Button getSaveBtnClauseWorkSpace() {
		return saveBtnClauseWorkSpace;
	}
	
	
	
	/**
	 * @param saveBtnClauseWorkSpace the saveBtnClauseWorkSpace to set
	 */
	public void setSaveBtnClauseWorkSpace(Button saveBtnClauseWorkSpace) {
		this.saveBtnClauseWorkSpace = saveBtnClauseWorkSpace;
	}
	
	
	
	/**
	 * @return the validateBtnClauseWorkSpace
	 */
	@Override
	public Button getValidateBtnClauseWorkSpace() {
		return validateBtnClauseWorkSpace;
	}
	
	
	
	/**
	 * @param validateBtnClauseWorkSpace the validateBtnClauseWorkSpace to set
	 */
	public void setValidateBtnClauseWorkSpace(Button validateBtnClauseWorkSpace) {
		this.validateBtnClauseWorkSpace = validateBtnClauseWorkSpace;
	}
	
	/**
	 * @return the clearClauseWorkSpace
	 */
	@Override
	public Button getClearClauseWorkSpace() {
		return clearClauseWorkSpace;
	}
	
	/**
	 * @param clearClauseWorkSpace the clearClauseWorkSpace to set
	 */
	public void setClearClauseWorkSpace(Button clearClauseWorkSpace) {
		this.clearClauseWorkSpace = clearClauseWorkSpace;
	}
	
	private void setCommentsBoxReadOnly(boolean b){
		commentArea.setReadOnly(b);
		commentButtons.setEnabled(!b);
	}
	
	@Override
	public ErrorMessageDisplay getClearErrorDisplay() {
		return clearErrorDisplay;
	}
	/**
	 * @return the commentButtons
	 */
	@Override
	public Button getCommentButtons() {
		return commentButtons;
	}
	
	/**
	 * @return the commentArea
	 */
	@Override
	public CommentAreaTextBox getCommentArea() {
		return commentArea;
	}
}
