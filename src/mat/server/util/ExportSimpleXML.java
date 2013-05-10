package mat.server.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import net.sf.saxon.TransformerFactoryImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import mat.client.shared.MatContext;
import mat.model.clause.MeasureXML;
import mat.shared.UUIDUtilClient;

public class ExportSimpleXML {

	private static final String STRATA = "strata";
	private static final Log _logger = LogFactory.getLog(ExportSimpleXML.class);
	private static final javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
	
	public static String export(MeasureXML measureXMLObject, List<String> message) {
		String exportedXML = "";
		//Validate the XML
		Document measureXMLDocument;
		try {
			measureXMLDocument = getXMLDocument(measureXMLObject);
			if(validateMeasure(measureXMLDocument, message)){
				exportedXML = generateExportedXML(measureXMLDocument);
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}		
		return exportedXML;
	}
	
	private static boolean validateMeasure(Document measureXMLDocument, List<String> message) throws XPathExpressionException{
		boolean isValid = true;
		XPathExpression expr = xPath.compile("boolean(" + XmlProcessor.XPATH_MEASURE_GROUPING_GROUP + ")");
		if(!(Boolean) expr.evaluate(measureXMLDocument, XPathConstants.BOOLEAN)){
			message.add(MatContext.get().getMessageDelegate().getGroupingRequiredMessage());
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 * This will work with the existing Measure XML & assume that it is correct and 
	 * validated to generate the exported XML. 
	 * @param measureXMLDocument
	 * @return 
	 */
	private static String generateExportedXML(Document measureXMLDocument) {
		_logger.info("In ExportSimpleXML.generateExportedXML()");
		try {
			return traverseXML(measureXMLDocument);
		} catch (Exception e) {
			_logger.info("Exception thrown on ExportSimpleXML.generateExportedXML()");
			e.printStackTrace();
		}
		return "";
	}
	
	//This will walk through the original Measure XML and generate the Measure Export XML.
	private static String traverseXML(Document originalDoc) throws XPathExpressionException {		
		List<String> usedQDMIds = getUsedQDMIds(originalDoc);
		List<String> usedClauseIds = getUsedClauseIds(originalDoc);
		
		//using the above 2 lists we need to traverse the originalDoc and remove the unnecessary nodes
		removeBlankStratificationClauses(originalDoc);
		removeUnwantedClauses(usedClauseIds, originalDoc);
		removeUnWantedQDMs(usedQDMIds, originalDoc);
		expandAndHandleGrouping(originalDoc);
		//modify the <startDate> and <stopDate> tags to have date in YYYYMMDD format
		modifyHeaderStart_Stop_Dates(originalDoc);
		
		return transform(originalDoc);
	}
	
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
		System.out.println(arrayOutputStream.toString());
		return arrayOutputStream.toString();
	}

	private static void removeUnWantedQDMs(List<String> usedQDMIds, Document originalDoc) throws XPathExpressionException {		
		NodeList allQDMIDs = (NodeList) xPath.evaluate("/measure/elementLookUp/qdm/@id", originalDoc.getDocumentElement(), XPathConstants.NODESET);
		
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
	 * This method will search for <clause> tags in the XML and check if the UUID attribute matches the usedClauseIds list.
	 * If not removes the <clause> tag from its parent.
	 * @param usedClauseIds
	 * @param originalDoc
	 * @throws XPathExpressionException
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
				if(!STRATA.equals(clauseParentNode.getNodeName())){
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
	 * This method will go through individual <group> tags and each <packageClause> child.
	 * For each <packageClause> it will copy the original <clause> to <group> and remove the 
	 * <packageClause> tag.
	 * Finally, at the end of the method it will remove the <populations> and <measureObservations>
	 * tags from the document.
	 * @param originalDoc
	 * @throws XPathExpressionException
	 */
	private static void expandAndHandleGrouping(Document originalDoc) throws XPathExpressionException {
		Node measureGroupingNode = (Node)xPath.evaluate("/measure/measureGrouping", 
				originalDoc.getDocumentElement(), XPathConstants.NODE);
		
		NodeList groupNodes = measureGroupingNode.getChildNodes();
		for(int j=0;j<groupNodes.getLength();j++){
			Node groupNode = groupNodes.item(j);
			String groupSequence = groupNode.getAttributes().getNamedItem("sequence").getNodeValue();
			NodeList packageClauses = groupNode.getChildNodes();
			List<Node> clauseNodes = new ArrayList<Node>();
			for(int i=0;i<packageClauses.getLength();i++){
				Node packageClause = packageClauses.item(i);
				String uuid = packageClause.getAttributes().getNamedItem("uuid").getNodeValue();
				Node clauseNode = findClauseByUUID(uuid,originalDoc);
				//deep clone the <clause> tag
				Node clonedClauseNode = clauseNode.cloneNode(true);
				//set a new 'uuid' attribute value for <clause>
				clonedClauseNode.getAttributes().getNamedItem("uuid").setNodeValue(UUIDUtilClient.uuid());
				String clauseName = clonedClauseNode.getAttributes().getNamedItem("displayName").getNodeValue();  
				//set a new 'displayName' for <clause> 
				clonedClauseNode.getAttributes().getNamedItem("displayName").setNodeValue(clauseName+"_"+groupSequence);
				clauseNodes.add(clonedClauseNode);
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
		removeNode("/measure/populations",originalDoc);
		removeNode("/measure/measureObservations",originalDoc);
	}

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

	private static List<String> getUsedQDMIds(Document originalDoc) throws XPathExpressionException {
		List<String> usedQDMIds = new ArrayList<String>();
		NodeList elementRefNodeList = (NodeList)xPath.evaluate("/measure//elementRef", originalDoc, XPathConstants.NODESET);
		for(int i=0;i<elementRefNodeList.getLength();i++){
			Node elementRefNode = elementRefNodeList.item(i);
			Node idAttributeNode = elementRefNode.getAttributes().getNamedItem("id");
			usedQDMIds.add(idAttributeNode.getNodeValue());
		}
		_logger.info("usedQDMIds:"+usedQDMIds);
		return usedQDMIds;
	}
	
	/**
	 * This method will look inside <strata>/<clause>/<logicalOp> and if if finds a <logicalOp> without any children.
	 * then remove the <clause> node.
	 * After that it will check the <strata> node and if it is empty then remove the <strata> tag completely. 
	 * @param originalDoc
	 * @throws XPathExpressionException 
	 */
	private static void removeBlankStratificationClauses(Document originalDoc) throws XPathExpressionException {
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
		
		/**
		 * If the <strata> tag now has no children remove the <strata> tag.
		 */
		if(childRemoved){
			Node strataNode = (Node) xPath.evaluate("/measure/strata", originalDoc,XPathConstants.NODE);
			if(strataNode.getChildNodes().getLength() == 0){
				Node measurenode = strataNode.getParentNode();
				measurenode.removeChild(strataNode);
			}
		}
	}
	
	private static Document getXMLDocument(MeasureXML measureXMLObject) throws ParserConfigurationException, SAXException, IOException{
		//Create Document object
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputSource oldXmlstream = new InputSource(new StringReader(measureXMLObject.getMeasureXMLAsString()));
		Document originalDoc = docBuilder.parse(oldXmlstream);
		return originalDoc;
	}
	
	/**
	 * This method finds a <clause> tag with a specified 'uuid' attribute.
	 * @param uuid
	 * @param originalDoc
	 * @return
	 * @throws XPathExpressionException
	 */
	private static Node findClauseByUUID(String uuid, Document originalDoc) throws XPathExpressionException {
		Node clauseNode = null;		
		clauseNode = (Node)xPath.evaluate("/measure//clause[@uuid='"+uuid+"']", originalDoc,XPathConstants.NODE);		
		return clauseNode;
	}
	
	/**
	 * Takes an XPath notation String for a particular tag and a Document object and finds and removes the tag
	 * from the document. 
	 * @param nodeXPath
	 * @param originalDoc
	 * @throws XPathExpressionException 
	 */
	private static void removeNode(String nodeXPath, Document originalDoc) throws XPathExpressionException {
		Node node = (Node)xPath.evaluate(nodeXPath, originalDoc.getDocumentElement(), XPathConstants.NODE);
		if(node != null){
			Node parentNode = node.getParentNode();
			parentNode.removeChild(node);
		}
	}
	
	/**
	 * We need to modify <startDate> and <stopDate> inside <measureDetails>/<period> to have YYYYMMDD format
	 * @param originalDoc
	 * @throws XPathExpressionException 
	 */
	private static void modifyHeaderStart_Stop_Dates(Document originalDoc) throws XPathExpressionException {
		Node periodNode = (Node)xPath.evaluate("/measure/measureDetails/period", originalDoc, XPathConstants.NODE);
		if(periodNode != null){
			NodeList childNodeList = periodNode.getChildNodes();
			for(int i=0;i<childNodeList.getLength();i++){
				Node node = childNodeList.item(i);
				if("startDate".equals(node.getNodeName())){
					//Date in MM/DD/YYYY
					String value = node.getTextContent();
					node.setTextContent(formatDate(value));
				}else if("stopDate".equals(node.getNodeName())){
					//Date in MM/DD/YYYY
					String value = node.getTextContent();
					node.setTextContent(formatDate(value));
				}
			}
		}
	}
	
	/**
	 * This method will expect Date String in MM/DD/YYYY format
	 * And convert it to YYYYMMDD format.
	 * @param date
	 * @return
	 */
	private static String formatDate(String date){
		Calendar cal = Calendar.getInstance();
		cal.setLenient(true);
		DateFormat oldDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		DateFormat newDateFormat = new SimpleDateFormat("yyyyMMdd");
		try {
			Date oldParsedDate = oldDateFormat.parse(date);
			cal.setTime(oldParsedDate);
			return newDateFormat.format(oldParsedDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}