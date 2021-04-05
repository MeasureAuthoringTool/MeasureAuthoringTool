package mat.server.util.fhirxmlclean;

import mat.model.cql.CQLIncludeLibrary;
import mat.server.util.XmlProcessor;

class LibraryCleaner extends FhirCleanerBase<CQLIncludeLibrary> {
    protected LibraryCleaner(XmlProcessor processor) {
        super(processor);
    }

    @Override
    protected String createXpath(CQLIncludeLibrary valueSet) {
        String nameXpath = "[@name='" + valueSet.getAliasName() + "']";
        return "//cqlLookUp/includeLibrarys/includeLibrary" + nameXpath;
    }
}
