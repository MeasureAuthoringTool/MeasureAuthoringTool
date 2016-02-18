package mat.client.clause;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mat.client.shared.MatContext;
import mat.client.shared.SpacerWidget;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLParameter;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Badge;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.NavPills;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.LabelType;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;
import org.gwtbootstrap3.extras.toggleswitch.client.ui.ToggleSwitch;
import org.gwtbootstrap3.extras.toggleswitch.client.ui.base.constants.ColorType;
import org.gwtbootstrap3.extras.toggleswitch.client.ui.base.constants.SizeType;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;

// TODO: Auto-generated Javadoc
/**
 * The Class CQLWorkSpaceView.
 */
public class CQLWorkSpaceView  implements CQLWorkSpacePresenter.ViewDisplay{
	
	
	/** The main panel. */
	private HorizontalPanel mainPanel = new HorizontalPanel();
	/** The cell table panel. */
	private VerticalPanel cellTablePanel = new VerticalPanel();
	
	/** Cell Table Row Count. */
	private static final int TABLE_ROW_COUNT = 2;
	
	/** The table. */
	private CellTable<CQLFunctionArgument> table;
	
	/** The sort provider. */
	private ListDataProvider<CQLFunctionArgument> listDataProvider;
	
	private CQLFunctions selectedFunction;
	
	/** The main v panel. */
	private VerticalPanel mainVPanel = new VerticalPanel();
	
	/** The tab panel. */
	private HorizontalPanel tabPanel = new HorizontalPanel();
	
	/** The right hand nav panel. */
	private VerticalPanel rightHandNavPanel = new VerticalPanel();
	
	/** The main flow panel. */
	private FlowPanel mainFlowPanel = new FlowPanel();
	
	/** The view cql. */
	private AnchorListItem viewCQL = new AnchorListItem();
	
	/** The general information. */
	private AnchorListItem generalInformation;
	
	/** The parameter library. */
	private AnchorListItem parameterLibrary;
	
	/** The definition library. */
	private AnchorListItem definitionLibrary;
	
	/** The function library. */
	private AnchorListItem functionLibrary;
	//private InlineRadio patientRadio = new InlineRadio("Context");
	/** The success message alert gen info. */
	private Alert successMessageAlertGenInfo = new Alert();
	
	/** The error message alert gen info. */
	private Alert errorMessageAlertGenInfo = new Alert();
	
	/** The success message alert definition. */
	private Alert successMessageAlertDefinition = new Alert();
	
	/** The error message alert definition. */
	private Alert errorMessageAlertDefinition = new Alert();
	
	/** The success message alert parameter. */
	private Alert successMessageAlertParameter = new Alert();
	
	/** The error message alert parameter. */
	private Alert errorMessageAlertParameter = new Alert();
	
	/**
	 * InlineRadio populationRadio.
	 */
	//private InlineRadio populationRadio = new InlineRadio("Context");
	/**
	 * TextArea parameterNameTxtArea.
	 */
	private TextArea parameterNameTxtArea = new TextArea();
	
	/** The parameter ace editor. */
	private AceEditor parameterAceEditor = new AceEditor();
	
	/** The define ace editor. */
	private AceEditor defineAceEditor = new AceEditor();
	
	/** The cql ace editor. */
	private AceEditor cqlAceEditor = new AceEditor();
	
	/** The Function Body ace editor. */
	private AceEditor functionBodyAceEditor = new AceEditor();
	
	/**
	 * Button addParameterButton.
	 */
	private Button addParameterButton = new Button();
	
	/**
	 * Button deleteParameterButton.
	 */
	private Button deleteParameterButton = new Button();
	
	/**
	 * SuggestBox searchSuggestTextBox.
	 */
	private SuggestBox searchSuggestTextBox;
	
	/**
	 * HashMap parameterNameMap.
	 */
	private HashMap<String, String> parameterNameMap = new HashMap<String, String>();
	/**
	 * HashMap parameterMap.
	 */
	private HashMap<String, CQLParameter> parameterMap = new HashMap<String, CQLParameter>();
	/**
	 * Button removeParameterButton.
	 */
	private Button removeParameterButton;
	
	/**
	 * ListBox parameterNameListBox.
	 */
	private ListBox parameterNameListBox;
	
	/** The view parameter list. */
	private List<CQLParameter> viewParameterList = new ArrayList<CQLParameter>();
	
	/**
	 * ListBox defineNameListBox.
	 */
	private ListBox defineNameListBox;
	
	/**
	 * ListBox funcNameListBox.
	 */
	private ListBox funcNameListBox;
	/**
	 * TextArea defineNameTxtArea.
	 */
	private TextArea defineNameTxtArea = new TextArea();
	
	/**
	 * TextArea defineNameTxtArea.
	 */
	private TextArea funcNameTxtArea = new TextArea();
	
	/**
	 * SuggestBox searchSuggestDefineTextBox.
	 */
	private SuggestBox searchSuggestDefineTextBox;
	
	/**
	 * SuggestBox searchSuggestFuncTextBox.
	 */
	private SuggestBox searchSuggestFuncTextBox;
	/**
	 * List viewDefinitions.
	 */
	private List<CQLDefinition> viewDefinitions = new ArrayList<CQLDefinition>();
	
	/**
	 * List viewFunctions.
	 */
	private List<CQLFunctions> viewFunctions = new ArrayList<CQLFunctions>();
	/**
	 * HashMap defineNameMap.
	 */
	private HashMap<String, String> defineNameMap = new HashMap<String, String>();
	
	/**
	 * HashMap funcNameMap.
	 */
	private HashMap<String, String> funcNameMap = new HashMap<String, String>();
	/**
	 * HashMap definitionMap.
	 */
	private HashMap<String, CQLDefinition> definitionMap = new HashMap<String, CQLDefinition>();
	/**
	 * Button deleteDefineButton.
	 */
	private Button deleteDefineButton = new Button();
	/**
	 * Button addDefineButton.
	 */
	private Button addDefineButton = new Button();
	
	/** The param badge. */
	private Badge paramBadge = new Badge();
	
	/** The define badge. */
	private Badge defineBadge = new Badge();
	
	/** The Function badge. */
	private Badge functionBadge = new Badge();
	
	/** The param label. */
	private Label paramLabel = new Label("Parameter");
	
	/** The define label. */
	private Label defineLabel = new Label("Definition");
	
	/** The function Lib Label. */
	private Label functionLibLabel = new Label("Function");
	
	/** The param collapse. */
	PanelCollapse paramCollapse = new PanelCollapse();
	
	/** The define collapse. */
	PanelCollapse defineCollapse = new PanelCollapse();
	/** The function Collapse. */
	PanelCollapse functionCollapse = new PanelCollapse();
	
	/** The clicked menu. */
	public String clickedMenu = "general";
	
	/** The current selected definition obj id. */
	private String currentSelectedDefinitionObjId = null;
	
	/** The current selected paramerter obj id. */
	private String currentSelectedParamerterObjId = null;
	
	/** The save cql general info btn. */
	private Button saveCQLGeneralInfoBtn = new Button("Save");
	
	/** The context toggle switch. */
	private ToggleSwitch contextToggleSwitch = new ToggleSwitch();
	
	@Override
	public PanelCollapse getDefineCollapse() {
		return defineCollapse;
	}
	
	/**
	 * Sets the define collapse.
	 *
	 * @param defineCollapse the new define collapse
	 */
	public void setDefineCollapse(PanelCollapse defineCollapse) {
		this.defineCollapse = defineCollapse;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getParamCollapse()
	 */
	@Override
	public PanelCollapse getParamCollapse() {
		return paramCollapse;
	}
	
	/**
	 * Sets the param collapse.
	 *
	 * @param paramCollapse the new param collapse
	 */
	public void setParamCollapse(PanelCollapse paramCollapse) {
		this.paramCollapse = paramCollapse;
	}
	
	/**
	 * Instantiates a new CQL work space view.
	 */
	public CQLWorkSpaceView() {
		defineAceEditor.startEditor();
		parameterAceEditor.startEditor();
		cqlAceEditor.startEditor();
		functionBodyAceEditor.startEditor();
		resetAll();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLNewPresenter.ViewDisplay#buildView()
	 */
	@Override
	public void buildView() {
		resetAll();
		paramCollapse = createParameterCollapsablePanel();
		defineCollapse = createDefineCollapsablePanel();
		functionCollapse = createFunctionCollapsablePanel();
		buildLeftHandNavNar();
		buildGeneralInformation();
		mainFlowPanel.setWidth("700px");
		mainPanel.add(rightHandNavPanel);
		mainPanel.add(mainFlowPanel);
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#buildCQLFileView()
	 */
	@Override
	public void buildCQLFileView(){
		mainFlowPanel.clear();
		
		VerticalPanel parameterVP = new VerticalPanel();
		HorizontalPanel parameterFP = new HorizontalPanel();
		
		cqlAceEditor.setMode(AceEditorMode.CQL);
		cqlAceEditor.setTheme(AceEditorTheme.ECLIPSE);
		
		cqlAceEditor.getElement().getStyle().setFontSize(14, Unit.PX);
		cqlAceEditor.setSize("675px", "500px");
		cqlAceEditor.setAutocompleteEnabled(true);
		cqlAceEditor.setReadOnly(true);
		Label viewCQlFileLabel = new Label(LabelType.INFO);
		viewCQlFileLabel.setText("View CQL file here");
		viewCQlFileLabel.setTitle("View CQL file here");
		parameterVP.add(new SpacerWidget());
		parameterVP.add(new SpacerWidget());
		parameterVP.add(viewCQlFileLabel);
		parameterVP.add(new SpacerWidget());
		parameterVP.add(new SpacerWidget());
		parameterVP.add(cqlAceEditor);
		mainFlowPanel.add(parameterVP);
		
		parameterFP.add(parameterVP);
		parameterFP.setStyleName("cqlRightContainer");
		
		VerticalPanel vp = new VerticalPanel();
		vp.setStyleName("cqlRightContainer");
		vp.setWidth("700px");
		vp.setHeight("500px");
		parameterFP.setWidth("700px");
		parameterFP.setStyleName("marginLeft15px");
		vp.add(parameterFP);
		vp.setHeight("675px");
		
		mainFlowPanel.add(vp);
		
		
	}
	
	
	/**
	 * Adds the parameter event handler.
	 */
	private void addParameterEventHandler(){
		
		getParameterNameListBox().addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				int selectedIndex  = getParameterNameListBox().getSelectedIndex();
				if (selectedIndex != -1) {
					final String selectedParamID = getParameterNameListBox().getValue(selectedIndex);
					currentSelectedParamerterObjId = selectedParamID;
					if(getParameterMap().get(selectedParamID) != null){
						getParameterNameTxtArea().setText(getParameterMap().get(selectedParamID).getParameterName());
						getParameterAceEditor().setText(getParameterMap().get(selectedParamID).getParameterLogic());
					}
				}
				successMessageAlertParameter.clear();
				successMessageAlertParameter.setVisible(false);
				errorMessageAlertParameter.clear();
				errorMessageAlertParameter.setVisible(false);
			}
		});
		
		
	}
	
	/**
	 * Adds the define event handkers.
	 */
	private void addDefineEventHandlers(){
		getDefineNameListBox().addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				int selectedIndex  = getDefineNameListBox().getSelectedIndex();
				if(selectedIndex != -1){
					final String selectedDefinitionID = getDefineNameListBox().getValue(selectedIndex);
					currentSelectedDefinitionObjId = selectedDefinitionID;
					if(getDefinitionMap().get(selectedDefinitionID) != null){
						getDefineNameTxtArea().setText(getDefinitionMap().get(selectedDefinitionID).getDefinitionName());
						getDefineAceEditor().setText(getDefinitionMap().get(selectedDefinitionID).getDefinitionLogic());
					}
				}
				
				successMessageAlertDefinition.clear();
				successMessageAlertDefinition.setVisible(false);
				errorMessageAlertDefinition.clear();
				errorMessageAlertDefinition.setVisible(false);
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#updateParamMap()
	 */
	@Override
	public void updateParamMap() {
		getParameterMap().clear();
		getParameterNameMap().clear();
		for(CQLParameter parameter : getViewParameterList()){
			getParameterNameMap().put(parameter.getId(), parameter.getParameterName());
			getParameterMap().put(parameter.getId(), parameter);
		}
		updateSuggestOracle();
		getParamBadge().setText(""+getViewParameterList().size());
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#updateDefineMap()
	 */
	@Override
	public void updateDefineMap() {
		getDefinitionMap().clear();
		getDefineNameMap().clear();
		for(CQLDefinition define : getViewDefinitions()){
			getDefineNameMap().put(define.getId(), define.getDefinitionName());
			getDefinitionMap().put(define.getId(), define);
		}
		
		updateSuggestDefineOracle();
		getDefineBadge().setText(""+getViewDefinitions().size());
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#updateSuggestOracle()
	 */
	@Override
	public void updateSuggestOracle() {
		if (searchSuggestTextBox != null) {
			MultiWordSuggestOracle multiWordSuggestOracle = (MultiWordSuggestOracle) searchSuggestTextBox.getSuggestOracle();
			multiWordSuggestOracle.clear();
			multiWordSuggestOracle.addAll(parameterNameMap.values());
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#buildGeneralInformation()
	 */
	@Override
	public void buildGeneralInformation() {
		setCurrentSelectedDefinitionObjId(null);
		setCurrentSelectedParamerterObjId(null);
		VerticalPanel generalInfoTopPanel = new VerticalPanel();
		
		Label libraryNameLabel = new Label(LabelType.INFO, "CQL Library Name");
		TextArea libraryNameValue = new TextArea();
		libraryNameLabel.getElement().setAttribute("style", "font-size:90%;margin-left:15px;background-color:#0964A2;");
		libraryNameLabel.setWidth("150px");
		libraryNameValue.getElement().setAttribute("style", "margin-left:15px;width:250px;height:25px;");
		libraryNameValue.setText(MatContext.get().getCurrentMeasureName().replaceAll(" ", ""));
		libraryNameValue.setReadOnly(true);
		
		Label usingModeLabel = new Label(LabelType.INFO, "Using Model");
		TextArea usingModelValue = new TextArea();
		usingModeLabel.getElement().setAttribute("style", "font-size:90%;margin-left:15px;background-color:#0964A2;");
		usingModeLabel.setWidth("150px");
		usingModelValue.getElement().setAttribute("style", "margin-left:15px;width:250px;height:25px;");
		usingModelValue.setText("QDM");
		usingModelValue.setReadOnly(true);
		
		Label defaultContextLabel = new Label(LabelType.INFO, "Context");
		defaultContextLabel.getElement().setAttribute("style", "font-size:90%;margin-left:15px;background-color:#0964A2;");
		FlowPanel usingFlowPanel = new FlowPanel();
		
		contextToggleSwitch.setSize(SizeType.MINI);
		contextToggleSwitch.setLabelText("Context");
		contextToggleSwitch.setTitle("Click to change context");
		contextToggleSwitch.setLabelWidth("100");
		contextToggleSwitch.setOnText("Patient");
		contextToggleSwitch.setOnColor(ColorType.SUCCESS);
		contextToggleSwitch.setOffText("Population");
		contextToggleSwitch.setOffColor(ColorType.PRIMARY);
		
		if(getContextToggleSwitch().getValue()){
			contextToggleSwitch.setValue(true);
		} else {
			contextToggleSwitch.setValue(false);
		}
		
		usingFlowPanel.add(contextToggleSwitch);
		usingFlowPanel.getElement().setAttribute("style", "margin-left:15px");
		
		successMessageAlertGenInfo.setType(AlertType.SUCCESS);
		successMessageAlertGenInfo.setWidth("600px");
		successMessageAlertGenInfo.setVisible(false);
		generalInfoTopPanel.add(new SpacerWidget());
		generalInfoTopPanel.add(successMessageAlertGenInfo);
		generalInfoTopPanel.add(new SpacerWidget());
		
		generalInfoTopPanel.add(libraryNameLabel);
		generalInfoTopPanel.add(new SpacerWidget());
		generalInfoTopPanel.add(libraryNameValue);
		generalInfoTopPanel.add(new SpacerWidget());
		generalInfoTopPanel.add(usingModeLabel);
		generalInfoTopPanel.add(new SpacerWidget());
		generalInfoTopPanel.add(usingModelValue);
		generalInfoTopPanel.add(new SpacerWidget());
		generalInfoTopPanel.add(defaultContextLabel);
		generalInfoTopPanel.add(new SpacerWidget());
		generalInfoTopPanel.add(usingFlowPanel);
		
		
		generalInfoTopPanel.add(new SpacerWidget());
		generalInfoTopPanel.add(new SpacerWidget());
		
		saveCQLGeneralInfoBtn.setType(ButtonType.SUCCESS);
		saveCQLGeneralInfoBtn.getElement().setAttribute("id", "SaveGeneralInformation_Button");
		saveCQLGeneralInfoBtn.setTitle("Save");
		
		HorizontalPanel buttonLayoutPanel = new HorizontalPanel();
		buttonLayoutPanel.setStylePrimaryName("myAccountButtonLayout");
		buttonLayoutPanel.add(saveCQLGeneralInfoBtn);
		
		generalInfoTopPanel.add(buttonLayoutPanel);
		generalInfoTopPanel.add(new SpacerWidget());
		generalInfoTopPanel.add(new SpacerWidget());
		VerticalPanel vp = new VerticalPanel();
		vp.setStyleName("cqlRightContainer");
		vp.setWidth("715px");
		generalInfoTopPanel.setWidth("700px");
		generalInfoTopPanel.setStyleName("marginLeft15px");
		vp.add(generalInfoTopPanel);
		
		mainFlowPanel.clear();
		mainFlowPanel.add(vp);
		
	}
	
	/**
	 * Method to create Right Hand side Nav bar in CQL Workspace.
	 */
	private void buildLeftHandNavNar() {
		setCurrentSelectedDefinitionObjId(null);
		setCurrentSelectedParamerterObjId(null);
		rightHandNavPanel.clear();
		NavPills navPills = new NavPills();
		navPills.setStacked(true);
		
		generalInformation = new AnchorListItem();
		parameterLibrary = new AnchorListItem();
		definitionLibrary = new AnchorListItem();
		functionLibrary = new AnchorListItem();
		viewCQL = new AnchorListItem();
		
		generalInformation.setIcon(IconType.INFO);
		generalInformation.setText("General Information");
		generalInformation.setTitle("General Information");
		generalInformation.setActive(true);
		
		parameterLibrary.setIcon(IconType.PENCIL);
		parameterLibrary.setTitle("Parameter");
		paramBadge.setText(""+viewParameterList.size());
		Anchor paramAnchor = (Anchor) (parameterLibrary.getWidget(0));
		paramLabel.setStyleName("transparentLabel");
		paramAnchor.add(paramLabel);
		paramBadge.setMarginLeft(65);
		paramAnchor.add(paramBadge);
		paramAnchor.setDataParent("#navGroup");
		paramAnchor.setDataToggle(Toggle.COLLAPSE);
		parameterLibrary.setHref("#collapseParameter");
		
		parameterLibrary.add(paramCollapse);
		
		definitionLibrary.setIcon(IconType.PENCIL);
		definitionLibrary.setTitle("Define");
		defineBadge.setText(""+viewDefinitions.size());
		Anchor defineAnchor = (Anchor) (definitionLibrary.getWidget(0));
		defineLabel.setStyleName("transparentLabel");
		defineAnchor.add(defineLabel);
		defineBadge.setMarginLeft(67);
		defineAnchor.add(defineBadge);
		defineAnchor.setDataParent("#navGroup");
		definitionLibrary.setDataToggle(Toggle.COLLAPSE);
		definitionLibrary.setHref("#collapseDefine");
		
		definitionLibrary.add(defineCollapse);
		
		functionLibrary.setIcon(IconType.PENCIL);
		/*functionLibrary.setText("Functions");*/
		functionLibrary.setTitle("Functions");
		
		functionBadge.setText(""+viewFunctions.size());
		Anchor funcAnchor = (Anchor) (functionLibrary.getWidget(0));
		functionLibLabel.setStyleName("transparentLabel");
		funcAnchor.add(functionLibLabel);
		functionBadge.setMarginLeft(67);
		funcAnchor.add(functionBadge);
		funcAnchor.setDataParent("#navGroup");
		functionLibrary.setDataToggle(Toggle.COLLAPSE);
		functionLibrary.setHref("#collapseFunction");
		
		functionLibrary.add(functionCollapse);
		
		
		viewCQL.setIcon(IconType.BOOK);
		viewCQL.setText("View CQL");
		viewCQL.setTitle("View CQL");
		
		navPills.add(generalInformation);
		navPills.add(parameterLibrary);
		
		navPills.add(definitionLibrary);
		navPills.add(functionLibrary);
		navPills.add(viewCQL);
		
		navPills.setWidth("200px");
		
		rightHandNavPanel.add(navPills);
	}
	
	/**
	 * Creates the parameter collapsable panel.
	 *
	 * @return the panel collapse
	 */
	private PanelCollapse createParameterCollapsablePanel() {
		paramCollapse.setId("collapseParameter");
		
		PanelBody parameterCollapseBody = new PanelBody();
		
		HorizontalPanel parameterFP = new HorizontalPanel();
		
		VerticalPanel rightVerticalPanel = new VerticalPanel();
		rightVerticalPanel.setSpacing(10);
		
		rightVerticalPanel.getElement().setId("rhsVerticalPanel_VerticalPanelParam");
		rightVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		Label paramLibraryLabel = new Label("Parameter Library");
		searchSuggestTextBox = new SuggestBox();
		updateSuggestOracle();
		searchSuggestTextBox.setWidth("180px");
		searchSuggestTextBox.setText("Search");
		searchSuggestTextBox.getElement().setId("searchTextBox_TextBoxParameterLib");
		
		searchSuggestTextBox.getValueBox().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if ("Search".equals(searchSuggestTextBox.getText())) {
					searchSuggestTextBox.setText("");
				}
			}
		});
		
		parameterNameListBox = new ListBox();
		parameterNameListBox.setWidth("180px");
		parameterNameListBox.setVisibleItemCount(10);
		parameterNameListBox.getElement().setAttribute("id", "paramListBox");
		
		clearAndAddParameterNamesToListBox();
		
		addSuggestHandler(searchSuggestTextBox, parameterNameListBox);
		addListBoxHandler(parameterNameListBox, searchSuggestTextBox);
		
		searchSuggestTextBox.getElement().setAttribute("id", "searchSuggestTextBox");
		rightVerticalPanel.add(searchSuggestTextBox);
		searchSuggestTextBox.getElement().setId("searchSuggestTextBox_SuggestBox");
		rightVerticalPanel.add(parameterNameListBox);
		parameterNameListBox.getElement().setId("paramNameListBox_ListBox");
		rightVerticalPanel.setCellHorizontalAlignment(paramLibraryLabel, HasHorizontalAlignment.ALIGN_LEFT);
		parameterFP.add(rightVerticalPanel);
		parameterCollapseBody.add(parameterFP);
		
		paramCollapse.add(parameterCollapseBody);
		return paramCollapse;
		
	}
	
	/**
	 * Creates the define collapsable panel.
	 *
	 * @return the panel collapse
	 */
	private PanelCollapse createDefineCollapsablePanel() {
		PanelCollapse parameterCollapsePanel = new PanelCollapse();
		parameterCollapsePanel.setId("collapseDefine");
		
		PanelBody parameterCollapseBody = new PanelBody();
		
		HorizontalPanel parameterFP = new HorizontalPanel();
		
		VerticalPanel rightVerticalPanel = new VerticalPanel();
		rightVerticalPanel.setSpacing(10);
		
		rightVerticalPanel.getElement().setId("rhsVerticalPanel_VerticalPanelParam");
		rightVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		Label paramLibraryLabel = new Label("Definition Library");
		searchSuggestDefineTextBox = new SuggestBox();
		updateSuggestDefineOracle();
		searchSuggestDefineTextBox.setWidth("180px");
		searchSuggestDefineTextBox.setText("Search");
		searchSuggestDefineTextBox.getElement().setId("searchTextBox_TextBoxParameterLib");
		
		searchSuggestDefineTextBox.getValueBox().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if ("Search".equals(searchSuggestDefineTextBox.getText())) {
					searchSuggestDefineTextBox.setText("");
				}
			}
		});
		
		defineNameListBox = new ListBox();
		defineNameListBox.setWidth("180px");
		defineNameListBox.setVisibleItemCount(10);
		defineNameListBox.getElement().setAttribute("id", "defineListBox");
		
		clearAndAddDefinitionNamesToListBox();
		
		addSuggestHandler(searchSuggestDefineTextBox, defineNameListBox);
		addListBoxHandler(defineNameListBox, searchSuggestDefineTextBox);
		
		searchSuggestDefineTextBox.getElement().setAttribute("id", "searchSuggestTextBox");
		rightVerticalPanel.add(searchSuggestDefineTextBox);
		searchSuggestDefineTextBox.getElement().setId("searchSuggestTextBox_SuggestBox");
		rightVerticalPanel.add(defineNameListBox);
		searchSuggestDefineTextBox.getElement().setId("DefineListBox_ListBox");
		rightVerticalPanel.setCellHorizontalAlignment(paramLibraryLabel, HasHorizontalAlignment.ALIGN_LEFT);
		parameterFP.add(rightVerticalPanel);
		parameterCollapseBody.add(parameterFP);
		parameterCollapsePanel.add(parameterCollapseBody);
		return parameterCollapsePanel;
		
	}
	
	
	/**
	 * Creates the Function collapsable panel.
	 *
	 * @return the panel collapse
	 */
	private PanelCollapse createFunctionCollapsablePanel() {
		PanelCollapse funcCollapsePanel = new PanelCollapse();
		funcCollapsePanel.setId("collapseFunction");
		
		PanelBody funcCollapseBody = new PanelBody();
		
		HorizontalPanel funcFP = new HorizontalPanel();
		
		VerticalPanel rightVerticalPanel = new VerticalPanel();
		rightVerticalPanel.setSpacing(10);
		
		rightVerticalPanel.getElement().setId("rhsVerticalPanel_VerticalPanelFunc");
		rightVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		Label functionLibraryLabel = new Label("Function Library");
		searchSuggestFuncTextBox = new SuggestBox();
		updateSuggestFuncOracle();
		searchSuggestFuncTextBox.setWidth("180px");
		searchSuggestFuncTextBox.setText("Search");
		searchSuggestFuncTextBox.getElement().setId("searchTextBox_TextBoxFuncLib");
		
		searchSuggestFuncTextBox.getValueBox().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if ("Search".equals(searchSuggestFuncTextBox.getText())) {
					searchSuggestFuncTextBox.setText("");
				}
			}
		});
		
		funcNameListBox = new ListBox();
		funcNameListBox.setWidth("180px");
		funcNameListBox.setVisibleItemCount(10);
		funcNameListBox.getElement().setAttribute("id", "funcListBox");
		
		clearAndAddFunctionsNamesToListBox();
		
		addSuggestHandler(searchSuggestFuncTextBox, funcNameListBox);
		addListBoxHandler(funcNameListBox, searchSuggestFuncTextBox);
		
		searchSuggestFuncTextBox.getElement().setAttribute("id", "searchSuggestTextBoxFunc");
		rightVerticalPanel.add(searchSuggestFuncTextBox);
		rightVerticalPanel.add(funcNameListBox);
		
		rightVerticalPanel.setCellHorizontalAlignment(functionLibraryLabel, HasHorizontalAlignment.ALIGN_LEFT);
		funcFP.add(rightVerticalPanel);
		funcCollapseBody.add(funcFP);
		funcCollapsePanel.add(funcCollapseBody);
		return funcCollapsePanel;
		
	}
	
	
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#buildParameterLibraryView()
	 */
	@Override
	public void buildParameterLibraryView(){
		setCurrentSelectedDefinitionObjId(null);
		setCurrentSelectedParamerterObjId(null);
		mainFlowPanel.clear();
		VerticalPanel parameterVP = new VerticalPanel();
		HorizontalPanel parameterFP = new HorizontalPanel();
		
		Label parameterLabel = new Label(LabelType.INFO,"Parameter");
		parameterLabel.setMarginTop(5);
		parameterLabel.setId("Parameter_Label");
		parameterNameTxtArea.setText("");
		parameterNameTxtArea.setPlaceholder("Enter Parameter Name here.");
		parameterNameTxtArea.setSize("260px", "25px");
		parameterNameTxtArea.setId("parameterNameField");
		parameterNameTxtArea.setName("parameterName");
		parameterLabel.setText("Parameter");
		
		
		//parameterAceEditor.startEditor();
		parameterAceEditor.setText("");
		parameterAceEditor.setMode(AceEditorMode.CQL);
		parameterAceEditor.setTheme(AceEditorTheme.ECLIPSE);
		parameterAceEditor.getElement().getStyle().setFontSize(14, Unit.PX);
		parameterAceEditor.setSize("675px", "500px");
		parameterAceEditor.setAutocompleteEnabled(true);
		parameterAceEditor.getElement().setAttribute("id", "Parameter_AceEditorID");
		
		//addParameterButton = new Button();
		addParameterButton.setType(ButtonType.PRIMARY);
		addParameterButton.setSize(ButtonSize.DEFAULT);
		addParameterButton.setId("AddParameter_Button");
		addParameterButton.setMarginTop(10);
		//addParameterButton.setMarginLeft(15);
		addParameterButton.setTitle("Save");
		addParameterButton.setText("Save");
		
		deleteParameterButton.setType(ButtonType.PRIMARY);
		deleteParameterButton.setSize(ButtonSize.EXTRA_SMALL);
		deleteParameterButton.setIcon(IconType.REMOVE);
		deleteParameterButton.setTitle("Delete");
		deleteParameterButton.setText("Delete");
		deleteParameterButton.setId("DeleteParameter_Button");
		
		parameterNameTxtArea.getElement().setAttribute("style", "width:250px;height:25px;margin-top:5px;");
		
		successMessageAlertParameter.setType(AlertType.SUCCESS);
		successMessageAlertParameter.setWidth("600px");
		successMessageAlertParameter.setVisible(false);
		
		errorMessageAlertParameter.setType(AlertType.DANGER);
		errorMessageAlertParameter.setWidth("600px");
		errorMessageAlertParameter.setVisible(false);
		
		parameterVP.add(successMessageAlertParameter);
		parameterVP.add(errorMessageAlertParameter);
		parameterVP.add(new SpacerWidget());
		parameterVP.add(parameterLabel);
		parameterVP.add(new SpacerWidget());
		parameterVP.add(parameterNameTxtArea);
		parameterVP.add(new SpacerWidget());
		parameterVP.add(parameterAceEditor);
		parameterVP.add(new SpacerWidget());
		parameterVP.add(addParameterButton);
		parameterVP.setStyleName("topping");
		parameterFP.add(parameterVP);
		parameterFP.setStyleName("cqlRightContainer");
		
		VerticalPanel vp = new VerticalPanel();
		vp.setStyleName("cqlRightContainer");
		vp.setWidth("700px");
		vp.setHeight("500px");
		parameterFP.setWidth("700px");
		parameterFP.setStyleName("marginLeft15px");
		
		vp.add(new SpacerWidget());
		vp.add(parameterFP);
		vp.setHeight("675px");
		addParameterEventHandler();
		mainFlowPanel.add(vp);
		
	}
	/**
	 * Add Handler to ListBox.
	 * @param listBox -ListBox
	 * @param suggestBox - {@link SuggestBox}
	 */
	private void addListBoxHandler(final ListBox listBox,
			final SuggestBox suggestBox) {
		listBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				System.out.println("listbox change event:" + event.getAssociatedType().getName());
				int selectedIndex = listBox.getSelectedIndex();
				String selectedItem = listBox.getItemText(selectedIndex);
				suggestBox.setText(selectedItem);
			}
		});
		
	}
	
	/**
	 * Add Suggest Handler.
	 * @param suggestBox - {@link SuggestBox}
	 * @param listBox - {@link ListBox}
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
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#clearAndAddParameterNamesToListBox()
	 */
	@Override
	public void clearAndAddParameterNamesToListBox() {
		if (parameterNameListBox != null) {
			parameterNameListBox.clear();
			for (CQLParameter param : viewParameterList) {
				parameterNameListBox.addItem(param.getParameterName(), param.getId());
			}
			// Set tooltips for each element in listbox
			SelectElement selectElement = SelectElement.as(parameterNameListBox.getElement());
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
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#buildDefinitionLibraryView()
	 */
	@Override
	public void buildDefinitionLibraryView(){
		setCurrentSelectedDefinitionObjId(null);
		setCurrentSelectedParamerterObjId(null);
		mainFlowPanel.clear();
		VerticalPanel definitionVP = new VerticalPanel();
		HorizontalPanel definitionFP = new HorizontalPanel();
		
		Label defineLabel = new Label(LabelType.INFO,"Definition Name");
		defineLabel.setMarginTop(5);
		defineLabel.setId("Definition_Label");
		defineNameTxtArea.setText("");
		defineNameTxtArea.setPlaceholder("Enter Definition Name here.");
		defineNameTxtArea.setSize("260px", "25px");
		defineNameTxtArea.setId("defineNameField");
		defineNameTxtArea.setName("defineName");
		defineLabel.setText("Definition Name");
		
		
		defineAceEditor.setText("");
		defineAceEditor.setMode(AceEditorMode.CQL);
		defineAceEditor.setTheme(AceEditorTheme.ECLIPSE);
		defineAceEditor.getElement().getStyle().setFontSize(14, Unit.PX);
		defineAceEditor.setSize("675px", "500px");
		defineAceEditor.setAutocompleteEnabled(true);
		defineAceEditor.getElement().setAttribute("id", "Define_AceEditorID");
		
		addDefineButton.setType(ButtonType.PRIMARY);
		addDefineButton.setSize(ButtonSize.DEFAULT);
		addDefineButton.setId("AddDefine_Button");
		addDefineButton.setMarginTop(10);
		//addDefineButton.setMarginLeft(15);
		addDefineButton.setTitle("Save");
		addDefineButton.setText("Save");
		
		deleteDefineButton.setType(ButtonType.PRIMARY);
		deleteDefineButton.setSize(ButtonSize.EXTRA_SMALL);
		deleteDefineButton.setIcon(IconType.REMOVE);
		deleteDefineButton.setTitle("Delete");
		deleteDefineButton.setText("Delete");
		deleteDefineButton.setId("DeleteDefine_Button");
		defineNameTxtArea.getElement().setAttribute("style", "width:250px;height:25px;margin-top:5px");
		
		successMessageAlertDefinition.setType(AlertType.SUCCESS);
		successMessageAlertDefinition.setWidth("600px");
		successMessageAlertDefinition.setVisible(false);
		
		errorMessageAlertDefinition.setType(AlertType.DANGER);
		errorMessageAlertDefinition.setWidth("600px");
		errorMessageAlertDefinition.setVisible(false);
		
		definitionVP.add(successMessageAlertDefinition);
		definitionVP.add(errorMessageAlertDefinition);
		definitionVP.add(new SpacerWidget());
		definitionVP.add(defineLabel);
		definitionVP.add(new SpacerWidget());
		definitionVP.add(defineNameTxtArea);
		definitionVP.add(new SpacerWidget());
		definitionVP.add(defineAceEditor);
		definitionVP.add(new SpacerWidget());
		definitionVP.add(addDefineButton);
		definitionVP.setStyleName("topping");
		definitionFP.add(definitionVP);
		definitionFP.setStyleName("cqlRightContainer");
		
		VerticalPanel vp = new VerticalPanel();
		vp.setStyleName("cqlRightContainer");
		vp.setWidth("700px");
		vp.setHeight("500px");
		definitionFP.setWidth("700px");
		definitionFP.setStyleName("marginLeft15px");
		
		vp.add(new SpacerWidget());
		vp.add(definitionFP);
		vp.setHeight("675px");
		addDefineEventHandlers();
		mainFlowPanel.add(vp);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#updateSuggestDefineOracle()
	 */
	@Override
	public void updateSuggestDefineOracle() {
		if (searchSuggestDefineTextBox != null) {
			MultiWordSuggestOracle multiWordSuggestOracle = (MultiWordSuggestOracle) searchSuggestDefineTextBox.getSuggestOracle();
			multiWordSuggestOracle.clear();
			multiWordSuggestOracle.addAll(defineNameMap.values());
		}
	}
	
	@Override
	public void updateSuggestFuncOracle() {
		if (searchSuggestFuncTextBox != null) {
			MultiWordSuggestOracle multiWordSuggestOracle = (MultiWordSuggestOracle) searchSuggestFuncTextBox.getSuggestOracle();
			multiWordSuggestOracle.clear();
			multiWordSuggestOracle.addAll(funcNameMap.values());
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#clearAndAddDefinitionNamesToListBox()
	 */
	@Override
	public void clearAndAddDefinitionNamesToListBox() {
		if (defineNameListBox != null) {
			defineNameListBox.clear();
			for (CQLDefinition define : viewDefinitions) {
				defineNameListBox.addItem(define.getDefinitionName(), define.getId());
			}
			// Set tooltips for each element in listbox
			SelectElement selectElement = SelectElement.as(defineNameListBox.getElement());
			com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement
					.getOptions();
			for (int i = 0; i < options.getLength(); i++) {
				String title = options.getItem(i).getText();
				OptionElement optionElement = options.getItem(i);
				optionElement.setTitle(title);
			}
		}
	}
	
	@Override
	public void clearAndAddFunctionsNamesToListBox() {
		if (funcNameListBox != null) {
			funcNameListBox.clear();
			for (CQLFunctions func : viewFunctions) {
				funcNameListBox.addItem(func.getFunctionName(), func.getId());
			}
			// Set tooltips for each element in listbox
			SelectElement selectElement = SelectElement.as(funcNameListBox.getElement());
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
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#buildFunctionLibraryView()
	 */
	@Override
	public void buildFunctionLibraryView(){
		setCurrentSelectedDefinitionObjId(null);
		setCurrentSelectedParamerterObjId(null);
		mainFlowPanel.clear();
		
		VerticalPanel funcVP = new VerticalPanel();
		HorizontalPanel funcFP = new HorizontalPanel();
		
		Label functionNameLabel = new Label(LabelType.INFO,"Function Name");
		functionNameLabel.setMarginTop(5);
		functionNameLabel.setId("Function_Label");
		funcNameTxtArea.setText("");
		funcNameTxtArea.setPlaceholder("Enter Function Name here.");
		funcNameTxtArea.setSize("260px", "25px");
		funcNameTxtArea.setId("FunctionNameField");
		funcNameTxtArea.setName("FunctionName");
		functionNameLabel.setText("Function Name");
		
		functionBodyAceEditor.setText("");
		functionBodyAceEditor.setMode(AceEditorMode.CQL);
		functionBodyAceEditor.setTheme(AceEditorTheme.ECLIPSE);
		functionBodyAceEditor.getElement().getStyle().setFontSize(14, Unit.PX);
		functionBodyAceEditor.setSize("675px", "500px");
		functionBodyAceEditor.setAutocompleteEnabled(true);
		functionBodyAceEditor.getElement().setAttribute("id", "Func_AceEditorID");
		
		funcVP.add(functionNameLabel);
		funcVP.add(new SpacerWidget());
		funcVP.add(funcNameTxtArea);
		funcVP.add(new SpacerWidget());
		/*createAddArgumentViewForFunctions();
		funcVP.add(cellTablePanel);*/
		funcVP.add(new SpacerWidget());
		funcVP.add(functionBodyAceEditor);
		funcVP.add(new SpacerWidget());
		funcVP.setStyleName("topping");
		funcFP.add(funcVP);
		funcFP.setStyleName("cqlRightContainer");
		
		VerticalPanel vp = new VerticalPanel();
		vp.setStyleName("cqlRightContainer");
		vp.setWidth("700px");
		vp.setHeight("500px");
		funcFP.setWidth("700px");
		funcFP.setStyleName("marginLeft15px");
		
		vp.add(funcFP);
		vp.setHeight("675px");
		
		mainFlowPanel.add(vp);
	}
	
	
	private void createAddArgumentViewForFunctions(){
		cellTablePanel.clear();
		cellTablePanel.setStyleName("cellTablePanel");
		if (selectedFunction != null) {
			
		} else {
			Label tableHeader = new Label("Added Arguments List");
			tableHeader.getElement().setId("tableHeader_Label");
			tableHeader.setStyleName("recentSearchHeader");
			tableHeader.getElement().setAttribute("tabIndex", "0");
			HTML desc = new HTML("<p> No Arguments Added.</p>");
			cellTablePanel.add(tableHeader);
			cellTablePanel.add(new SpacerWidget());
			cellTablePanel.add(desc);
		}
		
	}
	
	
	/**
	 * Reset All components to default state.
	 */
	private void resetAll() {
		rightHandNavPanel.clear();
		mainFlowPanel.clear();
		parameterNameTxtArea.setText("");
		defineNameTxtArea.setText("");
		funcNameTxtArea.setText("");
		
		defineAceEditor.setText("");
		parameterAceEditor.setText("");
		cqlAceEditor.setText("");
		functionBodyAceEditor.setText("");
		
		viewParameterList.clear();
		viewDefinitions.clear();
		viewFunctions.clear();
		
		if(paramCollapse != null){
			paramCollapse.clear();
		}
		if(defineCollapse != null){
			defineCollapse.clear();
		}
		if(functionCollapse != null){
			functionCollapse.clear();
		}
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getMainPanel()
	 */
	@Override
	public HorizontalPanel getMainPanel() {
		return mainPanel;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getMainVPanel()
	 */
	@Override
	public VerticalPanel getMainVPanel() {
		return mainVPanel;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getGeneralInformation()
	 */
	@Override
	public AnchorListItem getGeneralInformation() {
		return generalInformation;
	}
	
	/**
	 * Sets the general information.
	 *
	 * @param generalInformation the new general information
	 */
	public void setGeneralInformation(AnchorListItem generalInformation) {
		this.generalInformation = generalInformation;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getParameterLibrary()
	 */
	@Override
	public AnchorListItem getParameterLibrary() {
		return parameterLibrary;
	}
	
	/**
	 * Sets the parameter library.
	 *
	 * @param parameterLibrary the new parameter library
	 */
	public void setParameterLibrary(AnchorListItem parameterLibrary) {
		this.parameterLibrary = parameterLibrary;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getDefinitionLibrary()
	 */
	@Override
	public AnchorListItem getDefinitionLibrary() {
		return definitionLibrary;
	}
	
	/**
	 * Sets the definition library.
	 *
	 * @param definitionLibrary the new definition library
	 */
	public void setDefinitionLibrary(AnchorListItem definitionLibrary) {
		this.definitionLibrary = definitionLibrary;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getFunctionLibrary()
	 */
	@Override
	public AnchorListItem getFunctionLibrary() {
		return functionLibrary;
	}
	
	/**
	 * Sets the function library.
	 *
	 * @param functionLibrary the new function library
	 */
	public void setFunctionLibrary(AnchorListItem functionLibrary) {
		this.functionLibrary = functionLibrary;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getViewCQL()
	 */
	@Override
	public AnchorListItem getViewCQL() {
		return viewCQL;
	}
	
	/**
	 * Sets the view cql.
	 *
	 * @param viewCQL the new view cql
	 */
	public void setViewCQL(AnchorListItem viewCQL) {
		this.viewCQL = viewCQL;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getAddParameterButton()
	 */
	@Override
	public Button getAddParameterButton() {
		return addParameterButton;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getRemoveParameterButton()
	 */
	@Override
	public Button getRemoveParameterButton() {
		return removeParameterButton;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getParameterNameTxtArea()
	 */
	@Override
	public TextArea getParameterNameTxtArea() {
		return parameterNameTxtArea;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getParameterTxtArea()
	 */
	@Override
	public AceEditor getParameterTxtArea() {
		return parameterAceEditor;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getViewParameterList()
	 */
	@Override
	public List<CQLParameter> getViewParameterList() {
		return viewParameterList;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#setViewParameterList(java.util.List)
	 */
	@Override
	public void setViewParameterList(List<CQLParameter> viewParameterList) {
		this.viewParameterList = viewParameterList;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getParameterMap()
	 */
	@Override
	public HashMap<String, CQLParameter> getParameterMap() {
		return parameterMap;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getParameterNameMap()
	 */
	@Override
	public HashMap<String, String> getParameterNameMap() {
		return parameterNameMap;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getParameterNameListBox()
	 */
	@Override
	public ListBox getParameterNameListBox() {
		return parameterNameListBox;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getDefineNameMap()
	 */
	@Override
	public HashMap<String, String> getDefineNameMap() {
		return defineNameMap;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getDefinitionMap()
	 */
	@Override
	public HashMap<String, CQLDefinition> getDefinitionMap() {
		return definitionMap;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getDefineNameListBox()
	 */
	@Override
	public ListBox getDefineNameListBox() {
		return defineNameListBox;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getDeleteDefineButton()
	 */
	@Override
	public Button getDeleteDefineButton() {
		return deleteDefineButton;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getViewDefinitions()
	 */
	@Override
	public List<CQLDefinition> getViewDefinitions() {
		return viewDefinitions;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#setViewDefinitions(java.util.List)
	 */
	@Override
	public void setViewDefinitions(List<CQLDefinition> viewDefinitions) {
		this.viewDefinitions = viewDefinitions;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getDefineNameTxtArea()
	 */
	@Override
	public TextArea getDefineNameTxtArea() {
		return defineNameTxtArea;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getAddDefineButton()
	 */
	@Override
	public Button getAddDefineButton() {
		return addDefineButton;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getParamBadge()
	 */
	@Override
	public Badge getParamBadge() {
		return paramBadge;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getParameterAceEditor()
	 */
	@Override
	public AceEditor getParameterAceEditor() {
		return parameterAceEditor;
	}
	
	/**
	 * Sets the parameter ace editor.
	 *
	 * @param parameterAceEditor the new parameter ace editor
	 */
	public void setParameterAceEditor(AceEditor parameterAceEditor) {
		this.parameterAceEditor = parameterAceEditor;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getDefineAceEditor()
	 */
	@Override
	public AceEditor getDefineAceEditor() {
		return defineAceEditor;
	}
	
	/**
	 * Sets the define ace editor.
	 *
	 * @param defineAceEditor the new define ace editor
	 */
	public void setDefineAceEditor(AceEditor defineAceEditor) {
		this.defineAceEditor = defineAceEditor;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getDefineBadge()
	 */
	@Override
	public Badge getDefineBadge() {
		return defineBadge;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getClickedMenu()
	 */
	@Override
	public String getClickedMenu() {
		return clickedMenu;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#setClickedMenu(java.lang.String)
	 */
	@Override
	public void setClickedMenu(String clickedMenu) {
		this.clickedMenu = clickedMenu;
	}
	
	
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getCurrentSelectedDefinitionObjId()
	 */
	@Override
	public String getCurrentSelectedDefinitionObjId() {
		return currentSelectedDefinitionObjId;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#setCurrentSelectedDefinitionObjId(java.lang.String)
	 */
	@Override
	public void setCurrentSelectedDefinitionObjId(
			String currentSelectedDefinitionObjId) {
		this.currentSelectedDefinitionObjId = currentSelectedDefinitionObjId;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getCurrentSelectedParamerterObjId()
	 */
	@Override
	public String getCurrentSelectedParamerterObjId() {
		return currentSelectedParamerterObjId;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#setCurrentSelectedParamerterObjId(java.lang.String)
	 */
	@Override
	public void setCurrentSelectedParamerterObjId(String currentSelectedParamerterObjId) {
		this.currentSelectedParamerterObjId = currentSelectedParamerterObjId;
	}
	
	/**
	 * Gets the cql ace editor.
	 *
	 * @return the cql ace editor
	 */
	@Override
	public AceEditor getCqlAceEditor() {
		return cqlAceEditor;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getSaveCQLGeneralInfoBtn()
	 */
	@Override
	public Button getSaveCQLGeneralInfoBtn() {
		return saveCQLGeneralInfoBtn;
	}
	
	/**
	 * Sets the save cql general info btn.
	 *
	 * @param saveCQLGeneralInfoBtn the new save cql general info btn
	 */
	public void setSaveCQLGeneralInfoBtn(Button saveCQLGeneralInfoBtn) {
		this.saveCQLGeneralInfoBtn = saveCQLGeneralInfoBtn;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getSuccessMessageAlert()
	 */
	@Override
	public Alert getSuccessMessageAlert() {
		return successMessageAlertGenInfo;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#setSuccessMessageAlert(org.gwtbootstrap3.client.ui.Alert)
	 */
	@Override
	public void setSuccessMessageAlert(Alert successMessageAlert) {
		successMessageAlertGenInfo = successMessageAlert;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getErrorMessageAlertGenInfo()
	 */
	@Override
	public Alert getErrorMessageAlertGenInfo() {
		return errorMessageAlertGenInfo;
	}
	
	/**
	 * Sets the error message alert gen info.
	 *
	 * @param errorMessageAlertGenInfo the new error message alert gen info
	 */
	public void setErrorMessageAlertGenInfo(Alert errorMessageAlertGenInfo) {
		this.errorMessageAlertGenInfo = errorMessageAlertGenInfo;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getSuccessMessageAlertDefinition()
	 */
	@Override
	public Alert getSuccessMessageAlertDefinition() {
		return successMessageAlertDefinition;
	}
	
	/**
	 * Sets the success message alert definition.
	 *
	 * @param successMessageAlertDefinition the new success message alert definition
	 */
	public void setSuccessMessageAlertDefinition(
			Alert successMessageAlertDefinition) {
		this.successMessageAlertDefinition = successMessageAlertDefinition;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getErrorMessageAlertDefinition()
	 */
	@Override
	public Alert getErrorMessageAlertDefinition() {
		return errorMessageAlertDefinition;
	}
	
	/**
	 * Sets the error message alert definition.
	 *
	 * @param errorMessageAlertDefinition the new error message alert definition
	 */
	public void setErrorMessageAlertDefinition(
			Alert errorMessageAlertDefinition) {
		this.errorMessageAlertDefinition = errorMessageAlertDefinition;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getSuccessMessageAlertParameter()
	 */
	@Override
	public Alert getSuccessMessageAlertParameter() {
		return successMessageAlertParameter;
	}
	
	/**
	 * Sets the success message alert parameter.
	 *
	 * @param successMessageAlertParameter the new success message alert parameter
	 */
	public void setSuccessMessageAlertParameter(
			Alert successMessageAlertParameter) {
		this.successMessageAlertParameter = successMessageAlertParameter;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getErrorMessageAlertParameter()
	 */
	@Override
	public Alert getErrorMessageAlertParameter() {
		return errorMessageAlertParameter;
	}
	
	/**
	 * Sets the error message alert parameter.
	 *
	 * @param errorMessageAlertParameter the new error message alert parameter
	 */
	public void setErrorMessageAlertParameter(Alert errorMessageAlertParameter) {
		this.errorMessageAlertParameter = errorMessageAlertParameter;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getContextToggleSwitch()
	 */
	@Override
	public ToggleSwitch getContextToggleSwitch() {
		// TODO Auto-generated method stub
		return contextToggleSwitch;
	}
	@Override
	public TextArea getFuncNameTxtArea() {
		return funcNameTxtArea;
	}
	
	public void setFuncNameTxtArea(TextArea funcNameTxtArea) {
		this.funcNameTxtArea = funcNameTxtArea;
	}
	@Override
	public PanelCollapse getFunctionCollapse() {
		return functionCollapse;
	}
	
	public void setFunctionCollapse(PanelCollapse functionCollapse) {
		this.functionCollapse = functionCollapse;
	}
	
}
