package mat.server.hqmf.qdm_5_5;

import mat.model.clause.MeasureExport;
import mat.server.hqmf.Generator;
import mat.server.hqmf.qdm.HQMFFinalCleanUp;
import mat.server.logging.LogFactory;
import mat.server.util.XmlProcessor;
import org.apache.commons.logging.Log;

import java.time.Duration;
import java.time.Instant;

/**
 * The Class CQLbasedHQMFGenerator.
 *
 * @author jmeyer
 */
public class HQMFGenerator implements Generator {
	
	private final Log logger = LogFactory.getLog(HQMFDataCriteriaGenerator.class);

	

	/**
	 * Generate hqmf for CQL Based measures (QDM version 5.5)
	 *
	 * @param me the me
	 * @return the string
	 */
	@Override
	public String generate(MeasureExport me) {
		Instant start = Instant.now();
		String hqmfXML = "";
		try {

			// MAT 6911: Export CQL based HQMF w/ Meta Data Section
			String eMeasureDetailsXML = new HQMFMeasureDetailsGenerator().generate(me);
			logger.info("HQMF::measure details generate duration::" + Duration.between(start, Instant.now()).toMillis());

			// Inline comments are added after the end of last componentOf tag.
			// This is removed in this method
			eMeasureDetailsXML = replaceInlineCommentFromEnd(eMeasureDetailsXML);
			hqmfXML += eMeasureDetailsXML;
			logger.info("HQMF::cleanup measure details::" + Duration.between(start, Instant.now()).toMillis());

			String dataCriteriaXML = new HQMFDataCriteriaGenerator().generate(me);
			hqmfXML= appendToHQMF(dataCriteriaXML, hqmfXML);
			logger.info("HQMF::data criteria duration::" + Duration.between(start, Instant.now()).toMillis());
			
			XmlProcessor hqmfProcessor = new XmlProcessor(hqmfXML);
			me.setHQMFXmlProcessor(hqmfProcessor);
			logger.info("HQMF::hqmf processor::" + Duration.between(start, Instant.now()).toMillis());
			
			//generateNarrative(me);
			hqmfXML = finalCleanUp(me);
			logger.info("HQMF::clean up::" + Duration.between(start, Instant.now()).toMillis());
		} catch (Exception e) {
			logger.error("Unable to generate HQMF for QDM v5.5. Exception Stack Strace is as followed : ");
			e.printStackTrace();
		}
		logger.info("HQMF::generate duration::" + Duration.between(start, Instant.now()).toMillis());
		return hqmfXML;
	}

	/**
	 * Inline comments are added after the end of last componentOf tag. This is
	 * removed in this method
	 * 
	 * @param eMeasureDetailsXML
	 *            - String eMeasureDetailsXML.
	 * @return String eMeasureDetailsXML.
	 */
	private String replaceInlineCommentFromEnd(String eMeasureDetailsXML) {
		int indexOfComponentOf = eMeasureDetailsXML.lastIndexOf("</componentOf>");
		eMeasureDetailsXML = eMeasureDetailsXML.substring(0, indexOfComponentOf);
		eMeasureDetailsXML = eMeasureDetailsXML.concat("</componentOf></QualityMeasureDocument>");
		return eMeasureDetailsXML;
	}
	
	/**
	 * Append to hqmf.
	 *
	 * @param dataCriteriaXML the data criteria xml
	 * @param hqmfXML the hqmf xml
	 * @return the string
	 */
	private String appendToHQMF(String dataCriteriaXML, String hqmfXML) {
		int indexOfEnd = hqmfXML.indexOf("</QualityMeasureDocument>");
		if(indexOfEnd > -1){
			hqmfXML = hqmfXML.substring(0, indexOfEnd) + dataCriteriaXML + hqmfXML.substring(indexOfEnd);
		}
		return hqmfXML;
	}
	
	/**
	 * Final clean up.
	 *
	 * @param me the me
	 * @return the string
	 */
	private String finalCleanUp(MeasureExport me) {
		HQMFFinalCleanUp.clean(me);
		return removeXmlTagNamespace(me.getHQMFXmlProcessor().transform(me.getHQMFXmlProcessor().getOriginalDoc(), true));
	}
	
	/**
	 * Removes the xml tag namespace.
	 *
	 * @param xmlString the xml string
	 * @return the string
	 */
	private String removeXmlTagNamespace(String xmlString) {
		xmlString = xmlString.replaceAll(" xmlns=\"\"", "").replaceAll("&#34;", "&quot;");
		return xmlString;
	}
}
