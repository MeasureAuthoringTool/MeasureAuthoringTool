package mat.client.umls.service;

import mat.model.CodeListSearchDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface VSACAPIServiceAsync {
	void validateVsacUser(String userName, String password,
			AsyncCallback<String> callback);

	void getValueSetBasedOIDAndVersion(String eightHourTicket, String OID,
			String Version, AsyncCallback<CodeListSearchDTO> callback);

}
