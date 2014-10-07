package mat.server;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import mat.client.umls.service.VSACAPIService;
import mat.client.umls.service.VsacApiResult;
import mat.dao.DataTypeDAO;
import mat.model.DataType;
import mat.model.MatValueSet;
import mat.model.QualityDataSetDTO;
import mat.model.VSACValueSetWrapper;
import mat.server.service.MeasureLibraryService;
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
import org.vsac.VSACGroovyClient;
import org.vsac.VSACResponseResult;
import org.xml.sax.InputSource;

// TODO: Auto-generated Javadoc
/** VSACAPIServiceImpl class. **/
@SuppressWarnings("static-access")
public class VSACAPIServiceImpl extends SpringRemoteServiceServlet implements VSACAPIService {
	/** Logger for VSACAPIServiceImpl class. **/
	private static final Log LOGGER = LogFactory.getLog(VSACAPIServiceImpl.class);
	/** The Constant PROXY_HOST. */
	private String PROXY_HOST;
	/** The Constant PROXY_PORT. */
	private int PROXY_PORT;
	
	private String server;
	private String service;
	private String retieriveMultiOIDSService;
	
	/** serialVersionUID for VSACAPIServiceImpl class. **/
	private static final long serialVersionUID = -6645961609626183169L;
	/** The Constant REQUEST_FAILURE_CODE. */
	private static final int VSAC_REQUEST_FAILURE_CODE = 4;
	/** The Constant TIME_OUT_FAILURE_CODE. */
	private static final int VSAC_TIME_OUT_FAILURE_CODE = 3;
	
	private VSACGroovyClient vGroovyClient;
	
	public VSACAPIServiceImpl(){
		PROXY_HOST = System.getProperty("vsac_proxy_host");
		if(PROXY_HOST !=null) {
			PROXY_PORT = Integer.parseInt(System.getProperty("vsac_proxy_port"));
		}
		server = System.getProperty("SERVER_TICKET_URL");
		service = System.getProperty("SERVICE_URL");
		retieriveMultiOIDSService = System.getProperty("SERVER_MULTIPLE_VALUESET_URL");
		vGroovyClient = new VSACGroovyClient(PROXY_HOST, PROXY_PORT, server,service,retieriveMultiOIDSService);
	}
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
	 * MeasureLibrary Service Object.
	 * @return MeasureLibraryService.
	 * */
	public final MeasureLibraryService getMeasureLibraryService() {
		return (MeasureLibraryService) context.getBean("measureLibraryService");
	}
	
	
	/**
	 * Method to retrieve value set from VSAC based on OID and version.
	 * @param oid
	 *            - String.
	 * @param version
	 *            - String.
	 * @param effectiveDate
	 *            - String.
	 * @return VsacApiResult - VsacApiResult.
	 * **/
	@Override
	public final VsacApiResult getValueSetByOIDAndVersionOrEffectiveDate(final String oid, final String version,
			final String effectiveDate) {
		LOGGER.info("Start VSACAPIServiceImpl getValueSetBasedOIDAndVersion method : oid entered :" + oid + "for version entered :"
				+ version +" or Effective Date :" + effectiveDate);
		VsacApiResult result = new VsacApiResult();
		String eightHourTicket = UMLSSessionTicket.getTicket(getThreadLocalRequest().getSession().getId());
		if (eightHourTicket != null) {
			if ((oid != null) && StringUtils.isNotEmpty(oid) && StringUtils.isNotBlank(oid)) {
				LOGGER.info("Start ValueSetsResponseDAO...Using Proxy:" + PROXY_HOST + ":" + PROXY_PORT);
				String fiveMinServiceTicket = vGroovyClient.getServiceTicket(eightHourTicket);
				VSACResponseResult vsacResponseResult = null;
				if ((version != null) && StringUtils.isNotEmpty(version)) {
					vsacResponseResult = vGroovyClient.
							getMultipleValueSetsResponseByOIDAndVersion(oid.trim(),version,fiveMinServiceTicket);
				} else if ((effectiveDate != null) && StringUtils.isNotEmpty(effectiveDate)) {
					vsacResponseResult = vGroovyClient.getMultipleValueSetsResponseByOIDAndEffectiveDate(oid, effectiveDate,fiveMinServiceTicket);
				} else {
					vsacResponseResult = vGroovyClient.getMultipleValueSetsResponseByOID(oid.trim(),fiveMinServiceTicket);
				}
				if(!StringUtils.isEmpty(vsacResponseResult.getXmlPayLoad())) {
					result.setSuccess(true);
					VSACValueSetWrapper wrapper = convertXmltoValueSet(vsacResponseResult.getXmlPayLoad());
					for (MatValueSet valueSet : wrapper.getValueSetList()) {
						handleVSACGroupedValueSet(eightHourTicket, valueSet);
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
	/**
	 * Private method to Handle Grouped type Value set.
	 * @param eightHourTicket
	 *            - String.
	 * @param valueSet
	 *            - MatValueSet.
	 */
	private void handleVSACGroupedValueSet(final String eightHourTicket, final MatValueSet valueSet) {
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
							getMultipleValueSetsResponseByOID(groupedValueSetOid[0].trim(),fiveMinServiceTicket);
					VSACValueSetWrapper wrapperGrouped = convertXmltoValueSet(vsacResponseResult.getXmlPayLoad());
					valueSet.getGroupedValueSet().add(wrapperGrouped.getValueSetList().get(0));
				}
			}
		}
	}
	/**
	 *Method to invalidate VSAC user session by removing HTTP session Id from UMLSSessionMap.
	 * **/
	@Override
	public final void inValidateVsacUser() {
		LOGGER.info("Start VSACAPIServiceImpl inValidateVsacUser");
		UMLSSessionTicket.remove(getThreadLocalRequest().getSession().getId());
		LOGGER.info("End VSACAPIServiceImpl inValidateVsacUser");
	}
	/**
	 *Method to check if User already signed in at VSAC.
	 *@return Boolean.
	 ***/
	@Override
	public final boolean isAlreadySignedIn() {
		LOGGER.info("Start VSACAPIServiceImpl isAlreadySignedIn");
		String eightHourTicketForUser = UMLSSessionTicket.getTicket(getThreadLocalRequest().getSession().getId());
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
	public final VsacApiResult updateAllVSACValueSetsAtPackage(final String measureId) {
		VsacApiResult result = new VsacApiResult();
		if (isAlreadySignedIn()) {
			String eightHourTicket = UMLSSessionTicket.getTicket(getThreadLocalRequest().getSession().getId());
			ArrayList<QualityDataSetDTO> appliedQDMList = getMeasureLibraryService().
					getAppliedQDMFromMeasureXml(measureId, false);
			ArrayList<MatValueSet> matValueSetList = new ArrayList<MatValueSet>();
			HashMap<QualityDataSetDTO, QualityDataSetDTO> updateInMeasureXml =
					new HashMap<QualityDataSetDTO, QualityDataSetDTO>();
			List<String> notFoundOIDList = new ArrayList<String>();
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
						|| "1".equalsIgnoreCase(qualityDataSetDTO.getVersion())) {
					// Only Update Measure XML for those which have version as 1.0 or 1.
					LOGGER.info("Start ValueSetsResponseDAO...Using Proxy:" + PROXY_HOST + ":" + PROXY_PORT);
					VSACResponseResult vsacResponseResult = null;
					try {
						String fiveMinuteServiceTicket = vGroovyClient.getServiceTicket(eightHourTicket);
						vsacResponseResult = vGroovyClient.getMultipleValueSetsResponseByOID(
								qualityDataSetDTO.getOid(), fiveMinuteServiceTicket);
					} catch (Exception ex) {
						LOGGER.info("Value Set reterival failed at VSAC for OID :"
								+ qualityDataSetDTO.getOid() + " with Data Type : "
								+ qualityDataSetDTO.getDataType());
					}
					if (vsacResponseResult.getXmlPayLoad() != null) {
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
						if ((vsacResponseResult.getXmlPayLoad() != null)
								&& StringUtils.isNotEmpty(vsacResponseResult.getXmlPayLoad())) {
							VSACValueSetWrapper wrapper = convertXmltoValueSet(
									vsacResponseResult.getXmlPayLoad());
							MatValueSet matValueSet = wrapper.getValueSetList().get(0);
							QualityDataSetDTO toBeModifiedQDM = qualityDataSetDTO;
							if (matValueSet != null) {
								matValueSet.setQdmId(qualityDataSetDTO.getId());
								qualityDataSetDTO.setCodeListName(matValueSet.getDisplayName());
								if (matValueSet.isGrouping()) {
									qualityDataSetDTO.setTaxonomy(ConstantMessages.
											GROUPING_CODE_SYSTEM);
									handleVSACGroupedValueSet(eightHourTicket, matValueSet);
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
								updateInMeasureXml.put(qualityDataSetDTO, toBeModifiedQDM);
							}
						}
					}
				} else { // Add specific version other then 1.0 QDM in matValueSetList - Used for Value set sheet creation.
					LOGGER.info("Start ValueSetsResponseDAO...Using Proxy:" + PROXY_HOST + ":" + PROXY_PORT);
					VSACResponseResult responseResult = null;
					try {
						String fiveMinuteServiceTicket = vGroovyClient.getServiceTicket(eightHourTicket);
						responseResult = vGroovyClient.getMultipleValueSetsResponseByOIDAndVersion( qualityDataSetDTO.getOid(),
								qualityDataSetDTO.getVersion(),fiveMinuteServiceTicket);
					} catch (Exception ex) {
						LOGGER.info("Value Set reterival failed at VSAC for OID :" + qualityDataSetDTO.getOid()
								+ " with Data Type : " + qualityDataSetDTO.getDataType());
					}
					if (responseResult.getXmlPayLoad() != null) {
						if ((responseResult.getXmlPayLoad() != null) && StringUtils.isNotEmpty(responseResult.getXmlPayLoad())) {
							VSACValueSetWrapper wrapper = convertXmltoValueSet(responseResult.getXmlPayLoad());
							MatValueSet matValueSet = wrapper.getValueSetList().get(0);
							if (matValueSet != null) {
								matValueSet.setQdmId(qualityDataSetDTO.getId());
								handleVSACGroupedValueSet(eightHourTicket, matValueSet);
								if (matValueSet.isGrouping()
										&& (matValueSet.getGroupedValueSet().size() != 0)) {
									matValueSetList.add(matValueSet);
								} else if (matValueSet.getConceptList().getConceptList() != null) {
									matValueSetList.add(matValueSet);
								}
							}
						}
					}
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
	/***
	 * Method to update valueset's without versions from VSAC in Measure XML.
	 * Skip supplemental Data Elements and Timing elements, Expired, Birthdate and User defined QDM.
	 *
	 * @param measureId
	 *            - Selected Measure Id.
	 * @return VsacApiResult - Result.
	 * */
	@Override
	public final VsacApiResult updateVSACValueSets(final String measureId) {
		VsacApiResult result = new VsacApiResult();
		LOGGER.info("Start VSACAPIServiceImpl updateVSACValueSets method :");
		if (isAlreadySignedIn()) {
			ArrayList<QualityDataSetDTO> appliedQDMList = getMeasureLibraryService().
					getAppliedQDMFromMeasureXml(measureId, false);
			HashMap<QualityDataSetDTO, QualityDataSetDTO> updateInMeasureXml =
					new HashMap<QualityDataSetDTO, QualityDataSetDTO>();
			ArrayList<QualityDataSetDTO> modifiedQDMList = new ArrayList<QualityDataSetDTO>();
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
				} else if ("1.0".equalsIgnoreCase(qualityDataSetDTO.getVersion())
						|| "1".equalsIgnoreCase(qualityDataSetDTO.getVersion())) {
					LOGGER.info("Start ValueSetsResponseDAO...Using Proxy:" + PROXY_HOST + ":" + PROXY_PORT);
					VSACResponseResult vsacResponseResult = null;
					try {
						String fiveMinuteServiceTicket = vGroovyClient.getServiceTicket(
								UMLSSessionTicket.getTicket(getThreadLocalRequest().getSession().getId())
								);
						vsacResponseResult = vGroovyClient.getMultipleValueSetsResponseByOID(
								qualityDataSetDTO.getOid(), fiveMinuteServiceTicket);
					} catch (Exception ex) {
						LOGGER.info("VSACAPIServiceImpl updateVSACValueSets :: Value Set reterival failed at "
								+ "VSAC for OID :" + qualityDataSetDTO.getOid() + " with Data Type : "
								+ qualityDataSetDTO.getDataType());
					}
					if (vsacResponseResult.getXmlPayLoad() != null) {
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
	public final boolean validateVsacUser(final String userName, final String password) {
		LOGGER.info("Start VSACAPIServiceImpl validateVsacUser");
		//String eightHourTicketForUser = new VSACTicketService(PROXY_HOST, PROXY_PORT).getTicketGrantingTicket(userName, password);
		String eightHourTicketForUser = vGroovyClient.getTicketGrantingTicket(userName, password);
		UMLSSessionTicket.put(getThreadLocalRequest().getSession().getId(), eightHourTicketForUser);
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
}
