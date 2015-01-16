package mat.client.clause;

import java.util.List;
import java.util.Map;

import mat.model.DataType;
import mat.model.clause.QDSAttributes;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

// TODO: Auto-generated Javadoc
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
	
	/**
	 * Check if qdm data type is present.
	 *
	 * @param dataTypeName the data type name
	 * @return true, if successful
	 */
	public boolean checkIfQDMDataTypeIsPresent(String dataTypeName);

	/**
	 * Gets the datatype list.
	 *
	 * @param dataTypeList the data type list
	 * @return the datatype list
	 */
	Map<String, List<String>>getDatatypeList(List<String> dataTypeList);
	
	/**
	 * Gets the JSON object from xml.
	 *
	 * @return the JSON object from xml
	 */
	String getJSONObjectFromXML();
}

