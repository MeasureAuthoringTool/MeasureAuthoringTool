package mat.client.clause.cqlworkspace;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gwtbootstrap3.client.shared.event.HideEvent;
import org.gwtbootstrap3.client.shared.event.HideHandler;
import org.gwtbootstrap3.client.shared.event.ShowEvent;
import org.gwtbootstrap3.client.shared.event.ShowHandler;
import org.gwtbootstrap3.client.ui.InlineRadio;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.constants.ValidationState;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.ycp.cs.dh.acegwt.client.ace.AceAnnotationType;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.CqlComposerPresenter;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.clause.QDSAttributesService;
import mat.client.clause.QDSAttributesServiceAsync;
import mat.client.clause.cqlworkspace.CQLCodesView.Delegator;
import mat.client.clause.cqlworkspace.CQLFunctionsView.Observer;
import mat.client.clause.event.QDSElementCreatedEvent;
import mat.client.codelist.HasListBox;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.client.event.CQLLibrarySelectedEvent;
import mat.client.measure.service.CQLLibraryServiceAsync;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.client.shared.CQLButtonToolBar;
import mat.client.shared.MatContext;
import mat.client.shared.ValueSetNameInputValidator;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.umls.service.VsacApiResult;
import mat.model.CQLValueSetTransferObject;
import mat.model.CodeListSearchDTO;
import mat.model.MatCodeTransferObject;
import mat.model.MatValueSet;
import mat.model.VSACVersion;
import mat.model.clause.QDSAttributes;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.shared.CQLErrors;
import mat.shared.CQLIdentifierObject;
import mat.shared.CQLModelValidator;
import mat.shared.ConstantMessages;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.SaveUpdateCQLResult;

// TODO: Auto-generated Javadoc
/**
 * The Class CQLStandaloneWorkSpacePresenter.
 */
public class CQLStandaloneWorkSpacePresenter implements MatPresenter {

	/** The panel. */
	private SimplePanel panel = new SimplePanel();

	/** The search display. */
	private ViewDisplay searchDisplay;

	/** The empty widget. */
	private SimplePanel emptyWidget = new SimplePanel();

	/** The is measure details loaded. */
	private boolean isCQLWorkSpaceLoaded = false;

	/** The current section. */
	private String currentSection = "general";
	/** The next clicked menu. */
	private String nextSection = "general";

	/** The applied QDM list. */
	private List<CQLQualityDataSetDTO> appliedValueSetTableList = new ArrayList<CQLQualityDataSetDTO>();

	/** The validator. */
	CQLModelValidator validator = new CQLModelValidator();

	/** The cql library name. */
	private String cqlLibraryName;

	/** The exp profile to all qdm. */
//	private String expProfileToAllValueSet = "";

	/** The setId for the current library. */
	private String setId = null;
	
	/**
	 * The set id of the currently selected 
	 */
	private String currentIncludeLibrarySetId = null;
	
	/**
	 * The id of the currently selected included library
	 */
	private String currentIncludeLibraryId = null; 

	/** The is modfied. */
	private boolean isModified = false;

	/** The is expansion profile. */
	//private boolean isExpansionProfile = false;

	/** The is u ser defined. */
	private boolean isUserDefined = false;

	/** The modify value set dto. */
	private CQLQualityDataSetDTO modifyValueSetDTO;

	/** QDSAttributesServiceAsync instance. */
	private QDSAttributesServiceAsync attributeService = (QDSAttributesServiceAsync) GWT
			.create(QDSAttributesService.class);

	/** The cur ace editor. */
	private AceEditor curAceEditor;

	/** The vsacapi service. */
	private final VSACAPIServiceAsync vsacapiService = MatContext.get().getVsacapiServiceAsync();

	/** The cql service. */
	private final CQLLibraryServiceAsync cqlService = MatContext.get().getCQLLibraryService();

	/** The current mat value set. */
	private MatValueSet currentMatValueSet= null;
	
	/** The applied code table list. */
	private List<CQLCode> appliedCodeTableList = new ArrayList<CQLCode>();
	
	/**
	 * Flag for if parameters, definitions, or functions should be formatted.
	 * For now this flag will always be set to true. 
	 */
	private boolean isFormatable = true;

	/**
	 * The Interface ViewDisplay.
	 */
	public static interface ViewDisplay {

		/**
		 * Top Main panel of CQL Workspace Tab.
		 * 
		 * @return HorizontalPanel
		 */
		VerticalPanel getMainPanel();

		/**
		 * Gets the main v panel.
		 *
		 * @return the main v panel
		 */
		Widget asWidget();

		/**
		 * Gets the main h panel.
		 *
		 * @return the main h panel
		 */
		HorizontalPanel getMainHPanel();

		/**
		 * Gets the main flow panel.
		 *
		 * @return the main flow panel
		 */
		FlowPanel getMainFlowPanel();

		/**
		 * Generates View for CQLWorkSpace tab.
		 */
		void buildView();

		/**
		 * Gets the clicked menu.
		 *
		 * @return the clicked menu
		 */
		String getClickedMenu();

		/**
		 * Sets the clicked menu.
		 *
		 * @param clickedMenu the new clicked menu
		 */
		void setClickedMenu(String clickedMenu);

		/**
		 * Gets the next clicked menu.
		 *
		 * @return the next clicked menu
		 */
		String getNextClickedMenu();

		/**
		 * Sets the next clicked menu.
		 *
		 * @param nextClickedMenu the new next clicked menu
		 */
		void setNextClickedMenu(String nextClickedMenu);

		/**
		 * Gets the cql left nav bar panel view.
		 *
		 * @return the cql left nav bar panel view
		 */
		CQLLeftNavBarPanelView getCqlLeftNavBarPanelView();

		/**
		 * Reset message display.
		 */
		void resetMessageDisplay();

		/**
		 * Hide ace editor auto complete pop up.
		 */
		void hideAceEditorAutoCompletePopUp();

		/**
		 * Gets the CQL parameters view.
		 *
		 * @return the CQL parameters view
		 */
		CQLParametersView getCQLParametersView();

		/**
		 * Gets the CQL definitions view.
		 *
		 * @return the CQL definitions view
		 */
		CQlDefinitionsView getCQLDefinitionsView();

		/**
		 * Gets the CQL functions view.
		 *
		 * @return the CQL functions view
		 */
		CQLFunctionsView getCQLFunctionsView();

		/**
		 * Gets the cql include library view.
		 *
		 * @return the cql include library view
		 */
		CQLIncludeLibraryView getCqlIncludeLibraryView();

		/**
		 * Builds the CQL file view.
		 */
		void buildCQLFileView();

		/**
		 * Gets the cql ace editor.
		 *
		 * @return the cql ace editor
		 */
		AceEditor getCqlAceEditor();

		/**
		 * Builds the general information.
		 */
		void buildGeneralInformation();

		/**
		 * Gets the cql general information view.
		 *
		 * @return the cql general information view
		 */
		CQLGeneralInformationView getCqlGeneralInformationView();

		/**
		 * Gets the include view.
		 *
		 * @return the include view
		 */
		CQLIncludeLibraryView getIncludeView();

		/**
		 * Gets the alias name txt area.
		 *
		 * @return the alias name txt area
		 */
		TextBox getAliasNameTxtArea();

		/**
		 * Gets the view CQL editor.
		 *
		 * @return the view CQL editor
		 */
		AceEditor getViewCQLEditor();

		/**
		 * Gets the owner name text box.
		 *
		 * @return the owner name text box
		 */
		TextBox getOwnerNameTextBox();

		/**
		 * Builds the includes view.
		 */
		void buildIncludesView();

		/**
		 * Reset all.
		 */
		void resetAll();

		/**
		 * Builds the parameter library view.
		 */
		void buildParameterLibraryView();

		/**
		 * Builds the definition library view.
		 */
		void buildDefinitionLibraryView();

		/**
		 * Builds the function library view.
		 */
		void buildFunctionLibraryView();

		/**
		 * Creates the add argument view for functions.
		 *
		 * @param argumentList the argument list
		 */
		void createAddArgumentViewForFunctions(List<CQLFunctionArgument> argumentList);

		/**
		 * Gets the parameter button bar.
		 *
		 * @return the parameter button bar
		 */
		CQLButtonToolBar getParameterButtonBar();

		/**
		 * Gets the define button bar.
		 *
		 * @return the define button bar
		 */
		CQLButtonToolBar getDefineButtonBar();

		/**
		 * Gets the function button bar.
		 *
		 * @return the function button bar
		 */
		CQLButtonToolBar getFunctionButtonBar();

		/* void buildInfoPanel(Widget sourceWidget); */

		/**
		 * Gets the define name txt area.
		 *
		 * @return the define name txt area
		 */
		TextBox getDefineNameTxtArea();

		/**
		 * Gets the define ace editor.
		 *
		 * @return the define ace editor
		 */
		AceEditor getDefineAceEditor();

		/**
		 * Gets the context define PAT radio btn.
		 *
		 * @return the context define PAT radio btn
		 */
		InlineRadio getContextDefinePATRadioBtn();

		/**
		 * Gets the context define POP radio btn.
		 *
		 * @return the context define POP radio btn
		 */
		InlineRadio getContextDefinePOPRadioBtn();

		/**
		 * Gets the func name txt area.
		 *
		 * @return the func name txt area
		 */
		TextBox getFuncNameTxtArea();

		/**
		 * Gets the function body ace editor.
		 *
		 * @return the function body ace editor
		 */
		AceEditor getFunctionBodyAceEditor();

		/**
		 * Gets the context func PAT radio btn.
		 *
		 * @return the context func PAT radio btn
		 */
		InlineRadio getContextFuncPATRadioBtn();

		/**
		 * Gets the context func POP radio btn.
		 *
		 * @return the context func POP radio btn
		 */
		InlineRadio getContextFuncPOPRadioBtn();

		/**
		 * Gets the function argument list.
		 *
		 * @return the function argument list
		 */
		List<CQLFunctionArgument> getFunctionArgumentList();

		/**
		 * Gets the parameter name txt area.
		 *
		 * @return the parameter name txt area
		 */
		TextBox getParameterNameTxtArea();

		/**
		 * Gets the parameter ace editor.
		 *
		 * @return the parameter ace editor
		 */
		AceEditor getParameterAceEditor();

		/**
		 * Gets the function arg name map.
		 *
		 * @return the function arg name map
		 */
		Map<String, CQLFunctionArgument> getFunctionArgNameMap();

		/**
		 * Creates the add argument view for functions.
		 *
		 * @param argumentList the argument list
		 * @param isEditable the is editable
		 */
		void createAddArgumentViewForFunctions(List<CQLFunctionArgument> argumentList, boolean isEditable);

		/**
		 * Gets the value set view.
		 *
		 * @return the value set view
		 */
		CQLAppliedValueSetView getValueSetView();
		
		/**
		 * Gets the codes view.
		 *
		 * @return the codes view
		 */
		CQLCodesView getCodesView();

		/**
		 * Builds the applied QDM.
		 */
		void buildAppliedQDM();

		/**
		 * Builds the codes.
		 */
		void buildCodes();

		/**
		 * Gets the locked button V panel.
		 *
		 * @return the locked button V panel
		 */
		HorizontalPanel getLockedButtonVPanel();

		void hideInformationDropDown();

		CQLView getViewCQLView();

		void setGeneralInfoHeading();

	}

	/**
	 * Instantiates a new CQL presenter.
	 *
	 * @param srchDisplay the srch display
	 */
	public CQLStandaloneWorkSpacePresenter(final ViewDisplay srchDisplay) {
		searchDisplay = srchDisplay;
		emptyWidget.add(new Label("No CQL Library Selected"));
		addEventHandlers();
	}

	/**
	 * Adds the event handlers.
	 */
	private void addEventHandlers() {
		MatContext.get().getEventBus().addHandler(CQLLibrarySelectedEvent.TYPE, new CQLLibrarySelectedEvent.Handler() {

			@Override
			public void onLibrarySelected(CQLLibrarySelectedEvent event) {
				isCQLWorkSpaceLoaded = false;
				if (event.getCqlLibraryId() != null) {
					isCQLWorkSpaceLoaded = true;
					logRecentActivity();
				} else {
					displayEmpty();
				}
			}

		});
		searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBoxYesButton()
				.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId() != null) {
							deleteDefinition();
							searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
						} else if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId() != null
								&& searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionArgumentObjId() == null) {
							deleteFunction();
							searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
						}else if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionArgumentObjId() != null) {
							deleteFunctionArgument();
							searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
						} else if (searchDisplay.getCqlLeftNavBarPanelView()
								.getCurrentSelectedParamerterObjId() != null) {
							deleteParameter();
							searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
						} else if (searchDisplay.getCqlLeftNavBarPanelView()
								.getCurrentSelectedIncLibraryObjId() != null) {
							deleteInclude();
							searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
						}  else if(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedValueSetObjId() != null){
							checkAndDeleteValueSet();
							searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
						} else if(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedCodesObjId() != null){
							deleteCode();
							searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
						}
					}
				});

		searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBoxNoButton()
				.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						searchDisplay.resetMessageDisplay();
						searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
					}
				});

		ClickHandler cHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getCQLDefinitionsView().getDefineAceEditor().detach();
				searchDisplay.getCQLParametersView().getParameterAceEditor().detach();
				searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().detach();
			}
		};
		searchDisplay.getMainPanel().addDomHandler(cHandler, ClickEvent.getType());

		addGeneralInfoEventHandlers();
		addValueSetEventHandlers();
		addCodeSearchPanelHandlers();
		addIncludeCQLLibraryHandlers();
		addParameterEventHandlers();
		addDefineEventHandlers();
		addFunctionEventHandlers();
		addEventHandlerOnAceEditors();
		addEventHandlersOnContextRadioButtons();
		addWarningAlertHandlers();
	}

	/**
	 * On modify value set qdm.
	 *
	 * @param result
	 *            the result
	 * @param isUserDefined
	 *            the is user defined
	 */
	private void onModifyValueSet(CQLQualityDataSetDTO result, boolean isUserDefined) {

		String oid = isUserDefined ? "" : result.getOid();
		searchDisplay.getValueSetView().getOIDInput().setEnabled(true);

		searchDisplay.getValueSetView().getOIDInput().setValue(oid);
		searchDisplay.getValueSetView().getOIDInput().setTitle(oid);

		searchDisplay.getValueSetView().getRetrieveFromVSACButton().setEnabled(!isUserDefined);

		searchDisplay.getValueSetView().getUserDefinedInput().setEnabled(isUserDefined);
		searchDisplay.getValueSetView().getUserDefinedInput().setValue(result.getOriginalCodeListName());
		searchDisplay.getValueSetView().getUserDefinedInput().setTitle(result.getOriginalCodeListName());

		searchDisplay.getValueSetView().getSuffixInput().setEnabled(true);
		searchDisplay.getValueSetView().getSuffixInput().setValue(result.getSuffix());
		searchDisplay.getValueSetView().getSuffixInput().setTitle(result.getSuffix());
	
		searchDisplay.getValueSetView().getVersionListBox().clear();
		searchDisplay.getValueSetView().getVersionListBox().setEnabled(false);

	
		searchDisplay.getValueSetView().getSaveButton().setEnabled(isUserDefined);
	}

	
	/**
	 * Save library xml.
	 *
	 * @param toBeDeletedValueSetId
	 *            the to Be Deleted Value Set Id
	 * 
	 */
	private void deleteValueSet(String toBeDeletedValueSetId) {
		showSearchingBusy(true);
		MatContext.get().getCQLLibraryService().deleteValueSet(toBeDeletedValueSetId,
				MatContext.get().getCurrentCQLLibraryId(), new AsyncCallback<SaveUpdateCQLResult>() {

					@Override
					public void onFailure(final Throwable caught) {
						Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						showSearchingBusy(false);
					}

					@Override
					public void onSuccess(final SaveUpdateCQLResult result) {
						if (result != null && result.getCqlErrors().isEmpty()) {
							modifyValueSetDTO = null;
							// The below call will update the Applied QDM drop
							// down list in insert popup.
							getAppliedValueSetList();
							searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
									.createAlert(MatContext.get().getMessageDelegate().getSUCCESSFUL_QDM_REMOVE_MSG(result.getCqlQualityDataSetDTO().getCodeListName()));
							searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().setVisible(true);
						}
						getUsedArtifacts();
						showSearchingBusy(false);
					}
				});
	}

	/**
	 * Gets the applied QDM list.
	 *
	 * @return the applied QDM list
	 */
	private void getAppliedValueSetList() {
		showSearchingBusy(true);
		String cqlLibraryId = MatContext.get().getCurrentCQLLibraryId();
		if ((cqlLibraryId != null) && !cqlLibraryId.equals("")) {
			MatContext.get().getLibraryService().getCQLData(cqlLibraryId, new AsyncCallback<SaveUpdateCQLResult>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					showSearchingBusy(false);

				}

				@Override
				public void onSuccess(SaveUpdateCQLResult result) {
					appliedValueSetTableList.clear();
					List<CQLQualityDataSetDTO> allValuesets = new ArrayList<CQLQualityDataSetDTO>();
					for (CQLQualityDataSetDTO dto : result.getCqlModel().getAllValueSetList()) {
						if (dto.isSuppDataElement())
							continue;
						allValuesets.add(dto);
					}
				//	searchDisplay.getCqlLeftNavBarPanelView().setAppliedQdmList(allValuesets);
					MatContext.get().setValuesets(allValuesets);
					for (CQLQualityDataSetDTO valueset : allValuesets) {
						// filtering out codes from valuesets list
						if((valueset.getOid().equals("419099009") || valueset.getOid().equals("21112-8") 
								|| (valueset.getType() !=null) && valueset.getType().equalsIgnoreCase("code"))){
							continue;
						}
						appliedValueSetTableList.add(valueset);
					}
					searchDisplay.getValueSetView().buildAppliedValueSetCellTable(appliedValueSetTableList,
							MatContext.get().getLibraryLockService().checkForEditPermission());
					searchDisplay.getCqlLeftNavBarPanelView().updateValueSetMap(appliedValueSetTableList);
					showSearchingBusy(false);
				}
			});
		}

	}

	/**
	 * Adds the parameter event handlers.
	 */
	private void addParameterEventHandlers() {

		/*
		 * searchDisplay.getCQLParametersView().getParameterButtonBar().
		 * getCommentButton().addClickHandler(new ClickHandler() {
		 * 
		 * @Override public void onClick(ClickEvent event) {
		 * searchDisplay.getParameterAceEditor().execCommand(AceCommand.
		 * SELECT_ALL);
		 * searchDisplay.getParameterAceEditor().execCommand(AceCommand.
		 * TOGGLE_BLOCK_COMMENT);
		 * 
		 * } });
		 */
		searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
					DomEvent.fireNativeEvent(
							Document.get()
									.createDblClickEvent(searchDisplay.getCqlLeftNavBarPanelView()
											.getParameterNameListBox().getSelectedIndex(), 0, 0, 0, 0, false, false,
											false, false),
							searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox());
				}

			}
		});
		
		searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox()
				.addDoubleClickHandler(new DoubleClickHandler() {
					@Override
					public void onDoubleClick(DoubleClickEvent event) {

						if (searchDisplay.getCqlLeftNavBarPanelView().getIsLoading()) {
							event.stopPropagation();
						} else {
							showSearchBusyOnDoubleClick(true);
							searchDisplay.getCQLParametersView().getParameterAceEditor().clearAnnotations();
							searchDisplay.getCQLParametersView().getParameterAceEditor().removeAllMarkers();
							searchDisplay.getCQLParametersView().getParameterButtonBar().getInfoButtonGroup()
									.getElement().setAttribute("class", "btn-group");
							resetAceEditor(searchDisplay.getCQLParametersView().getViewCQLAceEditor());
							resetViewCQLCollapsiblePanel(
									searchDisplay.getCQLParametersView().getPanelViewCQLCollapse());

							searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(true);
							searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
							if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
								showSearchBusyOnDoubleClick(false);
								searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
							} else {
								int selectedIndex = searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox()
										.getSelectedIndex();
								if (selectedIndex != -1) {
									final String selectedParamID = searchDisplay.getCqlLeftNavBarPanelView()
											.getParameterNameListBox().getValue(selectedIndex);
									searchDisplay.getCqlLeftNavBarPanelView()
											.setCurrentSelectedParamerterObjId(selectedParamID);
									if (searchDisplay.getCqlLeftNavBarPanelView().getParameterMap()
											.get(selectedParamID) != null) {

										searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton()
												.setTitle("Delete");
										searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton()
												.setEnabled(false);
										searchDisplay.getCQLParametersView().setWidgetReadOnly(false);
										searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton()
												.setEnabled(false);
										// load most recent used cql artifacts
										MatContext.get().getCQLLibraryService().getUsedCqlArtifacts(
												MatContext.get().getCurrentCQLLibraryId(),
												new AsyncCallback<GetUsedCQLArtifactsResult>() {

													@Override
													public void onFailure(Throwable caught) {
														showSearchBusyOnDoubleClick(false);
														Window.alert(MatContext.get().getMessageDelegate()
																.getGenericErrorMessage());
													}

													@Override
													public void onSuccess(GetUsedCQLArtifactsResult result) {
														showSearchBusyOnDoubleClick(false);
														searchDisplay.getCQLParametersView().getParameterNameTxtArea().setText(searchDisplay.getCqlLeftNavBarPanelView().getParameterMap().get(selectedParamID).getParameterName());
														searchDisplay.getCQLParametersView().getParameterAceEditor().setText(searchDisplay.getCqlLeftNavBarPanelView().getParameterMap().get(selectedParamID).getParameterLogic());
														searchDisplay.getCQLParametersView().getParameterCommentTextArea().setText(searchDisplay.getCqlLeftNavBarPanelView().getParameterMap().get(selectedParamID).getCommentString());

														boolean isReadOnly = searchDisplay.getCqlLeftNavBarPanelView().getParameterMap().get(selectedParamID).isReadOnly();
														CQLParameter currentParameter = searchDisplay.getCqlLeftNavBarPanelView().getParameterMap().get(selectedParamID);
														if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
															searchDisplay.getCQLParametersView().setWidgetReadOnly(!isReadOnly);
															// checking if its Draft.
															if (MatContext.get().getCurrentCQLLibraryVersion().toLowerCase().contains(CQLWorkSpaceConstants.CQL_DRAFT.toLowerCase())) {
																searchDisplay.getCQLParametersView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
															}
															
															if (!currentParameter.isReadOnly()) {
																// if there are cql errors or the parameter is not in use, enable the delete button
																if (!result.getCqlErrors().isEmpty() || !result.getUsedCQLParameters().contains(currentParameter.getParameterName())) {
																	searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(true);
																} else{
																	searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(false);
																}
															}
															
														}
														

														Map<String, List<CQLErrors>> expressionCQLErrorMap = result.getCqlErrorsPerExpression();
														if (expressionCQLErrorMap != null) {
															List<CQLErrors> errorList = expressionCQLErrorMap.get(currentParameter.getParameterName());
															searchDisplay.getCQLParametersView().getParameterAceEditor().clearAnnotations();
															searchDisplay.getCQLParametersView().getParameterAceEditor().removeAllMarkers();
															for (CQLErrors error : errorList) {
																int startLine = error.getStartErrorInLine();
																int startColumn = error.getStartErrorAtOffset();
																searchDisplay.getCQLParametersView().getParameterAceEditor().addAnnotation(startLine, startColumn,error.getErrorMessage(),AceAnnotationType.ERROR);
															}
															searchDisplay.getCQLParametersView().getParameterAceEditor().setAnnotations();
														}

														// if it is not a default parameter, check if the delete button needs to be enabled
														/*if (!currentParameter.isReadOnly()) {
															// if there are cql errors or the parameter is not in use, enable the delete button
															if (!result.getCqlErrors().isEmpty() || !result.getUsedCQLParameters().contains(currentParameter.getParameterName())) {
																searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(true);
															}
														}*/
													}

												});

									} else {
										showSearchBusyOnDoubleClick(false);
									}
								} else {
									showSearchBusyOnDoubleClick(false);
								}

								searchDisplay.resetMessageDisplay();

							}
						}
						// 508 change to parameter section
						searchDisplay.getCqlLeftNavBarPanelView()
								.setFocus(searchDisplay.getCQLParametersView().getMainParamViewVerticalPanel());
					}
				});
		
		
		searchDisplay.getCQLParametersView().getPanelViewCQLCollapse().addShowHandler(new ShowHandler() {
			
			@Override
			public void onShow(ShowEvent showEvent) {
				searchDisplay.resetMessageDisplay();
				showCompleteCQL(searchDisplay.getCQLParametersView().getViewCQLAceEditor());
			}
		});
		
		searchDisplay.getCQLParametersView().getPanelViewCQLCollapse().addHideHandler(new HideHandler() {
			
			@Override
			public void onHide(HideEvent hideEvent) {
				searchDisplay.getCQLParametersView().getParameterButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
				resetAceEditor(searchDisplay.getCQLParametersView().getViewCQLAceEditor());
				
			}
		});
		
		
		// Parameter Save Icon Functionality
		searchDisplay.getCQLParametersView().getParameterButtonBar().getSaveButton()
		.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				resetViewCQLCollapsiblePanel(searchDisplay.getCQLParametersView().getPanelViewCQLCollapse());
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					addAndModifyParameters();
					//508 change to parameter section
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLParametersView().getParameterNameTxtArea());
				}
			}

		});
		// Parameter Erase Icon Functionality
		searchDisplay.getCQLParametersView().getParameterButtonBar().getEraseButton()
		.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				resetViewCQLCollapsiblePanel(searchDisplay.getCQLParametersView().getPanelViewCQLCollapse());
				eraseParameter(); 
				//508 change to parameter section
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLParametersView().getParameterAceEditor());
			}
		});
		// Parameter Delete Icon Functionality
		searchDisplay.getParameterButtonBar().getDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				resetViewCQLCollapsiblePanel(searchDisplay.getCQLParametersView().getPanelViewCQLCollapse());
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_PARAMETER(searchDisplay.getCQLParametersView().getParameterNameTxtArea().getValue()));
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();

			}

		});

		// Parameter Info Icon Functionality
		/*searchDisplay.getCQLParametersView().getParameterButtonBar().getInfoButton()
		.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlLeftNavBarPanelView().buildInfoPanel((Widget) event.getSource());

			}
		});*/

		searchDisplay.getCQLParametersView().getParameterNameTxtArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});
		
		searchDisplay.getCQLParametersView().getParameterCommentTextArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});


		// Parameter Add New Functionality
		searchDisplay.getCQLParametersView().getAddNewButtonBar().getaddNewButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				resetViewCQLCollapsiblePanel(searchDisplay.getCQLParametersView().getPanelViewCQLCollapse());
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
				} else {
					addNewParameter();
				}
				//508 change to parameter section
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLParametersView().getParameterNameTxtArea());
			}
		});
		
		searchDisplay.getCQLParametersView().getParameterCommentTextArea().addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCQLParametersView().getParamCommentGroup().setValidationState(ValidationState.NONE);
				String comment = searchDisplay.getCQLParametersView().getParameterCommentTextArea().getText();
				if(validator.validateForCommentTextArea(comment)){
					searchDisplay.getCQLParametersView().getParamCommentGroup().setValidationState(ValidationState.ERROR);
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
							MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				}
			}
		});
	}
	
	/**
	 * Get Complete CQL And add it to Collapsible Panel in read Only Mode.
	 */
	private void showCompleteCQL(final AceEditor aceEditor) {
		MatContext.get().getCQLLibraryService().getLibraryCQLFileData(MatContext.get().getCurrentCQLLibraryId(),
				new AsyncCallback<SaveUpdateCQLResult>() {
					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						if (result.isSuccess()) {
							if ((result.getCqlString() != null) && !result.getCqlString().isEmpty()) {
								
								aceEditor.clearAnnotations();
								aceEditor.redisplay();
								
								if (!result.getCqlErrors().isEmpty()) {
									
									for (CQLErrors error : result.getCqlErrors()) {
										String errorMessage = new String();
										errorMessage = errorMessage.concat("Error in line : " + error.getErrorInLine()
												+ " at Offset :" + error.getErrorAtOffeset());
										int line = error.getErrorInLine();
										int column = error.getErrorAtOffeset();
										aceEditor.addAnnotation(line - 1, column,
												error.getErrorMessage(), AceAnnotationType.ERROR);
									}
									aceEditor.setText(result.getCqlString());
									//searchDisplay.getCqlAceEditor().lockEditor(0, 0);
									aceEditor.setAnnotations();
									aceEditor.gotoLine(1);
									aceEditor.redisplay();
								} else {
									
									aceEditor.setText(result.getCqlString());
									aceEditor.gotoLine(1);
								}
							}

						}
						//showSearchingBusy(false);
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					//	showSearchingBusy(false);
					}
				});
	}
	
	/**
	 * 
	 */
	private void resetAceEditor(AceEditor aceEditor) {
		aceEditor.clearAnnotations();
		aceEditor.removeAllMarkers();
		//Commenting below code as its taking away focus and that makes our application not 508 compliant with other fields.
		//aceEditor.redisplay();
		aceEditor.setText("");
	}
	
	/**
	 * @param panelCollapse 
	 * 
	 */
	public void resetViewCQLCollapsiblePanel(PanelCollapse panelCollapse) {
		panelCollapse.getElement().setClassName("panel-collapse collapse");
	}
	
	/**
	 * Adds the new parameter.
	 */
	private void addNewParameter() {

		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedParamerterObjId(null);
		searchDisplay.getCQLParametersView().getParameterAceEditor().clearAnnotations();
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
		if ((searchDisplay.getCQLParametersView().getParameterAceEditor().getText() != null)) {
			searchDisplay.getCQLParametersView().getParameterAceEditor().setText("");
		}
		if ((searchDisplay.getCQLParametersView().getParameterNameTxtArea() != null)) {
			searchDisplay.getCQLParametersView().getParameterNameTxtArea().setText("");
		}
		
		if ((searchDisplay.getCQLParametersView().getParameterCommentTextArea() != null)) {
			searchDisplay.getCQLParametersView().getParameterCommentTextArea().setText("");
		}

		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			searchDisplay.getCQLParametersView()
					.setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
				searchDisplay.getCQLParametersView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
		}
		// Below lines are to clear search suggestion textbox and listbox
		// selection after erase.
		searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestParamTextBox().setText("");
		if (searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().getSelectedIndex() >= 0) {
			searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().setItemSelected(
					searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().getSelectedIndex(), false);
		}

		searchDisplay.getParameterButtonBar().getDeleteButton().setEnabled(false);
	}
	
	/**
	 * Adds the define event handlers.
	 */
	private void addDefineEventHandlers() {
		
		searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
					DomEvent.fireNativeEvent(
							Document.get()
									.createDblClickEvent(searchDisplay.getCqlLeftNavBarPanelView()
											.getDefineNameListBox().getSelectedIndex(), 0, 0, 0, 0, false, false, false,
											false),
							searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox());
				}

			}
		});
		
		searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox()
		.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsLoading()) {
					event.stopPropagation();
				} else {
					showSearchBusyOnDoubleClick(true);
					searchDisplay.getCQLDefinitionsView().getDefineAceEditor().clearAnnotations();
					searchDisplay.getCQLDefinitionsView().getDefineAceEditor().removeAllMarkers();
					
					resetAceEditor(searchDisplay.getCQLDefinitionsView().getViewCQLAceEditor());
					resetViewCQLCollapsiblePanel(searchDisplay.getCQLDefinitionsView().getPanelViewCQLCollapse());
					searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setText("");
					
					searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(true);
					if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
						showSearchBusyOnDoubleClick(false);
						searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
					} else {
						int selectedIndex = searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox()
								.getSelectedIndex();
						if (selectedIndex != -1) {
							final String selectedDefinitionID = searchDisplay.getCqlLeftNavBarPanelView()
									.getDefineNameListBox().getValue(selectedIndex);
							searchDisplay.getCqlLeftNavBarPanelView()
							.setCurrentSelectedDefinitionObjId(selectedDefinitionID);
							
							if (searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap()
									.get(selectedDefinitionID) != null) {
								searchDisplay.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton()
								.setTitle("Delete");
								
								searchDisplay.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(false);
								searchDisplay.getCQLDefinitionsView().setWidgetReadOnly(false);
								
								// load most recent used cql artifacts
								MatContext.get().getCQLLibraryService().getUsedCqlArtifacts(
										MatContext.get().getCurrentCQLLibraryId(),
										new AsyncCallback<GetUsedCQLArtifactsResult>() {

											@Override
											public void onFailure(Throwable caught) {
												showSearchBusyOnDoubleClick(false);
												searchDisplay.getCQLDefinitionsView().setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
												Window.alert(MatContext.get().getMessageDelegate()
														.getGenericErrorMessage());
											}

											@Override
											public void onSuccess(GetUsedCQLArtifactsResult result) {
												showSearchBusyOnDoubleClick(false);
												boolean isReadOnly = searchDisplay.getCqlLeftNavBarPanelView()
														.getDefinitionMap().get(selectedDefinitionID).isSupplDataElement();
												
												CQLDefinition currentDefinition = searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap().get(selectedDefinitionID);

												searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea()
												.setText(searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap()
														.get(selectedDefinitionID).getDefinitionName());
												searchDisplay.getCQLDefinitionsView().getDefineAceEditor()
												.setText(searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap()
														.get(selectedDefinitionID).getDefinitionLogic());
												searchDisplay.getCQLDefinitionsView().getDefineCommentTextArea()
												.setText(searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap().get(selectedDefinitionID).getCommentString());
												
												if (searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap()
														.get(selectedDefinitionID).getContext().equalsIgnoreCase("patient")) {
													searchDisplay.getCQLDefinitionsView().getContextDefinePATRadioBtn()
													.setValue(true);
													searchDisplay.getCQLDefinitionsView().getContextDefinePOPRadioBtn()
													.setValue(false);
												} else {
													searchDisplay.getCQLDefinitionsView().getContextDefinePOPRadioBtn()
													.setValue(true);
													searchDisplay.getCQLDefinitionsView().getContextDefinePATRadioBtn()
													.setValue(false);
												}
												
												if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
													searchDisplay.getCQLDefinitionsView().setWidgetReadOnly(!isReadOnly);
													//Checks if Draft
													if(MatContext.get().getCurrentCQLLibraryVersion().toLowerCase().contains(CQLWorkSpaceConstants.CQL_DRAFT.toLowerCase())){
														searchDisplay.getCQLDefinitionsView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
													}
													
													// if there are cql errors or the definition is not in use, enable the delete button
													if(!result.getCqlErrors().isEmpty() || !result.getUsedCQLDefinitions().contains(currentDefinition.getDefinitionName())) {
														searchDisplay.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(true);
													} else {
														searchDisplay.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(false);
													}
													
													
												}
												
												Map<String,List<CQLErrors>> expressionCQLErrorMap = result.getCqlErrorsPerExpression();
												if(expressionCQLErrorMap != null){
													List<CQLErrors> errorList = expressionCQLErrorMap.get(currentDefinition.getDefinitionName());
													searchDisplay.getCQLDefinitionsView().getDefineAceEditor().clearAnnotations();
													searchDisplay.getCQLDefinitionsView().getDefineAceEditor().removeAllMarkers();
													for (CQLErrors error : errorList) {
														int startLine = error.getStartErrorInLine();
														int startColumn = error.getStartErrorAtOffset();
														searchDisplay.getCQLDefinitionsView().getDefineAceEditor().addAnnotation(startLine, startColumn, error.getErrorMessage(), AceAnnotationType.ERROR);
													}
													searchDisplay.getCQLDefinitionsView().getDefineAceEditor().setAnnotations();
												}
												
												if(result.getCqlErrors().isEmpty() && result.getExpressionReturnTypeMap() != null){
													searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setText(result.getExpressionReturnTypeMap()
															.get(currentDefinition.getDefinitionName()));
													searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is "+result.getExpressionReturnTypeMap()
															.get(currentDefinition.getDefinitionName()) );
										
												} else {
													searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setText("");
													searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
													
												}
												
											}

										});
								
							} else {
								showSearchBusyOnDoubleClick(false);
							}
						}else {
							showSearchBusyOnDoubleClick(false);
						}

						searchDisplay.resetMessageDisplay();
					}
				}
				//508 changes for Definitions Section
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLDefinitionsView().getMainDefineViewVerticalPanel());
			}
		});
		
		searchDisplay.getCQLDefinitionsView().getPanelViewCQLCollapse().addShowHandler(new ShowHandler() {
			
			@Override
			public void onShow(ShowEvent showEvent) {
				searchDisplay.resetMessageDisplay();
				showCompleteCQL(searchDisplay.getCQLDefinitionsView().getViewCQLAceEditor());
			}
		});
		
		searchDisplay.getCQLDefinitionsView().getPanelViewCQLCollapse().addHideHandler(new HideHandler() {
			
			@Override
			public void onHide(HideEvent hideEvent) {
				searchDisplay.getCQLDefinitionsView().getDefineButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
				resetAceEditor(searchDisplay.getCQLDefinitionsView().getViewCQLAceEditor());
				
			}
			
		});
		
		
		searchDisplay.getDefineButtonBar().getInsertButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				buildInsertPopUp();
			}
		});
		// Definition Save Icon Functionality
		searchDisplay.getCQLDefinitionsView().getDefineButtonBar().getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				resetViewCQLCollapsiblePanel(searchDisplay.getCQLDefinitionsView().getPanelViewCQLCollapse());
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					addAndModifyDefintions();
					searchDisplay.getCQLDefinitionsView().getViewCQLAceEditor().clearAnnotations();
					searchDisplay.getCQLDefinitionsView().getViewCQLAceEditor().removeAllMarkers();
					//Commenting below code as its taking away focus and that makes our application not 508 compliant with other fields.
					//searchDisplay.getCQLDefinitionsView().getViewCQLAceEditor().redisplay();
					//508 changes for Definitions Section
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea());
				}
			}

			
		});
		// Definition Erase Icon Functionality
		searchDisplay.getCQLDefinitionsView().getDefineButtonBar().getEraseButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				searchDisplay.getCQLDefinitionsView().getViewCQLAceEditor().clearAnnotations();
				searchDisplay.getCQLDefinitionsView().getViewCQLAceEditor().removeAllMarkers();
				//Commenting below code as its taking away focus and that makes our application not 508 compliant with other fields.
				//searchDisplay.getCQLDefinitionsView().getViewCQLAceEditor().redisplay();
				
				resetViewCQLCollapsiblePanel(searchDisplay.getCQLDefinitionsView().getPanelViewCQLCollapse());
				eraseDefinition(); 
				//508 changes to Definition section
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLDefinitionsView().getDefineAceEditor());
			}
		});
		// Definition Info Icon Functionality
		/*searchDisplay.getCQLDefinitionsView().getDefineButtonBar().getInfoButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlLeftNavBarPanelView().buildInfoPanel((Widget) event.getSource());

			}
		});*/
		// Definition Delete Icon Functionality
		searchDisplay.getDefineButtonBar().getDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				resetViewCQLCollapsiblePanel(searchDisplay.getCQLDefinitionsView().getPanelViewCQLCollapse());
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDeleteConfirmationDefinitionCQLLibraryWorkspace(searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea().getValue()));
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();

			}
		});

		searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});
		
		searchDisplay.getCQLDefinitionsView().getDefineCommentTextArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});

		
		// Definition Add New Functionality
		searchDisplay.getCQLDefinitionsView().getAddNewButtonBar().getaddNewButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				searchDisplay.getCQLDefinitionsView().getViewCQLAceEditor().clearAnnotations();
				searchDisplay.getCQLDefinitionsView().getViewCQLAceEditor().removeAllMarkers();
				//searchDisplay.getCQLDefinitionsView().getViewCQLAceEditor().redisplay();
				
				resetViewCQLCollapsiblePanel(searchDisplay.getCQLDefinitionsView().getPanelViewCQLCollapse());
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
				} else {
					addNewDefinition();
				}
				//508 changes for Definitions Section
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea());
			}
		});
		
		
		searchDisplay.getCQLDefinitionsView().getDefineCommentTextArea().addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCQLDefinitionsView().getDefineCommentGroup().setValidationState(ValidationState.NONE);
				String comment = searchDisplay.getCQLDefinitionsView().getDefineCommentTextArea().getText();
				if(validator.validateForCommentTextArea(comment)){
					searchDisplay.getCQLDefinitionsView().getDefineCommentGroup().setValidationState(ValidationState.ERROR);
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
							MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				}
			}
		});
	}
	
	/**
	 * Adds the new definition.
	 */
	private void addNewDefinition() {

		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
		searchDisplay.getCQLDefinitionsView().getDefineAceEditor().clearAnnotations();
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
		if ((searchDisplay.getCQLDefinitionsView().getDefineAceEditor().getText() != null)) {
			searchDisplay.getCQLDefinitionsView().getDefineAceEditor().setText("");
		}
		if ((searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea() != null)) {
			searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea().setText("");
		}
		
		if ((searchDisplay.getCQLDefinitionsView().getDefineCommentTextArea()!= null)) {
			searchDisplay.getCQLDefinitionsView().getDefineCommentTextArea().setText("");
		}
		searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setText("");
		// Below lines are to clear search suggestion textbox and listbox
		// selection after erase.
		searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestDefineTextBox().setText("");
		if (searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().getSelectedIndex() >= 0) {
			searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().setItemSelected(
					searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().getSelectedIndex(), false);
		}

		// Functionality to reset the disabled features for supplemental data
		// definitions when erased.
		searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea().setEnabled(true);
		searchDisplay.getCQLDefinitionsView().getDefineAceEditor().setReadOnly(false);
		searchDisplay.getCQLDefinitionsView().getContextDefinePATRadioBtn().setEnabled(true);
		searchDisplay.getCQLDefinitionsView().getContextDefinePOPRadioBtn().setEnabled(true);
		searchDisplay.getCQLDefinitionsView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
		searchDisplay.getDefineButtonBar().getSaveButton().setEnabled(true);
		searchDisplay.getDefineButtonBar().getDeleteButton().setEnabled(false);
		searchDisplay.getDefineButtonBar().getInsertButton().setEnabled(true);
		searchDisplay.getDefineButtonBar().getTimingExpButton().setEnabled(true);
		searchDisplay.getCQLDefinitionsView().getContextDefinePATRadioBtn().setValue(true);
		searchDisplay.getCQLDefinitionsView().getContextDefinePOPRadioBtn().setValue(false);
	
		
	}
	
	/**
	 * Adds the function event handlers.
	 */
	private void addFunctionEventHandlers() {
		searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
					DomEvent.fireNativeEvent(
							Document.get()
									.createDblClickEvent(searchDisplay.getCqlLeftNavBarPanelView()
											.getFuncNameListBox().getSelectedIndex(), 0, 0, 0, 0, false, false, false,
											false),
							searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox());
				}

			}
		});
		searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsLoading()) {
					event.stopPropagation();
				} else {
					showSearchBusyOnDoubleClick(true);
					searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().clearAnnotations();
					searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().removeAllMarkers();
					searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(true);
					searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
					
					resetAceEditor(searchDisplay.getCQLFunctionsView().getViewCQLAceEditor());
					resetViewCQLCollapsiblePanel(searchDisplay.getCQLFunctionsView().getPanelViewCQLCollapse());
					
					searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setText("");
					
					
					if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
						showSearchBusyOnDoubleClick(false);
						searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
					} else {
						int selectedIndex = searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox()
								.getSelectedIndex();
						if (selectedIndex != -1) {
							final String selectedFunctionId = searchDisplay.getCqlLeftNavBarPanelView()
									.getFuncNameListBox().getValue(selectedIndex);
							searchDisplay.getCqlLeftNavBarPanelView()
									.setCurrentSelectedFunctionObjId(selectedFunctionId);
							
							if (searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap()
									.get(selectedFunctionId) != null) {
								searchDisplay.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(false);
								searchDisplay.getCQLFunctionsView().setWidgetReadOnly(false);
								// load most recent used cql artifacts
								MatContext.get().getCQLLibraryService().getUsedCqlArtifacts(
										MatContext.get().getCurrentCQLLibraryId(),
										new AsyncCallback<GetUsedCQLArtifactsResult>() {

											@Override
											public void onFailure(Throwable caught) {
												showSearchingBusy(false);
												searchDisplay.getCQLFunctionsView().setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
												Window.alert(
														MatContext.get().getMessageDelegate().getGenericErrorMessage());
											}

											@Override
											public void onSuccess(GetUsedCQLArtifactsResult result) {
												showSearchBusyOnDoubleClick(false);
												searchDisplay.getCQLFunctionsView().getFuncNameTxtArea().setText(searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId).getFunctionName());
												searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().setText(searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId).getFunctionLogic());
												searchDisplay.getCQLFunctionsView().getFunctionCommentTextArea().setText(searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId).getCommentString());

												if (searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId).getContext().equalsIgnoreCase("patient")) {
													searchDisplay.getCQLFunctionsView().getContextFuncPATRadioBtn().setValue(true);
													searchDisplay.getCQLFunctionsView().getContextFuncPOPRadioBtn().setValue(false);
												} else {
													searchDisplay.getCQLFunctionsView().getContextFuncPOPRadioBtn().setValue(true);
													searchDisplay.getCQLFunctionsView().getContextFuncPATRadioBtn().setValue(false);
												}
												CQLFunctions currentFunction = searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId);
												if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
													searchDisplay.getCQLFunctionsView().setWidgetReadOnly(true);
													//Checks if Draft
													if(MatContext.get().getCurrentCQLLibraryVersion().toLowerCase().contains(CQLWorkSpaceConstants.CQL_DRAFT.toLowerCase())){
														searchDisplay.getCQLFunctionsView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
													}
													
													if(!result.getCqlErrors().isEmpty() || !result.getUsedCQLFunctions().contains(currentFunction.getFunctionName())) {
														searchDisplay.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(true);
													} else {
														searchDisplay.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(false);
													}
												}
												Map<String,List<CQLErrors>> expressionCQLErrorMap = result.getCqlErrorsPerExpression();
												if(expressionCQLErrorMap != null){
													List<CQLErrors> errorList = expressionCQLErrorMap.get(currentFunction.getFunctionName());
													searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().clearAnnotations();
													searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().removeAllMarkers();
													for (CQLErrors error : errorList) {
														int startLine = error.getStartErrorInLine();
														int startColumn = error.getStartErrorAtOffset();
														searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().addAnnotation(startLine, startColumn, error.getErrorMessage(), AceAnnotationType.ERROR);
													}
													searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().setAnnotations();
												}
												if(result.getCqlErrors().isEmpty() && result.getExpressionReturnTypeMap() != null){
													searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setText(result.getExpressionReturnTypeMap()
															.get(currentFunction.getFunctionName()));
													searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is "+result.getExpressionReturnTypeMap()
															.get(currentFunction.getFunctionName()));
										
												} else {
													searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setText("");
													searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
												}
											}

										});
								
							} else {
								showSearchBusyOnDoubleClick(false);
							}
						} else {
							showSearchBusyOnDoubleClick(false);
						}
						if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId() != null) {
							CQLFunctions selectedFunction = searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap()
									.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId());
							if (selectedFunction.getArgumentList() != null) {
								searchDisplay.getCQLFunctionsView().getFunctionArgumentList().clear();
								searchDisplay.getCQLFunctionsView().getFunctionArgumentList()
										.addAll(selectedFunction.getArgumentList());
							} else {
								searchDisplay.getCQLFunctionsView().getFunctionArgumentList().clear();
							}
						}
						searchDisplay.resetMessageDisplay();
					}
					searchDisplay.getCQLFunctionsView().createAddArgumentViewForFunctions(
							searchDisplay.getCQLFunctionsView().getFunctionArgumentList(),
							MatContext.get().getLibraryLockService().checkForEditPermission());
					
				}
				//508 changes for Functions Section
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLFunctionsView().getMainFunctionVerticalPanel());
			}
		});
		
		searchDisplay.getCQLFunctionsView().getPanelViewCQLCollapse().addShowHandler(new ShowHandler() {

			@Override
			public void onShow(ShowEvent showEvent) {
				searchDisplay.resetMessageDisplay();
				showCompleteCQL(searchDisplay.getCQLFunctionsView().getViewCQLAceEditor());
			}
		});

		searchDisplay.getCQLFunctionsView().getPanelViewCQLCollapse().addHideHandler(new HideHandler() {

			@Override
			public void onHide(HideEvent hideEvent) {
				searchDisplay.getCQLFunctionsView().getFunctionButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
				resetAceEditor(searchDisplay.getCQLFunctionsView().getViewCQLAceEditor());

			}
		});
		
		
		// Function Insert Icon Functionality
		searchDisplay.getFunctionButtonBar().getInsertButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				buildInsertPopUp();
				//508 changes for Functions Section
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor());
			}
		});

		// Function Save Icon Functionality
		searchDisplay.getCQLFunctionsView().getFunctionButtonBar().getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				resetViewCQLCollapsiblePanel(searchDisplay.getCQLFunctionsView().getPanelViewCQLCollapse());
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					addAndModifyFunction();
					//508 changes for Functions Section
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLFunctionsView().getFuncNameTxtArea());
				}

			}
		});

		// Functions Erase Icon Functionality
		searchDisplay.getCQLFunctionsView().getFunctionButtonBar().getEraseButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				resetViewCQLCollapsiblePanel(searchDisplay.getCQLFunctionsView().getPanelViewCQLCollapse());
				eraseFunction();
				//508 changes for Functions Section
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor());
			}
		});

		// Add Function Argument functionality
		searchDisplay.getCQLFunctionsView().getAddNewArgument().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.hideAceEditorAutoCompletePopUp();
				searchDisplay.getCQLFunctionsView().getFunctionButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
				resetViewCQLCollapsiblePanel(searchDisplay.getCQLFunctionsView().getPanelViewCQLCollapse());
				CQLFunctionArgument addNewFunctionArgument = new CQLFunctionArgument();
				AddFunctionArgumentDialogBox.showArgumentDialogBox(addNewFunctionArgument, false,
						searchDisplay.getCQLFunctionsView(), searchDisplay.getCqlLeftNavBarPanelView(),
						MatContext.get().getLibraryLockService().checkForEditPermission());
				searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
			}
		});

		// Function Info Icon Functionality
		/*searchDisplay.getCQLFunctionsView().getFunctionButtonBar().getInfoButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlLeftNavBarPanelView().buildInfoPanel((Widget) event.getSource());

			}
		});*/

		// Function Delete Icon Functionality
		searchDisplay.getFunctionButtonBar().getDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				resetViewCQLCollapsiblePanel(searchDisplay.getCQLFunctionsView().getPanelViewCQLCollapse());
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDeleteConfirmationFunctionCQLLibraryWorkspace(searchDisplay.getCQLFunctionsView().getFuncNameTxtArea().getValue()));
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
				
				

			}

		});

		searchDisplay.getCQLFunctionsView().setObserver(new Observer() {

			@Override
			public void onModifyClicked(CQLFunctionArgument result) {
				// TODO Auto-generated method stub
				searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				searchDisplay.resetMessageDisplay();
				if (result.getArgumentType().equalsIgnoreCase(CQLWorkSpaceConstants.CQL_MODEL_DATA_TYPE)) {
					getAttributesForDataType(result);
				} else {
					AddFunctionArgumentDialogBox.showArgumentDialogBox(result, true,
							searchDisplay.getCQLFunctionsView(), searchDisplay.getCqlLeftNavBarPanelView(),
							MatContext.get().getLibraryLockService().checkForEditPermission());
				}

			}

			@Override
			public void onDeleteClicked(CQLFunctionArgument result, int index) {
				searchDisplay.getCQLFunctionsView().getFunctionButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
				searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(result.getId());
				searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(result.getArgumentName());
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_FUNCTION_ARGUMENT(result.getArgumentName()));
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
				//508 changes for Functions Section
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLFunctionsView().getMainFunctionVerticalPanel());
			}
		});

		searchDisplay.getCQLFunctionsView().getFuncNameTxtArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});
		
		searchDisplay.getCQLFunctionsView().getFunctionCommentTextArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});
		

		// Function Add New Functionality
		searchDisplay.getCQLFunctionsView().getAddNewButtonBar().getaddNewButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				resetViewCQLCollapsiblePanel(searchDisplay.getCQLFunctionsView().getPanelViewCQLCollapse());
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
				} else {
					addNewFunction();
				}
				//508 changes for Functions Section
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLFunctionsView().getMainFunctionVerticalPanel());
			}
		});
		
		
		searchDisplay.getCQLFunctionsView().getFunctionCommentTextArea().addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCQLFunctionsView().getFuncCommentGroup().setValidationState(ValidationState.NONE);
				String comment = searchDisplay.getCQLFunctionsView().getFunctionCommentTextArea().getText();
				if(validator.validateForCommentTextArea(comment)){
					searchDisplay.getCQLFunctionsView().getFuncCommentGroup().setValidationState(ValidationState.ERROR);
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
							MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				}
			}
		});
	}
	
	/**
	 * Adds the new function.
	 */
	private void addNewFunction() {
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
		searchDisplay.getCQLFunctionsView().getFunctionArgumentList().clear();
		searchDisplay.getCQLFunctionsView().getFunctionArgNameMap().clear();
		searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().clearAnnotations();
		searchDisplay.createAddArgumentViewForFunctions(new ArrayList<CQLFunctionArgument>());
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
		if ((searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().getText() != null)) {
			searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().setText("");
		}
		if ((searchDisplay.getCQLFunctionsView().getFuncNameTxtArea() != null)) {
			searchDisplay.getCQLFunctionsView().getFuncNameTxtArea().setText("");
		}
		
		if ((searchDisplay.getCQLFunctionsView().getFunctionCommentTextArea()!= null)) {
			searchDisplay.getCQLFunctionsView().getFunctionCommentTextArea().setText("");
		}
		searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setText("");
		
		// Below lines are to clear search suggestion textbox and listbox
		// selection after erase.
		searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestFuncTextBox().setText("");
		if (searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().getSelectedIndex() >= 0) {
			searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().setItemSelected(
					searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().getSelectedIndex(), false);
		}
		searchDisplay.getCQLFunctionsView().getContextFuncPATRadioBtn().setValue(true);
		searchDisplay.getCQLFunctionsView().getContextFuncPOPRadioBtn().setValue(false);
		searchDisplay.getCQLFunctionsView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
		searchDisplay.getFunctionButtonBar().getDeleteButton().setEnabled(false);
	}
	
	/**
	 * Adds the warning alert handlers.
	 */
	private void addWarningAlertHandlers() {

		searchDisplay.getCqlLeftNavBarPanelView().getWarningConfirmationYesButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
				searchDisplay.getCqlLeftNavBarPanelView().getWarningConfirmationMessageAlert().clearAlert();
				if (searchDisplay.getCqlLeftNavBarPanelView().isDoubleClick()) {
					clickEventOnListboxes();
				} else if (searchDisplay.getCqlLeftNavBarPanelView().isNavBarClick()) {
					changeSectionSelection();
				} else {
					clearViewIfDirtyNotSet();
				}
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
			}
		});

		searchDisplay.getCqlLeftNavBarPanelView().getWarningConfirmationNoButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getCqlLeftNavBarPanelView().getWarningConfirmationMessageAlert().clearAlert();
				// no was selected, don't move anywhere
				if (searchDisplay.getCqlLeftNavBarPanelView().isNavBarClick()) {
					unsetActiveMenuItem(nextSection);
				}
				if (currentSection.equals(CQLWorkSpaceConstants.CQL_FUNCTION_MENU)) {
					searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().setSelectedIndex(-1);
				} else if (currentSection.equals(CQLWorkSpaceConstants.CQL_PARAMETER_MENU)) {
					searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().setSelectedIndex(-1);
				} else if (currentSection.equals(CQLWorkSpaceConstants.CQL_DEFINE_MENU)) {
					searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().setSelectedIndex(-1);
				}
			}
		});

	}

	/**
	 * Adds the general info event handlers.
	 */
	private void addGeneralInfoEventHandlers() {

		searchDisplay.getCqlGeneralInformationView().getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				if (searchDisplay.getCqlGeneralInformationView().getLibraryNameValue().getText().isEmpty()) {
					searchDisplay.getCqlGeneralInformationView().getLibNameGroup().setValidationState(ValidationState.ERROR);
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
							.createAlert(MatContext.get().getMessageDelegate().getLibraryNameRequired());
				} else {

					if (validator.validateForAliasNameSpecialChar(
							searchDisplay.getCqlGeneralInformationView().getLibraryNameValue().getText().trim())) {
						saveCQLGeneralInformation();
					} else {
						searchDisplay.getCqlGeneralInformationView().getLibNameGroup().setValidationState(ValidationState.ERROR);
						searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
								.createAlert(MatContext.get().getMessageDelegate().getCqlStandAloneLibraryNameError());
					}

				}
			}
		});

		searchDisplay.getCqlGeneralInformationView().getCancelButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlGeneralInformationView().getLibraryNameValue().setText(cqlLibraryName);
			}
		});

		searchDisplay.getCqlGeneralInformationView().getLibraryNameValue().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});
	}

	/**
	 * Event Handlers for Ace Editors.
	 */
	private void addEventHandlerOnAceEditors() {
		searchDisplay.getCQLDefinitionsView().getDefineAceEditor().addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (!searchDisplay.getCQLDefinitionsView().getDefineAceEditor().isReadOnly()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});

		searchDisplay.getCQLParametersView().getParameterAceEditor().addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (!searchDisplay.getCQLParametersView().getParameterAceEditor().isReadOnly()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});

		searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (!searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().isReadOnly()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});
	}

	/**
	 * Adds the event handlers on context radio buttons.
	 */
	private void addEventHandlersOnContextRadioButtons() {
		searchDisplay.getCQLDefinitionsView().getContextDefinePATRadioBtn()
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
						if (searchDisplay.getCQLDefinitionsView().getContextDefinePATRadioBtn().getValue()) {
							searchDisplay.getCQLDefinitionsView().getContextDefinePOPRadioBtn().setValue(false);
						} else {
							searchDisplay.getCQLDefinitionsView().getContextDefinePOPRadioBtn().setValue(true);
						}

					}
				});

		searchDisplay.getCQLDefinitionsView().getContextDefinePOPRadioBtn()
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
						if (searchDisplay.getCQLDefinitionsView().getContextDefinePOPRadioBtn().getValue()) {
							searchDisplay.getCQLDefinitionsView().getContextDefinePATRadioBtn().setValue(false);
						} else {
							searchDisplay.getCQLDefinitionsView().getContextDefinePATRadioBtn().setValue(true);
						}

					}
				});

		searchDisplay.getCQLFunctionsView().getContextFuncPATRadioBtn()
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
						if (searchDisplay.getCQLFunctionsView().getContextFuncPATRadioBtn().getValue()) {
							searchDisplay.getCQLFunctionsView().getContextFuncPOPRadioBtn().setValue(false);
						} else {
							searchDisplay.getCQLFunctionsView().getContextFuncPOPRadioBtn().setValue(true);
						}
					}
				});

		searchDisplay.getCQLFunctionsView().getContextFuncPOPRadioBtn()
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
						if (searchDisplay.getCQLFunctionsView().getContextFuncPOPRadioBtn().getValue()) {
							searchDisplay.getCQLFunctionsView().getContextFuncPATRadioBtn().setValue(false);
						} else {
							searchDisplay.getCQLFunctionsView().getContextFuncPATRadioBtn().setValue(true);
						}
					}
				});
	}

	
	/**
	 * Adds the include CQL library handlers.
	 */
	private void addIncludeCQLLibraryHandlers() {
		
		searchDisplay.getIncludeView().getSaveModifyButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
				searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
				final EditIncludedLibraryDialogBox editIncludedLibraryDialogBox = new EditIncludedLibraryDialogBox("Replace Library");
				editIncludedLibraryDialogBox.findAvailableLibraries(currentIncludeLibrarySetId, currentIncludeLibraryId, false);
				
				editIncludedLibraryDialogBox.getApplyButton().addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent arg0) {
						searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
						searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
						if(editIncludedLibraryDialogBox.getSelectedList().size() > 0){
							final CQLIncludeLibrary toBeModified = searchDisplay.getCqlLeftNavBarPanelView()
									.getIncludeLibraryMap()
									.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedIncLibraryObjId());
							
							final CQLLibraryDataSetObject dto = editIncludedLibraryDialogBox.getSelectedList().get(0);
							if (dto != null) {
								final CQLIncludeLibrary currentObject = new CQLIncludeLibrary(dto);
								
								MatContext.get().getCQLLibraryService().saveIncludeLibrayInCQLLookUp(
										MatContext.get().getCurrentCQLLibraryId(), toBeModified, currentObject,
										searchDisplay.getCqlLeftNavBarPanelView().getViewIncludeLibrarys(),
										new AsyncCallback<SaveUpdateCQLResult>() {

											@Override
											public void onFailure(Throwable arg0) {
												
												editIncludedLibraryDialogBox.getDialogModal().hide();
												editIncludedLibraryDialogBox.getErrorMessageAlert().clearAlert();
												Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
											}

											@Override
											public void onSuccess(SaveUpdateCQLResult result) {
												editIncludedLibraryDialogBox.getErrorMessageAlert().clearAlert();
												if (result != null) {
													if (result.isSuccess()) {
														searchDisplay.getCqlLeftNavBarPanelView().setViewIncludeLibrarys(
																result.getCqlModel().getCqlIncludeLibrarys());
														searchDisplay.getCqlLeftNavBarPanelView().udpateIncludeLibraryMap();
														MatContext.get().setIncludes(getIncludesList(
																result.getCqlModel().getCqlIncludeLibrarys()));
														MatContext.get().setIncludedValueSetNames(
																result.getCqlModel().getIncludedValueSetNames());
														MatContext.get().setIncludedCodeNames(
																result.getCqlModel().getIncludedCodeNames());
														MatContext.get().setIncludedParamNames(
																result.getCqlModel().getIncludedParamNames());
														MatContext.get().setIncludedDefNames(
																result.getCqlModel().getIncludedDefNames());
														MatContext.get().setIncludedFuncNames(
																result.getCqlModel().getIncludedFuncNames());
														editIncludedLibraryDialogBox.getDialogModal().hide();
														DomEvent.fireNativeEvent(
																Document.get()
																		.createDblClickEvent(searchDisplay.getCqlLeftNavBarPanelView()
																				.getIncludesNameListbox().getSelectedIndex(), 0, 0, 0, 0, false, false, false,
																				false),
																searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox());
														String libraryNameWithVersion = result.getIncludeLibrary().getCqlLibraryName() + " v" +result.getIncludeLibrary().getVersion();
														searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
																.createAlert(libraryNameWithVersion + " has been successfully saved as the alias " + result.getIncludeLibrary().getAliasName());
													}
												}

											}

										});

							}
						} else {
							editIncludedLibraryDialogBox.getErrorMessageAlert().clearAlert();
							editIncludedLibraryDialogBox.getErrorMessageAlert()
							.createAlert(MatContext.get().getMessageDelegate().getNO_LIBRARY_TO_REPLACE());
						}


						}
				});
			}
		});
		
		searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
					DomEvent.fireNativeEvent(
							Document.get()
									.createDblClickEvent(searchDisplay.getCqlLeftNavBarPanelView()
											.getIncludesNameListbox().getSelectedIndex(), 0, 0, 0, 0, false, false, false,
											false),
							searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox());
				}

			}
		});
		searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox()
				.addDoubleClickHandler(new DoubleClickHandler() {
					@Override
					public void onDoubleClick(DoubleClickEvent event) {
						if (searchDisplay.getCqlLeftNavBarPanelView().getIsLoading()) {
							event.stopPropagation();
						} else {
							searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(true);
							searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);

							if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
								searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
							} else {
								int selectedIndex = searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox()
										.getSelectedIndex();
								if (selectedIndex != -1) {
									final String selectedIncludeLibraryID = searchDisplay.getCqlLeftNavBarPanelView()
											.getIncludesNameListbox().getValue(selectedIndex);
									searchDisplay.getCqlLeftNavBarPanelView()
											.setCurrentSelectedIncLibraryObjId(selectedIncludeLibraryID);
									if (searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap()
											.get(selectedIncludeLibraryID) != null) {

										MatContext.get().getCQLLibraryService().findCQLLibraryByID(
												searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap()
														.get(selectedIncludeLibraryID).getCqlLibraryId(),
												new AsyncCallback<CQLLibraryDataSetObject>() {

													@Override
													public void onSuccess(CQLLibraryDataSetObject result) {
														if (result != null) {
															currentIncludeLibrarySetId = result.getCqlSetId();
															currentIncludeLibraryId = result.getId();
															searchDisplay.getIncludeView().buildIncludesReadOnlyView();

															searchDisplay.getIncludeView().getAliasNameTxtArea()
																	.setText(searchDisplay.getCqlLeftNavBarPanelView()
																			.getIncludeLibraryMap()
																			.get(selectedIncludeLibraryID)
																			.getAliasName());
															searchDisplay.getIncludeView().getViewCQLEditor()
																	.setText(result.getCqlText());
															searchDisplay.getIncludeView().getOwnerNameTextBox()
																	.setText(searchDisplay.getCqlLeftNavBarPanelView()
																			.getOwnerName(result));
															searchDisplay.getIncludeView().getCqlLibraryNameTextBox()
																	.setText(result.getCqlName());
															//searchDisplay.getIncludeView().addAvailableItems(new ArrayList<String>());

	
															searchDisplay.getIncludeView().getSaveModifyButton().setEnabled(false);														
															searchDisplay.getIncludeView().getDeleteButton().setEnabled(false);

															searchDisplay.getIncludeView().getSaveModifyButton().setEnabled(false);
															if (MatContext.get().getLibraryLockService()
																	.checkForEditPermission()) {
																searchDisplay.getIncludeView().setWidgetReadOnly(false);
																searchDisplay.getIncludeView().getSaveModifyButton().setEnabled(true);

																
																// load most recent
																// used
																// cql artifacts
																MatContext.get().getCQLLibraryService().getUsedCqlArtifacts(
																		MatContext.get().getCurrentCQLLibraryId(),
																		new AsyncCallback<GetUsedCQLArtifactsResult>() {

																			@Override
																			public void onFailure(Throwable caught) {
																				Window.alert(MatContext.get()
																						.getMessageDelegate()
																						.getGenericErrorMessage());
																			}

																			@Override
																			public void onSuccess(
																					GetUsedCQLArtifactsResult result) {

																				CQLIncludeLibrary cqlIncludeLibrary = searchDisplay
																						.getCqlLeftNavBarPanelView()
																						.getIncludeLibraryMap()
																						.get(selectedIncludeLibraryID);
																				
																				// if there are errors or the library is not in use, enable the delete button
																				if(!result.getCqlErrors().isEmpty() || !result.getUsedCQLLibraries().contains(
																						cqlIncludeLibrary.getCqlLibraryName()+ "-"+ cqlIncludeLibrary.getVersion()+ "|"
																								+ cqlIncludeLibrary.getAliasName())) {
																					searchDisplay.getIncludeView().getDeleteButton().setEnabled(true);
																				}
																			}

																		});
															}
														}
													}

													@Override
													public void onFailure(Throwable caught) {
														searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
																.createAlert(MatContext.get().getMessageDelegate()
																		.getGenericErrorMessage());
													}
												});

										searchDisplay.getIncludeView().setSelectedObject(
												searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap()
														.get(selectedIncludeLibraryID).getCqlLibraryId());
										searchDisplay.getIncludeView()
												.setIncludedList(searchDisplay.getCqlLeftNavBarPanelView()
														.getIncludedList(searchDisplay.getCqlLeftNavBarPanelView()
																.getIncludeLibraryMap()));
										searchDisplay.getIncludeView().getSelectedObjectList().clear();
									}
								}
								searchDisplay.resetMessageDisplay();

							}

						}

					}
				});

		// Includes search Button Functionality
		searchDisplay.getIncludeView().getSearchButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getAllIncludeLibraryList(searchDisplay.getIncludeView().getSearchTextBox().getText().trim());
				// 508 changes for Library Alias.
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getIncludeView().getAliasNameTxtArea());
			}
		});

		// Includes search Button Focus Functionality
		searchDisplay.getIncludeView().getFocusPanel().addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				// Search when enter is pressed.
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					searchDisplay.getIncludeView().getSearchButton().click();
				}
			}
		});

		// Includes Save Functionality
		searchDisplay.getIncludeView().getIncludesButtonBar().getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					addIncludeLibraryInCQLLookUp();
					// 508 changes for Library Alias.
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getIncludeView().getAliasNameTxtArea());
				}
			}
		});

		// Includes Delete Functionality
		searchDisplay.getIncludeView().getDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_INCLUDE(searchDisplay.getIncludeView().getAliasNameTxtArea().getValue()));
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
				// 508 changes for Library Alias.
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getIncludeView().getAliasNameTxtArea());
				//currentIncludeLibrarySetId = null;
			}

		});

		// Includes close Functionality
		searchDisplay.getIncludeView().getCloseButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				// Below lines are to clear search suggestion textbox and
				// listbox
				// selection after erase.
				currentIncludeLibrarySetId =  null;
				searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestIncludeTextBox().setText("");
				if (searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex() >= 0) {
					searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().setItemSelected(
							searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex(),
							false);
				}
				searchDisplay.getIncludeView().getAliasNameTxtArea().setText("");
				searchDisplay.buildIncludesView();
				SaveCQLLibraryResult cqlLibrarySearchModel = new SaveCQLLibraryResult();
				cqlLibrarySearchModel
						.setCqlLibraryDataSetObjects(searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryList());
				searchDisplay.getIncludeView().setIncludedList(searchDisplay.getCqlLeftNavBarPanelView()
						.getIncludedList(searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap()));
				searchDisplay.getIncludeView().buildIncludeLibraryCellTable(cqlLibrarySearchModel,
						MatContext.get().getLibraryLockService().checkForEditPermission(), false);
				searchDisplay.getIncludeView()
						.setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
				if (searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox()
						.getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
					searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
							.createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
				} else {
					searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().clearAlert();
				}
				// 508 changes for Library Alias.
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getIncludeView().getAliasNameTxtArea());
			}
		});

		// Includes Erase Functionality
		searchDisplay.getIncludeView().getEraseButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				clearAlias();
				if (searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox()
						.getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
					searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
							.createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
				} else {
					searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().clearAlert();
				}
				// 508 changes for Library Alias.
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getIncludeView().getAliasNameTxtArea());
			}
		});

		// Includes Celltable Observer for CheckBox
		searchDisplay.getIncludeView().setObserver(new CQLIncludeLibraryView.Observer() {

			@Override
			public void onCheckBoxClicked(CQLLibraryDataSetObject result) {
				MatContext.get().getCQLLibraryService().findCQLLibraryByID(result.getId(),
						new AsyncCallback<CQLLibraryDataSetObject>() {

							@Override
							public void onFailure(Throwable caught) {
								Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
								Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
							}

							@Override
							public void onSuccess(CQLLibraryDataSetObject result) {
								searchDisplay.getIncludeView().getViewCQLEditor().setText(result.getCqlText());
							}
						});
			}
		});
	}

	/**
	 * Adds and modify function.
	 */
	protected void addAndModifyFunction() {
		searchDisplay.resetMessageDisplay();
		final String functionName = searchDisplay.getCQLFunctionsView().getFuncNameTxtArea().getText();
		String functionBody = searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().getText();
		String functionComment = searchDisplay.getCQLFunctionsView().getFunctionCommentTextArea().getText();
		String funcContext = "";
		if (searchDisplay.getCQLFunctionsView().getContextFuncPATRadioBtn().getValue()) {
			funcContext = "Patient";
		} else {
			funcContext = "Population";
		}
		if (!functionName.isEmpty()) {
			if (validator.validateForSpecialChar(functionName.trim())) {
				searchDisplay.getCQLFunctionsView().getFuncNameGroup().setValidationState(ValidationState.ERROR);
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getERROR_FUNCTION_NAME_NO_SPECIAL_CHAR());
				searchDisplay.getCQLFunctionsView().getFuncNameTxtArea().setText(functionName.trim());
			
				
			} else if(validator.validateForCommentTextArea(functionComment)){
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
				.createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				searchDisplay.getCQLFunctionsView().getFuncCommentGroup().setValidationState(ValidationState.ERROR);
			} else {
				CQLFunctions function = new CQLFunctions();
				function.setFunctionLogic(functionBody);
				function.setFunctionName(functionName);
				function.setCommentString(functionComment);
				function.setArgumentList(searchDisplay.getCQLFunctionsView().getFunctionArgumentList());
				function.setContext(funcContext);
				CQLFunctions toBeModifiedParamObj = null;

				if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId() != null) {
					toBeModifiedParamObj = searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap()
							.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId());
				}
				showSearchingBusy(true);
				MatContext.get().getCQLLibraryService().saveAndModifyFunctions(
						MatContext.get().getCurrentCQLLibraryId(), toBeModifiedParamObj, function,
						searchDisplay.getCqlLeftNavBarPanelView().getViewFunctions(),
						isFormatable, new AsyncCallback<SaveUpdateCQLResult>() {

							@Override
							public void onFailure(Throwable caught) {
								searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
								searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
								searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
								searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
										.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
								showSearchingBusy(false);
							}

							@Override
							public void onSuccess(SaveUpdateCQLResult result) {
								if (result != null) {
									if (result.isSuccess()) {

										searchDisplay.getCqlLeftNavBarPanelView()
												.setViewFunctions(result.getCqlModel().getCqlFunctions());
										MatContext.get()
												.setFuncs(getFunctionList(result.getCqlModel().getCqlFunctions()));
										searchDisplay.getCqlLeftNavBarPanelView()
												.setCurrentSelectedFunctionObjId(result.getFunction().getId());
										searchDisplay.getCqlLeftNavBarPanelView().clearAndAddFunctionsNamesToListBox();
										searchDisplay.getCqlLeftNavBarPanelView().updateFunctionMap();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
												.setVisible(true);

										searchDisplay.getCQLFunctionsView().getFuncNameTxtArea()
												.setText(result.getFunction().getFunctionName());
										searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor()
												.setText(result.getFunction().getFunctionLogic());
										searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
										searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor()
												.clearAnnotations();
										searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor()
												.removeAllMarkers();
										//Commenting below code as its taking away focus and that makes our application not 508 compliant with other fields.
										//searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().redisplay();

										if (validateCQLArtifact(result, currentSection)) {
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
													.clearAlert();
											searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getSUCESS_FUNCTION_MODIFY_WITH_ERRORS(functionName.trim()));
											searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setText("");
											searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");

										} else if (!result.isDatatypeUsedCorrectly()) {
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
													.clearAlert();
											searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getWarningBadDataTypeCombination());
											if(result.isValidCQLWhileSavingExpression()){
												searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setText(result.getFunction().getReturnType());
												searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is " + result.getFunction().getReturnType());
											} else {
												searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setText("");
												searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
											}
										} else {
											searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
													.clearAlert();
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getSUCESS_FUNCTION_MODIFY(functionName.trim()));
											if(result.isValidCQLWhileSavingExpression()){
												searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setText(result.getFunction().getReturnType());
												searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is " + result.getFunction().getReturnType());
											} else {
												searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setText("");
												searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
											}
										}

										searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().setAnnotations();
										searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().redisplay();

									} else if (result.getFailureReason() == 1) {
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert(MatContext.get().getMessageDelegate()
														.getERROR_DUPLICATE_IDENTIFIER_NAME());
										searchDisplay.getCQLFunctionsView().getFuncNameTxtArea()
												.setText(functionName.trim());
									} else if (result.getFailureReason() == 2) {
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert("Unable to find Node to modify.");
										searchDisplay.getCQLFunctionsView().getFuncNameTxtArea()
												.setText(functionName.trim());
									} else if (result.getFailureReason() == 3) {
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert(MatContext.get().getMessageDelegate()
														.getERROR_FUNCTION_NAME_NO_SPECIAL_CHAR());
										searchDisplay.getCQLFunctionsView().getFuncNameTxtArea()
												.setText(functionName.trim());
										if (result.getFunction() != null) {
											searchDisplay.createAddArgumentViewForFunctions(
													result.getFunction().getArgumentList());
										}
									} else if (result.getFailureReason() == 6) {
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get()
												.getMessageDelegate().getCqlFunctionArgumentNameError());
									} else if (result.getFailureReason() == 8) {
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get()
												.getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
									}

								}
								showSearchingBusy(false);
								
								// if there are cql errors enable the delete button
								if(!result.getCqlErrors().isEmpty()) {
									searchDisplay.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(true);
								}
								
								else {
									// if the function is in use, disable the delete button
									if(result.getUsedCQLArtifacts().getUsedCQLFunctions().contains(functionName)) {
										searchDisplay.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(false);
									}
									
									else {
										searchDisplay.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(true);
									}
									
								}
								
								
							}
						});

			}

		} else {
			searchDisplay.getCQLFunctionsView().getFuncNameGroup().setValidationState(ValidationState.ERROR);
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getERROR_SAVE_CQL_FUNCTION());
			searchDisplay.getCQLFunctionsView().getFuncNameTxtArea().setText(functionName.trim());
		}

	}

	/**
	 * Adds the and modify parameters.
	 */
	private void addAndModifyParameters() {

		searchDisplay.resetMessageDisplay();
		final String parameterName = searchDisplay.getCQLParametersView().getParameterNameTxtArea().getText();
		String parameterLogic = searchDisplay.getCQLParametersView().getParameterAceEditor().getText();
		String parameterComment = searchDisplay.getCQLParametersView().getParameterCommentTextArea().getText();
		if (!parameterName.isEmpty()) {

			if (validator.validateForSpecialChar(parameterName.trim())) {
				searchDisplay.getCQLParametersView().getParamNameGroup().setValidationState(ValidationState.ERROR);
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getERROR_PARAMETER_NAME_NO_SPECIAL_CHAR());
				searchDisplay.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());
			} else if(validator.validateForCommentTextArea(parameterComment)){
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
				.createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				searchDisplay.getCQLParametersView().getParamCommentGroup().setValidationState(ValidationState.ERROR);
				
			} else {
				CQLParameter parameter = new CQLParameter();
				parameter.setParameterLogic(parameterLogic);
				parameter.setParameterName(parameterName);
				parameter.setCommentString(parameterComment);
				CQLParameter toBeModifiedParamObj = null;

				if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedParamerterObjId() != null) {
					toBeModifiedParamObj = searchDisplay.getCqlLeftNavBarPanelView().getParameterMap()
							.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedParamerterObjId());
				}
				showSearchingBusy(true);
				MatContext.get().getCQLLibraryService().saveAndModifyParameters(
						MatContext.get().getCurrentCQLLibraryId(), toBeModifiedParamObj, parameter,
						searchDisplay.getCqlLeftNavBarPanelView().getViewParameterList(),
						isFormatable, new AsyncCallback<SaveUpdateCQLResult>() {

							@Override
							public void onFailure(Throwable caught) {
								searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedParamerterObjId(null);
								searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
										.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
								showSearchingBusy(false);
							}

							@Override
							public void onSuccess(SaveUpdateCQLResult result) {
								if (result != null) {
									if (result.isSuccess()) {
										searchDisplay.getCqlLeftNavBarPanelView()
												.setViewParameterList(result.getCqlModel().getCqlParameters());
										MatContext.get().setParameters(
												getParamaterList(result.getCqlModel().getCqlParameters()));
										searchDisplay.getCqlLeftNavBarPanelView()
												.setCurrentSelectedParamerterObjId(result.getParameter().getId());
										searchDisplay.getCqlLeftNavBarPanelView().clearAndAddParameterNamesToListBox();
										searchDisplay.getCqlLeftNavBarPanelView().updateParamMap();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
										searchDisplay.getCQLParametersView().getParameterNameTxtArea()
												.setText(result.getParameter().getParameterName());
										searchDisplay.getCQLParametersView().getParameterAceEditor()
												.setText(result.getParameter().getParameterLogic());
										searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
										searchDisplay.getCQLParametersView().getParameterAceEditor().clearAnnotations();
										searchDisplay.getCQLParametersView().getParameterAceEditor().removeAllMarkers();
										//Commenting below code as its taking away focus and that makes our application not 508 compliant with other fields.
										//searchDisplay.getCQLParametersView().getParameterAceEditor().redisplay();

										if (validateCQLArtifact(result, currentSection)) {
											searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getSUCESS_PARAMETER_MODIFY_WITH_ERRORS(parameterName.trim()));

										} else if (!result.isDatatypeUsedCorrectly()) {
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
													.clearAlert();
											searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getWarningBadDataTypeCombination());
										} else {
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getSUCESS_PARAMETER_MODIFY(parameterName.trim()));
										}

										

										searchDisplay.getCQLParametersView().getParameterAceEditor().setAnnotations();
										searchDisplay.getCQLParametersView().getParameterAceEditor().redisplay();

									} else if (result.getFailureReason() == 1) {
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert(MatContext.get().getMessageDelegate()
														.getERROR_DUPLICATE_IDENTIFIER_NAME());
										searchDisplay.getCQLParametersView().getParameterNameTxtArea()
												.setText(parameterName.trim());
									} else if (result.getFailureReason() == 2) {
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert("Unable to find Node to modify.");
										searchDisplay.getCQLParametersView().getParameterNameTxtArea()
												.setText(parameterName.trim());
									} else if (result.getFailureReason() == 3) {
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert(MatContext.get().getMessageDelegate()
														.getERROR_PARAMETER_NAME_NO_SPECIAL_CHAR());
										searchDisplay.getCQLParametersView().getParameterNameTxtArea()
												.setText(parameterName.trim());
									} else if (result.getFailureReason() == 8) {
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get()
												.getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
									}
								}
								showSearchingBusy(false);
								
								// if there are errors, enable the parameter delete button
								if (!result.getCqlErrors().isEmpty()) {
									searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(true);
								}

								else {
									// if the saved parameter is in use, then disable the delete button
									if (result.getUsedCQLArtifacts().getUsedCQLParameters().contains(parameterName)) {
										searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(false);
									}

									else {
										searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(true);
									}
								}

							}
						});

			}

		} else {
			searchDisplay.getCQLParametersView().getParamNameGroup().setValidationState(ValidationState.ERROR);
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getERROR_SAVE_CQL_PARAMETER());
			searchDisplay.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());
		}

	}

	/**
	 * This method is called to Add/Modify Definitions into Library Xml.
	 * 
	 */
	private void addAndModifyDefintions() {

		searchDisplay.resetMessageDisplay();
		final String definitionName = searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea().getText();
		String definitionLogic = searchDisplay.getCQLDefinitionsView().getDefineAceEditor().getText();
		String definitionComment = searchDisplay.getCQLDefinitionsView().getDefineCommentTextArea().getText();
		String defineContext = "";
		if (searchDisplay.getCQLDefinitionsView().getContextDefinePATRadioBtn().getValue()) {
			defineContext = "Patient";
		} else {
			defineContext = "Population";
		}
		if (!definitionName.isEmpty()) {

			if (validator.validateForSpecialChar(definitionName.trim())) {

				searchDisplay.getCQLDefinitionsView().getDefineNameGroup().setValidationState(ValidationState.ERROR);
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getERROR_DEFINITION_NAME_NO_SPECIAL_CHAR());
				searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
			} else if(validator.validateForCommentTextArea(definitionComment)){
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
				.createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				searchDisplay.getCQLDefinitionsView().getDefineCommentGroup().setValidationState(ValidationState.ERROR);
		
			} else {
				final CQLDefinition define = new CQLDefinition();
				define.setDefinitionName(definitionName);
				define.setDefinitionLogic(definitionLogic);
				define.setCommentString(definitionComment);
				define.setContext(defineContext);
				CQLDefinition toBeModifiedObj = null;

				if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId() != null) {
					toBeModifiedObj = searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap()
							.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId());
				}
				showSearchingBusy(true);
				MatContext.get().getCQLLibraryService().saveAndModifyDefinitions(
						MatContext.get().getCurrentCQLLibraryId(), toBeModifiedObj, define,
						searchDisplay.getCqlLeftNavBarPanelView().getViewDefinitions(),
						isFormatable, new AsyncCallback<SaveUpdateCQLResult>() {

							@Override
							public void onFailure(Throwable caught) {
								searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
								searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
										.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
								showSearchingBusy(false);
							}

							@Override
							public void onSuccess(SaveUpdateCQLResult result) {
								if (result != null) {
									if (result.isSuccess()) {
										searchDisplay.getCqlLeftNavBarPanelView()
												.setViewDefinitions(result.getCqlModel().getDefinitionList());
										MatContext.get().setDefinitions(
												getDefinitionList(result.getCqlModel().getDefinitionList()));
										searchDisplay.getCqlLeftNavBarPanelView()
												.setCurrentSelectedDefinitionObjId(result.getDefinition().getId());
										searchDisplay.getCqlLeftNavBarPanelView().clearAndAddDefinitionNamesToListBox();
										searchDisplay.getCqlLeftNavBarPanelView().updateDefineMap();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
										searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea()
												.setText(result.getDefinition().getDefinitionName());
										searchDisplay.getCQLDefinitionsView().getDefineAceEditor()
												.setText(result.getDefinition().getDefinitionLogic());
										searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
										searchDisplay.getCQLDefinitionsView().getDefineAceEditor().clearAnnotations();
										searchDisplay.getCQLDefinitionsView().getDefineAceEditor().removeAllMarkers();
										//Commenting below code as its taking away focus and that makes our application not 508 compliant with other fields.
										//searchDisplay.getCQLDefinitionsView().getDefineAceEditor().redisplay();
										searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setText(result.getDefinition().getReturnType());
										if (validateCQLArtifact(result, currentSection)) {
											searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getSUCESS_DEFINITION_MODIFY_WITH_ERRORS(definitionName.trim()));
											searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setText("");
											searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
										} else if (!result.isDatatypeUsedCorrectly()) {
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
													.clearAlert();
											searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getWarningBadDataTypeCombination());
											if(result.isValidCQLWhileSavingExpression()){
												searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setText(result.getDefinition().getReturnType());
												searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is "+result.getDefinition().getReturnType());
											} else {
												searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setText("");
												searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
											}
											
										} else {
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getSUCESS_DEFINITION_MODIFY(definitionName.trim()));
											if(result.isValidCQLWhileSavingExpression()){
												searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setText(result.getDefinition().getReturnType());
												searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is "+result.getDefinition().getReturnType());
											} else {
												searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setText("");
												searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
												
											}
										}

										searchDisplay.getCQLDefinitionsView().getDefineAceEditor().setAnnotations();
										searchDisplay.getCQLDefinitionsView().getDefineAceEditor().redisplay();

									} else {
										if (result.getFailureReason() == 1) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getERROR_DUPLICATE_IDENTIFIER_NAME());
											searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea()
													.setText(definitionName.trim());
										} else if (result.getFailureReason() == 2) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
													.createAlert("Unable to find Node to modify.");
											searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea()
													.setText(definitionName.trim());
										} else if (result.getFailureReason() == 3) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getERROR_DEFINITION_NAME_NO_SPECIAL_CHAR());
											searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea()
													.setText(definitionName.trim());
										} else if (result.getFailureReason() == 8) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get()
													.getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
										}
									}

								}
								showSearchingBusy(false);
								
								// if there are errors, enable the
								// definition button
								if (!result.getCqlErrors().isEmpty()) {
									searchDisplay.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton()
											.setEnabled(true);
								}
				
								else {
									// if the saved definition is in
									// use, then disable the delete
									// button
									if (result.getUsedCQLArtifacts().getUsedCQLDefinitions().contains(definitionName)) {
										searchDisplay.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(false);
									}
			
									else {
										searchDisplay.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(true);
									}
								}

							}
						});

			}

		} else {
			searchDisplay.getCQLDefinitionsView().getDefineNameGroup().setValidationState(ValidationState.ERROR);
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getERROR_SAVE_CQL_DEFINITION());
			searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
		}

	}

	/**
	 * Adds the include library in CQL look up.
	 */
	private void addIncludeLibraryInCQLLookUp() {
		searchDisplay.resetMessageDisplay();
		if (searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox()
				.getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
			searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
			return;

		} else {
			searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().clearAlert();
		}
		final String aliasName = searchDisplay.getIncludeView().getAliasNameTxtArea().getText();

		if (!aliasName.isEmpty() && searchDisplay.getIncludeView().getSelectedObjectList().size() > 0) {
			// functioanlity to add Include Library
			CQLLibraryDataSetObject cqlLibraryDataSetObject = searchDisplay.getIncludeView().getSelectedObjectList()
					.get(0);

			if (validator.validateForAliasNameSpecialChar(aliasName.trim())) {

				CQLIncludeLibrary incLibrary = new CQLIncludeLibrary();
				incLibrary.setAliasName(aliasName);
				incLibrary.setCqlLibraryId(cqlLibraryDataSetObject.getId());
				String versionValue = cqlLibraryDataSetObject.getVersion().replace("v", "") + "."
						+ cqlLibraryDataSetObject.getRevisionNumber();
				incLibrary.setVersion(versionValue);
				incLibrary.setCqlLibraryName(cqlLibraryDataSetObject.getCqlName());
				incLibrary.setQdmVersion(cqlLibraryDataSetObject.getQdmVersion());
				incLibrary.setSetId(cqlLibraryDataSetObject.getCqlSetId());
				if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedIncLibraryObjId() == null) {
					// this is just to add include library and not modify
					MatContext.get().getCQLLibraryService().saveIncludeLibrayInCQLLookUp(
							MatContext.get().getCurrentCQLLibraryId(), null, incLibrary,
							searchDisplay.getCqlLeftNavBarPanelView().getViewIncludeLibrarys(),
							new AsyncCallback<SaveUpdateCQLResult>() {

								@Override
								public void onFailure(Throwable caught) {
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
											MatContext.get().getMessageDelegate().getGenericErrorMessage());

								}

								@Override
								public void onSuccess(SaveUpdateCQLResult result) {
									if (result != null) {
										if (result.isSuccess()) {
											searchDisplay.resetMessageDisplay();
											searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
											searchDisplay.getCqlLeftNavBarPanelView().setViewIncludeLibrarys(
													result.getCqlModel().getCqlIncludeLibrarys());
											MatContext.get().setIncludes(
													getIncludesList(result.getCqlModel().getCqlIncludeLibrarys()));
											searchDisplay.getCqlLeftNavBarPanelView().clearAndAddAliasNamesToListBox();
											searchDisplay.getCqlLeftNavBarPanelView().udpateIncludeLibraryMap();
											searchDisplay.getIncludeView()
													.setIncludedList(searchDisplay.getCqlLeftNavBarPanelView()
															.getIncludedList(searchDisplay.getCqlLeftNavBarPanelView()
																	.getIncludeLibraryMap()));
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getIncludeLibrarySuccessMessage(
																	result.getIncludeLibrary().getAliasName()));
											clearAlias();
											MatContext.get().setIncludedValueSetNames(result.getCqlModel().getIncludedValueSetNames());
											MatContext.get().setIncludedCodeNames(result.getCqlModel().getIncludedCodeNames());
											//MatContext.get().getIncludedValueSetNames().addAll(result.getCqlModel().getIncludedCodeNames());
											MatContext.get().setIncludedParamNames(result.getCqlModel().getIncludedParamNames());
											MatContext.get().setIncludedDefNames(result.getCqlModel().getIncludedDefNames());
											MatContext.get().setIncludedFuncNames(result.getCqlModel().getIncludedFuncNames());
											if (searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox()
													.getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
												searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
														.createAlert(MatContext.get().getMessageDelegate()
																.getCqlLimitWarningMessage());
											} else {
												searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
														.clearAlert();
											}

										} else if (result.getFailureReason() == 1) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getERROR_INCLUDE_ALIAS_NAME_NO_SPECIAL_CHAR());
											searchDisplay.getAliasNameTxtArea().setText(aliasName.trim());
										} else if (result.getFailureReason() == 2) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
													.createAlert("Missing includes library tag.");
											searchDisplay.getAliasNameTxtArea().setText(aliasName.trim());
										} else if (result.getFailureReason() == 3) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getERROR_INCLUDE_ALIAS_NAME_NO_SPECIAL_CHAR());
											searchDisplay.getAliasNameTxtArea().setText(aliasName.trim());
										}
									}
								}
							});
				}

			} else {
				searchDisplay.getCqlIncludeLibraryView().getAliasNameGroup().setValidationState(ValidationState.ERROR);
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
						MatContext.get().getMessageDelegate().getERROR_INCLUDE_ALIAS_NAME_NO_SPECIAL_CHAR());
				searchDisplay.getAliasNameTxtArea().setText(aliasName.trim());
			}

		} else {
			searchDisplay.getCqlIncludeLibraryView().getAliasNameGroup().setValidationState(ValidationState.ERROR);
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getSAVE_INCLUDE_LIBRARY_VALIATION_ERROR());
		}
	}
	
	/**
	 * Clears the ace editor for parameters. Functionality for the eraser icon in parameter section. 
	 */
	private void eraseParameter() {
		
		searchDisplay.getCQLParametersView().getParameterAceEditor().clearAnnotations();
		if(searchDisplay.getCQLParametersView().getParameterAceEditor().getText() != null) {
			searchDisplay.getCQLParametersView().getParameterAceEditor().setText("");
			searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
		}
		
	}
	
	/**
	 * Clears the ace editor for definitions. Functionality for the eraser icon in definition section. 
	 */
	private void eraseDefinition() {
		
		searchDisplay.getCQLDefinitionsView().getDefineAceEditor().clearAnnotations();
		if ((searchDisplay.getCQLDefinitionsView().getDefineAceEditor().getText() != null)) {
			searchDisplay.getCQLDefinitionsView().getDefineAceEditor().setText("");
			searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
		}
	}
	
	/**
	 * Clears the ace editor for functions. Functionality for the eraser icon in function section. 
	 */
	private void eraseFunction() {
		
		searchDisplay.getCQLParametersView().getParameterAceEditor().clearAnnotations();
		if ((searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().getText() != null)) {
			searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().setText("");
			searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
		}
	}

	/**
	 * This method Clears parameter view on Erase Button click when isPageDirty
	 * is not set.
	 */
	private void clearParameter() {
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedParamerterObjId(null);
		searchDisplay.getCQLParametersView().getParameterAceEditor().clearAnnotations();
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
		if ((searchDisplay.getCQLParametersView().getParameterAceEditor().getText() != null)) {
			searchDisplay.getCQLParametersView().getParameterAceEditor().setText("");
		}
		if ((searchDisplay.getCQLParametersView().getParameterNameTxtArea() != null)) {
			searchDisplay.getCQLParametersView().getParameterNameTxtArea().setText("");
		}

		if ((searchDisplay.getCQLParametersView().getParameterCommentTextArea() != null)) {
			searchDisplay.getCQLParametersView().getParameterCommentTextArea().setText("");
		}
		
		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			searchDisplay.getCQLParametersView()
					.setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
				searchDisplay.getCQLParametersView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
		}
		// Below lines are to clear search suggestion textbox and listbox
		// selection after erase.
		searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestParamTextBox().setText("");
		if (searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().getSelectedIndex() >= 0) {
			searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().setItemSelected(
					searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().getSelectedIndex(), false);
		}

		searchDisplay.getParameterButtonBar().getDeleteButton().setEnabled(false);
	}

	/**
	 * This method Clears Definition view on Erase Button click when isPageDirty
	 * is not set.
	 */
	private void clearDefinition() {
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
		searchDisplay.getCQLDefinitionsView().getDefineAceEditor().clearAnnotations();
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
		if ((searchDisplay.getCQLDefinitionsView().getDefineAceEditor().getText() != null)) {
			searchDisplay.getCQLDefinitionsView().getDefineAceEditor().setText("");
		}
		if ((searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea() != null)) {
			searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea().setText("");
		}
		if ((searchDisplay.getCQLDefinitionsView().getDefineCommentTextArea() != null)) {
			searchDisplay.getCQLDefinitionsView().getDefineCommentTextArea().setText("");
		}
		searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setText("");
		
		// Below lines are to clear search suggestion textbox and listbox
		// selection after erase.
		searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestDefineTextBox().setText("");
		if (searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().getSelectedIndex() >= 0) {
			searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().setItemSelected(
					searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().getSelectedIndex(), false);
		}

		// Functionality to reset the disabled features for supplemental data
		// definitions when erased.
		searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea().setEnabled(true);
		searchDisplay.getCQLDefinitionsView().getDefineAceEditor().setReadOnly(false);
		searchDisplay.getCQLDefinitionsView().getContextDefinePATRadioBtn().setEnabled(true);
		searchDisplay.getCQLDefinitionsView().getContextDefinePOPRadioBtn().setEnabled(true);
		searchDisplay.getCQLDefinitionsView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
		searchDisplay.getDefineButtonBar().getSaveButton().setEnabled(true);
		searchDisplay.getDefineButtonBar().getDeleteButton().setEnabled(false);
		searchDisplay.getDefineButtonBar().getInsertButton().setEnabled(true);
		searchDisplay.getDefineButtonBar().getTimingExpButton().setEnabled(true);
		searchDisplay.getCQLDefinitionsView().getContextDefinePATRadioBtn().setValue(true);
		searchDisplay.getCQLDefinitionsView().getContextDefinePOPRadioBtn().setValue(false);
	}

	/**
	 * This method Clears Function view on Erase Button click when isPageDirty
	 * is not set.
	 */
	private void clearFunction() {
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
		searchDisplay.getCQLFunctionsView().getFunctionArgumentList().clear();
		searchDisplay.getCQLFunctionsView().getFunctionArgNameMap().clear();
		searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().clearAnnotations();
		searchDisplay.createAddArgumentViewForFunctions(new ArrayList<CQLFunctionArgument>());
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
		if ((searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().getText() != null)) {
			searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().setText("");
		}
		if ((searchDisplay.getCQLFunctionsView().getFuncNameTxtArea() != null)) {
			searchDisplay.getCQLFunctionsView().getFuncNameTxtArea().setText("");
		}
		if ((searchDisplay.getCQLFunctionsView().getFunctionCommentTextArea() != null)) {
			searchDisplay.getCQLFunctionsView().getFunctionCommentTextArea().setText("");
		}
		
		searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setText("");
		
		// Below lines are to clear search suggestion textbox and listbox
		// selection after erase.
		searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestFuncTextBox().setText("");
		if (searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().getSelectedIndex() >= 0) {
			searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().setItemSelected(
					searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().getSelectedIndex(), false);
		}
		searchDisplay.getCQLFunctionsView().getContextFuncPATRadioBtn().setValue(true);
		searchDisplay.getCQLFunctionsView().getContextFuncPOPRadioBtn().setValue(false);
		searchDisplay.getFunctionButtonBar().getDeleteButton().setEnabled(false);
		searchDisplay.getCQLFunctionsView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
	}

	/**
	 * Builds the insert pop up.
	 */
	/*
	 * Build Insert Pop up.
	 */
	private void buildInsertPopUp() {
		searchDisplay.resetMessageDisplay();
		InsertIntoAceEditorDialogBox
				.showListOfItemAvailableForInsertDialogBox(searchDisplay.getCqlLeftNavBarPanelView(), curAceEditor);
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
	}

	/**
	 * Delete definition.
	 */
	protected void deleteDefinition() {
		searchDisplay.resetMessageDisplay();
		final String definitionName = searchDisplay.getDefineNameTxtArea().getText();
		/*String definitionLogic = searchDisplay.getDefineAceEditor().getText();
		String defineContext = "";
		if (searchDisplay.getContextDefinePATRadioBtn().getValue()) {
			defineContext = "Patient";
		} else {
			defineContext = "Population";
		}
		if (!definitionName.isEmpty()) {

			if (!validator.validateForSpecialChar(definitionName.trim())) {

				final CQLDefinition define = new CQLDefinition();
				define.setDefinitionName(definitionName);
				define.setDefinitionLogic(definitionLogic);
				define.setContext(defineContext);*/

			if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId() != null) {
				final CQLDefinition toBeModifiedObj = searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap()
						.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId());
				showSearchingBusy(true);
				MatContext.get().getCQLLibraryService().deleteDefinition(MatContext.get().getCurrentCQLLibraryId(),
						toBeModifiedObj, searchDisplay.getCqlLeftNavBarPanelView().getViewDefinitions(),
						new AsyncCallback<SaveUpdateCQLResult>() {

							@Override
							public void onFailure(Throwable caught) {
								searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
								searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
										MatContext.get().getMessageDelegate().getGenericErrorMessage());
								showSearchingBusy(false);
							}

							@Override
							public void onSuccess(SaveUpdateCQLResult result) {
								if (result != null) {
									if (result.isSuccess()) {
										searchDisplay.getCqlLeftNavBarPanelView()
												.setViewDefinitions(result.getCqlModel().getDefinitionList());
										MatContext.get().setDefinitions(
												getDefinitionList(result.getCqlModel().getDefinitionList()));
										searchDisplay.getCqlLeftNavBarPanelView()
												.clearAndAddDefinitionNamesToListBox();
										searchDisplay.getCqlLeftNavBarPanelView().updateDefineMap();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
												.setVisible(true);

										searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestDefineTextBox()
												.setText("");
										searchDisplay.getDefineNameTxtArea().setText("");
										searchDisplay.getDefineAceEditor().setText("");
										searchDisplay.getCqlLeftNavBarPanelView()
												.setCurrentSelectedDefinitionObjId(null);
										searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
										searchDisplay.getDefineAceEditor().clearAnnotations();
										searchDisplay.getDefineAceEditor().removeAllMarkers();
										//Commenting below code as its taking away focus and that makes our application not 508 compliant with other fields.
										//searchDisplay.getDefineAceEditor().redisplay();
										searchDisplay.getDefineAceEditor().setAnnotations();
										searchDisplay.getDefineButtonBar().getDeleteButton().setEnabled(false);
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
												.createAlert(MatContext.get().getMessageDelegate().getSuccessfulDefinitionRemoveMessage(toBeModifiedObj.getDefinitionName()));
										searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setText("");

									} else if (result.getFailureReason() == 2) {
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
												.clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert("Unable to find Node to modify.");
										searchDisplay.getDefineNameTxtArea().setText(definitionName.trim());
									} else if (result.getFailureReason() == 4) {
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
												.clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert("Unauthorized delete operation.");
										searchDisplay.getDefineNameTxtArea().setText(definitionName.trim());
									}
								}
								showSearchingBusy(false);
								//508 changes for Definitions Section
								searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLDefinitionsView().getMainDefineViewVerticalPanel());
							}
						});
			} else {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert("Please select a definition to delete.");
				searchDisplay.getDefineNameTxtArea().setText(definitionName.trim());
			}
			/*} else {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getERROR_DEFINITION_NAME_NO_SPECIAL_CHAR());
				searchDisplay.getDefineNameTxtArea().setText(definitionName.trim());
			}

		} else {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert("Please select a definition to delete.");
			searchDisplay.getDefineNameTxtArea().setText(definitionName.trim());
		}*/
	}

	/**
	 * Delete function.
	 */
	protected void deleteFunction() {

		searchDisplay.resetMessageDisplay();
		final String functionName = searchDisplay.getFuncNameTxtArea().getText();
		/*String functionBody = searchDisplay.getFunctionBodyAceEditor().getText();
		String funcContext = "";
		if (searchDisplay.getContextFuncPATRadioBtn().getValue()) {
			funcContext = "Patient";
		} else {
			funcContext = "Population";
		}
		if (!functionName.isEmpty()) {
			CQLFunctions function = new CQLFunctions();
			function.setFunctionLogic(functionBody);
			function.setFunctionName(functionName);
			function.setArgumentList(searchDisplay.getFunctionArgumentList());
			function.setContext(funcContext);*/
		if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId() != null) {
			final CQLFunctions toBeModifiedFuncObj = searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap()
					.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId());
			showSearchingBusy(true);
			MatContext.get().getCQLLibraryService().deleteFunctions(MatContext.get().getCurrentCQLLibraryId(),
					toBeModifiedFuncObj, searchDisplay.getCqlLeftNavBarPanelView().getViewFunctions(),
					new AsyncCallback<SaveUpdateCQLResult>() {

						@Override
						public void onFailure(Throwable caught) {
							searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
									.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
							showSearchingBusy(false);
						}

						@Override
						public void onSuccess(SaveUpdateCQLResult result) {
							if (result != null) {
								if (result.isSuccess()) {
									searchDisplay.getCqlLeftNavBarPanelView()
											.setViewFunctions(result.getCqlModel().getCqlFunctions());
									MatContext.get()
											.setFuncs(getFunctionList(result.getCqlModel().getCqlFunctions()));
									searchDisplay.getCqlLeftNavBarPanelView().clearAndAddFunctionsNamesToListBox();
									searchDisplay.getCqlLeftNavBarPanelView().updateFunctionMap();
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
									searchDisplay.getCQLFunctionsView().getFunctionArgNameMap().clear();
									searchDisplay.getCQLFunctionsView().getFunctionArgumentList().clear();
									searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestFuncTextBox()
											.setText("");
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
											.setVisible(true);
									searchDisplay.getFuncNameTxtArea().setText("");
									searchDisplay.getFunctionBodyAceEditor().setText("");
									searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
									searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
									searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
									searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
									searchDisplay.getFunctionBodyAceEditor().clearAnnotations();
									searchDisplay.getFunctionBodyAceEditor().removeAllMarkers();
									//Commenting below code as its taking away focus and that makes our application not 508 compliant with other fields.
									//searchDisplay.getFunctionBodyAceEditor().redisplay();
									searchDisplay.getFunctionBodyAceEditor().setAnnotations();
									searchDisplay.getFunctionButtonBar().getDeleteButton().setEnabled(false);
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
											.createAlert(MatContext.get().getMessageDelegate().getSuccessfulFunctionRemoveMessage(toBeModifiedFuncObj.getFunctionName()));
									
									
									
									searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setText("");
									
									if (result.getFunction() != null) {
										searchDisplay.createAddArgumentViewForFunctions(
												new ArrayList<CQLFunctionArgument>());
									}
									
								} else if (result.getFailureReason() == 2) {
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
											.createAlert("Unable to find Node to modify.");
									searchDisplay.getFuncNameTxtArea().setText(functionName.trim());
								} else if (result.getFailureReason() == 4) {
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
											.createAlert("Unauthorized delete operation.");
									searchDisplay.getFuncNameTxtArea().setText(functionName.trim());
								}

								
							}
							showSearchingBusy(false);
							//508 Compliance for Function section
							searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLFunctionsView().getMainFunctionVerticalPanel());
						}
					});
		} else {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert("Please select a function to delete.");
			searchDisplay.getFuncNameTxtArea().setText(functionName.trim());
		}
		/*} else {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert("Please select a function to delete.");
			searchDisplay.getFuncNameTxtArea().setText(functionName.trim());
		}*/
	}
	
	protected void deleteFunctionArgument(){
		
		String funcArgName = null;
		
		searchDisplay.resetMessageDisplay();
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
		Iterator<CQLFunctionArgument> iterator = searchDisplay.getCQLFunctionsView().getFunctionArgumentList()
				.iterator();
		searchDisplay.getCQLFunctionsView().getFunctionArgNameMap()
		.remove(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionArgumentName().toLowerCase());
		while (iterator.hasNext()) {
			CQLFunctionArgument cqlFunArgument = iterator.next();
			if (cqlFunArgument.getId().equals(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionArgumentObjId())) {

				iterator.remove();
				searchDisplay.getCQLFunctionsView().createAddArgumentViewForFunctions(
						searchDisplay.getCQLFunctionsView().getFunctionArgumentList(),
						MatContext.get().getMeasureLockService().checkForEditPermission());
				funcArgName = cqlFunArgument.getArgumentName();
				break;
			}
		}

		//resetting name and id
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
		
		//508 Compliance for Function section
		searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLFunctionsView().getFuncNameTxtArea());
		
		searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
		.createAlert(MatContext.get().getMessageDelegate().getSuccessfulFunctionArgumentRemoveMessage(funcArgName));

	}

	/**
	 * Delete parameter.
	 */
	protected void deleteParameter() {

		searchDisplay.resetMessageDisplay();
		final String parameterName = searchDisplay.getParameterNameTxtArea().getText();
		/*String parameterBody = searchDisplay.getParameterAceEditor().getText();

		if (!parameterName.isEmpty()) {
			CQLParameter parameter = new CQLParameter();
			parameter.setParameterLogic(parameterBody);
			parameter.setParameterName(parameterName);*/
		if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedParamerterObjId() != null) {
			final CQLParameter toBeModifiedParamObj = searchDisplay.getCqlLeftNavBarPanelView().getParameterMap()
					.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedParamerterObjId());
			
			if(toBeModifiedParamObj.isReadOnly()){
				searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert("Unauthorized delete operation.");
				searchDisplay.getParameterNameTxtArea().setText(parameterName.trim());
				
			} else {
				showSearchingBusy(true);
				MatContext.get().getCQLLibraryService().deleteParameter(MatContext.get().getCurrentCQLLibraryId(),
						toBeModifiedParamObj,
						searchDisplay.getCqlLeftNavBarPanelView().getViewParameterList(),
						new AsyncCallback<SaveUpdateCQLResult>() {

							@Override
							public void onFailure(Throwable caught) {
								searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
										.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
								showSearchingBusy(false);
							}

							@Override
							public void onSuccess(SaveUpdateCQLResult result) {
								if (result != null) {
									if (result.isSuccess()) {
										searchDisplay.getCqlLeftNavBarPanelView()
												.setViewParameterList((result.getCqlModel().getCqlParameters()));
										MatContext.get().setParameters(
												getParamaterList(result.getCqlModel().getCqlParameters()));
										searchDisplay.getCqlLeftNavBarPanelView().clearAndAddParameterNamesToListBox();
										searchDisplay.getCqlLeftNavBarPanelView().updateParamMap();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
												.setVisible(true);

										searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestParamTextBox()
												.setText("");
										searchDisplay.getParameterNameTxtArea().setText("");
										searchDisplay.getParameterAceEditor().setText("");
										searchDisplay.getCqlLeftNavBarPanelView()
												.setCurrentSelectedParamerterObjId(null);
										searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
										searchDisplay.getParameterAceEditor().clearAnnotations();
										searchDisplay.getParameterAceEditor().removeAllMarkers();
										//Commenting below code as its taking away focus and that makes our application not 508 compliant with other fields.
										//searchDisplay.getParameterAceEditor().redisplay();
										searchDisplay.getParameterAceEditor().setAnnotations();
										searchDisplay.getParameterButtonBar().getDeleteButton().setEnabled(false);
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
												.createAlert(MatContext.get().getMessageDelegate().getSuccessfulParameterRemoveMessage(toBeModifiedParamObj.getParameterName()));  
									} else if (result.getFailureReason() == 2) {
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert("Unable to find Node to modify.");
										searchDisplay.getParameterNameTxtArea().setText(parameterName.trim());
									}else if (result.getFailureReason() == 4) {
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert("Unauthorized delete operation.");
										searchDisplay.getParameterNameTxtArea().setText(parameterName.trim());
									}
								}
								showSearchingBusy(false);
								//508 Compliance for Function section
								searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLParametersView().getMainParamViewVerticalPanel());
							}
						});
			} 
		}else {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert("Please select parameter to delete.");
			searchDisplay.getParameterNameTxtArea().setText(parameterName.trim());
		}
		/*} else {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert("Please select a parameter to delete.");
			searchDisplay.getParameterNameTxtArea().setText(parameterName.trim());
		}*/
	}

	/**
	 * Delete include.
	 */
	protected void deleteInclude() {

		searchDisplay.resetMessageDisplay();
		final String aliasName = searchDisplay.getIncludeView().getAliasNameTxtArea().getText();
		/*String includeLibName = searchDisplay.getIncludeView().getCqlLibraryNameTextBox().getText();

		if (!aliasName.isEmpty()) {
			CQLIncludeLibrary cqlLibObject = new CQLIncludeLibrary();
			cqlLibObject.setCqlLibraryName(includeLibName);
			cqlLibObject.setAliasName(aliasName);*/
		if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedIncLibraryObjId() != null) {
			final CQLIncludeLibrary toBeModifiedIncludeObj = searchDisplay.getCqlLeftNavBarPanelView()
					.getIncludeLibraryMap()
					.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedIncLibraryObjId());
			showSearchingBusy(true);
			MatContext.get().getCQLLibraryService().deleteInclude(MatContext.get().getCurrentCQLLibraryId(),
					toBeModifiedIncludeObj, 
					searchDisplay.getCqlLeftNavBarPanelView().getViewIncludeLibrarys(),
					new AsyncCallback<SaveUpdateCQLResult>() {

						@Override
						public void onFailure(Throwable caught) {
							searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
									.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
							showSearchingBusy(false);
						}

						@Override
						public void onSuccess(SaveUpdateCQLResult result) {
							if (result.isSuccess()) {
								searchDisplay.getCqlLeftNavBarPanelView()
										.setViewIncludeLibrarys(result.getCqlModel().getCqlIncludeLibrarys());
								MatContext.get()
										.setIncludes(getIncludesList(result.getCqlModel().getCqlIncludeLibrarys()));
								MatContext.get().setIncludedValueSetNames(result.getCqlModel().getIncludedValueSetNames());
								MatContext.get().setIncludedCodeNames(result.getCqlModel().getIncludedCodeNames());
								//MatContext.get().getIncludedValueSetNames().addAll(result.getCqlModel().getIncludedCodeNames());
								MatContext.get().setIncludedParamNames(result.getCqlModel().getIncludedParamNames());
								MatContext.get().setIncludedDefNames(result.getCqlModel().getIncludedDefNames());
								MatContext.get().setIncludedFuncNames(result.getCqlModel().getIncludedFuncNames());
								searchDisplay.getCqlLeftNavBarPanelView().clearAndAddAliasNamesToListBox();
								searchDisplay.getCqlLeftNavBarPanelView().udpateIncludeLibraryMap();
								searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
								searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().setVisible(true);

								searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestIncludeTextBox()
										.setText("");
								searchDisplay.getIncludeView().getAliasNameTxtArea().setText("");
								searchDisplay.getIncludeView().getCqlLibraryNameTextBox().setText("");
								searchDisplay.getIncludeView().getOwnerNameTextBox().setText("");
								searchDisplay.getIncludeView().getViewCQLEditor().setText("");
								searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedIncLibraryObjId(null);
								searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
								searchDisplay.getIncludeView().getViewCQLEditor().clearAnnotations();
								searchDisplay.getIncludeView().getViewCQLEditor().removeAllMarkers();
								//searchDisplay.getIncludeView().getViewCQLEditor().redisplay();
								searchDisplay.getIncludeView().getViewCQLEditor().setAnnotations();
								searchDisplay.getIncludeView().getDeleteButton().setEnabled(false);

								searchDisplay.getIncludeView().getCloseButton()
										.fireEvent(new GwtEvent<ClickHandler>() {
											@Override
											public com.google.gwt.event.shared.GwtEvent.Type<ClickHandler> getAssociatedType() {
												return ClickEvent.getType();
											}

											@Override
											protected void dispatch(ClickHandler handler) {
												handler.onClick(null);
											}
										});
								
								searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
								.createAlert(MatContext.get().getMessageDelegate().getSuccessfulIncludeRemoveMessage(toBeModifiedIncludeObj.getAliasName()));
							} else if (result.getFailureReason() == 2) {
								searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
								searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
										.createAlert("Unable to find Node to modify.");
								searchDisplay.getIncludeView().getAliasNameTxtArea().setText(aliasName.trim());
							}
							showSearchingBusy(false);
						}
					});
		} else {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert("Please select an alias to delete.");
			searchDisplay.getIncludeView().getAliasNameTxtArea().setText(aliasName.trim());
		}
		/*} else {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert("Please select an alias to delete.");
			searchDisplay.getIncludeView().getAliasNameTxtArea().setText(aliasName.trim());
		}*/
	}

	private void deleteCode(){
		searchDisplay.getCodesView().showSearchingBusyOnCodes(true);
		MatContext.get().getCQLLibraryService().deleteCode(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedCodesObjId(), MatContext.get().getCurrentCQLLibraryId(), new AsyncCallback<SaveUpdateCQLResult>() {

			@Override
			public void onFailure(Throwable caught) {
				searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedCodesObjId(null);
				searchDisplay.getCodesView().showSearchingBusyOnCodes(false);
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}

			@Override
			public void onSuccess(SaveUpdateCQLResult result) {
				searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedCodesObjId(null);
				searchDisplay.getCodesView().showSearchingBusyOnCodes(false);
				if(result.isSuccess()){
					searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getSUCCESSFUL_CODE_REMOVE_MSG(result.getCqlCode().getCodeOID()));
					searchDisplay.getCodesView().resetCQLCodesSearchPanel();
					appliedCodeTableList.clear();
					appliedCodeTableList.addAll(result.getCqlCodeList());
					searchDisplay.getCqlLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
					//searchDisplay.buildCodes();
					searchDisplay.getCodesView().buildCodesCellTable(
							appliedCodeTableList,
							MatContext.get().getLibraryLockService().checkForEditPermission());
					//Temporary fix to update codes for insert Icon.
					getAppliedValueSetList();
				} else {
					
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Unable to delete.");
					
				}
				
				//508 : Shift focus to code search panel.
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCodesView().getCodeSearchInput());
			}
		});
	}
	
	private void checkAndDeleteValueSet(){
		MatContext.get().getLibraryService().getCQLData(MatContext.get().getCurrentCQLLibraryId(),
				new AsyncCallback<SaveUpdateCQLResult>() {

					@Override
					public void onSuccess(final SaveUpdateCQLResult result) {
						searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedValueSetObjId(null);
						appliedValueSetTableList.clear();
						if (result.getCqlModel().getAllValueSetList() != null) {
							for (CQLQualityDataSetDTO dto : result.getCqlModel().getAllValueSetList()) {
								if(dto.isSuppDataElement() || 
										dto.getOid().equals("419099009") || dto.getOid().equals("21112-8") 
												|| (dto.getType() !=null && dto.getType().equalsIgnoreCase("code")))
									continue;
								appliedValueSetTableList.add(dto);
							}

							if (appliedValueSetTableList.size() > 0) {
								Iterator<CQLQualityDataSetDTO> iterator = appliedValueSetTableList
										.iterator();
								while (iterator.hasNext()) {
									CQLQualityDataSetDTO dataSetDTO = iterator.next();
									if (dataSetDTO.getUuid() != null) {
										if (dataSetDTO.getUuid().equals(searchDisplay.getValueSetView()
												.getSelectedElementToRemove().getUuid())) {
											if (!dataSetDTO.isUsed()) {
												deleteValueSet(dataSetDTO.getId());
												iterator.remove();
											}
										}
									}
								}
							}
						}
						showSearchingBusy(false);
					}

					@Override
					public void onFailure(Throwable caught) {
						searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedValueSetObjId(null);
						showSearchingBusy(false);
						Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					}
				});
	}
	/**
	 * Save CQL general information.
	 */
	private void saveCQLGeneralInformation() {

		String libraryId = MatContext.get().getCurrentCQLLibraryId();
		String libraryValue = searchDisplay.getCqlGeneralInformationView().getLibraryNameValue().getText().trim();
		showSearchingBusy(true);
		MatContext.get().getCQLLibraryService().saveAndModifyCQLGeneralInfo(libraryId, libraryValue,
				new AsyncCallback<SaveUpdateCQLResult>() {

					@Override
					public void onFailure(Throwable caught) {
						searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
								.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						showSearchingBusy(false);
					}

					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						if (result != null) {
							// cqlLibraryName =
							// result.getCqlModel().getLibraryName().trim();
							cqlLibraryName = result.getCqlModel().getLibraryName().trim();
							searchDisplay.getCqlGeneralInformationView().getLibraryNameValue().setText(cqlLibraryName);
							searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
									.createAlert(MatContext.get().getMessageDelegate().getMODIFY_CQL_LIBRARY_NAME());
							searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
							MatContext.get().getCurrentLibraryInfo().setLibraryName(cqlLibraryName);
							CqlComposerPresenter.setContentHeading();
						}
						showSearchingBusy(false);
					}
				});

	}

	/**
	 * Log recent activity.
	 */
	private void logRecentActivity() {
		MatContext.get().getCQLLibraryService().isLibraryAvailableAndLogRecentActivity(
				MatContext.get().getCurrentCQLLibraryId(), MatContext.get().getLoggedinUserId(),
				new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(Void result) {
						isCQLWorkSpaceLoaded = true;
						displayCQLView();
					}
				});

	}

	/**
	 * Display CQL view.
	 */
	private void displayCQLView() {
		panel.clear();

		currentSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
		searchDisplay.buildView();
		addLeftNavEventHandler();
		searchDisplay.resetMessageDisplay();
		getCQLData();
		panel.add(searchDisplay.asWidget());
	}

	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
		currentIncludeLibrarySetId = null;
		currentIncludeLibraryId = null; 
		searchDisplay.getCqlGeneralInformationView().clearAllGeneralInfoOfLibrary();
		searchDisplay.getCqlLeftNavBarPanelView().clearShotcutKeyList();
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedParamerterObjId(null);
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
		searchDisplay.getFunctionArgNameMap().clear();
		searchDisplay.getValueSetView().clearCellTableMainPanel();
		searchDisplay.getCodesView().clearCellTableMainPanel();
		searchDisplay.getIncludeView().getSearchTextBox().setText("");
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
		searchDisplay.resetMessageDisplay();
		searchDisplay.getCqlLeftNavBarPanelView().getIncludesCollapse().getElement()
				.setClassName("panel-collapse collapse");
		searchDisplay.getCqlLeftNavBarPanelView().getParamCollapse().getElement()
				.setClassName("panel-collapse collapse");
		searchDisplay.getCqlLeftNavBarPanelView().getDefineCollapse().getElement()
				.setClassName("panel-collapse collapse");
		searchDisplay.getCqlLeftNavBarPanelView().getFunctionCollapse().getElement()
				.setClassName("panel-collapse collapse");
		if (searchDisplay.getFunctionArgumentList().size() > 0) {
			searchDisplay.getFunctionArgumentList().clear();
		}
		isModified = false;
		setId = null;
	/*	isExpansionProfile = false;
		expProfileToAllValueSet = "";*/
		modifyValueSetDTO = null;
		curAceEditor = null;
		currentSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
		searchDisplay.getCqlLeftNavBarPanelView().getMessagePanel().clear();
		searchDisplay.resetAll();
		panel.clear();
		searchDisplay.getMainPanel().clear();
		MatContext.get().getValuesets().clear();
	}

	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {

		if ((MatContext.get().getCurrentCQLLibraryId() == null)
				|| MatContext.get().getCurrentCQLLibraryId().equals("")) {
			displayEmpty();
		} else {
			panel.clear();
			searchDisplay.getLockedButtonVPanel();
			panel.add(searchDisplay.asWidget());
			if (!isCQLWorkSpaceLoaded) { // this check is made so that when CQL
											// is
											// clicked from CQL library, its not
											// called twice.

				displayCQLView();

			} else {
				isCQLWorkSpaceLoaded = false;
			}
		}
	/*	MatContext.get().getAllCqlKeywordsAndQDMDatatypesForCQLWorkSpaceSA();
		MatContext.get().getAllUnits();*/
		CqlComposerPresenter.setSubSkipEmbeddedLink("CQLStandaloneWorkSpaceView.containerPanel");
		Mat.focusSkipLists("CqlComposer");

	}

	/**
	 * Gets the CQL data.
	 *
	 * @return the CQL data
	 */
	private void getCQLData() {
		showSearchingBusy(true);
		MatContext.get().getCQLLibraryService().getCQLData(MatContext.get().getCurrentCQLLibraryId(),
				new AsyncCallback<SaveUpdateCQLResult>() {

					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						if (result.isSuccess()) {
							if (result.getCqlModel() != null) {
								if (result.getSetId() != null) {
									setId = result.getSetId();
								}
								if (result.getCqlModel().getLibraryName() != null) {
									cqlLibraryName = searchDisplay.getCqlGeneralInformationView()
											.createCQLLibraryName(MatContext.get().getCurrentCQLLibraryeName());

									String libraryVersion = MatContext.get().getCurrentCQLLibraryVersion();

									libraryVersion = libraryVersion.replaceAll("Draft ", "").trim();
									if (libraryVersion.startsWith("v")) {
										libraryVersion = libraryVersion.substring(1);
									}
									searchDisplay.getCqlGeneralInformationView().setGeneralInfoOfLibrary(cqlLibraryName,libraryVersion, result.getCqlModel().getQdmVersion(),"QDM");
								}

								List<CQLQualityDataSetDTO> appliedAllValueSetList = new ArrayList<CQLQualityDataSetDTO>();
								List<CQLQualityDataSetDTO> appliedValueSetListInXML = result.getCqlModel()
										.getAllValueSetList();

								for (CQLQualityDataSetDTO dto : appliedValueSetListInXML) {
									if (dto.isSuppDataElement())
										continue;
									appliedAllValueSetList.add(dto);
								}

								MatContext.get().setValuesets(appliedAllValueSetList);
								//searchDisplay.getCqlLeftNavBarPanelView().setAppliedQdmList(appliedAllValueSetList);
								appliedValueSetTableList.clear();
								appliedCodeTableList.clear();
								for (CQLQualityDataSetDTO dto : result.getCqlModel().getValueSetList()) {
									if (dto.isSuppDataElement())
										continue;
									appliedValueSetTableList.add(dto);
								}
								searchDisplay.getCqlLeftNavBarPanelView()
										.setAppliedQdmTableList(appliedValueSetTableList);
								searchDisplay.getCqlLeftNavBarPanelView().updateValueSetMap(appliedValueSetTableList);
								
								if(result.getCqlModel().getCodeList()!=null){
									appliedCodeTableList.addAll(result.getCqlModel().getCodeList());	
								}
								searchDisplay.getCqlLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
								searchDisplay.getCqlLeftNavBarPanelView().setAppliedCodeTableList(appliedCodeTableList);
								
								if ((result.getCqlModel().getDefinitionList() != null)
										&& (result.getCqlModel().getDefinitionList().size() > 0)) {
									searchDisplay.getCqlLeftNavBarPanelView()
											.setViewDefinitions(result.getCqlModel().getDefinitionList());
									searchDisplay.getCqlLeftNavBarPanelView().clearAndAddDefinitionNamesToListBox();
									searchDisplay.getCqlLeftNavBarPanelView().updateDefineMap();
									MatContext.get().setDefinitions(
											getDefinitionList(result.getCqlModel().getDefinitionList()));
								} else {
									searchDisplay.getCqlLeftNavBarPanelView().getDefineBadge().setText("00");
								}
								if ((result.getCqlModel().getCqlParameters() != null)
										&& (result.getCqlModel().getCqlParameters().size() > 0)) {
									searchDisplay.getCqlLeftNavBarPanelView()
											.setViewParameterList(result.getCqlModel().getCqlParameters());
									searchDisplay.getCqlLeftNavBarPanelView().clearAndAddParameterNamesToListBox();
									searchDisplay.getCqlLeftNavBarPanelView().updateParamMap();
									MatContext.get()
											.setParameters(getParamaterList(result.getCqlModel().getCqlParameters()));
								} else {
									searchDisplay.getCqlLeftNavBarPanelView().getParamBadge().setText("00");
								}
								if ((result.getCqlModel().getCqlFunctions() != null)
										&& (result.getCqlModel().getCqlFunctions().size() > 0)) {
									searchDisplay.getCqlLeftNavBarPanelView()
											.setViewFunctions(result.getCqlModel().getCqlFunctions());
									searchDisplay.getCqlLeftNavBarPanelView().clearAndAddFunctionsNamesToListBox();
									searchDisplay.getCqlLeftNavBarPanelView().updateFunctionMap();
									MatContext.get().setFuncs(getFunctionList(result.getCqlModel().getCqlFunctions()));
								} else {
									searchDisplay.getCqlLeftNavBarPanelView().getFunctionBadge().setText("00");
								}
								if ((result.getCqlModel().getCqlIncludeLibrarys() != null)
										&& (result.getCqlModel().getCqlIncludeLibrarys().size() > 0)) {
									searchDisplay.getCqlLeftNavBarPanelView()
											.setViewIncludeLibrarys(result.getCqlModel().getCqlIncludeLibrarys());
									searchDisplay.getCqlLeftNavBarPanelView().clearAndAddAliasNamesToListBox();
									searchDisplay.getCqlLeftNavBarPanelView().udpateIncludeLibraryMap();
									MatContext.get()
											.setIncludes(getIncludesList(result.getCqlModel().getCqlIncludeLibrarys()));
									MatContext.get().setIncludedValueSetNames(result.getCqlModel().getIncludedValueSetNames());
									MatContext.get().setIncludedCodeNames(result.getCqlModel().getIncludedCodeNames());
									//MatContext.get().getIncludedValueSetNames().addAll(result.getCqlModel().getIncludedCodeNames());
									MatContext.get().setIncludedParamNames(result.getCqlModel().getIncludedParamNames());
									MatContext.get().setIncludedDefNames(result.getCqlModel().getIncludedDefNames());
									MatContext.get().setIncludedFuncNames(result.getCqlModel().getIncludedFuncNames());
								} else {
									searchDisplay.getCqlLeftNavBarPanelView().getIncludesBadge().setText("00");
									searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap().clear();
								}
								
								boolean isValidQDMVersion = searchDisplay.getCqlLeftNavBarPanelView().checkForIncludedLibrariesQDMVersion();
								if(!isValidQDMVersion){
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getINVALID_QDM_VERSION_IN_INCLUDES());
								} else {
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
								}
							}
							showSearchingBusy(false);
						}

					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						showSearchingBusy(false);
					}
				});
	}
	
	

	/**
	 * Adds the left nav event handler.
	 */
	private void addLeftNavEventHandler() {

		searchDisplay.getCqlLeftNavBarPanelView().getGeneralInformation().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// searchDisplay.hideAceEditorAutoCompletePopUp();
				generalInfoEvent();
			}
		});

		searchDisplay.getCqlLeftNavBarPanelView().getIncludesLibrary().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				// searchDisplay.hideAceEditorAutoCompletePopUp();
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_INCLUDES_MENU;
					searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
					event.stopPropagation();
				} else {
					includesEvent();
				}

			}
		});

		searchDisplay.getCqlLeftNavBarPanelView().getCodesLibrary().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				// searchDisplay.hideAceEditorAutoCompletePopUp();
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_CODES;
					searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
					event.stopPropagation();
				} else {
					codesEvent();
					//508 : Shift focus to code search panel.
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCodesView().getCodeSearchInput());
				}

			}
			
		});
		
		searchDisplay.getCqlLeftNavBarPanelView().getAppliedQDM().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				appliedQDMEvent();
				//508 : Shift focus to search panel.
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getValueSetView().getOIDInput());
			}
		});
		searchDisplay.getCqlLeftNavBarPanelView().getParameterLibrary().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.hideAceEditorAutoCompletePopUp();
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_PARAMETER_MENU;
					searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
					event.stopPropagation();
				} else {
					parameterEvent();
				}

			}
		});

		searchDisplay.getCqlLeftNavBarPanelView().getDefinitionLibrary().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.hideAceEditorAutoCompletePopUp();
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_DEFINE_MENU;
					searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
					event.stopPropagation();
				} else {
					definitionEvent();
				}
			}
		});

		searchDisplay.getCqlLeftNavBarPanelView().getFunctionLibrary().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.hideAceEditorAutoCompletePopUp();
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_FUNCTION_MENU;
					searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
					event.stopPropagation();
				} else {
					functionEvent();
				}
			}
		});

		searchDisplay.getCqlLeftNavBarPanelView().getViewCQL().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.hideAceEditorAutoCompletePopUp();
				viewCqlEvent();
			}
		});

	}

	/**
	 * Sets the expansion profile panel values.
	 */
	/*private void setExpansionProfilePanelValues() {
		//if UMLS is not logged in
		if (!MatContext.get().isUMLSLoggedIn()) {
			if(expProfileToAllValueSet !=null && !expProfileToAllValueSet.equalsIgnoreCase("")){
				searchDisplay.getValueSetView().getVSACExpansionProfileListBox().setEnabled(false);
				searchDisplay.getValueSetView().getVSACExpansionProfileListBox().clear();
				searchDisplay.getValueSetView().getVSACExpansionProfileListBox().addItem(expProfileToAllValueSet);
				searchDisplay.getValueSetView().getDefaultExpProfileSel().setValue(true);
				searchDisplay.getValueSetView().getDefaultExpProfileSel().setEnabled(false);
				isExpansionProfile = true;
			} else {
				isExpansionProfile = false;
			}
		} else {
			if(expProfileToAllValueSet != null && !expProfileToAllValueSet.equalsIgnoreCase("")){
				isExpansionProfile = true;
				searchDisplay.getValueSetView().getVSACExpansionProfileListBox().setEnabled(true);
				searchDisplay.getValueSetView().setExpProfileList(MatContext.get()
						.getExpProfileList());
				searchDisplay.getValueSetView().setDefaultExpansionProfileListBox();
				for(int j = 0; j < searchDisplay.getValueSetView().getVSACExpansionProfileListBox().getItemCount(); j++){
					if(searchDisplay.getValueSetView().getVSACExpansionProfileListBox().getItemText(j)
							.equalsIgnoreCase(expProfileToAllValueSet)) {
						searchDisplay.getValueSetView().getVSACExpansionProfileListBox().setItemSelected(j, true);
						searchDisplay.getValueSetView().getVSACExpansionProfileListBox().setSelectedIndex(j);
						searchDisplay.getValueSetView().getDefaultExpProfileSel().setValue(true);
						break;
					}
				}
			} else{
				isExpansionProfile = false;
			}
		}

	}*/
	
	/**
	 * Gets the used artifacts.
	 *
	 * @return the used artifacts
	 */
	private void getUsedArtifacts() {
		showSearchingBusy(true);
        MatContext.get().getLibraryService().getUsedCqlArtifacts(
                     MatContext.get().getCurrentCQLLibraryId(),
                     new AsyncCallback<GetUsedCQLArtifactsResult>() {

                            @Override
                            public void onFailure(Throwable caught) {
                            	 Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
                            	 showSearchingBusy(false);
                                   
                            }

                            @Override
                            public void onSuccess(GetUsedCQLArtifactsResult result) {
                            	showSearchingBusy(false);
                            	// if there are errors, set the valuesets to not used.
                            	if(!result.getCqlErrors().isEmpty()) {
                            		for(CQLQualityDataSetDTO cqlDTo : searchDisplay.getCqlLeftNavBarPanelView().getAppliedQdmTableList()){
                                      cqlDTo.setUsed(false);
                            		}
                            	}
                            	
                            	// otherwise, check if the valueset is in the used valusets list
                            	else {
                                   for(CQLQualityDataSetDTO cqlDTo : searchDisplay.getCqlLeftNavBarPanelView().getAppliedQdmTableList()){
                                          if (result.getUsedCQLValueSets().contains(cqlDTo.getCodeListName())) {
                                              cqlDTo.setUsed(true);
                                          } else{
                                        	  cqlDTo.setUsed(false);
                                          }
                                   }
                            	}
                            		
                            	if(searchDisplay.getCqlLeftNavBarPanelView().getAppliedQdmTableList().size() > 0) {
                                	searchDisplay.getValueSetView().getCelltable().redraw();
                                	searchDisplay.getValueSetView().getListDataProvider().refresh();
                            	}
                                   
                            }
                            
                     });
        
 }
	/**
	 * Adds the QDM Search Panel event Handlers.
	 */
	private void addValueSetEventHandlers() {
		/*searchDisplay.getValueSetView().getApplyDefaultExpansionIdButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					// code for adding profile to List to applied QDM
					searchDisplay.resetMessageDisplay();
					if (!MatContext.get().isUMLSLoggedIn()) { // UMLS
						// Login
						// Validation
						searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
								.createAlert(MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN());
						searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().setVisible(true);
						return;
					}
					int selectedindex = searchDisplay.getValueSetView().getVSACExpansionProfileListBox()
							.getSelectedIndex();
					String selectedValue = searchDisplay.getValueSetView().getVSACExpansionProfileListBox()
							.getValue(selectedindex);

					if (!selectedValue.equalsIgnoreCase("--Select--")) {
						isExpansionProfile = true;
						expProfileToAllValueSet = selectedValue;
						updateAllValueSetWithExpProfile(appliedValueSetTableList);
					} else if (!searchDisplay.getValueSetView().getDefaultExpProfileSel().getValue()) {
						isExpansionProfile = false;
						expProfileToAllValueSet = "";
						updateAllValueSetWithExpProfile(appliedValueSetTableList);
					} else {
						searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
								.createAlert(MatContext.get().getMessageDelegate().getVsacExpansionProfileSelection());
					}
				}
			}
		});*/

		/*searchDisplay.getValueSetView().getDefaultExpProfileSel()
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						if (event.getValue().toString().equals("true")) {
							if (!MatContext.get().isUMLSLoggedIn()) { // UMLS
								// Login
								// Validation
								searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
										.createAlert(MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN());
								searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().setVisible(true);
								return;
							}
							searchDisplay.getValueSetView().getVSACExpansionProfileListBox().setEnabled(true);
							searchDisplay.getValueSetView().setExpProfileList(MatContext.get().getExpProfileList());
							searchDisplay.getValueSetView().setDefaultExpansionProfileListBox();
						} else if (event.getValue().toString().equals("false")) {
							searchDisplay.getValueSetView().getVSACExpansionProfileListBox().setEnabled(false);
							searchDisplay.getValueSetView().setDefaultExpansionProfileListBox();
						}

					}
				});*/
		/**
		 * this functionality is to clear the content on the QDM Element Search
		 * Panel.
		 */
		searchDisplay.getValueSetView().getCancelQDMButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				isModified = false;
				searchDisplay.getValueSetView().resetCQLValuesetearchPanel();
				//508 compliance for Value Sets
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getValueSetView().getOIDInput());
			}
		});

		searchDisplay.getValueSetView().getUpdateFromVSACButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					updateVSACValueSets();
					//508 compliance for Value Sets
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getValueSetView().getOIDInput());
				}
			}
		});

		/**
		 * this functionality is to retrieve the value set from VSAC with latest
		 * information which consists of Expansion Profile list and Version
		 * List.
		 */
		searchDisplay.getValueSetView().getRetrieveFromVSACButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					String version = null;
					String expansionProfile = null;
					searchValueSetInVsac(version, expansionProfile);
					//508 compliance for Value Sets
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getValueSetView().getOIDInput());
				}
			}
		});

		/**
		 * this handler is invoked when apply button is clicked on search Panel
		 * in QDM elements tab and this is to add new value set or user Defined
		 * QDM to the Applied QDM list.
		 */
		searchDisplay.getValueSetView().getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					MatContext.get().clearDVIMessages();
					searchDisplay.resetMessageDisplay();

					if (isModified && (modifyValueSetDTO != null)) {
						modifyValueSetOrUserDefined(isUserDefined);
					} else {
						addNewValueSet(isUserDefined);
					}
					//508 compliance for Value Sets
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getValueSetView().getOIDInput());
				}
			}
		});

		/**
		 * Adding value Change handler for UserDefined Input in Search Panel in
		 * QDM Elements Tab
		 * 
		 */
		searchDisplay.getValueSetView().getUserDefinedInput().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				searchDisplay.resetMessageDisplay();
				isUserDefined = searchDisplay.getValueSetView().validateUserDefinedInput(isUserDefined);
			}
		});

		/**
		 * Adding value change handler for OID input in Search Panel in QDM
		 * elements Tab
		 */

		searchDisplay.getValueSetView().getOIDInput().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				searchDisplay.resetMessageDisplay();
				isUserDefined = searchDisplay.getValueSetView().validateOIDInput(isUserDefined);
			}
		});

		/**
		 * value change handler for Expansion Profile in Search Panel in QDM
		 * Elements Tab
		 */
		/*searchDisplay.getValueSetView().getQDMExpProfileListBox().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
				searchDisplay.resetMessageDisplay();
				if (!searchDisplay.getValueSetView()
						.getExpansionProfileValue(searchDisplay.getValueSetView().getQDMExpProfileListBox())
						.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
					searchDisplay.getValueSetView().getVersionListBox().setSelectedIndex(0);
				}
			}
		});*/

		/**
		 * value Change Handler for Version listBox in Search Panel
		 */
		searchDisplay.getValueSetView().getVersionListBox().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				searchDisplay.resetMessageDisplay();
				/*if (!searchDisplay.getValueSetView()
						.getVersionValue(searchDisplay.getValueSetView().getVersionListBox())
						.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
					searchDisplay.getValueSetView().getQDMExpProfileListBox().setSelectedIndex(0);
				}*/

			}
		});

		searchDisplay.getValueSetView().setObserver(new CQLAppliedValueSetView.Observer() {
			@Override
			public void onModifyClicked(CQLQualityDataSetDTO result) {
				if(!searchDisplay.getValueSetView().getIsLoading()){
					searchDisplay.resetMessageDisplay();
					searchDisplay.getValueSetView().resetCQLValuesetearchPanel();
					isModified = true;
					modifyValueSetDTO = result;
					String displayName = result.getCodeListName();
					// Substring at 60th character length.
					if(displayName.length() >=60){
						displayName = displayName.substring(0, 59);
					}
					HTML searchHeaderText = new HTML("<strong>Modify value set ( "+displayName +")</strong>");
					searchDisplay.getValueSetView().getSearchHeader().clear();
					searchDisplay.getValueSetView().getSearchHeader().add(searchHeaderText);
					searchDisplay.getValueSetView().getMainPanel().getElement().focus();
					if(result.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)){
						isUserDefined = true;
					} else {
						isUserDefined = false;
					}
					
					onModifyValueSet(result, isUserDefined);
					//508 Compliance for Value Sets section
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getValueSetView().getOIDInput());
				} else {
					//do nothing when loading.
				}
			}

			@Override
			public void onDeleteClicked(CQLQualityDataSetDTO result, final int index) {
				if(!searchDisplay.getValueSetView().getIsLoading()){
					searchDisplay.resetMessageDisplay();
					searchDisplay.getValueSetView().resetCQLValuesetearchPanel();
					if((modifyValueSetDTO!=null) && modifyValueSetDTO.getId().equalsIgnoreCase(result.getId())){
						isModified = false;
					}
					String measureId = MatContext.get().getCurrentMeasureId();
					if ((measureId != null) && !measureId.equals("")) {
						searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedValueSetObjId(result.getId());
						searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_VALUESET(result.getCodeListName()));
						searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
						//508 Compliance for Value Sets section
						searchDisplay.getValueSetView().getOIDInput().setFocus(true);
					}
				} else {
					//do nothing when loading.
				}
			}

		});
	}
	
	
/**
 * Adds the code search panel handlers.
 */
private void addCodeSearchPanelHandlers() {
		
		
		searchDisplay.getCodesView().getRetrieveFromVSACButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					searchCQLCodesInVsac();
					//508 Compliance for Codes section
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCodesView().getCodeInput());
				}
			}
		});
		
		searchDisplay.getCodesView().getSaveButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					MatContext.get().clearDVIMessages();
					searchDisplay.resetMessageDisplay();
					addNewCodes();
					//508 Compliance for Codes section
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCodesView().getCodeSearchInput());
				}
				
			}
		});
		
		searchDisplay.getCodesView().getCancelCodeButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCodesView().resetCQLCodesSearchPanel();
					//508 Compliance for Codes section
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCodesView().getCodeSearchInput());
				}
			}
		});
		searchDisplay.getCodesView().setDelegator(new Delegator() {
			
			@Override
			public void onDeleteClicked(CQLCode result, int index) {
				searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
				if(result != null){
					searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedCodesObjId(result.getId());
					searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_CODES(result.getCodeOID()));
					searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
					//508 Compliance for Codes section
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCodesView().getCodeInput());
				}
				
			}
		});
	}
	
	/**
	 * Adds the new codes.
	 */
	private void addNewCodes() {
		String cqlLibraryId = MatContext.get().getCurrentCQLLibraryId();
		MatCodeTransferObject transferObject = new MatCodeTransferObject();
		CQLCode refCode = new CQLCode();
		final String codeName = searchDisplay.getCodesView().getCodeDescriptorInput().getValue();
		refCode.setCodeOID(searchDisplay.getCodesView().getCodeInput().getValue());
		refCode.setCodeName(searchDisplay.getCodesView().getCodeDescriptorInput().getValue());
		refCode.setCodeSystemName(searchDisplay.getCodesView().getCodeSystemInput().getValue());
		refCode.setCodeSystemVersion(searchDisplay.getCodesView().getCodeSystemVersionInput().getValue());
		refCode.setCodeIdentifier(searchDisplay.getCodesView().getCodeSearchInput().getValue());
		//refCode.setDisplayName(searchDisplay.getCodesView().getCodeDescriptorInput().getValue());
		refCode.setCodeSystemOID(searchDisplay.getCodesView().getCodeSystemOid());
		
		final String codeSystemName = refCode.getCodeSystemName();
		final String codeId = refCode.getCodeOID();
		
		if(!searchDisplay.getCodesView().getSuffixTextBox().getValue().isEmpty()){
			refCode.setSuffix(searchDisplay.getCodesView().getSuffixTextBox().getValue());
			refCode.setDisplayName(codeName+" ("+searchDisplay.getCodesView().getSuffixTextBox().getValue()+")");
		} else {
			refCode.setDisplayName(codeName);
		}
		transferObject.setCqlCode(refCode);
		transferObject.setId(cqlLibraryId);
		transferObject.scrubForMarkUp();
		if(transferObject.isValidModel()){
			searchDisplay.getCodesView().showSearchingBusyOnCodes(true);
			cqlService.saveCQLCodestoCQLLibrary(transferObject, new AsyncCallback<SaveUpdateCQLResult>() {
				
				@Override
				public void onSuccess(SaveUpdateCQLResult result) {
					
					if(result.isSuccess()){
						searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(MatContext.get().
								getMessageDelegate().getCodeSuccessMessage(searchDisplay.getCodesView().getCodeInput().getText()));
						searchDisplay.getCodesView().resetCQLCodesSearchPanel();
						appliedCodeTableList.clear();
						appliedCodeTableList.addAll(result.getCqlCodeList());
						searchDisplay.getCodesView().buildCodesCellTable(appliedCodeTableList, MatContext.get().getLibraryLockService()
								.checkForEditPermission());
						searchDisplay.getCqlLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
						//Temporary fix to update codes for insert Icon.
						getAppliedValueSetList();
					} else {
						searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
						if(result.getFailureReason()==result.getDuplicateCode()){
							searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().generateDuplicateErrorMessage(codeName));
						}
						
						else if(result.getFailureReason() ==  result.getBirthdateOrDeadError()) {
							searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate()
									.getBirthdateOrDeadMessage(codeSystemName, codeId));
							
							searchDisplay.getCodesView().buildCodesCellTable(
									appliedCodeTableList,
									MatContext.get().getMeasureLockService().checkForEditPermission());
						}
					}
					//getUsedCodes();
					searchDisplay.getCodesView().showSearchingBusyOnCodes(false);
					//508 : Shift focus to code search panel.
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCodesView().getCodeSearchInput());
				}
				
				@Override
				public void onFailure(Throwable caught) {
					Window.alert(MatContext.get().getMessageDelegate()
							.getGenericErrorMessage());
					searchDisplay.getCodesView().showSearchingBusyOnCodes(false);
					
				}
			});
		}
	}
	
	/**
	 * Gets the applied code list.
	 *
	 * @return the applied code list
	 */
	private void getAppliedCodeList() {
		String cqlLibraryId = MatContext.get().getCurrentCQLLibraryId();
		if ((cqlLibraryId != null) && !cqlLibraryId.equals("")) {
			
			MatContext.get().getLibraryService().getCQLData(cqlLibraryId, new AsyncCallback<SaveUpdateCQLResult>() {
				
				@Override
				public void onSuccess(SaveUpdateCQLResult result) {
					appliedCodeTableList.clear();
					appliedCodeTableList.addAll(result.getCqlModel().getCodeList());
					searchDisplay.getCodesView().buildCodesCellTable(appliedCodeTableList, MatContext.get().getLibraryLockService()
							.checkForEditPermission());
					searchDisplay.getCqlLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
				}
				
				@Override
				public void onFailure(Throwable caught) {
					Window.alert(MatContext.get().getMessageDelegate()
							.getGenericErrorMessage());
					
				}
			});
		}
	}


	/**
	 * Update vsac value sets.
	 */
	private void updateVSACValueSets() {
		showSearchingBusy(true);
		String expansionId = null;
		
		cqlService.updateCQLVSACValueSets(MatContext.get().getCurrentCQLLibraryId(), expansionId,
				new AsyncCallback<VsacApiResult>() {

					@Override
					public void onFailure(final Throwable caught) {
						showSearchingBusy(false);
						Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						
					}

					@Override
					public void onSuccess(final VsacApiResult result) {
						
						if (result.isSuccess()) {
							searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
									.createAlert(MatContext.get().getMessageDelegate().getVSAC_UPDATE_SUCCESSFULL());
						
							/*List<CQLQualityDataSetDTO> appliedListModel = new ArrayList<CQLQualityDataSetDTO>();
							for (CQLQualityDataSetDTO cqlQDMDTO : result.getUpdatedCQLQualityDataDTOLIst()) {
								if (!ConstantMessages.EXPIRED_OID.equals(cqlQDMDTO
										.getDataType()) && !ConstantMessages.BIRTHDATE_OID.equals(cqlQDMDTO
												.getDataType())
										&& (cqlQDMDTO.getType() == null))  {
									appliedListModel.add(cqlQDMDTO);
								} 
							}*/
							List<CQLQualityDataSetDTO> appliedListModel = new ArrayList<CQLQualityDataSetDTO>();
							for (CQLQualityDataSetDTO cqlQDMDTO : result.getUpdatedCQLQualityDataDTOLIst()) {
								if (!ConstantMessages.EXPIRED_OID.equals(cqlQDMDTO
										.getDataType()) && !ConstantMessages.BIRTHDATE_OID.equals(cqlQDMDTO
												.getDataType())
										&& (cqlQDMDTO.getType() == null))  {
									appliedListModel.add(cqlQDMDTO);
									//Update existing Table value set list
									for(CQLQualityDataSetDTO cqlQualityDataSetDTO : appliedValueSetTableList){
										if(cqlQualityDataSetDTO.getId().equals(cqlQDMDTO.getId())){
											cqlQualityDataSetDTO.setOriginalCodeListName(cqlQDMDTO.getOriginalCodeListName());
											cqlQualityDataSetDTO.setCodeListName(cqlQDMDTO.getCodeListName());
										}
									}
									// update existing Value set list for Insert Button and short cut keys
									for(CQLIdentifierObject cqlIdentifierObject : MatContext.get().getValuesets()){
										if(cqlIdentifierObject.getId().equals(cqlQDMDTO.getId())){
											cqlIdentifierObject.setIdentifier(cqlQDMDTO.getCodeListName());
										}
									}
									// Update value set list for Attribute builder.
									for(CQLQualityDataSetDTO dataSetDTO : MatContext.get().getValueSetCodeQualityDataSetList()){
										if(dataSetDTO.getId().equals(cqlQDMDTO.getId())){
											dataSetDTO.setOriginalCodeListName(cqlQDMDTO.getOriginalCodeListName());
											dataSetDTO.setCodeListName(cqlQDMDTO.getCodeListName());
										}
									}
								}
							}
							searchDisplay.getValueSetView().buildAppliedValueSetCellTable(appliedListModel,
									MatContext.get().getLibraryLockService().checkForEditPermission());
							searchDisplay.getCqlLeftNavBarPanelView().setAppliedQdmTableList(appliedValueSetTableList);
							searchDisplay.getCqlLeftNavBarPanelView().updateValueSetMap(appliedValueSetTableList);
							
						} else {
							searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
									searchDisplay.getValueSetView().convertMessage(result.getFailureReason()));
						}
						showSearchingBusy(false);
					}
				});
	}

	/**
	 * Gets the profile list.
	 *
	 * @param list
	 *            the list
	 * @return the profile list
	 */
	/*private List<? extends HasListBox> getProfileList(List<VSACExpansionProfile> list) {
		return list;
	}*/

	/**
	 * Gets the version list.
	 *
	 * @param list
	 *            the list
	 * @return the version list
	 */
	private List<? extends HasListBox> getVersionList(List<VSACVersion> list) {
		return list;
	}

	/**
	 * Gets the VSAC version list by oid. if the default Expansion Profile is
	 * present then we are not making this VSAC call.
	 * 
	 * @param oid
	 *            the oid
	 * @return the VSAC version list by oid
	 */
	private void getVSACVersionListByOID(String oid) {
		vsacapiService.getAllVersionListByOID(oid, new AsyncCallback<VsacApiResult>() {

			@Override
			public void onSuccess(VsacApiResult result) {

				if (result.getVsacVersionResp() != null) {
					searchDisplay.getValueSetView()
							.setQDMVersionListBoxOptions(getVersionList(result.getVsacVersionResp()));
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getVSAC_RETRIEVE_FAILED());
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().setVisible(true);
				// showSearchingBusy(false);
			}
		});

	}

	/**
	 * Search value set in vsac.
	 *
	 * @param version
	 *            the version
	 * @param expansionProfile
	 *            the expansion profile
	 */
	private void searchValueSetInVsac(String version, String expansionProfile) {
		showSearchingBusy(true);
		currentMatValueSet = null;
		final String oid = searchDisplay.getValueSetView().getOIDInput().getValue();
		if (!MatContext.get().isUMLSLoggedIn()) {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN());
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().setVisible(true);
			showSearchingBusy(false);
			return;
		}

		// OID validation.
		if ((oid == null) || oid.trim().isEmpty()) {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getUMLS_OID_REQUIRED());
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().setVisible(true);
			showSearchingBusy(false);
			return;
		}
		
		vsacapiService.getMostRecentValueSetByOID(oid, expansionProfile, new AsyncCallback<VsacApiResult>() {

			@Override
			public void onFailure(final Throwable caught) {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getVSAC_RETRIEVE_FAILED());
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().setVisible(true);
				showSearchingBusy(false);
			}
			@Override
			public void onSuccess(final VsacApiResult result) {
				// to get the VSAC version list corresponding the OID
				if (result.isSuccess()) {
					List<MatValueSet> matValueSets = result.getVsacResponse();
					if (matValueSets != null) {
						MatValueSet matValueSet = matValueSets.get(0);
						currentMatValueSet = matValueSet;
					}
					searchDisplay.getValueSetView().getOIDInput().setTitle(oid);
					searchDisplay.getValueSetView().getUserDefinedInput()
							.setValue(matValueSets.get(0).getDisplayName());
					searchDisplay.getValueSetView().getUserDefinedInput()
							.setTitle(matValueSets.get(0).getDisplayName());
					searchDisplay.getValueSetView().getVersionListBox().setEnabled(true);

					searchDisplay.getValueSetView().getSaveButton().setEnabled(true);
					getVSACVersionListByOID(oid);
					searchDisplay.getValueSetView().getVersionListBox().setEnabled(true);
					
					searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getValuesetSuccessfulReterivalMessage(matValueSets.get(0).getDisplayName()));
					searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().setVisible(true);

				} else {
					String message = searchDisplay.getValueSetView().convertMessage(result.getFailureReason());
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(message);
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().setVisible(true);
				}
				showSearchingBusy(false);
			}
		});
	}
	
	
	/**
	 * Search CQL codes in vsac.
	 */
	private void searchCQLCodesInVsac() {

		final String url = searchDisplay.getCodesView().getCodeSearchInput().getValue().trim();
		searchDisplay.getCodesView().getCodeSearchInput().setText(url);
		
		if (!MatContext.get().isUMLSLoggedIn()) {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN());
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().setVisible(true);

			return;
		}
		
		// Code Identifier validation.
		if ((url == null) || url.trim().isEmpty()) {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getUMLS_CODE_IDENTIFIER_REQUIRED());
			
			return;
		}
		
		// Code Identifier validation to check Identifier starts with "CODE:"
		if(validator.validateForCodeIdentifier(url)){
			searchDisplay.getCodesView().getSaveButton().setEnabled(false);
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
					MatContext.get().getMessageDelegate().getUMLS_INVALID_CODE_IDENTIFIER());
			
			return;
		} else {
			retrieveCodeReferences(url);
		}
		
	}

	
	/**
	 * Retrieve code references.
	 *
	 * @param url the url
	 */
	private void retrieveCodeReferences(String url){
		
		showSearchingBusy(true);
		
		vsacapiService.getDirectReferenceCode(url, new AsyncCallback<VsacApiResult>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate()
						.getGenericErrorMessage());
				searchDisplay.getCodesView().showSearchingBusyOnCodes(false);
				
			}

			@Override
			public void onSuccess(VsacApiResult result) {
				
				if (result.isSuccess()) {
					searchDisplay.getCodesView().getCodeDescriptorInput().setValue(result.getDirectReferenceCode().getCodeDescriptor());
					searchDisplay.getCodesView().getCodeInput().setValue(result.getDirectReferenceCode().getCode());
					searchDisplay.getCodesView().getCodeSystemInput().setValue(result.getDirectReferenceCode().getCodeSystemName());
					searchDisplay.getCodesView().getCodeSystemVersionInput().setValue(result.getDirectReferenceCode().getCodeSystemVersion());
					searchDisplay.getCodesView().setCodeSystemOid(result.getDirectReferenceCode().getCodeSystemOid());
					searchDisplay.getCodesView().getSaveButton().setEnabled(true);
					
					searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert("Code "+result.getDirectReferenceCode().getCode()+" successfully retrieved from VSAC.");
				} else if (result.getFailureReason() == 5) { 
					 searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getUMLS_INVALID_CODE_IDENTIFIER());
					 
				 } else {
					 searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getVSAC_RETRIEVE_FAILED());
				 }
				
				showSearchingBusy(false);
				//508 : Shift focus to code search panel.
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCodesView().getCodeSearchInput());
			}
		});
	}


	/**
	 * Modify QDM.
	 *
	 * @param isUserDefined
	 *            the is user defined
	 */
	protected final void modifyValueSetOrUserDefined(final boolean isUserDefined) {
		if (!isUserDefined) { // Normal Available Value Set Flow
			modifyValueSet();
		} else { // Pseudo Value set Flow
			modifyUserDefinedValueSet();
		}
	}

	/**
	 * Modify value set QDM.
	 */
	private void modifyValueSet() {
		// Normal Available QDM Flow
		MatValueSet modifyWithDTO = currentMatValueSet;
		if ((modifyValueSetDTO != null) && (modifyWithDTO != null)) {
			
			String version;
			String originalName = searchDisplay.getValueSetView().getUserDefinedInput().getText();
			String suffix = searchDisplay.getValueSetView().getSuffixInput().getValue();
			String displayName = (!originalName.isEmpty() ? originalName : "")  + (!suffix.isEmpty() ? " (" + suffix + ")" : "");
			version = searchDisplay.getValueSetView()
					.getVersionValue(searchDisplay.getValueSetView().getVersionListBox());
			if (version == null) {
				version = "";
			}
			if (modifyValueSetDTO.getVersion() == null) {
				modifyValueSetDTO.setVersion("");
			}

			modifyValueSetList(modifyValueSetDTO);

			if (!CheckNameInValueSetList(displayName)) {
				if(!searchDisplay.getValueSetView().getSuffixInput().getValue().isEmpty()){
					modifyValueSetDTO.setSuffix(searchDisplay.getValueSetView().getSuffixInput().getValue());
					modifyValueSetDTO.setCodeListName(originalName+" ("+searchDisplay.getValueSetView().getSuffixInput().getValue()+")");
				} else {
					modifyValueSetDTO.setCodeListName(originalName);
					modifyValueSetDTO.setSuffix(null);
				}
				
				/*modifyValueSetDTO.setCodeListName(displayName);
				modifyValueSetDTO.setSuffix(suffix);*/
				modifyValueSetDTO.setOriginalCodeListName(originalName);
				//modifyWithDTO.setDisplayName(displayName);
				updateAppliedValueSetsList(modifyWithDTO, null, modifyValueSetDTO, false);
			}
			getUsedArtifacts();
		} else {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getMODIFY_VALUE_SET_SELECT_ATLEAST_ONE());
		}
	}

	/**
	 * Modify QDM list.
	 *
	 * @param qualityDataSetDTO
	 *            the quality data set DTO
	 */
	private void modifyValueSetList(CQLQualityDataSetDTO qualityDataSetDTO) {
		for (int i = 0; i < appliedValueSetTableList.size(); i++) {
			if (qualityDataSetDTO.getCodeListName().equals(appliedValueSetTableList.get(i).getCodeListName())) {
				appliedValueSetTableList.remove(i);
				break;

			}
		}
	}

	/**
	 * Update applied Value Set list.
	 *
	 * @param matValueSet
	 *            the mat value set
	 * @param codeListSearchDTO
	 *            the code list search DTO
	 * @param qualityDataSetDTO
	 *            the quality data set DTO
	 * @param isUSerDefined
	 *            the is U ser defined
	 */
	private void updateAppliedValueSetsList(final MatValueSet matValueSet, final CodeListSearchDTO codeListSearchDTO,
			final CQLQualityDataSetDTO qualityDataSetDTO, final boolean isUSerDefined) {

		String version = searchDisplay.getValueSetView()
				.getVersionValue(searchDisplay.getValueSetView().getVersionListBox());
		
		CQLValueSetTransferObject matValueSetTransferObject = new CQLValueSetTransferObject();
		matValueSetTransferObject.setCqlLibraryId(MatContext.get().getCurrentCQLLibraryId());
		matValueSetTransferObject.setMatValueSet(matValueSet);
		matValueSetTransferObject.setCodeListSearchDTO(codeListSearchDTO);
		matValueSetTransferObject.setCqlQualityDataSetDTO(qualityDataSetDTO);
		matValueSetTransferObject.setAppliedQDMList(appliedValueSetTableList);
		
		int versionSelectionIndex = searchDisplay.getValueSetView().getVersionListBox().getSelectedIndex();
		if ((version != null)) {
			if (!version.equalsIgnoreCase(MatContext.PLEASE_SELECT) && !version.equalsIgnoreCase("")) {
				matValueSetTransferObject.setVersion(true);
				currentMatValueSet.setVersion(
						searchDisplay.getValueSetView().getVersionListBox().getValue(versionSelectionIndex));
			}
		}
		matValueSetTransferObject.scrubForMarkUp();
		showSearchingBusy(true);
		MatContext.get().getLibraryService().modifyCQLValueSets(matValueSetTransferObject,
				new AsyncCallback<SaveUpdateCQLResult>() {
					@Override
					public void onFailure(final Throwable caught) {
						searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
								.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						showSearchingBusy(false);
						isModified = false;
						modifyValueSetDTO = null;
						currentMatValueSet = null;
						searchDisplay.getValueSetView().getSaveButton().setEnabled(false);
					}

					@Override
					public void onSuccess(final SaveUpdateCQLResult result) {
						if (result != null) {
							if (result.isSuccess()) {
								isModified = false;
								modifyValueSetDTO = null;
								currentMatValueSet = null;
								searchDisplay.getValueSetView().resetCQLValuesetearchPanel();
								//modifyValueSetDTO = result.getCqlQualityDataSetDTO();
								searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(
										MatContext.get().getMessageDelegate().getSUCCESSFUL_MODIFY_APPLIED_VALUESET());
								getAppliedValueSetList();
							} else {

								if (result.getFailureReason() == SaveUpdateCodeListResult.ALREADY_EXISTS) {
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
											MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(result.getCqlQualityDataSetDTO().getCodeListName()));

								} else if (result
										.getFailureReason() == SaveUpdateCodeListResult.SERVER_SIDE_VALIDATION) {
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
											.createAlert("Invalid Input data.");
								}
							}
						}
						searchDisplay.getValueSetView().getSaveButton().setEnabled(false);
						showSearchingBusy(false);
					}
				});

	}

	/**
	 * Check name in QDM list.
	 *
	 * @param userDefinedInput
	 *            the user defined input
	 * @return true, if successful
	 */
	private boolean CheckNameInValueSetList(String userDefinedInput) {
		if (appliedValueSetTableList.size() > 0) {
			Iterator<CQLQualityDataSetDTO> iterator = appliedValueSetTableList.iterator();
			while (iterator.hasNext()) {
				CQLQualityDataSetDTO dataSetDTO = iterator.next();
				if (dataSetDTO.getCodeListName().equalsIgnoreCase(userDefinedInput)) {
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
							.createAlert(MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(userDefinedInput));
					return true;

				}
			}
		}
		return false;
	}

	/**
	 * Adds the selected code listto library.
	 *
	 * @param isUserDefinedValueSet
	 *            the is user defined QDM
	 */
	private void addNewValueSet(final boolean isUserDefinedValueSet) {
		if (!isUserDefinedValueSet) {
			addVSACCQLValueset();
		} else {
			addUserDefinedValueSet();
		}
	}

	/**
	 * Adds the QDS with value set.
	 */
	private void addVSACCQLValueset() {

		String libraryID = MatContext.get().getCurrentCQLLibraryId();
		CQLValueSetTransferObject matValueSetTransferObject = createValueSetTransferObject(libraryID);
		matValueSetTransferObject.scrubForMarkUp();
		String originalCodeListName = matValueSetTransferObject.getMatValueSet().getDisplayName(); 
		String suffix = searchDisplay.getValueSetView().getSuffixInput().getValue();
		final String codeListName = (originalCodeListName!=null ? originalCodeListName : "") + (!suffix.isEmpty() ? " (" + suffix + ")" : "");
		
		String version = matValueSetTransferObject.getMatValueSet().getVersion();
		if (version == null) {
			version = "";
		}
		// Check if QDM name already exists in the list.
		if (!CheckNameInValueSetList(codeListName)) {
			showSearchingBusy(true);
			MatContext.get().getLibraryService().saveCQLValueset(matValueSetTransferObject,
					new AsyncCallback<SaveUpdateCQLResult>() {

						@Override
						public void onFailure(Throwable caught) {
							showSearchingBusy(false);
							if (appliedValueSetTableList.size() > 0) {
								appliedValueSetTableList.removeAll(appliedValueSetTableList);
							}
							currentMatValueSet = null;
							searchDisplay.getValueSetView().getSaveButton().setEnabled(false);
						}

						@Override
						public void onSuccess(SaveUpdateCQLResult result) {
							String message = "";
							if (result != null) {
								if (result.isSuccess()) {

									message = MatContext.get().getMessageDelegate()
											.getValuesetSuccessMessage(codeListName);
									MatContext.get().getEventBus().fireEvent(new QDSElementCreatedEvent(codeListName));
									searchDisplay.getValueSetView().resetCQLValuesetearchPanel();
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
											.createAlert(message);
									getAppliedValueSetList();
								} else {
									if (result.getFailureReason() == SaveUpdateCodeListResult.ALREADY_EXISTS) {
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
												MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(result.getCqlQualityDataSetDTO().getCodeListName()));
									}
								}
							}
							getUsedArtifacts();
							currentMatValueSet = null;
							searchDisplay.getValueSetView().getSaveButton().setEnabled(false);
						
							showSearchingBusy(false);
						}
					});
		}

	}

	/**
	 * Adds the QDS with out value set.
	 */
	private void addUserDefinedValueSet() {

		CQLValueSetTransferObject matValueSetTransferObject = createValueSetTransferObject(
				MatContext.get().getCurrentCQLLibraryId());
		matValueSetTransferObject.scrubForMarkUp();
		matValueSetTransferObject.setMatValueSet(null);
		if ((matValueSetTransferObject.getUserDefinedText().length() > 0)) {
			ValueSetNameInputValidator valueSetNameInputValidator = new ValueSetNameInputValidator();
			String message = valueSetNameInputValidator.validate(matValueSetTransferObject);
			if (message.isEmpty()) {
				final String userDefinedInput = matValueSetTransferObject.getCqlQualityDataSetDTO().getCodeListName(); 
				
				String version = searchDisplay.getValueSetView()
						.getVersionValue(searchDisplay.getValueSetView().getVersionListBox());
				
				if (version == null) {
					version = "";
				}
				// Check if QDM name already exists in the list.
				if (!CheckNameInValueSetList(userDefinedInput)) {
					showSearchingBusy(true);
					MatContext.get().getLibraryService().saveCQLUserDefinedValueset(matValueSetTransferObject,
							new AsyncCallback<SaveUpdateCQLResult>() {
								@Override
								public void onFailure(final Throwable caught) {
									Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
									showSearchingBusy(false);
									searchDisplay.getValueSetView().getSaveButton().setEnabled(false);
								}

								@SuppressWarnings("static-access")
								@Override
								public void onSuccess(final SaveUpdateCQLResult result) {
									if (result != null) {
										if (result.isSuccess()) {
											if (result.getXml() != null) {

												String message = MatContext.get().getMessageDelegate()
														.getValuesetSuccessMessage(userDefinedInput);
												searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
														.createAlert(message);
												MatContext.get().setValuesets(result.getCqlAppliedQDMList());
												searchDisplay.getValueSetView().resetCQLValuesetearchPanel();
												getAppliedValueSetList();
											}
										} else {
											if (result.getFailureReason() == result.ALREADY_EXISTS) {
												searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
														.createAlert(MatContext.get().getMessageDelegate()
																.getDuplicateAppliedValueSetMsg(result.getCqlQualityDataSetDTO().getCodeListName()));
											} else if (result.getFailureReason() == result.SERVER_SIDE_VALIDATION) {
												searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
														.createAlert("Invalid input data.");
											}
										}
									}
									getUsedArtifacts();
									searchDisplay.getValueSetView().getSaveButton().setEnabled(false);
									showSearchingBusy(false);
								}
							});

				}
			} else {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(message);
			}

		} else {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getVALIDATION_MSG_ELEMENT_WITHOUT_VSAC());
		}

	}

	/**
	 * Creates the value set transfer object.
	 *
	 * @param libraryID
	 *            the library ID
	 * @return the CQL value set transfer object
	 */
	private CQLValueSetTransferObject createValueSetTransferObject(String libraryID) {
		if(currentMatValueSet == null) {
			currentMatValueSet = new MatValueSet();
		}
		String version = searchDisplay.getValueSetView()
				.getVersionValue(searchDisplay.getValueSetView().getVersionListBox());
		
		int versionSelectionIndex = searchDisplay.getValueSetView().getVersionListBox().getSelectedIndex();

		CQLValueSetTransferObject matValueSetTransferObject = new CQLValueSetTransferObject();
		matValueSetTransferObject.setCqlLibraryId(libraryID);
		
		String originalCodeListName = searchDisplay.getValueSetView().getUserDefinedInput().getValue(); 
		/*String suffix = searchDisplay.getValueSetView().getSuffixInput().getValue();
		String codeListName = (!originalCodeListName.isEmpty() ? originalCodeListName : "") + (!suffix.isEmpty() ? " (" + suffix +")" : "");*/
		
		
		
		matValueSetTransferObject.setCqlQualityDataSetDTO(new CQLQualityDataSetDTO());
		matValueSetTransferObject.getCqlQualityDataSetDTO().setOriginalCodeListName(originalCodeListName);
		/*matValueSetTransferObject.getCqlQualityDataSetDTO().setSuffix(suffix);
		matValueSetTransferObject.getCqlQualityDataSetDTO().setCodeListName(codeListName);*/
		if(!searchDisplay.getValueSetView().getSuffixInput().getValue().isEmpty()){
			matValueSetTransferObject.getCqlQualityDataSetDTO().setSuffix(searchDisplay.getValueSetView().getSuffixInput().getValue());
			matValueSetTransferObject.getCqlQualityDataSetDTO().setCodeListName(originalCodeListName+" ("+searchDisplay.getValueSetView().getSuffixInput().getValue()+")");
		} else {
			matValueSetTransferObject.getCqlQualityDataSetDTO().setCodeListName(originalCodeListName);
		}
		CodeListSearchDTO codeListSearchDTO = new CodeListSearchDTO();
		codeListSearchDTO.setName(searchDisplay.getValueSetView().getUserDefinedInput().getText());
		matValueSetTransferObject.setCodeListSearchDTO(codeListSearchDTO);
		matValueSetTransferObject.setAppliedQDMList(appliedValueSetTableList);
		if ((version != null) ) {
			if (!version.equalsIgnoreCase(MatContext.PLEASE_SELECT) && !version.equalsIgnoreCase("")) {
				matValueSetTransferObject.setVersion(true);
				currentMatValueSet.setVersion(
						searchDisplay.getValueSetView().getVersionListBox().getValue(versionSelectionIndex));
			}
		}

		
		matValueSetTransferObject.setMatValueSet(currentMatValueSet);
		matValueSetTransferObject.setCqlLibraryId(libraryID);
		matValueSetTransferObject.setUserDefinedText(searchDisplay.getValueSetView().getUserDefinedInput().getText());
		return matValueSetTransferObject;
	}

	/**
	 * Modify QDM with out value set.
	 */
	private void modifyUserDefinedValueSet() {
		//modifyValueSetDTO.setExpansionIdentifier("");
		modifyValueSetDTO.setVersion("");
		if ((searchDisplay.getValueSetView().getUserDefinedInput().getText().trim().length() > 0)) {
			String originalName = searchDisplay.getValueSetView().getUserDefinedInput().getText();
			String suffix = searchDisplay.getValueSetView().getSuffixInput().getValue();
			String usrDefDisplayName = (!originalName.isEmpty() ? originalName : "") + (!suffix.isEmpty() ? " (" + suffix + ")" : ""); 
			/*String expProfile = searchDisplay.getValueSetView()
					.getExpansionProfileValue(searchDisplay.getValueSetView().getQDMExpProfileListBox());*/
			String version = searchDisplay.getValueSetView()
					.getVersionValue(searchDisplay.getValueSetView().getVersionListBox());
			/*if (expProfile == null) {
				expProfile = "";
			}*/
			if (version == null) {
				version = "";
			}

			modifyValueSetList(modifyValueSetDTO);
			if (!CheckNameInValueSetList(usrDefDisplayName)) {
				CQLValueSetTransferObject object = new CQLValueSetTransferObject();
				object.setUserDefinedText(searchDisplay.getValueSetView().getUserDefinedInput().getText());
				object.scrubForMarkUp();
				ValueSetNameInputValidator valueSetNameInputValidator = new ValueSetNameInputValidator();
				/* qdmInputValidator.validate(object); */
				String message = valueSetNameInputValidator.validate(object);
				if (message.isEmpty()) {

					CodeListSearchDTO modifyWithDTO = new CodeListSearchDTO();
					modifyWithDTO.setName(usrDefDisplayName);
					modifyValueSetDTO.setOriginalCodeListName(originalName);
					modifyValueSetDTO.setSuffix(suffix);
					modifyValueSetDTO.setCodeListName(usrDefDisplayName);
					updateAppliedValueSetsList(null, modifyWithDTO, modifyValueSetDTO, true);
				} else {
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(message);
				}
			}
			getUsedArtifacts();
		} else {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getVALIDATION_MSG_ELEMENT_WITHOUT_VSAC());
		}
	}

	
	/**
	 * Build View for General info when General Info AnchorList item is clicked.
	 */
	private void generalInfoEvent() {
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		searchDisplay.getValueSetView().getCellTableMainPanel().clear();
		searchDisplay.getCodesView().getCellTableMainPanel().clear();
		searchDisplay.hideInformationDropDown();
		if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
			searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();

		} else {
			unsetActiveMenuItem(currentSection);
			searchDisplay.getCqlLeftNavBarPanelView().getGeneralInformation().setActive(true);
			currentSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
			searchDisplay.buildGeneralInformation();
			boolean isValidQDMVersion = searchDisplay.getCqlLeftNavBarPanelView().checkForIncludedLibrariesQDMVersion();
			if(!isValidQDMVersion){
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getINVALID_QDM_VERSION_IN_INCLUDES());
			} else {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
			}
			searchDisplay.getCqlGeneralInformationView().getLibraryNameValue().setText(cqlLibraryName);
		}
		searchDisplay.setGeneralInfoHeading();
	}

	/**
	 * Applied QDM event.
	 */
	private void appliedQDMEvent() {
		// server
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		searchDisplay.hideInformationDropDown();
		if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_APPLIED_QDM;
			searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();

		} else {
			unsetActiveMenuItem(currentSection);
			searchDisplay.getCqlLeftNavBarPanelView().getAppliedQDM().setActive(true);
			currentSection = CQLWorkSpaceConstants.CQL_APPLIED_QDM;
			buildAppliedQDMTable();
		}
		searchDisplay.getValueSetView().setHeading("CQL Library Workspace > Value Sets", "subQDMAPPliedListContainerPanel");
		Mat.focusSkipLists("CqlComposer");
	}
	
	/**
	 * Builds the applied QDM table.
	 */
	private void buildAppliedQDMTable() {
		searchDisplay.buildAppliedQDM();
       // setExpansionProfilePanelValues();
		boolean isEditable = MatContext.get().getLibraryLockService().checkForEditPermission();
		
		// initialize the valuesets to be used, getUsedArtifacts() will update with the proper value
		for(CQLQualityDataSetDTO valuset : searchDisplay.getCqlLeftNavBarPanelView().getAppliedQdmTableList()) {
			valuset.setUsed(true);
		}
		
		searchDisplay.getValueSetView().buildAppliedValueSetCellTable(searchDisplay.getCqlLeftNavBarPanelView().getAppliedQdmTableList(), isEditable);
		searchDisplay.getValueSetView().resetCQLValuesetearchPanel();
		searchDisplay.getValueSetView().setWidgetsReadOnly(isEditable);
		getUsedArtifacts();
	}
	
	/**
	 * codes event.
	 */
	private void codesEvent() {
		// server
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_CODES;
			searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();

		} else {
			unsetActiveMenuItem(currentSection);
			searchDisplay.getCqlLeftNavBarPanelView().getCodesLibrary().setActive(true);
			currentSection = CQLWorkSpaceConstants.CQL_CODES;
			//searchDisplay.getCodesView().showSearchingBusyOnCodes(true);
			searchDisplay.buildCodes();
			searchDisplay.getCodesView().buildCodesCellTable(
					appliedCodeTableList,
					MatContext.get().getLibraryLockService().checkForEditPermission());
			searchDisplay.getCodesView().resetCQLCodesSearchPanel();
			searchDisplay.getCodesView()
					.setWidgetsReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
			getUsedCodes();
		}
		searchDisplay.getCodesView().setHeading("CQL Library Workspace > Codes", "codesContainerPanel");
		Mat.focusSkipLists("CqlComposer");
	}

	/**
	 * Method to find used codes for delete button state.
	 */
	private void getUsedCodes() {
		searchDisplay.getCodesView().showSearchingBusyOnCodes(true);
		
		MatContext.get().getLibraryService().getUsedCqlArtifacts(
				MatContext.get().getCurrentCQLLibraryId(),
		        new AsyncCallback<GetUsedCQLArtifactsResult>() {

		               @Override
		               public void onFailure(Throwable caught) {
		                      Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
		                      searchDisplay.getCodesView().showSearchingBusyOnCodes(false);
		               }

		               @Override
		               public void onSuccess(GetUsedCQLArtifactsResult result) {
		            	searchDisplay.getCodesView().showSearchingBusyOnCodes(false);
		               	// if there are errors, set the codes to not used.
		               	if(!result.getCqlErrors().isEmpty()) {
		               		for(CQLCode cqlCode : appliedCodeTableList){
		                         cqlCode.setUsed(false);
		               		}
		               	}                           	
		               	// otherwise, check if the valueset is in the used valusets list
		               	else {
		                      for(CQLCode cqlCode : appliedCodeTableList){
		                             //
		                    	  if (result.getUsedCQLcodes().contains(cqlCode.getDisplayName())) {
		                                    cqlCode.setUsed(true);
		                             } else{
		                           	  cqlCode.setUsed(false);
		                             }
		                      }
		               	}
		               	       
		               	if(searchDisplay.getCqlLeftNavBarPanelView().getAppliedCodeTableList().size() > 0) {
		                   	searchDisplay.getCodesView().getCelltable().redraw();
		                   	searchDisplay.getCodesView().getListDataProvider().refresh();
		               	}
		               }
		               
		        });
	}

	/**
	 * Build View for Parameter when Parameter AnchorList item is clicked.
	 */
	private void parameterEvent() {
		unsetActiveMenuItem(currentSection);
		searchDisplay.hideInformationDropDown();
		searchDisplay.getValueSetView().getCellTableMainPanel().clear();
		searchDisplay.getCodesView().getCellTableMainPanel().clear();
		searchDisplay.getCqlLeftNavBarPanelView().getParameterLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_PARAMETER_MENU;
		searchDisplay.buildParameterLibraryView();

		searchDisplay.getCQLParametersView()
				.setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
		//checking if its Draft.
		if(MatContext.get().getCurrentCQLLibraryVersion().toLowerCase().contains(CQLWorkSpaceConstants.CQL_DRAFT.toLowerCase())){
			searchDisplay.getCQLParametersView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
		}
		searchDisplay.getParameterButtonBar().getDeleteButton().setEnabled(false);
		searchDisplay.getParameterButtonBar().getDeleteButton().setTitle("Delete");
		curAceEditor = searchDisplay.getCQLParametersView().getParameterAceEditor();
		searchDisplay.getCQLParametersView().setHeading("CQL Library Workspace > Parameter", "mainParamViewVerticalPanel");
		Mat.focusSkipLists("CqlComposer");
	}

	/**
	 * Build View for Includes when Includes AnchorList item is clicked.
	 */
	private void includesEvent() {
		unsetActiveMenuItem(currentSection);
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		searchDisplay.getValueSetView().getCellTableMainPanel().clear();
		searchDisplay.getCodesView().getCellTableMainPanel().clear();
		searchDisplay.getCqlLeftNavBarPanelView().getIncludesLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_INCLUDES_MENU;
		searchDisplay.getMainFlowPanel().clear();
		searchDisplay.hideInformationDropDown();
		searchDisplay.getIncludeView().setIncludedList(searchDisplay.getCqlLeftNavBarPanelView()
				.getIncludedList(searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap()));
		searchDisplay.buildIncludesView();
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		result.setCqlLibraryDataSetObjects(new ArrayList<CQLLibraryDataSetObject>());
		searchDisplay.getIncludeView().buildIncludeLibraryCellTable(result,
				MatContext.get().getLibraryLockService().checkForEditPermission(), true);
		//getAllIncludeLibraryList(searchDisplay.getIncludeView().getSearchTextBox().getText());
		searchDisplay.getIncludeView().getAliasNameTxtArea().setText("");
		searchDisplay.getIncludeView().getSearchTextBox().setText("");
		searchDisplay.getCqlIncludeLibraryView()
				.setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
		searchDisplay.getIncludeView().setHeading("CQL Library Workspace > Includes", "IncludeSectionContainerPanel");
		Mat.focusSkipLists("CqlComposer");
	}

	/**
	 * Build View for Definition when Definition AnchorList item is clicked.
	 */
	private void definitionEvent() {
		unsetActiveMenuItem(currentSection);
		searchDisplay.hideInformationDropDown();
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		searchDisplay.getValueSetView().getCellTableMainPanel().clear();
		searchDisplay.getCodesView().getCellTableMainPanel().clear();

		searchDisplay.getCqlLeftNavBarPanelView().getDefinitionLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_DEFINE_MENU;
		searchDisplay.buildDefinitionLibraryView();

		searchDisplay.getCQLDefinitionsView()
				.setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
		//Checks if Draft
		if(MatContext.get().getCurrentCQLLibraryVersion().toLowerCase().contains(CQLWorkSpaceConstants.CQL_DRAFT.toLowerCase())){
			searchDisplay.getCQLDefinitionsView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
		}
		searchDisplay.getDefineButtonBar().getDeleteButton().setEnabled(false);
		searchDisplay.getDefineButtonBar().getDeleteButton().setTitle("Delete");
		curAceEditor = searchDisplay.getCQLDefinitionsView().getDefineAceEditor();
		searchDisplay.getCQLDefinitionsView().setHeading("CQL Library Workspace > Definition", "mainDefViewVerticalPanel");
		Mat.focusSkipLists("CqlComposer");
	}

	/**
	 * Build View for Function when Funtion AnchorList item is clicked.
	 */
	private void functionEvent() {
		searchDisplay.hideInformationDropDown();
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		searchDisplay.getValueSetView().getCellTableMainPanel().clear();
		searchDisplay.getCodesView().getCellTableMainPanel().clear();
		unsetActiveMenuItem(currentSection);
		searchDisplay.getCqlLeftNavBarPanelView().getFunctionLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_FUNCTION_MENU;
		searchDisplay.buildFunctionLibraryView();
		searchDisplay.getCQLFunctionsView()
				.setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
		//Checks if Draft
		if(MatContext.get().getCurrentCQLLibraryVersion().toLowerCase().contains(CQLWorkSpaceConstants.CQL_DRAFT.toLowerCase())){
			searchDisplay.getCQLFunctionsView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
		}
		searchDisplay.getFunctionButtonBar().getDeleteButton().setEnabled(false);
		searchDisplay.getFunctionButtonBar().getDeleteButton().setTitle("Delete");
		curAceEditor = searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor();
		searchDisplay.getCQLFunctionsView().setHeading("CQL Library Workspace > Function", "mainFuncViewVerticalPanel");
		Mat.focusSkipLists("CqlComposer");
	}

	/**
	 * Build View for View Cql when View Cql AnchorList item is clicked.
	 */
	private void viewCqlEvent() {
		searchDisplay.hideInformationDropDown();
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		searchDisplay.getValueSetView().getCellTableMainPanel().clear();
		searchDisplay.getCodesView().getCellTableMainPanel().clear();
		if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_VIEW_MENU;
			searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
		} else {
			unsetActiveMenuItem(currentSection);
			searchDisplay.getCqlLeftNavBarPanelView().getViewCQL().setActive(true);
			currentSection = CQLWorkSpaceConstants.CQL_VIEW_MENU;
			searchDisplay.buildCQLFileView();
			buildCQLView();
		}
		searchDisplay.getViewCQLView().setHeading("CQL Library Workspace > View CQL", "cqlViewCQL_Id");
		Mat.focusSkipLists("CqlComposer");
	}

	/**
	 * Method to build View for Anchor List item View CQL.
	 */
	private void buildCQLView() {
		searchDisplay.getCqlAceEditor().setText("");
		showSearchingBusy(true);
		MatContext.get().getCQLLibraryService().getCQLLibraryFileData(MatContext.get().getCurrentCQLLibraryId(),
				new AsyncCallback<SaveUpdateCQLResult>() {
					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						if (result.isSuccess()) {
							if ((result.getCqlString() != null) && !result.getCqlString().isEmpty()) {
								// validateViewCQLFile(result.getCqlString());
								// searchDisplay.getCqlAceEditor().setText(result.getCqlString());

								searchDisplay.getCqlAceEditor().clearAnnotations();
								searchDisplay.getCqlAceEditor().removeAllMarkers();
								searchDisplay.getCqlAceEditor().redisplay();
								searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
								searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().clearAlert();
								searchDisplay.getCqlLeftNavBarPanelView().getWarningConfirmationMessageAlert().clearAlert();

								if (!result.getCqlErrors().isEmpty()) {
									searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().createAlert(
											MatContext.get().getMessageDelegate().getVIEW_CQL_ERROR_MESSAGE());
									for (CQLErrors error : result.getCqlErrors()) {
										String errorMessage = new String();
										errorMessage = errorMessage.concat("Error in line : " + error.getErrorInLine()
												+ " at Offset :" + error.getErrorAtOffeset());
										int line = error.getErrorInLine();
										int column = error.getErrorAtOffeset();
										searchDisplay.getCqlAceEditor().addAnnotation(line - 1, column,
												error.getErrorMessage(), AceAnnotationType.ERROR);
									}
									searchDisplay.getCqlAceEditor().setText(result.getCqlString());
									//searchDisplay.getCqlAceEditor().lockEditor(0, 0);
									searchDisplay.getCqlAceEditor().setAnnotations();
									searchDisplay.getCqlAceEditor().redisplay();
								} else if (!result.isDatatypeUsedCorrectly()) {
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
									searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
											.createAlert(MatContext.get().getMessageDelegate()
													.getVIEW_CQL_ERROR_MESSAGE_BAD_VALUESET_DATATYPE());
									searchDisplay.getCqlAceEditor().setText(result.getCqlString());
								} else {
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().setVisible(true);
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(
											MatContext.get().getMessageDelegate().getVIEW_CQL_NO_ERRORS_MESSAGE());
									searchDisplay.getCqlAceEditor().setText(result.getCqlString());
								}
							}

						}
						showSearchingBusy(false);
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						showSearchingBusy(false);
					}
				});

	}

	/**
	 * Unset active menu item.
	 *
	 * @param menuClickedBefore the menu clicked before
	 */
	private void unsetActiveMenuItem(String menuClickedBefore) {
		if (!searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
			searchDisplay.resetMessageDisplay();
			if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_GENERAL_MENU)) {
				searchDisplay.getCqlLeftNavBarPanelView().getGeneralInformation().setActive(false);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_PARAMETER_MENU)) {
				searchDisplay.getCqlLeftNavBarPanelView().getParameterLibrary().setActive(false);
				searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().setSelectedIndex(-1);
				if (searchDisplay.getCqlLeftNavBarPanelView().getParamCollapse().getElement().getClassName()
						.equalsIgnoreCase("panel-collapse collapse in")) {
					searchDisplay.getCqlLeftNavBarPanelView().getParamCollapse().getElement()
							.setClassName("panel-collapse collapse");
				}
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_DEFINE_MENU)) {
				searchDisplay.getCqlLeftNavBarPanelView().getDefinitionLibrary().setActive(false);
				searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().setSelectedIndex(-1);
				if (searchDisplay.getCqlLeftNavBarPanelView().getDefineCollapse().getElement().getClassName()
						.equalsIgnoreCase("panel-collapse collapse in")) {
					searchDisplay.getCqlLeftNavBarPanelView().getDefineCollapse().getElement()
							.setClassName("panel-collapse collapse");
				}
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_FUNCTION_MENU)) {
				searchDisplay.getCqlLeftNavBarPanelView().getFunctionLibrary().setActive(false);
				searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().setSelectedIndex(-1);
				if (searchDisplay.getCqlLeftNavBarPanelView().getFunctionCollapse().getElement().getClassName()
						.equalsIgnoreCase("panel-collapse collapse in")) {
					searchDisplay.getCqlLeftNavBarPanelView().getFunctionCollapse().getElement()
							.setClassName("panel-collapse collapse");
				}
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_VIEW_MENU)) {
				searchDisplay.getCqlLeftNavBarPanelView().getViewCQL().setActive(false);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_APPLIED_QDM)) {
				searchDisplay.getCqlLeftNavBarPanelView().getAppliedQDM().setActive(false);
			}else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_CODES)) {
				searchDisplay.getCqlLeftNavBarPanelView().getCodesLibrary().setActive(false);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_INCLUDES_MENU)) {
				searchDisplay.getCqlLeftNavBarPanelView().getIncludesLibrary().setActive(false);
				searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().setSelectedIndex(-1);
				if (searchDisplay.getCqlLeftNavBarPanelView().getIncludesCollapse().getElement().getClassName()
						.equalsIgnoreCase("panel-collapse collapse in")) {
					searchDisplay.getCqlLeftNavBarPanelView().getIncludesCollapse().getElement()
							.setClassName("panel-collapse collapse");
				}
			}
		}
	}

	/**
	 * This method is called at beforeDisplay and get searchButton click on
	 * Include section and reterives CQL Versioned libraries eligible to be
	 * included into any parent cql library.
	 *
	 * @param searchText
	 *            the search text
	 * @return the all include library list
	 */
	private void getAllIncludeLibraryList(final String searchText) {
		searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
		searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
		searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().clearAlert();
		showSearchingBusy(true);

		MatContext.get().getCQLLibraryService().searchForIncludes(setId, searchText, false,
				new AsyncCallback<SaveCQLLibraryResult>() {

					@Override
					public void onFailure(Throwable caught) {
						searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
								.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						showSearchingBusy(false);
					}

					@Override
					public void onSuccess(SaveCQLLibraryResult result) {
						showSearchingBusy(false);
						if (result != null && result.getCqlLibraryDataSetObjects().size() > 0) {
							searchDisplay.getCqlLeftNavBarPanelView()
									.setIncludeLibraryList(result.getCqlLibraryDataSetObjects());
							searchDisplay.buildIncludesView();
							searchDisplay.getIncludeView().buildIncludeLibraryCellTable(result,
									MatContext.get().getLibraryLockService().checkForEditPermission(), false);

						} else {
							searchDisplay.buildIncludesView();
							searchDisplay.getIncludeView().buildIncludeLibraryCellTable(result,
									MatContext.get().getLibraryLockService().checkForEditPermission(), false);
							if (!searchDisplay.getIncludeView().getSearchTextBox().getText().isEmpty())
								searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
										.createAlert(MatContext.get().getMessageDelegate().getNoIncludes());
						}

						if (searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox()
								.getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
							searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
									.createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
						} else {
							searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().clearAlert();
						}
						// 508 changes for Library Alias.
						searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getIncludeView().getAliasNameTxtArea());
					}
				});

	}

	/**
	 * Show searching busy.
	 *
	 * @param busy the busy
	 */
	private void showSearchingBusy(final boolean busy) {
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
		searchDisplay.getCqlLeftNavBarPanelView().getGeneralInformation().setEnabled(!busy);
		/*if (!currentSection.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_INCLUDES_MENU))*/
		searchDisplay.getCqlLeftNavBarPanelView().getIncludesLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getCodesLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getAppliedQDM().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getParameterLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getDefinitionLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getFunctionLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getViewCQL().setEnabled(!busy);
		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			if(currentSection.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_GENERAL_MENU)){
				searchDisplay.getCqlGeneralInformationView().setWidgetReadOnly(!busy);
			} else if (currentSection.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_INCLUDES_MENU)){
				searchDisplay.getIncludeView().getSaveButton().setEnabled(!busy);
				searchDisplay.getIncludeView().getEraseButton().setEnabled(!busy);
				searchDisplay.getIncludeView().getSearchButton().setEnabled(!busy);
			} else if (currentSection.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_APPLIED_QDM)){
				searchDisplay.getValueSetView().getSaveButton().setEnabled(!busy);
				searchDisplay.getValueSetView().getCancelQDMButton().setEnabled(!busy);
				searchDisplay.getValueSetView().getUpdateFromVSACButton().setEnabled(!busy);
				searchDisplay.getValueSetView().getRetrieveFromVSACButton().setEnabled(!busy);
				searchDisplay.getValueSetView().setIsLoading(busy);
			} else if (currentSection.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_CODES)){
				searchDisplay.getCodesView().getSaveButton().setEnabled(!busy);
				searchDisplay.getCodesView().getCancelCodeButton().setEnabled(!busy);
				searchDisplay.getCodesView().getRetrieveFromVSACButton().setEnabled(!busy);
			} else if (currentSection.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_PARAMETER_MENU)){
				searchDisplay.getCQLParametersView().getParameterButtonBar().getSaveButton().setEnabled(!busy);
				searchDisplay.getCQLParametersView().getParameterButtonBar().getEraseButton().setEnabled(!busy);
				searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(!busy);
				searchDisplay.getCQLParametersView().getParameterButtonBar().getInfoButton().setEnabled(!busy);
			} else if (currentSection.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_DEFINE_MENU)){
				searchDisplay.getCQLDefinitionsView().getDefineButtonBar().getSaveButton().setEnabled(!busy);
				searchDisplay.getCQLDefinitionsView().getDefineButtonBar().getEraseButton().setEnabled(!busy);
				searchDisplay.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(!busy);
				searchDisplay.getCQLDefinitionsView().getDefineButtonBar().getInsertButton().setEnabled(!busy);
				searchDisplay.getCQLDefinitionsView().getDefineButtonBar().getInfoButton().setEnabled(!busy);
			} else if (currentSection.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_FUNCTION_MENU)){
				searchDisplay.getCQLFunctionsView().getFunctionButtonBar().getSaveButton().setEnabled(!busy);
				searchDisplay.getCQLFunctionsView().getFunctionButtonBar().getEraseButton().setEnabled(!busy);
				searchDisplay.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(!busy);
				searchDisplay.getCQLFunctionsView().getFunctionButtonBar().getInsertButton().setEnabled(!busy);
				searchDisplay.getCQLFunctionsView().getFunctionButtonBar().getInfoButton().setEnabled(!busy);
				searchDisplay.getCQLFunctionsView().getAddNewArgument().setEnabled(!busy);
			} else if (currentSection.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_VIEW_MENU)){
				
			}
		}
		searchDisplay.getCqlLeftNavBarPanelView().setIsLoading(busy);
	}
	
	private void showSearchBusyOnDoubleClick(boolean busy) {
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
		searchDisplay.getCqlLeftNavBarPanelView().getGeneralInformation().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getCodesLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getAppliedQDM().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getParameterLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getDefinitionLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getFunctionLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getViewCQL().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getIncludesLibrary().setEnabled(!busy);
	}

	/**
	 * This method Clears alias view on Erase Button click when isPageDirty is
	 * not set.
	 */
	private void clearAlias() {
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedIncLibraryObjId(null);
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
		if ((searchDisplay.getIncludeView().getAliasNameTxtArea() != null)) {
			searchDisplay.getIncludeView().getAliasNameTxtArea().setText("");
		}
		if ((searchDisplay.getIncludeView().getViewCQLEditor().getText() != null)) {
			searchDisplay.getIncludeView().getViewCQLEditor().setText("");
		}
		// Below lines are to clear Library search text box.
		if ((searchDisplay.getIncludeView().getSearchTextBox().getText() != null)) {
			searchDisplay.getIncludeView().getSearchTextBox().setText("");
		}
		searchDisplay.getIncludeView().getSelectedObjectList().clear();
		searchDisplay.getIncludeView().setSelectedObject(null);
		searchDisplay.getIncludeView().setIncludedList(searchDisplay.getCqlLeftNavBarPanelView()
				.getIncludedList(searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap()));
		unCheckAvailableLibraryCheckBox();

		// Below lines are to clear search suggestion textbox and listbox
		// selection after erase.
		searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestIncludeTextBox().setText("");
		if (searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex() >= 0) {
			searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().setItemSelected(
					searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex(), false);
		}

	}

	/**
	 * Un check available library check box.
	 */
	private void unCheckAvailableLibraryCheckBox() {
		List<CQLLibraryDataSetObject> availableLibraries = new ArrayList<CQLLibraryDataSetObject>();
		availableLibraries = searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryList();
		for (int i = 0; i < availableLibraries.size(); i++) {
			availableLibraries.get(i).setSelected(false);
		}
		/*
		 * searchDisplay.getIncludeView().buildIncludeLibraryCellTable(
		 * availableLibraries,
		 * MatContext.get().getLibraryLockService().checkForEditPermission());
		 */
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		result.setCqlLibraryDataSetObjects(searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryList());
		searchDisplay.getIncludeView().buildIncludeLibraryCellTable(result,
				MatContext.get().getLibraryLockService().checkForEditPermission(), false);
	}

	/**
	 * Validate CQL artifact.
	 *
	 * @param result
	 *            the result
	 * @param currentSect
	 *            the current sect
	 * @return true, if successful
	 */
	private boolean validateCQLArtifact(SaveUpdateCQLResult result, String currentSect) {
		boolean isInvalid = false;
		if (!result.getCqlErrors().isEmpty()) {
			// final AceEditor editor =
			// getAceEditorBasedOnCurrentSection(searchDisplay, currentSection);
			for (CQLErrors error : result.getCqlErrors()) {
				int startLine = error.getStartErrorInLine();
				int startColumn = error.getStartErrorAtOffset();
				curAceEditor.addAnnotation(startLine, startColumn, error.getErrorMessage(), AceAnnotationType.ERROR);
				if (!isInvalid) {
					isInvalid = true;
				}
			}
		}

		return isInvalid;
	}

	/**
	 * Gets the ace editor based on current section.
	 *
	 * @return the ace editor based on current section
	 */
	/*
	 * private static AceEditor getAceEditorBasedOnCurrentSection(ViewDisplay
	 * searchDisplay, String currentSection) { AceEditor editor = null; switch
	 * (currentSection) { case CQLWorkSpaceConstants.CQL_DEFINE_MENU: editor =
	 * searchDisplay.getCQLDefinitionsView().getDefineAceEditor(); break; case
	 * CQLWorkSpaceConstants.CQL_FUNCTION_MENU: editor =
	 * searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor(); break;
	 * case CQLWorkSpaceConstants.CQL_PARAMETER_MENU: editor =
	 * searchDisplay.getCQLParametersView().getParameterAceEditor(); break;
	 * default: editor = searchDisplay.getDefineAceEditor(); break; } return
	 * editor; }
	 */

	/**
	 * Method to trigger double Click on List Boxes based on section when user
	 * clicks Yes on Warning message (Dirty Check).
	 */
	private void clickEventOnListboxes() {

		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
		switch (currentSection) {
		case (CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
			searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().fireEvent(new DoubleClickEvent() {
			});
			break;
		case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
			searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().fireEvent(new DoubleClickEvent() {
			});
			break;
		case (CQLWorkSpaceConstants.CQL_DEFINE_MENU):
			searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().fireEvent(new DoubleClickEvent() {
			});
			break;
		case (CQLWorkSpaceConstants.CQL_INCLUDES_MENU):
			searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().fireEvent(new DoubleClickEvent() {
			});
			break;
		default:
			break;
		}

	}

	/**
	 * Method to Unset current Left Nav section and set next selected section
	 * when user clicks yes on warning message (Dirty Check).
	 */
	private void changeSectionSelection() {
		searchDisplay.hideInformationDropDown();
		// Unset current selected section.
		switch (currentSection) {
		case (CQLWorkSpaceConstants.CQL_INCLUDES_MENU):
			unsetActiveMenuItem(currentSection);
			searchDisplay.getCqlLeftNavBarPanelView().getIncludesLibrary().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_APPLIED_QDM):
			unsetActiveMenuItem(currentSection);
			searchDisplay.getCqlLeftNavBarPanelView().getAppliedQDM().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_CODES):
			unsetActiveMenuItem(currentSection);
			searchDisplay.getCqlLeftNavBarPanelView().getCodesLibrary().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
			unsetActiveMenuItem(currentSection);
			searchDisplay.getCqlLeftNavBarPanelView().getFunctionLibrary().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
			unsetActiveMenuItem(currentSection);
			searchDisplay.getCqlLeftNavBarPanelView().getParameterLibrary().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_DEFINE_MENU):
			unsetActiveMenuItem(currentSection);
			searchDisplay.getCqlLeftNavBarPanelView().getDefinitionLibrary().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_GENERAL_MENU):
			unsetActiveMenuItem(currentSection);
			searchDisplay.getCqlLeftNavBarPanelView().getGeneralInformation().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_VIEW_MENU):
			unsetActiveMenuItem(currentSection);
			searchDisplay.getCqlLeftNavBarPanelView().getViewCQL().setActive(false);
			break;
		default:
			break;
		}
		// Set Next Selected Section.
		switch (nextSection) {
		case (CQLWorkSpaceConstants.CQL_INCLUDES_MENU):
			currentSection = nextSection;
			includesEvent();
			searchDisplay.getCqlLeftNavBarPanelView().getIncludesCollapse().getElement()
					.setClassName("panel-collapse collapse in");
			break;
		case (CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
			currentSection = nextSection;
			functionEvent();
			searchDisplay.getCqlLeftNavBarPanelView().getFunctionCollapse().getElement()
					.setClassName("panel-collapse collapse in");
			break;
		case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
			currentSection = nextSection;
			parameterEvent();
			searchDisplay.getCqlLeftNavBarPanelView().getParamCollapse().getElement()
					.setClassName("panel-collapse collapse in");
			break;
		case (CQLWorkSpaceConstants.CQL_DEFINE_MENU):
			currentSection = nextSection;
			definitionEvent();
			searchDisplay.getCqlLeftNavBarPanelView().getDefineCollapse().getElement()
					.setClassName("panel-collapse collapse in");
			break;
		case (CQLWorkSpaceConstants.CQL_GENERAL_MENU):
			currentSection = nextSection;
			generalInfoEvent();
			break;
		case (CQLWorkSpaceConstants.CQL_VIEW_MENU):
			currentSection = nextSection;
			viewCqlEvent();
			break;
		case (CQLWorkSpaceConstants.CQL_APPLIED_QDM):
			currentSection = nextSection;
			appliedQDMEvent();
			break;
		case (CQLWorkSpaceConstants.CQL_CODES):
			currentSection = nextSection;
			codesEvent();
			break;
		default:
			break;
		}
	}

	/**
	 * This method clears the view if isPageDirty flag is not set.
	 */
	private void clearViewIfDirtyNotSet() {
		switch (currentSection) {
		case (CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
			clearFunction();
			break;
		case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
			clearParameter();
			break;
		case (CQLWorkSpaceConstants.CQL_DEFINE_MENU):
			clearDefinition();
			break;
		default:
			break;
		}
	}

	/**
	 * Get Attributed for Selected Function Argument - QDM Data Type from db.
	 *
	 * @param functionArg
	 *            - CQLFunctionArgument.
	 * @return the attributes for data type
	 */
	private void getAttributesForDataType(final CQLFunctionArgument functionArg) {
		attributeService.getAllAttributesByDataType(functionArg.getQdmDataType(),
				new AsyncCallback<List<QDSAttributes>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
						System.out.println("Error retrieving data type attributes. " + caught.getMessage());

					}

					@Override
					public void onSuccess(List<QDSAttributes> result) {
						searchDisplay.getCqlLeftNavBarPanelView().setAvailableQDSAttributeList(result);
						AddFunctionArgumentDialogBox.showArgumentDialogBox(functionArg, true,
								searchDisplay.getCQLFunctionsView(), searchDisplay.getCqlLeftNavBarPanelView(),
								MatContext.get().getLibraryLockService().checkForEditPermission());

					}

				});
	}

	/**
	 * Gets the definition list.
	 *
	 * @param definitionList
	 *            the definition list
	 * @return the definition list
	 */
	private List<CQLIdentifierObject> getDefinitionList(List<CQLDefinition> definitionList) {

		List<CQLIdentifierObject> defineList = new ArrayList<CQLIdentifierObject>();

		for (int i = 0; i < definitionList.size(); i++) {
			CQLIdentifierObject definition = new CQLIdentifierObject(null, definitionList.get(i).getDefinitionName(),definitionList.get(i).getId());
			defineList.add(definition);
		}
		return defineList;
	}

	/**
	 * Gets the paramater list.
	 *
	 * @param parameterList
	 *            the parameter list
	 * @return the paramater list
	 */
	private List<CQLIdentifierObject> getParamaterList(List<CQLParameter> parameterList) {

		List<CQLIdentifierObject> paramList = new ArrayList<CQLIdentifierObject>();

		for (int i = 0; i < parameterList.size(); i++) {
			CQLIdentifierObject parameter = new CQLIdentifierObject(null, parameterList.get(i).getParameterName(),parameterList.get(i).getId());
			paramList.add(parameter);
		}
		return paramList;
	}

	/**
	 * Gets the function list.
	 *
	 * @param functionList
	 *            the function list
	 * @return the function list
	 */
	private List<CQLIdentifierObject> getFunctionList(List<CQLFunctions> functionList) {

		List<CQLIdentifierObject> funcList = new ArrayList<CQLIdentifierObject>();

		for (int i = 0; i < functionList.size(); i++) {
			CQLIdentifierObject function = new CQLIdentifierObject(null, functionList.get(i).getFunctionName(),functionList.get(i).getId());
			funcList.add(function);
		}
		return funcList;
	}

	/**
	 * Gets the includes list.
	 *
	 * @param includesList
	 *            the includes list
	 * @return the includes list
	 */
	private List<String> getIncludesList(List<CQLIncludeLibrary> includesList) {

		List<String> incLibList = new ArrayList<String>();

		for (int i = 0; i < includesList.size(); i++) {
			incLibList.add(includesList.get(i).getAliasName());
		}
		return incLibList;
	}

	/**
	 * Display empty.
	 */
	private void displayEmpty() {
		panel.clear();
		panel.add(emptyWidget);
	}

	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		panel.setStyleName("contentPanel");
		return panel;
	}

	/**
	 * returns the searchDisplay.
	 * 
	 * @return ViewDisplay.
	 */
	public ViewDisplay getSearchDisplay() {
		return searchDisplay;
	}

}
