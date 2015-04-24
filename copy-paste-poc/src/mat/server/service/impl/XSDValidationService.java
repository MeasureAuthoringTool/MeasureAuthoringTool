package mat.server.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import mat.dao.MeasureValidationLogDAO;
import mat.model.clause.Measure;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * perform an xsd validation logging any failed validations.
 * 
 * @author aschmidt
 */
public class XSDValidationService {

	/** The Constant HTTP_WWW_W3_ORG_2001_XML_SCHEMA. */
	private static final String HTTP_WWW_W3_ORG_2001_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
	
	/** The Constant VALIDATION_FILE. */
	private static final String VALIDATION_FILE="xsd/schemas/EMeasure.xsd";
	
	/** The Constant EVENT_TYPE. */
	private static final String EVENT_TYPE = "MeasureValidationEvent";
	
	/** The emeasure xml. */
	private String emeasureXML = null;
	
	/** The interim xml. */
	private String interimXML = null;
	
	/** The measure. */
	private Measure measure = null;
	
	/** The measure validation log dao. */
	private MeasureValidationLogDAO measureValidationLogDAO = null;
	
	/** The xml error handler. */
	private XMLErrorHandler xmlErrorHandler = null;
	
	/** The bais. */
	private ByteArrayInputStream bais = null;
	
	
	
	/**
	 * Instantiates a new xSD validation service.
	 * 
	 * @param emeasureXML
	 *            the emeasure xml
	 * @param interimXML
	 *            the interim xml
	 * @param measure
	 *            the measure
	 * @param measureValidationLogDAO
	 *            the measure validation log dao
	 */
	public XSDValidationService(String emeasureXML, String interimXML, Measure measure, MeasureValidationLogDAO measureValidationLogDAO){
		this.emeasureXML = emeasureXML;
		this.interimXML = interimXML;
		this.measure = measure;
		this.measureValidationLogDAO = measureValidationLogDAO;
	}
	
	/**
	 * Validate emeasure xml.
	 */
	public void validateEmeasureXML(){
		byte[] barr = emeasureXML.getBytes();
		bais = new ByteArrayInputStream(barr);
		boolean validate = true;
		try {
			validateXSD(bais);
			bais.close();
		} catch (SAXException e) {
			e.printStackTrace();
			validate = false;
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			validate = false;
		} catch (IOException e) {
			e.printStackTrace();
			validate = false;
		}
		if(!validate || !xmlErrorHandler.getErrors().isEmpty()){
			logFailedValidation();
		}
	}
	
	/** The sax parser factory. */
	private SAXParserFactory saxParserFactory;

	/**
	 * Gets the sax parser factory.
	 * 
	 * @return the sax parser factory
	 */
	private SAXParserFactory getSaxParserFactory() {
	    if (saxParserFactory != null) {
	      return saxParserFactory;
	    }
	
	    saxParserFactory = SAXParserFactory.newInstance();
	    saxParserFactory.setValidating(false);
	    saxParserFactory.setNamespaceAware(true);
	    SchemaFactory schemaFactory = SchemaFactory.newInstance(HTTP_WWW_W3_ORG_2001_XML_SCHEMA);
	
	    XMLUtility xutil = new XMLUtility();
	    String xsd = xutil.getXMLResource(VALIDATION_FILE);
	    StreamSource source = new StreamSource(xsd);
	
	    try {
	      saxParserFactory.setSchema(schemaFactory.newSchema(source));
	    } catch (SAXException e) {
	      System.out.println("Could not get the SAXParserFactory...");
	      System.out.println(e.getMessage());
	    }
	    return saxParserFactory;
	}
	
	/**
	 * Validate xsd.
	 * 
	 * @param inXMLStream
	 *            the in xml stream
	 * @throws SAXException
	 *             the sAX exception
	 * @throws ParserConfigurationException
	 *             the parser configuration exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void validateXSD(InputStream inXMLStream) throws SAXException,
	      ParserConfigurationException, IOException {

	    xmlErrorHandler = new XMLErrorHandler();
	    SAXParser parser = getSaxParserFactory().newSAXParser();
	    XMLReader reader = parser.getXMLReader();
	    reader.setErrorHandler(xmlErrorHandler);
	    reader.parse(new InputSource(inXMLStream));
	}
	
	/**
	 * Log failed validation.
	 */
	private void logFailedValidation(){
		measureValidationLogDAO.recordMeasureValidationEvent(measure, EVENT_TYPE, interimXML.getBytes());
	}
}
