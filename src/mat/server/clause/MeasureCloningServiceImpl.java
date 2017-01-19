package mat.server.clause;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import mat.client.clause.cqlworkspace.CQLWorkSpaceConstants;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.service.CQLService;
import mat.client.measure.service.MeasureCloningService;
import mat.client.shared.MatException;
import mat.dao.UserDAO;
import mat.dao.clause.MeasureDAO;
import mat.dao.clause.MeasureSetDAO;
import mat.dao.clause.MeasureXMLDAO;
import mat.model.MeasureNotes;
import mat.model.QualityDataModelWrapper;
import mat.model.User;
import mat.model.clause.Measure;
import mat.model.clause.MeasureSet;
import mat.model.clause.MeasureXML;
import mat.model.cql.CQLParameter;
import mat.server.LoggedInUserUtil;
import mat.server.SpringRemoteServiceServlet;
import mat.server.service.MeasureLibraryService;
import mat.server.service.MeasureNotesService;
import mat.server.service.impl.MatContextServiceUtil;
import mat.server.util.MATPropertiesService;
import mat.server.util.MeasureUtility;
import mat.server.util.XmlProcessor;
import mat.shared.UUIDUtilClient;
import mat.shared.model.util.MeasureDetailsUtil;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.dom.ElementImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

// TODO: Auto-generated Javadoc
/**
 * The Class MeasureCloningServiceImpl.
 */
@SuppressWarnings("serial")
public class MeasureCloningServiceImpl extends SpringRemoteServiceServlet
implements MeasureCloningService {
	/** Constant for QDM Expired Name String**/
	private static final String QDM_EXPIRED_NON_DEFAULT = "expired";
	/** Constant for QDM Birth date Name String**/
	private static final String QDM_BIRTHDATE_NON_DEFAULT = "birthdate";

	/** The measure dao. */
	@Autowired
	private MeasureDAO measureDAO;
	
	/** The measure xml dao. */
	@Autowired
	private MeasureXMLDAO measureXmlDAO;
	
	/** The measure set dao. */
	@Autowired
	private MeasureSetDAO measureSetDAO;
	
	/** The user dao. */
	@Autowired
	private UserDAO userDAO;
	
	private CQLService cqlService;
	
	private MeasureLibraryService measureLibraryService;
	
	/** The Constant logger. */
	private static final Log logger = LogFactory
			.getLog(MeasureCloningServiceImpl.class);
	
	/** The Constant MEASURE_DETAILS. */
	private static final String MEASURE_DETAILS = "measureDetails";
	
	/** The Constant MEASURE_GROUPING. */
	private static final String MEASURE_GROUPING = "measureGrouping";
	
	/** The Constant UU_ID. */
	private static final String UU_ID = "uuid";
	
	/** The Constant TITLE. */
	private static final String TITLE = "title";
	
	/** The Constant SHORT_TITLE. */
	private static final String SHORT_TITLE = "shortTitle";
	
	/** The Constant GUID. */
	private static final String GUID = "guid";
	
	/** The Constant VERSION. */
	private static final String VERSION = "version";
	
	/** The Constant MEASURE_STATUS. */
	private static final String MEASURE_STATUS = "status";
	
	/** The Constant MEASURE_SCORING. */
	private static final String MEASURE_SCORING = "scoring";
	
	/** The Constant MEASUREMENT_PERIOD. */
	private static final String MEASUREMENT_PERIOD = "period";
	
	/** The Constant START_DATE. */
	private static final String START_DATE = "startDate";
	
	/** The Constant STOP_DATE. */
	private static final String STOP_DATE = "stopDate";
	
	/** The Constant SUPPLEMENTAL_DATA_ELEMENTS. */
	private static final String SUPPLEMENTAL_DATA_ELEMENTS = "supplementalDataElements";
	
	/** The Constant Risk_ADJUSTMENT_VARIABLES. */
	private static final String Risk_ADJUSTMENT_VARIABLES = "riskAdjustmentVariables";
	
	/** The Constant VERSION_ZERO. */
	private static final String VERSION_ZERO = "0.0";
	
	/** The Constant TRUE. */
	private static final boolean TRUE = true;
	
	/** The Constant XPATH_MEASURE_ELEMENT_LOOKUP_QDM. */
	private static final String XPATH_MEASURE_ELEMENT_LOOKUP_QDM = "/measure/elementLookUp/qdm [@suppDataElement='true']";
	
	/** The Constant PATIENT_CHARACTERISTIC_BIRTH_DATE_OID. */
	private static final String PATIENT_CHARACTERISTIC_BIRTH_DATE_OID = "21112-8";
	
	/** The Constant PATIENT_CHARACTERISTIC_EXPIRED_OID. */
	private static final String PATIENT_CHARACTERISTIC_EXPIRED_OID = "419099009";
	
	/** The Constant PATIENT_CHARACTERISTIC_BIRTH_DATE. */
	private static final String PATIENT_CHARACTERISTIC_BIRTH_DATE = "Patient Characteristic Birthdate";
	
	/** The Constant PATIENT_CHARACTERISTIC_EXPIRED. */
	private static final String PATIENT_CHARACTERISTIC_EXPIRED = "Patient Characteristic Expired";
	
	/** The Constant TIMING_ELEMENT. */
	private static final String TIMING_ELEMENT ="Timing ELement";

	
	/** The cloned doc. */
	private Document clonedDoc;
	
	/** The cloned measure. */
	Measure clonedMeasure;
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.MeasureCloningService#clone(mat.client.measure.ManageMeasureDetailModel, java.lang.String, boolean)
	 */
	@Override
	public ManageMeasureSearchModel.Result clone(
			ManageMeasureDetailModel currentDetails, String loggedinUserId,
			boolean creatingDraft) throws MatException {
		logger.info("In MeasureCloningServiceImpl.clone() method..");
		measureDAO = (MeasureDAO) context.getBean("measureDAO");
		measureXmlDAO = (MeasureXMLDAO) context.getBean("measureXMLDAO");
		measureSetDAO = (MeasureSetDAO) context.getBean("measureSetDAO");
		userDAO = (UserDAO) context.getBean("userDAO");
		cqlService = (CQLService) context.getBean("cqlService");
		measureLibraryService = (MeasureLibraryService) context.getBean("measureLibraryService");
		
		boolean isMeasureClonable = MatContextServiceUtil.get().isCurrentMeasureClonable(measureDAO, currentDetails.getId());
		if(!isMeasureClonable){
			Exception e = new Exception("Cannot access this measure.");
			log(e.getMessage(), e);
			throw new MatException(e.getMessage());
		}
		
		try {
			ManageMeasureSearchModel.Result result = new ManageMeasureSearchModel.Result();
			Measure measure = measureDAO.find(currentDetails.getId());
			
			if(checkNonCQLCloningValidation(measure, creatingDraft)){
				Exception e = new Exception("Cannot clone this measure.");
				log(e.getMessage(), e);
				throw new MatException(e.getMessage());
			}
			
			MeasureXML xml = measureXmlDAO.findForMeasure(currentDetails
					.getId());
			clonedMeasure = new Measure();
			String originalXml = xml.getMeasureXMLAsString();
			InputSource oldXmlstream = new InputSource(new StringReader(
					originalXml));
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document originalDoc = docBuilder.parse(oldXmlstream);
			clonedDoc = originalDoc;
			clonedMeasure.setaBBRName(currentDetails.getShortName());
			clonedMeasure.setDescription(currentDetails.getName());
			
			clonedMeasure.setReleaseVersion(measure.getReleaseVersion());			
			
			clonedMeasure.setDraft(TRUE);
			if (currentDetails.getMeasScoring() != null) {
				clonedMeasure
				.setMeasureScoring(currentDetails.getMeasScoring());
			} else {
				clonedMeasure.setMeasureScoring(measure.getMeasureScoring());
			}
			if (LoggedInUserUtil.getLoggedInUser() != null) {
				User currentUser = userDAO.find(LoggedInUserUtil
						.getLoggedInUser());
				clonedMeasure.setOwner(currentUser);
			}
			// when creating a draft of a shared version  Measure then the Measure Owner should not change
			if (creatingDraft) {
				clonedMeasure.setOwner(measure.getOwner());
				clonedMeasure.setMeasureSet(measure.getMeasureSet());
				clonedMeasure.setVersion(measure.getVersion());
				clonedMeasure.setRevisionNumber("000");
				clonedMeasure.seteMeasureId(measure.geteMeasureId());
				measureDAO.saveMeasure(clonedMeasure);
				saveMeasureNotesInDraftMeasure(clonedMeasure.getId(), measure);
				createNewMeasureDetailsForDraft();
			} else { 
				// Clear the measureDetails tag
				if (LoggedInUserUtil.getLoggedInUser() != null) {
					User currentUser = userDAO.find(LoggedInUserUtil
							.getLoggedInUser());
					clonedMeasure.setOwner(currentUser);
				}
				clearChildNodes(MEASURE_DETAILS);
				clonedMeasure.setRevisionNumber("000");
				MeasureSet measureSet = new MeasureSet();
				measureSet.setId(UUID.randomUUID().toString());
				measureSetDAO.save(measureSet);
				clonedMeasure.setMeasureSet(measureSet);
				clonedMeasure.setVersion(VERSION_ZERO);
				measureDAO.saveMeasure(clonedMeasure);
				createNewMeasureDetails();
			}
			
			// Create the measureGrouping tag
			clearChildNodes(MEASURE_GROUPING);
			clearChildNodes(SUPPLEMENTAL_DATA_ELEMENTS);
			clearChildNodes(Risk_ADJUSTMENT_VARIABLES);
			
			String clonedXMLString = convertDocumenttoString(clonedDoc);
			MeasureXML clonedXml = new MeasureXML();
			clonedXml.setMeasureXMLAsByteArray(clonedXMLString);
			clonedXml.setMeasure_id(clonedMeasure.getId());
			XmlProcessor xmlProcessor = new XmlProcessor(
					clonedXml.getMeasureXMLAsString());
			
			if ((currentDetails.getMeasScoring() != null)
					&& !currentDetails.getMeasScoring().equals(
							measure.getMeasureScoring())) {
				
				String scoringTypeId = MeasureDetailsUtil
						.getScoringAbbr(clonedMeasure.getMeasureScoring());
				xmlProcessor.removeNodesBasedOnScoring(scoringTypeId);
				xmlProcessor.createNewNodesBasedOnScoring(scoringTypeId,MATPropertiesService.get().getQmdVersion());  
				clonedXml.setMeasureXMLAsByteArray(xmlProcessor
						.transform(xmlProcessor.getOriginalDoc()));
			}
			
			boolean isUpdatedForCQL = updateForCQLMeasure(measure, clonedXml, xmlProcessor, clonedMeasure);
			
			if(!isUpdatedForCQL){
				//this means this is a CQL Measure to CQL Measure draft/clone.
				
				//create the default 4 CMS supplemental definitions
				appendSupplementalDefinitions(xmlProcessor, false);
				xmlProcessor.updateCQLLibraryName();
			}
			
			clonedXml.setMeasureXMLAsByteArray(xmlProcessor
					.transform(xmlProcessor.getOriginalDoc()));
			
			logger.info("Final XML after cloning/draft"
					+ clonedXml.getMeasureXMLAsString());
			measureXmlDAO.save(clonedXml);
			result.setId(clonedMeasure.getId());
			result.setName(currentDetails.getName());
			result.setShortName(currentDetails.getShortName());
			result.setScoringType(currentDetails.getMeasScoring());
			String formattedVersion = MeasureUtility.getVersionText(
					measure.getVersion(), measure.isDraft());
			result.setVersion(formattedVersion);
			result.setEditable(TRUE);
			result.setClonable(TRUE);
			return result;
		} catch (Exception e) {
			log(e.getMessage(), e);
			throw new MatException(e.getMessage());
		}
	}

	private boolean updateForCQLMeasure(Measure measure, MeasureXML clonedXml,
			XmlProcessor xmlProcessor, Measure clonedMsr) throws XPathExpressionException {
		
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
		
		String scoringTypeId = MeasureDetailsUtil
				.getScoringAbbr(clonedMeasure.getMeasureScoring());
		
		xmlProcessor.createNewNodesBasedOnScoring(scoringTypeId,MATPropertiesService.get().getQmdVersion());
		//xmlProcessor.checkForStratificationAndAdd();
		
		//copy qdm to cqlLookup/valuesets
		NodeList qdmNodes = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), "/measure/elementLookUp/qdm");		
		Node cqlValuesetsNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), "/measure/cqlLookUp/valuesets");
		List<Node> qdmNodeList = new ArrayList<Node>();
		List<Node> cqlValuesetsNodeList = new ArrayList<Node>();
		
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
				String oid = qdmNode.getAttributes().getNamedItem("oid").getNodeValue();
				String qdmName = qdmNode.getAttributes().getNamedItem("name").getNodeValue();
				String dataType = qdmNode.getAttributes().getNamedItem("datatype").getNodeValue();
				if(oid.equals(PATIENT_CHARACTERISTIC_EXPIRED_OID)){
					//expiredtimingQDMNode = qdmNode;
					qdmNodeList.add(qdmNode);
					isClonable = false;
				}else if(oid.equals(PATIENT_CHARACTERISTIC_BIRTH_DATE_OID)){
					//birthDataQDMNode = qdmNode;
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
					Node clonedqdmNode = qdmNode.cloneNode(true);
					xmlProcessor.getOriginalDoc().renameNode(clonedqdmNode, null, "valueset");
					cqlValuesetsNode.appendChild(clonedqdmNode);
				}
			}
		}
		
		for(int i=0;i<cqlValuesetsNode.getChildNodes().getLength();i++){
			cqlValuesetsNodeList.add(cqlValuesetsNode.getChildNodes().item(i));
		}
		
		//Remove all duplicate value sets for new Value Sets workspace.
		if(cqlValuesetsNodeList != null && cqlValuesetsNodeList.size() >0){
			List<String> cqlValueSetNames = new ArrayList<String>();
			for(int i=0;i<cqlValuesetsNodeList.size();i++){
				Node cqlNode = cqlValuesetsNodeList.get(i);
				Node parentNode = cqlNode.getParentNode();
				String valuesetName = cqlNode.getAttributes().getNamedItem("name").getTextContent();
				if(!cqlValueSetNames.contains(valuesetName)){
					cqlValueSetNames.add(valuesetName);
				}
				else{
					parentNode.removeChild(cqlNode);
				}
			}
			cqlValueSetNames.clear();
		}
		
		//Remove all unclonable QDM's collected above in For Loop from elementLookUp tag.
		if(qdmNodeList != null && qdmNodeList.size() >0){
			for(int i=0;i<qdmNodeList.size();i++){
				Node qNode = qdmNodeList.get(i);
				Node parentNode = qNode.getParentNode();
				parentNode.removeChild(qNode);
			}
		}
		//Re-factored MAT-8295 : birthdate is not removed from QDM. This is to keep uuid same for ClauseWorkspace references to birthdate qdm.
		/*if(birthDataQDMNode != null){
			Node parentNode = birthDataQDMNode.getParentNode();
			parentNode.removeChild(birthDataQDMNode);
		}*/
		
		//checkForTimingElementsAndAppend(xmlProcessor);
		checkForDefaultCQLParametersAndAppend(xmlProcessor);
		checkForDefaultCQLDefinitionsAndAppend(xmlProcessor);
		checkForDefaultCQLCodeSystemsAndAppend(xmlProcessor);
		checkForDefaultCQLCodesAndAppend(xmlProcessor);
		
		return true;
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
			return;
		}
		
		String defStr = cqlService.getSupplementalDefinitions();
		System.out.println("defStr:"+defStr);
		try {
			xmlProcessor.appendNode(defStr, "definition", "/measure/cqlLookUp/definitions");
			
			appendSupplementalDefinitions(xmlProcessor, true);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			System.out.println("suppl data elems..setting ids");
			for(int i=0;i<supplementalDefnNodes.getLength();i++){
				Node supplNode = supplementalDefnNodes.item(i);
			    			    
				if(createNewIds){
					supplNode.getAttributes().getNamedItem("id").setNodeValue(UUIDUtilClient.uuid());
				}
				
				Element cqlDefinitionRefNode = xmlProcessor.getOriginalDoc().createElement("cqldefinition");
				cqlDefinitionRefNode.setAttribute("displayName", supplNode.getAttributes().getNamedItem("name").getNodeValue());
				cqlDefinitionRefNode.setAttribute("uuid", supplNode.getAttributes().getNamedItem("id").getNodeValue());
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
	
	private void checkForTimingElementsAndAppend(XmlProcessor xmlProcessor) {
		
		/*List<String> missingMeasurementPeriod = xmlProcessor.checkForTimingElements();
		
		if (missingMeasurementPeriod.isEmpty()) {
			logger.info("All timing elements present in the measure while cloning.");
			return;
		}
		logger.info("While cloning, found the following timing elements missing:" + missingMeasurementPeriod);
		
		//		List<String> missingOIDList = new ArrayList<String>();
		//		missingOIDList.add(missingMeasurementPeriod);
		
		QualityDataModelWrapper wrapper = measureXmlDAO.createTimingElementQDMs(missingMeasurementPeriod);
		
		// Object to XML for elementLookUp
		ByteArrayOutputStream streamQDM = XmlProcessor.convertQualityDataDTOToXML(wrapper);
		
		String filteredString = removePatternFromXMLString(streamQDM.toString().substring(streamQDM.toString().indexOf("<measure>", 0)),
				"<measure>", "");
		filteredString = removePatternFromXMLString(filteredString, "</measure>", "");
		
		try {
			System.out.println("timing qdm String:"+filteredString);
			xmlProcessor.appendNode(filteredString, "qdm", "/measure/elementLookUp");
			String cqlValueSetString = filteredString.replaceAll("<qdm", "<valueset");
			System.out.println("timing cql valueset string:"+cqlValueSetString);
			xmlProcessor.appendNode(cqlValueSetString, "valueset", "/measure/cqlLookUp/valuesets");
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
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
		parameter.setParameterName(CQLWorkSpaceConstants.CQL_DEFAULT_MEASUREMENTPERIOD_PARAMETER_NAME);
		parameter.setParameterLogic(CQLWorkSpaceConstants.CQL_DEFAULT_MEASUREMENTPERIOD_PARAMETER_LOGIC);
		parameter.setReadOnly(true);	
		String parStr = cqlService.createParametersXML(parameter);
	
		try {
			xmlProcessor.appendNode(parStr, "parameter", "/measure/cqlLookUp/parameters");
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Check for default CQL code systems and append.
	 *
	 * @param processor the processor
	 */
	private void checkForDefaultCQLCodeSystemsAndAppend(XmlProcessor processor) {
		
		String codeSystemStr = cqlService.getDefaultCodeSystems();
		
		NodeList defaultCQLCodeSystemsList = findDefaultCodeSystems(processor);
		
		if (defaultCQLCodeSystemsList.getLength() > 0) {
			logger.info("All Default codesystems elements present in the measure.");
			return;
		}
		
		try {
			processor.appendNode(codeSystemStr, "codeSystem", "/measure/cqlLookUp/codeSystems");
			
			NodeList defaultCodeSystemNodes = processor.findNodeList(processor.getOriginalDoc(), 
					"/measure/cqlLookUp/codeSystems/codeSystem[@codeSystemName='LOINC' or @codeSystemName='SNOMEDCT']");
			
			if(defaultCodeSystemNodes != null){
				System.out.println("suppl data elems..setting ids");
				for(int i=0;i<defaultCodeSystemNodes.getLength();i++){
					Node codeSystemNode = defaultCodeSystemNodes.item(i);
				    System.out.println("name:"+codeSystemNode.getAttributes().getNamedItem("codeSystemName").getNodeValue());
				    System.out.println("id:"+codeSystemNode.getAttributes().getNamedItem("id").getNodeValue());
				    codeSystemNode.getAttributes().getNamedItem("id").setNodeValue(UUIDUtilClient.uuid());
				}
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Check for default CQL codes and append.
	 *
	 * @param processor the processor
	 */
	private void checkForDefaultCQLCodesAndAppend(XmlProcessor processor) {
		
		String codeStr = cqlService.getDefaultCodes();
		
		NodeList defaultCQLCodesList = findDefaultCodes(processor);
		
		if (defaultCQLCodesList.getLength() > 0) {
			logger.info("All Default code elements present in the measure.");
			return;
		}
		
		try {
			processor.appendNode(codeStr, "code", "/measure/cqlLookUp/codes");
			
			NodeList defaultCodeNodes = processor.findNodeList(processor.getOriginalDoc(), 
					"/measure/cqlLookUp/codes/code[@codeName='Birthdate' or @codeName='Dead']");
			
			if(defaultCodeNodes != null){
				System.out.println("suppl data elems..setting ids");
				for(int i=0;i<defaultCodeNodes.getLength();i++){
					Node codeNode = defaultCodeNodes.item(i);
					System.out.println("codename:"+codeNode.getAttributes().getNamedItem("codeName").getNodeValue());
				    System.out.println("codesystemname:"+codeNode.getAttributes().getNamedItem("codeSystemName").getNodeValue());
				    System.out.println("id:"+codeNode.getAttributes().getNamedItem("id").getNodeValue());
				    codeNode.getAttributes().getNamedItem("id").setNodeValue(UUIDUtilClient.uuid());
				}
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method will look into XPath "/measure/cqlLookUp/codeSystems/" and try and NodeList for Definitions with the following names;
	 * @param xmlProcessor
	 * @return
	 */
	private NodeList findDefaultCodeSystems(XmlProcessor xmlProcessor) {
		NodeList returnNodeList = null;
		Document originalDoc = xmlProcessor.getOriginalDoc();
		
		if (originalDoc != null) {
			try {				
				String defaultCodeSystemsXPath = "/measure/cqlLookUp/codeSystems/codeSystem[@codeSystemName='LOINC' or @codeSystemName='SNOMEDCT']";
				returnNodeList = xmlProcessor.findNodeList(originalDoc, defaultCodeSystemsXPath);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		}
		return returnNodeList;
	}
	
	/**
	 * This method will look into XPath "/measure/cqlLookUp/codes/" and try and NodeList for Definitions with the following names;
	 * @param xmlProcessor
	 * @return
	 */
	private NodeList findDefaultCodes(XmlProcessor xmlProcessor) {
		NodeList returnNodeList = null;
		Document originalDoc = xmlProcessor.getOriginalDoc();
		
		if (originalDoc != null) {
			try {				
				String defaultCodesXPath = "/measure/cqlLookUp/codes/code[@codeName='Birthdate' or @codeName='Dead']";
				returnNodeList = xmlProcessor.findNodeList(originalDoc, defaultCodesXPath);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		}
		return returnNodeList;
	}
	
	/**
	 * Save measure notes in draft measure.
	 * 
	 * @param draftMeasureId
	 *            the draft measure id
	 * @param measure
	 *            the measure
	 */
	private void saveMeasureNotesInDraftMeasure(String draftMeasureId,
			Measure measure) {
		List<MeasureNotes> measureNotesList = getMeasureNotesService()
				.getAllMeasureNotesByMeasureID(measure.getId());
		if ((measureNotesList != null) && !measureNotesList.isEmpty()) {
			
			for (MeasureNotes measureNotes : measureNotesList) {
				if (measureNotes != null) {
					try {
						MeasureNotes measureNotesDraft = measureNotes
								.clone();
						measureNotesDraft.setMeasure_id(draftMeasureId);
						getMeasureNotesService().saveMeasureNote(
								measureNotesDraft);
						logger.info("MeasureNotes saved successfully on creating draft measure.");
					} catch (Exception e) {
						logger.info("Failed to save MeasureNotes on creating draft measure. Exception occured:"
								+ e.getMessage());
					}
				}
			}
			
		}
	}
	
	/**
	 * Gets the measure notes service.
	 * 
	 * @return the measure notes service
	 */
	private MeasureNotesService getMeasureNotesService() {
		return ((MeasureNotesService) context.getBean("measureNotesService"));
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
	 * Gets the supplemental uu ids.
	 * 
	 * @return the supplemental uu ids
	 */
	private HashMap<String, String> getSupplementalUUIds() {
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
		HashMap<String, String> supplementalUUIdMap = null;
		try {
			NodeList nodesElementLookUpAll = (NodeList) xPath.evaluate(
					XPATH_MEASURE_ELEMENT_LOOKUP_QDM,
					clonedDoc.getDocumentElement(), XPathConstants.NODESET);
			if (nodesElementLookUpAll != null) {
				supplementalUUIdMap = new HashMap<String, String>();
				for (int i = 0; i < nodesElementLookUpAll.getLength(); i++) {
					Node newNode = nodesElementLookUpAll.item(i);
					String nodeName = newNode.getAttributes()
							.getNamedItem("name").getNodeValue().toString();
					String uuid = newNode.getAttributes().getNamedItem("uuid")
							.getNodeValue().toString();
					supplementalUUIdMap.put(nodeName, uuid);
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return supplementalUUIdMap;
	}
	
	/**
	 * Creates the new measure details.
	 */
	private void createNewMeasureDetails() {
		NodeList nodeList = clonedDoc.getElementsByTagName(MEASURE_DETAILS);
		Node parentNode = nodeList.item(0);
		Node uuidNode = clonedDoc.createElement(UU_ID);
		uuidNode.setTextContent(clonedMeasure.getId());
		Node titleNode = clonedDoc.createElement(TITLE);
		titleNode.setTextContent(clonedMeasure.getDescription());
		Node shortTitleNode = clonedDoc.createElement(SHORT_TITLE);
		shortTitleNode.setTextContent(clonedMeasure.getaBBRName());
		Node guidNode = clonedDoc.createElement(GUID);
		guidNode.setTextContent(clonedMeasure.getMeasureSet().getId());
		Node versionNode = clonedDoc.createElement(VERSION);
		versionNode.setTextContent(MeasureUtility.getVersionText(String.valueOf(clonedMeasure.getVersionNumber()),
				clonedMeasure.getRevisionNumber(), clonedMeasure.isDraft()));
		//Node statusNode = clonedDoc.createElement(MEASURE_STATUS);
		/*statusNode.setTextContent(clonedMeasure.getMeasureStatus());*/
		Node measurementPeriodNode = createMeasurementPeriodNode();
		Node measureScoringNode = clonedDoc.createElement(MEASURE_SCORING);
		String measureScoring = clonedMeasure.getMeasureScoring();
		ElementImpl element = (ElementImpl) measureScoringNode;
		element.setAttribute("id",
				MeasureDetailsUtil.getScoringAbbr(measureScoring));
		measureScoringNode.setTextContent(measureScoring);
		parentNode.appendChild(uuidNode);
		parentNode.appendChild(titleNode);
		parentNode.appendChild(shortTitleNode);
		parentNode.appendChild(guidNode);
		parentNode.appendChild(versionNode);
		parentNode.appendChild(measurementPeriodNode);
		//parentNode.appendChild(statusNode);
		parentNode.appendChild(measureScoringNode);
	}
	
	/**
	 * Creates the measurement period node.
	 *
	 * @return the node
	 */
	private Node createMeasurementPeriodNode() {
		Node measurementPeriodNode = clonedDoc.createElement(MEASUREMENT_PERIOD);
		ElementImpl element = (ElementImpl) measurementPeriodNode;
		element.setAttribute("calenderYear","true");
		element.setAttribute("uuid",UUID.randomUUID().toString());
		Node startDateNode = clonedDoc.createElement(START_DATE);
		startDateNode.setTextContent("01/01/20XX");
		Node stopDateNode = clonedDoc.createElement(STOP_DATE);
		stopDateNode.setTextContent("12/31/20XX");
		measurementPeriodNode.appendChild(startDateNode);
		measurementPeriodNode.appendChild(stopDateNode);
		return measurementPeriodNode;
	}

	/**
	 * Creates the new measure details for draft.
	 */
	private void createNewMeasureDetailsForDraft() {
		clonedDoc.getElementsByTagName(UU_ID).item(0)
		.setTextContent(clonedMeasure.getId());
		clonedDoc.getElementsByTagName(TITLE).item(0)
		.setTextContent(clonedMeasure.getDescription());
		clonedDoc.getElementsByTagName(SHORT_TITLE).item(0)
		.setTextContent(clonedMeasure.getaBBRName());
		/*clonedDoc.getElementsByTagName(MEASURE_STATUS).item(0)
		.setTextContent(clonedMeasure.getMeasureStatus());*/
		
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
		
		String measureReleaseVersion = (measure.getReleaseVersion() == null) ? "" : measure.getReleaseVersion();
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
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			return writer.toString();
		} catch (Exception e) {
			log(e.getMessage(), e);
			throw new Exception(e.getMessage());
		}
		
	}
	
	/**
	 * Removes the pattern from xml string.
	 * 
	 * @param xmlString
	 *            the xml string
	 * @param patternStart
	 *            the pattern start
	 * @param replaceWith
	 *            the replace with
	 * @return the string
	 */
	private String removePatternFromXMLString(String xmlString,
			String patternStart, String replaceWith) {
		String newString = xmlString;
		if (patternStart != null) {
			newString = newString.replaceAll(patternStart, replaceWith);
		}
		return newString;
	}
}
