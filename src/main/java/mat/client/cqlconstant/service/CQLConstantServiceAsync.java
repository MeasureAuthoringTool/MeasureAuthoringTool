package mat.client.cqlconstant.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import mat.client.shared.CQLConstantContainer;

public interface CQLConstantServiceAsync {
	
	void getAllCQLConstants(AsyncCallback<CQLConstantContainer> asyncCallback);

}
