package mat.server.service.impl;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.XMLContext;
import org.xml.sax.InputSource;

import mat.server.util.ResourceLoader;

public class XMLMarshalUtil {

	XMLContext xmlContext = new XMLContext();

	private Marshaller createMarshaller() {
		return xmlContext.createMarshaller();
	}

	private Unmarshaller createUnmarshaller() {
		return xmlContext.createUnmarshaller();
	}

	public String convertObjectToXML(String fileName, Object obj) 
			throws IOException, MappingException, MarshalException, ValidationException{
		
		try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
			final Mapping mapping = getMapping(fileName);
			
			final Marshaller marshaller = createMarshaller();
			marshaller.setWriter(new OutputStreamWriter(stream));
			marshaller.setMapping(mapping);
			marshaller.marshal(obj);
			
			return stream.toString(); 
		}
	}

	public Object convertXMLToObject(String fileName, String cqlLookUpXMLString, Class<?> objectClass) 
			throws MappingException, IOException, MarshalException, ValidationException {
		
		final Mapping mapping = getMapping(fileName);
		final Unmarshaller unmarshaller = createUnmarshaller();
		unmarshaller.setMapping(mapping);
		unmarshaller.setClass(objectClass);
		unmarshaller.setWhitespacePreserve(true);
		unmarshaller.setValidation(false);
		
		return unmarshaller.unmarshal(new InputSource(new StringReader(cqlLookUpXMLString)));
	}

	private Mapping getMapping(String fileName) throws MappingException, IOException {
		final Mapping mapping = new Mapping();
		mapping.loadMapping(new ResourceLoader().getResourceAsURL(fileName));
		return mapping;
	}

}
