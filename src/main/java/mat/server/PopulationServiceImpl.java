package mat.server;

import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.population.service.PopulationService;
import mat.server.service.MeasurePackageService;
import mat.server.util.XmlProcessor;
import mat.shared.SaveUpdateCQLResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PopulationServiceImpl extends SpringRemoteServiceServlet implements PopulationService {

    private static final long serialVersionUID = 1L;

    @Autowired
    private MeasurePackageService measurePackageService;

    @Override
    public SaveUpdateCQLResult savePopulations(String measureId, String nodeName, String nodeToReplace) {

        MeasureXmlModel model = measurePackageService.getMeasureXmlForMeasure(measureId);
        XmlProcessor xmlProcessor = new XmlProcessor(model.getXml());

        String parentNode = createParentNode(nodeName);

        String newXml = xmlProcessor.replaceNode(nodeToReplace, nodeName, parentNode);
        newXml = xmlProcessor.transform(xmlProcessor.getOriginalDoc());
        // Set the updated XML to the model
        model.setXml(newXml);
        // Persist the Modified XML
        measurePackageService.saveMeasureXml(model);

        SaveUpdateCQLResult result = new SaveUpdateCQLResult();

        if (StringUtils.isNotBlank(model.getXml())) {
            result.setXml(model.getXml());
            result.setSetId(measureId);
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
        }

        return result;
    }

    private String createParentNode(String populationTyp) {
        if (populationTyp.equals("measureObservations") || populationTyp.equals("strata")) {
            return "measure";
        } else {
            return "populations";
        }

    }

}
