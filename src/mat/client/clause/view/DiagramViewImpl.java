package mat.client.clause.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import mat.client.Enableable;
import mat.client.clause.AppController;
import mat.client.clause.ClauseController;
import mat.client.clause.diagram.Diagram;
import mat.client.clause.diagram.TraversalTree;
import mat.client.clause.event.PropertyEditorArranger;
import mat.client.clause.view.shape.DiagramShapeFactory;
import mat.client.clause.view.shape.SquareConnector;
import mat.client.diagramObject.Conditional;
import mat.client.diagramObject.Criterion;
import mat.client.diagramObject.CriterionParent;
import mat.client.diagramObject.DiagramObject;
import mat.client.diagramObject.DiagramObjectFactory;
import mat.client.diagramObject.InsertPhrase;
import mat.client.diagramObject.Phrase;
import mat.client.diagramObject.PlaceHolder;
import mat.client.diagramObject.Qdsel;
import mat.client.diagramObject.Rel;
import mat.client.diagramObject.SimpleStatement;
import mat.client.event.MATChangeHandler;
import mat.client.event.MATClickHandler;
import mat.client.event.MeasureSelectedEvent;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.FocusableWidget;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.MatTabLayoutPanel;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.search.MATAnchor;
import mat.client.shared.ui.MATTabPanel;
import mat.model.clause.Clause;
import mat.shared.Attribute;
import mat.shared.ConstantMessages;
import mat.shared.model.ClauseLT;

import org.vaadin.gwtgraphics.client.DrawingArea;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class DiagramViewImpl<T> extends Composite implements DiagramView<T> {
	//field used to indicate source of invoking class (JavaScript translation does not support this.getClass().getName())
	private static final String CLASS_NAME = "DiagramViewImpl";
	private static final String ARROW_RIGHT = "\u25b6";
	private static final String ARROW_DOWN = "\u25bc";
	private static final String ARROW_LEFT = "\u25c0";

	@SuppressWarnings("unchecked")

	@UiTemplate("DiagramView.ui.xml")
	interface DiagramViewUiBinder extends UiBinder<Widget, DiagramViewImpl> {}
	protected static DiagramViewUiBinder uiBinder = GWT.create(DiagramViewUiBinder.class);

	public static final int CLAUSE_LIBRARY_TAB = 0;
	public static final int MEASURE_PHRASE_TAB = 1;
	public static enum PROPERTY_GRID_STATE {INSERT, SELECT};
    private boolean savedStatus;
	

	DiagramView<?> me;
	AppController appController;
	ClauseController clauseController;

	@UiField FocusPanel diagramFocus;
	@UiField VerticalPanel canvasViewPanel;
	@UiField FocusPanel stackFocusPanel;
	
	MatTabLayoutPanel criterionPanel;
	
	ScrollPanel populationCanvasPanel = new ScrollPanel();
	ScrollPanel numeratorCanvasPanel = new ScrollPanel();
	ScrollPanel numeratorExCanvasPanel = new ScrollPanel();
	ScrollPanel denominatorCanvasPanel = new ScrollPanel();
	ScrollPanel exclusionsCanvasPanel = new ScrollPanel();
	ScrollPanel exceptionsCanvasPanel = new ScrollPanel();
	ScrollPanel measurePopCanvasPanel = new ScrollPanel();
	ScrollPanel measureObservCanvasPanel = new ScrollPanel();
	ScrollPanel stratificationCanvasPanel = new ScrollPanel();
	ScrollPanel otherCanvasPanel = new ScrollPanel();
	FlowPanel measurePhrasePanel = new FlowPanel();
	
	
	
	@UiField ScrollPanel textScrollPanel;
	TextArea measurePhraseTextArea1 = new TextArea();
	@UiField Label measurePhraseTextArea2;

	@UiField HorizontalPanel page1Buttons;
	@UiField HorizontalPanel page2Buttons;
	
 
	@UiField HorizontalPanel propertyEditor1Panel;
	Label propertyEditor1Label;
	FocusableWidget propertyEditor1Focus ;
	
	@UiField HorizontalPanel propertyEditor2Panel;
	Label propertyEditor2Label;
	FocusableWidget propertyEditor2Focus ;
	
	@UiField VerticalPanel propertyEditor1Top;
	@UiField VerticalPanel propertyEditor2Top;

	MATAnchor textViewButton1Inline = new MATAnchor("Text View");
	
	Anchor canvasViewButton2Inline = new Anchor("Canvas View");
	
	ScrollPanel[] scrollPanels;
	ScrollPanel currentScrollPanel;
	int currentStackIndex = 0;
	SelectionHandler<Integer> criterionStackSelectionHandler;

	@UiField SimplePanel qdsElementPanel; 
	@UiField HorizontalPanel buttonHolder;
	@UiField VerticalPanel saveHolder;
	@UiField SimplePanel messageHolder;

	@UiField VerticalPanel libraryVerticalPanel;
	MatTabLayoutPanel libraryTabPanel;
	private static final int CLAUSE_LBRARY_TAB = 0;
	private static final int MEASURE_PHRASE_LIBRARY_TAB = 1;
	int currentLibraryTab = 1;
	private ListBoxMVP clauseLibraryListBox;
	private ListBoxMVP measurePhraseListBox;
	
	@UiField HorizontalPanel errorMessageHolder;
	@UiField Button cloneButton;

	enum DIAGRAM_VIEW {CANVAS_VIEW, TEXT_VIEW};
	private DIAGRAM_VIEW diagramView = DIAGRAM_VIEW.CANVAS_VIEW;
	@UiField HorizontalPanel propertyEditor1;				// canvas view
	@UiField HorizontalPanel propertyEditor2;				// tree view
	@UiField ScrollPanel propertyEditorScrollPanel1;		// canvas view
	@UiField ScrollPanel propertyEditorScrollPanel2;		// tree view

	AttributeEditor attributeEditor;

	@UiField HorizontalPanel attributeEditorPanel1;
	@UiField HorizontalPanel attributeEditorHorizontalPanel1;
	@UiField HorizontalPanel attributePhraseNamePanel1;
	@UiField Label attributePhraseNameLabel1;
	@UiField VerticalPanel attributeTablePanel1;
	@UiField ListBox attributeList1;
	@UiField Button addAttributeButton1;
	@UiField ListBox attributeTypeList1;
	@UiField Button attributeUpdateTypeButton1;
	@UiField ListBox attributeComparisonList1;
	@UiField TextBox attributeQuantity1;
	@UiField ListBox attributeUnit1;
	@UiField Button attributeComparisonUpdateButton1;
	private ListBoxMVP attributeQDSElementList1;
	@UiField Button attributeQDSElementUpdateButton1;
	@UiField Button attributeEditorSaveButton1;
	@UiField Button attributeEditorCancelButton1;

	@UiField HTML attributeList1Label;
	@UiField HTML attributeTypeList1Label;
	@UiField HTML attributeComparisonList1Label;
	@UiField HTML attributeQuantity1Label;
	@UiField HTML attributeUnit1Label;
	@UiField HTML attributeQDSElementList1Label;
	
	@UiField HTML attributeList2Label;
	@UiField HTML attributeTypeList2Label;
	@UiField HTML attributeComparisonList2Label;
	@UiField HTML attributeQuantity2Label;
	@UiField HTML attributeUnit2Label;
	@UiField HTML attributeQDSElementList2Label;
	
	@UiField HorizontalPanel attributeEditorPanel2;
	@UiField HorizontalPanel attributeEditorHorizontalPanel2;
	@UiField HorizontalPanel attributePhraseNamePanel2;
	@UiField Label attributePhraseNameLabel2;
	@UiField VerticalPanel attributeTablePanel2;
	@UiField ListBox attributeList2;
	@UiField Button addAttributeButton2;
	@UiField ListBox attributeTypeList2;	
	@UiField Button attributeUpdateTypeButton2;
	@UiField ListBox attributeComparisonList2;
	@UiField TextBox attributeQuantity2;
	@UiField ListBox attributeUnit2;
	@UiField Button attributeComparisonUpdateButton2;
	private ListBoxMVP attributeQDSElementList2;
	@UiField Button attributeQDSElementUpdateButton2;
	@UiField Button attributeEditorSaveButton2;
	@UiField Button attributeEditorCancelButton2;


	Button saveButton = new Button("Save");
	@UiField VerticalPanel userDefinedPastedCriterionPanel;
	@UiField Label userDefinedPastedCriterionCaption;
	@UiField TextBox userDefinedPastedCriterionTextBox;
	@UiField Button userDefinedPastedCriterionOkButton;
	@UiField Button userDefinedPastedCriterionCancelButton;

	@UiField HorizontalPanel canvasViewToolbar;
	@UiField Label toolbarTitle1;
	@UiField Button criterionButton1;
	@UiField Button pasteCloneButton1;
	@UiField Button qdselButton1;
	@UiField Button andButton1;
	@UiField Button orButton1;
	@UiField Button timingButton1;
	@UiField Button addPhraseButton1;
	@UiField Button insertPhraseButton1;
	@UiField Button cutButton1;
	@UiField Button copyButton1;
	@UiField Button pasteButton1;
	@UiField Button deleteButton1;
	@UiField Button editButton1;

	@UiField HorizontalPanel textViewToolbar;
	@UiField Label toolbarTitle2;
	@UiField Button criterionButton2;
	@UiField Button addPhraseButton2;
	@UiField Button insertPhraseButton2;
	@UiField Button deleteButton2;
	@UiField Button editButton2;

	@UiField VerticalPanel textViewPanel;
	@UiField Button diagramTreeExpandButton;
	@UiField Button diagramTreeCollapseButton;
	Tree diagramTree;
	
	protected ErrorMessageDisplay propEditErrorMessages = new ErrorMessageDisplay();
	protected SuccessMessageDisplay propEditSuccessMessages = new SuccessMessageDisplay();
	protected ErrorMessageDisplay saveErrorMessages = new ErrorMessageDisplay();
	protected SuccessMessageDisplay saveSuccessMessages = new SuccessMessageDisplay();
	
	protected ClickHandler saveClickHandler;
	protected ClickHandler deleteClickHandler;
	protected ClickHandler criterionDeleteClickHandler;
	protected ClickHandler cancelClickHandler;

	protected Button currentCriterionButton;
	protected String currentCriterion = ConstantMessages.POPULATION_CONTEXT_DESC; 
	protected String[] criterionBeforeViewSwitch = new String[] {currentCriterion, currentCriterion, currentCriterion};
	protected DiagramObject currentDiagramObject;
	protected TraversalTree clipboardTraversalTree;
	protected DrawingArea canvas;
	protected FlexTable propertyGrid;

	protected Presenter<T> presenter;
	protected TraversalTree currentTraversalTree = null;
	protected Diagram diagram;
	protected boolean editable;
	
	private ClauseLT clauseToBeCloned = null;
	private ZoomCanvasDelegate zoomCanvasDelegate;
	
	private final int DEPTH_CRITERION_PARENT = 1;
	private final int DEPTH_CRITERION = 2;
	private final int DEPTH_TOP_LEVEL_AND = 3;
	private final int DEPTH_MEASURE_PHRASE = 4;
	
	private boolean ignoreCriterionPanelSelection = true;
	
	/*
	 * selection indicator for the text view diagram tree
	 */
	private final String INDICATOR = "\u25ba";
	
	public boolean isSaveStatus() {
		return savedStatus;
	}


	public void setSaveStatus(boolean saveStatus) {
		this.savedStatus = saveStatus;
	}
	@Override
	public void setEditable(boolean editable) {
		this.editable = editable;

		addAttributeButton1.setEnabled(editable);
		attributeUpdateTypeButton1.setEnabled(editable);
		attributeComparisonUpdateButton1.setEnabled(editable);
		attributeQDSElementUpdateButton1.setEnabled(editable);
		attributeEditorSaveButton1.setEnabled(editable);
		addAttributeButton2.setEnabled(editable);
		attributeUpdateTypeButton2.setEnabled(editable);
		attributeComparisonUpdateButton2.setEnabled(editable);
		attributeQDSElementUpdateButton2.setEnabled(editable);
		attributeEditorSaveButton2.setEnabled(editable);		
		
		criterionButton1.setEnabled(editable);
		criterionButton2.setEnabled(editable);
		qdselButton1.setEnabled(editable);
		andButton1.setEnabled(editable);
		orButton1.setEnabled(editable);
		timingButton1.setEnabled(editable);
		addPhraseButton1.setEnabled(editable);
		addPhraseButton2.setEnabled(editable);
		insertPhraseButton1.setEnabled(editable);
		insertPhraseButton2.setEnabled(editable);
		cutButton1.setEnabled(editable);
		copyButton1.setEnabled(editable);
		pasteButton1.setEnabled(editable);
		pasteCloneButton1.setEnabled(editable);
		deleteButton1.setEnabled(editable);
		editButton1.setEnabled(editable);
		deleteButton2.setEnabled(editable);
		editButton2.setEnabled(editable);
		cloneButton.setEnabled(editable);
		saveButton.setEnabled(editable);
	}

	
	
	
	public DiagramViewImpl(final AppController appController, final ClauseController clauseController) {
		me = this;
		MatContext.get().setDVIWindow(this);
		
		this.appController = appController;
		this.clauseController = clauseController;
		initWidget(uiBinder.createAndBindUi(this));	
		qdsElementPanel.add(clauseController.getWidget());
 
		attributeQDSElementList1 = new ListBoxMVP();
		attributeQDSElementList1.setVisible(false);
		int in1 = attributeEditorHorizontalPanel1.getWidgetCount() -1;
		attributeEditorHorizontalPanel1.insert(attributeQDSElementList1, in1);

		attributeQDSElementList2 = new ListBoxMVP();
		attributeQDSElementList2.setVisible(false);
		int in2 = attributeEditorHorizontalPanel2.getWidgetCount() -1;
		attributeEditorHorizontalPanel2.insert(attributeQDSElementList2, in2);
		
		zoomCanvasDelegate = new ZoomCanvasDelegate(this);
		
		scrollPanels = new ScrollPanel[] {
				populationCanvasPanel,
				numeratorCanvasPanel,
				numeratorExCanvasPanel,
				denominatorCanvasPanel,
				exclusionsCanvasPanel,
				exceptionsCanvasPanel,
				measurePopCanvasPanel,
				measureObservCanvasPanel,
				stratificationCanvasPanel,
				otherCanvasPanel
		};

		criterionStackSelectionHandler = new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				if(ignoreCriterionPanelSelection){
					ignoreCriterionPanelSelection = false;
					return;
				}
				deselectLibrary();
				Integer index = ((SelectionEvent<Integer>)event).getSelectedItem();
				if (!MatContext.get().getSynchronizationDelegate().isSavingClauses()){
					clearPropEditMessages();
					if (appController.isLoaded()) {
						//Auto Save Commented for Canvas Sub Tabs
						//appController.saveMainPhrases();
					} else {
						MatContext.get().getSynchronizationDelegate().setSavingClauses(false);
					}
					selectStack(index);
				}
			}
		};

		CanvasPanelDelegate cpd = new CanvasPanelDelegate();
		criterionPanel = new MatTabLayoutPanel(true);
		
		criterionPanel.setWidth("100%");
		criterionPanel.setHeight("250px");
		cpd.addCanvasPanel(criterionPanel, populationCanvasPanel, ConstantMessages.POP_TAB);
		cpd.addCanvasPanel(criterionPanel, numeratorCanvasPanel, ConstantMessages.NUM_TAB);
		cpd.addCanvasPanel(criterionPanel, numeratorExCanvasPanel, ConstantMessages.NUM_EX_TAB);
		cpd.addCanvasPanel(criterionPanel, denominatorCanvasPanel, ConstantMessages.DEN_TAB);
		cpd.addCanvasPanel(criterionPanel, exclusionsCanvasPanel, ConstantMessages.EXCL_TAB);
		cpd.addCanvasPanel(criterionPanel, exceptionsCanvasPanel, ConstantMessages.EXCEP_TAB);
		cpd.addCanvasPanel(criterionPanel, measurePopCanvasPanel, ConstantMessages.MEASURE_POP_TAB);
		cpd.addCanvasPanel(criterionPanel, measureObservCanvasPanel, ConstantMessages.MEASURE_OBS_TAB);
		cpd.addCanvasPanel(criterionPanel, stratificationCanvasPanel, ConstantMessages.STRAT_TAB);
		cpd.addCanvasPanel(criterionPanel, otherCanvasPanel, ConstantMessages.USER_DEFINED_TAB);
		cpd.addCanvasPanel(criterionPanel, measurePhrasePanel, measurePhraseTextArea1, ConstantMessages.MEASURE_PHRASE_TAB);
		
		criterionPanel.setId("criterionPanel");
		criterionPanel.addSelectionHandler(criterionStackSelectionHandler);
		stackFocusPanel.add(criterionPanel);
		criterionPanel.forceSelectTab(0);
		
		libraryTabPanel = new MatTabLayoutPanel(true);
		
		clauseLibraryListBox = new ListBoxMVP();
		String titleCL = "Clause Library";
		DOM.setElementAttribute(clauseLibraryListBox.getElement(), "id", "Clause Library");
		libraryTabPanel.add(clauseLibraryListBox, titleCL);
		MatContext.get().setAriaHidden(clauseLibraryListBox, "true");//set clause library to Aria-hidden true when it is initially added.  
		propertyEditor1Label = new Label("Property Editor");
		fireAlert(propertyEditor1Label);
		propertyEditor1Focus = new FocusableWidget(propertyEditor1Label);
        propertyEditor1Panel.add(propertyEditor1Focus);
		
        propertyEditor2Label = new Label("Property Editor");
        fireAlert(propertyEditor2Label);
		propertyEditor2Focus = new FocusableWidget(propertyEditor2Label);
        propertyEditor2Panel.add(propertyEditor2Focus);
        setVisible(attributeEditorPanel1, false);//This is for aria-hidden when the css is turned off.
        setVisible(attributeEditorPanel2, false);//This is for aria-hidden when the css is turned off.
		measurePhraseListBox = new ListBoxMVP();
		String titleMP = "Measure Phrases";
		DOM.setElementAttribute(measurePhraseListBox.getElement(), "id", "Measure Phrases");
	    libraryTabPanel.add(measurePhraseListBox, titleMP);
		
		libraryTabPanel.getWidget(CLAUSE_LBRARY_TAB).getElement().setTabIndex(0);
		libraryTabPanel.getWidget(MEASURE_PHRASE_LIBRARY_TAB).getElement().setTabIndex(0);
		
		
		libraryTabPanelHandlerRegistration = libraryTabPanel.addSelectionHandler(libraryTabPanelSelectionHandler);
		
		
		textViewButton1Inline.addClickHandler(new MATClickHandler() {
			@Override
			public void onEvent(GwtEvent event) {
				MatContext.get().clearDVIMessages();
				showTextView();
			}
		});
		
		canvasViewButton2Inline.addClickHandler(new MATClickHandler() {
			@Override
			public void onEvent(GwtEvent event) {
				MatContext.get().clearDVIMessages();
				showCanvasView();
			}
		});
		
		libraryVerticalPanel.add(libraryTabPanel);
		libraryTabPanel.forceSelectTab(MEASURE_PHRASE_TAB);
		MatContext.get().setAriaHidden(libraryTabPanel.getWidget(MEASURE_PHRASE_TAB),false);//set Aria-hidden false when the tab has been selected.
		
		measurePhraseListBoxHandlerRegistration = measurePhraseListBox.addChangeHandler(measurePhraseChangeHandler);
		clauseLibraryListBoxHandlerRegistration =  clauseLibraryListBox.addChangeHandler(measurePhraseChangeHandler);
		currentScrollPanel = populationCanvasPanel;

		deleteClickHandler = new MATClickHandler() {	
			@Override
			public void onEvent(GwtEvent event) {
				MatContext.get().clearDVIMessages();
				if (!appController.deleteDiagramObject(currentCriterion, currentDiagramObject))
					System.out.println("Unable to delete!");
				currentDiagramObject = null;
				hidePropertyEditor();
				if (diagramView == DIAGRAM_VIEW.TEXT_VIEW)
					redrawDiagramTree(null);
				initButtons(currentCriterion);
				updateEditButtons(false);
			}
		};
		criterionDeleteClickHandler = new MATClickHandler() {
			@Override
			public void onEvent(GwtEvent arg0) {
				MatContext.get().clearDVIMessages();
				Criterion criterion = (Criterion)currentDiagramObject;
				if (!appController.deleteCriterion(currentCriterion, currentDiagramObject))
					System.out.println("Unable to delete!");
				currentDiagramObject = null;
				hidePropertyEditor();
				if (diagramView == DIAGRAM_VIEW.TEXT_VIEW)
					redrawDiagramTree(null);
				initButtons(currentCriterion);
				updateEditButtons(false);
			}
		};

		addExplicitLabelsToAttributes();
		start();
		page1Buttons.add(textViewButton1Inline);
		page2Buttons.add(canvasViewButton2Inline);

		//measurePhraseTextArea1.setWordWrap(true);
		measurePhraseTextArea2.setWordWrap(true);
		
		errorMessageHolder.add(propEditErrorMessages);
		
		saveButton.addClickHandler( new MATClickHandler() {
			public void onEvent(GwtEvent event) {
				doSave(true);
			}
		});
	
		saveButton.setStyleName("primaryButton");
		saveButton.setTitle("Save     Ctrl+Alt+s");
		saveButton.getElement().setAttribute("alt", "Save");
		saveButton.setText("Save");
		saveHolder.add(saveButton);
		
		diagramTreeExpandButton.getElement().setAttribute("alt", "Expand diagram tree");
		diagramTreeCollapseButton.getElement().setAttribute("alt", "Collapse diagram tree");
		
		
		diagramTree = new Tree();
		diagramTree.setWidth("760px");
		diagramTree.setHeight("240px");
		textScrollPanel.add(diagramTree);
		/*
		 * handle the selection indicator in the diagram tree
		 */
		diagramTree.addSelectionHandler(new TreeSelectionHandler());
		
		/*
		 * handle the selection indicator in the diagram tree for open and close events
		 */
		diagramTree.addOpenHandler(new OpenHandler<TreeItem>(){
			@Override
			public void onOpen(OpenEvent<TreeItem> event) {
				handleExpansionImageStatus();
			}
		});
		diagramTree.addCloseHandler(new CloseHandler<TreeItem>(){
			@Override
			public void onClose(CloseEvent<TreeItem> event) {
				handleExpansionImageStatus();
			}
		});
	}
	
	private void doSave(final boolean showMessages){
		// TODO perform dirty check before saving 
		// even consider showing the saved message whether dirty or not
		MatContext.get().clearDVIMessages();
		//Auto Save Commented for Canvas Sub Tabs
		//appController.saveMainPhrases();
		Command waitForSave = new Command(){
			@Override
			public void execute() {
				if(!MatContext.get().getSynchronizationDelegate().isSavingClauses()){
					if(showMessages){
						if(isSaveStatus())
							showSavedMessage(true);
						else
							showSavedMessage(false);
					}
				}else{
					DeferredCommand.addCommand(this);
				}
			}
		};
		waitForSave.execute();	
	}  	
	
	public void init() {
		// ALIGN_RIGHT is already assigned in the UI binder, but this forces Eclipse not to
		// eliminate HasHorizontalAlignment if imports are cleaned up.
		page1Buttons.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		page2Buttons.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	}

	private void setAriaHiddenTrueForClosingTab(int deselectedIndex) {
		MatContext.get().setAriaHidden(libraryTabPanel.getWidget(deselectedIndex),true);
	}
	
	protected void selectTab(int tab) {
		currentLibraryTab = tab;
		libraryTabPanel.selectTab(tab);
		deselectLibrary();
	}

	//adding HandlerRegistration object and creating handler field to control the number of handlers that are created and added
	protected HandlerRegistration libraryTabPanelHandlerRegistration;
	protected SelectionHandler libraryTabPanelSelectionHandler = new SelectionHandler<Integer>() {
		@Override
		public void onSelection(SelectionEvent<Integer> event) {
			int selectedTab = event.getSelectedItem().intValue();
			
			if(currentLibraryTab != selectedTab){
				currentLibraryTab = selectedTab;
				setAriaHiddenTrueForClosingTab(currentLibraryTab);
	    		MatContext.get().setAriaHidden(libraryTabPanel.getWidget(selectedTab),false);//set Aria-hidden false when the tab has been selected.
	    		deselectLibrary();
	    		//US201 load system clauses only when the System Clause Library Tab is selected
				if(selectedTab == CLAUSE_LBRARY_TAB)
					appController.loadSystemClauseNames();
				
			}
			
		}
	};
	
	//adding HandlerRegistration objects and creating handler fields to control the number of handlers that are created and added
	protected HandlerRegistration measurePhraseListBoxHandlerRegistration;
	protected HandlerRegistration clauseLibraryListBoxHandlerRegistration;
	protected ChangeHandler measurePhraseChangeHandler =  new MATChangeHandler() {
		@Override
		public void onEvent(GwtEvent event) {
			ListBox listBox = (ListBox)event.getSource();
			doPhraseChangeHandlerBehavior(listBox);
		}
	};
	

	protected void start() {
		if (propertyGrid != null)
			propertyGrid.clear();
	}

	private void setPropertyEditorVisibile(boolean visible) {
		setVisible(propertyEditor1,visible);	
		setVisible(propertyEditor2,visible);
	}

	
	
	@Override
	public void showPropertyEditor() {
		setPropertyEditorVisibile(true);
		moveFocusToPropertyEditor();
	}

	@Override
	public void hidePropertyEditor() {
		setPropertyEditorVisibile(false);
		//TODO determine where to send focus
	}

	@Override
	public HorizontalPanel getPropertyEditor() {
		switch (diagramView) {
		case CANVAS_VIEW:
			return propertyEditor1;
		case TEXT_VIEW:
			return propertyEditor2;
		default:
			return null;
		}	
	}

	protected void showOrHideToolbars() {
		boolean cvtVisible = (diagramView == DIAGRAM_VIEW.CANVAS_VIEW);
		boolean tvtVisible = (diagramView == DIAGRAM_VIEW.TEXT_VIEW);
		setVisible(canvasViewToolbar, cvtVisible);
		setVisible(textViewToolbar, tvtVisible);
	}

	public void selectStack() {
		selectStack(currentStackIndex);
	}

	public void selectStack(Integer index) {
		/*
		 * http://www.unicode.org/charts/PDF/U25A0.pdf
		 * 25BC = down arrow - expanded
		 * 25B6 = right arrow - collapsed
		 */
		currentStackIndex = index;
		String criterion = AppController.criterionNames.get(index);
		if (index < ConstantMessages.CONTEXT_ID_COUNT-1){
			MatContext.get().getZoomFactorService().setCurrentFactor(index);
			selectCriterion(scrollPanels[index], criterion);
		}else{
			selectCriterion(measurePhrasePanel, ConstantMessages.MEASURE_PHRASE_CONTEXT_DESC);
		}
		initButtons(criterion);
		updateEditButtons(false);
	}

	private void initButtons() {
		initButtons(currentCriterion);
	}

	public void setToolbarTitle(String title, boolean showAttributeNamePanel) {
		switch (diagramView) {
		case CANVAS_VIEW:
			toolbarTitle1.setText(title);
			setVisible(attributePhraseNamePanel1, showAttributeNamePanel);
			break;
		case TEXT_VIEW:
			toolbarTitle2.setText(title);
			setVisible(attributePhraseNamePanel1, !showAttributeNamePanel);
			break;
		default:
			setVisible(attributePhraseNamePanel1, false);
		break;
		}
	}

	private void initButtons(String criterion) {
		String caption = "";
		//TODO Change the canvas view conditions for caption.
		switch(diagramView){
		case CANVAS_VIEW:
			if (currentDiagramObject != null && !(currentDiagramObject instanceof CriterionParent)) {
				if (currentDiagramObject instanceof Criterion) {
					if (clipboardTraversalTree == null)
						caption = "Edit  Criterion";
					else
						caption = "Insert";
				}
				else {		
					String longName = currentDiagramObject.getClass().getName();
					String shortName = longName.substring(longName.lastIndexOf(".")+1);
						if (shortName == null)
							shortName = "";
						else if (shortName.equals("PlaceHolder"))
							shortName = "Simple Statement";
						else if (shortName.equals("Qdsel"))
							shortName = "Phrase Element";
						caption = "insert or edit " + shortName;
				}
					
			}
			setToolbarTitle(caption,  false);
			setVisible(attributePhraseNameLabel1, false);
			break;
			default:
				 break;
		}
		boolean visible;
		boolean isMeasurePhrase = criterion.equals(ConstantMessages.MEASURE_PHRASE_CONTEXT_DESC);
		boolean isUserDefined = criterion.equalsIgnoreCase(ConstantMessages.USER_DEFINED_CONTEXT_DESC);
		boolean canHaveChildren = currentDiagramObject != null && currentDiagramObject.canHaveChildren();
		if (currentDiagramObject == null || isMeasurePhrase || isUserDefined)
			visible = false;
		else
			visible =  canHaveChildren;

		switch (diagramView) {
		case CANVAS_VIEW:
			initButtons(criterion, 
					criterionButton1, pasteCloneButton1, qdselButton1, andButton1, orButton1, timingButton1, addPhraseButton1, insertPhraseButton1, deleteButton1
//					,cutButton1, copyButton1, pasteButton1, editButton1
					);
			break;
		case TEXT_VIEW:
			initTextViewButtons(criterion,caption, visible, 
					criterionButton2, addPhraseButton2, insertPhraseButton2, deleteButton2);
			break;
		default:
			break;
		}
		setLibraryStyle();
	}

	private void setLibraryStyle() {
		//Left these 3 lines since in ie7 these following lines are required.
		libraryTabPanel.setHeight("210px");
		libraryTabPanel.setWidth("240px");
		libraryTabPanel.setStyleName("tabPanel");

		clauseLibraryListBox.setStylePrimaryName("listBox");
		measurePhraseListBox.setStylePrimaryName("listBox");
		//need these 2 lines for making the listbox wider in ie7 
		measurePhraseListBox.setSize("240px","300px");
		clauseLibraryListBox.setSize("240px","300px");
		measurePhraseListBox.getElement().setAttribute("style", "width: 100%; height: 300px;");
		clauseLibraryListBox.getElement().setAttribute("style", "width: 100%; height: 300px;");
	}

	private int getCurrentTreeDepth(TraversalTree tree, int depth){
		int ret = 0;
		if(tree.getDiagramObject().equals(currentDiagramObject))
			return tree.getData().getDepth()+1;
		else if(tree.isLeaf())
			return 0;
		for(TraversalTree child : tree.getChildren()){
			ret += getCurrentTreeDepth(child, depth+1);
		}
		return ret;
	}
	
	private void initButtons(String criterion,
			Button criterionButton, Button pasteCloneButton, Button qdselButton, Button andButton,
			Button orButton, Button timingButton, Button addPhraseButton,
			Button insertPhraseButton, Button deleteButton
//			,Button cutButton, Button copyButton, Button pasteButton, Button editButton
			) {
		showOrHideToolbars();
		
		boolean isMeasurePhrase = criterion.equals(ConstantMessages.MEASURE_PHRASE_CONTEXT_DESC);
		boolean isUserDefined = criterion.equals(ConstantMessages.USER_DEFINED_CONTEXT_DESC);
		boolean hasPhrases = appController.getMeasurePhraseList().size() > 0;
		boolean noSelection = currentDiagramObject == null;
		boolean selectedAndOr = false;
		boolean selectedTopLevelAnd = false;
		boolean selectedTiming = false;
		boolean selectedPhraseElement = false;
		boolean selectedPhrase = false;
		
		if(!noSelection && !isMeasurePhrase){
			int depth = getCurrentTreeDepth(diagram.getTree(), 0);
			
			selectedAndOr = depth != DEPTH_TOP_LEVEL_AND && currentDiagramObject instanceof Conditional;
			selectedTopLevelAnd = depth == DEPTH_TOP_LEVEL_AND && currentDiagramObject instanceof Conditional;
			selectedTiming = currentDiagramObject instanceof Rel;
			selectedPhraseElement = currentDiagramObject instanceof Qdsel;
			selectedPhrase = currentDiagramObject instanceof PlaceHolder;
		}
		
		boolean enableCriterionButton = (!isMeasurePhrase && !isUserDefined);//"Add Population"
		boolean enablePasteCloneButton = (!isMeasurePhrase);//"Paste Clone"
		boolean enableQdselButton = (selectedAndOr || selectedTopLevelAnd || selectedTiming);//"Phrase Element"
		boolean enableAndOrButtons = (selectedAndOr || selectedTopLevelAnd);//"And" "Or"
		boolean enableTimingButton = (selectedAndOr || selectedTopLevelAnd);//"Timing"
		boolean enableAddPhraseButton = (isMeasurePhrase || selectedAndOr || selectedTopLevelAnd);//"New Phrase"
		boolean enableInsertPhraseButton = hasPhrases && (selectedAndOr || selectedTopLevelAnd);//"Existing Phrase"
		boolean enableDeleteButton = (selectedAndOr || selectedTiming || selectedPhraseElement || selectedPhrase);//"Delete"
		
		setVisible(pasteCloneButton, enablePasteCloneButton);
		setVisible(qdselButton, enableQdselButton);
		setVisible(andButton, enableAndOrButtons);
		setVisible(orButton, enableAndOrButtons);	
		setVisible(timingButton, enableTimingButton);
		setVisible(addPhraseButton, enableAddPhraseButton);
		setVisible(insertPhraseButton, enableInsertPhraseButton);
		setVisible(deleteButton, enableDeleteButton);
		
		if (!(isMeasurePhrase) && !(isUserDefined)) {
			String text = "Add " + criterion;
			setVisible(criterionButton, true);
			criterionButton.setText(text);
			criterionButton.setTitle(text);
		}
		setVisible(criterionButton, enableCriterionButton);
	}

	private int getTreeDepth(TreeItem item){
		int d = -1; 
		while(item != null){
			item = item.getParentItem();
			d++;
		}
		return d;
		
	}
	
	private void initTextViewButtons(String criterion,String caption,boolean visible,
			Button criterionButton, Button addPhraseButton,
			Button insertPhraseButton, Button deleteButton) {
		MatContext.get().clearDVIMessages();
		showOrHideToolbars();
		caption = "Text View"; //Default value;
		//drive off of tree depth rather than complicated string and class checking
		int treeDepth = getTreeDepth(diagramTree.getSelectedItem());
		
		if(currentDiagramObject != null){
			if(treeDepth == DEPTH_CRITERION_PARENT || treeDepth == DEPTH_CRITERION){
				   
				//criterion or criterion parent depth = 1 or 2
				// set buttons for criterion parent or criterion i.e. system clause
			    setVisible(addPhraseButton, false);
				setVisible(insertPhraseButton, false);
				setVisible(editButton2, false);
				setVisible(deleteButton2,false);
				if(treeDepth == DEPTH_CRITERION_PARENT){
					 criterion = getTreeItemText(diagramTree.getSelectedItem());
				}else{
					 criterion = getTreeItemText(diagramTree.getSelectedItem().getParentItem());
				}
				if(!criterion.equals(ConstantMessages.USER_DEFINED_CONTEXT_DESC)){
					caption = "Edit Criterion";
					addSystemClauseButton(criterion, criterionButton);
				}
			}
				else if(treeDepth == DEPTH_TOP_LEVEL_AND){
						//top level AND depth = 3
						// set buttons for top level and
					    caption = "Insert or Edit Conditional";
					    criterion = getTreeItemText(diagramTree.getSelectedItem().getParentItem().getParentItem());
					    addSystemClauseButton(criterion, criterionButton);
						setVisible(addPhraseButton, true);
						setVisible(insertPhraseButton, true);
						setVisible(editButton2, false);
						setVisible(deleteButton2,false);
				}
				else if(treeDepth == DEPTH_MEASURE_PHRASE){
					    //measure phrase depth = 4
						// must be a measure phrase
					    caption = "Insert or Edit SimpleStatement";
					    setVisible(addPhraseButton, false);
						setVisible(insertPhraseButton, false);
						setVisible(editButton2, true);
						setVisible(deleteButton2,true);
						setVisible(criterionButton, false);
				}else if(criterion.equals(ConstantMessages.MEASURE_PHRASE_CONTEXT_DESC)){
						 //Measure Phrase selected from the measurePhraseLibrary.
						setVisible(addPhraseButton, true);
						setVisible(insertPhraseButton, false);
						setVisible(editButton2, false);
						setVisible(deleteButton2,false);
						setVisible(criterionButton, false);
				}else{
					setVisible(addPhraseButton, false);
					setVisible(insertPhraseButton, false);
					setVisible(criterionButton, false);
					setVisible(editButton2, false);
					setVisible(deleteButton2,false);
					setVisible(criterionButton, false);
				}
				currentCriterion = criterion;
		}else{
			setVisible(addPhraseButton, false);
			setVisible(insertPhraseButton, false);
			setVisible(criterionButton, false);
			setVisible(editButton2, false);
			setVisible(deleteButton2,false);
			setVisible(criterionButton, false);
		}
		setToolbarTitle(caption,  false);
		setVisible(attributePhraseNameLabel1, false);
		firePropertyEditorAlert();
	}

	
	private void updateEditButtons(boolean editButtonVisible) {
		switch (diagramView) {
		case CANVAS_VIEW:
			updateEditButtons(editButtonVisible, cutButton1, copyButton1, pasteButton1, deleteButton1, editButton1);
			break;
		default:
			break;
		}
	}

	private void addSystemClauseButton(String criterion,Button criterionButton){
		String text = "Add " + criterion;
		setVisible(criterionButton, true);
		criterionButton.setText(text);
		criterionButton.setTitle(text);
	}

	private void updateEditButtons(boolean editButtonVisible,
			Button cutButton, Button copyButton, Button pasteButton,
			Button deleteButton, Button editButton) {
		copyButton.setAccessKey('C');
		cutButton.setAccessKey('X');
		pasteButton.setAccessKey('V');

		boolean isClipboardEmpty = clipboardTraversalTree != null && clipboardTraversalTree.getDiagramObject() != null; 
		boolean isMeasurePhrase = currentCriterion.equals(ConstantMessages.MEASURE_PHRASE_CONTEXT_DESC);
		boolean noSelection = currentDiagramObject == null;
		boolean selectedAndOr = false;
		boolean selectedTopLevelAnd = false;
		boolean selectedTiming = false;
		boolean selectedPhraseElement = false;
		boolean selectedPhrase = false;
		
		if(!noSelection && !isMeasurePhrase){
			int depth = getCurrentTreeDepth(diagram.getTree(), 0);
			
			selectedAndOr = depth != DEPTH_TOP_LEVEL_AND && currentDiagramObject instanceof Conditional;
			selectedTopLevelAnd = depth == DEPTH_TOP_LEVEL_AND && currentDiagramObject instanceof Conditional;
			selectedTiming = currentDiagramObject instanceof Rel;
			selectedPhraseElement = currentDiagramObject instanceof Qdsel;
			selectedPhrase = currentDiagramObject instanceof PlaceHolder;
		}
		
		boolean enableCutButton = editButtonVisible && (selectedAndOr || selectedTiming || selectedPhraseElement);//"Cut"
		boolean enableCopyButton = editButtonVisible && (selectedAndOr || selectedTopLevelAnd || selectedPhraseElement);//"Copy"
		boolean enableEditButton = editButtonVisible && (selectedAndOr || selectedTiming || selectedPhraseElement || selectedPhrase);//"Edit"
		boolean enablePasteButton = editButtonVisible && isClipboardEmpty && (selectedAndOr || selectedTopLevelAnd);//"Paste"
		
		setVisible(cutButton, enableCutButton);
		setVisible(copyButton, enableCopyButton);
		setVisible(editButton, enableEditButton);
		setVisible(pasteButton, enablePasteButton);
	}

	private void showPasteButton() {
		Button pasteButton =  pasteButton1;
		pasteButton.setTitle("Paste " + clipboardTraversalTree.getDiagramObject().getIdentity());
		setVisible(pasteButton, true);
	}

	protected void handlePropertyGrid(final DiagramObject diagramObject) {
		DiagramObject clone = DiagramObjectFactory.clone(appController, diagramObject);
		handlePropertyGrid(clone, PROPERTY_GRID_STATE.INSERT);
	}

	protected void handlePropertyGrid(final DiagramObject diagramObject, PROPERTY_GRID_STATE propertyGridState) {
		List<String> elements = new ArrayList<String>();
        MatContext.get().clearDVIMessages();
		if (diagramObject instanceof Qdsel || diagramObject instanceof SimpleStatement)
			elements.addAll(Arrays.asList(appController.getQDSElements()));
		else if (diagramObject instanceof InsertPhrase) {
			Object[] objArray = appController.getMeasurePhraseList().toArray();
			String[] strArray = new String[objArray.length];
			int index = 0;
			for (Object obj : objArray)
				strArray[index++] = (String)obj;
			elements.addAll(Arrays.asList(strArray));
			//			elements.add("-----");	
			//			elements.addAll(Arrays.asList(appController.getQDSElements()));
		}
		else if (diagramObject instanceof Rel)
			//elements.addAll(Arrays.asList(Rel.longIdentities));
		    elements.addAll(getTimingConditionsList()); //US 171, Getting rid of Rel class static hard coded values.
		if (diagramObject instanceof Criterion)
			criterionClickHandlers(diagramObject, propertyGridState);
		else if (diagramObject instanceof Qdsel)
			qdselPropertyEditorClickHandlers(diagramObject, propertyGridState);
		else if (diagramObject instanceof SimpleStatement)
			simpleStatementPropertyEditorClickHandlers(diagramObject, propertyGridState);
		else if (diagramObject instanceof InsertPhrase)
			insertPhrasePropertyEditorClickHandlers(diagramObject);
		else if (diagramObject instanceof Conditional)
			conditionalPropertyEditorClickHandlers(diagramObject);
		else if (diagramObject instanceof Rel)
			relPropertyEditorClickHandlers(diagramObject);

		FlexTable updatedGrid;
		if (diagramObject instanceof Criterion) {
			updatedGrid = 
				(propertyGridState == PROPERTY_GRID_STATE.INSERT)
				? new PropertyEditorArranger().create(me, elements, diagramObject, propertyGrid,
						diagramObject.getIdentity(), saveClickHandler, cancelClickHandler)
						: new PropertyEditorArranger().select(me, elements, diagramObject, propertyGrid,
								diagramObject.getIdentity(), saveClickHandler, criterionDeleteClickHandler, cancelClickHandler);
		}
		else {
			updatedGrid = 
				(propertyGridState == PROPERTY_GRID_STATE.INSERT)
				? new PropertyEditorArranger().create(me, elements, diagramObject, propertyGrid,
						diagramObject.getIdentity(), saveClickHandler, cancelClickHandler)
						: new PropertyEditorArranger().select(me, elements, diagramObject, propertyGrid,
								diagramObject.getIdentity(), saveClickHandler, deleteClickHandler, cancelClickHandler);
				propertyGrid = updatedGrid;
		}

		showOrHideToolbars();
		showPropertyEditor();
	}

	private void criterionClickHandlers(final DiagramObject diagramObject, PROPERTY_GRID_STATE propertyGridState) {
		//saveClickHandler 1
		saveClickHandler = new MATClickHandler() {
			@Override	
			public void onEvent(GwtEvent event) {
				saveButton.setEnabled(false);
				MatContext.get().clearDVIMessages();
				diagramObject.update();
				Criterion criterion = (Criterion)diagramObject;
				Criterion newCriterion = (Criterion)(Criterion.clone(criterion));

				if (appController.isDiagramObject(currentCriterion, criterion))
					appController.updateDiagramObject(currentCriterion, currentDiagramObject, newCriterion);
				else
					appController.addDiagramObject(currentCriterion, currentDiagramObject, newCriterion);
				removePropertyEditor();
				if (diagramView == DIAGRAM_VIEW.TEXT_VIEW) {
					drawDiagramTree();
					selectDiagramTreeObject(newCriterion);
				}
				moveFocusToPropertyEditor();
				saveButton.setEnabled(true);
			}
		};

		cancelClickHandler = new MATClickHandler() {
			@Override	
			public void onEvent(GwtEvent event) {
				doCancelClickHandlerBehaviors();
			}
		};	
	}

	@Override
	public boolean criterionHasOnlyOneChild() {
		return appController.criterionHasOnlyOneChild(currentCriterion);
	}

	protected void qdselPropertyEditorClickHandlers(final DiagramObject diagramObject, PROPERTY_GRID_STATE propertyGridState) {
		if (propertyGridState == PROPERTY_GRID_STATE.INSERT) {
			//saveClickHandler 2
			saveClickHandler = new MATClickHandler() {
				@Override	
				public void onEvent(GwtEvent event) {
					saveButton.setEnabled(false);
					logSavePhraseEvent(diagramObject);
					MatContext.get().clearDVIMessages();
					diagramObject.update();	
					Qdsel qdsel = (Qdsel)diagramObject;
					DiagramObject newQdsel = appController.addDiagramObject(currentCriterion, currentDiagramObject, new Qdsel(qdsel.getIdentity()));
					removePropertyEditor();
					if (diagramView == DIAGRAM_VIEW.TEXT_VIEW) {
						drawDiagramTree();
						selectDiagramTreeObject(newQdsel);
					}
					moveFocusToPropertyEditor();
					saveButton.setEnabled(true);
				}
			};
		} else {
			//saveClickHandler 3
			saveClickHandler = new MATClickHandler() {
				@Override	
				public void onEvent(GwtEvent event) {
					saveButton.setEnabled(false);
					logSavePhraseEvent(diagramObject);
					MatContext.get().clearDVIMessages();
					diagramObject.update();
					appController.updateDiagramObject(currentCriterion, diagramObject, (Qdsel)currentDiagramObject);
					hidePropertyEditor();
					if (diagramView == DIAGRAM_VIEW.TEXT_VIEW) {
						drawDiagramTree();
						selectDiagramTreeObject(currentDiagramObject);
					}
					moveFocusToPropertyEditor();
					saveButton.setEnabled(true);
				}
			};		

			
			

		
			
		}

		cancelClickHandler = new MATClickHandler() {
			@Override	
			public void onEvent(GwtEvent event) {
				doCancelClickHandlerBehaviors();
			}
		};		
	}

	protected void endSimpleStatementEdit() {
		removePropertyEditor();
		unhilightDiagramObject(false);
		hideMeasurePhraseTextArea();
		measurePhraseListBox.setSelectedIndex(-1);
	}

	/**
	 * if a measure phrase is selected in the library and 
	 * there exists a match for arg name in the library
	 * then dup exists
	 * @param name
	 * @return dup exists
	 */
	private boolean isMeasurePhraseDuplicate(String name){
		boolean noLibrarySelection = measurePhraseListBox.getSelectedIndex() < 0;
		if(noLibrarySelection){
			for(int i = 0; i < measurePhraseListBox.getItemCount(); i++){
				if(measurePhraseListBox.getItemText(i).equalsIgnoreCase(name)){
					clearMessages();
					getPropEditErrorMessages().setMessage(MatContext.get().getMessageDelegate().getMeasurePhraseAlreadyExistsMessage(name));
					return true;
				}
			}
		}
		return false;
	}
	
	protected void simpleStatementPropertyEditorClickHandlers(final DiagramObject diagramObject, PROPERTY_GRID_STATE propertyGridState) {
		
		MatContext.get().clearDVIMessages();
		if (propertyGridState == PROPERTY_GRID_STATE.INSERT) {
			//saveClickHandler 4
			saveClickHandler = new MATClickHandler() {
				@Override
				public void onEvent(GwtEvent event) {
					//NOTE: saveButton will be enabled when the phrase library is refreshed
//					saveButton.setEnabled(false);
					disableLibraries(CLASS_NAME);
					MatContext.get().clearDVIMessages();
					if (diagramObject.update()) {
						//check for dup before insert invocation
						if(!isMeasurePhraseDuplicate(diagramObject.getChangedName())){
							diagramObject.setIdentity(diagramObject.getChangedName());//Set the changedName as the identity when the phrase has been created newly.
							logSavePhraseEvent(diagramObject);
							insertMeasurePhraseDiagramObject(diagramObject, currentCriterion);
							
							Command isSavingCmd = new Command(){
								public void execute() {
						    		  if(!MatContext.get().getSynchronizationDelegate().isSavingClauses()){ 
						    			    refreshMeasurePhrases(appController.getMeasurePhraseList());
											presenter.updateSavedMeasurePhrase(((SimpleStatement)diagramObject).getIdentity(),diagramObject);//This line will update the saved Measure Phrase.
											if (diagramView == DIAGRAM_VIEW.TEXT_VIEW)	 {		
												drawDiagramTree();
												selectDiagramTreeObject(diagramObject);
											}
											endSimpleStatementEdit();
											start();
											moveFocusToPropertyEditor();
											enableLibraries(CLASS_NAME);
											initButtons();
											updateEditButtons(false);
						    		  }else
						    			  DeferredCommand.addCommand(this);
						    		  
						    	}
							};//End of command
							isSavingCmd.execute();
						}else
							enableLibraries(CLASS_NAME);
					}else
						enableLibraries(CLASS_NAME);
					if(diagramObject instanceof SimpleStatement){
						//Don't just grab. Iterate through this as it may be empty
						//and may have multiple messages.
						if(((SimpleStatement)diagramObject).getErrorMsgList().size()>0){
							String bork =((SimpleStatement)diagramObject).getErrorMsgList().get(0);
							propEditErrorMessages.setMessage(bork);
							((SimpleStatement)diagramObject).getErrorMsgList().clear();
						}
					}
					
				}//end of click method
			};
		}
		else {
			//saveClickHandler 5
			saveClickHandler = new MATClickHandler() {
				@Override
				public void onEvent(GwtEvent event) {
					//NOTE: saveButton will be enabled when the phrase library is refreshed
//					saveButton.setEnabled(false);f
					disableLibraries(CLASS_NAME);
					MatContext.get().clearDVIMessages();
					if (diagramObject.update()) {
						logSavePhraseEvent(diagramObject);
						appController.save(((SimpleStatement)diagramObject).getIdentity());
						Command waitSavingCmd = new Command(){
							public void execute() {
					    		  if(!MatContext.get().getSynchronizationDelegate().isSavingClauses()){ 
					    			    presenter.updateMPMap(getSelectedMeasurePhrase(),diagramObject);
										presenter.updateSavedMeasurePhrase(getSelectedMeasurePhrase(),diagramObject);//This line will update the saved Measure Phrase.
										select(diagramObject);
										if (diagramView == DIAGRAM_VIEW.TEXT_VIEW) {
											drawDiagramTree();
											selectDiagramTreeObject(diagramObject);
										}
										endSimpleStatementEdit();
										start();
										refreshMeasurePhrases(appController.getMeasurePhraseList());
										refreshSystemClausesDiagrams(appController.getMeasurePhraseList());
										moveFocusToPropertyEditor();
					    		  }else
					    			  DeferredCommand.addCommand(this);
					    		  
					    	}
						};
						waitSavingCmd.execute();
					}
					if(diagramObject instanceof SimpleStatement){	
						//Don't just grab. Iterate through this as it may be empty
						//and may have multiple messages.
						if(((SimpleStatement)diagramObject).getErrorMsgList().size()>0){
							String bork =((SimpleStatement)diagramObject).getErrorMsgList().get(0);
							propEditErrorMessages.setMessage(bork);
							((SimpleStatement)diagramObject).getErrorMsgList().clear();
						}
					}
//					saveButton.setEnabled(true);
				}
			};	

			
			

		}

		cancelClickHandler = new MATClickHandler() {
			@Override
			public void onEvent(GwtEvent event) {
				MatContext.get().clearDVIMessages();
				retainSavedValue();
				endSimpleStatementEdit();		
				start();
				moveFocusToPropertyEditor();
				
				initButtons();
				updateEditButtons(true);
			}
		};
	}
	
	private void doCancelClickHandlerBehaviors(){
		MatContext.get().clearDVIMessages();
		removePropertyEditor();
		moveFocusToPropertyEditor();
	}


	
	private void retainSavedValue(){
		SimpleStatement saveddiagramObj = (SimpleStatement)presenter.getSavedMeasurePhrase(getSelectedMeasurePhrase());
		SimpleStatement currentdiagramObj =(SimpleStatement) presenter.getMeasurePhrase(getSelectedMeasurePhrase());
		if(saveddiagramObj != null && currentdiagramObj != null){
			currentdiagramObj.setMode(saveddiagramObj.getMode());
			currentdiagramObj.clearRadioButtonSelection();
			currentdiagramObj.getPhrase1().setText(saveddiagramObj.getPhrase1().getText());
			
			//band aid to validate if no phrase entered
			Phrase phrase2 = null;
			String phrase2Txt = null;			
			phrase2 = saveddiagramObj.getPhrase2();
			if(phrase2 != null){
				phrase2Txt = phrase2.getText();
				if(phrase2Txt != null){
					currentdiagramObj.getPhrase2().setText(phrase2Txt);
				}
			}
			currentdiagramObj.setCondition(saveddiagramObj.getCondition());
			//US 601 retaining status value
			currentdiagramObj.setStatus(saveddiagramObj.getStatus());
			currentdiagramObj.setFunction(saveddiagramObj.getFunction());
			currentdiagramObj.setFunctionOperator(saveddiagramObj.getFunctionOperator());
			currentdiagramObj.setFunctionQuantity(saveddiagramObj.getFunctionQuantity());
			currentdiagramObj.setFunctionUnit(saveddiagramObj.getFunctionUnit());
			//retain the saved attributes
			currentdiagramObj.getPhrase1().setAttributes(saveddiagramObj.getPhrase1().getAttributes());
			
			if(phrase2 != null){
				List<Attribute> phrase2AttrLst = saveddiagramObj.getPhrase2().getAttributes();
				if(phrase2AttrLst != null){
					currentdiagramObj.getPhrase2().setAttributes(phrase2AttrLst);
				}
			}
			for(int i=0; i< saveddiagramObj.getAdditionalPhraseList().size(); ++i){
			   currentdiagramObj.getAdditionalPhraseList().get(i).setAttributes(saveddiagramObj.getAdditionalPhraseList().get(i).getAttributes());
			   currentdiagramObj.getAdditionalPhraseList().get(i).setText(saveddiagramObj.getAdditionalPhraseList().get(i).getText());
			}
		}
	}

	protected void insertPhrasePropertyEditorClickHandlers(final DiagramObject diagramObject) {
		//saveClickHandler 6
		saveClickHandler = new MATClickHandler() {
			@Override	
			public void onEvent(GwtEvent event) {
				saveButton.setEnabled(false);
				logSavePhraseEvent(diagramObject);
				MatContext.get().clearDVIMessages();
				diagramObject.update();
				InsertPhrase insertPhrase = (InsertPhrase)diagramObject;
				String phrase = insertPhrase.getPhrase();
				SimpleStatement simpleStatement = (SimpleStatement)(appController.getMeasurePhrase(phrase));
				DiagramObject newPlaceHolder = appController.addDiagramObject(currentCriterion, currentDiagramObject, new PlaceHolder(simpleStatement));
				removePropertyEditor();
				if (diagramView == DIAGRAM_VIEW.TEXT_VIEW)	 {		
					drawDiagramTree();
					selectDiagramTreeObject(newPlaceHolder);
					initButtons(currentCriterion);
					updateEditButtons(true);
				}
				moveFocusToPropertyEditor();
				saveButton.setEnabled(true);
			}
		};

		cancelClickHandler = new MATClickHandler() {
			@Override	
			public void onEvent(GwtEvent event) {
				doCancelClickHandlerBehaviors();
			}
		};		
	}

	protected void relPropertyEditorClickHandlers(final DiagramObject diagramObject) {
		//saveClickHandler 7
		saveClickHandler = new MATClickHandler() {
			@Override	
			public void onEvent(GwtEvent event) {
				saveButton.setEnabled(false);
				logSavePhraseEvent(diagramObject);
				MatContext.get().clearDVIMessages();
				diagramObject.update();
				Rel rel = (Rel)diagramObject;
				String timing = rel.getTiming();
				Rel newRel;

				if (appController.isDiagramObject(currentCriterion, rel))
					appController.updateDiagramObject(currentCriterion, rel, newRel = new Rel(timing));
				else
					appController.addDiagramObject(currentCriterion, currentDiagramObject, newRel = new Rel(timing));
				removePropertyEditor();
				if (diagramView == DIAGRAM_VIEW.TEXT_VIEW) {
					drawDiagramTree();
					selectDiagramTreeObject(newRel);
				}
				moveFocusToPropertyEditor();
				saveButton.setEnabled(true);
			}
		};

		cancelClickHandler = new MATClickHandler() {
			@Override	
			public void onEvent(GwtEvent event) {
				doCancelClickHandlerBehaviors();
			}
		};		
	}

	protected void conditionalPropertyEditorClickHandlers(final DiagramObject diagramObject) {
		//saveClickHandler 8
		saveClickHandler = new MATClickHandler() {
			@Override	
			public void onEvent(GwtEvent event) {
				saveButton.setEnabled(false);
				logSavePhraseEvent(diagramObject);
				MatContext.get().clearDVIMessages();
				diagramObject.update();
				Conditional conditional = (Conditional)diagramObject;
				String condition = conditional.getIdentity();
				Conditional newConditional;
				if (appController.isDiagramObject(currentCriterion, conditional))
					appController.updateDiagramObject(currentCriterion, conditional, newConditional = new Conditional(condition));
				else
					appController.addDiagramObject(currentCriterion,  currentDiagramObject, 
							newConditional = new Conditional(conditional.getIdentity()));
				removePropertyEditor();
				if (diagramView == DIAGRAM_VIEW.TEXT_VIEW) {
					drawDiagramTree();
					selectDiagramTreeObject(newConditional);
				}
				moveFocusToPropertyEditor();
				saveButton.setEnabled(true);
			}
		};

		cancelClickHandler = new MATClickHandler() {
			@Override	
			public void onEvent(GwtEvent event) {
				doCancelClickHandlerBehaviors();
			}
		};	
	}

	
	@UiHandler("cloneButton")
	public void onCloneButtonClicked(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		if(!isLoading(true)){
			int selected = clauseLibraryListBox.getSelectedIndex();
			if (selected >= 0) {
				String text = clauseLibraryListBox.getItemTitle(selected);
				String value = clauseLibraryListBox.getValue(selected);
				clauseToBeCloned = new ClauseLT();
				clauseToBeCloned.setText(text);
				clauseToBeCloned.setMeasureID(value);
			} else {
				//Write to an error message box.
				clearPropEditMessages();
				propEditErrorMessages.setMessage(MatContext.get().getMessageDelegate().getPleaseSelectClauseMessage());
			}
		}
	}


	
	
	private void prepareViewSwitch() {
		switch (diagramView) { // better to get the indices directly from a getter in the enum!
		case CANVAS_VIEW:
			criterionBeforeViewSwitch[0] = currentCriterion;
			break;
		case TEXT_VIEW:
			criterionBeforeViewSwitch[1] = currentCriterion;
			break;
		default:
			criterionBeforeViewSwitch[2] = currentCriterion;
		break;
		}

		unhilightDiagramObject(false);
		currentDiagramObject = null;

		setVisible(canvasViewPanel, false);
		setVisible(textViewPanel, false);
		
		hideMeasurePhraseTextArea();

		hideAttributeEditor();
		hidePropertyEditor();		
		removeCanvas();
	}

	KeyDownHandler preventTypingKeyHandler = new KeyDownHandler() {
		@Override
		public void onKeyDown(KeyDownEvent event) {
			if (event.getNativeKeyCode() != 9) {
				event.preventDefault();
				event.stopPropagation();
			}
		}
	};

	private void showOrHideMeasurePhraseTextArea(boolean visible) {
	switch (diagramView) {
	case CANVAS_VIEW:
		setVisible(measurePhraseTextArea1, visible);
		setVisible(measurePhraseTextArea2, false);
		break;
	case TEXT_VIEW:
		setVisible(measurePhraseTextArea1, false);
		setVisible(measurePhraseTextArea2, visible);			
		break;
	default:
		break;
	}
	}

	private void showMeasurePhraseTextArea() {
		showOrHideMeasurePhraseTextArea(true);
	}

	private void hideMeasurePhraseTextArea() {
		showOrHideMeasurePhraseTextArea(false);
	}

	public void showCanvasView() {
		prepareViewSwitch();
		currentCriterion = criterionBeforeViewSwitch[0];
		setVisible(canvasViewPanel, true);

		diagramView = DIAGRAM_VIEW.CANVAS_VIEW;

		Integer criterionIndex = appController.getCriterionIndex(currentCriterion);
		if (criterionIndex >= 0 && criterionIndex < AppController.criterionNames.size())
			selectStack(criterionIndex);
		else
			System.out.println("Can't show criterion " + currentCriterion);
	}

	

	private void showTextView() {
		prepareViewSwitch();
		currentCriterion = criterionBeforeViewSwitch[2];
		setVisible(textViewPanel, true);
		diagramView = DIAGRAM_VIEW.TEXT_VIEW;
		drawDiagramTree();
	}

	@Override
	public void showAttributeEditor(Phrase phrase) {
		String labelTitle = phrase.getText();
		String labelText = MatContext.get().getTextSansOid(labelTitle);
		switch (diagramView) {
		case CANVAS_VIEW:
			attributeEditor = new AttributeEditor(
					this,
					phrase,
					attributeTablePanel1,
					attributeList1,
					addAttributeButton1, attributeTypeList1, attributeUpdateTypeButton1,
					attributeComparisonList1, attributeQuantity1, attributeUnit1, attributeComparisonUpdateButton1,
					attributeQDSElementList1, attributeQDSElementUpdateButton1,
					attributeEditorSaveButton1);
			attributePhraseNameLabel1.setText(labelText);
			attributePhraseNameLabel1.setTitle(labelTitle);
			setVisible(attributePhraseNameLabel1, true);
			setVisible(attributeEditorPanel1, true);
			setVisible(canvasViewToolbar, false);
			break;
		case TEXT_VIEW:
			attributeEditor = new AttributeEditor(
					this,
					phrase,
					attributeTablePanel2,
					attributeList2,
					addAttributeButton2, attributeTypeList2, attributeUpdateTypeButton2,
					attributeComparisonList2, attributeQuantity2, attributeUnit2, attributeComparisonUpdateButton2,
					attributeQDSElementList2, attributeQDSElementUpdateButton2,
					attributeEditorSaveButton2);
			attributePhraseNameLabel2.setText(labelText);
			attributePhraseNameLabel2.setTitle(labelTitle);
			setVisible(attributePhraseNameLabel2, true);
			setVisible(attributeEditorPanel2, true);	
			setVisible(textViewToolbar, false);
			break;
		default:
			return;
		}
		setToolbarTitle("Add/Edit Attributes for:", true);
	}

	@UiHandler("addAttributeButton1")
	public void onClickAddAttributeButton1(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		if(!isLoading(true))
			attributeEditor.onClickAddAttributeButton(event);
	}
	@UiHandler("addAttributeButton2")
	public void onClickAddAttributeButton2(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		if(!isLoading(true))
			attributeEditor.onClickAddAttributeButton(event);
	}

	@UiHandler("attributeUpdateTypeButton1")
	public void onClickAttributeUpdateTypeButton1(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		if(!isLoading(true))
			attributeEditor.onClickAttributeUpdateTypeButton(event);
	}
	@UiHandler("attributeUpdateTypeButton2")
	public void onClickAttributeUpdateTypeButton2(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		if(!isLoading(true))
			attributeEditor.onClickAttributeUpdateTypeButton(event);
	}

	@UiHandler("attributeComparisonUpdateButton1")
	public void onClickAttributeComparisonUpdateButton(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		if(!isLoading(true))
			attributeEditor.onClickAttributeComparisonUpdateButton(event);
	}
	@UiHandler("attributeComparisonUpdateButton2")
	public void onClickAttributeComparisonUpdateButton2(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		if(!isLoading(true))
			attributeEditor.onClickAttributeComparisonUpdateButton(event);
	}

	@UiHandler("attributeQDSElementUpdateButton1")
	public void onClickAttributeQDSElementUpdateButton1(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		if(!isLoading(true))
			attributeEditor.onClickAttributeQDSElementUpdateButton(event);
	}
	@UiHandler("attributeQDSElementUpdateButton2")
	public void onClickAttributeQDSElementUpdateButton2(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		if(!isLoading(true))
			attributeEditor.onClickAttributeQDSElementUpdateButton(event);
	}

	@UiHandler("attributeEditorSaveButton1")
	public void onClickAttributeEditorSaveButton1(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		if(!isLoading(true))
			onClickAttributeEditorSaveButton(event);
	}
	@UiHandler("attributeEditorSaveButton2")
	public void onClickAttributeEditorSaveButton2(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		if(!isLoading(true))
			onClickAttributeEditorSaveButton(event);
	}
	public void onClickAttributeEditorSaveButton(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		if(!isLoading(true)){
			switch (diagramView) {
			case CANVAS_VIEW:
				setVisible(canvasViewToolbar, true);
				break;
			case TEXT_VIEW:
				setVisible(textViewToolbar, true);
				break;
			default:
				break;
			}
			setToolbarTitle("Edit", false);
			attributeEditor.onClickAttributeEditorSaveButton(event);
			hideAttributeEditor();
			showPropertyEditor();
		}
	}

	@UiHandler("attributeEditorCancelButton1")
	public void onClickAttributeEditorCanceButton1(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		if(!isLoading(true))
			onClickAttributeEditorCanceButton(event);
	}
	@UiHandler("attributeEditorCancelButton2")
	public void onClickAttributeEditorCanceButton2(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		if(!isLoading(true))
			onClickAttributeEditorCanceButton(event);
	}	
	public void onClickAttributeEditorCanceButton(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		if(!isLoading(true)){
			switch (diagramView) {
			case CANVAS_VIEW:
				setVisible(canvasViewToolbar, true);
				break;
			case TEXT_VIEW:
				setVisible(textViewToolbar, true);
				break;
			default:
				break;
			}
			setToolbarTitle("Edit", false);
			hideAttributeEditor();
			showPropertyEditor();
		}
	}

	@Override
	public void hideAttributeEditor() {
		setVisible(attributeEditorPanel1, false);	
		setVisible(attributeEditorPanel2, false);	
	}

	protected void insertMeasurePhraseDiagramObject(DiagramObject diagramObject, String criterion) {
		presenter.addMeasurePhraseDiagramObject(diagramObject);
		appController.save(((SimpleStatement)diagramObject).getIdentity());
		if (criterion.equals(ConstantMessages.MEASURE_PHRASE_CONTEXT_DESC)) {
			drawMeasurePhrase(diagramObject);
			start();
		}
		else {
			appController.addDiagramObject(currentCriterion, currentDiagramObject, new PlaceHolder((SimpleStatement)diagramObject));
			start();
		}
	}

	@Override
	public void selectMeasurePhrase(String name) {
		for (int i = 0; i < measurePhraseListBox.getItemCount(); ++i){
			if((measurePhraseListBox.getValue(i)).equals(name)){
				measurePhraseListBox.setItemSelected(i, true);
				break;
			}
		}
		doPhraseChangeHandlerBehavior(measurePhraseListBox);
	}

	@Override
	public boolean isMeasurePhrase(String name) {
		return appController.isMeasurePhrase(name);
	}

	@Override
	public DiagramObject getMeasurePhrase(String name)  {
		return appController.getMeasurePhrase(name);
	}

	@Override
	public DiagramObject getSavedMeasurePhrase(String identity) {
		return appController.getSavedMeasurePhrase(identity);
	}
	
	private void clearMeasurePhraseArea(){
		switch (diagramView) {
		case CANVAS_VIEW:
			measurePhraseTextArea1.setText("");
			measurePhraseTextArea1.setVisible(false);
			break;
		case TEXT_VIEW:
			measurePhraseTextArea2.setText("");
			break;
		default:
			break;
		}
	}
	
	public void select(DiagramObject diagramObject) {
		if(diagramObject == null){
			clearMeasurePhraseArea();
			return;
		}
		handlePropertyGrid(diagramObject, PROPERTY_GRID_STATE.SELECT);
		if (diagramView == DIAGRAM_VIEW.CANVAS_VIEW) // && currentCriterion.equals(AppController.MEASURE_PHRASE))
			drawMeasurePhrase(diagramObject);
		else if (diagramView == DIAGRAM_VIEW.TEXT_VIEW)
			drawMeasurePhrase(diagramObject);
	}

	protected void removePropertyEditor() {
		Widget widget = propertyEditorScrollPanel1.getWidget();
		if (widget != null)
			propertyEditorScrollPanel1.remove(widget);
		hidePropertyEditor();
		showOrHideToolbars();
	}

	public void setPresenter(Presenter<T> presenter) {
		this.presenter = presenter;
	}
	
	private void clearPropEditMessages(){
		propEditErrorMessages.clear();
		propEditSuccessMessages.clear();
	}

	protected void exitCurrentEditorMessage() {
		clearPropEditMessages();
		propEditErrorMessages.setMessage("Please exit current editor first");
	}

	protected void selectCriterion(ScrollPanel scrollPanel, String criterion) {
		unhilightDiagramObject(false);
		removePropertyEditor();
		removeCanvas();
		currentScrollPanel = scrollPanel;
		currentCriterion = criterion;
		currentDiagramObject = null;

		if (presenter != null)
			presenter.selectCriterion(criterion);
		if (criterion.equals(ConstantMessages.MEASURE_PHRASE_CONTEXT_DESC)) {
			removePropertyEditor();
			DiagramObject diagramObject = presenter.getMeasurePhrase(getSelectedMeasurePhrase());
			select(diagramObject);
		}
		else {
			start();
			drawDiagram();
		}
	}

	private void selectCriterion(FlowPanel panel, String criterion) {
		removePropertyEditor();
		removeCanvas();
		//if (diagramView == DIAGRAM_VIEW.CANVAS_VIEW)
		currentCriterion = criterion;
		if (presenter != null)
			presenter.selectCriterion(criterion);
		DiagramObject diagramObject = presenter.getMeasurePhrase(getSelectedMeasurePhrase());
		select(diagramObject);
	}

	protected void removeCanvas() {
		if (canvas != null)
			currentScrollPanel.remove(canvas);	
	}

	private void unhilightDiagramObject(boolean editButtonVisible) {
		if (currentDiagramObject != null) {
			currentDiagramObject.setHilighted(false);
			drawDiagramObject();
			currentDiagramObject = null;
		}
	}

	@Override
	public void hilightDiagramObject(DiagramObject diagramObject, ClickEvent event) {
		clearPropEditMessages();
		
		unhilightDiagramObject(true);

		diagramObject.setHilighted(true);
		currentDiagramObject = diagramObject;
		drawDiagramObject();

		updateButtonGridForSelectedDialogObject();
		initButtons();
		updateEditButtons(true);

		NativeEvent nativeEvent = event.getNativeEvent(); 
		try {
			if (nativeEvent.getShiftKey())
				if (!(diagramObject instanceof PlaceHolder))
					handlePropertyGrid(diagramObject, PROPERTY_GRID_STATE.SELECT);
		} catch(Throwable e) {
			/**
			 * TODO why are we arriving here after deleting a criterion from a diagram 
			 * and then selecting one of the remaining criterions?
			 */
			e.printStackTrace();
		}
	}

	private void updateButtonGridForSelectedDialogObject() {
		boolean visible = !(currentDiagramObject instanceof PlaceHolder);
		setVisible(qdselButton1, visible);
		setVisible(andButton1, visible);
		setVisible(orButton1, visible);
		setVisible(timingButton1, visible);
		setVisible(addPhraseButton1, visible);
		setVisible(insertPhraseButton1, visible && (appController.getMeasurePhraseList().size() > 0));
	}

	@UiHandler("qdselButton1")
	public void onClickQdselButton1(ClickEvent event) {
		if(!isLoading(true))
			onClickQdselButton(event);
	}
	
	public void onClickQdselButton(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		if(!isLoading(true))
			handlePropertyGrid(new Qdsel());
	}

	@UiHandler("criterionButton1")
	public void onClickCriterionButton1(ClickEvent event) {
		if(!isLoading(true))
			onClickCriterionButton(event);
	}

	@UiHandler("criterionButton2")
	public void onClickCriterionButton2(ClickEvent event) {
		if(!isLoading(true))
			onClickCriterionButton(event);
	}

	public void onClickCriterionButton(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		DiagramObject newCriterionObject = appController.addCriterion(currentCriterion);
		appController.addConditional(currentCriterion, (Criterion) newCriterionObject);
		if (diagramView == DIAGRAM_VIEW.CANVAS_VIEW) {
			drawDiagram();
			if (currentDiagramObject != null)
				hilightDiagramObject(currentDiagramObject, event);
		}
		else {
			drawDiagramTree();
			TreeItem item = selectDiagramTreeObject(newCriterionObject);
			if (item != null){
				selectDiagramTreeItem(item);
				fireAlert(item.getElement());
			}
		}
		//Auto Save Commented for Canvas Sub Tabs
		//appController.saveMainPhrases();
	}

	@UiHandler("pasteCloneButton1")
	public void onClickPasteCloneButton1(ClickEvent event) {
		if(!isLoading(true))
			onClickPasteCloneButton(event);
	}

	public void onClickPasteCloneButton(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		if (clauseToBeCloned == null) {
			clearPropEditMessages();
			propEditErrorMessages.setMessage(MatContext.get().getMessageDelegate().getPleaseSelectClauseMessage());
		} else {
			
			String criterionNameToClone = getCurrentCriterion();
			String name = clauseToBeCloned.getClauseName();
			String measureID = clauseToBeCloned.getMeasureID();
			appController.cloneSystemClause(name, criterionNameToClone, measureID);
			clauseToBeCloned = null;
			//Auto Save Commented for Canvas Sub Tabs
			//appController.saveMainPhrases();
			moveFocusToPropertyEditor();
		}
	}

	@UiHandler("andButton1")
	public void onClickAndButton1(ClickEvent event) {
		if(!isLoading(true))
			onClickAndButton(event);
	}
	
	public void onClickAndButton(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		addConditional("AND");
	}

	@UiHandler("orButton1")
	public void onClickOrButton1(ClickEvent event) {
		if(!isLoading(true))
			onClickOrButton(event);
	}
		
	public void onClickOrButton(ClickEvent event) {	
		MatContext.get().clearDVIMessages();
		addConditional("OR");
	}

	protected void addConditional(String condition) {
		MatContext.get().clearDVIMessages();
		if (!appController.canAddConditional(currentDiagramObject)) {
			propEditErrorMessages.setMessage(MatContext.get().getMessageDelegate().getOneAndMessage());
			return;
		}
		DiagramObject newConditional = (DiagramObject)(new Conditional(condition));
		appController.addDiagramObject(currentCriterion, currentDiagramObject, newConditional);
		if (diagramView == DIAGRAM_VIEW.TEXT_VIEW) {
			drawDiagramTree();
			selectDiagramTreeObject(newConditional);
		}
	}

	@UiHandler("timingButton1")
	public void onClickTimingButton1(ClickEvent event) {
		if(!isLoading(true))
			onClickTimingButton(event);
	}
	
	public void onClickTimingButton(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		handlePropertyGrid(new Rel());
	}

	@UiHandler("addPhraseButton1")
	public void onClickAddPhraseButton1(ClickEvent event) {
		if(!isLoading(true))
			onClickAddPhraseButton(event);
	}
	@UiHandler("addPhraseButton2")
	public void onClickAddPhraseButton2(ClickEvent event) {
		if(!isLoading(true))
			onClickAddPhraseButton(event);
	}	
	public void onClickAddPhraseButton(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		deselectLibrary();
		handlePropertyGrid(new SimpleStatement(appController));
	}

	@UiHandler("insertPhraseButton1")
	public void onClickInsertPhraseButton1(ClickEvent event) {
		if(!isLoading(true))
			onClickInsertPhraseButton(event);
	}
	@UiHandler("insertPhraseButton2")
	public void onClickInsertPhraseButton2(ClickEvent event) {
		if(!isLoading(true))
			onClickInsertPhraseButton(event);
	}	
	public void onClickInsertPhraseButton(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		handlePropertyGrid(new InsertPhrase());
	}

	@UiHandler("cutButton1")
	public void onCutButton1Clicked(ClickEvent event) {
		if(!isLoading(true))
			onCutButtonClicked(event);
	}
		
	public void onCutButtonClicked(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		onCopyButtonClicked(event);
		if (!appController.deleteDiagramObject(currentCriterion, currentDiagramObject))
			System.out.println("Unable to delete!");
		appController.setDiagramDirty(currentCriterion);

		currentDiagramObject = null;
		if (diagramView == DIAGRAM_VIEW.TEXT_VIEW) {
			redrawDiagramTree(null);
			initButtons(currentCriterion);
			updateEditButtons(false);
		}
		initButtons(currentCriterion);
	}

	@UiHandler("copyButton1")
	public void onCopyButton1Clicked(ClickEvent event) {
		if(!isLoading(true))
			onCopyButtonClicked(event);
	}
	
	public void onCopyButtonClicked(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		clipboardTraversalTree = appController.findDiagramObject(currentDiagramObject).clone(appController);
		updateEditButtons(true);
	}

	@UiHandler("pasteButton1")
	public void onPasteButton1Clicked(ClickEvent event) {
		if(!isLoading(true))
			onPasteButtonClicked(event);
	}
		
	public void onPasteButtonClicked(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		if (clipboardTraversalTree == null)
			return;

		appController.setDiagramDirty(currentCriterion);

		DiagramObject diagramObject = clipboardTraversalTree.getDiagramObject();
		boolean doSave = true;
		if (currentCriterion.equals(ConstantMessages.USER_DEFINED_CONTEXT_DESC)) {
			if (diagramObject instanceof Criterion) {
				doSave = false;
				pasteCriterionIntoUserDefined(diagramObject);
			} else
				appController.findDiagramObject(currentDiagramObject).addChild(clipboardTraversalTree.clone(appController));
		}	
		else {
			if (diagramObject instanceof Criterion)
				appController.addCriterion(currentCriterion, clipboardTraversalTree);
			else
				appController.findDiagramObject(currentDiagramObject).addChild(clipboardTraversalTree.clone(appController));
		}
		drawDiagram();
		if (diagramView == DIAGRAM_VIEW.TEXT_VIEW)
			redrawDiagramTree(null);		

		unhilightDiagramObject(false);
		initButtons();
		updateEditButtons(false);
		
		//******************
		//Auto Save Commented for Canvas Sub Tabs
		/*if(doSave)
			appController.saveMainPhrases();	*/	
		//******************
	}

	@UiHandler("userDefinedPastedCriterionOkButton")
	public void onUserDefinedPastedCriterionOkButtonClicked(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		if(!isLoading(true)){
			clearPropEditMessages();
			String newName = userDefinedPastedCriterionTextBox.getText().trim();
			for (int i = 0; i < clauseLibraryListBox.getItemCount(); ++i)
				if (clauseLibraryListBox.getItemText(i).equals(newName)) {
					propEditErrorMessages.setMessage(MatContext.get().getMessageDelegate().getAlreadyExistsMessage(newName));
					userDefinedPastedCriterionTextBox.setText("");
					userDefinedPastedCriterionTextBox.setFocus(true);
					return;
				}
	
			setVisible(userDefinedPastedCriterionPanel, false);
			
			//the "true" parameter is used only to identify "User-defined" as a "system clause"
			TraversalTree clone = clipboardTraversalTree.clone(appController, true);
			//clone.setName(newName);
			//TODO derive identity value based on Parent Clause.
			String systemClauseName = appController.getNewClauseName("User-defined");
			clone.getDiagramObject().setIdentity(systemClauseName);
			clone.getDiagramObject().setCustomName(newName);
			
	
			appController.findCriterion(currentCriterion).addChild(clone);
			
			String text = clone.getName();
			String title = clone.getName();
			String value = MatContext.get().getCurrentMeasureId();
			clauseLibraryListBox.insertItem(text, value, title, -1);
//			clauseLibraryListBox.addItem(clone.getName(), clone.getName());
			//clauseLibraryListBox.setValue(clauseLibraryListBox.getItemCount() - 1, ConstantMessages.USER_DEFINED_CONTEXT_DESC);
			
			//since clauseLibraryListBox is being populated,
			//we must add a clause to the library list, this list
			//is used to keep track of all system clauses
			appController.addClauseToLibList(systemClauseName, MatContext.get().getCurrentMeasureId(), newName);
	
			endUserDefinedPastedCriterionEdit();
			drawDiagram();
	
			libraryTabPanel.selectTab(CLAUSE_LIBRARY_TAB);
			
			//******************
			//Auto Save Commented for Canvas Sub Tabs
		//	appController.saveMainPhrases();		
		//******************
		}
	}

	@UiHandler("userDefinedPastedCriterionCancelButton")
	public void onUserDefinedPastedCriterionCancelButtonClicked(ClickEvent event) {
		if(!isLoading(true)){
			MatContext.get().clearDVIMessages();
			endUserDefinedPastedCriterionEdit();
		}
	}

	private void endUserDefinedPastedCriterionEdit() {
		setVisible(userDefinedPastedCriterionPanel, false);
		switch (diagramView) {
		case CANVAS_VIEW:
			setVisible(propertyEditor1Top, true);
			setVisible(textViewToolbar, true);
			setVisible(propertyEditor1, true);
			setVisible(page1Buttons, true);
			break;
		case TEXT_VIEW:
			setVisible(propertyEditor2Top, true);
			setVisible(canvasViewToolbar, true);
			setVisible(propertyEditor2, true);
			setVisible(page2Buttons, true);
			break;
		default:
			return;
		}
	}

	public void pasteCriterionIntoUserDefined(DiagramObject diagramObject) {
		switch (diagramView) {
		case CANVAS_VIEW:
			setVisible(canvasViewToolbar, false);
			setVisible(propertyEditor1Top, false);
			setVisible(propertyEditor1, false);
			setVisible(page1Buttons, false);
			break;
		case TEXT_VIEW:
			setVisible(textViewToolbar, false);
			setVisible(propertyEditor2Top, false);
			setVisible(propertyEditor2, false);
			setVisible(page2Buttons, false);
			break;
		default:
			return;
		}

		String caption = "Enter name:";
		userDefinedPastedCriterionCaption.setText(caption);
		userDefinedPastedCriterionCaption.setTitle(caption);
		userDefinedPastedCriterionTextBox.setText("");

		setVisible(userDefinedPastedCriterionPanel, true);
		userDefinedPastedCriterionTextBox.setFocus(true);
	}

	@UiHandler("deleteButton1")
	public void onDeleteButton1Clicked(ClickEvent event) {
		if(!isLoading(true))
			onDeleteButtonClicked(event);
	}
	@UiHandler("deleteButton2")
	public void onDeleteButton2Clicked(ClickEvent event) {
		if(!isLoading(true))
			onDeleteButtonClicked(event);
	}
	public void onDeleteButtonClicked(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		boolean showAlert = false;
		
		if (currentDiagramObject instanceof Criterion) {
			Criterion criterion = (Criterion)currentDiagramObject;
			
			if(appController.canDeleteClause(criterion)) {
				if (!appController.deleteCriterion(currentCriterion, currentDiagramObject))
					/**
					 * TODO Should we display an alert or is this just a debug sysout?
					 */
					System.out.println("Unable to delete!");
			} else 
				showAlert = true;
			
		} else {
			if (!appController.deleteDiagramObject(currentCriterion, currentDiagramObject))
				/**
				 * TODO Should we display an alert or is this just a debug sysout?
				 */
				System.out.println("Unable to delete!");
		}
		
		if(showAlert) {
			clearPropEditMessages();
			propEditErrorMessages.setMessage("Unable to delete!");
		} else {
			appController.setDiagramDirty(currentCriterion);
			currentDiagramObject = null;
			if (diagramView == DIAGRAM_VIEW.TEXT_VIEW)
				redrawDiagramTree(null);
			initButtons(currentCriterion);
			updateEditButtons(false);
		}
	}

	@UiHandler("editButton1")
	public void onEditButton1Clicked(ClickEvent event) {
		if(!isLoading(true))
			onEditButtonClicked(event);
	}
	@UiHandler("editButton2")
	public void onEditButton2Clicked(ClickEvent event) {
		if(!isLoading(true))
			onEditButtonClicked(event);
	}	
	public void onEditButtonClicked(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		if (currentDiagramObject instanceof PlaceHolder)
			selectMeasurePhrase(((PlaceHolder)currentDiagramObject).getIdentity());
		else
			handlePropertyGrid(currentDiagramObject, PROPERTY_GRID_STATE.SELECT);
	}

	@Override
	public String getCurrentCriterion() {
		return currentCriterion;
	}

	@Override
	public void drawMeasurePhrase(DiagramObject diagramObject) {
		if (!(currentCriterion.equals(ConstantMessages.MEASURE_PHRASE_CONTEXT_DESC))) {
			setVisible(measurePhrasePanel, true);
			selectCriterion(measurePhrasePanel, ConstantMessages.MEASURE_PHRASE_CONTEXT_DESC);
		}
		String prettyPrintedFullText = ((SimpleStatement)diagramObject).getPrettyPrintedFullText();	
		showMeasurePhraseTextArea();
		switch (diagramView) {
		case CANVAS_VIEW:
			measurePhraseTextArea1.setText(prettyPrintedFullText);
			initButtons("Measure Phrase");
			updateEditButtons(false);
			break;
		case TEXT_VIEW:
			measurePhraseTextArea2.setText(prettyPrintedFullText);
			currentDiagramObject = diagramObject;
			deselectDiagramTreeItem();//This will prevent the wrong display of buttons.
			initButtons("Measure Phrase");
			break;
		default:
			break;
		}
		ignoreCriterionPanelSelection = true;
		criterionPanel.forceSelectTab(criterionPanel.getWidgetIndex(measurePhrasePanel));
	}

	private void drawOnNewCanvas(TraversalTree tree) {
		removeCanvas();
		int right = Math.max(1000, diagram.getRight());
		int bottom = Math.max(500, diagram.getBottom());
		canvas = new DrawingArea(right, bottom);
		canvas.addStyleName("canvas");

		currentScrollPanel.add(canvas);
		drawTree(tree, 0, true);
	}

	@Override
	public void drawDiagramObject() {
		if (currentDiagramObject == null)
			return;
		TraversalTree tree = appController.findDiagramObject(currentCriterion, currentDiagramObject);
		if (tree == null)
			return;
		switch (diagramView) {
		case CANVAS_VIEW:
			tree.getDiagramShape().draw(0, true);
			break;
		case TEXT_VIEW:
			break;
		default:
			tree.getDiagramShape().draw(diagram.getTop(), false);
		break;
		}
	}

	@Override
	public void drawDiagram() {
		/*if (currentScrollPanel == previewCanvasPanel) {
			drawPreview();
			return;
		}*/
		if (currentScrollPanel == textScrollPanel) {
			return;
		}

		TraversalTree tree;

		removeCanvas();
		diagram = resetDiagram();
		tree = diagram.getTree();
		diagram.reset();

		if (calcTreeExtent(tree))
			drawOnNewCanvas(tree);
	}

	protected Diagram resetDiagram() {
		Diagram diagram;
		if (currentCriterion.equals(ConstantMessages.MEASURE_PHRASE_CONTEXT_DESC))
			diagram = appController.getNewDiagram();
		else {
			diagram = appController.getCurrentDiagram(currentCriterion);
			diagram.setTop(0);
		}
		return diagram;
	}

	@Override
	public void calcExtent(int right, int bottom) {
		diagram.calcExtent(right, bottom);
	}

	protected boolean calcTreeExtent(TraversalTree t) {
		if (t == null) {
			System.out.println("DiagramViewImpl.drawTree: traversal tree is null!");
			return false;
		}		
		DiagramObject diagramObject = t.getDiagramObject();
		if (diagramObject == null) {
			System.out.println("DiagramViewImpl.drawTree: diagramObject is null!");
			return false;
		}	
		t.setDiagramShape(DiagramShapeFactory.getShape(me, t, diagramObject));
		t.getDiagramShape().calcExtent();

		for (TraversalTree c : t.getChildren())
			calcTreeExtent(c);

		return true;
	}

	protected void drawTree(TraversalTree t, boolean clickable) {
		drawTree(t, 0, clickable);
	}

	protected void drawTree(TraversalTree t, int top, boolean clickable) {
		MatContext.get().clearDVIMessages();
		zoomCanvasDelegate.drawReset(top);
		zoomCanvasDelegate.drawMinus(top);
		zoomCanvasDelegate.drawPlus(top);
		drawBoxes(t, top, clickable);
		drawConnectors(t, top);
	}

	@UiHandler("diagramTreeExpandButton")
	public void onClickDiagramTreeExpandButton(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		if(!isLoading(true)){
			setVisible(diagramTreeExpandButton, false);
			setVisible(diagramTreeCollapseButton, true);
			expandDiagramTree();
		}
	}

	private void expandDiagramTree() {
		for (Iterator<TreeItem> treeItemIterator = diagramTree.treeItemIterator(); treeItemIterator.hasNext(); ) {
			TreeItem next = treeItemIterator.next();
			next.setState(true);
		}
	}

	@UiHandler("diagramFocus")
	public void diagramKeyDownEvents(KeyDownEvent event) {
		//Control-Alt-s
		MatContext.get().clearDVIMessages();
		if(event.isAltKeyDown() && event.isControlKeyDown() && event.getNativeKeyCode()==83  && editable){
			//Auto Save Commented for Canvas Sub Tabs
			// appController.saveMainPhrases();
		    Command waitForSave = new Command(){
		    	@Override
		    	public void execute() {
		    		if(!MatContext.get().getSynchronizationDelegate().isSavingClauses()){
		    			if(isSaveStatus())
		    				showSavedMessage(true);
		    			else
		    				showSavedMessage(false);
		    		}else{
		    			DeferredCommand.addCommand(this);
		    		}	
		    	}
		    };
		    waitForSave.execute();	
		}
	}
	
	/*@UiHandler("diagramFocus")
	public void mouseEvents(ClickEvent event) {
//		MatContext.get().clearDVIMessages();
		//MatContext.get().restartTimeoutWarning();
	}*/
	
	@UiHandler("diagramTreeCollapseButton")
	public void onClickDiagramTreeCollapseButton(ClickEvent event) {
		MatContext.get().clearDVIMessages();
		if(!isLoading(true)){
			setVisible(diagramTreeExpandButton, true);
			setVisible(diagramTreeCollapseButton, false);		
			collapseDiagramTree();
		}
	}

	private void deselectDiagramTreeItem(){
		if(diagramTree.getSelectedItem() != null){
			TreeItem selectedItem = diagramTree.getSelectedItem();
			selectedItem.setSelected(false);
		}
		diagramTree.setSelectedItem(diagramTree.getItem(-1));//This will make No selection in the tree. 
	}
	
	private void collapseDiagramTree() {
		for (Iterator<TreeItem> treeItemIterator = diagramTree.treeItemIterator(); treeItemIterator.hasNext(); ) {
			TreeItem next = treeItemIterator.next();
			next.setState(false);
		}
	}

	/*
	@SuppressWarnings("unchecked")
	SelectionHandler<TreeItem> diagramTreeSelectionHandler = new MATSelectionHandler() {
		@Override
		public void onEvent(GwtEvent event) {
			removePropertyEditor();
			hideMeasurePhraseTextArea();
			TreeItem item = (TreeItem) ((SelectionEvent)event).getSelectedItem();
			Object userObject = item.getUserObject();
			if (userObject != null) {
				DiagramObject diagramObject = (DiagramObject)userObject;
				currentDiagramObject = diagramObject;
			}
			initButtons();
			showOrHideToolbars();
		}
	};
	*/
	@SuppressWarnings("unchecked")
	SelectionHandler<TreeItem> diagramTreeSelectionHandler = new SelectionHandler() {
		@Override
		public void onSelection(SelectionEvent event) {
			MatContext.get().clearDVIMessages();
			removePropertyEditor();
			hideMeasurePhraseTextArea();
			TreeItem item = (TreeItem) ((SelectionEvent)event).getSelectedItem();
			Object userObject = item.getUserObject();
			if (userObject != null) {
				DiagramObject diagramObject = (DiagramObject)userObject;
				currentDiagramObject = diagramObject;
			}
			initButtons();
			showOrHideToolbars();
		}
	};

	private void redrawDiagramTree(DiagramObject diagramObject) {
		currentDiagramObject = diagramObject;
		TreeItem selectedItem = diagramTree.getSelectedItem();

		if (diagramObject == null) {
			TreeItem parentItem = selectedItem.getParentItem();
			parentItem.removeItem(selectedItem);
			selectedItem = parentItem;
		}
		else
			selectedItem.setText(diagramObject.getIdentity());

		diagramTree.setSelectedItem(selectedItem);
		diagramTree.ensureSelectedItemVisible();
		handleExpansionImageStatus();
		hidePropertyEditor();
	}

	public void drawDiagramTree() {
		unhilightDiagramObject(false);
		initButtons();

		if (addDiagramTreeSelectionHandler != null)
			addDiagramTreeSelectionHandler.removeHandler();
		diagramTree.clear();
		addDiagramTreeSelectionHandler = diagramTree.addSelectionHandler(diagramTreeSelectionHandler);

		TreeItem rootItem = new TreeItem(appController.getMeasureName());
		diagramTree.addItem(rootItem);

		for (int i = 0; i < appController.criterionNames.size() - 2; ++i) {	// In diagramTree remove the User-defined and Measure phrase node.
			String name = AppController.criterionNames.get(i);
			diagram = appController.getCurrentDiagram(name);
			TraversalTree traversalTree = diagram.getTree();
			if (traversalTree == null || traversalTree.getDiagramObject() == null) 
				continue;

			DiagramObject diagramObject = traversalTree.getDiagramObject();
			TreeItem criterionItem = new TreeItem(diagramObject.getIdentity());
			criterionItem.setUserObject(diagramObject);
			rootItem.addItem(criterionItem);

			for (TraversalTree c : traversalTree.getChildren()) 
				drawDiagramTreeChildren(criterionItem, c);
		}
		setVisible(diagramTreeExpandButton, true);
		setVisible(diagramTreeCollapseButton, false);
		handleExpansionImageStatus();
	}

	private void drawDiagramTreeChildren(TreeItem criterionItem, TraversalTree t) {
		DiagramObject diagramObject = t.getDiagramObject();
		TreeItem item = new TreeItem(diagramObject.getIdentity());
		item.setUserObject(diagramObject);
		criterionItem.addItem(item);
		for (TraversalTree c : t.getChildren()) 
			drawDiagramTreeChildren(item, c);
	}

	
	private void drawDiagramTreeChildMeasurePhrases(String phraseName, TreeItem measurePhraseItem) {
		TreeItem childMeasurePhraseItem = measurePhraseItem.addItem(phraseName);
		SimpleStatement measurePhrase = (SimpleStatement)(appController.getMeasurePhrase(phraseName));
		childMeasurePhraseItem.setUserObject(measurePhrase);

		List<String>childMeasurePhraseList = measurePhrase.getChildMeasurePhrases(this);
		for (String childMeasurePhraseName : childMeasurePhraseList)
			drawDiagramTreeChildMeasurePhrases(childMeasurePhraseName, childMeasurePhraseItem);
	}

	private TreeItem selectDiagramTreeObject(DiagramObject selectedDiagramObject) {
		currentDiagramObject = selectedDiagramObject;
		initButtons();
		updateEditButtons(true);
		for (int i = 0; i < diagramTree.getItemCount(); ++i) {
			TreeItem item = selectDiagramTreeObject(diagramTree.getItem(i), selectedDiagramObject);
			if (item != null)
				return item;
		}
		return null;
	}

	private TreeItem selectDiagramTreeObject(TreeItem item, DiagramObject selectedDiagramObject) {
		Object userObject = item.getUserObject();
		if (userObject != null) {
			DiagramObject diagramObject = (DiagramObject)userObject;
			if (diagramObject == selectedDiagramObject)
				return item;
		}

		for (int i = 0; i < item.getChildCount(); ++i) {
			TreeItem childItem = selectDiagramTreeObject(item.getChild(i), selectedDiagramObject);
			if (childItem != null)
				return childItem;
		}

		return null;
	}

	private void selectDiagramTreeItem(TreeItem item) {
		diagramTree.setFocus(true);
		item.setVisible(true);
		item.setState(true);
		expandDiagramTree();
		item.getTree().setSelectedItem(item, true);
		diagramTree.ensureSelectedItemVisible();
	}

	private void drawBoxes(TraversalTree t) {
		drawBoxes(t, 0, true);
	}

	private void drawBoxes(TraversalTree t, int top, boolean clickable) {
		t.getDiagramShape().draw(top, clickable);
		DiagramObject diagramObject = t.getDiagramObject();
		if (diagramObject.isExpanded() && !(diagramObject instanceof PlaceHolder))
			for (TraversalTree c : t.getChildren()) 
				drawBoxes(c, top, clickable);	
	}		

	private void drawConnectors(TraversalTree t, int top) {
		DiagramObject diagramObject = t.getDiagramObject();
		if (diagramObject.isExpanded() && !(diagramObject instanceof PlaceHolder)) {
			for (TraversalTree c : t.getChildren()) {
				if (c.getDiagramObject() != null) {
					new SquareConnector(canvas, t.getDiagramShape().getRectangle(), c.getDiagramShape().getRectangle()).draw(top);
					drawConnectors(c, top);
				}
			}
		}
	}

	@Override
	public Diagram getDiagram() {
		return diagram;
	}

	public void refreshSystemClauses(List<Clause> systemClauses) {
		clauseLibraryListBox.clear();	
		for (Clause item : systemClauses) {
			String name;
			if (item.getCustomName()==null) {
				name = item.getName();
			} else {
				name = item.getCustomName();
			}
			String text = item.getVersion()+name;
			String title = item.getVersion()+name;
			String value = item.getMeasureId();
			clauseLibraryListBox.insertItem(text, value, title, -1);
//			clauseLibraryListBox.addItem(item.getVersion()+name,name);
		}
	}
	
	public void refreshSystemClausesDiagrams(List<String> systemClauses){
		appController.refreshSystemClauses(systemClauses);
	}
	
	/**
	 * api to control phrase and clause library selection and handlers
	 */
	@Override
	public void disableLibraries(String source){
		//O&M 17 disable system clause tabs
		if(source.equalsIgnoreCase(CLASS_NAME)){
			((Enableable)MatContext.get().enableRegistry.get(ConstantMessages.MAIN_TAB_LAYOUT_ID)).setEnabled(false);
			((Enableable)MatContext.get().enableRegistry.get(ConstantMessages.MEASURE_COMPOSER_TAB)).setEnabled(false);
		}
		criterionPanel.setEnabled(false);
		if(clauseController.getQDSCodeListSearchPresenter() != null){
			clauseController.getQDSCodeListSearchPresenter().setEnabled(false);
		}
		
		saveButton.setEnabled(false);
		cloneButton.setEnabled(false);
		if(libraryTabPanelHandlerRegistration != null){
			libraryTabPanelHandlerRegistration.removeHandler();
			libraryTabPanelHandlerRegistration = null;
		}
		measurePhraseListBox.setEnabled(false);
		clauseLibraryListBox.setEnabled(false);
		if(measurePhraseListBoxHandlerRegistration != null){
			measurePhraseListBoxHandlerRegistration.removeHandler();
			measurePhraseListBoxHandlerRegistration = null;
		}	
		if(clauseLibraryListBoxHandlerRegistration != null){
			clauseLibraryListBoxHandlerRegistration.removeHandler();
			clauseLibraryListBoxHandlerRegistration = null;
		}
		
		setButtonsEnabled(false);
		textViewButton1Inline.setEnabled(false);
	}
	
	/**
	 * api to control phrase and clause library selection and handlers
	 */
	@Override
	public void enableLibraries(String source){
		//O&M 17 enable system clause tabs
		((Enableable)MatContext.get().enableRegistry.get(ConstantMessages.MAIN_TAB_LAYOUT_ID)).setEnabled(true);
		((Enableable)MatContext.get().enableRegistry.get(ConstantMessages.MEASURE_COMPOSER_TAB)).setEnabled(true);
		criterionPanel.setEnabled(true);
		clauseController.getQDSCodeListSearchPresenter().setEnabled(true);
		
		saveButton.setEnabled(true);
		cloneButton.setEnabled(true);
		libraryTabPanelHandlerRegistration = libraryTabPanel.addSelectionHandler(libraryTabPanelSelectionHandler);
		measurePhraseListBoxHandlerRegistration = measurePhraseListBox.addChangeHandler(measurePhraseChangeHandler);
		clauseLibraryListBoxHandlerRegistration = clauseLibraryListBox.addChangeHandler(measurePhraseChangeHandler);
		measurePhraseListBox.setEnabled(true);
		clauseLibraryListBox.setEnabled(true);
		
		setButtonsEnabled(true);
		textViewButton1Inline.setEnabled(true);
	}
	
	private void setButtonsEnabled(boolean enabled){
		criterionButton1.setEnabled(enabled);
		pasteCloneButton1.setEnabled(enabled); 
		qdselButton1.setEnabled(enabled); 
		andButton1.setEnabled(enabled); 
		orButton1.setEnabled(enabled); 
		timingButton1.setEnabled(enabled); 
		addPhraseButton1.setEnabled(enabled); 
		insertPhraseButton1.setEnabled(enabled); 
		deleteButton1.setEnabled(enabled);
		cutButton1.setEnabled(enabled); 
		copyButton1.setEnabled(enabled); 
		pasteButton1.setEnabled(enabled); 
		deleteButton1.setEnabled(enabled); 
		editButton1.setEnabled(enabled);
	}
	
	//US201 giving access to the library panel to AppController
	public void selectPhraseLibrary(){
		libraryTabPanel.selectTab(MEASURE_PHRASE_LIBRARY_TAB);
	}

	@Override
	public void refreshMeasurePhrases(List<String> measurePhraseList) {
		measurePhraseListBox.clear();
		//clauseLibraryListBox.clear();

		for (String item : measurePhraseList) {
			SimpleStatement measurePhrase = (SimpleStatement)(appController.getMeasurePhrase(item));
			if (measurePhrase.getClauseType() == SimpleStatement.CLAUSE_TYPE.MEASURE_PHRASE)
				measurePhraseListBox.addItem(item);
			else{
				/*TODO if this is invoked set text, value, and title in clauseLibraryListBox*/
				clauseLibraryListBox.addItem(item,item);
			}
		}	

		measurePhraseListBox.setVisibleItemCount(10);
		clauseLibraryListBox.setVisibleItemCount(10);

		//O&M 17 do not enable "Existing Phrase" button, if needed invoke initButtons()
//		boolean visible = !(currentCriterion.equals(ConstantMessages.MEASURE_PHRASE_CONTEXT_DESC)) && (appController.getMeasurePhraseList().size() > 0);
//		setVisible(insertPhraseButton1, visible);	
		deselectLibrary();
	}

	protected String getSelectedMeasurePhrase() {
		ListBox listBox = (libraryTabPanel.getTabBar().getSelectedTab() == MEASURE_PHRASE_TAB) ? measurePhraseListBox : clauseLibraryListBox;
		int selectedIndex = listBox.getSelectedIndex();
		return selectedIndex >= 0 ? listBox.getValue(selectedIndex) : null;
	}

	@Override
	public void showSavedMessage(final boolean success) {
		MatContext.get().clearDVIMessages();
		//messageHolder.clear();
		Timer t = new Timer() {
			@Override
			public void run() {
				
			if(success){
				saveSuccessMessages.setMessage(MatContext.get().getMessageDelegate().getChangesSavedMessage());
				messageHolder.add(saveSuccessMessages);
				fireAlert(saveSuccessMessages);
			}else{
				saveErrorMessages.setMessage("Changes are not saved.");
				messageHolder.add(saveErrorMessages);
			}
		
			}
			
		};
		//This works even at one millisecond.
		//TODO refactor to be atomic so the save message is not being cleared by some other event.
		t.schedule(10);
		
	}
	
	public ErrorMessageDisplay getPropEditErrorMessages(){
		return propEditErrorMessages;
	}
	
	public void clearMessages(){
		propEditErrorMessages.clear();
		propEditSuccessMessages.clear();
		saveErrorMessages.clear();
		saveSuccessMessages.clear();
		
		//Do we need to do this also?
		messageHolder.clear();
	}

	@Override
	public DrawingArea getCanvas() {
		return canvas;
	}

	private HandlerRegistration addDiagramTreeSelectionHandler;

	public Widget asWidget() {
		return this;
	}

	@Override
	public void measurePhrases(List<String> measurePhraseList) {
	}

	@Override
	public void showMeasurePhrases(List<String> measurePhraseList) {
	}

	@Override
	public AppController getAppController() {
		return appController;
	}

	public void setDiagramDirty() {
		appController.setDiagramDirty(currentCriterion);
	}

	@Override
	public boolean isEditable() {
		return editable;
	}
	
	private TraversalTree getTraversalTree(TraversalTree tree) {
		if(tree == null)
			tree = this.diagram.getTree();
		if(tree.getDiagramObject().equals(this.currentDiagramObject))
			return tree;
		if(tree.isLeaf())
			return null;
		for(TraversalTree tt : tree.getChildren()) {
			TraversalTree ct = getTraversalTree(tt);
			if(ct != null)
				return ct;
		}
		return null;
	}
	
	private boolean hasTimingChild(TraversalTree tree) {
		if(!tree.isLeaf())
			for(TraversalTree treeChild : tree.getChildren())
				if(treeChild.getDiagramObject() != null && treeChild.getDiagramObject() instanceof Rel)
					return true;
		return false;
	}
	
	private boolean isTopLevelAnd(TraversalTree tree){
		return (tree.getParent().getDiagramObject() instanceof Criterion);
	}
	
	//Phrase, Rel, Conditional, Criterion
	private void initRelButtons(TraversalTree tree, Button criterionButton, Button pasteCloneButton, Button qdselButton, Button andButton,
			Button orButton, Button timingButton, Button addPhraseButton,
			Button insertPhraseButton, Button deleteButton) {
		setVisible(pasteCloneButton, true);
		setVisible(timingButton, false);
		setVisible(deleteButton, true);
		boolean hasChildren = !tree.isLeaf(); 
		setVisible(qdselButton, hasChildren);
		setVisible(andButton, hasChildren);
		setVisible(orButton, hasChildren);
		setVisible(addPhraseButton, hasChildren);
		setVisible(insertPhraseButton,hasChildren);
	}
	
	private void initConditionalButtons(TraversalTree tree, Button criterionButton, Button pasteCloneButton, Button qdselButton, Button andButton,
			Button orButton, Button timingButton, Button addPhraseButton,
			Button insertPhraseButton, Button deleteButton) {
		setVisible(pasteCloneButton, true);
		setVisible(timingButton, hasTimingChild(tree));
		setVisible(deleteButton, isTopLevelAnd(tree));
		setVisible(qdselButton, true);
		setVisible(andButton, true);
		setVisible(orButton, true);
		setVisible(addPhraseButton, true);
		setVisible(insertPhraseButton, true);
	}
	
	private void initCriterionButtons(TraversalTree tree, Button criterionButton, Button pasteCloneButton, Button qdselButton, Button andButton,
			Button orButton, Button timingButton, Button addPhraseButton,
			Button insertPhraseButton, Button deleteButton) {
		setVisible(pasteCloneButton, true);
		setVisible(timingButton, false);
		setVisible(deleteButton, false);
		setVisible(qdselButton, false);
		setVisible(andButton, false);
		setVisible(orButton, false);
		setVisible(addPhraseButton, false);
		setVisible(insertPhraseButton, false);
	}
	
	private void initPhraseButtons(TraversalTree tree, Button criterionButton, Button pasteCloneButton, Button qdselButton, Button andButton,
			Button orButton, Button timingButton, Button addPhraseButton,
			Button insertPhraseButton, Button deleteButton) {
		setVisible(pasteCloneButton, true);
		setVisible(timingButton, false);
		setVisible(deleteButton, true);
		setVisible(qdselButton, false);
		setVisible(andButton, false);
		setVisible(orButton, false);
		setVisible(addPhraseButton, false);
		setVisible(insertPhraseButton, false);
	}
	
	/*
	private void initButtons(String criterion, boolean visible,
			Button criterionButton, Button pasteCloneButton, Button qdselButton, Button andButton,
			Button orButton, Button timingButton, Button addPhraseButton,
			Button insertPhraseButton, Button deleteButton) {
		showOrHideToolbars();
		TraversalTree tree = getTraversalTree(null);
		if(this.currentDiagramObject instanceof Rel)
			initRelButtons(tree, criterionButton, pasteCloneButton, qdselButton, andButton,
			orButton, timingButton, addPhraseButton,
			insertPhraseButton, deleteButton);
		else if(this.currentDiagramObject instanceof Conditional)
			initConditionalButtons(tree, criterionButton, pasteCloneButton, qdselButton, andButton,
				orButton, timingButton, addPhraseButton,
				insertPhraseButton, deleteButton);
		else if(this.currentDiagramObject instanceof Criterion)
			initCriterionButtons(tree, criterionButton, pasteCloneButton, qdselButton, andButton,
				orButton, timingButton, addPhraseButton,
				insertPhraseButton, deleteButton);
		else if(criterion.equals(AppController.MEASURE_PHRASE))
			initPhraseButtons(tree, criterionButton, pasteCloneButton, qdselButton, andButton,
				orButton, timingButton, addPhraseButton,
				insertPhraseButton, deleteButton);
		
		if (!(criterion.equals(AppController.MEASURE_PHRASE)) && !(criterion.equals(AppController.USER_DEFINED))) {
			String text = "Add " + criterion;
			setVisible(criterionButton, true);
			criterionButton.setText(text);
			criterionButton.setTitle(text);
		}
		else
			setVisible(criterionButton, false);
	}
	*/
	private void addExplicitLabelsToAttributes(){
		LabelBuilder.setInvisibleLabel(attributeList1, attributeList1Label, "Attribute List");
		LabelBuilder.setInvisibleLabel(attributeTypeList1, attributeTypeList1Label, "Attribute Type List");
		LabelBuilder.setInvisibleLabel(attributeComparisonList1, attributeComparisonList1Label, "Attribute Comparison List");
		LabelBuilder.setInvisibleLabel(attributeQuantity1, attributeQuantity1Label, "Attribute Quantity");
		LabelBuilder.setInvisibleLabel(attributeUnit1, attributeUnit1Label, "Attribute Unit");
		LabelBuilder.setInvisibleLabel(attributeQDSElementList1, attributeQDSElementList1Label, "Attribute QDS Element List");
		
		LabelBuilder.setInvisibleLabel(attributeList2, attributeList2Label, "Attribute List");
		LabelBuilder.setInvisibleLabel(attributeTypeList2, attributeTypeList2Label, "Attribute Type List");
		LabelBuilder.setInvisibleLabel(attributeComparisonList2, attributeComparisonList2Label, "Attribute Comparison List");
		LabelBuilder.setInvisibleLabel(attributeQuantity2, attributeQuantity2Label, "Attribute Quantity");
		LabelBuilder.setInvisibleLabel(attributeUnit2, attributeUnit2Label, "Attribute Unit");
		LabelBuilder.setInvisibleLabel(attributeQDSElementList2, attributeQDSElementList2Label, "Attribute QDS Element List");
	}
	
	/**
	 * This method is the hub for dynamic visibility for DiagramViewImpl.
	 * Consider delegating the setting of aria attributes or styles to a shared location 
	 * and merely invoke that logic here.
	 * 
	 * @param widget The widget to be rendered or no longer rendered
	 * @param visible Widget's rendering status
	 */
	private void setVisible(Widget widget, Boolean visible){
		MatContext.get().setVisible(widget, visible);
		//US 430. Disable the buttons for read only mode.
		if(widget instanceof Button){
			((Button)widget).setEnabled(editable);
		}	
	}
	
	
	public void moveFocusToPropertyEditor(){
		
		
		switch (diagramView) {
		case CANVAS_VIEW:
			firePropertyEditorAlert();
			propertyEditor1Focus.setFocus(true);
			break;
		case TEXT_VIEW:
			firePropertyEditorAlert();
			propertyEditor2Focus.setFocus(true);
			break;
		default:
		break;
		}
	}
	
	public void firePropertyEditorAlert(){
		switch (diagramView) {
		case CANVAS_VIEW:
			propertyEditor1Focus.clear();
			fireAlert(propertyEditor1Label);
			propertyEditor1Focus.add(propertyEditor2Label);
			//propertyEditor2Label.getElement().setAttribute("role", "application");
			break;
		case TEXT_VIEW:
			propertyEditor2Focus.clear();
			fireAlert(propertyEditor2Label);
			propertyEditor2Focus.add(propertyEditor2Label);
			//fireAlert(propertyEditor1Label);.getElement().setAttribute("role", "application");
			break;
		default:
		break;
		}
	}
  
	private void fireAlert(Widget w){
		fireAlert(w.getElement());
	}
	private void fireAlert(Element e){
		e.setAttribute("role", "alert");
	}
	
	public String getCurrentDiagramView(){
		String currentDiagramView ="";
		switch(diagramView){
		case CANVAS_VIEW:
			currentDiagramView= "CANVAS_VIEW";
			break;
		case TEXT_VIEW:
			currentDiagramView = "TEXT_VIEW";
			break;
		}
		return currentDiagramView;
	}
	
	private void deselectLibrary(){
		measurePhraseListBox.setSelectedIndex(-1);
		clauseLibraryListBox.setSelectedIndex(-1);
		clearMeasurePhraseArea();
		removePropertyEditor();
		hideAttributeEditor();
	}
	
	/**
	 * Intended for the UIBinder event handling mechanism 
	 * where a handler is not explicitly created
	 * 
	 * If explicitly instantiating an event handler 
	 * then create an extension of MATEventHandler
	 * to ensure the loading check is performed
	 * 
	 * Arg showAlertOnLoading added in case firing an alert is not desired
	 * @param fireAlertOnLoading
	 * @return
	 */
	private boolean isLoading(boolean fireAlertOnLoading){
		if(!MatContext.get().isLoading())
			return false;
		else if(fireAlertOnLoading)
			MatContext.get().fireLoadingAlert();
		return true;
	}
	
	/*
	 * (1) reset selection indicator for surrent tree
	 * TODO nice to have: do (1) above without iterating through the tree
	 * 
	 * (2) set the selection indicator for the current selected item
	 */
	class TreeSelectionHandler implements SelectionHandler<TreeItem>{
		
		@Override
		public void onSelection(SelectionEvent<TreeItem> event) {
			
			TreeItem item = event.getSelectedItem();
			resetTreeItemSelectionIndicators(getRoot(item));
			
			item.setText(addSelectionIndicator(item.getText()));
		}
		
		private TreeItem getRoot(TreeItem item){
			while(item.getParentItem() != null)
				item = item.getParentItem();
			return item;
		}
		
		private void resetTreeItemSelectionIndicators(TreeItem root){
			if(root.getText().startsWith(INDICATOR))
				root.setText(trimSelectionIndicator(root.getText()));
			for(int i = 0; i < root.getChildCount(); i++)
				resetTreeItemSelectionIndicators(root.getChild(i));
		}
		
	}
	
	/**
	 * remove the selection indicator if it exists and then trim
	 * @param str
	 * @return
	 */
	private String trimSelectionIndicator(String str){
		return str.replaceFirst(INDICATOR, "").trim();
	}
	
	private String addSelectionIndicator(String str){
		return INDICATOR+" "+str;
	}
	/**
	 * prevent tree item text accessors from seeing the tree selection indicator
	 * @param item
	 * @return
	 */
	private String getTreeItemText(TreeItem item){
		return(trimSelectionIndicator(item.getText()));
	}
	
	/*
	 * for a given TreeItem, set the first <img> alt and title attributes
	 */
	private void handleExpansionImageStatus(TreeItem item){
		NodeList<com.google.gwt.dom.client.Element> imgs =  item.getElement().getElementsByTagName("img");
		if(item.getState()){
			for(int i = 0; i < 1; i++){
				com.google.gwt.dom.client.Element elem = imgs.getItem(i);
				elem.setAttribute("alt", "Select to collapse.");
				elem.setAttribute("title", "Select to collapse.");
			}
		}else if(item.getChildCount() > 0){
			for(int i = 0; i < 1; i++){
				com.google.gwt.dom.client.Element elem = imgs.getItem(i);
				elem.setAttribute("alt", "Select to expand.");
				elem.setAttribute("title", "Select to expand.");
			}
		}
	}
	/*
	 * for each TreeItem, set the first <img> alt and title attributes
	 */
	private void handleExpansionImageStatus(){
		for (Iterator<TreeItem> treeItemIterator = diagramTree.treeItemIterator(); treeItemIterator.hasNext(); ) {
			TreeItem next = treeItemIterator.next();
			handleExpansionImageStatus(next);
		}
	}
	
	private void logSavePhraseEvent(DiagramObject diagramObject){
		MatContext mc = MatContext.get();
		MeasureSelectedEvent mse = mc.getCurrentMeasureInfo();
		String mid = mse.getMeasureId();
		String msg = "[id] "+diagramObject.getIdentity()+" [change id] "+diagramObject.getChangedName();
		MatContext.get().recordTransactionEvent(mid, null, "SAVE_PHRASE_EVENT", msg, ConstantMessages.DB_LOG);
	}
	//US 171 //US 171, Getting rid of Rel class static hard coded values.
	private List<String> getTimingConditionsList(){
		return new ArrayList(MatContext.get().getListBoxCodeProvider().getTimingConditionsMap().keySet());
	}
	
	private void doPhraseChangeHandlerBehavior(ListBox listBox){
		/* performing save op because there may have been changes in the property editor
		   prior to making a selection in the measure phrase library */
		doSave(false);
		
		MatContext.get().clearDVIMessages();
		String itemValue = listBox.getValue(listBox.getSelectedIndex());
		if (itemValue != null && itemValue.equalsIgnoreCase(ConstantMessages.USER_DEFINED_CONTEXT_DESC)) {
			return;
		}
		removePropertyEditor();
		showOrHideToolbars();
		
		retainSavedValue();
		DiagramObject diagramObject = presenter.getMeasurePhrase(getSelectedMeasurePhrase());
		//The above diagramObject has information about the simpleStatement which was selected or saved previously.

		//this is to prevent system clauses from
		//trying to be displayed
		//you can't, we did not load them
		if (diagramObject==null) return;
		listBox.setTitle(diagramObject.getIdentity());
		setToolbarTitle("Edit", false);
		select(diagramObject);
	}
}
