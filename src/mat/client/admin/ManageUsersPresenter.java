package mat.client.admin;

import java.util.ArrayList;
import java.util.List;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.admin.service.SaveUpdateUserResult;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.search.PageSelectionEvent;
import mat.client.shared.search.PageSelectionEventHandler;
import mat.client.shared.search.PageSizeSelectionEvent;
import mat.client.shared.search.PageSizeSelectionEventHandler;
import mat.client.shared.search.SearchResultUpdate;
import mat.client.shared.search.SearchResults;
import mat.client.util.ClientConstants;
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

public class ManageUsersPresenter implements MatPresenter {
	public static interface SearchDisplay extends mat.client.shared.search.SearchDisplay{
		public HasClickHandlers getCreateNewButton();
		public HasSelectionHandlers<ManageUsersSearchModel.Result> getSelectIdForEditTool();
		public void buildDataTable(SearchResults<ManageUsersSearchModel.Result> results);
	}
	public static interface DetailDisplay {
		public Widget asWidget();
		
		public HasClickHandlers getSaveButton();
		public HasClickHandlers getCancelButton();
		//public HasClickHandlers getDeleteUserButton();
		
		public HasValue<String> getFirstName();
		public HasValue<String> getLastName();
		public HasValue<String> getMiddleInitial();
		public Label getLoginId();
		public HasValue<String> getTitle();
		public HasValue<String> getEmailAddress();
		public HasValue<String> getPhoneNumber();
		public HasValue<String> getOrganization();
		public HasValue<String> getRole();
		public HasValue<String> getOid();
		public HasValue<String> getRootOid();
		
		public HasValue<Boolean> getIsActive();
		public HasValue<Boolean> getIsRevoked();
		public HasValue<Boolean> getIsOrgUser();
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		public SuccessMessageDisplayInterface getSuccessMessageDisplay();
		public void setUserIsActiveEditable(boolean b);
		//public void setUserIsDeletable(boolean b);
		public void setUserLocked(boolean b);
		public void setShowRevokedStatus(boolean b);
		public void setShowUnlockOption(boolean b);
		public HasClickHandlers getResetPasswordButton();
		public void setTitle(String title);
	}

	private SimplePanel panel = new SimplePanel();
	private SearchDisplay searchDisplay;
	private DetailDisplay detailDisplay;
	private ManageUsersDetailModel currentDetails;
	private int startIndex = 1;
	private String lastSearchKey;
	
	public ManageUsersPresenter(SearchDisplay sDisplayArg, DetailDisplay dDisplayArg) {
		this.searchDisplay = sDisplayArg;
		this.detailDisplay = dDisplayArg;
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
		searchDisplay.getPageSelectionTool().addPageSelectionHandler(new PageSelectionEventHandler() {
			
			@Override
			public void onPageSelection(PageSelectionEvent event) {
				startIndex = searchDisplay.getPageSize() * (event.getPageNumber() - 1) + 1;
				search(lastSearchKey, startIndex, searchDisplay.getPageSize());
			}
		});
		searchDisplay.getPageSizeSelectionTool().addPageSizeSelectionHandler(new PageSizeSelectionEventHandler() {
			@Override
			public void onPageSizeSelection(PageSizeSelectionEvent event) {
				searchDisplay.getSearchString().setValue("");
				search("", startIndex, searchDisplay.getPageSize());
			}
		});
		
		TextBox searchWidget = (TextBox)(searchDisplay.getSearchString());
		searchWidget.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
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
								detailDisplay.getSuccessMessageDisplay().setMessage("Temporary Password E-mail has been sent.");
							}
							
							@Override
							public void onFailure(Throwable caught) {
								detailDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
								MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
							}
						});
			}
		});
		/*
		detailDisplay.getDeleteUserButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MatContext.get().getAdminService().deleteUser(currentDetails.getUserID(), 
						new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								detailDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
								MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
							}

							@Override
							public void onSuccess(Void result) {
								displaySearch();
							}
				});
			}
		});
		*/
		searchDisplay.getSearchButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String key = searchDisplay.getSearchString().getValue();
				search(key, startIndex, searchDisplay.getPageSize());
			}
		});
	}
	
	private void displaySearch() {
		panel.clear();
		panel.add(searchDisplay.asWidget());
		search("", 1, searchDisplay.getPageSize());
	}
	
	private void displayDetail() {
		resetMessages();
		setUserDetailsToView();
		panel.clear();
		panel.add(detailDisplay.asWidget());
		Mat.focusSkipLists("Manage Users");
	}
	
	
	private void update() {
		resetMessages();
		updateUserDetailsFromView();
		if(isValidUsersDetail(currentDetails)) {
			MatContext.get().getAdminService().saveUpdateUser(currentDetails, new AsyncCallback<SaveUpdateUserResult>() {
				
				@Override
				public void onSuccess(SaveUpdateUserResult result) {
					if(result.isSuccess()) {
						displaySearch();
					}
					else {
						String message = null;
						switch(result.getFailureReason()) {
						
							case SaveUpdateUserResult.ID_NOT_UNIQUE:
								message = MatContext.get().getMessageDelegate().getEmailAlreadyExistsMessage();
								break;
							default:
								message = MatContext.get().getMessageDelegate().getUnknownErrorMessage(result.getFailureReason());
						}
						detailDisplay.getErrorMessageDisplay().setMessage(message);
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					detailDisplay.getErrorMessageDisplay().setMessage(caught.getLocalizedMessage());
				}
			});
		}
	}
	
	private void resetMessages() {
		detailDisplay.getErrorMessageDisplay().clear();
		detailDisplay.getSuccessMessageDisplay().clear();
	}
	private void createNew() {
		detailDisplay.setTitle("Add a User");
		currentDetails = new ManageUsersDetailModel();
		displayDetail();
	}
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
				detailDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
			}
		});
	}
	
	private void search(String key, int startIndex, int pageSize) {
		lastSearchKey = key;
		showSearchingBusy(true);
		MatContext.get().getAdminService().searchUsers(key, startIndex, pageSize, 
				new AsyncCallback<ManageUsersSearchModel>() {
			@Override
			public void onSuccess(ManageUsersSearchModel result) {
				SearchResultUpdate sru = new SearchResultUpdate();
				sru.update(result, (TextBox)searchDisplay.getSearchString(), lastSearchKey);
				sru = null;
				searchDisplay.buildDataTable(result);
				showSearchingBusy(false);
				Mat.focusSkipLists("Manage Users");
			}
			@Override
			public void onFailure(Throwable caught) {
				detailDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
				showSearchingBusy(false);
				if(caught instanceof InCorrectUserRoleException){
					callSignOut();
				}
			}
		});
	}
	
	private void callSignOut(){
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
	
	private void showSearchingBusy(boolean busy){
		if(busy)
			Mat.showLoadingMessage();
		else
			Mat.hideLoadingMessage();
		((Button)searchDisplay.getSearchButton()).setEnabled(!busy);
		((TextBox)(searchDisplay.getSearchString())).setEnabled(!busy);
	}
	
	@Override
	public Widget getWidget() {
		return panel;
	}

	
	@Override
	public void beforeDisplay() {
		displaySearch();
		Mat.focusSkipLists("Manage Users");
	}
	@Override 
	public void beforeClosingDisplay() {
		
	}
	private void setUserDetailsToView() {
		detailDisplay.getFirstName().setValue(currentDetails.getFirstName());
		detailDisplay.getLastName().setValue(currentDetails.getLastName());
		detailDisplay.getMiddleInitial().setValue(currentDetails.getMiddleInitial());
		detailDisplay.getLoginId().setText(currentDetails.getLoginId());
		detailDisplay.getTitle().setValue(currentDetails.getTitle());
		detailDisplay.getEmailAddress().setValue(currentDetails.getEmailAddress());
		detailDisplay.getPhoneNumber().setValue(currentDetails.getPhoneNumber());
		detailDisplay.getOrganization().setValue(currentDetails.getOrganization());
		detailDisplay.getIsActive().setValue(currentDetails.isActive());
		if(!currentDetails.isActive()){
			detailDisplay.getIsRevoked().setValue(true);
		}
		detailDisplay.setUserLocked(currentDetails.isLocked());
		if(currentDetails.isExistingUser()){
		    detailDisplay.setShowRevokedStatus(currentDetails.isCurrentUserCanChangeAccountStatus());
		   // detailDisplay.setUserIsDeletable(currentDetails.isCurrentUserCanChangeAccountStatus());
		}else{
			detailDisplay.setShowRevokedStatus(false);
			//detailDisplay.setUserIsDeletable(false);
		}
		detailDisplay.setUserIsActiveEditable(currentDetails.isCurrentUserCanChangeAccountStatus());
		detailDisplay.setShowUnlockOption(currentDetails.isCurrentUserCanUnlock() && currentDetails.isActive());
		detailDisplay.getRole().setValue(currentDetails.getRole());
		detailDisplay.getOid().setValue(currentDetails.getOid());
		detailDisplay.getRootOid().setValue(currentDetails.getRootOid());
		
	}
	private void updateUserDetailsFromView() {
		currentDetails.setFirstName(detailDisplay.getFirstName().getValue());
		currentDetails.setLastName(detailDisplay.getLastName().getValue());
		currentDetails.setMiddleInitial(detailDisplay.getMiddleInitial().getValue());
		currentDetails.setTitle(detailDisplay.getTitle().getValue());
		currentDetails.setEmailAddress(detailDisplay.getEmailAddress().getValue());
		currentDetails.setPhoneNumber(detailDisplay.getPhoneNumber().getValue());
		currentDetails.setOrganization(detailDisplay.getOrganization().getValue());
		currentDetails.setActive(detailDisplay.getIsActive().getValue());
		currentDetails.setOid(detailDisplay.getOid().getValue());
		currentDetails.setRootOid(detailDisplay.getRootOid().getValue());
		currentDetails.setRole(detailDisplay.getRole().getValue());
	}
	
	private boolean isValidUsersDetail(ManageUsersDetailModel model) {
		List<String> message = new ArrayList<String>();
		
		if("".equals(model.getFirstName().trim())) {
			message.add(MatContext.get().getMessageDelegate().getFirstNameRequiredMessage());
		}
		if("".equals(model.getLastName().trim())) {
			message.add(MatContext.get().getMessageDelegate().getLastNameRequiredMessage());
		}
		if("".equals(model.getEmailAddress().trim())) {
			message.add(MatContext.get().getMessageDelegate().getLoginIDRequiredMessage());
		}
		if("".equals(model.getPhoneNumber().trim())) {
			
			message.add(MatContext.get().getMessageDelegate().getPhoneRequiredMessage());
		}
		
		String phoneNum = model.getPhoneNumber();
		int i, numCount;
		numCount=0;
		for(i=0;i<phoneNum.length(); i++){
			if(Character.isDigit(phoneNum.charAt(i)))
				numCount++;
		}
		if(numCount != 10) {
			message.add(MatContext.get().getMessageDelegate().getPhoneTenDigitMessage());
		}	
		
		if("".equals(model.getOrganization().trim())) {
			message.add(MatContext.get().getMessageDelegate().getOrgRequiredMessage());
		}
		if("".equals(model.getOid().trim())) {
			message.add(MatContext.get().getMessageDelegate().getOIDRequiredMessage());
		}
		if("".equals(model.getRootOid().trim())) {
			message.add(MatContext.get().getMessageDelegate().getRootOIDRequiredMessage());
		}
		if(model.getFirstName().length() < 2) {
			message.add(MatContext.get().getMessageDelegate().getFirstMinMessage());
		}
		if(model.getOid().length() > 50) {
			message.add(MatContext.get().getMessageDelegate().getOIDTooLongMessage());
		}
		if(model.getRootOid().length() > 50) {
			message.add(MatContext.get().getMessageDelegate().getRootOIDTooLongMessage());
		}
		
		boolean valid = message.size() == 0;
		if(!valid) {
			detailDisplay.getErrorMessageDisplay().setMessages(message);
		}
		else {
			detailDisplay.getErrorMessageDisplay().clear();
		}
		return valid;
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
}
