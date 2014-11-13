package mat.server.simplexml.hqmf;

import java.util.HashMap;
import java.util.Map;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import mat.model.clause.MeasureExport;
import mat.server.util.XmlProcessor;
import mat.shared.UUIDUtilClient;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class HQMFPopulationLogicGenerator extends HQMFClauseLogicGenerator {
	private Map<String, String> clauseLogicMap = new HashMap<String, String>();
	private Map<String, NodeList> measureGroupingMap = new HashMap<String, NodeList>();
	private String scoringType;
	private Node initialPopulation;
	@Override
	public String generate(MeasureExport me) throws Exception {
		/**1. Fetch all Clause Logic HQMF in MAP.
		   2. Fetch all groupings.
		   3. For Each Grouping :
		      1.Generate component tag.
			  2.Generate populationCriteriaSection tag.
			  3.Generate id tag with root and extension as attributes
			  4. Generate code tag with code and codesystem as attributes.
			  5. Generate title tag with value as attribute.
			  6. Generate text tag with value as attribute defining the populations used in current section.
			  7. For each population used :
			     1.Generate component tag as child of populationCriteriaSection with typeCode COMP.
			     2.Generate for ex : initialPopulationCriteria tag for IP with classCode="OBS" moodCode="EVN" as attributes.
			     3.Generate id tag with root and extension as attributes
			     4. Generate code tag with ccodeSystem="2.16.840.1.113883.5.1063" codeSystemName="HL7 Observation Value"
            	    code="IPOP" as attributes.
                 5.Generate displayName as child tag of code with value = name of population as in Simple Xml.
                 6. Generate precondition tag typeCode="PRCN".
                 7. Based on top AND/OR/ANDNOT/ORNOT generate "AllTrue", "AllFalse", "AtLeastOneTrue" tag. Generate id empty tag inside it.
                 8. Generate precondition tag typeCode="PRCN" for all children inside top Logical Op and add criteriaRef to it with id and extension.
		 **/
		getMeasureScoringType(me);
		generateClauseLogicMap(me);
		getAllMeasureGroupings(me);
		generatePopulationCriteria(me);
		return null;
	}
	
	/**
	 * Method to generate population Criteria.
	 * @param me - MeasureExport
	 * @throws XPathExpressionException - Exception
	 */
	private void generatePopulationCriteria(MeasureExport me) throws XPathExpressionException {
		for (String key : measureGroupingMap.keySet()) {
			Node populationCriteriaComponentElement = createPopulationCriteriaSection(key , me.getHQMFXmlProcessor());
			NodeList groupingChildList = measureGroupingMap.get(key);
			for (int i = 0; i < groupingChildList.getLength(); i++) {
				String popType = groupingChildList.item(i).getAttributes().getNamedItem(TYPE).getNodeValue();
				switch(popType) {
					case "initialPopulation" :
						initialPopulation = groupingChildList.item(i);
						generatePopulationTypeCriteria(groupingChildList.item(i)
								, populationCriteriaComponentElement , me,"initialPopulationCriteria","IPOP",null);
						break;
					case "denominator" :
						generatePopulationTypeCriteria(groupingChildList.item(i)
								, populationCriteriaComponentElement , me,"denominatorCriteria","DENOM",null);
						break;
					case "denominatorExclusions" :
						// top Logical Op is OR
						generatePopulationTypeCriteria(groupingChildList.item(i)
								, populationCriteriaComponentElement , me,"denominatorExclusionCriteria","DENEX",null);
						break;
					case "denominatorExceptions" :
						// top Logical Op is OR
						generatePopulationTypeCriteria(groupingChildList.item(i)
								, populationCriteriaComponentElement , me,"denominatorExceptionCriteria","DENEXCEP",null);
						break;
					case "numerator" :
						generatePopulationTypeCriteria(groupingChildList.item(i)
								, populationCriteriaComponentElement , me,"numeratorCriteria","NUMER",null);
						break;
					case "numeratorExclusions" :
						// top Logical Op is OR
						generatePopulationTypeCriteria(groupingChildList.item(i)
								, populationCriteriaComponentElement , me,"numeratorExclusionCriteria","NUMEX",null);
						break;
					case "measurePopulation" :
						generatePopulationTypeCriteria(groupingChildList.item(i)
								, populationCriteriaComponentElement , me,"measurePopulationCriteria","MSRPOPL",null);
						break;
					case "measurePopulationExclusions" :
						//If measurePopulationExclusions has no logic added then it should not be included in populationCriteria as per Stan.
						if(groupingChildList.item(i).getChildNodes().item(0).hasChildNodes()) {
							generatePopulationTypeCriteria(groupingChildList.item(i)
									, populationCriteriaComponentElement , me,"measurePopulationExclusionCriteria","MSRPOPLEX",null);
						}
						break;
					case "measureObservation" :
						//No top Logical Op.
						break;
					case "stratum" :
						//No top Logical Op.
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
	 * @param populationCriteriaComponentElement - Element
	 * @param me - MeasureExport
	 * @param criteriaTagName - String.
	 * @param criteriaTagCodeName - String code value.
	 * @param topLogicalOpName - top Logical op type.
	 * @throws XPathExpressionException - Exception
	 */
	//Except for Stratification , do not include top level Logical Op as per Stan.
	private void generatePopulationTypeCriteria(Node item, Node populationCriteriaComponentElement
			, MeasureExport me, String criteriaTagName, String criteriaTagCodeName
			, String topLogicalOpName) throws XPathExpressionException {
		Document doc = populationCriteriaComponentElement.getOwnerDocument();
		Element populationCriteriaElement = (Element) populationCriteriaComponentElement.getFirstChild();
		Element componentElement = doc.createElement("component");
		componentElement.setAttribute(TYPE_CODE, "COMP");
		Element initialPopCriteriaElement = doc.createElement(criteriaTagName);
		initialPopCriteriaElement.setAttribute(CLASS_CODE, "OBS");
		initialPopCriteriaElement.setAttribute(MOOD_CODE, "EVN");
		Element idElement = doc.createElement(ID);
		idElement.setAttribute(ROOT, item.getAttributes().getNamedItem(UUID).getNodeValue());
		idElement.setAttribute("extension", StringUtils.deleteWhitespace(item.getAttributes().getNamedItem(TYPE).getNodeValue()));
		initialPopCriteriaElement.appendChild(idElement);
		Element codeElem = doc.createElement(CODE);
		codeElem.setAttribute(CODE, criteriaTagCodeName);
		codeElem.setAttribute(CODE_SYSTEM, "2.16.840.1.113883.5.1063");
		codeElem.setAttribute(CODE_SYSTEM_NAME, "HL7 Observation Value");
		Element displayNameElement = doc.createElement(DISPLAY_NAME);
		displayNameElement.setAttribute(VALUE, item.getAttributes().getNamedItem(TYPE).getNodeValue());
		codeElem.appendChild(displayNameElement);
		initialPopCriteriaElement.appendChild(codeElem);
		Element preConditionElem = doc.createElement("precondition");
		preConditionElem.setAttribute(TYPE_CODE, "PRCN");
		checkScoringTypeToAssociateIP(preConditionElem,item);
		generatePopulationLogic(preConditionElem, item.getChildNodes().item(0), me);
		initialPopCriteriaElement.appendChild(preConditionElem);
		componentElement.appendChild(initialPopCriteriaElement);
		populationCriteriaElement.appendChild(componentElement);
	}
	/**
	 * Associate IP with Deno in Proportion Measures, With MeasurePopulation
	 *  In Continous Variable and based on association in Ratio Measures with Deno and NUm.
	 * @param preConditionElem
	 * @param item
	 */
	private void checkScoringTypeToAssociateIP(Element preConditionElem, Node item) {
		String nodeType = item.getAttributes().getNamedItem(TYPE).getNodeValue();
		if(scoringType.equalsIgnoreCase("Proportion") && nodeType.equalsIgnoreCase("denominator")) {
			Document mainDocument = preConditionElem.getOwnerDocument();
			Element criteriaRef = mainDocument.createElement("criteriaReference");
			criteriaRef.setAttribute(CLASS_CODE, "OBS");
			criteriaRef.setAttribute(MOOD_CODE, "EVN");
			Element idElement = mainDocument.createElement(ID);
			idElement.setAttribute(ROOT, initialPopulation.getAttributes().getNamedItem(UUID).getNodeValue());
			idElement.setAttribute("extension", StringUtils.deleteWhitespace(initialPopulation.getAttributes().getNamedItem(TYPE).getNodeValue()));
			criteriaRef.appendChild(idElement);
			preConditionElem.appendChild(criteriaRef);
		} else if(scoringType.equalsIgnoreCase("Continuous Variable") && nodeType.equalsIgnoreCase("measurePopulation")) {
			Document mainDocument = preConditionElem.getOwnerDocument();
			Element criteriaRef = mainDocument.createElement("criteriaReference");
			criteriaRef.setAttribute(CLASS_CODE, "OBS");
			criteriaRef.setAttribute(MOOD_CODE, "EVN");
			Element idElement = mainDocument.createElement(ID);
			idElement.setAttribute(ROOT, initialPopulation.getAttributes().getNamedItem(UUID).getNodeValue());
			idElement.setAttribute("extension", StringUtils.deleteWhitespace(initialPopulation.getAttributes().getNamedItem(TYPE).getNodeValue()));
			criteriaRef.appendChild(idElement);
			preConditionElem.appendChild(criteriaRef);
		} else if(scoringType.equalsIgnoreCase("Ratio") &&( nodeType.equalsIgnoreCase("denominator")
				|| nodeType.equalsIgnoreCase("numerator"))) {
			String associatedIPUUID = initialPopulation.getAttributes().getNamedItem(UUID).getNodeValue();
			if(item.getAttributes().getNamedItem("associatedPopulationUUID") != null) {
				associatedIPUUID = item.getAttributes().getNamedItem("associatedPopulationUUID").getNodeValue();
			}
			Document mainDocument = preConditionElem.getOwnerDocument();
			Element criteriaRef = mainDocument.createElement("criteriaReference");
			criteriaRef.setAttribute(CLASS_CODE, "OBS");
			criteriaRef.setAttribute(MOOD_CODE, "EVN");
			Element idElement = mainDocument.createElement(ID);
			idElement.setAttribute(ROOT, associatedIPUUID);
			idElement.setAttribute("extension", StringUtils.deleteWhitespace("initialPopulation"));
			criteriaRef.appendChild(idElement);
			preConditionElem.appendChild(criteriaRef);
		}
		
	}
	
	/**
	 * Method to generate tags for logical Operators.
	 * @param doc - Document.
	 * @param type - Logical Op type.
	 * @return Element.
	 */
	private Element generateLogicalOperator(Document doc , String type) {
		Element logicalOpElement = null;
		switch (type) {
			case "and":
				logicalOpElement = doc.createElement("allTrue");
				
				break;
			case "or":
				logicalOpElement = doc.createElement("atLeastOneTrue");
				
				break;
			case "andnot":
				logicalOpElement = doc.createElement("allFalse");
				
				break;
			case "ornot":
				logicalOpElement = doc.createElement("atLeastOneFalse");
				
				break;
			default:
				//do nothing
				break;
		}
		return logicalOpElement;
	}
	/**
	 * Method to generate tags for logic used inside population.
	 * @param topLevelPreConditionElement - Element.
	 * @param item - Node.
	 * @param me - MeasureExport.
	 * @throws XPathExpressionException - Exception.
	 */
	private void generatePopulationLogic(Element topLevelPreConditionElement, Node item, MeasureExport me) throws XPathExpressionException {
		for (int i = 0; i < item.getChildNodes().getLength(); i++) {
			Node childNode = item.getChildNodes().item(i);
			String nodeType = null;
			if (childNode.getAttributes().getNamedItem(TYPE) != null) {
				nodeType = childNode.getAttributes().getNamedItem(TYPE).getNodeValue();
			} else {
				nodeType = childNode.getNodeName();
			}
			Element preConditionElement = topLevelPreConditionElement.getOwnerDocument().createElement("precondition");
			preConditionElement.setAttribute(TYPE_CODE, "PRCN");
			switch(nodeType) {
				case "subTree":
					generateCritRefSubTreeRef(me, topLevelPreConditionElement, childNode, me.getHQMFXmlProcessor());
					break;
				case "comment":
					// skipping comment node as of now.
					break;
				case "itemCount":
					// skipping itemCount node as of now.
					break;
				case "and":
				case "or":
				case "andnot":
				case "ornot":
					Element logicalOpNode = generateLogicalOperator(topLevelPreConditionElement.getOwnerDocument(), nodeType);
					preConditionElement.appendChild(logicalOpNode);
					if (childNode.getChildNodes().getLength() > 0) {
						generatePopulationLogic(logicalOpNode, childNode , me);
					}
				default:
					break;
			}
			if (preConditionElement.hasChildNodes()) {
				topLevelPreConditionElement.appendChild(preConditionElement);
			}
		}
	}
	/**
	 * Method to generate component and populationCriteria default tags.
	 * @param sequenceNumber - Measure Grouping sequence number.
	 * @param outputProcessor - XmlProcessor.
	 * @return - Node.
	 */
	private Node createPopulationCriteriaSection(String sequenceNumber, XmlProcessor outputProcessor) {
		Node componentElement = outputProcessor.getOriginalDoc().createElement("component");
		Node popCriteriaElem = outputProcessor.getOriginalDoc()
				.createElement("populationCriteriaSection");
		Element templateId = outputProcessor.getOriginalDoc().createElement(TEMPLATE_ID);
		popCriteriaElem.appendChild(templateId);
		Element itemChild = outputProcessor.getOriginalDoc().createElement(ITEM);
		itemChild.setAttribute(ROOT, "2.16.840.1.113883.10.20.28.2.1");
		/*itemChild.setAttribute("extension", UUIDUtilClient.uuid());*/
		templateId.appendChild(itemChild);
		Element idElement = outputProcessor.getOriginalDoc()
				.createElement(ID);
		idElement.setAttribute(ROOT, UUIDUtilClient.uuid());
		idElement.setAttribute("extension", "PopulationCriteria" + sequenceNumber);
		popCriteriaElem.appendChild(idElement);
		Element codeElem = outputProcessor.getOriginalDoc()
				.createElement(CODE);
		codeElem.setAttribute(CODE, "57026-7");
		codeElem.setAttribute(CODE_SYSTEM, "2.16.840.1.113883.6.1");
		popCriteriaElem.appendChild(codeElem);
		Element titleElem = outputProcessor.getOriginalDoc()
				.createElement(TITLE);
		titleElem.setAttribute(VALUE, "Population Criteria Section.");
		popCriteriaElem.appendChild(titleElem);
		// creating text for PopulationCriteria
		Element textElem = outputProcessor.getOriginalDoc()
				.createElement("text");
		textElem.setAttribute(VALUE, "Population Criteria text.");
		popCriteriaElem.appendChild(textElem);
		componentElement.appendChild(popCriteriaElem);
		outputProcessor.getOriginalDoc().getDocumentElement().appendChild(componentElement);
		return componentElement;
	}
	/**
	 * Get Measure Scoring type.
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
			String firstChildName = firstChildNode.getNodeName();
			String displayName = null;
			switch (firstChildName) {
				case "elementRef":
					displayName = firstChildNode.getAttributes().getNamedItem(ID).getNodeValue();
					break;
				default:
					displayName = firstChildNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
			}
			clauseLogicMap.put(uuid, displayName);
		}
	}
	
	/**
	 * Method to populate all measure groupings in measureGroupingMap.
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
	
	private Node createSupplementalDateCriteriaSection(String sequenceNumber, XmlProcessor outputProcessor){
		
		Node componentElement = outputProcessor.getOriginalDoc().createElement("component");
		Node stratCriteriaElem = outputProcessor.getOriginalDoc()
				.createElement("stratifierCriteria");
		Element idElement = outputProcessor.getOriginalDoc()
				.createElement(ID);
		idElement.setAttribute(ROOT, "FF3FEB80-1663-11E4-2165-09173F13E4C5");
		idElement.setAttribute("identifierName", "Stratifiers");
		stratCriteriaElem.appendChild(idElement);
		Element codeElem = outputProcessor.getOriginalDoc()
				.createElement(CODE);
		codeElem.setAttribute(CODE, "STRAT");
		codeElem.setAttribute(CODE_SYSTEM, "2.16.840.1.113883.5.1063");
		stratCriteriaElem.appendChild(codeElem);
		componentElement.appendChild(stratCriteriaElem);
		outputProcessor.getOriginalDoc().getDocumentElement().appendChild(componentElement);
		return componentElement;
	}
}
