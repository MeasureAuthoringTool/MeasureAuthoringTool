package mat.server.simplexml.hqmf;

import mat.model.clause.MeasureExport;
import mat.server.util.XmlProcessor;

/**
 * The Class HQMFDataCriteriaGenerator.
 */
public class HQMFDataCriteriaGenerator implements Generator {
	
	/**
	 * Generate hqmf for measure.
	 *
	 * @param me            the me
	 * @return the string
	 * @throws Exception the exception
	 */
	@Override
	public String generate(MeasureExport me) throws Exception {
		
		HQMFDataCriteriaElementGenerator hqmfDataCriteriaElementGenerator = new HQMFDataCriteriaElementGenerator();
		hqmfDataCriteriaElementGenerator.generate(me);
		
		HQMFClauseLogicGenerator hqmfClauseLogicGenerator = new HQMFClauseLogicGenerator();
		hqmfClauseLogicGenerator.generate(me);
		HQMFPopulationLogicGenerator hqmfPopulationLogicGenerator = new HQMFPopulationLogicGenerator();
		hqmfPopulationLogicGenerator.generate(me);
		XmlProcessor dataCriteriaXMLProcessor = me.getHQMFXmlProcessor();
		return removeXmlTagNamespaceAndPreamble(dataCriteriaXMLProcessor.transform(dataCriteriaXMLProcessor.getOriginalDoc(), true));
	}
	
	/**
	 * @param xmlString
	 * @return
	 */
	private String removeXmlTagNamespaceAndPreamble(String xmlString) {
		xmlString = xmlString.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim()
				.replaceAll("(<\\?[^<]*\\?>)?", "");/* remove preamble */
								
		xmlString = xmlString.replaceAll("<root>", "").replaceAll("</root>","");
		
		String componentTag = "<component";
		int indx = xmlString.indexOf(componentTag);
		while(indx > -1){
			int indx2 = xmlString.indexOf(">", indx);
			xmlString = xmlString.substring(0, indx+componentTag.length()) + xmlString.substring(indx2);
			indx = xmlString.indexOf(componentTag,indx2);
		}
		
		return xmlString;
	}
}
