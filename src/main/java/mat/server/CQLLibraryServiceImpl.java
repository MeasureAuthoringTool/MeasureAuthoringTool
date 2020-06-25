package mat.server;

import mat.client.measure.service.CQLLibraryService;
import mat.client.measure.service.SaveCQLLibraryResult;
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
import mat.server.service.CQLLibraryServiceInterface;
import mat.server.util.XmlProcessor;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.LibrarySearchModel;
import mat.shared.SaveUpdateCQLResult;
import mat.shared.cql.error.InvalidLibraryException;
import mat.shared.error.AuthenticationException;

import java.util.List;

public class CQLLibraryServiceImpl extends SpringRemoteServiceServlet implements CQLLibraryService {
	private static final long serialVersionUID = -2412573290030426288L;

	@Override
	public SaveCQLLibraryResult search(LibrarySearchModel librarySearchModel) {
		return this.getCQLLibraryService().search(librarySearchModel);
	}
	@Override
	public SaveCQLLibraryResult searchForIncludes(String setId, String libraryName, String searchText){
		return this.getCQLLibraryService().searchForIncludes(setId, libraryName, searchText);
	}
	
	@Override
	public SaveCQLLibraryResult searchForReplaceLibraries(String setId) {
		return this.getCQLLibraryService().searchForReplaceLibraries(setId);

	}
	
	@Override
	public CQLLibraryDataSetObject findCQLLibraryByID(String cqlLibraryID){
		return this.getCQLLibraryService().findCQLLibraryByID(cqlLibraryID);
	}
	
	@Override
	public SaveCQLLibraryResult saveFinalizedVersion(String libraryId, boolean isMajor, String version, boolean ignoreUnusedLibraries){
		return this.getCQLLibraryService().saveFinalizedVersion(libraryId, isMajor, version, ignoreUnusedLibraries);
	}
	/**
	 * Gets the measure library service.
	 * 
	 * @return the measure library service
	 */
	public CQLLibraryServiceInterface getCQLLibraryService(){
		return (CQLLibraryServiceInterface) context.getBean("cqlLibraryService");
	}
	
	
	public SaveCQLLibraryResult saveCQLLibrary(CQLLibraryDataSetObject cqlLibraryDataSetObject) {
		return this.getCQLLibraryService().saveLibrary(cqlLibraryDataSetObject);
	}
	
	public String createCQLLookUpTag(String libraryName,String version) {
		return this.getCQLLibraryService().createCQLLookUpTag(libraryName, version);
	}
	
	public XmlProcessor loadCQLXmlTemplateFile() {
		return this.getCQLLibraryService().loadCQLXmlTemplateFile();
	}
	public SaveUpdateCQLResult getCQLData(String id) {
		return this.getCQLLibraryService().getCQLData(id);
	}
	
	public SaveUpdateCQLResult getCQLDataForLoad(String id) {
		return this.getCQLLibraryService().getCQLDataForLoad(id);
	}
	
	@Override
	public boolean isLibraryLocked(String id) {
		return this.getCQLLibraryService().isLibraryLocked(id);
	}

	@Override
	public SaveCQLLibraryResult resetLockedDate(String currentLibraryId, String userId) {
		return this.getCQLLibraryService().resetLockedDate(currentLibraryId, userId);
	}

	@Override
	public SaveCQLLibraryResult updateLockedDate(String currentLibraryId, String userId) {
		return this.getCQLLibraryService().updateLockedDate(currentLibraryId, userId);
	}

	@Override
	public SaveCQLLibraryResult getAllRecentCQLLibrariesForUser(String userId) {
		return this.getCQLLibraryService().getAllRecentCQLLibrariesForUser(userId);
	}
	
	@Override
	public void isLibraryAvailableAndLogRecentActivity(String libraryid, String userId){
		this.getCQLLibraryService().isLibraryAvailableAndLogRecentActivity(libraryid, userId);
	}
	
	@Override
	public SaveUpdateCQLResult getLibraryCQLFileData(String libraryId){
	   return this.getCQLLibraryService().getLibraryCQLFileData(libraryId);
	}
	
	@Override
	public SaveUpdateCQLResult getCQLLibraryFileData(String libraryId) {
		return this.getCQLLibraryService().getCQLLibraryFileData(libraryId);
	}
	
	@Override
	public SaveUpdateCQLResult saveAndModifyCQLGeneralInfo(String libraryId, String libraryValue, String libraryComment){
		return this.getCQLLibraryService().saveAndModifyCQLGeneralInfo(libraryId, libraryValue, libraryComment);
	}

	@Override
	public SaveCQLLibraryResult saveDraftFromVersion(String libraryId, String libraryName) throws MatException {
		return this.getCQLLibraryService().draftExistingCQLLibrary(libraryId, libraryName);
	}
	
	@Override
	public SaveCQLLibraryResult getUserShareInfo(String cqlId, final String searchText){
		return this.getCQLLibraryService().getUserShareInfo(cqlId, searchText);
	}
	@Override
	public void updateUsersShare(SaveCQLLibraryResult result) {
		this.getCQLLibraryService().updateUsersShare(result);
	} 
	@Override
	public SaveUpdateCQLResult saveIncludeLibrayInCQLLookUp(String libraryId, CQLIncludeLibrary toBeModifiedObj,
			CQLIncludeLibrary currentObj, List<CQLIncludeLibrary> incLibraryList) throws InvalidLibraryException {
		return this.getCQLLibraryService().saveIncludeLibrayInCQLLookUp(libraryId, toBeModifiedObj, currentObj, incLibraryList);
	}
	@Override
	public SaveUpdateCQLResult deleteInclude(String libraryId, CQLIncludeLibrary toBeDeletedIncludeObj) {
		return this.getCQLLibraryService().deleteInclude(libraryId, toBeDeletedIncludeObj);
	}
	@Override
	public GetUsedCQLArtifactsResult getUsedCqlArtifacts(String libraryId) {
		return this.getCQLLibraryService().getUsedCqlArtifacts(libraryId);
	}
	@Override
	public int countNumberOfAssociation(String Id) {
		return this.getCQLLibraryService().countNumberOfAssociation(Id);
	}
	@Override
	public SaveUpdateCQLResult saveCQLValueset(CQLValueSetTransferObject valueSetTransferObject) {
		return this.getCQLLibraryService().saveCQLValueset(valueSetTransferObject);
	}
	@Override
	public SaveUpdateCQLResult deleteValueSet(String toBeDelValueSetId, String libraryId) {
		return this.getCQLLibraryService().deleteValueSet(toBeDelValueSetId, libraryId);
	}
	
	@Override
	public SaveUpdateCQLResult deleteCode(String toBeDeletedId, String libraryId) {
		return this.getCQLLibraryService().deleteCode(toBeDeletedId, libraryId);
	}
	
	@Override
	public SaveUpdateCQLResult saveCQLFile(String libraryId, String cql) {
		return this.getCQLLibraryService().saveCQLFile(libraryId, cql);
	}
	
	@Override
	public SaveUpdateCQLResult saveAndModifyDefinitions(String libraryId, CQLDefinition toBeModifiedObj,
			CQLDefinition currentObj, List<CQLDefinition> definitionList, boolean isFormatable) {
		return this.getCQLLibraryService().saveAndModifyDefinitions(libraryId, toBeModifiedObj, currentObj, definitionList, isFormatable);
	}
	@Override
	public
	SaveUpdateCQLResult saveAndModifyFunctions(String libraryId, CQLFunctions toBeModifiedObj, CQLFunctions currentObj,
			List<CQLFunctions> functionsList, boolean isFormatable) {
		return this.getCQLLibraryService().saveAndModifyFunctions(libraryId, toBeModifiedObj, currentObj, functionsList, isFormatable);
	}
	@Override
	public SaveUpdateCQLResult saveAndModifyParameters(String libraryId, CQLParameter toBeModifiedObj, CQLParameter currentObj,
			List<CQLParameter> parameterList, boolean isFormatable) {
		return this.getCQLLibraryService().saveAndModifyParameters(libraryId, toBeModifiedObj, currentObj, parameterList, isFormatable);
	}
	@Override
	public SaveUpdateCQLResult deleteDefinition(String libraryId, CQLDefinition toBeDeletedObj) {
		return this.getCQLLibraryService().deleteDefinition(libraryId, toBeDeletedObj);
	}
	@Override
	public SaveUpdateCQLResult deleteFunction(String libraryId, CQLFunctions toBeDeletedObj) {
		return this.getCQLLibraryService().deleteFunction(libraryId, toBeDeletedObj);
	}
	@Override
	public SaveUpdateCQLResult deleteParameter(String libraryId, CQLParameter toBeDeletedObj) {
		return this.getCQLLibraryService().deleteParameter(libraryId, toBeDeletedObj);
	}

	@Override
	public VsacApiResult updateCQLVSACValueSets(String currentCQLLibraryId, String expansionId) {
		String sessionId = getThreadLocalRequest().getSession().getId();
		return this.getCQLLibraryService().updateCQLVSACValueSets(currentCQLLibraryId, expansionId, sessionId);
	}
	@Override
	public CQLKeywords getCQLKeywordsLists() {
		return this.getCQLLibraryService().getCQLKeywordsLists();
	}
	@Override
	public 
	void transferLibraryOwnerShipToUser(List<String> list, String toEmail){
		this.getCQLLibraryService().transferLibraryOwnerShipToUser(list, toEmail);
	}
	
	@Override
	public SaveUpdateCQLResult saveCQLCodestoCQLLibrary(MatCodeTransferObject transferObject){
		return this.getCQLLibraryService().saveCQLCodestoCQLLibrary(transferObject);
	}
	
	@Override
	public SaveUpdateCQLResult saveCQLCodeListToCQLLibrary(
			List<CQLCode> codeList, String libraryId) {
		return this.getCQLLibraryService().saveCQLCodeListToCQLLibrary(codeList, libraryId);
	}
		
	@Override
	public final void deleteCQLLibrary(final String cqllibId, String loginUserId) throws AuthenticationException {
		 this.getCQLLibraryService().deleteCQLLibrary(cqllibId, loginUserId);
	}
	@Override
	public CQLQualityDataModelWrapper saveValueSetList(List<CQLValueSetTransferObject> transferObjectList,
			List<CQLQualityDataSetDTO> appliedValueSetList, String cqlLibraryId) {
		return this.getCQLLibraryService().saveValueSetList(transferObjectList, appliedValueSetList, cqlLibraryId);
	}
}
