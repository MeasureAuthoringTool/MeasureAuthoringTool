package mat.server.simplexml;

import mat.server.util.XmlProcessor;

public class HumanReadableGenerator {
	
	
	public static String generateHTMLForPopulationOrSubtree(String measureId,
			String subXML, String measureXML) {
		
		XmlProcessor subXMLProcessor = new XmlProcessor(subXML);
		String firstNodeName = subXMLProcessor.getOriginalDoc().getDocumentElement().getFirstChild().getNodeName();
		System.out.println("firstNodeName:"+firstNodeName);
		String html = "";
		if("cqldefinition".equals(firstNodeName) || "cqlfunction".equals(firstNodeName) || "cqlaggfunction".equals(firstNodeName)){
			html = CQLHumanReadableGenerator.generateHTMLForPopulation(measureId, subXMLProcessor, measureXML);
		}else{
			html = HQMFHumanReadableGenerator.generateHTMLForPopulationOrSubtree(measureId,subXML,measureXML);
		}
		return html;
	}
	
}
