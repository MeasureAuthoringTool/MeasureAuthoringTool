package mat.server;

import java.util.List;

import mat.client.measure.service.CQLLibraryService;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.client.umls.service.VsacApiResult;
import mat.model.CQLValueSetTransferObject;
import mat.model.MatCodeTransferObject;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLKeywords;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.server.service.CQLLibraryServiceInterface;
import mat.server.util.XmlProcessor;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.SaveUpdateCQLResult;

public class CQLLibraryServiceImpl extends SpringRemoteServiceServlet implements CQLLibraryService{
	private static final long serialVersionUID = -2412573290030426288L;

	@Override
	public SaveCQLLibraryResult search(String searchText,int filter, int startIndex, int pageSize) {
		return this.getCQLLibraryService().search(searchText,filter, startIndex,pageSize);
	}
	@Override
	public SaveCQLLibraryResult searchForIncludes(String setId, String searchText){
		return this.getCQLLibraryService().searchForIncludes(setId,searchText);
	}
	
	@Override
	public CQLLibraryDataSetObject findCQLLibraryByID(String cqlLibraryID){
		return this.getCQLLibraryService().findCQLLibraryByID(cqlLibraryID);
	}
	
	@Override
	public SaveCQLLibraryResult saveFinalizedVersion(String libraryId, boolean isMajor, String version){
		return this.getCQLLibraryService().saveFinalizedVersion(libraryId, isMajor, version);
	}
	/**
	 * Gets the measure library service.
	 * 
	 * @return the measure library service
	 */
	public CQLLibraryServiceInterface getCQLLibraryService(){
		return (CQLLibraryServiceInterface) context.getBean("cqlLibraryService");
	}
	
	
	public SaveCQLLibraryResult save(CQLLibraryDataSetObject cqlLibraryDataSetObject) {
		return this.getCQLLibraryService().save(cqlLibraryDataSetObject);
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
		// TODO Auto-generated method stub
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
	public SaveUpdateCQLResult saveAndModifyCQLGeneralInfo(String libraryId, String libraryValue){
		return this.getCQLLibraryService().saveAndModifyCQLGeneralInfo(libraryId, libraryValue);
	}

	@Override
	public SaveCQLLibraryResult saveDraftFromVersion(String libraryId) {
		// TODO Auto-generated method stub
		return this.getCQLLibraryService().saveDraftFromVersion(libraryId);
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
			CQLIncludeLibrary currentObj, List<CQLIncludeLibrary> incLibraryList) {
		return this.getCQLLibraryService().saveIncludeLibrayInCQLLookUp(libraryId, toBeModifiedObj, currentObj, incLibraryList);
	}
	@Override
	public SaveUpdateCQLResult deleteInclude(String libraryId, CQLIncludeLibrary toBeModifiedIncludeObj,
			CQLIncludeLibrary cqlLibObject, List<CQLIncludeLibrary> viewIncludeLibrarys) {
		return this.getCQLLibraryService().deleteInclude(libraryId, toBeModifiedIncludeObj, cqlLibObject, viewIncludeLibrarys);
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
	public SaveUpdateCQLResult saveAndModifyDefinitions(String libraryId, CQLDefinition toBeModifiedObj,
			CQLDefinition currentObj, List<CQLDefinition> definitionList) {
		return this.getCQLLibraryService().saveAndModifyDefinitions(libraryId, toBeModifiedObj, currentObj, definitionList);
	}
	@Override
	public
	SaveUpdateCQLResult saveAndModifyFunctions(String libraryId, CQLFunctions toBeModifiedObj, CQLFunctions currentObj,
			List<CQLFunctions> functionsList) {
		return this.getCQLLibraryService().saveAndModifyFunctions(libraryId, toBeModifiedObj, currentObj, functionsList);
	}
	@Override
	public SaveUpdateCQLResult saveAndModifyParameters(String libraryId, CQLParameter toBeModifiedObj, CQLParameter currentObj,
			List<CQLParameter> parameterList) {
		return this.getCQLLibraryService().saveAndModifyParameters(libraryId, toBeModifiedObj, currentObj, parameterList);
	}
	@Override
	public SaveUpdateCQLResult deleteDefinition(String libraryId, CQLDefinition toBeDeletedObj, CQLDefinition currentObj,
			List<CQLDefinition> definitionList) {
		return this.getCQLLibraryService().deleteDefinition(libraryId, toBeDeletedObj, currentObj, definitionList);
	}
	@Override
	public SaveUpdateCQLResult deleteFunctions(String libraryId, CQLFunctions toBeDeletedObj, CQLFunctions currentObj,
			List<CQLFunctions> functionsList) {
		return this.getCQLLibraryService().deleteFunctions(libraryId, toBeDeletedObj, currentObj, functionsList);
	}
	@Override
	public SaveUpdateCQLResult deleteParameter(String libraryId, CQLParameter toBeDeletedObj, CQLParameter currentObj,
			List<CQLParameter> parameterList) {
		return this.getCQLLibraryService().deleteParameter(libraryId, toBeDeletedObj, currentObj, parameterList);
	}
	@Override
	public void updateCQLLibraryXMLForExpansionProfile(List<CQLQualityDataSetDTO> modifyWithDTO, String measureId,
			String expansionProfile) {
		this.getCQLLibraryService().updateCQLLibraryXMLForExpansionProfile(modifyWithDTO, measureId, expansionProfile);
	}
	@Override
	public SaveUpdateCQLResult saveCQLUserDefinedValueset(CQLValueSetTransferObject matValueSetTransferObject) {
		return this.getCQLLibraryService().saveCQLUserDefinedValueset(matValueSetTransferObject);
	}
	@Override
	public SaveUpdateCQLResult modifyCQLValueSets(CQLValueSetTransferObject matValueSetTransferObject) {
		return this.getCQLLibraryService().modifyCQLValueSets(matValueSetTransferObject);
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
	
}
