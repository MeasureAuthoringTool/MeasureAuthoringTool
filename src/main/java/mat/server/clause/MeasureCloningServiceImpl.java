package mat.server.clause;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.measure.service.CQLService;
import mat.client.measure.service.MeasureCloningService;
import mat.client.shared.CQLWorkSpaceConstants;
import mat.client.shared.MatContext;
import mat.client.shared.MatException;
import mat.client.shared.MessageDelegate;
import mat.dao.OrganizationDAO;
import mat.dao.UserDAO;
import mat.dao.clause.MeasureDAO;
import mat.dao.clause.MeasureSetDAO;
import mat.dao.clause.MeasureXMLDAO;
import mat.model.Author;
import mat.model.Organization;
import mat.model.User;
import mat.model.clause.ComponentMeasure;
import mat.model.clause.Measure;
import mat.model.clause.MeasureDeveloperAssociation;
import mat.model.clause.MeasureSet;
import mat.model.clause.MeasureTypeAssociation;
import mat.model.clause.MeasureXML;
import mat.model.cql.CQLParameter;
import mat.server.CQLLibraryService;
import mat.server.LoggedInUserUtil;
import mat.server.SpringRemoteServiceServlet;
import mat.server.service.impl.MatContextServiceUtil;
import mat.server.service.impl.XMLUtility;
import mat.server.util.MATPropertiesService;
import mat.server.util.ManageMeasureDetailModelConversions;
import mat.server.util.MeasureUtility;
import mat.server.util.XmlProcessor;
import mat.shared.ConstantMessages;
import mat.shared.SaveUpdateCQLResult;
import mat.shared.UUIDUtilClient;

/**
 * The Class MeasureCloningServiceImpl.
 */
@SuppressWarnings("serial")
public class MeasureCloningServiceImpl extends SpringRemoteServiceServlet implements MeasureCloningService {
	/** Constant for QDM Expired Name String**/
	private static final String QDM_EXPIRED_NON_DEFAULT = "expired";
	/** Constant for QDM Birth date Name String**/
	private static final String QDM_BIRTHDATE_NON_DEFAULT = "birthdate";
	@Autowired
	private CQLLibraryService cqlLibraryService;
	
	@Autowired
	private MeasureDAO measureDAO;
	
	@Autowired
	private MeasureXMLDAO measureXmlDAO;
	
	@Autowired
	private MeasureSetDAO measureSetDAO;
	
	@Autowired
	private OrganizationDAO organizationDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	private CQLService cqlService;
	
	private static final Log logger = LogFactory.getLog(MeasureCloningServiceImpl.class);
	
	private static final String MEASURE_DETAILS = "measureDetails";
	
	private static final String MEASURE_GROUPING = "measureGrouping";
	
	private static final String UU_ID = "uuid";
	
	private static final String OID = "oid";
	
	private static final String DATATYPE = "datatype";
	
	private static final String VERSION = "version";
	
	private static final String SUPPLEMENTAL_DATA_ELEMENTS = "supplementalDataElements";
	
	private static final String RISK_ADJUSTMENT_VARIABLES = "riskAdjustmentVariables";
	
	private static final String VERSION_ZERO = "0.0";
	
	private static final String PATIENT_CHARACTERISTIC_BIRTH_DATE_OID = "21112-8";
	
	private static final String PATIENT_CHARACTERISTIC_EXPIRED_OID = "419099009";
	
	private static final String PATIENT_CHARACTERISTIC_BIRTH_DATE = "Patient Characteristic Birthdate";
	
	private static final String PATIENT_CHARACTERISTIC_EXPIRED = "Patient Characteristic Expired";
	
	private static final String TIMING_ELEMENT ="Timing ELement";
	
	private static final String ORIGINAL_NAME = "originalName";
	private static final String NAME = "name";
	
	private static final String PROGRAM = "program";
	private static final String RELEASE = "release";
	private static final String TYPE = "type";
	private static final String TAXONOMY = "taxonomy";
	private static final String GROUPING = "Grouping";
	private static final String EXTENSIONAL = "Extensional";
	
	private static final String CANNOT_ACCESS_MEASURE = "Cannot access this measure.";
	
	private Document clonedDoc;
	
	Measure clonedMeasure;
	
	@Override
	public ManageMeasureSearchModel.Result cloneExistingMeasure(ManageMeasureDetailModel currentDetails) throws MatException {
		
		currentDetails.setMeasureSetId(null);
		if (!MatContextServiceUtil.get().isCurrentMeasureClonable(measureDAO, currentDetails.getId())) {
			createException(CANNOT_ACCESS_MEASURE);
		}
		
		return clone(currentDetails, false);
	}
	
	@Override
	public Result draftExistingMeasure(ManageMeasureDetailModel currentDetails) throws MatException {
		
		if (!MatContextServiceUtil.get().isCurrentMeasureDraftable(measureDAO, userDAO, currentDetails.getId())) {
			createException(CANNOT_ACCESS_MEASURE);
		}
		
		String name = measureDAO.getMeasureNameIfDraftAlreadyExists(currentDetails.getMeasureSetId());
		if (StringUtils.isNotBlank(name)) {
			createException("This draft can not be created. A draft of " + name + " has already been created in the system.");
		}
		
		return clone(currentDetails, true);
	}

	private void createException(String message) throws MatException {
		Exception e = new Exception(message);
		log(e.getMessage(), e);
		throw new MatException(e.getMessage());
	}
	
	private ManageMeasureSearchModel.Result clone(ManageMeasureDetailModel currentDetails, boolean creatingDraft) throws MatException {
		logger.info("In MeasureCloningServiceImpl.clone() method..");
		measureDAO = (MeasureDAO) context.getBean("measureDAO");
		measureXmlDAO = (MeasureXMLDAO) context.getBean("measureXMLDAO");
		measureSetDAO = (MeasureSetDAO) context.getBean("measureSetDAO");
		userDAO = (UserDAO) context.getBean("userDAO");
		cqlService = (CQLService) context.getBean("cqlService");

		cqlLibraryService = (CQLLibraryService) context.getBean("cqlLibraryService");

		if (cqlService.checkIfLibraryNameExists(currentDetails.getCQLLibraryName(), currentDetails.getMeasureSetId())){
			throw new MatException(MessageDelegate.DUPLICATE_LIBRARY_NAME);
		}
		
		try {
			ManageMeasureSearchModel.Result result = new ManageMeasureSearchModel.Result();
			Measure measure = measureDAO.find(currentDetails.getId());

			if(checkNonCQLCloningValidation(measure, creatingDraft)){
				Exception e = new Exception("Cannot clone this measure.");
				log(e.getMessage(), e);
				throw new MatException(e.getMessage());
			}
			
			MeasureXML xml = measureXmlDAO.findForMeasure(currentDetails.getId());

			clonedMeasure = new Measure();
			String originalXml = xml.getMeasureXMLAsString();
			
			InputSource oldXmlstream = new InputSource(new StringReader(originalXml));
			DocumentBuilderFactory documentBuilderFactory = XMLUtility.getInstance().buildDocumentBuilderFactory();
			DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();
			Document originalDoc = docBuilder.parse(oldXmlstream);
			clonedDoc = originalDoc;
			clonedMeasure.setaBBRName(currentDetails.getShortName());
			clonedMeasure.setDescription(currentDetails.getMeasureName());
			clonedMeasure.setCqlLibraryName(currentDetails.getCQLLibraryName());
			clonedMeasure.setQdmVersion(MATPropertiesService.get().getQmdVersion());
			clonedMeasure.setReleaseVersion(measure.getReleaseVersion());			
			clonedMeasure.setDraft(Boolean.TRUE);
			clonedMeasure.setPatientBased(currentDetails.isPatientBased());
			
			if(CollectionUtils.isNotEmpty(measure.getComponentMeasures()) && measure.getIsCompositeMeasure()){
				clonedMeasure.setIsCompositeMeasure(measure.getIsCompositeMeasure());
				clonedMeasure.setCompositeScoring(measure.getCompositeScoring());
				clonedMeasure.setComponentMeasures(cloneAndSetComponentMeasures(measure.getComponentMeasures(),clonedMeasure));
			}
			
			if (currentDetails.getMeasScoring() != null) {
				clonedMeasure.setMeasureScoring(currentDetails.getMeasScoring());
			} else {
				clonedMeasure.setMeasureScoring(measure.getMeasureScoring());
			}
			
			if (creatingDraft) {
				if(isOrgPresentCheckByOID(currentDetails.getStewardId())) {
					Organization organization = organizationDAO.findByOid(currentDetails.getStewardId());
					if(organization != null) {
						clonedMeasure.setMeasureStewardId(String.valueOf(organization.getId()));
					}
				}
				clonedMeasure.setNqfNumber(currentDetails.getNqfId());
				clonedMeasure.setMeasurementPeriodFrom(getTimestampFromDateString(currentDetails.getMeasFromPeriod()));
				clonedMeasure.setMeasurementPeriodTo(getTimestampFromDateString(currentDetails.getMeasToPeriod()));
				ManageMeasureDetailModelConversions conversion = new ManageMeasureDetailModelConversions();
				conversion.createMeasureDetails(clonedMeasure, currentDetails);
				createMeasureType(clonedMeasure, currentDetails);
				createMeasureDevelopers(clonedMeasure, currentDetails);
			}
			// when creating a draft of a shared version  Measure then the Measure Owner should not change
			boolean isNonCQLtoCQLDraft = false; 
			if (creatingDraft) {
				isNonCQLtoCQLDraft = createDraftAndDetermineIfNonCQL(currentDetails.getVersionNumber(), measure);
				
			} else { 
				cloneMeasure();
			}
			
			String formattedVersion = MeasureUtility.formatVersionText(clonedMeasure.getRevisionNumber(), clonedMeasure.getVersion());
			
			String formattedVersionWithText = MeasureUtility.getVersionTextWithRevisionNumber(clonedMeasure.getVersion(), 
					clonedMeasure.getRevisionNumber(), clonedMeasure.isDraft());
			
			SaveUpdateCQLResult saveUpdateCQLResult = cqlService.getCQLLibraryData(originalXml);
			List<String> usedCodeList = saveUpdateCQLResult.getUsedCQLArtifacts().getUsedCQLcodes();
			
			// Create the measureGrouping tag
			clearChildNodes(MEASURE_GROUPING);
			clearChildNodes(SUPPLEMENTAL_DATA_ELEMENTS);
			clearChildNodes(RISK_ADJUSTMENT_VARIABLES);
			
			String clonedXMLString = convertDocumenttoString(clonedDoc);
			MeasureXML clonedXml = new MeasureXML();
			clonedXml.setMeasureXMLAsByteArray(clonedXMLString);
			clonedXml.setMeasureId(clonedMeasure.getId());
			
			XmlProcessor xmlProcessor = new XmlProcessor(clonedXml.getMeasureXMLAsString());
			xmlProcessor.removeUnusedDefaultCodes(usedCodeList);
			
			removeMeasureDetailsNodes(xmlProcessor);
			
			if (!measure.getMeasureScoring().equals(currentDetails.getMeasScoring()) || currentDetails.isPatientBased()) {

				String scoringTypeId = clonedMeasure.getMeasureScoring();
				xmlProcessor.removeNodesBasedOnScoring(scoringTypeId, currentDetails.isPatientBased());
				xmlProcessor.createNewNodesBasedOnScoring(scoringTypeId, MATPropertiesService.get().getQmdVersion(), clonedMeasure.getPatientBased());  
				clonedXml.setMeasureXMLAsByteArray(xmlProcessor.transform(xmlProcessor.getOriginalDoc()));
			}
			
			boolean isUpdatedForCQL = updateForCQLMeasure(measure, xmlProcessor, clonedMeasure, isNonCQLtoCQLDraft);
			xmlProcessor.clearValuesetVersionAttribute();
			
			if (creatingDraft) {
				updateCQLLookUpVersionOnDraft(xmlProcessor, formattedVersion);
			} else {
				resetVersionOnCloning(xmlProcessor);
			}

			//this means this is a CQL Measure to CQL Measure draft/clone.
			if(!isUpdatedForCQL){
				//create the default 4 CMS supplemental definitions
				appendSupplementalDefinitions(xmlProcessor, false);
				// Always set latest QDM Version.
				MeasureUtility.updateLatestQDMVersion(xmlProcessor);
			}
	
			// Update CQL Library Name from the UI field
			xmlProcessor.updateCQLLibraryName(clonedMeasure.getCqlLibraryName());

			clonedXml.setMeasureXMLAsByteArray(xmlProcessor.transform(xmlProcessor.getOriginalDoc()));
			
			logger.info("Final XML after cloning/draft" + clonedXml.getMeasureXMLAsString());
			measureXmlDAO.save(clonedXml);
			
			result.setId(clonedMeasure.getId());
			result.setName(currentDetails.getMeasureName());
			result.setShortName(currentDetails.getShortName());
			result.setScoringType(currentDetails.getMeasScoring());
			result.setVersion(formattedVersionWithText);
			result.setEditable(Boolean.TRUE);
			result.setClonable(Boolean.TRUE);
			result.setPatientBased(clonedMeasure.getPatientBased());
			
			return result;
			
		} catch (Exception e) {
			log(e.getMessage(), e);
			throw new MatException(e.getMessage());
		}
	}
	
	public void removeMeasureDetailsNodes(XmlProcessor xmlProcessor) {
		try {
			Node measureDetailNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), "/measure/measureDetails");
			if(measureDetailNode != null) {
				Node parentNode = measureDetailNode.getParentNode();
				parentNode.removeChild(measureDetailNode);
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
	private Timestamp getTimestampFromDateString(String date) {
		if(StringUtils.isEmpty(date)) {
			return null;
		}
		try {
			SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat(
	                "MM/dd/yyyy");
			Date fromDate = datetimeFormatter1.parse(date);
			return new Timestamp(fromDate.getTime());
		} catch (ParseException e) {
			logger.error("Cant convert measure date");
			return null;
		}
		
	}
	
	private boolean isOrgPresentCheckByOID(String stewardOID) {
		return organizationDAO.getAllOrganizations().stream().anyMatch(org -> String.valueOf(org.getOrganizationOID()).equals(stewardOID)); 
	}

	private void createMeasureDevelopers(Measure clonedMeasure, ManageMeasureDetailModel currentDetails) {
		if(currentDetails.getAuthorSelectedList() != null) {
			List<MeasureDeveloperAssociation> developerAssociations =  currentDetails.getAuthorSelectedList().stream()
					.map(author -> createMeasureDetailsAssociation(author, clonedMeasure))
					.collect(Collectors.toList());
			clonedMeasure.setMeasureDevelopers(developerAssociations);
		}
	}

	private MeasureDeveloperAssociation createMeasureDetailsAssociation(Author author, Measure measure) {
		if(isOrgPresentCheckByOID(author.getId())) {
			Organization organization = organizationDAO.findByOid(author.getId());
			if(organization != null) {
				return new MeasureDeveloperAssociation(measure, organization);
			}
		}
		return null;
	}
	
	private void createMeasureType(Measure clonedMeasure, ManageMeasureDetailModel currentDetails) {
		if(currentDetails.getMeasureTypeSelectedList() != null) {
			List<MeasureTypeAssociation> typesAssociations = currentDetails.getMeasureTypeSelectedList().stream()
					.map(type -> new MeasureTypeAssociation(clonedMeasure, type))
					.collect(Collectors.toList());
			clonedMeasure.setMeasureTypes(typesAssociations);
		}
	}

	private List<ComponentMeasure> cloneAndSetComponentMeasures(List<ComponentMeasure> componentMeasures, Measure clonedMeasure) {
		return componentMeasures.stream().map(f -> new ComponentMeasure(clonedMeasure,f.getComponentMeasure(),f.getAlias())).collect(Collectors.toList());
	}

	private boolean createDraftAndDetermineIfNonCQL(String version, Measure measure) {
		boolean isNonCQLtoCQLDraft = false;
		clonedMeasure.setOwner(measure.getOwner());
		clonedMeasure.setMeasureSet(measure.getMeasureSet());
		//MAT-9206 changes
		String draftVer;
		if(MatContext.get().isCQLMeasure(measure.getReleaseVersion())) {
			draftVer = measure.getVersion();
		}else {
			isNonCQLtoCQLDraft = true;
			draftVer = getVersionOnNonCQLDraft(version);
		}
		clonedMeasure.setVersion(draftVer);
		clonedMeasure.setRevisionNumber("000");
		clonedMeasure.seteMeasureId(measure.geteMeasureId());
		measureDAO.saveMeasure(clonedMeasure);
		
		return isNonCQLtoCQLDraft;
	}
	
	private void cloneMeasure() {
		 
		// Clear the measureDetails tag
		if (LoggedInUserUtil.getLoggedInUser() != null) {
			User currentUser = userDAO.find(LoggedInUserUtil.getLoggedInUser());
			clonedMeasure.setOwner(currentUser);
		}
		clearChildNodes(MEASURE_DETAILS);
		
		MeasureSet measureSet = new MeasureSet();
		measureSet.setId(UUID.randomUUID().toString());
		measureSetDAO.save(measureSet);
		clonedMeasure.setMeasureSet(measureSet);
		clonedMeasure.setVersion(VERSION_ZERO);
		clonedMeasure.setRevisionNumber("000");
		measureDAO.saveMeasure(clonedMeasure);
	
	}

	private boolean updateForCQLMeasure(Measure measure, XmlProcessor xmlProcessor, Measure clonedMsr, boolean isNonCQLtoCQLDraft) 
			throws XPathExpressionException {
		
		Node cqlLookUpNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), "/measure/cqlLookUp");
		
		if(cqlLookUpNode != null){
					
			//Update QDM Version in Measure XMl for Draft and CQL Measures
			Node qdmVersionNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), "/measure/cqlLookUp/usingModelVersion");
			if(qdmVersionNode!=null){
				qdmVersionNode.setTextContent(MATPropertiesService.get().getQmdVersion());
			}
			
			return false;
		}
		
		clonedMsr.setReleaseVersion(MATPropertiesService.get().getCurrentReleaseVersion());
		
		Node populationsNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), "/measure/populations");
		if(populationsNode != null){
			Node populationsParentNode = populationsNode.getParentNode();
			populationsParentNode.removeChild(populationsNode);
		}
		
		Node measureObservationsNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), "/measure/measureObservations");
		if(measureObservationsNode != null){
			Node measureObservationsParentNode = measureObservationsNode.getParentNode();
			measureObservationsParentNode.removeChild(measureObservationsNode);
		}
		
		Node stratificationNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), "/measure/strata");
		if(stratificationNode != null){
			Node strataParentNode = stratificationNode.getParentNode();
			strataParentNode.removeChild(stratificationNode);
		}
		
		String scoringTypeId = clonedMeasure.getMeasureScoring();
		
		xmlProcessor.createNewNodesBasedOnScoring(scoringTypeId, MATPropertiesService.get().getQmdVersion(), clonedMeasure.getPatientBased());
		
		// This section generates CQL Look Up tag from CQLXmlTemplate.xml

		XmlProcessor cqlXmlProcessor = cqlLibraryService.loadCQLXmlTemplateFile();
		String libraryName = clonedMsr.getDescription();
		
		String version = clonedMsr.getVersion();
		
		
		String cqlLookUpTag = cqlLibraryService.getCQLLookUpXml((MeasureUtility.cleanString(libraryName)), version, cqlXmlProcessor, "//measure");
		if (cqlLookUpTag != null && StringUtils.isNotBlank(cqlLookUpTag)) {
			try {
				xmlProcessor.appendNode(cqlLookUpTag, "cqlLookUp", "/measure");
			} catch (SAXException | IOException e) {
				logger.debug("Measure lookup failed:" + e.getMessage());
			} 
		}
		
		//copy qdm to cqlLookup/valuesets
		NodeList qdmNodes = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), "/measure/elementLookUp/qdm");		
		Node cqlValuesetsNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), "/measure/cqlLookUp/valuesets");
		List<Node> qdmNodeList = new ArrayList<>();
		List<Node> cqlValuesetsNodeList = new ArrayList<>();
		
		/**
		 * We need to capture old "Patient Characteristic Expired"(oid=419099009) and "Patient Characteristic Birthdate"(oid=21112-8)
		 * and remove them. Also we need to remove qdm with name="Birthdate' and 'Expired' which are non default qdms along with Occurrence
		 * qdm elements.
		 * Further below, when checkForTimingElementsAndAppend() is called, it will add back the above 2 elements with new properties.
		 * For ex: "Patient Characteristic Expired" had an old name of "Expired", but the new name is "Dead".
		 */
		
		
		if(cqlValuesetsNode != null){
			for(int i=0;i<qdmNodes.getLength();i++){
				Node qdmNode = qdmNodes.item(i);
				boolean isClonable = true;
				String oid = qdmNode.getAttributes().getNamedItem(OID).getNodeValue();
				String qdmName = qdmNode.getAttributes().getNamedItem(NAME).getNodeValue();
				String dataType = qdmNode.getAttributes().getNamedItem(DATATYPE).getNodeValue();
				if(oid.equals(PATIENT_CHARACTERISTIC_EXPIRED_OID)){
					qdmNodeList.add(qdmNode);
					isClonable = false;
				}else if(oid.equals(PATIENT_CHARACTERISTIC_BIRTH_DATE_OID)){
					isClonable = false;
				} else if((qdmName.equalsIgnoreCase(QDM_BIRTHDATE_NON_DEFAULT) 
						|| dataType.equalsIgnoreCase(PATIENT_CHARACTERISTIC_BIRTH_DATE)) 
						&& !oid.equals(PATIENT_CHARACTERISTIC_BIRTH_DATE_OID)){
					qdmNodeList.add(qdmNode);
					isClonable = false;
				} else if((qdmName.equalsIgnoreCase(QDM_EXPIRED_NON_DEFAULT) 
						|| dataType.equalsIgnoreCase(PATIENT_CHARACTERISTIC_EXPIRED)) 
						&& !oid.equals(PATIENT_CHARACTERISTIC_EXPIRED_OID)){
					qdmNodeList.add(qdmNode);
					isClonable = false;
				} else if(dataType.equalsIgnoreCase(TIMING_ELEMENT)){
					qdmNodeList.add(qdmNode);
					isClonable = false;
				}
				//Remove Specific Occurrence of QDM
				if(qdmNode.getAttributes().getNamedItem("instance") != null){
					qdmNodeList.add(qdmNode);
					isClonable = false;
				}
				if(isClonable){
					//MAT-8729 : Drafting of non-CQL to CQL measures - force version to be 1.0 if its value is 1.
					String qdmAppliedVersion = qdmNode.getAttributes().getNamedItem(VERSION).getNodeValue();
					if(qdmAppliedVersion.equals("1")){
						qdmNode.getAttributes().getNamedItem(VERSION).setNodeValue("1.0");
					}
					Node clonedqdmNode = qdmNode.cloneNode(true);
					xmlProcessor.getOriginalDoc().renameNode(clonedqdmNode, null, "valueset");
					//MAT-8770
					if(clonedqdmNode.getAttributes().getNamedItem(DATATYPE) != null){
						clonedqdmNode.getAttributes().removeNamedItem(DATATYPE);
					}
					
					cqlValuesetsNode.appendChild(clonedqdmNode);
				}
			}
			for(int i=0;i<cqlValuesetsNode.getChildNodes().getLength();i++){
				Node valueSetNode = addOriginalNameAttributeIfNotPresent(cqlValuesetsNode.getChildNodes().item(i), xmlProcessor);
				valueSetNode = addProgramAttributeIfNotPresent(valueSetNode, xmlProcessor);
				valueSetNode = addReleaseAttributeIfNotPresent(valueSetNode, xmlProcessor);
				valueSetNode = addVSACTypeAttributeIfNotPresent(valueSetNode, xmlProcessor);
				cqlValuesetsNodeList.add(valueSetNode);
			}
		}
		
				
		//Remove all duplicate value sets for new Value Sets workspace.
		if(cqlValuesetsNodeList != null && !cqlValuesetsNodeList.isEmpty()){
			List<String> cqlVSACValueSets = new ArrayList<>();
			List<String> cqlUserDefValueSets = new ArrayList<>();
			for(int i=0;i<cqlValuesetsNodeList.size();i++){
				Node cqlNode = cqlValuesetsNodeList.get(i);
				Node parentNode = cqlNode.getParentNode();
				String valuesetName = cqlNode.getAttributes().getNamedItem(NAME).getTextContent();
				if(!isUserDefinedValueSet(cqlNode.getAttributes().getNamedItem(OID).getTextContent())){
					if(!cqlVSACValueSets.contains(valuesetName)){
						cqlVSACValueSets.add(valuesetName);
					}else{
						parentNode.removeChild(cqlNode);
					}
				}
				else{
					if(!cqlUserDefValueSets.contains(valuesetName)){
						cqlUserDefValueSets.add(valuesetName);
					}else{
						parentNode.removeChild(cqlNode);
					}
				}
			}

			//Loop through user Defined and remove if it exists already in VSAC list
			for(int i=0;i<cqlValuesetsNodeList.size();i++){
				Node cqlNode = cqlValuesetsNodeList.get(i);
				Node parentNode = cqlNode.getParentNode();
				if(isUserDefinedValueSet(cqlNode.getAttributes().getNamedItem(OID).getTextContent())){
					for (String userDefName : cqlUserDefValueSets) {
						if(cqlVSACValueSets.contains(userDefName)){
							parentNode.removeChild(cqlNode);
						}
					}
				}
			}

			cqlVSACValueSets.clear();
			cqlUserDefValueSets.clear();
		}
		
		//Remove all unclonable QDM's collected above in For Loop from elementLookUp tag.
		if(qdmNodeList != null && !qdmNodeList.isEmpty()){
			for(int i=0;i<qdmNodeList.size();i++){
				Node qNode = qdmNodeList.get(i);
				Node parentNode = qNode.getParentNode();
				parentNode.removeChild(qNode);
			}
		}

		checkForDefaultCQLParametersAndAppend(xmlProcessor);
		checkForDefaultCQLDefinitionsAndAppend(xmlProcessor);
		
		return true;
	}
	
	private boolean isUserDefinedValueSet(String valuesetOID) {
		return valuesetOID.equals(ConstantMessages.USER_DEFINED_QDM_OID);
	}
	
	private Node addOriginalNameAttributeIfNotPresent(Node valueSetNode, XmlProcessor xmlProcessor) {
		return addAttributeIfNotPresent(ORIGINAL_NAME, valueSetNode.getAttributes().getNamedItem(NAME).getNodeValue(), valueSetNode, xmlProcessor);
	}
	
	private Node addProgramAttributeIfNotPresent(Node nodeToUpdate, XmlProcessor xmlProcessor) {
		return addAttributeIfNotPresent(PROGRAM, StringUtils.EMPTY, nodeToUpdate, xmlProcessor);
	}
	
	private Node addReleaseAttributeIfNotPresent(Node nodeToUpdate, XmlProcessor xmlProcessor) {
		return addAttributeIfNotPresent(RELEASE, StringUtils.EMPTY, nodeToUpdate, xmlProcessor);
	}

	private Node addVSACTypeAttributeIfNotPresent(Node valueSetNode, XmlProcessor xmlProcessor) {
		String valueSetType = getVSACTypeBasedOnTaxonomy(valueSetNode);
		return addAttributeIfNotPresent(TYPE, valueSetType, valueSetNode, xmlProcessor);
	}
	
	private Node addAttributeIfNotPresent(String attributeName, String value, Node nodeToUpdate, XmlProcessor xmlProcessor) {
		Node attribute = nodeToUpdate.getAttributes().getNamedItem(attributeName);
		if(attribute == null) {
			createDefaultAttr(attributeName, value, nodeToUpdate, xmlProcessor);
		}
		return nodeToUpdate;
	}
	
	private void createDefaultAttr(String name, String value, Node nodeToUpdate, XmlProcessor xmlProcessor) {
		Attr attr = xmlProcessor.getOriginalDoc().createAttribute(name);
		attr.setNodeValue(value);
		nodeToUpdate.getAttributes().setNamedItem(attr);
	}
	
	private String getVSACTypeBasedOnTaxonomy(Node valueset) {
		if(isUserDefinedValueSet(valueset.getAttributes().getNamedItem(OID).getNodeValue())) {
			return "";
		}
		return GROUPING.equals(valueset.getAttributes().getNamedItem(TAXONOMY).getNodeValue()) ? GROUPING : EXTENSIONAL;
	}
	
	/**
	 * Append cql definitions.
	 *
	 * @param xmlProcessor the xml processor
	 */
	private void checkForDefaultCQLDefinitionsAndAppend(XmlProcessor xmlProcessor) {
		
		NodeList defaultCQLDefNodeList = findDefaultDefinitions(xmlProcessor);
		
		if (defaultCQLDefNodeList != null && defaultCQLDefNodeList.getLength() == 4) {
			logger.info("All Default definition elements present in the measure while cloning.");
			logger.info("Check if SupplementalDataElement present in the measure while cloning.");
			// This checks if SDE holds child elements ,if not then append it.
			String defaultSDE = "/measure/supplementalDataElements";
			try {
				Node sdeNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), defaultSDE);
				if(sdeNode != null && !sdeNode.hasChildNodes()){
					appendSupplementalDefinitions(xmlProcessor, true);
				}
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			return;
		}
		
		String defStr = cqlService.getSupplementalDefinitions();
		logger.info("defStr:" + defStr);
		try {
			xmlProcessor.appendNode(defStr, "definition", "/measure/cqlLookUp/definitions");
			
			appendSupplementalDefinitions(xmlProcessor, true);
		} catch (SAXException | IOException | XPathExpressionException e) {
			logger.debug("checkForDefaultCQLDefinitionsAndAppend:" + e.getMessage());
		}
	}

	private void appendSupplementalDefinitions(XmlProcessor xmlProcessor, boolean createNewIds)
			throws XPathExpressionException {
		Node supplementalDataNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), "/measure/supplementalDataElements");
					
		while(supplementalDataNode.hasChildNodes()){
			supplementalDataNode.removeChild(supplementalDataNode.getFirstChild());
		}
		
		NodeList supplementalDefnNodes = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), 
				"/measure/cqlLookUp/definitions/definition[@supplDataElement='true']");
		
		if(supplementalDefnNodes != null){
			for(int i=0;i<supplementalDefnNodes.getLength();i++){
				Node supplNode = supplementalDefnNodes.item(i);
			    			    
				if(createNewIds){
					supplNode.getAttributes().getNamedItem("id").setNodeValue(UUIDUtilClient.uuid());
				}
				
				Element cqlDefinitionRefNode = xmlProcessor.getOriginalDoc().createElement("cqldefinition");
				cqlDefinitionRefNode.setAttribute("displayName", supplNode.getAttributes().getNamedItem(NAME).getNodeValue());
				cqlDefinitionRefNode.setAttribute(UU_ID, supplNode.getAttributes().getNamedItem("id").getNodeValue());
				supplementalDataNode.appendChild(cqlDefinitionRefNode);
				
			}
		}
	}
	
	/**
	 * This method will look into XPath "/measure/cqlLookUp/definitions/" and try and NodeList for Definitions with the following names;
	 * 'SDE Ethnicity','SDE Payer','SDE Race','SDE Sex'.
	 * @param xmlProcessor
	 * @return
	 */
	private NodeList findDefaultDefinitions(XmlProcessor xmlProcessor) {
		NodeList returnNodeList = null;
		Document originalDoc = xmlProcessor.getOriginalDoc();
		
		if (originalDoc != null) {
			try {				
				String defaultDefinitionsXPath = "/measure/cqlLookUp/definitions/definition[@name ='SDE Ethnicity' or @name='SDE Payer' or @name='SDE Race' or @name='SDE Sex']";
				returnNodeList = xmlProcessor.findNodeList(originalDoc, defaultDefinitionsXPath);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		}
		return returnNodeList;
	}
	
	/**
	 * Check for default cql parameters and append.
	 *
	 * @param xmlProcessor the xml processor
	 */
	private void checkForDefaultCQLParametersAndAppend(XmlProcessor xmlProcessor) {
		
		List<String> missingDefaultCQLParameters = xmlProcessor.checkForDefaultParameters();
		
		if (missingDefaultCQLParameters.isEmpty()) {
			logger.info("All Default parameter elements present in the measure while cloning.");
			return;
		}
		logger.info("While cloning, found the following Default parameter elements missing:" + missingDefaultCQLParameters);
		CQLParameter parameter = new  CQLParameter();
	
		parameter.setId(UUID.randomUUID().toString());
		parameter.setName(CQLWorkSpaceConstants.CQL_DEFAULT_MEASUREMENTPERIOD_PARAMETER_NAME);
		parameter.setLogic(CQLWorkSpaceConstants.CQL_DEFAULT_MEASUREMENTPERIOD_PARAMETER_LOGIC);
		parameter.setReadOnly(true);	
		String parStr = cqlService.createParametersXML(parameter);
	
		try {
			xmlProcessor.appendNode(parStr, "parameter", "/measure/cqlLookUp/parameters");
		} catch (SAXException | IOException e) {
			logger.debug("checkForDefaultCQLParametersAndAppend:" + e.getMessage());
		} 
		
	}
	
	
	/**
	 * Clear child nodes.
	 * 
	 * @param nodeName
	 *            the node name
	 */
	private void clearChildNodes(String nodeName) {
		NodeList nodeList = clonedDoc.getElementsByTagName(nodeName);
		Node parentNode = nodeList.item(0);
		if (parentNode != null) {
			while (parentNode.hasChildNodes()) {
				parentNode.removeChild(parentNode.getFirstChild());
				
			}
		}
	}

	
	/**
	 * Method to check If a user is trying to Clone a measure whose release version is not 
	 * v5.xx. 
	 * If the version is v4.xxx or v3.xxx or is NULL, then the method returns true, indicating cloning is not 
	 * allowed for this measure.
	 * 
	 * @param measure
	 * @param creatingDraft
	 * @return
	 */
	private boolean checkNonCQLCloningValidation(Measure measure,
			boolean creatingDraft) {
		
		if(creatingDraft){
			return false;
		}
		
		boolean returnValue = false;
		
		String measureReleaseVersion =  StringUtils.trimToEmpty(measure.getReleaseVersion());
		if(measureReleaseVersion.length() == 0 || measureReleaseVersion.startsWith("v4") || measureReleaseVersion.startsWith("v3")){
			returnValue = true;
		}
		
		return returnValue;
	}
	
	/**
	 * Convert documentto string.
	 * 
	 * @param doc
	 *            the doc
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	private String convertDocumenttoString(Document doc) throws Exception {
		try {
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = XMLUtility.getInstance().buildTransformerFactory();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			return writer.toString();
		} catch (Exception e) {
			log(e.getMessage(), e);
			throw new Exception(e.getMessage());
		}
		
	}
	
	private String getVersionOnNonCQLDraft(String version) {

		String ver = StringUtils.substringAfterLast(version, "v");
		if(StringUtils.countMatches(ver, ".") == 2) {
			ver = StringUtils.substringBeforeLast(ver, ".");						
		} 
		String major = StringUtils.substringBeforeLast(ver, ".");
		String minor = StringUtils.substringAfterLast(ver, ".");

		return major + "." + StringUtils.leftPad(minor, 3, "0");
	}

	private void resetVersionOnCloning(XmlProcessor processor) {
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
		String cqlVersionXPath = "//cqlLookUp/version";
		try {
			Node node = (Node) xPath.evaluate(cqlVersionXPath, processor.getOriginalDoc().getDocumentElement(), XPathConstants.NODE);
			if (node != null) {
				node.setTextContent("0.0.000");
			}
		} catch (XPathExpressionException e) {
			logger.error(e.getMessage());
		}

	}
	
	private void updateCQLLookUpVersionOnDraft(XmlProcessor processor, String version) {
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
		String cqlVersionXPath = "//cqlLookUp/version";
		try {
			Node node = (Node) xPath.evaluate(cqlVersionXPath, processor.getOriginalDoc().getDocumentElement(), XPathConstants.NODE);
			if (node != null) {
				node.setTextContent(version);
			}
		} catch (XPathExpressionException e) {
			logger.error(e.getMessage());
		}

	}

}