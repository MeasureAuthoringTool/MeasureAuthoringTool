package mat.client.admin;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.admin.ManageOrganizationSearchModel.Result;
import mat.client.admin.service.SaveUpdateUserResult;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.InformationMessageDisplayInterface;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.search.SearchResultUpdate;
import mat.client.shared.search.SearchResults;
import mat.client.util.ClientConstants;
import mat.shared.AdminManageUserModelValidator;
import mat.shared.InCorrectUserRoleException;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class ManageUsersPresenter.
 */
public class ManageUsersPresenter implements MatPresenter {
	
	/**
	 * The Interface SearchDisplay.
	 */
	public static interface SearchDisplay {
		
		/**
		 * Gets the creates the new button.
		 * 
		 * @return the creates the new button
		 */
		HasClickHandlers getCreateNewButton();
		
		/**
		 * Gets the generate csv file button.
		 * 
		 * @return the generate csv file button
		 */
		/*Button getGenerateCSVFileButton();*/
		
		/**
		 * Gets the select id for edit tool.
		 * 
		 * @return the select id for edit tool
		 */
		HasSelectionHandlers<ManageUsersSearchModel.Result> getSelectIdForEditTool();
		
		/**
		 * Builds the data table.
		 * 
		 * @param results
		 *            the results
		 */
		void buildDataTable(SearchResults<ManageUsersSearchModel.Result> results);
		
		/**
		 * As widget.
		 *
		 * @return the widget
		 */
		Widget asWidget();
		
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
	}
	
	/**
	 * The Interface DetailDisplay.
	 */
	public static interface DetailDisplay {
		
		/**
		 * As widget.
		 * 
		 * @return the widget
		 */
		Widget asWidget();
		
		/**
		 * Gets the save button.
		 * 
		 * @return the save button
		 */
		HasClickHandlers getSaveButton();
		
		/**
		 * Gets the cancel button.
		 * 
		 * @return the cancel button
		 */
		HasClickHandlers getCancelButton();
		//public HasClickHandlers getDeleteUserButton();
		
		/**
		 * Gets the first name.
		 * 
		 * @return the first name
		 */
		HasValue<String> getFirstName();
		
		/**
		 * Gets the last name.
		 * 
		 * @return the last name
		 */
		HasValue<String> getLastName();
		
		/**
		 * Gets the middle initial.
		 * 
		 * @return the middle initial
		 */
		HasValue<String> getMiddleInitial();
		
		/**
		 * Gets the login id.
		 * 
		 * @return the login id
		 */
		Label getLoginId();
		
		/**
		 * Gets the title.
		 * 
		 * @return the title
		 */
		HasValue<String> getTitle();
		
		/**
		 * Gets the email address.
		 * 
		 * @return the email address
		 */
		HasValue<String> getEmailAddress();
		
		/**
		 * Gets the phone number.
		 * 
		 * @return the phone number
		 */
		HasValue<String> getPhoneNumber();
		
		
		/**
		 * Gets the organization list box.
		 *
		 * @return the organization list box
		 */
		ListBoxMVP getOrganizationListBox();
		
		/**
		 * Populate organizations.
		 *
		 * @param organizations the organizations
		 */
		void populateOrganizations(List<Result> organizations);
		
		/**
		 * Sets the organizations map.
		 *
		 * @param organizationsMap the organizations map
		 */
		void setOrganizationsMap(Map<String, Result> organizationsMap);
		
		/**
		 * Gets the organizations map.
		 *
		 * @return the organizations map
		 */
		Map<String, Result> getOrganizationsMap();
		/**
		 * Gets the role.
		 * 
		 * @return the role
		 */
		HasValue<String> getRole();
		
		/**
		 * Gets the oid.
		 * 
		 * @return the oid
		 */
		TextBox getOid();
		//public HasValue<String> getRootOid();
		
		/**
		 * Gets the checks if is active.
		 * 
		 * @return the checks if is active
		 */
		HasValue<Boolean> getIsActive();
		
		/**
		 * Gets the checks if is revoked.
		 * 
		 * @return the checks if is revoked
		 */
		HasValue<Boolean> getIsRevoked();
		
		/**
		 * Gets the checks if is org user.
		 * 
		 * @return the checks if is org user
		 */
		HasValue<Boolean> getIsOrgUser();
		
		/**
		 * Gets the error message display.
		 * 
		 * @return the error message display
		 */
		ErrorMessageDisplayInterface getErrorMessageDisplay();
		
		/**
		 * Gets the success message display.
		 * 
		 * @return the success message display
		 */
		SuccessMessageDisplayInterface getSuccessMessageDisplay();
		
		/**
		 * Sets the user is active editable.
		 * 
		 * @param b
		 *            the new user is active editable
		 */
		void setUserIsActiveEditable(boolean b);
		//public void setUserIsDeletable(boolean b);
		/**
		 * Sets the user locked.
		 * 
		 * @param b
		 *            the new user locked
		 */
		void setUserLocked(boolean b);
		
		/**
		 * Sets the show revoked status.
		 * 
		 * @param b
		 *            the new show revoked status
		 */
		void setShowRevokedStatus(boolean b);
		
		/**
		 * Sets the show unlock option.
		 * 
		 * @param b
		 *            the new show unlock option
		 */
		void setShowUnlockOption(boolean b);
		
		/**
		 * Gets the reset password button.
		 * 
		 * @return the reset password button
		 */
		HasClickHandlers getResetPasswordButton();
		
		/**
		 * Sets the title.
		 * 
		 * @param title
		 *            the new title
		 */
		void setTitle(String title);
		
		//Label getExpLabel();
		
		InformationMessageDisplayInterface getInformationMessageDisplay();
	}
	
	/** The panel. */
	private SimplePanel panel = new SimplePanel();
	
	/** The search display. */
	private SearchDisplay searchDisplay;
	
	/** The detail display. */
	private DetailDisplay detailDisplay;
	
	/** The current details. */
	private ManageUsersDetailModel currentDetails;
	
	/** The start index. */
	private int startIndex = 1;
	
	/** The last search key. */
	private String lastSearchKey;
	
	/**
	 * Instantiates a new manage users presenter.
	 * 
	 * @param sDisplayArg
	 *            the s display arg
	 * @param dDisplayArg
	 *            the d display arg
	 */
	public ManageUsersPresenter(SearchDisplay sDisplayArg, DetailDisplay dDisplayArg) {
		searchDisplay = sDisplayArg;
		detailDisplay = dDisplayArg;
		displaySearch();
		
		
		searchDisplay.getSelectIdForEditTool().addSelectionHandler(new SelectionHandler<ManageUsersSearchModel.Result>() {
			@Override
			public void onSelection(SelectionEvent<ManageUsersSearchModel.Result> event) {
				edit(event.getSelectedItem().getKey());
			}
		});
		
		searchDisplay.getCreateNewButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				createNew();
			}
		});
		TextBox searchWidget = (TextBox) (searchDisplay.getSearchString());
		searchWidget.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					((Button) searchDisplay.getSearchButton()).click();
				}
			}
		});
		
		detailDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				update();
			}
		});
		
		detailDisplay.getCancelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				displaySearch();
			}
		});
		
		detailDisplay.getResetPasswordButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				resetMessages();
				MatContext.get().getAdminService().resetUserPassword(currentDetails.getUserID(),
						new AsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
						detailDisplay.getSuccessMessageDisplay()
						.setMessage("Temporary Password E-mail has been sent.");
					}
					
					@Override
					public void onFailure(Throwable caught) {
						detailDisplay.getErrorMessageDisplay().setMessage(
								MatContext.get().getMessageDelegate().getGenericErrorMessage());
						MatContext.get().recordTransactionEvent(null, null, null,
								"Unhandled Exception: " + caught.getLocalizedMessage(), 0);
					}
				});
			}
		});
		searchDisplay.getSearchButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String key = searchDisplay.getSearchString().getValue();
				search(key);
			}
		});
	}
	
	/**
	 * Display search.
	 */
	private void displaySearch() {
		panel.clear();
		panel.add(searchDisplay.asWidget());
		search("");
	}
	
	/**
	 * Display detail.
	 */
	private void displayDetail() {
		resetMessages();
		populateOrganizations();
		panel.clear();
		panel.add(detailDisplay.asWidget());
		Mat.focusSkipLists("Manage Users");
	}
	
	/**
	 * Populate organizations in OrganizationListBox.
	 */
	private void populateOrganizations() {
		MatContext.get().getAdminService().getAllOrganizations(new AsyncCallback<ManageOrganizationSearchModel>() {
			@Override
			public void onFailure(Throwable caught) {
				
			}
			@Override
			public void onSuccess(ManageOrganizationSearchModel model) {
				List<Result> results = model.getData();
				detailDisplay.populateOrganizations(results);
				
				Map<String, Result> orgMap = new HashMap<String, Result>();
				for(Result organization : results) {
					orgMap.put(organization.getId(), organization);
				}
				detailDisplay.setOrganizationsMap(orgMap);
				
				setUserDetailsToView();
			}
		});
	}
	
	/**
	 * Update.
	 */
	private void update() {
		resetMessages();
		updateUserDetailsFromView();
		detailDisplay.getErrorMessageDisplay().clear();
		detailDisplay.getSuccessMessageDisplay().clear();
		if (isValid(currentDetails)) {
			MatContext.get().getAdminService().saveUpdateUser(currentDetails, new AsyncCallback<SaveUpdateUserResult>() {
				@Override
				public void onSuccess(SaveUpdateUserResult result) {
					if (result.isSuccess()) {
						//displaySearch();
						detailDisplay.getSuccessMessageDisplay().setMessage(MatContext.get()
								.getMessageDelegate().getUSER_SUCCESS_MESSAGE());
						detailDisplay.getFirstName().setValue(currentDetails.getFirstName());
						detailDisplay.getLastName().setValue(currentDetails.getLastName());
						detailDisplay.getTitle().setValue(currentDetails.getTitle());
						detailDisplay.getMiddleInitial().setValue(currentDetails.getMiddleInitial());
						detailDisplay.getEmailAddress().setValue(currentDetails.getEmailAddress());
						detailDisplay.getPhoneNumber().setValue(currentDetails.getPhoneNumber());
						detailDisplay.getOid().setValue(currentDetails.getOid());
					} else {
						List<String> messages = new ArrayList<String>();
						switch(result.getFailureReason()) {
							case SaveUpdateUserResult.ID_NOT_UNIQUE:
								messages.add(MatContext.get().getMessageDelegate()
										.getEmailAlreadyExistsMessage());
								break;
							case SaveUpdateUserResult.SERVER_SIDE_VALIDATION:
								messages = result.getMessages();
								break;
							default:
								messages.add(MatContext.get().getMessageDelegate()
										.getUnknownErrorMessage(result.getFailureReason()));
						}
						detailDisplay.getErrorMessageDisplay().setMessages(messages);
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					detailDisplay.getErrorMessageDisplay().setMessage(caught.getLocalizedMessage());
				}
			});
		}
	}
	
	/**
	 * Reset messages.
	 */
	private void resetMessages() {
		detailDisplay.getErrorMessageDisplay().clear();
		detailDisplay.getSuccessMessageDisplay().clear();
	}
	
	/**
	 * Creates the new.
	 */
	private void createNew() {
		detailDisplay.setTitle("Add a User");
		detailDisplay.getInformationMessageDisplay().clear();
		currentDetails = new ManageUsersDetailModel();
		displayDetail();
	}
	
	/**
	 * Generate csv of active user emails.
	 */
	/*private void generateCSVOfActiveUserEmails() {
		String url = GWT.getModuleBaseURL() + "export?format=exportActiveNonAdminUsersCSV";
		Window.open(url + "&type=save", "_self", "");
	}*/
	
	/**
	 * Edits the.
	 * 
	 * @param name
	 *            the name
	 */
	private void edit(String name) {
		detailDisplay.setTitle("Update a User");
		MatContext.get().getAdminService().getUser(name, new AsyncCallback<ManageUsersDetailModel>() {
			@Override
			public void onSuccess(ManageUsersDetailModel result) {
				currentDetails = result;
				displayDetail();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				detailDisplay.getErrorMessageDisplay()
				.setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null,
						"Unhandled Exception: " + caught.getLocalizedMessage(), 0);
			}
		});
	}
	
	/**
	 * Search.
	 * 
	 * @param key
	 *            the key
	 */
	private void search(String key) {
		lastSearchKey = key;
		showSearchingBusy(true);
		MatContext.get().getAdminService().searchUsers(key, new AsyncCallback<ManageUsersSearchModel>() {
			@Override
			public void onSuccess(ManageUsersSearchModel result) {
				SearchResultUpdate sru = new SearchResultUpdate();
				sru.update(result, (TextBox) searchDisplay.getSearchString(), lastSearchKey);
				sru = null;
				searchDisplay.buildDataTable(result);
				showSearchingBusy(false);
				Mat.focusSkipLists("Manage Users");
			}
			@Override
			public void onFailure(Throwable caught) {
				detailDisplay.getErrorMessageDisplay()
				.setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null,
						"Unhandled Exception: " + caught.getLocalizedMessage(), 0);
				showSearchingBusy(false);
				if (caught instanceof InCorrectUserRoleException) {
					callSignOut();
				}
			}
		});
	}
	
	/**
	 * Call sign out.
	 */
	private void callSignOut() {
		MatContext.get().getLoginService().signOut(new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable arg0) {
				redirectToLogin();
			}
			
			@Override
			public void onSuccess(Void arg0) {
				redirectToLogin();
			}
		});
	}
	
	/**
	 * Redirect to login.
	 */
	private void redirectToLogin() {
		/*
		 * Added a timer to have a delay before redirect since
		 * this was causing the firefox javascript exception.
		 */
		final Timer timer = new Timer() {
			@Override
			public void run() {
				MatContext.get().redirectToHtmlPage(ClientConstants.HTML_LOGIN);
			}
		};
		timer.schedule(1000);
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
		((Button) searchDisplay.getSearchButton()).setEnabled(!busy);
		((TextBox) (searchDisplay.getSearchString())).setEnabled(!busy);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		return panel;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		displaySearch();
		Mat.focusSkipLists("Manage Users");
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
	}
	
	/**
	 * Sets the user details to view.
	 */
	private void setUserDetailsToView() {
		detailDisplay.getFirstName().setValue(currentDetails.getFirstName());
		detailDisplay.getLastName().setValue(currentDetails.getLastName());
		detailDisplay.getMiddleInitial().setValue(currentDetails.getMiddleInitial());
		//detailDisplay.getLoginId().setText(currentDetails.getLoginId());
		detailDisplay.getTitle().setValue(currentDetails.getTitle());
		detailDisplay.getEmailAddress().setValue(currentDetails.getEmailAddress());
		detailDisplay.getPhoneNumber().setValue(currentDetails.getPhoneNumber());
		List<String> messages = new ArrayList<String>();
		messages.add("User ID : "+currentDetails.getLoginId());
		messages.add(currentDetails.getPasswordExpirationMsg());
		
		if(currentDetails.getLoginId() != null){
			detailDisplay.getInformationMessageDisplay().setMessages(messages);
		}
		detailDisplay.getIsActive().setValue(currentDetails.isActive());
		if (!currentDetails.isActive()) {
			detailDisplay.getIsRevoked().setValue(true);
			detailDisplay.getOrganizationListBox().setValue("");
			detailDisplay.getOrganizationListBox().setEnabled(false);
			detailDisplay.getOid().setValue("");
			detailDisplay.getOid().setTitle("");
			detailDisplay.getOid().setEnabled(false);
		} else { // added else to fix default Revoked radio check in Mozilla (User Story 755)
			detailDisplay.getIsRevoked().setValue(false);
			detailDisplay.getOrganizationListBox().setEnabled(true);
			detailDisplay.getOrganizationListBox().setValue(currentDetails.getOrganizationId());
			detailDisplay.getOid().setEnabled(true);
			detailDisplay.getOid().setValue(currentDetails.getOid());
			detailDisplay.getOid().setTitle(currentDetails.getOid());
		}
		
		detailDisplay.setUserLocked(currentDetails.isLocked());
		if (currentDetails.isExistingUser()) {
			detailDisplay.setShowRevokedStatus(currentDetails.isCurrentUserCanChangeAccountStatus());
			// detailDisplay.setUserIsDeletable(currentDetails.isCurrentUserCanChangeAccountStatus());
		} else {
			detailDisplay.setShowRevokedStatus(false);
			//detailDisplay.setUserIsDeletable(false);
		}
		detailDisplay.setUserIsActiveEditable(currentDetails.isCurrentUserCanChangeAccountStatus());
		detailDisplay.setShowUnlockOption(currentDetails.isCurrentUserCanUnlock() && currentDetails.isActive());
		detailDisplay.getRole().setValue(currentDetails.getRole());
		/*detailDisplay.getOid().setValue(currentDetails.getOid());
		detailDisplay.getOid().setTitle(currentDetails.getOid());*/
		//detailDisplay.getRootOid().setValue(currentDetails.getRootOid());
	}
	
	
	/**
	 * Update user details from view.
	 */
	private void updateUserDetailsFromView() {
		currentDetails.setFirstName(detailDisplay.getFirstName().getValue());
		currentDetails.setLastName(detailDisplay.getLastName().getValue());
		currentDetails.setMiddleInitial(detailDisplay.getMiddleInitial().getValue());
		currentDetails.setTitle(detailDisplay.getTitle().getValue());
		currentDetails.setEmailAddress(detailDisplay.getEmailAddress().getValue());
		currentDetails.setPhoneNumber(detailDisplay.getPhoneNumber().getValue());
		currentDetails.setActive(detailDisplay.getIsActive().getValue());
		//currentDetails.setRootOid(detailDisplay.getRootOid().getValue());
		currentDetails.setRole(detailDisplay.getRole().getValue());
		
		currentDetails.setOid(detailDisplay.getOid().getValue());
		String orgId = detailDisplay.getOrganizationListBox().getValue();
		currentDetails.setOrganizationId(orgId);
		Result organization =  detailDisplay.getOrganizationsMap().get(orgId);
		if (organization != null) {
			currentDetails.setOrganization(organization.getOrgName());
		} else {
			currentDetails.setOrganization("");
		}
		currentDetails.scrubForMarkUp();
	}
	
	/**
	 * Gets the widget with heading.
	 * 
	 * @param widget
	 *            the widget
	 * @param heading
	 *            the heading
	 * @return the widget with heading
	 */
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
	
	/**
	 * Checks if is valid.
	 * 
	 * @param model
	 *            the model
	 * @return true, if is valid
	 */
	private boolean isValid(ManageUsersDetailModel model) {
		AdminManageUserModelValidator test = new AdminManageUserModelValidator();
		List<String> message = test.isValidUsersDetail(model);
		
		boolean valid = message.size() == 0;
		if (!valid) {
			detailDisplay.getErrorMessageDisplay().setMessages(message);
		} else {
			detailDisplay.getErrorMessageDisplay().clear();
		}
		return valid;
	}
}
