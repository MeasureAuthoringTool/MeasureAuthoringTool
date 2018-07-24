package mat.client.bonnie;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mat.shared.BonnieOauthResult;


public interface BonnieServiceAsync {
	
	public void getBonnieAccessLink(AsyncCallback<String> asyncCallback);
	
	public void authenticateBonnieUser(String code, AsyncCallback<BonnieOauthResult> asyncCallback);
}
