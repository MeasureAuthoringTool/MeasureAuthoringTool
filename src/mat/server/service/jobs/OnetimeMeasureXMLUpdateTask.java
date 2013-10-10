package mat.server.service.jobs;

import java.util.List;

import mat.dao.MatFlagDAO;
import mat.dao.clause.MeasureXMLDAO;
import mat.model.MatFlag;
import mat.model.clause.MeasureXML;
import mat.server.service.MeasureLibraryService;
import mat.server.util.XmlProcessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OnetimeMeasureXMLUpdateTask {

	private static final Log logger = LogFactory
			.getLog(OnetimeMeasureXMLUpdateTask.class);
	private MeasureXMLDAO measureXMLDAO;
	private MeasureLibraryService measureLibraryService;
	private MatFlagDAO matFlagDAO;

	public void updateMeasureXMLsForTimingElements() {
		logger.info("Starting one time update Measure XMLs For Timing Elements task....");

		if (!doesJobNeedExecution()) {
			logger.info("Figured that one time update Measure XMLs For Timing Elements task doesnt need execution...stopping task.");
			return;
		}

		List<MeasureXML> allMeasureXML_List = getMeasureXMLDAO().find();

		for (MeasureXML measureXML : allMeasureXML_List) {

			logger.info("Updating Measure XML Timing Elements for Measure Id:"
					+ measureXML.getMeasure_id());

			// if (!measureXML.getMeasure_id().equals(
			// "8ae45366418fb6f901418fbcfc60000b")) {
			// continue;
			// }

			String xml = measureXML.getMeasureXMLAsString();
			XmlProcessor xmlProcessor = new XmlProcessor(xml);

			getMeasureLibraryService().checkForTimingElementsAndAppend(
					xmlProcessor);

			measureXML.setMeasureXMLAsByteArray(xmlProcessor
					.transform(xmlProcessor.getOriginalDoc()));
			getMeasureXMLDAO().save(measureXML);
		}
	}

	private boolean doesJobNeedExecution() {
		boolean result = false;

		MatFlag flag = matFlagDAO.find().get(0);
		if (flag.getFlag().equals("0")) {
			flag.setFlag("1");
			matFlagDAO.save(flag);
			result = true;
		}

		return result;
	}

	public void setMeasureXMLDAO(MeasureXMLDAO measureXMLDAO) {
		this.measureXMLDAO = measureXMLDAO;
	}

	public MeasureXMLDAO getMeasureXMLDAO() {
		return measureXMLDAO;
	}

	public void setMeasureLibraryService(
			MeasureLibraryService measureLibraryService) {
		this.measureLibraryService = measureLibraryService;
	}

	public MeasureLibraryService getMeasureLibraryService() {
		return measureLibraryService;
	}

	public void setMatFlagDAO(MatFlagDAO matFlagDAO) {
		this.matFlagDAO = matFlagDAO;
	}

	public MatFlagDAO getMatFlagDAO() {
		return matFlagDAO;
	}
}
