package mat.server.simplexml.hqmf;

import java.io.File;
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
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

// TODO: Auto-generated Javadoc
/**
 * The Class HQMFXmlProcessor.
 */
public class HQMFXmlProcessor {

	/** The Constant LOG. */
	private static final Log LOG = LogFactory.getLog(HQMFXmlProcessor.class);

	/** The original xml. */
	private String originalXml;
	
    /** The doc builder. */
    private DocumentBuilder docBuilder;
	
	/** The original doc. */
	private Document originalDoc;

	/**
	 * Instantiates a new HQMF xml processor.
	 *
	 * @param fileStr the file str
	 * @return the xml document
	 */
//	public HQMFXmlProcessor(String originalXml) {
//		LOG.info("In XmlProcessor() constructor");
//		this.originalXml = originalXml;
//		try {
//			docBuilder = DocumentBuilderFactory.newInstance()
//					.newDocumentBuilder();
//			InputSource oldXmlstream = new InputSource(new StringReader(
//					originalXml));
//			originalDoc = docBuilder.parse(oldXmlstream);
//			LOG.info("Document Object created successfully for the XML String");
//		} catch (Exception e) {
//			LOG.info("Exception thrown on XmlProcessor() costructor");
//			caughtExceptions(e);
//			e.printStackTrace();
//		}
//
//	}
	
	public Document getXmlDocument(String fileStr){
		File file = new File(fileStr);
		  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		  DocumentBuilder db= null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  try {
			originalDoc = db.parse(file);
			//getDataTypeTemplate(dataTypeStr,originalDoc);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  return originalDoc;
	}
	
	
	/**
	 * Gets the creates the data create elemet tag.
	 *
	 * @return the creates the data create elemet tag
	 */
	public String getCreateDataCreateElemetTag(String actText, Node childNode, 
			Node qdmNode){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document document = null;
		String oidValue = childNode.getAttributes().getNamedItem("oid").getNodeValue();
		String classCodeValue = childNode.getAttributes().getNamedItem("class").getNodeValue();
		String moodValue = childNode.getAttributes().getNamedItem("mood").getNodeValue();
		String statusValue = childNode.getAttributes().getNamedItem("status").getNodeValue();
		String rootValue = qdmNode.getAttributes().getNamedItem("id").getNodeValue();
		String dataType = qdmNode.getAttributes().getNamedItem("datatype").getNodeValue();
		String qdmOidValue = qdmNode.getAttributes().getNamedItem("oid").getNodeValue();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.newDocument();
			//creating 
			Element dataCriteriaElem = (Element) document.createElement(actText);
			document.appendChild(dataCriteriaElem);
			dataCriteriaElem.setAttribute("classCode", classCodeValue);
			dataCriteriaElem.setAttribute("moodCode", moodValue);
			Element templateId = (Element)document.createElement("templateId");
			dataCriteriaElem.appendChild(templateId);
			Element itemChild = (Element)document.createElement("item");
			itemChild.setAttribute("root", oidValue);
			templateId.appendChild(itemChild);
			Element idElem = (Element)document.createElement("id");
			idElem.setAttribute("root", rootValue);
			dataCriteriaElem.appendChild(idElem);
			Element titleElem = (Element)document.createElement("title");
			titleElem.setAttribute("value", dataType);
			dataCriteriaElem.appendChild(titleElem);
			Element statusCodeElem = (Element)document.createElement("statusCode");
			statusCodeElem.setAttribute("code", statusValue);
			dataCriteriaElem.appendChild(statusCodeElem);
			Element valueElem = (Element)document.createElement("value");
			//valueElem.setAttribute("xsi:type", "");
			valueElem.setAttribute("valueSet", qdmOidValue);
			dataCriteriaElem.appendChild(valueElem);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertXMLDocumentToString(document);
	}
	
	
	/**
	 * Convert xml document to string.
	 *
	 * @param document the document
	 * @return the string
	 */
	public static String convertXMLDocumentToString(Document document){
		StreamResult result = null;
		Transformer transformer;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
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
		  
		  return format(result.getWriter().toString());
	}
	
	 /**
 	 * Format.
 	 *
 	 * @param unformattedXml the unformatted xml
 	 * @return the string
 	 */
 	@SuppressWarnings("deprecation")
	public static String format(String unformattedXml) {
	        try {
	            Document document = parseXmlFile(unformattedXml);
	 
	            OutputFormat format = new OutputFormat(document);
	            format.setLineWidth(65);
	            format.setIndenting(true);
	            format.setIndent(2);
	            Writer out = new StringWriter();
	            XMLSerializer serializer = new XMLSerializer(out, format);
	            serializer.serialize(document);
	            return out.toString();
	        } catch (IOException e) {
	            e.printStackTrace();
	            return "";
	        }

}
	 
	 /**
 	 * Parses the xml file.
 	 *
 	 * @param xmlString the xml string
 	 * @return the document
 	 */
 	private static Document parseXmlFile(String xmlString) {
	        try {
	            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	            DocumentBuilder db = dbf.newDocumentBuilder();
	            InputSource is = new InputSource(new StringReader(xmlString));
	            return db.parse(is);
	        } catch (ParserConfigurationException e) {
	            throw new RuntimeException(e);
	        } catch (SAXException e) {
	            throw new RuntimeException(e);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	
	
	

//	private void getDataTypeTemplate(String fileStr, Document doc) {
//		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
//		String xpathForDataType = ""
//		
//	}

	/**
	 * Caught exceptions.
	 *
	 * @param excp the excp
	 */
	/*private void caughtExceptions(Exception excp) {
		if (excp instanceof ParserConfigurationException) {
			LOG.info("Document Builder Object creation failed"
					+ excp.getStackTrace());
		} else if (excp instanceof SAXException) {
			LOG.info("Xml parsing failed:" + excp.getStackTrace());
		} else if (excp instanceof IOException) {
			LOG.info("Conversion of String XML to InputSource failed"
					+ excp.getStackTrace());
		} else {
			LOG.info("Generic Exception: " + excp.getStackTrace());
		}
	}*/
}
