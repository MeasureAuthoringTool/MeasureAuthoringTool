package mat.server.hqmf.qdm_5_5;

import mat.model.clause.MeasureExport;
import mat.server.hqmf.QDMTemplateProcessorFactory;
import mat.server.util.XmlProcessor;
import mat.shared.UUIDUtilClient;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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

/**
 * @deprecated The Class HQMFPopulationLogicGenerator.
 */
public class HQMFMeasureObservationLogicGenerator extends HQMFClauseLogicGenerator {
	private static final String MEASURE_OBSERVATION_EXTENSION_VALUE = "2018-05-01";

	private static final int DATETIMEDIFF_CHILD_COUNT = 2;

	private Map<String, Node> clauseLogicMap = new HashMap<String, Node>();

	private TreeMap<Integer, NodeList> measureGroupingMap = new TreeMap<Integer, NodeList>();

	private MeasureExport me;

	private String scoringType;

	private boolean clauseLogicHasElementRef = false;

	/**
	 * MAP of Functional Ops AGGREGATE that can be used in Measure Observation.
	 */
	private static final Map<String, String> FUNCTIONAL_OPS_AGGREGATE = new TreeMap<String, String>(
			String.CASE_INSENSITIVE_ORDER);

	/**
	 * MAP of Functional Ops INCLUDED FUNCTIONS that can be used in Measure
	 * Observation.
	 */
	private static final Map<String, String> INCLUDED_FUNCTIONAL_NAMES = new TreeMap<String, String>(
			String.CASE_INSENSITIVE_ORDER);
	static {
		FUNCTIONAL_OPS_AGGREGATE.put("MAX", "MAX");
		FUNCTIONAL_OPS_AGGREGATE.put("MIN", "MIN");
		FUNCTIONAL_OPS_AGGREGATE.put("SUM", "SUM");
		FUNCTIONAL_OPS_AGGREGATE.put("AVG", "AVERAGE");
		FUNCTIONAL_OPS_AGGREGATE.put("COUNT", "COUNT");
		FUNCTIONAL_OPS_AGGREGATE.put("MEDIAN", "MEDIAN");
		FUNCTIONAL_OPS_AGGREGATE.put("DATETIMEDIFF", null);
		INCLUDED_FUNCTIONAL_NAMES.put("FIRST", "FIRST");
		INCLUDED_FUNCTIONAL_NAMES.put("SECOND", "SECOND");
		INCLUDED_FUNCTIONAL_NAMES.put("THIRD", "THIRD");
		INCLUDED_FUNCTIONAL_NAMES.put("FOURTH", "FOURTH");
		INCLUDED_FUNCTIONAL_NAMES.put("FIFTH", "FIFTH");
		INCLUDED_FUNCTIONAL_NAMES.put("MOST RECENT", "MOST RECENT");
		INCLUDED_FUNCTIONAL_NAMES.put("COUNT", "COUNT");
		INCLUDED_FUNCTIONAL_NAMES.put("DATETIMEDIFF", "DATETIMEDIFF");
	}

	@Override
	public String generate(MeasureExport me) throws Exception {
		this.me = me;
		setMeasureExport(me);
		getMeasureScoringType(me);
		generateClauseLogicMap(me);
		getAllMeasureGroupings(me);
		generateMeasureObSection(me);
		return null;
	}

	/**
	 * Get DotNotation from templates.xml.
	 * 
	 * @param attributeName
	 *            -String
	 * @param dataTypeName
	 *            - String
	 * @throws XPathExpressionException
	 *             -Exception
	 * @return String dot notation.
	 */
	private String getQdmAttributeMapppingDotNotation(String attributeName, String dataTypeName)
			throws XPathExpressionException {
		XmlProcessor templateXMLProcessor = QDMTemplateProcessorFactory.getTemplateProcessor(5.5);
		String xPath = "/templates/attributeMappings/attributeMapping[@qdmAttribute=\"" + attributeName + "\"]";
		Node attributeMappingNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc(), xPath);
		if (attributeMappingNode == null) {
			xPath = "/templates/attributeMappings/attributeMapping[@qdmAttribute='" + attributeName
					+ "'  and @datatypes = \"" + dataTypeName.toLowerCase() + "\"]";
			attributeMappingNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc(), xPath);
		}
		if (attributeMappingNode != null) {
			return attributeMappingNode.getAttributes().getNamedItem("dotNotation").getNodeValue();
		} else {
			return null;
		}
	}

	/**
	 * Method to generate MeasureObservation Criteria Section.
	 * 
	 * @param me
	 *            - MeasureExport
	 * @throws XPathExpressionException
	 *             - Exception
	 */
	private void generateMeasureObSection(MeasureExport me) throws XPathExpressionException {
		String isInGrouping = "";
		for (Integer key : measureGroupingMap.keySet()) {
			NodeList groupingChildList = measureGroupingMap.get(key);
			for (int i = 0; i < groupingChildList.getLength(); i++) {
				Node groupingChildListItem = groupingChildList.item(i);

				String popType = groupingChildListItem.getAttributes().getNamedItem(TYPE).getNodeValue();
				if (groupingChildList.item(i).getAttributes().getNamedItem(GROUPING_CHECK) != null) {
					isInGrouping = groupingChildList.item(i).getAttributes().getNamedItem(GROUPING_CHECK)
							.getNodeValue();
				}
				switch (popType) {
				case "measureObservation":
					if (isInGrouping != null && !isInGrouping.isEmpty()) {
						if (isInGrouping.equalsIgnoreCase("true")) {

							Node measureObSectionComponentElement = createMeasureObservationSection(
									me.getHQMFXmlProcessor());

							generateMeasureObDefinition(groupingChildListItem, measureObSectionComponentElement, me);
						}
					} else {
						Node measureObSectionComponentElement = createMeasureObservationSection(
								me.getHQMFXmlProcessor());

						generateMeasureObDefinition(groupingChildListItem, measureObSectionComponentElement, me);
					}
					break;
				case "denominator":
					break;
				case "numerator":
					break;
				default:
					// do nothing.
					break;
				}
			}
		}
	}

	/**
	 * Method to generate default criteriaTag for all population types included in
	 * measure grouping.
	 * 
	 * @param measureObsClauseNode
	 *            - Node
	 * @param measureObservationSecElement
	 *            - Element
	 * @param me
	 *            - MeasureExport
	 * @param key
	 * @throws XPathExpressionException
	 *             - Exception
	 */
	private void generateMeasureObDefinition(Node measureObsClauseNode, Node measureObservationSecElement,
			MeasureExport me) throws XPathExpressionException {

		if (!measureObsClauseNode.hasChildNodes()) {
			return;
		}

		Document doc = measureObservationSecElement.getOwnerDocument();
		Comment comment = doc.createComment(
				"Definition for " + measureObsClauseNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue());
		Element definitionElement = doc.createElement("definition");
		Element measureObDefinitionElement = doc.createElement("measureObservationDefinition");
		measureObDefinitionElement.setAttribute(CLASS_CODE, "OBS");
		measureObDefinitionElement.setAttribute(MOOD_CODE, "DEF");
		Element idElem = doc.createElement(ID);
		idElem.setAttribute(ROOT, measureObsClauseNode.getAttributes().getNamedItem("uuid").getNodeValue());
		idElem.setAttribute(EXTENSION, "MeasureObservation");
		measureObDefinitionElement.appendChild(idElem);

		Element codeElem = doc.createElement(CODE);
		codeElem.setAttribute(CODE, "AGGREGATE");
		codeElem.setAttribute(CODE_SYSTEM, "2.16.840.1.113883.5.4");
		measureObDefinitionElement.appendChild(codeElem);
		generateCQLLogicForMeasureObservation(measureObsClauseNode, measureObDefinitionElement);
		definitionElement.appendChild(measureObDefinitionElement);
		Element measurObSectionElement = (Element) measureObservationSecElement.getFirstChild();
		measurObSectionElement.appendChild(comment);
		measurObSectionElement.appendChild(definitionElement);
	}

	private void generateCQLLogicForMeasureObservation(Node measureObservationNode, Element measureObservationElementNode) {

		if (!measureObservationNode.hasChildNodes()) {
			return;
		}

		String extensionAttribute = "";

		Document doc = measureObservationElementNode.getOwnerDocument();

		Element valueElement = doc.createElement(VALUE);
		valueElement.setAttribute("xsi:type", "INT");
		valueElement.setAttribute("nullFlavor", "DER");

		Node firstChildNode = measureObservationNode.getFirstChild();
		String firstChildNodeName = firstChildNode.getNodeName();

		if ("cqlfunction".equals(firstChildNodeName)) {

			String functionName = firstChildNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
			extensionAttribute += functionName;

			Element expressionElement = createExpressionTag(measureObservationNode, doc, firstChildNode);
			valueElement.appendChild(expressionElement);
			measureObservationElementNode.appendChild(valueElement);

		} else if ("cqlaggfunction".equals(firstChildNodeName)) {

			String aggFunctionName = firstChildNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
			String aggFunctionHQMFName = getAggMethodCode(aggFunctionName);

			extensionAttribute += aggFunctionHQMFName + " Of ";

			Node aggNodeChild = firstChildNode.getFirstChild();
			if ("cqlfunction".equals(aggNodeChild.getNodeName())) {

				String functionName = aggNodeChild.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
				extensionAttribute += functionName;

				Element expressionElement = createExpressionTag(measureObservationNode, doc, aggNodeChild);
				valueElement.appendChild(expressionElement);
				measureObservationElementNode.appendChild(valueElement);
			}

			Element methodCodeElement = doc.createElement("methodCode");
			Element itemElement = doc.createElement(ITEM);
			itemElement.setAttribute(CODE, aggFunctionHQMFName);
			itemElement.setAttribute(CODE_SYSTEM, "2.16.840.1.113883.5.84");

			methodCodeElement.appendChild(itemElement);
			measureObservationElementNode.appendChild(methodCodeElement);
		}

		if (extensionAttribute.length() > 0) {
			NodeList idList = measureObservationElementNode.getElementsByTagName(ID);
			if (idList.getLength() > 0) {
				Node id = idList.item(0);
				id.getAttributes().getNamedItem(EXTENSION).setNodeValue(extensionAttribute);
			}
		}

		// check for associated population id and add related HQMF nodes.
		handleMeasureObservationAssociations(measureObservationNode, measureObservationElementNode);
	}

	private void handleMeasureObservationAssociations(Node measureObservationNode, Element measureObDefinitionElement) {
		Node associatedPopulationNode = null;
		try {
			associatedPopulationNode = getAssociatedPopulationNode(measureObservationNode);
			if (associatedPopulationNode != null && associatedPopulationNode.hasChildNodes()) {
				
				// the measure observation criteria reference root and extension should come from the root and extension values of the associated
				// population 
				String measureObservationCriteriaReferenceIdRoot = associatedPopulationNode.getAttributes().getNamedItem("uuid").getNodeValue();
				String measureObservationCriteriaReferenceIdExtension = associatedPopulationNode.getAttributes().getNamedItem("type").getNodeValue();

				Document doc = measureObDefinitionElement.getOwnerDocument();

				Element component = doc.createElement("component");
				component.setAttribute(TYPE_CODE, "COMP");

				Element criteriaReference = doc.createElement("criteriaReference");
				criteriaReference.setAttribute(CLASS_CODE, "OBS");
				criteriaReference.setAttribute(MOOD_CODE, "EVN");

				Element id = doc.createElement(ID);
				id.setAttribute(ROOT, measureObservationCriteriaReferenceIdRoot);
				id.setAttribute(EXTENSION, measureObservationCriteriaReferenceIdExtension);

				measureObDefinitionElement.appendChild(component);
				component.appendChild(criteriaReference);
				criteriaReference.appendChild(id);
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}

	private Node getAssociatedPopulationNode(Node measureObservationNode) throws XPathExpressionException {
		String associatedPopulationUUID = getAssociatedPopulationUUID(measureObservationNode);
		String associatedClauseXPath = "//measureGrouping/group/clause[@uuid = '" + associatedPopulationUUID + "']";

		Node associatedPopulationNode = me.getSimpleXMLProcessor().findNode(measureObservationNode.getOwnerDocument(),
				associatedClauseXPath);
		return associatedPopulationNode;
	}

	public String getAssociatedPopulationUUID(Node msrObsClauseNode) {
		String associatedPopuUUID = "";

		if (scoringType.equalsIgnoreCase("Continuous Variable")) {
			associatedPopuUUID = findMeasurePopulationUUID(msrObsClauseNode.getParentNode());
		} else {
			if (msrObsClauseNode.getAttributes().getNamedItem(ASSOCIATED_POPULATION_UUID) != null) {
				associatedPopuUUID = msrObsClauseNode.getAttributes().getNamedItem(ASSOCIATED_POPULATION_UUID).getNodeValue();
			}
		}

		return associatedPopuUUID;
	}

	private String findMeasurePopulationUUID(Node parentNode) {

		String uuid = "";

		NodeList nodeList = parentNode.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (MEASURE_POPULATION.equals(node.getAttributes().getNamedItem(TYPE).getNodeValue())) {
				uuid = node.getAttributes().getNamedItem(UUID).getNodeValue();
				break;
			}
		}

		return uuid;
	}

	/**
	 * Create <expression> tag for Measure Observation.
	 * 
	 * @param item
	 * @param doc
	 * @param cqlFunctionNode
	 * @return
	 */
	public Element createExpressionTag(Node item, Document doc, Node cqlFunctionNode) {

		Element expressionElement = doc.createElement("expression");
		String cqlFunctionNameValue = cqlFunctionNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();

		String cqlLibraryNameXPath = "//cqlLookUp/library";

		try {

			Node cqlLibraryNameNode = me.getSimpleXMLProcessor().findNode(item.getOwnerDocument(), cqlLibraryNameXPath);
			String libraryName = "";
			if (cqlLibraryNameNode != null) {
				libraryName = cqlLibraryNameNode.getTextContent();
			}
			String expressionValueAttribute = libraryName + ".\"" + cqlFunctionNameValue + "\"";
			expressionElement.setAttribute(VALUE, expressionValueAttribute);

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return expressionElement;
	}

	private String getAggMethodCode(String aggregateFunctionName) {

		String hqmfCodeForAggFunction = "";
		switch (aggregateFunctionName) {

		case "Count":
		case "Sum":
		case "Average":
		case "Median":
		case "Mode":
			hqmfCodeForAggFunction = aggregateFunctionName.toUpperCase();
			break;

		case "Sample Standard Deviation":
			hqmfCodeForAggFunction = "STDEV.S";
			break;
		case "Sample Variance":
			hqmfCodeForAggFunction = "VARIANCE.S";
			break;
		case "Population Standard Deviation":
			hqmfCodeForAggFunction = "STDEV.P";
			break;
		case "Population Variance":
			hqmfCodeForAggFunction = "VARIANCE.P";
			break;
		case "Minimum":
			hqmfCodeForAggFunction = "MIN";
			break;
		case "Maximum":
			hqmfCodeForAggFunction = "MAX";
			break;
		default:
			break;
		}

		return hqmfCodeForAggFunction;
	}

	/**
	 * Method to find Node's Parent - Subtree Name.
	 * 
	 * @param node
	 *            - Node
	 * @return SubTree DisplayName.
	 */
	private String findSubTreeDisplayName(Node node) {
		String displayName = null;
		if (node != null) {
			String nodeName = node.getNodeName();
			if ("subTree".equals(nodeName)) {
				displayName = node.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
				if (node.getAttributes().getNamedItem("qdmVariable") != null) {
					if (node.getAttributes().getNamedItem("qdmVariable").getNodeValue().equalsIgnoreCase("true")) {
						displayName = "qdm_var_" + displayName.replace("$", "");
					}
				}
			} else {
				displayName = findSubTreeDisplayName(node.getParentNode());
			}
		}
		return StringUtils.deleteWhitespace(displayName);
	}

	/**
	 * Method to Evaluate Clause logic Node types and generate Clause Logic in Data
	 * Criteria section if applicable and generates value expression/precondition.
	 * 
	 * @param clauseNodes
	 *            - Clause Logic Nodes
	 * @param elementRefList
	 *            - Element Ref List
	 * @param measureObDefinitionElement
	 *            - DOM element
	 * @throws XPathExpressionException
	 *             - Exception.
	 */
	private String generateMOClauseLogic(Node clauseNodes, List<Node> elementRefList,
			Element measureObDefinitionElement, boolean isClauseLogicGeneratable, String variableName,
			boolean checkIfDateTimeDiff) throws XPathExpressionException {
		String localVariableName = null;
		Node firstChildNode = null;
		Node parentSubTreeNode = null;
		String preCodExp = null;
		if (variableName != null) {
			localVariableName = variableName;
		}
		if (isClauseLogicGeneratable) {
			firstChildNode = clauseNodes.getFirstChild();
			if (clauseNodes.getParentNode() != null) {
				parentSubTreeNode = clauseNodes.getParentNode().cloneNode(false);
			} else if (clauseNodes.getNodeName().equals("subTree")) {
				parentSubTreeNode = clauseNodes.cloneNode(false);
			}
		} else {
			if ((checkIfParentSubTree(clauseNodes).getNodeName()).equalsIgnoreCase("subTree")) {
				if (clauseNodes.getNodeName().equals("elementRef")) {
					firstChildNode = clauseNodes;
				} else {
					firstChildNode = clauseNodes.getFirstChild();
				}
				parentSubTreeNode = clauseNodes.cloneNode(false);
			} else {
				firstChildNode = clauseNodes;
				parentSubTreeNode = clauseNodes.getParentNode().cloneNode(false);
			}
		}
		String firstChildNodeName = firstChildNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
		switch (firstChildNode.getNodeName()) {
		case "setOp":
			generateForSetOp(measureObDefinitionElement, isClauseLogicGeneratable, checkIfDateTimeDiff,
					localVariableName, firstChildNode, parentSubTreeNode);
			break;
		case "relationalOp":
			preCodExp = generateForRelationalOperator(measureObDefinitionElement, isClauseLogicGeneratable,
					checkIfDateTimeDiff, localVariableName, firstChildNode, parentSubTreeNode);
			break;
		case "elementRef":
			elementRefList.add(firstChildNode);
			if (localVariableName != null) {
				preCodExp = generateValueAndExpressionTag(elementRefList, measureObDefinitionElement, firstChildNode,
						localVariableName);
			} else {
				preCodExp = generateValueAndExpressionTag(elementRefList, measureObDefinitionElement, firstChildNode,
						null);
			}
			break;
		case "functionalOp":
			if (INCLUDED_FUNCTIONAL_NAMES.containsKey(firstChildNodeName)) {
				preCodExp = generateForFunctionalOperator(elementRefList, measureObDefinitionElement,
						checkIfDateTimeDiff, firstChildNode, parentSubTreeNode, firstChildNodeName);
			}
			break;
		case "subTreeRef":
			generateForSubTreeRef(elementRefList, measureObDefinitionElement, isClauseLogicGeneratable,
					checkIfDateTimeDiff, localVariableName, firstChildNode, parentSubTreeNode);
			break;
		default:
			break;
		}
		return preCodExp;
	}

	/**
	 * @param elementRefList
	 * @param measureObDefinitionElement
	 * @param isClauseLogicGeneratable
	 * @param checkIfDateTimeDiff
	 * @param localVariableName
	 * @param firstChildNode
	 * @param parentSubTreeNode
	 * @throws XPathExpressionException
	 */
	private void generateForSubTreeRef(List<Node> elementRefList, Element measureObDefinitionElement,
			boolean isClauseLogicGeneratable, boolean checkIfDateTimeDiff, String localVariableName,
			Node firstChildNode, Node parentSubTreeNode) throws XPathExpressionException {
		Node subTreeRefNodeLogic = clauseLogicMap.get(firstChildNode.getAttributes().getNamedItem("id").getNodeValue());
		Node subTreeRefParentNode = parentSubTreeNode.cloneNode(false);
		subTreeRefParentNode.appendChild(subTreeRefNodeLogic.cloneNode(true));
		String firstSubTreeNodeName = subTreeRefNodeLogic.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
		if (subTreeRefNodeLogic.getNodeName().equalsIgnoreCase("functionalOp")) {
			if (firstSubTreeNodeName.equalsIgnoreCase(DATETIMEDIFF)) {
				generateMOClauseLogicForDateTimeDiff(subTreeRefNodeLogic, elementRefList, measureObDefinitionElement);
			} else {
				if (isClauseLogicGeneratable) {
					localVariableName = generateClauseLogicForChildsInsideFnxOp(subTreeRefParentNode,
							checkIfDateTimeDiff);
				}
				generateMOClauseLogic(subTreeRefParentNode, elementRefList, measureObDefinitionElement, false,
						localVariableName, checkIfDateTimeDiff);
			}
		} else {
			if (isClauseLogicGeneratable) {
				localVariableName = generateClauseLogicForChildsInsideFnxOp(subTreeRefParentNode, checkIfDateTimeDiff);
			}
			generateMOClauseLogic(subTreeRefParentNode, elementRefList, measureObDefinitionElement, false,
					localVariableName, checkIfDateTimeDiff);
		}
	}

	private String generateForFunctionalOperator(List<Node> elementRefList, Element measureObDefinitionElement,
			boolean checkIfDateTimeDiff, Node firstChildNode, Node parentSubTreeNode, String firstChildNodeName)
			throws XPathExpressionException {
		String localVariableName;
		String preCodExp = null;
		if (DATETIMEDIFF.equalsIgnoreCase(firstChildNodeName)) {
			generateMOClauseLogicForDateTimeDiff(firstChildNode, elementRefList, measureObDefinitionElement);
		} else {
			Node childNode = firstChildNode.getFirstChild().getFirstChild();
			String childNodeName = "";
			boolean isDateTimeDiff = false;
			if (childNode != null) {
				childNodeName = childNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
			}
			if (childNodeName.equalsIgnoreCase(DATETIMEDIFF)) {
				isDateTimeDiff = true;
			}
			if (!isDateTimeDiff) {
				Node functionalOp = firstChildNode.cloneNode(true);
				parentSubTreeNode.appendChild(functionalOp);
				localVariableName = generateClauseLogicForChildsInsideFnxOp(parentSubTreeNode, false);
				preCodExp = generateMOClauseLogic(parentSubTreeNode.getFirstChild(), elementRefList,
						measureObDefinitionElement, false, localVariableName, checkIfDateTimeDiff);
			}
		}
		return preCodExp;
	}

	private String generateForRelationalOperator(Element measureObDefinitionElement, boolean isClauseLogicGeneratable,
			boolean checkIfDateTimeDiff, String localVariableName, Node firstChildNode, Node parentSubTreeNode)
			throws XPathExpressionException {
		String preConditionExpressionValue = null;
		Node relOpsNode = firstChildNode.cloneNode(true);
		parentSubTreeNode.appendChild(relOpsNode);
		Node relOpsFirstChild = relOpsNode.getFirstChild();
		// will not generate clause logic with timing LHS as DATETIMEDIFF
		if (!relOpsFirstChild.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue()
				.equalsIgnoreCase(DATETIMEDIFF)) {
			if (isClauseLogicGeneratable) {
				localVariableName = generateClauseLogicForChildsInsideFnxOp(parentSubTreeNode, checkIfDateTimeDiff);
			}
			if (localVariableName != null) {
				List<Node> relOpLHSQdm = findFirstLHSElementRef(firstChildNode, new ArrayList<Node>(),
						measureObDefinitionElement);
				if ((relOpLHSQdm != null) && (relOpLHSQdm.size() > 0)) {
					clauseLogicHasElementRef = true;
					preConditionExpressionValue = generateValueAndExpressionTag(relOpLHSQdm, measureObDefinitionElement,
							firstChildNode, localVariableName);
				} else {
					Element valueElementRelOp = measureObDefinitionElement.getOwnerDocument().createElement("value");
					valueElementRelOp.setAttribute(XSI_TYPE, "PQ");
					Element expressionElementRelOp = measureObDefinitionElement.getOwnerDocument()
							.createElement("expression");
					expressionElementRelOp.setAttribute(VALUE, localVariableName);
					valueElementRelOp.appendChild(expressionElementRelOp);
					measureObDefinitionElement.appendChild(valueElementRelOp);
				}
			}
		}
		return preConditionExpressionValue;
	}

	private void generateForSetOp(Element measureObDefinitionElement, boolean isClauseLogicGeneratable,
			boolean checkIfDateTimeDiff, String localVariableName, Node firstChildNode, Node parentSubTreeNode)
			throws XPathExpressionException {
		Node setOpsNode = firstChildNode.cloneNode(true);
		parentSubTreeNode.appendChild(setOpsNode);
		if (isClauseLogicGeneratable) {
			localVariableName = generateClauseLogicForChildsInsideFnxOp(parentSubTreeNode, checkIfDateTimeDiff);
		}
		if (localVariableName != null) {
			Element valueElement = measureObDefinitionElement.getOwnerDocument().createElement("value");
			valueElement.setAttribute(XSI_TYPE, "PQ");
			Element expressionElement = measureObDefinitionElement.getOwnerDocument().createElement("expression");
			expressionElement.setAttribute(VALUE, localVariableName);
			valueElement.appendChild(expressionElement);
			measureObDefinitionElement.appendChild(valueElement);
		}
	}

	private Node getSubTreeNode(Node parentNode) {
		Node subTreeNode = null;
		if (parentNode != null) {
			String nodeName = parentNode.getNodeName();
			if ("subTree".equals(nodeName)) {
				subTreeNode = parentNode;
			} else {
				subTreeNode = getSubTreeNode(parentNode.getParentNode());
			}
		}
		return subTreeNode;
	}

	/**
	 * Method to generate Measure Observation Clause logic for Date TimeDiff.
	 * 
	 * @param clauseNodes
	 *            - CLause Logic Nodes
	 * @param elementRefList
	 *            - Element Ref List
	 * @param measureObDefinitionElement
	 *            - DOM Element.
	 * @throws XPathExpressionException
	 *             - Exception.
	 */
	private void generateMOClauseLogicForDateTimeDiff(Node clauseNodes, List<Node> elementRefList,
			Element measureObDefinitionElement) throws XPathExpressionException {
		NodeList childNodes = clauseNodes.getChildNodes();
		if (childNodes.getLength() >= DATETIMEDIFF_CHILD_COUNT) { // DATETIMEDIFF SHOULD HAVE TWO OR MORE CHILD.
			boolean generateValueExpression = false;
			String localVariableName = null;
			Node firstChildNode = null;
			String preCondExp = StringUtils.EMPTY;
			for (int i = 0; i < childNodes.getLength(); i++) {
				firstChildNode = childNodes.item(i);
				String firstChildNodeName = firstChildNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
				Node parentSubTreeNode = getSubTreeNode(clauseNodes.getParentNode());
				switch (firstChildNode.getNodeName()) {
				case "relationalOp":
					Node relOpsNode = firstChildNode.cloneNode(true);
					parentSubTreeNode = parentSubTreeNode.cloneNode(false);
					parentSubTreeNode.appendChild(relOpsNode);
					localVariableName = generateClauseLogicForChildsInsideFnxOp(parentSubTreeNode, true);
					if (localVariableName != null) {
						List<Node> relOpLHSQDM = findFirstLHSElementRef(firstChildNode, new ArrayList<Node>(),
								measureObDefinitionElement);
						if ((relOpLHSQDM != null) && (relOpLHSQDM.size() > 0)) {
							if (preCondExp == null) {
								preCondExp = generateValueAndExpressionTag(relOpLHSQDM, measureObDefinitionElement,
										firstChildNode, localVariableName);
							} else {
								preCondExp = preCondExp + generateValueAndExpressionTag(relOpLHSQDM,
										measureObDefinitionElement, firstChildNode, localVariableName);
							}
							clauseLogicHasElementRef = true;
						} else {
							Element valueElementRelOp = measureObDefinitionElement.getOwnerDocument()
									.createElement("value");
							valueElementRelOp.setAttribute(XSI_TYPE, "PQ");
							Element expressionElementRelOp = measureObDefinitionElement.getOwnerDocument()
									.createElement("expression");
							expressionElementRelOp.setAttribute(VALUE, localVariableName);
							valueElementRelOp.appendChild(expressionElementRelOp);
							measureObDefinitionElement.appendChild(valueElementRelOp);
						}
					}
					break;
				case "elementRef":
					elementRefList.add(firstChildNode);
					generateValueExpression = true;
					break;
				case "functionalOp":
					if (INCLUDED_FUNCTIONAL_NAMES.containsKey(firstChildNodeName)) {
						parentSubTreeNode = parentSubTreeNode.cloneNode(false);
						if (!DATETIMEDIFF.equalsIgnoreCase(firstChildNodeName)) {
							Node functionalOp = firstChildNode.cloneNode(true);
							parentSubTreeNode.appendChild(functionalOp);
							localVariableName = generateClauseLogicForChildsInsideFnxOp(parentSubTreeNode, true);
							if (localVariableName != null) {
								preCondExp = preCondExp
										+ generateMOClauseLogic(functionalOp.getFirstChild(), new ArrayList<Node>(),
												measureObDefinitionElement, false, localVariableName, false);
							}
						}
					}
					break;
				case "subTreeRef":
					Node subTreeRefNodeLogic = clauseLogicMap
							.get(firstChildNode.getAttributes().getNamedItem("id").getNodeValue());
					if (!subTreeRefNodeLogic.getNodeName().equalsIgnoreCase("setOp")
							&& !subTreeRefNodeLogic.getNodeName().equalsIgnoreCase("elementRef")) {
						Node subTreeRefParentNode = parentSubTreeNode.cloneNode(false);
						subTreeRefParentNode.appendChild(subTreeRefNodeLogic);
						preCondExp = preCondExp + generateMOClauseLogic(subTreeRefParentNode, new ArrayList<Node>(),
								measureObDefinitionElement, true, localVariableName, true);
					} else if (subTreeRefNodeLogic.getNodeName().equalsIgnoreCase("elementRef")) {
						elementRefList.add(subTreeRefNodeLogic);
						generateValueExpression = true;
						break;
					}
					break;
				default:
					break;
				}
			}
			if (generateValueExpression || (StringUtils.isNotEmpty(preCondExp))) {
				String preConditionExpression = generateValueAndExpressionTag(elementRefList,
						measureObDefinitionElement, firstChildNode, null);
				if (StringUtils.isNotEmpty(preCondExp)) {
					preConditionExpression = preCondExp + preConditionExpression;
				}
				generatePreCondition(measureObDefinitionElement, preConditionExpression, elementRefList);
			}
		}
	}

	/**
	 * Method to find First QDM Element used in Clause Logic.
	 * 
	 * @param firstChildNode
	 *            - Clause Logic First Child Node.
	 * @param arrayList
	 *            - Element Ref List
	 * @param measureObDefinitionElement
	 *            - DOM Element.
	 * @return - Element Ref List
	 * @throws XPathExpressionException
	 *             - Exception.
	 */
	private List<Node> findFirstLHSElementRef(Node firstChildNode, ArrayList<Node> arrayList,
			Element measureObDefinitionElement) throws XPathExpressionException {
		if (firstChildNode.hasChildNodes()) {
			Node lhsNode = firstChildNode.getFirstChild();
			switch (lhsNode.getNodeName()) {
			case "elementRef":
				arrayList.add(lhsNode);
				break;
			case "setOp":
				arrayList = null;
				break;
			case "relationalOp":
				arrayList = (ArrayList<Node>) findFirstLHSElementRef(lhsNode, arrayList, measureObDefinitionElement);
				break;
			case "functionalOp":
				if (!lhsNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue().equalsIgnoreCase(DATETIMEDIFF)) {
					if (lhsNode.hasChildNodes()) {
						arrayList = (ArrayList<Node>) findFirstLHSElementRef(lhsNode, arrayList,
								measureObDefinitionElement);
						break;
					} else {
						arrayList = null;
						break;
					}
				}
				break;
			case "subTreeRef":
				Node subTreeRefNodeLogic = clauseLogicMap
						.get(lhsNode.getAttributes().getNamedItem("id").getNodeValue());
				arrayList = (ArrayList<Node>) findFirstLHSElementRef(subTreeRefNodeLogic.getParentNode(), arrayList,
						measureObDefinitionElement);
				break;
			default:
				break;
			}
		}
		return arrayList;
	}

	/**
	 * This method generates clause logic For Functional Op.
	 * 
	 * @param clauseNodes
	 *            - Clause Logic Nodes.
	 * @param checkIfDatimeDiff
	 *            - Boolean
	 * @return - LocalVariable Name.
	 * @throws XPathExpressionException
	 *             - Exception
	 */
	private String generateClauseLogicForChildsInsideFnxOp(Node clauseNodes, boolean checkIfDatimeDiff)
			throws XPathExpressionException {
		Node generatedClauseEntryNode = generateSubTreeXML(clauseNodes, checkIfDatimeDiff);
		String localVariableNameValue = null;
		if (generatedClauseEntryNode != null) {
			localVariableNameValue = findSubTreeDisplayName(clauseNodes);
			localVariableNameValue = localVariableNameValue + "_" + UUIDUtilClient.uuid(5);
			NodeList localVariableNode = ((Element) (generatedClauseEntryNode))
					.getElementsByTagName("localVariableName");
			if (localVariableNode != null) {
				Element localVariableElement = (Element) localVariableNode.item(0);
				if (localVariableElement != null) {
					localVariableElement.setAttribute(VALUE, localVariableNameValue);
				} else {
					localVariableElement = generatedClauseEntryNode.getOwnerDocument()
							.createElement("localVariableName");
					localVariableElement.setAttribute(VALUE, localVariableNameValue);
					generatedClauseEntryNode.insertBefore(localVariableElement,
							generatedClauseEntryNode.getFirstChild());
				}
			} else {
				Element localVariableElement = generatedClauseEntryNode.getOwnerDocument()
						.createElement("localVariableName");
				localVariableElement.setAttribute(VALUE, localVariableNameValue);
				generatedClauseEntryNode.insertBefore(localVariableElement, generatedClauseEntryNode.getFirstChild());
			}
		} else {
			if (getSubTreeNodeMap().containsKey(clauseNodes.getAttributes().getNamedItem("uuid").getNodeValue())) {
				localVariableNameValue = findSubTreeDisplayName(clauseNodes);
			}
		}
		return localVariableNameValue;
	}

	/**
	 * Method to Generate precondition Tag.
	 * 
	 * @param measureObDefinitionElement
	 *            - DOM Element
	 * @param preConditionJoinExpressionValue
	 *            - Expression String.
	 * @param elementRefList
	 *            - Element Ref List.
	 */
	private void generatePreCondition(Element measureObDefinitionElement, String preConditionJoinExpressionValue,
			List<Node> elementRefList) {
		// precondition is created if and only if more than 1 qdm is applied.
		if (((elementRefList.size() > 1) && (preConditionJoinExpressionValue != null)
				&& (preConditionJoinExpressionValue.length() > 0)) || (clauseLogicHasElementRef)) {
			Element preConditionElement = measureObDefinitionElement.getOwnerDocument().createElement("precondition");
			preConditionElement.setAttribute(TYPE_CODE, "PRCN");
			Element joinElement = measureObDefinitionElement.getOwnerDocument().createElement("join");
			joinElement.setAttribute(CLASS_CODE, "OBS");
			joinElement.setAttribute(MOOD_CODE, "DEF");
			Element valueElement = measureObDefinitionElement.getOwnerDocument().createElement("value");
			valueElement.setAttribute(XSI_TYPE, "ED");
			valueElement.setAttribute(VALUE, preConditionJoinExpressionValue);
			joinElement.appendChild(valueElement);
			preConditionElement.appendChild(joinElement);
			measureObDefinitionElement.appendChild(preConditionElement);
		}
	}

	/**
	 * Method to Generate Value/Expression tags.This method also returns
	 * precondition Expression value.
	 * 
	 * @param elementRefList
	 *            -List
	 * @param measureObDefinitionElement
	 *            -Element
	 * @param clauseNodes
	 *            -Node
	 * @param clauseLocalVariableName
	 *            - Local Variable Name.
	 * @throws XPathExpressionException
	 *             Exception
	 * @return String -String.
	 */
	private String generateValueAndExpressionTag(List<Node> elementRefList, Element measureObDefinitionElement,
			Node clauseNodes, String clauseLocalVariableName) throws XPathExpressionException {
		Node valueExpressionList = (measureObDefinitionElement).getElementsByTagName("expression").item(0);
		Element expressionElement = null;
		if (valueExpressionList != null) {
			expressionElement = (Element) valueExpressionList;
		} else {
			Element valueElement = measureObDefinitionElement.getOwnerDocument().createElement("value");
			valueElement.setAttribute(XSI_TYPE, "PQ");
			expressionElement = measureObDefinitionElement.getOwnerDocument().createElement("expression");
			valueElement.appendChild(expressionElement);
			measureObDefinitionElement.appendChild(valueElement);
		}
		String expressionValue = new String();
		if (expressionElement.getAttributes().getNamedItem("value") != null) {
			expressionValue = expressionElement.getAttributes().getNamedItem("value").getNodeValue();
		}
		String preConditionJoinExpressionValue = new String();
		for (Node node : elementRefList) {
			String qdmUUID = node.getAttributes().getNamedItem("id").getNodeValue();
			String xPath = "/measure/elementLookUp/qdm[@uuid ='" + qdmUUID + "']";
			Node qdmNode = me.getSimpleXMLProcessor().findNode(me.getSimpleXMLProcessor().getOriginalDoc(), xPath);
			String dataType = qdmNode.getAttributes().getNamedItem("datatype").getNodeValue();
			String qdmName = qdmNode.getAttributes().getNamedItem(NAME).getNodeValue();
			String ext = qdmName + "_" + dataType;
			if (qdmNode.getAttributes().getNamedItem(INSTANCE) != null) {
				ext = qdmNode.getAttributes().getNamedItem(INSTANCE).getNodeValue() + "_" + ext;
			}
			String qdmAttributeName = "";
			ext = StringUtils.deleteWhitespace(ext);
			String root = node.getAttributes().getNamedItem(ID).getNodeValue();
			if (node.hasChildNodes()) {
				ext = node.getFirstChild().getAttributes().getNamedItem("attrUUID").getNodeValue();
				qdmAttributeName = node.getFirstChild().getAttributes().getNamedItem(NAME).getNodeValue();
			}
			Node idNodeQDM = me.getHQMFXmlProcessor().findNode(me.getHQMFXmlProcessor().getOriginalDoc(),
					"//entry/*/id[@root='" + root + "'][@extension=\"" + ext + "\"]");
			if (idNodeQDM != null) {
				Node entryNodeForElementRef = idNodeQDM.getParentNode().getParentNode();
				String localVariableName = clauseLocalVariableName;
				if (clauseLocalVariableName == null) {
					localVariableName = entryNodeForElementRef.getFirstChild().getAttributes().getNamedItem("value")
							.getNodeValue();
				}
				String attributeMapping = "";
				// if the parent of elementRef is setOp then we'll not
				// be generating attribute Mapping for that particular QDM
				// in value Expression
				if ((qdmAttributeName.length() != 0) && !node.getParentNode().getNodeName().equals("setOp")) {
					attributeMapping = getQdmAttributeMapppingDotNotation(qdmAttributeName, dataType);
				}
				if (expressionValue.length() == 0) {
					expressionValue = localVariableName;
					preConditionJoinExpressionValue = localVariableName + ".getPatient().id";
				} else {
					expressionValue += " - " + localVariableName;
					preConditionJoinExpressionValue += " == " + localVariableName + ".getPatient().id";
				}
				// appending attributeMapping for expressionValue
				if ((attributeMapping != null) && (attributeMapping.length() != 0)) {
					expressionValue += "." + attributeMapping;
				}
			}
		}
		expressionElement.setAttribute(VALUE, expressionValue);
		return preConditionJoinExpressionValue;
	}

	/**
	 * Method to generate component and MeasureObservationSection default tags.
	 * 
	 * @param outputProcessor
	 *            - XmlProcessor.
	 * @return - Node.
	 * @throws XPathExpressionException
	 *             -Exception
	 */
	private Node createMeasureObservationSection(XmlProcessor outputProcessor) throws XPathExpressionException {
		Node measureObservationSection = outputProcessor.findNode(outputProcessor.getOriginalDoc(),
				"//component/measureObservationSection");
		if (measureObservationSection == null) {
			return createNewMeasureObservationSection(outputProcessor);
		} else {
			return measureObservationSection.getParentNode();
		}
	}

	public Node createNewMeasureObservationSection(XmlProcessor outputProcessor) {
		Element componentElement = outputProcessor.getOriginalDoc().createElement("component");
		Attr nameSpaceAttr = outputProcessor.getOriginalDoc().createAttribute("xmlns:xsi");
		nameSpaceAttr.setNodeValue(nameSpace);
		componentElement.setAttributeNodeNS(nameSpaceAttr);
		Node measureObSectionElem = outputProcessor.getOriginalDoc().createElement("measureObservationSection");
		Element templateId = outputProcessor.getOriginalDoc().createElement(TEMPLATE_ID);
		measureObSectionElem.appendChild(templateId);
		Element itemChild = outputProcessor.getOriginalDoc().createElement(ITEM);
		itemChild.setAttribute(ROOT, "2.16.840.1.113883.10.20.28.2.4");
		itemChild.setAttribute(EXTENSION, MEASURE_OBSERVATION_EXTENSION_VALUE);
		templateId.appendChild(itemChild);
		Element idElement = outputProcessor.getOriginalDoc().createElement(ID);
		idElement.setAttribute(ROOT, UUIDUtilClient.uuid());
		idElement.setAttribute(EXTENSION, "MeasureObservations");
		measureObSectionElem.appendChild(idElement);
		Element codeElem = outputProcessor.getOriginalDoc().createElement(CODE);
		codeElem.setAttribute(CODE, "57027-5");
		codeElem.setAttribute(CODE_SYSTEM, "2.16.840.1.113883.6.1");
		codeElem.setAttribute(CODE_SYSTEM_NAME, "LOINC");
		measureObSectionElem.appendChild(codeElem);
		Element titleElem = outputProcessor.getOriginalDoc().createElement(TITLE);
		titleElem.setAttribute(VALUE, "Measure Observation Section");
		measureObSectionElem.appendChild(titleElem);

		// creating text for PopulationCriteria
		Element textElem = outputProcessor.getOriginalDoc().createElement("text");

		measureObSectionElem.appendChild(textElem);
		componentElement.appendChild(measureObSectionElem);
		outputProcessor.getOriginalDoc().getDocumentElement().appendChild(componentElement);
		return componentElement;
	}

	/**
	 * Get Measure Scoring type.
	 *
	 * @param me
	 *            - MeasureExport
	 * @throws XPathExpressionException
	 *             - {@link Exception}
	 */
	private void getMeasureScoringType(MeasureExport me) throws XPathExpressionException {
		String xPathScoringType = "/measure/measureDetails/scoring/text()";
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
		scoringType = (String) xPath.evaluate(xPathScoringType, me.getSimpleXMLProcessor().getOriginalDoc(),
				XPathConstants.STRING);
	}

	/**
	 * Method to populate clause UUID and displayName.
	 * 
	 * @param me
	 *            - MeasureExport
	 * @throws XPathExpressionException
	 *             - {@link Exception}
	 */
	private void generateClauseLogicMap(MeasureExport me) throws XPathExpressionException {
		String xPath = "/measure/subTreeLookUp/subTree";
		NodeList subTreeNodeList = me.getSimpleXMLProcessor().findNodeList(me.getSimpleXMLProcessor().getOriginalDoc(),
				xPath);
		for (int i = 0; i < subTreeNodeList.getLength(); i++) {
			String uuid = subTreeNodeList.item(i).getAttributes().getNamedItem(UUID).getNodeValue();
			Node firstChildNode = subTreeNodeList.item(i).getFirstChild();
			if (firstChildNode != null) {
				clauseLogicMap.put(uuid, firstChildNode);
			}
		}
	}

	/**
	 * Method to populate all measure groupings in measureGroupingMap.
	 *
	 * @param me
	 *            - MeasureExport
	 * @throws XPathExpressionException
	 *             - {@link Exception}
	 */
	private void getAllMeasureGroupings(MeasureExport me) throws XPathExpressionException {
		String xPath = "/measure/measureGrouping/group";
		NodeList measureGroupings = me.getSimpleXMLProcessor().findNodeList(me.getSimpleXMLProcessor().getOriginalDoc(),
				xPath);
		for (int i = 0; i < measureGroupings.getLength(); i++) {
			String measureGroupingSequence = measureGroupings.item(i).getAttributes().getNamedItem("sequence")
					.getNodeValue();
			NodeList childNodeList = measureGroupings.item(i).getChildNodes();
			measureGroupingMap.put(Integer.parseInt(measureGroupingSequence), childNodeList);
		}
	}
}
