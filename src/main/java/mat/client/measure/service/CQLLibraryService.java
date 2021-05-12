package mat.client.measure.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import mat.client.shared.MatException;
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

import java.util.List;

@RemoteServiceRelativePath("cqlLibrary")
public interface CQLLibraryService extends RemoteService {
	SaveCQLLibraryResult search(LibrarySearchModel librarySearchModel);

	CQLLibraryDataSetObject findCQLLibraryByID(String cqlLibraryID);

	SaveCQLLibraryResult saveCQLLibrary(CQLLibraryDataSetObject cqlModel);

	SaveUpdateCQLResult getCQLData(String id);

	SaveUpdateCQLResult getCQLDataForLoad(String id);

	boolean isLibraryLocked(String id);

	SaveCQLLibraryResult resetLockedDate(String currentLibraryId, String userId);

	SaveCQLLibraryResult updateLockedDate(String currentLibraryId, String userId);

	SaveCQLLibraryResult getAllRecentCQLLibrariesForUser(String userId);

	void isLibraryAvailableAndLogRecentActivity(String libraryid, String userId);

	SaveCQLLibraryResult saveFinalizedVersion(String libraryId,
											  boolean isMajor,
											  String version,
											  boolean ignoreUnusedLibraries,
											  boolean keepAll);

	SaveCQLLibraryResult saveDraftFromVersion(String libraryId, String libraryName) throws MatException;

	SaveUpdateCQLResult getLibraryCQLFileData(String libraryId);

	SaveUpdateCQLResult saveAndModifyCQLGeneralInfo(String libraryId, String libraryValue, String libraryComment, String description, String stewardId, boolean isExperimental);

	SaveUpdateCQLResult saveCQLFile(String libraryid, String cql);

	SaveCQLLibraryResult getUserShareInfo(String cqlId, String searchText);

	SaveCQLLibraryResult searchForIncludes(String setId, String libraryName, String searchText, String modelType);

	SaveCQLLibraryResult searchForReplaceLibraries(String setId);

	void updateUsersShare(SaveCQLLibraryResult result);

	SaveUpdateCQLResult saveIncludeLibrayInCQLLookUp(String libraryId, CQLIncludeLibrary toBeModifiedObj,
			CQLIncludeLibrary currentObj, List<CQLIncludeLibrary> incLibraryList) throws InvalidLibraryException;

	SaveUpdateCQLResult deleteInclude(String libraryId, CQLIncludeLibrary toBeModifiedIncludeObj);

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

	SaveUpdateCQLResult deleteDefinition(String libraryId, CQLDefinition toBeDeletedObj);

	SaveUpdateCQLResult deleteFunction(String libraryId, CQLFunctions toBeDeletedObj);

	SaveUpdateCQLResult deleteParameter(String libraryId, CQLParameter toBeDeletedObj);

	VsacApiResult updateCQLVSACValueSets(String currentCQLLibraryId, String expansionId);
	 CQLKeywords getCQLKeywordsLists();
	 void transferLibraryOwnerShipToUser(List<String> list, String toEmail);

	SaveUpdateCQLResult saveCQLCodestoCQLLibrary(MatCodeTransferObject transferObject);

	SaveUpdateCQLResult saveCQLCodeListToCQLLibrary(List<CQLCode> codeList, String libraryId);

	SaveUpdateCQLResult deleteCode(String toBeDeletedId, String libraryId);

	void deleteCQLLibrary(String cqllibId, String loginUserId) throws AuthenticationException;

	SaveUpdateCQLResult getCQLLibraryFileData(String libraryId);

	CQLQualityDataModelWrapper saveValueSetList(List<CQLValueSetTransferObject> transferObjectList,
			List<CQLQualityDataSetDTO> appliedValueSetList, String cqlLibraryId);

    FhirConvertResultResponse convertCqlLibrary(CQLLibraryDataSetObject object) throws MatException;
}
