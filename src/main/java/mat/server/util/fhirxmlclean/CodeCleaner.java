package mat.server.util.fhirxmlclean;

import mat.model.cql.CQLCode;
import mat.server.util.XmlProcessor;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class CodeCleaner extends FhirCleanerBase<CQLCode> {
    private static final String ALL_CODE_SYSTEMS_XPATH = "//cqlLookUp/codeSystems/codeSystem";
    private static final String ALL_CODES_XPATH = "//cqlLookUp/codes/code";

    protected CodeCleaner(XmlProcessor processor) {
        super(processor);
    }

    @Override
    protected String createXpath(CQLCode cqlCode) {
        String nameXpath = "[@codeName='" + cqlCode.getCodeName() + "']";
        return ALL_CODES_XPATH + nameXpath;
    }

    /* Call method cleanElements(List<T> elements) first before calling this */
    public void cleanUnusedCodeSystems() {
        NodeList nodeList = evaluateXpath(ALL_CODE_SYSTEMS_XPATH);

        if (nodeList != null && nodeList.getLength() > 0) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                String codeSystemName = findCodeSystemName(nodeList.item(i));
                if (!containsCodeSystem(codeSystemName)) {
                    removeNode(nodeList.item(i));
                }
            }
        }

    }

    private boolean containsCodeSystem(String codeSystemNameIn) {
        NodeList nodeList = evaluateXpath(ALL_CODES_XPATH);

        if (nodeList == null || nodeList.getLength() == 0) {
            return false;
        } else {
            for (int i = 0; i < nodeList.getLength(); i++) {
                String codeSystemName = findCodeSystemName(nodeList.item(i));

                if (codeSystemName != null && codeSystemName.equals(codeSystemNameIn)) {
                    return true;
                }
            }

            return false;
        }

    }

    private String findCodeSystemName(Node node) {
        Element ele = (Element) node;
        return ele.getAttribute("codeSystemName");
    }
}
