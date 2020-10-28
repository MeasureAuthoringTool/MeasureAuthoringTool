package mat.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import mat.client.umls.service.VsacApiResult;
import mat.client.umls.service.VsacTicketInformation;
import mat.dao.DataTypeDAO;
import mat.model.DataType;
import mat.model.DirectReferenceCode;
import mat.model.VSACExpansionProfileWrapper;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.server.logging.LogFactory;
import mat.server.service.MeasureLibraryService;
import mat.server.service.VSACApiService;
import mat.server.service.impl.XMLMarshalUtil;
import mat.server.util.UMLSSessionTicket;
import mat.shared.CQLModelValidator;
import mat.shared.ConstantMessages;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import mat.vsac.VsacService;
import mat.vsacmodel.BasicResponse;
import mat.vsacmodel.ValueSet;
import mat.vsacmodel.ValueSetWrapper;

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

import static mat.client.cqlworkspace.valuesets.CQLAppliedValueSetUtility.getOidFromUrl;

@Service
public class VSACApiServImpl implements VSACApiService{
	private static final Log LOGGER = LogFactory.getLog(VSACAPIServiceImpl.class);
	private static final int VSAC_TIME_OUT_FAILURE_CODE = 3;

	@Value("${mat.qdm.default.expansion.id}")
	private String defaultExpId;

	private JsonMapper jsonMapper = new JsonMapper();

	@Autowired
	private VsacService vsacService;
	@Autowired
	private DataTypeDAO dataTypeDAO;
	@Autowired
	private MeasureLibraryService measureLibraryService;
	@Autowired
	private CQLLibraryService cqlLibraryService;
	
	/**
	 * Private method to Convert VSAC xml pay load into Java object through
	 * Castor.
	 * @param xmlPayLoad
	 *            - String mat.vsac pay load.
	 * @return VSACValueSetWrapper.
	 * */
	private ValueSetWrapper convertXmltoValueSet(final String xmlPayLoad) {
		LOGGER.debug("Start VSACAPIServiceImpl convertXmltoValueSet");
		ValueSetWrapper details = null;
		String xml = xmlPayLoad;
		if (StringUtils.isNotBlank(xml)) {
			LOGGER.debug("xml To reterive RetrieveMultipleValueSetsResponse tag is not null ");
		}
		try {
			XMLMarshalUtil xmlMarshalUtil = new XMLMarshalUtil();
			details = (ValueSetWrapper) xmlMarshalUtil.convertXMLToObject(
					"MultiValueSetMapping.xml",
					xml,
					ValueSetWrapper.class);
			LOGGER.debug("unmarshalling complete..RetrieveMultipleValueSetsResponse" +
					details.getValueSetList().get(0).getDefinition());
		} catch (MarshalException | ValidationException | MappingException | IOException e) {
			LOGGER.debug("Exception in convertXmltoValueSet:" + e);
			e.printStackTrace();
		}
		LOGGER.debug("End VSACAPIServiceImpl convertXmltoValueSet");
		return details;
	}
	
	/**
	 * Convert xml to profile list.
	 *
	 * @param xmlPayLoad the xml pay load
	 * @return the VSAC profile wrapper
	 */
	private VSACExpansionProfileWrapper convertXmlToProfileList(final String xmlPayLoad){
		LOGGER.debug("Start VSACAPIServiceImpl convertXmlToProfileList");
		VSACExpansionProfileWrapper profileDetails = null;
		String xml = xmlPayLoad;
		if (StringUtils.isNotBlank(xml)) {
			LOGGER.debug("xml To reterive RetrieveVSACProfileListResponse tag is not null ");
		}
		
		try {
			XMLMarshalUtil xmlMarshalUtil = new XMLMarshalUtil();
			profileDetails = (VSACExpansionProfileWrapper) xmlMarshalUtil.convertXMLToObject(
					"VSACExpIdentifierMapping.xml",
					xml,
					VSACExpansionProfileWrapper.class);
			LOGGER.debug("unmarshalling complete..RetrieveVSACProfileListResponse" +
					profileDetails.getExpProfileList().get(0).getName());
		} catch (MarshalException | ValidationException | MappingException | IOException e) {
			LOGGER.debug("Exception in convertXmlToProfileList:" + e);
			e.printStackTrace();
		}
		LOGGER.debug("End VSACAPIServiceImpl convertXmltoValueSet");
		return profileDetails;
	}
	
	private DirectReferenceCode convertXmltoDirectCodeRef(final String jsonPayload) {
		LOGGER.debug("Start VSACAPIServiceImpl convertXmltoValueSet");		
		DirectReferenceCode details = null;
		//{
		//  "data": {
		//    "resultCount": 1,
		//    "resultSet": [
		//      {
		//        "csName": "ActMood",
		//        "csOID": "2.16.840.1.113883.5.1001",
		//        "csVersion": "HL7V3.0_2019-12",
		//        "code": "_ActMoodDesire",
		//        "contentMode": "Complete",
		//        "codeName": "desire",
		//        "termType": "PT",
		//        "active": "Yes",
		//        "revision": 2098070245
		//      }
		//    ]
		//  },
		//  "message": "ok",
		//  "status": "ok"
		//}

		try {
			JsonNode node =  jsonMapper.readTree(jsonPayload).get("data").get("resultSet").elements().next();
			details = new DirectReferenceCode();
			details.setCode(node.get("code").asText());
			details.setCodeDescriptor(node.get("codeName").asText());
			details.setCodeSystemName(node.get("csName").asText());
			details.setCodeSystemOid(node.get("csOID").asText());
			details.setCodeSystemVersion(node.get("csVersion").asText());
			LOGGER.debug(details);
		} catch (Exception e) {
			LOGGER.debug("Exception in convertXmltoDirectCodeRef:" , e);
		}
		return details;
	}
	
	
	@Override
	public final void inValidateVsacUser(String sessionId) {
		LOGGER.debug("Start VSACAPIServiceImpl inValidateVsacUser");
		UMLSSessionTicket.remove(sessionId);
		LOGGER.debug("End VSACAPIServiceImpl inValidateVsacUser");
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
		LOGGER.debug("Start VSACAPIServiceImpl isAlreadySignedIn");
		VsacTicketInformation eightHourTicketForUser = UMLSSessionTicket.getTicket(sessionId);
		LOGGER.debug("End VSACAPIServiceImpl isAlreadySignedIn: ");
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
		LOGGER.debug("Start VSACAPIServiceImpl getAllExpIdentifierList method :");
		if (isAlreadySignedIn(sessionId)) {
			String fiveMinuteServiceTicket = vsacService.getServiceTicket(
					UMLSSessionTicket.getTicket(sessionId).getTicket());
			BasicResponse vsacResponseResult = null;
			try {
				vsacResponseResult = vsacService.getProfileList(fiveMinuteServiceTicket);
			} catch (Exception ex) {
				LOGGER.debug("VSACAPIServiceImpl ExpIdentifierList failed in method :: getAllProfileList");
			}
			if ((vsacResponseResult != null) && (vsacResponseResult.getXmlPayLoad() != null)) {
				if (vsacResponseResult.isFailResponse() &&
						(vsacResponseResult.getFailReason() == VSAC_TIME_OUT_FAILURE_CODE)) {
					LOGGER.debug("Expansion Identifier List reterival failed at VSAC with Failure Reason: " +
							vsacResponseResult.getFailReason());
					result.setSuccess(false);
					result.setFailureReason(vsacResponseResult.getFailReason());
					return result;
				}
				if ((vsacResponseResult.getXmlPayLoad() != null) &&
						StringUtils.isNotEmpty(vsacResponseResult.getXmlPayLoad())) {
					// Caster conversion here.
					VSACExpansionProfileWrapper wrapper = convertXmlToProfileList(vsacResponseResult.getXmlPayLoad());
					result.setVsacExpProfileResp(wrapper.getExpProfileList());
					return result;
				}
			}
		} else {
			result.setSuccess(false);
			result.setFailureReason(VsacApiResult.UMLS_NOT_LOGGEDIN);
			LOGGER.debug("VSACAPIServiceImpl getAllExpIdentifierList :: UMLS Login is required");
		}
		LOGGER.debug("End VSACAPIServiceImpl getAllExpIdentifierList method :");
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
	public final VsacApiResult updateCQLVSACValueSets(List<CQLQualityDataSetDTO> appliedQDMList,
													  String defaultExpId,
													  String sessionId) {
		VsacApiResult result = new VsacApiResult();
		LOGGER.debug("Start VSACAPIServiceImpl updateCQLVSACValueSets method :");

		if (isAlreadySignedIn(sessionId)) {
			if (isCASTicketGrantingTicketValid(sessionId)) {
				HashMap<CQLQualityDataSetDTO, CQLQualityDataSetDTO> updateInMeasureXml = new HashMap<>();
				ArrayList<CQLQualityDataSetDTO> modifiedQDMList = new ArrayList<>();
				if (defaultExpId == null) {
					defaultExpId = getDefaultExpId();
				}
				for (CQLQualityDataSetDTO cqlQualityDataSetDTO : appliedQDMList) {
					CQLQualityDataSetDTO toBeModifiedQDM = cqlQualityDataSetDTO;
					LOGGER.debug(" VSACAPIServiceImpl updateCQLVSACValueSets :: OID:: " +
							cqlQualityDataSetDTO.getOid());
					// Filter out Timing Element , Expired, Birthdate, User defined QDM's and
					// supplemental data elements.
					if (ConstantMessages.TIMING_ELEMENT.equals(cqlQualityDataSetDTO.getDataType())
							|| ConstantMessages.USER_DEFINED_QDM_OID.equalsIgnoreCase(cqlQualityDataSetDTO.getOid())
							|| ConstantMessages.BIRTHDATE_OID.equals(cqlQualityDataSetDTO.getOid())
							|| ConstantMessages.DEAD_OID.equals(cqlQualityDataSetDTO.getOid())) {
						LOGGER.debug("VSACAPIServiceImpl updateCQLVSACValueSets :: QDM filtered as it is of either"
								+ "for following type Supplemental data or User defined or Timing Element.");
						if (ConstantMessages.USER_DEFINED_QDM_OID.equalsIgnoreCase(cqlQualityDataSetDTO.getOid())) {
							toBeModifiedQDM.setNotFoundInVSAC(true);
							toBeModifiedQDM.setHasModifiedAtVSAC(true);
							modifiedQDMList.add(toBeModifiedQDM);
							DataType qdmDataType = dataTypeDAO.findByDataTypeName(toBeModifiedQDM.getDataType());
							if ((qdmDataType == null) 
									|| ConstantMessages.PATIENT_CHARACTERISTIC_BIRTHDATE.equals(
											cqlQualityDataSetDTO.getDataType()) ||
									ConstantMessages.PATIENT_CHARACTERISTIC_EXPIRED.equals(
											cqlQualityDataSetDTO.getDataType())) {
								toBeModifiedQDM.setDataTypeHasRemoved(true);
							}
						}
						continue;
					} else {
						BasicResponse vsacResponseResult = null;
						try {
							String fiveMinuteServiceTicket = vsacService.getServiceTicket(
									UMLSSessionTicket.getTicket(sessionId).getTicket());
								if (StringUtils.isNotBlank(cqlQualityDataSetDTO.getRelease())) {
									vsacResponseResult = vsacService.getMultipleValueSetsResponseByOIDAndRelease(
											getOidFromUrl(cqlQualityDataSetDTO.getOid()),
											cqlQualityDataSetDTO.getRelease(),
											fiveMinuteServiceTicket);
								} else if (StringUtils.isNotBlank(cqlQualityDataSetDTO.getVersion())) {
									vsacResponseResult = vsacService.getMultipleValueSetsResponseByOIDAndVersion(
											getOidFromUrl(cqlQualityDataSetDTO.getOid()),
											cqlQualityDataSetDTO.getVersion(),
											fiveMinuteServiceTicket);
								} else {
									vsacResponseResult = vsacService.getMultipleValueSetsResponseByOID(
											getOidFromUrl(cqlQualityDataSetDTO.getOid()),
											fiveMinuteServiceTicket,
											defaultExpId);
								}
								
						} catch (Exception ex) {
							LOGGER.debug("VSACAPIServiceImpl updateCQLVSACValueSets :: Value Set reterival failed at "
									+ "VSAC for OID :" + cqlQualityDataSetDTO.getOid() + " with Data Type : "
									+ cqlQualityDataSetDTO.getDataType());
						}
						if (vsacResponseResult != null && vsacResponseResult.getXmlPayLoad() != null) {
							if (vsacResponseResult.isFailResponse()
									&& (vsacResponseResult.getFailReason() == VSAC_TIME_OUT_FAILURE_CODE)) {
								LOGGER.debug("Value Set reterival failed at VSAC for OID :"
										+ cqlQualityDataSetDTO.getOid() + " with Data Type : "
										+ cqlQualityDataSetDTO.getDataType() + ". Failure Reason: "
										+ vsacResponseResult.getFailReason());
								result.setSuccess(false);
								result.setFailureReason(vsacResponseResult.getFailReason());
								return result;
							}
							if (vsacResponseResult.getXmlPayLoad() != null &&
									StringUtils.isNotEmpty(vsacResponseResult.getXmlPayLoad())) {
								ValueSetWrapper wrapper = convertXmltoValueSet(vsacResponseResult.getXmlPayLoad());
								ValueSet ValueSet = wrapper.getValueSetList().get(0);
								if (ValueSet != null) {
									cqlQualityDataSetDTO.setName(ValueSet.getDisplayName());
									cqlQualityDataSetDTO.setOriginalCodeListName(ValueSet.getDisplayName());
									if(cqlQualityDataSetDTO.getSuffix() != null &&
											!cqlQualityDataSetDTO.getSuffix().isEmpty()){
										cqlQualityDataSetDTO.setName(ValueSet.getDisplayName()+
												" ("+cqlQualityDataSetDTO.getSuffix()+")");
									}
									if (ValueSet.isGrouping()) {
										cqlQualityDataSetDTO.setTaxonomy(ConstantMessages.GROUPING_CODE_SYSTEM);
									} else {
										if (ValueSet.getConceptList().getConceptList() != null) {
											cqlQualityDataSetDTO.setTaxonomy(ValueSet.getConceptList().
													getConceptList().get(0).getCodeSystemName());
										} else {
											cqlQualityDataSetDTO.setTaxonomy(StringUtils.EMPTY);
										}
									}
									cqlQualityDataSetDTO.setValueSetType(ValueSet.getType());
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
			LOGGER.debug("VSACAPIServiceImpl updateCQLVSACValueSets :: UMLS Login is required");
		}
		LOGGER.debug("End VSACAPIServiceImpl updateCQLVSACValueSets method :");
		return result;
	}
	
	public boolean isCASTicketGrantingTicketValid(String sessionId) {
		String fiveMinuteServiceTicket = vsacService.getServiceTicket(
				UMLSSessionTicket.getTicket(sessionId).getTicket());
		return StringUtils.isNotBlank(fiveMinuteServiceTicket);
	}
	
	/**
	 *Method to authenticate user at VSAC and save eightHourTicket into UMLSSessionMap for valid user.
	 *@param apiKey - String.
	 *@return Boolean.
	 * **/
	@Override
	public final boolean validateVsacUser(final String apiKey, String sessionId) {
		LOGGER.debug("Start VSACAPIServiceImpl validateVsacUser");
		String eightHourTicketForUser = vsacService.getTicketGrantingTicket(apiKey);
		VsacTicketInformation ticketInformation = new VsacTicketInformation(eightHourTicketForUser, new Date());
		UMLSSessionTicket.put(sessionId, ticketInformation);
		LOGGER.debug("End VSACAPIServiceImpl validateVsacUser: " + " Ticket issued for 8 hours: " +
				eightHourTicketForUser);
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
	 * Fetches a direct reference code from mat.vsac
	 * @param url the url to fetch
	 * @param sessionId the user's session id
	 * @return the result of the mat.vsac api call
	 */
	private VsacApiResult getCodeFromVsac(String url, String sessionId) {
		LOGGER.debug("Start VSACAPIServiceImpl getDirectReferenceCode method : url entered :" + url);
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
				String fiveMinServiceTicket = vsacService.getServiceTicket(eightHourTicket);
				if (StringUtils.isNotBlank(fiveMinServiceTicket)) {
					if(url.contains(":")){
						String[] arg = url.split(":");
						if(arg.length >0 && arg[1] != null) {
							url = arg[1];
							LOGGER.debug("VSACAPIServiceImpl getDirectReferenceCode method : " +
									"URL after dropping text before : is :: "+ url);
						}
					}
					BasicResponse vsacResponseResult = vsacService.getDirectReferenceCode(url, fiveMinServiceTicket);
					
					if(vsacResponseResult != null && vsacResponseResult.getXmlPayLoad() != null &&
							!StringUtils.isEmpty(vsacResponseResult.getXmlPayLoad())) {
						DirectReferenceCode referenceCode = convertXmltoDirectCodeRef(
								vsacResponseResult.getXmlPayLoad());
						result.setDirectReferenceCode(referenceCode);
						result.setSuccess(true);
					}
					
				} else {
					disconnectUMLSOnServiceTicketFailure(sessionId, result);
				}
			
			}  else {
				result.setSuccess(false);
				result.setFailureReason(VsacApiResult.CODE_URL_REQUIRED);
				LOGGER.debug("URL is required");
			}
		} else {
			result.setSuccess(false);
			result.setFailureReason(VsacApiResult.UMLS_NOT_LOGGEDIN);
			LOGGER.debug("UMLS Login is required");
		}
		
		LOGGER.debug("End VSACAPIServiceImpl getDirectReferenceCode method : url entered :" + url);
		return result;
	}
	
	@Override
	public final VsacApiResult getMostRecentValueSetByOID(final String oid,
														  final String release,
														  final String expansionId,
														  final String sessionId) {
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
			LOGGER.debug("VSACAPIServiceIMpl: Call to the VSAC API timed out." + e.getMessage());
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
	 * @return
	 */
	private final VsacApiResult getMostRecentValuesetByOID(final String oid,
														   final String release,
														   String expansionId,
														   final String sessionId) {
		LOGGER.debug("Start VSACAPIServiceImpl getValueSetBasedOIDAndVersion method : oid entered :" + oid
				+ "for Expansion Identifier :" + expansionId);
		VsacApiResult result = new VsacApiResult();
		String eightHourTicket = UMLSSessionTicket.getTicket(sessionId).getTicket();
		
		if (eightHourTicket != null) {
			if (StringUtils.isNotBlank(oid)) {
				String fiveMinServiceTicket = vsacService.getServiceTicket(eightHourTicket);
				
				if (StringUtils.isNotBlank(fiveMinServiceTicket)) {
					BasicResponse vsacResponseResult = null;
					
					if (StringUtils.isNotBlank(release)){					
						vsacResponseResult = vsacService.getMultipleValueSetsResponseByOIDAndRelease(oid.trim(),
								release,
								fiveMinServiceTicket);
					} else {
						if (StringUtils.isBlank(expansionId)) {
							expansionId = getDefaultExpId();
						}
						
						vsacResponseResult = vsacService.getMultipleValueSetsResponseByOID(oid.trim(),
								fiveMinServiceTicket,
								expansionId);
					}
									
					if (vsacResponseResult != null && StringUtils.isNotBlank(vsacResponseResult.getXmlPayLoad())) {
						result.setSuccess(true);
						ValueSetWrapper wrapper = convertXmltoValueSet(vsacResponseResult.getXmlPayLoad());
						result.setVsacResponse(wrapper.getValueSetList());
						LOGGER.debug("Successfully converted valueset object from mat.vsac xml payload.");
					} else {
						result.setSuccess(false);
						LOGGER.debug("Unable to retrieve value set in VSAC.");
					}
					
				} else { 
					disconnectUMLSOnServiceTicketFailure(sessionId, result);
				}
				
			} else {
				result.setSuccess(false);
				result.setFailureReason(VsacApiResult.OID_REQUIRED);
				LOGGER.debug("OID is required");
			}
		} else {
			result.setSuccess(false);
			result.setFailureReason(VsacApiResult.UMLS_NOT_LOGGEDIN);
			LOGGER.debug("UMLS Login is required");
		}
		LOGGER.debug("End VSACAPIServiceImpl getValueSetBasedOIDAndVersion method : oid entered :"
				+ oid + "for Expansion Identifier entered :" + expansionId);
		return result;
	}

	private VsacApiResult disconnectUMLSOnServiceTicketFailure(String sessionId, VsacApiResult result) {
		LOGGER.debug("VSACApiServImpl Ticket :" + UMLSSessionTicket.getTicket(sessionId).getTicket() +
				" has expired");
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

	@Override
	public VsacApiResult getVSACProgramsReleasesAndProfiles() {

		LOGGER.debug("Start VSACAPIServiceImpl getProgramsList method :");
		VsacApiResult result = new VsacApiResult();
		
		try {
			BasicResponse vsacResponseResult = vsacService.getAllPrograms();
			if(vsacResponseResult != null && vsacResponseResult.getPgmRels() != null) {				
				if (vsacResponseResult.isFailResponse() &&
						(vsacResponseResult.getFailReason() == VSAC_TIME_OUT_FAILURE_CODE)) {
					LOGGER.debug("Program List retrieval failed at VSAC with Failure Reason: " +
							vsacResponseResult.getFailReason());
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
		LOGGER.debug("Start VSACAPIServiceImpl getProgramsList method :");
		BasicResponse vsacResponseResult = null;
		try {
			vsacResponseResult = vsacService.getReleasesOfProgram(programName);
		} catch (Exception e) {
			LOGGER.error("getReleasesListForProgram: " + e.getMessage());
		}
		return vsacResponseResult != null ? vsacResponseResult.getPgmRels() : null;
	}
	
	private String getLatestProfileOfProgram(String programName) {
		LOGGER.debug("Start getProfilesForProgram method :");
		BasicResponse vsacResponseResult = null;
		try {
			vsacResponseResult = vsacService.getLatestProfileOfProgram(programName);
		} catch (Exception e) {
			LOGGER.error("getLatestProfileOfProgram: " + e.getMessage());
		}
		return vsacResponseResult != null ? vsacResponseResult.getXmlPayLoad() : null;
	}
}