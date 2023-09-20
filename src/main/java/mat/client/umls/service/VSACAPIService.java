/**
 * 
 */
package mat.client.umls.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import mat.model.cql.CQLQualityDataSetDTO;

import java.util.List;

@RemoteServiceRelativePath("vsacapi")
public interface VSACAPIService extends RemoteService {
	void inValidateVsacUser();

	boolean isAlreadySignedIn();

	boolean validateVsacUser(String apiKey);

	VsacApiResult getMostRecentValueSetByOID(String oid, String release, String profile);

	VsacApiResult updateCQLVSACValueSets(List<CQLQualityDataSetDTO> appliedQDMList, String defaultExpId);

	VsacApiResult getDirectReferenceCode(String url);

	VsacApiResult getVSACProgramsReleasesAndProfiles();

	VsacTicketInformation getVsacInformation();

	String getSessionId();
}