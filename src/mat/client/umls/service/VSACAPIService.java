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
	
	VsacApiResult updateStandaloneCQLVSACValueSets(String libraryId, String defaultExpId);
	
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

	VsacApiResult getAllExpProfileList();

	VsacApiResult getAllVersionListByOID(String oid);

	VsacApiResult getMostRecentValueSetByOID(String oid, String profile);

	VsacApiResult updateCQLVSACValueSets(List<CQLQualityDataSetDTO> appliedQDMList, String defaultExpId);
	
}
