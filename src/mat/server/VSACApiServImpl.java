package mat.server;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import mat.client.umls.service.VsacApiResult;
import mat.dao.DataTypeDAO;
import mat.model.DataType;
import mat.model.MatConcept;
import mat.model.MatValueSet;
import mat.model.QualityDataModelWrapper;
import mat.model.QualityDataSetDTO;
import mat.model.VSACExpansionIdentifierWrapper;
import mat.model.VSACValueSetWrapper;
import mat.model.VSACVersionWrapper;
import mat.model.cql.CQLCodeSystem;
import mat.server.service.MeasureLibraryService;
import mat.server.service.VSACApiService;
import mat.server.util.ResourceLoader;
import mat.server.util.UMLSSessionTicket;
import mat.shared.ConstantMessages;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.vsac.VSACGroovyClient;
import org.vsac.VSACResponseResult;
import org.xml.sax.InputSource;


public class VSACApiServImpl implements VSACApiService{
	/** Logger for VSACAPIServiceImpl class. **/
	private static final Log LOGGER = LogFactory.getLog(VSACAPIServiceImpl.class);
	@Autowired
	private ApplicationContext context;
	/** The Constant PROXY_HOST. */
	private String PROXY_HOST;
	/** The Constant PROXY_PORT. */
	private int PROXY_PORT;
	
	/** The server. */
	private String server;
	
	/** The service. */
	private String service;
	
	/** The retierive multi oids service. */
	private String retieriveMultiOIDSService;
	
	/** The profile service. */
	private String profileService;
	/** serialVersionUID for VSACAPIServiceImpl class. **/
	private static final long serialVersionUID = -6645961609626183169L;
	/** The Constant REQUEST_FAILURE_CODE. */
	private static final int VSAC_REQUEST_FAILURE_CODE = 4;
	/** The Constant TIME_OUT_FAILURE_CODE. */
	private static final int VSAC_TIME_OUT_FAILURE_CODE = 3;
	
	/** The v groovy client. */
	private VSACGroovyClient vGroovyClient;
	
	/** The version service. */
	private String versionService;
	
	/** The mat value set list. */
	private ArrayList<MatValueSet> matValueSetList;
	
	/** The update in measure xml. */
	private HashMap<QualityDataSetDTO, QualityDataSetDTO> updateInMeasureXml;
	
	/** The default exp id. */
	private String defaultExpId;
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
		vGroovyClient = new VSACGroovyClient(PROXY_HOST, PROXY_PORT, server,service,retieriveMultiOIDSService,profileService,versionService);
		//defaultExpId = getDefaultExpansionId();
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
			LOGGER.info("unmarshalling complete..RetrieveMultipleValueSetsResponse"
					+ details.getValueSetList().get(0).getDefinition());
			
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
	private VSACExpansionIdentifierWrapper convertXmlToProfileList(final String xmlPayLoad){
		LOGGER.info("Start VSACAPIServiceImpl convertXmlToProfileList");
		VSACExpansionIdentifierWrapper profileDetails = null;
		String xml = xmlPayLoad;
		if ((xml != null) && StringUtils.isNotBlank(xml)) {
			LOGGER.info("xml To reterive RetrieveVSACProfileListResponse tag is not null ");
		}
		try {
			Mapping mapping = new Mapping();
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("VSACExpIdentifierMapping.xml"));
			Unmarshaller unmar = new Unmarshaller(mapping);
			unmar.setClass(VSACExpansionIdentifierWrapper.class);
			unmar.setWhitespacePreserve(true);
			profileDetails = (VSACExpansionIdentifierWrapper) unmar.unmarshal(new InputSource(new StringReader(xml)));
			LOGGER.info("unmarshalling complete..RetrieveVSACProfileListResponse"
					+ profileDetails.getExpIdentifierList().get(0).getName());
			
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
				LOGGER.info("unmarshalling complete..RetrieveVSACVersionListResponse"
						+ versionDetails.getVersionList().get(0).getName());
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
	
	/** Method to Iterate through Map of Quality Data set DTO(modify With) as key and Quality Data Set DTO (modifiable) as Value and update
	 * Measure XML by calling {@link MeasureLibraryServiceImpl} method 'updateMeasureXML'.
	 * @param map - HaspMap
	 * @param measureId - String */
	private void updateAllInMeasureXml(HashMap<QualityDataSetDTO, QualityDataSetDTO> map, String measureId) {
		LOGGER.info("Start VSACAPIServiceImpl updateAllInMeasureXml :");
		Iterator<Entry<QualityDataSetDTO, QualityDataSetDTO>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<QualityDataSetDTO, QualityDataSetDTO> entrySet = it.next();
			LOGGER.info("Calling updateMeasureXML for : " + entrySet.getKey().getOid());
			getMeasureLibraryService().updateMeasureXML(entrySet.getKey(),
					entrySet.getValue(), measureId);
			LOGGER.info("Successfully updated Measure XML for  : " + entrySet.getKey().getOid());
		}
		LOGGER.info("End VSACAPIServiceImpl updateAllInMeasureXml :");
	}
	/***
	 * Method to update valueset's without versions from VSAC in Measure XML.
	 * Skip Timing elements, Expired, Birthdate and User defined QDM. Supplemental Data Elements are considered here.
	 *
	 * @param measureId - Selected Measure Id.
	 * @return VsacApiResult - Result.
	 * */
	@Override
	public final VsacApiResult updateAllVSACValueSetsAtPackage(final String measureId, String sessionId) {
		VsacApiResult result = new VsacApiResult();
		String defaultExpId = getMeasureLibraryService().getDefaultExpansionIdentifier(measureId);
		if (isAlreadySignedIn(sessionId)) {
			String eightHourTicket = UMLSSessionTicket.getTicket(sessionId);
			//ArrayList<QualityDataSetDTO> appliedQDMList = getMeasureLibraryService().
			//	getAppliedQDMFromMeasureXml(measureId, false);
			QualityDataModelWrapper details = getMeasureLibraryService().
					getAppliedQDMFromMeasureXml(measureId, false);
			List<QualityDataSetDTO> appliedQDMList = details.getQualityDataDTO();
			matValueSetList = new ArrayList<MatValueSet>();
			updateInMeasureXml = new HashMap<QualityDataSetDTO, QualityDataSetDTO>();
			List<String> notFoundOIDList = new ArrayList<String>();
			if(defaultExpId == null){
				defaultExpId = getDefaultExpId();
			}
			for (QualityDataSetDTO qualityDataSetDTO : appliedQDMList) {
				LOGGER.info("OID ====" + qualityDataSetDTO.getOid());
				// Filter out Timing Element, Expired, Birthdate and User defined QDM's.
				if (ConstantMessages.TIMING_ELEMENT.equals(qualityDataSetDTO.getDataType())
						|| ConstantMessages.USER_DEFINED_QDM_OID.equalsIgnoreCase(qualityDataSetDTO.getOid())
						|| ConstantMessages.BIRTHDATE_OID.equals(qualityDataSetDTO.getOid())
						|| ConstantMessages.EXPIRED_OID.equals(qualityDataSetDTO.getOid())) {
					LOGGER.info("QDM filtered as it is of either for following type "
							+ "(User defined or Timing Element.");
					if (ConstantMessages.USER_DEFINED_QDM_OID.equalsIgnoreCase(qualityDataSetDTO.getOid())) {
						// To show warning message on Measure Package screen added
						// User define oid to notFoundOidList.
						notFoundOIDList.add(qualityDataSetDTO.getOid());
					}
					continue;
				} else if ("1.0".equalsIgnoreCase(qualityDataSetDTO.getVersion())
						|| ("1".equalsIgnoreCase(qualityDataSetDTO.getVersion()))) {
					// Only Update Measure XML for those which have version as 1.0 or 1 and no expansionProfile.
					LOGGER.info("Start ValueSetsResponseDAO...Using Proxy:" + PROXY_HOST + ":" + PROXY_PORT);
					VSACResponseResult vsacResponseResult = null;
					try {
						String fiveMinuteServiceTicket = vGroovyClient.getServiceTicket(eightHourTicket);
						if (qualityDataSetDTO.getExpansionIdentifier() != null) {
							vsacResponseResult = vGroovyClient.getMultipleValueSetsResponseByOIDAndProfile(
									qualityDataSetDTO.getOid(), qualityDataSetDTO.getExpansionIdentifier(),
									fiveMinuteServiceTicket);
						} else {
							vsacResponseResult = vGroovyClient.getMultipleValueSetsResponseByOID(
									qualityDataSetDTO.getOid(), fiveMinuteServiceTicket, defaultExpId);
						}
					} catch (Exception ex) {
						LOGGER.info("Value Set reterival failed at VSAC for OID :"
								+ qualityDataSetDTO.getOid() + " with Data Type : "
								+ qualityDataSetDTO.getDataType());
					}
					if ((vsacResponseResult != null) && (vsacResponseResult.getXmlPayLoad() != null)) {
						LOGGER.info("Value Set from VSAC for OID :"
								+ qualityDataSetDTO.getOid() + " with Data Type : "
								+ qualityDataSetDTO.getDataType()
								+ ". Failure Reason:" + vsacResponseResult.getFailReason());
						if (vsacResponseResult.getIsFailResponse()) {
							int failReason = vsacResponseResult.getFailReason();
							if (failReason == VSAC_TIME_OUT_FAILURE_CODE) {
								LOGGER.info("Value Set reterival failed at VSAC for OID :"
										+ qualityDataSetDTO.getOid() + " with Data Type : "
										+ qualityDataSetDTO.getDataType()
										+ ". Failure Reason:" + vsacResponseResult.getFailReason());
								result.setSuccess(false);
								result.setFailureReason(vsacResponseResult.getFailReason());
								return result;
							} else if (failReason == VSAC_REQUEST_FAILURE_CODE) {
								notFoundOIDList.add(qualityDataSetDTO.getOid());
								continue;
							}
						}
						getUpdateMeasureXMLList(vsacResponseResult, qualityDataSetDTO, eightHourTicket, defaultExpId);
						
					}
				} else { // Add specific version other then 1.0 QDM in matValueSetList - Used for Value set sheet creation.
					LOGGER.info("Start ValueSetsResponseDAO...Using Proxy:" + PROXY_HOST + ":" + PROXY_PORT);
					VSACResponseResult responseResult = null;
					try {
						String fiveMinuteServiceTicket = vGroovyClient.getServiceTicket(eightHourTicket);
						responseResult = vGroovyClient.getMultipleValueSetsResponseByOIDAndVersion(qualityDataSetDTO.getOid(),
								qualityDataSetDTO.getVersion(), fiveMinuteServiceTicket);
					} catch (Exception ex) {
						LOGGER.info("Value Set reterival failed at VSAC for OID :" + qualityDataSetDTO.getOid()
								+ " with Data Type : " + qualityDataSetDTO.getDataType());
					}
					getUpdateMeasureXMLList(responseResult, qualityDataSetDTO, eightHourTicket, defaultExpId);
				}
			}
			// Call to update Measure XML.
			updateAllInMeasureXml(updateInMeasureXml, measureId);
			result.setSuccess(true);
			result.setVsacResponse(matValueSetList);
			LOGGER.info("OID's not found:"+notFoundOIDList);
			result.setRetrievalFailedOIDs(notFoundOIDList);
		} else {
			result.setSuccess(false);
			result.setFailureReason(result.UMLS_NOT_LOGGEDIN);
			LOGGER.info("UMLS Login is required");
		}
		return result;
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
			String fiveMinuteServiceTicket = vGroovyClient.getServiceTicket(
					UMLSSessionTicket.getTicket(sessionId)
					);
			VSACResponseResult vsacResponseResult = null;
			try {
				vsacResponseResult = vGroovyClient.getProfileList(fiveMinuteServiceTicket);
			} catch (Exception ex) {
				LOGGER.info("VSACAPIServiceImpl ExpIdentifierList failed in method :: getAllProfileList");
			}
			if ((vsacResponseResult != null) && (vsacResponseResult.getXmlPayLoad() != null)) {
				if (vsacResponseResult.getIsFailResponse()
						&& (vsacResponseResult.getFailReason() == VSAC_TIME_OUT_FAILURE_CODE)) {
					LOGGER.info("Expansion Identifier List reterival failed at VSAC with Failure Reason: "
							+ vsacResponseResult.getFailReason());
					result.setSuccess(false);
					result.setFailureReason(vsacResponseResult.getFailReason());
					return result;
				}
				if ((vsacResponseResult.getXmlPayLoad() != null)
						&& StringUtils.isNotEmpty(vsacResponseResult.getXmlPayLoad())) {
					// Caster conversion here.
					VSACExpansionIdentifierWrapper wrapper = convertXmlToProfileList(vsacResponseResult.getXmlPayLoad());
					result.setVsacExpIdentifierResp(wrapper.getExpIdentifierList());
					return result;
				}
			}
		} else {
			result.setSuccess(false);
			result.setFailureReason(result.UMLS_NOT_LOGGEDIN);
			LOGGER.info("VSACAPIServiceImpl getAllExpIdentifierList :: UMLS Login is required");
		}
		LOGGER.info("End VSACAPIServiceImpl getAllExpIdentifierList method :");
		return result;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.umls.service.VSACAPIService#getAllVersionListByOID(java.lang.String)
	 */
	@Override
	public final VsacApiResult getAllVersionListByOID(String oid, String sessionId) {
		VsacApiResult result = new VsacApiResult();
		LOGGER.info("Start VSACAPIServiceImpl getAllProfileList method :");
		if (isAlreadySignedIn(sessionId)) {
			String fiveMinuteServiceTicket = vGroovyClient.getServiceTicket(
					UMLSSessionTicket.getTicket(sessionId)
					);
			VSACResponseResult vsacResponseResult = null;
			try {
				vsacResponseResult = vGroovyClient.reteriveVersionListForOid(oid, fiveMinuteServiceTicket);
				//vsacResponseResult = vGroovyClient.reteriveVersionListForOid(oid, serviceTicket);
			} catch (Exception ex) {
				LOGGER.info("VSACAPIServiceImpl VersionList failed in method :: getAllVersionListByOID");
			}
			if ((vsacResponseResult != null) && (vsacResponseResult.getXmlPayLoad() != null)) {
				if (vsacResponseResult.getIsFailResponse()
						&& (vsacResponseResult.getFailReason() == VSAC_TIME_OUT_FAILURE_CODE)) {
					LOGGER.info("Version List reterival failed at VSAC with Failure Reason: "
							+ vsacResponseResult.getFailReason());
					result.setSuccess(false);
					result.setFailureReason(vsacResponseResult.getFailReason());
					return result;
				}
				if ((vsacResponseResult.getXmlPayLoad() != null)
						&& StringUtils.isNotEmpty(vsacResponseResult.getXmlPayLoad())) {
					// Caster conversion here.
					VSACVersionWrapper wrapper = convertXmlToVersionList(vsacResponseResult.getXmlPayLoad());
					result.setVsacVersionResp(wrapper.getVersionList());
					return result;
				}
			}
		} else {
			result.setSuccess(false);
			result.setFailureReason(result.UMLS_NOT_LOGGEDIN);
			LOGGER.info("VSACAPIServiceImpl getAllVersionListByOID :: UMLS Login is required");
		}
		LOGGER.info("End VSACAPIServiceImpl getAllVersionListByOID method :");
		return result;
	}
	
	
	/**
	 * *
	 * Method to update valueset's without versions from VSAC in Measure XML.
	 * Skip supplemental Data Elements and Timing elements, Expired, Birth date and User defined QDM.
	 *
	 * @param measureId            - Selected Measure Id.
	 * @param defaultExpId the default exp id
	 * @return VsacApiResult - Result.
	 */
	@Override
	public final VsacApiResult updateVSACValueSets(final String measureId, String defaultExpId, String sessionId) {
		VsacApiResult result = new VsacApiResult();
		LOGGER.info("Start VSACAPIServiceImpl updateVSACValueSets method :");
		if (isAlreadySignedIn(sessionId)) {
			QualityDataModelWrapper details = getMeasureLibraryService().
					getAppliedQDMFromMeasureXml(measureId, false);
			List<QualityDataSetDTO> appliedQDMList = details.getQualityDataDTO();
			HashMap<QualityDataSetDTO, QualityDataSetDTO> updateInMeasureXml =
					new HashMap<QualityDataSetDTO, QualityDataSetDTO>();
			ArrayList<QualityDataSetDTO> modifiedQDMList = new ArrayList<QualityDataSetDTO>();
			if (defaultExpId == null) {
				defaultExpId = getDefaultExpId();
			}
			for (QualityDataSetDTO qualityDataSetDTO : appliedQDMList) {
				QualityDataSetDTO toBeModifiedQDM = qualityDataSetDTO;
				LOGGER.info(" VSACAPIServiceImpl updateVSACValueSets :: OID:: " + qualityDataSetDTO.getOid());
				// Filter out Timing Element , Expired, Birthdate, User defined QDM's and
				// supplemental data elements.
				if (ConstantMessages.TIMING_ELEMENT.equals(qualityDataSetDTO.getDataType())
						|| ConstantMessages.USER_DEFINED_QDM_OID.equalsIgnoreCase(qualityDataSetDTO.getOid())
						|| qualityDataSetDTO.isSuppDataElement()
						|| ConstantMessages.BIRTHDATE_OID.equals(qualityDataSetDTO.getOid())
						|| ConstantMessages.EXPIRED_OID.equals(qualityDataSetDTO.getOid())) {
					LOGGER.info("VSACAPIServiceImpl updateVSACValueSets :: QDM filtered as it is of either"
							+ "for following type Supplemental data or User defined or Timing Element.");
					if (ConstantMessages.USER_DEFINED_QDM_OID.equalsIgnoreCase(qualityDataSetDTO.getOid())) {
						toBeModifiedQDM.setNotFoundInVSAC(true);
						toBeModifiedQDM.setHasModifiedAtVSAC(true);
						modifiedQDMList.add(toBeModifiedQDM);
						DataType qdmDataType = getDataTypeDAO().findByDataTypeName(toBeModifiedQDM.getDataType());
						if ((qdmDataType == null) || ConstantMessages.PATIENT_CHARACTERISTIC_BIRTHDATE.equals(
								qualityDataSetDTO.getDataType()) || ConstantMessages.
								PATIENT_CHARACTERISTIC_EXPIRED.equals(qualityDataSetDTO.getDataType())) {
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
						if(qualityDataSetDTO.getExpansionIdentifier() != null){
							vsacResponseResult = vGroovyClient.getMultipleValueSetsResponseByOIDAndProfile(qualityDataSetDTO.getOid(),
									qualityDataSetDTO.getExpansionIdentifier(), fiveMinuteServiceTicket);
						} else  {
							if (!(qualityDataSetDTO.getVersion().equals("1.0")
									|| qualityDataSetDTO.getVersion().equals("1"))) {
								vsacResponseResult = vGroovyClient.
										getMultipleValueSetsResponseByOIDAndVersion(qualityDataSetDTO.getOid(),
												qualityDataSetDTO.getVersion(), fiveMinuteServiceTicket);
							} else {
								vsacResponseResult = vGroovyClient.getMultipleValueSetsResponseByOID(
										qualityDataSetDTO.getOid(), fiveMinuteServiceTicket, defaultExpId);
							}
						}
					} catch (Exception ex) {
						LOGGER.info("VSACAPIServiceImpl updateVSACValueSets :: Value Set reterival failed at "
								+ "VSAC for OID :" + qualityDataSetDTO.getOid() + " with Data Type : "
								+ qualityDataSetDTO.getDataType());
					}
					if ((vsacResponseResult != null) && (vsacResponseResult.getXmlPayLoad() != null)) {
						if (vsacResponseResult.getIsFailResponse()
								&& (vsacResponseResult.getFailReason() == VSAC_TIME_OUT_FAILURE_CODE)) {
							LOGGER.info("Value Set reterival failed at VSAC for OID :"
									+ qualityDataSetDTO.getOid() + " with Data Type : "
									+ qualityDataSetDTO.getDataType() + ". Failure Reason: "
									+ vsacResponseResult.getFailReason());
							// inValidateVsacUser();
							// MatContext.get().setUMLSLoggedIn(false);
							result.setSuccess(false);
							result.setFailureReason(vsacResponseResult.getFailReason());
							return result;
						}
						if ((vsacResponseResult.getXmlPayLoad() != null)
								&& StringUtils.isNotEmpty(vsacResponseResult.getXmlPayLoad())) {
							VSACValueSetWrapper wrapper = convertXmltoValueSet(vsacResponseResult.getXmlPayLoad());
							MatValueSet matValueSet = wrapper.getValueSetList().get(0);
							if (matValueSet != null) {
								qualityDataSetDTO.setCodeListName(matValueSet.getDisplayName());
								if (matValueSet.isGrouping()) {
									qualityDataSetDTO.setTaxonomy(ConstantMessages.
											GROUPING_CODE_SYSTEM);
								} else {
									if (matValueSet.getConceptList().getConceptList() != null) {
										qualityDataSetDTO.setTaxonomy(matValueSet.getConceptList().
												getConceptList().get(0).getCodeSystemName());
									} else {
										qualityDataSetDTO.setTaxonomy(StringUtils.EMPTY);
									}
								}
								getUniqueCodeSystems(wrapper);
								qualityDataSetDTO.setCodeSystemList(getCodeSystemList(wrapper.getValueSetList().get(0)
										.getConceptList().getConceptList(), qualityDataSetDTO.getOid()));
								updateInMeasureXml.put(qualityDataSetDTO, toBeModifiedQDM);
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
				//to validate removed DataTypes in Applied QDM ELements
				DataType qdmDataType = getDataTypeDAO().findByDataTypeName(toBeModifiedQDM.getDataType());
				if((qdmDataType == null) || ConstantMessages.PATIENT_CHARACTERISTIC_BIRTHDATE.equals(qualityDataSetDTO.getDataType())
						|| ConstantMessages.PATIENT_CHARACTERISTIC_EXPIRED.equals(qualityDataSetDTO.getDataType())){
					toBeModifiedQDM.setDataTypeHasRemoved(true);
				}
				modifiedQDMList.add(toBeModifiedQDM);
			}
			updateAllInMeasureXml(updateInMeasureXml, measureId);
			result.setSuccess(true);
			result.setUpdatedQualityDataDTOLIst(modifiedQDMList);
		} else {
			result.setSuccess(false);
			result.setFailureReason(result.UMLS_NOT_LOGGEDIN);
			LOGGER.info("VSACAPIServiceImpl updateVSACValueSets :: UMLS Login is required");
		}
		LOGGER.info("End VSACAPIServiceImpl updateVSACValueSets method :");
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
		//String eightHourTicketForUser = new VSACTicketService(PROXY_HOST, PROXY_PORT).getTicketGrantingTicket(userName, password);
		String eightHourTicketForUser = vGroovyClient.getTicketGrantingTicket(userName, password);
		UMLSSessionTicket.put(sessionId, eightHourTicketForUser);
		LOGGER.info("End VSACAPIServiceImpl validateVsacUser: " + " Ticket issued for 8 hours: " + eightHourTicketForUser);
		return eightHourTicketForUser != null;
	}
	
	/**
	 * Gets the data type dao.
	 *
	 * @return the data type dao
	 */
	private DataTypeDAO getDataTypeDAO(){
		return (DataTypeDAO)context.getBean("dataTypeDAO");
	}
	@Override
	public final VsacApiResult getValueSetByOIDAndVersionOrExpansionId (final String oid, final String version,
			final String expansionId, String sessionId) {
		LOGGER.info("Start VSACAPIServiceImpl getValueSetBasedOIDAndVersion method : oid entered :" + oid + "for version entered :"
				+ version +" or Effective Date :" + expansionId);
		VsacApiResult result = new VsacApiResult();
		String eightHourTicket = UMLSSessionTicket.getTicket(sessionId);
		if (eightHourTicket != null) {
			if ((oid != null) && StringUtils.isNotEmpty(oid) && StringUtils.isNotBlank(oid)) {
				LOGGER.info("Start ValueSetsResponseDAO...Using Proxy:" + PROXY_HOST + ":" + PROXY_PORT);
				String fiveMinServiceTicket = vGroovyClient.getServiceTicket(eightHourTicket);
				VSACResponseResult vsacResponseResult = null;
				if ((version != null) && StringUtils.isNotEmpty(version)) {
					vsacResponseResult = vGroovyClient.
							getMultipleValueSetsResponseByOIDAndVersion(oid.trim(),version,fiveMinServiceTicket);
				} else if ((expansionId != null) && StringUtils.isNotEmpty(expansionId)) {
					vsacResponseResult = vGroovyClient.
							getMultipleValueSetsResponseByOIDAndProfile(oid, expansionId,fiveMinServiceTicket);
				} else {
					vsacResponseResult = vGroovyClient.getMultipleValueSetsResponseByOID(oid.trim(),
							fiveMinServiceTicket, expansionId);
				}
				if((vsacResponseResult != null) && (vsacResponseResult.getXmlPayLoad() != null)
						&& (!StringUtils.isEmpty(vsacResponseResult.getXmlPayLoad()))) {
					result.setSuccess(true);
					VSACValueSetWrapper wrapper = convertXmltoValueSet(vsacResponseResult.getXmlPayLoad());
					for (MatValueSet valueSet : wrapper.getValueSetList()) {
						handleVSACGroupedValueSet(eightHourTicket, valueSet, expansionId);
					}
					result.setVsacResponse(wrapper.getValueSetList());
					LOGGER.info("Successfully converted valueset object from vsac xml payload.");
				} else {
					result.setSuccess(false);
					LOGGER.info("Unable to reterive value set in VSAC.");
				}
			} else {
				result.setSuccess(false);
				result.setFailureReason(result.OID_REQUIRED);
				LOGGER.info("OID is required");
			}
		} else {
			result.setSuccess(false);
			result.setFailureReason(result.UMLS_NOT_LOGGEDIN);
			LOGGER.info("UMLS Login is required");
		}
		LOGGER.info("End VSACAPIServiceImpl getValueSetBasedOIDAndVersion method : oid entered :"
				+ oid + "for version entered :" + version);
		return result;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.umls.service.VSACAPIService#getMostRecentValueSetByOID(java.lang.String, java.lang.String)
	 */
	@Override
	public final VsacApiResult getMostRecentValueSetByOID(final String oid, String expansionId, String sessionId) {
		LOGGER.info("Start VSACAPIServiceImpl getValueSetBasedOIDAndVersion method : oid entered :" + oid
				+ "for Expansion Identifier :" + expansionId);
		VsacApiResult result = new VsacApiResult();
		String eightHourTicket = UMLSSessionTicket.getTicket(sessionId);
		
		if(expansionId == null){
			expansionId = getDefaultExpId();
		}
		if (eightHourTicket != null) {
			if ((oid != null) && StringUtils.isNotEmpty(oid) && StringUtils.isNotBlank(oid)) {
				LOGGER.info("Start ValueSetsResponseDAO...Using Proxy:" + PROXY_HOST + ":" + PROXY_PORT);
				String fiveMinServiceTicket = vGroovyClient.getServiceTicket(eightHourTicket);
				VSACResponseResult vsacResponseResult = null;
				
				vsacResponseResult = vGroovyClient.getMultipleValueSetsResponseByOID(oid.trim(),fiveMinServiceTicket,
						expansionId);
				
				if((vsacResponseResult != null) && (vsacResponseResult.getXmlPayLoad() != null)
						&& (!StringUtils.isEmpty(vsacResponseResult.getXmlPayLoad()))) {
					result.setSuccess(true);
					VSACValueSetWrapper wrapper = convertXmltoValueSet(vsacResponseResult.getXmlPayLoad());
					getUniqueCodeSystems(wrapper);
					result.setVsacResponse(wrapper.getValueSetList());
					LOGGER.info("Successfully converted valueset object from vsac xml payload.");
				} else {
					result.setSuccess(false);
					LOGGER.info("Unable to reterive value set in VSAC.");
				}
			} else {
				result.setSuccess(false);
				result.setFailureReason(result.OID_REQUIRED);
				LOGGER.info("OID is required");
			}
		} else {
			result.setSuccess(false);
			result.setFailureReason(result.UMLS_NOT_LOGGEDIN);
			LOGGER.info("UMLS Login is required");
		}
		LOGGER.info("End VSACAPIServiceImpl getValueSetBasedOIDAndVersion method : oid entered :"
				+ oid + "for Expansion Idnetifier entered :" + expansionId);
		return result;
	}
	
	/**
	 * Gets the update measure xml list.
	 *
	 * @param vsacResponseResult the vsac response result
	 * @param qualityDataSetDTO the quality data set dto
	 * @param eightHourTicket the eight hour ticket
	 * @param defaultExpId the default exp id
	 * @return the update measure xml list
	 */
	private void getUpdateMeasureXMLList(VSACResponseResult vsacResponseResult, QualityDataSetDTO qualityDataSetDTO,
			String eightHourTicket, String defaultExpId){
		if ((vsacResponseResult.getXmlPayLoad() != null)
				&& StringUtils.isNotEmpty(vsacResponseResult.getXmlPayLoad())) {
			VSACValueSetWrapper wrapper = convertXmltoValueSet(
					vsacResponseResult.getXmlPayLoad());
			MatValueSet matValueSet = wrapper.getValueSetList().get(0);
			QualityDataSetDTO toBeModifiedQDM = qualityDataSetDTO;
			if (matValueSet != null) {
				matValueSet.setQdmId(qualityDataSetDTO.getId());
				qualityDataSetDTO.setCodeListName(matValueSet.getDisplayName());
				if(qualityDataSetDTO.getVersion().equals("1.0")
						|| qualityDataSetDTO.getVersion().equals("1")){
					matValueSet.setVersion("Draft"); // If expansion Profile is used or most recent search is done , version should not show up in value set sheet.
				} else {
					matValueSet.setVersion(qualityDataSetDTO.getVersion());
				}
				
				if (qualityDataSetDTO.getExpansionIdentifier() != null) {
					matValueSet.setExpansionProfile(qualityDataSetDTO.
							getExpansionIdentifier());
				}
				if (matValueSet.isGrouping()) {
					qualityDataSetDTO.setTaxonomy(ConstantMessages.
							GROUPING_CODE_SYSTEM);
					handleVSACGroupedValueSet(eightHourTicket, matValueSet, defaultExpId);
					if (matValueSet.getGroupedValueSet().size() != 0) {
						matValueSetList.add(matValueSet);
					}
				} else {
					if (matValueSet.getConceptList().getConceptList() != null) {
						qualityDataSetDTO.setTaxonomy(matValueSet.getConceptList().
								getConceptList().get(0).
								getCodeSystemName());
						matValueSetList.add(matValueSet);
					} else {
						qualityDataSetDTO.setTaxonomy(StringUtils.EMPTY);
					}
				}
				getUniqueCodeSystems(wrapper);
				qualityDataSetDTO.setCodeSystemList(getCodeSystemList(wrapper.getValueSetList().get(0)
						.getConceptList().getConceptList(), qualityDataSetDTO.getOid()));
				updateInMeasureXml.put(qualityDataSetDTO, toBeModifiedQDM);
			}
		}
	}
	/**
	 * Private method to Handle Grouped type Value set.
	 * @param eightHourTicket
	 *            - String.
	 * @param valueSet
	 *            - MatValueSet.
	 * @param defaultExpId TODO
	 */
	private void handleVSACGroupedValueSet(final String eightHourTicket, final MatValueSet valueSet, String defaultExpId) {
		if (!valueSet.isGrouping()) {
			return;
		}
		valueSet.setGroupedValueSet(new ArrayList<MatValueSet>());
		String definitation = valueSet.getDefinition();
		if ((definitation != null) && StringUtils.isNotBlank(definitation)) {
			// Definition format is
			// (oid:SourceName),(oid:sourceName).
			// Below code is removing '(' ')' and splitting
			// on ':' to find oid's.
			definitation = definitation.replace("(", "");
			definitation = definitation.replace(")", "");
			String[] newDefinitation = definitation.split(",");
			for (String element : newDefinitation) {
				String[] groupedValueSetOid = element.split(":");
				// If Check To avoid junk data.
				if (groupedValueSetOid.length == 2) {
					LOGGER.info("Start ValueSetsResponseDAO...Using Proxy:"+PROXY_HOST+":"+PROXY_PORT);
					String fiveMinServiceTicket = vGroovyClient.getServiceTicket(eightHourTicket);
					VSACResponseResult vsacResponseResult = vGroovyClient.
							getMultipleValueSetsResponseByOID(groupedValueSetOid[0].trim(),fiveMinServiceTicket,
									defaultExpId);
					
					if(vsacResponseResult != null) {
						
						VSACValueSetWrapper wrapperGrouped = convertXmltoValueSet(vsacResponseResult.getXmlPayLoad());
						MatValueSet valueSetGrouping = wrapperGrouped.getValueSetList().get(0);
						valueSetGrouping.setVersion(valueSet.getVersion()); 
						valueSetGrouping.setExpansionProfile(valueSet.getExpansionProfile());
						valueSet.getGroupedValueSet().add(valueSetGrouping);
					}
				}
			}
		}
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
	
	/**
	 * Gets the default expansion id from Mat.properties file
	 *
	 * @return the default expansion id
	 */
	/*private String getDefaultExpansionId(){
		String defaultExpansionId = null;
		Properties prop = new Properties();
		InputStream input = null;
		try {
			
			String filename = "Mat.properties";
			input = VSACAPIServiceImpl.class.getClassLoader().getResourceAsStream(filename);
			if(input == null){
				System.out.println("Could'nt find the file " + filename);
				return null;
			}
			
			//load a properties file from class path, inside static method
			prop.load(input);
			
			defaultExpansionId = prop.getProperty("mat.qdm.default.expansion.id");
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally{
			if(input!=null){
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		return defaultExpansionId;
	}*/
	/**
	 * MeasureLibrary Service Object.
	 * @return MeasureLibraryService.
	 * */
	public final MeasureLibraryService getMeasureLibraryService() {
		return (MeasureLibraryService) context.getBean("measureLibraryService");
	}
	
	private void getUniqueCodeSystems(VSACValueSetWrapper wrapper) {

		List<String> uniqueList = new ArrayList<String>();
		List<MatConcept> conceptList = wrapper.getValueSetList().get(0).getConceptList().getConceptList();
		List<MatConcept> uniqueConceptList = new ArrayList<MatConcept>();
		String codesystemStr = null;
		for (int i = 0; i < conceptList.size(); i++) {
			codesystemStr = conceptList.get(i).getCodeSystemName() + ":" + conceptList.get(i).getCodeSystemVersion();
			if(!uniqueList.contains(codesystemStr)){
				uniqueList.add(codesystemStr);
				uniqueConceptList.add(conceptList.get(i));
			}
		}
		wrapper.getValueSetList().get(0).getConceptList().getConceptList().clear();
		wrapper.getValueSetList().get(0).getConceptList().getConceptList().addAll(uniqueConceptList);
	}
	
	private List<CQLCodeSystem> getCodeSystemList(List<MatConcept> conceptList, String oid){
		List<CQLCodeSystem> codeSystemList = new ArrayList<CQLCodeSystem>();
		for(int i=0; i<conceptList.size(); i++ ){
			CQLCodeSystem codeSystem = new CQLCodeSystem();
			codeSystem.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			codeSystem.setCodeSystem(conceptList.get(i).getCodeSystem());
			codeSystem.setCodeSystemName(conceptList.get(i).getCodeSystemName());
			codeSystem.setCodeSystemVersion(conceptList.get(i).getCodeSystemVersion());
			codeSystem.setValueSetOID(oid);
			codeSystemList.add(codeSystem);
		}
		return codeSystemList;
	}
	
}
