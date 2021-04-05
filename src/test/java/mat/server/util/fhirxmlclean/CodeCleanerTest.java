package mat.server.util.fhirxmlclean;

import mat.model.cql.CQLCode;
import mat.server.util.XmlProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CodeCleanerTest implements FhirCleanerTestHelper {
    private final static String codeName = "Birth date";
    protected final static String tag = "codes";
    protected final static String stringElementToFind = "codeName=\"" + codeName + '"';

    private CodeCleaner cleaner;
    private XmlProcessor processor;
    private String xml;

    @BeforeEach
    void setUp() {
        xml = convertXmlToString(new XmlProcessor(getXML())); // run through transform so can compare xml strings
        processor = new XmlProcessor(xml);

        cleaner = new CodeCleaner(processor);

        assertTrue(xmlContainsOnlyOneDataElement(xml, tag, stringElementToFind));
    }

    @Test
    void dontClean() {
        List<CQLCode> elements = createUnused();
        elements.get(0).setName(codeName + "1");

        cleaner.cleanElements(elements);

        assertEquals(xml, convertXmlToString(processor));
    }

    @Test
    void clean() {
        cleaner.cleanElements(createUnused());
        assertFalse(xmlContainsOnlyOneDataElement(convertXmlToString(processor), tag, stringElementToFind));
    }

    protected static List<CQLCode> createUnused() {
        CQLCode cqlCode = new CQLCode();
        cqlCode.setCodeName(codeName);
        return List.of(cqlCode);
    }
}
