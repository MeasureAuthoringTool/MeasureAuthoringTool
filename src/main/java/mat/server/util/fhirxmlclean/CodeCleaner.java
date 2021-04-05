package mat.server.util.fhirxmlclean;

import mat.model.cql.CQLCode;
import mat.server.util.XmlProcessor;

class CodeCleaner extends FhirCleanerBase<CQLCode> {
    protected CodeCleaner(XmlProcessor processor) {
        super(processor);
    }

    @Override
    protected String createXpath(CQLCode cqlCode) {
        String nameXpath = "[@codeName='" + cqlCode.getCodeName() + "']";
        return "//cqlLookUp/codes/code" + nameXpath;
    }
}
