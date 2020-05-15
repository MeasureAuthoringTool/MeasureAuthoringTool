package mat.client.clause;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import mat.model.clause.QDSAttributes;

/**
 * The Interface QDSAttributesServiceAsync.
 */
public interface QDSAttributesServiceAsync {

    /**
     * Gets the all data type attributes.
     *
     * @param dataTypeName the data type name
     * @param callback     the callback
     * @return the all data type attributes
     */
    void getAllDataTypeAttributes(String dataTypeName, AsyncCallback<List<QDSAttributes>> callback);

    /**
     * Gets the all data flow attribute name.
     *
     * @param callback the callback
     * @return the all data flow attribute name
     */
    void getAllDataFlowAttributeName(AsyncCallback<List<QDSAttributes>> callback);

    /**
     * Gets the all attributes by data type.
     *
     * @param dataTypeName the data type name
     * @param callback     the callback
     * @return the all attributes by data type
     */
    void getAllAttributesByDataType(String dataTypeName,
                                    AsyncCallback<List<QDSAttributes>> callback);

    /**
     * Check if qdm data type is present.
     *
     * @param dataTypeName the data type name
     * @param callback     the callback
     */
    void checkIfQDMDataTypeIsPresent(String dataTypeName,
                                     AsyncCallback<Boolean> callback);

    /**
     * Gets the datatype list.
     *
     * @param dataTypeList  the data type list
     * @param asyncCallback the async callback
     * @return the datatype list
     */
    void getDatatypeList(List<String> dataTypeList, AsyncCallback<Map<String, List<String>>> asyncCallback);

    /**
     * Gets the JSON object from xml.
     *
     * @param asyncCallback the async callback
     * @return the JSON object from xml
     */
    void getJSONObjectFromXML(AsyncCallback<String> asyncCallback);

    /**
     * Gets the JSON object from mode details xml.
     *
     * @return the JSON object from mode details xml
     */
    void getModeDetailsJSONObjectFromXML(AsyncCallback<String> asyncCallback);

    /**
     * Gets the all attributes.
     *
     * @param callback the callback
     * @return the all attributes
     */
    void getAllAttributes(AsyncCallback<List<String>> callback);

    void getAllAttributesByDataTypeForFhir(String dataTypeName, AsyncCallback<List<String>> async);
}
