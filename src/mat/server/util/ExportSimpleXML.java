package mat.server.util;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
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

import mat.model.clause.MeasureXML;

public class ExportSimpleXML {

	private static final Log _logger = LogFactory.getLog(ExportSimpleXML.class);
	
	public static String export(MeasureXML measureXMLObject) {
		String exportedXML = "";
		//Validate the XML
		if(validateMeasure(measureXMLObject)){
			exportedXML = generateExportedXML(measureXMLObject.getMeasureXMLAsString());
		}
		return exportedXML;
	}
	
	/**
	 * This will work with the existing Measure XML & assume that it is correct and 
	 * validated to generate the exported XML. 
	 * @param measureXMLAsString
	 * @return 
	 */
	private static String generateExportedXML(String measureXMLAsString) {
		_logger.info("In ExportSimpleXML.generateExportedXML()");
		try {
			//Create Document object
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource oldXmlstream = new InputSource(new StringReader(measureXMLAsString));
			Document originalDoc = docBuilder.parse(oldXmlstream);
			_logger.info("Created DOM for the XML String");
			
			return traverseXML(originalDoc);
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
		removeUnwantedClauses(usedClauseIds, originalDoc);
		removeUnWantedQDMs(usedQDMIds, originalDoc);
		removeGrouping(originalDoc);
		
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
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
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
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
		NodeList allClauseIDs = (NodeList) xPath.evaluate("/measure//clause/@uuid", originalDoc.getDocumentElement(), XPathConstants.NODESET);
		
		for(int i=0;i<allClauseIDs.getLength();i++){
			Node clauseIdNode = allClauseIDs.item(i);
			String clauseNodeUuid = clauseIdNode.getNodeValue();
			System.out.println("clauseNodeUuid:"+clauseNodeUuid);
			
			if(!usedClauseIds.contains(clauseNodeUuid)){
				Node clauseNode = ((Attr)clauseIdNode).getOwnerElement();
				Node clauseParentNode = clauseNode.getParentNode();
				clauseParentNode.removeChild(clauseNode);
			}
		}
	}
	
	/**
	 * <measureGrouping> element is not needed in Exported XMl for Measure. Remove it.
	 * @param originalDoc
	 * @throws XPathExpressionException
	 */
	private static void removeGrouping(Document originalDoc) throws XPathExpressionException {
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
		Node measureGroupingNode = (Node)xPath.evaluate("/measure/measureGrouping", 
				originalDoc.getDocumentElement(), XPathConstants.NODE);
		
		if(measureGroupingNode != null){
			Node parentNode = measureGroupingNode.getParentNode();
			parentNode.removeChild(measureGroupingNode);
		}
	}

	private static List<String> getUsedClauseIds(Document originalDoc) throws XPathExpressionException {
		List<String> usedClauseIds = new ArrayList<String>();
		
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
		NodeList groupedClauseIdsNodeList = (NodeList)xPath.evaluate("/measure/measureGrouping/group/packageClause/@uuid", 
				originalDoc.getDocumentElement(), XPathConstants.NODESET);
		
		for(int i=0;i<groupedClauseIdsNodeList.getLength();i++){
			Node groupedClauseIdAttributeNode = groupedClauseIdsNodeList.item(i);
			usedClauseIds.add(groupedClauseIdAttributeNode.getNodeValue());
		}
		_logger.info("usedClauseIds:"+usedClauseIds);
		return usedClauseIds;
	}

	private static List<String>  getUsedQDMIds(Document originalDoc) {
		List<String> usedQDMIds = new ArrayList<String>();
		NodeList elementRefNodeList = originalDoc.getElementsByTagName("elementRef");
		for(int i=0;i<elementRefNodeList.getLength();i++){
			Node elementRefNode = elementRefNodeList.item(i);
			Node idAttributeNode = elementRefNode.getAttributes().getNamedItem("id");
			usedQDMIds.add(idAttributeNode.getNodeValue());
		}
		_logger.info("usedQDMIds:"+usedQDMIds);
		return usedQDMIds;
	}

	/**
	 * This will run all the necessary validations
	 * such as validate the Measure Details, etc
	 * @param measureXMLObject
	 */
	private static boolean validateMeasure(MeasureXML measureXMLObject) {
		boolean returnValue = true;
		return returnValue;
	}

}
