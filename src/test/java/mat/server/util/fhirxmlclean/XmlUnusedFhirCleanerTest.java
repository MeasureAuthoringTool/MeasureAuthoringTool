package mat.server.util.fhirxmlclean;

import mat.server.util.XmlProcessor;
import mat.shared.cql.model.UnusedCqlElements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class XmlUnusedFhirCleanerTest implements FhirCleanerTestHelper {
    private String xml;
    private XmlUnusedFhirCleaner xmlUnusedFhirCleaner;

    @BeforeEach
    void setUp() {
        xml = convertXmlToString(new XmlProcessor(getXML())); // run through transform so can compare xml strings
        xmlUnusedFhirCleaner = new XmlUnusedFhirCleaner();

        assertTrue(xmlContainsOnlyOneDataElement(xml,
                ValueSetCleanerTest.tag,
                ValueSetCleanerTest.stringElementToFind));

        assertTrue(xmlContainsOnlyOneDataElement(xml,
                LibraryCleanerTest.tag,
                LibraryCleanerTest.stringElementToFind1));

        assertTrue(xmlContainsOnlyOneDataElement(xml,
                CodeCleanerTest.tag,
                CodeCleanerTest.stringElementToFind));
    }

    @Test
    void dontClean() {
        UnusedCqlElements unusedCqlElements = new UnusedCqlElements();

        String cleanedXml = xmlUnusedFhirCleaner.clean(xml, unusedCqlElements);
        assertEquals(xml, cleanedXml);
    }

    @Test
    void clean() {
        UnusedCqlElements unusedCqlElements = new UnusedCqlElements();
        unusedCqlElements.setValueSets(ValueSetCleanerTest.createUnused());
        unusedCqlElements.setLibraries(LibraryCleanerTest.createUnused());
        unusedCqlElements.setCodes(CodeCleanerTest.createUnused());

        String cleanedXml = xmlUnusedFhirCleaner.clean(xml, unusedCqlElements);

        assertFalse(xmlContainsOnlyOneDataElement(cleanedXml,
                ValueSetCleanerTest.tag,
                ValueSetCleanerTest.stringElementToFind));

        assertFalse(xmlContainsOnlyOneDataElement(cleanedXml,
                LibraryCleanerTest.tag,
                LibraryCleanerTest.stringElementToFind1));

        assertFalse(xmlContainsOnlyOneDataElement(cleanedXml,
                CodeCleanerTest.tag,
                CodeCleanerTest.stringElementToFind));
    }
}