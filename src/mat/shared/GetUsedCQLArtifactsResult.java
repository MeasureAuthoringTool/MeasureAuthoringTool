package mat.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mat.client.shared.GenericResult;

// TODO: Auto-generated Javadoc
/**
 * The Class SaveUpdateCQLResult.
 */
public class GetUsedCQLArtifactsResult extends GenericResult{
	
	private List<String> usedCQLDefinitions = new ArrayList<String>(); 
	
	private List<String> usedCQLFunctions = new ArrayList<String>();
	
	private List<String> usedCQLParameters = new ArrayList<String>();
	
	private List<String> usedCQLValueSets = new ArrayList<String>();
	
	private List<String> usedCQLcodeSystems = new ArrayList<String>();
	
	private List<String> usedCQLcodes = new ArrayList<String>();
	
	private List<String> usedCQLLibraries = new ArrayList<String>();
	
	private Map<String, List<String>> valueSetDataTypeMap;

	public List<String> getUsedCQLDefinitions() {
		return usedCQLDefinitions;
	}

	public void setUsedCQLDefinitions(List<String> usedCQLDefinitions) {
		this.usedCQLDefinitions = usedCQLDefinitions;
	}

	public List<String> getUsedCQLFunctions() {
		return usedCQLFunctions;
	}

	public void setUsedCQLFunctions(List<String> usedCQLFunctionss) {
		this.usedCQLFunctions = usedCQLFunctionss;
	}

	public List<String> getUsedCQLParameters() {
		return usedCQLParameters;
	}

	public void setUsedCQLParameters(List<String> usedCQLParameters) {
		this.usedCQLParameters = usedCQLParameters;
	}

	public List<String> getUsedCQLValueSets() {
		return usedCQLValueSets;
	}

	public void setUsedCQLValueSets(List<String> usedCQLValueSets) {
		this.usedCQLValueSets = usedCQLValueSets;
	}

	public List<String> getUsedCQLcodeSystems() {
		return usedCQLcodeSystems;
	}

	public void setUsedCQLcodeSystems(List<String> usedCQLcodeSystems) {
		this.usedCQLcodeSystems = usedCQLcodeSystems;
	}

	public List<String> getUsedCQLcodes() {
		return usedCQLcodes;
	}

	public void setUsedCQLcodes(List<String> usedCQLcodes) {
		this.usedCQLcodes = usedCQLcodes;
	}

	public List<String> getUsedCQLLibraries() {
		return usedCQLLibraries;
	}

	public void setUsedCQLLibraries(List<String> usedCQLLibraries) {
		this.usedCQLLibraries = usedCQLLibraries;
	}

	public Map<String, List<String>> getValueSetDataTypeMap() {
		return valueSetDataTypeMap;
	}

	public void setValueSetDataTypeMap(Map<String, List<String>> valueSetDataTypeMap) {
		this.valueSetDataTypeMap = valueSetDataTypeMap;
	} 
	
	
}
