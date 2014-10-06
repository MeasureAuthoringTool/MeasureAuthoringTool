package mat.server.simplexml.hqmf;

import mat.model.clause.MeasureExport;

// TODO: Auto-generated Javadoc
/**
 * The Class HQMFGenerator.
 */
public class HQMFGenerator implements Generator {

	/**
	 * Generate hqm for measure.
	 *
	 * @param me the me
	 * @return the string
	 */
	@Override
	public String generate(MeasureExport me){
		String hqmfXML = "";
     
		String eMeasureDetailsXML = new HQMFMeasureDetailsGenerator().generate(me);
		hqmfXML += eMeasureDetailsXML;
		
		String dataCriteriaXML = new HQMFDataCriteriaGenerator().generate(me);
		hqmfXML= appendToHQMF(dataCriteriaXML, hqmfXML);		
		
		return hqmfXML;
	}
	
	private String appendToHQMF(String dataCriteriaXML, String hqmfXML) {		
		int indexOfEnd = hqmfXML.indexOf("</QualityMeasureDocument>");
		if(indexOfEnd > -1){
			hqmfXML = hqmfXML.substring(0, indexOfEnd) + dataCriteriaXML + hqmfXML.substring(indexOfEnd);
		}
		return hqmfXML;
	}

	
}
