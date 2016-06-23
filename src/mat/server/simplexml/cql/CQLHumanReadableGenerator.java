package mat.server.simplexml.cql;

import mat.model.cql.parser.CQLFileObject;
import mat.server.CQLUtilityClass;
import mat.server.cqlparser.MATCQLParser;
import mat.server.util.XmlProcessor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.cqframework.cql.cql2elm.CQLtoELM;
import org.cqframework.cql.cql2elm.CqlTranslator;
import org.w3c.dom.Node;

public class CQLHumanReadableGenerator {
	
	public static String generateHTMLForPopulation(String measureId,
			XmlProcessor subXMLProcessor, String measureXML) {
		
		Node cqlNode = subXMLProcessor.getOriginalDoc().getDocumentElement().getFirstChild();
		
		String cqlNodeString = subXMLProcessor.transform(subXMLProcessor.getOriginalDoc().getDocumentElement().getFirstChild());
		System.out.println("cqlNodeString:"+cqlNodeString);
		
		String cqlFileString = CQLUtilityClass.getCqlString(CQLUtilityClass.getCQLStringFromMeasureXML(measureXML,measureId),"").toString();
		try{
		parseCQL(cqlFileString);
		}catch(Throwable t){
			System.out.println("CQL to ELM failed..exception.");
			t.printStackTrace();
		}
		
		MATCQLParser matcqlParser = new MATCQLParser();
		CQLFileObject cqlFileObject = matcqlParser.parseCQL(cqlFileString);
			
		String humanReadableHTML = "";
		humanReadableHTML = CQLHumanReadableHTMLCreator.generateCQLHumanReadableForSinglePopulation(cqlNode.getParentNode(), cqlFileObject);
		
		return humanReadableHTML; 
	}
	
	private static void parseCQL(String cqlString) {
		System.out.println("Parse CQL:"+cqlString);
		
		//			CQLtoELM.doTranslation(cqlString, "C:\\Users\\jmeyer\\workspace\\mat\\war\\test.xml", null, "XML", false, false, true); 
		CqlTranslator.getErrors(); 
	
	}
	
}
