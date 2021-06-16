package mat.server.hqmf.qdm_5_4;

import mat.model.clause.MeasureExport;
import mat.server.util.XmlProcessor;
import mat.shared.UUIDUtilClient;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

// TODO: Auto-generated Javadoc
/**
 * @deprecated The Class HQMFPopulationLogicGenerator.
 */
public class HQMFPopulationLogicGenerator extends HQMFClauseLogicGenerator {
	
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
								, "initialPopulationCriteria", "IPOP");
						break;
					case "denominator" :
						if(checkForRequiredClauseByScoring(me, popType, groupingChildList.item(i))){
							generatePopulationTypeCriteria(groupingChildList.item(i)
									, populationCriteriaComponentElement , me
									, "denominatorCriteria", "DENOM");
						}
						break;
					case "denominatorExclusions" :
						// top Logical Op is OR
						if(checkForRequiredClauseByScoring(me, popType, groupingChildList.item(i))){
							generatePopulationTypeCriteria(groupingChildList.item(i)
									, populationCriteriaComponentElement , me
									, "denominatorExclusionCriteria", "DENEX");
						}
						break;
					case "denominatorExceptions" :
						// top Logical Op is OR
						if(checkForRequiredClauseByScoring(me, popType, groupingChildList.item(i))){
							generatePopulationTypeCriteria(groupingChildList.item(i)
									, populationCriteriaComponentElement , me
									, "denominatorExceptionCriteria", "DENEXCEP");
						}
						break;
					case "numerator" :
						if(checkForRequiredClauseByScoring(me, popType, groupingChildList.item(i))){
							generatePopulationTypeCriteria(groupingChildList.item(i)
									, populationCriteriaComponentElement , me
									, "numeratorCriteria", "NUMER");
						}
						break;
					case "numeratorExclusions" :
						// top Logical Op is OR
						if(checkForRequiredClauseByScoring(me, popType, groupingChildList.item(i))){
							generatePopulationTypeCriteria(groupingChildList.item(i)
									, populationCriteriaComponentElement , me
									, "numeratorExclusionCriteria", "NUMEX");
						}
						break;
					case "measurePopulation" :
						if(checkForRequiredClauseByScoring(me, popType, groupingChildList.item(i))){
							generatePopulationTypeCriteria(groupingChildList.item(i)
									, populationCriteriaComponentElement , me
									, "measurePopulationCriteria", "MSRPOPL");
						}
						break;
					case "measurePopulationExclusions" :
						//If measurePopulationExclusions has no logic added
						//then it should not be included in populationCriteria as per Stan.
						if(checkForRequiredClauseByScoring(me, popType, groupingChildList.item(i))){
							generatePopulationTypeCriteria(groupingChildList.item(i)
									, populationCriteriaComponentElement , me
									, "measurePopulationExclusionCriteria", "MSRPOPLEX");
						}
						break;
					case "measureObservation" :
						break;
					case "stratum" :
						if(checkForRequiredClauseByScoring(me, popType, groupingChildList.item(i))){
							generatePopulationTypeCriteria(groupingChildList.item(i)
									, populationCriteriaComponentElement, me, "stratifierCriteria", "STRAT");
						}
						break;
					default:
						//do nothing.
						break;
				}
			}
			//for creating SupplementalDataElements Criteria Section
			createSupplementalDataElmStratifier(me,populationCriteriaComponentElement.getFirstChild());
			createRiskAdjustmentStratifier(me, populationCriteriaComponentElement.getFirstChild());
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
	private void generatePopulationTypeCriteria(Node item, Node populationCriteriaComponentElement
			, MeasureExport me, String criteriaTagName, String criteriaTagCodeName) throws XPathExpressionException {
		String idExtenstion = "";
		/*String displayValue = "";*/
		Document doc = populationCriteriaComponentElement.getOwnerDocument();
		Element populationCriteriaElement = (Element) populationCriteriaComponentElement.getFirstChild();
		Element componentElement = doc.createElement("component");
		componentElement.setAttribute(TYPE_CODE, "COMP");
		Element initialPopCriteriaElement = doc.createElement(criteriaTagName);
		
		if(criteriaTagName.equalsIgnoreCase("stratifierCriteria")){
			idExtenstion = "Stratifiers";
		//	displayValue = "Stratification";
		} else {
			initialPopCriteriaElement.setAttribute(CLASS_CODE, "OBS");
			initialPopCriteriaElement.setAttribute(MOOD_CODE, "EVN");
			idExtenstion = StringUtils.deleteWhitespace(item.getAttributes().getNamedItem(TYPE).getNodeValue());
			//displayValue = item.getAttributes().getNamedItem(TYPE).getNodeValue();
		}
		Element idElement = doc.createElement(ID);
		idElement.setAttribute(ROOT, item.getAttributes().getNamedItem(UUID).getNodeValue());
		idElement.setAttribute("extension", idExtenstion);
		initialPopCriteriaElement.appendChild(idElement);
		Element codeElem = doc.createElement(CODE);
		codeElem.setAttribute(CODE, criteriaTagCodeName);
		codeElem.setAttribute(CODE_SYSTEM, "2.16.840.1.113883.5.4");
		codeElem.setAttribute(CODE_SYSTEM_NAME, "Act Code");
		// displayName inside <code> not needed for populations as per stan
	/*	Element displayNameElement = doc.createElement(DISPLAY_NAME);
		displayNameElement.setAttribute(VALUE, displayValue);
		codeElem.appendChild(displayNameElement);*/
		initialPopCriteriaElement.appendChild(codeElem);
		/*Element preConditionElem = doc.createElement("precondition");
		preConditionElem.setAttribute(TYPE_CODE, "PRCN");*/
		if (item.getChildNodes() != null) {
			for(int i=0;i<item.getChildNodes().getLength();i++){
				generatePopulationLogic(initialPopCriteriaElement, item.getChildNodes().item(i), me);
			}
		}
		checkScoringTypeToAssociateIP(initialPopCriteriaElement, item);
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
		Element preConditionElem = mainDocument.createElement("subject");
		preConditionElem.setAttribute(TYPE_CODE, "SUBJ");
	
		if (scoringType.equalsIgnoreCase("Ratio") && (nodeType.equalsIgnoreCase("denominator")
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
	 * Method to generate tags for logic used inside population.
	 * @param populationTypeCriteriaElement - Element.
	 * @param item - Node.
	 * @param me - MeasureExport.
	 * @throws XPathExpressionException - Exception.
	 */
	private void generatePopulationLogic(Element populationTypeCriteriaElement
			, Node childNode, MeasureExport me) throws XPathExpressionException {
		
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
					case "cqldefinition":
						generateCritRefCQLDefine( preConditionElement, childNode, me.getHQMFXmlProcessor());
						break;
					case "comment":
						// skipping comment node as of now.
						break;
					case "cqlfunction":
						break;

					default:
						break;
				}
				if (preConditionElement.hasChildNodes()) {
					populationTypeCriteriaElement.appendChild(preConditionElement);
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
		itemChild.setAttribute(ROOT, "2.16.840.1.113883.10.20.28.2.7");
		itemChild.setAttribute("extension", POPULATION_CRITERIA_EXTENSION_CQL);
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
			case "numerator":
			case "denominator":
			case "denominatorExclusions":
			case "numeratorExclusions":
			case "measureObservation":
			case "stratum":
			case "denominatorExceptions":
			case "measurePopulationExclusions":
				if(node.hasChildNodes()) {
					return true;
				}
				break;
				
			default:   //do Nothing
				break;
				
		}
		return false;
	}
	
	/**
	 * Create the risk adjustment components. This will create a create a component tag underneath 
	 * population critiera section for risk adjustment variables. 
	 * @param me the measure export
	 * @param parentNode the parent node 
	 * @throws XPathExpressionException
	 */
	private void createRiskAdjustmentStratifier(MeasureExport me, Node parentNode) throws XPathExpressionException {
	
		String xPathForRiskAdjustmentVariables = "/measure/riskAdjustmentVariables/cqldefinition"; 
		XmlProcessor simpleXmlProcessor = me.getSimpleXMLProcessor(); 
		NodeList riskAdjustmentVariables = simpleXmlProcessor.findNodeList(simpleXmlProcessor.getOriginalDoc(), 
																			xPathForRiskAdjustmentVariables);		
		String xPathForLibraryName = "/measure/cqlLookUp/library"; 
		Node libraryNode = simpleXmlProcessor.findNode(simpleXmlProcessor.getOriginalDoc(), xPathForLibraryName);
		String libraryName = libraryNode.getTextContent();
		
		String xPathForCQLUUID = "/measure/measureDetails/cqlUUID"; 
		Node cqluuidNode = simpleXmlProcessor.findNode(simpleXmlProcessor.getOriginalDoc(), xPathForCQLUUID); 
		String cqlUUID = cqluuidNode.getTextContent(); 
		
		for (int i = 0; i < riskAdjustmentVariables.getLength(); i++) {
			Node current = riskAdjustmentVariables.item(i); 
			String riskAdjustmentDefName = current.getAttributes().getNamedItem("displayName").getNodeValue(); 
						
			Element component = createRiskAdjustmentComponentNode(me, cqlUUID, libraryName, riskAdjustmentDefName, "MSRADJ");
			parentNode.appendChild(component);
		}
	}
	
	/**
	 * Creates the component for a risk adjustment variable in the hqmf document
	 * @param me the measure export
	 * @param riskAdjustmentUUID the risk adjustment variable uuid
	 * @param cqlUUID the cql file uuid 
	 * @param libraryName the cql library name
	 * @param riskAdjustmentDefName the risk adjustment definition name
	 * @return the component element
	 */
	private Element createRiskAdjustmentComponentNode(MeasureExport me, String cqlUUID, String libraryName, String riskAdjustmentDefName, String type) {
		XmlProcessor processor = me.getHQMFXmlProcessor(); 
		
		Element component = processor.getOriginalDoc().createElement("component"); 
		component.setAttribute("typeCode", "COMP");
		
		Attr qdmNameSpaceAttr = processor.getOriginalDoc().createAttribute("xmlns:cql-ext");
		qdmNameSpaceAttr.setNodeValue("urn:hhs-cql:hqmf-n1-extensions:v1");
		component.setAttributeNodeNS(qdmNameSpaceAttr);
		
		Element stratifierCriteria = processor.getOriginalDoc().createElement("cql-ext:supplementalDataElement");
		
		String extensionStr = "";
		String codeStr = "";
		if(type.equalsIgnoreCase("MSRADJ")){
			extensionStr = "Risk Adjustment Variables";
			codeStr = "MSRADJ";
		} else {
			extensionStr = "Supplemental Data Elements";
			codeStr = "SDE";
		}
		
		Element id = processor.getOriginalDoc().createElement("id");
		id.setAttribute("extension", extensionStr);
		id.setAttribute("root", UUIDUtilClient.uuid());
		stratifierCriteria.appendChild(id); 
		
		Element code = processor.getOriginalDoc().createElement("code"); 
		code.setAttribute("code", codeStr);
		code.setAttribute("codeSystem", "2.16.840.1.113883.5.4");
		code.setAttribute("codeSystemName", "Act Code");
		stratifierCriteria.appendChild(code);
		
		Element precondition = processor.getOriginalDoc().createElement("precondition"); 
		precondition.setAttribute("typeCode", "PRCN");
		stratifierCriteria.appendChild(precondition); 
		
		Element criteriaReference = processor.getOriginalDoc().createElement("criteriaReference"); 
		criteriaReference.setAttribute("moodCode", "EVN");
		criteriaReference.setAttribute("classCode", "OBS");
		precondition.appendChild(criteriaReference);
		
		Element criteriaReferenceId = processor.getOriginalDoc().createElement("id");
		criteriaReferenceId.setAttribute("root", cqlUUID);
		String extensionString = String.format("%s.\"%s\"", libraryName, riskAdjustmentDefName);
		criteriaReferenceId.setAttribute("extension", extensionString);
		criteriaReference.appendChild(criteriaReferenceId);
		component.appendChild(stratifierCriteria);
	
		return component;
		
	}
	/**
	 * Creates Logic for Each Supplemental Data Element Nodes.
	 *
	 * @param me the me
	 * @param parentNode - PopulationCriteria First Child Node.
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void createSupplementalDataElmStratifier(MeasureExport me, Node parentNode) throws XPathExpressionException {
		String xpathForOtherSupplementalQDMs = "/measure/supplementalDataElements/cqldefinition/@uuid";
		NodeList supplementalDataElements = me.getSimpleXMLProcessor().findNodeList(me.getSimpleXMLProcessor().getOriginalDoc(),
				xpathForOtherSupplementalQDMs);
		String xPathForLibraryName = "/measure/cqlLookUp/library"; 
		Node libraryNode = me.getSimpleXMLProcessor().findNode(me.getSimpleXMLProcessor().getOriginalDoc(), xPathForLibraryName);
		String libraryName = libraryNode.getTextContent();
		
		String xPathForCQLUUID = "/measure/measureDetails/cqlUUID"; 
		Node cqluuidNode = me.getSimpleXMLProcessor().findNode(me.getSimpleXMLProcessor().getOriginalDoc(), xPathForCQLUUID); 
		String cqlUUID = cqluuidNode.getTextContent(); 
		
		
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
			uuidXPathString += "@id = '" + uuidString + "' or ";
		}
		uuidXPathString = uuidXPathString.substring(0, uuidXPathString.lastIndexOf(" or "));
		String xpathforOtherSupplementalDataElements = "/measure/cqlLookUp/definitions/definition[" + uuidXPathString + "]";
		NodeList supplementalQDMNodeList = me.getSimpleXMLProcessor().findNodeList(me.getSimpleXMLProcessor().getOriginalDoc(),
				xpathforOtherSupplementalDataElements);
		if (supplementalQDMNodeList.getLength() < 1) {
			return;
		}
		
		for (int i = 0; i < supplementalQDMNodeList.getLength(); i++) {
			Node qdmNode = supplementalQDMNodeList.item(i);
			String qdmName = qdmNode.getAttributes().getNamedItem("name").getNodeValue();
			
			//createRiskAdjustmentComponentNode is good enough for this too.
		    Element componentElement = createRiskAdjustmentComponentNode(me, cqlUUID, libraryName, qdmName, "SDE");
			parentNode.appendChild(componentElement);
		}
		
	}
	
}
