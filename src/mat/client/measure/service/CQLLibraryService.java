package mat.client.measure.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.shared.GetUsedCQLArtifactsResult;
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
	
	SaveUpdateCQLResult getLibraryCQLFileData(String libraryId);

	SaveUpdateCQLResult saveAndModifyCQLGeneralInfo(String libraryId, String libraryValue);

	SaveCQLLibraryResult searchForIncludes(String searchText);

	SaveUpdateCQLResult saveIncludeLibrayInCQLLookUp(String libraryId, CQLIncludeLibrary toBeModifiedObj,
			CQLIncludeLibrary currentObj, List<CQLIncludeLibrary> incLibraryList);

	SaveUpdateCQLResult deleteInclude(String libraryId, CQLIncludeLibrary toBeModifiedIncludeObj,
			CQLIncludeLibrary cqlLibObject, List<CQLIncludeLibrary> viewIncludeLibrarys);

	GetUsedCQLArtifactsResult getUsedCqlArtifacts(String libraryId);
}
