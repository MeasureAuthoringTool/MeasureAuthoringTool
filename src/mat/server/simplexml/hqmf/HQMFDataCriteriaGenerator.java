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
		return removePreambleAndRootTags(dataCriteriaXMLProcessor.transform(dataCriteriaXMLProcessor.getOriginalDoc(), true));
	}
	
	/**
	 * @param xmlString
	 * @return
	 */
	private String removePreambleAndRootTags(String xmlString) {
		xmlString = xmlString.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim()
				.replaceAll("(<\\?[^<]*\\?>)?", "");/* remove preamble */
		xmlString = xmlString.replaceAll("<root>", "").replaceAll("</root>","");
		return xmlString;
	}
}
