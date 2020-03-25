package mat.cql;

import lombok.extern.slf4j.Slf4j;
import mat.model.cql.CQLModel;
import mat.server.service.impl.XMLMarshalUtil;
import mat.server.util.XmlProcessor;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

@Slf4j
public class TestCqlToMatXml {
    private static final String CQL_TEST_RESOURCES_DIR="/test-cql/";
    private XMLMarshalUtil xmlMarshalUtil = new XMLMarshalUtil();

    public  String loadCqlResource(String cqlResource) throws IOException {
        try (InputStream i = TestCqlToMatXml.class.getResourceAsStream(CQL_TEST_RESOURCES_DIR + cqlResource)) {
            return IOUtils.toString(i);
        }
    }

    public CQLModel loadMatXml(String fileName) throws Exception {
        String xml = loadCqlResource(fileName);
        XmlProcessor measureXMLProcessor = new XmlProcessor(xml);
        String cqlXmlFrag = measureXMLProcessor.getXmlByTagName("cqlLookUp");
        return (CQLModel) xmlMarshalUtil.convertXMLToObject("CQLModelMapping.xml", cqlXmlFrag, CQLModel.class);
    }

    @Test
    public void testConverted1() throws Exception {
        String cql = loadCqlResource("convert-1.cql");
        CQLModel existingModel = loadMatXml("convert-1-mat.xml");
        CqlToMatXml converter = new CqlToMatXml(existingModel, cql);
        CQLModel destination = converter.convert();
        assertEquals("FHIR" , destination.getUsingModel());
        assertEquals("4.0.0", destination.getUsingModelVersion());
        //TO DO: add more asserts when I get time.
        log.debug(converter.toString());
    }
}
