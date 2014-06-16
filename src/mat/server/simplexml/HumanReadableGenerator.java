package mat.server.simplexml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import mat.server.util.XmlProcessor;

import org.jsoup.nodes.Element;
import org.w3c.dom.DOMException;
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

	public static String generateHTMLForPopulationOrSubtree(String measureId, String subXML,
			String measureXML) {
		org.jsoup.nodes.Document htmlDocument = null;
		//replace the <subTree> tags in 'populationSubXML' with the appropriate subTree tags from 'simpleXML'.
		try {
			System.out.println("Original subXML:"+subXML);
			XmlProcessor populationOrSubtreeXMLProcessor = expandSubTreesAndImportQDMs(subXML, measureXML);
			
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
			
			parseAndBuildHTML(populationOrSubtreeXMLProcessor, populationOrSubtreeListElement, isPopulation);
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

	private static XmlProcessor expandSubTreesAndImportQDMs(String subXML, String measureXML) throws XPathExpressionException {
		
		XmlProcessor populationOrSubtreeXMLProcessor = new XmlProcessor(subXML);
		XmlProcessor measureXMLProcessor = new XmlProcessor(measureXML);
		
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
		
		//import <elementLookUp> tag to populationOrSubtreeXMLProcessor
		Node elementLookUpNode = measureXMLProcessor.findNode(measureXMLProcessor.getOriginalDoc(), "//elementLookUp");
		Node importedElementLookUpNode = populationOrSubtreeXMLProcessor.getOriginalDoc().importNode(elementLookUpNode, true);
		populationOrSubtreeXMLProcessor.getOriginalDoc().getFirstChild().appendChild(importedElementLookUpNode);
		
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
			XmlProcessor populationOrSubtreeXMLProcessor, Element populationOrSubtreeListElement, boolean isPopulation) {
		
		try {
			Node rootNode = populationOrSubtreeXMLProcessor.getOriginalDoc().getFirstChild();
			NodeList childNodes = rootNode.getChildNodes();
			for(int i = 0;i < childNodes.getLength(); i++){
				parseChild(childNodes.item(i),populationOrSubtreeListElement,rootNode,populationOrSubtreeXMLProcessor);
			}			
		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	private static void parseChild(Node item, Element parentListElement, Node parentNode, XmlProcessor populationOrSubtreeXMLProcessor) {
		String nodeName = item.getNodeName();
		
		if(LOGICAL_OP.equals(nodeName)){
			
			if(LOGICAL_OP.equals(parentNode.getNodeName()) ){
				Element liElement = parentListElement.appendElement(HTML_LI);				
				if(LOGICAL_OP.equals(parentNode.getNodeName())){
					liElement.appendText(getNodeText(parentNode, populationOrSubtreeXMLProcessor));
				}
			}
			
			Element ulElement = parentListElement.appendElement(HTML_UL);
			NodeList childNodes = item.getChildNodes();
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
			NodeList childNodes = item.getChildNodes();
			for (int i=0; i< childNodes.getLength(); i++){
				parseChild(childNodes.item(i), parentListElement,parentNode, populationOrSubtreeXMLProcessor);				
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
			
		}else if(ELEMENT_LOOK_UP.equals(nodeName)){
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
			parseChild(childNodes.item(0),liElement,item, populationOrSubtreeXMLProcessor);
			Element newLiElement = liElement;
//			if(LOGICAL_OP.equals(childNodes.item(0).getNodeName()) || SET_OP.equals(childNodes.item(0).getNodeName())){
//				newLiElement = liElement.children().last().appendElement(HTML_LI);
//			}
			
			if(!ELEMENT_REF.equals(childNodes.item(1).getNodeName())){
				newLiElement.appendElement(HTML_LI).appendText(item.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue().toLowerCase()+" ");
				newLiElement = liElement.appendElement(HTML_UL).appendElement(HTML_LI);
			}else{
				newLiElement.appendText(item.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue().toLowerCase()+" ");
			}
			parseChild(childNodes.item(1),newLiElement,item, populationOrSubtreeXMLProcessor);
		}
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
			name = node.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue().toUpperCase();
			name += " of ";
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
		if("AGE AT".equals(typeAttribute)){
			return item.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue() + " ";
		}
		return item.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue() + " of ";
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
			humanReadableHTML = HeaderHumanReadableGenerator.generateHeaderHTMLForMeasure(simpleXmlStr);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return humanReadableHTML;
	}

}
