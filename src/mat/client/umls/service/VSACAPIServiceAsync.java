package mat.client.umls.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface VSACAPIServiceAsync {
	void validateVsacUser(String userName, String password,
			AsyncCallback<Boolean> callback);

	void getValueSetBasedOIDAndVersion(String OID, String version,
			AsyncCallback<VsacApiResult> callback);

	void inValidateVsacUser(AsyncCallback<Void> callback);

}
