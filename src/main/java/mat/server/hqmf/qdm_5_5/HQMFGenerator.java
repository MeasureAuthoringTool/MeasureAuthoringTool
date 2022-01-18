package mat.server.hqmf.qdm_5_5;

import mat.model.clause.MeasureExport;
import mat.server.hqmf.Generator;
import mat.server.hqmf.qdm.HQMFFinalCleanUp;
import org.slf4j.LoggerFactory;
import mat.server.util.XmlProcessor;
import org.slf4j.Logger;

/**
 * @deprecated
 * The Class CQLbasedHQMFGenerator.
 *
 * @author jmeyer
 */
public class HQMFGenerator implements Generator {
	
	private final Logger logger = LoggerFactory.getLogger(HQMFDataCriteriaGenerator.class);

	

	/**
	 * Generate hqmf for CQL Based measures (QDM version 5.5)
	 *
	 * @param me the me
	 * @return the string
	 */
	@Override
	public String generate(MeasureExport me) {
		
		String hqmfXML = "";
		try {

			// MAT 6911: Export CQL based HQMF w/ Meta Data Section
			String eMeasureDetailsXML = new HQMFMeasureDetailsGenerator().generate(me);
			// Inline comments are added after the end of last componentOf tag.
			// This is removed in this method
			eMeasureDetailsXML = replaceInlineCommentFromEnd(eMeasureDetailsXML);
			hqmfXML += eMeasureDetailsXML;

			String dataCriteriaXML = new HQMFDataCriteriaGenerator().generate(me);
			hqmfXML= appendToHQMF(dataCriteriaXML, hqmfXML);
			
			XmlProcessor hqmfProcessor = new XmlProcessor(hqmfXML);
			me.setHQMFXmlProcessor(hqmfProcessor);
			
			//generateNarrative(me);
			hqmfXML = finalCleanUp(me);
		} catch (Exception e) {
			logger.error("Unable to generate HQMF for QDM v5.5. Exception Stack Strace is as followed : ");
			e.printStackTrace();
		}
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
