package mat.server.simplexml.cql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.xml.xpath.XPathExpressionException;

import mat.model.cql.parser.CQLBaseStatementInterface;
import mat.model.cql.parser.CQLDefinitionModelObject;
import mat.model.cql.parser.CQLFileObject;
import mat.model.cql.parser.CQLFunctionModelObject;
import mat.model.cql.parser.CQLFunctionModelObject.FunctionArgument;
import mat.model.cql.parser.CQLParameterModelObject;
import mat.model.cql.parser.CQLValueSetModelObject;
import mat.server.simplexml.HeaderHumanReadableGenerator;
import mat.server.util.XmlProcessor;

import org.apache.commons.lang.StringUtils;
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
	
	/** The Constant keyWordListArray. */
	private static final String[] keyWordListArray = {"library","version","using","include","called","public","private",
		"parameter","default","codesystem","valueset","codesystems","define",
		"function","with","without","in","from","where","return",
		"all","distinct","sort","by","asc","desc","is","not","cast","as","between",
		"difference","contains","and","or","xor","union","intersection","year","month",
		"day","hour","minute","second","millisecond","when","then","or","or less", 
		"before","after","or more","more","less","context","using", "QDM","Interval",
		"DateTime","Patient","Population","such that"};

	/** The Constant cqlFunctionsListArray. */
	private static final String[] cqlFunctionsListArray = {"date","time","timezone","starts","ends",
			"occurs","overlaps","Interval",
			"Tuple","List","DateTime","AgeInYearsAt"};
	
	/** The definitions or functions already displayed. */
	private static List<String> definitionsOrFunctionsAlreadyDisplayed = new ArrayList<String>();
	
	/** The cql objects. */
	private static List<String> cqlObjects = new ArrayList<String>();
	
	/** The Constant ELEMENT_LOOK_UP. */
	private static final String ELEMENT_LOOK_UP = "elementLookUp";
	
	/** The Constant FUNCTIONAL_OP. */
	private static final String FUNCTIONAL_OP = "functionalOp";
	
	/** The Constant DISPLAY_NAME. */
	private static final String DISPLAY_NAME = "displayName";
	
	/** The Constant ELEMENT_REF. */
	private static final String ELEMENT_REF = "elementRef";
	
	/** The Constant HTML_LI. */
	private static final String HTML_LI = "li";
	
	/** The Constant HTML_UL. */
	private static final String HTML_UL = "ul";
	
	/** The Constant SET_OP. */
	private static final String SET_OP = "setOp";
	
	/** The Constant COMMENT. */
	private static final String COMMENT = "comment";
	
	/** The Constant LOGICAL_OP. */
	private static final String LOGICAL_OP = "logicalOp";
	
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
		generateQDMDataElements(humanReadableHTMLDocument, simpleXMLProcessor); 
//		generateQDMVariables(humanReadableHTMLDocument, simpleXMLProcessor);
//		generateDataCriteria(humanReadableHTMLDocument, simpleXMLProcessor);
		generateSupplementalData(humanReadableHTMLDocument, simpleXMLProcessor);
//		generateRiskAdjustmentVariables(humanReadableHTMLDocument, simpleXMLProcessor);
		HeaderHumanReadableGenerator.addMeasureSet(simpleXMLProcessor,
				humanReadableHTMLDocument);
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
	 */
	private static void generateQDMDataElements(Document humanReadableHTMLDocument, XmlProcessor simpleXMLProcessor) {
		Element bodyElement = humanReadableHTMLDocument.body();
		bodyElement.append("<h3><a name=\"d1e647\" href=\"#toc\">Data Criteria (QDM Data Elements)</a></h3>");
		
		Element qdmElementUL = bodyElement.appendElement(HTML_UL);
		
		try {
			
			NodeList qdmElementList = simpleXMLProcessor.findNodeList(simpleXMLProcessor.getOriginalDoc(), 
														"/measure/cqlLookUp/valuesets/valueset[@suppDataElement='false']");
			if(qdmElementList.getLength() < 1) {
				String output = "None"; 
				Element qdmElementLI = qdmElementUL.appendElement(HTML_LI);   
				qdmElementLI.append(output);
			}
			
			else {
				for(int i = 0; i < qdmElementList.getLength(); i++) {
					String dataTypeName = qdmElementList.item(i).getAttributes().getNamedItem("datatype").getNodeValue(); 
					String name = qdmElementList.item(i).getAttributes().getNamedItem("name").getNodeValue(); 
					String oid = qdmElementList.item(i).getAttributes().getNamedItem("oid").getNodeValue(); 
					String taxonomy = qdmElementList.item(i).getAttributes().getNamedItem("taxonomy").getNodeValue(); 
					
					String output = String.format("\"%s: %s\" using \"%s %s Value Set (%s)\"", dataTypeName, name, name, taxonomy, oid); 
					Element qdmElementLI = qdmElementUL.appendElement(HTML_LI);   
					qdmElementLI.append(output);	
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
				
				if(!childPopulationName.startsWith("Initial Population")){
					populationListElement.appendText(childPopulationName
							+ (popassoc.length() > 0 ? popassoc : "")
							+ (itemCountText.length() > 0 ? itemCountText : "")
							+ " =");
				}
				
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
			if ("measureObservation".equalsIgnoreCase(node.getAttributes()
					.getNamedItem("type").getNodeValue())) {
				Node nodeAssoc = node.getAttributes().getNamedItem(
						"associatedPopulationUUID");
				if (nodeAssoc != null) {
					
					Node newAssoc = processor.findNode(
							processor.getOriginalDoc(), "//clause[@uuid=\""
									+ nodeAssoc.getNodeValue() + "\"]");
					if (newAssoc != null) {
						String name = getPopulationName(newAssoc
								.getAttributes().getNamedItem("type")
								.getNodeValue());
						stringAssoc = "    (Association: " + name + ")";
					}
				}
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stringAssoc;
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
			NodeList childNodes = clauseNode.getChildNodes();
			String scoring = populationOrSubtreeXMLProcessor.findNode(
					populationOrSubtreeXMLProcessor.getOriginalDoc(),
					"//measureDetails/scoring").getTextContent();
			String parentName = "";
			if (clauseNode.getAttributes().getNamedItem("type") != null) {
				parentName = clauseNode.getAttributes().getNamedItem("type")
						.getNodeValue();
			}
			/*if (childNodes.getLength() == 0) {
				if("measureObservation".equalsIgnoreCase(parentName)){
					displayNone(
							populationOrSubtreeListElement,
							populationOrSubtreeXMLProcessor, clauseNode);
				}else{
					displayNone(
							populationOrSubtreeListElement.appendElement(HTML_UL),
							populationOrSubtreeXMLProcessor, clauseNode);
				}
			}*/
			/*String parentName = "";*/
			/*if (clauseNode.getAttributes().getNamedItem("type") != null) {
				parentName = clauseNode.getAttributes().getNamedItem("type")
						.getNodeValue();
				if (("denominator".equalsIgnoreCase(parentName)
						|| "measurePopulation".equalsIgnoreCase(parentName) || ("numerator"
								.equalsIgnoreCase(parentName) && "ratio"
								.equalsIgnoreCase(scoring)))
								&& !"measureDetails".equalsIgnoreCase(clauseNode
										.getNodeName())) {
					
					displayInitialPop(populationOrSubtreeListElement,
							populationOrSubtreeXMLProcessor, clauseNode,
							currentGroupNumber);
				}
			}*/
			//for (int i = 0; i < childNodes.getLength(); i++) {
				generatePopulationCriteria(populationOrSubtreeListElement, cqlFileObject, clauseNode, parentName, PopulationDisplayName);
			//}
		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
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
		
		NodeList supplementalDefnNodes = simpleXMLProcessor.findNodeList(simpleXMLProcessor.getOriginalDoc(), 
				"/measure/cqlLookUp/definitions/definition[@supplDataElement='true']");
		Map<String, Node> qdmNodeMap = new HashMap<String, Node>();
		if(supplementalDefnNodes != null){
			Map<String, String> dataType = new HashMap<String, String>();
			for(int i=0;i<supplementalDefnNodes.getLength();i++){
			    NodeList logicElement = simpleXMLProcessor.findNodeList(
						simpleXMLProcessor.getOriginalDoc(),
						"/measure/cqlLookUp/definitions/definition/logic");
			    if(logicElement != null){
					for (int j = 0; j < logicElement.getLength(); j++) {
						Node nodeLogic = logicElement.item(j);
						if(nodeLogic.hasChildNodes()){
							NodeList defLogicMap = nodeLogic.getChildNodes();
							if(defLogicMap.getLength() > 0){
								String defLogic = defLogicMap.item(0).getNodeValue();
								//The replaceAll method cannot match the String literal [] which does not exist within the String alone so try replacing these items separately.
								String result = defLogic.replaceAll("\"","").replaceAll("\\[", "").replaceAll("\\]","");
								String[] pairs = result.split(":");
								dataType.put(pairs[0].trim(), pairs[1].trim());
							}
						}
					}
				}
			    for(Entry<String, String> entry : dataType.entrySet()){
			    	Node qdm = simpleXMLProcessor.findNode(
							simpleXMLProcessor.getOriginalDoc(),
							"/measure/cqlLookUp/valuesets/valueset[@datatype='" + entry.getKey() + "']");
					NamedNodeMap qdmMap = qdm.getAttributes();
					qdmNodeMap.put(qdmMap.getNamedItem("datatype").getNodeValue()
							+ ": " + qdmMap.getNamedItem("name").getNodeValue(),
							qdm); 
			    }
			    
			}
			List<String> qdmNameList = new ArrayList<String>(qdmNodeMap.keySet());
			Collections.sort(qdmNameList, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o1.compareToIgnoreCase(o2);
				}
			});
			
			for (String s : qdmNameList) {
				Node qdm = qdmNodeMap.get(s);
				NamedNodeMap qdmMap = qdm.getAttributes();
				Element listItem = mainListElement.appendElement(HTML_LI);
				
				listItem.appendText("\""
						+ qdmMap.getNamedItem("datatype").getNodeValue() + ": "
						+ qdmMap.getNamedItem("name").getNodeValue()
						+ "\" using \""
						+ qdmMap.getNamedItem("name").getNodeValue() + " "
						+ qdmMap.getNamedItem("taxonomy").getNodeValue()
						+ " Value Set ("
						+ qdmMap.getNamedItem("oid").getNodeValue() + ")\"");
			}
		}
		else {
			mainListElement.appendElement(HTML_LI).appendText("None");
		}
	}
	
	
	/**
	 * Parses the child.
	 *
	 * @param item the item
	 * @param parentListElement the parent list element
	 * @param parentNode the parent node
	 * @param populationOrSubtreeXMLProcessor the population or subtree xml processor
	 */
	private static void parseChild(Node item, Element parentListElement,
			Node parentNode, XmlProcessor populationOrSubtreeXMLProcessor) {
		
		String nodeName = item.getNodeName();
	
		if (LOGICAL_OP.equals(nodeName)) {
			String nodeDisplayName = item.getAttributes()
					.getNamedItem(DISPLAY_NAME).getNodeValue().toUpperCase();
			String parentNodeDisplayName = parentNode.getAttributes()
					.getNamedItem(DISPLAY_NAME).getNodeValue().toUpperCase();
			// set the Flag if we have AND -> AND NOT or OR -> OR NOT
			boolean isNestedNot = (nodeDisplayName.equals(parentNodeDisplayName
					+ " NOT"));
			
			if (LOGICAL_OP.equals(parentNode.getNodeName())) {
				if (LOGICAL_OP.equals(parentNode.getNodeName())) {
					if (isNestedNot) {
						// liElement.appendText(nodeDisplayName+":");
					} else {
						parentListElement = parentListElement
								.appendElement(HTML_LI);
						parentListElement.appendText(parentNodeDisplayName + ":");
					}
				}
			}
			Element ulElement = null;
			if (isNestedNot) {
				ulElement = parentListElement;
			} else {
				if(parentListElement.nodeName().equals(HTML_UL) && parentListElement.parent().html().contains("Measure Observation")){
					ulElement = parentListElement;
				}else{
					ulElement = parentListElement.appendElement(HTML_UL);
				}
			}
			NodeList childNodes = item.getChildNodes();
			if (childNodes.getLength() == 0) {
				boolean isNoneAdded = displayNone(ulElement, populationOrSubtreeXMLProcessor,
						parentNode);
				if(!isNoneAdded){
					if(ulElement.childNodeSize() == 0){
						ulElement.remove();
					}
				}
				// ulElement.appendElement(HTML_LI).appendText("None");
			}else if((childNodes.getLength() == 1) && childNodes.item(0).getNodeName().equals(COMMENT)){
				Node commentNode = childNodes.item(0);
				String commentValue = commentNode.getTextContent();
				if ((commentValue == null) || (commentValue.trim().length() == 0)) {
					boolean isNoneAdded = displayNone(ulElement, populationOrSubtreeXMLProcessor,
							parentNode);
					if(!isNoneAdded){
						if(ulElement.childNodeSize() == 0){
							ulElement.remove();
						}
					}
				}
			}
			for (int i = 0; i < childNodes.getLength(); i++) {
				if (!isEmptyComment(childNodes.item(i))) {
					parseChild(childNodes.item(i), ulElement, item,
						populationOrSubtreeXMLProcessor);
				}
			}
		} else if (COMMENT.equals(nodeName)) {
			
			//System.out.println("comment value:" + commentValue);
			if (!isEmptyComment(item)) {
				Element liElement = parentListElement.appendElement(HTML_LI);
				liElement.attr("style", "list-style-type: none");
				Element italicElement = liElement.appendElement("i");
				italicElement.appendText("# " + item.getTextContent());
			} else {
				// it is an empty comment
  				if ((item.getParentNode().getChildNodes().getLength() == 1)
						&& "AND".equalsIgnoreCase(item.getParentNode()
								.getAttributes().getNamedItem("displayName")
								.getNodeValue())) {
					displayNone(parentListElement, populationOrSubtreeXMLProcessor,
							parentNode);
				}
			}
			return;
		} else if (SET_OP.equals(nodeName)) {
			// Element liElement = parentListElement.appendElement(HTML_LI);
			if (LOGICAL_OP.equals(parentNode.getNodeName())) {
				parentListElement = parentListElement.appendElement(HTML_LI);
				if (LOGICAL_OP.equals(parentNode.getNodeName())) {
					parentListElement = parentListElement.appendText(getNodeText(parentNode,
							populationOrSubtreeXMLProcessor));
					// parentListElement = liElement.appendElement(HTML_UL);
				}
			}
			// Element ulElement = liElement.appendElement(HTML_UL);
			if (parentListElement.nodeName().equals(HTML_UL)) {
				parentListElement = parentListElement.appendElement(HTML_LI);
				parentListElement.appendText(
						getNodeText(item, populationOrSubtreeXMLProcessor));
			} else {
				if (parentListElement.html().contains("Stratification")) {
					parentListElement = parentListElement
							.appendElement(HTML_UL)
							.appendElement(HTML_LI)
							.appendText(
									getNodeText(item,
											populationOrSubtreeXMLProcessor));
				} else {
					parentListElement.appendText(getNodeText(item,
							populationOrSubtreeXMLProcessor));
				}
			}
			Element ulElement = parentListElement.appendElement(HTML_UL);
			NodeList childNodes = item.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				parseChild(childNodes.item(i), ulElement, item,
						populationOrSubtreeXMLProcessor);
			}
		} else if (ELEMENT_REF.equals(nodeName)) {
			
			if (LOGICAL_OP.equals(parentNode.getNodeName())
					|| SET_OP.equals(parentNode.getNodeName())) {
				Element liElement = parentListElement
						.appendElement(HTML_LI);
			
				if (LOGICAL_OP.equals(parentNode.getNodeName())) {
					liElement.appendText(getNodeText(parentNode,
							populationOrSubtreeXMLProcessor)
							+ getNodeText(item,
									populationOrSubtreeXMLProcessor));
				} else {
					liElement.appendText(getNodeText(item,
							populationOrSubtreeXMLProcessor));
				}
			} else {
				if(parentListElement.html().contains(
						"Stratification")) {
					parentListElement = parentListElement.appendElement(
							HTML_UL).appendElement(HTML_LI);
				}
				String parentNodeName = parentListElement.nodeName();
				if("ul".equals(parentNodeName)){
					parentListElement.appendElement(HTML_LI).appendText(getNodeText(item,
							populationOrSubtreeXMLProcessor));
				}else{
					parentListElement.appendText(getNodeText(item,
							populationOrSubtreeXMLProcessor));
				}
			}
			
		} else if (ELEMENT_LOOK_UP.equals(nodeName)
				|| "itemCount".equals(nodeName)
				|| "measureDetails".equals(nodeName)) {
			// ignore
		} else {
			Element liElement = parentListElement.appendElement(HTML_LI);
			
			if (LOGICAL_OP.equals(parentNode.getNodeName())) {
				// liElement.appendText(" "+getNodeText(parentNode,
				// populationOrSubtreeXMLProcessor));
				if (LOGICAL_OP.equals(parentNode.getNodeName())) {
					liElement.appendText(" "
							+ getNodeText(parentNode,
									populationOrSubtreeXMLProcessor));
				}
			}
			liElement.appendText(item.getAttributes()
					.getNamedItem(DISPLAY_NAME).getNodeValue()
					+ " ");
		}
		
		//System.out.println("End of parseChild - Node Name: " + nodeName);
		//System.out.println("End of parseChild - Parent List Element: " + parentListElement);
		
	}
	
	/**
	 * This method is used to get the correct text to add to human readable
	 * depending on the type of node.
	 *
	 * @param node the node
	 * @param populationOrSubtreeXMLProcessor the population or subtree xml processor
	 * @return the node text
	 */
	private static String getNodeText(Node node,
			XmlProcessor populationOrSubtreeXMLProcessor) {
		String nodeName = node.getNodeName();
		String name = "";
		if (LOGICAL_OP.equals(nodeName)) {
			name = node.getAttributes().getNamedItem(DISPLAY_NAME)
					.getNodeValue().toUpperCase();
			name += ": ";
		} else if (SET_OP.equals(nodeName)) {
			name = node.getAttributes().getNamedItem(DISPLAY_NAME)
					.getNodeValue().toLowerCase();
			name = StringUtils.capitalize(name);
			name += " of: ";
		} else if (ELEMENT_REF.equals(nodeName)) {
			name = node.getAttributes().getNamedItem(DISPLAY_NAME)
					.getNodeValue();
			if (name.endsWith(" : Timing Element")) {
				name = name.substring(0, name.indexOf(" : Timing Element"));
			} else if (!name.endsWith(" : Patient Characteristic Birthdate")
					&& !name.endsWith(" : Patient Characteristic Expired")) {
				String[] nameArr = name.split(":");
				
				if (nameArr.length == 2) {
					name = nameArr[1].trim() + ": " + nameArr[0].trim();
				}
				if (nameArr.length == 3) {
					name = nameArr[1].trim() + ": " + nameArr[2].trim() + ": "
							+ nameArr[0].trim();
				}
				// Move Occurrence of to the beginning of the name
				if (name.contains("Occurrence") && name.contains("of")) {
					int occurLoc = name.indexOf("Occurrence", 0);
					int ofLoc = name.indexOf("of", occurLoc);
					String occur = name.substring(occurLoc, ofLoc + 2);
					name = name.replaceAll(occur, "");
					occur += " ";
					name = occur.concat(name);
				}
			}
			
			name = "\"" + name + "\" ";
		}
		return name;
	}
	
	/**
	 * Checks if is empty comment.
	 *
	 * @param item the item
	 * @return true, if is empty comment
	 */
	private static boolean isEmptyComment(Node item) {
		boolean isEmptyComment = false;
		if(COMMENT.equals(item.getNodeName())){
			String commentValue = item.getTextContent();
			if ((commentValue == null)) {
				isEmptyComment = true;
			} else if (commentValue.trim().length() == 0) {
				isEmptyComment = true;
			}
		}
		return isEmptyComment;
	}
	
	/**
	 * Display initial pop.
	 *
	 * @param populationOrSubtreeListElement the population or subtree list element
	 * @param populationOrSubtreeXMLProcessor the population or subtree xml processor
	 * @param clause the clause
	 * @param loop the loop
	 */
	private static void displayInitialPop(
			Element populationOrSubtreeListElement,
			XmlProcessor populationOrSubtreeXMLProcessor, Node clause, int loop) {
		
		Element listStart = populationOrSubtreeListElement
				.appendElement(HTML_UL);
		Element list = listStart.appendElement(HTML_LI);
		try {
			Node assocPop = clause.getAttributes().getNamedItem(
					"associatedPopulationUUID");
			if (assocPop != null) {
				Node display = populationOrSubtreeXMLProcessor.findNode(
						populationOrSubtreeXMLProcessor.getOriginalDoc(),
						"//measure//measureGrouping//group/clause[@uuid = \""
								+ assocPop.getNodeValue() + "\"]");
				String name = display.getAttributes()
						.getNamedItem("displayName").getNodeValue();
				String number = initialPopulationHash.get(display
						.getAttributes().getNamedItem("uuid").getNodeValue());
				String lastNum = name.substring(name.length() - 1);
				if (!("-1".equals(number))) {
					name = name.replace(lastNum, number);
				} else {
					name = (name.substring(0, name.length() - 1)).trim();
				}
				list.appendText("AND: " + name);
			} else {
				list.appendText("AND: Initial Population");
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Display none.
	 *
	 * @param list the list
	 * @param populationOrSubtreeXMLProcessor the population or subtree xml processor
	 * @param parentNode the parent node
	 * @return true, if successful
	 */
	private static boolean displayNone(Element list,
			XmlProcessor populationOrSubtreeXMLProcessor, Node parentNode) {
		boolean retValue = false;
		try {
			// if the population displays "AND:Initial Population" then don't put a NONE
			String scoring = populationOrSubtreeXMLProcessor.findNode(
					populationOrSubtreeXMLProcessor.getOriginalDoc(),
					"//measureDetails/scoring").getTextContent();
			String type = parentNode.getAttributes().getNamedItem("type")
					.getNodeValue();
			if (("proportion".equalsIgnoreCase(scoring) && !"denominator"
					.equalsIgnoreCase(type))
					|| ("ratio".equalsIgnoreCase(scoring)
							&& !"denominator".equalsIgnoreCase(type) && !"numerator"
							.equalsIgnoreCase(type))
							|| ("continuous Variable".equalsIgnoreCase(scoring) && !"measurePopulation"
									.equalsIgnoreCase(type))
									|| ("cohort".equalsIgnoreCase(scoring))) {
				
				list.appendElement(HTML_LI).appendText("None");
				retValue = true;
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retValue;
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
	 * @param populationType the population type
	 * @param addPlural the add plural
	 * @return the population name
	 */
	private static String getPopulationName(String populationType,
			boolean addPlural) {
		String name = getPopulationName(populationType);
		if (addPlural
				&& (!name.endsWith("Exclusions") && !name
						.endsWith("Exceptions"))) {
			name += "s ";
		}
		return name;
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
		
		if(cqlNode.hasChildNodes()){
			cqlNode = cqlNode.getFirstChild();
		}

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

		if (definitionsOrFunctionsAlreadyDisplayed.contains(populationDisplayName)) {
			checkBoxElement.attr("checked", "");
		} else {
			definitionsOrFunctionsAlreadyDisplayed.add(populationDisplayName);
		}

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

		if (definitionsOrFunctionsAlreadyDisplayed.contains(populationDisplayName)) {
			checkBoxElement.attr("checked", "");
		} else {
			definitionsOrFunctionsAlreadyDisplayed.add(populationDisplayName);
		}

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
			if(cqlBaseStatementObject instanceof CQLFunctionModelObject){
				spanElem.appendText("define function ");
			}else {
				spanElem.appendText("define ");
			}
	
			Element spanElemDefName = getSpanElementWithClass(subDivElement,
					"cql-class");
			spanElemDefName.appendText(statementSignature + ":");
			
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
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see mat.model.cql.parser.CQLBaseStatementInterface#getReferredByParameters()
		 */
		@Override
		public List<CQLParameterModelObject> getReferredByParameters() {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see mat.model.cql.parser.CQLBaseStatementInterface#getReferredToValueSets()
		 */
		@Override
		public List<CQLValueSetModelObject> getReferredToValueSets() {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see mat.model.cql.parser.CQLBaseStatementInterface#getReferredByValueSets()
		 */
		@Override
		public List<CQLValueSetModelObject> getReferredByValueSets() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
