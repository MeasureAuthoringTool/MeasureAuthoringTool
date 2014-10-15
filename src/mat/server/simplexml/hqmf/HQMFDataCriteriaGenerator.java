package mat.server.simplexml.hqmf;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import mat.model.clause.MeasureExport;
import mat.server.util.XmlProcessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// TODO: Auto-generated Javadoc
/**
 * The Class HQMFDataCriteriaGenerator.
 */
public class HQMFDataCriteriaGenerator implements Generator {

	/** The x path. */
	static javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
	
	/** The Constant logger. */
	private final Log LOG = LogFactory.getLog(HQMFDataCriteriaGenerator.class);
	
	/** The name space. */
	private final String nameSpace = "http://www.w3.org/2001/XMLSchema-instance";

	/**
	 * Generate hqm for measure.
	 * 
	 * @param me
	 *            the me
	 * @return the string
	 */
	@Override
	public String generate(MeasureExport me) {

		String dataCriteria = "";
		dataCriteria = getHQMFXmlString(me);
		return dataCriteria.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
	}

	/**
	 * Gets the HQMF xml string.
	 * 
	 * @param me
	 *            the me
	 * @return the HQMF xml string
	 */
	private String getHQMFXmlString(MeasureExport me) {
		XmlProcessor dataCriteriaXMLProcessor = createDateCriteriaTemplate(me);
		
		String simpleXMLStr = me.getSimpleXML();
		XmlProcessor simpleXmlprocessor = new XmlProcessor(simpleXMLStr);
		
		createDataCriteriaForQDMELements(me, dataCriteriaXMLProcessor, simpleXmlprocessor);
		addDataCriteriaComment(dataCriteriaXMLProcessor);
		return convertXMLDocumentToString(dataCriteriaXMLProcessor.getOriginalDoc());
	}


	/**
	 * Creates the date criteria template.
	 * 
	 * @param me
	 *            the me
	 * @return the string
	 */
	private XmlProcessor createDateCriteriaTemplate(MeasureExport me) {
		XmlProcessor outputProcessor = new XmlProcessor(
				"<component><dataCriteriaSection></dataCriteriaSection></component>");
		
		Node dataCriteriaElem = outputProcessor.getOriginalDoc()
				.getElementsByTagName("dataCriteriaSection").item(0);
		Element templateId = (Element) outputProcessor.getOriginalDoc()
				.createElement("templateId");
		dataCriteriaElem.appendChild(templateId);
		Element itemChild = (Element) outputProcessor.getOriginalDoc()
				.createElement("item");
		itemChild.setAttribute("root", "2.16.840.1.113883.10.20.28.2.2");
		templateId.appendChild(itemChild);
		// creating Code Element for DataCriteria
		Element codeElem = (Element) outputProcessor.getOriginalDoc()
				.createElement("code");
		codeElem.setAttribute("code", "57025-9");
		codeElem.setAttribute("codeSystem", "2.16.840.1.113883.6.1");
		dataCriteriaElem.appendChild(codeElem);
		// creating title for DataCriteria
		Element titleElem = (Element) outputProcessor.getOriginalDoc()
				.createElement("title");
		titleElem.setAttribute("value", "Data Criteria Section");
		dataCriteriaElem.appendChild(titleElem);
		// creating text for DataCriteria
		Element textElem = (Element) outputProcessor.getOriginalDoc()
				.createElement("text");
		textElem.setAttribute("value", "Data Criteria text");
		dataCriteriaElem.appendChild(textElem);
		
		return outputProcessor;
	}

	/**
	 * Creates the data criteria for qdm elements.
	 *
	 * @param me            the me
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @return the string
	 */
	private void createDataCriteriaForQDMELements(MeasureExport me, XmlProcessor dataCriteriaXMLProcessor, XmlProcessor simpleXmlprocessor) {

		String xPathForElementLookUp = "/measure/elementLookUp/qdm";
		try {
			NodeList elementLookUpNode = (NodeList) xPath.evaluate(
					xPathForElementLookUp, simpleXmlprocessor.getOriginalDoc(),
					XPathConstants.NODESET);
			if (elementLookUpNode != null) {
				for (int i = 0; i < elementLookUpNode.getLength(); i++) {
					Node childNode = elementLookUpNode.item(i);					
					createXmlForDataCriteria(childNode, dataCriteriaXMLProcessor, simpleXmlprocessor);
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Createxml for data criteria.
	 *
	 * @param qdmNode            the qdm node
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @return the string
	 */
	private void createXmlForDataCriteria(Node qdmNode, XmlProcessor dataCriteriaXMLProcessor, XmlProcessor simpleXmlprocessor) {
		String dataType = qdmNode.getAttributes().getNamedItem("datatype").getNodeValue(); 
		
		XmlProcessor tempProcessor = new XmlProcessor(new File("templates.xml"));
		String xPathForTemplate = "/templates/template[text()='"
				+ dataType.toLowerCase() + "']";
		String actNodeStr = "";
		try {
			Node templateNode = (Node) xPath.evaluate(xPathForTemplate,
					tempProcessor.getOriginalDoc(), XPathConstants.NODE);
			
			if (templateNode != null) {
				String attrClass = templateNode.getAttributes()
						.getNamedItem("class").getNodeValue();
				String xpathForAct = "/templates/acts/act[@a_id='" + attrClass
						+ "']";
				Node actNode = (Node) xPath.evaluate(xpathForAct,
						tempProcessor.getOriginalDoc(), XPathConstants.NODE);
				if (actNode != null) {
					actNodeStr = actNode.getTextContent();
				}
				createDataCriteriaElementTag(actNodeStr, templateNode, qdmNode, dataCriteriaXMLProcessor, simpleXmlprocessor);
			}
			
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Gets the creates the data create element tag.
	 *
	 * @param actNodeStr            the act node str
	 * @param childNode            the child node
	 * @param qdmNode            the qdm node
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param simpleXmlprocessor 
	 * @return the creates the data create element tag
	 * @throws XPathExpressionException 
	 */
	private void createDataCriteriaElementTag(String actNodeStr, Node childNode,
			Node qdmNode, XmlProcessor dataCriteriaXMLProcessor, XmlProcessor simpleXmlprocessor) throws XPathExpressionException {

		String oidValue = childNode.getAttributes().getNamedItem("oid")
				.getNodeValue();
		String classCodeValue = childNode.getAttributes().getNamedItem("class")
				.getNodeValue();
		String moodValue = childNode.getAttributes().getNamedItem("mood")
				.getNodeValue();
		String statusValue = childNode.getAttributes().getNamedItem("status")
				.getNodeValue();
		String rootValue = qdmNode.getAttributes().getNamedItem("id")
				.getNodeValue();
		String dataType = qdmNode.getAttributes().getNamedItem("datatype")
				.getNodeValue();
		String qdmOidValue = qdmNode.getAttributes().getNamedItem("oid")
				.getNodeValue();
		String qdmLocalVariableName = qdmNode.getAttributes()
				.getNamedItem("localVariableName").getNodeValue();
		String qdmName = qdmNode.getAttributes()
				.getNamedItem("name").getNodeValue();
		String qdmTaxonomy = qdmNode.getAttributes()
				.getNamedItem("taxonomy").getNodeValue();

		Element dataCriteriaSectionElem = (Element)dataCriteriaXMLProcessor
				.getOriginalDoc().getElementsByTagName("dataCriteriaSection")
				.item(0);
		Attr nameSpaceAttr = dataCriteriaXMLProcessor.getOriginalDoc()
				.createAttribute("xmlns:xsi");
		nameSpaceAttr.setNodeValue(nameSpace);
		dataCriteriaSectionElem.setAttributeNodeNS(nameSpaceAttr);
		// creating Entry Tag
		Element entryElem = (Element) dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement("entry");
		entryElem.setAttribute("typeCode", "DRIV");
		dataCriteriaSectionElem.appendChild(entryElem);

		// creating LocalVariableName Tag
		Element localVarElem = (Element) dataCriteriaXMLProcessor
				.getOriginalDoc().createElement("localVariableName");
		localVarElem.setAttribute("value", qdmLocalVariableName);
		entryElem.appendChild(localVarElem);

		Element dataCriteriaElem = (Element) dataCriteriaXMLProcessor
				.getOriginalDoc().createElement(actNodeStr);
		entryElem.appendChild(dataCriteriaElem);
		dataCriteriaElem.setAttribute("classCode", classCodeValue);
		dataCriteriaElem.setAttribute("moodCode", moodValue);

		Element templateId = (Element) dataCriteriaXMLProcessor
				.getOriginalDoc().createElement("templateId");
		dataCriteriaElem.appendChild(templateId);

		Element itemChild = (Element) dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement("item");
		itemChild.setAttribute("root", oidValue);
		templateId.appendChild(itemChild);

		Element idElem = (Element) dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement("id");
		idElem.setAttribute("root", rootValue);
		idElem.setAttribute("extension", qdmName+"_"+qdmLocalVariableName);
		dataCriteriaElem.appendChild(idElem);

		Element codeElement = (Element) createCodeForDatatype(childNode,
				dataCriteriaXMLProcessor);
		if (codeElement != null) {
			dataCriteriaElem.appendChild(codeElement);
		}
		Element titleElem = (Element) dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement("title");
		titleElem.setAttribute("value", dataType);
		dataCriteriaElem.appendChild(titleElem);

		Element statusCodeElem = (Element) dataCriteriaXMLProcessor
				.getOriginalDoc().createElement("statusCode");
		statusCodeElem.setAttribute("code", statusValue);
		dataCriteriaElem.appendChild(statusCodeElem);

		Element valueElem = (Element) dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement("value");
		Node valueTypeAttr = childNode.getAttributes().getNamedItem("valueType");
		if(valueTypeAttr!=null){
			valueElem.setAttribute("xsi:type", valueTypeAttr.getNodeValue());
		}
		valueElem.setAttribute("valueSet", qdmOidValue);
		
		Element displayNameElem = (Element) dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement("displayName");
		displayNameElem.setAttribute("value", qdmName+" "+qdmTaxonomy+" Value Set");
		
		valueElem.appendChild(displayNameElem);		
		dataCriteriaElem.appendChild(valueElem);
		
		//checkForAttributes
		createDataCriteriaForAttributes(qdmNode, dataCriteriaElem, dataCriteriaXMLProcessor, simpleXmlprocessor);
	}

	/**

	 * Convert xml document to string.
	 * 
	 * @param document
	 *            the document
	 * @return the string
	 */
	private String convertXMLDocumentToString(Document document) {
		Transformer tf;
		Writer out = null;
		try {
			tf = TransformerFactory.newInstance().newTransformer();
			tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			out = new StringWriter();
			tf.transform(new DOMSource(document), new StreamResult(out));
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		
		return out.toString();
	}
	
	/**
	 * Adds the data criteria comment.
	 *
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 */
	private void addDataCriteriaComment(XmlProcessor dataCriteriaXMLProcessor) {
		Element element = dataCriteriaXMLProcessor.getOriginalDoc().getDocumentElement();
		Comment comment = dataCriteriaXMLProcessor.getOriginalDoc().createComment(
				"Data Criteria Section");
		element.getParentNode().insertBefore(comment, element);
	}
	
	/**
	 * Creates the code for datatype.
	 *
	 * @param childNode the child node
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @return the element
	 */
	private Element createCodeForDatatype(Node childNode,
			XmlProcessor dataCriteriaXMLProcessor) {
		Node codeAttr = childNode.getAttributes().getNamedItem("code");
		Node codeSystemAttr = childNode.getAttributes().getNamedItem(
				"codeSystem");
		Element codeElement = null;
		if (codeAttr != null || codeSystemAttr != null) {
			codeElement = (Element) dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement("code");
			if (codeAttr != null) {
				codeElement.setAttribute("code", 
						codeAttr.getNodeValue());
			}
			if (codeSystemAttr != null) {
				codeElement.setAttribute("codeSystem",
						codeSystemAttr.getNodeValue());
			}
		}
		return codeElement;
	}
	
	/**
	 * This method will look for attributes used in the subTree logic and then generate appropriate data criteria entries.
	 * @param childNode 
	 * @param dataCriteriaElem 
	 * @param dataCriteriaXMLProcessor
	 * @param simpleXmlprocessor
	 * @throws XPathExpressionException 
	 */
	private void createDataCriteriaForAttributes(Node childNode, Element dataCriteriaElem, XmlProcessor dataCriteriaXMLProcessor, XmlProcessor simpleXmlprocessor) throws XPathExpressionException {
		
		generateNegationRationalEntries(childNode, dataCriteriaElem, 
				dataCriteriaXMLProcessor, simpleXmlprocessor);
		
	}

	private void generateNegationRationalEntries(Node qdmNode, Element dataCriteriaElem, XmlProcessor dataCriteriaXMLProcessor,
			XmlProcessor simpleXmlprocessor) throws XPathExpressionException {
		
		String uuid = qdmNode.getAttributes().getNamedItem("uuid").getNodeValue();
		System.out.println("UUID:"+uuid);
		String xPathString = "//elementRef[@id='" + uuid
				+ "']/attribute[@name='negation rationale'][@qdmUUID]";
		
		NodeList elementRefList = simpleXmlprocessor.findNodeList(
				simpleXmlprocessor.getOriginalDoc(), xPathString);
		System.out.println("attributes found:"+elementRefList.getLength());
		List<String> attribUUIDList = new ArrayList<String>();
		
		for (int i = 0; i < elementRefList.getLength(); i++) {
			Node attribNode = elementRefList.item(i);
			String attribUUID = attribNode.getAttributes()
					.getNamedItem("qdmUUID").getNodeValue();
			System.out.println("attribute uuid:"+attribUUID);
			if (!attribUUIDList.contains(attribUUID)) {
				attribUUIDList.add(attribUUID);
				xPathString = "/measure/elementLookUp/qdm[@uuid='" + attribUUID
						+ "'][@datatype='attribute']";
				Node qdmAttributeNode = simpleXmlprocessor.findNode(
						simpleXmlprocessor.getOriginalDoc(), xPathString);
				System.out.println("attribute qdm found:"+(qdmAttributeNode == null));
				if (qdmAttributeNode != null) {
					String attributeValueSetName = qdmAttributeNode.getAttributes()
							.getNamedItem("name").getNodeValue();
					String attributeOID = qdmAttributeNode.getAttributes()
							.getNamedItem("oid").getNodeValue();
					String attributeTaxonomy = qdmAttributeNode.getAttributes()
							.getNamedItem("taxonomy").getNodeValue();
					
					dataCriteriaElem.setAttribute("actionNegationInd", "true");
					
					Element outboundRelationshipElem = (Element) dataCriteriaXMLProcessor.getOriginalDoc()
							.createElement("outboundRelationship");
					outboundRelationshipElem.setAttribute("typeCode", "RSON");
					
					Element observationCriteriaElem = (Element) dataCriteriaXMLProcessor.getOriginalDoc()
							.createElement("observationCriteria");
					observationCriteriaElem.setAttribute("classCode", "OBS");
					observationCriteriaElem.setAttribute("moodCode", "EVN");
					
					outboundRelationshipElem.appendChild(observationCriteriaElem);
					
					Element templateId = (Element) dataCriteriaXMLProcessor
							.getOriginalDoc().createElement("templateId");
					observationCriteriaElem.appendChild(templateId);

					Element itemChild = (Element) dataCriteriaXMLProcessor.getOriginalDoc()
							.createElement("item");
					itemChild.setAttribute("root", "2.16.840.1.113883.10.20.28.3.88");
					templateId.appendChild(itemChild);
					
					Element idElem = (Element) dataCriteriaXMLProcessor.getOriginalDoc()
							.createElement("id");
					idElem.setAttribute("root", attribUUID);
					idElem.setAttribute("extension", attributeValueSetName);
					observationCriteriaElem.appendChild(idElem);
					
					Element codeElem = (Element) dataCriteriaXMLProcessor.getOriginalDoc()
							.createElement("code");
					codeElem.setAttribute("code", "410666004");
					codeElem.setAttribute("codeSystem", "2.16.840.1.113883.6.96");
										
					Element displayNameElem = (Element) dataCriteriaXMLProcessor.getOriginalDoc()
							.createElement("displayName");
					displayNameElem.setAttribute("value", "Reason");
					
					observationCriteriaElem.appendChild(codeElem);
					codeElem.appendChild(displayNameElem);
					
					Element titleElem = (Element) dataCriteriaXMLProcessor.getOriginalDoc()
							.createElement("title");
					titleElem.setAttribute("value", "Reason");
					observationCriteriaElem.appendChild(titleElem);
					
					Element valueElem = (Element) dataCriteriaXMLProcessor.getOriginalDoc()
							.createElement("value");
					valueElem.setAttribute("xsi:type", "CD");					
					valueElem.setAttribute("valueSet", attributeOID);
					
					Element valueDisplayNameElem = (Element) dataCriteriaXMLProcessor.getOriginalDoc()
							.createElement("displayName");
					valueDisplayNameElem.setAttribute("value", attributeValueSetName+" "+attributeTaxonomy+" Value Set");
					
					valueElem.appendChild(valueDisplayNameElem);
					observationCriteriaElem.appendChild(valueElem);
					
					dataCriteriaElem.appendChild(outboundRelationshipElem);
				}
			}
		}
	}
	
}
