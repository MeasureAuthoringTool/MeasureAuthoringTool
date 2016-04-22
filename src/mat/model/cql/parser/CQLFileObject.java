package mat.model.cql.parser;

import java.util.HashMap;
import java.util.Map;

public class CQLFileObject {
	
	private Map<String, CQLDefinitionModelObject> definitionsMap = new HashMap<String, CQLDefinitionModelObject>();

	
	
	public Map<String, CQLDefinitionModelObject> getDefinitionsMap() {
		return definitionsMap;
	}



	public void setDefinitionsMap(
			Map<String, CQLDefinitionModelObject> definitionsMap) {
		this.definitionsMap = definitionsMap;
	}



	@Override
	public String toString() {
		StringBuffer stringOp = new StringBuffer();
		
		for(CQLDefinitionModelObject cqlDefinitionModelObject:this.definitionsMap.values()){
			stringOp.append(cqlDefinitionModelObject.toString()+"\r\n");
		}
		
		return stringOp.toString();
	}



	public void printHumanReadableString() {
		
		String humanReadableString = getHumanReadableForDefinition("IPPCount");
		System.out.println(humanReadableString);
		
		System.out.println("\r\n");
		
		humanReadableString = getHumanReadableForDefinition("DenominatorCount");
		System.out.println(humanReadableString);
		
		System.out.println("\r\n");
		
		humanReadableString = getHumanReadableForDefinition("DenominatorExclusionsCount");
		System.out.println(humanReadableString);
		
		System.out.println("\r\n");
		
		humanReadableString = getHumanReadableForDefinition("NumeratorCount");
		System.out.println(humanReadableString);
		
	}



	public String getHumanReadableForDefinition(String definitionName) {
		
		CQLDefinitionModelObject cqlDefinitionModelObject = this.getDefinitionsMap().get(definitionName);
		return printDefinition(cqlDefinitionModelObject, 0);
		
	}



	private String printDefinition(
			CQLDefinitionModelObject cqlDefinitionModelObject, int tabCount) {
		
		String tabString = "";
		for(int i=0;i<tabCount;i++){
			tabString += "\t";
		}
		
		StringBuffer buffer = new StringBuffer(tabString);
		
		buffer.append("\r\n\r\n" + tabString +"define "+cqlDefinitionModelObject.getAccessModifier() + " " 
				+ cqlDefinitionModelObject.getIdentifier() + " :\r\n" );
		
		buffer.append(tabString + "      ");
		for (int j=0;j<cqlDefinitionModelObject.getChildTokens().size();j++){
			buffer.append(cqlDefinitionModelObject.getChildTokens().get(j) + " ");
		}
		
		if(cqlDefinitionModelObject.getReferredToDefinitions().size() > 0){
			
			tabCount++;
			
			for(CQLDefinitionModelObject childDefinitionModelObject:cqlDefinitionModelObject.getReferredToDefinitions()){
				String childString = printDefinition(childDefinitionModelObject, tabCount);
				buffer.append(tabString + childString);
			}
			
			tabCount--;
		}
		
		return buffer.toString();
	}

}
