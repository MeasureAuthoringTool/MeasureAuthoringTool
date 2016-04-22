package mat.server.simplexml;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import mat.model.cql.CQLDataModel;
import mat.model.cql.CQLDefinitionsWrapper;
import mat.model.cql.CQLFunctionsWrapper;
import mat.model.cql.CQLLibraryModel;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParametersWrapper;
import mat.model.cql.parser.CQLDefinitionModelObject;
import mat.model.cql.parser.CQLFileObject;
import mat.server.CQLUtilityClass;
import mat.server.cqlparser.MATCQLParser;
import mat.server.util.ResourceLoader;
import mat.server.util.XmlProcessor;

import org.apache.commons.lang.StringUtils;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class CQLHumanReadableGenerator {
	
	private static List<String> definitionsAlreadyDisplayed = new ArrayList<String>();
	
	public static String generateHTMLForPopulation(String measureId,
			XmlProcessor subXMLProcessor, String measureXML) {
		
		Node cqlNode = subXMLProcessor.getOriginalDoc().getDocumentElement().getFirstChild();
		
		String cqlNodeString = subXMLProcessor.transform(subXMLProcessor.getOriginalDoc().getDocumentElement().getFirstChild());
		System.out.println("cqlNodeString:"+cqlNodeString);
		
		String cqlFileString = getCQLStringFromMeasureXML(measureXML);
		MATCQLParser matcqlParser = new MATCQLParser();
		CQLFileObject cqlFileObject = matcqlParser.parseCQL(cqlFileString);
		
		String humanReadableHTML = "";
		humanReadableHTML = generateCQLHumanReadableForSinglePopulation(cqlNode.getParentNode(), cqlFileObject);
		
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
	
public static String generateCQLHumanReadableForSinglePopulation(Node populationNode, CQLFileObject cqlFileObject){
		
		definitionsAlreadyDisplayed.clear();
		
		Node cqlNode = populationNode.getFirstChild();
		String populationName = populationNode.getAttributes().getNamedItem("displayName").getNodeValue();
							
		org.jsoup.nodes.Document htmlDocument = null;
		htmlDocument = createBaseHTMLDocument("Human Readable for "+populationName);
		
		Element bodyElement = htmlDocument.body();
		
		generatePopulationCriteria(bodyElement, cqlFileObject, cqlNode, populationName);
		
		return htmlDocument.html();
	}
	
	private static void generatePopulationCriteria(Element bodyElement, CQLFileObject cqlFileObject, Node cqlNode, String populationName) {
		
		/*bodyElement.append("<p id=\"nn-text\">Non-normative Example - Only CQL Logic.</p>");
		bodyElement.append("<div><span id=\"d1e521\" class=\"section\">Population Criteria</span>\r\n" + 
				"      <a href=\"#toc\" style=\"margin-top:-10px;\">Table of Contents</a></div>");*/
		
		Element mainDivElement = bodyElement.appendElement("div");
		mainDivElement.attr("class", "treeview hover p-l-10");
		
		Element mainULElement = mainDivElement.appendElement("ul");
		mainULElement.attr("class", "list-unstyled");
		
		String cqlNodeType = cqlNode.getNodeName();
		String cqlName = cqlNode.getAttributes().getNamedItem("displayName").getNodeValue();
		System.out.println("Generating Human readable for:"+cqlNodeType +":"+ cqlName);
			
		if("cqldefinition".equals(cqlNodeType)){
			generateHTMLForPopulation(mainULElement, cqlFileObject, populationName, cqlName);
		}
	
	}

	
	private static void generateHTMLForPopulation(
			Element mainElement, CQLFileObject cqlFileObject, String populationName, String mainDefinitionName) {
		
		//create a base LI element
		Element mainliElement = mainElement.appendElement("li");
		mainliElement.attr("class", "list-unstyled");
		
		Element checkBoxElement = mainliElement.appendElement("input");
		checkBoxElement.attr("type", "checkbox");
		String id = "test-"+populationName+"_"+(int)(Math.random() * 1000);
		checkBoxElement.attr("id", id);
		
		if(definitionsAlreadyDisplayed.contains(populationName)){
			checkBoxElement.attr("checked", "");
		}else{
			definitionsAlreadyDisplayed.add(populationName);
		}
		
		Element definitionLabelElement = mainliElement.appendElement("label");
		definitionLabelElement.attr("for", id);
		definitionLabelElement.attr("class", "list-header");
		
		Element strongElement = definitionLabelElement.appendElement("strong");
		strongElement.appendText(populationName);
		
		definitionLabelElement.appendText(" (click to expand/collapse)");
		System.out.println(mainDefinitionName);
		generateHTMLForDefinition(cqlFileObject.getDefinitionsMap().get(mainDefinitionName), mainliElement, true);
	}
	
	private static void generateHTMLForDefinition(
			CQLDefinitionModelObject cqlDefinitionModelObject,
			Element mainElement, boolean isTopDefinition) {
		
		String definitionIdentifier = cqlDefinitionModelObject.getIdentifier();
		
		Element mainULElement = mainElement;
		if(isTopDefinition){
			mainULElement = mainElement.appendElement("ul");
			mainULElement.attr("class", "code");
		}
		
		Element mainliElement = mainULElement;
		//create a base LI element
		if(isTopDefinition){
			mainliElement = mainULElement.appendElement("li");
		}
		
		Element mainDivElement = mainliElement.appendElement("div");
		mainDivElement.attr("class", "treeview hover p-l-10");
		
		Element checkBoxElement = mainDivElement.appendElement("input");
		checkBoxElement.attr("type", "checkbox");
		String id = "test-"+definitionIdentifier+"_"+(int)(Math.random() * 1000);
		checkBoxElement.attr("id", id);
		
		if(definitionsAlreadyDisplayed.contains(definitionIdentifier)){
			checkBoxElement.attr("checked", "");
		}else{
			definitionsAlreadyDisplayed.add(definitionIdentifier);
		}
		
		Element definitionLabelElement = mainDivElement.appendElement("label");
		definitionLabelElement.attr("for", id);
		definitionLabelElement.attr("class", "list-header");
		
		Element strongElement = definitionLabelElement.appendElement("strong");
		strongElement.appendText(definitionIdentifier);
		
		definitionLabelElement.appendText(" (click to expand/collapse)");
		
		Element subULElement = mainDivElement.appendElement("ul");
		Element subLiElement = subULElement.appendElement("li");
		Element subDivElement = subLiElement.appendElement("div");
		
		Element spanElem = getSpanElementWithClass(subDivElement, "cql-keyword-new");
		spanElem.appendText("define ");
		
		Element spanElemDefName = getSpanElementWithClass(subDivElement, "cql-class");
		spanElemDefName.appendText(definitionIdentifier+":");
		
		List<String> definitionLineList = getDefinitionLineList(cqlDefinitionModelObject);
		subDivElement.appendText(" " + definitionLineList.get(0));
		
		subDivElement.appendElement("br");
		
		for(int i=1;i<definitionLineList.size();i++){
			Element spanElemDefBody = getSpanElementWithClass(subDivElement, "cql-definition-body");
			spanElemDefBody.appendText(definitionLineList.get(i));
		}
		subDivElement.appendElement("br");
		
		List<CQLDefinitionModelObject> referredToDefinitionsModelObjectList = cqlDefinitionModelObject.getReferredToDefinitions();
		for(int j=0;j<referredToDefinitionsModelObjectList.size();j++){
			CQLDefinitionModelObject referredTDefinitionModelObject = referredToDefinitionsModelObjectList.get(j);
			generateHTMLForDefinition(referredTDefinitionModelObject, subDivElement, false);
		}
	}

	/**
	 * This method will go through definition body and try to format it 
	 * in series of lines for easier reading.
	 * @param cqlDefinitionModelObject
	 * @return
	 */
	private static List<String> getDefinitionLineList(
			CQLDefinitionModelObject cqlDefinitionModelObject) {
		
		List<String> definitionLineList = new ArrayList<String>();
		int tokenCounter = 0;
		
		List<String> childTokens = cqlDefinitionModelObject.getChildTokens();
		
		//find the first line
		if(childTokens.get(0).trim().equals("["))//Try to check for something like "["Encounter, Performed": "Ambulatory/ED Visit"] E"
		{
			String tokenString = "[";
			//look further until you find ']'
			for(tokenCounter=1;tokenCounter<childTokens.size();tokenCounter++){
				if(childTokens.get(tokenCounter).equals("]")){
					if(childTokens.size() > (tokenCounter + 1)){
					String nextToken = childTokens.get(tokenCounter + 1).trim();
					if(nextToken.length() == 1 && Character.isLetter(nextToken.charAt(0))){
						definitionLineList.add(tokenString+"] "+ nextToken);
						tokenCounter += 2;
					}else{ //check for something like " ["Diagnosis, Active": "Acute Pharyngitis"] union ["Diagnosis, Active": "Acute Tonsillitis"]"
						definitionLineList.add(" ");
						tokenCounter = 0;
					}
					}else{//check for something like " ["Diagnosis, Active": "Acute Pharyngitis"] union ["Diagnosis, Active": "Acute Tonsillitis"]"
						definitionLineList.add(" ");
						definitionLineList.add(tokenString+"] ");
						tokenCounter += 1; 
					}
					break;
				}
				else
				{
					tokenString += " " + childTokens.get(tokenCounter);
				}
			}
		}
		else if(childTokens.size() > 1 && 
				childTokens.get(1).trim().length() == 1 
				&& 
				Character.isLetter(childTokens.get(1).trim().charAt(0)))//check for something like "MeasurementPeriodEncounters E"
		{
			tokenCounter = 2;
			definitionLineList.add(childTokens.get(0) + " " + childTokens.get(1));
		}
		
		String tokenString = "";
		List<String> breakAtKeywords = new ArrayList<String>();
		breakAtKeywords.add("where");
		breakAtKeywords.add("with");
		breakAtKeywords.add("and");
		
		for(;tokenCounter < childTokens.size();tokenCounter++){
			if(breakAtKeywords.contains(childTokens.get(tokenCounter).trim().toLowerCase()) && tokenString.length() > 0){
				definitionLineList.add(tokenString);
				tokenString = "";
			}else {
				String fillerSpace = " ";
				
				List<String> noSpaceTokens = new ArrayList();
				noSpaceTokens.add(".");
				noSpaceTokens.add("(");
				//noSpaceTokens.add(")");
				//noSpaceTokens.add("[");
				//noSpaceTokens.add("]");
				String token = childTokens.get(tokenCounter);
				String lastChar = tokenString.length() > 0 ? tokenString.charAt(tokenString.length()-1)+"" : "";
				
				if(noSpaceTokens.contains(token) || noSpaceTokens.contains(lastChar)){
					fillerSpace = "";
				}
				tokenString += fillerSpace + childTokens.get(tokenCounter);
			}
		}
		
		if(tokenString.length() > 0){
			definitionLineList.add(tokenString);
		}
		
		return definitionLineList;
	}

	private static Element getSpanElementWithClass(Element subLiElement, String cssClassName) {
		Element spanElem = subLiElement.appendElement("span");
		spanElem.attr("class", cssClassName);
		return spanElem;
	}
	

	private static org.jsoup.nodes.Document createBaseHTMLDocument(String title) {
		org.jsoup.nodes.Document htmlDocument = new org.jsoup.nodes.Document("");
		
		// Must be added first for proper formating and styling
		DocumentType doc = new
				DocumentType("html","-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd","");
		htmlDocument.appendChild(doc);
		
		Element html = htmlDocument.appendElement("html");
		// POC - Added language attribute in html tag as asked by Matt.
		html.attributes().put(new Attribute("lang", "en"));
		html.appendElement("head");
		html.appendElement("body");
		
		Element head = htmlDocument.head();
		htmlDocument.title(title);
		appendStyleNode(head);
		return htmlDocument;
	}
	
	private static void appendStyleNode(Element head) {
		String styleTagString = MATCssCQLUtil.getCSS();
		head.append(styleTagString);
	}
	
}
