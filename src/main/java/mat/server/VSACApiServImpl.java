package mat.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import mat.client.cqlworkspace.valuesets.CQLAppliedValueSetUtility;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.vsac.VSACGroovyClient;
import org.vsac.VSACResponseResult;

import mat.client.umls.service.VsacApiResult;
import mat.client.umls.service.VsacTicketInformation;
import mat.dao.DataTypeDAO;
import mat.model.DataType;
import mat.model.DirectReferenceCode;
import mat.model.MatValueSet;
import mat.model.VSACExpansionProfileWrapper;
import mat.model.VSACValueSetWrapper;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.server.service.MeasureLibraryService;
import mat.server.service.VSACApiService;
import mat.server.service.impl.XMLMarshalUtil;
import mat.server.util.UMLSSessionTicket;
import mat.shared.CQLModelValidator;
import mat.shared.ConstantMessages;

import static mat.client.cqlworkspace.valuesets.CQLAppliedValueSetUtility.getOidFromUrl;

@Service
public class VSACApiServImpl implements VSACApiService{

	private static final Log LOGGER = LogFactory.getLog(VSACAPIServiceImpl.class);

	private static final int VSAC_TIME_OUT_FAILURE_CODE = 3;
	
	private String PROXY_HOST;
	private int PROXY_PORT;
	private String server;
	private String service;
	private String retieriveMultiOIDSService;
	private String profileService;
	private VSACGroovyClient vGroovyClient;
	private String versionService;
	@Value("${mat.qdm.default.expansion.id}")
	private String defaultExpId;
	private String vsacServerDRCUrl;
	
	@Autowired DataTypeDAO dataTypeDAO;
	@Autowired MeasureLibraryService measureLibraryService;
	@Autowired CQLLibraryService cqlLibraryService;
	
	public VSACApiServImpl(){
		
		PROXY_HOST = System.getProperty("https.proxyHost");
		if(PROXY_HOST !=null) {
			PROXY_PORT = Integer.parseInt(System.getProperty("https.proxyPort"));
		}
		server = System.getProperty("SERVER_TICKET_URL");
		service = System.getProperty("SERVICE_URL");
		retieriveMultiOIDSService = System.getProperty("SERVER_MULTIPLE_VALUESET_URL_NEW");
		profileService = System.getProperty("PROFILE_SERVICE");
		versionService = System.getProperty("VERSION_SERVICE");
		vsacServerDRCUrl = System.getProperty("VSAC_DRC_URL");
		
		vGroovyClient = new VSACGroovyClient(PROXY_HOST, PROXY_PORT, server,service,retieriveMultiOIDSService,profileService,versionService,vsacServerDRCUrl);
	}
	
	/**
	 * Instantiates a new VSACAPI service impl.
	 */
	
	/**
	 * Private method to Convert VSAC xml pay load into Java object through
	 * Castor.
	 * @param xmlPayLoad
	 *            - String vsac pay load.
	 * @return VSACValueSetWrapper.
	 * */
	private VSACValueSetWrapper convertXmltoValueSet(final String xmlPayLoad) {
		LOGGER.info("Start VSACAPIServiceImpl convertXmltoValueSet");
		VSACValueSetWrapper details = null;
		String xml = xmlPayLoad;
		if (StringUtils.isNotBlank(xml)) {
			LOGGER.info("xml To reterive RetrieveMultipleValueSetsResponse tag is not null ");
		}
		try {
			XMLMarshalUtil xmlMarshalUtil = new XMLMarshalUtil();
			details = (VSACValueSetWrapper) xmlMarshalUtil.convertXMLToObject("MultiValueSetMapping.xml", xml, VSACValueSetWrapper.class);
			LOGGER.info("unmarshalling complete..RetrieveMultipleValueSetsResponse" + details.getValueSetList().get(0).getDefinition());
		} catch (MarshalException | ValidationException | MappingException | IOException e) {
			LOGGER.debug("Exception in convertXmltoValueSet:" + e);
			e.printStackTrace();
		}
		LOGGER.info("End VSACAPIServiceImpl convertXmltoValueSet");
		return details;
	}
	
	/**
	 * Convert xml to profile list.
	 *
	 * @param xmlPayLoad the xml pay load
	 * @return the VSAC profile wrapper
	 */
	private VSACExpansionProfileWrapper convertXmlToProfileList(final String xmlPayLoad){
		LOGGER.info("Start VSACAPIServiceImpl convertXmlToProfileList");
		VSACExpansionProfileWrapper profileDetails = null;
		String xml = xmlPayLoad;
		if (StringUtils.isNotBlank(xml)) {
			LOGGER.info("xml To reterive RetrieveVSACProfileListResponse tag is not null ");
		}
		
		try {
			XMLMarshalUtil xmlMarshalUtil = new XMLMarshalUtil();
			profileDetails = (VSACExpansionProfileWrapper) xmlMarshalUtil.convertXMLToObject("VSACExpIdentifierMapping.xml", xml, VSACExpansionProfileWrapper.class);
			LOGGER.info("unmarshalling complete..RetrieveVSACProfileListResponse" + profileDetails.getExpProfileList().get(0).getName());
		} catch (MarshalException | ValidationException | MappingException | IOException e) {
			LOGGER.debug("Exception in convertXmlToProfileList:" + e);
			e.printStackTrace();
		}
		LOGGER.info("End VSACAPIServiceImpl convertXmltoValueSet");
		return profileDetails;
	}
	
	private DirectReferenceCode convertXmltoDirectCodeRef(final String xmlPayLoad) {
		LOGGER.info("Start VSACAPIServiceImpl convertXmltoValueSet");		
		DirectReferenceCode details = null;
		int firstIndex = xmlPayLoad.indexOf("<csCode>");
		int lastIndex = xmlPayLoad.lastIndexOf("</csCode>");
		
		String xml = xmlPayLoad.substring(firstIndex, lastIndex).concat("</csCode>");
		if (StringUtils.isNotBlank(xml)) {
			LOGGER.info("xml To reterive csCode tag is not null ");
		}
		
		try {
			XMLMarshalUtil xmlMarshalUtil = new XMLMarshalUtil();
			details = (DirectReferenceCode) xmlMarshalUtil.convertXMLToObject("DirectCodeReferenceMapping.xml", xml, DirectReferenceCode.class);
			LOGGER.info("unmarshalling complete..csCode" + details.getCodeDescriptor());
		} catch (MarshalException | ValidationException | MappingException | IOException e) {
			LOGGER.debug("Exception in convertXmltoDirectCodeRef:" + e);
			e.printStackTrace();
		}
		LOGGER.info("End VSACAPIServiceImpl convertXmltoDirectCodeRef");
		return details;
	}
	
	
	@Override
	public final void inValidateVsacUser(String sessionId) {
		LOGGER.info("Start VSACAPIServiceImpl inValidateVsacUser");
		UMLSSessionTicket.remove(sessionId);
		LOGGER.info("End VSACAPIServiceImpl inValidateVsacUser");
	}
	
	@Override
	public final VsacTicketInformation getTicketGrantingTicket(String sessionId) {
		return UMLSSessionTicket.getTicket(sessionId);
	}
	/**
	 *Method to check if User already signed in at VSAC.
	 *@return Boolean.
	 ***/
	@Override
	public final boolean isAlreadySignedIn(String sessionId) {
		LOGGER.info("Start VSACAPIServiceImpl isAlreadySignedIn");
		VsacTicketInformation eightHourTicketForUser = UMLSSessionTicket.getTicket(sessionId);
		LOGGER.info("End VSACAPIServiceImpl isAlreadySignedIn: ");
		return eightHourTicketForUser != null;
	}
	
	/**
	 * Method to retrive All Profile List from VSAC.
	 *
	 * @return the all profile list
	 */
	@Override
	public final VsacApiResult getAllExpIdentifierList(String sessionId) {
		VsacApiResult result = new VsacApiResult();
		LOGGER.info("Start VSACAPIServiceImpl getAllExpIdentifierList method :");
		if (isAlreadySignedIn(sessionId)) {
			String fiveMinuteServiceTicket = vGroovyClient.getServiceTicket(UMLSSessionTicket.getTicket(sessionId).getTicket());
			VSACResponseResult vsacResponseResult = null;
			try {
				vsacResponseResult = vGroovyClient.getProfileList(fiveMinuteServiceTicket);
			} catch (Exception ex) {
				LOGGER.info("VSACAPIServiceImpl ExpIdentifierList failed in method :: getAllProfileList");
			}
			if ((vsacResponseResult != null) && (vsacResponseResult.getXmlPayLoad() != null)) {
				if (vsacResponseResult.getIsFailResponse() && (vsacResponseResult.getFailReason() == VSAC_TIME_OUT_FAILURE_CODE)) {
					LOGGER.info("Expansion Identifier List reterival failed at VSAC with Failure Reason: " + vsacResponseResult.getFailReason());
					result.setSuccess(false);
					result.setFailureReason(vsacResponseResult.getFailReason());
					return result;
				}
				if ((vsacResponseResult.getXmlPayLoad() != null) && StringUtils.isNotEmpty(vsacResponseResult.getXmlPayLoad())) {
					// Caster conversion here.
					VSACExpansionProfileWrapper wrapper = convertXmlToProfileList(vsacResponseResult.getXmlPayLoad());
					result.setVsacExpProfileResp(wrapper.getExpProfileList());
					return result;
				}
			}
		} else {
			result.setSuccess(false);
			result.setFailureReason(VsacApiResult.UMLS_NOT_LOGGEDIN);
			LOGGER.info("VSACAPIServiceImpl getAllExpIdentifierList :: UMLS Login is required");
		}
		LOGGER.info("End VSACAPIServiceImpl getAllExpIdentifierList method :");
		return result;
	}
	
	/**
	 * *
	 * Method to update valueset's without versions from VSAC in CQLLookUp XML tag.
	 * Skip supplemental Data Elements and Timing elements, Expired, Birth date and User defined QDM.
	 *
	 * @param appliedQDMList            - Applied Value Set List
	 * @param defaultExpId the default exp id
	 * @return VsacApiResult - Result.
	 */
	@Override
	public final VsacApiResult updateCQLVSACValueSets(List<CQLQualityDataSetDTO> appliedQDMList, String defaultExpId, String sessionId) {
		VsacApiResult result = new VsacApiResult();
		LOGGER.info("Start VSACAPIServiceImpl updateCQLVSACValueSets method :");

		if (isAlreadySignedIn(sessionId)) {
			if (isCASTicketGrantingTicketValid(sessionId)) {
				HashMap<CQLQualityDataSetDTO, CQLQualityDataSetDTO> updateInMeasureXml = new HashMap<>();
				ArrayList<CQLQualityDataSetDTO> modifiedQDMList = new ArrayList<>();
				if (defaultExpId == null) {
					defaultExpId = getDefaultExpId();
				}
				for (CQLQualityDataSetDTO cqlQualityDataSetDTO : appliedQDMList) {
					CQLQualityDataSetDTO toBeModifiedQDM = cqlQualityDataSetDTO;
					LOGGER.info(" VSACAPIServiceImpl updateCQLVSACValueSets :: OID:: " + cqlQualityDataSetDTO.getOid());
					// Filter out Timing Element , Expired, Birthdate, User defined QDM's and
					// supplemental data elements.
					if (ConstantMessages.TIMING_ELEMENT.equals(cqlQualityDataSetDTO.getDataType())
							|| ConstantMessages.USER_DEFINED_QDM_OID.equalsIgnoreCase(cqlQualityDataSetDTO.getOid())
							|| ConstantMessages.BIRTHDATE_OID.equals(cqlQualityDataSetDTO.getOid())
							|| ConstantMessages.DEAD_OID.equals(cqlQualityDataSetDTO.getOid())) {
						LOGGER.info("VSACAPIServiceImpl updateCQLVSACValueSets :: QDM filtered as it is of either"
								+ "for following type Supplemental data or User defined or Timing Element.");
						if (ConstantMessages.USER_DEFINED_QDM_OID.equalsIgnoreCase(cqlQualityDataSetDTO.getOid())) {
							toBeModifiedQDM.setNotFoundInVSAC(true);
							toBeModifiedQDM.setHasModifiedAtVSAC(true);
							modifiedQDMList.add(toBeModifiedQDM);
							DataType qdmDataType = dataTypeDAO.findByDataTypeName(toBeModifiedQDM.getDataType());
							if ((qdmDataType == null) 
									|| ConstantMessages.PATIENT_CHARACTERISTIC_BIRTHDATE.equals(cqlQualityDataSetDTO.getDataType()) || ConstantMessages.PATIENT_CHARACTERISTIC_EXPIRED.equals(cqlQualityDataSetDTO.getDataType())) {
								toBeModifiedQDM.setDataTypeHasRemoved(true);
							}
						}
						continue;
					} else {
						LOGGER.info("Start ValueSetsResponseDAO...Using Proxy:" + PROXY_HOST + ":" + PROXY_PORT);
						VSACResponseResult vsacResponseResult = null;
						try {
							String fiveMinuteServiceTicket = vGroovyClient.getServiceTicket(UMLSSessionTicket.getTicket(sessionId).getTicket());
								if (StringUtils.isNotBlank(cqlQualityDataSetDTO.getRelease())) {
									vsacResponseResult = vGroovyClient.getMultipleValueSetsResponseByOIDAndRelease(
											getOidFromUrl(cqlQualityDataSetDTO.getOid()), cqlQualityDataSetDTO.getRelease(), fiveMinuteServiceTicket);
								} else if (StringUtils.isNotBlank(cqlQualityDataSetDTO.getVersion())) {
									vsacResponseResult = vGroovyClient.getMultipleValueSetsResponseByOIDAndVersion(
											getOidFromUrl(cqlQualityDataSetDTO.getOid()), cqlQualityDataSetDTO.getVersion(), fiveMinuteServiceTicket);
								} else {
									vsacResponseResult = vGroovyClient.getMultipleValueSetsResponseByOID(
											getOidFromUrl(cqlQualityDataSetDTO.getOid()), fiveMinuteServiceTicket, defaultExpId);
								}
								
						} catch (Exception ex) {
							LOGGER.info("VSACAPIServiceImpl updateCQLVSACValueSets :: Value Set reterival failed at "
									+ "VSAC for OID :" + cqlQualityDataSetDTO.getOid() + " with Data Type : "
									+ cqlQualityDataSetDTO.getDataType());
						}
						if (vsacResponseResult != null && vsacResponseResult.getXmlPayLoad() != null) {
							if (vsacResponseResult.getIsFailResponse()
									&& (vsacResponseResult.getFailReason() == VSAC_TIME_OUT_FAILURE_CODE)) {
								LOGGER.info("Value Set reterival failed at VSAC for OID :"
										+ cqlQualityDataSetDTO.getOid() + " with Data Type : "
										+ cqlQualityDataSetDTO.getDataType() + ". Failure Reason: "
										+ vsacResponseResult.getFailReason());
								result.setSuccess(false);
								result.setFailureReason(vsacResponseResult.getFailReason());
								return result;
							}
							if (vsacResponseResult.getXmlPayLoad() != null && StringUtils.isNotEmpty(vsacResponseResult.getXmlPayLoad())) {
								VSACValueSetWrapper wrapper = convertXmltoValueSet(vsacResponseResult.getXmlPayLoad());
								MatValueSet matValueSet = wrapper.getValueSetList().get(0);
								if (matValueSet != null) {
									cqlQualityDataSetDTO.setName(matValueSet.getDisplayName());
									cqlQualityDataSetDTO.setOriginalCodeListName(matValueSet.getDisplayName());
									if(cqlQualityDataSetDTO.getSuffix() != null && !cqlQualityDataSetDTO.getSuffix().isEmpty()){
										cqlQualityDataSetDTO.setName(matValueSet.getDisplayName()+" ("+cqlQualityDataSetDTO.getSuffix()+")");
									}
									if (matValueSet.isGrouping()) {
										cqlQualityDataSetDTO.setTaxonomy(ConstantMessages.GROUPING_CODE_SYSTEM);
									} else {
										if (matValueSet.getConceptList().getConceptList() != null) {
											cqlQualityDataSetDTO.setTaxonomy(matValueSet.getConceptList().getConceptList().get(0).getCodeSystemName());
										} else {
											cqlQualityDataSetDTO.setTaxonomy(StringUtils.EMPTY);
										}
									}
									cqlQualityDataSetDTO.setValueSetType(matValueSet.getType());
									updateInMeasureXml.put(cqlQualityDataSetDTO, toBeModifiedQDM);
									toBeModifiedQDM.setHasModifiedAtVSAC(true); // Used at Applied QDM Tab
									//to show icons in CellTable.
								}
							} else {
								toBeModifiedQDM.setHasModifiedAtVSAC(true);
								toBeModifiedQDM.setNotFoundInVSAC(true);
							}
						} else {
							toBeModifiedQDM.setHasModifiedAtVSAC(true);
							toBeModifiedQDM.setNotFoundInVSAC(true);
						}
					}
					modifiedQDMList.add(toBeModifiedQDM);
				}
				result.setCqlQualityDataSetMap(updateInMeasureXml);
				result.setSuccess(true);
				result.setUpdatedCQLQualityDataDTOLIst(modifiedQDMList);
			} else {
				disconnectUMLSOnServiceTicketFailure(sessionId, result);
			}
		} else {
			result.setSuccess(false);
			result.setFailureReason(VsacApiResult.UMLS_NOT_LOGGEDIN);
			LOGGER.info("VSACAPIServiceImpl updateCQLVSACValueSets :: UMLS Login is required");
		}
		LOGGER.info("End VSACAPIServiceImpl updateCQLVSACValueSets method :");
		return result;
	}
	
	public boolean isCASTicketGrantingTicketValid(String sessionId) {
		String fiveMinuteServiceTicket = vGroovyClient.getServiceTicket(UMLSSessionTicket.getTicket(sessionId).getTicket());
		return StringUtils.isNotBlank(fiveMinuteServiceTicket);
	}
	
	/**
	 *Method to authenticate user at VSAC and save eightHourTicket into UMLSSessionMap for valid user.
	 *@param userName - String.
	 *@param password - String.
	 *@return Boolean.
	 * **/
	@Override
	public final boolean validateVsacUser(final String userName, final String password, String sessionId) {
		LOGGER.info("Start VSACAPIServiceImpl validateVsacUser");
		String eightHourTicketForUser = vGroovyClient.getTicketGrantingTicket(userName, password);
		VsacTicketInformation ticketInformation = new VsacTicketInformation(eightHourTicketForUser, new Date());
		UMLSSessionTicket.put(sessionId, ticketInformation);
		LOGGER.info("End VSACAPIServiceImpl validateVsacUser: " + " Ticket issued for 8 hours: " + eightHourTicketForUser);
		return eightHourTicketForUser != null;
	}
	
	@Override
	public final VsacApiResult getDirectReferenceCode (String url, String sessionId) {
		Callable<VsacApiResult> getCodeTask = new Callable<VsacApiResult>() {
			@Override
			public VsacApiResult call() {
				return getCodeFromVsac(url, sessionId);
			}
		};
		
		return taskExecutor(getCodeTask);
	}
	
	/**
	 * Fetches a direct reference code from vsac
	 * @param url the url to fetch
	 * @param sessionId the user's session id
	 * @return the result of the vsac api call
	 */
	private VsacApiResult getCodeFromVsac(String url, String sessionId) {
		LOGGER.info("Start VSACAPIServiceImpl getDirectReferenceCode method : url entered :" + url);
		VsacApiResult result = new VsacApiResult();
		CQLModelValidator validator = new CQLModelValidator();
		
		if(validator.validateForCodeIdentifier(url)){
			result.setSuccess(false);
			result.setFailureReason(VsacApiResult.INVALID_CODE_URL);
			return result;
		}  
		
		String eightHourTicket = UMLSSessionTicket.getTicket(sessionId).getTicket();
		if (StringUtils.isNotBlank(eightHourTicket)) {
			if (StringUtils.isNotBlank(url)) {
				LOGGER.info(" VSACAPIServiceImpl getDirectReferenceCode method Using Proxy:" + PROXY_HOST + ":" + PROXY_PORT);
				String fiveMinServiceTicket = vGroovyClient.getServiceTicket(eightHourTicket);
				if (StringUtils.isNotBlank(fiveMinServiceTicket)) {
					if(url.contains(":")){
						String[] arg = url.split(":");
						if(arg.length >0 && arg[1] != null) {
							url = arg[1];
							LOGGER.info("VSACAPIServiceImpl getDirectReferenceCode method : URL after dropping text before : is :: "+ url);
						}
					}
					VSACResponseResult vsacResponseResult = vGroovyClient.getDirectReferenceCode(url, fiveMinServiceTicket);	
					
					if(vsacResponseResult != null && vsacResponseResult.getXmlPayLoad() != null && !StringUtils.isEmpty(vsacResponseResult.getXmlPayLoad())) {
						DirectReferenceCode referenceCode = convertXmltoDirectCodeRef(vsacResponseResult.getXmlPayLoad());
						result.setDirectReferenceCode(referenceCode);
						result.setSuccess(true);
					}
					
				} else {
					disconnectUMLSOnServiceTicketFailure(sessionId, result);
				}
			
			}  else {
				result.setSuccess(false);
				result.setFailureReason(VsacApiResult.CODE_URL_REQUIRED);
				LOGGER.info("URL is required");
			}
		} else {
			result.setSuccess(false);
			result.setFailureReason(VsacApiResult.UMLS_NOT_LOGGEDIN);
			LOGGER.info("UMLS Login is required");
		}
		
		LOGGER.info("End VSACAPIServiceImpl getDirectReferenceCode method : url entered :" + url);
		return result;
	}
	
	@Override
	public final VsacApiResult getMostRecentValueSetByOID(final String oid, final String release, String expansionId, String sessionId) {
		Callable<VsacApiResult> getValuesetTask = new Callable<VsacApiResult>() {
			@Override
			public VsacApiResult call() {
				return getMostRecentValuesetByOID(oid, release, expansionId, sessionId);
			}
		};
		
		return taskExecutor(getValuesetTask);
	}
	
	/**
	 * Executes a task which returns the VsacApiResult. This request will timeout after 60 seconds. 
	 * @param task the task to execute
	 * @return the VsacApiResult which was created during the call to the future
	 */
	private VsacApiResult taskExecutor(Callable<VsacApiResult> task) {
		ExecutorService executor = Executors.newCachedThreadPool();
		Future<VsacApiResult> futureGetValuesetTask = executor.submit(task);
		VsacApiResult result = new VsacApiResult();
		try {
			result = futureGetValuesetTask.get(60, TimeUnit.SECONDS);
			return result;
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.error("taskExecutor:" + e.getMessage());
		} catch (java.util.concurrent.TimeoutException e) {
			LOGGER.info("VSACAPIServiceIMpl: Call to the VSAC API timed out." + e.getMessage());
			result.setSuccess(false);
			result.setFailureReason(VsacApiResult.VSAC_REQUEST_TIMEOUT);
		}

		return result;
	}
	
	/**
	 * Calls the VSAC Api to get a valueset with a timeout
	 * @param oid the oid of the valueset
	 * @param expansionId the expansion id
	 * @param sessionId the session id 
	 * @param timeout the timeout in seconds
	 * @return
	 */
	private final VsacApiResult getMostRecentValuesetByOID(final String oid, String release, String expansionId, String sessionId) {
		LOGGER.info("Start VSACAPIServiceImpl getValueSetBasedOIDAndVersion method : oid entered :" + oid
				+ "for Expansion Identifier :" + expansionId);
		VsacApiResult result = new VsacApiResult();
		String eightHourTicket = UMLSSessionTicket.getTicket(sessionId).getTicket();
		
		if (eightHourTicket != null) {
			if (StringUtils.isNotBlank(oid)) {
				LOGGER.info("Start ValueSetsResponseDAO...Using Proxy:" + PROXY_HOST + ":" + PROXY_PORT);
				
				String fiveMinServiceTicket = vGroovyClient.getServiceTicket(eightHourTicket);
				
				if (StringUtils.isNotBlank(fiveMinServiceTicket)) {
					VSACResponseResult vsacResponseResult = null;
					
					if (StringUtils.isNotBlank(release)){					
						vsacResponseResult = vGroovyClient.getMultipleValueSetsResponseByOIDAndRelease(oid.trim(), release, fiveMinServiceTicket);
					} else {
						if (StringUtils.isBlank(expansionId)) {
							expansionId = getDefaultExpId();
						}
						
						vsacResponseResult = vGroovyClient.getMultipleValueSetsResponseByOID(oid.trim(), fiveMinServiceTicket, expansionId);
					}
									
					if (vsacResponseResult != null && StringUtils.isNotBlank(vsacResponseResult.getXmlPayLoad())) {
						result.setSuccess(true);
						VSACValueSetWrapper wrapper = convertXmltoValueSet(vsacResponseResult.getXmlPayLoad());
						result.setVsacResponse(wrapper.getValueSetList());
						LOGGER.info("Successfully converted valueset object from vsac xml payload.");
					} else {
						result.setSuccess(false);
						LOGGER.info("Unable to retrieve value set in VSAC.");
					}
					
				} else { 
					disconnectUMLSOnServiceTicketFailure(sessionId, result);
				}
				
			} else {
				result.setSuccess(false);
				result.setFailureReason(VsacApiResult.OID_REQUIRED);
				LOGGER.info("OID is required");
			}
		} else {
			result.setSuccess(false);
			result.setFailureReason(VsacApiResult.UMLS_NOT_LOGGEDIN);
			LOGGER.info("UMLS Login is required");
		}
		LOGGER.info("End VSACAPIServiceImpl getValueSetBasedOIDAndVersion method : oid entered :"
				+ oid + "for Expansion Identifier entered :" + expansionId);
		return result;
	}

	private VsacApiResult disconnectUMLSOnServiceTicketFailure(String sessionId, VsacApiResult result) {
		LOGGER.info("VSACApiServImpl Ticket :" + UMLSSessionTicket.getTicket(sessionId).getTicket() + " has expired");
		result.setSuccess(false);
		result.setFailureReason(VsacApiResult.VSAC_UNAUTHORIZED_ERROR);
		inValidateVsacUser(sessionId);
		return result;
	}
	
	/**
	 * Gets the default exp id.
	 *
	 * @return the default exp id
	 */
	public String getDefaultExpId() {
		return defaultExpId;
	}
	
	/**
	 * Sets the default exp id.
	 *
	 * @param defaultExpId the new default exp id
	 */
	public void setDefaultExpId(String defaultExpId) {
		this.defaultExpId = defaultExpId;
	}
	
	public String getVsacServerUrl() {
		return vsacServerDRCUrl;
	}

	public void setVsacServerUrl(String vsacServerUrl) {
		this.vsacServerDRCUrl = vsacServerUrl;
	}

	@Override
	public VsacApiResult getVSACProgramsReleasesAndProfiles() {

		LOGGER.info("Start VSACAPIServiceImpl getProgramsList method :");
		VsacApiResult result = new VsacApiResult();
		
		try {		
			
			VSACResponseResult vsacResponseResult = vGroovyClient.getAllPrograms();
			
			if(vsacResponseResult != null && vsacResponseResult.getPgmRels() != null) {				
				if (vsacResponseResult.getIsFailResponse() && (vsacResponseResult.getFailReason() == VSAC_TIME_OUT_FAILURE_CODE)) {
					LOGGER.info("Program List retrieval failed at VSAC with Failure Reason: " + vsacResponseResult.getFailReason());
					result.setSuccess(false);
					result.setFailureReason(vsacResponseResult.getFailReason());
					return result;

				} else {
					
					Map<String, List<String>> programToReleases = new HashMap<>();
					Map<String, String> programToProfiles = new HashMap<>();
					
					for (String program : vsacResponseResult.getPgmRels()) {
						programToReleases.put(program, getReleasesListForProgram(program));
						programToProfiles.put(program, getLatestProfileOfProgram(program));
					}
					
					result.setProgramToReleases(programToReleases);
					result.setProgramToProfiles(programToProfiles);
				}

			}				

		} catch (Exception e) {
			LOGGER.error("getVSACProgramsAndReleases : " + e.getMessage());
		}

		return result;

	}

	private List<String> getReleasesListForProgram(String programName) {
		LOGGER.info("Start VSACAPIServiceImpl getProgramsList method :");
		VSACResponseResult vsacResponseResult = null;
		try {
			vsacResponseResult = vGroovyClient.getReleasesOfProgram(programName);
		} catch (Exception e) {
			LOGGER.error("getReleasesListForProgram: " + e.getMessage());
		}
		return vsacResponseResult != null ? vsacResponseResult.getPgmRels() : null;
	}
	
	private String getLatestProfileOfProgram(String programName) {
		LOGGER.info("Start getProfilesForProgram method :");
		VSACResponseResult vsacResponseResult = null;
		try {
			vsacResponseResult = vGroovyClient.getLatestProfileOfProgram(programName);
		} catch (Exception e) {
			LOGGER.error("getLatestProfileOfProgram: " + e.getMessage());
		}
		return vsacResponseResult != null ? vsacResponseResult.getXmlPayLoad() : null;
	}
}