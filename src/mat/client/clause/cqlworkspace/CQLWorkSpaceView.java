package mat.client.clause.cqlworkspace;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.LabelType;
import org.gwtbootstrap3.client.ui.constants.Pull;
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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBox;
//import org.gwtbootstrap3.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;
import mat.client.shared.CQLButtonToolBar;
import mat.client.shared.CQLSuggestOracle;
import mat.client.shared.DeleteConfirmationMessageAlert;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MatContext;
import mat.client.shared.MessageAlert;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageAlert;
import mat.client.shared.WarningConfirmationMessageAlert;
import mat.client.shared.WarningMessageAlert;
import mat.client.util.MatTextBox;
import mat.model.clause.QDSAttributes;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLLibraryModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.shared.GetUsedCQLArtifactsResult;

// TODO: Auto-generated Javadoc
/**
 * The Class CQLWorkSpaceView.
 */
/**
 * @author jnarang
 *
 */
public class CQLWorkSpaceView implements CQLWorkSpacePresenter.ViewDisplay {

	/** The available qds attribute list. */
	private List<QDSAttributes> availableQDSAttributeList;

	/** The main horizontal panel. */
	HorizontalPanel mainHPPanel = new HorizontalPanel();

	/** The main horizontal panel. */
	private VerticalPanel mainPanel = new VerticalPanel();

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

	/** The applied QDM Element anchorItem. */
	private AnchorListItem appliedQDM = new AnchorListItem();

	/** The general information. */
	private AnchorListItem generalInformation;

	/** The includes section. */
	private AnchorListItem includesLibrary;

	/** The parameter library. */
	private AnchorListItem parameterLibrary;

	/** The definition library. */
	private AnchorListItem definitionLibrary;

	/** The function library. */
	private AnchorListItem functionLibrary;

	/** The CQL success message. */
	private MessageAlert successMessageAlert = new SuccessMessageAlert();

	/** The CQL warning message */
	private MessageAlert warningMessageAlert = new WarningMessageAlert();

	/** The CQL error message. */
	private MessageAlert errorMessageAlert = new ErrorMessageAlert();

	/** The CQL warning message. */
	private WarningConfirmationMessageAlert warningConfirmationMessageAlert = new WarningConfirmationMessageAlert();

	/** The delete confirmation box */
	DeleteConfirmationDialogBox deleteConfirmationDialogBox = new DeleteConfirmationDialogBox();

	/** The CQL warning message. */
	private WarningConfirmationMessageAlert globalWarningConfirmationMessageAlert = new WarningConfirmationMessageAlert();

	/** The cql ace editor. */
	private AceEditor cqlAceEditor = new AceEditor();

	/**
	 * SuggestBox searchSuggestTextBox.
	 */
	private SuggestBox searchSuggestTextBox;

	/**
	 * TreeMap parameterNameMap.
	 */
	private Map<String, String> parameterNameMap = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
	/**
	 * HashMap parameterMap.
	 */
	private HashMap<String, CQLParameter> parameterMap = new HashMap<String, CQLParameter>();

	/**
	 * ListBox parameterNameListBox.
	 */
	private ListBox parameterNameListBox;

	/** The view alias list. */
	// private List<CQLLibraryModel> viewAliasList = new
	// ArrayList<CQLLibraryModel>();

	/** The view parameter list. */
	private List<CQLParameter> viewParameterList = new ArrayList<CQLParameter>();

	private ListBox includesNameListbox;

	/**
	 * ListBox defineNameListBox.
	 */
	private ListBox defineNameListBox;

	/**
	 * ListBox funcNameListBox.
	 */
	private ListBox funcNameListBox;

	/**
	 * SuggestBox searchSuggestDefineTextBox.
	 */
	private SuggestBox searchSuggestDefineTextBox;

	/**
	 * SuggestBox searchSuggestFuncTextBox.
	 */
	private SuggestBox searchSuggestIncludeTextBox;

	/**
	 * SuggestBox searchSuggestFuncTextBox.
	 */
	private SuggestBox searchSuggestFuncTextBox;

	/**
	 * List viewIncludes.
	 */
	private List<CQLLibraryModel> viewIncludes = new ArrayList<CQLLibraryModel>();

	/**
	 * List viewDefinitions.
	 */
	private List<CQLDefinition> viewDefinitions = new ArrayList<CQLDefinition>();

	/** The applied qdm list. */
	private List<CQLQualityDataSetDTO> appliedQdmList = new ArrayList<CQLQualityDataSetDTO>();

	/** The applied qdm to show in Table list. */
	private List<CQLQualityDataSetDTO> appliedQdmTableList = new ArrayList<CQLQualityDataSetDTO>();

	private List<CQLLibraryDataSetObject> includeLibraryList = new ArrayList<CQLLibraryDataSetObject>();

	/**
	 * List viewFunctions.
	 */
	private List<CQLFunctions> viewFunctions = new ArrayList<CQLFunctions>();

	private List<CQLIncludeLibrary> viewIncludeLibrarys = new ArrayList<CQLIncludeLibrary>();
	/**
	 * TreeMap defineNameMap.
	 */
	private Map<String, String> defineNameMap = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);

	/**
	 * TreeMap funcNameMap.
	 */
	private Map<String, String> funcNameMap = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);

	private Map<String, String> includeLibraryNameMap = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
	/**
	 * HashMap definitionMap.
	 */
	private HashMap<String, CQLDefinition> definitionMap = new HashMap<String, CQLDefinition>();

	/**
	 * HashMap definitionMap.
	 */
	private HashMap<String, CQLFunctions> functionMap = new HashMap<String, CQLFunctions>();

	private HashMap<String, CQLIncludeLibrary> includeLibraryMap = new HashMap<String, CQLIncludeLibrary>();

	/** The define badge. */
	private Badge includesBadge = new Badge();

	/** The param badge. */
	private Badge paramBadge = new Badge();

	/** The define badge. */
	private Badge defineBadge = new Badge();

	/** The Function badge. */
	private Badge functionBadge = new Badge();

	/** The includes label. */
	private Label includesLabel = new Label("Includes");

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

	/** The Includes Collapse. */
	PanelCollapse includesCollapse = new PanelCollapse();

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

	private String currentSelectedIncLibraryObjId = null;

	/** The dirty flag for page. */
	private Boolean isPageDirty = false;

	/** The is double click. */
	private Boolean isDoubleClick = false;

	/** The is nav bar click. */
	private Boolean isNavBarClick = false;

	/** The vp. */
	VerticalPanel vp = new VerticalPanel();

	DeleteConfirmationMessageAlert deleteConfirmationMessgeAlert = new DeleteConfirmationMessageAlert();

	private GetUsedCQLArtifactsResult usedCqlArtifacts;

	private CQLQDMAppliedView qdmView;

	private CQLIncludeLibraryView inclView;

	private CQLGeneralInformationView generalInformationView;
	private CQLParametersView cqlParametersView;
	private CQlDefinitionsView cqlDefinitionsView;

	private CQLFunctionsView cqlFunctionsView;
	// private AnchorListItem includeLibrary;

	/**
	 * Instantiates a new CQL work space view.
	 */
	public CQLWorkSpaceView() {
		generalInformationView = new CQLGeneralInformationView();
		cqlParametersView = new CQLParametersView();
		cqlDefinitionsView = new CQlDefinitionsView();
		cqlFunctionsView = new CQLFunctionsView();
		qdmView = new CQLQDMAppliedView();
		inclView = new CQLIncludeLibraryView();
		cqlAceEditor.startEditor();
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
		includesCollapse = createIncludesCollapsablePanel();
		paramCollapse = createParameterCollapsablePanel();
		defineCollapse = createDefineCollapsablePanel();
		functionCollapse = createFunctionCollapsablePanel();
		buildLeftHandNavNar();

		buildGeneralInformation();
		mainFlowPanel.setWidth("700px");
		mainPanel.getElement().setId("CQLWorkspaceView.containerPanel");
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
		unsetEachSectionSelectedObject();
		mainFlowPanel.clear();

		VerticalPanel cqlViewVP = new VerticalPanel();
		SimplePanel cqlViewFP = new SimplePanel();

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
		cqlViewVP.add(new SpacerWidget());
		cqlViewVP.add(new SpacerWidget());
		// cqlViewVP.add(warningConfirmationMessageAlert);
		// cqlViewVP.add(new SpacerWidget());
		// cqlViewVP.add(successMessageAlert);
		// cqlViewVP.add(new SpacerWidget());
		cqlViewVP.add(viewCQlFileLabel);
		cqlViewVP.add(new SpacerWidget());
		cqlViewVP.add(new SpacerWidget());
		cqlViewVP.add(cqlAceEditor);
		cqlViewFP.add(cqlViewVP);
		cqlViewFP.setStyleName("cqlRightContainer");
		cqlViewFP.setWidth("700px");
		cqlViewFP.setStyleName("marginLeft15px");
		mainFlowPanel.add(cqlViewFP);

	}

	/**
	 * Adds the parameter event handler.
	 */
	private void addParameterEventHandler() {

		getParameterNameListBox().addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				cqlParametersView.getParameterAceEditor().clearAnnotations();
				cqlParametersView.getParameterAceEditor().removeAllMarkers();
				cqlParametersView.getParameterAceEditor().redisplay();
				System.out.println("In addParameterEventHandler on DoubleClick isPageDirty = " + getIsPageDirty()
						+ " selectedIndex = " + getParameterNameListBox().getSelectedIndex());
				setIsDoubleClick(true);
				setIsNavBarClick(false);
				if (getIsPageDirty()) {
					showUnsavedChangesWarning();
				} else {
					int selectedIndex = getParameterNameListBox().getSelectedIndex();
					if (selectedIndex != -1) {
						final String selectedParamID = getParameterNameListBox().getValue(selectedIndex);
						currentSelectedParamerterObjId = selectedParamID;
						if (getParameterMap().get(selectedParamID) != null) {
							getParameterNameTxtArea()
									.setText(getParameterMap().get(selectedParamID).getParameterName());
							getParameterAceEditor().setText(getParameterMap().get(selectedParamID).getParameterLogic());
							System.out.println("In Parameter DoubleClickHandler, doing setText()");
							// disable parameterName and Logic fields for
							// Default Parameter
							boolean isReadOnly = getParameterMap().get(selectedParamID).isReadOnly();
							getParameterButtonBar().getDeleteButton().setTitle("Delete");

							if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
								setParameterWidgetReadOnly(!isReadOnly);
							}

							// load most recent used cql artifacts
							MatContext.get().getMeasureService().getUsedCQLArtifacts(
									MatContext.get().getCurrentMeasureId(),
									new AsyncCallback<GetUsedCQLArtifactsResult>() {

										@Override
										public void onFailure(Throwable caught) {
											Window.alert(
													MatContext.get().getMessageDelegate().getGenericErrorMessage());
										}

										@Override
										public void onSuccess(GetUsedCQLArtifactsResult result) {
											if (result.getUsedCQLParameters().contains(
													getParameterMap().get(selectedParamID).getParameterName())) {
												getParameterButtonBar().getDeleteButton().setEnabled(false);
											}
										}

									});
						}
					}

					successMessageAlert.clearAlert();
					errorMessageAlert.clearAlert();
					warningMessageAlert.clearAlert();

				}

			}
		});
	}

	/**
	 * Adds the define event handlers.
	 */
	private void addDefineEventHandlers() {
		getDefineNameListBox().addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				cqlDefinitionsView.getDefineAceEditor().clearAnnotations();
				cqlDefinitionsView.getDefineAceEditor().removeAllMarkers();
				cqlDefinitionsView.getDefineAceEditor().redisplay();
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
							// disable definitionName and fields for
							// Supplemental data definitions
							boolean isReadOnly = getDefinitionMap().get(selectedDefinitionID).isSupplDataElement();
							getDefineButtonBar().getDeleteButton().setTitle("Delete");

							if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
								setDefinitionWidgetReadOnly(!isReadOnly);
							}

							// load most recent used cql artifacts
							MatContext.get().getMeasureService().getUsedCQLArtifacts(
									MatContext.get().getCurrentMeasureId(),
									new AsyncCallback<GetUsedCQLArtifactsResult>() {

										@Override
										public void onFailure(Throwable caught) {
											Window.alert(
													MatContext.get().getMessageDelegate().getGenericErrorMessage());
										}

										@Override
										public void onSuccess(GetUsedCQLArtifactsResult result) {
											if (result.getUsedCQLDefinitions().contains(
													getDefinitionMap().get(selectedDefinitionID).getDefinitionName())) {
												getDefineButtonBar().getDeleteButton().setEnabled(false);

											}
										}

									});
						}
					}

					successMessageAlert.clearAlert();
					errorMessageAlert.clearAlert();
					warningMessageAlert.clearAlert();
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
				cqlFunctionsView.getFunctionBodyAceEditor().clearAnnotations();
				cqlFunctionsView.getFunctionBodyAceEditor().removeAllMarkers();
				cqlFunctionsView.getFunctionBodyAceEditor().redisplay();
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
								cqlFunctionsView.getContextFuncPATRadioBtn().setValue(true);
								cqlFunctionsView.getContextFuncPOPRadioBtn().setValue(false);
							} else {
								cqlFunctionsView.getContextFuncPOPRadioBtn().setValue(true);
								cqlFunctionsView.getContextFuncPATRadioBtn().setValue(false);
							}

							getFunctionButtonBar().getDeleteButton().setEnabled(true);

							// load most recent used cql artifacts
							MatContext.get().getMeasureService().getUsedCQLArtifacts(
									MatContext.get().getCurrentMeasureId(),
									new AsyncCallback<GetUsedCQLArtifactsResult>() {

										@Override
										public void onFailure(Throwable caught) {
											Window.alert(
													MatContext.get().getMessageDelegate().getGenericErrorMessage());
										}

										@Override
										public void onSuccess(GetUsedCQLArtifactsResult result) {
											if (result.getUsedCQLFunctionss().contains(
													getFunctionMap().get(selectedFunctionId).getFunctionName())) {
												getFunctionButtonBar().getDeleteButton().setEnabled(false);

											}
										}

									});
						}
					}
					if (currentSelectedFunctionObjId != null) {
						CQLFunctions selectedFunction = getFunctionMap().get(currentSelectedFunctionObjId);
						if (selectedFunction.getArgumentList() != null) {
							cqlFunctionsView.getFunctionArgumentList().clear();
							cqlFunctionsView.getFunctionArgumentList().addAll(selectedFunction.getArgumentList());
						} else {
							cqlFunctionsView.getFunctionArgumentList().clear();
						}
					}
				}
				cqlFunctionsView.createAddArgumentViewForFunctions(cqlFunctionsView.getFunctionArgumentList());
				successMessageAlert.clearAlert();
				errorMessageAlert.clearAlert();
				warningMessageAlert.clearAlert();
			}
		});
	}

	private void addIncludeLibraryHandlers() {

		getIncludesNameListBox().addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {

				// System.out.println("In addParameterEventHandler on
				// DoubleClick isPageDirty = " + getIsPageDirty() + "
				// selectedIndex = " +
				// getParameterNameListBox().getSelectedIndex());
				setIsDoubleClick(true);
				setIsNavBarClick(false);
				
				if (getIsPageDirty()) {
					showUnsavedChangesWarning();
				} else {
					int selectedIndex = getIncludesNameListBox().getSelectedIndex();
					if (selectedIndex != -1) {
						final String selectedIncludeLibraryID = getIncludesNameListBox().getValue(selectedIndex);
						currentSelectedIncLibraryObjId = selectedIncludeLibraryID;
						if (getIncludeLibraryMap().get(selectedIncludeLibraryID) != null) {

							MatContext.get().getCQLLibraryService().findCQLLibraryByID(
									getIncludeLibraryMap().get(selectedIncludeLibraryID).getCqlLibraryId(),
									new AsyncCallback<CQLLibraryDataSetObject>() {

										@Override
										public void onSuccess(CQLLibraryDataSetObject result) {
											if (result != null) {
												getIncludeView().buildIncludesReadOnlyView();
												
												getAliasNameTxtArea().setText(getIncludeLibraryMap()
														.get(selectedIncludeLibraryID).getAliasName());
												getViewCQLEditor().setText(result.getCqlText());
												getIncludeView().getOwnerNameTextBox().setText(getOwnerName(result));
												getIncludeView().getCqlLibraryNameTextBox()
														.setText(result.getCqlName());
											}
										}

										@Override
										public void onFailure(Throwable caught) {

										}
									});

							inclView.setSelectedObject(
									getIncludeLibraryMap().get(selectedIncludeLibraryID).getCqlLibraryId());
							inclView.setIncludedList(getIncludedList(getIncludeLibraryMap()));
							inclView.getSelectedObjectList().clear();
						}
					}
					resetMessageDisplay();

				}

			}
		});
	}

	@Override
	public List<String> getIncludedList(Map<String, CQLIncludeLibrary> includeMap) {
		List<String> list = new ArrayList<String>();
		for (Map.Entry<String, CQLIncludeLibrary> entry : includeMap.entrySet()) {
			list.add(entry.getValue().getCqlLibraryId());
		}
		return list;
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
		updateNewSuggestParamOracle();
		if (getViewParameterList().size() < 10) {
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

		updateNewSuggestDefineOracle();
		if (getViewDefinitions().size() < 10) {
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
		// functionArgNameMap.clear();
		for (CQLFunctions function : viewFunctions) {
			funcNameMap.put(function.getId(), function.getFunctionName());
			functionMap.put(function.getId(), function);
			/*
			 * if (function.getArgumentList() != null) { for
			 * (CQLFunctionArgument argument : function.getArgumentList()) {
			 * functionArgNameMap.put(argument.getArgumentName().toLowerCase(),
			 * argument); } }
			 */
		}
		updateNewSuggestFuncOracle();
		if (viewFunctions.size() < 10) {
			functionBadge.setText("0" + viewFunctions.size());
		} else {
			functionBadge.setText("" + viewFunctions.size());
		}
	}

	@Override
	public void udpateIncludeLibraryMap() {
		includeLibraryMap.clear();
		includeLibraryNameMap.clear();
		for (CQLIncludeLibrary incLibrary : viewIncludeLibrarys) {
			includeLibraryNameMap.put(incLibrary.getId(), incLibrary.getAliasName());
			includeLibraryMap.put(incLibrary.getId(), incLibrary);
		}
		updateNewSuggestIncLibOracle();
		if (viewIncludeLibrarys.size() < 10) {
			includesBadge.setText("0" + viewIncludeLibrarys.size());
		} else {
			includesBadge.setText("" + viewIncludeLibrarys.size());
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
		unsetEachSectionSelectedObject();
		mainFlowPanel.clear();
		mainFlowPanel.add(generalInformationView.getView());

	}

	

	@Override
	public void buildAppliedQDM() {
		qdmView.resetVSACValueSetWidget();
		qdmView.setWidgetToDefault();
		resetMessageDisplay();

		unsetEachSectionSelectedObject();
		VerticalPanel appliedQDMTopPanel = new VerticalPanel();

		appliedQDMTopPanel.add(qdmView.asWidget());

		VerticalPanel vp = new VerticalPanel();
		vp.setStyleName("cqlRightContainer");
		vp.setWidth("700px");
		appliedQDMTopPanel.setWidth("700px");
		appliedQDMTopPanel.setStyleName("marginLeft15px");
		vp.add(appliedQDMTopPanel);

		mainFlowPanel.clear();
		mainFlowPanel.add(vp);

	}

	@Override
	public void buildIncludesView() {
		unsetEachSectionSelectedObject();
		mainFlowPanel.clear();
		resetMessageDisplay();

		VerticalPanel includesTopPanel = new VerticalPanel();
		inclView.resetToDefault();
		// building searchWidget for adding new aliasName
		inclView.buildAddNewAliasView();

		includesTopPanel.add(inclView.asWidget());

		VerticalPanel vp = new VerticalPanel();
		vp.setStyleName("cqlRightContainer");
		vp.setWidth("700px");
		includesTopPanel.setWidth("700px");
		includesTopPanel.setStyleName("marginLeft15px");
		vp.add(includesTopPanel);
		addIncludeLibraryHandlers();
		mainFlowPanel.add(vp);
	}

	/**
	 * Method to create Right Hand side Nav bar in CQL Workspace.
	 */
	private void buildLeftHandNavNar() {
		unsetEachSectionSelectedObject();
		rightHandNavPanel.clear();
		NavPills navPills = new NavPills();
		navPills.setStacked(true);

		generalInformation = new AnchorListItem();
		includesLibrary = new AnchorListItem();
		appliedQDM = new AnchorListItem();
		parameterLibrary = new AnchorListItem();
		definitionLibrary = new AnchorListItem();
		functionLibrary = new AnchorListItem();
		viewCQL = new AnchorListItem();

		generalInformation.setIcon(IconType.INFO);
		generalInformation.setText("General Information");
		generalInformation.setTitle("General Information");
		generalInformation.setActive(true);

		includesLibrary.setIcon(IconType.PENCIL);
		includesLibrary.setTitle("Includes");
		includesBadge.setText("0" + viewIncludeLibrarys.size());
		Anchor includesAnchor = (Anchor) (includesLibrary.getWidget(0));
		// Double Click causing issues.So Event is not propogated
		includesAnchor.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				// TODO Auto-generated method stub
				event.stopPropagation();
			}
		});
		includesLabel.setStyleName("transparentLabel");
		includesAnchor.add(includesLabel);
		includesBadge.setPull(Pull.RIGHT);
		includesBadge.setMarginLeft(52);
		includesAnchor.add(includesBadge);
		includesAnchor.setDataParent("#navGroup");
		includesLibrary.setDataToggle(Toggle.COLLAPSE);
		includesLibrary.setHref("#collapseIncludes");

		includesLibrary.add(includesCollapse);

		appliedQDM.setIcon(IconType.PENCIL);
		appliedQDM.setText("Value Sets/Codes");
		appliedQDM.setTitle("Value Sets/Codes");
		appliedQDM.setActive(false);

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
		// paramBadge.setMarginLeft(45);
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
		// defineBadge.setMarginLeft(52);
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

		// functionBadge.setMarginLeft(57);
		funcAnchor.add(functionBadge);
		funcAnchor.setDataParent("#navGroup");
		functionLibrary.setDataToggle(Toggle.COLLAPSE);
		functionLibrary.setHref("#collapseFunction");

		functionLibrary.add(functionCollapse);

		viewCQL.setIcon(IconType.BOOK);
		viewCQL.setText("View CQL");
		viewCQL.setTitle("View CQL");

		navPills.add(generalInformation);
		navPills.add(includesLibrary);
		navPills.add(appliedQDM);
		navPills.add(parameterLibrary);

		navPills.add(definitionLibrary);
		navPills.add(functionLibrary);
		navPills.add(viewCQL);

		navPills.setWidth("200px");

		messagePanel.add(successMessageAlert);
		messagePanel.add(warningMessageAlert);
		messagePanel.add(errorMessageAlert);
		messagePanel.add(warningConfirmationMessageAlert);
		messagePanel.add(globalWarningConfirmationMessageAlert);
		messagePanel.add(deleteConfirmationMessgeAlert);

		// rightHandNavPanel.add(messagePanel);
		rightHandNavPanel.add(navPills);
	}

	/**
	 * Creates the includes collapsable panel.
	 *
	 * @return the panel collapse
	 */
	private PanelCollapse createIncludesCollapsablePanel() {

		includesCollapse.setId("collapseIncludes");

		PanelBody includesCollapseBody = new PanelBody();

		HorizontalPanel includesFP = new HorizontalPanel();

		VerticalPanel rightVerticalPanel = new VerticalPanel();
		rightVerticalPanel.setSpacing(10);

		rightVerticalPanel.getElement().setId("rhsVerticalPanel_VerticalPanelIncludes");
		rightVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		Label includesLibraryLabel = new Label("Includes Library");
		searchSuggestIncludeTextBox = new SuggestBox(getSuggestOracle(includeLibraryNameMap.values()));
		searchSuggestIncludeTextBox.setWidth("180px");
		searchSuggestIncludeTextBox.setText("Search");
		searchSuggestIncludeTextBox.getElement().setId("searchTextBox_TextBoxIncludesLib");

		searchSuggestIncludeTextBox.getValueBox().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if ("Search".equals(searchSuggestIncludeTextBox.getText())) {
					searchSuggestIncludeTextBox.setText("");
				}
			}
		});

		includesNameListbox = new ListBox();
		includesNameListbox.setWidth("180px");
		includesNameListbox.setVisibleItemCount(10);
		includesNameListbox.getElement().setAttribute("id", "includesListBox");

		clearAndAddAliasNamesToListBox();

		addSuggestHandler(searchSuggestIncludeTextBox, includesNameListbox);
		addListBoxHandler(includesNameListbox, searchSuggestIncludeTextBox);

		searchSuggestIncludeTextBox.getElement().setAttribute("id", "searchSuggestIncludesTextBox");
		rightVerticalPanel.add(searchSuggestIncludeTextBox);
		searchSuggestIncludeTextBox.getElement().setId("searchSuggesIncludestTextBox_SuggestBox");
		rightVerticalPanel.add(includesNameListbox);
		includesNameListbox.getElement().setId("includesNameListBox_ListBox");
		rightVerticalPanel.setCellHorizontalAlignment(includesLibraryLabel, HasHorizontalAlignment.ALIGN_LEFT);
		includesFP.add(rightVerticalPanel);
		includesCollapseBody.add(includesFP);

		includesCollapse.add(includesCollapseBody);
		return includesCollapse;

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
		searchSuggestTextBox = new SuggestBox(getSuggestOracle(parameterNameMap.values()));
		// updateSuggestOracle();
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
		searchSuggestDefineTextBox = new SuggestBox(getSuggestOracle(defineNameMap.values()));
		// updateNewSuggestDefineOracle();
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
		searchSuggestFuncTextBox = new SuggestBox(getSuggestOracle(funcNameMap.values()));
		// updateNewSuggestFuncOracle();
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

	private SuggestOracle getSuggestOracle(Collection<String> values) {
		return new CQLSuggestOracle(values);
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
	@Override
	public void buildParameterLibraryView() {
		unsetEachSectionSelectedObject();
		// setIsPageDirty(false);
		mainFlowPanel.clear();
		addParameterEventHandler();
		mainFlowPanel.add(cqlParametersView.getView());

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

	/**
	 * Clear and add alias names to list box.
	 */
	@Override
	public void clearAndAddAliasNamesToListBox() {
		if (includesNameListbox != null) {
			includesNameListbox.clear();
			viewIncludeLibrarys = sortAliasList(viewIncludeLibrarys);
			for (CQLIncludeLibrary incl : viewIncludeLibrarys) {
				includesNameListbox.addItem(incl.getAliasName(), incl.getId());
			}
			// Set tooltips for each element in listbox
			SelectElement selectElement = SelectElement.as(includesNameListbox.getElement());
			com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement.getOptions();
			for (int i = 0; i < options.getLength(); i++) {
				String title = options.getItem(i).getText();
				OptionElement optionElement = options.getItem(i);
				optionElement.setTitle(title);
			}
		}
	}

	private List<CQLIncludeLibrary> sortAliasList(List<CQLIncludeLibrary> viewAliasList) {
		Collections.sort(viewAliasList, new Comparator<CQLIncludeLibrary>() {
			@Override
			public int compare(final CQLIncludeLibrary object1, final CQLIncludeLibrary object2) {
				return object1.getAliasName().compareToIgnoreCase(object2.getAliasName());
			}
		});
		return viewAliasList;
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
			viewParameterList = sortParamList(viewParameterList);
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

	private List<CQLParameter> sortParamList(List<CQLParameter> viewParamList) {
		Collections.sort(viewParamList, new Comparator<CQLParameter>() {
			@Override
			public int compare(final CQLParameter object1, final CQLParameter object2) {
				return object1.getParameterName().compareToIgnoreCase(object2.getParameterName());
			}
		});
		return viewParamList;
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
	@Override
	public void buildDefinitionLibraryView() {
		unsetEachSectionSelectedObject();
		mainFlowPanel.clear();
		addDefineEventHandlers();
		mainFlowPanel.add(cqlDefinitionsView.getView());
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

	/**
	 * Update suggest param oracle.
	 */
	public void updateNewSuggestParamOracle() {
		if (searchSuggestDefineTextBox != null) {
			CQLSuggestOracle cqlSuggestOracle = new CQLSuggestOracle(parameterNameMap.values());
		}
	}

	/**
	 * Update suggest define oracle.
	 */
	public void updateNewSuggestDefineOracle() {
		if (searchSuggestDefineTextBox != null) {
			CQLSuggestOracle cqlSuggestOracle = new CQLSuggestOracle(defineNameMap.values());
		}
	}

	/**
	 * Update suggest func oracle.
	 */
	public void updateNewSuggestFuncOracle() {
		if (searchSuggestFuncTextBox != null) {
			CQLSuggestOracle cqlSuggestOracle = new CQLSuggestOracle(funcNameMap.values());
		}
	}

	public void updateNewSuggestIncLibOracle() {
		if (searchSuggestIncludeTextBox != null) {
			CQLSuggestOracle cqlSuggestOracle = new CQLSuggestOracle(includeLibraryNameMap.values());
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
			viewDefinitions = sortDefinitionNames(viewDefinitions);
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

	private List<CQLDefinition> sortDefinitionNames(List<CQLDefinition> viewDef) {
		Collections.sort(viewDef, new Comparator<CQLDefinition>() {
			@Override
			public int compare(final CQLDefinition object1, final CQLDefinition object2) {
				return object1.getDefinitionName().compareToIgnoreCase(object2.getDefinitionName());
			}
		});
		return viewDef;
	}

	/**
	 * Clear and add functions names to list box.
	 */
	@Override
	public void clearAndAddFunctionsNamesToListBox() {
		if (funcNameListBox != null) {
			funcNameListBox.clear();
			// sort functions
			viewFunctions = sortFunctionNames(viewFunctions);
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

	private List<CQLFunctions> sortFunctionNames(List<CQLFunctions> viewFunc) {
		Collections.sort(viewFunc, new Comparator<CQLFunctions>() {
			@Override
			public int compare(final CQLFunctions object1, final CQLFunctions object2) {
				return object1.getFunctionName().compareToIgnoreCase(object2.getFunctionName());
			}
		});
		return viewFunc;
	}

	@Override
	public void clearAndAddIncludesNamesToListBox() {
		if (includesNameListbox != null) {
			includesNameListbox.clear();
			// sort Include Librarys
			viewIncludeLibrarys = sortIncludesNames(viewIncludeLibrarys);
			for (CQLIncludeLibrary incLib : viewIncludeLibrarys) {
				includesNameListbox.addItem(incLib.getAliasName(), incLib.getId());
			}
			// Set tooltips for each element in listbox
			SelectElement selectElement = SelectElement.as(includesNameListbox.getElement());
			com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement.getOptions();
			for (int i = 0; i < options.getLength(); i++) {
				String title = options.getItem(i).getText();
				OptionElement optionElement = options.getItem(i);
				optionElement.setTitle(title);
			}
		}
	}

	private List<CQLIncludeLibrary> sortIncludesNames(List<CQLIncludeLibrary> viewInclib) {
		Collections.sort(viewInclib, new Comparator<CQLIncludeLibrary>() {
			@Override
			public int compare(final CQLIncludeLibrary object1, final CQLIncludeLibrary object2) {
				return object1.getAliasName().compareToIgnoreCase(object2.getAliasName());
			}
		});
		return viewInclib;
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
	@Override
	public void buildFunctionLibraryView() {
		unsetEachSectionSelectedObject();
		mainFlowPanel.clear();

		addFuncEventHandlers();
		mainFlowPanel.add(cqlFunctionsView.getView());
	}

	/**
	 * Creates the add argument view for functions.
	 *
	 * @param argumentList
	 *            the argument list
	 */
	@Override
	public void createAddArgumentViewForFunctions(List<CQLFunctionArgument> argumentList) {
		cqlFunctionsView.createAddArgumentViewForFunctions(argumentList);
	}

	/**
	 * Reset All components to default state.
	 */
	private void resetAll() {
		rightHandNavPanel.clear();
		mainFlowPanel.clear();
		getIncludeView().setAliasNameTxtArea("");
		System.out.println(" in resetAll doing setText");
		cqlAceEditor.setText("");

		viewIncludeLibrarys.clear();
		viewParameterList.clear();
		viewDefinitions.clear();
		viewFunctions.clear();
		viewIncludeLibrarys.clear();

		if (includesCollapse != null) {
			includesCollapse.clear();
		}
		if (paramCollapse != null) {
			paramCollapse.clear();
		}
		if (defineCollapse != null) {
			defineCollapse.clear();
		}
		if (functionCollapse != null) {
			functionCollapse.clear();
		}
		cqlParametersView.resetAll();
		cqlDefinitionsView.resetAll();
		cqlFunctionsView.resetAll();
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

	/*
	 * @Override public AnchorListItem getIncludeLibrary() { return
	 * includeLibrary; }
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

	/**
	 * @return the includesLibrary
	 */
	public AnchorListItem getIncludesLibrary() {
		return includesLibrary;
	}

	/**
	 * @param includesLibrary
	 *            the includesLibrary to set
	 */
	public void setIncludesLibrary(AnchorListItem includesLibrary) {
		this.includesLibrary = includesLibrary;
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
	 * getParameterNameTxtArea()
	 */
	/**
	 * Gets the parameter name txt area.
	 *
	 * @return the parameter name txt area
	 */
	@Override
	public TextBox getParameterNameTxtArea() {
		return cqlParametersView.getParameterNameTxtArea();
	}

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
	public Map<String, CQLParameter> getParameterMap() {
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
	public Map<String, String> getParameterNameMap() {
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
	public Map<String, String> getDefineNameMap() {
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
	public Map<String, CQLDefinition> getDefinitionMap() {
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
		return cqlDefinitionsView.getDefineButtonBar().getDeleteButton();
	}

	/**
	 * Gets the delete parameter button.
	 *
	 * @return the delete define button
	 */
	@Override
	public Button getDeleteParameterButton() {
		return cqlParametersView.getParameterButtonBar().getDeleteButton();
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

	/**
	 * Gets the alias name txt area.
	 *
	 * @return the alias name txt area
	 */
	@Override
	public TextBox getAliasNameTxtArea() {
		return getIncludeView().getAliasNameTxtArea();
	}

	@Override
	public AceEditor getViewCQLEditor() {
		return getIncludeView().getViewCQLEditor();
	}

	@Override
	public TextBox getOwnerNameTextBox() {
		return getIncludeView().getOwnerNameTextBox();
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
		return cqlDefinitionsView.getDefineNameTxtArea();
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
		return cqlDefinitionsView.getDefineButtonBar().getSaveButton();
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
		return cqlParametersView.getParameterAceEditor();
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
		return cqlDefinitionsView.getDefineAceEditor();
	}

	/**
	 * Sets the define ace editor.
	 *
	 * @param defineAceEditor
	 *            the new define ace editor
	 */
	public void setDefineAceEditor(AceEditor defineAceEditor) {
		cqlDefinitionsView.setDefineAceEditor(defineAceEditor);
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
	 * @param nextClickedMenu
	 *            the new next clicked menu
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
	 * getWarningMessageAlert()
	 */
	/**
	 * Gets the warning message alert.
	 *
	 * @return the warning message alert
	 */
	@Override
	public MessageAlert getWarningMessageAlert() {
		warningMessageAlert.getElement().setAttribute("bg-color", "#ff3232");
		return warningMessageAlert;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#
	 * setWarningMessageAlert(org.gwtbootstrap3.client.ui.Alert)
	 */
	/**
	 * Sets the warning message alert.
	 *
	 * @param warningMessageAlert
	 *            the new warning message alert
	 */
	@Override
	public void setWarningMessageAlert(WarningMessageAlert warningMessageAlert) {
		this.warningMessageAlert = warningMessageAlert;
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
		return cqlFunctionsView.getFuncNameTxtArea();
	}

	/**
	 * Sets the func name txt area.
	 *
	 * @param funcNameTxtArea
	 *            the new func name txt area
	 */
	public void setFuncNameTxtArea(MatTextBox funcNameTxtArea) {
		cqlFunctionsView.setFuncNameTxtArea(funcNameTxtArea);
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
		return cqlDefinitionsView.getDefineButtonBar().getEraseButton();
	}

	/**
	 * get the erase define button.
	 *
	 * @return the erase parameter button
	 */
	@Override
	public Button getEraseParameterButton() {
		return cqlParametersView.getParameterButtonBar().getEraseButton();
	}

	/**
	 * get the erase define button.
	 *
	 * @return the erase parameter button
	 */
	@Override
	public Button getEraseFunctionButton() {
		return cqlFunctionsView.getFunctionButtonBar().getEraseButton();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getSaveFunctionButton()
	 */
	@Override
	public Button getSaveFunctionButton() {
		return cqlFunctionsView.getFunctionButtonBar().getSaveButton();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getFunctionBodyAceEditor()
	 */
	@Override
	public AceEditor getFunctionBodyAceEditor() {
		return cqlFunctionsView.getFunctionBodyAceEditor();
	}

	/**
	 * Sets the function body ace editor.
	 *
	 * @param functionBodyAceEditor
	 *            the new function body ace editor
	 */
	public void setFunctionBodyAceEditor(AceEditor functionBodyAceEditor) {
		cqlFunctionsView.setFunctionBodyAceEditor(functionBodyAceEditor);
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
	public Map<String, CQLFunctions> getFunctionMap() {
		return functionMap;
	}

	@Override
	public Map<String, CQLIncludeLibrary> getIncludeLibraryMap() {
		return includeLibraryMap;
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
		return cqlFunctionsView.getAddNewArgument();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getContextPATToggleSwitch()
	 */
	@Override
	public InlineRadio getContextDefinePATRadioBtn() {
		return cqlDefinitionsView.getContextDefinePATRadioBtn();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getContextPOPToggleSwitch()
	 */
	@Override
	public InlineRadio getContextDefinePOPRadioBtn() {
		return cqlDefinitionsView.getContextDefinePOPRadioBtn();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getAddParameterButton()
	 */
	@Override
	public Button getAddParameterButton() {
		return cqlParametersView.getParameterButtonBar().getSaveButton();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getParameterButtonBar()
	 */
	@Override
	public CQLButtonToolBar getParameterButtonBar() {
		return cqlParametersView.getParameterButtonBar();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getDefineButtonBar()
	 */
	@Override
	public CQLButtonToolBar getDefineButtonBar() {
		return cqlDefinitionsView.getDefineButtonBar();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getFunctionButtonBar()
	 */
	@Override
	public CQLButtonToolBar getFunctionButtonBar() {
		return cqlFunctionsView.getFunctionButtonBar();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getFunctionArgumentList()
	 */
	@Override
	public List<CQLFunctionArgument> getFunctionArgumentList() {
		return cqlFunctionsView.getFunctionArgumentList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * setFunctionArgumentList(java.util.List)
	 */
	@Override
	public void setFunctionArgumentList(List<CQLFunctionArgument> functionArgumentList) {
		cqlFunctionsView.setFunctionArgumentList(functionArgumentList);
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
	@Override
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

	@Override
	public SuggestBox getSearchSuggestIncludeTextBox() {
		return searchSuggestIncludeTextBox;
	}

	/**
	 * Gets the func name map.
	 *
	 * @return the func name map
	 */
	public Map<String, String> getFuncNameMap() {
		return funcNameMap;
	}

	public Map<String, String> getIncludeLibraryNameMap() {
		return includeLibraryNameMap;
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

	@Override
	public Badge getIncludesBadge() {
		return includesBadge;
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
		return cqlFunctionsView.getContextFuncPATRadioBtn();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getContextFuncPOPRadioBtn()
	 */
	@Override
	public InlineRadio getContextFuncPOPRadioBtn() {
		return cqlFunctionsView.getContextFuncPOPRadioBtn();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getFunctionArgNameMap()
	 */
	@Override
	public Map<String, CQLFunctionArgument> getFunctionArgNameMap() {
		return cqlFunctionsView.getFunctionArgNameMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * setFunctionArgNameMap(java.util.HashMap)
	 */
	@Override
	public void setFunctionArgNameMap(HashMap<String, CQLFunctionArgument> functionArgNameMap) {
		cqlFunctionsView.setFunctionArgNameMap(functionArgNameMap);
	}

	/**
	 * Sets the parameter widget read only.
	 *
	 * @param isEditable
	 *            the new parameter widget read only
	 */
	@Override
	public void setParameterWidgetReadOnly(boolean isEditable) {
		cqlParametersView.setWidgetReadOnly(isEditable);
	}

	/**
	 * Sets the define widget read only.
	 *
	 * @param isEditable
	 *            the new definition widget read only
	 */
	@Override
	public void setDefinitionWidgetReadOnly(boolean isEditable) {
		cqlDefinitionsView.setWidgetReadOnly(isEditable);
	}

	/*
	 * This Method is used to give information of Keyboard ShortcutKeys on
	 * AceEditor for Users.
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * buildInfoPanel(com.google.gwt.user.client.ui.Widget)
	 */
	@Override
	public void buildInfoPanel(Widget sourceWidget) {

		PopupPanel panel = new PopupPanel();
		panel.setAutoHideEnabled(true);
		panel.setPopupPosition(sourceWidget.getAbsoluteLeft() + 40, sourceWidget.getAbsoluteTop() + 20);
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.getElement().setId("dialogContents_VerticalPanel");
		panel.setWidget(dialogContents);

		HTML html1 = new HTML("Ctrl-Alt-v  : Value Sets");
		HTML html2 = new HTML("Ctrl-Alt-y  : Datatypes");
		HTML html3 = new HTML("CTRL-Alt-t  : Timings");
		HTML html4 = new HTML("Ctrl-Alt-f  : Functions");
		HTML html5 = new HTML("Ctrl-Alt-d  : Definitions");
		HTML html6 = new HTML("Ctrl-Alt-p  : Parameters");
		HTML html7 = new HTML("Ctrl-Alt-a  : Attributes");
		HTML html8 = new HTML("Ctrl-Space  : All");

		dialogContents.add(html1);
		dialogContents.add(html2);
		dialogContents.add(html3);
		dialogContents.add(html4);
		dialogContents.add(html5);
		dialogContents.add(html6);
		dialogContents.add(html7);
		dialogContents.add(html8);
		panel.show();
	}

	public void buildDeleteConfirmationPanel(String message) {

		deleteConfirmationDialogBox.show(message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * resetMessageDisplay()
	 */
	@Override
	public void resetMessageDisplay() {
		getWarningMessageAlert().clearAlert();
		getSuccessMessageAlert().clearAlert();
		getErrorMessageAlert().clearAlert();
		getWarningConfirmationMessageAlert().clearAlert();
		getGlobalWarningConfirmationMessageAlert().clearAlert();
		getDeleteConfirmationMessageAlert().clearAlert();
		hideAceEditorAutoCompletePopUp();

	}

	@Override
	public void hideAceEditorAutoCompletePopUp() {
		cqlDefinitionsView.hideAceEditorAutoCompletePopUp();
		cqlParametersView.hideAceEditorAutoCompletePopUp();
		cqlFunctionsView.hideAceEditorAutoCompletePopUp();
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

	public DeleteConfirmationDialogBox getDeleteConfirmationDialogBox() {
		return deleteConfirmationDialogBox;
	}

	public void setDeleteConfirmationDialogBox(DeleteConfirmationDialogBox deleteConfirmationDialogBox) {
		this.deleteConfirmationDialogBox = deleteConfirmationDialogBox;
	}

	public Button getDeleteConfirmationDialogBoxYesButton() {
		return this.deleteConfirmationDialogBox.getYesButton();
	}

	public Button getDeleteConfirmationDialogBoxNoButton() {
		return this.deleteConfirmationDialogBox.getNoButton();
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
	public List<CQLQualityDataSetDTO> getAppliedQdmList() {
		return appliedQdmList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * setAppliedQdmList(java.util.List)
	 */
	@Override
	public void setAppliedQdmList(List<CQLQualityDataSetDTO> appliedQdmList) {
		this.appliedQdmList = appliedQdmList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getDefineInfoButton()
	 */
	@Override
	public Button getDefineInfoButton() {
		return cqlDefinitionsView.getDefineButtonBar().getInfoButton();
	}

	@Override
	public Button getParamInfoButton() {
		return cqlParametersView.getParameterButtonBar().getInfoButton();
	}

	@Override
	public Button getFuncInfoButton() {
		return cqlFunctionsView.getFunctionButtonBar().getInfoButton();
	}

		/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * showUnsavedChangesWarning()
	 */
	@Override
	public void showUnsavedChangesWarning() {
		getWarningMessageAlert().clearAlert();
		getErrorMessageAlert().clearAlert();
		getSuccessMessageAlert().clearAlert();
		getGlobalWarningConfirmationMessageAlert().clearAlert();
		getDeleteConfirmationMessageAlert().clearAlert();
		getWarningConfirmationMessageAlert().createAlert();
		getWarningConfirmationMessageAlert().getWarningConfirmationYesButton().setFocus(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * showGlobalUnsavedChangesWarning()
	 */
	@Override
	public void showGlobalUnsavedChangesWarning() {
		getWarningMessageAlert().clearAlert();
		getErrorMessageAlert().clearAlert();
		getSuccessMessageAlert().clearAlert();
		getWarningConfirmationMessageAlert().clearAlert();
		getDeleteConfirmationMessageAlert().clearAlert();
		getGlobalWarningConfirmationMessageAlert().createAlert();
		getGlobalWarningConfirmationMessageAlert().getWarningConfirmationYesButton().setFocus(true);
	}

	@Override
	public Button getDefineTimingExpButton() {
		return getDefineButtonBar().getTimingExpButton();
	}

	@Override
	public Button getFuncTimingExpButton() {
		return getFunctionButtonBar().getTimingExpButton();
	}

	@Override
	public DeleteConfirmationMessageAlert getDeleteConfirmationMessageAlert() {
		return this.deleteConfirmationMessgeAlert;
	}

	@Override
	public void setDeleteConfirmationMessageAlert(DeleteConfirmationMessageAlert deleteConfirmationMessageAlert) {
		this.deleteConfirmationMessgeAlert = deleteConfirmationMessageAlert;
	}

	@Override
	public void showDeleteConfirmationMessageAlert(String message) {
		getWarningMessageAlert().clearAlert();
		getErrorMessageAlert().clearAlert();
		getSuccessMessageAlert().clearAlert();
		getWarningConfirmationMessageAlert().clearAlert();
		getDeleteConfirmationMessageAlert().createWarningAlert(message);
		getDeleteConfirmationMessageAlert().getWarningConfirmationYesButton().setFocus(true);
	}

	@Override
	public Button getDeleteConfirmationYesButton() {
		return this.getDeleteConfirmationMessageAlert().getWarningConfirmationYesButton();
	}

	@Override
	public Button getDeleteConfirmationNoButton() {
		return this.getDeleteConfirmationMessageAlert().getWarningConfirmationNoButton();
	}

	@Override
	public void setUsedCQLArtifacts(GetUsedCQLArtifactsResult results) {
		this.usedCqlArtifacts = results;
	}

	@Override
	public AnchorListItem getAppliedQDM() {
		return appliedQDM;
	}

	public void setAppliedQDM(AnchorListItem appliedQDM) {
		this.appliedQDM = appliedQDM;
	}

	@Override
	public CQLIncludeLibraryView getIncludeView() {
		return inclView;
	}

	@Override
	public CQLQDMAppliedView getQdmView() {
		return qdmView;
	}

	@Override
	public List<CQLQualityDataSetDTO> getAppliedQdmTableList() {
		return appliedQdmTableList;
	}

	@Override
	public void setAppliedQdmTableList(List<CQLQualityDataSetDTO> appliedQdmTableList) {
		this.appliedQdmTableList = appliedQdmTableList;
	}

	/**
	 * @return the includesCollapse
	 */
	@Override
	public PanelCollapse getIncludesCollapse() {
		return includesCollapse;
	}

	/**
	 * @param includesCollapse
	 *            the includesCollapse to set
	 */
	public void setIncludesCollapse(PanelCollapse includesCollapse) {
		this.includesCollapse = includesCollapse;
	}

	@Override
	public ListBox getIncludesNameListBox() {
		return includesNameListbox;
	}

	@Override
	public List<CQLLibraryDataSetObject> getIncludeLibraryList() {
		return includeLibraryList;
	}

	@Override
	public void setIncludeLibraryList(List<CQLLibraryDataSetObject> includeLibraryList) {
		this.includeLibraryList = includeLibraryList;
	}

	@Override
	public String getCurrentSelectedIncLibraryObjId() {
		return currentSelectedIncLibraryObjId;
	}

	@Override
	public void setCurrentSelectedIncLibraryObjId(String currentSelectedIncLibraryObjId) {
		this.currentSelectedIncLibraryObjId = currentSelectedIncLibraryObjId;
	}

	@Override
	public List<CQLIncludeLibrary> getViewIncludeLibrarys() {
		return viewIncludeLibrarys;
	}

	@Override
	public void setViewIncludeLibrarys(List<CQLIncludeLibrary> viewIncludeLibrarys) {
		this.viewIncludeLibrarys = viewIncludeLibrarys;
	}

	private String getOwnerName(CQLLibraryDataSetObject cqlLibrary) {
		StringBuilder owner = new StringBuilder();
		owner = owner.append(cqlLibrary.getOwnerFirstName()).append(" ").append(cqlLibrary.getOwnerLastName());
		return owner.toString();
	}

	@Override
	public CQLFunctionsView getCqlFunctionsView() {
		return cqlFunctionsView;
	}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * setIsNavBarClick(java.lang.Boolean)
	 */
	@Override
	public void setIsNavBarClick(Boolean isNavBarClick) {
		this.isNavBarClick = isNavBarClick;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * isNavBarClick()
	 */
	@Override
	public Boolean isNavBarClick() {
		return isNavBarClick;
	}

	private void unsetEachSectionSelectedObject() {
		setCurrentSelectedDefinitionObjId(null);
		setCurrentSelectedParamerterObjId(null);
		setCurrentSelectedFunctionObjId(null);
		setCurrentSelectedIncLibraryObjId(null);
		getFunctionArgumentList().clear();
		getFunctionArgNameMap().clear();
		if (getFunctionArgumentList().size() > 0) {
			getFunctionArgumentList().clear();
		}
	}
}
