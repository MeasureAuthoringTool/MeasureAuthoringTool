package mat.client.clause.cqlworkspace;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class PopulationDataModel {
	
	private List<String> definitionNameList = new ArrayList<String>();
	private List<String> functionNameList = new ArrayList<String>();
	
	private PopulationsObject initialPopulationsObject = new PopulationsObject("initialPopulations");
	private PopulationsObject numeratorsObject = new PopulationsObject("numerators");

	public PopulationDataModel(Document document) {
		
		extractDefinitionNames(document);
		extractFunctionNames(document);
		extractPopulationDetails(document);
	}

	private void extractPopulationDetails(Document document) {
		
		NodeList populationsNodeList = document.getElementsByTagName("populations");
		
		if(populationsNodeList == null || populationsNodeList.getLength() == 0) {
			return;
		}
		
		Node populationsNode = populationsNodeList.item(0);
		
		extractPopulations(populationsNode, initialPopulationsObject);
		extractPopulations(populationsNode, numeratorsObject);
	}

	private void extractPopulations(Node populationsNode, PopulationsObject populationsObject) {
		
		Node populationNode = findChildNode(populationsNode, populationsObject.getPopulationName());
		
		if(populationNode == null) {
			return;
		}
		
		extractClauses(populationNode, populationsObject);
	}

	private void extractClauses(Node populationNode, PopulationsObject populationsObject) {
		
		NodeList clauseNodeList = populationNode.getChildNodes();
		
		if(clauseNodeList == null || clauseNodeList.getLength() == 0) {
			return;
		}
		
		for(int i=0;i<clauseNodeList.getLength();i++) {
			Node clauseNode = clauseNodeList.item(i);
			
			if(clauseNode.getNodeName().equals("clause")) {
				populationsObject.addClause(clauseNode);
			}
		}
	}

	private void extractFunctionNames(Document document) {
		
		setFunctionNameList(extractExpressionNames(document,"functions", "function"));		
	}

	private void extractDefinitionNames(Document document) {
		
		setDefinitionNameList(extractExpressionNames(document,"definitions", "definition"));

	}

	private List<String> extractExpressionNames(Document document, String expressionParentNodeName, String expressionNodeName) {
		
		List<String> expressionNameList = new ArrayList<String>();
		
		NodeList cqlLookUpNodeList = document.getElementsByTagName("cqlLookUp");
		
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
						expressionNameList.add(definitionNode.getAttributes().getNamedItem("name").getNodeValue());
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

	private void setInitialPopulationsObject(PopulationsObject initialPopulationsObject) {
		this.initialPopulationsObject = initialPopulationsObject;
	}

	public PopulationsObject getNumeratorsObject() {
		return numeratorsObject;
	}

	private void setNumeratorsObject(PopulationsObject numeratorsObject) {
		this.numeratorsObject = numeratorsObject;
	}


}
