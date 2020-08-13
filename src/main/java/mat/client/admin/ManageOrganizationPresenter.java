package mat.client.admin;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.admin.ManageOrganizationSearchModel.Result;
import mat.client.admin.ManageOrganizationView.Observer;
import mat.client.admin.service.SaveUpdateOrganizationResult;
import mat.client.cqlworkspace.DeleteConfirmationDialogBox;
import mat.client.shared.MatContext;
import mat.client.shared.MessageAlert;
import mat.client.shared.search.SearchResultUpdate;
import mat.client.shared.search.SearchResults;
import mat.client.util.MatTextBox;
import mat.shared.AdminManageOrganizationModelValidator;
import org.gwtbootstrap3.client.ui.Button;

import java.util.ArrayList;
import java.util.List;

/** The Class ManageOrganizationPresenter. */
public class ManageOrganizationPresenter implements MatPresenter {
	/** The Interface DetailDisplay. */
	public static interface DetailDisplay {
		/** As widget.
		 * @return the widget */
		Widget asWidget();
		/** Gets the cancel button.
		 * @return the cancel button */
		HasClickHandlers getCancelButton();
		/** Gets the error message display.
		 * @return the error message display */
		MessageAlert getErrorMessageDisplay();
		/** Gets the oid.
		 * @return the oid */
		HasValue<String> getOid();
		/** Gets the organization.
		 * @return the organization */
		HasValue<String> getOrganization();
		/** Gets the save button.
		 * @return the save button */
		HasClickHandlers getSaveButton();
		/** Gets the success message display.
		 * @return the success message display */
		MessageAlert getSuccessMessageDisplay();
		/** Sets the title.
		 * @param title the new title */
		void setTitle(String title);
	}
	/** The Interface SearchDisplay. */
	public static interface SearchDisplay {
		/**
		 * As widget.
		 *
		 * @return the widget
		 */
		Widget asWidget();
		/** Builds the data table.
		 * @param results the results */
		void buildDataTable(SearchResults<ManageOrganizationSearchModel.Result> results);
		/** Gets the creates the new button.
		 * @return the creates the new button */
		HasClickHandlers getCreateNewButton();
		/**
		 * Gets the search button.
		 *
		 * @return the search button
		 */
		HasClickHandlers getSearchButton();
		/**
		 * Gets the search string.
		 *
		 * @return the search string
		 */
		HasValue<String> getSearchString();
		/** Gets the select id for edit tool.
		 * @return the select id for edit tool */
		HasSelectionHandlers<ManageOrganizationSearchModel.Result> getSelectIdForEditTool();
		
		/**
		 * Sets the observer.
		 *
		 * @param observer the new observer
		 */
		void setObserver(Observer observer);
		
		/**
		 * Gets the success message display.
		 *
		 * @return the success message display
		 */
		MessageAlert getSuccessMessageDisplay();
		
		/**
		 * Gets the error message display.
		 *
		 * @return the error message display
		 */
		MessageAlert getErrorMessageDisplay();
		
		/**
		 * Sets the title.
		 *
		 * @param title the new title
		 */
		void setTitle(String title);
		
	
	}
	/** The current details. */
	private ManageOrganizationDetailModel currentOrganizationDetails;
	/** The detail display. */
	private DetailDisplay detailDisplay;
	/** The last search key. */
	private String lastSearchKey;
	/** The panel. */
	private SimplePanel panel = new SimplePanel();
	/** The search display. */
	private SearchDisplay searchDisplay;
	
	private boolean isOrgDetailsModified = false;
	/** Instantiates a new manage Organizations presenter.
	 * @param sDisplayArg the SearchDisplay
	 * @param dDisplayArg the DetailDisplay */
	public ManageOrganizationPresenter(SearchDisplay sDisplayArg, DetailDisplay dDisplayArg) {
		searchDisplay = sDisplayArg;
		detailDisplay = dDisplayArg;
		displaySearch("");
		searchDisplay.getCreateNewButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				isOrgDetailsModified = false;
				searchDisplay.getErrorMessageDisplay().clearAlert();
				searchDisplay.getSuccessMessageDisplay().clearAlert();
				createNewOrganization();
			}
		});
		
		MatTextBox searchWidget = (MatTextBox) (searchDisplay.getSearchString());
		searchWidget.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				searchDisplay.getErrorMessageDisplay().clearAlert();
				searchDisplay.getSuccessMessageDisplay().clearAlert();
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					((Button) searchDisplay.getSearchButton()).click();
				}
			}
		});
		detailDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getErrorMessageDisplay().clearAlert();
				searchDisplay.getSuccessMessageDisplay().clearAlert();
				if(isOrgDetailsModified) {
					updateOrganization();
				} else {
					addOrganization();
				}
			}
		});
		detailDisplay.getCancelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				isOrgDetailsModified = false;
				searchDisplay.getErrorMessageDisplay().clearAlert();
				searchDisplay.getSuccessMessageDisplay().clearAlert();
				displaySearch(lastSearchKey);
			}
		});
		searchDisplay.getSearchButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getErrorMessageDisplay().clearAlert();
				searchDisplay.getSuccessMessageDisplay().clearAlert();
				String key = searchDisplay.getSearchString().getValue();
				search(key);
			}
		});
		searchDisplay.getSelectIdForEditTool().addSelectionHandler(new SelectionHandler<ManageOrganizationSearchModel.Result>() {
			@Override
			public void onSelection(SelectionEvent<ManageOrganizationSearchModel.Result> event) {
				isOrgDetailsModified = true;
				searchDisplay.getErrorMessageDisplay().clearAlert();
				searchDisplay.getSuccessMessageDisplay().clearAlert();
				edit(event.getSelectedItem().getOid());
			}
		});
	}
	/*
	 * (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
		isOrgDetailsModified = false;
		searchDisplay.getSearchString().setValue("");
	}
	/*
	 * (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		displaySearch("");
		Mat.focusSkipLists("Manage Organizations");
	}
	/** Creates the new. */
	private void createNewOrganization() {
		detailDisplay.setTitle("Add an Organization");
		currentOrganizationDetails = new ManageOrganizationDetailModel();
		displayDetail();
	}
	/** Display detail. */
	private void displayDetail() {
		resetMessages();
		setOrganizationDetailsToView();
		panel.clear();
		panel.add(detailDisplay.asWidget());
		Mat.focusSkipLists("Manage Organizations");
	}
	/** Display search. */
	private void displaySearch(String organization) {
		panel.clear();
		panel.add(searchDisplay.asWidget());
		searchDisplay.getErrorMessageDisplay().clearAlert();
		searchDisplay.getSuccessMessageDisplay().clearAlert();
		searchDisplay.setTitle("");
		search(organization);
	}
	/** Edits the.
	 * @param key the key */
	private void edit(String key) {
		detailDisplay.setTitle("Update an Organization");
		MatContext.get().getAdminService().getOrganization(key,
				new AsyncCallback<ManageOrganizationDetailModel>() {
			@Override
			public void onFailure(Throwable caught) {
				detailDisplay.getErrorMessageDisplay()
				.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null,
						null, "Unhandled Exception: " + caught.getLocalizedMessage(), 0);
			}
			@Override
			public void onSuccess(ManageOrganizationDetailModel result) {
				currentOrganizationDetails = result;
				displayDetail();
			}
		});
	}
	/*
	 * (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		return panel;
	}
	/** Gets the widget with heading.
	 * @param widget the widget
	 * @param heading the heading
	 * @return the widget with heading */
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

	/** Reset messages. */
	private void resetMessages() {
		detailDisplay.getErrorMessageDisplay().clearAlert();
		detailDisplay.getSuccessMessageDisplay().clearAlert();
	}
	/** Search.
	 * @param key the key
	 */
	private void search(String key) {
		lastSearchKey = key;
		showSearchingBusy(true);
		MatContext.get().getAdminService().searchOrganization(key, new AsyncCallback<ManageOrganizationSearchModel>() {
			@Override
			public void onFailure(Throwable caught) {
				detailDisplay.getErrorMessageDisplay()
				.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null,
						null, "Unhandled Exception: " + caught.getLocalizedMessage(), 0);
				showSearchingBusy(false);
			}
			@Override
			public void onSuccess(ManageOrganizationSearchModel result) {
				SearchResultUpdate sru = new SearchResultUpdate();
				sru.update(result, (MatTextBox) searchDisplay.getSearchString(), lastSearchKey);
				
				searchDisplay.setObserver(new Observer() {
					@Override
					public void onDeleteClicked(Result result) {
						if(!result.isUsed()){
							deleteOrganization(result);
						}
						
					}
				});
				searchDisplay.buildDataTable(result);
				showSearchingBusy(false);
				Mat.focusSkipLists("Manage Organizations");
			}
		});
	}
	
	private void deleteOrganization(Result result) {
		
		searchDisplay.getErrorMessageDisplay().clearAlert();
		searchDisplay.getSuccessMessageDisplay().clearAlert();
		
		DeleteConfirmationDialogBox deleteConfirmationDialogBox = new DeleteConfirmationDialogBox();
		deleteConfirmationDialogBox.getMessageAlert().
			createAlert("You have selected to delete organization " + 
					(result.getOrgName().length()>60 ? result.getOrgName().substring(0, 59) : result.getOrgName()) + ". Please confirm that you want to remove this organization permanently.");
		
		deleteConfirmationDialogBox.show();
		
		deleteConfirmationDialogBox.getYesButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				MatContext.get().getAdminService().deleteOrganization(result, new AsyncCallback<Void>() {
					
					@Override
					public void onFailure(Throwable caught) {
						searchDisplay.getErrorMessageDisplay().createAlert("Organization cannot be deleted.");
					}
					@Override
					public void onSuccess(Void res) {
						displaySearch(lastSearchKey);
						searchDisplay.getSuccessMessageDisplay().createAlert(result.getOrgName()+" successfully deleted.");
					}
				});
				
			}
		});
		
				
		deleteConfirmationDialogBox.getNoButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				deleteConfirmationDialogBox.hide();
			}
		});
		
	}
	/** Sets the user details to view. */
	private void setOrganizationDetailsToView() {
		detailDisplay.getOid().setValue(currentOrganizationDetails.getOid());
		detailDisplay.getOrganization().setValue(currentOrganizationDetails.getOrganization());
	}
	/** Show searching busy.
	 * @param busy the busy */
	private void showSearchingBusy(boolean busy) {
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
		((Button) searchDisplay.getSearchButton()).setEnabled(!busy);
		((MatTextBox) (searchDisplay.getSearchString())).setEnabled(!busy);
	}
	
	private void updateOrganization() {
		resetMessages();
		ManageOrganizationDetailModel updatedOrganizationDetailModel = buildOrganizationDetailModelFromView();
		AdminManageOrganizationModelValidator organizationValidator = new AdminManageOrganizationModelValidator();
		if(organizationValidator.isManageOrganizationDetailModelValid(updatedOrganizationDetailModel)) {
			MatContext.get().getAdminService().updateOrganization(currentOrganizationDetails.getId(), updatedOrganizationDetailModel, new AsyncCallback<SaveUpdateOrganizationResult>() {
				@Override
				public void onFailure(Throwable caught) {
					detailDisplay.getErrorMessageDisplay().createAlert(caught.getLocalizedMessage());
					isOrgDetailsModified = false;
				}
				@Override
				public void onSuccess(SaveUpdateOrganizationResult result) {
					if(result.isSuccess()) {
						isOrgDetailsModified = false;
						currentOrganizationDetails = updatedOrganizationDetailModel;
						setOrganizationDetailsToView();
						displaySearch(lastSearchKey);
						searchDisplay.getSuccessMessageDisplay().createAlert(MatContext.get()
								.getMessageDelegate().getORGANIZATION_MODIFIED_SUCCESS_MESSAGE());
					} else {
						displayErrorMessages(result);
					}
				}
			});
		} else {
			detailDisplay.getErrorMessageDisplay().createAlert(organizationValidator.getValidationErrors(updatedOrganizationDetailModel));
		}
	}
	
	private void addOrganization() {
		resetMessages();
		ManageOrganizationDetailModel updatedOrganizationDetailModel = buildOrganizationDetailModelFromView();
		AdminManageOrganizationModelValidator organizationValidator = new AdminManageOrganizationModelValidator();
		if(organizationValidator.isManageOrganizationDetailModelValid(updatedOrganizationDetailModel)) {
			MatContext.get().getAdminService().saveOrganization(updatedOrganizationDetailModel, new AsyncCallback<SaveUpdateOrganizationResult>() {
				@Override
				public void onFailure(Throwable caught) {
					detailDisplay.getErrorMessageDisplay().createAlert(caught.getLocalizedMessage());
					isOrgDetailsModified = false;
				}
				@Override
				public void onSuccess(SaveUpdateOrganizationResult result) {
					if(result.isSuccess()) {
						isOrgDetailsModified = false;
						currentOrganizationDetails = updatedOrganizationDetailModel;
						setOrganizationDetailsToView();
						displaySearch(lastSearchKey);
						searchDisplay.getSuccessMessageDisplay().createAlert(MatContext.get()
								.getMessageDelegate().getORGANIZATION_SUCCESS_MESSAGE());
					} else {
						displayErrorMessages(result);
					}
				}
			});
		} else {
			detailDisplay.getErrorMessageDisplay().createAlert(organizationValidator.getValidationErrors(updatedOrganizationDetailModel));
		}
	}
	
	protected void displayErrorMessages(SaveUpdateOrganizationResult result) {
		List<String> messages = new ArrayList<String>();
		switch (result.getFailureReason()) {
			case
			SaveUpdateOrganizationResult.OID_NOT_UNIQUE:
				messages.add(MatContext.get().getMessageDelegate().getOIDExistsMessage());
				break;
			case
			SaveUpdateOrganizationResult.SERVER_SIDE_VALIDATION:
				messages = result.getMessages();
				break;
			default:
				messages.add(MatContext.get().getMessageDelegate()
						.getUnknownErrorMessage(result.getFailureReason()));
		}
		detailDisplay.getErrorMessageDisplay().createAlert(messages);
	}
	
	private ManageOrganizationDetailModel buildOrganizationDetailModelFromView() {
		ManageOrganizationDetailModel manageOrganizationDetailModel = new ManageOrganizationDetailModel();
		manageOrganizationDetailModel.setOid(detailDisplay.getOid().getValue());
		manageOrganizationDetailModel.setOrganization(detailDisplay.getOrganization().getValue());
		manageOrganizationDetailModel.scrubForMarkUp();
		return manageOrganizationDetailModel;
	}
}
