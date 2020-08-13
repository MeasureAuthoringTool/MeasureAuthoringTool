package mat.server.hqmf.qdm;

import mat.model.clause.MeasureExport;
import mat.server.util.XmlProcessor;
import mat.shared.UUIDUtilClient;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
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

/**
 * @deprecated this class is deprecated since it is an old version of QDM. It should not be modified. 
 */
public class HQMFPopulationLogicGenerator extends HQMFClauseLogicGenerator {
	/** The clause logic map. */
	private Map<String, String> clauseLogicMap = new HashMap<String, String>();
	
	/** The measure grouping map. */
	//	private Map<String, NodeList> measureGroupingMap = new HashMap<String, NodeList>();
	
	private TreeMap<Integer, NodeList> measureGroupingMap = new TreeMap<Integer, NodeList>();
	
	/** The scoring type. */
	private String scoringType;
	
	/** The initial population. */
	private Node initialPopulation;
	
	/* (non-Javadoc)
	 * @see mat.server.simplexml.hqmf.HQMFClauseLogicGenerator#generate(mat.model.clause.MeasureExport)
	 */
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
			     4. Generate code tag with ccodeSystem="2.16.840.1.113883.5.4" codeSystemName="HL7 Observation Value"
            	    code="IPOP" as attributes.
                 5.Generate displayName as child tag of code with value = name of population as in Simple Xml.
                 6. Generate precondition tag typeCode="PRCN".
                 7. Based on top AND/OR/ANDNOT/ORNOT generate "AllTrue", "AllFalse", "AtLeastOneTrue" tag. Generate id empty tag inside it.
                 8. Generate precondition tag typeCode="PRCN" for all children inside top Logical Op and add criteriaRef to it with id and extension.
		 **/
		measureExport = me;
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
		for (Integer key : measureGroupingMap.keySet()) {
			Node populationCriteriaComponentElement = createPopulationCriteriaSection(key.toString() , me.getHQMFXmlProcessor());
			NodeList groupingChildList = measureGroupingMap.get(key);
			for (int i = 0; i < groupingChildList.getLength(); i++) {
				String popType = groupingChildList.item(i).getAttributes().getNamedItem(TYPE).getNodeValue();
				switch(popType) {
					case "initialPopulation" :
						initialPopulation = groupingChildList.item(i);
						generatePopulationTypeCriteria(groupingChildList.item(i)
								, populationCriteriaComponentElement , me
								, "initialPopulationCriteria", "IPOP", null);
						break;
					case "denominator" :
						if(checkForRequiredClauseByScoring(me, popType, groupingChildList.item(i))){
							generatePopulationTypeCriteria(groupingChildList.item(i)
									, populationCriteriaComponentElement , me
									, "denominatorCriteria", "DENOM", null);
						}
						break;
					case "denominatorExclusions" :
						// top Logical Op is OR
						if(checkForRequiredClauseByScoring(me, popType, groupingChildList.item(i))){
							generatePopulationTypeCriteria(groupingChildList.item(i)
									, populationCriteriaComponentElement , me
									, "denominatorExclusionCriteria", "DENEX", null);
						}
						break;
					case "denominatorExceptions" :
						// top Logical Op is OR
						if(checkForRequiredClauseByScoring(me, popType, groupingChildList.item(i))){
							generatePopulationTypeCriteria(groupingChildList.item(i)
									, populationCriteriaComponentElement , me
									, "denominatorExceptionCriteria", "DENEXCEP", null);
						}
						break;
					case "numerator" :
						if(checkForRequiredClauseByScoring(me, popType, groupingChildList.item(i))){
							generatePopulationTypeCriteria(groupingChildList.item(i)
									, populationCriteriaComponentElement , me
									, "numeratorCriteria", "NUMER", null);
						}
						break;
					case "numeratorExclusions" :
						// top Logical Op is OR
						if(checkForRequiredClauseByScoring(me, popType, groupingChildList.item(i))){
							generatePopulationTypeCriteria(groupingChildList.item(i)
									, populationCriteriaComponentElement , me
									, "numeratorExclusionCriteria", "NUMEX", null);
						}
						break;
					case "measurePopulation" :
						if(checkForRequiredClauseByScoring(me, popType, groupingChildList.item(i))){
							generatePopulationTypeCriteria(groupingChildList.item(i)
									, populationCriteriaComponentElement , me
									, "measurePopulationCriteria", "MSRPOPL", null);
						}
						break;
					case "measurePopulationExclusions" :
						//If measurePopulationExclusions has no logic added
						//then it should not be included in populationCriteria as per Stan.
						if(checkForRequiredClauseByScoring(me, popType, groupingChildList.item(i))){
							generatePopulationTypeCriteria(groupingChildList.item(i)
									, populationCriteriaComponentElement , me
									, "measurePopulationExclusionCriteria", "MSRPOPLEX", null);
						}
						break;
					case "measureObservation" :
						//No top Logical Op.
						break;
					case "stratum" :
						//No top Logical Op.
						if(checkForRequiredClauseByScoring(me, popType, groupingChildList.item(i))){
							generateStratifierCriteria(groupingChildList.item(i)
									, populationCriteriaComponentElement, me, key.toString());
						}
						break;
					default:
						//do nothing.
						break;
				}
			}
			//for creating SupplementalDataElements Criteria Section
			createSupplementalDataElmStratifier(me,populationCriteriaComponentElement.getFirstChild());
		}
		
	}
	
	/**
	 * Generate Stratification Criteria Tag inside populationCrtieriaSection.
	 * @param item - Node.
	 * @param populationCriteriaComponentElement - Element.
	 * @param me - MeasureExport.
	 * @param groupingSequence - Measure Group Sequence.
	 * @throws XPathExpressionException - Exception.
	 */
	private void generateStratifierCriteria(Node item, Node populationCriteriaComponentElement
			, MeasureExport me, String groupingSequence) throws XPathExpressionException {
		Document doc = populationCriteriaComponentElement.getOwnerDocument();
		Element populationCriteriaElement = (Element) populationCriteriaComponentElement.getFirstChild();
		//Node stratifierCriteraNode = null;
		/*Node populationCriteria = null;*/
		// Code to identify correct PopulationCriteria/StratificationCriteria Node based on Grouping Key.
		/*NodeList idNodeList = me.getHQMFXmlProcessor().findNodeList(doc, "//component/populationCriteriaSection/child::id");
		for (int i = 0; i < idNodeList.getLength(); i++) {
			String extension = idNodeList.item(i).getAttributes().getNamedItem("extension").getNodeValue();
			String extensionToBeCompared = "PopulationCriteria" + groupingSequence;
			if (extensionToBeCompared.equalsIgnoreCase(extension)) {
				populationCriteria = idNodeList.item(i).getParentNode();
				Node lastChild = populationCriteria.getLastChild();
				if ((lastChild.getChildNodes() != null)
						&& lastChild.getChildNodes().item(0).getNodeName().equalsIgnoreCase("stratifierCriteria")) {
					stratifierCriteraNode = lastChild.getChildNodes().item(0);
				}
			}
		}*/
		if (populationCriteriaElement != null) {
			Element componentElement = doc.createElement("component");
			componentElement.setAttribute(TYPE_CODE, "COMP");
			Element stratCriteriaElement = doc.createElement("stratifierCriteria");
			Element idElement = doc.createElement(ID);
			idElement.setAttribute(ROOT, item.getAttributes().getNamedItem(UUID).getNodeValue());
			idElement.setAttribute("extension", "Stratifiers");
			stratCriteriaElement.appendChild(idElement);
			Element codeElem = doc.createElement(CODE);
			codeElem.setAttribute(CODE, "STRAT");
			codeElem.setAttribute(CODE_SYSTEM, "2.16.840.1.113883.5.4");
			codeElem.setAttribute(CODE_SYSTEM_NAME, "HL7 Observation Value");
			Element displayNameElement = doc.createElement(DISPLAY_NAME);
			displayNameElement.setAttribute(VALUE, "Stratification");
			codeElem.appendChild(displayNameElement);
			stratCriteriaElement.appendChild(codeElem);
			componentElement.appendChild(stratCriteriaElement);
			populationCriteriaElement.appendChild(componentElement);
			/*else {
			
			stratCriteriaElement = (Element) stratifierCriteraNode;
			componentElement = (Element) stratCriteriaElement.getParentNode();
		}*/
			/*Element preConditionElem = doc.createElement("precondition");
		preConditionElem.setAttribute(TYPE_CODE, "PRCN");*/
			if (item.getChildNodes().item(0) != null) {
				generatePopulationLogic(stratCriteriaElement, item, me);
			}
		}
		/*stratCriteriaElement.appendChild(preConditionElem);*/
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
		codeElem.setAttribute(CODE_SYSTEM, "2.16.840.1.113883.5.4");
		codeElem.setAttribute(CODE_SYSTEM_NAME, "HL7 Observation Value");
		Element displayNameElement = doc.createElement(DISPLAY_NAME);
		displayNameElement.setAttribute(VALUE, item.getAttributes().getNamedItem(TYPE).getNodeValue());
		codeElem.appendChild(displayNameElement);
		initialPopCriteriaElement.appendChild(codeElem);
		/*Element preConditionElem = doc.createElement("precondition");
		preConditionElem.setAttribute(TYPE_CODE, "PRCN");*/
		checkScoringTypeToAssociateIP(initialPopCriteriaElement, item);
		if (item.getChildNodes() != null) {
			for(int i=0;i<item.getChildNodes().getLength();i++){
				generatePopulationLogic(initialPopCriteriaElement, item.getChildNodes().item(i), me);
			}
		}
		/*initialPopCriteriaElement.appendChild(preConditionElem);*/
		componentElement.appendChild(initialPopCriteriaElement);
		populationCriteriaElement.appendChild(componentElement);
	}
	
	/**
	 * Associate IP with Deno in Proportion Measures, With MeasurePopulation
	 *  In Continous Variable and based on association in Ratio Measures with Deno and NUm.
	 *
	 * @param populationCritieriaElem the pre condition elem
	 * @param item the item
	 */
	private void checkScoringTypeToAssociateIP(Element populationCritieriaElem, Node item) {
		String nodeType = item.getAttributes().getNamedItem(TYPE).getNodeValue();
		Document mainDocument = populationCritieriaElem.getOwnerDocument();
		Element preConditionElem = mainDocument.createElement("precondition");
		preConditionElem.setAttribute(TYPE_CODE, "PRCN");
		if (scoringType.equalsIgnoreCase("Proportion") && nodeType.equalsIgnoreCase("denominator")) {
			Element criteriaRef = mainDocument.createElement("criteriaReference");
			criteriaRef.setAttribute(CLASS_CODE, "OBS");
			criteriaRef.setAttribute(MOOD_CODE, "EVN");
			Element idElement = mainDocument.createElement(ID);
			idElement.setAttribute(ROOT, initialPopulation.getAttributes().getNamedItem(UUID).getNodeValue());
			idElement.setAttribute("extension"
					, StringUtils.deleteWhitespace(initialPopulation.getAttributes()
							.getNamedItem(TYPE).getNodeValue()));
			criteriaRef.appendChild(idElement);
			preConditionElem.appendChild(criteriaRef);
			populationCritieriaElem.appendChild(preConditionElem);
		} else if (scoringType.equalsIgnoreCase("Continuous Variable") && nodeType.equalsIgnoreCase("measurePopulation")) {
			Element criteriaRef = mainDocument.createElement("criteriaReference");
			criteriaRef.setAttribute(CLASS_CODE, "OBS");
			criteriaRef.setAttribute(MOOD_CODE, "EVN");
			Element idElement = mainDocument.createElement(ID);
			idElement.setAttribute(ROOT, initialPopulation.getAttributes().getNamedItem(UUID).getNodeValue());
			idElement.setAttribute("extension", StringUtils.deleteWhitespace(
					initialPopulation.getAttributes().getNamedItem(TYPE).getNodeValue()));
			criteriaRef.appendChild(idElement);
			preConditionElem.appendChild(criteriaRef);
			populationCritieriaElem.appendChild(preConditionElem);
		} else if (scoringType.equalsIgnoreCase("Ratio") && (nodeType.equalsIgnoreCase("denominator")
				|| nodeType.equalsIgnoreCase("numerator"))) {
			String associatedIPUUID = initialPopulation.getAttributes().getNamedItem(UUID).getNodeValue();
			if (item.getAttributes().getNamedItem("associatedPopulationUUID") != null) {
				associatedIPUUID = item.getAttributes().getNamedItem("associatedPopulationUUID").getNodeValue();
			}
			Element criteriaRef = mainDocument.createElement("criteriaReference");
			criteriaRef.setAttribute(CLASS_CODE, "OBS");
			criteriaRef.setAttribute(MOOD_CODE, "EVN");
			Element idElement = mainDocument.createElement(ID);
			idElement.setAttribute(ROOT, associatedIPUUID);
			idElement.setAttribute("extension", StringUtils.deleteWhitespace("initialPopulation"));
			criteriaRef.appendChild(idElement);
			preConditionElem.appendChild(criteriaRef);
			populationCritieriaElem.appendChild(preConditionElem);
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
			case "andNot":
				logicalOpElement = doc.createElement("allFalse");
				break;
			case "orNot":
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
	 * @param populationTypeCriteriaElement - Element.
	 * @param item - Node.
	 * @param me - MeasureExport.
	 * @throws XPathExpressionException - Exception.
	 */
	private void generatePopulationLogic(Element populationTypeCriteriaElement
			, Node item, MeasureExport me) throws XPathExpressionException {
		
		if (item.getChildNodes() != null) {
			for (int i = 0; i < item.getChildNodes().getLength(); i++) {
				Node childNode = item.getChildNodes().item(i);
				String nodeType = null;
				if (childNode.getAttributes().getNamedItem(TYPE) != null) {
					nodeType = childNode.getAttributes().getNamedItem(TYPE).getNodeValue();
				} else {
					nodeType = childNode.getNodeName();
				}
				Element preConditionElement = populationTypeCriteriaElement.
						getOwnerDocument().createElement("precondition");
				preConditionElement.setAttribute(TYPE_CODE, "PRCN");
				switch(nodeType) {
					case "subTree":
						generateCritRefSubTreeRef( preConditionElement, childNode, me.getHQMFXmlProcessor());
						break;
					case "comment":
						// skipping comment node as of now.
						break;
					case "itemCount":
						generatePopulationLogic(populationTypeCriteriaElement, childNode , me);
						break;
					case "elementRef":
						generateItemCountElementRef(me, populationTypeCriteriaElement, childNode, me.getHQMFXmlProcessor());
						break;
					case "and":
					case "or":
					case "andNot":
					case "orNot":
						Element logicalOpNode = generateLogicalOperator(
								populationTypeCriteriaElement.getOwnerDocument(), nodeType);
						preConditionElement.appendChild(logicalOpNode);
						if (childNode.getChildNodes().getLength() > 0) {
							generatePopulationLogic(logicalOpNode, childNode , me);
						}
					default:
						break;
				}
				if (preConditionElement.hasChildNodes()) {
					populationTypeCriteriaElement.appendChild(preConditionElement);
				}
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
		Element componentElement = outputProcessor.getOriginalDoc().createElement("component");
		Attr nameSpaceAttr = outputProcessor.getOriginalDoc()
				.createAttribute("xmlns:xsi");
		nameSpaceAttr.setNodeValue(nameSpace);
		componentElement.setAttributeNodeNS(nameSpaceAttr);
		Node popCriteriaElem = outputProcessor.getOriginalDoc()
				.createElement("populationCriteriaSection");
		Element templateId = outputProcessor.getOriginalDoc().createElement(TEMPLATE_ID);
		popCriteriaElem.appendChild(templateId);
		Element itemChild = outputProcessor.getOriginalDoc().createElement(ITEM);
		itemChild.setAttribute(ROOT, "2.16.840.1.113883.10.20.28.2.1");
		itemChild.setAttribute("extension", POPULATION_CRITERIA_EXTENSION);
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
		titleElem.setAttribute(VALUE, "Population Criteria Section");
		popCriteriaElem.appendChild(titleElem);
		// creating text for PopulationCriteria
		Element textElem = outputProcessor.getOriginalDoc()
				.createElement("text");
		//textElem.setAttribute(VALUE, "Population Criteria text");
		popCriteriaElem.appendChild(textElem);
		componentElement.appendChild(popCriteriaElem);
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
			measureGroupingMap.put(Integer.parseInt(measureGroupingSequence), childNodeList);
		}
	}
	/**
	 * Creates the supplemental date criteria section.
	 *
	 * @param me the me
	 * @return the node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private Element createSupplementalDataElmComponentNode(MeasureExport me) throws XPathExpressionException {
		XmlProcessor outputProcessor = me.getHQMFXmlProcessor();
		Element componentElement = outputProcessor.getOriginalDoc().createElement("component");
		componentElement.setAttribute(TYPE_CODE, "COMP");
		Node stratCriteriaElem = outputProcessor.getOriginalDoc()
				.createElement("stratifierCriteria");
		Element idElement = outputProcessor.getOriginalDoc()
				.createElement(ID);
		idElement.setAttribute(ROOT, UUIDUtilClient.uuid());
		idElement.setAttribute("extension", "Stratifiers");
		stratCriteriaElem.appendChild(idElement);
		Element codeElem = outputProcessor.getOriginalDoc()
				.createElement(CODE);
		codeElem.setAttribute(CODE, "STRAT");
		codeElem.setAttribute(CODE_SYSTEM, "2.16.840.1.113883.5.4");
		codeElem.setAttribute(CODE_SYSTEM_NAME, "HL7 Observation Value");
		Element displayNameElement = outputProcessor.getOriginalDoc().createElement(DISPLAY_NAME);
		displayNameElement.setAttribute(VALUE, "Stratification");
		codeElem.appendChild(displayNameElement);
		stratCriteriaElem.appendChild(codeElem);
		componentElement.appendChild(stratCriteriaElem);
		return componentElement;
	}
	/**
	 * Creates the measure attribute component.
	 *
	 * @param me the me
	 * @param parentElem the parent elem
	 */
	private void createMeasureAttributeComponent(MeasureExport me, Node parentElem) {
		XmlProcessor outputProcessor = me.getHQMFXmlProcessor();
		Element componentElement = outputProcessor.getOriginalDoc().createElement("component");
		componentElement.setAttribute(TYPE_CODE, "COMP");
		Node measureAttributeElem = outputProcessor.getOriginalDoc()
				.createElement("measureAttribute");
		Element codeElement = outputProcessor.getOriginalDoc()
				.createElement(CODE);
		codeElement.setAttribute(CODE, "SDE");
		codeElement.setAttribute(CODE_SYSTEM, "2.16.840.1.113883.5.4");
		Element displayNameElem = outputProcessor.getOriginalDoc()
				.createElement("displayName");
		displayNameElem.setAttribute(VALUE, "Supplemental Data Element");
		codeElement.appendChild(displayNameElem);
		Element valueElem = outputProcessor.getOriginalDoc()
				.createElement("value");
		valueElem.setAttribute(XSI_TYPE, "ED");
		valueElem.setAttribute("mediaType", "text/plain");
		valueElem.setAttribute(VALUE, "Supplemental Data Elements");
		measureAttributeElem.appendChild(codeElement);
		measureAttributeElem.appendChild(valueElem);
		componentElement.appendChild(measureAttributeElem);
		parentElem.appendChild(componentElement);
	}
	
	/**
	 * Gets the required clauses.
	 *
	 * @param type the type
	 * @return the required clauses
	 */
	private static List<String> getRequiredClauses(String type){
		List<String> list = new ArrayList<String>();
		if("Cohort".equalsIgnoreCase(type)){
			list.add("initialPopulation");
		}else if("Continuous Variable".equalsIgnoreCase(type)){
			list.add("initialPopulation");
			list.add("measurePopulation");
			list.add("measureObservation");
		}else if("Proportion".equalsIgnoreCase(type) ||
				"Ratio".equalsIgnoreCase(type)){
			list.add("initialPopulation");
			list.add("denominator");
			list.add("numerator");
		}
		return list;
	}
	
	/**
	 * Check for required clause by scoring.
	 *
	 * @param me the me
	 * @param popType the pop type
	 * @param node the node
	 * @return true, if successful
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static boolean checkForRequiredClauseByScoring(MeasureExport me, String popType, Node node) throws XPathExpressionException{
		boolean isRequiredClause = false;
		Node scoringType = me.getSimpleXMLProcessor().findNode(me.getSimpleXMLProcessor().getOriginalDoc(),
				"/measure/measureDetails/scoring");
		List<String> clauseList = new ArrayList<String>();
		clauseList = getRequiredClauses(scoringType.getTextContent());
		if(clauseList.contains(popType)){
			isRequiredClause = true;
		} else {
			isRequiredClause = checkForPackageClauseLogic(node, popType);
		}
		return isRequiredClause;
	}
	
	
	/**
	 * Check for package clause logic.
	 *
	 * @param node the node
	 * @param popType the pop type
	 * @return true, if successful
	 */
	private static boolean checkForPackageClauseLogic(Node node,
			String popType) {
		switch(popType) {
			
			case "measurePopulation":
				if(node.getChildNodes().item(0).hasChildNodes()) {
					return true;
				}
			case "numerator":
				if(node.getChildNodes().item(0).hasChildNodes()) {
					return true;
				}
			case "denominator":
				if(node.getChildNodes().item(0).hasChildNodes()) {
					return true;
				}
				
			case "denominatorExclusions":
				if(node.getChildNodes().item(0).hasChildNodes()) {
					return true;
				}
			case "numeratorExclusions":
				if(node.getChildNodes().item(0).hasChildNodes()) {
					return true;
				}
				break;
				
			case "measureObservation":
				if(node.hasChildNodes()) {
					return true;
				}
				break;
			case "stratum":
				if(node.hasChildNodes()) {
					return true;
				}
				break;
				
			case "denominatorExceptions":
				if(node.getChildNodes().item(0).hasChildNodes()) {
					return true;
				}
				break;
			case "measurePopulationExclusions":
				if(node.getChildNodes().item(0).hasChildNodes()) {
					return true;
				}
				break;
				
			default:   //do Nothing
				break;
				
		}
		return false;
	}
	
	/**
	 * Creates Logic for Each Supplemental Data Element Nodes.
	 *
	 * @param me the me
	 * @param parentNode - PopulationCriteria First Child Node.
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void createSupplementalDataElmStratifier(MeasureExport me, Node parentNode) throws XPathExpressionException {
		String xpathForOtherSupplementalQDMs = "/measure/supplementalDataElements/elementRef/@id";
		NodeList supplementalDataElements = me.getSimpleXMLProcessor().findNodeList(me.getSimpleXMLProcessor().getOriginalDoc(),
				xpathForOtherSupplementalQDMs);
		if ((supplementalDataElements == null)
				|| (supplementalDataElements.getLength() < 1)) {
			return;
		}
		List<String> supplementalElemenRefIds = new ArrayList<String>();
		for (int i = 0; i < supplementalDataElements.getLength(); i++) {
			supplementalElemenRefIds.add(supplementalDataElements.item(i).getNodeValue());
		}
		
		String uuidXPathString = "";
		for (String uuidString: supplementalElemenRefIds) {
			uuidXPathString += "@uuid = '" + uuidString + "' or";
		}
		uuidXPathString = uuidXPathString.substring(0, uuidXPathString.lastIndexOf(" or"));
		String xpathforOtherSupplementalDataElements = "/measure/elementLookUp/qdm[" + uuidXPathString + "]";
		NodeList supplementalQDMNodeList = me.getSimpleXMLProcessor().findNodeList(me.getSimpleXMLProcessor().getOriginalDoc(),
				xpathforOtherSupplementalDataElements);
		if (supplementalQDMNodeList.getLength() < 1) {
			return;
		}
		
		for (int i = 0; i < supplementalQDMNodeList.getLength(); i++) {
			Element componentElement = createSupplementalDataElmComponentNode(me);
			Node stratCriteriaElem = componentElement.getFirstChild();
			Node qdmNode = supplementalQDMNodeList.item(i);
			String qdmName = qdmNode.getAttributes().getNamedItem("name").getNodeValue();
			String qdmDatatype = qdmNode.getAttributes().getNamedItem("datatype").getNodeValue();
			String qdmUUID = qdmNode.getAttributes().getNamedItem("uuid").getNodeValue();
			String qdmExtension = qdmName.replaceAll("\\s", "") + "_" + qdmDatatype.replaceAll("\\s", "");
			createPreConditionTag(me.getHQMFXmlProcessor(), stratCriteriaElem, qdmUUID, qdmExtension);
			createMeasureAttributeComponent(me, stratCriteriaElem);
			componentElement.appendChild(stratCriteriaElem);
			parentNode.appendChild(componentElement);
		}
		
	}
	
	/**
	 * Creates the pre condition tag.
	 *
	 * @param hqmfXmlProcessor the hqmf xml processor
	 * @param parentElem the parent elem
	 * @param id the id
	 * @param extension the extension
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void createPreConditionTag(XmlProcessor hqmfXmlProcessor,
			Node parentElem, String id, String extension)
					throws XPathExpressionException {
		Node idNodeQDM = hqmfXmlProcessor.findNode(
				hqmfXmlProcessor.getOriginalDoc(), "//entry/*/id[@root='" + id
				+ "'][@extension=\"" + extension + "\"]");
		if (idNodeQDM != null) {
			Node parent = idNodeQDM.getParentNode();
			if (parent != null) {
				NamedNodeMap attribMap = parent.getAttributes();
				String classCode = attribMap.getNamedItem(CLASS_CODE)
						.getNodeValue();
				String moodCode = attribMap.getNamedItem(MOOD_CODE)
						.getNodeValue();
				Element preConditionElem = hqmfXmlProcessor.getOriginalDoc()
						.createElement("precondition");
				preConditionElem.setAttribute(TYPE_CODE, "PRCN");
				Element criteriaRefElem = hqmfXmlProcessor.getOriginalDoc()
						.createElement("criteriaReference");
				criteriaRefElem.setAttribute(CLASS_CODE, classCode);
				criteriaRefElem.setAttribute(MOOD_CODE, moodCode);
				Element criteriaRefIDElem = hqmfXmlProcessor.getOriginalDoc()
						.createElement("id");
				criteriaRefIDElem.setAttribute("root", id);
				criteriaRefIDElem.setAttribute("extension", extension);
				criteriaRefElem.appendChild(criteriaRefIDElem);
				preConditionElem.appendChild(criteriaRefElem);
				parentElem.appendChild(preConditionElem);
			}
		}
	}
	
}
