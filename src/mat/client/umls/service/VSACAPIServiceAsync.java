package mat.client.umls.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mat.model.cql.CQLQualityDataSetDTO;

/**
 * The Interface VSACAPIServiceAsync.
 */
public interface VSACAPIServiceAsync {
	
	
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
	
	void getAllVersionListByOID(String oid, AsyncCallback<VsacApiResult> callback);
	
    void getMostRecentValueSetByOID(final String oid, final String profile, 
			AsyncCallback<VsacApiResult> callback);

	void getDirectReferenceCode(String url, AsyncCallback<VsacApiResult> callback);
	
	void getVSACProgramsAndReleases(AsyncCallback<VsacApiResult> callback);	
	
}
