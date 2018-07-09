package mat.client.bonnie;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface BonnieServiceAsync {
	
	public void getBonnieLink(AsyncCallback<String> asyncCallback);
	
}
