package mat.server.util.fhirxmlclean;

import mat.server.util.XmlProcessor;
import mat.shared.cql.model.UnusedCqlElements;

public class XmlUnusedFhirCleaner {

    public String clean(String xml, UnusedCqlElements unusedCqlElements) {
        XmlProcessor xmlProcessor = new XmlProcessor(xml);

        new ValueSetCleaner(xmlProcessor).cleanElements(unusedCqlElements.getValueSets());
        new LibraryCleaner(xmlProcessor).cleanElements(unusedCqlElements.getLibraries());
        new CodeCleaner(xmlProcessor).cleanElements(unusedCqlElements.getCodes());

        return new String(xmlProcessor.transform(xmlProcessor.getOriginalDoc()).getBytes());
    }
}


