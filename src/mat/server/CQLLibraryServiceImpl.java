package mat.server;

import mat.client.measure.service.CQLLibraryService;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.server.service.CQLLibraryServiceInterface;
import mat.server.util.XmlProcessor;
import mat.shared.SaveUpdateCQLResult;

public class CQLLibraryServiceImpl extends SpringRemoteServiceServlet implements CQLLibraryService{
	private static final long serialVersionUID = -2412573290030426288L;

	@Override
	public SaveCQLLibraryResult search(String searchText,String searchFrom, int filter, int startIndex, int pageSize) {
		return this.getCQLLibraryService().search(searchText,searchFrom, filter,startIndex, pageSize);
	}
	
	@Override
	public CQLLibraryDataSetObject findCQLLibraryByID(String cqlLibraryID){
		return this.getCQLLibraryService().findCQLLibraryByID(cqlLibraryID);
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
}
