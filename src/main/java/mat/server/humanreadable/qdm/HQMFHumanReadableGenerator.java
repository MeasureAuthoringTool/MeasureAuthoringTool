package mat.server.humanreadable.qdm;

import mat.server.humanreadable.MATCssUtil;
import mat.server.util.XmlProcessor;
import mat.shared.ConstantMessages;
import mat.shared.MatConstants;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

/**
 * This class is used for generating Human Readable documents prior to CQL. It should not be modified. 
 */
@Deprecated
public class HQMFHumanReadableGenerator implements MatConstants{
	
	/** The Constant ELEMENT_LOOK_UP. */
	private static final String ELEMENT_LOOK_UP = "elementLookUp";
	
	/** The Constant FUNCTIONAL_OP. */
	private static final String FUNCTIONAL_OP = "functionalOp";
	
	/** The Constant DISPLAY_NAME. */
	private static final String DISPLAY_NAME = "displayName";
	
	/** The Constant ELEMENT_REF. */
	private static final String ELEMENT_REF = "elementRef";
	
	/** The Constant RELATIONAL_OP. */
	private static final String RELATIONAL_OP = "relationalOp";
	
	/** The Constant HTML_LI. */
	private static final String HTML_LI = "li";
	
	/** The Constant HTML_UL. */
	private static final String HTML_UL = "ul";
	
	/** The Constant SET_OP. */
	private static final String SET_OP = "setOp";
	
	/** The Constant SUB_TREE. */
	private static final String SUB_TREE = "subTree";
	
	/** The Constant COMMENT. */
	private static final String COMMENT = "comment";
	
	/** The Constant LOGICAL_OP. */
	private static final String LOGICAL_OP = "logicalOp";
	
	/** The Constant popNameArray. */
	private static final String[] POPULATION_NAME_ARRAY = {"initialPopulation",
		"denominator", "denominatorExclusions", "numerator",
		"numeratorExclusions", "denominatorExceptions",
		"measurePopulation", "measurePopulationExclusions", "stratum" ,
		"measureObservation"};
	
	/** The Constant subsetFunctions */
	private static List<String> subSetFunctionsList = new ArrayList<String>();
	static {
		subSetFunctionsList.add(MatConstants.FIRST);
		subSetFunctionsList.add(MatConstants.SECOND);
		subSetFunctionsList.add(MatConstants.THIRD);
		subSetFunctionsList.add(MatConstants.FOURTH);
		subSetFunctionsList.add(MatConstants.FIFTH);
		subSetFunctionsList.add(MatConstants.MOST_RECENT);
	}
	
	/** The show only variable name. */
	private static Boolean showOnlyVariableName = false;
	
	/** The lhs id. */
	private static Stack<String> lhsID;
	
	/** The initial population hash. */
	private static Map<String, String> initialPopulationHash = new HashMap<String, String>();
	
	/**
	 * Generate html for measure.
	 *
	 * @param measureId the measure id
	 * @param simpleXmlStr the simple xml str
	 * @return the string
	 */
	public static String generateHTMLForMeasure(String measureId,
			String simpleXmlStr) {
		String humanReadableHTML = "";
		lhsID = new Stack<String>();
		try {
			org.jsoup.nodes.Document humanReadableHTMLDocument = HeaderHumanReadableGenerator
					.generateHeaderHTMLForMeasure(simpleXmlStr);
			XmlProcessor simpleXMLProcessor = resolveSubTreesInPopulations(simpleXmlStr);
			if (simpleXMLProcessor == null) {
				org.jsoup.nodes.Document htmlDocument = createBaseHumanReadableDocument();
				Element bodyElement = htmlDocument.body();
				Element mainDivElement = bodyElement.appendElement("div");
				Element mainListElement = mainDivElement.appendElement(HTML_UL);
				Element populationListElement = mainListElement
						.appendElement(HTML_LI);
				populationListElement
				.appendText("Human readable encountered problems. "
						+ "Most likely you have included a clause within clause which is causing an infinite loop.");
				return htmlDocument.toString();
			}
			resolveRemainingSubTreeRefs(simpleXMLProcessor);
			
			generateHumanReadable(humanReadableHTMLDocument, simpleXMLProcessor);
			humanReadableHTML = humanReadableHTMLDocument.toString();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return humanReadableHTML;
	}
	
	public static String generateHTMLForPopulationOrSubtree(String measureId,
			String subXML, String measureXML) {
		
		org.jsoup.nodes.Document htmlDocument = null;
		// replace the <subTree> tags in 'populationSubXML' with the appropriate
		// subTree tags from 'simpleXML'.
		try {
			XmlProcessor populationOrSubtreeXMLProcessor = expandSubTreesAndImportQDMs(
					subXML, measureXML, true);
			
			lhsID = new Stack<String>();
			if (populationOrSubtreeXMLProcessor == null) {
				htmlDocument = createBaseHumanReadableDocument();
				Element bodyElement = htmlDocument.body();
				Element mainDivElement = bodyElement.appendElement("div");
				Element mainListElement = mainDivElement.appendElement(HTML_UL);
				Element populationListElement = mainListElement
						.appendElement(HTML_LI);
				populationListElement
				.appendText("Human readable encountered problems. "
						+ "Most likely you have included a clause with clause which is causing an infinite loop.");
				return htmlDocument.toString();
			}
			
			boolean isPopulation = checkIfPopulation(populationOrSubtreeXMLProcessor);
			String name = getPopulationOrSubtreeName(
					populationOrSubtreeXMLProcessor, isPopulation);
			
			htmlDocument = createBaseHumanReadableDocument();
			Element bodyElement = htmlDocument.body();
			Element mainDivElement = bodyElement.appendElement("div");
			Element mainListElement = mainDivElement.appendElement(HTML_UL);
			Element populationOrSubtreeListElement = mainListElement
					.appendElement(HTML_LI);
			
			Element boldNameElement = populationOrSubtreeListElement
					.appendElement("b");
			boldNameElement.appendText(name + " =");
			
			if (!isPopulation) {
				populationOrSubtreeListElement.appendElement("br");
				populationOrSubtreeListElement = populationOrSubtreeListElement
						.appendElement(HTML_UL).appendElement(HTML_LI);
			}
			
			parseAndBuildHTML(populationOrSubtreeXMLProcessor,
					populationOrSubtreeListElement);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		String returnHTML = "";
		if (htmlDocument != null) {
			returnHTML = htmlDocument.toString();
		}
		return returnHTML;
	}
	
	private static boolean checkIfPopulation(
			XmlProcessor populationOrSubtreeXMLProcessor) {
		boolean returnFlag = false;
		Node node = populationOrSubtreeXMLProcessor.getOriginalDoc()
				.getFirstChild();
		String nodeName = node.getNodeName();
		if ("clause".equals(nodeName)) {
			returnFlag = true;
		}
		return returnFlag;
	}
	
	/**
	 * This method will look for <subTree> tags within <subTreeLookUp> tag. For each <subTree> with
	 * an "instanceOf" attribute, we need to fetch the corrosponding <subTree> and copy its children.
	 *
	 * @param measureXMLProcessor the measure xml processor
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static void modifySubTreeLookUpForOccurances(XmlProcessor measureXMLProcessor) throws XPathExpressionException{
		NodeList qdmVariableSubTreeList = measureXMLProcessor.findNodeList(measureXMLProcessor.getOriginalDoc(), "/measure/subTreeLookUp/subTree[@instanceOf]");
		
		for(int i=0;i<qdmVariableSubTreeList.getLength();i++){
			Node qdmVariableNode = qdmVariableSubTreeList.item(i);
			
			String occuranceLetter = qdmVariableNode.getAttributes().getNamedItem("instance").getNodeValue();
			String displayName = qdmVariableNode.getAttributes().getNamedItem("displayName").getNodeValue();
			displayName = "Occurrence "+occuranceLetter + " of $" + StringUtils.deleteWhitespace(displayName);
			
			qdmVariableNode.getAttributes().getNamedItem("displayName").setNodeValue(displayName);
			
			String referencedUUID = qdmVariableNode.getAttributes().getNamedItem("instanceOf").getNodeValue();
			Node referencedSubTreeNode = measureXMLProcessor.findNode(measureXMLProcessor.getOriginalDoc(), "/measure/subTreeLookUp/subTree[not(@instanceOf)][@uuid='"+referencedUUID+"']");
			Node mainChild = referencedSubTreeNode.getChildNodes().item(0);
			Node mainChildClone = mainChild.cloneNode(true);
			
			qdmVariableNode.appendChild(mainChildClone);
		}
	}
	
	/**
	 * Expand sub trees and import qd ms.
	 *
	 * @param subXML the sub xml
	 * @param measureXML the measure xml
	 * @param isImportElementLookup the is import element lookup
	 * @return the xml processor
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static XmlProcessor expandSubTreesAndImportQDMs(String subXML,
			String measureXML, boolean isImportElementLookup)
					throws XPathExpressionException {
		
		XmlProcessor populationOrSubtreeXMLProcessor = new XmlProcessor(subXML);
		XmlProcessor measureXMLProcessor = new XmlProcessor(measureXML);
		modifySubTreeLookUpForOccurances(measureXMLProcessor);
		
		return expandSubTreesAndImportQDMs(populationOrSubtreeXMLProcessor,
				measureXMLProcessor, isImportElementLookup);
	}
	
	/**
	 * Expand sub trees and import qd ms.
	 *
	 * @param populationOrSubtreeXMLProcessor the population or subtree xml processor
	 * @param measureXMLProcessor the measure xml processor
	 * @param isImportElementLookup the is import element lookup
	 * @return the xml processor
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static XmlProcessor expandSubTreesAndImportQDMs(
			XmlProcessor populationOrSubtreeXMLProcessor,
			XmlProcessor measureXMLProcessor, boolean isImportElementLookup)
					throws XPathExpressionException {
		// find all <subTreeRef> tags in 'populationSubXML'
		NodeList subTreeRefNodeList = populationOrSubtreeXMLProcessor
				.findNodeList(populationOrSubtreeXMLProcessor.getOriginalDoc(),
						"//subTreeRef");
		
		if (subTreeRefNodeList.getLength() > 0) {
			
			// For each <subTreeRef> node replace it by actual <subTree> node
			// from 'simpleXML'
			for (int i = 0; i < subTreeRefNodeList.getLength(); i++) {
				Node subTreeRefNode = subTreeRefNodeList.item(i);
				Node firstChildNode = subTreeRefNode.getFirstChild();
				Node commentNode = null;
				if ((firstChildNode != null)
						&& "comment".equals(firstChildNode.getNodeName())) {
					commentNode = firstChildNode.cloneNode(true);
				}
				String subTreeId = subTreeRefNode.getAttributes()
						.getNamedItem("id").getNodeValue();
				//				System.out.println("subTreeId:" + subTreeId);
				
				Node subTreeNode = resolveMainSubTreeNode(measureXMLProcessor,
						subTreeId);
				
				if (subTreeNode != null) {
					replaceSubTreeNode(populationOrSubtreeXMLProcessor,
							subTreeRefNode, commentNode, subTreeNode);
				} else {
					return null;
				}
			}
		}
		if (isImportElementLookup) {
			// import <elementLookUp> tag to populationOrSubtreeXMLProcessor
			Node elementLookUpNode = measureXMLProcessor.findNode(
					measureXMLProcessor.getOriginalDoc(), "//elementLookUp");
			Node importedElementLookUpNode = populationOrSubtreeXMLProcessor
					.getOriginalDoc().importNode(elementLookUpNode, true);
			populationOrSubtreeXMLProcessor.getOriginalDoc().getFirstChild()
			.appendChild(importedElementLookUpNode);
		}
		
		// import 'measureDetails'
		Node measureDetailsNode = measureXMLProcessor.findNode(
				measureXMLProcessor.getOriginalDoc(), "//measureDetails");
		Node importedMeasureDetailsNode = populationOrSubtreeXMLProcessor
				.getOriginalDoc().importNode(measureDetailsNode, true);
		populationOrSubtreeXMLProcessor.getOriginalDoc().getFirstChild()
		.appendChild(importedMeasureDetailsNode);
		
		/*System.out.println("Inflated popualtion tree: "
				+ populationOrSubtreeXMLProcessor
						.transform(populationOrSubtreeXMLProcessor
								.getOriginalDoc()));*/
		return populationOrSubtreeXMLProcessor;
	}
	
	/**
	 * Resolve main sub tree node.
	 *
	 * @param measureXMLProcessor the measure xml processor
	 * @param subTreeId the sub tree id
	 * @return the node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static Node resolveMainSubTreeNode(
			XmlProcessor measureXMLProcessor, String subTreeId)
					throws XPathExpressionException {
		Node subTreeNode = measureXMLProcessor.findNode(
				measureXMLProcessor.getOriginalDoc(),
				"/measure/subTreeLookUp/subTree[@uuid='" + subTreeId + "']");
		// Node subTreeNodeClone = subTreeNode.cloneNode(true);
		// find all <subTreeRef> tags in 'subTreeNode'
		NodeList subTreeRefNodeList = measureXMLProcessor.findNodeList(
				measureXMLProcessor.getOriginalDoc(),
				"/measure/subTreeLookUp/subTree[@uuid='" + subTreeId
				+ "']//subTreeRef");
		
		// resolve for each subTreeRef with subTree (clause within clause)
		for (int i = 0; i < subTreeRefNodeList.getLength(); i++) {
			Node subTreeRefNode = subTreeRefNodeList.item(i);
			// Node subTreeRefNodeClone = subTreeRefNode.cloneNode(true);
			List<String> childSubTreeRefList = new ArrayList<String>();
			childSubTreeRefList.add(subTreeId);
			if (!resolveChildSubTreeNode(measureXMLProcessor, subTreeRefNode,
					childSubTreeRefList)) {
				return null;
			}
			// replaceSubTreeNode(measureXMLProcessor, subTreeRefNode, null,
			// subTreeNode );
		}
		return subTreeNode;
	}
	
	/**
	 * Resolve child sub tree node.
	 *
	 * @param measureXMLProcessor the measure xml processor
	 * @param subTreeRefNode the sub tree ref node
	 * @param childSubTreeRefList the child sub tree ref list
	 * @return true, if successful
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static boolean resolveChildSubTreeNode(
			XmlProcessor measureXMLProcessor, Node subTreeRefNode,
			List<String> childSubTreeRefList) throws XPathExpressionException {
		
		String subTreeId = subTreeRefNode.getAttributes().getNamedItem("id")
				.getNodeValue();
		//System.out.println("sub subTreeId:" + subTreeId);
		if (!childSubTreeRefList.contains(subTreeId)) {
			childSubTreeRefList.add(subTreeId);
			Node subTreeNode = measureXMLProcessor
					.findNode(measureXMLProcessor.getOriginalDoc(),
							"/measure/subTreeLookUp/subTree[@uuid='"
									+ subTreeId + "']");
					
			// find all <subTreeRef> tags in 'subTreeNode'
			NodeList subTreeRefNodeList = measureXMLProcessor.findNodeList(
					measureXMLProcessor.getOriginalDoc(),
					"/measure/subTreeLookUp/subTree[@uuid='" + subTreeId
					+ "']//subTreeRef");
			for (int i = 0; i < subTreeRefNodeList.getLength(); i++) {
				Node childSubTreeRefNode = subTreeRefNodeList.item(i);
				if (!resolveChildSubTreeNode(measureXMLProcessor,
						childSubTreeRefNode, childSubTreeRefList)) {
					return false;
				}
		
			}
			replaceSubTreeNode(measureXMLProcessor, subTreeRefNode, null,
					subTreeNode);
		} else {
			return false;
		}
		childSubTreeRefList.remove(subTreeId);
		return true;
	}
	
	/**
	 * Replace sub tree node.
	 *
	 * @param xmlProcessor the xml processor
	 * @param subTreeRefNode the sub tree ref node
	 * @param commentNode the comment node
	 * @param subTreeNode the sub tree node
	 */
	private static void replaceSubTreeNode(XmlProcessor xmlProcessor,
			Node subTreeRefNode, Node commentNode, Node subTreeNode) {
		// replace the 'subTreeRefNode' with 'subTreeNode'
		Node subTreeRefNodeParent = subTreeRefNode.getParentNode();
		Node subTreeNodeImportedClone = xmlProcessor.getOriginalDoc()
				.importNode(subTreeNode, true);
		
		Node newNode = subTreeNodeImportedClone;
		String qdmVariable = subTreeNodeImportedClone.getAttributes().getNamedItem("qdmVariable").getNodeValue();
		if (subTreeNodeImportedClone.hasChildNodes() && "false".equalsIgnoreCase(qdmVariable)) {
			newNode = subTreeNodeImportedClone.getFirstChild();
		}
		
		subTreeRefNodeParent.replaceChild(newNode, subTreeRefNode);
		if (commentNode != null) {
			subTreeRefNodeParent.insertBefore(commentNode,newNode);
		}
	}
	
	/**
	 * Gets the population or subtree name.
	 *
	 * @param populationOrSubtreeXMLProcessor the population or subtree xml processor
	 * @param isPopulation the is population
	 * @return the population or subtree name
	 */
	private static String getPopulationOrSubtreeName(
			XmlProcessor populationOrSubtreeXMLProcessor, boolean isPopulation) {
		String name = "";
		try {
			if (isPopulation) {
				name = populationOrSubtreeXMLProcessor.findNode(
						populationOrSubtreeXMLProcessor.getOriginalDoc(),
						"//clause/@displayName").getNodeValue();
			} else {
				name = populationOrSubtreeXMLProcessor.findNode(
						populationOrSubtreeXMLProcessor.getOriginalDoc(),
						"//subTree/@displayName").getNodeValue();
			}
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return name;
	}
	
	private static void parseAndBuildHTML(
			XmlProcessor populationOrSubtreeXMLProcessor,
			Element populationOrSubtreeListElement) {
		
		try {
			Node rootNode = populationOrSubtreeXMLProcessor.getOriginalDoc()
					.getFirstChild();
			parseAndBuildHTML(populationOrSubtreeXMLProcessor,
					populationOrSubtreeListElement, rootNode, 0);
		} catch (DOMException e) {
			e.printStackTrace();
		}
	}
	
	private static void parseAndBuildHTML(
			XmlProcessor populationOrSubtreeXMLProcessor,
			Element populationOrSubtreeListElement, Node clauseNode,
			int currentGroupNumber) {
		
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
			if (childNodes.getLength() == 1) {
				if("measureObservation".equalsIgnoreCase(parentName)){
					displayNone(
							populationOrSubtreeListElement,
							populationOrSubtreeXMLProcessor, clauseNode);
				}else{
					displayNone(
							populationOrSubtreeListElement.appendElement(HTML_UL),
							populationOrSubtreeXMLProcessor, clauseNode);
				}
			}
			/*String parentName = "";*/
			if (clauseNode.getAttributes().getNamedItem("type") != null) {
				/*parentName = clauseNode.getAttributes().getNamedItem("type")
						.getNodeValue();*/
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
			}
			for (int i = 0; i < childNodes.getLength(); i++) {
				parseChild(childNodes.item(i), populationOrSubtreeListElement,
						childNodes.item(i).getParentNode(),
						populationOrSubtreeXMLProcessor, false);
			}
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
	private static void parseChild(Node item, Element parentListElement,
			Node parentNode, XmlProcessor populationOrSubtreeXMLProcessor,
			boolean satisfiesAnyAll) {
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
						populationOrSubtreeXMLProcessor, satisfiesAnyAll);
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
		} else if (SUB_TREE.equals(nodeName)) {
			NamedNodeMap map = item.getAttributes();
			if ((map.getNamedItem("qdmVariable") != null)
					&& "true".equalsIgnoreCase(map.getNamedItem("qdmVariable")
							.getNodeValue()) && (showOnlyVariableName == false)) {
				if (parentListElement.nodeName().equals(HTML_UL) ) {
					parentListElement = parentListElement
							.appendElement(HTML_LI);
				}
				if (LOGICAL_OP.equals(parentNode.getNodeName())) {
					parentListElement.appendText(getNodeText(parentNode,
							populationOrSubtreeXMLProcessor));
				}
				if (parentListElement.html().contains("Stratification")) {
					parentListElement = parentListElement
							.appendElement(HTML_UL).appendElement(HTML_LI);
				}
				String displayName = map.getNamedItem("displayName").getNodeValue();
				
				if(map.getNamedItem("instanceOf") == null){
					displayName = "$"+StringUtils.deleteWhitespace(displayName);
				}
				
				if(COMMENT.equals(item.getFirstChild().getNodeName())){
					String commentValue = item.getFirstChild().getTextContent();
					if ((commentValue != null) && (commentValue.trim().length() > 0)) {
						parentListElement.appendElement("br");
						Element italicElement = parentListElement.appendElement("i");
						italicElement.appendText("# " + commentValue);
						parentListElement.appendElement("br");
					}
				}
				parentListElement.appendText(displayName+ " ");
				
			} else {
				showOnlyVariableName = false;
				NodeList childNodes = item.getChildNodes();
				for (int i = 0; i < childNodes.getLength(); i++) {
					Element temp = parentListElement;
					//					if(temp.html().contains("Measure Observation")){
					//						temp = parentListElement.appendElement(HTML_UL).appendElement(HTML_LI);
					//					}
					parseChild(childNodes.item(i), temp,
							parentNode, populationOrSubtreeXMLProcessor,
							satisfiesAnyAll);
				}
			}
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
						populationOrSubtreeXMLProcessor, satisfiesAnyAll);
			}
		} else if (RELATIONAL_OP.equals(nodeName)) {
			if (LOGICAL_OP.equals(parentNode.getNodeName())
					|| SET_OP.equals(parentNode.getNodeName())) {
				Element liElement = parentListElement.appendElement(HTML_LI);
				// liElement.appendText(getNodeText(parentNode,
				// populationOrSubtreeXMLProcessor));
				if (LOGICAL_OP.equals(parentNode.getNodeName())) {
					liElement.appendText(getNodeText(parentNode,
							populationOrSubtreeXMLProcessor));
				}
				getRelationalOpText(item, liElement,
						populationOrSubtreeXMLProcessor, satisfiesAnyAll);
			} else {
				if (parentListElement.html().contains("Stratification")) {
					parentListElement = parentListElement
							.appendElement(HTML_UL).appendElement(HTML_LI);
				}
				getRelationalOpText(item, parentListElement,
						populationOrSubtreeXMLProcessor, satisfiesAnyAll);
				/**
				 * A relationalOp can have 2 children. First evaluate the LHS
				 * child, then add the name of the relationalOp and finally
				 * evaluate the RHS child.
				 */
				/*
				 * NodeList childNodes = item.getChildNodes();
				 * if(childNodes.getLength() == 2){
				 * parseChild(childNodes.item(0),parentListElement,item);
				 * parentListElement
				 * .appendText(item.getAttributes().getNamedItem
				 * (DISPLAY_NAME).getNodeValue()+" ");
				 * parseChild(childNodes.item(1),parentListElement,item); }
				 */
			}
			
		} else if (ELEMENT_REF.equals(nodeName)) {
			if (satisfiesAnyAll
					&& (lhsID != null)
					&& lhsID.contains(item.getAttributes()
							.getNamedItem("id").getNodeValue())) {
				if (item.hasChildNodes()) {
					parentListElement.appendText(getAttributeText(
							item.getFirstChild(),
							populationOrSubtreeXMLProcessor) + " ");
				}
			} else {
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
			}
		} else if (FUNCTIONAL_OP.equals(nodeName)) {
			// check if it is a subset function ie.  FIRST-FIFTH, MOST RECENT
			boolean isSubsetFunction = isSubsetFunction(item, populationOrSubtreeXMLProcessor);
			if (LOGICAL_OP.equals(parentNode.getNodeName())
					|| SET_OP.equals(parentNode.getNodeName())) {
				Element liElement = parentListElement.appendElement(HTML_LI);
				// liElement.appendText(" "+getNodeText(parentNode,
				// populationOrSubtreeXMLProcessor));
				if (LOGICAL_OP.equals(parentNode.getNodeName())) {
					liElement.appendText(" "
							+ getNodeText(parentNode,
									populationOrSubtreeXMLProcessor));
				}
				if (item.getAttributes().getNamedItem("type").getNodeValue()
						.contains("SATISFIES")) {
					
					createSatisfies(item, liElement,
							populationOrSubtreeXMLProcessor);
				} else {
					liElement.appendText(getFunctionText(item));
					NodeList childNodes = item.getChildNodes();
					if ((childNodes.getLength() == 1)
							&& ELEMENT_REF.equals(childNodes.item(0)
									.getNodeName())) {
						parseChild(childNodes.item(0), liElement, item,
								populationOrSubtreeXMLProcessor,
								satisfiesAnyAll);
					} else {
						if ((childNodes.getLength() > 1)
								|| (childNodes.item(0).getNodeName()
										.equals(FUNCTIONAL_OP) && !isSubsetFunction)) {
							liElement = liElement.appendElement(HTML_UL);
						}
						for (int i = 0; i < childNodes.getLength(); i++) {
							if (childNodes.getLength() > 1) {
								Element newLiElement = liElement.appendElement(HTML_LI);
								parseChild(childNodes.item(i), newLiElement, item,
										populationOrSubtreeXMLProcessor,
										satisfiesAnyAll);
							}else{
								parseChild(childNodes.item(i), liElement, item,
										populationOrSubtreeXMLProcessor,
										satisfiesAnyAll);
							}
						}
					}
				}
			} else {
				if (item.getAttributes().getNamedItem("type").getNodeValue()
						.contains("SATISFIES")) {
					if (parentListElement.nodeName().equals(HTML_UL)) {
						parentListElement = parentListElement
								.appendElement(HTML_LI);
					} else if (parentListElement.html().contains(
							"Stratification")) {
						parentListElement = parentListElement.appendElement(
								HTML_UL).appendElement(HTML_LI);
					}
					createSatisfies(item, parentListElement,
							populationOrSubtreeXMLProcessor);
				} else {
					if (parentListElement.nodeName().equals(HTML_UL)) {
						parentListElement = parentListElement
								.appendElement(HTML_LI);
					} else if (parentListElement.html().contains(
							"Stratification")) {
						parentListElement = parentListElement.appendElement(
								HTML_UL).appendElement(HTML_LI);
					}
					parentListElement.appendText(getFunctionText(item));
					NodeList childNodes = item.getChildNodes();
					if ((childNodes.getLength() == 1)
							&& ELEMENT_REF.equals(childNodes.item(0)
									.getNodeName())) {
						parseChild(childNodes.item(0), parentListElement, item,
								populationOrSubtreeXMLProcessor,
								satisfiesAnyAll);
						//System.out.println(" if Adding a new line");
					} else {
						Element ulElement = parentListElement;
						if ( !(childNodes.getLength() == 0) && ((childNodes.getLength() > 1) || (childNodes.item(0).getNodeName()
								.equals(FUNCTIONAL_OP) && !isSubsetFunction))) {
							ulElement = parentListElement
									.appendElement(HTML_UL);
						}
						for (int i = 0; i < childNodes.getLength(); i++) {
							if ((childNodes.getLength() > 1) || (childNodes.item(0).getNodeName()
									.equals(FUNCTIONAL_OP) && !isSubsetFunction)) {
								Element newLiElem = ulElement.appendElement(HTML_LI);
								//ulElement = ulElement.appendElement(HTML_LI);
								parseChild(childNodes.item(i), newLiElem, item,
										populationOrSubtreeXMLProcessor,
										satisfiesAnyAll);
							}else{
								parseChild(childNodes.item(i), ulElement, item,
										populationOrSubtreeXMLProcessor,
										satisfiesAnyAll);
							}
						}
					}
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
	 * Checks if the FUNCTION_OP is a subset type (FIRST-FIFTH, and MOST_RECENT).
	 *
	 * @param item the iteml
	 * @param populationOrSubtreeXMLProcessor the population or subtree xml processor
	 * @return
	 */
	private static boolean isSubsetFunction(Node item, XmlProcessor populationOrSubtreeXMLProcessor) {
		boolean isSubset = false;
		String itemType = item.getAttributes().getNamedItem("type").getNodeValue();
		if(subSetFunctionsList.contains(itemType)){
			isSubset = true;
		}
		return isSubset;
	}
	
	
	/**
	 * Creates the satisfies.
	 *
	 * @param item the item
	 * @param liElement the li element
	 * @param populationOrSubtreeXMLProcessor the population or subtree xml processor
	 */
	private static void createSatisfies(Node item, Element liElement,
			XmlProcessor populationOrSubtreeXMLProcessor) {
		
		//System.out.println("createSatisfies nodeName: " + item);
		//System.out.println("Parent List Element: " + liElement);
		
		Node lhs = item.getFirstChild();
		if ("elementRef".equalsIgnoreCase(lhs.getNodeName())) {
			// Element ulElement = parentListElement.appendElement(HTML_LI);
			if( checkForSatisfiesParentNode(item.getParentNode())){
				parseChild(lhs, liElement, item, populationOrSubtreeXMLProcessor,
						true);
			} else {
				parseChild(lhs, liElement, item, populationOrSubtreeXMLProcessor,
						false);
			}
			liElement.appendText(" "
					+ item.getAttributes().getNamedItem("displayName")
					.getNodeValue().toLowerCase() + ":");
			String lhsId = lhs.getAttributes().getNamedItem("id").getNodeValue();
			
			lhsID.push(lhsId);
			//System.out.println("Added an ID: " + lhsId);
			//System.out.println("LhsID Array: ");
			//for (int i=0; i < lhsID.size(); i++) {
			//	System.out.println(lhsID.get(i));
			//}
			
			NodeList childNodes = item.getChildNodes();
			if (childNodes.getLength() > 1) {
				Element ulElement = liElement.appendElement(HTML_UL);
				for (int i = 1; i < childNodes.getLength(); i++) {
					parseChild(childNodes.item(i),
							ulElement.appendElement(HTML_LI), item,
							populationOrSubtreeXMLProcessor, true);
				}
				lhsID.pop();
			}
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
			e.printStackTrace();
		}
		return retValue;
	}
	
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
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the relational op text.
	 *
	 * @param item the item
	 * @param liElement the li element
	 * @param populationOrSubtreeXMLProcessor the population or subtree xml processor
	 * @param satisfiesAnyAll the satisfies any all
	 * @return the relational op text
	 */
	private static void getRelationalOpText(Node item, Element liElement,
			XmlProcessor populationOrSubtreeXMLProcessor,
			boolean satisfiesAnyAll) {
		
		//System.out.println("getRelationalOpText item: " + item);
		//System.out.println("List Element: " + liElement);
		
		/**
		 * A relationalOp can have 2 children. First evaluate the LHS child,
		 * then add the name of the relationalOp and finally evaluate the RHS
		 * child.
		 */
		NodeList childNodes = item.getChildNodes();
		
		if (childNodes.getLength() == 2) {
			Element newLiElement = liElement;
			if (newLiElement.nodeName().equals(HTML_UL)) {
				newLiElement = newLiElement.appendElement(HTML_LI);
			}
			String name = item.getAttributes().getNamedItem(DISPLAY_NAME)
					.getNodeValue().toLowerCase()
					+ " ";
			
			if (item.getAttributes().getNamedItem("unit") != null) {
				name = name.replaceFirst(
						item.getAttributes().getNamedItem("unit")
						.getNodeValue(), getUnitString(item));
			}
			
			boolean isChild1QDMOrVariable = checkIfElementRefOrQDMVariable(childNodes
					.item(0));
			boolean isChild2QDMOrVariable = checkIfElementRefOrQDMVariable(childNodes
					.item(1));
			
			if (isParentNodeName(item, LOGICAL_OP) && !isChild1QDMOrVariable) {
				newLiElement = liElement.appendElement(HTML_UL).appendElement(
						HTML_LI);
			}
			
			boolean isParentheses = false;
			
			if (RELATIONAL_OP.equals(childNodes.item(0).getNodeName())) {
				NodeList children = childNodes.item(0).getChildNodes();
				if (checkIfElementRefOrQDMVariable(children.item(0)) && checkIfElementRefOrQDMVariable(children.item(1))) {
					isParentheses = true;
				}
			}else if(FUNCTIONAL_OP.equals(childNodes.item(0).getNodeName())){
				isParentheses = true;
			}
			
			if(isParentheses){
				newLiElement.appendText(" (");
			}
			
			parseChild(childNodes.item(0), newLiElement, item,
					populationOrSubtreeXMLProcessor, satisfiesAnyAll);
			
			if(isParentheses){
				/* Find all elements under this element
				 * (including self, and children of children),
				 * and add a parentheses to the last element in the list.
				 */
				Elements elements = newLiElement.getAllElements();
				Element lastElement = elements.get(elements.size() - 1);
				lastElement.appendText(") ");
			}
			
			if (!newLiElement.children().isEmpty()) {
				Element firstElement = newLiElement.children().first();
				if (HTML_UL.equals(firstElement.nodeName())) {
					newLiElement = firstElement.appendElement(HTML_LI);
				}
			}
			
			if (!isChild2QDMOrVariable) {
				newLiElement.appendText(name);
				newLiElement = newLiElement.appendElement(HTML_UL)
						.appendElement(HTML_LI);
			} else {
				newLiElement.appendText(name);
				
				if (RELATIONAL_OP.equals(childNodes.item(1).getNodeName())) {
					NodeList children = childNodes.item(1).getChildNodes();
					if(checkIfElementRefOrQDMVariable(children.item(0)) && checkIfElementRefOrQDMVariable(children.item(1))) {
						newLiElement.appendText(" (");
						parseChild(childNodes.item(1), newLiElement, item,
								populationOrSubtreeXMLProcessor, false);
						
						Elements elements = newLiElement.getAllElements();
						Element lastElement = elements.get(elements.size() - 1);
						lastElement.appendText(") ");
						return;
					}
				}
				
			}
			parseChild(childNodes.item(1), newLiElement, item,
					populationOrSubtreeXMLProcessor, false);
		}
	}
	
	/**
	 * Checks if the Node is a "elementRef" node or a "subTree" node with a
	 * "qdmVariable" attribute whose value is "true". If Yes, returns true, else
	 * returns false.
	 *
	 * @param node the node
	 * @return true, if successful
	 */
	private static boolean checkIfElementRefOrQDMVariable(Node node) {
		boolean retValue = false;
		String nodeName = node.getNodeName();
		
		if (ELEMENT_REF.equals(nodeName)) {
			retValue = true;
		} else if (SUB_TREE.equals(nodeName)) {
			Node attributeNode = node.getAttributes().getNamedItem(
					"qdmVariable");
			if (attributeNode != null) {
				if ("true".equals(attributeNode.getNodeValue())) {
					retValue = true;
				}
			}
		} else if (LOGICAL_OP.equals(nodeName)) {
			retValue = true;
		} else if (SET_OP.equals(nodeName)) {
			retValue = true;
		} else if (RELATIONAL_OP.equals(nodeName)) {
			NodeList children = node.getChildNodes();
			retValue = checkIfElementRefOrQDMVariable(children.item(0));
		} else if(FUNCTIONAL_OP.equals(nodeName)){
			NodeList children = node.getChildNodes();
			//System.out.println("Number of children: " + children.getLength());
			if(children.getLength() == 1){
				Node child = children.item(0);
				retValue = checkIfElementRefOrQDMVariable(child);
			}
			if (children.getLength() == 3 ) {
				if ((node.getAttributes().getNamedItem("type") != null)
						&& node.getAttributes().getNamedItem("type").getNodeValue().contains("SATISFIES")) {
					retValue = true;
				}
			}
		}
		
		return retValue;
	}
	
	/**
	 * Checks if is parent node name.
	 *
	 * @param item the item
	 * @param parentNodeName the parent node name
	 * @return true, if is parent node name
	 */
	private static boolean isParentNodeName(Node item, String parentNodeName) {
		Node parentNode = item.getParentNode();
		if ((parentNode != null) && SUB_TREE.equals(parentNode.getNodeName())) {
			parentNode = parentNode.getParentNode();
		}
		
		if (parentNode != null) {
			if (parentNodeName.equals(parentNode.getNodeName())) {
				return true;
			}
		}
		return false;
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
			
			if (node.hasChildNodes()) {
				NodeList childNodes = node.getChildNodes();
				for (int j = 0; j < childNodes.getLength(); j++) {
					Node childNode = childNodes.item(j);
					if (childNode.getNodeName().equals("attribute")) {
						String attributeText = getAttributeText(childNode,
								populationOrSubtreeXMLProcessor);
						// special handling for 'negation rationale' attribute
						if (childNode.getAttributes().getNamedItem("name")
								.getNodeValue().equals("negation rationale")) {
							String[] nameArr = node.getAttributes()
									.getNamedItem(DISPLAY_NAME).getNodeValue()
									.split(":");
							if (nameArr.length == 2) {
								name = nameArr[1].trim() + attributeText + "\""
										+ nameArr[0].trim();
							} else if (nameArr.length == 3) {
								name = nameArr[1].trim() + ": "
										+ nameArr[2].trim() + attributeText
										+ "\"" + nameArr[0].trim();
							}
							// Move Occurrence of to the beginning of the name
							if (name.contains("Occurrence")
									&& name.contains("of")) {
								int occurLoc = name.indexOf("Occurrence", 0);
								int ofLoc = name.indexOf("of", occurLoc);
								String occur = name.substring(occurLoc,
										ofLoc + 2);
								name = name.replaceAll(occur, "");
								occur += " ";
								name = occur.concat(name);
							}
						}
						
						else {
							name += attributeText;
						}
					}
				}
			}
			
			name = "\"" + name + "\" ";
		}
		return name;
	}
	
	/**
	 * Gets the attribute text.
	 *
	 * @param attributeNode the attribute node
	 * @param populationOrSubtreeXMLProcessor the population or subtree xml processor
	 * @return the attribute text
	 */
	private static String getAttributeText(Node attributeNode,
			XmlProcessor populationOrSubtreeXMLProcessor) {
		String attributeText = "";
		String attributeName = attributeNode.getAttributes()
				.getNamedItem("name").getNodeValue();
		String modeName = attributeNode.getAttributes().getNamedItem("mode")
				.getNodeValue();
		
		if ("Check if Present".equals(modeName)) {
			attributeText = " (" + attributeName + ")";
		} else if ("Less Than Or Equal To".equals(modeName)) {
			attributeText = getAttributeText(attributeNode, attributeName," <= ");
		} else if ("Greater Than Or Equal To".equals(modeName)) {
			attributeText = getAttributeText(attributeNode, attributeName," >= ");
		} else if ("Equal To".equals(modeName)) {
			attributeText = getAttributeText(attributeNode, attributeName," = ");
		} else if ("Greater Than".equals(modeName)) {
			attributeText = getAttributeText(attributeNode, attributeName," > ");
		} else if ("Less Than".equals(modeName)) {
			attributeText = getAttributeText(attributeNode, attributeName," < ");
		} else if ("Value Set".equals(modeName)) {
			String qdmUUIDValue = attributeNode.getAttributes()
					.getNamedItem("qdmUUID").getNodeValue();
			// find the qdm tag using qdmUUIDValue
			try {
				Node qdmNodeNameAttribute = populationOrSubtreeXMLProcessor
						.findNode(attributeNode.getOwnerDocument(),
								"//elementLookUp/qdm[@uuid='" + qdmUUIDValue
								+ "']/@name");
				if (qdmNodeNameAttribute != null) {
					if ("negation rationale".equals(attributeName)) {
						attributeText = " not done: "
								+ qdmNodeNameAttribute.getNodeValue()
								+ "\" for ";
					} else {
						attributeText = " (" + attributeName + ": "
								+ qdmNodeNameAttribute.getNodeValue() + ")";
					}
				} else {
					attributeText = "";
				}
			} catch (XPathExpressionException e) {
				e.printStackTrace();
				attributeText = "";
			}
			
		}
		return attributeText;
	}
	
	/**
	 * Gets the attribute text.
	 *
	 * @param attributeNode the attribute node
	 * @param attributeName the attribute name
	 * @param operator the operator
	 * @return the attribute text
	 */
	private static String getAttributeText(Node attributeNode,
			String attributeName, String operator) {
		String attributeText;
		if(attributeName.contains("date")){
			String attrDate = attributeNode.getAttributes()
					.getNamedItem("attrDate").getNodeValue();
			attributeText = " (" + attributeName + operator + formatDate(attrDate) + ")";
		} else {
			String comparisonValue = attributeNode.getAttributes()
					.getNamedItem("comparisonValue").getNodeValue();
			attributeText = " (" + attributeName + operator + comparisonValue
					+ " " + getUnitString(attributeNode) + ")";
		}
		return attributeText;
	}
	
	/**
	 * Gets the unit string.
	 *
	 * @param attributeNode the attribute node
	 * @return the unit string
	 */
	private static String getUnitString(Node attributeNode) {
		String unitValue = "";
		
		Node unitNode = attributeNode.getAttributes().getNamedItem("unit");
		if (unitNode != null) {
			unitValue = attributeNode.getAttributes().getNamedItem("unit")
					.getNodeValue();
		}
		
		if (unitValue.equals("L")) {
			unitValue = "Liter";
		} else if (unitValue.equals("years") || unitValue.equals("year")) {
			unitValue = "year(s)";
		} else if (unitValue.equals("month") || unitValue.equals("months")) {
			unitValue = "month(s)";
		} else if (unitValue.equals("day") || unitValue.equals("days")) {
			unitValue = "day(s)";
		} else if (unitValue.equals("hour") || unitValue.equals("hours")) {
			unitValue = "hour(s)";
		} else if (unitValue.equals("week") || unitValue.equals("weeks")) {
			unitValue = "week(s)";
		} else if (unitValue.equals("minute") || unitValue.equals("minutes")) {
			unitValue = "minute(s)";
		} else if (unitValue.equals("quarter") || unitValue.equals("quarters")) {
			unitValue = "quarter(s)";
		} else if (unitValue.equals("second") || unitValue.equals("seconds")) {
			unitValue = "second(s)";
		}
		
		return unitValue;
	}
	
	/**
	 * Gets the function text.
	 *
	 * @param item the item
	 * @return the function text
	 */
	private static String getFunctionText(Node item) {
		if (!FUNCTIONAL_OP.equals(item.getNodeName())) {
			return "";
		}
		
		String typeAttribute = item.getAttributes().getNamedItem("type")
				.getNodeValue().toUpperCase();
		String functionDisplayName = item.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
		
		if (MatConstants.AGE_AT.equalsIgnoreCase(typeAttribute)) {
			functionDisplayName = item.getAttributes()
					.getNamedItem(DISPLAY_NAME).getNodeValue();
		} else if (functionDisplayName.startsWith("AVG")) {
			functionDisplayName = functionDisplayName.replaceFirst("AVG",
					"Average") + " of";
		} else if (functionDisplayName.startsWith("COUNT")) {
			functionDisplayName = functionDisplayName.replaceFirst("COUNT",
					"Count") + " of";
		} else if (functionDisplayName.startsWith("DATEDIFF")) {
			functionDisplayName = functionDisplayName.replaceFirst("DATEDIFF",
					"Difference between dates") + " of";
		} else if (functionDisplayName.startsWith("MAX")) {
			functionDisplayName = functionDisplayName.replaceFirst("MAX",
					"Maximum") + " of";
		} else if (functionDisplayName.startsWith("MIN")) {
			functionDisplayName = functionDisplayName
					.replaceFirst("MIN", "Min") + " of";
		} else if (functionDisplayName.startsWith("MEDIAN")) {
			functionDisplayName = functionDisplayName.replaceFirst("MEDIAN",
					"Median") + " of";
		} else if (functionDisplayName.startsWith("SUM")) {
			functionDisplayName = functionDisplayName
					.replaceFirst("SUM", "Sum") + " of";
		} else if (functionDisplayName.startsWith("TIMEDIFF")) {
			functionDisplayName = functionDisplayName.replaceFirst("TIMEDIFF",
					"Time difference") + " of";
			;
		} else if (functionDisplayName.startsWith("FOURTH")) {
			functionDisplayName = functionDisplayName.replaceFirst("FOURTH",
					"Fourth");
		} else if (functionDisplayName.startsWith("FIFTH")) {
			functionDisplayName = functionDisplayName.replaceFirst("FIFTH",
					"Fifth");
		} else if(functionDisplayName.startsWith("DATETIMEDIFF")){
			functionDisplayName = functionDisplayName.replaceFirst("DATETIMEDIFF", "Datetime difference") + " of";
		}
		
		if (item.getAttributes().getNamedItem("unit") != null) {
			String unit = item.getAttributes().getNamedItem("unit")
					.getNodeValue();
			functionDisplayName = functionDisplayName.replaceFirst(unit,
					getUnitString(item));
		}
//		functionDisplayName = StringUtils.capitalize(functionDisplayName
//				.toLowerCase());
		
		return functionDisplayName + ": ";
	}
	
	/**
	 * This will look at the elementRef node and return an appropriate display
	 * text for human readable HTML for that QDM. 
	 * <attributes> into account.
	 *
	 * @return the org.jsoup.nodes. document
	 */
	// private static String getQDMText(Node item) {
	// if(!ELEMENT_REF.equals(item.getNodeName())){
	// return "";
	// }
	// String displayName =
	// item.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
	// if(displayName.endsWith(" : Timing Element")){
	// displayName =
	// displayName.substring(0,displayName.indexOf(" : Timing Element"));
	// }
	// return "\"" + displayName + "\" ";
	// }
	
	private static org.jsoup.nodes.Document createBaseHumanReadableDocument() {
		org.jsoup.nodes.Document htmlDocument = org.jsoup.nodes.Document
				.createShell("");
		Element head = htmlDocument.head();
		appendStyleNode(head);
		return htmlDocument;
	}
	
	/**
	 * Append style node.
	 *
	 * @param head the head
	 */
	private static void appendStyleNode(Element head) {
		String styleTagString = MATCssUtil.getCSS();
		head.append(styleTagString);
	}
	
	/**
	 * Generate human readable.
	 *
	 * @param humanReadableHTMLDocument the human readable html document
	 * @param simpleXMLProcessor the simple xml processor
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static void generateHumanReadable(
			Document humanReadableHTMLDocument, XmlProcessor simpleXMLProcessor)
					throws XPathExpressionException {
		
		generateTableOfContents(humanReadableHTMLDocument, simpleXMLProcessor);
		generatePopulationCriteriaHumanReadable(humanReadableHTMLDocument,
				simpleXMLProcessor);
		generateQDMVariables(humanReadableHTMLDocument, simpleXMLProcessor);
		generateDataCriteria(humanReadableHTMLDocument, simpleXMLProcessor);
		generateSupplementalData(humanReadableHTMLDocument, simpleXMLProcessor);
		generateRiskAdjustmentVariables(humanReadableHTMLDocument, simpleXMLProcessor);
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
		boolean isFhir = simpleXMLProcessor.getOriginalXml().contains("<usingModel>FHIR</usingModel>");
		Element bodyElement = humanReadableHTMLDocument.body();
		
		bodyElement.append("<h2><a name=\"toc\">Table of Contents</a></h2>");
		Element tocULElement = bodyElement.appendElement(HTML_UL);
		
		Element populationCriteriaLI = tocULElement.appendElement(HTML_LI);
		populationCriteriaLI
		.append("<a href=\"#d1e405\">Population Criteria</a>");
		
		Element dataVariables = tocULElement.appendElement(HTML_LI);
		dataVariables
		.append("<a href=\"#d1e539\">Data Criteria (QDM Variables)</a>");
		
		Element dataCriteriaLI = tocULElement.appendElement(HTML_LI);
		dataCriteriaLI
		.append("<a href=\"#d1e647\">Data Criteria (" + (isFhir ? "FHIR Data Requirements" : "QDM Data Elements") + ")</a>");
		
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
	 * Generate data criteria.
	 *
	 * @param humanReadableHTMLDocument the human readable html document
	 * @param simpleXMLProcessor the simple xml processor
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static void generateDataCriteria(
			Document humanReadableHTMLDocument, XmlProcessor simpleXMLProcessor)
					throws XPathExpressionException {
		boolean isFhir = simpleXMLProcessor.getOriginalXml().contains("<usingModel>FHIR</usingModel>");
		Element bodyElement = humanReadableHTMLDocument.body();
		bodyElement
		.append("<h3><a name=\"d1e647\" href=\"#toc\">Data Criteria (" + (isFhir ? "FHIR Data Requirements" : "QDM Data Elements") + ")</a></h3>");
		
		Element mainDivElement = bodyElement.appendElement("div");
		Element mainListElement = mainDivElement.appendElement(HTML_UL);
		
		// get all qdm elemes in 'elementLookUp' which are not attributes or
		// dont have 'Timing Element', 'Patient Characteristic Expired' and
		// 'Patient Characteristic Birthdate' Default QDM data type and are not
		// supplement data elems
		String xpathForQDMElements = "/measure/elementLookUp/qdm[@datatype != 'Timing Element' and @oid!='"
				+ ConstantMessages.BIRTHDATE_OID + "' " + "and @oid!='"
				+ ConstantMessages.DEAD_OID + "']";
		NodeList qdmElements = simpleXMLProcessor.findNodeList(
				simpleXMLProcessor.getOriginalDoc(),
				xpathForQDMElements);
		
		Map<String, Node> qdmMap = new HashMap<String, Node>();
		Map<String, Node> attributeMap = new HashMap<String, Node>();
		
		for (int i = 0; i < qdmElements.getLength(); i++) {
			Node qdmNode = qdmElements.item(i);
			String oid = qdmNode.getAttributes().getNamedItem("oid")
					.getNodeValue();
			String datatype = qdmNode.getAttributes().getNamedItem("datatype")
					.getNodeValue();
			String suppDataElement = qdmNode.getAttributes()
					.getNamedItem("suppDataElement").getNodeValue();
			String uuid = qdmNode.getAttributes().getNamedItem("uuid")
					.getNodeValue();
			String name = qdmNode.getAttributes().getNamedItem("name")
					.getNodeValue();
			
			if ("attribute".equals(datatype)) {
				//attributeMap.put(oid + datatype, qdmNode);
				attributeMap.put(datatype + ":" + name + "~" + oid, qdmNode);
			} else if ("true".equals(suppDataElement)) {
				int isUsedInLogic = simpleXMLProcessor.getNodeCount(
						simpleXMLProcessor.getOriginalDoc(),
						"count(//subTree//elementRef[@id='" + uuid + "'])");
				if (isUsedInLogic > 0) {
					if (!qdmMap.containsKey(oid + datatype)) {
						qdmMap.put(datatype + ":" + name + "~" + oid, qdmNode);
					}
				}
			} else {
				int supplementalCount = simpleXMLProcessor.getNodeCount(
						simpleXMLProcessor.getOriginalDoc(),
						"count(/measure/supplementalDataElements/elementRef[@id='"
								+ uuid + "'])");
				if (supplementalCount > 0) {
					int isUsedInLogic = simpleXMLProcessor.getNodeCount(
							simpleXMLProcessor.getOriginalDoc(),
							"count(//subTree//elementRef[@id='" + uuid + "'])");
					if (isUsedInLogic > 0) {
						if (!qdmMap.containsKey(oid + datatype)) {
							qdmMap.put(datatype + ":" + name + "~" + oid,
									qdmNode);
						}
					}
				}
				if (!qdmMap.containsKey(oid + datatype)) {
					qdmMap.put(datatype + ":" + name + "~" + oid, qdmNode);
				}
			}
		}
		
		List<String> qdmNameList = new ArrayList<String>(qdmMap.keySet());
		Collections.sort(qdmNameList, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.substring(0, o1.indexOf('~')).compareToIgnoreCase(
						o2.substring(0, o2.indexOf('~')));
			}
		});
		if (qdmNameList.size() > 0) {
			List<String> qdmItemList = new ArrayList<String>();
			for (String s : qdmNameList) {
				Node qdm = qdmMap.get(s);
				NamedNodeMap qdmAttribs = qdm.getAttributes();
				
				String qdmString = "\""
						+ qdmAttribs.getNamedItem("datatype").getNodeValue()
						+ ": " + qdmAttribs.getNamedItem("name").getNodeValue()
						+ "\" using \""
						+ qdmAttribs.getNamedItem("name").getNodeValue() + " "
						+ qdmAttribs.getNamedItem("taxonomy").getNodeValue()
						+ " Value Set ("
						+ qdmAttribs.getNamedItem("oid").getNodeValue() + ")\"";
				qdmItemList.add(qdmString);
				
				checkForNegationRationaleAttributes(simpleXMLProcessor,
						mainListElement, qdm, qdmItemList);
			}
			
			Collections.sort(qdmItemList, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o1.substring(0, o1.indexOf(':'))
							.compareToIgnoreCase(
									o2.substring(0, o2.indexOf(':')));
				}
			});
			
			for (String qdmString : qdmItemList) {
				mainListElement.appendElement(HTML_LI).appendText(qdmString);
			}
			List<String> attrNameList = new ArrayList<String>();
			for (Node qdm : attributeMap.values()) {
				NamedNodeMap qdmAttribs = qdm.getAttributes();
				String uuid = qdmAttribs.getNamedItem("uuid").getNodeValue();
				NodeList nodeList = simpleXMLProcessor.findNodeList(simpleXMLProcessor
						.getOriginalDoc(), "//attribute[@qdmUUID=\""
								+ qdmAttribs.getNamedItem("uuid").getNodeValue()
								+ "\"][@name != \"negation rationale\"]");
				String name = "";
				if (nodeList != null) {
					for(int i=0;i<nodeList.getLength();i++){
						name = nodeList.item(i).getAttributes().getNamedItem("name")
								.getNodeValue();
						name = StringUtils.capitalize(name);
						String attrUUID_NAME = uuid + "~" +name;
						if(!attrNameList.contains(attrUUID_NAME)){
							Element listItem = mainListElement.appendElement(HTML_LI);
							listItem.appendText(" Attribute: "
									+ "\""
									+ name
									+ ": "
									+ qdmAttribs.getNamedItem("name").getNodeValue()
									+ "\" using \""
									+ qdmAttribs.getNamedItem("name").getNodeValue()
									+ " "
									+ qdmAttribs.getNamedItem("taxonomy")
									.getNodeValue() + " Value Set ("
									+ qdmAttribs.getNamedItem("oid").getNodeValue()
									+ ")\"");
							attrNameList.add(uuid+"~"+name);
						}
					}
				}
			}
		} else {
			mainListElement.appendElement(HTML_LI).appendText("None");
		}
	}
	
	/**
	 * Check for negation rationale attributes.
	 *
	 * @param simpleXMLProcessor the simple xml processor
	 * @param mainListElement the main list element
	 * @param qdm the qdm
	 * @param itemList the item list
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static void checkForNegationRationaleAttributes(
			XmlProcessor simpleXMLProcessor, Element mainListElement, Node qdm,
			List<String> itemList) throws XPathExpressionException {
		String uuid = qdm.getAttributes().getNamedItem("uuid").getNodeValue();
		
		String xPathString = "//elementRef[@id='" + uuid
				+ "']/attribute[@name='negation rationale'][@qdmUUID]";
		NodeList elementRefList = simpleXMLProcessor.findNodeList(
				simpleXMLProcessor.getOriginalDoc(), xPathString);
		
		List<String> attribUUIDList = new ArrayList<String>();
		
		for (int i = 0; i < elementRefList.getLength(); i++) {
			Node attribNode = elementRefList.item(i);
			String attribUUID = attribNode.getAttributes()
					.getNamedItem("qdmUUID").getNodeValue();
			if (!attribUUIDList.contains(attribUUID)) {
				attribUUIDList.add(attribUUID);
				xPathString = "/measure/elementLookUp/qdm[@uuid='" + attribUUID
						+ "'][@datatype='attribute']";
				Node qdmAttributeNode = simpleXMLProcessor.findNode(
						simpleXMLProcessor.getOriginalDoc(), xPathString);
				
				if (qdmAttributeNode != null) {
					String parentDataType = qdm.getAttributes()
							.getNamedItem("datatype").getNodeValue();
					NamedNodeMap qdmAttribs = qdmAttributeNode.getAttributes();
					
					String negRationalText = "\""
							+ parentDataType.trim()
							+ " not done: "
							+ qdmAttribs.getNamedItem("name").getNodeValue()
							+ "\" using \""
							+ qdmAttribs.getNamedItem("name").getNodeValue()
							+ " "
							+ qdmAttribs.getNamedItem("taxonomy")
							.getNodeValue() + " Value Set ("
							+ qdmAttribs.getNamedItem("oid").getNodeValue()
							+ ")\"";
					
					if (!itemList.contains(negRationalText)) {
						itemList.add(negRationalText);
					}
				}
			}
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
		
		NodeList elements = simpleXMLProcessor.findNodeList(
				simpleXMLProcessor.getOriginalDoc(),
				"/measure/supplementalDataElements/elementRef");
		if (elements.getLength() > 0) {
			Map<String, Node> qdmNodeMap = new HashMap<String, Node>();
			for (int i = 0; i < elements.getLength(); i++) {
				Node node = elements.item(i);
				NamedNodeMap map = node.getAttributes();
				String id = map.getNamedItem("id").getNodeValue();
				Node qdm = simpleXMLProcessor.findNode(
						simpleXMLProcessor.getOriginalDoc(),
						"/measure/elementLookUp/qdm[@uuid='" + id + "']");
				NamedNodeMap qdmMap = qdm.getAttributes();
				qdmNodeMap.put(qdmMap.getNamedItem("datatype").getNodeValue()
						+ ": " + qdmMap.getNamedItem("name").getNodeValue(),
						qdm);
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
		} else {
			mainListElement.appendElement(HTML_LI).appendText("None");
		}
	}
	
	
	/**
	 * Generate risk adjustment variables.
	 *
	 * @param humanReadableHTMLDocument the human readable html document
	 * @param simpleXMLProcessor the simple xml processor
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static void generateRiskAdjustmentVariables(
			Document humanReadableHTMLDocument, XmlProcessor simpleXMLProcessor)
					throws XPathExpressionException {
		Element bodyElement = humanReadableHTMLDocument.body();
		bodyElement
		.append("<h3><a name=\"d1e879\" href=\"#toc\">Risk Adjustment Variables</a></h3>");
		
		Element mainDivElement = bodyElement.appendElement("div");
		Element mainListElement = mainDivElement.appendElement(HTML_UL);
		
		NodeList elements = simpleXMLProcessor.findNodeList(
				simpleXMLProcessor.getOriginalDoc(),
				"/measure/riskAdjustmentVariables/subTreeRef");
		
		if (elements.getLength() > 0) {
			for(int i=0;i<elements.getLength();i++){
				Node childNode = elements.item(i);
				String uuid = childNode.getAttributes().getNamedItem("id").getNodeValue();
				String xpathforSubTree = "/measure/subTreeLookUp/subTree[@uuid='"+ uuid +"']";
				Node subTreeNode = simpleXMLProcessor.findNode(simpleXMLProcessor.getOriginalDoc(),
						xpathforSubTree);
				parseChild(subTreeNode.getFirstChild(), mainListElement, subTreeNode.getParentNode(), simpleXMLProcessor, false);
			}
		} else {
			mainListElement.appendElement(HTML_LI).appendText("None");
		}
	}
	
	/**
	 * Generate qdm variables.
	 *
	 * @param humanReadableHTMLDocument the human readable html document
	 * @param simpleXMLProcessor the simple xml processor
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static void generateQDMVariables(
			Document humanReadableHTMLDocument, XmlProcessor simpleXMLProcessor)
					throws XPathExpressionException {
		
		Element bodyElement = humanReadableHTMLDocument.body();
		bodyElement
		.append("<h3><a name=\"d1e539\" href=\"#toc\">Data Criteria (QDM Variables)</a></h3>");
		
		Element mainDivElement = bodyElement.appendElement("div");
		Element mainListElement = mainDivElement.appendElement(HTML_UL);
		
		NodeList variables = simpleXMLProcessor.findNodeList(
				simpleXMLProcessor.getOriginalDoc(),
				"/measure/subTreeLookUp/subTree[@qdmVariable='true'][not(@instanceOf)]");
		if (variables.getLength() > 0) {
			Node node;
			for (int i = 0; i < variables.getLength(); i++) {
				node = variables.item(i);
				NamedNodeMap map = node.getAttributes();
				String name = map.getNamedItem("displayName").getNodeValue();
				if (name.length() > 0) {
					name = StringUtils.deleteWhitespace(name);
					name = "$" + name;
				}
				Element variableElement = mainListElement
						.appendElement(HTML_LI);
				Element boldNameElement = variableElement.appendElement("b");
				boldNameElement.appendText(name + " = ");
				Element indentListElement = variableElement
						.appendElement(HTML_UL);
				showOnlyVariableName = true;
				parseChild(node, indentListElement, node.getParentNode(),
						simpleXMLProcessor, false);
				showOnlyVariableName = false;
			}
		} else {
			mainListElement.appendElement(HTML_LI).appendText("None");
		}
	}
	
	/**
	 * Generate population criteria human readable.
	 *
	 * @param humanReadableHTMLDocument the human readable html document
	 * @param simpleXMLProcessor the simple xml processor
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static void generatePopulationCriteriaHumanReadable(
			Document humanReadableHTMLDocument, XmlProcessor simpleXMLProcessor)
					throws XPathExpressionException {
		
		Element bodyElement = humanReadableHTMLDocument.body();
		bodyElement
		.append("<h3><a name=\"d1e405\" href=\"#toc\">Population Criteria</a></h3>");
		
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
		
//		for (int i = 0; i < groupNodeList.getLength(); i++) {
//			
//			if (groupNodeList.getLength() > 1) {
//				mainListElement.append("<li style=\"list-style: none;\"><br><b>------ Population Criteria "
//						+ (i + 1) + " ------</b><br><br></li>");
//			}
//			
//			Node groupNode = groupNodeList.item(i);
//			
//			NodeList clauseNodeList = groupNode.getChildNodes();
//			generatePopulationNodes(clauseNodeList, mainListElement,
//					groupNodeList.getLength(), i, simpleXMLProcessor);
//		}
		
		for (Integer key : groupMap.keySet()) {
			if (groupMap.size() > 1) {
				mainListElement.append("<li style=\"list-style: none;\"><br><b>------ Population Criteria "
						+ (key.toString()) + " ------</b><br><br></li>");
			}
			NodeList clauseNodeList = groupMap.get(key).getChildNodes();
			generatePopulationNodes(clauseNodeList, mainListElement,
					groupNodeList.getLength(),key, simpleXMLProcessor);
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
	 */
	private static void generatePopulationNodes(NodeList clauseNodeList,
			Element mainListElement, int totalGroupCount,
			int currentGroupNumber, XmlProcessor simpleXMLProcessor) {
		
		for (String element : POPULATION_NAME_ARRAY) {
			generatePopulationNodes(element, clauseNodeList,
					mainListElement, totalGroupCount, currentGroupNumber,
					simpleXMLProcessor);
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
	 */
	private static void generatePopulationNodes(String populationType,
			NodeList clauseNodeList, Element mainListElement,
			int totalGroupCount, int currentGroupNumber,
			XmlProcessor simpleXMLProcessor) {
		
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
			Element boldNameElement = populationListElement.appendElement("b");
			String populationName = getPopulationName(populationType, true);
			/*
			 * if (totalGroupCount > 1){ populationName += "s " +
			 * (currentGroupNumber+1); }
			 *//*
			 * else{ populationName += "s"; }
			 */
			boldNameElement.appendText(populationName + " =");
			Element childPopulationULElement = populationListElement
					.appendElement(HTML_UL);
			//System.out.println("clauseNodes.size():"+ clauseNodes.size());
			for (int c = 0; c < clauseNodes.size(); c++) {
				Node clauseNode = clauseNodes.get(c);
				Element childPopulationListElement = childPopulationULElement
						.appendElement(HTML_LI);
				Element childBoldNameElement = childPopulationListElement
						.appendElement("b");
				String childPopulationName = getPopulationName(populationType);
				/*
				 * if (totalGroupCount > 1){ childPopulationName += " " +
				 * (currentGroupNumber+1) + "." + (c+1); }else{
				 * childPopulationName += " " + (c+1); }
				 */
				childPopulationName += " " + (c + 1);
				if ("initialPopulation".equalsIgnoreCase(clauseNode
						.getAttributes().getNamedItem("type").getNodeValue())) {
					initialPopulationHash.put(clauseNode.getAttributes()
							.getNamedItem("uuid").getNodeValue(),
							String.valueOf(c + 1));
				}
				String itemCountText = getItemCountText(clauseNode);
				String popassoc = getPopAssoc(clauseNode, simpleXMLProcessor);
				childBoldNameElement.appendText(childPopulationName
						+ (popassoc.length() > 0 ? popassoc : "")
						+ (itemCountText.length() > 0 ? itemCountText : "")
						+ " =");
				if(childPopulationName.startsWith("Measure Observation")){
					childPopulationListElement = childPopulationListElement.appendElement(HTML_UL);
				}
				parseAndBuildHTML(simpleXMLProcessor,
						childPopulationListElement, clauseNode, c + 1);
			}
		} else if (clauseNodes.size() == 1) {
			Element populationListElement = mainListElement
					.appendElement(HTML_LI);
			Element boldNameElement = populationListElement.appendElement("b");
			String populationName = getPopulationName(populationType);
			
			// if (totalGroupCount > 1){
			// populationName += " " + (currentGroupNumber+1);
			// }
			if ("initialPopulation".equalsIgnoreCase(clauseNodes.get(0)
					.getAttributes().getNamedItem("type").getNodeValue())) {
				initialPopulationHash.put(clauseNodes.get(0).getAttributes()
						.getNamedItem("uuid").getNodeValue(), "-1");
			}
			String itemCountText = getItemCountText(clauseNodes.get(0));
			boldNameElement.appendText(populationName
					+ (itemCountText.length() > 0 ? itemCountText : "") + " =");
			if(populationName.startsWith("Measure Observation")){
				populationListElement = populationListElement.appendElement(HTML_UL);
			}
			parseAndBuildHTML(simpleXMLProcessor, populationListElement,
					clauseNodes.get(0), 1);
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
			e.printStackTrace();
		}
		return stringAssoc;
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
	 * Resolve sub trees in populations.
	 *
	 * @param simpleXmlStr the simple xml str
	 * @return the xml processor
	 */
	private static XmlProcessor resolveSubTreesInPopulations(String simpleXmlStr) {
		XmlProcessor simpleXMLProcessor = new XmlProcessor(simpleXmlStr);
		
		try {
			NodeList clauseNodeList = simpleXMLProcessor.findNodeList(
					simpleXMLProcessor.getOriginalDoc(),
					"/measure/measureGrouping/group/clause");
			Object[][] nodeArr = new Object[clauseNodeList.getLength()][2];
			for (int i = 0; i < clauseNodeList.getLength(); i++) {
				Node clauseNode = clauseNodeList.item(i);
				String clauseNodeXMLString = simpleXMLProcessor
						.transform(clauseNode);
				
				/*System.out.println("");
				System.out.println("clauseNode XML String:"
						+ clauseNodeXMLString);
				System.out.println("");*/
				
				XmlProcessor clauseXMLProcessor = new XmlProcessor(
						clauseNodeXMLString);
				clauseXMLProcessor = expandSubTreesAndImportQDMs(
						clauseXMLProcessor, simpleXMLProcessor, false);
				if(clauseXMLProcessor == null){
					return null;
				}
				Node expandedClauseNode = clauseXMLProcessor.getOriginalDoc()
						.getFirstChild();
				
				nodeArr[i][0] = clauseNode;
				nodeArr[i][1] = expandedClauseNode;
			}
			
			for (Object[] element : nodeArr) {
				Node clauseNode = (Node) element[0];
				Node expandedClauseNode = (Node) element[1];
				
				Node importedClauseNode = simpleXMLProcessor.getOriginalDoc()
						.importNode(expandedClauseNode, true);
				Node parentNode = clauseNode.getParentNode();
				parentNode.replaceChild(importedClauseNode, clauseNode);
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return simpleXMLProcessor;
	}
	
	/**
	 * Because of "QDM Variable with Occurrences", we might have some subTree's with subTreeRef's in subTreeLookUp.
	 * This is because these subTree's aren't directly referred to in the clause logic, but their occurrence(s)
	 * are.
	 * This method will look for subTreeRef's in subTree and resolve them to subTree, so that proper human readable
	 * is generated.
	 *
	 * @param simpleXMLProcessor the simple xml processor
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static void resolveRemainingSubTreeRefs(
			XmlProcessor simpleXMLProcessor) throws XPathExpressionException {
		
		NodeList remainingSubTreeRefs = simpleXMLProcessor.findNodeList(
				simpleXMLProcessor.getOriginalDoc(),
				"/measure/subTreeLookUp//subTreeRef");
		
		for(int i=0;i<remainingSubTreeRefs.getLength();i++){
			Node subTreeRefNode = remainingSubTreeRefs.item(i);
			String subTreeNodeID = subTreeRefNode.getAttributes().getNamedItem("id").getNodeValue();
			Node subTreeNode = simpleXMLProcessor.findNode(simpleXMLProcessor.getOriginalDoc(), "/measure/subTreeLookUp/subTree[@uuid='"+subTreeNodeID+"']");
			if(subTreeNode != null){
				Node clonedSubTreeNode = subTreeNode.cloneNode(true);
				Node subTreeRefParentNode = subTreeRefNode.getParentNode();
				
				Node newNode = clonedSubTreeNode;
				String qdmVariable = clonedSubTreeNode.getAttributes().getNamedItem("qdmVariable").getNodeValue();
				if (clonedSubTreeNode.hasChildNodes() && "false".equalsIgnoreCase(qdmVariable)) {
					newNode = clonedSubTreeNode.getFirstChild();
				}
				subTreeRefParentNode.replaceChild(newNode, subTreeRefNode);
			}
		}
	}
	
	/**
	 * Format date.
	 *
	 * @param date the date
	 * @return the string
	 */
	private static String formatDate(String date){
		String dateString = "";
		if(!date.contains("/")){
			String year = date.substring(0, 4);
			String month = date.substring(4, 6);
			String dt = date.substring(6, 8);
			if((year.length() != 4) || (year.toLowerCase().indexOf("x") > -1)){
				year = "0000";
			}
			dateString = month.concat("/") + dt.concat("/") + year;
		} else {
			return date;
		}
		return dateString;
	}
	
	
	/**
	 * Check for satisfies parent node.
	 *
	 * @param item the item
	 * @return true, if successful
	 */
	private static boolean checkForSatisfiesParentNode(Node item){
		if(item == null){
			return false;
		}
		String nodeName = item.getNodeName();
		switch(nodeName){
			case "functionalOp":
				if ((item.getAttributes().getNamedItem("type") != null)
						&& item.getAttributes()
						.getNamedItem("type").getNodeValue()
						.contains("SATISFIES")) {
					return true;
				}
				break;
			case "relationalOp":
				return checkForSatisfiesParentNode(item.getParentNode());
			case "setOp":
				return checkForSatisfiesParentNode(item.getParentNode());
			case "subTree":
				return false;
			default://do nothing
				break;
		}
		
		return checkForSatisfiesParentNode(item.getParentNode());
	}
	
}
