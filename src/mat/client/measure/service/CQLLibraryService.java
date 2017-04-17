package mat.client.measure.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mat.client.umls.service.VsacApiResult;
import mat.model.CQLValueSetTransferObject;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLKeywords;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.SaveUpdateCQLResult;

@RemoteServiceRelativePath("cqlLibrary")
public interface CQLLibraryService extends RemoteService {
	SaveCQLLibraryResult search(String searchText, int filter, int startIndex, int pageSize);

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

	SaveCQLLibraryResult getUserShareInfo(String cqlId, String searchText);

	SaveCQLLibraryResult searchForIncludes(String setId, String searchText);
	
	void updateUsersShare(SaveCQLLibraryResult result);

	SaveUpdateCQLResult saveIncludeLibrayInCQLLookUp(String libraryId, CQLIncludeLibrary toBeModifiedObj,
			CQLIncludeLibrary currentObj, List<CQLIncludeLibrary> incLibraryList);

	SaveUpdateCQLResult deleteInclude(String libraryId, CQLIncludeLibrary toBeModifiedIncludeObj,
			CQLIncludeLibrary cqlLibObject, List<CQLIncludeLibrary> viewIncludeLibrarys);

	GetUsedCQLArtifactsResult getUsedCqlArtifacts(String libraryId);

	int countNumberOfAssociation(String Id);
	
	SaveUpdateCQLResult saveCQLValueset(CQLValueSetTransferObject valueSetTransferObject);

	SaveUpdateCQLResult deleteValueSet(String toBeDelValueSetId, String libraryId);

	SaveUpdateCQLResult saveAndModifyDefinitions(String libraryId, CQLDefinition toBeModifiedObj,
			CQLDefinition currentObj, List<CQLDefinition> definitionList);
	
	SaveUpdateCQLResult saveAndModifyFunctions(String libraryId, CQLFunctions toBeModifiedObj, CQLFunctions currentObj,
			List<CQLFunctions> functionsList);
	
	SaveUpdateCQLResult saveAndModifyParameters(String libraryId, CQLParameter toBeModifiedObj, CQLParameter currentObj,
			List<CQLParameter> parameterList);
	
	SaveUpdateCQLResult deleteDefinition(String libraryId, CQLDefinition toBeDeletedObj, CQLDefinition currentObj,
			List<CQLDefinition> definitionList);
	
	SaveUpdateCQLResult deleteFunctions(String libraryId, CQLFunctions toBeDeletedObj, CQLFunctions currentObj,
			List<CQLFunctions> functionsList);
	
	SaveUpdateCQLResult deleteParameter(String libraryId, CQLParameter toBeDeletedObj, CQLParameter currentObj,
			List<CQLParameter> parameterList);
	
	void updateCQLLibraryXMLForExpansionProfile(List<CQLQualityDataSetDTO> modifyWithDTO, String measureId, String expansionProfile);
	
	SaveUpdateCQLResult saveCQLUserDefinedValueset(CQLValueSetTransferObject matValueSetTransferObject);
	
	SaveUpdateCQLResult modifyCQLValueSets(CQLValueSetTransferObject matValueSetTransferObject);
	
	VsacApiResult updateCQLVSACValueSets(String currentCQLLibraryId, String expansionId);
	 CQLKeywords getCQLKeywordsLists();
	 void transferLibraryOwnerShipToUser(List<String> list, String toEmail);
}
