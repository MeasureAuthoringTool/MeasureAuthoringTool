package mat.server.simplexml.cql;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import mat.server.util.ExportSimpleXML;
import mat.server.util.UuidUtility;
import mat.shared.UUIDUtilClient;
import net.sf.saxon.TransformerFactoryImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ExportNewSimpleXML {

	
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
	public static String export(MeasureXML measureXMLObject, List<String> message, 
			MeasureDAO measureDAO, OrganizationDAO organizationDAO, CQLFileObject cqlFileObject) {
		String exportedXML = "";
        //cqlObject = cqlFileObject;
		//Validate the XML
		Document measureXMLDocument;
		try {
			measureXMLDocument = getXMLDocument(measureXMLObject);
			/*if(validateMeasure(measureXMLDocument, message)){*/
			measure_Id = measureXMLObject.getMeasure_id();
			exportedXML = generateExportedXML(measureXMLDocument, organizationDAO,measureDAO, measure_Id, cqlFileObject);
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
		return exportedXML;
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
			MeasureDAO MeasureDAO, String measure_Id, CQLFileObject cqlFileObject) throws XPathExpressionException {
		//set attributes
		updateVersionforMeasureDetails(originalDoc, MeasureDAO, measure_Id);
		//update Steward and developer's node id with oid.
		updateStewardAndDevelopersIdWithOID(originalDoc, organizationDAO);
		setAttributesForComponentMeasures(originalDoc, MeasureDAO);
		//List<String> usedClauseIds = getUsedClauseIds(originalDoc);
		//using the above list we need to traverse the originalDoc and remove the unused Clauses
		//removeUnwantedClauses(usedClauseIds, originalDoc);
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
		createUsedCQLArtifactsWithPopulationNames(originalDoc);
		return transform(originalDoc);
	}
	
	/*private static void createUsedCQLArtifactsWithPopulations(Document originalDoc){
		
		try {
			NodeList popClauseNodes = (NodeList) xPath.evaluate("/measure/measureGrouping//clause",
					originalDoc.getDocumentElement(), XPathConstants.NODESET);
			
			for(int i=0; i<popClauseNodes.getLength(); i++){
				Node popNode = popClauseNodes.item(i);
				String popString = popNode.getAttributes().getNamedItem("uuid").getNodeValue();
				
				if(popNode.hasChildNodes()){
					Node childNode = popNode.getFirstChild();
					String cqlAtrifactStr = childNode.getAttributes().getNamedItem("uuid").getNodeValue();
					usedPopulations.put(popString, cqlAtrifactStr);
				}
			}
			
			
			createUsedCQLArtifactsWithPopulationNames(originalDoc);
			
			if(checkForMultipleGrouping(originalDoc)){
				
			}
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}*/
	
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
	 * @param origionalDoc the origional doc
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
			if(newClauseNode.getAttributes().getNamedItem("displayName").
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
			}
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
	
	private static void createUsedCQLArtifactsWithPopulationNames(Document originalDoc) {
		
		try {
			NodeList paclkageClauseNodes = (NodeList) xPath.evaluate("/measure/measureGrouping//clause",
					originalDoc.getDocumentElement(), XPathConstants.NODESET);
			int count = 1;
			String prevseq = "";
			String prevPopString ="";
			for(int i=0; i<paclkageClauseNodes.getLength(); i++) {
				Node node = paclkageClauseNodes.item(i);
				String popUUIDString = node.getAttributes().getNamedItem("uuid").getNodeValue();
				String popTypeString = node.getAttributes().getNamedItem("type").getNodeValue();
				Node parentNode = node.getParentNode();
				String currsequence = "";
				String sequence = parentNode.getAttributes().getNamedItem("sequence").getNodeValue();
				//if multilple Grouping then sequence number is added else no sequence is added.
				if(checkForMultipleGrouping(originalDoc)){
					currsequence = sequence;
				} 
				NodeList popSeqList = (NodeList) xPath.evaluate("/measure/measureGrouping/group[@sequence='"+
						sequence+"']/clause[@type='"+popTypeString+"']",
						originalDoc.getDocumentElement(), XPathConstants.NODESET);
				/*NodeList stratPopSeqList = (NodeList) xPath.evaluate("/measure/measureGrouping/group[@sequence='"+
						sequence+"']/packageClause[@type='stratum']",
								originalDoc.getDocumentElement(), XPathConstants.NODESET);*/
				
				
				if(popSeqList.getLength()>1){
					
					if(!prevPopString.equalsIgnoreCase(popTypeString) && 
							!prevseq.equalsIgnoreCase(sequence)){
						count = 1;
					} 
					
					if(!currsequence.isEmpty()){
						currsequence = currsequence + "_"+count++;
					}
					else {
						currsequence = ""+count++;
					}
				} 
				
				/*if(popTypeString.equalsIgnoreCase("initialPopulation") && 
						checkifScoringRatio(originalDoc)){
					
				} else {
					if(!currsequence.isEmpty()){
						
					}
				}*/
				
				
				
//				if(popTypeString.equalsIgnoreCase("initialPopulation") && 
//						checkifScoringRatio(originalDoc)){
//					
//					if(prevseq.equals(currsequence) && initialPopSeqList.getLength()>1){
//						if(currsequence.isEmpty()){
//							currsequence = ""+count++;
//						} else {
//							currsequence = currsequence + "_"+ count++;
//						}
//					}
//					
//				} else if(popTypeString.equalsIgnoreCase("stratum")) {
//					
//					if(prevseq.equals(currsequence) && stratPopSeqList.getLength()>1){
//						if(currsequence.isEmpty()){
//							currsequence = ""+count++;
//						} else {
//							currsequence = currsequence + "_"+ count++;
//						}
//					} 
//				} else {
//					count = 1;
//					if(currsequence.isEmpty()){
//						currsequence = ""+count++;
//					} else {
//						currsequence = currsequence + "_"+ count++;
//					}
//				}
				
				prevseq = parentNode.getAttributes().getNamedItem("sequence").getNodeValue();
				prevPopString = popTypeString;
				
				Map<String, String> usedPopulations = new HashMap<String, String>();
				if(node.hasChildNodes()){
					Node childNode = node.getFirstChild();
					String cqlAtrifactStr = childNode.getAttributes().getNamedItem("uuid").getNodeValue();
					usedPopulations.put(popUUIDString, cqlAtrifactStr);
				} else if(checkPopulationByScoring(getScoringType(originalDoc), popTypeString)) {
					usedPopulations.put(popUUIDString, "");
				}
				
				createCQLArtifacts(originalDoc, node, getPopulationString(popTypeString), popUUIDString , currsequence, usedPopulations);
			}
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	
	private static String getScoringType(Document originalDoc){
		String XPATH_MEASURE_SCORING = "/measure/measureDetails/scoring/@id";
		Node scoringNode;
		String scoringType = "";
		try {
			scoringNode = (Node) xPath.evaluate(XPATH_MEASURE_SCORING,
					originalDoc.getDocumentElement(), XPathConstants.NODE);
			scoringType = scoringNode.getNodeValue();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return scoringType;
	}
	


	private static void createCQLArtifacts(Document originalDoc, Node childNode, String popTypeString, String popUUIDStr, String sequence, Map<String, String> usedPopulations){
		
		String artifactUUIDStr = usedPopulations.get(popUUIDStr);
		try {
			Node cqldefArtifactNode = (Node) xPath.evaluate("/measure/cqlLookUp/definitions",
					originalDoc.getDocumentElement(), XPathConstants.NODE);
			Node cqlArtifactNode = (Node) xPath.evaluate("/measure/cqlLookUp//node()[@id='"+artifactUUIDStr+"']",
					originalDoc.getDocumentElement(), XPathConstants.NODE);
			if(!sequence.isEmpty()){
				popTypeString = popTypeString + " " + sequence;
			}
			
			
			if(cqlArtifactNode!=null){
				String context = cqlArtifactNode.getAttributes().getNamedItem("context").getNodeValue();
				String defName = cqlArtifactNode.getAttributes().getNamedItem("name").getNodeValue();
				
				Node newDefNode = cqldefArtifactNode.getFirstChild().cloneNode(true);
				newDefNode.getAttributes().getNamedItem("context").setNodeValue(context);
				newDefNode.getAttributes().getNamedItem("id").setNodeValue(popUUIDStr);
				newDefNode.getAttributes().getNamedItem("supplDataElement").setNodeValue("false");
				newDefNode.getAttributes().getNamedItem("popDefinition").setNodeValue("true");
				newDefNode.getAttributes().getNamedItem("name").setNodeValue(popTypeString);
				if(newDefNode.hasChildNodes()){
					for(int i=0;i<newDefNode.getChildNodes().getLength();i++){
						Node logicNode = newDefNode.getChildNodes().item(i);
						if(logicNode.getNodeName().equals("logic")){
							logicNode.setTextContent(defName);
							break;
						}
					}
				}
				cqldefArtifactNode.appendChild(newDefNode);
			} else if(cqlArtifactNode==null && artifactUUIDStr!=null 
					&& artifactUUIDStr.isEmpty()){
				//String context = cqlArtifactNode.getAttributes().getNamedItem("context").getNodeValue();
				//String defName = cqlArtifactNode.getAttributes().getNamedItem("name").getNodeValue();
				
				Node newDefNode = cqldefArtifactNode.getFirstChild().cloneNode(true);
				newDefNode.getAttributes().getNamedItem("context").setNodeValue("patient");
				newDefNode.getAttributes().getNamedItem("id").setNodeValue(popUUIDStr);
				newDefNode.getAttributes().getNamedItem("supplDataElement").setNodeValue("false");
				newDefNode.getAttributes().getNamedItem("popDefinition").setNodeValue("true");
				newDefNode.getAttributes().getNamedItem("name").setNodeValue(popTypeString);
				if(newDefNode.hasChildNodes()){
					for(int i=0;i<newDefNode.getChildNodes().getLength();i++){
						Node logicNode = newDefNode.getChildNodes().item(i);
						if(logicNode.getNodeName().equals("logic")){
							logicNode.setTextContent("true");
							break;
						}
					}
				}
				cqldefArtifactNode.appendChild(newDefNode);
			}
			
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static boolean checkForMultipleGrouping(Document originalDoc){
		boolean isMeasureGroupingMul = false;
		try {
			NodeList usedDefinitionsUuids = (NodeList) xPath.evaluate("/measure//measureGrouping/group",
					originalDoc.getDocumentElement(), XPathConstants.NODESET);
			
			if(usedDefinitionsUuids.getLength()>1){
				isMeasureGroupingMul = true;
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isMeasureGroupingMul;
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
					Node parentNode = unUnsedDefineNodes.item(0).getParentNode();
					for(int i=0;i<unUnsedDefineNodes.getLength();i++){
						parentNode.removeChild(unUnsedDefineNodes.item(i));
					}
				}
				
				//to remove functions
				NodeList unUnsedFunctionNodes = (NodeList) xPath.evaluate(xPathForUnunsedFunctionsTreeNodes, originalDoc.getDocumentElement(), XPathConstants.NODESET);
				if(unUnsedFunctionNodes.getLength() > 0){
					Node parentNode = unUnsedFunctionNodes.item(0).getParentNode();
					for(int i=0;i<unUnsedFunctionNodes.getLength();i++){
						parentNode.removeChild(unUnsedFunctionNodes.item(i));
					}
				}
				
				NodeList unUnsedValueSetNodes = (NodeList) xPath.evaluate(xPathForUnunsedValueSetsTreeNodes, originalDoc.getDocumentElement(), XPathConstants.NODESET);
				if(unUnsedValueSetNodes.getLength() > 0){
					Node parentNode = unUnsedValueSetNodes.item(0).getParentNode();
					for(int i=0;i<unUnsedValueSetNodes.getLength();i++){
						parentNode.removeChild(unUnsedValueSetNodes.item(i));
					}
				}
				
				NodeList unUnsedParameterNodes = (NodeList) xPath.evaluate(xPathForUnunsedParametersTreeNodes, originalDoc.getDocumentElement(), XPathConstants.NODESET);
				if(unUnsedParameterNodes.getLength() > 0){
					Node parentNode = unUnsedParameterNodes.item(0).getParentNode();
					for(int i=0;i<unUnsedParameterNodes.getLength();i++){
						parentNode.removeChild(unUnsedParameterNodes.item(i));
					}
				}
				
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	
	private static List<String> getAllCQLDefnArtifacts(Document originalDoc, CQLFileObject cqlObject){
		List<String> usedAllCQLArtifacts = new ArrayList<String>();
		try {
			NodeList cqlDefRefIdsNodeList = (NodeList) xPath.evaluate("/measure//cqldefinition/@displayName",
					originalDoc.getDocumentElement(), XPathConstants.NODESET);
			
			for (int i = 0; i < cqlDefRefIdsNodeList.getLength(); i++) {
				Node cqlDefnNameAttributeNode = cqlDefRefIdsNodeList.item(i);
				
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
			NodeList cqlDefRefIdsNodeList = (NodeList) xPath.evaluate("/measure//cqlfunction/@displayName",
					originalDoc.getDocumentElement(), XPathConstants.NODESET);
			for (int i = 0; i < cqlDefRefIdsNodeList.getLength(); i++) {
				Node cqlfuncNameAttributeNode = cqlDefRefIdsNodeList.item(i);
				
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
	
	
	private static String getPopulationString(String str){
		String popString = "";
		if(str.equalsIgnoreCase("initialPopulation")){
			popString = "Initial Population";
		} else if(str.equalsIgnoreCase("denominator")){
			popString =  "Denominator";
		} else if(str.equalsIgnoreCase("denominatorExclusions")){
			popString = "Denominator Exclusion";
		} else if(str.equalsIgnoreCase("denominatorExceptions")){
			popString = "Denominator Exception";
		} else if(str.equalsIgnoreCase("numerator")){
			popString = "Numerator";
		} else if(str.equalsIgnoreCase("numeratorExclusions")){
			popString = "Numerator Exclusion";
		} else if(str.equalsIgnoreCase("measurePopulation")){
			popString = "Measure Population";
		} else if(str.equalsIgnoreCase("measurePopulationExclusions")){
			popString = "Measure Population Exclusion";
		} else if(str.equalsIgnoreCase("measureObservation")){
			popString = "Measure Observation";
		} else if(str.equalsIgnoreCase("startum")){
			popString = "Stratification";
		}
		return popString;
	}
	
	private static boolean checkPopulationByScoring(String scoringType, String population){
		boolean isReqPopulation = false;
		if (SCORING_TYPE_CONTVAR.equals(scoringType)) {
			if(population.equalsIgnoreCase("initialPopulation") || 
				population.equalsIgnoreCase("measurePopulation") || 
					population.equalsIgnoreCase("measureObservation")){
				return true;
				}
		} else if (RATIO.equals(scoringType) || PROPOR.equals(scoringType)) {
			if(population.equalsIgnoreCase("initialPopulation") || 
					population.equalsIgnoreCase("denominator") || 
						population.equalsIgnoreCase("numerator")) {
					return true;
				}
		} else if (COHORT.equals(scoringType)) {
			if(population.equalsIgnoreCase("initialPopulation")){
					return true;
				}
		} 
		
		/*else if (PROPOR.equals(scoringType)) {
			if(population.equalsIgnoreCase("initialPopulation") || 
					population.equalsIgnoreCase("denominator") || 
						population.equalsIgnoreCase("numerator")){
					return true;
				}
		}*/
		
		
		return isReqPopulation;
	}
	
}
