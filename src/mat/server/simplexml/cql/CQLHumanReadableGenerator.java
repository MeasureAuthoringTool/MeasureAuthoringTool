package mat.server.simplexml.cql;

import mat.dao.clause.CQLLibraryDAO;
import mat.model.cql.CQLModel;
import mat.server.CQLUtilityClass;
import mat.server.util.XmlProcessor;

import org.w3c.dom.Node;

public class CQLHumanReadableGenerator {
	
	public static String generateHTMLForPopulation(String measureId,
			XmlProcessor subXMLProcessor, String measureXML, CQLLibraryDAO cqlLibraryDAO) {
		
		Node cqlNode = subXMLProcessor.getOriginalDoc().getDocumentElement().getFirstChild();
		
		String cqlNodeString = subXMLProcessor.transform(subXMLProcessor.getOriginalDoc().getDocumentElement().getFirstChild());
		System.out.println("cqlNodeString:"+cqlNodeString);
		
		CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(measureXML);

			
		String humanReadableHTML = "";
		humanReadableHTML = CQLHumanReadableHTMLCreator.generateCQLHumanReadableForSinglePopulation(cqlNode.getParentNode(), measureXML, cqlModel, cqlLibraryDAO);
		
		return humanReadableHTML; 
	}
	
	public static String generateHTMLForMeasure(String measureId,
			String simpleXmlStr, CQLLibraryDAO cqlLibraryDAO) {
		
		String humanReadableHTML = "";
		
		CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(simpleXmlStr);
	
		
		humanReadableHTML = CQLHumanReadableHTMLCreator.generateCQLHumanReadableForMeasure(simpleXmlStr, cqlModel, cqlLibraryDAO);
		
		return humanReadableHTML;
		
	}
	
}
