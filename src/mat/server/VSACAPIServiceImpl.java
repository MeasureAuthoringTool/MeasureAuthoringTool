package mat.server;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import mat.client.umls.service.VSACAPIService;
import mat.client.umls.service.VsacApiResult;
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
import org.telligen.vsac.dao.ValueSetsResponseDAO;
import org.telligen.vsac.object.ValueSetsResponse;
import org.telligen.vsac.service.VSACTicketService;
import org.xml.sax.InputSource;

/** VSACAPIServiceImpl class. **/
@SuppressWarnings("static-access")
public class VSACAPIServiceImpl extends SpringRemoteServiceServlet implements VSACAPIService {
	/** Logger for VSACAPIServiceImpl class. **/
	private static final Log LOGGER = LogFactory.getLog(VSACAPIServiceImpl.class);
	/** serialVersionUID for VSACAPIServiceImpl class. **/
	private static final long serialVersionUID = -6645961609626183169L;
	private static final String PROXY_HOST = System.getProperty("vsac_proxy_host");
	private static final int PROXY_PORT = Integer.parseInt(System.getProperty("vsac_proxy_port"));
	
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
				+ version);
		VsacApiResult result = new VsacApiResult();
		String eightHourTicket = UMLSSessionTicket.getTicket(getThreadLocalRequest().getSession().getId());
		if (eightHourTicket != null) {
			if ((oid != null) && StringUtils.isNotEmpty(oid) && StringUtils.isNotBlank(oid)) {
				LOGGER.info("Start ValueSetsResponseDAO...Using Proxy:"+PROXY_HOST+":"+PROXY_PORT);
				ValueSetsResponseDAO dao = new ValueSetsResponseDAO(eightHourTicket,PROXY_HOST,PROXY_PORT);
				ValueSetsResponse vsr = null;
				if ((version != null) && StringUtils.isNotEmpty(version)) {
					vsr = dao.getMultipleValueSetsResponseByOIDAndVersion(oid.trim(), version);
					result.setSuccess(true);
				} else if ((effectiveDate != null) && StringUtils.isNotEmpty(effectiveDate)) {
					vsr = dao.getMultipleValueSetsResponseByOIDAndEffectiveDate(oid, effectiveDate);
					result.setSuccess(true);
				} else {
					vsr = dao.getMultipleValueSetsResponseByOID(oid.trim());
					result.setSuccess(true);
				}
				if (!vsr.getXmlPayLoad().isEmpty()) {
					VSACValueSetWrapper wrapper = convertXmltoValueSet(vsr.getXmlPayLoad());
					for (MatValueSet valueSet : wrapper.getValueSetList()) {
						handleVSACGroupedValueSet(eightHourTicket, valueSet);
					}
					result.setVsacResponse(wrapper.getValueSetList());
					LOGGER.info("Successfully converted valueset object from vsac xml payload.");
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
					ValueSetsResponseDAO daoGroupped = new ValueSetsResponseDAO(eightHourTicket,PROXY_HOST,PROXY_PORT);
					ValueSetsResponse vsrGrouped = daoGroupped.getMultipleValueSetsResponseByOID(
							groupedValueSetOid[0].trim());
					VSACValueSetWrapper wrapperGrouped = convertXmltoValueSet(vsrGrouped.getXmlPayLoad());
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
	/***
	 * Method to update valueset's without versions from VSAC in Measure XML.
	 * Skip Timing elements and User defined QDM. Supplemental Data Elements are considered here.
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
			for (QualityDataSetDTO qualityDataSetDTO : appliedQDMList) {
				LOGGER.info("OID ====" + qualityDataSetDTO.getOid());
				// Filter out Timing Element and User defined QDM's.
				if (ConstantMessages.TIMING_ELEMENT.equals(qualityDataSetDTO.getDataType())
						|| ConstantMessages.USER_DEFINED_QDM_OID.equalsIgnoreCase(qualityDataSetDTO.getOid())) {
					LOGGER.info("QDM filtered as it is of either for following type "
							+ "(User defined or Timing Element.");
					continue;
				} else if ("1.0".equalsIgnoreCase(qualityDataSetDTO.getVersion())
						|| "1".equalsIgnoreCase(qualityDataSetDTO.getVersion())) {
					LOGGER.info("Start ValueSetsResponseDAO...Using Proxy:"+PROXY_HOST+":"+PROXY_PORT);
					ValueSetsResponseDAO dao = new ValueSetsResponseDAO(eightHourTicket,PROXY_HOST,PROXY_PORT);
					ValueSetsResponse vsr = new ValueSetsResponse();
					try {
						vsr = dao.getMultipleValueSetsResponseByOID(qualityDataSetDTO.getOid());
					} catch (Exception ex) {
						LOGGER.info("Value Set reterival failed at VSAC for OID :"
								+ qualityDataSetDTO.getOid() + " with Data Type : "
								+ qualityDataSetDTO.getDataType());
					}
					if (vsr != null) {
						if ((vsr.getXmlPayLoad() != null) && StringUtils.isNotEmpty(vsr.getXmlPayLoad())) {
							VSACValueSetWrapper wrapper = convertXmltoValueSet(vsr.getXmlPayLoad());
							MatValueSet matValueSet = wrapper.getValueSetList().get(0);
							QualityDataSetDTO toBeModifiedQDM = qualityDataSetDTO;
							if (matValueSet != null) {
								matValueSet.setQdmId(qualityDataSetDTO.getId());
								qualityDataSetDTO.setCodeListName(matValueSet.getDisplayName());
								if (matValueSet.isGrouping()) {
									qualityDataSetDTO.setTaxonomy(ConstantMessages.
											GROUPING_CODE_SYSTEM);
									handleVSACGroupedValueSet(eightHourTicket, matValueSet);
								} else {
									qualityDataSetDTO.setTaxonomy(matValueSet.getConceptList().
											getConceptList().get(0).getCodeSystemName());
								}
								matValueSetList.add(matValueSet);
								// Code which updated Measure XML against each
								// modifiable QDM.
								getMeasureLibraryService().updateMeasureXML(qualityDataSetDTO,
										toBeModifiedQDM, measureId);
							}
						}
					}
				} else if (!"1.0".equalsIgnoreCase(qualityDataSetDTO.getVersion())
						|| !"1".equalsIgnoreCase(qualityDataSetDTO.getVersion())) {
					LOGGER.info("Start ValueSetsResponseDAO...Using Proxy:"+PROXY_HOST+":"+PROXY_PORT);
					ValueSetsResponseDAO dao = new ValueSetsResponseDAO(eightHourTicket,PROXY_HOST,PROXY_PORT);
					ValueSetsResponse vsr = new ValueSetsResponse();
					try {
						vsr = dao.getMultipleValueSetsResponseByOIDAndVersion(
								qualityDataSetDTO.getOid(), qualityDataSetDTO.getVersion());
					} catch (Exception ex) {
						LOGGER.info("Value Set reterival failed at VSAC for OID :" + qualityDataSetDTO.getOid()
								+ " with Data Type : " + qualityDataSetDTO.getDataType());
					}
					if (vsr != null) {
						if ((vsr.getXmlPayLoad() != null) && StringUtils.isNotEmpty(vsr.getXmlPayLoad())) {
							VSACValueSetWrapper wrapper = convertXmltoValueSet(vsr.getXmlPayLoad());
							MatValueSet matValueSet = wrapper.getValueSetList().get(0);
							if (matValueSet != null) {
								matValueSet.setQdmId(qualityDataSetDTO.getId());
								handleVSACGroupedValueSet(eightHourTicket, matValueSet);
								matValueSetList.add(matValueSet);
							}
						}
					}
				}
			}
			result.setSuccess(true);
			result.setVsacResponse(matValueSetList);
		} else {
			result.setSuccess(false);
			result.setFailureReason(result.UMLS_NOT_LOGGEDIN);
			LOGGER.info("UMLS Login is required");
		}
		return result;
	}
	
	/***
	 * Method to update valueset's without versions from VSAC in Measure XML.
	 * Skip supplemental Data Elements and Timing elements and User defined QDM.
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
			for (QualityDataSetDTO qualityDataSetDTO : appliedQDMList) {
				LOGGER.info(" VSACAPIServiceImpl updateVSACValueSets :: OID:: " + qualityDataSetDTO.getOid());
				// Filter out Timing Element , User defined QDM's and
				// supplemental data elements.
				if (ConstantMessages.TIMING_ELEMENT.equals(qualityDataSetDTO.getDataType())
						|| ConstantMessages.USER_DEFINED_QDM_OID.equalsIgnoreCase(qualityDataSetDTO.getOid())
						|| qualityDataSetDTO.isSuppDataElement()) {
					LOGGER.info("VSACAPIServiceImpl updateVSACValueSets :: QDM filtered as it is of either"
							+ "for following type Supplemental data or User defined or Timing Element.");
					continue;
				} else if ("1.0".equalsIgnoreCase(qualityDataSetDTO.getVersion())) {
					LOGGER.info("Start ValueSetsResponseDAO...Using Proxy:"+PROXY_HOST+":"+PROXY_PORT);
					ValueSetsResponseDAO dao = new ValueSetsResponseDAO(UMLSSessionTicket.
							getTicket(getThreadLocalRequest().getSession().getId()),PROXY_HOST,PROXY_PORT);
					ValueSetsResponse vsr = new ValueSetsResponse();
					try {
						vsr = dao.getMultipleValueSetsResponseByOID(qualityDataSetDTO.getOid());
					} catch (Exception ex) {
						LOGGER.info("VSACAPIServiceImpl updateVSACValueSets :: Value Set reterival failed at "
								+ "VSAC for OID :" + qualityDataSetDTO.getOid() + " with Data Type : "
								+ qualityDataSetDTO.getDataType());
					}
					if (vsr != null) {
						if ((vsr.getXmlPayLoad() != null) && StringUtils.isNotEmpty(vsr.getXmlPayLoad())) {
							VSACValueSetWrapper wrapper = convertXmltoValueSet(vsr.getXmlPayLoad());
							MatValueSet matValueSet = wrapper.getValueSetList().get(0);
							QualityDataSetDTO toBeModifiedQDM = qualityDataSetDTO;
							if (matValueSet != null) {
								qualityDataSetDTO.setCodeListName(matValueSet.getDisplayName());
								if (matValueSet.isGrouping()) {
									qualityDataSetDTO.setTaxonomy(ConstantMessages.
											GROUPING_CODE_SYSTEM);
								} else {
									qualityDataSetDTO.setTaxonomy(matValueSet.getConceptList().
											getConceptList().get(0).getCodeSystemName());
								}
								// Code which updated Measure XML against each
								// modifiable QDM.
								getMeasureLibraryService().updateMeasureXML(qualityDataSetDTO,
										toBeModifiedQDM, measureId);
							}
						}
					}
					
				}
			}
			result.setSuccess(true);
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
		String eightHourTicketForUser = new VSACTicketService().getTicketGrantingTicket(userName, password);
		UMLSSessionTicket.put(getThreadLocalRequest().getSession().getId(), eightHourTicketForUser);
		LOGGER.info("End VSACAPIServiceImpl validateVsacUser: " + " Ticket issued for 8 hours: " + eightHourTicketForUser);
		return eightHourTicketForUser != null;
	}
}
