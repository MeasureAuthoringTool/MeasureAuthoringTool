package mat.server.util.fhirxmlclean;

import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.server.util.XmlProcessor;

class LibraryCleaner extends FhirCleanerBase<CQLIncludeLibrary> {
    protected LibraryCleaner(XmlProcessor processor) {
        super(processor);
    }

    @Override
    String createXpath(CQLIncludeLibrary valueSet) {
        String nameXpath = "[@name='" + valueSet.getAliasName() + "']";
        return "//cqlLookUp/includeLibrarys/includeLibrary" + nameXpath;
    }
}
