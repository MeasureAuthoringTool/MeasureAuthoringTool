package mat.shared;

import java.util.ArrayList;
import java.util.List;

import mat.client.shared.GenericResult;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.parser.CQLDefinitionModelObject;
import mat.model.cql.parser.CQLFunctionModelObject;
import mat.model.cql.parser.CQLParameterModelObject;

// TODO: Auto-generated Javadoc
/**
 * The Class SaveUpdateCQLResult.
 */
public class GetUsedCQLArtifactsResult extends GenericResult{
	
	private List<String> usedCQLDefinitions; 
	
	private List<String> usedCQLFunctionss; 
	
	private List<String> usedCQLParameters;

	public List<String> getUsedCQLDefinitions() {
		return usedCQLDefinitions;
	}

	public void setUsedCQLDefinitions(List<String> usedCQLDefinitions) {
		this.usedCQLDefinitions = usedCQLDefinitions;
	}

	public List<String> getUsedCQLFunctionss() {
		return usedCQLFunctionss;
	}

	public void setUsedCQLFunctionss(List<String> usedCQLFunctionss) {
		this.usedCQLFunctionss = usedCQLFunctionss;
	}

	public List<String> getUsedCQLParameters() {
		return usedCQLParameters;
	}

	public void setUsedCQLParameters(List<String> usedCQLParameters) {
		this.usedCQLParameters = usedCQLParameters;
	} 
	
	
}
