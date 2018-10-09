package mat.server.hqmf.qdm;

import mat.model.clause.MeasureExport;
import mat.server.hqmf.Generator;
import mat.server.util.XmlProcessor;

/**
 * @deprecated this class is deprecated since it is an old version of QDM. It should not be modified. 
 */
public class HQMFDataCriteriaGenerator implements Generator {
	
	@Override
	public String generate(MeasureExport me) throws Exception {
		HQMFDataCriteriaElementGenerator hqmfDataCriteriaElementGenerator = new HQMFDataCriteriaElementGenerator();
		hqmfDataCriteriaElementGenerator.generate(me);

		HQMFClauseLogicGenerator hqmfClauseLogicGenerator = new HQMFClauseLogicGenerator();
		hqmfClauseLogicGenerator.generate(me);
		HQMFPopulationLogicGenerator hqmfPopulationLogicGenerator = new HQMFPopulationLogicGenerator();
		hqmfPopulationLogicGenerator.generate(me);
		HQMFMeasureObservationLogicGenerator hqmfMeasureObservationLogicGenerator = new HQMFMeasureObservationLogicGenerator();
		hqmfMeasureObservationLogicGenerator.generate(me);
		XmlProcessor dataCriteriaXMLProcessor = me.getHQMFXmlProcessor();
		return removePreambleAndRootTags(
				dataCriteriaXMLProcessor.transform(dataCriteriaXMLProcessor.getOriginalDoc(), true));
	}

	private String removePreambleAndRootTags(String xmlString) {
		xmlString = xmlString.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim().replaceAll("(<\\?[^<]*\\?>)?", "");
		xmlString = xmlString.replaceAll("<root>", "").replaceAll("</root>", "");
		return xmlString;
	}

	// Strip out 'Occurrence A_' at the start of qdmName If found.
	public static String removeOccurrenceFromName(String qdmName) {
		String regExpression = "Occurrence [A-Z]_.*";
		String newQdmName = qdmName;
		if (newQdmName.matches(regExpression)) {
			newQdmName = newQdmName.substring(newQdmName.indexOf('_') + 1);
		}
		return newQdmName;
	}
}
