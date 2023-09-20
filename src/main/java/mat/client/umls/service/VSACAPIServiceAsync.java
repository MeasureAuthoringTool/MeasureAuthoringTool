package mat.client.umls.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import mat.model.cql.CQLQualityDataSetDTO;

import java.util.List;

/**
 * The Interface VSACAPIServiceAsync.
 */
public interface VSACAPIServiceAsync {

	void inValidateVsacUser(AsyncCallback<Void> callback);

	void getSessionId(AsyncCallback<String> callback);

	void isAlreadySignedIn(AsyncCallback<Boolean> callback);

	void updateCQLVSACValueSets(List<CQLQualityDataSetDTO> appliedQDMList, String defaultExpId, AsyncCallback<VsacApiResult> callback);

	void validateVsacUser(String apiKey, AsyncCallback<Boolean> callback);
	
    void getMostRecentValueSetByOID(final String oid, final String release, final String profile, AsyncCallback<VsacApiResult> callback);

	void getDirectReferenceCode(String url, AsyncCallback<VsacApiResult> callback);
	
	void getVSACProgramsReleasesAndProfiles(AsyncCallback<VsacApiResult> callback);	
	
	void getVsacInformation(AsyncCallback<VsacTicketInformation> callback);
}