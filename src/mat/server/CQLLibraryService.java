package mat.server;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import mat.client.clause.cqlworkspace.CQLWorkSpaceConstants;
import mat.client.measure.service.CQLService;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.client.shared.MatContext;
import mat.dao.CQLLibraryAuditLogDAO;
import mat.dao.RecentCQLActivityLogDAO;
import mat.dao.UserDAO;
import mat.dao.clause.CQLLibraryDAO;
import mat.dao.clause.CQLLibrarySetDAO;
import mat.dao.clause.CQLLibraryShareDAO;
import mat.dao.clause.ShareLevelDAO;
import mat.model.CQLValueSetTransferObject;
import mat.model.LockedUserInfo;
import mat.model.RecentCQLActivityLog;
import mat.model.SecurityRole;
import mat.model.User;
import mat.model.clause.CQLLibrary;
import mat.model.clause.CQLLibrarySet;
import mat.model.clause.ShareLevel;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLKeywords;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLLibraryShare;
import mat.model.cql.CQLLibraryShareDTO;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.server.service.CQLLibraryServiceInterface;
import mat.server.service.UserService;
import mat.server.service.impl.MatContextServiceUtil;
import mat.server.util.MATPropertiesService;
import mat.server.util.MeasureUtility;
import mat.server.util.ResourceLoader;
import mat.server.util.XmlProcessor;
import mat.shared.CQLModelValidator;
import mat.shared.ConstantMessages;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.SaveUpdateCQLResult;

// TODO: Auto-generated Javadoc
/**
 * The Class CQLLibraryService.
 */
public class CQLLibraryService implements CQLLibraryServiceInterface {
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(CQLLibraryService.class);
	
	/** The cql library DAO. */
	@Autowired
	private CQLLibraryDAO cqlLibraryDAO;
	
	/** The cql library set DAO. */
	@Autowired
	private CQLLibrarySetDAO cqlLibrarySetDAO;
	
	/** The user DAO. */
	@Autowired
	private UserDAO userDAO;

	/** The cql service. */
	@Autowired
	private CQLService cqlService;
	
	@Autowired
	private ShareLevelDAO shareLevelDAO;
	
	@Autowired
	private CQLLibraryShareDAO cqlLibraryShareDAO;
	
	@Autowired
	private CQLLibraryAuditLogDAO cqlLibraryAuditLogDAO;
	
	/** The context. */
	@Autowired
	private ApplicationContext context;

	/** The recent CQL activity log DAO. */
	@Autowired
	private RecentCQLActivityLogDAO recentCQLActivityLogDAO;
	
	/** The x path. */
	javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
	
	/** The lock threshold. */
	private final long lockThreshold = 3 * 60 * 1000; // 3 minutes
	
	
	@Override
	public SaveCQLLibraryResult searchForIncludes(String searchText){
		SaveCQLLibraryResult saveCQLLibraryResult = new SaveCQLLibraryResult();
		List<CQLLibraryDataSetObject> allLibraries = new ArrayList<CQLLibraryDataSetObject>();
		List<CQLLibrary> list = cqlLibraryDAO.searchForIncludes(searchText);
		
		saveCQLLibraryResult.setResultsTotal(list.size());
		
		for(CQLLibrary cqlLibrary : list){
			CQLLibraryDataSetObject object = extractCQLLibraryDataObject(cqlLibrary);
			allLibraries.add(object);
		}
		
		saveCQLLibraryResult.setCqlLibraryDataSetObjects(allLibraries);
		return saveCQLLibraryResult;
	}
	
	

	/* (non-Javadoc)
	 * @see mat.server.service.CQLLibraryServiceInterface#search(java.lang.String, java.lang.String, int, int, int)
	 */
	@Override
	public SaveCQLLibraryResult search(String searchText, String searchFrom, int filter,int startIndex, int pageSize) {
	//	List<CQLLibraryModel> cqlLibraries = new ArrayList<CQLLibraryModel>();
		SaveCQLLibraryResult searchModel = new SaveCQLLibraryResult();
		List<CQLLibraryDataSetObject> allLibraries = new ArrayList<CQLLibraryDataSetObject>();
		
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		List<CQLLibraryShareDTO> list = cqlLibraryDAO.search(searchText,searchFrom, Integer.MAX_VALUE,user,filter);
		
		searchModel.setResultsTotal(list.size());
		
		if (pageSize <= list.size()) {
			list = list
					.subList(startIndex - 1, pageSize);
		} else if (pageSize > list.size()) {
			list = list.subList(startIndex - 1,
					list.size());
		}
		
		for(CQLLibraryShareDTO dto : list){
			CQLLibraryDataSetObject object = extractCQLLibraryDataObjectFromShareDTO(user, dto  );
			allLibraries.add(object);
		}
	
		updateCQLLibraryFamily(allLibraries);
		searchModel.setCqlLibraryDataSetObjects(allLibraries);
		
		return searchModel;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.CQLLibraryServiceInterface#searchForVersion(java.lang.String)
	 */
	@Override
	public SaveCQLLibraryResult searchForVersion(String searchText){
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		
		ArrayList<CQLLibraryDataSetObject> cqList = new ArrayList<CQLLibraryDataSetObject>();
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		// To reuse existing search, filter is passed as -1 which otherwise has value 0 or 1
		List<CQLLibraryShareDTO> list = cqlLibraryDAO.search(searchText,"StandAlone", Integer.MAX_VALUE,user,-1);
		
		for(CQLLibraryShareDTO shareDTO : list){
			CQLLibraryDataSetObject cqlLibraryDataSetObject = extractCQLLibraryDataObjectFromShareDTO(user, shareDTO);
			
			if(cqlLibraryDataSetObject != null) {
				boolean canVersion = false;
				if(!cqlLibraryDataSetObject.isDraft()){
					canVersion = false;
				} else {
					if(cqlLibraryDataSetObject.isLocked()){
						canVersion = false;
					} else {
						if(user.getSecurityRole().getDescription().equalsIgnoreCase(SecurityRole.SUPER_USER_ROLE)){
							canVersion = true;
						} else {
							if(shareDTO.getOwnerUserId()
									.equalsIgnoreCase(
											user.getId())){
								canVersion = true; 
							} else {
								if(shareDTO.getShareLevel() == null){
									canVersion = false;
								} else {
									if(shareDTO.getShareLevel().equalsIgnoreCase(ShareLevel.MODIFY_ID)){
										canVersion = true;
									} else {
										canVersion = false;
									}
								}
								
							}
						}
					}
				}
				if(canVersion){
					cqList.add(cqlLibraryDataSetObject);
				}
			}
		}
		result.setResultsTotal(cqList.size());
		result.setCqlLibraryDataSetObjects(cqList);
		return result;
		
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.CQLLibraryServiceInterface#searchForDraft(java.lang.String)
	 */
	@Override
	public SaveCQLLibraryResult searchForDraft(String searchText) {
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();

		ArrayList<CQLLibraryDataSetObject> cqList = new ArrayList<CQLLibraryDataSetObject>();
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		// To reuse existing search, filter is passed as -1 which otherwise has value 0 or 1
		List<CQLLibraryShareDTO> list = cqlLibraryDAO.search(searchText, "StandAlone", Integer.MAX_VALUE, user, -1);
		HashSet<String> hasDraft = new HashSet<String>();
		for (CQLLibraryShareDTO library : list) {
			if (library.isDraft()) {
				String setId = library.getCqlLibrarySetId();
				hasDraft.add(setId);
			}
		}
		
		for (CQLLibraryShareDTO shareDTO : list) {
			CQLLibraryDataSetObject cqlLibraryDataSetObject = extractCQLLibraryDataObjectFromShareDTO(user, shareDTO);

			if (cqlLibraryDataSetObject != null) {
				boolean canDraft = false;
				if (cqlLibraryDataSetObject.isDraft()) {
					canDraft = false;
				} else {
						if(hasDraft.contains(shareDTO.getCqlLibrarySetId())){
							canDraft = false;
						} else {
							if (cqlLibraryDataSetObject.isLocked()) {
								canDraft = false;
							} else {
								if (user.getSecurityRole().getDescription()
									.equalsIgnoreCase(SecurityRole.SUPER_USER_ROLE)) {
									canDraft = true;
								} else {
									if(shareDTO.getOwnerUserId()
											.equalsIgnoreCase(
													user.getId())){
										canDraft = true; 
									} else {
										if(shareDTO.getShareLevel() == null){
											canDraft = false;
										} else {
											if(shareDTO.getShareLevel().equalsIgnoreCase(ShareLevel.MODIFY_ID)){
												canDraft = true;
											} else {
												canDraft = false;
											}
										}
										
									}
								}
							}
						}
					}
				if (canDraft) {
					cqList.add(cqlLibraryDataSetObject);
				}
			}
		}
		result.setResultsTotal(cqList.size());
		result.setCqlLibraryDataSetObjects(cqList);
		return result;
	}
	
	
	
	/* (non-Javadoc)
	 * @see mat.server.service.CQLLibraryServiceInterface#save(mat.model.clause.CQLLibrary)
	 */
	@Override
	public void save(CQLLibrary library) {
		this.cqlLibraryDAO.save(library);
	}
	
	/**
	 * Method to extract from DB CQLLibrary object to Client side DTO.
	 *
	 * @param cqlLibrary the cql library
	 * @return the CQL library data set object
	 */
	private CQLLibraryDataSetObject extractCQLLibraryDataObject(CQLLibrary cqlLibrary){
		
		CQLLibraryDataSetObject dataSetObject = new CQLLibraryDataSetObject();
		dataSetObject.setId(cqlLibrary.getId());
		dataSetObject.setCqlName(cqlLibrary.getName());
		dataSetObject.setDraft(cqlLibrary.isDraft());
		dataSetObject.setReleaseVersion(cqlLibrary.getReleaseVersion());
		if(cqlLibrary.getRevisionNumber() == null){
			dataSetObject.setRevisionNumber("000");
		} else {
			dataSetObject.setRevisionNumber(cqlLibrary.getRevisionNumber());
		}
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
		dataSetObject.setOwnerId(user.getId());
		/*if(cqlLibrary.getCqlSet()!=null){
			dataSetObject.setCqlSetId(cqlLibrary.getCqlSet().getId());	
		}*/
		//if(cqlLibrary.getMeasureId()==null){
		dataSetObject.setCqlSetId(cqlLibrary.getSet_id());
	//}
		//to check if the library is sharable library
		String currentUserId = LoggedInUserUtil.getLoggedInUser();
		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(userRole);
		boolean isOwner = currentUserId.equals(user.getId());
		dataSetObject.setSharable(isOwner || isSuperUser);
		
		String formattedVersion = MeasureUtility.getVersionTextWithRevisionNumber(cqlLibrary.getVersion(), 
				cqlLibrary.getRevisionNumber(), cqlLibrary.isDraft());
		dataSetObject.setVersion(formattedVersion);        
		
		dataSetObject.setEditable(MatContextServiceUtil.get()
				.isCurrentCQLLibraryEditable(cqlLibraryDAO, cqlLibrary.getId()));
		
		return dataSetObject;
		
	}

	
	/**
	 * Checks if is locked.
	 *
	 * @param lockedOutDate the locked out date
	 * @return true, if is locked
	 */
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
	
	/**
	 * Gets the user service.
	 *
	 * @return the user service
	 */
	private UserService getUserService() {
		return (UserService) context.getBean("userService");
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.CQLLibraryServiceInterface#findCQLLibraryByID(java.lang.String)
	 */
	@Override
	public CQLLibraryDataSetObject findCQLLibraryByID(String cqlLibraryId){
		CQLLibrary cqlLibrary = cqlLibraryDAO.find(cqlLibraryId);
		CQLLibraryDataSetObject cqlLibraryDataSetObject = extractCQLLibraryDataObject(cqlLibrary);
		cqlLibraryDataSetObject.setCqlText(getCQLLibraryData(cqlLibrary));
		return cqlLibraryDataSetObject; 
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.CQLLibraryServiceInterface#saveDraftFromVersion(java.lang.String)
	 */
	@Override
	public SaveCQLLibraryResult saveDraftFromVersion(String libraryId){
		
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		CQLLibrary existingLibrary = cqlLibraryDAO.find(libraryId);
		boolean isDraftable = MatContextServiceUtil.get().isCurrentCQLLibraryDraftable(
				cqlLibraryDAO, libraryId);
		if(existingLibrary != null && isDraftable){
			CQLLibrary newLibraryObject = new CQLLibrary();
			newLibraryObject.setDraft(true);
			newLibraryObject.setName(existingLibrary.getName());
			newLibraryObject.setSet_id(existingLibrary.getSet_id());;
			newLibraryObject.setOwnerId(existingLibrary.getOwnerId());
			newLibraryObject.setReleaseVersion(existingLibrary.getReleaseVersion());
			newLibraryObject.setQdmVersion(existingLibrary.getQdmVersion());
			newLibraryObject.setCQLByteArray(existingLibrary.getCQLByteArray());
			newLibraryObject.setVersion(existingLibrary.getVersion());
			newLibraryObject.setRevisionNumber("000");
			save(newLibraryObject);
			result.setSuccess(true);
			result.setId(newLibraryObject.getId());
			result.setCqlLibraryName(newLibraryObject.getName());
			String formattedVersion = MeasureUtility.getVersionTextWithRevisionNumber(newLibraryObject.getVersion(), 
					newLibraryObject.getRevisionNumber(), newLibraryObject.isDraft());
			result.setVersionStr(formattedVersion);
			result.setEditable(isDraftable);
			
		} else {
			result.setSuccess(false);
			result.setFailureReason(SaveCQLLibraryResult.INVALID_DATA);
		}
		return result;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.server.service.CQLLibraryServiceInterface#saveFinalizedVersion(java.lang.String, boolean, java.lang.String)
	 */
	@Override
	public SaveCQLLibraryResult saveFinalizedVersion (String libraryId,  boolean isMajor,
			 String version){
		logger.info("Inside saveFinalizedVersion: Start");
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		
		CQLLibrary library = cqlLibraryDAO.find(libraryId);
		if(library != null){
			String versionNumber = null;
			if (isMajor) {
				versionNumber = cqlLibraryDAO.findMaxVersion(library.getSet_id());
				if (versionNumber == null) {
					versionNumber = "0.000";
				}
				logger.info("Max Version Number loaded from DB: " + versionNumber);
			} else {
				int versionIndex = version.indexOf('v');
				logger.info("Min Version number passed from Page Model: " + versionIndex);
				String selectedVersion = version.substring(versionIndex + 1);
				logger.info("Min Version number after trim: " + selectedVersion);
				versionNumber = cqlLibraryDAO.findMaxOfMinVersion(library.getSet_id(), selectedVersion);

			}
			
			int endIndex = versionNumber.indexOf('.');
			String majorVersionNumber = versionNumber.substring(0, endIndex);
			if (!versionNumber.equalsIgnoreCase(ConstantMessages.MAXIMUM_ALLOWED_VERSION)) {
				String[] versionArr = versionNumber.split("\\.");
				if (isMajor) {
					if (!versionArr[0].equalsIgnoreCase(ConstantMessages.MAXIMUM_ALLOWED_MAJOR_VERSION)) {
						logger.info("Inside saveFinalizedVersion: incrementVersionNumberAndSave Start");
						return incrementVersionNumberAndSave(majorVersionNumber, "1", library);
					} else {
						logger.info("Inside saveFinalizedVersion: returnFailureReason  isMajor Start");
						result.setFailureReason(SaveCQLLibraryResult.REACHED_MAXIMUM_MAJOR_VERSION);
						result.setSuccess(false);
						return result;
					}

				} else {
					if (!versionArr[1].equalsIgnoreCase(ConstantMessages.MAXIMUM_ALLOWED_MINOR_VERSION)) {
						versionNumber = versionArr[0] + "." + versionArr[1];
						logger.info("Inside saveFinalizedVersion: incrementVersionNumberAndSave Start");
						return incrementVersionNumberAndSave(versionNumber, "0.001", library);
					} else {
						logger.info("Inside saveFinalizedVersion: returnFailureReason NOT isMajor Start");
						result.setFailureReason(SaveCQLLibraryResult.REACHED_MAXIMUM_MINOR_VERSION);
						result.setSuccess(false);
						return result;
					}
				}

			} else {
				logger.info("Inside saveFinalizedVersion: returnFailureReason MAX Major Minor Reached");
				result.setFailureReason(SaveCQLLibraryResult.REACHED_MAXIMUM_VERSION);
				result.setSuccess(false);
				return result;
				
			}
			
			
		} else {
			
			result.setFailureReason(SaveCQLLibraryResult.INVALID_DATA);
			result.setSuccess(false);
			return result;
		}
		
		
	}
	
	
	/**
	 * Increment version number and save.
	 *
	 * @param maximumVersionNumber the maximum version number
	 * @param incrementBy the increment by
	 * @param library the library
	 * @return the save CQL library result
	 */
	private SaveCQLLibraryResult incrementVersionNumberAndSave(String maximumVersionNumber, String incrementBy,
			 CQLLibrary library) {
		BigDecimal mVersion = new BigDecimal(maximumVersionNumber);
		mVersion = mVersion.add(new BigDecimal(incrementBy));
		library.setVersion(mVersion.toString());
		Date currentDate = new Date();
		long time = currentDate.getTime();
		Timestamp timestamp = new Timestamp(time);
		library.setFinalizedDate(timestamp);
		library.setDraft(false);
		
		String versionStr = mVersion.toString();
		// Divide the number by 1 and check for a remainder.
		// Any whole number should always have a remainder of 0 when divided by
		// 1.
		// For major versions, there may be case the minor version value is
		// zero.
		// THis makes the BigDecimal as Integer value causing issue while
		// formatVersionText method.
		// To fix that we are explicitly appending .0 in versionString.
		if (mVersion.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
			versionStr = versionStr.concat(".0");
		}
		// method to update version node to newly requested version value.
		XmlProcessor xmlProcessor = new XmlProcessor(getCQLLibraryXml(library));
		updateCQLVersion(xmlProcessor, library.getRevisionNumber(),versionStr);
		library.setCQLByteArray(xmlProcessor.transform(xmlProcessor.getOriginalDoc()).getBytes());
		cqlLibraryDAO.save(library);
		
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		result.setSuccess(true);
		result.setId(library.getId());

		result.setVersionStr(versionStr);
		logger.info("Result passed for Version Number " + versionStr);
		return result;
	}
	
	
	/**
	 * Update CQL version.
	 *
	 * @param processor the processor
	 * @param revisionNumber the revision number
	 * @param version the version
	 */
	private void updateCQLVersion(XmlProcessor processor,String revisionNumber ,String version) {
		String cqlVersionXPath = "//cqlLookUp/version";
		try {
			Node node = (Node) xPath.evaluate(cqlVersionXPath, processor.getOriginalDoc().getDocumentElement(),
					XPathConstants.NODE);
			if (node != null) {
				node.setTextContent(MeasureUtility.formatVersionText(revisionNumber, version));
			}
		} catch (XPathExpressionException e) {
			logger.error(e.getMessage());
		}

	}
	
	
	/* (non-Javadoc)
	 * @see mat.server.service.CQLLibraryServiceInterface#save(mat.model.cql.CQLLibraryDataSetObject)
	 */
	@Override
	public SaveCQLLibraryResult save(CQLLibraryDataSetObject cqlLibraryDataSetObject) {

		if (cqlLibraryDataSetObject != null) {
			cqlLibraryDataSetObject.scrubForMarkUp();
		}

		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		List<String> message = isValidCQLLibrary(cqlLibraryDataSetObject);
		if (message.size() == 0) {
			CQLLibrary library = new CQLLibrary();
			library.setDraft(true);
			library.setName(cqlLibraryDataSetObject.getCqlName());

			CQLLibrarySet cqlLibrarySet = new CQLLibrarySet();
			cqlLibrarySet.setId(UUID.randomUUID().toString());
			cqlLibrarySetDAO.save(cqlLibrarySet);

			library.setSet_id(cqlLibrarySet.getId());;
			library.setReleaseVersion(MATPropertiesService.get().getCurrentReleaseVersion());
			library.setQdmVersion(MATPropertiesService.get().getQmdVersion());
			library.setRevisionNumber("000");
			library.setVersion("0.0");
			if (LoggedInUserUtil.getLoggedInUser() != null) {
				User currentUser = userDAO.find(LoggedInUserUtil.getLoggedInUser());
				library.setOwnerId(currentUser);
			} else {
				result.setSuccess(false);
				result.setFailureReason(result.INVALID_USER);
				return result;
			}
			String cqlLookUpString = createCQLLookUpTag(cqlLibraryDataSetObject.getCqlName(),library.getVersion()+"."+library.getRevisionNumber());
			if (cqlLookUpString != null && !cqlLookUpString.isEmpty()) {
				byte[] cqlByteArray = cqlLookUpString.getBytes();
				library.setCQLByteArray(cqlByteArray);
				cqlLibraryDAO.save(library);
				result.setSuccess(true);
				result.setId(library.getId());
				result.setCqlLibraryName(cqlLibraryDataSetObject.getCqlName());
				result.setVersionStr(library.getVersion()+"."+library.getRevisionNumber());
				result.setEditable(MatContextServiceUtil.get()
						.isCurrentCQLLibraryEditable(cqlLibraryDAO, library.getId()));
			} else {
				result.setSuccess(false);
				result.setFailureReason(result.INVALID_CQL);
				return result;
			}
			return result;
		} else {
			result.setSuccess(false);
			result.setFailureReason(result.INVALID_DATA);
			return result;
		}
		//return result;
	}

	/**
	 * Checks if is valid CQL library.
	 *
	 * @param model the model
	 * @return the list
	 */
	private List<String> isValidCQLLibrary(CQLLibraryDataSetObject model) {

		List<String> message = new ArrayList<String>();

		if ((model.getCqlName() == null) || "".equals(model.getCqlName().trim())) {
			message.add(MatContext.get().getMessageDelegate().getLibraryNameRequired());
		} else {
			CQLModelValidator cqlLibraryModel = new CQLModelValidator();
			boolean isValid = cqlLibraryModel.validateForAliasNameSpecialChar(model.getCqlName());
			if(!isValid){
				message.add(MatContext.get().getMessageDelegate().getCqlStandAloneLibraryNameError());
			}
			
		}

		return message;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.CQLLibraryServiceInterface#createCQLLookUpTag(java.lang.String, java.lang.String)
	 */
	@Override
	public String createCQLLookUpTag(String libraryName, String version){
		XmlProcessor xmlProcessor = loadCQLXmlTemplateFile();
		String cqlLookUpString = getCQLLookUpXml(libraryName, version,xmlProcessor,"//standAlone");
		return cqlLookUpString;
	}
	
	
	
	
	/**
	 * Gets the CQL look up xml.
	 *
	 * @param libraryName the library name
	 * @param versionText the version text
	 * @param xmlProcessor the xml processor
	 * @param mainXPath the main X path
	 * @return the CQL look up xml
	 */
	@Override
	public String getCQLLookUpXml(String libraryName, String versionText, XmlProcessor xmlProcessor,String mainXPath) {
		String cqlLookUp = null;
		try {
			Node cqlTemplateNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), "/cqlTemplate");
			Node cqlLookUpNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), mainXPath+"/cqlLookUp");
			String xPath_ID = mainXPath+"/cqlLookUp/child::node()/*[@id]";
			String xPath_UUID = mainXPath+"/cqlLookUp/child::node()/*[@uuid]";
			if (cqlTemplateNode != null) {

				if (cqlTemplateNode.getAttributes().getNamedItem("changeAttribute") != null) {
					String[] attributeToBeModified = cqlTemplateNode.getAttributes().getNamedItem("changeAttribute")
							.getNodeValue().split(",");
					for (String changeAttribute : attributeToBeModified) {
						if (changeAttribute.equalsIgnoreCase("id")) {
							NodeList nodesForId = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), xPath_ID);
							for (int i = 0; i < nodesForId.getLength(); i++) {
								Node node = nodesForId.item(i);
								node.getAttributes().getNamedItem("id")
										.setNodeValue(UUID.randomUUID().toString());
							}
						} else if (changeAttribute.equalsIgnoreCase("uuid")) {
							NodeList nodesForUUId = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(),
									xPath_UUID);
							for (int i = 0; i < nodesForUUId.getLength(); i++) {
								Node node = nodesForUUId.item(i);
								node.getAttributes().getNamedItem("uuid")
										.setNodeValue(UUID.randomUUID().toString());
							}
						}
					}
				}

				if (cqlTemplateNode.getAttributes().getNamedItem("changeNodeTextContent") != null) {
					String[] nodeTextToBeModified = cqlTemplateNode.getAttributes()
							.getNamedItem("changeNodeTextContent").getNodeValue().split(",");
					for (String nodeTextToChange : nodeTextToBeModified) {
						if (nodeTextToChange.equalsIgnoreCase("library")) {
							Node libraryNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(),
									mainXPath+"//" + nodeTextToChange);
							if (libraryNode != null) {
								libraryNode.setTextContent(libraryName);
							}
						} else if (nodeTextToChange.equalsIgnoreCase("version")) {
							Node versionNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(),
									mainXPath+"//" + nodeTextToChange);
							if (versionNode != null) {
								versionNode.setTextContent(versionText);
							}
						} else if (nodeTextToChange.equalsIgnoreCase("usingModelVersion")) {
							Node usingModelVersionNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(),
									mainXPath+"//" + nodeTextToChange);
							if (usingModelVersionNode != null) {
								usingModelVersionNode.setTextContent(MATPropertiesService.get().getQmdVersion());
							}
						}
					}
				}

			}
			System.out.println(xmlProcessor.transform(cqlLookUpNode));
			cqlLookUp = xmlProcessor.transform(cqlLookUpNode);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return cqlLookUp;
	}

	/* (non-Javadoc)
	 * @see mat.server.service.CQLLibraryServiceInterface#loadCQLXmlTemplateFile()
	 */
	@Override
	public  XmlProcessor loadCQLXmlTemplateFile() {
		String fileName = "CQLXmlTemplate.xml";
		URL templateFileUrl = new ResourceLoader().getResourceAsURL(fileName);
		File templateFile = null;
		try {
			templateFile = new File(templateFileUrl.toURI());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		XmlProcessor templateXMLProcessor = new XmlProcessor(templateFile);
		return templateXMLProcessor;
	}
	
	/**
	 * Gets the CQL library data.
	 *
	 * @param cqlLibrary the cql library
	 * @return the CQL library data
	 */
	private String getCQLLibraryData(CQLLibrary cqlLibrary){
		CQLModel cqlModel = new CQLModel();
		byte[] bdata;
		String cqlFileString = null;
		try {
			bdata = cqlLibrary.getCqlXML().getBytes(1, (int) cqlLibrary.getCqlXML().length());
			String data = new String(bdata);
			cqlModel = CQLUtilityClass.getCQLStringFromXML(data);
			cqlFileString = CQLUtilityClass.getCqlString(cqlModel,"").toString();
			//SaveUpdateCQLResult result = cqlService.parseCQLStringForError(cqlFileString);
			//dataSetObject.setCqlModel(cqlModel);
			//dataSetObject.setCqlErrors(result.getCqlErrors());;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cqlFileString;
	}

	/* (non-Javadoc)
	 * @see mat.server.service.CQLLibraryServiceInterface#getCQLData(java.lang.String)
	 */
	@Override
	public SaveUpdateCQLResult getCQLData(String id) {
		SaveUpdateCQLResult cqlResult = new SaveUpdateCQLResult();
		CQLLibrary cqlLibrary = cqlLibraryDAO.find(id);
		String str = getCQLLibraryXml(cqlLibrary);
		
		if(str != null) {
			cqlResult = cqlService.getCQLData(str);
			cqlResult.setSuccess(true);
		}
		
		return cqlResult;
		
	}
	
	
	/**
	 * Gets the CQL library xml.
	 *
	 * @param library the library
	 * @return the CQL library xml
	 */
	private String getCQLLibraryXml(CQLLibrary library){
		String xmlString = null;
		//CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
		if(library != null ){
			try {
				xmlString = new String(library.getCqlXML().getBytes(1l, (int) library.getCqlXML().length()));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return xmlString;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.server.service.CQLLibraryServiceInterface#isLibraryLocked(java.lang.String)
	 */
	@Override
	public boolean isLibraryLocked(String id) {
		boolean isLocked = cqlLibraryDAO.isLibraryLocked(id);
		return isLocked;
	}

	/* (non-Javadoc)
	 * @see mat.server.service.CQLLibraryServiceInterface#resetLockedDate(java.lang.String, java.lang.String)
	 */
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

	/* (non-Javadoc)
	 * @see mat.server.service.CQLLibraryServiceInterface#updateLockedDate(java.lang.String, java.lang.String)
	 */
	@Override
	public SaveCQLLibraryResult updateLockedDate(String currentLibraryId, String userId) {
		CQLLibrary existingmeasure = null;
		LockedUserInfo lockedUserInfo = new LockedUserInfo();
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
	
	
	/* (non-Javadoc)
	 * @see mat.server.service.CQLLibraryServiceInterface#getAllRecentCQLLibrariesForUser(java.lang.String)
	 */
	@Override
	public SaveCQLLibraryResult getAllRecentCQLLibrariesForUser(String userId) {
		
		ArrayList<RecentCQLActivityLog> recentLibActivityList = (ArrayList<RecentCQLActivityLog>) recentCQLActivityLogDAO.getRecentCQLLibraryActivityLog(userId);
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		List<CQLLibraryDataSetObject> cqlLibraryDataSetObjects = new ArrayList<CQLLibraryDataSetObject> ();
		for (RecentCQLActivityLog activityLog : recentLibActivityList) {
			CQLLibrary library = cqlLibraryDAO.find(activityLog.getCqlId());
			CQLLibraryDataSetObject object = extractCQLLibraryDataObject(library);
			cqlLibraryDataSetObjects.add(object);
		}
		result.setCqlLibraryDataSetObjects(cqlLibraryDataSetObjects);
		result.setResultsTotal(cqlLibraryDataSetObjects.size());
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.CQLLibraryServiceInterface#isLibraryAvailableAndLogRecentActivity(java.lang.String, java.lang.String)
	 */
	@Override
	public void isLibraryAvailableAndLogRecentActivity(String libraryid, String userId){
		CQLLibrary library = cqlLibraryDAO.find(libraryid);
		if(library != null){
			recentCQLActivityLogDAO.recordRecentCQLLibraryActivity(libraryid, userId);
		}
	}
	
	
	
	/**
	 * Save and modify parameters.
	 *
	 * @param libraryId the library id
	 * @param toBeModifiedObj the to be modified obj
	 * @param currentObj the current obj
	 * @param parameterList the parameter list
	 * @return the save update CQL result
	 */
	public SaveUpdateCQLResult saveAndModifyParameters(String libraryId, CQLParameter toBeModifiedObj,
			CQLParameter currentObj, List<CQLParameter> parameterList) {

		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			return null;
		}
		CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
		String cqlXml = getCQLLibraryXml(cqlLibrary);
		SaveUpdateCQLResult result = null;
		if (cqlXml != null) {
			result = cqlService.saveAndModifyParameters(cqlXml, toBeModifiedObj, currentObj, parameterList);
			if (result != null && result.isSuccess()) {
				cqlLibrary.setCQLByteArray(result.getXml().getBytes());
				cqlLibraryDAO.save(cqlLibrary);
			}
		}
		
		return result;
	}
	
	/**
	 * Save and modify definitions.
	 *
	 * @param libraryId the library id
	 * @param toBeModifiedObj the to be modified obj
	 * @param currentObj the current obj
	 * @param definitionList the definition list
	 * @return the save update CQL result
	 */
	public SaveUpdateCQLResult saveAndModifyDefinitions(String libraryId, CQLDefinition toBeModifiedObj,
			CQLDefinition currentObj, List<CQLDefinition> definitionList) {

		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			return null;
		}
		CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
		String cqlXml = getCQLLibraryXml(cqlLibrary);
		SaveUpdateCQLResult result = null;
		if (cqlXml != null) {

			result = cqlService.saveAndModifyDefinitions(cqlXml, toBeModifiedObj,
					currentObj, definitionList);
			if (result != null && result.isSuccess()) {
				cqlLibrary.setCQLByteArray(result.getXml().getBytes());
				cqlLibraryDAO.save(cqlLibrary);
			}
		}
		return result;
	}
	
	
	/**
	 * Save and modify functions.
	 *
	 * @param libraryId the library id
	 * @param toBeModifiedObj the to be modified obj
	 * @param currentObj the current obj
	 * @param functionsList the functions list
	 * @return the save update CQL result
	 */
	public SaveUpdateCQLResult saveAndModifyFunctions(String libraryId, CQLFunctions toBeModifiedObj,
			CQLFunctions currentObj, List<CQLFunctions> functionsList) {
		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			return null;
		}
		CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
		String cqlXml = getCQLLibraryXml(cqlLibrary);
		SaveUpdateCQLResult result = null;
		if (cqlXml != null) {
			result = cqlService.saveAndModifyFunctions(cqlXml, toBeModifiedObj,
					currentObj, functionsList);
			if (result != null && result.isSuccess()) {
				cqlLibrary.setCQLByteArray(result.getXml().getBytes());
				cqlLibraryDAO.save(cqlLibrary);
			}
		}
		return result;
		
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.CQLLibraryServiceInterface#saveAndModifyCQLGeneralInfo(java.lang.String, java.lang.String)
	 */
	@Override
	public SaveUpdateCQLResult saveAndModifyCQLGeneralInfo(String libraryId, String libraryName) {
		SaveUpdateCQLResult result = null;
		if(MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, libraryId)){
			CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
			String cqlXml = getCQLLibraryXml(cqlLibrary);
			if (cqlXml != null) {
				result = cqlService.saveAndModifyCQLGeneralInfo(cqlXml, libraryName);
				if (result != null && result.isSuccess()) {
					cqlLibrary.setName(libraryName);
					cqlLibrary.setCQLByteArray(result.getXml().getBytes());
					cqlLibraryDAO.save(cqlLibrary);
				}
			}
		}
		
		return result;
	}
	
	
	/**
	 * Save include libray in CQL look up.
	 *
	 * @param libraryId the library id
	 * @param toBeModifiedObj the to be modified obj
	 * @param currentObj the current obj
	 * @param incLibraryList the inc library list
	 * @return the save update CQL result
	 */
	public SaveUpdateCQLResult saveIncludeLibrayInCQLLookUp(String libraryId, CQLIncludeLibrary toBeModifiedObj,
			CQLIncludeLibrary currentObj, List<CQLIncludeLibrary> incLibraryList) {

		SaveUpdateCQLResult result = null;
		CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
		if (cqlLibrary != null) {
			int associationCount = cqlService.countNumberOfAssociation(libraryId);
			//System.out.println("============== "+associationCount + "=============");
			if(associationCount < CQLWorkSpaceConstants.VALID_INCLUDE_COUNT){
				String cqlXml = getCQLLibraryXml(cqlLibrary);
				if (cqlXml != null) {
					result = cqlService.saveIncludeLibrayInCQLLookUp(cqlXml, toBeModifiedObj, currentObj, incLibraryList);
					if (result != null && result.isSuccess()) {
						cqlLibrary.setCQLByteArray(result.getXml().getBytes());
						cqlLibraryDAO.save(cqlLibrary);
						cqlService.saveCQLAssociation(currentObj, libraryId);
					}
				}
			}
			
		}

		return result;
	}
	
	
	/**
	 * Delete definition.
	 *
	 * @param libraryId the library id
	 * @param toBeDeletedObj the to be deleted obj
	 * @param currentObj the current obj
	 * @param definitionList the definition list
	 * @return the save update CQL result
	 */
	public SaveUpdateCQLResult deleteDefinition(String libraryId, CQLDefinition toBeDeletedObj,
			CQLDefinition currentObj, List<CQLDefinition> definitionList) {
		
		CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
		String cqlXml = getCQLLibraryXml(cqlLibrary);
		SaveUpdateCQLResult result = null;
		if (cqlXml != null) {
			result = cqlService.deleteDefinition(cqlXml, toBeDeletedObj, currentObj, definitionList);
			if (result != null && result.isSuccess()) {
				cqlLibrary.setCQLByteArray(result.getXml().getBytes());
				cqlLibraryDAO.save(cqlLibrary);
			}
		}
		return result;
	
	}
	
	
	/**
	 * Delete functions.
	 *
	 * @param libraryId the library id
	 * @param toBeDeletedObj the to be deleted obj
	 * @param currentObj the current obj
	 * @param functionsList the functions list
	 * @return the save update CQL result
	 */
	public SaveUpdateCQLResult deleteFunctions(String libraryId, CQLFunctions toBeDeletedObj, CQLFunctions currentObj,
			List<CQLFunctions> functionsList) {
		
		CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
		String cqlXml = getCQLLibraryXml(cqlLibrary);
		SaveUpdateCQLResult result = null;
		if (cqlXml != null) {
			result = cqlService.deleteFunctions(cqlXml, toBeDeletedObj, currentObj, functionsList);
			if (result != null && result.isSuccess()) {
				cqlLibrary.setCQLByteArray(result.getXml().getBytes());
				cqlLibraryDAO.save(cqlLibrary);
			}
		}
		return result;
	}
	
	
	/**
	 * Delete parameter.
	 *
	 * @param libraryId the library id
	 * @param toBeDeletedObj the to be deleted obj
	 * @param currentObj the current obj
	 * @param parameterList the parameter list
	 * @return the save update CQL result
	 */
	public SaveUpdateCQLResult deleteParameter(String libraryId, CQLParameter toBeDeletedObj, CQLParameter currentObj,
			List<CQLParameter> parameterList) {
		
		CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
		String cqlXml = getCQLLibraryXml(cqlLibrary);
		SaveUpdateCQLResult result = null;
		if (cqlXml != null) {
			result = cqlService.deleteParameter(cqlXml, toBeDeletedObj, currentObj, parameterList);
			if (result != null && result.isSuccess()) {
				cqlLibrary.setCQLByteArray(result.getXml().getBytes());
				cqlLibraryDAO.save(cqlLibrary);
			}
		}
		return result;
	}
	
	
	/**
	 * Delete include.
	 *
	 * @param libraryId the library id
	 * @param toBeModifiedIncludeObj the to be modified include obj
	 * @param cqlLibObject the cql lib object
	 * @param viewIncludeLibrarys the view include librarys
	 * @return the save update CQL result
	 */
    @Override 
	public SaveUpdateCQLResult deleteInclude(String libraryId, CQLIncludeLibrary toBeModifiedIncludeObj,
			CQLIncludeLibrary cqlLibObject, List<CQLIncludeLibrary> viewIncludeLibrarys) {
		
		CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
		String cqlXml = getCQLLibraryXml(cqlLibrary);
		SaveUpdateCQLResult result = null;
		if (cqlXml != null) {
			result = cqlService.deleteInclude(cqlXml, toBeModifiedIncludeObj, cqlLibObject,
					viewIncludeLibrarys);
			if (result != null && result.isSuccess()) {
				cqlLibrary.setCQLByteArray(result.getXml().getBytes());
				cqlLibraryDAO.save(cqlLibrary);
				
				cqlService.deleteCQLAssociation(toBeModifiedIncludeObj, cqlLibrary.getId());
			}
		}
		return result;
	}
	
	/**
	 * Gets the CQL keywords lists.
	 *
	 * @return the CQL keywords lists
	 */
	public CQLKeywords getCQLKeywordsLists() {
		return cqlService.getCQLKeyWords();
	}
	
	/**
	 * Gets the used cql artifacts.
	 *
	 * @param libraryId the library id
	 * @return the used cql artifacts
	 */
	@Override
	public GetUsedCQLArtifactsResult getUsedCqlArtifacts(String libraryId) {
		CQLLibrary library = cqlLibraryDAO.find(libraryId);
		String cqlXml = getCQLLibraryXml(library);
		return cqlService.getUsedCQlArtifacts(cqlXml);
	}
	
	
	/* (non-Javadoc)
	 * @see mat.server.service.CQLLibraryServiceInterface#getLibraryCQLFileData(java.lang.String)
	 */
	@Override
	public SaveUpdateCQLResult getLibraryCQLFileData(String libraryId){
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
		String cqlXml = getCQLLibraryXml(cqlLibrary);
		
		if (cqlXml != null && !StringUtils.isEmpty(cqlXml)) {
			result = cqlService.getCQLFileData(cqlXml);
			result.setSuccess(true);
		} else {
			result.setSuccess(false);
		}
		
		return result;
	}
	
	
	/**
	 * Update CQL Library family.
	 *
	 * @param detailModelList
	 *            the detail model list
	 */
	public void updateCQLLibraryFamily(List<CQLLibraryDataSetObject> detailModelList) {
		boolean isFamily = false;
		if ((detailModelList != null) & (detailModelList.size() > 0)) {
			for (int i = 0; i < detailModelList.size(); i++) {
				if (i > 0) {
					if (detailModelList.get(i).getCqlSetId()
							.equalsIgnoreCase(detailModelList.get(i - 1).getCqlSetId())) {
						detailModelList.get(i).setFamily(!isFamily);
					} else {
						detailModelList.get(i).setFamily(isFamily);
					}
				} else {
					detailModelList.get(i).setFamily(isFamily);
				}
			}
		}
	}
	
	
	
	/* (non-Javadoc)
	 * @see mat.server.service.CQLLibraryServiceInterface#getUserShareInfo(java.lang.String, int, int)
	 */
	@Override
	public SaveCQLLibraryResult getUserShareInfo(String cqlId, String searchText){
		SaveCQLLibraryResult result =  new SaveCQLLibraryResult();
		List<CQLLibraryShareDTO> shareDTOList = cqlLibraryDAO
				.getLibraryShareInfoForLibrary(cqlId, searchText);
		result.setCqlLibraryShareDTOs(shareDTOList);
		result.setId(cqlId);
		result.setResultsTotal(shareDTOList.size());
		return result;
	}
	
	/**
	 * Save CQL valueset.
	 *
	 * @param valueSetTransferObject the value set transfer object
	 * @return the save update CQL result
	 */
	public SaveUpdateCQLResult saveCQLValueset(CQLValueSetTransferObject valueSetTransferObject) {
		
		SaveUpdateCQLResult result = null;
		CQLLibrary library = cqlLibraryDAO.find(valueSetTransferObject.getCqlLibraryId());
		if(library != null) {
			result = cqlService.saveCQLValueset(valueSetTransferObject);
			if(result != null && result.isSuccess()) {
				String nodeName = "valueset";
				String parentNode = "//cqlLookUp/valuesets";
				appendAndSaveNode(library, nodeName, result.getXml(), parentNode);
			}
		}
		
		return result;
	}
	
	/**
	 * Save CQL user defined valuesetto measure.
	 *
	 * @param matValueSetTransferObject the mat value set transfer object
	 * @return the save update CQL result
	 */
	public SaveUpdateCQLResult saveCQLUserDefinedValuesettoMeasure(CQLValueSetTransferObject matValueSetTransferObject) {
		
		SaveUpdateCQLResult result = null;
		CQLLibrary library = cqlLibraryDAO.find(matValueSetTransferObject.getCqlLibraryId());
		if(library != null) {
			result = cqlService.saveCQLUserDefinedValueset(matValueSetTransferObject);
			if(result != null && result.isSuccess()) {
				String nodeName = "valueset";
				String parentNode = "//cqlLookUp/valuesets";
				appendAndSaveNode(library, nodeName, result.getXml(), parentNode);
			}
		}
		return result;
	}
	
	
	/**
	 * Modify CQL value sets.
	 *
	 * @param matValueSetTransferObject the mat value set transfer object
	 * @return the save update CQL result
	 */
	public SaveUpdateCQLResult modifyCQLValueSets(CQLValueSetTransferObject matValueSetTransferObject) {
		
		SaveUpdateCQLResult result = null;
		CQLLibrary library = cqlLibraryDAO.find(matValueSetTransferObject.getCqlLibraryId());
		if(library != null) {
			result = cqlService.modifyCQLValueSets(matValueSetTransferObject);
			if(result != null && result.isSuccess()) {
				 result = cqlService.updateCQLLookUpTag(getCQLLibraryXml(library), result.getCqlQualityDataSetDTO(),
							matValueSetTransferObject.getCqlQualityDataSetDTO());
				if(result != null && result.isSuccess()){
					library.setCQLByteArray(result.getXml().getBytes());
					save(library);
				}
			}
		}
		return result;
	}
	
	/**
	 * Delete value set.
	 *
	 * @param toBeDelValueSetId the to be del value set id
	 * @param libraryId the library id
	 * @return the save update CQL result
	 */
	public SaveUpdateCQLResult deleteValueSet(String toBeDelValueSetId, String libraryId) {
		
		SaveUpdateCQLResult cqlResult = new SaveUpdateCQLResult();
		
		CQLLibrary library = cqlLibraryDAO.find(libraryId);
		if(library != null) {
			cqlResult = cqlService.deleteValueSet(getCQLLibraryXml(library), toBeDelValueSetId);
			if(cqlResult != null && cqlResult.isSuccess()){
				library.setCQLByteArray(cqlResult.getXml().getBytes());
				save(library);
			}
		}
		return cqlResult;
	}
	
	/**
	 * Append and save node.
	 *
	 * @param library the library
	 * @param nodeName the node name
	 * @param newXml the new xml
	 * @param parentNode the parent node
	 */
	public final void appendAndSaveNode(CQLLibrary library, final String nodeName, String newXml, String parentNode) {
		
		if ((library != null && !StringUtils.isEmpty(getCQLLibraryXml(library)))
				&& (nodeName != null && StringUtils.isNotBlank(nodeName))) {
			String result = callAppendNode(getCQLLibraryXml(library), newXml, nodeName,
					parentNode);
			library.setCQLByteArray(result.getBytes());
			save(library);
		}

	}
	
	
	/**
	 * Call append node.
	 *
	 * @param xml the xml
	 * @param newXml the new xml
	 * @param nodeName the node name
	 * @param parentNodeName the parent node name
	 * @return the string
	 */
	private String callAppendNode(String xml, String newXml, String nodeName,
			String parentNodeName) {
		XmlProcessor xmlProcessor = new XmlProcessor(xml);
		String result = null;
		try {
			result = xmlProcessor.appendNode(newXml, nodeName, parentNodeName);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * Extract CQL library data object from share DTO.
	 *
	 * @param user the user
	 * @param dto the dto
	 * @return the CQL library data set object
	 */
	private CQLLibraryDataSetObject extractCQLLibraryDataObjectFromShareDTO(final User user, final CQLLibraryShareDTO dto) {
		
		boolean isOwner = user.getId().equals(dto.getOwnerUserId());
		boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(user.getSecurityRole().getDescription());
		
		CQLLibraryDataSetObject dataObject = new CQLLibraryDataSetObject();
		String formattedVersion = MeasureUtility.getVersionTextWithRevisionNumber(dto.getVersion(),
				dto.getRevisionNumber(), dto.isDraft());
		
		dataObject.setId(dto.getCqlLibraryId());
		dataObject.setCqlName(dto.getCqlLibraryName());
		dataObject.setVersion(formattedVersion);
		dataObject.setDraft(dto.isDraft());
		dataObject.setFinalizedDate(dto.getFinalizedDate());
		dataObject.setLocked(dto.isLocked());
		dataObject.setLockedUserInfo(dto.getLockedUserInfo());
		dataObject.setOwnerEmailAddress(user.getEmailAddress());
		dataObject.setOwnerFirstName(user.getFirstName());
		dataObject.setOwnerLastName(user.getLastName());
		dataObject.setSharable(isOwner || isSuperUser);
		dataObject.setCqlSetId(dto.getCqlLibrarySetId());
		dataObject.setEditable(MatContextServiceUtil.get().isCurrentCQLLibraryEditable(
				cqlLibraryDAO, dto.getCqlLibraryId()));
		return dataObject;
	}
	
	
	@Override
	public void updateUsersShare(final SaveCQLLibraryResult result) {
		StringBuffer auditLogAdditionlInfo = new StringBuffer("CQL Library shared with ");
		StringBuffer auditLogForModifyRemove = new StringBuffer("CQL Library shared status revoked with ");
		CQLLibraryShare cqlLibraryShare = null;
		boolean first = true;
		boolean firstRemove = true;
		boolean recordShareEvent = false;
		boolean recordRevokeShareEvent = false;
		List<CQLLibraryShareDTO> libraryShareDTO = result.getCqlLibraryShareDTOs();
		for (int i = 0; i < libraryShareDTO.size(); i++) {
			CQLLibraryShareDTO dto = libraryShareDTO.get(i);
			if ((dto.getShareLevel() != null) && !"".equals(dto.getShareLevel())) {
				User user = userDAO.find(dto.getUserId());
				ShareLevel sLevel = shareLevelDAO.find(dto.getShareLevel());
				cqlLibraryShare = null;
				for (CQLLibraryShare ms : user.getCqlLibraryShares()) {
					if (ms.getCqlLibrary().getId().equals(result.getId())) {
						cqlLibraryShare = ms;
						break;
					}
				}
				
				if ((cqlLibraryShare == null) && ShareLevel.MODIFY_ID.equals(dto.getShareLevel())) {
					recordShareEvent = true;
					cqlLibraryShare = new CQLLibraryShare();
					cqlLibraryShare.setCqlLibrary(cqlLibraryDAO.find(result.getId()));
					cqlLibraryShare.setShareUser(user);
					User currentUser = userDAO.find(LoggedInUserUtil.getLoggedInUser());
					cqlLibraryShare.setOwner(currentUser);
					user.getCqlLibraryShares().add(cqlLibraryShare);
					currentUser.getOwnedCQLLibraryShares().add(cqlLibraryShare);
					logger.info("Sharing " + cqlLibraryShare.getCqlLibrary().getId() + " with " + user.getId()
							+ " at level " + sLevel.getDescription());
					if (!first) { //first time, don't add the comma.
						auditLogAdditionlInfo.append(", ");
					}
					first = false;
					auditLogAdditionlInfo.append(user.getEmailAddress());
					
					cqlLibraryShare.setShareLevel(sLevel);
					cqlLibraryShareDAO.save(cqlLibraryShare);
				} else if (!ShareLevel.MODIFY_ID.equals(dto.getShareLevel())) {
					recordRevokeShareEvent = true;
					cqlLibraryShareDAO.delete(cqlLibraryShare.getId());
					logger.info("Removing Sharing " + cqlLibraryShare.getCqlLibrary().getId()
							+ " with " + user.getId()
							+ " at level " + sLevel.getDescription());
					System.out.println("Removing Sharing " + cqlLibraryShare.getCqlLibrary().getId()
							+ " with " + user.getId() + " at level " + sLevel.getDescription());
					if (!firstRemove) { //first time, don't add the comma.
						auditLogForModifyRemove.append(", ");
					}
					firstRemove = false;
					auditLogForModifyRemove.append(user.getEmailAddress());
				}
			}
		}
		
		//US 170. Log share event
		if (recordShareEvent || recordRevokeShareEvent) {
			if (recordShareEvent && recordRevokeShareEvent) {
				auditLogAdditionlInfo.append("\n").append(auditLogForModifyRemove);
			} else if (recordRevokeShareEvent) {
				auditLogAdditionlInfo = new StringBuffer(auditLogForModifyRemove);
			}
			cqlLibraryAuditLogDAO.recordCQLLibraryEvent(cqlLibraryShare.getCqlLibrary(),
					"CQL Library Shared", auditLogAdditionlInfo.toString());
		}
	}

}
