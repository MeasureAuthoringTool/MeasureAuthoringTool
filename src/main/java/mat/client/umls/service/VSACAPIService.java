/**
 * 
 */
package mat.client.umls.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mat.model.cql.CQLQualityDataSetDTO;

@RemoteServiceRelativePath("vsacapi")
public interface VSACAPIService extends RemoteService {
	void inValidateVsacUser();

	boolean isAlreadySignedIn();

	boolean validateVsacUser(String userName, String password);

	VsacApiResult getMostRecentValueSetByOID(String oid, String release, String profile);

	VsacApiResult updateCQLVSACValueSets(List<CQLQualityDataSetDTO> appliedQDMList, String defaultExpId);

	VsacApiResult getDirectReferenceCode(String url);

	VsacApiResult getVSACProgramsReleasesAndProfiles();

	VsacTicketInformation getTicketGrantingToken();

	String getSessionId();
}