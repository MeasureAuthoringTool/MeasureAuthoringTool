package mat.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import mat.client.bonnie.BonnieService;
import mat.client.bonnie.BonnieServiceAsync;
import mat.client.shared.MatContext;
import mat.client.util.ClientConstants;
import mat.shared.BonnieOAuthResult;

public class Bonnie extends MainLayout implements EntryPoint {


	private BonnieServiceAsync bonnie = (BonnieServiceAsync) GWT.create(BonnieService.class);

	@Override
	protected void initEntryPoint() {
		String code = com.google.gwt.user.client.Window.Location.getParameter("code");
		getToken(code);
	}

	private void getToken(String code) {
		bonnie.exchangeCodeForTokens(code, new AsyncCallback<BonnieOAuthResult>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().redirectToHtmlPage(ClientConstants.HTML_MAT);
			}

			@Override
			public void onSuccess(BonnieOAuthResult result) {
				MatContext.get().redirectToHtmlPage(ClientConstants.HTML_MAT);
				getBonnieDisconnectButton().getElement().focus();
				getBonnieDisconnectButton().getElement().setAttribute("role", "alert");
				getBonnieDisconnectButton().getElement().setAttribute("aria-label", "Bonnie Active. Click the Disconnect from Bonnie link to sign out of Bonnie.");
			}

		});

	}

}
