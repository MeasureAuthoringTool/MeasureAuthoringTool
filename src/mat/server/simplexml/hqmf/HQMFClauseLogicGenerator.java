package mat.server.simplexml.hqmf;

import java.util.HashMap;
import java.util.Map;
import javax.xml.xpath.XPathExpressionException;
import mat.model.clause.MeasureExport;
import mat.server.util.XmlProcessor;
import mat.shared.UUIDUtilClient;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class HQMFClauseLogicGenerator implements Generator {
	
	Map<String, Node> subTreeNodeMap = new HashMap<String,Node>();
	private static final Log logger = LogFactory
			.getLog(HQMFClauseLogicGenerator.class);
	
	
	@Override
	public String generate(MeasureExport me) throws Exception {
		generateSubTreeXML(me);
		return null;
	}
	
	private void generateSubTreeXML(MeasureExport me) throws XPathExpressionException{
		String xpath = "/measure/subTreeLookUp/child::node()";
		NodeList subTreeNodeList = me.getSimpleXMLProcessor().findNodeList(me.getSimpleXMLProcessor().getOriginalDoc(), xpath);
		
		for(int i=0;i<subTreeNodeList.getLength();i++){
			Node subTreeNode = subTreeNodeList.item(i);
			generateSubTreeXML(me,subTreeNode);
			
		}
	}
	
	private void generateSubTreeXML(MeasureExport me, Node subTreeNode) throws XPathExpressionException {
		
		/**
		 * If this is an empty or NULL clause, return right now.
		 */
		if((subTreeNode == null) || !subTreeNode.hasChildNodes()){
			return;
		}
		
		String subTreeUUID = subTreeNode.getAttributes().getNamedItem("uuid").getNodeValue();
		String clauseName = subTreeNode.getAttributes().getNamedItem("displayName").getNodeValue();
		
		/**
		 * Check the 'subTreeNodeMap' to make sure the clause isnt already generated.
		 */
		if(subTreeNodeMap.containsKey(subTreeUUID)){
			logger.info("HQMF for Clause "+clauseName + " is already generated. Skipping.");
			return;
		}
		
		//get the first child of the subTreeNode
		Node firstChild = subTreeNode.getFirstChild();
		String firstChildName = firstChild.getNodeName();
		logger.info("Generating HQMF for clause:'"+clauseName+"' with first child named:'"+firstChildName+"'.");
		
		XmlProcessor hqmfXmlProcessor = me.getHQMFXmlProcessor();
		Element dataCriteriaSectionElem = (Element) hqmfXmlProcessor.getOriginalDoc().getElementsByTagName("dataCriteriaSection").item(0);
		
		//generate comment
		Comment comment = hqmfXmlProcessor.getOriginalDoc().createComment("Clause '"+clauseName+"'");
		dataCriteriaSectionElem.appendChild(comment);
		
		switch (firstChildName) {
			case "setOp":
				generateSetOpHQMF(me,firstChild,dataCriteriaSectionElem);
				break;
			case "elementRef":
				generateElementRefHQMF(me, firstChild,dataCriteriaSectionElem);
				break;
			case "subTreeRef":
				generateSubTreeHQMF(me, firstChild,dataCriteriaSectionElem);
				break;
			case "relationalOp":
				generateRelOpHQMF(me, firstChild, dataCriteriaSectionElem);
			default:
				//Dont do anything
				break;
		}
		
		/**
		 * The clause is generated now. Make an entry in the 'subTreeNodeMap' to keep track of its generation.
		 */
		subTreeNodeMap.put(subTreeUUID, subTreeNode);
		
	}
	
	/**
	 * This will take care of the use case where a user can create a Clause with only one
	 * QDM elementRef inside it.
	 * Since we have no way of referencing that element using <outBoundRelationShip> directly, we are
	 * adding it to a default UNION grouper.
	 * @param me
	 * @param elementRefNode
	 * @param parentNode
	 * @throws XPathExpressionException
	 */
	private void generateElementRefHQMF(MeasureExport me, Node elementRefNode, Node parentNode) throws XPathExpressionException {
		XmlProcessor hqmfXmlProcessor = me.getHQMFXmlProcessor();
		
		String ext = getElementRefExt(elementRefNode, me.getSimpleXMLProcessor());
		String root = elementRefNode.getAttributes().getNamedItem(ID).getNodeValue();
		Node idNodeQDM = hqmfXmlProcessor.findNode(hqmfXmlProcessor.getOriginalDoc(), "//entry/*/id[@root='"+root+"'][@extension='"+ext+"']");
		if(idNodeQDM != null){
			Node entryElem = idNodeQDM.getParentNode().getParentNode().cloneNode(true);
			Node newIdNode = ((Element)entryElem.getFirstChild()).getElementsByTagName(ID).item(0);
			
			/**
			 * Create a dummy grouper for UNION with the id@root = uuid of the subTree
			 * and id@extension = id of elementRef
			 */
			String idroot = "0";
			String idExt = elementRefNode.getAttributes().getNamedItem("id").getNodeValue();
			Node parNode = elementRefNode.getParentNode();
			if((parNode != null) && "subTree".equals(parNode.getNodeName())){
				idroot = parNode.getAttributes().getNamedItem("uuid").getNodeValue();
			}
			((Element)newIdNode).setAttribute(ROOT, idroot);
			((Element)newIdNode).setAttribute("extension", idExt);
			parentNode.appendChild(entryElem);
			
		}
		
	}
	
	/**
	 * This will take care of the use case where a user can create a Clause with only one
	 * child Clause inside it.
	 * If HQMF for the child clause is already generated, then since we have no way of referencing
	 * this child clause using <outBoundRelationShip> directly, we are
	 * adding it to a default UNION grouper.
	 * 
	 * If it isnt generated then we generate it and then add a criteriaRef to it inside a default UNION.
	 * @param me
	 * @param subTreeRefNode
	 * @param parentNode
	 * @throws XPathExpressionException
	 */
	private void generateSubTreeHQMF(MeasureExport me, Node subTreeRefNode, Node parentNode) throws XPathExpressionException {
		
		String subTreeUUID = subTreeRefNode.getAttributes().getNamedItem("id").getNodeValue();
		
		/**
		 * Check if the Clause has already been generated.
		 * If it is not generated yet, then generate it by
		 * calling the 'generateSubTreeXML' method.
		 */
		if(!subTreeNodeMap.containsKey(subTreeUUID)){
			String xpath = "/measure/subTreeLookUp/subTree[@uuid='"+subTreeUUID+"']";
			Node subTreeNode = me.getSimpleXMLProcessor().findNode(me.getSimpleXMLProcessor().getOriginalDoc(), xpath);
			generateSubTreeXML(me, subTreeNode);
		}
		
		XmlProcessor hqmfXmlProcessor = me.getHQMFXmlProcessor();
		
		// creating Entry Tag
		Element entryElem = hqmfXmlProcessor.getOriginalDoc().createElement("entry");
		entryElem.setAttribute(TYPE_CODE, "DRIV");
		
		String root = "0";
		String ext = subTreeRefNode.getAttributes().getNamedItem("id").getNodeValue();
		Node parNode = subTreeRefNode.getParentNode();
		if((parNode != null) && "subTree".equals(parNode.getNodeName())){
			root = parNode.getAttributes().getNamedItem("uuid").getNodeValue();
		}
		Node grouperElem = generateEmptyGrouper(hqmfXmlProcessor, root, ext);
		
		//generate comment
		Comment comment = hqmfXmlProcessor.getOriginalDoc().createComment("outBoundRelationship for "+subTreeRefNode.getAttributes().getNamedItem("displayName").getNodeValue());
		grouperElem.appendChild(comment);
		
		//generate outboundRelationship
		Element outboundRelElem = generateEmptyOutboundElem(hqmfXmlProcessor);
		Element conjunctionCodeElem = hqmfXmlProcessor.getOriginalDoc().createElement("conjunctionCode");
		conjunctionCodeElem.setAttribute(CODE, "OR");
		
		outboundRelElem.appendChild(conjunctionCodeElem);
		generateCritRefForNode(me, outboundRelElem, subTreeRefNode);
		
		grouperElem.appendChild(outboundRelElem);
		
		entryElem.appendChild(grouperElem);
		parentNode.appendChild(entryElem);
		
	}
	
	/**
	 * This method wil generate HQMF code for setOp (UNION,INTERSECTION)
	 * @param me
	 * @param setOpNode
	 * @throws XPathExpressionException
	 */
	private Node generateSetOpHQMF(MeasureExport me, Node setOpNode, Node parentNode) throws XPathExpressionException {
		
		XmlProcessor hqmfXmlProcessor = me.getHQMFXmlProcessor();
		String setOpType = setOpNode.getAttributes().getNamedItem("displayName").getNodeValue();
		String conjunctionType = "OR";
		
		if("UNION".equals(setOpType)){
			conjunctionType = "OR";
		}else{
			conjunctionType = "AND";
		}
		
		// creating Entry Tag
		Element entryElem = hqmfXmlProcessor.getOriginalDoc().createElement("entry");
		entryElem.setAttribute(TYPE_CODE, "DRIV");
		
		//creating grouperCriteria element
		String root = "0";
		String ext = setOpType;
		Node parNode = setOpNode.getParentNode();
		if((parNode != null) && "subTree".equals(parNode.getNodeName())){
			root = parNode.getAttributes().getNamedItem("uuid").getNodeValue();
		}else{
			root = UUIDUtilClient.uuid();
		}
		Node grouperElem = generateEmptyGrouper(hqmfXmlProcessor, root, ext);
		
		NodeList childNodes = setOpNode.getChildNodes();
		for(int i=0;i<childNodes.getLength();i++){
			Node childNode = childNodes.item(i);
			String childName = childNode.getNodeName();
			if("comment".equals(childName)){
				continue;
			}
			
			//generate comment
			Comment comment = hqmfXmlProcessor.getOriginalDoc().createComment("outBoundRelationship for "+childNode.getAttributes().getNamedItem("displayName").getNodeValue());
			grouperElem.appendChild(comment);
			
			//generate outboundRelationship
			Element outboundRelElem = generateEmptyOutboundElem(hqmfXmlProcessor);
			Element conjunctionCodeElem = hqmfXmlProcessor.getOriginalDoc().createElement("conjunctionCode");
			conjunctionCodeElem.setAttribute(CODE, conjunctionType);
			
			outboundRelElem.appendChild(conjunctionCodeElem);
			if("elementRef".equals(childName) || "subTreeRef".equals(childName)){
				generateCritRefForNode(me, outboundRelElem, childNode);
			}else{
				switch (childName) {
					case "setOp":
						generateCritRefSetOp(me, parentNode, hqmfXmlProcessor,
								childNode, outboundRelElem);
						break;
					case "relationalOp":
						generateCritRefRelOp(me, parentNode, hqmfXmlProcessor,
								childNode, outboundRelElem);
						break;
					default:
						//Dont do anything
						break;
				}
			}
			grouperElem.appendChild(outboundRelElem);
		}
		
		entryElem.appendChild(grouperElem);
		parentNode.appendChild(entryElem);
		
		return entryElem;
	}
	
	/**
	 * 
	 * @param me
	 * @param relOpNode
	 * @param dataCriteriaSectionElem
	 * @throws XPathExpressionException
	 */
	private void generateRelOpHQMF(MeasureExport me, Node relOpNode, Node dataCriteriaSectionElem) throws XPathExpressionException {
		
		XmlProcessor hqmfXmlProcessor = me.getHQMFXmlProcessor();
		
		if(relOpNode.getChildNodes().getLength() == 2){
			Node lhsNode = relOpNode.getFirstChild();
			Node rhsNode = relOpNode.getLastChild();
			
			String lhsName = lhsNode.getNodeName();
			String rhsName = rhsNode.getNodeName();
			
			if("elementRef".equals(lhsName)){
				String ext = getElementRefExt(lhsNode, me.getSimpleXMLProcessor());
				String root = lhsNode.getAttributes().getNamedItem(ID).getNodeValue();
				
				Node idNodeQDM = hqmfXmlProcessor.findNode(hqmfXmlProcessor.getOriginalDoc(), "//entry/*/id[@root='"+root+"'][@extension='"+ext+"']");
				if(idNodeQDM != null){
					Node entryNodeForElementRef = idNodeQDM.getParentNode().getParentNode();
					Node clonedEntryNodeForElementRef = entryNodeForElementRef.cloneNode(true);
					
					Element temporallyRelatedInfoNode = createBaseTemporalNode(
							relOpNode, hqmfXmlProcessor);
					
					if("elementRef".equals(rhsName) || "subTreeRef".equals(rhsName)){
						generateCritRefForNode(me, temporallyRelatedInfoNode, rhsNode);
					}else{
						switch (rhsName) {
							case "setOp":
								generateCritRefSetOp(me, dataCriteriaSectionElem, hqmfXmlProcessor,
										rhsNode, temporallyRelatedInfoNode);
								break;
							case "relationalOp":
								generateCritRefRelOp(me, dataCriteriaSectionElem, hqmfXmlProcessor,
										rhsNode, temporallyRelatedInfoNode);
								break;
							default:
								//Dont do anything
								break;
						}
					}
					clonedEntryNodeForElementRef.getFirstChild().appendChild(temporallyRelatedInfoNode);
					
					//create comment node
					Comment comment = hqmfXmlProcessor.getOriginalDoc().createComment("entry for "+relOpNode.getAttributes().getNamedItem("displayName").getNodeValue());
					dataCriteriaSectionElem.appendChild(comment);
					dataCriteriaSectionElem.appendChild(clonedEntryNodeForElementRef);
				}
			}
		}else{
			logger.info("Relational Op:"+relOpNode.getAttributes().getNamedItem("displayName").getNodeValue()+" does not have exactly 2 children. Skipping HQMF for it.");
		}
	}
	
	private Element createBaseTemporalNode(Node relOpNode,
			XmlProcessor hqmfXmlProcessor) {
		
		NamedNodeMap attribMap = relOpNode.getAttributes();
		Element temporallyRelatedInfoNode = hqmfXmlProcessor.getOriginalDoc().createElement("temporallyRelatedInformation");
		temporallyRelatedInfoNode.setAttribute("typeCode", attribMap.getNamedItem("type").getNodeValue().toUpperCase());
		
		Element temporalInfoNode = hqmfXmlProcessor.getOriginalDoc().createElement("qdm:temporalInformation");
		temporalInfoNode.setAttribute("precisionUnit", "min");
		
		temporallyRelatedInfoNode.appendChild(temporalInfoNode);
		
		if(attribMap.getNamedItem("operatorType") != null){
			String operatorType = attribMap.getNamedItem("operatorType").getNodeValue();
			String quantity = attribMap.getNamedItem("quantity").getNodeValue();
			String unit = attribMap.getNamedItem("unit").getNodeValue();
			
			if(!"hours".equals(unit) && !"minutes".equals(unit)){
				temporalInfoNode.setAttribute("precisionUnit", "d");
				if("days".equals(unit)){
					unit = "d";
				}else if("weeks".equals(unit)){
					unit = "wk";
				}else if("months".equals(unit)){
					unit = "mo";
				}else if("years".equals(unit)){
					unit = "a";
				}
			}else{
				if("hours".equals(unit)){
					unit = "h";
				}else{
					unit = "min";
				}
			}
			
			Element deltaNode = hqmfXmlProcessor.getOriginalDoc().createElement("qdm:delta");
			Element lowNode = hqmfXmlProcessor.getOriginalDoc().createElement("low");
			lowNode.setAttribute("unit", unit);
			
			Element highNode = hqmfXmlProcessor.getOriginalDoc().createElement("high");
			highNode.setAttribute("unit", unit);
			
			if(operatorType.startsWith("Greater Than")){
				lowNode.setAttribute("value", quantity);
				highNode.removeAttribute("unit");
				highNode.setAttribute(NULL_FLAVOR, "PINF");
				if("Greater Than or Equal To".equals(operatorType)){
					deltaNode.setAttribute("lowClosed", "true");
				}
			}else if("Equal To".equals(operatorType)){
				deltaNode.setAttribute("lowClosed", "true");
				deltaNode.setAttribute("highClosed", "true");
				lowNode.setAttribute("value", quantity);
				highNode.setAttribute("value", quantity);
			}else if(operatorType.startsWith("Less Than")){
				deltaNode.setAttribute("lowClosed", "true");
				lowNode.setAttribute("value", "0");
				highNode.setAttribute("value", quantity);
				if("Less Than or Equal To".equals(operatorType)){
					deltaNode.setAttribute("highClosed", "true");
				}
			}
			deltaNode.appendChild(lowNode);
			deltaNode.appendChild(highNode);
			temporalInfoNode.appendChild(deltaNode);
		}
		
		return temporallyRelatedInfoNode;
	}
	
	private void generateCritRefRelOp(MeasureExport me, Node parentNode,
			XmlProcessor hqmfXmlProcessor, Node childNode,
			Node outboundRelElem) throws XPathExpressionException {
		//TODO: Finish this method
		generateRelOpHQMF(me, childNode,parentNode);
	}
	
	private void generateCritRefSetOp(MeasureExport me, Node parentNode,
			XmlProcessor hqmfXmlProcessor, Node childNode,
			Node outboundRelElem) throws XPathExpressionException {
		
		Node setOpEntry = generateSetOpHQMF(me,childNode,parentNode);
		NodeList childList = setOpEntry.getChildNodes();
		for(int j=0;j<childList.getLength();j++){
			Node child = childList.item(j);
			if("grouperCriteria".equals(child.getNodeName())){
				Node idChild = child.getFirstChild();
				NamedNodeMap attribMap = idChild.getAttributes();
				String idRoot = attribMap.getNamedItem(ROOT).getNodeValue();
				String idExt = attribMap.getNamedItem("extension").getNodeValue();
				
				//create criteriaRef
				Element criteriaReference = hqmfXmlProcessor.getOriginalDoc().createElement("criteriaReference");
				criteriaReference.setAttribute(CLASS_CODE, "GROUPER");
				criteriaReference.setAttribute(MOOD_CODE, "EVN");
				
				Element id = hqmfXmlProcessor.getOriginalDoc().createElement("id");
				id.setAttribute(ROOT, idRoot);
				id.setAttribute("extension", idExt);
				
				criteriaReference.appendChild(id);
				outboundRelElem.appendChild(criteriaReference);
			}
		}
	}
	
	private void generateCritRefForNode(MeasureExport me, Node outboundRelElem, Node childNode) throws XPathExpressionException {
		XmlProcessor hqmfXmlProcessor = me.getHQMFXmlProcessor();
		String childName = childNode.getNodeName();
		
		switch(childName){
			case "elementRef":
				generateCritRefElementRef(me, outboundRelElem, childNode,hqmfXmlProcessor);
				break;
			case "subTreeRef":
				generateCritRefSubTreeRef(me, outboundRelElem, childNode, hqmfXmlProcessor);
				break;
				
			default:
				break;
		}
		
	}
	
	
	/**
	 * This method will basically create a <criteriaReference> with classCode='GROUPER' and moodCode='EVN'
	 * and have the <id> tag pointing to the <grouperCriteria> for the referenced subTree/clause.
	 * @param me
	 * @param outboundRelElem
	 * @param subTreeRefNode
	 * @param hqmfXmlProcessor
	 * @throws XPathExpressionException
	 */
	protected void generateCritRefSubTreeRef(MeasureExport me, Node outboundRelElem, Node subTreeRefNode, XmlProcessor hqmfXmlProcessor) throws XPathExpressionException {
		
		String subTreeUUID = subTreeRefNode.getAttributes().getNamedItem("id").getNodeValue();
		String root = subTreeUUID;
		
		String xpath = "/measure/subTreeLookUp/subTree[@uuid='"+subTreeUUID+"']";
		Node subTreeNode = me.getSimpleXMLProcessor().findNode(me.getSimpleXMLProcessor().getOriginalDoc(), xpath);
		if(subTreeNode != null ) {
			Node firstChild = subTreeNode.getFirstChild();
			String firstChildName = firstChild.getNodeName();
			
			String ext = firstChild.getAttributes().getNamedItem("displayName").getNodeValue();
			if("elementRef".equals(firstChildName)){
				ext = firstChild.getAttributes().getNamedItem("id").getNodeValue();
			}
			
			Node idNodeQDM = hqmfXmlProcessor.findNode(hqmfXmlProcessor.getOriginalDoc(), "//entry/*/id[@root='"+root+"'][@extension='"+ext+"']");
			if(idNodeQDM != null){
				Node parent = idNodeQDM.getParentNode();
				if(parent != null){
					NamedNodeMap attribMap = parent.getAttributes();
					String classCode = attribMap.getNamedItem(CLASS_CODE).getNodeValue();
					String moodCode = attribMap.getNamedItem(MOOD_CODE).getNodeValue();
					
					//create criteriaRef
					Element criteriaReference = hqmfXmlProcessor.getOriginalDoc().createElement("criteriaReference");
					criteriaReference.setAttribute(CLASS_CODE, classCode);
					criteriaReference.setAttribute(MOOD_CODE, moodCode);
					
					Element id = hqmfXmlProcessor.getOriginalDoc().createElement("id");
					id.setAttribute(ROOT, root);
					id.setAttribute("extension", ext);
					
					criteriaReference.appendChild(id);
					outboundRelElem.appendChild(criteriaReference);
				}
			}
		}
	}
	
	private void generateCritRefElementRef(MeasureExport me,
			Node outboundRelElem, Node elementRefNode,
			XmlProcessor hqmfXmlProcessor) throws XPathExpressionException {
		String ext = getElementRefExt(elementRefNode, me.getSimpleXMLProcessor());
		String root = elementRefNode.getAttributes().getNamedItem(ID).getNodeValue();
		Node idNodeQDM = hqmfXmlProcessor.findNode(hqmfXmlProcessor.getOriginalDoc(), "//entry/*/id[@root='"+root+"'][@extension='"+ext+"']");
		if(idNodeQDM != null){
			Node parent = idNodeQDM.getParentNode();
			if(parent != null){
				NamedNodeMap attribMap = parent.getAttributes();
				String classCode = attribMap.getNamedItem(CLASS_CODE).getNodeValue();
				String moodCode = attribMap.getNamedItem(MOOD_CODE).getNodeValue();
				
				//create criteriaRef
				Element criteriaReference = hqmfXmlProcessor.getOriginalDoc().createElement("criteriaReference");
				criteriaReference.setAttribute(CLASS_CODE, classCode);
				criteriaReference.setAttribute(MOOD_CODE, moodCode);
				
				Element id = hqmfXmlProcessor.getOriginalDoc().createElement("id");
				id.setAttribute(ROOT, root);
				id.setAttribute("extension", ext);
				
				criteriaReference.appendChild(id);
				outboundRelElem.appendChild(criteriaReference);
			}
		}
	}
	
	/**
	 * 
	 * @param me
	 * @param outboundRelElem
	 * @param childNode
	 * @param hqmfXmlProcessor
	 */
	private void generateCritRefRelOp(MeasureExport me, Element outboundRelElem, Node childNode, XmlProcessor hqmfXmlProcessor) {
		// TODO Auto-generated method stub
		
	}
	
	private String getElementRefExt(Node elementRefNode, XmlProcessor simpleXmlProcessor) throws XPathExpressionException {
		String extension = "";
		if(elementRefNode.hasChildNodes()){
			Node childNode = elementRefNode.getFirstChild();
			if("attribute".equals(childNode.getNodeName())){
				extension = childNode.getAttributes().getNamedItem("attrUUID").getNodeValue();
			}
		}else{
			String id = elementRefNode.getAttributes().getNamedItem("id").getNodeValue();
			Node qdmNode = simpleXmlProcessor.findNode(simpleXmlProcessor.getOriginalDoc(), "/measure/elementLookUp/qdm[@uuid='"+id+"']");
			if(qdmNode != null){
				String dataType = qdmNode.getAttributes().getNamedItem("datatype").getNodeValue();
				String qdmName = qdmNode.getAttributes().getNamedItem(NAME).getNodeValue();
				extension = qdmName + "_" + dataType;
				if(qdmNode.getAttributes().getNamedItem("instance") != null){
					extension = qdmNode.getAttributes().getNamedItem("instance").getNodeValue() +"_" + extension;
				}
			}
		}
		return StringUtils.deleteWhitespace(extension);
	}
	
	private Node generateEmptyGrouper(XmlProcessor hqmfXmlProcessor,
			String root, String ext) {
		Element grouperElem = hqmfXmlProcessor.getOriginalDoc().createElement("grouperCriteria");
		grouperElem.setAttribute(CLASS_CODE, "GROUPER");
		grouperElem.setAttribute(MOOD_CODE, "EVN");
		
		Element idElem = hqmfXmlProcessor.getOriginalDoc().createElement(ID);
		idElem.setAttribute(ROOT, root);
		idElem.setAttribute("extension", ext);
		
		grouperElem.appendChild(idElem);
		
		return grouperElem;
	}
	
	private Element generateEmptyOutboundElem(XmlProcessor hqmfXmlProcessor) {
		Element outboundRelElem = hqmfXmlProcessor.getOriginalDoc().createElement("outboundRelationship");
		outboundRelElem.setAttribute(TYPE_CODE, "COMP");
		return outboundRelElem;
	}
}
