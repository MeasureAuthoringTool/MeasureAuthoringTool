package mat.client.clause;

import java.util.List;

import mat.model.clause.QDSAttributes;

import com.google.gwt.user.client.rpc.AsyncCallback;

// TODO: Auto-generated Javadoc
/**
 * The Interface QDSAttributesServiceAsync.
 */
public interface QDSAttributesServiceAsync {
	
	/**
	 * Gets the all data type attributes.
	 * 
	 * @param dataTypeName
	 *            the data type name
	 * @param callback
	 *            the callback
	 * @return the all data type attributes
	 */
	void getAllDataTypeAttributes(String dataTypeName, AsyncCallback<List<QDSAttributes>> callback);
	
	/**
	 * Gets the all data flow attribute name.
	 * 
	 * @param callback
	 *            the callback
	 * @return the all data flow attribute name
	 */
	void getAllDataFlowAttributeName(AsyncCallback<List<QDSAttributes>> callback);
	
	/**
	 * Gets the all attributes by data type.
	 * 
	 * @param dataTypeName
	 *            the data type name
	 * @param callback
	 *            the callback
	 * @return the all attributes by data type
	 */
	void getAllAttributesByDataType(String dataTypeName,
			AsyncCallback<List<QDSAttributes>> callback);
	
	/**
	 * Check if qdm data type is present.
	 *
	 * @param dataTypeName the data type name
	 * @param callback the callback
	 */
	void checkIfQDMDataTypeIsPresent(String dataTypeName, 
			AsyncCallback<Boolean> callback);
}
