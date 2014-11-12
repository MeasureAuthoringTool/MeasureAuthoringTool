package mat.server.simplexml.hqmf;

import java.util.HashMap;
import java.util.Map;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import mat.model.clause.MeasureExport;
import mat.server.util.XmlProcessor;
import mat.shared.UUIDUtilClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class HQMFPopulationLogicGenerator extends HQMFClauseLogicGenerator {
	private Map<String, String> clauseLogicMap = new HashMap<String, String>();
	private Map<String, NodeList> measureGroupingMap = new HashMap<String, NodeList>();
	private String scoringType;
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
                 7. Based on top AND/OR/ANDNOT/ORNOT/NOT generate "AllTrue", "AllFalse", "AtLeastOneTrue" tag. Generate id empty tag inside it.
                 8. Generate precondition tag typeCode="PRCN" for all children inside top Logical Op and add criteriaRef to it with id and extension.
		 **/
		getMeasureScoringType(me);
		generateClauseLogicMap(me);
		getAllMeasureGroupings(me);
		generatePopulationCriteria(me);
		return null;
	}
	
	private void generatePopulationCriteria(MeasureExport me) {
		for (String key : measureGroupingMap.keySet()){
			Node populationCriteriaComponentElement = createPopulationCriteriaSection(key , me.getHQMFXmlProcessor());
			NodeList groupingChildList = measureGroupingMap.get(key);
			for(int i=0;i< groupingChildList.getLength();i++){
				String popType = groupingChildList.item(i).getAttributes().getNamedItem(TYPE).getNodeValue();
				switch(popType) {
					case "initialPopulation" :
						generateInitialPopulationCriteria(groupingChildList.item(i),populationCriteriaComponentElement);
						break;
					default:
						//do nothing.
						break;
				}
			}
		}
		
	}
	
	/**
	 * @param item
	 * @param populationCriteriaComponentElement
	 */
	private void generateInitialPopulationCriteria(Node item, Node populationCriteriaComponentElement) {
		Document doc = populationCriteriaComponentElement.getOwnerDocument();
		Element populationCriteriaElement = (Element) populationCriteriaComponentElement.getFirstChild();
		Element componentElement = doc.createElement("component");
		componentElement.setAttribute(TYPE_CODE, "COMP");
		Element initialPopCriteriaElement = doc.createElement("initialPopulationCriteria");
		initialPopCriteriaElement.setAttribute(CLASS_CODE, "OBS");
		initialPopCriteriaElement.setAttribute(MOOD_CODE, "EVN");
		Element idElement = doc.createElement(ID);
		idElement.setAttribute(ROOT, item.getAttributes().getNamedItem(UUID).getNodeValue());
		idElement.setAttribute("extension", UUIDUtilClient.uuid());
		initialPopCriteriaElement.appendChild(idElement);
		Element codeElem = doc.createElement(CODE);
		codeElem.setAttribute(CODE, "IPOP");
		codeElem.setAttribute(CODE_SYSTEM, "2.16.840.1.113883.5.1063");
		codeElem.setAttribute(CODE_SYSTEM_NAME, "HL7 Observation Value");
		Element displayNameElement = doc.createElement(DISPLAY_NAME);
		displayNameElement.setAttribute(VALUE, item.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue());
		codeElem.appendChild(displayNameElement);
		initialPopCriteriaElement.appendChild(codeElem);
		Element preConditionElem = doc.createElement("precondition");
		preConditionElem.setAttribute(TYPE_CODE, "PRCN");
		generatePopulationLogic(preConditionElem,item);
		initialPopCriteriaElement.appendChild(preConditionElem);
		componentElement.appendChild(initialPopCriteriaElement);
		populationCriteriaElement.appendChild(componentElement);
	}
	
	/**
	 * @param preConditionElem
	 * @param item
	 */
	private void generatePopulationLogic(Element preConditionElem, Node item) {
		for(int i=0;i<item.getChildNodes().getLength();i++){
			
		}
		
	}
	
	/**
	 * @param sequenceNumber
	 * @param outputProcessor
	 * @return
	 */
	private Node createPopulationCriteriaSection(String sequenceNumber, XmlProcessor outputProcessor){
		Node componentElement = outputProcessor.getOriginalDoc().createElement("component");
		Node popCriteriaElem = outputProcessor.getOriginalDoc()
				.createElement("populationCriteriaSection");
		Element idElement = outputProcessor.getOriginalDoc()
				.createElement(ID);
		idElement.setAttribute(ROOT, "2.16.840.1.113883.3.100.1");
		idElement.setAttribute("extension", "Population" + sequenceNumber);
		popCriteriaElem.appendChild(idElement);
		Element codeElem = outputProcessor.getOriginalDoc()
				.createElement(CODE);
		codeElem.setAttribute(CODE, "57026-7");
		codeElem.setAttribute(CODE_SYSTEM, "2.16.840.1.113883.6.1");
		popCriteriaElem.appendChild(codeElem);
		Element titleElem = outputProcessor.getOriginalDoc()
				.createElement(TITLE);
		titleElem.setAttribute(VALUE, "Population Criteria Section for Grouping " + sequenceNumber);
		popCriteriaElem.appendChild(titleElem);
		// creating text for PopulationCriteria
		Element textElem = outputProcessor.getOriginalDoc()
				.createElement("text");
		textElem.setAttribute(VALUE, "Population Criteria text");
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
