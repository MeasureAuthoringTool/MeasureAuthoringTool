package mat.server.util.fhirxmlclean;

import lombok.SneakyThrows;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.server.util.XmlProcessor;
import org.apache.commons.collections.CollectionUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.util.List;

public abstract class FhirCleanerBase<T> {
    static final javax.xml.xpath.XPath xPathEngine = XPathFactory.newInstance().newXPath();

    final XmlProcessor processor;

    protected FhirCleanerBase(XmlProcessor processor) {
        this.processor = processor;
    }

    public void cleanElements(List<T> valueSets) {
        if (!CollectionUtils.isEmpty(valueSets)) {
            valueSets.forEach(this::cleanElement);
        }
    }

    abstract String createXpath(T fhirType);

    private void cleanElement(T fhirType) {
        String xpath = createXpath(fhirType);
        evaluateAndClean(xpath);
    }

    @SneakyThrows
    private void evaluateAndClean(String xpath) {
        NodeList nodeList = (NodeList) xPathEngine.evaluate(xpath, processor.getOriginalDoc(), XPathConstants.NODESET);

        if (nodeList != null && nodeList.getLength() > 0) {

            if (nodeList.getLength() > 1) {
                throw new IllegalAccessException("Found more than one node with xpath: " + xpath);
            }

            Node node = nodeList.item(0);
            Node parent = node.getParentNode();
            parent.removeChild(node);
        }
    }
}
