package mat.server;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;


import mat.dao.clause.CQLLibraryDAO;
import mat.server.CQLServiceImpl;
import mat.model.LockedUserInfo;
import mat.model.User;
import mat.model.clause.CQLLibrary;
import mat.model.clause.MeasureSet;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLModel;
import mat.server.service.CQLLibraryServiceInterface;
import mat.server.service.UserService;
import mat.server.util.MeasureUtility;
import mat.shared.SaveUpdateCQLResult;

public class CQLLibraryService implements CQLLibraryServiceInterface {
	@Autowired
	private CQLLibraryDAO cqlLibraryDAO;
	@Autowired
	private CQLServiceImpl cqlService;
	
	/** The context. */
	@Autowired
	private ApplicationContext context;
	
	/** The lock threshold. */
	private final long lockThreshold = 3 * 60 * 1000; // 3 minutes

	@Override
	public List<CQLLibraryDataSetObject> search(String searchText, String searchFrom) {
	//	List<CQLLibraryModel> cqlLibraries = new ArrayList<CQLLibraryModel>();
		List<CQLLibraryDataSetObject> allLibraries = new ArrayList<CQLLibraryDataSetObject>();
		List<CQLLibrary> list = cqlLibraryDAO.search(searchText,searchFrom);
		for(CQLLibrary cqlLibrary : list){
			CQLLibraryDataSetObject object = extractCQLLibraryDataObject(cqlLibrary);
			allLibraries.add(object);
		}
		return allLibraries;
	}
	
	@Override
	public void save(String libraryName, String measureId, User owner, MeasureSet measureSet, String version, String releaseVersion, 
			Timestamp finalizedDate, byte[] cqlByteArray) {
		
		CQLLibrary cqlLibrary = new CQLLibrary(); 
		if(libraryName.length() >200){
			libraryName = libraryName.substring(0, 199);
		}
		cqlLibrary.setName(libraryName);
		cqlLibrary.setMeasureId(measureId);
		cqlLibrary.setOwnerId(owner);
		cqlLibrary.setMeasureSet(measureSet);
		cqlLibrary.setVersion(version);
		cqlLibrary.setReleaseVersion(releaseVersion);
		// TODO CQL SET
		// cqlLibrary.setCqlSetId(cqlSetId);
		cqlLibrary.setDraft(false);
		cqlLibrary.setFinalizedDate(finalizedDate);
		cqlLibrary.setCQLByteArray(cqlByteArray);
		
		this.cqlLibraryDAO.save(cqlLibrary);
	}
	
	/**
	 * Method to extract from DB CQLLibrary object to Client side DTO.
	 * @param cqlLibrary
	 * @return
	 */
	private CQLLibraryDataSetObject extractCQLLibraryDataObject(CQLLibrary cqlLibrary){
		
		CQLLibraryDataSetObject dataSetObject = new CQLLibraryDataSetObject();
		dataSetObject.setId(cqlLibrary.getId());
		dataSetObject.setCqlName(cqlLibrary.getName());
		dataSetObject.setDraft(cqlLibrary.isDraft());
		dataSetObject.setReleaseVersion(cqlLibrary.getReleaseVersion());
		dataSetObject.setFinalizedDate(cqlLibrary.getFinalizedDate());
		dataSetObject.setMeasureId(cqlLibrary.getMeasureId());
		boolean isLocked =isLocked(cqlLibrary.getLockedOutDate());
		
		if (isLocked && (cqlLibrary.getLockedUserId() != null)) {
			LockedUserInfo lockedUserInfo = new LockedUserInfo();
			lockedUserInfo.setUserId(cqlLibrary.getLockedUserId().getUserId());
			lockedUserInfo.setEmailAddress(cqlLibrary.getLockedUserId()
					.getEmailAddress());
			lockedUserInfo.setFirstName(cqlLibrary.getLockedUserId().getFirstName());
			lockedUserInfo.setLastName(cqlLibrary.getLockedUserId().getLastName());
			dataSetObject.setLockedUserInfo(lockedUserInfo);
		}
		
		dataSetObject.setLocked(isLocked);
		dataSetObject.setLockedUserInfo(cqlLibrary.getLockedUserId());
		User user = getUserService().getById(cqlLibrary.getOwnerId().getId());
		dataSetObject.setOwnerFirstName(user.getFirstName());
		dataSetObject.setOwnerLastName(user.getLastName());
		dataSetObject.setOwnerEmailAddress(user.getEmailAddress());
		
		String formattedVersion = MeasureUtility.getVersionTextWithRevisionNumber(cqlLibrary.getVersion(), "", cqlLibrary.isDraft());
		dataSetObject.setVersion(formattedVersion);
		
		
		CQLModel cqlModel = new CQLModel();
		byte[] bdata;
		try {
			bdata = cqlLibrary.getCqlXML().getBytes(1, (int) cqlLibrary.getCqlXML().length());
			String data = new String(bdata);
			cqlModel = CQLUtilityClass.getCQLStringFromMeasureXML(data,"");
			String cqlFileString = CQLUtilityClass.getCqlString(cqlModel,"").toString();
			//SaveUpdateCQLResult result = cqlService.parseCQLStringForError(cqlFileString);
			dataSetObject.setCqlText(cqlFileString);
			//dataSetObject.setCqlModel(cqlModel);
			//dataSetObject.setCqlErrors(result.getCqlErrors());;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
		return dataSetObject;
		
	}

	private boolean isLocked(Date lockedOutDate) {
			
			boolean locked = false;
			if (lockedOutDate == null) {
				return locked;
			}
			long currentTime = System.currentTimeMillis();
			long lockedOutTime = lockedOutDate.getTime();
			long timeDiff = currentTime - lockedOutTime;
			locked = timeDiff < lockThreshold;
			
			return locked;
		}
	
	private UserService getUserService() {
		return (UserService) context.getBean("userService");
	}
	
	@Override
	public CQLLibraryDataSetObject findCQLLibraryByID(String cqlLibraryId){
		return extractCQLLibraryDataObject(cqlLibraryDAO.find(cqlLibraryId)); 
	}
}
