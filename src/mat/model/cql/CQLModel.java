package mat.model.cql;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CQLModel implements IsSerializable{
	private CQLLibraryModelObject library;
	private CQLContextModelObject context;
	private List<CQLLibraryModelObject> includeLibraryList;
	private List<CQLValueSetModelObject> valueSetList = new ArrayList<CQLValueSetModelObject>();
	private List<CQLParameterModelObject> cqlParameters = new ArrayList<CQLParameterModelObject>();
	private List<CQLDefinitionModelObject> definitionList = new ArrayList<CQLDefinitionModelObject>();
	private List<CQLFunctionModelObject> functionsList = new ArrayList<CQLFunctionModelObject>();
	private List<CQLQualityDataSetDTO> codeList;
	private String cqlBuilder;
	
	private String measureId;

	public String getCqlBuilder() {
		return cqlBuilder;
	}
	public void setCqlBuilder(String cqlBuilder) {
		this.cqlBuilder = cqlBuilder;
	}
	public String getMeasureId() {
		return measureId;
	}
	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}
	public CQLLibraryModelObject getLibrary() {
		return library;
	}
	public void setLibrary(CQLLibraryModelObject library) {
		this.library = library;
	}
	public CQLContextModelObject getContext() {
		return context;
	}
	public void setContext(CQLContextModelObject context) {
		this.context = context;
	}
	public List<CQLValueSetModelObject> getValueSetList() {
		return valueSetList;
	}
	public void setValueSetList(List<CQLValueSetModelObject> valueSetList) {
		this.valueSetList = valueSetList;
	}
	public List<CQLParameterModelObject> getCqlParameters() {
		return cqlParameters;
	}
	public void setCqlParameters(List<CQLParameterModelObject> cqlParameters) {
		this.cqlParameters = cqlParameters;
	}
	public List<CQLDefinitionModelObject> getDefinitionList() {
		return definitionList;
	}
	public void setDefinitionList(List<CQLDefinitionModelObject> definitionList) {
		this.definitionList = definitionList;
	}
	public List<CQLFunctionModelObject> getFunctionsList() {
		return functionsList;
	}
	public void setFunctionsList(List<CQLFunctionModelObject> functionsList) {
		this.functionsList = functionsList;
	}
	public List<CQLLibraryModelObject> getIncludeLibraryList() {
		return includeLibraryList;
	}
	public void setIncludeLibraryList(List<CQLLibraryModelObject> includeLibraryList) {
		this.includeLibraryList = includeLibraryList;
	}
	public List<CQLQualityDataSetDTO> getCodeList() {
		return codeList;
	}
	public void setCodeList(List<CQLQualityDataSetDTO> codeList) {
		this.codeList = codeList;
	}
	public void printCQL() {
		System.out.println("CQL Model Info:");
		System.out.println(this);
		System.out.println("Library: " + library.getIdentifier() +  " " + library.getVersion() != null ? library.getVersion(): " ");
		System.out.println("Parameters: ");
		for (CQLParameterModelObject param: getCqlParameters()) {
			System.out.println(param.getIdentifier() +  " " + param.getTypeSpecifier());
		}
		System.out.println("Definitions: ");
		for (CQLDefinitionModelObject definition: getDefinitionList()) {
			System.out.println(definition.getIdentifier() +  " " + definition.getExpression());
		}
	}

}
