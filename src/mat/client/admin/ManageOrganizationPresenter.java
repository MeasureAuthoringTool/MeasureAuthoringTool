package mat.client.admin;

import java.util.ArrayList;
import java.util.List;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.search.SearchResults;
import mat.client.util.ClientConstants;
import mat.shared.AdminManageUserModelValidator;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/** The Class ManageUsersPresenter. */
public class ManageOrganizationPresenter implements MatPresenter {
	
	/** The Interface DetailDisplay. */
	public static interface DetailDisplay {
		
		/** As widget.
		 * 
		 * @return the widget */
		Widget asWidget();
		
		/** Gets the cancel button.
		 * 
		 * @return the cancel button */
		HasClickHandlers getCancelButton();
		
		// public HasClickHandlers getDeleteUserButton();
		
		/** Gets the error message display.
		 * 
		 * @return the error message display */
		ErrorMessageDisplayInterface getErrorMessageDisplay();
		
		/** Gets the oid.
		 * 
		 * @return the oid */
		HasValue<String> getOid();
		
		
		/** Gets the organization.
		 * 
		 * @return the organization */
		HasValue<String> getOrganization();
		
		/** Gets the save button.
		 * 
		 * @return the save button */
		HasClickHandlers getSaveButton();
		
		/** Gets the success message display.
		 * 
		 * @return the success message display */
		SuccessMessageDisplayInterface getSuccessMessageDisplay();
	}
	
	/** The Interface SearchDisplay. */
	public static interface SearchDisplay extends mat.client.shared.search.SearchDisplay {
		
		/** Builds the data table.
		 * 
		 * @param results the results */
		void buildDataTable(SearchResults<ManageOrganizationSearchModel.Result> results);
		
		/** Gets the creates the new button.
		 * 
		 * @return the creates the new button */
		HasClickHandlers getCreateNewButton();
	}
	
	/** The current details. */
	private ManageOrganizationDetailModel currentDetails;
	
	/** The detail display. */
	private DetailDisplay detailDisplay;
	
	/** The last search key. */
	private String lastSearchKey;
	
	/** The panel. */
	private SimplePanel panel = new SimplePanel();
	
	/** The search display. */
	private SearchDisplay searchDisplay;
	
	/** The start index. */
	private int startIndex = 1;
	
	/** Instantiates a new manage users presenter.
	 * 
	 * @param sDisplayArg the s display arg
	 * @param dDisplayArg the d display arg */
	public ManageOrganizationPresenter(SearchDisplay sDisplayArg, DetailDisplay dDisplayArg) {
		searchDisplay = sDisplayArg;
		detailDisplay = dDisplayArg;
		displaySearch();
		
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
		
		searchDisplay.getSearchButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String key = searchDisplay.getSearchString().getValue();
				search(key, startIndex, searchDisplay.getPageSize());
			}
		});
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		displaySearch();
		Mat.focusSkipLists("Manage Users");
	}
	
	/** Creates the new. */
	private void createNew() {
		currentDetails = new ManageOrganizationDetailModel();
		displayDetail();
	}
	
	/** Display detail. */
	private void displayDetail() {
		resetMessages();
		setUserDetailsToView();
		panel.clear();
		panel.add(detailDisplay.asWidget());
		Mat.focusSkipLists("Manage Users");
	}
	
	/** Display search. */
	private void displaySearch() {
		panel.clear();
		panel.add(searchDisplay.asWidget());
		search("", 1, searchDisplay.getPageSize());
	}
	
	/** Edits the.
	 * 
	 * @param name the name */
	private void edit(String name) {
		/*
		 * MatContext.get().getAdminService().getUser(name, new AsyncCallback<ManageUsersDetailModel>() {
		 * 
		 * @Override public void onFailure(Throwable caught) { detailDisplay.getErrorMessageDisplay()
		 * .setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage()); MatContext.get().recordTransactionEvent(null, null,
		 * null, "Unhandled Exception: " + caught.getLocalizedMessage(), 0); }
		 * 
		 * @Override public void onSuccess(ManageUsersDetailModel result) { currentDetails = result; displayDetail(); } });
		 */
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
	
	/** Gets the widget with heading.
	 * 
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
	
	/** Checks if is valid.
	 * 
	 * @param model the model
	 * @return true, if is valid */
	private boolean isValid(ManageOrganizationDetailModel model) {
		AdminManageUserModelValidator test = new AdminManageUserModelValidator();
		List<String> message = new ArrayList<String>(); // test.isValidUsersDetail(model);
		
		boolean valid = message.size() == 0;
		if (!valid) {
			detailDisplay.getErrorMessageDisplay().setMessages(message);
		} else {
			detailDisplay.getErrorMessageDisplay().clear();
		}
		return valid;
	}
	
	/** Redirect to login. */
	private void redirectToLogin() {
		/*
		 * Added a timer to have a delay before redirect since this was causing the firefox javascript exception.
		 */
		final Timer timer = new Timer() {
			@Override
			public void run() {
				MatContext.get().redirectToHtmlPage(ClientConstants.HTML_LOGIN);
			}
		};
		timer.schedule(1000);
	}
	
	/** Reset messages. */
	private void resetMessages() {
		detailDisplay.getErrorMessageDisplay().clear();
		detailDisplay.getSuccessMessageDisplay().clear();
	}
	
	/** Search.
	 * 
	 * @param key the key
	 * @param startIndex the start index
	 * @param pageSize the page size */
	private void search(String key, int startIndex, int pageSize) {
		lastSearchKey = key;
		// showSearchingBusy(true);
		/*
		 * MatContext.get().getAdminService().searchUsers(key, startIndex, pageSize, new AsyncCallback<ManageUsersSearchModel>() {
		 * 
		 * @Override public void onFailure(Throwable caught) { detailDisplay.getErrorMessageDisplay()
		 * .setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage()); MatContext.get().recordTransactionEvent(null, null,
		 * null, "Unhandled Exception: " + caught.getLocalizedMessage(), 0); showSearchingBusy(false); if (caught instanceof
		 * InCorrectUserRoleException) { callSignOut(); } }
		 * 
		 * @Override public void onSuccess(ManageUsersSearchModel result) { SearchResultUpdate sru = new SearchResultUpdate();
		 * sru.update(result, (TextBox) searchDisplay.getSearchString(), lastSearchKey); sru = null; searchDisplay.buildDataTable(result);
		 * showSearchingBusy(false); Mat.focusSkipLists("Manage Users"); } });
		 */
	}
	
	/** Sets the user details to view. */
	private void setUserDetailsToView() {
		detailDisplay.getOid().setValue(currentDetails.getOid());
		// detailDisplay.getRootOid().setValue(currentDetails.getRootOid());
	}
	
	/** Show searching busy.
	 * 
	 * @param busy the busy */
	private void showSearchingBusy(boolean busy) {
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
		((Button) searchDisplay.getSearchButton()).setEnabled(!busy);
		((TextBox) (searchDisplay.getSearchString())).setEnabled(!busy);
	}
	
	/** Update. */
	private void update() {
		resetMessages();
		updateUserDetailsFromView();
		if (isValid(currentDetails)) {
			/*
			 * MatContext.get().getAdminService().saveUpdateUser(currentDetails, new AsyncCallback<SaveUpdateUserResult>() {
			 * 
			 * @Override public void onFailure(Throwable caught) {
			 * detailDisplay.getErrorMessageDisplay().setMessage(caught.getLocalizedMessage()); }
			 * 
			 * @Override public void onSuccess(SaveUpdateUserResult result) { if (result.isSuccess()) { displaySearch(); } else {
			 * List<String> messages = new ArrayList<String>(); switch (result.getFailureReason()) { case
			 * SaveUpdateUserResult.ID_NOT_UNIQUE: messages.add(MatContext.get().getMessageDelegate() .getEmailAlreadyExistsMessage());
			 * break; case SaveUpdateUserResult.SERVER_SIDE_VALIDATION: messages = result.getMessages(); break; default:
			 * messages.add(MatContext.get().getMessageDelegate() .getUnknownErrorMessage(result.getFailureReason())); }
			 * detailDisplay.getErrorMessageDisplay().setMessages(messages); } } });
			 */
		}
	}
	
	/**
	 * Update user details from view.
	 */
	private void updateUserDetailsFromView() {
		currentDetails.setOid(detailDisplay.getOid().getValue());
	}
}
