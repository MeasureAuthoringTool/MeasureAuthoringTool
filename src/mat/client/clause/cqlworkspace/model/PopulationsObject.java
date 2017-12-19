package mat.client.clause.cqlworkspace.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class PopulationsObject {
	
	private String populationName = "";
	private String displayName = "";
	private int sequenceNumber;
	List<PopulationClauseObject> populationClauseObjectList = new ArrayList<PopulationClauseObject>();

	
	public void addClause(Node clauseNode) {
		
		if(clauseNode == null) {
			return;
		}
		
		PopulationClauseObject populationClauseObject = new PopulationClauseObject();
		String displayName = clauseNode.getAttributes().getNamedItem("displayName").getNodeValue();
		int sequenceNumber;
		if(displayName != null && !displayName.isEmpty()) {
			sequenceNumber = Integer.parseInt(displayName.substring(displayName.lastIndexOf(" "), displayName.length()).trim());
			populationClauseObject.setSequenceNumber(sequenceNumber);
		}

		populationClauseObject.setDisplayName(displayName);
		populationClauseObject.setType(clauseNode.getAttributes().getNamedItem("type").getNodeValue());
		populationClauseObject.setUuid(clauseNode.getAttributes().getNamedItem("uuid").getNodeValue());
		
		if(clauseNode.hasChildNodes()) {
			NodeList childs = clauseNode.getChildNodes();
			
			for(int i=0;i<childs.getLength();i++) {
				
				Node child = childs.item(i);
				if(child.getNodeName().equals("cqldefinition")) {
					populationClauseObject.setCqlExpressionType(child.getNodeName());
					populationClauseObject.setCqlExpressionDisplayName(child.getAttributes().getNamedItem("displayName").getNodeValue());
					populationClauseObject.setCqlExpressionUUID(child.getAttributes().getNamedItem("uuid").getNodeValue());
					
					break;
				}else if(child.getNodeName().equals("cqlfunction")) {
					populationClauseObject.setCqlExpressionType(child.getNodeName());
					populationClauseObject.setCqlExpressionDisplayName(child.getAttributes().getNamedItem("displayName").getNodeValue());
					populationClauseObject.setCqlExpressionUUID(child.getAttributes().getNamedItem("uuid").getNodeValue());
					
					break;
				}else if(child.getNodeName().equals("cqlaggfunction")) {
					populationClauseObject.setAggFunctionName(child.getAttributes().getNamedItem("displayName").getNodeValue());
					
					//check if the agg function has a node of name "cqlfunction". It is fine if there is none.
					NodeList children = child.getChildNodes();
					
					for(int c=0; c < children.getLength(); c++) {
						Node childNode = children.item(c);
						
						if("cqlfunction".equals(childNode.getNodeName())) {
							
							populationClauseObject.setCqlExpressionType(childNode.getNodeName());
							populationClauseObject.setCqlExpressionDisplayName(childNode.getAttributes().getNamedItem("displayName").getNodeValue());
							populationClauseObject.setCqlExpressionUUID(childNode.getAttributes().getNamedItem("uuid").getNodeValue());
							
							break;
						}
					}
				}
			}
		}
		
		populationClauseObjectList.add(populationClauseObject);
	}
	
	public PopulationsObject(String name) {
		setPopulationName(name);
	}

	public String getPopulationName() {
		return populationName;
	}

	private void setPopulationName(String populationName) {
		this.populationName = populationName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public List<PopulationClauseObject> getPopulationClauseObjectList() {
		return populationClauseObjectList;
	}

	private void setPopulationClauseObjectList(List<PopulationClauseObject> populationClauseObjectList) {
		this.populationClauseObjectList = populationClauseObjectList;
	}

	public String getPopulationType() {
		String popType = null;
		if(populationName != null && !populationName.isEmpty()) {
			switch (populationName) {
			case "initialPopulations":
				popType = "Initial Population";
				break;
			case "numerators":
				popType = "Numerator";
				break;
				
			case "denominators":
				popType = "Denominator";
				break;
				
			case "measurePopulations":
				popType = "Measure Population";
				break;
				
			case "numeratorExclusions":
				popType = "Numerator Exclusion";
				break;
				
			case "denominatorExclusions":
				popType = "Denominator Exclusion";
				break;
				
			case "denominatorExceptions":
				popType = "Denominator Exception";
				break;
				
			case "measurePopulationExclusions":
				popType = "Measure Population Exclusion";
				break;
				
			case "measureObservations":
				popType = "Measure Observation";
				break;
				
			case "stratification":
				popType = "Stratification";
				break;	
				
			default:
				break;
			}
		}
		return popType;
	}

	public int getLastClauseSequenceNumber() {
		int lastSequenceNumber = 1;
		if(populationClauseObjectList.size() > 1) {
			populationClauseObjectList.sort((PopulationClauseObject pc1, PopulationClauseObject pc2)->pc1.getSequenceNumber().compareTo(pc2.getSequenceNumber()));
			lastSequenceNumber = populationClauseObjectList.get(populationClauseObjectList.size() - 1).getSequenceNumber();
		}
		return lastSequenceNumber;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
}
