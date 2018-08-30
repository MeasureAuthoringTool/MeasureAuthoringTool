package mat.client.admin;

import org.gwtbootstrap3.client.ui.Button;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.MatContext;
import mat.client.shared.search.SearchResultUpdate;
import mat.client.util.ClientConstants;
import mat.client.util.MatTextBox;
import mat.shared.InCorrectUserRoleException;

public class ManageBonnieTokenPresenter implements MatPresenter {
	private ContentWithHeadingWidget panel = new ContentWithHeadingWidget();
	private ManageBonnieTokenView searchDisplay;
	ManageUsersDetailView manageUsersDetailView;
	private String lastSearchKey;
	public ManageBonnieTokenPresenter() {
		manageUsersDetailView = new ManageUsersDetailView();
		searchDisplay = new ManageBonnieTokenView();
		
		searchDisplay.setObserver(new ManageBonnieTokenView.Observer() {
			@Override
			public void onStopBonnieSessionClicked(mat.client.admin.ManageUsersSearchModel.Result result) {
				GWT.log("clicked revoke session");
				// TODO Auto-generated method stub
				//TODO implement this
			}
		});
		
	}

	@Override
	public void beforeClosingDisplay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeDisplay() {
		displaySearch("");
		Mat.focusSkipLists("Manage Users");
	}

	@Override
	public Widget getWidget() {
		return panel;
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
	
	private void search(String key) {
		lastSearchKey = key;
		showSearchingBusy(true);

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
					callSignOut();
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
	
	private void redirectToLogin() {
		/*
		 * Added a timer to have a delay before redirect since this was causing
		 * the firefox javascript exception.
		 */
		final Timer timer = new Timer() {
			@Override
			public void run() {
				MatContext.get().redirectToHtmlPage(ClientConstants.HTML_LOGIN);
			}
		};
		timer.schedule(1000);
	}
}
