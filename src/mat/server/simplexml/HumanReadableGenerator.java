package mat.server.simplexml;

import mat.client.shared.MatContext;
import mat.dao.clause.CQLLibraryDAO;
import mat.server.simplexml.cql.CQLHumanReadableGenerator;
import mat.server.util.XmlProcessor;

public class HumanReadableGenerator {
	
	
	public static String generateHTMLForPopulationOrSubtree(String measureId,
			String subXML, String measureXML, CQLLibraryDAO cqlLibraryDAO) {
		
		XmlProcessor subXMLProcessor = new XmlProcessor(subXML);
		String html = "";
		
		if(subXMLProcessor.getOriginalDoc().getDocumentElement().hasChildNodes()){			
			String firstNodeName = subXMLProcessor.getOriginalDoc().getDocumentElement().getFirstChild().getNodeName();
			System.out.println("firstNodeName:"+firstNodeName);
			
			if("cqldefinition".equals(firstNodeName) || "cqlfunction".equals(firstNodeName) || "cqlaggfunction".equals(firstNodeName)){
				html = CQLHumanReadableGenerator.generateHTMLForPopulation(measureId, subXMLProcessor, measureXML, cqlLibraryDAO);
			}else{
				html = HQMFHumanReadableGenerator.generateHTMLForPopulationOrSubtree(measureId,subXML,measureXML);
			}
		}else{
			return "<html></html>";
		}
		
		return html;
	}
	
	public static String generateHTMLForMeasure(String measureId,String simpleXmlStr, String measureReleaseVersion, CQLLibraryDAO cqlLibraryDAO){
		
		String html = "";
		System.out.println("Generating human readable for ver:"+measureReleaseVersion);
		if(MatContext.get().isCQLMeasure(measureReleaseVersion)){
			html = CQLHumanReadableGenerator.generateHTMLForMeasure(measureId, simpleXmlStr, cqlLibraryDAO);
		}else{
			html = HQMFHumanReadableGenerator.generateHTMLForMeasure(measureId,simpleXmlStr);
		}
		
		return html;
	}
	
}
