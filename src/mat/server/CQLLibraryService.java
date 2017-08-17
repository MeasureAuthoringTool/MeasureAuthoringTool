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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.clause.cqlworkspace.CQLWorkSpaceConstants;
import mat.client.measure.service.CQLService;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.client.shared.MatContext;
import mat.client.umls.service.VsacApiResult;
import mat.client.util.ClientConstants;
import mat.dao.CQLLibraryAuditLogDAO;
import mat.dao.RecentCQLActivityLogDAO;
import mat.dao.UserDAO;
import mat.dao.clause.CQLLibraryDAO;
import mat.dao.clause.CQLLibrarySetDAO;
import mat.dao.clause.CQLLibraryShareDAO;
import mat.dao.clause.MeasureDAO;
import mat.dao.clause.ShareLevelDAO;
import mat.model.CQLLibraryOwnerReportDTO;
import mat.model.CQLValueSetTransferObject;
import mat.model.LockedUserInfo;
import mat.model.MatCodeTransferObject;
import mat.model.RecentCQLActivityLog;
import mat.model.SecurityRole;
import mat.model.User;
import mat.model.clause.CQLLibrary;
import mat.model.clause.CQLLibrarySet;
import mat.model.clause.Measure;
import mat.model.clause.ShareLevel;
import mat.model.cql.CQLCodeSystem;
import mat.model.cql.CQLCodeWrapper;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLKeywords;
import mat.model.cql.CQLLibraryAssociation;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLLibraryShare;
import mat.model.cql.CQLLibraryShareDTO;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.server.model.MatUserDetails;
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
@SuppressWarnings("serial")
public class CQLLibraryService extends SpringRemoteServiceServlet implements CQLLibraryServiceInterface {
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(CQLLibraryService.class);
	
	/** The cql library DAO. */
	@Autowired
	private CQLLibraryDAO cqlLibraryDAO;
	
	/** The measure dao. */
	@Autowired
	private MeasureDAO measureDAO;
	
	/** The cql library set DAO. */
	@Autowired
	private CQLLibrarySetDAO cqlLibrarySetDAO;
	
	/** The user DAO. */
	@Autowired
	private UserDAO userDAO;

	/** The cql service. */
	@Autowired
	private CQLService cqlService;
	
	/** The share level DAO. */
	@Autowired
	private ShareLevelDAO shareLevelDAO;
	
	/** The cql library share DAO. */
	@Autowired
	private CQLLibraryShareDAO cqlLibraryShareDAO;
	
	/** The cql library audit log DAO. */
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
	
	/**
	 * Gets the vsac service.
	 * 
	 * @return the service
	 */
	private VSACApiServImpl getVsacService() {
		return (VSACApiServImpl) context.getBean("vsacapi");
	}

	/* (non-Javadoc)
	 * @see mat.server.service.CQLLibraryServiceInterface#searchForIncludes(java.lang.String)
	 */
	@Override
	public SaveCQLLibraryResult searchForIncludes(String setId, String searchText, boolean filter){
        SaveCQLLibraryResult saveCQLLibraryResult = new SaveCQLLibraryResult();
        List<CQLLibraryDataSetObject> allLibraries = new ArrayList<CQLLibraryDataSetObject>();
        List<CQLLibrary> list = cqlLibraryDAO.searchForIncludes(setId, searchText, filter);
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
	public SaveCQLLibraryResult search(String searchText, int filter, int startIndex,int pageSize) {
	//	List<CQLLibraryModel> cqlLibraries = new ArrayList<CQLLibraryModel>();
		SaveCQLLibraryResult searchModel = new SaveCQLLibraryResult();
		List<CQLLibraryDataSetObject> allLibraries = new ArrayList<CQLLibraryDataSetObject>();
		
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		List<CQLLibraryShareDTO> list = cqlLibraryDAO.search(searchText,Integer.MAX_VALUE, user,filter);
		
		searchModel.setResultsTotal(list.size());
		
		if (pageSize <= list.size()) {
			list = list
					.subList(startIndex - 1, pageSize);
		} else if (pageSize > list.size()) {
			list = list.subList(startIndex - 1,
					list.size());
		}
		
		for(CQLLibraryShareDTO dto : list){
			User userForShare = user;
			if(LoggedInUserUtil.getLoggedInUserRole().equalsIgnoreCase(ClientConstants.ADMINISTRATOR)){
				userForShare = userDAO.find(dto.getOwnerUserId());
			}
			CQLLibraryDataSetObject object = extractCQLLibraryDataObjectFromShareDTO(userForShare, dto  );
			allLibraries.add(object);
		}
	
		updateCQLLibraryFamily(allLibraries);
		searchModel.setCqlLibraryDataSetObjects(allLibraries);
		
		return searchModel;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.server.service.CQLLibraryServiceInterface#save(mat.model.clause.CQLLibrary)
	 */
	@Override
	public void save(CQLLibrary library) {
		library.setQdmVersion(MATPropertiesService.get().getQmdVersion());
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
		dataSetObject.setQdmVersion(cqlLibrary.getQdmVersion());
		if(cqlLibrary.getRevisionNumber() == null){
			dataSetObject.setRevisionNumber("000");
		} else {
			dataSetObject.setRevisionNumber(cqlLibrary.getRevisionNumber());
		}
		dataSetObject.setFinalizedDate(cqlLibrary.getFinalizedDate());
		dataSetObject.setMeasureId(cqlLibrary.getMeasureId());
		boolean isLocked =isLocked(cqlLibrary.getLockedOutDate());
		dataSetObject.setLocked(isLocked);
		if (isLocked && (cqlLibrary.getLockedUserId() != null)) {
			LockedUserInfo lockedUserInfo = new LockedUserInfo();
			lockedUserInfo.setUserId(cqlLibrary.getLockedUserId().getId());
			lockedUserInfo.setEmailAddress(cqlLibrary.getLockedUserId()
					.getEmailAddress());
			lockedUserInfo.setFirstName(cqlLibrary.getLockedUserId().getFirstName());
			lockedUserInfo.setLastName(cqlLibrary.getLockedUserId().getLastName());
			dataSetObject.setLockedUserInfo(lockedUserInfo);
		} else {
			LockedUserInfo lockedUserInfo = new LockedUserInfo();
			dataSetObject.setLockedUserInfo(lockedUserInfo);
		}
		
		
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
			newLibraryObject.setReleaseVersion(MATPropertiesService.get().getCurrentReleaseVersion());
			newLibraryObject.setQdmVersion(MATPropertiesService.get().getQmdVersion());
		// Update QDM Version to latest QDM Version.
			String versionLibraryXml = getCQLLibraryXml(existingLibrary);
			if(versionLibraryXml != null){
				XmlProcessor processor = new XmlProcessor(getCQLLibraryXml(existingLibrary));
				try {
					MeasureUtility.updateLatestQDMVersion(processor);
					versionLibraryXml = processor.transform(processor.getOriginalDoc());
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			newLibraryObject.setCQLByteArray(versionLibraryXml.getBytes());
			newLibraryObject.setVersion(existingLibrary.getVersion());
			newLibraryObject.setRevisionNumber("000");
			save(newLibraryObject);
			result.setSuccess(true);
			result.setId(newLibraryObject.getId());
			result.setCqlLibraryName(newLibraryObject.getName());
			String formattedVersion = MeasureUtility.getVersionTextWithRevisionNumber(existingLibrary.getVersion(), 
					newLibraryObject.getRevisionNumber(), existingLibrary.isDraft());
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
		
		boolean isVersionable = MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, libraryId);
		
		if(!isVersionable){
			result.setSuccess(false);
			result.setFailureReason(ConstantMessages.INVALID_DATA);
			return result;
		}
		
		SaveUpdateCQLResult cqlResult  = getCQLData(libraryId);
		if(cqlResult.getCqlErrors().size() >0 || !cqlResult.isDatatypeUsedCorrectly()){
			result.setSuccess(false);
			result.setFailureReason(ConstantMessages.INVALID_CQL_DATA);
			return result;
		}
		
		
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
        versionStr = MeasureUtility.formatVersionText(versionStr);
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
			//cqlResult.setExpIdentifier(cqlService.getDefaultExpansionIdentifier(str));
			cqlResult.setSetId(cqlLibrary.getSet_id());
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
				if ((existingLibrary.getLockedUserId() != null) && existingLibrary.getLockedUserId().getId().equalsIgnoreCase(userId)) {
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
		CQLLibrary cqlLib = null;
		User user = null;
		
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		if ((currentLibraryId != null) && (userId != null)) {
			cqlLib = cqlLibraryDAO.find(currentLibraryId);
			if (cqlLib != null) {
				if (!cqlLibraryDAO.isLibraryLocked(cqlLib.getId())) {
					user = getUserService().getById(userId);
					cqlLib.setLockedUserId(user);
					cqlLib.setLockedOutDate(new Timestamp(new Date().getTime()));
					cqlLibraryDAO.save(cqlLib);
					result.setSuccess(true);
				}
			}
		}
		
		result.setId(cqlLib.getId());
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
			CQLLibraryDataSetObject object = findCQLLibraryByID(activityLog.getCqlId());
			//CQLLibraryDataSetObject object = extractCQLLibraryDataObject(library);
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
	 * @param isFormatable flag to determine if the parameter should be formatted on save
	 * @return the save update CQL result
	 */
	@Override
	public SaveUpdateCQLResult saveAndModifyParameters(String libraryId, CQLParameter toBeModifiedObj,
			CQLParameter currentObj, List<CQLParameter> parameterList, boolean isFormatable) {

		SaveUpdateCQLResult result = null;
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, libraryId)) {
			CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
			String cqlXml = getCQLLibraryXml(cqlLibrary);

			if (cqlXml != null) {
				result = cqlService.saveAndModifyParameters(cqlXml, toBeModifiedObj, currentObj, parameterList, isFormatable);
				if (result != null && result.isSuccess()) {
					cqlLibrary.setCQLByteArray(result.getXml().getBytes());
					cqlLibraryDAO.save(cqlLibrary);
				}
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
	 * @param isFormatable flag to determine if the definition should be formatted on save
	 * @return the save update CQL result
	 */
	@Override
	public SaveUpdateCQLResult saveAndModifyDefinitions(String libraryId, CQLDefinition toBeModifiedObj,
			CQLDefinition currentObj, List<CQLDefinition> definitionList, boolean isFormatable) {

		SaveUpdateCQLResult result = null;
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, libraryId)) {
			CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
			String cqlXml = getCQLLibraryXml(cqlLibrary);

			if (cqlXml != null) {

				result = cqlService.saveAndModifyDefinitions(cqlXml, toBeModifiedObj, currentObj, definitionList, isFormatable);
				if (result != null && result.isSuccess()) {
					cqlLibrary.setCQLByteArray(result.getXml().getBytes());
					cqlLibraryDAO.save(cqlLibrary);
				}
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
	 * @param isFormatable flag to determine if the function should be formatted on save
	 * @return the save update CQL result
	 */
	@Override
	public SaveUpdateCQLResult saveAndModifyFunctions(String libraryId, CQLFunctions toBeModifiedObj,
			CQLFunctions currentObj, List<CQLFunctions> functionsList, boolean isFormatable) {
		
		SaveUpdateCQLResult result = null;
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, libraryId)) {
			CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
			String cqlXml = getCQLLibraryXml(cqlLibrary);

			if (cqlXml != null) {
				result = cqlService.saveAndModifyFunctions(cqlXml, toBeModifiedObj, currentObj, functionsList, isFormatable);
				if (result != null && result.isSuccess()) {
					cqlLibrary.setCQLByteArray(result.getXml().getBytes());
					cqlLibraryDAO.save(cqlLibrary);
				}
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
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, libraryId)) {
			CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
			if (cqlLibrary != null) {
				int associationCount = cqlService.countNumberOfAssociation(libraryId);
				// System.out.println("============== "+associationCount +
				// "=============");
				if (associationCount < CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
					String cqlXml = getCQLLibraryXml(cqlLibrary);
					if (cqlXml != null) {
						result = cqlService.saveIncludeLibrayInCQLLookUp(cqlXml, toBeModifiedObj, currentObj,
								incLibraryList);
						if (result != null && result.isSuccess()) {
							cqlLibrary.setCQLByteArray(result.getXml().getBytes());
							cqlLibraryDAO.save(cqlLibrary);
							cqlService.saveCQLAssociation(currentObj, libraryId);
						}
					}
				}

			}
		}

		return result;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.CQLLibraryServiceInterface#countNumberOfAssociation(java.lang.String)
	 */
	@Override
	public int countNumberOfAssociation(String Id){
		return cqlService.countNumberOfAssociation(Id);
	}
	
	@Override
	public List<CQLLibraryAssociation> getAssociations(String Id){
		return cqlService.getAssociations(Id);
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
	@Override
	public SaveUpdateCQLResult deleteDefinition(String libraryId, CQLDefinition toBeDeletedObj,
			List<CQLDefinition> definitionList) {
		SaveUpdateCQLResult result = null;
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, libraryId)) {

			CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
			String cqlXml = getCQLLibraryXml(cqlLibrary);

			if (cqlXml != null) {
				result = cqlService.deleteDefinition(cqlXml, toBeDeletedObj, definitionList);
				if (result != null && result.isSuccess()) {
					cqlLibrary.setCQLByteArray(result.getXml().getBytes());
					cqlLibraryDAO.save(cqlLibrary);
				}
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
	@Override
	public SaveUpdateCQLResult deleteFunctions(String libraryId, CQLFunctions toBeDeletedObj, 
			List<CQLFunctions> functionsList) {
		
		SaveUpdateCQLResult result = null;
		
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, libraryId)) {
			CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
			String cqlXml = getCQLLibraryXml(cqlLibrary);

			if (cqlXml != null) {
				result = cqlService.deleteFunctions(cqlXml, toBeDeletedObj, functionsList);
				if (result != null && result.isSuccess()) {
					cqlLibrary.setCQLByteArray(result.getXml().getBytes());
					cqlLibraryDAO.save(cqlLibrary);
				}
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
	@Override
	public SaveUpdateCQLResult deleteParameter(String libraryId, CQLParameter toBeDeletedObj, 
			List<CQLParameter> parameterList) {
		SaveUpdateCQLResult result = null;
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, libraryId)) {
			CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
			String cqlXml = getCQLLibraryXml(cqlLibrary);

			if (cqlXml != null) {
				result = cqlService.deleteParameter(cqlXml, toBeDeletedObj, parameterList);
				if (result != null && result.isSuccess()) {
					cqlLibrary.setCQLByteArray(result.getXml().getBytes());
					cqlLibraryDAO.save(cqlLibrary);
				}
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
			List<CQLIncludeLibrary> viewIncludeLibrarys) {
    	SaveUpdateCQLResult result = null;
		CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
		String cqlXml = getCQLLibraryXml(cqlLibrary);
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, libraryId)) {
			if (cqlXml != null) {
				result = cqlService.deleteInclude(cqlXml, toBeModifiedIncludeObj, viewIncludeLibrarys);
				if (result != null && result.isSuccess()) {
					cqlLibrary.setCQLByteArray(result.getXml().getBytes());
					cqlLibraryDAO.save(cqlLibrary);

					cqlService.deleteCQLAssociation(toBeModifiedIncludeObj, cqlLibrary.getId());
				}
			}
		}
		return result;
	}
	
	/**
	 * Gets the CQL keywords lists.
	 *
	 * @return the CQL keywords lists
	 */
    @Override
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
	
	@Override
	public SaveUpdateCQLResult getCQLLibraryFileData(String libraryId) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
		String cqlXml = getCQLLibraryXml(cqlLibrary);
		
		if (cqlXml != null && !StringUtils.isEmpty(cqlXml)) {
			result = cqlService.getCQLLibraryData(cqlXml);
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
	@Override
	public SaveUpdateCQLResult saveCQLValueset(CQLValueSetTransferObject valueSetTransferObject) {
		
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, valueSetTransferObject.getCqlLibraryId())) {
			CQLLibrary library = cqlLibraryDAO.find(valueSetTransferObject.getCqlLibraryId());
			if (library != null) {
				result = cqlService.saveCQLValueset(valueSetTransferObject);
				if (result != null && result.isSuccess()) {
					String nodeName = "valueset";
					String parentNode = "//cqlLookUp/valuesets";
					appendAndSaveNode(library, nodeName, result.getXml(), parentNode);
				}
			}
		}
		return result;
	}
	
	@Override
	public SaveUpdateCQLResult saveCQLCodestoCQLLibrary(MatCodeTransferObject transferObject) {
		
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, transferObject.getId())) {
			
			CQLLibrary cqlLibrary = cqlLibraryDAO.find(transferObject.getId());
			if (cqlLibrary != null) {
				String cqlXml = getCQLLibraryXml(cqlLibrary);
				if(!cqlXml.isEmpty()){
					result = cqlService.saveCQLCodes(cqlXml,transferObject);
					if(result != null && result.isSuccess()) {
						String nodeName = "code";
						String parentNode = "//cqlLookUp/codes";
						String newXml= appendAndSaveNode(cqlLibrary, nodeName, result.getXml(), parentNode);
						cqlLibraryDAO.refresh(cqlLibrary);
						System.out.println("newXml ::: " + newXml);
						
						
						CQLCodeSystem codeSystem = new CQLCodeSystem();
						codeSystem.setCodeSystem(transferObject.getCqlCode().getCodeSystemOID());
						codeSystem.setCodeSystemName(transferObject.getCqlCode().getCodeSystemName());
						codeSystem.setCodeSystemVersion(transferObject.getCqlCode().getCodeSystemVersion());
						SaveUpdateCQLResult updatedResult = cqlService.saveCQLCodeSystem(newXml, codeSystem);
						if(updatedResult.isSuccess()) {
							newXml = saveCQLCodeSystemInLibrary(cqlLibrary, updatedResult);
							System.out.println("Updated newXml ::: " + newXml);
						}
						result.setCqlCodeList(getSortedCQLCodes(newXml).getCqlCodeList());
					}
				}
			}
		}
		return result;
	}

	/**
	 * @param cqlLibrary
	 * @param updatedResult
	 */
	private String saveCQLCodeSystemInLibrary(CQLLibrary cqlLibrary, SaveUpdateCQLResult updatedResult) {
		String nodeName = "codeSystem";
		String parentNode = "//cqlLookUp/codeSystems";
		 return appendAndSaveNode(cqlLibrary, nodeName, updatedResult.getXml(), parentNode);
	}
	
	
	private CQLCodeWrapper getSortedCQLCodes(String newXml) {
		CQLCodeWrapper cqlCodeWrapper = new CQLCodeWrapper();
		if(newXml != null && !newXml.isEmpty()){
			cqlCodeWrapper = cqlService.getCQLCodes(newXml);
		}
		
		return cqlCodeWrapper;
	}
	
	/**
	 * Save CQL user defined valuesetto measure.
	 *
	 * @param matValueSetTransferObject the mat value set transfer object
	 * @return the save update CQL result
	 */
	@Override
	public SaveUpdateCQLResult saveCQLUserDefinedValueset(CQLValueSetTransferObject matValueSetTransferObject) {
		
		SaveUpdateCQLResult result = null;
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO,
				matValueSetTransferObject.getCqlLibraryId())) {
			CQLLibrary library = cqlLibraryDAO.find(matValueSetTransferObject.getCqlLibraryId());
			if (library != null) {
				result = cqlService.saveCQLUserDefinedValueset(matValueSetTransferObject);
				if (result != null && result.isSuccess()) {
					String nodeName = "valueset";
					String parentNode = "//cqlLookUp/valuesets";
					appendAndSaveNode(library, nodeName, result.getXml(), parentNode);
				}
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
	@Override
	public SaveUpdateCQLResult modifyCQLValueSets(CQLValueSetTransferObject matValueSetTransferObject) {
		
		SaveUpdateCQLResult result = null;
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO,
				matValueSetTransferObject.getCqlLibraryId())) {
			CQLLibrary library = cqlLibraryDAO.find(matValueSetTransferObject.getCqlLibraryId());
			if (library != null) {
				result = cqlService.modifyCQLValueSets(matValueSetTransferObject);
				if (result != null && result.isSuccess()) {
					result = cqlService.updateCQLLookUpTag(getCQLLibraryXml(library), result.getCqlQualityDataSetDTO(),
							matValueSetTransferObject.getCqlQualityDataSetDTO());
					if (result != null && result.isSuccess()) {
						library.setCQLByteArray(result.getXml().getBytes());
						save(library);
					}
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
	@Override
	public SaveUpdateCQLResult deleteValueSet(String toBeDelValueSetId, String libraryId) {
		
		SaveUpdateCQLResult cqlResult = null;
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, libraryId)) {
			CQLLibrary library = cqlLibraryDAO.find(libraryId);
			if (library != null) {
				cqlResult = cqlService.deleteValueSet(getCQLLibraryXml(library), toBeDelValueSetId);
				if (cqlResult != null && cqlResult.isSuccess()) {
					library.setCQLByteArray(cqlResult.getXml().getBytes());
					save(library);
				}
			}
		}
		return cqlResult;
	}
	
	
	@Override
	public SaveUpdateCQLResult deleteCode(String toBeDeletedId, String libraryId) {
		
		SaveUpdateCQLResult cqlResult = new SaveUpdateCQLResult();
		if (MatContextServiceUtil.get().isCurrentCQLLibraryEditable(cqlLibraryDAO, libraryId)) {
			CQLLibrary library = cqlLibraryDAO.find(libraryId);
			if (library != null) {
				cqlResult = cqlService.deleteCode(getCQLLibraryXml(library), toBeDeletedId);
				if (cqlResult != null && cqlResult.isSuccess()) {
					library.setCQLByteArray(cqlResult.getXml().getBytes());
					save(library);
					cqlResult.setCqlCodeList(getSortedCQLCodes(cqlResult.getXml()).getCqlCodeList());
					
				}
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
	public final String appendAndSaveNode(CQLLibrary library, final String nodeName, String newXml, String parentNode) {
		String result = new String();
		if ((library != null && !StringUtils.isEmpty(getCQLLibraryXml(library)))
				&& (nodeName != null && StringUtils.isNotBlank(nodeName))) {
			 result = callAppendNode(getCQLLibraryXml(library), newXml, nodeName,
					parentNode);
			library.setCQLByteArray(result.getBytes());
			save(library);
		}
		return result;

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
		dataObject.setDeletable(isOwner && dto.isDraft());
		dataObject.setCqlSetId(dto.getCqlLibrarySetId());
		dataObject.setEditable(MatContextServiceUtil.get().isCurrentCQLLibraryEditable(
				cqlLibraryDAO, dto.getCqlLibraryId()));
		dataObject.setDraftable(dto.isDraftable());
		dataObject.setVersionable(dto.isVersionable());
		return dataObject;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.server.service.CQLLibraryServiceInterface#updateUsersShare(mat.client.measure.service.SaveCQLLibraryResult)
	 */
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
					auditLogAdditionlInfo.append(user.getFirstName() +  " " + user.getLastName());
					
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
					auditLogForModifyRemove.append(user.getFirstName() +  " " + user.getLastName());
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


	//@Override
	/*public void updateCQLLibraryXMLForExpansionProfile(List<CQLQualityDataSetDTO> modifyWithDTO, String libraryId,
			String expansionProfile) {
		logger.debug(" CQLLibraryService: updateLibraryXMLForExpansionIdentifier Start : Library Id :: "
				+ libraryId);
		CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
		
		if (cqlLibrary != null) {
			String cqlXml = getCQLLibraryXml(cqlLibrary);
			XmlProcessor processor = new XmlProcessor(cqlXml);
			String XPATH_EXP_FOR_ELEMENTLOOKUP_ATTR = "/cqlLookUp/valuesets";
			try {
				Node nodesElementLookUp = (Node) xPath.evaluate(XPATH_EXP_FOR_ELEMENTLOOKUP_ATTR,
						processor.getOriginalDoc(), XPathConstants.NODE);
				if (nodesElementLookUp != null) {
					if (nodesElementLookUp.getAttributes().getNamedItem("vsacExpIdentifier") != null) {
						if (!StringUtils.isBlank(expansionProfile)) {
							nodesElementLookUp.getAttributes().getNamedItem("vsacExpIdentifier")
									.setNodeValue(expansionProfile);
						} else {
							nodesElementLookUp.getAttributes().removeNamedItem("vsacExpIdentifier");
						}
					} else {
						if (!StringUtils.isEmpty(expansionProfile)) {
							Attr vsacExpIdentifierAttr = processor.getOriginalDoc()
									.createAttribute("vsacExpIdentifier");
							vsacExpIdentifierAttr.setNodeValue(expansionProfile);
							nodesElementLookUp.getAttributes().setNamedItem(vsacExpIdentifierAttr);
						}
					}
				}
				for (CQLQualityDataSetDTO dto : modifyWithDTO) {
					updateCQLLibraryXmlForQDM(dto, processor, expansionProfile);
				}
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}

			cqlLibrary.setCQLByteArray(processor.transform(processor.getOriginalDoc()).getBytes());
			cqlLibraryDAO.save(cqlLibrary);
		}
	}
*/

	/*private void updateCQLLibraryXmlForQDM(CQLQualityDataSetDTO dto, XmlProcessor processor, String expansionProfile) {
			String XPATH_EXPRESSION_ELEMENTLOOKUP = "/cqlLookUp/valuesets/valueset[@uuid='"
					+ dto.getUuid() + "']";
			NodeList nodesElementLookUp;
			try {
				nodesElementLookUp = (NodeList) xPath.evaluate(XPATH_EXPRESSION_ELEMENTLOOKUP,
						processor.getOriginalDoc(), XPathConstants.NODESET);

				for (int i = 0; i < nodesElementLookUp.getLength(); i++) {
					Node newNode = nodesElementLookUp.item(i);
					newNode.getAttributes().getNamedItem("version").setNodeValue("1.0");
					if (newNode.getAttributes().getNamedItem("expansionIdentifier") != null) {
						if (!StringUtils.isBlank(dto.getExpansionIdentifier())) {
							newNode.getAttributes().getNamedItem("expansionIdentifier").setNodeValue(expansionProfile);
						} else {
							newNode.getAttributes().removeNamedItem("expansionIdentifier");
						}
					} else {
						if (!StringUtils.isEmpty(expansionProfile)) {
							Attr expansionIdentifierAttr = processor.getOriginalDoc()
									.createAttribute("expansionIdentifier");
							expansionIdentifierAttr.setNodeValue(expansionProfile);
							newNode.getAttributes().setNamedItem(expansionIdentifierAttr);
						}
					}
				}
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		}*/


	public void updateCQLLookUpTagWithModifiedValueSet(CQLQualityDataSetDTO modifyWithDTO, CQLQualityDataSetDTO modifyDTO,
			String libraryId) {
		CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
		if (cqlLibrary != null) {
			cqlService.updateCQLLookUpTag(getCQLLibraryXml(cqlLibrary), modifyWithDTO, modifyDTO);
		}
		
	}
		
	@Override
	public VsacApiResult updateCQLVSACValueSets(String cqlLibraryId, String expansionId, String sessionId) {
		List<CQLQualityDataSetDTO> appliedQDMList = getCQLData(cqlLibraryId).getCqlModel().getAllValueSetList();
		VsacApiResult result = getVsacService().updateCQLVSACValueSets(appliedQDMList, expansionId, sessionId);
		if(result.isSuccess()){
			updateAllCQLInLibraryXml(result.getCqlQualityDataSetMap(), cqlLibraryId);
		}
		return result;
	}

	/** Method to Iterate through Map of Quality Data set DTO(modify With) as key and Quality Data Set DTO (modifiable) as Value and update
	 * @param map - HaspMap
	 * @param libraryId - String */
	private void updateAllCQLInLibraryXml(HashMap<CQLQualityDataSetDTO, CQLQualityDataSetDTO> map, String libraryId) {
		logger.info("Start VSACAPIServiceImpl updateAllInLibraryXml :");
		Iterator<Entry<CQLQualityDataSetDTO, CQLQualityDataSetDTO>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<CQLQualityDataSetDTO, CQLQualityDataSetDTO> entrySet = it.next();
			logger.info("Calling updateLibraryXML for : " + entrySet.getKey().getOid());
			updateCQLLookUpTagWithModifiedValueSet(entrySet.getKey(),
					entrySet.getValue(), libraryId);
			logger.info("Successfully updated Library XML for  : " + entrySet.getKey().getOid());
		}
		logger.info("End VSACAPIServiceImpl updateAllInLibraryXml :");
	}
	
	
	@Override
	public void transferLibraryOwnerShipToUser(final List<String> list, final String toEmail) {
		User userTo = userDAO.findByEmail(toEmail);
		
		for (int i = 0; i < list.size(); i++) {
			CQLLibrary library = cqlLibraryDAO.find(list.get(i));
			List<CQLLibrary> libraries = new ArrayList <CQLLibrary>();
			libraries.add(library);
			//Get All Family Libraries for each CQL Library
			List<CQLLibrary> allLibrariesInFamily = cqlLibraryDAO.getAllLibrariesInSet(libraries);
			for (int j = 0; j < allLibrariesInFamily.size(); j++) {
				String additionalInfo = "CQL Library Owner transferred from "
						+ allLibrariesInFamily.get(j).getOwnerId().getEmailAddress() + " to " + toEmail;
				allLibrariesInFamily.get(j).setOwnerId(userTo);
				this.save(allLibrariesInFamily.get(j));
				cqlLibraryAuditLogDAO.recordCQLLibraryEvent(allLibrariesInFamily.get(j), "CQL Library Ownership Changed", additionalInfo);
				additionalInfo = "";
				
			}
			List<CQLLibraryShare> cqlLibShareInfo = cqlLibraryDAO.getLibraryShareInforForLibrary(list.get(i));
			for (int k = 0; k < cqlLibShareInfo.size(); k++) {
				cqlLibShareInfo.get(k).setOwner(userTo);
				cqlLibraryShareDAO.save(cqlLibShareInfo.get(k));
			}
			
		}
		
	}
	
	@Override
    public List<CQLLibraryOwnerReportDTO> getCQLLibrariesForOwner() {
        Map<User, List<CQLLibrary>> map = new HashMap<>();
        List<User> nonAdminUserList = getUserService().getAllNonAdminActiveUsers();
        for(User user : nonAdminUserList) {
            List<CQLLibrary> libraryList = cqlLibraryDAO.getLibraryListForLibraryOwner(user);
            if((libraryList != null && libraryList.size() > 0)) {
                map.put(user,  libraryList);
            }
        }
 
        List<CQLLibraryOwnerReportDTO> cqlLibraryOwnerReports = populateCQLLibraryOwnerReport(map); 
        return cqlLibraryOwnerReports;
 
    }
	
	 /**
     * Populates the cql library ownership dto list
     * @param map the map of users and cql libraries
     * @return the cql library ownership report list
     */
 
    private List<CQLLibraryOwnerReportDTO> populateCQLLibraryOwnerReport(Map<User, List<CQLLibrary>> map) {
        List<CQLLibraryOwnerReportDTO> cqlLibraryOwnerReports = new ArrayList<CQLLibraryOwnerReportDTO>();
        for(Entry<User, List<CQLLibrary>> entry : map.entrySet()) {
            User user = entry.getKey();
            List<CQLLibrary> libraries = entry.getValue();
            for(CQLLibrary cqlLibrary : libraries) {
                String cqlLibraryName = cqlLibrary.getName();
                String type = ""; 
                if(cqlLibrary.getMeasureId() == null) {
                    type = "Stand Alone"; 
                } else {
                    type = "Measure"; 
                }
                String status = ""; 
                if(cqlLibrary.isDraft()) {
                    status = "Draft";
                } else {
                    status = "Versioned"; 
                }
                String versionNumber = "v" + cqlLibrary.getVersionNumber(); 
                String id = cqlLibrary.getId();
                String setId = cqlLibrary.getSet_id();
                String firstName = user.getFirstName();
                String lastName = user.getLastName(); 
                String organization = user.getOrganization().getOrganizationName();
                CQLLibraryOwnerReportDTO cqlLibraryOwnerReportDTO = new CQLLibraryOwnerReportDTO(cqlLibraryName, type, status, versionNumber, id, setId, firstName, lastName, organization);
                cqlLibraryOwnerReports.add(cqlLibraryOwnerReportDTO);
            }
        }
        return cqlLibraryOwnerReports; 
 
    }
    
    @Override
    public final void deleteCQLLibrary(final String cqllibId, String loginUserId) {
		logger.info("CQLLibraryService: DeleteCQLLibrary start : cqlLibId:: " + cqllibId);
		CQLLibrary cqlLib = cqlLibraryDAO.find(cqllibId);
		SecurityContext sc = SecurityContextHolder.getContext();
		MatUserDetails details = (MatUserDetails) sc.getAuthentication().getDetails();
		if (cqlLib.getOwnerId().getId().equalsIgnoreCase(details.getId())) {
			logger.info("CQL Library Deletion Started for cql library Id :: " + cqllibId);
			try {
				cqlLibraryDAO.delete(cqlLib);
				logger.info("CQL Library Deleted Successfully :: " + cqllibId);
			} catch (Exception e) {
				logger.info("CQL Libraray not deleted.Something went wrong for cql Library Id :: " + cqllibId);
			}
		}

		logger.info("CQLLibraryService: DeleteCQLLibrary End : cqlLibraryId:: " + cqllibId);
	}

	/*@Override
	public SaveCQLLibraryResult searchForStandaloneIncludes(String setId, String searchText) {
		SaveCQLLibraryResult saveCQLLibraryResult = new SaveCQLLibraryResult();
        List<CQLLibraryDataSetObject> allLibraries = new ArrayList<CQLLibraryDataSetObject>();
        List<CQLLibrary> list = cqlLibraryDAO.searchForStandaloneIncludes(setId, searchText);
        saveCQLLibraryResult.setResultsTotal(list.size());
        for(CQLLibrary cqlLibrary : list){
               CQLLibraryDataSetObject object = extractCQLLibraryDataObject(cqlLibrary);
               allLibraries.add(object);
        }
        saveCQLLibraryResult.setCqlLibraryDataSetObjects(allLibraries);
        return saveCQLLibraryResult;
	}*/

}
