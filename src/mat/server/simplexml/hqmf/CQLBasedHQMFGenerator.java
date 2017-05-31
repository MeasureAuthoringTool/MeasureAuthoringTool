package mat.server.simplexml.hqmf;

import mat.model.clause.MeasureExport;
import mat.server.util.XmlProcessor;

// TODO: Auto-generated Javadoc
/**
 * The Class CQLbasedHQMFGenerator.
 *
 * @author jmeyer
 */
public class CQLBasedHQMFGenerator implements Generator {
	

	/**
	 * Generate hqmf for CQL Based measures (version 5.0 and greater)
	 *
	 * @param me the me
	 * @return the string
	 */
	@Override
	public String generate(MeasureExport me) {
		
		String hqmfXML = "";
		try {

			// MAT 6911: Export CQL based HQMF w/ Meta Data Section
			String eMeasureDetailsXML = new CQLBasedHQMFMeasureDetailsGenerator().generate(me);
			// Inline comments are added after the end of last componentOf tag.
			// This is removed in this method
			eMeasureDetailsXML = replaceInlineCommentFromEnd(eMeasureDetailsXML);
			hqmfXML += eMeasureDetailsXML;

			String dataCriteriaXML = new CQLBasedHQMFDataCriteriaGenerator().generate(me);
			hqmfXML= appendToHQMF(dataCriteriaXML, hqmfXML);
			
			XmlProcessor hqmfProcessor = new XmlProcessor(hqmfXML);
			me.setHQMFXmlProcessor(hqmfProcessor);
			
			//generateNarrative(me);
			hqmfXML = finalCleanUp(me);
			
			// TODO Export more Stuff

		} catch (Exception e) {
			LOGGER.error("Unable to generate human readable. Exception Stack Strace is as followed : ");
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
