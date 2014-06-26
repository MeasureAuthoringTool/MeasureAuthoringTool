package mat.server.simplexml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import mat.server.util.XmlProcessor;

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
	
	private static Boolean showOnlyVariableName = false;
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
			parseAndBuildHTML(populationOrSubtreeXMLProcessor, populationOrSubtreeListElement, rootNode);			
		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	private static void parseAndBuildHTML(
			XmlProcessor populationOrSubtreeXMLProcessor, Element populationOrSubtreeListElement, Node clauseNode) {
		
		try {
			NodeList childNodes = clauseNode.getChildNodes();
			for(int i = 0;i < childNodes.getLength(); i++){
				parseChild(childNodes.item(i),populationOrSubtreeListElement,clauseNode,populationOrSubtreeXMLProcessor);
			}			
		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	private static void parseChild(Node item, Element parentListElement, Node parentNode, XmlProcessor populationOrSubtreeXMLProcessor) {
		String nodeName = item.getNodeName();
		System.out.println("parseChild:"+nodeName);
		if(LOGICAL_OP.equals(nodeName)){
			
			if(LOGICAL_OP.equals(parentNode.getNodeName()) ){
				Element liElement = parentListElement.appendElement(HTML_LI);				
				if(LOGICAL_OP.equals(parentNode.getNodeName())){
					liElement.appendText(getNodeText(parentNode, populationOrSubtreeXMLProcessor));
				}
			}
			
			Element ulElement = parentListElement.appendElement(HTML_UL);
			NodeList childNodes = item.getChildNodes();
			if(childNodes.getLength() == 0){
				ulElement.appendElement(HTML_LI).appendText("None");
			}
			for (int i=0; i< childNodes.getLength(); i++){
				parseChild(childNodes.item(i), ulElement, item, populationOrSubtreeXMLProcessor);				
			}
		}else if(COMMENT.equals(nodeName)){
			String commentValue = item.getTextContent();
			if(commentValue != null && commentValue.trim().length() > 0){
				Element liElement = parentListElement.appendElement(HTML_LI);
				liElement.attr("style","list-style-type: none");
				Element italicElement = liElement.appendElement("i");
				italicElement.appendText("# "+item.getTextContent());
			}
			return;
		}else if(SUB_TREE.equals(nodeName)){
			NamedNodeMap map = item.getAttributes();
			if("true".equalsIgnoreCase(map.item(1).getNodeValue()) && showOnlyVariableName == false){
				if(parentListElement.nodeName().equals(HTML_UL)){
					parentListElement = parentListElement.appendElement(HTML_LI);
				}
				if(LOGICAL_OP.equals(parentNode.getNodeName()) ){
					parentListElement.appendText(getNodeText(parentNode, populationOrSubtreeXMLProcessor));
				}

				String name = StringUtils.deleteWhitespace(map.item(0).getNodeValue());
				parentListElement.appendText("$" + name + " ");
			}
			else{
				showOnlyVariableName = false;
				NodeList childNodes = item.getChildNodes();
				for (int i=0; i< childNodes.getLength(); i++){
					parseChild(childNodes.item(i), parentListElement,parentNode, populationOrSubtreeXMLProcessor);				
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
				parseChild(childNodes.item(i), ulElement,item, populationOrSubtreeXMLProcessor);				
			}
		}else if(RELATIONAL_OP.equals(nodeName)){
			if(LOGICAL_OP.equals(parentNode.getNodeName()) || SET_OP.equals(parentNode.getNodeName())){
				Element liElement = parentListElement.appendElement(HTML_LI);
				//liElement.appendText(getNodeText(parentNode, populationOrSubtreeXMLProcessor));
				if(LOGICAL_OP.equals(parentNode.getNodeName())){
					liElement.appendText(getNodeText(parentNode, populationOrSubtreeXMLProcessor));
				}
				getRelationalOpText(item, liElement, populationOrSubtreeXMLProcessor);
			}else{
				getRelationalOpText(item, parentListElement, populationOrSubtreeXMLProcessor);
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
		}else if(FUNCTIONAL_OP.equals(nodeName)){
			if(LOGICAL_OP.equals(parentNode.getNodeName()) || SET_OP.equals(parentNode.getNodeName())){
				Element liElement = parentListElement.appendElement(HTML_LI);
				//liElement.appendText(" "+getNodeText(parentNode, populationOrSubtreeXMLProcessor));
				if(LOGICAL_OP.equals(parentNode.getNodeName())){
					liElement.appendText(" "+getNodeText(parentNode, populationOrSubtreeXMLProcessor));
				}
				liElement.appendText(getFunctionText(item));
				NodeList childNodes = item.getChildNodes();
				if(childNodes.getLength() == 1 && ELEMENT_REF.equals(childNodes.item(0).getNodeName())){
					parseChild(childNodes.item(0), liElement,item, populationOrSubtreeXMLProcessor);
				}else{
					Element ulElement = liElement.appendElement(HTML_UL);
					for (int i=0; i< childNodes.getLength(); i++){
						parseChild(childNodes.item(i), ulElement.appendElement(HTML_LI),item, populationOrSubtreeXMLProcessor);				
					}
				}
			}else{
				parentListElement.appendText(getFunctionText(item));
				NodeList childNodes = item.getChildNodes();
				if(childNodes.getLength() == 1 && ELEMENT_REF.equals(childNodes.item(0).getNodeName())){
					parseChild(childNodes.item(0), parentListElement,item, populationOrSubtreeXMLProcessor);
				}else{
					Element ulElement = parentListElement.appendElement(HTML_UL);
					for (int i=0; i< childNodes.getLength(); i++){
						parseChild(childNodes.item(i), ulElement.appendElement(HTML_LI),item, populationOrSubtreeXMLProcessor);				
					}
				}
			}
			
		}else if(ELEMENT_LOOK_UP.equals(nodeName) || "itemCount".equals(nodeName)){
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

	/**
	 * @param item
	 * @param liElement
	 * @param populationOrSubtreeXMLProcessor 
	 */
	private static void getRelationalOpText(Node item, Element liElement, XmlProcessor populationOrSubtreeXMLProcessor) {
		/**
		 * A relationalOp can have 2 children. First evaluate the LHS child, then add the name of the relationalOp and finally
		 * evaluate the RHS child.
		 */
		NodeList childNodes = item.getChildNodes();
		
		if(childNodes.getLength() == 2){
			
			Element newLiElement = liElement;
			if(isParentNodeName(item,LOGICAL_OP) && !ELEMENT_REF.equals(childNodes.item(0).getNodeName())){
				newLiElement = liElement.appendElement(HTML_UL).appendElement(HTML_LI);
			}
			parseChild(childNodes.item(0),newLiElement,item, populationOrSubtreeXMLProcessor);
			
			if(!ELEMENT_REF.equals(childNodes.item(1).getNodeName())){
				newLiElement.appendElement(HTML_LI).appendText(item.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue().toLowerCase()+" ");
				newLiElement = newLiElement.appendElement(HTML_UL).appendElement(HTML_LI);
			}else{
				if(!ELEMENT_REF.equals(childNodes.item(0).getNodeName())){
					newLiElement = newLiElement.appendElement(HTML_LI);
					newLiElement.appendText(item.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue().toLowerCase()+" ");
				}else{
					newLiElement.appendText(item.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue().toLowerCase()+" ");
				}
			}
			parseChild(childNodes.item(1),newLiElement,item, populationOrSubtreeXMLProcessor);
		}
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
			}else {
				String[] nameArr = name.split(":");
				if(nameArr.length == 2){
					name = nameArr[1].trim()+": "+nameArr[0].trim();
				}
			}
			//TODO: Write code to take <attributes> into account.
			if(node.hasChildNodes()){
				NodeList childNodes = node.getChildNodes();
				for(int j=0;j<childNodes.getLength();j++){
					Node childNode = childNodes.item(j);
					if(childNode.getNodeName().equals("attribute")){
						String attributeText = getAttributeText(childNode, populationOrSubtreeXMLProcessor);						
						name += attributeText;
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
					attributeText = " (" + attributeName + ": " + qdmNodeNameAttribute.getNodeValue() + ")";
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
			functionDisplayName = item.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue().toLowerCase() + " ";
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
		
		//get all qdm elemes in 'elementLookUp' which are not attributes or dont have 'Timing Element' data type and are not supplement data elems
		NodeList qdmElements = simpleXMLProcessor.findNodeList(simpleXMLProcessor.getOriginalDoc(), 
				"/measure/elementLookUp/qdm[@datatype != 'Timing Element'][@suppDataElement != 'true']");
		
		Map<String, Node> qdmMap = new HashMap<String, Node>();
		Map<String, Node> attributeMap = new HashMap<String, Node>();
		
		for(int i=0;i<qdmElements.getLength();i++){
			Node qdmNode = qdmElements.item(i);
			String oid = qdmNode.getAttributes().getNamedItem("oid").getNodeValue();
			String datatype = qdmNode.getAttributes().getNamedItem("datatype").getNodeValue();
			
			if("attribute".equals(datatype)){
				attributeMap.put(oid+datatype, qdmNode);
			}else{
				if(!qdmMap.containsKey(oid+datatype)){
					qdmMap.put(oid+datatype, qdmNode);
				}
			}
		}
		
		for(Node qdm:qdmMap.values()){
			NamedNodeMap qdmAttribs = qdm.getAttributes();
			Element listItem = mainListElement.appendElement(HTML_LI);

			listItem.appendText("\"" + qdmAttribs.getNamedItem("datatype").getNodeValue()+": "+qdmAttribs.getNamedItem("name").getNodeValue()+"\" using \""+qdmAttribs.getNamedItem("name").getNodeValue() +" "+ qdmAttribs.getNamedItem("taxonomy").getNodeValue() +" Value Set ("+qdmAttribs.getNamedItem("oid").getNodeValue()+")\"");
		}
		
		for(Node qdm:attributeMap.values()){
			NamedNodeMap qdmAttribs = qdm.getAttributes();
			Element listItem = mainListElement.appendElement(HTML_LI);

			listItem.appendText(" Attribute: "+"\"" +qdmAttribs.getNamedItem("name").getNodeValue()+"\" using \""+qdmAttribs.getNamedItem("name").getNodeValue() +" "+ qdmAttribs.getNamedItem("taxonomy").getNodeValue() +" Value Set ("+qdmAttribs.getNamedItem("oid").getNodeValue()+")\"");
		}
	}
	
	private static void generateSupplementalData(
			Document humanReadableHTMLDocument, XmlProcessor simpleXMLProcessor) throws XPathExpressionException {

		Element bodyElement = humanReadableHTMLDocument.body();
		bodyElement.append("<h3><a name=\"d1e767\" href=\"#toc\">Supplemental Data Elements</a></h3>");

		Element mainDivElement = bodyElement.appendElement("div");
		Element mainListElement = mainDivElement.appendElement(HTML_UL);

		NodeList elements = simpleXMLProcessor.findNodeList(simpleXMLProcessor.getOriginalDoc(), "/measure/supplementalDataElements/elementRef");
		for(int i = 0; i<elements.getLength(); i++){
			Node node = elements.item(i);
			NamedNodeMap map = node.getAttributes();
			String id = map.item(0).getNodeValue();
			System.out.println(id);
			Node qdm = simpleXMLProcessor.findNode(simpleXMLProcessor.getOriginalDoc(), "/measure/elementLookUp/qdm[@uuid='"+id+"']");
			NamedNodeMap qdmMap = qdm.getAttributes();
			Element listItem = mainListElement.appendElement(HTML_LI);

			listItem.appendText("\"" + qdmMap.getNamedItem("datatype").getNodeValue()+": "+qdmMap.getNamedItem("name").getNodeValue()+"\" using \""+qdmMap.getNamedItem("name").getNodeValue() +" "+ qdmMap.getNamedItem("taxonomy").getNodeValue() +" Value Set ("+qdmMap.getNamedItem("oid").getNodeValue()+")\"");

		}
	}
	
	
	private static void generateQDMVariables(
			Document humanReadableHTMLDocument, XmlProcessor simpleXMLProcessor) throws XPathExpressionException {
		
		Element bodyElement = humanReadableHTMLDocument.body();
		bodyElement.append("<h3><a name=\"d1e539\" href=\"#toc\">Data Criteria (QDM Variables)</a></h3>");
			
		Element mainDivElement = bodyElement.appendElement("div");
		Element mainListElement = mainDivElement.appendElement(HTML_UL);

		NodeList variables = simpleXMLProcessor.findNodeList(simpleXMLProcessor.getOriginalDoc(), "/measure/subTreeLookUp/subTree[@qdmVariable='true']");
		Node node;
		for(int i=0; i<variables.getLength();i++){
			node = variables.item(i);
			System.out.println(node.getNodeName());
			NamedNodeMap map = node.getAttributes();
			String name = map.item(0).getNodeValue();
			if (name.length() > 0){
				name = StringUtils.deleteWhitespace(name);
				name = "$" + name;
			}
			Element variableElement = mainListElement.appendElement(HTML_LI);
			Element boldNameElement = variableElement.appendElement("b");
			boldNameElement.appendText(name+" = ");
			Element indentListElement = variableElement.appendElement(HTML_UL);
			showOnlyVariableName= true;
			parseChild(node,indentListElement, node.getParentNode(),simpleXMLProcessor);
			showOnlyVariableName = false;
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
			for(int j=0;j<clauseNodeList.getLength();j++){
				Node clauseNode = clauseNodeList.item(j);
				if("clause".equals(clauseNode.getNodeName())){
					Element populationListElement = mainListElement.appendElement(HTML_LI);
					Element boldNameElement = populationListElement.appendElement("b");
					String populationName = getPopulationName(clauseNode.getAttributes().getNamedItem("type").getNodeValue());
					if (groupNodeList.getLength() > 1){
						populationName += " " + (i+1);
					}
					boldNameElement.appendText(populationName+" =");
					parseAndBuildHTML(simpleXMLProcessor, populationListElement,clauseNode);
				}
			}
		}
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
		}else if("stratification".equals(nodeValue)){
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
