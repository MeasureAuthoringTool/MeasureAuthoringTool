package mat.model.cql.parser;

import java.util.List;

import mat.model.cql.parser.CQLFunctionModelObject.FunctionArgument;

public interface CQLBaseStatementInterface {
	
	public String getIdentifier();
	public List<String> getChildTokens();
	public List<FunctionArgument> getArguments();
	public List<CQLDefinitionModelObject> getReferredToDefinitions();
	public List<CQLDefinitionModelObject> getReferredByDefinitions();
	public List<CQLFunctionModelObject> getReferredToFunctions();
	public List<CQLFunctionModelObject> getReferredByFunctions();
	public List<CQLParameterModelObject> getReferredToParameters();
	public List<CQLParameterModelObject> getReferredByParameters();
	public List<CQLValueSetModelObject> getReferredToValueSets();
	public List<CQLValueSetModelObject> getReferredByValueSets();

}
