/**
 * 
 */
package mat.client.umls.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The Interface VSACAPIService.
 * 
 * @author jnarang
 */
@RemoteServiceRelativePath("vsacapi")
public interface VSACAPIService extends RemoteService {
	
	/**
	 * Gets the value set by oid and version.
	 * 
	 * @param oid
	 *            the oid
	 * @param version
	 *            the version
	 * @param effectiveDate
	 *            the effective date
	 * @return the value set by oid and version
	 */
	/* VsacApiResult getValueSetByOIDAndVersion(String OID, String version); */
	
	VsacApiResult getValueSetByOIDAndVersionOrExpansionId(String oid, String version, String effectiveDate);
	
	/**
	 * In validate vsac user.
	 */
	void inValidateVsacUser();
	
	/**
	 * Checks if is already signed in.
	 * 
	 * @return true, if is already signed in
	 */
	boolean isAlreadySignedIn();
	
	/**
	 * Update all vsac value sets at package.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the vsac api result
	 */
	VsacApiResult updateAllVSACValueSetsAtPackage(String measureId);
	
	/**
	 * Update vsac value sets.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param defaultExpId TODO
	 * @return the vsac api result
	 */
	VsacApiResult updateVSACValueSets(String measureId, String defaultExpId);
	
	/**
	 * Update vsac CQL value sets.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param defaultExpId TODO
	 * @return the vsac api result
	 */
	VsacApiResult updateCQLVSACValueSets(String measureId, String defaultExpId);
	
	/**
	 * Validate vsac user.
	 * 
	 * @param userName
	 *            the user name
	 * @param password
	 *            the password
	 * @return true, if successful
	 */
	boolean validateVsacUser(String userName, String password);

	VsacApiResult getAllExpIdentifierList();

	VsacApiResult getAllVersionListByOID(String oid);

	VsacApiResult getMostRecentValueSetByOID(String oid, String profile);
	
}
