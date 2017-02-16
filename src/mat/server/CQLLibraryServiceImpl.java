package mat.server;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import mat.client.measure.service.CQLLibraryService;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.dao.clause.CQLLibraryDAO;
import mat.model.LockedUserInfo;
import mat.model.clause.CQLLibrary;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.server.service.CQLLibraryServiceInterface;
import mat.server.util.XmlProcessor;
import mat.shared.SaveUpdateCQLResult;

public class CQLLibraryServiceImpl extends SpringRemoteServiceServlet implements CQLLibraryService{
	private static final long serialVersionUID = -2412573290030426288L;

	/** The cql library dao. */
	@Autowired
	private CQLLibraryDAO cqlLibraryDAO;
	
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
		boolean isLocked = cqlLibraryDAO.isLibraryLocked(id);
		return isLocked;
	}

	@Override
	public SaveCQLLibraryResult resetLockedDate(String currentLibraryId, String userId) {
		CQLLibrary existingLibrary = null;
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		if ((currentLibraryId != null) && (userId != null) && !currentLibraryId.isEmpty()) {
			existingLibrary = cqlLibraryDAO.find(currentLibraryId);
			if (existingLibrary != null) {
				if ((existingLibrary.getLockedUserId() != null) && existingLibrary.getLockedUserId().toString().equalsIgnoreCase(userId)) {
					// Only if the lockedUser and loggedIn User are same we can
					// allow the user to unlock the measure.
					if (existingLibrary.getLockedOutDate() != null) {
						// if it is not null then set it to null and save it.
						existingLibrary.setLockedOutDate(null);
						existingLibrary.setLockedUserId(null);
						cqlLibraryDAO.updateLockedOutDate(existingLibrary);
						result.setSuccess(true);
					}
				}
			}
			result.setId(existingLibrary.getId());
		}
		
		return result;
	}

	@Override
	public SaveCQLLibraryResult updateLockedDate(String currentLibraryId, String userId) {
		CQLLibrary existingmeasure = null;
		LockedUserInfo lockedUserInfo = null;
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		if ((currentLibraryId != null) && (userId != null)) {
			existingmeasure = cqlLibraryDAO.find(currentLibraryId);
			if (existingmeasure != null) {
				if (!cqlLibraryDAO.isLibraryLocked(existingmeasure.getId())) {
					lockedUserInfo.setUserId(userId);
					existingmeasure.setLockedUserId(lockedUserInfo);
					existingmeasure.setLockedOutDate(new Timestamp(new Date().getTime()));
					cqlLibraryDAO.save(existingmeasure);
					result.setSuccess(true);
				}
			}
		}
		
		result.setId(existingmeasure.getId());
		return result;
	}

}
