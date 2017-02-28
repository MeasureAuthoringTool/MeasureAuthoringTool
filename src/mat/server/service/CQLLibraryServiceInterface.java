package mat.server.service;

import mat.client.measure.service.SaveCQLLibraryResult;
import mat.model.clause.CQLLibrary;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.server.util.XmlProcessor;
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

	//SaveCQLLibraryResult searchForDraft(String searchText);
	
	
}
