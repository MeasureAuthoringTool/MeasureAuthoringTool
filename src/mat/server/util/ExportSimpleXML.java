package mat.server.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import mat.client.shared.MatContext;
import mat.dao.clause.MeasureDAO;
import mat.model.clause.Measure;
import mat.model.clause.MeasureXML;
import mat.shared.UUIDUtilClient;
import net.sf.saxon.TransformerFactoryImpl;

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
	
	/** The Constant MEASUREMENT_END_DATE_OID. */
	private static final String MEASUREMENT_END_DATE_OID = "2.16.840.1.113883.3.67.1.101.1.55";
	
	/** The Constant MEASUREMENT_START_DATE_OID. */
	private static final String MEASUREMENT_START_DATE_OID = "2.16.840.1.113883.3.67.1.101.1.54";
	
	/** The Constant MEASUREMENT_PERIOD_OID. */
	private static final String MEASUREMENT_PERIOD_OID = "2.16.840.1.113883.3.67.1.101.1.53";
	
	/** The measure_ id. */
	private static String measure_Id;
	
	/**
	 * Export.
	 * 
	 * @param measureXMLObject
	 *            the measure xml object
	 * @param message
	 *            the message
	 * @param measureDAO TODO
	 * @return the string
	 */
	public static String export(MeasureXML measureXMLObject, List<String> message, MeasureDAO measureDAO) {
		String exportedXML = "";
		//Validate the XML
		Document measureXMLDocument;
		try {
			measureXMLDocument = getXMLDocument(measureXMLObject);
			/*if(validateMeasure(measureXMLDocument, message)){*/
				measure_Id = measureXMLObject.getMeasure_id();
				exportedXML = generateExportedXML(measureXMLDocument, measureDAO, measure_Id);
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
	 * This method will check If this measure has at-least one grouping. In
	 * addition, it will make sure that all the <relationalOp> tags have exactly
	 * 2 children.
	 * 
	 * @param measureXMLDocument
	 *            the measure xml document
	 * @param message
	 *            the message
	 * @return true, if successful
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private static boolean validateMeasure(Document measureXMLDocument, List<String> message) throws XPathExpressionException{
		boolean isValid = true;
		XPathExpression expr = xPath.compile("boolean(" + XmlProcessor.XPATH_MEASURE_GROUPING_GROUP + ")");
		if(!(Boolean) expr.evaluate(measureXMLDocument, XPathConstants.BOOLEAN)){
			message.add(MatContext.get().getMessageDelegate().getGroupingRequiredMessage());
			isValid = false;
		}else{		
			isValid = checkForValidRelationalOps(measureXMLDocument, message);
		}		
		return isValid;
	}
	
	/**
	 * Go through all <relationalOp> tags in the XML and verify that each has
	 * exactly 2 children. Stop the search the moment you find a <relationalOp>
	 * having less than or greater than 2 children.
	 * 
	 * @param measureXMLDocument
	 *            the measure xml document
	 * @param message
	 *            the message
	 * @return true, if successful
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private static boolean checkForValidRelationalOps(Document measureXMLDocument,
			List<String> message) throws XPathExpressionException {
		boolean retValue = true;
		NodeList allRelationalOps = (NodeList) xPath.evaluate("/measure//relationalOp", measureXMLDocument, XPathConstants.NODESET);
		for(int i=0;i<allRelationalOps.getLength();i++){
			Node relationalOpNode = allRelationalOps.item(i);
			if (relationalOpNode.getChildNodes().getLength() != 2){
				message.add(MatContext.get().getMessageDelegate().getRelationalOpTwoChildMessage());
				retValue = false;
				break;
			}
		}
		return retValue;
	}

	/**
	 * This will work with the existing Measure XML & assume that it is correct
	 * and validated to generate the exported XML.
	 * 
	 * @param measureXMLDocument
	 *            the measure xml document
	 * @param measureDAO TODO
	 * @param measure_Id TODO
	 * @return the string
	 */
	private static String generateExportedXML(Document measureXMLDocument, MeasureDAO measureDAO, String measure_Id) {
		_logger.info("In ExportSimpleXML.generateExportedXML()");
		try {
			return traverseXML(measureXMLDocument, measureDAO, measure_Id);
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
	 * @param originalDoc
	 *            the original doc
	 * @param MeasureDAO TODO
	 * @param measure_Id TODO
	 * @return the string
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private static String traverseXML(Document originalDoc, MeasureDAO MeasureDAO, String measure_Id) throws XPathExpressionException {
		//set attributes
		updateVersionforMeasureDetails(originalDoc, MeasureDAO, measure_Id);
		setAttributesForComponentMeasures(originalDoc, MeasureDAO);
		List<String> usedClauseIds = getUsedClauseIds(originalDoc);
		
		//using the above list we need to traverse the originalDoc and remove the unused Clauses
		removeUnwantedClauses(usedClauseIds, originalDoc);
		//to get SubTreeRefIds from Population WorkSpace
		List<String> usedSubtreeRefIds = getUsedSubtreeRefIds(originalDoc);
		
		//to get SubTreeIds From Clause WorksPace in a Whole
		List<String> usedSubTreeIds = checkUnUsedSubTreeRef(usedSubtreeRefIds, originalDoc);
	   
		//this will remove unUsed SubTrees From SubTreeLookUp
		removeUnwantedSubTrees(usedSubTreeIds, originalDoc);
		
		List<String> usedQDMIds = getUsedQDMIds(originalDoc);
		//using the above list we need to traverse the originalDoc and remove the unused QDM's
		removeUnWantedQDMs(usedQDMIds, originalDoc);
		expandAndHandleGrouping(originalDoc);
		addUUIDToFunctions(originalDoc);
		//modify the <startDate> and <stopDate> tags to have date in YYYYMMDD format
		modifyHeaderStart_Stop_Dates(originalDoc);
		modifyElementLookUpForOccurances(originalDoc);
		return transform(originalDoc);
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
		String xPathForComponentMeasureIds = "/measure//componentMeasures/measure";
		NodeList componentMeasureIdList = (NodeList) xPath.evaluate(xPathForComponentMeasureIds, originalDoc, XPathConstants.NODESET);
		if(componentMeasureIdList !=null && componentMeasureIdList.getLength() >0){
			for(int i=0; i<componentMeasureIdList.getLength(); i++){
				Node measureNode = componentMeasureIdList.item(i);
				measureId = measureNode.getAttributes().getNamedItem("id").getNodeValue();
				Node attrcomponentMeasureName = originalDoc.createAttribute("name");		
				Node attrcomponentMeasureSetId = originalDoc.createAttribute("measureSetId");
				Node attrcomponentVersionNo= originalDoc.createAttribute("versionNo");
				Measure measure = measureDAO.find(measureId);
				componentMeasureName = measure.getDescription();
				componentMeasureSetId = measure.getMeasureSet().getId();
				
				attrcomponentMeasureName.setNodeValue(componentMeasureName);
				attrcomponentMeasureSetId.setNodeValue(componentMeasureSetId);
				attrcomponentVersionNo.setNodeValue(measure.getMajorVersionStr()+"."+measure.getMinorVersionStr());
				
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
		if(usedSubTreeIds !=null && usedSubTreeIds.size()>0){
			
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
		    System.out.println("usedSubTreeIds are empty so removed from the SimpeXml");
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
			System.out.println("Instance:"+instance);
			if(qdmOID_Datatype_List.contains(datatype + oid)){
				continue;
			}else{
				qdmOID_Datatype_List.add(datatype + oid);
			}
			System.out.println("/measure/elementLookUp/qdm[@datatype="+datatype+"][@oid="+oid+"][not(@instance)]");
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
		//System.out.println(arrayOutputStream.toString());
		return arrayOutputStream.toString();
	}

	/**
	 * Removes the un wanted qd ms.
	 * 
	 * @param usedQDMIds
	 *            the used qdm ids
	 * @param originalDoc
	 *            the original doc
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private static void removeUnWantedQDMs(List<String> usedQDMIds, Document originalDoc) throws XPathExpressionException {		
		NodeList allQDMIDs = (NodeList) xPath.evaluate("/measure/elementLookUp/qdm/@uuid", originalDoc.getDocumentElement(), XPathConstants.NODESET);
		
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

				Node clauseNode = findClauseByUUID(uuid, type, originalDoc);
				
				if("stratification".equals(clauseNode.getNodeName())){
					NodeList stratificationClauses = clauseNode.getChildNodes();
					
					for(int h=0;h<stratificationClauses.getLength();h++){
						Node stratificationClause = stratificationClauses.item(h);
						//add childCount to clauseNode
						if(packageClause.getChildNodes()!=null && packageClause.getChildNodes().getLength()>0){				
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
					if(packageClause.getChildNodes()!=null && packageClause.getChildNodes().getLength()>0){				
						Node itemCount = packageClause.getChildNodes().item(0);
						Node clonedItemCount = itemCount.cloneNode(true);
						clauseNode.appendChild(clonedItemCount);
					}

					//add associatedPopulationUUID to clauseNode
					if(type.equalsIgnoreCase("denominator") || type.equalsIgnoreCase("numerator")|| type.equalsIgnoreCase("measureObservation")){
						Node hasAssociatedPopulationUUID = packageClause.getAttributes().getNamedItem("associatedPopulationUUID");
						if(hasAssociatedPopulationUUID != null && !hasAssociatedPopulationUUID.toString().isEmpty()){
							String associatedPopulationUUID = hasAssociatedPopulationUUID.getNodeValue();
							Node attr = originalDoc.createAttribute("associatedPopulationUUID");
							attr.setNodeValue(associatedPopulationUUID);
							clauseNode.getAttributes().setNamedItem(attr);
						}
					}

					//deep clone the <clause> tag
					Node clonedClauseNode = clauseNode.cloneNode(true);

					//set a new 'uuid' attribute value for <clause>
					String cureUUID = UUIDUtilClient.uuid();
					clonedClauseNode.getAttributes().getNamedItem("uuid").setNodeValue(cureUUID);
					//				String clauseName = clonedClauseNode.getAttributes().getNamedItem("displayName").getNodeValue();  
					//set a new 'displayName' for <clause> 
					//				clonedClauseNode.getAttributes().getNamedItem("displayName").setNodeValue(clauseName+"_"+groupSequence);

					//modify associcatedUUID
					modifyAssociatedPOPID(uuid, cureUUID,groupSequence, originalDoc);
					clauseNodes.add(clonedClauseNode);
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

	private static void addMissingEmptyClauses(NodeList groupNodes,
			Document originalDoc) throws DOMException, XPathExpressionException {
		
		List<String> clauseList = new ArrayList<String>();
		List<String> existingClauses = new ArrayList<String>();
		Node node = (Node)xPath.evaluate("/measure/measureDetails/scoring", 
				originalDoc.getDocumentElement(), XPathConstants.NODE);
		Node groupNode;
		Node childNode;
		clauseList = getRequiredClauses(node.getTextContent());
		
		for(int i = 0; i< groupNodes.getLength(); i++){
			groupNode = groupNodes.item(i);
			NodeList children = groupNode.getChildNodes();
			System.out.println(children.getLength());
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

	private static void generateClauseNode(Node groupNode, String type,Document origionalDoc) {
		// TODO Auto-generated method stub
		Node newClauseNode = groupNode.getFirstChild().cloneNode(true);
		newClauseNode.getAttributes().getNamedItem("displayName").setNodeValue(type);
		newClauseNode.getAttributes().getNamedItem("type").setNodeValue(type);
		newClauseNode.getAttributes().getNamedItem("uuid").setNodeValue(UUID.randomUUID().toString());

		NodeList logicalNode = newClauseNode.getChildNodes();

		for(int i = 0; i<logicalNode.getLength();i++){
			Node innerNode = logicalNode.item(i);
			System.out.println(innerNode.getNodeName());
			NodeList innerNodeChildren = innerNode.getChildNodes();
			int length =  innerNodeChildren.getLength();
			for(int j = length - 1; j>-1; j--){
				Node child = innerNodeChildren.item(j);
				System.out.println("J= " + j);
				System.out.println("REMOVING THIS NODE " + child.getNodeName() + " " + j);
				innerNode.removeChild(child);
			}
		}
		groupNode.appendChild(newClauseNode);
	}

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
	 * This method will go through all the <group> tags and within rearrange
	 * <clause> tags to have the <clause type="denominator"> as the 2nd to last
	 * <clause> element and <clause type="initialPatientPopulation"> as the last
	 * <clause> in a <group>. This is being done to aid the final export to
	 * eMeasure XML.
	 * 
	 * @param originalDoc
	 *            the original doc
	 * @return the used clause ids
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	/*private static void reArrangeClauseNodes(Document originalDoc) throws XPathExpressionException {
		NodeList groupNodes = (NodeList)xPath.evaluate("/measure/measureGrouping/group", 
				originalDoc.getDocumentElement(), XPathConstants.NODESET);
		
		for(int i=0;i<groupNodes.getLength();i++){
			List<Node> otherClauses = new ArrayList<Node>();
			Node denomNode = null;
			Node ippNode = null;
			Node groupNode = groupNodes.item(i);
			NodeList clauseNodes = groupNode.getChildNodes();
			for(int j=0;j<clauseNodes.getLength();j++){
				Node clauseNode = clauseNodes.item(j);
				String type = clauseNode.getAttributes().getNamedItem("type").getNodeValue();
				if("denominator".equals(type)){
					denomNode = clauseNode;
				}else if("initialPatientPopulation".equals(type)){
					ippNode = clauseNode;					
				}else{
					otherClauses.add(clauseNode);
				}
			}
			//finally remove the all the <packageClause> tags from <group>
			for(int k=clauseNodes.getLength();k>0;k--){
				groupNode.removeChild(clauseNodes.item(0));
			}
			for(Node nod:otherClauses){
				groupNode.appendChild(nod);
			}
			if(denomNode != null){
				groupNode.appendChild(denomNode);
			}
			if(ippNode != null){
				groupNode.appendChild(ippNode);
			}
		}
	}*/

	private static List<String> getUsedClauseIds(Document originalDoc) throws XPathExpressionException {
		List<String> usedClauseIds = new ArrayList<String>();
		
		NodeList groupedClauseIdsNodeList = (NodeList)xPath.evaluate("/measure/measureGrouping/group/packageClause/@uuid", 
				originalDoc.getDocumentElement(), XPathConstants.NODESET);
		
		for(int i=0;i<groupedClauseIdsNodeList.getLength();i++){
			Node groupedClauseIdAttributeNode = groupedClauseIdsNodeList.item(i);
			usedClauseIds.add(groupedClauseIdAttributeNode.getNodeValue());
		}
		_logger.info("usedClauseIds:"+usedClauseIds);
		return usedClauseIds;
	}
	
	/**
	 * Gets the used subtree ref ids.
	 *
	 * @param originalDoc the original doc
	 * @return the used subtree ref ids
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static List<String> getUsedSubtreeRefIds(Document originalDoc)
			throws XPathExpressionException {
		// Populations
		List<String> usedSubTreeRefIdsPop = new ArrayList<String>();
		NodeList groupedSubTreeRefIdsNodeListPop = (NodeList) xPath.evaluate(
				"/measure/populations//subTreeRef/@id",
				originalDoc.getDocumentElement(), XPathConstants.NODESET);

		for (int i = 0; i < groupedSubTreeRefIdsNodeListPop.getLength(); i++) {
			Node groupedSubTreeRefIdAttributeNodePop = groupedSubTreeRefIdsNodeListPop
					.item(i);
			usedSubTreeRefIdsPop.add(groupedSubTreeRefIdAttributeNodePop
					.getNodeValue());
		}

		// Measure Observations
		List<String> usedSubTreeRefIdsMO = new ArrayList<String>();
		NodeList groupedSubTreeRefIdsNodeListMO = (NodeList) xPath.evaluate(
				"/measure/measureObservations//subTreeRef/@id",
				originalDoc.getDocumentElement(), XPathConstants.NODESET);

		for (int i = 0; i < groupedSubTreeRefIdsNodeListMO.getLength(); i++) {
			Node groupedSubTreeRefIdAttributeNodeMO = groupedSubTreeRefIdsNodeListMO
					.item(i);
			usedSubTreeRefIdsMO.add(groupedSubTreeRefIdAttributeNodeMO
					.getNodeValue());
		}

		// Stratifications
		List<String> usedSubTreeRefIdsStrat = new ArrayList<String>();

		NodeList groupedSubTreeRefIdListStrat = (NodeList) xPath.evaluate(
				"/measure/strata/stratification//subTreeRef/@id",
				originalDoc.getDocumentElement(), XPathConstants.NODESET);

		for (int i = 0; i < groupedSubTreeRefIdListStrat.getLength(); i++) {
			Node groupedSubTreeRefIdAttributeNodeStrat = groupedSubTreeRefIdListStrat
					.item(i);
			usedSubTreeRefIdsStrat.add(groupedSubTreeRefIdAttributeNodeStrat
					.getNodeValue());
		}

		//if (usedSubTreeRefIdsPop != null || usedSubTreeRefIdsMO != null) {
			usedSubTreeRefIdsPop.removeAll(usedSubTreeRefIdsMO);
			usedSubTreeRefIdsMO.addAll(usedSubTreeRefIdsPop);
			
			//if (usedSubTreeRefIdsStrat != null) {
				usedSubTreeRefIdsMO.removeAll(usedSubTreeRefIdsStrat);
				usedSubTreeRefIdsStrat.addAll(usedSubTreeRefIdsMO);
			//}
		//}

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
			Node SubTreeRefIdAttributeNode = subTreeRefIdsNodeList.item(i);
			if(!allSubTreeRefIds.contains(SubTreeRefIdAttributeNode.getNodeValue())){
				allSubTreeRefIds.add(SubTreeRefIdAttributeNode.getNodeValue());
			}
		}
		allSubTreeRefIds.removeAll(usedSubTreeRefIds);
		
		for(int i = 0; i< usedSubTreeRefIds.size(); i++){
			for(int j=0; j<allSubTreeRefIds.size(); j++){
				Node usedSubTreeRefNode = (Node) xPath.evaluate("/measure/subTreeLookUp/subTree[@uuid='"+ 
			                   usedSubTreeRefIds.get(i)+ "']//subTreeRef[@id='"+allSubTreeRefIds.get(j)+"']",
						originalDoc.getDocumentElement(), XPathConstants.NODE);
				if(usedSubTreeRefNode!=null){
					if(!usedSubTreeRefIds.contains(allSubTreeRefIds.get(j))){
					usedSubTreeRefIds.add(allSubTreeRefIds.get(j));
					}
				}
			}
			
		}
		
		return usedSubTreeRefIds;
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
	 * This method will look inside <strata>/<clause>/<logicalOp> and if it
	 * finds a <logicalOp> without any children. then remove the <clause> node.
	 * After that it will check the <strata> node and if it is empty then remove
	 * the <strata> tag completely.
	 * 
	 * @param measureXMLObject
	 *            the measure xml object
	 * @return the xML document
	 * @throws ParserConfigurationException
	 *             the parser configuration exception
	 * @throws SAXException
	 *             the sAX exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	/*private static void removeBlankStratificationClauses(Document originalDoc) throws XPathExpressionException {
		NodeList strataLogicalOpNodes = (NodeList)xPath.evaluate("/measure/strata/clause/logicalOp", originalDoc, XPathConstants.NODESET);
		boolean childRemoved = false;
		for(int i=0;i<strataLogicalOpNodes.getLength();i++){
			Node strataLogicalOpNode = strataLogicalOpNodes.item(i);
			if(strataLogicalOpNode.getChildNodes().getLength() == 0){
				Node strataClauseNode = strataLogicalOpNode.getParentNode();
				Node strataNode = strataClauseNode.getParentNode();
				strataNode.removeChild(strataClauseNode);
				childRemoved = true;
			}
		}
		
		*//**
		 * If the <strata> tag now has no children remove the <strata> tag.
		 *//*
		if(childRemoved){
			Node strataNode = (Node) xPath.evaluate("/measure/strata", originalDoc,XPathConstants.NODE);
			if(strataNode.getChildNodes().getLength() == 0){
				Node measurenode = strataNode.getParentNode();
				measurenode.removeChild(strataNode);
			}
		}
	}*/
	
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
		Node measurementPeriodStartDateNode = (Node)xPath.evaluate("/measure/elementLookUp/qdm[@oid='"+ MEASUREMENT_START_DATE_OID + "']",originalDoc, 
				XPathConstants.NODE);
		Node measurementPeriodEndDateNode = (Node)xPath.evaluate("/measure/elementLookUp/qdm[@oid='"+ MEASUREMENT_END_DATE_OID + "']",originalDoc, 
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
					if(measurementPeriodStartDateNode != null){
						node.getAttributes().getNamedItem("uuid").setNodeValue(measurementPeriodStartDateNode.getAttributes().
								getNamedItem("uuid").getNodeValue());
					}
				}else if("stopDate".equals(node.getNodeName())){
					//Date in MM/DD/YYYY
					String value = node.getTextContent();
					node.setTextContent(formatDate(value));
					if(measurementPeriodEndDateNode != null){
						node.getAttributes().getNamedItem("uuid").setNodeValue(measurementPeriodEndDateNode.getAttributes().
								getNamedItem("uuid").getNodeValue());
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
		   
		   if(year.length() != 4 || year.toLowerCase().indexOf("x") > -1){
		    year = "0000";
		   }
		   dateString = year + month + dt;
		  }catch (Exception e) {
		   _logger.info("Bad Start/Stop dates in Measure Details."+e.getMessage());
		  }
		  return dateString;

		
		
//		return date.substring(6) + date.substring(0,2) + date.substring(3,5);
		
		
//		Calendar cal = Calendar.getInstance();
//		cal.setLenient(false);
//		DateFormat oldDateFormat = new SimpleDateFormat("MM/dd/yyyy");
//		DateFormat newDateFormat = new SimpleDateFormat("yyyyMMdd");
//		try {
//			Date oldParsedDate = oldDateFormat.parse(date);
//			cal.setTime(oldParsedDate);
//			System.out.println("returning date1:"+newDateFormat.format(oldParsedDate));
//			return newDateFormat.format(oldParsedDate);
//		} catch (ParseException e) {
//			//Parse the dates with substring (these might contain the notorious xxx's
//			e.printStackTrace();
//			System.out.println("returning date2:"+date.substring(6) + date.substring(0,2) + date.substring(3,5));
//			return date.substring(6) + date.substring(0,2) + date.substring(3,5);
//		}
	}
	
}