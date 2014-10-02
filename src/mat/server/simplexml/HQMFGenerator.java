package mat.server.simplexml;

import mat.model.clause.MeasureExport;
import mat.server.service.impl.XMLUtility;

// TODO: Auto-generated Javadoc
/**
 * The Class HQMFGenerator.
 */
public class HQMFGenerator {

	
	/** The Constant conversionFileForHQMF_Header. */
	private static final String conversionFileForHQMF_Header = "xsl/new_measureDetails.xsl";
	
	/**
	 * Generate hqm ffor measure.
	 *
	 * @param me the me
	 * @return the string
	 */
	public static String generateEMeasureXMLforMeasure(MeasureExport me){
		String hqmfXML = "";
     
		String eMeasureDetailsXML = generateEMeasureXMLforMsrDetails(me);
		hqmfXML += eMeasureDetailsXML;
		
		String dataCriteriaXML = HQMFDataCriteriaGenerator.generateHQMFforMeasure(me);
		hqmfXML= appendToHQMF(dataCriteriaXML, hqmfXML);		
		
		return hqmfXML;
	}
	
	
	/**
	 * Generate e measure xm lfor msr details.
	 *
	 * @param me the me
	 * @return the string
	 */
	private static String generateEMeasureXMLforMsrDetails(MeasureExport me){
		XMLUtility xmlUtility = new XMLUtility();
		String measureDetailsHQMF_XML = xmlUtility.applyXSL(me.getSimpleXML(),
				xmlUtility.getXMLResource(conversionFileForHQMF_Header));
		return measureDetailsHQMF_XML;
	}
	
	private static String appendToHQMF(String dataCriteriaXML, String hqmfXML) {
		
		int indexOfEnd = hqmfXML.indexOf("</QualityMeasureDocument>");
		if(indexOfEnd > -1){
			hqmfXML = hqmfXML.substring(0, indexOfEnd) + dataCriteriaXML + hqmfXML.substring(indexOfEnd);
		}
		
		return hqmfXML;
	}

	
}
