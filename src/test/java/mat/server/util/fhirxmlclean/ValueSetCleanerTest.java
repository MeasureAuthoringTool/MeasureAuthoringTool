package mat.server.util.fhirxmlclean;

import mat.model.cql.CQLQualityDataSetDTO;
import mat.server.util.XmlProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

class ValueSetCleanerTest implements FhirCleanerTestHelper {
    private static final String valueSetName = "Race";
    protected static final String tag = "valuesets";
    protected static final String stringElementToFind = "name=\"" + valueSetName + '"';

    private ValueSetCleaner cleaner;
    private XmlProcessor xmlProcessor;
    private String xml;

    @BeforeEach
    void setUp() {
        xml = convertXmlToString(new XmlProcessor(getXML())); // run through transform so can compare xml strings
        xmlProcessor = new XmlProcessor(xml);
        cleaner = new ValueSetCleaner(xmlProcessor);

        assertTrue(xmlContainsOnlyOneDataElement(xml, tag, stringElementToFind));
    }

    @Test
    void dontClean() {
        List<CQLQualityDataSetDTO> elements = createUnused();
        elements.get(0).setName(valueSetName + "1");

        cleaner.cleanElements(elements);

        assertEquals(xml, convertXmlToString(xmlProcessor));
    }

    @Test
    void clean() {
        cleaner.cleanElements(createUnused());
        assertFalse(xmlContainsOnlyOneDataElement(convertXmlToString(xmlProcessor), tag, stringElementToFind));
    }

    protected static List<CQLQualityDataSetDTO> createUnused() {
        CQLQualityDataSetDTO cqlQualityDataSetDTO = new CQLQualityDataSetDTO();
        cqlQualityDataSetDTO.setName(valueSetName);
        return List.of(cqlQualityDataSetDTO);
    }
}
