package mat.server;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.xpath.XPathExpressionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import mat.client.measure.service.CQLService;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.client.shared.MatContext;
import mat.dao.UserDAO;
import mat.dao.clause.CQLLibraryDAO;
import mat.dao.clause.CQLLibrarySetDAO;
import mat.model.LockedUserInfo;
import mat.model.User;
import mat.model.clause.CQLLibrary;
import mat.model.clause.CQLLibrarySet;
import mat.model.clause.MeasureSet;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLModel;
import mat.server.service.CQLLibraryServiceInterface;
import mat.server.service.UserService;
import mat.server.util.MATPropertiesService;
import mat.server.util.MeasureUtility;
import mat.server.util.ResourceLoader;
import mat.server.util.XmlProcessor;
import mat.shared.CQLModelValidator;
import mat.shared.SaveUpdateCQLResult;
import mat.shared.UUIDUtilClient;

public class CQLLibraryService implements CQLLibraryServiceInterface {
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
	public void save(CQLLibrary library) {
		
		/*CQLLibrary cqlLibrary = new CQLLibrary(); 
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
		cqlLibrary.setCQLByteArray(cqlByteArray);*/
		
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
		String cqlLookUpString = getCQLLookUpXml(libraryName, version,xmlProcessor);
		return cqlLookUpString;
	}
	
	
	/**
	 * @param cqlLibraryDataSetObject
	 */
	@Override
	public String getCQLLookUpXml(String libraryName, String versionText, XmlProcessor xmlProcessor) {
		String cqlLookUp = null;
		try {
			Node cqlTemplateNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), "/cqlTemplate");
			Node cqlLookUpNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), "//cqlLookUp");
			String xPath_ID = "//cqlLookUp/child::node()/*[@id]";
			String xPath_UUID = "//cqlLookUp/child::node()/*[@uuid]";
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
										.setNodeValue(UUIDUtilClient.uuid());
							}
						} else if (changeAttribute.equalsIgnoreCase("uuid")) {
							NodeList nodesForUUId = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(),
									xPath_UUID);
							for (int i = 0; i < nodesForUUId.getLength(); i++) {
								Node node = nodesForUUId.item(i);
								node.getAttributes().getNamedItem("uuid")
										.setNodeValue(UUIDUtilClient.uuid());
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
									"//" + nodeTextToChange);
							if (libraryNode != null) {
								libraryNode.setTextContent(libraryName);
							}
						} else if (nodeTextToChange.equalsIgnoreCase("version")) {
							Node versionNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(),
									"//" + nodeTextToChange);
							if (versionNode != null) {
								versionNode.setTextContent(versionText);
							}
						} else if (nodeTextToChange.equalsIgnoreCase("usingModelVersion")) {
							Node usingModelVersionNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(),
									"//" + nodeTextToChange);
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
		//cqlResult = cqlService.getCQLData(id, fromTable);
		return cqlResult;
		
	}

}
