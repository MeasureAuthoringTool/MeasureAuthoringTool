package mat.server;

import mat.model.cql.CQLKeywords;
import mat.server.cqlparser.CQLTemplateXML;
import mat.server.util.XmlProcessor;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;

public class CQLKeywordsUtil {
	
	public static CQLKeywords getCQLKeywords() {
		CQLKeywords cqlKeywords = new CQLKeywords();
		XmlProcessor cqlXMLProcessor = CQLTemplateXML.getCQLTemplateXmlProcessor();
		String XPATH_DATATYPES = "/cqlTemplate/datatypes/datatype";
		String XPATH_TIMINGS = "/cqlTemplate/timings/timing";
		String XPATH_FUNCTIONS = "/cqlTemplate/functions/function";
		String XPATH_KEYWORDS = "/cqlTemplate/keywords/keyword";
		List<String> cqlDataTypeList = new ArrayList<String>();
		List<String> cqlTimingList = new ArrayList<String>();
		List<String> cqlFunctionList = new ArrayList<String>();
		List<String> cqlKeywordsList = new ArrayList<String>();
		try {
			NodeList dataTypeNodeList = cqlXMLProcessor.findNodeList(cqlXMLProcessor.getOriginalDoc(), XPATH_DATATYPES);
			NodeList timingNodeList = cqlXMLProcessor.findNodeList(cqlXMLProcessor.getOriginalDoc(), XPATH_TIMINGS);
			NodeList functionNodeList = cqlXMLProcessor.findNodeList(cqlXMLProcessor.getOriginalDoc(), XPATH_FUNCTIONS);
			NodeList keywordNodeList = cqlXMLProcessor.findNodeList(cqlXMLProcessor.getOriginalDoc(), XPATH_KEYWORDS);
			if (dataTypeNodeList != null) {
				for (int i = 0; i < dataTypeNodeList.getLength(); i++) {
					Node node = dataTypeNodeList.item(i);
					cqlDataTypeList.add(node.getTextContent());
				}
			}
			if (timingNodeList != null) {
				for (int i = 0; i < timingNodeList.getLength(); i++) {
					Node node = timingNodeList.item(i);
					cqlTimingList.add(node.getTextContent());
				}
			}
			if (functionNodeList != null) {
				for (int i = 0; i < functionNodeList.getLength(); i++) {
					Node node = functionNodeList.item(i);
					cqlFunctionList.add(node.getTextContent());
				}
			}
			if (keywordNodeList != null) {
				for (int i = 0; i < keywordNodeList.getLength(); i++) {
					Node node = keywordNodeList.item(i);
					cqlKeywordsList.add(node.getTextContent());
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		cqlKeywords.setCqlDataTypeList(cqlDataTypeList);
		cqlKeywords.setCqlTimingList(cqlTimingList);
		cqlKeywords.setCqlFunctionsList(cqlFunctionList);
		cqlKeywords.setCqlKeywordsList(cqlKeywordsList);
		return cqlKeywords;
	}

}
