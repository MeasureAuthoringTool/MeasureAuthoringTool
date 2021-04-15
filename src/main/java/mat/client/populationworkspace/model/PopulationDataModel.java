package mat.client.populationworkspace.model;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import mat.client.shared.MatContext;

import java.util.ArrayList;
import java.util.List;

public class PopulationDataModel {
	
	private static final String ARGUMENT = "argument";
	private static final String ARGUMENTS = "arguments";
	private static final String TAGNAME_STRATA = "strata";
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
	private static final String TAGNAME_MEASURE_OBSERVATIONS = "measureObservations";
	private static final String TAGNAME_ID = "id";
	
	
	private List<ExpressionObject> definitionNameList = new ArrayList<>();
	private List<ExpressionObject> functionNameList = new ArrayList<>();
	private String measureId;
	
	private PopulationsObject initialPopulationsObject = new PopulationsObject(TAGNAME_INITIAL_POPULATIONS);
	private PopulationsObject numeratorsObject = new PopulationsObject(TAGNAME_NUMERATORS);
	private PopulationsObject numeratorExclusionsObject = new PopulationsObject(TAGNAME_NUMERATOR_EXCLUSIONS);
	private PopulationsObject denominatorsObject = new PopulationsObject(TAGNAME_DENOMINATORS);
	private PopulationsObject denominatorExclusionsObject = new PopulationsObject(TAGNAME_DENOMINATOR_EXCLUSIONS);
	private PopulationsObject denominatorExceptionsObject = new PopulationsObject(TAGNAME_DENOMINATOR_EXCEPTIONS);
	private PopulationsObject measurePopulationsObject = new PopulationsObject(TAGNAME_MEASURE_POPULATIONS);
	private PopulationsObject measurePopulationsExclusionsObject = new PopulationsObject(TAGNAME_MEASURE_POPULATION_EXCLUSIONS);
	
	private StrataDataModel strataDataModel = new StrataDataModel();
	private PopulationsObject measureObservationsObject = new PopulationsObject(TAGNAME_MEASURE_OBSERVATIONS);
	
	
	public class ExpressionObject {
		private String uuid; 
		private String name; 
		
		ExpressionObject(String uuid, String name) {
			this.uuid = uuid; 
			this.name = name; 
		}
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		public String getUuid() {
			return uuid;
		}

		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
	}
	
	public PopulationDataModel(Document document) {
		this.setMeasureId(MatContext.get().getCurrentMeasureId());
		extractDefinitionNames(document);
		extractFunctionNames(document);
		loadPopulations(document);
	}
	
	public void loadPopulations(Document document) {
		
		initialPopulationsObject = new PopulationsObject(TAGNAME_INITIAL_POPULATIONS);
		numeratorsObject = new PopulationsObject(TAGNAME_NUMERATORS);
		numeratorExclusionsObject = new PopulationsObject(TAGNAME_NUMERATOR_EXCLUSIONS);
		denominatorsObject = new PopulationsObject(TAGNAME_DENOMINATORS);
		denominatorExclusionsObject = new PopulationsObject(TAGNAME_DENOMINATOR_EXCLUSIONS);
		denominatorExceptionsObject = new PopulationsObject(TAGNAME_DENOMINATOR_EXCEPTIONS);
		measurePopulationsObject = new PopulationsObject(TAGNAME_MEASURE_POPULATIONS);
		measurePopulationsExclusionsObject = new PopulationsObject(TAGNAME_MEASURE_POPULATION_EXCLUSIONS);
		
		strataDataModel = new StrataDataModel();
		measureObservationsObject = new PopulationsObject(TAGNAME_MEASURE_OBSERVATIONS);
		
		extractPopulationDetails(document);
		extractMeasureObservations(document);
		extractStratifications(document);
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

	private void extractMeasureObservations(Document document) {
		
		NodeList measureNodeList = document.getElementsByTagName("measure"); 
		
		if(measureNodeList == null || measureNodeList.getLength() == 0) {
			return;
		}
		
		Node measureNode = measureNodeList.item(0);
		
		extractPopulations(measureNode, measureObservationsObject);
		
	}
	
	private void extractStratifications(Document document) {
		NodeList strataNodeList = document.getElementsByTagName(TAGNAME_STRATA); 
		
		if(strataNodeList == null || strataNodeList.getLength() == 0) {
			return;
		}
		
		Node strataNode = strataNodeList.item(0);
		
		for(int k=0;k<strataNode.getChildNodes().getLength();k++) {
			int sequenceNumber;
			Node stratificationNode = strataNode.getChildNodes().item(k);
			String displayName = stratificationNode.getAttributes().getNamedItem(ATTRIBUTE_DISPLAY_NAME).getNodeValue();
			String uuid = stratificationNode.getAttributes().getNamedItem("uuid").getNodeValue();
			StratificationsObject stratificationsObject = new StratificationsObject("stratification", uuid);
			stratificationsObject.setDisplayName(displayName);
			
			if(displayName != null && !displayName.isEmpty()) {
				sequenceNumber = Integer.parseInt(displayName.substring(displayName.lastIndexOf(" "), displayName.length()).trim());
				stratificationsObject.setSequenceNumber(sequenceNumber);
			}
			
			NodeList clauseNodeList = stratificationNode.getChildNodes();
			if(clauseNodeList == null || clauseNodeList.getLength() == 0) {
				return;
			}
			for(int i=0;i<clauseNodeList.getLength();i++) {
				Node clauseNode = clauseNodeList.item(i);
				if(clauseNode.getNodeName().equals(TAGNAME_CLAUSE)) {
					stratificationsObject.addClause(clauseNode);
				}
			}
			strataDataModel.addStratification(stratificationsObject);
		}
	}

	private List<ExpressionObject> extractExpressionNames(Document document, String expressionParentNodeName, String expressionNodeName) {
		
		List<ExpressionObject> expressionNameList = new ArrayList<>();
		
		NodeList cqlLookUpNodeList = document.getElementsByTagName(TAGNAME_CQL_LOOK_UP);
		
		if(cqlLookUpNodeList == null || cqlLookUpNodeList.getLength() == 0) {
			return expressionNameList;
		}
		
		Node cqlLookUpNode = cqlLookUpNodeList.item(0);
		
		Node expressionsNode = findChildNode(cqlLookUpNode, expressionParentNodeName);
		
		if(expressionsNode != null) {
			NodeList expressionsNodeList = expressionsNode.getChildNodes();
			
			if(expressionsNodeList != null && expressionsNodeList.getLength() > 0) {
				
				for(int i=0;i<expressionsNodeList.getLength();i++) {
					
					Node expressionNode = expressionsNodeList.item(i);
					if(expressionNode.getNodeName().equals(expressionNodeName)) {

						boolean patientBased = MatContext.get().getCurrentMeasureInfo().isPatientBased();

						if(TAGNAME_FUNCTION.equals(expressionNodeName)) {
							if (patientBased) {
								if(getFunctionArgumentCount(expressionNode) != 0) {
									continue;
								}
							} else {
								if(getFunctionArgumentCount(expressionNode) != 1) {
									continue;
								}
							}
						}


						String name = expressionNode.getAttributes().getNamedItem(TAGNAME_NAME).getNodeValue();
						String uuid = expressionNode.getAttributes().getNamedItem(TAGNAME_ID).getNodeValue();
						
						ExpressionObject expression = new ExpressionObject(uuid, name);
						expressionNameList.add(expression);
					}
					
				}
			}
		}
		expressionNameList.sort((ExpressionObject e1, ExpressionObject e2) -> e1.getName().compareTo(e2.getName()));
		return expressionNameList;
	}

	
	/**
	 * Check for CQL Function to find out how many arguments it has. 
	 * @param expressionNode
	 * @return
	 */
	private int getFunctionArgumentCount(Node expressionNode) {
		int argCount = 0;
		
		if(TAGNAME_FUNCTION.equals(expressionNode.getNodeName())) {
			NodeList childNodes = expressionNode.getChildNodes();
			
			for(int i=0;i<childNodes.getLength();i++) {
				Node child = childNodes.item(i);
				if(ARGUMENTS.equals(child.getNodeName())) {
					NodeList argumentNodes = child.getChildNodes();
					
					for(int j=0;j<argumentNodes.getLength();j++) {
						Node argNode = argumentNodes.item(j);
						if(argNode.getNodeName().equals(ARGUMENT)) {
							argCount++;
						}
					}
				}
			}
		}
		
		return argCount;
	}

	private void extractFunctionNames(Document document) {
		
		setFunctionNameList(extractExpressionNames(document,TAGNAME_FUNCTIONS, TAGNAME_FUNCTION));		
	}

	private void extractDefinitionNames(Document document) {
		
		setDefinitionNameList(extractExpressionNames(document,TAGNAME_DEFINITIONS, TAGNAME_DEFINITION));

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

	public List<ExpressionObject> getDefinitionNameList() {
		return definitionNameList;
	}

	private void setDefinitionNameList(List<ExpressionObject> definitionNameList) {
		this.definitionNameList = definitionNameList;
	}

	public List<ExpressionObject> getFunctionNameList() {
		return functionNameList;
	}

	private void setFunctionNameList(List<ExpressionObject> functionNameList) {
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

	public StrataDataModel getStrataDataModel() {
		return strataDataModel;
	}

	public void setStrataDataModel(StrataDataModel strataDataModel) {
		this.strataDataModel = strataDataModel;
	}

	public PopulationsObject getMeasureObservationsObject() {
		return measureObservationsObject;
	}

	public void setMeasureObservationsObject(PopulationsObject measureObservationsObject) {
		this.measureObservationsObject = measureObservationsObject;
	}
}
