package mat.client.cqlconstant.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import mat.client.shared.CQLConstantContainer;

/**
 * The CQL Constant Service Async. 
 * 
 * Defines the async calls to get static content such as datatypes, attributes, keywords, and units
 * @author jmeyer
 *
 */
public interface CQLConstantServiceAsync {
	
	public void getAllCQLConstants(boolean isFhirEnabled, AsyncCallback<CQLConstantContainer> asyncCallback);

}
