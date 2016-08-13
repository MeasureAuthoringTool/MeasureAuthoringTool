package mat.server.simplexml.hqmf;

import javax.xml.xpath.XPathExpressionException;

import mat.model.clause.MeasureExport;
import mat.server.service.impl.XMLUtility;
import mat.server.util.XmlProcessor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The Class HQMFFinalCleanUp.
 */
public class HQMFFinalCleanUp {
	
	/** The Constant logger. */
	private static final Log logger = LogFactory
			.getLog(HQMFFinalCleanUp.class);
	
	/** The Constant reverseEntryCheckFile. */
	private static final String reverseEntryCheckFile = "xsl/final_hqmf_entry_deletion_check.xsl";
	
	/** The Constant deleteUnUsedEntryFile. */
	private static final String deleteUnUsedEntryFile = "xsl/hqmf_delete_Unused_entry.xsl";
	
	/**
	 * Clean.
	 *
	 * @param me the me
	 */
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
		//deleteUnusedEntry(me);
		
	}
	
	public static void cleanAndDeleteUnused(MeasureExport me) {
		clean(me);
		deleteUnusedEntry(me);		
	}
	
	
	/**
	 * Clean extensions.
	 *
	 * @param me the me
	 */
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
	
	/**
	 * Clean local variable names.
	 *
	 * @param me the me
	 */
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
				String [] preConditionExpArray = value.split("==");
				String preCondExpValue = null;
				for (String valueToEval : preConditionExpArray) {
					if (preCondExpValue == null) {
						preCondExpValue = getReplaceString(valueToEval);
					} else {
						preCondExpValue = preCondExpValue + "==" + getReplaceString(valueToEval);
					}
				}
				value = getReplaceString(value);
				valueNode.getAttributes().getNamedItem("value").setNodeValue(preCondExpValue);
			}
			
		} catch (XPathExpressionException e) {
			logger.error("Exception in HQMFFinalCleanUp.cleanExtensions:"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Reverse entry check.
	 *
	 * @param me the me
	 */
	private static void reverseEntryCheck(String hqmfXML) {
		XMLUtility xmlUtility = new XMLUtility();
		String reverseEntryCheckResults = xmlUtility.applyXSL(hqmfXML,	xmlUtility.getXMLResource(reverseEntryCheckFile));
		logger.info("Reverse Entry Check results: " + reverseEntryCheckResults);
	}
	
	/**
	 * Gets the replace string.
	 *
	 * @param extValue the ext value
	 * @return the replace string
	 */
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
		if(extValue.contains("'")) {
			extValue = StringUtils.remove(extValue, "'");
		}
		return extValue;
	}
	
	
	/**
	 * Delete unused entry.
	 * This method is to check and delete Unused QDM Entries
	 * in HQMF
	 *
	 * @param me the me
	 */
	private static void deleteUnusedEntry(MeasureExport me) {
		XMLUtility xmlUtility = new XMLUtility();
		String delDupEntryResults = xmlUtility.applyXSL(me.getHQMFXmlProcessor().transform(me.getHQMFXmlProcessor().getOriginalDoc()),
				xmlUtility.getXMLResource(deleteUnUsedEntryFile));
		
		me.setHQMFXmlProcessor(new XmlProcessor(delDupEntryResults));
		
		reverseEntryCheck(delDupEntryResults);
	}

}
