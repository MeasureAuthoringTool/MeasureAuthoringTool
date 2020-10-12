package mat.vsac.util;


import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.XMLContext;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;

public class XMLMarshalUtil {
    XMLContext xmlContext = new XMLContext();

    private Unmarshaller createUnmarshaller() {
        return xmlContext.createUnmarshaller();
    }

    public Object convertXMLToObject(String fileName, String cqlLookUpXMLString, Class<?> objectClass)
            throws MappingException, IOException, MarshalException, ValidationException {

        final Mapping mapping = getMapping(fileName);
        final Unmarshaller unmarshaller = createUnmarshaller();
        unmarshaller.setMapping(mapping);
        unmarshaller.setClass(objectClass);
        unmarshaller.setWhitespacePreserve(true);
        unmarshaller.setValidation(true);

        return unmarshaller.unmarshal(new InputSource(new StringReader(cqlLookUpXMLString)));
    }

    private Mapping getMapping(String fileName) throws MappingException, IOException {
        final Mapping mapping = new Mapping();
        mapping.loadMapping(new ResourceLoader().getResourceAsURL(fileName));
        return mapping;
    }

}
