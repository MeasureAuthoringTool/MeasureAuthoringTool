package mat.client.bonnie;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mat.shared.BonnieOAuthResult;


public interface BonnieServiceAsync {
	
	public void getBonnieAccessLink(AsyncCallback<String> asyncCallback);
	
	public void exchangeCodeForTokens(String code, AsyncCallback<BonnieOAuthResult> asyncCallback);
}
