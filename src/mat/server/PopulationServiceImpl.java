package mat.server;

import org.apache.commons.lang3.StringUtils;

import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.population.service.PopulationService;
import mat.server.service.MeasurePackageService;
import mat.server.util.XmlProcessor;
import mat.shared.SaveUpdateCQLResult;

public class PopulationServiceImpl extends SpringRemoteServiceServlet implements PopulationService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Gets the service.
	 * 
	 * @return the service
	 */
	private MeasurePackageService getService() {
		return (MeasurePackageService) context.getBean("measurePackageService");
	}

	@Override
	public SaveUpdateCQLResult savePopulations(String measureId, String nodeName, String nodeToReplace) {
		
		MeasureXmlModel model = getService().getMeasureXmlForMeasure(measureId);
		XmlProcessor xmlProcessor = new XmlProcessor(model.getXml());

		String parentNode = "";
		if(isMeasureObservations(nodeName)) {
			parentNode = "measure";
		}else {
			parentNode = "populations";
		}
		
		String newXml = xmlProcessor.replaceNode(nodeToReplace, nodeName, parentNode);
		newXml = xmlProcessor.transform(xmlProcessor.getOriginalDoc());

		model.setXml(newXml);
		
		//TODO:uncomment the below line after unit testing
		//System.out.println("NEW XML: " + newXml);
		getService().saveMeasureXml(model);

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

	private boolean isMeasureObservations(String name) {
		return name.equals("measureObservations");
	}

}
