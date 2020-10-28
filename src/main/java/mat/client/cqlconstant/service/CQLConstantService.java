package mat.client.cqlconstant.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import mat.client.shared.CQLConstantContainer;

/**
 * The CQL Constant Service interface. Defines how to get static cql contant such as attributes, datatypes, keywords, and units.
 * @author jmeyer
 *
 */
@RemoteServiceRelativePath("cqlConstantService")
public interface CQLConstantService extends RemoteService {
	
	CQLConstantContainer getAllCQLConstants(boolean isFhirEnabled);

}
