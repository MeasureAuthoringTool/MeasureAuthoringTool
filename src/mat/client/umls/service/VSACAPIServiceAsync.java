package mat.client.umls.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mat.model.cql.CQLQualityDataSetDTO;

/**
 * The Interface VSACAPIServiceAsync.
 */
public interface VSACAPIServiceAsync {
	
	/**
	 * Gets the value set by oid and version.
	 * 
	 * @param oid
	 *            the oid
	 * @param version
	 *            the version
	 * @param effectiveDate
	 *            the effective date
	 * @param callback
	 *            the callback
	 * @return the value set by oid and version or effective date
	 */
	void getValueSetByOIDAndVersionOrExpansionId(String oid, String version, String effectiveDate,
			AsyncCallback<VsacApiResult> callback);
	
	/**
	 * In validate vsac user.
	 * 
	 * @param callback
	 *            the callback
	 */
	void inValidateVsacUser(AsyncCallback<Void> callback);
	
	/**
	 * Checks if is already signed in.
	 * 
	 * @param callback
	 *            the callback
	 */
	void isAlreadySignedIn(AsyncCallback<Boolean> callback);
	
	/**
	 * Update all vsac value sets at package.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param callback
	 *            the callback
	 */
	void updateAllVSACValueSetsAtPackage(String measureId,
			AsyncCallback<VsacApiResult> callback);
	
	/**
	 * Update vsac value sets.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param callback
	 *            the callback
	 */
	void updateVSACValueSets(String measureId, String defaultExpId,
			AsyncCallback<VsacApiResult> callback);
	
	/**
	 * Update vsac value sets.
	 * 
	 * @param appliedQDMList
	 *            the applied qdm list
	 * @param callback
	 *            the callback
	 */
	void updateCQLVSACValueSets(List<CQLQualityDataSetDTO> appliedQDMList, String defaultExpId,
			AsyncCallback<VsacApiResult> callback);
	
	/**
	 * Update stand alone vsac value sets.
	 * 
	 * @param libraryId
	 *            the library id
	 * @param callback
	 *            the callback
	 */
	void updateStandaloneCQLVSACValueSets(String libraryId, String defaultExpId,
			AsyncCallback<VsacApiResult> callback);
	
	/**
	 * Validate vsac user.
	 * 
	 * @param userName
	 *            the user name
	 * @param password
	 *            the password
	 * @param callback
	 *            the callback
	 */
	void validateVsacUser(String userName, String password,
			AsyncCallback<Boolean> callback);
	
	void getAllExpProfileList(AsyncCallback<VsacApiResult> callback);
	
	void getAllVersionListByOID(String oid, AsyncCallback<VsacApiResult> callback);
	
    void getMostRecentValueSetByOID(final String oid, final String profile, 
			AsyncCallback<VsacApiResult> callback);

	void getDirectReferenceCode(String url, AsyncCallback<VsacApiResult> callback);
	
}
