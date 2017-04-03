package mat.server.simplexml.cql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.xpath.XPathExpressionException;

import mat.model.cql.parser.CQLBaseStatementInterface;
import mat.model.cql.parser.CQLCodeModelObject;
import mat.model.cql.parser.CQLDefinitionModelObject;
import mat.model.cql.parser.CQLFileObject;
import mat.model.cql.parser.CQLFunctionModelObject;
import mat.model.cql.parser.CQLFunctionModelObject.FunctionArgument;
import mat.model.cql.parser.CQLParameterModelObject;
import mat.model.cql.parser.CQLValueSetModelObject;
import mat.server.simplexml.HeaderHumanReadableGenerator;
import mat.server.util.XmlProcessor;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.emory.mathcs.backport.java.util.Collections;

// TODO: Auto-generated Javadoc
/**
 * The Class CQLHumanReadableHTMLCreator.
 */
public class CQLHumanReadableHTMLCreator {
	
	
	/** The Constant keyWordListArray. */
	private static final String[] keyWordListArray = 		{"-", "!", "(", ")", "*", ",", ".", "/", ":", "[", "]", "^", "{", "}", "~", "+", "<", "=", ">",
															 "after", "all", "and", "as", "asc", "ascending", "before", "between", "begins", "begun", 
															 "between", "Boolean", "by", "called", "case", "cast", "Code", "codesystem", "codesystems", "collapse", 
															 "Concept", "contains", "context", "convert", "date", "DateTime", "day", "days", "Decimal", "defeault", 
															 "define", "desc", "descending", "difference", "display", "distinct", "div", "duration", "during", 
															 "else", "end", "ends", "except", "exists", "false", "flatten", "from", "function", "hour", "hours", 
															 "if", "in", "include", "included in", "includes", "Integer", "intersect", "Interval", "is", 
															 "less", "let", "library", "List", "maximum", "meets", "millisecond", "milliseconds", "minimum", 
															 "minute", "minutes", "mod", "month", "months", "more", "not", "null", "occurs", "of", "or", "or after",
															 "or before", "or less", "or more", "overlaps", "parameter", "predecessor", "predecessor of", "private", 
															 "properly", "properly included in", "properly includes", "public", "QDM", "Quantity", "return", "same", 
															 "second", "seconds", "singleton", "singleton from", "sort", "sort by", "start", "starts", "String", "successor", 
															 "such", "such that", "Sum", "that", "then", "Time", "time", "timezone", "to", "true", "Tuple", "union", 
															 "using", "valueset", "version", "week", "weeks", "when", "where", "width", "with", "within", "without", "xor", 
															 "year", "years"};

	/** The Constant cqlFunctionsListArray. */
	private static final String[] cqlFunctionsListArray = 	{"Abs", "AgeInDays", "AgeInDaysAt", "AgeInHours", "AgeInHoursAt",
															"AgeInMinutes", "AgeInMinutesAt", "AgeInMonths", "AgeInMonthsAt", 
															"AgeInSeconds", "AgeInSecondsAt", "AgeInYears", "AgeInYearsAt",
															"AllTrue", "AnyTrue", "Avg", "CalculateAgeInDays", "CalculateAgeInDaysAt", 
															"CalculateAgeInHours", "CalculateAgeInHoursAt", "CalculateAgeInMinutes", "CalculateAgeInMinutesAt", 
															"CalcualteAgeInMonths", "CalculateAgeInMonthsAt", "CalculateAgeInSeconds", "CalculateAgeInSecondsAt", 
															"CalculateAgeInYears", "CalculateAgeInYearsAt", "Ceiling", "Coalesce", 
															"Count", "DateTime", "Exp", "First", "Floor", "IndexOf", "Last", "Length", 
															"Ln", "Log", "Max", "Median", "Min", "Mode", "Now", "PopulationStdDev", "PopulationVariance", 
															"Round", "StdDev", "Sum", "Time", "TimeOfDay", "Today", "Truncate", "Variance"};
	
	
	/** complete timing list */
//	private static final String[] cqlTimingListArray   =	{"after", "after end", "after start", "before", "before end", "before start", "during", "ends", "ends after",
//															 "ends after end", "ends after start", "ends before", "ends before end", "ends before start", "ends during", 
//															 "ends properly during", "ends properly within", "ends properly within end", "ends properly within start", 
//															 "ends same as", "ends same as end", "ends same as start", "ends same or after", "ends same or after end", 
//															 "ends same or after start", "ends same or before", "ends same or before end", "ends same or before start", 
//															 "ends within", "ends within end", "ends within start", "included in", "includes", "includes end", "includes start", 
//															 "meets", "meets after", "meets before", "overlaps", "overlaps after", "overlaps before", "properly during", 
//															 "properly included in", "properly includes", "properly includes end", "properly includes start", "properly within", 
//															 "properly within end", "properly within start", "same as", "same as end", "same as satrt", "same or after", 
//															 "same or after end", "same or after start", "same or before", "same or before end", "same or before start", 
//															 "starts during", "starts properly during", "starts properly within", "starts properly within end", "starts properly within start",
//															 "starts same as", "starts same as end", "starts same as start", "starts same or after", "starts same or after end", 
//															 "starts same or after start", "starts same or before", "starts same or before end", "starts same or before start", "starts within",
//															 "starts within end", "starts within start", "within"};
	
	
	private static final String[] cqlAttributeListArray = 	{"activeDatetime", "admissionSource", "anatomicalApproachSite", "anatomicalLocationSite", "authorDatetime", "authorTime", "birthDatetime", 
															 "cause", "code", "diagnosis", "dischargeDisposition", "dosage", "expiredDatetime", "facilityLocation", "frequency", "id", 
															 "incisionDatetime", "lengthOfStay", "locationPeriod", "method", "negationRationale", "ordinality", "prevalencePeriod", 
															 "principalDiagnosis", "radiationDosage", "radiationDuration", "reason", "recorder", "referenceRange", "refills", "relatedTo", 
															 "relationship", "relevantPeriod", "reporter", "result", "resultDatetime", "route", "severity", "status", "supply", "targetOutcome",
															 "type"};
	
	
	/** The definitions or functions already displayed. */
	private static List<String> definitionsOrFunctionsAlreadyDisplayed = new ArrayList<String>();
	
	/** The cql objects. */
	private static List<String> cqlObjects = new ArrayList<String>();
		
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
	 * @param cqlFileObject the cql file object
	 * @return the string
	 */
	public static String generateCQLHumanReadableForMeasure(
			String simpleXmlStr, CQLFileObject cqlFileObject) {
		
		definitionsOrFunctionsAlreadyDisplayed.clear();
		cqlObjects.clear();
		
		populateCQLObjectsList(cqlFileObject);
		
		String humanReadableHTML = "";
		
		try {
			
			org.jsoup.nodes.Document humanReadableHTMLDocument = HeaderHumanReadableGenerator
					.generateHeaderHTMLForCQLMeasure(simpleXmlStr);
			
			humanReadableHTML = humanReadableHTMLDocument.toString();
			XmlProcessor simpleXMLProcessor = new XmlProcessor(simpleXmlStr);
			generateHumanReadable(humanReadableHTMLDocument, simpleXMLProcessor, cqlFileObject);
			humanReadableHTML = humanReadableHTMLDocument.toString();
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return humanReadableHTML;
	}
	
	/**
	 * Generate human readable.
	 *
	 * @param humanReadableHTMLDocument the human readable html document
	 * @param simpleXMLProcessor the simple xml processor
	 * @param cqlFileObject the cql file object
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static void generateHumanReadable(
			Document humanReadableHTMLDocument, XmlProcessor simpleXMLProcessor, CQLFileObject cqlFileObject)
					throws XPathExpressionException {
		
		generateTableOfContents(humanReadableHTMLDocument, simpleXMLProcessor);
		generatePopulationCriteriaHumanReadable(humanReadableHTMLDocument,
				simpleXMLProcessor, cqlFileObject);
		generateQDMDataElements(humanReadableHTMLDocument, simpleXMLProcessor, cqlFileObject); 
//		generateQDMVariables(humanReadableHTMLDocument, simpleXMLProcessor);
//		generateDataCriteria(humanReadableHTMLDocument, simpleXMLProcessor);
		generateSupplementalData(humanReadableHTMLDocument, simpleXMLProcessor);
		generateRiskAdjustmentVariables(humanReadableHTMLDocument, simpleXMLProcessor, cqlFileObject);
		HeaderHumanReadableGenerator.addMeasureSet(simpleXMLProcessor,
				humanReadableHTMLDocument);
	}
	
	private static void generateRiskAdjustmentVariables(
			Document humanReadableHTMLDocument, XmlProcessor simpleXMLProcessor, CQLFileObject cqlFileObject)
					throws XPathExpressionException {
		definitionsOrFunctionsAlreadyDisplayed.clear();
		Element bodyElement = humanReadableHTMLDocument.body();
		bodyElement
		.append("<h3><a name=\"d1e879\" href=\"#toc\">Risk Adjustment Variables</a></h3>");
		
		Element mainDivElement = bodyElement.appendElement("div");
		Element mainListElement = mainDivElement.appendElement(HTML_UL);
		
		NodeList elements = simpleXMLProcessor.findNodeList(
				simpleXMLProcessor.getOriginalDoc(),
				"/measure/riskAdjustmentVariables/cqldefinition");
		
		if (elements.getLength() > 0) {
			for(int i=0;i<elements.getLength();i++){
				Node childNode = elements.item(i);
				String uuid = childNode.getAttributes().getNamedItem("uuid").getNodeValue();
				String xpathforSubTree = "/measure/cqlLookUp//definition[@id='"+ uuid +"']";
				Node defineNode = simpleXMLProcessor.findNode(simpleXMLProcessor.getOriginalDoc(),
						xpathforSubTree);
				String defineNodeName = defineNode.getAttributes().getNamedItem("name").getNodeValue();
				defineNodeName = "\"" + defineNodeName + "\"";
				generatePopulationCriteria(mainListElement, cqlFileObject, childNode, defineNodeName, defineNodeName);
				
			}
		} else {
			mainListElement.appendElement(HTML_LI).appendText("None");
		}
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
		
		Element populationCriteriaLI = tocULElement.appendElement(HTML_LI);
		populationCriteriaLI
		.append("<a href=\"#d1e405\">Population Criteria</a>");
		
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
	
	/**
	 * Generate population criteria human readable.
	 *
	 * @param humanReadableHTMLDocument the human readable html document
	 * @param simpleXMLProcessor the simple xml processor
	 * @param cqlFileObject the cql file object
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static void generatePopulationCriteriaHumanReadable(
			Document humanReadableHTMLDocument, XmlProcessor simpleXMLProcessor, CQLFileObject cqlFileObject)
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
	
		for (Integer key : groupMap.keySet()) {
			if (groupMap.size() > 1) {
				mainListElement.append("<li style=\"list-style: none;\"><br><b>------ Population Criteria "
						+ (key.toString()) + " ------</b><br><br></li>");
			}
			NodeList clauseNodeList = groupMap.get(key).getChildNodes();
			generatePopulationNodes(clauseNodeList, mainListElement,
					groupNodeList.getLength(),key, simpleXMLProcessor, cqlFileObject);
		}
	}
	
	/**
	 * Generate the qdm elements in the Human Readable
	 * 
	 * @param humanReadableHTMLDocument the human readable html document
	 * @param simpleXMLProcessor the simple xml processor
	 * @param cqlFileObject 
	 */
	private static void generateQDMDataElements(Document humanReadableHTMLDocument, XmlProcessor simpleXMLProcessor, CQLFileObject cqlFileObject) {
		Element bodyElement = humanReadableHTMLDocument.body();
		bodyElement.append("<h3><a name=\"d1e647\" href=\"#toc\">Data Criteria (QDM Data Elements)</a></h3>");
		
		Element qdmElementUL = bodyElement.appendElement(HTML_UL);
		
		try {
			
			NodeList qdmElementList = simpleXMLProcessor.findNodeList(simpleXMLProcessor.getOriginalDoc(), 
														"/measure/elementLookUp/qdm[@suppDataElement='false']");
			
			if(qdmElementList.getLength() < 1) {
				String output = "None"; 
				Element qdmElementLI = qdmElementUL.appendElement(HTML_LI);   
				qdmElementLI.append(output);
			}
			
			else {
				ArrayList<String> qdmElementStringList = new ArrayList<String>(); 				

				// make the output string from the qdm node information and add it to the string list.
				for(int i = 0; i < qdmElementList.getLength(); i++) {
					
					String dataTypeName = qdmElementList.item(i).getAttributes().getNamedItem("datatype").getNodeValue(); 
					if("attribute".equals(dataTypeName)){
						dataTypeName = "Attribute";
					}
					//End Comment
					String name = qdmElementList.item(i).getAttributes().getNamedItem("name").getNodeValue(); 
					String oid = qdmElementList.item(i).getAttributes().getNamedItem("oid").getNodeValue(); 
					String taxonomy = qdmElementList.item(i).getAttributes().getNamedItem("taxonomy").getNodeValue(); 
					
					String output = String.format("\"%s: %s\" using \"%s %s Value Set (%s)\"", dataTypeName, name, name, taxonomy, oid); 
								
					qdmElementStringList.add(output); 
				}
				
				// sort and append the qdm elements
				Collections.sort(qdmElementStringList);
				for(int i = 0; i < qdmElementStringList.size(); i++) {
					Element qdmElemtentLI = qdmElementUL.appendElement(HTML_LI);
					qdmElemtentLI.append(qdmElementStringList.get(i));
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
	 * @param cqlFileObject the cql file object
	 */
	private static void generatePopulationNodes(NodeList clauseNodeList,
			Element mainListElement, int totalGroupCount,
			int currentGroupNumber, XmlProcessor simpleXMLProcessor, CQLFileObject cqlFileObject) {
		
		for (String element : POPULATION_NAME_ARRAY) {
			generatePopulationNodes(element, clauseNodeList,
					mainListElement, totalGroupCount, currentGroupNumber,
					simpleXMLProcessor, cqlFileObject);
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
	 * @param cqlFileObject the cql file object
	 */
	private static void generatePopulationNodes(String populationType,
			NodeList clauseNodeList, Element mainListElement,
			int totalGroupCount, int currentGroupNumber,
			XmlProcessor simpleXMLProcessor, CQLFileObject cqlFileObject) {
		
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
			//Element boldNameElement = populationListElement.appendElement("b");
		//	String populationName = getPopulationName(populationType, true);
			
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
				
//				if(!childPopulationName.startsWith("Initial Population")){
//					populationListElement.appendText(childPopulationName
//							+ (popassoc.length() > 0 ? popassoc : "")
//							+ (itemCountText.length() > 0 ? itemCountText : ""));
//							//+ " =");
//				}
				
				childPopulationName += (popassoc.length() > 0 ? popassoc : "") + (itemCountText.length() > 0 ? itemCountText : "");
				
				/*if(childPopulationName.startsWith("Measure Observation")){
					populationListElement = populationListElement.appendElement(HTML_UL);
				}*/
				parseAndBuildHTML(simpleXMLProcessor,
						populationListElement, clauseNode, c + 1, cqlFileObject, childPopulationName);
				
			}
		} else if (clauseNodes.size() == 1) {
			Element populationListElement = mainListElement
					.appendElement(HTML_LI);
			populationListElement.attr("class", "list-unstyled");
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
					clauseNodes.get(0), 1, cqlFileObject, populationName);
			
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
	 * @param cqlFileObject the cql file object
	 * @param PopulationDisplayName the population display name
	 */
	private static void parseAndBuildHTML(
			XmlProcessor populationOrSubtreeXMLProcessor,
			Element populationOrSubtreeListElement, Node clauseNode,
			int currentGroupNumber, CQLFileObject cqlFileObject, String PopulationDisplayName) {
		
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
			generatePopulationCriteria(populationOrSubtreeListElement, cqlFileObject, cqlNode, parentName, PopulationDisplayName);
			
		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Generate supplemental data.
	 *
	 * @param humanReadableHTMLDocument the human readable html document
	 * @param simpleXMLProcessor the simple xml processor
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static void generateSupplementalData(
			Document humanReadableHTMLDocument, XmlProcessor simpleXMLProcessor)
					throws XPathExpressionException {
		Element bodyElement = humanReadableHTMLDocument.body();
		bodyElement
		.append("<h3><a name=\"d1e767\" href=\"#toc\">Supplemental Data Elements</a></h3>");
		
		Element mainDivElement = bodyElement.appendElement("div");
		Element mainListElement = mainDivElement.appendElement(HTML_UL);
		
		NodeList supplementalQdmElementList = simpleXMLProcessor.findNodeList(simpleXMLProcessor.getOriginalDoc(), 
				"/measure/elementLookUp/qdm[@suppDataElement='true']");
				
		if(supplementalQdmElementList != null && supplementalQdmElementList.getLength()>0){
			ArrayList<String> suppQdmStringList = new ArrayList<String>(); 
			
			// make output string from sde node and add it to the string list
			for(int i = 0; i < supplementalQdmElementList.getLength(); i++) {
				Node suppNode = supplementalQdmElementList.item(i);
				NamedNodeMap suppNodeAttribMap = suppNode.getAttributes();
				String output = "\"" + suppNodeAttribMap.getNamedItem("datatype").getNodeValue() + ": "
						+ suppNodeAttribMap.getNamedItem("name").getNodeValue()
						+ "\" using \""
						+ suppNodeAttribMap.getNamedItem("name").getNodeValue() + " "
						+ suppNodeAttribMap.getNamedItem("taxonomy").getNodeValue()
						+ " Value Set ("
						+ suppNodeAttribMap.getNamedItem("oid").getNodeValue() + ")\"";
				suppQdmStringList.add(output);			
			}
			
			// sort and append sde string
			Collections.sort(suppQdmStringList);
			for(int i = 0; i < suppQdmStringList.size(); i++) {
				Element listItem = mainListElement.appendElement(HTML_LI);
				listItem.append(suppQdmStringList.get(i));
			}
			
		}
		else{
			mainListElement.appendElement(HTML_LI).appendText("None");
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

	/**
	 * Generate cql human readable for single population.
	 *
	 * @param populationNode the population node
	 * @param cqlFileObject the cql file object
	 * @return the string
	 */
	public static String generateCQLHumanReadableForSinglePopulation(
			Node populationNode, CQLFileObject cqlFileObject) {

		definitionsOrFunctionsAlreadyDisplayed.clear();
		cqlObjects.clear();

		populateCQLObjectsList(cqlFileObject);

		Node cqlNode = populationNode.getFirstChild();
		String populationName = populationNode.getAttributes()
				.getNamedItem("displayName").getNodeValue();

		org.jsoup.nodes.Document htmlDocument = null;
		htmlDocument = createBaseHTMLDocument("Human Readable for "
				+ populationName);

		Element bodyElement = htmlDocument.body();

		generatePopulationCriteria(bodyElement, cqlFileObject, cqlNode,
				populationName, populationName);

		return htmlDocument.html();
	}

	/**
	 * Generate population criteria.
	 *
	 * @param bodyElement the body element
	 * @param cqlFileObject the cql file object
	 * @param cqlNode the cql node
	 * @param populationName the population name
	 * @param populationDisplayName the population display name
	 */
	private static void generatePopulationCriteria(Element bodyElement,
			CQLFileObject cqlFileObject, Node cqlNode, String populationName, String populationDisplayName) {

		/*
		 * bodyElement.append(
		 * "<p id=\"nn-text\">Non-normative Example - Only CQL Logic.</p>");
		 * bodyElement.append(
		 * "<div><span id=\"d1e521\" class=\"section\">Population Criteria</span>\r\n"
		 * +
		 * "      <a href=\"#toc\" style=\"margin-top:-10px;\">Table of Contents</a></div>"
		 * );
		 */
		
		Element mainDivElement = bodyElement.appendElement("div");
		mainDivElement.attr("class", "treeview hover p-l-10");

		Element mainULElement = mainDivElement.appendElement("ul");
		mainULElement.attr("class", "list-unstyled");

		String cqlNodeType = cqlNode.getNodeName();
		String cqlName = cqlNode.getAttributes().getNamedItem("displayName").getNodeValue();
				
		System.out.println("Generating Human readable for:" + cqlNodeType + ":"
				+ cqlName);

		if ("cqldefinition".equals(cqlNodeType)) {
			cqlName = "\"" + cqlName + "\"";

			generateHTMLForPopulation(mainULElement, cqlFileObject.getDefinitionsMap().get(cqlName),
					populationName, populationDisplayName, cqlName);
		} else if ("cqlfunction".equals(cqlNodeType)){
			cqlName = "\"" + cqlName + "\"";
			System.out.println("Functions map:"+cqlFileObject.getFunctionsMap());
			generateHTMLForPopulation(mainULElement, cqlFileObject.getFunctionsMap().get(cqlName),
					populationName, populationDisplayName, cqlName);
		} else if("cqlaggfunction".equals(cqlNodeType)){
			CQLAggregateFunction cqlAggregateFunction = new CQLHumanReadableHTMLCreator().new CQLAggregateFunction();
			cqlAggregateFunction.setIdentifier(cqlName + " of:");
			
			Node childCQLNode = cqlNode.getChildNodes().item(0);
			String childCQLName = childCQLNode.getAttributes().getNamedItem("displayName").getNodeValue();
			childCQLName = "\"" + childCQLName + "\"";
			CQLFunctionModelObject cqlFunctionModelObject = cqlFileObject.getFunctionsMap().get(childCQLName);
			
			cqlAggregateFunction.getReferredToFunctions().add(cqlFunctionModelObject);
			
			generateHTMLForPopulation(mainULElement, cqlAggregateFunction, populationName, populationDisplayName, cqlName);
		} else {
			generateHTMLForPopulation(mainULElement, populationName, populationDisplayName, "");
		}

	}
	
	
	/**
	 * Generate html for population.
	 *
	 * @param mainElement the main element
	 * @param populationName the population name
	 * @param populationDisplayName the population display name
	 * @param mainDefinitionName the main definition name
	 */
	private static void generateHTMLForPopulation(Element mainElement,String populationName, String populationDisplayName, 
			String mainDefinitionName) {
		// create a base LI element
		Element mainliElement = mainElement.appendElement("li");
		mainliElement.attr("class", "list-unstyled");

		Element checkBoxElement = mainliElement.appendElement("input");
		checkBoxElement.attr("type", "checkbox");
		String id = "test-" + populationName + "_"
				+ (int) (Math.random() * 1000);
		checkBoxElement.attr("id", id);

		/*if (definitionsOrFunctionsAlreadyDisplayed.contains(populationDisplayName)) {
			checkBoxElement.attr("checked", "");
		} else {
			definitionsOrFunctionsAlreadyDisplayed.add(populationDisplayName);
		}*/

		Element definitionLabelElement = mainliElement.appendElement("label");
		definitionLabelElement.attr("for", id);
		definitionLabelElement.attr("class", "list-header");

		Element strongElement = definitionLabelElement.appendElement("strong");
		strongElement.appendText(populationDisplayName);
		
		definitionLabelElement.appendText(" (click to expand/collapse)");
		System.out.println(mainDefinitionName);
		/*Element ulElement = definitionLabelElement.appendElement(HTML_UL);
		ulElement.appendElement(HTML_LI).appendText("None");*/
		generateHTMLToDsiplayNone(mainliElement);
		//generateHTMLForDefinitionOrFunction(cqlBaseStatementObject, mainliElement, true);
	}

	/**
	 * Generate html for population.
	 *
	 * @param mainElement the main element
	 * @param cqlBaseStatementObject the cql base statement object
	 * @param populationName the population name
	 * @param populationDisplayName the population display name
	 * @param mainDefinitionName the main definition name
	 */
	private static void generateHTMLForPopulation(Element mainElement,
			CQLBaseStatementInterface cqlBaseStatementObject, String populationName, String populationDisplayName, 
			String mainDefinitionName) {
		// create a base LI element
		Element mainliElement = mainElement.appendElement("li");
		mainliElement.attr("class", "list-unstyled");

		Element checkBoxElement = mainliElement.appendElement("input");
		checkBoxElement.attr("type", "checkbox");
		String id = "test-" + populationName + "_"
				+ (int) (Math.random() * 1000);
		checkBoxElement.attr("id", id);

		/*if (definitionsOrFunctionsAlreadyDisplayed.contains(populationDisplayName)) {
			checkBoxElement.attr("checked", "");
		} else {
			definitionsOrFunctionsAlreadyDisplayed.add(populationDisplayName);
		}*/

		Element definitionLabelElement = mainliElement.appendElement("label");
		definitionLabelElement.attr("for", id);
		definitionLabelElement.attr("class", "list-header");

		Element strongElement = definitionLabelElement.appendElement("strong");
		strongElement.appendText(populationDisplayName);
		
		definitionLabelElement.appendText(" (click to expand/collapse)");
		System.out.println("Main Defintion Name: " + mainDefinitionName);
		generateHTMLForDefinitionOrFunction(cqlBaseStatementObject, mainliElement, true);
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
		Element noneLabelElement = divElement.appendElement("label");
		noneLabelElement.attr("for", id);
		Element strongElement = noneLabelElement.appendElement("strong");
		strongElement.attr("class","cql-class");
		strongElement.appendText("None");
	}
	
	
	
	/**
	 * Generate html for definition or function.
	 *
	 * @param cqlBaseStatementObject the cql base statement object
	 * @param mainElement the main element
	 * @param isTopDefinition the is top definition
	 */
	private static void generateHTMLForDefinitionOrFunction(
			CQLBaseStatementInterface cqlBaseStatementObject,
			Element mainElement, boolean isTopDefinition) {

		String statementIdentifier = cqlBaseStatementObject.getIdentifier();
		
		//If this cqlBaseStatementObject is a function, then we need to consider 
		//arguments to the function.
		String statementSignature = statementIdentifier;
		
		List<FunctionArgument> argumentList = cqlBaseStatementObject.getArguments();
		if(argumentList != null && argumentList.size() > 0){
			statementSignature += "(";
		}
		for(FunctionArgument functionArgument:argumentList){
			statementSignature += functionArgument.getArgumentName() + " " + functionArgument.getArgumentType() + ", ";
		}
		
		if(argumentList != null && argumentList.size() > 0){
			if(statementSignature.endsWith(", ")){
				statementSignature = statementSignature.substring(0, statementSignature.length()-2);
			}
			statementSignature += ")";
		}

		Element mainULElement = mainElement;
		if (isTopDefinition) {
			mainULElement = mainElement.appendElement("ul");
			mainULElement.attr("class", "code");
		}

		Element mainliElement = mainULElement;
		// create a base LI element
		if (isTopDefinition) {
			mainliElement = mainULElement.appendElement("li");
		}

		Element mainDivElement = mainliElement.appendElement("div");
		mainDivElement.attr("class", "treeview hover p-l-10");

		Element checkBoxElement = mainDivElement.appendElement("input");
		checkBoxElement.attr("type", "checkbox");
		String id = "test-" + statementIdentifier + "_"
				+ (int) (Math.random() * 1000);
		checkBoxElement.attr("id", id);

		if (definitionsOrFunctionsAlreadyDisplayed.contains(statementIdentifier)) {
			checkBoxElement.attr("checked", "");
		} else {
			definitionsOrFunctionsAlreadyDisplayed.add(statementIdentifier);
		}

		Element defnOrFuncLabelElement = mainDivElement.appendElement("label");
		defnOrFuncLabelElement.attr("for", id);
		defnOrFuncLabelElement.attr("class", "list-header");

		Element strongElement = defnOrFuncLabelElement.appendElement("strong");
		strongElement.appendText(statementIdentifier);
				
		defnOrFuncLabelElement.appendText(" (click to expand/collapse)");

		Element subULElement = mainDivElement.appendElement("ul");
		Element subLiElement = subULElement.appendElement("li");
		Element subDivElement = subLiElement.appendElement("div");
		
		if(!(cqlBaseStatementObject instanceof CQLAggregateFunction)){
			
			Element spanElem = getSpanElementWithClass(subDivElement, "cql_keyword");
			Element spanElemDefName = getSpanElementWithClass(subDivElement,
					"cql-class");
			if(cqlBaseStatementObject instanceof CQLFunctionModelObject){
				spanElem.appendText("define function ");
				spanElemDefName.appendText(statementSignature + ":");
			}else if(cqlBaseStatementObject instanceof CQLDefinitionModelObject) {
				spanElem.appendText("define ");
				spanElemDefName.appendText(statementSignature + ":");
			} else {
				spanElem.appendText("parameter ");
				spanElemDefName.appendText(statementSignature);
			}			
			
			List<String> codeLineList = getDefnOrFuncLineList(cqlBaseStatementObject);
			
			if(codeLineList.size() > 0){
				subDivElement.append("&nbsp;" + codeLineList.get(0));
		
				subDivElement.appendElement("br");
		
				for (int i = 1; i < codeLineList.size(); i++) {
					Element spanElemDefBody = getSpanElementWithClass(subDivElement,
							"cql-definition-body");
					spanElemDefBody.append(codeLineList.get(i));
				}
			}
			subDivElement.appendElement("br");
		}
		

		List<CQLDefinitionModelObject> referredToDefinitionsModelObjectList = cqlBaseStatementObject
				.getReferredToDefinitions();
		
		for(CQLDefinitionModelObject referredTDefinitionModelObject : referredToDefinitionsModelObjectList){
			generateHTMLForDefinitionOrFunction(referredTDefinitionModelObject, subDivElement, false);
		}
		
		List<CQLFunctionModelObject> referredToFunctionsModelObjectList = cqlBaseStatementObject.getReferredToFunctions();
		
		for(CQLFunctionModelObject referredToFunctionModelObject : referredToFunctionsModelObjectList){
			generateHTMLForDefinitionOrFunction(referredToFunctionModelObject, subDivElement, false);
		}
		
        List<CQLParameterModelObject> referredToParameterModelObjectList = cqlBaseStatementObject.getReferredToParameters();
		
		for(CQLParameterModelObject referredToParameterModelObject : referredToParameterModelObjectList){
			generateHTMLForDefinitionOrFunction(referredToParameterModelObject, subDivElement, false);
		}
		
	}

	/**
	 * This method will go through definition body and try to format it in
	 * series of lines for easier reading.
	 *
	 * @param cqlBaseStatementObject the cql base statement object
	 * @return the defn or func line list
	 */
	private static List<String> getDefnOrFuncLineList(
			CQLBaseStatementInterface cqlBaseStatementObject) {

		List<String> lineList = new ArrayList<String>();
		
		if(cqlBaseStatementObject instanceof CQLFunctionModelObject){
			lineList.add("");
		}
		
		int tokenCounter = 0;

		List<String> childTokens = cqlBaseStatementObject.getChildTokens();
		
		if(childTokens.size() == 0){
			return new ArrayList<String>();
		}

		// find the first line
		if (childTokens.get(0).trim().equals("["))// Try to check for something
													// like "["Encounter,
													// Performed": "Ambulatory/ED
													// Visit"] E"
		{
			String tokenString = "[";
			// look further until you find ']'
			for (tokenCounter = 1; tokenCounter < childTokens.size(); tokenCounter++) {
				if (childTokens.get(tokenCounter).equals("]")) {
					if (childTokens.size() > (tokenCounter + 1)) {
						String nextToken = childTokens.get(tokenCounter + 1)
								.trim();
						if (nextToken.length() == 1
								&& Character.isLetter(nextToken.charAt(0))) {
							lineList.add(tokenString + "] "
									+ nextToken);
							tokenCounter += 2;
						} else { // check for something like " ["Diagnosis,
									// Active": "Acute
									// Pharyngitis"] union ["Diagnosis,
									// Active": "Acute Tonsillitis"]"
							lineList.add(" ");
							tokenCounter = 0;
						}
					} else {// check for something like " ["Diagnosis,
							// Active": "Acute Pharyngitis"] union ["Diagnosis,
							// Active": "Acute Tonsillitis"]"
						lineList.add(" ");
						lineList.add(tokenString + "] ");
						tokenCounter += 1;
					}
					break;
				} else {
					tokenString += " "
							+ wrapWithCssClass(childTokens.get(tokenCounter));
				}
			}
		} else if (childTokens.size() > 1
				&& childTokens.get(1).trim().length() == 1
				&& Character.isLetter(childTokens.get(1).trim().charAt(0)))// check
																			// for
																			// something
																			// like
																			// "MeasurementPeriodEncounters E"
		{
			tokenCounter = 2;
			lineList.add(childTokens.get(0) + " "
					+ wrapWithCssClass(childTokens.get(1)));
		}

		String tokenString = "";
		List<String> breakAtKeywords = new ArrayList<String>();
		breakAtKeywords.add("where");
		breakAtKeywords.add("with");
		breakAtKeywords.add("and");
		// breakAtKeywords.add("such that");

		for (; tokenCounter < childTokens.size(); tokenCounter++) {
			if (breakAtKeywords.contains(childTokens.get(tokenCounter).trim()
					.toLowerCase())
					&& tokenString.length() > 0) {
				lineList.add(tokenString + " "
						+ wrapWithCssClass(childTokens.get(tokenCounter)));
				tokenString = "";
			} else {
				String fillerSpace = " ";

				List<String> noSpaceTokens = new ArrayList<String>();
				noSpaceTokens.add(".");
				noSpaceTokens.add("(");
				// noSpaceTokens.add(")");
				// noSpaceTokens.add("[");
				// noSpaceTokens.add("]");
				String token = childTokens.get(tokenCounter);
				// String lastToken = tokenString.length() > 0 ?
				// tokenString.charAt(tokenString.length()-1)+"" : "";

				String lastToken = (tokenCounter > 0) ? childTokens
						.get(tokenCounter - 1) : "";
				
				System.out.println("token:"+token);
				System.out.println("lastToken:"+lastToken);
				
				if (noSpaceTokens.contains(token)
						|| noSpaceTokens.contains(lastToken)) {
					fillerSpace = "";
				}
				System.out.println("filler:"+fillerSpace+"!");
				tokenString += fillerSpace
						+ wrapWithCssClass(childTokens.get(tokenCounter));
				System.out.println("tokenString:" + tokenString);
				System.out.println();
			}
		}

		if (tokenString.length() > 0) {
			lineList.add(tokenString);
		}

		return lineList;
	}

	/**
	 * Wrap with css class.
	 *
	 * @param string the string
	 * @return the string
	 */
	private static String wrapWithCssClass(String string) {

		String cssClass = "";
		if (string.trim().startsWith("\"") && string.endsWith("\"")) {
			cssClass = "cql_string";
		} else if (string.trim().length() == 1) {
			cssClass = "cql_identifier";
		} else if (contains(keyWordListArray, string.trim())) {
			cssClass = "cql_keyword";
		} else if (contains(cqlFunctionsListArray, string.trim())) {
			cssClass = "cql_function";
		} else if(contains(cqlAttributeListArray, string.trim())) {
			cssClass = "cql_attribute"; 
		} else if (cqlObjects.contains(string)) {
			cssClass = "cql-object";
		}

		string = "<span class=\"" + cssClass + "\">" + string + "</span>";
		return string;
	}

	/**
	 * Contains.
	 *
	 * @param stringArray the string array
	 * @param tokenString the token string
	 * @return true, if successful
	 */
	private static boolean contains(String[] stringArray, String tokenString) {

		for (int i = 0; i < stringArray.length; i++) {
			if (tokenString.equals(stringArray[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the span element with class.
	 *
	 * @param subLiElement the sub li element
	 * @param cssClassName the css class name
	 * @return the span element with class
	 */
	private static Element getSpanElementWithClass(Element subLiElement,
			String cssClassName) {
		Element spanElem = subLiElement.appendElement("span");
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
	 * @param cqlFileObject the cql file object
	 */
	private static void populateCQLObjectsList(CQLFileObject cqlFileObject) {
		
		Map<String, CQLDefinitionModelObject> cqlDefinitionMap = cqlFileObject.getDefinitionsMap();
		cqlObjects.addAll(cqlDefinitionMap.keySet());
		
		Map<String, CQLFunctionModelObject> cqlFunctionsMap = cqlFileObject.getFunctionsMap();
		cqlObjects.addAll(cqlFunctionsMap.keySet());
	}
	
	/**
	 * The Class CQLAggregateFunction.
	 */
	class CQLAggregateFunction implements CQLBaseStatementInterface {

		/** The identifier. */
		private String identifier = "";
		
		/** The referred to functions. */
		private List<CQLFunctionModelObject> referredToFunctions = new ArrayList<CQLFunctionModelObject>();
		
		/* (non-Javadoc)
		 * @see mat.model.cql.parser.CQLBaseStatementInterface#getIdentifier()
		 */
		@Override
		public String getIdentifier() {
			return identifier;
		}
		
		/**
		 * Sets the identifier.
		 *
		 * @param identifierName the new identifier
		 */
		public void setIdentifier(String identifierName) {
			identifier = identifierName;
		}

		/* (non-Javadoc)
		 * @see mat.model.cql.parser.CQLBaseStatementInterface#getChildTokens()
		 */
		@Override
		public List<String> getChildTokens() {
			return new ArrayList<String>();
		}

		/* (non-Javadoc)
		 * @see mat.model.cql.parser.CQLBaseStatementInterface#getArguments()
		 */
		@Override
		public List<FunctionArgument> getArguments() {
			return new ArrayList<FunctionArgument>();
		}

		/* (non-Javadoc)
		 * @see mat.model.cql.parser.CQLBaseStatementInterface#getReferredToDefinitions()
		 */
		@Override
		public List<CQLDefinitionModelObject> getReferredToDefinitions() {
			return new ArrayList<CQLDefinitionModelObject>();
		}

		/* (non-Javadoc)
		 * @see mat.model.cql.parser.CQLBaseStatementInterface#getReferredByDefinitions()
		 */
		@Override
		public List<CQLDefinitionModelObject> getReferredByDefinitions() {
			return new ArrayList<CQLDefinitionModelObject>();
		}

		/* (non-Javadoc)
		 * @see mat.model.cql.parser.CQLBaseStatementInterface#getReferredToFunctions()
		 */
		@Override
		public List<CQLFunctionModelObject> getReferredToFunctions() {
			return referredToFunctions;
		}
		
		/* (non-Javadoc)
		 * @see mat.model.cql.parser.CQLBaseStatementInterface#getReferredByFunctions()
		 */
		@Override
		public List<CQLFunctionModelObject> getReferredByFunctions() {
			return new ArrayList<CQLFunctionModelObject>();
		}

		/* (non-Javadoc)
		 * @see mat.model.cql.parser.CQLBaseStatementInterface#getReferredToParameters()
		 */
		@Override
		public List<CQLParameterModelObject> getReferredToParameters() {
			return new ArrayList<CQLParameterModelObject>();
		}

		/* (non-Javadoc)
		 * @see mat.model.cql.parser.CQLBaseStatementInterface#getReferredByParameters()
		 */
		@Override
		public List<CQLParameterModelObject> getReferredByParameters() {
			return new ArrayList<CQLParameterModelObject>();
		}

		/* (non-Javadoc)
		 * @see mat.model.cql.parser.CQLBaseStatementInterface#getReferredToValueSets()
		 */
		@Override
		public List<CQLValueSetModelObject> getReferredToValueSets() {
			return new ArrayList<CQLValueSetModelObject>();
		}

		/* (non-Javadoc)
		 * @see mat.model.cql.parser.CQLBaseStatementInterface#getReferredByValueSets()
		 */
		@Override
		public List<CQLValueSetModelObject> getReferredByValueSets() {
			return new ArrayList<CQLValueSetModelObject>();
		}

		@Override
		public List<CQLCodeModelObject> getReferredToCodes() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
