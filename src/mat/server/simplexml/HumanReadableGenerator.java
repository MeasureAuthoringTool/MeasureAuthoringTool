package mat.server.simplexml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import mat.server.util.XmlProcessor;
import mat.shared.ConstantMessages;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class HumanReadableGenerator {

	private static final String ELEMENT_LOOK_UP = "elementLookUp";
	private static final String FUNCTIONAL_OP = "functionalOp";
	private static final String DISPLAY_NAME = "displayName";
	private static final String ELEMENT_REF = "elementRef";
	private static final String RELATIONAL_OP = "relationalOp";
	private static final String HTML_LI = "li";
	private static final String HTML_UL = "ul";
	private static final String SET_OP = "setOp";
	private static final String SUB_TREE = "subTree";
	private static final String COMMENT = "comment";
	private static final String LOGICAL_OP = "logicalOp";
	private static final String[] popNameArray = {"initialPopulation","denominator","denominatorExclusions","numerator","numeratorExclusions",
		"denominatorExceptions","measurePopulation","measurePopulationExclusions","measureObservation","stratum"};
	
	private static Boolean showOnlyVariableName = false;
	private static String lhsID;
	private static Map<String, String> initialPopulationHash = new HashMap<String, String>();
	public static String generateHTMLForPopulationOrSubtree(String measureId, String subXML,
			String measureXML) {
		org.jsoup.nodes.Document htmlDocument = null;
		//replace the <subTree> tags in 'populationSubXML' with the appropriate subTree tags from 'simpleXML'.
		try {
			System.out.println("Original subXML:"+subXML);
			XmlProcessor populationOrSubtreeXMLProcessor = expandSubTreesAndImportQDMs(subXML, measureXML, true);
			
			if(populationOrSubtreeXMLProcessor == null){
				htmlDocument = createBaseHumanReadableDocument();
				Element bodyElement = htmlDocument.body();
				Element mainDivElement = bodyElement.appendElement("div");
				Element mainListElement = mainDivElement.appendElement(HTML_UL);
				Element populationListElement = mainListElement.appendElement(HTML_LI);
				populationListElement.appendText("Human readable encountered problems. " +
						"Most likely you have included a clause with clause which is causing an infinite loop.");
				return htmlDocument.toString();
			}
			
			boolean isPopulation = checkIfPopulation(populationOrSubtreeXMLProcessor);
			String name = getPopulationOrSubtreeName(populationOrSubtreeXMLProcessor,isPopulation);
			
			htmlDocument = createBaseHumanReadableDocument();
			Element bodyElement = htmlDocument.body();
			Element mainDivElement = bodyElement.appendElement("div");
			Element mainListElement = mainDivElement.appendElement(HTML_UL);
			Element populationOrSubtreeListElement = mainListElement.appendElement(HTML_LI);
			
			Element boldNameElement = populationOrSubtreeListElement.appendElement("b");
			boldNameElement.appendText(name + " =");
			
			if(!isPopulation){
				populationOrSubtreeListElement.appendElement("br");
				populationOrSubtreeListElement = populationOrSubtreeListElement.appendElement(HTML_UL).appendElement(HTML_LI);
			}
			
			parseAndBuildHTML(populationOrSubtreeXMLProcessor, populationOrSubtreeListElement);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(htmlDocument);
		String returnHTML = "";
		if(htmlDocument != null){
			returnHTML = htmlDocument.toString();
		}
		return returnHTML;
	}

	private static boolean checkIfPopulation(XmlProcessor populationOrSubtreeXMLProcessor) {
		boolean returnFlag = false;
		Node  node = populationOrSubtreeXMLProcessor.getOriginalDoc().getFirstChild();
		String nodeName = node.getNodeName();
		if("clause".equals(nodeName)){
			returnFlag = true;
		}
		return returnFlag;
	}

	private static XmlProcessor expandSubTreesAndImportQDMs(String subXML, String measureXML,boolean isImportElementLookup) throws XPathExpressionException {
		
		XmlProcessor populationOrSubtreeXMLProcessor = new XmlProcessor(subXML);
		XmlProcessor measureXMLProcessor = new XmlProcessor(measureXML);
		
		return expandSubTreesAndImportQDMs(populationOrSubtreeXMLProcessor, measureXMLProcessor, isImportElementLookup);
	}
	
	private static XmlProcessor expandSubTreesAndImportQDMs(XmlProcessor populationOrSubtreeXMLProcessor, XmlProcessor measureXMLProcessor, boolean isImportElementLookup) throws XPathExpressionException {
		//find all <subTreeRef> tags in 'populationSubXML'
		NodeList subTreeRefNodeList = populationOrSubtreeXMLProcessor.findNodeList(populationOrSubtreeXMLProcessor.getOriginalDoc(), "//subTreeRef");
		
		if(subTreeRefNodeList.getLength() > 0){
			
			//For each <subTreeRef> node replace it by actual <subTree> node from 'simpleXML' 
			for(int i=0;i<subTreeRefNodeList.getLength();i++){
				Node subTreeRefNode = subTreeRefNodeList.item(i);
				Node firstChildNode = subTreeRefNode.getFirstChild();
				Node commentNode = null;
				if(firstChildNode != null && "comment".equals(firstChildNode.getNodeName())){
					commentNode = firstChildNode.cloneNode(true);
				}
				String subTreeId = subTreeRefNode.getAttributes().getNamedItem("id").getNodeValue();
				System.out.println("subTreeId:"+subTreeId);
				
				Node subTreeNode = resolveMainSubTreeNode(measureXMLProcessor, 
						subTreeId);
				
				if(subTreeNode != null){
					replaceSubTreeNode(populationOrSubtreeXMLProcessor,
							subTreeRefNode, commentNode, subTreeNode);
				}else{
					return null;
				}
			}
		}
		if(isImportElementLookup){
			//import <elementLookUp> tag to populationOrSubtreeXMLProcessor
			Node elementLookUpNode = measureXMLProcessor.findNode(measureXMLProcessor.getOriginalDoc(), "//elementLookUp");
			Node importedElementLookUpNode = populationOrSubtreeXMLProcessor.getOriginalDoc().importNode(elementLookUpNode, true);
			populationOrSubtreeXMLProcessor.getOriginalDoc().getFirstChild().appendChild(importedElementLookUpNode);
		}
		
		//import 'measureDetails'
		Node measureDetailsNode = measureXMLProcessor.findNode(measureXMLProcessor.getOriginalDoc(), "//measureDetails");
		Node importedMeasureDetailsNode = populationOrSubtreeXMLProcessor.getOriginalDoc().importNode(measureDetailsNode, true);
		populationOrSubtreeXMLProcessor.getOriginalDoc().getFirstChild().appendChild(importedMeasureDetailsNode);
		
		System.out.println("Inflated popualtion tree: "+populationOrSubtreeXMLProcessor.transform(populationOrSubtreeXMLProcessor.getOriginalDoc()));	
		return populationOrSubtreeXMLProcessor;
	}

	/**
	 * @param measureXMLProcessor
	 * @param subTreeId
	 * @return
	 * @throws XPathExpressionException
	 */
	private static Node resolveMainSubTreeNode(XmlProcessor measureXMLProcessor,
			String subTreeId) throws XPathExpressionException {
		Node subTreeNode = measureXMLProcessor.findNode(measureXMLProcessor.getOriginalDoc(), "/measure/subTreeLookUp/subTree[@uuid='"+subTreeId+"']");
		//Node subTreeNodeClone = subTreeNode.cloneNode(true);
		//find all <subTreeRef> tags in 'subTreeNode' 
		NodeList subTreeRefNodeList = measureXMLProcessor.findNodeList(measureXMLProcessor.getOriginalDoc(), 
				"/measure/subTreeLookUp/subTree[@uuid='"+subTreeId+"']//subTreeRef");
		
		//resolve for each subTreeRef with subTree (clause within clause)
		for(int i=0;i<subTreeRefNodeList.getLength();i++){
			Node subTreeRefNode = subTreeRefNodeList.item(i);
			//Node subTreeRefNodeClone = subTreeRefNode.cloneNode(true);
			List<String> childSubTreeRefList = new ArrayList<String>();
			childSubTreeRefList.add(subTreeId);
			if(!resolveChildSubTreeNode(measureXMLProcessor,subTreeRefNode,childSubTreeRefList)){
				return null;
			}
//			replaceSubTreeNode(measureXMLProcessor, subTreeRefNode, null, subTreeNode );
		}
		return subTreeNode;
	}

	private static boolean resolveChildSubTreeNode(
			XmlProcessor measureXMLProcessor, Node subTreeRefNode,
			List<String> childSubTreeRefList) throws XPathExpressionException {
		
		String subTreeId = subTreeRefNode.getAttributes().getNamedItem("id").getNodeValue();
		System.out.println("sub subTreeId:"+subTreeId);
		if(!childSubTreeRefList.contains(subTreeId)){
			childSubTreeRefList.add(subTreeId);
			Node subTreeNode = measureXMLProcessor.findNode(measureXMLProcessor.getOriginalDoc(), "/measure/subTreeLookUp/subTree[@uuid='"+subTreeId+"']");
			//Node subTreeNodeClone = subTreeNode.cloneNode(true);
			
			//find all <subTreeRef> tags in 'subTreeNode' 
			NodeList subTreeRefNodeList = measureXMLProcessor.findNodeList(measureXMLProcessor.getOriginalDoc(), 
					"/measure/subTreeLookUp/subTree[@uuid='"+subTreeId+"']//subTreeRef");
			for(int i=0;i<subTreeRefNodeList.getLength();i++){
				Node childSubTreeRefNode = subTreeRefNodeList.item(i);
				//Node childSubTreeRefNodeClone = childSubTreeRefNode.cloneNode(true);
				if(!resolveChildSubTreeNode(measureXMLProcessor,childSubTreeRefNode,childSubTreeRefList)){
					return false;
				}
				//replaceSubTreeNode(measureXMLProcessor, childSubTreeRefNode, null, subTreeNode);
			}
			replaceSubTreeNode(measureXMLProcessor, subTreeRefNode, null, subTreeNode );
		}else{
			System.out.println("Found a chain of Clauses. Abort Human readable generation.");
			System.out.println(childSubTreeRefList);
			return false;
		}
		childSubTreeRefList.remove(subTreeId);
		return true;
	}

	/**
	 * @param xmlProcessor
	 * @param subTreeRefNode
	 * @param commentNode
	 * @param subTreeNode
	 */
	private static void replaceSubTreeNode(
			XmlProcessor xmlProcessor, Node subTreeRefNode,
			Node commentNode, Node subTreeNode) {
		//replace the 'subTreeRefNode' with 'subTreeNode'
		Node subTreeRefNodeParent = subTreeRefNode.getParentNode();								
		Node subTreeNodeImportedClone = xmlProcessor.getOriginalDoc().importNode(subTreeNode, true);
		if(commentNode != null){
			subTreeNodeImportedClone.insertBefore(commentNode, subTreeNodeImportedClone.getFirstChild());
		}
		subTreeRefNodeParent.replaceChild(subTreeNodeImportedClone, subTreeRefNode);
	}
	
	private static String getPopulationOrSubtreeName(
			XmlProcessor populationOrSubtreeXMLProcessor, boolean isPopulation) {
		String name = "";
		try {
			if(isPopulation){
				name = populationOrSubtreeXMLProcessor.findNode(populationOrSubtreeXMLProcessor.getOriginalDoc(), "//clause/@displayName").getNodeValue();
			}else{
				name = populationOrSubtreeXMLProcessor.findNode(populationOrSubtreeXMLProcessor.getOriginalDoc(), "//subTree/@displayName").getNodeValue();
			}
		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name;
	}
	
	private static void parseAndBuildHTML(
			XmlProcessor populationOrSubtreeXMLProcessor, Element populationOrSubtreeListElement) {
		
		try {
			Node rootNode = populationOrSubtreeXMLProcessor.getOriginalDoc().getFirstChild();
			parseAndBuildHTML(populationOrSubtreeXMLProcessor, populationOrSubtreeListElement, rootNode,0);			
		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	private static void parseAndBuildHTML(
			XmlProcessor populationOrSubtreeXMLProcessor, Element populationOrSubtreeListElement, Node clauseNode, int currentGroupNumber) {
		
		try {
			NodeList childNodes = clauseNode.getChildNodes();
			System.out.println("NAME: " + clauseNode.getAttributes().getNamedItem("displayName").getNodeValue());
			String scoring = populationOrSubtreeXMLProcessor.findNode(populationOrSubtreeXMLProcessor.getOriginalDoc(), "//measureDetails/scoring").getTextContent();
			System.out.println("CHILD NODE LENGTH: " + childNodes.getLength());
			if(childNodes.getLength() == 1){
				displayNone(populationOrSubtreeListElement.appendElement(HTML_UL),populationOrSubtreeXMLProcessor,clauseNode);
			}
			String parentName = "";
			if(clauseNode.getAttributes().getNamedItem("type") != null){
				parentName = clauseNode.getAttributes().getNamedItem("type").getNodeValue();
				if(("denominator".equalsIgnoreCase(parentName) || "measurePopulation".equalsIgnoreCase(parentName) ||
						("numerator".equalsIgnoreCase(parentName) && "ratio".equalsIgnoreCase(scoring))) && !"measureDetails".equalsIgnoreCase(clauseNode.getNodeName())){
					
					displayInitialPop(populationOrSubtreeListElement,populationOrSubtreeXMLProcessor,clauseNode,currentGroupNumber);
				}
			} 
			for(int i = 0;i < childNodes.getLength(); i++){		
				parseChild(childNodes.item(i),populationOrSubtreeListElement,childNodes.item(i).getParentNode(),populationOrSubtreeXMLProcessor,false);
			}			
		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	private static void parseChild(Node item, Element parentListElement, Node parentNode, XmlProcessor populationOrSubtreeXMLProcessor, boolean satisfiesAnyAll) {
		String nodeName = item.getNodeName();
		System.out.println("parseChild:"+nodeName);
		if(LOGICAL_OP.equals(nodeName)){
			
			String nodeDisplayName = item.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue().toUpperCase();
			String parentNodeDisplayName = parentNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue().toUpperCase();
			//set the Flag is we have AND -> AND NOT or OR -> OR NOT
			boolean isNestedNot = (nodeDisplayName.equals(parentNodeDisplayName + " NOT"));
						
			if(LOGICAL_OP.equals(parentNode.getNodeName()) ){
				if(LOGICAL_OP.equals(parentNode.getNodeName())){
					if(isNestedNot){
						//liElement.appendText(nodeDisplayName+":");
					}else{
						Element liElement = parentListElement.appendElement(HTML_LI);
						liElement.appendText(parentNodeDisplayName+":");
					}
				}
			}
			Element ulElement = null;
			if(isNestedNot){
				ulElement = parentListElement;
			}else{
				ulElement = parentListElement.appendElement(HTML_UL);
			}
			NodeList childNodes = item.getChildNodes();
			if(childNodes.getLength() == 0){
				displayNone(ulElement,populationOrSubtreeXMLProcessor,parentNode);
				//ulElement.appendElement(HTML_LI).appendText("None");
			}
			for (int i=0; i< childNodes.getLength(); i++){
				parseChild(childNodes.item(i), ulElement, item, populationOrSubtreeXMLProcessor,satisfiesAnyAll);				
			}
		}else if(COMMENT.equals(nodeName)){
			String commentValue = item.getTextContent();
			if(commentValue != null && commentValue.trim().length() > 0){
				Element liElement = parentListElement.appendElement(HTML_LI);
				liElement.attr("style","list-style-type: none");
				Element italicElement = liElement.appendElement("i");
				italicElement.appendText("# "+item.getTextContent());
			}
			if(item.getParentNode().getChildNodes().getLength() == 1 && "AND".equalsIgnoreCase(item.getParentNode().getAttributes().getNamedItem("displayName").getNodeValue())){
				//Element ulElement = parentListElement.appendElement(HTML_UL);
				//Element list = parentListElement.appendElement(HTML_LI);
				displayNone(parentListElement,populationOrSubtreeXMLProcessor,parentNode);
			}
			return;
		}else if(SUB_TREE.equals(nodeName)){
			NamedNodeMap map = item.getAttributes();
			if(map.getNamedItem("qdmVariable") != null && "true".equalsIgnoreCase(map.getNamedItem("qdmVariable").getNodeValue()) && showOnlyVariableName == false){
				if(parentListElement.nodeName().equals(HTML_UL)){
					parentListElement = parentListElement.appendElement(HTML_LI);
				}
				if(LOGICAL_OP.equals(parentNode.getNodeName()) ){
					parentListElement.appendText(getNodeText(parentNode, populationOrSubtreeXMLProcessor));
				}

				String name = StringUtils.deleteWhitespace(map.getNamedItem("displayName").getNodeValue());
				parentListElement.appendText("$" + name + " ");
			}
			else{
				showOnlyVariableName = false;
				NodeList childNodes = item.getChildNodes();
				for (int i=0; i< childNodes.getLength(); i++){
					parseChild(childNodes.item(i), parentListElement,parentNode, populationOrSubtreeXMLProcessor,satisfiesAnyAll);				
				}
			}
		}else if(SET_OP.equals(nodeName)){
			//Element liElement = parentListElement.appendElement(HTML_LI);
			if(LOGICAL_OP.equals(parentNode.getNodeName()) ){
				Element liElement = parentListElement.appendElement(HTML_LI);
				if(LOGICAL_OP.equals(parentNode.getNodeName())){
					liElement.appendText(getNodeText(parentNode, populationOrSubtreeXMLProcessor));
					parentListElement = liElement.appendElement(HTML_UL);
				}
			}
			//Element ulElement = liElement.appendElement(HTML_UL);
			if(parentListElement.nodeName().equals(HTML_UL)){
				parentListElement.appendElement(HTML_LI).appendText(getNodeText(item, populationOrSubtreeXMLProcessor));
			}else{
				parentListElement.appendText(getNodeText(item, populationOrSubtreeXMLProcessor));
			}
			Element ulElement = parentListElement.appendElement(HTML_UL);
			NodeList childNodes = item.getChildNodes();
			for (int i=0; i< childNodes.getLength(); i++){
				parseChild(childNodes.item(i), ulElement,item, populationOrSubtreeXMLProcessor,satisfiesAnyAll);				
			}
		}else if(RELATIONAL_OP.equals(nodeName)){
			if(LOGICAL_OP.equals(parentNode.getNodeName()) || SET_OP.equals(parentNode.getNodeName())){
				Element liElement = parentListElement.appendElement(HTML_LI);
				//liElement.appendText(getNodeText(parentNode, populationOrSubtreeXMLProcessor));
				if(LOGICAL_OP.equals(parentNode.getNodeName())){
					liElement.appendText(getNodeText(parentNode, populationOrSubtreeXMLProcessor));
				}
				getRelationalOpText(item, liElement, populationOrSubtreeXMLProcessor,satisfiesAnyAll);
			}else{
				getRelationalOpText(item, parentListElement, populationOrSubtreeXMLProcessor,satisfiesAnyAll);
				/**
				 * A relationalOp can have 2 children. First evaluate the LHS child, then add the name of the relationalOp and finally
				 * evaluate the RHS child.
				 */
				/*NodeList childNodes = item.getChildNodes();
				if(childNodes.getLength() == 2){
					parseChild(childNodes.item(0),parentListElement,item);
					parentListElement.appendText(item.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue()+" ");
					parseChild(childNodes.item(1),parentListElement,item);
				}*/
			}
			
		}else if(ELEMENT_REF.equals(nodeName)){
			if(satisfiesAnyAll && lhsID != null && lhsID.equalsIgnoreCase(item.getAttributes().getNamedItem("id").getNodeValue())){
				if(item.hasChildNodes()){
					//TODO: Don't know how this will work with "negation rationale" attribute. Need to ask Nicole.
					parentListElement.appendText(getAttributeText(item.getFirstChild(),populationOrSubtreeXMLProcessor)+ " ");
				}
			}else{
				if(LOGICAL_OP.equals(parentNode.getNodeName()) || SET_OP.equals(parentNode.getNodeName())){
					Element liElement = parentListElement.appendElement(HTML_LI);
					//liElement.appendText(getNodeText(parentNode, populationOrSubtreeXMLProcessor)+getNodeText(item, populationOrSubtreeXMLProcessor));
					if(LOGICAL_OP.equals(parentNode.getNodeName())){
						liElement.appendText(getNodeText(parentNode, populationOrSubtreeXMLProcessor)+getNodeText(item, populationOrSubtreeXMLProcessor));
					}else{
						liElement.appendText(getNodeText(item, populationOrSubtreeXMLProcessor));
					}
				}else{
					parentListElement.appendText(getNodeText(item,populationOrSubtreeXMLProcessor));
				}
			}
		}else if(FUNCTIONAL_OP.equals(nodeName)){
			if(LOGICAL_OP.equals(parentNode.getNodeName()) || SET_OP.equals(parentNode.getNodeName())){
				Element liElement = parentListElement.appendElement(HTML_LI);
				//liElement.appendText(" "+getNodeText(parentNode, populationOrSubtreeXMLProcessor));
				if(LOGICAL_OP.equals(parentNode.getNodeName())){
					liElement.appendText(" "+getNodeText(parentNode, populationOrSubtreeXMLProcessor));
				}
				if(item.getAttributes().getNamedItem("type").getNodeValue().contains("SATISFIES")){
					createSatisfies(item,liElement,populationOrSubtreeXMLProcessor);
				}
				else{
				liElement.appendText(getFunctionText(item));
				NodeList childNodes = item.getChildNodes();
				if(childNodes.getLength() == 1 && ELEMENT_REF.equals(childNodes.item(0).getNodeName())){
					parseChild(childNodes.item(0), liElement,item, populationOrSubtreeXMLProcessor,satisfiesAnyAll);
				}else{
					for (int i=0; i< childNodes.getLength(); i++){
						parseChild(childNodes.item(i),liElement,item, populationOrSubtreeXMLProcessor,satisfiesAnyAll);				
					}
				}
				}
			}else{
				if(item.getAttributes().getNamedItem("type").getNodeValue().contains("SATISFIES")){
					createSatisfies(item,parentListElement,populationOrSubtreeXMLProcessor);
				}
				else{
					parentListElement.appendText(getFunctionText(item));
					NodeList childNodes = item.getChildNodes();
					if(childNodes.getLength() == 1 && ELEMENT_REF.equals(childNodes.item(0).getNodeName())){
						parseChild(childNodes.item(0), parentListElement,item, populationOrSubtreeXMLProcessor,satisfiesAnyAll);
					}else{
						Element ulElement = parentListElement.appendElement(HTML_UL);
						for (int i=0; i< childNodes.getLength(); i++){
							parseChild(childNodes.item(i), ulElement.appendElement(HTML_LI),item, populationOrSubtreeXMLProcessor,satisfiesAnyAll);				
						}
					}
				}
			}
		}else if(ELEMENT_LOOK_UP.equals(nodeName) || "itemCount".equals(nodeName) || "measureDetails".equals(nodeName)){
			//ignore
		}
		else {
			Element liElement = parentListElement.appendElement(HTML_LI);
			
			if(LOGICAL_OP.equals(parentNode.getNodeName()) ){
				//liElement.appendText(" "+getNodeText(parentNode, populationOrSubtreeXMLProcessor));
				if(LOGICAL_OP.equals(parentNode.getNodeName())){
					liElement.appendText(" "+getNodeText(parentNode, populationOrSubtreeXMLProcessor));
				}
			}
			liElement.appendText(item.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue() + " ");
		}
	}

	private static void createSatisfies(Node item, Element liElement, XmlProcessor populationOrSubtreeXMLProcessor){
		Node lhs = item.getFirstChild();
		if("elementRef".equalsIgnoreCase(lhs.getNodeName())){
			//Element ulElement = parentListElement.appendElement(HTML_LI);
			parseChild(lhs, liElement,item, populationOrSubtreeXMLProcessor,false);
			liElement.appendText(" " + item.getAttributes().getNamedItem("displayName").getNodeValue().toLowerCase());
			lhsID = lhs.getAttributes().getNamedItem("id").getNodeValue();
			NodeList childNodes = item.getChildNodes();
			if(childNodes.getLength() > 1){
				liElement = liElement.appendElement(HTML_UL);
				for(int i = 1; i<childNodes.getLength();i++){
					parseChild(childNodes.item(i), liElement.appendElement(HTML_LI),item, populationOrSubtreeXMLProcessor,true);
				}
			}
		}
	}
	
	private static void displayNone(Element list,
			XmlProcessor populationOrSubtreeXMLProcessor, Node parentNode) {
		try {
			String scoring = populationOrSubtreeXMLProcessor.findNode(populationOrSubtreeXMLProcessor.getOriginalDoc(), "//measureDetails/scoring").getTextContent();
			String type = parentNode.getAttributes().getNamedItem("type").getNodeValue();
			if(("proportion".equalsIgnoreCase(scoring)&& !"denominator".equalsIgnoreCase(type)) ||
					("ratio".equalsIgnoreCase(scoring) && !"denominator".equalsIgnoreCase(type) && !"numerator".equalsIgnoreCase(type))|| 
					("continuous Variable".equalsIgnoreCase(scoring) && !"measurePopulation".equalsIgnoreCase(type)) ||
					("cohort".equalsIgnoreCase(scoring))){
				
				list.appendElement(HTML_LI).appendText("None");
			}
		}catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	private static void displayInitialPop(Element populationOrSubtreeListElement, XmlProcessor populationOrSubtreeXMLProcessor, Node clause, int loop) {
		
		Element listStart = populationOrSubtreeListElement.appendElement(HTML_UL);
		Element list = listStart.appendElement(HTML_LI);
		try {
			Node assocPop = clause.getAttributes().getNamedItem("associatedPopulationUUID");
			if(assocPop != null){
				Node display = populationOrSubtreeXMLProcessor.findNode(populationOrSubtreeXMLProcessor.getOriginalDoc(), "//measure//measureGrouping//group/clause[@uuid = \""+assocPop.getNodeValue()+"\"]");
				String name = display.getAttributes().getNamedItem("displayName").getNodeValue();
				String number =initialPopulationHash.get(display.getAttributes().getNamedItem("uuid").getNodeValue());
				String lastNum = name.substring(name.length()-1);
				if(!("-1".equals(number))){
					name = name.replace(lastNum, number);
				}else{
					name = (name.substring(0, name.length()-1)).trim();
				}
				list.appendText("AND: "+name);
			}else{
				list.appendText("AND: Initial Population");
			}
		}catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @param item
	 * @param liElement
	 * @param populationOrSubtreeXMLProcessor 
	 */
	private static void getRelationalOpText(Node item, Element liElement, XmlProcessor populationOrSubtreeXMLProcessor,boolean satisfiesAnyAll) {
		/**
		 * A relationalOp can have 2 children. First evaluate the LHS child, then add the name of the relationalOp and finally
		 * evaluate the RHS child.
		 */
		NodeList childNodes = item.getChildNodes();
		
		if(childNodes.getLength() == 2){
			
			Element newLiElement = liElement;
			String name = item.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue().toLowerCase()+" ";
			
			if(item.getAttributes().getNamedItem("unit") != null){
				name = name.replaceFirst(item.getAttributes().getNamedItem("unit").getNodeValue(), getUnitString(item));
			}
			
			boolean isChild1QDMOrVariable = checkIfElementRefOrQDMVariable(childNodes.item(0));
			boolean isChild2QDMOrVariable = checkIfElementRefOrQDMVariable(childNodes.item(1));
			
			if(isParentNodeName(item,LOGICAL_OP) && !isChild1QDMOrVariable){
				newLiElement = liElement.appendElement(HTML_UL).appendElement(HTML_LI);
			}
			parseChild(childNodes.item(0),newLiElement,item, populationOrSubtreeXMLProcessor,satisfiesAnyAll);
			
			if(!newLiElement.children().isEmpty()){
				Element firstElement = newLiElement.children().first();
				if(HTML_UL.equals(firstElement.nodeName())){
					newLiElement = firstElement.appendElement(HTML_LI);
				}
			}
			
			if(!isChild2QDMOrVariable){
				newLiElement.appendText(name);
				newLiElement = newLiElement.appendElement(HTML_UL).appendElement(HTML_LI);
			}else{
				if(!isChild1QDMOrVariable){
					//newLiElement = newLiElement.appendElement(HTML_LI);
					newLiElement.appendText(name);
				}else{
					newLiElement.appendText(name);
				}
			}
			parseChild(childNodes.item(1),newLiElement,item, populationOrSubtreeXMLProcessor,false);
		}
	}
	
	/**
	 * Checks if the Node is a "elementRef" node or a "subTree" node with 
	 * a "qdmVariable" attribute whose value is "true".
	 * If Yes, returns true, else returns false.
	 * @param node
	 * @return
	 */
	private static boolean checkIfElementRefOrQDMVariable(Node node){
		boolean retValue = false;
		String nodeName = node.getNodeName();
		
		if(ELEMENT_REF.equals(nodeName)){
			retValue = true;
		}else if(SUB_TREE.equals(nodeName)){
			Node attributeNode = node.getAttributes().getNamedItem("qdmVariable");
			if(attributeNode != null){
				if("true".equals(attributeNode.getNodeValue())){
					retValue = true;
				}
			}
		}
		else if(LOGICAL_OP.equals(nodeName)){
			retValue = true;
		}
		
		return retValue;
	}

	private static boolean isParentNodeName(Node item, String parentNodeName) {
		Node parentNode = item.getParentNode();
		if(parentNode != null && SUB_TREE.equals(parentNode.getNodeName())){
			parentNode = parentNode.getParentNode();
		}
		
		if(parentNode != null){
			if(parentNodeName.equals(parentNode.getNodeName())){
				return true;
			}
		}
		return false;
	}

	/**
	 * This method is used to get the correct text to add to human readable depending on the 
	 * type of node. 
	 * @param node
	 * @param populationOrSubtreeXMLProcessor 
	 */
	private static String getNodeText(Node node, XmlProcessor populationOrSubtreeXMLProcessor) {
		String nodeName = node.getNodeName();
		String name = "";
		if(LOGICAL_OP.equals(nodeName)){
		    name = node.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue().toUpperCase();
			name += ": ";
		}else if(SET_OP.equals(nodeName)){
			name = node.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue().toLowerCase();
			name = StringUtils.capitalize(name);
			name += " of: ";
		}else if(ELEMENT_REF.equals(nodeName)){
			name = node.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
			if(name.endsWith(" : Timing Element")){
				name = name.substring(0,name.indexOf(" : Timing Element"));
			}else if(!name.endsWith(" : Patient Characteristic Birthdate") &&
					!name.endsWith(" : Patient Characteristic Expired")) {
				String[] nameArr = name.split(":");
	
				if(nameArr.length == 2){
					name = nameArr[1].trim()+": "+nameArr[0].trim();
				}
				if(nameArr.length == 3){
					name = nameArr[1].trim()+": "+nameArr[2].trim()+": "+nameArr[0].trim();
				}
				// Move Occurrence of to the beginning of the name 
				if(name.contains("Occurrence") && name.contains("of")){
					int occurLoc = name.indexOf("Occurrence", 0);
					int ofLoc = name.indexOf("of", occurLoc);
					String occur = name.substring(occurLoc, ofLoc + 2);
					name = name.replaceAll(occur, "");
					occur += " ";
					name = occur.concat(name);
				}
			}

			if(node.hasChildNodes()){
				NodeList childNodes = node.getChildNodes();
				for(int j=0;j<childNodes.getLength();j++){
					Node childNode = childNodes.item(j);
					if(childNode.getNodeName().equals("attribute")){
						String attributeText = getAttributeText(childNode, populationOrSubtreeXMLProcessor);
						//special handling for 'negation rationale' attribute
						if(childNode.getAttributes().getNamedItem("name").getNodeValue().equals("negation rationale")){
							String[] nameArr = node.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue().split(":");
							if(nameArr.length == 2){
								name = nameArr[1].trim() + attributeText + "\""+nameArr[0].trim();
							}else if(nameArr.length == 3){
								name = nameArr[1].trim()+": "+nameArr[2].trim() + attributeText + "\""+nameArr[0].trim();
							}
							// Move Occurrence of to the beginning of the name 
							if(name.contains("Occurrence") && name.contains("of")){
								int occurLoc = name.indexOf("Occurrence", 0);
								int ofLoc = name.indexOf("of", occurLoc);
								String occur = name.substring(occurLoc, ofLoc + 2);
								name = name.replaceAll(occur, "");
								occur += " ";
								name = occur.concat(name);
							}
						}
						
						else{
							name += attributeText;
						}
					}
				}
			}
			
			name = "\"" + name + "\" ";
		}
		return name;
	}
	
	private static String getAttributeText(Node attributeNode, XmlProcessor populationOrSubtreeXMLProcessor){
		String attributeText = "";
		String attributeName = attributeNode.getAttributes().getNamedItem("name").getNodeValue();
		String modeName = attributeNode.getAttributes().getNamedItem("mode").getNodeValue();
		
		if("Check if Present".equals(modeName)){
			attributeText = " ("+attributeName+")";
		}else if("Less Than Or Equal To".equals(modeName)){
			String comparisonValue = attributeNode.getAttributes().getNamedItem("comparisonValue").getNodeValue();
			attributeText = " (" + attributeName + " <= " + comparisonValue + " " + getUnitString(attributeNode) + ")";
		}else if("Greater Than Or Equal To".equals(modeName)){
			String comparisonValue = attributeNode.getAttributes().getNamedItem("comparisonValue").getNodeValue();
			attributeText = " (" + attributeName + " >= " + comparisonValue + " " + getUnitString(attributeNode) + ")";
		}else if("Equal To".equals(modeName)){
			String comparisonValue = attributeNode.getAttributes().getNamedItem("comparisonValue").getNodeValue();
			attributeText = " (" + attributeName + " = " + comparisonValue + " " + getUnitString(attributeNode) + ")";
		}else if("Greater Than".equals(modeName)){
			String comparisonValue = attributeNode.getAttributes().getNamedItem("comparisonValue").getNodeValue();
			attributeText = " (" + attributeName + " > " + comparisonValue + " " + getUnitString(attributeNode) + ")";
		}else if("Less Than".equals(modeName)){
			String comparisonValue = attributeNode.getAttributes().getNamedItem("comparisonValue").getNodeValue();
			attributeText = " (" + attributeName + " < " + comparisonValue + " " + getUnitString(attributeNode) + ")";
		}else if("Value Set".equals(modeName)){
			String qdmUUIDValue = attributeNode.getAttributes().getNamedItem("qdmUUID").getNodeValue();
			//find the qdm tag using qdmUUIDValue 
			try {
				Node qdmNodeNameAttribute = populationOrSubtreeXMLProcessor.findNode(attributeNode.getOwnerDocument(), "//elementLookUp/qdm[@uuid='"+qdmUUIDValue+"']/@name");
				if(qdmNodeNameAttribute != null){
					if("negation rationale".equals(attributeName)){
						attributeText = " not done: " + qdmNodeNameAttribute.getNodeValue() + "\" for ";
					}else{
						attributeText = " (" + attributeName + ": " + qdmNodeNameAttribute.getNodeValue() + ")";
					}
				}else{
					attributeText = "";
				}
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				attributeText = "";
			}
			
			
		}
		return attributeText;
	}
	
	private static String getUnitString(Node attributeNode) {
		String unitValue = "";
		
		Node unitNode = attributeNode.getAttributes().getNamedItem("unit");
		if(unitNode != null){
			unitValue = attributeNode.getAttributes().getNamedItem("unit").getNodeValue();
		}
		if(unitValue.equals("celsius")){
			unitValue = "\u2103";
		}
		else if(unitValue.equals("years")|| unitValue.equals("year")){
			unitValue = "year(s)";
		}
		else if (unitValue.equals("month") || unitValue.equals("months")){
			unitValue = "month(s)";
		}
		else if (unitValue.equals("day") || unitValue.equals("days")){
			unitValue = "day(s)";
		}
		else if(unitValue.equals("hour") || unitValue.equals("hours")){
			unitValue = "hour(s)";
		}
		else if(unitValue.equals("week") || unitValue.equals("weeks")){
			unitValue = "week(s)";
		}
		else if(unitValue.equals("minute") || unitValue.equals("minutes")){
			unitValue = "minute(s)";
		}
		else if(unitValue.equals("quarter") || unitValue.equals("quarters")){
			unitValue = "quarter(s)";
		}
		else if(unitValue.equals("second") || unitValue.equals("seconds")){
			unitValue = "second(s)";
		}
		
		return unitValue;
	}

	private static String getFunctionText(Node item) {
		if(!FUNCTIONAL_OP.equals(item.getNodeName())){
			return "";
		}
		
		String typeAttribute = item.getAttributes().getNamedItem("type").getNodeValue();
		String functionDisplayName = item.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
		
		if("AGE AT".equals(typeAttribute)){
			functionDisplayName = item.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue().toLowerCase();
		}else if(functionDisplayName.startsWith("AVG")){
			functionDisplayName = functionDisplayName.replaceFirst("AVG", "Average") + " of";
		}else if(functionDisplayName.startsWith("COUNT")){
			functionDisplayName = functionDisplayName.replaceFirst("COUNT", "Count") + " of";
		}else if(functionDisplayName.startsWith("DATEDIFF")){
			functionDisplayName = functionDisplayName.replaceFirst("DATEDIFF", "Difference between dates") + " of";
		}else if(functionDisplayName.startsWith("MAX")){
			functionDisplayName = functionDisplayName.replaceFirst("MAX", "Maximum") + " of";
		}else if(functionDisplayName.startsWith("MIN")){
			functionDisplayName = functionDisplayName.replaceFirst("MIN", "Min") + " of";
		}else if(functionDisplayName.startsWith("MEDIAN")){
			functionDisplayName = functionDisplayName.replaceFirst("MEDIAN", "Median") + " of";
		}else if(functionDisplayName.startsWith("SUM")){
			functionDisplayName = functionDisplayName.replaceFirst("SUM", "Sum") + " of";
		}else if(functionDisplayName.startsWith("TIMEDIFF")){
			functionDisplayName = functionDisplayName.replaceFirst("TIMEDIFF", "Time difference") + " of";;
		}else if(functionDisplayName.startsWith("FOURTH")){
			functionDisplayName = functionDisplayName.replaceFirst("FOURTH", "Fourth");
		}else if(functionDisplayName.startsWith("FIFTH")){
			functionDisplayName = functionDisplayName.replaceFirst("FIFTH", "Fifth");
		}		
		
		if(item.getAttributes().getNamedItem("unit")!= null){
			String unit = item.getAttributes().getNamedItem("unit").getNodeValue();
			functionDisplayName = functionDisplayName.replaceFirst(unit, getUnitString(item));
		}
		functionDisplayName =StringUtils.capitalize(functionDisplayName.toLowerCase());
		
		return functionDisplayName+": ";
	}

	/**
	 * This will look at the elementRef node and return an appropriate 
	 * display text for human readable HTML for that QDM.
	 * TODO: Write code to take <attributes> into account.
	 * @param item
	 * @return
	 */
//	private static String getQDMText(Node item) {
//		if(!ELEMENT_REF.equals(item.getNodeName())){
//			return "";
//		}
//		String displayName = item.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
//		if(displayName.endsWith(" : Timing Element")){
//			displayName = displayName.substring(0,displayName.indexOf(" : Timing Element"));
//		}
//		return "\"" + displayName + "\" ";
//	}

	private static org.jsoup.nodes.Document createBaseHumanReadableDocument() {
		org.jsoup.nodes.Document htmlDocument = org.jsoup.nodes.Document.createShell("");
		Element head = htmlDocument.head();
		appendStyleNode(head);
		return htmlDocument;
	}

	private static void appendStyleNode(Element head) {
		String styleTagString = MATCssUtil.getCSS();
		head.append(styleTagString);
	}

	public static String generateHTMLForMeasure(String measureId,
			String simpleXmlStr) {
		String humanReadableHTML = "";
		try {
			org.jsoup.nodes.Document humanReadableHTMLDocument = HeaderHumanReadableGenerator.generateHeaderHTMLForMeasure(simpleXmlStr);
			XmlProcessor simpleXMLProcessor = resolveSubTreesInPopulations(simpleXmlStr);
			//XmlProcessor simpleXMLProcessor = new XmlProcessor(simpleXmlStr);
			System.out.println(simpleXmlStr);
			
			generateHumanReadable(humanReadableHTMLDocument, simpleXMLProcessor);
			humanReadableHTML = humanReadableHTMLDocument.toString();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return humanReadableHTML;
	}

	private static void generateHumanReadable(
			Document humanReadableHTMLDocument, XmlProcessor simpleXMLProcessor) throws XPathExpressionException {
		
		generateTableOfContents(humanReadableHTMLDocument, simpleXMLProcessor);
		generatePopulationCriteriaHumanReadable(humanReadableHTMLDocument, simpleXMLProcessor);
		generateQDMVariables(humanReadableHTMLDocument, simpleXMLProcessor);
		generateDataCriteria(humanReadableHTMLDocument, simpleXMLProcessor);
		generateSupplementalData(humanReadableHTMLDocument, simpleXMLProcessor);
		HeaderHumanReadableGenerator.addMeasureSet(simpleXMLProcessor, humanReadableHTMLDocument);
	}
	
	private static void generateTableOfContents(
			Document humanReadableHTMLDocument,XmlProcessor simpleXMLProcessor) {
		Element bodyElement = humanReadableHTMLDocument.body();
		
		bodyElement.append("<h2><a name=\"toc\">Table of Contents</a></h2>");
		Element tocULElement = bodyElement.appendElement(HTML_UL);
		
		Element populationCriteriaLI = tocULElement.appendElement(HTML_LI);
		populationCriteriaLI.append("<a href=\"#d1e405\">Population criteria</a>");
		
		//TODO:code to decide if we need to add 'Measure observations'
		
		Element dataVariables = tocULElement.appendElement(HTML_LI);
		dataVariables.append("<a href=\"#d1e539\">Data Criteria (QDM Variables)</a>");
		
		Element dataCriteriaLI = tocULElement.appendElement(HTML_LI);
		dataCriteriaLI.append("<a href=\"#d1e647\">Data criteria (QDM Data Elements)</a>");
		
		//TODO:code to decide if we need to add 'Reporting Stratification'
		
		Element supplementalCriteriaLI = tocULElement.appendElement(HTML_LI);
		supplementalCriteriaLI.append("<a href=\"#d1e767\">Supplemental Data Elements</a>");
		
		bodyElement.append("<hr align=\"left\" color=\"teal\" size=\"2\" width=\"80%\">");
		
	}

	private static void generateDataCriteria(
			Document humanReadableHTMLDocument, XmlProcessor simpleXMLProcessor) throws XPathExpressionException {
		
		Element bodyElement = humanReadableHTMLDocument.body();
		bodyElement.append("<h3><a name=\"d1e647\" href=\"#toc\">Data criteria (QDM Data Elements)</a></h3>");
			
		Element mainDivElement = bodyElement.appendElement("div");
		Element mainListElement = mainDivElement.appendElement(HTML_UL);
		
		//get all qdm elemes in 'elementLookUp' which are not attributes or dont have 'Timing Element', 'Patient Characteristic Expired' and
				// 'Patient Characteristic Birthdate' Default QDM data type and are not supplement data elems
				NodeList qdmElements = simpleXMLProcessor.findNodeList(simpleXMLProcessor.getOriginalDoc(), 
						"/measure/elementLookUp/qdm[@datatype != 'Timing Element' and @oid!='"+ConstantMessages.BIRTHDATE_OID +"' " +
								"and @oid!='"+ConstantMessages.EXPIRED_OID +"']");
		
			Map<String, Node> qdmMap = new HashMap<String, Node>();
			Map<String, Node> attributeMap = new HashMap<String, Node>();
			
			for(int i=0;i<qdmElements.getLength();i++){
				Node qdmNode = qdmElements.item(i);
				String oid = qdmNode.getAttributes().getNamedItem("oid").getNodeValue();
				String datatype = qdmNode.getAttributes().getNamedItem("datatype").getNodeValue();
				String suppDataElement = qdmNode.getAttributes().getNamedItem("suppDataElement").getNodeValue();
				String uuid = qdmNode.getAttributes().getNamedItem("uuid").getNodeValue();
				String name = qdmNode.getAttributes().getNamedItem("name").getNodeValue();
				
				if("attribute".equals(datatype)){
					attributeMap.put(oid+datatype, qdmNode);
				}else if("true".equals(suppDataElement)){
					int isUsedInLogic = simpleXMLProcessor.getNodeCount(simpleXMLProcessor.getOriginalDoc(), "count(//subTree//elementRef[@id='"+uuid+"'])");
					if(isUsedInLogic > 0){
						if(!qdmMap.containsKey(oid+datatype)){
							qdmMap.put(datatype+":"+name+"~"+oid, qdmNode);
						}
					}
				}else{
					int supplementalCount = simpleXMLProcessor.getNodeCount(simpleXMLProcessor.getOriginalDoc(), "count(/measure/supplementalDataElements/elementRef[@id='"+uuid+"'])");
					if(supplementalCount > 0){
						int isUsedInLogic = simpleXMLProcessor.getNodeCount(simpleXMLProcessor.getOriginalDoc(), "count(//subTree//elementRef[@id='"+uuid+"'])");
						if(isUsedInLogic > 0){
							if(!qdmMap.containsKey(oid+datatype)){
								qdmMap.put(datatype+":"+name+"~"+oid, qdmNode);
							}
						}
					}
					if(!qdmMap.containsKey(oid+datatype)){
						qdmMap.put(datatype+":"+name+"~"+oid, qdmNode);
					}
				}
			}
			
			List<String> qdmNameList = new ArrayList(qdmMap.keySet());
			Collections.sort(qdmNameList,new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o1.substring(0,o1.indexOf('~')).compareToIgnoreCase(o2.substring(0,o2.indexOf('~')));
				}
			});
		if(qdmNameList.size()>0){
			for(String s: qdmNameList){
				Node qdm = qdmMap.get(s);
				NamedNodeMap qdmAttribs = qdm.getAttributes();
				Element listItem = mainListElement.appendElement(HTML_LI);
				
				listItem.appendText("\"" + qdmAttribs.getNamedItem("datatype").getNodeValue()+": "+qdmAttribs.getNamedItem("name").getNodeValue()+"\" using \""+qdmAttribs.getNamedItem("name").getNodeValue() +" "+ qdmAttribs.getNamedItem("taxonomy").getNodeValue() +" Value Set ("+qdmAttribs.getNamedItem("oid").getNodeValue()+")\"");
			}
			
			for(Node qdm:attributeMap.values()){
				checkAndAddForNegationRationale(mainListElement, qdm, simpleXMLProcessor);
				NamedNodeMap qdmAttribs = qdm.getAttributes();
				Element listItem = mainListElement.appendElement(HTML_LI);
				Node node = simpleXMLProcessor.findNode(simpleXMLProcessor.getOriginalDoc(), "//attribute[@qdmUUID=\""+qdmAttribs.getNamedItem("uuid").getNodeValue()+"\"][@name != \"negation rationale\"]");
				String name ="";
				System.out.println("UUID: " + qdmAttribs.getNamedItem("uuid").getNodeValue());
				if(node != null){
					name = node.getAttributes().getNamedItem("name").getNodeValue();
					name = StringUtils.capitalize(name);
				}
				listItem.appendText(" Attribute: "+"\"" +name+": "+qdmAttribs.getNamedItem("name").getNodeValue()+"\" using \""+qdmAttribs.getNamedItem("name").getNodeValue() +" "+ qdmAttribs.getNamedItem("taxonomy").getNodeValue() +" Value Set ("+qdmAttribs.getNamedItem("oid").getNodeValue()+")\"");
			}
		}
		else{
			mainListElement.appendElement(HTML_LI).appendText("None");
		}
	}
	
	private static void checkAndAddForNegationRationale(
			Element mainListElement, Node qdm, XmlProcessor simpleXMLProcessor) throws XPathExpressionException, DOMException {
		System.out.println("in checkAndAddForNegationRationale");
		NamedNodeMap qdmAttribs = qdm.getAttributes();
		NodeList nodeList = simpleXMLProcessor.findNodeList(simpleXMLProcessor.getOriginalDoc(), "//attribute[@qdmUUID=\""+qdmAttribs.getNamedItem("uuid").getNodeValue()+"\"][@name = \"negation rationale\"]");
		List<String> elementRefUUIDList = new ArrayList<String>();
		
		for(int i=0;i<nodeList.getLength();i++){
			Node attributeNode = nodeList.item(i);
			Node parentNode = attributeNode.getParentNode();
			if(!elementRefUUIDList.contains(parentNode.getAttributes().getNamedItem("id").getNodeValue())){
				elementRefUUIDList.add(parentNode.getAttributes().getNamedItem("id").getNodeValue());
				String name = parentNode.getAttributes().getNamedItem("displayName").getNodeValue();
				System.out.println("parent tag name:"+parentNode.getNodeName());
				System.out.println("neg rationale name:"+name);
				String[] nameArr = name.split(":");
				if(nameArr.length == 2){
					name = nameArr[1].trim();
					Element listItem = mainListElement.appendElement(HTML_LI);
					listItem.appendText("\"" +name.trim()+" not done: "+qdmAttribs.getNamedItem("name").getNodeValue()+"\" using \""+qdmAttribs.getNamedItem("name").getNodeValue() +" "+ qdmAttribs.getNamedItem("taxonomy").getNodeValue() +" Value Set ("+qdmAttribs.getNamedItem("oid").getNodeValue()+")\"");
				}else if(nameArr.length == 3){
					name = nameArr[1].trim()+": "+nameArr[2].trim();
					Element listItem = mainListElement.appendElement(HTML_LI);
					listItem.appendText("\"" +name.trim()+" not done: "+qdmAttribs.getNamedItem("name").getNodeValue()+"\" using \""+qdmAttribs.getNamedItem("name").getNodeValue() +" "+ qdmAttribs.getNamedItem("taxonomy").getNodeValue() +" Value Set ("+qdmAttribs.getNamedItem("oid").getNodeValue()+")\"");
				}
			}
		}
	}

	private static void generateSupplementalData(
			Document humanReadableHTMLDocument, XmlProcessor simpleXMLProcessor) throws XPathExpressionException {

		Element bodyElement = humanReadableHTMLDocument.body();
		bodyElement.append("<h3><a name=\"d1e767\" href=\"#toc\">Supplemental Data Elements</a></h3>");

		Element mainDivElement = bodyElement.appendElement("div");
		Element mainListElement = mainDivElement.appendElement(HTML_UL);

		NodeList elements = simpleXMLProcessor.findNodeList(simpleXMLProcessor.getOriginalDoc(), "/measure/supplementalDataElements/elementRef");
		if(elements.getLength() > 0){
			Map<String, Node> qdmNodeMap = new HashMap<String, Node>();
			for(int i = 0; i<elements.getLength(); i++){
				Node node = elements.item(i);
				NamedNodeMap map = node.getAttributes();
				String id = map.getNamedItem("id").getNodeValue();
				System.out.println(id);
				Node qdm = simpleXMLProcessor.findNode(simpleXMLProcessor.getOriginalDoc(), "/measure/elementLookUp/qdm[@uuid='"+id+"']");
				NamedNodeMap qdmMap = qdm.getAttributes();
				qdmNodeMap.put(qdmMap.getNamedItem("datatype").getNodeValue()+": "+qdmMap.getNamedItem("name").getNodeValue(), qdm);
			}
			
			List<String> qdmNameList = new ArrayList(qdmNodeMap.keySet());
			Collections.sort(qdmNameList,new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o1.compareToIgnoreCase(o2);
				}
			});
			
			for(String s: qdmNameList){
				Node qdm = qdmNodeMap.get(s);
				NamedNodeMap qdmMap = qdm.getAttributes();
				Element listItem = mainListElement.appendElement(HTML_LI);
	
				listItem.appendText("\"" + qdmMap.getNamedItem("datatype").getNodeValue()+": "+qdmMap.getNamedItem("name").getNodeValue()+"\" using \""+qdmMap.getNamedItem("name").getNodeValue() +" "+ qdmMap.getNamedItem("taxonomy").getNodeValue() +" Value Set ("+qdmMap.getNamedItem("oid").getNodeValue()+")\"");
			}
		}
		else{
			mainListElement.appendElement(HTML_LI).appendText("None");
		}
	}
	
	
	private static void generateQDMVariables(
			Document humanReadableHTMLDocument, XmlProcessor simpleXMLProcessor) throws XPathExpressionException {
		
		Element bodyElement = humanReadableHTMLDocument.body();
		bodyElement.append("<h3><a name=\"d1e539\" href=\"#toc\">Data Criteria (QDM Variables)</a></h3>");
			
		Element mainDivElement = bodyElement.appendElement("div");
		Element mainListElement = mainDivElement.appendElement(HTML_UL);

		NodeList variables = simpleXMLProcessor.findNodeList(simpleXMLProcessor.getOriginalDoc(), "/measure/subTreeLookUp/subTree[@qdmVariable='true']");
		if(variables.getLength()>0){
			Node node;
			for(int i=0; i<variables.getLength();i++){
				node = variables.item(i);
				System.out.println(node.getNodeName());
				NamedNodeMap map = node.getAttributes();
				String name = map.getNamedItem("displayName").getNodeValue();
				if (name.length() > 0){
					name = StringUtils.deleteWhitespace(name);
					name = "$" + name;
				}
				Element variableElement = mainListElement.appendElement(HTML_LI);
				Element boldNameElement = variableElement.appendElement("b");
				boldNameElement.appendText(name+" = ");
				Element indentListElement = variableElement.appendElement(HTML_UL);
				showOnlyVariableName= true;
				parseChild(node,indentListElement, node.getParentNode(),simpleXMLProcessor,false);
				showOnlyVariableName = false;
			}
		}
		else{
			mainListElement.appendElement(HTML_LI).appendText("None");
		}
	}
	
	private static void generatePopulationCriteriaHumanReadable(
			Document humanReadableHTMLDocument, XmlProcessor simpleXMLProcessor) throws XPathExpressionException {
		
		Element bodyElement = humanReadableHTMLDocument.body();
		bodyElement.append("<h3><a name=\"d1e405\" href=\"#toc\">Population criteria</a></h3>");
			
		Element mainDivElement = bodyElement.appendElement("div");
		Element mainListElement = mainDivElement.appendElement(HTML_UL);
			
		NodeList groupNodeList = simpleXMLProcessor.findNodeList(simpleXMLProcessor.getOriginalDoc(), "/measure/measureGrouping/group");
		
		for(int i=0;i<groupNodeList.getLength();i++){
			
			if(groupNodeList.getLength() > 1){
				mainListElement.append("<br><b>------ Population Criteria "+ (i+1) +" ------</b><br><br>");
			}
			
			Node groupNode = groupNodeList.item(i);
						
			NodeList clauseNodeList = groupNode.getChildNodes();
			generatePopulationNodes(clauseNodeList,mainListElement,groupNodeList.getLength(),i,simpleXMLProcessor);
		}
	}
	
	private static void generatePopulationNodes(NodeList clauseNodeList,
			Element mainListElement, int totalGroupCount, int currentGroupNumber,
			XmlProcessor simpleXMLProcessor) {
		
		for(int i=0;i<popNameArray.length;i++){
			generatePopulationNodes(popNameArray[i], clauseNodeList,mainListElement,totalGroupCount,currentGroupNumber,simpleXMLProcessor);
		}
		
	}

	private static void generatePopulationNodes(String populationType,
			NodeList clauseNodeList, Element mainListElement, int totalGroupCount, int currentGroupNumber,XmlProcessor simpleXMLProcessor) {
		
		//find all clause nodes with attribute type=populationType
		List<Node> clauseNodes = new ArrayList<Node>();
		for(int j=0;j<clauseNodeList.getLength();j++){
			Node clauseNode = clauseNodeList.item(j);
			if("clause".equals(clauseNode.getNodeName())){
				String popType = clauseNode.getAttributes().getNamedItem("type").getNodeValue();
				if(popType.equals(populationType)){
					clauseNodes.add(clauseNode);
				}
			}
		}
		
		if(clauseNodes.size() > 1){
			Element populationListElement = mainListElement.appendElement(HTML_LI);
			Element boldNameElement = populationListElement.appendElement("b");
			String populationName = getPopulationName(populationType,true);
			/*if (totalGroupCount > 1){
				populationName += "s " + (currentGroupNumber+1);
			}*//*else{
				populationName += "s";
			}*/
			boldNameElement.appendText(populationName+" =");
			Element childPopulationULElement = populationListElement.appendElement(HTML_UL);
			for(int c=0;c<clauseNodes.size();c++){
				Node clauseNode = clauseNodes.get(c);
				Element childPopulationListElement = childPopulationULElement.appendElement(HTML_LI);
				Element childBoldNameElement = childPopulationListElement.appendElement("b");
				String childPopulationName = getPopulationName(populationType);
				/*if (totalGroupCount > 1){
					childPopulationName += " " + (currentGroupNumber+1) + "." + (c+1);
				}else{
					childPopulationName += " " + (c+1);
				}*/
				childPopulationName += " " + (c+1);
				if("initialPopulation".equalsIgnoreCase(clauseNode.getAttributes().getNamedItem("type").getNodeValue())){
					initialPopulationHash.put(clauseNode.getAttributes().getNamedItem("uuid").getNodeValue(), String.valueOf(c+1));
				}
				String itemCountText = getItemCountText(clauseNode);
				String popassoc = getPopAssoc(clauseNode,simpleXMLProcessor);
				childBoldNameElement.appendText(childPopulationName+(popassoc.length() > 0 ? popassoc : "")+(itemCountText.length() > 0 ? itemCountText : "")+" =");
				parseAndBuildHTML(simpleXMLProcessor, childPopulationListElement,clauseNode,c+1);
			}
		}else if(clauseNodes.size() == 1){
			Element populationListElement = mainListElement.appendElement(HTML_LI);
			Element boldNameElement = populationListElement.appendElement("b");
			String populationName = getPopulationName(populationType);
//			if (totalGroupCount > 1){
//				populationName += " " + (currentGroupNumber+1);
//			}
			if("initialPopulation".equalsIgnoreCase(clauseNodes.get(0).getAttributes().getNamedItem("type").getNodeValue())){
				initialPopulationHash.put(clauseNodes.get(0).getAttributes().getNamedItem("uuid").getNodeValue(), "-1");
			}
			String itemCountText = getItemCountText(clauseNodes.get(0));
			boldNameElement.appendText(populationName+(itemCountText.length() > 0 ? itemCountText : "")+" =");
			parseAndBuildHTML(simpleXMLProcessor, populationListElement,clauseNodes.get(0),1);
		}
	}
	
	private static String getPopAssoc(Node node, XmlProcessor processor){
		String stringAssoc = "";
		try {
			if("measureObservation".equalsIgnoreCase(node.getAttributes().getNamedItem("type").getNodeValue())){
				Node nodeAssoc = node.getAttributes().getNamedItem("associatedPopulationUUID");
				if(nodeAssoc != null){
					
					Node newAssoc = processor.findNode(processor.getOriginalDoc(), "//clause[@uuid=\""+nodeAssoc.getNodeValue()+"\"]");
					if(newAssoc != null){
						String name = getPopulationName(newAssoc.getAttributes().getNamedItem("type").getNodeValue());
						stringAssoc = "    (Association: "+ name + ")";
					}
				}
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stringAssoc;
	}
	
	private static String getItemCountText(Node node) {
		String itemCountText = "";
		
		if("clause".equals(node.getNodeName())){
			NodeList childNodes = node.getChildNodes();
			for(int i=0;i<childNodes.getLength();i++){
				Node child = childNodes.item(i);
				if("itemCount".equals(child.getNodeName())){
					NodeList elementRefList = child.getChildNodes();
					for(int j=0;j<elementRefList.getLength();j++){
						Node elementRef = elementRefList.item(j);
						String nodeText = elementRef.getAttributes().getNamedItem("dataType").getNodeValue()+": "+elementRef.getAttributes().getNamedItem("name").getNodeValue();
						if(j == (elementRefList.getLength()-1)){
							itemCountText += nodeText;
						}else{
							itemCountText += nodeText + "; ";
						}
					}
					if(itemCountText.length() > 0){
						itemCountText = "    (Item Count: "+itemCountText+")";
					}
					break;
				}
			}
		}
		
		return itemCountText;
	}

	private static String getPopulationName(String populationType, boolean addPlural) {
		String name = getPopulationName(populationType);
		if(addPlural && (!name.endsWith("Exclusions") && !name.endsWith("Exceptions"))){
			name += "s ";
		}
		return name;
	}
	
	private static String getPopulationName(String nodeValue) {
		String populationName = "";
		if("initialPopulation".equals(nodeValue)){
			populationName = "Initial Population";
		}else if("measurePopulation".equals(nodeValue)){
			populationName = "Measure Population";
		}else if("measurePopulationExclusions".equals(nodeValue)){
			populationName = "Measure Population Exclusions";
		}else if("measureObservation".equals(nodeValue)){
			populationName = "Measure Observation";
		}else if("stratum".equals(nodeValue)){
			populationName = "Stratification";
		}else if("denominator".equals(nodeValue)){
			populationName = "Denominator";
		}else if("denominatorExclusions".equals(nodeValue)){
			populationName = "Denominator Exclusions";
		}else if("denominatorExceptions".equals(nodeValue)){
			populationName = "Denominator Exceptions";
		}else if("numerator".equals(nodeValue)){
			populationName = "Numerator";
		}else if("numeratorExclusions".equals(nodeValue)){
			populationName = "Numerator Exclusions";
		}
		return populationName;
	}

	private static XmlProcessor resolveSubTreesInPopulations(String simpleXmlStr) {
		XmlProcessor simpleXMLProcessor = new XmlProcessor(simpleXmlStr);		
		
		try {
			NodeList clauseNodeList = simpleXMLProcessor.findNodeList(simpleXMLProcessor.getOriginalDoc(), "/measure/measureGrouping/group/clause");
			Object[][] nodeArr = new Object[clauseNodeList.getLength()][2];
			for(int i=0;i<clauseNodeList.getLength();i++){
				Node clauseNode = clauseNodeList.item(i);
				String clauseNodeXMLString = simpleXMLProcessor.transform(clauseNode);
				
				System.out.println("");
				System.out.println("clauseNode XML String:"+clauseNodeXMLString);
				System.out.println("");
				
				XmlProcessor clauseXMLProcessor = new XmlProcessor(clauseNodeXMLString);
				clauseXMLProcessor = expandSubTreesAndImportQDMs(clauseXMLProcessor, simpleXMLProcessor, false);
				System.out.println();
				Node expandedClauseNode = clauseXMLProcessor.getOriginalDoc().getFirstChild();
				
				nodeArr[i][0] = clauseNode;
				nodeArr[i][1] = expandedClauseNode;
			}
			
			for(int j=0;j<nodeArr.length;j++){
				Node clauseNode = (Node) nodeArr[j][0];
				Node expandedClauseNode = (Node) nodeArr[j][1];
				
				Node importedClauseNode = simpleXMLProcessor.getOriginalDoc().importNode(expandedClauseNode, true);
				Node parentNode = clauseNode.getParentNode();
				parentNode.replaceChild(importedClauseNode, clauseNode);
			}
			//removeNode("/measure/subTreeLookUp",simpleXMLProcessor);
			System.out.println("Expanded simple xml:"+simpleXMLProcessor.transform(simpleXMLProcessor.getOriginalDoc()));
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return simpleXMLProcessor;
	}
	
	private static void removeNode(String nodeXPath, XmlProcessor xmlProcessor) throws XPathExpressionException {
		Node node = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), nodeXPath);
		if(node != null){
			Node parentNode = node.getParentNode();
			parentNode.removeChild(node);
		}
	}

}
