package mat.server.simplexml.hqmf;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


// TODO: Auto-generated Javadoc
/**
 * The Class HQMFDataCriteriaGenerator.
 */
public class HQMFDataCriteriaGenerator implements Generator {

	/** The document. */
	private static Document document;
	
	/** The transformer. */
	private static Transformer transformer;
	
	/** The output. */
	private static String output;
	
	static javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
	

	/**
	 * Generate hqm for measure.
	 *
	 * @param me the me
	 * @return the string
	 */
	@Override
	public String generate(MeasureExport me) {

		String dataCriteria = "";
		dataCriteria = createDateCriteriaTemplate(me);
		dataCriteria += createDataCriteriaForQDMELements(me); 
		return dataCriteria.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
	}

	/**
	 * Creates the date criteria template.
	 *
	 * @param me the me
	 * @return the string
	 */
	private static String createDateCriteriaTemplate(MeasureExport me) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		HQMFXmlProcessor hqmfXmlProcessor = new HQMFXmlProcessor();
		
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.newDocument();
			// Creating TemplateId for DataCriteria
			Element dataCriteriaElem = (Element) document.createElement("DataCriteria");
			document.appendChild(dataCriteriaElem);
			Element templateId = (Element) document.createElement("templateId");
			dataCriteriaElem.appendChild(templateId);
			Element itemChild = (Element)document.createElement("item");
			itemChild.setAttribute("root", "2.16.840.1.113883.10.20.28.2.2");
			templateId.appendChild(itemChild);
			//creating Code Element for DataCriteria
			Element codeElem = (Element) document.createElement("code");
			codeElem.setAttribute("code", "57025-9");
			codeElem.setAttribute("codeSystem", "2.16.840.1.113883.6.1");
			dataCriteriaElem.appendChild(codeElem);
			//creating title for DataCriteria
			Element titleElem = (Element) document.createElement("title");
			titleElem.setAttribute("value", "Data Criteria Section");
			dataCriteriaElem.appendChild(titleElem);
			//creating text for DataCriteria
			Element textElem = (Element) document.createElement("text");
			textElem.setAttribute("value", "Data Criteria text");
			dataCriteriaElem.appendChild(textElem);
			
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		}
		//createDataCriteriaForQDMELements(me);
		return HQMFXmlProcessor.convertXMLDocumentToString(document); 
	}
	
	
	/**
	 * Creates the data criteria for qdme lements.
	 *
	 * @param me the me
	 * @return the string
	 */
	private static String createDataCriteriaForQDMELements(MeasureExport me) {
		
		String simpleXMLStr = me.getSimpleXML();
		XmlProcessor xmlProcessor = new XmlProcessor(simpleXMLStr);
		String xPathForElementLookUp = "/measure/elementLookUp/qdm";
		String dataTypeCriteria="";
		try {
			NodeList elementLookUpNode = (NodeList) xPath.evaluate(xPathForElementLookUp, 
					xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);
			if(elementLookUpNode!=null&& elementLookUpNode.getLength()>0){
				for(int i=0; i<elementLookUpNode.getLength();i++){
					Node childNode = elementLookUpNode.item(i);
					String dataType = childNode.getAttributes().getNamedItem("datatype").getNodeValue();
					HQMFXmlProcessor processor = new HQMFXmlProcessor();
					Document templateDoc = processor.getXmlDocument("templates.xml");
					dataTypeCriteria += createxmlForDataCriteria(dataType, templateDoc, childNode);
				}
			}
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dataTypeCriteria;
	}
	
	

	/**
	 * Createxml for data criteria.
	 *
	 * @param dataType the data type
	 * @param templateDoc the template doc
	 */
	private static String createxmlForDataCriteria(String dataType,
			Document templateDoc, Node qdmNode) {
		HQMFXmlProcessor hqmfXmlProcessor = new HQMFXmlProcessor();
		String xPathForTemplate = "/templates/template[text()='"+dataType.toLowerCase()+"']";
		String actText = "";
		String qdmElementStr = "";
		try {
			Node templateNode = (Node) xPath.evaluate(xPathForTemplate, 
					templateDoc, XPathConstants.NODE);
			if(templateNode!=null){
				String attrClass = templateNode.getAttributes().getNamedItem("class").getNodeValue();
				String xpathForAct = "/templates/acts/act[@a_id='"+attrClass+"']";
				Node actNode = (Node) xPath.evaluate(xpathForAct, 
					templateDoc, XPathConstants.NODE);
				if(actNode!=null){
					actText =  actNode.getTextContent();
				}
			}
			qdmElementStr = hqmfXmlProcessor.getCreateDataCreateElemetTag(actText, templateNode, 
					qdmNode);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return qdmElementStr;
		
	}
	 
}
