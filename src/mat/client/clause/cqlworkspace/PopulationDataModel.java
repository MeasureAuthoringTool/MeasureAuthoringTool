package mat.client.clause.cqlworkspace;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import mat.client.shared.MatContext;

public class PopulationDataModel {
	
	private static final String TAGNAME_NAME = "name";
	private static final String TAGNAME_CQL_LOOK_UP = "cqlLookUp";
	private static final String TAGNAME_DEFINITION = "definition";
	private static final String TAGNAME_DEFINITIONS = "definitions";
	private static final String TAGNAME_FUNCTION = "function";
	private static final String TAGNAME_FUNCTIONS = "functions";
	private static final String TAGNAME_CLAUSE = "clause";
	private static final String ATTRIBUTE_DISPLAY_NAME = "displayName";
	private static final String TAGNAME_POPULATIONS = "populations";
	private static final String TAGNAME_MEASURE_POPULATION_EXCLUSIONS = "measurePopulationExclusions";
	private static final String TAGNAME_MEASURE_POPULATIONS = "measurePopulations";
	private static final String TAGNAME_DENOMINATOR_EXCEPTIONS = "denominatorExceptions";
	private static final String TAGNAME_DENOMINATOR_EXCLUSIONS = "denominatorExclusions";
	private static final String TAGNAME_DENOMINATORS = "denominators";
	private static final String TAGNAME_NUMERATOR_EXCLUSIONS = "numeratorExclusions";
	private static final String TAGNAME_NUMERATORS = "numerators";
	private static final String TAGNAME_INITIAL_POPULATIONS = "initialPopulations";
	
	
	private List<String> definitionNameList = new ArrayList<String>();
	private List<String> functionNameList = new ArrayList<String>();
	private String measureId;
	
	private PopulationsObject initialPopulationsObject = new PopulationsObject(TAGNAME_INITIAL_POPULATIONS);
	private PopulationsObject numeratorsObject = new PopulationsObject(TAGNAME_NUMERATORS);
	private PopulationsObject numeratorExclusionsObject = new PopulationsObject(TAGNAME_NUMERATOR_EXCLUSIONS);
	private PopulationsObject denominatorsObject = new PopulationsObject(TAGNAME_DENOMINATORS);
	private PopulationsObject denominatorExclusionsObject = new PopulationsObject(TAGNAME_DENOMINATOR_EXCLUSIONS);
	private PopulationsObject denominatorExceptionsObject = new PopulationsObject(TAGNAME_DENOMINATOR_EXCEPTIONS);
	private PopulationsObject measurePopulationsObject = new PopulationsObject(TAGNAME_MEASURE_POPULATIONS);
	private PopulationsObject measurePopulationsExclusionsObject = new PopulationsObject(TAGNAME_MEASURE_POPULATION_EXCLUSIONS);
	
	public PopulationDataModel(Document document) {
		this.setMeasureId(MatContext.get().getCurrentMeasureId());
		extractDefinitionNames(document);
		extractFunctionNames(document);
		extractPopulationDetails(document);
	}

	private void extractPopulationDetails(Document document) {
		
		NodeList populationsNodeList = document.getElementsByTagName(TAGNAME_POPULATIONS);
		
		if(populationsNodeList == null || populationsNodeList.getLength() == 0) {
			return;
		}
		
		Node populationsNode = populationsNodeList.item(0);
		
		extractPopulations(populationsNode, initialPopulationsObject);
		extractPopulations(populationsNode, numeratorsObject);
		extractPopulations(populationsNode, numeratorExclusionsObject);
		extractPopulations(populationsNode, denominatorsObject);
		extractPopulations(populationsNode, denominatorExclusionsObject);
		extractPopulations(populationsNode, denominatorExceptionsObject);
		extractPopulations(populationsNode, measurePopulationsObject);
		extractPopulations(populationsNode, measurePopulationsExclusionsObject);
	}

	private void extractPopulations(Node populationsNode, PopulationsObject populationsObject) {
		
		Node populationNode = findChildNode(populationsNode, populationsObject.getPopulationName());
		
		if(populationNode == null) {
			return;
		}
		String displayName = populationNode.getAttributes().getNamedItem(ATTRIBUTE_DISPLAY_NAME).getNodeValue();
		populationsObject.setDisplayName(displayName);

		extractClauses(populationNode, populationsObject);
	}

	private void extractClauses(Node populationNode, PopulationsObject populationsObject) {
		
		NodeList clauseNodeList = populationNode.getChildNodes();
		
		if(clauseNodeList == null || clauseNodeList.getLength() == 0) {
			return;
		}
		
		for(int i=0;i<clauseNodeList.getLength();i++) {
			Node clauseNode = clauseNodeList.item(i);
			
			if(clauseNode.getNodeName().equals(TAGNAME_CLAUSE)) {
				populationsObject.addClause(clauseNode);
			}
		}
	}

	private void extractFunctionNames(Document document) {
		
		setFunctionNameList(extractExpressionNames(document,TAGNAME_FUNCTIONS, TAGNAME_FUNCTION));		
	}

	private void extractDefinitionNames(Document document) {
		
		setDefinitionNameList(extractExpressionNames(document,TAGNAME_DEFINITIONS, TAGNAME_DEFINITION));

	}

	private List<String> extractExpressionNames(Document document, String expressionParentNodeName, String expressionNodeName) {
		
		List<String> expressionNameList = new ArrayList<String>();
		
		NodeList cqlLookUpNodeList = document.getElementsByTagName(TAGNAME_CQL_LOOK_UP);
		
		if(cqlLookUpNodeList == null || cqlLookUpNodeList.getLength() == 0) {
			return expressionNameList;
		}
		
		Node cqlLookUpNode = cqlLookUpNodeList.item(0);
		
		Node definitionsNode = findChildNode(cqlLookUpNode, expressionParentNodeName);
		
		if(definitionsNode != null) {
			NodeList definitionsNodeList = definitionsNode.getChildNodes();
			
			if(definitionsNodeList != null && definitionsNodeList.getLength() > 0) {
				
				for(int i=0;i<definitionsNodeList.getLength();i++) {
					
					Node definitionNode = definitionsNodeList.item(i);
					if(definitionNode.getNodeName().equals(expressionNodeName)) {
						expressionNameList.add(definitionNode.getAttributes().getNamedItem(TAGNAME_NAME).getNodeValue());
					}
					
				}
			}
		}
		
		return expressionNameList;		
	}

	private Node findChildNode(Node node, String nodeName) {
		
		Node childNode = null;
		NodeList childNodeList = node.getChildNodes();
				
		if(childNodeList == null || childNodeList.getLength() == 0) {
			return null;
		}
		
		for(int i=0;i<childNodeList.getLength();i++) {
			Node child = childNodeList.item(i);
			if(child.getNodeName().equals(nodeName)) {
				childNode = child;
				break;
			}
		}
		
		return childNode;
	}

	public List<String> getDefinitionNameList() {
		return definitionNameList;
	}

	private void setDefinitionNameList(List<String> definitionNameList) {
		this.definitionNameList = definitionNameList;
	}

	public List<String> getFunctionNameList() {
		return functionNameList;
	}

	private void setFunctionNameList(List<String> functionNameList) {
		this.functionNameList = functionNameList;
	}
	
	public PopulationsObject getInitialPopulationsObject() {
		return initialPopulationsObject;
	}

	public PopulationsObject getNumeratorsObject() {
		return numeratorsObject;
	}

	public PopulationsObject getNumeratorExclusionsObject() {
		return numeratorExclusionsObject;
	}

	public PopulationsObject getDenominatorsObject() {
		return denominatorsObject;
	}

	public PopulationsObject getDenominatorExclusionsObject() {
		return denominatorExclusionsObject;
	}

	public PopulationsObject getDenominatorExceptionsObject() {
		return denominatorExceptionsObject;
	}

	public PopulationsObject getMeasurePopulationsObject() {
		return measurePopulationsObject;
	}

	public PopulationsObject getMeasurePopulationsExclusionsObject() {
		return measurePopulationsExclusionsObject;
	}

	public String getMeasureId() {
		return measureId;
	}

	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}


}
