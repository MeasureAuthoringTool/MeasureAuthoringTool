package mat.server.simplexml.cql;

import java.io.StringReader;

import javax.xml.xpath.XPathExpressionException;

import mat.model.cql.CQLDataModel;
import mat.model.cql.CQLDefinitionsWrapper;
import mat.model.cql.CQLFunctionsWrapper;
import mat.model.cql.CQLLibraryModel;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParametersWrapper;
import mat.model.cql.parser.CQLFileObject;
import mat.server.CQLUtilityClass;
import mat.server.cqlparser.MATCQLParser;
import mat.server.util.ResourceLoader;
import mat.server.util.XmlProcessor;

import org.apache.commons.lang.StringUtils;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class CQLHumanReadableGenerator {
	
	private static final String[] keyWordListArray = {"library","version","using","include","called","public","private",
														"parameter","default","codesystem","valueset","codesystems","define",
														"function","with","without","in","from","where","return",
														"all","distinct","sort","by","asc","desc","is","not","cast","as","between",
														"difference","contains","and","or","xor","union","intersection","year","month",
														"day","hour","minute","second","millisecond","when","then","or","or less", 
														"before","after","or more","more","less","context","using", "QDM","Interval",
														"DateTime","Patient","Population","such that"};
	
	private static final String[] cqlFunctionsListArray = {"date","time","timezone","starts","ends",
															"occurs","overlaps","Interval",
															"Tuple","List","DateTime","AgeInYearsAt"};
	
	
	public static String generateHTMLForPopulation(String measureId,
			XmlProcessor subXMLProcessor, String measureXML) {
		
		Node cqlNode = subXMLProcessor.getOriginalDoc().getDocumentElement().getFirstChild();
		
		String cqlNodeString = subXMLProcessor.transform(subXMLProcessor.getOriginalDoc().getDocumentElement().getFirstChild());
		System.out.println("cqlNodeString:"+cqlNodeString);
		
		String cqlFileString = getCQLStringFromMeasureXML(measureXML);
		MATCQLParser matcqlParser = new MATCQLParser();
		CQLFileObject cqlFileObject = matcqlParser.parseCQL(cqlFileString);
			
		String humanReadableHTML = "";
		humanReadableHTML = CQLHumanReadableHTMLCreator.generateCQLHumanReadableForSinglePopulation(cqlNode.getParentNode(), cqlFileObject);
		
		return humanReadableHTML; 
	}
	
	private static String getCQLStringFromMeasureXML(String measureXML){
		
		CQLModel cqlModel = new CQLModel();
		XmlProcessor measureXMLProcessor = new XmlProcessor(measureXML);
		String cqlLookUpXMLString = measureXMLProcessor.getXmlByTagName("cqlLookUp");
		
		if(StringUtils.isNotBlank(cqlLookUpXMLString)){
			getCQLGeneralInfo(cqlModel, measureXMLProcessor);
			getCQLDefinitionsInfo(cqlModel, cqlLookUpXMLString);
			getCQLParametersInfo(cqlModel,cqlLookUpXMLString);
			getCQLFunctionsInfo(cqlModel, cqlLookUpXMLString);
		}
		
		return CQLUtilityClass.getCqlString(cqlModel).toString();
	}
	
	private static void getCQLGeneralInfo(CQLModel cqlModel, XmlProcessor measureXMLProcessor) {
		
		String libraryNameStr = "";
		String usingModelStr = "";
		CQLLibraryModel libraryModel = new CQLLibraryModel();
		CQLDataModel usingModel = new CQLDataModel();
		
		
		if (measureXMLProcessor != null) {
					
			String XPATH_EXPRESSION_CQLLOOKUP_lIBRARY = "/measure/cqlLookUp/library/text()";
			String XPATH_EXPRESSION_CQLLOOKUP_USING = "/measure/cqlLookUp/usingModel/text()";
			
			try {
				
				Node nodeCQLLibrary = measureXMLProcessor.findNode(
						measureXMLProcessor.getOriginalDoc(),
						XPATH_EXPRESSION_CQLLOOKUP_lIBRARY);
				Node nodeCQLUsingModel = measureXMLProcessor.findNode(
						measureXMLProcessor.getOriginalDoc(),
						XPATH_EXPRESSION_CQLLOOKUP_USING);
				
				if (nodeCQLLibrary != null) {
					libraryNameStr = nodeCQLLibrary.getTextContent();
					libraryModel.setLibraryName(libraryNameStr);
				}
				
				if (nodeCQLUsingModel != null) {
					usingModelStr = nodeCQLUsingModel.getTextContent();
					usingModel.setName(usingModelStr);
				}
				
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			
		}
		
		cqlModel.setLibrary(libraryModel);
		cqlModel.setUsedModel(usingModel);
		
	}
	
	private static void getCQLDefinitionsInfo(CQLModel cqlModel, String cqlLookUpXMLString) {
		CQLDefinitionsWrapper details = null;
		
		try {			 
			
			Mapping mapping = new Mapping();
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("CQLDefinitionModelMapping.xml"));
			Unmarshaller unmarshaller = new Unmarshaller(mapping);
			unmarshaller.setClass(CQLDefinitionsWrapper.class);
			unmarshaller.setWhitespacePreserve(true);
			
			details = (CQLDefinitionsWrapper) unmarshaller.unmarshal(new InputSource(new StringReader(cqlLookUpXMLString)));
			cqlModel.setDefinitionList(details.getCqlDefinitions());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private static void getCQLParametersInfo(CQLModel cqlModel, String cqlLookUpXMLString) {
		
		CQLParametersWrapper details = null;
		try {
				Mapping mapping = new Mapping();
				mapping.loadMapping(new ResourceLoader().getResourceAsURL("CQLParameterModelMapping.xml"));
				Unmarshaller unmarshaller = new Unmarshaller(mapping);
				unmarshaller.setClass(CQLParametersWrapper.class);
				unmarshaller.setWhitespacePreserve(true);
			
				details = (CQLParametersWrapper) unmarshaller.unmarshal(new InputSource(new StringReader(cqlLookUpXMLString)));
				cqlModel.setCqlParameters(details.getCqlParameterList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private static void getCQLFunctionsInfo(CQLModel cqlModel, String cqlLookUpXMLString) {
		
		CQLFunctionsWrapper details = null;
		try {
			Mapping mapping = new Mapping();
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("CQLFunctionModelMapping.xml"));
			Unmarshaller unmarshaller = new Unmarshaller(mapping);
			unmarshaller.setClass(CQLFunctionsWrapper.class);
			unmarshaller.setWhitespacePreserve(true);
			details = (CQLFunctionsWrapper) unmarshaller.unmarshal(new InputSource(new StringReader(cqlLookUpXMLString)));
			cqlModel.setCqlFunctions(details.getCqlFunctionsList());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
