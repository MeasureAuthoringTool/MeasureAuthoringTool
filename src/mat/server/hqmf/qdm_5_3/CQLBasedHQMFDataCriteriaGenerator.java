package mat.server.hqmf.qdm_5_3;

import mat.model.clause.MeasureExport;
import mat.server.hqmf.qdm.Generator;
import mat.server.util.XmlProcessor;

/**
 * The Class HQMFDataCriteriaGenerator.
 */
public class CQLBasedHQMFDataCriteriaGenerator implements Generator {
	
	/**
	 * Generate hqmf for measure.
	 *
	 * @param me            the me
	 * @return the string
	 * @throws Exception the exception
	 */
	@Override
	public String generate(MeasureExport me) throws Exception {
		
		CQLBasedHQMFDataCriteriaElementGenerator cqlBasedHQMFDataCriteriaElementGenerator = new CQLBasedHQMFDataCriteriaElementGenerator();
		cqlBasedHQMFDataCriteriaElementGenerator.generate(me);
		
		CQLBasedHQMFPopulationLogicGenerator cqlBasedHQMFPopulationLogicGenerator = new CQLBasedHQMFPopulationLogicGenerator();
		cqlBasedHQMFPopulationLogicGenerator.generate(me);
		
		CQLBasedHQMFMeasureObservationLogicGenerator cqlBasedHQMFMeasureObservationLogicGenerator = new CQLBasedHQMFMeasureObservationLogicGenerator();
		cqlBasedHQMFMeasureObservationLogicGenerator.generate(me);
		
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

	//Strip out 'Occurrence A_' at the start of qdmName If found.
	public static String removeOccurrenceFromName(String qdmName){
		String regExpression = "Occurrence [A-Z]_.*";
		String newQdmName = qdmName;
		if(newQdmName.matches(regExpression)){
			newQdmName = newQdmName.substring(newQdmName.indexOf('_')+1);
		}
		return newQdmName;
	}
}
