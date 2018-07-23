package mat.client.bonnie;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mat.shared.BonnieOAuthResult;
import mat.shared.bonnie.result.BonnieUserInformationResult;

public interface BonnieServiceAsync {
	
	public void getBonnieAccessLink(AsyncCallback<String> asyncCallback);

	public void exchangeCodeForTokens(String code, AsyncCallback<BonnieOAuthResult> asyncCallback);
	
	public void getBonnieUserInformationForUser(String userId, AsyncCallback<BonnieUserInformationResult> asyncCallback);
}
