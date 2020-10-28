package mat.client.clause.clauseworkspace.view;

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
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
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
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.model.CellTreeNodeImpl;
import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;
import mat.client.clause.clauseworkspace.presenter.XmlConversionlHelper;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatContext;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SecondaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.WarningMessageDisplay;
import mat.shared.ConstantMessages;
import mat.shared.MatConstants;
import mat.shared.UUIDUtilClient;
import org.apache.commons.lang3.StringUtils;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.CheckBox;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class XmlTreeView extends Composite implements  XmlTreeDisplay, TreeViewModel, KeyDownHandler, FocusHandler {

	public static final int CLAUSE_NESTED_DEPTH = 10;

	private static final int COMMENT_MAX_LENGTH = 250;

	interface Template extends SafeHtmlTemplates {

		@Template("<div class=\"{0}\" id=\"{1}\" title=\"{2}\" aria-role=\"tree\">{3}</div>")
		SafeHtml outerDiv(String classes, String id , String title, String content);
	
		@Template("<div class=\"{0}\" id=\"{1}\" title=\"{2}\" aria-role=\"treeitem\">{3}</div>")
		SafeHtml outerDivItem(String classes,String id, String title, String content);
	
		@Template("<div class=\"{0}\" id=\"{1}\" title=\"{2}\" aria-role=\"treeitem\">{3}"
				+ "<span class =\"populationWorkSpaceCommentNode\">&nbsp;(C)</span></div>")
		SafeHtml outerDivItemWithSpan(String classes, String id, String title, String content);
		
		
	}
	
	private static final Template template = GWT.create(Template.class);
	
	private FlowPanel  mainPanel = new FlowPanel();
	
	private static final String STRATUM = "Stratum";
	
	private static final String MEASURE_OBSERVATION = "Measure Observation";
	
	private static final String STRATIFICATION = "Stratification";

	private Label remainingCharsLabel = new Label("250");
	
	private FocusPanel focusPanel = new FocusPanel(mainPanel);
	
	private CellTree cellTree;
	
	private Button saveBtn = new PrimaryButton("Save", "primaryButton");

	private Button saveBtnClauseWorkSpace = new PrimaryButton("Save", "primaryButton");
	
	private Button validateBtnClauseWorkSpace = new SecondaryButton("Validate");
	
	private Button clearClauseWorkSpace = new SecondaryButton("Clear");

	private static final String COMMENT = "COMMENT";
	
	private Button commentButtons = new Button("OK");
	
	private CommentAreaTextBox commentArea = new CommentAreaTextBox(COMMENT_MAX_LENGTH);
	
	private Button buttonExpand = new Button();
	
	private Button buttonCollapse = new Button();
	
	private Button buttonExpandClauseWorkSpace = new Button();
	
	private Button buttonCollapseClauseWorkSpace = new Button();
	
	private ErrorMessageDisplay errorMessageDisplay = new ErrorMessageDisplay();
	
	private SuccessMessageDisplay successMessageDisplay = new SuccessMessageDisplay();
	
	private SuccessMessageDisplay successMessageAddCommentDisplay = new SuccessMessageDisplay();
	
	private boolean isClauseWorkSpace = false;
	
	VerticalPanel addCommentPanel;

	private ErrorMessageDisplay clearErrorDisplay = new ErrorMessageDisplay();
	
	private WarningMessageDisplay warningMessageDisplay = new WarningMessageDisplay();
	
	private ListDataProvider<CellTreeNode> nodeDataProvider;
	
	private CellTreeNode selectedNode;
	
	private final SingleSelectionModel<CellTreeNode> selectionModel = new SingleSelectionModel<CellTreeNode>();
	
	private CellTreeNode copiedNode;
	
	private PopupPanel popupPanel;
	
	private ListBox subTreeNameListBox;
	
	private SuggestBox searchSuggestTextBox;
	
	private Button openClauseButton = new Button("Show Clause");
	
	private Button deleteClauseButton = new Button("Delete Clause");
	
	private ClauseWorkspaceContextMenu clauseWorkspaceContextMenu;
	
	private boolean isDirty = false;
	private boolean isClauseOpen;
	private boolean isEditable;
	
	private String isQdmVariable = "false";
	
	private boolean isQdmVariableDirty = false;
	
	private CheckBox includeQdmVaribale = new CheckBox("QDM Variable");

	private boolean isValid = false;
	
	private boolean isValidHumanReadable = false;
	
	boolean isDateTimeDiffNotInMO = false;
	
	boolean isSubTreeLogicValidInPopulationWorkSpace = true;
	
	private static final String MEASURE_OBSERVATIONS = "Measure Observations";
	
	private boolean isMeasureObservations = false;
	
	boolean isFuncWithOpInMO = false;

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
		buttonExpand.getElement().setId("buttonExpand_Button");
		buttonCollapse.setStylePrimaryName("collapseAllButton");
		buttonCollapse.getElement().setId("buttonCollapse_Button");
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
		//savePanel.add(new SpacerWidget());
		vp.add(successMessageDisplay);
		successMessageDisplay.getElement().setId("successMessageDisplay_SuccessMessageDisplay");
		savePanel.add(saveBtn);
		//Commented Validate Button from Population Work Space as part of Mat-3162
		// back to commeneted as part of mat-7847
//		validateBtnPopulationWorkspace.setTitle("Validate");//uncommented
//		validateBtnPopulationWorkspace.getElement().setId("validateBtnPopulationWorkspace_Button");
//		savePanel.add(validateBtnPopulationWorkspace);// uncommented
		vp.add(warningMessageDisplay);//uncommented
		warningMessageDisplay.getElement().setId("warningMessageDisplay_WarningMessageDisplay");
		vp.add(savePanel);
		bottomSavePanel.add(vp);
		SimplePanel errPanel = new SimplePanel();
		errPanel.getElement().setId("errPanel_SimplePanel");
		errPanel.add(errorMessageDisplay);
		errorMessageDisplay.getElement().setId("errorMessageDisplay_ErrorMessageDisplay");
		addCommentPanel = addCommentPanelToRightPanel();
		addCommentPanel.setVisible(false);
		rightPanel.add(addCommentPanel);
		mainPanel.add(errPanel);
		mainPanel.add(leftPanel);
		mainPanel.add(rightPanel);
		mainPanel.add(bottomSavePanel);
		focusPanel.addKeyDownHandler(this);
		focusPanel.addFocusHandler(this);
		isClauseWorkSpace = false;
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
		remainCharsPanel.add(remainingCharsLabel);
		remainCharsPanel.add(new HTML("&nbsp;characters remaining."));
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
	 *
	 * @param cellTree the cell tree
	 */
	public void createClauseWorkSpacePageView(CellTree cellTree) {
		this.cellTree = cellTree;
		mainPanel.clear();
		mainPanel.setStyleName("div-wrapper"); //main div
		mainPanel.getElement().setId("mainPanel_FlowPanel");
		SimplePanel leftPanel = new SimplePanel();
		leftPanel.getElement().setId("leftPanel_SimplePanelCW");
		leftPanel.setStyleName("div-first bottomPadding10px"); //left side div which will  have tree
		
		
		VerticalPanel rightVerticalPanel = new VerticalPanel();
		rightVerticalPanel.setSpacing(10);
		rightVerticalPanel.setStyleName("div-second");
		rightVerticalPanel.setWidth("290px");
		rightVerticalPanel.setHeight("200px");
		rightVerticalPanel.getElement().setId("rhsVerticalPanel_VerticalPanelCW");
		rightVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		Label clauseLibraryLabel = new Label("Clause Library");
		//clauseLibraryLabel.setStyleName("clauseLibraryLabel");
		
		searchSuggestTextBox = new SuggestBox();
		updateSuggestOracle();
		searchSuggestTextBox.setWidth("250px");
		searchSuggestTextBox.setText("Search");
		searchSuggestTextBox.getElement().setId("searchTextBox_TextBoxCW");
		
		searchSuggestTextBox.getValueBox().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if ("Search".equals(searchSuggestTextBox.getText())) {
					searchSuggestTextBox.setText("");
				}
			}
		});
		
		subTreeNameListBox = new ListBox();
		subTreeNameListBox.setWidth("250px");
		subTreeNameListBox.setVisibleItemCount(10);
		subTreeNameListBox.getElement().setAttribute("id", "subTreeListBox");
		clearAndAddClauseNamesToListBox();
		
		addSuggestHandler(searchSuggestTextBox, subTreeNameListBox);
		addListBoxHandler(subTreeNameListBox, searchSuggestTextBox);
		
		HorizontalPanel clauseButtonPanel = new HorizontalPanel();
		clauseButtonPanel.setWidth("100%");
		clauseButtonPanel.add(openClauseButton);
		openClauseButton.getElement().setId("openClauseButton_Button");
		clauseButtonPanel.add(deleteClauseButton);
		deleteClauseButton.getElement().setId("deleteClauseButton_Button");
		clauseButtonPanel.setCellHorizontalAlignment(openClauseButton, HasHorizontalAlignment.ALIGN_LEFT);
		clauseButtonPanel.setCellHorizontalAlignment(deleteClauseButton, HasHorizontalAlignment.ALIGN_RIGHT);
		
		rightVerticalPanel.add(clauseLibraryLabel);
		clauseLibraryLabel.getElement().setId("clauseLibraryLabel_Label");
		searchSuggestTextBox.getElement().setAttribute("id", "searchSuggestTextBox");
		rightVerticalPanel.add(searchSuggestTextBox);
		searchSuggestTextBox.getElement().setId("searchSuggestTextBox_SuggestBox");
		rightVerticalPanel.add(subTreeNameListBox);
		subTreeNameListBox.getElement().setId("subTreeNameListBox_ListBox");
		rightVerticalPanel.add(clauseButtonPanel);
		clauseButtonPanel.getElement().setId("clauseButtonPanel_HorizontalPanel");
		
		rightVerticalPanel.setCellHorizontalAlignment(clauseLibraryLabel, HasHorizontalAlignment.ALIGN_LEFT);
		
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
		buttonExpandClauseWorkSpace.getElement().setId("buttonExpandClauseWorkSpace_Button");
		expandCollapse.add(buttonCollapseClauseWorkSpace);
		buttonCollapseClauseWorkSpace.getElement().setId("buttonCollapseClauseWorkSpace_Button");
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
		SimplePanel chbxPanel = new SimplePanel();
		chbxPanel.getElement().setId("QDM_Attribute_CheckBox");
		chbxPanel.setStyleName("div-first bottomPadding10px");
		includeQdmVaribale.setTitle("Select 'QDM Variable' to create clause as " +
				"local variable.");
		chbxPanel.add(includeQdmVaribale);
		includeQdmVaribale.setVisible(false);
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
		saveBtnClauseWorkSpace.getElement().setAttribute("id", "saveButton_CW");
		savePanel.add(saveBtnClauseWorkSpace);
		validateBtnClauseWorkSpace.setTitle("Validate");
		clearClauseWorkSpace.setTitle("Clear Clause WorkSpace");
		savePanel.add(validateBtnClauseWorkSpace);
		savePanel.add(clearClauseWorkSpace);
		clearClauseWorkSpace.getElement().setId("clearClauseWorkSpace_Button");
		vp.add(warningMessageDisplay);
		vp.add(savePanel);
		bottomSavePanel.add(vp);
		SimplePanel errPanel = new SimplePanel();
		errPanel.getElement().setId("errPanel_SimplePanelCW");
		errPanel.add(errorMessageDisplay);
		mainPanel.add(errPanel);
		mainPanel.add(leftPanel);
		mainPanel.add(rightVerticalPanel);
		mainPanel.add(chbxPanel);
		mainPanel.add(bottomSavePanel);
		focusPanel.addKeyDownHandler(this);
		focusPanel.addFocusHandler(this);
		isClauseWorkSpace = true;
	}
	
	/**
	 * Creates the clause logic page view.
	 *
	 * @param cellTree the cell tree
	 */
	public void createClauseLogicPageView(CellTree cellTree) {
		this.cellTree = cellTree;
		mainPanel.clear();
		//mainPanel.setStyleName("div-wrapper"); //main div
		SimplePanel leftPanel = new SimplePanel();
		leftPanel.getElement().setId("leftPanel_SimplePanelCW");
		leftPanel.setStyleName("div-first bottomPadding10px"); //left side div which will  have tree
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
		buttonExpandClauseWorkSpace.getElement().setId("buttonExpandClauseWorkSpace_Button");
		buttonCollapseClauseWorkSpace.getElement().setId("buttonCollapseClauseWorkSpace_Button");
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
		mainPanel.add(leftPanel);
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
	 * Adds the suggest handler.
	 * 
	 * @param suggestBox
	 *            the suggest box
	 * @param listBox
	 *            the list box
	 */
	private void addSuggestHandler(final SuggestBox suggestBox,
			final ListBox listBox) {
		suggestBox.addSelectionHandler(new SelectionHandler<Suggestion>() {
			
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				String selectedQDMName = event.getSelectedItem()
						.getReplacementString();
				for (int i = 0; i < listBox.getItemCount(); i++) {
					if (selectedQDMName.equals(listBox.getItemText(i))) {
						listBox.setItemSelected(i, true);
						
						break;
					}
				}
			}
		});
	}
	
	/**
	 * Adds the list box handler.
	 *
	 * @param listBox the list box
	 * @param suggestBox the suggest box
	 */
	private void addListBoxHandler(final ListBox listBox,
			final SuggestBox suggestBox) {
		listBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				System.out.println("listbox change event:"+event.getAssociatedType().getName());
				int selectedIndex = listBox.getSelectedIndex();
				String selectedItem = listBox.getItemText(selectedIndex);
				suggestBox.setText(selectedItem);
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#clearAndAddClauseNamesToListBox()
	 */
	@Override
	public void clearAndAddClauseNamesToListBox() {
		if(subTreeNameListBox != null){
			subTreeNameListBox.clear();
			List<Entry<String,String>> subTreeNameEntries = new LinkedList<Map.Entry<String,String>>(PopulationWorkSpaceConstants.subTreeLookUpName.entrySet());
			
			for(Entry<String, String> entry:subTreeNameEntries){
				subTreeNameListBox.addItem(entry.getValue(),entry.getKey());
			}
			
			// Set tooltips for each element in listbox
			SelectElement selectElement = SelectElement.as(subTreeNameListBox.getElement());
			com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement
					.getOptions();
			for (int i = 0; i < options.getLength(); i++) {
				String title = options.getItem(i).getText();
				OptionElement optionElement = options.getItem(i);
				optionElement.setTitle(title);
			}
		}
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#updateSuggestOracle()
	 */
	@Override
	public void updateSuggestOracle(){
		if(searchSuggestTextBox != null){
			MultiWordSuggestOracle multiWordSuggestOracle = (MultiWordSuggestOracle)searchSuggestTextBox.getSuggestOracle();
			multiWordSuggestOracle.clear();
			multiWordSuggestOracle.addAll(PopulationWorkSpaceConstants.subTreeLookUpName
					.values());
		}
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
		 * Constructor.
		 *
		 * @param maxLen the max len
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
					if (!CommentAreaTextBox.this.isReadOnly()) {
						String commentAreaUpdatedText;
						int pos = getCursorPos();
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
							setCursorPos(maxLength);
						} else {
							CommentAreaTextBox.this.setValue(commentAreaUpdatedText);
							setCursorPos(pos);
						}
						
						setDirty(true);
						onTextAreaContentChanged(remainingCharsLabel);
					}
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
			// Checking for key Down event.
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
				if ((event.getKeyCode() != KeyCodes.KEY_LEFT)
						&& (event.getKeyCode() != KeyCodes.KEY_TAB)
						&& (event.getKeyCode() != KeyCodes.KEY_RIGHT)
						&& (event.getKeyCode() != KeyCodes.KEY_SHIFT)) {
					if (!event.getCtrlKey()) {
						Scheduler.get().scheduleDeferred(new ScheduledCommand() {
							@Override
							public void execute() {
								ValueChangeEvent.fire(CommentAreaTextBox.this,
										CommentAreaTextBox.this.getText());
							}
						});
					}
				}
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
		 *
		 * @param maxLength the new max length
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
			if ((cellTreeNode.getNodeType()
					== CellTreeNode.MASTER_ROOT_NODE)
					|| (cellTreeNode.getNodeType()
							== CellTreeNode.ROOT_NODE)) {
				sb.append(template.outerDiv(getStyleClass(cellTreeNode), UUIDUtilClient.uuid(5).concat("_treeNode_"+cellTreeNode.getLabel()),
						cellTreeNode.getTitle(),
						cellTreeNode.getLabel() != null
						? cellTreeNode.getLabel() : cellTreeNode.getName()));
			} else {
				if((cellTreeNode.getNodeType() == CellTreeNode.LOGICAL_OP_NODE)
						|| (cellTreeNode.getNodeType() == CellTreeNode.SUBTREE_REF_NODE) ){
					boolean foundComment = false;
					@SuppressWarnings("unchecked")
					List<CellTreeNode> childNode = (List<CellTreeNode>) cellTreeNode.
					getExtraInformation(PopulationWorkSpaceConstants.COMMENTS);
					if (childNode != null) {
						for (CellTreeNode treeNode : childNode) {
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
		@SuppressWarnings("unchecked")
		@Override
		public void onBrowserEvent(Context context, Element parent, CellTreeNode value,
				NativeEvent event, ValueUpdater<CellTreeNode> valueUpdater) {
			if (event.getType().equals(BrowserEvents.CONTEXTMENU)) {
				/*successMessageAddCommentDisplay.removeStyleName("successMessageCommentPanel");*/
				successMessageDisplay.clear();
				getErrorMessageDisplay().clear();
				event.preventDefault();
				event.stopPropagation();
				if (!isClauseOpen && MatContext.get().getMeasureLockService().checkForEditPermission()) {
					onRightClick(value, (Event) event, parent);
				}
			} else if (event.getType().equals(BrowserEvents.CLICK)
					|| event.getType().equalsIgnoreCase(BrowserEvents.FOCUS)) {
				
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					if ((value.getNodeType() == CellTreeNodeImpl.LOGICAL_OP_NODE)
							|| (value.getNodeType() == CellTreeNodeImpl.SUBTREE_REF_NODE)) {
						if(addCommentPanel != null) {
							addCommentPanel.setVisible(true);
							commentArea.setText("");
							List<CellTreeNode> childNode = (List<CellTreeNode>) value.
									getExtraInformation(PopulationWorkSpaceConstants.COMMENTS);
							if (childNode != null) {
								for (CellTreeNode cellTreeNode : childNode) {
									if (cellTreeNode.getNodeType() == CellTreeNode.COMMENT_NODE) {
										commentArea.setText(cellTreeNode.getNodeText());
									}
								}
							}
							onTextAreaContentChanged(remainingCharsLabel);
							setCommentsBoxReadOnly(false);
						}
						
						/*setDirty(true);*/
					} else {
						if(addCommentPanel != null) {
							addCommentPanel.setVisible(false);
							commentArea.setText("");
							onTextAreaContentChanged(remainingCharsLabel);
							setCommentsBoxReadOnly(true);
						}
						
					}
				} else {
					if(addCommentPanel != null) {
						addCommentPanel.setVisible(false);
					}
					if ((value.getNodeType() == CellTreeNodeImpl.LOGICAL_OP_NODE)
							|| (value.getNodeType() == CellTreeNodeImpl.SUBTREE_REF_NODE)) {
						if(addCommentPanel != null) {
							addCommentPanel.setVisible(true);
							commentArea.setText("");
							List<CellTreeNode> childNode = (List<CellTreeNode>) value.
									getExtraInformation(PopulationWorkSpaceConstants.COMMENTS);
							if (childNode != null) {
								for (CellTreeNode cellTreeNode : childNode) {
									if (cellTreeNode.getNodeType() == CellTreeNode.COMMENT_NODE) {
										commentArea.setText(cellTreeNode.getNodeText());
									}
								}
							}
							onTextAreaContentChanged(remainingCharsLabel);
							setCommentsBoxReadOnly(true);
						}
					}
				}
			}  else {
				successMessageDisplay.clear();
				getErrorMessageDisplay().clear();
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
				case CellTreeNode.SUBTREE_REF_NODE:
					return "populationWorkSpaceCommentNode";
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
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#refreshCellTreeAfterAddingComment(mat.client.clause.clauseworkspace.model.CellTreeNode)
	 */
	@Override
	public void refreshCellTreeAfterAddingComment(CellTreeNode selectedNode) {
		closeNodes(cellTree.getRootTreeNode());
		openAllNodes(cellTree.getRootTreeNode());
		selectionModel.setSelected(selectedNode, false);
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
	 * Gets the success message add comment display.
	 *
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
		saveBtnClauseWorkSpace.setEnabled(enabled);
		// commented as part of mat-7387
//		validateBtnPopulationWorkspace.setEnabled(enabled);
		validateBtnClauseWorkSpace.setEnabled(enabled);
		deleteClauseButton.setEnabled(enabled);
		isEditable = enabled;
		includeQdmVaribale.setEnabled(enabled);
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
		copiedNode = selectedNode.cloneNode();
		copiedNode.setParent(selectedNode.getParent());//Setting Parent node
	}
	/*@Override
	public void globalCopy(){
		CellTreeNode globalCopiedNode = selectedNode.cloneNode();
		globalCopiedNode.setParent(selectedNode.getParent());
		GlobalCopyPaste globalCopyPaste = new GlobalCopyPaste();
		globalCopyPaste.setCopiedNode(globalCopiedNode);
		globalCopyPaste.setClauseXml(XmlConversionlHelper.createXmlFromTree(globalCopiedNode));
		ArrayList<QualityDataSetDTO> elementsList = new ArrayList<QualityDataSetDTO>();
		
		globalCopyPaste.setElementLookUpXml(XmlConversionlHelper.createXmlElementLookUpGlobalCopy(globalCopiedNode , elementsList));
		globalCopyPaste.setElementLookUpList(elementsList);
		globalCopyPaste.setCopiedFromMeasureId(MatContext.get().getCurrentMeasureId());
		MatContext.get().setCopyPaste(globalCopyPaste);
	}*/
	
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#paste()
	 */
	@Override
	public void paste() {
		if (selectedNode != null) {
			CellTreeNode pasteNode = copiedNode.cloneNode();//It was calling copiedNode object before
			selectedNode.appendChild(pasteNode);
			closeSelectedOpenNodes(cellTree.getRootTreeNode());
			selectionModel.setSelected(selectedNode, true);
			CellTreeNode clonedNode = pasteNode.cloneNode();//created new instance for pasted node, prevent from overriding
			clonedNode.setParent(pasteNode.getParent());//set parent of the cloned node
			copiedNode = clonedNode;//Assigning pasted value to the copiedNode
		}
	}
	
	/*@Override
	public void globalPaste() {
		if (selectedNode != null) {
			CellTreeNode pasteNode = MatContext.get().getCopyPaste().getCopiedNode().cloneNodeForGlobalPaste(); //It was calling copiedNode object before
			selectedNode.appendChild(pasteNode);
			closeSelectedOpenNodes(cellTree.getRootTreeNode());
			selectionModel.setSelected(selectedNode, true);
			CellTreeNode clonedNode = pasteNode.cloneNode(); //created new instance for pasted node, prevent from overriding
			clonedNode.setParent(pasteNode.getParent()); //set parent of the cloned node
			copiedNode = clonedNode; //Assigning pasted value to the copiedNode
		}
		
	}*/
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#moveUp()
	 */
	@Override
	public void moveUp() {
		
		if (selectedNode != null) {
			int index = 0;
			for (int i = 0; i <= selectedNode.getParent().getChilds().size(); i++) {
				if (selectedNode.equals(selectedNode.getParent().getChilds().get(i))) {
					index = i;
					System.out.println("Index found ---- " + index);
					CellTreeNode parentNode = selectedNode.getParent();
					CellTreeNode nodeToMoveUp = selectedNode.cloneNode();
					nodeToMoveUp.setParent(parentNode);
					parentNode.getChilds().add(index - 1, nodeToMoveUp);
					selectedNode.getParent().removeChild(selectedNode);
					selectionModel.setSelected(nodeToMoveUp, false);
					closeNodes(cellTree.getRootTreeNode());
					openAllNodes(cellTree.getRootTreeNode());
					selectionModel.setSelected(nodeToMoveUp, false);
					isDirty = true;
					break;
				}
			}
		}
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#moveDown()
	 */
	@Override
	public void moveDown() {
		
		if (selectedNode != null) {
			int index = 0;
			for (int i = 0; i <= selectedNode.getParent().getChilds().size(); i++) {
				if (selectedNode.equals(selectedNode.getParent().getChilds().get(i))) {
					index = i;
					System.out.println("Index found ---- " + index);
					CellTreeNode parentNode = selectedNode.getParent();
					CellTreeNode nodeToMoveDown = selectedNode.cloneNode();
					nodeToMoveDown.setParent(parentNode);
					selectedNode.getParent().removeChild(selectedNode);
					parentNode.getChilds().add(index + 1, nodeToMoveDown);
					selectionModel.setSelected(nodeToMoveDown, false);
					closeNodes(cellTree.getRootTreeNode());
					openAllNodes(cellTree.getRootTreeNode());
					selectionModel.setSelected(nodeToMoveDown, false);
					isDirty = true;
					break;
				}
			}
		}
		
	}
	
	/*
	 * 
	 * POC Global Copy Paste.
	 * 
	 * @Override
	public void copyToClipboard() {
		String generatedXml = XmlConversionlHelper.createXmlFromTree(selectedNode);
		MatContext.get().setCopiedStringOnClipboard(generatedXml);
		MatContext.get().setCopiedNodeNameOnClipBoard(selectedNode.getName());
		copiedNode = selectedNode.cloneNode();
		copiedNode.setParent(selectedNode.getParent());//Setting Parent node
		MatContext.get().setCopiedNode(copiedNode);
	}
	
	@Override
	public void pasteFromClipboard() {
		if (selectedNode != null) {
			CellTreeNode pasteNode = MatContext.get().getCopiedNode().cloneNode();//It was calling copiedNode object before
			selectedNode.appendChild(pasteNode);
			closeSelectedOpenNodes(cellTree.getRootTreeNode());
			selectionModel.setSelected(selectedNode, true);
			CellTreeNode clonedNode = pasteNode.cloneNode();//created new instance for pasted node, prevent from overriding
			clonedNode.setParent(pasteNode.getParent());//set parent of the cloned node
			MatContext.get().setCopiedNode(clonedNode);//Assigning pasted value to the copiedNode
		}
	}*/
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
			selectionModel.setSelected(selectedNode.getParent(), true);
			// This is done to invoke focus event on Parent node to show Inline comment in Comment Area
			//when Edit Operation is Performed on ClauseWorspace or PopulationWorkspace.
			((NodeCell) getNodeInfo(selectedNode.getParent()).getCell()).
			onBrowserEvent(new Context(0, 0, null), null, selectedNode.getParent(), Document.get().createFocusEvent(), null);
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
	
	/**
	 * Sets the population workspace context menu.
	 * @param populationWorkspaceContextMenu
	 *            the populationWorkspaceContextMenu to set
	 */
	@Override
	public void setpopulationWorkspaceContextMenu(
			PopulationWorkSpaceContextMenu populationWorkspaceContextMenu) {
		popupPanel = clauseWorkspaceContextMenu.popupPanel;
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.dom.client.KeyDownHandler#onKeyDown(com.google.gwt.event.dom.client.KeyDownEvent)
	 */
	@Override
	public void onKeyDown(KeyDownEvent event) {
		int keyCode = event.getNativeKeyCode();
		/*Element element = Element.as(event.getNativeEvent().getEventTarget());
		// This is done to avoid executing Delete/Cut/Paste key board short cuts
		// to be executed on nodes when they event is triggered in Comment text area
		//in Population Work Space or suggestion Text Area in ClauseWorkSpace
		//or List item from Clause Library.
		if (element.getId().equalsIgnoreCase("addComment_TextArea")
				|| element.getId().equalsIgnoreCase("searchSuggestTextBox")
				|| element.getId().equalsIgnoreCase("subTreeListBox")) {
			System.out.println("Element - ID" + element.getId());
			
			return;
		}*/
		Element element = Element.as(event.getNativeEvent().getEventTarget());
		System.out.println("Element - ID" + element.getAttribute("id"));
		System.out.println("Element - getElementsByTagName" + element.getElementsByTagName("div"));
		String id= StringUtils.EMPTY;
		// This is done to avoid executing Delete/Cut/Paste key board short cuts
		// to be executed on nodes when they event is triggered in Comment text area.
		if ((element.getElementsByTagName("div") != null)
				&& (element.getElementsByTagName("div").getLength() > 0)) {
			id = element.getElementsByTagName("div").getItem(0).getAttribute("id");
			System.out.println("Element - ID " + id);
		}
		if (!(id.toLowerCase()).contains("treenode".toLowerCase())) {
			return;
		}
		if ((selectedNode != null) && isEditable) {
			if (keyCode == PopulationWorkSpaceConstants.DELETE_DELETE) { //DELETE
				popupPanel.hide();
				if((selectedNode.getNodeType() == CellTreeNode.CQL_DEFINITION_NODE)
						||  (selectedNode.getNodeType() == CellTreeNode.CQL_FUNCTION_NODE)
						||  (selectedNode.getNodeType() == CellTreeNode.CQL_AGG_FUNCTION_NODE)){
					
					removeNode();
					isDirty = true;
				}
				if (((selectedNode.getNodeType() != CellTreeNode.MASTER_ROOT_NODE)
						&& (selectedNode.getNodeType() != CellTreeNode.ROOT_NODE)
						&& (selectedNode.getNodeType() != CellTreeNode.SUBTREE_NODE)
						&& (selectedNode.getNodeType() != CellTreeNode.SUBTREE_ROOT_NODE)
						&& (selectedNode.getParent().getNodeType() != CellTreeNode.CLAUSE_NODE)
						&& (selectedNode.getNodeType() != CellTreeNode.CLAUSE_NODE))
						|| ((selectedNode.getNodeType() == CellTreeNode.CLAUSE_NODE)
								&& (selectedNode.getParent().getChilds().size() > 1))
						) {
					if( selectedNode.getParent().getName().equalsIgnoreCase("SATISFIES ALL")
							||  selectedNode.getParent().getName().equalsIgnoreCase("SATISFIES ANY")){
						if(selectedNode.getParent().getChilds().indexOf(selectedNode) != 0){
							removeNode();
							isDirty = true;
						}
					}
					else{
						removeNode();
						isDirty = true;
					}
				}
				
				if((selectedNode.getNodeType() != CellTreeNode.MASTER_ROOT_NODE)
						&& (selectedNode.getNodeType() != CellTreeNode.ROOT_NODE)
						&& (selectedNode.getNodeType() != CellTreeNode.CLAUSE_NODE)
						&& (selectedNode.getNodeType() != CellTreeNode.LOGICAL_OP_NODE)
						&& (selectedNode.getParent().getNodeType() == CellTreeNode.CLAUSE_NODE)
						&& (selectedNode.getNodeType() == CellTreeNode.SUBTREE_REF_NODE)){
					removeNode();
					isDirty = true;
					
				}
				if((selectedNode.getNodeType() != CellTreeNode.MASTER_ROOT_NODE)
						&& (selectedNode.getParent().getName().equalsIgnoreCase(STRATIFICATION))
						&& (selectedNode.getNodeType() != CellTreeNode.CLAUSE_NODE)
						&& (selectedNode.getParent().getNodeType() == CellTreeNode.MASTER_ROOT_NODE)
						&& (selectedNode.getNodeType() == CellTreeNode.ROOT_NODE)
						&& (selectedNode.getParent().getChilds().size() > 1)
						&& (selectedNode.getNodeType() != CellTreeNode.LOGICAL_OP_NODE)){
					removeNode();
					isDirty = true;
					
				}
				if((selectedNode.getNodeType() != CellTreeNode.MASTER_ROOT_NODE)
						&& (selectedNode.getNodeType() != CellTreeNode.CLAUSE_NODE)
						&& (selectedNode.getParent().getNodeType() == CellTreeNode.CLAUSE_NODE)
						&& (selectedNode.getParent().getName().contains(MEASURE_OBSERVATION)
								|| selectedNode.getParent().getName().contains(STRATUM))
								&& (selectedNode.getNodeType() != CellTreeNode.ROOT_NODE)
								&& (selectedNode.getNodeType() == CellTreeNode.LOGICAL_OP_NODE)){
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
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#validateCellTreeNodesPopulationWorkspace(com.google.gwt.user.cellview.client.TreeNode)
	 */
	@Override
	public List<String> validateCellTreeNodesPopulationWorkspace(TreeNode treeNode) {
		List<String> inValidNodeAtPopulationWorkspace = new ArrayList<String>();
		if (treeNode != null) {
			closeNodes(treeNode);
			setValidHumanReadable(true);
			isSubTreeLogicValidInPopulationWorkSpace = false;
			for (int i = 0; i < treeNode.getChildCount(); i++) {
				//TreeNode subTree = null;
				CellTreeNode node =(CellTreeNode) treeNode.getChildValue(i);
				validateCellTreeNodesPopulationWorkspace(node, inValidNodeAtPopulationWorkspace);
			}
		}
		openAllNodes(getXmlTree().getRootTreeNode());
		return inValidNodeAtPopulationWorkspace;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#validatePopulationCellTreeNodes(mat.client.clause.clauseworkspace.model.CellTreeNode)
	 */
	@Override
	public List<String> validatePopulationCellTreeNodes(CellTreeNode cellNode) {
		List<String> inValidNodeAtPopulationWorkspace = new ArrayList<String>();
		setValidHumanReadable(true);
		isSubTreeLogicValidInPopulationWorkSpace = false;
		if ((cellNode != null) && (cellNode.getChilds() != null)) {
			for (int i = 0; i < cellNode.getChilds().size(); i++) {
				//TreeNode subTree = null;
				CellTreeNode node = cellNode.getChilds().get(i);
				validateCellTreeNodesPopulationWorkspace(node, inValidNodeAtPopulationWorkspace);
			}
		}
		closeNodes(getXmlTree().getRootTreeNode());
		openAllNodes(getXmlTree().getRootTreeNode());
		return inValidNodeAtPopulationWorkspace;
	}

	public void validateCellTreeNodesPopulationWorkspace(
			CellTreeNode cellTreeNode,
			List<String> inValidNodeAtPopulationWorkspace) {
		int nodeType = cellTreeNode.getNodeType();
		switch(nodeType) {
			case CellTreeNode.LOGICAL_OP_NODE:
				if (cellTreeNode.getParent().getName()
						.contains(MEASURE_OBSERVATION)) {
					editNode(false, cellTreeNode);
					setValidHumanReadable(false);
					if(!inValidNodeAtPopulationWorkspace
							.contains("inValidAtMeasureObservationLogicalNode")){
						inValidNodeAtPopulationWorkspace
						.add("inValidAtMeasureObservationLogicalNode");
					}
				} else if(PopulationWorkSpaceConstants.getPopulationName().contains(cellTreeNode.getParent().getParent().getName())){
					//System.out.println("Top Level Logical Operator Found..... for :: " + cellTreeNode.getParent().getName());
					editNode(true, cellTreeNode);
				} else {
					//System.out.println("Child Nodes inside Logical Operator must be greater than 0 :: " + cellTreeNode.getName());
					if(cellTreeNode.hasChildren()){
						editNode(true, cellTreeNode);
						//System.out.println("Good Logical Node");
					} else {
						//	System.out.println("BAD Logical Node");
						if(!inValidNodeAtPopulationWorkspace
								.contains("inValidAtLogicalOperatorNode")){
							inValidNodeAtPopulationWorkspace
							.add("inValidAtLogicalOperatorNode");
						}
						editNode(false, cellTreeNode);
						setValidHumanReadable(false);
					}
				}
				break;
				
			case CellTreeNode.CLAUSE_NODE:
			case CellTreeNode.MASTER_ROOT_NODE:
			case CellTreeNode.ROOT_NODE:
				if (cellTreeNode.getName().equalsIgnoreCase(MEASURE_OBSERVATIONS)) {
					setMeasureObservations(true);
				}
				break;
			case CellTreeNode.SUBTREE_REF_NODE:
				// Added check for Measure Observation - For DateTimeDiff Fnx.
				if (cellTreeNode.getParent().getName()
						.contains(MEASURE_OBSERVATION)) {
					setMeasureObservations(true);
				}
				boolean checkValidation = validateSubTreeRefNode(cellTreeNode);
				//Method to find nesting depth of clauses inside clause.
				Node node = PopulationWorkSpaceConstants.subTreeLookUpNode
						.get(cellTreeNode.getName() + "~" + cellTreeNode.getUUID());
				NamedNodeMap namedNodeMap = node.getAttributes();
				CellTreeNode subTreeCellTreeNode = null;
				//for validating Occurrence of QDM Variable Clause
				if (namedNodeMap.getNamedItem("instance") != null) {
					String instanceOfUUID = namedNodeMap.getNamedItem("instanceOf").getNodeValue();
					String instanceOfDisplayName = namedNodeMap.getNamedItem("displayName").getNodeValue();
					Node occurenceNode = PopulationWorkSpaceConstants.subTreeLookUpNode
							.get(instanceOfDisplayName + "~" + instanceOfUUID);
					subTreeCellTreeNode = XmlConversionlHelper
							.createCellTreeNode(occurenceNode, instanceOfDisplayName);
				} else {
					subTreeCellTreeNode = XmlConversionlHelper
							.createCellTreeNode(node, cellTreeNode.getName());
				}
				boolean checkNestedValidation = validateClauseNodeNesting(subTreeCellTreeNode
						, inValidNodeAtPopulationWorkspace, 0);
				editNode(!checkValidation, cellTreeNode);
				if (checkNestedValidation) {
					editNode(false, cellTreeNode);
					setValidHumanReadable(false);
					//isSubTreeLogicValidInPopulationWorkSpace = true;
				}
				if (isDateTimeDiffNotInMO) {
					if (!inValidNodeAtPopulationWorkspace
							.contains("inValidAtPopulationAndStratificationNode")
							&& checkValidation) {
						inValidNodeAtPopulationWorkspace
						.add("inValidAtPopulationAndStratificationNode");
					}
					isDateTimeDiffNotInMO = false;
				} else if(isFuncWithOpInMO){
					if (!inValidNodeAtPopulationWorkspace
							.contains("isFunctionInvalidInMO") && checkValidation
							) {
						inValidNodeAtPopulationWorkspace
						.add("isFunctionInvalidInMO");
					}
					isFuncWithOpInMO = false;
				} else if(isSubTreeLogicValidInPopulationWorkSpace){
					if (!inValidNodeAtPopulationWorkspace
							.contains("invalidClauseLogic") && checkValidation
							) {
						inValidNodeAtPopulationWorkspace
						.add("invalidClauseLogic");
					}
					/*isSubTreeLogicValidInPopulationWorkSpace = false;*/
				}
				//}
				break;
			case CellTreeNode.CQL_AGG_FUNCTION_NODE:
			case CellTreeNode.CQL_DEFINITION_NODE:
			case CellTreeNode.CQL_FUNCTION_NODE:
				editNode(true, cellTreeNode);
				break;
			default:
				editNode(false, cellTreeNode);
				if(!inValidNodeAtPopulationWorkspace
						.contains("inValidAtOtherNode")){
					inValidNodeAtPopulationWorkspace
					.add("inValidAtOtherNode");
				}
				break;
				
				
		}
		
		List<CellTreeNode> children = cellTreeNode.getChilds();
		if((children != null) && (children.size() > 0)){
			for(CellTreeNode node:children){
				validateCellTreeNodesPopulationWorkspace(node,
						inValidNodeAtPopulationWorkspace);
				
			}
		}
	}
	/**
	 * Validate sub tree ref node.
	 *
	 * @param cellTreeNode the cell tree node
	 * @return true, if successful
	 */
	private boolean validateSubTreeRefNode(CellTreeNode cellTreeNode) {
		Node node = PopulationWorkSpaceConstants.subTreeLookUpNode
				.get(cellTreeNode.getName() + "~" + cellTreeNode.getUUID());
		CellTreeNode subTreeCellTreeNode = null;
		NamedNodeMap namedNodeMap = node.getAttributes();
		//for validating Occurrence of QDM Variable Clause
		if (namedNodeMap.getNamedItem("instance") != null) {
			String instanceOfUUID = namedNodeMap.getNamedItem("instanceOf").getNodeValue();
			String instanceOfDisplayName = namedNodeMap.getNamedItem("displayName").getNodeValue();
			Node occurenceNode = PopulationWorkSpaceConstants.subTreeLookUpNode
					.get(instanceOfDisplayName + "~" + instanceOfUUID);
			subTreeCellTreeNode = XmlConversionlHelper
					.createCellTreeNode(occurenceNode, instanceOfDisplayName);
		} else {
			subTreeCellTreeNode = XmlConversionlHelper
					.createCellTreeNode(node, cellTreeNode.getName());
		}
		setValid(false);
		
		boolean valid = validateSubtreeNodeAtPopulation(subTreeCellTreeNode);
		
		return valid;
	}
	/**
	 * Validate subtree node at population.
	 *
	 * @param subTreeCellTreeNode the sub tree cell tree node
	 * @return true, if successful
	 */
	private boolean validateSubtreeNodeAtPopulation(CellTreeNode subTreeCellTreeNode) {
		int nodeType = subTreeCellTreeNode.getNodeType();
		ArrayList<String> inValideNodesList =  new ArrayList<String>();
		//System.out.println("Start Node evaluated === "+ subTreeCellTreeNode.getName());
		//System.out.println("isValid Flag  === "+ isValid);
		switch(nodeType){
			case CellTreeNode.SET_OP_NODE:
			case CellTreeNode.TIMING_NODE:
			case CellTreeNode.RELATIONSHIP_NODE:
			case CellTreeNode.ELEMENT_REF_NODE:
			case CellTreeNode.SUBTREE_NODE:
				validateClauseWorkspaceCellTreeNodes(subTreeCellTreeNode,
						PopulationWorkSpaceConstants.datatypeMap, inValideNodesList);
				if (isValidHumanReadable) {
					setValidHumanReadable(inValideNodesList.size() == 0);
				}
				if(! isSubTreeLogicValidInPopulationWorkSpace) {
					isSubTreeLogicValidInPopulationWorkSpace = !(inValideNodesList.size()==0);
				}
				if(!isValid) {
					setValid(!(inValideNodesList.size() == 0));
				}
				
				break;
			case CellTreeNode.SUBTREE_REF_NODE:
				checkIfClauseAndAppend(subTreeCellTreeNode);
				if(isValidHumanReadable) {
					setValidHumanReadable(inValideNodesList.size() == 0);
				}
				if(! isSubTreeLogicValidInPopulationWorkSpace) {
					isSubTreeLogicValidInPopulationWorkSpace = !(inValideNodesList.size()==0);
				}
				if(!isValid) {
					setValid(!(inValideNodesList.size() == 0));
				}
				
				break;
			case CellTreeNode.FUNCTIONS_NODE:
				validateClauseWorkspaceCellTreeNodes(subTreeCellTreeNode, PopulationWorkSpaceConstants.datatypeMap, inValideNodesList);
				if(isValidHumanReadable) {
					setValidHumanReadable(inValideNodesList.size() == 0);
				}
				if(! isSubTreeLogicValidInPopulationWorkSpace) {
					isSubTreeLogicValidInPopulationWorkSpace = !(inValideNodesList.size()==0);
				}
				if(!isValid) {
					setValid(!(inValideNodesList.size() == 0));
				}
				if(!isClauseWorkSpace) {
					// Check for measure Ob and datetimediff is to be performed on Population workspace only.
					if (!isMeasureObservations
							&& (subTreeCellTreeNode.getName().toUpperCase().contains("DATETIMEDIFF"))) {
						setValid(true);
						isDateTimeDiffNotInMO = true;
						setValidHumanReadable(false);
						
						//validate functions with Operator which is not valid in Measure Observation
					} else if(isMeasureObservations
							&& !MatContext.get().functions.contains(capWords(subTreeCellTreeNode.getName()))){
						setValid(true);
						isFuncWithOpInMO = true;
						setValidHumanReadable(false);
					}
				}
				break;
		}
		//System.out.println("End Node evaluated === "+ subTreeCellTreeNode.getName());
		//System.out.println("isValid Flag  === "+ isValid);
		List<CellTreeNode> children = subTreeCellTreeNode.getChilds();
		if((children != null) && (children.size() > 0)
				&& !isValid()){
			for(CellTreeNode node:children){
				validateSubtreeNodeAtPopulation(node);
				
			}
		}
		
		return isValid;
	}
	
	/**
	 * Check if clause and append.
	 *
	 * @param subTreeCellTreeNode the sub tree cell tree node
	 */
	private void checkIfClauseAndAppend(CellTreeNode subTreeCellTreeNode) {
		
		Node childNode = PopulationWorkSpaceConstants.subTreeLookUpNode
				.get(subTreeCellTreeNode.getName() + "~" + subTreeCellTreeNode.getUUID());
		CellTreeNode subTreeRefCellTreeNode = null;
		NamedNodeMap namedNodeMap = childNode.getAttributes();
		if (namedNodeMap.getNamedItem("instance") != null) {
			String instanceOfUUID = namedNodeMap.getNamedItem("instanceOf").getNodeValue();
			String instanceOfDisplayName = namedNodeMap.getNamedItem("displayName").getNodeValue();
			Node occurenceNode = PopulationWorkSpaceConstants.subTreeLookUpNode
					.get(instanceOfDisplayName + "~" + instanceOfUUID);
			subTreeRefCellTreeNode = XmlConversionlHelper
					.createCellTreeNode(occurenceNode, instanceOfDisplayName);
		} else {
			subTreeRefCellTreeNode = XmlConversionlHelper
					.createCellTreeNode(childNode, subTreeCellTreeNode.getName());
		}
		if((subTreeRefCellTreeNode.getChilds() != null) && (subTreeRefCellTreeNode.getChilds().size() > 0)
				&& !isValid()){
			for(CellTreeNode node:subTreeRefCellTreeNode.getChilds()){
				validateSubtreeNodeAtPopulation(node);
				
			}
		}
		
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#validateCellTreeNodes
	 * (com.google.gwt.user.cellview.client.TreeNode)
	 */
	@Override
	public void validateCellTreeNodes(TreeNode treeNode , boolean isValidateButtonClicked) {
		validateClauseWorkspaceCellTreeNodes(treeNode , isValidateButtonClicked);
	}
	/**
	 * Validate clause workspace cell tree nodes.
	 *
	 * @param treeNode the tree node
	 * @param isValidateButtonClicked the is validate button clicked
	 */
	private void validateClauseWorkspaceCellTreeNodes(final TreeNode treeNode , final boolean isValidateButtonClicked) {
		List<String> inValidNodeList = new ArrayList<String>();
		if (treeNode != null) {
			openAllNodes(treeNode);
			for (int i = 0; i < treeNode.getChildCount(); i++) {
				CellTreeNode node = (CellTreeNode) treeNode.getChildValue(i);
				validateClauseWorkspaceCellTreeNodes(node, PopulationWorkSpaceConstants.getDatatypeMap(), inValidNodeList);
				if((node != null) && node.hasChildren() && (inValidNodeList.size() == 0)) {
					validateClauseNodeNesting(node.getChilds().get(0),inValidNodeList,1);
				}
			}
		}
		closeNodes(getXmlTree().getRootTreeNode());
		openAllNodes(getXmlTree().getRootTreeNode());
		setValidHumanReadable(true);
		getErrorMessageDisplay().clear();
		if (isValidateButtonClicked) {
			List<String> warningMessages = new ArrayList<String>();
			if (inValidNodeList.size() == 0) {
				getSuccessMessageDisplay().setMessage(
						MatContext.get().getMessageDelegate().
						getCLAUSE_WORK_SPACE_VALIDATION_SUCCESS());
			} else {
				for (String element: inValidNodeList) {
					if (element.equalsIgnoreCase("inValidAtTimingRelationShip")) {
						warningMessages.add(MatContext.get().getMessageDelegate().getLHS_RHS_REQUIRED());
					} else if (element.equalsIgnoreCase("inValidAtSetoperatorAndOrFunction")) {
						warningMessages.add(MatContext.get().getMessageDelegate().getATLEAST_ONE_CHILD_REQUIRED());
					} else if (element.equalsIgnoreCase("invalidNeed2Children")) {
						warningMessages.add(MatContext.get().getMessageDelegate().getAT_LEAST_TWO_CHILDREN_REQUIRED());
					} else if (element.equalsIgnoreCase("invalidNeed3Children")) {
						warningMessages.add(MatContext.get()
								.getMessageDelegate().getAT_LEAST_THREE_CHILDREN_REQUIRED());
					} else if (element.equalsIgnoreCase("invalidSATLhsChild")) {
						warningMessages.add(MatContext.get().getMessageDelegate()
								.getCLAUSE_WORK_SPACE_INVALID_ATTRIBUTE_IN_SAT_ALL_ANY());
					} else if (element.equalsIgnoreCase("invalidClauseLogic")) {
						warningMessages.add(MatContext.get().getMessageDelegate()
								.getCLAUSE_WORK_SPACE_INVALID_NESTED_CLAUSE());
					} else if (element.equalsIgnoreCase("nestedClauseLogic")) {
						warningMessages.add(MatContext.get().getMessageDelegate()
								.getCLAUSE_WORK_SPACE_INVALID_NESTED_DEPTH_CLAUSE());
					} else if (element.equalsIgnoreCase("emptyClauseLogic")) {
						warningMessages.add(MatContext.get().getMessageDelegate()
								.getCLAUSE_EMPTY());
					} else if(element.equalsIgnoreCase("invalidChildsATFunction")){
						warningMessages.add(MatContext.get().getMessageDelegate()
								.getONLY_ONE_CHILD_REQUIRED());
					}
					
				}
				if (warningMessages.size() > 0) {
					if (!warningMessages.get(0).equalsIgnoreCase(MatContext.get().getMessageDelegate()
							.getMEASURE_LOGIC_IS_INCOMPLETE())) {
						warningMessages.add(0, MatContext.get().getMessageDelegate()
								.getMEASURE_LOGIC_IS_INCOMPLETE());
					}
					getWarningMessageDisplay().setMessages(warningMessages);
				}
			}
		} else {
			if (inValidNodeList.size() == 0) {
				setValidHumanReadable(true);
			} else {
				setValidHumanReadable(false);
				errorMessageDisplay.setMessage(MatContext.get().getMessageDelegate().getINVALIDLOGIC_CLAUSE_WORK_SPACE());
			}
		}
	}
	/**
	 * Validate clause workspace cell tree nodes.
	 *
	 * @param treeNode the tree node
	 * @param dataTypeMap the data type map
	 * @param inValidNodeList the in valid node list
	 * @return the string
	 */
	private  List<String> validateClauseWorkspaceCellTreeNodes(CellTreeNode treeNode, Map<String,
			List<String>> dataTypeMap, List<String> inValidNodeList) {
		
		
		//String dateTimeDiff = "DateTimeDiff";
		if (treeNode != null) {
			CellTreeNode node = treeNode;
			switch (node.getNodeType()) {
				case CellTreeNode.ELEMENT_REF_NODE :
					validateClauseWorkspaceElementRefNode(node, dataTypeMap, inValidNodeList );
					break;
				case CellTreeNode.TIMING_NODE:
				case CellTreeNode.RELATIONSHIP_NODE:
					validateClauseWorkspaceTimingAndRelNode(node, inValidNodeList);
					break;
				case CellTreeNode.SET_OP_NODE:
					validateClauseWorkspaceForSetOpNode(inValidNodeList, node);
					break;
				case CellTreeNode.FUNCTIONS_NODE:
					validateClauseWorkspaceFunctionNode(inValidNodeList, node);
					break;
				case CellTreeNode.SUBTREE_REF_NODE:
					validateClauseWorkspaceSubTreeRefNode(inValidNodeList, node);
					
					break;
				case CellTreeNode.SUBTREE_NODE:
					//validation for empty clause logic in clauseWorkspace
					validateClauseWorkspaceSubTreeNode(inValidNodeList, node);
					
					break;
				default:
					break;
			}
			List<CellTreeNode> children = treeNode.getChilds();
			if ((children != null) && (children.size() > 0)) {
				for (CellTreeNode childNode:children) {
					validateClauseWorkspaceCellTreeNodes(childNode,
							PopulationWorkSpaceConstants.getDatatypeMap(), inValidNodeList);
				}
			}
		}
		return inValidNodeList;
	}
	
	/**
	 * Validate clause workspace sub tree node.
	 *
	 * @param inValidNodeList the in valid node list
	 * @param node the node
	 */
	private void validateClauseWorkspaceSubTreeNode(
			List<String> inValidNodeList, CellTreeNode node) {
		if((node.getChilds() == null) || (node.getChilds().size() == 0)){
			if (!inValidNodeList.contains("emptyClauseLogic")) {
				inValidNodeList.add("emptyClauseLogic");
			}
			editNode(false, node);
		}
	}
	
	/**
	 * Validate clause workspace sub tree ref node.
	 *
	 * @param inValidNodeList the in valid node list
	 * @param node the node
	 */
	private void validateClauseWorkspaceSubTreeRefNode(
			List<String> inValidNodeList, CellTreeNode node) {
		boolean checkForValidation = validateSubTreeRefNode(node);
		if (checkForValidation) {
			if (!inValidNodeList.contains("invalidClauseLogic")) {
				inValidNodeList.add("invalidClauseLogic");
			}
			editNode(false, node);
		}
	}
	
	/**
	 * Validate clause workspace function node.
	 *
	 * @param inValidNodeList the in valid node list
	 * @param node the node
	 */
	@SuppressWarnings("unchecked")
	private void validateClauseWorkspaceFunctionNode(
			List<String> inValidNodeList, CellTreeNode node) {
		String satisfiesAll = "SATISFIES ALL";
		String satisfiesAny = "SATISFIES ANY";
		int checkChildCount = 1;
		String invalidKeyForMap = "inValidAtSetoperatorAndOrFunction";
		Map<String, String> map = (Map<String, String>) node.getExtraInformation("extraAttributes");
		String funcType;
		if (map!=null) {
			funcType = map.get("type");
		} else {
			funcType = node.getName();
		}
		if ((node.getName().equalsIgnoreCase(satisfiesAll))
				|| (node.getName().equalsIgnoreCase(satisfiesAny))) {
			checkChildCount = 3;
			invalidKeyForMap = "invalidNeed3Children";
		} else if (node.getName().contains(MatConstants.DATETIMEDIFF)) {
			checkChildCount = 2;
			invalidKeyForMap = "invalidNeed2Children";
		} else if (!MatContext.get().functions.contains(capWords(funcType))) {
			invalidKeyForMap = "invalidAtFunction";
			editNode(false, node);
			if (!inValidNodeList.contains(invalidKeyForMap)) {
				inValidNodeList.add(invalidKeyForMap);
			}
		}
		
		if (((node.getChilds() != null) && (node.getChilds().size() >= checkChildCount))) {
			if ((node.getName().equalsIgnoreCase(satisfiesAll))
					|| (node.getName().equalsIgnoreCase(satisfiesAny))) {
				CellTreeNode lhsQdmNode = node.getChilds().get(0);
				List<CellTreeNode> lhsQdmNodeAttributeList = (List<CellTreeNode>)
						lhsQdmNode.getExtraInformation("attributes");
				if ((lhsQdmNodeAttributeList != null) && (lhsQdmNodeAttributeList.size() > 0)) {
					invalidKeyForMap = "invalidSATLhsChild";
					editNode(false, node);
					if (!inValidNodeList.contains(invalidKeyForMap)) {
						inValidNodeList.add(invalidKeyForMap);
					}
				} else {
					if (!node.getValidNode()
							&& !inValidNodeList.contains("invalidSATLhsChild")) {
						editNode(true, node);
					}
				}
			} else if(MatContext.get().functions.contains(capWords(funcType))
					&& !(node.getName().contains(MatConstants.DATETIMEDIFF))
					&& (node.getChilds().size() != checkChildCount)){
				invalidKeyForMap = "invalidChildsATFunction";
				editNode(false, node);
				if (!inValidNodeList.contains(invalidKeyForMap)) {
					inValidNodeList.add(invalidKeyForMap);
				}
			} else {
				if (!node.getValidNode()
						&& !inValidNodeList.contains("invalidAtFunction")) {
					editNode(true, node);
				}
			}
		} else {
			editNode(false, node);
			if (!inValidNodeList.contains(invalidKeyForMap)) {
				inValidNodeList.add(invalidKeyForMap);
			}
		}
	}
	
	/**
	 * Validate clause workspace for set op node.
	 *
	 * @param inValidNodeList the in valid node list
	 * @param node the node
	 */
	private void validateClauseWorkspaceForSetOpNode(
			List<String> inValidNodeList, CellTreeNode node) {
		String intersection = "INTERSECTION";
		String union = "Union";
		int checkOpChildCount = 0;
		String invalidKeyForOpMap = null;
		if ((node.getName().equalsIgnoreCase(intersection)) ||
				(node.getName().equalsIgnoreCase(union))) {
			checkOpChildCount = 2;
			invalidKeyForOpMap = "invalidNeed2Children";
		}
		if (((node.getChilds() != null) && (node.getChilds().size() >= checkOpChildCount))) {
			if (!node.getValidNode()) {
				editNode(true, node);
			}
		} else {
			editNode(false, node);
			if ((invalidKeyForOpMap != null) && !inValidNodeList.contains(invalidKeyForOpMap)) {
				inValidNodeList.add(invalidKeyForOpMap);
			}
		}
	}
	
	
	/**
	 * Method to convert String into Sentence/Title Case.
	 *
	 * @param strToConvert the str to convert
	 * @return Sentence Case String
	 */
	@SuppressWarnings("null")
	private String capWords(String strToConvert) {
		if ((strToConvert == null) && strToConvert.isEmpty()) {
			return strToConvert;
		} else {
			StringBuilder sb = new StringBuilder();
			for (String token : strToConvert.split(" ")) {
				if (token.isEmpty()) {
					if (sb.length() > 0) {
						sb.append(" ");
					}
				} else {
					if (sb.length() > 0) {
						sb.append(" ");
					}
					sb.append(Character.toUpperCase(token.charAt(0)));
					if (token.length() > 1) {
						sb.append(token.substring(1).toLowerCase());
					}
				}
			}
			return sb.toString();
		}
	}
	
	/**
	 * Validate clause node nesting.
	 *
	 * @param treeNode the tree node
	 * @param inValidNodeList the in valid node list
	 * @param counter the counter
	 * @return true, if successful
	 */
	private boolean validateClauseNodeNesting(CellTreeNode treeNode , List<String> inValidNodeList, int counter) {
		//List <Node> subTreeRefList = new ArrayList<Node>();
		//Node subTreeNode = PopulationWorkSpaceConstants.getSubTreeLookUpNode().get(treeNode.getName() + "~" + treeNode.getUUID());
		//findAllSubTreeRefsInNode(subTreeNode, subTreeRefList);
		//boolean isValidDepth = validateNestedSubTreeDepth(treeNode, subTreeRefList , counter, false);
		boolean isValidDepth = false;
		if(treeNode != null) {
			editNode(true, treeNode);
			isValidDepth = findChildCount(treeNode, 0, false);
			if (isValidDepth) {
				/*editNode(!isValidDepth, treeNode);*/
				if (!inValidNodeList.contains("nestedClauseLogic")) {
					inValidNodeList.add("nestedClauseLogic");
				}
			}
		}
		return isValidDepth;
	}
	
	/**
	 * Find child count.
	 *
	 * @param treeNode the tree node
	 * @param counter the counter
	 * @param flag the flag
	 * @return true, if successful
	 *//*
	private boolean validateNestedSubTreeDepth(CellTreeNode treeNode,List<Node> subTreeRefList, int counter , boolean isValidDepth) {
		Node parentNode = null;
		for (int i = 0; ((i < subTreeRefList.size()) && !isValidDepth); i++) {
			Node subTreeNode = subTreeRefList.get(i);
			int currentCounter = counter;
			if(currentCounter == 1){
				parentNode = subTreeNode;
			}
			if(subTreeNode != null) {
				NamedNodeMap namedNodeMap = subTreeNode.getAttributes();
				String displayName = namedNodeMap.getNamedItem("displayName").getNodeValue();
				String uuid = namedNodeMap.getNamedItem("id").getNodeValue();
				if (namedNodeMap.getNamedItem("instance") != null) {
					uuid = namedNodeMap.getNamedItem("instanceOf").getNodeValue();
					displayName = namedNodeMap.getNamedItem("displayName").getNodeValue();
				}
				Node node  = PopulationWorkSpaceConstants.getSubTreeLookUpNode().get(displayName + "~" + uuid);
				List <Node> subTreeNodeRefList = new ArrayList<Node>();
				findAllSubTreeRefsInNode(node, subTreeNodeRefList);
				if(subTreeNodeRefList.size() > 0){
					currentCounter = currentCounter +1;
				}
				if (currentCounter > 10) {
					isValidDepth = true;
					break;
				} else {
					isValidDepth = validateNestedSubTreeDepth(treeNode,subTreeNodeRefList ,currentCounter, isValidDepth);
				}
			}
		}
		if(isValidDepth && (parentNode != null)) {
			CellTreeNode nodeToEdit = findCellTreeNode(treeNode, parentNode);
			if(nodeToEdit != null){
				editNode(false, nodeToEdit);
			}
			
		}
		return isValidDepth;
	}*/
	
	/**
	 * This method finds CellTreeNode for Node from treeNode.
	 * @param treeNode - CellTreeNode - top Node.
	 * @param parentNode - Node - to be located in top Node.
	 * @return CellTreeNode.
	 */
	/*private CellTreeNode findCellTreeNode(CellTreeNode treeNode, Node parentNode) {
		String uuid = parentNode.getAttributes().getNamedItem("id").getNodeValue();
		CellTreeNode node = null;
		if (treeNode != null) {
			List<CellTreeNode> children = treeNode.getChilds();
			if ((children != null) && (children.size() > 0)) {
				for (CellTreeNode childNode:children) {
					if (childNode.getUUID().equals(uuid)) {
						node = childNode;
						break;
					} else {
						if (childNode.hasChildren()) {
							node = findCellTreeNode(childNode, parentNode);
						}
					}
				}
			}
		}
		return node;
	}*/
	/**
	 * This Method finds all the SubTreeRef nodes present in treeNode.
	 * @param treeNode - Node
	 * @param subTreeRefNodeList - List of Nodes.
	 *//*
	private void findAllSubTreeRefsInNode(Node treeNode, List<Node> subTreeRefNodeList){
		if (treeNode != null) {
			switch (treeNode.getNodeName()) {
				case "subTreeRef":
					subTreeRefNodeList.add(treeNode);
					break;
				default:
					break;
			}
			NodeList children = treeNode.getChildNodes();
			if ((children != null) && (children.getLength() > 0)) {
				for (int i = 0; i < children.getLength(); i++) {
					findAllSubTreeRefsInNode(children.item(i), subTreeRefNodeList);
				}
			}
		}
	}*/
	
	/**
	 * Method to find out depth of Node.
	 * @param treeNode - Node
	 * @param counter - Int
	 * @param flag - Boolean
	 * @return - Boolean
	 */
	private boolean findChildCount(CellTreeNode treeNode, int counter, boolean flag) {
		List<CellTreeNode> children = treeNode.getChilds();
		CellTreeNode childNode = null;
		if ((children != null) && (children.size() > 0)) {
			for (int i = 0; (i < children.size()) && !flag; i++) {
				int currentCounter = counter;
				CellTreeNode subTreeCellTreeNode = children.get(i);
				childNode = subTreeCellTreeNode;
				String uuid = subTreeCellTreeNode.getUUID();
				if (subTreeCellTreeNode.getNodeType() == CellTreeNode.SUBTREE_REF_NODE) {
					String displayName = subTreeCellTreeNode.getName();
					Node node = PopulationWorkSpaceConstants.subTreeLookUpNode
							.get(displayName + "~" + uuid);
					if (node.getAttributes().getNamedItem("instance") != null) {
						uuid = node.getAttributes().getNamedItem("instanceOf").getNodeValue();
						displayName = node.getAttributes().getNamedItem("displayName").getNodeValue();
					}
					node = PopulationWorkSpaceConstants.getSubTreeLookUpNode().get(displayName + "~" + uuid);
					CellTreeNode subTreeReferenceNode  = XmlConversionlHelper
							.createCellTreeNode(node, displayName);
					if (subTreeReferenceNode.hasChildren()) {
						subTreeCellTreeNode =  subTreeReferenceNode.getChilds().get(0);
					}
				}
				if ((subTreeCellTreeNode.getChilds() != null) && (subTreeCellTreeNode.getChilds().size() > 0)
						&& (subTreeCellTreeNode.getNodeType() != CellTreeNode.SUBTREE_NODE)) {
					currentCounter = currentCounter + 1;
					System.out.println("Current Counter Value ========" + currentCounter + " For Node :: " + subTreeCellTreeNode.getName());
				}
				if (currentCounter > CLAUSE_NESTED_DEPTH) {
					flag = true;
					break;
				} else {
					flag = findChildCount(subTreeCellTreeNode, currentCounter, flag);
				}
			}
		}
		if (flag && (childNode != null)) {
			editNode(!flag, childNode);
		}
		return flag;
	}
	
	/**
	 * Validate node for old birth date and expired element.
	 *
	 * @param nodeDataType the node data type
	 * @param node the node
	 * @param inValidNodeList the in valid node list
	 */
	private void validateNodeForOldBirthDateAndExpiredElement(
			String nodeDataType, CellTreeNode node, List<String> inValidNodeList) {
		String qdmName = PopulationWorkSpaceConstants.getElementLookUpName().get(
				node.getUUID());
		Node qdmNode = PopulationWorkSpaceConstants.getElementLookUpNode().get(
				qdmName + "~" + node.getUUID());
		Node oid = qdmNode.getAttributes().getNamedItem("oid");
		String  oidValue = oid.getNodeValue().trim();
		if (oidValue.equalsIgnoreCase(ConstantMessages.DEAD_OID)
				|| oidValue.equalsIgnoreCase(ConstantMessages.BIRTHDATE_OID)) {
			if (!node.getValidNode()) {
				editNode(true, node);
			}
		} else {
			inValidAtQdmNode(node, inValidNodeList);
		}
	}
	
	/**
	 * In valid at qdm node.
	 *
	 * @param node the node
	 * @param inValidNodeList the in valid node list
	 */
	protected void inValidAtQdmNode(CellTreeNode node, List<String> inValidNodeList) {
		editNode(false, node);
		if(!inValidNodeList.contains("inValidAtQDMNode")){
			inValidNodeList.add("inValidAtQDMNode");
		}
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
			selectionModel.setSelected(selectedNode.getParent(), true);
			// This is done to invoke focus event on Parent node to show Inline comment in Comment Area
			//when Edit Operation is Performed on ClauseWorspace or PopulationWorkspace.
			((NodeCell) getNodeInfo(selectedNode.getParent()).getCell()).
			onBrowserEvent(new Context(0, 0, null), null, selectedNode.getParent(), Document.get().createFocusEvent(), null);
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#editNode(boolean, mat.client.clause.clauseworkspace.model.CellTreeNode, com.google.gwt.user.cellview.client.TreeNode)
	 */
	@Override
	public void editNode(boolean isValideNodeValue, CellTreeNode node) {
		node.setValidNode(isValideNodeValue);
		selectedNode = node;
		//closeParentOpenNodes(cellTree.getRootTreeNode());
	}
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#addCommentNodeToSelectedNode()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void addCommentNodeToSelectedNode() {
		if (((getSelectedNode() != null))) {
			if ((getSelectedNode().getNodeType() == CellTreeNode.LOGICAL_OP_NODE)
					|| (getSelectedNode().getNodeType() == CellTreeNode.SUBTREE_REF_NODE)) {
				List<CellTreeNode> nodeCommentList = (List<CellTreeNode>) getSelectedNode().getExtraInformation(COMMENT);
				if (nodeCommentList == null) {
					nodeCommentList = new ArrayList<CellTreeNode>();
				}
				nodeCommentList.clear();
				CellTreeNode commentNode = new CellTreeNodeImpl();
				commentNode.setName(PopulationWorkSpaceConstants.COMMENT_NODE_NAME);
				commentNode.setNodeType(CellTreeNode.COMMENT_NODE);
				commentNode.setNodeText(getCommentArea().getText());
				nodeCommentList.add(commentNode);
				getSelectedNode().setExtraInformation(COMMENT, nodeCommentList);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#getWarningMessageDisplay()
	 */
	@Override
	public WarningMessageDisplay getWarningMessageDisplay() {
		return warningMessageDisplay;
	}
	/**
	 * Gets the button expand clause work space.
	 *
	 * @return the buttonExpandClauseWorkSpace
	 */
	@Override
	public Button getButtonExpandClauseWorkSpace() {
		return buttonExpandClauseWorkSpace;
	}
	
	/**
	 * Sets the button expand clause work space.
	 *
	 * @param buttonExpandClauseWorkSpace the buttonExpandClauseWorkSpace to set
	 */
	public void setButtonExpandClauseWorkSpace(Button buttonExpandClauseWorkSpace) {
		this.buttonExpandClauseWorkSpace = buttonExpandClauseWorkSpace;
	}
	
	/**
	 * Gets the button collapse clause work space.
	 *
	 * @return the buttonCollapseClauseWorkSpace
	 */
	@Override
	public Button getButtonCollapseClauseWorkSpace() {
		return buttonCollapseClauseWorkSpace;
	}
	
	/**
	 * Sets the button collapse clause work space.
	 *
	 * @param buttonCollapseClauseWorkSpace the buttonCollapseClauseWorkSpace to set
	 */
	public void setButtonCollapseClauseWorkSpace(Button buttonCollapseClauseWorkSpace) {
		this.buttonCollapseClauseWorkSpace = buttonCollapseClauseWorkSpace;
	}
	
	/**
	 * Gets the save btn clause work space.
	 *
	 * @return the saveBtnClauseWorkSpace
	 */
	@Override
	public Button getSaveBtnClauseWorkSpace() {
		return saveBtnClauseWorkSpace;
	}
	
	/**
	 * Sets the save btn clause work space.
	 *
	 * @param saveBtnClauseWorkSpace the saveBtnClauseWorkSpace to set
	 */
	public void setSaveBtnClauseWorkSpace(Button saveBtnClauseWorkSpace) {
		this.saveBtnClauseWorkSpace = saveBtnClauseWorkSpace;
	}
	
	/**
	 * Gets the validate btn clause work space.
	 *
	 * @return the validateBtnClauseWorkSpace
	 */
	@Override
	public Button getValidateBtnClauseWorkSpace() {
		return validateBtnClauseWorkSpace;
	}
	
	/**
	 * Sets the validate btn clause work space.
	 *
	 * @param validateBtnClauseWorkSpace the validateBtnClauseWorkSpace to set
	 */
	public void setValidateBtnClauseWorkSpace(Button validateBtnClauseWorkSpace) {
		this.validateBtnClauseWorkSpace = validateBtnClauseWorkSpace;
	}
	
	/**
	 * Gets the clear clause work space.
	 *
	 * @return the clearClauseWorkSpace
	 */
	@Override
	public Button getClearClauseWorkSpace() {
		return clearClauseWorkSpace;
	}
	
	/**
	 * Sets the clear clause work space.
	 *
	 * @param clearClauseWorkSpace the clearClauseWorkSpace to set
	 */
	public void setClearClauseWorkSpace(Button clearClauseWorkSpace) {
		this.clearClauseWorkSpace = clearClauseWorkSpace;
	}
	
	/**
	 * Sets the comments box read only.
	 *
	 * @param isReadOnly - boolean.
	 */
	private void setCommentsBoxReadOnly(boolean isReadOnly) {
		commentArea.setReadOnly(isReadOnly);
		commentButtons.setEnabled(!isReadOnly);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#getClearErrorDisplay()
	 */
	@Override
	public ErrorMessageDisplay getClearErrorDisplay() {
		return clearErrorDisplay;
	}
	
	/**
	 * Gets the comment buttons.
	 *
	 * @return the commentButtons
	 */
	@Override
	public Button getCommentButtons() {
		return commentButtons;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#getShowClauseButton()
	 */
	@Override
	public Button getShowClauseButton() {
		return openClauseButton;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#getDeleteClauseButton()
	 */
	@Override
	public Button getDeleteClauseButton() {
		return deleteClauseButton;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#getClauseNamesListBox()
	 */
	@Override
	public ListBox getClauseNamesListBox() {
		return subTreeNameListBox;
	}
	
	
	/**
	 * Gets the comment area.
	 *
	 * @return the commentArea
	 */
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#getCommentArea()
	 */
	@Override
	public CommentAreaTextBox getCommentArea() {
		return commentArea;
	}
	
	//added by hari
	/**
	 * Gets the validate btn population workspace.
	 *
	 * @return the ValidateBtnPopulationWorkspace
	 */
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#getValidateBtnPopulationWorkspace()
//	 */
//	@Override
//	public Button getValidateBtnPopulationWorkspace() {
//		
//		return validateBtnPopulationWorkspace;
//	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#setClauseEnabled(boolean)
	 */
	@Override
	public void setClauseEnabled(boolean isClauseOpen) {
		this.isClauseOpen = isClauseOpen;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#getIncludeQdmVaribale()
	 */
	@Override
	public CheckBox getIncludeQdmVaribale() {
		return includeQdmVaribale;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#setQdmVariable(boolean)
	 */
	@Override
	public void setQdmVariable(String isQdmVariable) {
		this.isQdmVariable = isQdmVariable;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#isQdmVariable()
	 */
	@Override
	public String isQdmVariable() {
		return isQdmVariable;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#isQdmVariableDirty()
	 */
	@Override
	public boolean isQdmVariableDirty() {
		return isQdmVariableDirty;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#setQdmVariableDirty(boolean)
	 */
	@Override
	public void setQdmVariableDirty(boolean isQdmVariableDirty) {
		this.isQdmVariableDirty = isQdmVariableDirty;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#getSearchSuggestTextBox()
	 */
	@Override
	public SuggestBox getSearchSuggestTextBox() {
		return searchSuggestTextBox;
	}
	
	/**
	 * Checks if is valid.
	 *
	 * @return true, if is valid
	 */
	public boolean isValid() {
		return isValid;
	}
	
	/**
	 * Sets the valid.
	 *
	 * @param isValid the new valid
	 */
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	
	/**
	 * Checks if is measure observations.
	 *
	 * @return true, if is measure observations
	 */
	public boolean isMeasureObservations() {
		return isMeasureObservations;
	}
	
	/**
	 * Sets the measure observations.
	 *
	 * @param isMeasureObservations the new measure observations
	 */
	public void setMeasureObservations(boolean isMeasureObservations) {
		this.isMeasureObservations = isMeasureObservations;
	}
	
	/**
	 * Checks if is valid human readable.
	 *
	 * @return the isValidHumanReadable
	 */
	@Override
	public boolean isValidHumanReadable() {
		return isValidHumanReadable;
	}
	
	/**
	 * Sets the valid human readable.
	 *
	 * @param isValidHumanReadable the isValidHumanReadable to set
	 */
	public void setValidHumanReadable(boolean isValidHumanReadable) {
		this.isValidHumanReadable = isValidHumanReadable;
	}
	
	/**
	 * Validate clause workspace element ref node.
	 *
	 * @param node the node
	 * @param dataTypeMap the data type map
	 * @param inValidNodeList the in valid node list
	 */
	private void validateClauseWorkspaceElementRefNode(CellTreeNode node,
			Map<String, List<String>> dataTypeMap, List<String> inValidNodeList){
		
		String timingElement = "timing element";
		String timingElementMeasurementEndDate = "Measurement End Date : Timing Element";
		String timingElementMeasurementStartDate = "Measurement Start Date : Timing Element";
		String patientCharBirthDate = "Patient characteristic Birthdate";
		String patientCharExpired = "Patient characteristic Expired";
		String attributeValue = "";
		
		String nodeName = node.getName();
		@SuppressWarnings("unchecked")
		List<CellTreeNode> attributeList = (List<CellTreeNode>)
				node.getExtraInformation("attributes");
		if ((attributeList != null) && (attributeList.size() > 0)) {
			CellTreeNode attributeNode = attributeList.get(0);
			attributeValue = attributeNode.getExtraInformation("name").toString();
		}
		String nodeDataType = PopulationWorkSpaceConstants.getElementLookUpDataTypeName()
				.get(node.getUUID());
		if (nodeDataType.equalsIgnoreCase(timingElement)) {
			if (nodeName.equalsIgnoreCase(timingElementMeasurementEndDate)
					|| nodeName.equalsIgnoreCase(timingElementMeasurementStartDate)) {
				inValidAtQdmNode(node, inValidNodeList);
			} else {
				if (!node.getValidNode()) {
					editNode(true, node);
				}
			}
		} else {
			if (nodeDataType.equalsIgnoreCase(patientCharBirthDate)
					|| nodeDataType.equalsIgnoreCase(patientCharExpired)) {
				validateNodeForOldBirthDateAndExpiredElement(nodeDataType,
						node, inValidNodeList);
			} else if (attributeValue.isEmpty()) {
				if (!dataTypeMap.containsKey(nodeDataType)) {
					inValidAtQdmNode(node, inValidNodeList);
				} else { //Removed attributes when
					//replaced with blank still showing red.Added else to set text to black.
					if (!node.getValidNode()) {
						editNode(true, node);
					}
				}
			} else if (!attributeValue.isEmpty() && (attributeValue.length() > 0)) {
				if (!dataTypeMap.containsKey(nodeDataType)) {
					inValidAtQdmNode(node, inValidNodeList);
				} else {
					List<String> attribList = dataTypeMap.get(nodeDataType);
					if (!attribList.contains(attributeValue)) {
						inValidAtQdmNode(node, inValidNodeList);
					} else { //Removed attributes when
						//replaced with valid attributes still showing red.
						//Added else to set text to black.
						if (!node.getValidNode()) {
							editNode(true, node);
						}
					}
				}
			} else {
				if (!node.getValidNode()) {
					editNode(true, node);
				}
			}
		}
	}
	
	/**
	 * Validate clause workspace timing and rel node.
	 *
	 * @param node the node
	 * @param inValidNodeList the in valid node list
	 */
	private void validateClauseWorkspaceTimingAndRelNode(CellTreeNode node, List<String> inValidNodeList) {
		if (((node.getChilds() != null) && (node.getChilds().size() == 2))) {
			if (!node.getValidNode() && (MatContext.get().relationships.contains(node.getName())
					|| (node.getNodeType() == CellTreeNode.TIMING_NODE))) {
				editNode(true, node);
			} else if (!MatContext.get().relationships.contains(node.getName())
					&& (node.getNodeType() != CellTreeNode.TIMING_NODE)) {
				editNode(false, node);
				if (!inValidNodeList.contains("inValidAtRelationshipNode")) {
					inValidNodeList.add("inValidAtRelationshipNode");
				}
			}
		} else {
			editNode(false, node);
			if (!inValidNodeList.contains("inValidAtTimingRelationShip")) {
				inValidNodeList.add("inValidAtTimingRelationShip");
			}
		}
		
	}
	
}
