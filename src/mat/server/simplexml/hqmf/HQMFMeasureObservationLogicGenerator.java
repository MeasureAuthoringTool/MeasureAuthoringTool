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
	
	private List<Node> elementRefList;// = new ArrayList<Node>();
	
	/** The scoring type. */
	private String scoringType;
	
	/** The initial population. */
	private Node initialPopulation;
	MeasureExport me;
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
			Node measureObSectionComponentElement = createMeasureObservationSection(me.getHQMFXmlProcessor());
			NodeList groupingChildList = measureGroupingMap.get(key);
			for (int i = 0; i < groupingChildList.getLength(); i++) {
				String popType = groupingChildList.item(i).getAttributes().getNamedItem(TYPE).getNodeValue();
				switch(popType) {
					case "measureObservation" :
						generateMeasureObDefinition(groupingChildList.item(i)
								, measureObSectionComponentElement , me);
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
		Comment comment = doc.createComment("Definition for "+item.getAttributes().getNamedItem("displayName").getNodeValue());
		
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
		Element measurObSectionElement = (Element) measureObservationSecElement.getFirstChild();
		measurObSectionElement.appendChild(comment);
		measurObSectionElement.appendChild(definitionElement);
	}
	
	/**
	 * @param item - Node
	 * @param measureObDefinitionElement - Element
	 * @throws XPathExpressionException
	 */
	private void generateLogicForMeasureObservation(Node item, Element measureObDefinitionElement) throws XPathExpressionException {
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
	 * @throws XPathExpressionException
	 */
	private void generateClauseLogic(Node clauseNodes, Element measureObDefinitionElement) throws XPathExpressionException {
		String clauseNodeName = clauseNodes.getAttributes().getNamedItem("displayName").getNodeValue();
		// No Method code for DATETIMEDIFF is added as per stan's examples.
		if (FUNCTIONAL_OPS.containsKey(clauseNodeName)) {
			if (FUNCTIONAL_OPS.get(clauseNodeName) != null) {
				Element methodCodeElement = measureObDefinitionElement.getOwnerDocument().createElement("methodCode");
				Element itemElement = measureObDefinitionElement.getOwnerDocument().createElement("item");
				itemElement.setAttribute(CODE, FUNCTIONAL_OPS.get(clauseNodeName));
				itemElement.setAttribute(CODE_SYSTEM, "2.16.840.1.113883.5.4");
				methodCodeElement.appendChild(itemElement);
				measureObDefinitionElement.appendChild(methodCodeElement);
			}
			elementRefList = findAllElementRefsUsed(clauseNodes, new ArrayList<Node>());
			if(elementRefList.size() >0){
				generateValueAndExpressionTag(elementRefList, measureObDefinitionElement, clauseNodes);
			}
		}
	}
	/**
	 * @param elementRefList
	 * @param measureObDefinitionElement
	 * @param clauseNodes
	 * @throws XPathExpressionException
	 */
	private void generateValueAndExpressionTag(List<Node> elementRefList, Element measureObDefinitionElement, Node clauseNodes) throws XPathExpressionException {
		Element valueElement = measureObDefinitionElement.getOwnerDocument().createElement("value");
		valueElement.setAttribute(XSI_TYPE, "PQ");
		Element expressionElement = measureObDefinitionElement.getOwnerDocument().createElement("expression");
		String expressionValue = new String();
		for(Node node: elementRefList){
			String qdmUUID = node.getAttributes().getNamedItem("id").getNodeValue();
			String xPath = "/measure/elementLookUp/qdm[@uuid ='"+qdmUUID+"']";
			Node qdmNode = me.getSimpleXMLProcessor().findNode(me.getSimpleXMLProcessor().getOriginalDoc(), xPath);
			String dataType = qdmNode.getAttributes().getNamedItem("datatype")
					.getNodeValue();
			String qdmName = qdmNode.getAttributes().getNamedItem(NAME).getNodeValue();
			String ext = qdmName + "_" + dataType;
			if(qdmNode.getAttributes().getNamedItem("instance") != null){
				ext = qdmNode.getAttributes().getNamedItem("instance").getNodeValue() +"_" + ext;
			}
			ext = StringUtils.deleteWhitespace(ext);
			String root = node.getAttributes().getNamedItem(ID).getNodeValue();
			if(node.hasChildNodes()) {
				ext = node.getFirstChild().getAttributes().getNamedItem("attrUUID").getNodeValue();
			}
			Node idNodeQDM = me.getHQMFXmlProcessor().findNode(me.getHQMFXmlProcessor().getOriginalDoc()
					, "//entry/*/id[@root='"+root+"'][@extension='"+ext+"']");
			if(idNodeQDM != null){
				Node entryNodeForElementRef = idNodeQDM.getParentNode().getParentNode();
				String localVariableName = entryNodeForElementRef.getFirstChild().getAttributes().getNamedItem("value").getNodeValue();
				if(expressionValue.length() ==0) {
					expressionValue = localVariableName;
				} else {
					expressionValue = expressionValue + " - " + localVariableName;
				}
				
			} else {
				// add check for measurement period.
			}
		}
		expressionElement.setAttribute(VALUE, expressionValue);
		valueElement.appendChild(expressionElement);
		measureObDefinitionElement.appendChild(valueElement);
		
	}
	private List<Node> findAllElementRefsUsed(Node childNode, List<Node> elementRefList) {
		//elementRefList = new ArrayList<Node>();
		if(childNode.hasChildNodes()) {
			NodeList childNodeList = childNode.getChildNodes();
			for(int i=0; i< childNodeList.getLength();i++){
				Node node = childNodeList.item(i);
				if(node.getNodeName().equalsIgnoreCase("elementRef")){
					System.out.println("Size of elementRefList ====== "+ elementRefList.size());
					elementRefList.add(node);
				}
				if(node.hasChildNodes()){
					findAllElementRefsUsed(node,elementRefList);
				}
			}
		}
		return elementRefList;
	}
	/**
	 * Method to generate component and MeasureObservationSection default tags.
	 * @param outputProcessor - XmlProcessor.
	 * @return - Node.
	 * @throws XPathExpressionException
	 */
	private Node createMeasureObservationSection(XmlProcessor outputProcessor) throws XPathExpressionException {
		
		Node measureObservationSection = outputProcessor.findNode(outputProcessor.getOriginalDoc(), "//component/measureObservationSection");
		
		if(measureObservationSection == null) {
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
			return measureObservationSection;
		}
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
