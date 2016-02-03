package mat.model.cql;

public class CQLDefinitionModelObject extends CQLBaseModelDefinitionObject {

	private String expression = "";
	private String id;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
	
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
	
	@Override
	/**
	 * Override this to do nothing, since Context definitions don't have a 'typeSpecifier' 
	 * attribute.
	 */
	public String getTypeSpecifier() {
		return "";
	}

	@Override
	/**
	 * Override this to do nothing, since Context definitions don't have a 'typeSpecifier' 
	 * attribute.
	 */
	public void setTypeSpecifier(String typeSpecifier) {
	}

	@Override
	/**
	 * Override this to do nothing, since Context definitions don't have a 'defaultExpression' 
	 * attribute.
	 */
	public String getDefaultExpression() {
		return "";
	}
	
	@Override
	/**
	 * Override this to do nothing, since Context definitions don't have a 'defaultExpression' 
	 * attribute.
	 */
	public void setDefaultExpression(String defaultExpression) {		
	}

}
