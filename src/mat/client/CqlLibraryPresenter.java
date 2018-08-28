package mat.client;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.DTO.AuditLogDTO;
import mat.DTO.SearchHistoryDTO;
import mat.client.buttons.CustomButton;
import mat.client.cql.CQLLibraryDetailView;
import mat.client.cql.CQLLibraryHistoryView;
import mat.client.cql.CQLLibrarySearchView;
import mat.client.cql.CQLLibraryShareView;
import mat.client.cql.CQLLibraryVersionView;
import mat.client.cql.ConfirmationObserver;
import mat.client.cqlworkspace.ConfirmationDialogBox;
import mat.client.cqlworkspace.EditConfirmationDialogBox;
import mat.client.event.CQLLibraryDeleteEvent;
import mat.client.event.CQLLibraryEditEvent;
import mat.client.event.CQLLibrarySelectedEvent;
import mat.client.event.CQLVersionEvent;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.FocusableWidget;
import mat.client.shared.MatContext;
import mat.client.shared.MessageAlert;
import mat.client.shared.MessageDelegate;
import mat.client.shared.MostRecentCQLLibraryWidget;
import mat.client.shared.SearchWidgetBootStrap;
import mat.client.shared.SearchWidgetWithFilter;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SynchronizationDelegate;
import mat.client.shared.search.SearchResultUpdate;
import mat.client.shared.ui.DeleteConfirmDialogBox;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLLibraryShareDTO;
import mat.shared.CQLModelValidator;
import mat.shared.ConstantMessages;


/**
 * The Class CqlLibraryPresenter.

 */
public class CqlLibraryPresenter implements MatPresenter {

	/** The panel. */
	// private SimplePanel panel = new SimplePanel();

	/** The panel. */
	private ContentWithHeadingWidget panel = new ContentWithHeadingWidget();
	
	/** The sub skip content holder. */
	private static FocusableWidget subSkipContentHolder;

	/** The cql library view. */
	ViewDisplay cqlLibraryView;
	
	/** The detail display. */
	DetailDisplay detailDisplay;
	
	/** The version display. */
	VersionDisplay versionDisplay;

	/** The share display. */
	ShareDisplay shareDisplay;
	
	/** The is Library search filter visible. */
	boolean isSearchFilterVisible = true;
	/** The is search visible on version. */
	boolean isSearchVisibleOnVersion = true;
	/** The is search visible on draft. */
	boolean isSearchVisibleOnDraft = true;
	
	boolean isLoading = false;

	/** The validator. */
	CQLModelValidator validator = new CQLModelValidator();
	
	/** The cql shared data set object. */
	CQLLibraryDataSetObject cqlSharedDataSetObject;
	
	/** The save CQL library result. */
	SaveCQLLibraryResult saveCQLLibraryResult;

	/** The history display. */
	CQLLibraryHistoryView historyDisplay;
	
	/** The cqlLibrary deletion. */
	private boolean cqlLibraryDeletion = false;

	/** The cqlLibrary deletion message. */
	private String cqlLibraryDelMessage;
	
	/** The cqlLibrary version message. */
	private String cqlLibraryVerMessage;
	
	private boolean isCqlLibraryDeleted = false;
	
	private boolean isCqlLibraryVersioned = false;
	
	private SaveCQLLibraryResult resultToFireEvent;
	
	private boolean cqlLibraryShared = false;
	
	private String cqlLibraryShareMessage;
	
	/**
	 * The Interface ViewDisplay.
	 */
	public static interface ViewDisplay {

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

		/**
		 * Builds the cell table.
		 *
		 * @param searchModel the search model
		 * @param searchText the search text
		 * @param filter the filter
		 */
		void buildCellTable(SaveCQLLibraryResult searchModel, String searchText,int filter);

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

	}

	public static interface VersionDisplay{

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
		
		ConfirmationDialogBox createConfirmationDialogBox(String messageText, String yesButtonText, String noButtonText, ConfirmationObserver observer);
	}
	
	
	/**
	 * The Interface DetailDisplay.
	 */
	public static interface DetailDisplay {
		/**
		 * Gets the name.
		 * 
		 * @return the name
		 */
		public HasValue<String> getName();

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
	}
	
	
	/**
	 * The Interface ShareDisplay.
	 */
	public static interface ShareDisplay {

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

	/**
	 * Instantiates a new cql library presenter.
	 *
	 * @param cqlLibraryView the cql library view
	 * @param detailDisplay the detail display
	 * @param versionDisplay the version display
	 * @param draftDisplay the draft display
	 * @param shareDisplay the share display
	 * @param historyDisplay the history display
	 */
	public CqlLibraryPresenter(CqlLibraryView cqlLibraryView, CQLLibraryDetailView detailDisplay, 
			CQLLibraryVersionView versionDisplay,  CQLLibraryShareView shareDisplay, CQLLibraryHistoryView historyDisplay) {
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
		cqlLibraryView.getCQLLibrarySearchView().setObserver(new CQLLibrarySearchView.Observer() {
			
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
				displayHistory(
						result.getId(),
						result.getCqlName());
			}

			@Override
			public void onCreateClicked(CQLLibraryDataSetObject object) {
				if(!isLoading && object.isDraftable()){
					CQLLibraryDataSetObject selectedLibrary = object;
					if (((selectedLibrary !=null) && (selectedLibrary.getId() != null))) {
						saveDraftFromVersion(selectedLibrary);
					}
				} else if (!isLoading && object.isVersionable()){
					versionDisplay.setSelectedLibraryObject(object);
					createVersion();
				} 
				
			}

			@Override
			public void onDeleteClicked(CQLLibraryDataSetObject object) {
				final String cqlLibraryId = object.getId();
				final DeleteConfirmDialogBox dialogBox = new DeleteConfirmDialogBox();
				dialogBox.showDeletionConfimationDialog(MatContext.get().getMessageDelegate()
						.getWARNING_DELETION_CQL_LIBRARY());
				dialogBox.getConfirmbutton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						
						checkPasswordForCQLLibraryDeletion(dialogBox.getPasswordEntered(), 
								cqlLibraryId);
						
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
			
		});
		
	}

	/**
	 * Adds the share display view handlers.
	 */
	private void addShareDisplayViewHandlers() {
		
		shareDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				/*cqlSharedDataSetObject = null;*/
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
						if(cqlLibraryShared) {
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
				/*cqlSharedDataSetObject = null;*/
				shareDisplay.getSearchWidgetBootStrap().getSearchBox().setValue(""); 
				displaySearch();
			}
		});
		
		shareDisplay.getSearchWidgetBootStrap().getGo().addClickHandler(new ClickHandler() {			
			
			@Override
			public void onClick(ClickEvent event) {
				searchUsersForSharing();				
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

	

	/**
	 * Save draft from version.
	 *
	 * @param selectedLibrary the selected library
	 */
	protected void saveDraftFromVersion(CQLLibraryDataSetObject selectedLibrary) {
		cqlLibraryView.resetMessageDisplay();
		showSearchingBusy(true);
		MatContext.get().getCQLLibraryService().saveDraftFromVersion(selectedLibrary.getId(), new AsyncCallback<SaveCQLLibraryResult>() {

			@Override
			public void onFailure(Throwable caught) {
				showSearchingBusy(false);
				cqlLibraryView.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}

			@Override
			public void onSuccess(SaveCQLLibraryResult result) {
				showSearchingBusy(false);
				
				resultToFireEvent = result;
				
				cqlLibraryView.getDraftConfirmationDialogBox().show(MatContext.get().getMessageDelegate().getLibraryDraftSuccessfulMessage(result.getCqlLibraryName()));
				cqlLibraryView.getDraftConfirmationDialogBox().getYesButton().setTitle("Continue");
				cqlLibraryView.getDraftConfirmationDialogBox().getYesButton().setText("Continue");
				cqlLibraryView.getDraftConfirmationDialogBox().getYesButton().setFocus(true);
				
			}
		});
		cqlLibraryView.getDraftConfirmationDialogBox().hide();
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
							DeferredCommand.addCommand(this);
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
	private void addMostRecentWidgetSelectionHandler(){
		
		cqlLibraryView.getMostRecentLibraryWidget().addSelectionHandler(new SelectionHandler<CQLLibraryDataSetObject>() {

			@Override
			public void onSelection(SelectionEvent<CQLLibraryDataSetObject> event) {
				//String cqlId = event.getSelectedItem().getId();
				//isLibrarySelected(event.getSelectedItem());
				final String mid = event.getSelectedItem().getId();
				//final boolean isEditable = event.getSelectedItem().isEditable();
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
							DeferredCommand.addCommand(this);
						}
					}
				};
				waitForLockCheck.execute();
				
			}
		});
		
	}
	
	/**
	 * Checks if is library selected.
	 *
	 * @param selectedItem the selected item
	 */
	private void isLibrarySelected(CQLLibraryDataSetObject selectedItem) {
		String userId = selectedItem.getLockedUserId(selectedItem.getLockedUserInfo());
		String email = selectedItem.getLockedUserEmail(selectedItem.getLockedUserInfo());
		String userName  = selectedItem.getLockedUserName(selectedItem.getLockedUserInfo());
		fireCQLLibrarySelectedEvent(selectedItem.getId(), selectedItem.getVersion(), selectedItem.getCqlName(), selectedItem.isEditable(), selectedItem.isLocked(),
				userId,email,userName);
		fireCqlLibraryEditEvent();
	}

	
	/**
	 * Version Event Handlers are added here.
	 */
	private void addVersionDisplayViewHandlers(){
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
				cqlLibraryDeletion =false;
				cqlLibraryShared = false;
				versionDisplay.getErrorMessages().clearAlert();
				CQLLibraryDataSetObject selectedLibrary = versionDisplay.getSelectedLibrary();
				if (((selectedLibrary !=null) && (selectedLibrary.getId() != null))
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
	
	private void recordCQLLibraryAuditEvent(String libraryId, String versionString) {
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
	
	/**
	 * Method to Save Version of a Draft.
	 *
	 * @param libraryId the library id
	 * @param string 
	 * @param isMajor the is major
	 * @param version the version
	 */
	protected void saveFinalizedVersion(final String libraryId, final String cqlLibName, Boolean isMajor, String version, boolean ignoreUnusedLibraries) {
		versionDisplay.getErrorMessages().clearAlert();
		showSearchingBusy(true);
		((Button)versionDisplay.getSaveButton()).setEnabled(false);
		((Button)versionDisplay.getCancelButton()).setEnabled(false);
		MatContext.get().getCQLLibraryService().saveFinalizedVersion(libraryId, isMajor, version, ignoreUnusedLibraries,
				new AsyncCallback<SaveCQLLibraryResult>() {

					@Override
					public void onFailure(Throwable caught) {
						showSearchingBusy(false);
						versionDisplay.getErrorMessages().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						((Button) versionDisplay.getSaveButton()).setEnabled(true);
						((Button) versionDisplay.getCancelButton()).setEnabled(true);
					}

					@Override
					public void onSuccess(SaveCQLLibraryResult result) {
						showSearchingBusy(false);
						((Button) versionDisplay.getSaveButton()).setEnabled(true);
						((Button) versionDisplay.getCancelButton()).setEnabled(true);
						if (result.isSuccess()) {
							displaySearch();
							String versionStr = result.getVersionStr();
							recordCQLLibraryAuditEvent(libraryId, versionStr);
							isCqlLibraryVersioned = true;
							fireSuccessfullVersionEvent(isCqlLibraryVersioned,cqlLibName,MatContext.get().getMessageDelegate().getVersionSuccessfulMessage(cqlLibName, versionStr));
						} else {
							isCqlLibraryVersioned = false;
							if(result.getFailureReason() == ConstantMessages.INVALID_CQL_DATA) {
								versionDisplay.getErrorMessages().createAlert(MatContext.get().getMessageDelegate().getNoVersionCreated());
							} else if(result.getFailureReason() == ConstantMessages.INVALID_CQL_LIBRARIES) {
								String libraryName = versionDisplay.getSelectedLibrary().getCqlName();
								String errorMessage =  MatContext.get().getMessageDelegate().getUnusedIncludedLibraryWarning(libraryName);
								ConfirmationDialogBox dialogBox = versionDisplay.createConfirmationDialogBox(errorMessage, "Continue", "Cancel", null);
								dialogBox.setObserver(new ConfirmationObserver() {
									
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
								dialogBox.show();
							}
						}
					}
		});
	}

	private void fireSuccessfullVersionEvent(boolean isSuccess, String name, String message){
		CQLVersionEvent versionEvent = new CQLVersionEvent(isSuccess, name, message);
		MatContext.get().getEventBus().fireEvent(versionEvent);
	}
	
	/**
	 * Detail View Event Handlers.
	 */
	private void addDetailDisplayViewHandlers() {
		detailDisplay.getCancelButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				detailDisplay.resetAll();
				displaySearch();

			}
		});

		detailDisplay.getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				detailDisplay.getErrorMessage().clearAlert();
				if (detailDisplay.getNameField().getText().isEmpty()) {
					detailDisplay.getErrorMessage().createAlert(MatContext.get().getMessageDelegate().getLibraryNameRequired());
				} else {
					if (validator.validateForAliasNameSpecialChar(detailDisplay.getNameField().getText().trim())) {
						showSearchingBusy(true);
						detailDisplay.getCreateNewConfirmationDialogBox().show(MatContext.get().getMessageDelegate().getCreateNewLibrarySuccessfulMessage(detailDisplay.getName().getValue()));
						detailDisplay.getCreateNewConfirmationDialogBox().getYesButton().setTitle("Continue");
						detailDisplay.getCreateNewConfirmationDialogBox().getYesButton().setText("Continue");
						detailDisplay.getCreateNewConfirmationDialogBox().getYesButton().setFocus(true);
					} else {
						detailDisplay.getErrorMessage().createAlert(MatContext.get().getMessageDelegate().getCqlStandAloneLibraryNameError());
					}
				}
			}
		});
		detailDisplay.getCreateNewConfirmationDialogBox().getYesButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				CQLLibraryDataSetObject libraryDataSetObject = new CQLLibraryDataSetObject();
				libraryDataSetObject.setCqlName(detailDisplay.getNameField().getText());
				saveCqlLibrary(libraryDataSetObject);
				showSearchingBusy(false);
			}
		});
	}

	/**
	 * Save cql library.
	 *
	 * @param libraryDataSetObject the library data set object
	 */
	private void saveCqlLibrary(CQLLibraryDataSetObject libraryDataSetObject) {
		MatContext.get().getCQLLibraryService().save(libraryDataSetObject, new AsyncCallback<SaveCQLLibraryResult>() {

			@Override
			public void onSuccess(SaveCQLLibraryResult result) {
				if(result.isSuccess()){
					fireCQLLibrarySelectedEvent(result.getId(), result.getVersionStr(), result.getCqlLibraryName(), result.isEditable(), false,
									null,"","");
					fireCqlLibraryEditEvent();
				} else {
						detailDisplay.getErrorMessage().createAlert(MatContext.get().getMessageDelegate().getCqlStandAloneLibraryNameError());
				}

			}

			@Override
			public void onFailure(Throwable caught) {
				detailDisplay.getErrorMessage().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
		});

	}

	
	/**
	 * CQLLibrary Selected Event is fired from this method.
	 *
	 * @param id the id
	 * @param version the version
	 * @param name the name
	 * @param isEditable the is editable
	 * @param isLocked the is locked
	 * @param lockedUserId the locked user id
	 */
	private void fireCQLLibrarySelectedEvent(String id, String version,
			String name, boolean isEditable, boolean isLocked, String lockedUserId, String lockedUserEmail, String lockedUserName) {
		CQLLibrarySelectedEvent evt = new CQLLibrarySelectedEvent(id, version, name,isEditable, isLocked, lockedUserId,lockedUserEmail,lockedUserName);
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
	
	
	/**
	 * CQL Library View Event Handlers are added here.
	 */
	private void addCQLLibraryViewHandlers() {
//		cqlLibraryView.getAddNewFolderButton().addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				cqlLibraryView.getErrorMessageAlert().clearAlert();
//			
//				isCreateNewItemWidgetVisible = !isCreateNewItemWidgetVisible;
//				
//				if(isSearchFilterVisible){
//					isSearchFilterVisible = !isSearchFilterVisible;
//					cqlLibraryView.getWidgetVP().clear();
//				}
//				
//				if(isCreateNewItemWidgetVisible){
//					cqlLibraryView.getWidgetVP().add(cqlLibraryView.getCreateNewItemWidget());
//				} else {
//					cqlLibraryView.getWidgetVP().clear();
//				}
//
//			}
//		});
//		
//		cqlLibraryView.getZoomButton().addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				cqlLibraryView.getErrorMessageAlert().clearAlert();
//				
//				if (isCreateNewItemWidgetVisible) {
//					isCreateNewItemWidgetVisible = !isCreateNewItemWidgetVisible;
//					cqlLibraryView.getWidgetVP().clear();
//				}
//				isSearchFilterVisible = !isSearchFilterVisible;
//				
//				if(isSearchFilterVisible){
//					cqlLibraryView.getWidgetVP().add(cqlLibraryView.getSearchFilterWidget());
//				} else {
//					cqlLibraryView.getWidgetVP().clear();
//				}
//				
//			}
//		});

		cqlLibraryView.getDraftConfirmationDialogBox().getYesButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(resultToFireEvent.isSuccess()){
					cqlLibraryView.getDraftConfirmationDialogBox().hide();
					fireCQLLibrarySelectedEvent(resultToFireEvent.getId(), resultToFireEvent.getVersionStr(), resultToFireEvent.getCqlLibraryName(), resultToFireEvent.isEditable(), false,
							null,"","");
					fireCqlLibraryEditEvent();
					MatContext
					.get()
					.getAuditService()
					.recordCQLLibraryEvent(
							resultToFireEvent.getId(),
							"Draft Created",
							"Draft created based on Version "
									+ resultToFireEvent.getVersionStr(),
									false,
									new AsyncCallback<Boolean>() {
								
								@Override
								public void onFailure(
										Throwable caught) {
									
								}
								
								@Override
								public void onSuccess(
										Boolean result) {
									
								}
							});
				} else {
					cqlLibraryView.getDraftConfirmationDialogBox().hide();
					cqlLibraryView.getErrorMessageAlert().createAlert("Invalid data");
				}
				resultToFireEvent = new SaveCQLLibraryResult();
			}
		});
		
		cqlLibraryView.getCreateNewLibraryButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				createNew(); 
			}

		});
		
		cqlLibraryView.getSearchFilterWidget().getMainFocusPanel().addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					cqlLibraryView.getSearchFilterWidget().getSearchButton().click();
				}
			}
		});
		
		
		cqlLibraryView.getSearchButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int startIndex = 1;
				cqlLibraryDeletion = false;
				cqlLibraryShared = false;
				isCqlLibraryVersioned = false;
				cqlLibraryView.resetMessageDisplay();
				int filter = cqlLibraryView.getSelectedFilter();
				search(cqlLibraryView.getSearchString().getValue(),filter, startIndex,Integer.MAX_VALUE);
			}
		});

	}

	/**
	 * Method is invoked When Option to Create Library Version of Draft is selected from CreateNewItemWidget.
	 */
	private void createVersion() {
		
		versionDisplay.getErrorMessages().clearAlert();
		cqlLibraryView.resetMessageDisplay();
		panel.getButtonPanel().clear();
		panel.setHeading("My CQL Library > Create CQL Library Version of Draft", "CQLLibrary");
		panel.setContent(versionDisplay.asWidget());
		Mat.focusSkipLists("CQLLibrary");
		versionDisplay.clearRadioButtonSelection();

	}
	
	
	/**
	 * Display share.
	 */
	private void displayShare() {
		searchUsersForSharing();
		shareDisplay.setCQLibraryName(cqlSharedDataSetObject.getCqlName());
		shareDisplay.getSearchWidgetBootStrap().getSearchBox().setValue("");
		panel.getButtonPanel().clear();
		panel.setHeading("My CQL Libraries > CQL Library Sharing", "CQLLibrary");
		panel.setContent(shareDisplay.asWidget());
		Mat.focusSkipLists("CQLLibrary");
	}
	
	/**
	 * Display history.
	 *
	 * @param cqlLibraryId the cql library id
	 * @param cqlLibraryName the cql library name
	 */
	private void displayHistory(String cqlLibraryId, String cqlLibraryName) {
		int startIndex = 0;
		int pageSize = Integer.MAX_VALUE;
		String heading = "My CQL Library > History";
		panel.getButtonPanel().clear();
		panel.setHeading(heading, "CQLLibrary");
		searchHistory(cqlLibraryId, startIndex, pageSize);
		historyDisplay.setCQLLibraryId(cqlLibraryId);
		historyDisplay.setCQLLibraryName(cqlLibraryName);
		panel.setContent(historyDisplay.asWidget());
		Mat.focusSkipLists("CQLLibrary");
	}

	/**
	 * Display edit.
	 * @param result 
	 */
	private void displayEdit(CQLLibraryDataSetObject result) {
		fireCQLLibrarySelectedEvent(result.getId(), result.getVersion(), result.getCqlName(), result.isEditable(), false,
				null,"","");
		fireCqlLibraryEditEvent();
	}
	
	/**
	 * Search history.
	 *
	 * @param cqlLibraryId the cql library id
	 * @param startIndex the start index
	 * @param pageSize the page size
	 */
	private void searchHistory(String cqlLibraryId, int startIndex, int pageSize) {
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

	
	/**
	 * Search users for sharing.
	 */
	private void searchUsersForSharing(){
		String searchText = shareDisplay.getSearchWidgetBootStrap().getSearchBox().getValue();
	    final String lastSearchText = (searchText != null) ? searchText.trim() : null;
	    shareDisplay.resetMessageDisplay();
		showSearchingBusy(true);		
		((Button)shareDisplay.getSaveButton()).setEnabled(false);
		((Button)shareDisplay.getCancelButton()).setEnabled(false);
		
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
				((Button)shareDisplay.getSaveButton()).setEnabled(true);
				((Button)shareDisplay.getCancelButton()).setEnabled(true);
				showSearchingBusy(false);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				showSearchingBusy(false);				
				((Button)shareDisplay.getSaveButton()).setEnabled(true);
				((Button)shareDisplay.getCancelButton()).setEnabled(true);
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				
			}
		});
		
		
	}
	
	
	/**
	 * This method is called when New Library Option is selected from CreateNewItemWidget. 
	 */
	private void createNew() {
		panel.getButtonPanel().clear();
		panel.setHeading("My CQL Library > Create New CQL Library", "CQLLibrary");
		panel.setContent(detailDisplay.asWidget());
		Mat.focusSkipLists("CQLLibrary");

	}
	
	/**
	 * This method is called from beforeDisplay and this becomes main method for CQL Library View. 
	 */
	private void displaySearch() {
		cqlLibraryView.getCellTablePanel().clear();
		cqlLibraryView.resetMessageDisplay();
		
		String heading = "CQL Library";
		panel.setHeading(heading, "CQLLibrary");
		setSubSkipEmbeddedLink("CQLSearchView_mainPanel");
		FlowPanel fp = new FlowPanel();
		fp.getElement().setId("fp_FlowPanel_CQL");
		
		int filter = cqlLibraryView.getSelectedFilter();
		search(cqlLibraryView.getSearchString().getValue(), filter, 1,Integer.MAX_VALUE);
		searchRecentLibraries();
		
		buildCreateLibrary(); 
		
		fp.add(cqlLibraryView.asWidget());
		isSearchFilterVisible = true;
		panel.setContent(fp);
		Mat.focusSkipLists("CQLLibrary");
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
	
	/**
	 * This method reterives all Libraries in CQL Library tab based on Selected filters and Search Input.
	 *
	 * @param searchText the search text
	 * @param filter the filter
	 * @param startIndex the start index
	 * @param pageSize the page size
	 */
	private void search(final String searchText, final int filter, int startIndex,int pageSize) {
		final String lastSearchText = (searchText != null) ? searchText
				.trim() : null;
				//pageSize = Integer.MAX_VALUE;
				pageSize = 25;
				showSearchingBusy(true);
				cqlLibraryView.resetMessageDisplay();
				MatContext.get().getCQLLibraryService().search(lastSearchText, filter, startIndex,pageSize, new AsyncCallback<SaveCQLLibraryResult>() {
					
					@Override
					public void onSuccess(SaveCQLLibraryResult result) {
						if(cqlLibraryView.getSearchFilterWidget().
								getSelectedFilter()!=0){
							cqlLibraryView.getCQLLibrarySearchView().setCQLLibraryListLabel("All CQL Libraries");
						}else{
							cqlLibraryView.getCQLLibrarySearchView().setCQLLibraryListLabel("My CQL Libraries");
						}
						
						if ((result.getResultsTotal() == 0)
								&& !lastSearchText.isEmpty()) {
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

							} else if(cqlLibraryShared) {
								cqlLibraryView.getSuccessMessageAlert().createAlert(cqlLibraryShareMessage);
								cqlLibraryShared = false;
							}
							
							if(isCqlLibraryVersioned){
								cqlLibraryView.getSuccessMessageAlert().createAlert(cqlLibraryVerMessage);
							}

						}

						SearchResultUpdate sru = new SearchResultUpdate();
						sru.update(result, (TextBox) cqlLibraryView.getSearchString(), lastSearchText);
						cqlLibraryView.buildCellTable(result, lastSearchText,filter);
						showSearchingBusy(false);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						
					}
				});
	}
	
	/**
	 * This method reterives Two recent Libraries in CQL Library.
	 */
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
				cqlLibraryView.getMostRecentLibraryWidget().setObserver(new MostRecentCQLLibraryWidget.Observer() {
					
					@Override
					public void onEditClicked(CQLLibraryDataSetObject object) {
						cqlLibraryDeletion = false;
						isCqlLibraryDeleted = false;
						isCqlLibraryVersioned = false;
						cqlLibraryView.resetMessageDisplay();
						displayEdit(object);
					}
				});
				cqlLibraryView.buildMostRecentWidget();;
			}
		});
	}
	
	
	
	/**
	 * Sets the sub skip embedded link.
	 *
	 * @param name
	 *            the new sub skip embedded link
	 */
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

	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
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
	}

	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		cqlLibraryView.buildDefaultView();
		Command waitForUnlock = new Command() {
			@Override
			public void execute() {
				if (!MatContext.get().getLibraryLockService().isResettingLock()) {
					displaySearch();
				} else {
					DeferredCommand.addCommand(this);
				}
			}
		};
		if (MatContext.get().getLibraryLockService().isResettingLock()) {
			waitForUnlock.execute();
		} else {
			displaySearch();
		}

	}
	
	private void checkPasswordForCQLLibraryDeletion(final String password, final String cqlLibraryId) {

		MatContext.get().getLoginService().isValidPassword(MatContext.get().getLoggedinLoginId(), password,
				new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						fireSuccessfullDeletionEvent(false, null);
					}

					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							deleteCQLLibrary(cqlLibraryId);
						} else {
							fireSuccessfullDeletionEvent(false,
									MatContext.get().getMessageDelegate().getMeasureDeletionInvalidPwd());
						}
					}
				});
	}
	
	
	/**
	 * Delete CQL library.
	 *
	 * @param cqlLibraryId the cql library id
	 */
	private void deleteCQLLibrary(final String cqlLibraryId) {

		MatContext.get().getCQLLibraryService().deleteCQLLibrary(cqlLibraryId,
				MatContext.get().getLoggedinLoginId(), new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						fireSuccessfullDeletionEvent(false, null);
					}

					/* (non-Javadoc)
					 * @see com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(java.lang.Object)
					 */
					@Override
					public void onSuccess(Void result) {
						MatContext.get().recordTransactionEvent(cqlLibraryId, null,
								"CQLLibrary_DELETE_EVENT", "CQLLibrary Successfully Deleted", ConstantMessages.DB_LOG);
						fireSuccessfullDeletionEvent(true,
								MatContext.get().getMessageDelegate().getCQL_LIBRARY_DELETION_SUCCESS_MSG());
					}

				});	

	}
	
	private void fireSuccessfullDeletionEvent(boolean isSuccess, String message){
		CQLLibraryDeleteEvent deleteEvent = new CQLLibraryDeleteEvent(isSuccess, message);
		MatContext.get().getEventBus().fireEvent(deleteEvent);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		return panel;
	}
	
	/**
	 * Show searching busy.
	 * 
	 * @param busy
	 *            the busy
	 */
	private void showSearchingBusy(boolean busy) {
		isLoading= busy;
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
	}

}
