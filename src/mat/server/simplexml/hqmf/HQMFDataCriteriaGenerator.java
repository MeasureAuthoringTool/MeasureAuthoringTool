package mat.server.simplexml.hqmf;

import java.io.File;
import java.io.IOException;
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

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
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

	/** The output processor. */
	private static XmlProcessor outputProcessor;

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
	private static String getHQMFXmlString(MeasureExport me) {
		createDateCriteriaTemplate(me);
		createDataCriteriaForQDMELements(me);
		return convertXMLDocumentToString(outputProcessor.getOriginalDoc());
	}

	/**
	 * Creates the date criteria template.
	 * 
	 * @param me
	 *            the me
	 * @return the string
	 */
	private static void createDateCriteriaTemplate(MeasureExport me) {
		outputProcessor = new XmlProcessor(
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
	}

	/**
	 * Creates the data criteria for qdme lements.
	 * 
	 * @param me
	 *            the me
	 * @return the string
	 */
	private static void createDataCriteriaForQDMELements(MeasureExport me) {

		String simpleXMLStr = me.getSimpleXML();
		XmlProcessor simpleXmlprocessor = new XmlProcessor(simpleXMLStr);
		String xPathForElementLookUp = "/measure/elementLookUp/qdm";
		try {
			NodeList elementLookUpNode = (NodeList) xPath.evaluate(
					xPathForElementLookUp, simpleXmlprocessor.getOriginalDoc(),
					XPathConstants.NODESET);
			if (elementLookUpNode != null && elementLookUpNode.getLength() > 0) {
				for (int i = 0; i < elementLookUpNode.getLength(); i++) {
					Node childNode = elementLookUpNode.item(i);
					String dataType = childNode.getAttributes()
							.getNamedItem("datatype").getNodeValue();
					createXmlForDataCriteria(dataType, childNode);
				}
			}

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Createxml for data criteria.
	 * 
	 * @param dataType
	 *            the data type
	 * @param qdmNode
	 *            the qdm node
	 * @return the string
	 */
	private static void createXmlForDataCriteria(String dataType, Node qdmNode) {
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
			}
			createDataCriteriaElemetTag(actNodeStr, templateNode, qdmNode);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Gets the creates the data create elemet tag.
	 * 
	 * @param actNodeStr
	 *            the act node str
	 * @param childNode
	 *            the child node
	 * @param qdmNode
	 *            the qdm node
	 * @return the creates the data create elemet tag
	 */
	public static void createDataCriteriaElemetTag(String actNodeStr,
			Node childNode, Node qdmNode) {
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
		Node dataCriteriaSectionElem = outputProcessor.getOriginalDoc()
				.getElementsByTagName("dataCriteriaSection").item(0);
		Element dataCriteriaElem = (Element) outputProcessor.getOriginalDoc()
				.createElement(actNodeStr);
		dataCriteriaSectionElem.appendChild(dataCriteriaElem);
		dataCriteriaElem.setAttribute("classCode", classCodeValue);
		dataCriteriaElem.setAttribute("moodCode", moodValue);
		Element templateId = (Element) outputProcessor.getOriginalDoc()
				.createElement("templateId");
		dataCriteriaElem.appendChild(templateId);
		Element itemChild = (Element) outputProcessor.getOriginalDoc()
				.createElement("item");
		itemChild.setAttribute("root", oidValue);
		templateId.appendChild(itemChild);
		Element idElem = (Element) outputProcessor.getOriginalDoc()
				.createElement("id");
		idElem.setAttribute("root", rootValue);
		dataCriteriaElem.appendChild(idElem);
		Element titleElem = (Element) outputProcessor.getOriginalDoc()
				.createElement("title");
		titleElem.setAttribute("value", dataType);
		dataCriteriaElem.appendChild(titleElem);
		Element statusCodeElem = (Element) outputProcessor.getOriginalDoc()
				.createElement("statusCode");
		statusCodeElem.setAttribute("code", statusValue);
		dataCriteriaElem.appendChild(statusCodeElem);
		Element valueElem = (Element) outputProcessor.getOriginalDoc()
				.createElement("value");
		valueElem.setAttribute("valueSet", qdmOidValue);
		dataCriteriaElem.appendChild(valueElem);
	}

	/**
	 * Convert xml document to string.
	 * 
	 * @param document
	 *            the document
	 * @return the string
	 */
	public static String convertXMLDocumentToString(Document document) {
		StreamResult result = null;
		Transformer transformer;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(document);
			try {
				transformer.transform(source, result);
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return format(result.getWriter().toString(), document);
	}

	/**
	 * Format.
	 * 
	 * @param unformattedXml
	 *            the unformatted xml
	 * @param document
	 *            the document
	 * @return the string
	 */
	@SuppressWarnings("deprecation")
	public static String format(String unformattedXml, Document document) {
		try {
			OutputFormat format = new OutputFormat(document);
			format.setLineWidth(70);
			format.setIndenting(true);
			format.setIndent(3);
			Writer out = new StringWriter();
			XMLSerializer serializer = new XMLSerializer(out, format);
			serializer.serialize(document);
			return out.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}

	}

}
