package mat.server.simplexml;

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

import mat.model.clause.MeasureExport;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


// TODO: Auto-generated Javadoc
/**
 * The Class HQMFDataCriteriaGenerator.
 */
public class HQMFDataCriteriaGenerator {

	/** The document. */
	private static Document document;
	
	/** The transformer. */
	private static Transformer transformer;
	
	/** The output. */
	private static String output;

	/**
	 * Generate hqm ffor measure.
	 *
	 * @param me the me
	 * @return the string
	 */
	public static String generateHQMFforMeasure(MeasureExport me) {

		String dataCriteria = "";
//		dataCriteria = createDateCriteriaTemplate(me);
		return dataCriteria;
	}

	/**
	 * Creates the date criteria template.
	 *
	 * @param me the me
	 * @return the string
	 */
	private static String createDateCriteriaTemplate(MeasureExport me) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
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

		return convertXMLDocumentToString();
	}
	
	/**
	 * Convert xml document to string.
	 *
	 * @return the string
	 */
	private static String convertXMLDocumentToString(){
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
 	 * @param in the in
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
	 
}
