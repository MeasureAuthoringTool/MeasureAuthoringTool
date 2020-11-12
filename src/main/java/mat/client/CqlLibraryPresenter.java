package mat.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.advancedsearch.AdvancedSearchPillPanel;
import mat.client.buttons.CustomButton;
import mat.client.cql.CQLLibraryHistoryView;
import mat.client.cql.CQLLibrarySearchView;
import mat.client.cql.CQLLibrarySearchView.Observer;
import mat.client.cql.CQLLibraryShareView;
import mat.client.cql.CQLLibraryVersionView;
import mat.client.cql.NewLibraryView;
import mat.client.cqlworkspace.EditConfirmationDialogBox;
import mat.client.event.CQLLibraryDeleteEvent;
import mat.client.event.CQLLibraryEditEvent;
import mat.client.event.CQLLibrarySelectedEvent;
import mat.client.event.CQLVersionEvent;
import mat.client.event.UmlsActivatedEvent;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.measure.service.CheckForConversionResult;
import mat.client.measure.service.FhirConvertResultResponse;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.shared.ConfirmationObserver;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.FocusableWidget;
import mat.client.shared.MatContext;
import mat.client.shared.MatTabLayoutPanel;
import mat.client.shared.MessageAlert;
import mat.client.shared.MessageDelegate;
import mat.client.shared.MostRecentCQLLibraryWidget;
import mat.client.shared.SearchWidgetBootStrap;
import mat.client.shared.SearchWidgetWithFilter;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SynchronizationDelegate;
import mat.client.shared.WarningConfirmationMessageAlert;
import mat.client.shared.ui.DeleteConfirmDialogBox;
import mat.client.util.ClientConstants;
import mat.client.util.FeatureFlagConstant;
import mat.dto.AuditLogDTO;
import mat.dto.SearchHistoryDTO;
import mat.model.clause.ModelTypeHelper;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLLibraryShareDTO;
import mat.shared.CQLModelValidator;
import mat.shared.ConstantMessages;
import mat.shared.LibrarySearchModel;
import mat.shared.SaveUpdateCQLResult;
import mat.shared.error.AuthenticationException;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.constants.ButtonDismiss;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * The Class CqlLibraryPresenter.
 */
public class CqlLibraryPresenter implements MatPresenter, TabObserver {
    private ContentWithHeadingWidget panel = new ContentWithHeadingWidget();

    private static FocusableWidget subSkipContentHolder;

    private ViewDisplay cqlLibraryView;

    private DetailDisplay detailDisplay;

    private VersionDisplay versionDisplay;

    private ShareDisplay shareDisplay;

    private boolean isLoading = false;

    private CQLModelValidator validator = new CQLModelValidator();

    private CQLLibraryDataSetObject cqlSharedDataSetObject;

    private SaveCQLLibraryResult saveCQLLibraryResult;

    private CQLLibraryHistoryView historyDisplay;

    private boolean cqlLibraryDeletion = false;

    private String cqlLibraryDelMessage;

    private String cqlLibraryVerMessage;

    private boolean isCqlLibraryDeleted = false;

    private boolean isCqlLibraryVersioned = false;

    private SaveCQLLibraryResult resultToFireEvent;

    private boolean cqlLibraryShared = false;

    private String cqlLibraryShareMessage;

    private DeleteConfirmDialogBox dialogBox;

    private HandlerRegistration saveHandler;

    private MatPresenter targetPresenter;

    private MatTabLayoutPanel targetTabLayout;

    private MatPresenter sourcePresenter;

    private WarningConfirmationMessageAlert warningConfirmationMessageAlert;

    private boolean isDirty;

    private HandlerRegistration yesHandler;

    private HandlerRegistration noHandler;

    private static final String CONTINUE = "Continue";

    private static final String CQL_LIBRARY = "CQLLibrary";

    private final Logger logger = Logger.getLogger("MAT");

    private static final String UNHANDLED_EXCEPTION = "Unhandled Exception: ";

    /**
     * The Interface ViewDisplay.
     */
    public interface ViewDisplay {

        /**
         * Top Main panel of CQL Workspace Tab.
         *
         * @return HorizontalPanel
         */
        FlowPanel getMainPanel();

        /**
         * Generates View for CQLWorkSpace tab.
         */
        void buildDefaultView();

        /**
         * Gets the adds the new folder button.
         *
         * @return the adds the new folder button
         */
        CustomButton getAddNewFolderButton();

        /**
         * Gets the zoom button.
         *
         * @return the zoom button
         */
        CustomButton getZoomButton();

        /**
         * As widget.
         *
         * @return the widget
         */
        Widget asWidget();

        void buildCellTable(SaveCQLLibraryResult searchModel, LibrarySearchModel model, int filter);

        /**
         * Gets the CQL library search view.
         *
         * @return the CQL library search view
         */
        CQLLibrarySearchView getCQLLibrarySearchView();

        /**
         * Gets the error message alert.
         *
         * @return the error message alert
         */
        MessageAlert getErrorMessageAlert();

        /**
         * Builds the create new view.
         */
        void buildCreateNewView();

        /**
         * Gets the select id for edit tool.
         *
         * @return the select id for edit tool
         */
        HasSelectionHandlers<CQLLibraryDataSetObject> getSelectIdForEditTool();

        /**
         * Gets the widget VP.
         *
         * @return the widget VP
         */
        VerticalPanel getWidgetVP();

        /**
         * Gets the search filter widget.
         *
         * @return the search filter widget
         */
        SearchWidgetWithFilter getSearchFilterWidget();

        /**
         * Gets the selected filter.
         *
         * @return the selected filter
         */
        int getSelectedFilter();

        /**
         * Gets the search string.
         *
         * @return the search string
         */
        HasValue<String> getSearchString();

        /**
         * Gets the search button.
         *
         * @return the search button
         */
        HasClickHandlers getSearchButton();

        /**
         * Gets the most recent library widget.
         *
         * @return the most recent library widget
         */
        MostRecentCQLLibraryWidget getMostRecentLibraryWidget();

        /**
         * Builds the most recent widget.
         */
        void buildMostRecentWidget();

        VerticalPanel getCellTablePanel();

        Button getCreateNewLibraryButton();

        MessageAlert getSuccessMessageAlert();

        EditConfirmationDialogBox getDraftConfirmationDialogBox();

        void resetMessageDisplay();

        CustomCheckBox getCustomFilterCheckBox();

        AdvancedSearchPillPanel getSearchPillPanel();

        void resetSearchDisplay();

        VerticalPanel getMostRecentLibraryVerticalPanel();

    }

    public interface VersionDisplay {

        void buildDataTable(SaveCQLLibraryResult result);

        Widget asWidget();

        RadioButton getMajorRadioButton();

        RadioButton getMinorRadio();

        ErrorMessageAlert getErrorMessages();

        Button getSaveButton();

        Button getCancelButton();

        CQLLibraryDataSetObject getSelectedLibrary();

        void clearRadioButtonSelection();

        void setSelectedLibraryObject(CQLLibraryDataSetObject selectedLibraryObject);

        ConfirmationDialogBox createConfirmationDialogBox(String messageText, String yesButtonText, String noButtonText,
                                                          ConfirmationObserver observer, boolean isError);
    }


    /**
     * The Interface DetailDisplay.
     */
    public interface DetailDisplay {
        /**
         * Gets the name.
         *
         * @return the name
         */
        public HasValue<String> getName();

        public String getLibraryModelType();

        public void setLibraryModelType(String name, boolean isDraft);

        /**
         * Gets the save button.
         *
         * @return the save button
         */
        public HasClickHandlers getSaveButton();

        /**
         * Gets the cancel button.
         *
         * @return the cancel button
         */
        public HasClickHandlers getCancelButton();

        /**
         * Reset all.
         */
        public void resetAll();

        /**
         * As widget.
         *
         * @return the widget
         */
        Widget asWidget();

        /**
         * Gets the name field.
         *
         * @return the name field
         */
        TextArea getNameField();

        /**
         * Gets the error message.
         *
         * @return the error message
         */
        ErrorMessageAlert getErrorMessage();

        EditConfirmationDialogBox getCreateNewConfirmationDialogBox();

        WarningConfirmationMessageAlert getWarningConfirmationAlert();
    }


    /**
     * The Interface ShareDisplay.
     */
    public interface ShareDisplay {

        /**
         * As widget.
         *
         * @return the widget
         */
        Widget asWidget();

        /**
         * Builds the CQL library share table.
         *
         * @param data the data
         */
        void buildCQLLibraryShareTable(List<CQLLibraryShareDTO> data);

        /**
         * Gets the cancel button.
         *
         * @return the cancel button
         */
        HasClickHandlers getCancelButton();

        /**
         * Gets the error message display.
         *
         * @return the error message display
         */
        MessageAlert getErrorMessageDisplay();

        /**
         * Gets the success message display.
         *
         * @return the success message display
         */
        MessageAlert getSuccessMessageDisplay();

        /**
         * Gets the success message display.
         *
         * @return the success message display
         */
        MessageAlert getWarningMessageDisplay();

        /**
         * Gets the save button.
         *
         * @return the save button
         */
        HasClickHandlers getSaveButton();

        /**
         * Sets the CQ library name.
         *
         * @param name the new CQ library name
         */
        void setCQLibraryName(String name);

        /**
         * Gets the search button.
         *
         * @return the search button
         */
        SearchWidgetBootStrap getSearchWidgetBootStrap();

        /**
         * Gets the focus panel.
         *
         * @return the focus panel
         */
        FocusPanel getSearchWidgetFocusPanel();

        void resetMessageDisplay();
    }

    /**
     * The Interface HistoryDisplay.
     */
    public interface HistoryDisplay {

        /**
         * Builds the cell table.
         *
         * @param results the results
         */
        void buildCellTable(List<AuditLogDTO> results);

        /**
         * Sets the CQL library name.
         *
         * @param name the new CQL library name
         */
        void setCQLLibraryName(String name);

        /**
         * Sets the CQL library id.
         *
         * @param id the new CQL library id
         */
        void setCQLLibraryId(String id);

        /**
         * Gets the CQL library id.
         *
         * @return the CQL library id
         */
        String getCQLLibraryId();

        /**
         * Gets the CQL library name.
         *
         * @return the CQL library name
         */
        String getCQLLibraryName();

        /**
         * Gets the return to link.
         *
         * @return the return to link
         */
        HasClickHandlers getReturnToLink();

        /**
         * Sets the return to link text.
         *
         * @param s the new return to link text
         */
        void setReturnToLinkText(String s);

    }

    public CqlLibraryPresenter(CqlLibraryView cqlLibraryView, NewLibraryView detailDisplay,
                               CQLLibraryVersionView versionDisplay, CQLLibraryShareView shareDisplay, CQLLibraryHistoryView historyDisplay) {
        this.cqlLibraryView = cqlLibraryView;
        this.detailDisplay = detailDisplay;
        this.versionDisplay = versionDisplay;

        this.shareDisplay = shareDisplay;
        this.historyDisplay = historyDisplay;
        addCQLLibraryViewHandlers();
        addDetailDisplayViewHandlers();
        addCQLLibrarySelectionHandlers();
        addMostRecentWidgetSelectionHandler();
        addVersionDisplayViewHandlers();

        addShareDisplayViewHandlers();
        addHistoryDisplayHandlers();
        addObserverHandlers();
        addDeleteEventHandler();

        MatContext.get().getEventBus().addHandler(UmlsActivatedEvent.TYPE, event -> cqlLibraryView.resetMessageDisplay());

    }

    private void addDeleteEventHandler() {

        MatContext.get().getEventBus().addHandler(CQLLibraryDeleteEvent.TYPE, new CQLLibraryDeleteEvent.Handler() {

            @Override
            public void onDeletion(CQLLibraryDeleteEvent event) {
                displaySearch();
                isCqlLibraryVersioned = false;
                cqlLibraryDelMessage = event.getMessage();
                cqlLibraryDeletion = true;
                cqlLibraryShared = false;
                if (event.isDeleted()) {
                    isCqlLibraryDeleted = true;
                    System.out.println("Event - is Deleted : " + isCqlLibraryDeleted + cqlLibraryDeletion);
                    System.out.println("Event - message : " + cqlLibraryDelMessage);
                } else {
                    isCqlLibraryDeleted = false;
                    System.out.println("Event - is NOT Deleted : " + isCqlLibraryDeleted + cqlLibraryDeletion);
                    System.out.println("Event - message : " + cqlLibraryDelMessage);
                }
            }
        });

    }

    /**
     * Adds the history display handlers.
     */
    private void addHistoryDisplayHandlers() {
        historyDisplay.getReturnToLink().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                displaySearch();

            }
        });
    }

    /**
     * Adds the observer handlers.
     */
    private void addObserverHandlers() {
        cqlLibraryView.getCQLLibrarySearchView().setTableObserver(createCQLLibraryObserver());
    }

    private Observer createCQLLibraryObserver() {
        return new CQLLibrarySearchView.Observer() {

            @Override
            public void onShareClicked(CQLLibraryDataSetObject result) {
                cqlSharedDataSetObject = result;
                cqlLibraryDeletion = false;
                cqlLibraryShared = false;
                isCqlLibraryDeleted = false;
                isCqlLibraryVersioned = false;
                displayShare();
            }

            @Override
            public void onHistoryClicked(CQLLibraryDataSetObject result) {
                historyDisplay
                        .setReturnToLinkText("<< Return to CQL Library");
                cqlLibraryDeletion = false;
                cqlLibraryShared = false;
                isCqlLibraryDeleted = false;
                isCqlLibraryVersioned = false;
                displayHistoryWidget(
                        result.getId(),
                        result.getCqlName());
            }

            @Override
            public void onDraftOrVersionClick(CQLLibraryDataSetObject object) {
                if (!isLoading && object.isDraftable()) {
                    CQLLibraryDataSetObject selectedLibrary = object;
                    if (((selectedLibrary != null) && (selectedLibrary.getId() != null))) {
                        displayDraftCQLLibraryWidget(selectedLibrary);
                    }
                } else if (!isLoading && object.isVersionable()) {
                    versionDisplay.setSelectedLibraryObject(object);
                    displayVersionWidget();
                }
            }

            @Override
            public void onDeleteClicked(CQLLibraryDataSetObject object) {
                final String cqlLibraryId = object.getId();
                dialogBox = new DeleteConfirmDialogBox();
                dialogBox.showDeletionConfimationDialog(MatContext.get().getMessageDelegate().getWARNING_DELETION_CQL_LIBRARY());
                dialogBox.getConfirmButton().addClickHandler(event -> deleteCQLLibrary(cqlLibraryId));
            }

            @Override
            public void onConvertClicked(CQLLibraryDataSetObject object) {
                showSearchingBusy(true);
                MatContext.get().getCQLLibraryService().checkLibraryForConversion(object, new AsyncCallback<CheckForConversionResult>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        logger.log(Level.SEVERE, "Error while checking a draft for the library set " + object.getCqlSetId() + ". Error message: " + caught.getMessage(), caught);
                        showErrorAlertDialogBox(MatContext.get().getMessageDelegate().getGenericErrorMessage(), false);
                        MatContext.get().recordTransactionEvent(null, null, null, UNHANDLED_EXCEPTION + caught.getLocalizedMessage(), 0);
                    }

                    @Override
                    public void onSuccess(CheckForConversionResult result) {
                        logger.log(Level.WARNING, "Result is " + result);
                        if (result.isProceedImmediately()) {
                            convertCqlLibraryFhir(object);
                        } else if (result.isConfirmBeforeProceed()) {
                            confirmAndConvertFhir(object);
                        } else {
                            showErrorAlertDialogBox(MatContext.get().getMessageDelegate().getConversionBlockedWithDraftsErrorMessageForCqlLibrary(), false);
                        }
                    }
                });
            }


            @Override
            public void onEditClicked(CQLLibraryDataSetObject result) {
                cqlSharedDataSetObject = result;
                cqlLibraryDeletion = false;
                cqlLibraryShared = false;
                isCqlLibraryDeleted = false;
                isCqlLibraryVersioned = false;
                cqlLibraryView.getErrorMessageAlert().clearAlert();
                cqlLibraryView.getSuccessMessageAlert().clearAlert();
                displayEdit(result);

            }

        };
    }

    private void confirmAndConvertFhir(CQLLibraryDataSetObject object) {
        ConfirmationDialogBox confirmationDialogBox = new ConfirmationDialogBox("Are you sure you want to convert this Cql Library again? The existing FHIR Library will be overwritten.", "Yes", "No", null, false);
        confirmationDialogBox.getNoButton().setVisible(true);
        confirmationDialogBox.setObserver(new ConfirmationObserver() {

            @Override
            public void onYesButtonClicked() {
                convertCqlLibraryFhir(object);
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

        showSearchingBusy(false);
        confirmationDialogBox.show();
    }

    private boolean showAlertAndReturnIfNotUMLSLoggedIn() {
        if (!MatContext.get().isUMLSLoggedIn()) {
            cqlLibraryView.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN());
            cqlLibraryView.getErrorMessageAlert().setVisible(true);
            showSearchingBusy(false);
            return true;
        } else {
            cqlLibraryView.getErrorMessageAlert().clearAlert();
            cqlLibraryView.resetMessageDisplay();
        }
        return false;
    }

    private void convertCqlLibraryFhir(CQLLibraryDataSetObject object) {
        if (showAlertAndReturnIfNotUMLSLoggedIn()) {
            logger.log(Level.WARNING, "User is not logged in UMSL");
            return;
        }


        logger.log(Level.INFO, "Please wait. Conversion is in progress...");

        showSearchingBusy(true);
        MatContext.get().getCQLLibraryService().convertCqlLibrary(object, new AsyncCallback<FhirConvertResultResponse>() {
            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error while converting the library id " + object.getId() + ". Error message: " + caught.getMessage(), caught);
                showErrorAlertDialogBox(MatContext.get().getMessageDelegate().getConvertCqlLibraryFailureMessage());
                MatContext.get().recordTransactionEvent(null, null, null, UNHANDLED_EXCEPTION + caught.getLocalizedMessage(), 0);
            }

            @Override
            public void onSuccess(FhirConvertResultResponse response) {
                String outcome = response.getValidationStatus().getOutcome();
                String errorReason = response.getValidationStatus().getErrorReason();
                logger.log(Level.WARNING, "Library " + object.getId() + " conversion has completed. Outcome: " + outcome + " errorReason: " + errorReason);
                showSearchingBusy(false);
                showFhirValidationReport(response.getFhirMeasureId(), true);
                displaySearch();
            }
        });
    }

    private void showFhirValidationReport(String libraryId, boolean converted) {
        StringBuilder url = new StringBuilder(GWT.getModuleBaseURL()).append("validationReport");
        url.append("?id=").append(SafeHtmlUtils.htmlEscape(libraryId));
        if (converted) {
            url.append("&converted=true");
        }
        url.append("&type=library");
        Window.open(url.toString(), "_blank", "");
    }

    private void showErrorAlertDialogBox(final String errorMessage) {
        showErrorAlertDialogBox(errorMessage, true);
    }

    private void showErrorAlertDialogBox(final String errorMessage, final boolean shouldRefreshSearch) {
        showSearchingBusy(false);
        ConfirmationDialogBox errorAlert = new ConfirmationDialogBox(errorMessage, "Return to CqlLibrary Library", "Cancel", null, true);
        errorAlert.getNoButton().setVisible(false);
        errorAlert.setObserver(new ConfirmationObserver() {

            @Override
            public void onYesButtonClicked() {
                refresh();
            }

            @Override
            public void onNoButtonClicked() {
            }

            @Override
            public void onClose() {
                refresh();
            }

            private void refresh() {
                if (shouldRefreshSearch) {
                    displaySearch();
                }
            }
        });
        errorAlert.show();
    }

    private void addShareDisplayViewHandlers() {

        shareDisplay.getSaveButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                MatContext.get().getCQLLibraryService().updateUsersShare(saveCQLLibraryResult, new AsyncCallback<Void>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        shareDisplay
                                .getErrorMessageDisplay()
                                .createAlert(
                                        MatContext
                                                .get()
                                                .getMessageDelegate()
                                                .getGenericErrorMessage());
                        MatContext
                                .get()
                                .recordTransactionEvent(
                                        null,
                                        null,
                                        null,
                                        "Unhandled Exception: "
                                                + caught.getLocalizedMessage(),
                                        0);

                    }

                    @Override
                    public void onSuccess(Void result) {
                        shareDisplay.getSearchWidgetBootStrap().getSearchBox().setValue("");
                        cqlLibraryView.resetMessageDisplay();
                        cqlLibraryShared = saveCQLLibraryResult.getCqlLibraryShareDTOs().stream().anyMatch(sd -> (sd.getShareLevel() != null && !(sd.getShareLevel().equals(""))));
                        if (cqlLibraryShared) {
                            cqlLibraryShareMessage = MessageDelegate.getLibrarySuccessfullyShared(saveCQLLibraryResult.getCqlLibraryName());
                        }
                        displaySearch();
                    }
                });

            }
        });


        shareDisplay.getCancelButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                shareDisplay.getSearchWidgetBootStrap().getSearchBox().setValue("");
                displaySearch();
            }
        });

        shareDisplay.getSearchWidgetBootStrap().getGo().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                displayShareWidget();
            }
        });

        shareDisplay.getSearchWidgetFocusPanel().addKeyDownHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    ((Button) shareDisplay.getSearchWidgetBootStrap().getGo()).click();
                }
            }
        });

    }

    protected void draftCQLLibrary(CQLLibraryDataSetObject selectedLibrary) {
        selectedLibrary.setCqlName(detailDisplay.getNameField().getValue().trim());
        selectedLibrary.setLibraryModelType(detailDisplay.getLibraryModelType());
        cqlLibraryView.resetMessageDisplay();

        if (isLibraryNameValid(selectedLibrary.isFhir())) {
            createDraftFromLibrary(selectedLibrary.getId(), selectedLibrary.getCqlName());
        }
    }

    private void createDraftFromLibrary(String libraryId, String libraryName) {
        showSearchingBusy(true);
        MatContext.get().getCQLLibraryService().saveDraftFromVersion(libraryId, libraryName, new AsyncCallback<SaveCQLLibraryResult>() {

            @Override
            public void onFailure(Throwable caught) {
                showSearchingBusy(false);
                detailDisplay.getErrorMessage().createAlert(caught.getLocalizedMessage());
            }

            @Override
            public void onSuccess(SaveCQLLibraryResult result) {
                setIsPageDirty(false);
                showSearchingBusy(false);
                resultToFireEvent = result;

                CQLLibrarySelectedEvent event = CQLLibrarySelectedEvent.Builder.newBuilder()
                        .withCqlLibraryId(result.getId())
                        .withCqlLibraryVersion(result.getVersionStr())
                        .withLibraryName(result.getCqlLibraryName())
                        .withEditable(result.isEditable())
                        .withLocked(false)
                        .withLockedUserId(null)
                        .withLockedUserEmail("")
                        .withLockedUserName("")
                        .withDraft(true) //true because the library is being saved so it is a draft
                        .withLibraryType(result.getLibraryModelType())
                        .build();
                fireCQLLibrarySelectedEvent(event);
                fireCqlLibraryEditEvent();
                MatContext.get().getAuditService().recordCQLLibraryEvent(resultToFireEvent.getId(), "Draft Created", "Draft created based on Version " + resultToFireEvent.getVersionStr(), false, new AsyncCallback<Boolean>() {

                    @Override
                    public void onFailure(Throwable caught) {

                    }

                    @Override
                    public void onSuccess(Boolean result) {

                    }
                });

                showDialogBox(MatContext.get().getMessageDelegate().getLibraryDraftSuccessfulMessage(result.getCqlLibraryName()));
            }
        });
    }

    /**
     * This method is invoked when CQL Library is selected from My/All Libraries Table.
     */
    private void addCQLLibrarySelectionHandlers() {

        cqlLibraryView.getSelectIdForEditTool().addSelectionHandler(new SelectionHandler<CQLLibraryDataSetObject>() {

            @Override
            public void onSelection(SelectionEvent<CQLLibraryDataSetObject> event) {
                final String mid = event.getSelectedItem().getId();
                final CQLLibraryDataSetObject selectedItem = event.getSelectedItem();
                MatContext.get().getLibraryLockService().isLibraryLocked(mid);
                Command waitForLockCheck = new Command() {
                    @Override
                    public void execute() {
                        SynchronizationDelegate synchDel = MatContext.get().getSynchronizationDelegate();
                        if (!synchDel.isCheckingLock()) {
                            if (!synchDel.isLibraryIsLocked()) {
                                isLibrarySelected(selectedItem);
                                if (selectedItem.isEditable()) {
                                    MatContext.get().getLibraryLockService().setLibraryLock();
                                }
                            } else {
                                isLibrarySelected(selectedItem);
                                if (selectedItem.isEditable()) {
                                    MatContext.get().getLibraryLockService().setLibraryLock();
                                }
                            }
                        } else {
                            Scheduler.get().scheduleDeferred(this);
                        }
                    }
                };
                waitForLockCheck.execute();
            }


        });
    }

    /**
     * This method is invoked when CQL Library is selected from Recent Activity Table.
     */
    private void addMostRecentWidgetSelectionHandler() {

        cqlLibraryView.getMostRecentLibraryWidget().addSelectionHandler(new SelectionHandler<CQLLibraryDataSetObject>() {

            @Override
            public void onSelection(SelectionEvent<CQLLibraryDataSetObject> event) {
                final String mid = event.getSelectedItem().getId();
                final CQLLibraryDataSetObject selectedItem = event.getSelectedItem();
                isLibrarySelected(selectedItem);
                MatContext.get().getLibraryLockService().isLibraryLocked(mid);
                Command waitForLockCheck = new Command() {
                    @Override
                    public void execute() {
                        SynchronizationDelegate synchDel = MatContext.get().getSynchronizationDelegate();
                        if (!synchDel.isCheckingLock()) {
                            if (!synchDel.isLibraryIsLocked()) {
                                isLibrarySelected(selectedItem);
                                if (selectedItem.isEditable()) {
                                    MatContext.get().getLibraryLockService().setLibraryLock();
                                }
                            } else {
                                isLibrarySelected(selectedItem);
                                if (selectedItem.isEditable()) {
                                    MatContext.get().getLibraryLockService().setLibraryLock();
                                }
                            }
                        } else {
                            Scheduler.get().scheduleDeferred(this);
                        }
                    }
                };
                waitForLockCheck.execute();

            }
        });

    }

    private void isLibrarySelected(CQLLibraryDataSetObject selectedItem) {
        String userId = selectedItem.getLockedUserId(selectedItem.getLockedUserInfo());
        String email = selectedItem.getLockedUserEmail(selectedItem.getLockedUserInfo());
        String userName = selectedItem.getLockedUserName(selectedItem.getLockedUserInfo());
        boolean isDraft = selectedItem.isDraft();

        CQLLibrarySelectedEvent event = CQLLibrarySelectedEvent.Builder.newBuilder()
                .withCqlLibraryId(selectedItem.getId())
                .withCqlLibraryVersion(selectedItem.getVersion())
                .withLibraryName(selectedItem.getCqlName())
                .withEditable(selectedItem.isEditable())
                .withLocked(selectedItem.isLocked())
                .withLockedUserId(userId)
                .withLockedUserEmail(email)
                .withLockedUserName(userName)
                .withDraft(isDraft)
                .withLibraryType(selectedItem.getLibraryModelType())
                .build();

        fireCQLLibrarySelectedEvent(event);
        fireCqlLibraryEditEvent();
    }

    private void addVersionDisplayViewHandlers() {
        MatContext.get().getEventBus().addHandler(CQLVersionEvent.TYPE, new CQLVersionEvent.Handler() {

            @Override
            public void onVersioned(CQLVersionEvent event) {
                displaySearch();
                if (event.isVersioned()) {

                    isCqlLibraryDeleted = true;
                    cqlLibraryDeletion = true;
                    isCqlLibraryVersioned = true;
                    cqlLibraryVerMessage = event.getMessage();
                } else {

                    isCqlLibraryDeleted = false;
                    cqlLibraryDeletion = false;
                    isCqlLibraryVersioned = false;
                    cqlLibraryVerMessage = event.getMessage();
                }
            }
        });


        versionDisplay.getCancelButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                versionDisplay.getErrorMessages().clearAlert();
                displaySearch();

            }
        });

        versionDisplay.getSaveButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                isCqlLibraryDeleted = false;
                cqlLibraryDeletion = false;
                cqlLibraryShared = false;
                versionDisplay.getErrorMessages().clearAlert();
                CQLLibraryDataSetObject selectedLibrary = versionDisplay.getSelectedLibrary();
                if (((selectedLibrary != null) && (selectedLibrary.getId() != null))
                        && (versionDisplay.getMajorRadioButton().getValue() || versionDisplay
                        .getMinorRadio().getValue())) {
                    saveFinalizedVersion(selectedLibrary.getId(), selectedLibrary.getCqlName(),
                            versionDisplay.getMajorRadioButton().getValue(),
                            selectedLibrary.getVersion(), false);

                } else {
                    versionDisplay
                            .getErrorMessages().createAlert(MatContext.get().getMessageDelegate().getERROR_LIBRARY_VERSION());
                }

            }
        });


    }

    private void recordSuccessCQLLibraryVersionEvent(String libraryId, String versionString) {
        MatContext.get().getAuditService().recordCQLLibraryEvent(libraryId, "CQL Library Versioned",
                "CQL Library Version " + versionString + " created", false,
                new AsyncCallback<Boolean>() {

                    @Override
                    public void onFailure(Throwable caught) {

                    }

                    @Override
                    public void onSuccess(Boolean result) {

                    }
                });
    }

    protected void saveFinalizedVersion(final String libraryId, final String cqlLibName, Boolean isMajor, String version, boolean ignoreUnusedLibraries) {
        versionDisplay.getErrorMessages().clearAlert();
        showSearchingBusy(true);
        versionDisplay.getSaveButton().setEnabled(false);
        versionDisplay.getCancelButton().setEnabled(false);

        MatContext.get().getCQLLibraryService().saveFinalizedVersion(libraryId, isMajor, version, ignoreUnusedLibraries, new AsyncCallback<SaveCQLLibraryResult>() {

            @Override
            public void onFailure(Throwable caught) {
                showSearchingBusy(false);
                versionDisplay.getErrorMessages().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
                versionDisplay.getSaveButton().setEnabled(true);
                versionDisplay.getCancelButton().setEnabled(true);
            }

            @Override
            public void onSuccess(SaveCQLLibraryResult result) {
                showSearchingBusy(false);
                versionDisplay.getSaveButton().setEnabled(true);
                versionDisplay.getCancelButton().setEnabled(true);
                if (result.isSuccess()) {
                    versionSuccessfulEvent(libraryId, cqlLibName, result);
                } else {
                    versionFailureEvent(result.getFailureReason(), libraryId, cqlLibName, isMajor, version);
                }
            }
        });
    }

    private void versionSuccessfulEvent(final String libraryId, final String cqlLibName, SaveCQLLibraryResult result) {
        displaySearch();
        String versionStr = result.getVersionStr();
        recordSuccessCQLLibraryVersionEvent(libraryId, versionStr);
        isCqlLibraryVersioned = true;
        fireSuccessfulVersionEvent(isCqlLibraryVersioned, cqlLibName, MatContext.get().getMessageDelegate().getVersionSuccessfulMessage(cqlLibName, versionStr));
    }

    private void versionFailureEvent(int failureReason, final String libraryId, final String cqlLibName, Boolean isMajor, String version) {
        isCqlLibraryVersioned = false;
        switch (failureReason) {
            case ConstantMessages.INVALID_CQL_DATA:
                versionDisplay.getErrorMessages().createAlert(MatContext.get().getMessageDelegate().getNoVersionCreated());
                break;
            case ConstantMessages.DESCRIPTION_REQUIRED:
                versionDisplay.getErrorMessages().createAlert(MatContext.get().getMessageDelegate().getDescriptionRequired());
                break;
            case ConstantMessages.PUBLISHER_REQUIRED:
                versionDisplay.getErrorMessages().createAlert(MatContext.get().getMessageDelegate().getPublisherRequired());
               break;
            case ConstantMessages.INVALID_CQL_LIBRARIES:
                displayInvalidLibraryDialog(libraryId, cqlLibName, isMajor, version);
                break;
            case SaveUpdateCQLResult.DUPLICATE_LIBRARY_NAME:
                displayDuplicateLibraryDialog();
                break;
            default:
                break;
        }
    }

    private void displayDuplicateLibraryDialog() {
        ConfirmationDialogBox confirmationDialogBox = versionDisplay.createConfirmationDialogBox(MessageDelegate.VERSION_STANDALONE_LIBRARY_NAME_ERROR_MESSAGE, "Return to CQL Library", "Cancel", null, true);
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

    private void displayInvalidLibraryDialog(final String libraryId, final String cqlLibName, Boolean isMajor, String version) {
        String errorMessage = MatContext.get().getMessageDelegate().getUnusedIncludedLibraryWarning(versionDisplay.getSelectedLibrary().getCqlName());
        ConfirmationDialogBox confirmationDialogBox = versionDisplay.createConfirmationDialogBox(errorMessage, CONTINUE, "Cancel", null, false);
        confirmationDialogBox.setObserver(new ConfirmationObserver() {

            @Override
            public void onYesButtonClicked() {
                saveFinalizedVersion(libraryId, cqlLibName, isMajor, version, true);
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

    private void fireSuccessfulVersionEvent(boolean isSuccess, String name, String message) {
        CQLVersionEvent versionEvent = new CQLVersionEvent(isSuccess, name, message);
        MatContext.get().getEventBus().fireEvent(versionEvent);
    }

    private void addDetailDisplayViewHandlers() {
        detailDisplay.getCancelButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                detailDisplay.resetAll();
                displaySearch();

            }
        });

        detailDisplay.getNameField().addValueChangeHandler(event -> setIsPageDirty(true));
    }

    private void setIsPageDirty(boolean isPageDirty) {
        isDirty = isPageDirty;
    }

    private boolean isLibraryNameValid(boolean isFhir) {
        detailDisplay.getErrorMessage().clearAlert();
        if (detailDisplay.getNameField().getText().isEmpty()) {
            detailDisplay.getErrorMessage().createAlert(MatContext.get().getMessageDelegate().getLibraryNameRequired());
            return false;
        } else {
            String cqlName = detailDisplay.getNameField().getText();
            if (!isFhir && !validator.isValidQDMName(cqlName)) {
                detailDisplay.getErrorMessage().createAlert(MatContext.get().getMessageDelegate().getQDMCqlLibyNameError());
                return false;
            }
            logger.log(Level.INFO,"isLibraryNameValid isFhir" + isFhir + " " + cqlName);
            if (isFhir && !validator.isValidFhirCqlName(cqlName)) {
                logger.log(Level.INFO,"isLibraryNameValid invalid name");
                detailDisplay.getErrorMessage().createAlert(MatContext.get().getMessageDelegate().getFhirCqlLibyNameError());
                return false;
            }
        }
        return true;
    }

    private void showDialogBox(String message) {
        detailDisplay.getCreateNewConfirmationDialogBox().show(message);
        detailDisplay.getCreateNewConfirmationDialogBox().getYesButton().setDataDismiss(ButtonDismiss.MODAL);
        detailDisplay.getCreateNewConfirmationDialogBox().getYesButton().setTitle(CONTINUE);
        detailDisplay.getCreateNewConfirmationDialogBox().getYesButton().setText(CONTINUE);
        detailDisplay.getCreateNewConfirmationDialogBox().getYesButton().setFocus(true);
    }

    private void createCQLLibrary() {
        CQLLibraryDataSetObject libraryDataSetObject = new CQLLibraryDataSetObject();
        detailDisplay.getNameField().setText(detailDisplay.getNameField().getText().trim());
        libraryDataSetObject.setCqlName(detailDisplay.getNameField().getText());
        libraryDataSetObject.setLibraryModelType(detailDisplay.getLibraryModelType());

        if (isLibraryNameValid(libraryDataSetObject.isFhir())) {
            MatContext.get().getCQLLibraryService().saveCQLLibrary(libraryDataSetObject, new AsyncCallback<SaveCQLLibraryResult>() {
                @Override
                public void onFailure(Throwable caught) {
                    detailDisplay.getErrorMessage().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
                }

                @Override
                public void onSuccess(SaveCQLLibraryResult result) {
                    resultToFireEvent = result;
                    if (result.isSuccess()) {
                        setIsPageDirty(false);

                        CQLLibrarySelectedEvent event = CQLLibrarySelectedEvent.Builder.newBuilder()
                                .withCqlLibraryId(result.getId())
                                .withCqlLibraryVersion(result.getVersionStr())
                                .withLibraryName(result.getCqlLibraryName())
                                .withEditable(result.isEditable())
                                .withLocked(false)
                                .withLockedUserId(null)
                                .withLockedUserEmail("")
                                .withLockedUserName("")
                                .withDraft(true) //true because the library is being saved so it is a draft
                                .withLibraryType(result.getLibraryModelType())
                                .build();

                        fireCQLLibrarySelectedEvent(event);
                        fireCqlLibraryEditEvent();
                        showDialogBox(MatContext.get().getMessageDelegate().getCreateNewLibrarySuccessfulMessage(detailDisplay.getName().getValue()));
                    } else {
                        if (result.getFailureReason() == SaveUpdateCQLResult.DUPLICATE_LIBRARY_NAME) {
                            detailDisplay.getErrorMessage().createAlert(MessageDelegate.DUPLICATE_LIBRARY_NAME);
                        } else if (result.getFailureReason() == SaveUpdateCQLResult.DUPLICATE_CQL_KEYWORD) {
                            detailDisplay.getErrorMessage().createAlert(MessageDelegate.LIBRARY_NAME_IS_CQL_KEYWORD_ERROR);
                        } else {
                            boolean isFhir = MatContext.get().isCurrentModelTypeFhir();
                            if (isFhir) {
                                detailDisplay.getErrorMessage().createAlert(MatContext.get().getMessageDelegate().getQDMCqlLibyNameError());
                            } else {
                                detailDisplay.getErrorMessage().createAlert(MatContext.get().getMessageDelegate().getFhirCqlLibyNameError());
                            }

                        }
                    }
                }
            });
        }
    }

    private void fireCQLLibrarySelectedEvent(CQLLibrarySelectedEvent evt) {
        cqlLibraryView.resetMessageDisplay();
        detailDisplay.getErrorMessage().clearAlert();
        MatContext.get().getEventBus().fireEvent(evt);
    }

    /**
     * Fire CQL Library edit event.
     */
    private void fireCqlLibraryEditEvent() {
        CQLLibraryEditEvent evt = new CQLLibraryEditEvent();
        MatContext.get().getEventBus().fireEvent(evt);
    }

    private void addCQLLibraryViewHandlers() {
        cqlLibraryView.getCreateNewLibraryButton().addClickHandler(event -> displayNewCQLLibraryWidget());

        cqlLibraryView.getSearchFilterWidget().getSearchInput().addKeyUpHandler(event -> {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                cqlLibraryView.getSearchFilterWidget().getSearchButton().click();
            }
        });

        cqlLibraryView.getSearchButton().addClickHandler(event -> onSearchButtonClick());
    }

    private void onSearchButtonClick() {
        int startIndex = 1;
        cqlLibraryDeletion = false;
        cqlLibraryShared = false;
        isCqlLibraryVersioned = false;
        cqlLibraryView.resetMessageDisplay();
        int filter = cqlLibraryView.getSelectedFilter();
        search(cqlLibraryView.getSearchString().getValue(), filter, startIndex, Integer.MAX_VALUE);
    }

    /**
     * Method is invoked When Option to Create Library Version of Draft is selected from CreateNewItemWidget.
     */
    private void displayVersionWidget() {
        versionDisplay.getErrorMessages().clearAlert();
        cqlLibraryView.resetMessageDisplay();
        panel.getButtonPanel().clear();
        panel.setHeading("My CQL Library > Create CQL Library Version of Draft", CQL_LIBRARY);
        panel.setContent(versionDisplay.asWidget());
        Mat.focusSkipLists(CQL_LIBRARY);
        versionDisplay.clearRadioButtonSelection();
    }

    private void displayShare() {
        displayShareWidget();
        shareDisplay.setCQLibraryName(cqlSharedDataSetObject.getCqlName());
        shareDisplay.getSearchWidgetBootStrap().getSearchBox().setValue("");
        panel.getButtonPanel().clear();
        panel.setHeading("My CQL Libraries > CQL Library Sharing", CQL_LIBRARY);
        panel.setContent(shareDisplay.asWidget());
        Mat.focusSkipLists(CQL_LIBRARY);
    }

    private void displayHistoryWidget(String cqlLibraryId, String cqlLibraryName) {
        setIsPageDirty(false);
        int startIndex = 0;
        int pageSize = Integer.MAX_VALUE;
        String heading = "My CQL Library > History";
        panel.getButtonPanel().clear();
        panel.setHeading(heading, CQL_LIBRARY);
        displayHistoryWidget(cqlLibraryId, startIndex, pageSize);
        historyDisplay.setCQLLibraryId(cqlLibraryId);
        historyDisplay.setCQLLibraryName(cqlLibraryName);
        panel.setContent(historyDisplay.asWidget());
        Mat.focusSkipLists(CQL_LIBRARY);
    }

    private void displayEdit(CQLLibraryDataSetObject result) {

        CQLLibrarySelectedEvent event = CQLLibrarySelectedEvent.Builder.newBuilder()
                .withCqlLibraryId(result.getId())
                .withCqlLibraryVersion(result.getVersion())
                .withLibraryName(result.getCqlName())
                .withEditable(result.isEditable())
                .withLocked(false)
                .withLockedUserId(null)
                .withLockedUserEmail("")
                .withLockedUserName("")
                .withDraft(result.isDraft())
                .withLibraryType(result.getLibraryModelType())
                .build();

        fireCQLLibrarySelectedEvent(event);
        fireCqlLibraryEditEvent();
    }

    private void displayHistoryWidget(String cqlLibraryId, int startIndex, int pageSize) {
        List<String> filterList = new ArrayList<String>();

        MatContext.get().getAuditService().executeSearch(cqlLibraryId, startIndex, pageSize, filterList,
                new AsyncCallback<SearchHistoryDTO>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        historyDisplay.getErrorAlert()
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

    private void displayShareWidget() {
        setIsPageDirty(false);
        String searchText = shareDisplay.getSearchWidgetBootStrap().getSearchBox().getValue();
        final String lastSearchText = (searchText != null) ? searchText.trim() : null;
        shareDisplay.resetMessageDisplay();
        showSearchingBusy(true);
        ((Button) shareDisplay.getSaveButton()).setEnabled(false);
        ((Button) shareDisplay.getCancelButton()).setEnabled(false);

        MatContext.get().getCQLLibraryService().getUserShareInfo(cqlSharedDataSetObject.getId(),
                lastSearchText, new AsyncCallback<SaveCQLLibraryResult>() {

                    @Override
                    public void onSuccess(SaveCQLLibraryResult result) {
                        if ((result.getResultsTotal() == 0)
                                && !lastSearchText.isEmpty()) {
                            shareDisplay.getErrorMessageDisplay().createAlert(MessageDelegate.getNoUsersReturned());
                        }
                        saveCQLLibraryResult = result;
                        shareDisplay.buildCQLLibraryShareTable(result.getCqlLibraryShareDTOs());
                        ((Button) shareDisplay.getSaveButton()).setEnabled(true);
                        ((Button) shareDisplay.getCancelButton()).setEnabled(true);
                        showSearchingBusy(false);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        showSearchingBusy(false);
                        ((Button) shareDisplay.getSaveButton()).setEnabled(true);
                        ((Button) shareDisplay.getCancelButton()).setEnabled(true);
                        Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());

                    }
                });
    }


    private void updateSaveButtonHandler(ClickHandler handler) {
        if (saveHandler != null) {
            saveHandler.removeHandler();
        }

        saveHandler = detailDisplay.getSaveButton().addClickHandler(handler);
    }

    /**
     * This method is called when New Library Option is selected from CreateNewItemWidget.
     */
    private void displayNewCQLLibraryWidget() {
        setIsPageDirty(false);
        boolean isFhirAvailable = MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.MAT_ON_FHIR);
        warningConfirmationMessageAlert = detailDisplay.getWarningConfirmationAlert();
        warningConfirmationMessageAlert.clearAlert();
        panel.getButtonPanel().clear();
        panel.setHeading("My CQL Library > Create New CQL Library", CQL_LIBRARY);
        panel.setContent(detailDisplay.asWidget());
        detailDisplay.setLibraryModelType(isFhirAvailable ? ModelTypeHelper.FHIR : ModelTypeHelper.QDM,
                false);
        updateSaveButtonHandler(event -> createCQLLibrary());
        Mat.focusSkipLists(CQL_LIBRARY);
    }

    private void displayDraftCQLLibraryWidget(CQLLibraryDataSetObject selectedLibrary) {
        setIsPageDirty(false);
        warningConfirmationMessageAlert = detailDisplay.getWarningConfirmationAlert();
        warningConfirmationMessageAlert.clearAlert();
        panel.getButtonPanel().clear();
        panel.setHeading("My CQL Library > Draft CQL Library", CQL_LIBRARY);
        panel.setContent(detailDisplay.asWidget());
        detailDisplay.getNameField().setText(selectedLibrary.getCqlName());
        detailDisplay.setLibraryModelType(selectedLibrary.getLibraryModelType(), true);
        updateSaveButtonHandler(event -> draftCQLLibrary(selectedLibrary));
        Mat.focusSkipLists(CQL_LIBRARY);
    }

    /**
     * This method is called from beforeDisplay and this becomes main method for CQL Library View.
     */
    private void displaySearch() {
        setIsPageDirty(false);
        cqlLibraryView.getCellTablePanel().clear();
        cqlLibraryView.resetMessageDisplay();

        String heading = "CQL Library";
        panel.setHeading(heading, CQL_LIBRARY);
        setSubSkipEmbeddedLink("CQLSearchView_mainPanel");
        FlowPanel fp = new FlowPanel();
        fp.getElement().setId("fp_FlowPanel_CQL");

        int filter = cqlLibraryView.getSelectedFilter();
        search(cqlLibraryView.getSearchString().getValue(), filter, 1, Integer.MAX_VALUE);
        searchRecentLibraries();

        buildCreateLibrary();

        fp.add(cqlLibraryView.asWidget());

        panel.setContent(fp);
        Mat.focusSkipLists(CQL_LIBRARY);
    }

    private void buildCreateLibrary() {
        panel.getButtonPanel().clear();

        cqlLibraryView.getCreateNewLibraryButton().setId("newLibrary_button");
        cqlLibraryView.getCreateNewLibraryButton().setIcon(IconType.LIGHTBULB_O);
        cqlLibraryView.getCreateNewLibraryButton().setIconSize(IconSize.LARGE);
        cqlLibraryView.getCreateNewLibraryButton().setType(ButtonType.LINK);
        cqlLibraryView.getCreateNewLibraryButton().setTitle("Click to create new library");

        cqlLibraryView.getCreateNewLibraryButton().setStyleName("createNewButton");
        panel.getButtonPanel().add(cqlLibraryView.getCreateNewLibraryButton());
    }

    private void search(final String searchText, final int filter, int startIndex, int pageSize) {
        final String lastSearchText = (searchText != null) ? searchText.trim() : null;
        pageSize = 25;
        showSearchingBusy(true);
        cqlLibraryView.resetMessageDisplay();
        boolean isMatOnFhir = MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.MAT_ON_FHIR);

        LibrarySearchModel searchModel = new LibrarySearchModel(filter, startIndex, pageSize, lastSearchText, isMatOnFhir);
        if (!MatContext.get().getLoggedInUserRole().equalsIgnoreCase(ClientConstants.ADMINISTRATOR)) {
            buildAdvancedSearchModel(searchModel);
            cqlLibraryView.getSearchFilterWidget().getAdvancedSearchPanel().getCollapsePanel().setIn(false);
        }
        MatContext.get().getCQLLibraryService().search(searchModel, new AsyncCallback<SaveCQLLibraryResult>() {

            @Override
            public void onSuccess(SaveCQLLibraryResult result) {
                searchModel.setTotalResults(result.getResultsTotal());
                setSearchPills(searchModel);
                if (cqlLibraryView.getSearchFilterWidget().getSelectedFilter() != 0) {
                    cqlLibraryView.getCQLLibrarySearchView().setCQLLibraryListLabel("All CQL Libraries");
                } else {
                    cqlLibraryView.getCQLLibrarySearchView().setCQLLibraryListLabel("My CQL Libraries");
                }

                if (result.getResultsTotal() == 0) {
                    cqlLibraryView.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getNoLibrarues());
                } else {
                    cqlLibraryView.resetMessageDisplay();
                    if (cqlLibraryDeletion) {
                        if (isCqlLibraryDeleted) {
                            cqlLibraryView.getSuccessMessageAlert().createAlert(cqlLibraryDelMessage);
                        } else {
                            if (cqlLibraryDelMessage != null) {
                                cqlLibraryView.getErrorMessageAlert().createAlert(cqlLibraryDelMessage);
                            }
                        }

                    } else if (cqlLibraryShared) {
                        cqlLibraryView.getSuccessMessageAlert().createAlert(cqlLibraryShareMessage);
                        cqlLibraryShared = false;
                    }

                    if (isCqlLibraryVersioned) {
                        cqlLibraryView.getSuccessMessageAlert().createAlert(cqlLibraryVerMessage);
                    }

                }

                cqlLibraryView.buildCellTable(result, searchModel, filter);
                showSearchingBusy(false);
            }

            @Override
            public void onFailure(Throwable caught) {
                Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());

            }
        });
    }

    private void buildAdvancedSearchModel(LibrarySearchModel searchModel) {
        searchModel.setIsDraft(cqlLibraryView.getSearchFilterWidget().getAdvancedSearchPanel().getSearchStateValue());
        searchModel.setModelType(cqlLibraryView.getSearchFilterWidget().getAdvancedSearchPanel().getModelTypeValue());
        searchModel.setModifiedDate(Integer.parseInt(cqlLibraryView.getSearchFilterWidget().getAdvancedSearchPanel().getModifiedWithinValue()));
        searchModel.setModifiedOwner(cqlLibraryView.getSearchFilterWidget().getAdvancedSearchPanel().getModifiedByValue());
        searchModel.setOwner(cqlLibraryView.getSearchFilterWidget().getAdvancedSearchPanel().getOwnedByValue());
    }

    private void resetSearchFields(LibrarySearchModel librarySearchModel) {
        cqlLibraryView.resetSearchDisplay();
        librarySearchModel.reset();
        setSearchPills(librarySearchModel);
    }

    private void setSearchPills(LibrarySearchModel model) {
        cqlLibraryView.getSearchPillPanel().setSearchedByPills(model, "Libraries");
        cqlLibraryView.getSearchPillPanel().getReset().addClickHandler(event -> resetSearchFields(model));
    }

    private void searchRecentLibraries() {
        MatContext.get().getCQLLibraryService().getAllRecentCQLLibrariesForUser(MatContext.get().getLoggedinUserId(),
                new AsyncCallback<SaveCQLLibraryResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
                    }

                    @Override
                    public void onSuccess(SaveCQLLibraryResult result) {
                        cqlLibraryView.getMostRecentLibraryWidget().setResult(result);
                        cqlLibraryView.getMostRecentLibraryWidget().setTableObserver(createCQLLibraryObserver());
                        cqlLibraryView.buildMostRecentWidget();
                        cqlLibraryView.getMostRecentLibraryVerticalPanel().setVisible(true);
                    }
                });
    }

    public static void setSubSkipEmbeddedLink(String name) {
        if (subSkipContentHolder == null) {
            subSkipContentHolder = new FocusableWidget(SkipListBuilder.buildSkipList("Skip to Sub Content"));
        }
        Mat.removeInputBoxFromFocusPanel(subSkipContentHolder.getElement());
        Widget w = SkipListBuilder.buildSubSkipList(name);
        subSkipContentHolder.clear();
        subSkipContentHolder.add(w);
    }

    @Override
    public void beforeClosingDisplay() {
        cqlLibraryView.resetMessageDisplay();
        shareDisplay.resetMessageDisplay();
        shareDisplay.getSearchWidgetBootStrap().getSearchBox().setValue("");
        isLoading = false;
        isCqlLibraryDeleted = false;
        cqlLibraryDeletion = false;
        isCqlLibraryVersioned = false;
        cqlLibraryShared = false;
        cqlLibraryView.getMostRecentLibraryVerticalPanel().setVisible(false);
    }

    @Override
    public void beforeDisplay() {
        cqlLibraryView.buildDefaultView();
        Command waitForUnlock = new Command() {
            @Override
            public void execute() {
                if (!MatContext.get().getLibraryLockService().isResettingLock()) {
                    displaySearch();
                } else {
                    Scheduler.get().scheduleDeferred(this);
                }
            }
        };
        if (MatContext.get().getLibraryLockService().isResettingLock()) {
            waitForUnlock.execute();
        } else {
            displaySearch();
            cqlLibraryView.getSearchFilterWidget().getAdvancedSearchPanel().getCollapsePanel().setIn(false);
        }

    }

    private void deleteCQLLibrary(String libraryId) {
        MatContext.get().getCQLLibraryService().deleteCQLLibrary(libraryId, MatContext.get().getLoggedinLoginId(), new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof AuthenticationException) {
                    dialogBox.setMessage(caught.getLocalizedMessage());
                    dialogBox.getPassword().setText("");
                } else {
                    fireDeletionEvent(true, MatContext.get().getMessageDelegate().getGenericErrorMessage());
                }
            }

            @Override
            public void onSuccess(Void result) {
                dialogBox.closeDialogBox();
                MatContext.get().recordTransactionEvent(libraryId, null, "CQLLibrary_DELETE_EVENT", "CQLLibrary Successfully Deleted", ConstantMessages.DB_LOG);
                fireDeletionEvent(true, MatContext.get().getMessageDelegate().getCQL_LIBRARY_DELETION_SUCCESS_MSG());
            }
        });
    }

    private void fireDeletionEvent(boolean isSuccess, String message) {
        CQLLibraryDeleteEvent deleteEvent = new CQLLibraryDeleteEvent(isSuccess, message);
        MatContext.get().getEventBus().fireEvent(deleteEvent);
    }

    @Override
    public Widget getWidget() {
        return panel;
    }

    private void showSearchingBusy(boolean busy) {
        isLoading = busy;
        ((Button) cqlLibraryView.getSearchButton()).setEnabled(!busy);
        ((TextBox) (cqlLibraryView.getSearchString())).setEnabled(!busy);
        cqlLibraryView.getCreateNewLibraryButton().setEnabled(!busy);
        cqlLibraryView.getCustomFilterCheckBox().setEnabled(!busy);
        cqlLibraryView.getSearchFilterWidget().getAdvancedSearchPanel().getAdvanceSearchAnchor().setEnabled(!busy);
        if (busy) {
            Mat.showLoadingMessage();
        } else {
            Mat.hideLoadingMessage();
        }
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
        detailDisplay.getErrorMessage().clearAlert();
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
