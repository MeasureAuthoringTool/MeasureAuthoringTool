package mat.client.bonnie;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface BonnieServiceAsync {
	
	public void getBonnieAccessLink(AsyncCallback<String> asyncCallback);
	
	public void getBonnieTokens(String code, AsyncCallback<String> asyncCallback);
}
