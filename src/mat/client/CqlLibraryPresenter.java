package mat.client;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.TextArea;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
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
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.DTO.AuditLogDTO;
import mat.DTO.SearchHistoryDTO;
import mat.client.cql.CQLLibraryDetailView;
import mat.client.cql.CQLLibraryDraftView;
import mat.client.cql.CQLLibraryHistoryView;
import mat.client.cql.CQLLibrarySearchView;
import mat.client.cql.CQLLibraryShareView;
import mat.client.cql.CQLLibraryVersionView;
import mat.client.event.CQLLibraryEditEvent;
import mat.client.event.CQLLibrarySelectedEvent;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.CreateNewItemWidget;
import mat.client.shared.CustomButton;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.FocusableWidget;
import mat.client.shared.MatContext;
import mat.client.shared.MessageAlert;
import mat.client.shared.MostRecentCQLLibraryWidget;
import mat.client.shared.SearchWidget;
import mat.client.shared.SearchWidgetWithFilter;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SynchronizationDelegate;
import mat.client.shared.search.SearchResultUpdate;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLLibraryShareDTO;
import mat.model.cql.CQLModel;
import mat.shared.CQLModelValidator;
import mat.shared.ConstantMessages;
// TODO: Auto-generated Javadoc

/**
 * The Class CqlLibraryPresenter.
 *
 * @author jnarang
 */
@SuppressWarnings("deprecation")
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
	
	/** The draft display. */
	DraftDisplay draftDisplay;
	
	/** The share display. */
	ShareDisplay shareDisplay;

	/** The is create new Item widget visible. */
	boolean isCreateNewItemWidgetVisible = false;
	
	/** The is Library search filter visible. */
	boolean isSearchFilterVisible = true;
	/** The is search visible on version. */
	boolean isSearchVisibleOnVersion = true;
	/** The is search visible on draft. */
	boolean isSearchVisibleOnDraft = true;
	

	/** The cql model. */
	private CQLModel cqlModel;

	/** The validator. */
	CQLModelValidator validator = new CQLModelValidator();
	
	/** The cql shared data set object. */
	CQLLibraryDataSetObject cqlSharedDataSetObject;
	
	/** The save CQL library result. */
	SaveCQLLibraryResult saveCQLLibraryResult;

	/** The history display. */
	CQLLibraryHistoryView historyDisplay;
	
	/**
	 * The Interface DraftDisplay.
	 */
	public static interface DraftDisplay {

		/**
		 * Builds the data table.
		 *
		 * @param result the result
		 */
		void buildDataTable(SaveCQLLibraryResult result);

		/**
		 * As widget.
		 *
		 * @return the widget
		 */
		Widget asWidget();

		/**
		 * Gets the error messages.
		 *
		 * @return the error messages
		 */
		ErrorMessageAlert getErrorMessages();

		/**
		 * Gets the selected library.
		 *
		 * @return the selected library
		 */
		CQLLibraryDataSetObject getSelectedLibrary();

		/**
		 * Gets the zoom button.
		 *
		 * @return the zoom button
		 */
		CustomButton getZoomButton();

		/**
		 * Gets the search widget.
		 *
		 * @return the search widget
		 */
		SearchWidget getSearchWidget();

		/**
		 * Gets the search button.
		 *
		 * @return the search button
		 */
		HasClickHandlers getSearchButton();

		/**
		 * Gets the cancel button.
		 *
		 * @return the cancel button
		 */
		HasClickHandlers getCancelButton();

		/**
		 * Gets the save button.
		 *
		 * @return the save button
		 */
		HasClickHandlers getSaveButton();
		
	}
	
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
		 * Gets the creates the new item widget.
		 *
		 * @return the creates the new item widget
		 */
		CreateNewItemWidget getCreateNewItemWidget();

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
		 * Gets the selected option.
		 *
		 * @return the selected option
		 */
		String getSelectedOption();

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
		 * Clear selections.
		 */
		void clearSelections();

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

	}

	/**
	 * The Interface VersionDisplay.
	 */
	public static interface VersionDisplay{

		/**
		 * Builds the data table.
		 *
		 * @param result the result
		 */
		void buildDataTable(SaveCQLLibraryResult result);

		/**
		 * As widget.
		 *
		 * @return the widget
		 */
		Widget asWidget();

		/**
		 * Gets the major radio button.
		 *
		 * @return the major radio button
		 */
		RadioButton getMajorRadioButton();

		/**
		 * Gets the minor radio.
		 *
		 * @return the minor radio
		 */
		RadioButton getMinorRadio();

		/**
		 * Gets the search widget.
		 *
		 * @return the search widget
		 */
		//SearchWidget getSearchWidget();

		/**
		 * Gets the zoom button.
		 *
		 * @return the zoom button
		 */
		//CustomButton getZoomButton();

		/**
		 * Gets the error messages.
		 *
		 * @return the error messages
		 */
		ErrorMessageAlert getErrorMessages();

		/**
		 * Gets the save button.
		 *
		 * @return the save button
		 */
		org.gwtbootstrap3.client.ui.Button getSaveButton();

		/**
		 * Gets the cancel button.
		 *
		 * @return the cancel button
		 */
		org.gwtbootstrap3.client.ui.Button getCancelButton();

		/**
		 * Gets the selected library.
		 *
		 * @return the selected library
		 */
		CQLLibraryDataSetObject getSelectedLibrary();

		/**
		 * Clear radio button selection.
		 */
		void clearRadioButtonSelection();

		void setSelectedLibraryObject(CQLLibraryDataSetObject selectedLibraryObject);
		
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
		ErrorMessageAlert getErrorMessageDisplay();

		/**
		 * Gets the save button.
		 *
		 * @return the save button
		 */
		HasClickHandlers getSaveButton();

		//HasValueChangeHandlers<Boolean> privateCheckbox();

		/**
		 * Sets the CQ library name.
		 *
		 * @param name the new CQ library name
		 */
		void setCQLibraryName(String name);

		/**
		 * Gets the zoom button.
		 *
		 * @return the zoom button
		 */
		CustomButton getZoomButton();

		/**
		 * Gets the search widget.
		 *
		 * @return the search widget
		 */
		SearchWidget getSearchWidget();

		//void setPrivate(boolean isPrivate);
		
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
			CQLLibraryVersionView versionDisplay, CQLLibraryDraftView draftDisplay, CQLLibraryShareView shareDisplay, CQLLibraryHistoryView historyDisplay) {
		this.cqlLibraryView = cqlLibraryView;
		this.detailDisplay = detailDisplay;
		this.versionDisplay = versionDisplay;
		this.draftDisplay = draftDisplay;
		this.shareDisplay = shareDisplay;
		this.historyDisplay = historyDisplay;
		addCQLLibraryViewHandlers();
		addDetailDisplayViewHandlers();
		addCQLLibrarySelectionHandlers();
		addMostRecentWidgetSelectionHandler();
		addVersionDisplayViewHandlers();
		//addDraftDisplayViewHandlers();
		addShareDisplayViewHandlers();
		addHistoryDisplayHandlers();
		addObserverHandlers();
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
				displayShare();
			}
			
			@Override
			public void onHistoryClicked(CQLLibraryDataSetObject result) {
				historyDisplay
				.setReturnToLinkText("<< Return to CQL Library");
				displayHistory(
						result.getId(),
						result.getCqlName());
			}

			@Override
			public void onCreateClicked(CQLLibraryDataSetObject object) {
				if(object.isDraftable()){
					CQLLibraryDataSetObject selectedLibrary = object;
					if (((selectedLibrary !=null) && (selectedLibrary.getId() != null))) {
						saveDraftFromVersion(selectedLibrary);
					}
				} else if (object.isVersionable()){
					versionDisplay.setSelectedLibraryObject(object);
					createVersion();
				} 
				
			}
			
		});
		
	}

	/**
	 * Adds the share display view handlers.
	 */
	private void addShareDisplayViewHandlers() {
		shareDisplay.getZoomButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				cqlLibraryView.getErrorMessageAlert().clearAlert();
				shareDisplay.getErrorMessageDisplay().clearAlert();
				isSearchVisibleOnVersion = !isSearchVisibleOnVersion;
				shareDisplay.getSearchWidget().setVisible(isSearchVisibleOnVersion);
			}
		});
		
		
		shareDisplay.getSearchWidget().getSearchButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchUsersForSharing();
			}
		});
		
		
		shareDisplay.getSearchWidget().getSearchInputFocusPanel().addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					shareDisplay.getSearchWidget().getSearchButton().click();
				}
			}
		});
		
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
						shareDisplay.getSearchWidget().getSearchInput().setText(""); 
						displaySearch();
					}
				});
				
			}
		});
		
		
		shareDisplay.getCancelButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				cqlLibraryView.clearSelections();
				/*cqlSharedDataSetObject = null;*/
				shareDisplay.getSearchWidget().getSearchInput().setText(""); 
				displaySearch();
			}
		});
		
	}

	/**
	 * Draft event Handlers are added here.
	 */
	/*private void addDraftDisplayViewHandlers() {
		draftDisplay.getZoomButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				cqlLibraryView.getErrorMessageAlert().clearAlert();
				isSearchVisibleOnDraft = !isSearchVisibleOnDraft;
				draftDisplay.getSearchWidget().setVisible(isSearchVisibleOnDraft);
			}
		});
		
		
		draftDisplay.getCancelButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				cqlLibraryView.clearSelections();
				draftDisplay.getSearchWidget().getSearchInput().setText("");
				displaySearch();
				
			}
		});
		
		
		draftDisplay.getSearchWidget().getSearchButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchLibrariesForDraft(draftDisplay.getSearchWidget().getSearchInput().getText());
			}
		});
		
		
		draftDisplay.getSearchWidget().getSearchInputFocusPanel().addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					draftDisplay.getSearchWidget().getSearchButton().click();
				}
			}
		});
		
		draftDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				draftDisplay.getErrorMessages().clearAlert();
				CQLLibraryDataSetObject selectedLibrary = draftDisplay.getSelectedLibrary();
				if (((selectedLibrary !=null) && (selectedLibrary.getId() != null))) {
					saveDraftFromVersion(selectedLibrary);
				} else {
					draftDisplay
					.getErrorMessages().createAlert(MatContext.get().getMessageDelegate().getERROR_LIBRARY_DRAFT());
				}
				
			}
		});
		
	}*/


	/**
	 * Save draft from version.
	 *
	 * @param selectedLibrary the selected library
	 */
	protected void saveDraftFromVersion(CQLLibraryDataSetObject selectedLibrary) {
		draftDisplay.getErrorMessages().clearAlert();
		draftDisplay.getZoomButton().setEnabled(false);
		((Button)draftDisplay.getSaveButton()).setEnabled(false);
		((Button)draftDisplay.getCancelButton()).setEnabled(false);
		MatContext.get().getCQLLibraryService().saveDraftFromVersion(selectedLibrary.getId(), new AsyncCallback<SaveCQLLibraryResult>() {

			@Override
			public void onFailure(Throwable caught) {
				draftDisplay.getErrorMessages().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				draftDisplay.getZoomButton().setEnabled(true);
				((Button)draftDisplay.getSaveButton()).setEnabled(true);
				((Button)draftDisplay.getCancelButton()).setEnabled(true);
				
			}

			@Override
			public void onSuccess(SaveCQLLibraryResult result) {
				draftDisplay.getZoomButton().setEnabled(true);
				((Button)draftDisplay.getSaveButton()).setEnabled(true);
				((Button)draftDisplay.getCancelButton()).setEnabled(true);
				draftDisplay.getSearchWidget().getSearchInput().setText("");
				if(result.isSuccess()){
					fireCQLLibrarySelectedEvent(result.getId(), result.getVersionStr(), result.getCqlLibraryName(), result.isEditable(), false,
							null,"","");
					fireCqlLibraryEditEvent();
					MatContext
					.get()
					.getAuditService()
					.recordCQLLibraryEvent(
							result.getId(),
							"Draft Created",
							"Draft created based on Version "
									+ result.getVersionStr(),
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
					draftDisplay.getErrorMessages().createAlert("Invalid data");
				}
				
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
		versionDisplay.getCancelButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				cqlLibraryView.clearSelections();
				versionDisplay.getErrorMessages().clearAlert();
				//versionDisplay.getSearchWidget().getSearchInput().setText("");
				displaySearch();
				
			}
		});
		
		
		/*versionDisplay.getZoomButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				cqlLibraryView.getErrorMessageAlert().clearAlert();
				versionDisplay.getErrorMessages().clearAlert();
				isSearchVisibleOnVersion = !isSearchVisibleOnVersion;
				versionDisplay.getSearchWidget().setVisible(isSearchVisibleOnVersion);
			}
		});
		
		versionDisplay.getSearchWidget().getSearchButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchLibrariesForVersion(versionDisplay.getSearchWidget().getSearchInput().getText());
			}
		});
		
		
		versionDisplay.getSearchWidget().getSearchInputFocusPanel().addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					versionDisplay.getSearchWidget().getSearchButton().click();
				}
			}
		});*/
		
		versionDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				versionDisplay.getErrorMessages().clearAlert();
				CQLLibraryDataSetObject selectedLibrary = versionDisplay.getSelectedLibrary();
				if (((selectedLibrary !=null) && (selectedLibrary.getId() != null))
						&& (versionDisplay.getMajorRadioButton().getValue() || versionDisplay
								.getMinorRadio().getValue())) {
					saveFinalizedVersion(selectedLibrary.getId(),
							versionDisplay.getMajorRadioButton().getValue(),
							selectedLibrary.getVersion());
				} else {
					versionDisplay
					.getErrorMessages().createAlert(MatContext.get().getMessageDelegate().getERROR_LIBRARY_VERSION());
				}
				
			}
		});
		
		
	}
	
	/**
	 * Method to Save Version of a Draft.
	 *
	 * @param libraryId the library id
	 * @param isMajor the is major
	 * @param version the version
	 */
	protected void saveFinalizedVersion(final String libraryId, Boolean isMajor, String version) {
		Mat.showLoadingMessage();
		versionDisplay.getErrorMessages().clearAlert();
		//versionDisplay.getZoomButton().setEnabled(false);
		((Button)versionDisplay.getSaveButton()).setEnabled(false);
		((Button)versionDisplay.getCancelButton()).setEnabled(false);
		MatContext.get().getCQLLibraryService().saveFinalizedVersion(libraryId, isMajor, version,
				new AsyncCallback<SaveCQLLibraryResult>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						Mat.hideLoadingMessage();
						versionDisplay.getErrorMessages()
								.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						//versionDisplay.getZoomButton().setEnabled(true);
						((Button) versionDisplay.getSaveButton()).setEnabled(true);
						((Button) versionDisplay.getCancelButton()).setEnabled(true);
					}

					@Override
					public void onSuccess(SaveCQLLibraryResult result) {
						Mat.hideLoadingMessage();
						//versionDisplay.getZoomButton().setEnabled(true);
						((Button) versionDisplay.getSaveButton()).setEnabled(true);
						((Button) versionDisplay.getCancelButton()).setEnabled(true);
						//versionDisplay.getSearchWidget().getSearchInput().setText("");
						if (result.isSuccess()) {
							cqlLibraryView.clearSelections();
							displaySearch();
							String versionStr = result.getVersionStr();
							MatContext.get().getAuditService().recordCQLLibraryEvent(libraryId, "CQL Library Versioned",
									"CQL Library Version " + versionStr + " created", false,
									new AsyncCallback<Boolean>() {

										@Override
										public void onFailure(Throwable caught) {

										}

										@Override
										public void onSuccess(Boolean result) {

										}
									});
						} else {
							if(result.getFailureReason() == ConstantMessages.INVALID_CQL_DATA){
								versionDisplay.getErrorMessages().createAlert(MatContext.get().getMessageDelegate().getNoVersionCreated());
							}
						}
					}

				});
		
	}

	/**
	 * Detail View Event Handlers.
	 */
	private void addDetailDisplayViewHandlers() {
		detailDisplay.getCancelButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				detailDisplay.resetAll();
				cqlLibraryView.clearSelections();
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
						CQLLibraryDataSetObject libraryDataSetObject = new CQLLibraryDataSetObject();
						libraryDataSetObject.setCqlName(detailDisplay.getNameField().getText());
						saveCqlLibrary(libraryDataSetObject);
					} else {
						detailDisplay.getErrorMessage().createAlert(MatContext.get().getMessageDelegate().getCqlStandAloneLibraryNameError());
					}
				}
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
		cqlLibraryView.getErrorMessageAlert().clearAlert();
		detailDisplay.getErrorMessage().clearAlert();
		MatContext.get().getEventBus().fireEvent(evt);
	}
	
	/**
	 * Fire measure edit event.
	 */
	private void fireCqlLibraryEditEvent() {
		CQLLibraryEditEvent evt = new CQLLibraryEditEvent();
		MatContext.get().getEventBus().fireEvent(evt);
	}
	
	
	/**
	 * CQL Library View Event Handlers are added here.
	 */
	private void addCQLLibraryViewHandlers() {
		cqlLibraryView.getAddNewFolderButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlLibraryView.getErrorMessageAlert().clearAlert();
			//	cqlLibraryView.getSearchFilterWidget().getSearchFilterDisclosurePanel().setOpen(false);
				isCreateNewItemWidgetVisible = !isCreateNewItemWidgetVisible;
				
				if(isSearchFilterVisible){
					isSearchFilterVisible = !isSearchFilterVisible;
					cqlLibraryView.getWidgetVP().clear();
				}
				
				if(isCreateNewItemWidgetVisible){
					cqlLibraryView.getWidgetVP().add(cqlLibraryView.getCreateNewItemWidget());
				} else {
					cqlLibraryView.getWidgetVP().clear();
				}

			}
		});
		
		cqlLibraryView.getZoomButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				cqlLibraryView.getErrorMessageAlert().clearAlert();
				//cqlLibraryView.getSearchFilterWidget().getSearchFilterDisclosurePanel().setOpen(false);
				if (isCreateNewItemWidgetVisible) {
					isCreateNewItemWidgetVisible = !isCreateNewItemWidgetVisible;
					cqlLibraryView.getWidgetVP().clear();
				}
				isSearchFilterVisible = !isSearchFilterVisible;
				
				if(isSearchFilterVisible){
					cqlLibraryView.getWidgetVP().add(cqlLibraryView.getSearchFilterWidget());
				} else {
					cqlLibraryView.getWidgetVP().clear();
				}
				
			}
		});

		cqlLibraryView.getCreateNewItemWidget().getCreateItemButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				if (cqlLibraryView.getSelectedOption().equalsIgnoreCase(ConstantMessages.CREATE_NEW_CQL)) {
					cqlLibraryView.getErrorMessageAlert().clearAlert();
					createNew();
				} /*else if (cqlLibraryView.getSelectedOption().equalsIgnoreCase(ConstantMessages.CREATE_NEW_CQL_DRAFT)) {
					cqlLibraryView.getErrorMessageAlert().clearAlert();
					createDraft();
				} else if (cqlLibraryView.getSelectedOption()
						.equalsIgnoreCase(ConstantMessages.CREATE_NEW_CQL_VERSION)) {
					cqlLibraryView.getErrorMessageAlert().clearAlert();
					  createVersion();
				}*/ else {
					cqlLibraryView.getErrorMessageAlert()
							.createAlert("Please select an option from the Create list box.");
				}

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
				cqlLibraryView.getErrorMessageAlert().clearAlert();
				int filter = cqlLibraryView.getSelectedFilter();
				search(cqlLibraryView.getSearchString().getValue(),filter, startIndex,Integer.MAX_VALUE);
			}
		});

	}

	/**
	 * Method is invoked When Option to Create Library Version of Draft is selected from CreateNewItemWidget.
	 */
	private void createVersion() {
		//searchLibrariesForVersion(versionDisplay.getSearchWidget().getSearchInput().getValue());
		versionDisplay.getErrorMessages().clearAlert();
		cqlLibraryView.getErrorMessageAlert().clearAlert();
		panel.getButtonPanel().clear();
		//panel.setButtonPanel(null, null,versionDisplay.getZoomButton(),"searchButton_cqlVersion");
		//versionDisplay.getSearchWidget().setVisible(true);
		//isSearchVisibleOnVersion = false;
		panel.setHeading("My CQL Library > Create CQL Library Version of Draft", "CQLLibrary");
		panel.setContent(versionDisplay.asWidget());
		Mat.focusSkipLists("CQLLibrary");
		versionDisplay.clearRadioButtonSelection();

	}
	/**
	 * Method is invoked When Option to Create Library Draft of Existing version is selected from CreateNewItemWidget.
	 */
	/*private void createDraft() {
		searchLibrariesForDraft(draftDisplay.getSearchWidget().getSearchInput().getText());
		draftDisplay.getErrorMessages().clearAlert();
		cqlLibraryView.getErrorMessageAlert().clearAlert();
		panel.getButtonPanel().clear();
		panel.setButtonPanel(null, null,draftDisplay.getZoomButton(),"searchButton_cqlDraft");
		draftDisplay.getSearchWidget().setVisible(false);
		isSearchVisibleOnDraft = false;
		panel.setHeading("My CQL Library > Create Draft of Existing Libraries", "CQLLibrary");
		panel.setContent(draftDisplay.asWidget());
		Mat.focusSkipLists("CQLLibrary");
	}*/
	
	/**
	 * Display share.
	 */
	private void displayShare() {
		searchUsersForSharing();
		shareDisplay.setCQLibraryName(cqlSharedDataSetObject.getCqlName());
		panel.getButtonPanel().clear();
		panel.setButtonPanel(null, null,shareDisplay.getZoomButton(),"searchButton_cqlShare");
		shareDisplay.getSearchWidget().setVisible(false);
		isSearchVisibleOnVersion = false;
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
		panel.setHeading(heading, "CQL Library");
		searchHistory(cqlLibraryId, startIndex, pageSize);
		historyDisplay.setCQLLibraryId(cqlLibraryId);
		historyDisplay.setCQLLibraryName(cqlLibraryName);
		panel.setContent(historyDisplay.asWidget());
		Mat.focusSkipLists("MeasureLibrary");
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
	 * This method returns all libraries available for versioning.
	 *
	 * @param searchText the search text
	 */
	/*private void searchLibrariesForVersion(String searchText) {
		final String lastSearchText = (searchText != null) ? searchText.trim() : null;
		versionDisplay.getErrorMessages().clearAlert();
		showSearchingBusy(true);
		//versionDisplay.getZoomButton().setEnabled(false);
		((Button)versionDisplay.getSaveButton()).setEnabled(false);
		((Button)versionDisplay.getCancelButton()).setEnabled(false);
		MatContext.get().getCQLLibraryService().searchForVersion(lastSearchText, new AsyncCallback<SaveCQLLibraryResult>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				showSearchingBusy(false);
			//	versionDisplay.getZoomButton().setEnabled(true);
				((Button)versionDisplay.getSaveButton()).setEnabled(true);
				((Button)versionDisplay.getCancelButton()).setEnabled(true);
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}

			@Override
			public void onSuccess(SaveCQLLibraryResult result) {
				
				if ((result.getResultsTotal() == 0)
						&& !lastSearchText.isEmpty()) {
					versionDisplay.getErrorMessages().createAlert(MatContext.get().getMessageDelegate().getNoLibrarues());
				} 
				SearchResultUpdate sru = new SearchResultUpdate();
				//sru.update(result, versionDisplay.getSearchWidget().getSearchInput(), lastSearchText);
				versionDisplay.buildDataTable(result);
				//versionDisplay.getZoomButton().setEnabled(true);
				((Button)versionDisplay.getSaveButton()).setEnabled(true);
				((Button)versionDisplay.getCancelButton()).setEnabled(true);
				showSearchingBusy(false);
				
			}
		});
		
	}*/

	
	/**
	 * Search libraries for draft.
	 *
	 * @param searchText the search text
	 */
	/*private void searchLibrariesForDraft(String searchText) {
		final String lastSearchText = (searchText != null) ? searchText.trim() : null;
		draftDisplay.getErrorMessages().clearAlert();
		showSearchingBusy(true);
		draftDisplay.getZoomButton().setEnabled(false);
		((Button)draftDisplay.getSaveButton()).setEnabled(false);
		((Button)draftDisplay.getCancelButton()).setEnabled(false);
		MatContext.get().getCQLLibraryService().searchForDraft(lastSearchText, new AsyncCallback<SaveCQLLibraryResult>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				showSearchingBusy(false);
				draftDisplay.getZoomButton().setEnabled(true);
				((Button)draftDisplay.getSaveButton()).setEnabled(true);
				((Button)draftDisplay.getCancelButton()).setEnabled(true);
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}

			@Override
			public void onSuccess(SaveCQLLibraryResult result) {
				if ((result.getResultsTotal() == 0)
						&& !lastSearchText.isEmpty()) {
					draftDisplay.getErrorMessages().createAlert(MatContext.get().getMessageDelegate().getNoLibrarues());
				} 
				SearchResultUpdate sru = new SearchResultUpdate();
				sru.update(result, draftDisplay.getSearchWidget().getSearchInput(), lastSearchText);
				draftDisplay.buildDataTable(result);
				draftDisplay.getZoomButton().setEnabled(true);
				((Button)draftDisplay.getSaveButton()).setEnabled(true);
				((Button)draftDisplay.getCancelButton()).setEnabled(true);
				showSearchingBusy(false);
			}
		});
		
	}*/
	
	
	/**
	 * Search users for sharing.
	 */
	private void searchUsersForSharing(){
		String searchText = shareDisplay.getSearchWidget().getSearchInput().getText();
	    final String lastSearchText = (searchText != null) ? searchText.trim() : null;
		shareDisplay.getErrorMessageDisplay().clearAlert();
		showSearchingBusy(true);
		shareDisplay.getZoomButton().setEnabled(false);
		((Button)shareDisplay.getSaveButton()).setEnabled(false);
		((Button)shareDisplay.getCancelButton()).setEnabled(false);
		
		MatContext.get().getCQLLibraryService().getUserShareInfo(cqlSharedDataSetObject.getId(),
				lastSearchText, new AsyncCallback<SaveCQLLibraryResult>() {
			
			@Override
			public void onSuccess(SaveCQLLibraryResult result) {
				
				if ((result.getResultsTotal() == 0)
						&& !lastSearchText.isEmpty()) {
					shareDisplay.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getNoUsersReturned());
				} 
				saveCQLLibraryResult = result;
				SearchResultUpdate sru = new SearchResultUpdate();
				sru.update(result, shareDisplay.getSearchWidget().getSearchInput(), lastSearchText);
				shareDisplay.buildCQLLibraryShareTable(result.getCqlLibraryShareDTOs());
				shareDisplay.getZoomButton().setEnabled(true);
				((Button)shareDisplay.getSaveButton()).setEnabled(true);
				((Button)shareDisplay.getCancelButton()).setEnabled(true);
				showSearchingBusy(false);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				showSearchingBusy(false);
				shareDisplay.getZoomButton().setEnabled(true);
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
		cqlModel = new CQLModel();
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
		cqlLibraryView.getErrorMessageAlert().clearAlert();
		String heading = "CQL Library";
		panel.setHeading(heading, "CQLLibrary");
		setSubSkipEmbeddedLink("CQLSearchView_mainPanel");
		FlowPanel fp = new FlowPanel();
		fp.getElement().setId("fp_FlowPanel_CQL");
		isCreateNewItemWidgetVisible = false;
		//cqlLibraryView.getCreateNewItemWidget().setVisible(false);
		int filter = cqlLibraryView.getSelectedFilter();
		search(cqlLibraryView.getSearchString().getValue(), filter, 1,Integer.MAX_VALUE);
		searchRecentLibraries();
		panel.getButtonPanel().clear();
		panel.setButtonPanel(cqlLibraryView.getAddNewFolderButton(),"createElement_cqlLib", cqlLibraryView.getZoomButton(),"searchButton_cqlLib");
		
		fp.add(cqlLibraryView.asWidget());
		isSearchFilterVisible = true;
		panel.setContent(fp);
		Mat.focusSkipLists("CQLLibrary");
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
				cqlLibraryView.getErrorMessageAlert().clearAlert();
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
		cqlLibraryView.getErrorMessageAlert().clearAlert();
		draftDisplay.getSearchWidget().getSearchInput().setText("");
		//versionDisplay.getSearchWidget().getSearchInput().setText("");
		shareDisplay.getSearchWidget().getSearchInput().setText("");
	}

	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		
		cqlLibraryView.buildDefaultView();
		cqlLibraryView.clearSelections();
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
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
	}

}
