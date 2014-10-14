package mat.server.simplexml.hqmf;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;

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
		createDataCriteriaForQDMELements(me, dataCriteriaXMLProcessor);
		//createDataCriteriaForAttributes(me, dataCriteriaXMLProcessor);
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
	 * Creates the data criteria for qdme lements.
	 *
	 * @param me            the me
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @return the string
	 */
	private void createDataCriteriaForQDMELements(MeasureExport me, XmlProcessor dataCriteriaXMLProcessor) {

		String simpleXMLStr = me.getSimpleXML();
		XmlProcessor simpleXmlprocessor = new XmlProcessor(simpleXMLStr);
		String xPathForElementLookUp = "/measure/elementLookUp/qdm";
		try {
			NodeList elementLookUpNode = (NodeList) xPath.evaluate(
					xPathForElementLookUp, simpleXmlprocessor.getOriginalDoc(),
					XPathConstants.NODESET);
			if (elementLookUpNode != null) {
				for (int i = 0; i < elementLookUpNode.getLength(); i++) {
					Node childNode = elementLookUpNode.item(i);					
					createXmlForDataCriteria(childNode, dataCriteriaXMLProcessor);
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
	private void createXmlForDataCriteria(Node qdmNode, XmlProcessor dataCriteriaXMLProcessor) {
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
				createDataCriteriaElementTag(actNodeStr, templateNode, qdmNode, dataCriteriaXMLProcessor);
			}
			
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Gets the creates the data create elemet tag.
	 *
	 * @param actNodeStr            the act node str
	 * @param childNode            the child node
	 * @param qdmNode            the qdm node
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @return the creates the data create elemet tag
	 */
	public void createDataCriteriaElementTag(String actNodeStr, Node childNode,
			Node qdmNode, XmlProcessor dataCriteriaXMLProcessor) {

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
	}

	/**

	 * Convert xml document to string.
	 * 
	 * @param document
	 *            the document
	 * @return the string
	 */
	public String convertXMLDocumentToString(Document document) {
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
	public void addDataCriteriaComment(XmlProcessor dataCriteriaXMLProcessor) {
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
	public Element createCodeForDatatype(Node childNode,
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
	
}
