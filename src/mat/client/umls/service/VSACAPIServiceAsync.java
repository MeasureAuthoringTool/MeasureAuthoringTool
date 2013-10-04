package mat.client.umls.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface VSACAPIServiceAsync {
	void validateVsacUser(String userName, String password,
			AsyncCallback<Boolean> callback);

	void getValueSetByOIDAndVersion(String OID,
			AsyncCallback<VsacApiResult> callback);

	void inValidateVsacUser(AsyncCallback<Void> callback);

	void isAlreadySignedIn(AsyncCallback<Boolean> callback);

	void updateVSACValueSets(String measureId,
			AsyncCallback<VsacApiResult> callback);

	void updateAllVSACValueSetsAtPackage(String measureId,
			AsyncCallback<VsacApiResult> callback);

}
