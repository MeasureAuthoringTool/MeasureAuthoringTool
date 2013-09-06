package mat.client.umls.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface VSACAPIServiceAsync {
	void validateVsacUser(String userName, String password,
			AsyncCallback<String> callback);

}
