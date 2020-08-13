package mat.client.clause;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import mat.model.clause.QDSAttributes;

import java.util.List;
import java.util.Map;

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
	 * Gets QDM Attributes modes from SimplifiedAttributePatterns.xml.
	 *
	 * @return the JSON object from xml
	 */
	String getJSONObjectFromXML();
	
	/**
	 * Gets the Attributes Mode Details JSON object from mode details (ModeDetails.xml) xml.
	 *
	 * @return the JSON object from mode details xml
	 */
	String getModeDetailsJSONObjectFromXML();

	/**
	 * Gets the all attributes.
	 *
	 * @return the all attributes
	 */
	List<String> getAllAttributes();

	List<String> getAllAttributesByDataTypeForFhir(String dataTypeName);
}

