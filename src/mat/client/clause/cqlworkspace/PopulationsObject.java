package mat.client.clause.cqlworkspace;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class PopulationsObject {
	
	private String populationName = "";
	List<PopulationClauseObject> populationClauseObjectList = new ArrayList<PopulationClauseObject>();

	public PopulationsObject(String name) {
		setPopulationName(name);
	}

	public String getPopulationName() {
		return populationName;
	}

	private void setPopulationName(String populationName) {
		this.populationName = populationName;
	}

	public void addClause(Node clauseNode) {
		
		if(clauseNode == null) {
			return;
		}
		
		PopulationClauseObject populationClauseObject = new PopulationClauseObject();
		
		populationClauseObject.setDisplayName(clauseNode.getAttributes().getNamedItem("displayName").getNodeValue());
		populationClauseObject.setType(clauseNode.getAttributes().getNamedItem("type").getNodeValue());
		populationClauseObject.setUuid(clauseNode.getAttributes().getNamedItem("uuid").getNodeValue());
		
		if(clauseNode.hasChildNodes()) {
			NodeList childs = clauseNode.getChildNodes();
			
			for(int i=0;i<childs.getLength();i++) {
				
				Node child = childs.item(i);
				if(child.getNodeName().equals("cqldefinition")) {
					
					populationClauseObject.setCqlDefinitionDisplayName(child.getAttributes().getNamedItem("displayName").getNodeValue());
					populationClauseObject.setCqlDefinitionUUID(child.getAttributes().getNamedItem("uuid").getNodeValue());
					
					break;
				}
			}
		}
		
		populationClauseObjectList.add(populationClauseObject);
	}

}
