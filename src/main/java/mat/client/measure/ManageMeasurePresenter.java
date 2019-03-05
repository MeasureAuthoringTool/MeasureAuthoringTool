package mat.client.measure;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import mat.DTO.CompositeMeasureScoreDTO;
import mat.DTO.SearchHistoryDTO;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.codelist.HasListBox;
import mat.client.codelist.events.OnChangeMeasureVersionOptionsEvent;
import mat.client.event.MeasureDeleteEvent;
import mat.client.event.MeasureEditEvent;
import mat.client.event.MeasureSelectedEvent;
import mat.client.event.MeasureVersionEvent;
import mat.client.export.ManageExportPresenter;
import mat.client.export.ManageExportView;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.measure.MeasureSearchView.Observer;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.measure.service.MeasureCloningService;
import mat.client.measure.service.MeasureCloningServiceAsync;
import mat.client.measure.service.SaveMeasureResult;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.shared.ConfirmationObserver;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.FocusableWidget;
import mat.client.shared.ManageCompositeMeasureModelValidator;
import mat.client.shared.ManageMeasureModelValidator;
import mat.client.shared.MatContext;
import mat.client.shared.MessageDelegate;
import mat.client.shared.MostRecentMeasureWidget;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SearchWidgetWithFilter;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SynchronizationDelegate;
import mat.client.shared.search.SearchResultUpdate;
import mat.client.util.ClientConstants;
import mat.client.util.MatTextBox;
import mat.shared.CompositeMeasureValidationResult;
import mat.shared.ConstantMessages;
import mat.shared.MatConstants;
import mat.shared.MeasureSearchModel;
import mat.shared.StringUtility;

public class ManageMeasurePresenter implements MatPresenter {

	private static final String MEASURE_LIBRARY = "MeasureLibrary";
	
	private List<String> bulkExportMeasureIds;

	private ClickHandler cancelClickHandler = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			if(!isLoading) {
				isClone = false;
	
				if(detailDisplay != null) {
					detailDisplay.getName().setValue("");
					detailDisplay.getShortName().setValue("");
				}
	
				if(compositeDetailDisplay != null) {
					compositeDetailDisplay.getName().setValue("");
					compositeDetailDisplay.getShortName().setValue("");
					compositeDetailDisplay.clearFields();
				}
				
				if(componentMeasureDisplay != null) {
					componentMeasureDisplay.getComponentMeasureSearch().clearFields(false);
					componentMeasureDisplay.getMessagePanel().clearAlerts();
				}
				displaySearch();
			}
		}
	};

	private ManageMeasureSearchModel.Result resultToFireEvent;

	private ManageMeasureDetailModel currentDetails;
	
	private ManageCompositeMeasureDetailModel currentCompositeMeasureDetails;

	private ManageMeasureShareModel currentShareDetails;

	final String currentUserRole = MatContext.get().getLoggedInUserRole();

	private DetailDisplay detailDisplay;
	
	private DetailDisplay compositeDetailDisplay;
	
	private ComponentMeasureDisplay componentMeasureDisplay;

	private HistoryDisplay historyDisplay;

	private boolean isClone;

	private boolean isMeasureDeleted = false;
	
	private boolean isMeasureVersioned = false;
	
	boolean isMeasureSearchFilterVisible = true;

	private static FocusableWidget subSkipContentHolder;
	
	boolean isLoading = false;

	List<ManageMeasureSearchModel.Result> listofMeasures = new ArrayList<>();

	private ManageMeasureSearchModel manageMeasureSearchModel;

	private boolean measureDeletion = false;

	private String measureDelMessage;

	private boolean measureShared = false;

	private String measureShareMessage;

	private String measureVerMessage;

	private TransferOwnerShipModel model = null;

	private ContentWithHeadingWidget panel = new ContentWithHeadingWidget();

	private SearchDisplay searchDisplay;

	private ShareDisplay shareDisplay;

	private int startIndex = 1;

	private TransferOwnershipView transferDisplay;

	private UserShareInfoAdapter userShareInfo = new UserShareInfoAdapter();

	private VersionDisplay versionDisplay;

	private ManageExportView exportView;

	public ManageMeasurePresenter(SearchDisplay sDisplayArg, DetailDisplay dDisplayArg, DetailDisplay compositeDisplayArg, ComponentMeasureDisplay componentMeasureDisplayArg, ShareDisplay shareDisplayArg,
			ManageExportView exportView, HistoryDisplay hDisplay,
			VersionDisplay vDisplay,
			final TransferOwnershipView transferDisplay) {

		searchDisplay = sDisplayArg;
		detailDisplay = dDisplayArg;
		compositeDetailDisplay = compositeDisplayArg;
		componentMeasureDisplay = componentMeasureDisplayArg;
		historyDisplay = hDisplay;
		shareDisplay = shareDisplayArg;
		this.exportView = exportView; 

		versionDisplay = vDisplay;
		this.transferDisplay = transferDisplay;
		displaySearch();
		if (searchDisplay != null) {
			searchDisplay.getMeasureSearchFilterWidget().setVisible(isMeasureSearchFilterVisible);
			searchDisplayHandlers(searchDisplay);
		}

		if (versionDisplay != null) {
			versionDisplayHandlers(versionDisplay);
		}
		if (historyDisplay != null) {
			historyDisplayHandlers(historyDisplay);
		}
		if (shareDisplay != null) {
			shareDisplayHandlers(shareDisplay);
		}
		if (transferDisplay != null) {
			transferDisplayHandlers(transferDisplay);
		}
		if (detailDisplay != null) {
			detailDisplayHandlers(detailDisplay);
		}
		if(compositeDetailDisplay != null) {
			compositeDetailDisplayHandlers(compositeDetailDisplay);
		}		
		if(componentMeasureDisplay != null) {
			componentMeasureDisplayHandlers();
		}

		// This event will be called when measure is successfully deleted and
		// then MeasureLibrary is reloaded.
		MatContext.get().getEventBus().addHandler(MeasureDeleteEvent.TYPE, new MeasureDeleteEvent.Handler() {

			@Override
			public void onDeletion(MeasureDeleteEvent event) {
				displaySearch();
				if (event.isDeleted()) {

					isMeasureDeleted = true;
					measureDeletion = true;
					isMeasureVersioned = false;
					measureDelMessage = event.getMessage();
					System.out.println("Event - is Deleted : " + isMeasureDeleted + measureDeletion);
					System.out.println("Event - message : " + measureDelMessage);
				} else {
					isMeasureDeleted = false;
					measureDeletion = true;
					isMeasureVersioned = false;
					measureDelMessage = event.getMessage();
					System.out.println("Event - is NOT Deleted : " + isMeasureDeleted + measureDeletion);
					System.out.println("Event - message : " + measureDelMessage);
				}
			}
		});

		HandlerManager eventBus = MatContext.get().getEventBus();
		eventBus.addHandler(OnChangeMeasureVersionOptionsEvent.TYPE, new OnChangeMeasureVersionOptionsEvent.Handler() {
			@Override
			public void onChangeOptions(OnChangeMeasureVersionOptionsEvent event) {
				PrimaryButton button = (PrimaryButton) versionDisplay.getSaveButton();
				button.setFocus(true);
			}
		});

	}

	@Override
	public void beforeClosingDisplay() {
		searchDisplay.resetMessageDisplay();
		isMeasureDeleted = false;
		measureShared = false;
		measureDeletion = false;
		isMeasureVersioned = false;
		isClone = false;
		isLoading = false;
		if(detailDisplay != null){
			detailDisplay.getMessageFormGrp().setValidationState(ValidationState.NONE);
			detailDisplay.getHelpBlock().setText("");
		}
		searchDisplay.getAdminSearchString().setValue("");
		if (transferDisplay != null)
			transferDisplay.getSearchString().setValue("");
		searchDisplay.getMeasureSearchFilterWidget().setVisible(false);
		searchDisplay.getMostRecentMeasureVerticalPanel().setVisible(false);
	}

	@Override
	public void beforeDisplay() {
		Command waitForUnlock = new Command() {
			@Override
			public void execute() {
				if (!MatContext.get().getMeasureLockService().isResettingLock()) {
					displaySearch();
				} else {
					Scheduler.get().scheduleDeferred(this);
				}
			}
		};
		if (MatContext.get().getMeasureLockService().isResettingLock()) {
			waitForUnlock.execute();
		} else {
			displaySearch();
			searchDisplay.getMeasureSearchFilterWidget().getAdvancedSearchPanel().getCollapsePanel().setIn(false);
		}

		Mat.focusSkipLists(MEASURE_LIBRARY);
	}



	private void bulkExport(List<String> selectedMeasureIds) {
		String measureId = "";

		MatContext.get().getAuditService().recordMeasureEvent(selectedMeasureIds, "Measure Package Exported", null,
				true, new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
						detailDisplay.getErrorMessageDisplay().createAlert("Error while adding bulk export log entry.");
					}

					@Override
					public void onSuccess(Void result) {
					}
				});

		for (String id : selectedMeasureIds) {
			measureId += id + "&id=";
		}
		measureId = measureId.substring(0, measureId.lastIndexOf("&"));
		String url = GWT.getModuleBaseURL() + "bulkExport?id=" + measureId;
		url += "&type=open";
		manageMeasureSearchModel.getSelectedExportIds().clear();
		FormPanel form = searchDisplay.getForm();
		form.setAction(url);
		form.setEncoding(FormPanel.ENCODING_URLENCODED);
		form.setMethod(FormPanel.METHOD_POST);
		form.submit();
	}

	private void clearRadioButtonSelection() {
		versionDisplay.getMajorRadioButton().setValue(false);
		versionDisplay.getMinorRadioButton().setValue(false);
	}

	private void cloneMeasure(final ManageMeasureDetailModel currentDetails, final boolean isDraftCreation) {
		String loggedinUserId = MatContext.get().getLoggedinUserId();
		searchDisplay.resetMessageDisplay();
		MeasureCloningServiceAsync mcs = (MeasureCloningServiceAsync) GWT.create(MeasureCloningService.class);
		
		mcs.clone(currentDetails, loggedinUserId, isDraftCreation,
				new AsyncCallback<ManageMeasureSearchModel.Result>() {
					@Override
					public void onFailure(Throwable caught) {

						setSearchingBusy(false);
						shareDisplay.getErrorMessageDisplay()
								.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						MatContext.get().recordTransactionEvent(null, null, null,
								"Unhandled Exception: " + caught.getLocalizedMessage(), 0);
					}

					@Override
					public void onSuccess(ManageMeasureSearchModel.Result result) {
						resultToFireEvent = result;
						if(isDraftCreation){
						searchDisplay.getDraftConfirmationDialogBox().show(MatContext.get().getMessageDelegate().getMeasureDraftSuccessfulMessage(result.getName()));
						searchDisplay.getDraftConfirmationDialogBox().getYesButton().setTitle("Continue");
						searchDisplay.getDraftConfirmationDialogBox().getYesButton().setText("Continue");
						searchDisplay.getDraftConfirmationDialogBox().getYesButton().setFocus(true);
						}
						else if(isClone){
							fireMeasureSelected(result);
						}
					
					}
				});
	}

	public void fireMeasureSelected(ManageMeasureSearchModel.Result result){
		fireMeasureSelectedEvent(result.getId(), result.getVersion(), result.getName(),
				result.getShortName(), result.getScoringType(), result.isEditable(),
				result.isMeasureLocked(), result.getLockedUserId(result.getLockedUserInfo()), result.isDraft());
		setSearchingBusy(false);
		isClone = false;
	}
	
	private void auditMeasureSelected(ManageMeasureSearchModel.Result result){
		MatContext.get().getAuditService().recordMeasureEvent(result.getId(), "Draft Created",
				"Draft created based on Version " + result.getVersionValue(), false,
				new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable caught) {}
					@Override
					public void onSuccess(Boolean result) {}
				});
	}

	private void createDraftOfSelectedVersion(ManageMeasureDetailModel currentDetails) {
		cloneMeasure(currentDetails, true);
	}

	private void createNewMeasure() {
		clearErrorMessageAlerts();
		currentDetails = new ManageMeasureDetailModel();
		displayDetailForAdd();
	}
	
	protected void createNewCompositeMeasure() {
		clearErrorMessageAlerts();
		currentCompositeMeasureDetails = new ManageCompositeMeasureDetailModel();
		displayDetailForAddComposite();
	}
	
	private void clearErrorMessageAlerts() {
		detailDisplay.getErrorMessageDisplay().clearAlert();
		searchDisplay.getErrorMessageDisplayForBulkExport().clearAlert();
		Mat.focusSkipLists(MEASURE_LIBRARY);
	}

	private void createVersion() {
		versionDisplay.getErrorMessageDisplay().clearAlert();
		searchDisplay.resetMessageDisplay();
		panel.getButtonPanel().clear();
		panel.setHeading("My Measures > Create Measure Version of Draft", MEASURE_LIBRARY);
		panel.setContent(versionDisplay.asWidget());
		Mat.focusSkipLists(MEASURE_LIBRARY);
		clearRadioButtonSelection();
	}
	
	private void componentMeasureDisplayHandlers() {
		
		componentMeasureDisplay.getCancelButton().addClickHandler(cancelClickHandler);
		componentMeasureDisplay.getBackButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				displayDetailForAddComposite();
			}
		});
		componentMeasureDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				componentMeasureDisplay.setComponentBusy(true);
				updateCompositeDetailsFromComponentMeasureDisplay();
				
				MatContext.get().getMeasureService().validateCompositeMeasure(currentCompositeMeasureDetails, new AsyncCallback<CompositeMeasureValidationResult>() {

					@Override
					public void onFailure(Throwable caught) {
						componentMeasureDisplay.setComponentBusy(false);
					}

					@Override
					public void onSuccess(CompositeMeasureValidationResult result) {
						currentCompositeMeasureDetails = result.getModel();
						if(isValidCompositeMeasureForSave(result.getMessages())){
							saveCompositeMeasure();
						}
					}
				});
			}
		});
	}

	private void compositeDetailDisplayHandlers(final DetailDisplay compositeDetailDisplay) {
		compositeDetailDisplay.getCancelButton().addClickHandler(cancelClickHandler);
		
		MatContext.get().getListBoxCodeProvider().getScoringList(new AsyncCallback<List<? extends HasListBox>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MessageDelegate.s_ERR_RETRIEVE_SCORING_CHOICES);
			}

			@Override
			public void onSuccess(List<? extends HasListBox> result) {
				compositeDetailDisplay.setScoringChoices(result);
				MatContext.get().createSelectionMap(result);

				List<CompositeMeasureScoreDTO> compositeChoices = MatContext.get().buildCompositeScoringChoiceList();
				((ManageCompositeMeasureDetailView) compositeDetailDisplay).setCompositeScoringChoices(compositeChoices);
			}
		});

		((ManageCompositeMeasureDetailView) compositeDetailDisplay).getCompositeScoringMethodInput().addChangeHandler(event -> createSelectionMapAndSetScorings());
		
		compositeDetailDisplay.getMeasScoringChoice().addChangeHandler(event -> setPatientBasedIndicatorBasedOnScoringChoice(compositeDetailDisplay));
		
		compositeDetailDisplay.getSaveButton().addClickHandler(event -> updateCompositeDetailsOnContinueButton());

	}

	private void createSelectionMapAndSetScorings() {
		String compositeScoringValue = ((ManageCompositeMeasureDetailView)compositeDetailDisplay).getCompositeScoringValue();
		compositeDetailDisplay.setScoringChoices(MatContext.get().getSelectionMap().get(compositeScoringValue));
		setPatientBasedIndicatorBasedOnScoringChoice(compositeDetailDisplay);
	}
	
	
	private void updateCompositeDetailsOnContinueButton() { 

		boolean isEdit = false;
		if(componentMeasureDisplay != null) {
			if(currentCompositeMeasureDetails.getId()!=null) {
				isEdit = true;
			}
			componentMeasureDisplay.getComponentMeasureSearch().clearFields(isEdit);
			componentMeasureDisplay.getMessagePanel().clearAlerts();
		}

		String panelHeading = "";
		if(StringUtility.isEmptyOrNull(currentCompositeMeasureDetails.getId())) {
			panelHeading = "My Measures > Create New Composite Measure > Component Measures";					
		} else {
			panelHeading = "My Measures > Edit Composite Measure > Update Component Measures.";
		}
		
		updateCompositeDetailsFromCompositeDetailView();
		
		if(!isValidCompositeMeasure(currentCompositeMeasureDetails)){
			return;
		}
		
		displayComponentDetails(panelHeading);
	
	}
	
	private void detailDisplayHandlers(final DetailDisplay detailDisplay) {
		
		detailDisplay.getConfirmationDialogBox().getYesButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				update();
			}
		});
		
		detailDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				updateDetailsFromView();
				if(!isValid(currentDetails, isClone)){
					return;
				}
				
				// create new measure flow
				if(!isClone && currentDetails.getId() == null) {
					showConfirmationDialog(MatContext.get().getMessageDelegate().getCreateNewMeasureSuccessfulMessage(detailDisplay.getName().getValue()));
					
				} else if(isClone) { // if clone
					showConfirmationDialog(MatContext.get().getMessageDelegate().getCloneMeasureSuccessfulMessage(detailDisplay.getName().getValue()));
					
				} else { //if edit
					showConfirmationDialog(MatContext.get().getMessageDelegate().getEditMeasureSuccessfulMessage(detailDisplay.getName().getValue()));	
				}
			
			}

			private void showConfirmationDialog(final String message) {
				detailDisplay.getConfirmationDialogBox().show(message);
				detailDisplay.getConfirmationDialogBox().getYesButton().setTitle("Continue");
				detailDisplay.getConfirmationDialogBox().getYesButton().setText("Continue");
				detailDisplay.getConfirmationDialogBox().getYesButton().setFocus(true);
			}
		});

		detailDisplay.getCancelButton().addClickHandler(cancelClickHandler);
		
		MatContext.get().getListBoxCodeProvider().getScoringList(new AsyncCallback<List<? extends HasListBox>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MessageDelegate.s_ERR_RETRIEVE_SCORING_CHOICES);
			}

			@Override
			public void onSuccess(List<? extends HasListBox> result) {
				detailDisplay.setScoringChoices(result);
			}
		});
		
		detailDisplay.getMeasScoringChoice().addChangeHandler(event -> setPatientBasedIndicatorBasedOnScoringChoice((detailDisplay)));
	}

	private void setPatientBasedIndicatorBasedOnScoringChoice(DetailDisplay detailDisplay) {

		if (MatConstants.CONTINUOUS_VARIABLE.equalsIgnoreCase(detailDisplay.getMeasScoringChoice().getItemText(detailDisplay.getMeasScoringChoice().getSelectedIndex()))) {
			if(detailDisplay.getPatientBasedInput().getItemCount() > 1) {
				// yes is the second element in the list, so the 1 index. 
				detailDisplay.getPatientBasedInput().removeItem(1);
			}
			detailDisplay.getPatientBasedInput().setSelectedIndex(0);
			detailDisplay.getHelpBlock().setText("Patient based indicator set to no.");
			
		} else {
			resetPatientBasedInput(detailDisplay); 
			detailDisplay.getHelpBlock().setText("Patient based indicator set to yes.");
		}

		detailDisplay.getMessageFormGrp().setValidationState(ValidationState.SUCCESS);
		detailDisplay.getHelpBlock().setColor("transparent");
	}

	private void displayCommonDetailForAdd(DetailDisplay detailDisplay) {
		panel.getButtonPanel().clear();
		resetPatientBasedInput(detailDisplay);
		
		detailDisplay.showMeasureName(false);
		detailDisplay.showCautionMsg(false);
		panel.setContent(detailDisplay.asWidget());
	}
	
	private void displayDetailForAdd() {
		displayCommonDetailForAdd(detailDisplay);
		panel.setHeading("My Measures > Create New Measure", MEASURE_LIBRARY);
		setDetailsToView();
	}
	
	private void displayDetailForAddComposite() {
		displayCommonDetailForAdd(compositeDetailDisplay);	
		panel.setHeading("My Measures > Create New Composite Measure", "CompositeMeasure");	
		setCompositeDetailsToView();
		Mat.focusSkipLists("CompositeMeasure");
	}
	
	private void displayComponentDetails(String panelHeading) {
		panel.getButtonPanel().clear();
		panel.setHeading(panelHeading, "ComponentMeasure");
		panel.setContent(componentMeasureDisplay.asWidget());
		Mat.focusSkipLists("ComponentMeasure");
	}

	private void displayDetailForClone() {
		detailDisplay.clearFields();
		resetPatientBasedInput(detailDisplay); 
		
		detailDisplay.setMeasureName(currentDetails.getName());
		detailDisplay.showMeasureName(true);
		detailDisplay.showCautionMsg(true);
		detailDisplay.getMeasScoringChoice().setValueMetadata(currentDetails.getMeasScoring());
		
		// set the patient based indicators, yes is index 1, no is index 0
		if(currentDetails.isPatientBased()) {
			detailDisplay.getPatientBasedInput().setSelectedIndex(1);
		} else {
			detailDisplay.getPatientBasedInput().setSelectedIndex(0);
		}
		
		if(currentDetails.getMeasScoring().equalsIgnoreCase(MatConstants.CONTINUOUS_VARIABLE)) {
			detailDisplay.getPatientBasedInput().removeItem(1);
			detailDisplay.getPatientBasedInput().setSelectedIndex(0);
		}
		
		
		panel.getButtonPanel().clear();
		panel.setHeading("My Measures > Clone Measure", MEASURE_LIBRARY);
		panel.setContent(detailDisplay.asWidget());
		Mat.focusSkipLists(MEASURE_LIBRARY);
	}


	private void resetPatientBasedInput(DetailDisplay currentDisplay) {
		currentDisplay.getPatientBasedInput().clear();
		currentDisplay.getPatientBasedInput().addItem("No", "No");
		currentDisplay.getPatientBasedInput().addItem("Yes", "Yes");
		// default the selected index to be 1, which is yes.  				
		currentDisplay.getPatientBasedInput().setSelectedIndex(1);
	}

	private void displayHistory(String measureId, String measureName) {
		int startIndex = 0;
		int pageSize = Integer.MAX_VALUE;
		String heading = "My Measures > History";
		if (ClientConstants.ADMINISTRATOR.equalsIgnoreCase(MatContext.get().getLoggedInUserRole())) {
			heading = "Measures > History";
		}
		panel.getButtonPanel().clear();
		panel.setHeading(heading, MEASURE_LIBRARY);
		searchHistory(measureId, startIndex, pageSize);
		historyDisplay.setMeasureId(measureId);
		historyDisplay.setMeasureName(measureName);
		panel.setContent(historyDisplay.asWidget());
		Mat.focusSkipLists(MEASURE_LIBRARY);
	}

	public void displaySearch() {
		searchDisplay.getCellTablePanel().clear();
		String heading = "Measure Library";
		int filter;
		panel.setHeading(heading, MEASURE_LIBRARY);
		setSubSkipEmbeddedLink("measureserachView_mainPanel");
		FlowPanel fp = new FlowPanel();
		fp.getElement().setId("fp_FlowPanel");
		if (ClientConstants.ADMINISTRATOR.equalsIgnoreCase(MatContext.get().getLoggedInUserRole())) {
			heading = "";
			filter = SearchWidgetWithFilter.ALL;
			search(searchDisplay.getAdminSearchString().getValue(), 1, Integer.MAX_VALUE, filter, false);
			fp.add(searchDisplay.asWidget());
		} else {
			searchDisplay.getMeasureSearchFilterWidget().setVisible(true);
			isMeasureSearchFilterVisible = true;
			filter = searchDisplay.getSelectedFilter();
			search(searchDisplay.getSearchString().getValue(), 1, Integer.MAX_VALUE, filter, false);
			searchRecentMeasures();
			buildCreateMeasure(); 
			buildCreateCompositeMeasure();
			fp.add(searchDisplay.asWidget());
		}

		panel.setContent(fp);
		Mat.focusSkipLists(MEASURE_LIBRARY);
	}

	private void buildCreateMeasure() {
		panel.getButtonPanel().clear();
		searchDisplay.getCreateMeasureButton().setId("newMeasure_button");
		searchDisplay.getCreateMeasureButton().setIcon(IconType.LIGHTBULB_O);
		searchDisplay.getCreateMeasureButton().setIconSize(IconSize.LARGE);
		searchDisplay.getCreateMeasureButton().setType(ButtonType.LINK);
		searchDisplay.getCreateMeasureButton().setTitle("Click to create new measure");
		searchDisplay.getCreateMeasureButton().setStyleName("createNewButton");
		panel.getButtonPanel().add(searchDisplay.getCreateMeasureButton());
	}
	
	private void buildCreateCompositeMeasure() {
		searchDisplay.getCreateCompositeMeasureButton().setId("newCompositeMeasure_button");
		searchDisplay.getCreateCompositeMeasureButton().setIcon(IconType.SITEMAP);
		searchDisplay.getCreateCompositeMeasureButton().setIconSize(IconSize.LARGE);
		searchDisplay.getCreateCompositeMeasureButton().setType(ButtonType.LINK);
		searchDisplay.getCreateCompositeMeasureButton().setTitle("Click to create new composite measure");
		searchDisplay.getCreateCompositeMeasureButton().setStyleName("createNewCompositeButton");
		panel.getButtonPanel().add(searchDisplay.getCreateCompositeMeasureButton());
	}
	

	private void displayShare(String userName, String id, String name) {
		//Setting this value so that visiting this page every time from share link, any previously entered value is reset
		shareDisplay.getSearchWidgetBootStrap().getSearchBox().setValue("");
		shareDisplay.setMeasureName(name);
		displayShare(userName, id);
	}

	private void displayShare(String userName, String id) {
		getShareDetails(userName, id, 1);
		panel.getButtonPanel().clear();
		panel.setHeading("My Measures > Measure Sharing", MEASURE_LIBRARY);
		panel.setContent(shareDisplay.asWidget());
		Mat.focusSkipLists(MEASURE_LIBRARY);
	}

	private void displayTransferView(String searchString, int startIndex, int pageSize) {
		final ArrayList<ManageMeasureSearchModel.Result> transferMeasureResults = (ArrayList<Result>) manageMeasureSearchModel
				.getSelectedTransferResults();
		pageSize = Integer.MAX_VALUE;
		searchDisplay.getSuccessMessageDisplay().clearAlert();
		searchDisplay.getErrorMessageDisplay().clearAlert();
		searchDisplay.getErrorMessagesForTransferOS().clearAlert();
		transferDisplay.getErrorMessageDisplay().clearAlert();
		if (!transferMeasureResults.isEmpty()) {
			setSearchingBusy(true);
			MatContext.get().getMeasureService().searchUsers(searchString, startIndex, pageSize,
					new AsyncCallback<TransferOwnerShipModel>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
							setSearchingBusy(false);
						}

						@Override
						public void onSuccess(TransferOwnerShipModel result) {
							SearchResultUpdate sru = new SearchResultUpdate();
							sru.update(result, (MatTextBox) transferDisplay.getSearchString(), searchString);

							transferDisplay.buildHTMLForMeasures(transferMeasureResults);
							transferDisplay.buildCellTable(result);
							panel.setHeading("Measure Library Ownership >  Measure Ownership Transfer", MEASURE_LIBRARY);
							panel.setContent(transferDisplay.asWidget());
							setSearchingBusy(false);
							model = result;
						}
					});
		} else {
			searchDisplay.getErrorMessagesForTransferOS()
					.createAlert(MatContext.get().getMessageDelegate().getTransferCheckBoxErrorMeasure());
		}

	}

	private void editClone(String id) {
		detailDisplay.getErrorMessageDisplay().clearAlert();
		searchDisplay.getErrorMessageDisplayForBulkExport().clearAlert();
		MatContext.get().getMeasureService().getMeasure(id, new AsyncCallback<ManageMeasureDetailModel>() {

			@Override
			public void onFailure(Throwable caught) {
				detailDisplay.getErrorMessageDisplay()
						.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null,
						"Unhandled Exception: " + caught.getLocalizedMessage(), 0);
			}

			@Override
			public void onSuccess(ManageMeasureDetailModel result) {
				currentDetails = result;
				displayDetailForClone();
				Mat.hideLoadingMessage();
			}
		});
	}

	private void export(ManageMeasureSearchModel.Result result) {
		String id = result.getId();
		MatContext.get().getAuditService().recordMeasureEvent(id, "Measure Exported", null, true,
				new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable caught) {
						detailDisplay.getErrorMessageDisplay().createAlert("Error while adding export comment");
					}

					@Override
					public void onSuccess(Boolean result) {
					}

				});

		ManageExportPresenter exportPresenter = new ManageExportPresenter(exportView, result, this);

		searchDisplay.getErrorMessageDisplayForBulkExport().clearAlert();
		panel.getButtonPanel().clear();
		panel.setContent(exportPresenter.getWidget());
		Mat.focusSkipLists(MEASURE_LIBRARY);
	}

	private void fireMeasureEditEvent() {
		MeasureEditEvent evt = new MeasureEditEvent();
		MatContext.get().getEventBus().fireEvent(evt);
	} 

	private void fireMeasureSelectedEvent(String id, String version, String name, String shortName, String scoringType,
			boolean isEditable, boolean isLocked, String lockedUserId, boolean isDraft) {
		MeasureSelectedEvent evt = new MeasureSelectedEvent(id, version, name, shortName, scoringType, isEditable,
				isLocked, lockedUserId, isDraft);
		searchDisplay.resetMessageDisplay();
		MatContext.get().getEventBus().fireEvent(evt);
	}

	public List<String> getBulkExportMeasureIds() {
		return bulkExportMeasureIds;
	}

	private void getShareDetails(String userName, String id, int startIndex) {
		searchDisplay.resetMessageDisplay();
		shareDisplay.resetMessageDisplay();
		MatContext.get().getMeasureService().getUsersForShare(userName, id, startIndex, Integer.MAX_VALUE,
				new AsyncCallback<ManageMeasureShareModel>() {
					@Override
					public void onFailure(Throwable caught) {
						shareDisplay.getErrorMessageDisplay()
								.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						MatContext.get().recordTransactionEvent(null, null, null,
								"Unhandled Exception: " + caught.getLocalizedMessage(), 0);
					}

					@Override
					public void onSuccess(ManageMeasureShareModel result) {
						currentShareDetails = result;
						shareDisplay.setPrivate(currentShareDetails.isPrivate());
						userShareInfo.setData(result);
						shareDisplay.buildDataTable(userShareInfo);
						if (result.getData() == null || result.getData().isEmpty()) {
							shareDisplay.getErrorMessageDisplay().createAlert(MessageDelegate.getNoUsersReturned());
						} 
					}
				});
	}

	@Override
	public Widget getWidget() {
		return panel;
	}

	public Widget getWidgetWithHeading(Widget widget, String heading) {
		FlowPanel vPanel = new FlowPanel();
		Label h = new Label(heading);
		h.addStyleName("myAccountHeader");
		h.addStyleName("leftAligned");
		vPanel.add(h);
		vPanel.add(widget);
		vPanel.addStyleName("myAccountPanel");
		widget.addStyleName("myAccountPanelContent");
		return vPanel;
	}

	private void historyDisplayHandlers(final HistoryDisplay historyDisplay) {

		historyDisplay.getReturnToLink().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				displaySearch();

			}
		});

	}

	public boolean isValid(ManageMeasureDetailModel model, boolean isClone) {
		ManageMeasureModelValidator manageMeasureModelValidator = new ManageMeasureModelValidator();
		List<String> message = manageMeasureModelValidator.validateMeasureWithClone(model, isClone);
		boolean valid = message.size() == 0;
		if (!valid) {
			String errorMessage = "";
			if(message.size() > 0) {
				errorMessage = message.get(0);
			}
			detailDisplay.getErrorMessageDisplay().createAlert(errorMessage);
			Mat.hideLoadingMessage();
		} else {
			detailDisplay.getErrorMessageDisplay().clearAlert();
			searchDisplay.getErrorMessageDisplayForBulkExport().clearAlert();
		}
		return valid;
	}
	
	private boolean isValidCompositeMeasure(ManageCompositeMeasureDetailModel compositeMeasureDetails) {
		ManageCompositeMeasureModelValidator manageCompositeMeasureModelValidator = new ManageCompositeMeasureModelValidator();
		List<String> message = manageCompositeMeasureModelValidator.validateMeasureWithClone(compositeMeasureDetails, isClone);
		boolean valid = message.size() == 0;
		if(!valid) {
			String errorMessage = "";
			if(message.size() > 0) {
				errorMessage = message.get(0);
			}
			compositeDetailDisplay.getErrorMessageDisplay().createAlert(errorMessage);
		} else {
			compositeDetailDisplay.getErrorMessageDisplay().clearAlert();
		}
		return valid;
	}
	
	private boolean isValidCompositeMeasureForSave(List<String> message) {
		boolean valid = message.size() == 0;
		componentMeasureDisplay.getSuccessMessage().clearAlert();
		if(!valid) {
			String errorMessage = "";
			if(message.size() > 0) {
				errorMessage = message.get(0);
				componentMeasureDisplay.setComponentBusy(false);
			}
			componentMeasureDisplay.getErrorMessageDisplay().createAlert(errorMessage);
		} else {
			componentMeasureDisplay.getErrorMessageDisplay().clearAlert();
		}
		return valid;
	}

	
	private void getUnusedLibraryDialog(String measureId, String measureName, boolean isMajor, String version, boolean shouldPackage) {
		ConfirmationDialogBox confirmationDialogBox = new ConfirmationDialogBox(
				MatContext.get().getMessageDelegate().getUnusedIncludedLibraryWarning(measureName), "Continue",
				"Cancel", null, false);
		confirmationDialogBox.setObserver(new ConfirmationObserver() {

			@Override
			public void onYesButtonClicked() {
				saveFinalizedVersion(measureId, measureName, isMajor, version, shouldPackage, true);
			}

			@Override
			public void onNoButtonClicked() {
				displaySearch(); 
			}
			
			@Override
			public void onClose() {
				displaySearch();
			}
		});

		confirmationDialogBox.show();
	} 
	
	private ConfirmationDialogBox getVersionWithoutPackageDialog(String measureId, String measureName, boolean isMajor, String version, boolean shouldPackage) {
		ConfirmationDialogBox dialogBox = new ConfirmationDialogBox(MatContext.get().getMessageDelegate().getVersionAndPackageUnsuccessfulMessage(), "Continue", "Cancel", null);
		dialogBox.setObserver(new ConfirmationObserver() {
			
			@Override
			public void onYesButtonClicked() {
				saveFinalizedVersion(measureId, measureName, isMajor, version, shouldPackage, true);
			}
			
			@Override
			public void onNoButtonClicked() {
			}
			
			@Override
			public void onClose() {
			}
		});
			
		return dialogBox;
	}
	
	private void recordMeasureAuditEvent(String measureId, String versionString) {
		MatContext.get().getAuditService().recordMeasureEvent(measureId, "Measure Versioned",
				"Measure Version " + versionString + " created", false, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(Boolean result) {

					}
				});
	}

	private void saveFinalizedVersion(final String measureId, final String measureName, final boolean isMajor, final String version, boolean shouldPackage, boolean ignoreUnusedLibraries) {
		setSearchingBusy(true);
		MatContext.get().getMeasureService().saveFinalizedVersion(measureId, isMajor, version, shouldPackage, ignoreUnusedLibraries, new AsyncCallback<SaveMeasureResult>() {
					@Override
					public void onFailure(Throwable caught) {
						setSearchingBusy(false);
						versionDisplay.getErrorMessageDisplay()
								.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						MatContext.get().recordTransactionEvent(null, null, null,
								"Unhandled Exception: " + caught.getLocalizedMessage(), 0);
					}

					@Override
					public void onSuccess(SaveMeasureResult result) {
						setSearchingBusy(false);
						if (result.isSuccess()) {
							displaySearch();
							String versionStr = result.getVersionStr();
							recordMeasureAuditEvent(measureId, versionStr);
							isMeasureVersioned = true;
							
							if(shouldPackage) {
								fireSuccessfullVersionAndPackageEvent(isMeasureVersioned, measureName, MatContext.get().getMessageDelegate().getVersionAndPackageSuccessfulMessage(measureName, versionStr));
							} else  {
								fireSuccessfullVersionEvent(isMeasureVersioned, measureName, MatContext.get().getMessageDelegate().getVersionSuccessfulMessage(measureName, versionStr));
							}
							
						} else {
							isMeasureVersioned = false;
							if (result.getFailureReason() == ConstantMessages.INVALID_CQL_DATA) {
								versionDisplay.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getNoVersionCreated());
							} else if (result.getFailureReason() == SaveMeasureResult.UNUSED_LIBRARY_FAIL) {
								getUnusedLibraryDialog(measureId, measureName, isMajor, version, shouldPackage);
							} else if(result.getFailureReason() == SaveMeasureResult.PACKAGE_FAIL) {
								getVersionWithoutPackageDialog(measureId, measureName, isMajor, version, false).show();
							}
						}
					}
				});
	}

	private void fireSuccessfullVersionEvent(boolean isSuccess, String name, String message){
		MeasureVersionEvent versionEvent = new MeasureVersionEvent(isSuccess, name, message);
		MatContext.get().getEventBus().fireEvent(versionEvent);
	}
	
	private void fireSuccessfullVersionAndPackageEvent(boolean isSuccess, String name, String message) {
		fireSuccessfullVersionEvent(isSuccess, name, message);
	}

	private void search(final String searchText, int startIndex, int pageSize, final int filter, boolean didUserSelectSearch) {
		final String lastSearchText = (searchText != null) ? searchText.trim() : null;

		// This to fetch all Measures if user role is Admin. This will go away
		// when Pagination will be implemented in Measure Library.
		if (currentUserRole.equalsIgnoreCase(ClientConstants.ADMINISTRATOR)) {
			pageSize = 25;
			setSearchingBusy(true);
			MeasureSearchModel searchAdminModel = new MeasureSearchModel(filter, startIndex, pageSize, lastSearchText);
			
			if(null != searchDisplay) {
				searchDisplay.getSuccessMessageDisplay().clearAlert();	
			}
			
			MatContext.get().getMeasureService().search(searchAdminModel,
					new AsyncCallback<ManageMeasureSearchModel>() {
						@Override
						public void onFailure(Throwable caught) {
							detailDisplay.getErrorMessageDisplay()
									.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
							MatContext.get().recordTransactionEvent(null, null, null,
									"Unhandled Exception: " + caught.getLocalizedMessage(), 0);
							showAdminSearchingBusy(false);
						}

						@Override
						public void onSuccess(ManageMeasureSearchModel result) {
							result.setSelectedTransferIds(new ArrayList<String>());
							result.setSelectedTransferResults(new ArrayList<Result>());
							manageMeasureSearchModel = result;
							MeasureSearchView measureSearchView = new MeasureSearchView();
							measureSearchView.setData(result);

							MatContext.get().setManageMeasureSearchModel(manageMeasureSearchModel);
							searchDisplay.setAdminObserver(new MeasureSearchView.AdminObserver() {

								@Override
								public void onTransferSelectedClicked(Result result) {
									searchDisplay.getErrorMessageDisplay().clearAlert();
									searchDisplay.getErrorMessagesForTransferOS().clearAlert();
									updateTransferIDs(result, manageMeasureSearchModel);
								}

								@Override
								public void onHistoryClicked(Result result) {
									historyDisplay.setReturnToLinkText("<< Return to MeasureLibrary Owner Ship");

									displayHistory(result.getId(), result.getName());

								}
							});
							if ((result.getResultsTotal() == 0) && !lastSearchText.isEmpty()) {
								searchDisplay.getErrorMessageDisplay()
										.createAlert(MatContext.get().getMessageDelegate().getNoMeasuresMessage());
							} else {
								searchDisplay.getErrorMessageDisplay().clearAlert();
								searchDisplay.getErrorMessagesForTransferOS().clearAlert();
							}
							SearchResultUpdate sru = new SearchResultUpdate();
							sru.update(result, (TextBox) searchDisplay.getAdminSearchString(), lastSearchText);
							searchDisplay.buildDataTable(manageMeasureSearchModel, filter, searchText);
							panel.setContent(searchDisplay.asWidget());
							showAdminSearchingBusy(false);

						}
					});
		} else {
			MeasureSearchModel searchModel = new MeasureSearchModel(filter, startIndex, 25, lastSearchText);
			buildAdvancedSearchModel(searchModel);
			searchDisplay.getMeasureSearchFilterWidget().getAdvancedSearchPanel().getCollapsePanel().setIn(false);
			advancedSearch(searchModel, didUserSelectSearch);
		}
	}

	private void buildAdvancedSearchModel(MeasureSearchModel searchModel) {
		searchModel.setIsDraft(searchDisplay.getMeasureSearchFilterWidget().getAdvancedSearchPanel().getSearchStateValue());
		searchModel.setPatientBased(searchDisplay.getMeasureSearchFilterWidget().getAdvancedSearchPanel().getPatientBasedValue());
		searchModel.setScoringTypes(searchDisplay.getMeasureSearchFilterWidget().getAdvancedSearchPanel().getScoringTypeList());
		searchModel.setModifiedDate(Integer.parseInt(searchDisplay.getMeasureSearchFilterWidget().getAdvancedSearchPanel().getModifiedWithinValue()));
		searchModel.setModifiedOwner(searchDisplay.getMeasureSearchFilterWidget().getAdvancedSearchPanel().getModifiedByValue());
		searchModel.setOwner(searchDisplay.getMeasureSearchFilterWidget().getAdvancedSearchPanel().getOwnedByValue());
	}
	
	private void advancedSearch(MeasureSearchModel measureSearchModel, boolean didUserSelectSearch) {
		setSearchingBusy(true);
		MatContext.get().getMeasureService().search(measureSearchModel,
				new AsyncCallback<ManageMeasureSearchModel>() {
					@Override
					public void onFailure(Throwable caught) {
						detailDisplay.getErrorMessageDisplay()
								.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						MatContext.get().recordTransactionEvent(null, null, null,
								"Unhandled Exception: " + caught.getLocalizedMessage(), 0);
						setSearchingBusy(false);
					}

					@Override
					public void onSuccess(ManageMeasureSearchModel result) {
						measureSearchModel.setTotalResults(result.getResultsTotal());
						addSearchPills(measureSearchModel);
						String measureListLabel = (measureSearchModel.getIsMyMeasureSearch() != 0) ? "All Measures" : "My Measures";
						searchDisplay.getMeasureSearchView().setMeasureListLabel(measureListLabel);
						
						boolean isExportSelectedButtonVisible = (result.getData().size() > 0);
						searchDisplay.getExportSelectedButton().setVisible(isExportSelectedButtonVisible);
						searchDisplay.getMeasureSearchView().setObserver(createMeasureTableObserver());
						result.setSelectedExportIds(new ArrayList<String>());
						result.setSelectedExportResults(new ArrayList<Result>());
						manageMeasureSearchModel = result;
						MatContext.get().setManageMeasureSearchModel(manageMeasureSearchModel);

						if (result.getResultsTotal() == 0) {
							searchDisplay.getErrorMessageDisplay()
									.createAlert(MatContext.get().getMessageDelegate().getNoMeasuresMessage());
						} else {
							searchDisplay.getErrorMessageDisplay().clearAlert();
							searchDisplay.getErrorMessageDisplayForBulkExport().clearAlert();
							if (measureDeletion) {
								if (isMeasureDeleted) {
									searchDisplay.getSuccessMeasureDeletion().createAlert(measureDelMessage);
								} else {
									if (measureDelMessage != null) {
										searchDisplay.getErrorMeasureDeletion().createAlert(measureDelMessage);
									}
								}
							} else if (measureShared) {
								searchDisplay.getSuccessMessageDisplay().createAlert(measureShareMessage);
								measureShared = false;
							} else {
								searchDisplay.resetMessageDisplay();
							}if(isMeasureVersioned){
								searchDisplay.getSuccessMeasureDeletion().createAlert(measureVerMessage);
							} 
						}

						searchDisplay.buildCellTable(manageMeasureSearchModel, measureSearchModel.getIsMyMeasureSearch(), measureSearchModel);

						setSearchingBusy(false);
						if(didUserSelectSearch) {
							searchDisplay.getSearchPillPanel().getBadgeHeader().getElement().focus();
						}
					}
				});
	}
	private Observer createMeasureTableObserver() {
		return new MeasureSearchView.Observer() {
			@Override
			public void onCloneClicked(ManageMeasureSearchModel.Result result) {
				if(result.isClonable()) {
					resetMeasureFlags();
					isClone = true;
					editClone(result.getId());
				}
			}


			@Override
			public void onExportClicked(ManageMeasureSearchModel.Result result) {
				resetMeasureFlags();
				export(result);
			}

			@Override
			public void onExportSelectedClicked(Result result, boolean isCBChecked) {
				resetMeasureFlags();
				searchDisplay.getErrorMessageDisplayForBulkExport().clearAlert();
				updateExportedIDs(result, manageMeasureSearchModel, isCBChecked);

			}

			@Override
			public void onExportSelectedClicked(CustomCheckBox checkBox) {
				resetMeasureFlags();
				searchDisplay.getErrorMessageDisplayForBulkExport().clearAlert();
				if (checkBox.getValue()) {
					if (manageMeasureSearchModel.getSelectedExportIds().size() > 89) {
						searchDisplay.getErrorMessageDisplayForBulkExport()
								.createAlert("Export file has a limit of 90 measures");
						searchDisplay.getExportSelectedButton().setFocus(true);
						checkBox.setValue(false);
					} else {
						manageMeasureSearchModel.getSelectedExportIds()
								.add(checkBox.getFormValue());
					}
				} else {
					manageMeasureSearchModel.getSelectedExportIds().remove(checkBox.getFormValue());
				}
			}

			@Override
			public void onHistoryClicked(ManageMeasureSearchModel.Result result) {
				resetMeasureFlags();
				historyDisplay.setReturnToLinkText("<< Return to Measure Library");
				displayHistory(result.getId(), result.getName());
			}

			@Override
			public void onShareClicked(ManageMeasureSearchModel.Result result) {
				resetMeasureFlags();
				displayShare(null, result.getId(), result.getName());
			}

			@Override
			public void onClearAllBulkExportClicked() {
				manageMeasureSearchModel.getSelectedExportResults().clear();
				manageMeasureSearchModel.getSelectedExportIds().clear();
			}

			@Override
			public void onCreateClicked(Result object) {
				ManageMeasureSearchModel.Result selectedMeasure = object;
				if (!isLoading && selectedMeasure.isDraftable()) {
					if (selectedMeasure.getId() != null) {
						setSearchingBusy(true);
						if(selectedMeasure.getIsComposite()){
							draftCompositeMeasure(selectedMeasure);
						}else {
							draftMeasure(selectedMeasure);
						}
					}
				} else if (!isLoading && selectedMeasure.isVersionable()) {
					versionDisplay.setSelectedMeasure(selectedMeasure);
					createVersion();
				}

			}

			private void draftCompositeMeasure(Result selectedMeasure) {
				MatContext.get().getMeasureService().getCompositeMeasure(selectedMeasure.getId(),
						new AsyncCallback<ManageCompositeMeasureDetailModel>() {
							@Override
							public void onFailure(Throwable caught) {
								setSearchingBusy(false);
								searchDisplay.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
								MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: " + caught.getLocalizedMessage(), 0);
							}

							@Override
							public void onSuccess(ManageCompositeMeasureDetailModel result) {
								searchDisplay.getErrorMessageDisplay().clearAlert();
								currentDetails = result;
								createDraftOfSelectedVersion(currentDetails);
							}
						});
				
			}

			private void draftMeasure(ManageMeasureSearchModel.Result selectedMeasure) {
				MatContext.get().getMeasureService().getMeasure(selectedMeasure.getId(),
						new AsyncCallback<ManageMeasureDetailModel>() {
							@Override
							public void onFailure(Throwable caught) {
								setSearchingBusy(false);
								searchDisplay.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
								MatContext.get().recordTransactionEvent(null, null, null,"Unhandled Exception: "+ caught.getLocalizedMessage(),0);
							}

							@Override
							public void onSuccess(ManageMeasureDetailModel result) {
								searchDisplay.getErrorMessageDisplay().clearAlert();
								currentDetails = result;
								createDraftOfSelectedVersion(currentDetails);
							}
						});
			}

		};
	}
	
	private void resetSearchFields(MeasureSearchModel measureSearchModel) {
		searchDisplay.resetDisplay();
		measureSearchModel.reset();
		addSearchPills(measureSearchModel);
	}
	
	private void addSearchPills(MeasureSearchModel measureSearchModel) {
		searchDisplay.getSearchPillPanel().setSearchedByPills(measureSearchModel, "Measures");
		searchDisplay.getSearchPillPanel().getReset().addClickHandler(event -> resetSearchFields(measureSearchModel));
	}

	public static void setSubSkipEmbeddedLink(String name) {
		if (subSkipContentHolder == null) {
			subSkipContentHolder = new FocusableWidget(SkipListBuilder.buildSkipList("Skip to Sub Content"));
		}
		Mat.removeInputBoxFromFocusPanel(subSkipContentHolder.getElement());
		Widget w = SkipListBuilder.buildSubSkipList(name);
		subSkipContentHolder.clear();
		subSkipContentHolder.add(w);
		subSkipContentHolder.setFocus(true);
	}

	private void searchDisplayHandlers(final SearchDisplay searchDisplay) {
		searchDisplay.getDraftConfirmationDialogBox().getYesButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireMeasureSelected(resultToFireEvent);
				auditMeasureSelected(resultToFireEvent);
				resultToFireEvent = new ManageMeasureSearchModel.Result();
			}
		});
		
		searchDisplay.getSelectIdForEditTool()
				.addSelectionHandler(new SelectionHandler<ManageMeasureSearchModel.Result>() {

					@Override
					public void onSelection(SelectionEvent<ManageMeasureSearchModel.Result> event) {

						searchDisplay.getErrorMeasureDeletion().clearAlert();
						searchDisplay.getSuccessMeasureDeletion().clearAlert();
						measureDeletion = false;
						isMeasureDeleted = false;
						isMeasureVersioned = false;
						if (!currentUserRole.equalsIgnoreCase(ClientConstants.ADMINISTRATOR)) {
							final String mid = event.getSelectedItem().getId();
							Result result = event.getSelectedItem();

							final String name = result.getName();
							final String version = result.getVersion();
							final String shortName = result.getShortName();
							final String scoringType = result.getScoringType();
							final boolean isEditable = result.isEditable();
							final boolean isMeasureLocked = result.isMeasureLocked();
							final boolean isDraft = result.isDraft();
							final String userId = result.getLockedUserId(result.getLockedUserInfo());

							MatContext.get().getMeasureLockService().isMeasureLocked(mid);
							Command waitForLockCheck = new Command() {
								@Override
								public void execute() {
									SynchronizationDelegate synchDel = MatContext.get().getSynchronizationDelegate();
									if (!synchDel.isCheckingLock()) {
										if (!synchDel.measureIsLocked()) {
											fireMeasureSelectedEvent(mid, version, name, shortName, scoringType,
													isEditable, isMeasureLocked, userId, isDraft);
											if (isEditable) {
												MatContext.get().getMeasureLockService().setMeasureLock();
											}
										} else {
											fireMeasureSelectedEvent(mid, version, name, shortName, scoringType, false,
													isMeasureLocked, userId, isDraft);
											if (isEditable) {
												MatContext.get().getMeasureLockService().setMeasureLock();
											}
										}
									} else {
										Scheduler.get().scheduleDeferred(this);
									}
								}
							};
							waitForLockCheck.execute();
						}
					}
				});

		searchDisplay.getMostRecentMeasureWidget().getSelectIdForEditTool()
				.addSelectionHandler(new SelectionHandler<ManageMeasureSearchModel.Result>() {
					@Override
					public void onSelection(SelectionEvent<ManageMeasureSearchModel.Result> event) {
						searchDisplay.getErrorMeasureDeletion().clearAlert();
						searchDisplay.getSuccessMeasureDeletion().clearAlert();
						measureDeletion = false;
						isMeasureDeleted = false;
						isMeasureVersioned = false;
						if (!currentUserRole.equalsIgnoreCase(ClientConstants.ADMINISTRATOR)) {
							final String mid = event.getSelectedItem().getId();
							Result result = event.getSelectedItem();
							final String name = result.getName();
							final String version = result.getVersion();
							final String shortName = result.getShortName();
							final String scoringType = result.getScoringType();
							final boolean isEditable = result.isEditable();
							final boolean isMeasureLocked = result.isMeasureLocked();
							final boolean isDraft = result.isDraft();
							final String userId = result.getLockedUserId(result.getLockedUserInfo());
							MatContext.get().getMeasureLockService().isMeasureLocked(mid);
							Command waitForLockCheck = new Command() {
								@Override
								public void execute() {
									SynchronizationDelegate synchDel = MatContext.get().getSynchronizationDelegate();
									if (!synchDel.isCheckingLock()) {
										if (!synchDel.measureIsLocked()) {
											fireMeasureSelectedEvent(mid, version, name, shortName, scoringType,
													isEditable, isMeasureLocked, userId, isDraft);
											if (isEditable) {
												MatContext.get().getMeasureLockService().setMeasureLock();
											}
										} else {
											fireMeasureSelectedEvent(mid, version, name, shortName, scoringType, false,
													isMeasureLocked, userId, isDraft);
											if (isEditable) {
												MatContext.get().getMeasureLockService().setMeasureLock();
											}
										}
									} else {
										Scheduler.get().scheduleDeferred(this);
									}
								}
							};
							waitForLockCheck.execute();
						}
					}
				});
		searchDisplay.getCreateMeasureButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				measureDeletion = false;
				isMeasureDeleted = false;
				isMeasureVersioned = false;
				createNewMeasure(); 
			}
		});
		
		searchDisplay.getCreateCompositeMeasureButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				measureDeletion = false;
				isMeasureDeleted = false;
				isMeasureVersioned = false;
				createNewCompositeMeasure();
			}
		});

		searchDisplay.getSearchButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int startIndex = 1;
				measureDeletion = false;
				isMeasureVersioned = false;
				searchDisplay.resetMessageDisplay();
				int filter = searchDisplay.getSelectedFilter();
				search(searchDisplay.getSearchString().getValue(), startIndex, Integer.MAX_VALUE, filter, true);
			}
		});

		searchDisplay.getBulkExportButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getErrorMessageDisplayForBulkExport().clearAlert();
				isMeasureDeleted = false;
				measureDeletion = false;
				isMeasureVersioned = false;
				searchDisplay.resetMessageDisplay();
				versionDisplay.getErrorMessageDisplay().clearAlert();

				detailDisplay.getErrorMessageDisplay().clearAlert();
				historyDisplay.getErrorMessageDisplay().clearAlert();
				shareDisplay.getErrorMessageDisplay().clearAlert();
				if (manageMeasureSearchModel.getSelectedExportIds().isEmpty()) {
					searchDisplay.getErrorMessageDisplayForBulkExport()
							.createAlert(MatContext.get().getMessageDelegate().getMeasureSelectionError());
				} else {
					bulkExport(manageMeasureSearchModel.getSelectedExportIds());
					searchDisplay.getMeasureSearchView().clearBulkExportCheckBoxes();

				}
			}
		});
		FormPanel form = searchDisplay.getForm();
		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				String errorMsg = event.getResults();
				if ((null != errorMsg) && errorMsg.contains("Exceeded Limit")) {
					List<String> err = new ArrayList<>();
					err.add("Export file size is " + errorMsg);
					err.add("File size limit is 100 MB");
					searchDisplay.getErrorMessageDisplayForBulkExport().createAlert(err);
				} else {
					Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				}
			}
		});

		searchDisplay.getMeasureSearchFilterWidget().getSearchInput().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					searchDisplay.getMeasureSearchFilterWidget().getSearchButton().click();
				}
			}
		});

		MatTextBox searchWidget = (MatTextBox) searchDisplay.getAdminSearchString();
		searchWidget.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					((Button) searchDisplay.getAdminSearchButton()).click();
				}				
			}
		});

		searchDisplay.getAdminSearchButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int startIndex = 1;
				searchDisplay.getErrorMessageDisplay().clearAlert();
				int filter = 1;
				search(searchDisplay.getAdminSearchString().getValue(), startIndex, Integer.MAX_VALUE, filter, false);
			}
		});
		
		searchDisplay.getTransferButton().addClickHandler(event -> displayActiveMATUsersToTransferMeasureOwnership());

		searchDisplay.getClearButton().addClickHandler(event -> clearAllMeasureOwnershipTransfers());

	}

	private void displayActiveMATUsersToTransferMeasureOwnership() {
		searchDisplay.clearTransferCheckBoxes();
		transferDisplay.getSearchString().setValue("");
		displayTransferView("", startIndex, Integer.MAX_VALUE);
	}
	
	private void clearAllMeasureOwnershipTransfers() {
		manageMeasureSearchModel.getSelectedTransferResults().clear();
		manageMeasureSearchModel.getSelectedTransferIds().clear();
		search(searchDisplay.getAdminSearchString().getValue(), 1, Integer.MAX_VALUE, 1, false);
	}
	
	private void searchHistory(String measureId, int startIndex, int pageSize) {
		List<String> filterList = new ArrayList<>();
		MatContext.get().getAuditService().executeMeasureLogSearch(measureId, startIndex, pageSize, filterList,
				new AsyncCallback<SearchHistoryDTO>() {
					@Override
					public void onFailure(Throwable caught) {
						historyDisplay.getErrorMessageDisplay()
								.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						MatContext.get().recordTransactionEvent(null, null, null,
								"Unhandled Exception: " + caught.getLocalizedMessage(), 0);
					}

					@Override
					public void onSuccess(SearchHistoryDTO data) {
						historyDisplay.buildCellTable(data.getLogs());
					}
				});
	}

	/** Method to Load most recent Used Measures for Logged In User. */
	private void searchRecentMeasures() {
		searchDisplay.getMostRecentMeasureVerticalPanel().setVisible(false);
		MatContext.get().getMeasureService().getAllRecentMeasureForUser(MatContext.get().getLoggedinUserId(),
				new AsyncCallback<ManageMeasureSearchModel>() {
					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(ManageMeasureSearchModel result) {
						searchDisplay.getMostRecentMeasureWidget().setMeasureSearchModel(result);
						searchDisplay.getMostRecentMeasureWidget().setObserver(new MostRecentMeasureWidget.Observer() {
							@Override
							public void onExportClicked(Result result) {
								measureDeletion = false;
								isMeasureDeleted = false;
								isMeasureVersioned = false;
								searchDisplay.resetMessageDisplay();
								export(result);
							}

						});
						searchDisplay.buildMostRecentWidget();
						searchDisplay.getMostRecentMeasureWidget().setTableObserver(createMeasureTableObserver());
						searchDisplay.getMostRecentMeasureVerticalPanel().setVisible(true);
					}
				});
	}

	public void setBulkExportMeasureIds(List<String> bulkExportMeasureIds) {
		this.bulkExportMeasureIds = bulkExportMeasureIds;
	}

	private void setCompositeDetailsToView() {
		compositeDetailDisplay.getName().setValue(currentCompositeMeasureDetails.getName());
		compositeDetailDisplay.getShortName().setValue(currentCompositeMeasureDetails.getShortName());
		((ManageCompositeMeasureDetailView) compositeDetailDisplay).setCompositeScoringSelectedValue(currentCompositeMeasureDetails.getCompositeScoringMethod());
		compositeDetailDisplay.getMeasScoringChoice().setValueMetadata(currentCompositeMeasureDetails.getMeasScoring());
	}
	
	private void setDetailsToView() {
		detailDisplay.getName().setValue(currentDetails.getName());
		detailDisplay.getShortName().setValue(currentDetails.getShortName());
		detailDisplay.getMeasScoringChoice().setValueMetadata(currentDetails.getMeasScoring());
	}

	private void shareDisplayHandlers(final ShareDisplay shareDisplay) {

		shareDisplay.getCancelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				isClone = false;
				displaySearch();
			}
		});
		shareDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MatContext.get().getMeasureService().updateUsersShare(currentShareDetails, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						shareDisplay.getErrorMessageDisplay()
								.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						MatContext.get().recordTransactionEvent(null, null, null,
								"Unhandled Exception: " + caught.getLocalizedMessage(), 0);
					}

					@Override
					public void onSuccess(Void result) {
						searchDisplay.resetMessageDisplay();
						measureShared = currentShareDetails.getData().stream().anyMatch(sd -> (sd.getShareLevel() != null && !(sd.getShareLevel().equals(""))));
						if(measureShared) {
							measureShareMessage = MessageDelegate.getMeasureSuccessfullyShared(currentShareDetails.getMeasureName());
						}
						displaySearch();
					}
				});
			}
		});

		shareDisplay.privateCheckbox().addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				MatContext.get().getMeasureService().updatePrivateColumnInMeasure(currentShareDetails.getMeasureId(),
						event.getValue(), new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {}

							@Override
							public void onSuccess(Void result) {}
						});
			}
		});

		shareDisplay.getSearchWidgetBootStrap().getGo().addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				displayShare(shareDisplay.getSearchWidgetBootStrap().getSearchBox().getValue(), currentShareDetails.getMeasureId());				
			}
		});
		
		shareDisplay.getSearchWidgetFocusPanel().addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					shareDisplay.getSearchWidgetBootStrap().getGo().click();
				}
			}
		}); 
	}

	private void showAdminSearchingBusy(boolean busy) {
		toggleLoadingMessage(busy);
		((Button) searchDisplay.getAdminSearchButton()).setEnabled(!busy);
		((TextBox) (searchDisplay.getAdminSearchString())).setEnabled(!busy);
		((Button) transferDisplay.getSearchButton()).setEnabled(!busy);
		((TextBox) (transferDisplay.getSearchString())).setEnabled(!busy);

	}
	
	private void setSearchingBusy(boolean busy) {
		toggleLoadingMessage(busy);
		((Button) searchDisplay.getSearchButton()).setEnabled(!busy);
		((Button) searchDisplay.getBulkExportButton()).setEnabled(!busy);
		((TextBox) (searchDisplay.getSearchString())).setEnabled(!busy);
		((Button) versionDisplay.getSaveButton()).setEnabled(!busy);
		((Button) versionDisplay.getCancelButton()).setEnabled(!busy);
		searchDisplay.getCreateMeasureButton().setEnabled(!busy);
		searchDisplay.getCreateCompositeMeasureButton().setEnabled(!busy);
		searchDisplay.getCustomFilterCheckBox().setEnabled(!busy);
		searchDisplay.getMeasureSearchFilterWidget().getAdvancedSearchPanel().getAdvanceSearchAnchor().setEnabled(!busy);
	}

	private void toggleLoadingMessage(boolean busy) {
		isLoading = busy;
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
	}

	private void transferDisplayHandlers(final TransferOwnershipView transferDisplay) {

		transferDisplay.getSaveButton().addClickHandler(event -> saveTransferMeasureOwnership());
		
		transferDisplay.getCancelButton().addClickHandler(event -> cancelTransferMeasureOwnership());

		MatTextBox searchWidget = (MatTextBox) transferDisplay.getSearchString();
		searchWidget.addKeyUpHandler(keyUpEvent -> simulateButtonClick(keyUpEvent));
				
		transferDisplay.getSearchButton().addClickHandler(event -> searchMeasureLibraryOwners());
	}

	private void saveTransferMeasureOwnership() {
		transferDisplay.getErrorMessageDisplay().clearAlert();
		transferDisplay.getSuccessMessageDisplay().clearAlert();
		boolean userSelected = false;
		for (int i = 0; i < model.getData().size(); i = i + 1) {
			if (model.getData().get(i).isSelected()) {
				userSelected = true;
				final String emailTo = model.getData().get(i).getEmailId();
				final int rowIndex = i;

				MatContext.get().getMeasureService().transferOwnerShipToUser(
						manageMeasureSearchModel.getSelectedTransferIds(), emailTo, new AsyncCallback<Void>() {
							@Override
							public void onFailure(Throwable caught) {
								Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
								model.getData().get(rowIndex).setSelected(false);
								transferDisplay.clearRadioButtons();
							}

							@Override
							public void onSuccess(Void result) {
								model.getData().get(rowIndex).setSelected(false);
								transferDisplay.clearRadioButtons();
								transferDisplay.getSuccessMessageDisplay().clearAlert();
								transferDisplay.getErrorMessageDisplay().clearAlert();
								int filter = 1;
								search(searchDisplay.getAdminSearchString().getValue(), 1, Integer.MAX_VALUE, filter, false);
								searchDisplay.getSuccessMessageDisplay().createAlert(
										MatContext.get().getMessageDelegate().getTransferOwnershipSuccess() + emailTo);
							}
						});
			}
		}
		if (!userSelected) {
			transferDisplay.getSuccessMessageDisplay().clearAlert();
			transferDisplay.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getUserRequiredErrorMessage());
		}
	}
	
	private void simulateButtonClick(KeyUpEvent event) { 
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			((Button) transferDisplay.getSearchButton()).click();	
		}				
	}
	
	private void cancelTransferMeasureOwnership() {
		manageMeasureSearchModel.getSelectedTransferResults().clear();
		manageMeasureSearchModel.getSelectedTransferIds().clear();
		transferDisplay.getSuccessMessageDisplay().clearAlert();
		transferDisplay.getErrorMessageDisplay().clearAlert();
		searchDisplay.getSuccessMessageDisplay().clearAlert();
		int filter = 1;
		search(searchDisplay.getAdminSearchString().getValue(), 1, Integer.MAX_VALUE, filter, false);
	}
	
	private void searchMeasureLibraryOwners() {
		transferDisplay.getSuccessMessageDisplay().clearAlert();
		displayTransferView(transferDisplay.getSearchString().getValue(), startIndex, Integer.MAX_VALUE);
	}
	
	private void update() {
		if (!MatContext.get().getLoadingQueue().isEmpty()) {
			return;
		}

		setSearchingBusy(true);
		updateDetailsFromView();

		if (isClone && isValid(currentDetails, isClone)) {
			cloneMeasure(currentDetails, false);
		} else if (isValid(currentDetails, false)) {
			final boolean isInsert = currentDetails.getId() == null;
			final String name = currentDetails.getName();
			final String shortName = currentDetails.getShortName();
			final String scoringType = currentDetails.getMeasScoring();
			final String version = currentDetails.getVersionNumber()+"."+currentDetails.getRevisionNumber();		
			final boolean isDraft = currentDetails.isDraft();
			MatContext.get().getMeasureService().save(currentDetails, new AsyncCallback<SaveMeasureResult>() {

				@Override
				public void onFailure(Throwable caught) {
					detailDisplay.getErrorMessageDisplay().createAlert(caught.getLocalizedMessage());
					setSearchingBusy(false);
				}

				@Override
				public void onSuccess(SaveMeasureResult result) {
					postSaveMeasureEvents(isInsert, result, detailDisplay, name, shortName, scoringType, version, isDraft);
					setSearchingBusy(false);
				}
			});
		}
	}

	/**
	 * Update details from view.
	 */
	private void updateDetailsFromView() {
		currentDetails.setName(detailDisplay.getName().getValue().trim());
		currentDetails.setShortName(detailDisplay.getShortName().getValue().trim());
		String measureScoring = detailDisplay.getMeasScoringValue();

		currentDetails.setMeasScoring(measureScoring);

		if(detailDisplay.getPatientBasedInput().getItemText(detailDisplay.getPatientBasedInput().getSelectedIndex()).equalsIgnoreCase("Yes")) {
			currentDetails.setIsPatientBased(true);
		} else {
			currentDetails.setIsPatientBased(false);
		}
		
		currentDetails.scrubForMarkUp();
		detailDisplay.getName().setValue(currentDetails.getName());
		detailDisplay.getShortName().setValue(currentDetails.getShortName());
		MatContext.get().setCurrentMeasureName(currentDetails.getName());
		MatContext.get().setCurrentShortName(currentDetails.getShortName());
		MatContext.get().setCurrentMeasureScoringType(currentDetails.getMeasScoring());
	}
	
	private void updateCompositeDetailsFromCompositeDetailView() {
		currentCompositeMeasureDetails.setName(compositeDetailDisplay.getName().getValue().trim());
		currentCompositeMeasureDetails.setShortName(compositeDetailDisplay.getShortName().getValue().trim());
		currentCompositeMeasureDetails.setCompositeScoringMethod(((ManageCompositeMeasureDetailView)compositeDetailDisplay).getCompositeScoringValue());
		String measureScoring = compositeDetailDisplay.getMeasScoringValue();
		currentCompositeMeasureDetails.setMeasScoring(measureScoring);
		if(compositeDetailDisplay.getPatientBasedInput().getItemText(compositeDetailDisplay.getPatientBasedInput().getSelectedIndex()).equalsIgnoreCase("Yes")) {
			currentCompositeMeasureDetails.setIsPatientBased(true);
		} else {
			currentCompositeMeasureDetails.setIsPatientBased(false);
		}
		currentCompositeMeasureDetails.setQdmVersion(MatContext.get().getCurrentQDMVersion());
		currentCompositeMeasureDetails.scrubForMarkUp();
	}
	
	private void updateCompositeDetailsFromComponentMeasureDisplay() {
		currentCompositeMeasureDetails.setAppliedComponentMeasures(componentMeasureDisplay.getComponentMeasureSearch().getAppliedComponentMeasuresList());
		currentCompositeMeasureDetails.setAliasMapping(componentMeasureDisplay.getComponentMeasureSearch().getAliasMapping());
	}
	
	private void postSaveMeasureEvents(boolean isInsert, SaveMeasureResult result, DetailDisplay detailDisplay,
			String name, String shortName, String scoringType, String version, boolean isDraft) {
		
		if (result.isSuccess()) {
			if (isInsert) {
				fireMeasureSelectedEvent(result.getId(), version, name, shortName, scoringType, true, false, null, isDraft);
				fireMeasureEditEvent();
			} else {
				displaySearch();
			}
		} else {
			String message = null;
			if(SaveMeasureResult.INVALID_DATA == result.getFailureReason()) {
				message = "Data Validation Failed.Please verify data.";
			} else {
				message = "Unknown Code " + result.getFailureReason();
			}
			detailDisplay.getErrorMessageDisplay().createAlert(message);
		}	
		
		componentMeasureDisplay.setComponentBusy(false);
	}
	

	
	private void saveCompositeMeasure() {
		final boolean isInsert = currentCompositeMeasureDetails.getId() == null;
		final String name = currentCompositeMeasureDetails.getName();
		final String shortName = currentCompositeMeasureDetails.getShortName();
		final String scoringType = currentCompositeMeasureDetails.getMeasScoring();
		final String version = currentCompositeMeasureDetails.getVersionNumber() + "." + currentCompositeMeasureDetails.getRevisionNumber();
		final boolean isDraft = currentCompositeMeasureDetails.isDraft();
		MatContext.get().getMeasureService().saveCompositeMeasure(currentCompositeMeasureDetails, new AsyncCallback<SaveMeasureResult>() {

			@Override
			public void onFailure(Throwable caught) {
				compositeDetailDisplay.getErrorMessageDisplay().createAlert(caught.getLocalizedMessage());
				componentMeasureDisplay.setComponentBusy(false);
			}

			@Override
			public void onSuccess(SaveMeasureResult result) {
				postSaveMeasureEvents(isInsert, result, compositeDetailDisplay, name, shortName, scoringType, version, isDraft);
				componentMeasureDisplay.setComponentBusy(false);

			}
			
		});
	}


	private void updateTransferIDs(Result result, ManageMeasureSearchModel model) {
		if (result.isTransferable()) {
			List<String> selectedIdList = model.getSelectedTransferIds();
			if (!selectedIdList.contains(result.getId())) {
				model.getSelectedTransferResults().add(result);
				selectedIdList.add(result.getId());
			}
		} else {
			for (int i = 0; i < model.getSelectedTransferIds().size(); i++) {
				if (result.getId().equals(model.getSelectedTransferResults().get(i).getId())) {
					model.getSelectedTransferIds().remove(i);
					model.getSelectedTransferResults().remove(i);
				}
			}

		}
	}


	private void updateExportedIDs(Result result, ManageMeasureSearchModel model, boolean isCBChecked) {
		List<String> selectedIdList = model.getSelectedExportIds();
		
		if (isCBChecked) {
			if (!selectedIdList.contains(result.getId())) {
				model.getSelectedExportResults().add(result);
				selectedIdList.add(result.getId());
			}
		} else {
			for (int i = 0; i < model.getSelectedExportIds().size(); i++) {
				if (result.getId().equals(model.getSelectedExportResults().get(i).getId())) {
					model.getSelectedExportIds().remove(i);
					model.getSelectedExportResults().remove(i);
				}
			}

		}
	}
	
	private void versionDisplayHandlers(final VersionDisplay versionDisplay) {
		
		MatContext.get().getEventBus().addHandler(MeasureVersionEvent.TYPE, new MeasureVersionEvent.Handler() {

			@Override
			public void onVersioned(MeasureVersionEvent event) {
				displaySearch();
				if (event.isVersioned()) {

					measureDeletion = false;
					isMeasureDeleted = false;
					isMeasureVersioned = true;
					measureVerMessage = event.getMessage();
				} else {
					
					measureDeletion = false;
					isMeasureDeleted = false;
					isMeasureVersioned = false;
					measureVerMessage = event.getMessage();
				}
			}
		});
		
		versionDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(!isLoading) {
					isMeasureDeleted = false;
					measureDeletion = false;
					ManageMeasureSearchModel.Result selectedMeasure = versionDisplay.getSelectedMeasure();
					versionDisplay.getErrorMessageDisplay().clearAlert();
					if (((selectedMeasure != null) && (selectedMeasure.getId() != null))
							&& (versionDisplay.getMajorRadioButton().getValue()
									|| versionDisplay.getMinorRadioButton().getValue())) {
						
						boolean shouldPackage = true; 
						boolean ignoreUnusedIncludedLibraries = false; 
						saveFinalizedVersion(selectedMeasure.getId(), selectedMeasure.getName(),versionDisplay.getMajorRadioButton().getValue(), selectedMeasure.getVersion(), shouldPackage, ignoreUnusedIncludedLibraries);		
					} else {
						versionDisplay.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getERROR_LIBRARY_VERSION());
					}
				}
			}
		});

		versionDisplay.getCancelButton().addClickHandler(cancelClickHandler);
	}

	private void resetMeasureFlags() {
		measureDeletion = false;
		measureShared = false;
		isMeasureDeleted = false;
		isMeasureVersioned = false;
		searchDisplay.getSuccessMeasureDeletion().clearAlert();
		searchDisplay.getErrorMeasureDeletion().clearAlert();
	}

	public ContentWithHeadingWidget getPanel() {
		return panel;
	}
}