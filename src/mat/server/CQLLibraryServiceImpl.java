package mat.server;

import java.util.List;

import mat.client.measure.service.CQLLibraryService;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLLibraryDataSetObject;
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
	public SaveCQLLibraryResult searchForIncludes(String searchText){
		return this.getCQLLibraryService().searchForIncludes(searchText);
	}
	
	@Override
	public CQLLibraryDataSetObject findCQLLibraryByID(String cqlLibraryID){
		return this.getCQLLibraryService().findCQLLibraryByID(cqlLibraryID);
	}
	
	@Override
	public SaveCQLLibraryResult searchForVersion(String searchText){
		return this.getCQLLibraryService().searchForVersion(searchText);
	}
	@Override
	public SaveCQLLibraryResult searchForDraft(String searchText){
		return this.getCQLLibraryService().searchForDraft(searchText);
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
	
}
