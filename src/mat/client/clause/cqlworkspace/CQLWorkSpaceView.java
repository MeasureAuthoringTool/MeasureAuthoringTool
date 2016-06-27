package mat.client.clause.cqlworkspace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mat.client.CustomPager;
import mat.client.admin.ManageOrganizationPresenter.SearchDisplay;
import mat.client.shared.CQLButtonToolBar;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.MessageAlert;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageAlert;
import mat.client.shared.WarningConfirmationMessageAlert;
import mat.client.util.CellTableUtility;
import mat.model.QualityDataSetDTO;
import mat.model.clause.QDSAttributes;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLParameter;
import mat.shared.ClickableSafeHtmlCell;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Badge;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.InlineRadio;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.NavPills;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.LabelType;
import org.gwtbootstrap3.client.ui.constants.Pull;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.gwtbootstrap3.client.ui.gwt.CellTable;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;

// TODO: Auto-generated Javadoc
/**
 * The Class CQLWorkSpaceView.
 */
public class CQLWorkSpaceView implements CQLWorkSpacePresenter.ViewDisplay {
	
	/** The available qds attribute list. */
	private List<QDSAttributes> availableQDSAttributeList;
	/** The is editable. */
	boolean isEditable = false;
	
	/** The main horizontal panel. */
	HorizontalPanel mainHPPanel = new HorizontalPanel();
	
	/** The main horizontal panel. */
	private VerticalPanel mainPanel = new VerticalPanel();
	
	/** The cell table panel. */
	private VerticalPanel cellTablePanel = new VerticalPanel();
	
	/** Cell Table Row Count. */
	private static final int TABLE_ROW_COUNT = 2;
	
	/** The argument list table. */
	private CellTable<CQLFunctionArgument> argumentListTable;
	
	/** The sort provider. */
	private ListDataProvider<CQLFunctionArgument> listDataProvider;
	
	/** The spager. */
	private MatSimplePager spager;
	
	/** The main v panel. */
	private VerticalPanel mainVPanel = new VerticalPanel();
	
	/** The right hand nav panel. */
	private VerticalPanel rightHandNavPanel = new VerticalPanel();
	
	/** The main flow panel. */
	private FlowPanel mainFlowPanel = new FlowPanel();
	
	/** The message panel. */
	private HorizontalPanel messagePanel = new HorizontalPanel();
	
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
	
	/** The CQL success message. */
	private MessageAlert successMessageAlert = new SuccessMessageAlert();
	
	/** The CQL error message. */
	private MessageAlert errorMessageAlert = new ErrorMessageAlert();
	
	/** The CQL warning message. */
	private WarningConfirmationMessageAlert warningConfirmationMessageAlert = new WarningConfirmationMessageAlert();
	
	/** The CQL warning message. */
	private WarningConfirmationMessageAlert globalWarningConfirmationMessageAlert = new WarningConfirmationMessageAlert();
	
	/**
	 * TextArea parameterNameTxtArea.
	 */
	private TextBox parameterNameTxtArea = new TextBox();
	
	/** The parameter ace editor. */
	private AceEditor parameterAceEditor = new AceEditor();
	
	/** The define ace editor. */
	private AceEditor defineAceEditor = new AceEditor();
	
	/** The cql ace editor. */
	private AceEditor cqlAceEditor = new AceEditor();
	
	/** The Function Body ace editor. */
	private AceEditor functionBodyAceEditor = new AceEditor();
	
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
	
	/** The function argument list. */
	private List<CQLFunctionArgument> functionArgumentList = new ArrayList<CQLFunctionArgument>();
	
	/** The function arg name map. */
	private Map<String, CQLFunctionArgument> functionArgNameMap = new  HashMap<String, CQLFunctionArgument>();
	
	
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
	private TextBox defineNameTxtArea = new TextBox();
	
	/**
	 * TextArea defineNameTxtArea.
	 */
	private TextBox funcNameTxtArea = new TextBox();
	
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
	
	/** The applied qdm list. */
	private List<QualityDataSetDTO> appliedQdmList = new ArrayList<QualityDataSetDTO>();
	
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
	 * HashMap definitionMap.
	 */
	private HashMap<String, CQLFunctions> functionMap = new HashMap<String, CQLFunctions>();
	
	/** The add new argument. */
	private Button addNewArgument = new Button();
	
	/** The function button bar. */
	CQLButtonToolBar functionButtonBar = new CQLButtonToolBar("function");
	
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
	
	/** The clicked menu. */
	public String nextClickedMenu = "general";
	
	/** The current selected definition obj id. */
	private String currentSelectedDefinitionObjId = null;
	
	/** The current selected paramerter obj id. */
	private String currentSelectedParamerterObjId = null;
	
	/** The current selected function obj id. */
	private String currentSelectedFunctionObjId = null;
	
	/** The dirty flag for page. */
	private Boolean isPageDirty = false;
	
	/** The is double click. */
	private Boolean isDoubleClick = false;
	
	/** The is nav bar click. */
	private Boolean isNavBarClick = false;
	
	/** The vp. */
	VerticalPanel vp = new VerticalPanel();
	
	/** The context pat toggle switch. */
	private InlineRadio contextDefinePATRadioBtn = new InlineRadio("Patient");
	
	/** The context pop toggle switch. */
	private InlineRadio contextDefinePOPRadioBtn = new InlineRadio("Population");
	
	/** The define button bar. */
	private CQLButtonToolBar defineButtonBar = new CQLButtonToolBar("definition");
	
	/** The parameter button bar. */
	private CQLButtonToolBar parameterButtonBar = new CQLButtonToolBar("parameter");
	
	/** The context pat toggle switch. */
	private InlineRadio contextFuncPATRadioBtn = new InlineRadio("Patient");
	
	/** The context pop toggle switch. */
	private InlineRadio contextFuncPOPRadioBtn = new InlineRadio("Population");
	
	//private AnchorListItem includeLibrary;
	
	/**
	 * The Interface Observer.
	 */
	public static interface Observer {
		
		/**
		 * On modify clicked.
		 * 
		 * @param result
		 *            the result
		 */
		void onModifyClicked(CQLFunctionArgument result);
		
		/**
		 * On delete clicked.
		 *
		 * @param result
		 *            the result
		 * @param index
		 *            the index
		 */
		void onDeleteClicked(CQLFunctionArgument result, int index);
	}
	
	/** The observer. */
	private Observer observer;
	
	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getIsPageDirty()
	 */
	public Boolean getIsPageDirty() {
		return isPageDirty;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * setIsPageDirty(java.lang.Boolean)
	 */
	@Override
	public void setIsPageDirty(Boolean isPageDirty) {
		this.isPageDirty = isPageDirty;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * setIsDoubleClick(java.lang.Boolean)
	 */
	@Override
	public void setIsDoubleClick(Boolean isDoubleClick) {
		this.isDoubleClick = isDoubleClick;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * isDoubleClick()
	 */
	@Override
	public Boolean isDoubleClick() {
		return isDoubleClick;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#setIsNavBarClick(java.lang.Boolean)
	 */
	@Override
	public void setIsNavBarClick(Boolean isNavBarClick) {
		this.isNavBarClick = isNavBarClick;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#isNavBarClick()
	 */
	@Override
	public Boolean isNavBarClick() {
		return isNavBarClick;
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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLNewPresenter.ViewDisplay#buildView()
	 */
	/**
	 * Builds the view.
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
		mainPanel.add(new SpacerWidget());
		messagePanel.setStyleName("marginLeft15px");
		mainPanel.add(messagePanel);
		mainPanel.add(mainFlowPanel);
		
		resetMessageDisplay();
		
		mainHPPanel.addStyleName("cqlRightMessage");
		mainHPPanel.add(rightHandNavPanel);
		mainHPPanel.add(mainPanel);
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#buildCQLFileView()
	 */
	/**
	 * Builds the cql file view.
	 */
	@Override
	public void buildCQLFileView() {
		setCurrentSelectedFunctionObjId(null);
		setCurrentSelectedDefinitionObjId(null);
		setCurrentSelectedParamerterObjId(null);
		if (getFunctionArgumentList().size() > 0) {
			getFunctionArgumentList().clear();
		}
		getFunctionArgNameMap().clear();
		mainFlowPanel.clear();
		
		VerticalPanel parameterVP = new VerticalPanel();
		SimplePanel parameterFP = new SimplePanel();
		
		cqlAceEditor.setMode(AceEditorMode.CQL);
		cqlAceEditor.setTheme(AceEditorTheme.ECLIPSE);
		
		cqlAceEditor.getElement().getStyle().setFontSize(14, Unit.PX);
		cqlAceEditor.setSize("675px", "500px");
		cqlAceEditor.setAutocompleteEnabled(true);
		cqlAceEditor.setReadOnly(true);
		cqlAceEditor.setUseWrapMode(true);
		cqlAceEditor.clearAnnotations();
		cqlAceEditor.redisplay();
		Label viewCQlFileLabel = new Label(LabelType.INFO);
		viewCQlFileLabel.setText("View CQL file here");
		viewCQlFileLabel.setTitle("View CQL file here");
		parameterVP.add(new SpacerWidget());
		parameterVP.add(new SpacerWidget());
		parameterVP.add(viewCQlFileLabel);
		parameterVP.add(new SpacerWidget());
		parameterVP.add(new SpacerWidget());
		parameterVP.add(cqlAceEditor);
		parameterFP.add(parameterVP);
		parameterFP.setStyleName("cqlRightContainer");
		parameterFP.setWidth("700px");
		parameterFP.setStyleName("marginLeft15px");
		mainFlowPanel.add(parameterFP);
		
	}
	
	/**
	 * Adds the parameter event handler.
	 */
	private void addParameterEventHandler(){
		
		getParameterNameListBox().addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				parameterAceEditor.clearAnnotations();
				parameterAceEditor.removeAllMarkers();
				parameterAceEditor.redisplay();
				System.out.println("In addParameterEventHandler on DoubleClick isPageDirty = " + getIsPageDirty() + " selectedIndex = " + getParameterNameListBox().getSelectedIndex());
				setIsDoubleClick(true);
				setIsNavBarClick(false);
				if (getIsPageDirty()) {
					showUnsavedChangesWarning();
				} else {
					int selectedIndex  = getParameterNameListBox().getSelectedIndex();
					if (selectedIndex != -1) {
						final String selectedParamID = getParameterNameListBox().getValue(selectedIndex);
						currentSelectedParamerterObjId = selectedParamID;
						if(getParameterMap().get(selectedParamID) != null){
							getParameterNameTxtArea().setText(getParameterMap().get(selectedParamID).getParameterName());
							getParameterAceEditor().setText(getParameterMap().get(selectedParamID).getParameterLogic());
							System.out.println("In Parameter DoubleClickHandler, doing setText()");
							//disable parameterName and Logic fields for Default Parameter
							boolean isReadOnly = getParameterMap().get(selectedParamID).isReadOnly();
							
							if(MatContext.get().getMeasureLockService()
									.checkForEditPermission()){
								setParameterWidgetReadOnly(!isReadOnly);
							}
						}
					}
					successMessageAlert.clearAlert();
					errorMessageAlert.clearAlert();
				}
				
			}
		});
	}
	
	/**
	 * Adds the define event handkers.
	 */
	private void addDefineEventHandlers() {
		getDefineNameListBox().addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				defineAceEditor.clearAnnotations();
				defineAceEditor.removeAllMarkers();
				defineAceEditor.redisplay();
				setIsDoubleClick(true);
				if (getIsPageDirty()) {
					showUnsavedChangesWarning();
				} else {
					int selectedIndex = getDefineNameListBox().getSelectedIndex();
					if (selectedIndex != -1) {
						final String selectedDefinitionID = getDefineNameListBox().getValue(selectedIndex);
						currentSelectedDefinitionObjId = selectedDefinitionID;
						if (getDefinitionMap().get(selectedDefinitionID) != null) {
							getDefineNameTxtArea()
							.setText(getDefinitionMap().get(selectedDefinitionID).getDefinitionName());
							getDefineAceEditor()
							.setText(getDefinitionMap().get(selectedDefinitionID).getDefinitionLogic());
							if (getDefinitionMap().get(selectedDefinitionID).getContext().equalsIgnoreCase("patient")) {
								getContextDefinePATRadioBtn().setValue(true);
								getContextDefinePOPRadioBtn().setValue(false);
							} else {
								getContextDefinePOPRadioBtn().setValue(true);
								getContextDefinePATRadioBtn().setValue(false);
							}
							//disable definitionName and fields for Supplemental data definitions
							boolean isReadOnly = getDefinitionMap().get(selectedDefinitionID).isSupplDataElement();
							
							if(MatContext.get().getMeasureLockService()
									.checkForEditPermission()){
								setDefinitionWidgetReadOnly(!isReadOnly);
							}
						}
					}
					
					successMessageAlert.clearAlert();
					errorMessageAlert.clearAlert();
				}
			}
		});
	}
	
	/**
	 * Adds the Function event handlers.
	 */
	private void addFuncEventHandlers() {
		funcNameListBox.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				functionBodyAceEditor.clearAnnotations();
				functionBodyAceEditor.removeAllMarkers();
				functionBodyAceEditor.redisplay();
				setIsDoubleClick(true);
				setIsNavBarClick(false);
				if (getIsPageDirty()) {
					showUnsavedChangesWarning();
				} else {
					int selectedIndex = funcNameListBox.getSelectedIndex();
					if (selectedIndex != -1) {
						final String selectedFunctionId = funcNameListBox.getValue(selectedIndex);
						currentSelectedFunctionObjId = selectedFunctionId;
						if (functionMap.get(selectedFunctionId) != null) {
							getFuncNameTxtArea().setText(functionMap.get(selectedFunctionId).getFunctionName());
							getFunctionBodyAceEditor().setText(functionMap.get(selectedFunctionId).getFunctionLogic());
							if (functionMap.get(selectedFunctionId).getContext().equalsIgnoreCase("patient")) {
								contextFuncPATRadioBtn.setValue(true);
								contextFuncPOPRadioBtn.setValue(false);
							} else {
								contextFuncPOPRadioBtn.setValue(true);
								contextFuncPATRadioBtn.setValue(false);
							}
						}
					}
					if (currentSelectedFunctionObjId != null) {
						CQLFunctions selectedFunction = getFunctionMap().get(currentSelectedFunctionObjId);
						if (selectedFunction.getArgumentList() != null) {
							functionArgumentList.clear();
							functionArgumentList.addAll(selectedFunction.getArgumentList());
						} else {
							functionArgumentList.clear();
						}
					}
				}
				createAddArgumentViewForFunctions(functionArgumentList);
				successMessageAlert.clearAlert();
				errorMessageAlert.clearAlert();
			}
		});
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#updateParamMap()
	 */
	/**
	 * Update param map.
	 */
	@Override
	public void updateParamMap() {
		getParameterMap().clear();
		getParameterNameMap().clear();
		for (CQLParameter parameter : getViewParameterList()) {
			getParameterNameMap().put(parameter.getId(), parameter.getParameterName());
			getParameterMap().put(parameter.getId(), parameter);
		}
		updateSuggestOracle();
		if(getViewParameterList().size() < 10){
			getParamBadge().setText("0" + getViewParameterList().size());
		} else {
			getParamBadge().setText("" + getViewParameterList().size());
		}
		
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#updateDefineMap()
	 */
	/**
	 * Update define map.
	 */
	@Override
	public void updateDefineMap() {
		getDefinitionMap().clear();
		getDefineNameMap().clear();
		for (CQLDefinition define : getViewDefinitions()) {
			getDefineNameMap().put(define.getId(), define.getDefinitionName());
			getDefinitionMap().put(define.getId(), define);
		}
		
		updateSuggestDefineOracle();
		if(getViewDefinitions().size() < 10){
			getDefineBadge().setText("0" + getViewDefinitions().size());
		} else {
			getDefineBadge().setText("" + getViewDefinitions().size());
		}
		
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * updateFunctionMap()
	 */
	@Override
	public void updateFunctionMap() {
		functionMap.clear();
		funcNameMap.clear();
		//functionArgNameMap.clear();
		for (CQLFunctions function : viewFunctions) {
			funcNameMap.put(function.getId(), function.getFunctionName());
			functionMap.put(function.getId(), function);
			/*if (function.getArgumentList() != null) {
				for (CQLFunctionArgument argument : function.getArgumentList()) {
					functionArgNameMap.put(argument.getArgumentName().toLowerCase(), argument);
				}
			}*/
		}
		updateSuggestFuncOracle();
		if(viewFunctions.size() < 10){
			functionBadge.setText("0" + viewFunctions.size());
		} else {
			functionBadge.setText("" + viewFunctions.size());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#updateSuggestOracle()
	 */
	/**
	 * Update suggest oracle.
	 */
	@Override
	public void updateSuggestOracle() {
		if (searchSuggestTextBox != null) {
			MultiWordSuggestOracle multiWordSuggestOracle = (MultiWordSuggestOracle) searchSuggestTextBox
					.getSuggestOracle();
			multiWordSuggestOracle.clear();
			multiWordSuggestOracle.addAll(parameterNameMap.values());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#
	 * buildGeneralInformation()
	 */
	/**
	 * Builds the general information.
	 */
	@Override
	public void buildGeneralInformation() {
		setCurrentSelectedDefinitionObjId(null);
		setCurrentSelectedParamerterObjId(null);
		setCurrentSelectedFunctionObjId(null);
		getFunctionArgNameMap().clear();
		if (getFunctionArgumentList().size() > 0) {
			getFunctionArgumentList().clear();
		}
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
		
		generalInfoTopPanel.add(new SpacerWidget());
		// messagePanel.add(successMessageAlert);
		generalInfoTopPanel.add(new SpacerWidget());
		
		generalInfoTopPanel.add(libraryNameLabel);
		generalInfoTopPanel.add(new SpacerWidget());
		generalInfoTopPanel.add(libraryNameValue);
		generalInfoTopPanel.add(new SpacerWidget());
		generalInfoTopPanel.add(usingModeLabel);
		generalInfoTopPanel.add(new SpacerWidget());
		generalInfoTopPanel.add(usingModelValue);
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
		setCurrentSelectedFunctionObjId(null);
		getFunctionArgumentList().clear();
		getFunctionArgNameMap().clear();
		rightHandNavPanel.clear();
		NavPills navPills = new NavPills();
		navPills.setStacked(true);
		
		generalInformation = new AnchorListItem();
		//includeLibrary = new AnchorListItem();
		parameterLibrary = new AnchorListItem();
		definitionLibrary = new AnchorListItem();
		functionLibrary = new AnchorListItem();
		viewCQL = new AnchorListItem();
		
		generalInformation.setIcon(IconType.INFO);
		generalInformation.setText("General Information");
		generalInformation.setTitle("General Information");
		generalInformation.setActive(true);
		
		//includeLibrary.setIcon(IconType.INFO);
		//includeLibrary.setText("Inlude library");
		//includeLibrary.setTitle("Inlude library");
		//includeLibrary.setActive(true);
		
		parameterLibrary.setIcon(IconType.PENCIL);
		parameterLibrary.setTitle("Parameter");
		paramBadge.setText("" + viewParameterList.size());
		Anchor paramAnchor = (Anchor) (parameterLibrary.getWidget(0));
		// Double Click causing issues.So Event is not propogated
		paramAnchor.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				// TODO Auto-generated method stub
				event.stopPropagation();
			}
		});
		paramLabel.setStyleName("transparentLabel");
		paramAnchor.add(paramLabel);
		paramBadge.setPull(Pull.RIGHT);
		//paramBadge.setMarginLeft(45);
		paramAnchor.add(paramBadge);
		paramAnchor.setDataParent("#navGroup");
		paramAnchor.setDataToggle(Toggle.COLLAPSE);
		parameterLibrary.setHref("#collapseParameter");
		
		parameterLibrary.add(paramCollapse);
		
		definitionLibrary.setIcon(IconType.PENCIL);
		definitionLibrary.setTitle("Define");
		defineBadge.setText("" + viewDefinitions.size());
		Anchor defineAnchor = (Anchor) (definitionLibrary.getWidget(0));
		// Double Click causing issues.So Event is not propogated
		defineAnchor.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				// TODO Auto-generated method stub
				event.stopPropagation();
			}
		});
		defineLabel.setStyleName("transparentLabel");
		defineAnchor.add(defineLabel);
		defineBadge.setPull(Pull.RIGHT);
		//defineBadge.setMarginLeft(52);
		defineAnchor.add(defineBadge);
		defineAnchor.setDataParent("#navGroup");
		definitionLibrary.setDataToggle(Toggle.COLLAPSE);
		definitionLibrary.setHref("#collapseDefine");
		
		definitionLibrary.add(defineCollapse);
		
		functionLibrary.setIcon(IconType.PENCIL);
		/* functionLibrary.setText("Functions"); */
		functionLibrary.setTitle("Functions");
		
		functionBadge.setText("" + viewFunctions.size());
		Anchor funcAnchor = (Anchor) (functionLibrary.getWidget(0));
		// Double Click causing issues.So Event is not propogated
		funcAnchor.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				// TODO Auto-generated method stub
				event.stopPropagation();
			}
		});
		functionLibLabel.setStyleName("transparentLabel");
		funcAnchor.add(functionLibLabel);
		functionBadge.setPull(Pull.RIGHT);
		
		//functionBadge.setMarginLeft(57);
		funcAnchor.add(functionBadge);
		funcAnchor.setDataParent("#navGroup");
		functionLibrary.setDataToggle(Toggle.COLLAPSE);
		functionLibrary.setHref("#collapseFunction");
		
		functionLibrary.add(functionCollapse);
		
		viewCQL.setIcon(IconType.BOOK);
		viewCQL.setText("View CQL");
		viewCQL.setTitle("View CQL");
		
		navPills.add(generalInformation);
		//snavPills.add(includeLibrary);
		navPills.add(parameterLibrary);
		
		navPills.add(definitionLibrary);
		navPills.add(functionLibrary);
		navPills.add(viewCQL);
		
		navPills.setWidth("200px");
		
		messagePanel.add(successMessageAlert);
		messagePanel.add(errorMessageAlert);
		messagePanel.add(warningConfirmationMessageAlert);
		messagePanel.add(globalWarningConfirmationMessageAlert);
		
		// rightHandNavPanel.add(messagePanel);
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
		PanelCollapse defineCollapsePanel = new PanelCollapse();
		defineCollapsePanel.setId("collapseDefine");
		
		PanelBody defineCollapseBody = new PanelBody();
		
		HorizontalPanel defineFP = new HorizontalPanel();
		
		VerticalPanel rightVerticalPanel = new VerticalPanel();
		rightVerticalPanel.setSpacing(10);
		
		rightVerticalPanel.getElement().setId("rhsVerticalPanel_VerticalPanelParam");
		rightVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		Label defineLibraryLabel = new Label("Definition Library");
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
		rightVerticalPanel.setCellHorizontalAlignment(defineLibraryLabel, HasHorizontalAlignment.ALIGN_LEFT);
		defineFP.add(rightVerticalPanel);
		defineCollapseBody.add(defineFP);
		defineCollapsePanel.add(defineCollapseBody);
		return defineCollapsePanel;
		
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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#
	 * buildParameterLibraryView()
	 */
	/**
	 * Builds the parameter library view.
	 */
	@SuppressWarnings("static-access")
	@Override
	public void buildParameterLibraryView() {
		setCurrentSelectedDefinitionObjId(null);
		setCurrentSelectedParamerterObjId(null);
		setCurrentSelectedFunctionObjId(null);
		if (getFunctionArgumentList().size() > 0) {
			getFunctionArgumentList().clear();
		}
		getFunctionArgNameMap().clear();
		//setIsPageDirty(false);
		mainFlowPanel.clear();
		VerticalPanel parameterVP = new VerticalPanel();
		HorizontalPanel parameterFP = new HorizontalPanel();
		
		Label parameterLabel = new Label(LabelType.INFO, "Parameter");
		parameterLabel.setMarginTop(5);
		parameterLabel.setId("Parameter_Label");
		parameterNameTxtArea.setText("");
		//parameterNameTxtArea.setPlaceholder("Enter Parameter Name here.");
		parameterNameTxtArea.setSize("260px", "25px");
		parameterNameTxtArea.getElement().setId("parameterNameField");
		parameterNameTxtArea.setName("parameterName");
		parameterLabel.setText("Parameter");
		
		SimplePanel paramAceEditorPanel = new SimplePanel();
		paramAceEditorPanel.setSize("685px", "510px");
		// parameterAceEditor.startEditor();
		parameterAceEditor.setText("");
		System.out.println("In buildParameterLibraryView setText to ace editor.");
		parameterAceEditor.setMode(AceEditorMode.CQL);
		parameterAceEditor.setTheme(AceEditorTheme.ECLIPSE);
		parameterAceEditor.getElement().getStyle().setFontSize(14, Unit.PX);
		parameterAceEditor.setSize("675px", "500px");
		parameterAceEditor.setAutocompleteEnabled(true);
		parameterAceEditor.addAutoCompletions();
		parameterAceEditor.setUseWrapMode(true);
		parameterAceEditor.clearAnnotations();
		parameterAceEditor.removeAllMarkers();
		parameterAceEditor.redisplay();
		parameterAceEditor.getElement().setAttribute("id", "Parameter_AceEditorID");
		paramAceEditorPanel.add(parameterAceEditor);
		paramAceEditorPanel.getElement().setAttribute("id", "SimplePanel_Parameter_AceEditor");
		paramAceEditorPanel.setStyleName("cqlRightContainer");
		
		parameterNameTxtArea.getElement().setAttribute("style", "width:250px;height:25px;margin-top:5px;");
		
		parameterButtonBar.getInsertButton().setVisible(false);
		parameterButtonBar.getTimingExpButton().setVisible(false);
		parameterVP.add(new SpacerWidget());
		parameterVP.add(parameterLabel);
		parameterVP.add(new SpacerWidget());
		parameterVP.add(parameterNameTxtArea);
		parameterVP.add(new SpacerWidget());
		parameterVP.add(parameterButtonBar);
		parameterVP.add(paramAceEditorPanel);
		parameterVP.add(new SpacerWidget());
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
	 * 
	 * @param listBox
	 *            -ListBox
	 * @param suggestBox
	 *            - {@link SuggestBox}
	 */
	private void addListBoxHandler(final ListBox listBox, final SuggestBox suggestBox) {
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
	 * 
	 * @param suggestBox
	 *            - {@link SuggestBox}
	 * @param listBox
	 *            - {@link ListBox}
	 */
	private void addSuggestHandler(final SuggestBox suggestBox, final ListBox listBox) {
		suggestBox.addSelectionHandler(new SelectionHandler<Suggestion>() {
			
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				String selectedQDMName = event.getSelectedItem().getReplacementString();
				for (int i = 0; i < listBox.getItemCount(); i++) {
					if (selectedQDMName.equals(listBox.getItemText(i))) {
						listBox.setItemSelected(i, true);
						
						break;
					}
				}
			}
		});
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#
	 * clearAndAddParameterNamesToListBox()
	 */
	/**
	 * Clear and add parameter names to list box.
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
			com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement.getOptions();
			for (int i = 0; i < options.getLength(); i++) {
				String title = options.getItem(i).getText();
				OptionElement optionElement = options.getItem(i);
				optionElement.setTitle(title);
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#
	 * buildDefinitionLibraryView()
	 */
	/**
	 * Builds the definition library view.
	 */
	@SuppressWarnings("static-access")
	@Override
	public void buildDefinitionLibraryView() {
		setCurrentSelectedDefinitionObjId(null);
		setCurrentSelectedParamerterObjId(null);
		setCurrentSelectedFunctionObjId(null);
		getFunctionArgNameMap().clear();
		//setIsPageDirty(false);
		if (getFunctionArgumentList().size() > 0) {
			getFunctionArgumentList().clear();
		}
		mainFlowPanel.clear();
		VerticalPanel definitionVP = new VerticalPanel();
		HorizontalPanel definitionFP = new HorizontalPanel();
		
		Label defineLabel = new Label(LabelType.INFO, "Definition Name");
		defineLabel.setMarginTop(5);
		defineLabel.setId("Definition_Label");
		defineNameTxtArea.setText("");
		//defineNameTxtArea.setPlaceholder("Enter Definition Name here.");
		defineNameTxtArea.setSize("260px", "25px");
		defineNameTxtArea.getElement().setId("defineNameField");
		defineNameTxtArea.setName("defineName");
		defineLabel.setText("Definition Name");
		
		SimplePanel defAceEditorPanel = new SimplePanel();
		defAceEditorPanel.setSize("685px", "510px");
		defineAceEditor.setText("");
		defineAceEditor.setMode(AceEditorMode.CQL);
		defineAceEditor.setTheme(AceEditorTheme.ECLIPSE);
		defineAceEditor.getElement().getStyle().setFontSize(14, Unit.PX);
		defineAceEditor.setSize("675px", "500px");
		defineAceEditor.setAutocompleteEnabled(true);
		defineAceEditor.addAutoCompletions();
		defineAceEditor.setUseWrapMode(true);
		defineAceEditor.removeAllMarkers();
		defineAceEditor.clearAnnotations();
		defineAceEditor.redisplay();
		defineAceEditor.getElement().setAttribute("id", "Define_AceEditorID");
		defAceEditorPanel.add(defineAceEditor);
		defAceEditorPanel.getElement().setAttribute("id", "SimplePanel_Define_AceEditor");
		defAceEditorPanel.setStyleName("cqlRightContainer");
		
		Label defineContextLabel = new Label(LabelType.INFO, "Context");
		FlowPanel defineConextPanel = new FlowPanel();
		
		contextDefinePATRadioBtn.setValue(true);
		contextDefinePATRadioBtn.setText("Patient");
		contextDefinePATRadioBtn.setId("context_PatientRadioButton");
		contextDefinePOPRadioBtn.setValue(false);
		contextDefinePOPRadioBtn.setText("Population");
		contextDefinePOPRadioBtn.setId("context_PopulationRadioButton");
		
		defineConextPanel.add(contextDefinePATRadioBtn);
		defineConextPanel.add(contextDefinePOPRadioBtn);
		defineConextPanel.setStyleName("contextToggleSwitch");
		definitionVP.add(new SpacerWidget());
		definitionVP.add(defineLabel);
		definitionVP.add(new SpacerWidget());
		definitionVP.add(defineNameTxtArea);
		definitionVP.add(new SpacerWidget());
		definitionVP.add(defineContextLabel);
		definitionVP.add(new SpacerWidget());
		definitionVP.add(defineConextPanel);
		definitionVP.add(defineButtonBar);
		definitionVP.add(defAceEditorPanel);
		definitionVP.add(new SpacerWidget());
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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#
	 * updateSuggestDefineOracle()
	 */
	/**
	 * Update suggest define oracle.
	 */
	@Override
	public void updateSuggestDefineOracle() {
		if (searchSuggestDefineTextBox != null) {
			MultiWordSuggestOracle multiWordSuggestOracle = (MultiWordSuggestOracle) searchSuggestDefineTextBox
					.getSuggestOracle();
			multiWordSuggestOracle.clear();
			multiWordSuggestOracle.addAll(defineNameMap.values());
		}
	}
	
	/**
	 * Update suggest func oracle.
	 */
	@Override
	public void updateSuggestFuncOracle() {
		if (searchSuggestFuncTextBox != null) {
			MultiWordSuggestOracle multiWordSuggestOracle = (MultiWordSuggestOracle) searchSuggestFuncTextBox
					.getSuggestOracle();
			multiWordSuggestOracle.clear();
			multiWordSuggestOracle.addAll(funcNameMap.values());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#
	 * clearAndAddDefinitionNamesToListBox()
	 */
	/**
	 * Clear and add definition names to list box.
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
			com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement.getOptions();
			for (int i = 0; i < options.getLength(); i++) {
				String title = options.getItem(i).getText();
				OptionElement optionElement = options.getItem(i);
				optionElement.setTitle(title);
			}
		}
	}
	
	/**
	 * Clear and add functions names to list box.
	 */
	@Override
	public void clearAndAddFunctionsNamesToListBox() {
		if (funcNameListBox != null) {
			funcNameListBox.clear();
			for (CQLFunctions func : viewFunctions) {
				funcNameListBox.addItem(func.getFunctionName(), func.getId());
			}
			// Set tooltips for each element in listbox
			SelectElement selectElement = SelectElement.as(funcNameListBox.getElement());
			com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement.getOptions();
			for (int i = 0; i < options.getLength(); i++) {
				String title = options.getItem(i).getText();
				OptionElement optionElement = options.getItem(i);
				optionElement.setTitle(title);
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#
	 * buildFunctionLibraryView()
	 */
	/**
	 * Builds the function library view.
	 */
	@SuppressWarnings("static-access")
	@Override
	public void buildFunctionLibraryView() {
		setCurrentSelectedDefinitionObjId(null);
		setCurrentSelectedParamerterObjId(null);
		setCurrentSelectedFunctionObjId(null);
		getFunctionArgNameMap().clear();
		if (getFunctionArgumentList().size() > 0) {
			getFunctionArgumentList().clear();
		}
		mainFlowPanel.clear();
		
		VerticalPanel funcVP = new VerticalPanel();
		HorizontalPanel funcFP = new HorizontalPanel();
		
		Label functionNameLabel = new Label(LabelType.INFO, "Function Name");
		functionNameLabel.setMarginTop(5);
		functionNameLabel.setId("Function_Label");
		funcNameTxtArea.setText("");
		//funcNameTxtArea.setPlaceholder("Enter Function Name here.");
		funcNameTxtArea.setSize("260px", "25px");
		funcNameTxtArea.getElement().setId("FunctionNameField");
		funcNameTxtArea.setName("FunctionName");
		functionNameLabel.setText("Function Name");
		
		SimplePanel funcAceEditorPanel = new SimplePanel();
		funcAceEditorPanel.setSize("685", "510");
		functionBodyAceEditor.setText("");
		functionBodyAceEditor.setMode(AceEditorMode.CQL);
		functionBodyAceEditor.setTheme(AceEditorTheme.ECLIPSE);
		functionBodyAceEditor.getElement().getStyle().setFontSize(14, Unit.PX);
		functionBodyAceEditor.setSize("675px", "500px");
		functionBodyAceEditor.setAutocompleteEnabled(true);
		functionBodyAceEditor.addAutoCompletions();
		functionBodyAceEditor.setUseWrapMode(true);
		functionBodyAceEditor.clearAnnotations();
		functionBodyAceEditor.removeAllMarkers();
		functionBodyAceEditor.redisplay();
		functionBodyAceEditor.getElement().setAttribute("id", "Func_AceEditorID");
		funcAceEditorPanel.add(functionBodyAceEditor);
		funcAceEditorPanel.getElement().setAttribute("id", "SimplePanel_Function_AceEditor");
		funcAceEditorPanel.setStyleName("cqlRightContainer");
		
		addNewArgument.setType(ButtonType.LINK);
		addNewArgument.getElement().setId("addArgument_Button");
		
		addNewArgument.setTitle("Add Argument");
		addNewArgument.setText("Add Argument");
		addNewArgument.setId("Add_Argument_ID");
		addNewArgument.setIcon(IconType.PLUS);
		addNewArgument.setSize(ButtonSize.SMALL);
		
		addNewArgument.setPull(Pull.RIGHT);
		
		Label funcContextLabel = new Label(LabelType.INFO, "Context");
		FlowPanel funcConextPanel = new FlowPanel();
		
		contextFuncPATRadioBtn.setValue(true);
		contextFuncPATRadioBtn.setText("Patient");
		contextFuncPATRadioBtn.setId("context_PatientRadioButton");
		contextFuncPOPRadioBtn.setValue(false);
		contextFuncPOPRadioBtn.setText("Population");
		contextFuncPOPRadioBtn.setId("context_PopulationRadioButton");
		
		funcConextPanel.add(contextFuncPATRadioBtn);
		funcConextPanel.add(contextFuncPOPRadioBtn);
		funcConextPanel.setStyleName("contextToggleSwitch");
		
		funcVP.add(new SpacerWidget());
		funcVP.add(functionNameLabel);
		funcVP.add(new SpacerWidget());
		funcVP.add(funcNameTxtArea);
		funcVP.add(new SpacerWidget());
		funcVP.add(funcContextLabel);
		funcVP.add(new SpacerWidget());
		funcVP.add(funcConextPanel);
		funcVP.add(new SpacerWidget());
		funcVP.add(addNewArgument);
		createAddArgumentViewForFunctions(functionArgumentList);
		funcVP.add(cellTablePanel);
		funcVP.add(functionButtonBar);
		funcVP.add(new SpacerWidget());
		funcVP.add(funcAceEditorPanel);
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
		addFuncEventHandlers();
		mainFlowPanel.add(vp);
	}
	
	/**
	 * Creates the add argument view for functions.
	 *
	 * @param argumentList
	 *            the argument list
	 */
	@Override
	public void createAddArgumentViewForFunctions(List<CQLFunctionArgument> argumentList) {
		cellTablePanel.clear();
		cellTablePanel.setStyleName("cellTablePanel");
		isEditable = MatContext.get().getMeasureLockService().checkForEditPermission();
		
		if ((argumentList != null) && (argumentList.size() > 0)) {
			updateFunctionArgumentNameMap(argumentList);
			argumentListTable = new CellTable<CQLFunctionArgument>();
			argumentListTable.setStriped(true);
			argumentListTable.setCondensed(true);
			argumentListTable.setBordered(true);
			argumentListTable.setHover(true);
			
			argumentListTable.setPageSize(TABLE_ROW_COUNT);
			argumentListTable.redraw();
			listDataProvider = new ListDataProvider<CQLFunctionArgument>();
			listDataProvider.refresh();
			listDataProvider.getList().addAll(argumentList);
			ListHandler<CQLFunctionArgument> sortHandler = new ListHandler<CQLFunctionArgument>(
					listDataProvider.getList());
			argumentListTable.addColumnSortHandler(sortHandler);
			argumentListTable = addColumnToTable(argumentListTable, sortHandler);
			listDataProvider.addDataDisplay(argumentListTable);
			CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
			spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true);
			spager.setDisplay(argumentListTable);
			spager.setPageStart(0);
			cellTablePanel.add(argumentListTable);
			// cellTablePanel.add(new SpacerWidget());
			cellTablePanel.add(spager);
		} else {
			com.google.gwt.user.client.ui.Label tableHeader = new com.google.gwt.user.client.ui.Label(
					"Added Arguments List");
			tableHeader.getElement().setId("tableHeader_Label");
			tableHeader.setStyleName("measureGroupingTableHeader");
			tableHeader.getElement().setAttribute("tabIndex", "0");
			HTML desc = new HTML("<p> No Arguments Added.</p>");
			cellTablePanel.add(tableHeader);
			cellTablePanel.add(new SpacerWidget());
			cellTablePanel.add(desc);
		}
		/*
		 * } else { com.google.gwt.user.client.ui.Label tableHeader = new
		 * com.google.gwt.user.client.ui.Label("Added Arguments List");
		 * tableHeader.getElement().setId("tableHeader_Label");
		 * tableHeader.setStyleName("measureGroupingTableHeader");
		 * tableHeader.getElement().setAttribute("tabIndex", "0"); HTML desc =
		 * new HTML("<p> No Arguments Added.</p>");
		 * cellTablePanel.add(tableHeader); cellTablePanel.add(new
		 * SpacerWidget()); cellTablePanel.add(desc); }
		 */
		
	}
	
	/**
	 * Update function argument name map.
	 *
	 * @param argumentList the argument list
	 */
	private void updateFunctionArgumentNameMap(List<CQLFunctionArgument> argumentList) {
		functionArgNameMap.clear();
		if (argumentList != null) {
			for (CQLFunctionArgument argument : argumentList) {
				functionArgNameMap.put(argument.getArgumentName().toLowerCase(), argument);
			}
		}
		
	}
	
	/**
	 * Gets the composite cell for qdm modify and delete.
	 *
	 * @return the composite cell for qdm modify and delete
	 */
	private CompositeCell<CQLFunctionArgument> getCompositeCellForQDMModifyAndDelete() {
		final List<HasCell<CQLFunctionArgument, ?>> cells = new LinkedList<HasCell<CQLFunctionArgument, ?>>();
		if (isEditable) {
			cells.add(getModifyQDMButtonCell());
			cells.add(getDeleteQDMButtonCell());
		}
		
		CompositeCell<CQLFunctionArgument> cell = new CompositeCell<CQLFunctionArgument>(cells) {
			@Override
			public void render(Context context, CQLFunctionArgument object, SafeHtmlBuilder sb) {
				sb.appendHtmlConstant("<table tabindex=\"-1\"><tbody><tr tabindex=\"-1\">");
				for (HasCell<CQLFunctionArgument, ?> hasCell : cells) {
					render(context, object, sb, hasCell);
				}
				sb.appendHtmlConstant("</tr></tbody></table>");
			}
			
			@Override
			protected <X> void render(Context context, CQLFunctionArgument object, SafeHtmlBuilder sb,
					HasCell<CQLFunctionArgument, X> hasCell) {
				Cell<X> cell = hasCell.getCell();
				sb.appendHtmlConstant("<td class='emptySpaces' tabindex=\"0\">");
				if ((object != null)) {
					cell.render(context, hasCell.getValue(object), sb);
				} else {
					sb.appendHtmlConstant("<span tabindex=\"-1\"></span>");
				}
				sb.appendHtmlConstant("</td>");
			}
			
			@Override
			protected Element getContainerElement(Element parent) {
				return parent.getFirstChildElement().getFirstChildElement().getFirstChildElement();
			}
		};
		return cell;
	}
	
	/**
	 * Gets the modify qdm button cell.
	 *
	 * @return the modify qdm button cell
	 */
	private HasCell<CQLFunctionArgument, SafeHtml> getModifyQDMButtonCell() {
		
		HasCell<CQLFunctionArgument, SafeHtml> hasCell = new HasCell<CQLFunctionArgument, SafeHtml>() {
			
			ClickableSafeHtmlCell modifyButonCell = new ClickableSafeHtmlCell();
			
			@Override
			public Cell<SafeHtml> getCell() {
				return modifyButonCell;
			}
			
			@Override
			public FieldUpdater<CQLFunctionArgument, SafeHtml> getFieldUpdater() {
				
				return new FieldUpdater<CQLFunctionArgument, SafeHtml>() {
					@Override
					public void update(int index, CQLFunctionArgument object, SafeHtml value) {
						if ((object != null)) {
							observer.onModifyClicked(object);
						}
					}
				};
			}
			
			@Override
			public SafeHtml getValue(CQLFunctionArgument object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				String title = "Click to Modify QDM";
				String cssClass = "customEditButton";
				
				if (isEditable) {
					sb.appendHtmlConstant("<button tabindex=\"0\" type=\"button\" title='" + title + "' class=\" "
							+ cssClass + "\">Editable</button>");
				} else {
					sb.appendHtmlConstant("<button tabindex=\"0\" type=\"button\" title='" + title + "' class=\" "
							+ cssClass + "\" disabled/>Editable</button>");
				}
				
				return sb.toSafeHtml();
			}
		};
		
		return hasCell;
	}
	
	/**
	 * Gets the delete qdm button cell.
	 * 
	 * @return the delete qdm button cell
	 */
	private HasCell<CQLFunctionArgument, SafeHtml> getDeleteQDMButtonCell() {
		
		HasCell<CQLFunctionArgument, SafeHtml> hasCell = new HasCell<CQLFunctionArgument, SafeHtml>() {
			
			ClickableSafeHtmlCell deleteButonCell = new ClickableSafeHtmlCell();
			
			@Override
			public Cell<SafeHtml> getCell() {
				return deleteButonCell;
			}
			
			@Override
			public FieldUpdater<CQLFunctionArgument, SafeHtml> getFieldUpdater() {
				
				return new FieldUpdater<CQLFunctionArgument, SafeHtml>() {
					@Override
					public void update(int index, CQLFunctionArgument object, SafeHtml value) {
						observer.onDeleteClicked(object, index);
					}
				};
			}
			
			@Override
			public SafeHtml getValue(CQLFunctionArgument object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				String title = "Click to Delete QDM";
				String cssClass;
				
				cssClass = "customDeleteButton";
				if (isEditable) {
					sb.appendHtmlConstant("<button tabindex=\"0\"type=\"button\" title='" + title + "' class=\" "
							+ cssClass + "\"/>Delete</button>");
					
				} else {
					sb.appendHtmlConstant("<button tabindex=\"0\"type=\"button\" title='" + title + "' class=\" "
							+ cssClass + "\" disabled/>Delete</button>");
				}
				
				return sb.toSafeHtml();
			}
		};
		
		return hasCell;
	}
	
	/**
	 * Adds the column to table.
	 *
	 * @param table
	 *            the table
	 * @param sortHandler
	 *            the sort handler
	 * @return the cell table
	 */
	private CellTable<CQLFunctionArgument> addColumnToTable(CellTable<CQLFunctionArgument> table,
			ListHandler<CQLFunctionArgument> sortHandler) {
		if (table.getColumnCount() != TABLE_ROW_COUNT) {
			com.google.gwt.user.client.ui.Label searchHeader = new com.google.gwt.user.client.ui.Label(
					"Added Arguments List");
			searchHeader.getElement().setId("searchHeader_Label");
			searchHeader.setStyleName("measureGroupingTableHeader");
			searchHeader.getElement().setAttribute("tabIndex", "0");
			com.google.gwt.dom.client.TableElement elem = table.getElement().cast();
			TableCaptionElement caption = elem.createCaption();
			caption.appendChild(searchHeader.getElement());
			
			MultiSelectionModel<CQLFunctionArgument> selectionModel = new MultiSelectionModel<CQLFunctionArgument>();
			table.setSelectionModel(selectionModel);
			Column<CQLFunctionArgument, SafeHtml> nameColumn = new Column<CQLFunctionArgument, SafeHtml>(
					new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(CQLFunctionArgument object) {
					StringBuilder title = new StringBuilder();
					StringBuilder value = new StringBuilder(object.getArgumentName());
					
					title = title.append("Name : ").append(value);
					/*
					 * return
					 * CellTableUtility.getColumnToolTip(value.toString(),
					 * title.toString());
					 */
					return getDataTypeColumnToolTip(value.toString(), title, object.isValid());
				}
				
			};
			table.addColumn(nameColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Name\">" + "Name" + "</span>"));
			
			Column<CQLFunctionArgument, SafeHtml> dataTypeColumn = new Column<CQLFunctionArgument, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLFunctionArgument object) {
					StringBuilder title = new StringBuilder();
					StringBuilder value = new StringBuilder(object.getArgumentType());
					if (value.toString().equalsIgnoreCase(CQLWorkSpaceConstants.CQL_MODEL_DATA_TYPE)) {
						value = value.append(":").append(object.getQdmDataType());
						if (object.getAttributeName() != null) {
							value = value.append(".").append(object.getAttributeName());
						}
					} else if (value.toString().equalsIgnoreCase(CQLWorkSpaceConstants.CQL_OTHER_DATA_TYPE)) {
						value = value.append(":").append(object.getOtherType());
					}
					title = title.append("Datatype : ").append(value);
					return CellTableUtility.getColumnToolTip(value.toString(), title.toString());
				}
			};
			table.addColumn(dataTypeColumn,
					SafeHtmlUtils.fromSafeConstant("<span title=\"Datatype\">" + "Datatype" + "</span>"));
			
			String colName = "Modify";
			
			// Modify by Delete Column
			table.addColumn(
					new Column<CQLFunctionArgument, CQLFunctionArgument>(getCompositeCellForQDMModifyAndDelete()) {
						
						@Override
						public CQLFunctionArgument getValue(CQLFunctionArgument object) {
							return object;
						}
					}, SafeHtmlUtils.fromSafeConstant("<span title='" + colName + "'>  " + colName + "</span>"));
			
			table.setColumnWidth(0, 25.0, Unit.PCT);
			table.setColumnWidth(1, 35.0, Unit.PCT);
			table.setColumnWidth(2, 10.0, Unit.PCT);
		}
		return table;
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
		System.out.println(" in resetAll doing setText");
		cqlAceEditor.setText("");
		functionBodyAceEditor.setText("");
		
		viewParameterList.clear();
		viewDefinitions.clear();
		viewFunctions.clear();
		
		if (paramCollapse != null) {
			paramCollapse.clear();
		}
		if (defineCollapse != null) {
			defineCollapse.clear();
		}
		if (functionCollapse != null) {
			functionCollapse.clear();
		}
		
		setIsPageDirty(false);
		resetMessageDisplay();
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getMainPanel()
	 */
	/**
	 * Gets the main panel.
	 *
	 * @return the main panel
	 */
	@Override
	public VerticalPanel getMainPanel() {
		return mainPanel;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getMainVPanel()
	 */
	/**
	 * Gets the main h panel.
	 *
	 * @return the main h panel
	 */
	@Override
	public HorizontalPanel getMainHPanel() {
		return mainHPPanel;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getMainVPanel()
	 */
	/**
	 * Gets the main v panel.
	 *
	 * @return the main v panel
	 */
	@Override
	public VerticalPanel getMainVPanel() {
		return mainVPanel;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getGeneralInformation
	 * ()
	 */
	/**
	 * Gets the general information.
	 *
	 * @return the general information
	 */
	@Override
	public AnchorListItem getGeneralInformation() {
		return generalInformation;
	}
	
	/*@Override
	public AnchorListItem getIncludeLibrary() {
		return includeLibrary;
	}
	*/
	/**
	 * Sets the general information.
	 *
	 * @param generalInformation
	 *            the new general information
	 */
	public void setGeneralInformation(AnchorListItem generalInformation) {
		this.generalInformation = generalInformation;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getParameterLibrary()
	 */
	/**
	 * Gets the parameter library.
	 *
	 * @return the parameter library
	 */
	@Override
	public AnchorListItem getParameterLibrary() {
		return parameterLibrary;
	}
	
	/**
	 * Sets the parameter library.
	 *
	 * @param parameterLibrary
	 *            the new parameter library
	 */
	public void setParameterLibrary(AnchorListItem parameterLibrary) {
		this.parameterLibrary = parameterLibrary;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getDefinitionLibrary(
	 * )
	 */
	/**
	 * Gets the definition library.
	 *
	 * @return the definition library
	 */
	@Override
	public AnchorListItem getDefinitionLibrary() {
		return definitionLibrary;
	}
	
	/**
	 * Sets the definition library.
	 *
	 * @param definitionLibrary
	 *            the new definition library
	 */
	public void setDefinitionLibrary(AnchorListItem definitionLibrary) {
		this.definitionLibrary = definitionLibrary;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getFunctionLibrary()
	 */
	/**
	 * Gets the function library.
	 *
	 * @return the function library
	 */
	@Override
	public AnchorListItem getFunctionLibrary() {
		return functionLibrary;
	}
	
	/**
	 * Sets the function library.
	 *
	 * @param functionLibrary
	 *            the new function library
	 */
	public void setFunctionLibrary(AnchorListItem functionLibrary) {
		this.functionLibrary = functionLibrary;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getViewCQL()
	 */
	/**
	 * Gets the view cql.
	 *
	 * @return the view cql
	 */
	@Override
	public AnchorListItem getViewCQL() {
		return viewCQL;
	}
	
	/**
	 * Sets the view cql.
	 *
	 * @param viewCQL
	 *            the new view cql
	 */
	public void setViewCQL(AnchorListItem viewCQL) {
		this.viewCQL = viewCQL;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#
	 * getRemoveParameterButton()
	 */
	/**
	 * Gets the removes the parameter button.
	 *
	 * @return the removes the parameter button
	 */
	@Override
	public Button getRemoveParameterButton() {
		return removeParameterButton;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#
	 * getParameterNameTxtArea()
	 */
	/**
	 * Gets the parameter name txt area.
	 *
	 * @return the parameter name txt area
	 */
	@Override
	public TextBox getParameterNameTxtArea() {
		return parameterNameTxtArea;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getParameterTxtArea()
	 */
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getViewParameterList(
	 * )
	 */
	/**
	 * Gets the view parameter list.
	 *
	 * @return the view parameter list
	 */
	@Override
	public List<CQLParameter> getViewParameterList() {
		return viewParameterList;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#setViewParameterList(
	 * java.util.List)
	 */
	/**
	 * Sets the view parameter list.
	 *
	 * @param viewParameterList
	 *            the new view parameter list
	 */
	@Override
	public void setViewParameterList(List<CQLParameter> viewParameterList) {
		this.viewParameterList = viewParameterList;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getParameterMap()
	 */
	/**
	 * Gets the parameter map.
	 *
	 * @return the parameter map
	 */
	@Override
	public HashMap<String, CQLParameter> getParameterMap() {
		return parameterMap;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getParameterNameMap()
	 */
	/**
	 * Gets the parameter name map.
	 *
	 * @return the parameter name map
	 */
	@Override
	public HashMap<String, String> getParameterNameMap() {
		return parameterNameMap;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#
	 * getParameterNameListBox()
	 */
	/**
	 * Gets the parameter name list box.
	 *
	 * @return the parameter name list box
	 */
	@Override
	public ListBox getParameterNameListBox() {
		return parameterNameListBox;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getDefineNameMap()
	 */
	/**
	 * Gets the define name map.
	 *
	 * @return the define name map
	 */
	@Override
	public HashMap<String, String> getDefineNameMap() {
		return defineNameMap;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getDefinitionMap()
	 */
	/**
	 * Gets the definition map.
	 *
	 * @return the definition map
	 */
	@Override
	public HashMap<String, CQLDefinition> getDefinitionMap() {
		return definitionMap;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getDefineNameListBox(
	 * )
	 */
	/**
	 * Gets the define name list box.
	 *
	 * @return the define name list box
	 */
	@Override
	public ListBox getDefineNameListBox() {
		return defineNameListBox;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getDeleteDefineButton
	 * ()
	 */
	/**
	 * Gets the delete define button.
	 *
	 * @return the delete define button
	 */
	@Override
	public Button getDeleteDefineButton() {
		return defineButtonBar.getDeleteButton();
	}
	
	/**
	 * Gets the delete parameter button.
	 *
	 * @return the delete define button
	 */
	@Override
	public Button getDeleteParameterButton() {
		return parameterButtonBar.getDeleteButton();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getViewDefinitions()
	 */
	/**
	 * Gets the view definitions.
	 *
	 * @return the view definitions
	 */
	@Override
	public List<CQLDefinition> getViewDefinitions() {
		return viewDefinitions;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#setViewDefinitions(
	 * java.util.List)
	 */
	/**
	 * Sets the view definitions.
	 *
	 * @param viewDefinitions
	 *            the new view definitions
	 */
	@Override
	public void setViewDefinitions(List<CQLDefinition> viewDefinitions) {
		this.viewDefinitions = viewDefinitions;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getDefineNameTxtArea(
	 * )
	 */
	/**
	 * Gets the define name txt area.
	 *
	 * @return the define name txt area
	 */
	@Override
	public TextBox getDefineNameTxtArea() {
		return defineNameTxtArea;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getAddDefineButton()
	 */
	/**
	 * Gets the adds the define button.
	 *
	 * @return the adds the define button
	 */
	@Override
	public Button getAddDefineButton() {
		return defineButtonBar.getSaveButton();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getParamBadge()
	 */
	/**
	 * Gets the param badge.
	 *
	 * @return the param badge
	 */
	@Override
	public Badge getParamBadge() {
		return paramBadge;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getParameterAceEditor
	 * ()
	 */
	/**
	 * Gets the parameter ace editor.
	 *
	 * @return the parameter ace editor
	 */
	@Override
	public AceEditor getParameterAceEditor() {
		return parameterAceEditor;
	}
	
	/**
	 * Sets the parameter ace editor.
	 *
	 * @param parameterAceEditor
	 *            the new parameter ace editor
	 */
	public void setParameterAceEditor(AceEditor parameterAceEditor) {
		this.parameterAceEditor = parameterAceEditor;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getDefineAceEditor()
	 */
	/**
	 * Gets the define ace editor.
	 *
	 * @return the define ace editor
	 */
	@Override
	public AceEditor getDefineAceEditor() {
		return defineAceEditor;
	}
	
	/**
	 * Sets the define ace editor.
	 *
	 * @param defineAceEditor
	 *            the new define ace editor
	 */
	public void setDefineAceEditor(AceEditor defineAceEditor) {
		this.defineAceEditor = defineAceEditor;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getDefineBadge()
	 */
	/**
	 * Gets the define badge.
	 *
	 * @return the define badge
	 */
	@Override
	public Badge getDefineBadge() {
		return defineBadge;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getClickedMenu()
	 */
	/**
	 * Gets the clicked menu.
	 *
	 * @return the clicked menu
	 */
	@Override
	public String getClickedMenu() {
		return clickedMenu;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#setClickedMenu(java.
	 * lang.String)
	 */
	/**
	 * Sets the clicked menu.
	 *
	 * @param clickedMenu
	 *            the new clicked menu
	 */
	@Override
	public void setClickedMenu(String clickedMenu) {
		this.clickedMenu = clickedMenu;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getClickedMenu()
	 */
	/**
	 * Gets the clicked menu.
	 *
	 * @return the clicked menu
	 */
	@Override
	public String getNextClickedMenu() {
		return nextClickedMenu;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#setClickedMenu(java.
	 * lang.String)
	 */
	/**
	 * Sets the clicked menu.
	 *
	 * @param nextClickedMenu the new next clicked menu
	 */
	@Override
	public void setNextClickedMenu(String nextClickedMenu) {
		this.nextClickedMenu = nextClickedMenu;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#
	 * getCurrentSelectedDefinitionObjId()
	 */
	/**
	 * Gets the current selected definition obj id.
	 *
	 * @return the current selected definition obj id
	 */
	@Override
	public String getCurrentSelectedDefinitionObjId() {
		return currentSelectedDefinitionObjId;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#
	 * setCurrentSelectedDefinitionObjId(java.lang.String)
	 */
	/**
	 * Sets the current selected definition obj id.
	 *
	 * @param currentSelectedDefinitionObjId
	 *            the new current selected definition obj id
	 */
	@Override
	public void setCurrentSelectedDefinitionObjId(String currentSelectedDefinitionObjId) {
		this.currentSelectedDefinitionObjId = currentSelectedDefinitionObjId;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#
	 * getCurrentSelectedParamerterObjId()
	 */
	/**
	 * Gets the current selected paramerter obj id.
	 *
	 * @return the current selected paramerter obj id
	 */
	@Override
	public String getCurrentSelectedParamerterObjId() {
		return currentSelectedParamerterObjId;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#
	 * setCurrentSelectedParamerterObjId(java.lang.String)
	 */
	/**
	 * Sets the current selected paramerter obj id.
	 *
	 * @param currentSelectedParamerterObjId
	 *            the new current selected paramerter obj id
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
	
	/**
	 * Gets the message panel.
	 *
	 * @return the message panel
	 */
	@Override
	public HorizontalPanel getMessagePanel() {
		return messagePanel;
	}
	
	/**
	 * Sets the message panel.
	 *
	 * @param messagePanel
	 *            the new message panel
	 */
	public void setMessagePanel(HorizontalPanel messagePanel) {
		this.messagePanel = messagePanel;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#
	 * getSuccessMessageAlert()
	 */
	/**
	 * Gets the success message alert.
	 *
	 * @return the success message alert
	 */
	@Override
	public MessageAlert getSuccessMessageAlert() {
		return successMessageAlert;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#
	 * setSuccessMessageAlert(org.gwtbootstrap3.client.ui.Alert)
	 */
	/**
	 * Sets the success message alert.
	 *
	 * @param successMessageAlert
	 *            the new success message alert
	 */
	@Override
	public void setSuccessMessageAlert(SuccessMessageAlert successMessageAlert) {
		this.successMessageAlert = successMessageAlert;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#
	 * getErrorMessageAlertGenInfo()
	 */
	/**
	 * Gets the error message alert gen info.
	 *
	 * @return the error message alert gen info
	 */
	@Override
	public MessageAlert getErrorMessageAlert() {
		return errorMessageAlert;
	}
	
	/**
	 * Gets the func name txt area.
	 *
	 * @return the func name txt area
	 */
	@Override
	public TextBox getFuncNameTxtArea() {
		return funcNameTxtArea;
	}
	
	/**
	 * Sets the func name txt area.
	 *
	 * @param funcNameTxtArea
	 *            the new func name txt area
	 */
	public void setFuncNameTxtArea(TextBox funcNameTxtArea) {
		this.funcNameTxtArea = funcNameTxtArea;
	}
	
	/**
	 * Gets the function collapse.
	 *
	 * @return the function collapse
	 */
	@Override
	public PanelCollapse getFunctionCollapse() {
		return functionCollapse;
	}
	
	/**
	 * Sets the function collapse.
	 *
	 * @param functionCollapse
	 *            the new function collapse
	 */
	public void setFunctionCollapse(PanelCollapse functionCollapse) {
		this.functionCollapse = functionCollapse;
	}
	
	/**
	 * get the erase define button.
	 *
	 * @return the erase define button
	 */
	@Override
	public Button getEraseDefineButton() {
		return defineButtonBar.getEraseButton();
	}
	
	/**
	 * get the erase define button.
	 *
	 * @return the erase parameter button
	 */
	@Override
	public Button getEraseParameterButton() {
		return parameterButtonBar.getEraseButton();
	}
	
	/**
	 * get the erase define button.
	 *
	 * @return the erase parameter button
	 */
	@Override
	public Button getEraseFunctionButton() {
		return functionButtonBar.getEraseButton();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getSaveFunctionButton()
	 */
	@Override
	public Button getSaveFunctionButton() {
		return functionButtonBar.getSaveButton();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getFunctionBodyAceEditor()
	 */
	@Override
	public AceEditor getFunctionBodyAceEditor() {
		return functionBodyAceEditor;
	}
	
	/**
	 * Sets the function body ace editor.
	 *
	 * @param functionBodyAceEditor
	 *            the new function body ace editor
	 */
	public void setFunctionBodyAceEditor(AceEditor functionBodyAceEditor) {
		this.functionBodyAceEditor = functionBodyAceEditor;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getCurrentSelectedFunctionObjId()
	 */
	@Override
	public String getCurrentSelectedFunctionObjId() {
		return currentSelectedFunctionObjId;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * setCurrentSelectedFunctionObjId(java.lang.String)
	 */
	@Override
	public void setCurrentSelectedFunctionObjId(String currentSelectedFunctionObjId) {
		this.currentSelectedFunctionObjId = currentSelectedFunctionObjId;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getFunctionMap()
	 */
	@Override
	public HashMap<String, CQLFunctions> getFunctionMap() {
		return functionMap;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getViewFunctions()
	 */
	@Override
	public List<CQLFunctions> getViewFunctions() {
		return viewFunctions;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * setViewFunctions(java.util.List)
	 */
	@Override
	public void setViewFunctions(List<CQLFunctions> viewFunctions) {
		this.viewFunctions = viewFunctions;
	}
	
	/**
	 * Gets the define collapse.
	 *
	 * @return the define collapse
	 */
	@Override
	public PanelCollapse getDefineCollapse() {
		return defineCollapse;
	}
	
	/**
	 * Sets the define collapse.
	 *
	 * @param defineCollapse
	 *            the new define collapse
	 */
	public void setDefineCollapse(PanelCollapse defineCollapse) {
		this.defineCollapse = defineCollapse;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getClearParameterYesButton()
	 */
	@Override
	public Button getWarningConfirmationYesButton() {
		return getWarningConfirmationMessageAlert().getWarningConfirmationYesButton();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getClearParameterNoButton()
	 */
	@Override
	public Button getWarningConfirmationNoButton() {
		return getWarningConfirmationMessageAlert().getWarningConfirmationNoButton();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getClearParameterYesButton()
	 */
	@Override
	public Button getGlobalWarningConfirmationYesButton() {
		return getGlobalWarningConfirmationMessageAlert().getWarningConfirmationYesButton();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getClearParameterNoButton()
	 */
	@Override
	public Button getGlobalWarningConfirmationNoButton() {
		return getGlobalWarningConfirmationMessageAlert().getWarningConfirmationNoButton();
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getParamCollapse()
	 */
	/**
	 * Gets the param collapse.
	 *
	 * @return the param collapse
	 */
	@Override
	public PanelCollapse getParamCollapse() {
		return paramCollapse;
	}
	
	/**
	 * Sets the param collapse.
	 *
	 * @param paramCollapse
	 *            the new param collapse
	 */
	public void setParamCollapse(PanelCollapse paramCollapse) {
		this.paramCollapse = paramCollapse;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getAddNewArgument()
	 */
	@Override
	public Button getAddNewArgument() {
		return addNewArgument;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getObserver()
	 */
	@Override
	public Observer getObserver() {
		return observer;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * setObserver(mat.client.clause.cqlworkspace.CQLWorkSpaceView.Observer)
	 */
	@Override
	public void setObserver(Observer observer) {
		this.observer = observer;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getContextPATToggleSwitch()
	 */
	@Override
	public InlineRadio getContextDefinePATRadioBtn() {
		return contextDefinePATRadioBtn;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getContextPOPToggleSwitch()
	 */
	@Override
	public InlineRadio getContextDefinePOPRadioBtn() {
		return contextDefinePOPRadioBtn;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getAddParameterButton()
	 */
	@Override
	public Button getAddParameterButton() {
		return parameterButtonBar.getSaveButton();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getParameterButtonBar()
	 */
	@Override
	public CQLButtonToolBar getParameterButtonBar() {
		return parameterButtonBar;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getDefineButtonBar()
	 */
	@Override
	public CQLButtonToolBar getDefineButtonBar() {
		return defineButtonBar;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getFunctionButtonBar()
	 */
	@Override
	public CQLButtonToolBar getFunctionButtonBar() {
		return functionButtonBar;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getFunctionArgumentList()
	 */
	@Override
	public List<CQLFunctionArgument> getFunctionArgumentList() {
		return functionArgumentList;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * setFunctionArgumentList(java.util.List)
	 */
	@Override
	public void setFunctionArgumentList(List<CQLFunctionArgument> functionArgumentList) {
		this.functionArgumentList = functionArgumentList;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getAvailableQDSAttributeList()
	 */
	@Override
	public List<QDSAttributes> getAvailableQDSAttributeList() {
		return availableQDSAttributeList;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * setAvailableQDSAttributeList(java.util.List)
	 */
	@Override
	public void setAvailableQDSAttributeList(List<QDSAttributes> availableQDSAttributeList) {
		this.availableQDSAttributeList = availableQDSAttributeList;
	}
	
	/**
	 * Checks if is editable.
	 *
	 * @return true, if is editable
	 */
	public boolean isEditable() {
		return isEditable;
	}
	
	/**
	 * Gets the cell table panel.
	 *
	 * @return the cell table panel
	 */
	public VerticalPanel getCellTablePanel() {
		return cellTablePanel;
	}
	
	/**
	 * Gets the table row count.
	 *
	 * @return the table row count
	 */
	public static int getTableRowCount() {
		return TABLE_ROW_COUNT;
	}
	
	/**
	 * Gets the argument list table.
	 *
	 * @return the argument list table
	 */
	public CellTable<CQLFunctionArgument> getArgumentListTable() {
		return argumentListTable;
	}
	
	/**
	 * Gets the list data provider.
	 *
	 * @return the list data provider
	 */
	public ListDataProvider<CQLFunctionArgument> getListDataProvider() {
		return listDataProvider;
	}
	
	/**
	 * Gets the spager.
	 *
	 * @return the spager
	 */
	public MatSimplePager getSpager() {
		return spager;
	}
	
	/**
	 * Gets the right hand nav panel.
	 *
	 * @return the right hand nav panel
	 */
	public VerticalPanel getRightHandNavPanel() {
		return rightHandNavPanel;
	}
	
	/**
	 * Gets the main flow panel.
	 *
	 * @return the main flow panel
	 */
	public FlowPanel getMainFlowPanel() {
		return mainFlowPanel;
	}
	
	/**
	 * Gets the search suggest text box.
	 *
	 * @return the search suggest text box
	 */
	@Override
	public SuggestBox getSearchSuggestTextBox() {
		return searchSuggestTextBox;
	}
	
	/**
	 * Gets the func name list box.
	 *
	 * @return the func name list box
	 */
	@Override
	public ListBox getFuncNameListBox() {
		return funcNameListBox;
	}
	
	/**
	 * Gets the search suggest define text box.
	 *
	 * @return the search suggest define text box
	 */
	@Override
	public SuggestBox getSearchSuggestDefineTextBox() {
		return searchSuggestDefineTextBox;
	}
	
	/**
	 * Gets the search suggest func text box.
	 *
	 * @return the search suggest func text box
	 */
	@Override
	public SuggestBox getSearchSuggestFuncTextBox() {
		return searchSuggestFuncTextBox;
	}
	
	/**
	 * Gets the func name map.
	 *
	 * @return the func name map
	 */
	public HashMap<String, String> getFuncNameMap() {
		return funcNameMap;
	}
	
	/**
	 * Gets the function badge.
	 *
	 * @return the function badge
	 */
	@Override
	public Badge getFunctionBadge() {
		return functionBadge;
	}
	
	/**
	 * Gets the param label.
	 *
	 * @return the param label
	 */
	public Label getParamLabel() {
		return paramLabel;
	}
	
	/**
	 * Gets the define label.
	 *
	 * @return the define label
	 */
	public Label getDefineLabel() {
		return defineLabel;
	}
	
	/**
	 * Gets the function lib label.
	 *
	 * @return the function lib label
	 */
	public Label getFunctionLibLabel() {
		return functionLibLabel;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getContextFuncPATRadioBtn()
	 */
	@Override
	public InlineRadio getContextFuncPATRadioBtn() {
		return contextFuncPATRadioBtn;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getContextFuncPOPRadioBtn()
	 */
	@Override
	public InlineRadio getContextFuncPOPRadioBtn() {
		return contextFuncPOPRadioBtn;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getFunctionArgNameMap()
	 */
	@Override
	public Map<String, CQLFunctionArgument> getFunctionArgNameMap() {
		return functionArgNameMap;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * setFunctionArgNameMap(java.util.HashMap)
	 */
	@Override
	public void setFunctionArgNameMap(HashMap<String, CQLFunctionArgument> functionArgNameMap) {
		this.functionArgNameMap = functionArgNameMap;
	}
	
	/**
	 * Gets the data type column tool tip.
	 *
	 * @param columnText
	 *            the column text
	 * @param title
	 *            the title
	 * @param hasImage
	 *            the has image
	 * @return the data type column tool tip
	 */
	private SafeHtml getDataTypeColumnToolTip(String columnText, StringBuilder title, boolean hasImage) {
		if (hasImage) {
			String htmlConstant = "<html>"
					+ "<head> </head> <Body><img src =\"images/error.png\" alt=\"Arugment Name is InValid.\""
					+ "title = \"Arugment Name is InValid.\"/>" + "<span tabIndex = \"0\" title='" + title + "'>"
					+ columnText + "</span></body>" + "</html>";
			return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
		} else {
			String htmlConstant = "<html>" + "<head> </head> <Body><span tabIndex = \"0\" title='" + title + "'>"
					+ columnText + "</span></body>" + "</html>";
			return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
		}
	}
	
	
	
	/**
	 * Sets the parameter widget read only.
	 *
	 * @param isEditable the new parameter widget read only
	 */
	@Override
	public void setParameterWidgetReadOnly(boolean isEditable){
		
		getParameterNameTxtArea().setEnabled(isEditable);
		getParameterAceEditor().setReadOnly(!isEditable);
		System.out.println("in setParameterWidgetReadOnly: setting Ace Editor read only flag. read only = " + !isEditable);
		getParameterButtonBar().getSaveButton().setEnabled(isEditable);
		getParameterButtonBar().getDeleteButton().setEnabled(isEditable);
		getParameterButtonBar().getInsertButton().setEnabled(isEditable);
	}
	
	/**
	 * Sets the define widget read only.
	 *
	 * @param isEditable the new definition widget read only
	 */
	@Override
	public void setDefinitionWidgetReadOnly(boolean isEditable){
		
		getDefineNameTxtArea().setEnabled(isEditable);
		getDefineAceEditor().setReadOnly(!isEditable);
		getContextDefinePATRadioBtn().setEnabled(isEditable);
		getContextDefinePOPRadioBtn().setEnabled(isEditable);
		System.out.println("in setDefinitionWidgetReadOnly: setting Ace Editor read only flag. read only = " + !isEditable);
		getDefineButtonBar().getSaveButton().setEnabled(isEditable);
		getDefineButtonBar().getDeleteButton().setEnabled(isEditable);
		getDefineButtonBar().getInsertButton().setEnabled(isEditable);
		getDefineButtonBar().getTimingExpButton().setEnabled(isEditable);
	}
	
	
	/*
	 * This Method is used to give information of Keyboard ShortcutKeys
	 *  on AceEditor for Users.
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#buildInfoPanel(com.google.gwt.user.client.ui.Widget)
	 */
	@Override
	public void buildInfoPanel(Widget sourceWidget){
		
		PopupPanel panel = new PopupPanel();
		panel.setAutoHideEnabled(true);
		panel.setPopupPosition(sourceWidget.getAbsoluteLeft()+40, sourceWidget.getAbsoluteTop()+20);
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.getElement().setId("dialogContents_VerticalPanel");
		panel.setWidget(dialogContents);
		
		HTML html1 = new HTML("CTRL-Shift-t  :timings");
		HTML html2 = new HTML("Ctrl-Shift-f  :functions");
		HTML html3 = new HTML("Ctrl-Shift-u  :definitions");
		HTML html4 = new HTML("Ctrl-Shift-p  :parameters");
		HTML html5 = new HTML("Ctrl-Shift-a  :attributes");
		HTML html6 = new HTML("Ctrl-Space :all Keywords");
		dialogContents.add(html1);
		dialogContents.add(html2);
		dialogContents.add(html3);
		dialogContents.add(html4);
		dialogContents.add(html5);
		dialogContents.add(html6);
		
		panel.show();
	}
	
	
	
	
	/**
	 * The Class CustomTextAreaWithNoWhiteSpaces.
	 */
	/*public class CustomTextAreaWithNoWhiteSpaces extends TextArea {
		
		*//** The max length. *//*
		private int maxLength;
		
		*//**
		 * Constructor.
		 *
		 * @param maxLen
		 *            the max len
		 *//*
		public CustomTextAreaWithNoWhiteSpaces(int maxLen) {
			
			super(Document.get().createTextAreaElement());
			maxLength = maxLen;
			setStyleName("gwt-TextArea");
			sinkEvents(Event.ONPASTE | Event.ONKEYDOWN | Event.ONKEYPRESS);
			
			CustomTextAreaWithNoWhiteSpaces.this.addValueChangeHandler(new ValueChangeHandler<String>() {
				
				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					if (!CustomTextAreaWithNoWhiteSpaces.this.isReadOnly()) {
//						String nameTextArea = event.getValue().replaceAll(" ", "").trim();
						String nameTextArea = event.getValue().trim();
						CustomTextAreaWithNoWhiteSpaces.this.setText(nameTextArea);
						
						if (nameTextArea.length() >= maxLength) {
							String subStringText = nameTextArea.substring(0, maxLength);
							CustomTextAreaWithNoWhiteSpaces.this.setValue(subStringText);
							setCursorPos(maxLength);
						} else {
							CustomTextAreaWithNoWhiteSpaces.this.setValue(nameTextArea);
							setCursorPos(nameTextArea.length());
						}
						resetMessageDisplay();
						setIsPageDirty(true);
					}
				}
			});
		}
		
		*//**
		 * Description: Takes the browser event.
		 *
		 * @param event
		 *            declared.
		 *//*
		@Override
		public void onBrowserEvent(Event event) {
			
			String nameTextArea;
			try {
				nameTextArea = CustomTextAreaWithNoWhiteSpaces.this.getText();
			} catch (Exception e) {
				nameTextArea = "";
			}
			
			//Checking on Click Events
			if (event.getTypeInt() == Event.ONCLICK) {
				NativeEvent nativeEvent = Document.get().createClickEvent(1, 0, 0,
						0, 0, false, false, false, false);
				DomEvent.fireNativeEvent(nativeEvent, this);
			}
			
			// Checking for paste event
			if (event.getTypeInt() == Event.ONPASTE) {
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					@Override
					public void execute() {
						ValueChangeEvent.fire(CustomTextAreaWithNoWhiteSpaces.this,
								CustomTextAreaWithNoWhiteSpaces.this.getText());
					}
				});
				return;
			}
			// Checking for key Down event.
			if ((event.getTypeInt() == Event.ONKEYDOWN) && (nameTextArea.length() > maxLength)
					&& (event.getKeyCode() != KeyCodes.KEY_LEFT) && (event.getKeyCode() != KeyCodes.KEY_TAB)
					&& (event.getKeyCode() != KeyCodes.KEY_RIGHT) && (event.getKeyCode() != KeyCodes.KEY_DELETE)
					&& (event.getKeyCode() != KeyCodes.KEY_BACKSPACE) && (event.getKeyCode() != KeyCodes.KEY_SHIFT)
					&& (event.getKeyCode() != KeyCodes.KEY_CTRL)) {
				event.preventDefault();
			} 
			else if ((event.getTypeInt() == Event.ONKEYDOWN) && (event.getKeyCode() == KeyCodes.KEY_SPACE)) {
				event.preventDefault();
			} 
			else if ((event.getTypeInt() == Event.ONKEYDOWN) && (nameTextArea.length() <= maxLength)) {
				if ((event.getKeyCode() != KeyCodes.KEY_LEFT) && (event.getKeyCode() != KeyCodes.KEY_TAB)
						&& (event.getKeyCode() != KeyCodes.KEY_RIGHT) && (event.getKeyCode() != KeyCodes.KEY_SHIFT)) {
					if (!event.getCtrlKey()) {
						Scheduler.get().scheduleDeferred(new ScheduledCommand() {
							@Override
							public void execute() {
								ValueChangeEvent.fire(CustomTextAreaWithNoWhiteSpaces.this,
										CustomTextAreaWithNoWhiteSpaces.this.getText());
							}
						});
					}
				}
			}
		}
		
		*//**
		 * Getter for maximum length.
		 * 
		 * @return - int.
		 *//*
		public int getMaxLength() {
			return maxLength;
		}
		
		*//**
		 * Setter for maximum length.
		 *
		 * @param maxLength
		 *            the new max length
		 *//*
		@Override
		public void setMaxLength(int maxLength) {
			this.maxLength = maxLength;
		}
	}
	*/
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * resetMessageDisplay()
	 */
	@Override
	public void resetMessageDisplay() {
		getSuccessMessageAlert().clearAlert();
		getErrorMessageAlert().clearAlert();
		getWarningConfirmationMessageAlert().clearAlert();
		getGlobalWarningConfirmationMessageAlert().clearAlert();
		hideAceEditorAutoCompletePopUp();
		
	}
	@Override
	public void hideAceEditorAutoCompletePopUp() {
		defineAceEditor.detach();
		parameterAceEditor.detach();
		functionBodyAceEditor.detach();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * setErrorMessageAlert(mat.client.shared.ErrorMessageAlert)
	 */
	@Override
	public void setErrorMessageAlert(ErrorMessageAlert errorMessageAlert) {
		this.errorMessageAlert = errorMessageAlert;
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getWarningConfirmationMessageAlert()
	 */
	@Override
	public WarningConfirmationMessageAlert getWarningConfirmationMessageAlert() {
		return warningConfirmationMessageAlert;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * setWarningConfirmationMessageAlert(mat.client.shared.MessageAlert)
	 */
	@Override
	public void setWarningConfirmationMessageAlert(WarningConfirmationMessageAlert warningMessageAlert) {
		warningConfirmationMessageAlert = warningMessageAlert;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getGlobalWarningConfirmationMessageAlert()
	 */
	@Override
	public WarningConfirmationMessageAlert getGlobalWarningConfirmationMessageAlert() {
		return globalWarningConfirmationMessageAlert;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * setGlobalWarningConfirmationMessageAlert(mat.client.shared.MessageAlert)
	 */
	@Override
	public void setGlobalWarningConfirmationMessageAlert(WarningConfirmationMessageAlert globalWarningMessageAlert) {
		globalWarningConfirmationMessageAlert = globalWarningMessageAlert;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getAppliedQdmList()
	 */
	@Override
	public List<QualityDataSetDTO> getAppliedQdmList() {
		return appliedQdmList;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * setAppliedQdmList(java.util.List)
	 */
	@Override
	public void setAppliedQdmList(List<QualityDataSetDTO> appliedQdmList) {
		this.appliedQdmList = appliedQdmList;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#getDefineInfoButton()
	 */
	@Override
	public Button getDefineInfoButton(){
		return defineButtonBar.getInfoButton();
	}
	
	@Override
	public Button getParamInfoButton(){
		return parameterButtonBar.getInfoButton();
	}
	
	@Override
	public Button getFuncInfoButton(){
		return functionButtonBar.getInfoButton();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#getArgumentTextArea()
	 */
	@Override
	public TextBox getArgumentTextArea(){
		return new TextBox();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#showUnsavedChangesWarning()
	 */
	@Override
	public void showUnsavedChangesWarning() {
		getErrorMessageAlert().clearAlert();
		getSuccessMessageAlert().clearAlert();
		getGlobalWarningConfirmationMessageAlert().clearAlert();
		getWarningConfirmationMessageAlert().createAlert();
		getWarningConfirmationMessageAlert().getWarningConfirmationYesButton().setFocus(true);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#showGlobalUnsavedChangesWarning()
	 */
	@Override
	public void showGlobalUnsavedChangesWarning() {
		getErrorMessageAlert().clearAlert();
		getSuccessMessageAlert().clearAlert();
		getWarningConfirmationMessageAlert().clearAlert();
		getGlobalWarningConfirmationMessageAlert().createAlert();
		getGlobalWarningConfirmationMessageAlert().getWarningConfirmationYesButton().setFocus(true);
	}
	
	@Override
	public Button getDefineTimingExpButton(){
		return getDefineButtonBar().getTimingExpButton();
	}
	
	@Override
	public Button getFuncTimingExpButton(){
		return getFunctionButtonBar().getTimingExpButton();
	}
	
}
