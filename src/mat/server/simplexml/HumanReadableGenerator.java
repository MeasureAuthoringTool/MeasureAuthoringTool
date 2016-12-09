package mat.server.simplexml;

import mat.server.simplexml.cql.CQLHumanReadableGenerator;
import mat.server.util.XmlProcessor;
import mat.shared.MATPropertiesUtil;

public class HumanReadableGenerator {
	
	
	public static String generateHTMLForPopulationOrSubtree(String measureId,
			String subXML, String measureXML) {
		
		XmlProcessor subXMLProcessor = new XmlProcessor(subXML);
		String html = "";
		
		if(subXMLProcessor.getOriginalDoc().getDocumentElement().hasChildNodes()){			
			String firstNodeName = subXMLProcessor.getOriginalDoc().getDocumentElement().getFirstChild().getNodeName();
			System.out.println("firstNodeName:"+firstNodeName);
			
			if("cqldefinition".equals(firstNodeName) || "cqlfunction".equals(firstNodeName) || "cqlaggfunction".equals(firstNodeName)){
				html = CQLHumanReadableGenerator.generateHTMLForPopulation(measureId, subXMLProcessor, measureXML);
			}else{
				html = HQMFHumanReadableGenerator.generateHTMLForPopulationOrSubtree(measureId,subXML,measureXML);
			}
		}else{
			return "<html></html>";
		}
		
		return html;
	}
	
	public static String generateHTMLForMeasure(String measureId,String simpleXmlStr, String measureReleaseVersion){
		
		String html = "";
		System.out.println("Generating human readable for ver:"+measureReleaseVersion);
		if(measureReleaseVersion.equals(MATPropertiesUtil.MAT_RELEASE_VERSION)){
			html = CQLHumanReadableGenerator.generateHTMLForMeasure(measureId, simpleXmlStr);
		}else{
			html = HQMFHumanReadableGenerator.generateHTMLForMeasure(measureId,simpleXmlStr);
		}
		
		return html;
	}
	
}
