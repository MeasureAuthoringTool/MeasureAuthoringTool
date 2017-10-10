/**
 * 
 */
package mat.client.umls.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mat.model.cql.CQLQualityDataSetDTO;

/**
 * The Interface VSACAPIService.
 * 
 * @author jnarang
 */
@RemoteServiceRelativePath("vsacapi")
public interface VSACAPIService extends RemoteService {
	
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
	 * Validate vsac user.
	 * 
	 * @param userName
	 *            the user name
	 * @param password
	 *            the password
	 * @return true, if successful
	 */
	boolean validateVsacUser(String userName, String password);

	VsacApiResult getAllVersionListByOID(String oid);

	VsacApiResult getMostRecentValueSetByOID(String oid, String profile);

	VsacApiResult updateCQLVSACValueSets(List<CQLQualityDataSetDTO> appliedQDMList, String defaultExpId);

	VsacApiResult getDirectReferenceCode(String url); 
	
}
