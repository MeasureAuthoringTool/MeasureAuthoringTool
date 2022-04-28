package mat.server.hqmf.qdm_5_6;

import mat.model.clause.MeasureExport;
import mat.server.hqmf.Generator;
import mat.server.hqmf.qdm.HQMFAttributeGenerator;
import org.slf4j.LoggerFactory;
import mat.server.util.XmlProcessor;
import mat.shared.MatConstants;
import mat.shared.UUIDUtilClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HQMFClauseLogicGenerator implements Generator {
	private static final String GROUPER_CRITERIA = "grouperCriteria";
	private static final String CONJUNCTION_CODE = "conjunctionCode";
	private static final String LOCAL_VARIABLE_NAME = "localVariableName";
	private static final String CRITERIA_REFERENCE = "criteriaReference";

	private static final String ROLE = "role";

	private static final String PARTICIPATION = "participation";

	private static final String EXCERPT = "excerpt";

	private static final String SEQUENCE_NUMBER = "sequenceNumber";

	private static final String GROUPER = "grouper";

	private static final String ENTRY = "entry";

	private static final String DATA_CRITERIA_SECTION = "dataCriteriaSection";

	private Map<String, Node> subTreeNodeMap = new HashMap<>();

	public MeasureExport measureExport;

	private static final Logger logger = LoggerFactory.getLogger(HQMFClauseLogicGenerator.class);

	/**
	 * MAP of Functional Ops NON Subset Type.
	 */
	private static final Map<String, String> FUNCTIONAL_OPS_NON_SUBSET = new TreeMap<>(
			String.CASE_INSENSITIVE_ORDER);
	/**
	 * MAP of Functional Ops Subset Type.
	 */
	private static final Map<String, String> FUNCTIONAL_OPS_SUBSET = new TreeMap<>(
			String.CASE_INSENSITIVE_ORDER);
	static {
		FUNCTIONAL_OPS_NON_SUBSET.put(MatConstants.FIRST, "1");
		FUNCTIONAL_OPS_NON_SUBSET.put(MatConstants.SECOND, "2");
		FUNCTIONAL_OPS_NON_SUBSET.put(MatConstants.THIRD, "3");
		FUNCTIONAL_OPS_NON_SUBSET.put(MatConstants.FOURTH, "4");
		FUNCTIONAL_OPS_NON_SUBSET.put(MatConstants.FIFTH, "5");

		FUNCTIONAL_OPS_SUBSET.put(MatConstants.MOST_RECENT, "QDM_LAST");
		FUNCTIONAL_OPS_SUBSET.put(MatConstants.COUNT, "QDM_SUM");
		FUNCTIONAL_OPS_SUBSET.put(MatConstants.MIN, "QDM_MIN");
		FUNCTIONAL_OPS_SUBSET.put(MatConstants.MAX, "QDM_MAX");
		FUNCTIONAL_OPS_SUBSET.put(MatConstants.SUM, "QDM_SUM");
		FUNCTIONAL_OPS_SUBSET.put(MatConstants.MEDIAN, "QDM_MEDIAN");
		FUNCTIONAL_OPS_SUBSET.put(MatConstants.AVG, "QDM_AVERAGE");

	}

	/** The Constant populations. */
	private static final List<String> POPULATION_NAME_LIST = new ArrayList<>();

	static {
		POPULATION_NAME_LIST.add("initialPopulation");
		POPULATION_NAME_LIST.add("denominator");
		POPULATION_NAME_LIST.add("denominatorExclusions");
		POPULATION_NAME_LIST.add("denominatorExceptions");
		POPULATION_NAME_LIST.add("numerator");
		POPULATION_NAME_LIST.add("numeratorExclusions");
		POPULATION_NAME_LIST.add("measurePopulation");
		POPULATION_NAME_LIST.add("measurePopulationExclusions");
		POPULATION_NAME_LIST.add("stratum");
	}

	/** The sub tree node in mo map. */
	Map<String, Node> subTreeNodeInMOMap = new HashMap<>();

	/** The sub tree node in pop map. */
	Map<String, Node> subTreeNodeInPOPMap = new HashMap<>();

	/** The sub tree node in RA map. */
	Map<String, Node> subTreeNodeInRAMap = new HashMap<>();

	/** The Constant FUNCTIONAL_OP_RULES_IN_POP. */
	private static final Map<String, List<String>> FUNCTIONAL_OP_RULES_IN_POP = new TreeMap<>(
			String.CASE_INSENSITIVE_ORDER);

	/** The Constant FUNCTIONAL_OP_RULES_IN_MO. */
	private static final Map<String, List<String>> FUNCTIONAL_OP_RULES_IN_MO = new TreeMap<>(
			String.CASE_INSENSITIVE_ORDER);

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

		/* Rules for Functions in Measure Observations */
		FUNCTIONAL_OP_RULES_IN_MO.put("MEDIAN", getFunctionalOpFirstChildInMO("MEDIAN"));
		FUNCTIONAL_OP_RULES_IN_MO.put("AVERAGE", getFunctionalOpFirstChildInMO("AVERAGE"));
		FUNCTIONAL_OP_RULES_IN_MO.put("MIN", getFunctionalOpFirstChildInMO("MIN"));
		FUNCTIONAL_OP_RULES_IN_MO.put("SUM", getFunctionalOpFirstChildInMO("SUM"));
		FUNCTIONAL_OP_RULES_IN_MO.put("COUNT", getFunctionalOpFirstChildInMO("COUNT"));
		FUNCTIONAL_OP_RULES_IN_MO.put("DATETIMEDIFF", getFunctionalOpFirstChildInMO("DATETIMEDIFF"));
	}

	@Override
	public String generate(MeasureExport me) throws Exception {
		measureExport = me;
		createUsedSubTreeRefMap();
		generateSubTreeXML();
		return null;
	}

	/**
	 * Generate sub tree xml.
	 *
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private void generateSubTreeXML() throws XPathExpressionException {
		String xpath = "/measure/subTreeLookUp/subTree[not(@instance)]";
		NodeList subTreeNodeList = measureExport.getSimpleXMLProcessor()
				.findNodeList(measureExport.getSimpleXMLProcessor().getOriginalDoc(), xpath);
		for (int i = 0; i < subTreeNodeList.getLength(); i++) {
			Node subTreeNode = subTreeNodeList.item(i);
			subTreeNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
			String uuid = subTreeNode.getAttributes().getNamedItem(UUID).getNodeValue();
			if (subTreeNodeInPOPMap.containsKey(uuid) && subTreeNodeInMOMap.containsKey(uuid)
					|| subTreeNodeInPOPMap.containsKey(uuid) || subTreeNodeInRAMap.containsKey(uuid)) {
				generateSubTreeXML(subTreeNode, false);
			}
		}
		String xpathOccurrence = "/measure/subTreeLookUp/subTree[(@instance)]";
		NodeList occurrenceSubTreeNodeList = measureExport.getSimpleXMLProcessor()
				.findNodeList(measureExport.getSimpleXMLProcessor().getOriginalDoc(), xpathOccurrence);
		for (int i = 0; i < occurrenceSubTreeNodeList.getLength(); i++) {
			Node subTreeNode = occurrenceSubTreeNodeList.item(i);
			generateOccHQMF(subTreeNode);
		}
	}

	/**
	 * Generate sub tree xml.
	 *
	 * @param subTreeNode
	 *            the sub tree node
	 * @param msrObsDateTimeDiffSubTree
	 *            the msr obs date time diff sub tree
	 * @return the node
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	protected Node generateSubTreeXML(Node subTreeNode, boolean msrObsDateTimeDiffSubTree)
			throws XPathExpressionException {

		// If this is an empty or NULL clause, return right now.
		if (subTreeNode == null || !subTreeNode.hasChildNodes()) {
			return null;
		}

		/*
		  If this is a Occurrence clause then we need to find the base clause and
		  generate HQMF for the base clause. Then we need to generate Occurrence HQMF
		  for the occurrence clause.
		 */
		if (subTreeNode.getAttributes().getNamedItem(INSTANCE_OF) != null) {
			String baseClauseUUID = subTreeNode.getAttributes().getNamedItem(INSTANCE_OF).getNodeValue();
			String xpath = "/measure/subTreeLookUp/subTree[@uuid = '" + baseClauseUUID + "']";
			Node baseSubTreeNode = measureExport.getSimpleXMLProcessor()
					.findNode(measureExport.getSimpleXMLProcessor().getOriginalDoc(), xpath);
			generateSubTreeXML(baseSubTreeNode, false);
			generateOccHQMF(subTreeNode);
		}

		String subTreeUUID = subTreeNode.getAttributes().getNamedItem(UUID).getNodeValue();
		String clauseName = subTreeNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
		Boolean isRav = isRiskAdjustmentVariable(subTreeUUID, clauseName);

		// Check the 'subTreeNodeMap' to make sure the clause isn't already generated.
		if (subTreeNodeMap.containsKey(subTreeUUID) && !msrObsDateTimeDiffSubTree) {
			logger.debug("HQMF for Clause " + clauseName + " is already generated. Skipping.");
			return null;
		}

		// get the first child of the subTreeNode
		Node firstChild = subTreeNode.getFirstChild();
		String firstChildName = firstChild.getNodeName();
		logger.debug("Generating HQMF for clause:'" + clauseName + "' with first child named:'" + firstChildName + "'.");

		XmlProcessor hqmfXmlProcessor = measureExport.getHQMFXmlProcessor();
		Element dataCriteriaSectionElem = (Element) hqmfXmlProcessor.getOriginalDoc()
				.getElementsByTagName(DATA_CRITERIA_SECTION).item(0);

		if (isRav) {
			Comment riskAdjustmentVariable = hqmfXmlProcessor.getOriginalDoc().createComment("Risk Adjustment Variable");
			dataCriteriaSectionElem.appendChild(riskAdjustmentVariable);
		}
		Node entryElement = null;
		switch (firstChildName) {
		case SET_OP:
			entryElement = generateSetOpHQMF(firstChild, dataCriteriaSectionElem, clauseName);
			break;
		case ELEMENT_REF:
			entryElement = generateElementRefHQMF(firstChild, dataCriteriaSectionElem, clauseName);
			break;
		case SUB_TREE_REF:
			entryElement = generateSubTreeHQMF(firstChild, dataCriteriaSectionElem, clauseName);
			break;
		case RELATIONAL_OP:
			entryElement = generateRelOpHQMF(firstChild, dataCriteriaSectionElem, clauseName);
			break;
		case FUNCTIONAL_OP:
			entryElement = generateFunctionalOpHQMF(firstChild, dataCriteriaSectionElem, clauseName);
			break;
		default:
			// Dont do anything
			break;
		}
		if (isRav) {
			Node temp = hqmfXmlProcessor.getOriginalDoc().createAttribute(RAV);
			temp.setNodeValue("true");
			entryElement.getAttributes().setNamedItem(temp);
		}
		/*
		  The clause is generated now. Make an entry in the 'subTreeNodeMap' to keep
		  track of its generation.
		 */
		subTreeNodeMap.put(subTreeUUID, subTreeNode);
		return entryElement;

	}

	/**
	 * This method is used to discover weither a given class name and UUID is a risk
	 * adjustment variable.
	 *
	 * @param subTreeUUID
	 *            the sub tree uuid
	 * @param clauseName
	 *            the clause name
	 * @return the boolean
	 */
	private Boolean isRiskAdjustmentVariable(String subTreeUUID, String clauseName) {
		String xPath = "/measure/riskAdjustmentVariables/subTreeRef[@displayName=\"" + clauseName + "\" and @id='"
				+ subTreeUUID + "']";
		boolean isRiskAdjustmentVariable = false;
		try {
			Node riskAdjVarNode = measureExport.getSimpleXMLProcessor()
					.findNode(measureExport.getSimpleXMLProcessor().getOriginalDoc(), xPath);
			if (riskAdjVarNode != null) {
				isRiskAdjustmentVariable = true;
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return isRiskAdjustmentVariable;
	}

	/**
	 * Generate occ hqmf.
	 *
	 * @param subTreeNode
	 *            the sub tree node
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private void generateOccHQMF(Node subTreeNode) throws XPathExpressionException {
		// If this is an empty or NULL clause, return right now.
		if (subTreeNode == null || subTreeNode.getAttributes().getNamedItem(INSTANCE_OF) == null) {
			return;
		}
		XmlProcessor hqmfXmlProcessor = measureExport.getHQMFXmlProcessor();
		String occSubTreeUUID = subTreeNode.getAttributes().getNamedItem(UUID).getNodeValue();
		String qdmVariableSubTreeUUID = subTreeNode.getAttributes().getNamedItem(INSTANCE_OF).getNodeValue();
		String clauseName = subTreeNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
		Boolean isRAV = isRiskAdjustmentVariable(qdmVariableSubTreeUUID, clauseName);
		// Check the 'subTreeNodeMap' to make sure the occ clause isn't already generated.
		if (subTreeNodeMap.containsKey(occSubTreeUUID)) {
			logger.debug("HQMF for Occ Clause " + clauseName + " is already generated. Skipping.");
			return;
		}

		if (!subTreeNodeMap.containsKey(qdmVariableSubTreeUUID)) {
			logger.debug("HQMF for Clause " + clauseName + " is not already generated. Skipping.");
			return;
		}

		String xpath = "/measure/subTreeLookUp/subTree[@uuid = '" + qdmVariableSubTreeUUID + "']";
		Node baseSubTreeNode = measureExport.getSimpleXMLProcessor()
				.findNode(measureExport.getSimpleXMLProcessor().getOriginalDoc(), xpath);
		Node baseFirstChild = baseSubTreeNode.getFirstChild();
		String baseExt = baseFirstChild.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();

		Node firstChild = subTreeNode.getFirstChild();
		String firstChildName = firstChild.getNodeName();
		String ext = firstChild.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
		// Local Variable Name.
		String localVarName = clauseName.replace("$", "") + "_" + UUIDUtilClient.uuid(5);
		String root = subTreeNode.getAttributes().getNamedItem(INSTANCE_OF).getNodeValue();
		// Check for Element Ref as first CHild.
		if (firstChildName.equalsIgnoreCase(ELEMENT_REF)) {
			ext = getElementRefExt(firstChild, measureExport.getSimpleXMLProcessor());
			baseExt = getElementRefExt(baseFirstChild, measureExport.getSimpleXMLProcessor());
		} else if (RELATIONAL_OP.equals(firstChildName) || FUNCTIONAL_OP.equals(firstChildName)
				|| SET_OP.equals(firstChildName)) {
			ext += "_" + firstChild.getAttributes().getNamedItem(UUID).getNodeValue();
			baseExt += "_" + baseFirstChild.getAttributes().getNamedItem(UUID).getNodeValue();
		}

		if (FUNCTIONAL_OP.equals(firstChildName) && firstChild.getFirstChild() != null) {
			Node functionChild = firstChild.getFirstChild();
			Node baseFunctionChild = baseFirstChild.getFirstChild();

			if (functionChild != null) {
				if (functionChild.getNodeName().equalsIgnoreCase(SUB_TREE_REF)) {
					ext = functionChild.getAttributes().getNamedItem(ID).getNodeValue();
					baseExt = baseFunctionChild.getAttributes().getNamedItem(ID).getNodeValue();
				} else if (functionChild.getNodeName().equalsIgnoreCase(ELEMENT_REF)) {
					ext = getElementRefExt(functionChild, measureExport.getSimpleXMLProcessor());
					baseExt = getElementRefExt(baseFunctionChild, measureExport.getSimpleXMLProcessor());
				} else {
					ext = StringUtils.deleteWhitespace(
							functionChild.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue() + "_"
									+ functionChild.getAttributes().getNamedItem(UUID).getNodeValue())
							.replaceAll(":", "_");
					baseExt = StringUtils.deleteWhitespace(
							baseFunctionChild.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue() + "_"
									+ baseFunctionChild.getAttributes().getNamedItem(UUID).getNodeValue())
							.replaceAll(":", "_");
				}
			}
		}

		String isQdmVariable = subTreeNode.getAttributes().getNamedItem(QDM_VARIABLE).getNodeValue();
		if (isQdmVariable.equalsIgnoreCase(TRUE)) {
			ext = "qdm_var_" + StringUtils.deleteWhitespace(ext);
			baseExt = "qdm_var_" + StringUtils.deleteWhitespace(baseExt);
			localVarName = "qdm_var_" + StringUtils.deleteWhitespace(localVarName);
		}
		String extForOccurrenceNode = "occ" + subTreeNode.getAttributes().getNamedItem("instance").getNodeValue()
				+ "of_" + ext;
		ext = StringUtils.deleteWhitespace(ext);
		localVarName = StringUtils.deleteWhitespace(localVarName);
		logger.debug("generateOccHQMF " + "//entry/*/id[@root='" + root + "'][@extension=\"" + baseExt + "\"]");

		Node idNodeQDM = hqmfXmlProcessor.findNode(hqmfXmlProcessor.getOriginalDoc(),
				"//entry/*/id[@root='" + root + "'][@extension=\"" + baseExt + "\"]");
		logger.debug("idNodeQDM == null?" + (idNodeQDM == null));

		if (idNodeQDM != null) {
			// Add code here which will create a replica of the entry elem of 'idNodeQDM'
			// and assign it an extension
			// which has "Occ_X" string in it.
			Node cloneMainEntryNode = idNodeQDM.getParentNode().getParentNode().cloneNode(true);
			Node cloneIDNode = findNode(cloneMainEntryNode, "ID");
			if (cloneMainEntryNode != null) {
				Node localVariableNode = cloneMainEntryNode.getFirstChild();
				if (localVariableNode.getAttributes().getNamedItem("value") != null) {
					localVariableNode.getAttributes().getNamedItem("value").setNodeValue(localVarName);
				}
			}
			cloneIDNode.getAttributes().getNamedItem(EXTENSION).setNodeValue(extForOccurrenceNode);

			Element dataCriteriaSectionElem = (Element) hqmfXmlProcessor.getOriginalDoc()
					.getElementsByTagName(DATA_CRITERIA_SECTION).item(0);
			
			dataCriteriaSectionElem.appendChild(cloneMainEntryNode);

			Node parentNode = cloneIDNode.getParentNode().cloneNode(false);
			if (isRAV) {
				Comment RAComment = hqmfXmlProcessor.getOriginalDoc().createComment("Risk Adjustment Variable");
				dataCriteriaSectionElem.appendChild(RAComment);
			}
			Element entryElem = hqmfXmlProcessor.getOriginalDoc().createElement(ENTRY);
			entryElem.setAttribute(TYPE_CODE, "DRIV");
			Element idElement = hqmfXmlProcessor.getOriginalDoc().createElement(ID);
			idElement.setAttribute(ROOT, subTreeNode.getAttributes().getNamedItem(UUID).getNodeValue());
			idElement.setAttribute(EXTENSION, extForOccurrenceNode);
			parentNode.appendChild(idElement);
			Element outboundRelElem = hqmfXmlProcessor.getOriginalDoc().createElement(OUTBOUND_RELATIONSHIP);
			outboundRelElem.setAttribute(TYPE_CODE, "OCCR");

			Element criteriaRefElem = hqmfXmlProcessor.getOriginalDoc().createElement(CRITERIA_REFERENCE);
			String refClassCodeValue = parentNode.getAttributes().getNamedItem(CLASS_CODE).getNodeValue();
			String refMoodValue = parentNode.getAttributes().getNamedItem(MOOD_CODE).getNodeValue();
			criteriaRefElem.setAttribute(CLASS_CODE, refClassCodeValue);
			criteriaRefElem.setAttribute(MOOD_CODE, refMoodValue);

			Element idRelElem = hqmfXmlProcessor.getOriginalDoc().createElement(ID);
			idRelElem.setAttribute(ROOT, root);
			idRelElem.setAttribute(EXTENSION, extForOccurrenceNode);

			criteriaRefElem.appendChild(idRelElem);
			outboundRelElem.appendChild(criteriaRefElem);
			parentNode.appendChild(outboundRelElem);
			entryElem.appendChild(parentNode);
			dataCriteriaSectionElem.appendChild(entryElem);

			subTreeNodeMap.put(occSubTreeUUID, subTreeNode);
		}
	}

	/**
	 * Generate functional op hqmf.
	 *
	 * @param functionalNode
	 *            the functional node
	 * @param dataCriteriaSectionElem
	 *            the data criteria section elem
	 * @param clauseName
	 *            the clause name
	 * @return the node
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private Node generateFunctionalOpHQMF(Node functionalNode, Element dataCriteriaSectionElem, String clauseName)
			throws XPathExpressionException {
		Node node = null;
		if (functionalNode.getChildNodes() != null) {
			Node firstChildNode = functionalNode.getFirstChild();
			String firstChildName = firstChildNode.getNodeName();
			switch (firstChildName) {
			case SET_OP:
				String functionOpType = functionalNode.getAttributes().getNamedItem(TYPE).getNodeValue();
				if (FUNCTIONAL_OPS_NON_SUBSET.containsKey(functionOpType.toUpperCase())
						|| FUNCTIONAL_OPS_SUBSET.containsKey(functionOpType.toUpperCase())) {
					node = generateSetOpHQMF(firstChildNode, dataCriteriaSectionElem, clauseName);
				}
				break;
			case ELEMENT_REF:
				node = generateElementRefHQMF(firstChildNode, dataCriteriaSectionElem, clauseName);
				break;
			case RELATIONAL_OP:
				node = generateRelOpHQMF(firstChildNode, dataCriteriaSectionElem, clauseName);
				break;
			case FUNCTIONAL_OP:
				// findFunctionalOpChild(firstChildNode, dataCriteriaSectionElem);
				break;
			case SUB_TREE_REF:
				node = generateSubTreeHQMFInFunctionalOp(firstChildNode, dataCriteriaSectionElem, clauseName);
				break;
			default:
				// Dont do anything
				break;
			}

			String localVarName = clauseName;

			localVarName = localVarName.replace("$", "");
			Node parentNode = functionalNode.getParentNode();
			if (parentNode != null && parentNode.getNodeName().equalsIgnoreCase("subTree")) {
				if (parentNode.getAttributes().getNamedItem(QDM_VARIABLE) != null) {
					String isQdmVariable = parentNode.getAttributes().getNamedItem(QDM_VARIABLE).getNodeValue();
					if (TRUE.equalsIgnoreCase(isQdmVariable)) {
						localVarName = localVarName.replace("$", "");
						localVarName = "qdm_var_" + localVarName;
					}
				}
				localVarName = localVarName + "_" + UUIDUtilClient.uuid(5);
				localVarName = StringUtils.deleteWhitespace(localVarName);
				updateLocalVar(node, localVarName);
			}
		}
		return node;

	}

	/**
	 * Method to generate HQMF for function Ops with first child as subTreeRef. In
	 * this case grouperCriteria will be generated for SubTreeRef with Excerpt entry
	 * inside it for functional Op.
	 *
	 * @param firstChildNode
	 *            - SubTreeRef Node.
	 * @param dataCriteriaSectionElem
	 *            - Data Criteria Element.
	 * @param clauseName
	 *            the clause name
	 * @return the node
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private Node generateSubTreeHQMFInFunctionalOp(Node firstChildNode, Element dataCriteriaSectionElem,
			String clauseName) throws XPathExpressionException {
		Node parentNode = firstChildNode.getParentNode();

		// temp node.
		String subTreeUUID = firstChildNode.getAttributes().getNamedItem(ID).getNodeValue();
		String xpath = "/measure/subTreeLookUp/subTree[@uuid='" + subTreeUUID + "']";
		Node subTreeNode = measureExport.getSimpleXMLProcessor()
				.findNode(measureExport.getSimpleXMLProcessor().getOriginalDoc(), xpath);
		String firstChildNameOfSubTree = subTreeNode.getFirstChild().getNodeName();
		if (FUNCTIONAL_OP.equals(firstChildNameOfSubTree)) {
			String firstChildNodeName = parentNode.getAttributes().getNamedItem(TYPE).getNodeValue();
			if (!SATISFIES_ALL.equalsIgnoreCase(firstChildNodeName)
					|| !SATISFIES_ANY.equalsIgnoreCase(firstChildNodeName) || !AGE_AT.equals(firstChildNodeName)) {
				return null;
			}
		}
		Element root = measureExport.getHQMFXmlProcessor().getOriginalDoc().createElement("temp");
		generateSubTreeHQMF(firstChildNode, root, clauseName);
		Element entryElement = (Element) root.getFirstChild();
		Node firstChild = entryElement.getFirstChild();
		if ("localVariableName".equals(firstChild.getNodeName())) {
			firstChild = firstChild.getNextSibling();
		}
		Element excerpt = generateExcerptEntryForFunctionalNode(parentNode, null, measureExport.getHQMFXmlProcessor(),
				entryElement);
		if (excerpt != null) {
			firstChild.appendChild(excerpt);
		}
		dataCriteriaSectionElem.appendChild(entryElement);
		return entryElement;
	}

	/**
	 * This will take care of the use case where a user can create a Clause with
	 * only one QDM elementRef inside it. We will make a copy of the original entry
	 * for QDM and update the id@root and id@extension for it. This will server as
	 * an entry for the Clause.
	 *
	 * @param elementRefNode
	 *            the element ref node
	 * @param parentNode
	 *            the parent node
	 * @param clauseName
	 *            the clause name
	 * @return the node
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private Node generateElementRefHQMF(Node elementRefNode, Node parentNode, String clauseName)
			throws XPathExpressionException {
		XmlProcessor hqmfXmlProcessor = measureExport.getHQMFXmlProcessor();
		Node node = null;
		String ext = getElementRefExt(elementRefNode, measureExport.getSimpleXMLProcessor());
		String root = elementRefNode.getAttributes().getNamedItem(ID).getNodeValue();
		String localVariableName = clauseName;
		Node idNodeQDM = hqmfXmlProcessor.findNode(hqmfXmlProcessor.getOriginalDoc(),
				"//entry/*/id[@root='" + root + "'][@extension=\"" + ext + "\"]");
		if (idNodeQDM != null) {
			Node entryElem = idNodeQDM.getParentNode().getParentNode().cloneNode(true);
			Node newIdNode = getTagFromEntry(entryElem, ID);

			if (newIdNode == null) {
				return null;
			}

			String idroot = "0";
			Node parNode = elementRefNode.getParentNode();
			if (parNode != null && SUB_TREE.equals(parNode.getNodeName())) {
				idroot = parNode.getAttributes().getNamedItem(UUID).getNodeValue();
				// Added logic to show qdm_variable in extension if clause is of qdm variable
				// type.
				String isQdmVariable = parNode.getAttributes().getNamedItem(QDM_VARIABLE).getNodeValue();
				if (isQdmVariable.equalsIgnoreCase(TRUE)) {
					String occText = null;
					// Handled Occurrence Of QDM Variable.
					if (parNode.getAttributes().getNamedItem(INSTANCE_OF) != null) {
						occText = "occ" + parNode.getAttributes().getNamedItem(INSTANCE).getNodeValue() + "of_";
					}
					if (occText != null) {
						ext = occText + "qdm_var_" + ext;
					} else {
						ext = "qdm_var_" + ext;
						localVariableName = "qdm_var_" + localVariableName;
					}
				}
				localVariableName = localVariableName + "_" + UUIDUtilClient.uuid(5);
				localVariableName = StringUtils.deleteWhitespace(localVariableName);
				((Element) newIdNode).setAttribute(ROOT, idroot);
				((Element) newIdNode).setAttribute(EXTENSION, ext);
				Node localVariableNode = entryElem.getFirstChild();
				if (localVariableNode.getAttributes().getNamedItem("value") != null) {
					localVariableNode.getAttributes().getNamedItem("value").setNodeValue(localVariableName);
				}

				parentNode.appendChild(entryElem);
				node = entryElem;
			} else {
				// if the the parentNode for ElementRef is other than SubTreeNode
				Element excerptElement = null;
				Node subTreeParentNode = checkIfSubTree(parNode);
				if (subTreeParentNode != null) {
					root = subTreeParentNode.getAttributes().getNamedItem(UUID).getNodeValue();
					if (subTreeParentNode.getAttributes().getNamedItem(QDM_VARIABLE) != null) {
						String isQdmVariable = subTreeParentNode.getAttributes().getNamedItem(QDM_VARIABLE)
								.getNodeValue();
						if (TRUE.equalsIgnoreCase(isQdmVariable)) {
							String occText = null;
							// Handled Occurrence Of QDM Variable.
							if (subTreeParentNode.getAttributes().getNamedItem(INSTANCE_OF) != null) {
								occText = "occ"
										+ subTreeParentNode.getAttributes().getNamedItem("instance").getNodeValue()
										+ "of_";
							}
							if (occText != null) {
								ext = occText + "qdm_var_" + ext;
							} else {
								ext = "qdm_var_" + ext;
							}
						}
					}
				} else {
					root = UUIDUtilClient.uuid();
				}

				Node entryNodeForElementRef = idNodeQDM.getParentNode().getParentNode();
				Node clonedEntryNodeForElementRef = entryNodeForElementRef.cloneNode(true);
				NodeList idChildNodeList = ((Element) clonedEntryNodeForElementRef).getElementsByTagName(ID);
				if (idChildNodeList != null && idChildNodeList.getLength() > 0) {
					Node idChildNode = idChildNodeList.item(0);
					idChildNode.getAttributes().getNamedItem(EXTENSION).setNodeValue(ext);
					idChildNode.getAttributes().getNamedItem(ROOT).setNodeValue(root);
				}

				Node firstChild = clonedEntryNodeForElementRef.getFirstChild();
				if (LOCAL_VARIABLE_NAME.equals(firstChild.getNodeName())) {
					firstChild = firstChild.getNextSibling();
				}
				// Added logic to show qdm_variable in extension if clause is of qdm variable
				// type.
				if (FUNCTIONAL_OP.equals(parNode.getNodeName())) {
					excerptElement = generateExcerptEntryForFunctionalNode(parNode, elementRefNode, hqmfXmlProcessor,
							clonedEntryNodeForElementRef);
				}

				if (excerptElement != null) {
				
					firstChild.appendChild(excerptElement);
				}
				parentNode.appendChild(clonedEntryNodeForElementRef);
				node = clonedEntryNodeForElementRef;

			}
			updateLocalVar(node, ext);
		}

		return node;
	}

	/**
	 * This will take care of the use case where a user can create a Clause with
	 * only one child Clause inside it. If HQMF for the child clause is already
	 * generated, then since we have no way of referencing this child clause using
	 * <outBoundRelationShip> directly, we are adding it to a default UNION grouper.
	 * 
	 * If it isnt generated then we generate it and then add a criteriaRef to it
	 * inside a default UNION.
	 *
	 * @param subTreeRefNode
	 *            the sub tree ref node
	 * @param parentNode
	 *            the parent node
	 * @param clauseName
	 *            the clause name
	 * @return the node
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private Node generateSubTreeHQMF(Node subTreeRefNode, Node parentNode, String clauseName)
			throws XPathExpressionException {

		String subTreeUUID = subTreeRefNode.getAttributes().getNamedItem(ID).getNodeValue();

		/*
		 * Check if the Clause has already been generated. If it is not generated yet,
		 * then generate it by calling the 'generateSubTreeXML' method.
		 */
		if (!subTreeNodeMap.containsKey(subTreeUUID)) {
			String xpath = "/measure/subTreeLookUp/subTree[@uuid='" + subTreeUUID + "']";
			Node subTreeNode = measureExport.getSimpleXMLProcessor()
					.findNode(measureExport.getSimpleXMLProcessor().getOriginalDoc(), xpath);
			generateSubTreeXML(subTreeNode, false);
		}

		XmlProcessor hqmfXmlProcessor = measureExport.getHQMFXmlProcessor();

		// creating Entry Tag
		Element entryElem = hqmfXmlProcessor.getOriginalDoc().createElement(ENTRY);
		entryElem.setAttribute(TYPE_CODE, "DRIV");
		String root = UUIDUtilClient.uuid();
		String ext = subTreeRefNode.getAttributes().getNamedItem(ID).getNodeValue();
		String localVarName = clauseName;
		localVarName = localVarName.replace("$", "");
		Node parNode = checkIfSubTree(subTreeRefNode.getParentNode());
		if (parNode != null) {
			root = parNode.getAttributes().getNamedItem(UUID).getNodeValue();
			if (parNode.getAttributes().getNamedItem(QDM_VARIABLE) != null) {
				String isQdmVariable = parNode.getAttributes().getNamedItem(QDM_VARIABLE).getNodeValue();
				if (TRUE.equalsIgnoreCase(isQdmVariable)) {
					String occText = null;
					// Handled Occurrence Of QDM Variable.
					if (parNode.getAttributes().getNamedItem(INSTANCE_OF) != null) {
						occText = "occ" + parNode.getAttributes().getNamedItem(INSTANCE).getNodeValue() + "of_";
					}
					if (occText != null) {
						ext = occText + "qdm_var_" + ext;
					} else {
						ext = "qdm_var_" + ext;
						localVarName = "qdm_var_" + localVarName;
					}
				}
			}
		}

		Node grouperElem = generateEmptyGrouper(hqmfXmlProcessor, root, ext);

		// generate comment
		Comment comment = hqmfXmlProcessor.getOriginalDoc().createComment(
				"outBoundRelationship for " + subTreeRefNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue());
		grouperElem.appendChild(comment);

		// generate outboundRelationship
		Element outboundRelElem = generateEmptyOutboundElem(hqmfXmlProcessor);
		Element conjunctionCodeElem = hqmfXmlProcessor.getOriginalDoc().createElement(CONJUNCTION_CODE);
		conjunctionCodeElem.setAttribute(CODE, "OR");

		outboundRelElem.appendChild(conjunctionCodeElem);
		generateCritRefForNode(outboundRelElem, subTreeRefNode);

		grouperElem.appendChild(outboundRelElem);

		Element localVarElem = hqmfXmlProcessor.getOriginalDoc().createElement(LOCAL_VARIABLE_NAME);
		localVarName = localVarName + "_" + UUIDUtilClient.uuid(5);
		localVarName = StringUtils.deleteWhitespace(localVarName);
		localVarElem.setAttribute(VALUE, localVarName);
		entryElem.appendChild(localVarElem);

		entryElem.appendChild(grouperElem);
		parentNode.appendChild(entryElem);
		return entryElem;

	}

	/**
	 * This method wil generate HQMF code for setOp (UNION,INTERSECTION).
	 *
	 * @param setOpNode
	 *            the set op node
	 * @param parentNode
	 *            the parent node
	 * @param clauseName
	 *            the clause name
	 * @return the node
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private Node generateSetOpHQMF(Node setOpNode, Node parentNode, String clauseName) throws XPathExpressionException {

		XmlProcessor hqmfXmlProcessor = measureExport.getHQMFXmlProcessor();
		// DISPLAY NAME is used instead of type as it is in Title case.
		String setOpType = setOpNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
		String conjunctionType = "OR";

		if ("union".equalsIgnoreCase(setOpType) || SATISFIES_ANY.equalsIgnoreCase(setOpType)) {
			conjunctionType = "OR";
		} else if ("intersection".equalsIgnoreCase(setOpType) || SATISFIES_ALL.equalsIgnoreCase(setOpType)) {
			conjunctionType = "AND";
		}

		// creating Entry Tag
		Element entryElem = hqmfXmlProcessor.getOriginalDoc().createElement(ENTRY);
		entryElem.setAttribute(TYPE_CODE, "DRIV");

		// creating grouperCriteria element
		String root = "0";
		// String ext = setOpType.toUpperCase();

		String ext = setOpType + "_" + setOpNode.getAttributes().getNamedItem(UUID).getNodeValue();
		String localVariableName = clauseName + "_" + setOpNode.getAttributes().getNamedItem(UUID).getNodeValue();
		Node subTreeParentNode = checkIfSubTree(setOpNode.getParentNode());
		if (subTreeParentNode != null) {
			root = subTreeParentNode.getAttributes().getNamedItem(UUID).getNodeValue();
			if (subTreeParentNode.getAttributes().getNamedItem(QDM_VARIABLE) != null) {
				String isQdmVariable = subTreeParentNode.getAttributes().getNamedItem(QDM_VARIABLE).getNodeValue();
				if (TRUE.equalsIgnoreCase(isQdmVariable)) {
					String occText = null;
					// Handled Occurrence Of QDM Variable.
					if (subTreeParentNode.getAttributes().getNamedItem(INSTANCE_OF) != null) {
						occText = "occ" + subTreeParentNode.getAttributes().getNamedItem(INSTANCE).getNodeValue()
								+ "of_";
					}
					if (occText != null) {
						ext = occText + "qdm_var_" + ext;
					} else {
						ext = "qdm_var_" + ext;
					}
					localVariableName = "qdm_var_" + localVariableName;
				}
			}
		} else {
			root = UUIDUtilClient.uuid();
		}

		Node grouperElem = generateEmptyGrouper(hqmfXmlProcessor, root, ext);
		Node templateIdNode = getTemplateIdForSatisfies(hqmfXmlProcessor, setOpType);
		if (templateIdNode != null) {
			grouperElem.insertBefore(templateIdNode, grouperElem.getFirstChild());
		}

		NodeList childNodes = setOpNode.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node childNode = childNodes.item(i);
			String childName = childNode.getNodeName();
			if ("comment".equals(childName)) {
				continue;
			}

			// generate comment
			Comment comment = hqmfXmlProcessor.getOriginalDoc().createComment(
					"outBoundRelationship for " + childNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue());
			grouperElem.appendChild(comment);

			// generate outboundRelationship
			Element outboundRelElem = generateEmptyOutboundElem(hqmfXmlProcessor);
			Element conjunctionCodeElem = hqmfXmlProcessor.getOriginalDoc().createElement(CONJUNCTION_CODE);
			conjunctionCodeElem.setAttribute(CODE, conjunctionType);

			outboundRelElem.appendChild(conjunctionCodeElem);
			if (ELEMENT_REF.equals(childName) || SUB_TREE_REF.equals(childName)) {
				generateCritRefForNode(outboundRelElem, childNode);
			} else {
				switch (childName) {
				case SET_OP:
					generateCritRefSetOp(parentNode, hqmfXmlProcessor, childNode, outboundRelElem, clauseName);
					break;
				case RELATIONAL_OP:
					generateCritRefRelOp(parentNode, hqmfXmlProcessor, childNode, outboundRelElem, clauseName);
					break;
				case FUNCTIONAL_OP:
					generateCritRefFunctionalOp(childNode, outboundRelElem, clauseName);
					break;

				default:
					// Dont do anything
					break;
				}
			}
			grouperElem.appendChild(outboundRelElem);
		}

		// Added logic to show qdm_variable in extension if clause is of qdm variable
		// type.
		Node grouperEntryNode = grouperElem.cloneNode(true);
		if (FUNCTIONAL_OP.equals(setOpNode.getParentNode().getNodeName())) {
			Element excerptElement = generateExcerptEntryForFunctionalNode(setOpNode.getParentNode(), null,
					hqmfXmlProcessor, grouperEntryNode);
			// Comment comment = hqmfXmlProcessor.getOriginalDoc().createComment("excerpt
			// for
			// "+setOpNode.getParentNode().getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue());
			// grouperElem.appendChild(comment);
			grouperElem.appendChild(excerptElement);
		}
		Element localVarElem = hqmfXmlProcessor.getOriginalDoc().createElement(LOCAL_VARIABLE_NAME);
		localVarElem.setAttribute(VALUE, StringUtils.deleteWhitespace(localVariableName));
		entryElem.appendChild(localVarElem);

		entryElem.appendChild(grouperElem);
		parentNode.appendChild(entryElem);

		return entryElem;
	}

	/**
	 * Generate crit ref functional op.
	 *
	 * @param childNode
	 *            -Node
	 * @param outboundRelElem
	 *            - outBoundElement
	 * @param clauseName
	 *            the clause name
	 * @throws XPathExpressionException
	 *             -Exception
	 */
	private void generateCritRefFunctionalOp(Node childNode, Element outboundRelElem, String clauseName)
			throws XPathExpressionException {
		Element dataCriteriaSectionElem = (Element) measureExport.getHQMFXmlProcessor().getOriginalDoc()
				.getElementsByTagName(DATA_CRITERIA_SECTION).item(0);
		Node entryNode = generateFunctionalOpHQMF(childNode, dataCriteriaSectionElem, clauseName);
		if (entryNode != null && entryNode.getNodeName().equals(ENTRY)) {
			Node fChild = entryNode.getFirstChild();
			if (LOCAL_VARIABLE_NAME.equals(fChild.getNodeName())) {
				fChild = fChild.getNextSibling();
			}
			// create criteriaRef
			Element criteriaReference = measureExport.getHQMFXmlProcessor().getOriginalDoc()
					.createElement(CRITERIA_REFERENCE);
			criteriaReference.setAttribute(CLASS_CODE, fChild.getAttributes().getNamedItem(CLASS_CODE).getNodeValue());
			criteriaReference.setAttribute(MOOD_CODE, fChild.getAttributes().getNamedItem(MOOD_CODE).getNodeValue());
			NodeList childNodeList = fChild.getChildNodes();
			for (int j = 0; j < childNodeList.getLength(); j++) {
				Node entryChildNodes = childNodeList.item(j);
				if (entryChildNodes.getNodeName().equalsIgnoreCase(ID)) {
					Element id = measureExport.getHQMFXmlProcessor().getOriginalDoc().createElement(ID);
					id.setAttribute(ROOT, entryChildNodes.getAttributes().getNamedItem(ROOT).getNodeValue());
					id.setAttribute(EXTENSION, entryChildNodes.getAttributes().getNamedItem(EXTENSION).getNodeValue());
					criteriaReference.appendChild(id);
					outboundRelElem.appendChild(criteriaReference);
					break;
				}
			}
		}
	}

	/**
	 * This method is used to create a <templateId> tag for SATISFIES ALL/SATISFIES
	 * ANY functionalOps. These are functionalOp's but are converted to setOps and
	 * treated as Groupers.
	 *
	 * @param hqmfXmlProcessor
	 *            the hqmf xml processor
	 * @param type
	 *            the type
	 * @return the template id for satisfies
	 */
	private Node getTemplateIdForSatisfies(XmlProcessor hqmfXmlProcessor, String type) {
		Node templateIdNode = null;

		if (SATISFIES_ALL.equalsIgnoreCase(type) || SATISFIES_ANY.equalsIgnoreCase(type)) {
			templateIdNode = hqmfXmlProcessor.getOriginalDoc().createElement(TEMPLATE_ID);
			Element itemNode = hqmfXmlProcessor.getOriginalDoc().createElement(ITEM);

			// initialize rootOID with the OID for SATISFIES ALL
			String rootOID = "2.16.840.1.113883.10.20.28.3.109";
			// if we are dealing with SATISFIES ANY change the OID
			if (SATISFIES_ANY.equalsIgnoreCase(type)) {
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
	 * @param relOpNode
	 *            the rel op node
	 * @param dataCriteriaSectionElem
	 *            the data criteria section elem
	 * @param clauseName
	 *            the clause name
	 * @return the node
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private Node generateRelOpHQMF(Node relOpNode, Node dataCriteriaSectionElem, String clauseName)
			throws XPathExpressionException {
		Node finalNode = null;
		if (relOpNode.getChildNodes().getLength() == 2) {
			Node lhsNode = relOpNode.getFirstChild();
			Node rhsNode = relOpNode.getLastChild();
			String lhsName = lhsNode.getNodeName();

			// NamedNodeMap attribMap = relOpNode.getAttributes();
			// String relDisplayName =
			// StringUtils.deleteWhitespace(attribMap.getNamedItem(DISPLAY_NAME).getNodeValue());
			// String relUUID = attribMap.getNamedItem(UUID).getNodeValue();
			// String localVarName = "localVar_"+relDisplayName+"_"+relUUID;

			String localVarName = clauseName;
			Node parentNode = relOpNode.getParentNode();
			if (parentNode != null) {
				if (parentNode.getNodeName().equalsIgnoreCase("subTree") &&
						parentNode.getAttributes().getNamedItem(QDM_VARIABLE) != null) {
					String isQdmVariable = parentNode.getAttributes().getNamedItem(QDM_VARIABLE).getNodeValue();
					if (TRUE.equalsIgnoreCase(isQdmVariable)) {
						localVarName = localVarName.replace("$", "");
						localVarName = "qdm_var_" + localVarName;
					}
				}
			}
			localVarName = localVarName + "_" + UUIDUtilClient.uuid(5);
			localVarName = StringUtils.deleteWhitespace(localVarName);
			if (ELEMENT_REF.equals(lhsName)) {
				finalNode = getrelOpLHSQDM(relOpNode, dataCriteriaSectionElem, lhsNode, rhsNode, clauseName);
			} else if (RELATIONAL_OP.equals(lhsName)) {
				finalNode = getrelOpLHSRelOp(relOpNode, dataCriteriaSectionElem, lhsNode, rhsNode, clauseName);
				Node relOpParentNode = relOpNode.getParentNode();
				if (relOpParentNode.getNodeName().equalsIgnoreCase(FUNCTIONAL_OP)) {
					Element excerptElement = generateExcerptEntryForFunctionalNode(relOpNode.getParentNode(), lhsNode,
							measureExport.getHQMFXmlProcessor(), finalNode);
					if (excerptElement != null) {
						Node firstNode = finalNode.getFirstChild();
						if (LOCAL_VARIABLE_NAME.equals(firstNode.getNodeName())) {
							firstNode = firstNode.getNextSibling();
						}
						// Comment comment =
						// measureExport.getHQMFXmlProcessor().getOriginalDoc().createComment("entry for
						// "+relOpNode.getParentNode().getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue());
						// firstNode.appendChild(comment);
						firstNode.appendChild(excerptElement);
					}
				}
				// return finalNode;
			} else if (SET_OP.equals(lhsName)) {
				finalNode = getrelOpLHSSetOp(relOpNode, dataCriteriaSectionElem, lhsNode, rhsNode, clauseName);
				Node relOpParentNode = relOpNode.getParentNode();
				if (relOpParentNode.getNodeName().equalsIgnoreCase(FUNCTIONAL_OP)) {
					Element excerptElement = generateExcerptEntryForFunctionalNode(relOpNode.getParentNode(), lhsNode,
							measureExport.getHQMFXmlProcessor(), finalNode);
					if (excerptElement != null) {
						Node firstNode = finalNode.getFirstChild();
						if (LOCAL_VARIABLE_NAME.equals(firstNode.getNodeName())) {
							firstNode = firstNode.getNextSibling();
						}
						// Comment comment =
						// measureExport.getHQMFXmlProcessor().getOriginalDoc().createComment("entry for
						// "+relOpNode.getParentNode().getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue());
						// firstNode.appendChild(comment);
						firstNode.appendChild(excerptElement);
					}
				}
				// return finalNode;
			} else if (SUB_TREE_REF.equals(lhsName) || FUNCTIONAL_OP.equalsIgnoreCase(lhsName)) {
				Node functionalOpNodeWithChildQDM = checkLHSFunctionalOpWithChildQDM(lhsNode);
				if (functionalOpNodeWithChildQDM != null) {
					// Do something godawful here.
					Node functionEntryNode = generateFunctionalOpHQMF(functionalOpNodeWithChildQDM,
							(Element) dataCriteriaSectionElem, clauseName);
					dataCriteriaSectionElem.appendChild(functionEntryNode);
					finalNode = createSpecialGrouperForRelOp(relOpNode, functionEntryNode, rhsNode,
							dataCriteriaSectionElem, clauseName);
				} else if (FUNCTIONAL_OP.equalsIgnoreCase(lhsName)) {
					finalNode = getFunctionalOpLHS(relOpNode, dataCriteriaSectionElem, lhsNode, rhsNode, clauseName);
				} else {
					finalNode = getrelOpLHSSubtree(relOpNode, dataCriteriaSectionElem, lhsNode, rhsNode, clauseName);
				}

			}
			// else if(FUNCTIONAL_OP.equalsIgnoreCase(lhsName)) {
			// finalNode = getFunctionalOpLHS(relOpNode, dataCriteriaSectionElem, lhsNode,
			// rhsNode, clauseName);
			// }
			if (parentNode.getNodeName().equalsIgnoreCase("subTree")) {
				updateLocalVar(finalNode, localVarName);
			}
		} else {
			logger.debug("Relational Op:" + relOpNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue()
					+ " does not have exactly 2 children. Skipping HQMF for it.");
		}
		return finalNode;
	}

	/**
	 * When we have a case of "First:(Encounter,Performed:Inpatient) During
	 * Measurement Period"; we need to generate a entry with Grouper.
	 *
	 * @param relOpNode
	 *            the rel op node
	 * @param functionEntryNode
	 *            the function entry node
	 * @param rhsNode
	 *            the rhs node
	 * @param dataCriteriaSectionElem
	 *            the data criteria section elem
	 * @param clauseName
	 *            the clause name
	 * @return the node
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private Node createSpecialGrouperForRelOp(Node relOpNode, Node functionEntryNode, Node rhsNode,
			Node dataCriteriaSectionElem, String clauseName) throws XPathExpressionException {
		XmlProcessor hqmfXmlProcessor = measureExport.getHQMFXmlProcessor();

		// creating Entry Tag
		Element entryElem = hqmfXmlProcessor.getOriginalDoc().createElement(ENTRY);
		entryElem.setAttribute(TYPE_CODE, "DRIV");

		String localVariableName = relOpNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
		Element localVarElem = hqmfXmlProcessor.getOriginalDoc().createElement(LOCAL_VARIABLE_NAME);
		localVariableName = localVariableName + "_" + UUIDUtilClient.uuid(5);
		localVariableName = StringUtils.deleteWhitespace(localVariableName);
		localVarElem.setAttribute(VALUE, localVariableName);
		entryElem.appendChild(localVarElem);

		String root = relOpNode.getAttributes().getNamedItem(UUID).getNodeValue();
		String ext = StringUtils.deleteWhitespace(relOpNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue()
				+ "_" + relOpNode.getAttributes().getNamedItem(UUID).getNodeValue());

		Node grouperElem = generateEmptyGrouper(hqmfXmlProcessor, root, ext);
		entryElem.appendChild(grouperElem);

		Node subTreeParentNode = checkIfSubTree(relOpNode.getParentNode());
		Node idNode = findNode(entryElem, "ID");
		if (idNode != null && subTreeParentNode != null) {
			String idExtension = idNode.getAttributes().getNamedItem(EXTENSION).getNodeValue();
			String idRoot = idNode.getAttributes().getNamedItem(ROOT).getNodeValue();
			root = subTreeParentNode.getAttributes().getNamedItem(UUID).getNodeValue();
			ext = StringUtils.deleteWhitespace(relOpNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue()
					+ "_" + relOpNode.getAttributes().getNamedItem(UUID).getNodeValue());
			if (subTreeParentNode.getAttributes().getNamedItem(QDM_VARIABLE) != null) {
				String isQdmVariable = subTreeParentNode.getAttributes().getNamedItem(QDM_VARIABLE).getNodeValue();
				if (TRUE.equalsIgnoreCase(isQdmVariable)) {
					String occText = null;
					// Handled Occurrence Of QDM Variable.
					if (subTreeParentNode.getAttributes().getNamedItem(INSTANCE_OF) != null) {
						occText = "occ" + subTreeParentNode.getAttributes().getNamedItem(INSTANCE).getNodeValue()
								+ "of_";
					}
					if (occText != null) {
						ext = occText + "qdm_var_" + ext;
					} else {
						ext = "qdm_var_" + ext;
					}
				}
			}
			idNode.getAttributes().getNamedItem(ROOT).setNodeValue(root);
			idNode.getAttributes().getNamedItem(EXTENSION).setNodeValue(ext);
			// Updated Excerpt tag idNode root and extension.
			// String hqmfXmlString = measureExport.getHQMFXmlProcessor().getOriginalXml();
			Node idNodeExcerpt = measureExport.getHQMFXmlProcessor().findNode(
					measureExport.getHQMFXmlProcessor().getOriginalDoc(),
					"//entry/*/excerpt/*/id[@root='" + idRoot + "'][@extension=\"" + idExtension + "\"]");
			if (idNodeExcerpt != null) {
				idNodeExcerpt.getAttributes().getNamedItem(ROOT).setNodeValue(root);
				idNodeExcerpt.getAttributes().getNamedItem(EXTENSION).setNodeValue(ext);
			}

		}

		// Element temporallyRelatedInfoNode = createBaseTemporalNode(relOpNode,
		// measureExport.getHQMFXmlProcessor());
		Element temporallyRelatedInfoNode = null;
		if (!FULFILLS.equalsIgnoreCase(relOpNode.getAttributes().getNamedItem(TYPE).getNodeValue())) {
			temporallyRelatedInfoNode = createBaseTemporalNode(relOpNode, measureExport.getHQMFXmlProcessor());
		} else {
			temporallyRelatedInfoNode = measureExport.getHQMFXmlProcessor().getOriginalDoc()
					.createElement(OUTBOUND_RELATIONSHIP);
			temporallyRelatedInfoNode.setAttribute(TYPE_CODE, "FLFS");
		}
		handleRelOpRHS(rhsNode, temporallyRelatedInfoNode, clauseName);
		Node firstChild = entryElem.getFirstChild();
		if (LOCAL_VARIABLE_NAME.equals(firstChild.getNodeName())) {
			firstChild = firstChild.getNextSibling();
		}
		NodeList outBoundList = ((Element) firstChild).getElementsByTagName(OUTBOUND_RELATIONSHIP);
		if (outBoundList != null && outBoundList.getLength() > 0) {
			Node outBound = outBoundList.item(0);
			firstChild.insertBefore(temporallyRelatedInfoNode, outBound);
		} else {
			NodeList excerptList = ((Element) firstChild).getElementsByTagName(EXCERPT);
			if (excerptList != null && excerptList.getLength() > 0) {
				Node excerptNode = excerptList.item(0);
				firstChild.insertBefore(temporallyRelatedInfoNode, excerptNode);
			} else {
				firstChild.appendChild(temporallyRelatedInfoNode);
			}
		}

		// Add a outBound Relationship for the 'functionEntryNode' passed above.
		Element outBoundForFunction = measureExport.getHQMFXmlProcessor().getOriginalDoc()
				.createElement(OUTBOUND_RELATIONSHIP);
		outBoundForFunction.setAttribute(TYPE_CODE, "COMP");
		Node idNodeForFunctionEntryNode = findNode(functionEntryNode, "ID");
		Node excerptNodeForFunctionEntryNode = findNode(functionEntryNode, "excerpt");
		Node idNodeInExcerptNode = findNode(excerptNodeForFunctionEntryNode, "id");
		String newExtension = StringUtils.deleteWhitespace(clauseName) + "_"
				+ idNodeForFunctionEntryNode.getAttributes().getNamedItem(EXTENSION).getNodeValue();

		idNodeForFunctionEntryNode.getAttributes().getNamedItem(EXTENSION).setNodeValue(newExtension);
		idNodeInExcerptNode.getAttributes().getNamedItem(EXTENSION).setNodeValue(newExtension);

		Node firstChildOfFunctionEntryElem = functionEntryNode.getFirstChild();
		if (LOCAL_VARIABLE_NAME.equals(firstChildOfFunctionEntryElem.getNodeName())) {
			firstChildOfFunctionEntryElem = firstChildOfFunctionEntryElem.getNextSibling();
		}
		NamedNodeMap criteriaNodeAttributeMap = firstChildOfFunctionEntryElem.getAttributes();
		if (criteriaNodeAttributeMap.getNamedItem(CLASS_CODE) != null
				&& criteriaNodeAttributeMap.getNamedItem(MOOD_CODE) != null) {
			// create criteriaRef
			Element criteriaReference = hqmfXmlProcessor.getOriginalDoc().createElement(CRITERIA_REFERENCE);
			criteriaReference.setAttribute(CLASS_CODE,
					criteriaNodeAttributeMap.getNamedItem(CLASS_CODE).getNodeValue());
			criteriaReference.setAttribute(MOOD_CODE,
					criteriaNodeAttributeMap.getNamedItem(MOOD_CODE).getNodeValue());

			Node idNodeForFunctionEntryNode_Clone = idNodeForFunctionEntryNode.cloneNode(true);
			criteriaReference.appendChild(idNodeForFunctionEntryNode_Clone);

			outBoundForFunction.appendChild(criteriaReference);
			grouperElem.appendChild(outBoundForFunction);
		}
		dataCriteriaSectionElem.appendChild(entryElem);
		return entryElem;
	}

	/**
	 * This is to be called when you want to check If the node passed is a
	 * FunctionOp with it's child being an elementRef/QDM. If the node passed is a
	 * SubTree/Clause node then this will "recursively" look into the child of that
	 * SubTree/Clause node to see if that child is a FunctionOp with child being an
	 * elementRef/QDM.
	 *
	 * @param node
	 *            the node
	 * @return the node
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private Node checkLHSFunctionalOpWithChildQDM(Node node) throws XPathExpressionException {
		Node returnFunctionalNode = null;

		String nodeName = node.getNodeName();
		if (FUNCTIONAL_OP.equalsIgnoreCase(nodeName)) {
			returnFunctionalNode = node;
			/*
			 * Node childNode = node.getFirstChild(); if(childNode != null &&
			 * ELEMENT_REF.equals(childNode.getNodeName())){ returnFunctionalNode = node; }
			 */
		} else if (SUB_TREE_REF.equals(nodeName)) {
			String subTreeUUID = node.getAttributes().getNamedItem(ID).getNodeValue();

			String xpath = "/measure/subTreeLookUp/subTree[@uuid='" + subTreeUUID + "']";
			Node subTreeNode = measureExport.getSimpleXMLProcessor()
					.findNode(measureExport.getSimpleXMLProcessor().getOriginalDoc(), xpath);

			Node childNode = subTreeNode.getFirstChild();
			if (childNode != null) {
				returnFunctionalNode = checkLHSFunctionalOpWithChildQDM(childNode);
			}
		}
		return returnFunctionalNode;
	}

	/**
	 * Gets the functional op lhs.
	 *
	 * @param relOpNode
	 *            the rel op node
	 * @param dataCriteriaSectionElem
	 *            the data criteria section elem
	 * @param lhsNode
	 *            the lhs node
	 * @param rhsNode
	 *            the rhs node
	 * @param clauseName
	 *            the clause name
	 * @return the functional op lhs
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private Node getFunctionalOpLHS(Node relOpNode, Node dataCriteriaSectionElem, Node lhsNode, Node rhsNode,
			String clauseName) throws XPathExpressionException {
		Node entryNode = generateFunctionalOpHQMF(lhsNode, (Element) dataCriteriaSectionElem, clauseName);

		// Comment comment =
		// measureExport.getHQMFXmlProcessor().getOriginalDoc().createComment("entry for
		// "+relOpNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue());
		// dataCriteriaSectionElem.appendChild(comment);
		if (entryNode != null) {
			Node subTreeParentNode = checkIfSubTree(relOpNode.getParentNode());
			Node idNode = findNode(entryNode, "ID");
			if (idNode != null && subTreeParentNode != null) {
				String idExtension = idNode.getAttributes().getNamedItem(EXTENSION).getNodeValue();
				String idRoot = idNode.getAttributes().getNamedItem(ROOT).getNodeValue();
				String root = subTreeParentNode.getAttributes().getNamedItem(UUID).getNodeValue();
				String ext = StringUtils
						.deleteWhitespace(relOpNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue() + "_"
								+ relOpNode.getAttributes().getNamedItem(UUID).getNodeValue());
				if (subTreeParentNode.getAttributes().getNamedItem(QDM_VARIABLE) != null) {
					String isQdmVariable = subTreeParentNode.getAttributes().getNamedItem(QDM_VARIABLE).getNodeValue();
					if (TRUE.equalsIgnoreCase(isQdmVariable)) {
						String occText = null;
						// Handled Occurrence Of QDM Variable.
						if (subTreeParentNode.getAttributes().getNamedItem(INSTANCE_OF) != null) {
							occText = "occ" + subTreeParentNode.getAttributes().getNamedItem(INSTANCE).getNodeValue()
									+ "of_";
						}
						if (occText != null) {
							ext = occText + "qdm_var_" + ext;
						} else {
							ext = "qdm_var_" + ext;
						}
					}
				}
				idNode.getAttributes().getNamedItem(ROOT).setNodeValue(root);
				idNode.getAttributes().getNamedItem(EXTENSION).setNodeValue(ext);
				// Updated Excerpt tag idNode root and extension.
				// String hqmfXmlString = measureExport.getHQMFXmlProcessor().getOriginalXml();
				Node idNodeExcerpt = measureExport.getHQMFXmlProcessor().findNode(
						measureExport.getHQMFXmlProcessor().getOriginalDoc(),
						"//entry/*/excerpt/*/id[@root='" + idRoot + "'][@extension=\"" + idExtension + "\"]");
				if (idNodeExcerpt != null) {
					idNodeExcerpt.getAttributes().getNamedItem(ROOT).setNodeValue(root);
					idNodeExcerpt.getAttributes().getNamedItem(EXTENSION).setNodeValue(ext);
				}

			}

			// Element temporallyRelatedInfoNode = createBaseTemporalNode(relOpNode,
			// measureExport.getHQMFXmlProcessor());
			Element temporallyRelatedInfoNode = null;
			if (!FULFILLS.equalsIgnoreCase(relOpNode.getAttributes().getNamedItem(TYPE).getNodeValue())) {
				temporallyRelatedInfoNode = createBaseTemporalNode(relOpNode, measureExport.getHQMFXmlProcessor());
			} else {
				temporallyRelatedInfoNode = measureExport.getHQMFXmlProcessor().getOriginalDoc()
						.createElement(OUTBOUND_RELATIONSHIP);
				temporallyRelatedInfoNode.setAttribute(TYPE_CODE, "FLFS");
			}
			handleRelOpRHS(rhsNode, temporallyRelatedInfoNode, clauseName);
			Node firstChild = entryNode.getFirstChild();
			if (LOCAL_VARIABLE_NAME.equals(firstChild.getNodeName())) {
				firstChild = firstChild.getNextSibling();
			}
			NodeList outBoundList = ((Element) firstChild).getElementsByTagName(OUTBOUND_RELATIONSHIP);
			if (outBoundList != null && outBoundList.getLength() > 0) {
				Node outBound = outBoundList.item(0);
				firstChild.insertBefore(temporallyRelatedInfoNode, outBound);
			} else {
				NodeList excerptList = ((Element) firstChild).getElementsByTagName(EXCERPT);
				if (excerptList != null && excerptList.getLength() > 0) {
					Node excerptNode = excerptList.item(0);
					firstChild.insertBefore(temporallyRelatedInfoNode, excerptNode);
				} else {
					firstChild.appendChild(temporallyRelatedInfoNode);
				}
			}
			dataCriteriaSectionElem.appendChild(entryNode);
		}
		/*
		 * else{ Comment commnt = measureExport.getHQMFXmlProcessor().getOriginalDoc().
		 * createComment("CHECK:Could not find an entry for functionalOp:"+lhsNode.
		 * getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue());
		 * dataCriteriaSectionElem.appendChild(commnt); }
		 */
		return entryNode;
	}

	/**
	 * Find node.
	 *
	 * @param criteriaNodeInEntry
	 *            the criteria node in entry
	 * @param nodeName
	 *            the node name
	 * @return idNode
	 */
	private Node findNode(Node criteriaNodeInEntry, String nodeName) {
		Node idNode = null;
		for (int i = 0; i < criteriaNodeInEntry.getChildNodes().getLength(); i++) {
			Node childNode = criteriaNodeInEntry.getChildNodes().item(i);
			String childNodeName = childNode.getNodeName();
			if (childNodeName.contains("Criteria")) {
				NodeList criteriaChildNodes = childNode.getChildNodes();
				for (int j = 0; j < criteriaChildNodes.getLength(); j++) {
					Node criteriaChildNode = criteriaChildNodes.item(j);
					if (nodeName.equalsIgnoreCase(criteriaChildNode.getNodeName())) {
						idNode = criteriaChildNode;
						break;
					}
				}
				break;
			}
		}
		return idNode;
	}

	/**
	 * Gets the rel op lhs subtree.
	 *
	 * @param relOpNode
	 *            the rel op node
	 * @param dataCriteriaSectionElem
	 *            the data criteria section elem
	 * @param lhsNode
	 *            the lhs node
	 * @param rhsNode
	 *            the rhs node
	 * @param clauseName
	 *            the clause name
	 * @return the rel op lhs subtree
	 */
	private Node getrelOpLHSSubtree(Node relOpNode, Node dataCriteriaSectionElem, Node lhsNode, Node rhsNode,
			String clauseName) {

		try {
			String subTreeUUID = lhsNode.getAttributes().getNamedItem(ID).getNodeValue();
			String root = subTreeUUID;

			Node relOpParentNode = relOpNode.getParentNode();

			String xpath = "/measure/subTreeLookUp/subTree[@uuid='" + subTreeUUID + "']";
			Node subTreeNode = measureExport.getSimpleXMLProcessor()
					.findNode(measureExport.getSimpleXMLProcessor().getOriginalDoc(), xpath);
			if (subTreeNode != null) {
				/**
				 * Check if the Clause has already been generated. If it is not generated yet,
				 * then generate it by calling the 'generateSubTreeXML' method.
				 */

				if (!subTreeNodeMap.containsKey(subTreeUUID)) {
					generateSubTreeXML(subTreeNode, false);
				}
				String isQdmVariable = subTreeNode.getAttributes().getNamedItem(QDM_VARIABLE).getNodeValue();
				Node firstChild = subTreeNode.getFirstChild();
				String firstChildName = firstChild.getNodeName();

				String ext = StringUtils
						.deleteWhitespace(firstChild.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue());
				if (FUNCTIONAL_OP.equals(firstChildName) || RELATIONAL_OP.equals(firstChildName)
						|| SET_OP.equals(firstChildName)) {
					ext += "_" + firstChild.getAttributes().getNamedItem(UUID).getNodeValue();
				}

				if (ELEMENT_REF.equals(firstChildName)) {
					ext = getElementRefExt(firstChild, measureExport.getSimpleXMLProcessor());
				} else if (FUNCTIONAL_OP.equals(firstChildName)) {
					if (firstChild.getFirstChild() != null) {
						Node functionChild = firstChild.getFirstChild();
						if (functionChild != null) {
							if (functionChild.getNodeName().equalsIgnoreCase(SUB_TREE_REF)) {
								ext = functionChild.getAttributes().getNamedItem(ID).getNodeValue();
							} else if (functionChild.getNodeName().equalsIgnoreCase(ELEMENT_REF)) {
								ext = getElementRefExt(functionChild, measureExport.getSimpleXMLProcessor());
							} else {
								ext = StringUtils
										.deleteWhitespace(functionChild.getAttributes().getNamedItem(DISPLAY_NAME)
												.getNodeValue() + "_"
												+ functionChild.getAttributes().getNamedItem(UUID).getNodeValue())
										.replaceAll(":", "_");
							}
						}
					}
				}
				if (TRUE.equalsIgnoreCase(isQdmVariable)) {
					String occText = null;
					// Handled Occurrence Of QDM Variable.
					if (subTreeNode.getAttributes().getNamedItem(INSTANCE_OF) != null) {
						occText = "occ" + subTreeNode.getAttributes().getNamedItem("instance").getNodeValue() + "of_";
					}
					if (occText != null) {
						ext = occText + "qdm_var_" + ext;
					} else {
						ext = "qdm_var_" + ext;
					}
				}

				Node idNodeQDM = measureExport.getHQMFXmlProcessor().findNode(
						measureExport.getHQMFXmlProcessor().getOriginalDoc(),
						"//entry/*/id[@root='" + root + "'][@extension=\"" + ext + "\"]");
				if (idNodeQDM != null) {
					String newExt = StringUtils
							.deleteWhitespace(relOpNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue() + "_"
									+ relOpNode.getAttributes().getNamedItem(UUID).getNodeValue());
					if (relOpParentNode != null && SUB_TREE.equals(relOpParentNode.getNodeName())) {
						root = relOpParentNode.getAttributes().getNamedItem(UUID).getNodeValue();
						Node qdmVarNode = relOpParentNode.getAttributes().getNamedItem(QDM_VARIABLE);
						if (qdmVarNode != null) {
							String isQdmVar = relOpParentNode.getAttributes().getNamedItem(QDM_VARIABLE).getNodeValue();
							if (TRUE.equalsIgnoreCase(isQdmVar)) {
								String occText = null;
								// Handled Occurrence Of QDM Variable.
								if (relOpParentNode.getAttributes().getNamedItem(INSTANCE_OF) != null) {
									occText = "occ"
											+ relOpParentNode.getAttributes().getNamedItem("instance").getNodeValue()
											+ "of_";
								}
								if (occText != null) {
									newExt = occText + "qdm_var_" + newExt;
								} else {
									newExt = "qdm_var_" + newExt;
								}
							}
						}
					} else {
						Node tempParentNode = checkIfParentSubTree(relOpParentNode);
						if (tempParentNode != null) {
							root = tempParentNode.getAttributes().getNamedItem(UUID).getNodeValue();
						}
					}

					Node parent = idNodeQDM.getParentNode();
					Node isOcc = subTreeNode.getAttributes().getNamedItem(INSTANCE_OF);
					Node newEntryNode = null;

					if (TRUE.equals(isQdmVariable) && isOcc == null) {
						XmlProcessor hqmfXmlProcessor = measureExport.getHQMFXmlProcessor();
						// creating Entry Tag
						Element entryElem = hqmfXmlProcessor.getOriginalDoc().createElement(ENTRY);
						entryElem.setAttribute(TYPE_CODE, "DRIV");
						// create empty grouperCriteria
						Node grouperElem = generateEmptyGrouper(hqmfXmlProcessor, root, newExt);

						// generate outboundRelationship
						Element outboundRelElem = generateEmptyOutboundElem(hqmfXmlProcessor);

						NamedNodeMap attribMap = parent.getAttributes();
						String classCode = attribMap.getNamedItem(CLASS_CODE).getNodeValue();
						String moodCode = attribMap.getNamedItem(MOOD_CODE).getNodeValue();

						// create criteriaRef
						Element criteriaReference = hqmfXmlProcessor.getOriginalDoc().createElement(CRITERIA_REFERENCE);
						criteriaReference.setAttribute(CLASS_CODE, classCode);
						criteriaReference.setAttribute(MOOD_CODE, moodCode);

						Element id = hqmfXmlProcessor.getOriginalDoc().createElement(ID);
						id.setAttribute(ROOT, subTreeUUID);
						id.setAttribute(EXTENSION, ext);

						criteriaReference.appendChild(id);
						outboundRelElem.appendChild(criteriaReference);

						Element localVarElem = hqmfXmlProcessor.getOriginalDoc().createElement(LOCAL_VARIABLE_NAME);
						localVarElem.setAttribute(VALUE, ext);
						entryElem.appendChild(localVarElem);

						grouperElem.appendChild(outboundRelElem);
						entryElem.appendChild(grouperElem);

						newEntryNode = entryElem;
					} else {
						Node entryNodeForSubTree = idNodeQDM.getParentNode().getParentNode();
						newEntryNode = entryNodeForSubTree.cloneNode(true);

						NodeList idChildNodeList = ((Element) newEntryNode).getElementsByTagName(ID);
						if (idChildNodeList != null && idChildNodeList.getLength() > 0) {
							Node idChildNode = idChildNodeList.item(0);
							idChildNode.getAttributes().getNamedItem(EXTENSION).setNodeValue(newExt);
							idChildNode.getAttributes().getNamedItem(ROOT).setNodeValue(root);
						}
					}

					// Element temporallyRelatedInfoNode = createBaseTemporalNode(relOpNode,
					// measureExport.getHQMFXmlProcessor());
					Element temporallyRelatedInfoNode = null;
					if (!FULFILLS.equalsIgnoreCase(relOpNode.getAttributes().getNamedItem(TYPE).getNodeValue())) {
						temporallyRelatedInfoNode = createBaseTemporalNode(relOpNode,
								measureExport.getHQMFXmlProcessor());
					} else {
						temporallyRelatedInfoNode = measureExport.getHQMFXmlProcessor().getOriginalDoc()
								.createElement(OUTBOUND_RELATIONSHIP);
						temporallyRelatedInfoNode.setAttribute(TYPE_CODE, "FLFS");
					}

					handleRelOpRHS(rhsNode, temporallyRelatedInfoNode, clauseName);

					Node firstNode = newEntryNode.getFirstChild();
					if (LOCAL_VARIABLE_NAME.equals(firstNode.getNodeName())) {
						firstNode = firstNode.getNextSibling();
					}
					NodeList outBoundList = ((Element) firstNode).getElementsByTagName(OUTBOUND_RELATIONSHIP);
					if (outBoundList != null && outBoundList.getLength() > 0) {
						Node outBound = outBoundList.item(0);
						firstNode.insertBefore(temporallyRelatedInfoNode, outBound);
					} else {
						firstNode.appendChild(temporallyRelatedInfoNode);
					}
					// Entry for Functional Op.
					if (FUNCTIONAL_OP.equals(relOpParentNode.getNodeName())) {
						Element excerptElement = generateExcerptEntryForFunctionalNode(relOpParentNode, lhsNode,
								measureExport.getHQMFXmlProcessor(), firstNode.getParentNode());
						if (excerptElement != null) {
							// Comment comment =
							// measureExport.getHQMFXmlProcessor().getOriginalDoc().createComment("entry for
							// "+relOpParentNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue());
							// firstNode.appendChild(comment);
							firstNode.appendChild(excerptElement);
						}
					}
					// create comment node
					// Comment comment =
					// measureExport.getHQMFXmlProcessor().getOriginalDoc().createComment("entry for
					// "+relOpNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue());
					// dataCriteriaSectionElem.appendChild(comment);
					dataCriteriaSectionElem.appendChild(newEntryNode);
					return newEntryNode;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the rel op lhs set op.
	 *
	 * @param relOpNode
	 *            the rel op node
	 * @param dataCriteriaSectionElem
	 *            the data criteria section elem
	 * @param lhsNode
	 *            the lhs node
	 * @param rhsNode
	 *            the rhs node
	 * @param clauseName
	 *            the clause name
	 * @return the rel op lhs set op
	 */
	private Node getrelOpLHSSetOp(Node relOpNode, Node dataCriteriaSectionElem, Node lhsNode, Node rhsNode,
			String clauseName) {

		XmlProcessor hqmfXmlProcessor = measureExport.getHQMFXmlProcessor();
		// Node relOpParentNode = relOpNode.getParentNode();

		try {
			Node setOpEntryNode = generateSetOpHQMF(lhsNode, dataCriteriaSectionElem, clauseName);
			// Element temporallyRelatedInfoNode = createBaseTemporalNode(relOpNode,
			// hqmfXmlProcessor);
			Node relOpParentNode = checkIfSubTree(relOpNode.getParentNode());
			if (relOpParentNode != null) {
				NodeList idChildNodeList = ((Element) setOpEntryNode).getElementsByTagName(ID);
				if (idChildNodeList != null && idChildNodeList.getLength() > 0) {
					Node idChildNode = idChildNodeList.item(0);
					String root = relOpParentNode.getAttributes().getNamedItem(UUID).getNodeValue();
					String ext = StringUtils
							.deleteWhitespace(relOpNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue() + "_"
									+ relOpNode.getAttributes().getNamedItem(UUID).getNodeValue());
					if (relOpParentNode.getAttributes().getNamedItem(QDM_VARIABLE) != null) {
						String isQdmVariable = relOpParentNode.getAttributes().getNamedItem(QDM_VARIABLE)
								.getNodeValue();
						if (TRUE.equalsIgnoreCase(isQdmVariable)) {
							String occText = null;
							// Handled Occurrence Of QDM Variable.
							if (relOpParentNode.getAttributes().getNamedItem(INSTANCE_OF) != null) {
								occText = "occ" + relOpParentNode.getAttributes().getNamedItem(INSTANCE).getNodeValue()
										+ "of_";
							}
							if (occText != null) {
								ext = occText + "qdm_var_" + ext;
							} else {
								ext = "qdm_var_" + ext;
							}
						}

					}
					idChildNode.getAttributes().getNamedItem(EXTENSION).setNodeValue(ext);
					idChildNode.getAttributes().getNamedItem(ROOT).setNodeValue(root);
				}
			}
			Element temporallyRelatedInfoNode = null;
			if (!FULFILLS.equalsIgnoreCase(relOpNode.getAttributes().getNamedItem(TYPE).getNodeValue())) {
				temporallyRelatedInfoNode = createBaseTemporalNode(relOpNode, hqmfXmlProcessor);
			} else {
				temporallyRelatedInfoNode = hqmfXmlProcessor.getOriginalDoc().createElement(OUTBOUND_RELATIONSHIP);
				temporallyRelatedInfoNode.setAttribute(TYPE_CODE, "FLFS");
			}
			handleRelOpRHS(rhsNode, temporallyRelatedInfoNode, clauseName);

			Node firstChild = setOpEntryNode.getFirstChild();
			if (LOCAL_VARIABLE_NAME.equals(firstChild.getNodeName())) {
				firstChild = firstChild.getNextSibling();
			}
			NodeList outBoundList = ((Element) firstChild).getElementsByTagName(OUTBOUND_RELATIONSHIP);
			if (outBoundList != null && outBoundList.getLength() > 0) {
				Node outBound = outBoundList.item(0);
				firstChild.insertBefore(temporallyRelatedInfoNode, outBound);
			} else {
				firstChild.appendChild(temporallyRelatedInfoNode);
			}

			// create comment node
			// Comment comment = hqmfXmlProcessor.getOriginalDoc().createComment("entry for
			// "+relOpNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue());
			// dataCriteriaSectionElem.appendChild(comment);
			dataCriteriaSectionElem.appendChild(setOpEntryNode);
			return setOpEntryNode;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Gets the rel op lhs rel op.
	 *
	 * @param relOpNode
	 *            the rel op node
	 * @param dataCriteriaSectionElem
	 *            the data criteria section elem
	 * @param lhsNode
	 *            the lhs node
	 * @param rhsNode
	 *            the rhs node
	 * @param clauseName
	 *            the clause name
	 * @return the rel op lhs rel op
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private Node getrelOpLHSRelOp(Node relOpNode, Node dataCriteriaSectionElem, Node lhsNode, Node rhsNode,
			String clauseName) throws XPathExpressionException {

		XmlProcessor hqmfXmlProcessor = measureExport.getHQMFXmlProcessor();
		// Node relOpParentNode = relOpNode.getParentNode();

		try {
			Node relOpEntryNode = generateRelOpHQMF(lhsNode, dataCriteriaSectionElem, clauseName);
			/*
			 * Element temporallyRelatedInfoNode = createBaseTemporalNode(relOpNode,
			 * hqmfXmlProcessor);
			 */

			Node relOpParentNode = checkIfSubTree(relOpNode.getParentNode());

			if (relOpParentNode != null) {
				NodeList idChildNodeList = ((Element) relOpEntryNode).getElementsByTagName(ID);

				if (idChildNodeList != null && idChildNodeList.getLength() > 0) {
					Node idChildNode = idChildNodeList.item(0);
					String root = relOpParentNode.getAttributes().getNamedItem(UUID).getNodeValue();
					String ext = StringUtils
							.deleteWhitespace(relOpNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue() + "_"
									+ relOpNode.getAttributes().getNamedItem(UUID).getNodeValue());
					if (relOpParentNode.getAttributes().getNamedItem(QDM_VARIABLE) != null) {
						String isQdmVariable = relOpParentNode.getAttributes().getNamedItem(QDM_VARIABLE)
								.getNodeValue();
						if (TRUE.equalsIgnoreCase(isQdmVariable)) {
							String occText = null;
							// Handled Occurrence Of QDM Variable.
							if (relOpParentNode.getAttributes().getNamedItem(INSTANCE_OF) != null) {
								occText = "occ"
										+ relOpParentNode.getAttributes().getNamedItem("instance").getNodeValue()
										+ "of_";
							}
							if (occText != null) {
								ext = occText + "qdm_var_" + ext;
							} else {
								ext = "qdm_var_" + ext;
							}
						}
					}
					idChildNode.getAttributes().getNamedItem(EXTENSION).setNodeValue(ext);
					idChildNode.getAttributes().getNamedItem(ROOT).setNodeValue(root);
				}
			}
			Element temporallyRelatedInfoNode = null;
			if (!FULFILLS.equalsIgnoreCase(relOpNode.getAttributes().getNamedItem(TYPE).getNodeValue())) {
				temporallyRelatedInfoNode = createBaseTemporalNode(relOpNode, hqmfXmlProcessor);
			} else {
				temporallyRelatedInfoNode = hqmfXmlProcessor.getOriginalDoc().createElement(OUTBOUND_RELATIONSHIP);
				temporallyRelatedInfoNode.setAttribute(TYPE_CODE, "FLFS");
			}

			handleRelOpRHS(rhsNode, temporallyRelatedInfoNode, clauseName);

			Node firstChild = relOpEntryNode.getFirstChild();
			if (LOCAL_VARIABLE_NAME.equals(firstChild.getNodeName())) {
				firstChild = firstChild.getNextSibling();
			}
			NodeList outBoundList = ((Element) firstChild).getElementsByTagName(OUTBOUND_RELATIONSHIP);
			if (outBoundList != null && outBoundList.getLength() > 0) {
				Node outBound = outBoundList.item(0);
				firstChild.insertBefore(temporallyRelatedInfoNode, outBound);
			} else {
				firstChild.appendChild(temporallyRelatedInfoNode);
			}
			// create comment node
			// Comment comment = hqmfXmlProcessor.getOriginalDoc().createComment("entry for
			// "+relOpNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue());
			// dataCriteriaSectionElem.appendChild(comment);
			dataCriteriaSectionElem.appendChild(relOpEntryNode);
			return relOpEntryNode;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Gets the rel op lhsqdm.
	 *
	 * @param relOpNode
	 *            the rel op node
	 * @param dataCriteriaSectionElem
	 *            the data criteria section elem
	 * @param lhsNode
	 *            the lhs node
	 * @param rhsNode
	 *            the rhs node
	 * @param clauseName
	 *            the clause name
	 * @return the rel op lhsqdm
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private Node getrelOpLHSQDM(Node relOpNode, Node dataCriteriaSectionElem, Node lhsNode, Node rhsNode,
			String clauseName) throws XPathExpressionException {

		String ext = getElementRefExt(lhsNode, measureExport.getSimpleXMLProcessor());
		String root = lhsNode.getAttributes().getNamedItem(ID).getNodeValue();
		XmlProcessor hqmfXmlProcessor = measureExport.getHQMFXmlProcessor();
		Node relOpParentNode = relOpNode.getParentNode();
		Element excerptElement = null;
		Node idNodeQDM = hqmfXmlProcessor.findNode(hqmfXmlProcessor.getOriginalDoc(),
				"//entry/*/id[@root='" + root + "'][@extension=\"" + ext + "\"]");

		if (relOpParentNode != null && idNodeQDM != null) {
			ext = StringUtils.deleteWhitespace(relOpNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue() + "_"
					+ relOpNode.getAttributes().getNamedItem(UUID).getNodeValue());
			Node subTreeParentNode = checkIfSubTree(relOpParentNode);
			if (subTreeParentNode != null) {
				root = subTreeParentNode.getAttributes().getNamedItem(UUID).getNodeValue();
				if (subTreeParentNode.getAttributes().getNamedItem(QDM_VARIABLE) != null) {
					String isQdmVariable = subTreeParentNode.getAttributes().getNamedItem(QDM_VARIABLE).getNodeValue();
					if (TRUE.equalsIgnoreCase(isQdmVariable)) {
						String occText = null;
						// Handled Occurrence Of QDM Variable.
						if (relOpParentNode.getAttributes().getNamedItem(INSTANCE_OF) != null) {
							occText = "occ" + relOpParentNode.getAttributes().getNamedItem("instance").getNodeValue()
									+ "of_";
						}
						if (occText != null) {
							ext = occText + "qdm_var_" + ext;
						} else {
							ext = "qdm_var_" + ext;
						}
					}
				}
			}

			Node entryNodeForElementRef = idNodeQDM.getParentNode().getParentNode();
			Node clonedEntryNodeForElementRef = entryNodeForElementRef.cloneNode(true);
			/*
			 * Element localVarName = (Element)
			 * ((Element)clonedEntryNodeForElementRef).getElementsByTagName(
			 * "localVariableName").item(0);
			 * localVarName.setAttribute(VALUE,findSubTreeDisplayName(lhsNode));
			 */
			NodeList idChildNodeList = ((Element) clonedEntryNodeForElementRef).getElementsByTagName(ID);
			updateLocalVar(clonedEntryNodeForElementRef, ext);
			if (idChildNodeList != null && idChildNodeList.getLength() > 0) {
				Node idChildNode = idChildNodeList.item(0);
				idChildNode.getAttributes().getNamedItem(EXTENSION).setNodeValue(ext);
				idChildNode.getAttributes().getNamedItem(ROOT).setNodeValue(root);
			}

			// Added logic to show qdm_variable in extension if clause is of qdm variable
			// type.
			if (FUNCTIONAL_OP.equals(relOpParentNode.getNodeName())) {
				excerptElement = generateExcerptEntryForFunctionalNode(relOpParentNode, lhsNode, hqmfXmlProcessor,
						clonedEntryNodeForElementRef);
			}
			Element temporallyRelatedInfoNode = null;
			if (!FULFILLS.equalsIgnoreCase(relOpNode.getAttributes().getNamedItem(TYPE).getNodeValue())) {
				temporallyRelatedInfoNode = createBaseTemporalNode(relOpNode, hqmfXmlProcessor);
				generateTemporalAttribute(hqmfXmlProcessor, lhsNode, temporallyRelatedInfoNode,
						clonedEntryNodeForElementRef, true);
			} else {
				temporallyRelatedInfoNode = hqmfXmlProcessor.getOriginalDoc().createElement(OUTBOUND_RELATIONSHIP);
				temporallyRelatedInfoNode.setAttribute(TYPE_CODE, "FLFS");
			}
			handleRelOpRHS(rhsNode, temporallyRelatedInfoNode, clauseName);
			Node firstChild = clonedEntryNodeForElementRef.getFirstChild();
			if (LOCAL_VARIABLE_NAME.equals(firstChild.getNodeName())) {
				firstChild = firstChild.getNextSibling();
			}
			NodeList outBoundList = ((Element) firstChild).getElementsByTagName(OUTBOUND_RELATIONSHIP);
			if (outBoundList != null && outBoundList.getLength() > 0) {
				Node outBound = outBoundList.item(0);
				firstChild.insertBefore(temporallyRelatedInfoNode, outBound);
			} else {
				firstChild.appendChild(temporallyRelatedInfoNode);
			}

			if (excerptElement != null) {
				// Comment comment = hqmfXmlProcessor.getOriginalDoc().createComment("excerpt
				// for
				// "+relOpParentNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue());
				// firstChild.appendChild(comment);
				firstChild.appendChild(excerptElement);
			}
			// create comment node
			// Comment comment = hqmfXmlProcessor.getOriginalDoc().createComment("entry for
			// "+relOpNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue());
			// dataCriteriaSectionElem.appendChild(comment);
			dataCriteriaSectionElem.appendChild(clonedEntryNodeForElementRef);
			return clonedEntryNodeForElementRef;
		}
		return null;
	}

	/**
	 * Handle rel op rhs.
	 *
	 * @param rhsNode
	 *            the rhs node
	 * @param temporallyRelatedInfoNode
	 *            the temporally related info node
	 * @param clauseName
	 *            the clause name
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private void handleRelOpRHS(Node rhsNode, Element temporallyRelatedInfoNode, String clauseName) throws XPathExpressionException {

		XmlProcessor hqmfXmlProcessor = measureExport.getHQMFXmlProcessor();
		String rhsName = rhsNode.getNodeName();
		Node dataCritSectionNode = hqmfXmlProcessor.findNode(temporallyRelatedInfoNode.getOwnerDocument(),
				"//dataCriteriaSection");

		if (ELEMENT_REF.equals(rhsName)) {
			Node entryNode = generateCritRefElementRef(temporallyRelatedInfoNode, rhsNode,
					measureExport.getHQMFXmlProcessor());
			generateTemporalAttribute(hqmfXmlProcessor, rhsNode, temporallyRelatedInfoNode, entryNode, false);
		} else if (SUB_TREE_REF.equals(rhsName)) {
			generateCritRefForNode(temporallyRelatedInfoNode, rhsNode);
		} else {
			switch (rhsName) {
			case SET_OP:
				generateCritRefSetOp(dataCritSectionNode, hqmfXmlProcessor, rhsNode, temporallyRelatedInfoNode,
						clauseName);
				break;
			case RELATIONAL_OP:
				generateRelOpHQMF(rhsNode, temporallyRelatedInfoNode, clauseName);
				Node lastChild = temporallyRelatedInfoNode.getLastChild();
				if (lastChild.getNodeName().equals(ENTRY)) {
					temporallyRelatedInfoNode.removeChild(lastChild);

					Node fChild = lastChild.getFirstChild();
					if (LOCAL_VARIABLE_NAME.equals(fChild.getNodeName())) {
						fChild = fChild.getNextSibling();
					}

					Node criteriaNode = fChild;
					// temporallyRelatedInfoNode.appendChild(criteriaNode);
					dataCritSectionNode.appendChild(lastChild);
					// create criteriaRef
					Element criteriaReference = hqmfXmlProcessor.getOriginalDoc().createElement(CRITERIA_REFERENCE);
					criteriaReference.setAttribute(CLASS_CODE,
							criteriaNode.getAttributes().getNamedItem(CLASS_CODE).getNodeValue());
					criteriaReference.setAttribute(MOOD_CODE,
							criteriaNode.getAttributes().getNamedItem(MOOD_CODE).getNodeValue());

					NodeList childNodeList = criteriaNode.getChildNodes();
					for (int i = 0; i < childNodeList.getLength(); i++) {
						Node childNode = childNodeList.item(i);
						if (childNode.getNodeName().equalsIgnoreCase(ID)) {
							Element id = hqmfXmlProcessor.getOriginalDoc().createElement(ID);
							id.setAttribute(ROOT, childNode.getAttributes().getNamedItem(ROOT).getNodeValue());
							id.setAttribute(EXTENSION,
									childNode.getAttributes().getNamedItem(EXTENSION).getNodeValue());
							criteriaReference.appendChild(id);
							temporallyRelatedInfoNode.appendChild(criteriaReference);
							break;
						}
					}

					NodeList childTemporalNodeList = ((Element) criteriaNode)
							.getElementsByTagName("temporallyRelatedInformation");
					if (childTemporalNodeList != null && childTemporalNodeList.getLength() > 0) {
						Node childTemporalNode = childTemporalNodeList.item(0);
						Node temporalInfoNode = childTemporalNode.getFirstChild();
						// find sourceAttribute
						NodeList childs = temporalInfoNode.getChildNodes();
						for (int c = 0; c < childs.getLength(); c++) {
							Node child = childs.item(c);
							String childName = child.getNodeName();
							if ("qdm:sourceAttribute".equals(childName)) {
								Node cloneAttNode = child.cloneNode(true);
								temporallyRelatedInfoNode.getFirstChild().appendChild(cloneAttNode);
								hqmfXmlProcessor.getOriginalDoc().renameNode(cloneAttNode, "", "qdm:targetAttribute");
								break;
							}
						}
					}
				}
				break;
			case FUNCTIONAL_OP:
				Node entryNode = generateFunctionalOpHQMF(rhsNode, (Element) dataCritSectionNode, clauseName);
				if (entryNode != null && entryNode.getNodeName().equals(ENTRY)) {
					Node fChild = entryNode.getFirstChild();
					if (LOCAL_VARIABLE_NAME.equals(fChild.getNodeName())) {
						fChild = fChild.getNextSibling();
					}
					// create criteriaRef
					Element criteriaReference = hqmfXmlProcessor.getOriginalDoc().createElement(CRITERIA_REFERENCE);
					criteriaReference.setAttribute(CLASS_CODE,
							fChild.getAttributes().getNamedItem(CLASS_CODE).getNodeValue());
					criteriaReference.setAttribute(MOOD_CODE,
							fChild.getAttributes().getNamedItem(MOOD_CODE).getNodeValue());
					NodeList childNodeList = fChild.getChildNodes();
					for (int i = 0; i < childNodeList.getLength(); i++) {
						Node childNode = childNodeList.item(i);
						if (childNode.getNodeName().equalsIgnoreCase(ID)) {
							Element id = hqmfXmlProcessor.getOriginalDoc().createElement(ID);
							id.setAttribute(ROOT, childNode.getAttributes().getNamedItem(ROOT).getNodeValue());
							id.setAttribute(EXTENSION,
									childNode.getAttributes().getNamedItem(EXTENSION).getNodeValue());
							criteriaReference.appendChild(id);
							temporallyRelatedInfoNode.appendChild(criteriaReference);
							break;
						}
					}
				}
				break;
			default:
				// Dont do anything
				break;
			}
		}
	}

	/**
	 * Generate temporal attribute.
	 *
	 * @param hqmfXmlProcessor
	 *            the hqmf xml processor
	 * @param rhsNode
	 *            the rhs node
	 * @param temporallyRelatedInfoNode
	 *            the temporally related info node
	 * @param entryNode
	 *            the entry node
	 * @param isSource
	 *            the is source
	 * @return the node
	 */
	private Node generateTemporalAttribute(XmlProcessor hqmfXmlProcessor, Node rhsNode,
			Element temporallyRelatedInfoNode, Node entryNode, boolean isSource) {
		if (entryNode != null) {
			Element entryElement = (Element) entryNode;

			if (!rhsNode.hasChildNodes()) {
				return null;
			} else {
				Node child = rhsNode.getFirstChild();
				if (!"attribute".equals(child.getNodeName())) {
					return null;
				}

				String value = child.getAttributes().getNamedItem(NAME).getNodeValue();
				List<String> validAttribNames = new ArrayList<>();
				validAttribNames.add("incision datetime");
				validAttribNames.add("facility location arrival datetime");
				validAttribNames.add("facility location departure datetime");
				validAttribNames.add("recorded datetime");
				validAttribNames.add("signed datetime");
				validAttribNames.add("start datetime");
				validAttribNames.add("stop datetime");
				if (!validAttribNames.contains(value)) {
					return null;
				}
				if ("start datetime".equals(value) || "stop datetime".equals(value)) {
					String dataType = rhsNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
					if (!dataType.endsWith("Order")) {
						return null;
					}
				}

				// create sourceAttribute/targetAttribute
				String attribName = "qdm:sourceAttribute";
				if (!isSource) {
					attribName = "qdm:targetAttribute";
				}
				Element attribute = hqmfXmlProcessor.getOriginalDoc().createElement(attribName);
				attribute.setAttribute(NAME, value);
				String boundValue = "effectiveTime.low";

				if ("incision datetime".equals(value)) {
					NodeList nodeList = entryElement.getElementsByTagName(OUTBOUND_RELATIONSHIP);
					if (nodeList != null && nodeList.getLength() > 0) {
						// Always get the last outBoundRelationShip tag, because this is the one
						// which will represent the
						Node outBoundNode = nodeList.item(nodeList.getLength() - 1);
						Node criteriaNode = outBoundNode.getFirstChild();

						NodeList idNodeList = ((Element) criteriaNode).getElementsByTagName(ID);
						if (idNodeList != null && idNodeList.getLength() > 0) {
							Node idNode = idNodeList.item(0);
							Element qdmId = hqmfXmlProcessor.getOriginalDoc().createElement("qdm:id");
							qdmId.setAttribute(ROOT, idNode.getAttributes().getNamedItem(ROOT).getNodeValue());
							qdmId.setAttribute(EXTENSION,
									idNode.getAttributes().getNamedItem(EXTENSION).getNodeValue());
							attribute.appendChild(qdmId);
						}
					}
				} else {
					NodeList nodeList = entryElement.getElementsByTagName(PARTICIPATION);
					if (nodeList != null && nodeList.getLength() > 0) {
						// Always get the last outBoundRelationShip tag, because this is the one
						// which will represent the
						Node participationNode = nodeList.item(nodeList.getLength() - 1);
						Node roleNode = ((Element) participationNode).getElementsByTagName(ROLE).item(0);
						NodeList idNodeList = ((Element) roleNode).getElementsByTagName(ID);
						if (idNodeList != null && idNodeList.getLength() > 0) {
							Node idNode = idNodeList.item(0);
							Node itemNode = idNode.getFirstChild();
							Element qdmId = hqmfXmlProcessor.getOriginalDoc().createElement("qdm:id");
							qdmId.setAttribute(ROOT, itemNode.getAttributes().getNamedItem(ROOT).getNodeValue());
							qdmId.setAttribute(EXTENSION,
									itemNode.getAttributes().getNamedItem(EXTENSION).getNodeValue());
							attribute.appendChild(qdmId);

							if ("facility location departure datetime".equals(value) || "stop datetime".equals(value)
									|| "signed datetime".equals(value) || "recorded datetime".equals(value)) {
								boundValue = "effectiveTime.high";
							}
						}
					}
				}
				attribute.setAttribute("bound", boundValue);
				Node temporalInformation = temporallyRelatedInfoNode.getFirstChild();
				if (temporallyRelatedInfoNode.getElementsByTagName("qdm:delta").item(0) != null) {
					Node qdmDeltaNode = temporallyRelatedInfoNode.getElementsByTagName("qdm:delta").item(0);
					temporalInformation.insertBefore(attribute, qdmDeltaNode);
				} else {
					temporalInformation.appendChild(attribute);
				}

				return attribute;
			}
		}
		return null;
	}

	/**
	 * Generate Excerpt for Functional Op used with timing/Relationship.
	 *
	 * @param functionalOpNode
	 *            the functional op node
	 * @param lhsNode
	 *            the lhs node
	 * @param hqmfXmlProcessor
	 *            the hqmf xml processor
	 * @param clonedNodeToAppendExcerpt
	 *            the cloned node to append excerpt
	 * @return the element
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private Element generateExcerptEntryForFunctionalNode(Node functionalOpNode, Node lhsNode,
			XmlProcessor hqmfXmlProcessor, Node clonedNodeToAppendExcerpt) throws XPathExpressionException {
		Element excerptElement = hqmfXmlProcessor.getOriginalDoc().createElement(EXCERPT);
		String functionalOpName = functionalOpNode.getAttributes().getNamedItem(TYPE).getNodeValue();
		Element criteriaElement = null;
		if (FUNCTIONAL_OPS_NON_SUBSET.containsKey(functionalOpName)) {
			Element sequenceElement = hqmfXmlProcessor.getOriginalDoc().createElement(SEQUENCE_NUMBER);
			sequenceElement.setAttribute(VALUE, FUNCTIONAL_OPS_NON_SUBSET.get(functionalOpName.toUpperCase()));
			excerptElement.appendChild(sequenceElement);
			if (clonedNodeToAppendExcerpt != null) {
				if (clonedNodeToAppendExcerpt.getNodeName().contains(GROUPER)) {
					criteriaElement = generateCriteriaElementForSetOpExcerpt(hqmfXmlProcessor,
							clonedNodeToAppendExcerpt);
					excerptElement.appendChild(criteriaElement);
				} else {
					NodeList entryChildNodes = clonedNodeToAppendExcerpt.getChildNodes();
					criteriaElement = generateCriteriaElementForExcerpt(hqmfXmlProcessor, entryChildNodes);
					excerptElement.appendChild(criteriaElement);
				}
			}
		} else if (FUNCTIONAL_OPS_SUBSET.containsKey(functionalOpName)) {
			NamedNodeMap attributeMap = functionalOpNode.getAttributes();
			if (clonedNodeToAppendExcerpt.getNodeName().contains(GROUPER)) {
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
					if (attributeMap.getNamedItem(OPERATOR_TYPE) != null && lhsNode != null) {
						String lhsNodeType = lhsNode.getNodeName();
						if (ELEMENT_REF.equalsIgnoreCase(lhsNodeType)) {
							String qdmUUID = lhsNode.getAttributes().getNamedItem(ID).getNodeValue();
							String xPath = "/measure/elementLookUp/qdm[@uuid ='" + qdmUUID + "']";
							Node node = measureExport.getSimpleXMLProcessor()
									.findNode(measureExport.getSimpleXMLProcessor().getOriginalDoc(), xPath);
							if (node != null && lhsNode.hasChildNodes()) {
								Node qdmNode = node.cloneNode(true);
								Node attributeNode = lhsNode.getFirstChild().cloneNode(true);
								attributeNode.setUserData(ATTRIBUTE_NAME,
										attributeNode.getAttributes().getNamedItem(NAME).getNodeValue(), null);
								attributeNode.setUserData(ATTRIBUTE_MODE,
										attributeMap.getNamedItem(OPERATOR_TYPE).getNodeValue(), null);
								attributeNode.setUserData(ATTRIBUTE_UUID,
										attributeNode.getAttributes().getNamedItem(ATTR_UUID).getNodeValue(), null);
								Element attributeElement = (Element) attributeNode;

								attributeElement.setAttribute(MODE,
										attributeMap.getNamedItem(OPERATOR_TYPE).getNodeValue());
								if (attributeElement.getAttributes().getNamedItem(ATTR_DATE) != null) {
									attributeNode.setUserData(ATTRIBUTE_DATE,
											attributeMap.getNamedItem(QUANTITY).getNodeValue(), null);
								} else {
									attributeElement.setAttribute(COMPARISON_VALUE,
											attributeMap.getNamedItem(QUANTITY).getNodeValue());
								}
								if (attributeMap.getNamedItem(UNIT) != null) {
									attributeElement.setAttribute(UNIT, attributeMap.getNamedItem(UNIT).getNodeValue());
								} else {
									if (attributeElement.getAttributes().getNamedItem(UNIT) != null) {
										attributeElement.removeAttribute(UNIT);
									}
								}
								attributeNode = attributeElement;

								// HQMFDataCriteriaElementGenerator hqmfDataCriteriaElementGenerator = new
								// HQMFDataCriteriaElementGenerator();
								// hqmfDataCriteriaElementGenerator.generateAttributeTagForFunctionalOp(measureExport,qdmNode,
								// criteriaElement, attributeNode);
								HQMFAttributeGenerator attributeGenerator = new HQMFAttributeGenerator();
								attributeGenerator.generateAttributeTagForFunctionalOp(measureExport, qdmNode,
										criteriaElement, attributeNode);
							}
						}
					}
					Element qdmSubSetElement = hqmfXmlProcessor.getOriginalDoc().createElement("qdm:subsetCode");
					qdmSubSetElement.setAttribute(CODE, FUNCTIONAL_OPS_SUBSET.get(functionalOpName.toUpperCase()));

					if ("sum".equalsIgnoreCase(functionalOpName)) {
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
	 * @param parentNode
	 *            the parent node
	 * @return the node
	 */
	protected Node checkIfParentSubTree(Node parentNode) {
		Node returnNode = null;
		if (parentNode != null) {
			String parentName = parentNode.getNodeName();
			if (SUB_TREE.equals(parentName)) {
				returnNode = parentNode;
			} else {
				returnNode = checkIfParentSubTree(parentNode.getParentNode());
			}
		}

		return returnNode;
	}

	/**
	 * Generates RepeatNumber tags for Count Function.
	 * 
	 * @param hqmfXmlProcessor
	 *            - XmlProcessor.
	 * @param attributeMap
	 *            - NamedNodeMap.
	 * @param criteriaElement
	 *            - Element.
	 */
	private void createRepeatNumberTagForCountFuncttion(XmlProcessor hqmfXmlProcessor, NamedNodeMap attributeMap,
			Element criteriaElement) {
		Element repeatNumberElement = hqmfXmlProcessor.getOriginalDoc().createElement("repeatNumber");
		Element lowNode = hqmfXmlProcessor.getOriginalDoc().createElement("low");
		Element highNode = hqmfXmlProcessor.getOriginalDoc().createElement("high");
		if (attributeMap.getNamedItem(OPERATOR_TYPE) != null) {
			String operatorType = attributeMap.getNamedItem(OPERATOR_TYPE).getNodeValue();
			String quantity = attributeMap.getNamedItem(QUANTITY).getNodeValue();
			if (operatorType.startsWith("Greater Than")) {
				lowNode.setAttribute(VALUE, quantity);
				highNode.setAttribute(NULL_FLAVOR, "PINF");
				if ("Greater Than or Equal To".equals(operatorType)) {
					repeatNumberElement.setAttribute("lowClosed", TRUE);
				}
			} else if ("Equal To".equals(operatorType)) {
				repeatNumberElement.setAttribute("lowClosed", TRUE);
				repeatNumberElement.setAttribute("highClosed", TRUE);
				lowNode.setAttribute(VALUE, quantity);
				highNode.setAttribute(VALUE, quantity);
			} else if (operatorType.startsWith("Less Than")) {
				repeatNumberElement.setAttribute("lowClosed", TRUE);
				lowNode.setAttribute(VALUE, "0");
				highNode.setAttribute(VALUE, quantity);
				if ("Less Than or Equal To".equals(operatorType)) {
					repeatNumberElement.setAttribute("highClosed", TRUE);
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
	 * @param hqmfXmlProcessor
	 *            the hqmf xml processor
	 * @param entryChildNodes
	 *            the entry child nodes
	 * @return the element
	 */
	private Element generateCriteriaElementForExcerpt(XmlProcessor hqmfXmlProcessor, NodeList entryChildNodes) {
		Element criteriaElement = null;
		for (int i = 0; i < entryChildNodes.getLength(); i++) {
			Node childNode = entryChildNodes.item(i);
			String childNodeName = childNode.getNodeName();
			if (childNodeName.contains("Criteria")) {
				criteriaElement = hqmfXmlProcessor.getOriginalDoc().createElement(childNodeName);
				criteriaElement.setAttribute(CLASS_CODE,
						childNode.getAttributes().getNamedItem(CLASS_CODE).getNodeValue());
				criteriaElement.setAttribute(MOOD_CODE,
						childNode.getAttributes().getNamedItem(MOOD_CODE).getNodeValue());
				NodeList criteriaChildNodes = childNode.getChildNodes();
				for (int j = 0; j < criteriaChildNodes.getLength(); j++) {
					Node criteriaChildNode = criteriaChildNodes.item(j);
					if (ID.equalsIgnoreCase(criteriaChildNode.getNodeName())) {
						Element idElement = hqmfXmlProcessor.getOriginalDoc().createElement(ID);
						idElement.setAttribute(ROOT,
								criteriaChildNode.getAttributes().getNamedItem(ROOT).getNodeValue());
						idElement.setAttribute(EXTENSION,
								criteriaChildNode.getAttributes().getNamedItem(EXTENSION).getNodeValue());
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
	 * @param hqmfXmlProcessor
	 *            the hqmf xml processor
	 * @param clonedNodeToAppendExcerpt
	 *            the cloned node to append excerpt
	 * @return the element
	 */
	private Element generateCriteriaElementForSetOpExcerpt(XmlProcessor hqmfXmlProcessor,
			Node clonedNodeToAppendExcerpt) {
		Element criteriaElement = null;
		for (int i = 0; i < clonedNodeToAppendExcerpt.getChildNodes().getLength(); i++) {
			Node childNode = clonedNodeToAppendExcerpt.getChildNodes().item(i);
			if (ID.equalsIgnoreCase(childNode.getNodeName())) {
				Node criteriaNode = generateEmptyGrouper(hqmfXmlProcessor,
						childNode.getAttributes().getNamedItem(ROOT).getNodeValue(),
						childNode.getAttributes().getNamedItem(EXTENSION).getNodeValue());
				criteriaElement = (Element) criteriaNode;
				break;
			}
		}
		return criteriaElement;
	}

	/**
	 * Creates the base temporal node.
	 *
	 * @param relOpNode
	 *            the rel op node
	 * @param hqmfXmlProcessor
	 *            the hqmf xml processor
	 * @return the element
	 */
	private Element createBaseTemporalNode(Node relOpNode, XmlProcessor hqmfXmlProcessor) {

		NamedNodeMap attribMap = relOpNode.getAttributes();
		Element temporallyRelatedInfoNode = hqmfXmlProcessor.getOriginalDoc()
				.createElement("temporallyRelatedInformation");
		temporallyRelatedInfoNode.setAttribute(TYPE_CODE, attribMap.getNamedItem(TYPE).getNodeValue().toUpperCase());

		Element temporalInfoNode = hqmfXmlProcessor.getOriginalDoc().createElement("qdm:temporalInformation");
		String precisionUnit = "min"; // use min by default

		if (attribMap.getNamedItem(OPERATOR_TYPE) != null) {
			String operatorType = attribMap.getNamedItem(OPERATOR_TYPE).getNodeValue();
			String quantity = attribMap.getNamedItem(QUANTITY).getNodeValue();
			String unit = attribMap.getNamedItem(UNIT).getNodeValue();

			if ("seconds".equals(unit)) {
				precisionUnit = "s";
				unit = "s";
			} else if ("hours".equals(unit)) {
				unit = "h";
			} else if ("minutes".equals(unit)) {
				unit = "min";
			} else {
				precisionUnit = "d";
				if ("days".equals(unit)) {
					unit = "d";
				} else if ("weeks".equals(unit)) {
					unit = "wk";
				} else if ("months".equals(unit)) {
					unit = "mo";
				} else if ("years".equals(unit)) {
					unit = "a";
				}
			}

			Element deltaNode = hqmfXmlProcessor.getOriginalDoc().createElement("qdm:delta");
			Element lowNode = hqmfXmlProcessor.getOriginalDoc().createElement("low");
			lowNode.setAttribute(UNIT, unit);

			Element highNode = hqmfXmlProcessor.getOriginalDoc().createElement("high");
			highNode.setAttribute(UNIT, unit);

			if (operatorType.startsWith("Greater Than")) {
				lowNode.setAttribute(VALUE, quantity);
				highNode.removeAttribute(UNIT);
				highNode.setAttribute(NULL_FLAVOR, "PINF");
				if ("Greater Than or Equal To".equals(operatorType)) {
					deltaNode.setAttribute("lowClosed", TRUE);
				}
			} else if ("Equal To".equals(operatorType)) {
				deltaNode.setAttribute("lowClosed", TRUE);
				deltaNode.setAttribute("highClosed", TRUE);
				lowNode.setAttribute(VALUE, quantity);
				highNode.setAttribute(VALUE, quantity);
			} else if (operatorType.startsWith("Less Than")) {
				deltaNode.setAttribute("lowClosed", TRUE);
				lowNode.setAttribute(VALUE, "0");
				highNode.setAttribute(VALUE, quantity);
				if ("Less Than or Equal To".equals(operatorType)) {
					deltaNode.setAttribute("highClosed", TRUE);
				}
			}
			deltaNode.appendChild(lowNode);
			deltaNode.appendChild(highNode);
			temporalInfoNode.appendChild(deltaNode);
		}
		temporalInfoNode.setAttribute("precisionUnit", precisionUnit);
		temporallyRelatedInfoNode.appendChild(temporalInfoNode);
		return temporallyRelatedInfoNode;
	}

	/**
	 * Generate crit ref rel op.
	 *
	 * @param parentNode
	 *            the parent node
	 * @param hqmfXmlProcessor
	 *            the hqmf xml processor
	 * @param childNode
	 *            the child node
	 * @param outboundRelElem
	 *            the outbound rel elem
	 * @param clauseName
	 *            the clause name
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private void generateCritRefRelOp(Node parentNode, XmlProcessor hqmfXmlProcessor, Node childNode,
			Node outboundRelElem, String clauseName) throws XPathExpressionException {
		Node relOpEntryNode = generateRelOpHQMF(childNode, parentNode, clauseName);

		if (relOpEntryNode != null) {
			Node idNode = getTagFromEntry(relOpEntryNode, ID);
			// Node critNode = relOpEntryNode.getFirstChild();
			// NodeList nodeList = ((Element)critNode).getElementsByTagName(ID);
			// if(nodeList != null && nodeList.getLength() > 0){
			if (idNode != null) {
				// Node idNode = nodeList.item(0);
				NamedNodeMap idAttribMap = idNode.getAttributes();
				String idRoot = idAttribMap.getNamedItem(ROOT).getNodeValue();
				String idExt = idAttribMap.getNamedItem(EXTENSION).getNodeValue();

				Node parent = idNode.getParentNode();

				NamedNodeMap attribMap = parent.getAttributes();
				String classCode = attribMap.getNamedItem(CLASS_CODE).getNodeValue();
				String moodCode = attribMap.getNamedItem(MOOD_CODE).getNodeValue();

				// create criteriaRef
				Element criteriaReference = hqmfXmlProcessor.getOriginalDoc().createElement(CRITERIA_REFERENCE);
				criteriaReference.setAttribute(CLASS_CODE, classCode);
				criteriaReference.setAttribute(MOOD_CODE, moodCode);

				Element id = hqmfXmlProcessor.getOriginalDoc().createElement(ID);
				id.setAttribute(ROOT, idRoot);
				id.setAttribute(EXTENSION, idExt);

				criteriaReference.appendChild(id);
				outboundRelElem.appendChild(criteriaReference);
			}
		}
	}

	/**
	 * Generate crit ref set op.
	 *
	 * @param parentNode
	 *            the parent node
	 * @param hqmfXmlProcessor
	 *            the hqmf xml processor
	 * @param childNode
	 *            the child node
	 * @param outboundRelElem
	 *            the outbound rel elem
	 * @param clauseName
	 *            the clause name
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private void generateCritRefSetOp(Node parentNode, XmlProcessor hqmfXmlProcessor, Node childNode,
			Node outboundRelElem, String clauseName) throws XPathExpressionException {

		Node setOpEntry = generateSetOpHQMF(childNode, parentNode, clauseName);
		NodeList childList = setOpEntry.getChildNodes();
		for (int j = 0; j < childList.getLength(); j++) {
			Node child = childList.item(j);
			if (GROUPER_CRITERIA.equals(child.getNodeName())) {
				NodeList idChildList = ((Element) child).getElementsByTagName(ID);
				if (idChildList.getLength() > 0) {
					Node idChild = idChildList.item(0);
					NamedNodeMap attribMap = idChild.getAttributes();
					String idRoot = attribMap.getNamedItem(ROOT).getNodeValue();
					String idExt = attribMap.getNamedItem(EXTENSION).getNodeValue();

					// create criteriaRef
					Element criteriaReference = hqmfXmlProcessor.getOriginalDoc().createElement(CRITERIA_REFERENCE);
					criteriaReference.setAttribute(CLASS_CODE, "GROUPER");
					criteriaReference.setAttribute(MOOD_CODE, "EVN");

					Element id = hqmfXmlProcessor.getOriginalDoc().createElement(ID);
					id.setAttribute(ROOT, idRoot);
					id.setAttribute(EXTENSION, idExt);

					criteriaReference.appendChild(id);
					outboundRelElem.appendChild(criteriaReference);
				}
			}
		}
	}

	/**
	 * Generate crit ref for node.
	 *
	 * @param outboundRelElem
	 *            the outbound rel elem
	 * @param childNode
	 *            the child node
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private void generateCritRefForNode(Node outboundRelElem, Node childNode)
			throws XPathExpressionException {
		XmlProcessor hqmfXmlProcessor = measureExport.getHQMFXmlProcessor();
		String childName = childNode.getNodeName();

		switch (childName) {
		case ELEMENT_REF:
			generateCritRefElementRef(outboundRelElem, childNode, hqmfXmlProcessor);
			break;
		case SUB_TREE_REF:
			generateCritRefCQLDefine(outboundRelElem, childNode, hqmfXmlProcessor, true);
			break;

		default:
			break;
		}

	}

	/**
	 * This method will basically create a <criteriaReference> with
	 * classCode='GROUPER' and moodCode='EVN' and have the <id> tag pointing to the
	 * <grouperCriteria> for the referenced subTree/clause.
	 *
	 * @param outboundRelElem
	 *            the outbound rel elem
	 * @param subTreeRefNode
	 *            the sub tree ref node
	 * @param hqmfXmlProcessor
	 *            the hqmf xml processor
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	protected void generateCritRefCQLDefine(Node outboundRelElem, Node subTreeRefNode, XmlProcessor hqmfXmlProcessor)
			throws XPathExpressionException {
		generateCritRefCQLDefine(outboundRelElem, subTreeRefNode, hqmfXmlProcessor, false);
	}

	/**
	 * This method will basically create a <criteriaReference> with
	 * classCode='GROUPER' and moodCode='EVN' and have the <id> tag pointing to the
	 * <grouperCriteria> for the referenced subTree/clause.
	 *
	 * @param outboundRelElem
	 *            the outbound rel elem
	 * @param cqlDefineNode
	 *            the sub tree ref node
	 * @param hqmfXmlProcessor
	 *            the hqmf xml processor
	 * @param checkExisting
	 *            check in the map if already existing
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */

	protected void generateCritRefCQLDefine(Node outboundRelElem, Node cqlDefineNode, XmlProcessor hqmfXmlProcessor,
			boolean checkExisting) throws XPathExpressionException {

		String cqlDefUUID = cqlDefineNode.getAttributes().getNamedItem(UUID).getNodeValue();

		String xpath = "/measure/cqlLookUp//definition[@id='" + cqlDefUUID + "']";
		Node definitionNode = measureExport.getSimpleXMLProcessor()
				.findNode(measureExport.getSimpleXMLProcessor().getOriginalDoc(), xpath);
		if (definitionNode != null) {
			String defineName = definitionNode.getAttributes().getNamedItem("name").getNodeValue();
			Node cqlUUIDNode = measureExport.getSimpleXMLProcessor().findNode(
					measureExport.getSimpleXMLProcessor().getOriginalDoc(), "/measure/measureDetails/cqlUUID");
			Node cqlLibraryNode = measureExport.getSimpleXMLProcessor()
					.findNode(measureExport.getSimpleXMLProcessor().getOriginalDoc(), "/measure/cqlLookUp/library");
			if (cqlUUIDNode != null && cqlLibraryNode != null) {
				String uuid = cqlUUIDNode.getTextContent();
				String libraryName = cqlLibraryNode.getTextContent();
				String ext = libraryName + ".\"" + defineName + "\"";
				// create criteriaRef
				Element criteriaReference = hqmfXmlProcessor.getOriginalDoc().createElement(CRITERIA_REFERENCE);
				criteriaReference.setAttribute(CLASS_CODE, "OBS");
				criteriaReference.setAttribute(MOOD_CODE, "EVN");

				Element id = hqmfXmlProcessor.getOriginalDoc().createElement(ID);
				id.setAttribute(ROOT, uuid);
				id.setAttribute(EXTENSION, ext);

				criteriaReference.appendChild(id);
				outboundRelElem.appendChild(criteriaReference);
			}
		}
	}

	/**
	 * Generate crit ref element ref.
	 *
	 * @param outboundRelElem
	 *            the outbound rel elem
	 * @param elementRefNode
	 *            the element ref node
	 * @param hqmfXmlProcessor
	 *            the hqmf xml processor
	 * @return the node
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private Node generateCritRefElementRef(Node outboundRelElem, Node elementRefNode, XmlProcessor hqmfXmlProcessor)
			throws XPathExpressionException {
		String ext = getElementRefExt(elementRefNode, measureExport.getSimpleXMLProcessor());
		String root = elementRefNode.getAttributes().getNamedItem(ID).getNodeValue();
		Node idNodeQDM = hqmfXmlProcessor.findNode(hqmfXmlProcessor.getOriginalDoc(),
				"//entry/*/id[@root=\"" + root + "\"][@extension=\"" + ext + "\"]");
		if (idNodeQDM != null) {
			Node parent = idNodeQDM.getParentNode();
			if (parent != null) {
				NamedNodeMap attribMap = parent.getAttributes();
				String classCode = attribMap.getNamedItem(CLASS_CODE).getNodeValue();
				String moodCode = attribMap.getNamedItem(MOOD_CODE).getNodeValue();

				// create criteriaRef
				Element criteriaReference = hqmfXmlProcessor.getOriginalDoc().createElement(CRITERIA_REFERENCE);
				criteriaReference.setAttribute(CLASS_CODE, classCode);
				criteriaReference.setAttribute(MOOD_CODE, moodCode);

				Element id = hqmfXmlProcessor.getOriginalDoc().createElement(ID);
				id.setAttribute(ROOT, root);
				id.setAttribute(EXTENSION, ext);

				criteriaReference.appendChild(id);
				outboundRelElem.appendChild(criteriaReference);
				// return <entry> element
				return parent.getParentNode();
			}
		} else {
			// check if this is a measurement period
			String displayName = elementRefNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
			if ("Measurement Period : Timing Element".equals(displayName)) {
				// create criteriaRef
				Element criteriaReference = hqmfXmlProcessor.getOriginalDoc().createElement(CRITERIA_REFERENCE);
				criteriaReference.setAttribute(CLASS_CODE, "OBS");
				criteriaReference.setAttribute(MOOD_CODE, "EVN");

				Element id = hqmfXmlProcessor.getOriginalDoc().createElement(ID);
				id.setAttribute(ROOT, elementRefNode.getAttributes().getNamedItem(ID).getNodeValue());
				id.setAttribute(EXTENSION, "measureperiod");

				criteriaReference.appendChild(id);
				outboundRelElem.appendChild(criteriaReference);
			}
		}
		return null;
	}

	/**
	 * Generate item count element ref.
	 *
	 * @param me
	 *            the me
	 * @param populationTypeCriteriaElement
	 *            the population type criteria element
	 * @param elementRefNode
	 *            the element ref node
	 * @param hqmfXmlProcessor
	 *            the hqmf xml processor
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	public void generateItemCountElementRef(MeasureExport me, Element populationTypeCriteriaElement,
			Node elementRefNode, XmlProcessor hqmfXmlProcessor) throws XPathExpressionException {
		String ext = getElementRefExt(elementRefNode, me.getSimpleXMLProcessor());
		String root = elementRefNode.getAttributes().getNamedItem(ID).getNodeValue();
		Node idNodeQDM = hqmfXmlProcessor.findNode(hqmfXmlProcessor.getOriginalDoc(),
				"//entry/*/id[@root='" + root + "'][@extension=\"" + ext + "\"]");
		if (idNodeQDM != null) {
			Node parent = idNodeQDM.getParentNode();
			if (parent != null) {
				String classCode = parent.getAttributes().getNamedItem("classCode").getNodeValue();
				String moodCode = parent.getAttributes().getNamedItem("moodCode").getNodeValue();
				// item count Criteria Ref for Measure Observations
				if ("measureObservationDefinition".equals(populationTypeCriteriaElement.getNodeName())) {
					Element componentOfElem = hqmfXmlProcessor.getOriginalDoc().createElement("componentOf");
					componentOfElem.setAttribute(TYPE_CODE, "COMP");
					Element criteriaRef = hqmfXmlProcessor.getOriginalDoc().createElement(CRITERIA_REFERENCE);
					criteriaRef.setAttribute(CLASS_CODE, classCode);
					criteriaRef.setAttribute(MOOD_CODE, moodCode);
					Element idElement = hqmfXmlProcessor.getOriginalDoc().createElement(ID);
					idElement.setAttribute(ROOT, root);
					idElement.setAttribute(EXTENSION, ext);
					criteriaRef.appendChild(idElement);
					componentOfElem.appendChild(criteriaRef);
					Comment comment = hqmfXmlProcessor.getOriginalDoc().createComment("Item Count ");
					populationTypeCriteriaElement.appendChild(comment);
					populationTypeCriteriaElement.appendChild(componentOfElem);
				} else { // item count Criteria Ref for Populations
					// create component for ItemCount ElmentRef
					Element componentElem = hqmfXmlProcessor.getOriginalDoc().createElement("component");
					componentElem.setAttribute(TYPE_CODE, "COMP");
					Element measureAttrElem = hqmfXmlProcessor.getOriginalDoc().createElement("measureAttribute");
					componentElem.appendChild(measureAttrElem);
					Element codeElem = hqmfXmlProcessor.getOriginalDoc().createElement("code");
					codeElem.setAttribute(CODE, "ITMCNT");
					codeElem.setAttribute(CODE_SYSTEM, "2.16.840.1.113883.5.4");
					codeElem.setAttribute(CODE_SYSTEM_NAME, "HL7 Observation Value");
					Element displayNameElem = hqmfXmlProcessor.getOriginalDoc().createElement(DISPLAY_NAME);
					displayNameElem.setAttribute(VALUE, "Items to count");
					codeElem.appendChild(displayNameElem);
					Element valueElem = hqmfXmlProcessor.getOriginalDoc().createElement(VALUE);
					valueElem.setAttribute("xsi:type", "II");
					valueElem.setAttribute(ROOT, root);
					valueElem.setAttribute(EXTENSION, ext);
					measureAttrElem.appendChild(codeElem);
					measureAttrElem.appendChild(valueElem);
					populationTypeCriteriaElement.appendChild(componentElem);
				}
			}
		}
	}

	/**
	 * Gets the element ref ext.
	 *
	 * @param elementRefNode
	 *            the element ref node
	 * @param simpleXmlProcessor
	 *            the simple xml processor
	 * @return the element ref ext
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private String getElementRefExt(Node elementRefNode, XmlProcessor simpleXmlProcessor)
			throws XPathExpressionException {
		String extension = "";
		if (elementRefNode.hasChildNodes()) {
			Node childNode = elementRefNode.getFirstChild();
			if ("attribute".equals(childNode.getNodeName())) {
				extension = childNode.getAttributes().getNamedItem(ATTR_UUID).getNodeValue();
			}
		} else {
			String id = elementRefNode.getAttributes().getNamedItem(ID).getNodeValue();
			Node qdmNode = simpleXmlProcessor.findNode(simpleXmlProcessor.getOriginalDoc(),
					"/measure/elementLookUp/qdm[@uuid='" + id + "']");
			if (qdmNode != null) {
				String dataType = qdmNode.getAttributes().getNamedItem(DATATYPE).getNodeValue();
				String qdmName = qdmNode.getAttributes().getNamedItem(NAME).getNodeValue();
				extension = qdmName + "_" + dataType;
				if (qdmNode.getAttributes().getNamedItem(INSTANCE) != null) {
					extension = qdmNode.getAttributes().getNamedItem(INSTANCE).getNodeValue() + "_" + extension;
				}
			}
		}
		return StringUtils.deleteWhitespace(extension);
	}

	/**
	 * Generate empty grouper.
	 *
	 * @param hqmfXmlProcessor
	 *            the hqmf xml processor
	 * @param root
	 *            the root
	 * @param ext
	 *            the ext
	 * @return the node
	 */
	private Node generateEmptyGrouper(XmlProcessor hqmfXmlProcessor, String root, String ext) {
		Element grouperElem = hqmfXmlProcessor.getOriginalDoc().createElement(GROUPER_CRITERIA);
		grouperElem.setAttribute(CLASS_CODE, "GROUPER");
		grouperElem.setAttribute(MOOD_CODE, "EVN");

		Element idElem = hqmfXmlProcessor.getOriginalDoc().createElement(ID);
		idElem.setAttribute(ROOT, root);
		idElem.setAttribute(EXTENSION, StringUtils.deleteWhitespace(ext));

		grouperElem.appendChild(idElem);

		return grouperElem;
	}

	/**
	 * Generate empty outbound elem.
	 *
	 * @param hqmfXmlProcessor
	 *            the hqmf xml processor
	 * @return the element
	 */
	private Element generateEmptyOutboundElem(XmlProcessor hqmfXmlProcessor) {
		Element outboundRelElem = hqmfXmlProcessor.getOriginalDoc().createElement(OUTBOUND_RELATIONSHIP);
		outboundRelElem.setAttribute(TYPE_CODE, "COMP");
		return outboundRelElem;
	}

	/**
	 * Gets the tag from entry.
	 *
	 * @param entryElem
	 *            the entry elem
	 * @param tagName
	 *            the tag name
	 * @return the tag from entry
	 */
	private Node getTagFromEntry(Node entryElem, String tagName) {

		String entryElemName = entryElem.getNodeName();
		if (ENTRY.equals(entryElemName)) {
			Node firstChild = entryElem.getFirstChild();
			if (LOCAL_VARIABLE_NAME.equals(firstChild.getNodeName())) {
				NodeList nodeList = ((Element) firstChild.getNextSibling()).getElementsByTagName(tagName);
				if (nodeList != null && nodeList.getLength() > 0) {
					return nodeList.item(0);
				}
			} else {
				NodeList nodeList = ((Element) firstChild).getElementsByTagName(tagName);
				if (nodeList != null && nodeList.getLength() > 0) {
					return nodeList.item(0);
				}
			}
		}
		return null;
	}

	/**
	 * Check If the parentNode is a SUB_TREE node. Or else, if parent is a
	 * 'functionalOp' then recursively check if the parentNode's parent is a
	 * 'subTree'. If yes, then return true.
	 *
	 * @param parentNode
	 *            the parent node
	 * @return boolean
	 */
	private Node checkIfSubTree(Node parentNode) {
		Node returnNode = null;
		if (parentNode != null) {
			String parentName = parentNode.getNodeName();
			if (SUB_TREE.equals(parentName)) {
				returnNode = parentNode;
			} else if (FUNCTIONAL_OP.equals(parentName)) {
				returnNode = checkIfSubTree(parentNode.getParentNode());
			}
		}
		return returnNode;
	}

	/**
	 * Gets the firt child list.
	 *
	 * @param function
	 *            the function
	 * @return the firt child list
	 */
	public static List<String> getFunctionalOpFirstChild(String function) {
		List<String> childList = new ArrayList<>();
		if (AGE_AT.equalsIgnoreCase(function)) {
			childList.add(SUB_TREE_REF);
			childList.add(RELATIONAL_OP);
			childList.add(FUNCTIONAL_OP);
			childList.add(ELEMENT_REF);
		} else {
			childList.add(ELEMENT_REF);
			childList.add(SET_OP);
			childList.add(SUB_TREE_REF);
			childList.add(RELATIONAL_OP);
		}
		return childList;
	}

	/**
	 * Gets the aggregate and instance function childs.
	 *
	 * @param typeChild
	 *            the type child
	 * @return the aggregate and instance function childs
	 */
	public static List<String> getAggregateAndInstanceFunctionChilds(String typeChild) {
		List<String> aggregateList = new ArrayList<>();
		aggregateList.add("FIRST");
		aggregateList.add("SECOND");
		aggregateList.add("THIRD");
		aggregateList.add("FOURTH");
		aggregateList.add("FIFTH");
		aggregateList.add("MOST RECENT");
		if ("AGGREGATE".equals(typeChild)) {
			aggregateList.add("DATETIMEDIFF");
		}
		return aggregateList;
	}

	/**
	 * Gets the functional op first child in mo.
	 *
	 * @param function
	 *            the function
	 * @return the functional op first child in mo
	 */
	public static List<String> getFunctionalOpFirstChildInMO(String function) {
		List<String> childList = new ArrayList<>();
		if ("DATETIMEDIFF".equalsIgnoreCase(function)) {
			childList.add(ELEMENT_REF);
			childList.add(SUB_TREE_REF);
			childList.add(RELATIONAL_OP);
			childList.addAll(getAggregateAndInstanceFunctionChilds("INSTANCE"));
		} else {
			childList.addAll(getFunctionalOpFirstChild(function));
			childList.addAll(getAggregateAndInstanceFunctionChilds("AGGREGATE"));
		}
		return childList;
	}

	/**
	 * Check for used Used sub tree ref Node map in Populations and Meausre
	 * Observations.
	 */
	private void createUsedSubTreeRefMap() {

		XmlProcessor simpleXmlProcessor = measureExport.getSimpleXMLProcessor();
		String typeXpathString = "";
		List<String> usedSubTreeRefIdsPop = new ArrayList<>();
		List<String> usedSubTreeRefIdsMO = new ArrayList<>();
		List<String> usedSubTreeRefIDsRA = new ArrayList<>();
		for (String typeString : POPULATION_NAME_LIST) {
			typeXpathString += "@type = '" + typeString + "' or";
		}
		typeXpathString = typeXpathString.substring(0, typeXpathString.lastIndexOf(" or"));
		String xpathForSubTreeInPOPClause = "/measure/measureGrouping//clause[" + typeXpathString + "]//subTreeRef/@id";
		String xpathForSubTreeInMOClause = "/measure/measureGrouping//clause[@type='measureObservation']//subTreeRef/@id";
		String xpathForSubTreeInRAClause = "/measure//riskAdjustmentVariables/subTreeRef/@id";
		try {

			// creating used Subtree Red Map in Populations
			NodeList populationsSubTreeNode = simpleXmlProcessor.findNodeList(simpleXmlProcessor.getOriginalDoc(),
					xpathForSubTreeInPOPClause);
			for (int i = 0; i < populationsSubTreeNode.getLength(); i++) {
				String uuid = populationsSubTreeNode.item(i).getNodeValue();
				uuid = checkIfQDMVarInstanceIsPresent(uuid, simpleXmlProcessor);
				if (!usedSubTreeRefIdsPop.contains(uuid)) {
					usedSubTreeRefIdsPop.add(uuid);
				}
			}
			usedSubTreeRefIdsPop = checkUnUsedSubTreeRef(simpleXmlProcessor, usedSubTreeRefIdsPop);
			for (String uuid : usedSubTreeRefIdsPop) {
				Node subTreeNode = createUsedSubTreeRefMap(simpleXmlProcessor, uuid);
				subTreeNodeInPOPMap.put(uuid, subTreeNode);
			}

			// creating used Subtree Red Map in Measure Observations
			NodeList measureObsSubTreeNode = simpleXmlProcessor.findNodeList(simpleXmlProcessor.getOriginalDoc(),
					xpathForSubTreeInMOClause);
			for (int i = 0; i < measureObsSubTreeNode.getLength(); i++) {
				String uuid = measureObsSubTreeNode.item(i).getNodeValue();
				uuid = checkIfQDMVarInstanceIsPresent(uuid, simpleXmlProcessor);
				if (!usedSubTreeRefIdsMO.contains(uuid)) {
					usedSubTreeRefIdsMO.add(uuid);
				}
			}
			usedSubTreeRefIdsMO = checkUnUsedSubTreeRef(simpleXmlProcessor, usedSubTreeRefIdsMO);
			for (String uuid : usedSubTreeRefIdsMO) {
				Node subTreeNode = createUsedSubTreeRefMap(simpleXmlProcessor, uuid);
				subTreeNodeInMOMap.put(uuid, subTreeNode);
			}
			// creating used Subtree Red in Risk Adjustment
			NodeList riskAdjSubTreeNode = simpleXmlProcessor.findNodeList(simpleXmlProcessor.getOriginalDoc(),
					xpathForSubTreeInRAClause);
			for (int i = 0; i < riskAdjSubTreeNode.getLength(); i++) {
				String uuid = riskAdjSubTreeNode.item(i).getNodeValue();
				uuid = checkIfQDMVarInstanceIsPresent(uuid, simpleXmlProcessor);
				if (!usedSubTreeRefIDsRA.contains(uuid)) {
					usedSubTreeRefIDsRA.add(uuid);
				}
			}
			usedSubTreeRefIDsRA = checkUnUsedSubTreeRef(simpleXmlProcessor, usedSubTreeRefIDsRA);
			for (String uuid : usedSubTreeRefIDsRA) {
				Node subTreeNode = createUsedSubTreeRefMap(simpleXmlProcessor, uuid);
				subTreeNodeInRAMap.put(uuid, subTreeNode);
			}

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates the used sub tree ref map.
	 *
	 * @param simpleXmlProcessor
	 *            the simple xml processor
	 * @param uuid
	 *            the uuid
	 * @return the node
	 */
	private Node createUsedSubTreeRefMap(XmlProcessor simpleXmlProcessor, String uuid) {
		String xpathforUsedSubTreeMap = "/measure/subTreeLookUp/subTree[@uuid='" + uuid + "']";
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
	 * @param xmlProcessor
	 *            the xml processor
	 * @param usedSubTreeRefIds
	 *            the used sub tree ref ids
	 * @return the list
	 */
	private List<String> checkUnUsedSubTreeRef(XmlProcessor xmlProcessor, List<String> usedSubTreeRefIds) {

		List<String> allSubTreeRefIds = new ArrayList<>();
		NodeList subTreeRefIdsNodeList;
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
		try {
			subTreeRefIdsNodeList = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(),
					"/measure//subTreeRef/@id");

			for (int i = 0; i < subTreeRefIdsNodeList.getLength(); i++) {
				Node SubTreeRefIdAttributeNode = subTreeRefIdsNodeList.item(i);
				if (!allSubTreeRefIds.contains(SubTreeRefIdAttributeNode.getNodeValue())) {
					allSubTreeRefIds.add(SubTreeRefIdAttributeNode.getNodeValue());
				}
			}
			allSubTreeRefIds.removeAll(usedSubTreeRefIds);

			for (int i = 0; i < usedSubTreeRefIds.size(); i++) {
				for (int j = 0; j < allSubTreeRefIds.size(); j++) {
					Node usedSubTreeRefNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(),
							"/measure/subTreeLookUp/subTree[@uuid='" + usedSubTreeRefIds.get(i) + "']//subTreeRef[@id='"
									+ allSubTreeRefIds.get(j) + "']");
					if (usedSubTreeRefNode != null) {

						String subTreeUUID = usedSubTreeRefNode.getAttributes().getNamedItem(ID).getNodeValue();
						String XPATH_IS_INSTANCE_OF = "//subTree [boolean(@instanceOf)]/@uuid ='" + subTreeUUID + "'";
						boolean isOccurrenceNode = (Boolean) xPath.evaluate(XPATH_IS_INSTANCE_OF,
								xmlProcessor.getOriginalDoc(), XPathConstants.BOOLEAN);
						if (isOccurrenceNode) {
							String XPATH_PARENT_UUID = "//subTree [@uuid ='" + subTreeUUID + "']/@instanceOf";
							String parentUUID = (String) xPath.evaluate(XPATH_PARENT_UUID,
									xmlProcessor.getOriginalDoc(), XPathConstants.STRING);
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
			e.printStackTrace();
		}
		return usedSubTreeRefIds;
	}

	/**
	 * Validate sub tree ref in pop.
	 *
	 * @param subTreeNode
	 *            the sub tree node
	 * @param functionalOpNode
	 *            the functional op node
	 * @return true, if successful
	 */
	public boolean validateSubTreeRefInPOP(Node subTreeNode, Node functionalOpNode) {
		if (subTreeNodeInPOPMap.get(subTreeNode.getAttributes().getNamedItem(UUID).getNodeValue()) != null) {
			String firstChildName = functionalOpNode.getFirstChild().getNodeName();
			String functionalOpType = functionalOpNode.getAttributes().getNamedItem(TYPE).getNodeValue();
			List<String> childsList = FUNCTIONAL_OP_RULES_IN_POP.get(functionalOpType);
			if (childsList.contains(firstChildName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Utility method which will try to find the tag "localVariableName" and set the
	 * given string value to its VALUE attribute.
	 *
	 * @param node
	 *            the node
	 * @param localVarName
	 *            the local var name
	 */
	private void updateLocalVar(Node node, String localVarName) {
		if (node == null) {
			return;
		}
		NodeList localVarNodeList = ((Element) node).getElementsByTagName(LOCAL_VARIABLE_NAME);
		if (localVarNodeList != null && localVarNodeList.getLength() > 0) {
			Element localVar = (Element) localVarNodeList.item(0);
			localVar.setAttribute(VALUE, localVarName);
		}
	}

	/**
	 * Check if qdm var instance is present.
	 *
	 * @param usedSubtreeRefId
	 *            the used subtree ref id
	 * @param xmlProcessor
	 *            the xml processor
	 * @return the string
	 */
	private String checkIfQDMVarInstanceIsPresent(String usedSubtreeRefId, XmlProcessor xmlProcessor) {

		String XPATH_INSTANCE_QDM_VAR = "/measure/subTreeLookUp/subTree[@uuid='" + usedSubtreeRefId + "']/@instance";
		String XPATH_INSTANCE_OF_QDM_VAR = "/measure/subTreeLookUp/subTree[@uuid='" + usedSubtreeRefId
				+ "']/@instanceOf";
		try {
			Node nodesSDE_SubTree = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), XPATH_INSTANCE_QDM_VAR);
			if (nodesSDE_SubTree != null) {
				Node nodesSDE_SubTreeInstance = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(),
						XPATH_INSTANCE_OF_QDM_VAR);
				usedSubtreeRefId = nodesSDE_SubTreeInstance.getNodeValue();
			}

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return usedSubtreeRefId;
	}

	/**
	 * Gets the sub tree node map.
	 *
	 * @return the subTreeNodeMap
	 */
	public Map<String, Node> getSubTreeNodeMap() {
		return subTreeNodeMap;
	}

	/**
	 * Sets the sub tree node map.
	 *
	 * @param subTreeNodeMap
	 *            the subTreeNodeMap to set
	 */
	public void setSubTreeNodeMap(Map<String, Node> subTreeNodeMap) {
		this.subTreeNodeMap = subTreeNodeMap;
	}

	/**
	 * Gets the measure export.
	 *
	 * @return the measureExport
	 */
	public MeasureExport getMeasureExport() {
		return measureExport;
	}

	/**
	 * Sets the measure export.
	 *
	 * @param measureExport
	 *            the measureExport to set
	 */
	public void setMeasureExport(MeasureExport measureExport) {
		this.measureExport = measureExport;
	}

	/**
	 * Gets the sub tree node in mo map.
	 *
	 * @return the subTreeNodeInMOMap
	 */
	public Map<String, Node> getSubTreeNodeInMOMap() {
		return subTreeNodeInMOMap;
	}

	/**
	 * Sets the sub tree node in mo map.
	 *
	 * @param subTreeNodeInMOMap
	 *            the subTreeNodeInMOMap to set
	 */
	public void setSubTreeNodeInMOMap(Map<String, Node> subTreeNodeInMOMap) {
		this.subTreeNodeInMOMap = subTreeNodeInMOMap;
	}

	/**
	 * Gets the sub tree node in ra map.
	 *
	 * @return the subTreeNodeInMOMap
	 */
	public Map<String, Node> getSubTreeNodeInRAMap() {
		return subTreeNodeInRAMap;
	}

	/**
	 * Sets the sub tree node in ra map.
	 *
	 * @param subTreeNodeInRAMap
	 *            the sub tree node in ra map
	 */
	public void setSubTreeNodeInRAMap(Map<String, Node> subTreeNodeInRAMap) {
		this.subTreeNodeInRAMap = subTreeNodeInRAMap;
	}

	/**
	 * Gets the sub tree node in pop map.
	 *
	 * @return the subTreeNodeInPOPMap
	 */
	public Map<String, Node> getSubTreeNodeInPOPMap() {
		return subTreeNodeInPOPMap;
	}

	/**
	 * Sets the sub tree node in pop map.
	 *
	 * @param subTreeNodeInPOPMap
	 *            the subTreeNodeInPOPMap to set
	 */
	public void setSubTreeNodeInPOPMap(Map<String, Node> subTreeNodeInPOPMap) {
		this.subTreeNodeInPOPMap = subTreeNodeInPOPMap;
	}
}
