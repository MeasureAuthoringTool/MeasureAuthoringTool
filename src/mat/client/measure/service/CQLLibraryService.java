package mat.client.measure.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mat.model.cql.CQLLibraryDataSetObject;
import mat.shared.SaveUpdateCQLResult;

@RemoteServiceRelativePath("cqlLibrary")
public interface CQLLibraryService extends RemoteService {
	SaveCQLLibraryResult search(String searchText, String searchFrom, int filter, int startIndex, int pageSize);

	CQLLibraryDataSetObject findCQLLibraryByID(String cqlLibraryID);
	SaveCQLLibraryResult save(CQLLibraryDataSetObject cqlModel);
	public SaveUpdateCQLResult getCQLData(String id);
	boolean isLibraryLocked(String id);
	SaveCQLLibraryResult resetLockedDate(String currentLibraryId,String userId);
	SaveCQLLibraryResult updateLockedDate(String currentLibraryId,String userId);

	SaveCQLLibraryResult getAllRecentCQLLibrariesForUser(String userId);

	void isLibraryAvailableAndLogRecentActivity(String libraryid, String userId);

	SaveCQLLibraryResult searchForVersion(String searchText);

	SaveCQLLibraryResult saveFinalizedVersion(String libraryId, boolean isMajor, String version);

	SaveCQLLibraryResult searchForDraft(String searchText);
	public SaveCQLLibraryResult saveDraftFromVersion(String libraryId);

}
