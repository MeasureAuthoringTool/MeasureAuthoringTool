package mat.client.measure;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonDismiss;
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
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import mat.DTO.CompositeMeasureScoreDTO;
import mat.DTO.SearchHistoryDTO;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.TabObserver;
import mat.client.codelist.HasListBox;
import mat.client.codelist.events.OnChangeMeasureVersionOptionsEvent;
import mat.client.cqlworkspace.AbstractCQLWorkspacePresenter;
import mat.client.event.MeasureDeleteEvent;
import mat.client.event.MeasureEditEvent;
import mat.client.event.MeasureSelectedEvent;
import mat.client.event.MeasureVersionEvent;
import mat.client.event.UmlsActivatedEvent;
import mat.client.export.ManageExportPresenter;
import mat.client.export.ManageExportView;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.measure.MeasureSearchView.Observer;
import mat.client.measure.service.FhirConvertResultResponse;
import mat.client.measure.service.FhirMeasureRemoteService;
import mat.client.measure.service.FhirMeasureRemoteServiceAsync;
import mat.client.measure.service.MeasureCloningRemoteService;
import mat.client.measure.service.MeasureCloningRemoteServiceAsync;
import mat.client.measure.service.SaveMeasureResult;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.shared.ConfirmationObserver;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.FocusableWidget;
import mat.client.shared.MatContext;
import mat.client.shared.MatTabLayoutPanel;
import mat.client.shared.MessageDelegate;
import mat.client.shared.MostRecentMeasureWidget;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SearchWidgetWithFilter;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SynchronizationDelegate;
import mat.client.shared.WarningConfirmationMessageAlert;
import mat.client.shared.search.SearchResultUpdate;
import mat.client.util.ClientConstants;
import mat.client.util.FeatureFlagConstant;
import mat.client.util.MatTextBox;
import mat.shared.CompositeMeasureValidationResult;
import mat.shared.ConstantMessages;
import mat.shared.MatConstants;
import mat.shared.MeasureSearchModel;
import mat.shared.SaveUpdateCQLResult;
import mat.shared.StringUtility;
import mat.shared.validator.measure.ManageCompositeMeasureModelValidator;
import mat.shared.validator.measure.ManageMeasureModelValidator;

public class ManageMeasurePresenter implements MatPresenter, TabObserver {

    private static final String CONTINUE = "Continue";

    private static final String MEASURE_LIBRARY = "MeasureLibrary";

    private static final String COMPOSITE_MEASURE = "CompositeMeasure";

    private static final String UNHANDLED_EXCEPTION = "Unhandled Exception: ";

    private final Logger logger = Logger.getLogger("MAT");

    private List<String> bulkExportMeasureIds;

    private WarningConfirmationMessageAlert warningConfirmationMessageAlert;

    private ClickHandler cancelClickHandler = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            setIsPageDirty(false);
            if (!isLoading) {
                isClone = false;

                if (detailDisplay != null) {
                    detailDisplay.clearFields();
                }

                if (compositeDetailDisplay != null) {
                    compositeDetailDisplay.getMeasureNameTextBox().setValue("");
                    compositeDetailDisplay.getECQMAbbreviatedTitleTextBox().setValue("");
                    compositeDetailDisplay.clearFields();
                }

                if (componentMeasureDisplay != null) {
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

    private HandlerRegistration saveHandler = null;

    private HandlerRegistration compositeSaveHandler = null;

    private boolean isDirty = false;

    private MatPresenter targetPresenter;

    private MatTabLayoutPanel targetTabLayout;

    private MatPresenter sourcePresenter;

    private HandlerRegistration yesHandler;

    private HandlerRegistration noHandler;

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
            addMeasureLibrarySearchHandlers(searchDisplay);
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
        if (compositeDetailDisplay != null) {
            compositeDetailDisplayHandlers(compositeDetailDisplay);
        }
        if (componentMeasureDisplay != null) {
            componentMeasureDisplayHandlers();
        }

        HandlerManager eventBus = MatContext.get().getEventBus();

        // This event will be called when measure is successfully deleted and
        // then MeasureLibrary is reloaded.
        eventBus.addHandler(MeasureDeleteEvent.TYPE, new MeasureDeleteEvent.Handler() {

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

        eventBus.addHandler(OnChangeMeasureVersionOptionsEvent.TYPE, new OnChangeMeasureVersionOptionsEvent.Handler() {
            @Override
            public void onChangeOptions(OnChangeMeasureVersionOptionsEvent event) {
                PrimaryButton button = (PrimaryButton) versionDisplay.getSaveButton();
                button.setFocus(true);
            }
        });

        eventBus.addHandler(UmlsActivatedEvent.TYPE, new UmlsActivatedEvent.Handler() {

            @Override
            public void onSuccessfulLogin(UmlsActivatedEvent event) {
                searchDisplay.resetMessageDisplay();
            }
        });
    }

    @Override
    public void beforeClosingDisplay() {
        setIsPageDirty(false);
        searchDisplay.resetMessageDisplay();
        isMeasureDeleted = false;
        measureShared = false;
        measureDeletion = false;
        isMeasureVersioned = false;
        isClone = false;
        isLoading = false;
        if (detailDisplay != null) {
            detailDisplay.getMessageFormGrp().setValidationState(ValidationState.NONE);
            detailDisplay.getHelpBlock().setText("");
            detailDisplay.getErrorMessageDisplay().clearAlert();
        }
        searchDisplay.getAdminSearchString().setValue("");
        if (transferDisplay != null)
            transferDisplay.getSearchString().setValue("");
        searchDisplay.getMeasureSearchFilterWidget().setVisible(false);
        searchDisplay.getMostRecentMeasureVerticalPanel().setVisible(false);
        removeHandlers();
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

    public WarningConfirmationMessageAlert getWarningConfirmationMessageAlert() {
        return warningConfirmationMessageAlert;
    }


    private void bulkExport(List<String> selectedMeasureIds) {
        StringBuilder measureId = new StringBuilder("");

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
            if (measureId.length() > 0) {
                measureId.append("&id=");
            }
            measureId.append(id);
        }
        String url = GWT.getModuleBaseURL() + "bulkExport?id=" + measureId + "&type=open";
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

    public void fireMeasureSelected(ManageMeasureSearchModel.Result result) {
        fireMeasureSelectedEvent(result.getId(), result.getVersion(), result.getName(),
                result.getShortName(), result.getScoringType(), result.isEditable(),
                result.isMeasureLocked(), result.getLockedUserId(result.getLockedUserInfo()), result.isDraft(), calculatePatientBased(result.isPatientBased(), result.getScoringType()), result.getMeasureModel());
        setSearchingBusy(false);
        isClone = false;
    }

    private void auditMeasureDraft(ManageMeasureSearchModel.Result result) {
        MatContext.get().getAuditService().recordMeasureEvent(result.getId(), "Draft Created",
                "Draft created based on Version v" + result.getVersionValue(), false,
                new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                    }
                });
    }


    private void clearErrorMessageAlerts() {
        detailDisplay.getWarningConfirmationMessageAlert().clearAlert();
        compositeDetailDisplay.getWarningConfirmationMessageAlert().clearAlert();
        detailDisplay.getErrorMessageDisplay().clearAlert();
        searchDisplay.getErrorMessageDisplayForBulkExport().clearAlert();
        Mat.focusSkipLists(MEASURE_LIBRARY);
    }

    private void createVersion() {
        versionDisplay.getMessagePanel().clearAlerts();
        searchDisplay.resetMessageDisplay();
        panel.getButtonPanel().clear();
        panel.setHeading("My Measures > Create Measure Version of Draft", MEASURE_LIBRARY);
        panel.setContent(versionDisplay.asWidget());
        Mat.focusSkipLists(MEASURE_LIBRARY);
        clearRadioButtonSelection();
        checkAndDisplayLibraryNameWarning();
    }

    private void checkAndDisplayLibraryNameWarning() {
        if (versionDisplay.getSelectedMeasure().getCqlLibraryName().length() > AbstractCQLWorkspacePresenter.CQL_LIBRARY_NAME_WARNING_LENGTH) {
            versionDisplay.getMessagePanel().getWarningMessageAlert().createAlert(AbstractCQLWorkspacePresenter.CQL_LIBRARY_NAME_WARNING_MESSAGE);
        }
    }

    private void componentMeasureDisplayHandlers() {

        componentMeasureDisplay.getCancelButton().addClickHandler(cancelClickHandler);
        componentMeasureDisplay.getBackButton().addClickHandler(event -> displayNewCompositeMeasureWidget());
        componentMeasureDisplay.getSaveButton().addClickHandler(event -> validateOnCompositeContinueButtonClick());
    }

    private void validateOnCompositeContinueButtonClick() {
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
                if (isValidCompositeMeasureForSave(result.getMessages())) {
                    createNewCompositeMeasure();
                }
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
                ((NewCompositeMeasureView) compositeDetailDisplay).setCompositeScoringChoices(compositeChoices);
            }
        });
        ((NewCompositeMeasureView) compositeDetailDisplay).getMeasureScoringListBox().addChangeHandler(event -> setPatientBasedIndicatorBasedOnScoringChoice((((NewCompositeMeasureView) compositeDetailDisplay))));

        ((NewCompositeMeasureView) compositeDetailDisplay).getMeasureNameTextBox().addValueChangeHandler(event -> setIsPageDirty(true));
        ((NewCompositeMeasureView) compositeDetailDisplay).getECQMAbbreviatedTitleTextBox().addValueChangeHandler(event -> setIsPageDirty(true));
        ((NewCompositeMeasureView) compositeDetailDisplay).getCQLLibraryNameTextBox().addValueChangeHandler(event -> setIsPageDirty(true));
        ((NewCompositeMeasureView) compositeDetailDisplay).getMeasureScoringListBox().addValueChangeHandler(event -> setIsPageDirty(true));
        ((NewCompositeMeasureView) compositeDetailDisplay).getPatientBasedListBox().addValueChangeHandler(event -> setIsPageDirty(true));
        ((NewCompositeMeasureView) compositeDetailDisplay).getCompositeScoringListBox().addChangeHandler(event -> setIsPageDirty(true));
        ((NewCompositeMeasureView) compositeDetailDisplay).getCompositeScoringListBox().addChangeHandler(event -> createSelectionMapAndSetScorings());
    }

    private void setIsPageDirty(boolean isPageDirty) {
        isDirty = isPageDirty;
    }

    private void createSelectionMapAndSetScorings() {
        String compositeScoringValue = ((NewCompositeMeasureView) compositeDetailDisplay).getCompositeScoringValue();
        compositeDetailDisplay.setScoringChoices(MatContext.get().getSelectionMap().get(compositeScoringValue));
        setPatientBasedIndicatorBasedOnScoringChoice(compositeDetailDisplay);
    }


    private void updateCompositeDetailsOnContinueButton() {
        compositeDetailDisplay.getErrorMessageDisplay().clearAlert();
        boolean isEdit = false;
        if (componentMeasureDisplay != null) {
            if (currentCompositeMeasureDetails.getId() != null) {
                isEdit = true;
            }
            componentMeasureDisplay.getComponentMeasureSearch().clearFields(isEdit);
            componentMeasureDisplay.getMessagePanel().clearAlerts();
        }

        updateCompositeDetailsFromCompositeDetailView();

        if (isValidCompositeMeasure(currentCompositeMeasureDetails)) {
            MatContext.get().getMeasureService().checkIfLibraryNameExists(currentCompositeMeasureDetails.getCQLLibraryName(),
                    currentCompositeMeasureDetails.getMeasureSetId(), new AsyncCallback<Boolean>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            compositeDetailDisplay.getErrorMessageDisplay().createAlert(caught.getLocalizedMessage());
                            componentMeasureDisplay.setComponentBusy(false);
                        }

                        @Override
                        public void onSuccess(Boolean result) {
                            if (result) {
                                compositeDetailDisplay.getErrorMessageDisplay().createAlert(MessageDelegate.DUPLICATE_LIBRARY_NAME);

                            } else {
                                String panelHeading = "";
                                if (StringUtility.isEmptyOrNull(currentCompositeMeasureDetails.getId())) {
                                    panelHeading = "My Measures > Create New Composite Measure > Component Measures";
                                } else {
                                    panelHeading = "My Measures > Edit Composite Measure > Update Component Measures.";
                                }

                                displayComponentDetails(panelHeading);

                            }
                        }
                    });
        }

    }

    private void showConfirmationDialog(final String message) {
        detailDisplay.getConfirmationDialogBox().show(message);
        detailDisplay.getConfirmationDialogBox().getYesButton().setDataDismiss(ButtonDismiss.MODAL);
        detailDisplay.getConfirmationDialogBox().getYesButton().setTitle(CONTINUE);
        detailDisplay.getConfirmationDialogBox().getYesButton().setText(CONTINUE);
        detailDisplay.getConfirmationDialogBox().getYesButton().setFocus(true);
    }

    private void detailDisplayHandlers(final DetailDisplay detailDisplay) {
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

        detailDisplay.getMeasureScoringListBox().addChangeHandler(event -> setPatientBasedIndicatorBasedOnScoringChoice((detailDisplay)));

        detailDisplay.getMeasureNameTextBox().addValueChangeHandler(event -> setIsPageDirty(true));
        detailDisplay.getECQMAbbreviatedTitleTextBox().addValueChangeHandler(event -> setIsPageDirty(true));
        detailDisplay.getCQLLibraryNameTextBox().addValueChangeHandler(event -> setIsPageDirty(true));
        detailDisplay.getMeasureScoringListBox().addValueChangeHandler(event -> setIsPageDirty(true));
        detailDisplay.getPatientBasedListBox().addValueChangeHandler(event -> setIsPageDirty(true));
    }

    private void setPatientBasedIndicatorBasedOnScoringChoice(DetailDisplay detailDisplay) {

        if (MatConstants.CONTINUOUS_VARIABLE.equalsIgnoreCase(detailDisplay.getMeasureScoringListBox().getItemText(detailDisplay.getMeasureScoringListBox().getSelectedIndex()))) {
            if (detailDisplay.getPatientBasedListBox().getItemCount() > 1) {
                // yes is the second element in the list, so the 1 index.
                detailDisplay.getPatientBasedListBox().removeItem(1);
            }
            detailDisplay.getPatientBasedListBox().setSelectedIndex(0);
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

    private void confirmAndConvertFhir(Result object) {
        ConfirmationDialogBox confirmationDialogBox = new ConfirmationDialogBox("Are you sure you want to convert this measure again? The existing FHIR measure will be overwritten.", "Yes", "No", null, false);
        confirmationDialogBox.getNoButton().setVisible(true);
        confirmationDialogBox.setObserver(new ConfirmationObserver() {

            @Override
            public void onYesButtonClicked() {
                convertMeasureFhir(object);
            }

            @Override
            public void onNoButtonClicked() {
                // Just skip any conversion
            }

            @Override
            public void onClose() {
                // Just skip any conversion
            }
        });

        confirmationDialogBox.show();
    }

    private void convertMeasureFhir(Result object) {
        if (showAlertAndReturnIfNotUMLSLoggedIn()) return;

        GWT.log("Please wait. Conversion is in progress...");

        if (!MatContext.get().getLoadingQueue().isEmpty()) {
            return;
        }

        setSearchingBusy(true);
        FhirMeasureRemoteServiceAsync fhirMeasureService = GWT.create(FhirMeasureRemoteService.class);
        fhirMeasureService.convert(object, new AsyncCallback<FhirConvertResultResponse>() {
            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error while converting the measure id " + object.getId() + ". Error message: " + caught.getMessage(), caught);
                setSearchingBusy(false);
                showFhirConversionError(MatContext.get().getMessageDelegate().getConvertMeasureFailureMessage());
                MatContext.get().recordTransactionEvent(null, null, null, UNHANDLED_EXCEPTION + caught.getLocalizedMessage(), 0);
            }

            @Override
            public void onSuccess(FhirConvertResultResponse response) {
                String outcome = response.getValidationStatus().getOutcome();
                String errorReason = response.getValidationStatus().getErrorReason();
                logger.log(Level.INFO, "Measure " + object.getId() + " conversion has completed. Outcome: " + outcome + " errorReason: " + errorReason);
                setSearchingBusy(false);
                if (!response.isSuccess()) {
                    logger.log(Level.SEVERE, "Measure id " + object.getId() + " cannot be converted due to FHIR validation errors. Outcome " + outcome + " errorReason: " + errorReason);
                    showFhirConversionError(MatContext.get().getMessageDelegate().getCannotConvertMeasureValidationFailedMessage());
                } else {
                    logger.log(Level.INFO, "Your measure was successfully converted to FHIR.");
                }
                //On conversion show the fhir measure validation errors.
                showFhirValidationReport(response.getFhirMeasure().getId());
                displaySearch();
            }
        });

    }

    private void showFhirValidationReport(String measureId) {
        if (showAlertAndReturnIfNotUMLSLoggedIn()) return;

        String url = GWT.getModuleBaseURL() + "validationReport?id=" + SafeHtmlUtils.htmlEscape(measureId);
        Window.open(url, "_blank", "");
    }

    private boolean showAlertAndReturnIfNotUMLSLoggedIn() {
        if (!MatContext.get().isUMLSLoggedIn()) {
            searchDisplay.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN());
            searchDisplay.getErrorMessageDisplay().setVisible(true);
            setSearchingBusy(false);
            return true;
        } else {
            searchDisplay.resetMessageDisplay();
        }
        return false;
    }

    private void showFhirConversionError(final String errorMessage) {
        ConfirmationDialogBox errorAlert = new ConfirmationDialogBox(errorMessage, "Return to Measure Library", "Cancel", null, true);
        errorAlert.getNoButton().setVisible(false);
        errorAlert.setObserver(new ConfirmationObserver() {

            @Override
            public void onYesButtonClicked() {
                displaySearch();
            }

            @Override
            public void onNoButtonClicked() {
            }

            @Override
            public void onClose() {
                displaySearch();
            }
        });
        errorAlert.show();
    }


    private void cloneMeasure() {
        if (!MatContext.get().getLoadingQueue().isEmpty()) {
            return;
        }

        updateDetailsFromView();

        searchDisplay.resetMessageDisplay();
        MeasureCloningRemoteServiceAsync measureCloningService = GWT.create(MeasureCloningRemoteService.class);

        if (isValid(currentDetails, false)) {
            measureCloningService.cloneExistingMeasure(currentDetails, new AsyncCallback<ManageMeasureSearchModel.Result>() {
                @Override
                public void onFailure(Throwable caught) {
                    detailDisplay.getErrorMessageDisplay().createAlert(caught.getLocalizedMessage());
                    MatContext.get().recordTransactionEvent(null, null, null, UNHANDLED_EXCEPTION + caught.getLocalizedMessage(), 0);
                }

                @Override
                public void onSuccess(ManageMeasureSearchModel.Result result) {
                    setIsPageDirty(false);
                    displaySuccessAndFireSelectedEvent(result, MatContext.get().getMessageDelegate().getCloneMeasureSuccessfulMessage(detailDisplay.getMeasureNameTextBox().getValue()));
                }
            });
        } else {
            setSearchingBusy(false);
        }
    }

    private void draftCompositeMeasure() {
        setIsPageDirty(false);
        if (!MatContext.get().getLoadingQueue().isEmpty()) {
            return;
        }

        setSearchingBusy(true);
        updateCompositeDetailsFromCompositeDetailView();
        updateCompositeDetailsFromComponentMeasureDisplay();

        searchDisplay.resetMessageDisplay();
        MeasureCloningRemoteServiceAsync measureCloningService = GWT.create(MeasureCloningRemoteService.class);
        if (isValidCompositeMeasure(currentCompositeMeasureDetails)) {
            measureCloningService.draftExistingMeasure(currentCompositeMeasureDetails, new AsyncCallback<ManageMeasureSearchModel.Result>() {
                @Override
                public void onFailure(Throwable caught) {
                    setSearchingBusy(false);
                    compositeDetailDisplay.getErrorMessageDisplay().createAlert(caught.getLocalizedMessage());
                    MatContext.get().recordTransactionEvent(null, null, null, UNHANDLED_EXCEPTION + caught.getLocalizedMessage(), 0);
                }

                @Override
                public void onSuccess(ManageMeasureSearchModel.Result result) {
                    auditMeasureDraft(result);
                    displaySuccessAndFireSelectedEvent(result, MatContext.get().getMessageDelegate().getMeasureDraftSuccessfulMessage(compositeDetailDisplay.getMeasureNameTextBox().getValue()));
                }

            });
        } else {
            setSearchingBusy(false);
        }
    }

    private void displaySuccessAndFireSelectedEvent(ManageMeasureSearchModel.Result result, String successMessage) {
        setIsPageDirty(false);
        setSearchingBusy(false);
        resultToFireEvent = result;
        fireMeasureSelected(result);
        showConfirmationDialog(successMessage);
    }

    private void draftMeasure() {
        if (!MatContext.get().getLoadingQueue().isEmpty()) {
            return;
        }

        updateDetailsFromView();

        searchDisplay.resetMessageDisplay();
        MeasureCloningRemoteServiceAsync measureCloningService = GWT.create(MeasureCloningRemoteService.class);
        if (isValid(currentDetails, true)) {
            measureCloningService.draftExistingMeasure(currentDetails, new AsyncCallback<ManageMeasureSearchModel.Result>() {
                @Override
                public void onFailure(Throwable caught) {
                    detailDisplay.getErrorMessageDisplay().createAlert(caught.getLocalizedMessage());
                    MatContext.get().recordTransactionEvent(null, null, null, UNHANDLED_EXCEPTION + caught.getLocalizedMessage(), 0);
                }

                @Override
                public void onSuccess(ManageMeasureSearchModel.Result result) {
                    auditMeasureDraft(result);
                    displaySuccessAndFireSelectedEvent(result, MatContext.get().getMessageDelegate().getMeasureDraftSuccessfulMessage(detailDisplay.getMeasureNameTextBox().getValue()));
                }
            });
        } else {
            setSearchingBusy(false);
        }
    }

    private void createNewMeasure() {
        if (!MatContext.get().getLoadingQueue().isEmpty()) {
            return;
        }

        updateDetailsFromView();

        if (isValid(currentDetails, false)) {
            MatContext.get().getMeasureService().saveNewMeasure(currentDetails, new AsyncCallback<SaveMeasureResult>() {

                @Override
                public void onFailure(Throwable caught) {
                    detailDisplay.getErrorMessageDisplay().createAlert(caught.getLocalizedMessage());
                }

                @Override
                public void onSuccess(SaveMeasureResult result) {
                    if (result.isSuccess()) {
                        displaySuccessAndRedirectToMeasure(result.getId());
                    } else {
                        detailDisplay.getErrorMessageDisplay().createAlert(displayErrorMessage(result));
                    }
                }
            });
        } else {
            setSearchingBusy(false);
        }
    }

    private void displaySuccessAndRedirectToMeasure(String measureId) {
        final String name = currentDetails.getMeasureName();
        final String shortName = currentDetails.getShortName();
        final String scoringType = currentDetails.getMeasScoring();
        final String version = currentDetails.getVersionNumber() + "." + currentDetails.getRevisionNumber();
        final boolean isDraft = currentDetails.isDraft();
        final boolean isPatientBased = calculatePatientBased(currentDetails.isPatientBased(), scoringType);
        final String measureModel = currentDetails.getMeasureModel();

        postSaveMeasureEvents(true, measureId, detailDisplay, name, shortName, scoringType, version, isDraft, isPatientBased, measureModel);
    }

    private void displaySuccessAndRedirectToComposite(String measureId) {
        final boolean isInsert = currentCompositeMeasureDetails.getId() == null;
        final String name = currentCompositeMeasureDetails.getMeasureName();
        final String shortName = currentCompositeMeasureDetails.getShortName();
        final String scoringType = currentCompositeMeasureDetails.getMeasScoring();
        final String version = currentCompositeMeasureDetails.getVersionNumber() + "." + currentCompositeMeasureDetails.getRevisionNumber();
        final boolean isDraft = currentCompositeMeasureDetails.isDraft();
        final boolean isPatientBased = calculatePatientBased(currentCompositeMeasureDetails.isPatientBased(), scoringType);
        final String measureModel = currentCompositeMeasureDetails.getMeasureModel();

        postSaveMeasureEvents(isInsert, measureId, compositeDetailDisplay, name, shortName, scoringType, version, isDraft, isPatientBased, measureModel);
    }

    private String displayErrorMessage(SaveMeasureResult result) {
        String message = null;
        if (SaveMeasureResult.INVALID_DATA == result.getFailureReason()) {
            message = "Data Validation Failed.Please verify data.";
        } else if (SaveUpdateCQLResult.DUPLICATE_LIBRARY_NAME == result.getFailureReason()) {
            message = MessageDelegate.DUPLICATE_LIBRARY_NAME;
        } else if (SaveUpdateCQLResult.DUPLICATE_CQL_KEYWORD == result.getFailureReason()) {
            message = MatContext.get().getMessageDelegate().getLibraryNameIsCqlKeywordError();

        } else {
            message = "Unknown Code " + result.getFailureReason();
        }
        return message;
    }

    private void updateSaveButtonClickHandler(ClickHandler handler) {
        if (saveHandler != null) {
            saveHandler.removeHandler();
        }

        saveHandler = detailDisplay.getSaveButton().addClickHandler(handler);
    }

    private void updateCompositeSaveButtonClickHandler(ClickHandler handler) {
        if (compositeSaveHandler != null) {
            compositeSaveHandler.removeHandler();
        }

        compositeSaveHandler = compositeDetailDisplay.getSaveButton().addClickHandler(handler);
    }

    private void displayNewMeasureWidget() {
        warningConfirmationMessageAlert = detailDisplay.getWarningConfirmationMessageAlert();
        currentDetails = new ManageMeasureDetailModel();
        detailDisplay.showCautionMsg(false);
        displayCommonDetailForAdd(detailDisplay);
        panel.setHeading("My Measures > Create New Measure", MEASURE_LIBRARY);
        setDetailsToView();
        updateSaveButtonClickHandler(event -> createNewMeasure());
        panel.setContent(detailDisplay.asWidget());
    }


    private void displayCloneMeasureWidget() {
        warningConfirmationMessageAlert = detailDisplay.getWarningConfirmationMessageAlert();
        panel.setHeading("My Measures > Clone Measure", COMPOSITE_MEASURE);
        detailDisplay.setMeasureName(currentDetails.getMeasureName());
        detailDisplay.setMeasureModelType(currentDetails.getMeasureModel());
        Mat.focusSkipLists(MEASURE_LIBRARY);
        detailDisplay.showMeasureName(true);
        detailDisplay.showCautionMsg(true);
        setDetailsToView();

        detailDisplay.getMeasureNameTextBox().setValue("");
        detailDisplay.getECQMAbbreviatedTitleTextBox().setValue("");
        detailDisplay.getCQLLibraryNameTextBox().setValue("");
        updateSaveButtonClickHandler(event -> cloneMeasure());
        panel.setContent(detailDisplay.asWidget());
    }

    private void displayDraftMeasureWidget() {
        warningConfirmationMessageAlert = detailDisplay.getWarningConfirmationMessageAlert();
        panel.setHeading("My Measures > Draft Measure", MEASURE_LIBRARY);
        detailDisplay.setMeasureName(currentDetails.getMeasureName());
        detailDisplay.setMeasureModelType(currentDetails.getMeasureModel());
        Mat.focusSkipLists(MEASURE_LIBRARY);
        detailDisplay.showCautionMsg(true);
        detailDisplay.showMeasureName(true);
        setDetailsToView();
        updateSaveButtonClickHandler(event -> draftMeasure());
        panel.setContent(detailDisplay.asWidget());
    }

    private void displayNewCompositeMeasureWidget() {
        clearErrorMessageAlerts();
        warningConfirmationMessageAlert = compositeDetailDisplay.getWarningConfirmationMessageAlert();
        displayCommonDetailForAdd(compositeDetailDisplay);
        panel.setHeading("My Measures > Create New Composite Measure", COMPOSITE_MEASURE);
        setCompositeDetailsToView();
        Mat.focusSkipLists(COMPOSITE_MEASURE);
        updateCompositeSaveButtonClickHandler(event -> updateCompositeDetailsOnContinueButton());
        panel.setContent(compositeDetailDisplay.asWidget());
    }

    private void displayDraftCompositeMeasureWidget() {
        warningConfirmationMessageAlert = compositeDetailDisplay.getWarningConfirmationMessageAlert();
        displayCommonDetailForAdd(compositeDetailDisplay);
        panel.setHeading("My Measures > Draft Composite Measure", COMPOSITE_MEASURE);
        compositeDetailDisplay.setMeasureName(currentCompositeMeasureDetails.getMeasureName());
        compositeDetailDisplay.setMeasureModelType(currentCompositeMeasureDetails.getMeasureModel());
        compositeDetailDisplay.showCautionMsg(true);
        compositeDetailDisplay.showMeasureName(true);
        setCompositeDetailsToView();
        Mat.focusSkipLists(COMPOSITE_MEASURE);
        updateCompositeSaveButtonClickHandler(event -> draftCompositeMeasure());
        panel.setContent(compositeDetailDisplay.asWidget());
    }

    private void displayComponentDetails(String panelHeading) {
        panel.getButtonPanel().clear();
        panel.setHeading(panelHeading, "ComponentMeasure");
        panel.setContent(componentMeasureDisplay.asWidget());
        Mat.focusSkipLists("ComponentMeasure");
    }

    private void resetPatientBasedInput(DetailDisplay currentDisplay) {
        currentDisplay.getPatientBasedListBox().clear();
        currentDisplay.getPatientBasedListBox().addItem("No", "No");
        currentDisplay.getPatientBasedListBox().addItem("Yes", "Yes");
        // default the selected index to be 1, which is yes.
        currentDisplay.getPatientBasedListBox().setSelectedIndex(1);
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

        if (MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.MAT_ON_FHIR)) {
            heading = "MAT on FHIR - Measure Library";
        } else {
            heading = "MAT on QDM - Measure Library";
        }
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
        warningConfirmationMessageAlert = null;
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
            showAdminSearchingBusy(true);
            MatContext.get().getMeasureService().searchUsers(searchString, startIndex, pageSize,
                    new AsyncCallback<TransferOwnerShipModel>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
                            showAdminSearchingBusy(false);
                        }

                        @Override
                        public void onSuccess(TransferOwnerShipModel result) {
                            SearchResultUpdate sru = new SearchResultUpdate();
                            sru.update(result, (MatTextBox) transferDisplay.getSearchString(), searchString);

                            transferDisplay.buildHTMLForMeasures(transferMeasureResults);
                            transferDisplay.buildCellTable(result);
                            panel.setHeading("Measure Library Ownership >  Measure Ownership Transfer", MEASURE_LIBRARY);
                            panel.setContent(transferDisplay.asWidget());
                            showAdminSearchingBusy(false);
                            model = result;
                        }
                    });
        } else {
            searchDisplay.getErrorMessagesForTransferOS()
                    .createAlert(MatContext.get().getMessageDelegate().getTransferCheckBoxErrorMeasure());
        }

    }

    private void export(ManageMeasureSearchModel.Result result) {
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
                                          boolean isEditable, boolean isLocked, String lockedUserId, boolean isDraft, boolean isPatientBased, String measureModel) {
        MeasureSelectedEvent evt = new MeasureSelectedEvent(id, version, name, shortName, scoringType, isEditable,
                isLocked, lockedUserId, isDraft, isPatientBased, measureModel);
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
                                UNHANDLED_EXCEPTION + caught.getLocalizedMessage(), 0);
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
        boolean valid = message.isEmpty();
        if (valid) {
            detailDisplay.getErrorMessageDisplay().clearAlert();
            searchDisplay.getErrorMessageDisplayForBulkExport().clearAlert();
        } else {
            String errorMessage = message.get(0);
            detailDisplay.getErrorMessageDisplay().createAlert(errorMessage);
        }
        return valid;
    }

    private boolean isValidCompositeMeasure(ManageCompositeMeasureDetailModel compositeMeasureDetails) {
        ManageCompositeMeasureModelValidator manageCompositeMeasureModelValidator = new ManageCompositeMeasureModelValidator();
        List<String> message = manageCompositeMeasureModelValidator.validateMeasureWithClone(compositeMeasureDetails, isClone);

        if (StringUtility.isNotBlank(compositeMeasureDetails.getCQLLibraryName()) && !AbstractCQLWorkspacePresenter.isValidExpressionName(compositeMeasureDetails.getCQLLibraryName())) {
            message.add(MatContext.get().getMessageDelegate().getLibraryNameIsCqlKeywordError());
        }

        boolean valid = message.isEmpty();
        if (valid) {
            compositeDetailDisplay.getErrorMessageDisplay().clearAlert();
        } else {
            String errorMessage = message.get(0);
            compositeDetailDisplay.getErrorMessageDisplay().createAlert(errorMessage);
        }
        return valid;
    }

    private boolean isValidCompositeMeasureForSave(List<String> message) {
        boolean valid = message.isEmpty();
        componentMeasureDisplay.getSuccessMessage().clearAlert();
        if (valid) {
            componentMeasureDisplay.getErrorMessageDisplay().clearAlert();
        } else {
            String errorMessage = message.get(0);
            componentMeasureDisplay.setComponentBusy(false);
            componentMeasureDisplay.getErrorMessageDisplay().createAlert(errorMessage);
        }
        return valid;
    }


    private void displayUnusedLibraryDialog(String measureId, String measureName, boolean isMajor, String version, boolean shouldPackage) {
        ConfirmationDialogBox confirmationDialogBox = new ConfirmationDialogBox(
                MatContext.get().getMessageDelegate().getUnusedIncludedLibraryWarning(measureName), CONTINUE,
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

    private void displayDuplicateLibraryDialog() {
        ConfirmationDialogBox confirmationDialogBox = new ConfirmationDialogBox(MessageDelegate.VERSION_LIBRARY_NAME_ERROR_MESSAGE, "Return to Measure Library", "Cancel", null, true);
        confirmationDialogBox.getNoButton().setVisible(false);
        confirmationDialogBox.setObserver(new ConfirmationObserver() {

            @Override
            public void onYesButtonClicked() {
                displaySearch();
            }

            @Override
            public void onNoButtonClicked() {

            }

            @Override
            public void onClose() {

            }
        });

        confirmationDialogBox.show();
    }

    private ConfirmationDialogBox displayVersionWithoutPackageDialog(String measureId, String measureName, boolean isMajor, String version, boolean shouldPackage) {
        ConfirmationDialogBox dialogBox = new ConfirmationDialogBox(MatContext.get().getMessageDelegate().getVersionAndPackageUnsuccessfulMessage(), CONTINUE, "Cancel", null);
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
                versionDisplay.getMessagePanel().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
                MatContext.get().recordTransactionEvent(null, null, null, UNHANDLED_EXCEPTION + caught.getLocalizedMessage(), 0);
            }

            @Override
            public void onSuccess(SaveMeasureResult result) {
                setSearchingBusy(false);
                if (result.isSuccess()) {
                    versionSuccessEvent(measureId, measureName, shouldPackage, result);
                } else {
                    versionFailureEvent(result.getFailureReason(), measureId, measureName, isMajor, version, shouldPackage);
                }
            }
        });
    }

    private void versionSuccessEvent(final String measureId, final String measureName, boolean shouldPackage, SaveMeasureResult result) {
        displaySearch();
        String versionStr = result.getVersionStr();
        recordMeasureAuditEvent(measureId, versionStr);
        isMeasureVersioned = true;

        if (shouldPackage) {
            fireSuccessfulVersionAndPackageEvent(isMeasureVersioned, measureName, MatContext.get().getMessageDelegate().getVersionAndPackageSuccessfulMessage(measureName, versionStr));
        } else {
            fireSuccessfulVersionEvent(isMeasureVersioned, measureName, MatContext.get().getMessageDelegate().getVersionSuccessfulMessage(measureName, versionStr));
        }
    }

    private void versionFailureEvent(final int failureReason, final String measureId, final String measureName, final boolean isMajor, final String version,
                                     final boolean shouldPackage) {
        isMeasureVersioned = false;
        switch (failureReason) {
            case ConstantMessages.INVALID_CQL_DATA:
                versionDisplay.getMessagePanel().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getNoVersionCreated());
                break;
            case SaveMeasureResult.UNUSED_LIBRARY_FAIL:
                displayUnusedLibraryDialog(measureId, measureName, isMajor, version, shouldPackage);
                break;
            case SaveMeasureResult.PACKAGE_FAIL:
                displayVersionWithoutPackageDialog(measureId, measureName, isMajor, version, false).show();
                break;
            case SaveUpdateCQLResult.DUPLICATE_LIBRARY_NAME:
                displayDuplicateLibraryDialog();
                break;
            default:
                break;
        }
    }

    private void fireSuccessfulVersionEvent(boolean isSuccess, String name, String message) {
        MeasureVersionEvent versionEvent = new MeasureVersionEvent(isSuccess, name, message);
        MatContext.get().getEventBus().fireEvent(versionEvent);
    }

    private void fireSuccessfulVersionAndPackageEvent(boolean isSuccess, String name, String message) {
        fireSuccessfulVersionEvent(isSuccess, name, message);
    }

    private void search(final String searchText, int startIndex, int pageSize, final int filter, boolean didUserSelectSearch) {
        final String lastSearchText = (searchText != null) ? searchText.trim() : null;

        // This to fetch all Measures if user role is Admin. This will go away
        // when Pagination will be implemented in Measure Library.
        if (currentUserRole.equalsIgnoreCase(ClientConstants.ADMINISTRATOR)) {
            pageSize = 25;
            showAdminSearchingBusy(true);
            MeasureSearchModel searchAdminModel = new MeasureSearchModel(filter, startIndex, pageSize, lastSearchText);

            if (null != searchDisplay) {
                searchDisplay.getSuccessMessageDisplay().clearAlert();
            }

            MatContext.get().getMeasureService().search(searchAdminModel,
                    new AsyncCallback<ManageMeasureSearchModel>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            detailDisplay.getErrorMessageDisplay()
                                    .createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
                            MatContext.get().recordTransactionEvent(null, null, null,
                                    UNHANDLED_EXCEPTION + caught.getLocalizedMessage(), 0);
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
        searchModel.setModelType(searchDisplay.getMeasureSearchFilterWidget().getAdvancedSearchPanel().getModelTypeValue());
        searchModel.setCqlLibraryName(searchDisplay.getMeasureSearchFilterWidget().getAdvancedSearchPanel().getCqlLibraryNameByValue());
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
                                UNHANDLED_EXCEPTION + caught.getLocalizedMessage(), 0);
                        setSearchingBusy(false);
                    }

                    @Override
                    public void onSuccess(ManageMeasureSearchModel result) {
                        measureSearchModel.setTotalResults(result.getResultsTotal());
                        addSearchPills(measureSearchModel);
                        String measureListLabel = (measureSearchModel.getIsMyMeasureSearch() != 0) ? "All Measures" : "My Measures";
                        searchDisplay.getMeasureSearchView().setMeasureListLabel(measureListLabel);

                        searchDisplay.getMeasureSearchView().setObserver(createMeasureTableObserver());
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
                            }
                            if (isMeasureVersioned) {
                                searchDisplay.getSuccessMeasureDeletion().createAlert(measureVerMessage);
                            }
                        }

                        searchDisplay.buildCellTable(manageMeasureSearchModel, measureSearchModel.getIsMyMeasureSearch(), measureSearchModel);

                        setSearchingBusy(false);
                        if (didUserSelectSearch) {
                            searchDisplay.getSearchPillPanel().getBadgeHeader().getElement().focus();
                        }
                    }
                });
    }

    private Observer createMeasureTableObserver() {
        return new MeasureSearchView.Observer() {
            @Override
            public void onCloneClicked(ManageMeasureSearchModel.Result result) {
                if (result.isClonable()) {
                    setSearchingBusy(true);
                    resetMeasureFlags();
                    isClone = true;
                    MatContext.get().getMeasureService().getMeasure(result.getId(), new AsyncCallback<ManageMeasureDetailModel>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            setSearchingBusy(false);
                            detailDisplay.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
                            MatContext.get().recordTransactionEvent(null, null, null, UNHANDLED_EXCEPTION + caught.getLocalizedMessage(), 0);
                        }

                        @Override
                        public void onSuccess(ManageMeasureDetailModel result) {
                            setSearchingBusy(false);
                            currentDetails = result;
                            displayCloneMeasureWidget();
                            Mat.hideLoadingMessage();
                        }
                    });
                }
            }


            @Override
            public void onExport(ManageMeasureSearchModel.Result result) {
                resetMeasureFlags();
                export(result);
            }

            @Override
            public void onBulkExport(List<Result> exportable) {
                searchDisplay.getErrorMessageDisplayForBulkExport().clearAlert();
                isMeasureDeleted = false;
                measureDeletion = false;
                isMeasureVersioned = false;
                searchDisplay.resetMessageDisplay();
                versionDisplay.getMessagePanel().clearAlerts();

                detailDisplay.getErrorMessageDisplay().clearAlert();
                historyDisplay.getErrorMessageDisplay().clearAlert();
                shareDisplay.getErrorMessageDisplay().clearAlert();


                if (exportable.isEmpty()) {
                    searchDisplay.getErrorMessageDisplayForBulkExport()
                            .createAlert(MatContext.get().getMessageDelegate().getMeasureSelectionError());
                } else {
                    bulkExport(exportable.stream().map(i -> i.getId()).collect(Collectors.toList()));
                    searchDisplay.getMeasureSearchView().clearBulkExportCheckBoxes();
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
            public void onDraftOrVersionClick(Result object) {
                ManageMeasureSearchModel.Result selectedMeasure = object;
                if (!isLoading && selectedMeasure.isDraftable()) {
                    setSearchingBusy(true);
                    if (selectedMeasure.getIsComposite()) {
                        MatContext.get().getMeasureService().getCompositeMeasure(selectedMeasure.getId(), new AsyncCallback<ManageCompositeMeasureDetailModel>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                setSearchingBusy(false);
                                searchDisplay.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
                                MatContext.get().recordTransactionEvent(null, null, null, UNHANDLED_EXCEPTION + caught.getLocalizedMessage(), 0);
                            }

                            @Override
                            public void onSuccess(ManageCompositeMeasureDetailModel result) {
                                setSearchingBusy(false);
                                currentCompositeMeasureDetails = result;
                                displayDraftCompositeMeasureWidget();
                            }
                        });
                    } else {
                        MatContext.get().getMeasureService().getMeasure(selectedMeasure.getId(), new AsyncCallback<ManageMeasureDetailModel>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                setSearchingBusy(false);
                                searchDisplay.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
                                MatContext.get().recordTransactionEvent(null, null, null, UNHANDLED_EXCEPTION + caught.getLocalizedMessage(), 0);
                            }

                            @Override
                            public void onSuccess(ManageMeasureDetailModel result) {
                                setSearchingBusy(false);
                                currentDetails = result;
                                displayDraftMeasureWidget();
                            }
                        });
                    }
                } else if (!isLoading && selectedMeasure.isVersionable()) {
                    versionDisplay.setSelectedMeasure(selectedMeasure);
                    createVersion();
                }
            }

            @Override
            public void onFhirValidationClicked(ManageMeasureSearchModel.Result result) {
                showFhirValidationReport(result.getId());
            }

            @Override
            public void onConvertMeasureFhir(Result object) {
                if (object.isConvertedToFhir()) {
                    confirmAndConvertFhir(object);
                } else {
                    convertMeasureFhir(object);
                }
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

    private void onNewMeasureButtonClick() {
        searchDisplay.resetMessageDisplay();
        measureDeletion = false;
        isMeasureDeleted = false;
        isMeasureVersioned = false;
        displayNewMeasureWidget();
    }

    private void onNewCompositeMeasureButtonClick() {
        searchDisplay.resetMessageDisplay();
        measureDeletion = false;
        isMeasureDeleted = false;
        isMeasureVersioned = false;
        currentCompositeMeasureDetails = new ManageCompositeMeasureDetailModel();
        displayNewCompositeMeasureWidget();
    }

    private void addMeasureLibrarySearchHandlers(final SearchDisplay searchDisplay) {
        searchDisplay.getDraftConfirmationDialogBox().getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                fireMeasureSelected(resultToFireEvent);
                auditMeasureDraft(resultToFireEvent);
                resultToFireEvent = new ManageMeasureSearchModel.Result();
            }
        });

        searchDisplay.getSelectIdForEditTool().addSelectionHandler(new SelectionHandler<ManageMeasureSearchModel.Result>() {

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
                    final boolean isPatientBased = calculatePatientBased(result.isPatientBased(), scoringType);
                    final String measureModel = result.getMeasureModel();

                    MatContext.get().getMeasureLockService().isMeasureLocked(mid);
                    Command waitForLockCheck = new Command() {
                        @Override
                        public void execute() {
                            SynchronizationDelegate synchDel = MatContext.get().getSynchronizationDelegate();
                            if (!synchDel.isCheckingLock()) {
                                if (!synchDel.measureIsLocked()) {
                                    fireMeasureSelectedEvent(mid, version, name, shortName, scoringType,
                                            isEditable, isMeasureLocked, userId, isDraft, isPatientBased, measureModel);
                                    if (isEditable) {
                                        MatContext.get().getMeasureLockService().setMeasureLock();
                                    }
                                } else {
                                    fireMeasureSelectedEvent(mid, version, name, shortName, scoringType, false,
                                            isMeasureLocked, userId, isDraft, isPatientBased, measureModel);
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

        searchDisplay.getMostRecentMeasureWidget().getSelectIdForEditTool().addSelectionHandler(new SelectionHandler<ManageMeasureSearchModel.Result>() {
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
                    final boolean isPatientBased = calculatePatientBased(result.isPatientBased(), scoringType);
                    final String measureModel = result.getMeasureModel();
                    MatContext.get().getMeasureLockService().isMeasureLocked(mid);
                    Command waitForLockCheck = new Command() {
                        @Override
                        public void execute() {
                            SynchronizationDelegate synchDel = MatContext.get().getSynchronizationDelegate();
                            if (!synchDel.isCheckingLock()) {
                                if (!synchDel.measureIsLocked()) {
                                    fireMeasureSelectedEvent(mid, version, name, shortName, scoringType,
                                            isEditable, isMeasureLocked, userId, isDraft, isPatientBased, measureModel);
                                    if (isEditable) {
                                        MatContext.get().getMeasureLockService().setMeasureLock();
                                    }
                                } else {
                                    fireMeasureSelectedEvent(mid, version, name, shortName, scoringType, false,
                                            isMeasureLocked, userId, isDraft, isPatientBased, measureModel);
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

        searchDisplay.getCreateMeasureButton().addClickHandler(event -> onNewMeasureButtonClick());

        searchDisplay.getCreateCompositeMeasureButton().addClickHandler(event -> onNewCompositeMeasureButtonClick());

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

        FormPanel form = searchDisplay.getForm();
        form.addSubmitCompleteHandler(event -> {
            String errorMsg = event.getResults();
            if ((null != errorMsg) && errorMsg.contains("Exceeded Limit")) {
                List<String> err = new ArrayList<>();
                err.add("Export file size is " + errorMsg);
                err.add("File size limit is 100 MB");
                searchDisplay.getErrorMessageDisplayForBulkExport().createAlert(err);
            } else {
                Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
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
        searchWidget.addKeyUpHandler(event -> {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                ((Button) searchDisplay.getAdminSearchButton()).click();
            }
        });

        searchDisplay.getAdminSearchButton().addClickHandler(event -> {
            int startIndex = 1;
            searchDisplay.getErrorMessageDisplay().clearAlert();
            int filter = 1;
            search(searchDisplay.getAdminSearchString().getValue(), startIndex, Integer.MAX_VALUE, filter, false);
        });

        searchDisplay.getTransferButton().addClickHandler(event -> displayActiveMATUsersToTransferMeasureOwnership());

        searchDisplay.getClearButton().addClickHandler(event -> clearAllMeasureOwnershipTransfers());

        searchDisplay.getMeasureSearchView().getClearSelectedRowsAnchor().addClickHandler(event -> onClearSelectedMeasures());
    }

    protected void onClearSelectedMeasures() {
        searchDisplay.getMeasureSearchView().clearSelectedMeasures();
    }

    protected boolean calculatePatientBased(Boolean patientBased, String scoringType) {
        return patientBased == null ? !scoringType.equals(ConstantMessages.CONTINUOUS_VARIABLE_SCORING) : patientBased;
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
                                UNHANDLED_EXCEPTION + caught.getLocalizedMessage(), 0);
                    }

                    @Override
                    public void onSuccess(SearchHistoryDTO data) {
                        historyDisplay.buildCellTable(data.getLogs());
                    }
                });
    }

    /**
     * Method to Load most recent Used Measures for Logged In User.
     */
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
        compositeDetailDisplay.clearFields();
        resetPatientBasedInput(compositeDetailDisplay);
        compositeDetailDisplay.getMeasureNameTextBox().setValue(currentCompositeMeasureDetails.getMeasureName());
        compositeDetailDisplay.getECQMAbbreviatedTitleTextBox().setValue(currentCompositeMeasureDetails.getShortName());
        compositeDetailDisplay.getCQLLibraryNameTextBox().setValue(currentCompositeMeasureDetails.getCQLLibraryName());
        ((NewCompositeMeasureView) compositeDetailDisplay).setCompositeScoringSelectedValue(currentCompositeMeasureDetails.getCompositeScoringMethod());
        compositeDetailDisplay.getMeasureScoringListBox().setValueMetadata(currentCompositeMeasureDetails.getMeasScoring());

        if (!StringUtility.isEmptyOrNull(currentCompositeMeasureDetails.getMeasureName())) {
            if (currentCompositeMeasureDetails.isPatientBased()) {
                compositeDetailDisplay.getPatientBasedListBox().setSelectedIndex(1);
            } else {
                compositeDetailDisplay.getPatientBasedListBox().setSelectedIndex(0);
            }

            if (currentCompositeMeasureDetails.getMeasScoring().equalsIgnoreCase(MatConstants.CONTINUOUS_VARIABLE)) {
                compositeDetailDisplay.getPatientBasedListBox().removeItem(1);
                compositeDetailDisplay.getPatientBasedListBox().setSelectedIndex(0);
            }
        }
    }

    private void setDetailsToView() {
        panel.getButtonPanel().clear();
        detailDisplay.clearFields();
        resetPatientBasedInput(detailDisplay);
        detailDisplay.getMeasureNameTextBox().setValue(currentDetails.getMeasureName());
        detailDisplay.getCQLLibraryNameTextBox().setValue(currentDetails.getCQLLibraryName());
        detailDisplay.getECQMAbbreviatedTitleTextBox().setValue(currentDetails.getShortName());
        detailDisplay.getMeasureScoringListBox().setValueMetadata(currentDetails.getMeasScoring());

        if (!StringUtility.isEmptyOrNull(currentDetails.getMeasureName())) {
            if (currentDetails.isPatientBased()) {
                detailDisplay.getPatientBasedListBox().setSelectedIndex(1);
            } else {
                detailDisplay.getPatientBasedListBox().setSelectedIndex(0);
            }

            if (currentDetails.getMeasScoring().equalsIgnoreCase(MatConstants.CONTINUOUS_VARIABLE)) {
                detailDisplay.getPatientBasedListBox().removeItem(1);
                detailDisplay.getPatientBasedListBox().setSelectedIndex(0);
            }
        }
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
                                UNHANDLED_EXCEPTION + caught.getLocalizedMessage(), 0);
                    }

                    @Override
                    public void onSuccess(Void result) {
                        searchDisplay.resetMessageDisplay();
                        measureShared = currentShareDetails.getData().stream().anyMatch(sd -> (sd.getShareLevel() != null && !(sd.getShareLevel().equals(""))));
                        if (measureShared) {
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
                            public void onFailure(Throwable caught) {
                            }

                            @Override
                            public void onSuccess(Void result) {
                            }
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

    /**
     * Update details from view.
     */
    private void updateDetailsFromView() {
        currentDetails.setMeasureName(detailDisplay.getMeasureNameTextBox().getValue().trim());
        currentDetails.setShortName(detailDisplay.getECQMAbbreviatedTitleTextBox().getValue().trim());
        currentDetails.setMeasureModel(detailDisplay.getMeasureModelType());
        currentDetails.setCQLLibraryName(detailDisplay.getCQLLibraryNameTextBox().getValue().trim());
        String measureScoring = detailDisplay.getMeasureScoringValue();

        currentDetails.setMeasScoring(measureScoring);

        if (detailDisplay.getPatientBasedListBox().getItemText(detailDisplay.getPatientBasedListBox().getSelectedIndex()).equalsIgnoreCase("Yes")) {
            currentDetails.setIsPatientBased(true);
        } else {
            currentDetails.setIsPatientBased(false);
        }

        currentDetails.scrubForMarkUp();
        detailDisplay.getMeasureNameTextBox().setValue(currentDetails.getMeasureName());
        detailDisplay.getECQMAbbreviatedTitleTextBox().setValue(currentDetails.getShortName());
        MatContext.get().setCurrentMeasureName(currentDetails.getMeasureName());
        MatContext.get().setCurrentShortName(currentDetails.getShortName());
        MatContext.get().setCurrentMeasureScoringType(currentDetails.getMeasScoring());
    }

    private void updateCompositeDetailsFromCompositeDetailView() {
        currentCompositeMeasureDetails.setMeasureName(compositeDetailDisplay.getMeasureNameTextBox().getValue().trim());
        currentCompositeMeasureDetails.setCQLLibraryName(compositeDetailDisplay.getCQLLibraryNameTextBox().getValue().trim());
        currentCompositeMeasureDetails.setShortName(compositeDetailDisplay.getECQMAbbreviatedTitleTextBox().getValue().trim());
        currentCompositeMeasureDetails.setCompositeScoringMethod(((NewCompositeMeasureView) compositeDetailDisplay).getCompositeScoringValue());
        String measureScoring = compositeDetailDisplay.getMeasureScoringValue();
        currentCompositeMeasureDetails.setMeasScoring(measureScoring);
        if (compositeDetailDisplay.getPatientBasedListBox().getItemText(compositeDetailDisplay.getPatientBasedListBox().getSelectedIndex()).equalsIgnoreCase("Yes")) {
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

    private void postSaveMeasureEvents(boolean isInsert, String measureId, DetailDisplay detailDisplay,
                                       String name, String shortName, String scoringType, String version, boolean isDraft, boolean isPatientBased, String measureModel) {


        setIsPageDirty(false);
        if (isInsert) {
            fireMeasureSelectedEvent(measureId, version, name, shortName, scoringType, true, false, null, isDraft, isPatientBased, measureModel);
            fireMeasureEditEvent();
            showConfirmationDialog(MatContext.get().getMessageDelegate().getCreateNewMeasureSuccessfulMessage(detailDisplay.getMeasureNameTextBox().getValue()));
        } else {
            displaySearch();
        }
    }

    private void createNewCompositeMeasure() {
        updateCompositeDetailsFromComponentMeasureDisplay();
        updateCompositeDetailsFromCompositeDetailView();
        MatContext.get().getMeasureService().saveCompositeMeasure(currentCompositeMeasureDetails, new AsyncCallback<SaveMeasureResult>() {

            @Override
            public void onFailure(Throwable caught) {
                componentMeasureDisplay.getErrorMessageDisplay().createAlert(caught.getLocalizedMessage());
                componentMeasureDisplay.setComponentBusy(false);
            }

            @Override
            public void onSuccess(SaveMeasureResult result) {
                if (result.isSuccess()) {
                    displaySuccessAndRedirectToComposite(result.getId());

                } else {
                    componentMeasureDisplay.getErrorMessageDisplay().createAlert(displayErrorMessage(result));
                }

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

        versionDisplay.getSaveButton().addClickHandler(event -> onPackageAndVersionButtonClick());

        versionDisplay.getCancelButton().addClickHandler(cancelClickHandler);
    }

    private void onPackageAndVersionButtonClick() {

        if (!isLoading) {
            isMeasureDeleted = false;
            measureDeletion = false;
            ManageMeasureSearchModel.Result selectedMeasure = versionDisplay.getSelectedMeasure();
            versionDisplay.getMessagePanel().clearAlerts();
            if (((selectedMeasure != null) && (selectedMeasure.getId() != null))
                    && (versionDisplay.getMajorRadioButton().getValue()
                    || versionDisplay.getMinorRadioButton().getValue())) {

                boolean shouldPackage = true;
                boolean ignoreUnusedIncludedLibraries = false;
                saveFinalizedVersion(selectedMeasure.getId(), selectedMeasure.getName(), versionDisplay.getMajorRadioButton().getValue(), selectedMeasure.getVersion(), shouldPackage, ignoreUnusedIncludedLibraries);
            } else {
                versionDisplay.getMessagePanel().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_LIBRARY_VERSION());
            }
        }
    }

    private void resetMeasureFlags() {
        setIsPageDirty(false);
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

    public boolean isDirty() {
        return isDirty;
    }

    public void setTabTargets(MatTabLayoutPanel targetTabLayout, MatPresenter sourcePresenter, MatPresenter targetPresenter) {
        this.targetPresenter = targetPresenter;
        this.targetTabLayout = targetTabLayout;
        this.sourcePresenter = sourcePresenter;
    }

    @Override
    public boolean isValid() {
        return !isDirty();
    }

    @Override
    public void showUnsavedChangesError() {
        detailDisplay.getErrorMessageDisplay().clearAlert();
        compositeDetailDisplay.getErrorMessageDisplay().clearAlert();
        warningConfirmationMessageAlert.createAlert();
        warningConfirmationMessageAlert.getWarningConfirmationYesButton().setFocus(true);
        handleClickEventsOnUnsavedChangesMsg(warningConfirmationMessageAlert);
    }

    private void removeHandlers() {
        if (yesHandler != null) {
            yesHandler.removeHandler();
        }
        if (noHandler != null) {
            noHandler.removeHandler();
        }
    }

    private void handleClickEventsOnUnsavedChangesMsg(final WarningConfirmationMessageAlert saveErrorMessage) {
        removeHandlers();
        yesHandler = saveErrorMessage.getWarningConfirmationYesButton().addClickHandler(event -> onYesButtonClicked(saveErrorMessage));
        noHandler = saveErrorMessage.getWarningConfirmationNoButton().addClickHandler(event -> onNoButtonClicked(saveErrorMessage));
    }

    private void onNoButtonClicked(WarningConfirmationMessageAlert saveErrorMessage) {
        saveErrorMessage.clearAlert();
        resetTabTargets();
    }

    private void onYesButtonClicked(WarningConfirmationMessageAlert saveErrorMessage) {
        saveErrorMessage.clearAlert();
        notifyCurrentTabOfClosing();
        targetTabLayout.setIndexFromTargetSelection();
        MatContext.get().setAriaHidden(targetPresenter.getWidget(), "false");
        targetPresenter.beforeDisplay();
        resetTabTargets();
    }

    private void resetTabTargets() {
        targetPresenter = null;
        targetTabLayout = null;
        sourcePresenter = null;
    }

    @Override
    public void updateOnBeforeSelection() {
    }

    @Override
    public void notifyCurrentTabOfClosing() {
        if (sourcePresenter != null) {
            MatContext.get().setAriaHidden(sourcePresenter.getWidget(), "true");
            sourcePresenter.beforeClosingDisplay();
        }
    }
}
