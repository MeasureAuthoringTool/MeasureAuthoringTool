package mat.server.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sf.saxon.TransformerFactoryImpl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlProcessor {
	
	private static final Log logger = LogFactory.getLog(XmlProcessor.class);
	
	private String originalXml;
	
	private DocumentBuilder docBuilder;
	
	private Document originalDoc;
	
	
	
	public XmlProcessor(String originalXml) {
		logger.info("In XmlProcessor() constructor");
		this.originalXml = originalXml;
		try {
			docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource oldXmlstream = new InputSource(new StringReader(originalXml));
			originalDoc = docBuilder.parse(oldXmlstream);
			logger.info("Document Object created successfully for the XML String");
		} catch (Exception e) {
			logger.info("Exception thrown on XmlProcessor() costructor");
			caughtExceptions(e);
			e.printStackTrace();
		}		
		
	}

	/**
	 * Method with Replace/Insert Node into the Original Xml
	 *------- REPLACE -----
	 * Example NewXml - <ABC>new text</ABC>
	 * 		   OldXml - <AAA><ABC>old Text</ABC><AAA>
	 * 		   Result - <AAA><ABC>new text</ABC><AAA>
	 * ------- INSERT ------
	 * Example  NewXml - <ABC>new text</ABC>
	 * 		    OldXml - <AAA><BBB>first</BBB><AAA>
	 * 		    Result - <AAA><BBB>first</BBB><ABC>new text</ABC><AAA>
	 * @param newXml 
	 * @param nodeName
	 * @param parentName // this is optional, can be null or empty. if parentName not null, 
	 * the oldNode to be replaced will be retrieved based on the parent Node, this is done to make sure we are replacing the correct node.
	 * 
	 * 
	 * @return
	 */
	public String replaceNode(String newXml, String nodeName, String parentName){
		try {
			logger.info("In replaceNode() method");
			InputSource newXmlstream = new InputSource(new StringReader(newXml));
			Document newDoc = docBuilder.parse(newXmlstream);//Parse the NewXml which should be replaced
			Node newNode = null;
			Node oldNode = null;
			NodeList newNodeList = newDoc.getElementsByTagName(nodeName);
			NodeList oldNodeList = originalDoc.getElementsByTagName(nodeName);
			
			if(oldNodeList.getLength() > 0){
				if(StringUtils.isBlank(parentName)){
					oldNode = oldNodeList.item(0);
				}else{
					for (int i = 0; i < oldNodeList.getLength(); i++) {
						if(parentName.equals(oldNodeList.item(i).getParentNode().getNodeName())){//get the old node with the matching Parent Node.
							oldNode = oldNodeList.item(i);
							break;
						}
					}
				}
			}
			
			if(newNodeList.getLength() > 0){
				newNode = newNodeList.item(0);
				for (int i = 0; i < newNodeList.getLength(); i++) {
					if(parentName.equals(newNodeList.item(i).getParentNode().getNodeName())){//get the new node used to replace.
						newNode = newNodeList.item(i);
						break;
					}
				}
				
				if(oldNode != null){// check if the OriginalXml has the Node that should be replaced
					Node nextSibling = oldNode.getNextSibling();
					Node parentNode = oldNode.getParentNode();
					parentNode.removeChild(oldNode);// Removing the old child node
					if(nextSibling != null){					
						parentNode.insertBefore(originalDoc.importNode(newNode, true), nextSibling);// to maintain the order insert before the next sibling if exists
					}else{
						parentNode.appendChild(originalDoc.importNode(newNode, true));//insert the new child node to the old child's Parent node,. 
					}
					logger.info("Replaced old Child Node with new Child Node " + nodeName);
				}else{//if the Original Document doesnt have the Node, then insert the new Node under the first child
					Node importNode = originalDoc.importNode(newNode, true);
					originalDoc.getFirstChild().appendChild(importNode);
					logger.info("Inserted new Child Node" + nodeName);
				}
				return transform(originalDoc);
			}
			
		}catch (Exception e) {
			logger.info("Exception thrown on replaceNode() method");
			caughtExceptions(e);
			e.printStackTrace();
		}
		return originalXml;// not replaced returnig the original Xml;
	}
	
	public String updateNodeText(String nodeName, String nodeValue){
		try {
			logger.info("In updateNodeText() method");
			InputSource xmlStream = new InputSource(new StringReader(originalXml));
			Document doc = docBuilder.parse(xmlStream);		
			doc.getElementsByTagName(nodeName).item(0).setTextContent(nodeValue);
			logger.info("update NoedText");
			return transform(doc);
		} catch (Exception e) {
			logger.info("Exception thrown on updateNodeText() method");
			caughtExceptions(e);
			e.printStackTrace();
		}
		return null;
	}
	
	private String transform(Node node){	
		logger.info("In transform() method");
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		TransformerFactory transformerFactory = TransformerFactoryImpl.newInstance();
		DOMSource source = new DOMSource(node);
		StreamResult result = new StreamResult(arrayOutputStream);
		
		try {
			transformerFactory.newTransformer().transform(source, result);
		} catch (TransformerException e) {
			logger.info("Document object to ByteArray transformation failed "+e.getStackTrace());
			e.printStackTrace();
		}
		logger.info("Document object to ByteArray transformation complete");
		System.out.println(arrayOutputStream.toString());
		return arrayOutputStream.toString();
	}

	public String getOriginalXml() {
		return originalXml;
	}

	public void setOriginalXml(String originalXml) {
		this.originalXml = originalXml;
	}
	
	public String getXmlByTagName(String tagName){
		Node node = originalDoc.getElementsByTagName(tagName).getLength() > 0 ? originalDoc.getElementsByTagName(tagName).item(0) : null;
		if(null != node){
			return transform(node);
		}
		return null;
	}
	

	public String addParentNode(String parentTagName){
		if(originalDoc.hasChildNodes()){
			Document newDoc = docBuilder.newDocument();
			Node parentNode = newDoc.appendChild(newDoc.createElement(parentTagName));
			Node importedNode  = newDoc.importNode(originalDoc.getFirstChild(), true);
			parentNode.appendChild(importedNode);
			return transform(newDoc);
		}
		return null;
	}
	
	
	private void caughtExceptions(Exception e){
		if(e instanceof ParserConfigurationException){
			logger.info("Document Builder Object creation failed" + e.getStackTrace());	
		}else if(e instanceof SAXException){
			logger.info("Xml parsing failed:" + e.getStackTrace());
		}else if(e instanceof IOException){
			logger.info("Conversion of String XML to InputSource failed" + e.getStackTrace());
		}else{
			logger.info("Generic Exception: "+ e.getStackTrace());
		}
	}
	

	
}
