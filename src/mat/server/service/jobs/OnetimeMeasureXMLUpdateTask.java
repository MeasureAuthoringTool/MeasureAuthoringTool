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

/**
 * The Class OnetimeMeasureXMLUpdateTask.
 */
public class OnetimeMeasureXMLUpdateTask {

	/** The Constant logger. */
	private static final Log logger = LogFactory
			.getLog(OnetimeMeasureXMLUpdateTask.class);
	
	/** The measure xmldao. */
	private MeasureXMLDAO measureXMLDAO;
	
	/** The measure library service. */
	private MeasureLibraryService measureLibraryService;
	
	/** The mat flag dao. */
	private MatFlagDAO matFlagDAO;

	/**
	 * Update measure xm ls for timing elements.
	 */
	public void updateMeasureXMLsForTimingElements() {
		logger.info("Starting one time update Measure XMLs For Timing Elements task....");

		if (!doesJobNeedExecution()) {
			logger.info("Figured that one time update Measure XMLs For Timing Elements task doesnt need execution...stopping task.");
			return;
		}

		List<MeasureXML> allMeasureXML_List = getMeasureXMLDAO().find();

		for (MeasureXML measureXML : allMeasureXML_List) {

			try {
				addTimingElement(measureXML);
			} catch (Exception e) {
				logger.info("Exception encountered for Measure Id:"
						+ measureXML.getMeasure_id() + "." + e.getMessage());
			}
		}
	}

	/**
	 * Adds the timing element.
	 * 
	 * @param measureXML
	 *            the measure xml
	 * @throws Exception
	 *             the exception
	 */
	private void addTimingElement(MeasureXML measureXML) throws Exception {
		logger.info("Updating Measure XML Timing Elements for Measure Id:"
				+ measureXML.getMeasure_id());

		// if (!measureXML.getMeasure_id().equals(
		// "8ae45366418fb6f901418fbcfc60000b")) {
		// continue;
		// }

		String xml = measureXML.getMeasureXMLAsString();
		XmlProcessor xmlProcessor = new XmlProcessor(xml);

		getMeasureLibraryService()
				.checkForTimingElementsAndAppend(xmlProcessor);

		measureXML.setMeasureXMLAsByteArray(xmlProcessor.transform(xmlProcessor
				.getOriginalDoc()));
		getMeasureXMLDAO().save(measureXML);
	}

	/**
	 * Does job need execution.
	 * 
	 * @return true, if successful
	 */
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

	/**
	 * Sets the measure xmldao.
	 * 
	 * @param measureXMLDAO
	 *            the new measure xmldao
	 */
	public void setMeasureXMLDAO(MeasureXMLDAO measureXMLDAO) {
		this.measureXMLDAO = measureXMLDAO;
	}

	/**
	 * Gets the measure xmldao.
	 * 
	 * @return the measure xmldao
	 */
	public MeasureXMLDAO getMeasureXMLDAO() {
		return measureXMLDAO;
	}

	/**
	 * Sets the measure library service.
	 * 
	 * @param measureLibraryService
	 *            the new measure library service
	 */
	public void setMeasureLibraryService(
			MeasureLibraryService measureLibraryService) {
		this.measureLibraryService = measureLibraryService;
	}

	/**
	 * Gets the measure library service.
	 * 
	 * @return the measure library service
	 */
	public MeasureLibraryService getMeasureLibraryService() {
		return measureLibraryService;
	}

	/**
	 * Sets the mat flag dao.
	 * 
	 * @param matFlagDAO
	 *            the new mat flag dao
	 */
	public void setMatFlagDAO(MatFlagDAO matFlagDAO) {
		this.matFlagDAO = matFlagDAO;
	}

	/**
	 * Gets the mat flag dao.
	 * 
	 * @return the mat flag dao
	 */
	public MatFlagDAO getMatFlagDAO() {
		return matFlagDAO;
	}
}
