package mat.server.util.fhirxmlclean;

import mat.model.cql.CQLIncludeLibrary;
import mat.server.util.XmlProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

class LibraryCleanerTest implements FhirCleanerTestHelper {
    private final static String libraryAliasName1 = "SDE";
    protected final static String stringElementToFind1 = "name=\"" + libraryAliasName1 + '"';
    protected final static String tag = "includeLibrarys";

    private final static String libraryAliasName2 = "Global";
    private final static String stringElementToFind2 = "name=\"" + libraryAliasName2 + '"';

    private LibraryCleaner cleaner;
    private XmlProcessor processor;
    private String xml;

    @BeforeEach
    void setUp() {
        xml = convertXmlToString(new XmlProcessor(getXML())); // run through transform so can compare xml strings
        processor = new XmlProcessor(xml);
        cleaner = new LibraryCleaner(processor);

        assertTrue(xmlContainsOnlyOneDataElement(xml, tag, stringElementToFind1));
        assertTrue(xmlContainsOnlyOneDataElement(xml, tag, stringElementToFind2));
    }

    @Test
    void dontClean() {
        List<CQLIncludeLibrary> elements = createUnused();
        elements.get(0).setAliasName(libraryAliasName1 + "1");
        elements.get(1).setAliasName(libraryAliasName2 + "1");

        cleaner.cleanElements(elements);

        assertEquals(xml, convertXmlToString(processor));
    }

    @Test
    void clean() {
        cleaner.cleanElements(createUnused());
        String cleanedXml = convertXmlToString(processor);

        assertFalse(xmlContainsOnlyOneDataElement(cleanedXml, tag, stringElementToFind1));
        assertFalse(xmlContainsOnlyOneDataElement(cleanedXml, tag, stringElementToFind2));
    }

    protected static List<CQLIncludeLibrary> createUnused() {
        CQLIncludeLibrary library1 = new CQLIncludeLibrary();
        library1.setAliasName(libraryAliasName1);

        CQLIncludeLibrary library2 = new CQLIncludeLibrary();
        library2.setAliasName(libraryAliasName2);

        return List.of(library1, library2);
    }
}
