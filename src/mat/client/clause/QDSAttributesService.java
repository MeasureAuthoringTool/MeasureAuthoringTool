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
	
	/**
	 * Gets the all data type attributes.
	 * 
	 * @param dataTypeName
	 *            the data type name
	 * @return the all data type attributes
	 */
	List<QDSAttributes> getAllDataTypeAttributes(String dataTypeName);
	
	/**
	 * Gets the all data flow attribute name.
	 * 
	 * @return the all data flow attribute name
	 */
	List<QDSAttributes> getAllDataFlowAttributeName();
	
	/**
	 * Gets the all attributes by data type.
	 * 
	 * @param dataTypeName
	 *            the data type name
	 * @return the all attributes by data type
	 */
	public List<QDSAttributes> getAllAttributesByDataType(String dataTypeName);
}

