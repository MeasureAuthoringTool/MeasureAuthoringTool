package mat.server;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.vsac.VSACGroovyClient;
import org.vsac.VSACResponseResult;
import org.xml.sax.InputSource;

import mat.client.umls.service.VsacApiResult;
import mat.dao.DataTypeDAO;
import mat.model.DataType;
import mat.model.DirectReferenceCode;
import mat.model.MatValueSet;
import mat.model.VSACExpansionProfileWrapper;
import mat.model.VSACValueSetWrapper;
import mat.model.VSACVersionWrapper;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.server.service.MeasureLibraryService;
import mat.server.service.VSACApiService;
import mat.server.util.ResourceLoader;
import mat.server.util.UMLSSessionTicket;
import mat.shared.CQLModelValidator;
import mat.shared.ConstantMessages;


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
	private String defaultExpId;
	private String vsacServerDRCUrl;
	
	@Autowired DataTypeDAO dataTypeDAO;
	@Autowired MeasureLibraryService measureLibraryService;
	@Autowired CQLLibraryService cqlLibraryService;
	
	public VSACApiServImpl(){
		PROXY_HOST = System.getProperty("vsac_proxy_host");
		if(PROXY_HOST !=null) {
			PROXY_PORT = Integer.parseInt(System.getProperty("vsac_proxy_port"));
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
		if ((xml != null) && StringUtils.isNotBlank(xml)) {
			LOGGER.info("xml To reterive RetrieveMultipleValueSetsResponse tag is not null ");
		}
		try {
			Mapping mapping = new Mapping();
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("MultiValueSetMapping.xml"));
			Unmarshaller unmar = new Unmarshaller(mapping);
			unmar.setClass(VSACValueSetWrapper.class);
			unmar.setWhitespacePreserve(true);
			details = (VSACValueSetWrapper) unmar.unmarshal(new InputSource(new StringReader(xml)));
			LOGGER.info("unmarshalling complete..RetrieveMultipleValueSetsResponse" + details.getValueSetList().get(0).getDefinition());
		} catch (Exception e) {
			if (e instanceof IOException) {
				LOGGER.info("Failed to load MultiValueSetMapping.xml" + e);
			} else if (e instanceof MappingException) {
				LOGGER.info("Mapping Failed" + e);
			} else if (e instanceof MarshalException) {
				LOGGER.info("Unmarshalling Failed" + e);
			} else {
				LOGGER.info("Other Exception" + e);
			}
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
		if ((xml != null) && StringUtils.isNotBlank(xml)) {
			LOGGER.info("xml To reterive RetrieveVSACProfileListResponse tag is not null ");
		}
		try {
			Mapping mapping = new Mapping();
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("VSACExpIdentifierMapping.xml"));
			Unmarshaller unmar = new Unmarshaller(mapping);
			unmar.setClass(VSACExpansionProfileWrapper.class);
			unmar.setWhitespacePreserve(true);
			profileDetails = (VSACExpansionProfileWrapper) unmar.unmarshal(new InputSource(new StringReader(xml)));
			LOGGER.info("unmarshalling complete..RetrieveVSACProfileListResponse" + profileDetails.getExpProfileList().get(0).getName());
		} catch (Exception e) {
			if (e instanceof IOException) {
				LOGGER.info("Failed to load VSACExpIdentifierMapping.xml" + e);
			} else if (e instanceof MappingException) {
				LOGGER.info("Mapping Failed" + e);
			} else if (e instanceof MarshalException) {
				LOGGER.info("Unmarshalling Failed" + e);
			} else {
				LOGGER.info("Other Exception" + e);
			}
		}
		LOGGER.info("End VSACAPIServiceImpl convertXmltoValueSet");
		return profileDetails;
	}
	
	/**
	 * Caster conversion for Version list.
	 * @param xmlPayLoad - String
	 * @return VSACVersionWrapper
	 */
	private VSACVersionWrapper convertXmlToVersionList(final String xmlPayLoad) {
		LOGGER.info("Start VSACAPIServiceImpl convertXmlToVersionList");
		VSACVersionWrapper versionDetails = null;
		String xml = xmlPayLoad;
		if ((xml != null) && StringUtils.isNotBlank(xml)) {
			LOGGER.info("xml To reterive RetrieveVSACVersionListResponse tag is not null ");
		}
		try {
			Mapping mapping = new Mapping();
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("VSACVersionMapping.xml"));
			Unmarshaller unmar = new Unmarshaller(mapping);
			unmar.setClass(VSACVersionWrapper.class);
			unmar.setWhitespacePreserve(true);
			versionDetails = (VSACVersionWrapper) unmar.unmarshal(new InputSource(new StringReader(xml)));
			if (versionDetails.getVersionList() != null) {
				LOGGER.info("unmarshalling complete..RetrieveVSACVersionListResponse" + versionDetails.getVersionList().get(0).getName());
			} else {
				LOGGER.info("unmarshalling complete..RetrieveVSACVersionListResponse Empty Version List");
			}
		} catch (Exception e) {
			if (e instanceof IOException) {
				LOGGER.info("Failed to load VSACVersionMapping.xml" + e);
			} else if (e instanceof MappingException) {
				LOGGER.info("Mapping Failed" + e);
			} else if (e instanceof MarshalException) {
				LOGGER.info("Unmarshalling Failed" + e);
			} else {
				LOGGER.info("Other Exception" + e);
			}
		}
		LOGGER.info("End VSACAPIServiceImpl convertXmlToVersionList");
		return versionDetails;
	}
	
	
	private DirectReferenceCode convertXmltoDirectCodeRef(final String xmlPayLoad) {
		LOGGER.info("Start VSACAPIServiceImpl convertXmltoValueSet");		
		DirectReferenceCode details = null;
		int firstIndex = xmlPayLoad.indexOf("<csCode>");
		int lastIndex = xmlPayLoad.lastIndexOf("</csCode>");
		
		String xml = xmlPayLoad.substring(firstIndex, lastIndex).concat("</csCode>");
		if ((xml != null) && StringUtils.isNotBlank(xml)) {
			LOGGER.info("xml To reterive csCode tag is not null ");
		}
		try {
			Mapping mapping = new Mapping();
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("DirectCodeReferenceMapping.xml"));
			Unmarshaller unmar = new Unmarshaller(mapping);
			unmar.setClass(DirectReferenceCode.class);
			unmar.setWhitespacePreserve(true);
			details = (DirectReferenceCode) unmar.unmarshal(new InputSource(new StringReader(xml)));
			LOGGER.info("unmarshalling complete..csCode" + details.getCodeDescriptor());
		} catch (Exception e) {
			if (e instanceof IOException) {
				LOGGER.info("Failed to load DirectCodeReferenceMapping.xml" + e);
			} else if (e instanceof MappingException) {
				LOGGER.info("Mapping Failed" + e);
			} else if (e instanceof MarshalException) {
				LOGGER.info("Unmarshalling Failed" + e);
			} else {
				LOGGER.info("Other Exception" + e);
			}
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
	/**
	 *Method to check if User already signed in at VSAC.
	 *@return Boolean.
	 ***/
	@Override
	public final boolean isAlreadySignedIn(String sessionId) {
		LOGGER.info("Start VSACAPIServiceImpl isAlreadySignedIn");
		String eightHourTicketForUser = UMLSSessionTicket.getTicket(sessionId);
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
			String fiveMinuteServiceTicket = vGroovyClient.getServiceTicket(UMLSSessionTicket.getTicket(sessionId));
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
	
	
	/* 
	 * {@inheritDoc}
	 */
	@Override
	public final VsacApiResult getAllVersionListByOID(String oid, String sessionId) {
		VsacApiResult result = new VsacApiResult();
		LOGGER.info("Start VSACAPIServiceImpl getAllProfileList method :");
		if (isAlreadySignedIn(sessionId)) {
			String fiveMinuteServiceTicket = vGroovyClient.getServiceTicket(UMLSSessionTicket.getTicket(sessionId));
			VSACResponseResult vsacResponseResult = null;
			try {
				vsacResponseResult = vGroovyClient.reteriveVersionListForOid(oid, fiveMinuteServiceTicket);
			} catch (Exception ex) {
				LOGGER.info("VSACAPIServiceImpl VersionList failed in method :: getAllVersionListByOID");
			}
			if ((vsacResponseResult != null) && (vsacResponseResult.getXmlPayLoad() != null)) {
				if (vsacResponseResult.getIsFailResponse() && (vsacResponseResult.getFailReason() == VSAC_TIME_OUT_FAILURE_CODE)) {
					LOGGER.info("Version List reterival failed at VSAC with Failure Reason: " + vsacResponseResult.getFailReason());
					result.setSuccess(false);
					result.setFailureReason(vsacResponseResult.getFailReason());
					return result;
				}
				if ((vsacResponseResult.getXmlPayLoad() != null) && StringUtils.isNotEmpty(vsacResponseResult.getXmlPayLoad())) {
					// Caster conversion here.
					VSACVersionWrapper wrapper = convertXmlToVersionList(vsacResponseResult.getXmlPayLoad());
					result.setVsacVersionResp(wrapper.getVersionList());
					return result;
				}
			}
		} else {
			result.setSuccess(false);
			result.setFailureReason(VsacApiResult.UMLS_NOT_LOGGEDIN);
			LOGGER.info("VSACAPIServiceImpl getAllVersionListByOID :: UMLS Login is required");
		}
		LOGGER.info("End VSACAPIServiceImpl getAllVersionListByOID method :");
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
			HashMap<CQLQualityDataSetDTO, CQLQualityDataSetDTO> updateInMeasureXml = new HashMap<CQLQualityDataSetDTO, CQLQualityDataSetDTO>();
			ArrayList<CQLQualityDataSetDTO> modifiedQDMList = new ArrayList<CQLQualityDataSetDTO>();
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
						|| cqlQualityDataSetDTO.isSuppDataElement()
						|| ConstantMessages.BIRTHDATE_OID.equals(cqlQualityDataSetDTO.getOid())
						|| ConstantMessages.EXPIRED_OID.equals(cqlQualityDataSetDTO.getOid())) {
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
						String fiveMinuteServiceTicket = vGroovyClient.getServiceTicket(
								UMLSSessionTicket.getTicket(sessionId));
							if (StringUtils.isNotBlank(cqlQualityDataSetDTO.getRelease())) {
								vsacResponseResult = vGroovyClient.getMultipleValueSetsResponseByOIDAndRelease(
									cqlQualityDataSetDTO.getOid(), cqlQualityDataSetDTO.getRelease(), fiveMinuteServiceTicket);
							} else if (!(cqlQualityDataSetDTO.getVersion().equals("1.0") 
									|| cqlQualityDataSetDTO.getVersion().equals("1"))) {
								vsacResponseResult = vGroovyClient.getMultipleValueSetsResponseByOIDAndVersion(
										cqlQualityDataSetDTO.getOid(), cqlQualityDataSetDTO.getVersion(), fiveMinuteServiceTicket);
							} else {
								vsacResponseResult = vGroovyClient.getMultipleValueSetsResponseByOID(
										cqlQualityDataSetDTO.getOid(), fiveMinuteServiceTicket, defaultExpId);
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
			result.setSuccess(false);
			result.setFailureReason(VsacApiResult.UMLS_NOT_LOGGEDIN);
			LOGGER.info("VSACAPIServiceImpl updateCQLVSACValueSets :: UMLS Login is required");
		}
		LOGGER.info("End VSACAPIServiceImpl updateCQLVSACValueSets method :");
		return result;
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
		UMLSSessionTicket.put(sessionId, eightHourTicketForUser);
		LOGGER.info("End VSACAPIServiceImpl validateVsacUser: " + " Ticket issued for 8 hours: " + eightHourTicketForUser);
		return eightHourTicketForUser != null;
	}
	
	@Override
	public final VsacApiResult getDirectReferenceCode (String url, String sessionId) {
		Callable<VsacApiResult> getCodeTask = new Callable<VsacApiResult>() {
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
		
		String eightHourTicket = UMLSSessionTicket.getTicket(sessionId);
		if (eightHourTicket != null) {
			if ((url != null) && StringUtils.isNotEmpty(url) && StringUtils.isNotBlank(url)) {
				LOGGER.info(" VSACAPIServiceImpl getDirectReferenceCode method Using Proxy:" + PROXY_HOST + ":" + PROXY_PORT);
				String fiveMinServiceTicket = vGroovyClient.getServiceTicket(eightHourTicket);
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
	
	
	/* 
	 * {@inheritDoc}
	 */
	@Override
	public final VsacApiResult getMostRecentValueSetByOID(final String oid, String expansionId, String sessionId) {
		Callable<VsacApiResult> getValuesetTask = new Callable<VsacApiResult>() {
			public VsacApiResult call() {
				return getMostRecentValuesetByOID(oid, expansionId, sessionId);
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
			result.setSuccess(true);
			return result;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (java.util.concurrent.TimeoutException e) {
			LOGGER.info("VSACAPIServiceIMpl: Call to the VSAC API timed out.");
			result.setSuccess(false);
			result.setFailureReason(VsacApiResult.VSAC_REQUEST_TIMEOUT);
			e.printStackTrace();
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
	private final VsacApiResult getMostRecentValuesetByOID(final String oid, String expansionId, String sessionId) {
		LOGGER.info("Start VSACAPIServiceImpl getValueSetBasedOIDAndVersion method : oid entered :" + oid
				+ "for Expansion Identifier :" + expansionId);
		VsacApiResult result = new VsacApiResult();
		String eightHourTicket = UMLSSessionTicket.getTicket(sessionId);
		
		if (eightHourTicket != null) {
			if (oid != null && StringUtils.isNotEmpty(oid) && StringUtils.isNotBlank(oid)) {
				LOGGER.info("Start ValueSetsResponseDAO...Using Proxy:" + PROXY_HOST + ":" + PROXY_PORT);
				String fiveMinServiceTicket = vGroovyClient.getServiceTicket(eightHourTicket);
				VSACResponseResult vsacResponseResult = null;
		
				if (StringUtils.isNotBlank(expansionId)){					
					vsacResponseResult = vGroovyClient.getMultipleValueSetsResponseByOIDAndRelease(oid.trim(), expansionId, fiveMinServiceTicket);
				}else {  
					expansionId = getDefaultExpId();
					vsacResponseResult = vGroovyClient.getMultipleValueSetsResponseByOID(oid.trim(),fiveMinServiceTicket, expansionId);
				}
								
				if(vsacResponseResult != null && vsacResponseResult.getXmlPayLoad() != null && !StringUtils.isEmpty(vsacResponseResult.getXmlPayLoad())) {
					result.setSuccess(true);
					VSACValueSetWrapper wrapper = convertXmltoValueSet(vsacResponseResult.getXmlPayLoad());
					result.setVsacResponse(wrapper.getValueSetList());
					LOGGER.info("Successfully converted valueset object from vsac xml payload.");
				} else {
					result.setSuccess(false);
					LOGGER.info("Unable to reterive value set in VSAC.");
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
				+ oid + "for Expansion Idnetifier entered :" + expansionId);
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
	public VsacApiResult getVSACProgramsAndReleases() {

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
					
					for (String program : vsacResponseResult.getPgmRels()) {
						programToReleases.put(program, getReleasesListForProgram(program));
					}
					
					result.setProgramToReleases(programToReleases);
				}

			}				

		} catch (Exception ex) {
			LOGGER.info("VSACAPIServiceImpl failed in method :: getProgramsList");
		}

		return result;

	}

	private List<String> getReleasesListForProgram(String programName) {
		LOGGER.info("Start VSACAPIServiceImpl getProgramsList method :");
		VSACResponseResult vsacResponseResult = null;
		try {
			vsacResponseResult = vGroovyClient.getReleasesOfProgram(programName);
		} catch (Exception ex) {
			LOGGER.info("VSACAPIServiceImpl failed in method :: getProgramsList");
		}
		return vsacResponseResult != null ? vsacResponseResult.getPgmRels() : null;
	}
}
