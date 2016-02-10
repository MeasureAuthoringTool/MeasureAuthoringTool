package mat.client.clause;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mat.client.shared.MatContext;
import mat.client.shared.SpacerWidget;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLParameter;
import mat.shared.UUIDUtilClient;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Badge;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.InlineRadio;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.NavPills;
import org.gwtbootstrap3.client.ui.NavTabs;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.LabelType;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

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
import com.google.gwt.user.client.Window;
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
	/*private AnchorListItem generalInformation = new AnchorListItem();
	private AnchorListItem parameterLibrary = new AnchorListItem();
	private AnchorListItem definitionLibrary = new AnchorListItem();
	private AnchorListItem functionLibrary = new AnchorListItem();*/
	private AnchorListItem viewCQL = new AnchorListItem();
	private AnchorListItem generalInformation;
	private AnchorListItem parameterLibrary;
	private AnchorListItem definitionLibrary;
	private AnchorListItem functionLibrary;
	private InlineRadio patientRadio = new InlineRadio("Context");
	
	/**
	 * InlineRadio populationRadio.
	 */
	private InlineRadio populationRadio = new InlineRadio("Context");
	/**
	 * TextArea parameterNameTxtArea.
	 */
	private TextArea parameterNameTxtArea = new TextArea();
	/**
	 * TextArea parameterTxtArea.
	 */
	/*private TextArea parameterTxtArea = new TextArea();*/
	private AceEditor parameterAceEditor = new AceEditor();

	/** The define ace editor. */
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

	
	/**
	 * Button addViewButton.
	 */
	private Button addCQLViewButton = new Button();

	private Badge paramBadge = new Badge();
	private Badge defineBadge = new Badge();

	private Label paramLabel = new Label("Parameter");
	private Label defineLabel = new Label("Definition");
	PanelCollapse paramCollapse = new PanelCollapse();
	PanelCollapse defineCollapse = new PanelCollapse();
	public String clickedMenu = "general";
	public String currentSelectedClause = null;
	
	
	public String currentSelectedDefinitionObjId = null;
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
		addHandler();
	}
	@Override
	public void buildCQLView(){
		resetAll();
		buildTabLayout();
		buildGeneralInformation();
		mainFlowPanel.setWidth("700px");

		mainVPanel.add(tabPanel);
		mainVPanel.add(mainFlowPanel);

	}
	protected void unsetActiveMenuItem(String menuClickedBefore) {

		if(menuClickedBefore.equalsIgnoreCase("general")){
			getGeneralInformation().setActive(false);
		} else if(menuClickedBefore.equalsIgnoreCase("param")){
			getParameterLibrary().setActive(false);
			if(getParamCollapse().getElement().getClassName().equalsIgnoreCase("panel-collapse collapse in")){
				getParamCollapse().getElement().setClassName("panel-collapse collapse");
			}
		} else if(menuClickedBefore.equalsIgnoreCase("define")){
			getDefinitionLibrary().setActive(false);
			if(getDefineCollapse().getElement().getClassName().equalsIgnoreCase("panel-collapse collapse in")){
				getDefineCollapse().getElement().setClassName("panel-collapse collapse");
			}
		} else if(menuClickedBefore.equalsIgnoreCase("func")){
			getFunctionLibrary().setActive(false);
		} else if(menuClickedBefore.equalsIgnoreCase("view")){
			getViewCQL().setActive(false);
		}
	}
	private void addHandler() {
		getGeneralInformation().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				unsetActiveMenuItem(clickedMenu);
				getGeneralInformation().setActive(true);
				clickedMenu = "general";
				buildGeneralInformation();

			}
		});

		getParameterLibrary().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				unsetActiveMenuItem(clickedMenu);
				getParameterLibrary().setActive(true);
				clickedMenu = "param";
				buildParameterLibraryView();

			}
		});
		getDefinitionLibrary().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				unsetActiveMenuItem(clickedMenu);
				getDefinitionLibrary().setActive(true);
				clickedMenu = "define";
				buildDefinitionLibraryView();

			}
		});

		getFunctionLibrary().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				unsetActiveMenuItem(clickedMenu);
				getFunctionLibrary().setActive(true);
				clickedMenu = "func";
				buildFunctionLibraryView();
			}
		});

		getViewCQL().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				unsetActiveMenuItem(clickedMenu);
				getViewCQL().setActive(true);
				clickedMenu = "view";
				buildViewCQLView();
			}
		});
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
					currentSelectedClause = selectedParamID;
					if(getParameterMap().get(selectedParamID) != null){
						getParameterNameTxtArea().setText(getParameterMap().get(selectedParamID).getParameterName());
						getParameterAceEditor().setText(getParameterMap().get(selectedParamID).getParameterLogic());
					}
				}

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
	public void setParameterIntoList() {
		String parameterName = getParameterNameTxtArea().getText();
		String parameterLogic = getParameterAceEditor().getText();
		getParameterNameTxtArea().clear();
		getParameterAceEditor().setText("");;
		if (!parameterName.isEmpty() && !parameterLogic.isEmpty()) {
			CQLParameter parameter = new CQLParameter();
			parameter.setParameterLogic(parameterLogic);
			parameter.setParameterName(parameterName);
			parameter.setId(UUIDUtilClient.uuid(5));
			getViewParameterList().add(parameter);
			clearAndAddParameterNamesToListBox();
			updateParamMap();
		} else {
			Window.alert("Parameter Name and Logic cannot be empty");
		}
	}
	/**
	 *
	 */
	private void updateParamMap() {
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
	public void setDefinitionIntoList() {
		String definitionName = getDefineNameTxtArea().getText();
		String definitionLogic = getDefineAceEditor().getText();
		getDefineNameTxtArea().clear();
		getDefineAceEditor().setText("");
		if (!definitionName.isEmpty() && !definitionLogic.isEmpty()) {

			CQLDefinition define = new CQLDefinition();
			define.setDefinitionName(definitionName);
			define.setDefinitionLogic(definitionLogic);
			define.setId(UUIDUtilClient.uuid(5));

			getViewDefinitions().add(define);
			clearAndAddDefinitionNamesToListBox();
			updateDefineMap();


		} else {
			Window.alert("Definition Name and Logic cannot be empty");
		}
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
		libraryNameValue.getElement().setAttribute("style", "margin-left:15px;width:360px;height:25px;");
		libraryNameValue.setText(MatContext.get().getCurrentMeasureName().replaceAll(" ", ""));
		libraryNameValue.setReadOnly(true);

		Label usingModeLabel = new Label(LabelType.INFO, "Using Model");
		TextArea usingModelValue = new TextArea();
		usingModeLabel.getElement().setAttribute("style", "font-size:90%;margin-left:15px;background-color:#0964A2;");
		usingModeLabel.setWidth("150px");
		usingModelValue.getElement().setAttribute("style", "margin-left:15px;width:360px;height:25px;");
		usingModelValue.setText("QDM");
		usingModelValue.setReadOnly(true);

		Label defaultContextLabel = new Label(LabelType.INFO, "Context");
		defaultContextLabel.getElement().setAttribute("style", "font-size:90%;margin-left:15px;background-color:#0964A2;");
		FlowPanel usingFlowPanel = new FlowPanel();
		patientRadio.setFormValue("Patient");
		patientRadio.setValue(true);
		populationRadio.setFormValue("Population");
		patientRadio.setText("Patient");
		populationRadio.setText("Population");
		usingFlowPanel.add(patientRadio);
		usingFlowPanel.add(populationRadio);
		usingFlowPanel.getElement().setAttribute("style", "margin-left:15px");


		generalInfoTopPanel.add(new SpacerWidget());
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
		generalInfoTopPanel.add(new SpacerWidget());
		generalInfoTopPanel.add(new SpacerWidget());
		VerticalPanel vp = new VerticalPanel();
		vp.setStyleName("cqlRightContainer");
		vp.setWidth("762px");
		generalInfoTopPanel.setWidth("750px");
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

	private void buildTabLayout(){
		NavTabs tabs = new NavTabs();
		generalInformation.setIcon(IconType.INFO);
		generalInformation.setText("General Information");
		generalInformation.setTitle("General Information");
		generalInformation.setActive(true);

		parameterLibrary.setIcon(IconType.PENCIL);
		parameterLibrary.setText("Parameter");
		parameterLibrary.setTitle("Parameter");

		definitionLibrary.setIcon(IconType.PENCIL);
		definitionLibrary.setText("Define");
		definitionLibrary.setTitle("Define");

		functionLibrary.setIcon(IconType.PENCIL);
		functionLibrary.setText("Functions");
		functionLibrary.setTitle("Functions");

		viewCQL.setIcon(IconType.BOOK);
		viewCQL.setText("View CQL");
		viewCQL.setTitle("View CQL");
		tabs.setJustified(true);
		tabs.add(generalInformation);
		tabs.add(parameterLibrary);
		tabs.add(definitionLibrary);
		tabs.add(functionLibrary);
		tabs.add(viewCQL);
		tabPanel.add(tabs);
		tabPanel.setWidth("900px");

	}


	@Override
	public void buildParameterLibraryView(){
		mainFlowPanel.clear();
		VerticalPanel parameterVP = new VerticalPanel();
		HorizontalPanel parameterFP = new HorizontalPanel();

		Label parameterLabel = new Label(LabelType.INFO,"Parameter - optional");
		parameterLabel.setMarginTop(5);
		parameterNameTxtArea.setPlaceholder("Enter Parameter Name here.");
		parameterNameTxtArea.setSize("260px", "25px");
		parameterNameTxtArea.setId("parameterNameField");
		parameterNameTxtArea.setName("parameterName");
		parameterLabel.setText("Parameter - Optional");


		parameterAceEditor.startEditor();
		parameterAceEditor.setMode(AceEditorMode.CQL);
		parameterAceEditor.setTheme(AceEditorTheme.ECLIPSE);
		parameterAceEditor.getElement().getStyle().setFontSize(14, Unit.PX);
		parameterAceEditor.setSize("500px", "500px");
		parameterAceEditor.setAutocompleteEnabled(true);

		//addParameterButton = new Button();
		addParameterButton.setType(ButtonType.PRIMARY);
		addParameterButton.setSize(ButtonSize.DEFAULT);

		addParameterButton.setMarginTop(10);
		addParameterButton.setMarginLeft(15);
		addParameterButton.setTitle("OK");
		addParameterButton.setText("OK");

		deleteParameterButton.setType(ButtonType.PRIMARY);
		deleteParameterButton.setSize(ButtonSize.EXTRA_SMALL);
		deleteParameterButton.setIcon(IconType.REMOVE);
		deleteParameterButton.setTitle("Delete");
		deleteParameterButton.setText("Delete");

		parameterNameTxtArea.getElement().setAttribute("style", "width:250px;height:25px;margin-top:5px;margin-left:15px;");

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
		VerticalPanel parameterVP = new VerticalPanel();
		HorizontalPanel parameterFP = new HorizontalPanel();

		Label defineLabel = new Label(LabelType.INFO,"Definition Name");
		//parameterLabel.getElement().setAttribute("style", "font-size:90%;margin-left:15px;margin-top:15px;background-color:#0964A2;");

		defineLabel.setMarginTop(5);
		defineNameTxtArea.setPlaceholder("Enter Definition Name here.");
		defineNameTxtArea.setSize("260px", "25px");
		defineNameTxtArea.setId("parameterNameField");
		defineNameTxtArea.setName("parameterName");
		defineLabel.setText("Definition Name");


		defineAceEditor.startEditor();
		defineAceEditor.setMode(AceEditorMode.CQL);
		defineAceEditor.setTheme(AceEditorTheme.ECLIPSE);
		defineAceEditor.getElement().getStyle().setFontSize(14, Unit.PX);
		defineAceEditor.setSize("500px", "500px");
		defineAceEditor.setAutocompleteEnabled(true);

		addDefineButton.setType(ButtonType.PRIMARY);
		addDefineButton.setSize(ButtonSize.DEFAULT);

		addDefineButton.setMarginTop(10);
		addDefineButton.setMarginLeft(15);
		addDefineButton.setTitle("OK");
		addDefineButton.setText("OK");

		deleteDefineButton.setType(ButtonType.PRIMARY);
		deleteDefineButton.setSize(ButtonSize.EXTRA_SMALL);
		deleteDefineButton.setIcon(IconType.REMOVE);
		deleteDefineButton.setTitle("Delete");
		deleteDefineButton.setText("Delete");

		defineNameTxtArea.getElement().setAttribute("style", "width:250px;height:25px;margin-top:5px;margin-left:15px;");


		parameterVP.add(defineLabel);
		parameterVP.add(new SpacerWidget());
		parameterVP.add(defineNameTxtArea);
		parameterVP.add(new SpacerWidget());
		parameterVP.add(defineAceEditor);
		parameterVP.add(new SpacerWidget());
		parameterVP.add(addDefineButton);
		parameterVP.setStyleName("topping");
		parameterFP.add(parameterVP);
		parameterFP.setStyleName("cqlRightContainer");

		VerticalPanel vp = new VerticalPanel();
		vp.setStyleName("cqlRightContainer");
		vp.setWidth("700px");
		vp.setHeight("500px");
		parameterFP.setWidth("700px");
		parameterFP.setStyleName("marginLeft15px");
		vp.add(parameterFP);

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

	@Override
	public void buildViewCQLView(){
		mainFlowPanel.clear();
		
		VerticalPanel parameterVP = new VerticalPanel();
		HorizontalPanel parameterFP = new HorizontalPanel();
		
		cqlAceEditor.startEditor();
		cqlAceEditor.setMode(AceEditorMode.CQL);
		cqlAceEditor.setTheme(AceEditorTheme.ECLIPSE);
		cqlAceEditor.getElement().getStyle().setFontSize(14, Unit.PX);
		cqlAceEditor.setSize("500px", "500px");
		cqlAceEditor.setAutocompleteEnabled(true);
		cqlAceEditor.setText("");
		
		parameterVP.add(cqlAceEditor);
		mainFlowPanel.add(parameterVP);
		
//		addCQLViewButton.setType(ButtonType.PRIMARY);
//		addCQLViewButton.setSize(ButtonSize.DEFAULT);
//
//		addCQLViewButton.setMarginTop(10);
//		addCQLViewButton.setMarginLeft(15);
//		addCQLViewButton.setTitle("OK");
//		addCQLViewButton.setText("OK");

		
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
	/**
	 * Reset All components to default state.
	 */
	private void resetAll(){
		rightHandNavPanel.clear();
		mainFlowPanel.clear();
		parameterNameTxtArea.setText("");
		defineNameTxtArea.setText("");
		/*generalInformation.setActive(true);
		parameterLibrary.setActive(false);
		definitionLibrary.setActive(false);
		functionLibrary.setActive(false)*/;
		viewParameterList.clear();
		viewDefinitions.clear();
		/*viewCQL.setActive(false);*/
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
	public InlineRadio getPatientRadio() {
		return patientRadio;
	}

	public void setPatientRadio(InlineRadio patientRadio) {
		this.patientRadio = patientRadio;
	}

	@Override
	public InlineRadio getPopulationRadio() {
		return populationRadio;
	}

	public void setPopulationRadio(InlineRadio populationRadio) {
		this.populationRadio = populationRadio;
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

	/**
	 * Gets the cql ace editor.
	 *
	 * @return the cql ace editor
	 */
	@Override
	public AceEditor getCqlAceEditor() {
		return cqlAceEditor;
	}

	/**
	 * Sets the cql ace editor.
	 *
	 * @param cqlAceEditor the new cql ace editor
	 */
//	@Override
//	public void setCqlAceEditor(AceEditor cqlAceEditor) {
//		this.cqlAceEditor = cqlAceEditor;
//	}

}
