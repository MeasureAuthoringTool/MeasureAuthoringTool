package org.ifmc.mat.server.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.ifmc.mat.dao.MeasureValidationLogDAO;
import org.ifmc.mat.model.clause.Measure;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * perform an xsd validation logging any failed validations
 * @author aschmidt
 *
 */
public class XSDValidationService {

	private static final String HTTP_WWW_W3_ORG_2001_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
	private static final String VALIDATION_FILE="xsd/schemas/EMeasure.xsd";
	private static final String EVENT_TYPE = "MeasureValidationEvent";
	
	private String emeasureXML = null;
	private String interimXML = null;
	private Measure measure = null;
	private MeasureValidationLogDAO measureValidationLogDAO = null;
	private XMLErrorHandler xmlErrorHandler = null;
	
	private ByteArrayInputStream bais = null;
	
	
	
	public XSDValidationService(String emeasureXML, String interimXML, Measure measure, MeasureValidationLogDAO measureValidationLogDAO){
		this.emeasureXML = emeasureXML;
		this.interimXML = interimXML;
		this.measure = measure;
		this.measureValidationLogDAO = measureValidationLogDAO;
	}
	
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
	
	private SAXParserFactory saxParserFactory;

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
	private void validateXSD(InputStream inXMLStream) throws SAXException,
	      ParserConfigurationException, IOException {

	    xmlErrorHandler = new XMLErrorHandler();
	    SAXParser parser = getSaxParserFactory().newSAXParser();
	    XMLReader reader = parser.getXMLReader();
	    reader.setErrorHandler(xmlErrorHandler);
	    reader.parse(new InputSource(inXMLStream));
	}
	
	private void logFailedValidation(){
		measureValidationLogDAO.recordMeasureValidationEvent(measure, EVENT_TYPE, interimXML.getBytes());
	}
}
