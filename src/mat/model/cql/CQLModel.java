package mat.model.cql;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CQLModel implements IsSerializable{
	private CQLLibraryModel library;
	private CQLDataModel usedModel;
	private List<CQLLibraryModel> includeLibraryList;
	private String context;
	private List<CQLQualityDataSetDTO> valueSetList;
	private List<CQLQualityDataSetDTO> codeList;
	private List<CQLParameter> cqlParameters;
	private List<CQLDefinition> cqlDefinitions;
	//private List<Functions> functionsList;
	
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
	public List<CQLQualityDataSetDTO> getCodeList() {
		return codeList;
	}
	public void setCodeList(List<CQLQualityDataSetDTO> codeList) {
		this.codeList = codeList;
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
		this.cqlDefinitions = definitionList;
	}

}
