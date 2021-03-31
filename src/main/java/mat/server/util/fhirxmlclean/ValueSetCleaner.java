package mat.server.util.fhirxmlclean;

import mat.model.cql.CQLQualityDataSetDTO;
import mat.server.util.XmlProcessor;

class ValueSetCleaner extends FhirCleanerBase<CQLQualityDataSetDTO> {
    protected ValueSetCleaner(XmlProcessor processor) {
        super(processor);
    }

    @Override
    String createXpath(CQLQualityDataSetDTO valueSet) {
        String nameXpath = "[@name='" + valueSet.getName() + "']";
        return "//cqlLookUp/valuesets/valueset" + nameXpath;
    }
}
