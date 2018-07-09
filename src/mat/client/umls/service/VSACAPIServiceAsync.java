package mat.client.umls.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mat.model.cql.CQLQualityDataSetDTO;

public interface VSACAPIServiceAsync {
	
	
	void inValidateVsacUser(AsyncCallback<Void> callback);
	
	void isAlreadySignedIn(AsyncCallback<Boolean> callback);
	
	void updateCQLVSACValueSets(List<CQLQualityDataSetDTO> appliedQDMList, String defaultExpId,
			AsyncCallback<VsacApiResult> callback);
	
	void validateVsacUser(String userName, String password,
			AsyncCallback<Boolean> callback);
	
	void getAllVersionListByOID(String oid, AsyncCallback<VsacApiResult> callback);
	
    void getMostRecentValueSetByOID(final String oid, final String profile, 
			AsyncCallback<VsacApiResult> callback);

	void getDirectReferenceCode(String url, AsyncCallback<VsacApiResult> callback);
	
	void getVSACProgramsAndReleases(AsyncCallback<VsacApiResult> callback);	
	
}
