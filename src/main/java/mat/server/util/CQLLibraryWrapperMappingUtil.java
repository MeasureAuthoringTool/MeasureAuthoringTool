package mat.server.util;

import mat.model.cql.CQLIncludeLibraryWrapper;
import mat.server.service.impl.XMLMarshalUtil;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import java.io.IOException;

public class CQLLibraryWrapperMappingUtil {

    private static final String MAPPING = "CQLIncludeLibraryMapping.xml";

    public static String convertCQLIncludeLibraryWrapperToXML(CQLIncludeLibraryWrapper wrapper)
            throws IOException, MappingException, MarshalException, ValidationException {

        final XMLMarshalUtil xmlMarshalUtil = new XMLMarshalUtil();
        return xmlMarshalUtil.convertObjectToXML(MAPPING, wrapper);

    }

}
