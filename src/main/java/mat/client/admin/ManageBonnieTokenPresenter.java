package mat.client.admin;

import mat.client.event.LogoffEvent;
import org.gwtbootstrap3.client.ui.Button;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.shared.ConfirmationObserver;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.MatContext;
import mat.client.shared.search.SearchResultUpdate;
import mat.client.util.ClientConstants;
import mat.client.util.MatTextBox;
import mat.shared.InCorrectUserRoleException;
import mat.shared.StringUtility;

public class ManageBonnieTokenPresenter implements MatPresenter {
	private ContentWithHeadingWidget panel = new ContentWithHeadingWidget();
	private ManageBonnieTokenView searchDisplay;
	ManageUsersDetailView manageUsersDetailView;
	private String lastSearchKey;
	private RevokeReasonDialogBox reasonDialogBox;
	public ManageBonnieTokenPresenter() {
		manageUsersDetailView = new ManageUsersDetailView();
		searchDisplay = new ManageBonnieTokenView();
		
		searchDisplay.setObserver(new ManageBonnieTokenView.Observer() {
			@Override
			public void onStopBonnieSessionClicked(mat.client.admin.ManageUsersSearchModel.Result userResult) {
				ConfirmationDialogBox confirmationDialogBox = new ConfirmationDialogBox("This action will revoke active Bonnie tokens for this user. Once done, this action can not be undone. Are you sure you wish to proceed?", "Yes", "No", new ConfirmationObserver() {
					@Override
					public void onYesButtonClicked() {
						String userId = userResult.getKey();
						revokeBonnieAccessTokenForUser(userResult, userId);
					}

					@Override
					public void onNoButtonClicked() {}
					
					@Override
					public void onClose() {}
				});
				confirmationDialogBox.show();
			}

			@Override
			public void onRevokeAllBonnieSessionsClicked() {
				ConfirmationDialogBox confirmationDialogBox = new ConfirmationDialogBox("This action will revoke all active Bonnie tokens in the MAT. Once done, this action can not be undone. Are you sure you wish to proceed?", "Yes", "No", new ConfirmationObserver() {
					@Override
					public void onYesButtonClicked() {
						displayReasonDialogBox();
					}

					@Override
					public void onNoButtonClicked() {}
					
					@Override
					public void onClose() {}
				});
				confirmationDialogBox.show();

			}
		});
		
		MatTextBox searchWidget = (MatTextBox) (searchDisplay.getSearchString());
		searchWidget.addKeyUpHandler(event -> searchWidgetKeyUpPressed(event));
		searchDisplay.getSearchButton().addClickHandler(event -> performSearch());
	}
	
	private void displayReasonDialogBox() {
		reasonDialogBox = new RevokeReasonDialogBox("Revoke Bonnie Tokens", "Submit", "Cancel", new ConfirmationObserver() {

			@Override
			public void onYesButtonClicked() {
				if(StringUtility.isEmptyOrNull(reasonDialogBox.getReasonText())){
					reasonDialogBox.getMessageAlert().getErrorMessageAlert().createAlert("Reason is a requied field");
				} else {
					reasonDialogBox.hide();
					revokeAllBonnieTokens(reasonDialogBox.getReasonText());
				}
			}

			@Override
			public void onNoButtonClicked() {}

			@Override
			public void onClose() {}
		});
		reasonDialogBox.show();
	}

	private void revokeAllBonnieTokens(String reason) {
		MatContext.get().getBonnieService().revokeAllBonnieAccessTokens(MatContext.get().getLoggedinLoginId(), reason, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				searchDisplay.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: " + caught.getLocalizedMessage(), 0);
			}

			@Override
			public void onSuccess(Void result) {
				displaySearch("");
				searchDisplay.getSuccessMessageDisplay().createAlert("All active Bonnie tokens have been revoked.");
			}
		});
	}

	private void searchWidgetKeyUpPressed(KeyUpEvent event) {
		searchDisplay.getSuccessMessageDisplay().clearAlert();
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			performSearch();					
		}
	}

	private void performSearch() {
		String key = searchDisplay.getSearchString().getValue();
		search(key);
		searchDisplay.getSuccessMessageDisplay().clearAlert();
	}

	private void revokeBonnieAccessTokenForUser(
			mat.client.admin.ManageUsersSearchModel.Result userResult, String userId) {
		MatContext.get().getBonnieService().revokeBonnieAccessTokenForUser(userId, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				searchDisplay.getErrorMessageAlert()
				.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
		MatContext.get().recordTransactionEvent(null, null, null,
				"Unhandled Exception: " + caught.getLocalizedMessage(), 0);
			}

			@Override
			public void onSuccess(Void result) {
				displaySearch("");
				searchDisplay.getSuccessMessageDisplay().createAlert("Active Bonnie tokens have been revoked for user " + userResult.getLoginId());
			}
		});
	}
	
	@Override
	public void beforeClosingDisplay() {}

	@Override
	public void beforeDisplay() {
		displaySearch("");
		Mat.focusSkipLists("Manage Users");
	}

	@Override
	public Widget getWidget() {
		return panel;
	}

	private void displaySearch(String name) {
		panel.setContent(searchDisplay.asWidget());
		panel.setHeading("", "");
		searchDisplay.setTitle("");
		searchDisplay.getSuccessMessageDisplay().clearAlert();
		searchDisplay.getErrorMessageAlert().clearAlert();
		search(name);
	}
	
	private void search(String key) {
		lastSearchKey = key;
		showSearchingBusy(true);
		searchUsersWithActiveBonnie(key);
	}

	private void searchUsersWithActiveBonnie(String key) {
		MatContext.get().getAdminService().searchUsersWithActiveBonnie(key, new AsyncCallback<ManageUsersSearchModel>() {
			@Override
			public void onSuccess(ManageUsersSearchModel result) {
				SearchResultUpdate sru = new SearchResultUpdate();
				sru.update(result, (com.google.gwt.user.client.ui.TextBox)searchDisplay.getSearchString(), lastSearchKey);
				searchDisplay.buildDataTable(result);
				showSearchingBusy(false);
				Mat.focusSkipLists("Manage Users");
			}

			@Override
			public void onFailure(Throwable caught) {
				manageUsersDetailView.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: " + caught.getLocalizedMessage(), 0);
				showSearchingBusy(false);
				if (caught instanceof InCorrectUserRoleException) {
					MatContext.get().getEventBus().fireEvent(new LogoffEvent());
				}
			}
		});
	}
	
	private void showSearchingBusy(boolean busy) {
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
		((Button) searchDisplay.getSearchButton()).setEnabled(!busy);
		((MatTextBox) (searchDisplay.getSearchString())).setEnabled(!busy);
	}
}
