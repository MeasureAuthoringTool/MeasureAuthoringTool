package mat.server.simplexml.hqmf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

// TODO: Auto-generated Javadoc
/**
 * The Class HQMFClauseLogicGenerator.
 */
public class HQMFClauseLogicGenerator implements Generator {
	
	/** The sub tree node map. */
	Map<String, Node> subTreeNodeMap = new HashMap<String,Node>();
	
	/** The Constant logger. */
	private static final Log logger = LogFactory
			.getLog(HQMFClauseLogicGenerator.class);
	
	
	/* (non-Javadoc)
	 * @see mat.server.simplexml.hqmf.Generator#generate(mat.model.clause.MeasureExport)
	 */
	@Override
	public String generate(MeasureExport me) throws Exception {
		generateSubTreeXML(me);
		return null;
	}
	
	/**
	 * Generate sub tree xml.
	 *
	 * @param me the me
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateSubTreeXML(MeasureExport me) throws XPathExpressionException {
		String xpath = "/measure/subTreeLookUp/subTree[not(@instance)]";
		NodeList subTreeNodeList = me.getSimpleXMLProcessor().findNodeList(me.getSimpleXMLProcessor().getOriginalDoc(), xpath);
		for(int i=0;i<subTreeNodeList.getLength();i++){
			Node subTreeNode = subTreeNodeList.item(i);
			generateSubTreeXML(me,subTreeNode);
		}
		String xpathOccurrence = "/measure/subTreeLookUp/subTree[(@instance)]";
		NodeList occurrenceSubTreeNodeList = me.getSimpleXMLProcessor().findNodeList(me.getSimpleXMLProcessor().getOriginalDoc(), xpathOccurrence);
		for(int i=0;i<occurrenceSubTreeNodeList.getLength();i++){
			Node subTreeNode = occurrenceSubTreeNodeList.item(i);
			generateOccHQMF(me,subTreeNode);
		}
	}
	
	/**
	 * @param me
	 * @param subTreeNode
	 * @throws XPathExpressionException
	 */
	private void generateOccHQMF(MeasureExport me, Node subTreeNode) throws XPathExpressionException {
		/**
		 * If this is an empty or NULL clause, return right now.
		 */
		if((subTreeNode == null) || (subTreeNode.getAttributes().getNamedItem("instanceOf")==null)){
			return;
		}
		XmlProcessor hqmfXmlProcessor = me.getHQMFXmlProcessor();
		String qdmVariableSubTreeUUID = subTreeNode.getAttributes().getNamedItem("instanceOf").getNodeValue();
		String clauseName = subTreeNode.getAttributes().getNamedItem("displayName").getNodeValue();
		if(!subTreeNodeMap.containsKey(qdmVariableSubTreeUUID)){
			logger.info("HQMF for Clause "+clauseName + " is not already generated. Skipping.");
			return;
		}
		Node firstChild = subTreeNode.getFirstChild();
		String firstChildName = firstChild.getNodeName();
		String ext = firstChild.getAttributes().getNamedItem("displayName").getNodeValue();
		String root = subTreeNode.getAttributes().getNamedItem("instanceOf").getNodeValue();
		// Check for Element Ref as first CHild.
		if (firstChildName.equalsIgnoreCase("elementRef")) {
			ext = firstChild.getAttributes().getNamedItem("id").getNodeValue();
		}
		String isQdmVariable = subTreeNode.getAttributes().getNamedItem("qdmVariable").getNodeValue();
		if (isQdmVariable.equalsIgnoreCase("true")) {
			ext = "qdm_var_" + StringUtils.deleteWhitespace(ext);
		}
		String extForOccurrenceNode = "occ" + subTreeNode.getAttributes().getNamedItem(
				"instance").getNodeValue() + "of_qdm_var_"
				+ StringUtils.deleteWhitespace(firstChild.getAttributes().getNamedItem(
						"displayName").getNodeValue());
		System.out.println("generateOccHQMF "+"//entry/*/id[@root='" + root + "'][@extension='" + ext + "']");
		
		Node idNodeQDM = hqmfXmlProcessor.findNode(hqmfXmlProcessor.getOriginalDoc(),
				"//entry/*/id[@root='" + root + "'][@extension='" + ext + "']");
		System.out.println("idNodeQDM == null?"+(idNodeQDM == null));
		if (idNodeQDM != null) {
			Node parentNode = idNodeQDM.getParentNode().cloneNode(false);
			
			Element dataCriteriaSectionElem = (Element) hqmfXmlProcessor.getOriginalDoc()
					.getElementsByTagName("dataCriteriaSection").item(0);
			Comment comment = hqmfXmlProcessor.getOriginalDoc().createComment("Clause '"+clauseName+"'");
			dataCriteriaSectionElem.appendChild(comment);
			Element entryElem = hqmfXmlProcessor.getOriginalDoc().createElement("entry");
			entryElem.setAttribute(TYPE_CODE, "DRIV");
			Element idElement = hqmfXmlProcessor.getOriginalDoc().createElement(ID);
			idElement.setAttribute(ROOT, subTreeNode.getAttributes().getNamedItem("uuid").getNodeValue());
			idElement.setAttribute("extension", extForOccurrenceNode);
			parentNode.appendChild(idElement);
			Element outboundRelElem = hqmfXmlProcessor.getOriginalDoc().createElement("outboundRelationship");
			outboundRelElem.setAttribute("typeCode", "OCCR");
			
			Element criteriaRefElem = hqmfXmlProcessor.getOriginalDoc().createElement("criteriaReference");
			String refClassCodeValue = parentNode.getAttributes().getNamedItem(CLASS_CODE)
					.getNodeValue();
			String refMoodValue = parentNode.getAttributes().getNamedItem(MOOD_CODE)
					.getNodeValue();
			criteriaRefElem.setAttribute(CLASS_CODE, refClassCodeValue);
			criteriaRefElem.setAttribute(MOOD_CODE, refMoodValue);
			
			Element idRelElem = hqmfXmlProcessor.getOriginalDoc().createElement(ID);
			idRelElem.setAttribute(ROOT, root);
			idRelElem.setAttribute("extension", ext);
			
			criteriaRefElem.appendChild(idRelElem);
			outboundRelElem.appendChild(criteriaRefElem);
			parentNode.appendChild(outboundRelElem);
			entryElem.appendChild(parentNode);
			dataCriteriaSectionElem.appendChild(entryElem);
		}
	}
	
	/**
	 * Generate sub tree xml.
	 *
	 * @param me the me
	 * @param subTreeNode the sub tree node
	 * @throws XPathExpressionException the x path expression exception
	 */
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
	 *
	 * @param me the me
	 * @param elementRefNode the element ref node
	 * @param parentNode the parent node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateElementRefHQMF(MeasureExport me, Node elementRefNode, Node parentNode) throws XPathExpressionException {
		XmlProcessor hqmfXmlProcessor = me.getHQMFXmlProcessor();
		
		String ext = getElementRefExt(elementRefNode, me.getSimpleXMLProcessor());
		String root = elementRefNode.getAttributes().getNamedItem(ID).getNodeValue();
		Node idNodeQDM = hqmfXmlProcessor.findNode(hqmfXmlProcessor.getOriginalDoc(), "//entry/*/id[@root='"+root+"'][@extension='"+ext+"']");
		if(idNodeQDM != null){
			Node entryElem = idNodeQDM.getParentNode().getParentNode().cloneNode(true);
			Node newIdNode = getTagFromEntry(entryElem, ID);
			//Node newIdNode = ((Element)entryElem.getFirstChild()).getElementsByTagName(ID).item(0);
			
			if(newIdNode == null){
				return;
			}
			
			/**
			 * Create a dummy grouper for UNION with the id@root = uuid of the subTree
			 * and id@extension = id of elementRef
			 */
			String idroot = "0";
			String idExt = elementRefNode.getAttributes().getNamedItem("id").getNodeValue();
			Node parNode = elementRefNode.getParentNode();
			if((parNode != null) && "subTree".equals(parNode.getNodeName())){
				idroot = parNode.getAttributes().getNamedItem("uuid").getNodeValue();
				// Added logic to show qdm_variable in extension if clause is of qdm variable type.
				String isQdmVariable = parNode.getAttributes().getNamedItem("qdmVariable").getNodeValue();
				if(isQdmVariable.equalsIgnoreCase("true")) {
					idExt = "qdm_var_"+idExt;
				}
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
	 *
	 * @param me the me
	 * @param subTreeRefNode the sub tree ref node
	 * @param parentNode the parent node
	 * @throws XPathExpressionException the x path expression exception
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
			//Added logic to show qdm_variable in extension if clause is of qdm variable type.
			String isQdmVariable = parNode.getAttributes().getNamedItem("qdmVariable").getNodeValue();
			if(isQdmVariable.equalsIgnoreCase("true")) {
				ext = "qdm_var_" + ext;
			}
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
	 * This method wil generate HQMF code for setOp (UNION,INTERSECTION).
	 *
	 * @param me the me
	 * @param setOpNode the set op node
	 * @param parentNode the parent node
	 * @return the node
	 * @throws XPathExpressionException the x path expression exception
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
			//Added logic to show qdm_variable in extension if clause is of qdm variable type.
			String isQdmVariable = parNode.getAttributes().getNamedItem("qdmVariable").getNodeValue();
			if(isQdmVariable.equalsIgnoreCase("true")) {
				ext = "qdm_var_"+ext;
			}
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
	 * Generate rel op hqmf.
	 *
	 * @param me the me
	 * @param relOpNode the rel op node
	 * @param dataCriteriaSectionElem the data criteria section elem
	 * @throws XPathExpressionException the x path expression exception
	 */
	private Node generateRelOpHQMF(MeasureExport me, Node relOpNode, Node dataCriteriaSectionElem) throws XPathExpressionException {
					
		if(relOpNode.getChildNodes().getLength() == 2){
			Node lhsNode = relOpNode.getFirstChild();
			Node rhsNode = relOpNode.getLastChild();
			String lhsName = lhsNode.getNodeName();			
			
			if("elementRef".equals(lhsName)){
				return getrelOpLHSQDM(me, relOpNode, dataCriteriaSectionElem,lhsNode, rhsNode);
			}else if("relationalOp".equals(lhsName)){
				return getrelOpLHSRelOp(me, relOpNode, dataCriteriaSectionElem,lhsNode, rhsNode);
			}else if("setOp".equals(lhsName)){
				return getrelOpLHSSetOp(me, relOpNode, dataCriteriaSectionElem,lhsNode, rhsNode);
			}else if("subTreeRef".equals(lhsName)){
				return getrelOpLHSSubtree(me, relOpNode, dataCriteriaSectionElem,lhsNode, rhsNode);
			}
		}else{
			logger.info("Relational Op:"+relOpNode.getAttributes().getNamedItem("displayName").getNodeValue()+" does not have exactly 2 children. Skipping HQMF for it.");
		}
		return null;
	}
	
	private Node getrelOpLHSSubtree(MeasureExport me, Node relOpNode,
			Node dataCriteriaSectionElem, Node lhsNode, Node rhsNode) {
		
		try{
			String subTreeUUID = lhsNode.getAttributes().getNamedItem("id").getNodeValue();
			String root = subTreeUUID;
			Node relOpParentNode = relOpNode.getParentNode();
			
			String xpath = "/measure/subTreeLookUp/subTree[@uuid='"+subTreeUUID+"']";
			Node subTreeNode = me.getSimpleXMLProcessor().findNode(me.getSimpleXMLProcessor().getOriginalDoc(), xpath);
			if(subTreeNode != null ) {
				String isQdmVariable = subTreeNode.getAttributes()
						.getNamedItem("qdmVariable").getNodeValue();
				Node firstChild = subTreeNode.getFirstChild();
				String firstChildName = firstChild.getNodeName();
				
				String ext = StringUtils.deleteWhitespace(firstChild.getAttributes().getNamedItem("displayName").getNodeValue());
				
				
				if("elementRef".equals(firstChildName)){
					ext = firstChild.getAttributes().getNamedItem("id").getNodeValue();
				}
				if("true".equalsIgnoreCase(isQdmVariable)){
					ext = "qdm_var_"+ext;
				}
				
				/**
				 * Check if the Clause has already been generated.
				 * If it is not generated yet, then generate it by
				 * calling the 'generateSubTreeXML' method.
				 */
				if(!subTreeNodeMap.containsKey(subTreeUUID)){
					generateSubTreeXML(me, subTreeNode);
				}
				
				Node idNodeQDM = me.getHQMFXmlProcessor().findNode(me.getHQMFXmlProcessor().getOriginalDoc(), "//entry/*/id[@root='"+root+"'][@extension='"+ext+"']");
				if(idNodeQDM != null){
					if((relOpParentNode != null) && "subTree".equals(relOpParentNode.getNodeName())){
						root = relOpParentNode.getAttributes().getNamedItem("uuid").getNodeValue();
					}
					ext = StringUtils.deleteWhitespace(relOpNode.getAttributes().getNamedItem("displayName").getNodeValue());
					Node entryNodeForSubTree = idNodeQDM.getParentNode().getParentNode();
					Node clonedEntryNodeForSubTree = entryNodeForSubTree.cloneNode(true);
					
					NodeList idChildNodeList = ((Element)clonedEntryNodeForSubTree).getElementsByTagName(ID);
					if(idChildNodeList != null && idChildNodeList.getLength() > 0){
						Node idChildNode = idChildNodeList.item(0);
						idChildNode.getAttributes().getNamedItem("extension").setNodeValue(ext);
						idChildNode.getAttributes().getNamedItem("root").setNodeValue(root);
					}
					
					Element temporallyRelatedInfoNode = createBaseTemporalNode(relOpNode, me.getHQMFXmlProcessor());
					handleRelOpRHS(me, dataCriteriaSectionElem, rhsNode, temporallyRelatedInfoNode);
					
					Node firstChld = clonedEntryNodeForSubTree.getFirstChild();
					if("localVariableName".equals(firstChild.getNodeName())){
						firstChld = firstChild.getNextSibling();
					}
					NodeList outBoundList = ((Element)firstChld).getElementsByTagName("outboundRelationship");
					if((outBoundList != null) && (outBoundList.getLength() > 0)){
						Node outBound = outBoundList.item(0);
						firstChld.insertBefore(temporallyRelatedInfoNode, outBound);
					}else{
						firstChld.appendChild(temporallyRelatedInfoNode);
					}
					
					//create comment node
					Comment comment = me.getHQMFXmlProcessor().getOriginalDoc().createComment("entry for "+relOpNode.getAttributes().getNamedItem("displayName").getNodeValue());
					dataCriteriaSectionElem.appendChild(comment);
					dataCriteriaSectionElem.appendChild(clonedEntryNodeForSubTree);
					return clonedEntryNodeForSubTree;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 
	 * @param me
	 * @param relOpNode
	 * @param dataCriteriaSectionElem
	 * @param lhsNode
	 * @param rhsNode
	 * @return
	 */
	private Node getrelOpLHSSetOp(MeasureExport me, Node relOpNode,
			Node dataCriteriaSectionElem, Node lhsNode, Node rhsNode) {
		
		XmlProcessor hqmfXmlProcessor = me.getHQMFXmlProcessor();
		Node relOpParentNode = relOpNode.getParentNode();
		
		try{
			Node setOpEntryNode = generateSetOpHQMF(me, lhsNode, dataCriteriaSectionElem);
			Element temporallyRelatedInfoNode = createBaseTemporalNode(relOpNode, hqmfXmlProcessor);
			
			if((relOpParentNode != null) && "subTree".equals(relOpParentNode.getNodeName())) {
				NodeList idChildNodeList = ((Element)setOpEntryNode).getElementsByTagName(ID);
				if(idChildNodeList != null && idChildNodeList.getLength() > 0){
					Node idChildNode = idChildNodeList.item(0);
					String root = relOpParentNode.getAttributes().getNamedItem("uuid").getNodeValue();
					String ext = StringUtils.deleteWhitespace(relOpNode.getAttributes().getNamedItem("displayName").getNodeValue());
					if (relOpParentNode.getAttributes().getNamedItem("qdmVariable") != null) {
						String isQdmVariable = relOpParentNode.getAttributes()
								.getNamedItem("qdmVariable").getNodeValue();
						if ("true".equalsIgnoreCase(isQdmVariable)) {
							ext = "qdm_var_" + ext;
						}
					}
					idChildNode.getAttributes().getNamedItem("extension").setNodeValue(ext);
					idChildNode.getAttributes().getNamedItem("root").setNodeValue(root);	
				}
			}
			
			handleRelOpRHS(me, dataCriteriaSectionElem, rhsNode, temporallyRelatedInfoNode);
			
			Node firstChild = setOpEntryNode.getFirstChild();
			if("localVariableName".equals(firstChild.getNodeName())){
				firstChild = firstChild.getNextSibling();
			}
			NodeList outBoundList = ((Element)firstChild).getElementsByTagName("outboundRelationship");
			if((outBoundList != null) && (outBoundList.getLength() > 0)){
				Node outBound = outBoundList.item(0);
				firstChild.insertBefore(temporallyRelatedInfoNode, outBound);
			}else{
				firstChild.appendChild(temporallyRelatedInfoNode);
			}
			
			//create comment node
			Comment comment = hqmfXmlProcessor.getOriginalDoc().createComment("entry for "+relOpNode.getAttributes().getNamedItem("displayName").getNodeValue());
			dataCriteriaSectionElem.appendChild(comment);
			dataCriteriaSectionElem.appendChild(setOpEntryNode);
			return setOpEntryNode;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param me
	 * @param relOpNode
	 * @param dataCriteriaSectionElem
	 * @param lhsNode
	 * @param rhsNode
	 * @return
	 * @throws XPathExpressionException
	 */
	private Node getrelOpLHSRelOp(MeasureExport me, Node relOpNode,
			Node dataCriteriaSectionElem, Node lhsNode, Node rhsNode) throws XPathExpressionException {
		
		XmlProcessor hqmfXmlProcessor = me.getHQMFXmlProcessor();
		Node relOpParentNode = relOpNode.getParentNode();
						
		try{
			Node relOpEntryNode = generateRelOpHQMF(me, lhsNode, dataCriteriaSectionElem);
			Element temporallyRelatedInfoNode = createBaseTemporalNode(relOpNode, hqmfXmlProcessor);
			
			if((relOpParentNode != null) && "subTree".equals(relOpParentNode.getNodeName())) {
				NodeList idChildNodeList = ((Element)relOpEntryNode).getElementsByTagName(ID);
				if(idChildNodeList != null && idChildNodeList.getLength() > 0){
					Node idChildNode = idChildNodeList.item(0);
					String root = relOpParentNode.getAttributes().getNamedItem("uuid").getNodeValue();
					String ext = StringUtils.deleteWhitespace(relOpNode.getAttributes().getNamedItem("displayName").getNodeValue());
					if (relOpParentNode.getAttributes().getNamedItem("qdmVariable") != null) {
						String isQdmVariable = relOpParentNode.getAttributes()
								.getNamedItem("qdmVariable").getNodeValue();
						if ("true".equalsIgnoreCase(isQdmVariable)) {
							ext = "qdm_var_" + ext;
						}
					}
					idChildNode.getAttributes().getNamedItem("extension").setNodeValue(ext);
					idChildNode.getAttributes().getNamedItem("root").setNodeValue(root);	
				}
			}
			
			handleRelOpRHS(me, dataCriteriaSectionElem, rhsNode, temporallyRelatedInfoNode);
			
			Node firstChild = relOpEntryNode.getFirstChild();
			if("localVariableName".equals(firstChild.getNodeName())){
				firstChild = firstChild.getNextSibling();
			}
			NodeList outBoundList = ((Element)firstChild).getElementsByTagName("outboundRelationship");
			if((outBoundList != null) && (outBoundList.getLength() > 0)){
				Node outBound = outBoundList.item(0);
				firstChild.insertBefore(temporallyRelatedInfoNode, outBound);
			}else{
				firstChild.appendChild(temporallyRelatedInfoNode);
			}
			
			//create comment node
			Comment comment = hqmfXmlProcessor.getOriginalDoc().createComment("entry for "+relOpNode.getAttributes().getNamedItem("displayName").getNodeValue());
			dataCriteriaSectionElem.appendChild(comment);
			dataCriteriaSectionElem.appendChild(relOpEntryNode);
			return relOpEntryNode;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * @param me
	 * @param relOpNode
	 * @param dataCriteriaSectionElem
	 * @param hqmfXmlProcessor
	 * @param lhsNode
	 * @param rhsNode
	 * @param relOpParentNode
	 * @param rhsName
	 * @throws XPathExpressionException
	 */
	private Node getrelOpLHSQDM(MeasureExport me, Node relOpNode,
			Node dataCriteriaSectionElem, 
			Node lhsNode, Node rhsNode)
			throws XPathExpressionException {
		
		String ext = getElementRefExt(lhsNode, me.getSimpleXMLProcessor());
		String root = lhsNode.getAttributes().getNamedItem(ID).getNodeValue();
		XmlProcessor hqmfXmlProcessor = me.getHQMFXmlProcessor();
		Node relOpParentNode = relOpNode.getParentNode();
		
		Node idNodeQDM = hqmfXmlProcessor.findNode(hqmfXmlProcessor.getOriginalDoc(), "//entry/*/id[@root='"+root+"'][@extension='"+ext+"']");
		
		if (idNodeQDM != null) {
			if((relOpParentNode != null) && "subTree".equals(relOpParentNode.getNodeName())){
				root = relOpParentNode.getAttributes().getNamedItem("uuid").getNodeValue();
			}
			ext = StringUtils.deleteWhitespace(relOpNode.getAttributes().getNamedItem("displayName").getNodeValue());
			Node entryNodeForElementRef = idNodeQDM.getParentNode().getParentNode();
			Node clonedEntryNodeForElementRef = entryNodeForElementRef.cloneNode(true);
			
			//Added logic to show qdm_variable in extension if clause is of qdm variable type.
			if (relOpParentNode != null) {
				if (relOpParentNode.getAttributes().getNamedItem("qdmVariable") != null) {
					String isQdmVariable = relOpParentNode.getAttributes()
							.getNamedItem("qdmVariable").getNodeValue();
					if ("true".equalsIgnoreCase(isQdmVariable)) {
						ext = "qdm_var_" + ext;
					}
				}
			}
			
			NodeList idChildNodeList = ((Element)clonedEntryNodeForElementRef).getElementsByTagName(ID);
			if(idChildNodeList != null && idChildNodeList.getLength() > 0){
				Node idChildNode = idChildNodeList.item(0);
				idChildNode.getAttributes().getNamedItem("extension").setNodeValue(ext);
				idChildNode.getAttributes().getNamedItem("root").setNodeValue(root);
			}
			
			Element temporallyRelatedInfoNode = createBaseTemporalNode(relOpNode, hqmfXmlProcessor);
			generateTemporalAttribute(hqmfXmlProcessor, lhsNode,temporallyRelatedInfoNode, clonedEntryNodeForElementRef, true);
			
			handleRelOpRHS(me, dataCriteriaSectionElem, rhsNode, temporallyRelatedInfoNode);
			
			Node firstChild = clonedEntryNodeForElementRef.getFirstChild();
			if("localVariableName".equals(firstChild.getNodeName())){
				firstChild = firstChild.getNextSibling();
			}
			NodeList outBoundList = ((Element)firstChild).getElementsByTagName("outboundRelationship");
			if((outBoundList != null) && (outBoundList.getLength() > 0)){
				Node outBound = outBoundList.item(0);
				firstChild.insertBefore(temporallyRelatedInfoNode, outBound);
			}else{
				firstChild.appendChild(temporallyRelatedInfoNode);
			}
			
			//create comment node
			Comment comment = hqmfXmlProcessor.getOriginalDoc().createComment("entry for "+relOpNode.getAttributes().getNamedItem("displayName").getNodeValue());
			dataCriteriaSectionElem.appendChild(comment);
			dataCriteriaSectionElem.appendChild(clonedEntryNodeForElementRef);
			return clonedEntryNodeForElementRef;
		}
		return null;
	}

	private void handleRelOpRHS(MeasureExport me, Node dataCriteriaSectionElem,
			Node rhsNode, Element temporallyRelatedInfoNode) throws XPathExpressionException {
		
		XmlProcessor hqmfXmlProcessor = me.getHQMFXmlProcessor();
		String rhsName = rhsNode.getNodeName();
		
		if("elementRef".equals(rhsName)){
			Node entryNode = generateCritRefElementRef(me, temporallyRelatedInfoNode, rhsNode, me.getHQMFXmlProcessor());
			generateTemporalAttribute(hqmfXmlProcessor, rhsNode,temporallyRelatedInfoNode, entryNode, false);
		}else if("subTreeRef".equals(rhsName)){
			generateCritRefForNode(me, temporallyRelatedInfoNode, rhsNode);
		}else{
			switch (rhsName) {
				case "setOp":
					generateCritRefSetOp(me, dataCriteriaSectionElem, hqmfXmlProcessor,
							rhsNode, temporallyRelatedInfoNode);
					break;
				case "relationalOp":
					generateRelOpHQMF(me,rhsNode,temporallyRelatedInfoNode);
					Node lastChild = temporallyRelatedInfoNode.getLastChild();
					if(lastChild.getNodeName().equals("entry")){
						temporallyRelatedInfoNode.removeChild(lastChild);
						
						Node fChild = lastChild.getFirstChild();
						if("localVariableName".equals(fChild.getNodeName())){
							fChild = fChild.getNextSibling();
						}
						
						Node criteriaNode = fChild;
						temporallyRelatedInfoNode.appendChild(criteriaNode);
						NodeList childTemporalNodeList = ((Element)criteriaNode).getElementsByTagName("temporallyRelatedInformation");
						if((childTemporalNodeList != null) && (childTemporalNodeList.getLength() > 0)){
							Node childTemporalNode = childTemporalNodeList.item(0);
							Node temporalInfoNode = childTemporalNode.getFirstChild();
							//find sourceAttribute
							NodeList childs = temporalInfoNode.getChildNodes();
							for(int c=0;c<childs.getLength();c++){
								Node child = childs.item(c);
								String childName = child.getNodeName();
								if("qdm:sourceAttribute".equals(childName)){
									Node cloneAttNode = child.cloneNode(true);
									temporallyRelatedInfoNode.getFirstChild().appendChild(cloneAttNode);
									hqmfXmlProcessor.getOriginalDoc().renameNode(cloneAttNode, "qdm", "targetAttribute");
									break;
								}
							}
						}
					}
					break;
				default:
					//Dont do anything
					break;
			}
		}
	}
	
	/**
	 * Generate temporal attribute.
	 *
	 * @param hqmfXmlProcessor the hqmf xml processor
	 * @param rhsNode the rhs node
	 * @param temporallyRelatedInfoNode the temporally related info node
	 * @param entryNode the entry node
	 * @param isSource the is source
	 * @return the node
	 */
	private Node generateTemporalAttribute(XmlProcessor hqmfXmlProcessor,
			Node rhsNode, Element temporallyRelatedInfoNode, Node entryNode, boolean isSource) {
		if(entryNode != null){
			Element entryElement = (Element)entryNode;
			
			if(!rhsNode.hasChildNodes()){
				return null;
			}else{
				Node child = rhsNode.getFirstChild();
				if(!"attribute".equals(child.getNodeName())){
					return null;
				}
				
				String value = child.getAttributes().getNamedItem("name").getNodeValue();
				List<String> validAttribNames = new ArrayList<String>();
				validAttribNames.add("incision datetime");
				validAttribNames.add("facility location arrival datetime");
				validAttribNames.add("facility location departure datetime");
				validAttribNames.add("signed datetime");
				validAttribNames.add("start datetime");
				validAttribNames.add("stop datetime");
				if(!validAttribNames.contains(value)){
					return null;
				}
				if("start datetime".equals(value) || "stop datetime".equals(value)){
					String dataType = rhsNode.getAttributes().getNamedItem("displayName").getNodeValue();
					if(!dataType.endsWith("Order")){
						return null;
					}
				}
				
				//create sourceAttribute/targetAttribute
				String attribName = "qdm:sourceAttribute";
				if(!isSource){
					attribName = "qdm:targetAttribute";
				}
				Element attribute = hqmfXmlProcessor.getOriginalDoc().createElement(attribName);
				attribute.setAttribute("name", value);
				String boundValue = "effectiveTime.low";
								
				if("incision datetime".equals(value)){
					NodeList nodeList = entryElement.getElementsByTagName("outboundRelationship");
					if((nodeList != null) && (nodeList.getLength() > 0)){
						//Always get the last outBoundRelationShip tag, because this is the one 
						//which will represent the 
						Node outBoundNode = nodeList.item(nodeList.getLength()-1);
						Node criteriaNode = outBoundNode.getFirstChild();
						
						NodeList idNodeList = ((Element)criteriaNode).getElementsByTagName(ID);
						if((idNodeList != null) && (idNodeList.getLength() > 0)){
							Node idNode = idNodeList.item(0);
							Element qdmId = hqmfXmlProcessor.getOriginalDoc().createElement("qdm:id");
							qdmId.setAttribute(ROOT, idNode.getAttributes().getNamedItem(ROOT).getNodeValue());
							qdmId.setAttribute("extension", idNode.getAttributes().getNamedItem("extension").getNodeValue());
							attribute.appendChild(qdmId);
						}
					}
				}else{
					NodeList nodeList = entryElement.getElementsByTagName("participation");
					if((nodeList != null) && (nodeList.getLength() > 0)){
						//Always get the last outBoundRelationShip tag, because this is the one 
						//which will represent the 
						Node participationNode = nodeList.item(nodeList.getLength()-1);
						Node roleNode = ((Element)participationNode).getElementsByTagName("role").item(0);
						NodeList idNodeList = ((Element)roleNode).getElementsByTagName(ID);
						if((idNodeList != null) && (idNodeList.getLength() > 0)){
							Node idNode = idNodeList.item(0);
							Node itemNode = idNode.getFirstChild();
							Element qdmId = hqmfXmlProcessor.getOriginalDoc().createElement("qdm:id");
							qdmId.setAttribute(ROOT, itemNode.getAttributes().getNamedItem(ROOT).getNodeValue());
							qdmId.setAttribute("extension", itemNode.getAttributes().getNamedItem("extension").getNodeValue());
							attribute.appendChild(qdmId);
							
							if("start datetime".equals(value)){
								boundValue = "time.low";
							}else if("stop datetime".equals(value) || "signed datetime".equals(value)){
								boundValue = "time.high";
							}else if("facility location departure datetime".equals(value)){
								boundValue = "effectiveTime.high"; 
							}
						}
					}
				}
				attribute.setAttribute("bound", boundValue);
				temporallyRelatedInfoNode.getFirstChild().appendChild(attribute);
				return attribute;
			}
		}
		return null;
	}
	
	/**
	 * Creates the base temporal node.
	 *
	 * @param relOpNode the rel op node
	 * @param hqmfXmlProcessor the hqmf xml processor
	 * @return the element
	 */
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
	
	/**
	 * Generate crit ref rel op.
	 *
	 * @param me the me
	 * @param parentNode the parent node
	 * @param hqmfXmlProcessor the hqmf xml processor
	 * @param childNode the child node
	 * @param outboundRelElem the outbound rel elem
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateCritRefRelOp(MeasureExport me, Node parentNode,
			XmlProcessor hqmfXmlProcessor, Node childNode,
			Node outboundRelElem) throws XPathExpressionException {
		Node relOpEntryNode = generateRelOpHQMF(me, childNode,parentNode);
		
		if(relOpEntryNode != null){
			Node idNode = getTagFromEntry(relOpEntryNode, ID);
			//Node critNode = relOpEntryNode.getFirstChild();
			//NodeList nodeList = ((Element)critNode).getElementsByTagName(ID);
			//if(nodeList != null && nodeList.getLength() > 0){
			if(idNode != null){
				//Node idNode = nodeList.item(0);
				NamedNodeMap idAttribMap = idNode.getAttributes();
				String idRoot = idAttribMap.getNamedItem(ROOT).getNodeValue();
				String idExt = idAttribMap.getNamedItem("extension").getNodeValue();
				
				Node parent = idNode.getParentNode();
				
				NamedNodeMap attribMap = parent.getAttributes();
				String classCode = attribMap.getNamedItem(CLASS_CODE).getNodeValue();
				String moodCode = attribMap.getNamedItem(MOOD_CODE).getNodeValue();
				
				//create criteriaRef
				Element criteriaReference = hqmfXmlProcessor.getOriginalDoc().createElement("criteriaReference");
				criteriaReference.setAttribute(CLASS_CODE, classCode);
				criteriaReference.setAttribute(MOOD_CODE, moodCode);
				
				Element id = hqmfXmlProcessor.getOriginalDoc().createElement("id");
				id.setAttribute(ROOT, idRoot);
				id.setAttribute("extension", idExt);
				
				criteriaReference.appendChild(id);
				outboundRelElem.appendChild(criteriaReference);
			}
		}
	}
	
	/**
	 * Generate crit ref set op.
	 *
	 * @param me the me
	 * @param parentNode the parent node
	 * @param hqmfXmlProcessor the hqmf xml processor
	 * @param childNode the child node
	 * @param outboundRelElem the outbound rel elem
	 * @throws XPathExpressionException the x path expression exception
	 */
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
	
	/**
	 * Generate crit ref for node.
	 *
	 * @param me the me
	 * @param outboundRelElem the outbound rel elem
	 * @param childNode the child node
	 * @throws XPathExpressionException the x path expression exception
	 */
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
	 *
	 * @param me the me
	 * @param outboundRelElem the outbound rel elem
	 * @param subTreeRefNode the sub tree ref node
	 * @param hqmfXmlProcessor the hqmf xml processor
	 * @throws XPathExpressionException the x path expression exception
	 */
	protected void generateCritRefSubTreeRef(MeasureExport me, Node outboundRelElem, Node subTreeRefNode, XmlProcessor hqmfXmlProcessor) throws XPathExpressionException {
		
		String subTreeUUID = subTreeRefNode.getAttributes().getNamedItem("id").getNodeValue();
		String root = subTreeUUID;
		
		String xpath = "/measure/subTreeLookUp/subTree[@uuid='"+subTreeUUID+"']";
		Node subTreeNode = me.getSimpleXMLProcessor().findNode(me.getSimpleXMLProcessor().getOriginalDoc(), xpath);
		if(subTreeNode != null ) {
			String isQdmVariable = subTreeNode.getAttributes()
					.getNamedItem("qdmVariable").getNodeValue();
			Node firstChild = subTreeNode.getFirstChild();
			String firstChildName = firstChild.getNodeName();
			
			String ext = StringUtils.deleteWhitespace(firstChild.getAttributes().getNamedItem("displayName").getNodeValue());
			
			
			if("elementRef".equals(firstChildName)){
				ext = firstChild.getAttributes().getNamedItem("id").getNodeValue();
			}
			if("true".equalsIgnoreCase(isQdmVariable)){
				ext = "qdm_var_"+ext;
			}
			
			/**
			 * Check if the Clause has already been generated.
			 * If it is not generated yet, then generate it by
			 * calling the 'generateSubTreeXML' method.
			 */
			if(!subTreeNodeMap.containsKey(subTreeUUID)){
				generateSubTreeXML(me, subTreeNode);
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
	
	/**
	 * Generate crit ref element ref.
	 *
	 * @param me the me
	 * @param outboundRelElem the outbound rel elem
	 * @param elementRefNode the element ref node
	 * @param hqmfXmlProcessor the hqmf xml processor
	 * @return the node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private Node generateCritRefElementRef(MeasureExport me,
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
				//return <entry> element
				return parent.getParentNode();
			}
		}else{
			//check if this is a measurement period
			String displayName = elementRefNode.getAttributes().getNamedItem("displayName").getNodeValue();
			if("Measurement Period : Timing Element".equals(displayName)){
				//create criteriaRef
				Element criteriaReference = hqmfXmlProcessor.getOriginalDoc().createElement("criteriaReference");
				criteriaReference.setAttribute(CLASS_CODE, "OBS");
				criteriaReference.setAttribute(MOOD_CODE, "EVN");
				
				Element id = hqmfXmlProcessor.getOriginalDoc().createElement("id");
				id.setAttribute(ROOT, elementRefNode.getAttributes().getNamedItem(ID).getNodeValue());
				id.setAttribute("extension", "measureperiod");
				
				criteriaReference.appendChild(id);
				outboundRelElem.appendChild(criteriaReference);
			}
		}
		return null;
	}
	
	/**
	 * Generate item count element ref.
	 *
	 * @param me the me
	 * @param populationTypeCriteriaElement the population type criteria element
	 * @param elementRefNode the element ref node
	 * @param hqmfXmlProcessor the hqmf xml processor
	 * @throws XPathExpressionException the x path expression exception
	 */
	public void generateItemCountElementRef(MeasureExport me, Element populationTypeCriteriaElement, Node elementRefNode,
			XmlProcessor hqmfXmlProcessor) throws XPathExpressionException {
		String ext = getElementRefExt(elementRefNode, me.getSimpleXMLProcessor());
		String root = elementRefNode.getAttributes().getNamedItem(ID).getNodeValue();
		Node idNodeQDM = hqmfXmlProcessor.findNode(hqmfXmlProcessor.getOriginalDoc(), "//entry/*/id[@root='"+root+"'][@extension='"+ext+"']");
		if(idNodeQDM != null){
			Node parent = idNodeQDM.getParentNode();
			if(parent != null){
				//create component for ItemCount ElmentRef
				Element  componentElem = hqmfXmlProcessor.getOriginalDoc().createElement("component");
				componentElem.setAttribute(TYPE_CODE, "COMP");
				Element  measureAttrElem = hqmfXmlProcessor.getOriginalDoc().createElement("measureAttribute");
				componentElem.appendChild(measureAttrElem);
				Element  codeElem = hqmfXmlProcessor.getOriginalDoc().createElement("code");
				codeElem.setAttribute(CODE, "ITMCNT");
				codeElem.setAttribute(CODE_SYSTEM, "2.16.840.1.113883.5.4");
				codeElem.setAttribute(CODE_SYSTEM_NAME, "HL7 Observation Value");
				Element  displayNameElem = hqmfXmlProcessor.getOriginalDoc().createElement("displayName");
				displayNameElem.setAttribute(VALUE, "Items to count");
				codeElem.appendChild(displayNameElem);
				Element valueElem = hqmfXmlProcessor.getOriginalDoc().createElement(VALUE);
				valueElem.setAttribute("xsi:type", "II");
				valueElem.setAttribute(ROOT, root);
				valueElem.setAttribute("extension", ext);
				measureAttrElem.appendChild(codeElem);
				measureAttrElem.appendChild(valueElem);
				populationTypeCriteriaElement.appendChild(componentElem);
			}
		}
	}
		
	/**
	 * Gets the element ref ext.
	 *
	 * @param elementRefNode the element ref node
	 * @param simpleXmlProcessor the simple xml processor
	 * @return the element ref ext
	 * @throws XPathExpressionException the x path expression exception
	 */
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
	
	/**
	 * Generate empty grouper.
	 *
	 * @param hqmfXmlProcessor the hqmf xml processor
	 * @param root the root
	 * @param ext the ext
	 * @return the node
	 */
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
	
	/**
	 * Generate empty outbound elem.
	 *
	 * @param hqmfXmlProcessor the hqmf xml processor
	 * @return the element
	 */
	private Element generateEmptyOutboundElem(XmlProcessor hqmfXmlProcessor) {
		Element outboundRelElem = hqmfXmlProcessor.getOriginalDoc().createElement("outboundRelationship");
		outboundRelElem.setAttribute(TYPE_CODE, "COMP");
		return outboundRelElem;
	}
	
	private Node getTagFromEntry(Node entryElem, String tagName) {
		
		String entryElemName = entryElem.getNodeName();
		if("entry".equals(entryElemName)){
			Node firstChild = entryElem.getFirstChild();
			if("localVariableName".equals(firstChild.getNodeName())){
				NodeList nodeList = ((Element)firstChild.getNextSibling()).getElementsByTagName(tagName);
				if(nodeList != null && nodeList.getLength() > 0){
					return nodeList.item(0);
				}
			}else{
				NodeList nodeList = ((Element)firstChild).getElementsByTagName(tagName);
				if(nodeList != null && nodeList.getLength() > 0){
					return nodeList.item(0);
				}
			}
		}
		return null;
	}

}
