package mat.client.codelist.service;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mat.DTO.OperatorDTO;
import mat.DTO.UnitDTO;
import mat.DTO.VSACCodeSystemDTO;
import mat.client.codelist.HasListBox;
import mat.model.MatValueSetTransferObject;
import mat.model.QualityDataSetDTO;


/**
 * The Interface CodeListServiceAsync.
 */
public interface CodeListServiceAsync {

	/**
	 * @return Returns a hash map keyed by oid, e.g. urn:oid:2.16.840.1.113883.12.292, and valued by VSACCodeSystemDTO containing
	 * fhir model 4.0.1 url, e.g. http://hl7.org/fhir/sid/cvx, and the default vsac version.
	 */
	public void getOidToVsacCodeSystemMap(AsyncCallback<Map<String, VSACCodeSystemDTO>> asyncCallback);
	
	/**
	 * Gets the all data types.
	 * 
	 * @param asyncCallback
	 *            the async callback
	 * @return the all data types
	 */
	public void getAllDataTypes(
			AsyncCallback<List<? extends HasListBox>> asyncCallback);
	
	/**
	 * Gets the all operators.
	 * 
	 * @param callback
	 *            the callback
	 * @return the all operators
	 */
	void getAllOperators(AsyncCallback<List<OperatorDTO>> callback);

	/**
	 * Gets the all units.
	 * 
	 * @param callback
	 *            the callback
	 * @return the all units
	 */
	void getAllCqlUnits(AsyncCallback<List<UnitDTO>> callback);
	
	
	
	/**
	 * Gets the list box data.
	 * 
	 * @param callback
	 *            the callback
	 * @return the list box data
	 */
	void getListBoxData(AsyncCallback<CodeListService.ListBoxData> callback);
	
	/**
	 * Gets the qDS data type for category.
	 * 
	 * @param category
	 *            the category
	 * @param callback
	 *            the callback
	 * @return the qDS data type for category
	 */
	public void getQDSDataTypeForCategory(String category, AsyncCallback<List<? extends HasListBox>> callback);
	
	/**
	 * Gets the qDS elements.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param vertsion
	 *            the vertsion
	 * @param callback
	 *            the callback
	 * @return the qDS elements
	 */
	public void getQDSElements(String measureId, String vertsion, AsyncCallback<List<QualityDataSetDTO>> callback);
	
	
	/**
	 * Save qd sto measure.
	 * 
	 * @param matValueSetTransferObject
	 *            the mat value set transfer object
	 * @param callback
	 *            the callback
	 */
	void saveQDStoMeasure(MatValueSetTransferObject matValueSetTransferObject,
			AsyncCallback<SaveUpdateCodeListResult> callback);
	
	/**
	 * Save user defined qds to measure.
	 * 
	 * @param matValueSetTransferObject
	 *            the mat value set transfer object
	 * @param asyncCallback
	 *            the async callback
	 */
	public void saveUserDefinedQDStoMeasure(MatValueSetTransferObject matValueSetTransferObject,
			AsyncCallback<SaveUpdateCodeListResult> asyncCallback);
	
	
	
	/**
	 * Update code list to measure.
	 * 
	 * @param matValueSetTransferObject
	 *            mat Value Set Transfer Object
	 * @param asyncCallback
	 *            the async callback
	 */
	void updateCodeListToMeasure(MatValueSetTransferObject matValueSetTransferObject,
			AsyncCallback<SaveUpdateCodeListResult> asyncCallback);
	
	
}
