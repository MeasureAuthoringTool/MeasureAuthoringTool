package mat.server.simplexml.hqmf;

import java.util.HashMap;
import java.util.Map;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import mat.model.clause.MeasureExport;
import mat.server.util.XmlProcessor;
import mat.shared.UUIDUtilClient;
import org.w3c.dom.Attr;
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
	
	/**
	 * Array of Functional Ops that can be used in Measure Observation.
	 */
	private static final Map<String, String> FUNCTIONAL_OPS = new HashMap<String, String>();
	/** The scoring type. */
	private String scoringType;
	
	/** The initial population. */
	private Node initialPopulation;
	
	static {
		FUNCTIONAL_OPS.put("MAX", "MAX");
		FUNCTIONAL_OPS.put("MIN", "MIN");
		FUNCTIONAL_OPS.put("SUM", "SUM");
		FUNCTIONAL_OPS.put("AVG", "AVERAGE");
		FUNCTIONAL_OPS.put("COUNT", "COUNT");
		FUNCTIONAL_OPS.put("MEDIAN", "MEDIAN");
		FUNCTIONAL_OPS.put("DATETIMEDIFF", "null");
	}
	
	/* (non-Javadoc)
	 * @see mat.server.simplexml.hqmf.HQMFClauseLogicGenerator#generate(mat.model.clause.MeasureExport)
	 */
	@Override
	public String generate(MeasureExport me) throws Exception {
		getMeasureScoringType(me);
		generateClauseLogicMap(me);
		getAllMeasureGroupings(me);
		generateMeasureObSection(me);
		return null;
	}
	/**
	 * @param attributeName
	 * @param dataTypeName
	 * @throws XPathExpressionException
	 */
	private String getQdmAttributeMapppingDotNotation(String attributeName, String dataTypeName) throws XPathExpressionException {
		XmlProcessor templateXMLProcessor = TemplateXMLSingleton.getTemplateXmlProcessor();
		String xPath = "/templates/attributeMappings/attributeMapping[@qdmAttribute='" + attributeName + "']";
		Node attributeMappingNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc(), xPath);
		if (attributeMappingNode == null) {
			xPath = "/templates/attributeMappings/attributeMapping[@qdmAttribute='"
					+ attributeName + "'  and @datatypes = '" + dataTypeName + "']";
			attributeMappingNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc(), xPath);
		}
		return attributeMappingNode.getAttributes().getNamedItem("dotNotation").getNodeValue();
	}
	
	/**
	 * Method to generate population Criteria.
	 * @param me - MeasureExport
	 * @throws XPathExpressionException - Exception
	 */
	private void generateMeasureObSection
	(MeasureExport me) throws XPathExpressionException {
		for (String key : measureGroupingMap.keySet()) {
			Node populationCriteriaComponentElement = createMeasureObservationSection(me.getHQMFXmlProcessor());
			NodeList groupingChildList = measureGroupingMap.get(key);
			for (int i = 0; i < groupingChildList.getLength(); i++) {
				String popType = groupingChildList.item(i).getAttributes().getNamedItem(TYPE).getNodeValue();
				switch(popType) {
					case "measureObservation" :
						generateMeasureObDefinition(groupingChildList.item(i)
								, populationCriteriaComponentElement , me);
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
	 * @param criteriaTagName - String.
	 * @param criteriaTagCodeName - String code value.
	 * @param topLogicalOpName - top Logical op type.
	 * @throws XPathExpressionException - Exception
	 */
	private void generateMeasureObDefinition(Node item, Node measureObservationSecElement
			, MeasureExport me) throws XPathExpressionException {
		Document doc = measureObservationSecElement.getOwnerDocument();
		Element definitionElement = doc.createElement("definition");
		Element measureObDefinitionElement = doc.createElement("measureObservationDefinition");
		measureObDefinitionElement.setAttribute(CLASS_CODE, "OBS");
		measureObDefinitionElement.setAttribute(MOOD_CODE, "DEF");
		Element codeElem = doc.createElement(CODE);
		codeElem.setAttribute(CODE, "AGGREGATE");
		codeElem.setAttribute(CODE_SYSTEM, "2.16.840.1.113883.5.4");
		measureObDefinitionElement.appendChild(codeElem);
		generateLogicForMeasureObservation(item,measureObDefinitionElement);
		definitionElement.appendChild(measureObDefinitionElement);
		measureObservationSecElement.appendChild(definitionElement);
	}
	
	/**
	 * @param item - Node
	 * @param measureObDefinitionElement - Element
	 */
	private void generateLogicForMeasureObservation(Node item, Element measureObDefinitionElement) {
		if ((item != null) && (item.getChildNodes() != null)) {
			NodeList childNodes = item.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node childNode = childNodes.item(i);
				String subTreeUUID = childNode.getAttributes().getNamedItem("id").getNodeValue();
				Node clauseNodes = clauseLogicMap.get(subTreeUUID);
				if (clauseNodes != null) {
					generateClauseLogic(clauseNodes , measureObDefinitionElement);
				}
			}
		}
	}
	/**
	 * @param clauseNodes
	 * @param measureObDefinitionElement
	 */
	private void generateClauseLogic(Node clauseNodes, Element measureObDefinitionElement) {
		String clauseNodeName = clauseNodes.getAttributes().getNamedItem("displayName").getNodeValue();
		if (FUNCTIONAL_OPS.containsKey(clauseNodeName)) {
			NodeList childNodeList = clauseNodes.getChildNodes();
			for (int i = 0; i < childNodeList.getLength(); i++) {
				Node childNode = childNodeList.item(i);
				String childNodeName = childNode.getNodeName();
				switch(childNodeName) {
					case "setOp":
						break;
					case "relOp":
						break;
					case "functionalOp":
						break;
					case "elementRef":
						break;
					case "subTreeRef":
						String subTreeUUID = childNode.getAttributes().getNamedItem("id").getNodeValue();
						Node newClauseNode = clauseLogicMap.get(subTreeUUID);
						if (newClauseNode != null) {
							generateClauseLogic(newClauseNode , measureObDefinitionElement);
						}
						break;
					default:
						break;
				}
			}
		}
	}
	/**
	 * Method to generate component and MeasureObservationSection default tags.
	 * @param outputProcessor - XmlProcessor.
	 * @return - Node.
	 */
	private Node createMeasureObservationSection(XmlProcessor outputProcessor) {
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
	}
	
	/**
	 * Get Measure Scoring type.
	 *
	 * @param me - MeasureExport
	 * @return the measure scoring type
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
	 * @return the all measure groupings
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
