package mat.server.simplexml.hqmf;

import javax.xml.xpath.XPathExpressionException;
import mat.model.clause.MeasureExport;
import mat.server.util.XmlProcessor;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class HQMFFinalCleanUp {
	
	/** The Constant logger. */
	private static final Log logger = LogFactory
			.getLog(HQMFFinalCleanUp.class);
	
	public static void clean(MeasureExport me) {
		
		XmlProcessor hqmfProcessor = me.getHQMFXmlProcessor();
		XmlProcessor simpleProcessor = me.getSimpleXMLProcessor();
		
		if(hqmfProcessor == null){
			logger.info("HQMF document is null. Aborting clean up.");
			return;
		}
		
		if(simpleProcessor == null){
			logger.info("SimpleXML document is null. Aborting clean up.");
			return;
		}
		
		cleanExtensions(me);
		cleanLocalVariableNames(me);
	}
	
	private static void cleanExtensions(MeasureExport me) {
		
		String xPathForExtensions = "//*/@extension";
		try {
			NodeList extensionsList = me.getHQMFXmlProcessor().findNodeList(me.getHQMFXmlProcessor().getOriginalDoc(), xPathForExtensions);
			for(int i=0;i<extensionsList.getLength();i++){
				Node extNode = extensionsList.item(i);
				String extValue = extNode.getNodeValue();
				
				extValue = getReplaceString(extValue);
				extNode.setNodeValue(extValue);
			}
			
		} catch (XPathExpressionException e) {
			logger.error("Exception in HQMFFinalCleanUp.cleanExtensions:"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	private static void cleanLocalVariableNames(MeasureExport me) {
		
		String xPathForLocalVars = "//localVariableName/@value";
		String xPathForMeasureObValueExpression = "//measureObservationDefinition//value/expression";
		String xPathForMeasureObPreconditionVal = "//measureObservationDefinition//precondition//value";
		
		try {
			NodeList localVarValuesList = me.getHQMFXmlProcessor().findNodeList(me.getHQMFXmlProcessor().getOriginalDoc(), xPathForLocalVars);
			NodeList measureObValueExpList = me.getHQMFXmlProcessor().findNodeList(me.getHQMFXmlProcessor().getOriginalDoc(), xPathForMeasureObValueExpression);
			NodeList measureObPreConditionValList = me.getHQMFXmlProcessor().findNodeList(me.getHQMFXmlProcessor().getOriginalDoc(), xPathForMeasureObPreconditionVal);
			for(int i=0;i<localVarValuesList.getLength();i++){
				Node extNode = localVarValuesList.item(i);
				String extValue = extNode.getNodeValue();
				extValue = getReplaceString(extValue);
				extNode.setNodeValue(extValue);
			}
			
			for(int i=0;i<measureObValueExpList.getLength();i++){
				Node expressionNode = measureObValueExpList.item(i);
				String value = expressionNode.getAttributes().getNamedItem("value").getNodeValue();
				value = getReplaceString(value);
				expressionNode.getAttributes().getNamedItem("value").setNodeValue(value);
			}
			
			for(int i=0;i<measureObPreConditionValList.getLength();i++){
				Node valueNode = measureObPreConditionValList.item(i);
				String value = valueNode.getAttributes().getNamedItem("value").getNodeValue();
				value = getReplaceString(value);
				valueNode.getAttributes().getNamedItem("value").setNodeValue(value);
			}
			
		} catch (XPathExpressionException e) {
			logger.error("Exception in HQMFFinalCleanUp.cleanExtensions:"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	private static String getReplaceString(String extValue) {
		if(extValue.indexOf(">=") > -1){
			extValue = StringUtils.replace(extValue, ">=", "grtr_thn_eql_");
			
		}
		if(extValue.indexOf(">") > -1){
			extValue = StringUtils.replace(extValue, ">", "grtr_thn_");
			
		}
		if(extValue.indexOf("<=") > -1){
			extValue = StringUtils.replace(extValue, "<=", "less_thn_eql_");
		}
		if(extValue.indexOf("<") > -1){
			extValue = StringUtils.replace(extValue, "<", "less_thn_");
		}
		if(extValue.indexOf("=") > -1){
			extValue = StringUtils.replace(extValue, "=", "eql_");
		}
		if(extValue.contains(",")) {
			extValue = StringUtils.remove(extValue, ',');
		}
		return extValue;
	}
	
}
