package mat.server.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import mat.dao.OrganizationDAO;
import mat.dao.clause.MeasureDAO;
import mat.model.Organization;
import mat.model.clause.Measure;
import mat.model.clause.MeasureXML;
import mat.model.cql.parser.CQLDefinitionModelObject;
import mat.model.cql.parser.CQLFileObject;
import mat.model.cql.parser.CQLFunctionModelObject;
import mat.model.cql.parser.CQLParameterModelObject;
import mat.model.cql.parser.CQLValueSetModelObject;
import mat.shared.UUIDUtilClient;
import net.sf.saxon.TransformerFactoryImpl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

// TODO: Auto-generated Javadoc
/**
 * The Class ExportSimpleXML.
 */
public class ExportSimpleXML {
	
	/** The Constant STRATA. */
	private static final String STRATIFICATION = "stratification";
	
	/** The Constant _logger. */
	private static final Log _logger = LogFactory.getLog(ExportSimpleXML.class);
	
	/** The Constant xPath. */
	private static final javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
	
	/** The Constant MEASUREMENT_PERIOD_OID. */
	private static final String MEASUREMENT_PERIOD_OID = "2.16.840.1.113883.3.67.1.101.1.53";
	
	/** The measure_ id. */
	private static String measure_Id;
	
	/** The Constant Continuous Variable. */
	private static final String SCORING_TYPE_CONTVAR = "CONTVAR";
	
	/** The Constant RATIO. */
	private static final String RATIO = "RATIO";
	
	/** The Constant PROPOR. */
	private static final String PROPOR = "PROPOR";
	
	private static final String COHORT = "COHORT";
	
	/**
	 * Export.
	 *
	 * @param measureXMLObject            the measure xml object
	 * @param message            the message
	 * @param measureDAO TODO
	 * @param organizationDAO the organization dao
	 * @return the string
	 */
	public static String export(MeasureXML measureXMLObject, List<String> message, MeasureDAO measureDAO, OrganizationDAO organizationDAO) {
		String exportedXML = "";
		//Validate the XML
		Document measureXMLDocument;
		try {
			measureXMLDocument = getXMLDocument(measureXMLObject);
			/*if(validateMeasure(measureXMLDocument, message)){*/
			measure_Id = measureXMLObject.getMeasure_id();
			exportedXML = generateExportedXML(measureXMLDocument, organizationDAO,measureDAO, measure_Id);
			//}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} /*catch (XPathExpressionException e) {
			e.printStackTrace();
		}*/
		measure_Id = null;
		return exportedXML;
	}
	
	/**
	 * Export a CQL measure.
	 *
	 * @param measureXMLObject            the measure xml object
	 * @param message            the message
	 * @param measureDAO TODO
	 * @param organizationDAO the organization dao
	 * @return the string
	 */
	public static String export(MeasureXML measureXMLObject, List<String> message, 
			MeasureDAO measureDAO, OrganizationDAO organizationDAO, CQLFileObject cqlFileObject) {
		String exportedXML = "";
        //Validate the XML
		Document measureXMLDocument;
		try {
			measureXMLDocument = getXMLDocument(measureXMLObject);
			/*if(validateMeasure(measureXMLDocument, message)){*/
			measure_Id = measureXMLObject.getMeasure_id();
			exportedXML = generateExportedXML(measureXMLDocument, organizationDAO,measureDAO, measure_Id, cqlFileObject);
			
			
			int insertAt = exportedXML.indexOf("<title>"); 
			exportedXML = exportedXML.substring(0,  insertAt) + "<cqlUUID>" + UUIDUtilClient.uuid() + "</cqlUUID>" + exportedXML.substring(insertAt, exportedXML.length());
			
			
			
			//}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		measure_Id = null;
		return exportedXML;
	}
	
	/**
	 * Sets the qdm id as uuid.
	 * 
	 * @param xmlString
	 *            the xml string
	 * @return the string
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 * @throws ParserConfigurationException
	 *             the parser configuration exception
	 * @throws SAXException
	 *             the sAX exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static String setQDMIdAsUUID(String xmlString) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException{
		//Code to replace all @id attributes of <qdm> with @uuid attribute value
		
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputSource oldXmlstream = new InputSource(new StringReader(xmlString));
		Document originalDoc = docBuilder.parse(oldXmlstream);
		
		NodeList allQDMs = (NodeList) xPath.evaluate("/measure/elementLookUp/qdm", originalDoc.getDocumentElement(), XPathConstants.NODESET);
		for(int i=0;i<allQDMs.getLength();i++){
			Node qdmNode = allQDMs.item(i);
			String uuid = qdmNode.getAttributes().getNamedItem("uuid").getNodeValue();
			qdmNode.getAttributes().getNamedItem("id").setNodeValue(uuid);
		}
		
		return transform(originalDoc);
	}
	
	/**
	 * This will work with the existing Measure XML & assume that it is correct
	 * and validated to generate the exported XML.
	 *
	 * @param measureXMLDocument            the measure xml document
	 * @param organizationDAO the organization dao
	 * @param measureDAO TODO
	 * @param measure_Id TODO
	 * @return the string
	 */
	private static String generateExportedXML(Document measureXMLDocument, OrganizationDAO organizationDAO, MeasureDAO measureDAO, String measure_Id) {
		_logger.info("In ExportSimpleXML.generateExportedXML()");
		try {
			return traverseXML(measureXMLDocument,organizationDAO, measureDAO, measure_Id);
		} catch (Exception e) {
			_logger.info("Exception thrown on ExportSimpleXML.generateExportedXML()");
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * This will work with the existing CQL Measure XML & assume that it is correct
	 * and validated, to generate the exported XML.
	 *
	 * @param measureXMLDocument            the measure xml document
	 * @param organizationDAO the organization dao
	 * @param measureDAO TODO
	 * @param measure_Id TODO
	 * @return the string
	 */
	private static String generateExportedXML(Document measureXMLDocument, OrganizationDAO organizationDAO, MeasureDAO measureDAO, String measure_Id, CQLFileObject cqlFileObject) {
		_logger.info("In ExportSimpleXML.generateExportedXML()");
		try {
			return traverseXML(measureXMLDocument,organizationDAO, measureDAO, measure_Id, cqlFileObject);
		} catch (Exception e) {
			_logger.info("Exception thrown on ExportSimpleXML.generateExportedXML()");
			e.printStackTrace();
		}
		return "";
	}
	
	//This will walk through the original Measure XML and generate the Measure Export XML.
	/**
	 * Traverse xml.
	 *
	 * @param originalDoc            the original doc
	 * @param organizationDAO the organization dao
	 * @param MeasureDAO TODO
	 * @param measure_Id TODO
	 * @return the string
	 * @throws XPathExpressionException             the x path expression exception
	 */
	private static String traverseXML(Document originalDoc, OrganizationDAO organizationDAO,
			MeasureDAO MeasureDAO, String measure_Id) throws XPathExpressionException {
		//set attributes
		updateVersionforMeasureDetails(originalDoc, MeasureDAO, measure_Id);
		//update Steward and developer's node id with oid.
		updateStewardAndDevelopersIdWithOID(originalDoc, organizationDAO);
		setAttributesForComponentMeasures(originalDoc, MeasureDAO);
		List<String> usedClauseIds = getUsedClauseIds(originalDoc);
		
		//using the above list we need to traverse the originalDoc and remove the unused Clauses
		removeUnwantedClauses(usedClauseIds, originalDoc);
		//to get SubTreeRefIds from Population WorkSpace
		List<String> usedSubtreeRefIds = getUsedSubtreeRefIds(usedClauseIds, originalDoc);
		//to get SubTreeIds From Clause WorksPace in a Whole
		List<String> usedSubTreeIds = checkUnUsedSubTreeRef(usedSubtreeRefIds, originalDoc);
		/*	usedSubTreeIds = getUsedSubRefFromRiskAdjustmentVariables(usedSubTreeIds, originalDoc);
		usedSubTreeIds = checkUnUsedSubTreeRef(usedSubTreeIds, originalDoc);*/
		formatAttributeDateInQDMAttribute(usedSubTreeIds, originalDoc);
		//this will remove unUsed SubTrees From SubTreeLookUp
		removeUnwantedSubTrees(usedSubTreeIds, originalDoc);
		//to add UUID attribute for QDM Attribute
		addUUIDtoQDMAttribute(usedSubTreeIds, originalDoc);
		List<String> usedQDMIds = getUsedQDMIds(originalDoc);
		//using the above list we need to traverse the originalDoc and remove the unused QDM's
		removeUnWantedQDMs(usedQDMIds, originalDoc);
		expandAndHandleGrouping(originalDoc);
		addUUIDToFunctions(originalDoc);
		//modify the <startDate> and <stopDate> tags to have date in YYYYMMDD format
		modifyHeaderStart_Stop_Dates(originalDoc);
		modifyElementLookUpForOccurances(originalDoc);
		modifySubTreeLookUpForOccurances(originalDoc);
		// re-order measure Grouping sequence.
		modifyMeasureGroupingSequence(originalDoc);
		//Remove Empty Comments nodes from population Logic.
		removeEmptyCommentsFromPopulationLogic(originalDoc);
		//addLocalVariableNameToQDMs(originalDoc);
		return transform(originalDoc);
	}
	
	//This will walk through the original CQL Measure XML and generate the Measure Export XML.
		/**
		 * Traverse xml.
		 *
		 * @param originalDoc            the original doc
		 * @param organizationDAO the organization dao
		 * @param MeasureDAO TODO
		 * @param measure_Id TODO
		 * @return the string
		 * @throws XPathExpressionException             the x path expression exception
		 */
		private static String traverseXML(Document originalDoc, OrganizationDAO organizationDAO,
				MeasureDAO MeasureDAO, String measure_Id, CQLFileObject cqlFileObject) throws XPathExpressionException {
			//set attributes
			updateVersionforMeasureDetails(originalDoc, MeasureDAO, measure_Id);
			//update Steward and developer's node id with oid.
			updateStewardAndDevelopersIdWithOID(originalDoc, organizationDAO);
			setAttributesForComponentMeasures(originalDoc, MeasureDAO);
			List<String> usedClauseIds = getUsedClauseIds(originalDoc);
			//using the above list we need to traverse the originalDoc and remove the unused Clauses
			removeUnwantedClauses(usedClauseIds, originalDoc);
			List<String> usedCQLArtifacts = checkForUsedCQLArtifacts(originalDoc, cqlFileObject);
			removeUnwantedCQLArtifacts(usedCQLArtifacts, originalDoc);
			removeNode("/measure/subTreeLookUp",originalDoc);
			removeNode("/measure/elementLookUp",originalDoc);
			expandAndHandleGrouping(originalDoc);
			//addUUIDToFunctions(originalDoc);
			//modify the <startDate> and <stopDate> tags to have date in YYYYMMDD format
			modifyHeaderStart_Stop_Dates(originalDoc);
			modifyMeasureGroupingSequence(originalDoc);
			//Remove Empty Comments nodes from population Logic.
			removeEmptyCommentsFromPopulationLogic(originalDoc);
			//addLocalVariableNameToQDMs(originalDoc);
			//createUsedCQLArtifactsWithPopulationNames(originalDoc);
			return transform(originalDoc);
		}
		
		private static List<String> checkForUsedCQLArtifacts(Document originalDoc, CQLFileObject cqlFileObject){
			List<String> masterList = new ArrayList<String>();
			masterList = getAllCQLDefnArtifacts(originalDoc, cqlFileObject);
			List<String> funcList = getAllCQLFuncArtifacts(originalDoc, cqlFileObject);
			for(int i=0;i<funcList.size();i++){
				if(!masterList.contains(funcList.get(i))){
					masterList.add(funcList.get(i));
				}
			}
			
			return masterList;
		}
		
		private static List<String> getAllCQLDefnArtifacts(Document originalDoc, CQLFileObject cqlObject){
			List<String> usedAllCQLArtifacts = new ArrayList<String>();
			try {
				NodeList subTreeRefIdsNodeList = (NodeList) xPath.evaluate("/measure//cqldefinition/@displayName",
						originalDoc.getDocumentElement(), XPathConstants.NODESET);
				
				for (int i = 0; i < subTreeRefIdsNodeList.getLength(); i++) {
					Node cqlDefnNameAttributeNode = subTreeRefIdsNodeList.item(i);
					
					if(!usedAllCQLArtifacts.contains(cqlDefnNameAttributeNode.getNodeValue())){
						usedAllCQLArtifacts.add(cqlDefnNameAttributeNode.getNodeValue());
					}
					for ( String key : cqlObject.getDefinitionsMap().keySet() ) {
					    System.out.println( key );
					}
					
					String cqlName = cqlDefnNameAttributeNode.getNodeValue();
				    cqlName = "\"" + cqlName + "\""; 
					List<CQLDefinitionModelObject> referredToDefinitionsModelObjectList = cqlObject.getDefinitionsMap()
					                      .get(cqlName).getReferredToDefinitions();
					
					for(int j=0 ;j<referredToDefinitionsModelObjectList.size();j++){
						if(!usedAllCQLArtifacts.contains(referredToDefinitionsModelObjectList.get(j).getIdentifier().replaceAll("\"", ""))){
							usedAllCQLArtifacts.add(referredToDefinitionsModelObjectList.get(j).getIdentifier().replaceAll("\"", ""));
						}
					}
					
					List<CQLFunctionModelObject> referredToFunctionsModelObjectList = cqlObject.getDefinitionsMap()
							.get(cqlName).getReferredToFunctions();
					
					for(int m=0 ;m<referredToFunctionsModelObjectList.size();m++){
						if(!usedAllCQLArtifacts.contains(referredToFunctionsModelObjectList.get(m).getIdentifier().replaceAll("\"", ""))){
							usedAllCQLArtifacts.add(referredToFunctionsModelObjectList.get(m).getIdentifier().replaceAll("\"", ""));
						}
					}
					
					List<CQLValueSetModelObject> valueSetsReferredByDefinitionsModelObjectList = cqlObject.getDefinitionsMap()
							.get(cqlName).getReferredByValueSets();
					
					for(int k=0 ;k<valueSetsReferredByDefinitionsModelObjectList.size();k++){
						if(!usedAllCQLArtifacts.contains(valueSetsReferredByDefinitionsModelObjectList.get(k).getIdentifier().replaceAll("\"", ""))){
							usedAllCQLArtifacts.add(valueSetsReferredByDefinitionsModelObjectList.get(k).getIdentifier().replaceAll("\"", ""));
						}
					}
					
					List<CQLParameterModelObject> referredByDefinitionsModelObjectList = cqlObject.getDefinitionsMap()
							.get(cqlName).getReferredByParameters();
					
					for(int n=0 ;n<referredByDefinitionsModelObjectList.size();n++){
						if(!usedAllCQLArtifacts.contains(referredByDefinitionsModelObjectList.get(n).getIdentifier().replaceAll("\"", ""))){
							usedAllCQLArtifacts.add(referredByDefinitionsModelObjectList.get(n).getIdentifier().replaceAll("\"", ""));
						}
					}

				}
				
				
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			
			return usedAllCQLArtifacts;
		}
		
		private static List<String> getAllCQLFuncArtifacts(Document originalDoc, CQLFileObject cqlObject){
			List<String> usedAllCQLArtifacts = new ArrayList<String>();
			try {
				NodeList subTreeRefIdsNodeList = (NodeList) xPath.evaluate("/measure//cqlfunction/@displayName",
						originalDoc.getDocumentElement(), XPathConstants.NODESET);
				for (int i = 0; i < subTreeRefIdsNodeList.getLength(); i++) {
					Node cqlfuncNameAttributeNode = subTreeRefIdsNodeList.item(i);
					
					if(!usedAllCQLArtifacts.contains(cqlfuncNameAttributeNode.getNodeValue())){
						usedAllCQLArtifacts.add(cqlfuncNameAttributeNode.getNodeValue());
					}
					String cqlName = cqlfuncNameAttributeNode.getNodeValue();
				    cqlName = "\"" + cqlName + "\""; 
					List<CQLDefinitionModelObject> referredToDefinitionsModelObjectList = cqlObject.getFunctionsMap()
							.get(cqlName).getReferredToDefinitions();
					
					for(int j=0 ;j<referredToDefinitionsModelObjectList.size();j++){
						if(!usedAllCQLArtifacts.contains(referredToDefinitionsModelObjectList.get(j).getIdentifier().replaceAll("\"", ""))){
							usedAllCQLArtifacts.add(referredToDefinitionsModelObjectList.get(j).getIdentifier().replaceAll("\"", ""));
						}
					}
					
					List<CQLFunctionModelObject> referredToFunctionsModelObjectList = cqlObject.getFunctionsMap()
							.get(cqlName).getReferredToFunctions();
					
					for(int m=0 ;m<referredToFunctionsModelObjectList.size();m++){
						if(!usedAllCQLArtifacts.contains(referredToFunctionsModelObjectList.get(m).getIdentifier().replaceAll("\"", ""))){
							usedAllCQLArtifacts.add(referredToFunctionsModelObjectList.get(m).getIdentifier().replaceAll("\"", ""));
						}
					}
					
					List<CQLValueSetModelObject> valueSetsReferredByDefinitionsModelObjectList = cqlObject.getFunctionsMap()
							.get(cqlName).getReferredByValueSets();
					
					for(int k=0 ;k<valueSetsReferredByDefinitionsModelObjectList.size();k++){
						if(!usedAllCQLArtifacts.contains(valueSetsReferredByDefinitionsModelObjectList.get(k).getIdentifier().replaceAll("\"", ""))){
							usedAllCQLArtifacts.add(valueSetsReferredByDefinitionsModelObjectList.get(k).getIdentifier().replaceAll("\"", ""));
						}
					}
					
					List<CQLParameterModelObject> referredByDefinitionsModelObjectList = cqlObject.getFunctionsMap()
							.get(cqlName).getReferredByParameters();
					
					for(int n=0 ;n<referredByDefinitionsModelObjectList.size();n++){
						if(!usedAllCQLArtifacts.contains(referredByDefinitionsModelObjectList.get(n).getIdentifier().replaceAll("\"", ""))){
							usedAllCQLArtifacts.add(referredByDefinitionsModelObjectList.get(n).getIdentifier().replaceAll("\"", ""));
						}
					}

				}
				
				
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return usedAllCQLArtifacts;
		}
		
		/**
		 * Removes the unwanted CQL definitions, functions, valuesets abd parameters.
		 *
		 * @param usedCQLAtrifactsIds the used sub tree ids
		 * @param originalDoc the original doc
		 * @throws XPathExpressionException the x path expression exception
		 */
		private static void removeUnwantedCQLArtifacts(List<String> usedCQLAtrifactsIds, Document originalDoc) throws XPathExpressionException{
			if((usedCQLAtrifactsIds !=null) && (usedCQLAtrifactsIds.size()>0)){
				
				String uuidXPathString = "";
				
				for(String uuidString:usedCQLAtrifactsIds){
					uuidXPathString += "@name != '"+uuidString + "' and";
				}
				uuidXPathString = uuidXPathString.substring(0,uuidXPathString.lastIndexOf(" and"));
				
				String xPathForUnunsedDefinitionsTreeNodes = "/measure/cqlLookUp//definition["+uuidXPathString+"]";
				String xPathForUnunsedFunctionsTreeNodes = "/measure/cqlLookUp//function["+uuidXPathString+"]";
				String xPathForUnunsedValueSetsTreeNodes = "/measure/cqlLookUp//valueset["+uuidXPathString+"]";
				String xPathForUnunsedParametersTreeNodes = "/measure/cqlLookUp//parameter["+uuidXPathString+"]";
				
				try {
					NodeList unUnsedDefineNodes = (NodeList) xPath.evaluate(xPathForUnunsedDefinitionsTreeNodes, originalDoc.getDocumentElement(), XPathConstants.NODESET);
					if(unUnsedDefineNodes.getLength() > 0){
						Node parentSubTreeNode = unUnsedDefineNodes.item(0).getParentNode();
						for(int i=0;i<unUnsedDefineNodes.getLength();i++){
							parentSubTreeNode.removeChild(unUnsedDefineNodes.item(i));
						}
					}
					
					//to remove functions
					NodeList unUnsedFunctionNodes = (NodeList) xPath.evaluate(xPathForUnunsedFunctionsTreeNodes, originalDoc.getDocumentElement(), XPathConstants.NODESET);
					if(unUnsedFunctionNodes.getLength() > 0){
						Node parentSubTreeNode = unUnsedFunctionNodes.item(0).getParentNode();
						for(int i=0;i<unUnsedFunctionNodes.getLength();i++){
							parentSubTreeNode.removeChild(unUnsedFunctionNodes.item(i));
						}
					}
					
					NodeList unUnsedValueSetNodes = (NodeList) xPath.evaluate(xPathForUnunsedValueSetsTreeNodes, originalDoc.getDocumentElement(), XPathConstants.NODESET);
					if(unUnsedValueSetNodes.getLength() > 0){
						Node parentSubTreeNode = unUnsedValueSetNodes.item(0).getParentNode();
						for(int i=0;i<unUnsedValueSetNodes.getLength();i++){
							parentSubTreeNode.removeChild(unUnsedValueSetNodes.item(i));
						}
					}
					
					NodeList unUnsedParameterNodes = (NodeList) xPath.evaluate(xPathForUnunsedParametersTreeNodes, originalDoc.getDocumentElement(), XPathConstants.NODESET);
					if(unUnsedParameterNodes.getLength() > 0){
						Node parentSubTreeNode = unUnsedParameterNodes.item(0).getParentNode();
						for(int i=0;i<unUnsedParameterNodes.getLength();i++){
							parentSubTreeNode.removeChild(unUnsedParameterNodes.item(i));
						}
					}
					
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
		}
		
		/**
	 * This method will remove empty comments nodes from clauses which are part of Measure Grouping.
	 * @param originalDoc - Document
	 * @throws XPathExpressionException -Exception.
	 */
	private static void removeEmptyCommentsFromPopulationLogic(Document originalDoc) throws XPathExpressionException {
		NodeList commentsNodeList = (NodeList) xPath.evaluate("/measure/measureGrouping//comment",
				originalDoc.getDocumentElement(), XPathConstants.NODESET);
		for (int i = 0; i < commentsNodeList.getLength(); i++) {
			Node commentNode = commentsNodeList.item(i);
			if ((commentNode.getTextContent() == null)
					|| (commentNode.getTextContent().trim().length() == 0)) {
				Node parentNode = commentNode.getParentNode();
				parentNode.removeChild(commentNode);
			}
		}
	}
	/**
	 * Gets the used sub ref from risk adjustment variables.
	 *
	 * @param usedSubTreeIds the used sub tree ids
	 * @param originalDoc the original doc
	 * @return the used sub ref from risk adjustment variables
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static List<String> getUsedSubRefFromRiskAdjustmentVariables(
			List<String> usedSubTreeIds, Document originalDoc) throws XPathExpressionException {
		
		List<String> subTreeRefIdsInRAVList = new ArrayList<String>();
		String xpathforSubTreeInRAV = "/measure/riskAdjustmentVariables/subTreeRef/@id";
		NodeList subTreeRefIdsNodeList = (NodeList) xPath.evaluate(xpathforSubTreeInRAV,
				originalDoc.getDocumentElement(), XPathConstants.NODESET);
		for(int i=0;i<subTreeRefIdsNodeList.getLength();i++){
			subTreeRefIdsInRAVList.add(subTreeRefIdsNodeList.item(i).getNodeValue());
		}
		usedSubTreeIds.removeAll(subTreeRefIdsInRAVList);
		subTreeRefIdsInRAVList.addAll(usedSubTreeIds);
		return subTreeRefIdsInRAVList;
	}
	
	/**
	 * Format attribute date in qdm attribute.
	 *
	 * @param usedSubTreeIds the used sub tree ids
	 * @param originalDoc the original doc
	 */
	private static void formatAttributeDateInQDMAttribute(List<String> usedSubTreeIds,
			Document originalDoc) {
		
		String XPATH_EXP_ATTR_DATE = "/measure/subTreeLookUp//elementRef/attribute";
		try {
			NodeList qdmAttributeList = (NodeList) xPath.evaluate(XPATH_EXP_ATTR_DATE,
					originalDoc.getDocumentElement(), XPathConstants.NODESET);
			for(int i = 0; i< qdmAttributeList.getLength(); i++){
				Node attrNode = qdmAttributeList.item(i);
				if(attrNode.getAttributes().getNamedItem("attrDate")!=null){
					String date = attrNode.getAttributes().getNamedItem("attrDate").getNodeValue();
					attrNode.getAttributes().getNamedItem("attrDate").setNodeValue(formatDate(date));
				}
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Adds the uuid to qdm attribute.
	 *
	 * @param usedSubTreeIds the used sub tree ids
	 * @param originalDoc the original doc
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static void addUUIDtoQDMAttribute(List<String> usedSubTreeIds,
			Document originalDoc) throws XPathExpressionException {
		
		if (usedSubTreeIds.size() == 0) {
			return;
		}
		
		String uuidXPathString = "";
		for(String uuidString: usedSubTreeIds){
			uuidXPathString += "@uuid = '"+uuidString + "' or";
		}
		
		
		uuidXPathString = uuidXPathString.substring(0,uuidXPathString.lastIndexOf(" or"));
		String XPATH_QDM_UUID_ATTRIBUTE = "/measure/subTreeLookUp/subTree["+uuidXPathString+"]//elementRef/attribute";
		NodeList qdmAttrNodeList = (NodeList) xPath.evaluate(XPATH_QDM_UUID_ATTRIBUTE, originalDoc, XPathConstants.NODESET);
		for(int i = 0; i < qdmAttrNodeList.getLength(); i++){
			Node qdmAttrNode = qdmAttrNodeList.item(i);
			
			Attr uuidAttr = originalDoc.createAttribute("attrUUID");
			uuidAttr.setValue(UUID.randomUUID().toString());
			
			qdmAttrNode.getAttributes().setNamedItem(uuidAttr);
		}
		
	}
	
	/**
	 * Update versionfor measure details.
	 *
	 * @param originalDoc the original doc
	 * @param measureDAO the measure dao
	 * @param measure_Id TODO
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static void updateVersionforMeasureDetails(Document originalDoc, MeasureDAO measureDAO, String measure_Id) throws XPathExpressionException {
		String xPathForMeasureDetailsVerion = "/measure/measureDetails/version";
		Node versionNode = (Node) xPath.evaluate(xPathForMeasureDetailsVerion, originalDoc, XPathConstants.NODE);
		Measure measure = measureDAO.find(measure_Id);
		
		versionNode.setTextContent(measure.getMajorVersionStr()
				+ "."+measure.getMinorVersionStr() + "."+measure.getRevisionNumber());
		
	}
	
	
	/**
	 * Update steward and developers id with oid.
	 *
	 * @param originalDoc the original doc
	 * @param organizationDAO the organization dao
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static void updateStewardAndDevelopersIdWithOID(Document originalDoc, OrganizationDAO organizationDAO) throws XPathExpressionException{
		String XPATH_EXPRESSION_STEWARD = "/measure//measureDetails//steward";
		String XPATH_EXPRESSION_DEVELOPERS = "/measure//measureDetails//developers";
		Node stewardParentNode = (Node) xPath.evaluate(	XPATH_EXPRESSION_STEWARD, originalDoc,XPathConstants.NODE);
		if (stewardParentNode != null) {
			String id = stewardParentNode.getAttributes()
					.getNamedItem("id").getNodeValue();
			Organization org = organizationDAO.findById(id);
			if (org != null) {
				stewardParentNode.getAttributes().getNamedItem("id").setNodeValue(org.getOrganizationOID());
			}
		}
		NodeList developerParentNodeList = (NodeList) xPath.evaluate(
				XPATH_EXPRESSION_DEVELOPERS, originalDoc,
				XPathConstants.NODESET);
		Node developerParentNode = developerParentNodeList.item(0);
		if (developerParentNode != null) {
			NodeList developerNodeList = developerParentNode
					.getChildNodes();
			for (int i = 0; i < developerNodeList.getLength(); i++) {
				Node newNode = developerNodeList.item(i);
				String developerId = newNode.getAttributes()
						.getNamedItem("id").getNodeValue();
				Organization org = organizationDAO.findById(developerId);
				newNode.getAttributes().getNamedItem("id").setNodeValue(org.getOrganizationOID());
			}
		}
	}
	/**
	 * Sets the attributes for component measures.
	 *
	 * @param originalDoc the original doc
	 * @param measureDAO the measure dao
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static void setAttributesForComponentMeasures(Document originalDoc, MeasureDAO measureDAO) throws XPathExpressionException{
		String measureId ="";
		String componentMeasureName ="";
		String componentMeasureSetId ="";
		String xPathForComponentMeasureIds = "/measure/measureDetails/componentMeasures/measure";
		NodeList componentMeasureIdList = (NodeList) xPath.evaluate(xPathForComponentMeasureIds, originalDoc, XPathConstants.NODESET);
		if(componentMeasureIdList !=null){
			for(int i=0; i<componentMeasureIdList.getLength(); i++){
				Node measureNode = componentMeasureIdList.item(i);
				measureId = measureNode.getAttributes().getNamedItem("id").getNodeValue();
				//to change ID format to UUID
				measureNode.getAttributes().getNamedItem("id").setNodeValue(UuidUtility.idToUuid(measureId));
				Node attrcomponentMeasureName = originalDoc.createAttribute("name");
				Node attrcomponentMeasureSetId = originalDoc.createAttribute("measureSetId");
				Node attrcomponentVersionNo= originalDoc.createAttribute("versionNo");
				Measure measure = measureDAO.find(measureId);
				componentMeasureName = measure.getDescription();
				componentMeasureSetId = measure.getMeasureSet().getId();
				attrcomponentMeasureName.setNodeValue(componentMeasureName);
				attrcomponentMeasureSetId.setNodeValue(componentMeasureSetId);
				if(measure.isDraft()){
					attrcomponentVersionNo.setNodeValue(measure.getMajorVersionStr()+ "."+ measure.getMinorVersionStr()
							+ "."+ measure.getRevisionNumber());
				}else{
					attrcomponentVersionNo.setNodeValue(measure.getMajorVersionStr()+ "."+ measure.getMinorVersionStr());
				}
				measureNode.getAttributes().setNamedItem(attrcomponentMeasureName);
				measureNode.getAttributes().setNamedItem(attrcomponentMeasureSetId);
				measureNode.getAttributes().setNamedItem(attrcomponentVersionNo);
			}
		}
	}
	
	/**
	 * Removes the unwanted sub trees.
	 *
	 * @param usedSubTreeIds the used sub tree ids
	 * @param originalDoc the original doc
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static void removeUnwantedSubTrees(List<String> usedSubTreeIds, Document originalDoc) throws XPathExpressionException{
		if((usedSubTreeIds !=null) && (usedSubTreeIds.size()>0)){
			
			String uuidXPathString = "";
			
			for(String uuidString:usedSubTreeIds){
				uuidXPathString += "@uuid != '"+uuidString + "' and";
			}
			uuidXPathString = uuidXPathString.substring(0,uuidXPathString.lastIndexOf(" and"));
			
			String xPathForUnunsedSubTreeNodes = "/measure/subTreeLookUp/subTree["+uuidXPathString+"]";
			
			try {
				NodeList unUnsedSubTreeNodes = (NodeList) xPath.evaluate(xPathForUnunsedSubTreeNodes, originalDoc.getDocumentElement(), XPathConstants.NODESET);
				if(unUnsedSubTreeNodes.getLength() > 0){
					Node parentSubTreeNode = unUnsedSubTreeNodes.item(0).getParentNode();
					for(int i=0;i<unUnsedSubTreeNodes.getLength();i++){
						parentSubTreeNode.removeChild(unUnsedSubTreeNodes.item(i));
					}
				}
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			NodeList allSubTreeNodeList = (NodeList) xPath.evaluate("/measure/subTreeLookUp/subTree",
					originalDoc.getDocumentElement(), XPathConstants.NODESET);
			for(int i = 0; i<allSubTreeNodeList.getLength(); i++ ) {
				removeNode("/measure/subTreeLookUp/subTree",originalDoc);
			}
		}
		
	}
	
	/**
	 * Modify element look up for occurances.
	 *
	 * @param originalDoc the original doc
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static void modifyElementLookUpForOccurances(Document originalDoc) throws XPathExpressionException {
		NodeList allOccuranceQDMs = (NodeList) xPath.evaluate("/measure/elementLookUp/qdm[@instance]", originalDoc.getDocumentElement(), XPathConstants.NODESET);
		List<String> qdmOID_Datatype_List = new ArrayList<String>();
		
		for(int i=0;i<allOccuranceQDMs.getLength();i++){
			Node qdmNode = allOccuranceQDMs.item(i);
			String oid = qdmNode.getAttributes().getNamedItem("oid").getNodeValue();
			String datatype = qdmNode.getAttributes().getNamedItem("datatype").getNodeValue();
			String instance = qdmNode.getAttributes().getNamedItem("instance").getNodeValue();
			
			if(qdmOID_Datatype_List.contains(datatype + oid)){
				continue;
			}else{
				qdmOID_Datatype_List.add(datatype + oid);
			}
			
			NodeList nonOccuranceQDMs = (NodeList) xPath.evaluate("/measure/elementLookUp/qdm[@datatype='"+datatype+"'][@oid='"+oid+"'][not(@instance)]",
					originalDoc.getDocumentElement(), XPathConstants.NODESET);
			if(nonOccuranceQDMs.getLength() > 0){
				Node nonOccuranceQDM = nonOccuranceQDMs.item(0);
				Node parentNode = nonOccuranceQDM.getParentNode();
				parentNode.removeChild(nonOccuranceQDM);
				parentNode.appendChild(nonOccuranceQDM.cloneNode(true));
			}else{
				Node newNode = qdmNode.cloneNode(true);
				Node parentNode = qdmNode.getParentNode();
				newNode.getAttributes().removeNamedItem("instance");
				String uuid = UUID.randomUUID().toString();
				newNode.getAttributes().getNamedItem("uuid").setNodeValue(uuid);
				newNode.getAttributes().getNamedItem("id").setNodeValue(uuid);
				parentNode.appendChild(newNode);
			}
		}
	}
	
	/**
	 * This method will look for <subTree> tags within <subTreeLookUp> tag. For each <subTree> with
	 * an "instanceOf" attribute, we need to fetch the corresponding <subTree> and copy its children.
	 *
	 * @param originalDoc the original doc
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static void modifySubTreeLookUpForOccurances(Document originalDoc) throws XPathExpressionException{
		NodeList qdmVariableSubTreeList = (NodeList)xPath.evaluate("/measure/subTreeLookUp/subTree[@instanceOf]", originalDoc.getDocumentElement(), XPathConstants.NODESET);
		
		for(int i=0;i<qdmVariableSubTreeList.getLength();i++){
			Node qdmVariableNode = qdmVariableSubTreeList.item(i);
			String uuid = qdmVariableNode.getAttributes().getNamedItem("uuid").getNodeValue();
			String occuranceLetter = qdmVariableNode.getAttributes().getNamedItem("instance").getNodeValue();
			String displayName = qdmVariableNode.getAttributes().getNamedItem("displayName").getNodeValue();
			displayName = "Occurrence "+occuranceLetter + " of $" + StringUtils.deleteWhitespace(displayName);
			
			qdmVariableNode.getAttributes().getNamedItem("displayName").setNodeValue(displayName);
			
			String referencedUUID = qdmVariableNode.getAttributes().getNamedItem("instanceOf").getNodeValue();
			
			Node referencedSubTreeNode = (Node)xPath.evaluate("/measure/subTreeLookUp/subTree[not(@instanceOf)][@uuid='"+referencedUUID+"']", originalDoc.getDocumentElement(),XPathConstants.NODE);
			Node mainChild = referencedSubTreeNode.getChildNodes().item(0);
			Node mainChildClone = mainChild.cloneNode(true);
			qdmVariableNode.appendChild(mainChildClone);
			findAllElementRefNodes(qdmVariableNode);
		}
	}
	
	/**
	 * Logic copied to Occ Clause Logic Nodes in Simple xml from actual Clause
	 * requires attrUUID to be updated to new in case there are elementRef's at
	 * any level with Attributes. This method is doing the same.
	 * @param qdmVariableNode - Node.
	 */
	private static void findAllElementRefNodes(Node qdmVariableNode) {
		if ((qdmVariableNode != null) && qdmVariableNode.hasChildNodes()) {
			for (int i = 0; i < qdmVariableNode.getChildNodes().getLength(); i++) {
				Node childNode = qdmVariableNode.getChildNodes().item(i);
				if (childNode.getNodeName().equalsIgnoreCase("elementRef")) {
					if (childNode.hasChildNodes()) {
						System.out.println(childNode.getFirstChild().getNodeName());
						Node attrNode = childNode.getFirstChild();
						if (attrNode.getAttributes().getNamedItem("attrUUID") != null) {
							attrNode.getAttributes().getNamedItem("attrUUID")
							.setNodeValue(UUIDUtilClient.uuid());
						}
					}
				} else {
					if (childNode.hasChildNodes()) {
						findAllElementRefNodes(childNode);
					}
				}
			}
		}
	}
	/**
	 * Method to re order Measure Grouping Sequence.
	 * @param originalDoc - Document.
	 * @throws XPathExpressionException - Exception.
	 */
	private static void modifyMeasureGroupingSequence(Document originalDoc) throws XPathExpressionException {
		NodeList groupingNodeList = (NodeList) xPath.evaluate("/measure/measureGrouping/group",
				originalDoc.getDocumentElement(), XPathConstants.NODESET);
		TreeMap<Integer, Node> groupMap = new TreeMap<Integer, Node>();
		for (int i = 0; i < groupingNodeList.getLength(); i++) {
			Node measureGroupingNode = groupingNodeList.item(i);
			String key = measureGroupingNode.getAttributes().getNamedItem("sequence").getNodeValue();
			groupMap.put(Integer.parseInt(key), measureGroupingNode);
		}
		int measureGroupingSequenceCounter = 0;
		for (Integer key : groupMap.keySet()) {
			String xPathMeasureGroupingForSeq = "/measure/measureGrouping/group[@sequence='" + key + "']";
			Node measureGroupingNode = (Node) xPath.evaluate(xPathMeasureGroupingForSeq,
					originalDoc.getDocumentElement(), XPathConstants.NODE);
			measureGroupingSequenceCounter = measureGroupingSequenceCounter + 1;
			measureGroupingNode.getAttributes().getNamedItem("sequence").setNodeValue(measureGroupingSequenceCounter + "");
		}
	}
	
	/**
	 * Transform.
	 * 
	 * @param node
	 *            the node
	 * @return the string
	 */
	private static String transform(Node node){
		_logger.info("In transform() method");
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		TransformerFactory transformerFactory = TransformerFactoryImpl.newInstance();
		DOMSource source = new DOMSource(node);
		StreamResult result = new StreamResult(arrayOutputStream);
		
		try {
			transformerFactory.newTransformer().transform(source, result);
		} catch (TransformerException e) {
			_logger.info("Document object to ByteArray transformation failed "+e.getStackTrace());
			e.printStackTrace();
		}
		_logger.info("Document object to ByteArray transformation complete");
		return arrayOutputStream.toString();
	}
	
	/**
	 * Removes un-wanted qdms, except 'Measurement Period', 'Expired', 'Birthdate' QDM elements.
	 * 
	 * @param usedQDMIds
	 *            the used qdm ids
	 * @param originalDoc
	 *            the original doc
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private static void removeUnWantedQDMs(List<String> usedQDMIds, Document originalDoc) throws XPathExpressionException {
		NodeList allQDMIDs = (NodeList) xPath.evaluate("/measure/elementLookUp/qdm[@name != 'Measurement Period']"
				+ "[@name != 'Expired']"
				+ "[@name != 'Birthdate']/@uuid",
				originalDoc.getDocumentElement(), XPathConstants.NODESET);
		
		for(int i=0;i<allQDMIDs.getLength();i++){
			Node idNode = allQDMIDs.item(i);
			String idNodeValue = idNode.getNodeValue();
			if(!usedQDMIds.contains(idNodeValue)){
				Node qdmNode = ((Attr)idNode).getOwnerElement();
				Node elementLookUpNode = qdmNode.getParentNode();
				elementLookUpNode.removeChild(qdmNode);
			}
		}
	}
	
	/**
	 * This method will search for <clause> tags in the XML and check if the
	 * UUID attribute matches the usedClauseIds list. If not removes the
	 * <clause> tag from its parent.
	 * 
	 * @param usedClauseIds
	 *            the used clause ids
	 * @param originalDoc
	 *            the original doc
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private static void removeUnwantedClauses(List<String> usedClauseIds, Document originalDoc) throws XPathExpressionException {
		//"/measure//clause/@uuid" will get us uuid attribute of all the <clause> tags where ever they are on underneath the <measure> tag
		NodeList allClauseIDs = (NodeList) xPath.evaluate("/measure//clause/@uuid", originalDoc.getDocumentElement(), XPathConstants.NODESET);
		
		for(int i=0;i<allClauseIDs.getLength();i++){
			Node clauseIdNode = allClauseIDs.item(i);
			String clauseNodeUuid = clauseIdNode.getNodeValue();
			
			if(!usedClauseIds.contains(clauseNodeUuid)){
				Node clauseNode = ((Attr)clauseIdNode).getOwnerElement();
				Node clauseParentNode = clauseNode.getParentNode();
				//Ignore if the clause is a Stratification clause.
				if(!STRATIFICATION.equals(clauseParentNode.getNodeName())){
					clauseParentNode.removeChild(clauseNode);
					
					//Check if the parent of the clause is now empty. If yes, remove the parent from its parent.
					if(!clauseParentNode.hasChildNodes()){
						Node parentOfClauseParent = clauseParentNode.getParentNode();
						parentOfClauseParent.removeChild(clauseParentNode);
					}
				}
			}
		}
	}
	
	/**
	 * This method will go through individual <group> tags and each
	 * <packageClause> child. For each <packageClause> it will copy the original
	 * <clause> to <group> and remove the <packageClause> tag. Finally, at the
	 * end of the method it will remove the <populations> tag from the document.
	 * 
	 * @param originalDoc
	 *            the original doc
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private static void expandAndHandleGrouping(Document originalDoc) throws XPathExpressionException {
		Node measureGroupingNode = (Node)xPath.evaluate("/measure/measureGrouping",
				originalDoc.getDocumentElement(), XPathConstants.NODE);
		
		NodeList groupNodes = measureGroupingNode.getChildNodes();
		List<Node> groupNodesList = reArrangeGroupsBySequence(groupNodes);
		
		for(int j=0;j<groupNodesList.size();j++){
			Node groupNode = groupNodesList.get(j);
			String groupSequence = groupNode.getAttributes().getNamedItem("sequence").getNodeValue();
			NodeList packageClauses = groupNode.getChildNodes();
			List<Node> clauseNodes = new ArrayList<Node>();
			for(int i=0;i<packageClauses.getLength();i++){
				
				Node packageClause = packageClauses.item(i);
				
				String uuid = packageClause.getAttributes().getNamedItem("uuid").getNodeValue();
				String type = packageClause.getAttributes().getNamedItem("type").getNodeValue();
				
				Node clauseNode = findClauseByUUID(uuid, type, originalDoc).cloneNode(true);
				
				if("stratification".equals(clauseNode.getNodeName())){
					NodeList stratificationClauses = clauseNode.getChildNodes();
					
					for(int h=0;h<stratificationClauses.getLength();h++){
						Node stratificationClause = stratificationClauses.item(h);
						//add childCount to clauseNode
						if((packageClause.getChildNodes()!=null) && (packageClause.getChildNodes().getLength()>0)){
							Node itemCount = packageClause.getChildNodes().item(0);
							Node clonedItemCount = itemCount.cloneNode(true);
							stratificationClause.appendChild(clonedItemCount);
						}
						Node clonedClauseNode = stratificationClause.cloneNode(true);
						//set a new 'uuid' attribute value for <clause>
						String cureUUID = UUIDUtilClient.uuid();
						clonedClauseNode.getAttributes().getNamedItem("uuid").setNodeValue(cureUUID);
						clauseNodes.add(clonedClauseNode);
					}
				}else{
					//add childCount to clauseNode
					if((packageClause.getChildNodes()!=null) && (packageClause.getChildNodes().getLength()>0)){
						Node itemCount = packageClause.getChildNodes().item(0);
						Node clonedItemCount = itemCount.cloneNode(true);
						clauseNode.appendChild(clonedItemCount);
					}
					
					//add associatedPopulationUUID to clauseNode
					if(type.equalsIgnoreCase("denominator") || type.equalsIgnoreCase("numerator")|| type.equalsIgnoreCase("measureObservation")){
						Node hasAssociatedPopulationUUID = packageClause.getAttributes().getNamedItem("associatedPopulationUUID");
						if((hasAssociatedPopulationUUID != null) && !hasAssociatedPopulationUUID.toString().isEmpty()){
							String associatedPopulationUUID = hasAssociatedPopulationUUID.getNodeValue();
							Node attr = originalDoc.createAttribute("associatedPopulationUUID");
							attr.setNodeValue(associatedPopulationUUID);
							clauseNode.getAttributes().setNamedItem(attr);
						}
					}
					
					//deep clone the <clause> tag
					//Node clonedClauseNode = clauseNode.cloneNode(true);
					
					//set a new 'uuid' attribute value for <clause>
					String cureUUID = UUIDUtilClient.uuid();
					clauseNode.getAttributes().getNamedItem("uuid").setNodeValue(cureUUID);
					//				String clauseName = clonedClauseNode.getAttributes().getNamedItem("displayName").getNodeValue();
					//set a new 'displayName' for <clause>
					//				clonedClauseNode.getAttributes().getNamedItem("displayName").setNodeValue(clauseName+"_"+groupSequence);
					
					//modify associcatedUUID
					modifyAssociatedPOPID(uuid, cureUUID,groupSequence, originalDoc);
					clauseNodes.add(clauseNode);
				}
				
			}
			//finally remove the all the <packageClause> tags from <group>
			for(int i=packageClauses.getLength();i>0;i--){
				groupNode.removeChild(packageClauses.item(0));
			}
			//set the cloned <clause>'s as children of <group>
			for(Node cNode:clauseNodes){
				groupNode.appendChild(cNode);
			}
			
		}
		
		addMissingEmptyClauses(groupNodes,originalDoc);
		
		//reArrangeClauseNodes(originalDoc);
		removeNode("/measure/populations",originalDoc);
		removeNode("/measure/measureObservations",originalDoc);
		removeNode("/measure/strata",originalDoc);
	}
	
	/**
	 * Re arrange groups by sequence.
	 *
	 * @param groupNodes the group nodes
	 * @return the list
	 */
	private static List<Node> reArrangeGroupsBySequence(NodeList groupNodes) {
		List<Node> nodeList = new ArrayList<Node>();
		for(int i=0;i<groupNodes.getLength();i++){
			nodeList.add(groupNodes.item(i));
		}
		Collections.sort(nodeList, new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {
				if("group".equals(o1.getNodeName()) && "group".equals(o2.getNodeName())){
					return 0;
				}
				String o1Sequence = o1.getAttributes().getNamedItem("sequence").getNodeValue();
				String o2Sequence = o2.getAttributes().getNamedItem("sequence").getNodeValue();
				if(Integer.parseInt(o1Sequence) >= Integer.parseInt(o2Sequence)){
					return 1;
				}else{
					return -1;
				}
			}
		});
		return nodeList;
	}
	
	/**
	 * Adds the missing empty clauses.
	 *
	 * @param groupNodes the group nodes
	 * @param originalDoc the original doc
	 * @throws DOMException the DOM exception
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static void addMissingEmptyClauses(NodeList groupNodes,
			Document originalDoc) throws DOMException, XPathExpressionException {
		
		
		Node node = (Node)xPath.evaluate("/measure/measureDetails/scoring",
				originalDoc.getDocumentElement(), XPathConstants.NODE);
		Node groupNode;
		Node childNode;
		
		for(int i = 0; i< groupNodes.getLength(); i++){
			List<String> existingClauses = new ArrayList<String>();
			List<String> clauseList = new ArrayList<String>();
			clauseList = getRequiredClauses(node.getTextContent());
			groupNode = groupNodes.item(i);
			
			NodeList children = groupNode.getChildNodes();
			
			for(int j = 0; j<children.getLength(); j++){
				childNode = children.item(j);
				NamedNodeMap map = childNode.getAttributes();
				existingClauses.add(map.getNamedItem("type").getNodeValue());
			}
			if(clauseList.removeAll(existingClauses)){
				for(int x = 0; x<clauseList.size();x++){
					generateClauseNode(groupNode,clauseList.get(x),originalDoc);
				}
			}
		}
	}
	
	/**
	 * Generate clause node.
	 *
	 * @param groupNode the group node
	 * @param type the type
	 * @param origionalDoc the original doc
	 */
	private static void generateClauseNode(Node groupNode, String type,Document origionalDoc) {
		// TODO Auto-generated method stub
		Node newClauseNode = groupNode.getFirstChild().cloneNode(true);
		newClauseNode.getAttributes().getNamedItem("displayName").setNodeValue(type);
		newClauseNode.getAttributes().getNamedItem("type").setNodeValue(type);
		newClauseNode.getAttributes().getNamedItem("uuid").setNodeValue(UUID.randomUUID().toString());
		
		NodeList logicalNode = newClauseNode.getChildNodes();
		
		//		for(int i = 0; i<logicalNode.getLength();i++){
		for(int i = logicalNode.getLength()-1; i>-1; i--){
			Node innerNode = logicalNode.item(i);
			newClauseNode.removeChild(innerNode);
			/*if(newClauseNode.getAttributes().getNamedItem("displayName").
					getNodeValue().contains("stratum")){
				newClauseNode.removeChild(innerNode);
			} else if(innerNode.getNodeName().equalsIgnoreCase("itemCount")){//for removing the empty <itemCount> tags
				newClauseNode.removeChild(innerNode);
			}else {
				NodeList innerNodeChildren = innerNode.getChildNodes();
				int length =  innerNodeChildren.getLength();
				for(int j = length - 1; j>-1; j--){
					Node child = innerNodeChildren.item(j);
					innerNode.removeChild(child);
				}
			}*/
		}
		groupNode.appendChild(newClauseNode);
	}
	
	/**
	 * Gets the required clauses.
	 *
	 * @param type the type
	 * @return the required clauses
	 */
	private static List<String> getRequiredClauses(String type){
		List<String> list = new ArrayList<String>();
		if("Cohort".equalsIgnoreCase(type)){
			list.add("initialPopulation");
		}else if("Continuous Variable".equalsIgnoreCase(type)){
			list.add("initialPopulation");
			list.add("measurePopulation");
			list.add("measurePopulationExclusions");
			list.add("measureObservation");
			list.add("stratum");
		}else if("Proportion".equalsIgnoreCase(type)){
			list.add("initialPopulation");
			list.add("denominator");
			list.add("denominatorExclusions");
			list.add("numerator");
			list.add("numeratorExclusions");
			list.add("denominatorExceptions");
			list.add("stratum");
		}else if("Ratio".equalsIgnoreCase(type)){
			list.add("initialPopulation");
			list.add("denominator");
			list.add("denominatorExclusions");
			list.add("numerator");
			list.add("numeratorExclusions");
			list.add("measureObservation");
			list.add("stratum");
		}
		return list;
	}
	
	/**
	 * Modify associated popid.
	 *
	 * @param previousUUID the previous uuid
	 * @param currentUUID the current uuid
	 * @param groupSequence the group sequence
	 * @param originalDoc the original doc
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static void modifyAssociatedPOPID(String previousUUID, String currentUUID,String groupSequence,  Document originalDoc) throws XPathExpressionException {
		NodeList nodeList = (NodeList)xPath.evaluate("/measure/measureGrouping/group[@sequence='"+
				groupSequence +"']/packageClause[@associatedPopulationUUID='"+ previousUUID +"']",
				originalDoc.getDocumentElement(), XPathConstants.NODESET);
		
		for(int i = 0; i<nodeList.getLength(); i++){
			Node childNode = nodeList.item(i);
			childNode.getAttributes().getNamedItem("associatedPopulationUUID").setNodeValue(currentUUID);
		}
		
	}
	
	/**
	 * Gets the used clause ids.
	 *
	 * @param originalDoc the original doc
	 * @return the used clause ids
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static List<String> getUsedClauseIds(Document originalDoc) throws XPathExpressionException {
		List<String> usedClauseIds = new ArrayList<String>();
		
		NodeList measureGrpupingNodeList = (NodeList)xPath.evaluate("/measure/measureGrouping/ group/packageClause" +
				"[not(@uuid = preceding:: group/packageClause/@uuid)]",
				originalDoc.getDocumentElement(), XPathConstants.NODESET);
		
		for(int i=0;i<measureGrpupingNodeList.getLength();i++){
			Node childNode = measureGrpupingNodeList.item(i);
			String uuid = childNode.getAttributes().getNamedItem("uuid").getNodeValue();
			String type = childNode.getAttributes().getNamedItem("type").getNodeValue();
			if(type.equals("stratification")){
				List<String> stratificationClausesIDlist = getStratificationClasuesIDList(uuid, originalDoc);
				usedClauseIds.addAll(stratificationClausesIDlist);
			} else {
				usedClauseIds.add(uuid);
			}
		}
		_logger.info("usedClauseIds:"+usedClauseIds);
		return usedClauseIds;
	}
	
	/**
	 * Gets the stratification clasues id list.
	 *
	 * @param uuid the uuid
	 * @param originalDoc the original doc
	 * @return the stratification clasues id list
	 */
	private static List<String> getStratificationClasuesIDList(String uuid, Document originalDoc) {
		
		String XPATH_MEASURE_GROUPING_STRATIFICATION_CLAUSES = "/measure/strata/stratification" +
				"[@uuid='"+uuid+"']/clause/@uuid";
		List<String> clauseList = new ArrayList<String>();
		try {
			NodeList stratificationClausesNodeList = (NodeList)xPath.evaluate(XPATH_MEASURE_GROUPING_STRATIFICATION_CLAUSES,
					originalDoc,XPathConstants.NODESET);
			for(int i=0;i<stratificationClausesNodeList.getLength();i++){
				clauseList.add(stratificationClausesNodeList.item(i).getNodeValue());
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clauseList;
	}
	
	/**
	 * Gets the used subtree ref ids.
	 *
	 * @param usedClauseIds the used clause ids
	 * @param originalDoc the original doc
	 * @return the used subtree ref ids
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static List<String> getUsedSubtreeRefIds(List<String> usedClauseIds, Document originalDoc)
			throws XPathExpressionException {
		// Populations
		List<String> usedSubTreeRefIdsPop = new ArrayList<String>();
		String uuidXPathString = "";
		
		for(String uuidString: usedClauseIds){
			uuidXPathString += "@uuid = '"+uuidString + "' or";
		}
		uuidXPathString = uuidXPathString.substring(0,uuidXPathString.lastIndexOf(" or"));
		String XPATH_POPULATION_SUBTREEREF = "/measure/populations//clause["+uuidXPathString+"]" +
				"//subTreeRef[not(@id = preceding:: populations//clause//subTreeRef/@id)]/@id";
		NodeList groupedSubTreeRefIdsNodeListPop = (NodeList) xPath.evaluate(
				XPATH_POPULATION_SUBTREEREF,
				originalDoc.getDocumentElement(), XPathConstants.NODESET);
		
		for (int i = 0; i < groupedSubTreeRefIdsNodeListPop.getLength(); i++) {
			Node groupedSubTreeRefIdAttributeNodePop = groupedSubTreeRefIdsNodeListPop
					.item(i);
			usedSubTreeRefIdsPop.add(groupedSubTreeRefIdAttributeNodePop
					.getNodeValue());
		}
		
		// Measure Observations
		List<String> usedSubTreeRefIdsMO = new ArrayList<String>();
		String measureObservationSubTreeRefID = "/measure/measureObservations//clause["+
				uuidXPathString+"]//subTreeRef[not(@id = preceding:: measureObservations//clause//subTreeRef/@id)]/@id";
		NodeList groupedSubTreeRefIdsNodeListMO = (NodeList) xPath.evaluate(
				measureObservationSubTreeRefID,
				originalDoc.getDocumentElement(), XPathConstants.NODESET);
		
		for (int i = 0; i < groupedSubTreeRefIdsNodeListMO.getLength(); i++) {
			Node groupedSubTreeRefIdAttributeNodeMO = groupedSubTreeRefIdsNodeListMO
					.item(i);
			usedSubTreeRefIdsMO.add(groupedSubTreeRefIdAttributeNodeMO
					.getNodeValue());
		}
		
		// Stratifications
		List<String> usedSubTreeRefIdsStrat = new ArrayList<String>();
		String startSubTreeRefID = "/measure/strata//clause["+
				uuidXPathString+"]//subTreeRef[not(@id = preceding:: strata//clause//subTreeRef/@id)]/@id";
		NodeList groupedSubTreeRefIdListStrat = (NodeList) xPath.evaluate(
				startSubTreeRefID,
				originalDoc.getDocumentElement(), XPathConstants.NODESET);
		
		for (int i = 0; i < groupedSubTreeRefIdListStrat.getLength(); i++) {
			Node groupedSubTreeRefIdAttributeNodeStrat = groupedSubTreeRefIdListStrat
					.item(i);
			usedSubTreeRefIdsStrat.add(groupedSubTreeRefIdAttributeNodeStrat
					.getNodeValue());
		}
		
		
		usedSubTreeRefIdsPop.removeAll(usedSubTreeRefIdsMO);
		usedSubTreeRefIdsMO.addAll(usedSubTreeRefIdsPop);
		
		
		usedSubTreeRefIdsMO.removeAll(usedSubTreeRefIdsStrat);
		usedSubTreeRefIdsStrat.addAll(usedSubTreeRefIdsMO);
		
		//for each used subTree id, find out if this an occurrance of a QDM Variable.
		//If Yes, then find out the real subTree being referenced and make sure it is
		//a part of the used SubTree List
		List<String> usedQDMOccuranceRefs = new ArrayList<String>();
		for(String uuid: usedSubTreeRefIdsStrat){
			Node subTreeNode = (Node) xPath.evaluate("/measure/subTreeLookUp/subTree[@uuid='"+uuid+"']",
					originalDoc.getDocumentElement(), XPathConstants.NODE);
			
			NamedNodeMap namedNodeMap = subTreeNode.getAttributes();
			Node attribute = namedNodeMap.getNamedItem("instanceOf");
			if(attribute != null){
				String attributeValue = attribute.getNodeValue();
				if(!usedSubTreeRefIdsStrat.contains(attributeValue)){
					usedQDMOccuranceRefs.add(attributeValue);
				}
			}
		}
		usedSubTreeRefIdsStrat.addAll(usedQDMOccuranceRefs);
		
		//to get the UsedSubTreeIds from RiskAdjustment Variables from MeasurePackager Tab
		usedSubTreeRefIdsStrat = getUsedSubRefFromRiskAdjustmentVariables(usedSubTreeRefIdsStrat, originalDoc);
		return usedSubTreeRefIdsStrat;
	}
	
	
	/**
	 * Check un used sub tree ref.
	 *
	 * @param usedSubTreeRefIds the used sub tree ref ids
	 * @param originalDoc the original doc
	 * @return the list
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static List<String> checkUnUsedSubTreeRef(List<String> usedSubTreeRefIds, Document originalDoc) throws XPathExpressionException{
		
		List<String> allSubTreeRefIds = new ArrayList<String>();
		NodeList subTreeRefIdsNodeList = (NodeList) xPath.evaluate("/measure//subTreeRef/@id",
				originalDoc.getDocumentElement(), XPathConstants.NODESET);
		
		for (int i = 0; i < subTreeRefIdsNodeList.getLength(); i++) {
			Node subTreeRefIdAttributeNode = subTreeRefIdsNodeList.item(i);
			if(!allSubTreeRefIds.contains(subTreeRefIdAttributeNode.getNodeValue())){
				allSubTreeRefIds.add(subTreeRefIdAttributeNode.getNodeValue());
			}
		}
		allSubTreeRefIds.removeAll(usedSubTreeRefIds);
		
		List<String> referencedSubTrees = getReferencedSubTrees(usedSubTreeRefIds, allSubTreeRefIds, originalDoc);
		usedSubTreeRefIds.addAll(referencedSubTrees);
		
		return usedSubTreeRefIds;
	}
	
	/**
	 * Gets the referenced sub trees.
	 *
	 * @param usedSubTreeRefIds the used sub tree ref ids
	 * @param allSubTreeRefIds the all sub tree ref ids
	 * @param originalDoc the original doc
	 * @return the referenced sub trees
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static List<String> getReferencedSubTrees(List<String> usedSubTreeRefIds, List<String> allSubTreeRefIds, Document originalDoc) throws XPathExpressionException{
		List<String> referencedIds = new ArrayList<String>();
		
		for(int i = 0; i< usedSubTreeRefIds.size(); i++){
			for(int j=0; j<allSubTreeRefIds.size(); j++){
				Node usedSubTreeRefNode = (Node) xPath.evaluate("/measure/subTreeLookUp/subTree[@uuid='"+
						usedSubTreeRefIds.get(i)+ "']//subTreeRef[@id='"+allSubTreeRefIds.get(j)+"']",
						originalDoc.getDocumentElement(), XPathConstants.NODE);
				if(usedSubTreeRefNode != null){
					if(!usedSubTreeRefIds.contains(allSubTreeRefIds.get(j))){
						referencedIds.add(allSubTreeRefIds.get(j));
					}
				}
			}
		}
		
		if(referencedIds.size() > 0){
			
			//for each used subTree id, find out if this an occurance of a QDM Variable.
			//If Yes, then find out the real subTree being referenced and make sure it is
			//a part of the used SubTree List
			List<String> usedQDMOccuranceRefs = new ArrayList<String>();
			for(String uuid: referencedIds){
				Node subTreeNode = (Node) xPath.evaluate("/measure/subTreeLookUp/subTree[@uuid='"+uuid+"'][@instanceOf]",
						originalDoc.getDocumentElement(), XPathConstants.NODE);
				
				if(subTreeNode != null){
					NamedNodeMap namedNodeMap = subTreeNode.getAttributes();
					String attributeValue = namedNodeMap.getNamedItem("instanceOf").getNodeValue();
					if(!usedSubTreeRefIds.contains(attributeValue)){
						usedQDMOccuranceRefs.add(attributeValue);
					}
				}
			}
			referencedIds.addAll(usedQDMOccuranceRefs);
			
			allSubTreeRefIds.removeAll(referencedIds);
			referencedIds.addAll(getReferencedSubTrees(referencedIds, allSubTreeRefIds, originalDoc));
			
		}
		
		return referencedIds;
	}
	
	/**
	 * Gets the used qdm ids.
	 * 
	 * @param originalDoc
	 *            the original doc
	 * @return the used qdm ids
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private static List<String> getUsedQDMIds(Document originalDoc) throws XPathExpressionException {
		List<String> usedQDMIds = new ArrayList<String>();
		NodeList elementRefNodeList = (NodeList)xPath.evaluate("/measure//elementRef", originalDoc, XPathConstants.NODESET);
		for(int i=0;i<elementRefNodeList.getLength();i++){
			Node elementRefNode = elementRefNodeList.item(i);
			Node idAttributeNode = elementRefNode.getAttributes().getNamedItem("id");
			usedQDMIds.add(idAttributeNode.getNodeValue());
		}
		NodeList elementInQDMAttributesList = (NodeList)xPath.evaluate("/measure//attribute/@qdmUUID", originalDoc, XPathConstants.NODESET);
		for(int i=0;i<elementInQDMAttributesList.getLength();i++){
			Node elementRefNode = elementInQDMAttributesList.item(i);
			usedQDMIds.add(elementRefNode.getNodeValue());
		}
		_logger.info("usedQDMIds:"+usedQDMIds);
		return usedQDMIds;
	}
	
	/**
	 * Gets the XML document.
	 *
	 * @param measureXMLObject the measure xml object
	 * @return the XML document
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws SAXException the SAX exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static Document getXMLDocument(MeasureXML measureXMLObject) throws ParserConfigurationException, SAXException, IOException{
		//Create Document object
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputSource oldXmlstream = new InputSource(new StringReader(measureXMLObject.getMeasureXMLAsString()));
		Document originalDoc = docBuilder.parse(oldXmlstream);
		return originalDoc;
	}
	
	/**
	 * This method finds a <clause> tag in <measure>/<populations> with a
	 * specified 'uuid' attribute.
	 *
	 * @param uuid the uuid
	 * @param type the type
	 * @param originalDoc the original doc
	 * @return the node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static Node findClauseByUUID(String uuid, String type, Document originalDoc) throws XPathExpressionException {
		Node clauseNode = null;
		if(type.equalsIgnoreCase("stratification")){
			String startificationXPath = "/measure/strata/stratification[@uuid='"+uuid+"']";
			clauseNode = (Node)xPath.evaluate(startificationXPath, originalDoc,XPathConstants.NODE);
			
		}else{
			clauseNode = (Node)xPath.evaluate("/measure//clause[@uuid='"+uuid+"']", originalDoc,XPathConstants.NODE);
		}
		return clauseNode;
	}
	
	/**
	 * Takes an XPath notation String for a particular tag and a Document object
	 * and finds and removes the tag from the document.
	 * 
	 * @param nodeXPath
	 *            the node x path
	 * @param originalDoc
	 *            the original doc
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private static void removeNode(String nodeXPath, Document originalDoc) throws XPathExpressionException {
		Node node = (Node)xPath.evaluate(nodeXPath, originalDoc.getDocumentElement(), XPathConstants.NODE);
		if(node != null){
			Node parentNode = node.getParentNode();
			parentNode.removeChild(node);
		}
	}
	
	/**
	 * We need to modify <startDate> and <stopDate> inside
	 * <measureDetails>/<period> to have YYYYMMDD format.
	 * 
	 * @param originalDoc
	 *            the original doc
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private static void modifyHeaderStart_Stop_Dates(Document originalDoc) throws XPathExpressionException {
		Node periodNode = (Node)xPath.evaluate("/measure/measureDetails/period", originalDoc, XPathConstants.NODE);
		
		Node measurementPeriodNode = (Node)xPath.evaluate("/measure/elementLookUp/qdm[@oid='"+ MEASUREMENT_PERIOD_OID + "']",originalDoc,
				XPathConstants.NODE);
		
		
		if(periodNode != null){
			
			if(measurementPeriodNode != null){
				periodNode.getAttributes().getNamedItem("uuid").setNodeValue(measurementPeriodNode.getAttributes().
						getNamedItem("uuid").getNodeValue());
			}
			
			NodeList childNodeList = periodNode.getChildNodes();
			for(int i=0;i<childNodeList.getLength();i++){
				Node node = childNodeList.item(i);
				if("startDate".equals(node.getNodeName())){
					//Date in MM/DD/YYYY
					String value = node.getTextContent();
					node.setTextContent(formatDate(value));
					Node uuidAttributeNode = node.getAttributes().getNamedItem("uuid");
					if(uuidAttributeNode != null){
						node.getAttributes().removeNamedItem("uuid");
					}
					
				}else if("stopDate".equals(node.getNodeName())){
					//Date in MM/DD/YYYY
					String value = node.getTextContent();
					node.setTextContent(formatDate(value));
					Node uuidAttributeNode = node.getAttributes().getNamedItem("uuid");
					if(uuidAttributeNode!=null){
						node.getAttributes().removeNamedItem("uuid");
					}
				}
			}
		}
	}
	
	/**
	 * This method will go through the entire XML file and find <functionalOp>
	 * tags and add a 'uuid' attribute to each.
	 * 
	 * @param originalDoc
	 *            the original doc
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private static void addUUIDToFunctions(Document originalDoc) throws XPathExpressionException {
		NodeList functionalOpNodes = (NodeList)xPath.evaluate("/measure//clause//functionalOp", originalDoc, XPathConstants.NODESET);
		
		for(int i=0;i<functionalOpNodes.getLength();i++){
			Node functionalOpNode = functionalOpNodes.item(i);
			
			Attr uuidAttr = originalDoc.createAttribute("uuid");
			uuidAttr.setValue(UUID.randomUUID().toString());
			
			functionalOpNode.getAttributes().setNamedItem(uuidAttr);
		}
	}
	/**
	 * This method will expect Date String in MM/DD/YYYY format And convert it
	 * to YYYYMMDD format.
	 * 
	 * @param date
	 *            the date
	 * @return the string
	 */
	private static String formatDate(String date){
		String dateString = "";
		try{
			String[] splitDate = date.split("/");
			String month = splitDate[0];
			String dt = splitDate[1];
			String year = splitDate[2];
			
			if((year.length() != 4) || (year.toLowerCase().indexOf("x") > -1)){
				year = "0000";
			}
			dateString = year + month + dt;
		}catch (Exception e) {
			_logger.info("Bad Start/Stop dates in Measure Details."+e.getMessage());
		}
		return dateString;
	}
	
}