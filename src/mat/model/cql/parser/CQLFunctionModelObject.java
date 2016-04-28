package mat.model.cql.parser;

import java.util.ArrayList;
import java.util.List;

public class CQLFunctionModelObject extends CQLBaseModelDefinitionObject implements CQLBaseStatementInterface {
	
	private List<FunctionArgument> arguments = new ArrayList<CQLFunctionModelObject.FunctionArgument>();
	private List<String> childTokens = new ArrayList<String>();
	
	/**
	 * This is a list of all Definition object that are being 'referred to'/'called from' from this function.
	 */
	private List<CQLDefinitionModelObject> referredToDefinitions = new ArrayList<CQLDefinitionModelObject>();
	
	/**
	 * This is list of all definitions that this function is 'referred by'/'called by'.
	 */
	private List<CQLDefinitionModelObject> referredByDefinitions = new ArrayList<CQLDefinitionModelObject>();
	
	/**
	 * This is a list of all functions 'referred to'/'called from' from this function.
	 */
	private List<CQLFunctionModelObject> referredToFunctions = new ArrayList<CQLFunctionModelObject>();
	
	/**
	 * This is list of all functions that this function is 'referred by'/'called by'.
	 */
	private List<CQLFunctionModelObject> referredByFunctions = new ArrayList<CQLFunctionModelObject>();
	
	@Override
	/**
	 * Override this to do nothing, since Context definitions don't have a 'version'
	 * attribute.
	 */
	public String getVersion() {
		return "";
	}
	
	@Override
	/**
	 * Override this to do nothing, since Context definitions don't have a 'version'
	 * attribute.
	 */
	public void setVersion(String version) {
	}
	
	
	
	public List<String> getChildTokens() {
		return childTokens;
	}

	public void setChildTokens(List<String> childTokens) {
		this.childTokens = childTokens;
	}



	public List<FunctionArgument> getArguments() {
		return arguments;
	}

	public void setArguments(List<FunctionArgument> arguments) {
		this.arguments = arguments;
	}



	public List<CQLDefinitionModelObject> getReferredToDefinitions() {
		return referredToDefinitions;
	}

	public void setReferredToDefinitions(List<CQLDefinitionModelObject> referredToDefinitions) {
		this.referredToDefinitions = referredToDefinitions;
	}



	public List<CQLDefinitionModelObject> getReferredByDefinitions() {
		return referredByDefinitions;
	}

	public void setReferredByDefinitions(List<CQLDefinitionModelObject> referredByDefinitions) {
		this.referredByDefinitions = referredByDefinitions;
	}



	public List<CQLFunctionModelObject> getReferredToFunctions() {
		return referredToFunctions;
	}

	public void setReferredToFunctions(List<CQLFunctionModelObject> referredToFunctions) {
		this.referredToFunctions = referredToFunctions;
	}



	public List<CQLFunctionModelObject> getReferredByFunctions() {
		return referredByFunctions;
	}

	public void setReferredByFunctions(List<CQLFunctionModelObject> referredByFunctions) {
		this.referredByFunctions = referredByFunctions;
	}



	public class FunctionArgument{
		
		private String argumentName;
		private String argumentType;
		
		public String getArgumentName() {
			return argumentName;
		}
		public void setArgumentName(String argumentName) {
			this.argumentName = argumentName;
		}
		public String getArgumentType() {
			return argumentType;
		}
		public void setArgumentType(String argumentType) {
			this.argumentType = argumentType;
		}
		
		
	}

}
