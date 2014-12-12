package mat.server.simplexml.hqmf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
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
	
	/** The measure export. */
	MeasureExport measureExport;
	
	/** The Constant logger. */
	private static final Log logger = LogFactory
			.getLog(HQMFClauseLogicGenerator.class);
	
	/**
	 * MAP of Functional Ops NON Subset Type.
	 */
	private static final Map<String, String> FUNCTIONAL_OPS_NON_SUBSET = new HashMap<String, String>();
	/**
	 * MAP of Functional Ops Subset Type.
	 */
	private static final Map<String, String> FUNCTIONAL_OPS_SUBSET = new HashMap<String, String>();
	static {
		FUNCTIONAL_OPS_NON_SUBSET.put("FIRST", "1");
		FUNCTIONAL_OPS_NON_SUBSET.put("SECOND", "2");
		FUNCTIONAL_OPS_NON_SUBSET.put("THIRD", "3");
		FUNCTIONAL_OPS_NON_SUBSET.put("FOURTH", "4");
		FUNCTIONAL_OPS_NON_SUBSET.put("FIFTH", "5");
		
		FUNCTIONAL_OPS_SUBSET.put("MOST RECENT", "QDM_LAST");
		FUNCTIONAL_OPS_SUBSET.put("COUNT", "QDM_SUM");
		FUNCTIONAL_OPS_SUBSET.put("MIN", "QDM_MIN");
		FUNCTIONAL_OPS_SUBSET.put("MAX", "QDM_MAX");
		FUNCTIONAL_OPS_SUBSET.put("SUM", "QDM_SUM");
		FUNCTIONAL_OPS_SUBSET.put("MEDIAN", "QDM_MEDIAN");
		FUNCTIONAL_OPS_SUBSET.put("AVG", "QDM_AVERAGE");
		
	}

	/** The Constant populations. */
	private static final List<String> populations = new ArrayList<String>();  
	
	static{
		populations.add("initialPopulation");
		populations.add("denominator");
		populations.add("denominatorExclusions");
		populations.add("denominatorExceptions");
		populations.add("numerator");
		populations.add("numeratorExclusions");
		populations.add("measurePopulation");
		populations.add("measurePopulationExclusions");
		populations.add("stratum");
	}
	
	/** The sub tree node in mo map. */
	Map<String, Node> subTreeNodeInMOMap = new HashMap<String,Node>();
	
	/** The sub tree node in pop map. */
	Map<String, Node> subTreeNodeInPOPMap = new HashMap<String,Node>();
	
	
	/** The Constant FUNCTIONAL_OP_RULES_IN_POP. */
	private static final Map<String, List<String>> FUNCTIONAL_OP_RULES_IN_POP = new HashMap<String, List<String>>();
	
	/** The Constant FUNCTIONAL_OP_RULES_IN_MO. */
	private static final Map<String, List<String>> FUNCTIONAL_OP_RULES_IN_MO = new HashMap<String, List<String>>();
	
	static {
		FUNCTIONAL_OP_RULES_IN_POP.put("MEDIAN", getFunctionalOpFirstChild("MEDIAN"));
		FUNCTIONAL_OP_RULES_IN_POP.put("AVG", getFunctionalOpFirstChild("AVG"));
		FUNCTIONAL_OP_RULES_IN_POP.put("MAX", getFunctionalOpFirstChild("MAX"));
		FUNCTIONAL_OP_RULES_IN_POP.put("MIN", getFunctionalOpFirstChild("MIN"));
		FUNCTIONAL_OP_RULES_IN_POP.put("SUM", getFunctionalOpFirstChild("SUM"));
		FUNCTIONAL_OP_RULES_IN_POP.put("COUNT", getFunctionalOpFirstChild("COUNT"));
		FUNCTIONAL_OP_RULES_IN_POP.put("FIRST", getFunctionalOpFirstChild("FIRST"));
		FUNCTIONAL_OP_RULES_IN_POP.put("SECOND", getFunctionalOpFirstChild("SECOND"));
		FUNCTIONAL_OP_RULES_IN_POP.put("THIRD", getFunctionalOpFirstChild("THIRD"));
		FUNCTIONAL_OP_RULES_IN_POP.put("FOURTH", getFunctionalOpFirstChild("FOURTH"));
		FUNCTIONAL_OP_RULES_IN_POP.put("FIFTH", getFunctionalOpFirstChild("FIFTH"));
		FUNCTIONAL_OP_RULES_IN_POP.put("MOST RECENT", getFunctionalOpFirstChild("MOST RECENT"));
		FUNCTIONAL_OP_RULES_IN_POP.put("AGE AT", getFunctionalOpFirstChild("AGE AT"));
		
		/* Rules for Functions in Measure Observations*/
		FUNCTIONAL_OP_RULES_IN_MO.put("MEDIAN", getFunctionalOpFirstChildInMO("MEDIAN"));
		FUNCTIONAL_OP_RULES_IN_MO.put("AVERAGE", getFunctionalOpFirstChildInMO("AVERAGE"));
		FUNCTIONAL_OP_RULES_IN_MO.put("MIN", getFunctionalOpFirstChildInMO("MIN"));
		FUNCTIONAL_OP_RULES_IN_MO.put("SUM", getFunctionalOpFirstChildInMO("SUM"));
		FUNCTIONAL_OP_RULES_IN_MO.put("COUNT", getFunctionalOpFirstChildInMO("COUNT"));
		FUNCTIONAL_OP_RULES_IN_MO.put("DATETIMEDIFF", getFunctionalOpFirstChildInMO("DATETIMEDIFF"));
	}
	
	
	/* (non-Javadoc)
	 * @see mat.server.simplexml.hqmf.Generator#generate(mat.model.clause.MeasureExport)
	 */
	@Override
	public String generate(MeasureExport me) throws Exception {
		measureExport = me;
		//creating Map for Populations and Measure Observations.
		createUsedSubTreeRefMap();
		generateSubTreeXML();
		return null;
	}
	
	/**
	 * Generate sub tree xml.
	 *
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateSubTreeXML() throws XPathExpressionException {
		String xpath = "/measure/subTreeLookUp/subTree[not(@instance)]";
		NodeList subTreeNodeList = measureExport.getSimpleXMLProcessor().findNodeList(measureExport.getSimpleXMLProcessor().getOriginalDoc(), xpath);
		for(int i=0;i<subTreeNodeList.getLength();i++){
			Node subTreeNode = subTreeNodeList.item(i);
			String clauseName = subTreeNode.getAttributes().getNamedItem("displayName").getNodeValue();
			String uuid = subTreeNode.getAttributes().getNamedItem("uuid").getNodeValue();
			System.out.println("Calling generateSubTreeXML for:"+clauseName);
			if((subTreeNodeInPOPMap.containsKey(uuid)&&subTreeNodeInMOMap.containsKey(uuid))
					|| subTreeNodeInPOPMap.containsKey(uuid)){
				generateSubTreeXML(subTreeNode);
			}	
		}
		String xpathOccurrence = "/measure/subTreeLookUp/subTree[(@instance)]";
		NodeList occurrenceSubTreeNodeList = measureExport.getSimpleXMLProcessor().findNodeList(measureExport.getSimpleXMLProcessor().getOriginalDoc(), xpathOccurrence);
		for(int i=0;i<occurrenceSubTreeNodeList.getLength();i++){
			Node subTreeNode = occurrenceSubTreeNodeList.item(i);
			generateOccHQMF(subTreeNode);
		}
	}
	
	/**
	 * Generate sub tree xml.
	 * @param subTreeNode the sub tree node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateSubTreeXML( Node subTreeNode) throws XPathExpressionException {
		
		/**
		 * If this is an empty or NULL clause, return right now.
		 */
		if((subTreeNode == null) || !subTreeNode.hasChildNodes()){
			return;
		}
		
		/**
		 * If this is a Occurance clause then we need to find the base clause and generate HQMF for the base clause.
		 * Then we need to generate Occurance HQMF for the occurance clause.
		 */
		if(subTreeNode.getAttributes().getNamedItem("instanceOf") != null){
			String baseClauseUUID = subTreeNode.getAttributes().getNamedItem("instanceOf").getNodeValue();
			String xpath = "/measure/subTreeLookUp/subTree[@uuid = '"+baseClauseUUID+"']";
			Node baseSubTreeNode = measureExport.getSimpleXMLProcessor().findNode(measureExport.getSimpleXMLProcessor().getOriginalDoc(), xpath);
			generateSubTreeXML(baseSubTreeNode);
			generateOccHQMF(subTreeNode);			
		}
		
		String subTreeUUID = subTreeNode.getAttributes().getNamedItem("uuid").getNodeValue();
		String clauseName = subTreeNode.getAttributes().getNamedItem("displayName").getNodeValue();
		System.out.println("subTreeNodeMap:"+subTreeNodeMap);
		
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
		System.out.println("Generating HQMF for clause:'"+clauseName+"' with first child named:'"+firstChildName);
		
		XmlProcessor hqmfXmlProcessor = measureExport.getHQMFXmlProcessor();
		Element dataCriteriaSectionElem = (Element) hqmfXmlProcessor.getOriginalDoc().getElementsByTagName("dataCriteriaSection").item(0);
		
		//generate comment
		Comment comment = hqmfXmlProcessor.getOriginalDoc().createComment("Clause '"+clauseName+"'");
		dataCriteriaSectionElem.appendChild(comment);
		
		switch (firstChildName) {
			case "setOp":
				generateSetOpHQMF(firstChild,dataCriteriaSectionElem);
				break;
			case "elementRef":
				generateElementRefHQMF(firstChild,dataCriteriaSectionElem);
				break;
			case "subTreeRef":
				generateSubTreeHQMF(firstChild,dataCriteriaSectionElem);
				break;
			case "relationalOp":
				generateRelOpHQMF(firstChild, dataCriteriaSectionElem);
				break;
			case "functionalOp":
				generateFunctionalOpHQMF(firstChild,dataCriteriaSectionElem);
				break;
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
	 * Generate occ hqmf.
	 *
	 * @param subTreeNode the sub tree node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateOccHQMF( Node subTreeNode) throws XPathExpressionException {
		/**
		 * If this is an empty or NULL clause, return right now.
		 */
		if((subTreeNode == null) || (subTreeNode.getAttributes().getNamedItem("instanceOf")==null)){
			return;
		}
		XmlProcessor hqmfXmlProcessor = measureExport.getHQMFXmlProcessor();
		String occSubTreeUUID = subTreeNode.getAttributes().getNamedItem("uuid").getNodeValue();
		String qdmVariableSubTreeUUID = subTreeNode.getAttributes().getNamedItem("instanceOf").getNodeValue();
		String clauseName = subTreeNode.getAttributes().getNamedItem("displayName").getNodeValue();
		
		/**
		 * Check the 'subTreeNodeMap' to make sure the occ clause isnt already generated.
		 */
		if(subTreeNodeMap.containsKey(occSubTreeUUID)){
			logger.info("HQMF for Occ Clause "+clauseName + " is already generated. Skipping.");
			return;
		}
		
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
			
			/**
			 * The occ clause is generated now. Make an entry in the 'subTreeNodeMap' to keep track of its generation.
			 */
			subTreeNodeMap.put(occSubTreeUUID, subTreeNode);
		}
	}
	
	/**
	 * Generate functional op hqmf.
	 *
	 * @param functionalNode the functional node
	 * @param dataCriteriaSectionElem the data criteria section elem
	 * @return the node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private Node generateFunctionalOpHQMF(Node functionalNode, Element dataCriteriaSectionElem) throws XPathExpressionException {
		Node node = null;
		if(functionalNode.getChildNodes() != null){
			Node firstChildNode = functionalNode.getFirstChild();
			String firstChildName = firstChildNode.getNodeName();
			switch (firstChildName) {
				case "setOp":
					String functionOpType = functionalNode.getAttributes().getNamedItem("type").getNodeValue();
					if (FUNCTIONAL_OPS_NON_SUBSET.containsKey(functionOpType.toUpperCase())
							|| FUNCTIONAL_OPS_SUBSET.containsKey(functionOpType.toUpperCase())) {
						node = generateSetOpHQMF(firstChildNode, dataCriteriaSectionElem);
					}
					break;
				case "elementRef":
					node = generateElementRefHQMF(firstChildNode, dataCriteriaSectionElem);
					break;
				case "relationalOp":
					node = generateRelOpHQMF(firstChildNode, dataCriteriaSectionElem);
					break;
				case "functionalOp":
					//findFunctionalOpChild(firstChildNode, dataCriteriaSectionElem);
					break;
				case "subTreeRef":
					node = generateSubTreeHQMFInFunctionalOp(firstChildNode, dataCriteriaSectionElem);
					break;
				default:
					//Dont do anything
					break;
			}
		}
		return node;
		
	}
	
	/**
	 * Method to generate HQMF for function Ops with first child as subTreeRef. In this case grouperCriteria will be generated for
	 * SubTreeRef with Excerpt entry inside it for functional Op.
	 *
	 * @param firstChildNode - SubTreeRef Node.
	 * @param dataCriteriaSectionElem - Data Criteria Element.
	 * @return the node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private Node generateSubTreeHQMFInFunctionalOp(Node firstChildNode, Element dataCriteriaSectionElem) throws XPathExpressionException {
		Node parentNode = firstChildNode.getParentNode();
		
		//temp node.
		String subTreeUUID = firstChildNode.getAttributes().getNamedItem("id").getNodeValue();
		String xpath = "/measure/subTreeLookUp/subTree[@uuid='"+subTreeUUID+"']";
		Node subTreeNode = measureExport.getSimpleXMLProcessor().findNode(measureExport.getSimpleXMLProcessor().getOriginalDoc(), xpath);
		String firstChildNameOfSubTree = subTreeNode.getFirstChild().getNodeName();
		if("functionalOp".equals(firstChildNameOfSubTree)){
			String firstChildNodeName = parentNode.getAttributes().getNamedItem("type").getNodeValue();
			if(!"SATISFIES ALL".equals(firstChildNodeName)||!"SATISFIES ANY".equals(firstChildNodeName)
					|| !"AGE AT".equals(firstChildNodeName)){
				return null;
			}
		}
		Element root = measureExport.getHQMFXmlProcessor().getOriginalDoc().createElement("temp");
		generateSubTreeHQMF(firstChildNode, root);
		Element entryElement = (Element) root.getFirstChild();
		Element excerpt = generateExcerptEntryForFunctionalNode(parentNode, null, measureExport.getHQMFXmlProcessor(), entryElement);
		if(excerpt != null) {
			//create comment node
			Comment comment = measureExport.getHQMFXmlProcessor().getOriginalDoc().
					createComment("entry for " + parentNode.getAttributes().getNamedItem("displayName").getNodeValue());
			entryElement.appendChild(comment);
			entryElement.appendChild(excerpt);
		}
		dataCriteriaSectionElem.appendChild(entryElement);
		return entryElement;
	}
	
	/**
	 * This will take care of the use case where a user can create a Clause with only one
	 * QDM elementRef inside it.
	 * We will make a copy of the original entry for QDM and update the id@root and id@extension
	 * for it. This will server as an entry for the Clause.
	 *
	 * @param elementRefNode the element ref node
	 * @param parentNode the parent node
	 * @return the node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private Node generateElementRefHQMF(Node elementRefNode, Node parentNode) throws XPathExpressionException {
		XmlProcessor hqmfXmlProcessor = measureExport.getHQMFXmlProcessor();
		Node node = null;
		String ext = getElementRefExt(elementRefNode, measureExport.getSimpleXMLProcessor());
		String root = elementRefNode.getAttributes().getNamedItem(ID).getNodeValue();
		Node idNodeQDM = hqmfXmlProcessor.findNode(hqmfXmlProcessor.getOriginalDoc(), "//entry/*/id[@root='"+root+"'][@extension='"+ext+"']");
		if(idNodeQDM != null){
			Node entryElem = idNodeQDM.getParentNode().getParentNode().cloneNode(true);
			Node newIdNode = getTagFromEntry(entryElem, ID);
			
			if(newIdNode == null){
				return null;
			}
			
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
				((Element)newIdNode).setAttribute(ROOT, idroot);
				((Element)newIdNode).setAttribute("extension", idExt);
				parentNode.appendChild(entryElem);
				node = entryElem;
			} else {
				//if the the parentNode for ElementRef is other than SubTreeNode
				Element excerptElement = null;
				Node subTreeParentNode = checkIfSubTree(parNode);
				if(subTreeParentNode != null){
					root = subTreeParentNode.getAttributes().getNamedItem("uuid").getNodeValue();
					if (subTreeParentNode.getAttributes().getNamedItem("qdmVariable") != null) {
						String isQdmVariable = subTreeParentNode.getAttributes()
								.getNamedItem("qdmVariable").getNodeValue();
						if ("true".equalsIgnoreCase(isQdmVariable)) {
							ext = "qdm_var_" + ext;
						}
					}
				}
				
				Node entryNodeForElementRef = idNodeQDM.getParentNode().getParentNode();
				Node clonedEntryNodeForElementRef = entryNodeForElementRef.cloneNode(true);
				NodeList idChildNodeList = ((Element)clonedEntryNodeForElementRef).getElementsByTagName(ID);
				if((idChildNodeList != null) && (idChildNodeList.getLength() > 0)){
					Node idChildNode = idChildNodeList.item(0);
					idChildNode.getAttributes().getNamedItem("extension").setNodeValue(ext);
					idChildNode.getAttributes().getNamedItem("root").setNodeValue(root);
				}
				
				Node firstChild = clonedEntryNodeForElementRef.getFirstChild();
				if("localVariableName".equals(firstChild.getNodeName())){
					firstChild = firstChild.getNextSibling();
				}
				//Added logic to show qdm_variable in extension if clause is of qdm variable type.
				if ("functionalOp".equals(parNode.getNodeName())) {
					excerptElement = generateExcerptEntryForFunctionalNode(parNode, elementRefNode, hqmfXmlProcessor, clonedEntryNodeForElementRef);
				}
				
				if(excerptElement != null){
					Comment comment = hqmfXmlProcessor.getOriginalDoc().createComment("excerpt for "+parNode.getAttributes().getNamedItem("displayName").getNodeValue());
					firstChild.appendChild(comment);
					firstChild.appendChild(excerptElement);
				}
				//create comment node
				Comment comment = hqmfXmlProcessor.getOriginalDoc().createComment("entry for "+elementRefNode.getAttributes().getNamedItem("displayName").getNodeValue());
				parentNode.appendChild(comment);
				parentNode.appendChild(clonedEntryNodeForElementRef);
				//				clonedEntryNodeForElementRef.appendChild(excerptElement);
				node = clonedEntryNodeForElementRef;
				
			}
			
		}
		
		return node;
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
	 * @param subTreeRefNode the sub tree ref node
	 * @param parentNode the parent node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateSubTreeHQMF(Node subTreeRefNode, Node parentNode) throws XPathExpressionException {
		
		String subTreeUUID = subTreeRefNode.getAttributes().getNamedItem("id").getNodeValue();
		
		/**
		 * Check if the Clause has already been generated.
		 * If it is not generated yet, then generate it by
		 * calling the 'generateSubTreeXML' method.
		 */
		if(!subTreeNodeMap.containsKey(subTreeUUID)){
			String xpath = "/measure/subTreeLookUp/subTree[@uuid='"+subTreeUUID+"']";
			Node subTreeNode = measureExport.getSimpleXMLProcessor().findNode(measureExport.getSimpleXMLProcessor().getOriginalDoc(), xpath);
			generateSubTreeXML( subTreeNode);
		}
		
		XmlProcessor hqmfXmlProcessor = measureExport.getHQMFXmlProcessor();
		
		// creating Entry Tag
		Element entryElem = hqmfXmlProcessor.getOriginalDoc().createElement("entry");
		entryElem.setAttribute(TYPE_CODE, "DRIV");
		
		String root = "0";
		String ext = subTreeRefNode.getAttributes().getNamedItem("id").getNodeValue();
		/*Node parNode = subTreeRefNode.getParentNode();*/
		Node parNode = checkIfSubTree(subTreeRefNode.getParentNode());
		if(parNode != null){
			root = parNode.getAttributes().getNamedItem("uuid").getNodeValue();
			if (parNode.getAttributes().getNamedItem("qdmVariable") != null) {
				String isQdmVariable = parNode.getAttributes()
						.getNamedItem("qdmVariable").getNodeValue();
				if ("true".equalsIgnoreCase(isQdmVariable)) {
					ext = "qdm_var_" + ext;
				}
			}
		}
		/*
		if((parNode != null) && "subTree".equals(parNode.getNodeName())){
			root = parNode.getAttributes().getNamedItem("uuid").getNodeValue();
			//Added logic to show qdm_variable in extension if clause is of qdm variable type.
			String isQdmVariable = parNode.getAttributes().getNamedItem("qdmVariable").getNodeValue();
			if(isQdmVariable.equalsIgnoreCase("true")) {
				ext = "qdm_var_" + ext;
			}
		}*/
		Node grouperElem = generateEmptyGrouper(hqmfXmlProcessor, root, ext);
		
		//generate comment
		Comment comment = hqmfXmlProcessor.getOriginalDoc().createComment("outBoundRelationship for "+subTreeRefNode.getAttributes().getNamedItem("displayName").getNodeValue());
		grouperElem.appendChild(comment);
		
		//generate outboundRelationship
		Element outboundRelElem = generateEmptyOutboundElem(hqmfXmlProcessor);
		Element conjunctionCodeElem = hqmfXmlProcessor.getOriginalDoc().createElement("conjunctionCode");
		conjunctionCodeElem.setAttribute(CODE, "OR");
		
		outboundRelElem.appendChild(conjunctionCodeElem);
		generateCritRefForNode(outboundRelElem, subTreeRefNode);
		
		grouperElem.appendChild(outboundRelElem);
		
		entryElem.appendChild(grouperElem);
		parentNode.appendChild(entryElem);
		
	}
	
	/**
	 * This method wil generate HQMF code for setOp (UNION,INTERSECTION).
	 *
	 * @param setOpNode the set op node
	 * @param parentNode the parent node
	 * @return the node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private Node generateSetOpHQMF( Node setOpNode, Node parentNode) throws XPathExpressionException {
		
		XmlProcessor hqmfXmlProcessor = measureExport.getHQMFXmlProcessor();
		String setOpType = setOpNode.getAttributes().getNamedItem("type").getNodeValue();
		String conjunctionType = "OR";
		
		if("union".equals(setOpType) || "SATISFIES ANY".equals(setOpType)){
			conjunctionType = "OR";
		}else if("intersection".equals(setOpType) || "SATISFIES ALL".equals(setOpType)){
			conjunctionType = "AND";
		}
		
		// creating Entry Tag
		Element entryElem = hqmfXmlProcessor.getOriginalDoc().createElement("entry");
		entryElem.setAttribute(TYPE_CODE, "DRIV");
		
		//creating grouperCriteria element
		String root = "0";
		String ext = setOpType.toUpperCase();
		
		Node subTreeParentNode = checkIfSubTree(setOpNode.getParentNode());
		if (subTreeParentNode != null) {
			root = subTreeParentNode.getAttributes().getNamedItem("uuid").getNodeValue();
			if (subTreeParentNode.getAttributes().getNamedItem("qdmVariable") != null) {
				String isQdmVariable = subTreeParentNode.getAttributes()
						.getNamedItem("qdmVariable").getNodeValue();
				if ("true".equalsIgnoreCase(isQdmVariable)) {
					ext = "qdm_var_" + ext;
				}
			}
		} else {
			root = UUIDUtilClient.uuid();
		}
		
		Node grouperElem = generateEmptyGrouper(hqmfXmlProcessor, root, ext);
		Node templateIdNode = getTemplateIdForSatisfies(hqmfXmlProcessor, setOpType);
		if(templateIdNode != null){
			grouperElem.insertBefore(templateIdNode, grouperElem.getFirstChild());
		}
		
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
				generateCritRefForNode(outboundRelElem, childNode);
			}else{
				switch (childName) {
					case "setOp":
						generateCritRefSetOp(parentNode, hqmfXmlProcessor,
								childNode, outboundRelElem);
						break;
					case "relationalOp":
						generateCritRefRelOp(parentNode, hqmfXmlProcessor,
								childNode, outboundRelElem);
						break;
					case "functionalOp":
						generateCritRefFunctionalOp(childNode, outboundRelElem);
						break;
						
					default:
						//Dont do anything
						break;
				}
			}
			grouperElem.appendChild(outboundRelElem);
		}
		
		//Added logic to show qdm_variable in extension if clause is of qdm variable type.
		Node grouperEntryNode = grouperElem.cloneNode(true);
		if ("functionalOp".equals(setOpNode.getParentNode().getNodeName())) {
			Element excerptElement = generateExcerptEntryForFunctionalNode(setOpNode.getParentNode(), null, hqmfXmlProcessor, grouperEntryNode);
			Comment comment = hqmfXmlProcessor.getOriginalDoc().createComment("excerpt for "+setOpNode.getParentNode().getAttributes().getNamedItem("displayName").getNodeValue());
			grouperElem.appendChild(comment);
			grouperElem.appendChild(excerptElement);
		}
		entryElem.appendChild(grouperElem);
		parentNode.appendChild(entryElem);
		
		return entryElem;
	}
	
	/**
	 * Generate crit ref functional op.
	 *
	 * @param childNode -Node
	 * @param outboundRelElem - outBoundElement
	 * @throws XPathExpressionException -Exception
	 */
	private void generateCritRefFunctionalOp(Node childNode, Element outboundRelElem)
			throws XPathExpressionException {
		Element dataCriteriaSectionElem = (Element) measureExport.getHQMFXmlProcessor().
				getOriginalDoc().getElementsByTagName("dataCriteriaSection").item(0);
		Node entryNode = generateFunctionalOpHQMF(childNode, dataCriteriaSectionElem);
		if ((entryNode != null) && entryNode.getNodeName().equals("entry")) {
			Node fChild = entryNode.getFirstChild();
			if ("localVariableName".equals(fChild.getNodeName())) {
				fChild = fChild.getNextSibling();
			}
			//create criteriaRef
			Element criteriaReference = measureExport.getHQMFXmlProcessor().getOriginalDoc().createElement("criteriaReference");
			criteriaReference.setAttribute(CLASS_CODE, fChild.getAttributes().getNamedItem(CLASS_CODE).getNodeValue());
			criteriaReference.setAttribute(MOOD_CODE, fChild.getAttributes().getNamedItem(MOOD_CODE).getNodeValue());
			NodeList childNodeList = fChild.getChildNodes();
			for (int j = 0; j < childNodeList.getLength(); j++) {
				Node entryChildNodes = childNodeList.item(j);
				if (entryChildNodes.getNodeName().equalsIgnoreCase(ID)) {
					Element id = measureExport.getHQMFXmlProcessor().getOriginalDoc().createElement("id");
					id.setAttribute(ROOT, entryChildNodes.getAttributes().getNamedItem(ROOT).getNodeValue());
					id.setAttribute("extension", entryChildNodes.getAttributes()
							.getNamedItem("extension").getNodeValue());
					criteriaReference.appendChild(id);
					outboundRelElem.appendChild(criteriaReference);
					break;
				}
			}
		}
	}
	
	/**
	 * This method is used to create a <templateId> tag for SATISFIES ALL/SATISFIES ANY
	 * functionalOps.
	 * These are functionalOp's but are converted to setOps and treated as Groupers.
	 *
	 * @param hqmfXmlProcessor the hqmf xml processor
	 * @param type the type
	 * @return the template id for satisfies
	 */
	private Node getTemplateIdForSatisfies(XmlProcessor hqmfXmlProcessor,
			String type) {
		Node templateIdNode = null;
		
		if("SATISFIES ALL".equals(type) || "SATISFIES ANY".equals(type)){
			templateIdNode = hqmfXmlProcessor.getOriginalDoc().createElement("templateId");
			Element itemNode = hqmfXmlProcessor.getOriginalDoc().createElement("item");
			
			//initialize rootOID with the OID for SATISFIES ALL
			String rootOID = "2.16.840.1.113883.10.20.28.3.109";
			//if we are dealing with SATISFIES ANY change the OID
			if("SATISFIES ANY".equals(type)){
				rootOID = "2.16.840.1.113883.10.20.28.3.108";
			}
			itemNode.setAttribute(ROOT, rootOID);
			
			templateIdNode.appendChild(itemNode);
		}
		
		return templateIdNode;
	}
	
	/**
	 * Generate rel op hqmf.
	 *
	 * @param relOpNode the rel op node
	 * @param dataCriteriaSectionElem the data criteria section elem
	 * @return the node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private Node generateRelOpHQMF( Node relOpNode, Node dataCriteriaSectionElem) throws XPathExpressionException {
		
		if(relOpNode.getChildNodes().getLength() == 2){
			Node lhsNode = relOpNode.getFirstChild();
			Node rhsNode = relOpNode.getLastChild();
			String lhsName = lhsNode.getNodeName();
			
			if("elementRef".equals(lhsName)){
				return getrelOpLHSQDM(relOpNode, dataCriteriaSectionElem,lhsNode, rhsNode);
			}else if("relationalOp".equals(lhsName)){
				Node finalNode = getrelOpLHSRelOp(relOpNode, dataCriteriaSectionElem,lhsNode, rhsNode);
				Node relOpParentNode = relOpNode.getParentNode();
				if(relOpParentNode.getNodeName().equalsIgnoreCase("functionalOp")) {
					Element excerptElement = generateExcerptEntryForFunctionalNode(relOpNode.getParentNode()
							, lhsNode, measureExport.getHQMFXmlProcessor(), finalNode);
					if(excerptElement != null){
						Node firstNode = finalNode.getFirstChild();
						if("localVariableName".equals(firstNode.getNodeName())){
							firstNode = firstNode.getNextSibling();
						}
						Comment comment = measureExport.getHQMFXmlProcessor().getOriginalDoc().createComment("entry for "+relOpNode.getParentNode().getAttributes().getNamedItem("displayName").getNodeValue());
						firstNode.appendChild(comment);
						firstNode.appendChild(excerptElement);
					}
				}
				return finalNode;
			}else if("setOp".equals(lhsName)){
				Node finalNode =  getrelOpLHSSetOp( relOpNode, dataCriteriaSectionElem,lhsNode, rhsNode);
				Node relOpParentNode = relOpNode.getParentNode();
				if(relOpParentNode.getNodeName().equalsIgnoreCase("functionalOp")) {
					Element excerptElement = generateExcerptEntryForFunctionalNode(relOpNode.getParentNode()
							, lhsNode, measureExport.getHQMFXmlProcessor(), finalNode);
					if(excerptElement != null){
						Node firstNode = finalNode.getFirstChild();
						if("localVariableName".equals(firstNode.getNodeName())){
							firstNode = firstNode.getNextSibling();
						}
						Comment comment = measureExport.getHQMFXmlProcessor().getOriginalDoc().createComment("entry for "+relOpNode.getParentNode().getAttributes().getNamedItem("displayName").getNodeValue());
						firstNode.appendChild(comment);
						firstNode.appendChild(excerptElement);
					}
				}
				return finalNode;
			}else if("subTreeRef".equals(lhsName)){
				return getrelOpLHSSubtree(relOpNode, dataCriteriaSectionElem,lhsNode, rhsNode);
			} else if("functionalOp".equalsIgnoreCase(lhsName)) {
				return getFunctionalOpLHS(relOpNode, dataCriteriaSectionElem, lhsNode, rhsNode);
			}
		}else{
			logger.info("Relational Op:"+relOpNode.getAttributes().getNamedItem("displayName").getNodeValue()+" does not have exactly 2 children. Skipping HQMF for it.");
		}
		return null;
	}
	
	/**
	 * Gets the functional op lhs.
	 *
	 * @param relOpNode the rel op node
	 * @param dataCriteriaSectionElem the data criteria section elem
	 * @param lhsNode the lhs node
	 * @param rhsNode the rhs node
	 * @return the functional op lhs
	 * @throws XPathExpressionException the x path expression exception
	 */
	private Node getFunctionalOpLHS(Node relOpNode, Node dataCriteriaSectionElem, Node lhsNode, Node rhsNode)
			throws XPathExpressionException {
		Node entryNode = generateFunctionalOpHQMF(lhsNode, (Element)dataCriteriaSectionElem);
		Comment comment = measureExport.getHQMFXmlProcessor().getOriginalDoc().createComment("entry for "+relOpNode.getAttributes().getNamedItem("displayName").getNodeValue());
		dataCriteriaSectionElem.appendChild(comment);
		if(entryNode != null) {
			//Element temporallyRelatedInfoNode = createBaseTemporalNode(relOpNode, measureExport.getHQMFXmlProcessor());
			Element temporallyRelatedInfoNode = null;
			if(!"FULFILLS".equalsIgnoreCase(relOpNode.getAttributes().getNamedItem("type").getNodeValue())) {
				temporallyRelatedInfoNode = createBaseTemporalNode(relOpNode, measureExport.getHQMFXmlProcessor());
			} else {
				temporallyRelatedInfoNode = measureExport.getHQMFXmlProcessor().getOriginalDoc().createElement("outboundRelationship");
				temporallyRelatedInfoNode.setAttribute(TYPE_CODE, "FLFS");
			}
			handleRelOpRHS(dataCriteriaSectionElem, rhsNode, temporallyRelatedInfoNode);
			Node firstChild = entryNode.getFirstChild();
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
			dataCriteriaSectionElem.appendChild(entryNode);
		}
		return entryNode;
	}
	
	/**
	 * Gets the rel op lhs subtree.
	 *
	 * @param relOpNode the rel op node
	 * @param dataCriteriaSectionElem the data criteria section elem
	 * @param lhsNode the lhs node
	 * @param rhsNode the rhs node
	 * @return the rel op lhs subtree
	 */
	private Node getrelOpLHSSubtree( Node relOpNode,
			Node dataCriteriaSectionElem, Node lhsNode, Node rhsNode) {
		
		try{
			String subTreeUUID = lhsNode.getAttributes().getNamedItem("id").getNodeValue();
			String root = subTreeUUID;
			Node relOpParentNode = relOpNode.getParentNode();
			
			String xpath = "/measure/subTreeLookUp/subTree[@uuid='"+subTreeUUID+"']";
			Node subTreeNode = measureExport.getSimpleXMLProcessor().findNode(measureExport.getSimpleXMLProcessor().getOriginalDoc(), xpath);
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
					generateSubTreeXML(subTreeNode);
				}
				
				Node idNodeQDM = measureExport.getHQMFXmlProcessor().findNode(measureExport.getHQMFXmlProcessor().getOriginalDoc(), "//entry/*/id[@root='"+root+"'][@extension='"+ext+"']");
				if(idNodeQDM != null){
					ext = StringUtils.deleteWhitespace(relOpNode.getAttributes().getNamedItem("displayName").getNodeValue());
					if((relOpParentNode != null) && "subTree".equals(relOpParentNode.getNodeName())){
						root = relOpParentNode.getAttributes().getNamedItem("uuid").getNodeValue();
						if (relOpParentNode.getAttributes().getNamedItem("qdmVariable") != null) {
							isQdmVariable = relOpParentNode.getAttributes()
									.getNamedItem("qdmVariable").getNodeValue();
							if ("true".equalsIgnoreCase(isQdmVariable)) {
								ext = "qdm_var_" + ext;
							}
						}
					}
					
					Node entryNodeForSubTree = idNodeQDM.getParentNode().getParentNode();
					Node clonedEntryNodeForSubTree = entryNodeForSubTree.cloneNode(true);
					
					NodeList idChildNodeList = ((Element)clonedEntryNodeForSubTree).getElementsByTagName(ID);
					if((idChildNodeList != null) && (idChildNodeList.getLength() > 0)){
						Node idChildNode = idChildNodeList.item(0);
						idChildNode.getAttributes().getNamedItem("extension").setNodeValue(ext);
						idChildNode.getAttributes().getNamedItem("root").setNodeValue(root);
					}
					
					//Element temporallyRelatedInfoNode = createBaseTemporalNode(relOpNode, measureExport.getHQMFXmlProcessor());
					Element temporallyRelatedInfoNode = null;
					if(!"FULFILLS".equalsIgnoreCase(relOpNode.getAttributes().getNamedItem("type").getNodeValue())) {
						temporallyRelatedInfoNode = createBaseTemporalNode(relOpNode, measureExport.getHQMFXmlProcessor());
					} else {
						temporallyRelatedInfoNode = measureExport.getHQMFXmlProcessor().getOriginalDoc().createElement("outboundRelationship");
						temporallyRelatedInfoNode.setAttribute(TYPE_CODE, "FLFS");
					}
					handleRelOpRHS( dataCriteriaSectionElem, rhsNode, temporallyRelatedInfoNode);
					
					Node firstNode = clonedEntryNodeForSubTree.getFirstChild();
					if("localVariableName".equals(firstNode.getNodeName())){
						firstNode = firstNode.getNextSibling();
					}
					NodeList outBoundList = ((Element)firstNode).getElementsByTagName("outboundRelationship");
					if((outBoundList != null) && (outBoundList.getLength() > 0)){
						Node outBound = outBoundList.item(0);
						firstNode.insertBefore(temporallyRelatedInfoNode, outBound);
					}else{
						firstNode.appendChild(temporallyRelatedInfoNode);
					}
					// Entry for Functional Op.
					if ("functionalOp".equals(relOpParentNode.getNodeName())) {
						Element excerptElement = generateExcerptEntryForFunctionalNode(relOpParentNode, lhsNode,
								measureExport.getHQMFXmlProcessor(), clonedEntryNodeForSubTree.getFirstChild());
						if(excerptElement != null) {
							Comment comment = measureExport.getHQMFXmlProcessor().getOriginalDoc().createComment("entry for "+relOpParentNode.getAttributes().getNamedItem("displayName").getNodeValue());
							firstNode.appendChild(comment);
							firstNode.appendChild(excerptElement);
						}
					}
					//create comment node
					Comment comment = measureExport.getHQMFXmlProcessor().getOriginalDoc().createComment("entry for "+relOpNode.getAttributes().getNamedItem("displayName").getNodeValue());
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
	 * Gets the rel op lhs set op.
	 *
	 * @param relOpNode the rel op node
	 * @param dataCriteriaSectionElem the data criteria section elem
	 * @param lhsNode the lhs node
	 * @param rhsNode the rhs node
	 * @return the rel op lhs set op
	 */
	private Node getrelOpLHSSetOp(Node relOpNode,
			Node dataCriteriaSectionElem, Node lhsNode, Node rhsNode) {
		
		XmlProcessor hqmfXmlProcessor = measureExport.getHQMFXmlProcessor();
		//Node relOpParentNode = relOpNode.getParentNode();
		
		try{
			Node setOpEntryNode = generateSetOpHQMF(lhsNode, dataCriteriaSectionElem);
			//Element temporallyRelatedInfoNode = createBaseTemporalNode(relOpNode, hqmfXmlProcessor);
			Node relOpParentNode = checkIfSubTree(relOpNode.getParentNode());
			if((relOpParentNode != null)) {
				NodeList idChildNodeList = ((Element)setOpEntryNode).getElementsByTagName(ID);
				if((idChildNodeList != null) && (idChildNodeList.getLength() > 0)){
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
			Element temporallyRelatedInfoNode = null;
			if(!"FULFILLS".equalsIgnoreCase(relOpNode.getAttributes().getNamedItem("type").getNodeValue())) {
				temporallyRelatedInfoNode = createBaseTemporalNode(relOpNode, hqmfXmlProcessor);
			} else {
				temporallyRelatedInfoNode = hqmfXmlProcessor.getOriginalDoc().createElement("outboundRelationship");
				temporallyRelatedInfoNode.setAttribute(TYPE_CODE, "FLFS");
			}
			handleRelOpRHS(dataCriteriaSectionElem, rhsNode, temporallyRelatedInfoNode);
			
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
	 * Gets the rel op lhs rel op.
	 *
	 * @param relOpNode the rel op node
	 * @param dataCriteriaSectionElem the data criteria section elem
	 * @param lhsNode the lhs node
	 * @param rhsNode the rhs node
	 * @return the rel op lhs rel op
	 * @throws XPathExpressionException the x path expression exception
	 */
	private Node getrelOpLHSRelOp( Node relOpNode,
			Node dataCriteriaSectionElem, Node lhsNode, Node rhsNode) throws XPathExpressionException {
		
		XmlProcessor hqmfXmlProcessor = measureExport.getHQMFXmlProcessor();
		//Node relOpParentNode = relOpNode.getParentNode();
		
		try{
			Node relOpEntryNode = generateRelOpHQMF( lhsNode, dataCriteriaSectionElem);
			/*Element temporallyRelatedInfoNode = createBaseTemporalNode(relOpNode, hqmfXmlProcessor);*/
			
			
			Node relOpParentNode = checkIfSubTree(relOpNode.getParentNode());
			
			if((relOpParentNode != null)) {
				NodeList idChildNodeList = ((Element)relOpEntryNode).getElementsByTagName(ID);
				if((idChildNodeList != null) && (idChildNodeList.getLength() > 0)){
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
			Element temporallyRelatedInfoNode = null;
			if(!"FULFILLS".equalsIgnoreCase(relOpNode.getAttributes().getNamedItem("type").getNodeValue())) {
				temporallyRelatedInfoNode = createBaseTemporalNode(relOpNode, hqmfXmlProcessor);
			} else {
				temporallyRelatedInfoNode = hqmfXmlProcessor.getOriginalDoc().createElement("outboundRelationship");
				temporallyRelatedInfoNode.setAttribute(TYPE_CODE, "FLFS");
			}
			
			handleRelOpRHS(dataCriteriaSectionElem, rhsNode, temporallyRelatedInfoNode);
			
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
	 * Gets the rel op lhsqdm.
	 *
	 * @param relOpNode the rel op node
	 * @param dataCriteriaSectionElem the data criteria section elem
	 * @param lhsNode the lhs node
	 * @param rhsNode the rhs node
	 * @return the rel op lhsqdm
	 * @throws XPathExpressionException the x path expression exception
	 */
	private Node getrelOpLHSQDM(Node relOpNode,
			Node dataCriteriaSectionElem,
			Node lhsNode, Node rhsNode)
					throws XPathExpressionException {
		
		String ext = getElementRefExt(lhsNode, measureExport.getSimpleXMLProcessor());
		String root = lhsNode.getAttributes().getNamedItem(ID).getNodeValue();
		XmlProcessor hqmfXmlProcessor = measureExport.getHQMFXmlProcessor();
		Node relOpParentNode = relOpNode.getParentNode();
		Element excerptElement = null;
		Node idNodeQDM = hqmfXmlProcessor.findNode(hqmfXmlProcessor.getOriginalDoc(), "//entry/*/id[@root='"+root+"'][@extension='"+ext+"']");
		
		if ((relOpParentNode != null) && (idNodeQDM != null)) {
			ext = StringUtils.deleteWhitespace(relOpNode.getAttributes().getNamedItem("displayName").getNodeValue());
			Node subTreeParentNode = checkIfSubTree(relOpParentNode);
			if(subTreeParentNode != null){
				root = subTreeParentNode.getAttributes().getNamedItem("uuid").getNodeValue();
				if (subTreeParentNode.getAttributes().getNamedItem("qdmVariable") != null) {
					String isQdmVariable = subTreeParentNode.getAttributes()
							.getNamedItem("qdmVariable").getNodeValue();
					if ("true".equalsIgnoreCase(isQdmVariable)) {
						ext = "qdm_var_" + ext;
					}
				}
			}
			
			Node entryNodeForElementRef = idNodeQDM.getParentNode().getParentNode();
			Node clonedEntryNodeForElementRef = entryNodeForElementRef.cloneNode(true);
			NodeList idChildNodeList = ((Element)clonedEntryNodeForElementRef).getElementsByTagName(ID);
			if((idChildNodeList != null) && (idChildNodeList.getLength() > 0)){
				Node idChildNode = idChildNodeList.item(0);
				idChildNode.getAttributes().getNamedItem("extension").setNodeValue(ext);
				idChildNode.getAttributes().getNamedItem("root").setNodeValue(root);
			}
			//Added logic to show qdm_variable in extension if clause is of qdm variable type.
			if ("functionalOp".equals(relOpParentNode.getNodeName())) {
				excerptElement = generateExcerptEntryForFunctionalNode(relOpParentNode, lhsNode, hqmfXmlProcessor, clonedEntryNodeForElementRef);
			}
			Element temporallyRelatedInfoNode = null;
			if(!"FULFILLS".equalsIgnoreCase(relOpNode.getAttributes().getNamedItem("type").getNodeValue())) {
				temporallyRelatedInfoNode = createBaseTemporalNode(relOpNode, hqmfXmlProcessor);
				generateTemporalAttribute(hqmfXmlProcessor, lhsNode,temporallyRelatedInfoNode, clonedEntryNodeForElementRef, true);
			} else {
				temporallyRelatedInfoNode = hqmfXmlProcessor.getOriginalDoc().createElement("outboundRelationship");
				temporallyRelatedInfoNode.setAttribute(TYPE_CODE, "FLFS");
			}
			handleRelOpRHS(dataCriteriaSectionElem, rhsNode, temporallyRelatedInfoNode);
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
			
			if(excerptElement != null){
				Comment comment = hqmfXmlProcessor.getOriginalDoc().createComment("excerpt for "+relOpParentNode.getAttributes().getNamedItem("displayName").getNodeValue());
				firstChild.appendChild(comment);
				firstChild.appendChild(excerptElement);
			}
			//create comment node
			Comment comment = hqmfXmlProcessor.getOriginalDoc().createComment("entry for "+relOpNode.getAttributes().getNamedItem("displayName").getNodeValue());
			dataCriteriaSectionElem.appendChild(comment);
			dataCriteriaSectionElem.appendChild(clonedEntryNodeForElementRef);
			return clonedEntryNodeForElementRef;
		}
		return null;
	}
	
	/**
	 * Handle rel op rhs.
	 *
	 * @param dataCriteriaSectionElem the data criteria section elem
	 * @param rhsNode the rhs node
	 * @param temporallyRelatedInfoNode the temporally related info node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void handleRelOpRHS( Node dataCriteriaSectionElem,
			Node rhsNode, Element temporallyRelatedInfoNode) throws XPathExpressionException {
		
		XmlProcessor hqmfXmlProcessor = measureExport.getHQMFXmlProcessor();
		String rhsName = rhsNode.getNodeName();
		
		if("elementRef".equals(rhsName)){
			Node entryNode = generateCritRefElementRef(temporallyRelatedInfoNode, rhsNode, measureExport.getHQMFXmlProcessor());
			generateTemporalAttribute(hqmfXmlProcessor, rhsNode,temporallyRelatedInfoNode, entryNode, false);
		}else if("subTreeRef".equals(rhsName)){
			generateCritRefForNode(temporallyRelatedInfoNode, rhsNode);
		}else{
			switch (rhsName) {
				case "setOp":
					generateCritRefSetOp(dataCriteriaSectionElem, hqmfXmlProcessor,
							rhsNode, temporallyRelatedInfoNode);
					break;
				case "relationalOp":
					generateRelOpHQMF(rhsNode,temporallyRelatedInfoNode);
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
				case "functionalOp" :
					Node entryNode = generateFunctionalOpHQMF(rhsNode , (Element) dataCriteriaSectionElem);
					if ((entryNode !=null) && entryNode.getNodeName().equals("entry")) {
						Node fChild = entryNode.getFirstChild();
						if ("localVariableName".equals(fChild.getNodeName())) {
							fChild = fChild.getNextSibling();
						}
						//create criteriaRef
						Element criteriaReference = hqmfXmlProcessor.getOriginalDoc().createElement("criteriaReference");
						criteriaReference.setAttribute(CLASS_CODE, fChild.getAttributes().getNamedItem(CLASS_CODE).getNodeValue());
						criteriaReference.setAttribute(MOOD_CODE, fChild.getAttributes().getNamedItem(MOOD_CODE).getNodeValue());
						NodeList childNodeList = fChild.getChildNodes();
						for(int i =0; i< childNodeList.getLength();i++) {
							Node childNode = childNodeList.item(i);
							if(childNode.getNodeName().equalsIgnoreCase(ID)){
								Element id = hqmfXmlProcessor.getOriginalDoc().createElement("id");
								id.setAttribute(ROOT, childNode.getAttributes().getNamedItem(ROOT).getNodeValue());
								id.setAttribute("extension", childNode.getAttributes().getNamedItem("extension").getNodeValue());
								criteriaReference.appendChild(id);
								temporallyRelatedInfoNode.appendChild(criteriaReference);
								break;
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
	 * Generate Excerpt for Functional Op used with timing/Relationship.
	 *
	 * @param functionalOpNode the functional op node
	 * @param lhsNode the lhs node
	 * @param hqmfXmlProcessor the hqmf xml processor
	 * @param clonedNodeToAppendExcerpt the cloned node to append excerpt
	 * @return the element
	 * @throws XPathExpressionException the x path expression exception
	 */
	private Element generateExcerptEntryForFunctionalNode(Node functionalOpNode, Node lhsNode,
			XmlProcessor hqmfXmlProcessor, Node clonedNodeToAppendExcerpt)
					throws XPathExpressionException {
		Element excerptElement = hqmfXmlProcessor.getOriginalDoc().createElement("excerpt");
		String functionalOpName = functionalOpNode.getAttributes().getNamedItem("type").getNodeValue();
		Element criteriaElement = null;
		if(FUNCTIONAL_OPS_NON_SUBSET.containsKey(functionalOpName.toUpperCase())) {
			Element sequenceElement = hqmfXmlProcessor.getOriginalDoc().createElement("sequenceNumber");
			sequenceElement.setAttribute(VALUE, FUNCTIONAL_OPS_NON_SUBSET.get(functionalOpName.toUpperCase()));
			excerptElement.appendChild(sequenceElement);
			if (clonedNodeToAppendExcerpt != null) {
				if(clonedNodeToAppendExcerpt.getNodeName().contains("grouper")) {
					criteriaElement = generateCriteriaElementForSetOpExcerpt(hqmfXmlProcessor, clonedNodeToAppendExcerpt);
					excerptElement.appendChild(criteriaElement);
				} else {
					NodeList entryChildNodes = clonedNodeToAppendExcerpt.getChildNodes();
					criteriaElement = generateCriteriaElementForExcerpt(hqmfXmlProcessor, entryChildNodes);
					excerptElement.appendChild(criteriaElement);
				}
			}
		} else if (FUNCTIONAL_OPS_SUBSET.containsKey(functionalOpName.toUpperCase())) {
			NamedNodeMap attributeMap = functionalOpNode.getAttributes();
			if(clonedNodeToAppendExcerpt.getNodeName().contains("grouper")) {
				criteriaElement = generateCriteriaElementForSetOpExcerpt(hqmfXmlProcessor, clonedNodeToAppendExcerpt);
				excerptElement.appendChild(criteriaElement);
			} else {
				NodeList entryChildNodes = clonedNodeToAppendExcerpt.getChildNodes();
				criteriaElement = generateCriteriaElementForExcerpt(hqmfXmlProcessor, entryChildNodes);
				excerptElement.appendChild(criteriaElement);
			}
			if (clonedNodeToAppendExcerpt != null) {
				
				if ("count".equalsIgnoreCase(functionalOpName)) {
					
					createRepeatNumberTagForCountFuncttion(hqmfXmlProcessor, attributeMap, criteriaElement);
					Element qdmSubSetElement = hqmfXmlProcessor.getOriginalDoc().createElement("qdm:subsetCode");
					qdmSubSetElement.setAttribute(CODE, FUNCTIONAL_OPS_SUBSET.get(functionalOpName.toUpperCase()));
					Element subSetCodeElement = hqmfXmlProcessor.getOriginalDoc().createElement("subsetCode");
					subSetCodeElement.setAttribute(CODE, "SUM");
					
					excerptElement.appendChild(subSetCodeElement);
					excerptElement.appendChild(qdmSubSetElement);
					excerptElement.appendChild(criteriaElement);
				} else {
					if ((attributeMap.getNamedItem("operatorType") != null) && (lhsNode != null)) {
						String lhsNodeType = lhsNode.getNodeName();
						if ("elementRef".equalsIgnoreCase(lhsNodeType)) {
							String qdmUUID = lhsNode.getAttributes().getNamedItem("id").getNodeValue();
							String xPath = "/measure/elementLookUp/qdm[@uuid ='"+qdmUUID+"']";
							Node node = measureExport.getSimpleXMLProcessor().findNode(measureExport.getSimpleXMLProcessor().getOriginalDoc(), xPath);
							if((node != null) && lhsNode.hasChildNodes()){
								Node qdmNode = node.cloneNode(true);
								Node attributeNode = lhsNode.getFirstChild().cloneNode(true);
								attributeNode.setUserData(ATTRIBUTE_NAME, attributeNode.getAttributes().getNamedItem(NAME).getNodeValue(), null);
								attributeNode.setUserData(ATTRIBUTE_MODE,attributeMap.getNamedItem("operatorType").getNodeValue(), null);
								attributeNode.setUserData(ATTRIBUTE_UUID, attributeNode.getAttributes().getNamedItem("attrUUID").getNodeValue(), null);
								Element attributeElement = (Element)attributeNode;
								
								attributeElement.setAttribute("mode", attributeMap.getNamedItem("operatorType").getNodeValue());
								if(attributeElement.getAttributes().getNamedItem("attrDate") != null){
									attributeNode.setUserData(ATTRIBUTE_DATE, attributeMap.getNamedItem("quantity").getNodeValue(),null);
								} else {
									attributeElement.setAttribute("comparisonValue", attributeMap.getNamedItem("quantity").getNodeValue());
								}
								if(attributeMap.getNamedItem("unit") != null){
									attributeElement.setAttribute("unit", attributeMap.getNamedItem("unit").getNodeValue());
								} else {
									if(attributeElement.getAttributes().getNamedItem("unit") != null){
										attributeElement.removeAttribute("unit");
									}
								}
								attributeNode = attributeElement;
								
								HQMFAttributeGenerator attributeGenerator = new HQMFAttributeGenerator();
								attributeGenerator.generateAttributeTagForFunctionalOp(qdmNode, criteriaElement, measureExport.getHQMFXmlProcessor()
										, measureExport.getSimpleXMLProcessor(), attributeNode);
								
								
							}
						}
					}
					Element qdmSubSetElement = hqmfXmlProcessor.getOriginalDoc().createElement("qdm:subsetCode");
					qdmSubSetElement.setAttribute(CODE, FUNCTIONAL_OPS_SUBSET.get(functionalOpName.toUpperCase()));
					
					if("sum".equalsIgnoreCase(functionalOpName)){
						Element subSetCodeElement = hqmfXmlProcessor.getOriginalDoc().createElement("subsetCode");
						subSetCodeElement.setAttribute(CODE, "SUM");
						excerptElement.appendChild(subSetCodeElement);
					}
					excerptElement.appendChild(qdmSubSetElement);
					excerptElement.appendChild(criteriaElement);
				}
			}
		}
		
		return excerptElement;
	}
	/**
	 * Check if parent sub tree.
	 *
	 * @param parentNode the parent node
	 * @return the node
	 */
	private Node checkIfParentSubTree(Node parentNode){
		Node returnNode = null;
		if(parentNode!=null){
			String parentName = parentNode.getNodeName();
			if("subTree".equals(parentName)){
				returnNode = parentNode;
			} else {
				returnNode = checkIfParentSubTree(parentNode.getParentNode());
			}
		}
		
		return returnNode;
	}
	/**
	 * Generates RepeatNumber tags for Count Function.
	 * @param hqmfXmlProcessor - XmlProcessor.
	 * @param attributeMap - NamedNodeMap.
	 * @param criteriaElement - Element.
	 */
	private void createRepeatNumberTagForCountFuncttion(XmlProcessor hqmfXmlProcessor, NamedNodeMap attributeMap,
			Element criteriaElement) {
		Element repeatNumberElement = hqmfXmlProcessor.getOriginalDoc().createElement("repeatNumber");
		Element lowNode = hqmfXmlProcessor.getOriginalDoc().createElement("low");
		Element highNode = hqmfXmlProcessor.getOriginalDoc().createElement("high");
		if (attributeMap.getNamedItem("operatorType") != null) {
			String operatorType = attributeMap.getNamedItem("operatorType").getNodeValue();
			String quantity = attributeMap.getNamedItem("quantity").getNodeValue();
			if (operatorType.startsWith("Greater Than")) {
				lowNode.setAttribute("value", quantity);
				highNode.setAttribute(NULL_FLAVOR, "PINF");
				if ("Greater Than or Equal To".equals(operatorType)) {
					repeatNumberElement.setAttribute("lowClosed", "true");
				}
			} else if ("Equal To".equals(operatorType)) {
				repeatNumberElement.setAttribute("lowClosed", "true");
				repeatNumberElement.setAttribute("highClosed", "true");
				lowNode.setAttribute("value", quantity);
				highNode.setAttribute("value", quantity);
			} else if (operatorType.startsWith("Less Than")) {
				repeatNumberElement.setAttribute("lowClosed", "true");
				lowNode.setAttribute("value", "0");
				highNode.setAttribute("value", quantity);
				if ("Less Than or Equal To".equals(operatorType)) {
					repeatNumberElement.setAttribute("highClosed", "true");
				}
			}
			repeatNumberElement.appendChild(lowNode);
			repeatNumberElement.appendChild(highNode);
			criteriaElement.appendChild(repeatNumberElement);
		}
	}
	
	/**
	 * Generate criteria element for excerpt.
	 *
	 * @param hqmfXmlProcessor the hqmf xml processor
	 * @param entryChildNodes the entry child nodes
	 * @return the element
	 */
	private Element generateCriteriaElementForExcerpt(XmlProcessor hqmfXmlProcessor, NodeList entryChildNodes) {
		Element criteriaElement = null;
		for (int i = 0; i < entryChildNodes.getLength(); i++) {
			Node childNode = entryChildNodes.item(i);
			String childNodeName = childNode.getNodeName();
			if (childNodeName.contains("Criteria")) {
				criteriaElement = hqmfXmlProcessor.getOriginalDoc().createElement(childNodeName);
				criteriaElement.setAttribute(CLASS_CODE, childNode.getAttributes().getNamedItem(CLASS_CODE).getNodeValue());
				criteriaElement.setAttribute(MOOD_CODE, childNode.getAttributes().getNamedItem(MOOD_CODE).getNodeValue());
				NodeList criteriaChildNodes = childNode.getChildNodes();
				for(int j = 0; j < criteriaChildNodes.getLength(); j++){
					Node criteriaChildNode = criteriaChildNodes.item(j);
					if(ID.equalsIgnoreCase(criteriaChildNode.getNodeName())) {
						Element idElement = hqmfXmlProcessor.getOriginalDoc().createElement(ID);
						idElement.setAttribute(ROOT, criteriaChildNode.getAttributes().getNamedItem(ROOT).getNodeValue());
						idElement.setAttribute("extension", criteriaChildNode.getAttributes().getNamedItem("extension").getNodeValue());
						criteriaElement.appendChild(idElement);
						break;
					}
				}
				
				break;
			}
		}
		return criteriaElement;
	}
	
	/**
	 * Generate criteria element for set op excerpt.
	 *
	 * @param hqmfXmlProcessor the hqmf xml processor
	 * @param clonedNodeToAppendExcerpt the cloned node to append excerpt
	 * @return the element
	 */
	private Element generateCriteriaElementForSetOpExcerpt(XmlProcessor hqmfXmlProcessor, Node clonedNodeToAppendExcerpt) {
		Element criteriaElement = null;
		for (int i = 0; i < clonedNodeToAppendExcerpt.getChildNodes().getLength(); i++) {
			Node childNode = clonedNodeToAppendExcerpt.getChildNodes().item(i);
			if(ID.equalsIgnoreCase(childNode.getNodeName())) {
				Node criteriaNode = generateEmptyGrouper(hqmfXmlProcessor, childNode.getAttributes().getNamedItem(ROOT).getNodeValue()
						,  childNode.getAttributes().getNamedItem("extension").getNodeValue());
				criteriaElement = (Element) criteriaNode;
				break;
			}
		}
		return criteriaElement;
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
	 * @param parentNode the parent node
	 * @param hqmfXmlProcessor the hqmf xml processor
	 * @param childNode the child node
	 * @param outboundRelElem the outbound rel elem
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateCritRefRelOp( Node parentNode,
			XmlProcessor hqmfXmlProcessor, Node childNode,
			Node outboundRelElem) throws XPathExpressionException {
		Node relOpEntryNode = generateRelOpHQMF(childNode,parentNode);
		
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
	 * @param parentNode the parent node
	 * @param hqmfXmlProcessor the hqmf xml processor
	 * @param childNode the child node
	 * @param outboundRelElem the outbound rel elem
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateCritRefSetOp( Node parentNode,
			XmlProcessor hqmfXmlProcessor, Node childNode,
			Node outboundRelElem) throws XPathExpressionException {
		
		Node setOpEntry = generateSetOpHQMF(childNode,parentNode);
		NodeList childList = setOpEntry.getChildNodes();
		for(int j=0;j<childList.getLength();j++){
			Node child = childList.item(j);
			if("grouperCriteria".equals(child.getNodeName())){
				NodeList idChildList = ((Element)child).getElementsByTagName(ID);
				if(idChildList.getLength() > 0){
					Node idChild = idChildList.item(0);
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
	}
	
	/**
	 * Generate crit ref for node.
	 *
	 * @param outboundRelElem the outbound rel elem
	 * @param childNode the child node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateCritRefForNode(Node outboundRelElem, Node childNode) throws XPathExpressionException {
		XmlProcessor hqmfXmlProcessor = measureExport.getHQMFXmlProcessor();
		String childName = childNode.getNodeName();
		
		switch(childName){
			case "elementRef":
				generateCritRefElementRef( outboundRelElem, childNode,hqmfXmlProcessor);
				break;
			case "subTreeRef":
				generateCritRefSubTreeRef(outboundRelElem, childNode, hqmfXmlProcessor, true);
				break;
				
			default:
				break;
		}
		
	}
	
	/**
	 * This method will basically create a <criteriaReference> with classCode='GROUPER' and moodCode='EVN'
	 * and have the <id> tag pointing to the <grouperCriteria> for the referenced subTree/clause.
	 *
	 * @param outboundRelElem the outbound rel elem
	 * @param subTreeRefNode the sub tree ref node
	 * @param hqmfXmlProcessor the hqmf xml processor
	 * @throws XPathExpressionException the x path expression exception
	 */
	protected void generateCritRefSubTreeRef(Node outboundRelElem, Node subTreeRefNode, XmlProcessor hqmfXmlProcessor) throws XPathExpressionException {
		generateCritRefSubTreeRef(outboundRelElem, subTreeRefNode, hqmfXmlProcessor, false);
	}
	
	
	/**
	 * This method will basically create a <criteriaReference> with classCode='GROUPER' and moodCode='EVN'
	 * and have the <id> tag pointing to the <grouperCriteria> for the referenced subTree/clause.
	 *
	 * @param outboundRelElem the outbound rel elem
	 * @param subTreeRefNode the sub tree ref node
	 * @param hqmfXmlProcessor the hqmf xml processor
	 * @param checkExisting check in the map if already existing
	 * @throws XPathExpressionException the x path expression exception
	 */
	protected void generateCritRefSubTreeRef( Node outboundRelElem, Node subTreeRefNode, XmlProcessor hqmfXmlProcessor, boolean checkExisting) throws XPathExpressionException {
		
		String subTreeUUID = subTreeRefNode.getAttributes().getNamedItem("id").getNodeValue();
		String root = subTreeUUID;
		
		String xpath = "/measure/subTreeLookUp/subTree[@uuid='"+subTreeUUID+"']";
		Node subTreeNode = measureExport.getSimpleXMLProcessor().findNode(measureExport.getSimpleXMLProcessor().getOriginalDoc(), xpath);
		if(subTreeNode != null ) {
			String isQdmVariable = subTreeNode.getAttributes()
					.getNamedItem("qdmVariable").getNodeValue();
			Node firstChild = subTreeNode.getFirstChild();
			String firstChildName = firstChild.getNodeName();
			
			String ext = StringUtils.deleteWhitespace(firstChild.getAttributes().getNamedItem("displayName").getNodeValue());
			String occText = null;
			// Handled Occurrence Of QDM Variable.
			if(subTreeNode.getAttributes().getNamedItem("instanceOf") != null){
				occText = "occ"+subTreeNode.getAttributes().getNamedItem("instance").getNodeValue()+"of_";
			}
			if(subTreeNode.getAttributes().getNamedItem("instanceOf") == null) {
				if("elementRef".equals(firstChildName) ){
					ext = firstChild.getAttributes().getNamedItem("id").getNodeValue();
				} else if("functionalOp".equals(firstChildName)){
					if(firstChild.getFirstChild() != null) {
						ext = StringUtils.deleteWhitespace(firstChild.getFirstChild().getAttributes()
								.getNamedItem("displayName").getNodeValue().replaceAll(":", "_"));
						if(firstChild.getFirstChild().getNodeName().equalsIgnoreCase("subTreeRef")){
							ext = firstChild.getFirstChild().getAttributes()
									.getNamedItem(ID).getNodeValue();
						}
					}
				}
			}
			
			
			if("true".equalsIgnoreCase(isQdmVariable)){
				if (occText != null) {
					ext = occText + "qdm_var_"+ext;
				} else {
					ext = "qdm_var_"+ext;
				}
				
			}
			
			/**
			 * Check if the Clause has already been generated.
			 * If it is not generated yet, then generate it by
			 * calling the 'generateSubTreeXML' method.
			 */
			if(checkExisting && !subTreeNodeMap.containsKey(subTreeUUID)){
				generateSubTreeXML( subTreeNode);
			}
			System.out.println("Finding entry for ");
			System.out.println("root="+root);
			System.out.println("ext="+ext);
			Node idNodeQDM = hqmfXmlProcessor.findNode(hqmfXmlProcessor.getOriginalDoc(), "//entry/*/id[@root='"+root+"'][@extension='"+ext+"']");
			System.out.println("idNodeQDM == null:"+(idNodeQDM == null));
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
	 * @param outboundRelElem the outbound rel elem
	 * @param elementRefNode the element ref node
	 * @param hqmfXmlProcessor the hqmf xml processor
	 * @return the node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private Node generateCritRefElementRef(
			Node outboundRelElem, Node elementRefNode,
			XmlProcessor hqmfXmlProcessor) throws XPathExpressionException {
		String ext = getElementRefExt(elementRefNode, measureExport.getSimpleXMLProcessor());
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
		idElem.setAttribute("extension",  StringUtils.deleteWhitespace(ext));
		
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
	
	/**
	 * Gets the tag from entry.
	 *
	 * @param entryElem the entry elem
	 * @param tagName the tag name
	 * @return the tag from entry
	 */
	private Node getTagFromEntry(Node entryElem, String tagName) {
		
		String entryElemName = entryElem.getNodeName();
		if("entry".equals(entryElemName)){
			Node firstChild = entryElem.getFirstChild();
			if("localVariableName".equals(firstChild.getNodeName())){
				NodeList nodeList = ((Element)firstChild.getNextSibling()).getElementsByTagName(tagName);
				if((nodeList != null) && (nodeList.getLength() > 0)){
					return nodeList.item(0);
				}
			}else{
				NodeList nodeList = ((Element)firstChild).getElementsByTagName(tagName);
				if((nodeList != null) && (nodeList.getLength() > 0)){
					return nodeList.item(0);
				}
			}
		}
		return null;
	}
	
	/**
	 * Check If the parentNode is a "subTree" node.
	 * Or else, if parent is a 'functionalOp' then recursively check if the parentNode's parent
	 * is a 'subTree'.
	 * If yes, then return true.
	 *
	 * @param parentNode the parent node
	 * @return boolean
	 */
	private Node checkIfSubTree(Node parentNode) {
		Node returnNode = null;
		if(parentNode != null){
			String parentName = parentNode.getNodeName();
			if("subTree".equals(parentName)){
				returnNode = parentNode;
			}else if("functionalOp".equals(parentName)){
				returnNode = checkIfSubTree(parentNode.getParentNode());
			}
		}
		return returnNode;
	}
	
	/**
	 * Gets the firt child list.
	 *
	 * @param function the function
	 * @return the firt child list
	 */
	public static List<String> getFunctionalOpFirstChild(String function){
		List<String> childList =new ArrayList<String>();
		if("AGE AT".equals(function)){
			childList.add("subTreeRef");
			childList.add("relationalOp");
			childList.add("functionalOp");
			childList.add("elementRef");
		} else {
			childList.add("elementRef");
			childList.add("setOP");
			childList.add("subTreeRef");
			childList.add("relationalOp");
		}
		return childList;
	}
	
	/**
	 * Gets the aggregate and instance function childs.
	 *
	 * @param typeChild the type child
	 * @return the aggregate and instance function childs
	 */
	public static List<String> getAggregateAndInstanceFunctionChilds(String typeChild){
		List<String> aggregateList = new ArrayList<String>();
		aggregateList.add("FIRST");
		aggregateList.add("SECOND");
		aggregateList.add("THIRD");
		aggregateList.add("FOURTH");
		aggregateList.add("FIFTH");
		aggregateList.add("MOST RECENT");
		if("AGGREGATE".equals(typeChild)){
			aggregateList.add("DATETIMEDIFF");
		}
		return aggregateList;
	}
	
	/**
	 * Gets the functional op first child in mo.
	 *
	 * @param function the function
	 * @return the functional op first child in mo
	 */
	public static List<String> getFunctionalOpFirstChildInMO(String function){
		List<String> childList  = new ArrayList<String>();
		if("DATETIMEDIFF".equals(function)){
			childList.add("elementRef");
			childList.add("subTreeRef");
			childList.add("relationalOp");
			childList.addAll(getAggregateAndInstanceFunctionChilds("INSTANCE"));
		} else {
			childList.addAll(getFunctionalOpFirstChild(function));
			childList.addAll(getAggregateAndInstanceFunctionChilds("AGGREGATE"));
		}
		
		return childList;
	}
	
	
     /**
      * Check for used Used sub tree ref Node map in Populations and Meausre Observations.
      */
     private void createUsedSubTreeRefMap(){
		
		XmlProcessor simpleXmlProcessor = measureExport.getSimpleXMLProcessor();
		String typeXpathString ="";
		List<String> usedSubTreeRefIdsPop = new ArrayList<String>();
		List<String> usedSubTreeRefIdsMO = new ArrayList<String>();
		for(String typeString : populations){
			typeXpathString += "@type = '"+typeString + "' or";
		}
		typeXpathString = typeXpathString.substring(0,typeXpathString.lastIndexOf(" or"));
		String xpathForSubTreeInPOPClause = "/measure/measureGrouping//clause["+typeXpathString+"]//subTreeRef/@id";
		String xpathForSubTreeInMOClause = "/measure/measureGrouping//clause[@type='measureObservation']//subTreeRef/@id";
		try {
			
			//creating used Subtree Red Map in Populations
			NodeList populationsSubTreeNode = simpleXmlProcessor.findNodeList(simpleXmlProcessor.getOriginalDoc(), xpathForSubTreeInPOPClause);
			for(int i=0;i<populationsSubTreeNode.getLength();i++){
				String uuid = populationsSubTreeNode.item(i).getNodeValue();
				uuid = checkIfQDMVarInstanceIsPresent(uuid, simpleXmlProcessor);
				if(!usedSubTreeRefIdsPop.contains(uuid)){
					usedSubTreeRefIdsPop.add(uuid);
				}
			}
			usedSubTreeRefIdsPop = checkUnUsedSubTreeRef(simpleXmlProcessor, usedSubTreeRefIdsPop);
			for(String uuid: usedSubTreeRefIdsPop){
				Node subTreeNode = createUsedSubTreeRefMap(simpleXmlProcessor, uuid);
				subTreeNodeInPOPMap.put(uuid, subTreeNode);
			}
			
			//creating used Subtree Red Map in Measure Observations 
			NodeList measureObsSubTreeNode = simpleXmlProcessor.findNodeList(simpleXmlProcessor.getOriginalDoc(), xpathForSubTreeInMOClause);
			for(int i=0;i<measureObsSubTreeNode.getLength();i++){
				String uuid = measureObsSubTreeNode.item(i).getNodeValue();
				uuid = checkIfQDMVarInstanceIsPresent(uuid, simpleXmlProcessor);
				if(!usedSubTreeRefIdsMO.contains(uuid)){
					usedSubTreeRefIdsMO.add(uuid);
				}
			}
			usedSubTreeRefIdsMO = checkUnUsedSubTreeRef(simpleXmlProcessor, usedSubTreeRefIdsMO);
			for(String uuid: usedSubTreeRefIdsMO){
				Node subTreeNode = createUsedSubTreeRefMap(simpleXmlProcessor, uuid);
				subTreeNodeInMOMap.put(uuid, subTreeNode);
			}
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
     
     /**
      * Creates the used sub tree ref map.
      *
      * @param simpleXmlProcessor the simple xml processor
      * @param uuid the uuid
      * @return the node
      */
     private Node createUsedSubTreeRefMap(XmlProcessor simpleXmlProcessor, String uuid) {	 
    		 String xpathforUsedSubTreeMap ="/measure/subTreeLookUp/subTree[@uuid='"+uuid+"']";
    		 Node subTreeNode = null;
    		 try {
				 subTreeNode = simpleXmlProcessor.findNode(simpleXmlProcessor.getOriginalDoc(), xpathforUsedSubTreeMap);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		return subTreeNode;
	}

	/**
	 * Check un used sub tree ref.
	 *
	 * @param xmlProcessor the xml processor
	 * @param usedSubTreeRefIds the used sub tree ref ids
	 * @return the list
	 */
	private  List<String> checkUnUsedSubTreeRef(XmlProcessor xmlProcessor, List<String> usedSubTreeRefIds){
 		
 		List<String> allSubTreeRefIds = new ArrayList<String>();
 		NodeList subTreeRefIdsNodeList;
 		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
 		try {
 			subTreeRefIdsNodeList = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), 
 					"/measure//subTreeRef/@id");
 			
 			for (int i = 0; i < subTreeRefIdsNodeList.getLength(); i++) {
 				Node SubTreeRefIdAttributeNode = subTreeRefIdsNodeList.item(i);
 				if (!allSubTreeRefIds.contains(SubTreeRefIdAttributeNode
 						.getNodeValue())) {
 					allSubTreeRefIds.add(SubTreeRefIdAttributeNode.getNodeValue());
 				}
 			}
 			allSubTreeRefIds.removeAll(usedSubTreeRefIds);
 			
 			for (int i = 0; i < usedSubTreeRefIds.size(); i++) {
 				for (int j = 0; j < allSubTreeRefIds.size(); j++) {
 					Node usedSubTreeRefNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), "/measure/subTreeLookUp/subTree[@uuid='"
 									+ usedSubTreeRefIds.get(i)
 									+ "']//subTreeRef[@id='"
 									+ allSubTreeRefIds.get(j) + "']");
 					if (usedSubTreeRefNode != null) {
 						
 						String subTreeUUID = usedSubTreeRefNode.getAttributes().getNamedItem("id").getNodeValue();
 						String XPATH_IS_INSTANCE_OF = "//subTree [boolean(@instanceOf)]/@uuid ='"
 								+ subTreeUUID +"'";
 						boolean isOccurrenceNode = (Boolean) xPath.evaluate(XPATH_IS_INSTANCE_OF, xmlProcessor.getOriginalDoc(), XPathConstants.BOOLEAN);
 						if(isOccurrenceNode) {
 							String XPATH_PARENT_UUID = "//subTree [@uuid ='"+subTreeUUID +"']/@instanceOf";
 							String parentUUID = (String) xPath.evaluate(XPATH_PARENT_UUID, xmlProcessor.getOriginalDoc(), XPathConstants.STRING);
 							if (!usedSubTreeRefIds.contains(parentUUID)) {
 								usedSubTreeRefIds.add(parentUUID);
 							}
 							
 						}
 						if (!usedSubTreeRefIds.contains(allSubTreeRefIds.get(j))) {
 							
 							
 							usedSubTreeRefIds.add(allSubTreeRefIds.get(j));
 						}
 					}
 				}
 				
 			}
 		} catch (XPathExpressionException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
 		return usedSubTreeRefIds;
 	}
     
     /**
      * Validate sub tree ref in pop.
      *
      * @param subTreeNode the sub tree node
      * @param functionalOpNode the functional op node
      * @return true, if successful
      */
     public boolean validateSubTreeRefInPOP(Node subTreeNode, Node functionalOpNode){
 		if(subTreeNodeInPOPMap.get(subTreeNode.getAttributes()
 				.getNamedItem("uuid").getNodeValue())!=null){
 			String firstChildName = functionalOpNode.getFirstChild().getNodeName();
 			String functionalOpType = functionalOpNode.getAttributes().getNamedItem("type").getNodeValue();
 			List<String> childsList = FUNCTIONAL_OP_RULES_IN_POP.get(functionalOpType);
 			if(childsList.contains(firstChildName)){
 				return true;
 			} 
 		}
 		return false;
 	}
     
     
     /**
      * Check if qdm var instance is present.
      *
      * @param usedSubtreeRefId the used subtree ref id
      * @param xmlProcessor the xml processor
      * @return the string
      */
     private String checkIfQDMVarInstanceIsPresent(String usedSubtreeRefId,
 			XmlProcessor xmlProcessor){
 		
 		String XPATH_INSTANCE_QDM_VAR = "/measure/subTreeLookUp/subTree[@uuid='"+usedSubtreeRefId+"']/@instance";
 		String XPATH_INSTANCE_OF_QDM_VAR = "/measure/subTreeLookUp/subTree[@uuid='"+usedSubtreeRefId+"']/@instanceOf";
 		try {
 			Node nodesSDE_SubTree = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), XPATH_INSTANCE_QDM_VAR);
 			if(nodesSDE_SubTree!=null){
 				Node nodesSDE_SubTreeInstance = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), XPATH_INSTANCE_OF_QDM_VAR);
 				usedSubtreeRefId = nodesSDE_SubTreeInstance.getNodeValue();
 			}
 			
 		} catch (XPathExpressionException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
 		
 		
 		return usedSubtreeRefId;
 	}
}
