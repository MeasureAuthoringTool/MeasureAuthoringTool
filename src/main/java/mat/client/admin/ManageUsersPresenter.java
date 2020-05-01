package mat.client.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.TextBox;

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
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import mat.DTO.UserAuditLogDTO;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.admin.ManageOrganizationSearchModel.Result;
import mat.client.admin.ManageUsersSearchView.Observer;
import mat.client.admin.service.SaveUpdateUserResult;
import mat.client.event.LogoffEvent;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.CustomTextAreaWithMaxLength;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.MessageAlert;
import mat.client.shared.search.SearchResultUpdate;
import mat.client.shared.search.SearchResults;
import mat.client.util.MatTextBox;
import mat.shared.AdminManageUserModelValidator;
import mat.shared.InCorrectUserRoleException;

/**
 * The Class ManageUsersPresenter.
 */
public class ManageUsersPresenter implements MatPresenter {

	private static final Logger logger = Logger.getLogger(ManageUsersPresenter.class.getSimpleName());

	/** The panel. */
	private ContentWithHeadingWidget panel = new ContentWithHeadingWidget();

	/** The search display. */
	private SearchDisplay searchDisplay;

	/** The detail display. */
	private DetailDisplay detailDisplay;

	/** The history display. */
	private HistoryDisplay historyDisplay;

	/** The current details. */
	private ManageUsersDetailModel currentDetails;

	/** The last search key. */
	private String lastSearchKey;

	/** The is personal info modified. */
	private boolean isPersonalInfoModified = false;

	/** The updated details. */
	private ManageUsersDetailModel updatedDetails;

	/**
	 * Instantiates a new manage users presenter.
	 *
	 * @param sDisplayArg            the s display arg
	 * @param dDisplayArg            the d display arg
	 * @param hDisplayArg the h display arg
	 */
	public ManageUsersPresenter(SearchDisplay sDisplayArg, DetailDisplay dDisplayArg, HistoryDisplay hDisplayArg) {
		searchDisplay = sDisplayArg;
		detailDisplay = dDisplayArg;
		historyDisplay = hDisplayArg;
		displaySearch("");
		
		if (historyDisplay != null) {
			historyDisplayHandlers(historyDisplay);
		}

		searchDisplay.setObserver(new ManageUsersSearchView.Observer() {
			@Override
			public void onHistoryClicked(mat.client.admin.ManageUsersSearchModel.Result result) {
				historyDisplay.setReturnToLinkText("<< Return to Manage Users");
				displayHistory(result.getKey(), result.getFirstName());
			}
		});

		searchDisplay.getSelectIdForEditTool().addSelectionHandler(
			new SelectionHandler<ManageUsersSearchModel.Result>() {
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
		
		MatTextBox searchWidget = (MatTextBox) (searchDisplay.getSearchString());
		searchWidget.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				searchDisplay.getSuccessMessageDisplay().clearAlert();
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					((Button)searchDisplay.getSearchButton()).click();					
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
				displaySearch(lastSearchKey);
			}
		});

		detailDisplay.getReactivateAccountButton().addClickHandler(
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						resetMessages();
						MatContext
								.get()
								.getAdminService()
								.activateUser(currentDetails.getUserID(),
										new AsyncCallback<Void>() {
											@Override
											public void onSuccess(Void result) {
												detailDisplay.getSuccessMessageDisplay().createAlert("Re-activate E-mail has been sent.");
												List<String> event = new ArrayList<String>();
												event.add("Re-activate");
												MatContext.get()
														.recordUserEvent(
																currentDetails
																		.getUserID(),
																event, null,
																false);
											}

											@Override
											public void onFailure(
													Throwable caught) {
												detailDisplay.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
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
										});
					}
				});
		
		searchDisplay.getSearchButton().addClickHandler(event -> performSearch());
		
	}
	
	private void performSearch() {
		String key = searchDisplay.getSearchString().getValue();
		search(key);
		searchDisplay.getSuccessMessageDisplay().clearAlert();
	}

	/**
	 * Display search.
	 */
	private void displaySearch(String name) {
		panel.setContent(searchDisplay.asWidget());
		panel.setHeading("", "");
		searchDisplay.setTitle("");
		searchDisplay.getSuccessMessageDisplay().clearAlert();
		search(name);
	}

	/**
	 * Display detail.
	 */
	private void displayDetail() {
		resetMessages();
		populateOrganizations();
		buildNotesHistory(currentDetails.getUserID());
		panel.setContent(detailDisplay.asWidget());
		Mat.focusSkipLists("Manage Users");
	}

	/**
	 * Populate organizations in OrganizationListBox.
	 */
	private void populateOrganizations() {
		MatContext
				.get()
				.getAdminService()
				.getAllOrganizations(
						new AsyncCallback<ManageOrganizationSearchModel>() {
							@Override
							public void onFailure(Throwable caught) {
								logger.log(Level.SEVERE, "AdminService::getAllOrganizations -> onFailure : " + caught.getMessage(), caught);
							}

							@Override
							public void onSuccess(ManageOrganizationSearchModel model) {
								logger.log(Level.INFO, "AdminService::getAllOrganizations -> onSuccess");
								List<Result> results = model.getData();
								detailDisplay.populateOrganizations(results);

								Map<String, Result> orgMap = new HashMap<String, Result>();
								for (Result organization : results) {
									orgMap.put(organization.getId(),
											organization);
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
		isUserDetailsModified();
		detailDisplay.getErrorMessageDisplay().clearAlert();
		detailDisplay.getSuccessMessageDisplay().clearAlert();
		
		if (isValid(updatedDetails)) {
			MatContext
					.get()
					.getAdminService()
					.saveUpdateUser(updatedDetails,
							new AsyncCallback<SaveUpdateUserResult>() {
								@Override
								public void onSuccess(SaveUpdateUserResult result) {
									logger.log(Level.INFO, "AdminService::saveUpdateUser -> onSuccess");
									if (result.isSuccess()) {
										
										List<String> event = new ArrayList<String>();
										String addInfo = null;
										String actMsg = "";
										if(currentDetails.getKey()!=null){
											if (detailDisplay.getAddInfoArea()
													.getText().length() > 0) {
												event.add("Administrator Notes");
												addInfo = updatedDetails.getAdditionalInfo();
												detailDisplay.getAddInfoArea().setText("");
												
												//Adding Message if the User Account it activated by Admin.
												if ( !(detailDisplay.getIsActive()
														.getValue() == currentDetails
														.isActive()) && detailDisplay.getIsActive()
														.getValue()) {
													actMsg = MatContext.get().getMessageDelegate().getTempEmailSentMessage();
												}
												
											} else {
												// adding logs for change in
												// personal Information
												if (isPersonalInfoModified) {
													event.add("Personal Information Modified");
													isPersonalInfoModified = false;
												}

												// maintaining logs for change in
												// organization
												if (currentDetails
														.getOid() != null
														&& !(updatedDetails
																.getOid()
																.equalsIgnoreCase(currentDetails
																		.getOid()))) {
													event.add("Organization Changed");
												}

												// maintaining logs for change in
												// security role
												if (currentDetails.getRole() != null
														&& !(updatedDetails
																.getRole()
																.equalsIgnoreCase(currentDetails
																		.getRole()))) {
													event.add("Security Role Changed");
												}
												
												// maintaining logs for active and
												// revoked activity
												if (!(detailDisplay.getIsActive()
														.getValue() == currentDetails
														.isActive())) {
													if (detailDisplay.getIsActive()
															.getValue()) {
														event.add("Activated");
														actMsg = MatContext.get().getMessageDelegate().getTempEmailSentMessage();
													} else {
														event.add("Revoked");
													}
												}
											}

											if (event.size() > 0) {
												MatContext.get().recordUserEvent(currentDetails.getUserID(), event, addInfo, false);
											}
										} else {
											onNewUserCreation(updatedDetails.getEmailAddress());
										}
										
										currentDetails = updatedDetails;
										
										detailDisplay.getFirstName().setValue(currentDetails.getFirstName());
										detailDisplay.getLastName().setValue(currentDetails.getLastName());
										detailDisplay.getTitle().setValue(currentDetails.getTitle());
										detailDisplay.getMiddleInitial().setValue(currentDetails.getMiddleInitial());
										detailDisplay.getEmailAddress().setValue(currentDetails.getEmailAddress());
										detailDisplay.getHarpId().setValue(currentDetails.getHarpId());
										detailDisplay.getPhoneNumber().setValue(currentDetails.getPhoneNumber());
										detailDisplay.getOid().setValue(currentDetails.getOid());
										displaySearch(lastSearchKey);
										searchDisplay.getSuccessMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getUSER_SUCCESS_MESSAGE() + actMsg);
									} else {
										List<String> messages = new ArrayList<String>();
										switch (result.getFailureReason()) {
											case SaveUpdateUserResult.ID_NOT_UNIQUE:
												messages.add(MatContext
														.get()
														.getMessageDelegate()
														.getEmailAlreadyExistsMessage());
												break;
											case SaveUpdateUserResult.SERVER_SIDE_VALIDATION:
												messages = result.getMessages();
												break;
											default:
												messages.add(MatContext
														.get()
														.getMessageDelegate()
														.getUnknownErrorMessage(
																result.getFailureReason()));
										}
										detailDisplay.getErrorMessageDisplay().createAlert(messages);
									}

								}

								@Override
								public void onFailure(Throwable caught) {
									logger.log(Level.SEVERE, "AdminService::saveUpdateUser -> onFailure: " + caught.getMessage(), caught);
									detailDisplay.getErrorMessageDisplay().createAlert(caught.getLocalizedMessage());
								}
							});
		}
	}
	
	/**
	 * On new user creation.
	 *
	 * @param emailId the email id
	 */
	private void onNewUserCreation(String emailId){
		detailDisplay.setTitle("Update a User");
		detailDisplay.getAddInfoArea().setText("");
		detailDisplay.getAddInfoArea().setEnabled(true);
		
		MatContext.get().getAdminService().getUserByEmail(emailId, new AsyncCallback<ManageUsersDetailModel>() {
			@Override
			public void onSuccess(ManageUsersDetailModel result) {
				logger.log(Level.INFO, "AdminService::getUserByEmail -> onSuccess");
				currentDetails = result;
				displaySearch(lastSearchKey);
				searchDisplay.getSuccessMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getUSER_SUCCESS_MESSAGE());
				
				List<String> event = new ArrayList<String>();
				event.add("User Created");
				MatContext.get().recordUserEvent(currentDetails.getUserID(), event, null, false);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "AdminService::getUserByEmail -> onFailure: " + caught.getMessage(), caught);
				//do nothing
			}
		});
	}

	/**
	 * Checks if is user details modified.
	 */
	// check if personal info is changed
	private void isUserDetailsModified() {

		if (currentDetails != null) {
			if (currentDetails.getFirstName() != null
					&& !currentDetails.getFirstName().equalsIgnoreCase(
							detailDisplay.getFirstName().getValue())) {
				isPersonalInfoModified = true;
			} else if (currentDetails.getLastName() != null
					&& !currentDetails.getLastName().equalsIgnoreCase(
							detailDisplay.getLastName().getValue())) {
				isPersonalInfoModified = true;
			} else if (currentDetails.getMiddleInitial() != null
					&& !currentDetails.getMiddleInitial().equalsIgnoreCase(
							detailDisplay.getMiddleInitial().getValue())) {
				isPersonalInfoModified = true;
			} else if (currentDetails.getTitle() != null
					&& !currentDetails.getTitle().equalsIgnoreCase(
							detailDisplay.getTitle().getValue())) {
				isPersonalInfoModified = true;
			} else if (currentDetails.getEmailAddress() != null
					&& !currentDetails.getEmailAddress().equalsIgnoreCase(
							detailDisplay.getEmailAddress().getValue())) {
				isPersonalInfoModified = true;
			} else if (currentDetails.getPhoneNumber() != null
					&& !currentDetails.getPhoneNumber().equalsIgnoreCase(
							detailDisplay.getPhoneNumber().getValue())) {
				isPersonalInfoModified = true;
			} else if (currentDetails.getHarpId() != null
					&& !currentDetails.getHarpId().equalsIgnoreCase(
							detailDisplay.getHarpId().getValue())) {
				isPersonalInfoModified = true;
			}
		}
	}

	/**
	 * Reset messages.
	 */
	private void resetMessages() {
		detailDisplay.getErrorMessageDisplay().clearAlert();
		detailDisplay.getSuccessMessageDisplay().clearAlert();
	}

	/**
	 * Creates the new.
	 */
	private void createNew() {
		detailDisplay.setTitle("Add a User");
		detailDisplay.getInformationMessageDisplay().clearAlert();
		currentDetails = new ManageUsersDetailModel();
		displayDetail();
	}

	/**
	 * Edits the.
	 * 
	 * @param userKey
	 *            the user key
	 */
	private void edit(String userKey) {
		detailDisplay.setTitle("Update a User");
		detailDisplay.getAddInfoArea().setText("");
		detailDisplay.getAddInfoArea().setEnabled(true);
		MatContext.get().getAdminService()
				.getUser(userKey, new AsyncCallback<ManageUsersDetailModel>() {
					@Override
					public void onSuccess(ManageUsersDetailModel result) {
						logger.log(Level.INFO, "AdminService::getUser -> onSuccess: " + result);
						currentDetails = result;
						displayDetail();
					}

					@Override
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, "AdminService::getUser -> onFailure: " + caught.getMessage(), caught);
						detailDisplay.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: " + caught.getLocalizedMessage(), 0);
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
				logger.log(Level.INFO, "AdminService::searchUsers -> onSuccess: "  + result);
				SearchResultUpdate sru = new SearchResultUpdate();
				sru.update(result, (com.google.gwt.user.client.ui.TextBox)searchDisplay.getSearchString(), lastSearchKey);
				searchDisplay.buildDataTable(result);
				showSearchingBusy(false);
				Mat.focusSkipLists("Manage Users");
			}

			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.INFO, "AdminService::searchUsers -> onFailure: "  + caught.getMessage(), caught);
				detailDisplay.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: " + caught.getLocalizedMessage(), 0);
				showSearchingBusy(false);
				if (caught instanceof InCorrectUserRoleException) {
					MatContext.get().getEventBus().fireEvent(new LogoffEvent());
				}
			}
		});
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
		((MatTextBox) (searchDisplay.getSearchString())).setEnabled(!busy);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		return panel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		displaySearch("");
		Mat.focusSkipLists("Manage Users");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
		searchDisplay.getSearchString().setValue("");
		searchDisplay.getSuccessMessageDisplay().clearAlert();
	}

	/**
	 * Sets the user details to view.
	 */
	private void setUserDetailsToView() {
		detailDisplay.getFirstName().setValue(currentDetails.getFirstName());
		detailDisplay.getLastName().setValue(currentDetails.getLastName());
		detailDisplay.getMiddleInitial().setValue(currentDetails.getMiddleInitial());
		detailDisplay.getTitle().setValue(currentDetails.getTitle());
		detailDisplay.getEmailAddress().setValue(currentDetails.getEmailAddress());
		detailDisplay.getHarpId().setValue(currentDetails.getHarpId());
		detailDisplay.getPhoneNumber().setValue(currentDetails.getPhoneNumber());
		
		List<String> messages = new ArrayList<>();
		messages.add("User ID : " + currentDetails.getLoginId());
		messages.add(currentDetails.getPasswordExpirationMsg());
		messages.add(currentDetails.getLastSuccessFullLoginDateTimeMessage());

		if (currentDetails.getLoginId() != null) {
			detailDisplay.getInformationMessageDisplay().createAlert(messages);
			detailDisplay.getInformationMessageDisplay().setWidth("900px");
		}
		detailDisplay.getIsActive().setValue(currentDetails.isActive());
		if (!currentDetails.isActive()) {
			detailDisplay.getIsRevoked().setValue(true);
			detailDisplay.getOrganizationListBox().setValue("");
			detailDisplay.getOrganizationListBox().setEnabled(false);
			detailDisplay.getOid().setValue("");
			detailDisplay.getOid().setTitle("");
			detailDisplay.getOid().setEnabled(false);
		} else { // added else to fix default Revoked radio check in Mozilla
					// (User Story 755)
			detailDisplay.getIsRevoked().setValue(false);
			detailDisplay.getOrganizationListBox().setEnabled(true);
			detailDisplay.getOrganizationListBox().setValue(currentDetails.getOrganizationId());
			detailDisplay.getOid().setEnabled(true);
			detailDisplay.getOid().setValue(currentDetails.getOid());
			detailDisplay.getOid().setTitle(currentDetails.getOid());
		}
		detailDisplay.getRevokeDate().setText(currentDetails.getRevokeDate());
		detailDisplay.setUserLocked(currentDetails.isLocked());
		if (currentDetails.isExistingUser()) {
			detailDisplay.setShowRevokedStatus(currentDetails.isCurrentUserCanChangeAccountStatus());
			detailDisplay.setShowAdminNotes(true);
			if (!currentDetails.isCurrentUserCanChangeAccountStatus()) {
				detailDisplay.getRevokeDate().setText("");
			}
		} else {
			detailDisplay.setShowRevokedStatus(false);
			detailDisplay.setShowAdminNotes(false);
		}
		detailDisplay.setUserIsActiveEditable(currentDetails.isCurrentUserCanChangeAccountStatus());
		detailDisplay.setShowUnlockOption(currentDetails.isCurrentUserCanUnlock() && currentDetails.isActive());
		detailDisplay.getRole().setValue(currentDetails.getRole());
	}

	/**
	 * Update user details from view.
	 */
	private void updateUserDetailsFromView() {
		updatedDetails = new ManageUsersDetailModel();
		if (currentDetails.getKey() != null) {
			updatedDetails.setUserID(currentDetails.getKey());
			updatedDetails.setExistingUser(true);
		}

		updatedDetails.setFirstName(detailDisplay.getFirstName().getValue());
		updatedDetails.setLastName(detailDisplay.getLastName().getValue());
		updatedDetails.setMiddleInitial(detailDisplay.getMiddleInitial().getValue());
		updatedDetails.setTitle(detailDisplay.getTitle().getValue());
		updatedDetails.setEmailAddress(detailDisplay.getEmailAddress().getValue());
		updatedDetails.setHarpId(detailDisplay.getHarpId().getValue());
		updatedDetails.setPhoneNumber(detailDisplay.getPhoneNumber().getValue());
		updatedDetails.setActive(detailDisplay.getIsActive().getValue());
		updatedDetails.setRole(detailDisplay.getRole().getValue());
		updatedDetails.setRevokeDate(detailDisplay.getRevokeDate().getText());
		updatedDetails.setOid(detailDisplay.getOid().getValue());
		String orgId = detailDisplay.getOrganizationListBox().getValue();
		updatedDetails.setOrganizationId(orgId);
		Result organization = detailDisplay.getOrganizationsMap().get(orgId);
		if (organization != null) {
			updatedDetails.setOrganization(organization.getOrgName());
		} else {
			updatedDetails.setOrganization("");
		}
		if (!(detailDisplay.getIsActive().getValue() == currentDetails.isActive())) {
			if (detailDisplay.getIsActive().getValue()) {
				updatedDetails.setBeingActivated(true);
			} else {
				updatedDetails.setBeingRevoked(true);
			}
		}
		updatedDetails.setAdditionalInfo(detailDisplay.getAddInfoArea().getText());
		updatedDetails.scrubForMarkUp();
	}

	/**
	 * History display handlers.
	 *
	 * @param historyDisplay the history display
	 */
	private void historyDisplayHandlers(final HistoryDisplay historyDisplay) {

		historyDisplay.getReturnToLink().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				displaySearch(lastSearchKey);
			}
		});
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
			detailDisplay.getErrorMessageDisplay().createAlert(message);
		} else {
			detailDisplay.getErrorMessageDisplay().clearAlert();
		}
		return valid;
	}

	/**
	 * Display history.
	 *
	 * @param userId the user id
	 * @param userName the user name
	 */
	private void displayHistory(String userId, String userName) {

		searchUserHistory(userId);
		historyDisplay.setUserName(userName);
		panel.getButtonPanel().clear();
		panel.setHeading("Users > History", "UserLibrary");
		panel.setContent(historyDisplay.asWidget());
		Mat.focusSkipLists("UserLibrary");
	}

	private void buildNotesHistory(String userId) {

		MatContext.get().getAuditService().executeUserLogSearch(userId, new AsyncCallback<List<UserAuditLogDTO>>() {
			@Override
			public void onFailure(Throwable caught) {
				//do nothing
				logger.log(Level.SEVERE, "AdminService::executeUserLogSearch -> onFailure: ",  caught);
			}

			@Override
			public void onSuccess(List<UserAuditLogDTO> result) {
				logger.log(Level.INFO, "AdminService::executeUserLogSearch -> onSuccess");
				detailDisplay.populateNotes(result);
			}
		});
	}
	
	/**
	 * Search user history.
	 *
	 * @param userId the user id
	 */
	private void searchUserHistory(String userId) {

		MatContext.get().getAuditService().executeUserLogSearch(userId, new AsyncCallback<List<UserAuditLogDTO>>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "AdminService::executeUserLogSearch -> onFailure: " + caught.getMessage(), caught);
				//do nothing
			}

			@Override
			public void onSuccess(List<UserAuditLogDTO> result) {
				logger.log(Level.INFO, "AdminService::executeUserLogSearch -> onSuccess");
				historyDisplay.buildCellTable(result);
			}
		});
	}

	/**
	 * The Interface SearchDisplay.
	 */
	public interface SearchDisplay {

		/**
		 * Gets the creates the new button.
		 *
		 * @return the creates the new button
		 */
		HasClickHandlers getCreateNewButton();

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

		/**
		 * Sets the observer.
		 *
		 * @param observer the new observer
		 */
		void setObserver(Observer observer);

		/**
		 * Sets the title.
		 *
		 * @param title the new title
		 */
		void setTitle(String title);

		/**
		 * Gets the success message display.
		 *
		 * @return the success message display
		 */
		MessageAlert getSuccessMessageDisplay();
	}

	/**
	 * The Interface DetailDisplay.
	 */
	public interface DetailDisplay {

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

		HasValue<String> getHarpId();

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
		 * @param organizations
		 *            the organizations
		 */
		void populateOrganizations(List<Result> organizations);

		/**
		 * Sets the organizations map.
		 *
		 * @param organizationsMap
		 *            the organizations map
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
		MessageAlert getErrorMessageDisplay();

		/**
		 * Gets the success message display.
		 *
		 * @return the success message display
		 */
		MessageAlert getSuccessMessageDisplay();

		/**
		 * Sets the user is active editable.
		 *
		 * @param b
		 *            the new user is active editable
		 */
		void setUserIsActiveEditable(boolean b);

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
		HasClickHandlers getReactivateAccountButton();

		/**
		 * Sets the title.
		 *
		 * @param title
		 *            the new title
		 */
		void setTitle(String title);

		/**
		 * Gets the information message display.
		 *
		 * @return the information message display
		 */
		MessageAlert getInformationMessageDisplay();

		/**
		 * Gets the revoke date.
		 *
		 * @return the revoke date
		 */
		Label getRevokeDate();

		/**
		 * Sets the revoke date.
		 *
		 * @param revokeDate the new revoke date
		 */
		void setRevokeDate(Label revokeDate);

		/**
		 * Gets the adds the info area.
		 *
		 * @return the adds the info area
		 */
		CustomTextAreaWithMaxLength getAddInfoArea();

		/**
		 * Sets the adds the info area.
		 *
		 * @param addInfoArea the new adds the info area
		 */
		void setAddInfoArea(CustomTextAreaWithMaxLength addInfoArea);

		/**
		 * Sets the show admin notes.
		 *
		 * @param b the new show admin notes
		 */
		void setShowAdminNotes(boolean b);

		void populateNotes(List<UserAuditLogDTO> result);
	}

	/**
	 * The Interface HistoryDisplay.
	 */
	public static interface HistoryDisplay {

		/**
		 * As widget.
		 *
		 * @return the widget
		 */
		public Widget asWidget();

		/**
		 * Gets the user id.
		 *
		 * @return the user id
		 */
		public String getUserId();

		/**
		 * Gets the measure name.
		 *
		 * @return the measure name
		 */
		public String getUserName();

		/**
		 * Gets the return to link.
		 *
		 * @return the return to link
		 */
		public HasClickHandlers getReturnToLink();

		/**
		 * Sets the user id.
		 *
		 * @param id the new user id
		 */
		public void setUserId(String id);

		/**
		 * Sets the measure name.
		 *
		 * @param name
		 *            the new measure name
		 */
		public void setUserName(String name);

		/**
		 * Sets the return to link text.
		 *
		 * @param s
		 *            the new return to link text
		 */
		public void setReturnToLinkText(String s);

		/**
		 * Builds the cell table.
		 *
		 * @param results
		 *            the results
		 */

		void buildCellTable(List<UserAuditLogDTO> results);

	}
}
