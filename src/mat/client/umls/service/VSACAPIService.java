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
	 * Validate vsac user.
	 * 
	 * @param userName
	 *            the user name
	 * @param password
	 *            the password
	 * @return true, if successful
	 */
	boolean validateVsacUser(String userName, String password);

	/**
	 * Gets the value set by oid and version.
	 * 
	 * @param OID
	 *            the oid
	 * @param version
	 *            the version
	 * @return the value set by oid and version
	 */
	VsacApiResult getValueSetByOIDAndVersion(String OID, String version);

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
	 * Update vsac value sets.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the vsac api result
	 */
	VsacApiResult updateVSACValueSets(String measureId);

	/**
	 * Update all vsac value sets at package.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the vsac api result
	 */
	VsacApiResult updateAllVSACValueSetsAtPackage(String measureId);

}
