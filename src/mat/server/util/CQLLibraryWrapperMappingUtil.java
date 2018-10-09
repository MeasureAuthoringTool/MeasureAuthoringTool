package mat.server.util;

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
import org.xml.sax.InputSource;

import mat.model.cql.CQLIncludeLibraryWrapper;

public class CQLLibraryWrapperMappingUtil {
	
	public static CQLIncludeLibraryWrapper convertXMLToCQLIncludeLibraryWrapper(String cqlIncludeLibraryXML) throws IOException, MappingException, MarshalException, ValidationException {
		Mapping mapping = new Mapping();
		mapping.loadMapping(new ResourceLoader().getResourceAsURL("CQLIncludeLibraryMapping.xml"));
		Unmarshaller unmarshaller = new Unmarshaller(mapping);
		unmarshaller.setClass(CQLIncludeLibraryWrapper.class);
		unmarshaller.setWhitespacePreserve(true);
		unmarshaller.setValidation(false);
		CQLIncludeLibraryWrapper cqlIncludeLibraryWrapper = (CQLIncludeLibraryWrapper) unmarshaller.unmarshal(new InputSource(new StringReader(cqlIncludeLibraryXML)));
		return cqlIncludeLibraryWrapper;
	}
	
	public static String convertCQLIncludeLibraryWrapperToXML(CQLIncludeLibraryWrapper wrapper) throws IOException, MappingException, MarshalException, ValidationException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		Mapping mapping = new Mapping(); 
		mapping.loadMapping(new ResourceLoader().getResourceAsURL("CQLIncludeLibraryMapping.xml"));
		Marshaller marshaller = new Marshaller(new OutputStreamWriter(stream));
		marshaller.setMapping(mapping);
		marshaller.marshal(wrapper);
		return new String(stream.toByteArray()); 
	}

}
