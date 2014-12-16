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
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// TODO: Auto-generated Javadoc
/**
 * The Class HQMFPopulationLogicGenerator.
 */
public class HQMFMeasureObservationLogicGenerator extends HQMFClauseLogicGenerator {
	
	/** The clause logic map. */
	private Map<String, Node> clauseLogicMap = new HashMap<String, Node>();
	/** The measure grouping map. */
	private Map<String, NodeList> measureGroupingMap = new HashMap<String, NodeList>();
	/** The elementRefList. */
	private List<Node> elementRefList;
	/** The MeasureExport object. */
	private MeasureExport me;
	/** The Measure Scoring type. */
	private String scoringType;
	/** The denominator. */
	private Node denominator;
	/** The numerator. */
	private Node numerator;
	/**
	 * Array of Functional Ops that can be used in Measure Observation.
	 */
	private static final Map<String, String> FUNCTIONAL_OPS = new HashMap<String, String>();
	static {
		FUNCTIONAL_OPS.put("MAX", "MAX");
		FUNCTIONAL_OPS.put("MIN", "MIN");
		FUNCTIONAL_OPS.put("SUM", "SUM");
		FUNCTIONAL_OPS.put("AVG", "AVERAGE");
		FUNCTIONAL_OPS.put("COUNT", "COUNT");
		FUNCTIONAL_OPS.put("MEDIAN", "MEDIAN");
		FUNCTIONAL_OPS.put("DATETIMEDIFF", null);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.simplexml.hqmf.HQMFClauseLogicGenerator#generate(mat.model.clause.MeasureExport)
	 */
	@Override
	public String generate(MeasureExport me) throws Exception {
		this.me = me;
		getMeasureScoringType(me);
		generateClauseLogicMap(me);
		getAllMeasureGroupings(me);
		generateMeasureObSection(me);
		return null;
	}
	/**
	 * Get DotNotation from templates.xml.
	 * @param attributeName -String
	 * @param dataTypeName - String
	 * @throws XPathExpressionException -Exception
	 * @return String dot notation.
	 */
	private String getQdmAttributeMapppingDotNotation(String attributeName, String dataTypeName) throws XPathExpressionException {
		XmlProcessor templateXMLProcessor = TemplateXMLSingleton.getTemplateXmlProcessor();
		String xPath = "/templates/attributeMappings/attributeMapping[@qdmAttribute='" + attributeName + "']";
		Node attributeMappingNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc(), xPath);
		if (attributeMappingNode == null) {
			xPath = "/templates/attributeMappings/attributeMapping[@qdmAttribute='"
					+ attributeName + "'  and @datatypes = '" + dataTypeName.toLowerCase() + "']";
			attributeMappingNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc(), xPath);
		}
		return attributeMappingNode.getAttributes().getNamedItem("dotNotation").getNodeValue();
	}
	
	/**
	 * Method to generate MeasureObservation Criteria Section.
	 * @param me - MeasureExport
	 * @throws XPathExpressionException - Exception
	 */
	private void generateMeasureObSection(MeasureExport me) throws XPathExpressionException {
		for (String key : measureGroupingMap.keySet()) {
			NodeList groupingChildList = measureGroupingMap.get(key);
			for (int i = 0; i < groupingChildList.getLength(); i++) {
				String popType = groupingChildList.item(i).getAttributes().getNamedItem(TYPE).getNodeValue();
				switch(popType) {
					case "measureObservation" :
						Node measureObSectionComponentElement = createMeasureObservationSection(me.getHQMFXmlProcessor());
						generateMeasureObDefinition(groupingChildList.item(i)
								, measureObSectionComponentElement , me);
						break;
					case "denominator" :
						denominator = groupingChildList.item(i);
						break;
					case "numerator" :
						numerator = groupingChildList.item(i);
						break;
					default:
						//do nothing.
						break;
				}
			}
		}
		
	}
	/**
	 * Method to generate default criteriaTag for all population types included in measure grouping.
	 * @param item - Node
	 * @param measureObservationSecElement - Element
	 * @param me - MeasureExport
	 * @throws XPathExpressionException - Exception
	 */
	private void generateMeasureObDefinition(Node item, Node measureObservationSecElement
			, MeasureExport me) throws XPathExpressionException {
		Document doc = measureObservationSecElement.getOwnerDocument();
		Comment comment = doc.createComment("Definition for "
				+ item.getAttributes().getNamedItem("displayName").getNodeValue());
		
		Element definitionElement = doc.createElement("definition");
		Element measureObDefinitionElement = doc.createElement("measureObservationDefinition");
		measureObDefinitionElement.setAttribute(CLASS_CODE, "OBS");
		measureObDefinitionElement.setAttribute(MOOD_CODE, "DEF");
		Element codeElem = doc.createElement(CODE);
		codeElem.setAttribute(CODE, "AGGREGATE");
		codeElem.setAttribute(CODE_SYSTEM, "2.16.840.1.113883.5.4");
		measureObDefinitionElement.appendChild(codeElem);
		generateLogicForMeasureObservation(item, measureObDefinitionElement);
		checkForScoringTypeForAssociation(item,measureObDefinitionElement);
		//for Item Count
		generateItemCountForMrsObs(me, item, measureObDefinitionElement);
		definitionElement.appendChild(measureObDefinitionElement);
		Element measurObSectionElement = (Element) measureObservationSecElement.getFirstChild();
		measurObSectionElement.appendChild(comment);
		measurObSectionElement.appendChild(definitionElement);
	}
	
	
	/**
	 * Generate item count for mrs obs.
	 *
	 * @param me the me
	 * @param item the item
	 * @param measureObDefinitionElement the measure ob definition element
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateItemCountForMrsObs(MeasureExport me, Node item,
			Element measureObDefinitionElement) throws XPathExpressionException {
		if (item.getChildNodes() != null) {
			for (int i = 0; i < item.getChildNodes().getLength(); i++) {
				Node childNode = item.getChildNodes().item(i);
				String nodeType = childNode.getNodeName();
				switch (nodeType) {
				case "itemCount":
					generateItemCountForMrsObs(me, childNode, measureObDefinitionElement);
					break;
				case "elementRef":
					generateItemCountElementRef(me, measureObDefinitionElement, childNode, me.getHQMFXmlProcessor());
				default:
					break;
				}
			}
		}
		
	}
	/**
	 * Check for scoring type for association.
	 *
	 * @param item the item
	 * @param measureObDefinitionElement the measure ob definition element
	 */
	private void checkForScoringTypeForAssociation(Node item,
			Element measureObDefinitionElement) {
		String nodeType = item.getAttributes().getNamedItem(TYPE).getNodeValue();
		String associatedType="";
		if (nodeType.equalsIgnoreCase("measureObservation")&& (scoringType.equalsIgnoreCase("Ratio") 
				|| scoringType.equalsIgnoreCase("Continuous Variable"))) {
			if (item.getAttributes().getNamedItem("associatedPopulationUUID") != null) {
				Document mainDocument = measureObDefinitionElement.getOwnerDocument();
				Element componentOfElement = mainDocument.createElement("componentOf");
				componentOfElement.setAttribute(TYPE_CODE, "COMP");
			   item.getAttributes().getNamedItem("associatedPopulationUUID").getNodeValue();
				Element criteriaRef = mainDocument.createElement("criteriaReference");
				criteriaRef.setAttribute(CLASS_CODE, "OBS");
				criteriaRef.setAttribute(MOOD_CODE, "EVN");
				Element idElement = mainDocument.createElement(ID);
				idElement.setAttribute(ROOT, item.getAttributes()
						.getNamedItem("associatedPopulationUUID").getNodeValue());
				if(item.getAttributes().getNamedItem("associatedPopulationUUID").getNodeValue().equals(
						denominator.getAttributes().getNamedItem("uuid").getNodeValue())){
					associatedType = denominator.getAttributes().getNamedItem("type").getNodeValue();
				} else if(item.getAttributes().getNamedItem("associatedPopulationUUID").getNodeValue().equals(
						numerator.getAttributes().getNamedItem("uuid").getNodeValue())){
					associatedType = numerator.getAttributes().getNamedItem("type").getNodeValue();
				}
				idElement.setAttribute("extension", StringUtils.deleteWhitespace(associatedType));
				Comment comment = mainDocument.createComment("Measure Observation Associated with "+associatedType);
				criteriaRef.appendChild(idElement);
				componentOfElement.appendChild(criteriaRef);
				measureObDefinitionElement.appendChild(comment);
				measureObDefinitionElement.appendChild(componentOfElement);
			}
			
		}
		
	}
	
	/**
	 * @param item - Node
	 * @param measureObDefinitionElement - Element
	 * @throws XPathExpressionException -Exception.
	 */
	private void generateLogicForMeasureObservation(Node item, Element measureObDefinitionElement)
			throws XPathExpressionException {
		if ((item != null) && (item.getChildNodes() != null)) {
			NodeList childNodes = item.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node childNode = childNodes.item(i);
				if("subTreeRef".equals(childNode.getNodeName())){
					String subTreeUUID = childNode.getAttributes().getNamedItem("id").getNodeValue();
					Node clauseNodes = clauseLogicMap.get(subTreeUUID);
					if (clauseNodes != null) {
						generateClauseLogic(clauseNodes , measureObDefinitionElement);
						}
					}
				}
			}
		}
	/**
	 * Method to generate Clause Logic used inside MeasureObservation.
	 * @param clauseNodes -Node
	 * @param measureObDefinitionElement -Element
	 * @throws XPathExpressionException -Exception.
	 */
	private void generateClauseLogic(Node clauseNodes, Element measureObDefinitionElement) throws XPathExpressionException {
		String clauseNodeName = clauseNodes.getAttributes().getNamedItem("displayName").getNodeValue();
		// No Method code for DATETIMEDIFF is added as per stan's examples.
		if (FUNCTIONAL_OPS.containsKey(clauseNodeName)) {
			elementRefList = findAllElementRefsUsed(clauseNodes, new ArrayList<Node>());
			String preConditionJoinExpressionValue = null;
			if (elementRefList.size() > 0) {
				preConditionJoinExpressionValue = generateValueAndExpressionTag(elementRefList
						, measureObDefinitionElement, clauseNodes);
			}
			if (FUNCTIONAL_OPS.get(clauseNodeName) != null) {
				Element methodCodeElement = measureObDefinitionElement.getOwnerDocument().createElement("methodCode");
				Element itemElement = measureObDefinitionElement.getOwnerDocument().createElement("item");
				itemElement.setAttribute(CODE, FUNCTIONAL_OPS.get(clauseNodeName));
				itemElement.setAttribute(CODE_SYSTEM, "2.16.840.1.113883.5.4");
				methodCodeElement.appendChild(itemElement);
				measureObDefinitionElement.appendChild(methodCodeElement);
			}
			if ((preConditionJoinExpressionValue != null)
					&& (preConditionJoinExpressionValue.length() > 0)) {
				Element preConditionElement = measureObDefinitionElement.getOwnerDocument().createElement("precondition");
				preConditionElement.setAttribute(TYPE_CODE, "PRCN");
				Element joinElement = measureObDefinitionElement.getOwnerDocument().createElement("join");
				joinElement.setAttribute(CLASS_CODE, "OBS");
				joinElement.setAttribute(MOOD_CODE, "DEF");
				Element valueElement = measureObDefinitionElement.getOwnerDocument().createElement("value");
				valueElement.setAttribute(XSI_TYPE, "ED");
				Element valueExpressionElement = measureObDefinitionElement.getOwnerDocument().createElement("expression");
				valueExpressionElement.setAttribute(VALUE, preConditionJoinExpressionValue);
				valueElement.appendChild(valueExpressionElement);
				joinElement.appendChild(valueElement);
				preConditionElement.appendChild(joinElement);
				measureObDefinitionElement.appendChild(preConditionElement);
			}
		}
	}
	/**
	 * Method to Generate Value/Expression tags.This method also returns preCondition Expression value.
	 * @param elementRefList -List
	 * @param measureObDefinitionElement -Element
	 * @param clauseNodes -Node
	 * @throws XPathExpressionException Exception
	 * @return String -String.
	 */
	private String generateValueAndExpressionTag(List<Node> elementRefList
			, Element measureObDefinitionElement, Node clauseNodes) throws XPathExpressionException {
		Element valueElement = measureObDefinitionElement.getOwnerDocument().createElement("value");
		valueElement.setAttribute(XSI_TYPE, "PQ");
		Element expressionElement = measureObDefinitionElement.getOwnerDocument().createElement("expression");
		String expressionValue = new String();
		String preConditionJoinExpressionValue = new String();
		for (Node node: elementRefList) {
			String qdmUUID = node.getAttributes().getNamedItem("id").getNodeValue();
			String xPath = "/measure/elementLookUp/qdm[@uuid ='" + qdmUUID + "']";
			Node qdmNode = me.getSimpleXMLProcessor().findNode(me.getSimpleXMLProcessor().getOriginalDoc(), xPath);
			String dataType = qdmNode.getAttributes().getNamedItem("datatype")
					.getNodeValue();
			String qdmName = qdmNode.getAttributes().getNamedItem(NAME).getNodeValue();
			String ext = qdmName + "_" + dataType;
			if (qdmNode.getAttributes().getNamedItem("instance") != null) {
				ext = qdmNode.getAttributes().getNamedItem("instance").getNodeValue() + "_" + ext;
			}
			String qdmAttributeName = "";
			ext = StringUtils.deleteWhitespace(ext);
			String root = node.getAttributes().getNamedItem(ID).getNodeValue();
			if (node.hasChildNodes())  {
				ext = node.getFirstChild().getAttributes().getNamedItem("attrUUID").getNodeValue();
				qdmAttributeName = node.getFirstChild().getAttributes().getNamedItem("name").getNodeValue();
			}
			Node idNodeQDM = me.getHQMFXmlProcessor().findNode(me.getHQMFXmlProcessor().getOriginalDoc()
					, "//entry/*/id[@root='" + root + "'][@extension='" + ext + "']");
			if (idNodeQDM != null) {
				Node entryNodeForElementRef = idNodeQDM.getParentNode().getParentNode();
				String localVariableName = entryNodeForElementRef.getFirstChild().getAttributes()
						.getNamedItem("value").getNodeValue();
				String attributeMapping = "";
				//if the parent of elementRef is setOp then we'll not
				//be generating attribute Mapping for that particular QDM
				//in value Expression
				if ((qdmAttributeName.length() != 0)
						&& !node.getParentNode().getNodeName().equals("setOp")) {
					attributeMapping = getQdmAttributeMapppingDotNotation(qdmAttributeName, dataType);
				}
				if (expressionValue.length() == 0) {
					expressionValue = localVariableName;
					preConditionJoinExpressionValue = localVariableName + ".getPatient().id";
				} else {
					expressionValue += " - " + localVariableName;
					preConditionJoinExpressionValue += " == " + localVariableName + ".getPatient().id";
				}
				//appending attributeMapping for expressionValue
				if (attributeMapping.length() != 0) {
					expressionValue += "." + attributeMapping;
				}
			} else {
				// add check for measurement period.
			}
		}
		expressionElement.setAttribute(VALUE, expressionValue);
		valueElement.appendChild(expressionElement);
		measureObDefinitionElement.appendChild(valueElement);
		return preConditionJoinExpressionValue;
	}
	/**
	 * Method to find all QDM's used in a clause.
	 * @param childNode - Node
	 * @param elementRefList - List
	 * @return List of QDm Node
	 */
	private List<Node> findAllElementRefsUsed(Node childNode, List<Node> elementRefList) {
		if (childNode.hasChildNodes()) {
			NodeList childNodeList = childNode.getChildNodes();
			for (int i = 0; i < childNodeList.getLength(); i++) {
				Node node = childNodeList.item(i);
				if (node.getNodeName().equalsIgnoreCase("elementRef")) {
					System.out.println("Size of elementRefList ====== " + elementRefList.size());
					elementRefList.add(node);
				} else if (node.getNodeName().equalsIgnoreCase("subTreeRef")) {
					Node subTreeRefNodeLogic = clauseLogicMap.get(node.getAttributes()
							.getNamedItem("id").getNodeValue());
					if (subTreeRefNodeLogic.getNodeName().equalsIgnoreCase("elementRef")) {
						System.out.println("Size of elementRefList ====== " + elementRefList.size());
						elementRefList.add(subTreeRefNodeLogic);
					} else {
						findAllElementRefsUsed(subTreeRefNodeLogic, elementRefList);
					}
				}
				if (node.hasChildNodes()) {
					findAllElementRefsUsed(node, elementRefList);
				}
			}
		}
		return elementRefList;
	}
	/**
	 * Method to generate component and MeasureObservationSection default tags.
	 * @param outputProcessor - XmlProcessor.
	 * @return - Node.
	 * @throws XPathExpressionException -Exception
	 */
	private Node createMeasureObservationSection(XmlProcessor outputProcessor) throws XPathExpressionException {
		Node measureObservationSection = outputProcessor.findNode(
				outputProcessor.getOriginalDoc(), "//component/measureObservationSection");
		if (measureObservationSection == null) {
			Element componentElement = outputProcessor.getOriginalDoc().createElement("component");
			Attr nameSpaceAttr = outputProcessor.getOriginalDoc()
					.createAttribute("xmlns:xsi");
			nameSpaceAttr.setNodeValue(nameSpace);
			componentElement.setAttributeNodeNS(nameSpaceAttr);
			Node measureObSectionElem = outputProcessor.getOriginalDoc()
					.createElement("measureObservationSection");
			Element templateId = outputProcessor.getOriginalDoc().createElement(TEMPLATE_ID);
			measureObSectionElem.appendChild(templateId);
			Element itemChild = outputProcessor.getOriginalDoc().createElement(ITEM);
			itemChild.setAttribute(ROOT, "2.16.840.1.113883.10.20.28.2.4");
			templateId.appendChild(itemChild);
			Element idElement = outputProcessor.getOriginalDoc()
					.createElement(ID);
			idElement.setAttribute(ROOT, UUIDUtilClient.uuid());
			idElement.setAttribute("extension", "MeasureObservations");
			measureObSectionElem.appendChild(idElement);
			Element codeElem = outputProcessor.getOriginalDoc()
					.createElement(CODE);
			codeElem.setAttribute(CODE, "57027-5");
			codeElem.setAttribute(CODE_SYSTEM, "2.16.840.1.113883.6.1");
			measureObSectionElem.appendChild(codeElem);
			Element titleElem = outputProcessor.getOriginalDoc()
					.createElement(TITLE);
			titleElem.setAttribute(VALUE, "Measure Observation Section");
			measureObSectionElem.appendChild(titleElem);
			// creating text for PopulationCriteria
			Element textElem = outputProcessor.getOriginalDoc()
					.createElement("text");
			textElem.setAttribute(VALUE, "Measure Observation text");
			measureObSectionElem.appendChild(textElem);
			componentElement.appendChild(measureObSectionElem);
			outputProcessor.getOriginalDoc().getDocumentElement().appendChild(componentElement);
			return componentElement;
		} else {
			return measureObservationSection.getParentNode();
		}
	}
	
	/**
	 * Get Measure Scoring type.
	 *
	 * @param me - MeasureExport
	 * @throws XPathExpressionException - {@link Exception}
	 */
	private void getMeasureScoringType(MeasureExport me) throws XPathExpressionException {
		String xPathScoringType = "/measure/measureDetails/scoring/text()";
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
		scoringType = (String) xPath.evaluate(xPathScoringType, me.getSimpleXMLProcessor().getOriginalDoc(), XPathConstants.STRING);
	}
	/**
	 * Method to populate clause UUID and displayName.
	 * @param me - MeasureExport
	 * @throws XPathExpressionException - {@link Exception}
	 */
	private void generateClauseLogicMap(MeasureExport me) throws XPathExpressionException {
		String xPath = "/measure/subTreeLookUp/subTree";
		NodeList subTreeNodeList = me.getSimpleXMLProcessor().findNodeList(me.getSimpleXMLProcessor().getOriginalDoc(), xPath);
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
	 * @param me - MeasureExport
	 * @throws XPathExpressionException - {@link Exception}
	 */
	private void getAllMeasureGroupings(MeasureExport me) throws XPathExpressionException {
		String xPath = "/measure/measureGrouping/group";
		NodeList measureGroupings = me.getSimpleXMLProcessor().findNodeList(me.getSimpleXMLProcessor().getOriginalDoc(), xPath);
		for (int i = 0; i < measureGroupings.getLength(); i++) {
			String measureGroupingSequence = measureGroupings.item(i).getAttributes().getNamedItem("sequence").getNodeValue();
			NodeList childNodeList = measureGroupings.item(i).getChildNodes();
			measureGroupingMap.put(measureGroupingSequence, childNodeList);
		}
	}
	
}
