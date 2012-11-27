package mat.client.clause;

import java.util.List;

import mat.model.clause.QDSAttributes;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */	
@RemoteServiceRelativePath("qdsattributes")
public interface QDSAttributesService extends RemoteService {
	List<QDSAttributes> getAllDataTypeAttributes(String dataTypeName);
	List<QDSAttributes> getAllDataFlowAttributeName();

}

