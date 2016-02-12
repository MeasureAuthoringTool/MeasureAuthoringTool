package mat.client.clause;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mat.client.shared.MatContext;
import mat.client.shared.SpacerWidget;
import mat.model.cql.CQLDefinition;
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
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.VerticalPanel;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;

public class CQLWorkSpaceView  implements CQLWorkSpacePresenter.ViewDisplay{
	
	
	private HorizontalPanel mainPanel = new HorizontalPanel();
	private VerticalPanel mainVPanel = new VerticalPanel();
	private HorizontalPanel tabPanel = new HorizontalPanel();
	private VerticalPanel rightHandNavPanel = new VerticalPanel();
	private FlowPanel mainFlowPanel = new FlowPanel();
	
	private AnchorListItem viewCQL = new AnchorListItem();
	private AnchorListItem generalInformation;
	private AnchorListItem parameterLibrary;
	private AnchorListItem definitionLibrary;
	private AnchorListItem functionLibrary;
	//private InlineRadio patientRadio = new InlineRadio("Context");
	private Alert successMessageAlertGenInfo = new Alert();
	private Alert errorMessageAlertGenInfo = new Alert();
	private Alert successMessageAlertDefinition = new Alert();
	private Alert errorMessageAlertDefinition = new Alert();
	private Alert successMessageAlertParameter = new Alert();
	private Alert errorMessageAlertParameter = new Alert();
	
	/**
	 * InlineRadio populationRadio.
	 */
	//private InlineRadio populationRadio = new InlineRadio("Context");
	/**
	 * TextArea parameterNameTxtArea.
	 */
	private TextArea parameterNameTxtArea = new TextArea();
	
	private AceEditor parameterAceEditor = new AceEditor();
	
	private AceEditor defineAceEditor = new AceEditor();
	
	/** The cql ace editor. */
	private AceEditor cqlAceEditor = new AceEditor();
	
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
	
	private List<CQLParameter> viewParameterList = new ArrayList<CQLParameter>();
	
	/**
	 * ListBox defineNameListBox.
	 */
	private ListBox defineNameListBox;
	/**
	 * TextArea defineNameTxtArea.
	 */
	private TextArea defineNameTxtArea = new TextArea();
	
	/**
	 * SuggestBox searchSuggestDefineTextBox.
	 */
	private SuggestBox searchSuggestDefineTextBox;
	/**
	 * List viewDefinitions.
	 */
	private List<CQLDefinition> viewDefinitions = new ArrayList<CQLDefinition>();
	/**
	 * HashMap defineNameMap.
	 */
	private HashMap<String, String> defineNameMap = new HashMap<String, String>();
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
	
	private Badge paramBadge = new Badge();
	private Badge defineBadge = new Badge();
	
	private Label paramLabel = new Label("Parameter");
	private Label defineLabel = new Label("Definition");
	PanelCollapse paramCollapse = new PanelCollapse();
	PanelCollapse defineCollapse = new PanelCollapse();
	public String clickedMenu = "general";
	public String currentSelectedClause = null;
	
	
	private String currentSelectedDefinitionObjId = null;
	private String currentSelectedParamerterObjId = null;
	private Button saveCQLGeneralInfoBtn = new Button("Save");
	private ToggleSwitch contextToggleSwitch = new ToggleSwitch();
	
	/*@Override
	public FlowPanel getMainFlowPanel() {
		return mainFlowPanel;
	}
	@Override
	public void setMainFlowPanel(FlowPanel mainFlowPanel) {
		this.mainFlowPanel = mainFlowPanel;
	}*/
	
	@Override
	public PanelCollapse getDefineCollapse() {
		return defineCollapse;
	}
	
	public void setDefineCollapse(PanelCollapse defineCollapse) {
		this.defineCollapse = defineCollapse;
	}
	
	@Override
	public PanelCollapse getParamCollapse() {
		return paramCollapse;
	}
	
	public void setParamCollapse(PanelCollapse paramCollapse) {
		this.paramCollapse = paramCollapse;
	}
	
	public CQLWorkSpaceView() {
		defineAceEditor.startEditor();
		parameterAceEditor.startEditor();
		cqlAceEditor.startEditor();
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
		buildLeftHandNavNar();
		buildGeneralInformation();
		mainFlowPanel.setWidth("700px");
		mainPanel.add(rightHandNavPanel);
		mainPanel.add(mainFlowPanel);
		
	}
	@Override
	public void buildCQLFileView(){
		mainFlowPanel.clear();
		
		VerticalPanel parameterVP = new VerticalPanel();
		HorizontalPanel parameterFP = new HorizontalPanel();
		
		cqlAceEditor.setMode(AceEditorMode.CQL);
		cqlAceEditor.setTheme(AceEditorTheme.ECLIPSE);
		
		cqlAceEditor.getElement().getStyle().setFontSize(14, Unit.PX);
		cqlAceEditor.setSize("500px", "500px");
		cqlAceEditor.setAutocompleteEnabled(true);
		cqlAceEditor.setReadOnly(true);
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
		
		
		//addCqlEventHandkers();
		mainFlowPanel.add(vp);
		
		
	}
	
	
	private void addParameterEventHandler(){
		/*getAddParameterButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setParameterIntoList();
			}
		});*/
		
		
		
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
	
	private void addDefineEventHandkers(){
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
		
		/*getAddDefineButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setDefinitionIntoList();
			}
		});*/
	}
	/**
	 *
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
	/**
	 *
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
	
	@Override
	public void updateSuggestOracle() {
		if (searchSuggestTextBox != null) {
			MultiWordSuggestOracle multiWordSuggestOracle = (MultiWordSuggestOracle) searchSuggestTextBox.getSuggestOracle();
			multiWordSuggestOracle.clear();
			multiWordSuggestOracle.addAll(parameterNameMap.values());
		}
	}
	
	@Override
	public void buildGeneralInformation(){
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
		contextToggleSwitch.setLabelWidth("125");
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
	private void buildLeftHandNavNar(){
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
		functionLibrary.setText("Functions");
		functionLibrary.setTitle("Functions");
		
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
	
	
	
	@Override
	public void buildParameterLibraryView(){
		mainFlowPanel.clear();
		VerticalPanel parameterVP = new VerticalPanel();
		HorizontalPanel parameterFP = new HorizontalPanel();
		
		Label parameterLabel = new Label(LabelType.INFO,"Parameter - optional");
		parameterLabel.setMarginTop(5);
		parameterLabel.setId("Parameter_Label");
		parameterNameTxtArea.setText("");
		parameterNameTxtArea.setPlaceholder("Enter Parameter Name here.");
		parameterNameTxtArea.setSize("260px", "25px");
		parameterNameTxtArea.setId("parameterNameField");
		parameterNameTxtArea.setName("parameterName");
		parameterLabel.setText("Parameter - Optional");
		
		
		//parameterAceEditor.startEditor();
		parameterAceEditor.setText("");
		parameterAceEditor.setMode(AceEditorMode.CQL);
		parameterAceEditor.setTheme(AceEditorTheme.ECLIPSE);
		parameterAceEditor.getElement().getStyle().setFontSize(14, Unit.PX);
		parameterAceEditor.setSize("500px", "500px");
		parameterAceEditor.setAutocompleteEnabled(true);
		parameterAceEditor.getElement().setAttribute("id", "Parameter_AceEditorID");
		
		//addParameterButton = new Button();
		addParameterButton.setType(ButtonType.PRIMARY);
		addParameterButton.setSize(ButtonSize.DEFAULT);
		addParameterButton.setId("AddParameter_Button");
		addParameterButton.setMarginTop(10);
		addParameterButton.setMarginLeft(15);
		addParameterButton.setTitle("Save");
		addParameterButton.setText("Save");
		
		deleteParameterButton.setType(ButtonType.PRIMARY);
		deleteParameterButton.setSize(ButtonSize.EXTRA_SMALL);
		deleteParameterButton.setIcon(IconType.REMOVE);
		deleteParameterButton.setTitle("Delete");
		deleteParameterButton.setText("Delete");
		deleteParameterButton.setId("DeleteParameter_Button");
		
		parameterNameTxtArea.getElement().setAttribute("style", "width:250px;height:25px;margin-top:5px;margin-left:15px;");
		
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
	@Override
	public void buildDefinitionLibraryView(){
		mainFlowPanel.clear();
		VerticalPanel definitionVP = new VerticalPanel();
		HorizontalPanel definitionFP = new HorizontalPanel();
		
		Label defineLabel = new Label(LabelType.INFO,"Definition Name");
		//parameterLabel.getElement().setAttribute("style", "font-size:90%;margin-left:15px;margin-top:15px;background-color:#0964A2;");
		
		defineLabel.setMarginTop(5);
		defineLabel.setId("Definition_Label");
		defineNameTxtArea.setText("");
		defineNameTxtArea.setPlaceholder("Enter Definition Name here.");
		defineNameTxtArea.setSize("260px", "25px");
		defineNameTxtArea.setId("parameterNameField");
		defineNameTxtArea.setName("parameterName");
		defineLabel.setText("Definition Name");
		
		
		defineAceEditor.setText("");
		defineAceEditor.setMode(AceEditorMode.CQL);
		defineAceEditor.setTheme(AceEditorTheme.ECLIPSE);
		defineAceEditor.getElement().getStyle().setFontSize(14, Unit.PX);
		defineAceEditor.setSize("500px", "500px");
		defineAceEditor.setAutocompleteEnabled(true);
		defineAceEditor.getElement().setAttribute("id", "Define_AceEditorID");
		
		addDefineButton.setType(ButtonType.PRIMARY);
		addDefineButton.setSize(ButtonSize.DEFAULT);
		addDefineButton.setId("AddDefine_Button");
		addDefineButton.setMarginTop(10);
		addDefineButton.setMarginLeft(15);
		addDefineButton.setTitle("Save");
		addDefineButton.setText("Save");
		
		deleteDefineButton.setType(ButtonType.PRIMARY);
		deleteDefineButton.setSize(ButtonSize.EXTRA_SMALL);
		deleteDefineButton.setIcon(IconType.REMOVE);
		deleteDefineButton.setTitle("Delete");
		deleteDefineButton.setText("Delete");
		deleteDefineButton.setId("DeleteDefine_Button");
		defineNameTxtArea.getElement().setAttribute("style", "width:250px;height:25px;margin-top:5px;margin-left:15px;");
		
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
		
		addDefineEventHandkers();
		mainFlowPanel.add(vp);
	}
	
	@Override
	public void updateSuggestDefineOracle() {
		if (searchSuggestDefineTextBox != null) {
			MultiWordSuggestOracle multiWordSuggestOracle = (MultiWordSuggestOracle) searchSuggestDefineTextBox.getSuggestOracle();
			multiWordSuggestOracle.clear();
			multiWordSuggestOracle.addAll(defineNameMap.values());
		}
	}
	
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
	public void buildFunctionLibraryView(){
		mainFlowPanel.clear();
	}
	
	
	
	/**
	 * Reset All components to default state.
	 */
	private void resetAll(){
		rightHandNavPanel.clear();
		mainFlowPanel.clear();
		parameterNameTxtArea.setText("");
		defineNameTxtArea.setText("");
		defineAceEditor.setText("");
		parameterAceEditor.setText("");
		cqlAceEditor.setText("");
		
		viewParameterList.clear();
		viewDefinitions.clear();
		
		if(paramCollapse != null){
			paramCollapse.clear();
		}
		if(defineCollapse != null){
			defineCollapse.clear();
		}
		
	}
	@Override
	public HorizontalPanel getMainPanel() {
		return mainPanel;
	}
	@Override
	public VerticalPanel getMainVPanel() {
		return mainVPanel;
	}
	
	@Override
	public AnchorListItem getGeneralInformation() {
		return generalInformation;
	}
	
	public void setGeneralInformation(AnchorListItem generalInformation) {
		this.generalInformation = generalInformation;
	}
	@Override
	public AnchorListItem getParameterLibrary() {
		return parameterLibrary;
	}
	
	public void setParameterLibrary(AnchorListItem parameterLibrary) {
		this.parameterLibrary = parameterLibrary;
	}
	@Override
	public AnchorListItem getDefinitionLibrary() {
		return definitionLibrary;
	}
	
	public void setDefinitionLibrary(AnchorListItem definitionLibrary) {
		this.definitionLibrary = definitionLibrary;
	}
	@Override
	public AnchorListItem getFunctionLibrary() {
		return functionLibrary;
	}
	
	public void setFunctionLibrary(AnchorListItem functionLibrary) {
		this.functionLibrary = functionLibrary;
	}
	@Override
	public AnchorListItem getViewCQL() {
		return viewCQL;
	}
	
	public void setViewCQL(AnchorListItem viewCQL) {
		this.viewCQL = viewCQL;
	}
	@Override
	public Button getAddParameterButton() {
		return addParameterButton;
	}
	@Override
	public Button getRemoveParameterButton() {
		return removeParameterButton;
	}
	@Override
	public TextArea getParameterNameTxtArea() {
		return parameterNameTxtArea;
	}
	@Override
	public AceEditor getParameterTxtArea() {
		return parameterAceEditor;
	}
	@Override
	public List<CQLParameter> getViewParameterList() {
		return viewParameterList;
	}
	@Override
	public void setViewParameterList(List<CQLParameter> viewParameterList) {
		this.viewParameterList = viewParameterList;
	}
	
	@Override
	public HashMap<String, CQLParameter> getParameterMap() {
		return parameterMap;
	}
	@Override
	public HashMap<String, String> getParameterNameMap() {
		return parameterNameMap;
	}
	
	@Override
	public ListBox getParameterNameListBox() {
		return parameterNameListBox;
	}
	@Override
	public HashMap<String, String> getDefineNameMap() {
		return defineNameMap;
	}
	@Override
	public HashMap<String, CQLDefinition> getDefinitionMap() {
		return definitionMap;
	}
	@Override
	public ListBox getDefineNameListBox() {
		return defineNameListBox;
	}
	@Override
	public Button getDeleteDefineButton() {
		return deleteDefineButton;
	}
	@Override
	public List<CQLDefinition> getViewDefinitions() {
		return viewDefinitions;
	}
	@Override
	public void setViewDefinitions(List<CQLDefinition> viewDefinitions) {
		this.viewDefinitions = viewDefinitions;
	}
	@Override
	public TextArea getDefineNameTxtArea() {
		return defineNameTxtArea;
	}
	@Override
	public Button getAddDefineButton() {
		return addDefineButton;
	}
	@Override
	public Badge getParamBadge() {
		return paramBadge;
	}
	@Override
	public AceEditor getParameterAceEditor() {
		return parameterAceEditor;
	}
	
	public void setParameterAceEditor(AceEditor parameterAceEditor) {
		this.parameterAceEditor = parameterAceEditor;
	}
	@Override
	public AceEditor getDefineAceEditor() {
		return defineAceEditor;
	}
	
	public void setDefineAceEditor(AceEditor defineAceEditor) {
		this.defineAceEditor = defineAceEditor;
	}
	@Override
	public Badge getDefineBadge() {
		return defineBadge;
	}
	@Override
	public String getClickedMenu() {
		return clickedMenu;
	}
	@Override
	public void setClickedMenu(String clickedMenu) {
		this.clickedMenu = clickedMenu;
	}
	@Override
	public void setCurrentSelectedClause(String currentSelectedClause) {
		this.currentSelectedClause = currentSelectedClause;
	}
	
	@Override
	public String getCurrentSelectedClause() {
		return currentSelectedClause;
	}
	
	
	@Override
	public String getCurrentSelectedDefinitionObjId() {
		return currentSelectedDefinitionObjId;
	}
	
	@Override
	public void setCurrentSelectedDefinitionObjId(
			String currentSelectedDefinitionObjId) {
		this.currentSelectedDefinitionObjId = currentSelectedDefinitionObjId;
	}
	@Override
	public String getCurrentSelectedParamerterObjId() {
		return currentSelectedParamerterObjId;
	}
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
	
	@Override
	public Button getSaveCQLGeneralInfoBtn() {
		return saveCQLGeneralInfoBtn;
	}
	
	public void setSaveCQLGeneralInfoBtn(Button saveCQLGeneralInfoBtn) {
		this.saveCQLGeneralInfoBtn = saveCQLGeneralInfoBtn;
	}
	
	@Override
	public Alert getSuccessMessageAlert() {
		return successMessageAlertGenInfo;
	}
	
	@Override
	public void setSuccessMessageAlert(Alert successMessageAlert) {
		successMessageAlertGenInfo = successMessageAlert;
	}
	
	@Override
	public Alert getErrorMessageAlertGenInfo() {
		return errorMessageAlertGenInfo;
	}
	
	public void setErrorMessageAlertGenInfo(Alert errorMessageAlertGenInfo) {
		this.errorMessageAlertGenInfo = errorMessageAlertGenInfo;
	}
	
	@Override
	public Alert getSuccessMessageAlertDefinition() {
		return successMessageAlertDefinition;
	}
	
	public void setSuccessMessageAlertDefinition(
			Alert successMessageAlertDefinition) {
		this.successMessageAlertDefinition = successMessageAlertDefinition;
	}
	
	@Override
	public Alert getErrorMessageAlertDefinition() {
		return errorMessageAlertDefinition;
	}
	
	public void setErrorMessageAlertDefinition(
			Alert errorMessageAlertDefinition) {
		this.errorMessageAlertDefinition = errorMessageAlertDefinition;
	}
	
	@Override
	public Alert getSuccessMessageAlertParameter() {
		return successMessageAlertParameter;
	}
	
	public void setSuccessMessageAlertParameter(
			Alert successMessageAlertParameter) {
		this.successMessageAlertParameter = successMessageAlertParameter;
	}
	
	@Override
	public Alert getErrorMessageAlertParameter() {
		return errorMessageAlertParameter;
	}
	
	public void setErrorMessageAlertParameter(Alert errorMessageAlertParameter) {
		this.errorMessageAlertParameter = errorMessageAlertParameter;
	}
	
	@Override
	public ToggleSwitch getContextToggleSwitch() {
		// TODO Auto-generated method stub
		return contextToggleSwitch;
	}
	
}
