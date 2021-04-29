package mat.server.util;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PatientBasedQdmValidator {
    private final XPath xPath;
    private final XmlProcessor xmlProcessor;

    private String measureScoring;
    private List<String> errors = new ArrayList<>();

    public PatientBasedQdmValidator(XPath xPath, XmlProcessor xmlProcessor) {
        this.xPath = xPath;
        this.xmlProcessor = xmlProcessor;
    }


    // Measure must be patientBased
    public List<String> validate(NodeList groupSDE, String measureScoring) throws XPathExpressionException {
        this.measureScoring = measureScoring;

        NodeList measureGroupingNodes = (NodeList) xPath.evaluate("/measure/measureGrouping",
                xmlProcessor.getOriginalDoc(),
                XPathConstants.NODESET);

        checkMeasureObservationCount(measureGroupingNodes.getLength());

        checkMeasureObservationCount(measureGroupingNodes.getLength());

        return errors;
    }

    private void checkMeasureObservationCount(int count) {

        switch (measureScoring) {
            case "ratio":
                checkRatioCount(count);
                break;
            case "continous_var":
                break;
            default:
                log.debug("Did not process count check for measureScoring type: {}", measureScoring);
        }

    }

    private void checkRatioCount(int count) {
        if (count > 2) {
            errors.add("Ratio Measures can only have 2 measure observations.");
        }
    }


}
