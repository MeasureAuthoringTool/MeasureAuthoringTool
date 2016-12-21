package mat.model.cql;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CQLModel implements IsSerializable{
	private CQLLibraryModel library;
	private CQLDataModel usedModel;
	private List<CQLLibraryModel> includeLibraryList = new ArrayList<CQLLibraryModel>();
	private String context;
	private List<CQLQualityDataSetDTO> valueSetList = new ArrayList<CQLQualityDataSetDTO>();
	private List<CQLQualityDataSetDTO> allValueSetList = new ArrayList<CQLQualityDataSetDTO>();
	private List<CQLParameter> cqlParameters = new ArrayList<CQLParameter>();
	private List<CQLDefinition> cqlDefinitions = new ArrayList<CQLDefinition>();
	private List<CQLFunctions> cqlFunctions = new ArrayList<CQLFunctions>();
	private List<CQLCodeSystem> codeSystemList = new ArrayList<CQLCodeSystem>();
	private List<CQLCode> codeList = new ArrayList<CQLCode>();
	
	private int lines;
	
	public CQLDataModel getUsedModel() {
		return usedModel;
	}
	public void setUsedModel(CQLDataModel usedModel) {
		this.usedModel = usedModel;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public List<CQLQualityDataSetDTO> getValueSetList() {
		return valueSetList;
	}
	public void setValueSetList(List<CQLQualityDataSetDTO> valueSetList) {
		this.valueSetList = valueSetList;
	}
	public List<CQLLibraryModel> getIncludeLibraryList() {
		return includeLibraryList;
	}
	public void setIncludeLibraryList(List<CQLLibraryModel> includeLibraryList) {
		this.includeLibraryList = includeLibraryList;
	}
	public CQLLibraryModel getLibrary() {
		return library;
	}
	public void setLibrary(CQLLibraryModel library) {
		this.library = library;
	}
	public List<CQLParameter> getCqlParameters() {
		return cqlParameters;
	}
	public void setCqlParameters(List<CQLParameter> cqlParameters) {
		this.cqlParameters = cqlParameters;
	}
	public List<CQLDefinition> getDefinitionList() {
		return cqlDefinitions;
	}
	public void setDefinitionList(List<CQLDefinition> definitionList) {
		cqlDefinitions = definitionList;
	}
	public List<CQLFunctions> getCqlFunctions() {
		return cqlFunctions;
	}
	public void setCqlFunctions(List<CQLFunctions> cqlFunctions) {
		this.cqlFunctions = cqlFunctions;
	}
	public int getLines() {
		return lines;
	}
	public void setLines(int lines) {
		this.lines = lines;
	}
	public List<CQLCodeSystem> getCodeSystemList() {
		return codeSystemList;
	}
	public void setCodeSystemList(List<CQLCodeSystem> codeSystemList) {
		this.codeSystemList = codeSystemList;
	}
	public List<CQLCode> getCodeList() {
		return codeList;
	}
	public void setCodeList(List<CQLCode> codeList) {
		this.codeList = codeList;
	}
	public List<CQLQualityDataSetDTO> getAllValueSetList() {
		return allValueSetList;
	}
	public void setAllValueSetList(List<CQLQualityDataSetDTO> allValueSetList) {
		this.allValueSetList = allValueSetList;
	}
	
}
