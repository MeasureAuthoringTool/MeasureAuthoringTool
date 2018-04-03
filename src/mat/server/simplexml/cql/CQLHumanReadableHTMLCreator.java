package mat.server.simplexml.cql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.xpath.XPathExpressionException;

import mat.dao.clause.CQLLibraryDAO;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLModel;
import mat.server.simplexml.HeaderHumanReadableGenerator;
import mat.server.util.CQLUtil;
import mat.server.util.CQLUtil.CQLArtifactHolder;
import mat.server.util.XmlProcessor;
import mat.shared.LibHolderObject;
import mat.shared.SaveUpdateCQLResult;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


// TODO: Auto-generated Javadoc
/**
 * The Class CQLHumanReadableHTMLCreator.
 */
public class CQLHumanReadableHTMLCreator {
	
	
	private static final String CQLAGGFUNCTION = "cqlaggfunction";

	private static final String CQLFUNCTION = "cqlfunction";

	private static final String CQLDEFINITION = "cqldefinition";

	/** The definitions or functions already displayed. */
	private static List<String> definitionsOrFunctionsAlreadyDisplayed = new ArrayList<String>();
	
	/** The cql objects. */
	private static List<String> cqlObjects = new ArrayList<String>();
	
	private static Map<String, XmlProcessor> includedXMLMap = new HashMap<String, XmlProcessor>();
		
	/** The Constant HTML_LI. */
	private static final String HTML_LI = "li";
	
	/** The Constant HTML_UL. */
	private static final String HTML_UL = "ul";
		
	/** The initial population hash. */
	private static Map<String, String> initialPopulationHash = new HashMap<String, String>();
	
	/** The Constant popNameArray. */
	private static final String[] POPULATION_NAME_ARRAY = {"initialPopulation",
		"denominator", "denominatorExclusions", "numerator",
		"numeratorExclusions", "denominatorExceptions",
		"measurePopulation", "measurePopulationExclusions",
		"measureObservation", "stratum" };
	
	/**
	 * Generate cql human readable for measure.
	 *
	 * @param simpleXmlStr the simple xml str
	 * @param cqlModel the cql file object
	 * @param cqlLibraryDAO 
	 * @return the string
	 */
	public static String generateCQLHumanReadableForMeasure(
			String simpleXmlStr, CQLModel cqlModel, CQLLibraryDAO cqlLibraryDAO) {
		
		definitionsOrFunctionsAlreadyDisplayed.clear();
		cqlObjects.clear();
		includedXMLMap.clear();
		
		XmlProcessor simpleXMLProcessor = new XmlProcessor(simpleXmlStr);
		
		CQLArtifactHolder usedCQLArtifactHolder = CQLUtil.getCQLArtifactsReferredByPoplns(simpleXMLProcessor.getOriginalDoc());
		
		cqlObjects = new ArrayList<String>();
		cqlObjects.addAll(usedCQLArtifactHolder.getCqlDefFromPopSet());
		cqlObjects.addAll(usedCQLArtifactHolder.getCqlFuncFromPopSet());
				
		SaveUpdateCQLResult cqlResult = CQLUtil.parseCQLLibraryForErrors(cqlModel, cqlLibraryDAO, cqlObjects);
		includedXMLMap = loadIncludedLibXMLProcessors(cqlModel);
		
		String humanReadableHTML = "";
		
		try {
			
			org.jsoup.nodes.Document humanReadableHTMLDocument = HeaderHumanReadableGenerator
					.generateHeaderHTMLForCQLMeasure(simpleXmlStr);
			
			humanReadableHTML = humanReadableHTMLDocument.toString();
			
			generateHumanReadable(humanReadableHTMLDocument, simpleXMLProcessor, cqlModel, cqlResult);
			humanReadableHTML = humanReadableHTMLDocument.toString();
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		definitionsOrFunctionsAlreadyDisplayed.clear();
		cqlObjects.clear();
		includedXMLMap.clear();
		
		return humanReadableHTML;
	}
	
	private static Map<String, XmlProcessor> loadIncludedLibXMLProcessors(
			CQLModel cqlModel) {
		
		Map<String, XmlProcessor> returnMap = new HashMap<String, XmlProcessor>();
		
		Map<String, LibHolderObject> includeMap = cqlModel.getIncludedCQLLibXMLMap();
		
		for(String libName: includeMap.keySet()){
			LibHolderObject lib = includeMap.get(libName);
			String xml = lib.getMeasureXML();
			XmlProcessor xmlProcessor = new XmlProcessor(xml);
			returnMap.put(libName, xmlProcessor);
		}		
		
		return returnMap;
	}

	/**
	 * Generate human readable.
	 *
	 * @param humanReadableHTMLDocument the human readable html document
	 * @param simpleXMLProcessor the simple xml processor
	 * @param cqlModel the cql file object
	 * @param cqlResult 
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static void generateHumanReadable(
			Document humanReadableHTMLDocument, XmlProcessor simpleXMLProcessor, CQLModel cqlModel, SaveUpdateCQLResult cqlResult)
					throws XPathExpressionException {
		
		generateTableOfContents(humanReadableHTMLDocument, simpleXMLProcessor);
		generatePopulationCriteriaHumanReadable(humanReadableHTMLDocument,
				simpleXMLProcessor, cqlModel,cqlResult);
		
		CQLArtifactHolder usedCQLArtifactHolder = 
				CQLUtil.getCQLArtifactsReferredByPoplns(simpleXMLProcessor.getOriginalDoc());
		
		generateDefinitionsSection(humanReadableHTMLDocument, simpleXMLProcessor, cqlModel, cqlResult, usedCQLArtifactHolder);
		generateFunctionsSection(humanReadableHTMLDocument, simpleXMLProcessor, cqlModel, cqlResult, usedCQLArtifactHolder);
		generateTerminology(humanReadableHTMLDocument, simpleXMLProcessor, cqlResult);
		generateQDMDataElements(humanReadableHTMLDocument, simpleXMLProcessor); 
		generateSupplementalDataVariables(humanReadableHTMLDocument, simpleXMLProcessor, cqlModel, cqlResult);
		generateRiskAdjustmentVariables(humanReadableHTMLDocument, simpleXMLProcessor, cqlModel, cqlResult);
		HeaderHumanReadableGenerator.addMeasureSet(simpleXMLProcessor,
				humanReadableHTMLDocument);
	}
	
	/**
	 * Generates the terminology section of the human readable export. 
	 * @param humanReadableHTMLDocument the html document
	 * @param simpleXMLProcessor the simple xml processor
	 * @param cqlModel the cql model
	 * @param cqlResult the cql result, which contains information about used artifacts
	 * @throws XPathExpressionException 
	 */
	private static void generateTerminology(Document humanReadableHTMLDocument, XmlProcessor simpleXMLProcessor, SaveUpdateCQLResult cqlResult) throws XPathExpressionException {
		definitionsOrFunctionsAlreadyDisplayed.clear();
		Element bodyElement = humanReadableHTMLDocument.body(); 
		bodyElement.append("<h3><a name=\"d1e555\" href=\"#toc\">Terminology</a></h3>");
		
		Element mainDivElement = bodyElement.appendElement("div"); 
		Element mainListElement = mainDivElement.appendElement(HTML_UL); 
		mainListElement.attr("style","padding-left: 50px;");
		NodeList elements = simpleXMLProcessor.findNodeList(simpleXMLProcessor.getOriginalDoc(), "/measure/elementLookUp/qdm"); 	
		if(elements.getLength() > 0) {
			generateTerminologyCode(mainListElement, simpleXMLProcessor);
			generateTerminologyValuesets(mainListElement, simpleXMLProcessor);
		}
		
		else {
			mainListElement.appendElement(HTML_LI).appendText("None");
		}
	}
	
	/**
	 * Generates the valuesets for the terminology section
	 * @param mainListElement the list element for the terminology section
	 * @param simpleXMLProcessor the xml procsesor
	 * @throws XPathExpressionException
	 */
	private static void generateTerminologyValuesets(Element mainListElement, XmlProcessor simpleXMLProcessor) throws XPathExpressionException {
		NodeList elements = simpleXMLProcessor.findNodeList(simpleXMLProcessor.getOriginalDoc(), "/measure/elementLookUp/qdm[@code=\"false\"]");
		ArrayList<String> valuesetStringList = new ArrayList<>(); 
		
		for(int i = 0; i < elements.getLength(); i++) {
			Node current = elements.item(i);
			String name = current.getAttributes().getNamedItem("name").getNodeValue();
			String oid = current.getAttributes().getNamedItem("oid").getNodeValue();  
			String version = current.getAttributes().getNamedItem("version").getNodeValue(); 
			
			String output = "";
			if(StringUtils.isNotBlank(version) && !version.equalsIgnoreCase("1.0")) {
				output = "valueset \"" + name + "\" (" + oid + ", version " + version + ")";
			} else {
				output = "valueset \"" + name + "\" (" + oid + ")";
			}
			
			// no duplicates should appear
			if(!valuesetStringList.contains(output)) {
				valuesetStringList.add(output);
			}
		}
		
		Collections.sort(valuesetStringList, String.CASE_INSENSITIVE_ORDER);
		for(String listItem : valuesetStringList) {
			mainListElement.appendElement(HTML_LI).append(listItem);
		}
	}
	
	/**
	 * Generates the the code part of the terminology section
	 * @param mainListElement the list element for the terminology section
	 * @param simpleXMLProcessor the xml processor
	 * @throws XPathExpressionException
	 */
	private static void generateTerminologyCode(Element mainListElement, XmlProcessor simpleXMLProcessor) throws XPathExpressionException {
		NodeList elements = simpleXMLProcessor.findNodeList(simpleXMLProcessor.getOriginalDoc(), "/measure/elementLookUp/qdm[@code=\"true\"]");
		
		Set<String> tempSet = new HashSet<>(); 
		ArrayList<String> codeStringList = new ArrayList<>(); 
		for(int i = 0; i < elements.getLength(); i++) {
			Node current = elements.item(i);
			String codeName = current.getAttributes().getNamedItem("name").getNodeValue(); 
			String codeOID = current.getAttributes().getNamedItem("oid").getNodeValue();
			String codeSystemName = current.getAttributes().getNamedItem("taxonomy").getNodeValue(); 

			Node isCodeSystemVersionIncludedNode = current.getAttributes().getNamedItem("isCodeSystemVersionIncluded");
			// by default the code system should be included if the isCodeSystemIncluded tag does not exist
			String isCodeSystemVersionIncluded = "true";
			if(isCodeSystemVersionIncludedNode != null) {
				isCodeSystemVersionIncluded = isCodeSystemVersionIncludedNode.getNodeValue();
			} 

			String codeSystemVersion = "";
			if("true".equals(isCodeSystemVersionIncluded)) {
				codeSystemVersion = " version " + current.getAttributes().getNamedItem("codeSystemVersion").getNodeValue();
			}

			String codeOutput = "code \"" + codeName + "\" (\"" + codeSystemName  + codeSystemVersion + " Code (" + codeOID +")\")";

			// output strings will be unique due the suffix constraint. No code can have the same identifier
			// no duplicates should appear
			if(tempSet.add(codeOutput)) {
				codeStringList.add(codeOutput);
			}
		}  
		
		codeStringList.sort(String::compareToIgnoreCase);
		
		for(String listItem : codeStringList) {
			Element codeLIelement = mainListElement.appendElement(HTML_LI);
			codeLIelement.attr("style", "width:80%");	
			codeLIelement.append(listItem);
		}
	}

	
	private static void generateSupplementalDataVariables(
			Document humanReadableHTMLDocument, XmlProcessor simpleXMLProcessor, CQLModel cqlModel, SaveUpdateCQLResult cqlResult)
					throws XPathExpressionException {
		definitionsOrFunctionsAlreadyDisplayed.clear();
		Element bodyElement = humanReadableHTMLDocument.body();
		bodyElement
		.append("<h3><a name=\"d1e767\" href=\"#toc\">Supplemental Data Elements</a></h3>");
		
		Element mainDivElement = bodyElement.appendElement("div");
		Element mainListElement = mainDivElement.appendElement(HTML_UL);
		mainListElement.attr("style", "list-style:none;padding-left: 25px;");
				
		List<String> supplementalDefinitionList = getSupplementalDefinitions(simpleXMLProcessor);
		
		if (supplementalDefinitionList.size() > 0) {
			for(String definitionName : supplementalDefinitionList){
				generateHTMLForSingleExpression(cqlModel,cqlResult,simpleXMLProcessor,CQLDEFINITION,definitionName,mainListElement,true,null);				
			}
		} else {
			mainListElement.appendElement(HTML_LI).appendText("None");
		}
	}
	
	private static List<String> getSupplementalDefinitions(XmlProcessor simpleXMLProcessor) throws XPathExpressionException{
		return getReferredDefinitions(simpleXMLProcessor, "/measure/supplementalDataElements/cqldefinition");
	}
	
	private static void generateRiskAdjustmentVariables(
			Document humanReadableHTMLDocument, XmlProcessor simpleXMLProcessor, CQLModel cqlModel, SaveUpdateCQLResult cqlResult)
					throws XPathExpressionException {
		definitionsOrFunctionsAlreadyDisplayed.clear();
		Element bodyElement = humanReadableHTMLDocument.body();
		bodyElement
		.append("<h3><a name=\"d1e879\" href=\"#toc\">Risk Adjustment Variables</a></h3>");
		
		Element mainDivElement = bodyElement.appendElement("div");
		Element mainListElement = mainDivElement.appendElement(HTML_UL);
		mainListElement.attr("style", "list-style:none;padding-left: 25px;");
				
		List<String> riskAdjDefinitionList = getRiskAdjustmentDefinitions(simpleXMLProcessor);
		
		if (riskAdjDefinitionList.size() > 0) {
			for(String definitionName : riskAdjDefinitionList){
				generateHTMLForSingleExpression(cqlModel,cqlResult,simpleXMLProcessor,CQLDEFINITION,definitionName,mainListElement,true,null);
			}
		} else {
			mainListElement.appendElement(HTML_LI).appendText("None");
		}
	}
	
	private static List<String> getRiskAdjustmentDefinitions(XmlProcessor simpleXMLProcessor) throws XPathExpressionException{
		
		return getReferredDefinitions(simpleXMLProcessor, "/measure/riskAdjustmentVariables/cqldefinition");
		
	}
	

	private static List<String> getReferredDefinitions(
			XmlProcessor simpleXMLProcessor, String referredDefXPath) throws XPathExpressionException {
		List<String> definitionList = new ArrayList<String>();
				
		NodeList elements = simpleXMLProcessor.findNodeList(
				simpleXMLProcessor.getOriginalDoc(),
				referredDefXPath);
		
		if (elements.getLength() > 0) {
			for(int i=0;i<elements.getLength();i++){
				Node childNode = elements.item(i);
				String uuid = childNode.getAttributes().getNamedItem("uuid").getNodeValue();
				String xpathforSubTree = "/measure/cqlLookUp//definition[@id='"+ uuid +"']";
				Node defineNode = simpleXMLProcessor.findNode(simpleXMLProcessor.getOriginalDoc(),
						xpathforSubTree);
				String defineNodeName = defineNode.getAttributes().getNamedItem("name").getNodeValue();
				definitionList.add(defineNodeName);
			}
		}
		
		return definitionList;
	}

	/**
	 * Generate table of contents.
	 *
	 * @param humanReadableHTMLDocument the human readable html document
	 * @param simpleXMLProcessor the simple xml processor
	 */
	private static void generateTableOfContents(
			Document humanReadableHTMLDocument, XmlProcessor simpleXMLProcessor) {
		Element bodyElement = humanReadableHTMLDocument.body();
		
		bodyElement.append("<h2><a name=\"toc\">Table of Contents</a></h2>");
		Element tocULElement = bodyElement.appendElement(HTML_UL);
		tocULElement.attr("style","padding-left: 50px;");
		
		Element populationCriteriaLI = tocULElement.appendElement(HTML_LI);
		populationCriteriaLI
		.append("<a href=\"#d1e405\">Population Criteria</a>");
		
		Element definitionsLI = tocULElement.appendElement(HTML_LI);
		definitionsLI
		.append("<a href=\"#d1e649\">Definitions</a>");
		
		Element functionsLI = tocULElement.appendElement(HTML_LI);
		functionsLI
		.append("<a href=\"#d1e650\">Functions</a>");
		
		Element terminologyLI = tocULElement.appendElement(HTML_LI); 
		terminologyLI.append("<a href=\"#d1e555\">Terminology</a>"); 
		
		Element dataCriteriaLI = tocULElement.appendElement(HTML_LI);
		dataCriteriaLI
		.append("<a href=\"#d1e647\">Data Criteria (QDM Data Elements)</a>");
		
		Element supplementalCriteriaLI = tocULElement.appendElement(HTML_LI);
		supplementalCriteriaLI
		.append("<a href=\"#d1e767\">Supplemental Data Elements</a>");
		
		Element riskAdjustmentCriteriaLI = tocULElement.appendElement(HTML_LI);
		riskAdjustmentCriteriaLI
		.append("<a href=\"#d1e879\">Risk Adjustment Variables</a>");
		
		bodyElement
		.append("<div style=\"float:left; background:teal; height:3px; width:80%\"></div><pre><br/></pre>");
		
	}
	
	private static void generateDefinitionsSection(
			Document humanReadableHTMLDocument, XmlProcessor simpleXMLProcessor, CQLModel cqlModel, SaveUpdateCQLResult cqlResult, CQLArtifactHolder usedCQLArtifactHolder)
			throws XPathExpressionException {
			
		Element bodyElement = humanReadableHTMLDocument.body();
		bodyElement.append("<h3><a name=\"d1e649\" href=\"#toc\">Definitions</a></h3>");	
		
		Element mainDivElement = bodyElement.appendElement("div");
		Element mainListElement = mainDivElement.appendElement(HTML_UL);
		mainListElement.attr("style","list-style:none;padding-left: 10px;");
		
		List<String> usedDefinitions = cqlResult.getUsedCQLArtifacts().getUsedCQLDefinitions();
		
		List<String> definitionsList = new ArrayList<String>(usedCQLArtifactHolder.getCqlDefFromPopSet());
		definitionsList.removeAll(usedDefinitions);
		
		definitionsList.addAll(usedDefinitions);
		
		definitionsList = definitionsList.stream().distinct().collect(Collectors.toList());
		java.util.Collections.sort(definitionsList, String.CASE_INSENSITIVE_ORDER);
				
		Element mainElement = mainListElement.appendElement("li");
		mainElement.attr("class", "list-unstyled");
		mainElement.attr("style","list-style:none;");
		
		Element divElement = mainElement.appendElement("div");
				
		for(String definitionName:definitionsList){
			
			Element ulElement = divElement.appendElement(HTML_UL);
			ulElement.attr("class", "list-unstyled");
			ulElement.attr("style","list-style:none;padding-left:0;");
			generateHTMLForSingleExpression(cqlModel,cqlResult,simpleXMLProcessor,CQLDEFINITION,definitionName,ulElement,true,null);
		}
	}
	
	private static void generateFunctionsSection(
			Document humanReadableHTMLDocument, XmlProcessor simpleXMLProcessor, CQLModel cqlModel, SaveUpdateCQLResult cqlResult, CQLArtifactHolder usedCQLArtifactHolder)
			throws XPathExpressionException {
			
		Element bodyElement = humanReadableHTMLDocument.body();
		bodyElement.append("<h3><a name=\"d1e650\" href=\"#toc\">Functions</a></h3>");
		
		Element mainDivElement = bodyElement.appendElement("div");
		Element mainListElement = mainDivElement.appendElement(HTML_UL);
		mainListElement.attr("style","list-style:none;padding-left: 10px;");
		
		List<String> usedFunctions = cqlResult.getUsedCQLArtifacts().getUsedCQLFunctions();
		
		List<String> functionsList = new ArrayList<String>(usedCQLArtifactHolder.getCqlFuncFromPopSet());
		functionsList.removeAll(usedFunctions);
		
		functionsList.addAll(usedFunctions);
		
		functionsList = functionsList.stream().distinct().collect(Collectors.toList());
		java.util.Collections.sort(functionsList, String.CASE_INSENSITIVE_ORDER);
		
		Element mainElement = mainListElement.appendElement("li");
		mainElement.attr("class", "list-unstyled");
		mainElement.attr("style","list-style:none;");
		
		Element divElement = mainElement.appendElement("div");
		
		if(functionsList.size() == 0){
			mainListElement.appendElement(HTML_LI).appendText("None");
		}
		
		for(String functionName:functionsList){
			
			Element ulElement = divElement.appendElement(HTML_UL);
			ulElement.attr("class", "list-unstyled");
			ulElement.attr("style","list-style:none;padding-left:0;");
			generateHTMLForSingleExpression(cqlModel,cqlResult,simpleXMLProcessor,CQLFUNCTION,functionName,ulElement,true,null);
		}
	}
	
	/**
	 * Generate population criteria human readable.
	 *
	 * @param humanReadableHTMLDocument the human readable html document
	 * @param simpleXMLProcessor the simple xml processor
	 * @param cqlModel the cql file object
	 * @param cqlResult 
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static void generatePopulationCriteriaHumanReadable(
			Document humanReadableHTMLDocument, XmlProcessor simpleXMLProcessor, CQLModel cqlModel, SaveUpdateCQLResult cqlResult)
					throws XPathExpressionException {
		
		Element bodyElement = humanReadableHTMLDocument.body();
		bodyElement.append("<h3><a name=\"d1e405\" href=\"#toc\">Population Criteria</a></h3>");
		
		Element mainDivElement = bodyElement.appendElement("div");
		Element mainListElement = mainDivElement.appendElement(HTML_UL);
		
				
		NodeList groupNodeList = simpleXMLProcessor.findNodeList(
				simpleXMLProcessor.getOriginalDoc(),
				"/measure/measureGrouping/group");
		
		TreeMap<Integer, Node> groupMap = new TreeMap<Integer, Node>();
		
		for (int i = 0; i < groupNodeList.getLength(); i++) {
			Node measureGroupingNode = groupNodeList.item(i);
			String key = measureGroupingNode.getAttributes().getNamedItem("sequence").getNodeValue();
			groupMap.put(Integer.parseInt(key), measureGroupingNode);
		}
		
		int j=0;
		for (Integer key : groupMap.keySet()) {
			if (groupMap.size() > 1) {
				
				mainListElement.attr("style","list-style:none;");
				
				Element mainElement = mainListElement.appendElement("li");
				mainElement.attr("class", "list-unstyled");
								
				if(j > 0){
					mainElement.attr("style","list-style:none;padding-left:0;padding-top:15px;");
				}else{
					mainElement.attr("style","list-style:none;padding-left:0;");
				}
				
				Element divElement = mainElement.appendElement("div");
				divElement.attr("class", "treeview hover p-l-10");
				
				Element checkBoxElement = divElement.appendElement("input");
				checkBoxElement.attr("type", "checkbox");
				String id = "test-Population_Criteria" + (key.toString()) + "_" 
						+ (int) (Math.random() * 1000);
				checkBoxElement.attr("id", id);
								
				Element labelElement = divElement.appendElement("label");
				labelElement.attr("for", id);
				labelElement.attr("class", "list-header");
				labelElement.append("<b>Population Criteria "
						+ (key.toString()) );
								
				NodeList clauseNodeList = groupMap.get(key).getChildNodes();
				generatePopulationNodes(clauseNodeList, divElement.appendElement(HTML_UL),
						groupNodeList.getLength(),key, simpleXMLProcessor, cqlModel, cqlResult);
								
			}else{
				mainListElement.attr("style","list-style:none;padding-left:10px;");
				
				NodeList clauseNodeList = groupMap.get(key).getChildNodes();
				generatePopulationNodes(clauseNodeList, mainListElement,
						groupNodeList.getLength(),key, simpleXMLProcessor, cqlModel, cqlResult);
			}
			j++;
		}
	}
	
	/**
	 * Generate the qdm elements in the Human Readable
	 * 
	 * @param humanReadableHTMLDocument the human readable html document
	 * @param simpleXMLProcessor the simple xml processor
	 * @param cqlModel 
	 */
	private static void generateQDMDataElements(Document humanReadableHTMLDocument, XmlProcessor simpleXMLProcessor) {
		Element bodyElement = humanReadableHTMLDocument.body();
		bodyElement.append("<h3><a name=\"d1e647\" href=\"#toc\">Data Criteria (QDM Data Elements)</a></h3>");
		
		Element qdmElementUL = bodyElement.appendElement(HTML_UL);
		qdmElementUL.attr("style","padding-left: 50px;");
		
		try {
			
			NodeList qdmValuesetElementList = simpleXMLProcessor.findNodeList(simpleXMLProcessor.getOriginalDoc(), 
														"/measure/elementLookUp/qdm[@code='false'][@datatype]");
			
			NodeList qdmCodeElementList = simpleXMLProcessor.findNodeList(simpleXMLProcessor.getOriginalDoc(), 
														"/measure/elementLookUp/qdm[@code='true'][@datatype]");
			
			if((qdmValuesetElementList.getLength() + qdmCodeElementList.getLength()) == 0) {
				String output = "None"; 
				Element qdmElementLI = qdmElementUL.appendElement(HTML_LI);   
				qdmElementLI.attr("style", "width:80%");
				qdmElementLI.append(output);
			}
			
			else {
				ArrayList<String> qdmValueSetElementStringList = new ArrayList<String>();
				ArrayList<String> qdmCodeElementStringList = new ArrayList<String>();

				// make HTML output strings for qdm value-set nodes 
				//Pattern: "{Datatype}: {value set name}" using "{value set name} ({value set OID} version {value set version*})"
				for(int i = 0; i < qdmValuesetElementList.getLength(); i++) {
					
					String dataTypeName = qdmValuesetElementList.item(i).getAttributes().getNamedItem("datatype").getNodeValue(); 
					if("attribute".equals(dataTypeName)){
						dataTypeName = "Attribute";
					}
					
					String name = qdmValuesetElementList.item(i).getAttributes().getNamedItem("name").getNodeValue(); 
					String oid = qdmValuesetElementList.item(i).getAttributes().getNamedItem("oid").getNodeValue(); 

					String version = null;
					if(qdmValuesetElementList.item(i).getAttributes().getNamedItem("version") != null){
						version = qdmValuesetElementList.item(i).getAttributes().getNamedItem("version").getNodeValue();
					}
										
					String output = String.format("\"%s: %s\" using \"%s (%s)\"", dataTypeName, name, name, oid);
					
					if(StringUtils.isNotBlank(version) && !version.equals("1.0") && !version.equals("1")){
						output = String.format("\"%s: %s\" using \"%s (%s, version %s)\"", dataTypeName, name, name, oid, version);
					}
								
					qdmValueSetElementStringList.add(output); 
				}
				
				// sort and append qdm value-set elements
				Collections.sort(qdmValueSetElementStringList, String.CASE_INSENSITIVE_ORDER);
				
				for(String valueSetString:qdmValueSetElementStringList){
					Element qdmElemtentLI = qdmElementUL.appendElement(HTML_LI);
					qdmElemtentLI.attr("style", "width:80%");
					qdmElemtentLI.append(valueSetString);
				}
				
				//make HTML output strings for qdm code elements 
				//Pattern: "{Datatype}: {code name}" using "{code name} ({code system name} version {code system version} Code {code})"
				for(int i = 0; i < qdmCodeElementList.getLength(); i++) {
					NamedNodeMap attributeMap = qdmCodeElementList.item(i).getAttributes();
					String dataTypeName = attributeMap.getNamedItem("datatype").getNodeValue(); 
					if("attribute".equals(dataTypeName)){
						dataTypeName = "Attribute";
					}
					
					String name = attributeMap.getNamedItem("name").getNodeValue(); 
					String oid = attributeMap.getNamedItem("oid").getNodeValue(); 
					String codeSystemVersion = attributeMap.getNamedItem("codeSystemVersion").getNodeValue();					
					String codeSystemName = attributeMap.getNamedItem("taxonomy").getNodeValue();
					boolean isCodeSystemVersionIncluded = true;
					String output = "";
					Node isCodeSystemVersionIncludedNode = attributeMap.getNamedItem("isCodeSystemVersionIncluded");
					if(isCodeSystemVersionIncludedNode != null) {
						isCodeSystemVersionIncluded = Boolean.parseBoolean(isCodeSystemVersionIncludedNode.getNodeValue());
					} 
					if(isCodeSystemVersionIncluded) {
						output = String.format("\"%s: %s\" using \"%s (%s version %s Code %s)\"", dataTypeName, name, name, codeSystemName, codeSystemVersion, oid);
					} else {
						output = String.format("\"%s: %s\" using \"%s (%s Code %s)\"", dataTypeName, name, name, codeSystemName, oid);
					}
													
					qdmCodeElementStringList.add(output); 
				}
				
				//sort and append qdm code elements
				Collections.sort(qdmCodeElementStringList, String.CASE_INSENSITIVE_ORDER);
				
				for(String codeString:qdmCodeElementStringList){
					Element qdmElemtentLI = qdmElementUL.appendElement(HTML_LI);
					qdmElemtentLI.attr("style", "width:80%");
					qdmElemtentLI.append(codeString);
				}
			}
			
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Generate population nodes.
	 *
	 * @param clauseNodeList the clause node list
	 * @param mainListElement the main list element
	 * @param totalGroupCount the total group count
	 * @param currentGroupNumber the current group number
	 * @param simpleXMLProcessor the simple xml processor
	 * @param cqlModel the cql file object
	 */
	private static void generatePopulationNodes(NodeList clauseNodeList,
			Element mainListElement, int totalGroupCount,
			int currentGroupNumber, XmlProcessor simpleXMLProcessor, CQLModel cqlModel, SaveUpdateCQLResult cqlResult) {
		
		for (String element : POPULATION_NAME_ARRAY) {
			generatePopulationNodes(element, clauseNodeList,
					mainListElement, totalGroupCount, currentGroupNumber,
					simpleXMLProcessor, cqlModel, cqlResult);
		}
	}
	
	/**
	 * Generate population nodes.
	 *
	 * @param populationType the population type
	 * @param clauseNodeList the clause node list
	 * @param mainListElement the main list element
	 * @param totalGroupCount the total group count
	 * @param currentGroupNumber the current group number
	 * @param simpleXMLProcessor the simple xml processor
	 * @param cqlModel the cql file object
	 */
	private static void generatePopulationNodes(String populationType,
			NodeList clauseNodeList, Element mainListElement,
			int totalGroupCount, int currentGroupNumber,
			XmlProcessor simpleXMLProcessor, CQLModel cqlModel, SaveUpdateCQLResult cqlResult) {
		
		// find all clause nodes with attribute type=populationType
		List<Node> clauseNodes = new ArrayList<Node>();
		for (int j = 0; j < clauseNodeList.getLength(); j++) {
			Node clauseNode = clauseNodeList.item(j);
			if ("clause".equals(clauseNode.getNodeName())) {
				String popType = clauseNode.getAttributes()
						.getNamedItem("type").getNodeValue();
				if (popType.equals(populationType)) {
					clauseNodes.add(clauseNode);
				}
			}
		}
		
		if (clauseNodes.size() > 1) {
			Element populationListElement = mainListElement
					.appendElement(HTML_LI);
			populationListElement.attr("class", "list-unstyled");
			populationListElement.attr("style", "list-style:none;padding-left:0px;");
			//Element boldNameElement = populationListElement.appendElement("b");
			//String populationName = getPopulationName(populationType, true);
			
			//boldNameElement.appendText(populationName + " =");
			for (int c = 0; c < clauseNodes.size(); c++) {
				Node clauseNode = clauseNodes.get(c);
				String childPopulationName = getPopulationName(populationType);
			
				childPopulationName += " " + (c + 1);
				if ("initialPopulation".equalsIgnoreCase(clauseNode
						.getAttributes().getNamedItem("type").getNodeValue())) {
					initialPopulationHash.put(clauseNode.getAttributes()
							.getNamedItem("uuid").getNodeValue(),
							String.valueOf(c + 1));
				}
				String itemCountText = getItemCountText(clauseNode);
				String popassoc = getPopAssoc(clauseNode, simpleXMLProcessor);
				
				childPopulationName += (popassoc.length() > 0 ? popassoc : "") + (itemCountText.length() > 0 ? itemCountText : "");

				parseAndBuildHTML(simpleXMLProcessor,
						populationListElement, clauseNode, c + 1, cqlModel, childPopulationName, cqlResult);
				
			}
		} else if (clauseNodes.size() == 1) {
			Element populationListElement = mainListElement
					.appendElement(HTML_LI);
			populationListElement.attr("class", "list-unstyled");
			populationListElement.attr("style", "list-style:none;padding-left:0px;");
			//Element boldNameElement = populationListElement.appendElement("b");
			String populationName = getPopulationName(populationType);
			
			if ("initialPopulation".equalsIgnoreCase(clauseNodes.get(0)
					.getAttributes().getNamedItem("type").getNodeValue())) {
				initialPopulationHash.put(clauseNodes.get(0).getAttributes()
						.getNamedItem("uuid").getNodeValue(), "-1");
			}
			
			String itemCountText = getItemCountText(clauseNodes.get(0));
			String popassoc = getPopAssoc(clauseNodes.get(0), simpleXMLProcessor);
			populationName += (popassoc.length() > 0 ? popassoc : "") + (itemCountText.length() > 0 ? itemCountText : "");
			
			/*boldNameElement.appendText(populationName
					+ (itemCountText.length() > 0 ? itemCountText : "") + " =");*/
			/*if(populationName.startsWith("Measure Observation")){
				populationListElement = populationListElement.appendElement(HTML_UL);
			}*/
			parseAndBuildHTML(simpleXMLProcessor, populationListElement,
					clauseNodes.get(0), 1, cqlModel, populationName, cqlResult);
			
		}
	}
	
	/**
	 * Gets the pop assoc.
	 *
	 * @param node the node
	 * @param processor the processor
	 * @return the pop assoc
	 */
	private static String getPopAssoc(Node node, XmlProcessor processor) {
		String stringAssoc = "";
		try {

			Node nodeAssoc = node.getAttributes().getNamedItem(
					"associatedPopulationUUID");
			if (nodeAssoc != null) {

				Node newAssoc = processor.findNode(processor.getOriginalDoc(),
						"//clause[@uuid=\"" + nodeAssoc.getNodeValue() + "\"]");
				if (newAssoc != null) {
					String name = newAssoc.getAttributes().getNamedItem("displayName").getNodeValue();
					String type = newAssoc.getAttributes().getNamedItem("type").getNodeValue();
					int popCpount = countSimilarPopulationsInGroup(type, newAssoc.getParentNode());
					if(popCpount == 1){
						name = getPopulationName(type);
					}
					stringAssoc = "    (Association: " + name + ")";
				}
			}

		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stringAssoc;
	}
	
	private static int countSimilarPopulationsInGroup(String type,
			Node node) {
		
		int count = 0;
		NodeList childClauses = node.getChildNodes();
		if(childClauses != null){
			for(int i=0;i<childClauses.getLength();i++){
				Node clauseNode = childClauses.item(i);
				String popType = clauseNode.getAttributes().getNamedItem("type").getNodeValue();
				if(popType.equals(type)){
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * Parses the and build html.
	 *
	 * @param populationOrSubtreeXMLProcessor the population or subtree xml processor
	 * @param populationOrSubtreeListElement the population or subtree list element
	 * @param clauseNode the clause node
	 * @param currentGroupNumber the current group number
	 * @param cqlModel the cql file object
	 * @param PopulationDisplayName the population display name
	 * @param cqlResult 
	 */
	private static void parseAndBuildHTML(
			XmlProcessor populationOrSubtreeXMLProcessor,
			Element populationOrSubtreeListElement, Node clauseNode,
			int currentGroupNumber, CQLModel cqlModel, String populationDisplayName, SaveUpdateCQLResult cqlResult) {
		
		try {
						
			String parentName = "";
			if (clauseNode.getAttributes().getNamedItem("type") != null) {
				parentName = clauseNode.getAttributes().getNamedItem("type")
						.getNodeValue();
			}
			
			Node cqlNode = clauseNode;
			if(clauseNode.hasChildNodes()){
				cqlNode = clauseNode.getFirstChild();
			}
			generatePopulationCriteria(populationOrSubtreeListElement, cqlModel, cqlNode, parentName, populationDisplayName, cqlResult, populationOrSubtreeXMLProcessor);
			
		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the item count text.
	 *
	 * @param node the node
	 * @return the item count text
	 */
	private static String getItemCountText(Node node) {
		String itemCountText = "";
		
		if ("clause".equals(node.getNodeName())) {
			NodeList childNodes = node.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node child = childNodes.item(i);
				if ("itemCount".equals(child.getNodeName())) {
					NodeList elementRefList = child.getChildNodes();
					for (int j = 0; j < elementRefList.getLength(); j++) {
						Node elementRef = elementRefList.item(j);
						String nodeText = elementRef.getAttributes()
								.getNamedItem("dataType").getNodeValue()
								+ ": "
								+ elementRef.getAttributes()
								.getNamedItem("name").getNodeValue();
						if (j == (elementRefList.getLength() - 1)) {
							itemCountText += nodeText;
						} else {
							itemCountText += nodeText + "; ";
						}
					}
					if (itemCountText.length() > 0) {
						itemCountText = "    (Item Count: " + itemCountText
								+ ")";
					}
					break;
				}
			}
		}
		
		return itemCountText;
	}
	
	
	/**
	 * Gets the population name.
	 *
	 * @param nodeValue the node value
	 * @return the population name
	 */
	private static String getPopulationName(String nodeValue) {
		String populationName = "";
		if ("initialPopulation".equals(nodeValue)) {
			populationName = "Initial Population";
		} else if ("measurePopulation".equals(nodeValue)) {
			populationName = "Measure Population";
		} else if ("measurePopulationExclusions".equals(nodeValue)) {
			populationName = "Measure Population Exclusions";
		} else if ("measureObservation".equals(nodeValue)) {
			populationName = "Measure Observation";
		} else if ("stratum".equals(nodeValue)) {
			populationName = "Stratification";
		} else if ("denominator".equals(nodeValue)) {
			populationName = "Denominator";
		} else if ("denominatorExclusions".equals(nodeValue)) {
			populationName = "Denominator Exclusions";
		} else if ("denominatorExceptions".equals(nodeValue)) {
			populationName = "Denominator Exceptions";
		} else if ("numerator".equals(nodeValue)) {
			populationName = "Numerator";
		} else if ("numeratorExclusions".equals(nodeValue)) {
			populationName = "Numerator Exclusions";
		}
		return populationName;
	}
	
	public static String generateCQLHumanReadableForSinglePopulation(
			Node populationNode, String measureXML, CQLModel cqlModel,
			CQLLibraryDAO cqlLibraryDAO) {
		
		definitionsOrFunctionsAlreadyDisplayed.clear();
		cqlObjects.clear();
		includedXMLMap.clear();
		
		XmlProcessor measureXMLProcessor = new XmlProcessor(measureXML);
		
		populateCQLObjectsList(cqlModel);
		
		SaveUpdateCQLResult cqlResult = CQLUtil.parseCQLLibraryForErrors(cqlModel, cqlLibraryDAO, cqlObjects);
		includedXMLMap = loadIncludedLibXMLProcessors(cqlModel);

		Node cqlNode = populationNode.getFirstChild();
		String populationName = populationNode.getAttributes()
				.getNamedItem("displayName").getNodeValue();

		org.jsoup.nodes.Document htmlDocument = null;
		htmlDocument = createBaseHTMLDocument("Human Readable for "
				+ populationName);

		Element bodyElement = htmlDocument.body();

		if(cqlResult.getCqlErrors().size() == 0){
			includedXMLMap = loadIncludedLibXMLProcessors(cqlModel);
	
			generatePopulationCriteria(bodyElement, cqlModel, cqlNode,
					populationName, populationName,cqlResult,measureXMLProcessor);
		}else{
			Element mainDivElement = bodyElement.appendElement("div");
			mainDivElement.attr("class", "treeview hover p-l-10");
			mainDivElement.append("Unable to generate Human Readable. There are validation errors in CQL. Please correct and try again.");
		}
		
		definitionsOrFunctionsAlreadyDisplayed.clear();
		cqlObjects.clear();
		includedXMLMap.clear();
		
		return htmlDocument.html();
	}

	/**
	 * Generate population criteria.
	 *
	 * @param bodyElement the body element
	 * @param cqlModel the cql file object
	 * @param cqlNode the cql node
	 * @param populationName the population name
	 * @param populationDisplayName the population display name
	 * @param cqlResult 
	 * @param populationOrSubtreeXMLProcessor 
	 */
	private static void generatePopulationCriteria(Element bodyElement,
			CQLModel cqlModel, Node cqlNode, String populationName, String populationDisplayName, SaveUpdateCQLResult cqlResult, XmlProcessor populationOrSubtreeXMLProcessor) {
		
		Element mainDivElement = bodyElement.appendElement("div");
		mainDivElement.attr("class", "treeview hover p-l-10");

		Element mainULElement = mainDivElement.appendElement("ul");
		mainULElement.attr("class", "list-unstyled");

		String cqlNodeType = cqlNode.getNodeName();
		String cqlName = cqlNode.getAttributes().getNamedItem("displayName").getNodeValue();
				
		System.out.println("Generating Human readable for:" + cqlNodeType + ":"
				+ cqlName);

		if (CQLDEFINITION.equals(cqlNodeType)) {
			
			generateHTMLForPopulation(mainULElement, cqlModel, cqlResult, 
					populationName, populationDisplayName, cqlName, populationOrSubtreeXMLProcessor, cqlNodeType,null);
		} else if (CQLFUNCTION.equals(cqlNodeType)){
			
			generateHTMLForPopulation(mainULElement, cqlModel, cqlResult, 
					populationName, populationDisplayName, cqlName, populationOrSubtreeXMLProcessor, cqlNodeType,null);
		} else if(CQLAGGFUNCTION.equals(cqlNodeType)){

			Node childCQLNode = cqlNode.getChildNodes().item(0);
			String childCQLName = childCQLNode.getAttributes().getNamedItem("displayName").getNodeValue();
			generateHTMLForPopulation(mainULElement, cqlModel, cqlResult, 
					populationName, populationDisplayName, childCQLName, populationOrSubtreeXMLProcessor, CQLFUNCTION, cqlName);
		} else {
			generateHTMLForPopulation(mainULElement, populationName, populationDisplayName, "");
		}
	}
	
	
	/**
	 * Generate html for population.
	 *
	 * @param mainElement the main element
	 * @param cqlResult 
	 * @param cqlModel 
	 * @param populationName the population name
	 * @param populationDisplayName the population display name
	 * @param mainDefinitionName the main definition name
	 * @param populationOrSubtreeXMLProcessor 
	 */
	private static void generateHTMLForPopulation(Element mainElement, String populationName, String populationDisplayName, 
			String mainDefinitionName) {
		// create a base LI element
		Element mainliElement = mainElement.appendElement("li");
		mainliElement.attr("class", "list-unstyled");

		Element checkBoxElement = mainliElement.appendElement("input");
		checkBoxElement.attr("type", "checkbox");
		String id = "test-" + populationName.replace(" ", "_") + "_"
				+ (int) (Math.random() * 1000);
		checkBoxElement.attr("id", id);
		
		Element definitionLabelElement = mainliElement.appendElement("label");
		definitionLabelElement.attr("for", id);
		definitionLabelElement.attr("class", "list-header");

		Element strongElement = definitionLabelElement.appendElement("strong");
		strongElement.appendText(populationDisplayName);
		
		System.out.println(mainDefinitionName);

		generateHTMLToDsiplayNone(mainliElement);
	}

	/**
	 * Generate html for population.
	 *
	 * @param mainElement the main element
	 * @param cqlBaseStatementObject the cql base statement object
	 * @param populationName the population name
	 * @param populationDisplayName the population display name
	 * @param mainDefinitionName the main definition name
	 * @param cqlNodeType 
	 */
	private static void generateHTMLForPopulation(Element mainElement,
			CQLModel cqlModel, SaveUpdateCQLResult cqlResult, String populationName, String populationDisplayName, 
			String mainDefinitionName, XmlProcessor populationOrSubtreeXMLProcessor, String cqlNodeType, String aggFuncLabel) {
		// create a base LI element
		Element mainliElement = mainElement.appendElement("li");
		mainliElement.attr("class", "list-unstyled");

		Element checkBoxElement = mainliElement.appendElement("input");
		checkBoxElement.attr("type", "checkbox");
		String id = "test-" + populationName.replace(" ", "_") + "_"
				+ (int) (Math.random() * 1000);
		checkBoxElement.attr("id", id);
		
		Element definitionLabelElement = mainliElement.appendElement("label");
		definitionLabelElement.attr("for", id);
		definitionLabelElement.attr("class", "list-header");

		Element strongElement = definitionLabelElement.appendElement("strong");
		strongElement.appendText(populationDisplayName);
				
		System.out.println("Main Defintion Name: " + mainDefinitionName);
		generateHTMLForCQLPopulation(cqlModel, cqlResult, populationOrSubtreeXMLProcessor, mainDefinitionName, mainliElement, true,aggFuncLabel,cqlNodeType);
	}

	
	/**
	 * Generate html to dsiplay none.
	 *
	 * @param mainElement the main element
	 */
	private static void generateHTMLToDsiplayNone(Element mainElement){
		Element ulElement = mainElement.appendElement("ul");
		ulElement.attr("class", "code");
		Element liElement = ulElement.appendElement("li");
		Element divElement = liElement.appendElement("div");
		String id = "test-None_" + (int) (Math.random() * 1000);
		divElement.attr("id", id);
		//Element noneLabelElement = divElement.appendElement("label");
		//noneLabelElement.attr("for", id);
		Element strongElement = divElement.appendElement("strong");
		strongElement.attr("class","cql-class");
		strongElement.appendText("None");
	}
	
	/**
	 * Generate html for definition or function, w/o the name of the attached CQL defn/function. Just the logic.
	 *
	 * @param cqlBaseStatementObject the cql base statement object
	 * @param mainElement the main element
	 * @param isTopExpression the is top definition
	 * @param cqlNodeType 
	 */
	private static void generateHTMLForCQLPopulation(
			CQLModel cqlModel, SaveUpdateCQLResult cqlResult, XmlProcessor populationOrSubtreeXMLProcessor, String expressionName, 
			Element mainElement, boolean isTopExpression, String aggFuncLabel, String cqlNodeType) {

		String statementIdentifier = expressionName;
				
		String[] arr = expressionName.split(Pattern.quote("|"));
		if(arr.length == 3){
			expressionName = arr[2];
			statementIdentifier = arr[1] + "." + arr[2];
		}
		
		Element mainULElement = mainElement;
		if (isTopExpression) {
			mainULElement = mainElement.appendElement("ul");
			mainULElement.attr("class", "code");
		}

		Element mainliElement = mainULElement; 
		// create a base LI element
		if (isTopExpression) {
			mainliElement = mainULElement.appendElement("li");
			mainliElement.attr("class", "list-unstyled");
		}

		Element mainDivElement = mainliElement.appendElement("div");
		mainDivElement.attr("class", "treeview hover p-l-10");

		Element subULElement = mainDivElement.appendElement("ul");
		Element subLiElement = subULElement.appendElement("li");
		subLiElement.attr("style","padding-left: 0px;");
		Element subDivElement = subLiElement.appendElement("div");
		
		List<String> codeLineList = new ArrayList<>();
		
		codeLineList = getExpressionLineList(cqlModel, cqlResult, populationOrSubtreeXMLProcessor, expressionName, cqlNodeType,aggFuncLabel);
				
		if(codeLineList.size() > 0){
			mainDivElement.append("&nbsp;" + codeLineList.get(0));
						
			for (int i = 1; i < codeLineList.size(); i++) {
				Element spanElemDefBody = getPreElementWithClass(subDivElement,
						"cql-definition-body");
				spanElemDefBody.append(codeLineList.get(i));
			}
		}			
		//subDivElement.appendElement("br");
	}
	
	/**
	 * Generate html for definition or function.
	 *
	 * @param cqlBaseStatementObject the cql base statement object
	 * @param mainElement the main element
	 * @param isTopExpression the is top definition
	 */
	private static void generateHTMLForSingleExpression(
			CQLModel cqlModel, SaveUpdateCQLResult cqlResult, XmlProcessor populationOrSubtreeXMLProcessor, String cqlNodeType, String expressionName, 
			Element mainElement, boolean isTopExpression, String aggFuncLabel) {

		String statementIdentifier = expressionName;
		XmlProcessor currentProcessor = populationOrSubtreeXMLProcessor;
				
		String[] arr = expressionName.split(Pattern.quote("|"));
		if(arr.length == 3){
			expressionName = arr[2];
			statementIdentifier = arr[1] + "." + arr[2];
			currentProcessor = includedXMLMap.get(arr[0] + "|" + arr[1]);
		}
		
		Element mainliElement = mainElement.appendElement("li");
		mainliElement.attr("class", "list-unstyled");
		
		Element mainDivElement = mainliElement.appendElement("div");
		mainDivElement.attr("class", "treeview hover p-l-10");
		
		Element checkBoxElement = mainDivElement.appendElement("input");
		checkBoxElement.attr("type", "checkbox");
		String id = "test-" + statementIdentifier.replace(" ", "_") + "_"
				+ (int) (Math.random() * 1000);
		checkBoxElement.attr("id", id);
		
		Element definitionLabelElement = mainDivElement.appendElement("label");
		definitionLabelElement.attr("for", id);
		definitionLabelElement.attr("class", "list-header");

		Element strongElement = definitionLabelElement.appendElement("strong");
		String strongText = (aggFuncLabel != null)?(aggFuncLabel + " " + statementIdentifier):(" " + statementIdentifier);
		
		if(cqlNodeType.equals(CQLFUNCTION)){
			String signature = getCQLFunctionSignature(expressionName, currentProcessor);
			strongText += signature;
		}
		strongElement.appendText(strongText);
		
		Element mainULElement = mainDivElement;//assign the <div> element to start with
		if (isTopExpression) {
			mainULElement = mainULElement.appendElement(HTML_UL);
			mainULElement.attr("class", "code");
			mainULElement.attr("style", "margin-right: 20%; opacity: 1;");
		}

		Element liElement = mainULElement;
		// create a base LI element
		if (isTopExpression) {
			liElement = mainULElement.appendElement("li");
			liElement.attr("class", "list-unstyled");
		}
		
		Element codeDivElement = liElement.appendElement("div");

		Element subULElement = codeDivElement.appendElement("ul");
		subULElement.attr("style","padding-left: 0px;");
		Element subLiElement = subULElement.appendElement("li");
		subLiElement.attr("style","padding-left: 0px;");
		subLiElement.attr("class","list-unstyled");
		Element subDivElement = subLiElement.appendElement("div");
		
		List<String> codeLineList = getExpressionLineList(cqlModel, cqlResult, currentProcessor, expressionName, cqlNodeType,aggFuncLabel);
		
		if(codeLineList.size() > 0){
			codeDivElement.append("&nbsp;" + codeLineList.get(0));
						
			for (int i = 1; i < codeLineList.size(); i++) {
				Element spanElemDefBody = getPreElementWithClass(subDivElement,
						"cql-definition-body");
				spanElemDefBody.append(codeLineList.get(i));
			}
		}			
		//subDivElement.appendElement("br");
	}
	
	private static String getCQLFunctionSignature(String expressionName,
			XmlProcessor populationOrSubtreeXMLProcessor) {
		
		String signature = "";
		
		String xPath = "//cqlLookUp//function[@name = '"+ expressionName +"']/arguments";
		try {
			Node argumentsNode = populationOrSubtreeXMLProcessor.findNode(populationOrSubtreeXMLProcessor.getOriginalDoc(), xPath);
			if(argumentsNode != null){
				NodeList children = argumentsNode.getChildNodes();
				for(int i=0;i < children.getLength();i++){
					Node child = children.item(i);
					if(child.getNodeName().equals("argument")){
						String type = child.getAttributes().getNamedItem("type").getNodeValue();
						if("QDM Datatype".equals(type)){
							type = child.getAttributes().getNamedItem("qdmDataType").getNodeValue();
							type = "\"" + type + "\"";
						}else if("Others".equals(type)){
							type = child.getAttributes().getNamedItem("otherType").getNodeValue();
						}
						String argName = child.getAttributes().getNamedItem("argumentName").getNodeValue();
						signature += argName + " " + type + ", ";
					}
				}
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(signature.length() > 0){
			signature = signature.trim();
			if(signature.endsWith(",")){
				signature = signature.substring(0, signature.length() - 1);
			}
			signature = "(" + signature + ")";
		}else{
			signature = "()";
		}
		
		return signature;
	}

	private static List<String> getExpressionLineList(CQLModel cqlModel, SaveUpdateCQLResult cqlResult, XmlProcessor simpleXMLProcessor, String cqlName, String cqlType, String aggFuncLabel){
		
		List<String> lineList = new ArrayList<String>();
			
		if(cqlType.equals(CQLFUNCTION) || cqlType.equals(CQLDEFINITION)){
			lineList.add("");
		}	
		
		String logic = getLogicStringFromXML(cqlName, cqlType, simpleXMLProcessor);
		
		if(aggFuncLabel != null){
			logic = logic.replace("\n", "\n\t\t");
			logic = aggFuncLabel +  " (" + System.lineSeparator() + "\t\t" +  logic + System.lineSeparator() + "\t )";
		}
				
		lineList.add(logic);
	
		return lineList;
	}

	private static String getLogicStringFromXML(String cqlName, String cqlType,
			XmlProcessor simpleXMLProcessor) {
		
		String logic = "";
		String xPath = "//cqlLookUp//";
		
		if(cqlType.equals(CQLDEFINITION)){
			xPath += "definition[@name='" + cqlName + "']/logic";
		}else if(cqlType.equals(CQLFUNCTION)){
			xPath += "function[@name='" + cqlName + "']/logic";
		}
		
		 try {
			Node logicNode = simpleXMLProcessor.findNode(simpleXMLProcessor.getOriginalDoc(), xPath);
			if(logicNode != null){
				logic = logicNode.getTextContent();
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		 
		return logic;
	}
	
	/**
	 * Gets the span element with class.
	 *
	 * @param subLiElement the sub li element
	 * @param cssClassName the css class name
	 * @return the span element with class
	 */
	private static Element getPreElementWithClass(Element subLiElement,
			String cssClassName) {
		Element spanElem = subLiElement.appendElement("pre");
		spanElem.attr("class", cssClassName);
		return spanElem;
	}

	/**
	 * Creates the base html document.
	 *
	 * @param title the title
	 * @return the org.jsoup.nodes. document
	 */
	private static org.jsoup.nodes.Document createBaseHTMLDocument(String title) {
		org.jsoup.nodes.Document htmlDocument = new org.jsoup.nodes.Document("");

		// Must be added first for proper formating and styling
		DocumentType doc = new DocumentType("html",
				"-//W3C//DTD HTML 4.01 Transitional//EN",
				"http://www.w3.org/TR/html4/loose.dtd", "");
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

	/**
	 * Append style node.
	 *
	 * @param head the head
	 */
	private static void appendStyleNode(Element head) {
		String styleTagString = MATCssCQLUtil.getCSS();
		head.append(styleTagString);
	}
	
	/**
	 * Populate cql objects list.
	 *
	 * @param cqlModel the cql file object
	 */
	private static void populateCQLObjectsList(CQLModel cqlModel) {
		
		List<CQLDefinition> cqlDefinition = cqlModel.getDefinitionList();
		for(CQLDefinition cqlDef:cqlDefinition){
			cqlObjects.add(cqlDef.getDefinitionName());
		}
		
		List<CQLFunctions> cqlFunctions = cqlModel.getCqlFunctions();
		for(CQLFunctions cqlFunc:cqlFunctions){
			cqlObjects.add(cqlFunc.getFunctionName());
		}
	}

}
