package mat.model.cql.parser;

import java.util.ArrayList;
import java.util.List;

public class CQLFunctionModelObject extends CQLBaseModelDefinitionObject {
	
	List<FunctionArgument> arguments = new ArrayList<CQLFunctionModelObject.FunctionArgument>();
	
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
	
	
	
	class FunctionArgument{
		
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
