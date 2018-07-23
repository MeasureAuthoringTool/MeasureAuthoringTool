package mat.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Panel;

import mat.client.bonnie.BonniePresenter;
import mat.client.bonnie.BonnieService;
import mat.client.bonnie.BonnieServiceAsync;
import mat.client.shared.MatContext;
import mat.client.util.ClientConstants;

public class Bonnie extends MainLayout implements EntryPoint {

	private Panel content;

	private String bonnieTokenURL;

	private BonniePresenter bonniePresenter;

	private BonnieServiceAsync bonnie = (BonnieServiceAsync) GWT.create(BonnieService.class);
	
	@Override
	protected void initEntryPoint() {
		String code = com.google.gwt.user.client.Window.Location.getParameter("code");
		getToken(code);
	}

	private void getToken(String code) {
		bonnie.getBonnieTokens(code, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().redirectToHtmlPage(ClientConstants.HTML_MAT);
			}

			@Override
			public void onSuccess(String result) {
				MatContext.get().redirectToHtmlPage(ClientConstants.HTML_MAT);
			}

		});
		
	}

}
