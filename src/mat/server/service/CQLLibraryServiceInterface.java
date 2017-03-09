package mat.server.service;

import java.util.List;

import mat.client.measure.service.SaveCQLLibraryResult;
import mat.model.clause.CQLLibrary;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.server.util.XmlProcessor;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.SaveUpdateCQLResult;

public interface CQLLibraryServiceInterface {
	
	SaveCQLLibraryResult search(String searchText, String searchFrom, int filter,int startIndex, int pageSize);
	
	void save(CQLLibrary cqlLibrary);

	CQLLibraryDataSetObject findCQLLibraryByID(String cqlLibraryId);
	public SaveCQLLibraryResult save(CQLLibraryDataSetObject cqlLibraryDataSetObject);

	String createCQLLookUpTag(String libraryName,String version);

	XmlProcessor loadCQLXmlTemplateFile();
	
	public SaveUpdateCQLResult getCQLData(String id);

	public boolean isLibraryLocked(String id);

	public SaveCQLLibraryResult resetLockedDate(String currentLibraryId, String userId);

	public SaveCQLLibraryResult updateLockedDate(String currentLibraryId, String userId);
	
	SaveCQLLibraryResult getAllRecentCQLLibrariesForUser(String userId);

	void isLibraryAvailableAndLogRecentActivity(String libraryid, String userId);

	String getCQLLookUpXml(String libraryName, String versionText, XmlProcessor xmlProcessor, String mainXPath);

	SaveCQLLibraryResult searchForVersion(String searchText);

	SaveCQLLibraryResult saveFinalizedVersion(String libraryId, boolean isMajor, String version);

	SaveCQLLibraryResult saveDraftFromVersion(String libraryId);

	SaveCQLLibraryResult searchForDraft(String searchText);

	SaveUpdateCQLResult saveAndModifyCQLGeneralInfo(String libraryId, String context);

	SaveUpdateCQLResult getLibraryCQLFileData(String libraryId);

	SaveCQLLibraryResult getUserShareInfo(String cqlId, String searchText);

	SaveCQLLibraryResult searchForIncludes(String searchText);

	void updateUsersShare(SaveCQLLibraryResult result);

	SaveUpdateCQLResult saveIncludeLibrayInCQLLookUp(String libraryId, CQLIncludeLibrary toBeModifiedObj,
			CQLIncludeLibrary currentObj, List<CQLIncludeLibrary> incLibraryList);

	SaveUpdateCQLResult deleteInclude(String libraryId, CQLIncludeLibrary toBeModifiedIncludeObj,
			CQLIncludeLibrary cqlLibObject, List<CQLIncludeLibrary> viewIncludeLibrarys);

	GetUsedCQLArtifactsResult getUsedCqlArtifacts(String libraryId);

	int countNumberOfAssociation(String id);

}
