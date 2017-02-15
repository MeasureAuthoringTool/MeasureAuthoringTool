package mat.server.simplexml.cql;

import mat.model.cql.parser.CQLFileObject;
import mat.server.CQLUtilityClass;
import mat.server.cqlparser.MATCQLParser;
import mat.server.util.XmlProcessor;

import org.w3c.dom.Node;

public class CQLHumanReadableGenerator {
	
	public static String generateHTMLForPopulation(String measureId,
			XmlProcessor subXMLProcessor, String measureXML) {
		
		Node cqlNode = subXMLProcessor.getOriginalDoc().getDocumentElement().getFirstChild();
		
		String cqlNodeString = subXMLProcessor.transform(subXMLProcessor.getOriginalDoc().getDocumentElement().getFirstChild());
		System.out.println("cqlNodeString:"+cqlNodeString);
		
		String cqlFileString = CQLUtilityClass.getCqlString(CQLUtilityClass.getCQLStringFromXML(measureXML),"").toString();
				
		MATCQLParser matcqlParser = new MATCQLParser();
		CQLFileObject cqlFileObject = matcqlParser.parseCQL(cqlFileString);
			
		String humanReadableHTML = "";
		humanReadableHTML = CQLHumanReadableHTMLCreator.generateCQLHumanReadableForSinglePopulation(cqlNode.getParentNode(), cqlFileObject);
		
		return humanReadableHTML; 
	}
	
	public static String generateHTMLForMeasure(String measureId,
			String simpleXmlStr) {
		
		String humanReadableHTML = "";
		
		String cqlFileString = CQLUtilityClass.getCqlString(CQLUtilityClass.getCQLStringFromXML(simpleXmlStr),"").toString();
		MATCQLParser matcqlParser = new MATCQLParser();
		CQLFileObject cqlFileObject = matcqlParser.parseCQL(cqlFileString);
			
		
		humanReadableHTML = CQLHumanReadableHTMLCreator.generateCQLHumanReadableForMeasure(simpleXmlStr, cqlFileObject);
		
		return humanReadableHTML;
		
	}
	
}
