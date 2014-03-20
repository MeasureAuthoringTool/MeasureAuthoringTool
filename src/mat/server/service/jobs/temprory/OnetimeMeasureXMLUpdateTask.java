package mat.server.service.jobs.temprory;

import java.util.List;
import javax.xml.xpath.XPathExpressionException;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.dao.MatFlagDAO;
import mat.dao.clause.MeasureDAO;
import mat.model.MatFlag;
import mat.model.clause.Measure;
import mat.server.service.MeasureLibraryService;
import mat.server.service.MeasurePackageService;
import mat.server.util.XmlProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;

// TODO: Auto-generated Javadoc
/**
 * The Class OnetimeMeasureXMLUpdateTask.
 */
public class OnetimeMeasureXMLUpdateTask implements ApplicationContextAware{
	
	/** The Constant logger. */
	private static final Log logger = LogFactory
			.getLog(OnetimeMeasureXMLUpdateTask.class);
	
	/** The measure xmldao. */
	private MeasureDAO measureDAO;
	
	/** The measure library service. */
	private MeasureLibraryService measureLibraryService;
	
	/** The measure package service. */
	private MeasurePackageService measurePackageService;
	
	/** The mat flag dao. */
	private MatFlagDAO matFlagDAO;
	
	/** The application context. */
	private ApplicationContext applicationContext;
	
	/**
	 * Update measure xmls for based on new changes for Scoring type.
	 * Also update measure XML's for IPP to IP renaming.
	 * 
	 * This is not the most efficient way to achieve the measure XML updates, but we
	 * are setting this as a class which will be run once and then discarded.
	 * And so we are trying to reuse the already written code in service classes.
	 * The effort to reuse makes us do repeated database reads between classes.
	 */
	public void updateMeasureXMLs() {
		logger.info("Starting one time update Measure XMLs For Timing Elements task....");
		
		if (!doesJobNeedExecution()) {
			logger.info("Figured that one time update Measure XMLs task doesnt need execution...stopping task.");
			return;
		}
		
		logger.info("Get all Measure Ids");
		//Measure measure = getMeasureDAO().find("8a4d92813418f6e201341a590f460438");
		List<Measure> measureList = getMeasureDAO().find();
		//List<Measure> measureList = new ArrayList<Measure>();
		//measureList.add(measure);
		logger.info("\r\n\r\nUpdating all Measure XML's based to replace IPP by IP and save them back.");
		updateIPP_To_IP(measureList);
		
		logger.info("\r\n\r\nUpdating all Measure XML's based to replace SBOD by SBE and save them back.");
		renameTimingConventions(measureList);
		
		logger.info("\r\n\r\nUpdating all Measure XML's based on the scoring types and save them back.");
		checkForScoringAndUpdate(measureList);
		
		//logger.info("\r\n\r\nUpdating all Measure XML's based on the Ratio scoring type and save them back.");
		//checkForRatioMeasures(measureList);
		
	}
	
	/**
	 * Add Measure Observations in Ratio Measure types.
	 * @param measureList - List.
	 */
	/*private void checkForRatioMeasures(List<Measure> measureList) {
		List <Measure> ratioMeasures = new ArrayList<Measure>();
		for (Measure measure : measureList) {
			if(measure.getMeasureScoring().equalsIgnoreCase("ratio")){
				ratioMeasures.add(measure);
			}
		}
		checkForScoringAndUpdate(ratioMeasures);
	}*/
	
	/**
	 * Check for scoring and update.
	 *
	 * @param measureList the measure list
	 */
	private void checkForScoringAndUpdate(List<Measure> measureList) {
		for (Measure measure : measureList) {
			logger.info("\r\n\r\nCheck for scoring for Measure:"+measure.getaBBRName());
			MeasureXmlModel measureXmlModel = getMeasureLibraryService().getMeasureXmlForMeasure(measure.getId());
			if((measureXmlModel == null) || (measureXmlModel.getXml() == null) || (measureXmlModel.getXml().trim().length() == 0)){
				logger.info("	Measure XML for:"+measure.getaBBRName()+" is blank or null. Skipping this measure.");
				continue;
			}
			XmlProcessor xmlProcessor = new XmlProcessor(measureXmlModel.getXml());
			
			xmlProcessor.checkForScoringType();
			measureXmlModel.setXml(xmlProcessor.transform(xmlProcessor.getOriginalDoc()));
			getMeasurePackageService().saveMeasureXml(measureXmlModel);
		}
	}
	
	/**
	 * Update ip p_ to_ ip.
	 *
	 * @param measureList the measure list
	 */
	private void updateIPP_To_IP(List<Measure> measureList) {
		for (Measure measure : measureList) {
			logger.info("\r\n\r\nUpdate IPP to IP for Measure:"+measure.getaBBRName());
			MeasureXmlModel measureXmlModel = getMeasureLibraryService()
					.getMeasureXmlForMeasure(measure.getId());
			
			if((measureXmlModel == null) || (measureXmlModel.getXml() == null) || (measureXmlModel.getXml().trim().length() == 0)){
				logger.info("	Measure XML for:"+measure.getaBBRName()+" is blank or null. Skipping this measure.");
				continue;
			}
			// update Initial Patient Population to Initial Population
			
			XmlProcessor xmlProcessor = new XmlProcessor(
					measureXmlModel.getXml());
			try {
				xmlProcessor.renameIPP_To_IP(xmlProcessor.getOriginalDoc());
				String measureXML = xmlProcessor.transform(xmlProcessor
						.getOriginalDoc());
				measureXmlModel.setXml(measureXML);
				getMeasurePackageService().saveMeasureXml(measureXmlModel);
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Rename timing conventions.
	 *
	 * @param measureList the measure list
	 */
	private void renameTimingConventions(List<Measure> measureList){
		for (Measure measure : measureList) {
			logger.info("\r\n\r\nUpdate SBOD to SBE for Measure:"+measure.getaBBRName());
			MeasureXmlModel measureXmlModel = getMeasureLibraryService()
					.getMeasureXmlForMeasure(measure.getId());
			
			if((measureXmlModel == null) || (measureXmlModel.getXml() == null) || (measureXmlModel.getXml().trim().length() == 0)){
				logger.info("	Measure XML for:"+measure.getaBBRName()+" is blank or null. Skipping this measure.");
				continue;
			}
			// update Starts Before or During to Starts Before End and
			// Ends Before or During to Ends Before End
			
			XmlProcessor xmlProcessor = new XmlProcessor(
					measureXmlModel.getXml());
			try {
				xmlProcessor.renameTimingConventions(xmlProcessor.getOriginalDoc());
				String measureXML = xmlProcessor.transform(xmlProcessor
						.getOriginalDoc());
				measureXmlModel.setXml(measureXML);
				getMeasurePackageService().saveMeasureXml(measureXmlModel);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		}
		
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
		
		if(applicationContext instanceof WebApplicationContext){
			String contextPath = ((WebApplicationContext)applicationContext).getServletContext().getContextPath();
			System.out.println("$%$%$%$%$%$%$%$%$%$%$%$%$%$%$%----------->ContextPath:"+contextPath);
			
			String realPath = ((WebApplicationContext)applicationContext).getServletContext().getRealPath("Login.html");
			System.out.println("$%$%$%$%$%$%$%$%$%$%$%$%$%$%$%----------->realPath:"+realPath);
			
			String realContextPath = ((WebApplicationContext)applicationContext).getServletContext().getRealPath(contextPath);
			System.out.println("$%$%$%$%$%$%$%$%$%$%$%$%$%$%$%----------->realContextPath:"+realContextPath);
			
			String serverInfo = ((WebApplicationContext)applicationContext).getServletContext().getServerInfo();
			System.out.println("$%$%$%$%$%$%$%$%$%$%$%$%$%$%$%----------->serverInfo:"+serverInfo);
			
			String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = ((WebApplicationContext) applicationContext).ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE;
			System.out.println("$%$%$%$%$%$%$%$%$%$%$%$%$%$%$%----------->ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE:"+ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
			
			String contextClassName = applicationContext.getClass().getName();
			System.out.println("$%$%$%$%$%$%$%$%$%$%$%$%$%$%$%----------->contextClassName:"+contextClassName);
		}
		
		return result;
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
	
	/**
	 * Sets the measure dao.
	 *
	 * @param measureDAO the new measure dao
	 */
	public void setMeasureDAO(MeasureDAO measureDAO) {
		this.measureDAO = measureDAO;
	}
	
	/**
	 * Gets the measure dao.
	 *
	 * @return the measure dao
	 */
	public MeasureDAO getMeasureDAO() {
		return measureDAO;
	}
	
	/**
	 * Sets the measure package service.
	 *
	 * @param measurePackageService the new measure package service
	 */
	public void setMeasurePackageService(MeasurePackageService measurePackageService) {
		this.measurePackageService = measurePackageService;
	}
	
	/**
	 * Gets the measure package service.
	 *
	 * @return the measure package service
	 */
	public MeasurePackageService getMeasurePackageService() {
		return measurePackageService;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		applicationContext = arg0;
		
	}
}
