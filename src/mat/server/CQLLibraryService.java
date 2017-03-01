package mat.server;

import java.io.File;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import mat.client.measure.service.CQLService;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.client.shared.MatContext;
import mat.dao.RecentCQLActivityLogDAO;
import mat.dao.UserDAO;
import mat.dao.clause.CQLLibraryDAO;
import mat.dao.clause.CQLLibrarySetDAO;
import mat.model.LockedUserInfo;
import mat.model.RecentCQLActivityLog;
import mat.model.SecurityRole;
import mat.model.User;
import mat.model.clause.CQLLibrary;
import mat.model.clause.CQLLibrarySet;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLKeywords;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.server.service.CQLLibraryServiceInterface;
import mat.server.service.UserService;
import mat.server.util.MATPropertiesService;
import mat.server.util.MeasureUtility;
import mat.server.util.ResourceLoader;
import mat.server.util.XmlProcessor;
import mat.shared.CQLModelValidator;
import mat.shared.ConstantMessages;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.SaveUpdateCQLResult;

public class CQLLibraryService implements CQLLibraryServiceInterface {
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(CQLLibraryService.class);
	@Autowired
	private CQLLibraryDAO cqlLibraryDAO;
	@Autowired
	private CQLLibrarySetDAO cqlLibrarySetDAO;
	@Autowired
	private UserDAO userDAO;

	@Autowired
	private CQLService cqlService;
	@Autowired
	private ApplicationContext context;

	@Autowired
	private RecentCQLActivityLogDAO recentCQLActivityLogDAO;
	
	javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
	
	private final long lockThreshold = 3 * 60 * 1000; // 3 minutes

	@Override
	public SaveCQLLibraryResult search(String searchText, String searchFrom, int filter,int startIndex, int pageSize) {
	//	List<CQLLibraryModel> cqlLibraries = new ArrayList<CQLLibraryModel>();
		SaveCQLLibraryResult searchModel = new SaveCQLLibraryResult();
		List<CQLLibraryDataSetObject> allLibraries = new ArrayList<CQLLibraryDataSetObject>();
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		List<CQLLibrary> list = cqlLibraryDAO.search(searchText,searchFrom, Integer.MAX_VALUE,user,filter);
		
		searchModel.setResultsTotal(list.size());
		
		if (pageSize <= list.size()) {
			list = list
					.subList(startIndex - 1, pageSize);
		} else if (pageSize > list.size()) {
			list = list.subList(startIndex - 1,
					list.size());
		}
		
		for(CQLLibrary cqlLibrary : list){
			CQLLibraryDataSetObject object = extractCQLLibraryDataObject(cqlLibrary);
			allLibraries.add(object);
		}
		searchModel.setCqlLibraryDataSetObjects(allLibraries);
		
		return searchModel;
	}
	
	@Override
	public SaveCQLLibraryResult searchForVersion(String searchText){
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		
		ArrayList<CQLLibraryDataSetObject> cqList = new ArrayList<CQLLibraryDataSetObject>();
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		// To reuse existing search, filter is passed as -1 which otherwise has value 0 or 1
		List<CQLLibrary> list = cqlLibraryDAO.search(searchText,"StandAlone", Integer.MAX_VALUE,user,-1);
		for(CQLLibrary library : list){
			CQLLibraryDataSetObject cqlLibraryDataSetObject = extractCQLLibraryDataObject(library);
			
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
							if(library.getOwnerId().getId()
									.equalsIgnoreCase(
											user.getId())){
								canVersion = true;
							} else {
								canVersion = false;
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
	
	@Override
	public SaveCQLLibraryResult searchForDraft(String searchText) {
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();

		ArrayList<CQLLibraryDataSetObject> cqList = new ArrayList<CQLLibraryDataSetObject>();
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		// To reuse existing search, filter is passed as -1 which otherwise has value 0 or 1
		List<CQLLibrary> list = cqlLibraryDAO.search(searchText, "StandAlone", Integer.MAX_VALUE, user, -1);
		HashSet<String> hasDraft = new HashSet<String>();
		for (CQLLibrary library : list) {
			if (library.isDraft()) {
				String setId = library.getCqlSet().getId();
				hasDraft.add(setId);
			}
		}
		
		for (CQLLibrary library : list) {
			CQLLibraryDataSetObject cqlLibraryDataSetObject = extractCQLLibraryDataObject(library);

			if (cqlLibraryDataSetObject != null) {
				boolean canDraft = false;
				if (cqlLibraryDataSetObject.isDraft()) {
					canDraft = false;
				} else {
						if(hasDraft.contains(library.getCqlSet().getId())){
							canDraft = false;
						} else {
							if (cqlLibraryDataSetObject.isLocked()) {
								canDraft = false;
							} else {
								if (user.getSecurityRole().getDescription()
									.equalsIgnoreCase(SecurityRole.SUPER_USER_ROLE)) {
									canDraft = true;
								} else {
									if (library.getOwnerId().getId().equalsIgnoreCase(user.getId())) {
										canDraft = true;
									} else {
										canDraft = false;
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
	
	
	
	@Override
	public void save(CQLLibrary library) {
		this.cqlLibraryDAO.save(library);
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
		if(cqlLibrary.getCqlSet()!=null){
			dataSetObject.setCqlSetId(cqlLibrary.getCqlSet().getId());	
		}
		
		String formattedVersion = MeasureUtility.getVersionTextWithRevisionNumber(cqlLibrary.getVersion(), 
				cqlLibrary.getRevisionNumber(), cqlLibrary.isDraft());
		dataSetObject.setVersion(formattedVersion);        
		
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
		CQLLibrary cqlLibrary = cqlLibraryDAO.find(cqlLibraryId);
		CQLLibraryDataSetObject cqlLibraryDataSetObject = extractCQLLibraryDataObject(cqlLibrary);
		cqlLibraryDataSetObject.setCqlText(getCQLLibraryData(cqlLibrary));
		return cqlLibraryDataSetObject; 
	}
	@Override
	public SaveCQLLibraryResult saveDraftFromVersion(String libraryId){
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		CQLLibrary existingLibrary = cqlLibraryDAO.find(libraryId);
		if(existingLibrary != null){
			CQLLibrary newLibraryObject = new CQLLibrary();
			newLibraryObject.setDraft(true);
			newLibraryObject.setName(existingLibrary.getName());
			newLibraryObject.setCqlSet(existingLibrary.getCqlSet());
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
			result.setVersionStr(newLibraryObject.getVersion()+"."+newLibraryObject.getRevisionNumber());
			
		} else {
			result.setSuccess(false);
			result.setFailureReason(SaveCQLLibraryResult.INVALID_DATA);
		}
		return result;
	}
	
	
	@Override
	public SaveCQLLibraryResult saveFinalizedVersion (String libraryId,  boolean isMajor,
			 String version){
		logger.info("Inside saveFinalizedVersion: Start");
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		
		CQLLibrary library = cqlLibraryDAO.find(libraryId);
		if(library != null){
			String versionNumber = null;
			if (isMajor) {
				versionNumber = cqlLibraryDAO.findMaxVersion(library.getCqlSet().getId());
				if (versionNumber == null) {
					versionNumber = "0.000";
				}
				logger.info("Max Version Number loaded from DB: " + versionNumber);
			} else {
				int versionIndex = version.indexOf('v');
				logger.info("Min Version number passed from Page Model: " + versionIndex);
				String selectedVersion = version.substring(versionIndex + 1);
				logger.info("Min Version number after trim: " + selectedVersion);
				versionNumber = cqlLibraryDAO.findMaxOfMinVersion(library.getCqlSet().getId(), selectedVersion);

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

			library.setCqlSet(cqlLibrarySet);
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
	@Override
	public String createCQLLookUpTag(String libraryName, String version){
		XmlProcessor xmlProcessor = loadCQLXmlTemplateFile();
		String cqlLookUpString = getCQLLookUpXml(libraryName, version,xmlProcessor,"//standAlone");
		return cqlLookUpString;
	}
	
	
	
	
	/**
	 * @param cqlLibraryDataSetObject
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
	@Override
	public void isLibraryAvailableAndLogRecentActivity(String libraryid, String userId){
		CQLLibrary library = cqlLibraryDAO.find(libraryid);
		if(library != null){
			recentCQLActivityLogDAO.recordRecentCQLLibraryActivity(libraryid, userId);
		}
	}
	
	
	
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
	
	public SaveUpdateCQLResult saveAndModifyCQLGeneralInfo(String libraryId, String context) {
		 
		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			return null;
		}
		CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
		String cqlXml = getCQLLibraryXml(cqlLibrary);
		SaveUpdateCQLResult result = null;
		if (cqlXml != null) {
			result = cqlService.saveAndModifyCQLGeneralInfo(cqlXml, context);
			if (result != null && result.isSuccess()) {
				cqlLibrary.setCQLByteArray(result.getXml().getBytes());
				cqlLibraryDAO.save(cqlLibrary);
			}
		}
		return result;
	}
	
	
	public SaveUpdateCQLResult saveIncludeLibrayInCQLLookUp(String libraryId, CQLIncludeLibrary toBeModifiedObj,
			CQLIncludeLibrary currentObj, List<CQLIncludeLibrary> incLibraryList) {

		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			return null;
		}
		CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
		String cqlXml = getCQLLibraryXml(cqlLibrary);
		SaveUpdateCQLResult result = null;
		if (cqlXml != null) {
			result = cqlService.saveIncludeLibrayInCQLLookUp(cqlXml,
					toBeModifiedObj, currentObj, incLibraryList);
			if (result != null && result.isSuccess()) {
				cqlLibrary.setCQLByteArray(result.getXml().getBytes());
				cqlLibraryDAO.save(cqlLibrary);
				cqlService.saveCQLAssociation(currentObj, libraryId);
			}
		}
		return result;
	}
	
	
	public SaveUpdateCQLResult deleteDefinition(String libraryId, CQLDefinition toBeDeletedObj,
			CQLDefinition currentObj, List<CQLDefinition> definitionList) {
		
		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			return null;
		}
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
	
	
	public SaveUpdateCQLResult deleteFunctions(String libraryId, CQLFunctions toBeDeletedObj, CQLFunctions currentObj,
			List<CQLFunctions> functionsList) {
		
		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			return null;
		}
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
	
	
	public SaveUpdateCQLResult deleteParameter(String libraryId, CQLParameter toBeDeletedObj, CQLParameter currentObj,
			List<CQLParameter> parameterList) {
		
		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			return null;
		}
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
	
	
	public SaveUpdateCQLResult deleteInclude(String libraryId, CQLIncludeLibrary toBeModifiedIncludeObj,
			CQLIncludeLibrary cqlLibObject, List<CQLIncludeLibrary> viewIncludeLibrarys) {

		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			return null;
		}
		CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
		String cqlXml = getCQLLibraryXml(cqlLibrary);
		SaveUpdateCQLResult result = null;
		if (cqlXml != null) {
			result = cqlService.deleteInclude(cqlXml, toBeModifiedIncludeObj, cqlLibObject,
					viewIncludeLibrarys);
			if (result != null && result.isSuccess()) {
				cqlLibrary.setCQLByteArray(result.getXml().getBytes());
				cqlLibraryDAO.save(cqlLibrary);
				//deleteFromAssociationTable.
			}
		}
		return result;
	}
	
	public CQLKeywords getCQLKeywordsLists() {
		return cqlService.getCQLKeyWords();
	}
	
	public GetUsedCQLArtifactsResult getUsedCqlArtifacts(String libraryId) {
		CQLLibrary library = cqlLibraryDAO.find(libraryId);
		String cqlXml = getCQLLibraryXml(library);
		return cqlService.getUsedCQlArtifacts(cqlXml);
	}
	
}
