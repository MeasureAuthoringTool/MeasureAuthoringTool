package mat.client.measure.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

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

public interface CQLLibraryServiceAsync {

	void search(String searchText, int filter, int startIndex,int pageSize, AsyncCallback<SaveCQLLibraryResult> callback);

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

	void getUserShareInfo(String cqlId, String searchText,
			AsyncCallback<SaveCQLLibraryResult> callback);

	void searchForIncludes(String referringID, String searchText, AsyncCallback<SaveCQLLibraryResult> callback);

	void updateUsersShare(SaveCQLLibraryResult result, AsyncCallback<Void> callback);

	void saveIncludeLibrayInCQLLookUp(String libraryId, CQLIncludeLibrary toBeModifiedObj, CQLIncludeLibrary currentObj,
			List<CQLIncludeLibrary> incLibraryList, AsyncCallback<SaveUpdateCQLResult> callback);

	void deleteInclude(String libraryId, CQLIncludeLibrary toBeModifiedIncludeObj, CQLIncludeLibrary cqlLibObject,
			List<CQLIncludeLibrary> viewIncludeLibrarys, AsyncCallback<SaveUpdateCQLResult> callback);

	void getUsedCqlArtifacts(String libraryId, AsyncCallback<GetUsedCQLArtifactsResult> callback);

	void countNumberOfAssociation(String Id, AsyncCallback<Integer> callback);

	void saveAndModifyDefinitions(String libraryId, CQLDefinition toBeModifiedObj, CQLDefinition currentObj,
			List<CQLDefinition> definitionList, AsyncCallback<SaveUpdateCQLResult> callback);

	void saveAndModifyFunctions(String libraryId, CQLFunctions toBeModifiedObj, CQLFunctions currentObj,
			List<CQLFunctions> functionsList, AsyncCallback<SaveUpdateCQLResult> callback);

	void saveAndModifyParameters(String libraryId, CQLParameter toBeModifiedObj, CQLParameter currentObj,
			List<CQLParameter> parameterList, AsyncCallback<SaveUpdateCQLResult> callback);

	void deleteDefinition(String libraryId, CQLDefinition toBeDeletedObj, CQLDefinition currentObj,
			List<CQLDefinition> definitionList, AsyncCallback<SaveUpdateCQLResult> callback);

	void deleteFunctions(String libraryId, CQLFunctions toBeDeletedObj, CQLFunctions currentObj,
			List<CQLFunctions> functionsList, AsyncCallback<SaveUpdateCQLResult> callback);

	void deleteParameter(String libraryId, CQLParameter toBeDeletedObj, CQLParameter currentObj,
			List<CQLParameter> parameterList, AsyncCallback<SaveUpdateCQLResult> callback);
	
	void saveCQLValueset(CQLValueSetTransferObject valueSetTransferObject, AsyncCallback<SaveUpdateCQLResult> asyncCallback);
	
	void deleteValueSet(String toBeDeletedValueSetId, String currentMeasureId,
			AsyncCallback<SaveUpdateCQLResult> asyncCallback);

	void updateCQLLibraryXMLForExpansionProfile(List<CQLQualityDataSetDTO> modifiedCqlQDMList, String libraryId,
			String expProfileToAllValueSet, AsyncCallback<Void> asyncCallback);

	void saveCQLUserDefinedValueset(CQLValueSetTransferObject matValueSetTransferObject,
			AsyncCallback<SaveUpdateCQLResult> asyncCallback);

	void modifyCQLValueSets(CQLValueSetTransferObject matValueSetTransferObject,
			AsyncCallback<SaveUpdateCQLResult> asyncCallback);

	void updateCQLVSACValueSets(String currentCQLLibraryId, String expansionId,
			AsyncCallback<VsacApiResult> asyncCallback);

	void getCQLKeywordsLists(AsyncCallback<CQLKeywords> callback);

	void transferLibraryOwnerShipToUser(List<String> list, String toEmail, AsyncCallback<Void> callback);
}
