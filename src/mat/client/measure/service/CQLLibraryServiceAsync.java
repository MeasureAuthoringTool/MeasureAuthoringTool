package mat.client.measure.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.SaveUpdateCQLResult;

public interface CQLLibraryServiceAsync {

	void search(String searchText, String searchFrom, int filter,int startIndex, int pageSize,
			AsyncCallback<SaveCQLLibraryResult> callback);

	void findCQLLibraryByID(String cqlLibraryID, AsyncCallback<CQLLibraryDataSetObject> callback);

	void save(CQLLibraryDataSetObject cqlModel, AsyncCallback<SaveCQLLibraryResult> callback);


	void getCQLData(String id, AsyncCallback<SaveUpdateCQLResult> callback);

	void isLibraryLocked(String id, AsyncCallback<Boolean> isLocked);
	
	void resetLockedDate(String measureId,String userId, AsyncCallback<SaveCQLLibraryResult> callback);

	void updateLockedDate(String currentLibraryId, String loggedinUserId,AsyncCallback<SaveCQLLibraryResult> asyncCallback);

	
	void getAllRecentCQLLibrariesForUser(String userId, AsyncCallback<SaveCQLLibraryResult> callback);

	void isLibraryAvailableAndLogRecentActivity(String libraryid, String userId, AsyncCallback<Void> callback);

	void searchForVersion(String searchText, AsyncCallback<SaveCQLLibraryResult> callback);

	void saveFinalizedVersion(String libraryId, boolean isMajor, String version,
			AsyncCallback<SaveCQLLibraryResult> callback);

	void searchForDraft(String searchText, AsyncCallback<SaveCQLLibraryResult> callback);

	void saveDraftFromVersion(String libraryId, AsyncCallback<SaveCQLLibraryResult> callback);
	
	void getLibraryCQLFileData(String libraryId, AsyncCallback<SaveUpdateCQLResult> callback);

	void saveAndModifyCQLGeneralInfo(String libraryId, String libraryValue,
			AsyncCallback<SaveUpdateCQLResult> callback);

	void searchForIncludes(String searchText, AsyncCallback<SaveCQLLibraryResult> callback);

	void saveIncludeLibrayInCQLLookUp(String libraryId, CQLIncludeLibrary toBeModifiedObj, CQLIncludeLibrary currentObj,
			List<CQLIncludeLibrary> incLibraryList, AsyncCallback<SaveUpdateCQLResult> callback);

	void deleteInclude(String libraryId, CQLIncludeLibrary toBeModifiedIncludeObj, CQLIncludeLibrary cqlLibObject,
			List<CQLIncludeLibrary> viewIncludeLibrarys, AsyncCallback<SaveUpdateCQLResult> callback);

	void getUsedCqlArtifacts(String libraryId, AsyncCallback<GetUsedCQLArtifactsResult> callback);

}
