package mat.client.measure.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mat.client.umls.service.VsacApiResult;
import mat.model.CQLValueSetTransferObject;
import mat.model.MatCodeTransferObject;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLKeywords;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataModelWrapper;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.LibrarySearchModel;
import mat.shared.SaveUpdateCQLResult;
import mat.shared.cql.error.InvalidLibraryException;
import mat.shared.error.AuthenticationException;

@RemoteServiceRelativePath("cqlLibrary")
public interface CQLLibraryService extends RemoteService {
	SaveCQLLibraryResult search(LibrarySearchModel librarySearchModel);

	CQLLibraryDataSetObject findCQLLibraryByID(String cqlLibraryID);

	SaveCQLLibraryResult save(CQLLibraryDataSetObject cqlModel);

	SaveUpdateCQLResult getCQLData(String id);

	SaveUpdateCQLResult getCQLDataForLoad(String id);
	
	boolean isLibraryLocked(String id);

	SaveCQLLibraryResult resetLockedDate(String currentLibraryId, String userId);

	SaveCQLLibraryResult updateLockedDate(String currentLibraryId, String userId);

	SaveCQLLibraryResult getAllRecentCQLLibrariesForUser(String userId);

	void isLibraryAvailableAndLogRecentActivity(String libraryid, String userId);

	
	SaveCQLLibraryResult saveFinalizedVersion(String libraryId, boolean isMajor, String version, boolean ignoreUnusedLibraries);
	
	SaveCQLLibraryResult saveDraftFromVersion(String libraryId);
	
	SaveUpdateCQLResult getLibraryCQLFileData(String libraryId);

	SaveUpdateCQLResult saveAndModifyCQLGeneralInfo(String libraryId, String libraryValue, String libraryComment);

	SaveCQLLibraryResult getUserShareInfo(String cqlId, String searchText);

	SaveCQLLibraryResult searchForIncludes(String setId, String libraryName, String searchText);
	
	SaveCQLLibraryResult searchForReplaceLibraries(String setId);
		
	void updateUsersShare(SaveCQLLibraryResult result);

	SaveUpdateCQLResult saveIncludeLibrayInCQLLookUp(String libraryId, CQLIncludeLibrary toBeModifiedObj,
			CQLIncludeLibrary currentObj, List<CQLIncludeLibrary> incLibraryList) throws InvalidLibraryException;

	SaveUpdateCQLResult deleteInclude(String libraryId, CQLIncludeLibrary toBeModifiedIncludeObj,
			List<CQLIncludeLibrary> viewIncludeLibrarys);

	GetUsedCQLArtifactsResult getUsedCqlArtifacts(String libraryId);

	int countNumberOfAssociation(String Id);
	
	SaveUpdateCQLResult saveCQLValueset(CQLValueSetTransferObject valueSetTransferObject);

	SaveUpdateCQLResult deleteValueSet(String toBeDelValueSetId, String libraryId);

	SaveUpdateCQLResult saveAndModifyDefinitions(String libraryId, CQLDefinition toBeModifiedObj,
			CQLDefinition currentObj, List<CQLDefinition> definitionList, boolean isFormatable);
	
	SaveUpdateCQLResult saveAndModifyFunctions(String libraryId, CQLFunctions toBeModifiedObj, CQLFunctions currentObj,
			List<CQLFunctions> functionsList, boolean isFormatable);
	
	SaveUpdateCQLResult saveAndModifyParameters(String libraryId, CQLParameter toBeModifiedObj, CQLParameter currentObj,
			List<CQLParameter> parameterList, boolean isFormatable);
	
	SaveUpdateCQLResult deleteDefinition(String libraryId, CQLDefinition toBeDeletedObj, 
			List<CQLDefinition> definitionList);
	
	SaveUpdateCQLResult deleteFunctions(String libraryId, CQLFunctions toBeDeletedObj, 
			List<CQLFunctions> functionsList);
	
	SaveUpdateCQLResult deleteParameter(String libraryId, CQLParameter toBeDeletedObj, 
			List<CQLParameter> parameterList);
		
	SaveUpdateCQLResult saveCQLUserDefinedValueset(CQLValueSetTransferObject matValueSetTransferObject);
	
	SaveUpdateCQLResult modifyCQLValueSets(CQLValueSetTransferObject matValueSetTransferObject);
	
	VsacApiResult updateCQLVSACValueSets(String currentCQLLibraryId, String expansionId);
	 CQLKeywords getCQLKeywordsLists();
	 void transferLibraryOwnerShipToUser(List<String> list, String toEmail);

	SaveUpdateCQLResult saveCQLCodestoCQLLibrary(MatCodeTransferObject transferObject);
	
	SaveUpdateCQLResult saveCQLCodeListToCQLLibrary(List<CQLCode> codeList, String libraryId);
	
	SaveUpdateCQLResult modifyCQLCodeInCQLLibrary(CQLCode codeToReplace, CQLCode replacementCode, String cqlLibraryId);
	
	SaveUpdateCQLResult deleteCode(String toBeDeletedId, String libraryId);

	void deleteCQLLibrary(String cqllibId, String loginUserId, String password) throws AuthenticationException;

	SaveUpdateCQLResult getCQLLibraryFileData(String libraryId);

	CQLQualityDataModelWrapper saveValueSetList(List<CQLValueSetTransferObject> transferObjectList,
			List<CQLQualityDataSetDTO> appliedValueSetList, String cqlLibraryId);
}
