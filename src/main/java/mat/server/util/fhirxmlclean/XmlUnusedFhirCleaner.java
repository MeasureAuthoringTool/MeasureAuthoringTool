package mat.server.util.fhirxmlclean;

import mat.server.CQLLibraryService;
import org.slf4j.LoggerFactory;
import mat.server.util.XmlProcessor;
import mat.shared.cql.model.UnusedCqlElements;
import org.slf4j.Logger;


public class XmlUnusedFhirCleaner {
    private static final Logger log = LoggerFactory.getLogger(CQLLibraryService.class);
    XmlProcessor xmlProcessor;

    public String clean(String xml, UnusedCqlElements unusedCqlElements) {
        XmlProcessor xmlProcessor = new XmlProcessor(xml);

        new ValueSetCleaner(xmlProcessor).cleanElements(unusedCqlElements.getValueSets());
        new LibraryCleaner(xmlProcessor).cleanElements(unusedCqlElements.getLibraries());

        CodeCleaner codeCleaner = new CodeCleaner(xmlProcessor);

        codeCleaner.cleanUnusedCodeSystems();

        codeCleaner.cleanElements(unusedCqlElements.getCodes());

        String cleanedXml = new String(xmlProcessor.transform(xmlProcessor.getOriginalDoc()).getBytes());
        log.debug(cleanedXml);
        return cleanedXml;
    }
}


